import java.io.IOException;

public class Request {

    public static enum REQ_TYPE {
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
        data = req_data;
        String[] fields = data.split("|");
        type = castToREQType(Integer.parseInt(fields[0]));
        if (type == null) {
            throw new InvalidRequestException("Invalid type");
        }
        data = "";
        for (int i = 1; i < fields.length; i++) {
            data.concat(fields[i] + " ");
        }
        data.trim();
    }

    private REQ_TYPE castToREQType(int t) {
        switch(t) {
            case 0:
                return REQ_TYPE.POST;
            case 1:
                return REQ_TYPE.GET;
            case 2:
                return REQ_TYPE.SHOTGUN;
            default:
                return null;
        }
    }
    
}
