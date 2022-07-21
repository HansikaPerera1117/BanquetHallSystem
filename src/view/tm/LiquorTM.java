package view.tm;

import javafx.scene.control.Button;

public class LiquorTM {
    private String LId;
    private String Name;
    private double Price;
    private String Description;
    private int QtyOnHand;
    private Button btn;

    public LiquorTM() {
    }

    public LiquorTM(String LId, String name, double price, String description, int qtyOnHand, Button btn) {
        this.LId = LId;
        Name = name;
        Price = price;
        Description = description;
        QtyOnHand = qtyOnHand;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "LiquorTM{" +
                "LId='" + LId + '\'' +
                ", Name='" + Name + '\'' +
                ", Price=" + Price +
                ", Description='" + Description + '\'' +
                ", QtyOnHand=" + QtyOnHand +
                ", btn=" + btn +
                '}';
    }
}
