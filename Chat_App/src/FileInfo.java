import java.io.Serializable;

public class FileInfo implements Serializable {
    private String iFilename;
    private long iSize;
    private int iPartitions;
    private int iLastBytes;
    private String iFrom;
    private String iTo;

    public FileInfo(String pFilename, long pSize, int pPartitions, int pLastBytes, String pFrom, String pTo) {
        iFilename = pFilename;
        iSize = pSize;
        iPartitions = pPartitions;
        iLastBytes = pLastBytes;
        iFrom = pFrom;
        iTo = pTo;
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

    public int getiPartitions() {
        return iPartitions;
    }

    public int getiLastBytes() {
        return iLastBytes;
    }
}
