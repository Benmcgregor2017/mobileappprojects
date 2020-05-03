package com.example.movieapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Movie;
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

    TextView movie,info,movie_plot;
    ImageView movie_poster;
    Button add_favorite,back;
    String movie_url,user_email;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent movie_intent = getIntent();
        String movie_name = movie_intent.getStringExtra("Title");
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user_email = currentUser.getEmail();
        movie = (TextView)findViewById(R.id.movie);
        info = (TextView)findViewById(R.id.info);
        movie_plot = (TextView)findViewById(R.id.plot);
        movie_poster = (ImageView)findViewById(R.id.poster);
        add_favorite = (Button)findViewById(R.id.favorites);
        back = (Button)findViewById(R.id.back);

        movie.setText(movie_name);
        setup_url(movie.getText().toString());

        add_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Favorites favorites = new Favorites();
                favorites.setEmail(user_email);
                favorites.setTitle(movie.getText().toString());
                favorites.setPoster(movie_url);
                new FirebaseDatabaseHelper().addFavorite(favorites, new FirebaseDatabaseHelper.DataStatus() {
                    @Override
                    public void DataIsLoaded(List<Favorites> favorites_list, List<String> keys) {
                        Toast.makeText(MovieActivity.this, "Added to Favorites list",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void DataIsInserted() {

                    }

                    @Override
                    public void DataIsUpdated() {

                    }

                    @Override
                    public void DataIsDeleted() {

                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home_intent = new Intent(MovieActivity.this, HomeActivity.class);
                startActivity(home_intent);
            }
        });


    }

    public void setup_url(String movie_name){
        //Setting up the URL for the api call
        String beginning = "https://www.omdbapi.com/?t=";
        String api_key = "&apikey=90607f2b";
        String plot = "&plot=long";
        String url = beginning + movie_name + api_key + plot;

        //Making the call to the Movie API

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        grabmoviedata(response);
                        //                       textView.setText("Response: " + response.toString());
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                    }
                });
        //Adds to the queue
        queue.add(jsonObjectRequest);

    }

    public void grabmoviedata(JSONObject response){
        try{
            String linebreak = "\n";
            String plot = response.getString("Plot");
            String director = response.getString("Director");
            String actors = response.getString("Actors");
            String rating = response.getString("Rated");
            String poster = response.getString("Poster");
            movie_url = poster;

            Glide.with(this).load(poster).diskCacheStrategy(DiskCacheStrategy.ALL).into(movie_poster);
            info.setText("Director: " +
                    director + linebreak +
                    "Actors: " + actors + linebreak +
                    "Rating: " + rating);
            movie_plot.setText(plot);




        }catch(JSONException e){
            e.printStackTrace();
        }
    }

}
