
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import servidor.Server;
import cliente.Client;
import java.io.IOException;
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

    /**
     * Test of setCapPath method, of class Aircrack.
     */
    @Test
    public void testInitialState() {
        this.initServer();
        this.s.configureDictionary("abc", 4, 5);
        this.initClient();
        try {
            Thread.sleep(2000);
            if (s.getNumberOfNodes() > 0) {
                Node n = s.getNodes().get(0);
                n.updateStatistics();
                Thread.sleep(4000);
                assertEquals(n.getStatus(), "INIT");
                assertEquals(n.getCurrentKeysPerSecond(), "N/A");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void initServer() {
        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            s.listen();
                        } catch (Exception e) {
                            e.printStackTrace();
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
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }).start();
    }
}
