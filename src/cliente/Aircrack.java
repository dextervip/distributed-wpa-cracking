package cliente;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Rafael
 */
public class Aircrack extends WPACracker implements Runnable {

    private ProcessBuilder pb;
    private Process process;
    private Matcher m;
    private InputStream is;
    private Thread thread;
    private static final Logger LOG = Logger.getLogger(Aircrack.class.getName());

    public Aircrack() {
        super();
        this.thread = new Thread(this);
        this.status = "INIT";
    }
    /**
     * Process output screen from aircrack process
     * @throws IOException 
     */
    public void processInputSream() throws IOException {
        InputStreamReader isr = new InputStreamReader(this.is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            this.processText(line);
        }
    }
    /**
     * Process text from aircrak output screen
     * @param text 
     */
    private void processText(String text) {
        String regex = "(?i)\\QReading packets, please wait...\\E";
        Matcher m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            this.status = "PROCESSING";
        }

        regex = "(?i)keys tested\\s\\Q(\\E(?<keys>[\\w\\s\\Q/.\\E]+)\\Q)\\E";
        m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            this.currentKeysPerSecond = m.group("keys");
        }

        regex = "(?i)\\Q[\\E\\d{2}:\\d{2}:\\d{2}\\Q]\\E";
        m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            this.currentTime = m.group();
        }

        regex = "(?i)Current\\spassphrase:\\s(?<passphrase>[\\w]+)";
        m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            this.currentPassphrase = m.group("passphrase");
        }

        regex = "(?i)KEY FOUND!\\s\\Q[\\E\\s(?<key>[\\w]+)\\s\\Q]\\E";
        m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            this.keyFound = m.group("key");
            this.status = "KEY_FOUND";
        }

        regex = "(?i)\\bPassphrase not in dictionary\\b";
        m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            this.status = "KEY_NOT_FOUND";
        }

        regex = "(?i)\\bNo networks found\\Q,\\E exiting\\b";
        m = Pattern.compile(regex).matcher(text);
        if (m.find()) {
            this.status = "ERROR";
        }
    }
    /*
     * Start aircrak process
     */
    @Override
    public void startCrack() throws Exception {
        File f = new File(this.combinationPath);
        this.pb = new ProcessBuilder("aircrack-ng", this.capPath, "-w", f.getAbsolutePath());
        LOG.log(Level.INFO, this.pb.command().toString());
        this.process = this.pb.start();
        this.is = this.process.getInputStream();
        this.thread.start();
        this.process.waitFor();
    }
    /**
     * Stop aircrak process
     */
    @Override
    public void stopCrack() {
        this.thread.interrupt();
        this.status = "STOPPED";
    }

    @Override
    public void run() {
        try {
            this.processInputSream();
        } catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
        }
    }
}
