/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package examples;

/**
 *
 * @author Rafael
 */
public class CombinationTest {

    public static void main(String[] args) {

        Combinations c = new Combinations();
        c.setCharacterSet("0123456789");
        c.setMinCharacter(8);
        c.setMaxCharacter(8);
        c.setActualPart(1);
        c.setTotalParts(1);
        c.setPath(".\\tmp\\combinations.txt");
        c.startProceeds();

    }
}
