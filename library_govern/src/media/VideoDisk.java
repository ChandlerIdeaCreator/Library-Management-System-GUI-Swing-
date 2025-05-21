package media; // <--- 添加包声明

import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class VideoDisk extends Object { // Extends Object from media package
    private String producer;
    private Time releaseDate; // Uses Time from media package
    private int duration;

    public VideoDisk(String id, String title, String author, String rating,
                     String producer, Time releaseDate, int duration) {
        super(id, title, author, rating);
        this.producer = producer;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    public VideoDisk() {
        super();
        this.releaseDate = new Time();
    }

    public String getProducer() { return producer; }
    public Time getReleaseDate() { return releaseDate; }
    public int getDuration() { return duration; }

    public void updateVideoDiskFields(String producer, Time releaseDate, int duration) {
        this.producer = producer;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }

    @Override
    public void input(Scanner scanner) { /* ... */ }

    @Override
    public String getDisplayString() {
        return String.format("视频光盘 [编号: %s, 标题: %s, 作者: %s, 评级: %s, 出品人: %s, 出品日期: %s, 时长: %d分钟, 查询次数: %d]",
                id, title, author, rating, producer, releaseDate != null ? releaseDate.toString() : "N/A", duration, queryCount);
    }

    @Override
    public void saveToFile(PrintWriter writer) {
        writer.print("VideoDisk|");
        saveCommonFields(writer);
        writer.print("|" + producer + "|");
        releaseDate.write(writer);
        writer.println("|" + duration);
    }

    @Override
    public void loadFromFile(Scanner scanner) {
        try {
            loadCommonFields(scanner);

            if (!scanner.hasNext()) throw new InputMismatchException("Missing Producer");
            this.producer = scanner.next();

            this.releaseDate = new Time();
            this.releaseDate.read(scanner);

            if (!scanner.hasNextInt()) throw new InputMismatchException("Missing or invalid Duration");
            this.duration = scanner.nextInt();

        } catch (InputMismatchException e) {
            throw new InputMismatchException("Error loading VideoDisk object: " + e.getMessage());
        }
    }
}