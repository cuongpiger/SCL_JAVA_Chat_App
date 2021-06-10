import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket iSocket;
    private Server iServer;
    private ObjectInputStream iInStream;
    private ObjectOutputStream iOutStream;

    public ClientThread(Socket pSocket, Server pServer) throws IOException {
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
        while (true) {
            try {
                Package box = (Package) receivePackage();

                if (box.getiService().equals("SIGN-UP")) {
                    User user = (User) box.getiContent();
                    sendPackage("SIGN-UP", iServer.signup(user));
                } else if (box.getiService().equals("SIGN-IN")) {
                    User user = (User) box.getiContent();

                    if (iServer.signin(user)) { // login success
                        sendPackage("SIGN-IN", true);
                        iServer.addNewClient(user.getiAccount(), this);
                        iServer.broadcast(); // broadcast new users joined

                        do {
                            box = (Package) receivePackage();

                            if (box.getiService().equals("CHATTING")) {
                                Message message = (Message) box.getiContent();
                                iServer.sendMessage(message);
                            }
                        } while (!box.getiService().equals("CLOSE"));

                        iServer.removeClient(user.getiPassword());
                        iSocket.close();

                        iServer.broadcast();
                    } else { // login failed
                        sendPackage("SIGN-IN", false);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
