package com.example.myapplication.Model;

import android.location.Location;
import android.widget.Chronometer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;


public class LocationHandler {

    private List<Location> locations = new ArrayList<>();
    private UnitConverter unitConverter = new UnitConverter();

    public void saveLocation(Location location){
        locations.add(location);
    }

    public double getAvgSpeed(){
        double sum = 0;
        Iterator tmp = locations.iterator();
        while (tmp.hasNext()){
            Location location = (Location) tmp.next();
             sum += unitConverter.toKmperHour(location.getSpeed());
        }
        return sum/locations.size();
    }

    public double getDistance() {
        double sum = 0;
        Iterator tmp = locations.iterator();
        while (tmp.hasNext()) {
            Location l1 = (Location) tmp.next();
            while (tmp.hasNext()) {
                Location l2 = (Location) tmp.next();
                sum += getDistanceBtw2PointInKm(l1.getLatitude(), l1.getLongitude(), l2.getLatitude(), l2.getLongitude());
                l1 = l2;
            }
        }
        return sum;
    }

    private double getDistanceBtw2PointInKm(double lat1, double lon1, double lat2, double lon2)
    {
        final int R = 6371;
        // Radius of the earth in km
        double dLat = deg2rad(lat2 - lat1);
        // deg2rad below
        double dLon = deg2rad(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;
        // Distance in km
        return d;
    }
    private double deg2rad(double deg)
    {
        return deg * (Math.PI / 180);
    }

    public  double getSpeed(){
        return locations.get(locations.size()-1).getSpeed();
    }

    public double getAltitude(){
        return locations.get(locations.size()-1).getAltitude();
    }

    public double getAltitudeGain(){
        double sum =0;
        Iterator tmp = locations.iterator();
        while (tmp.hasNext()) {
            Location l1 = (Location) tmp.next();
            while (tmp.hasNext()) {
                Location l2 = (Location) tmp.next();
                if(l1.getAltitude() < l2.getAltitude()){
                    sum += l2.getAltitude() - l1.getAltitude();
                }
                l1 = l2;
            }
        }
        return sum;
    }

    public double getAltitudeLoss(){
        double sum =0;
        Iterator tmp = locations.iterator();
        while (tmp.hasNext()) {
            Location l1 = (Location) tmp.next();
            while (tmp.hasNext()) {
                Location l2 = (Location) tmp.next();
                if(l2.getAltitude() < l1.getAltitude()){
                    sum += l1.getAltitude() - l2.getAltitude();
                }
                l1 = l2;
            }
        }
        return sum;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    public UnitConverter getUnitConverter() {
        return unitConverter;
    }

    public Hashtable toDictionary(){
        Hashtable d = new Hashtable();

        d.put("distance",getDistance());
        d.put("avgSpeed",getAvgSpeed());
        d.put("locations",getLocations());

        return d;
    }
}

