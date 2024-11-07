package com.example.firstapplication;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchBooksTask extends AsyncTask<Void, Void, String> {

    private static final String BOOKS_URL = "https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/Books2022.json";

    @Override
    protected String doInBackground(Void... voids) {
        StringBuilder result = new StringBuilder();
        try {
            URL url = new URL(BOOKS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            reader.close();
        } catch (Exception e) {
            Log.e("com.example.firstapplication.FetchBooksTask", "Error fetching data", e);
        }
        return result.toString();
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        try {
            // Разбор JSON
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject book = jsonArray.getJSONObject(i);
                Log.d("com.example.firstapplication.Book Info", "Title: " + book.getString("title"));
                Log.d("com.example.firstapplication.Book Info", "Author: " + book.getString("author"));
                Log.d("com.example.firstapplication.Book Info", "Published Year: " + book.getString("publishedYear"));
                Log.d("com.example.firstapplication.Book Info", "Genre: " + book.getString("genre"));
            }
        } catch (Exception e) {
            Log.e("com.example.firstapplication.FetchBooksTask", "Error parsing JSON", e);
        }
    }
}
