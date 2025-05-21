package media; // <--- 添加包声明

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Book extends Object { // Extends Object from the same package (media)
    private String publisher;
    private String isbn;
    private int pageCount;

    public Book(String id, String title, String author, String rating,
                String publisher, String isbn, int pageCount) {
        super(id, title, author, rating);
        this.publisher = publisher;
        this.isbn = isbn;
        this.pageCount = pageCount;
    }

    public Book() {
        super();
    }

    public String getPublisher() { return publisher; }
    public String getIsbn() { return isbn; }
    public int getPageCount() { return pageCount; }

    public void updateBookFields(String publisher, String isbn, int pageCount) {
        this.publisher = publisher;
        this.isbn = isbn;
        this.pageCount = pageCount;
    }

    @Override
    public void input(Scanner scanner) { /* ... */ }

    @Override
    public String getDisplayString() {
        return String.format("图书 [编号: %s, 标题: %s, 作者: %s, 评级: %s, 出版社: %s, ISBN: %s, 页数: %d, 查询次数: %d]",
                id, title, author, rating, publisher, isbn, pageCount, queryCount);
    }

    @Override
    public void saveToFile(PrintWriter writer) {
        writer.print("Book|");
        saveCommonFields(writer);
        writer.println("|" + publisher + "|" + isbn + "|" + pageCount);
    }

    @Override
    public void loadFromFile(Scanner scanner) {
        try {
            loadCommonFields(scanner);
            if (!scanner.hasNext()) throw new InputMismatchException("Missing Publisher");
            this.publisher = scanner.next();
            if (!scanner.hasNext()) throw new InputMismatchException("Missing ISBN");
            this.isbn = scanner.next();
            if (!scanner.hasNextInt()) throw new InputMismatchException("Missing or invalid PageCount");
            this.pageCount = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Error loading Book object: " + e.getMessage());
        }
    }
}