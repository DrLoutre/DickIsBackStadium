package com.example.gecko.smartstadium.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gecko.smartstadium.R;
import com.example.gecko.smartstadium.classes.custom.MatchNotEnded;

import java.util.ArrayList;

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {


    private ArrayList<MatchNotEnded> mDataset;

    /**
     * initialise mDataset
     *
     * @param myDataset
     */
    public MatchAdapter(ArrayList<MatchNotEnded> myDataset) {
        mDataset = myDataset;
    }

    /**
     * Create new views (invoked by the layout manager)
     * @param parent
     * @param viewType
     * @return the views
     */
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
        return new ViewHolder(v);
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.domicile.setText(mDataset.get(position).getTeam1());
        holder.visiteur.setText(mDataset.get(position).getTeam2());
        holder.date.setText(mDataset.get(position).getDate());

    }


    /**
     * Return the size of your dataset (invoked by the layout manager)
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Provide a reference to the views for each data item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView card;
        TextView date;
        TextView domicile;
        TextView visiteur;

        public ViewHolder(View v) {
            super(v);
            card = (CardView) itemView.findViewById(R.id.matchCard);
            date = (TextView) itemView.findViewById(R.id.dateMatchCard);
            domicile = (TextView) itemView.findViewById(R.id.domicieMatchCard);
            visiteur = (TextView) itemView.findViewById(R.id.visiteurMatchCard);
        }
    }
}
