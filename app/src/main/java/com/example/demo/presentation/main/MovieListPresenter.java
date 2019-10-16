package com.example.demo.presentation.main;

import com.example.demo.data.model.Movie;
import com.example.demo.data.repository.MovieListImpl;

import java.util.List;

public class MovieListPresenter implements MovieListContract.Presenter,
        MovieListContract.Model.OnFinishedListener, MovieListContract.Model.OnSearchFinishedListener {

    private MovieListContract.View movieListView;
    private MovieListContract.Model movieListModel;

    public MovieListPresenter(MovieListContract.View movieListView) {
        this.movieListView = movieListView;
        movieListModel = new MovieListImpl();
    }

    @Override
    public void onDestroy() {
        this.movieListView = null;
    }

    @Override
    public void getMoreData(int pageNo) {

        if (movieListView != null) {
            movieListView.showProgress();
        }
        movieListModel.getMovieList(this, pageNo);
    }

    @Override
    public void requestDataFromServer() {

        if (movieListView != null) {
            movieListView.showProgress();
        }
        movieListModel.getMovieList(this, 1);
    }

    @Override
    public void searchMovie(String query) {
        if (movieListView != null) {
            movieListView.showProgress();
        }
        movieListModel.getSearchedMovies(this, query, 1);
    }

    @Override
    public void getMoreSearchMovie(String query, int pageNo) {
        if (movieListView != null) {
            movieListView.showProgress();
        }
        movieListModel.getSearchedMovies(this, query, pageNo);
    }

    @Override
    public void onFinished(List<Movie> movieArrayList) {
        movieListView.setDataToRecyclerView(movieArrayList);
        if (movieListView != null) {
            movieListView.hideProgress();
        }
    }

    @Override
    public void onFailure(Throwable t) {

        movieListView.onResponseFailure(t);
        if (movieListView != null) {
            movieListView.hideProgress();
        }
    }


    @Override
    public void onSearchFinished(List<Movie> searchMovieArrayList) {
        movieListView.setDataToRecyclerView(searchMovieArrayList);
        if (movieListView != null) {
            movieListView.hideProgress();
        }
    }

    @Override
    public void onSearchFailure(Throwable t) {

    }
}
