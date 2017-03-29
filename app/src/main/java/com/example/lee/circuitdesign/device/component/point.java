package com.example.lee.circuitdesign.device.component;

import com.example.lee.circuitdesign.MainResource;
import com.example.lee.circuitdesign.device.wire.Wire;

import java.io.Serializable;

/**
 * Created by Lee on 2016-12-05.
 */
public class point implements Serializable{
    private Component context;
    private int type;
    float x,y;
    private boolean connected;
    String name;

    private Wire wire;
    point(Component context,float x,float y,String s){

        this.context=context;
        connected=false;
        this.name=s;
        if(name.contains("Input")){

            type= MainResource.INPUT;

        }else{
            type=MainResource.OUTPUT;
        }
        this.x=x;
        this.y=y;


    }

    public int getType(){
        return type;
    }
    public String toString(){
        return name;
    }

    public void setConnected(Wire w){



        this.wire=w;
        this.connected=true;
    }

    public void setConnected(boolean b){

        if(!b){
        this.wire=null;}
        this.connected=b;


    }
    public void setWire(Wire w){
        this.wire=w;

    }
    public Wire getWire(){
        return this.wire;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }
    public boolean isConnected(){
        return this.connected;
    }
    public Component getContext(){
        return this.context;
    }

}