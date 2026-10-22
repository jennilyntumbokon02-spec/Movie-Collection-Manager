package dao;

import database.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TagDAO {

    public static boolean addTag(String userEmail, String movieTitle, String tagName) {
        try {
            Connection conn = DBConnection.getConnection();

            String check = "SELECT id FROM tags WHERE user_email=? AND movie_title=? AND tag_name=?";
            PreparedStatement ps = conn.prepareStatement(check);
            ps.setString(1, userEmail);
            ps.setString(2, movieTitle);
            ps.setString(3, tagName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                rs.close(); ps.close(); conn.close();
                return false;
            }
            rs.close(); ps.close();

            String sql = "INSERT INTO tags(user_email, movie_title, tag_name) VALUES(?,?,?)";
            PreparedStatement ps2 = conn.prepareStatement(sql);
            ps2.setString(1, userEmail);
            ps2.setString(2, movieTitle);
            ps2.setString(3, tagName);
            ps2.executeUpdate();
            ps2.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<String[]> getTagsByUser(String userEmail) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, movie_title, tag_name FROM tags WHERE user_email=? ORDER BY tag_name"
            );
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("movie_title"),
                        rs.getString("tag_name")
                });
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getTagsByMovie(String userEmail, String movieTitle) {
        List<String> tags = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT tag_name FROM tags WHERE user_email=? AND movie_title=? ORDER BY tag_name"
            );
            ps.setString(1, userEmail);
            ps.setString(2, movieTitle);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tags.add(rs.getString("tag_name"));
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tags;
    }

    public static List<String[]> getMoviesByTag(String userEmail, String tagName) {
        List<String[]> list = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT id, movie_title FROM tags WHERE user_email=? AND tag_name=?"
            );
            ps.setString(1, userEmail);
            ps.setString(2, tagName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new String[]{
                        String.valueOf(rs.getInt("id")),
                        rs.getString("movie_title")
                });
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static List<String> getDistinctTags(String userEmail) {
        List<String> tags = new ArrayList<>();
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT DISTINCT tag_name FROM tags WHERE user_email=? ORDER BY tag_name"
            );
            ps.setString(1, userEmail);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tags.add(rs.getString("tag_name"));
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tags;
    }

    public static boolean deleteTag(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM tags WHERE id=?"
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

    public static boolean deleteAllTagsForMovie(String userEmail, String movieTitle) {
        try {
            Connection conn = DBConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM tags WHERE user_email=? AND movie_title=?"
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
}
