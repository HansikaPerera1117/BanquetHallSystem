package controller;

import com.jfoenix.controls.JFXButton;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import model.Ballroom;
import model.Menu;
import model.MenuChoice;
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

public class MenuFormController implements Navigation{
    public AnchorPane menuContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<Menu> tblMenu;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colChargeNetPerPerson;
    public TextField txtCharge;
    public TextField txtID;
    public TableView<MenuChoice> tblMenuChoice;
    public TableColumn colNum;
    public TableColumn colChoiceName;
    public TableColumn colChoice;
    public Label lblName;
    public JFXButton btnUpdateCharges;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        loadDateAndTime();

        //============menu===============================
        colID.setCellValueFactory(new PropertyValueFactory("MId"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colChargeNetPerPerson.setCellValueFactory(new PropertyValueFactory("ChargeNetPerPerson"));

        //============menu choice===============================
        colNum.setCellValueFactory(new PropertyValueFactory("Num"));
        colChoiceName.setCellValueFactory(new PropertyValueFactory("Name"));
        colChoice.setCellValueFactory(new PropertyValueFactory("Choise"));

        try {
            loadAllMenu();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            loadAllMenuChoice();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(M-)[0-9]{3,5}$");
        Pattern chargeNetPerPersonPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");

        map.put(txtID,idPattern);
        map.put(txtCharge,chargeNetPerPersonPattern);

    }

    private void loadAllMenuChoice() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM menuType");
        ObservableList<MenuChoice> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new MenuChoice(
                            result.getInt("Num"),
                            result.getString("Name"),
                            result.getString("Choise")
                    )
            );
        }
        tblMenuChoice.setItems(obList);
    }

    private void loadAllMenu() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM menu");
        ObservableList<Menu> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Menu(
                            result.getString("MId"),
                            result.getString("Name"),
                            result.getDouble("ChargeNetPerPerson")
                    )
            );
        }
        tblMenu.setItems(obList);
    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(menuContext,"MainForm");

    }

    public void btnUpdateChargeOnAction(ActionEvent actionEvent) {

        Menu m = new Menu(txtID.getText(),lblName.getText(), Double.parseDouble(txtCharge.getText()));

        try{
            boolean isUpdated = CrudUtil.execute("UPDATE menu SET ChargeNetPerPerson=? WHERE MId=?",m.getChargeNetPerPerson(),m.getMId());

            if (isUpdated){
                Notifications notifications = Notifications.create().title("Update Completed !").text("Charge Net Per Person of menu Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllMenu();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {

        try {
            ResultSet result =  CrudUtil.execute("SELECT * FROM menu WHERE MId=?",txtID.getText());
            if (result.next()) {
                lblName.setText(result.getString(2));
                txtCharge.setText(result.getString(3));
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

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnUpdateCharges);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnUpdateCharges);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
