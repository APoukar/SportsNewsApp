package com.androidbasics.apoukar.sportnewsapp;

import android.annotation.SuppressLint;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.androidbasics.apoukar.sportnewsapp.NewsActivity.LOG_TAG;

public class QueryUtils {

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static List<News> fetchNews(String requestURL) {
        //Create URL object
        URL url = createUrl(requestURL);

        //Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error during making HTTP request: " + e);
        }

        //Extract relevant fields from the JSON response and create a list of {@link News}s
        List<News> news = extractFeatureFromJSON(jsonResponse);

        return news;
    }

    private static URL createUrl(String p_Url) {
        URL url = null;
        try {
            url = new URL(p_Url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL: " + e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHTTPRequest(URL url) throws IOException {
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        httpURLConnection = (HttpURLConnection) url.openConnection();
        try {

            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //if connection request was successful (response code 200),
            //than read the input stream and parse the response.
            if (httpURLConnection.getResponseCode() == 200) {
                inputStream = httpURLConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + httpURLConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    private static ArrayList<News> extractFeatureFromJSON(String jsonResponse) {

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.

        try {
            JSONObject object = new JSONObject(jsonResponse);
            JSONObject response = object.getJSONObject("response");
            JSONArray results = response.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject o = results.getJSONObject(i);
                String title = o.getString("webTitle");
                String section = o.getString("sectionName");
                String published = o.getString("webPublicationDate");
                String author = "";
                String url = o.getString("webUrl");

                news.add(new News(title, section, parseDate(published), author, url));
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem occurred while parsing the JSON response: " + e);
        }

        // Return the list of news
        return news;
    }

    @SuppressLint("SimpleDateFormat")
    private static String parseDate(String date) {
        if (date == null) {
            return null;
        }

        Date todaysDate = java.util.Calendar.getInstance().getTime();

        @SuppressLint("SimpleDateFormat") Date dateFormat = null;
        try {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem occured while parsing date in parseDate() method");
            return  null;
        }
        return new SimpleDateFormat("MM/dd HH:mm").format(dateFormat);

    }

}