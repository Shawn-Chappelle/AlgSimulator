package edu.redwoods.cis12;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class LinearSearchSimulationController {
    private LinearSearchSimulation lss;

    @FXML
    private GridPane controlsGridPaneLinear;

    @FXML
    private TextField arraySizeTextFieldLinear;

    @FXML
    private ColorPicker colorColorPickerLinear;

    @FXML
    private Button createBoardButtonLinear;

    @FXML
    private Button simulateButtonLinear;

    @FXML
    private TextField searchForTextFieldLinear;

    public void initialize() { }

    @FXML
    void createBoardButtonPressed(ActionEvent event) {
        try {
            this.lss.createBoard(
                    Integer.parseInt(
                            this.arraySizeTextFieldLinear.getText()), this.colorColorPickerLinear.getValue());
        } catch (NumberFormatException nfe) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Invalid Input");
            a.setContentText("Please set an \"Array Size\"");
            a.show();
        }
    }

    @FXML
    private void simulateButtonPressed(ActionEvent ignoredEvent) {
        this.lss.simulate();
    }

    public void setLss(LinearSearchSimulation lss) {
        this.lss = lss;
    }

    public TextField getSearchForTextField() {
        return searchForTextFieldLinear;
    }
}
