package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.team5735.constants.RobotConstants;

public class DrivetrainIntake implements Subsystem {

    // ===== Singleton =====
    private static DrivetrainIntake instance = new DrivetrainIntake();

    public static DrivetrainIntake getInstance() {
        if (instance == null) {
            instance = new DrivetrainIntake();
        }
        return instance;
    }

    // ===== Constants =====
    private static final double OUTPUT_LIMIT = 0.8;

    // ===== Instance Fields =====
    // Motor Controllers
    private VictorSPX drivetrainIntakeMotorLeft, drivetrainIntakeMotorRight;

    // Target Outputs
    private double leftTargetSpeed, rightTargetSpeed;

    // ===== Methods =====
    private DrivetrainIntake() {
        initMotors();
        leftTargetSpeed = 0;
        rightTargetSpeed = 0;
    }

    private void initMotors() {
        // ===== LEFT MOTOR SETUP =====
        drivetrainIntakeMotorLeft = new VictorSPX(RobotConstants.VICTOR_INTAKE_DT_LEFT_ID);
        drivetrainIntakeMotorLeft.set(ControlMode.PercentOutput,leftTargetSpeed); // Default control mode
        drivetrainIntakeMotorLeft.setInverted(true);                              // In is positive (green led)

        // Configure voltage limits
        drivetrainIntakeMotorLeft.configNominalOutputForward(+0.0f, 0);
        drivetrainIntakeMotorLeft.configNominalOutputReverse(-0.0f, 0);
        drivetrainIntakeMotorLeft.configPeakOutputForward(+12.0f, 0);
        drivetrainIntakeMotorLeft.configPeakOutputReverse(-12.0f, 0);

        // ===== LEFT MOTOR SETUP =====
        drivetrainIntakeMotorRight = new VictorSPX(RobotConstants.VICTOR_INTAKE_DT_RIGHT_ID);
        drivetrainIntakeMotorRight.set(ControlMode.PercentOutput,rightTargetSpeed); // Default control mode
        drivetrainIntakeMotorRight.setInverted(false);                              // In is positive (green led)

        // Configure voltage limits
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
        drivetrainIntakeMotorRight.set(ControlMode.PercentOutput,rightTargetSpeed*OUTPUT_LIMIT);
        drivetrainIntakeMotorLeft.set(ControlMode.PercentOutput,leftTargetSpeed*OUTPUT_LIMIT);
    }

    @Override
    public void disabledInit() {
        runInit();
    }
}
