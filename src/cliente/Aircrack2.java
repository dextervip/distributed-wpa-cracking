package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Rafael
 */
public class Aircrack2 extends Thread {

    private String capPath;
    private String combinationListPath;
    private Process process;
    private InputStream is;
    Matcher m;
    static final String NOT_STARTED = "1";
    static final String PROCESSING = "2";
    static final String FAILED = "3";
    static final String KEY_FOUND = "4";
    static final String KEY_NOT_FOUND = "5";
    private String Status;
    private String currentPassphrase;
    private String currentTime;
    private String currentKeysPerSecond;
    private String KeyFound;

    public Aircrack2() {
        this.Status = Aircrack2.NOT_STARTED;
    }

    public void setCapPath(String capPath) {
        this.capPath = capPath;
    }

    public void setCombinationListPath(String combinationListPath) {
        this.combinationListPath = combinationListPath;
    }

    public void startCrack() throws Exception {
        //process = new ProcessBuilder("aircrack-ng", "--help").start();
        this.process = new ProcessBuilder("aircrack-ng", this.capPath, "-w", this.combinationListPath).start();
        this.is = this.process.getInputStream();
    }

    public void stopCrack() {
        this.process.destroy();
        this.Status = FAILED;
    }

    public String getCurrentPassphrase() {
        return currentPassphrase;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getCurrentKeysPerSecond() {
        return currentKeysPerSecond;
    }

    public String getKeyFound() {
        return KeyFound;
    }

    public String getStatus() {
        return Status;
    }

    public void processInputSream() throws IOException {
        InputStreamReader isr = new InputStreamReader(this.is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            System.out.println("");

            String regex = "(?i)\\QReading packets, please wait...\\E";
            Matcher m = Pattern.compile(regex).matcher(line);
            if (m.find()) {
                this.Status = Aircrack2.PROCESSING;
            }

            regex = "(?i)keys tested\\s\\Q(\\E(?<keys>[\\w\\s\\Q/.\\E]+)\\Q)\\E";
            m = Pattern.compile(regex).matcher(line);
            if (m.find()) {
                this.currentKeysPerSecond = m.group("keys");
            }

            regex = "(?i)\\Q[\\E\\d{2}:\\d{2}:\\d{2}\\Q]\\E";
            m = Pattern.compile(regex).matcher(line);
            if (m.find()) {
                this.currentTime = m.group();
            }

            regex = "(?i)Current\\spassphrase:\\s(?<passphrase>[\\w]+)";
            m = Pattern.compile(regex).matcher(line);
            if (m.find()) {
                this.currentPassphrase = m.group("passphrase");
            }

            regex = "(?i)KEY FOUND!\\s\\Q[\\E\\s([\\w]+)\\s\\Q]\\E";
            m = Pattern.compile(regex).matcher(line);
            if (m.find()) {
                this.KeyFound = m.group();
                this.Status = Aircrack2.KEY_FOUND;
            }

            regex = "(?i)\\bPassphrase not in dictionary\\b";
            m = Pattern.compile(regex).matcher(line);
            if (m.find()) {
                this.Status = Aircrack2.KEY_NOT_FOUND;
            }

        }
    }

    @Override
    public void run() {
        try {
            this.startCrack();
            this.processInputSream();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Aircrack2 c = new Aircrack2();
        c.setCapPath(".\\test\\Private-02.cap");
        c.setCombinationListPath(".\\test\\wordlist_vazia.txt");
        c.start();
//        while(true){
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Aircrack.class.getName()).log(Level.SEVERE, null, ex);
//            }
//            System.out.println(c.Status);
//            System.out.println(c.getCurrentTime());
//            System.out.println(c.getCurrentPassphrase());
//            System.out.println(c.getCurrentKeysPerSecond());
//            System.out.println(c.getKeyFound());
//            if(c.Status == Aircrack.KEY_FOUND){
//                break;
//            }
//        }
//        String line = "[8;27H[2KKEY FOUND! [ engenharia ][11B";
//        String regex = "(?i)KEY FOUND!\\s\\Q[\\E\\s([\\w]+)\\s\\Q]\\E";
//        Matcher m = Pattern.compile(regex).matcher(line);
//        if (m.find()) {
//            System.out.println(m.group());
//        }
//
//        line = "[5;20H[00:00:18] 58128 keys tested (3270.11 k/s)[8;24HCurrent passphrase: paramenta    ";
//        regex = "(?i)keys tested\\s\\Q(\\E(?<keys>[\\w\\s\\Q/.\\E]+)\\Q)\\E";
//        m = Pattern.compile(regex).matcher(line);
//        if (m.find()) {
//            System.out.println(m.group());
//        }
//
//        line = "[5;20H[00:00:18] 58128 keys tested (3270.11 k/s)[8;24HCurrent passphrase: paramenta    ";
//        regex = "(?i)\\Q[\\E\\d{2}:\\d{2}:\\d{2}\\Q]\\E";
//        m = Pattern.compile(regex).matcher(line);
//        if (m.find()) {
//            System.out.println(m.group());
//        }
//
//        line = "[5;20H[00:00:18] 58128 keys tested (3270.11 k/s)[8;24HCurrent passphrase: paramenta12    ";
//        regex = "(?i)Current\\spassphrase:\\s(?<passphrase>[\\w]+)";
//        m = Pattern.compile(regex).matcher(line);
//        if (m.find()) {
//            System.out.println(m.group());
//        }

//        String line = "[2J[2;34HAircrack-ng 1.1\n Passphrase not in dictionary pp";
//        String regex = "\\bPassphrase not in dictionary\\b";
//        Matcher m = Pattern.compile(regex).matcher(line);
//        if (m.find()) {
//            System.out.println("Match: " + m.group());
//        }
//         line = "Opening .\\test\\Private-02.cap\n"
//                + "\n"
//                + "Reading packets, please wait...\n"
//                + "\n"
//                + "[2J[2;34HAircrack-ng 1.1";
//         regex = "(?i)\\QReading packets, please wait...\\E";
//         m = Pattern.compile(regex).matcher(line);
//        if (m.find()) {
//            System.out.println("Match: " + m.group());
//        }


    }
}