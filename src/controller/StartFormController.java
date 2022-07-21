package controller;

import javafx.event.ActionEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class StartFormController implements Navigation{
    public AnchorPane startContext;

    public void openLoginOnAction(ActionEvent actionEvent) throws IOException {
        setUI(startContext,"LogInForm");
    }
}
