package com.example.lee.circuitdesign.device.component;

import com.example.lee.circuitdesign.MainResource;
import com.example.lee.circuitdesign.R;

/**
 * Created by Lee on 2016-12-05.
 */
public class Output extends Component{

    public Output(float x ,float y){

        super(x,y,1, 0, MainResource.OUTPUT_GAB);
        setImageSource(R.drawable.light_off);

    }

    @Override
    public boolean operate() {
        try {
            boolean result = getPoint(MainResource.KeyInput + "1").getWire().getOutputValue();
            if (result) {
                setImageSource(R.drawable.light_on);
            } else {
                setImageSource(R.drawable.light_off);
            }
            return result;

        }catch (Exception e){

            e.printStackTrace();
        }
        return false;
    }
}
