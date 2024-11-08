package com.example.firstapplication;

public class Book {
    private String author;
    private String genre;
    private String name;
    private String publicationDate;
    private int rating;

    public Book(String author, String genre, String name, String publicationDate, int rating) {
        this.author = author;
        this.genre = genre;
        this.name = name;
        this.publicationDate = publicationDate;
        this.rating = rating;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public String getName() {
        return name;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public int getRating() {
        return rating;
    }
}
