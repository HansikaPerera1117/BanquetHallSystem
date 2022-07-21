package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import model.Employee;
import model.Supplier;
import model.SupplierType;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;
import view.tm.EmployeeTM;
import view.tm.SupplierTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class SupplierFormController implements Navigation{
    public AnchorPane supplierContext;
    public Label lblTime;
    public Label lblDate;
    public TableView <SupplierType>tblSupplierType;
    public TableColumn colSupplierTypeId;
    public TableColumn colType;
    public TableView <Ballroom>tblBallroom;
    public TableColumn colBallroomID;
    public TableColumn colBallroomName;
    public TableView<SupplierTM> tblSupplier;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colSTId;
    public TableColumn colBId;
    public TableColumn colAddress;
    public TableColumn colTelNo;
    public TableColumn colEmail;
    public TableColumn colAccNum;
    public TableColumn colDescription;
    public TableColumn colOption;
    public JFXComboBox<String> cmbSupplierTypeId;
    public JFXComboBox<String > cmbBallroomId;
    public TextField txtID;
    public TextField txtName;
    public TextField txtAddress;
    public TextField txtTelNo;
    public TextField txtEmail;
    public TextField txtAccNum;
    public TextField txtDescription;
    public JFXButton btnAddSupplier;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){
        loadDateAndTime();
        setBallroomIds();
        setSupplierTypeIds();
        //========supplier ================================
        colID.setCellValueFactory(new PropertyValueFactory("SId"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colSTId.setCellValueFactory(new PropertyValueFactory("SupplierTypeId"));
        colBId.setCellValueFactory(new PropertyValueFactory("BallroomId"));
        colAddress.setCellValueFactory(new PropertyValueFactory("Address"));
        colTelNo.setCellValueFactory(new PropertyValueFactory("TelNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory("Email"));
        colAccNum.setCellValueFactory(new PropertyValueFactory("AccNum"));
        colDescription.setCellValueFactory(new PropertyValueFactory("Description"));
        colOption.setCellValueFactory(new PropertyValueFactory("btn"));

        //========supplier Type==============================
        colSupplierTypeId.setCellValueFactory(new PropertyValueFactory("STId"));
        colType.setCellValueFactory(new PropertyValueFactory("Type"));

        //========Ballroom ===================================
        colBallroomID.setCellValueFactory(new PropertyValueFactory("ID"));
        colBallroomName.setCellValueFactory(new PropertyValueFactory("Name"));


        try {
            loadAllSuppliers();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            loadAllSupplierTypes();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            loadAllBallroom();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern IdPattern = Pattern.compile("^(S-)[0-9]{3,5}$");
        Pattern NamePattern = Pattern.compile("^[A-z ]{3,50}$");
        Pattern AddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,30}$");
        Pattern TelNoPattern = Pattern.compile("^(011|070|071|072|074|075|076|077|078)[0-9]{7}$");
        Pattern EmailPattern = Pattern.compile("^[a-z0-9]{5,30}(@gmail.com|@yahoo.com)$");
        Pattern AccNumPattern = Pattern.compile("^[1-9][0-9]{10,15}$");
        Pattern DescriptionPattern = Pattern.compile("^[A-z .]{5,}$");

        map.put(txtID,IdPattern);
        map.put(txtName,NamePattern);
        map.put(txtAddress,AddressPattern);
        map.put(txtTelNo,TelNoPattern);
        map.put(txtEmail,EmailPattern);
        map.put(txtAccNum,AccNumPattern);
        map.put(txtDescription,DescriptionPattern);


    }

    private void loadAllSuppliers() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM supplier");
        ObservableList<SupplierTM> obList = FXCollections.observableArrayList();


        while (result.next()) {
            Button btn = new Button("Delete");
            SupplierTM tm = new SupplierTM(
                    result.getString("SId"),
                    result.getString("Name"),
                    result.getString("SupplierTypeId"),
                    result.getString("BallroomId"),
                    result.getString("Address"),
                    result.getString("TelNo"),
                    result.getString("Email"),
                    result.getString("AccNum"),
                    result.getString("Description"),
                    btn
            );

            btn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get().equals(ButtonType.YES)) {

                       SupplierTM stm= tblSupplier.getSelectionModel().getSelectedItem();


                    try {
                        if (CrudUtil.execute("DELETE FROM supplier WHERE SId=?", stm.getSId())) {

                            Notifications notifications = Notifications.create().title("Delete Completed !").text("Supplier Deleted successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.show();

                        } else {

                            Notifications notifications = Notifications.create().title("Delete Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.showError();

                        }

                    } catch (SQLException | ClassNotFoundException exception) {


                    }

                    obList.remove(stm);


                }
            });


            obList.add(tm);
            tblSupplier.setItems(obList);
        }

    }

    private void setSupplierTypeIds() {
        try {
            ObservableList<String> SupplierTypeIdObList = FXCollections.observableArrayList(SupplierCrudController.getSupplierTypeIds());
            cmbSupplierTypeId.setItems(SupplierTypeIdObList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setBallroomIds() {
        try {
            ObservableList<String> BallroomIdObList = FXCollections.observableArrayList(SupplierCrudController.getBallroomIds());
            cmbBallroomId.setItems(BallroomIdObList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllBallroom() throws SQLException, ClassNotFoundException {
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

    private void loadAllSupplierTypes() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM supplierType");
        ObservableList<SupplierType> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new SupplierType(
                            result.getString("STId"),
                            result.getString("Type")
                    )
            );
        }
        tblSupplierType.setItems(obList);
    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(supplierContext,"MainForm");

    }

    public void btnAddSupplierOnAction(ActionEvent actionEvent) {


        Supplier s = new Supplier(
                txtID.getText(),txtName.getText(),cmbSupplierTypeId.getValue(),
                cmbBallroomId.getValue(),txtAddress.getText(), txtTelNo.getText(),
                txtEmail.getText(),txtAccNum.getText(), txtDescription.getText()
        );

        if (btnAddSupplier.getText().equals("Add Supplier")) {
            try {
                if (CrudUtil.execute("INSERT INTO supplier VALUES (?,?,?,?,?,?,?,?,?)",s.getSId(),s.getName(),s.getSupplierTypeId(),s.getBallroomId(),s.getAddress(),s.getTelNo(),s.getEmail(),s.getAccNum(),s.getDescription())) {

                    Notifications notifications = Notifications.create().title("Save Completed !").text("Supplier Saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                    notifications.darkStyle();
                    notifications.show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();

                Notifications notifications = Notifications.create().title("Save Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.showError();

            }
        }else {
            Supplier s1 = new Supplier(
                    txtID.getText(),txtName.getText(),cmbSupplierTypeId.getValue(),
                    cmbBallroomId.getValue(),txtAddress.getText(), txtTelNo.getText(),
                    txtEmail.getText(),txtAccNum.getText(), txtDescription.getText()
            );

            try{
                boolean isUpdated = CrudUtil.execute("UPDATE supplier SET Name=? , SupplierTypeId=? , BallroomId=? , Address=? , TelNo=? , Email=? , AccNum=? , Description=?   WHERE SId=?",s1.getName(),s1.getSupplierTypeId(),s1.getBallroomId(),s1.getAddress(),s1.getTelNo(),s1.getEmail(),s1.getAccNum(),s1.getDescription(),s1.getSId());
                if (isUpdated){
                    Notifications notifications = Notifications.create().title("Update Completed !").text("Supplier Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                    notifications.darkStyle();
                    notifications.show();

                }else{
                    Notifications notifications = Notifications.create().title("Update Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                    notifications.darkStyle();
                    notifications.showError();

                }

            }catch (SQLException | ClassNotFoundException e){

            }

        }
        try {
            loadAllSuppliers();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnNewSupplierOnAction(ActionEvent actionEvent) {
        txtID.setEditable(true);
        btnAddSupplier.setText("Add Supplier");
        txtID.setText("");
        txtName.setText("");
        cmbSupplierTypeId.setValue("");
        cmbBallroomId.setValue("");
        txtAddress.setText("");
        txtTelNo.setText("");
        txtEmail.setText("");
        txtAccNum.setText("");
        txtDescription.setText("");
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void search() throws SQLException, ClassNotFoundException {
        btnAddSupplier.setText("Update Supplier");
        ResultSet result = CrudUtil.execute("SELECT * FROM supplier WHERE SId=?",txtID.getText());
        if (result.next()) {
            txtName.setText(result.getString(2));
            cmbSupplierTypeId.setValue(result.getString(3));
            cmbBallroomId.setValue((result.getString(4)));
            txtAddress.setText(result.getString(5));
            txtTelNo.setText(result.getString(6));
            txtEmail.setText((result.getString(7)));
            txtAccNum.setText(result.getString(8));
            txtDescription.setText(result.getString(9));

            txtID.setEditable(false);
        } else {
            btnAddSupplier.setText("Add Supplier");
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

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddSupplier);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddSupplier);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
