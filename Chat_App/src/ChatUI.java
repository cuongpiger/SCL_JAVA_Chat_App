import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatUI extends JFrame implements ActionListener {
    private JPanel panel1;
    private JTextArea vContent;
    private JButton vSend;
    private JButton vFile;
    private JTextArea vChat;

    public ChatUI(String pTitle) {
        super(pTitle);
        setContentPane(panel1);
        vContent.setEditable(false);
    }

    public void actionPerformed(ActionEvent pEvent) {

    }
}
