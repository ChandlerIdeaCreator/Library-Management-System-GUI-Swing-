package gui;

import library.LibraryManager;
import media.*;
import media.Object;
import media.Time;

import javax.swing.*;
import java.awt.*;
// No explicit ActionEvent/ActionListener imports for lambdas
// No time/date format imports needed as Time class handles it

public class AddItemDialog extends JDialog { // Extends JDialog from javax.swing
    private LibraryManager libraryManager; // LibraryManager is in library package
    private LibraryAppGUI mainGUI; // LibraryAppGUI is in gui package

    private JComboBox<String> typeComboBox; // JComboBox
    private JTextField idField, titleField, authorField, ratingField; // JTextField

    private JPanel typeSpecificPanel; // JPanel
    private JPanel bookPanel, picturePanel, videoDiskPanel;

    private JTextField publisherField, isbnField, pageCountField;
    private JTextField nationalityField, lengthField, widthField;
    private JTextField producerField, durationField;
    private JTextField releaseDateField;


    private JButton addButton, cancelButton; // JButton

    public AddItemDialog(JFrame parent, LibraryManager libraryManager) { // Extends JDialog, accepts JFrame from javax.swing
        super(parent, "添加新物品", true); // Call JDialog constructor
        this.libraryManager = libraryManager;
        this.mainGUI = (LibraryAppGUI) parent; // Cast to LibraryAppGUI (in gui package)

        setLayout(new BorderLayout()); // BorderLayout is in java.awt
        setPreferredSize(new Dimension(400, 550)); // Dimension is in java.awt
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE); // JDialog constant

        JPanel commonPanel = new JPanel(new GridBagLayout()); // JPanel, GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints(); // GridBagConstraints
        gbc.insets = new Insets(5, 5, 5, 5); // Insets
        gbc.fill = GridBagConstraints.HORIZONTAL; // GridBagConstraints constant

        int row = 0;
        commonPanel.add(new JLabel("物品类型:"), getGBC(0, row, GridBagConstraints.EAST, 0.0)); // JLabel, GridBagConstraints constant, getGBC is local method
        typeComboBox = new JComboBox<>(new String[]{"图书", "图画", "视频光盘"}); // JComboBox
        commonPanel.add(typeComboBox, getGBC(1, row++, GridBagConstraints.WEST, 1.0)); // GridBagConstraints constant

        commonPanel.add(new JLabel("编号:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        idField = new JTextField(20); // JTextField
        commonPanel.add(idField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        commonPanel.add(new JLabel("标题:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        titleField = new JTextField(20);
        commonPanel.add(titleField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        commonPanel.add(new JLabel("作者:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        authorField = new JTextField(20);
        commonPanel.add(authorField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        commonPanel.add(new JLabel("评级 (未评级/一般/成人/儿童):"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        ratingField = new JTextField(20);
        commonPanel.add(ratingField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        add(commonPanel, BorderLayout.NORTH);

        typeSpecificPanel = new JPanel(new CardLayout()); // JPanel, CardLayout
        createTypeSpecificPanels(); // Calls local method
        typeSpecificPanel.add(bookPanel, "图书"); // JPanel
        typeSpecificPanel.add(picturePanel, "图画"); // JPanel
        typeSpecificPanel.add(videoDiskPanel, "视频光盘"); // JPanel
        add(typeSpecificPanel, BorderLayout.CENTER); // BorderLayout constant

        typeComboBox.addActionListener(e -> { // addActionListener
            CardLayout cl = (CardLayout)(typeSpecificPanel.getLayout()); // CardLayout cast
            cl.show(typeSpecificPanel, (String)typeComboBox.getSelectedItem()); // CardLayout method
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // JPanel, FlowLayout
        addButton = new JButton("添加"); // JButton
        cancelButton = new JButton("取消");

        buttonPanel.add(addButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> performAddItem()); // Calls local method
        cancelButton.addActionListener(e -> dispose()); // JDialog method

        ((CardLayout)typeSpecificPanel.getLayout()).show(typeSpecificPanel, (String)typeComboBox.getSelectedItem());

        pack(); // JDialog method
        setLocationRelativeTo(parent); // JDialog method, parent is JFrame from javax.swing
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

    private void createTypeSpecificPanels() {
        // --- Book Panel ---
        bookPanel = new JPanel(new GridBagLayout());
        int row = 0;
        bookPanel.add(new JLabel("出版社:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        publisherField = new JTextField(20);
        bookPanel.add(publisherField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        bookPanel.add(new JLabel("ISBN号:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        isbnField = new JTextField(20);
        bookPanel.add(isbnField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        bookPanel.add(new JLabel("页数:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        pageCountField = new JTextField(20);
        bookPanel.add(pageCountField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        // --- Picture Panel ---
        picturePanel = new JPanel(new GridBagLayout());
        row = 0;
        picturePanel.add(new JLabel("出品国籍:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        nationalityField = new JTextField(20);
        picturePanel.add(nationalityField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        picturePanel.add(new JLabel("长度 (cm):"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        lengthField = new JTextField(20);
        picturePanel.add(lengthField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        picturePanel.add(new JLabel("宽度 (cm):"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        widthField = new JTextField(20);
        picturePanel.add(widthField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        // --- VideoDisk Panel ---
        videoDiskPanel = new JPanel(new GridBagLayout());
        row = 0;
        videoDiskPanel.add(new JLabel("出品人:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        producerField = new JTextField(20);
        videoDiskPanel.add(producerField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        videoDiskPanel.add(new JLabel("出品日期 (YYYY-MM-DD):"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        releaseDateField = new JTextField(20);
        videoDiskPanel.add(releaseDateField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        videoDiskPanel.add(new JLabel("时长 (min):"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        durationField = new JTextField(20);
        videoDiskPanel.add(durationField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));
    }


    private void performAddItem() {
        String id = idField.getText().trim();
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String rating = ratingField.getText().trim();

        if (id.isEmpty() || title.isEmpty() || author.isEmpty() || rating.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写所有必填的通用信息 (编号、标题、作者、评级)。", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        media.Object newItem = null; // media.Object
        String selectedType = (String) typeComboBox.getSelectedItem();

        try {
            switch (selectedType) {
                case "图书":
                    String publisher = publisherField.getText().trim();
                    String isbn = isbnField.getText().trim();
                    int pageCount;
                    try {
                        pageCount = Integer.parseInt(pageCountField.getText().trim()); // Integer.parseInt from java.lang
                        if (pageCount < 0) throw new NumberFormatException("页数不能为负数。"); // NumberFormatException from java.lang
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("页数输入无效: " + e.getMessage()); // IllegalArgumentException from java.lang
                    }
                    if (publisher.isEmpty() || isbn.isEmpty()) throw new IllegalArgumentException("请填写图书的出版社和ISBN。");
                    newItem = new Book(id, title, author, rating, publisher, isbn, pageCount); // Book from media package
                    break;

                case "图画":
                    String nationality = nationalityField.getText().trim();
                    int length, width;
                    try {
                        length = Integer.parseInt(lengthField.getText().trim());
                        if (length < 0) throw new NumberFormatException("长度不能为负数。");
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("长度输入无效: " + e.getMessage());
                    }
                    try {
                        width = Integer.parseInt(widthField.getText().trim());
                        if (width < 0) throw new NumberFormatException("宽度不能为负数。");
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("宽度输入无效: " + e.getMessage());
                    }
                    if (nationality.isEmpty()) throw new IllegalArgumentException("请填写图画的出品国籍。");
                    newItem = new Picture(id, title, author, rating, nationality, length, width); // Picture from media package
                    break;

                case "视频光盘":
                    String producer = producerField.getText().trim();
                    String dateString = releaseDateField.getText().trim();
                    int duration;
                    try {
                        duration = Integer.parseInt(durationField.getText().trim());
                        if (duration < 0) throw new NumberFormatException("时长不能为负数。");
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("时长输入无效: " + e.getMessage());
                    }
                    if (producer.isEmpty() || dateString.isEmpty()) throw new IllegalArgumentException("请填写视频光盘的出品人和日期。");

                    media.Time releaseDate; // media.Time
                    try {
                        releaseDate = media.Time.parse(dateString); // media.Time.parse
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("出品日期格式无效，应为 YYYY-MM-DD: " + e.getMessage());
                    }
                    newItem = new VideoDisk(id, title, author, rating, producer, releaseDate, duration); // VideoDisk from media package
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "未知的物品类型被选择。", "内部错误", JOptionPane.ERROR_MESSAGE);
                    return;
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "输入错误: " + ex.getMessage(), "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (Exception ex) { // Catch any other Exception from java.lang
            JOptionPane.showMessageDialog(this, "创建物品时发生未知错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace(); // PrintStackTrace
            return;
        }

        if (newItem != null) {
            try {
                libraryManager.addItem(newItem); // addItem from LibraryManager (in library package)
                JOptionPane.showMessageDialog(this, "物品添加成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                mainGUI.getLibraryDisplayPanel().refreshTable(); // Getters from LibraryAppGUI, refreshTable from LibraryDisplayPanel
                dispose();
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "添加失败: " + ex.getMessage(), "添加失败", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalStateException ex) { // IllegalStateException from java.lang
                JOptionPane.showMessageDialog(this, "添加失败: " + ex.getMessage(), "添加失败", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "添加物品时发生未知错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}