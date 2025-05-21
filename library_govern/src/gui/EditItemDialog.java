package gui;

import library.LibraryManager;
import media.*;
import media.Object;
import media.Time;

import javax.swing.*;
import java.awt.*;
// No time/date format imports needed

public class EditItemDialog extends JDialog { // Extends JDialog
    private LibraryManager libraryManager; // LibraryManager
    private LibraryAppGUI mainGUI; // LibraryAppGUI
    private media.Object itemToEdit; // media.Object

    private JTextField idField, titleField, authorField, ratingField;

    private JPanel typeSpecificPanel;
    private JPanel bookPanel, picturePanel, videoDiskPanel;

    private JTextField publisherField, isbnField, pageCountField;
    private JTextField nationalityField, lengthField, widthField;
    private JTextField producerField, durationField;
    private JTextField releaseDateField;


    private JButton saveButton, cancelButton;

    public EditItemDialog(JFrame parent, LibraryManager libraryManager, media.Object itemToEdit) { // Accepts JFrame, LibraryManager, media.Object
        super(parent, "编辑物品", true);
        this.libraryManager = libraryManager;
        this.mainGUI = (LibraryAppGUI) parent;
        this.itemToEdit = itemToEdit;

        if (itemToEdit == null) {
            JOptionPane.showMessageDialog(parent, "要编辑的物品为空。", "错误", JOptionPane.ERROR_MESSAGE);
            dispose();
            return;
        }

        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(400, 550));
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel commonPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int row = 0;
        commonPanel.add(new JLabel("物品类型:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        JLabel typeLabel = new JLabel(itemToEdit.getClass().getSimpleName()); // getClass().getSimpleName() is fine
        commonPanel.add(typeLabel, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        commonPanel.add(new JLabel("编号:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        idField = new JTextField(itemToEdit.getId(), 20);
        idField.setEditable(false);
        commonPanel.add(idField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        commonPanel.add(new JLabel("标题:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        titleField = new JTextField(itemToEdit.getTitle(), 20);
        commonPanel.add(titleField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        commonPanel.add(new JLabel("作者:"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        authorField = new JTextField(itemToEdit.getAuthor(), 20);
        commonPanel.add(authorField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        commonPanel.add(new JLabel("评级 (未评级/一般/成人/儿童):"), getGBC(0, row, GridBagConstraints.EAST, 0.0));
        ratingField = new JTextField(itemToEdit.getRating(), 20);
        commonPanel.add(ratingField, getGBC(1, row++, GridBagConstraints.WEST, 1.0));

        add(commonPanel, BorderLayout.NORTH);

        typeSpecificPanel = new JPanel(new CardLayout());
        createTypeSpecificPanels();
        typeSpecificPanel.add(bookPanel, Book.class.getSimpleName()); // Use Class.getSimpleName()
        typeSpecificPanel.add(picturePanel, Picture.class.getSimpleName());
        typeSpecificPanel.add(videoDiskPanel, VideoDisk.class.getSimpleName());
        add(typeSpecificPanel, BorderLayout.CENTER);

        populateAndShowTypeSpecificPanel(itemToEdit); // Calls local method

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        saveButton = new JButton("保存修改");
        cancelButton = new JButton("取消");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> performSaveEdit()); // Calls local method
        cancelButton.addActionListener(e -> dispose());

        pack();
        setLocationRelativeTo(parent);
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

    private void createTypeSpecificPanels() { /* ... (panel creation, no changes) ... */
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


    private void populateAndShowTypeSpecificPanel(media.Object item) { // Accepts media.Object
        CardLayout cl = (CardLayout)(typeSpecificPanel.getLayout());

        if (item instanceof Book) { // instanceof check
            Book book = (Book) item; // Cast
            publisherField.setText(book.getPublisher()); // Getter from Book
            isbnField.setText(book.getIsbn()); // Getter from Book
            pageCountField.setText(String.valueOf(book.getPageCount())); // Getter from Book, String.valueOf
            cl.show(typeSpecificPanel, Book.class.getSimpleName()); // Book.class.getSimpleName()
        } else if (item instanceof Picture) { // instanceof check
            Picture picture = (Picture) item; // Cast
            nationalityField.setText(picture.getNationality()); // Getter from Picture
            lengthField.setText(String.valueOf(picture.getLength())); // Getter from Picture
            widthField.setText(String.valueOf(picture.getWidth())); // Getter from Picture
            cl.show(typeSpecificPanel, Picture.class.getSimpleName()); // Picture.class.getSimpleName()
        } else if (item instanceof VideoDisk) { // instanceof check
            VideoDisk videoDisk = (VideoDisk) item; // Cast
            producerField.setText(videoDisk.getProducer()); // Getter from VideoDisk
            // Handle N/A date string
            if (videoDisk.getReleaseDate() != null && videoDisk.getReleaseDate().getLocalDate() != null) { // Getter from VideoDisk, Time methods
                releaseDateField.setText(videoDisk.getReleaseDate().toString()); // Time.toString()
            } else {
                releaseDateField.setText("N/A");
            }
            durationField.setText(String.valueOf(videoDisk.getDuration())); // Getter from VideoDisk
            cl.show(typeSpecificPanel, VideoDisk.class.getSimpleName()); // VideoDisk.class.getSimpleName()
        } else {
            JOptionPane.showMessageDialog(this, "未知物品类型，无法编辑。", "错误", JOptionPane.ERROR_MESSAGE);
            saveButton.setEnabled(false);
            cl.show(typeSpecificPanel, "");
        }
    }


    private void performSaveEdit() {
        String id = itemToEdit.getId(); // Getter from media.Object
        String title = titleField.getText().trim();
        String author = authorField.getText().trim();
        String rating = ratingField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || rating.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请填写所有必填的通用信息 (标题、作者、评级)。", "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        }

        media.Object updatedItemData = null; // media.Object

        try {
            if (itemToEdit instanceof Book) { // instanceof check
                String publisher = publisherField.getText().trim();
                String isbn = isbnField.getText().trim();
                int pageCount;
                try {
                    pageCount = Integer.parseInt(pageCountField.getText().trim());
                    if (pageCount < 0) throw new NumberFormatException("页数不能为负数。");
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("页数输入无效: " + e.getMessage());
                }
                if (publisher.isEmpty() || isbn.isEmpty()) throw new IllegalArgumentException("请填写图书的出版社和ISBN。");
                updatedItemData = new Book(id, title, author, rating, publisher, isbn, pageCount); // Book constructor

            } else if (itemToEdit instanceof Picture) { // instanceof check
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
                updatedItemData = new Picture(id, title, author, rating, nationality, length, width); // Picture constructor

            } else if (itemToEdit instanceof VideoDisk) { // instanceof check
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
                updatedItemData = new VideoDisk(id, title, author, rating, producer, releaseDate, duration); // VideoDisk constructor

            } else {
                JOptionPane.showMessageDialog(this, "未知的物品类型被编辑。", "内部错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, "输入错误: " + ex.getMessage(), "输入错误", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "处理编辑数据时发生未知错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return;
        }

        if (updatedItemData != null) {
            try {
                boolean success = libraryManager.editItemById(id, updatedItemData); // editItemById from LibraryManager
                if (success) {
                    JOptionPane.showMessageDialog(this, "物品信息更新成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    mainGUI.getLibraryDisplayPanel().refreshTable(); // Getters, refreshTable
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "更新失败: 未找到物品。", "更新失败", JOptionPane.WARNING_MESSAGE);
                }
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, "更新失败: " + ex.getMessage(), "更新失败", JOptionPane.WARNING_MESSAGE);
            } catch (IllegalStateException ex) {
                JOptionPane.showMessageDialog(this, "更新失败: " + ex.getMessage(), "更新失败", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "编辑物品时发生未知错误: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}