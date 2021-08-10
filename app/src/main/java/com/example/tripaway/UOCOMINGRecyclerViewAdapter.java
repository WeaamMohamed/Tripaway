package com.example.tripaway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.models.UpcomingTripModel;
import com.example.tripaway.ui.Upcoming.UpcomingFragment;

import java.util.ArrayList;
import java.util.List;

public class UOCOMINGRecyclerViewAdapter extends RecyclerView.Adapter<UOCOMINGRecyclerViewAdapter.MyViewHolder> {

    private List<UpcomingTripModel> mData ;
    private UpcomingFragment upcomingFragment;
    Context context;

    public UOCOMINGRecyclerViewAdapter(ArrayList<UpcomingTripModel> mData) {
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
        holder.txtName.setText(mData.get(position).getTripName());
        holder.txtTime.setText((CharSequence) mData.get(position).getTime());
        holder.txtDate.setText((CharSequence) mData.get(position).getDate());
        holder.txtStart.setText(mData.get(position).getStartPoint());
        holder.txtEnd.setText(mData.get(position).getEndPoint());
        holder.notes = mData.get(position).getNotes().toArray(new String[0]);
        holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(view.getContext(), holder.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.card);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menuActionNotes:
                                //handle menu1 click
                                return true;
                            case R.id.menuActionEdit:
                                //handle menu2 click
                                return true;
                            case R.id.menuActionDelete:
                                //handle menu3 click
                                return true;
                            case R.id.menuActionCancel:
                                //handle menu3 click
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

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
        public Button buttonViewOption;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtTripName) ;
            txtStart = (TextView) itemView.findViewById(R.id.txtStartPoint);
            txtEnd = (TextView) itemView.findViewById(R.id.txtEndPoint);
            txtDate = (TextView) itemView.findViewById(R.id.txtViewDate);
            txtTime = (TextView) itemView.findViewById(R.id.textViewTime);
            buttonViewOption = (Button) itemView.findViewById(R.id.btnViewOptions);
        }
    }
}