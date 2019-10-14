package com.example.demo.activities.main;

import android.util.Log;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieListResponse;
import com.example.demo.network.ApiClient;
import com.example.demo.network.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListModel implements MovieListContract.Model {

    private final String TAG = "MovieListModel";
    private final String API_KEY = "727527f479d25c4ef62860b6b1c3fd07";


    /**
     * This function will fetch movies data
     * @param onFinishedListener
     * @param pageNo : Which page to load.
     */
    @Override
    public void getMovieList(final OnFinishedListener onFinishedListener, int pageNo) {

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<MovieListResponse> call = apiService.getPopularMovies(API_KEY, pageNo);
        call.enqueue(new Callback<MovieListResponse>() {
            @Override
            public void onResponse(Call<MovieListResponse> call, Response<MovieListResponse> response) {
                List<Movie> movies = response.body().getResults();
                Log.d(TAG, "Number of movies received: " + movies.size());
                onFinishedListener.onFinished(movies);
            }

            @Override
            public void onFailure(Call<MovieListResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
                onFinishedListener.onFailure(t);
            }
        });
    }

}