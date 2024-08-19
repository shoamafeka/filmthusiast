package com.example.filmthusiast.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmthusiast.MovieActivity;
import com.example.filmthusiast.R;
import com.example.filmthusiast.models.Movie;
import com.example.filmthusiast.models.UserRating;
import com.example.filmthusiast.models.Ratings;
import com.example.filmthusiast.utilities.MovieCallback;

import java.util.List;

public class RatingAdapter extends RecyclerView.Adapter<RatingAdapter.MovieViewHolder> {

    private List<UserRating> rates;
    private Context context;
    private MovieCallback movieCallback;

    public RatingAdapter(Context context, List<UserRating> rates, MovieCallback movieCallback) {
        this.context = context;
        this.rates = rates;
        this.movieCallback = movieCallback;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rating, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        UserRating userR = rates.get(position);
        
        holder.name.setText(userR.user);
        holder.comment.setText(userR.comment);
        holder.rate_bar.setRating(userR.userRatingValue/2);
        if(!userR.imagePath.isEmpty() && userR.imagePath!=null) {
            holder.addImage.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(userR.imagePath)
                    .placeholder(R.drawable.add_image)
                    .into(holder.addImage);



        }



    }

    @Override
    public int getItemCount() {
        return rates.size();
    }
    public void updateMovies(List<UserRating> myRates) {
        this.rates = myRates;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView comment;
        private AppCompatRatingBar rate_bar;
        private  ImageView addImage;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            rate_bar = itemView.findViewById(R.id.rate_bar);
            addImage = itemView.findViewById(R.id.addImage);
            

        }
    }


}
