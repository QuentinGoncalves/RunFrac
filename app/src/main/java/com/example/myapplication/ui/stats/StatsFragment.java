package com.example.myapplication.ui.stats;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.CountDownTimerActivity;
import com.example.myapplication.Model.DataStats;
import com.example.myapplication.R;
import com.example.myapplication.RunStatActivity;
import com.example.myapplication.ui.RunningMenuViewAdapter;
import com.google.gson.Gson;

import static android.content.Context.MODE_PRIVATE;

public class StatsFragment extends Fragment implements StatsFragmentViewAdapter.ItemClickListener {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_stats, container, false);

        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        Gson gson = new Gson();
        DataStats datastats;
        if(mPrefs.contains("datastats")){

            String json = mPrefs.getString("datastats", "");
            datastats = gson.fromJson(json, DataStats.class);

            // set up the RecyclerView
            RecyclerView recyclerView = root.findViewById(R.id.rv_stat);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            StatsFragmentViewAdapter adapter = new StatsFragmentViewAdapter(getContext(), datastats);
            adapter.setClickListener(this);
            recyclerView.setAdapter(adapter);
        }

        return root;
    }

    @Override
    public void onItemClick(View view, int position) {

    }
}