package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private Socket socket;
    private int port = 8080;
    private String serverAddress = "localhost";
    private WPACracker cracker;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Matcher m;
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    public Client() {
        this.cracker = new Aircrack();
    }

    public void connect() {
        try {
            socket = new Socket(serverAddress, port);
            LOG.log(Level.INFO, "Connected to server");
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());
            this.inputStream = new DataInputStream(this.socket.getInputStream());
        } catch (ConnectException ex) {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            LOG.log(Level.WARNING, "Connection refused, retrying to connect in 3 seconds...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
            this.connect();
        } catch (SocketException ex) {
            LOG.log(Level.WARNING, "Connection Error, retrying to connect in 3 seconds...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
            this.connect();
        } catch (UnknownHostException ex) {
            //LOG.log(Level.SEVERE, null, ex);
            LOG.log(Level.WARNING, "Unknown Host");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void send(String msg) {
        try {
            this.outputStream.writeUTF(msg);
            this.outputStream.flush();
        } catch (SocketException e) {
            //falha na conexao
            LOG.log(Level.WARNING, "Connection Error");
            this.connect();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unknown Error", e);
        }

    }

    public String receive() {
        try {
            String msg = inputStream.readUTF();
            return msg;
        } catch (SocketException e) {
            LOG.log(Level.WARNING, "Connection Error");
            this.connect();
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Unknown Error", e);
        }
        return null;
    }

    public void setCracker(WPACracker cracker) {
        this.cracker = cracker;
    }

    public WPACracker getCracker() {
        return this.cracker;
    }

    public void process(String msg) throws IOException {
        LOG.log(Level.INFO, "Mensagem recebida: {0}", msg);
        String regex = "(?i)\\QSTATUS\\E";
        Matcher m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.send("STATUS "+this.cracker.getStatus());
        }
        
        regex = "(?i)\\QSTATS\\E";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.send("STATS "+this.cracker.getCurrentTime()+" "+this.cracker.getCurrentKeysPerSecond()+
                    " "+this.cracker.getCurrentPassphrase());
        }

    }
}
