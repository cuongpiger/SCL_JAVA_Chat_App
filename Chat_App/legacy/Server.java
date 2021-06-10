import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class FileServer extends Thread {
    public static final int PIECE = 1024 * 32;
    private static DatagramSocket iServer;
    private static HostInfo iHost;
    private static DatagramPacket iInPacket;

    public FileServer(String pAddress, int pPort) throws SocketException {
        iHost = new HostInfo(pAddress, pPort);
        iServer = new DatagramSocket(pPort);
    }

    public void run() {
        while (true) {
            byte[] buffer = new byte[PIECE];

            try {
                iInPacket = new DatagramPacket(buffer, buffer.length);
                iServer.receive(iInPacket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class FileController extends Thread {
    private DatagramSocket iSocket;
    private DatagramPacket iInPacket;
    private HostInfo iHost;

    public FileController(DatagramSocket pSocket, DatagramPacket pPacket, HostInfo pHost) {
        iSocket = pSocket;
        iInPacket = pPacket;
        iHost = pHost;
    }

    private Package unzip() throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(iInPacket.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);

        return (Package) ois.readObject();
    }

    public void run() {
        try {
            Package pkg = unzip();

            if (pkg != null && pkg.getiService().equals("UPLOAD")) {
                byte[] buffer = new byte[FileServer.PIECE];
                FileInfo file_info = (FileInfo) pkg.getiContent();
                File file_received = new File("./resources/" + file_info.getiFilename());
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file_received));
                var address = iInPacket.getAddress();
                var port = iInPacket.getPort();

                for (int i = 0; i < file_info.getiPartitions() - 1; ++i) {
                    iInPacket = new DatagramPacket(buffer, buffer.length);
                    
                    while (true) {
                        try {
                            iSocket.setSoTimeout(3000);
                            iSocket.receive(iInPacket);
                        
                            break;
                        } catch(SocketException | SocketTimeoutException err) {
                            Package order = new Package("REUPLOAD", file_info.getiFilename() + "`" + i);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            ObjectOutput oo = new ObjectOutputStream(baos);
                            oo.writeObject(order);
                            oo.close();
                            
                            byte[] box = baos.toByteArray();
                            iSocket.send(new DatagramPacket(box, box.length, address, port));
                            iSocket.send
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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

    public void sendMessage(Message pMessage) {
        ClientThread client = iClients.get(pMessage.getiTo());
        client.sendPackage("CHATTING", pMessage);
    }

    public void execute() {
        try (ServerSocket server_socket = new ServerSocket(iLocal.getiPort())) {
            System.out.println("Server is running on PORT: " + iLocal.getiPort());

            while (true) {
                Socket socket = server_socket.accept();
                System.out.println("New client connected");
                ClientThread new_client = new ClientThread(socket, this);
                new_client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeClient(String iAccount) {
        iClients.remove(iAccount);
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.execute();
    }
}
