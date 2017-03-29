package com.example.lee.circuitdesign.Add;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lee.circuitdesign.R;

/**
 * Created by Lee on 2016-11-26.
 */
public class addInputFrag extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState){
        return inflater.inflate(R.layout.add_input,container,false);
    }
}
