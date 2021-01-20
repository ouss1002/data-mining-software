package gui.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import datamining.DataSet;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.IOException;

public class ApplicationController {

    @FXML
    private TableView<?> tableDataset;

    @FXML
    private TableView<?> tableBoxTab;

    @FXML
    private JFXComboBox<?> cbDatasetTabAttribute;

    @FXML
    private Label lblDatasetType;

    @FXML
    private Label lblDatasetMean;

    @FXML
    private Label lblDatasetMedian;

    @FXML
    private Label lblDatasetMode;

    @FXML
    private Label lblDatasetSymmetry;

    @FXML
    private JFXButton btnDatasetImport;

    @FXML
    private JFXComboBox<?> cbDatasetImport;

    @FXML
    private JFXComboBox<?> cbBoxTabAttribute;

    @FXML
    private Label lblBoxTabMin;

    @FXML
    private Label lblBoxTabQ1;

    @FXML
    private Label lblBoxTabMedian;

    @FXML
    private Label lblBoxTabQ3;

    @FXML
    private Label lblBoxTabMax;

    @FXML
    private JFXTreeTableView<?> tableBoxTab;

    @FXML
    private JFXComboBox<?> cbHistogramTabAttribute;

    @FXML
    private JFXComboBox<?> cbScatterTabAttribute1;

    @FXML
    private JFXComboBox<?> cbScatterTabAttribute2;

    @FXML
    private JFXButton btnScatterTabPlot;

    @FXML
    private AnchorPane apBoxTab;

    @FXML
    private AnchorPane apScatterTab;

    @FXML
    private AnchorPane apHistoTab;

    public FileChooser fileChooserObject = new FileChooser();
    DataSet dataset;

    public ApplicationController() {

    }

    public void initialize() {
        fileChooserObject.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.txt"));

        btnDatasetImport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent2) {
                File selectedFile = fileChooserObject.showOpenDialog(null);

                try {
                     dataset = new DataSet(selectedFile.getAbsolutePath());
                     //System.out.println("perfect");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
