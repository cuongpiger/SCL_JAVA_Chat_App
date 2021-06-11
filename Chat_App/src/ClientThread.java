import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientThread extends Thread {
    private Socket iSocket;
    private Server iServer;
    private ObjectInputStream iInStream;
    private ObjectOutputStream iOutStream;

    public ClientThread(Socket pSocket, Server pServer) {
        try {
            iSocket = pSocket;
            iServer = pServer;
            iInStream = new ObjectInputStream(iSocket.getInputStream());
            iOutStream = new ObjectOutputStream(iSocket.getOutputStream());
        } catch (IOException err) {
            err.printStackTrace();
        }
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

            if (box.getiService().equals("SIGN-IN")) {
                User user = (User) box.getiContent();

                if (iServer.signin(user)) { // login success
                    sendPackage("SIGN-IN", true);
                    iServer.addNewClient(user.getiAccount(), this);
                    iServer.broadcast(); // broadcast new users joined

                    while (true) {
                        System.out.println(box.getiService());

                        box = (Package) receivePackage();

                        if (box.getiService().equals("CHATTING")) {
                            Message message = (Message) box.getiContent();
                            iServer.sendMessage("CHATTING", message);
                        } else if (box.getiService().equals("CLOSE")) {
                            iServer.sendMessage("CLOSE", new Message(null, user.getiAccount(), null));
                            break;
                        };
                    }

                    iServer.removeClient(user.getiAccount());
                    iServer.broadcast();
                    iSocket.close();
                } else { // login failed
                    sendPackage("SIGN-IN", false);
                    iSocket.close();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
