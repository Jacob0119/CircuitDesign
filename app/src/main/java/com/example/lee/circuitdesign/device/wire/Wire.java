package com.example.lee.circuitdesign.device.wire;

import android.graphics.RectF;

import com.example.lee.circuitdesign.device.component.point;

import java.io.Serializable;

/**
 * Created by Lee on 2016-11-25.
 */
public class Wire implements Serializable{

    final int GAB=20;
    float x1,y1,x2,y2,x3;
    private boolean selected;
    transient RectF[] rectF;


    private point outputPoint,inputPoint;




    public Wire(point input,point output){
        selected=false;

        this.x1=input.getX();
        this.y1=input.getY();
        this.x2=output.getX();
        this.y2=output.getY();
        x3=x1+(x2-x1)/2f;
        outputPoint=output;
        inputPoint=input;
        input.setConnected(this);
        output.setConnected(this);
    }


    public boolean isSelected(){
        return selected;


    }
    public void setSelected(boolean b){

        selected=b;
    }
    public void setVertex(float x1,float y1,float x2,float y2){


        rectF=new RectF[3];
        rectF[0]=getRectF(x1,y1,x3,y1);
        rectF[1]=getRectF(x3,y1,x3,y2);
        rectF[2]=getRectF(x3,y2,x2,y2);
    }

    public RectF getRectF(float x1,float y1,float x2,float y2){ //선을 따라 선택할 수 있는 범위 설정


        if(x1<x2){

            return new RectF(x1,y1-GAB,x2,y2+GAB);
        }else if(x1>x2){

            return new RectF(x2,y2-GAB,x1,y1+GAB);


        }else if(x1==x2){
            if(y1<y2){

                return new RectF(x1-GAB,y1,x2+GAB,y2);

            }else{

                return new RectF(x2-GAB,y2,x1+GAB,y1);
            }
        }


        return new RectF();

    }

    public boolean check(float touchX,float touchY){ //삭제 모드에서 사용자가 선택한 좌표 범위안에 들어가는지



        for(int i=0;i<3;i++){

            if(rectF==null){
                setVertex(x1,y1,x2,y2);
            }

            if(rectF[i].contains(touchX,touchY)){

                selected=!selected;
                return true;
            }}

        return false;
    }

    public boolean getOutputValue(){

        return outputPoint.getContext().operate();
    }

    public void SetDisconnect(){

        if(inputPoint!=null&&outputPoint!=null){
            inputPoint.setConnected(false);
            inputPoint=null;



        }
        if(outputPoint!=null){
            outputPoint.setConnected(false);
            outputPoint=null;
        }


    }



}