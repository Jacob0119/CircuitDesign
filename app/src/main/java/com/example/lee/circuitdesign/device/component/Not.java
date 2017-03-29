package com.example.lee.circuitdesign.device.component;

import com.example.lee.circuitdesign.MainResource;
import com.example.lee.circuitdesign.R;

/**
 * Created by Lee on 2016-12-05.
 */
public class Not extends Component {

    public Not(float x,float y){
        super(x,y,1,1, MainResource.GATE_GAB);
        setImageSource(R.drawable.not);
    }

    @Override
    public boolean operate() {
        return !getPoint(MainResource.KeyInput+"1").getWire().getOutputValue();
    }
}
