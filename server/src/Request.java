import java.io.IOException;

public class Request {

    public static enum REQ_TYPE {
        BOOTSTRAP,
        POST,
        GET,
        SHOTGUN
    }

    public static enum SERV_RESPONSE {
        SUCCESS,
        IOERROR,
        INVALID
    }

    private REQ_TYPE type;
    private String data;

    public Request(REQ_TYPE t, String d) {
        this.type = t;
        this.data = d;
    }

    public REQ_TYPE getType() {
        return type;
    }

    public String getData() {
        return data;
    }

    public Request(String req_data) throws IOException {
        if (req_data == null) {
            throw new InvalidRequestException("No data given.");
        }
        String[] fields = req_data.split("\\|");
        type = castToREQType(fields[0]);
        if (type == null) {
            throw new InvalidRequestException("Invalid type");
        }
        this.data = "";
        
        for (int i = 1; i < fields.length; i++) {
            this.data += fields[i] + " ";
        }
        this.data = this.data.trim();
    }

    public boolean equals(Object obj) {
        if (! (obj instanceof Request)) {
            return false;
        }
        Request req = (Request)obj;
        return (this.data == req.data && this.data.equals(req.data));
    }

    private REQ_TYPE castToREQType(String t) {
        switch(t) {
            case "BOOTSTRAP":
                return REQ_TYPE.BOOTSTRAP;
            case "POST":
                return REQ_TYPE.POST;
            case "GET":
                return REQ_TYPE.GET;
            case "SHOTGUN":
                return REQ_TYPE.SHOTGUN;
            default:
                return null;
        }
    }
    
}
