package model;

public class BallroomLiquorDetail {
    private String BallroomId;
    private String LiquorId;
    private int Qty;

    public BallroomLiquorDetail() {
    }

    public BallroomLiquorDetail(String ballroomId, String liquorId, int qty) {
        BallroomId = ballroomId;
        LiquorId = liquorId;
        Qty = qty;
    }

    public String getBallroomId() {
        return BallroomId;
    }

    public void setBallroomId(String ballroomId) {
        BallroomId = ballroomId;
    }

    public String getLiquorId() {
        return LiquorId;
    }

    public void setLiquorId(String liquorId) {
        LiquorId = liquorId;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    @Override
    public String toString() {
        return "BallroomLiquorDetail{" +
                "BallroomId='" + BallroomId + '\'' +
                ", LiquorId='" + LiquorId + '\'' +
                ", Qty=" + Qty +
                '}';
    }
}
