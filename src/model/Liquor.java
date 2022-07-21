package model;

public class Liquor {
    private String LId;
    private String Name;
    private double Price;
    private String Description;
    private int QtyOnHand;

    public Liquor() {
    }

    public Liquor(String LId, String name, double price, String description, int qtyOnHand) {
        this.LId = LId;
        Name = name;
        Price = price;
        Description = description;
        QtyOnHand = qtyOnHand;
    }

    public String getLId() {
        return LId;
    }

    public void setLId(String LId) {
        this.LId = LId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getQtyOnHand() {
        return QtyOnHand;
    }

    public void setQtyOnHand(int qtyOnHand) {
        QtyOnHand = qtyOnHand;
    }

    @Override
    public String toString() {
        return "Liquor{" +
                "LId='" + LId + '\'' +
                ", Name='" + Name + '\'' +
                ", Price=" + Price +
                ", Description='" + Description + '\'' +
                ", QtyOnHand=" + QtyOnHand +
                '}';
    }
}
