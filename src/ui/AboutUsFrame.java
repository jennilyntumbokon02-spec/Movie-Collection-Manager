package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class AboutUsFrame extends JFrame {

    private static final Color BG        = new Color(5, 8, 24);
    private static final Color CARD_BG   = new Color(14, 18, 45);
    private static final Color CARD_BG2  = new Color(10, 14, 38);
    private static final Color ACCENT    = new Color(100, 170, 255);
    private static final Color ACCENT2   = new Color(0, 200, 120);
    private static final Color BORDER    = new Color(40, 55, 120);
    private static final Color TEXT_DIM  = new Color(160, 175, 220);
    private static final Color TEXT_MID  = new Color(120, 140, 200);

    public AboutUsFrame() {
        setTitle("About Us");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout()) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(5, 5, 20),
                        getWidth(), getHeight(), new Color(10, 18, 50));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        root.setOpaque(false);

        // ── NAV ──────────────────────────────────────────────────
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(new Color(5, 8, 24));
        nav.setOpaque(true);
        nav.setBorder(BorderFactory.createCompoundBorder(
                new EmptyBorder(16, 50, 16, 50),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(30, 45, 100))
        ));

        JPanel navLeft = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        navLeft.setOpaque(false);

        JLabel logo = new JLabel("MATLYN");
        logo.setForeground(ACCENT);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel subLogo = new JLabel("  /  About Us");
        subLogo.setForeground(new Color(60, 85, 150));
        subLogo.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        navLeft.add(logo);
        navLeft.add(subLogo);

        JButton backBtn = new JButton("← Back");
        backBtn.setBackground(new Color(20, 26, 60));
        backBtn.setForeground(new Color(140, 180, 255));
        backBtn.setFocusPainted(false);
        backBtn.setBorderPainted(false);
        backBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backBtn.setPreferredSize(new Dimension(100, 32));
        backBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { backBtn.setBackground(new Color(30, 40, 90)); }
            public void mouseExited(java.awt.event.MouseEvent e)  { backBtn.setBackground(new Color(20, 26, 60)); }
        });
        backBtn.addActionListener(e -> { new LandingPageFrame(); dispose(); });

        nav.add(navLeft,  BorderLayout.WEST);
        nav.add(backBtn,  BorderLayout.EAST);

        // ── SCROLL CONTENT ───────────────────────────────────────
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        content.setBorder(new EmptyBorder(50, 100, 60, 100));

        // ── HERO SECTION ─────────────────────────────────────────
        JPanel hero = new JPanel(new BorderLayout());
        hero.setOpaque(false);
        hero.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        hero.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel heroText = new JPanel();
        heroText.setLayout(new BoxLayout(heroText, BoxLayout.Y_AXIS));
        heroText.setOpaque(false);

        JLabel heroTitle = new JLabel("About the Project");
        heroTitle.setForeground(ACCENT);
        heroTitle.setFont(new Font("Segoe UI", Font.BOLD, 38));
        heroTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heroSub = new JLabel("A Java-based Movie Collection Manager built for CC106 — Application Development & Emerging Technologies");
        heroSub.setForeground(TEXT_DIM);
        heroSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        heroSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heroInst = new JLabel("University of Caloocan City  ·  Computer Studies Department");
        heroInst.setForeground(TEXT_MID);
        heroInst.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        heroInst.setAlignmentX(Component.LEFT_ALIGNMENT);

        heroText.add(heroTitle);
        heroText.add(Box.createRigidArea(new Dimension(0, 8)));
        heroText.add(heroSub);
        heroText.add(Box.createRigidArea(new Dimension(0, 6)));
        heroText.add(heroInst);

        hero.add(heroText, BorderLayout.WEST);

        content.add(hero);
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // ── DIVIDER ───────────────────────────────────────────────
        content.add(styledDivider());
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // ── TOP CARDS ROW ─────────────────────────────────────────
        JPanel topRow = new JPanel(new GridLayout(1, 2, 28, 0));
        topRow.setOpaque(false);
        topRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        topRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 220));

        topRow.add(infoCard(
                "Welcome to Movie Collection Manager",
                "Your personal cinematic universe — manage, track, and review your favorite films all in one place. " +
                        "Built with Java Swing and SQLite for a fully offline, secure experience.",
                ACCENT, false
        ));

        topRow.add(infoCard(
                "Our Mission",
                "We aim to create a simple yet powerful experience for managing your personal movie collection. " +
                        "From ratings to reviews and organized watchlists, MATLYN puts you in control of your film journey.",
                ACCENT2, true
        ));

        content.add(topRow);
        content.add(Box.createRigidArea(new Dimension(0, 36)));

        // ── FEATURES ROW ─────────────────────────────────────────
        JLabel featuresHeading = sectionHeading("Key Features");
        content.add(featuresHeading);
        content.add(Box.createRigidArea(new Dimension(0, 18)));

        JPanel featuresRow = new JPanel(new GridLayout(1, 4, 20, 0));
        featuresRow.setOpaque(false);
        featuresRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        featuresRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));

        featuresRow.add(featureCard("\uD83C\uDFAC", "Track Movies",   "Save and organize your entire watched film collection."));
        featuresRow.add(featureCard("\u2B50",        "Rate & Review",  "Give star ratings and write personal reviews for each film."));
        featuresRow.add(featureCard("\uD83D\uDCCB",  "Organize Lists", "Create custom movie lists and watchlists to plan your viewing."));
        featuresRow.add(featureCard("\uD83D\uDD0D",  "Search & Filter","Find any movie by title, genre, or release year instantly."));

        content.add(featuresRow);
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // ── DIVIDER ───────────────────────────────────────────────
        content.add(styledDivider());
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // ── TEAM SECTION ─────────────────────────────────────────
        JLabel teamHeading = sectionHeading("Meet the Developers");
        content.add(teamHeading);
        content.add(Box.createRigidArea(new Dimension(0, 18)));

        JPanel teamRow = new JPanel(new GridLayout(1, 2, 28, 0));
        teamRow.setOpaque(false);
        teamRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        teamRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        teamRow.add(developerCard("Matthew James Q. Calunod", "matthewjamequimson@gmail.com", "09383883614", "Lead Developer"));
        teamRow.add(developerCard("Jennilyn Tumbokon",        "jennilyn.tumbokon02@gmail.com", "",           "Co-Developer"));

        content.add(teamRow);
        content.add(Box.createRigidArea(new Dimension(0, 40)));

        // ── FOOTER ───────────────────────────────────────────────
        JPanel footer = new JPanel();
        footer.setOpaque(false);
        footer.setAlignmentX(Component.LEFT_ALIGNMENT);
        footer.setLayout(new BoxLayout(footer, BoxLayout.Y_AXIS));

        JSeparator footerLine = new JSeparator();
        footerLine.setForeground(BORDER);
        footerLine.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        footerLine.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel footerText = new JLabel("© 2025 MATLYN — Movie Collection Manager  ·  University of Caloocan City");
        footerText.setForeground(new Color(50, 65, 120));
        footerText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        footerText.setAlignmentX(Component.CENTER_ALIGNMENT);

        footer.add(footerLine);
        footer.add(Box.createRigidArea(new Dimension(0, 14)));
        footer.add(footerText);

        content.add(footer);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        root.add(nav,    BorderLayout.NORTH);
        root.add(scroll, BorderLayout.CENTER);

        add(root);
        setVisible(true);
    }

    // ── HELPERS ──────────────────────────────────────────────────

    private JPanel infoCard(String title, String body, Color accentColor, boolean highlight) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(highlight ? new Color(10, 30, 20) : CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(highlight ? new Color(0, 200, 120, 60) : new Color(40, 55, 120, 80));
                g2.setStroke(new BasicStroke(1.2f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(28, 30, 28, 30));

        JLabel titleLbl = new JLabel("<html>" + title + "</html>");
        titleLbl.setForeground(accentColor);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel line = new JLabel(" ");
        line.setOpaque(true);
        line.setBackground(accentColor);
        line.setMaximumSize(new Dimension(40, 2));
        line.setPreferredSize(new Dimension(40, 2));
        line.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel bodyLbl = new JLabel("<html>" + body + "</html>");
        bodyLbl.setForeground(TEXT_DIM);
        bodyLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bodyLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(line);
        card.add(Box.createRigidArea(new Dimension(0, 14)));
        card.add(bodyLbl);
        return card;
    }

    private JPanel featureCard(String icon, String title, String desc) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG2);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(22, 22, 22, 22));

        JLabel iconLbl = new JLabel(icon);
        iconLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 26));
        iconLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titleLbl = new JLabel(title);
        titleLbl.setForeground(Color.WHITE);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descLbl = new JLabel("<html>" + desc + "</html>");
        descLbl.setForeground(TEXT_MID);
        descLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        descLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(iconLbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(titleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 6)));
        card.add(descLbl);
        return card;
    }

    private JPanel developerCard(String name, String email, String phone, String role) {
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_BG);
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 14, 14));
                g2.setColor(BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 14, 14));
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(26, 30, 26, 30));

        JLabel avatar = new JLabel("\uD83D\uDC64");
        avatar.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        avatar.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel roleLbl = new JLabel(role.toUpperCase());
        roleLbl.setForeground(ACCENT2);
        roleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        roleLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel nameLbl = new JLabel(name);
        nameLbl.setForeground(Color.WHITE);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 16));
        nameLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel emailLbl = new JLabel("\u2709  " + email);
        emailLbl.setForeground(TEXT_DIM);
        emailLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        emailLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(avatar);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(roleLbl);
        card.add(Box.createRigidArea(new Dimension(0, 4)));
        card.add(nameLbl);
        card.add(Box.createRigidArea(new Dimension(0, 10)));
        card.add(emailLbl);

        if (phone != null && !phone.isEmpty()) {
            JLabel phoneLbl = new JLabel("\uD83D\uDCDE  " + phone);
            phoneLbl.setForeground(TEXT_DIM);
            phoneLbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            phoneLbl.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(Box.createRigidArea(new Dimension(0, 4)));
            card.add(phoneLbl);
        }

        return card;
    }

    private JLabel sectionHeading(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setForeground(Color.WHITE);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JSeparator styledDivider() {
        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);
        return sep;
    }
}