package gui.app;

import charts.Charts;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import datamining.DataSet;
import datamining.Instance;
import datamining.Variable;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XYChart;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ApplicationController {

    @FXML
    private TableView<ObservableList<String>> tableDataset;

    //@FXML
    //private TableView tableBoxTab;

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

    @FXML private TableView<OutliersTableItem> tableBoxTab;
    @FXML private TableColumn<OutliersTableItem, SimpleStringProperty> instanceColumn;
    @FXML private TableColumn<OutliersTableItem, SimpleStringProperty> valueColumn;
    // Observable list to show all rules in tableview
    ObservableList<OutliersTableItem> outliers = FXCollections.observableArrayList();

    public FileChooser fileChooserObject = new FileChooser();
    public DataSet dataset;

    public ApplicationController() {

    }

    public void initialize() throws IOException {
        fileChooserObject.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.txt"));

        instanceColumn.setCellValueFactory(new PropertyValueFactory<>("instanceColumn"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valueColumn"));


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
            this.fillDatasetTable();
            System.out.println("ok");
        }
        this.assignEventListeners();
    }

    private void enableComboBoxes() {
        cbDatasetTabAttribute.setDisable(false);
    }

    public ObservableList<String>  getAttributes() {
        ArrayList<String> names = dataset.getStaticNames();
        ObservableList<String> choices = FXCollections.observableArrayList(names);

        return choices;
    }

    public void refreshDatasetValues() {
        String cbString = (String)cbDatasetTabAttribute.getValue();
        ArrayList<String> columns = dataset.getStaticNames();

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
        TableColumn numberColumn = new TableColumn("Number");
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));

        TableColumn classsColumn = new TableColumn("Classs");
        classsColumn.setCellValueFactory(new PropertyValueFactory<>("classs"));

        TableColumn t3ResinColumn = new TableColumn("T3Resin");
        t3ResinColumn.setCellValueFactory(new PropertyValueFactory<>("t3Resin"));

        TableColumn totalThyroxin = new TableColumn("TotalThyroxin");
        totalThyroxin.setCellValueFactory(new PropertyValueFactory<>("totalThyroxin"));

        TableColumn totalTriio = new TableColumn("TotalTriio");
        totalTriio.setCellValueFactory(new PropertyValueFactory<>("totalTriio"));

        TableColumn tsh = new TableColumn("Tsh");
        tsh.setCellValueFactory(new PropertyValueFactory<>("tsh"));

        TableColumn maxDiffTsh = new TableColumn("MaxDiffTsh");
        maxDiffTsh.setCellValueFactory(new PropertyValueFactory<>("maxDiffTsh"));

        this.tableDataset.getColumns().addAll(numberColumn, classsColumn, t3ResinColumn, totalThyroxin, totalTriio, tsh, maxDiffTsh);

        for(Instance i : this.dataset.getInstances()) {
            tableDataset.getItems().add((ObservableList<String>) i);
        }
    }

    public void assignEventListeners() {
        cbDatasetTabAttribute.setOnAction(this::refreshDatasetEvent);
        cbBoxTabAttribute.setOnAction(event1 -> {
            try {
                refreshBoxTabEvent(event1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        cbHistogramTabAttribute.setOnAction(event -> {
            try {
                refreshHistoEvent(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        btnScatterTabPlot.setOnAction(actionEvent1 -> {
            try {
                plotScatter(actionEvent1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
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

    private void plotScatter(ActionEvent actionEvent) throws IOException {
        String attr1 = cbScatterTabAttribute1.getValue();
        String attr2 = cbScatterTabAttribute2.getValue();

        if(dataset.getStaticNames().contains(attr1) && dataset.getStaticNames().contains(attr2)) {
            XYChart chart = Charts.ScatterPlot(dataset, attr1, attr2);
            Charts.showChart(apScatterTab, chart);
        }
    }

    private void refreshHistoEvent(Event event) throws IOException {
        this.refreshHistoValues();
    }

    private void refreshHistoValues() throws IOException {
        String attr = cbHistogramTabAttribute.getValue().toString();

        if(dataset.getStaticNames().contains(attr)) {
            CategoryChart chart = Charts.Histogram(dataset, attr);
            Charts.showChart(apHistoTab, chart);
        }
    }

    private void refreshBoxTabEvent(Event event) throws IOException {
        cbBoxTabAttribute.setPromptText(cbBoxTabAttribute.getValue().toString());
        this.refreshBoxTabValues();
        this.refreshBoxTabTable(cbBoxTabAttribute.getValue().toString());
    }

    private void refreshBoxTabTable(String name) {
        ArrayList<Double> outliersTMP = dataset.getOutliers(name);
        Integer i = 1;
        for (Double outlier: outliersTMP) {
            outliers.add(new OutliersTableItem(new SimpleStringProperty(String.valueOf(i)), new SimpleStringProperty(String.valueOf(outlier))));
            tableBoxTab.setItems(outliers);
            i++;
        }

    }

    private void refreshBoxTabValues() throws IOException {
        String cbString = (String)cbBoxTabAttribute.getValue();
        ArrayList<String> columns = dataset.getStaticNames();

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

    private void fillBoxTabTable() throws IOException {
        String attr = cbBoxTabAttribute.getValue().toString();

        if(dataset.getStaticNames().contains(attr)) {
            BoxChart chart = Charts.BoxPlot(dataset, attr);
            Charts.showChart(apBoxTab, chart);
        }
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


