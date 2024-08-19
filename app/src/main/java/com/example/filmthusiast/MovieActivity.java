package com.example.filmthusiast;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.Manifest;

import androidx.core.app.ActivityCompat;


import android.widget.ImageView;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.widget.Toast;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.example.filmthusiast.adapters.CategoryItemAdapter;

import com.example.filmthusiast.adapters.RatingAdapter;
import com.example.filmthusiast.models.Movie;
import com.example.filmthusiast.models.UserRating;
import com.example.filmthusiast.utilities.DataManager;
import com.example.filmthusiast.utilities.ImageUploadUtility;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MovieActivity extends AppCompatActivity {


    private ImageView imagePoster;
    private TextView title;
    private TextView year;
    private TextView duration;
    private RecyclerView recyclerGenreCategories;
    private TextView textView8;
    private TextView description;
    private TextView textViewOverview;
    private RecyclerView recyclerActorsCategories;
    private TextView textView9;
    private AppCompatRatingBar userRatebar;
    private EditText comment;
    private Button submit;
    private TextView textView4;
    private RecyclerView rating;
    private ImageView back;
    private ImageView addImage;

    private static final int REQUEST_CODE_PICK_IMAGE = 1001;
    private static final int REQUEST_CODE_CAMERA = 1002;
    private static final int REQUEST_CODE_PERMISSIONS = 1003;

    private DataManager data;
    private RatingAdapter ratingAdapter;
    private CategoryItemAdapter genreItemAdapter;
    private CategoryItemAdapter actorsItemAdapter;
    private FirebaseUser User;
    private Movie movie;
    private String imagePath ="";
    private ImageUploadUtility imageUploadUtility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_movie);
        User = FirebaseAuth.getInstance().getCurrentUser();
        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra("movie");

        // Initialize the views
        imagePoster = findViewById(R.id.imagePoster);
        title = findViewById(R.id.title);
        year = findViewById(R.id.year);
        duration = findViewById(R.id.duration);
        recyclerGenreCategories = findViewById(R.id.recycler_genre_categories);
        textView8 = findViewById(R.id.textView8);
        description = findViewById(R.id.description);
        textViewOverview = findViewById(R.id.textViewOverview);
        recyclerActorsCategories = findViewById(R.id.recycler_actors_categories);
        textView9 = findViewById(R.id.textView9);
        userRatebar = findViewById(R.id.userRatebar);
        comment = findViewById(R.id.comment);
        submit = findViewById(R.id.submit);
        textView4 = findViewById(R.id.textView4);
        rating = findViewById(R.id.rating);
        back = findViewById(R.id.back);
        addImage = findViewById(R.id.addImage);
        imageUploadUtility = new ImageUploadUtility(this);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSIONS);
        // Check if permission is already granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PICK_IMAGE);
        }


        Glide.with(MovieActivity.this)
                .load(movie.getPosterUrl())
                .into(imagePoster);

        title.setText(movie.getTitle());
        year.setText(movie.getYear());
        duration.setText(String.valueOf(movie.getDuration()).concat(" mins"));
        description.setText(movie.getOverview());

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageUploadUtility.pickImageAndUpload(MovieActivity.this, new ImageUploadUtility.UploadCallback() {
                    @Override
                    public void onUploadSuccess(String imageUrl) {
                        imagePath = imageUrl;
                        if(!imagePath.isEmpty() && imagePath!=null) {
                            Glide.with(MovieActivity.this)
                                    .load(imagePath)
                                    .placeholder(R.drawable.add_image)
                                    .into(addImage);
                        }
                        //Toast.makeText(MovieActivity.this, imagePath, Toast.LENGTH_SHORT).show();
                        Toast.makeText(MovieActivity.this, "Image uploaded: ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUploadFailure(Exception exception) {
                        // Handle upload failure
                        Toast.makeText(MovieActivity.this, "Upload failed: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }


        });

        genreItemAdapter = new CategoryItemAdapter(this, movie.getGenres(), item -> {});
        recyclerGenreCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerGenreCategories.setAdapter(genreItemAdapter);


        actorsItemAdapter = new CategoryItemAdapter(this, movie.getActors(), item -> {});
        recyclerActorsCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerActorsCategories.setAdapter(actorsItemAdapter);

        data = new DataManager(dataManager -> {
            String myUser = User.getUid() ;
            if(User.getDisplayName() != null ){
                if(!User.getDisplayName().isEmpty()) {
                    myUser = User.getDisplayName();
                }
            }
            UserRating myuserRating = dataManager.getUserRatings(myUser,movie.getTitle());
            if(myuserRating!=null) {
                userRatebar.setRating(myuserRating.userRatingValue/2);
                comment.setText(myuserRating.comment);
            }

            List<Movie> topBoxOfficeMovies = dataManager.topBoxOfficeMovies;
            List<Movie> upcomingMovies = dataManager.upcomingMovies;

            if(topBoxOfficeMovies!=null && upcomingMovies!=null && !topBoxOfficeMovies.isEmpty() && !upcomingMovies.isEmpty()) {
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String cmt = String.valueOf(comment.getText());
                        Float rateValue = userRatebar.getRating() * 2;
                        UserRating userRating = new UserRating();
                        userRating.comment = cmt;
                        userRating.userRatingValue = rateValue;
                        userRating.user = User.getUid() ;
                        if(User.getDisplayName() != null ){
                            if(!User.getDisplayName().isEmpty()) {
                                userRating.user = User.getDisplayName();
                            }
                        }
                        Log.d("user", User.getUid());
                        userRating.imagePath = imagePath == null ? "" : imagePath ;

                        dataManager.rateMovie(userRating, movie.getTitle());
                        dataManager.saveMovieRatings();
                        Toast.makeText(MovieActivity.this, "Saved rating successfully", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if(dataManager.getMovieRatings(movie.getTitle())!=null && !dataManager.getMovieRatings(movie.getTitle()).isEmpty()) {
                ratingAdapter = new RatingAdapter(this, dataManager.getMovieRatings(movie.getTitle()), item -> {
                });
                rating.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                rating.setAdapter(ratingAdapter);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });






    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        imageUploadUtility.handleActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with image picking
            } else {
                // Permission denied, handle accordingly
            }
        }
    }

}