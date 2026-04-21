package matrix;

public class NaivLoopUnrollingFour {

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = 0.0;
                int k = 0;
                for (; k <= n - 4; k += 4) {
                    sum += A[i][k]     * B[k][j]
                         + A[i][k + 1] * B[k + 1][j]
                         + A[i][k + 2] * B[k + 2][j]
                         + A[i][k + 3] * B[k + 3][j];
                }
                for (; k < n; k++) {
                    sum += A[i][k] * B[k][j];
                }
                C[i][j] = sum;
            }
        }
        return C;
    }
}
