package dao;

import models.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    private static final String URL = "jdbc:sqlite:movies.db";

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void addMovie(String title, String genre, int year, int rating, String review, String imagePath, String userEmail) {
        String sql = "INSERT INTO movies(title, genre, year, rating, review, imagePath, user_email) VALUES(?,?,?,?,?,?,?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, genre);
            ps.setInt(3, year);
            ps.setInt(4, rating);
            ps.setString(5, review);
            ps.setString(6, imagePath);
            ps.setString(7, userEmail);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Movie> getMoviesByUser(String userEmail) {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies WHERE user_email = ?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("year"),
                        rs.getInt("rating"),
                        rs.getString("review"),
                        rs.getString("imagePath")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static List<Movie> getAllMovies() {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM movies";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                list.add(new Movie(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("genre"),
                        rs.getInt("year"),
                        rs.getInt("rating"),
                        rs.getString("review"),
                        rs.getString("imagePath")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static void deleteMovie(int id) {
        String sql = "DELETE FROM movies WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateMovie(int id, String title, String genre, int year, int rating, String review, String imagePath) {
        String sql = "UPDATE movies SET title=?, genre=?, year=?, rating=?, review=?, imagePath=? WHERE id=?";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, title);
            ps.setString(2, genre);
            ps.setInt(3, year);
            ps.setInt(4, rating);
            ps.setString(5, review);
            ps.setString(6, imagePath);
            ps.setInt(7, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addMovie(String t, String g, int y, int r, String rev, String s) {
    }
}