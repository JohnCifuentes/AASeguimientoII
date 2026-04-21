package matrix;

public class StrassenWinograd {

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

        double[][] S1 = add(A21, A22);
        double[][] S2 = sub(S1,  A11);
        double[][] S3 = sub(A11, A21);
        double[][] S4 = sub(A12, S2);
        double[][] S5 = sub(B12, B11);
        double[][] S6 = sub(B22, S5);
        double[][] S7 = sub(B22, B12);
        double[][] S8 = sub(S6,  B21);

        double[][] P1 = multiply(S2,  S6);
        double[][] P2 = multiply(A11, B11);
        double[][] P3 = multiply(A12, B21);
        double[][] P4 = multiply(S3,  S7);
        double[][] P5 = multiply(S1,  S5);
        double[][] P6 = multiply(S4,  B22);
        double[][] P7 = multiply(A22, S8);

        double[][] T1 = add(P1, P2);
        double[][] T2 = add(T1, P4);

        double[][] C11 = add(P2, P3);
        double[][] C12 = add(add(T1, P5), P6);
        double[][] C21 = sub(T2, P7);
        double[][] C22 = add(T2, P5);

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
