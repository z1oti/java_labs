package bsu.rfe.java.group7.lab1.Savchuk.varA3.food;

public class Cheese extends Food{

    public Cheese(){
        super("Сыр");
    }

    @Override
    public void consume() {
        System.out.println(this + " съедено");
    }
}