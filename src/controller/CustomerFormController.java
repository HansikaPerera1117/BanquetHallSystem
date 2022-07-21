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
import model.Customer;
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

public class CustomerFormController implements Navigation{
    public AnchorPane customerContext;
    public TableView<Customer> tblCustomer;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colNIC;
    public TableColumn colAddress;
    public TableColumn colTelNo;
    public TableColumn colEmail;
    public TextField txtID;
    public TextField txtName;
    public TextField txtNIC;
    public TextField txtAddress;
    public TextField txtTelNo;
    public TextField txtEmail;
    public Label lblTime;
    public Label lblDate;
    public JFXButton btnUpdateCustomer;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        loadDateAndTime();

        colID.setCellValueFactory(new PropertyValueFactory("CId"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colNIC.setCellValueFactory(new PropertyValueFactory("NIC"));
        colAddress.setCellValueFactory(new PropertyValueFactory("Address"));
        colTelNo.setCellValueFactory(new PropertyValueFactory("TelNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory("Email"));

        try {
            loadAllCustomer();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern IdPattern = Pattern.compile("^(C-)[0-9]{3,5}$");
        Pattern NamePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern NICPattern = Pattern.compile("^([0-9]{10}V)$|^([0-9]{12})$");
        Pattern TelNoPattern = Pattern.compile("^(011|070|071|072|074|075|076|077|078)[0-9]{7}$");
        Pattern EmailPattern = Pattern.compile("^[a-z0-9]{5,30}(@gmail.com|@yahoo.com)$");
        Pattern AddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,30}$");

        map.put(txtID,IdPattern);
        map.put(txtName,NamePattern);
        map.put(txtNIC,NICPattern);
        map.put(txtTelNo,TelNoPattern);
        map.put(txtEmail,EmailPattern);
        map.put(txtAddress,AddressPattern);

    }

    private void loadAllCustomer() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM customer");
        ObservableList<Customer> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Customer(
                            result.getString("CId"),
                            result.getString("Name"),
                            result.getString("NIC"),
                            result.getString("Address"),
                            result.getString("TelNo"),
                            result.getString("Email")
                    )
            );
        }
        tblCustomer.setItems(obList);
    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(customerContext,"MainForm");
    }

    public void btnUpdateCustomerOnAction(ActionEvent actionEvent) {

        Customer c = new Customer(txtID.getText(),txtName.getText(),txtNIC.getText(),txtAddress.getText(),txtTelNo.getText(),txtEmail.getText());

        try{
            boolean isUpdated = CrudUtil.execute("UPDATE customer SET  Name=? , NIC=? , Address=?, TelNo=? , Email=? WHERE CId=?",c.getName(),c.getNIC(),c.getAddress(),c.getTelNo(),c.getEmail(),c.getCId());

            if (isUpdated){
                Notifications notifications = Notifications.create().title("Update Completed !").text("Customer details Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllCustomer();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnUpdateCustomer);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnUpdateCustomer);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void search() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM customer WHERE CId=?",txtID.getText());
        if (result.next()) {
            txtName.setText(result.getString(2));
            txtNIC.setText(result.getString(3));
            txtAddress.setText(result.getString(4));
            txtTelNo.setText(result.getString(5));
            txtEmail.setText(result.getString(6));
        } else {
            Notifications notifications = Notifications.create().title("Empty Result").text("There is no such recode").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
            notifications.darkStyle();
            notifications.showWarning();
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
