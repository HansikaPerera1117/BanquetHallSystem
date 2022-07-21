package model;

public class Ballroom {
    private String ID;
    private String Name;
    private String Description;
    private double Price;

    public Ballroom() {
    }

    public Ballroom(String ID, String name, String description, double price) {
        this.ID = ID;
        Name = name;
        Description = description;
        Price = price;
    }

    public String getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getDescription() {
        return Description;
    }

    public double getPrice() {
        return Price;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setPrice(double price) {
        Price = price;
    }

    @Override
    public String toString() {
        return "Ballroom{" +
                "ID='" + ID + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Price=" + Price +
                '}';
    }
}
