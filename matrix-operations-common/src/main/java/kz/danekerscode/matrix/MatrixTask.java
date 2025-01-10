package kz.danekerscode.matrix;

import java.io.Serializable;

public class MatrixTask implements Serializable {
    private String jobId;
    private int rowIndex;
    private int colIndex;
    private double[] rowA;
    private double[] colB;

    public MatrixTask() {
    }

    public MatrixTask(String jobId, int rowIndex, int colIndex, double[] rowA, double[] colB) {
        this.jobId = jobId;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.rowA = rowA;
        this.colB = colB;
    }

    public String getJobId() {
        return jobId;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColIndex() {
        return colIndex;
    }

    public void setColIndex(int colIndex) {
        this.colIndex = colIndex;
    }

    public double[] getRowA() {
        return rowA;
    }

    public void setRowA(double[] rowA) {
        this.rowA = rowA;
    }

    public double[] getColB() {
        return colB;
    }

    public void setColB(double[] colB) {
        this.colB = colB;
    }
}