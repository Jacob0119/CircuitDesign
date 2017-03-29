package com.example.lee.circuitdesign.device.component;

import android.graphics.RectF;

import com.example.lee.circuitdesign.MainResource;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Lee on 2016-11-28.
 */
public  abstract class Component implements Serializable{


        protected int InputSize;
        protected int OutputSize;

        private int GAB;
        private float locationX;
        private float locationY;
        private HashMap<String,point> Values;
        private boolean selected;
       transient RectF rectF;
        private int imageSource;


    public Component(){
        selected=false;
        Values=new HashMap<>();

    }
    public Component(float x, float y){
        this();

        this.locationX=x;
        this.locationY=y;


    }
    public Component(float x, float y,int inputNum,int outputNum,int GAB){
       this(x,y);
        this.InputSize=inputNum;
        this.OutputSize=outputNum;
        this.GAB=GAB;
        for(int i=1;i<=inputNum;i++){

            Values.put(MainResource.KeyInput+i,new point(this,x-GAB,y-GAB+50+(((GAB-50)*2)*i/(inputNum+1)),MainResource.KeyInput+i));
                                    //50은 인풋 값 점이 안 맞아서 조절한 값
        }
        if(outputNum>0) {
            Values.put(MainResource.KeyOutput, new point(this, x + GAB, y, MainResource.KeyOutput));
        }



    }

    public String[] getArray(){
        ArrayList<String> arrayList=new ArrayList<>();


            for(point a:Values.values()){
                if(!a.isConnected())
                arrayList.add(a.name);





            }


        Collections.sort(arrayList);
        return arrayList.toArray(new String[arrayList.size()]);

    }

    public String[] getArray(int type){
        ArrayList<String> arrayList=new ArrayList<>();


        for(point a:Values.values()){
            if(!a.isConnected()&&a.getType()==type)
                arrayList.add(a.name);





        }


        Collections.sort(arrayList);
        return arrayList.toArray(new String[arrayList.size()]);

    }

    public  abstract   boolean operate();

    public RectF getRectF(){
        rectF=new RectF(locationX-GAB,locationY-GAB,locationX+GAB,locationY+GAB);
        return rectF;
    }

    public boolean isSelected(){

        return  selected;
    }

    public void setSelected(boolean b){

        selected=b;
    }
    public void setImageSource(int i){

        imageSource=i;
    }

    public int getImageSource(){

        return imageSource;
    }



    public point getPoint(String s){

       return Values.get(s);

    }



        public boolean check(float touchX,float touchY){ //삭제 모드에서 사용자가 선택한 좌표 범위안에 들어가는지





                if(rectF.contains(touchX,touchY)) {
                    selected = !selected;
                    return true;
                }
            return false;


        }

    public void setDisconnect(){

        for(point p:Values.values()){

            if(p.isConnected())
                p.getWire().setSelected(true);

        }

    }



}

