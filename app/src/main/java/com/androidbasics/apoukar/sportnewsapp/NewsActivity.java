package com.androidbasics.apoukar.sportnewsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    public static final String LOG_TAG = NewsActivity.class.getName();
    private static final String URL_SPORT_CATEGORY = "https://content.guardianapis.com/search?section=sport&api-key=d62f91e7-da0f-4d7f-a5cf-4ff2aee0b788";
    private static final int NEWS_LOADER_ID = 1;
    private List<News> mNewsList;
    private NewsAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        mNewsList = new ArrayList<>();
        initToolbar();

        if (!interenetConnection()) {
            showNoConnection();
        } else {
            initRecycleView();
            startLoadingNews();
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, URL_SPORT_CATEGORY);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        mNewsList.clear();
        mNewsList.addAll(news);
        mAdapter.notifyDataSetChanged();
        if (mNewsList.isEmpty()) {
                showNoNews();
            }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        mNewsList.clear();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void initRecycleView() {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        mAdapter = new NewsAdapter(mNewsList, this);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    private boolean interenetConnection() {
        boolean result = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            result = true;
        }
        return result;
    }

    private void showNoConnection() {
        findViewById(R.id.noconnection).setVisibility(View.VISIBLE);
        findViewById(R.id.recycler_view).setVisibility(View.GONE);
    }

    private void startLoadingNews() {
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(NEWS_LOADER_ID, null, this);
    }

    private void showNoNews() {
        findViewById(R.id.nonews).setVisibility(View.VISIBLE);
        findViewById(R.id.recycler_view).setVisibility(View.GONE);
    }
}
