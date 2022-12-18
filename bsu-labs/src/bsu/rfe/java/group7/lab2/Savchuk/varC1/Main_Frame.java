package bsu.rfe.java.group7.lab2.Savchuk.varC1;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import static java.lang.Math.*;


public class Main_Frame extends JFrame
{
    private static final int width = 1000;
    private static final int height = 800;
    private JTextField result_field;
    private JTextField x_field;
    private JTextField y_field;
    private JTextField z_field;
    private JLabel image;
    private ButtonGroup radioButtons = new ButtonGroup();
    private Box formula_type = Box.createHorizontalBox();
    private ButtonGroup radioMemoryButtons = new ButtonGroup();
    private Box hBoxMemoryType = Box.createHorizontalBox();
    private JTextField memoryTextField;
    private int formula_number = 1;
    private int memoryId= 1;

    private Double mem1 = new Double(0);
    private Double mem2 = new Double(0);
    private Double mem3 = new Double(0);

    //вычисление функций
    public Double formula1(Double x, Double y, Double z)
    {
        if (y<=0)	{
            JOptionPane.showMessageDialog(Main_Frame.this,
                    "y не может быть меньше нуля", "" +
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }

        return  (sin(log(y)+sin(PI*y*y)) * pow(x*x+sin(z)+pow(E,cos(z)),0.25));
    }
    public Double formula2(Double x, Double y, Double z)
    {
        if (y == -1)	{
            JOptionPane.showMessageDialog(Main_Frame.this,
                    " y не должен быть равен -1", "" +
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }
        if ((cos(exp(y)) + exp(y*y) + sqrt(1/x)) < 0){
            JOptionPane.showMessageDialog(Main_Frame.this,
                    "Выражение (cos(exp(y)) + exp(y*y) + sqrt(1/x) должно быть больше 0", "" +
                            "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
            return 0.0;
        }
        return pow((cos(exp(y)) + log((1+y)*(1+y)) + sqrt(exp(cos(x))+sin(PI*z)*sin(PI*z)) + sqrt(1/x) + cos(y*y)), sin(z));
    }
    // радиокнопки для запоминания значения
    private void addMemoryRadioButton (String buttonName, final int memoryId)	{
        JRadioButton button = new JRadioButton(buttonName);

        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event)	{
                Main_Frame.this.memoryId = memoryId;
                if (memoryId == 1)	memoryTextField.setText(mem1.toString());
                if (memoryId == 2)	memoryTextField.setText(mem2.toString());
                if (memoryId == 3)	memoryTextField.setText(mem3.toString());
            }
        });

        radioMemoryButtons.add(button);
        hBoxMemoryType.add(button);
    }
    // радиокнопки для формул 1,2
    private void addRadioButton(String name, final int formula_number)
    {
        JRadioButton button = new JRadioButton(name);
        button.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                Main_Frame.this.formula_number = formula_number;
                if (formula_number == 1)	image.setIcon(new ImageIcon(Main_Frame.class.getResource("formula_1.bmp")));
                if (formula_number == 2) image.setIcon(new ImageIcon(Main_Frame.class.getResource("formula_2.bmp")));
            }
        });
        radioButtons.add(button);
        formula_type.add(button);
    }

    public Main_Frame() {
        super("Формулы (Лаб-2)");
        setSize(width,height);
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width-width)/2,
                (kit.getScreenSize().height-height)/2);


        // область с картинкой формулы----------------------------------------------------------------------------------
        Box picture = Box.createHorizontalBox();
        picture.add(Box.createVerticalGlue());
        picture.add(Box.createHorizontalGlue());
        image = new JLabel(new ImageIcon(Main_Frame.class.getResource("formula_1.bmp")));
        picture.add(image);
        picture.add(Box.createHorizontalGlue());
        picture.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // область с выбором формул-------------------------------------------------------------------------------------
        formula_type.add(Box.createHorizontalGlue());
        addRadioButton("Формула 1", 1);
        addRadioButton("Формула 2", 2);
        radioButtons.setSelected(radioButtons.getElements().nextElement().getModel(), true);
        formula_type.add(Box.createHorizontalGlue());
        formula_type.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        // область с полями ввода значений------------------------------------------------------------------------------
        Box data=Box.createHorizontalBox();
        //data.add(Box.createHorizontalGlue());
        x_field = new JTextField("0",10);
        x_field.setMaximumSize(x_field.getPreferredSize());
        y_field = new JTextField("0",10);
        y_field.setMaximumSize(x_field.getPreferredSize());
        z_field = new JTextField("0",10);
        z_field.setMaximumSize(x_field.getPreferredSize());
        JLabel x_label=new JLabel("X:");
        JLabel y_label=new JLabel("Y:");
        JLabel z_label=new JLabel("Z:");
        data.add(Box.createHorizontalStrut(0));
        data.add(Box.createHorizontalGlue());
        data.add(x_label);
        //data.add(Box.createHorizontalStrut(10));
        data.add(x_field);
        //data.add(Box.createHorizontalStrut(100));
        data.add(y_label);
        data.add(Box.createHorizontalStrut(10));
        data.add(y_field);
        //data.add(Box.createHorizontalStrut(100));
        data.add(z_label);
        data.add(Box.createHorizontalStrut(10));
        data.add(z_field);
        data.add(Box.createHorizontalGlue());
        //data.add(Box.createHorizontalStrut(100));
        data.setBorder(BorderFactory.createLineBorder(Color.BLACK));


        // область для ответа-------------------------------------------------------------------------------------------
        Box result_area = Box.createHorizontalBox();
        result_area.add(Box.createHorizontalGlue());
        JLabel resultlabel=new JLabel("Результат:");
        result_field = new JTextField("0",15);
        result_field.setMaximumSize(result_field.getPreferredSize());
        result_area.add(resultlabel);
        result_area.add(Box.createHorizontalStrut(10));
        result_area.add(result_field);
        result_area.add(Box.createHorizontalGlue());
        result_area.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // область для действий-----------------------------------------------------------------------------------------
        Box actions=Box.createHorizontalBox();
        JButton calc_button=new JButton("Вычислить");
        calc_button.addActionListener(new ActionListener() {
            //преобразование введённых данных
            public void actionPerformed(ActionEvent ev) {
                //Преобразование введенных строк в числа с плавающей точкой может
                //спровоцировать исключительную ситуацию при неправильном формате чисел,
                //поэтому необходим блок try-catch
                try {
                    //Получить значение X
                    Double x = Double.parseDouble(x_field.getText());
                    //Получить значение Y
                    Double y = Double.parseDouble(y_field.getText());
                    //Получить значение Z
                    Double z = Double.parseDouble(z_field.getText());
                    // Результат
                    Double result;

                    //Вычислить результат
                    if (formula_number==1)
                        result = formula1(x, y, z);
                    else
                        result = formula2(x, y, z);
                    //Установить текст надписи равным результату
                    result_field.setText(result.toString());
                } catch (NumberFormatException ex) {
                    //В случае исключительной ситуации показать сообщение ошибки
                    JOptionPane.showMessageDialog(Main_Frame.this, "Ошибка в" +
                                    "формате записи числа с плавающей точкой", "Ошибочный формат числа",
                            JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        //очистка данных
        JButton clean_button=new JButton("Очистить");
        clean_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                x_field.setText("0");
                y_field.setText("0");
                z_field.setText("0");
                result_field.setText("0");
            }
        });

        actions.add(Box.createHorizontalStrut(10));
        //actions.add(Box.createHorizontalGlue());
        actions.add(calc_button);
        actions.add(Box.createHorizontalGlue());
        actions.add(clean_button);
        //actions.add(Box.createHorizontalGlue());

        actions.add(Box.createHorizontalStrut(10));
        actions.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        hBoxMemoryType.add(Box.createHorizontalGlue());
        addMemoryRadioButton("Память 1",1);
        addMemoryRadioButton("Память 2",2);
        addMemoryRadioButton("Память 3",3);
        radioMemoryButtons.setSelected(radioMemoryButtons.getElements().nextElement().getModel(), true);
        hBoxMemoryType.add(Box.createHorizontalGlue());



        Box memory_result=Box.createHorizontalBox();
        memory_result.add(Box.createHorizontalGlue());
        JButton MC=new JButton("MC");

        MC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                if (memoryId == 1)	mem1 = (double) 0;
                if (memoryId == 2)	mem2 = (double) 0;
                if (memoryId == 3)	mem3 = (double) 0;
                memoryTextField.setText("0.0");
            }
        });

        JButton M_plus=new JButton("M+");
        M_plus.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent arg0) {
                try{
                    Double result = Double.parseDouble(result_field.getText());

                    if (memoryId == 1)
                    {
                        mem1 += result;
                        memoryTextField.setText(mem1.toString());
                    }
                    if (memoryId == 2)
                    {
                        mem2 += result;
                        memoryTextField.setText(mem2.toString());
                    }
                    if (memoryId == 3)
                    {
                        mem3 += result;
                        memoryTextField.setText(mem3.toString());
                    }

                }catch (NumberFormatException ex)
                { JOptionPane.showMessageDialog(Main_Frame.this,
                        "Ошибка в формате записи числа с плавающей точкой", "" +
                                "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        memoryTextField = new JTextField("0.0", 15);
        memoryTextField.setMaximumSize(memoryTextField.getPreferredSize());

        memory_result.add(MC);
        memory_result.add(Box.createHorizontalStrut(10));
        memory_result.add(memoryTextField);
        memory_result.add(Box.createHorizontalStrut(10));
        memory_result.add(M_plus);
        memory_result.add(Box.createHorizontalGlue());
        memory_result.setBorder(BorderFactory.createLineBorder(Color.BLACK));



        //связывание области воедино
        Box contentBox = Box.createVerticalBox();
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(formula_type);
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(data);
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(result_area);
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(actions);
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(hBoxMemoryType);
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(memory_result);
        contentBox.add(Box.createVerticalGlue());
        contentBox.add(picture);
        contentBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));



        getContentPane().add(contentBox, BorderLayout.CENTER);




    }

}
