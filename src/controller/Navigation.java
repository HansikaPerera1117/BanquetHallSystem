package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public interface Navigation {

    default void setPage(String location) throws IOException {
        URL resource = getClass().getResource("../view/"+location+".fxml");
        Parent load = FXMLLoader.load(resource);
        Scene scene=new Scene(load);
        Stage stage=new Stage();
        stage.setScene(scene);
        stage.setTitle(location);
        stage.show();
    }

    default void setUI(AnchorPane ap, String location) throws IOException {
        Stage stage = (Stage) ap.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/"+location+".fxml"))));
        stage.setTitle(location);
        stage.centerOnScreen();
    }

}
