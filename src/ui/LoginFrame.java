package ui;

import dao.UserDAO;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField email;
    private JPasswordField password;

    public LoginFrame() {
        setTitle("Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(new Color(5, 10, 30));

        JPanel card = new JPanel(new GridLayout(7, 1, 15, 15));
        card.setPreferredSize(new Dimension(400, 500));
        card.setBackground(new Color(15, 20, 50));
        card.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel title = new JLabel("LOGIN");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));

        email = new JTextField();
        email.setBorder(BorderFactory.createTitledBorder("Email"));

        password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setForeground(Color.WHITE);
        showPass.setBackground(new Color(15, 20, 50));
        char defaultEcho = password.getEchoChar();
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) password.setEchoChar((char) 0);
            else password.setEchoChar(defaultEcho);
        });

        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBackground(new Color(70, 130, 255));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setFocusPainted(false);

        JButton signup = new JButton("Create Account");
        signup.setBackground(new Color(40, 40, 80));
        signup.setForeground(Color.WHITE);
        signup.setFocusPainted(false);

        JButton back = new JButton("Back to Landing Page");
        back.setBackground(new Color(40, 40, 80));
        back.setForeground(Color.WHITE);
        back.setFocusPainted(false);

        card.add(title);
        card.add(email);
        card.add(password);
        card.add(showPass);
        card.add(loginBtn);
        card.add(signup);
        card.add(back);

        bg.add(card);
        add(bg);

        loginBtn.addActionListener(e -> login());
        signup.addActionListener(e -> { new SignupFrame(); dispose(); });
        back.addActionListener(e -> { new LandingPageFrame(); dispose(); });

        setVisible(true);
    }

    private void login() {
        String userEmail = email.getText().trim();
        boolean success = UserDAO.login(
                userEmail,
                new String(password.getPassword()).trim()
        );

        if (success) {
            LandingPageFrame.setLoggedInUser(userEmail);
            new MainFrame(userEmail);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Email or Password");
        }
    }
}