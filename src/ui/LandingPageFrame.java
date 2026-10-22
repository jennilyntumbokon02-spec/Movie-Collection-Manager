package ui;

import models.Movie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class LandingPageFrame extends JFrame {

    private static final Color BG    = new Color(5, 8, 24);
    private static final Color BLUE  = new Color(100, 170, 255);
    private static final Color ACCENT = new Color(0, 200, 120);

    private static String loggedInEmail = null;

    public static void setLoggedInUser(String email) { loggedInEmail = email; }
    public static void clearLoggedInUser()           { loggedInEmail = null; }
    public static String getLoggedInEmail()          { return loggedInEmail; }

    public LandingPageFrame() {
        setTitle("Movie Collection Manager");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel bg = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(5, 5, 20),
                        getWidth(), getHeight(), new Color(10, 18, 50)
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        bg.setLayout(new BorderLayout());

        // ── NAV ──────────────────────────────────────────────────
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(new Color(5, 8, 24));
        nav.setOpaque(true);
        nav.setBorder(new EmptyBorder(14, 40, 14, 40));

        JLabel logo = new JLabel("MATLYN");
        logo.setForeground(BLUE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel navLinks = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 0));
        navLinks.setOpaque(false);

        JButton home  = navButton("Home");
        JButton about = navButton("About Us");
        JButton login = navButton("Log In");

        navLinks.add(home);
        navLinks.add(about);
        navLinks.add(login);

        nav.add(logo,     BorderLayout.WEST);
        nav.add(navLinks, BorderLayout.EAST);

        // ── CENTER ───────────────────────────────────────────────
        JPanel center = new JPanel(new GridLayout(1, 2));
        center.setOpaque(false);

        // LEFT
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);
        left.setBorder(new EmptyBorder(80, 80, 60, 40));

        JLabel title1 = new JLabel("Movie Collection");
        title1.setForeground(BLUE);
        title1.setFont(new Font("Segoe UI", Font.BOLD, 52));
        title1.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel title2 = new JLabel("Manager");
        title2.setForeground(BLUE);
        title2.setFont(new Font("Segoe UI", Font.BOLD, 52));
        title2.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel tagline = new JLabel("Discover. Organize. Review.");
        tagline.setForeground(Color.WHITE);
        tagline.setFont(new Font("Segoe UI", Font.BOLD, 18));
        tagline.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel desc = new JLabel("<html>Your personal movie universe.<br>"
                + "Track your favorite films, manage reviews,<br>"
                + "and build your own cinematic collection.</html>");
        desc.setForeground(new Color(180, 190, 220));
        desc.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        desc.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton getStarted = new JButton("Get Started") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                super.paintComponent(g);
                g2.dispose();
            }
        };
        getStarted.setBackground(ACCENT);
        getStarted.setForeground(Color.WHITE);
        getStarted.setFocusPainted(false);
        getStarted.setBorderPainted(false);
        getStarted.setContentAreaFilled(false);
        getStarted.setOpaque(false);
        getStarted.setFont(new Font("Segoe UI", Font.BOLD, 15));
        getStarted.setPreferredSize(new Dimension(200, 46));
        getStarted.setMaximumSize(new Dimension(200, 46));
        getStarted.setAlignmentX(Component.LEFT_ALIGNMENT);
        getStarted.setCursor(new Cursor(Cursor.HAND_CURSOR));
        getStarted.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { getStarted.setBackground(new Color(0, 170, 100)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { getStarted.setBackground(ACCENT); }
        });
        getStarted.addActionListener(e -> {
            if (loggedInEmail != null) {
                new MainFrame(loggedInEmail);
            } else {
                new LoginFrame();
            }
            dispose();
        });

        left.add(title1);
        left.add(title2);
        left.add(Box.createRigidArea(new Dimension(0, 16)));
        left.add(tagline);
        left.add(Box.createRigidArea(new Dimension(0, 14)));
        left.add(desc);
        left.add(Box.createRigidArea(new Dimension(0, 30)));
        left.add(getStarted);

        // RIGHT — featured movies
        JPanel rightWrapper = new JPanel(new BorderLayout());
        rightWrapper.setOpaque(false);
        rightWrapper.setBorder(new EmptyBorder(50, 20, 20, 60));

        JLabel featuredLabel = new JLabel("  Featured Movies");
        featuredLabel.setForeground(BLUE);
        featuredLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
        featuredLabel.setBorder(new EmptyBorder(40, 0, 12, 0));

        JPanel grid = new JPanel(new GridLayout(2, 2, 12, 12));
        grid.setOpaque(false);

        grid.add(movieCard("Inception",    "inception.jpg",    makeMovie("Inception")));
        grid.add(movieCard("Interstellar", "interstellar.jpg", makeMovie("Interstellar")));
        grid.add(movieCard("Parasite",     "parasite.jpg",     makeMovie("Parasite")));
        grid.add(movieCard("Avengers",     "avengers.jpg",     makeMovie("Avengers")));

        rightWrapper.add(featuredLabel, BorderLayout.NORTH);
        rightWrapper.add(grid,          BorderLayout.CENTER);

        center.add(left);
        center.add(rightWrapper);

        // ── FEATURES ROW ─────────────────────────────────────────
        JPanel features = new JPanel(new GridLayout(1, 3, 20, 0));
        features.setOpaque(false);
        features.setBorder(new EmptyBorder(10, 80, 36, 80));

        features.add(featureCard("Track Movies",   "Save and manage your collection"));
        features.add(featureCard("Write Reviews",  "Share your thoughts and ratings"));
        features.add(featureCard("Organize Lists", "Create your own movie lists"));

        bg.add(nav,      BorderLayout.NORTH);
        bg.add(center,   BorderLayout.CENTER);
        bg.add(features, BorderLayout.SOUTH);

        add(bg);

        home.addActionListener(e  -> { new LandingPageFrame(); dispose(); });
        about.addActionListener(e -> { new AboutUsFrame(); dispose(); });
        login.addActionListener(e -> { new LoginFrame(); dispose(); });

        setVisible(true);
    }

    // ── FIXED makeMovie() with real data ─────────────────────────
    private Movie makeMovie(String title) {
        switch (title) {
            case "Inception":
                return new Movie("Inception", "Sci-Fi", 2010, 5,
                        "A thief who steals corporate secrets through dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O.",
                        "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page, Tom Hardy",
                        "Netflix, HBO Max",
                        "inception.jpg");
            case "Interstellar":
                return new Movie("Interstellar", "Drama", 2014, 5,
                        "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival.",
                        "Matthew McConaughey, Anne Hathaway, Jessica Chastain, Michael Caine",
                        "Paramount+, Apple TV",
                        "interstellar.jpg");
            case "Parasite":
                return new Movie("Parasite", "Thriller", 2019, 5,
                        "Greed and class discrimination threaten the newly formed symbiotic relationship between the wealthy Park family and the destitute Kim clan.",
                        "Song Kang-ho, Lee Sun-kyun, Cho Yeo-jeong, Choi Woo-shik",
                        "HBO Max, Hulu",
                        "parasite.jpg");
            case "Avengers":
                return new Movie("Avengers", "Action", 2019, 5,
                        "Earth's mightiest heroes must come together and learn to fight as a team to stop the mischievous Loki and his alien army from enslaving humanity.",
                        "Robert Downey Jr., Chris Evans, Scarlett Johansson, Chris Hemsworth",
                        "Disney+",
                        "avengers.jpg");
            default:
                return new Movie(title, "N/A", 0, 0, "", "", "", "");
        }
    }

    private JPanel movieCard(String title, String imagePath, Movie movie) {
        JPanel card = new JPanel(new BorderLayout()) {
            Image img = Toolkit.getDefaultToolkit().getImage(imagePath);
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (img != null) g2.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                g2.setColor(new Color(0, 0, 0, 120));
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        card.setLayout(new BorderLayout());
        card.setPreferredSize(new Dimension(240, 155));
        card.setBorder(BorderFactory.createLineBorder(new Color(50, 80, 160), 1));
        card.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        labelPanel.setBackground(new Color(0, 0, 0, 160));
        labelPanel.setOpaque(true);

        JLabel lbl = new JLabel(title);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelPanel.add(lbl);

        card.add(labelPanel, BorderLayout.SOUTH);

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(120, 180, 255), 2));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBorder(BorderFactory.createLineBorder(new Color(50, 80, 160), 1));
            }
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new MovieDetailsFrame(movie, loggedInEmail);
            }
        });

        return card;
    }

    private JPanel featureCard(String title, String desc) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(14, 18, 45));
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 22, 20, 22));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel("<html>" + desc + "</html>");
        descLbl.setForeground(new Color(150, 165, 210));
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(descLbl);

        return card;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.WHITE);
        btn.setOpaque(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setForeground(BLUE); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setForeground(Color.WHITE); }
        });
        return btn;
    }
}