package database;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseSetup {

    public static void createTables() {
        try {
            Connection conn = DBConnection.getConnection();
            Statement stmt = conn.createStatement();

            String users = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT NOT NULL UNIQUE," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "birthday TEXT NOT NULL," +
                    "phone TEXT NOT NULL," +
                    "password TEXT NOT NULL" +
                    ")";

            String movies = "CREATE TABLE IF NOT EXISTS movies (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "title TEXT," +
                    "genre TEXT," +
                    "year INTEGER," +
                    "rating INTEGER," +
                    "review TEXT," +
                    "imagePath TEXT," +
                    "user_email TEXT" +
                    ")";

            String reviews = "CREATE TABLE IF NOT EXISTS reviews (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "movie_title TEXT," +
                    "user_email TEXT," +
                    "rating INTEGER," +
                    "comment TEXT" +
                    ")";

            String watchlist = "CREATE TABLE IF NOT EXISTS watchlist (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_email TEXT NOT NULL," +
                    "movie_title TEXT NOT NULL," +
                    "genre TEXT," +
                    "year INTEGER," +
                    "date_added TEXT" +
                    ")";

            String diary = "CREATE TABLE IF NOT EXISTS diary (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_email TEXT NOT NULL," +
                    "movie_title TEXT NOT NULL," +
                    "watch_date TEXT," +
                    "notes TEXT," +
                    "rating INTEGER" +
                    ")";

            String lists = "CREATE TABLE IF NOT EXISTS lists (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_email TEXT NOT NULL," +
                    "list_name TEXT NOT NULL," +
                    "movie_title TEXT NOT NULL," +
                    "genre TEXT," +
                    "year INTEGER" +
                    ")";

            String tags = "CREATE TABLE IF NOT EXISTS tags (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "user_email TEXT NOT NULL," +
                    "movie_title TEXT NOT NULL," +
                    "tag_name TEXT NOT NULL" +
                    ")";

            stmt.execute(users);
            stmt.execute(movies);
            stmt.execute(reviews);
            stmt.execute(watchlist);
            stmt.execute(diary);
            stmt.execute(lists);
            stmt.execute(tags);

            stmt.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}