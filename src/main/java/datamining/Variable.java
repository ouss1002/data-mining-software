package datamining;

public class Variable {
    String value;

    public Variable(String value) {
        this.value = value;
    }
    public Double getDouble() {
        return Double.parseDouble(value);
    }
    public Integer getInteger() {
        return Integer.parseInt(value);
    }
    public String get() {
        return value;
    }
}
