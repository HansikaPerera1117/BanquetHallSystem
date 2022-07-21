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
import model.BallroomItem;
import model.Employee;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;
import view.tm.BallroomItemTM;
import view.tm.EmployeeTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class EmployeeFormController implements Navigation{
    public AnchorPane employeeContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<EmployeeTM> tblEmployee;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colBallroomId;
    public TableColumn colGender;
    public TableColumn colNIC;
    public TableColumn colAddress;
    public TableColumn colTelNo;
    public TableColumn colEmail;
    public TableColumn colType;
    public TableColumn colOption;
    public JFXButton btnAddEmployee;
    public JFXComboBox<String> cmbBallroomId;
    public TextField txtID;
    public TextField txtName;
    public TextField txtGender;
    public TextField txtNIC;
    public TextField txtAddress;
    public TextField txtTelNo;
    public TextField txtEmail;
    public TextField txtType;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();


    public void initialize(){
        loadDateAndTime();
        setBallroomIds();

        colID.setCellValueFactory(new PropertyValueFactory("EId"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colBallroomId.setCellValueFactory(new PropertyValueFactory("BallroomId"));
        colGender.setCellValueFactory(new PropertyValueFactory("Gender"));
        colNIC.setCellValueFactory(new PropertyValueFactory("NIC"));
        colAddress.setCellValueFactory(new PropertyValueFactory("Address"));
        colTelNo.setCellValueFactory(new PropertyValueFactory("TelNo"));
        colEmail.setCellValueFactory(new PropertyValueFactory("Email"));
        colType.setCellValueFactory(new PropertyValueFactory("Type"));
        colOption.setCellValueFactory(new PropertyValueFactory("btn"));

        try {
            loadAllEmployees();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern IdPattern = Pattern.compile("^(E-)[0-9]{3,5}$");
        Pattern NamePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern GenderPattern = Pattern.compile("^(Male)$|^(Female)$");
        Pattern NICPattern = Pattern.compile("^([0-9]{10}V)$|^([0-9]{12})$");
        Pattern AddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,30}$");
        Pattern TelNoPattern = Pattern.compile("^(011|070|071|072|074|075|076|077|078)[0-9]{7}$");
        Pattern EmailPattern = Pattern.compile("^[a-z0-9]{5,30}(@gmail.com|@yahoo.com)$");
        Pattern TypePattern = Pattern.compile("^[A-z]{3,15}$");

        map.put(txtID,IdPattern);
        map.put(txtName,NamePattern);
        map.put(txtGender,GenderPattern);
        map.put(txtNIC,NICPattern);
        map.put(txtAddress,AddressPattern);
        map.put(txtTelNo,TelNoPattern);
        map.put(txtEmail,EmailPattern);
        map.put(txtType,TypePattern);

    }

    private void loadAllEmployees() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM employee");
        ObservableList<EmployeeTM> obList = FXCollections.observableArrayList();


        while (result.next()) {
            Button btn = new Button("Delete");
            EmployeeTM tm = new EmployeeTM(
                    result.getString("EId"),
                    result.getString("Name"),
                    result.getString("BallroomId"),
                    result.getString("Gender"),
                    result.getString("NIC"),
                    result.getString("Address"),
                    result.getString("TelNo"),
                    result.getString("Email"),
                    result.getString("Type"),
                    btn
            );

            btn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get().equals(ButtonType.YES)) {


                   EmployeeTM employeeTM= tblEmployee.getSelectionModel().getSelectedItem();


                    try {
                        if (CrudUtil.execute("DELETE FROM employee WHERE EId=?", employeeTM.getEId())) {

                            Notifications notifications = Notifications.create().title("Delete Completed !").text("Employee Deleted successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.show();

                        } else {

                            Notifications notifications = Notifications.create().title("Delete Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.showError();

                        }

                    } catch (SQLException | ClassNotFoundException exception) {


                    }

                    obList.remove(employeeTM);


                }
            });


            obList.add(tm);
            tblEmployee.setItems(obList);
        }

    }

    private void setBallroomIds(){
        try {
            ObservableList<String> BallroomIdObList = FXCollections.observableArrayList(EmployeeCrudController.getBallroomIds());
            cmbBallroomId.setItems(BallroomIdObList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(employeeContext,"MainForm");
    }

    public void btnAddEmployeeOnAction(ActionEvent actionEvent) {

        Employee em = new Employee(
                txtID.getText(),txtName.getText(),cmbBallroomId.getValue(),
                txtGender.getText(),txtNIC.getText(), txtAddress.getText(),
                txtTelNo.getText(),txtEmail.getText(), txtType.getText()
        );

        if (btnAddEmployee.getText().equals("Add Employee")) {
            try {
                if (CrudUtil.execute("INSERT INTO employee VALUES (?,?,?,?,?,?,?,?,?)",em.getEId(),em.getName(),em.getBallroomId(),em.getGender(),em.getNIC(),em.getAddress(),em.getTelNo(),em.getEmail(),em.getType())) {

                    Notifications notifications = Notifications.create().title("Save Completed !").text("Employee Saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            Employee em1 = new Employee(
                    txtID.getText(),txtName.getText(),cmbBallroomId.getValue(),
                    txtGender.getText(),txtNIC.getText(), txtAddress.getText(),
                    txtTelNo.getText(),txtEmail.getText(), txtType.getText()
            );

            try{
                boolean isUpdated = CrudUtil.execute("UPDATE employee SET Name=? , BallroomId=? , Gender=? , NIC=? , Address=? , TelNo=? , Email=? , Type=?   WHERE EId=?",em1.getName(),em1.getBallroomId(),em1.getGender(),em1.getNIC(),em1.getAddress(),em1.getTelNo(),em1.getEmail(),em1.getType(),em1.getEId());
                if (isUpdated){
                    Notifications notifications = Notifications.create().title("Update Completed !").text("Employee Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllEmployees();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnAddEmployee);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddEmployee);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnNewEmployeeOnAction(ActionEvent actionEvent) {
        txtID.setEditable(true);
        btnAddEmployee.setText("Add Employee");
        txtID.setText("");
        txtName.setText("");
        cmbBallroomId.setValue("");
        txtGender.setText("");
        txtNIC.setText("");
        txtAddress.setText("");
        txtTelNo.setText("");
        txtEmail.setText("");
        txtType.setText("");

    }

    private void search() throws SQLException, ClassNotFoundException {
        btnAddEmployee.setText("Update Employee");
        ResultSet result = CrudUtil.execute("SELECT * FROM employee WHERE EId=?",txtID.getText());
        if (result.next()) {
            txtName.setText(result.getString(2));
            cmbBallroomId.setValue(result.getString(3));
            txtGender.setText((result.getString(4)));
            txtNIC.setText(result.getString(5));
            txtAddress.setText(result.getString(6));
            txtTelNo.setText((result.getString(7)));
            txtEmail.setText(result.getString(8));
            txtType.setText(result.getString(9));

            txtID.setEditable(false);

        } else {
            btnAddEmployee.setText("Add Employee");
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
