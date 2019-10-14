package com.example.demo.adapter;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.demo.R;
import com.example.demo.activities.main.MainActivity;
import com.example.demo.model.Movie;
import com.example.demo.network.ApiClient;

import java.util.List;


public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>
        implements Filterable {

    private MainActivity mainActivity;
    private List<Movie> movieList;
    private List<Movie> originalMovieList;

    public MoviesAdapter(MainActivity mainActivity, List<Movie> movieList) {
        this.mainActivity = mainActivity;
        this.movieList = movieList;
        this.originalMovieList = movieList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {

        Movie movie = movieList.get(position);

        holder.tvMovieTitle.setText(movie.getTitle());
        holder.tvMovieRatings.setText(String.valueOf(movie.getRating()));
        holder.tvReleaseDate.setText(movie.getReleaseDate());

        // loading album cover using Glide library
        Glide.with(mainActivity)
                .load(ApiClient.IMAGE_BASE_URL + movie.getThumbPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.pbLoadImage.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.pbLoadImage.setVisibility(View.GONE);
                        return false;
                    }
                })
                .apply(new RequestOptions().placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder))
                .into(holder.ivMovieThumb);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainActivity.onMovieItemClick(position);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }


    @Override
    public Filter getFilter() {
        return null;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvMovieTitle;
        TextView tvMovieRatings;
        TextView tvReleaseDate;
        ImageView ivMovieThumb;
        ProgressBar pbLoadImage;

        MyViewHolder(View itemView) {
            super(itemView);

            tvMovieTitle = itemView.findViewById(R.id.tv_movie_title);
            tvReleaseDate = itemView.findViewById(R.id.tv_release_date);
            tvMovieRatings = itemView.findViewById(R.id.tv_movie_ratings);
            ivMovieThumb = itemView.findViewById(R.id.iv_movie_thumb);
            pbLoadImage = itemView.findViewById(R.id.pb_load_image);
        }
    }
}