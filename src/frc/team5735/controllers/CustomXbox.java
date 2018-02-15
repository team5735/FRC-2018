package frc.team5735.controllers;

import edu.wpi.first.wpilibj.XboxController;

public class CustomXbox extends XboxController{

    /**
     * Construct an instance of a joystick. The joystick index is the USB port on the drivers
     * station.
     *
     * @param port The port on the Driver Station that the joystick is plugged into.
     */
    public CustomXbox(int port) {
        super(port);
    }

    @Override
    public double getY(Hand hand) {
        return -super.getY(hand);
    }

    public double getY(Hand hand, double deadband) {
        double value = getY(hand);
        if (Math.abs(value) < deadband) {
            return 0;
        } else {
            return value;
        }
    }

    public double getX(Hand hand, double deadband) {
        double value = getX(hand);
        if (Math.abs(value) < deadband) {
            return 0;
        } else {
            return value;
        }
    }
}
