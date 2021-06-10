import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeUI extends JFrame implements ActionListener {
    private JPanel panel1;
    private JTextField vAccount;
    private JButton vChat;
    private JTextArea vClients;
    private static Map<String, ChatUI> iUIs;
    private static ArrayList<String> iActiveClients;
    private static Client iClient;

    public HomeUI(String pTitle, Client pClient) {
        super(pTitle);
        iClient = pClient;
        iUIs = new HashMap<String, ChatUI>();

        setContentPane(panel1);
        vClients.setEditable(false);
        vChat.addActionListener(this);
    }

    public void updateChatUI(Message pMessage) {
        ChatUI ui = iUIs.get(pMessage.getiFrom());

        if (ui == null) {
            ui = creatNewChatUI(pMessage.getiFrom());
            iUIs.put(pMessage.getiFrom(), ui);
        }

        ui.appendvContent(pMessage);
    }

    public void updateHomepage(ArrayList<String> pClients) {
        vClients.setText("");
        for (String client : pClients) {
            vClients.append(client + "\n");
        }

        iActiveClients = pClients;
    }

    private ChatUI creatNewChatUI(String pFriend) {
        ChatUI chat_ui = new ChatUI(pFriend, iClient);
        chat_ui.setSize(350, 500);
        chat_ui.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        chat_ui.setLocationRelativeTo(null);
        chat_ui.setVisible(true);

        chat_ui.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                iUIs.keySet().removeIf(key -> key == pFriend);
            }
        });

        return chat_ui;
    }

    public void actionPerformed(ActionEvent pEvent) {
        if (pEvent.getSource() == vChat) {
            String friend = vAccount.getText().strip();

            if (iActiveClients.contains(friend)) {
                iUIs.put(friend, creatNewChatUI(friend));
            }

            vAccount.setText("");
        }
    }
}
