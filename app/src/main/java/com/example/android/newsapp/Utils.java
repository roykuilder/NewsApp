package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Creates an URL and return a List of NewsItem objects.
     */
    public static List<NewsItem> getNewsItems(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Get data from The Guardian API.
        List<NewsItem> newsList = extractFeatureFromJson(jsonResponse);

        return newsList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Set up a connection to The Guardian API and get the JSON response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the newsItems JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Makes a readable string from the streamed data.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return an List<NewsItem> from the parsed JSON response.
     */
    private static List<NewsItem> extractFeatureFromJson(String newsListJSON) {
        // Check if JSONObject is not empty
        if (TextUtils.isEmpty(newsListJSON)) {
            return null;
        }

        try {
            JSONObject baseJsonResponse = new JSONObject(newsListJSON);

            if (!baseJsonResponse.has("response")){
                return null;
            }

            JSONObject responseJSON = baseJsonResponse.getJSONObject("response");

            if (!responseJSON.has("results")){
                return null;
            }

            JSONArray resultsArray = responseJSON.getJSONArray("results");

            // Check if array is empty and return early or extract data to list.
            if (resultsArray.length() == 0) {
                return null;
            } else {
                ArrayList<NewsItem> newsItems = new ArrayList<>();

                // For each of the items in the resultsArray a new NewsItem is created and added to
                // the newsItems ArrayList.
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject item = resultsArray.getJSONObject(i);

                    // Get the Headline from the JSON response.
                    String headLine = item.getString("webTitle");

                    // Get the section name from the JSON response.
                    String section = item.getString("sectionName");

                    // Get the url from the JSON response.
                    String url = item.getString("webUrl");

                    // Get the author from the JSON response.
                    JSONObject fields = item.getJSONObject("fields");
                    String author = fields.getString("byline");

                    // Add new NewsItem to the list
                    newsItems.add(new NewsItem(headLine, author, url, section));
                }
                return newsItems;
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the data from the guardian", e);
        }
        return null;
    }
}

