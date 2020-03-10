package com.example.myapplication.Model.RunningDisplay.Strategies;

import android.content.Context;

import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.RunningDisplay.RunningDisplayStrategy;
import com.example.myapplication.R;

public class AltitudeGainStrategy extends RunningDisplayStrategy {
    @Override
    public String getTitle(Context c) {
        return c.getString(R.string.running_altitude_gain);
    }

    @Override
    public String getValue(LocationHandler l, Context c) {
        double altitude = l.getAltitudeGain();
        String text = String.format("%.0f",altitude) + c.getString(R.string.running_altitude_unit);
        return text;
    }
}
