package com.example.demo.presentation.main;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.adapter.MoviesAdapter;
import com.example.demo.data.model.Movie;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieListContract.View,
        MovieItemClickListener {

    private static final String TAG = "MainActivity";
    @BindView(R.id.rv_movie_list)
    RecyclerView rvMovieList;
    @BindView(R.id.pb_loading)
    ProgressBar pbLoading;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private SearchView searchView;
    private MovieListPresenter movieListPresenter;
    private List<Movie> moviesList;
    private MoviesAdapter moviesAdapter;
    private int pageNo = 1;
    //Constants for load more
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    private GridLayoutManager mLayoutManager;

    //to decide if recycler view is loading search list
    private boolean isSearchON = false;

    private String searchQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initUI();

        setListeners();

        //Initializing presenter
        movieListPresenter = new MovieListPresenter(this);
        movieListPresenter.requestDataFromServer();
    }

    private void initUI() {

        moviesList = new ArrayList<>();
        moviesAdapter = new MoviesAdapter(this, moviesList);
        mLayoutManager = new GridLayoutManager(this, 2);
        rvMovieList.setLayoutManager(mLayoutManager);
        rvMovieList.setItemAnimator(new DefaultItemAnimator());
        rvMovieList.setAdapter(moviesAdapter);


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
                    if (isSearchON) {
                        movieListPresenter.getMoreSearchMovie(searchQuery, pageNo);
                    } else {
                        movieListPresenter.getMoreData(pageNo);
                    }
                    loading = true;
                }

                Log.d("loading", loading + " ," + visibleItemCount + " ," + totalItemCount + " ," + previousTotal + " ," + loading);

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

           /* moviesList.clear();
            moviesList.addAll(movieArrayList);
            moviesAdapter.notifyDataSetChanged();*/

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
    public void onMovieItemClick(int position) {
        if (position == -1) {
            return;
        }

        moviesList.remove(position);
        moviesAdapter.notifyItemRemoved(position);
        moviesAdapter.notifyItemRangeChanged(position, moviesList.size());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        setupSearchView();
        MenuItem searchItem = menu.findItem(R.id.search);
        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //search is closed. refreshing the recycler view for popular result
                isSearchON = false;
                previousTotal = 0;
                moviesList.clear();
                pageNo = 1;
                movieListPresenter.requestDataFromServer();
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(MainActivity.this, "" + query, Toast.LENGTH_SHORT).show();
                isSearchON = true;
                searchQuery = query;
                if (query == null || query.isEmpty())
                    return false;

                //refreshing the recycler view for search result
                previousTotal = 0;
                moviesList.clear();
                pageNo = 1;
                movieListPresenter.searchMovie(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            item.expandActionView();
        }
        return true;
    }


}
