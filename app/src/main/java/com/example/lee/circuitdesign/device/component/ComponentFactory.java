package com.example.lee.circuitdesign.device.component;

import android.graphics.Color;
import android.graphics.Paint;

import com.example.lee.circuitdesign.MainResource;
import com.example.lee.circuitdesign.ManageCircuit;
import com.example.lee.circuitdesign.device.factory;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Lee on 2016-11-28.
 */
public class ComponentFactory implements factory,Serializable{

    private static ComponentFactory instance;
    private ArrayList<Component> componentArray;
    private ArrayList<Output> outputs;
    transient private Paint  mypaint;


    public static ComponentFactory getInstance(){

        if(!MainResource.IsNew){
            instance= (ComponentFactory) ManageCircuit.getInstance().getObject(ComponentFactory.class.getName());



        }else if(instance==null||MainResource.IsNew){
            instance=new ComponentFactory();



        }
        instance.setPaint();
        return instance;

    }

    public ComponentFactory(){
        outputs=new ArrayList<>();
        componentArray=new ArrayList<>();


    }
private void setPaint(){


    mypaint = new Paint();
    mypaint.setColor(Color.RED);
    mypaint.setStrokeWidth(6);
    mypaint.setStyle(Paint.Style.STROKE);
    mypaint.setStrokeJoin(Paint.Join.BEVEL);


}

    public Paint getPaint(){

        return mypaint;
    }
    @Override
    public int size() {
        return componentArray.size();
    }

    @Override
    public Component get(int i) {

        return componentArray.get(i);
    }
    public Component get(float x, float y){


        for(int i=0;i<size();i++){

            if(get(i).rectF.contains(x,y)){

                return get(i);
            }

        }
        return null;

}
    public boolean create(float x,float y,int type){

        switch (type){
            case MainResource.AND:
                componentArray.add(new And(x, y));
                break;
            case MainResource.OR:
                componentArray.add(new Or(x, y));
                break;
            case MainResource.NOT:
                componentArray.add(new Not(x, y));
                break;
            case MainResource.HIGH:
            case MainResource.LOW:
            case MainResource.CLOCK:

                componentArray.add(new InputValue(x,y,type));
                break;
            case MainResource.OUTPUT:
                Output temp=new Output(x,y);
                componentArray.add(temp);
                outputs.add(temp);
                break;

        }
        return true;

    }


    public void add(Object o) {
       Component c=(Component)o;
        componentArray.add(c);

    }

    @Override
    public void delete() {
            Component c;
       for(int i=size()-1;i>=0;i--) {
           c=get(i);
          if(c.isSelected()) {
              c.setDisconnect();
             if( outputs.contains(c)){
                 outputs.remove(c);
             }
              componentArray.remove(i);
          }

       }


    }

    @Override
    public void deleteAll() {
        componentArray.clear();
        outputs.clear();

    }

    public void start(){

        for(int i=0;i<outputs.size();i++){

            outputs.get(i).operate();
        }
    }

    public boolean select(float x,float y){

        for(Component c:componentArray){

            if(c.check(x,y))
                return  true;
        }
        return false;



    }
    public void reset(){
        for(Component c : componentArray){
            if(c.isSelected()){
                c.setSelected(false);
            }
        }

    }

    class componentPaint extends Paint implements Serializable{


    }


}
