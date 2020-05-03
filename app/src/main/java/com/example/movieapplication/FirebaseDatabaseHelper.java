/*
    OVERVIEW FIREBASE_DATABASE_HELPER.JAVA

    This class is responsible for handling Firebase Realtime database operations. The operations used include
    reading all the records under the user's email,deleting a given record, and adding a record.  Also includes an interface
    that allows the user to use these functions


 */

package com.example.movieapplication;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FirebaseDatabaseHelper {
    //Database object
    private FirebaseDatabase mDatabase;
    //Reference in Database
    private DatabaseReference mReferenceFavorites;
    //List of Favorites objects
    private List<Favorites> favorites_list = new ArrayList<>();

    //Data Status interface that alerts user when data changes
    public interface DataStatus{
        void DataIsLoaded(List<Favorites> favorites_list,List<String> keys);
        void DataIsInserted();
        void DataIsDeleted();
    }

    //Constructor
    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFavorites = mDatabase.getReference();
    }


    //Reads all the records in the database with the users email. Utilizes the query object to get a
    //result that contains only records containing the current users email. Used in FavoriteActivity to
    //Display the list of favorites in a recyclerview
    public void readFavorites(String email,final DataStatus dataStatus){
        //Query used to set the reference. My database is under the Favorites parent. It checks all the records in the Favorites "umbrella"
        //and verifies if any records contain the current users email.
        Query mQueryRef = mReferenceFavorites.child("Favorites").orderByChild("email").equalTo(email);

        mQueryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Clears current favorites list
                favorites_list.clear();

                //Creates a list of keys that will make sure that the records are pulled from the database
                List<String> keys = new ArrayList<>();

                //A Datasnapshot object is created and iterated through the children and grabs the movie poster url and movie title and puts it into their
                //respective lists.
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                        keys.add(keyNode.getKey());
                        Favorites favorites = keyNode.getValue(Favorites.class);
                        favorites_list.add(favorites);
                }
                //Calls data is loaded
                dataStatus.DataIsLoaded(favorites_list,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //This function is used in MovieActivity if the status on that activity is set to 0. It creates a new favorites record and adds it to the data base under the
    //"Favorites umbrella" On success a dataStatus call is made
    public void addFavorite(Favorites favorites, final DataStatus dataStatus){
        String key = mReferenceFavorites.push().getKey();
        mReferenceFavorites.child("Favorites").child(key).setValue(favorites).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }

    //This function is used in MovieActivity if the status on that activity is set to 1. It deletes a favorites record and removes it to the data base under the
    //"Favorites umbrella" On success a dataStatus call is made
    public void deleteFavorite(String key, final DataStatus dataStatus){
        mReferenceFavorites.child("Favorites").child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsDeleted();
            }
        });
    }
}
