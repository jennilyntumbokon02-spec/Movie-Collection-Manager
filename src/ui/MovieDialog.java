package ui;

import dao.MovieDAO;
import models.Movie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class MovieDialog extends JDialog {

    private static final Color DIALOG_BG = new Color(15, 20, 50);
    private static final Color INPUT_BG = new Color(25, 32, 70);
    private static final Color LABEL_COLOR = new Color(160, 185, 255);

    public MovieDialog(JFrame parent, Movie m, Runnable refreshCallback) {
        super(parent, m != null ? "Edit Movie" : "Add Movie", true);

        boolean isEdit = m != null;

        setSize(540, 580);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(DIALOG_BG);
        setResizable(false);

        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(DIALOG_BG);
        body.setBorder(new EmptyBorder(20, 24, 16, 24));

        JTextField titleField = new JTextField(isEdit ? m.getTitle() : "");
        JTextField yearField = new JTextField(isEdit ? String.valueOf(m.getYear()) : "");

        JComboBox<String> genreBox = new JComboBox<>(new String[]{
                "Action", "Drama", "Comedy", "Horror", "Sci-Fi", "Romance", "Thriller"
        });

        JTextArea reviewArea = new JTextArea(isEdit ? m.getReview() : "", 4, 20);

        StarRating starRating = new StarRating();
        if (isEdit) starRating.setRating(m.getRating());

        JButton saveBtn = new JButton(isEdit ? "Save Changes" : "Add Movie");

        saveBtn.addActionListener(e -> {
            try {
                String t = titleField.getText();
                String g = genreBox.getSelectedItem().toString();
                int y = Integer.parseInt(yearField.getText());
                int r = starRating.getRating();
                String rev = reviewArea.getText();

                if (isEdit) {
                    MovieDAO.updateMovie(m.getId(), t, g, y, r, rev, "");
                } else {
                    MovieDAO.addMovie(t, g, y, r, rev, "");
                }

                refreshCallback.run();
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input");
            }
        });

        body.add(new JLabel("Title"));
        body.add(titleField);

        body.add(new JLabel("Genre"));
        body.add(genreBox);

        body.add(new JLabel("Year"));
        body.add(yearField);

        body.add(new JLabel("Rating"));
        body.add(starRating);

        body.add(new JLabel("Review"));
        body.add(new JScrollPane(reviewArea));

        body.add(saveBtn);

        add(body, BorderLayout.CENTER);
    }
}