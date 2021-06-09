package modules;

public class Message {
    private String iFrom;
    private String iContent;

    public Message(String pFrom, String pContent) {
        iFrom = pFrom;
        iContent = pContent;
    }

    public String getIFrom() {
        return iFrom;
    }

    public String getiContent() {
        return iContent;
    }
}
