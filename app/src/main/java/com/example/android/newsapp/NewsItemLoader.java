package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class NewsItemLoader extends AsyncTaskLoader<List<NewsItem>> {

    // Variables
    private static final String LOG_TAG = "NewsItemLoader:";
    private String dataString;

    /**
     * Constructor for a new NewsItemLoader. It handles the loading of data from the Guardian
     * API on a background thread.
     *
     * @param context
     * @param url
     */

    public NewsItemLoader(Context context, String url) {
        super(context);
        dataString = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.v(LOG_TAG, "onStartLoading is called");
    }

    /**
     * Make the request to the Guardian API and return a List of NewsItems.
     */

    @Override
    public List<NewsItem> loadInBackground() {
        if (dataString == null) {
            return null;
        }

        List<NewsItem> list = Utils.getNewsItems(dataString);
        Log.v(LOG_TAG, "loadInBackground has finished");
        return list;
    }
}
