package gui.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import datamining.DataSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import javax.swing.event.ChangeEvent;
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
    private JFXComboBox cbDatasetImport;

    @FXML
    private JFXComboBox cbBoxTabAttribute;

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
    private JFXComboBox cbHistogramTabAttribute;

    @FXML
    private JFXComboBox cbScatterTabAttribute1;

    @FXML
    private JFXComboBox cbScatterTabAttribute2;

    @FXML
    private JFXButton btnScatterTabPlot;

    @FXML
    private AnchorPane apBoxTab;

    @FXML
    private AnchorPane apScatterTab;

    @FXML
    private AnchorPane apHistoTab;

    @FXML
    private Tab tabDataset;

    @FXML
    private Tab tabBoxPlot;

    @FXML
    private Tab tabHisto;

    @FXML
    private Tab tabScatter;

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
        this.enableTabs();
        ObservableList<String> attrs = this.getAttributes();
        cbDatasetTabAttribute.setItems(attrs);
        cbBoxTabAttribute.setItems(attrs);
        cbHistogramTabAttribute.setItems(attrs);
        cbScatterTabAttribute1.setItems(attrs);
        cbScatterTabAttribute2.setItems(attrs);

    }

    public ObservableList<String>  getAttributes() {
        ArrayList<String> names = dataset.getVariablesNames();
        ObservableList<String> choices = FXCollections.observableArrayList(names);

        return choices;
    }

    public void refreshValues() {
        String cbString = (String)cbDatasetTabAttribute.getValue();
        ArrayList<String> columns = dataset.getVariablesNames();

        if(columns.contains(cbString)) {
            lblDatasetType.setText("FLOAT");
            lblDatasetMean.setText(dataset.getMean(cbString).toString());
            lblDatasetMedian.setText(dataset.getMedian(cbString).toString());
            lblDatasetMode.setText(dataset.getMode(cbString).toString());
            lblDatasetSymmetry.setText(dataset.isSymmetrical(cbString).toString() ? "YES" : "NO");
        }
        else{
            this.emptyLabels();
        }
    }

    public void emptyLabels() {
        lblDatasetType.setText("");
        lblDatasetMean.setText("");
        lblDatasetMedian.setText("");
        lblDatasetMode.setText("");
        lblDatasetSymmetry.setText("");
    }

    public void fillDatasetTable() {
        // TODO: fill the table after importing the dataset
    }

    public void assignEventListeners() {
        cbDatasetTabAttribute.setOnAction(this::refreshEvent);
    }

    private void refreshEvent(Event event) {
        this.refreshValues();
    }

    public void enableTabs() {
        tabBoxPlot.setDisable(false);
        tabHisto.setDisable(false);
        tabScatter.setDisable(false);
    }
}
