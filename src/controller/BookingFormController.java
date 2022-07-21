package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
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
import model.Booking;
import model.Customer;
import model.Liquor;
import model.Supplier;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class BookingFormController implements Navigation{
    public AnchorPane bookingContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<Booking> tblBooking;
    public TableColumn colBookingID;
    public TableColumn colDate;
    public TableColumn colTime;
    public TableColumn colCustomerID;
    public TableColumn colBallroomID;
    public TableColumn colMenuID;
    public TableColumn colMusicID;
    public TableColumn colDescription;
    public TableColumn colGuestCount;
    public TableColumn colLiquorID;
    public TableColumn colLiquorCount;
    public JFXTimePicker tmPicker;
    public JFXDatePicker dtPicker;
    public Label lblCusId;
    public JFXComboBox<String> cmbBallroomID;
    public JFXComboBox<String> cmbMenuID;
    public JFXComboBox<String > cmbMusicID;
    public JFXComboBox<String> cmbLiquorID;
    public JFXButton btnPlaceBooking;
    public Label lblTotal;
    public Label lblBalance;
    public TextField txtCID;
    public TextField txtCNIC;
    public TextField txtCName;
    public TextField txtCTelNo;
    public TextField txtCEmail;
    public TextField txtCAddress;
    public TextField txtBookingID;
    public TextField txtDescription;
    public TextField txtLiquorCount;
    public TextField txtAdvance;
    public TextField txtGuestCount;
    public JFXButton btnPrintBill;


    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> map2 = new LinkedHashMap<>();
    LinkedHashMap<TextField, Pattern> map3 = new LinkedHashMap<>();

    double total=0;

    public void initialize(){
        loadDateAndTime();
        setBallroomIds();
        setMenuIds();
        setMusicIds();

        cmbBallroomID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            setLiquorIds(newValue);
        });


        colBookingID.setCellValueFactory(new PropertyValueFactory("BookingId"));
        colDate.setCellValueFactory(new PropertyValueFactory("Date"));
        colTime.setCellValueFactory(new PropertyValueFactory("Time"));
        colGuestCount.setCellValueFactory(new PropertyValueFactory("GuestCount"));
        colCustomerID.setCellValueFactory(new PropertyValueFactory("CustomerId"));
        colBallroomID.setCellValueFactory(new PropertyValueFactory("BallroomId"));
        colMenuID.setCellValueFactory(new PropertyValueFactory("MenuId"));
        colMusicID.setCellValueFactory(new PropertyValueFactory("MusicId"));
        colLiquorID.setCellValueFactory(new PropertyValueFactory("LiquorId"));
        colLiquorCount.setCellValueFactory(new PropertyValueFactory("LiquorCount"));
        colDescription.setCellValueFactory(new PropertyValueFactory("Description"));


        try {
            loadAllBookings();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }

        //=================add booking================================================================================
        //=============customer========================

        Pattern CIdPattern = Pattern.compile("^(C-)[0-9]{3,5}$");
        Pattern CNamePattern = Pattern.compile("^[A-z ]{3,25}$");
        Pattern CNICPattern = Pattern.compile("^([0-9]{10}V)$|^([0-9]{12})$");
        Pattern CTelNoPattern = Pattern.compile("^(011|070|071|072|074|075|076|077|078)[0-9]{7}$");
        Pattern CEmailPattern = Pattern.compile("^[a-z0-9]{5,30}(@gmail.com|@yahoo.com)$");
        Pattern CAddressPattern = Pattern.compile("^[A-z0-9 ,/]{4,30}$");

        //==============booking=========================

        Pattern BookingIdPattern = Pattern.compile("^(Booking-)[0-9]{3,5}$");
        Pattern GuestCountPattern = Pattern.compile("^[1-4][0-9]{1,2}$");
        Pattern DescriptionPattern = Pattern.compile("^[A-z ]{5,20}$");
        Pattern LiquorCountPattern = Pattern.compile("^[1-9][0-9]{0,1}$");
        Pattern AdvancePattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");


        map.put(txtCID,CIdPattern);
        map.put(txtCName,CNamePattern);
        map.put(txtCNIC,CNICPattern);
        map.put(txtCTelNo,CTelNoPattern);
        map.put(txtCEmail,CEmailPattern);
        map.put(txtCAddress,CAddressPattern);
        map.put(txtBookingID,BookingIdPattern);
        map.put(txtGuestCount,GuestCountPattern);
        map.put(txtDescription,DescriptionPattern);
        map.put(txtLiquorCount,LiquorCountPattern);


        //=================update booking================================================================================
        map2.put(txtBookingID,BookingIdPattern);
        map2.put(txtGuestCount,GuestCountPattern);
        map2.put(txtDescription,DescriptionPattern);

        //===================advance==============================
        map3.put(txtAdvance,AdvancePattern);



    }

    private void setLiquorIds(String selectedBallroomId) {
        try {
            ObservableList<String> LiquorIdObList = FXCollections.observableArrayList(BookingCrudController.getLiquorIds(selectedBallroomId));
            cmbLiquorID.setItems(LiquorIdObList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setMusicIds() {
        try {
            ObservableList<String> MusicIdObList = FXCollections.observableArrayList(BookingCrudController.getMusicIds());
            cmbMusicID.setItems(MusicIdObList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setMenuIds() {
        try {
            ObservableList<String> MenuIdObList = FXCollections.observableArrayList(BookingCrudController.getMenuIds());
            cmbMenuID.setItems(MenuIdObList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setBallroomIds() {
        try {
            ObservableList<String> BallroomIdObList = FXCollections.observableArrayList(BookingCrudController.getBallroomIds());
            cmbBallroomID.setItems(BallroomIdObList);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadAllBookings() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM booking");
        ObservableList<Booking> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new Booking(
                            result.getString("BookingId"),
                            result.getString("Date"),
                            result.getString("Time"),
                            result.getInt("GuestCount"),
                            result.getString("CustomerId"),
                            result.getString("BallroomId"),
                            result.getString("MenuId"),
                            result.getString("MusicId"),
                            result.getString("LiquorId"),
                            result.getInt("LiquorCount"),
                            result.getString("Description")
                    )
            );
        }
        tblBooking.setItems(obList);
    }

    public void printBillOnAction(ActionEvent actionEvent) throws IOException {
        String customerID = txtCID.getText();
        String customerName = txtCName.getText();
        String BookingID = txtBookingID.getText();
        String BookingDate = String.valueOf(dtPicker.getValue());
        String BookingTime = String.valueOf(tmPicker.getValue());
        int GuestCount = Integer.parseInt(txtGuestCount.getText());
        String Description = txtDescription.getText();
        String BallroomID =cmbBallroomID.getValue();
        String MenuID = cmbMenuID.getValue();
        String MusicID = cmbMusicID.getValue();
        String LiquorId = cmbLiquorID.getValue();
        int LiquorCount = Integer.parseInt(txtLiquorCount.getText());
        double Total = Double.parseDouble(lblTotal.getText());
        double Advance = Double.parseDouble(txtAdvance.getText());
        double Balance = Double.parseDouble(lblBalance.getText());

        HashMap paramMap = new HashMap();
        paramMap.put("CustomerID", customerID);
        paramMap.put("Customer Name", customerName);
        paramMap.put("Booking ID", BookingID);
        paramMap.put("Booking Date", BookingDate);
        paramMap.put("Booking Time", BookingTime);
        paramMap.put("Guest Count", GuestCount);
        paramMap.put("Description", Description);
        paramMap.put("BallroomID", BallroomID);
        paramMap.put("Menu ID", MenuID);
        paramMap.put("Music ID", MusicID);
        paramMap.put("Liquor ID", LiquorId);
        paramMap.put("Liquor Count", LiquorCount);
        paramMap.put("Total", Total);
        paramMap.put("Advance", Advance);
        paramMap.put("Balance", Balance);


        try {
            JasperReport compileReport = (JasperReport) JRLoader.loadObject(this.getClass().getResource("/view/reports/BookingBill.jasper"));
            JasperPrint jasperPrint = JasperFillManager.fillReport(compileReport, paramMap, new JREmptyDataSource(1));
            JasperViewer.viewReport(jasperPrint, false);

        } catch (JRException e) {
            e.printStackTrace();
        }



    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(bookingContext,"MainForm");
    }

    public void btnPlaceBookingOnAction(ActionEvent actionEvent) {

        Customer c = new Customer(
                txtCID.getText(),txtCName.getText(),txtCNIC.getText(),
                txtCAddress.getText(),txtCTelNo.getText(),txtCEmail.getText()
        );

        lblCusId.setText(txtCID.getText());

        Booking b = new Booking(
                txtBookingID.getText(),String.valueOf(dtPicker.getValue()),String.valueOf(tmPicker.getValue()),
                Integer.parseInt(txtGuestCount.getText()),lblCusId.getText(),cmbBallroomID.getValue(), cmbMenuID.getValue(),
                cmbMusicID.getValue(), cmbLiquorID.getValue(),Integer.parseInt(txtLiquorCount.getText()),txtDescription.getText()
        );

        if (btnPlaceBooking.getText().equals("Place Booking")) {
            //==========customer==============================================================
            try {
                if (CrudUtil.execute("INSERT INTO customer VALUES (?,?,?,?,?,?)",c.getCId(),c.getName(),c.getNIC(),c.getAddress(),c.getTelNo(),c.getEmail())) {

                    Notifications notifications = Notifications.create().title("Save Completed !").text("Customer Saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                    notifications.darkStyle();
                    notifications.show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();

                Notifications notifications = Notifications.create().title("Save Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.showError();

            }
            //===================booking============================================
            try {
                if (CrudUtil.execute("INSERT INTO booking VALUES (?,?,?,?,?,?,?,?,?,?,?)",b.getBookingId(),b.getDate(),b.getTime(),b.getGuestCount(),b.getCustomerId(),b.getBallroomId(),b.getMenuId(),b.getMusicId(),b.getLiquorId(),b.getLiquorCount(),b.getDescription())) {

                    Notifications notifications = Notifications.create().title("Save Completed !").text("Booking Placed successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                    notifications.darkStyle();
                    notifications.show();
                }
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();

                Notifications notifications = Notifications.create().title("Save Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                notifications.darkStyle();
                notifications.showError();

            }
            //====================Update (-) liquor count ========================================
            try {
                CrudUtil.execute("UPDATE liquor SET  QtyOnHand=QtyOnHand-? WHERE LId=?",Integer.parseInt(txtLiquorCount.getText()),cmbLiquorID.getValue());
                CrudUtil.execute("UPDATE ballroomLiquorDetail SET  Qty=Qty-? WHERE LiquorId=? AND BallroomId=? ",Integer.parseInt(txtLiquorCount.getText()),cmbLiquorID.getValue(),cmbBallroomID.getValue());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

            //=======================total price==========================================


            ResultSet result = null;
            try {
                //=============ballroom price=========================
                result = CrudUtil.execute("SELECT * FROM ballroom WHERE BId=?",cmbBallroomID.getValue());
                while (result.next()) {
                    total += (result.getDouble(4));
                }

                //=============menu price=========================
                result = CrudUtil.execute("SELECT * FROM menu WHERE MId=?",cmbMenuID.getValue());
                while (result.next()) {
                    total += (result.getDouble(3)*Integer.parseInt(txtGuestCount.getText()));
                }

                //=============music price=========================
                result = CrudUtil.execute("SELECT * FROM music WHERE MuId=?",cmbMusicID.getValue());
                while (result.next()) {
                    total += (result.getDouble(4));
                }

                //=============Liquor price=========================
                result =  CrudUtil.execute("SELECT * FROM liquor WHERE LId=?",cmbLiquorID.getValue());

                while (result.next()) {
                    total += (result.getDouble(3))*Double.parseDouble(txtLiquorCount.getText());
                }

                lblTotal.setText(String.valueOf(total));


            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }



            //===================Update  ===========================
        }else {
            Booking b1 = new Booking(
                    txtBookingID.getText(),String.valueOf(dtPicker.getValue()),String.valueOf(tmPicker.getValue()),
                    Integer.parseInt(txtGuestCount.getText()),lblCusId.getText(),cmbBallroomID.getValue(), cmbMenuID.getValue(),
                    cmbMusicID.getValue(), cmbLiquorID.getValue(),Integer.parseInt(txtLiquorCount.getText()),txtDescription.getText()
            );

            try{
                boolean isUpdated = CrudUtil.execute("UPDATE booking SET Date=? , Time=? , Description=?  WHERE BookingId=?",b1.getDate(),b1.getTime(),b1.getDescription(),b1.getBookingId());
                if (isUpdated){
                    Notifications notifications = Notifications.create().title("Update Completed !").text("Booking Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllBookings();
        } catch (SQLException |ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void textFields_Key_Released(KeyEvent keyEvent) {
        if (btnPlaceBooking.getText().equals("Place Booking")){
            ValidationUtil.validate(map,btnPlaceBooking);

            if (keyEvent.getCode() == KeyCode.ENTER) {
                Object response =  ValidationUtil.validate(map,btnPlaceBooking);;

                if (response instanceof TextField) {
                    TextField textField = (TextField) response;
                    textField.requestFocus();
                }
            }


        }else{
            ValidationUtil.validate(map2,btnPlaceBooking);

            if (keyEvent.getCode() == KeyCode.ENTER) {
                Object response =  ValidationUtil.validate(map2,btnPlaceBooking);

                if (response instanceof TextField) {
                    TextField textField = (TextField) response;
                    textField.requestFocus();
                }
            }
        }

    }

    public void textFields_Key_ReleasedAdvance(KeyEvent keyEvent) {
        ValidationUtil.validate(map3,btnPrintBill);
        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map3,btnPrintBill);

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }

    public void txtSetBalanceOnAction(ActionEvent actionEvent) {

        double tempTotal=total;
        double balance = tempTotal-Double.parseDouble(txtAdvance.getText());
        lblBalance.setText(String.valueOf(balance));

    }

    public void btnNewBookingOnAction(ActionEvent actionEvent) {
        txtCID.setEditable(true);
        txtCName.setEditable(true);
        txtCNIC.setEditable(true);
        txtCTelNo.setEditable(true);
        txtCEmail.setEditable(true);
        txtCAddress.setEditable(true);
        txtBookingID.setEditable(true);
        txtGuestCount.setEditable(true);
        cmbBallroomID.setDisable(false);
        cmbMenuID.setDisable(false);
        cmbMusicID.setDisable(false);
        cmbLiquorID.setDisable(false);
        txtLiquorCount.setEditable(true);

        btnPlaceBooking.setText("Place Booking");
        txtBookingID.setText("");
        dtPicker.setValue(null);
        tmPicker.setValue(null);
        txtGuestCount.setText("");
        lblCusId.setText("");
        cmbBallroomID.setValue("");
        cmbMenuID.setValue("");
        cmbMusicID.setValue("");
        cmbLiquorID.setValue("");
        txtDescription.setText("");
        txtLiquorCount.setText("");

        txtCID.setText("");
        txtCName.setText("");
        txtCNIC.setText("");
        txtCTelNo.setText("");
        txtCEmail.setText("");
        txtCAddress.setText("");

        txtAdvance.setText("");
        lblTotal.setText("");
        lblBalance.setText("");

    }

    public void btnSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void search() throws SQLException, ClassNotFoundException {
        btnPlaceBooking.setText("Update Booking");

        ResultSet result = CrudUtil.execute("SELECT * FROM booking WHERE BookingId=?",txtBookingID.getText());
        if (result.next()) {

            txtCID.setEditable(false);
            txtCName.setEditable(false);
            txtCNIC.setEditable(false);
            txtCTelNo.setEditable(false);
            txtCEmail.setEditable(false);
            txtCAddress.setEditable(false);

            dtPicker.setValue(LocalDate.parse(result.getString(2)));
            tmPicker.setValue(LocalTime.parse(result.getString(3)));
            txtGuestCount.setText((result.getString(4)));
            lblCusId.setText((result.getString(5)));
            cmbBallroomID.setValue(result.getString(6));
            cmbMenuID.setValue(result.getString(7));
            cmbMusicID.setValue((result.getString(8)));
            cmbLiquorID.setValue((result.getString(9)));
            txtLiquorCount.setText((result.getString(10)));
            txtDescription.setText(result.getString(11));

            txtBookingID.setEditable(false);
            txtGuestCount.setEditable(false);
            cmbBallroomID.setDisable(true);
            cmbMenuID.setDisable(true);
            cmbMusicID.setDisable(true);
            cmbLiquorID.setDisable(true);
            txtLiquorCount.setEditable(false);



        } else {
            btnPlaceBooking.setText("Place Booking");
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
