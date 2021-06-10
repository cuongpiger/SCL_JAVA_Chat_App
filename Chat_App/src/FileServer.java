import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileServer extends Thread {
    private static ServerSocket iSocket;
    private static int iPort = 4321;
    private static Server iServer;

    public FileServer(Server pServer) {
        iServer = pServer;

        try {
            iSocket = new ServerSocket(iPort);
            System.out.println("FILE SERVER is running on PORT: " + iPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            Socket client = null;

            try {
                client = iSocket.accept();
                System.out.println("FILE SERVER: new client");
                new FileController(client, iServer).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

class FileController extends Thread {
    private Socket iSocket;
    private static Server iServer;

    public FileController(Socket pSocket, Server pServer) {
        iSocket = pSocket;
        iServer = pServer;
    }

    public void run() {
        try {
            DataInputStream dis = new DataInputStream(iSocket.getInputStream());
            ObjectInputStream ois = new ObjectInputStream(iSocket.getInputStream());

            Package box = (Package) ois.readObject();

            if (box.getiService().equals("UPLOAD")) {
                FileInfo file_info = (FileInfo) box.getiContent();
                if (file_info != null) {
                    createFileInResources(file_info);
                }

                ObjectOutputStream oos = new ObjectOutputStream(iSocket.getOutputStream());
                file_info.setiStatus("SUCCESS");
                file_info.setiDataBytes(null);
                oos.writeObject(file_info);

                closeStream(oos);

                Message message = new Message(file_info.getiFrom(), file_info.getiTo(),
                        "[FILE] " + file_info.getiFilename());
                iServer.sendMessage(message);
            }
            closeStream(ois);
            closeStream(dis);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createFileInResources(FileInfo pFile) {
        BufferedOutputStream bos = null;

        try {
            if (pFile != null) {
                File file_receive = new File("./resources/" + pFile.getiFilename());
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
