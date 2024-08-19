package com.example.filmthusiast.adapters;

import static androidx.core.content.ContextCompat.startActivity;

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

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;
    private MovieCallback movieCallback;

    public MovieAdapter(Context context, List<Movie> movies, MovieCallback movieCallback) {
        this.context = context;
        this.movies = movies;
        this.movieCallback = movieCallback;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_movie_card, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.movieYear.setText(movie.getYear());
        holder.movieDuration.setText(movie.getDuration() + " mins");
        holder.movieGenres.setText(String.join(", ", movie.getGenres()));
        holder.movieActors.setText(String.join(", ", movie.getActors()));
        holder.movieOverview.setText(movie.getOverview());
        holder.movieRating.setRating(movie.getRating() / 2);

        Glide.with(context)
                .load(movie.getPosterUrl())
                .into(holder.moviePoster);

        holder.movieFavorite.setImageResource(movie.isFavorite() ? R.drawable.heart : R.drawable.empty_heart);

        holder.movieFavorite.setOnClickListener(v -> {
            //movie.setFavorite(!movie.isFavorite());
            movieCallback.onFavoriteButtonClicked(movie);
            notifyDataSetChanged();
        });

        holder.constainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MovieActivity.class);

                intent.putExtra("movie", movie);
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
    public void updateMovies(List<Movie> newMovies) {
        this.movies = newMovies;
        notifyDataSetChanged();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView moviePoster;
        ImageView movieFavorite;
        TextView movieTitle;
        TextView movieYear;
        TextView movieDuration;
        TextView movieGenres;
        TextView movieActors;
        TextView movieOverview;
        RatingBar movieRating;
        ConstraintLayout constainer;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.movie_IMG_poster);
            movieFavorite = itemView.findViewById(R.id.movie_IMG_favorite);
            movieTitle = itemView.findViewById(R.id.movie_LBL_name);
            movieYear = itemView.findViewById(R.id.movie_LBL_year);
            movieDuration = itemView.findViewById(R.id.movie_LBL_duration);
            movieGenres = itemView.findViewById(R.id.movie_LBL_genres);
            movieActors = itemView.findViewById(R.id.movie_LBL_actors);
            movieOverview = itemView.findViewById(R.id.movie_LBL_overview);
            movieRating = itemView.findViewById(R.id.movie_RTNG_rating);
            constainer = itemView.findViewById(R.id.container);
        }
    }


}
