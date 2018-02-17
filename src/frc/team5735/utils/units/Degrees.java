package frc.team5735.utils.units;

public class Degrees extends Unit {
    public Degrees(double value) {
        super(value);
    }

    public NativeUnits toNativeUnits() {
        return new NativeUnits(getValue() / 360. * 4096.);
    }
}
