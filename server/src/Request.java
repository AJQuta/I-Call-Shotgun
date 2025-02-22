public class Request {

    public static enum REQ_TYPE {
        POST,
        GET
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

    public Request(String req_data) {
        // TODO: take in data and create request
    }
    
}
