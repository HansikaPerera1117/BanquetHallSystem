package model;

public class BallroomItem {
    private String BItId;
    private String Name;
    private String Description;
    private int QtyOnHand;

    public BallroomItem() {
    }

    public BallroomItem(String BItId, String name, String description, int qtyOnHand) {
        this.BItId = BItId;
        Name = name;
        Description = description;
        QtyOnHand = qtyOnHand;
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

    @Override
    public String toString() {
        return "BallroomItem{" +
                "BItId='" + BItId + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", QtyOnHand=" + QtyOnHand +
                '}';
    }
}
