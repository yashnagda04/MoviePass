package com.example.demo.presentation.main;

import com.example.demo.data.model.Movie;

import java.util.List;

public interface MovieListContract {

    interface Model {

        interface OnFinishedListener {
            void onFinished(List<Movie> movieArrayList);

            void onFailure(Throwable t);
        }

        void getMovieList(OnFinishedListener onFinishedListener, int pageNo);

        void getSearchedMovies(OnSearchFinishedListener onSearchFinishedListener, String query, int pageNo);

        interface OnSearchFinishedListener {
            void onSearchFinished(List<Movie> searchMovieArrayList);

            void onSearchFailure(Throwable t);
        }

    }

    interface View {

        void showProgress();

        void hideProgress();

        void setDataToRecyclerView(List<Movie> movieArrayList);

        void onResponseFailure(Throwable throwable);

    }

    interface Presenter {

        void onDestroy();

        void getMoreData(int pageNo);

        void requestDataFromServer();

        void searchMovie(String query);

        void getMoreSearchMovie(String query, int pageNo);

    }
}