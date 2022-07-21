package model;

public class Music {
    private String MuId;
    private String Name;
    private String Description;
    private double Payment;

    public Music() {
    }

    public Music(String muId, String name, String description, double payment) {
        MuId = muId;
        Name = name;
        Description = description;
        Payment = payment;
    }

    public String getMuId() {
        return MuId;
    }

    public void setMuId(String muId) {
        MuId = muId;
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

    public double getPayment() {
        return Payment;
    }

    public void setPayment(double payment) {
        Payment = payment;
    }

    @Override
    public String toString() {
        return "Music{" +
                "MuId='" + MuId + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Payment=" + Payment +
                '}';
    }
}
