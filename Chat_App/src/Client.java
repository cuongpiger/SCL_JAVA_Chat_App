import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

class ClientListener extends Thread {
    private static Socket iSocket;
    private static ObjectInputStream iInStream;
    private static Client iClient;
    private static SigninUI iUI;

    public ClientListener(Socket pSocket, Client pClient, SigninUI pUI) throws IOException {
        iSocket = pSocket;
        iClient = pClient;
        iUI = pUI;
        iInStream = new ObjectInputStream(iSocket.getInputStream());
    }

    private Object receivePackage() {
        try {
            Package box = (Package) iInStream.readObject();
            return box;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void run() {
        try {
            Package box = (Package) receivePackage();

            if (box.getiService().equals("SIGN-IN")) {
                boolean checker = (boolean) box.getiContent();

                if (!checker) {
                    iUI.showDialog("Login unsuccessful!");
                    iSocket.close();
                } else {
                    iUI.setVisible(false);
                    iUI.showHomePage();

                    while (true) {
                        box = (Package) receivePackage();

                        if (box.getiService().equals("NEW-CLIENT")) {
                            ArrayList<String> clients = (ArrayList<String>) box.getiContent();
                            clients.remove(iClient.getiUsername());
                            iUI.updateHomepage(clients);
                        } else if (box.getiService().equals("CHATTING")) {
                            Message message = (Message) box.getiContent();
                            iUI.updateChatUI(message);
                        } else if (box.getiService().equals("CLOSE")) {
                            break;
                        }
                    }

                    iSocket.close();
                    System.exit(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Client extends Thread {
    private static HostInfo iServer;
    private static User iUser;
    private static ObjectOutputStream iOutStream;
    private static SigninUI iUI;
    private static ClientListener iListener;
    private static Socket iSocket;

    public Client(SigninUI pUI, User pUser) {
        iServer = Utils.loadHostInfo("./config/master.txt");
        iUI = pUI;
        iUser = pUser;
    }

    public String getiUsername() {
        return iUser.getiAccount();
    }

    public void run() {
        try {
            iSocket = new Socket(iServer.getiAddress(), iServer.getiPort());
            iOutStream = new ObjectOutputStream(iSocket.getOutputStream());
            sendPackage("SIGN-IN", iUser);
            iListener = new ClientListener(iSocket, this, iUI);
            iListener.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPackage(String pService, Object pContent) {
        Package box = new Package(pService, pContent);
        try {
            iOutStream.writeObject(box);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        JFrame login_frame = new SigninUI("Login");
        login_frame.setSize(300, 500);
        login_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login_frame.setLocationRelativeTo(null);
        login_frame.setVisible(true);
    }
}
