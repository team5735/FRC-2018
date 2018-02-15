package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team5735.constants.PidConstants;
import frc.team5735.constants.RobotConstants;
import frc.team5735.utils.UnitConversion;
import frc.team5735.utils.units.Degrees;

/**
 * Subsystem Class for the Wrist
 */
public class Wrist implements Subsystem {

    // ===== Singleton =====
    private static Wrist instance = null;   // Singleton Instance

    public static Wrist getInstance() {
        if (instance == null) {
            instance = new Wrist();
        }
        return instance;
    }

    // ===== Constants =====
    private static final Degrees
            BACKLASH_MARGIN = new Degrees(5);         // Margin of error to determine states
    private static final Degrees
            LOWER_BOUND = new Degrees(0),             // Lowest position of the Wrist
            UPPER_BOUND = new Degrees(110);            // Highest position of the Wrist

    private static final double GEAR_RATIO = 3.5;           // Gear ratio between motor and Wrist
    private static final double ZEROING_SPEED = -0.15;      // Percent output value for zeroing
    private static final double DEFAULT_SPEED_LIMIT = 0.4;  // Factor to limit speed when in DEFAULT state
    private static final WristState DEFAULT_ENABLE_STATE = WristState.POSITION_HOLDING;

    // ===== Instance Fields =====
    private TalonSRX wristMotor;    // Main wrist motor

    private WristState state;       // State of the Wrist
    private Degrees targetAngle;    // Angle to move to in POSITION states
    private double targetSpeed;     // Motor output in DEFAULT state
    private boolean hasZeroed;      // Flag to notify if limit switch has been zeroed for this switch interaction

    // ===== Methods =====

    /**
     * Private Constructor only for initializing the singleton
     */
    private Wrist() {
        state = WristState.IDLE;
        initMotors();
        hasZeroed = false;
    }

    /**
     * Initialize motors
     */
    private void initMotors() {
        // ===== WRIST MOTOR SETUP =====
        wristMotor = new TalonSRX(RobotConstants.TALON_WRIST_ID);
        wristMotor.set(ControlMode.PercentOutput,0);                // Percent Output in DEFAULT state
        wristMotor.setInverted(false);                              // Up is positive (green led)

        // Configure Sensors    //TODO Add soft limits ??
        wristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute,0,10); //TODO Check encoder error codes
        wristMotor.setSensorPhase(false);
        wristMotor.overrideLimitSwitchesEnable(true);

        // Configure voltage limits TODO Check over these limits
        wristMotor.configNominalOutputForward(+0.0f, 0);
        wristMotor.configNominalOutputReverse(-0.0f, 0);
        wristMotor.configPeakOutputForward(+12.0f, 0);
        wristMotor.configPeakOutputReverse(-12.0f, 0);

        // Configure PID constants TODO Continue to tune pid
//        wristMotor.config_kF(PidConstants.WRIST_POS_SLOT_ID, 1023/(218./0.5), 100);        // Not needed for position mode
        wristMotor.config_kP(PidConstants.WRIST_POS_SLOT_ID, PidConstants.WRIST_POS_KP, 100);    // Tested with 0.2 (too weak)
        wristMotor.config_kI(PidConstants.WRIST_POS_SLOT_ID, PidConstants.WRIST_POS_KI, 100);
        wristMotor.config_kD(PidConstants.WRIST_POS_SLOT_ID, PidConstants.WRIST_POS_KD, 100);
    }

    /**
     * Run this code before every mode enable
     */
    @Override
    public void runInit() {
        // Set target angle to be the current sensor position so it won't move/jump when motor output updates
        targetAngle = new Degrees(UnitConversion.nativeToDegree(wristMotor.getSelectedSensorPosition(PidConstants.WRIST_POS_SLOT_ID) / GEAR_RATIO));

        // Set target speed to be 0
        targetSpeed = 0;

        state = DEFAULT_ENABLE_STATE;
//        // Runs periodic code once TODO Check if needed
        runPeriodic();
    }

    /**
     * Run this code periodically (20ms) when enabled
     */
    @Override
    public void runPeriodic() {
        if (state == WristState.ZEROING) {                                                          // ZEROING STATE
            wristMotor.set(ControlMode.PercentOutput,ZEROING_SPEED);
            if(checkLowerLimitSwitch()){
                state = WristState.POSITION_HOLDING;
                setTargetAngle(new Degrees(5));
            }
        } else if (state == WristState.POSITION_HOLDING || state == WristState.POSITION_BUSY){      // POSITION STATES
            // Check if lower limit switch is hit
            checkLowerLimitSwitch();

            // UPDATE MOTOR OUTPUT !!!
            wristMotor.set(ControlMode.Position, targetAngle.toNativeUnits().getValue()/GEAR_RATIO);

            // Update Wrist State depending on if current angle is within margin for target angle
            if (new Degrees(wristMotor.getSelectedSensorPosition(PidConstants.WRIST_POS_SLOT_ID)).withinMargin(targetAngle, BACKLASH_MARGIN)) {
                state = WristState.POSITION_HOLDING;
            } else {
                state = WristState.POSITION_BUSY;
            }
        } else {
            wristMotor.set(ControlMode.PercentOutput, targetSpeed * DEFAULT_SPEED_LIMIT);
        }

        // Dashboard Information
//        SmartDashboard.putNumber("Wrist Motor Vel",wristMotor.getSelectedSensorVelocity(0));    //TODO Remove later
//        SmartDashboard.putNumber("angle", targetAngle.getValue());
//        SmartDashboard.putNumber("pos", UnitConversion.nativeToDegree(wristMotor.getSelectedSensorPosition(0))/GEAR_RATIO);
    }

    /**
     * Run this code when robot is disabled
     */
    @Override
    public void disabledInit() {
        // Put motor in IDLE state and stop motor
        state = WristState.IDLE;
        wristMotor.set(ControlMode.PercentOutput,0);
    }

    /**
     * Method to check lower limit switch and reset sensor if needed
     * @return Lower limit switch is pressed
     */
    public boolean checkLowerLimitSwitch() {
        // Check if switch is pressed
        if (wristMotor.getSensorCollection().isRevLimitSwitchClosed()) {
            // Check if sensor has been zeroed in this instance of sensor being pressed
            if(!hasZeroed) {
                wristMotor.setSelectedSensorPosition(0, 0, 0);  //TODO Set timeout
                targetAngle = LOWER_BOUND;  // Set angle to lowest position
                hasZeroed = true;           // Indicate that sensor has been zeroed in this instance
            }
            return true;        // Switch IS pressed
        } else {
            hasZeroed = false;  // Resets flag
            return false;       // Switch is NOT pressed
        }
    }

    /**
     * Gets the current target angle
     * @return Current target angle
     */
    public Degrees getTargetAngle() {
        return targetAngle;
    }

    /**
     * Sets target angle (limits it to the bounds of the wrist)
     * @param targetAngle New target angle
     */
    public void setTargetAngle(Degrees targetAngle) {
        if (targetAngle.getValue() > UPPER_BOUND.getValue()) {          // UPPER_BOUND < targetAngle
            this.targetAngle = UPPER_BOUND;
        } else if (targetAngle.getValue() < LOWER_BOUND.getValue()) {   // targetAngle < LOWER_BOUND
            this.targetAngle = LOWER_BOUND;
        } else {                                                        // LOWER_BOUND < targetAngle < UPPER_BOUND
            this.targetAngle = targetAngle;
        }
    }

    /**
     * Sets target speed
     */
    public void setTargetSpeed(double targetSpeed) {
        this.targetSpeed = targetSpeed;
    }

    /**
     * Sets Wrist to ZEROING state to Zero itself
     */
    public void zeroSensor() {
        state = WristState.ZEROING;
    }

    public void setState(WristState state) {
        // Set target angle to be the current sensor position so it won't move/jump when motor output updates
        targetAngle = new Degrees(UnitConversion.nativeToDegree(wristMotor.getSelectedSensorPosition(PidConstants.WRIST_POS_SLOT_ID) / GEAR_RATIO));
        this.state = state;
    }

    /**
     * Enum for wrist states
     */
    public enum WristState {
        IDLE, DEFAULT, POSITION_HOLDING, POSITION_BUSY, ZEROING;
    }
}
