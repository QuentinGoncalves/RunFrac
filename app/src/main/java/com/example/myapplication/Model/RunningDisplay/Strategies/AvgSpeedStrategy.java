package com.example.myapplication.Model.RunningDisplay.Strategies;

import android.content.Context;

import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.RunningDisplay.RunningDisplayStrategy;
import com.example.myapplication.R;

public class AvgSpeedStrategy extends RunningDisplayStrategy {
    @Override
    public String getTitle(Context c) {
        return c.getString(R.string.running_average_speed);
    }

    @Override
    public String getValue(LocationHandler l,Context c) {
        String avgSpeed = String.format("%.2f",l.getAvgSpeed()) + c.getString(R.string.running_speed_unit);
        return avgSpeed;
    }
}
