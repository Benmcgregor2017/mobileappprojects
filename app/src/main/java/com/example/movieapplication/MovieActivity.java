/*
    OVERVIEW OF MOVIE_ACTIVITY.JAVA

    MovieActivity is where the movie is displayed. When a user clicks on the Recyclerview items from either HomeActivity or FavoriteActivity
    they will be sent here. While the user is sent here, an intent packages the title name and the status variable. If the status variable is 0, the
    favorites button will be set to the add setting which allows a user to add a given movie to their favorites list. If the status variable is 1, the user can
    remove the item from their favorites list. The API call made here is based on the name of the movie with a tag "t=" which means it only returns one movie making parsing easier.
    The back button also functions with the status variable, bringing the user back to previous activity.



 */
package com.example.movieapplication;

//Imports used
import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MovieActivity extends Activity {

    //View objects used
    TextView movie,info,movie_plot;
    ImageView movie_poster;
    Button add_favorite,back;

    //Getting copy of values
    String movie_url,user_email;

    //Firebase object
    private FirebaseAuth mAuth;

    //Status Variable
    int status;

    //Key from Database
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        //Get the title of the movie and the status variable
        Intent movie_intent = getIntent();
        String movie_name = movie_intent.getStringExtra("Title");
        status = movie_intent.getIntExtra("Status",0);

        //Get Instance of the database and get email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user_email = currentUser.getEmail();


        //Initialize view objects
        movie = (TextView)findViewById(R.id.movie);
        info = (TextView)findViewById(R.id.info);
        movie_plot = (TextView)findViewById(R.id.plot);
        movie_poster = (ImageView)findViewById(R.id.poster);

        //Initialize favorite button but can change text if status is equal to 1 or the user came from the FavoriteActivity which also gets the database key that will be used for deletion
        add_favorite = (Button)findViewById(R.id.favorites);
        if (status == 1){
            add_favorite.setText("Delete from Favorites");
            key = movie_intent.getStringExtra("Key");
        }

        back = (Button)findViewById(R.id.back);

        //Setup page title and url for JSON Parse
        movie.setText(movie_name);
        setup_url(movie.getText().toString());

        //Favorites listener. Adds the movie to the favorites list if status = 0 or deletes the movie from the list if status = 1
        add_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorites favorites = new Favorites();
                favorites.setEmail(user_email);
                favorites.setTitle(movie.getText().toString());
                favorites.setPoster(movie_url);
                if (status == 0) {
                    //INSERTION BLOCK
                    new FirebaseDatabaseHelper().addFavorite(favorites, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Favorites> favorites_list, List<String> keys) {
                            Toast.makeText(MovieActivity.this, "Added to Favorites list", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void DataIsInserted() {

                        }


                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                    Toast.makeText(MovieActivity.this, "Added to Favorites list", Toast.LENGTH_SHORT).show();
                } else if (status == 1) {
                    //DELETION BLOCK
                    new FirebaseDatabaseHelper().deleteFavorite(key, new FirebaseDatabaseHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Favorites> favorites_list, List<String> keys) {

                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsDeleted() {
                            Toast.makeText(MovieActivity.this,"Deleted",Toast.LENGTH_LONG).show();
                            finish(); return;
                        }
                    });
                    //Switches back to Favorite Activity after deletion
                    Toast.makeText(MovieActivity.this,"Deleted",Toast.LENGTH_LONG).show();
                    Intent favorite_intent = new Intent(MovieActivity.this, FavoriteActivity.class);
                    startActivity(favorite_intent);

                }
            }
        });


        //Back button listener
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (status == 0) {
                    Intent home_intent = new Intent(MovieActivity.this, HomeActivity.class);
                    startActivity(home_intent);
                } else if (status == 1) {
                    Intent favorite_intent = new Intent(MovieActivity.this, FavoriteActivity.class);
                    startActivity(favorite_intent);
                }else{
                     }
            }
        });


    }

    //Sets up url with "t=" tag that will be used to populate the views in this activity
    public void setup_url(String movie_name){
        //Setting up the URL for the api call
        String beginning = "https://www.omdbapi.com/?t=";
        String api_key = "&apikey=90607f2b";
        String plot = "&plot=long";
        String url = beginning + movie_name + api_key + plot;

        //Making the call to the Movie API
        //Request queue used for getting JSON Object
        RequestQueue queue = Volley.newRequestQueue(this);

        //Making the call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        grabmoviedata(response);
                        //Success
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        //On Fail
                    }
                });
        //Adds to the queue
        queue.add(jsonObjectRequest);

    }

    //Called after a successful api pull. Parses JSON object and populates the view objects in MovieActivity
    public void grabmoviedata(JSONObject response){
        try{
            String linebreak = "\n";
            String plot = response.getString("Plot");
            String director = response.getString("Director");
            String actors = response.getString("Actors");
            String rating = response.getString("Rated");
            String poster = response.getString("Poster");
            movie_url = poster;

            //Glide is used to set the imageview with a url
            Glide.with(this).load(poster).diskCacheStrategy(DiskCacheStrategy.ALL).into(movie_poster);
            info.setText("Director: " +
                    director + linebreak +
                    "Actors: " + actors + linebreak +
                    "Rating: " + rating);
            movie_plot.setText(plot);




        }catch(JSONException e){
            e.printStackTrace();
            //If failed
        }
    }

}
