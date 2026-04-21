package matrix;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class EnhancedParallelBlockIV_5 {

    private static final int BLOCK_SIZE = 64;

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        int numRowBlocks = (n + BLOCK_SIZE - 1) / BLOCK_SIZE;

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        try {
            pool.submit(() ->
                IntStream.range(0, numRowBlocks).parallel().forEach(bi -> {
                    int i1   = bi * BLOCK_SIZE;
                    int iMax = Math.min(i1 + BLOCK_SIZE, n);
                    for (int k1 = 0; k1 < n; k1 += BLOCK_SIZE) {
                        int kMax = Math.min(k1 + BLOCK_SIZE, n);
                        for (int j1 = 0; j1 < n; j1 += BLOCK_SIZE) {
                            int jMax = Math.min(j1 + BLOCK_SIZE, n);
                            for (int i = i1; i < iMax; i++) {
                                for (int k = k1; k < kMax; k++) {
                                    double aik = A[i][k];
                                    for (int j = j1; j < jMax; j++) {
                                        C[i][j] += aik * B[k][j];
                                    }
                                }
                            }
                        }
                    }
                })
            ).get();
        } catch (Exception e) {
            throw new RuntimeException("EnhancedParallelBlockIV_5 failed", e);
        } finally {
            pool.shutdown();
        }
        return C;
    }
}
