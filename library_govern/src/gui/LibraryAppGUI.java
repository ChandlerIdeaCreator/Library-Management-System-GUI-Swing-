package gui;

import library.LibraryManager;
import user.UserManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Optional;

public class LibraryAppGUI extends JFrame { // Extends JFrame from javax.swing
    private UserManager userManager; // UserManager is in user package
    private LibraryManager libraryManager; // LibraryManager is in library package

    private CardLayout cardLayout; // CardLayout is in java.awt
    private JPanel cardPanel; // JPanel is in javax.swing

    private LoginPanel loginPanel; // LoginPanel is in gui package
    private MainMenuPanel mainMenuPanel; // MainMenuPanel is in gui package
    // Changed access to package-private (no modifier) so dialogs/panels in the same package can access
    LibraryDisplayPanel libraryDisplayPanel; // LibraryDisplayPanel is in gui package
    UserPanel userPanel; // UserPanel is in gui package


    public LibraryAppGUI(UserManager userManager, LibraryManager libraryManager) {
        this.userManager = userManager;
        this.libraryManager = libraryManager;

        setTitle("媒体库管理系统"); // JFrame method
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); // JFrame constant
        setSize(800, 600); // JFrame method
        setLocationRelativeTo(null); // JFrame method

        addWindowListener(new WindowAdapter() { // WindowAdapter, WindowEvent is in java.awt.event
            @Override
            public void windowClosing(WindowEvent e) {
                int response = JOptionPane.showConfirmDialog( // JOptionPane is in javax.swing
                        LibraryAppGUI.this,
                        "确定退出并保存媒体库吗？",
                        "退出",
                        JOptionPane.YES_NO_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );

                if (response == JOptionPane.YES_OPTION) {
                    try {
                        libraryManager.saveToFile();
                        dispose(); // JFrame method
                        System.exit(0); // System is in java.lang
                    } catch (RuntimeException ex) {
                        JOptionPane.showMessageDialog(LibraryAppGUI.this,
                                "保存文件时出错:\n" + ex.getMessage(),
                                "保存失败",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else if (response == JOptionPane.NO_OPTION) {
                    dispose();
                    System.exit(0);
                }
            }
        });

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // Create panels - they are in the same package (gui)
        loginPanel = new LoginPanel(userManager, this);
        mainMenuPanel = new MainMenuPanel(this);
        libraryDisplayPanel = new LibraryDisplayPanel(libraryManager, this);
        userPanel = new UserPanel(userManager, this);

        // Add panels to card panel
        cardPanel.add(loginPanel, "Login");
        cardPanel.add(mainMenuPanel, "MainMenu");
        cardPanel.add(libraryDisplayPanel, "Display");
        cardPanel.add(userPanel, "UserManagement");

        add(cardPanel); // JFrame method

        showPanel("Login"); // Calls method in THIS class
    }

    public void showPanel(String panelName) {
        cardLayout.show(cardPanel, panelName);
        if ("Display".equals(panelName)) { // String.equals is in java.lang
            libraryDisplayPanel.refreshTable(); // Calls method in LibraryDisplayPanel (in gui package)
        }
        if ("UserManagement".equals(panelName)) {
            userPanel.refreshUserList(); // Calls method in UserPanel (in gui package)
        }
        if (!"Login".equals(panelName)) {
            loginPanel.clearFields(); // Calls method in LoginPanel (in gui package)
        }
    }

    // --- Public Getters for Panels (Accessed by Dialogs) ---
    public LibraryDisplayPanel getLibraryDisplayPanel() {
        return libraryDisplayPanel;
    }

    public UserPanel getUserPanel() {
        return userPanel;
    }
    // --- Getters for managers ---
    public LibraryManager getLibraryManager() {
        return libraryManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }


    // --- Methods called by MainMenuPanel/Buttons (Delegated from MainMenuPanel) ---

    public void showAddItemDialog() {
        AddItemDialog dialog = new AddItemDialog(this, libraryManager); // AddItemDialog is in gui package
        dialog.setVisible(true); // JDialog method
    }

    public void showEditItemDialog() {
        media.Object selectedItem = libraryDisplayPanel.getSelectedItem(); // media.Object, getSelectedItem from LibraryDisplayPanel
        if (selectedItem == null) {
            JOptionPane.showMessageDialog(this, "请先选择要编辑的物品。", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        EditItemDialog dialog = new EditItemDialog(this, libraryManager, selectedItem); // EditItemDialog is in gui package
        dialog.setVisible(true);
    }

    public void performQuery() {
        String query = JOptionPane.showInputDialog(this, "请输入查询内容 (编号 或 标题):", "查询物品", JOptionPane.QUESTION_MESSAGE);
        if (query != null && !query.trim().isEmpty()) {
            String[] options = {"按编号", "按标题"}; // String array
            int choice = JOptionPane.showOptionDialog(this,
                    "请选择查询方式:",
                    "查询方式",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options, options[0]);

            if (choice == JOptionPane.CLOSED_OPTION) {
                return;
            }

            boolean byId = (choice == JOptionPane.YES_OPTION);

            Optional<media.Object> foundItemOpt = libraryManager.queryItem(query.trim(), byId); // media.Object, Optional from java.util

            if (foundItemOpt.isPresent()) {
                media.Object foundItem = foundItemOpt.get(); // media.Object
                JOptionPane.showMessageDialog(this,
                        "找到物品:\n" + foundItem.getDisplayString(), // getDisplayString from media.Object
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
                libraryDisplayPanel.refreshTable();
                libraryDisplayPanel.highlightItem(foundItem);
            } else {
                JOptionPane.showMessageDialog(this,
                        "未能找到相关物品。",
                        "查询结果",
                        JOptionPane.INFORMATION_MESSAGE);
                libraryDisplayPanel.refreshTable();
            }
        } else if (query != null) {
            JOptionPane.showMessageDialog(this, "查询内容不能为空。", "输入错误", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void performDelete() {
        String idToDelete = JOptionPane.showInputDialog(this, "请输入要删除的物品的编号：", "删除物品", JOptionPane.QUESTION_MESSAGE);
        if (idToDelete != null && !idToDelete.trim().isEmpty()) {
            boolean success = libraryManager.deleteItemById(idToDelete.trim()); // deleteItemById from LibraryManager
            if (success) {
                JOptionPane.showMessageDialog(this, "物品删除成功！", "删除物品", JOptionPane.INFORMATION_MESSAGE);
                libraryDisplayPanel.refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "未能找到该编号的物品。", "删除失败", JOptionPane.WARNING_MESSAGE);
            }
        } else if (idToDelete != null) {
            JOptionPane.showMessageDialog(this, "物品编号不能为空。", "输入错误", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void performSave() {
        try {
            libraryManager.saveToFile(); // saveToFile from LibraryManager
            JOptionPane.showMessageDialog(this, "媒体库已保存成功。", "保存", JOptionPane.INFORMATION_MESSAGE);
        } catch (RuntimeException e) { // RuntimeException from java.lang
            JOptionPane.showMessageDialog(this,
                    "保存文件时出错:\n" + e.getMessage(),
                    "保存失败",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public void performLoad() {
        int response = JOptionPane.showConfirmDialog(
                this,
                "载入将覆盖当前未保存的数据，确定吗？",
                "载入媒体库",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (response == JOptionPane.YES_OPTION) {
            try {
                libraryManager.loadFromFile(); // loadFromFile from LibraryManager
                libraryDisplayPanel.refreshTable();
                JOptionPane.showMessageDialog(this, "媒体库已载入成功。", "载入", JOptionPane.INFORMATION_MESSAGE);
            } catch (RuntimeException e) {
                JOptionPane.showMessageDialog(this,
                        "从文件载入时出错:\n" + e.getMessage(),
                        "载入失败",
                        JOptionPane.ERROR_MESSAGE);
                libraryDisplayPanel.refreshTable();
            }
        }
    }
}