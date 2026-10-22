package ui;

import dao.ListDAO;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class ListsFrame extends JFrame {

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

    public ListsFrame(String userEmail) {
        this(userEmail, null);
    }

    public ListsFrame(String userEmail, JFrame parentFrame) {
        this.userEmail   = userEmail;
        this.parentFrame = parentFrame;

        setTitle("My Lists");
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

        loadLists();
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(14, 18, 45));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C),
                new EmptyBorder(24, 60, 24, 60)
        ));

        JLabel title = new JLabel("My Lists");
        title.setForeground(new Color(100, 170, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JButton addBtn = styledButton("+ Create List", ACCENT, 150, 38);
        addBtn.addActionListener(e -> showCreateDialog());

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

    private void loadLists() {
        listPanel.removeAll();
        List<String> listNames = ListDAO.getListNames(userEmail);

        if (listNames.isEmpty()) {
            JLabel empty = new JLabel("No lists yet. Create one to organize your movies!");
            empty.setForeground(new Color(90, 120, 190));
            empty.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            empty.setAlignmentX(Component.CENTER_ALIGNMENT);
            empty.setBorder(new EmptyBorder(80, 0, 0, 0));
            listPanel.add(empty);
        } else {
            for (String name : listNames) {
                listPanel.add(createListRow(name));
                listPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createListRow(String listName) {
        List<String[]> movies = ListDAO.getMoviesInList(userEmail, listName);

        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setBackground(CARD_BG);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(14, 20, 14, 20)
        ));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(CARD_BG);

        JLabel nameLabel = new JLabel(listName);
        nameLabel.setForeground(WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel countLabel = new JLabel(movies.size() + " movie" + (movies.size() == 1 ? "" : "s"));
        countLabel.setForeground(LABEL_C);
        countLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        info.add(nameLabel);
        info.add(Box.createRigidArea(new Dimension(0, 5)));
        info.add(countLabel);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setBackground(CARD_BG);

        JButton viewBtn   = styledButton("View",   new Color(40, 80, 180),  80, 32);
        JButton deleteBtn = styledButton("Delete", new Color(180, 40, 40),  80, 32);

        viewBtn.addActionListener(e -> showMoviesInList(listName));
        deleteBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Delete list \"" + listName + "\"?", "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) { ListDAO.deleteList(userEmail, listName); loadLists(); }
        });

        btnPanel.add(viewBtn);
        btnPanel.add(deleteBtn);

        row.add(info,     BorderLayout.CENTER);
        row.add(btnPanel, BorderLayout.EAST);
        return row;
    }

    private void showMoviesInList(String listName) {
        List<String[]> movies = ListDAO.getMoviesInList(userEmail, listName);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(12, 16, 42));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        if (movies.isEmpty()) {
            panel.add(new JLabel("No movies in this list."));
        } else {
            for (String[] m : movies) {
                JPanel row = new JPanel(new BorderLayout());
                row.setBackground(new Color(20, 26, 60));
                row.setBorder(new EmptyBorder(8, 10, 8, 10));
                row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

                JLabel lbl = new JLabel(m[1] + "  (" + m[3] + ")  -  " + m[2]);
                lbl.setForeground(WHITE);
                lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));

                JButton removeBtn = styledButton("Remove", new Color(180, 40, 40), 80, 28);
                int id = Integer.parseInt(m[0]);
                removeBtn.addActionListener(e -> { ListDAO.removeMovieFromList(id); loadLists(); });

                row.add(lbl, BorderLayout.CENTER);
                row.add(removeBtn, BorderLayout.EAST);
                panel.add(row);
                panel.add(Box.createRigidArea(new Dimension(0, 4)));
            }
        }

        JButton addMovieBtn = styledButton("+ Add Movie", ACCENT, 140, 32);
        addMovieBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        addMovieBtn.addActionListener(e -> showAddMovieToListDialog(listName));
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(addMovieBtn);

        JOptionPane.showMessageDialog(this, new JScrollPane(panel), "List: " + listName, JOptionPane.PLAIN_MESSAGE);
        loadLists();
    }

    private void showAddMovieToListDialog(String listName) {
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

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Movie to " + listName, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String t = titleField.getText().trim();
            if (t.isEmpty()) { JOptionPane.showMessageDialog(this, "Please enter a title."); return; }
            try {
                ListDAO.createList(userEmail, listName, t, genreField.getText().trim(), yearField.getText().isEmpty() ? 0 : Integer.parseInt(yearField.getText().trim()));
                loadLists();
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Year must be a number."); }
        }
    }

    private void showCreateDialog() {
        JTextField nameField  = styledField("");
        JTextField titleField = styledField("");
        JTextField genreField = styledField("");
        JTextField yearField  = styledField("");

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(12, 16, 42));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        panel.add(dialogLabel("List Name"));        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(nameField);                       panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("First Movie Title")); panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(titleField);                      panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Genre"));             panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(genreField);                      panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(dialogLabel("Year"));              panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(yearField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Create New List", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String n = nameField.getText().trim();
            String t = titleField.getText().trim();
            if (n.isEmpty() || t.isEmpty()) { JOptionPane.showMessageDialog(this, "List name and movie title are required."); return; }
            try {
                ListDAO.createList(userEmail, n, t, genreField.getText().trim(), yearField.getText().isEmpty() ? 0 : Integer.parseInt(yearField.getText().trim()));
                loadLists();
            } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Year must be a number."); }
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