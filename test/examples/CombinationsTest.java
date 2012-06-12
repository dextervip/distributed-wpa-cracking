/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class CombinationsTest {

    public CombinationsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        System.out.println("setUp");
        this.delete();
    }

    @After
    public void tearDown() {
        System.out.println("tearDown");
        //this.delete();
    }

    @Test
    public void testCombinationsNumbers1Part() {
        Combinations c = new Combinations();
        c.setCharacterSet("0123456789");
        c.setMinCharacter(4);
        c.setMaxCharacter(4);
        c.setActualPart(1);
        c.setTotalParts(1);
        c.setPath(".\\tmp\\combinations.txt");
        c.startProceeds();

        assertEquals(this.countLines(".\\tmp\\combinations.txt"), 10000);

    }

    @Test
    public void testCombinationsNumbers3Parts() {
        for (int i = 1; i <= 3; i++) {
            Combinations c = new Combinations();
            c.setCharacterSet("0123456789");
            c.setMinCharacter(4);
            c.setMaxCharacter(4);
            c.setActualPart(i);
            c.setTotalParts(3);
            c.setPath(".\\tmp\\combinations" + i + ".txt");
            c.startProceeds();
        }

        int file1 = this.countLines(".\\tmp\\combinations1.txt");
        int file2 = this.countLines(".\\tmp\\combinations2.txt");
        int file3 = this.countLines(".\\tmp\\combinations3.txt");

        assertEquals(file1, 3333);
        assertEquals(file2, 3333);
        assertEquals(file3, 3334);
        assertEquals(file1 + file2 + file3, 10000);

    }

    @Test
    public void testCombinationsNumbers10Parts() {
        for (int i = 1; i <= 10; i++) {
            Combinations c = new Combinations();
            c.setCharacterSet("0123456789");
            c.setMinCharacter(4);
            c.setMaxCharacter(4);
            c.setActualPart(i);
            c.setTotalParts(10);
            c.setPath(".\\tmp\\combinations" + i + ".txt");
            c.startProceeds();
        }

        int file1 = this.countLines(".\\tmp\\combinations1.txt");
        int file2 = this.countLines(".\\tmp\\combinations2.txt");
        int file3 = this.countLines(".\\tmp\\combinations3.txt");
        int file4 = this.countLines(".\\tmp\\combinations4.txt");
        int file5 = this.countLines(".\\tmp\\combinations5.txt");
        int file6 = this.countLines(".\\tmp\\combinations6.txt");
        int file7 = this.countLines(".\\tmp\\combinations7.txt");
        int file8 = this.countLines(".\\tmp\\combinations8.txt");
        int file9 = this.countLines(".\\tmp\\combinations9.txt");
        int file10 = this.countLines(".\\tmp\\combinations10.txt");

        assertEquals(file1, 1000);
        assertEquals(file2, 1000);
        assertEquals(file3, 1000);
        assertEquals(file4, 1000);
        assertEquals(file5, 1000);
        assertEquals(file6, 1000);
        assertEquals(file7, 1000);
        assertEquals(file8, 1000);
        assertEquals(file9, 1000);
        assertEquals(file10, 1000);
        assertEquals(file1 + file2 + file3 + file4 + file5 + file6 + file7 + file8 + file9 + file10, 10000);
    }

    @Test
    public void testCombinationsLettersNumbers1Part() {
        Combinations c = new Combinations();
        c.setCharacterSet("0123456789abcd");
        c.setMinCharacter(4);
        c.setMaxCharacter(4);
        c.setActualPart(1);
        c.setTotalParts(1);
        c.setPath(".\\tmp\\combinations.txt");
        c.startProceeds();

        assertEquals(this.countLines(".\\tmp\\combinations.txt"), 14 * 14 * 14 * 14);
    }

    @Test
    public void testCombinationsLettersNumbers4Part() {
        for (int i = 1; i <= 4; i++) {
            Combinations c = new Combinations();
            c.setCharacterSet("0123456789abcd");
            c.setMinCharacter(4);
            c.setMaxCharacter(4);
            c.setActualPart(i);
            c.setTotalParts(4);
            c.setPath(".\\tmp\\combinations" + i + ".txt");
            c.startProceeds();
        }

        int file1 = this.countLines(".\\tmp\\combinations1.txt");
        int file2 = this.countLines(".\\tmp\\combinations2.txt");
        int file3 = this.countLines(".\\tmp\\combinations3.txt");
        int file4 = this.countLines(".\\tmp\\combinations4.txt");

        assertEquals(file1, 14 * 14 * 14 * 14 / 4);
        assertEquals(file2, 14 * 14 * 14 * 14 / 4);
        assertEquals(file3, 14 * 14 * 14 * 14 / 4);
        assertEquals(file4, 14 * 14 * 14 * 14 / 4);
        assertEquals(file1 + file2 + file3 + file4, 14 * 14 * 14 * 14);


    }

    @Test
    public void testCombinationsLettersNumbers10Parts() {
        for (int i = 1; i <= 10; i++) {
            Combinations c = new Combinations();
            c.setCharacterSet("0123456789abcdefghij");
            c.setMinCharacter(4);
            c.setMaxCharacter(4);
            c.setActualPart(i);
            c.setTotalParts(10);
            c.setPath(".\\tmp\\combinations" + i + ".txt");
            c.startProceeds();
        }

        int file1 = this.countLines(".\\tmp\\combinations1.txt");
        int file2 = this.countLines(".\\tmp\\combinations2.txt");
        int file3 = this.countLines(".\\tmp\\combinations3.txt");
        int file4 = this.countLines(".\\tmp\\combinations4.txt");
        int file5 = this.countLines(".\\tmp\\combinations5.txt");
        int file6 = this.countLines(".\\tmp\\combinations6.txt");
        int file7 = this.countLines(".\\tmp\\combinations7.txt");
        int file8 = this.countLines(".\\tmp\\combinations8.txt");
        int file9 = this.countLines(".\\tmp\\combinations9.txt");
        int file10 = this.countLines(".\\tmp\\combinations10.txt");
        System.out.println("Total de combinações: " + ((int) Math.pow(20, 4)));
        assertEquals(file1, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file2, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file3, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file4, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file5, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file6, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file7, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file8, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file9, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file10, ((int) Math.pow(20, 4)) / 10);
        assertEquals(file1 + file2 + file3 + file4 + file5 + file6 + file7 + file8 + file9 + file10, (int) Math.pow(20, 4));

    }

    @Test
    public void testCombinationsLettersNumbers200Parts() {
        for (int i = 1; i <= 200; i++) {
            Combinations c = new Combinations();
            c.setCharacterSet("0123456789abcdefghij");
            c.setMinCharacter(4);
            c.setMaxCharacter(4);
            c.setActualPart(i);
            c.setTotalParts(200);
            c.setPath(".\\tmp\\combinations" + i + ".txt");
            c.startProceeds();
        }
        int soma = 0;
        for (int i = 1; i <= 200; i++) {
            int file1 = this.countLines(".\\tmp\\combinations" + i + ".txt");
            soma += file1;
            assertEquals(file1, ((int) Math.pow(20, 4)) / 200);
        }
        assertEquals(soma, (int) Math.pow(20, 4));

    }

    private int countLines(String path) {
        LineNumberReader lnr;
        try {
            lnr = new LineNumberReader(new FileReader(new File(path)));
            lnr.skip(Long.MAX_VALUE);
            int lines = lnr.getLineNumber();
            lnr.close();
            return lines;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return -1;
    }

    private void delete() {
        File files[] = new File(".\\tmp\\").listFiles();
        for (File file : files) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }
}
