package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server extends Thread {

    private ArrayList<Node> nodes = new ArrayList<Node>();
    private ServerSocket socket;
    private int port = 8080;
    private String capPath;
    private String charset;
    private int min;
    private int max;
    private int updateFrequency = 5;
    private static final Logger LOG = Logger.getLogger(Server.class.getName());
    /**
     * Start server and listen for client's connection
     * @throws IOException 
     */
    public void listen() throws IOException {
        this.socket = new ServerSocket(this.port);
        while (true) {
            Node node = new Node(this.socket.accept());
            this.addNode(node);
            LOG.log(Level.INFO, "New client connected {0}", node.getIp());
            LOG.log(Level.INFO, "Number of clients: {0}", this.getNumberOfNodes());
            node.start();
        }
    }
    /**
     * Add an node to list
     * @param node 
     */
    public void addNode(Node node) {
        this.nodes.add(node);
    }
    /**
     * Send a message to all nodes
     * @param msg
     * @throws IOException 
     */
    public void sendAll(String msg) throws IOException {
        ListIterator<Node> nodeList = nodes.listIterator();
        while (nodeList.hasNext()) {
            Node node = nodeList.next();
            node.send(msg);
        }
    }
    /*
     * Send a message to a node
     */
    public void sendTo(Node n, String msg) throws IOException {
        Node node = this.nodes.get(this.nodes.indexOf(n));
        node.send(msg);
    }
    /**
     * Get the number of active nodes
     * @return 
     */
    public int getNumberOfNodes() {
        return this.nodes.size();
    }

    public ArrayList<Node> getNodes() {
        return this.nodes;
    }
    /**
     * Load cap path
     * @param path 
     */
    public void loadCap(String path) {
        this.capPath = path;
    }
    /**
     * Configure the brute force dictionary
     * @param charset
     * @param min
     * @param max 
     */
    public void configureDictionary(String charset, int min, int max) {
        this.charset = charset;
        this.min = min;
        this.max = max;
    }
    /*
     * Start crack process in all nodes
     */
    public void startCrack() throws Exception {
        LOG.log(Level.INFO, "Starting crack...");
        if (this.capPath == null || this.capPath.isEmpty()) {
            LOG.log(Level.WARNING, "No cap file loaded");
            throw new Exception("No cap file loaded");
        }
        if (this.charset == null || this.charset.isEmpty()
                || this.min == 0 || this.max == 0) {
            LOG.log(Level.WARNING, "No dictionary configured");
            throw new Exception("No dictionary configured");
        }
        
        ListIterator<Node> list = this.getNodes().listIterator();
        int totalClients = this.getNumberOfNodes();
        int count = 0;
        while(list.hasNext()){
            count++;
            Node node =  list.next();
            node.startCrack(this.charset, this.min, this.max, count, totalClients, this.capPath);
        }

    }
    /**
     * Stop crack process in all nodes
     */
    public void stopCrack() {
        LOG.log(Level.INFO, "Stopping crack...");

    }
    /**
     * Get statistics about cracking process from all nodes
     * @return 
     */
    public String[] getStatistics() {
        return null;
    }
    /**
     * Set update UI frequency 
     * @param updateFrequency 
     */
    public void setUpdateFrequency(int updateFrequency) {
        this.updateFrequency = updateFrequency;
    }
    /**
     * Get update UI frequency
     * @return int seconds frequency
     */
    public int getUpdateFrequency() {
        return updateFrequency;
    }
    
    
    @Override
    public void run() {
        try {
            this.listen();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
