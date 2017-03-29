package com.example.lee.circuitdesign;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.SparseBooleanArray;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.lee.circuitdesign.R.id.filename;

public class LoadActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,MenuItem.OnMenuItemClickListener
{

    //ListView listView;
    //CustomAdapter adapter;


    private int choiceMode=ListView.CHOICE_MODE_SINGLE;
    RecyclerView recyclerView;
    MyAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    android.support.v7.view.ActionMode actionMode;
    CollapsingToolbarLayout collapsingToolbarLayout;
    Toolbar toolbar;
    FloatingActionButton delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_load);



        delete=(FloatingActionButton)findViewById(R.id.load_floating);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.removeData();
            }
        });
        delete.hide();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerViewOnItemClickListener(this, recyclerView,
                new RecyclerViewOnItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        if(choiceMode==ListView.CHOICE_MODE_SINGLE) {//일반모드일때
                            adapter.checked(position);
                            TextView file = (TextView) v.findViewById(filename);
                            String filename = file.getText().toString();
                            ManageCircuit.getInstance().load(filename);
                            Intent intent = new Intent(LoadActivity.this, MakeActivity.class);
                            intent.putExtra("filename", filename);
                            startActivity(intent);
                        }else if(choiceMode==ListView.CHOICE_MODE_MULTIPLE){//다중 모드
                            myToggleSelection(position);




                        }
                    }

                    @Override
                    public void onItemLongClick(View v, int position) {


                       if(choiceMode==ListView.CHOICE_MODE_SINGLE){
                             setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                           invalidateOptionsMenu();
                            myToggleSelection(position);
                           adapter.notifyDataSetChanged();
                        }
                    }
                }
        ));


        layoutManager =new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new MyAdapter();
        recyclerView.setAdapter(adapter);
        toolbar=(Toolbar)findViewById(R.id.loadToolbar);
        setSupportActionBar(toolbar);
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingToolbarLayout);
            collapsingToolbarLayout.setTitle("Load Circuit");

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



    }
    private void myToggleSelection(int idx) {
        adapter.toggleSelection(idx);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){


        getMenuInflater().inflate(R.menu.load_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu){

        MenuItem edit_menu=menu.findItem(R.id.load_edit);
        edit_menu.setOnMenuItemClickListener(this);
        MenuItem clear_menu=menu.findItem(R.id.load_delete);
        clear_menu.setOnMenuItemClickListener(this);
        MenuItem complete_menu=menu.findItem(R.id.load_complete);
        complete_menu.setOnMenuItemClickListener(this);



       if(this.getChoiceMode()==ListView.CHOICE_MODE_SINGLE){
          edit_menu.setVisible(true);
           clear_menu.setVisible(false);
           complete_menu.setVisible(false);
           delete.hide();

       }else if(this.getChoiceMode()==ListView.CHOICE_MODE_MULTIPLE){
           edit_menu.setVisible(false);
           clear_menu.setVisible(true);
           complete_menu.setVisible(true);
           delete.show();
       }


        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){

            case R.id.load_edit: //편집 누를 때
                this.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);


                break;
            case R.id.load_complete:// Complete button Click
                this.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                adapter.clearSelections();
                adapter.notifyDataSetChanged();

                break;
            case R.id.load_delete:
                adapter.removeData();

               break;

        }
        invalidateOptionsMenu();//상태바 메뉴 변경
        adapter.notifyDataSetChanged();//리스트 모양 바꾸기
        return false;

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
/*
        if(listView.getChoiceMode()==ListView.CHOICE_MODE_SINGLE) {
            TextView file = (TextView) view.findViewById(R.id.filename);
            String filename = file.getText().toString();
            ManageCircuit.getInstance().load(filename);
            Intent intent = new Intent(this, MakeActivity.class);
            intent.putExtra("filename",filename);
            startActivity(intent);

           // Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();
        }
*/
    }
    public boolean onOptionsItemSelected(android.view.MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                // NavUtils.navigateUpFromSameTask(this);

                finish();

                return true;

        }

        return super.onOptionsItemSelected(item);



    }

    public int getChoiceMode(){
        return this.choiceMode;
    }
    public void setChoiceMode(int Mode){
        this.choiceMode=Mode;
    }





/*
    public class CustomAdapter extends BaseAdapter{
            private List<FileInfo> list;
            private LayoutInflater mLayoutInflater;
        public CustomAdapter(){

            list=ManageCircuit.getInstance().getFileList();
            Log.d("Load",list.size()+"");
        }


            @Override
            public View getView(int position,View convertView,ViewGroup parent){
                Log.d("Load","call");
                final Context context=parent.getContext();
                FileInfo item=getItem(position);
                if(null==convertView){

                    mLayoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView=(MyItem)mLayoutInflater.inflate(R.layout.item_list,parent,false);
                }

                TextView filename=(TextView)convertView.findViewById(R.id.filename);
                filename.setText(item.getFileName());

                TextView filedate=(TextView)convertView.findViewById(R.id.filedate);
                filedate.setText(item.getDate());
                CheckBox cb=(CheckBox)convertView.findViewById(R.id.checkbox);
                if(listView.getChoiceMode()==ListView.CHOICE_MODE_MULTIPLE){

                    cb.setVisibility(View.VISIBLE);
                }else{
                    cb.setVisibility(View.INVISIBLE);
                }



                return convertView;

            }


        @Override
        public int getCount() {
            return list.size();
        }

        public FileInfo getItem(int i){
              return list.get(i);
            }
        public void removeItem(int position){
            FileInfo fileInfo=getItem(position);
            list.remove(position);
            ManageCircuit.getInstance().delete(fileInfo.getFileName());
        }

@Override
        public long getItemId(int position){
           return position;
        }



}
*/

class MyAdapter extends  RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    List<FileInfo> list;

    private SparseBooleanArray selectedItems;
    MyAdapter(){

        list=ManageCircuit.getInstance().getFileList();
        selectedItems=new SparseBooleanArray();
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


       View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list,parent,false);
        MyViewHolder holder=new MyViewHolder(v);
       return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        FileInfo fileInfo=getItem(position);

        holder.title.setText(fileInfo.getFileName());
        holder.date.setText(fileInfo.getDate());

        holder.itemView.setActivated(selectedItems.get(position, false));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public FileInfo getItem(int i){return list.get(i);}

    //toggle
    public void toggleSelection(int pos){
        if(this.selectedItems.get(pos,false)){
            selectedItems.delete(pos);
        }else{
            selectedItems.put(pos,true);
        }
        notifyItemChanged(pos);
    }
    public void clearSelections(){
        selectedItems.clear();
        notifyDataSetChanged();
    }
    public int getSelectedItemCount(){
        return  this.selectedItems.size();
    }
    public List<Integer> getSelectedItems(){
        List<Integer> items=new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
    public void removeData(){


        List<Integer> tempList=getSelectedItems();
        for(int i=tempList.size()-1;i>=0;i--){
            removeData(tempList.get(i));

        }
        clearSelections();
        Snackbar.make(recyclerView,"삭제 완료",Snackbar.LENGTH_SHORT).show();


    }
    public void removeData(int position) {

        FileInfo fileInfo=getItem(position);
        list.remove(position);
        ManageCircuit.getInstance().delete(fileInfo.getFileName());
        notifyItemRemoved(position);
    }
    public void checked(int pos){

    }

class MyViewHolder extends RecyclerView.ViewHolder{
   public TextView title;
    public TextView date;

    public MyViewHolder(View itemView) {
        super(itemView);
        title=(TextView)itemView.findViewById(R.id.filename);
         date=(TextView)itemView.findViewById(R.id.filedate);




    }
}
}





    public static class RecyclerViewOnItemClickListener extends RecyclerView.SimpleOnItemTouchListener {

        private OnItemClickListener mListener;
        private GestureDetector mGestureDetector;
        public RecyclerViewOnItemClickListener(Context context, final RecyclerView recyclerView, OnItemClickListener listener) {
            this.mListener = listener;

            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View childView = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if(childView != null && mListener != null){

                        mListener.onItemLongClick(childView, recyclerView.getChildAdapterPosition(childView));
                    }
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
                mListener.onItemClick(child, rv.getChildAdapterPosition(child));
                return true;
            }
            return false;
        }

        public interface OnItemClickListener {
            void onItemClick(View v, int position);
            void onItemLongClick(View v, int position);
        }
    }
}


