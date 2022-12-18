package bsu.rfe.java.group7.lab1.Savchuk.varA3.food;

public class Cupcake extends Food{

    private String preparedness;

    public Cupcake(String preparedness) {
        super("Кекс");
        this.preparedness = preparedness;
    }

    @Override
    public void consume() {
        System.out.println(this + " съеден.");
    }

    public String toString() {
        return super.toString() + " " + this.preparedness.toUpperCase();
    }

    public String getPreparedness() {
        return preparedness;
    }

    public void setSize(String preparedness) {
        this.preparedness = preparedness;
    }
}
