package matrix;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.function.Supplier;

@SpringBootApplication
public class ExecutionRunner {

    private static int currentN;

    public static void main(String[] args) {
        int[] sizes = {256, 512};

        for (int n : sizes) {
            currentN = n;
            System.out.println("\n========================================");
            System.out.println(" Matrix size: " + n + " x " + n);
            System.out.println("========================================");

            MatrixRepository.init(n);
            double[][] A = MatrixRepository.getMatrixA();
            double[][] B = MatrixRepository.getMatrixB();

            ejecutar("NaivOnArray",               () -> NaivOnArray.multiply(A, B));
            ejecutar("NaivLoopUnrollingTwo",       () -> NaivLoopUnrollingTwo.multiply(A, B));
            ejecutar("NaivLoopUnrollingFour",      () -> NaivLoopUnrollingFour.multiply(A, B));
            ejecutar("WinogradOriginal",           () -> WinogradOriginal.multiply(A, B));
            ejecutar("WinogradScaled",             () -> WinogradScaled.multiply(A, B));
            ejecutar("StrassenNaiv",               () -> StrassenNaiv.multiply(A, B));
            ejecutar("StrassenWinograd",           () -> StrassenWinograd.multiply(A, B));
            ejecutar("SequentialBlockIII_3",       () -> SequentialBlockIII_3.multiply(A, B));
            ejecutar("ParallelBlockIII_4",         () -> ParallelBlockIII_4.multiply(A, B));
            ejecutar("EnhancedParallelBlockIII_5", () -> EnhancedParallelBlockIII_5.multiply(A, B));
            ejecutar("SequentialBlockIV_3",        () -> SequentialBlockIV_3.multiply(A, B));
            ejecutar("ParallelBlockIV_4",          () -> ParallelBlockIV_4.multiply(A, B));
            ejecutar("EnhancedParallelBlockIV_5",  () -> EnhancedParallelBlockIV_5.multiply(A, B));
            ejecutar("SequentialBlockV_3",         () -> SequentialBlockV_3.multiply(A, B));
            ejecutar("ParallelBlockV_4",           () -> ParallelBlockV_4.multiply(A, B));
        }

        ResultWriter.close();
        System.out.println("\nResults saved to results.csv");
    }

    private static void ejecutar(String nombre, Supplier<double[][]> algoritmo) {
        long start = System.nanoTime();
        double[][] result = algoritmo.get();
        long end   = System.nanoTime();
        long tiempo = end - start;

        System.out.printf("%-35s | n=%4d | %,15d ns%n", nombre, currentN, tiempo);
        ResultWriter.write(nombre, currentN, tiempo);

        // Prevent dead-code elimination by reading one value
        if (result[0][0] == Double.MAX_VALUE) {
            System.out.println("(checksum guard)");
        }
    }
}
