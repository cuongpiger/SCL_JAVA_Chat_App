import java.io.Serializable;

public class HostInfo implements Serializable {
    private String iAddress = null;
    private int iPort;

    public HostInfo(String pAddress, int pPort) {
        iAddress = pAddress;
        iPort = pPort;
    }

    public String getiAddress() {
        return iAddress;
    }

    public int getiPort() {
        return iPort;
    }
}