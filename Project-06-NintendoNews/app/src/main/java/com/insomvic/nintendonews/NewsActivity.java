package com.insomvic.nintendonews;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderCallbacks<List<News>> {

    // Static variables relating to Guardian API
    private static final String API_KEY = "test";
    private static final String SEARCH_TEXT = "Nintendo";
    private static final int PAGE_SIZE = 30;
    private static final String REQUEST_URL =
            "http://content.guardianapis.com/search?section=games&order-by=newest&show-fields=thumbnail,bodyText&q=" + SEARCH_TEXT + "&page-size=" + PAGE_SIZE + "&api-key=" + API_KEY;
    private static final int NEWS_LOADER_ID = 1;
    // Adapter variables
    private NewsAdapter mAdapter;
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_activity);
        // Get reference id's for layout
        ListView newsListView = findViewById(R.id.list);
        mEmptyStateTextView = findViewById(R.id.empty);
        newsListView.setEmptyView(mEmptyStateTextView);
        // Init adapter
        mAdapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(mAdapter);
        // Init onClickListener
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mAdapter.getItem(position);
                Uri newsUri = Uri.parse(currentNews.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                startActivity(websiteIntent);
            }
        });
        // Get network status
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // Check network status
        if (networkInfo != null && networkInfo.isConnected()) {
            // Connection successful, so create loader
            LoaderManager loaderManager = getLoaderManager();
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        } else {
            // No internet connection
            View loadingIndicator = findViewById(R.id.loading);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create loader
        return new NewsLoader(this, REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Done loading
        View loadingIndicator = findViewById(R.id.loading);
        loadingIndicator.setVisibility(View.GONE);
        // If no news shows, "No news to fetch!" will be displayed
        mEmptyStateTextView.setText(R.string.no_news);
        mAdapter.clear();
        // Add news to the adapter
        if (news != null && !news.isEmpty()) {
            mAdapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Clear adapter
        mAdapter.clear();
    }
}