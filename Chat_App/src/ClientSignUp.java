import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class ClientSignUp extends Thread {
    private User iUser;
    private SigninUI iUI;
    private Socket iSocket;
    private HostInfo iHostServer;

    public ClientSignUp(SigninUI pUI, User pUser) {
        iUser = pUser;
        iUI = pUI;
        iHostServer = Utils.loadHostInfo("./config/master.txt");
    }

    private Object receivePackage() {
        try {
            ObjectInputStream iInStream = new ObjectInputStream(iSocket.getInputStream());
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
            iSocket = new Socket(iHostServer.getiAddress(), 9900);
            sendPackage("SIGN-UP", iUser);
            Package box = (Package) receivePackage();
            String alert = (boolean) box.getiContent() ? "Sign up success." : "Registration failed!";
            iUI.showDialog(alert);
            iSocket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendPackage(String pService, Object pContent) {
        Package box = new Package(pService, pContent);
        try {
            ObjectOutputStream out_stream = new ObjectOutputStream(iSocket.getOutputStream());
            out_stream.writeObject(box);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}