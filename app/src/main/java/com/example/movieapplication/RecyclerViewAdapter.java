/*
    OVERVIEW OF RECYCLER_VIEW_ADAPTER.JAVA

    This is the RecyclerView java class that helps fill in the Recyclerview. This java class is used in the HomeActivity
    while RecyclerView_Fave is used in FavoriteActivity. The view uses the layout resource file: layout_listitem.xml and
    contains an image and a textview. The image holds the movie poster while the textview holds the movie title.

 */
package com.example.movieapplication;

//Imports used in the Java class

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    //Log tag
    private static final String TAG = "RecyclerViewAdapter";

    //Movie title and movie poster url array lists used to populate the recyclerview items
    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();

    //HomeActivity context
    private Context mContext;


    //Constructor: takes in movie titles , Poster urls, and the current context
    public RecyclerViewAdapter(ArrayList<String> mImageNames, ArrayList<String> mImages, Context mContext) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    //Creates the item boxes used in the RecyclerView
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override

    //Sets variables in recyclerview items. Also has an onclick listener which directs the user to the MovieActivity

    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Log.d(TAG,"onBindViewHolder: called.");

        // Glide library used to load the imageUrl into the RecyclerView item
        Glide.with(mContext)
                .asBitmap()
                .load(mImages.get(position))
                .into(holder.image);
        holder.imageName.setText(mImageNames.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));
                Intent movie_intent = new Intent(mContext, MovieActivity.class);

                //Puts movie title into the Intent. The movie title will be used in the API call in MovieActivity
                movie_intent.putExtra("Title",mImageNames.get(position));

                //Sets the value of the favorites button in movie activity. A 0 means the user can add the movie
                //to their favorites list while a 1 means the user can delete the movie on their favorites list. (It'll already
                // be on their favorites list.
                movie_intent.putExtra("Status",0);

                mContext.startActivity(movie_intent);
            }
        });
    }

    //gets number of search results returned.
    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    //ViewHolder subclass that initializes the RecyclerView item views.
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView imageName;
        RelativeLayout parentLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.image_name);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
