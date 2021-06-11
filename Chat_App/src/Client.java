import javax.swing.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Map;

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
        while (true) {
            try {
                Package box = (Package) receivePackage();

                if (box.getiService().equals("SIGN-UP")) {
                    boolean checker = (boolean) box.getiContent();
                    String alert = checker ? "Sign up success." : "Registration failed!";
                    iUI.showDialog(alert);
                } else if (box.getiService().equals("SIGN-IN")) {
                    boolean checker = (boolean) box.getiContent();

                    if (!checker) {
                        iUI.showDialog("Login unsuccessful!");
                    } else {
                        iUI.setVisible(false);
                        iUI.showHomePage();
                    }
                } else if (box.getiService().equals("NEW-CLIENT")) {
                    ArrayList<String> clients = (ArrayList<String>) box.getiContent();
                    clients.remove(iClient.getiUsername());
                    iUI.updateHomepage(clients);
                } else if (box.getiService().equals("CHATTING")) {
                    Message message = (Message) box.getiContent();
                    iUI.updateChatUI(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

public class Client extends Thread {
    private static HostInfo iServer;
    private static String iUsername;
    private static ObjectOutputStream iOutStream;
    private static SigninUI iUI;

    public Client(SigninUI pUI) {
        iServer = Utils.loadHostInfo("./config/master.txt");
        iUI = pUI;
    }

    public void setiUsername(String pUsername) {
        iUsername = pUsername;
    }

    public String getiUsername() {
        return iUsername;
    }

    public void run() {
        try {
            Socket socket = new Socket(iServer.getiAddress(), iServer.getiPort());
            iOutStream = new ObjectOutputStream(socket.getOutputStream());
            new ClientListener(socket, this, iUI).start();
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
