package models;

public class Movie {

    private int id, year, rating;
    private String title, genre, review, imagePath, synopsis, cast, whereToWatch;

    // Constructor for database movies (user collection)
    public Movie(int id, String title, String genre, int year, int rating, String review, String imagePath) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.review = review;
        this.imagePath = imagePath;
        this.synopsis = review;
        this.cast = "";
        this.whereToWatch = "";
    }

    // Constructor for hardcoded landing page movies
    public Movie(String title, String genre, int year, int rating,
                 String synopsis, String cast, String whereToWatch, String imagePath) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.rating = rating;
        this.synopsis = synopsis;
        this.cast = cast;
        this.whereToWatch = whereToWatch;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getYear() { return year; }
    public int getRating() { return rating; }
    public String getReview() { return review; }
    public String getImagePath() { return imagePath; }
    public String getSynopsis() { return synopsis; }
    public String getCast() { return cast; }
    public String getWhereToWatch() { return whereToWatch; }
}