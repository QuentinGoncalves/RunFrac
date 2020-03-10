package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.myapplication.Model.LocationHandler;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.style.layers.LineLayer;
import com.mapbox.mapboxsdk.style.layers.Property;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RunStatActivity extends AppCompatActivity {

    private MapView mapView;
    private List<Point> routeCoordinates;
    private LocationHandler lh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        lh = new LocationHandler();
        ArrayList locations = getIntent().getParcelableArrayListExtra("locations");
        lh.setLocations(locations);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, "pk.eyJ1Ijoia2lsbGlhbm1hcmNoYW5kIiwiYSI6ImNrNXF2ajk0czA2YjAzb211bWRxNzB3NjYifQ.tGG4K9nljqiHDXjPmkHwAQ");

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_run_stat);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull final MapboxMap mapboxMap) {

                mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {

                        initRouteCoordinates();
                        CameraPosition position = new CameraPosition.Builder()
                                .target(new LatLng(routeCoordinates.get(0).latitude(), routeCoordinates.get(0).longitude())) // Sets the new camera position
                                .zoom(14) // Sets the zoom
                                .bearing(180) // Rotate the camera
                                .tilt(30) // Set the camera tilt
                                .build(); // Creates a CameraPosition from the builder

                        mapboxMap.animateCamera(CameraUpdateFactory
                                .newCameraPosition(position), 7000);

                        // Create the LineString from the list of coordinates and then make a GeoJSON
                        // FeatureCollection so we can add the line to our map as a layer.
                        style.addSource(new GeoJsonSource("line-source",
                                FeatureCollection.fromFeatures(new Feature[] {Feature.fromGeometry(
                                        LineString.fromLngLats(routeCoordinates)
                                )})));

                        // The layer properties for our line. This is where we make the line dotted, set the
                        // color, etc.
                        style.addLayer(new LineLayer("linelayer", "line-source").withProperties(
                                //PropertyFactory.lineDasharray(new Float[] {0.01f, 2f}),
                                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                                PropertyFactory.lineWidth(5f),
                                PropertyFactory.lineColor(getColor(R.color.red))

                        ));
                    }
                });
            }
        });

        TextView avRytm = (TextView)findViewById(R.id.avrRyhmValue);
        TextView avSpeed = (TextView)findViewById(R.id.avrSpeedValue);
        TextView maxSpeed = (TextView)findViewById(R.id.maxSpeedValue);

        double value = 60/lh.getUnitConverter().toKmperHour(lh.getAvgSpeed());
        String text = String.format("%.2f",value) + getString(R.string.running_pace_unit);
        avRytm.setText(text);

        value = lh.getUnitConverter().toKmperHour(lh.getAvgSpeed());
        text = String.format("%.2f",value) + getString(R.string.running_speed_unit);
        avSpeed.setText(text);

        maxSpeed.setText(("5,00"));

    }

    private void initRouteCoordinates() {
        // Create a list to store our line coordinates.
        routeCoordinates = new ArrayList<>();
        Iterator iter = lh.getLocations().iterator();
        while (iter.hasNext()){
            Location l = (Location) iter.next();
            routeCoordinates.add(Point.fromLngLat(l.getLongitude(), l.getLatitude()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("fromRunningActivity", true);
        startActivity(intent);
    }
}
