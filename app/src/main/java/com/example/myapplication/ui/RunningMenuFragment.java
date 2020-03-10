package com.example.myapplication.ui;

import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.Model.RunningDisplay.RunningDisplayFactory;
import com.example.myapplication.R;
import com.example.myapplication.RunningActivity;

import java.util.ArrayList;
import java.util.List;


public class RunningMenuFragment extends Fragment implements RunningMenuViewAdapter.ItemClickListener {

    private OnFragmentInteractionListener mListener;
    private RunningMenuViewAdapter adapter;
    private int layoutId;
    private List<String> runInformation;
    private RunningDisplayFactory runningDisplayFactory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_running_menu, container, false);

        RunningActivity runA = (RunningActivity) getActivity();

        runInformation = runA.getRunningDisplayFactory().getUndisplayedStrategyTitle(getContext());
        runningDisplayFactory = runA.getRunningDisplayFactory();
        // set up the RecyclerView
        RecyclerView recyclerView = root.findViewById(R.id.rv_running_menu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new RunningMenuViewAdapter(getContext(), runInformation);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        Bundle b = this.getArguments();
        if (b != null){
            layoutId = b.getInt("layout");
        }

        return root;
    }

    @Override
    public void onItemClick(View view, final int position) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                RunningActivity runA = (RunningActivity) getActivity();
                runningDisplayFactory.setDisplayedStrategy(runInformation.get(position), layoutId, getContext());
                runA.refresh();

                getActivity().getSupportFragmentManager().popBackStack();
            }

        }, 250);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public List<String> getRunInformation() {
        return runInformation;
    }
}
