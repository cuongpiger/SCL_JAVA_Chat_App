import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ServerSignUp extends Thread {
    private static Server iServer = null;

    public ServerSignUp(Server pServer) {
        iServer = pServer;
    }

    public void run() {
        ServerSocket server_socket = null;
        try {
            server_socket = new ServerSocket(9900);
            System.out.println("SERVER SIGN-UP is running on PORT: " + 9900);
        } catch (IOException e) {
            e.printStackTrace();
        }

        do {
            Socket socket = null;
            try {
                socket = server_socket.accept();
                new SignUp(socket, iServer).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("SERVER SIGN-UP: new client connected");
        } while (true);
    }
}

class SignUp extends Thread {
    private static Server iServer;
    private Socket iSocket;
    private ObjectInputStream iInStream;
    private ObjectOutputStream iOutStream;

    public SignUp(Socket pSocket, Server pServer) throws IOException {
        iSocket = pSocket;
        iServer = pServer;
        iInStream = new ObjectInputStream(iSocket.getInputStream());
        iOutStream = new ObjectOutputStream(iSocket.getOutputStream());
    }

    private Object receivePackage() {
        try {
            Package box = (Package) iInStream.readObject();
            return box;
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public void sendPackage(String pService, Object pContent) {
        Package box = new Package(pService, pContent);
        try {
            iOutStream.writeObject(box);
        } catch (IOException err) {
            System.exit(1);
        }
    }

    public void run() {
        try {
            Package box = (Package) receivePackage();

            if (box.getiService().equals("SIGN-UP")) {
                User user = (User) box.getiContent();
                sendPackage("SIGN-UP", iServer.signup(user));
                iSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

public class Server {
    private static HostInfo iLocal = null;
    private static Map<String, String> iUsers = null;
    private static Map<String, ClientThread> iClients = null;

    public Server() {
        iLocal = Utils.loadHostInfo("./config/master.txt");
        iUsers = new HashMap<String, String>();
        iClients = new HashMap<String, ClientThread>();
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

    public void addNewClient(String pAccount, ClientThread pClient) {
        iClients.put(pAccount, pClient);
    }

    public void broadcast() {
        ArrayList<String> active_users = new ArrayList<String>(iClients.keySet());

        for (ClientThread client : iClients.values()) {
            client.sendPackage("NEW-CLIENT", active_users);
        }
    }

    public void sendMessage(String pService, Message pMessage) {
        ClientThread client = iClients.get(pMessage.getiTo());
        client.sendPackage(pService, pMessage);
    }

    public void execute() throws IOException {
        new FileServer(this).start();
        ServerSocket server_socket = null;
        try {
            server_socket = new ServerSocket(iLocal.getiPort());
            System.out.println("MASTER SERVER is running on PORT: " + iLocal.getiPort());
            new ServerSignUp(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        do {
            Socket socket = server_socket.accept();
            System.out.println("MASTER SERVER: new client connected");
            new ClientThread(socket, this).start();
        } while (true);
    }

    public void removeClient(String iAccount) {
        iClients.remove(iAccount);
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.execute();
    }
}
