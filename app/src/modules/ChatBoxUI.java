package modules;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ChatBoxUI extends JFrame implements ActionListener {
    private JPanel panel1;
    public JTextArea vContent;
    public JTextArea vText;
    public JButton vSend;
    public JButton vFile;

    public JTextArea getvContent() {
        return vContent;
    }

    public ChatBoxUI(String pTitle) {
        super(pTitle);
        setContentPane(panel1);
    }

    public void actionPerformed(ActionEvent pae) {

    }

}
