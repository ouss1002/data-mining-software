package gui.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import datamining.DataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;


import java.io.IOException;
import java.util.ArrayList;

public class ApplicationController {

    @FXML
    private TableView<?> tableDataset;

    @FXML
    private TableView<?> tableBoxTab;

    @FXML
    private JFXComboBox cbDatasetTabAttribute;

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
    public DataSet dataset;

    public ApplicationController() {

    }

    public void initialize() throws IOException {
        fileChooserObject.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.txt"));

        prepare();
    }

    public void prepare() throws IOException {

        tableDataset.getItems().clear();
        dataset = new DataSet("ADD FILE PATH HERE");
        cbDatasetTabAttribute.setItems(this.getAttributes());

    }

    public ObservableList<String>  getAttributes() {
        ArrayList<String> names = dataset.getColumnNames();
        ObservableList<String> choices = FXCollections.observableArrayList(names);

        return choices;
    }

    public void fillDatasetTable() {
        // TODO: fill the table after importing the dataset
    }


}
