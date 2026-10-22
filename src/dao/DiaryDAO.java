package dao;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiaryDAO {

    public static boolean addEntry(String userEmail, String movieTitle, String watchDate, String notes, int rating) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO diary(user_email, movie_title, watch_date, notes, rating) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userEmail);
            ps.setString(2, movieTitle);
            ps.setString(3, watchDate);
            ps.setString(4, notes);
            ps.setInt(5, rating);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String[]> getEntries(String userEmail) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, movie_title, watch_date, notes, rating FROM diary WHERE user_email=? ORDER BY watch_date DESC"
            );
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("movie_title"),
                        rs.getString("watch_date"),
                        rs.getString("notes"),
                        String.valueOf(rs.getInt("rating"))
                });
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteEntry(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM diary WHERE id=?"
            );
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close(); conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateEntry(int id, String movieTitle, String watchDate, String notes, int rating) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "UPDATE diary SET movie_title=?, watch_date=?, notes=?, rating=? WHERE id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, movieTitle);
            ps.setString(2, watchDate);
            ps.setString(3, notes);
            ps.setInt(4, rating);
            ps.setInt(5, id);
            ps.executeUpdate();
            ps.close(); conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
