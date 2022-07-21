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
import model.MenuSelection;
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

public class MenuSelectionFormController implements Navigation{
    public AnchorPane menuSelectionContext;
    public Label lblDate;
    public Label lblTime;
    public TableView<MenuSelection> tblMenuSelection;
    public TableColumn colMenuID;
    public TableColumn colMenuChoiceNum;
    public TableColumn colOptions;
    public Label lblMenuChoiceName;
    public TextField txtOptions;
    public TextField txtNum;
    public TableColumn colNum;
    public Label lblMenuID;
    public Label lblMenuChoiceNum;
    public JFXButton btnUpdateMenuOptions;
    LinkedHashMap<TextField, Pattern> map = new LinkedHashMap<>();


    public void initialize(){
        loadDateAndTime();


        colNum.setCellValueFactory(new PropertyValueFactory("Num"));
        colMenuID.setCellValueFactory(new PropertyValueFactory("MenuId"));
        colMenuChoiceNum.setCellValueFactory(new PropertyValueFactory("MenuTypeNum"));
        colOptions.setCellValueFactory(new PropertyValueFactory("Options"));


        try {
            loadAllMenuSelections();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        Pattern NumPattern = Pattern.compile("^[1-9][0-9]{0,1}$");
        Pattern OptionPattern = Pattern.compile("^[A-z ,&]{5,300}$");

        map.put(txtNum,NumPattern);
        map.put(txtOptions,OptionPattern);

    }

    private void loadAllMenuSelections() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM menuSelection");
        ObservableList<MenuSelection> obList = FXCollections.observableArrayList();

        while (result.next()){
            obList.add(
                    new MenuSelection(
                            result.getInt("Num"),
                            result.getString("MenuId"),
                            result.getInt("MenuTypeNum"),
                            result.getString("Options")
                    )
            );
        }
        tblMenuSelection.setItems(obList);
    }

    public void backToMainOnAction(ActionEvent actionEvent) throws IOException {
        setUI(menuSelectionContext,"MainForm");

    }

    public void btnUpdateOptionsOnAction(ActionEvent actionEvent) {
        MenuSelection ms = new MenuSelection(
             Integer.parseInt(txtNum.getText()),
              lblMenuID.getText(),
                Integer.parseInt(lblMenuChoiceNum.getText()),
                txtOptions.getText()
        );
        try{
            boolean isUpdated = CrudUtil.execute("UPDATE menuSelection SET Options=?  WHERE Num=?",ms.getOptions(),ms.getNum());
            if (isUpdated){
                Notifications notifications = Notifications.create().title("Update Completed !").text("Options Updated successfully...").hideAfter(Duration.seconds(5)).position(Pos.BOTTOM_RIGHT);
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
            loadAllMenuSelections();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void btnSearchOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        try {
            search();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void txtSearchOnAction(ActionEvent actionEvent) {
        try {
            search();
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void search() throws SQLException, ClassNotFoundException {
        ResultSet result = CrudUtil.execute("SELECT * FROM menuSelection WHERE Num=?",txtNum.getText());
        if (result.next() ) {
            lblMenuID.setText(result.getString(2));
            lblMenuChoiceNum.setText(result.getString(3));
            txtOptions.setText(result.getString(4));

            ResultSet resultSet = CrudUtil.execute("SELECT * FROM menuType WHERE Num=?",lblMenuChoiceNum.getText());
            if (resultSet.next()) {
                lblMenuChoiceName.setText(resultSet.getString(2));
            }

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

    public void textFields_Key_Released(KeyEvent keyEvent) {
        ValidationUtil.validate(map,btnUpdateMenuOptions);

        if (keyEvent.getCode() == KeyCode.ENTER) {
            Object response =  ValidationUtil.validate(map,btnUpdateMenuOptions);;

            if (response instanceof TextField) {
                TextField textField = (TextField) response;
                textField.requestFocus();
            }
        }
    }
}
