package matrix;

public class SequentialBlockIII_3 {

    private static final int BLOCK_SIZE = 64;

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i1 = 0; i1 < n; i1 += BLOCK_SIZE) {
            int iMax = Math.min(i1 + BLOCK_SIZE, n);
            for (int j1 = 0; j1 < n; j1 += BLOCK_SIZE) {
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
            }
        }
        return C;
    }
}
