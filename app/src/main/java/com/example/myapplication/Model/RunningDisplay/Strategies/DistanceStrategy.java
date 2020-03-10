package com.example.myapplication.Model.RunningDisplay.Strategies;

import android.content.Context;

import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.RunningDisplay.RunningDisplayStrategy;
import com.example.myapplication.R;

public class DistanceStrategy extends RunningDisplayStrategy {
    @Override
    public String getTitle(Context c) {
        return c.getString(R.string.running_distance);
    }

    @Override
    public String getValue(LocationHandler l,Context c) {
        String distance = String.format("%.2f",l.getDistance()) + c.getString(R.string.running_distance_unit);
        return distance;
    }
}
