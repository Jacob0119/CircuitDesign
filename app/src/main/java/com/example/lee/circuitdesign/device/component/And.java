package com.example.lee.circuitdesign.device.component;

import com.example.lee.circuitdesign.MainResource;
import com.example.lee.circuitdesign.R;

/**
 * Created by Lee on 2016-11-30.
 */
public class And extends Component {



public And(float x,float y){

    super(x,y,2, 1,MainResource.GATE_GAB);
    setImageSource(R.drawable.and);

}

    @Override
    public boolean operate(){
        boolean[] temp=new boolean[2];
       for(int i=1;i<=this.InputSize;i++){
          temp[i-1]=  getPoint(MainResource.KeyInput+i).getWire().getOutputValue();



       }
        return temp[0]&temp[1];


    }

}
