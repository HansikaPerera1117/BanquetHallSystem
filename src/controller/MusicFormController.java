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
import model.Liquor;
import model.Music;
import org.controlsfx.control.Notifications;
import util.CrudUtil;
import util.ValidationUtil;
import view.tm.LiquorTM;
import view.tm.MusicTM;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.regex.Pattern;

public class MusicFormController implements Navigation{
    public AnchorPane musicContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<MusicTM> tblMusic;
    public TableColumn colID;
    public TableColumn colName;
    public TableColumn colDescription;
    public TableColumn colPayment;
    public TableColumn colOption;
    public JFXButton btnAddMusic;
    public TextField txtID;
    public TextField txtName;
    public TextField txtPayment;
    public TextField txtDescription;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();


    public void initialize(){
        loadDateAndTime();

        colID.setCellValueFactory(new PropertyValueFactory("MuId"));
        colName.setCellValueFactory(new PropertyValueFactory("Name"));
        colDescription.setCellValueFactory(new PropertyValueFactory("Description"));
        colPayment.setCellValueFactory(new PropertyValueFactory("Payment"));
        colOption.setCellValueFactory(new PropertyValueFactory("btn"));


        try {
            loadAllMusic();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern idPattern = Pattern.compile("^(Mu-)[0-9]{3,5}$");
        Pattern namePattern = Pattern.compile("^[A-z ]{3,15}$");
        Pattern paymentPattern = Pattern.compile("^[1-9][0-9]*(.[0-9]{1,2})?$");
        Pattern descriptionPattern = Pattern.compile("^[A-z .]{5,}$");

        map.put(txtID,idPattern);
        map.put(txtName,namePattern);
        map.put(txtPayment,paymentPattern);
        map.put(txtDescription,descriptionPattern);


    }

    private void loadAllMusic() throws SQLException, ClassNotFoundException {

        ResultSet result = CrudUtil.execute("SELECT * FROM music");
        ObservableList<MusicTM> obList = FXCollections.observableArrayList();


        while (result.next()) {
            Button btn = new Button("Delete");
            MusicTM tm = new MusicTM(
                    result.getString("MuId"),
                    result.getString("Name"),
                    result.getString("Description"),
                    result.getDouble("Payment"),
                    btn
            );

            btn.setOnAction(e -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure?", ButtonType.YES, ButtonType.NO);
                Optional<ButtonType> buttonType = alert.showAndWait();

                if (buttonType.get().equals(ButtonType.YES)) {

                    MusicTM mtm =tblMusic.getSelectionModel().getSelectedItem();

                    try {

                        if (CrudUtil.execute("DELETE FROM music WHERE MuId=?",mtm.getMuId())) {

                            Notifications notifications = Notifications.create().title("Delete Completed !").text("Music Deleted successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.show();

                        } else {

                            Notifications notifications = Notifications.create().title("Delete Uncompleted !").text("Try Again").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
                            notifications.darkStyle();
                            notifications.showError();

                        }

                    } catch (SQLException | ClassNotFoundException exception) {

                    }

                    obList.remove(mtm);

                }
            });

            obList.add(tm);
            tblMusic.setItems(obList);
        }
    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(musicContext,"MainForm");

    }

    public void btnAddMusicOnAction(ActionEvent actionEvent) {


        Music m = new Music(
                txtID.getText(),txtName.getText(),txtDescription.getText(),
                Double.parseDouble(txtPayment.getText())
        );

        if (btnAddMusic.getText().equals("Add Music")) {
            try {
                if (CrudUtil.execute("INSERT INTO music VALUES (?,?,?,?)",m.getMuId(),m.getName(),m.getDescription(),m.getPayment())) {

                    Notifications notifications = Notifications.create().title("Save Completed !").text("Music Saved successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            Music m1 = new Music(
                    txtID.getText(),txtName.getText(),txtDescription.getText(),
                    Double.parseDouble(txtPayment.getText())
            );

            try{
                boolean isUpdated = CrudUtil.execute("UPDATE music SET Name=? , Description=? , Payment=? WHERE MuId=?",m1.getName(),m1.getDescription(),m1.getPayment(),m1.getMuId());
                if (isUpdated){
                    Notifications notifications = Notifications.create().title("Update Completed !").text("Music Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllMusic();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void btnNewMusicOnAction(ActionEvent actionEvent) {
        txtID.setEditable(true);
        btnAddMusic.setText("Add Music");
        txtID.setText("");
        txtName.setText("");
        txtPayment.setText("");
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
        btnAddMusic.setText("Update Music");
        ResultSet result = CrudUtil.execute("SELECT * FROM music WHERE MuId=?",txtID.getText());
        if (result.next()) {
            txtName.setText(result.getString(2));
            txtDescription.setText((result.getString(3)));
            txtPayment.setText(String.valueOf((result.getDouble(4))));

            txtID.setEditable(false);
        } else {
            btnAddMusic.setText("Add Music");
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
        ValidationUtil.validate(map,btnAddMusic);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnAddMusic);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
