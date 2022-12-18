package bsu.rfe.java.group7.lab3.Savchuk.varC1;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.io.Console;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.stream.IntStream;

public class GornerTableCellRender implements TableCellRenderer {

    private String requiredValue;

    private boolean coolSearch;

    private JPanel panel = new JPanel();
    private JLabel label = new JLabel();

    private DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance();

    {
        formatter.setMaximumFractionDigits(6);
        formatter.setGroupingUsed(false);
        DecimalFormatSymbols curSymbol = formatter.getDecimalFormatSymbols();
        curSymbol.setDecimalSeparator('.');
        formatter.setDecimalFormatSymbols(curSymbol);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        String formattedValue = formatter.format(value);
        double a = Double.parseDouble(formattedValue);

        label.setText(formattedValue);
        panel.add(label);
        if(a > 0)  panel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        else if(a < 0)  panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        else panel.setLayout(new FlowLayout(FlowLayout.CENTER));


        if (coolSearch && isNearPrime(formattedValue)) panel.setBackground(Color.RED);
        else if (column == 1 && requiredValue != null && requiredValue.equals(formattedValue)) panel.setBackground(Color.RED);
            //else if (requiredValue != null && a != 0 && requiredValue.equals(formattedValue)) panel.setBackground(Color.RED);
        else panel.setBackground(Color.WHITE);

        return panel;
    }

    public void setRequiredValue(String requiredValue) {
        this.requiredValue = requiredValue;
    }

    public void setCoolSearch(boolean coolSearch) {
        this.coolSearch = coolSearch;
    }

    public DecimalFormat getFormatter() {
        return formatter;
    }

    private boolean isNearPrime(String value){
        double num = Double.parseDouble(value);
        for (int i = 2; i < num + 1; i++){
            if (isPrime(i)){
                if (i - 0.1 < num && num < i + 0.1){
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isPrime(final int i) {
        if (i<=1)
            return false;
        else if (i <=3)
            return true;
        else if (i%2==0 || i %3 ==0)
            return false;
        int n = 5;
        while (n*n <=i){
            if (i % n ==0 || i % (n+2) == 0)
                return false;
            n=n+6;
        }
        return true;
    }
}