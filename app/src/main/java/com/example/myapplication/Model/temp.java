//package com.example.myapplication;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.appcompat.widget.Toolbar;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import android.Manifest;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//
//import android.location.Location;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.myapplication.Model.AveragePaceCalculator;
//import com.example.myapplication.Model.LocationUpdatesService;
//import com.example.myapplication.Model.UnitConverter;
//import com.example.myapplication.Model.Utils;
//
//
//import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
//
//public class RunningActivity extends AppCompatActivity implements
//        SharedPreferences.OnSharedPreferenceChangeListener {
//
//    private LinearLayout parentLinearLayout;
//
//    public TextView tSpeed;
//    public TextView tPace;
//    public TextView tAvgPace;
//
//    private int requestCode = 136;
//
//
//    private UnitConverter unitConverter;
//    private AveragePaceCalculator averagePaceCalculator;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_running);
//        Toolbar toolbar = findViewById(R.id.toolbar2);
//        setSupportActionBar(toolbar);
//        parentLinearLayout = (LinearLayout) findViewById(R.id.running_buttons);
//
//        CircularProgressIndicator circularProgress = findViewById(R.id.circular_progress);
//        circularProgress.setProgress(0.25, 1);
//
//        Boolean interval = true;
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            interval = extras.getBoolean("interval");
//        }
//        if(!interval){
//            onDelete(findViewById(R.id.running_start_interval));
//        }
//
//        Button pause = findViewById(R.id.running_pause);
//        pause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            }
//        });
//
//        unitConverter = new UnitConverter();
//        averagePaceCalculator = new AveragePaceCalculator();
//
//        tSpeed = findViewById(R.id.running_speed_value);
//        tPace = findViewById(R.id.running_pace_value);
//        tAvgPace = findViewById(R.id.running_avg_pace_value);
//
//        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION,requestCode);
//        checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,requestCode);
//        checkPermission(Manifest.permission.INTERNET,requestCode);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        moveTaskToBack(true);
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(true);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//    public void onDelete(View v) {
//        parentLinearLayout.removeView((View) v);
//    }
//
//    // Function to check and request permission
//    public void checkPermission(String permission, int requestCode)
//    {
//
//        // Checking if permission is not granted
//        if (ContextCompat.checkSelfPermission(
//                this,
//                permission)
//                == PackageManager.PERMISSION_DENIED) {
//            ActivityCompat
//                    .requestPermissions(
//                            this,
//                            new String[] { permission },
//                            requestCode);
//        }
//        else {
//
//        }
//    }
//
//
//
//
//}
//
///**
// * Receiver for broadcasts sent by {@link LocationUpdatesService}.
// */
//private class MyReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
//        if (location != null) {
//            Toast.makeText(RunningActivity.this, Double.toString(location.getSpeed())+ "m/s",
//                    Toast.LENGTH_SHORT).show();
//        }
//    }
//}
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
//        // Update the buttons state depending on whether location updates are being requested.
//        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
//            setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
//                    false));
//        }
//    }
//
//    private void setButtonsState(boolean requestingLocationUpdates) {
//
//    }
//}
