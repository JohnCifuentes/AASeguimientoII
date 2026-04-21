package matrix;

import java.util.stream.IntStream;

public class ParallelBlockV_4 {

    private static final int BLOCK_SIZE = 64;

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        int numColBlocks = (n + BLOCK_SIZE - 1) / BLOCK_SIZE;

        IntStream.range(0, numColBlocks).parallel().forEach(bj -> {
            int j1   = bj * BLOCK_SIZE;
            int jMax = Math.min(j1 + BLOCK_SIZE, n);
            for (int k1 = 0; k1 < n; k1 += BLOCK_SIZE) {
                int kMax = Math.min(k1 + BLOCK_SIZE, n);
                for (int i1 = 0; i1 < n; i1 += BLOCK_SIZE) {
                    int iMax = Math.min(i1 + BLOCK_SIZE, n);
                    for (int j = j1; j < jMax; j++) {
                        for (int k = k1; k < kMax; k++) {
                            double bkj = B[k][j];
                            for (int i = i1; i < iMax; i++) {
                                C[i][j] += A[i][k] * bkj;
                            }
                        }
                    }
                }
            }
        });
        return C;
    }
}
