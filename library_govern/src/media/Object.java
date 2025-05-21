package media; // <--- 添加包声明

import java.io.PrintWriter;
import java.util.Scanner;
import java.util.InputMismatchException;

public abstract class Object {
    protected String id;
    protected String title;
    protected String author;
    protected String rating;
    protected int queryCount = 0;

    public Object(String id, String title, String author, String rating) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    public Object() {}

    public abstract void input(Scanner scanner);
    public abstract String getDisplayString();
    public abstract void saveToFile(PrintWriter writer);
    public abstract void loadFromFile(Scanner scanner);

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getRating() { return rating; }
    public int getQueryCount() { return queryCount; }

    public void updateCommonFields(String title, String author, String rating) {
        this.title = title;
        this.author = author;
        this.rating = rating;
    }

    public void incrementQueryCount() {
        queryCount++;
    }

    protected void saveCommonFields(PrintWriter writer) {
        writer.print(id + "|" + title + "|" + author + "|" + rating + "|" + queryCount);
    }

    protected void loadCommonFields(Scanner scanner) {
        try {
            if (!scanner.hasNext()) throw new InputMismatchException("Missing ID");
            this.id = scanner.next();
            if (!scanner.hasNext()) throw new InputMismatchException("Missing Title");
            this.title = scanner.next();
            if (!scanner.hasNext()) throw new InputMismatchException("Missing Author");
            this.author = scanner.next();
            if (!scanner.hasNext()) throw new InputMismatchException("Missing Rating");
            this.rating = scanner.next();
            if (!scanner.hasNextInt()) throw new InputMismatchException("Missing or invalid QueryCount");
            this.queryCount = scanner.nextInt();
        } catch (InputMismatchException e) {
            throw new InputMismatchException("Error loading common fields: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return getDisplayString();
    }
}