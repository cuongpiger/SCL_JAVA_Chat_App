import modules.HostInfo;
import modules.User;
import modules.Utils;
import modules.Package;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


class ServerController implements Runnable {
    Socket iSocket = null;
    ObjectInputStream iInStream = null;
    ObjectOutputStream iOutStream = null;
    Thread iThread = null;
    Map iClients = null;

    ServerController(Socket pSocket, Map pClients) {
        iSocket = pSocket;
        iClients = pClients;

        try {
            iInStream = new ObjectInputStream(iSocket.getInputStream());
            iOutStream = new ObjectOutputStream(iSocket.getOutputStream());
            iThread = new Thread(this);
            iThread.start();
            iThread.join();
        } catch (InterruptedException | IOException err) {
            System.exit(1);
        }
    }

    public void run() {
        try {
            Package box = (Package) iInStream.readObject();

            if (box.getiMessage().equals("SIGN-UP")) {
                User user = (User) box.getiContent();

                synchronized (iClients) {
                    if (iClients.containsKey(user.getiAccount())) {
                        sendPackage("0", null);
                    } else {
                        iClients.put(user.getiAccount(), user.getiPassword());
                        sendPackage("1", null);
                    }
                }
            } else if (box.getiMessage().equals("SIGN-IN")) {
                User user = (User) box.getiContent();

                synchronized (iClients) {
                    if (iClients.containsKey(user.getiAccount())) {
                        if (iClients.get(user.getiAccount()).equals(user.getiPassword())) {
                            sendPackage("1", null);
                        } else {
                            sendPackage("1", null);
                        }
                    } else {
                        sendPackage("0", null);
                    }
                }
            } else if (box.getiMessage().equals("LOAD-CLIENTS")) {
                ArrayList<String> clients = new ArrayList<String>();

                for (var key : iClients.keySet()) {
                    clients.add((String) key);
                }

                sendPackage("UPDATE-CLIENTS", clients);
            }
        } catch (IOException | ClassNotFoundException err) {
            System.exit(1);
        }
    }

    private void sendPackage(String pMessage, Object pContent) {
        Package box = new Package("SERVER", pMessage, pContent);
        try {
            iOutStream.writeObject(box);
        } catch (IOException err) {
            System.exit(1);
        }
    }

    private Object receivePackage() {
        try {
            Package box = (Package) iInStream.readObject();
            return box.getiContent();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}

public class Server {
    private static ServerSocket iSocket = null;
    private static HostInfo iServer = null;
    private static Map<String, String> iClients = null;

    public static void main(String[] args) {
        iServer = Utils.loadHostInfo("./config/master.txt");
        iClients = new HashMap<String, String>();
        User cuongpiger = new User("cuongpiger", "Cuong*0902902209");
        iClients.put(cuongpiger.getiAccount(), cuongpiger.getiPassword());

        try {
            iSocket = new ServerSocket(iServer.getiPort());
            System.out.println(">> Server is running on PORT: " + iServer.getiPort());
        } catch (IOException err) {
            System.out.println(">> Unable to set up PORT: " + iServer.getiPort());
            System.exit(1);
        }

        do {
            try {
                Socket client = iSocket.accept();
                new ServerController(client, iClients);
            } catch (IOException err) {

            }
        } while (true);
    }
}
