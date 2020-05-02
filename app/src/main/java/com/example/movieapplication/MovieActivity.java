package com.example.movieapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieActivity extends Activity {

    TextView movie,info,movie_plot;
    ImageView movie_poster;
    Button add_favorite,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        Intent movie_intent = getIntent();
        String movie_name = movie_intent.getStringExtra("UserID");

        movie = (TextView)findViewById(R.id.movie);
        info = (TextView)findViewById(R.id.info);
        movie_plot = (TextView)findViewById(R.id.plot);
        movie_poster = (ImageView)findViewById(R.id.poster);
        add_favorite = (Button)findViewById(R.id.favorites);
        back = (Button)findViewById(R.id.back);

        movie.setText(movie_name);
        setup_url(movie.getText().toString());




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
