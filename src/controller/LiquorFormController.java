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
import model.BallroomLiquorDetail;
import model.Liquor;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;
import view.tm.LiquorTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class LiquorFormController implements Navigation{
    public AnchorPane liquorContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<BallroomLiquorDetail> tblBallroomLiquorDetails;
    public TableColumn colBallroomID;
    public TableColumn colLiquorID;
    public TableColumn colQty;
    public TextField txtQtyOnHand;
    public TextField txtDescription;
    public TextField txtPrice;
    public TextField txtName;
    public TextField txtID;
    public JFXButton btnAddLiquor;
    public TableColumn colOption;
    public TableColumn colQtyOnHand;
    public TableColumn colDescription;
    public TableColumn colPrice;
    public TableColumn colName;
    public TableColumn cloID;
    public TableView <LiquorTM> tblLiquor;
    public TextField txtBallroomID;
    public TextField txtQty;
    public JFXButton btnAddLiquorBallroom;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> map2 = new LinkedHashMap<>();


    public void initialize(){
        loadDateAndTime();

        //======liquor details==============================================

        cloID.setCellValueFactory(new PropertyValueFactory("LId"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colPrice.setCellValueFactory(new PropertyValueFactory("Price"));
        colDescription.setCellValueFactory(new PropertyValueFactory("Description"));
        colQtyOnHand.setCellValueFactory(new PropertyValueFactory("QtyOnHand"));
        colOption.setCellValueFactory(new PropertyValueFactory("btn"));

        //=============ballroom liquor details====================

        colBallroomID.setCellValueFactory(new PropertyValueFactory("BallroomId"));
        colLiquorID.setCellValueFactory(new PropertyValueFactory("LiquorId"));
        colQty.setCellValueFactory(new PropertyValueFactory("Qty"));


        try {
            loadAllBallroomLiquorDetails();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        try {
            loadAllLiquors();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //=========all liquor==================================
        Pattern IdPattern = Pattern.compile("^(L-)[0-9]{3,5}$");
        Pattern NamePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern PricePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");
        Pattern DescriptionPattern = Pattern.compile("^[A-z 0-9,/%-]{5,35}$");
        Pattern QtyOnHandPattern = Pattern.compile("^[1-9][0-9]{1,3}$");

        map.put(txtID,IdPattern);
        map.put(txtName,NamePattern);
        map.put(txtPrice,PricePattern);
        map.put(txtDescription,DescriptionPattern);
        map.put(txtQtyOnHand,QtyOnHandPattern);

        //==============ballroom liquor =================

        Pattern BallroomIdPattern = Pattern.compile("^(B-)[0-9]{3,5}$");
        Pattern QtyPattern = Pattern.compile("^[1-9][0-9]{0,1}$");

        map2.put(txtBallroomID,BallroomIdPattern);
        map2.put(txtQty,QtyPattern);


    }

    private void loadAllLiquors() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM liquor");
        ObservableList<LiquorTM> obList = FXCollections.observableArrayList();


        while (result.next()) {
            Button btn = new Button("Delete");
            LiquorTM tm = new LiquorTM(
                    result.getString("LId"),
                    result.getString("Name"),
                    result.getDouble("Price"),
                    result.getString("Description"),
                    result.getInt("QtyOnHand"),
                    btn
            );

            btn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get().equals(ButtonType.YES)) {

                    LiquorTM li =tblLiquor.getSelectionModel().getSelectedItem();

                    try {
                        if (CrudUtil.execute("DELETE FROM liquor WHERE LId=?", li.getLId())) {

                            Notifications notifications = Notifications.create().title("Delete Completed !").text("Liquor Deleted successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.show();

                        } else {

                            Notifications notifications = Notifications.create().title("Delete Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.showError();

                        }

                    } catch (SQLException | ClassNotFoundException exception) {

                    }

                    obList.remove(li);

                }
            });

            obList.add(tm);
            tblLiquor.setItems(obList);
        }

    }

    private void loadAllBallroomLiquorDetails() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM ballroomLiquorDetail");
        ObservableList<BallroomLiquorDetail> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new BallroomLiquorDetail(
                            result.getString("BallroomId"),
                            result.getString("LiquorId"),
                            result.getInt("Qty")
                    )
            );
        }
        tblBallroomLiquorDetails.setItems(obList);
    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(liquorContext,"MainForm");
    }

    public void btnSaveLiquorOnAction(ActionEvent actionEvent) {

        Liquor l = new Liquor(
                txtID.getText(),txtName.getText(),Double.parseDouble(txtPrice.getText()),
                txtDescription.getText(),Integer.parseInt(txtQtyOnHand.getText())
        );

        if (btnAddLiquor.getText().equals("Add Liquor")) {
            try {
                if (CrudUtil.execute("INSERT INTO liquor VALUES (?,?,?,?,?)",l.getLId(),l.getName(),l.getPrice(),l.getDescription(),l.getQtyOnHand())) {

                    Notifications notifications = Notifications.create().title("Save Completed !").text("Liquor Saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            Liquor l1 = new Liquor(
                    txtID.getText(),txtName.getText(),Double.parseDouble(txtPrice.getText()),
                    txtDescription.getText(),Integer.parseInt(txtQtyOnHand.getText())
            );

            try{
                boolean isUpdated = CrudUtil.execute("UPDATE liquor SET Name=? , Price=? , Description=? , QtyOnHand=?  WHERE LId=?",l1.getName(),l1.getPrice(),l1.getDescription(),l1.getQtyOnHand(),l1.getLId());
                if (isUpdated){
                    Notifications notifications = Notifications.create().title("Update Completed !").text("Liquor Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllLiquors();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void btnNewLiquorOnAction(ActionEvent actionEvent) {
        txtID.setEditable(true);
        btnAddLiquor.setText("Add Liquor");
        txtID.setText("");
        txtName.setText("");
        txtPrice.setText("");
        txtDescription.setText("");
        txtQtyOnHand.setText("");

    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void search() throws SQLException, ClassNotFoundException {
        btnAddLiquor.setText("Update Liquor");
        ResultSet result = CrudUtil.execute("SELECT * FROM liquor WHERE LId=?",txtID.getText());
        if (result.next()) {
            txtName.setText(result.getString(2));
            txtPrice.setText(String.valueOf(result.getDouble(3)));
            txtDescription.setText((result.getString(4)));
            txtQtyOnHand.setText(String.valueOf(result.getInt(5)));

            txtID.setEditable(false);

        } else {
            btnAddLiquor.setText("Add Liquor");
            Notifications notifications = Notifications.create().title("Empty Result").text("There is no such recode").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
            notifications.darkStyle();
            notifications.showWarning();
        }
    }

    public void btnAddLiquorToBallroomOnAction(ActionEvent actionEvent) {

        try{
            boolean isUpdated =  CrudUtil.execute("UPDATE ballroomLiquorDetail SET  Qty=Qty+? WHERE BallroomId=? ",Integer.parseInt(txtQty.getText()),txtBallroomID.getText());
            if (isUpdated){
                Notifications notifications = Notifications.create().title("Increment Completed !").text("Successfully increased the liquor count...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.show();

            }else{
                Notifications notifications = Notifications.create().title("Increment Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.showError();

            }

        }catch (SQLException | ClassNotFoundException e){

        }

        try {
            loadAllBallroomLiquorDetails();
        } catch (SQLException | ClassNotFoundException e) {
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
        ValidationUtil.validate(map,btnAddLiquor);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddLiquor);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    public void textFields_Key_ReleasedAddLiquorToBallroom(KeyEvent keyEvent) {
        ValidationUtil.validate(map2,btnAddLiquorBallroom);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map2,btnAddLiquorBallroom);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
