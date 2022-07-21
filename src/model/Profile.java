package model;

public class Profile {
    private String UserName;
    private String Password;

    public Profile(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    public Profile() {
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                '}';
    }
}
