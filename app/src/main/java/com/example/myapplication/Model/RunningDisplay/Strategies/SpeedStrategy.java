package com.example.myapplication.Model.RunningDisplay.Strategies;

import android.content.Context;

import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.RunningDisplay.RunningDisplayStrategy;
import com.example.myapplication.R;

public class SpeedStrategy extends RunningDisplayStrategy {
    @Override
    public String getTitle(Context c) {
        return c.getString(R.string.running_speed);
    }

    @Override
    public String getValue(LocationHandler l,Context c) {

        double speed = unitConverter.toKmperHour(l.getSpeed());
        String text = String.format("%.2f",speed) + c.getString(R.string.running_speed_unit);
        return text;
    }

}
