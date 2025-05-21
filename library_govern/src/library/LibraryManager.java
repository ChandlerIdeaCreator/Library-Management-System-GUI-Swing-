package library; // <--- 添加包声明

import media.Book;
import media.Picture;
import media.VideoDisk;
import media.Time; //
import media.Object; //
import user.UserManager;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


public class LibraryManager { // This class is in the 'library' package
    private List<media.Object> library; // Use media.Object explicitly, List is in java.util
    private static final String FILE_NAME = "library.txt";

    private UserManager userManager; // UserManager is in the user package

    public LibraryManager(UserManager userManager) {
        this.userManager = userManager;
        library = new ArrayList<>(); // ArrayList is in java.util
        loadFromFile();
    }

    public void addItem(media.Object item) throws IllegalArgumentException, IllegalStateException { // Use media.Object
        if (item == null) {
            throw new IllegalArgumentException("无法添加空物品。");
        }
        if (item.getId() == null || item.getId().trim().isEmpty()) {
            throw new IllegalArgumentException("物品编号不能为空。");
        }
        String itemId = item.getId().trim();

        if (findItemById(itemId).isPresent()) { // Optional is in java.util
            throw new IllegalArgumentException("编号 " + itemId + " 已存在，无法添加重复物品。");
        }

        library.add(item);
        sortByQueryCount();
        System.out.println("控制台: 物品添加成功！");
    }

    public List<media.Object> getItems(int typeFilter) { // Return List<media.Object>, List is in java.util
        List<media.Object> filteredList; // Use media.Object, List is in java.util
        switch (typeFilter) {
            case 1: filteredList = library.stream().filter(item -> item instanceof Book).collect(Collectors.toList()); break; // Book is in media package, Collectors is in java.util.stream
            case 2: filteredList = library.stream().filter(item -> item instanceof Picture).collect(Collectors.toList()); break; // Picture is in media package
            case 3: filteredList = library.stream().filter(item -> item instanceof VideoDisk).collect(Collectors.toList()); break; // VideoDisk is in media package
            default: filteredList = new ArrayList<>(library); break; // ArrayList is in java.util
        }
        return filteredList;
    }

    private void sortByQueryCount() {
        library.sort(Comparator.comparingInt(media.Object::getQueryCount).reversed()); // Comparator is in java.util, media.Object
    }

    public void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) { // PrintWriter, FileWriter is in java.io
            for (media.Object item : library) { // media.Object
                item.saveToFile(writer); // saveToFile is defined in media.Object subclasses
            }
            System.out.println("控制台: 恭喜！保存成功！ 文件: " + FILE_NAME);
        } catch (IOException e) { // IOException is in java.io
            throw new RuntimeException("保存文件时出错: " + FILE_NAME, e);
        }
    }

    public void loadFromFile() {
        library.clear();

        File file = new File(FILE_NAME); // File is in java.io
        if (!file.exists()) {
            System.out.println("控制台: 对不起，没有找到文件: " + FILE_NAME + ". 新建媒体库。");
            return;
        }

        try (Scanner scanner = new Scanner(file)) { // Scanner is in java.util
            scanner.useDelimiter("[\\|\\r\\n]+");

            int lineNumber = 0;
            while (scanner.hasNext()) {
                lineNumber++;
                String type = null;
                media.Object obj = null; // Use media.Object
                String itemId = "Unknown";

                try {
                    if (!scanner.hasNext()) {
                        System.err.println("控制台错误: 文件在行 " + lineNumber + " 意外结束或不完整。");
                        break;
                    }
                    type = scanner.next();

                    switch (type) {
                        case "Book": obj = new Book(); break; // Book is in media package
                        case "VideoDisk": obj = new VideoDisk(); break; // VideoDisk is in media package
                        case "Picture": obj = new Picture(); break; // Picture is in media package
                        default:
                            System.err.println("控制台错误: 在行 " + lineNumber + " 发现未知物品类型: '" + type + "'. 跳过此项。");
                            while(scanner.hasNext() && !scanner.hasNext("Book|") && !scanner.hasNext("Picture|") && !scanner.hasNext("VideoDisk|")) {
                                scanner.next();
                            }
                            continue;
                    }

                    obj.loadFromFile(scanner); // loadFromFile is defined in media.Object subclasses
                    itemId = obj.getId();

                    if (findItemById(itemId).isPresent()) { // Optional is in java.util
                        System.err.println("控制台错误: 在行 " + lineNumber + ", 发现重复的物品编号 '" + itemId + "'. 跳过此项。");
                        while(scanner.hasNext() && !scanner.hasNext("Book|") && !scanner.hasNext("Picture|") && !scanner.hasNext("VideoDisk|")) {
                            scanner.next();
                        }
                    } else {
                        library.add(obj);
                    }

                } catch (InputMismatchException e) { // InputMismatchException is in java.util
                    System.err.println("控制台错误: 在行 " + lineNumber + ", 读取类型 '" + type + "' 的物品时发生数据格式错误 (ID '" + itemId + "'): " + e.getMessage());
                    while(scanner.hasNext() && !scanner.hasNext("Book|") && !scanner.hasNext("Picture|") && !scanner.hasNext("VideoDisk|")) {
                        scanner.next();
                    }
                } catch (NoSuchElementException e) { // NoSuchElementException is in java.util
                    System.err.println("控制台错误: 在行 " + lineNumber + ", 读取类型 '" + type + "' 的物品时发生数据缺失错误 (ID '" + itemId + "'): " + e.getMessage());
                    while(scanner.hasNext() && !scanner.hasNext("Book|") && !scanner.hasNext("Picture|") && !scanner.hasNext("VideoDisk|")) {
                        scanner.next();
                    }
                } catch (Exception e) { // Exception is in java.lang
                    System.err.println("控制台错误: 在行 " + lineNumber + ", 加载类型 '" + type + "' 的物品时发生未知错误 (ID '" + itemId + "'): " + e.getMessage());
                    e.printStackTrace();
                    while(scanner.hasNext() && !scanner.hasNext("Book|") && !scanner.hasNext("Picture|") && !scanner.hasNext("VideoDisk|")) {
                        scanner.next();
                    }
                }
            }
            System.out.println("控制台: 恭喜！加载成功！ 文件: " + FILE_NAME + ", 载入物品数量: " + library.size());
            sortByQueryCount();
        } catch (FileNotFoundException e) { // FileNotFoundException is in java.io
            System.out.println("控制台: 对不起，没有找到文件: " + FILE_NAME + ". (已在前面处理)");
        } catch (Exception e) { // Catch any other exception during setup/file open
            throw new RuntimeException("从文件载入时发生致命错误: " + FILE_NAME + " (" + e.getMessage() + ")", e);
        }
    }

    public boolean deleteItemById(String id) {
        if (id == null || id.trim().isEmpty()) {
            System.out.println("控制台: 删除物品时编号不能为空！");
            return false;
        }
        String itemId = id.trim();

        boolean removed = library.removeIf(item -> item.getId().equals(itemId));

        if (removed) {
            System.out.println("控制台: 恭喜！物品 " + itemId + " 删除成功！");
        } else {
            System.out.println("控制台: sorry!未找到编号为 " + itemId + " 的物品！");
        }
        return removed;
    }

    public Optional<media.Object> findItemById(String id) { // Return Optional<media.Object>, Optional is in java.util
        if (id == null || id.trim().isEmpty()) return Optional.empty();
        return library.stream()
                .filter(item -> item.getId().equals(id.trim()))
                .findFirst();
    }

    public Optional<media.Object> findItemByTitle(String title) { // Return Optional<media.Object>, Optional is in java.util
        if (title == null || title.trim().isEmpty()) return Optional.empty();
        return library.stream()
                .filter(item -> item.getTitle().equals(title.trim()))
                .findFirst();
    }

    public boolean editItemById(String id, media.Object updatedItemData) throws IllegalArgumentException, IllegalStateException { // Accept media.Object
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("物品编号不能为空。");
        }
        String itemId = id.trim();
        if (updatedItemData == null) {
            throw new IllegalArgumentException("更新数据不能为null。");
        }

        Optional<media.Object> itemOpt = findItemById(itemId); // Use media.Object

        if (itemOpt.isPresent()) {
            media.Object existingItem = itemOpt.get(); // Use media.Object

            if (!existingItem.getClass().equals(updatedItemData.getClass())) {
                throw new IllegalArgumentException("错误：无法将物品类型从 " + existingItem.getClass().getSimpleName() + " 修改为 " + updatedItemData.getClass().getSimpleName());
            }

            existingItem.updateCommonFields(updatedItemData.getTitle(), updatedItemData.getAuthor(), updatedItemData.getRating());

            if (existingItem instanceof Book && updatedItemData instanceof Book) { // Book is in media package
                Book existingBook = (Book) existingItem;
                Book updatedBook = (Book) updatedItemData;
                existingBook.updateBookFields(updatedBook.getPublisher(), updatedBook.getIsbn(), updatedBook.getPageCount());
            } else if (existingItem instanceof Picture && updatedItemData instanceof Picture) { // Picture is in media package
                Picture existingPicture = (Picture) existingItem;
                Picture updatedPicture = (Picture) updatedItemData;
                existingPicture.updatePictureFields(updatedPicture.getNationality(), updatedPicture.getLength(), updatedPicture.getWidth());
            } else if (existingItem instanceof VideoDisk && updatedItemData instanceof VideoDisk) { // VideoDisk is in media package
                VideoDisk existingVideoDisk = (VideoDisk) existingItem;
                VideoDisk updatedVideoDisk = (VideoDisk) updatedItemData;
                existingVideoDisk.updateVideoDiskFields(updatedVideoDisk.getProducer(), updatedVideoDisk.getReleaseDate(), updatedVideoDisk.getDuration());
            }

            System.out.println("控制台: nice!物品信息更新成功！ ID: " + itemId);
            return true;
        } else {
            System.out.println("控制台: 实在抱歉！未找到编号为 " + itemId + " 的物品！");
            return false;
        }
    }

    public Optional<media.Object> queryItem(String query, boolean byId) { // Accept and Return Optional<media.Object>
        if (query == null || query.trim().isEmpty()) {
            System.out.println("控制台: 查询内容不能为空。");
            return Optional.empty();
        }
        String trimmedQuery = query.trim();

        Optional<media.Object> foundItem;
        if (byId) {
            foundItem = findItemById(trimmedQuery);
        } else {
            foundItem = findItemByTitle(trimmedQuery);
        }

        if (foundItem.isPresent()) {
            foundItem.get().incrementQueryCount();
            sortByQueryCount();
            System.out.println("控制台: 查询成功！找到物品: " + foundItem.get().getId());
        } else {
            System.out.println("控制台: 抱歉了哈！未能找到相关物品！ 查询: " + trimmedQuery);
        }
        return foundItem;
    }

    public int getItemCount() {
        return library.size();
    }
}