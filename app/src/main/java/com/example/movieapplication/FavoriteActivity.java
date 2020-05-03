/*
    OVERVIEW OF FAVORITE_ACTIVITY

    When a user clicks on the favorites button in the HomeActivity, they are sent to this activity. This activity holds the users
    favorites that they added from previous searches. A recyclerview is used to house these favorites. It uses the RecyclerView_Fav.java
    file to do this. Clicking on any of the recyclerview items brings the user to the MovieActivity and sends a status int of 1 and the name of the title.
    This means that one can click the item and view the movie and also delete the favorite from their favorites list. The activity also contains a back button
    which send them back to HomeActivity

 */
package com.example.movieapplication;

//Imports used in Favorite.Activity
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FavoriteActivity extends Activity {
    //RecyclerView object
    RecyclerView mRecyclerView;

    //Firebase object
    private FirebaseAuth mAuth;

    //Copy of users email
    String user_email;

    //Back button
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        //initialize button
        back = (Button)findViewById(R.id.back_button);

        //Get Firebase instance and get a copy of the users email which is used for retrieving the record with the users email
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user_email = currentUser.getEmail();

        //initialize recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerv_view_fav);

        //Reads favorites from database with users email
        new FirebaseDatabaseHelper().readFavorites(user_email,new FirebaseDatabaseHelper.DataStatus(){

            @Override
            public void DataIsLoaded(List<Favorites> favorites_list, List<String> keys) {
                //Configures recyclerview object
                new RecyclerView_Fav().setConfig(mRecyclerView,FavoriteActivity.this,favorites_list,keys);
            }

            @Override
            public void DataIsInserted() {

            }


            @Override
            public void DataIsDeleted() {

            }
        });

        //Sends user back to HomeActivity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favorite_intent = new Intent(FavoriteActivity.this, HomeActivity.class);
                startActivity(favorite_intent);
            }
        });
    }


}
