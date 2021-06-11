import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{
    private ServerSocket serverSocket;
    private int port = 9900;

    public static void main(String[] args) {
        Server server = new Server();
        server.open();
        server.start();
    }

    public void open() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is open on port " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            Socket server = null;
            DataOutputStream dos = null;
            ObjectInputStream ois = null;
            ObjectOutputStream oos = null;

            try {
                server = serverSocket.accept();
                System.out.println("new connection");

                dos = new DataOutputStream(server.getOutputStream());
                FileInfo fileInfo = getFileInfo("/home/manhcuong/test.pdf", "/home/manhcuong/test/");
                oos = new ObjectOutputStream(server.getOutputStream());
                oos.writeObject(fileInfo);
                ois = new ObjectInputStream(server.getInputStream());

                fileInfo = (FileInfo) ois.readObject();

                if (fileInfo != null) {
                    System.out.println(fileInfo.getStatus());
                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                closeStream(oos);
                closeStream(ois);
                closeStream(dos);
                closeSocket(server);
            }
        }
    }

    private FileInfo getFileInfo(String sourceFilePath, String destinationDir) {
        FileInfo fileInfo = null;
        BufferedInputStream bis = null;
        try {
            File sourceFile = new File(sourceFilePath);
            bis = new BufferedInputStream(new FileInputStream(sourceFile));
            fileInfo = new FileInfo();
            byte[] fileBytes = new byte[(int) sourceFile.length()];
            // get file info
            bis.read(fileBytes, 0, fileBytes.length);
            fileInfo.setFilename(sourceFile.getName());
            fileInfo.setDataBytes(fileBytes);
            fileInfo.setDestinationDirectory(destinationDir);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeStream(bis);
        }
        return fileInfo;
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
