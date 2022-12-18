package bsu.rfe.java.group7.lab5.Savchuk.varC;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private boolean fileLoaded = false;
    private JFileChooser fileChooser = null;
    private JCheckBoxMenuItem showAxisMenuItem;
    private JCheckBoxMenuItem showMarkersMenuItem;
    private JCheckBoxMenuItem reformCoordinateItem;
    private GraphicsDisplay display = new GraphicsDisplay();
    private JCheckBoxMenuItem showGridItem;

    public MainFrame() {
        super("График");
        setSize(WIDTH,HEIGHT);
        Dimension ss= new Dimension();
        ss.height=60;
        ss.width=40;
        Toolkit kit=Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2,(kit.getScreenSize().height - HEIGHT)/2);
        fileChooser=new JFileChooser();
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser==null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) ==
                        JFileChooser.APPROVE_OPTION)
                    openGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(openGraphicsAction);
        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        Action saveGraphicsAction = new AbstractAction("Сохранить файл с графиком") {
            public void actionPerformed(ActionEvent event) {
                fileChooser.setCurrentDirectory(new File("~"));
                if (fileChooser.showSaveDialog(MainFrame.this)==JFileChooser.APPROVE_OPTION)
                    saveGraphics(fileChooser.getSelectedFile());
            }
        };
        fileMenu.add(saveGraphicsAction);

        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            public void actionPerformed(ActionEvent event) {
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };
        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true);
        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);

        Action reformCoordinateAction = new AbstractAction("Преобразовать координаты") {
            public void actionPerformed(ActionEvent event) {
                display.setTransform(reformCoordinateItem.isSelected());
            }
        };
        reformCoordinateItem = new JCheckBoxMenuItem(reformCoordinateAction);
        graphicsMenu.add(reformCoordinateItem);
        reformCoordinateItem.setSelected(false);
        Action showGridAction = new AbstractAction("Показывать сетку") {
            public void actionPerformed(ActionEvent event) {
                display.setShowGrid(showGridItem.isSelected());
            }
        };
        showGridItem = new JCheckBoxMenuItem(showGridAction);
        graphicsMenu.add(showGridItem);
        showGridItem.setSelected(true);

        graphicsMenu.addMenuListener(new GraphicsMenuListener());
        getContentPane().add(display, BorderLayout.CENTER);
    }

    protected void saveGraphics(File selectedFile) {
        File file=fileChooser.getSelectedFile();
        try {
            DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
            for (int i = 0; i<display.getDataLenght(); i++) {
                out.writeDouble(display.getValue(i,0));
                out.writeDouble(display.getValue(i,1));
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Не удалость создать файл");
        }
    }

    protected void openGraphics(File selectedFile) {
        try {
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));
            double[][] graphicsData = new double[in.available()/(Double.SIZE/8)/2][];
            int i = 0;
            while (in.available()>0) {
                Double x = in.readDouble();
                Double y = in.readDouble();
                graphicsData[i++] = new double[] {x, y};
            }
            if (graphicsData!=null && graphicsData.length>0) {
                fileLoaded = true;
                display.showGraphics(graphicsData);
                display.repaint();
            }
            in.close();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Указанный файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения координат точек из файла", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private class GraphicsMenuListener implements MenuListener {
        public void menuSelected(MenuEvent e) {
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showGridItem.setEnabled(fileLoaded);
            reformCoordinateItem.setEnabled(fileLoaded);
        }

        public void menuDeselected(MenuEvent e) {
        }

        public void menuCanceled(MenuEvent e) {
        }
    }
}