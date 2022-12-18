package bsu.rfe.java.group7.lab3.Savchuk.varC1;

import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {

    private double[] coefficients;
    private double xBeg;
    private double xEnd;
    private double step;

    public GornerTableModel(double xBeg, double xEnd, double step, double[] coefficients) {
        this.xBeg = xBeg;
        this.xEnd = xEnd;
        this.step = step;
        this.coefficients = coefficients;
    }

    @Override
    public int getRowCount() {
        return (int) (Math.ceil((xEnd - xBeg) / step) + 1);
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        double x = xBeg + rowIndex * step;
        double result = 0;
        double alternativeResult = 0;

        if (columnIndex == 1 || columnIndex == 3) {
            int i = coefficients.length - 1;
            result = coefficients[i--];
            while (i >= 0) {
                result = result * x + coefficients[i--];
            }
        }

        if (columnIndex == 2 || columnIndex == 3) {
            int i = coefficients.length - 1;
            alternativeResult = 0;
            while (i >= 0) {
                alternativeResult += coefficients[i] * Math.pow(x, i);
                i--;
            }
        }

        switch (columnIndex) {
            case 0: {
                return x;
            }
            case 1: {
                return result;
            }
            case 2: {
                return alternativeResult;
            }
            case 3: {
                return Math.abs(result - alternativeResult);
            }
            default: {
                return 0;
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0: {
                return "Значение X";
            }
            case 1: {
                return "Значение многочлена по схеме Горнера";
            }
            case 2: {
                return "Значение многочлена возведением в степень";
            }
            case 3: {
                return "Разница";
            }
            default: {
                return "";
            }
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Double.class;
    }

    public double getXBeg() {
        return xBeg;
    }

    public double getXEnd() {
        return xEnd;
    }

    public double getStep() {
        return step;
    }
}