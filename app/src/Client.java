import modules.LoginUI;

import javax.swing.*;

public class Client {
    public static void main(String[] args) {
        JFrame frame = new LoginUI("Login Page");
        frame.setSize(300, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
