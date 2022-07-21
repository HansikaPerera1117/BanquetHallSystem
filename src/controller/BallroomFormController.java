package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Ballroom;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class BallroomFormController implements Navigation{
    public AnchorPane ballroomContext;
    public TableView<Ballroom> tblBallroom;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colPrice;
    public TableColumn colDescription;
    public Label lblName;
    public TextField txtPrice;
    public Label lblDescription;
    public TextField txtID;
    public Label lblDate;
    public Label lblTime;
    public JFXButton btnUpdatePrice;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        loadDateAndTime();


        colID.setCellValueFactory(new PropertyValueFactory("ID"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colDescription.setCellValueFactory(new PropertyValueFactory("Description"));
        colPrice.setCellValueFactory(new PropertyValueFactory("Price"));

        try {
            loadAllBallrooms();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(B-)[0-9]{3,5}$");
        Pattern pricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");

        map.put(txtID,idPattern);
        map.put(txtPrice,pricePattern);


    }

    private void loadAllBallrooms() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM ballroom");
        ObservableList<Ballroom> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Ballroom(
                            result.getString("BId"),
                            result.getString("Name"),
                            result.getString("Description"),
                            result.getDouble("Price")
                    )
            );
        }
        tblBallroom.setItems(obList);
    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(ballroomContext,"MainForm");
    }

    public void btnUpdateBallroomPriceOnAction(ActionEvent actionEvent) {

        Ballroom b = new Ballroom(
                txtID.getText(),lblName.getText(), lblDescription.getText(),
                Double.parseDouble(txtPrice.getText())
        );

        try{
            boolean isUpdated = CrudUtil.execute("UPDATE ballroom SET Price=? WHERE BId=?",b.getPrice(),b.getID());
            if (isUpdated){
                Notifications notifications = Notifications.create().title("Update Completed !").text("Price of ballroom Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            }else{
                Notifications notifications = Notifications.create().title("Update Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.showError();

            }

        }catch (SQLException | ClassNotFoundException e){

        }

        try {
            loadAllBallrooms();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnUpdatePrice);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnUpdatePrice);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {

        try {
            ResultSet result =  CrudUtil.execute("SELECT * FROM ballroom WHERE BId=?",txtID.getText());
            if (result.next()) {
                lblName.setText(result.getString(2));
                lblDescription.setText(result.getString(3));
                txtPrice.setText(String.valueOf(result.getDouble(4)));
            } else {

                Notifications notifications = Notifications.create().title("Empty Result").text("There is no such recode").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.showWarning();

            }
        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }

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
