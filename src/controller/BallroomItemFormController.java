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
import model.BallroomItem;
import model.BallroomItemDetail;
import model.Customer;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;
import view.tm.BallroomItemTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class BallroomItemFormController implements Navigation{
    public AnchorPane ballroomItemContext;
    public TableView<BallroomItemTM> tblBallroomItem;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colDescription;
    public TableColumn colQty;
    public TableColumn colOption;
    public TableView<BallroomItemDetail> tblBallroomItemDetails;
    public TableColumn colBallroomID;
    public TableColumn colItemID;
    public TableColumn colBIDQty;
    public TextField txtID;
    public TextField txtName;
    public TextField txtDescription;
    public TextField txtQty;
    public Label lblDate;
    public Label lblTime;
    public JFXButton btnAddItem;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();

    public void initialize(){

        loadDateAndTime();

        //===============ballroom item========================================

        colID.setCellValueFactory(new PropertyValueFactory("BItId"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colDescription.setCellValueFactory(new PropertyValueFactory("Description"));
        colQty.setCellValueFactory(new PropertyValueFactory("QtyOnHand"));
        colOption.setCellValueFactory(new PropertyValueFactory("btn"));

        //===============ballroom item detail========================================

        colBallroomID.setCellValueFactory(new PropertyValueFactory("BallroomId"));
        colItemID.setCellValueFactory(new PropertyValueFactory("BallroomItemId"));
        colBIDQty.setCellValueFactory(new PropertyValueFactory("QTY"));


        try {
            loadAllBallroomItems();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        try {
            loadAllBallroomItemDetails();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(BIt-)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,15}$");
        Pattern descriptionPattern = Pattern.compile("^[A-z .]{5,}$");
        Pattern qtyPattern = Pattern.compile("^[1-9][0-9]{0,4}$");

        map.put(txtID,idPattern);
        map.put(txtName,namePattern);
        map.put(txtDescription,descriptionPattern);
        map.put(txtQty,qtyPattern);


    }

    private void loadAllBallroomItemDetails() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM ballroomItemDetail");
        ObservableList<BallroomItemDetail> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new BallroomItemDetail(
                            result.getString("BallroomId"),
                            result.getString("BallroomItemId"),
                            result.getInt("QTY")
                    )
            );
        }
        tblBallroomItemDetails.setItems(obList);

    }

    private void loadAllBallroomItems() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM ballroomItem");
        ObservableList<BallroomItemTM> obList = FXCollections.observableArrayList();

        while (result.next()) {
            Button btn = new Button("Delete");
            BallroomItemTM tm = new BallroomItemTM(
                    result.getString("BItId"),
                    result.getString("Name"),
                    result.getString("Description"),
                    result.getInt("QtyOnHand"),
                    btn

            );

            btn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get().equals(ButtonType.YES)) {



                    BallroomItemTM ballroomItemTM = tblBallroomItem.getSelectionModel().getSelectedItem();


                        try {
                            if (CrudUtil.execute("DELETE FROM ballroomItem WHERE BItId=?", ballroomItemTM.getBItId())) {

                                Notifications notifications = Notifications.create().title("Delete Completed !").text("Ballroom Item Deleted successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                                notifications.darkStyle();
                                notifications.show();

                            } else {

                                Notifications notifications = Notifications.create().title("Delete Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                                notifications.darkStyle();
                                notifications.showError();

                            }

                        } catch (SQLException | ClassNotFoundException exception) {


                        }

                        obList.remove(ballroomItemTM);


                }
            });


            obList.add(tm);
            tblBallroomItem.setItems(obList);
        }
        }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(ballroomItemContext,"MainForm");
    }

    public void btnSaveItemOnAction(ActionEvent actionEvent) {

        BallroomItem bi = new BallroomItem(
                txtID.getText(),txtName.getText(), txtDescription.getText(),
                Integer.parseInt(txtQty.getText())
        );

        if (btnAddItem.getText().equals("Add Items")) {
            try {
                if (CrudUtil.execute("INSERT INTO ballroomItem VALUES (?,?,?,?)", bi.getBItId(), bi.getName(), bi.getDescription(), bi.getQtyOnHand())) {

                    Notifications notifications = Notifications.create().title("Save Completed !").text("Ballroom Item Saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            BallroomItem b = new BallroomItem(
                    txtID.getText(),txtName.getText(), txtDescription.getText(),
                    Integer.parseInt(txtQty.getText())
            );

            try{
                boolean isUpdated = CrudUtil.execute("UPDATE ballroomItem SET Name=? , Description=? , QtyOnHand=?  WHERE BItId=?",b.getName(),b.getDescription(),b.getQtyOnHand(),b.getBItId());
                if (isUpdated){
                    Notifications notifications = Notifications.create().title("Update Completed !").text("Ballroom Item Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllBallroomItems();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void btnNewItemOnAction(ActionEvent actionEvent) {
        txtID.setEditable(true);
        btnAddItem.setText("Add Items");
        txtID.setText("");
        txtName.setText("");
        txtDescription.setText("");
        txtQty.setText("");


    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void search() throws SQLException, ClassNotFoundException {
        btnAddItem.setText("Update Items");
        ResultSet result = CrudUtil.execute("SELECT * FROM ballroomItem WHERE BItId=?",txtID.getText());
        if (result.next()) {
            txtName.setText(result.getString(2));
            txtDescription.setText(result.getString(3));
            txtQty.setText(String.valueOf(result.getInt(4)));
            txtID.setEditable(false);
        } else {
            btnAddItem.setText("Add Items");
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
        ValidationUtil.validate(map,btnAddItem);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddItem);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
