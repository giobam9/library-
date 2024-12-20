package com.example.firstapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Имя и версия базы данных
    private static final String DATABASE_NAME = "library.db";
    private static final int DATABASE_VERSION = 5;

    // Таблица пользователей
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_LOGIN = "login";
    private static final String COLUMN_USER_PASSWORD = "password";

    // Таблица книг
    private static final String TABLE_BOOKS = "Books";
    private static final String COLUMN_BOOK_ID = "id";
    private static final String COLUMN_BOOK_NAME = "name";
    private static final String COLUMN_BOOK_AUTHOR = "author";
    private static final String COLUMN_BOOK_GENRE = "genre";
    private static final String COLUMN_BOOK_PUBLICATION_DATE = "publication_date";
    private static final String COLUMN_BOOK_RATING = "rating";

    // Таблица избранного
    private static final String TABLE_FAVORITES = "Favorites";
    private static final String COLUMN_FAVORITE_ID = "id";
    private static final String COLUMN_FAVORITE_USER_ID = "user_id";
    private static final String COLUMN_FAVORITE_BOOK_ID = "book_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы Users
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_LOGIN + " TEXT NOT NULL, " +
                COLUMN_USER_PASSWORD + " TEXT NOT NULL)");

        // Создание таблицы Books
        db.execSQL("CREATE TABLE " + TABLE_BOOKS + " (" +
                COLUMN_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BOOK_NAME + " TEXT NOT NULL, " +
                COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, " +
                COLUMN_BOOK_GENRE + " TEXT NOT NULL, " +
                COLUMN_BOOK_PUBLICATION_DATE + " TEXT NOT NULL, " +
                COLUMN_BOOK_RATING + " REAL NOT NULL)");

        // Создание таблицы Favorites
        db.execSQL("CREATE TABLE " + TABLE_FAVORITES + " (" +
                COLUMN_FAVORITE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FAVORITE_USER_ID + " INTEGER NOT NULL, " +
                COLUMN_FAVORITE_BOOK_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY (" + COLUMN_FAVORITE_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                "FOREIGN KEY (" + COLUMN_FAVORITE_BOOK_ID + ") REFERENCES " + TABLE_BOOKS + "(" + COLUMN_BOOK_ID + "))");

        // Первичное наполнение данных
        seedUsers(db);
        seedBooks(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOOKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }

    // Методы для наполнения Users
    private void seedUsers(SQLiteDatabase db) {
        insertUser(db, "user1", "password1");
        insertUser(db, "user2", "password2");
    }

    private void insertUser(SQLiteDatabase db, String login, String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_LOGIN, login);
        values.put(COLUMN_USER_PASSWORD, password);
        db.insert(TABLE_USERS, null, values);
    }

    // Методы для наполнения Books
    private void seedBooks(SQLiteDatabase db) {
        insertBook(db, "Book One", "Author One", "Genre One", "2000", 4);
        insertBook(db, "Book Two", "Author Two", "Genre Two", "2010", 3);
    }

    private void insertBook(SQLiteDatabase db, String name, String author, String genre, String publicationDate, int rating) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_BOOK_NAME, name);
        values.put(COLUMN_BOOK_AUTHOR, author);
        values.put(COLUMN_BOOK_GENRE, genre);
        values.put(COLUMN_BOOK_PUBLICATION_DATE, publicationDate);
        values.put(COLUMN_BOOK_RATING, rating);
        db.insert(TABLE_BOOKS, null, values);
    }


    public void insertBooksFromJson(List<Book> books) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (Book book : books) {
                ContentValues values = new ContentValues();
                values.put("author", book.getAuthor());
                values.put("genre", book.getGenre());
                values.put("name", book.getName());
                values.put("publication_date", book.getPublicationDate());
                values.put("rating", book.getRating());
                db.insert(TABLE_BOOKS, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    // Получение всех книг из базы данных
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_BOOKS;
        Cursor cursor = db.rawQuery("SELECT * FROM Books", null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_GENRE));
                String publicationDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_PUBLICATION_DATE));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_RATING));

                books.add(new Book(author, genre, name, publicationDate, rating));
            } while (cursor.moveToNext());
        }
        cursor.close();

        return books;
    }

    // Добавление книги в избранное
    public boolean addToFavorites(int userId, int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_USER_ID, userId);
        values.put(COLUMN_FAVORITE_BOOK_ID, bookId);

        long result = db.insert(TABLE_FAVORITES, null, values);
        return result != -1; // Возвращает true, если запись успешна
    }




    // Получение избранных книг пользователя
    public List<Book> getFavoriteBooks(int userId) {
        List<Book> favoriteBooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT Books.* FROM " + TABLE_FAVORITES +
                " JOIN " + TABLE_BOOKS + " ON " + TABLE_FAVORITES + "." + COLUMN_FAVORITE_BOOK_ID + " = " + TABLE_BOOKS + "." + COLUMN_BOOK_ID +
                " WHERE " + TABLE_FAVORITES + "." + COLUMN_FAVORITE_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR));
                String genre = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_GENRE));
                String publicationDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_PUBLICATION_DATE));
                int rating = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_RATING));

                favoriteBooks.add(new Book(author, genre, name, publicationDate, rating));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return favoriteBooks;
    }


    public boolean addToFavorites(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_FAVORITE_BOOK_ID + " = (SELECT " + COLUMN_BOOK_ID +
                " FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_NAME + " = ?)";
        Cursor cursor = db.rawQuery(query, new String[]{book.getName()});
        if (cursor.getCount() > 0) {
            cursor.close();
            return false; // Книга уже в избранном
        }
        cursor.close();

        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE_USER_ID, 1); // Замените на текущего пользователя
        values.put(COLUMN_FAVORITE_BOOK_ID, getBookId(book));
        db.insert(TABLE_FAVORITES, null, values);
        return true;
    }

    private int getBookId(Book book) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_BOOK_ID + " FROM " + TABLE_BOOKS + " WHERE " + COLUMN_BOOK_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{book.getName()});
        if (cursor.moveToFirst()) {
            int bookId = cursor.getInt(0);
            cursor.close();
            return bookId;
        }
        cursor.close();
        return -1;
    }

}
