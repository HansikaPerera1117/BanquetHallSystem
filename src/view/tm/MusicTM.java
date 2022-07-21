package view.tm;

import javafx.scene.control.Button;

public class MusicTM {
    private String MuId;
    private String Name;
    private String Description;
    private double Payment;
    private Button btn;

    public MusicTM() {
    }

    public MusicTM(String muId, String name, String description, double payment, Button btn) {
        MuId = muId;
        Name = name;
        Description = description;
        Payment = payment;
        this.btn = btn;
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

    public Button getBtn() {
        return btn;
    }

    public void setBtn(Button btn) {
        this.btn = btn;
    }

    @Override
    public String toString() {
        return "MusicTM{" +
                "MuId='" + MuId + '\'' +
                ", Name='" + Name + '\'' +
                ", Description='" + Description + '\'' +
                ", Payment=" + Payment +
                ", btn=" + btn +
                '}';
    }
}
