package controller;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import util.CrudUtil;

import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LogInFormController implements Navigation{
    public AnchorPane LogInContext;
    public JFXTextField txtUserName;
    public JFXPasswordField pwdPassword;
    int attempts=0;
    String username;
    String password;

    public void loginOnAction(ActionEvent actionEvent) throws IOException {
        try {
            ResultSet result = CrudUtil.execute("SELECT * FROM login");
            while (result.next()){
             username = result.getString("UserName");
                 password = result.getString("Password");

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        attempts++;
        if (attempts<=3){
            if (txtUserName.getText().equals(username) && pwdPassword.getText().equals(password)){

                Notifications notifications = Notifications.create().title("LogIn Successful !").text("You lodged in to the system  successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

                setUI(LogInContext,"MainForm");

            }else if(txtUserName.getText().equals("") && pwdPassword.getText().equals("")){

                Notifications notifications = Notifications.create().title("Data Required !").text("Please enter your Username and Password...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            }else{

                Notifications notifications = Notifications.create().title("LogIn Unsuccessful !").text("Please check and  Try again!").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            }

        }else{
            txtUserName.setEditable(false);
            pwdPassword.setEditable(false);

        }
    }
}
