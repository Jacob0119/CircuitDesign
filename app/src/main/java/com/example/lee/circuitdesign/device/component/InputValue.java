package com.example.lee.circuitdesign.device.component;

import com.example.lee.circuitdesign.MainResource;
import com.example.lee.circuitdesign.R;

/**
 * Created by Lee on 2016-12-05.
 */
public class InputValue extends Component {


    private int type;
    private boolean clock;

    public InputValue(float x,float y,int Type){

        super(x, y, 0,1,MainResource.INPUT_GAB);
        switch (Type){

            case MainResource.HIGH:
                setImageSource(R.drawable.one);
                type=Type;
                break;
            case MainResource.LOW:
                setImageSource(R.drawable.zero);
                type=Type;
                break;
            case MainResource.CLOCK:
                setImageSource(R.drawable.clock);
                clock=false;
                type=Type;
                break;




        }


    }
    @Override
    public boolean operate() {

        switch (type){
            case MainResource.HIGH:
                return true;

            case MainResource.LOW:
                return false;

            case MainResource.CLOCK:
                return clock=!clock;



        }

        return false;

    }
}
