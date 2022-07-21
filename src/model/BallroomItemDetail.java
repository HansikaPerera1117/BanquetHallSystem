package model;

public class BallroomItemDetail {
    private String BallroomId;
    private String BallroomItemId;
    private int QTY;

    public BallroomItemDetail() {
    }

    public BallroomItemDetail(String ballroomId, String ballroomItemId, int QTY) {
        BallroomId = ballroomId;
        BallroomItemId = ballroomItemId;
        this.QTY = QTY;
    }

    public String getBallroomId() {
        return BallroomId;
    }

    public void setBallroomId(String ballroomId) {
        BallroomId = ballroomId;
    }

    public String getBallroomItemId() {
        return BallroomItemId;
    }

    public void setBallroomItemId(String ballroomItemId) {
        BallroomItemId = ballroomItemId;
    }

    public int getQTY() {
        return QTY;
    }

    public void setQTY(int QTY) {
        this.QTY = QTY;
    }

    @Override
    public String toString() {
        return "BallroomItemDetail{" +
                "BallroomId='" + BallroomId + '\'' +
                ", BallroomItemId='" + BallroomItemId + '\'' +
                ", QTY=" + QTY +
                '}';
    }
}
