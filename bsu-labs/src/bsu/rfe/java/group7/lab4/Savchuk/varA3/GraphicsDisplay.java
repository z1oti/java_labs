package bsu.rfe.java.group7.lab4.Savchuk.varA3;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
@SuppressWarnings("serial")
public class GraphicsDisplay extends JPanel {

    // Список координат точек для построения графика
    private Double[][] graphicsData;

    // Флаговые переменные, задающие правила отображения графика
    private boolean showAxis = true, showMarkers = true, showLevels = false;

    // Границы диапазона пространства, подлежащего отображению
    private double minX, maxX, minY, maxY;

    // Используемый масштаб отображения
    private double scale;

    // Различные стили черчения линий
    private BasicStroke graphicsStroke, axisStroke, markerStroke, levelStroke;

    // Различные шрифты отображения надписей
    private Font axisFont, levelsFont;

    // Среднее арифметическое значений функций в точках

    private double average(){
        double sum = 0;
        double i = 1;
        for (Double[] point: graphicsData)
        {
            sum += point[1];
            i++;
        }
        return sum / i;
    }

    public GraphicsDisplay() {

        setBackground(Color.WHITE);

        // Сконструировать необходимые объекты, используемые в рисовании
        // Перо для рисования графика
        graphicsStroke = new BasicStroke(5.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND, 10.0f, new float[] {5,5,5,5,5,5,20,5,10,5,10,5}, 0.0f);

        // Перо для рисования осей координат
        axisStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);

        // Перо для рисования контуров маркеров
        markerStroke = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, null, 0.0f);

        // Перо для рисования уровней графика
        levelStroke = new BasicStroke(2.0f, BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER, 10.0f, new float [] {3, 2}, 0.0f);

        // Шрифт для подписей осей координат
        levelsFont = new Font("Serif", Font.PLAIN, 20);
        axisFont = new Font("Serif", Font.BOLD, 36);
    }

    // Данный метод вызывается из обработчика элемента меню "Открыть файл с графиком"
    // главного окна приложения в случае успешной загрузки данных
    public void showGraphics(Double[][] graphicsData) {

        // Сохранить массив точек во внутреннем поле класса
        this.graphicsData = graphicsData;
        // Запросить перерисовку компонента, т.е. неявно вызвать paintComponent()
        repaint();
    }

    // Методы-модификаторы для изменения параметров отображения графика
    // Изменение любого параметра приводит к перерисовке области
    public void setShowAxis(boolean showAxis) {
        this.showAxis = showAxis;
        repaint();
    }
    public void setShowMarkers(boolean showMarkers) {
        this.showMarkers = showMarkers;
        repaint();
    }
    public void setShowLevels(boolean showLevels) {
        this.showLevels = showLevels;
        repaint();
    }

    // Метод отображения всего компонента, содержащего график
    public void paintComponent(Graphics g) {

        super.paintComponent(g); // Заливка области цветом заднего фона
        if (graphicsData == null || graphicsData.length == 0) // Данных нет - ничего не делать
            return;

        // Определение области пространства, подлежащей отображению
        // Верхний левый угол это (minX, maxY) - правый нижний это (maxX, minY)
        minX = graphicsData[0][0];
        maxX = graphicsData[graphicsData.length - 1][0];
        minY = graphicsData[0][1];
        maxY = minY;

        // Найти минимальное и максимальное значение функции
        for (int i = 1; i < graphicsData.length; i++) {
            if (graphicsData[i][1] < minY) {
                minY = graphicsData[i][1];
            }
            if (graphicsData[i][1] > maxY) {
                maxY = graphicsData[i][1];
            }
        }

        // Определить (исходя из размеров окна) масштабы по осям X и Y
        double scaleX = getSize().getWidth() / (maxX - minX);
        double scaleY = getSize().getHeight() / (maxY - minY);

        // Чтобы изображение было неискаженным - масштаб должен быть одинаков
        // Выбираем за основу минимальный
        scale = Math.min(scaleX, scaleY);

        // Корректировка границ отображаемой области согласно выбранному масштабу
        if (scale == scaleX) {

            // Вычислим, сколько делений влезет по Y при выбранном масштабе и
            // вычтем сколько деление требовалось изначально
            double yIncrement = (getSize().getHeight()/scale - (maxY - minY))/2;
            maxY += yIncrement;
            minY -= yIncrement;
        }
        if (scale == scaleY) {

            // Если за основу был взят масштаб по оси Y, действовать по аналогии
            double xIncrement = (getSize().getWidth()/scale - (maxX - minX))/2;
            maxX += xIncrement;
            minX -= xIncrement;
        }

        // Сохранить текущие настройки холста
        Graphics2D canvas = (Graphics2D) g;
        Stroke oldStroke = canvas.getStroke();
        Color oldColor = canvas.getColor();
        Paint oldPaint = canvas.getPaint();
        Font oldFont = canvas.getFont();

        // Вызов методов отображения элементов графика
        // Порядок вызова методов имеет значение (редыдущий рисунок будет затираться последующим)
        if (showAxis)
            paintAxis(canvas); // Оси координат
        paintGraphics(canvas); // График
        if (showMarkers)
            paintMarkers(canvas); // Маркеры точек
        if (showLevels)
            paintLevelLines(canvas); // Уровни

        // Восстановить старые настройки холста
        canvas.setFont(oldFont);
        canvas.setPaint(oldPaint);
        canvas.setColor(oldColor);
        canvas.setStroke(oldStroke);
    }

    // Отрисовка графика по прочитанным координатам
    protected void paintGraphics(Graphics2D canvas) {

        // Линия для рисования графика
        canvas.setStroke(graphicsStroke);
        canvas.setColor(Color.RED);

        GeneralPath graphics = new GeneralPath();
        for (int i = 0; i < graphicsData.length; i++) {

            // Преобразовать значения (x,y) в точку на экране point
            Point2D.Double point = xyToPoint(graphicsData[i][0], graphicsData[i][1]);
            if (i > 0) {

                // Не первая итерация цикла - вести линию в точку point
                graphics.lineTo(point.getX(), point.getY()); // Ввод линии в точку
            }
            else {
                graphics.moveTo(point.getX(), point.getY()); // Начало пути при первой итерации
            }
        }
        canvas.draw(graphics); // Отобразить график
    }

    // Отображение горизонтальных линий, отмечающие уровни на графике
    protected void paintLevelLines(Graphics2D canvas) {

        // Рассчет уровней
        double lvl1, lvl2, lvl3;
        lvl1 = minY + 0.9 * (maxY - minY);
        lvl2 = minY + 0.5 * (maxY - minY);
        lvl3 = minY + 0.1 * (maxY - minY);

        canvas.setStroke(levelStroke);
        canvas.setColor(Color.GREEN);
        canvas.setFont(levelsFont);
        canvas.draw(new Line2D.Double(xyToPoint(minX, lvl1), xyToPoint(maxX, lvl1)));
        canvas.draw(new Line2D.Double(xyToPoint(minX, lvl2), xyToPoint(maxX, lvl2)));
        canvas.draw(new Line2D.Double(xyToPoint(minX, lvl3), xyToPoint(maxX, lvl3)));

        //FontRenderContext context = canvas.getFontRenderContext();
        //Rectangle2D bounds = axisFont.getStringBounds("90%", context);
        Point2D.Double labelPos1 = xyToPoint(0, lvl1);
        Point2D.Double labelPos2 = xyToPoint(0, lvl2);
        Point2D.Double labelPos3 = xyToPoint(0, lvl3);



        // Вывести надпись в точке с вычисленными координатами
        canvas.drawString("90%", (float)labelPos1.getX() + 5, (float)(labelPos1.getY() - 3));// - bounds.getY()));
        canvas.drawString("50%", (float)labelPos2.getX() + 5, (float)(labelPos2.getY() - 3));// - bounds.getY()));
        canvas.drawString("10%", (float)labelPos3.getX() + 5, (float)(labelPos3.getY() - 3));// - bounds.getY()));

    }

    // Отображение маркеров точек, по которым рисовался график
    protected void paintMarkers(Graphics2D canvas) {

        canvas.setStroke(markerStroke); // Установить перо для черчения контуров маркеров

        // Организовать цикл по всем точкам графика
        for (Double[] point: graphicsData) {

            if (point[1] > 2 * average()) {
                canvas.setColor(Color.GREEN); // Установить цвет для контуров маркеров
                canvas.setPaint(Color.GREEN); // Установить цвет для закрашивания маркеров внутри
            }
            else{
                canvas.setColor(Color.RED); // Установить цвет для контуров маркеров
                canvas.setPaint(Color.RED); // Установить цвет для закрашивания маркеров внутри
            }

            // Нарисуем треугольгик как сложную линию
            GeneralPath marker = new GeneralPath();

            // Центр - в точке (x,y)
            Point2D.Double center = xyToPoint(point[0], point[1]);

            // Углы треугольника и их соединение
            Point2D.Double corner1 = shiftPoint(center, -5.5, -5.5);
            Point2D.Double corner2 = shiftPoint(center, 5.5, -5.5);
            Point2D.Double corner3 = shiftPoint(center, 0, 5.5);
            marker.append(new Line2D.Double(corner1, corner2), true);
            marker.append(new Line2D.Double(corner2, corner3), true);
            marker.append(new Line2D.Double(corner3, corner1), true);

            canvas.draw(marker); // Начертить контур маркера
            //canvas.fill(marker); // Залить внутреннюю область маркера
        }
    }

    // Метод, обеспечивающий отображение осей координат
    protected void paintAxis(Graphics2D canvas) {

        canvas.setStroke(axisStroke); // Установить особое начертание для осей
        canvas.setColor(Color.BLACK); // Оси рисуются чѐрным цветом
        canvas.setPaint(Color.BLACK); // Стрелки заливаются чѐрным цветом
        canvas.setFont(axisFont); // Подписи к координатным осям делаются специальным шрифтом

        // Создать объект контекста отображения текста для получения характеристик устройства (экрана)
        FontRenderContext context = canvas.getFontRenderContext();

        // Определить, должна ли быть видна ось Y на графике
        // Она должна быть видна, если левая граница показываемой области (minX) <= 0.0,
        // а правая (maxX) >= 0.0
        if (minX <= 0.0 && maxX >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(0, maxY), xyToPoint(0, minY)));
            GeneralPath arrow = new GeneralPath(); // Стрелка оси Y

            // Установить начальную точку ломаной точно на верхний конец оси Y
            Point2D.Double lineEnd = xyToPoint(0, maxY);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            // Вести левый "скат" стрелки в точку с относительными координатами (5,20)
            arrow.lineTo(arrow.getCurrentPoint().getX() + 5, arrow.getCurrentPoint().getY() + 20);

            // Вести нижнюю часть стрелки в точку с относительными координатами (-10, 0)
            arrow.lineTo(arrow.getCurrentPoint().getX() - 10, arrow.getCurrentPoint().getY());

            arrow.closePath(); // Замкнуть треугольник стрелки
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку

            Rectangle2D bounds = axisFont.getStringBounds("y", context); // Подпись к оси Y
            Point2D.Double labelPos = xyToPoint(0, maxY); // Сколько места понадобится для надписи "y"

            // Вывести надпись в точке с вычисленными координатами
            canvas.drawString("y", (float)labelPos.getX() + 10, (float)(labelPos.getY() - bounds.getY()));
            bounds = axisFont.getStringBounds("0", context);
            labelPos = xyToPoint(0, 0);
            // Вывести надпись в точке с вычисленными координатами
            canvas.drawString("хуй", (float) labelPos.getX() + 10,
                    (float) (labelPos.getY() - bounds.getY()));
        }

        // Определить, должна ли быть видна ось X на графике
        // Она должна быть видна, если верхняя граница показываемой области (maxX) >= 0.0,
        // а нижняя (minY) <= 0.0
        if (minY <= 0.0 && maxY >= 0.0) {

            canvas.draw(new Line2D.Double(xyToPoint(minX, 0), xyToPoint(maxX, 0)));
            GeneralPath arrow = new GeneralPath(); // Стрелка оси X

            // Установить начальную точку ломаной точно на правый конец оси X
            Point2D.Double lineEnd = xyToPoint(maxX, 0);
            arrow.moveTo(lineEnd.getX(), lineEnd.getY());

            // Вести верхний "скат" стрелки в точку с относительными координатами (-20,-5)
            arrow.lineTo(arrow.getCurrentPoint().getX()-20, arrow.getCurrentPoint().getY()-5);

            // Вести левую часть стрелки в точку с относительными координатами (0, 10)
            arrow.lineTo(arrow.getCurrentPoint().getX(), arrow.getCurrentPoint().getY()+10);

            arrow.closePath(); // Замкнуть треугольник стрелки
            canvas.draw(arrow); // Нарисовать стрелку
            canvas.fill(arrow); // Закрасить стрелку

            Rectangle2D bounds = axisFont.getStringBounds("x", context); // Подпись к оси X
            Point2D.Double labelPos = xyToPoint(maxX, 0); // Cколько места понадобится для надписи "x"

            // Вывести надпись в точке с вычисленными координатами
            canvas.drawString("x", (float)(labelPos.getX() - bounds.getWidth() - 10),
                    (float)(labelPos.getY() + bounds.getY()));
        }
    }

    /* Метод-помощник, осуществляющий преобразование координат.
    Оно необходимо, т.к. верхнему левому углу холста с координатами
    (0.0, 0.0) соответствует точка графика с координатами (minX, maxY),
    где minX - это самое "левое" значение X, а maxY - самое "верхнее" значение Y.
    */
    protected Point2D.Double xyToPoint(double x, double y) {

        double deltaX = x - minX; // Смещение X от самой левой точки (minX)
        double deltaY = maxY - y; // Смещение Y от точки верхней точки (maxY)
        return new Point2D.Double(deltaX*scale, deltaY*scale);
    }

    /* Метод-помощник, возвращающий экземпляр класса Point2D.Double
    смещённый по отношению к исходному на deltaX, deltaY
    К сожалению, стандартного метода, выполняющего такую задачу, нет.
    */
    protected Point2D.Double shiftPoint(Point2D.Double src, double deltaX, double deltaY) {

        // Инициализировать новый экземпляр точки
        Point2D.Double dest = new Point2D.Double();

        // Задать ее координаты как координаты существующей точки + заданные смещения
        dest.setLocation(src.getX() + deltaX, src.getY() + deltaY);
        return dest;
    }
}
