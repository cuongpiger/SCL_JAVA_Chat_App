package modules;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginUI extends JFrame implements ActionListener {
    private JPanel vPanel;
    private JTextField vAcc;
    private JTextField vPass;
    private JButton vButton;
    private static Socket iServer = null;
    private static HostInfo iHost = null;
    private static ObjectOutputStream iOutStream


    private static void showChatWindow() {
        ChatUI chat_window = new ChatUI();
        chat_window.setSize(400, 600);
        chat_window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chat_window.setLocationRelativeTo(null);
        chat_window.setVisible(true);
    }

    private void showDialog(String pText) {
        JOptionPane.showMessageDialog(this, pText);
    }

    public LoginUI(String pTitle) {
        super(pTitle);
        setContentPane(vPanel);
        vButton.addActionListener(this);
        iHost = Utils.loadHostInfo("./config/master.txt");
    }

    public void actionPerformed(ActionEvent pae) {
        if (pae.getSource() == vButton) {
            if (vButton.getText().equals("Sign up")) {
//                vButton.setText("Sign in");
                vAcc.setText("");
                vPass.setText("");
                vAcc.grabFocus();
                showDialog("Sign up success");
            } else if (vButton.getText().equals("Sign in")) {
                setVisible(false);
                showChatWindow();
            }
        }
    }

    public void signUp() {
        try {
            iServer = new Socket(iHost.getiAddress(), iHost.getiPort());

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
