package com.example.gecko.smartstadium.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.gecko.smartstadium.R;

/**
 * Created by Quentin Jacquemotte on 22-04-17.
 */

public class MatchAdapter extends RecyclerView.Adapter<MatchAdapter.ViewHolder> {

    private String[] mDataset;


    public MatchAdapter(String[] myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MatchAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.match, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //todo set les bons éléments
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.domicile.setText(mDataset[position]);
        holder.visiteur.setText(mDataset[position]);
        holder.date.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Provide a reference to the views for each data item
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
