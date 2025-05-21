package gui;

import user.UserManager;
import user.User;

import javax.swing.*;
import java.awt.*;
// No explicit ActionEvent/ActionListener import needed for lambdas

public class MainMenuPanel extends JPanel { // Extends JPanel
    private LibraryAppGUI mainGUI; // LibraryAppGUI is in gui package

    private JButton displayButton; // JButton
    private JButton addButton;
    private JButton editButton;
    private JButton deleteButton;
    private JButton queryButton;
    private JButton saveButton;
    private JButton loadButton;
    private JButton userManagementButton;
    private JButton logoutButton;


    public MainMenuPanel(LibraryAppGUI mainGUI) {
        this.mainGUI = mainGUI;

        setLayout(new GridBagLayout()); // GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints
        gbc.insets = new Insets(10, 10, 10, 10); // Insets
        gbc.fill = GridBagConstraints.HORIZONTAL; // GridBagConstraints constant

        JLabel titleLabel = new JLabel("<html><font size='+2'>媒体库管理系统</font></html>", SwingConstants.CENTER); // JLabel, SwingConstants
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc); // JPanel method

        addButton = new JButton("1. 添加物品");
        displayButton = new JButton("2. 显示物品库 (排序/分类)");
        saveButton = new JButton("3. 保存到文件");
        loadButton = new JButton("4. 从文件载入");
        deleteButton = new JButton("5. 删除物品");
        editButton = new JButton("6. 编辑物品");
        queryButton = new JButton("7. 查询物品");
        userManagementButton = new JButton("用户管理");
        logoutButton = new JButton("退出登录");

        gbc.gridwidth = 1;
        gbc.gridx = 0; gbc.gridy = 1; add(addButton, gbc);
        gbc.gridx = 1; gbc.gridy = 1; add(displayButton, gbc);
        gbc.gridx = 0; gbc.gridy = 2; add(saveButton, gbc);
        gbc.gridx = 1; gbc.gridy = 2; add(loadButton, gbc);
        gbc.gridx = 0; gbc.gridy = 3; add(deleteButton, gbc);
        gbc.gridx = 1; gbc.gridy = 3; add(editButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; add(queryButton, gbc);
        gbc.gridx = 1; gbc.gridy = 4; add(userManagementButton, gbc);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; add(logoutButton, gbc);

        addButton.addActionListener(e -> mainGUI.showAddItemDialog()); // Delegates to LibraryAppGUI
        displayButton.addActionListener(e -> mainGUI.showPanel("Display")); // Delegates to LibraryAppGUI
        saveButton.addActionListener(e -> mainGUI.performSave()); // Delegates to LibraryAppGUI
        loadButton.addActionListener(e -> mainGUI.performLoad()); // Delegates to LibraryAppGUI
        deleteButton.addActionListener(e -> mainGUI.performDelete()); // Delegates to LibraryAppGUI
        editButton.addActionListener(e -> mainGUI.showEditItemDialog()); // Delegates to LibraryAppGUI
        queryButton.addActionListener(e -> mainGUI.performQuery()); // Delegates to LibraryAppGUI
        userManagementButton.addActionListener(e -> mainGUI.showPanel("UserManagement")); // Delegates to LibraryAppGUI
        logoutButton.addActionListener(e -> performLogout()); // Calls method in THIS class
    }

    private void performLogout() {
        mainGUI.getUserManager().logout(); // getUserManager from LibraryAppGUI, logout from UserManager
        JOptionPane.showMessageDialog(this, "您已成功退出登录。", "注销成功", JOptionPane.INFORMATION_MESSAGE); // JOptionPane
        mainGUI.showPanel("Login"); // showPanel in LibraryAppGUI
    }
}