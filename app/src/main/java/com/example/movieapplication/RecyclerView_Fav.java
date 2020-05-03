package com.example.movieapplication;

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
    private Context mContext;
    private FavoriteAdapter mFavoriteAdapter;
    private static final String TAG = "RecyclerViewAdapter";

    public void setConfig(RecyclerView recyclerView,Context context, List<Favorites> favorites,List<String> keys) {
        mContext = context;
        mFavoriteAdapter = new FavoriteAdapter(favorites,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFavoriteAdapter);
    }

    class FavItemView extends RecyclerView.ViewHolder{
        private ImageView poster;
        private TextView title;
        RelativeLayout parentLayout;

        private String key;

        public FavItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.layout_listitem,parent,false));

            title = (TextView) itemView.findViewById(R.id.image_name);
            poster = (ImageView) itemView.findViewById(R.id.image);
            parentLayout = itemView.findViewById(R.id.parent_layout);

            parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent movie_intent = new Intent(mContext, MovieActivity.class);
                    movie_intent.putExtra("Title",title.getText().toString());
                    movie_intent.putExtra("Status",1);
                    Toast.makeText(mContext, key,
                            Toast.LENGTH_SHORT).show();
                    movie_intent.putExtra("Key",key);
                    mContext.startActivity(movie_intent);
                }
            });

        }
        public void bind(final Favorites favorites, final String key){
            title.setText(favorites.getTitle());
            Glide.with(mContext).load(favorites.getPoster()).diskCacheStrategy(DiskCacheStrategy.ALL).into(poster);
            Toast.makeText(mContext, key,
                    Toast.LENGTH_SHORT).show();
            this.key = key;

        }
    }
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

