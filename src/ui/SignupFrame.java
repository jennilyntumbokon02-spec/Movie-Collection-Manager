package ui;

import dao.UserDAO;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;

public class SignupFrame extends JFrame {

    private JTextField email, first, last, phone;
    private JPasswordField password;
    private JComboBox<String> month, day, year;

    public SignupFrame() {
        setTitle("Sign Up");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel bg = new JPanel(new GridBagLayout());
        bg.setBackground(new Color(5, 10, 30));

        JPanel card = new JPanel(new GridLayout(11,1,10,10));
        card.setPreferredSize(new Dimension(450, 600));
        card.setBackground(new Color(15, 20, 50));
        card.setBorder(BorderFactory.createEmptyBorder(25,40,25,40));

        JLabel title = new JLabel("CREATE ACCOUNT");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        email = new JTextField();
        email.setBorder(BorderFactory.createTitledBorder("Email"));

        first = new JTextField();
        first.setBorder(BorderFactory.createTitledBorder("First Name"));

        last = new JTextField();
        last.setBorder(BorderFactory.createTitledBorder("Last Name"));

        phone = new JTextField();
        phone.setBorder(BorderFactory.createTitledBorder("Phone Number"));

        ((AbstractDocument) phone.getDocument()).setDocumentFilter(new DocumentFilter() {
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d+") && fb.getDocument().getLength() + string.length() <= 11) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            public void replace(FilterBypass fb, int offset, int length, String string, AttributeSet attr) throws BadLocationException {
                if (string.matches("\\d+") && fb.getDocument().getLength() - length + string.length() <= 11) {
                    super.replace(fb, offset, length, string, attr);
                }
            }
        });

        password = new JPasswordField();
        password.setBorder(BorderFactory.createTitledBorder("Password"));

        JCheckBox showPass = new JCheckBox("Show Password");
        showPass.setForeground(Color.WHITE);
        showPass.setBackground(new Color(15, 20, 50));
        char defaultEcho = password.getEchoChar();
        showPass.addActionListener(e -> {
            if (showPass.isSelected()) {
                password.setEchoChar((char)0);
            } else {
                password.setEchoChar(defaultEcho);
            }
        });

        String[] months = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        String[] days = new String[31];
        String[] years = new String[50];

        for(int i=0;i<31;i++) days[i] = String.valueOf(i+1);
        for(int i=0;i<50;i++) years[i] = String.valueOf(2025 - i);

        month = new JComboBox<>(months);
        day = new JComboBox<>(days);
        year = new JComboBox<>(years);

        JPanel datePanel = new JPanel(new GridLayout(1,3));
        datePanel.add(month);
        datePanel.add(day);
        datePanel.add(year);

        JButton register = new JButton("REGISTER");
        register.setBackground(new Color(70,130,255));
        register.setForeground(Color.WHITE);

        JButton back = new JButton("Back to Login");
        back.setBackground(new Color(40,40,80));
        back.setForeground(Color.WHITE);

        card.add(title);
        card.add(email);
        card.add(first);
        card.add(last);
        card.add(datePanel);
        card.add(phone);
        card.add(password);
        card.add(showPass);
        card.add(register);
        card.add(back);

        bg.add(card);
        add(bg);

        register.addActionListener(e -> register());

        back.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        setVisible(true);
    }

    private void register() {
        String birthday = month.getSelectedItem()+"-"+day.getSelectedItem()+"-"+year.getSelectedItem();

        boolean success = UserDAO.addUser(
                email.getText().trim(),
                first.getText().trim(),
                last.getText().trim(),
                birthday,
                phone.getText().trim(),
                new String(password.getPassword()).trim()
        );

        if(success){
            JOptionPane.showMessageDialog(this,"Account Created");
            new LoginFrame();
            dispose();
        }else{
            JOptionPane.showMessageDialog(this,"Registration failed");
        }
    }
}
