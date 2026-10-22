package ui;

import dao.ReviewDAO;
import models.Movie;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MovieDetailsFrame extends JFrame {

    private JPanel reviewListPanel;
    private String movieTitle;

    public MovieDetailsFrame(Movie movie, String userEmail) {
        setTitle(movie.getTitle());
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(900, 600));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(true);

        movieTitle = movie.getTitle();

        JPanel main = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(5, 5, 20),
                        getWidth(), getHeight(), new Color(20, 30, 70)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 15));

        JLabel image = new JLabel();
        image.setPreferredSize(new Dimension(260, 390));
        image.setHorizontalAlignment(SwingConstants.CENTER);
        image.setBorder(BorderFactory.createLineBorder(new Color(120, 180, 255), 2));

        if (movie.getImagePath() != null && !movie.getImagePath().isEmpty()) {
            try {
                ImageIcon icon = new ImageIcon(movie.getImagePath());
                Image scaled = icon.getImage().getScaledInstance(260, 390, Image.SCALE_SMOOTH);
                image.setIcon(new ImageIcon(scaled));
            } catch (Exception ignored) {
                image.setText("No Image");
                image.setForeground(new Color(60, 80, 140));
            }
        } else {
            image.setText("No Image");
            image.setForeground(new Color(60, 80, 140));
        }

        leftPanel.add(image, BorderLayout.NORTH);

        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setOpaque(false);
        info.setBorder(BorderFactory.createEmptyBorder(30, 15, 30, 30));

        JLabel title = new JLabel(movie.getTitle());
        title.setForeground(new Color(140, 200, 255));
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(120, 180, 255));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel metaRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        metaRow.setOpaque(false);
        metaRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        metaRow.add(pill(movie.getGenre() != null && !movie.getGenre().isEmpty() ? movie.getGenre() : "N/A", new Color(40, 60, 120)));
        metaRow.add(pill(movie.getYear() > 0 ? String.valueOf(movie.getYear()) : "N/A", new Color(40, 80, 60)));

        JLabel ratingLabel = new JLabel("Rating:");
        ratingLabel.setForeground(Color.LIGHT_GRAY);
        ratingLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ratingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel stars = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        stars.setOpaque(false);
        stars.setAlignmentX(Component.LEFT_ALIGNMENT);
        for (int i = 1; i <= 5; i++) {
            JLabel star = new JLabel(i <= movie.getRating() ? "\u2605" : "\u2606");
            star.setForeground(i <= movie.getRating() ? new Color(255, 200, 50) : Color.GRAY);
            star.setFont(new Font("Dialog", Font.PLAIN, 22));
            stars.add(star);
        }

        // CAST
        JLabel castLabel = sectionLabel("Cast");
        String castText = (movie.getCast() != null && !movie.getCast().isEmpty())
                ? movie.getCast() : "No cast info available.";
        JLabel cast = new JLabel("<html><span style='color:#cccccc'>" + castText + "</span></html>");
        cast.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cast.setAlignmentX(Component.LEFT_ALIGNMENT);

        // SYNOPSIS
        JLabel synopsisLabel = sectionLabel("Synopsis");
        String synopsisText = (movie.getSynopsis() != null && !movie.getSynopsis().isEmpty())
                ? movie.getSynopsis() : "No synopsis available.";
        JTextArea synopsis = new JTextArea(synopsisText);
        synopsis.setWrapStyleWord(true);
        synopsis.setLineWrap(true);
        synopsis.setEditable(false);
        synopsis.setOpaque(false);
        synopsis.setForeground(Color.LIGHT_GRAY);
        synopsis.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        synopsis.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        synopsis.setAlignmentX(Component.LEFT_ALIGNMENT);

        // WHERE TO WATCH
        JLabel watchLabel = sectionLabel("Where to Watch");
        String watchText = (movie.getWhereToWatch() != null && !movie.getWhereToWatch().isEmpty())
                ? movie.getWhereToWatch() : "Not specified.";
        JLabel watch = new JLabel("\u25ba  " + watchText);
        watch.setForeground(new Color(80, 200, 255));
        watch.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        watch.setAlignmentX(Component.LEFT_ALIGNMENT);

        JSeparator sep2 = new JSeparator();
        sep2.setForeground(new Color(120, 180, 255));
        sep2.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel commentSectionLabel = sectionLabel("Comments");

        reviewListPanel = new JPanel();
        reviewListPanel.setLayout(new BoxLayout(reviewListPanel, BoxLayout.Y_AXIS));
        reviewListPanel.setOpaque(false);
        reviewListPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        loadReviews();

        JScrollPane reviewScroll = new JScrollPane(reviewListPanel);
        reviewScroll.setOpaque(false);
        reviewScroll.getViewport().setOpaque(false);
        reviewScroll.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 80)));
        reviewScroll.setPreferredSize(new Dimension(0, 120));
        reviewScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        reviewScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
        reviewScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        info.add(title);
        info.add(Box.createRigidArea(new Dimension(0, 8)));
        info.add(sep);
        info.add(Box.createRigidArea(new Dimension(0, 10)));
        info.add(metaRow);
        info.add(Box.createRigidArea(new Dimension(0, 10)));
        info.add(ratingLabel);
        info.add(stars);
        info.add(Box.createRigidArea(new Dimension(0, 12)));
        info.add(castLabel);
        info.add(cast);
        info.add(Box.createRigidArea(new Dimension(0, 12)));
        info.add(synopsisLabel);
        info.add(synopsis);
        info.add(Box.createRigidArea(new Dimension(0, 12)));
        info.add(watchLabel);
        info.add(watch);
        info.add(Box.createRigidArea(new Dimension(0, 20)));
        info.add(sep2);
        info.add(Box.createRigidArea(new Dimension(0, 10)));
        info.add(commentSectionLabel);
        info.add(Box.createRigidArea(new Dimension(0, 8)));
        info.add(reviewScroll);
        info.add(Box.createRigidArea(new Dimension(0, 10)));

        if (userEmail != null) {
            JLabel yourRatingLabel = sectionLabel("Your Rating");
            yourRatingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            int[] selectedRating = {0};
            JLabel[] starButtons = new JLabel[5];
            JPanel starInput = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
            starInput.setOpaque(false);
            starInput.setAlignmentX(Component.LEFT_ALIGNMENT);

            for (int i = 0; i < 5; i++) {
                final int starIndex = i + 1;
                starButtons[i] = new JLabel("\u2606");
                starButtons[i].setForeground(Color.GRAY);
                starButtons[i].setFont(new Font("Dialog", Font.PLAIN, 26));
                starButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
                starButtons[i].addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        selectedRating[0] = starIndex;
                        for (int j = 0; j < 5; j++) {
                            starButtons[j].setText(j < starIndex ? "\u2605" : "\u2606");
                            starButtons[j].setForeground(j < starIndex ? new Color(255, 200, 50) : Color.GRAY);
                        }
                    }
                    public void mouseEntered(java.awt.event.MouseEvent e) {
                        for (int j = 0; j < 5; j++) {
                            starButtons[j].setText(j < starIndex ? "\u2605" : "\u2606");
                            starButtons[j].setForeground(j < starIndex ? new Color(255, 200, 50) : Color.GRAY);
                        }
                    }
                    public void mouseExited(java.awt.event.MouseEvent e) {
                        for (int j = 0; j < 5; j++) {
                            starButtons[j].setText(j < selectedRating[0] ? "\u2605" : "\u2606");
                            starButtons[j].setForeground(j < selectedRating[0] ? new Color(255, 200, 50) : Color.GRAY);
                        }
                    }
                });
                starInput.add(starButtons[i]);
            }

            JLabel identityLabel = sectionLabel("Post As");
            identityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            String[] identityOptions = {"Use my email (" + userEmail + ")", "Anonymous"};
            JComboBox<String> identityDropdown = new JComboBox<>(identityOptions);
            identityDropdown.setBackground(new Color(20, 20, 50));
            identityDropdown.setForeground(Color.WHITE);
            identityDropdown.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            identityDropdown.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            identityDropdown.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextArea commentInput = new JTextArea(3, 20);
            commentInput.setLineWrap(true);
            commentInput.setWrapStyleWord(true);
            commentInput.setBackground(new Color(20, 20, 50));
            commentInput.setForeground(Color.WHITE);
            commentInput.setCaretColor(Color.WHITE);
            commentInput.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            commentInput.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JScrollPane commentScroll = new JScrollPane(commentInput);
            commentScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
            commentScroll.setAlignmentX(Component.LEFT_ALIGNMENT);
            commentScroll.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 120)));

            JButton submitBtn = new JButton("Post Comment");
            submitBtn.setBackground(new Color(50, 50, 120));
            submitBtn.setForeground(Color.WHITE);
            submitBtn.setFocusPainted(false);
            submitBtn.setBorderPainted(false);
            submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
            submitBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            submitBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            submitBtn.setMaximumSize(new Dimension(150, 35));
            submitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent e) { submitBtn.setBackground(new Color(80, 80, 180)); }
                public void mouseExited(java.awt.event.MouseEvent e)  { submitBtn.setBackground(new Color(50, 50, 120)); }
            });

            submitBtn.addActionListener(e -> {
                String comment = commentInput.getText().trim();
                if (selectedRating[0] == 0) {
                    JOptionPane.showMessageDialog(this, "Please select a star rating.");
                    return;
                }
                if (comment.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please write a comment.");
                    return;
                }
                int selectedIndex = identityDropdown.getSelectedIndex();
                String poster = selectedIndex == 1 ? "Anonymous" : userEmail;
                boolean success = ReviewDAO.addReview(movieTitle, poster, selectedRating[0], comment);
                if (success) {
                    commentInput.setText("");
                    selectedRating[0] = 0;
                    for (JLabel s : starButtons) {
                        s.setText("\u2606");
                        s.setForeground(Color.GRAY);
                    }
                    loadReviews();
                    reviewListPanel.revalidate();
                    reviewListPanel.repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to post comment.");
                }
            });

            info.add(yourRatingLabel);
            info.add(Box.createRigidArea(new Dimension(0, 5)));
            info.add(starInput);
            info.add(Box.createRigidArea(new Dimension(0, 8)));
            info.add(identityLabel);
            info.add(Box.createRigidArea(new Dimension(0, 5)));
            info.add(identityDropdown);
            info.add(Box.createRigidArea(new Dimension(0, 8)));
            info.add(commentScroll);
            info.add(Box.createRigidArea(new Dimension(0, 8)));
            info.add(submitBtn);

        } else {
            JLabel loginMsg = new JLabel("Log in to leave a comment.");
            loginMsg.setForeground(new Color(180, 180, 180));
            loginMsg.setFont(new Font("Segoe UI", Font.ITALIC, 13));
            loginMsg.setAlignmentX(Component.LEFT_ALIGNMENT);
            info.add(loginMsg);
        }

        JButton close = new JButton("\u2715  Close");
        close.setBackground(new Color(50, 50, 120));
        close.setForeground(Color.WHITE);
        close.setFocusPainted(false);
        close.setBorderPainted(false);
        close.setFont(new Font("Segoe UI", Font.BOLD, 13));
        close.setCursor(new Cursor(Cursor.HAND_CURSOR));
        close.setAlignmentX(Component.LEFT_ALIGNMENT);
        close.setMaximumSize(new Dimension(120, 35));
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { close.setBackground(new Color(80, 80, 180)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { close.setBackground(new Color(50, 50, 120)); }
        });
        close.addActionListener(e -> dispose());

        info.add(Box.createRigidArea(new Dimension(0, 20)));
        info.add(close);

        JScrollPane scroll = new JScrollPane(info);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        main.add(leftPanel, BorderLayout.WEST);
        main.add(scroll, BorderLayout.CENTER);

        add(main);
        setVisible(true);
    }

    private void loadReviews() {
        reviewListPanel.removeAll();
        List<String> reviews = ReviewDAO.getReviews(movieTitle);
        if (reviews.isEmpty()) {
            JLabel none = new JLabel("No comments yet. Be the first!");
            none.setForeground(Color.GRAY);
            none.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            reviewListPanel.add(none);
        } else {
            for (String review : reviews) {
                JTextArea r = new JTextArea(review);
                r.setEditable(false);
                r.setOpaque(false);
                r.setForeground(Color.LIGHT_GRAY);
                r.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                r.setWrapStyleWord(true);
                r.setLineWrap(true);
                r.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
                r.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(40, 40, 80)));
                reviewListPanel.add(r);
                reviewListPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            }
        }
    }

    private JLabel pill(String text, Color bg) {
        JLabel label = new JLabel(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                super.paintComponent(g);
            }
        };
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        label.setOpaque(false);
        return label;
    }

    private JLabel sectionLabel(String text) {
        JLabel label = new JLabel(text.toUpperCase());
        label.setForeground(new Color(120, 180, 255));
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
}