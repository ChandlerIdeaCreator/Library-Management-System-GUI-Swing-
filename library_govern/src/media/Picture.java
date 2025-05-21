package media; // <--- 添加包声明

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Picture extends Object { // Extends Object from the same package (media)
    private String nationality;
    private int length;
    private int width;

    public Picture(String id, String title, String author, String rating,
                   String nationality, int length, int width) {
        super(id, title, author, rating);
        this.nationality = nationality;
        this.length = length;
        this.width = width;
    }

    public Picture() {
        super();
    }

    public String getNationality() { return nationality; }
    public int getLength() { return length; }
    public int getWidth() { return width; }

    public void updatePictureFields(String nationality, int length, int width) {
        this.nationality = nationality;
        this.length = length;
        this.width = width;
    }

    @Override
    public void input(Scanner scanner) { /* ... */ }

    @Override
    public String getDisplayString() {
        return String.format("图画 [编号: %s, 标题: %s, 作者: %s, 评级: %s, 国籍: %s, 长度: %d厘米, 宽度: %d厘米, 查询次数: %d]",
                id, title, author, rating, nationality, length, width, queryCount);
    }

    @Override
    public void saveToFile(PrintWriter writer) {
        writer.print("Picture|");
        saveCommonFields(writer);
        writer.println("|" + nationality + "|" + length + "|" + width);
    }

    @Override
    public void loadFromFile(Scanner scanner) {
        try {
            loadCommonFields(scanner);
            if (!scanner.hasNext()) throw new InputMismatchException("Missing Nationality");
            this.nationality = scanner.next();
            if (!scanner.hasNextInt()) throw new InputMismatchException("Missing or invalid Length");
            this.length = scanner.nextInt();
            if (!scanner.hasNextInt()) throw new InputMismatchException("Missing or invalid Width");
            this.width = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Error loading Picture object: " + e.getMessage());
        }
    }
}