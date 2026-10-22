package ui;

import dao.MovieDAO;
import dao.WatchlistDAO;
import dao.ListDAO;
import dao.UserDAO;
import models.Movie;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.time.LocalDate;
import java.util.List;

public class ProfileFrame extends JFrame {

    private static final Color BG       = new Color(5,  8,  24);
    private static final Color CARD_BG  = new Color(14, 18, 45);
    private static final Color INPUT_BG = new Color(20, 26, 60);
    private static final Color BORDER_C = new Color(40, 55, 120);
    private static final Color ACCENT   = new Color(0,  200, 120);
    private static final Color LABEL_C  = new Color(120, 145, 200);
    private static final Color WHITE    = Color.WHITE;

    private String userEmail;
    private JFrame parentFrame;

    private JLabel avatarLabel;
    private String avatarPath = null;

    private JLabel moviesStatLabel, watchlistStatLabel, listsStatLabel;

    public ProfileFrame(String userEmail) {
        this(userEmail, null);
    }

    public ProfileFrame(String userEmail, JFrame parentFrame) {
        this.userEmail   = userEmail;
        this.parentFrame = parentFrame;

        setTitle("My Profile");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        String[] info = UserDAO.getUserInfo(userEmail);
        avatarPath = UserDAO.getProfilePicture(userEmail);

        add(buildBanner(info[0], info[1]), BorderLayout.NORTH);
        add(buildCenter(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        setVisible(true);
    }

    // ── BANNER ────────────────────────────────────────────────────────────────
    private JPanel buildBanner(String firstName, String lastName) {
        JPanel banner = new JPanel(new BorderLayout());
        banner.setBackground(CARD_BG);
        banner.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_C),
                new EmptyBorder(30, 60, 0, 60)
        ));

        JPanel topRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        topRow.setBackground(CARD_BG);

        avatarLabel = buildAvatarLabel();
        avatarLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        avatarLabel.setToolTipText("Click to change photo");
        avatarLabel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) { chooseAvatar(); }
        });
        topRow.add(avatarLabel);

        JPanel nameBlock = new JPanel();
        nameBlock.setLayout(new BoxLayout(nameBlock, BoxLayout.Y_AXIS));
        nameBlock.setBackground(CARD_BG);
        nameBlock.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel nameLabel = new JLabel(firstName + " " + lastName);
        nameLabel.setForeground(WHITE);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));

        JLabel emailLabel = new JLabel(userEmail);
        emailLabel.setForeground(LABEL_C);
        emailLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JLabel hint = new JLabel("Click avatar to change photo");
        hint.setForeground(new Color(80, 110, 170));
        hint.setFont(new Font("Segoe UI", Font.ITALIC, 11));

        nameBlock.add(nameLabel);
        nameBlock.add(Box.createRigidArea(new Dimension(0, 4)));
        nameBlock.add(emailLabel);
        nameBlock.add(Box.createRigidArea(new Dimension(0, 6)));
        nameBlock.add(hint);

        topRow.add(nameBlock);
        banner.add(topRow, BorderLayout.NORTH);
        banner.add(buildStatsBar(), BorderLayout.SOUTH);
        return banner;
    }

    private JLabel buildAvatarLabel() {
        JLabel lbl = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int size = Math.min(getWidth(), getHeight());
                g2.setClip(new Ellipse2D.Float(0, 0, size, size));
                if (avatarPath != null) {
                    try { g2.drawImage(ImageIO.read(new File(avatarPath)), 0, 0, size, size, null); }
                    catch (Exception ex) { drawDefault(g2, size); }
                } else { drawDefault(g2, size); }
                g2.setClip(null);
                g2.setStroke(new BasicStroke(3f));
                g2.setColor(ACCENT);
                g2.draw(new Ellipse2D.Float(1, 1, size - 2, size - 2));
                g2.dispose();
            }
            private void drawDefault(Graphics2D g2, int size) {
                g2.setColor(INPUT_BG);
                g2.fillOval(0, 0, size, size);
                g2.setColor(LABEL_C);
                g2.setFont(new Font("Segoe UI", Font.BOLD, size / 3));
                FontMetrics fm = g2.getFontMetrics();
                String init = userEmail.substring(0, 1).toUpperCase();
                g2.drawString(init, (size - fm.stringWidth(init)) / 2, (size - fm.getHeight()) / 2 + fm.getAscent());
            }
        };
        lbl.setPreferredSize(new Dimension(100, 100));
        lbl.setMinimumSize(new Dimension(100, 100));
        lbl.setMaximumSize(new Dimension(100, 100));
        return lbl;
    }

    private JPanel buildStatsBar() {
        int movieCount     = MovieDAO.getMoviesByUser(userEmail).size();
        int watchlistCount = WatchlistDAO.getWatchlist(userEmail).size();
        int listsCount     = ListDAO.getListNames(userEmail).size();

        moviesStatLabel    = new JLabel();
        watchlistStatLabel = new JLabel();
        listsStatLabel     = new JLabel();

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        bar.setBackground(CARD_BG);
        bar.setBorder(new EmptyBorder(20, 0, 0, 0));
        bar.add(wrapStat(moviesStatLabel,    "FILMS",     movieCount));
        bar.add(wrapStat(watchlistStatLabel, "WATCHLIST", watchlistCount));
        bar.add(wrapStat(listsStatLabel,     "LISTS",     listsCount));
        return bar;
    }

    private JPanel wrapStat(JLabel numLabel, String labelText, int count) {
        numLabel.setText(String.valueOf(count));
        numLabel.setForeground(WHITE);
        numLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        numLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setForeground(LABEL_C);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel col = new JPanel();
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));
        col.setBackground(CARD_BG);
        col.setBorder(new EmptyBorder(0, 0, 16, 50));
        col.add(numLabel);
        col.add(lbl);
        return col;
    }

    // ── CENTER ────────────────────────────────────────────────────────────────
    private JPanel buildCenter() {
        JPanel center = new JPanel(new GridLayout(1, 2));
        center.setBackground(BG);
        center.add(buildBioPanel());
        center.add(buildRecentMoviesPanel());
        return center;
    }

    private JPanel buildBioPanel() {
        String bio         = UserDAO.getBio(userEmail);
        String memberSince = UserDAO.getMemberSince(userEmail);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, BORDER_C),
                new EmptyBorder(40, 60, 40, 50)
        ));

        JLabel bioTitle = new JLabel("BIO");
        bioTitle.setForeground(LABEL_C);
        bioTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        bioTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea bioText = new JTextArea(bio != null && !bio.isEmpty() ? bio : "No bio yet. Click Edit Profile to add one.");
        bioText.setForeground(bio != null && !bio.isEmpty() ? WHITE : new Color(70, 90, 150));
        bioText.setBackground(BG);
        bioText.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        bioText.setLineWrap(true);
        bioText.setWrapStyleWord(true);
        bioText.setEditable(false);
        bioText.setFocusable(false);
        bioText.setAlignmentX(Component.LEFT_ALIGNMENT);
        bioText.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_C);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        sep.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sinceTitle = new JLabel("MEMBER SINCE");
        sinceTitle.setForeground(LABEL_C);
        sinceTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sinceTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sinceValue = new JLabel(memberSince != null ? memberSince : "—");
        sinceValue.setForeground(WHITE);
        sinceValue.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        sinceValue.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(bioTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(bioText);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(sep);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(sinceTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(sinceValue);

        return panel;
    }

    private JPanel buildRecentMoviesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG);
        panel.setBorder(new EmptyBorder(30, 40, 30, 60));

        JLabel sectionTitle = new JLabel("RECENT MOVIES");
        sectionTitle.setForeground(LABEL_C);
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 13));
        sectionTitle.setBorder(new EmptyBorder(0, 0, 16, 0));
        panel.add(sectionTitle, BorderLayout.NORTH);

        List<Movie> movies = MovieDAO.getMoviesByUser(userEmail);
        JPanel grid = new JPanel(new GridLayout(2, 3, 12, 12));
        grid.setBackground(BG);

        int total = movies.size();
        int count = Math.min(total, 6);
        for (int i = total - 1; i >= total - count; i--) {
            grid.add(buildMovieTile(movies.get(i)));
        }
        for (int i = count; i < 6; i++) {
            JPanel empty = new JPanel();
            empty.setBackground(INPUT_BG);
            empty.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));
            grid.add(empty);
        }

        panel.add(grid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildMovieTile(Movie movie) {
        JPanel tile = new JPanel(new BorderLayout());
        tile.setBackground(CARD_BG);
        tile.setBorder(BorderFactory.createLineBorder(BORDER_C, 1));

        if (movie.getImagePath() != null && !movie.getImagePath().isEmpty()) {
            try {
                BufferedImage img = ImageIO.read(new File(movie.getImagePath()));
                JLabel imgLabel = new JLabel(new ImageIcon(img.getScaledInstance(120, 90, Image.SCALE_SMOOTH)));
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
                tile.add(imgLabel, BorderLayout.CENTER);
            } catch (Exception e) { addTileText(tile, movie.getTitle()); }
        } else { addTileText(tile, movie.getTitle()); }

        JLabel titleLbl = new JLabel(movie.getTitle(), SwingConstants.CENTER);
        titleLbl.setForeground(WHITE);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        titleLbl.setBorder(new EmptyBorder(4, 4, 4, 4));
        titleLbl.setBackground(new Color(0, 0, 0, 150));
        titleLbl.setOpaque(true);
        tile.add(titleLbl, BorderLayout.SOUTH);
        return tile;
    }

    private void addTileText(JPanel tile, String title) {
        JLabel lbl = new JLabel(title, SwingConstants.CENTER);
        lbl.setForeground(LABEL_C);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        tile.add(lbl, BorderLayout.CENTER);
    }

    // ── BOTTOM ────────────────────────────────────────────────────────────────
    private JPanel buildBottom() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 14));
        panel.setBackground(BG);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_C));

        if (parentFrame != null) {
            JButton homeBtn = styledButton("⌂  Home", new Color(30, 40, 90), 140, 40);
            homeBtn.addActionListener(e -> { dispose(); parentFrame.toFront(); parentFrame.requestFocus(); });
            panel.add(homeBtn);
        }

        JButton editBtn = styledButton("Edit Profile", new Color(0, 120, 200), 150, 40);
        editBtn.addActionListener(e -> showEditDialog());
        panel.add(editBtn);

        return panel;
    }

    // ── EDIT DIALOG ───────────────────────────────────────────────────────────
    private void showEditDialog() {
        String[] info = UserDAO.getUserInfo(userEmail);
        String bio     = UserDAO.getBio(userEmail);

        JTextField firstNameField = dialogField(info[0]);
        JTextField lastNameField  = dialogField(info[1]);
        JTextField emailField     = dialogField(userEmail);
        emailField.setEditable(false);
        emailField.setForeground(new Color(80, 100, 160));
        JTextField birthdayField  = dialogField(info[2]);
        JTextField phoneField     = dialogField(info[3]);
        JTextArea  bioArea        = new JTextArea(bio != null ? bio : "", 4, 20);
        bioArea.setBackground(INPUT_BG);
        bioArea.setForeground(WHITE);
        bioArea.setCaretColor(WHITE);
        bioArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bioArea.setLineWrap(true);
        bioArea.setWrapStyleWord(true);
        bioArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(new Color(12, 16, 42));
        form.setBorder(new EmptyBorder(20, 20, 10, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill      = GridBagConstraints.HORIZONTAL;
        gbc.weightx   = 1.0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        addDialogRow(form, gbc, 0, "First Name",  firstNameField);
        addDialogRow(form, gbc, 1, "Last Name",   lastNameField);
        addDialogRow(form, gbc, 2, "Email",       emailField);
        addDialogRow(form, gbc, 3, "Birthday",    birthdayField);
        addDialogRow(form, gbc, 4, "Phone",       phoneField);

        GridBagConstraints lblGbc = (GridBagConstraints) gbc.clone();
        lblGbc.gridy  = 10;
        lblGbc.insets = new Insets(12, 0, 4, 0);
        form.add(dialogLabel("Bio"), lblGbc);

        GridBagConstraints areaGbc = (GridBagConstraints) gbc.clone();
        areaGbc.gridy  = 11;
        areaGbc.insets = new Insets(0, 0, 10, 0);
        form.add(new JScrollPane(bioArea), areaGbc);

        JSeparator sep = new JSeparator();
        sep.setForeground(BORDER_C);
        GridBagConstraints sepGbc = (GridBagConstraints) gbc.clone();
        sepGbc.gridy  = 12;
        sepGbc.insets = new Insets(10, 0, 10, 0);
        form.add(sep, sepGbc);

        JLabel pwTitle = new JLabel("CHANGE PASSWORD");
        pwTitle.setForeground(LABEL_C);
        pwTitle.setFont(new Font("Segoe UI", Font.BOLD, 11));
        GridBagConstraints pwTitleGbc = (GridBagConstraints) gbc.clone();
        pwTitleGbc.gridy  = 13;
        pwTitleGbc.insets = new Insets(0, 0, 8, 0);
        form.add(pwTitle, pwTitleGbc);

        JPasswordField currentPw = dialogPwField();
        JPasswordField newPw     = dialogPwField();
        JPasswordField confirmPw = dialogPwField();

        addDialogRow(form, gbc, 7, "Current Password", currentPw);
        addDialogRow(form, gbc, 8, "New Password",     newPw);
        addDialogRow(form, gbc, 9, "Confirm Password", confirmPw);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setPreferredSize(new Dimension(460, 580));
        scroll.setBorder(null);
        scroll.getViewport().setBackground(new Color(12, 16, 42));

        int result = JOptionPane.showConfirmDialog(
                this, scroll, "Edit Profile",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String fn = firstNameField.getText().trim();
            String ln = lastNameField.getText().trim();
            if (fn.isEmpty() || ln.isEmpty()) {
                JOptionPane.showMessageDialog(this, "First and Last name cannot be empty."); return;
            }

            UserDAO.updateUserInfo(userEmail, fn, ln,
                    birthdayField.getText().trim(), phoneField.getText().trim());
            UserDAO.updateBio(userEmail, bioArea.getText().trim());

            String c  = new String(currentPw.getPassword()).trim();
            String np = new String(newPw.getPassword()).trim();
            String cp = new String(confirmPw.getPassword()).trim();

            if (!c.isEmpty() || !np.isEmpty() || !cp.isEmpty()) {
                if (c.isEmpty() || np.isEmpty() || cp.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Fill all password fields or leave all empty."); return;
                }
                if (!np.equals(cp)) {
                    JOptionPane.showMessageDialog(this, "New passwords do not match."); return;
                }
                boolean pwOk = UserDAO.changePassword(userEmail, c, np);
                if (!pwOk) { JOptionPane.showMessageDialog(this, "Current password is incorrect."); return; }
            }

            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            dispose();
            new ProfileFrame(userEmail, parentFrame);
        }
    }

    private void addDialogRow(JPanel parent, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        GridBagConstraints lgbc = (GridBagConstraints) gbc.clone();
        lgbc.gridy  = row * 2;
        lgbc.insets = new Insets(row == 0 ? 0 : 8, 0, 2, 0);
        parent.add(dialogLabel(labelText), lgbc);

        GridBagConstraints fgbc = (GridBagConstraints) gbc.clone();
        fgbc.gridy  = row * 2 + 1;
        fgbc.insets = new Insets(0, 0, 2, 0);
        parent.add(field, fgbc);
    }

    private JTextField dialogField(String value) {
        JTextField tf = new JTextField(value != null ? value : "");
        tf.setBackground(INPUT_BG);
        tf.setForeground(WHITE);
        tf.setCaretColor(WHITE);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        tf.setPreferredSize(new Dimension(0, 38));
        return tf;
    }

    private JPasswordField dialogPwField() {
        JPasswordField pf = new JPasswordField();
        pf.setBackground(INPUT_BG);
        pf.setForeground(WHITE);
        pf.setCaretColor(WHITE);
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        pf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_C, 1),
                new EmptyBorder(8, 10, 8, 10)
        ));
        pf.setPreferredSize(new Dimension(0, 38));
        return pf;
    }

    // ── HELPERS ───────────────────────────────────────────────────────────────
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

    private JLabel dialogLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(LABEL_C);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return l;
    }

    private void chooseAvatar() {
        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png", "gif"));
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            avatarPath = fc.getSelectedFile().getAbsolutePath();
            UserDAO.updateProfilePicture(userEmail, avatarPath);
            avatarLabel.repaint();
        }
    }
}