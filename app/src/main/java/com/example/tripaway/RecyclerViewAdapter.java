package com.example.tripaway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.ui.gallery.UpcomingFragment;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private List<Trips> mData ;
    private UpcomingFragment upcomingFragment;


    public RecyclerViewAdapter(ArrayList<Trips> mData) {
        this.mData = mData;
    }
    /*public RecyclerViewAdapter(UpcomingFragment upcomingFragment, List<Trips> tripsList) {
        this.upcomingFragment = upcomingFragment;
        this.mData = tripsList;
    }*/


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
/*
        View view ;
        LayoutInflater mInflater = LayoutInflater.from(timeFragment.getContext());
        view = mInflater.inflate(R.layout.card_layout,parent,false);
        return new MyViewHolder(view);*/
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txtName.setText(mData.get(position).getName());
        holder.txtTime.setText((CharSequence) mData.get(position).getTime());
        holder.txtDate.setText((CharSequence) mData.get(position).getDate());
        holder.txtStart.setText(mData.get(position).getStartPoint());
        holder.txtEnd.setText(mData.get(position).getEndPoint());
        holder.notes = mData.get(position).getNotes();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView txtName;
        public TextView txtTime;
        public TextView txtDate;
        public TextView txtStart;
        public TextView txtEnd;
        public String[] notes;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtTripName) ;
            txtStart = (TextView) itemView.findViewById(R.id.txtStartPoint);
            txtEnd = (TextView) itemView.findViewById(R.id.txtEndPoint);
            txtDate = (TextView) itemView.findViewById(R.id.txtViewDate);
            txtTime = (TextView) itemView.findViewById(R.id.textViewTime);
        }
    }
}