package com.example.android.newsapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.newsapp.Utils.LOG_TAG;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsItem>> {

    private ProgressBar loadingIndicator;
    private NewsItemAdapter adapter;
    private ListView newsListView;

    private static final String TEST_STRING = "https://content.guardianapis.com/search?api-key" +
            "=a412a2c2-8da6-4fc4-8e1d-8a40f5f5c6ff&format=json&q=music&show-fields=headline," +
            "byline&order-by=newest";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // creates an ArrayList and sets the a custom adapter on it.
        // the empty view is added to display a hint for the user.
        ArrayList<NewsItem> emptyList = new ArrayList<>();
        newsListView = (ListView) findViewById(R.id.news_list);
        adapter = new NewsItemAdapter(MainActivity.this, emptyList);
        newsListView.setAdapter(adapter);
        loadingIndicator = (ProgressBar) findViewById(R.id.progress_bar);

        // Check for internet connectivity and store the boolean isConnected.
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (isConnected) {
            // Start LoaderManager and show the loading bar.
            LoaderManager loaderManager = getLoaderManager();

            // Set loadingIndicator to visible to show the data is being retrieved.
            loadingIndicator.setVisibility(View.VISIBLE);

            // Start the Loader to het the data from the Guardian.
            loaderManager.initLoader(1, null, MainActivity.this);
        } else {
            // Inform the user that no internet connection is available
            TextView emptyListView = (TextView) findViewById(R.id.empty_list);
            newsListView.setEmptyView(emptyListView);

        }


    }

    @Override
    public Loader<List<NewsItem>> onCreateLoader(int i, Bundle bundle) {
        Log.v(LOG_TAG, "onCreateLoader()");
        // Create a new NewsItemLoader for the given URL
        return new NewsItemLoader(this, TEST_STRING);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsItem>> loader, final List<NewsItem> newsList) {
        // Clear adapter.
        Log.v(LOG_TAG, "onLoadFinished() is called");
        adapter.clear();

        // Check if the list hold valid Object and set the list on the adapter.
        if (newsList != null && !newsList.isEmpty()) {
            loadingIndicator.setVisibility(View.GONE);
            adapter.addAll(newsList);
            Log.v(LOG_TAG, "Finished Loading: adapter updated.");

        } else {
            loadingIndicator.setVisibility(View.GONE);
            TextView emptyListView = (TextView) findViewById(R.id.empty_list);
            emptyListView.setText(R.string.unable);
            newsListView.setEmptyView(emptyListView);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsItem>> loader) {
        // Loader reset, so we can clear out our existing data.
        adapter.clear();
        Log.v(LOG_TAG, "onLoaderReset adapter is cleared");
    }

    //Build the String for the http request and include the search result.
    private String makeURL(String searchValue) {
        //return API_REQUEST_1 + searchValue + API_REQUEST_2;
        return TEST_STRING;
    }
}
