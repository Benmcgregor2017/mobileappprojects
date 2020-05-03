/*
    OVERVIEW OF RECYCLER_VIEW_FAV.JAVA

    This is the recycler view that is used with FavoriteActivity. It functions about the same but initializes itself with lists of favorites objects and keys. This is because the
    information used to populate this view comes from the Firebase RealTimeDatabase. This recyclerview also uses the same layoout file as RecyclerViewAdapter



 */
package com.example.movieapplication;

//Imports
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class RecyclerView_Fav {
    //Members used
    private Context mContext;
    private FavoriteAdapter mFavoriteAdapter;
    //Debug
    private static final String TAG = "RecyclerViewAdapter";

    public void setConfig(RecyclerView recyclerView,Context context, List<Favorites> favorites,List<String> keys) {
        //Set up Recycler view for Favorite Activity
        mContext = context;
        mFavoriteAdapter = new FavoriteAdapter(favorites,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFavoriteAdapter);
    }

    class FavItemView extends RecyclerView.ViewHolder{
        //Individual item view in RecyclerView
        private ImageView poster;
        private TextView title;
        RelativeLayout parentLayout;

        private String key;

        public FavItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.layout_listitem,parent,false));

            title = (TextView) itemView.findViewById(R.id.image_name);
            poster = (ImageView) itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            //Individual on click listener for recyclerview item
            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent movie_intent = new Intent(mContext, MovieActivity.class);
                    movie_intent.putExtra("Title",title.getText().toString());
                    movie_intent.putExtra("Status",1);
                    movie_intent.putExtra("Key",key);
                    mContext.startActivity(movie_intent);
                }
            });

        }
        //Bind variables to view objects in layout_listitem
        public void bind(final Favorites favorites, final String key){
            title.setText(favorites.getTitle());
            Glide.with(mContext).load(favorites.getPoster()).diskCacheStrategy(DiskCacheStrategy.ALL).into(poster);
            this.key = key;

        }
    }

        //class used to take a favorite from the favorites list and put it into an individual item view
        class FavoriteAdapter extends RecyclerView.Adapter<FavItemView>{
            private List<Favorites> mFavoritelist;
            private List<String> mKeys;

            public FavoriteAdapter(List<Favorites> mFavoritelist, List<String> mKeys) {
                this.mFavoritelist = mFavoritelist;
                this.mKeys = mKeys;
            }

            @NonNull
            @Override
            public FavItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return new FavItemView(parent);
            }

            @Override
            public void onBindViewHolder(@NonNull FavItemView holder, int position) {
                holder.bind(mFavoritelist.get(position),mKeys.get(position));
            }

            @Override
            public int getItemCount() {
                return mFavoritelist.size();
            }
        }
    }

