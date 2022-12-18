package bsu.rfe.java.group7.lab1.Savchuk.varA3.food;

public abstract class Food implements Consumable{
    private String name = null;

    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Food(String name){
        this.name = name;
    }

    public String toString(){
        return name;
    }

    public boolean equals(Object arg){
        if(!(arg instanceof Food)){
            return false;
        }
        else if (name == null || ((Food)arg).name == null){
            return false;
        }
        return name.equals(((Food)arg).name);
    }
}
