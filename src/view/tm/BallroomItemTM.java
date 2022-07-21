package view.tm;

import javafx.scene.control.Button;

public class BallroomItemTM {
    private String BItId;
    private String Name;
    private String Description;
    private int QtyOnHand;
    private Button btn;

    public BallroomItemTM() {
    }

    public BallroomItemTM(String BItId, String name, String description, int qtyOnHand, Button btn) {
        this.BItId = BItId;
        Name = name;
        Description = description;
        QtyOnHand = qtyOnHand;
        this.btn = btn;
    }

    public String getBItId() {
        return BItId;
    }

    public void setBItId(String BItId) {
        this.BItId = BItId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
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
        return "BallroomItemTM{" +
                "BItId='" + BItId + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", QtyOnHand=" + QtyOnHand +
                ", btn=" + btn +
                '}';
    }
}
