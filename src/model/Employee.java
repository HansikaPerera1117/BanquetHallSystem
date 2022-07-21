package model;

public class Employee {
    private String EId;
    private String Name;
    private String BallroomId;
    private String Gender;
    private String NIC;
    private String Address;
    private String TelNo;
    private String Email;
    private String Type;

    public Employee() {
    }

    public Employee(String EId, String name, String ballroomId, String gender, String NIC, String address, String telNo, String email, String type) {
        this.EId = EId;
        Name = name;
        BallroomId = ballroomId;
        Gender = gender;
        this.NIC = NIC;
        Address = address;
        TelNo = telNo;
        Email = email;
        Type = type;
    }

    public String getEId() {
        return EId;
    }

    public void setEId(String EId) {
        this.EId = EId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBallroomId() {
        return BallroomId;
    }

    public void setBallroomId(String ballroomId) {
        BallroomId = ballroomId;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
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

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "EId='" + EId + '\'' +
                ", Name='" + Name + '\'' +
                ", BallroomId='" + BallroomId + '\'' +
                ", Gender='" + Gender + '\'' +
                ", NIC='" + NIC + '\'' +
                ", Address='" + Address + '\'' +
                ", TelNo='" + TelNo + '\'' +
                ", Email='" + Email + '\'' +
                ", Type='" + Type + '\'' +
                '}';
    }
}
