package matrix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class MatrixRepository {

    /** Directory (relative to working directory) where CSV files are stored. */
    private static final String DATA_DIR = "data";

    /**
     * Fixed seed used only when a CSV file does not yet exist.
     * Once the file is written, the seed is irrelevant – the file is the source of truth.
     */
    private static final long FIXED_SEED = 123_456_789L;

    private static double[][] A;
    private static double[][] B;
    private static int currentSize;

    // ------------------------------------------------------------------
    // Public API
    // ------------------------------------------------------------------

    public static void init(int n) {
        currentSize = n;

        Path dir   = Paths.get(DATA_DIR);
        Path fileA = dir.resolve("matrix_A_" + n + ".csv");
        Path fileB = dir.resolve("matrix_B_" + n + ".csv");

        try {
            Files.createDirectories(dir);

            if (Files.exists(fileA) && Files.exists(fileB)) {
                System.out.println("[MatrixRepository] Loading matrices from files for n=" + n);
                A = loadMatrix(fileA, n);
                B = loadMatrix(fileB, n);
            } else {
                System.out.println("[MatrixRepository] Generating and saving matrices for n=" + n);
                generateAndSave(n, fileA, fileB);
            }
        } catch (IOException e) {
            throw new RuntimeException(
                    "MatrixRepository: error accessing data files – " + e.getMessage(), e);
        }
    }

    public static double[][] getMatrixA() {
        return A;
    }

    public static double[][] getMatrixB() {
        return B;
    }

    // ------------------------------------------------------------------
    // Private helpers
    // ------------------------------------------------------------------

    /** Generates A and B from a fixed seed and persists them as CSV. */
    private static void generateAndSave(int n, Path fileA, Path fileB) throws IOException {
        Random rng = new Random(FIXED_SEED);

        double[][] matA = new double[n][n];
        double[][] matB = new double[n][n];

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matA[i][j] = 100_000 + rng.nextInt(900_000);

        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                matB[i][j] = 100_000 + rng.nextInt(900_000);

        saveMatrix(matA, fileA);
        saveMatrix(matB, fileB);

        A = matA;
        B = matB;
    }

    /** Writes a matrix as a CSV file – one row per line, values separated by commas. */
    private static void saveMatrix(double[][] M, Path path) throws IOException {
        int n = M.length;
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            for (int i = 0; i < n; i++) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < n; j++) {
                    if (j > 0) sb.append(',');
                    sb.append((long) M[i][j]);
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    /** Reads a matrix from a CSV file produced by {@link #saveMatrix}. */
    private static double[][] loadMatrix(Path path, int n) throws IOException {
        double[][] M = new double[n][n];
        try (BufferedReader br = Files.newBufferedReader(path)) {
            for (int i = 0; i < n; i++) {
                String line = br.readLine();
                if (line == null) {
                    throw new IOException(
                            "Matrix file has fewer rows than expected (" + n + "): " + path);
                }
                String[] parts = line.split(",");
                if (parts.length != n) {
                    throw new IOException(
                            "Row " + i + " has " + parts.length + " columns, expected " + n
                            + " in: " + path);
                }
                for (int j = 0; j < n; j++) {
                    M[i][j] = Double.parseDouble(parts[j].trim());
                }
            }
        }
        return M;
    }
}

