package modules;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

class Client implements Runnable {
    Thread iThread = null;
    String iAction = null;
    LoginUI iUI = null;
    Socket iSocket = null;
    ObjectOutputStream iOutStream = null;
    ObjectInputStream iInStream = null;

    Client(String pAction, LoginUI pUI) throws IOException {
        iAction = pAction;
        iUI = pUI;
        iSocket = new Socket(iUI.iHost.getiAddress(), iUI.iHost.getiPort());
        iThread = new Thread(this);
        iThread.start();
    }

    private void sendPackage(String pMessage, Object pContent) {
        Package box = new Package("CLIENT", pMessage, pContent);
        try {
            iOutStream = new ObjectOutputStream(iSocket.getOutputStream());
            iOutStream.writeObject(box);
        } catch (IOException err) {
            System.exit(1);
        }
    }

    private Package receivePackage() {
        try {
            iInStream = new ObjectInputStream(iSocket.getInputStream());
            Package box = (Package) iInStream.readObject();
            return box;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void run() {
        if (iAction.equals("SIGN-UP")) {
            sendPackage("SIGN-UP", iUI.userSignup());
            String response = receivePackage().getiMessage();

            if (response.equals("1")) {
                iUI.vButton.setText("Sign in");
                // iUI.vAcc.setText("");
                // iUI.vPass.setText("");
                iUI.showDialog("Sign up success.");
            } else {
                iUI.showDialog("Registration failed!");
            }
        } else if (iAction.equals("SIGN-IN")) {
            iUI.iUser = iUI.userSignup();
            sendPackage("SIGN-IN", iUI.iUser);
            String response = receivePackage().getiMessage();

            if (response.equals("1")) {
                iUI.setVisible(false);
                iUI.showChatWindow();
            } else {
                iUI.showDialog("Login failed!");
            }
        }

        try {
            iSocket.close();
        } catch (IOException e) {
            System.exit(1);
        }
    }
}

public class LoginUI extends JFrame implements ActionListener {
    private JPanel vPanel;
    public JTextField vAcc;
    public JPasswordField vPass;
    public JButton vButton;
    public static HostInfo iHost = null;
    public static User iUser = null;

    public static void showChatWindow() {
        ChatUI chat_window = new ChatUI();
        chat_window.setSize(400, 600);
        chat_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat_window.setLocationRelativeTo(null);
        chat_window.setVisible(true);
    }

    public void showDialog(String pText) {
        JOptionPane.showMessageDialog(this, pText);
    }

    public LoginUI(String pTitle) {
        super(pTitle);
        setContentPane(vPanel);
        vButton.addActionListener(this);

        iHost = Utils.loadHostInfo("./config/master.txt");
    }

    public User userSignup() {
        String account = vAcc.getText().strip();
        String password = new String(vPass.getPassword());

        return new User(account, password);
    }

    public void actionPerformed(ActionEvent pae) {
        if (pae.getSource() == vButton) {
            try {
                if (vButton.getText().equals("Sign up")) {
                    new Client("SIGN-UP", this);
                } else {
                    new Client("SIGN-IN", this);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
