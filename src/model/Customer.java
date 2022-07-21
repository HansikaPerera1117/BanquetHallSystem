package model;

public class Customer {
    private String CId;
    private String Name;
    private String NIC;
    private String Address;
    private String TelNo;
    private String Email;

    public Customer() {
    }

    public Customer(String CId, String name, String NIC, String address, String telNo, String email) {
        this.CId = CId;
        Name = name;
        this.NIC = NIC;
        Address = address;
        TelNo = telNo;
        Email = email;
    }

    public String getCId() {
        return CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getNIC() {
        return NIC;
    }

    public void setNIC(String NIC) {
        this.NIC = NIC;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getTelNo() {
        return TelNo;
    }

    public void setTelNo(String telNo) {
        TelNo = telNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "CId='" + CId + '\'' +
                ", Name='" + Name + '\'' +
                ", NIC='" + NIC + '\'' +
                ", Address='" + Address + '\'' +
                ", TelNo='" + TelNo + '\'' +
                ", Email='" + Email + '\'' +
                '}';
    }
}

