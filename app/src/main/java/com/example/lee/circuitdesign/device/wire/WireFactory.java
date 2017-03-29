package com.example.lee.circuitdesign.device.wire;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.example.lee.circuitdesign.MainResource;
import com.example.lee.circuitdesign.ManageCircuit;
import com.example.lee.circuitdesign.device.component.Component;
import com.example.lee.circuitdesign.device.component.point;
import com.example.lee.circuitdesign.device.factory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Lee on 2016-11-25.
 */
public class WireFactory implements factory,Serializable {

    private static WireFactory wireFactory;
    private ArrayList<Wire> wireArray;  //wire를 담고 있는 리스트
    public static boolean START;
   transient private Paint mPaint;
   transient private Paint mPaint2;//선택된 인풋 아웃풋을 위해서
    transient Wire wire;

    transient   private point input,output;

    public static WireFactory getInstance(){

        if(!MainResource.IsNew){

            wireFactory=(WireFactory) ManageCircuit.getInstance().getObject(WireFactory.class.getName());

            Log.d("WireFctory",wireFactory.size()+"");

        }else if(wireFactory==null||MainResource.IsNew){

            wireFactory=new WireFactory();

        }
        wireFactory.setPaint();
        return wireFactory;
    }
    private WireFactory(){

        wireArray=new ArrayList<>();
        START=false;


    }
    private void setPaint(){
        mPaint=new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint2=new Paint();
        mPaint2.setColor(Color.RED);

        mPaint.setAntiAlias(true);

    }

    public  int size(){ //array 사이즈

        return wireArray.size();
    }

    public  Wire get(int i){ //wire 가져오기

        return wireArray.get(i);
    }

    private void add(Wire w){ //wire 넣기 생성 제거는 오로지 WireFactory에서 관여할 일이므로 private

        wireArray.add(w);
    }


    public boolean create(){
            if(input!=null&&output!=null){
                    wire=new Wire(input,output);
                    add(wire);
                    input=null;
                    output=null;
                    return true;

            }else {
                return false;
}

    }
    @Override
    public void add(Object o) {
        Wire w=(Wire)o;
        wireArray.add(w);
    }

    @Override
    public void delete() {

        for (int i = size()-1; i >= 0; i--) {

            if(get(i).isSelected()){
                get(i).SetDisconnect();
                wireArray.remove(i);
            }

        }


    }
    @Override
    public void deleteAll(){
        input=null;
        output=null;
        wireArray.clear();
    }



    public Paint getWirePaint(){

        return mPaint;

    }
    public Paint getSelectedPaint(){
        return mPaint2;
    }
    public void setColor(int c){

        mPaint.setColor(c);
    }

    public Path getWire(int i){

        Path w=new Path();


            Wire temp = get(i);
            if (temp.isSelected()){
                setColor(Color.RED);
            }else{
                setColor(Color.BLACK);
            }

            w.reset();

            w.moveTo(temp.x1, temp.y1);
            w.addCircle(temp.x1, temp.y1, 5, Path.Direction.CW);
            w.lineTo(temp.x3, temp.y1);
            w.moveTo(temp.x3, temp.y1);
            w.lineTo(temp.x3, temp.y2);
            w.moveTo(temp.x3, temp.y2);
            w.lineTo(temp.x2, temp.y2);
            w.addCircle(temp.x2, temp.y2, 5, Path.Direction.CW);





        return w;

    }

    public Path getSelectedPoint(){
        Path p=new Path();
        if(input!=null)
        p.addCircle(input.getX(), input.getY(), 8, Path.Direction.CW);
        if(output!=null)
        p.addCircle(output.getX(), output.getY(), 8, Path.Direction.CW);
        return p;
    }

    public void setValus(Component c,String s){


           point p= c.getPoint(s);

        if(p.getType()== MainResource.INPUT){ //input 포인트이면
           if(output!=null&&output.getContext()==p.getContext())  //같은 컴포턴트 인풋 아웃풋 선택방지
               output=null;


            this.input=p;


        }else if(p.getType()==MainResource.OUTPUT){
            if(input!=null&&input.getContext()==p.getContext())
                input=null;


            this.output=p;
        }


    }

    public boolean select(float x ,float y){

        for(Wire w:wireArray){

            if(w.check(x,y))
                return true;


        }
        return false;
    }

public void reset(){

    for(Wire w : wireArray){
        if(w.isSelected()){
            w.setSelected(false);
        }
    }
}



}
