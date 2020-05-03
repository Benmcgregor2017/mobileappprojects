/*
    OVERVIEW OF FAVORITES.JAVA

    Favorites.java is a class file used to assist with getting and setting information in the Firebase RealTimeDatabase.
    Each Favorites instance represents a favorites record in my RealTimeDatabase
    It models what values I have in my database. email represents the users email and is key in making sure users don't access
    other users data. Title represents the movie title while poster represents the movie poster url. Only get and set methods are used here, while
    the more complex database functions are in the FirebaseDatabaseHelper.java file.


 */

package com.example.movieapplication;

public class Favorites {
    //Members
    private String email;
    private String title;
    private String poster;


    public Favorites() {
    }

    public Favorites(String email, String title, String poster) {
        this.email = email;
        this.title = title;
        this.poster = poster;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }
}
