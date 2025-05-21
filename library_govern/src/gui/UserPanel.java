package gui;

import user.User;
import user.UserManager;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel { // Extends JPanel
    private UserManager userManager; // UserManager
    private LibraryAppGUI mainGUI; // LibraryAppGUI

    private JList<String> userList; // JList
    private DefaultListModel<String> userListModel; // DefaultListModel
    private JButton registerButton; // JButton
    private JButton deleteButton;
    private JButton backButton;

    public UserPanel(UserManager userManager, LibraryAppGUI mainGUI) {
        this.userManager = userManager;
        this.mainGUI = mainGUI;

        setLayout(new BorderLayout(10, 10)); // BorderLayout
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // BorderFactory

        JLabel titleLabel = new JLabel("用户管理", SwingConstants.CENTER); // JLabel, SwingConstants
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f)); // Font method
        add(titleLabel, BorderLayout.NORTH); // JPanel method, BorderLayout constant

        userListModel = new DefaultListModel<>();
        userList = new JList<>(userListModel);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // ListSelectionModel constant
        JScrollPane listScrollPane = new JScrollPane(userList); // JScrollPane
        add(listScrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // FlowLayout
        registerButton = new JButton("注册新用户");
        deleteButton = new JButton("删除选定用户");
        backButton = new JButton("返回主菜单");

        buttonPanel.add(registerButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(backButton);
        add(buttonPanel, BorderLayout.SOUTH);

        registerButton.addActionListener(e -> showRegisterDialog()); // Calls method in THIS class
        deleteButton.addActionListener(e -> performDeleteUser()); // Calls method in THIS class
        backButton.addActionListener(e -> mainGUI.showPanel("MainMenu")); // Delegates to LibraryAppGUI

        refreshUserList(); // Calls method in THIS class
    }

    // Populates the JList with current users
    public void refreshUserList() {
        userListModel.clear(); // DefaultListModel method
        List<User> users = userManager.getAllUsers(); // List from java.util, User from user package
        for (User user : users) { // User from user package
            userListModel.addElement(user.getUsername() + " (" + user.getRole() + ")");
        }
    }

    private void showRegisterDialog() {
        RegisterUserDialog dialog = new RegisterUserDialog(mainGUI, userManager); // RegisterUserDialog is in gui package
        dialog.setVisible(true); // JDialog method
        // Dialog will call mainGUI.getUserPanel().refreshUserList() on success
    }

    private void performDeleteUser() {
        String selectedUserString = userList.getSelectedValue(); // JList method
        if (selectedUserString == null) {
            JOptionPane.showMessageDialog(this, "请先从列表中选择要删除的用户。", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int spaceIndex = selectedUserString.indexOf(" "); // String method
        String usernameToDelete = (spaceIndex != -1) ? selectedUserString.substring(0, spaceIndex) : selectedUserString; // String method

        int confirm = JOptionPane.showConfirmDialog(this,
                "确定要删除用户 '" + usernameToDelete + "' 吗？",
                "确认删除",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = userManager.deleteUser(usernameToDelete); // deleteUser from UserManager
            if (success) {
                JOptionPane.showMessageDialog(this, "用户删除成功！", "删除用户", JOptionPane.INFORMATION_MESSAGE);
                refreshUserList(); // Calls method in THIS class
            } else {
                String reason = "未知错误。";
                if (userManager.getLoggedInUser() == null || !userManager.getLoggedInUser().getRole().equals("管理员")) {
                    reason = "您没有权限。";
                } else if (userManager.getLoggedInUser().getUsername().equals(usernameToDelete)) {
                    reason = "您不能删除当前登录的账户！";
                } else {
                    reason = "用户 '" + usernameToDelete + "' 未找到。";
                }
                JOptionPane.showMessageDialog(this, "删除失败: " + reason, "删除用户", JOptionPane.ERROR_MESSAGE);
                refreshUserList(); // Refresh list just in case
            }
        }
    }
}