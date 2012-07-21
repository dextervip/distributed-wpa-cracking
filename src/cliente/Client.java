package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.ini4j.Ini;

public class Client {

    private Socket socket;
    private int port = 8080;
    private String serverAddress = "localhost";
    private WPACracker cracker;
    private CombinationGenerator cg;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private Matcher m;
    private static final Logger LOG = Logger.getLogger(Client.class.getName());

    public Client() {
        this.cracker = new Aircrack();
        this.cg = new CombinationGenerator();
        this.loadConfing();
    }
    /**
     * Load the configuration from ini file
     */
    private void loadConfing() {
        Ini ini = new Ini();
        try {
            ini.load(new File("config.ini"));
            Ini.Section config = ini.get("config");
            this.serverAddress = config.get("host");
            this.port = Integer.parseInt(config.get("port"));
        } catch (Exception ex) {
            LOG.log(Level.SEVERE, "Error while loading config", ex);
        } 
    }

    /**
     * Connect to server
     */
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

    /**
     * Send a message to server
     *
     * @param msg
     */
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

    /**
     * Read a message from server
     *
     * @return string message
     */
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

    /**
     * Process a message incoming from server
     *
     * @param msg
     * @throws Exception
     */
    public void process(String msg) throws Exception {
        LOG.log(Level.INFO, "Mensagem recebida: {0}", msg);
        String regex = "(?i)\\QSTATUS\\E";
        Matcher m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.send("STATUS " + this.cracker.getStatus());
            //return;
        }

        regex = "(?i)\\QSTATS\\E";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.send("STATS " + this.cracker.getCurrentTime() + " " + this.cracker.getCurrentKeysPerSecond()
                    + " " + this.cracker.getCurrentPassphrase());
            //return;
        }
        regex = "(?i)\\QGET_KEY_FOUND\\E";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.send("KEY " + this.cracker.getKeyFound());
            //return;
        }
        regex = "(?i)\\QCAP\\E";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            //receber arquivo
            this.receiveCapFile();
            //return;
        }
        regex = "(?i)\\QSTART_CRACK\\E\\s(?<charset>[\\w]+)\\s(?<min>[\\d]+)\\s(?<max>[\\d]+)\\s(?<part>[\\d]+)\\s(?<totalClients>[\\d]+)";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            //gera lista de combinações
            cg.setCharacterSet(m.group("charset"));
            cg.setMinCharacter(Integer.parseInt(m.group("min")));
            cg.setMaxCharacter(Integer.parseInt(m.group("max")));
            cg.setActualPart(Integer.parseInt(m.group("part")));
            cg.setTotalParts(Integer.parseInt(m.group("totalClients")));
            cg.setPath("combinations" + m.group("part") + ".txt");
            cg.startProceeds();
            //cg.generate("combinations.txt");
            //inicia o wpacracker
            this.cracker.setCapPath("received.cap");
            this.cracker.setCombinationPath("combinations" + m.group("part") + ".txt");
            this.cracker.startCrack();
            //return;
        }
    }

    /**
     * Receive a cap file from server
     *
     * @throws IOException
     */
    private void receiveCapFile() throws IOException {
        try {
            //recebendo arquivo pelo stream
            int dataLength = this.inputStream.readInt();
            byte[] receivedData = new byte[dataLength];

            for (int i = 0; i < receivedData.length; i++) {
                receivedData[i] = this.inputStream.readByte();
            }
            //gravando arquivo recebido pelo strem
            FileOutputStream fos = new FileOutputStream("received.cap");
            fos.write(receivedData);
            fos.close();
            //mandando reposta de recebido
            this.send("CAP_OK");
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error while transfering file", ex);
            this.send("CAP_TRANSFERENCE_ERROR " + ex.getMessage());
        }
    }
}
