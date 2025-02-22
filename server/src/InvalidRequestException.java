import java.io.IOException;

public class InvalidRequestException extends IOException {
    
    public InvalidRequestException(String error) {
        super(error);
    }

    public InvalidRequestException() {
        super();
    }
}
