import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
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
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        vChat.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        vChat.requestFocus();
    }

    public void actionPerformed(ActionEvent pEvent) {

    }
}
