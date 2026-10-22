package ui;

import javax.swing.*;
import java.awt.*;

public class StarRating extends JPanel {

    private int rating = 0;
    private final JLabel[] stars = new JLabel[5];

    public StarRating() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
        setOpaque(false);

        for (int i = 0; i < 5; i++) {
            int index = i;

            stars[i] = new JLabel("★"); // solid star
            stars[i].setFont(new Font("Dialog", Font.PLAIN, 24)); // FIXED FONT
            stars[i].setForeground(Color.GRAY);
            stars[i].setCursor(new Cursor(Cursor.HAND_CURSOR));

            stars[i].addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    rating = index + 1;
                    updateStars();
                }

                public void mouseEntered(java.awt.event.MouseEvent e) {
                    highlightStars(index + 1);
                }

                public void mouseExited(java.awt.event.MouseEvent e) {
                    updateStars();
                }
            });

            add(stars[i]);
        }
    }

    private void updateStars() {
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                stars[i].setForeground(new Color(255, 215, 0)); // ⭐ GOLD
            } else {
                stars[i].setForeground(Color.GRAY);
            }
        }
    }

    private void highlightStars(int hoverRating) {
        for (int i = 0; i < 5; i++) {
            if (i < hoverRating) {
                stars[i].setForeground(new Color(255, 215, 0));
            } else {
                stars[i].setForeground(Color.GRAY);
            }
        }
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int value) {
        rating = value;
        updateStars();
    }
}