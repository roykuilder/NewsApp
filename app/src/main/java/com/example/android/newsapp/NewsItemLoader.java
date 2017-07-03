package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsItemLoader extends AsyncTaskLoader<List<NewsItem>> {

    // Variable
    private String dataString;

    /**
     * Constructor for a new NewsItemLoader. It handles the loading of data from the Guardian
     * API on a background thread.
     */

    public NewsItemLoader(Context context, String url) {
        super(context);
        dataString = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
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
        return list;
    }
}
