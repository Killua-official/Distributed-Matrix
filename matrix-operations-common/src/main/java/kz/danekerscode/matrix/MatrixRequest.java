package kz.danekerscode.matrix;

public class MatrixRequest {
    private double[][] matrixA;
    private double[][] matrixB;

    public double[][] getMatrixA() {
        return matrixA;
    }

    public void setMatrixA(double[][] matrixA) {
        this.matrixA = matrixA;
    }

    public double[][] getMatrixB() {
        return matrixB;
    }

    public void setMatrixB(double[][] matrixB) {
        this.matrixB = matrixB;
    }

    public double[][] addMatrices() {
        if (!canAdd()) {
            throw new IllegalArgumentException("Matrices dimensions do not match for addition");
        }
        double[][] result = new double[matrixA.length][matrixA[0].length];
        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixA[0].length; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }
        return result;
    }

    private boolean canAdd() {
        return matrixA.length == matrixB.length && matrixA[0].length == matrixB[0].length;
    }
}

