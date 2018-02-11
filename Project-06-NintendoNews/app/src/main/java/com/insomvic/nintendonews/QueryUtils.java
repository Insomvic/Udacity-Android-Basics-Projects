package com.insomvic.nintendonews;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

// Helper methods related to requesting and receiving news data from Guardian API.
public final class QueryUtils {

    // Log name for easy debugging
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    // Query the Guardian data and return a list of news objects
    public static List<News> fetchNewsData(String requestUrl) {
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        List<News> news = extractFeatureFromJson(jsonResponse);
        return news;
    }

    // Returns new URL object from the given string URL
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL.", e);
        }
        return url;
    }

    // Make an HTTP request to the given URL
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                // Request was successful
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
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

    // Convert the InputStream into a String from HTTP response
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

    // Return objects from JSON
    private static List<News> extractFeatureFromJson(String newsJSON) {
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        // Create new ArrayList from JSON
        List<News> news = new ArrayList<>();
        try {
            // This is from the structure of API
            JSONObject jsonResponse = new JSONObject(newsJSON);
            JSONObject newsResponse = jsonResponse.getJSONObject("response");
            JSONArray newsArray = newsResponse.getJSONArray("results");
            // Once we get an array of the results, we can now grab each info from the articles
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject currentNews = newsArray.getJSONObject(i);
                String title = currentNews.getString("webTitle");
                String date = currentNews.getString("webPublicationDate");
                String section = currentNews.getString("sectionName");
                String url = currentNews.getString("webUrl");
                // Get custom field text (thumbnail image and body text)
                JSONObject fields = currentNews.getJSONObject("fields");
                String description = fields.getString("bodyText");
                String author = "Unknown";
                // Create temporary bitmap
                Bitmap thumbnail = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
                try {
                    author = fields.getString("byline");
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Couldn't find author", e);
                }
                // Start getting thumbnail image
                try {
                    String image = fields.getString("thumbnail");
                    thumbnail = getThumbnail(image);
                } catch (JSONException e) {
                    Log.e(LOG_TAG, "Couldn't find thumbnail", e);
                }
                // Create News object and add to ArrayList
                News article = new News(title, author, date, section, description, thumbnail, url);
                news.add(article);
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }
        return news;
    }

    private static Bitmap getThumbnail(String url) {
        try {
            URL imageUrl = new URL(url);
            Bitmap thumbnail = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
            return thumbnail;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Couldn't download thumbnail image", e);
            return null;
        }
    }

}
