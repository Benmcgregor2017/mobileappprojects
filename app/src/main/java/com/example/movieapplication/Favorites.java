package com.example.movieapplication;

public class Favorites {
    private String email;
    private String title;
    private String poster_url;

    public Favorites() {
    }

    public Favorites(String email, String title, String poster_url) {
        this.email = email;
        this.title = title;
        this.poster_url = poster_url;
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

    public String getPoster_url() {
        return poster_url;
    }

    public void setPoster_url(String poster_url) {
        this.poster_url = poster_url;
    }
}
