package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.util.Duration;
import model.Profile;
import org.controlsfx.control.Notifications;
import util.CrudUtil;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SettingFormController {
    public JFXPasswordField pwdOldPassword;
    public JFXTextField txtUserName;
    public JFXPasswordField pwdNewPassword;

    public void btnResetOnAction(ActionEvent actionEvent) {
        Profile p = new Profile(txtUserName.getText(),pwdNewPassword.getText());

        try{
            boolean isUpdated = CrudUtil.execute("UPDATE login SET Password=?  WHERE UserName=?",p.getPassword(),p.getUserName());
            if (isUpdated){
                Notifications notifications = Notifications.create().title("Password Changed !").text("Reset Password successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            }else{
                Notifications notifications = Notifications.create().title("Error  !").text("Unsuccessful ! Try again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.showError();

            }

        }catch (SQLException | ClassNotFoundException e){

        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM login WHERE UserName=?",txtUserName.getText());
        if (result.next()) {

            txtUserName.setText(result.getString(1));

        } else {

            Notifications notifications = Notifications.create().title("Invalid Username").text(" Username is invalid...Please check and try again!").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
            notifications.darkStyle();
            notifications.showWarning();
        }
    }

    public void pwdSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM login WHERE Password=?",pwdOldPassword.getText());
        if (result.next()) {
            pwdOldPassword.setText(result.getString(2));

        } else {
            Notifications notifications = Notifications.create().title("Wrong Password").text(" Old Password  is invalid...Please check and try again!").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
            notifications.darkStyle();
            notifications.showWarning();
        }
    }
}
