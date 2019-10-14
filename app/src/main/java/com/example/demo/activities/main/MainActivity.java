package com.example.demo.activities.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demo.R;
import com.example.demo.adapter.MoviesAdapter;
import com.example.demo.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MovieListContract.View,
        ShowEmptyView,MovieItemClickListener {

    private static final String TAG = "MainActivity";
    private MovieListPresenter movieListPresenter;
    private RecyclerView rvMovieList;
    private List<Movie> moviesList;
    private MoviesAdapter moviesAdapter;
    private ProgressBar pbLoading;
    private TextView tvEmptyView;

    private int pageNo = 1;

    //Constants for load more
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private GridLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();

        setListeners();

        //Initializing presenter
        movieListPresenter = new MovieListPresenter(this);

        movieListPresenter.requestDataFromServer();
    }

    private void initUI() {

        rvMovieList = findViewById(R.id.rv_movie_list);
        moviesList = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(this, moviesList);
        mLayoutManager = new GridLayoutManager(this, 2);
        rvMovieList.setLayoutManager(mLayoutManager);
        rvMovieList.setItemAnimator(new DefaultItemAnimator());
        rvMovieList.setAdapter(moviesAdapter);
        pbLoading = findViewById(R.id.pb_loading);
        tvEmptyView = findViewById(R.id.tv_empty_view);
    }


    private void setListeners() {

        rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = rvMovieList.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                // Handling the infinite scroll
                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    movieListPresenter.getMoreData(pageNo);
                    loading = true;
                }

            }
        });

    }

    @Override
    public void showProgress() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void setDataToRecyclerView(List<Movie> movieArrayList) {
        moviesList.addAll(movieArrayList);
        moviesAdapter.notifyDataSetChanged();

        // fetch data from next page no
        pageNo++;
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        Log.e(TAG, throwable.getMessage());
        Toast.makeText(this, "error", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        movieListPresenter.onDestroy();
    }

    @Override
    public void showEmptyView() {
        rvMovieList.setVisibility(View.GONE);
        tvEmptyView.setVisibility(View.VISIBLE);

    }

    @Override
    public void hideEmptyView() {
        rvMovieList.setVisibility(View.VISIBLE);
        tvEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void onMovieItemClick(int position) {
        if (position == -1) {
            return;
        }

        moviesList.remove(position);
        moviesAdapter.notifyItemRemoved(position);
        moviesAdapter.notifyItemRangeChanged(position,moviesList.size());
    }
}
