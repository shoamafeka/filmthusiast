package com.example.filmthusiast.utilities;

import android.content.Context;
import android.widget.Toast;

import com.example.filmthusiast.models.Chat;
import com.example.filmthusiast.models.Movie;
import com.example.filmthusiast.models.MovieRating;
import com.example.filmthusiast.models.Ratings;
import com.example.filmthusiast.models.UserRating;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DataManager {
    public List<Movie> topBoxOfficeMovies;
    public List<Movie> upcomingMovies;
    public List<Movie> favourites =new ArrayList<>();
    private List<MovieRating> movieRatings = new ArrayList<>();

    private List<Chat> chats = new ArrayList<>();
    private static final FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public DataManager(DataManagerCallback callback){
        getTopBoxOfficeMovies(movies -> {
            topBoxOfficeMovies = movies;

            getUpcomingMovies(moviess -> {
                upcomingMovies = moviess;

                getFavouriteMovies(moviesss -> {
                    for (Movie m : moviesss) {
                        updateMovieFavoriteStatus(m, true);

                    }

                    getMovieRating(movieRatings -> {
                        this.movieRatings = movieRatings;
                        for (MovieRating m : movieRatings) {
                            updateMovieRating(m);

                        }

                        getChat(chats -> {
                            this.chats = chats;
                            callback.onCallback(this);
                        });
                    });
                });
            });
        });




    }


    public interface MoviesCallback {
        void onCallback(List<Movie> movies);
    }
    public interface MovieRatingsCallback {
        void onCallback(List<MovieRating> movies);
    }
    public interface ChatCallback {
        void onCallback(List<Chat> chats);
    }

    // Fetching chat data from Firebase
    public void getChat(final ChatCallback callback) {
        DatabaseReference chatRef = database.getReference("Chat");

        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Chat> chats = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat != null) {
                        chats.add(chat);
                    }
                }
                callback.onCallback(chats);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error fetching data: " + error.getMessage());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error.
            }
        });
    }

    public List<Chat> getChats() {
        return chats;
    }

    public void addChat(Chat chat) {
        this.chats.add(chat);
    }

    public void saveChat() {
        DatabaseReference chatRef = database.getReference("Chat");
        chatRef.setValue(chats);
    }

    // Deleting a chat message from Firebase
    public void deleteChat(Chat chat, Context context) {
        DatabaseReference chatRef = database.getReference("Chat");

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean found = false;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat c = snapshot.getValue(Chat.class);
                    if (c != null && c.getNow().equals(chat.getNow()) &&
                            c.getText().equals(chat.getText()) &&
                            c.getUser_id().equals(chat.getUser_id()) &&
                            c.getUser_id().equals(user.getUid())) {
                        snapshot.getRef().removeValue(); // Remove the specific message
                        Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    Toast.makeText(context, "You cannot delete this message", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error deleting chat: " + error.getMessage());
            }
        });
    }

    public void getFavouriteMovies(final MoviesCallback callback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference moviesRef = database.getReference("favourites/"+user.getUid());

        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Movie> movies = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    movies.add(movie);
                }
                callback.onCallback(movies);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors.
                System.err.println("Error fetching data: " + error.getMessage());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error.
            }
        });
    }

    public void getTopBoxOfficeMovies(final MoviesCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference moviesRef = database.getReference("topBoxOfficeMovies");

        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Movie> movies = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    movies.add(movie);
                }
                callback.onCallback(movies);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors.
                System.err.println("Error fetching data: " + error.getMessage());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error.
            }
        });
    }

    public void getUpcomingMovies(final MoviesCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference moviesRef = database.getReference("upcomingMovies");

        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Movie> movies = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Movie movie = snapshot.getValue(Movie.class);
                    movies.add(movie);
                }
                callback.onCallback(movies);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors.
                System.err.println("Error fetching data: " + error.getMessage());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error.
            }
        });
    }

    public void getMovieRating(final MovieRatingsCallback callback) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference moviesRef = database.getReference("MovieRating");

        moviesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<MovieRating> ratings = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MovieRating movieRating = snapshot.getValue(MovieRating.class);
                    ratings.add(movieRating);
                }
                callback.onCallback(ratings);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle possible errors.
                System.err.println("Error fetching data: " + error.getMessage());
                callback.onCallback(new ArrayList<>()); // Return an empty list on error.
            }
        });
    }

    public void updateMovieFavoriteStatus(Movie movie, Boolean bool) {
        // Find and update the movie in the lists
        for (Movie m : topBoxOfficeMovies) {
            if (m.getTitle().equals(movie.getTitle())) {
                m.setFavorite(bool);
            }

        }
        for (Movie m : upcomingMovies) {
            if (m.getTitle().equals(movie.getTitle())) {
                m.setFavorite(bool);
            }
        }
        for (Movie m : favourites) {
            if (m.getTitle().equals(movie.getTitle())) {
                m.setFavorite(bool);
            }
        }
        if(bool){
            Boolean isPresent = false;
            for (Movie m : favourites) {
                if (m.getTitle().equals(movie.getTitle())) {
                    isPresent = true;
                }
            }
            if(!isPresent) {
                favourites.add(movie);
            }
        }else{
            for (Movie m : favourites) {
                if (m.getTitle().equals(movie.getTitle())) {
                    //favourites.remove(movie);
                    favourites.remove(m);
                    break;
                }
            }

        }
    }

    private void updateMovieRating(MovieRating rating) {
        // Find and update the movie in the lists
        for (Movie m : topBoxOfficeMovies) {
            if (m.getTitle().equals(rating.getTitle())) {
                m.setRating(rating.ratings.getAverageRating());
            }

        }
        for (Movie m : upcomingMovies) {
            if (m.getTitle().equals(rating.getTitle())) {
                m.setRating(rating.ratings.getAverageRating());
            }
        }
    }

    public void saveFavourites(){
        int i = 0;
        DatabaseReference favoritesRef = database.getReference("favourites").child(user.getUid());
        favoritesRef.setValue(favourites);

    }

    public void saveMovieRatings(){
        DatabaseReference favoritesRef = database.getReference("MovieRating");
        favoritesRef.setValue(movieRatings);
    }

    public void rateMovie(UserRating userRating, String movieTitle){

        boolean isReplaced = false;
        for(MovieRating mR : this.movieRatings){
            if(mR.getTitle().equals(movieTitle)){
                mR.ratings.appendToRatingList(userRating);
                isReplaced = true;
            }
        }
        if(!isReplaced){
            List<UserRating> userRtsList = new ArrayList<>();
            userRtsList.add(userRating);

            Ratings rating = new Ratings();
            rating.ratingList = userRtsList;

            MovieRating myRating = new MovieRating();
            myRating.ratings=rating;
            myRating.title=movieTitle;

            movieRatings.add(myRating);
        }

    }

    public UserRating getUserRatings(String user, String movieTitle){
        UserRating output = null;
        for(MovieRating mR : this.movieRatings){
            if(mR.getTitle().equals(movieTitle)){
                output = mR.getUserRating(user);
            }
        }
        return output;
    }

    public List<UserRating> getMovieRatings(String movieTitle){
        List<UserRating> output = null;
        for(MovieRating mR : this.movieRatings){
            if(mR.getTitle().equals(movieTitle)){
                output = mR.ratings.getRatingList();
            }
        }
        return output;
    }
}
