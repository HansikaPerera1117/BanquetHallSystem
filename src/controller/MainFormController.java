package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class MainFormController implements Navigation{
    public AnchorPane MainContext;
    public Label lblDate;
    public Label lblTime;



    public void initialize(){
        loadDateAndTime();
    }


    public void logOutOnAction(MouseEvent mouseEvent) throws IOException {
        setUI(MainContext,"StartForm");
    }

    public void openSettingsOnAction(MouseEvent mouseEvent) throws IOException {
        setPage("SettingForm");
    }

    public void openBookingOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"BookingForm");

    }

    public void openBallroomOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"BallroomForm");

    }

    public void openMenuOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"MenuForm");

    }

    public void openCustomerOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"CustomerForm");

    }

    public void openEmployeeOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"EmployeeForm");

    }

    public void openMenuSelectionOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"MenuSelectionForm");

    }

    public void openBallroomItemOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"BallroomItemForm");
    }

    public void openLiquorOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"LiquorForm");
    }

    public void openSupplierOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"SupplierForm");

    }

    public void openMusicOnAction(ActionEvent actionEvent) throws IOException {
        setUI(MainContext,"MusicForm");

    }

    private void loadDateAndTime() {

        lblDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            lblTime.setText(currentTime.getHour() + ":" +
                    currentTime.getMinute() + ":" +
                    currentTime.getSecond());
        }),
                new KeyFrame(Duration.seconds(1))
        );
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }


}
