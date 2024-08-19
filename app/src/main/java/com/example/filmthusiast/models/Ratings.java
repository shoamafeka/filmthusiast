package com.example.filmthusiast.models;

import java.util.ArrayList;
import java.util.List;

public class Ratings {
    public List<UserRating> ratingList = new ArrayList<>();

    public Ratings(){

    }

    public List<UserRating> getRatingList(){
        return this.ratingList;
    }

    public void appendToRatingList(UserRating usr){
        boolean alreadyRated = false;
        for(UserRating userR: this.ratingList){
            if(userR.user.equals(usr.user)) alreadyRated = true;
        }
        if(!alreadyRated){
            //add new user rating
            this.ratingList.add(usr);
        }
        else{
            //overwrite user rating
            for(UserRating userR: this.ratingList){
                if(userR.user.equals(usr.user))
                    userR.userRatingValue= usr.userRatingValue;
                    userR.comment = usr.comment;
            }
        }
    }

    public Float getAverageRating() {
        Float sum = 0f;
        int count = 0;
        for(UserRating r : this.ratingList){

            sum += r.userRatingValue;
            count++;
        }
        return sum/count;

    }
}
