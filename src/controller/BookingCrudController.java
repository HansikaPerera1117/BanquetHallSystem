package controller;

import model.BallroomLiquorDetail;
import model.Customer;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookingCrudController {
    public static ArrayList<String> getBallroomIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT BId FROM ballroom");
        ArrayList<String> BIds= new ArrayList<>();
        while (result.next()){
            BIds.add(result.getString(1));
        }
        return BIds;
    }

    public static ArrayList<String> getMenuIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT MId FROM menu");
        ArrayList<String> MIds= new ArrayList<>();
        while (result.next()){
            MIds.add(result.getString(1));
        }
        return MIds;
    }

    public static ArrayList<String> getMusicIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT MuId FROM music");
        ArrayList<String> MuIds= new ArrayList<>();
        while (result.next()){
            MuIds.add(result.getString(1));
        }
        return MuIds;
    }

    public static ArrayList<String> getLiquorIds(String id) throws SQLException, ClassNotFoundException {
        ResultSet result= CrudUtil.execute("SELECT * FROM ballroomLiquorDetail WHERE BallroomId=?", id);
        ArrayList<String> LIds= new ArrayList<>();
        while (result.next()){
            LIds.add(result.getString(2));
        }
        return LIds;
    }
}
