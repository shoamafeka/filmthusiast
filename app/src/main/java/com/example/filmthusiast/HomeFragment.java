package com.example.filmthusiast;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.filmthusiast.adapters.MovieAdapter;
import com.example.filmthusiast.models.Movie;
import com.example.filmthusiast.utilities.DataManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DataManager data;
    private EditText searchBar;
    private RecyclerView topBoxOfficeRecyclerView;
    private RecyclerView upcomingMoviesRecyclerView;
    private BottomNavigationView bottomNavigationView;
    private ImageView logout;

    private List<Movie> topBoxOfficeMovies;
    private List<Movie> upcomingMovies;

    private MovieAdapter topBoxOfficeAdapter;
    private MovieAdapter upcomingMoviesAdapter;
    private DatabaseReference databaseReference;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        searchBar = (EditText) view.findViewById(R.id.search_bar);
        topBoxOfficeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_top_box_office);
        upcomingMoviesRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_upcoming);

        logout = (ImageView) view.findViewById(R.id.logout);

        // Load data
        loadData();

        // Set up search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterMovies(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();

                mAuth.signOut();

                Intent i =new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }

    private void loadData(){
        // Load data
        data = new DataManager(dataManager -> {
            topBoxOfficeMovies = dataManager.topBoxOfficeMovies;
            upcomingMovies = dataManager.upcomingMovies;

            if(topBoxOfficeMovies!=null && upcomingMovies!=null && !topBoxOfficeMovies.isEmpty() && !upcomingMovies.isEmpty()) {
                // Set up adapters and RecyclerViews
                topBoxOfficeAdapter = new MovieAdapter(getContext(), topBoxOfficeMovies, movie -> {
                    data.updateMovieFavoriteStatus(movie, !movie.isFavorite());
                    data.saveFavourites();
                });

                upcomingMoviesAdapter = new MovieAdapter(getContext(), upcomingMovies, movie -> {
                    data.updateMovieFavoriteStatus(movie, !movie.isFavorite());
                    data.saveFavourites();
                });
            }else{
                Log.d(TAG,"empty lists");
            }




            topBoxOfficeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            topBoxOfficeRecyclerView.setAdapter(topBoxOfficeAdapter);

            upcomingMoviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            upcomingMoviesRecyclerView.setAdapter(upcomingMoviesAdapter);
        });
    }

    private void filterMovies(String query) {
        List<Movie> filteredTopBoxOffice = new ArrayList<>();
        List<Movie> filteredUpcomingMovies = new ArrayList<>();

        for (Movie movie : topBoxOfficeMovies) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredTopBoxOffice.add(movie);
            }
        }

        for (Movie movie : upcomingMovies) {
            if (movie.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredUpcomingMovies.add(movie);
            }
        }

        if (filteredTopBoxOffice.isEmpty() && filteredUpcomingMovies.isEmpty()) {
            Toast.makeText(getContext(), "Couldn't find the movie", Toast.LENGTH_SHORT).show();
        } else {
            topBoxOfficeAdapter.updateMovies(filteredTopBoxOffice);
            upcomingMoviesAdapter.updateMovies(filteredUpcomingMovies);
        }
    }



}
