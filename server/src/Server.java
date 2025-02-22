import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Server {

    private class Thread {
        private int thr_port;
        private ServerSocket thr_serv;
        private Socket thr_sock;

        protected Thread(int p) {
            thr_port = p;
            try {
                thr_serv = new ServerSocket(thr_port);
                thr_sock = thr_serv.accept();

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

    private int create_serv_thread() {
        int new_port = port + 1;
        if (socket_threads.isEmpty()) {
            socket_threads.add(new Thread(new_port));
        } else {
            new_port = socket_threads.getLast().thr_port + 1;
        }
        return new_port;
    }

    public static void main(String[] args) {
        Server s = new Server();

        Request req;
        String req_str;
        while (true) {
            try {
                s.sock = s.servSock.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.sock.getInputStream(), StandardCharsets.UTF_8));
                req_str = bf.readLine();
                req = new Request(req_str);
                System.out.println(req.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
