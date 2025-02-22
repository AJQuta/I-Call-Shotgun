import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;




public class SwingApp extends JFrame implements ActionListener {
    JButton exit_button;
    JButton call_shotgun;
    JButton tester;
    public SwingApp () {
        exit_button = new JButton("Exit");
        call_shotgun = new JButton("Shotgun");
        tester = new JButton("test");

        JFrame frame = new JFrame();

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new FlowLayout());
        frame.setUndecorated(true);


        exit_button.addActionListener(this);
        call_shotgun.addActionListener(this);
        tester.addActionListener(this);

        frame.add(exit_button);
        frame.add(call_shotgun);
        frame.add(tester);

        frame.setVisible(true);

    }


    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exit_button) {
            System.exit(1);
        } else if (e.getSource() == call_shotgun) {
            try {

                String message = "Shotgun|MichaelL11";
                URL server_port = new URL("http://localhost:portNumber/");
                HttpsURLConnection connection =  (HttpsURLConnection) server_port.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "text/plain");
                OutputStream os = connection.getOutputStream();
                byte[] raw_message = message.getBytes(StandardCharsets.UTF_8);
                os.write(raw_message, 0, raw_message.length);

                connection.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
                System.exit(1);
            }
        } else if (e.getSource() == tester) {
            System.out.println("tester works");
        }
    }
}
