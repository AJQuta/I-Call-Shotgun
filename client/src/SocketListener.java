import javax.swing.*;
import javax.sound.midi.Soundbank;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;
import javax.imageio.*;
import java.io.*;
import java.net.*;
import java.util.List;

public class SocketListener extends SwingWorker<Void, String> {

    private BufferedReader br;
    private JTextArea text;
    public SocketListener(Socket socket, JTextArea text) throws Exception {
        this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.text = text;
        System.out.println("Listener made");
    }

    @Override
    protected Void doInBackground() throws Exception {
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println("read line " + line);
            publish(line);

        }
        return null;
    }

    @Override
    protected void process(List<String> chunks) {
        text.setText("");
        for (String message : chunks) {
            text.append(message);
        }
    }


}
