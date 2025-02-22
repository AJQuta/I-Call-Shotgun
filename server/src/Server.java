import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.lang.Thread;

public class Server {
    private int userCount;
    private ArrayList<String> userInfoArr;

    private class Serv_Thread extends Thread {

        private final Object lock = new Object();

        private int thr_port;
        private Request request;
        private Server serv;
        private ServerSocket thr_serv;
        private Socket thr_sock;

        public Serv_Thread(Server s, int p, Request req) {
            serv = s;
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

        public void handle_interrupt(String data) {
            try {
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(thr_sock.getOutputStream(), StandardCharsets.UTF_8), true);
                pw.println(data);
            } catch (IOException e) {
                synchronized (lock) {
                    System.out.println(this.getName() + " received signal " + Request.SERV_RESPONSE.IOERROR);
                    e.printStackTrace();
                }
            }
        }

        public boolean forUser(String name) {
            return this.request.getData().equals(name);
        }

        public void run() {
            BufferedReader bf;
            PrintWriter pw = null;
            try {
                thr_sock = thr_serv.accept();
                bf = new BufferedReader(new InputStreamReader(thr_sock.getInputStream(), StandardCharsets.UTF_8));
                pw = new PrintWriter(new OutputStreamWriter(thr_sock.getOutputStream(), StandardCharsets.UTF_8), true);
                pw.println("Ehlo " + request.getData() + " from " + getName());
                Request req;
                while (true) {
                    req = new Request(bf.readLine());
                    synchronized (lock) {
                        serv.request_post_office(req, bf, pw);
                        pw.println(Request.SERV_RESPONSE.SUCCESS);
                    }
                }
            } catch (IOException e) {
                synchronized (lock) {
                    if (!(pw == null ||  pw.checkError())) {
                        pw.println(Request.SERV_RESPONSE.INVALID);
                    } else {
                        System.out.println(Request.SERV_RESPONSE.IOERROR);
                    }
                    e.printStackTrace();
                }
            } //catch (InterruptedException e) {

            //}
        }
    }

    private int port;
    private LinkedList<Serv_Thread> socket_threads;
    private Socket sock;
    private ServerSocket servSock;
    
    public Server() {
        port = 3444;
        socket_threads = new LinkedList<>();
        userInfoArr = new ArrayList<>();
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

        socket_threads.add(new Serv_Thread(this, new_port, req));
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
                for (Serv_Thread  thread : socket_threads) {
                    // if (! thread.request.equals(req)) {
                    //     thread.interrupt();
                    // }
                    thread.handle_interrupt("SHOTGUN: " + req.getData());
                }
                break;
            default:
                throw new InvalidRequestException("Invalid request type");
        }
    }

    public static void main(String[] args) {
        Server s = new Server();
        SwingAppServer GUI = new SwingAppServer();
        s.userCount = 0;

        Request req;
        BufferedReader bf;
        PrintWriter pw = null;
        while (true) {
            try {
                s.sock = s.servSock.accept();
                bf = new BufferedReader(new InputStreamReader(s.sock.getInputStream(), StandardCharsets.UTF_8));
                pw = new PrintWriter(new OutputStreamWriter(s.sock.getOutputStream(), StandardCharsets.UTF_8), true);
                req = new Request(bf.readLine());
                s.request_post_office(req, bf, pw);
                pw.println(Request.SERV_RESPONSE.SUCCESS);
                bf.close();
                pw.close();
                for (int i = 0; i < s.socket_threads.size(); i++) {
                    if()
                }
                String connectionInfo = "User: " + req.getData() + ", Port: " + s.socket_threads;
                s.userInfoArr.add(connectionInfo);
                s.userCount++;
                String users = "";
                for (int i = 0; i < s.userCount; i++) {
                    users = users.concat(s.userInfoArr.get(i));
                }
                GUI.updateStats(users);



            } catch (IOException e) {
                if (!(pw == null ||  pw.checkError())) {
                    pw.println(Request.SERV_RESPONSE.INVALID);
                } else {
                    System.out.println(Request.SERV_RESPONSE.IOERROR);
                }
                e.printStackTrace();
            }
        }
    }
}
