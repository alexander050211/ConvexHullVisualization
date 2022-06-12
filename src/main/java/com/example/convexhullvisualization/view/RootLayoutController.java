package com.example.convexhullvisualization.view;

import com.example.convexhullvisualization.MainApp;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;

public class RootLayoutController {

    private MainApp mainApp;

    @FXML public Canvas canvas;

    @FXML public Button button;

    @FXML
    private void handleButton() {
        mainApp.handleButton();
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp=mainApp;
    }
}