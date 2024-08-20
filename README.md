# Filmthusiast

**Filmthusiast** is an Android application designed for movie enthusiasts who want to keep track of their favorite movies, browse top box office hits, and explore upcoming releases.
 The app also features a chat section for discussing movies with other users and allows users to rate and review movies.

## Features

- **Top Box Office**: View the top-performing movies at the box office.
- **Upcoming Movies**: Discover movies that are set to release soon.
- **Favorites**: Mark movies as favorites and view them in a dedicated section.
- **Search**: Search for movies by title.
- **Movie Ratings & Reviews**: Rate and review movies, including text and photo reviews.
- **Chat**: Engage with other users in movie-related discussions.
- **User Authentication**: Sign in with email, password, or Google account.
- **Firebase Integration**: The app uses Firebase for authentication, real-time database, and image storage.

## Libraries Used

- [Firebase Authentication]- For user authentication.
- [Firebase Realtime Database] - For storing and retrieving movie data and chat messages.
- [Firebase Storage] - For uploading and storing user-uploaded images.
- [ImagePicker] - For picking and compressing images.
- [Glide] - For loading and displaying images from URLs.

## Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/your-username/filmthusiast.git
    ```
2. Open the project in Android Studio.
3. Sync the project with Gradle to download all dependencies.
4. Set up your Firebase project:
   - Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
   - Add your Android app to the Firebase project.
   - Download the `google-services.json` file and place it in the `app/` directory.
5. Run the app on your Android device or emulator.

## Usage

- **Sign Up / Login**: Users can sign up using email and password or sign in using Google.
- **Search & Explore**: Use the search bar to find specific movies or explore the top box office and upcoming movies.
- **Favorites**: Mark movies as favorites by clicking on the favorite icon.
- **Rate & Review**: Add ratings and reviews (including images) for movies.
- **Chat**: Long press on a chat message to delete it (only if you are the message owner).


Thank you for using **Filmthusiast**! We hope you enjoy it.
