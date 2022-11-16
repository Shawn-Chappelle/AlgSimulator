package edu.redwoods.cis12;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.io.IOException;
import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class LinearSearchSimulation extends AlgorithmSimulation {
    private LinearSearchSimulationController lssController;

    private GridPane gridPaneLinear;
    public LinearSearchSimulation(AlgSimulatorController asc) {
        super("Linear Search", asc);
    }

    @Override
    public void loadControls() {
        try {
            //controlsGridPane = FXMLLoader.load(getClass().getResource("linearSearchControls.fxml"));
            FXMLLoader loader = new FXMLLoader();
            Pane controlsGridPaneLinear = loader.load(getClass().getResourceAsStream("linearSearchControls.fxml"));
            this.lssController = loader.getController();
            this.lssController.setLss(this);
            this.getAsc().setControls(controlsGridPaneLinear);
        } catch (IOException | NullPointerException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("File Not Found");
            a.setContentText(e.getMessage());
            a.show();
        }
    }

    private void algorithmStep(int searchFor, int start, int end) {
        try {
            if(end < start) {
                return;
            }
            int middle = (start + end)/2;  // Integer division is OK

            Button b = (Button)gridPaneLinear.getChildren().get(middle);
            b.setStyle("-fx-background-color: #00AA00");
            TimeUnit.SECONDS.sleep(1);
            int value = Integer.parseInt(b.getText());

            if(value == searchFor) {
                b.setStyle("-fx-background-color: #FF0000");
                return;
            }
            if(value < searchFor) {
                // Make all squares from search and below disabled.
                for(int i=start; i<=middle; i++) {
                    b = (Button)gridPaneLinear.getChildren().get(i);
                    b.setStyle("-fx-background-color: #666666");
                }
                start = middle + 1;
            } else {
                // Make all squares from search and above disabled.
                for(int i=middle; i<=end; i++) {
                    b = (Button)gridPaneLinear.getChildren().get(i);
                    b.setStyle("-fx-background-color: #666666");
                }
                end = middle - 1;
            }
            TimeUnit.SECONDS.sleep(3);
            algorithmStep(searchFor, start, end);
        } catch(NumberFormatException e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Invalid Search");
            a.setContentText("Please enter an integer to search for in the \"Search For\" text-field.");
            a.show();
        } catch(InterruptedException ie) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Interrupted");
            a.setContentText("Please enter an integer to search for in the \"Search For\" text-field.");
            a.show();
        }
    }

    private void sortGrid() {
        // Generates a sorted list, sorting on button text value, but is not a deep copy
        ObservableList<Node> sortedList = gridPaneLinear.getChildren().sorted(
                Comparator.comparingInt(o -> Integer.parseInt(((Button) o).getText())));
        // Create a new list to make a deep copy so clear doesn't garbage collect GridPane Nodes
        ObservableList<Node> deepCopy = FXCollections.observableArrayList();
        deepCopy.addAll(sortedList);

        // Clear previous list of Nodes, so we don't get duplicate add errors.
        gridPaneLinear.getChildren().clear();

        // Apparently I have to loop over gridPane and re-Add the sorted Nodes instead of using getChildren().addAll()
        // to get the nodes to show up in the right spots. I'm probably missing something here.
        int size = deepCopy.size();
        int columns = (int)Math.sqrt(size);
        for(int i=0, c=0, r=0; i<size; i++, c = (c+1) % columns, r = (i-c)/columns) {
            gridPaneLinear.add(deepCopy.get(i), c, r);
        }
    }
    @Override
    public void simulate() {
        try {
            sortGrid();

            int searchFor = Integer.parseInt(lssController.getSearchForTextField().getText());
            new Thread(() -> {
                algorithmStep(searchFor, 0, gridPaneLinear.getChildren().size() - 1); // -1 because size is > last index
            }).start();
        } catch(NumberFormatException|NullPointerException n) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("Invalid Search");
            a.setContentText("Please enter an integer to search for in the \"Search For\" text-field.");
            a.show();
        }

    }
    public void createBoard(int size, Color color) {
        //Creating a Grid Pane
        gridPaneLinear = new GridPane();
        //Setting the padding
        gridPaneLinear.setPadding(new Insets(10, 10, 10, 10));
        //Setting the vertical and horizontal gaps between the columns
        gridPaneLinear.setVgap(5);
        gridPaneLinear.setHgap(5);
        gridPaneLinear.setMaxWidth(Double.MAX_VALUE);
        gridPaneLinear.setMaxHeight(Double.MAX_VALUE);
        //Setting the Grid alignment
        gridPaneLinear.setAlignment(Pos.CENTER);
        //Arranging all the nodes in the grid
        int columns = (int)Math.sqrt(size);

        Random ran = new Random();

        for(int i=0, c=0, r=0; i<size; i++, c = (c+1) % columns, r = (i-c)/columns) {
            //Button b = new Button(String.valueOf(i));
            Button b = new Button(
                    String.valueOf(ran.nextInt(100) + 1));
            b.setMaxWidth(Double.MAX_VALUE);
            b.setMaxHeight(Double.MAX_VALUE);
            Font font = Font.font("Courier New", FontWeight.BOLD, 36);
            b.setFont(font);
            b.setStyle(String.format("-fx-background-color: %s",
                    String.format( "#%02X%02X%02X",
                            (int)( color.getRed() * 255 ),
                            (int)( color.getGreen() * 255 ),
                            (int)( color.getBlue() * 255 ) )));
            gridPaneLinear.add(b, c, r);
        }
        this.getAsc().setSimulationArea(gridPaneLinear);
    }
}

