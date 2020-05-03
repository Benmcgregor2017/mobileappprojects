package com.example.movieapplication;

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
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReferenceFavorites;
    private List<Favorites> favorites_list = new ArrayList<>();

    public interface DataStatus{
        void DataIsLoaded(List<Favorites> favorites_list,List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public FirebaseDatabaseHelper() {
        mDatabase = FirebaseDatabase.getInstance();
        mReferenceFavorites = mDatabase.getReference();
    }

    public void readFavorites(String email,final DataStatus dataStatus){
        Query mQueryRef = mReferenceFavorites.child("Favorites").orderByChild("email").equalTo(email);

        mQueryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favorites_list.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode : dataSnapshot.getChildren()){
                        keys.add(keyNode.getKey());
                        Favorites favorites = keyNode.getValue(Favorites.class);
                        favorites_list.add(favorites);
                }
                dataStatus.DataIsLoaded(favorites_list,keys);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void addFavorite(Favorites favorites, final DataStatus dataStatus){
        String key = mReferenceFavorites.push().getKey();
        mReferenceFavorites.child("Favorites").child(key).setValue(favorites).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                dataStatus.DataIsInserted();
            }
        });
    }
}