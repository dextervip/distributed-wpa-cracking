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
            LOG.log(Level.INFO, "Conectado ao servidor");
            this.outputStream = new DataOutputStream(this.socket.getOutputStream());
            this.inputStream = new DataInputStream(this.socket.getInputStream());
        } catch (ConnectException ex) {
            //Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            LOG.log(Level.WARNING, "Conexão recusada, tentando reconectar em 3 segundos...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
            this.connect();
        } catch (SocketException ex){
            LOG.log(Level.WARNING, "Problemas de conexão, tentando reconectar em 3 segundos...");
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex1) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex1);
            }
            this.connect();
        } 
        catch (UnknownHostException ex) {
            //LOG.log(Level.SEVERE, null, ex);
            LOG.log(Level.WARNING, "Host desconhecido");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void send(String msg) throws IOException {
        this.outputStream.writeUTF(msg);
        this.outputStream.flush();
    }

    public String receive() throws IOException {
        String msg = inputStream.readUTF();
        return msg;
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
            this.send(this.cracker.getStatus());
        }

    }
}
