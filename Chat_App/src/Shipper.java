import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Shipper extends Thread {
    private String iFilePath;
    private String iFrom;
    private String iTo;
    private String iAddress;
    private Socket iSocket;

    public Shipper(String pFrom, String pTo, String pFilePath) {
        iFrom = pFrom;
        iTo = pTo;
        iFilePath = pFilePath;
        iAddress = Utils.loadHostInfo("./config/master.txt").getiAddress();

        try {
            iSocket = new Socket(iAddress, 4321);
            System.out.println("Send file");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(iSocket.getOutputStream());
            FileInfo file_info = getFileInfo();
            ObjectOutputStream oos = new ObjectOutputStream(iSocket.getOutputStream());
            oos.writeObject(new Package("UPLOAD", file_info));
            ObjectInputStream ois = new ObjectInputStream(iSocket.getInputStream());
            file_info = (FileInfo) ois.readObject();

            if (file_info != null) {
                System.out.println(file_info.getiStatus());
            }

            closeStream(oos);
            closeStream(ois);
            closeStream(dos);
            closeSocket(iSocket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private FileInfo getFileInfo() {
        FileInfo file_info = null;
        BufferedInputStream bis = null;

        try {
            File file = new File(iFilePath);
            bis = new BufferedInputStream(new FileInputStream(file));
            byte[] file_bytes = new byte[(int) file.length()];
            bis.read(file_bytes, 0, file_bytes.length);
            file_info = new FileInfo(iFrom, iTo, file.getName(), file_bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(bis);
        }

        return file_info;
    }

    private void closeSocket(Socket pSocket) {
        try {
            if (pSocket != null) {
                pSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStream(InputStream pInStream) {
        try {
            if (pInStream != null) {
                pInStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeStream(OutputStream pOutputStream) {
        try {
            if (pOutputStream != null) {
                pOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
