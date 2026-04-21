package matrix;

import java.util.Random;

public class MatrixRepository {

    private static double[][] A;
    private static double[][] B;
    private static int currentSize;

    public static void init(int n) {
        currentSize = n;
        A = new double[n][n];
        B = new double[n][n];

        Random rng = new Random(System.currentTimeMillis());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                A[i][j] = 100_000 + rng.nextInt(900_000);
                B[i][j] = 100_000 + rng.nextInt(900_000);
            }
        }
    }

    public static double[][] getMatrixA() {
        return A;
    }

    public static double[][] getMatrixB() {
        return B;
    }

    public static int getCurrentSize() {
        return currentSize;
    }
}
