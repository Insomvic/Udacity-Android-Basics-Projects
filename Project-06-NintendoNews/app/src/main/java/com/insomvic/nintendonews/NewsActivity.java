package com.insomvic.nintendonews;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity
        implements LoaderCallbacks<List<News>>,
        SharedPreferences.OnSharedPreferenceChangeListener{

    // Request API URL
    private static final int MIN_NEWS_COUNT = 5;
    private static final int MAX_NEWS_COUNT = 25;
    private static final String API_KEY = "test";
    private static final String SECTION_KEY = "games";
    private static final String SHOW_FIELDS_KEY = "thumbnail,bodyText,byline";
    private static final String SEARCH_KEY = "Nintendo";
    private static final String REQUEST_URL = "http://content.guardianapis.com/search";
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
        // get default shared preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // register values to know when a preference has changed
        prefs.registerOnSharedPreferenceChangeListener(this);
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
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_news_count_key)) ||
                key.equals(getString(R.string.settings_order_by_key))){
            // Clear adapter
            mAdapter.clear();
            mEmptyStateTextView.setVisibility(View.GONE);
            // Show the loading indicator while fetching data
            View loadingIndicator = findViewById(R.id.loading);
            loadingIndicator.setVisibility(View.VISIBLE);
            // Restart the loader
            getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Get preferences
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Get page-size preferences
        String newsCount = sharedPrefs.getString (
                getString(R.string.settings_news_count_key),
                getString(R.string.settings_news_count_default));
        // Get order-by preferences
        String orderBy = sharedPrefs.getString (
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));
        // Make sure user defines reasonable amount of pages to load
        if (Integer.parseInt(newsCount) > MAX_NEWS_COUNT) {
            newsCount = Integer.toString(MAX_NEWS_COUNT);
            Toast.makeText(NewsActivity.this, getString(R.string.max_amount) + MAX_NEWS_COUNT + getString(R.string.check_settings), Toast.LENGTH_SHORT).show();
        } else if (Integer.parseInt(newsCount) < MIN_NEWS_COUNT) {
            newsCount = Integer.toString(MIN_NEWS_COUNT);
            Toast.makeText(NewsActivity.this, getString(R.string.min_amount) + MIN_NEWS_COUNT + getString(R.string.check_settings), Toast.LENGTH_SHORT).show();
        }
        // Build URI based on API queries
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        // Add query parameters for specific search
        uriBuilder.appendQueryParameter("section", SECTION_KEY);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-fields", SHOW_FIELDS_KEY);
        uriBuilder.appendQueryParameter("q", SEARCH_KEY);
        uriBuilder.appendQueryParameter("page-size", newsCount);
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        // Create loader based on preferences
        return new NewsLoader(this, uriBuilder.toString());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}