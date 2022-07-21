package model;

public class Booking {
    private String BookingId;
    private String Date;
    private String Time;
    private int GuestCount;
    private String CustomerId;
    private String BallroomId;
    private String MenuId;
    private String MusicId;
    private String LiquorId;
    private int LiquorCount;
    private String Description;

    public Booking() {
    }

    public Booking(String bookingId, String date, String time, int guestCount, String customerId, String ballroomId, String menuId, String musicId, String liquorId, int liquorCount, String description) {
        BookingId = bookingId;
        Date = date;
        Time = time;
        GuestCount = guestCount;
        CustomerId = customerId;
        BallroomId = ballroomId;
        MenuId = menuId;
        MusicId = musicId;
        LiquorId = liquorId;
        LiquorCount = liquorCount;
        Description = description;
    }

    public String getBookingId() {
        return BookingId;
    }

    public void setBookingId(String bookingId) {
        BookingId = bookingId;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getGuestCount() {
        return GuestCount;
    }

    public void setGuestCount(int guestCount) {
        GuestCount = guestCount;
    }

    public String getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(String customerId) {
        CustomerId = customerId;
    }

    public String getBallroomId() {
        return BallroomId;
    }

    public void setBallroomId(String ballroomId) {
        BallroomId = ballroomId;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public String getMusicId() {
        return MusicId;
    }

    public void setMusicId(String musicId) {
        MusicId = musicId;
    }

    public String getLiquorId() {
        return LiquorId;
    }

    public void setLiquorId(String liquorId) {
        LiquorId = liquorId;
    }

    public int getLiquorCount() {
        return LiquorCount;
    }

    public void setLiquorCount(int liquorCount) {
        LiquorCount = liquorCount;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "BookingId='" + BookingId + '\'' +
                ", Date='" + Date + '\'' +
                ", Time='" + Time + '\'' +
                ", GuestCount=" + GuestCount +
                ", CustomerId='" + CustomerId + '\'' +
                ", BallroomId='" + BallroomId + '\'' +
                ", MenuId='" + MenuId + '\'' +
                ", MusicId='" + MusicId + '\'' +
                ", LiquorId='" + LiquorId + '\'' +
                ", LiquorCount=" + LiquorCount +
                ", Description='" + Description + '\'' +
                '}';
    }
}
