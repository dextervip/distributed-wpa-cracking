package examples;

import java.text.DecimalFormat;

/**
 *
 * @author Rafael
 */
public class Memory {

    public void getTotalMemoryJVM() {
        DecimalFormat df = new DecimalFormat("0.00");
        long totalMem = Runtime.getRuntime().totalMemory();
        System.out.println(df.format(totalMem / 1000000F) + " MB");
    }

    public void maxMemory() {
        DecimalFormat df = new DecimalFormat("0.00");
        long maxMem = Runtime.getRuntime().maxMemory();
        System.out.println(df.format(maxMem / 1000000F) + " MB");
    }

    public void freeMemoryJVM() {
        DecimalFormat df = new DecimalFormat("0.00");
        long freeMem = Runtime.getRuntime().freeMemory();
        System.out.println(df.format(freeMem / 1000000F) + " MB");
    }

    public static void main(String[] args) {
        Memory m = new Memory();
        m.getTotalMemoryJVM();
        m.maxMemory();
        m.freeMemoryJVM();
    }
}
