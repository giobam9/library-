package com.example.firstapplication;
import com.example.firstapplication.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment; // Обратите внимание на этот импорт
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SelectedBookFragment extends Fragment {

    private static final String ARG_BOOK_TITLE = "book_title";

    public static SelectedBookFragment newInstance(String bookTitle) {
        SelectedBookFragment fragment = new SelectedBookFragment();
        Bundle args = new Bundle();
        args.putString(ARG_BOOK_TITLE, bookTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_selected_book, container, false);
        TextView bookTitleTextView = view.findViewById(R.id.bookTitleTextView);

        // Получаем название книги из аргументов
        if (getArguments() != null) {
            String bookTitle = getArguments().getString(ARG_BOOK_TITLE);
            bookTitleTextView.setText(bookTitle);
        }

        return view;
    }
}
