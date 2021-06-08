import javax.swing.*;

public class App {
    public static void main(String[] args) {
        showWindow();
    }

    public static void showWindow() {
        JFrame frame = new MainWindowUI("Main Window");
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
