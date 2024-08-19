package com.example.filmthusiast.utilities;

import com.google.firebase.database.FirebaseDatabase;

public class FirebaseUtils {
    private static FirebaseDatabase database;

    public static FirebaseDatabase getDatabase() {
        if (database == null) {
            database = FirebaseDatabase.getInstance();
            database.setPersistenceEnabled(true); // Enable offline persistence
        }
        return database;
    }
}
