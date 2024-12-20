package com.example.firstapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private List<Book> books;
    private OnBookClickListener listener;
    private OnBookLongClickListener longClickListener;

    // Конструктор адаптера с двумя слушателями
    public BookAdapter(List<Book> books, OnBookClickListener listener, OnBookLongClickListener longClickListener) {
        this.books = books;
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    // Создание нового ViewHolder
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    // Привязка данных к элементу списка
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);

        // Обработка клика
        holder.itemView.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                listener.onBookClick(books.get(pos));
            }
        });

        // Обработка долгого нажатия
        holder.itemView.setOnLongClickListener(v -> {
            if (longClickListener != null) {
                longClickListener.onBookLongClick(book);
                return true; // Событие обработано
            }
            return false; // Передача события дальше
        });
    }

    // Возвращаем количество элементов в списке
    @Override
    public int getItemCount() {
        return books.size();
    }

    // Интерфейсы для обработки кликов
    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public interface OnBookLongClickListener {
        void onBookLongClick(Book book);
    }

    // ViewHolder для элемента списка
    public class BookViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleTextView;
        private final TextView authorTextView;
        private final TextView genreTextView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.bookTitleTextView);
            authorTextView = itemView.findViewById(R.id.bookAuthorTextView);
            genreTextView = itemView.findViewById(R.id.bookGenreTextView);
        }

        public void bind(Book book) {
            titleTextView.setText(book.getName());
            authorTextView.setText(book.getAuthor());
            genreTextView.setText(book.getGenre());
        }
    }
}
