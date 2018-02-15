package com.systemplus.webservice.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.systemplus.webservice.R;
import com.systemplus.webservice.model.Genre;
import com.systemplus.webservice.model.MoviesResponse;
import com.systemplus.webservice.model.Result;

import java.util.List;

/**
 * Created by Administrator on 15-02-2018.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.MovieHolder> {
    private MoviesResponse results;
    private Context context;

    public MovieDetailsAdapter(MoviesResponse results, Context context) {
        this.results = results;
        this.context = context;
    }

    @Override
    public MovieDetailsAdapter.MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_movie_view, parent, false);
        return new MovieDetailsAdapter.MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        //MoviesResponse result = results.get(position);
        holder.txtTitle.setText("Title : "+results.getTitle());
        holder.txtOverView.setText("Overview : "+results.getOverview());
        holder.txtRating.setText("Vote Count : "+results.getVoteCount());
        holder.txtVote.setText("Rating : "+results.getVoteAverage());
    }




    @Override
    public int getItemCount() {
        return 1;
    }


    class MovieHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtOverView, txtRating, txtVote;

        public MovieHolder(View itemView) {
            super(itemView);
            txtTitle = (TextView)itemView.findViewById(R.id.txtTitle);
            txtOverView = (TextView)itemView.findViewById(R.id.txtOverView);
            txtRating = (TextView)itemView.findViewById(R.id.txtRating);
            txtVote = (TextView)itemView.findViewById(R.id.txtVote);

        }
    }

}
