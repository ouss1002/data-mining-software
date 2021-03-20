package gui.app;

import algorithms.Apriori.*;
import algorithms.Clarans;
import algorithms.Discretization;
import algorithms.KMeans;
import algorithms.KMedoids;
import charts.Charts;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTextArea;
import datamining.DataSet;
import datamining.Instance;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.XYChart;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ApplicationController {

    @FXML
    private Tab tabDataset;

    @FXML
    private JFXComboBox<String> cbDatasetTabAttribute;

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
    private JFXComboBox<String> cbDatasetImport;

    @FXML
    private Tab tabBoxPlot;

    @FXML
    private AnchorPane apBoxTab;

    @FXML
    private JFXComboBox<String> cbBoxTabAttribute;

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
    private TableView<OutliersTableItem> tableBoxTab;

    @FXML
    private TableColumn<?, ?> instanceColumn;

    @FXML
    private TableColumn<?, ?> valueColumn;

    @FXML
    private Tab tabHisto;

    @FXML
    private AnchorPane apHistoTab;

    @FXML
    private JFXComboBox<String> cbHistogramTabAttribute;

    @FXML
    private Tab tabScatter;

    @FXML
    private AnchorPane apScatterTab;

    @FXML
    private JFXComboBox<String> cbScatterTabAttribute1;

    @FXML
    private JFXComboBox<String> cbScatterTabAttribute2;

    @FXML
    private JFXButton btnScatterTabPlot;

    @FXML
    private Tab tabApriori;

    @FXML
    private AnchorPane apScatterTab1;

    @FXML
    private JFXComboBox<String> cbScatterTabAttribute11;

    @FXML
    private JFXComboBox<String> cbScatterTabAttribute21;

    @FXML
    private JFXButton btnScatterTabPlot1;

    @FXML
    private Tab tabKMeans;

    @FXML
    private AnchorPane apScatterTab2;

    @FXML
    private Label lblKMeansExecTime;

    @FXML
    private Label lblKMeansEstimation;

    @FXML
    private Label lblKMeansFMeasure;

    @FXML
    private JFXTextArea textAreaKMeans;

    @FXML
    private VBox vbKMeans;

    @FXML
    private JFXSlider sliderKMeansNumClusters;

    @FXML
    private JFXButton btnKMeansBuildClusters;

    @FXML
    private Tab tabKMedoids;

    @FXML
    private AnchorPane apScatterTab21;

    @FXML
    private Label lblKMedoidsExecTime;

    @FXML
    private Label lblKMedoidsEstimation;

    @FXML
    private Label lblKMedoidsFMeasure;

    @FXML
    private JFXTextArea textAreaKMedoids;

    @FXML
    private VBox vbKMedoids;

    @FXML
    private JFXSlider sliderKMedoidsNumClusters;

    @FXML
    private JFXButton btnKMedoidsBuildClusters;

    @FXML
    private Tab tabClarans;

    @FXML
    private AnchorPane apScatterTab211;

    @FXML
    private Label lblClaransExecTime;

    @FXML
    private Label lblClaransEstimation;

    @FXML
    private Label lblClaransFMeasure;

    @FXML
    private JFXTextArea textAreaClarans;

    @FXML
    private VBox vbClarans;

    @FXML
    private JFXSlider sliderClaransNumClusters;

    @FXML
    private JFXSlider sliderClaransNumSearches;

    @FXML
    private JFXSlider sliderClaransNumTries;

    @FXML
    private JFXButton btnClaransBuildClusters;

    @FXML
    private Label lblAprioriDiscretization;

    @FXML
    private Label lblAprioriFreqPatterns;

    @FXML
    private Label lblAprioriAssocRules;

    @FXML
    private Label lblAprioriFullTime;

    @FXML
    private JFXTextArea textAreaAprioriFreqSets;

    @FXML
    private JFXTextArea textAreaAssocRules;

    @FXML
    private JFXSlider sliderAprioriNumBins;

    @FXML
    private JFXButton btnClaransBuildClusters11;

    @FXML
    private JFXSlider sliderAprioriMinSupport;

    @FXML
    private JFXButton btnAprioriFreqPatterns;

    @FXML
    private JFXButton btnAprioriAssocRules;

    @FXML
    private JFXSlider sliderAprioriMinConfidence;

    @FXML
    private JFXButton btnAprioriDiscretize;

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
    public DataSet normalizedDataset;
    public ArrayList<Rule> rules;

    long start, finish;

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
        btnKMeansBuildClusters.setOnAction(actionEvent -> {
            startKMeans();
        });
        btnKMedoidsBuildClusters.setOnAction(actionEvent -> {
            startKMedoids();
        });
        btnClaransBuildClusters.setOnAction(actionEvent -> {
            startClarans();
        });
        btnAprioriDiscretize.setOnAction(actionEvent -> {
            startDiscretize();
            btnAprioriFreqPatterns.setDisable(false);
            sliderAprioriMinSupport.setDisable(false);
        });
        btnAprioriFreqPatterns.setOnAction(actionEvent -> {
            startFreqPatterns();
            btnAprioriAssocRules.setDisable(false);
            sliderAprioriMinConfidence.setDisable(false);
        });
        btnAprioriAssocRules.setOnAction(actionEvent -> {
            startAssocRules();
        });
    }

    private void startAssocRules() {
        start = System.nanoTime();
        //Apriori.generateTransactionDB(Apriori.discretizedInstances);
        int value = (int) sliderAprioriMinConfidence.getValue();
        AssociationRules.generateRules(Apriori.frequentItemSetsList, value);
        Apriori.rules = AssociationRules.getRules();
        finish = System.nanoTime();
        Apriori.timeFreqPat = (finish - start) / 1000000;
        lblAprioriAssocRules.setText(Apriori.timeFreqPat + "ms");

        lblAprioriFullTime.setText((Apriori.timeDiscretization + Apriori.timeFreqPat + Apriori.timeAssocRules) + "ms");

        fillAssocTable();
    }

    private void fillAssocTable() {
        StringBuilder printed = new StringBuilder();
        for (Rule rule: Apriori.rules) {
            printed.append(rule.getConditions().getItemSet()).append(" ==> ").append(rule.getConsequences().getItemSet()).append(" ::= ").append(rule.getConfidence()).append("\n");
        }
        String ret = printed.toString();
        textAreaAssocRules.setText(ret);
    }

    private void startFreqPatterns() {
        start = System.nanoTime();
        Apriori.generateTransactionDB(Apriori.discretizedInstances);
        int value = (int) sliderAprioriMinSupport.getValue();
        Apriori.frequentItemSetsList = Apriori.apriori(value);
        finish = System.nanoTime();
        Apriori.timeFreqPat = (finish - start) / 1000000;
        lblAprioriFreqPatterns.setText(Apriori.timeFreqPat + "ms");
        fillFreqTable();
    }

    private void fillFreqTable() {
        StringBuilder printed = new StringBuilder();
        for (FrequentItemSets frequentItemSets: Apriori.frequentItemSetsList) {
            for (ItemSet itemSet: frequentItemSets.getItemSets()) {
                printed.append(itemSet.getItemSet()).append("\n");
            }
        }
        String ret = String.valueOf(printed);
        textAreaAprioriFreqSets.setText(ret);
    }

    private void startDiscretize() {

        start = System.nanoTime();
        int value = (int) sliderAprioriNumBins.getValue();
        Apriori.discretizedInstances = Discretization.discretizeDataset(dataset, value);
        finish = System.nanoTime();
        Apriori.timeDiscretization = (finish - start) / 1000000;
        lblAprioriDiscretization.setText(Apriori.timeDiscretization + "ms");
    }

    private void startKMeans() {
        if(normalizedDataset == null) {
            return;
        }
        int value = (int)sliderKMeansNumClusters.getValue();

        HashMap<Integer, ArrayList<Integer>> kmeans = KMeans.getKMeans(this.normalizedDataset, value);
        double ret = KMeans.getTotalFMeasure(this.normalizedDataset, kmeans);
        this.fillKMeansResults();
    }
    private void startKMedoids() {
        if(normalizedDataset == null) {
            return;
        }
        int value = (int)sliderKMedoidsNumClusters.getValue();

        HashMap<Integer, ArrayList<Integer>> kmedoids = KMedoids.getKMedoids(this.normalizedDataset, value);
        double ret = KMedoids.getTotalFMeasure(this.normalizedDataset, kmedoids);
        this.fillKMedoidsResults();
    }
    private void startClarans() {
        if(normalizedDataset == null) {
            return;
        }
        int value = (int)sliderClaransNumClusters.getValue();
        int valueSearches = (int)sliderClaransNumSearches.getValue();
        int valueTries = (int)sliderClaransNumTries.getValue();

        HashMap<Integer, ArrayList<Integer>> clarans = Clarans.getClarans(this.normalizedDataset, value, valueSearches, valueTries);
        double ret = Clarans.getTotalFMeasure(this.normalizedDataset, clarans);
        this.fillClaransResults();
    }

    private void fillKMeansResults() {
        lblKMeansExecTime.setText(KMeans.time + "ms");
        lblKMeansEstimation.setText(String.valueOf(Math.floor(KMeans.estimation * 100) / 100));
        lblKMeansFMeasure.setText(String.valueOf(Math.floor(KMeans.fmeasure * 100) / 100));

        String text = this.generateTextAreaContent(KMeans.clusters);
        setKMeansTextAreaContent(text);

        HashMap<Integer, Integer> numElements = getNumberOfElements(KMeans.clusters);
        ArrayList<Label> lbls = constructNumElements(numElements);
        fillKMeansVBox(lbls);
    }
    private void fillKMedoidsResults() {
        lblKMedoidsExecTime.setText(KMedoids.time + "ms");
        lblKMedoidsEstimation.setText(String.valueOf(Math.floor(KMedoids.estimation * 100) / 100));
        lblKMedoidsFMeasure.setText(String.valueOf(Math.floor(KMedoids.fmeasure * 100) / 100));

        String text = this.generateTextAreaContent(KMedoids.clusters);
        setKMedoidsTextAreaContent(text);

        HashMap<Integer, Integer> numElements = getNumberOfElements(KMedoids.clusters);
        ArrayList<Label> lbls = constructNumElements(numElements);
        fillKMedoidsVBox(lbls);
    }
    private void fillClaransResults() {
        lblClaransExecTime.setText(Clarans.time + "ms");
        lblClaransEstimation.setText(String.valueOf(Math.floor(Clarans.estimation * 100) / 100));
        lblClaransFMeasure.setText(String.valueOf(Math.floor(Clarans.fmeasure * 100) / 100));

        String text = this.generateTextAreaContent(Clarans.clusters);
        setClaransTextAreaContent(text);

        HashMap<Integer, Integer> numElements = getNumberOfElements(Clarans.clusters);
        ArrayList<Label> lbls = constructNumElements(numElements);
        fillClaransVBox(lbls);
    }

    private void fillKMeansVBox(ArrayList<Label> lbls) {
        vbKMeans.getChildren().clear();
        vbKMeans.getChildren().addAll(lbls);
        System.out.println(lbls);
    }
    private void fillKMedoidsVBox(ArrayList<Label> lbls) {
        vbKMedoids.getChildren().clear();
        vbKMedoids.getChildren().addAll(lbls);
    }
    private void fillClaransVBox(ArrayList<Label> lbls) {
        vbClarans.getChildren().clear();
        vbClarans.getChildren().addAll(lbls);
    }

    private String generateTextAreaContent(HashMap<Integer, ArrayList<Integer>> clusters) {
        String instance = "Instance ";
        String separator = " ==> ";

        ArrayList<Integer> classA = KMeans.getArrayFromClass(normalizedDataset, 1);
        ArrayList<Integer> classB = KMeans.getArrayFromClass(normalizedDataset, 2);
        ArrayList<Integer> classC = KMeans.getArrayFromClass(normalizedDataset, 3);

        StringBuilder ret = new StringBuilder();

        for(Instance inst : dataset.getInstances()) {
            String instClass = "";
            if(classA.contains(inst.getInstanceNumber())) {
                instClass = "A";
            }
            else if(classB.contains(inst.getInstanceNumber())) {
                instClass = "B";
            }
            else if(classC.contains(inst.getInstanceNumber())){
                instClass = "C";
            }
            String instCluster = "";
            for(int key : clusters.keySet()) {
                if(clusters.get(key).contains(inst.getInstanceNumber())) {
                    instCluster = key + "";
                    break;
                }
            }

            String add = instance + inst.getInstanceNumber() + separator + instClass + "_" + instCluster;
            ret.append(add);
            ret.append("\n");
        }

        return String.valueOf(ret);
    }
    private HashMap<Integer, Integer> getNumberOfElements(HashMap<Integer, ArrayList<Integer>> clusters) {
        HashMap<Integer, Integer> ret = new HashMap<>();

        for(int key : clusters.keySet()) {
            ret.put(key, clusters.get(key).size());
        }
        return ret;
    }
    private ArrayList<Label> constructNumElements(HashMap<Integer, Integer> clusters) {
        ArrayList<Label> lbls = new ArrayList<>();

        for(int key : clusters.keySet()) {
            Label lbl = new Label();
            lbl.setText("Cluster " + key + ": " + clusters.get(key));
            lbl.setFont(Font.font("System", FontWeight.BOLD, 18));
            lbl.setAlignment(Pos.BASELINE_LEFT);
            lbl.setPrefHeight(40);
            lbl.prefWidthProperty().bind(vbKMeans.prefWidthProperty());
            lbl.prefWidthProperty().bind(vbKMeans.widthProperty());
            lbls.add(lbl);
        }
        return lbls;
    }

    private void setKMeansTextAreaContent(String text) {
        textAreaKMeans.setText(text);
    }
    private void setKMedoidsTextAreaContent(String text) {
        textAreaKMedoids.setText(text);
    }
    private void setClaransTextAreaContent(String text) {
        textAreaClarans.setText(text);
    }

    private void importDatasetEvent(ActionEvent actionEvent) throws IOException {
        fileChooserObject.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.txt"));
        File selectedFile = fileChooserObject.showOpenDialog(null);
        if(selectedFile != null) {
            String path = selectedFile.getAbsolutePath();
            dataset = new DataSet(path);
            normalizedDataset = dataset.normalize();
            prepare();
        }
    }

    private void plotScatter(ActionEvent actionEvent) throws IOException {
        String attr1 = (String) cbScatterTabAttribute1.getValue();
        String attr2 = (String) cbScatterTabAttribute2.getValue();

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
        tabApriori.setDisable(false);
        tabKMeans.setDisable(false);
        tabKMedoids.setDisable(false);
        tabClarans.setDisable(false);
    }
}


