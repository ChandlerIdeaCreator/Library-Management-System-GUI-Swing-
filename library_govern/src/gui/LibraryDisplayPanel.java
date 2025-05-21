package gui;
import library.LibraryManager;
import media.Book;
import media.Object;
import media.Picture;
import media.VideoDisk;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;

public class LibraryDisplayPanel extends JPanel { // 继承自 JPanel
    private LibraryManager libraryManager; // 需要 LibraryManager 实例
    private LibraryAppGUI mainGUI; // 需要 LibraryAppGUI 实例来调用其方法

    private JTable libraryTable; // 表格组件
    private DefaultTableModel tableModel; // 表格模型
    private JScrollPane scrollPane; // 滚动面板
    private JButton backButton; // 返回按钮
    private JComboBox<String> filterComboBox; // 过滤下拉框
    private JButton refreshButton; // 刷新按钮


    public LibraryDisplayPanel(LibraryManager libraryManager, LibraryAppGUI mainGUI) {
        this.libraryManager = libraryManager; // 初始化 LibraryManager
        this.mainGUI = mainGUI; // 初始化 LibraryAppGUI

        setLayout(new BorderLayout()); // 设置布局管理器

        // 控制面板 (Filter, Refresh, Back)
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controlPanel.add(new JLabel("显示类型:"));
        filterComboBox = new JComboBox<>(new String[]{"所有物品", "图书", "图画", "视频光盘"});
        controlPanel.add(filterComboBox);

        refreshButton = new JButton("刷新/过滤");
        controlPanel.add(refreshButton);

        backButton = new JButton("返回主菜单");
        controlPanel.add(backButton);

        add(controlPanel, BorderLayout.NORTH); // 将控制面板添加到顶部

        // 表格面板
        String[] columnNames = {"编号", "类型", "标题", "作者", "评级", "查询次数", "详细信息"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 使表格单元格不可编辑
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // 设置列的数据类型，用于排序
                if (columnIndex == 5) return Integer.class; // 查询次数是整数
                return String.class; // 其他列是字符串
            }
        };
        libraryTable = new JTable(tableModel); // 创建 JTable

        // 为表格添加排序器，允许点击表头排序
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        libraryTable.setRowSorter(sorter);

        // 设置列的首选宽度 (可选)
        libraryTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        libraryTable.getColumnModel().getColumn(1).setPreferredWidth(80);
        libraryTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        libraryTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        libraryTable.getColumnModel().getColumn(4).setPreferredWidth(80);
        libraryTable.getColumnModel().getColumn(5).setPreferredWidth(80);
        libraryTable.getColumnModel().getColumn(6).setPreferredWidth(100);


        scrollPane = new JScrollPane(libraryTable); // 将表格放入滚动面板
        add(scrollPane, BorderLayout.CENTER); // 将滚动面板添加到中部


        // 添加事件监听器
        backButton.addActionListener(e -> mainGUI.showPanel("MainMenu")); // 返回主菜单
        refreshButton.addActionListener(e -> refreshTable()); // 刷新表格
        filterComboBox.addActionListener(e -> refreshTable()); // 过滤时也刷新表格

        // 添加鼠标监听器，用于双击表格行显示详情
        libraryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (evt.getClickCount() == 2) { // 检测双击
                    displaySelectedItemDetails(); // 显示选定物品详情
                }
            }
        });


        // 初始加载并显示数据
        refreshTable();
    }

    // 从 LibraryManager 获取数据并刷新表格显示
    public void refreshTable() {
        tableModel.setRowCount(0); // 清空现有数据

        int filterType = filterComboBox.getSelectedIndex(); // 获取当前过滤类型

        List<media.Object> items = libraryManager.getItems(filterType); // 从 Manager 获取过滤并排序后的物品列表

        // 遍历物品列表，将数据添加到表格模型
        for (media.Object item : items) {
            String type = item.getClass().getSimpleName(); // 获取物品类型名称
            java.lang.Object[] rowData = { // 创建一行数据，使用 java.lang.Object 数组
                    item.getId(), // String
                    type, // String
                    item.getTitle(), // String
                    item.getAuthor(), // String
                    item.getRating(), // String
                    item.getQueryCount(), // int 自动装箱为 Integer (它是 java.lang.Object 的子类)
                    "点击查看" // String
            };
            tableModel.addRow(rowData); // 将行数据添加到表格模型
        }
    }

    // 获取当前表格中选定的物品对应的 media.Object 对象
    public media.Object getSelectedItem() {
        int selectedRowView = libraryTable.getSelectedRow(); // 获取视图中选定的行索引
        if (selectedRowView == -1) {
            return null; // 没有选定行
        }
        // 将视图行索引转换为模型行索引 (考虑了排序和过滤)
        int modelRow = libraryTable.convertRowIndexToModel(selectedRowView);
        if (modelRow == -1) return null; // 如果转换失败 (不应该发生)

        // 从表格模型中获取选定行的第一个列 (编号)
        String id = (String) tableModel.getValueAt(modelRow, 0);
        // 通过编号从 LibraryManager 中找到对应的 media.Object 对象
        return libraryManager.findItemById(id).orElse(null); // 使用 Optional 安全地获取对象
    }

    // 在表格中高亮显示某个物品
    public void highlightItem(media.Object item) {
        if (item == null) return;

        // 在表格模型中查找该物品的行索引 (通过编号)
        int modelRowIndex = -1;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (item.getId().equals(tableModel.getValueAt(i, 0))) {
                modelRowIndex = i;
                break;
            }
        }

        if (modelRowIndex != -1) {
            // 将模型行索引转换为视图行索引 (再次确认方法名 convertRowIndexToView)
            // 这一行代码是之前报错的位置
            int viewRowIndex = libraryTable.convertRowIndexToView(modelRowIndex);
            if (viewRowIndex != -1) { // 确保该行在当前过滤视图中可见
                // 选定该行
                libraryTable.setRowSelectionInterval(viewRowIndex, viewRowIndex);
                // 滚动到该行可见区域
                libraryTable.scrollRectToVisible(libraryTable.getCellRect(viewRowIndex, 0, true));
            }
        }
    }

    // 显示选定物品的详细信息对话框
    private void displaySelectedItemDetails() {
        media.Object selectedItem = getSelectedItem(); // 获取选定的物品对象
        if (selectedItem != null) {
            // 显示一个信息对话框
            JOptionPane.showMessageDialog(this,
                    selectedItem.getDisplayString(), // 获取物品的显示字符串
                    "物品详情 - " + selectedItem.getTitle(), // 对话框标题
                    JOptionPane.INFORMATION_MESSAGE); // 对话框类型
        } else {
            // 如果没有选定物品或获取失败，显示错误信息
            JOptionPane.showMessageDialog(this,
                    "未能获取物品详情。",
                    "错误",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}