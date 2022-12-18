package bsu.rfe.java.group7.lab4.Savchuk.varA3;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

@SuppressWarnings("serial")
public class MainFrame extends JFrame {

    // Начальные размеры окна приложения
    private static final int WIDTH = 800, HEIGHT = 600;

    // Объект диалогового окна для выбора файлов
    private JFileChooser fileChooser = null;

    // Пункты меню
    private JCheckBoxMenuItem showAxisMenuItem, showMarkersMenuItem, showLevelsMenuItem;

    // Компонент-отображатель графика
    private GraphicsDisplay display = new GraphicsDisplay();

    // Флаг, указывающий на загруженность данных графика
    private boolean fileLoaded = false;

    public MainFrame() {

        // Вызов конструктора предка Frame
        super("Построение графиков функций на основе заранее подготовленных файлов");

        // Установка размеров окна
        setSize(WIDTH, HEIGHT);
        Toolkit kit = Toolkit.getDefaultToolkit();

        // Отцентрировать окно приложения на экране
        setLocation((kit.getScreenSize().width - WIDTH) / 2, (kit.getScreenSize().height - HEIGHT) / 2);

        // Развёртывание окна на весь экран
        setExtendedState(MAXIMIZED_BOTH);

        // Создать и установить полосу меню
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Добавить пункт меню "Файл"
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);

        // Создать действие по открытию файла
        Action openGraphicsAction = new AbstractAction("Открыть файл с графиком") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showOpenDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    openGraphics(fileChooser.getSelectedFile());
                }
            }
        };

        // Добавить соответствующий элемент меню
        fileMenu.add(openGraphicsAction);

        // Создать пункт меню "График"
        JMenu graphicsMenu = new JMenu("График");
        menuBar.add(graphicsMenu);

        // Создать действие для реакции на активацию элемента "Показывать оси координат"
        Action showAxisAction = new AbstractAction("Показывать оси координат") {
            public void actionPerformed(ActionEvent event) {
                // Свойство showAxis класса GraphicsDisplay истина, если элемент меню
                // showAxisMenuItem отмечен флажком, и ложь - в противном случае
                display.setShowAxis(showAxisMenuItem.isSelected());
            }
        };

        showAxisMenuItem = new JCheckBoxMenuItem(showAxisAction);
        graphicsMenu.add(showAxisMenuItem);
        showAxisMenuItem.setSelected(true); // Элемент по умолчанию включен


        Action showMarkersAction = new AbstractAction("Показывать маркеры точек") {
            public void actionPerformed(ActionEvent event) {
                display.setShowMarkers(showMarkersMenuItem.isSelected());
            }
        };
        showMarkersMenuItem = new JCheckBoxMenuItem(showMarkersAction);
        graphicsMenu.add(showMarkersMenuItem);
        showMarkersMenuItem.setSelected(true);


        Action showLevelsAction = new AbstractAction("Показывать уровни графика") {
            public void actionPerformed(ActionEvent event) {
                display.setShowLevels(showLevelsMenuItem.isSelected());
            }
        };
        showLevelsMenuItem = new JCheckBoxMenuItem(showLevelsAction);
        graphicsMenu.add(showLevelsMenuItem);
        showLevelsMenuItem.setSelected(false);

        // Зарегистрировать обработчик событий, связанных с меню "График"
        graphicsMenu.addMenuListener(new GraphicsMenuListener());

        // Установить GraphicsDisplay в цент граничной компоновки
        getContentPane().add(display, BorderLayout.CENTER);
    }

    // Считывание данных графика из существующего файла
    protected void openGraphics(File selectedFile) {
        try {

            //Открыть поток чтения данных, связанный с входным файловым потоком
            DataInputStream in = new DataInputStream(new FileInputStream(selectedFile));

            /* Зная объем данных в потоке ввода можно вычислить, сколько памяти нужно зарезервировать в массиве:
            Всего байт в потоке - in.available() байт;
            Размер одного числа Double - Double.SIZE бит или Double.SIZE/8 байт;
            Так как числа записываются парами, то число пар меньше в 2 раза */

            Double[][] graphicsData = new Double[in.available() / (Double.SIZE / 8) / 2][];

            // Цикл чтения данных (пока в потоке есть данные)
            int i = 0;
            while (in.available() > 0) {

                Double x = in.readDouble(); // Из потока читается координата точки X
                Double y = in.readDouble(); // Из потока читается значение графика Y в точке X
                graphicsData[i++] = new Double[]{x, y}; // Прочитанная пара координат добавляется в массив
            }

            // Проверка, имеется ли в списке в результате чтения хотя бы одна пара координат
            if (graphicsData != null && graphicsData.length > 0) {

                fileLoaded = true; // Да - установить флаг загруженности данных
                display.showGraphics(graphicsData);
            }

            // Закрыть входной поток
            in.close();
        }
        catch (FileNotFoundException ex) {

            JOptionPane.showMessageDialog(MainFrame.this, "Указанный " +
                    "файл не найден", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
        catch (IOException ex) {

            JOptionPane.showMessageDialog(MainFrame.this, "Ошибка чтения " +
                    "координат точек из файла", "Ошибка загрузки данных", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    // Класс-слушатель событий, связанных с отображением меню
    private class GraphicsMenuListener implements MenuListener {
        // Обработчик, вызываемый перед показом меню
        public void menuSelected(MenuEvent e) {

            // Доступность или недоступность элементов меню "График" определяется загруженностью данных
            showAxisMenuItem.setEnabled(fileLoaded);
            showMarkersMenuItem.setEnabled(fileLoaded);
            showLevelsMenuItem.setEnabled(fileLoaded);
        }

        // Обработчик, вызываемый после того, как меню исчезло с экрана
        public void menuDeselected(MenuEvent e) {
        }

        // Обработчик, вызываемый в случае отмены выбора пункта меню
        public void menuCanceled(MenuEvent e) {
        }
    }
}
