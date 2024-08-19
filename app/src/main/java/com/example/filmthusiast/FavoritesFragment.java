package com.example.filmthusiast;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmthusiast.adapters.MovieAdapter;
import com.example.filmthusiast.models.Movie;
import com.example.filmthusiast.utilities.DataManager;
import com.google.firebase.database.DatabaseReference;


import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView favoritesRecyclerView;
    private MovieAdapter favoritesAdapter;
    private List<Movie> favoriteMovies;
    private DatabaseReference databaseReference;
    private DataManager data;
    private ProgressBar progressbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        TextView favoritesTitle = (TextView) view.findViewById(R.id.favorites_title);
        favoritesTitle.setText("Your Favorites...");

        favoritesRecyclerView = (RecyclerView) view.findViewById(R.id.favorites_recycler_view);
        progressbar =(ProgressBar) view.findViewById(R.id.progressBar);
        progressbar.setVisibility(View.VISIBLE);

        try {
            data = new DataManager(dataManager -> {
                if (dataManager.favourites != null && !dataManager.favourites.isEmpty()) {
                    favoriteMovies = dataManager.favourites;
                    for (Movie m : favoriteMovies) {
                        m.setFavorite(true);
                    }
                    favoritesAdapter = new MovieAdapter(getContext(), favoriteMovies, movie -> {
                        data.updateMovieFavoriteStatus(movie, !movie.isFavorite());
                        data.saveFavourites();
//                    favoritesAdapter.notifyDataSetChanged();
                    });

                } else {
                    Log.d(TAG, "empty lists");
                }

                favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));//vertical default
                favoritesRecyclerView.setAdapter(favoritesAdapter);
                progressbar.setVisibility(View.GONE);

            });
        }catch(Exception e){
            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }

        return view;


    }

//



}
