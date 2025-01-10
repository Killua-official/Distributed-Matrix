package kz.danekerscode.matrix;

import java.io.Serializable;

public class MatrixResult implements Serializable {
    private String jobId;
    private int rowIndex;
    private int colIndex;
    private double value;

    public MatrixResult() {
    }

    public MatrixResult(String jobId, int rowIndex, int colIndex, double value) {
        this.jobId = jobId;
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
        this.value = value;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getJobId() {
        return jobId;
    }
}
