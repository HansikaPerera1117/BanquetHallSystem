package model;

public class Menu {
    private String MId;
    private String Name;
    private double ChargeNetPerPerson;

    public Menu() {
    }

    public Menu(String MId, String name, double chargeNetPerPerson) {
        this.MId = MId;
        Name = name;
        ChargeNetPerPerson = chargeNetPerPerson;
    }

    public String getMId() {
        return MId;
    }

    public void setMId(String MId) {
        this.MId = MId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getChargeNetPerPerson() {
        return ChargeNetPerPerson;
    }

    public void setChargeNetPerPerson(double chargeNetPerPerson) {
        ChargeNetPerPerson = chargeNetPerPerson;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "MId='" + MId + '\'' +
                ", Name='" + Name + '\'' +
                ", ChargeNetPerPerson=" + ChargeNetPerPerson +
                '}';
    }
}
