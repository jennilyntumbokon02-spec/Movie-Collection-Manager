package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

public class ProfileDropdown extends JPanel {

    private JPopupMenu popup;
    private String userEmail;
    private JFrame parentFrame;

    private static final Color BG      = new Color(5, 8, 24);
    private static final Color MENU_BG = new Color(14, 18, 45);
    private static final Color HOVER   = new Color(25, 32, 75);

    public ProfileDropdown(JFrame parentFrame, String userEmail, String firstName) {
        this.parentFrame = parentFrame;
        this.userEmail   = userEmail;

        setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        setBackground(BG);
        setOpaque(false);

        JButton profileBtn = new JButton(firstName + "  v");
        profileBtn.setBackground(new Color(20, 26, 60));
        profileBtn.setForeground(new Color(180, 210, 255));
        profileBtn.setFocusPainted(false);
        profileBtn.setBorderPainted(false);
        profileBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        profileBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profileBtn.setPreferredSize(new Dimension(140, 32));

        popup = new JPopupMenu();
        popup.setBackground(MENU_BG);
        popup.setBorder(BorderFactory.createLineBorder(new Color(40, 55, 120), 1));

        popup.add(menuItem("Profile",   e -> new ProfileFrame(userEmail, parentFrame)));
        popup.add(menuItem("Watchlist", e -> new WatchlistFrame(userEmail, parentFrame)));
        popup.add(menuItem("Diary",     e -> new DiaryFrame(userEmail, parentFrame)));
        popup.add(menuItem("Lists",     e -> new ListsFrame(userEmail, parentFrame)));
        popup.add(menuItem("Tags",      e -> new TagsFrame(userEmail, parentFrame)));
        popup.addSeparator();
        popup.add(menuItem("Sign Out",  e -> {
            int confirm = JOptionPane.showConfirmDialog(parentFrame,
                    "Are you sure you want to logout?", "Confirm Logout",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                LandingPageFrame.clearLoggedInUser();
                new LandingPageFrame();
                parentFrame.dispose();
            }
        }));

        profileBtn.addActionListener(e ->
                popup.show(profileBtn, 0, profileBtn.getHeight())
        );

        add(profileBtn);
    }

    private JMenuItem menuItem(String text, ActionListener action) {
        JMenuItem item = new JMenuItem(text);
        item.setBackground(MENU_BG);
        item.setForeground(Color.WHITE);
        item.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        item.setBorder(new EmptyBorder(8, 16, 8, 16));
        item.addActionListener(action);
        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { item.setBackground(HOVER); }
            public void mouseExited(MouseEvent e)  { item.setBackground(MENU_BG); }
        });
        return item;
    }
}