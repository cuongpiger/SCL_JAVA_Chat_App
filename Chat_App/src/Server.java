import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Server {
    private static HostInfo iLocal = null;
    private static Map<String, String> iUsers = null;
    private static Map<String, ClientThread> iClients = null;

    public Server() {
        iLocal = Utils.loadHostInfo("./config/master.txt");
        iUsers = new HashMap<String, String>();

    }

    public boolean signup(User pNewUser) {
        if (iUsers.containsKey(pNewUser.getiAccount())) {
            return false;
        } else {
            iUsers.put(pNewUser.getiAccount(), pNewUser.getiPassword());
            return true;
        }
    }

    public boolean signin(User pUser) {
        if (iUsers.containsKey(pUser.getiAccount())) {
            String correct_password = iUsers.get(pUser.getiAccount());
            return correct_password.equals(pUser.getiPassword());
        }

        return false;
    }

    public void broadcast() {
        List<String> active_users = new ArrayList<String>(iClients.keySet());

        for (ClientThread client : iClients.values()) {
            client.sendPackage("NEW-CLIENT", active_users);
        }
    }

    public void sendMessage(Message pMessage) {
        ClientThread client = iClients.get(pMessage.getiTo());
        client.sendPackage("SEND-MESSAGE", pMessage);
    }

    public void execute() {
        try (ServerSocket server_socket = new ServerSocket(iLocal.getiPort())) {
            System.out.println("Server is running on PORT: " + iLocal.getiPort());

            while (true) {
                Socket socket = server_socket.accept();
                System.out.println("New client connected");


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(String iAccount) {
        iClients.remove(iAccount);
    }

    public static void main(String[] args) {

    }
}
