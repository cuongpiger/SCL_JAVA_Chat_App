import java.io.Serializable;

public class FileInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String iFrom;
    private String iTo;
    private String iFilename;
    private long iSize;
    private int iPieces;
    private int iLastByte;
    private byte[] iDataBytes;
    private String iStatus;

    public FileInfo(String pFrom, String pTo, String pFilename, byte[] pDataBytes) {
        iFrom = pFrom;
        iTo = pTo;
        iFilename = pFilename;
        iDataBytes = pDataBytes;
    }

    public String getiStatus() {
        return iStatus;
    }

    public String getiFrom() {
        return iFrom;
    }

    public String getiTo() {
        return iTo;
    }

    public String getiFilename() {
        return iFilename;
    }

    public byte[] getiDataBytes() {
        return iDataBytes;
    }

    public void setiStatus(String pStatus) {
        iStatus = pStatus;
    }

    public void setiDataBytes(byte[] pData) {
        iDataBytes = pData;
    }
}