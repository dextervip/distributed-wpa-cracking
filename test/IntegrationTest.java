
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

    @After
    public void tearDown() {
    }

    @Test
    public void testInitialState() {
        try {
            this.initServer();
            this.initClient();
            this.s.configureDictionary("abc", 4, 5);
            //Espera para o cliente se conectar ao servidor
            Thread.sleep(2000);
            Node n = s.getNodes().get(0);
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
            Node n = s.getNodes().get(0);
            assertEquals("WAITING", n.getStatus());

            //deleta o arquivo cap recebido
            File f = new File("received.cap");
            if (f.exists()) {
                Logger.getLogger(IntegrationTest.class.getName()).log(Level.INFO, "Detelting {0}", f.getAbsolutePath());
                f.deleteOnExit();
            }
        } catch (Exception ex) {
            Logger.getLogger(IntegrationTest.class.getName()).log(Level.SEVERE, null, ex);
            fail(ex.getMessage());
        }

    }

    @Test
    public void testSendCapFile() {
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
}
