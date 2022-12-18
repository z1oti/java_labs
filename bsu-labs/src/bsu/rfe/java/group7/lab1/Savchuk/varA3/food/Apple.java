package bsu.rfe.java.group7.lab1.Savchuk.varA3.food;

public class Apple extends Food {
    private String size;

    public String getSize() {
        return size;
    }

    public Apple(String size) {
        super("Яблоко");
        this.size = size;
    }

    public void consume(){
        System.out.println(this + " съедено");
    }

    @Override
    public String toString() {
        return super.toString() + " размера '" + size.toUpperCase() + "'";
    }

//    @Override
//    public boolean equals(Object obj) {
//        if (!(obj instanceof bsu.rfe.java.group7.lab1.Savchuk.varA3.food.Apple)) {
//            return false;
//        } else if (size == null || ((bsu.rfe.java.group7.lab1.Savchuk.varA3.food.Apple) obj).size == null) {  // размер не задан
//            return false;
//        }
//        else if (!(size.equals(((bsu.rfe.java.group7.lab1.Savchuk.varA3.food.Apple) obj).size))) {
//            return false;
//        }
//        return super.equals(obj);
//    }
}
