package servidor;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
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
    
    public Node(Socket socket) throws IOException {
        this.socket = socket;
        this.outputStream = new DataOutputStream(this.socket.getOutputStream());
        this.inputStream = new DataInputStream(this.socket.getInputStream());
        this.ip = this.socket.getInetAddress().getHostAddress();
    }

    public void send(String msg) throws IOException {
        this.outputStream.writeUTF(msg);
        this.outputStream.flush();
    }

    private String receive() throws IOException {
        return this.inputStream.readUTF();
    }

    public void startCrack(String charset, int min, int max, int part, int totalClients, String cap) {
    }

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

    public void updateStatistics() throws IOException {
        this.send("STATUS");
        //this.send("STATISTICS");
    }
    
    public void process() throws IOException{
        String msg = this.receive();
        LOG.log(Level.INFO, "Mensagem recebida: {0}", msg);
        String regex = "(?i)\\QSTATUS\\E\\s(?<status>[\\w]+)";
        Matcher m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            this.status = m.group("status");
        }

    }
    
    @Override
    public void run(){
        try{
            this.process();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
