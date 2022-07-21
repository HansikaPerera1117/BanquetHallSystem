package model;

public class SupplierType {
    private String STId;
    private String Type;

    public SupplierType() {
    }

    public SupplierType(String STId, String type) {
        this.STId = STId;
        Type = type;
    }

    public String getSTId() {
        return STId;
    }

    public void setSTId(String STId) {
        this.STId = STId;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "SupplierType{" +
                "STId='" + STId + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
