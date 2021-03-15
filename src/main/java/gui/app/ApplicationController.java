package gui.app;

import charts.Charts;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import datamining.DataSet;
import datamining.Instance;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ApplicationController {

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

    @FXML private TableView<DatasetTableItem> tableDataset;
    @FXML private TableColumn<DatasetTableItem, SimpleStringProperty> numberColumn;
    @FXML private TableColumn<DatasetTableItem, SimpleStringProperty> classsColumn;
    @FXML private TableColumn<DatasetTableItem, SimpleStringProperty> t3ResinColumn;
    @FXML private TableColumn<DatasetTableItem, SimpleStringProperty> totalThyroxinColumn;
    @FXML private TableColumn<DatasetTableItem, SimpleStringProperty> totalTriioColumn;
    @FXML private TableColumn<DatasetTableItem, SimpleStringProperty> tshColumn;
    @FXML private TableColumn<DatasetTableItem, SimpleStringProperty> maxDiffTshColumn;
    // Observable list to show all rules in tableview
    ObservableList<DatasetTableItem> datas = FXCollections.observableArrayList();

    public FileChooser fileChooserObject = new FileChooser();
    public DataSet dataset;

    public ApplicationController() {

    }

    public void initialize() throws IOException {
        fileChooserObject.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.txt"));

        instanceColumn.setCellValueFactory(new PropertyValueFactory<>("instanceColumn"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("valueColumn"));

        numberColumn.setCellValueFactory(new PropertyValueFactory<>("numberColumn"));
        classsColumn.setCellValueFactory(new PropertyValueFactory<>("classsColumn"));
        t3ResinColumn.setCellValueFactory(new PropertyValueFactory<>("t3ResinColumn"));
        totalThyroxinColumn.setCellValueFactory(new PropertyValueFactory<>("totalThyroxinColumn"));
        totalTriioColumn.setCellValueFactory(new PropertyValueFactory<>("totalTriioColumn"));
        tshColumn.setCellValueFactory(new PropertyValueFactory<>("tshColumn"));
        maxDiffTshColumn.setCellValueFactory(new PropertyValueFactory<>("maxDiffTshColumn"));

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
        }
        this.assignEventListeners();
    }

    private void enableComboBoxes() {
        cbDatasetTabAttribute.setDisable(false);
    }

    public ObservableList<String>  getAttributes() {
        ArrayList<String> names = dataset.getStaticNames();
        names.remove(0);
        ObservableList<String> choices = FXCollections.observableArrayList(names);

        return choices;
    }

    public void refreshDatasetValues() {
        String cbString = (String)cbDatasetTabAttribute.getValue();
        ArrayList<String> columns = dataset.getStaticNames();

        if(columns.contains(cbString)) {
            lblDatasetType.setText("FLOAT");
            String s = dataset.getMean(cbString).toString();
            String[] l = s.split("\\.");
            l[1] = l[1].substring(0, 2);
            s = "" + l[0] + "." + l[1];
            lblDatasetMean.setText(String.valueOf(s));
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
        for (Instance i : dataset.getInstances()) {
            datas.add(new DatasetTableItem(
                    new SimpleStringProperty(String.valueOf(i.getVariables().get(0).getInteger())),
                    new SimpleStringProperty(String.valueOf(i.getVariables().get(1).getDouble())),
                    new SimpleStringProperty(String.valueOf(i.getVariables().get(2).getDouble())),
                    new SimpleStringProperty(String.valueOf(i.getVariables().get(3).getDouble())),
                    new SimpleStringProperty(String.valueOf(i.getVariables().get(4).getDouble())),
                    new SimpleStringProperty(String.valueOf(i.getVariables().get(5).getDouble())),
                    new SimpleStringProperty(String.valueOf(i.getVariables().get(6).getDouble()))
            ));
            tableDataset.setItems(datas);
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
            outliers.add(new OutliersTableItem(
                    new SimpleStringProperty(String.valueOf(i)),
                    new SimpleStringProperty(String.valueOf(outlier)))
            );
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


