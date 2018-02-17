package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.team5735.constants.PidConstants;
import frc.team5735.constants.RobotConstants;
import frc.team5735.utils.units.Degrees;

/**
 * Subsystem Class for the Wrist
 */
public class Wrist implements Subsystem {

    // ===== Singleton =====
    private static Wrist instance = new Wrist();   // Singleton Instance

    public static Wrist getInstance() {
        if (instance == null) {
            instance = new Wrist();
        }
        return instance;
    }

    // ===== Constants =====
    private static final Degrees
            BACKLASH_MARGIN = new Degrees(5);             // Margin of error to determine POSITION states
    private static final Degrees
            LOWER_BOUND = new Degrees(-110),              // Lowest position of the Wrist
            UPPER_BOUND = new Degrees(0);                 // Highest position of the Wrist

    private static final double GEAR_RATIO = 3.5;               // Gear ratio between motor and Wrist
    private static final double ZEROING_SPEED = 0.275;           // Percent output value for zeroing
    private static final double DEFAULT_SPEED_LIMIT = 0.4;      // Speed limit when in DEFAULT (percentOutput) state
    private static final WristState
            DEFAULT_ENABLE_STATE = WristState.POSITION_HOLDING; // Default state when robot is enabled

    // ===== Instance Fields =====
    private TalonSRX wristMotor;                    // Main wrist motor

    private WristState state;                       // State of the Wrist
    private Degrees targetAngle;                    // Angle to move to     (POSITION states)
    private double targetSpeed;                     // Motor output         (DEFAULT state)
    private boolean isUpperLimitSwitchPressed;      // Used to prevent motor from continuously "zeroing"
    private boolean hasZeroed;                      // Check if wrist has been zeroed

    // ===== Methods =====

    /**
     * Private Constructor only for initializing the singleton
     */
    private Wrist() {
        state = WristState.IDLE;
        initMotors();
        hasZeroed = false;
        isUpperLimitSwitchPressed = false;
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
        wristMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10); //TODO Check encoder error codes
        wristMotor.setSensorPhase(false);
        wristMotor.overrideLimitSwitchesEnable(true);

        // Configure voltage limits TODO Check over these limits
        wristMotor.configNominalOutputForward(+0.0f, 0);
        wristMotor.configNominalOutputReverse(-0.0f, 0);
        wristMotor.configPeakOutputForward(+12.0f, 0);
        wristMotor.configPeakOutputReverse(-12.0f, 0);

        // Configure PID constants TODO Continue to tune pid
        wristMotor.config_kF(PidConstants.WRIST_POS_SLOT_ID, PidConstants.WRIST_POS_KF, 100);
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
        targetAngle = getCurrentAngle();

        // Set target speed to be 0
        targetSpeed = 0;

        state = DEFAULT_ENABLE_STATE;

        if(!hasZeroed && state == WristState.POSITION_HOLDING) {
            zeroSensor();
        }
    }

    /**
     * Run this code periodically (20ms) when enabled
     */
    @Override
    public void runPeriodic() {
        System.out.println("Target:" + targetAngle.getValue() + "Current:" + getCurrentAngle().getValue());
        if (state == WristState.ZEROING) {                                                          // ZEROING STATE
            // UPDATE MOTOR OUTPUT !!!
            wristMotor.set(ControlMode.PercentOutput,ZEROING_SPEED);
            if(checkUpperLimitSwitch()){
                state = WristState.POSITION_HOLDING;
                hasZeroed = true;
                setTargetAngle(new Degrees(0));
            }
        } else if (state == WristState.POSITION_HOLDING || state == WristState.POSITION_BUSY){      // POSITION STATES
            // UPDATE MOTOR OUTPUT !!!
            double output = targetAngle.toNativeUnits().getValue() * GEAR_RATIO;
            wristMotor.set(ControlMode.Position, output);

            // Check if upper limit switch is hit
            checkUpperLimitSwitch();

            updateState();
        } else {
            // UPDATE MOTOR OUTPUT !!!
            wristMotor.set(ControlMode.PercentOutput, targetSpeed * DEFAULT_SPEED_LIMIT);
        }
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
    private boolean checkUpperLimitSwitch() {
        // Check if switch is pressed
        if (wristMotor.getSensorCollection().isFwdLimitSwitchClosed()) {
            // Check if sensor has been zeroed in this instance of sensor being pressed
            if(!isUpperLimitSwitchPressed) {
                wristMotor.setSelectedSensorPosition(0, 0, 0);  //TODO Set timeout
                targetAngle = UPPER_BOUND;                          // Set angle to highest position
                isUpperLimitSwitchPressed = true;                   // Update the limit switch state
            }
        } else {
            isUpperLimitSwitchPressed = false;
        }
        return isUpperLimitSwitchPressed;
    }

    private void updateState() {
        if (state == WristState.POSITION_HOLDING || state == WristState.POSITION_BUSY) {
            // Update Wrist State depending on if current angle is within margin for target angle
            if (new Degrees(wristMotor.getSelectedSensorPosition(PidConstants.WRIST_POS_SLOT_ID)).withinMargin(targetAngle, BACKLASH_MARGIN)) {
                state = WristState.POSITION_HOLDING;
            } else {
                state = WristState.POSITION_BUSY;
            }
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
    public WristState setTargetAngle(Degrees targetAngle) {
        if (targetAngle.getValue() > UPPER_BOUND.getValue()) {          // UPPER_BOUND < targetAngle
            this.targetAngle = UPPER_BOUND;
        } else if (targetAngle.getValue() < LOWER_BOUND.getValue()) {   // targetAngle < LOWER_BOUND
            this.targetAngle = LOWER_BOUND;
        } else {                                                        // LOWER_BOUND < targetAngle < UPPER_BOUND
            this.targetAngle = targetAngle;
        }

        updateState();
        return state;
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
        targetAngle = getCurrentAngle();
        this.state = state;
    }

    public Degrees getCurrentAngle() {
        return new Degrees(nativeToDegree(wristMotor.getSelectedSensorPosition(PidConstants.WRIST_POS_SLOT_ID) / GEAR_RATIO));
    }

    public double nativeToDegree(double nativeUnits) {
        return nativeUnits / 4096 * 360; //TODO check if this is correct
    }

    public void printStatus() {
        System.out.println("===== Drivetrain =====");
        System.out.println("Target Angle: " + targetAngle);
        System.out.println("Current Angle: " + getCurrentAngle());
        System.out.println();
    }

    /**
     * Enum for wrist states
     */
    public enum WristState {
        IDLE, DEFAULT, POSITION_HOLDING, POSITION_BUSY, ZEROING;
    }
}
