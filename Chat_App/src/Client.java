import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
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
                System.out.println("get a response");

                if (box.getiService().equals("SIGN-UP")) {
                    boolean checker = (boolean) box.getiContent();
                    String alert = checker ? "Dang ki thanh cong" : "Dang ki ko thanh cong";
                    iUI.showDialog(alert);
                } else if (box.getiService().equals("SIGN-IN")) {
                    boolean checker = (boolean) box.getiContent();

                    if (!checker) {
                        iUI.showDialog("Dang nhap ko thanh cong");
                    } else {
                        iUI.setVisible(false);
                        iClient.showHomePage();
                    }
                } else if (box.getiService().equals("NEW-CLIENT")) {
                    ArrayList<String> clients = (ArrayList<String>) box.getiContent();
                    clients.remove(iClient.getiUsername());
                    iClient.updateHomepage(clients);
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
    private static HomeUI iHomeUI;
    private static Map<String, ChatUI> iChatUIs;

    public Client(SigninUI pUI) {
        iServer = Utils.loadHostInfo("./config/master.txt");
        iUI = pUI;
    }

    public void showHomePage() {
        iChatUIs = new HashMap<String, ChatUI>();

        iHomeUI = new HomeUI("Home", iChatUIs);
        iHomeUI.setSize(350, 500);
        iHomeUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        iHomeUI.setLocationRelativeTo(null);
        iHomeUI.setVisible(true);
    }

    public void updateHomepage(ArrayList<String> pClients) {
        iHomeUI.updateHomepage(pClients);
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
            System.out.println("Connected to the server.");
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
