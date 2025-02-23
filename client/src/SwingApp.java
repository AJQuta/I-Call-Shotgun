import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;
import javax.imageio.*;
import java.io.*;
import java.net.*;




public class SwingApp extends JFrame implements ActionListener {
    JButton exit_button;
    JButton call_shotgun;
    JButton tester;
    Socket socket;
    String username;
    String futureMessage;
    public SwingApp() {
        try {




            username = JOptionPane.showInputDialog(null, "Enter your name:", "Input", JOptionPane.QUESTION_MESSAGE);

            String message = "BOOTSTRAP|" + username;

            Socket preSocket = new Socket("localhost", 3444);
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(preSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            pw.println(message);

            BufferedReader bf = new BufferedReader(new InputStreamReader(preSocket.getInputStream(), StandardCharsets.UTF_8));
            int newPort = Integer.parseInt(bf.readLine());
            System.out.println("MyPort" + newPort);
            bf.close();

            socket = new Socket("localhost", newPort);
            bf = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            System.out.println(bf.readLine());


        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }


        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        exit_button = new JButton("Exit");
        call_shotgun = new JButton("");
        tester = new JButton("test");


        JFrame frame = new JFrame();
        frame.getContentPane().setLayout(null);

        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        ImageIcon imageIcon = new ImageIcon("./client/src/shotgun.png");
        Image image = imageIcon.getImage();
        imageIcon = new ImageIcon(image.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH));
        frame.setContentPane(new JLabel(imageIcon));

        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenWidth, screenHeight);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false); // So users can't type in it
        textArea.setRows(10); // Set number of visible rows
        textArea.setColumns(30); // Set width in columns
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setText("HELLO");
        textArea.setBounds(screenWidth/10, screenHeight/10, screenWidth/2, screenHeight/2);




        try {
            SocketListener worker = new SocketListener(socket, textArea);
            worker.execute(); // Start the worker
        } catch (Exception e) {
            e.printStackTrace();
        }

        //frame.setLayout(null);
        frame.setUndecorated(true);


        exit_button.addActionListener(this);
        call_shotgun.addActionListener(this);
        tester.addActionListener(this);

        frame.add(exit_button);
        frame.add(call_shotgun);
        frame.add(tester);


        call_shotgun.setOpaque(true);
        call_shotgun.setBounds((screenWidth*10)/90, (screenHeight*10)/25, screenWidth/5, screenHeight/30);
        call_shotgun.setForeground(Color.getColor("#787569"));
        call_shotgun.setBackground(Color.getColor("#787569"));
        //call_shotgun.setBackground(Color.BLACK);


        exit_button.setBounds(screenWidth - screenWidth/10, screenHeight - screenHeight/7, screenWidth/20, screenHeight/30);

        frame.setVisible(true);

    }

    public String epicReader() {
        BufferedReader shotgunReader;
        try {
            shotgunReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            futureMessage = shotgunReader.readLine();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println(futureMessage);
        return futureMessage;
    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit_button) {
            System.exit(1);
        } else if (e.getSource() == call_shotgun) {
            try {

                String message = "SHOTGUN|" + username;
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                pw.println(message);
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }


        } else if (e.getSource() == tester) {
            System.out.println("tester works");
        }
    }
}
