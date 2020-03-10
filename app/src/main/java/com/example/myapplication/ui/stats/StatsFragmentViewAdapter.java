package com.example.myapplication.ui.stats;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Model.DataStats;
import com.example.myapplication.R;

import java.util.List;

public class StatsFragmentViewAdapter extends RecyclerView.Adapter<StatsFragmentViewAdapter.ViewHolder> {

    private DataStats mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    StatsFragmentViewAdapter(Context context, DataStats data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        mData.reverseStats();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.fragment_stats_item, parent, false);
        view.getLayoutParams().height = parent.getHeight() / 10;

        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mData.setRunIndex(position);
        String sDistance = String.format("%.2f",mData.getDistance()) + mInflater.getContext().getString(R.string.running_distance_unit);
        holder.distance.setText(sDistance);

        String sDate = "Date : ";
        holder.date.setText(sDate);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.getStats().size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView distance , date;

        ViewHolder(View itemView) {
            super(itemView);
            distance = itemView.findViewById(R.id.stat_distance);
            date = itemView.findViewById(R.id.stat_date);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
//    String getItem(int id) {
//        return mData.get(id);
//    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}