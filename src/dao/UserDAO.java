package dao;

import database.DBConnection;
import java.security.MessageDigest;
import java.sql.*;
import java.time.LocalDate;

public class UserDAO {
    public static String getBio(String email) {
        ensureColumn("bio", "TEXT");
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return null;
            PreparedStatement ps = conn.prepareStatement("SELECT bio FROM users WHERE email=?");
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            String val = rs.next() ? rs.getString("bio") : null;
            rs.close(); ps.close(); conn.close();
            return val;
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    public static boolean updateBio(String email, String bio) {
        ensureColumn("bio", "TEXT");
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return false;
            PreparedStatement ps = conn.prepareStatement("UPDATE users SET bio=? WHERE email=?");
            ps.setString(1, bio);
            ps.setString(2, email);
            int rows = ps.executeUpdate();
            ps.close(); conn.close();
            return rows > 0;
        } catch (Exception e) { e.printStackTrace(); return false; }
    }

    public static String getMemberSince(String email) {
        ensureColumn("created_at", "TEXT");
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return null;
            PreparedStatement check = conn.prepareStatement("SELECT created_at FROM users WHERE email=?");
            check.setString(1, email);
            ResultSet rs = check.executeQuery();
            String val = rs.next() ? rs.getString("created_at") : null;
            rs.close(); check.close();
            if (val == null || val.isEmpty()) {
                PreparedStatement upd = conn.prepareStatement("UPDATE users SET created_at=? WHERE email=?");
                String today = LocalDate.now().toString();
                upd.setString(1, today);
                upd.setString(2, email);
                upd.executeUpdate();
                upd.close();
                val = today;
            }
            conn.close();
            return val;
        } catch (Exception e) { e.printStackTrace(); return null; }
    }

    private static void ensureColumn(String column, String type) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return;
            try {
                Statement st = conn.createStatement();
                st.execute("ALTER TABLE users ADD COLUMN " + column + " " + type);
                st.close();
            } catch (SQLException ignored) {}
            conn.close();
        } catch (Exception e) { e.printStackTrace(); }
    }

    private static boolean columnChecked = false;

    private static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return password;
        }
    }

    private static void ensureProfilePictureColumn() {
        if (columnChecked) return;
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return;
            try {
                Statement st = conn.createStatement();
                st.execute("ALTER TABLE users ADD COLUMN profile_picture TEXT");
                st.close();
            } catch (SQLException ignored) {}
            conn.close();
            columnChecked = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean addUser(String email, String first, String last,
                                  String birthday, String phone, String password) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return false;
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO users(email, first_name, last_name, birthday, phone, password) VALUES(?,?,?,?,?,?)"
            );
            ps.setString(1, email);
            ps.setString(2, first);
            ps.setString(3, last);
            ps.setString(4, birthday);
            ps.setString(5, phone);
            ps.setString(6, hashPassword(password));
            int result = ps.executeUpdate();
            ps.close();
            conn.close();
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(String email, String password) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return false;
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT * FROM users WHERE email=? AND password=?"
            );
            ps.setString(1, email);
            ps.setString(2, hashPassword(password));
            ResultSet rs = ps.executeQuery();
            boolean found = rs.next();
            rs.close();
            ps.close();
            conn.close();
            return found;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getFirstName(String email) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return "User";
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT first_name FROM users WHERE email=?"
            );
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String name = rs.getString("first_name");
                rs.close(); ps.close(); conn.close();
                return name;
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "User";
    }

    public static String[] getUserInfo(String email) {

        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return new String[]{"", "", "", ""};
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT first_name, last_name, birthday, phone FROM users WHERE email=?"
            );
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String[] info = {
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("birthday"),
                        rs.getString("phone")
                };
                rs.close(); ps.close(); conn.close();
                return info;
            }
            rs.close(); ps.close(); conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{"", "", "", ""};
    }

    public static boolean updateUserInfo(String email, String firstName, String lastName,
                                         String birthday, String phone) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return false;
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET first_name=?, last_name=?, birthday=?, phone=? WHERE email=?"
            );
            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, birthday);
            ps.setString(4, phone);
            ps.setString(5, email);
            int rows = ps.executeUpdate();
            ps.close(); conn.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getProfilePicture(String email) {
        ensureProfilePictureColumn();
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return null;
            PreparedStatement ps = conn.prepareStatement(
                    "SELECT profile_picture FROM users WHERE email=?"
            );
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            String path = null;
            if (rs.next()) path = rs.getString("profile_picture");
            rs.close(); ps.close(); conn.close();
            return path;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean updateProfilePicture(String email, String imagePath) {
        ensureProfilePictureColumn();
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return false;
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET profile_picture=? WHERE email=?"
            );
            ps.setString(1, imagePath);
            ps.setString(2, email);
            int rows = ps.executeUpdate();
            ps.close(); conn.close();
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean changePassword(String email, String currentPassword, String newPassword) {
        try {
            Connection conn = DBConnection.getConnection();
            if (conn == null) return false;
            PreparedStatement check = conn.prepareStatement(
                    "SELECT id FROM users WHERE email=? AND password=?"
            );
            check.setString(1, email);
            check.setString(2, hashPassword(currentPassword));
            ResultSet rs = check.executeQuery();
            if (!rs.next()) {
                rs.close(); check.close(); conn.close();
                return false;
            }
            rs.close(); check.close();
            PreparedStatement ps = conn.prepareStatement(
                    "UPDATE users SET password=? WHERE email=?"
            );
            ps.setString(1, hashPassword(newPassword));
            ps.setString(2, email);
            ps.executeUpdate();
            ps.close(); conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }
}