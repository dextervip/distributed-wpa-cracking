
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import servidor.Server;
import cliente.Client;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.Node;

/**
 *
 * @author Rafael
 */
public class IntegrationTest {

    private Server s = new Server();
    private Client c = new Client();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @Test
    public void testInitialState() {
        try {
            this.initServer();
            this.initClient();
            this.s.configureDictionary("81579", 8, 8);
            //Espera para o cliente se conectar ao servidor
            Thread.sleep(2000);
            Node n = s.getNodes().get(0);
            n.updateStatus();
            n.updateStatistics();
            //Esperar atualização de estatisticas
            Thread.sleep(2000);
            assertEquals(n.getStatus(), "INIT");
            assertEquals(n.getCurrentKeysPerSecond(), "N/A");
            assertEquals(n.getCurrentPassphrase(), "N/A");
            assertEquals(n.getCurrentTime(), "N/A");
        } catch (Exception ex) {
            Logger.getLogger(IntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

        try {
            this.s.loadCap(".\\test\\Private-01.cap");
            this.s.startCrack();
            Thread.sleep(3000);
            Node node = s.getNodes().get(0);
            assertEquals("WAITING", node.getStatus());
            while (node.getStatus().equalsIgnoreCase("KEY_FOUND") == false) {
                Thread.sleep(3000);
                node.updateStatus();
            }
            node.updateKeyFound();
            Thread.sleep(2000);
            assertEquals("KEY_FOUND", node.getStatus());
            assertEquals("81579997", node.getKeyFound());

        } catch (Exception ex) {
            Logger.getLogger(IntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

    }

    public void initServer() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            s.listen();
                        } catch (Exception ex) {
                            Logger.getLogger(IntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }).start();
    }

    public void initClient() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        c.connect();
                        while (true) {
                            try {
                                c.process(c.receive());
                                Thread.sleep(1000);
                            } catch (Exception ex) {
                                Logger.getLogger(IntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                }).start();
    }

    @After
    public void tearDown() {
        //deleta o arquivo cap recebido
        File f = new File("received.cap");
        if (f.exists()) {
            Logger.getLogger(IntegrationTest.class.getName()).log(Level.INFO, "Detelting {0}", f.getAbsolutePath());
            f.deleteOnExit();
        }
        f = new File("combinations1.txt");
        if (f.exists()) {
            Logger.getLogger(IntegrationTest.class.getName()).log(Level.INFO, "Detelting {0}", f.getAbsolutePath());
            f.deleteOnExit();
        }
    }
}
