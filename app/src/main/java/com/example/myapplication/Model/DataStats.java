package com.example.myapplication.Model;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

public class DataStats {
    private List<Hashtable> stats = new ArrayList<>();
    private int runIndex;

    public void addData(Hashtable d){
        stats.add(d);
    }

    public  ArrayList<Location> getLocations(){
        return (ArrayList<Location>) stats.get(runIndex).get("locations");
    }

    public double getDistance(){
        return (double) stats.get(runIndex).get("distance");
    }

    public double getAvgSpeed(){
        return (double) stats.get(runIndex).get("avgspeed");
    }

    public double getAvgPace(){
        return (double) stats.get(runIndex).get("avgpace");
    }

    public void setRunIndex(int runIndex) {
        this.runIndex = runIndex;
    }

    public List<Hashtable> getStats() {
        return stats;
    }

    public void reverseStats(){
        Collections.reverse(stats);
    }
}
