package com.example.filmthusiast.models;

public class MovieRating {
    public String title;
    public Ratings ratings;



    public MovieRating(){

    }


    public String getTitle() {
        return title;
    }

    public UserRating getUserRating(String user){
        UserRating output = null;
        for(UserRating r : this.ratings.getRatingList()){
            if (user.equals(r.user)){
               output = r;
               break;
            }
        }
        return output;
    }
}
