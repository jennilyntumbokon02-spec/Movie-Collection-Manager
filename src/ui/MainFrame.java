package ui;

import dao.MovieDAO;
import dao.UserDAO;
import models.Movie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    private JPanel movieGrid;
    private String userEmail;
    private JTextField searchField;
    private JComboBox<String> genreFilter, ratingFilter, sortBox;
    private JLabel movieCountLabel;
    private List<Movie> currentMovies;

    private static final Color BG          = new Color(5, 8, 24);
    private static final Color PANEL       = new Color(8, 12, 35);
    private static final Color CARD_BG     = new Color(14, 18, 45);
    private static final Color CARD_HOVER  = new Color(20, 26, 65);
    private static final Color ACCENT      = new Color(0, 200, 120);
    private static final Color DIALOG_BG   = new Color(12, 16, 42);
    private static final Color INPUT_BG    = new Color(20, 26, 60);
    private static final Color LABEL_COLOR = new Color(160, 185, 255);
    private static final Color GOLD        = new Color(255, 200, 50);
    private static final Color BORDER_DIM  = new Color(40, 55, 120);

    private static final int CARD_W   = 200;
    private static final int POSTER_H = 280;
    private static final int INFO_H   = 110;
    private static final int CARD_H   = POSTER_H + INFO_H;
    private static final int GAP      = 16;

    public MainFrame(String userEmail) {
        this.userEmail = userEmail;
        setTitle("My Movies");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG);

        // ── HEADER ──────────────────────────────────────────────
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BG);
        header.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(18, 40, 18, 40),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(30, 45, 100))
        ));

        JPanel leftHeader = new JPanel(new FlowLayout(FlowLayout.LEFT, 16, 0));
        leftHeader.setBackground(BG);

        JPanel logoPanel = new JPanel();
        logoPanel.setLayout(new BoxLayout(logoPanel, BoxLayout.Y_AXIS));
        logoPanel.setOpaque(false);
        logoPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel logoLabel = new JLabel("MATLYN");
        logoLabel.setForeground(new Color(100, 170, 255));
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subLabel = new JLabel("Movie Collection");
        subLabel.setForeground(new Color(60, 85, 150));
        subLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        subLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        logoPanel.add(logoLabel);
        logoPanel.add(subLabel);

        logoPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new LandingPageFrame();
                dispose();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                logoLabel.setForeground(new Color(140, 200, 255));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                logoLabel.setForeground(new Color(100, 170, 255));
            }
        });

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(220, 32));
        searchField.setBackground(INPUT_BG);
        searchField.setForeground(Color.WHITE);
        searchField.setCaretColor(Color.WHITE);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DIM, 1),
                new EmptyBorder(4, 10, 4, 10)
        ));
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { filterMovies(); }
            public void removeUpdate(DocumentEvent e)  { filterMovies(); }
            public void changedUpdate(DocumentEvent e) { filterMovies(); }
        });

        genreFilter  = new JComboBox<>(new String[]{"All Genres","Action","Drama","Comedy","Horror","Sci-Fi","Romance","Thriller"});
        ratingFilter = new JComboBox<>(new String[]{"All Ratings","1","2","3","4","5"});
        sortBox      = new JComboBox<>(new String[]{"Sort: Default","A - Z","Z - A","Rating: High","Rating: Low","Year: Newest","Year: Oldest"});

        styleComboBox(genreFilter);
        styleComboBox(ratingFilter);
        styleComboBox(sortBox);

        genreFilter.addActionListener(e  -> filterMovies());
        ratingFilter.addActionListener(e -> filterMovies());
        sortBox.addActionListener(e      -> filterMovies());

        leftHeader.add(logoPanel);
        leftHeader.add(Box.createHorizontalStrut(16));
        leftHeader.add(searchField);
        leftHeader.add(genreFilter);
        leftHeader.add(ratingFilter);
        leftHeader.add(sortBox);

        JPanel rightHeader = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightHeader.setBackground(BG);

        String firstName = UserDAO.getFirstName(userEmail);
        ProfileDropdown profileDropdown = new ProfileDropdown(this, userEmail, firstName);

        JButton addBtn = new JButton("+ Add Movie");
        addBtn.setBackground(ACCENT);
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setBorderPainted(false);
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        addBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addBtn.setPreferredSize(new Dimension(130, 32));
        addBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { addBtn.setBackground(new Color(0, 170, 100)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { addBtn.setBackground(ACCENT); }
        });
        addBtn.addActionListener(e -> showAddDialog());

        rightHeader.add(profileDropdown);
        rightHeader.add(addBtn);

        header.add(leftHeader,  BorderLayout.WEST);
        header.add(rightHeader, BorderLayout.EAST);
        add(header, BorderLayout.NORTH);

        // ── SUBHEADER ────────────────────────────────────────────
        JPanel centerWrapper = new JPanel(new BorderLayout());
        centerWrapper.setBackground(PANEL);

        JPanel subHeader = new JPanel(new FlowLayout(FlowLayout.LEFT));
        subHeader.setBackground(BG);
        subHeader.setBorder(new EmptyBorder(8, 44, 8, 44));

        movieCountLabel = new JLabel("");
        movieCountLabel.setForeground(new Color(100, 130, 210));
        movieCountLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        subHeader.add(movieCountLabel);
        centerWrapper.add(subHeader, BorderLayout.NORTH);

        // ── MOVIE GRID — auto-fit ────────────────────────────────
        movieGrid = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        movieGrid.setBackground(PANEL);
        movieGrid.setBorder(new EmptyBorder(20, 36, 30, 36));

        JScrollPane scroll = new JScrollPane(movieGrid);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(PANEL);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        // listen to resize to recalculate columns
        scroll.getViewport().addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                if (currentMovies != null) renderMovies(currentMovies);
            }
        });

        centerWrapper.add(scroll, BorderLayout.CENTER);
        add(centerWrapper, BorderLayout.CENTER);

        loadMovies();
        setVisible(true);
    }

    private int calculateColumns(int availableWidth) {
        int usable = availableWidth - 72; // subtract left+right border padding
        int cols = Math.max(1, usable / (CARD_W + GAP));
        return cols;
    }

    private void styleComboBox(JComboBox<String> box) {
        box.setBackground(INPUT_BG);
        box.setForeground(Color.WHITE);
        box.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        box.setFocusable(false);
        box.setPreferredSize(new Dimension(box.getPreferredSize().width, 32));
    }

    private void loadMovies() {
        currentMovies = MovieDAO.getMoviesByUser(userEmail);
        renderMovies(currentMovies);
    }

    private void filterMovies() {
        String search = searchField.getText().toLowerCase().trim();
        String genre  = genreFilter.getSelectedItem().toString();
        String rating = ratingFilter.getSelectedItem().toString();
        String sort   = sortBox.getSelectedItem().toString();

        currentMovies = MovieDAO.getMoviesByUser(userEmail)
                .stream()
                .filter(m -> search.isEmpty() || m.getTitle().toLowerCase().contains(search))
                .filter(m -> genre.equals("All Genres")   || m.getGenre().equalsIgnoreCase(genre))
                .filter(m -> rating.equals("All Ratings") || m.getRating() == Integer.parseInt(rating))
                .collect(Collectors.toList());

        if      (sort.equals("A - Z"))        currentMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER));
        else if (sort.equals("Z - A"))        currentMovies.sort(Comparator.comparing(Movie::getTitle, String.CASE_INSENSITIVE_ORDER).reversed());
        else if (sort.equals("Rating: High")) currentMovies.sort(Comparator.comparingInt(Movie::getRating).reversed());
        else if (sort.equals("Rating: Low"))  currentMovies.sort(Comparator.comparingInt(Movie::getRating));
        else if (sort.equals("Year: Newest")) currentMovies.sort(Comparator.comparingInt(Movie::getYear).reversed());
        else if (sort.equals("Year: Oldest")) currentMovies.sort(Comparator.comparingInt(Movie::getYear));

        renderMovies(currentMovies);
    }

    private void renderMovies(List<Movie> movies) {
        movieGrid.removeAll();

        int count = movies.size();
        movieCountLabel.setText(count == 0 ? ""
                : count + " movie" + (count == 1 ? "" : "s") + " in your collection");

        if (movies.isEmpty()) {
            movieGrid.setLayout(new BorderLayout());

            JPanel emptyPanel = new JPanel();
            emptyPanel.setLayout(new BoxLayout(emptyPanel, BoxLayout.Y_AXIS));
            emptyPanel.setBackground(PANEL);

            JLabel emptyIcon = new JLabel("[ No Movies ]");
            emptyIcon.setFont(new Font("Segoe UI", Font.BOLD, 32));
            emptyIcon.setForeground(new Color(40, 55, 110));
            emptyIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

            boolean noFilter = searchField.getText().trim().isEmpty()
                    && genreFilter.getSelectedItem().toString().equals("All Genres")
                    && ratingFilter.getSelectedItem().toString().equals("All Ratings");

            JLabel emptyLabel = new JLabel(noFilter
                    ? "No movies yet. Click + Add Movie to get started!"
                    : "No movies match your search or filter.");
            emptyLabel.setForeground(new Color(90, 120, 190));
            emptyLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            emptyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            emptyPanel.add(Box.createVerticalStrut(100));
            emptyPanel.add(emptyIcon);
            emptyPanel.add(Box.createVerticalStrut(14));
            emptyPanel.add(emptyLabel);
            movieGrid.add(emptyPanel, BorderLayout.CENTER);

        } else {
            // auto-fit: calculate columns based on current viewport width
            int viewWidth = movieGrid.getParent() != null
                    ? movieGrid.getParent().getWidth()
                    : getWidth();
            int cols = calculateColumns(viewWidth);

            movieGrid.setLayout(new GridLayout(0, cols, GAP, GAP));
            for (Movie movie : movies) {
                movieGrid.add(createCard(movie));
            }

            // fill remaining cells in last row with empty panels
            int remainder = movies.size() % cols;
            if (remainder != 0) {
                int blanks = cols - remainder;
                for (int i = 0; i < blanks; i++) {
                    JPanel blank = new JPanel();
                    blank.setBackground(PANEL);
                    blank.setOpaque(true);
                    movieGrid.add(blank);
                }
            }
        }

        movieGrid.revalidate();
        movieGrid.repaint();
    }

    private JPanel createCard(Movie m) {
        JPanel card = new JPanel(new BorderLayout(0, 0)) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                // left accent border
                g2.setColor(new Color(80, 130, 255));
                g2.fillRoundRect(0, 0, 4, getHeight(), 4, 4);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBackground(CARD_BG);
        card.setPreferredSize(new Dimension(CARD_W, CARD_H));

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { card.setBackground(CARD_HOVER); card.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { card.setBackground(CARD_BG);   card.repaint(); }
        });

        // ── POSTER ───────────────────────────────────────────────
        JLabel poster = new JLabel();
        poster.setHorizontalAlignment(SwingConstants.CENTER);
        poster.setPreferredSize(new Dimension(CARD_W, POSTER_H));
        poster.setMinimumSize(new Dimension(CARD_W, POSTER_H));
        poster.setBackground(new Color(10, 14, 38));
        poster.setOpaque(true);

        ImageIcon icon = loadImage(m.getImagePath());
        if (icon != null) {
            Image img = icon.getImage().getScaledInstance(CARD_W, POSTER_H, Image.SCALE_SMOOTH);
            poster.setIcon(new ImageIcon(img));
        } else {
            poster.setText("No Image");
            poster.setForeground(new Color(60, 80, 140));
            poster.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        }

        poster.setCursor(new Cursor(Cursor.HAND_CURSOR));
        poster.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new MovieDetailsDialog(MainFrame.this, m).setVisible(true);
            }
        });

        // ── INFO ─────────────────────────────────────────────────
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(new EmptyBorder(8, 12, 4, 8));

        JLabel titleLabel = new JLabel(m.getTitle());
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        StringBuilder starsStr = new StringBuilder();
        for (int i = 0; i < m.getRating(); i++) starsStr.append("\u2605");
        for (int i = m.getRating(); i < 5; i++) starsStr.append("\u2606");
        JLabel starsLabel = new JLabel(starsStr.toString());
        starsLabel.setForeground(GOLD);
        starsLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        starsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        String reviewText = m.getReview() != null && !m.getReview().isEmpty()
                ? m.getReview() : "No review yet.";
        if (reviewText.length() > 50) reviewText = reviewText.substring(0, 50) + "...";
        JLabel reviewLabel = new JLabel("<html><i>" + reviewText + "</i></html>");
        reviewLabel.setForeground(new Color(140, 155, 200));
        reviewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        reviewLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        info.add(titleLabel);
        info.add(Box.createVerticalStrut(3));
        info.add(starsLabel);
        info.add(Box.createVerticalStrut(3));
        info.add(reviewLabel);

        // ── ACTIONS ──────────────────────────────────────────────
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 4));
        actions.setOpaque(false);

        JButton editBtn   = actionButton("Edit",   new Color(50, 100, 200));
        JButton deleteBtn = actionButton("Delete", new Color(200, 50, 50));

        editBtn.addActionListener(e -> showEditDialog(m));
        deleteBtn.addActionListener(e -> {
            int c = JOptionPane.showConfirmDialog(this,
                    "Delete \"" + m.getTitle() + "\"?", "Confirm Delete",
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (c == JOptionPane.YES_OPTION) {
                MovieDAO.deleteMovie(m.getId());
                loadMovies();
            }
        });

        actions.add(editBtn);
        actions.add(deleteBtn);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(info,    BorderLayout.CENTER);
        bottom.add(actions, BorderLayout.SOUTH);

        card.add(poster, BorderLayout.NORTH);
        card.add(bottom, BorderLayout.CENTER);

        return card;
    }

    private JButton actionButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(70, 26));
        return btn;
    }

    private ImageIcon loadImage(String path) {
        try {
            if (path == null || path.isEmpty()) return null;
            File file = new File(path);
            if (file.exists()) return new ImageIcon(path);
        } catch (Exception ignored) {}
        return null;
    }

    private void showAddDialog()         { buildMovieDialog(null).setVisible(true); }
    private void showEditDialog(Movie m) { buildMovieDialog(m).setVisible(true); }

    private JDialog buildMovieDialog(Movie m) {
        boolean isEdit = m != null;
        JDialog dialog = new JDialog(this, isEdit ? "Edit Movie" : "Add Movie", true);
        dialog.setSize(540, 580);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        dialog.getContentPane().setBackground(DIALOG_BG);
        dialog.setResizable(false);

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(8, 12, 35));
        header.setBorder(new EmptyBorder(14, 20, 14, 20));

        JLabel headerTitle = new JLabel(isEdit ? "Edit Movie" : "Add Movie");
        headerTitle.setForeground(new Color(140, 200, 255));
        headerTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        header.add(headerTitle, BorderLayout.WEST);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DIALOG_BG);
        body.setBorder(new EmptyBorder(20, 24, 16, 24));

        final String[] selectedImagePath = {isEdit && m.getImagePath() != null ? m.getImagePath() : ""};

        JLabel posterPreview = new JLabel();
        posterPreview.setPreferredSize(new Dimension(90, 125));
        posterPreview.setMinimumSize(new Dimension(90, 125));
        posterPreview.setMaximumSize(new Dimension(90, 125));
        posterPreview.setHorizontalAlignment(SwingConstants.CENTER);
        posterPreview.setVerticalAlignment(SwingConstants.CENTER);
        posterPreview.setBackground(INPUT_BG);
        posterPreview.setOpaque(true);
        posterPreview.setBorder(BorderFactory.createLineBorder(BORDER_DIM, 1));

        if (!selectedImagePath[0].isEmpty()) {
            ImageIcon existing = loadImage(selectedImagePath[0]);
            if (existing != null) {
                Image img = existing.getImage().getScaledInstance(90, 125, Image.SCALE_SMOOTH);
                posterPreview.setIcon(new ImageIcon(img));
            } else { setPosterPlaceholder(posterPreview); }
        } else { setPosterPlaceholder(posterPreview); }

        JTextField imagePathField = styledField(selectedImagePath[0]);
        imagePathField.setEditable(false);
        imagePathField.setForeground(new Color(120, 145, 200));

        JButton browseBtn = new JButton("Browse...");
        browseBtn.setBackground(new Color(30, 50, 120));
        browseBtn.setForeground(new Color(160, 200, 255));
        browseBtn.setFocusPainted(false);
        browseBtn.setBorderPainted(false);
        browseBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        browseBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        browseBtn.setPreferredSize(new Dimension(95, 34));
        browseBtn.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Select Movie Poster");
            fc.setFileFilter(new FileNameExtensionFilter("Image files", "jpg", "jpeg", "png", "gif"));
            fc.setAcceptAllFileFilterUsed(false);
            if (fc.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                File chosen = fc.getSelectedFile();
                File imagesDir = new File("images");
                if (!imagesDir.exists()) imagesDir.mkdirs();
                File dest = new File(imagesDir, chosen.getName());
                try {
                    Files.copy(chosen.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    selectedImagePath[0] = dest.getPath();
                    imagePathField.setText(dest.getPath());
                    Image img = new ImageIcon(dest.getPath()).getImage()
                            .getScaledInstance(90, 125, Image.SCALE_SMOOTH);
                    posterPreview.setIcon(new ImageIcon(img));
                    posterPreview.setText("");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(dialog, "Failed to copy image: " + ex.getMessage());
                }
            }
        });

        JTextField titleField = styledField(isEdit ? m.getTitle() : "");
        JComboBox<String> genreBox = new JComboBox<>(new String[]{
                "Action","Drama","Comedy","Horror","Sci-Fi","Romance","Thriller","Animation","Documentary"
        });
        genreBox.setBackground(INPUT_BG);
        genreBox.setForeground(Color.WHITE);
        genreBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        genreBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        genreBox.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (isEdit && m.getGenre() != null) genreBox.setSelectedItem(m.getGenre());

        JTextField yearField = styledField(isEdit ? String.valueOf(m.getYear()) : "");

        JPanel genreYearRow = new JPanel(new GridLayout(1, 2, 8, 0));
        genreYearRow.setBackground(DIALOG_BG);
        genreYearRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        genreYearRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        genreYearRow.add(genreBox);
        genreYearRow.add(yearField);

        JPanel imgRow = new JPanel(new BorderLayout(6, 0));
        imgRow.setBackground(DIALOG_BG);
        imgRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        imgRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        imgRow.add(imagePathField, BorderLayout.CENTER);
        imgRow.add(browseBtn,      BorderLayout.EAST);

        JPanel rightFields = new JPanel();
        rightFields.setLayout(new BoxLayout(rightFields, BoxLayout.Y_AXIS));
        rightFields.setBackground(DIALOG_BG);
        rightFields.add(styledLabel("Title"));
        rightFields.add(Box.createRigidArea(new Dimension(0, 4)));
        rightFields.add(titleField);
        rightFields.add(Box.createRigidArea(new Dimension(0, 10)));
        rightFields.add(styledLabel("Genre & Year"));
        rightFields.add(Box.createRigidArea(new Dimension(0, 4)));
        rightFields.add(genreYearRow);
        rightFields.add(Box.createRigidArea(new Dimension(0, 10)));
        rightFields.add(styledLabel("Image"));
        rightFields.add(Box.createRigidArea(new Dimension(0, 4)));
        rightFields.add(imgRow);

        JPanel topRow = new JPanel(new BorderLayout(14, 0));
        topRow.setBackground(DIALOG_BG);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 130));
        topRow.add(posterPreview, BorderLayout.WEST);
        topRow.add(rightFields,   BorderLayout.CENTER);

        StarRating starRating = new StarRating();
        starRating.setAlignmentX(Component.LEFT_ALIGNMENT);
        if (isEdit) starRating.setRating(m.getRating());

        JTextArea reviewArea = new JTextArea(isEdit ? m.getReview() : "", 4, 20);
        reviewArea.setLineWrap(true);
        reviewArea.setWrapStyleWord(true);
        reviewArea.setBackground(INPUT_BG);
        reviewArea.setForeground(Color.WHITE);
        reviewArea.setCaretColor(Color.WHITE);
        reviewArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reviewArea.setBorder(new EmptyBorder(8, 10, 8, 10));

        JScrollPane reviewScroll = new JScrollPane(reviewArea);
        reviewScroll.setBorder(BorderFactory.createLineBorder(BORDER_DIM, 1));
        reviewScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        body.add(topRow);
        body.add(Box.createRigidArea(new Dimension(0, 16)));
        body.add(styledLabel("Rating"));
        body.add(Box.createRigidArea(new Dimension(0, 5)));
        body.add(starRating);
        body.add(Box.createRigidArea(new Dimension(0, 16)));
        body.add(styledLabel("Review"));
        body.add(Box.createRigidArea(new Dimension(0, 5)));
        body.add(reviewScroll);

        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 12));
        footer.setBackground(new Color(8, 12, 35));
        footer.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(25, 35, 75)));

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.setBackground(new Color(25, 32, 70));
        cancelBtn.setForeground(new Color(140, 160, 220));
        cancelBtn.setFocusPainted(false);
        cancelBtn.setBorderPainted(false);
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelBtn.addActionListener(e -> dialog.dispose());

        JButton saveBtn = new JButton(isEdit ? "Save Changes" : "Add Movie");
        saveBtn.setBackground(new Color(30, 90, 210));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.setBorderPainted(false);
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        saveBtn.addActionListener(e -> {
            String t        = titleField.getText().trim();
            String g        = genreBox.getSelectedItem().toString();
            String yearText = yearField.getText().trim();
            int    r        = starRating.getRating();
            String rev      = reviewArea.getText().trim();
            String img      = selectedImagePath[0];

            if (t.isEmpty()) { JOptionPane.showMessageDialog(dialog, "Please enter a title.", "Missing Field", JOptionPane.WARNING_MESSAGE); return; }
            if (yearText.isEmpty()) { JOptionPane.showMessageDialog(dialog, "Please enter a year.", "Missing Field", JOptionPane.WARNING_MESSAGE); return; }
            if (r == 0) { JOptionPane.showMessageDialog(dialog, "Please select a star rating.", "Missing Field", JOptionPane.WARNING_MESSAGE); return; }

            try {
                int y = Integer.parseInt(yearText);
                if (isEdit) MovieDAO.updateMovie(m.getId(), t, g, y, r, rev, img);
                else        MovieDAO.addMovie(t, g, y, r, rev, img, userEmail);
                dialog.dispose();
                loadMovies();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Year must be a number.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
            }
        });

        footer.add(cancelBtn);
        footer.add(saveBtn);

        dialog.add(header, BorderLayout.NORTH);
        dialog.add(body,   BorderLayout.CENTER);
        dialog.add(footer, BorderLayout.SOUTH);

        return dialog;
    }

    private void setPosterPlaceholder(JLabel label) {
        label.setIcon(null);
        label.setText("<html><center><font color='#3a4a80' size='2'>no<br>image</font></center></html>");
    }

    private JTextField styledField(String value) {
        JTextField field = new JTextField(value);
        field.setBackground(INPUT_BG);
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.WHITE);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_DIM, 1),
                new EmptyBorder(6, 10, 6, 10)
        ));
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 34));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        return field;
    }

    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(LABEL_COLOR);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}