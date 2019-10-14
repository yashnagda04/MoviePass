package com.example.demo.network;

import com.example.demo.model.MovieListResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/popular")
    Call<MovieListResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int PageNo);
}