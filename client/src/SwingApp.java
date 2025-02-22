import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;
import javax.imageio.*;
import java.io.*;
import javax.swing.ImageIcon;
import java.net.*;




public class SwingApp extends JFrame implements ActionListener {
    JButton exit_button;
    JButton call_shotgun;
    JButton tester;
    public SwingApp () {

        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        exit_button = new JButton("Exit");
        call_shotgun = new JButton("");
        tester = new JButton("test");


        JFrame frame = new JFrame();
        //frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setSize(screenWidth, screenHeight);


        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        ImageIcon imageIcon = new ImageIcon("./client/src/shotgun.png");
        Image image = imageIcon.getImage();
        imageIcon = new ImageIcon(image.getScaledInstance(screenWidth, screenHeight, Image.SCALE_SMOOTH));
        frame.setContentPane(new JLabel(imageIcon));

        frame.setLayout(null);
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


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit_button) {
            System.exit(1);
        } else if (e.getSource() == call_shotgun) {
            try {

                String message = "Shotgun|MichaelL11";

                Socket socket = new Socket("localhost", 4444);
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
                pw.println(message);


                /*URL server_port = new URL("https://localhost:4444/");
                HttpsURLConnection connection =  (HttpsURLConnection) server_port.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "text/plain");
                OutputStream os = connection.getOutputStream();
                byte[] raw_message = message.getBytes(StandardCharsets.UTF_8);
                os.write(raw_message, 0, raw_message.length);

                connection.disconnect();*/



            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        } else if (e.getSource() == tester) {
            System.out.println("tester works");
        }
    }
}
