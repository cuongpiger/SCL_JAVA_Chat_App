package modules;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChatUI extends JFrame implements ActionListener {
    private JPanel main_panel;
    public JTextArea vChat;
    private JTextField vAcc;
    private JButton vConn;
    public ClientController client_controller = null;
    public static Map<String, ChatBoxUI> iFrames = null;

    public ChatUI() {
        super("Chat App");
        setContentPane(main_panel);
        vConn.addActionListener(this);
        iFrames = new HashMap<String, ChatBoxUI>();

        try {
            client_controller = new ClientController(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addChatBoxUI(Message mess) {
        var chatbox = createNewChatBox(mess.getIFrom());
        chatbox.vContent.append(mess.getIFrom() + ": " + mess.getiContent() + "\n");
        iFrames.put(mess.getIFrom(), chatbox);
        chatbox.setVisible(true);
    }

    private static ChatBoxUI createNewChatBox(String friend) {
        ChatBoxUI frame = new ChatBoxUI(friend);
        frame.setSize(400, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                iFrames.keySet().removeIf(key -> key == friend);
            }
        });

        return frame;
    }

    public void actionPerformed(ActionEvent pae) {
        if (pae.getSource() == vConn) {
            String friend = vAcc.getText();
            String[] friends = vChat.getText().split("\n");

            if (Arrays.asList(friends).contains(friend)) {

                for (String key : iFrames.keySet()) {
                    if (key.equals(friend)) {
                        return;
                    }
                }
                var new_frame = createNewChatBox(friend);
                iFrames.put(friend, new_frame);
                new_frame.setVisible(true);
            }
        }
    }
}