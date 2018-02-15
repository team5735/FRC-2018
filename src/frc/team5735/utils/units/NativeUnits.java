package frc.team5735.utils.units;

public class NativeUnits extends Unit {
    public NativeUnits(double value) {
        super(value);
    }

    public Degrees toDegrees() {
        return new Degrees(getValue() / 4096 * 360);
    }
}
