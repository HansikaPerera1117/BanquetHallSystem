package model;

public class Supplier {
    private String SId;
    private String Name;
    private String SupplierTypeId;
    private String BallroomId;
    private String Address;
    private String TelNo;
    private String Email;
    private String AccNum;
    private String Description;

    public Supplier() {
    }

    public Supplier(String SId, String name, String supplierTypeId, String ballroomId, String address, String telNo, String email, String accNum, String description) {
        this.SId = SId;
        Name = name;
        SupplierTypeId = supplierTypeId;
        BallroomId = ballroomId;
        Address = address;
        TelNo = telNo;
        Email = email;
        AccNum = accNum;
        Description = description;
    }

    public String getSId() {
        return SId;
    }

    public void setSId(String SId) {
        this.SId = SId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSupplierTypeId() {
        return SupplierTypeId;
    }

    public void setSupplierTypeId(String supplierTypeId) {
        SupplierTypeId = supplierTypeId;
    }

    public String getBallroomId() {
        return BallroomId;
    }

    public void setBallroomId(String ballroomId) {
        BallroomId = ballroomId;
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

    public String getAccNum() {
        return AccNum;
    }

    public void setAccNum(String accNum) {
        AccNum = accNum;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return "Supplier{" +
                "SId='" + SId + '\'' +
                ", Name='" + Name + '\'' +
                ", SupplierTypeId='" + SupplierTypeId + '\'' +
                ", BallroomId='" + BallroomId + '\'' +
                ", Address='" + Address + '\'' +
                ", TelNo='" + TelNo + '\'' +
                ", Email='" + Email + '\'' +
                ", AccNum='" + AccNum + '\'' +
                ", Description='" + Description + '\'' +
                '}';
    }
}
