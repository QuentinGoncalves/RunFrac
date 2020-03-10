package com.example.myapplication.Model.RunningDisplay.Strategies;

import android.content.Context;

import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.RunningDisplay.RunningDisplayStrategy;
import com.example.myapplication.R;

public class AvgPaceStrategy extends RunningDisplayStrategy {
    @Override
    public String getTitle(Context c) {
        return c.getString(R.string.running_average_pace);
    }

    @Override
    public String getValue(LocationHandler l, Context c) {
        double avgPace = 60/unitConverter.toKmperHour(l.getAvgSpeed());
        String text = String.format("%.2f",avgPace) + c.getString(R.string.running_pace_unit);
        return text;
    }
}
