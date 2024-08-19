package com.example.filmthusiast.models;

import java.io.Serializable;
import java.util.List;

public class Movie implements Serializable {
    private String title;
    private List<String> genres;
    private List<String> actors;
    private int duration;
    private String posterUrl;
    private String overview;
    private String year; // Changed from LocalDate to String for simplicity
    private float rating;
    private boolean isFavorite;
    private boolean isCollapsed;

    public Movie() {
        this.isCollapsed = true; // Default to collapsed
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public List<String> getGenres() {
        return genres;
    }

    public Movie setGenres(List<String> genres) {
        this.genres = genres;
        return this;
    }

    public List<String> getActors() {
        return actors;
    }

    public Movie setActors(List<String> actors) {
        this.actors = actors;
        return this;
    }

    public int getDuration() {
        return duration;
    }

    public Movie setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public Movie setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getYear() {
        return year;
    }

    public Movie setYear(String year) {
        this.year = year;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Movie setRating(float rating) {
        this.rating = rating;
        return this;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public Movie setFavorite(boolean favorite) {
        this.isFavorite = favorite;
        return this;
    }

    public boolean isCollapsed() {
        return isCollapsed;
    }

    public Movie setCollapsed(boolean collapsed) {
        isCollapsed = collapsed;
        return this;
    }
}
