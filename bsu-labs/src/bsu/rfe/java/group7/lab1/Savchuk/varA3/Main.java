package bsu.rfe.java.group7.lab1.Savchuk.varA3;

import bsu.rfe.java.group7.lab1.Savchuk.varA3.food.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Food[] breakfast = new Food[args.length];

        for (int i = 0; i < args.length; i++) {
            String[] parts = args[i].split("/");
            if (parts[0].equals("Мясо")) {
                breakfast[i] = new Beef(parts[1]);
            } else
                if(parts[0].equals("Кекс")){
                    breakfast[i] = new Cupcake(parts[1]);
                }
            else if (parts[0].equals("Сыр")) {
                breakfast[i] = new Cheese();
            } else {
                breakfast[i] = new Apple(parts[1]);
            }
        }

        for (Food item : breakfast) {
            item.consume();
        }

        Beef food = new Beef("С кровью");
        int count0 = 0;
        for (int i = 0; i < breakfast.length; i++) {
            if(food.equals(breakfast[i]))
            {
                count0++;
            }
        }
        Cheese food1 = new Cheese();
        int count1 = 0;
        for (int i = 0; i < breakfast.length; i++) {
            if(food1.equals(breakfast[i]))
            {
                count1++;
            }
        }
        Apple food2 = new Apple("Малое");
        int count2 = 0;
        for (int i = 0; i < breakfast.length; i++) {
            if(food2.equals(breakfast[i]))
            {
                count2++;
            }
        }
        Cupcake food3 = new Cupcake("Кексик");
        int count3 = 0;
        for (int i = 0; i < breakfast.length; i++) {
            if(food3.equals(breakfast[i]))
            {
                count3++;
            }
        }
        System.out.println("Количество съеденного мяса: " + count0);
        System.out.println("Количество съеденных яблок: " + count2);
        System.out.println("Количество съеденного сыра: " + count1);
        System.out.println("Количество съеденных кексов: " + count3);
    }
}





