package ui;

import dao.DiaryDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class DiaryFrame extends JFrame {

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
    private static final Color GOLD     = new Color(255, 200, 50);
    private static final Color WHITE    = Color.WHITE;

    public DiaryFrame(String userEmail) {
        this(userEmail, null);
    }

    public DiaryFrame(String userEmail, JFrame parentFrame) {
        this.userEmail   = userEmail;
        this.parentFrame = parentFrame;

        setTitle("My Diary");
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

        loadDiary();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(14, 18, 45));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C),
                new EmptyBorder(24, 60, 24, 60)
        ));

        JLabel title = new JLabel("My Movie Diary");
        title.setForeground(new Color(100, 170, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton addBtn = styledButton("+ Log Movie", ACCENT, 150, 38);
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

    private void loadDiary() {
        listPanel.removeAll();
        List<String[]> entries = DiaryDAO.getEntries(userEmail);

        if (entries.isEmpty()) {
            JLabel empty = new JLabel("No diary entries yet. Log a movie you watched!");
            empty.setForeground(new Color(90, 120, 190));
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(80, 0, 0, 0));
            listPanel.add(empty);
        } else {
            for (String[] entry : entries) {
                listPanel.add(createRow(entry));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createRow(String[] entry) {
        int    id     = Integer.parseInt(entry[0]);
        int    rating = Integer.parseInt(entry[4]);

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_BG);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(14, 20, 14, 20)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(CARD_BG);

        JLabel titleLabel = new JLabel(entry[1]);
        titleLabel.setForeground(WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < rating; i++) stars.append("★");
        for (int i = rating; i < 5; i++) stars.append("☆");
        JLabel starsLabel = new JLabel(stars.toString());
        starsLabel.setForeground(GOLD);
        starsLabel.setFont(new Font("Dialog", Font.PLAIN, 14));

        JLabel dateLabel = new JLabel("Watched: " + entry[2]);
        dateLabel.setForeground(LABEL_C);
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JLabel notesLabel = new JLabel("<html><i>" + (entry[3] != null && !entry[3].isEmpty() ? entry[3] : "No notes.") + "</i></html>");
        notesLabel.setForeground(new Color(140, 155, 200));
        notesLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        info.add(titleLabel);
        info.add(Box.createRigidArea(new Dimension(0, 3)));
        info.add(starsLabel);
        info.add(Box.createRigidArea(new Dimension(0, 3)));
        info.add(dateLabel);
        info.add(Box.createRigidArea(new Dimension(0, 3)));
        info.add(notesLabel);

        JButton deleteBtn = styledButton("Delete", new Color(180, 40, 40), 80, 32);
        deleteBtn.addActionListener(e -> { DiaryDAO.deleteEntry(id); loadDiary(); });

        row.add(info,      BorderLayout.CENTER);
        row.add(deleteBtn, BorderLayout.EAST);
        return row;
    }

    private void showAddDialog() {
        JTextField titleField = styledField("");
        JTextField dateField  = styledField(LocalDate.now().toString());
        JTextArea  notesArea  = new JTextArea(3, 20);
        notesArea.setBackground(INPUT_BG); notesArea.setForeground(WHITE);
        notesArea.setCaretColor(WHITE); notesArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        notesArea.setLineWrap(true); notesArea.setWrapStyleWord(true);
        notesArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), new EmptyBorder(6, 10, 6, 10)));

        StarRating starRating = new StarRating();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(12, 16, 42));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        panel.add(dialogLabel("Movie Title"));             panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(titleField);                             panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Watch Date (YYYY-MM-DD)")); panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(dateField);                              panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Rating"));                  panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(starRating);                             panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Notes"));                   panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(new JScrollPane(notesArea));

        int result = JOptionPane.showConfirmDialog(this, panel, "Log Movie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String t = titleField.getText().trim();
            int    r = starRating.getRating();
            if (t.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter a movie title."); return; }
            if (r == 0)      { JOptionPane.showMessageDialog(this, "Please select a rating.");     return; }
            DiaryDAO.addEntry(userEmail, t, dateField.getText().trim(), notesArea.getText().trim(), r);
            loadDiary();
        }
    }

    private JButton styledButton(String text, Color bg, int w, int h) {
        JButton btn = new JButton(text);
        btn.setBackground(bg); btn.setForeground(WHITE);
        btn.setFocusPainted(false); btn.setBorderPainted(false);
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