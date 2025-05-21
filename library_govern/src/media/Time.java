package media; // <--- 添加包声明

import java.io.PrintWriter;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Time {
    private LocalDate date;

    public Time(int year, int month, int day) {
        try {
            this.date = LocalDate.of(year, month, day);
        } catch (java.time.DateTimeException e) {
            throw new IllegalArgumentException("Invalid date components: " + e.getMessage(), e);
        }
    }

    public Time(LocalDate date) {
        this.date = date;
    }

    public Time() {
        this.date = null;
    }

    public static Time parse(String dateString) throws IllegalArgumentException {
        if (dateString == null || dateString.trim().isEmpty() || "N/A".equalsIgnoreCase(dateString.trim())) {
            return new Time();
        }
        try {
            LocalDate parsedDate = LocalDate.parse(dateString.trim());
            return new Time(parsedDate);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format, expected YYYY-MM-DD: '" + dateString + "'", e);
        }
    }

    public LocalDate getLocalDate() {
        return date;
    }

    public void read(Scanner scanner) throws InputMismatchException {
        if (!scanner.hasNext()) {
            throw new InputMismatchException("Missing date string token");
        }
        String dateString = scanner.next();
        try {
            Time parsedTime = Time.parse(dateString);
            this.date = parsedTime.date;
        } catch (IllegalArgumentException e) {
            throw new InputMismatchException("Invalid date format received during load: '" + dateString + "' (" + e.getMessage() + ")");
        }
    }

    public void write(PrintWriter writer) {
        writer.print(this.toString());
    }

    @Override
    public String toString() {
        if (date == null) {
            return "N/A";
        }
        return date.toString();
    }

    public int getYear() { return date != null ? date.getYear() : 0; }
    public int getMonth() { return date != null ? date.getMonthValue() : 0; }
    public int getDay() { return date != null ? date.getDayOfMonth() : 0; }
}