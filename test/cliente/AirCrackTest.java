/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Rafael
 */
public class AirCrackTest {

    public AirCrackTest() {
    }

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
     * Test cracking with non-existing cap file path
     */
    @Test
    public void testAircrackWrongPath() {
        try {
            Aircrack c = new Aircrack();
            c.setCapPath("Private-02.cap");
            c.setCombinationPath("wordlist_pt_br_final.txt");
            c.startCrack();
            assertEquals(c.getStatus(), "ERROR");
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
    }
    /**
     * Test a successful cracking
     */
    @Test
    public void testAircrackKeyFound() {
        try {
            Aircrack c = new Aircrack();
            c.setCapPath(".\\test\\Private-01.cap");
            c.setCombinationPath(".\\test\\combinations1.txt");
            c.startCrack();
            Thread.sleep(2000);
            assertEquals("KEY_FOUND", c.getStatus());
            assertEquals("81579997", c.getKeyFound());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        
    }
}
