import java.io.Serializable;

public class Package implements Serializable {
    private String iService;
    private Object iContent;

    public Package(String pService, Object pContent) {
        iService = pService;
        iContent = pContent;
    }

    public String getiService() {
        return iService;
    }

    public Object getiContent() {
        return iContent;
    }
}
