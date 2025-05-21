package gui;

import user.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel { // Extends JPanel from javax.swing
    private UserManager userManager; // UserManager is in user package
    private LibraryAppGUI mainGUI; // LibraryAppGUI is in gui package

    private JTextField usernameField; // JTextField is in javax.swing
    private JPasswordField passwordField; // JPasswordField is in javax.swing
    private JButton loginButton; // JButton is in javax.swing
    private JButton userManagementButton; // JButton is in javax.swing
    private JButton exitButton; // JButton is in javax.swing


    public LoginPanel(UserManager userManager, LibraryAppGUI mainGUI) {
        this.userManager = userManager;
        this.mainGUI = mainGUI;

        setLayout(new GridBagLayout()); // GridBagLayout is in java.awt
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints is in java.awt
        gbc.insets = new Insets(5, 5, 5, 5); // Insets is in java.awt

        JLabel titleLabel = new JLabel("<html><font size='+2'>媒体库管理系统 - 用户登录</font></html>", SwingConstants.CENTER); // JLabel, SwingConstants from javax.swing
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER; // GridBagConstraints constant
        add(titleLabel, gbc); // JPanel method add

        JLabel usernameLabel = new JLabel("用户名:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(usernameLabel, gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("密码:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(passwordLabel, gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // FlowLayout is in java.awt
        loginButton = new JButton("登录");
        userManagementButton = new JButton("用户管理");
        exitButton = new JButton("退出");

        buttonPanel.add(loginButton);
        buttonPanel.add(userManagementButton);
        buttonPanel.add(exitButton);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);


        loginButton.addActionListener(e -> performLogin()); // addActionListener needs java.awt.event.ActionListener (lambda is fine)
        userManagementButton.addActionListener(e -> mainGUI.showPanel("UserManagement")); // showPanel is in LibraryAppGUI
        exitButton.addActionListener(e -> System.exit(0)); // System is in java.lang

        passwordField.addActionListener(e -> performLogin());
        usernameField.addActionListener(e -> performLogin());
    }

    private void performLogin() {
        String username = usernameField.getText(); // JTextField method
        String password = new String(passwordField.getPassword()); // JPasswordField method

        if (username.trim().isEmpty() || password.isEmpty()) { // String methods
            JOptionPane.showMessageDialog(this, "请输入用户名和密码。", "登录失败", JOptionPane.WARNING_MESSAGE); // JOptionPane
            return;
        }

        if (userManager.login(username, password)) { // login is in UserManager
            JOptionPane.showMessageDialog(this, "登录成功！欢迎 " + username, "登录成功", JOptionPane.INFORMATION_MESSAGE);
            mainGUI.showPanel("MainMenu"); // showPanel in LibraryAppGUI
            // clearFields() is called by mainGUI.showPanel("MainMenu"); - this line is NOT here
        } else {
            JOptionPane.showMessageDialog(this, "登录失败: 用户名或密码错误。", "登录失败", JOptionPane.ERROR_MESSAGE);
        }
    }

    // This method needs to be PUBLIC so LibraryAppGUI can call it
    public void clearFields() { // **Corrected signature**
        usernameField.setText(""); // JTextField method
        passwordField.setText(""); // JPasswordField method
    }
}