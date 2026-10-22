package ui;

import dao.TagDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class TagsFrame extends JFrame {

    private String userEmail;
    private JFrame parentFrame;
    private JPanel tagsPanel;

    private static final Color BG       = new Color(5, 8, 24);
    private static final Color PANEL    = new Color(10, 14, 38);
    private static final Color CARD_BG  = new Color(14, 18, 45);
    private static final Color ACCENT   = new Color(0, 200, 120);
    private static final Color INPUT_BG = new Color(20, 26, 60);
    private static final Color BORDER_C = new Color(40, 55, 120);
    private static final Color LABEL_C  = new Color(120, 145, 200);
    private static final Color WHITE    = Color.WHITE;

    public TagsFrame(String userEmail) {
        this(userEmail, null);
    }

    public TagsFrame(String userEmail, JFrame parentFrame) {
        this.userEmail   = userEmail;
        this.parentFrame = parentFrame;

        setTitle("My Tags");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        add(buildHeader(), BorderLayout.NORTH);

        tagsPanel = new JPanel();
        tagsPanel.setLayout(new BoxLayout(tagsPanel, BoxLayout.Y_AXIS));
        tagsPanel.setBackground(PANEL);
        tagsPanel.setBorder(new EmptyBorder(16, 60, 16, 60));

        JScrollPane scroll = new JScrollPane(tagsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        add(scroll, BorderLayout.CENTER);

        add(buildBottom(), BorderLayout.SOUTH);

        loadTags();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(14, 18, 45));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C),
                new EmptyBorder(24, 60, 24, 60)
        ));

        JLabel title = new JLabel("My Tags");
        title.setForeground(new Color(100, 170, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton addBtn = styledButton("+ Add Tag", ACCENT, 130, 38);
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

    private void loadTags() {
        tagsPanel.removeAll();
        List<String> distinctTags = TagDAO.getDistinctTags(userEmail);

        if (distinctTags.isEmpty()) {
            JLabel empty = new JLabel("No tags yet. Tag your movies to organize them!");
            empty.setForeground(new Color(90, 120, 190));
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(80, 0, 0, 0));
            tagsPanel.add(empty);
        } else {
            for (String tag : distinctTags) {
                tagsPanel.add(createTagRow(tag));
                tagsPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        tagsPanel.revalidate();
        tagsPanel.repaint();
    }

    private JPanel createTagRow(String tagName) {
        List<String[]> movies = TagDAO.getMoviesByTag(userEmail, tagName);

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

        JLabel tagLabel = new JLabel("# " + tagName);
        tagLabel.setForeground(new Color(140, 200, 255));
        tagLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel countLabel = new JLabel(movies.size() + " movie" + (movies.size() == 1 ? "" : "s") + " tagged");
        countLabel.setForeground(LABEL_C);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        info.add(tagLabel);
        info.add(Box.createRigidArea(new Dimension(0, 5)));
        info.add(countLabel);

        JButton viewBtn = styledButton("View", new Color(40, 80, 180), 80, 32);
        viewBtn.addActionListener(e -> showMoviesWithTag(tagName));

        row.add(info,    BorderLayout.CENTER);
        row.add(viewBtn, BorderLayout.EAST);
        return row;
    }

    private void showMoviesWithTag(String tagName) {
        List<String[]> movies = TagDAO.getMoviesByTag(userEmail, tagName);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(12, 16, 42));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (movies.isEmpty()) {
            panel.add(new JLabel("No movies with this tag."));
        } else {
            for (String[] m : movies) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(new Color(20, 26, 60));
                row.setBorder(new EmptyBorder(8, 10, 8, 10));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));

                JLabel lbl = new JLabel(m[1]);
                lbl.setForeground(WHITE);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                JButton removeBtn = styledButton("Remove", new Color(180, 40, 40), 80, 28);
                int id = Integer.parseInt(m[0]);
                removeBtn.addActionListener(e -> { TagDAO.deleteTag(id); loadTags(); });

                row.add(lbl, BorderLayout.CENTER);
                row.add(removeBtn, BorderLayout.EAST);
                panel.add(row);
                panel.add(Box.createRigidArea(new Dimension(0, 4)));
            }
        }

        JOptionPane.showMessageDialog(this, new JScrollPane(panel), "Tag: #" + tagName, JOptionPane.PLAIN_MESSAGE);
        loadTags();
    }

    private void showAddDialog() {
        JTextField movieField = styledField("");
        JTextField tagField   = styledField("");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(12, 16, 42));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        panel.add(dialogLabel("Movie Title")); panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(movieField);                 panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Tag Name"));    panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(tagField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Tag", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String movie = movieField.getText().trim();
            String tag   = tagField.getText().trim();
            if (movie.isEmpty() || tag.isEmpty()) { JOptionPane.showMessageDialog(this, "Both fields are required."); return; }
            boolean added = TagDAO.addTag(userEmail, movie, tag);
            if (!added) JOptionPane.showMessageDialog(this, "This tag already exists for that movie.");
            loadTags();
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