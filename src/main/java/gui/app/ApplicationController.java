package gui.app;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTreeTableView;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ApplicationController {

    @FXML
    private JFXTreeTableView<?> tableDataset;

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
    private WebView wvBoxTab;

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
    private WebView wvHistogramTab;

    @FXML
    private JFXComboBox<?> cbHistogramTabAttribute;

    @FXML
    private WebView wvScatterTab;

    @FXML
    private JFXComboBox<?> cbScatterTabAttribute1;

    @FXML
    private JFXComboBox<?> cbScatterTabAttribute2;

    @FXML
    private JFXButton btnScatterTabPlot;

    public FileChooser fileChooserObject = new FileChooser();
    public String fileChosen = "";

    public ApplicationController() throws Exception {

    }

    public void initialize(){
        fileChooserObject.getExtensionFilters().add(new FileChooser.ExtensionFilter("files", "*.txt"));

        btnDatasetImport.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent2) {
                /*String str = ApplicationController.this.fileChooserClick();
                fileChosen = str;
                try {
                    trythis();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        });
    }

    /*private void trythis() throws IOException {
        Table tt = readData(fileChosen);
        System.out.println(tt);

        System.out.println(tableDataset.getTreeItem(0));
    }

    private String fileChooserClick() {
        File selectedFile = fileChooserObject.showOpenDialog(null);

        return selectedFile.getPath();
    }

    private Table readData(String path) throws IOException {
        CsvReadOptions.Builder builder = CsvReadOptions.builder(path).
                header(false).tableName("Thyroid Dataset");

        CsvReadOptions options = builder.build();
        Table t = Table.read().usingOptions(options);
        String[] column_names = {"class", "t3_resin", "total_thyroxin", "total_triio", "tsh", "max_diff_tsh"};
        for(int i=0;i<column_names.length;i++){
            t.column(i).setName(column_names[i]);
        }
        return t;
    }*/

}
