package com.example.movieapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

public class RecyclerView_Fav {
    private Context mContext;
    private FavoriteAdapter mFavoriteAdapter;

    public void setConfig(RecyclerView recyclerView,Context context, List<Favorites> favorites,List<String> keys) {
        mContext = context;
        mFavoriteAdapter = new FavoriteAdapter(favorites,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mFavoriteAdapter);
    }

    class FavItemView extends RecyclerView.ViewHolder{
        private ImageView poster;
        private TextView title;

        private String key;

        public FavItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext).inflate(R.layout.layout_listitem,parent,false));

            title = (TextView) itemView.findViewById(R.id.image_name);
            poster = (ImageView) itemView.findViewById(R.id.image);

        }
        public void bind(Favorites favorites, String key){
            title.setText(favorites.getTitle());
            Glide.with(mContext).load(favorites.getPoster()).diskCacheStrategy(DiskCacheStrategy.ALL).into(poster);
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

