package com.example.movieapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FavoriteActivity extends Activity {

    RecyclerView mRecyclerView;
    private FirebaseAuth mAuth;
    String user_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        user_email = currentUser.getEmail();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerv_view_fav);
        new FirebaseDatabaseHelper().readFavorites(user_email,new FirebaseDatabaseHelper.DataStatus(){

            @Override
            public void DataIsLoaded(List<Favorites> favorites_list, List<String> keys) {
                new RecyclerView_Fav().setConfig(mRecyclerView,FavoriteActivity.this,favorites_list,keys);
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


}
