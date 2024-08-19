package com.example.filmthusiast.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.filmthusiast.MovieActivity;
import com.example.filmthusiast.R;
import com.example.filmthusiast.models.Movie;
import com.example.filmthusiast.utilities.MovieCallback;

import java.util.List;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.MovieViewHolder> {

    private List<String> categories;
    private Context context;
    private MovieCallback movieCallback;

    public CategoryItemAdapter(Context context, List<String> categories, MovieCallback movieCallback) {
        this.context = context;
        this.categories = categories;
        this.movieCallback = movieCallback;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        String category = categories.get(position);
        holder.name.setText(category);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
    public void updateMovies(List<String> categories) {
        this.categories = categories;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        private TextView name;


        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
        }
    }


}
