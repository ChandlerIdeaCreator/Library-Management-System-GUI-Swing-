package gui;

import user.UserManager;

import javax.swing.*;
import java.awt.*;
// No explicit ActionEvent/ActionListener import needed

public class RegisterUserDialog extends JDialog { // Extends JDialog
    private UserManager userManager; // UserManager
    private LibraryAppGUI mainGUI; // LibraryAppGUI

    private JTextField usernameField; // JTextField
    private JPasswordField passwordField; // JPasswordField
    private JComboBox<String> roleComboBox; // JComboBox
    private JButton registerButton, cancelButton; // JButton

    public RegisterUserDialog(JFrame parent, UserManager userManager) { // Accepts JFrame, UserManager
        super(parent, "注册新用户", true);
        this.userManager = userManager;
        this.mainGUI = (LibraryAppGUI)parent;

        setLayout(new GridBagLayout()); // GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints
        gbc.insets = new Insets(5, 5, 5, 5); // Insets
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        add(new JLabel("用户名:"), getGBC(0, row, GridBagConstraints.EAST, 0.0)); // JLabel, getGBC
        usernameField = new JTextField(20); // JTextField
        add(usernameField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        add(new JLabel("密码:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        passwordField = new JPasswordField(20); // JPasswordField
        add(passwordField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        add(new JLabel("角色:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        roleComboBox = new JComboBox<>(new String[]{"普通用户", "管理员"}); // JComboBox, String array
        add(roleComboBox, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // JPanel, FlowLayout
        registerButton = new JButton("注册"); // JButton
        cancelButton = new JButton("取消"); // JButton
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        registerButton.addActionListener(e -> performRegistration()); // Calls local method
        cancelButton.addActionListener(e -> dispose()); // JDialog method

        pack(); // JDialog method
        setLocationRelativeTo(parent); // JDialog method
    }

    private GridBagConstraints getGBC(int gridx, int gridy, int anchor, double weightx) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.anchor = anchor;
        gbc.fill = (gridx == 1) ? GridBagConstraints.HORIZONTAL : GridBagConstraints.NONE;
        gbc.weightx = weightx;
        return gbc;
    }


    private void performRegistration() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "用户名和密码不能为空。", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Call registerUser method in UserManager
        // This line MUST receive a boolean if UserManager.registerUser returns boolean
        boolean success = userManager.registerUser(username, password, role); // This should be line ~83

        if (success) {
            JOptionPane.showMessageDialog(this, "用户 " + username + " 注册成功！", "注册成功", JOptionPane.INFORMATION_MESSAGE);
            if (mainGUI.getUserPanel() != null) { // Getters
                mainGUI.getUserPanel().refreshUserList(); // refreshUserList from UserPanel
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "用户注册失败。\n请查看控制台了解详情。", "注册失败", JOptionPane.ERROR_MESSAGE);
        }
    }
}