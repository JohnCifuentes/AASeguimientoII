package matrix;

public class WinogradOriginal {

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        double[] rowFactor = new double[n];
        double[] colFactor = new double[n];
        int half = n / 2;

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < half; k++) {
                rowFactor[i] += A[i][2 * k] * A[i][2 * k + 1];
            }
        }

        for (int j = 0; j < n; j++) {
            for (int k = 0; k < half; k++) {
                colFactor[j] += B[2 * k][j] * B[2 * k + 1][j];
            }
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double sum = -rowFactor[i] - colFactor[j];
                for (int k = 0; k < half; k++) {
                    sum += (A[i][2 * k] + B[2 * k + 1][j])
                         * (A[i][2 * k + 1] + B[2 * k][j]);
                }
                C[i][j] = sum;
            }
        }

        if (n % 2 != 0) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    C[i][j] += A[i][n - 1] * B[n - 1][j];
                }
            }
        }
        return C;
    }
}
