package matrix;

public class StrassenNaiv {

    private static final int THRESHOLD = 64;

    public static double[][] multiply(double[][] A, double[][] B) {
        int n = A.length;
        if (n <= THRESHOLD) {
            return naiv(A, B);
        }
        int h = n / 2;

        double[][] A11 = extract(A, 0, 0, h);
        double[][] A12 = extract(A, 0, h, h);
        double[][] A21 = extract(A, h, 0, h);
        double[][] A22 = extract(A, h, h, h);
        double[][] B11 = extract(B, 0, 0, h);
        double[][] B12 = extract(B, 0, h, h);
        double[][] B21 = extract(B, h, 0, h);
        double[][] B22 = extract(B, h, h, h);

        double[][] P1 = multiply(A11,               sub(B12, B22));
        double[][] P2 = multiply(add(A11, A12),     B22);
        double[][] P3 = multiply(add(A21, A22),     B11);
        double[][] P4 = multiply(A22,               sub(B21, B11));
        double[][] P5 = multiply(add(A11, A22),     add(B11, B22));
        double[][] P6 = multiply(sub(A12, A22),     add(B21, B22));
        double[][] P7 = multiply(sub(A11, A21),     add(B11, B12));

        double[][] C11 = add(sub(add(P5, P4), P2), P6);
        double[][] C12 = add(P1, P2);
        double[][] C21 = add(P3, P4);
        double[][] C22 = add(add(sub(P5, P3), P1), P7);

        return combine(C11, C12, C21, C22, n);
    }

    private static double[][] naiv(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double s = 0.0;
                for (int k = 0; k < n; k++) s += A[i][k] * B[k][j];
                C[i][j] = s;
            }
        }
        return C;
    }

    private static double[][] extract(double[][] M, int r, int c, int size) {
        double[][] sub = new double[size][size];
        for (int i = 0; i < size; i++)
            System.arraycopy(M[r + i], c, sub[i], 0, size);
        return sub;
    }

    private static double[][] add(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] + B[i][j];
        return C;
    }

    private static double[][] sub(double[][] A, double[][] B) {
        int n = A.length;
        double[][] C = new double[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                C[i][j] = A[i][j] - B[i][j];
        return C;
    }

    private static double[][] combine(double[][] C11, double[][] C12,
                                      double[][] C21, double[][] C22, int n) {
        double[][] C = new double[n][n];
        int h = n / 2;
        for (int i = 0; i < h; i++) {
            System.arraycopy(C11[i], 0, C[i],     0, h);
            System.arraycopy(C12[i], 0, C[i],     h, h);
            System.arraycopy(C21[i], 0, C[i + h], 0, h);
            System.arraycopy(C22[i], 0, C[i + h], h, h);
        }
        return C;
    }
}
