package matrix;

public class WinogradScaled {

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        double[] scale = new double[n];

        for (int k = 0; k < n; k++) {
            double normAcol = 0.0;
            double normBrow = 0.0;
            for (int i = 0; i < n; i++) {
                normAcol = Math.max(normAcol, Math.abs(A[i][k]));
            }
            for (int j = 0; j < n; j++) {
                normBrow = Math.max(normBrow, Math.abs(B[k][j]));
            }
            scale[k] = (normAcol > 0.0 && normBrow > 0.0)
                    ? Math.sqrt(normBrow / normAcol)
                    : 1.0;
        }

        double[][] As = new double[n][n];
        double[][] Bs = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {
                As[i][k] = A[i][k] * scale[k];
            }
        }
        for (int k = 0; k < n; k++) {
            for (int j = 0; j < n; j++) {
                Bs[k][j] = B[k][j] / scale[k];
            }
        }

        return WinogradOriginal.multiply(As, Bs);
    }
}
