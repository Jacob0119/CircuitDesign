package com.example.lee.circuitdesign;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by Lee on 2016-11-25.
 */
public class MenuActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
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
    }


    public void mOnClick(View v){
            Intent intent;
        switch (v.getId()){

            case R.id.make:
                MainResource.IsNew=true;
                 intent=new Intent(this,MakeActivity.class);
                startActivity(intent);
                break;

            case R.id.load:
                MainResource.IsNew=false;
                 intent=new Intent(this,LoadActivity.class);
                startActivity(intent);
                break;
            case R.id.exit:
                finish();
                break;

        }

    }
}
