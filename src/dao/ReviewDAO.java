package dao;

import database.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewDAO {

    public static boolean addReview(String movieTitle, String userEmail, int rating, String comment) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO reviews(movie_title, user_email, rating, comment) VALUES(?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movieTitle);
            ps.setString(2, userEmail);
            ps.setInt(3, rating);
            ps.setString(4, comment);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getReviews(String movieTitle) {
        List<String> reviews = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT user_email, rating, comment FROM reviews WHERE movie_title=? ORDER BY id DESC"
            );
            ps.setString(1, movieTitle);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String email = rs.getString("user_email");
                int rating = rs.getInt("rating");
                String comment = rs.getString("comment");
                String stars = "★".repeat(rating) + "☆".repeat(5 - rating);
                reviews.add(email + "  " + stars + "\n" + comment);
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }
}