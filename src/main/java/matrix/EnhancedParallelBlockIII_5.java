package matrix;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

public class EnhancedParallelBlockIII_5 {

    private static final int BLOCK_SIZE = 64;

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        int numBlocks  = (n + BLOCK_SIZE - 1) / BLOCK_SIZE;
        int totalPairs = numBlocks * numBlocks;

        ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        try {
            pool.submit(() ->
                IntStream.range(0, totalPairs).parallel().forEach(idx -> {
                    int bi   = idx / numBlocks;
                    int bj   = idx % numBlocks;
                    int i1   = bi * BLOCK_SIZE;
                    int j1   = bj * BLOCK_SIZE;
                    int iMax = Math.min(i1 + BLOCK_SIZE, n);
                    int jMax = Math.min(j1 + BLOCK_SIZE, n);

                    for (int k1 = 0; k1 < n; k1 += BLOCK_SIZE) {
                        int kMax = Math.min(k1 + BLOCK_SIZE, n);
                        for (int i = i1; i < iMax; i++) {
                            for (int j = j1; j < jMax; j++) {
                                double sum = 0.0;
                                for (int k = k1; k < kMax; k++) {
                                    sum += A[i][k] * B[k][j];
                                }
                                C[i][j] += sum;
                            }
                        }
                    }
                })
            ).get();
        } catch (Exception e) {
            throw new RuntimeException("EnhancedParallelBlockIII_5 failed", e);
        } finally {
            pool.shutdown();
        }
        return C;
    }
}
