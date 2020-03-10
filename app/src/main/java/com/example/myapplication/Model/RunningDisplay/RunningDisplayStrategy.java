package com.example.myapplication.Model.RunningDisplay;

import android.content.Context;

import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.UnitConverter;

public abstract class RunningDisplayStrategy {

    private int layout;
    public UnitConverter unitConverter;

    public abstract String getTitle(Context c);
    public abstract String getValue(LocationHandler l,Context c);

    public RunningDisplayStrategy(){
        unitConverter = new UnitConverter();
    }

    public void setLayout(int l){
        layout = l;
    }

    public int getLayout() {
        return layout;
    }

}
