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
    private static Client iClient;
    private static String iFriend;

    public ChatUI(String pTitle, Client pClient) {
        super(pTitle);
        iClient = pClient;
        iFriend = pTitle;

        setContentPane(panel1);
        vContent.setEditable(false);
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        vChat.setBorder(BorderFactory.createCompoundBorder(border,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        vChat.requestFocus();
        vSend.addActionListener(this);
    }

    public void actionPerformed(ActionEvent pEvent) {
        if (pEvent.getSource() == vSend) {
            String content = vChat.getText();

            if (content.isEmpty() == false) {
                Message message = new Message(iClient.getiUsername(), iFriend, content);
                iClient.sendPackage("CHATTING", message);

                vChat.setText("");
                vContent.append("Me: " + content + "\n");
            }
        }
    }

    public void appendvContent(Message message) {
        vContent.append(message.getiFrom() + ": " + message.getiContent() + "\n");
    }
}
