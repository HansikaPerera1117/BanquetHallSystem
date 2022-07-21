package model;

public class MenuChoice {
    private int Num;
    private String Name;
    private String Choise;

    public MenuChoice() {
    }

    public MenuChoice(int num, String name, String choise) {
        Num = num;
        Name = name;
        Choise = choise;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getChoise() {
        return Choise;
    }

    public void setChoise(String choise) {
        Choise = choise;
    }

    @Override
    public String toString() {
        return "MenuChoice{" +
                "Num=" + Num +
                ", Name='" + Name + '\'' +
                ", Choise='" + Choise + '\'' +
                '}';
    }
}
