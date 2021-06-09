package modules;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatUI extends JFrame implements ActionListener {
    private JPanel main_panel;
    private JTextArea vMes;
    private JButton vFile;
    private JTextArea vChat;
    private JTextField vAcc;
    private JButton vConn;
    private JButton vSend;

    public ChatUI() {
        super("Chat App");
        setContentPane(main_panel);
    }
    
    public void actionPerformed(ActionEvent pae) {
        
    }
}
