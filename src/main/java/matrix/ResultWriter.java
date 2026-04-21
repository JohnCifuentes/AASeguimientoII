package matrix;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class ResultWriter {

    private static final String FILE_PATH = "results.csv";
    private static PrintWriter writer;

    static {
        try {
            writer = new PrintWriter(new FileWriter(FILE_PATH, true));
        } catch (IOException e) {
            throw new ExceptionInInitializerError("Cannot open results file: " + e.getMessage());
        }
    }

    /**
     * Appends one result line: algoritmo,n,tiempo
     *
     * @param algorithm  algorithm name
     * @param n          matrix dimension
     * @param timeNanos  elapsed time in nanoseconds
     */
    public static void write(String algorithm, int n, long timeNanos) {
        writer.println(algorithm + "," + n + "," + timeNanos);
        writer.flush();
    }

    public static void close() {
        if (writer != null) {
            writer.close();
        }
    }
}
