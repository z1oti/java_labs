package bsu.rfe.java.group7.lab3.Savchuk.varC1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class GornerTableFrame extends JFrame {

    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private double[] coefficients;

    private JFileChooser fileChooser = null;

    private JMenuItem saveToTxtMI;
    private JMenuItem saveToBinMI;
    private JMenuItem saveToCsvMI;
    private JMenuItem searchValueMI;
    private JMenuItem searchValueFromRangeMI;
    private JMenuItem showInfoMI;

    private JTextField xBegTF;
    private JTextField xEndTF;
    private JTextField stepTF;

    private Box hResultTableBox;

    private GornerTableCellRender renderer = new GornerTableCellRender();

    private GornerTableModel dataTable;

    GornerTableFrame(double[] coefficients) {
        super("ТАБУЛИРОВАНИЕ ПО СХЕМЕ ГОРНЕРА");
        this.coefficients = coefficients;
        setSize(WIDTH, HEIGHT);

        Toolkit tk = Toolkit.getDefaultToolkit();
        setLocation((tk.getScreenSize().width - WIDTH) / 2, (tk.getScreenSize().height - HEIGHT) / 2);

        constructMenu();
        constructTop();
        constructMid();
        constructBot();
    }

    private void constructMenu() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenu tableMenu = new JMenu("Таблица");
        JMenu infoMenu = new JMenu("Справка");

        menuBar.add(fileMenu);
        menuBar.add(tableMenu);
        menuBar.add(infoMenu);

        Action saveToTxtAction = new AbstractAction("Сохранить в .txt") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(GornerTableFrame.this) == JFileChooser.APPROVE_OPTION) {
                    saveToTxt(new File(fileChooser.getSelectedFile().getName().concat(".txt")));
                }
            }
        };
        saveToTxtMI = fileMenu.add(saveToTxtAction);
        saveToTxtMI.setEnabled(false);

        Action saveToBinAction = new AbstractAction("Сохранить в .bin") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(GornerTableFrame.this) == JFileChooser.APPROVE_OPTION) {
                    saveToBin(new File(fileChooser.getSelectedFile().getName().concat(".bin")));
                }
            }
        };
        saveToBinMI = fileMenu.add(saveToBinAction);
        saveToBinMI.setEnabled(false);

        Action saveToCsvAction = new AbstractAction("Сохранить в .csv") {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(GornerTableFrame.this) == JFileChooser.APPROVE_OPTION) {
                    saveToCsv(new File(fileChooser.getSelectedFile().getName().concat(".csv")));
                }
            }
        };
        saveToCsvMI = fileMenu.add(saveToCsvAction);
        saveToCsvMI.setEnabled(false);

        Action searchValueAction = new AbstractAction("Найти значение") {
            @Override
            public void actionPerformed(ActionEvent e) {
                String value = JOptionPane.showInputDialog(GornerTableFrame.this, "Введите значение", "Поиск", JOptionPane.QUESTION_MESSAGE);
                try {
                    Double test = Double.parseDouble(value);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(GornerTableFrame.this, "Неверные данные", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                renderer.setRequiredValue(value);
                renderer.setCoolSearch(false);
                repaint();
            }
        };
        searchValueMI = tableMenu.add(searchValueAction);
        searchValueMI.setEnabled(false);

        Action searchValueFromRangeAction = new AbstractAction("Найти близкие к простым") {
            @Override
            public void actionPerformed(ActionEvent e) {
                renderer.setCoolSearch(true);
                renderer.setRequiredValue(null);
                repaint();
            }
        };
        searchValueFromRangeMI = tableMenu.add(searchValueFromRangeAction);
        searchValueFromRangeMI.setEnabled(false);

        Action showInfoAction = new AbstractAction("О программе") {
            @Override
            public void actionPerformed(ActionEvent e) {
                ImageIcon icon = new ImageIcon(new ImageIcon(GornerTableFrame.class.getResource("photo.jpg")).getImage().getScaledInstance(500, 500, Image.SCALE_DEFAULT));
                JOptionPane.showMessageDialog(GornerTableFrame.this, "Савчук Злата \n7 группа \nДля связи tg: z1otya", "Справка", JOptionPane.PLAIN_MESSAGE, icon);
            }
        };
        showInfoMI = infoMenu.add(showInfoAction);

        setJMenuBar(menuBar);
    }

    private void constructTop() {
        JLabel xBegLabel = new JLabel("X от: ");
        xBegTF = new JTextField("0.0", 15);
        xBegTF.setMaximumSize(xBegTF.getPreferredSize());

        JLabel xEndLabel = new JLabel("до: ");
        xEndTF = new JTextField("1.0", 15);
        xEndTF.setMaximumSize(xEndTF.getPreferredSize());

        JLabel stepLabel = new JLabel("с шагом: ");
        stepTF = new JTextField("0.1", 15);
        stepTF.setMaximumSize(stepTF.getPreferredSize());

        Box hBoxTextFields = Box.createHorizontalBox();
        //hBoxTextFields.setBorder(BorderFactory.createBevelBorder(1));
        hBoxTextFields.add(Box.createHorizontalGlue());
        hBoxTextFields.add(xBegLabel);
        hBoxTextFields.add(Box.createHorizontalStrut(10));
        hBoxTextFields.add(xBegTF);
        hBoxTextFields.add(Box.createHorizontalStrut(30));
        hBoxTextFields.add(xEndLabel);
        hBoxTextFields.add(Box.createHorizontalStrut(10));
        hBoxTextFields.add(xEndTF);
        hBoxTextFields.add(Box.createHorizontalStrut(30));
        hBoxTextFields.add(stepLabel);
        hBoxTextFields.add(Box.createHorizontalStrut(10));
        hBoxTextFields.add(stepTF);
        hBoxTextFields.add(Box.createHorizontalGlue());

        hBoxTextFields.setPreferredSize(new Dimension(
                (int) hBoxTextFields.getMaximumSize().getWidth(),
                (int) hBoxTextFields.getMinimumSize().getHeight() * 2));

        add(hBoxTextFields, BorderLayout.SOUTH);
    }

    private void constructMid() {
        hResultTableBox = Box.createHorizontalBox();
        hResultTableBox.add(new JPanel());
        add(hResultTableBox, BorderLayout.CENTER);
    }

    private void constructBot() {
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double xBeg = Double.parseDouble(xBegTF.getText());
                    double xEnd = Double.parseDouble(xEndTF.getText());
                    double step = Double.parseDouble(stepTF.getText());

                    if (xBeg > xEnd) {
                        double tmp = xBeg;
                        xBeg = xEnd;
                        xEnd = tmp;
                        xBegTF.setText(String.valueOf(xBeg));
                        xEndTF.setText(String.valueOf(xEnd));
                    }

                    if (step <= 0) {
                        throw new NumberFormatException();
                    }

                    dataTable = new GornerTableModel(xBeg, xEnd, step, coefficients);
                    JTable table = new JTable(dataTable);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);
                    hResultTableBox.removeAll();
                    hResultTableBox.add(new JScrollPane(table));
                    validate();

                    saveToTxtMI.setEnabled(true);
                    saveToBinMI.setEnabled(true);
                    saveToCsvMI.setEnabled(true);
                    searchValueMI.setEnabled(true);
                    searchValueFromRangeMI.setEnabled(true);
                } catch (NumberFormatException exception) {
                    JOptionPane.showMessageDialog(GornerTableFrame.this, "Неверные данные", "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        JButton resetButton = new JButton("Очистить");
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                xBegTF.setText("0.0");
                xEndTF.setText("1.0");
                stepTF.setText("0.1");
                hResultTableBox.removeAll();
                hResultTableBox.add(new JPanel());
                saveToTxtMI.setEnabled(false);
                saveToBinMI.setEnabled(false);
                saveToCsvMI.setEnabled(false);
                searchValueMI.setEnabled(false);
                searchValueFromRangeMI.setEnabled(false);
                renderer.setRequiredValue(null);
                renderer.setCoolSearch(false);
                validate();
            }
        });

        Box hBoxButtons = Box.createHorizontalBox();
        //hBoxButtons.setBorder(BorderFactory.createBevelBorder(1));
        hBoxButtons.add(Box.createHorizontalGlue());
        hBoxButtons.add(buttonCalc);
        hBoxButtons.add(Box.createHorizontalStrut(30));
        hBoxButtons.add(resetButton);
        hBoxButtons.add(Box.createHorizontalGlue());
        hBoxButtons.setPreferredSize(new Dimension(
                (int) hBoxButtons.getMaximumSize().getWidth(),
                (int) hBoxButtons.getMinimumSize().getHeight() * 2
        ));

        add(hBoxButtons, BorderLayout.NORTH);
    }

    void saveToTxt(File file) {
        try {
            PrintStream out = new PrintStream(file);
            out.println("Результаты табулирования по схеме Горнера");
            out.print("Многочлен: ");
            for (int i = 0; i < coefficients.length; i++) {
                out.print(coefficients[i] + "*x^" + i);
                if (i != coefficients.length - 1) {
                    out.print(" + ");
                }
            }
            out.println("");
            out.println("Интервал от " + renderer.getFormatter().format(dataTable.getXBeg()) + " до " + renderer.getFormatter().format(dataTable.getXEnd()) + " с шагом " + renderer.getFormatter().format(dataTable.getStep()));
            out.println("\n\n");
            for (int i = 0; i < dataTable.getRowCount(); i++) {
                out.println("F(" + renderer.getFormatter().format(dataTable.getValueAt(i, 0)) + ") = " + renderer.getFormatter().format(dataTable.getValueAt(i, 1)) + " или " + renderer.getFormatter().format(dataTable.getValueAt(i, 2)) + ", с разницей " + renderer.getFormatter().format(dataTable.getValueAt(i, 3)));
            }
            out.close();
        } catch (FileNotFoundException ignore) {

        }
    }

    void saveToBin(File file) {
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            for (int i = 0; i < dataTable.getRowCount(); i++) {
                out.writeDouble((Double) dataTable.getValueAt(i, 0));
                out.writeDouble((Double) dataTable.getValueAt(i, 1));
            }
            out.close();
        } catch (Exception ignore) {

        }
    }

    void saveToCsv(File file) {
        try {
            PrintStream out = new PrintStream(file);
            for (int i = 0; i < dataTable.getRowCount(); i++) {
                out.print(dataTable.getValueAt(i, 0) + ";");
                out.print(dataTable.getValueAt(i, 1) + ";");
                out.print(dataTable.getValueAt(i, 2) + ";");
                out.println(dataTable.getValueAt(i, 3));
            }
            out.close();
        } catch (FileNotFoundException ignore) {

        }
    }
}