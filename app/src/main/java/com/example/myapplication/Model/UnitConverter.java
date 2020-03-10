package com.example.myapplication.Model;

import java.text.DecimalFormat;

public class UnitConverter {

    private DecimalFormat df = new DecimalFormat("#.##");
    public double toKmperHour(double speed){
        if (speed < 1){
            return 0.00;
        }
        return speed * 3.6;
    }

    public double toMinPerSec(double speed){
        df.format(speed);
        if (speed < 1){
            return 0.00;
        }
        return 16.6666666/speed;
    }
}
