package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Node extends Thread {

    private String ip;
    private String status;
    private String currentKeysPerSecond;
    private String currentPassphrase;
    private String currentTime;
    private String keyFound;
    private Socket socket;
    private DataOutputStream outputStream;
    private DataInputStream inputStream;
    private static final Logger LOG = Logger.getLogger(Node.class.getName());
    private boolean finalized = false;

    public Node(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        this.inputStream = new DataInputStream(this.socket.getInputStream());
        this.ip = this.socket.getInetAddress().getHostAddress();
    }
    /**
     * Send a message to node
     * @param msg
     * @throws IOException 
     */
    public void send(String msg) throws IOException {
        this.outputStream.writeUTF(msg);
        this.outputStream.flush();
    }
    /*
     * Read a message from node
     */
    private String receive() throws IOException {
        return this.inputStream.readUTF();
    }

    /**
     * Start node cracking
     * @param charset
     * @param min
     * @param max
     * @param part
     * @param totalClients
     * @param capFilePath 
     */
    public void startCrack(String charset, int min, int max, int part, int totalClients, String capFilePath) {

        try {
            this.send("CAP");
            
            File file = new File(capFilePath);
            FileInputStream fin = new FileInputStream(file);
            byte sendData[] = new byte[(int) file.length()];
            fin.read(sendData);
            this.outputStream.writeInt(sendData.length);
            this.outputStream.write(sendData, 0, sendData.length);
            this.outputStream.flush();
            
            this.send("START_CRACK "+charset+" "+min+" "+max+" "+part+" "+totalClients);
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, "Error while sending cap file.", ex);
        }
    }
    /**
     * Stop node cracking
     */
    public void stopCrack() {
    }

    public String getIp() {
        return ip;
    }

    public String getStatus() {
        return this.status;
    }

    public String getCurrentPassphrase() {
        return this.currentPassphrase;
    }

    public String getCurrentTime() {
        return this.currentTime;
    }

    public String getCurrentKeysPerSecond() {
        return this.currentKeysPerSecond;
    }

    public String getKeyFound() {
        return this.keyFound;
    }
    /**
     * Request node to update status
     * @throws IOException 
     */
    public void updateStatus() throws IOException {
        this.send("STATUS");
    }
    /**
     * Request node to update statistics
     * @throws IOException 
     */
    public void updateStatistics() throws IOException {
        this.send("STATS");
    }
    /**
     * Request node to update key found
     * @throws IOException 
     */
    public void updateKeyFound() throws IOException {
        this.send("GET_KEY_FOUND");
    }
    
    /**
     * Process all incoming stream from the node
     * @throws IOException 
     */
    public void process() throws IOException {
        String msg = this.receive();
        LOG.log(Level.INFO, "Mensagem recebida: {0}", msg);
        String regex = "(?i)\\QSTATUS\\E\\s(?<status>[\\w]+)";
        Matcher m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.status = m.group("status");
            return;
        }
        regex = "(?i)\\QSTATS\\E\\s(?<tempo>[\\w\\/]+)\\s(?<keysPerSecond>[\\w\\/]+)\\s(?<currentPassphrase>[\\w\\/]+)";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.currentTime = m.group("tempo");
            this.currentKeysPerSecond = m.group("keysPerSecond");
            this.currentPassphrase = m.group("currentPassphrase");
            return;
        }
        regex = "(?i)\\QCAP_OK\\E";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.status = "WAITING";
            return;
        }
        regex = "(?i)\\QKEY\\E\\s(?<key>[\\w]+)";
        m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.keyFound = m.group("key");
            return;
        }

    }
    /**
     * Finalize node thread
     */
    public void finalizeThread() {
        this.finalized = true;
    }

    @Override
    public void run() {
        try {
            while (finalized == false) {
                this.process();
            }
        } catch (SocketException e) {
            // Cliente desconectado
            e.printStackTrace();
            this.status = "Disconnected";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
