package ui;

import models.Movie;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class DashboardPanel extends JPanel {

    private JLabel totalLabel;
    private JLabel avgLabel;
    private JLabel welcomeLabel;

    public DashboardPanel(String userEmail) {
        setLayout(new GridLayout(1, 3, 20, 0));
        setBackground(new Color(10, 15, 40));
        setBorder(new EmptyBorder(15, 40, 15, 40));

        welcomeLabel = createBox("Welcome", userEmail);
        totalLabel = createBox("Total Movies", "0");
        avgLabel = createBox("Avg Rating", "0.0");

        add(welcomeLabel);
        add(totalLabel);
        add(avgLabel);
    }

    private JLabel createBox(String title, String value) {
        JLabel label = new JLabel("<html><center>" +
                "<span style='color:#8fb3ff;font-size:12px;'>" + title + "</span><br>" +
                "<span style='color:white;font-size:18px;font-weight:bold;'>" + value + "</span>" +
                "</center></html>");

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(20, 25, 60));
        label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        return label;
    }

    public void updateStats(List<Movie> movies) {
        int total = movies.size();

        double avg = 0;
        if (total > 0) {
            int sum = 0;
            for (Movie m : movies) sum += m.getRating();
            avg = (double) sum / total;
        }

        totalLabel.setText("<html><center><span style='color:#8fb3ff;'>Total Movies</span><br>" +
                "<b style='color:white;font-size:18px;'>" + total + "</b></center></html>");

        avgLabel.setText("<html><center><span style='color:#8fb3ff;'>Avg Rating</span><br>" +
                "<b style='color:white;font-size:18px;'>" + String.format("%.1f", avg) + "</b></center></html>");
    }
}