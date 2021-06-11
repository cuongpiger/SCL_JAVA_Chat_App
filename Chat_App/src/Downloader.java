import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Downloader extends Thread {
    private Socket iSocket;
    private String iFilename;

    public Downloader(String pFilename) {
        String hostname = Utils.loadHostInfo("./config/master.txt").getiAddress();
        iFilename = pFilename;
        try {
            iSocket = new Socket(hostname, 4321);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            DataOutputStream dos = new DataOutputStream(iSocket.getOutputStream());
            ObjectOutputStream oos = new ObjectOutputStream(iSocket.getOutputStream());
            oos.writeObject(new Package("DOWNLOAD", iFilename));
            ObjectInputStream ois = new ObjectInputStream(iSocket.getInputStream());
            FileInfo file_info = (FileInfo) ois.readObject();

            if (file_info != null) {
                createFileInDownload(file_info);
            }

            closeStream(ois);
            closeStream(oos);
            closeSocket(iSocket);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createFileInDownload(FileInfo pFile) {
        BufferedOutputStream bos = null;

        try {
            if (pFile != null) {
                File file_receive = new File("./downloads/" + pFile.getiFilename());
                bos = new BufferedOutputStream(new FileOutputStream(file_receive));
                bos.write(pFile.getiDataBytes());
                bos.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(bos);
        }
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
