package com.example.lee.circuitdesign;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import static com.example.lee.circuitdesign.R.id.clear_commit;

public class MakeActivity extends AppCompatActivity implements MenuItem.OnMenuItemClickListener,
        Toolbar.OnMenuItemClickListener,
        NavigationView.OnNavigationItemSelectedListener{


    MyCanvas myCanvas;
    DrawerLayout drawerLayout;
    NavigationView drawer;
    Thread executeThread;
    FloatingActionButton executeButton;
    NavigationView navigationView;
    Toolbar titleToolbar;
    Toolbar toolsToolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make);

         myCanvas=(MyCanvas)findViewById(R.id.canvas);

        drawerLayout=(DrawerLayout)findViewById(R.id.draw_layout);
        drawer=(NavigationView)findViewById(R.id.drawer);
        drawer.setNavigationItemSelectedListener(this);

        titleToolbar= (Toolbar)findViewById(R.id.makeToolbar);


        Intent intent=getIntent();
        String filename=intent.getStringExtra("filename");
        TextView title=(TextView)findViewById(R.id.circuit_title);
        title.setText(filename==null?"New Circuit":filename);

        toolsToolbar=(Toolbar)findViewById(R.id.makeTools);


        setSupportActionBar(toolsToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        executeButton=(FloatingActionButton)findViewById(R.id.execute);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.wire_menu, menu);
        //toolsToolbar.inflateMenu(R.menu.wire_menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){


        MenuItem clear_cancle=menu.findItem(R.id.clear_cancle);
        clear_cancle.setOnMenuItemClickListener(this);
        MenuItem store=menu.findItem(R.id.store);
        store.setOnMenuItemClickListener(this);
        MenuItem clear=menu.findItem(R.id.clear);
        clear.setOnMenuItemClickListener(this);
        MenuItem add=menu.findItem(R.id.add);
        add.setOnMenuItemClickListener(this);
        MenuItem wire=menu.findItem(R.id.wire_menu);
        wire.setOnMenuItemClickListener(this);
       MenuItem clear_commit=menu.findItem(R.id.clear_commit);
        clear_commit.setOnMenuItemClickListener(this);
        MenuItem clear_all=menu.findItem(R.id.clearAll);
        clear_all.setOnMenuItemClickListener(this);
        if(MyCanvas.Mode!=MainResource.CLEAR){
            menu.setGroupVisible(R.id.group_make1,true);
            menu.setGroupVisible(R.id.group_make2,false);
            executeButton.show();
        }else{
            menu.setGroupVisible(R.id.group_make1,false);
            menu.setGroupVisible(R.id.group_make2,true);
            executeButton.hide();

        }

        return super.onPrepareOptionsMenu(menu);
    }

    public void mOnClick(View v){


        switch(v.getId()){
            case R.id.execute:

                if(MainResource.IsExecuted){



                    MainResource.IsExecuted=!MainResource.IsExecuted;
                    executeButton.setImageResource(R.drawable.execute);


                }else{ //실행 중이 아닐때

                    MainResource.IsExecuted=!MainResource.IsExecuted;

                    executeButton.setImageResource(R.drawable.pause);

                    myCanvas.execute();


                    /*
                    executeThread = new Thread(myCanvas);
                    executeThread.setDaemon(true);
                    executeThread.start();
*/
                }
                break;












        }

        invalidateOptionsMenu();

    }


    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {
            case R.id.add:
                drawerLayout.openDrawer(drawer);
                Log.d("wire","add!!");
                break;
            case R.id.wire_menu:

               myCanvas.wireCreate();


            break;
            case R.id.clear_cancle:
                myCanvas.clearCancle();
                MyCanvas.Mode=MainResource.START;
                invalidateOptionsMenu();
                break;

            case  clear_commit:


                myCanvas.clear();
                break;

            case R.id.store:

                //파일 저장 권한 얻기
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if(permissionCheck!= PackageManager.PERMISSION_GRANTED){

                    if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)){



                    }else{
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
                    }

                }

                //사용자한테 회로 이름받고 저장하기
                dialog_store();




                break;
            case R.id.clear:
                MyCanvas.Mode=MainResource.CLEAR;

                invalidateOptionsMenu();
                break;

            case R.id.clearAll:
                new AlertDialog.Builder(this).setTitle("전체 삭제")
                        .setMessage("모든 디바이스를 삭제 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               myCanvas.clearAll();


                    }
                }).setNegativeButton("취소", null).show();

                break;


        }
        return false;
    }

    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:



                finish();

                return true;

        }

        return super.onOptionsItemSelected(item);



    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){



            case R.id.inputZero:
                MyCanvas.Mode=MainResource.LOW;

                drawerLayout.closeDrawer(drawer);
                break;

            case R.id.inputOne:
                MyCanvas.Mode=MainResource.HIGH;

                drawerLayout.closeDrawer(drawer);
                break;

            case R.id.inputClock:
                MyCanvas.Mode=MainResource.CLOCK;

                drawerLayout.closeDrawer(drawer);
                break;

            case R.id.and:
                MyCanvas.Mode=MainResource.AND;

                drawerLayout.closeDrawer(drawer);
                break;
            case R.id.or:
                MyCanvas.Mode=MainResource.OR;

                drawerLayout.closeDrawer(drawer);
                break;
            case R.id.not:
                MyCanvas.Mode=MainResource.NOT;

                drawerLayout.closeDrawer(drawer);
                break;

            case R.id.output:

                MyCanvas.Mode=MainResource.OUTPUT;
                drawerLayout.closeDrawer(drawer);
                break;


        }
        return false;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults){

    switch (requestCode){


    }

}

    public void dialog_store(){


        if(MainResource.IsNew) {
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
            final View layout = inflater.inflate(R.layout.dialog_store_item, (ViewGroup) findViewById(R.id.dialog_store_layout));
            //다이얼로그에 넣을 레이아웃 생성

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("저장")
                    .setView(layout)
                    .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText filename = (EditText) layout.findViewById(R.id.edit_filename);
                            String name=filename.getText().toString();

                            if(name.trim().equals("")){ //회로 이름을 빈칸으로 입력했을 때
                                new AlertDialog.Builder(MakeActivity.this).setMessage("회로 이름을 정하세요")
                                        .setNegativeButton("확인", new DialogInterface.OnClickListener(){

                                    public void onClick(DialogInterface dialog,int which){
                                        dialog_store(); //다시 이름 부르는 다이얼로그 부르기
                                    }
                                })
                                .create().show();
                            }else{

                                 ManageCircuit.getInstance().store(filename.getText().toString());

                                Snackbar.make(myCanvas,"저장완료",Snackbar.LENGTH_SHORT).show();
                                //Toast.makeText(MakeActivity.this,"저장완료",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).setNegativeButton("취소", null).create().show();
        }

        else{

            new AlertDialog.Builder(this).setTitle("덮어쓰기")
                    .setMessage("덮어 쓰시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            ManageCircuit.getInstance().store();


                        }
                    })
                    .setNegativeButton("아니오",null).create().show();

        }
    }



}


