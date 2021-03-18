package datamining;

public class Variable {
    String value;

    public Variable(String value) {
        this.value = value;
    }
    public double getDouble() {
        return Double.parseDouble(value);
    }
    public int getInteger() {
        return (int) this.getDouble();
    }
    public String get() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
