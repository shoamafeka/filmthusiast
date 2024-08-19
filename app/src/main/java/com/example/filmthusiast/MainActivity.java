package com.example.filmthusiast;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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

public class MainActivity extends AppCompatActivity {
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

    @Override
    protected void onResume() {
        super.onResume();


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary));

            // Optionally set the status bar icons to dark or light
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                View decor = window.getDecorView();
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); // For dark icons
                // For light icons, omit the above line or set `0` as the visibility
            }
        }


        // Set the default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
        }


        bottomNavigationView = findViewById(R.id.bottom_navigation);



        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                    if(item.getItemId()==R.id.nav_favorites) {
                        fragment = new FavoritesFragment();
                    }
                    else if(item.getItemId()==R.id.nav_group) {
                        fragment = new ChatFragment();
                    }
                    else{
                        fragment = new HomeFragment();
                    }

                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }



}
