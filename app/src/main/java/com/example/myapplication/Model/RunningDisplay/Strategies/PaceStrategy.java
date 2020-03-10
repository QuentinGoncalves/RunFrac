package com.example.myapplication.Model.RunningDisplay.Strategies;

import android.content.Context;

import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.RunningDisplay.RunningDisplayStrategy;
import com.example.myapplication.R;

public class PaceStrategy extends RunningDisplayStrategy {
    @Override
    public String getTitle(Context c) {
        return c.getString(R.string.running_pace);
    }

    @Override
    public String getValue(LocationHandler l, Context c) {

        double pace = unitConverter.toMinPerSec(l.getSpeed());
        String text = String.format("%.2f",pace) + c.getString(R.string.running_pace_unit);
        return text;
    }
}
