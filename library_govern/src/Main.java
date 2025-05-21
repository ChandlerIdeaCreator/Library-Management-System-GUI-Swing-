// Main.java is in the src root directory, so it has no package declaration

import gui.LibraryAppGUI;
import library.LibraryManager;
import user.UserManager;

import javax.swing.*;

public class Main { // Main class
    public static void main(String[] args) { // main method
        // Run the GUI creation on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> { // SwingUtilities from javax.swing, lambda expression
            UserManager userManager = new UserManager(); // UserManager constructor
            // Correct constructor call: Pass the userManager instance
            LibraryManager libraryManager = new LibraryManager(userManager); // LibraryManager constructor

            LibraryAppGUI gui = new LibraryAppGUI(userManager, libraryManager); // LibraryAppGUI constructor
            gui.setVisible(true); // LibraryAppGUI method (inherits from JFrame)

            // Add a shutdown hook to save data on exit
            Runtime.getRuntime().addShutdownHook(new Thread(() -> { // Runtime, Thread from java.lang
                System.out.println("控制台: JVM 正在关闭，保存媒体库数据..."); // System.out from java.lang
                try {
                    libraryManager.saveToFile(); // saveToFile from LibraryManager
                    System.out.println("控制台: 保存完成。");
                } catch (RuntimeException e) { // RuntimeException from java.lang
                    System.err.println("控制台错误: 关闭时保存文件失败: " + e.getMessage()); // System.err, Exception method
                    e.printStackTrace(); // Exception method
                }
            }));
        });
    }
}