/**
 * Example demonstrating the velocity closed-loop servo.
 * Tested with Logitech F350 USB Gamepad inserted into Driver Station]
 *
 * Be sure to select the correct feedback sensor using SetFeedbackDevice() below.
 *
 * After deploying/debugging this to your RIO, first use the left Y-stick 
 * to throttle the Talon manually.  This will confirm your hardware setup.
 * Be sure to confirm that when the Talon is driving forward (green) the 
 * position sensor is moving in a positive direction.  If this is not the cause
 * flip the boolena input to the SetSensorDirection() call below.
 *
 * Once you've ensured your feedback device is in-phase with the motor,
 * use the button shortcuts to servo to target velocity.  
 *
 * Tweak the PID gains accordingly.
 */

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;

public class Tuning extends IterativeRobot {

    TalonSRX leftMaster = new TalonSRX(4);
    TalonSRX leftSlave = new TalonSRX(3);
    TalonSRX rightMaster = new TalonSRX(1);
    TalonSRX rightSlave = new TalonSRX(2);
    Joystick _joy = new Joystick(0);
    StringBuilder _sb = new StringBuilder();
    int _loops = 0;

    public void robotInit() {
        /* first choose the sensor */
        leftMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
        leftMaster.setSensorPhase(true);
        leftMaster.setInverted(false);
        //leftMaster.configEncoderCodesPerRev(XXX), // if using FeedbackDevice.QuadEncoder
        //leftMaster.configPotentiometerTurns(XXX), // if using FeedbackDevice.AnalogEncoder or AnalogPot

        /* set the peak and nominal outputs, 12V means full */
        leftMaster.configNominalOutputForward(+0.0f, 0);
        leftMaster.configNominalOutputReverse(-0.0f, 0);
        leftMaster.configPeakOutputForward(+12.0f, 0);
        leftMaster.configPeakOutputReverse(-12.0f, 0);
//        leftMaster.configPeakOutputReverse(-0.0f, 0);
        /* set closed loop gains in slot0 */
//        leftMaster.config_kF(0, 0.3589473684, 100);
//        leftMaster.config_kP(0, 0.2728, 100);
//        leftMaster.config_kI(0, 0.002728, 100);
//        leftMaster.config_kD(0, 0, 100);
        leftMaster.config_kF(0, 0.4526548673, 100);
        leftMaster.config_kP(0, 0, 100);
        leftMaster.config_kI(0, 0, 100);
        leftMaster.config_kD(0, 0, 100);

        leftSlave.follow(leftMaster);
        leftSlave.setInverted(false);

        rightMaster.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
        rightMaster.setSensorPhase(true);
        rightMaster.setInverted(false);
        //leftMaster.configEncoderCodesPerRev(XXX), // if using FeedbackDevice.QuadEncoder
        //leftMaster.configPotentiometerTurns(XXX), // if using FeedbackDevice.AnalogEncoder or AnalogPot

        /* set the peak and nominal outputs, 12V means full */
        rightMaster.configNominalOutputForward(+0.0f, 0);
        rightMaster.configNominalOutputReverse(-0.0f, 0);
        rightMaster.configPeakOutputForward(+12.0f, 0);
        rightMaster.configPeakOutputReverse(-12.0f, 0);
//        leftMaster.configPeakOutputReverse(-0.0f, 0);
        /* set closed loop gains in slot0 */
//        leftMaster.config_kF(0, 0.3589473684, 100);
//        leftMaster.config_kP(0, 0.2728, 100);
//        leftMaster.config_kI(0, 0.002728, 100);
//        leftMaster.config_kD(0, 0, 100);
        rightMaster.config_kF(0, 0, 100);
        rightMaster.config_kP(0, 0, 100);
        rightMaster.config_kI(0, 0, 100);
        rightMaster.config_kD(0, 0, 100);

        rightSlave.follow(rightMaster);
        rightSlave.setInverted(false);
    }
    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic() {
	    	/* get gamepad axis */
        double leftYstick = -_joy.getY(Hand.kLeft);
//        double rightYstick = _joy.getY(Hand.kRight);
        double rightYstick = _joy.getRawAxis(5);
        double motorOutput = leftMaster.getMotorOutputVoltage() / leftMaster.getBusVoltage();

        _sb.append("\tlstick:");
        _sb.append(leftYstick);
        _sb.append(" \tout:");
        _sb.append(motorOutput);
        _sb.append(" \tspd:");
        _sb.append(leftMaster.getSelectedSensorVelocity(0) );
        _sb.append(" \tpos:");
        _sb.append(leftMaster.getSelectedSensorPosition(0) );

        _sb.append(" \n\n\trstick:");
        _sb.append(rightYstick);
        _sb.append(" \tout:");
        _sb.append(motorOutput);
        _sb.append(" \tspd:");
        _sb.append(rightMaster.getSelectedSensorVelocity(0) );
        _sb.append(" \tpos:");
        _sb.append(rightMaster.getSelectedSensorPosition(0) );

        if(_joy.getRawButton(2)) {
            leftMaster.setSelectedSensorPosition(0, 0, 100);
            rightMaster.setSelectedSensorPosition(0, 0, 100);
        }

        if(_joy.getRawButton(1)){
        		/* Speed mode */
            double ltargetSpeed = leftYstick * 1000; /* 1500 RPM in either direction */
            double rtargetSpeed = rightYstick * 1000; /* 1500 RPM in either direction */

            leftMaster.set(ControlMode.Velocity, ltargetSpeed);
            rightMaster.set(ControlMode.Velocity, rtargetSpeed);

        		/* append more signals to print when in speed mode. */
            _sb.append(" \n\n\tlerr:");
            _sb.append(leftMaster.getClosedLoopError(0));
            _sb.append(" \tltrg:");
            _sb.append(ltargetSpeed);
            _sb.append(" \trerr:");
            _sb.append(rightMaster.getClosedLoopError(0));
            _sb.append(" \trtrg:");
            _sb.append(rtargetSpeed);
        } else {
        		/* Percent voltage mode */
            leftMaster.set(ControlMode.PercentOutput, leftYstick * 0.5);
            rightMaster.set(ControlMode.PercentOutput, rightYstick * 0.5);
        }

        _sb.append("\n-------------");

        if(++_loops >= 10) {
            _loops = 0;
            System.out.println(_sb.toString());
        }
        _sb.setLength(0);
    }

}
