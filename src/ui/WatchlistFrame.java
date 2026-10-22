package ui;

import dao.WatchlistDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class WatchlistFrame extends JFrame {

    private String userEmail;
    private JFrame parentFrame;
    private JPanel listPanel;

    private static final Color BG       = new Color(5, 8, 24);
    private static final Color PANEL    = new Color(10, 14, 38);
    private static final Color CARD_BG  = new Color(14, 18, 45);
    private static final Color ACCENT   = new Color(0, 200, 120);
    private static final Color INPUT_BG = new Color(20, 26, 60);
    private static final Color BORDER_C = new Color(40, 55, 120);
    private static final Color LABEL_C  = new Color(120, 145, 200);
    private static final Color WHITE    = Color.WHITE;

    public WatchlistFrame(String userEmail) {
        this(userEmail, null);
    }

    public WatchlistFrame(String userEmail, JFrame parentFrame) {
        this.userEmail   = userEmail;
        this.parentFrame = parentFrame;

        setTitle("My Watchlist");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildHeader(), BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(PANEL);
        listPanel.setBorder(new EmptyBorder(16, 60, 16, 60));

        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        add(buildBottom(), BorderLayout.SOUTH);

        loadWatchlist();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(14, 18, 45));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C),
                new EmptyBorder(24, 60, 24, 60)
        ));

        JLabel title = new JLabel("My Watchlist");
        title.setForeground(new Color(100, 170, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton addBtn = styledButton("+ Add to Watchlist", ACCENT, 180, 38);
        addBtn.addActionListener(e -> showAddDialog());

        header.add(title,  BorderLayout.WEST);
        header.add(addBtn, BorderLayout.EAST);
        return header;
    }

    private JPanel buildBottom() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 12));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_C));

        if (parentFrame != null) {
            JButton homeBtn = styledButton("⌂  Home", new Color(30, 40, 90), 130, 38);
            homeBtn.addActionListener(e -> { dispose(); parentFrame.toFront(); parentFrame.requestFocus(); });
            panel.add(homeBtn);
        }
        return panel;
    }

    private void loadWatchlist() {
        listPanel.removeAll();
        List<String[]> items = WatchlistDAO.getWatchlist(userEmail);

        if (items.isEmpty()) {
            JLabel empty = new JLabel("Your watchlist is empty. Add movies you want to watch!");
            empty.setForeground(new Color(90, 120, 190));
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(80, 0, 0, 0));
            listPanel.add(empty);
        } else {
            for (String[] item : items) {
                listPanel.add(createRow(item));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createRow(String[] item) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_BG);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(14, 20, 14, 20)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 76));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(CARD_BG);

        JLabel titleLabel = new JLabel(item[0]);
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel metaLabel = new JLabel(item[1] + "  •  " + item[2] + "  •  Added: " + item[3]);
        metaLabel.setForeground(LABEL_C);
        metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        info.add(titleLabel);
        info.add(Box.createRigidArea(new Dimension(0, 5)));
        info.add(metaLabel);

        JButton removeBtn = styledButton("Remove", new Color(180, 40, 40), 90, 32);
        removeBtn.addActionListener(e -> { WatchlistDAO.removeFromWatchlist(userEmail, item[0]); loadWatchlist(); });

        row.add(info,      BorderLayout.CENTER);
        row.add(removeBtn, BorderLayout.EAST);
        return row;
    }

    private void showAddDialog() {
        JTextField titleField = styledField("");
        JTextField genreField = styledField("");
        JTextField yearField  = styledField("");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(12, 16, 42));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        panel.add(dialogLabel("Movie Title")); panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(titleField);                 panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Genre"));       panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(genreField);                 panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Year"));        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add to Watchlist", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String t = titleField.getText().trim();
            String g = genreField.getText().trim();
            String y = yearField.getText().trim();
            if (t.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter a movie title."); return; }
            try {
                boolean added = WatchlistDAO.addToWatchlist(userEmail, t, g, y.isEmpty() ? 0 : Integer.parseInt(y));
                if (!added) JOptionPane.showMessageDialog(this, "Movie already in watchlist!");
                loadWatchlist();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Year must be a number.");
            }
        }
    }

    private JButton styledButton(String text, Color bg, int w, int h) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(w, h));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }

    private JTextField styledField(String value) {
        JTextField f = new JTextField(value);
        f.setBackground(INPUT_BG); f.setForeground(WHITE); f.setCaretColor(WHITE);
        f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), new EmptyBorder(6, 10, 6, 10)));
        f.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        f.setAlignmentX(Component.LEFT_ALIGNMENT);
        return f;
    }

    private JLabel dialogLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(160, 185, 255));
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }
}