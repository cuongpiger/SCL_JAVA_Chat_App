import java.io.Serializable;

public class Message implements Serializable {
    private String iFrom;
    private String iTo;
    private String iContent;

    public Message(String pFrom, String pTo, String pContent) {
        iFrom = pFrom;
        iTo = pTo;
        iContent = pContent;
    }

    public String getiFrom() {
        return iFrom;
    }

    public String getiContent() {
        return iContent;
    }

    public String getiTo() {
        return iTo;
    }
}
