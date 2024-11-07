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

    public BookAdapter(List<Book> books, OnBookClickListener listener) {
        this.books = books;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.textView.setText(book.getName()); // Устанавливаем название книги
        holder.itemView.setOnClickListener(v -> listener.onBookClick(book));
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public interface OnBookClickListener {
        void onBookClick(Book book);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bookTitleTextView); // Убедитесь, что это правильный ID
        }
    }
}
