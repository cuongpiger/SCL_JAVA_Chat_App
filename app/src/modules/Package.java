package modules;

import java.io.Serializable;

public class Package implements Serializable {
    private String iService = null;
    private String iMessage = null;
    private Object iContent = null;

    public Package(String pService, String pMessage, Object pContent) {
        iService = pService;
        iMessage = pMessage;
        iContent = pContent;
    }

    public String getiService() {
        return iService;
    }

    public String getiMessage() {
        return iMessage;
    }

    public Object getiContent() {
        return iContent;
    }
}
