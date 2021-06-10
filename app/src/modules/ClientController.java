package modules;

import modules.HostInfo;
import modules.Package;
import modules.Utils;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientController implements Runnable {
    public static ObjectOutputStream iOutStream = null;
    public static ObjectInputStream iInStream = null;
    public static Socket iSocket = null;
    public static Thread iThread = null;
    public static HostInfo iHost = null;
    public static ChatUI iUI = null;

    public ClientController(ChatUI pUI) throws IOException {
        iHost = Utils.loadHostInfo("./config/master.txt");
        iUI = pUI;
        iSocket = new Socket(iHost.getiAddress(), iHost.getiPort());
        iOutStream = new ObjectOutputStream(iSocket.getOutputStream());
        iInStream = new ObjectInputStream(iSocket.getInputStream());
        iThread = new Thread(this);
        iThread.start();

        sendPackage("KEEP-CONNECT", LoginUI.iUser);
        sendPackage("KEEP-CONNECT", LoginUI.iUser);

    }

    public void run() {
        while (true) {
            Package box = (Package) receivePackage();

            if (box.getiMessage().equals("UPDATE-CLIENTS")) {
                iUI.vChat.setText("");

                for (var client : (ArrayList<String>) box.getiContent()) {
                    if (LoginUI.iUser.getiAccount().equals(client)) {
                        continue;
                    }
                    iUI.vChat.append(client + "\n");
                }
            } else if (box.getiMessage().equals("RECEIVE-MESSAGE")) {
                Message mess = (Message) box.getiContent();

                for (var friend : ChatUI.iFrames.keySet()) {
                    if (friend.equals(mess.getIFrom())) {
                        ChatUI.iFrames.get(friend).vContent.append(mess.getIFrom() + ": " + mess.getiContent() + "\n");
                        return;
                    }
                }

                ChatUI.addChatBoxUI(mess);
            }
        }
    }

    public static void sendPackage(String pMessage, Object pContent) {
        Package box = new Package("CLIENT", pMessage, pContent);
        try {
            iOutStream.writeObject(box);
        } catch (IOException err) {
            err.printStackTrace();
            System.exit(1);
        }
    }

    private Package receivePackage() {
        try {
            Package box = (Package) iInStream.readObject();
            return box;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}