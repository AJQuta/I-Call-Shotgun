import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class Server {

    class Thread {
        private int thr_port;
        private ServerSocket thr_serv;

        protected Thread(int p) {
            thr_port = p;
            try {
                thr_serv = new ServerSocket(thr_port);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }

        
    }

    private int port;
    private LinkedList<Thread> socket_threads;
    private Socket sock;
    private ServerSocket servSock;
    
    public Server() {
        port = 4444;
        try {
            this.servSock = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        
    }

    private Socket create_serv_thread() {

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
