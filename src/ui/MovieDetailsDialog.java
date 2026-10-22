package ui;

import models.Movie;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class MovieDetailsDialog extends JDialog {

    public MovieDetailsDialog(JFrame parent, Movie m) {
        super(parent, m.getTitle(), true);

        setSize(400, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());

        Color bg = new Color(15, 20, 50);

        JPanel main = new JPanel(new BorderLayout(10,10));
        main.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        main.setBackground(bg);

        JLabel poster = new JLabel();
        poster.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            File file = new File(m.getImagePath());
            if (file.exists()) {
                ImageIcon icon = new ImageIcon(m.getImagePath());
                Image img = icon.getImage().getScaledInstance(250, 300, Image.SCALE_SMOOTH);
                poster.setIcon(new ImageIcon(img));
            } else {
                poster.setText("No Image");
                poster.setForeground(Color.GRAY);
            }
        } catch (Exception e) {
            poster.setText("No Image");
        }

        main.add(poster, BorderLayout.NORTH);

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setBackground(bg);

        JLabel title = new JLabel(m.getTitle());
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);

        JLabel genre = new JLabel("Genre: " + m.getGenre());
        JLabel year = new JLabel("Year: " + m.getYear());
        JLabel rating = new JLabel("Rating: " + "★".repeat(m.getRating()));

        genre.setForeground(Color.WHITE);
        year.setForeground(Color.WHITE);
        rating.setForeground(new Color(255,215,0));

        JTextArea review = new JTextArea(m.getReview());
        review.setLineWrap(true);
        review.setWrapStyleWord(true);
        review.setEditable(false);
        review.setBackground(bg);
        review.setForeground(Color.LIGHT_GRAY);

        details.add(title);
        details.add(Box.createVerticalStrut(10));
        details.add(genre);
        details.add(year);
        details.add(rating);
        details.add(Box.createVerticalStrut(10));
        details.add(new JScrollPane(review));

        main.add(details, BorderLayout.CENTER);

        add(main);
    }
}