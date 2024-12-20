package com.example.firstapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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

        // Создаем адаптер с двумя слушателями
        bookAdapter = new BookAdapter(books, this, this::onLongBookClick);
        recyclerView.setAdapter(bookAdapter);

        // Загружаем книги
        loadBooksFromDatabase();
        fetchBooks();

        return view;
    }


    /**
     * Загружает книги из локальной базы данных.
     */
    private void loadBooksFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        books.clear();
        books.addAll(dbHelper.getAllBooks());
        bookAdapter.notifyDataSetChanged();
    }

    /**
     * Загружает книги из удаленного источника.
     */
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

    /**
     * Парсит JSON строку и добавляет книги в список.
     */
    private void parseJson(String jsonString) {
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            List<Book> parsedBooks = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject bookObject = jsonArray.getJSONObject(i);
                String author = bookObject.getString("Author");
                String genre = bookObject.getString("Genre");
                String name = bookObject.getString("Name");
                String publicationDate = bookObject.getString("PublicationDate");
                int rating = bookObject.getInt("rating");

                Book book = new Book(author, genre, name, publicationDate, rating);
                parsedBooks.add(book);
            }

            // Сохраняем книги в базу данных
            DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
            dbHelper.insertBooksFromJson(parsedBooks);

            // Обновляем данные в адаптере
            getActivity().runOnUiThread(() -> {
                books.clear();
                books.addAll(parsedBooks);
                bookAdapter.notifyDataSetChanged();
            });
        } catch (JSONException e) {
            Log.e("BookListFragment", "Error parsing JSON", e);
        }
    }

    /**
     * Обрабатывает событие клика по книге.
     */
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

    /**
     * Обрабатывает длительное нажатие на книгу.
     */

    public void onLongBookClick(Book book) {
        showDialog(book);
    }

    /**
     * Показывает диалоговое окно для взаимодействия с книгой.
     */
    private void showDialog(Book book) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Выберите действие")
                .setMessage("Книга: " + book.getName() + "\nАвтор: " + book.getAuthor())
                .setPositiveButton("В избранное", (dialog, which) -> addToFavorites(book))
                .setNegativeButton("Отмена", (dialog, which) -> dialog.dismiss())
                .show();
    }

    /**
     * Добавляет книгу в избранное.
     */
    private void addToFavorites(Book book) {
        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        if (dbHelper.addToFavorites(book)) {
            Log.i("BookListFragment", "Книга добавлена в избранное: " + book.getName());
        } else {
            Log.i("BookListFragment", "Книга уже в избранном: " + book.getName());
        }
    }
}
