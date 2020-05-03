package com.example.movieapplication;

public class Favorites {
    private String email;
    private String title;
    private String poster;
    private String email_title;


    public String getEmail_title() {
        return email_title;
    }

    public void setEmail_title(String email_title) {
        this.email_title = email_title;
    }



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
