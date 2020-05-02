package com.example.movieapplication;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    Button logout,favorites,submit;
    EditText search;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        search = (EditText)findViewById(R.id.SearchBar);
        submit = (Button)findViewById(R.id.submit);
        logout = (Button)findViewById(R.id.logout);
        favorites = (Button)findViewById(R.id.favorites);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent main_intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(main_intent);

            }
        });

        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorite_intent = new Intent(HomeActivity.this, FavoriteActivity.class);
                startActivity(favorite_intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String input = search.getText().toString();
                urlcreation(input);
            }
        });

    }
    private void initImageBitmaps(JSONObject response){
        try {

            ArrayList<String> mNames = new ArrayList<>();
            ArrayList<String> mImageUrls = new ArrayList<>();

            JSONArray searchArray = response.getJSONArray("Search");

            for(int i = 0; i < searchArray.length();i++){
                JSONObject movie = searchArray.getJSONObject(i);
                String title = movie.getString("Title");
                String poster = movie.getString("Poster");

                mImageUrls.add(poster);
                mNames.add(title);
            }
            Toast.makeText(HomeActivity.this, "Pass",
                    Toast.LENGTH_SHORT).show();
            initRecyclerView(mImageUrls,mNames);
        }
        catch(JSONException e){
            e.printStackTrace();
        }



    }
    private void initRecyclerView(ArrayList mImageUrls,ArrayList mNames){
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames,mImageUrls,HomeActivity.this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void urlcreation(String search_res){
        String beginning = "https://www.omdbapi.com/?s=";
        String api_key = "&apikey=90607f2b";
        String url = beginning + search_res + api_key;

        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response);
                        initImageBitmaps(response);
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
}
