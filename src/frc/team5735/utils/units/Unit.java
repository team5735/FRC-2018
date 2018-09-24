package frc.team5735.utils.units;

public abstract class Unit {
    private double value;

    public Unit(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public boolean withinMargin(Unit target, Unit margin) {
        double difference = Math.abs(value - target.getValue());
        return difference <= margin.getValue();
    }
}
