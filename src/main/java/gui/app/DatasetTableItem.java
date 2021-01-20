package gui.app;

import javafx.beans.property.SimpleStringProperty;

public class DatasetTableItem {
    private SimpleStringProperty numberColumn;
    private SimpleStringProperty classsColumn;
    private SimpleStringProperty t3ResinColumn;
    private SimpleStringProperty totalThyroxinColumn;
    private SimpleStringProperty totalTriioColumn;
    private SimpleStringProperty tshColumn;
    private SimpleStringProperty maxDiffTshColumn;

    public DatasetTableItem(
            SimpleStringProperty numberColumn,
            SimpleStringProperty classsColumn,
            SimpleStringProperty t3ResinColumn,
            SimpleStringProperty totalThyroxinColumn,
            SimpleStringProperty totalTriioColumn,
            SimpleStringProperty tshColumn,
            SimpleStringProperty maxDiffTshColumn)
    {
        this.numberColumn = numberColumn;
        this.classsColumn = classsColumn;
        this.t3ResinColumn = t3ResinColumn;
        this.totalThyroxinColumn = totalThyroxinColumn;
        this.totalTriioColumn = totalTriioColumn;
        this.tshColumn = tshColumn;
        this.maxDiffTshColumn = maxDiffTshColumn;
    }

    public String getNumberColumn() {
        return numberColumn.get();
    }

    public SimpleStringProperty numberColumnProperty() {
        return numberColumn;
    }

    public void setNumberColumn(String numberColumn) {
        this.numberColumn.set(numberColumn);
    }

    public String getClasssColumn() {
        return classsColumn.get();
    }

    public SimpleStringProperty classsColumnProperty() {
        return classsColumn;
    }

    public void setClasssColumn(String classsColumn) {
        this.classsColumn.set(classsColumn);
    }

    public String getT3ResinColumn() {
        return t3ResinColumn.get();
    }

    public SimpleStringProperty t3ResinColumnProperty() {
        return t3ResinColumn;
    }

    public void setT3ResinColumn(String t3ResinColumn) {
        this.t3ResinColumn.set(t3ResinColumn);
    }

    public String getTotalThyroxinColumn() {
        return totalThyroxinColumn.get();
    }

    public SimpleStringProperty totalThyroxinColumnProperty() {
        return totalThyroxinColumn;
    }

    public void setTotalThyroxinColumn(String totalThyroxinColumn) {
        this.totalThyroxinColumn.set(totalThyroxinColumn);
    }

    public String getTotalTriioColumn() {
        return totalTriioColumn.get();
    }

    public SimpleStringProperty totalTriioColumnProperty() {
        return totalTriioColumn;
    }

    public void setTotalTriioColumn(String totalTriioColumn) {
        this.totalTriioColumn.set(totalTriioColumn);
    }

    public String getTshColumn() {
        return tshColumn.get();
    }

    public SimpleStringProperty tshColumnProperty() {
        return tshColumn;
    }

    public void setTshColumn(String tshColumn) {
        this.tshColumn.set(tshColumn);
    }

    public String getMaxDiffTshColumn() {
        return maxDiffTshColumn.get();
    }

    public SimpleStringProperty maxDiffTshColumnProperty() {
        return maxDiffTshColumn;
    }

    public void setMaxDiffTshColumn(String maxDiffTshColumn) {
        this.maxDiffTshColumn.set(maxDiffTshColumn);
    }
}
