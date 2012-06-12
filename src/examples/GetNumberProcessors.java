package examples;

/**
 *
 * @author Rafael
 */
public class GetNumberProcessors {

    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        int nrOfProcessors = runtime.availableProcessors();

        System.out.println("Number of processors available to the Java Virtual Machine: "
                + nrOfProcessors);
    }
}
