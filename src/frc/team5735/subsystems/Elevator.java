package frc.team5735.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.team5735.constants.PidConstants;
import frc.team5735.constants.RobotConstants;
import frc.team5735.utils.units.Inches;

/**
 * Subsystem Class for the Elevator
 */
public class Elevator implements Subsystem {

    // ===== Singleton =====
    private static Elevator instance = new Elevator();   // Singleton Instance

    public static Elevator getInstance() {
        if (instance == null) {
            instance = new Elevator();
        }
        return instance;
    }

    // ===== Constants =====
    private static final Inches
            BACKLASH_MARGIN = new Inches(1);         // Margin of error to determine states
    private static final Inches
            LOWER_BOUND = new Inches(0),             // Lowest position of the Elevator
            UPPER_BOUND = new Inches(43);            // Highest position of the Elevator

    private static final double GEAR_RATIO = 4.0 / 3.0;           // Gear ratio between motor and Elevator
    private static final int SPROCKET_TOOTH_COUNT = 22;
    private static final double LENGTH_OF_LINK = 0.25;

    private static final int ENCODER_TICKS_PER_REVOLUTION = 4096;   //TODO CHECK this?

    private static final double ZEROING_SPEED = -0.1;      // Percent output value for zeroing
    private static final double DEFAULT_SPEED_LIMIT = 0.80;  // Factor to limit speed when in DEFAULT state
    private static final ElevatorState DEFAULT_ENABLE_STATE = ElevatorState.POSITION_HOLDING;

    // ===== Instance Fields =====
    private TalonSRX elevatorMotor;                 // Main elevator motor

    private ElevatorState state;                    // State of the Elevator
    private Inches targetHeight;                    // Height to move to    (POSITION states)
    private double targetSpeed;                     // Motor output         (DEFAULT state)
    private boolean isLowerLimitSwitchPressed;      // Used to prevent motor from continuously "zeroing"
    private boolean hasZeroed;                      // Check if elevator has been zeroed

    // ===== Methods =====

    /**
     * Private Constructor only for initializing the singleton
     */
    private Elevator() {
        state = ElevatorState.IDLE;
        initMotors();
        isLowerLimitSwitchPressed = false;
        hasZeroed = false;
    }

    /**
     * Initialize motors
     */
    private void initMotors() {
        // ===== ELEVATOR MOTOR SETUP =====
        elevatorMotor = new TalonSRX(RobotConstants.TALON_ELEV_ID);
        elevatorMotor.set(ControlMode.PercentOutput,0);                // Percent Output in DEFAULT state
        elevatorMotor.setInverted(true);                              // Up is positive (green led)

        // Configure Sensors
        elevatorMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,10);
        elevatorMotor.setSensorPhase(true);
        elevatorMotor.overrideLimitSwitchesEnable(true);

        // Configure voltage limits TODO Check over these limits
        elevatorMotor.configNominalOutputForward(+0.0f, 0);
        elevatorMotor.configNominalOutputReverse(-0.0f, 0);
        elevatorMotor.configPeakOutputForward(+12.0f, 0);
        elevatorMotor.configPeakOutputReverse(-12.0f, 0);

        // Configure PID constants TODO Continue to tune pid
        elevatorMotor.config_kF(PidConstants.ELEVATOR_POS_SLOT_ID, PidConstants.ELEVATOR_POS_KF, 100);    // Not needed for position mode
        elevatorMotor.config_kP(PidConstants.ELEVATOR_POS_SLOT_ID, PidConstants.ELEVATOR_POS_KP, 100);    // Tested with 0.2 (too weak)
        elevatorMotor.config_kI(PidConstants.ELEVATOR_POS_SLOT_ID, PidConstants.ELEVATOR_POS_KI, 100);
        elevatorMotor.config_kD(PidConstants.ELEVATOR_POS_SLOT_ID, PidConstants.ELEVATOR_POS_KD, 100);
    }

    /**
     * Run this code before every mode enable
     */
    @Override
    public void runInit() {
        // Set target height to be the current sensor position so it won't move/jump when motor output updates
        targetHeight = encoderTicksToElevatorInches(elevatorMotor.getSelectedSensorPosition(0));

        // Set target speed to be 0
        targetSpeed = 0;

        state = DEFAULT_ENABLE_STATE;

        if(!hasZeroed && state == ElevatorState.POSITION_HOLDING) {
            zeroSensor();
        }
    }

    /**
     * Run this code periodically (20ms) when enabled
     */
    @Override
    public void runPeriodic() {
//        System.out.println("Target:" +targetHeight.getValue() + "Current:" + encoderTicksToElevatorInches(elevatorMotor.getSelectedSensorPosition(0)).getValue());
        if (state == ElevatorState.ZEROING) {                                                          // ZEROING STATE
            // UPDATE MOTOR OUTPUT !!!
            elevatorMotor.set(ControlMode.PercentOutput,ZEROING_SPEED);
            if(checkLowerLimitSwitch()){
                state = ElevatorState.POSITION_HOLDING;
                hasZeroed = true;
                setTargetHeight(new Inches(0));
            }
        } else if (state == ElevatorState.POSITION_HOLDING || state == ElevatorState.POSITION_BUSY){      // POSITION STATES
            // UPDATE MOTOR OUTPUT !!!
            elevatorMotor.set(ControlMode.Position, elevatorInchesToEncoderTicks(targetHeight));

            // Check if lower limit switch is hit
            checkLowerLimitSwitch();

            updateState();

        } else {
            elevatorMotor.set(ControlMode.PercentOutput, targetSpeed * DEFAULT_SPEED_LIMIT);
        }
    }

    /**
     * Run this code when robot is disabled
     */
    @Override
    public void disabledInit() {
        // Put motor in IDLE state and stop motor
        state = ElevatorState.IDLE;
        elevatorMotor.set(ControlMode.PercentOutput,0);
    }

    /**
     * Method to check lower limit switch and reset sensor if needed
     * @return Lower limit switch is pressed
     */
    public boolean checkLowerLimitSwitch() {
        // Check if switch is pressed
        if (elevatorMotor.getSensorCollection().isRevLimitSwitchClosed()) {
            // Check if sensor has been zeroed in this instance of sensor being pressed
            if(!isLowerLimitSwitchPressed) {
                elevatorMotor.setSelectedSensorPosition(0, 0, 0);  //TODO Set timeout
                targetHeight = LOWER_BOUND;  // Set height to lowest position
                isLowerLimitSwitchPressed = true;           // Indicate that sensor has been zeroed in this instance
            }
        } else {
            isLowerLimitSwitchPressed = false;  // Resets flag
        }
        return isLowerLimitSwitchPressed;
    }

    public void updateState () {
        if (state == ElevatorState.POSITION_HOLDING || state == ElevatorState.POSITION_BUSY) {
            // Update Elevator State depending on if current height is within margin for target height
            if (encoderTicksToElevatorInches(elevatorMotor.getSelectedSensorPosition(0)).withinMargin(targetHeight, BACKLASH_MARGIN)) {
                state = ElevatorState.POSITION_HOLDING;
            } else {
                state = ElevatorState.POSITION_BUSY;
            }
        }
    }

    /**
     * Gets the current target height
     * @return Current target height
     */
    public Inches getTargetHeight() {
        return targetHeight;
    }

    /**
     * Sets target height (limits it to the bounds of the Elevator)
     * @param targetHeight New target height
     */
    public ElevatorState setTargetHeight(Inches targetHeight) {
        if (targetHeight.getValue() > UPPER_BOUND.getValue()) {          // UPPER_BOUND < targetHeight
            this.targetHeight = UPPER_BOUND;
        } else if (targetHeight.getValue() < LOWER_BOUND.getValue()) {   // targetHeight < LOWER_BOUND
            this.targetHeight = LOWER_BOUND;
        } else {                                                        // LOWER_BOUND < targetHeight < UPPER_BOUND
            this.targetHeight = targetHeight;
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
     * Sets Elevator to ZEROING state to Zero itself
     */
    public void zeroSensor() {
        state = ElevatorState.ZEROING;
    }

    public void setState(ElevatorState state) {
        // Set target height to be the current sensor position so it won't move/jump when motor output updates
        targetHeight = encoderTicksToElevatorInches(elevatorMotor.getSelectedSensorPosition(0));
        this.state = state;
    }

    public static double elevatorInchesToEncoderTicks(Inches elevatorInches) {
        return (elevatorInches.getValue() / (GEAR_RATIO * SPROCKET_TOOTH_COUNT * LENGTH_OF_LINK)) * ENCODER_TICKS_PER_REVOLUTION / 2;
    }

    public static Inches encoderTicksToElevatorInches(double encoderTicks) {
        return new Inches((encoderTicks / ENCODER_TICKS_PER_REVOLUTION) * GEAR_RATIO * SPROCKET_TOOTH_COUNT * LENGTH_OF_LINK * 2);
    }

    public void printInformation(){
        SmartDashboard.putNumber("Voltage", elevatorMotor.getMotorOutputVoltage());
        SmartDashboard.putNumber("Position: ", elevatorMotor.getSelectedSensorPosition(0));
        SmartDashboard.putNumber("Velocity: ", elevatorMotor.getSelectedSensorVelocity(0));
        SmartDashboard.putNumber("Output", elevatorMotor.getMotorOutputPercent());
        SmartDashboard.putNumber("Height", targetHeight.getValue());
    }

    public ElevatorState getState() {
        return state;
    }

    /**
     * Enum for elevator states
     */
    public enum ElevatorState {
        IDLE, DEFAULT, POSITION_HOLDING, POSITION_BUSY, ZEROING;
    }
}
