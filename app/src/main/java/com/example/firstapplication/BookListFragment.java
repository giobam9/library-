package com.example.firstapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BookListFragment extends Fragment implements BookAdapter.OnBookClickListener {
    private List<Book> books = new ArrayList<>();
    private BookAdapter bookAdapter;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        bookAdapter = new BookAdapter(books, this);
        recyclerView.setAdapter(bookAdapter);

        // Начинаем асинхронный запрос
        fetchBooks();

        return view;
    }

    private void fetchBooks() {
        new Thread(() -> {
            try {
                URL url = new URL("https://raw.githubusercontent.com/Lpirskaya/JsonLab/master/Books2022.json");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonBuilder.append(line);
                }
                reader.close();

                String jsonString = jsonBuilder.toString();
                parseJson(jsonString);
            } catch (Exception e) {
                Log.e("BookListFragment", "Error fetching books", e);
            }
        }).start();
    }

    private void parseJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);
                String author = bookObject.getString("Author");
                String genre = bookObject.getString("Genre");
                String name = bookObject.getString("Name");
                String publicationDate = bookObject.getString("PublicationDate");
                int rating = bookObject.getInt("rating");

                Book book = new Book(author, genre, name, publicationDate, rating);
                books.add(book);

                // Логируем данные о каждой книге
                Log.d("Book Info", "Author: " + author + ", Genre: " + genre + ", Name: " + name + ", PublicationDate: " + publicationDate + ", Rating: " + rating);
            }
            getActivity().runOnUiThread(() -> bookAdapter.notifyDataSetChanged());
        } catch (JSONException e) {
            Log.e("BookListFragment", "Error parsing JSON", e);
        }
    }

    @Override
    public void onBookClick(Book book) {
        Fragment selectedBookFragment = SelectedBookFragment.newInstance(
                book.getName(),
                book.getAuthor(),
                book.getGenre(),
                book.getPublicationDate(),
                book.getRating()
        );

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, selectedBookFragment)
                .addToBackStack(null)
                .commit();
    }
}
