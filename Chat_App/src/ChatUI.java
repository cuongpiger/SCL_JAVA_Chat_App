import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

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
        vFile.addActionListener(this);
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

        if (pEvent.getSource() == vFile) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String file_path = selectedFile.getAbsolutePath();
                new Shipper(iClient.getiUsername(), iFriend, file_path).start();

                vContent.append("Me: [FILE] " + selectedFile.getName() + "\n");
            }
        }
    }

    public void appendvContent(Message message) {
        vContent.append(message.getiFrom() + ": " + message.getiContent() + "\n");
    }
}
