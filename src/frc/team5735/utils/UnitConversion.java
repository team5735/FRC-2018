package frc.team5735.utils;

public class UnitConversion {
    public static double degreeToNative(double degree) {
        return degree / 360 * 4096; //TODO check if this is correct
    }

    public static double nativeToDegree(double nativeUnits) {
        return nativeUnits / 4096 * 360; //TODO check if this is correct
    }
}
