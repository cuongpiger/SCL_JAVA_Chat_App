package modules;

public class Message {
    private String iFrom;
    private String iContent;
    private String iTo;

    public Message(String pFrom, String pTo, String pContent) {
        iFrom = pFrom;
        iTo = pTo;
        iContent = pContent;
    }

    public String getIFrom() {
        return iFrom;
    }

    public String getiContent() {
        return iContent;
    }

    public String getiTo() {
        return iTo;
    }
}
