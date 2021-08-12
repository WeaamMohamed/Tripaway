package com.example.tripaway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.models.OldTripsModel;
import com.example.tripaway.ui.history.HistoryFragment;

import java.util.ArrayList;

public class OLDRecyclerViewAdapter extends RecyclerView.Adapter<OLDRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<OldTripsModel> mData ;
    private HistoryFragment historyFragment;
    Context context;

    public OLDRecyclerViewAdapter(ArrayList<OldTripsModel> mData) {
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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_card_view,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.txtName.setText(mData.get(position).getTripName());
        holder.txtTime.setText( mData.get(position).getTime());
        holder.txtDate.setText( mData.get(position).getDate());
        holder.txtStart.setText(mData.get(position).getStartPoint());
        holder.txtEnd.setText(mData.get(position).getEndPoint());
        holder.notes = mData.get(position).getNotes().toArray(new String[0]);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View hiddenView = holder.itemView.findViewById(R.id.lytHidden);
                hiddenView.setVisibility( hiddenView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            }
        });
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
        public Button buttonOption;
        public ImageView imgHistory;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtTripName) ;
            txtStart = (TextView) itemView.findViewById(R.id.txtStartPoint);
            txtEnd = (TextView) itemView.findViewById(R.id.txtEndPoint);
            txtDate = (TextView) itemView.findViewById(R.id.txtViewDate);
            txtTime = (TextView) itemView.findViewById(R.id.textViewTime);
            buttonOption = (Button) itemView.findViewById(R.id.btnOptions);
            imgHistory = (ImageView) itemView.findViewById(R.id.imgHistory);
        }
    }
}