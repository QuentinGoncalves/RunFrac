/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.myapplication;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.myapplication.Model.DataStats;
import com.example.myapplication.Model.LocationHandler;
import com.example.myapplication.Model.LocationUpdatesService;
import com.example.myapplication.Model.RunningDisplay.RunningDisplayFactory;
import com.example.myapplication.Model.UnitConverter;
import com.example.myapplication.Model.Utils;
import com.example.myapplication.ui.RunningMenuFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;


;import java.io.Serializable;
import java.util.ArrayList;

/**
 * The only activity in this sample.
 *
 * Note: Users have three options in "Q" regarding location:
 * <ul>
 *     <li>Allow all the time</li>
 *     <li>Allow while app is in use, i.e., while app is in foreground</li>
 *     <li>Not allow location at all</li>
 * </ul>
 * Because this app creates a foreground service (tied to a Notification) when the user navigates
 * away from the app, it only needs location "while in use." That is, there is no need to ask for
 * location all the time (which requires additional permissions in the manifest).
 *
 * "Q" also now requires developers to specify foreground service type in the manifest (in this
 * case, "location").
 *
 * Note: For Foreground Services, "P" requires additional permission in manifest. Please check
 * project manifest for more information.
 *
 * Note: for apps running in the background on "O" devices (regardless of the targetSdkVersion),
 * location may be computed less frequently than requested when the app is not in the foreground.
 * Apps that use a foreground service -  which involves displaying a non-dismissable
 * notification -  can bypass the background location limits and request location updates as before.
 *
 * This sample uses a long-running bound and started service for location updates. The service is
 * aware of foreground status of this activity, which is the only bound client in
 * this sample. After requesting location updates, when the activity ceases to be in the foreground,
 * the service promotes itself to a foreground service and continues receiving location updates.
 * When the activity comes back to the foreground, the foreground service stops, and the
 * notification associated with that foreground service is removed.
 *
 * While the foreground service notification is displayed, the user has the option to launch the
 * activity from the notification. The user can also remove location updates directly from the
 * notification. This dismisses the notification and stops the service.
 */
public class RunningActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener, RunningMenuFragment.OnFragmentInteractionListener {
    private static final String TAG = RunningActivity.class.getSimpleName();

    // Used in checking for runtime permissions.
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // The BroadcastReceiver used to listen from broadcasts from the service.
    private MyReceiver myReceiver;

    // A reference to the service used to get location updates.
    private LocationUpdatesService mService = null;

    // Tracks the bound state of the service.
    private boolean mBound = false;

    //Interval training
    private boolean interval = true;

    // UI elements.
    private LinearLayout mButtonsLinearLayout;
    private Button mPauseButton;
    private Button mEndButton;

    private boolean running = true;
    private boolean created = false;

    private LinearLayout mLayout1;
    private LinearLayout mLayout2;
    private LinearLayout mLayout3;
    private LinearLayout mLayout4;

    // Class that calcul average (speed/pace...)
    private LocationHandler locationHandler;
    private RunningDisplayFactory runningDisplayFactory;

    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            mService.putAvgCalculatorExtra(locationHandler);
            mService.requestLocationUpdates();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myReceiver = new MyReceiver();
        setContentView(R.layout.activity_running);

        //Model class
        locationHandler = new LocationHandler();
        runningDisplayFactory = new RunningDisplayFactory();

        // Check that the user hasn't revoked permissions by going to Settings.
        if (Utils.requestingLocationUpdates(this)) {
            if (!checkPermissions()) {
                requestPermissions();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);


        // UI Management
        mButtonsLinearLayout = findViewById(R.id.running_buttons);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            interval = extras.getBoolean("interval");
            getIntent().putExtra("interval",interval);
            if(extras.containsKey("locations")){
                locationHandler.setLocations(extras.<Location>getParcelableArrayList("locations"));
            }

        }
        if(!interval){
            onDelete(findViewById(R.id.running_start_interval));
        }


        /**  Pour le changement d'affichage (stategy pattern)
         *   connaitre le layout cliker **/

        View.OnLongClickListener longClickListener = new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                RunningMenuFragment fragment = new RunningMenuFragment();
                Bundle b = new Bundle();
                b.putInt("layout",v.getId());
                fragment.setArguments(b);
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.activity_running,fragment);
                transaction.addToBackStack(null);
                transaction.commit();
                return true;
            }

        };



        mLayout1 = findViewById(R.id.running_layout1);
        mLayout1.setOnLongClickListener(longClickListener);
        TextView tv = (TextView) mLayout1.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout1).getTitle(this));

        mLayout2 = findViewById(R.id.running_layout2);
        mLayout2.setOnLongClickListener(longClickListener);
        tv = (TextView) mLayout2.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout2).getTitle(this));

        mLayout3 = findViewById(R.id.running_layout3);
        mLayout3.setOnLongClickListener(longClickListener);
        tv = (TextView) mLayout3.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout3).getTitle(this));

        mLayout4 = findViewById(R.id.running_layout4);
        mLayout4.setOnLongClickListener(longClickListener);
        tv = (TextView) mLayout4.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout4).getTitle(this));

        mPauseButton = findViewById(R.id.running_pause);
        mEndButton = findViewById(R.id.running_end);

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button v = (Button) view;
                if(running){
                    mService.removeLocationUpdates();
                    v.setBackgroundColor(getResources().getColor(R.color.green));
                    v.setText(getString(R.string.running_resume));
                    running = false;
                }
                else {
                    mService.requestLocationUpdates();
                    v.setBackgroundColor(getResources().getColor(R.color.purple));
                    v.setText(getString(R.string.running_pause));
                    running = true;
                }

            }
        });

        mEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mService.removeLocationUpdates();

                SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Gson gson = new Gson();
                DataStats datastats;
                if(mPrefs.contains("datastats")){
                    Log.i("Hello", "I'm I in ?");
                    String json = mPrefs.getString("datastats", "");
                    datastats = gson.fromJson(json, DataStats.class);
                    datastats.addData(locationHandler.toDictionary());
                }
                else {
                    datastats = new DataStats();
                    datastats.addData(locationHandler.toDictionary());
                }

                SharedPreferences.Editor prefsEditor = mPrefs.edit();
                String json = gson.toJson(datastats);
                prefsEditor.putString("datastats", json);
                prefsEditor.apply();

                Intent intent = new Intent(getBaseContext(), RunStatActivity.class);
                intent.putParcelableArrayListExtra("locations", (ArrayList<? extends Parcelable>) locationHandler.getLocations());
                startActivity(intent);
            }
        });

        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                Context.BIND_AUTO_CREATE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            if(extras.containsKey("end")){
                if(extras.getBoolean("end")){
                    mService.removeLocationUpdates();
                    Intent intent = new Intent(this, RunStatActivity.class);
                    intent.putParcelableArrayListExtra("locations",(ArrayList) locationHandler.getLocations());
                    startActivity(intent);
                }
            }
        }
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mService.putIntervalExtra(interval);
        mService.putAvgCalculatorExtra(locationHandler);
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

        public void onDelete(View v) {
        mButtonsLinearLayout.removeView(v);
    }

    public void refresh(){

        TextView tv = (TextView) mLayout1.getChildAt(1);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout1).getValue(locationHandler,this));
        tv = (TextView) mLayout2.getChildAt(1);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout2).getValue(locationHandler,this));
        tv = (TextView) mLayout3.getChildAt(1);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout3).getValue(locationHandler,this));
        tv = (TextView) mLayout4.getChildAt(1);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout4).getValue(locationHandler,this));

        tv = (TextView) mLayout1.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout1).getTitle(this));
        tv = (TextView) mLayout2.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout2).getTitle(this));
        tv = (TextView) mLayout3.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout3).getTitle(this));
        tv = (TextView) mLayout4.getChildAt(0);
        tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout4).getTitle(this));
    }

    /**
     * Returns the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        return  PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            Snackbar.make(
                    findViewById(R.id.activity_running),
                    R.string.permission_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(RunningActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    })
                    .show();
        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(RunningActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
                mService.requestLocationUpdates();
            } else {
                // Permission denied.
                Snackbar.make(
                        findViewById(R.id.activity_running),
                        R.string.permission_denied_explanation,
                        Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.settings, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .show();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public RunningDisplayFactory getRunningDisplayFactory() {
        return runningDisplayFactory;
    }

    /**
     * Receiver for broadcasts sent by {@link LocationUpdatesService}.
     */
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                locationHandler.saveLocation(location);

                TextView tv = (TextView) mLayout1.getChildAt(1);
                tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout1).getValue(locationHandler,context));

                tv = (TextView) mLayout2.getChildAt(1);
                tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout2).getValue(locationHandler,context));

                tv = (TextView) mLayout3.getChildAt(1);
                tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout3).getValue(locationHandler,context));

                tv = (TextView) mLayout4.getChildAt(1);
                tv.setText(runningDisplayFactory.getStrategyByLayoutId(R.id.running_layout4).getValue(locationHandler,context));
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

    }

}
