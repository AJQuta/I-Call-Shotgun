import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;

public class Server {

    private class Thread {
        private int thr_port;
        private ServerSocket thr_serv;
        private Socket thr_sock;

        protected Thread(int p) {
            thr_port = p;
            try {
                thr_serv = new ServerSocket(thr_port);
                // thr_sock = thr_serv.accept();

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            
        }

        private void run() { //async

        }
        
    }

    private int port;
    private LinkedList<Thread> socket_threads;
    private Socket sock;
    private ServerSocket servSock;
    
    public Server() {
        port = 4444;
        socket_threads = new LinkedList<>();
        try {
            this.servSock = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private boolean check_port_freedom(int port) {
        try (ServerSocket test = new ServerSocket(port)) {
            test.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private int create_request_handler(Request req) {
        int new_port = 4000;
        if (!socket_threads.isEmpty()) {
            new_port = socket_threads.getLast().thr_port + 1;
        }
        while (! check_port_freedom(new_port)) {
            new_port += 1;
        }

        socket_threads.add(new Thread(new_port));
        return new_port;
    }

    public static void main(String[] args) {
        Server s = new Server();

        Request req;
        while (true) {
            try {
                s.sock = s.servSock.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(s.sock.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(s.sock.getOutputStream(), StandardCharsets.UTF_8), true);
                req = new Request(bf.readLine());
                System.out.println(req.getData());
                if (req.getType() == Request.REQ_TYPE.BOOTSTRAP) {
                    System.out.println("hello");
                    int port = s.create_request_handler(req);
                    System.out.println("This is new port: " + port);
                    pw.println("" + port);
                }
                SwingAppServer GUI = new SwingAppServer();
                while (true) {
                    String stats = "Server is connected to: ";
                    stats.concat(req.getData());
                    GUI.updateStats(stats);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
