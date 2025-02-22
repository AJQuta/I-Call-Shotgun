import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private Socket sock;
    private ServerSocket servSock;

    public Server() {

        try {
            this.servSock = new ServerSocket(4444);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
    }

    public static void main(String[] args) {
        Server s = new Server();

        while (true) {
            try {
                s.sock = s.servSock.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
