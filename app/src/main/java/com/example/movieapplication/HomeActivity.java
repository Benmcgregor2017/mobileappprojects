/*

    OVERVIEW OF HOME_ACTIVITY.JAVA

    Everything on the application is centered around the HomeActivity. Here a user can search for a movie using the name of a movie and click
    the search button to show movies in a Recyclerview. The search results are achieved through the use of a JSON response and omdbapi Movie api.
    The api call used in this activity is the "s=" tag which returns an arraylist of movie results. The user can logout using the logout button or
    go to the FavoritesActivity using the favorites button. Only authenticated users can access this activity along side the Favorite and MovieActivity.


 */

package com.example.movieapplication;

//Imports used in this activity
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HomeActivity extends Activity {

    //View objects used in the activity
    Button logout,favorites,submit;
    EditText search;

    //Firebase object
    private FirebaseAuth mAuth;

    //String that saves the email from the Firebase user
    String current_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Instance of Firebase database
        mAuth = FirebaseAuth.getInstance();

        //Initializes the view objects
        search = (EditText)findViewById(R.id.SearchBar);
        submit = (Button)findViewById(R.id.submit);
        logout = (Button)findViewById(R.id.logout);
        favorites = (Button)findViewById(R.id.favorites);

        //Logout onClickListener. When clicked, it logs the user out using the Firebase signout method and sends
        // the user back to the MainActivity
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseAuth.getInstance().signOut();
                Intent main_intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(main_intent);

            }
        });

        //Favorites onClickListener. Sends user to the FavoriteActivity
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorite_intent = new Intent(HomeActivity.this, FavoriteActivity.class);
                startActivity(favorite_intent);
            }
        });

        //Submit onClickListener. Submits information to be used in the API. Calls the urlcreation method which
        //prepares the url to be used in the api call.
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String input = search.getText().toString();
                urlcreation(input);
            }
        });

    }

    //Takes data from JSONObject response variable and adds the movie names and movie poster urls to respective lists
    //that will be used in a RecyclerView
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
            Toast.makeText(HomeActivity.this, "Search Success",
                    Toast.LENGTH_SHORT).show();
            initRecyclerView(mImageUrls,mNames);
        }
        catch(JSONException e){
            e.printStackTrace();
        }



    }

    //Initializes the RecyclerView that holds the JSON response
    private void initRecyclerView(ArrayList mImageUrls,ArrayList mNames){
        //Initializes RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerv_view_fav);

        //Creates RecyclerViewAdapter object using the movie names and movie urls lists used to initiliaze recyclervieew
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mNames,mImageUrls,HomeActivity.this);
        recyclerView.setAdapter(adapter);

        //Sets Layout to Linear
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    //Takes the search_res and builds the url to be used in the JSONObjectRequest.
    public void urlcreation(String search_res){
        //Strings used to build the API url.
        String beginning = "https://www.omdbapi.com/?s=";
        String api_key = "&apikey=90607f2b";
        String url = beginning + search_res + api_key;

        //Instantiates Request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        //Makes API call
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //On Success
                        System.out.println(response);
                        initImageBitmaps(response);
                        //                       textView.setText("Response: " + response.toString());
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

}
