package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.drive.RobotDriveBase;
import edu.wpi.first.wpilibj.smartdashboard.SendableBuilder;
import frc.team5735.constants.PidConstants;
import frc.team5735.constants.RobotConstants;

public class Drivetrain extends RobotDriveBase implements Subsystem {
    // ===== Singleton =====
    private static Drivetrain instance = new Drivetrain();

    public static Drivetrain getInstance() {
        if (instance == null) {
            instance = new Drivetrain();
        }
        return instance;
    }

    // ===== Instance Fields =====
    // Curvature drive
    private double m_quickStopThreshold = 0.2;
    private double m_quickStopAlpha = 0.1;
    private double m_quickStopAccumulator = 0.0;

    // Motor Controllers
    private TalonSRX leftFrontMotor, rightFrontMotor, leftRearMotor;
    private VictorSPX rightRearMotor;

    // Output Values
    private double leftSideTargetOutput = 0, rightSideTargetOutput = 0;

    // ===== Methods =====
    private Drivetrain() {
        initMotors();
    }

    private void initMotors() {
        // ===== LEFT FRONT MOTOR SETUP =====
        leftFrontMotor = new TalonSRX(RobotConstants.TALON_DT_LEFT_FRONT_ID);
        leftFrontMotor.set(ControlMode.PercentOutput,0);     //Default control mode TODO ask if current was a typo
        leftFrontMotor.setInverted(false);

        // Configure Sensors
        leftFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10); //TODO Check encoder error codes
        leftFrontMotor.setSensorPhase(true);

        // Configure voltage limits TODO add ramp rate
        leftFrontMotor.configNominalOutputForward(+0.0f, 0);
        leftFrontMotor.configNominalOutputReverse(-0.0f, 0);
        leftFrontMotor.configPeakOutputForward(+12.0f, 0);
        leftFrontMotor.configPeakOutputReverse(-12.0f, 0);

        // Configure PID constants
        leftFrontMotor.config_kF(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KF, 100);
        leftFrontMotor.config_kP(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KP, 100);
        leftFrontMotor.config_kI(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KI, 100);
        leftFrontMotor.config_kD(PidConstants.DT_LEFT_VEL_SLOT_ID, PidConstants.DT_LEFT_VEL_KD, 100);

        // ===== LEFT REAR MOTOR SETUP =====
        leftRearMotor = new TalonSRX(RobotConstants.TALON_DT_LEFT_REAR_ID);
        leftRearMotor.set(ControlMode.Follower, 0);
        leftRearMotor.follow(leftFrontMotor);
        leftRearMotor.setInverted(false);

        // ===== RIGHT FRONT MOTOR SETUP =====
        rightFrontMotor = new TalonSRX(RobotConstants.TALON_DT_RIGHT_FRONT_ID);
        rightFrontMotor.set(ControlMode.PercentOutput,0);     //Default control mode TODO ask if current was a typo
        rightFrontMotor.setInverted(true);

        // Configure Sensors
        rightFrontMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10); //TODO Check encoder error codes
        rightFrontMotor.setSensorPhase(false);

        // Configure voltage limits TODO add ramp rate
        rightFrontMotor.configNominalOutputForward(+0.0f, 0);
        rightFrontMotor.configNominalOutputReverse(-0.0f, 0);
        rightFrontMotor.configPeakOutputForward(+12.0f, 0);
        rightFrontMotor.configPeakOutputReverse(-12.0f, 0);

        // Configure PID constants
        rightFrontMotor.config_kF(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KF, 100);
        rightFrontMotor.config_kP(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KP, 100);
        rightFrontMotor.config_kI(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KI, 100);
        rightFrontMotor.config_kD(PidConstants.DT_RIGHT_VEL_SLOT_ID, PidConstants.DT_RIGHT_VEL_KD, 100);

        // ===== RIGHT REAR MOTOR SETUP =====
        rightRearMotor = new VictorSPX(RobotConstants.VICTOR_DT_RIGHT_REAR_ID);
        rightRearMotor.set(ControlMode.Follower, 0);
        rightRearMotor.follow(rightFrontMotor);
        rightRearMotor.setInverted(true);
    }

    @Override
    public void runInit() {

    }

    @Override
    public void runPeriodic() {
        leftFrontMotor.set(ControlMode.PercentOutput, leftSideTargetOutput);
        rightFrontMotor.set(ControlMode.PercentOutput, rightSideTargetOutput);
    }

    @Override
    public void disabledInit() {

    }

    public void curvatureDrive(double xSpeed, double zRotation, boolean isQuickTurn) {
        xSpeed = limit(xSpeed);
        xSpeed = applyDeadband(xSpeed, m_deadband);

        zRotation = limit(zRotation);
        zRotation = applyDeadband(zRotation, m_deadband);

        double angularPower;
        boolean overPower;

        if (isQuickTurn) {
            if (Math.abs(xSpeed) < m_quickStopThreshold) {
                m_quickStopAccumulator = (1 - m_quickStopAlpha) * m_quickStopAccumulator
                        + m_quickStopAlpha * limit(zRotation) * 2;
            }
            overPower = true;
            angularPower = zRotation;
        } else {
            overPower = false;
            angularPower = Math.abs(xSpeed) * zRotation - m_quickStopAccumulator;

            if (m_quickStopAccumulator > 1) {
                m_quickStopAccumulator -= 1;
            } else if (m_quickStopAccumulator < -1) {
                m_quickStopAccumulator += 1;
            } else {
                m_quickStopAccumulator = 0.0;
            }
        }

        double leftMotorOutput = xSpeed + angularPower;
        double rightMotorOutput = xSpeed - angularPower;

        // If rotation is overpowered, reduce both outputs to within acceptable range
        if (overPower) {
            if (leftMotorOutput > 1.0) {
                rightMotorOutput -= leftMotorOutput - 1.0;
                leftMotorOutput = 1.0;
            } else if (rightMotorOutput > 1.0) {
                leftMotorOutput -= rightMotorOutput - 1.0;
                rightMotorOutput = 1.0;
            } else if (leftMotorOutput < -1.0) {
                rightMotorOutput -= leftMotorOutput + 1.0;
                leftMotorOutput = -1.0;
            } else if (rightMotorOutput < -1.0) {
                leftMotorOutput -= rightMotorOutput + 1.0;
                rightMotorOutput = -1.0;
            }
        }

        leftSideTargetOutput = leftMotorOutput * m_maxOutput;
        rightSideTargetOutput = rightMotorOutput * m_maxOutput;

        m_safetyHelper.feed();
    }

    public TalonSRX getLeftMotor() {
        return leftFrontMotor;
    }

    public TalonSRX getRightMotor() {
        return rightFrontMotor;
    }

    // ===== Extending RobotDriveBase =====

    @Override
    public void initSendable(SendableBuilder builder) {

    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public void stopMotor() {
        rightFrontMotor.set(ControlMode.Disabled, 0);
    }
}
