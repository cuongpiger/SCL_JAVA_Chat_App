import modules.HostInfo;
import modules.User;
import modules.Utils;
import modules.Package;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

class LoginController implements Runnable {
    Socket iSocket = null;
    ObjectInputStream pInStream = null;
    Thread iThread = null;


    public void run() {

    }
}

class ServerController implements Runnable {
    Socket iSocket = null;
    ObjectInputStream iInStream = null;
    PrintWriter iOutStream = null;
    Thread iThread = null;
    Map iClients = null;

    ServerController(Socket pSocket, Map pClients) {
        iSocket = pSocket;
        iClients = pClients;

        try {
            iInStream = new ObjectInputStream(iSocket.getInputStream());
            iOutStream = new PrintWriter(iSocket.getOutputStream(), true);
            iThread = new Thread(this);
            iThread.start();
            iThread.join();
        } catch (IOException err) {
            System.out.println(err);
            System.exit(1);
        } catch (InterruptedException err) {
            System.out.println(err);
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
                        iOutStream.println(0);
                    } else {
                        iClients.put(user.getiAccount(), user.getiPassword());
                        iOutStream.println(1);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException err) {
            System.out.println(err);
            System.exit(1);
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
                ServerController server_controller = new ServerController(client, iClients);
            } catch (IOException err) {

            }
        } while (true);
    }
}
