package controller;

import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmployeeCrudController {

    public static ArrayList<String> getBallroomIds() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT BId FROM ballroom");
        ArrayList<String> BIds= new ArrayList<>();
        while (result.next()){
            BIds.add(result.getString(1));
        }
        return BIds;
    }


}
