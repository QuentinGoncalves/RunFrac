package com.example.myapplication.ui.interval;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.CountDownTimerActivity;
import com.example.myapplication.R;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Locale;

import static android.content.ContentValues.TAG;

public class IntervalFragment extends Fragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_interval, container, false);
        final TextView textView = root.findViewById(R.id.text_gallery);

        NumberPicker setsPicker;

        setsPicker = root.findViewById(R.id.numberpicker_sets_picker);
        setsPicker.setMaxValue(99);
        setsPicker.setMinValue(1);
        setsPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        NumberPicker workoutMinPicker;

        workoutMinPicker = root.findViewById(R.id.numberpicker_workout_min);
        workoutMinPicker.setMaxValue(15);
        workoutMinPicker.setMinValue(0);
        workoutMinPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        NumberPicker workoutSecPicker;

        workoutSecPicker = root.findViewById(R.id.numberpicker_workout_sec);
        workoutSecPicker.setMaxValue(59);
        workoutSecPicker.setMinValue(0);
        workoutSecPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        NumberPicker restMinPicker;

        restMinPicker = root.findViewById(R.id.numberpicker_rest_min);
        restMinPicker.setMaxValue(15);
        restMinPicker.setMinValue(0);
        restMinPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        NumberPicker restSecPicker;

        restSecPicker = root.findViewById(R.id.numberpicker_rest_sec);
        restSecPicker.setMaxValue(59);
        restSecPicker.setMinValue(0);
        restSecPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int oldVal, int newVal) {
                Log.d(TAG, String.format(Locale.US, "oldVal: %d, newVal: %d", oldVal, newVal));
            }
        });

        Button run = root.findViewById(R.id.interval_start_running);
        run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CountDownTimerActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}