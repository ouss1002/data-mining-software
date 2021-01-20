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

import java.io.File;
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
    private JFXComboBox<String> cbHistogramTabAttribute;

    @FXML
    private JFXComboBox<String> cbScatterTabAttribute1;

    @FXML
    private JFXComboBox<String> cbScatterTabAttribute2;

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
        tableBoxTab.getItems().clear();
        if(dataset != null) {
            this.enableTabs();
            this.enableComboBoxes();
            ObservableList<String> attrs = this.getAttributes();
            cbDatasetTabAttribute.setItems(attrs);
            cbBoxTabAttribute.setItems(attrs);
            cbHistogramTabAttribute.setItems(attrs);
            cbScatterTabAttribute1.setItems(attrs);
            cbScatterTabAttribute2.setItems(attrs);
        }
        this.assignEventListeners();
    }

    private void enableComboBoxes() {
        cbDatasetTabAttribute.setDisable(false);
    }

    public ObservableList<String>  getAttributes() {
        ArrayList<String> names = dataset.getVariablesNames();
        ObservableList<String> choices = FXCollections.observableArrayList(names);

        return choices;
    }

    public void refreshDatasetValues() {
        String cbString = (String)cbDatasetTabAttribute.getValue();
        ArrayList<String> columns = dataset.getVariablesNames();

        if(columns.contains(cbString)) {
            lblDatasetType.setText("FLOAT");
            lblDatasetMean.setText(dataset.getMean(cbString).toString());
            lblDatasetMedian.setText(dataset.getMedian(cbString).toString());
            lblDatasetMode.setText(dataset.getModeString(cbString));
            lblDatasetSymmetry.setText(dataset.isSymmetrical(cbString) ? "YES" : "NO");
        }
        else{
            this.emptyDatasetLabels();
        }
    }

    public void emptyDatasetLabels() {
        lblDatasetType.setText("");
        lblDatasetMean.setText("");
        lblDatasetMedian.setText("");
        lblDatasetMode.setText("");
        lblDatasetSymmetry.setText("");
    }

    public void emptyBoxPlotLabels() {
        lblBoxTabMin.setText("");
        lblBoxTabQ1.setText("");
        lblBoxTabMedian.setText("");
        lblBoxTabQ3.setText("");
        lblBoxTabMax.setText("");
    }

    public void fillDatasetTable() {
        // TODO: fill the table after importing the dataset
    }

    public void assignEventListeners() {
        cbDatasetTabAttribute.setOnAction(this::refreshDatasetEvent);
        cbBoxTabAttribute.setOnAction(this::refreshBoxTabEvent);
        cbHistogramTabAttribute.setOnAction(this::refreshHistoEvent);
        btnScatterTabPlot.setOnAction(this::plotScatter);
        btnDatasetImport.setOnAction(actionEvent -> {
            try {
                importDatasetEvent(actionEvent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void importDatasetEvent(ActionEvent actionEvent) throws IOException {
        fileChooserObject.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.txt"));
        File selectedFile = fileChooserObject.showOpenDialog(null);
        if(selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            dataset = new DataSet(path);
            prepare();
        }
    }

    private void plotScatter(ActionEvent actionEvent) {
        // TODO: plot the scatter graph
    }

    private void refreshHistoEvent(Event event) {
        this.refreshHistoValues();
    }

    private void refreshHistoValues() {
        // TODO: prepare the histogram
    }

    private void refreshBoxTabEvent(Event event) {
        cbBoxTabAttribute.setPromptText(cbBoxTabAttribute.getValue().toString());
        this.refreshBoxTabValues();
    }

    private void refreshBoxTabValues() {
        String cbString = (String)cbBoxTabAttribute.getValue();
        ArrayList<String> columns = dataset.getVariablesNames();

        if(columns.contains(cbString)) {
            lblBoxTabMin.setText(dataset.getMin(cbString).toString());
            lblBoxTabQ1.setText(dataset.getQ1(cbString).toString());
            lblBoxTabMedian.setText(dataset.getMedian(cbString).toString());
            lblBoxTabQ3.setText(dataset.getQ3(cbString).toString());
            lblBoxTabMax.setText(dataset.getMax(cbString).toString());
        }
        else{
            this.emptyBoxPlotLabels();
        }

        tableBoxTab.getItems().clear();
        this.fillBoxTabTable();
    }

    private void fillBoxTabTable() {
        // TODO: fill the BoxTab table with outliers
    }

    private void refreshDatasetEvent(Event event) {
        cbDatasetTabAttribute.setPromptText(cbDatasetTabAttribute.getValue().toString());

        this.refreshDatasetValues();
    }

    public void enableTabs() {
        tabBoxPlot.setDisable(false);
        tabHisto.setDisable(false);
        tabScatter.setDisable(false);
    }
}
