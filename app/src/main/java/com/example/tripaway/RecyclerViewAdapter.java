package com.example.tripaway;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripaway.ui.gallery.UpcomingFragment;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Observer<String> mContext ;
    private List<Trips> mData ;
    private UpcomingFragment timeFragment;


    public RecyclerViewAdapter(Observer<String> mContext, List<Trips> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    public RecyclerViewAdapter(UpcomingFragment upcomingFragment, List<Trips> lstBook) {
        this.timeFragment = timeFragment;
        this.mData = lstBook;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(timeFragment.getContext());
        view = mInflater.inflate(R.layout.card_layout,parent,false);
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
        CardView cardView ;

        public MyViewHolder(View itemView) {
            super(itemView);
            txtName = (TextView) itemView.findViewById(R.id.txtTripName) ;
            txtStart = (TextView) itemView.findViewById(R.id.txtStartPoint);
            txtEnd = (TextView) itemView.findViewById(R.id.txtEndPoint);
            txtDate = (TextView) itemView.findViewById(R.id.txtViewDate);
            txtTime = (TextView) itemView.findViewById(R.id.textViewTime);
            cardView = (CardView) itemView.findViewById(R.id.card_layout);
        }
    }
}