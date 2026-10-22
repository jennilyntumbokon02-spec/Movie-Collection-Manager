package dao;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ListDAO {

    public static boolean createList(String userEmail, String listName, String movieTitle, String genre, int year) {
        try {
            Connection conn = DBConnection.getConnection();
            String sql = "INSERT INTO lists(user_email, list_name, movie_title, genre, year) VALUES(?,?,?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, userEmail);
            ps.setString(2, listName);
            ps.setString(3, movieTitle);
            ps.setString(4, genre);
            ps.setInt(5, year);
            ps.executeUpdate();
            ps.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String> getListNames(String userEmail) {
        List<String> names = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT DISTINCT list_name FROM lists WHERE user_email=? ORDER BY list_name"
            );
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                names.add(rs.getString("list_name"));
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public static List<String[]> getMoviesInList(String userEmail, String listName) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, movie_title, genre, year FROM lists WHERE user_email=? AND list_name=?"
            );
            ps.setString(1, userEmail);
            ps.setString(2, listName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("movie_title"),
                        rs.getString("genre"),
                        String.valueOf(rs.getInt("year"))
                });
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean removeMovieFromList(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM lists WHERE id=?"
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

    public static boolean deleteList(String userEmail, String listName) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM lists WHERE user_email=? AND list_name=?"
            );
            ps.setString(1, userEmail);
            ps.setString(2, listName);
            ps.executeUpdate();
            ps.close(); conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}