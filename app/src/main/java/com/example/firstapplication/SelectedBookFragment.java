package com.example.firstapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.firstapplication.R;
import androidx.fragment.app.Fragment;

public class SelectedBookFragment extends Fragment {

    private static final String ARG_BOOK_TITLE = "book_title";
    private static final String ARG_BOOK_AUTHOR = "book_author";
    private static final String ARG_BOOK_GENRE = "book_genre";
    private static final String ARG_BOOK_PUBLICATION_DATE = "book_publication_date";
    private static final String ARG_BOOK_RATING = "book_rating";

    public static SelectedBookFragment newInstance(String title, String author, String genre, String publicationDate, double rating) {
        SelectedBookFragment fragment = new SelectedBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOK_TITLE, title);
        args.putString(ARG_BOOK_AUTHOR, author);
        args.putString(ARG_BOOK_GENRE, genre);
        args.putString(ARG_BOOK_PUBLICATION_DATE, publicationDate);
        args.putDouble(ARG_BOOK_RATING, rating);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_book, container, false);

        TextView bookTitleTextView = view.findViewById(R.id.bookTitle);
        TextView bookAuthorTextView = view.findViewById(R.id.bookAuthor);
        TextView bookGenreTextView = view.findViewById(R.id.bookGenre);
        TextView bookPublicationDateTextView = view.findViewById(R.id.bookPublicationDate);
        TextView bookRatingTextView = view.findViewById(R.id.bookRating);

        // Получаем данные из аргументов и устанавливаем их в TextView
        if (getArguments() != null) {
            String title = getArguments().getString(ARG_BOOK_TITLE);
            String author = getArguments().getString(ARG_BOOK_AUTHOR);
            String genre = getArguments().getString(ARG_BOOK_GENRE);
            String publicationDate = getArguments().getString(ARG_BOOK_PUBLICATION_DATE);
            double rating = getArguments().getDouble(ARG_BOOK_RATING);

            bookTitleTextView.setText(title);
            bookAuthorTextView.setText("Автор: " + author);
            bookGenreTextView.setText("Жанр: " + genre);
            bookPublicationDateTextView.setText("Год публикации: " + publicationDate);
            bookRatingTextView.setText("Рейтинг: " + rating);
        }

        return view;
    }
}
