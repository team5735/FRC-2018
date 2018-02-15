package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.team5735.constants.RobotConstants;

public class ElevatorIntake implements Subsystem {

    private static ElevatorIntake instance = null;

    public static ElevatorIntake getInstance() {
        if (instance == null) {
            instance = new ElevatorIntake();
        }
        return instance;
    }

    private VictorSPX elevatorIntakeMotor;

    private double targetSpeed;

    private ElevatorIntake() {
        initMotors();
        targetSpeed = 0;
    }

    private void initMotors() {
        // ===== Elevator FRONT SETUP =====
        elevatorIntakeMotor = new VictorSPX(RobotConstants.VICTOR_INTAKE_ELEV_ID);
        elevatorIntakeMotor.set(ControlMode.PercentOutput,targetSpeed);                // Default control mode
        elevatorIntakeMotor.setInverted(false);                                        // In is positive (green led) TODO Check/change inversion to match

        //TODO Maybe add limit switch

        // Configure voltage limits TODO Check if these limits are desired
        elevatorIntakeMotor.configNominalOutputForward(+0.0f, 0);
        elevatorIntakeMotor.configNominalOutputReverse(-0.0f, 0);
        elevatorIntakeMotor.configPeakOutputForward(+12.0f, 0);
        elevatorIntakeMotor.configPeakOutputReverse(-12.0f, 0);
    }

    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    @Override
    public void runInit() {
        targetSpeed = 0;
        elevatorIntakeMotor.set(ControlMode.PercentOutput,targetSpeed);
    }

    @Override
    public void runPeriodic() {
        elevatorIntakeMotor.set(ControlMode.PercentOutput,targetSpeed);
//        System.out.println(elevatorIntakeMotor.getMotorOutputVoltage());
//        System.out.println(elevatorIntakeMotor.getMotorOutputPercent());
    }

    @Override
    public void disabledInit() {
        targetSpeed = 0;
        elevatorIntakeMotor.set(ControlMode.PercentOutput,targetSpeed);
    }
}
