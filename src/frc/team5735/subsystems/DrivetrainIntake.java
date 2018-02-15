package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.team5735.constants.RobotConstants;

public class DrivetrainIntake implements Subsystem {

    private static DrivetrainIntake instance = null;

    public static DrivetrainIntake getInstance() {
        if (instance == null) {
            instance = new DrivetrainIntake();
        }
        return instance;
    }

    private VictorSPX drivetrainIntakeMotorLeft, drivetrainIntakeMotorRight;

    private double leftTargetSpeed, rightTargetSpeed;

    private DrivetrainIntake() {
        initMotors();
        leftTargetSpeed = 0;
        rightTargetSpeed = 0;
    }

    private void initMotors() {
        // ===== LEFT MOTOR SETUP =====
        drivetrainIntakeMotorLeft = new VictorSPX(RobotConstants.VICTOR_INTAKE_DT_LEFT_ID);
        drivetrainIntakeMotorLeft.set(ControlMode.PercentOutput,leftTargetSpeed);                // Default control mode
        drivetrainIntakeMotorLeft.setInverted(true);                              // In is positive (green led) TODO Check/change inversion to match

        //TODO Maybe add limit switch

        // Configure voltage limits TODO Check if these limits are desired
        drivetrainIntakeMotorLeft.configNominalOutputForward(+0.0f, 0);
        drivetrainIntakeMotorLeft.configNominalOutputReverse(-0.0f, 0);
        drivetrainIntakeMotorLeft.configPeakOutputForward(+12.0f, 0);
        drivetrainIntakeMotorLeft.configPeakOutputReverse(-12.0f, 0);

        // ===== LEFT MOTOR SETUP =====
        drivetrainIntakeMotorRight = new VictorSPX(RobotConstants.VICTOR_INTAKE_DT_RIGHT_ID);
        drivetrainIntakeMotorRight.set(ControlMode.PercentOutput,rightTargetSpeed);                // Default control mode
        drivetrainIntakeMotorRight.setInverted(false);                              // In is positive (green led) TODO Check/change inversion to match

        //TODO Maybe add limit switch

        // Configure voltage limits TODO Check if these limits are desired
        drivetrainIntakeMotorRight.configNominalOutputForward(+0.0f, 0);
        drivetrainIntakeMotorRight.configNominalOutputReverse(-0.0f, 0);
        drivetrainIntakeMotorRight.configPeakOutputForward(+12.0f, 0);
        drivetrainIntakeMotorRight.configPeakOutputReverse(-12.0f, 0);
    }

    public void setTargetSpeed(double targetSpeed) {
        this.leftTargetSpeed = targetSpeed;
        this.rightTargetSpeed = targetSpeed;
    }

    public void setLeftTargetSpeed(double leftTargetSpeed) {
        this.leftTargetSpeed = leftTargetSpeed;
    }

    public void setRightTargetSpeed(double rightTargetSpeed) {
        this.rightTargetSpeed = rightTargetSpeed;
    }

    @Override
    public void runInit() {
        leftTargetSpeed = 0;
        rightTargetSpeed = 0;
        drivetrainIntakeMotorRight.set(ControlMode.PercentOutput,rightTargetSpeed);
        drivetrainIntakeMotorLeft.set(ControlMode.PercentOutput,leftTargetSpeed);
    }

    @Override
    public void runPeriodic() {
        drivetrainIntakeMotorRight.set(ControlMode.PercentOutput,rightTargetSpeed*0.6);
        drivetrainIntakeMotorLeft.set(ControlMode.PercentOutput,leftTargetSpeed*0.6);
    }

    @Override
    public void disabledInit() {
        leftTargetSpeed = 0;
        rightTargetSpeed = 0;
        drivetrainIntakeMotorRight.set(ControlMode.PercentOutput,rightTargetSpeed);
        drivetrainIntakeMotorLeft.set(ControlMode.PercentOutput,leftTargetSpeed);
    }
}
