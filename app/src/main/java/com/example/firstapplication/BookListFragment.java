package com.example.firstapplication;
import com.example.firstapplication.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Обратите внимание на этот импорт
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;



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
                String rating = bookObject.getString("rating"); // Убедитесь, что это поле доступно в JSON

                Book book = new Book(author, genre, name, publicationDate, rating);
                books.add(book);

                // Логируем данные о каждой книге
                Log.d("Book Info", "Author: " + author + ", Genre: " + genre + ", Name: " + name + ", PublicationDate: " + publicationDate + ", Rating: " + rating );
            }
            getActivity().runOnUiThread(() -> bookAdapter.notifyDataSetChanged()); // Обновляем адаптер
        } catch (JSONException e) {
            Log.d("BookListFragment", "Error parsing JSON", e);
        }
    }

    @Override
    public void onBookClick(Book book) {
        // Здесь можно реализовать логику для обработки клика на книгу
    }
}