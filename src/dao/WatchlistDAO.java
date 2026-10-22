package dao;

import database.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WatchlistDAO {

    public static boolean addToWatchlist(String userEmail, String movieTitle, String genre, int year) {
        try {
            Connection conn = DBConnection.getConnection();
            String check = "SELECT id FROM watchlist WHERE user_email=? AND movie_title=?";
            PreparedStatement ps = conn.prepareStatement(check);
            ps.setString(1, userEmail);
            ps.setString(2, movieTitle);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close(); ps.close(); conn.close();
                return false;
            }
            rs.close(); ps.close();

            String sql = "INSERT INTO watchlist(user_email, movie_title, genre, year, date_added) VALUES(?,?,?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setString(1, userEmail);
            ps2.setString(2, movieTitle);
            ps2.setString(3, genre);
            ps2.setInt(4, year);
            ps2.setString(5, LocalDate.now().toString());
            ps2.executeUpdate();
            ps2.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String[]> getWatchlist(String userEmail) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT movie_title, genre, year, date_added FROM watchlist WHERE user_email=? ORDER BY date_added DESC"
            );
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                        rs.getString("movie_title"),
                        rs.getString("genre"),
                        String.valueOf(rs.getInt("year")),
                        rs.getString("date_added")
                });
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean removeFromWatchlist(String userEmail, String movieTitle) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM watchlist WHERE user_email=? AND movie_title=?"
            );
            ps.setString(1, userEmail);
            ps.setString(2, movieTitle);
            ps.executeUpdate();
            ps.close(); conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isInWatchlist(String userEmail, String movieTitle) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id FROM watchlist WHERE user_email=? AND movie_title=?"
            );
            ps.setString(1, userEmail);
            ps.setString(2, movieTitle);
            ResultSet rs = ps.executeQuery();
            boolean found = rs.next();
            rs.close(); ps.close(); conn.close();
            return found;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}