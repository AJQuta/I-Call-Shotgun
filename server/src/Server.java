import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.lang.Thread;

public class Server {

    private class Serv_Thread extends Thread {

        private final Object lock1 = new Object();

        private int thr_port;
        private Request request;
        private ServerSocket thr_serv;
        private Socket thr_sock;

        public Serv_Thread(int p, Request req) {
            thr_port = p;
            request = req;
            try {
                thr_serv = new ServerSocket(thr_port);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            this.start();
        }

        public void run() {
            try {
                thr_sock = thr_serv.accept();
                BufferedReader bf = new BufferedReader(new InputStreamReader(thr_sock.getInputStream(), StandardCharsets.UTF_8));
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(thr_sock.getOutputStream(), StandardCharsets.UTF_8), true);
                pw.println("Ehlo " + request.getData() + " from " + getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
            
        }
        
    }

    private int port;
    private LinkedList<Serv_Thread> socket_threads;
    private Socket sock;
    private ServerSocket servSock;
    
    public Server() {
        port = 3444;
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

        socket_threads.add(new Serv_Thread(new_port, req));
        return new_port;
    }

    private void request_post_office(Request req, BufferedReader bf, PrintWriter pw) throws IOException {
        switch (req.getType()) {
            case Request.REQ_TYPE.BOOTSTRAP:
                int port = create_request_handler(req);
                pw.println("" + port);
                break;
            case Request.REQ_TYPE.POST:
                break;
            case Request.REQ_TYPE.GET:
                break;
            case Request.REQ_TYPE.SHOTGUN:
                break;
            default:
                throw new InvalidRequestException("Invalid request type");
        }
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
                s.request_post_office(req, bf, pw);
                bf.close();
                pw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
