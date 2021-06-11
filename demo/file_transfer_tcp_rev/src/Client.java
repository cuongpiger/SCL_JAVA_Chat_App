import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket client;
    private String serverHost = "localhost";
    private int serverPort = 9900;

    public static void main(String[] args) {
        Client client = new Client();
        client.connectServer();
        client.getFile();
    }

    public void connectServer() {
        try {
            client = new Socket(serverHost, serverPort);
            System.out.println("Connected to server");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getFile() {
        DataInputStream dis = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;

        try {
            dis = new DataInputStream(client.getInputStream());
            ois = new ObjectInputStream(client.getInputStream());
            FileInfo fileInfo = (FileInfo) ois.readObject();

            if (fileInfo != null) {
                createFile(fileInfo);
            }

            oos = new ObjectOutputStream(client.getOutputStream());
            fileInfo.setStatus("success");
            fileInfo.setDataBytes(null);
            oos.writeObject(fileInfo);
            System.out.println("Get file success");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeStream(ois);
            closeStream(oos);
            closeStream(dis);
            closeSocket(client);

            System.out.println("Close connection to server");
        }
    }

    private boolean createFile(FileInfo fileInfo) {
        BufferedOutputStream bos = null;

        try {
            if (fileInfo != null) {
                File fileReceive = new File(fileInfo.getDestinationDirectory()
                        + fileInfo.getFilename());
                bos = new BufferedOutputStream(
                        new FileOutputStream(fileReceive));
                // write file content
                bos.write(fileInfo.getDataBytes());
                bos.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            closeStream(bos);
        }
        return true;
    }

    public void closeSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * close input stream
     *
     * @author viettuts.vn
     */
    public void closeStream(InputStream inputStream) {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * close output stream
     *
     * @author viettuts.vn
     */
    public void closeStream(OutputStream outputStream) {
        try {
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
