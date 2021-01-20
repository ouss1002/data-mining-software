package gui.app;

import javafx.beans.property.SimpleStringProperty;

public class OutliersTableItem {
    private SimpleStringProperty instanceColumn;
    private SimpleStringProperty valueColumn;

    public OutliersTableItem(SimpleStringProperty instanceColumn, SimpleStringProperty valueColumn) {
        this.instanceColumn = instanceColumn;
        this.valueColumn = valueColumn;
    }

    public String getInstanceColumn() {
        return instanceColumn.get();
    }

    public SimpleStringProperty instanceColumnProperty() {
        return instanceColumn;
    }

    public void setInstanceColumn(String instanceColumn) {
        this.instanceColumn.set(instanceColumn);
    }

    public String getValueColumn() {
        return valueColumn.get();
    }

    public SimpleStringProperty valueColumnProperty() {
        return valueColumn;
    }

    public void setValueColumn(String valueColumn) {
        this.valueColumn.set(valueColumn);
    }
}
