package frc.team5735.controllers.auto;

import frc.team5735.constants.TrajectoryConstants;
import frc.team5735.controllers.motionprofiling.CustomTrajectory;
import frc.team5735.controllers.motionprofiling.TrajectoryParser;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;
import frc.team5735.subsystems.ElevatorIntake;
import frc.team5735.subsystems.Wrist;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;
import jaci.pathfinder.Trajectory;

import static frc.team5735.constants.PositionConstants.*;

public class Autos {

    public static AutoCommand[][] moveForward = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.straight),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
    };

    public static AutoCommand[][] rightToRightSwitch = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.rightToRightSwitch),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    public static AutoCommand[][] leftToLeftSwitch = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.rightToRightSwitch)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    //TWO CUBE
    public static AutoCommand[][] centerToRightSwitch = {
            {
                    // hold
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {
                    //go to switch
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.centerToRightSwitch),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE)
            },
            {
                    // go back to switch backup
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.rightSwitchToSwitchBackup),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            },
            {
                    // give the good zucc
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.switchBackupToCube),
                    new AutoCommand(ElevatorIntake.getInstance(), false),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_INTAKE_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop, secure
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(ElevatorIntake.getInstance(), true),
            },
            {
                    // go back to switch backup
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.switchBackupToCube)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
            },
            {
                    // go to switch
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.rightSwitchToSwitchBackup)),
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // reverse reverse
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    public static AutoCommand[][] centerToLeftSwitch = {
            {
                    // hold
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {
                    //go to switch
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.centerToRightSwitch)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE)
            },
            {
                    // go back to switch backup
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.rightSwitchToSwitchBackup)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            },
            {
                    // give the good zucc
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.switchBackupToCube),
                    new AutoCommand(ElevatorIntake.getInstance(), false),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_INTAKE_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop, secure
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(ElevatorIntake.getInstance(), true),
            },
            {
                    // go back to switch backup
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(CustomTrajectory.reverse(TrajectoryConstants.switchBackupToCube))),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
            },
            {
                    // go to switch
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(CustomTrajectory.reverse(TrajectoryConstants.rightSwitchToSwitchBackup))),
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // reverse reverse
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    public static AutoCommand[][] rightToRightScale = {
            {       // secure
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {       // drive to scale
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.rightToRightScale),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE))
            },
            {       // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {       // stop, turn around, drop intake
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(180)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            },
            {       // go, open up, zucc
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.rightScaleToCube),
                    new AutoCommand(ElevatorIntake.getInstance(), false),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_INTAKE_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop, secure
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(ElevatorIntake.getInstance(), true),
            },
            {
                    // go back to switch backup
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.rightScaleToCube)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
            },
            {       // turn
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(0)),
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // reverse reverse
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    public static AutoCommand[][] leftToLeftScale = {
            {       // secure
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {       // drive to scale
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.rightToRightScale)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE))
            },
            {       // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {       // stop, turn around, drop intake
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(180)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            },
            {       // go, open up, zucc
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.rightScaleToCube)),
                    new AutoCommand(ElevatorIntake.getInstance(), false),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_INTAKE_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop, secure
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(ElevatorIntake.getInstance(), true),
            },
            {
                    // go back to switch backup
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(CustomTrajectory.reverse(TrajectoryConstants.rightScaleToCube))),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
            },
            {       // turn
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(0)),
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // reverse reverse
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    // TODO: cross field auto
    public static AutoCommand[][] rightToLeftScale = {
            {       // secure
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {       // drive to left scale prep
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.rightToLeftScalePrep),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {       // turn to 0, lift up
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(0)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE))
            },
            {       // go to scale
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.leftScalePrepToLeftScale),
            },
            {       // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {       // stop, turn around, drop intake
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(180)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            },
            {       // go, open up, zucc
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.rightScaleToCube)),
                    new AutoCommand(ElevatorIntake.getInstance(), false),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_INTAKE_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop, secure
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(ElevatorIntake.getInstance(), true),
            },
            {
                    // go back to scale
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(CustomTrajectory.reverse(TrajectoryConstants.rightScaleToCube))),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
            },
            {       // turn
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(0)),
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // reverse reverse
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    public static AutoCommand[][] leftToRightScale = {
            {       // secure
                    new AutoCommand(ElevatorIntake.getInstance(), true)
            },
            {       // drive to left scale prep
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.rightToLeftScalePrep)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {       // turn to 0, lift up
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(0)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE))
            },
            {       // go to scale
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.swapSides(TrajectoryConstants.leftScalePrepToLeftScale)),
            },
            {       // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {       // stop, turn around, drop intake
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(180)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            },
            {       // go, open up, zucc
                    new AutoCommand(Drivetrain.getInstance(), TrajectoryConstants.rightScaleToCube),
                    new AutoCommand(ElevatorIntake.getInstance(), false),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_INTAKE_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // stop, secure
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(ElevatorIntake.getInstance(), true),
            },
            {
                    // go back to scale
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.rightScaleToCube)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
            },
            {       // turn
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(0)),
            },
            {
                    // eject
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_EJECT_VALUE)
            },
            {
                    new AutoCommand(null, INTAKE_DELAY_VALUE)
            },
            {
                    // reverse reverse
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_STOP_VALUE),
                    new AutoCommand(Drivetrain.getInstance(), CustomTrajectory.reverse(TrajectoryConstants.shortPath)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    // ======= TESTING CODE =======
    public static AutoCommand[][] gyroTest = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(45))
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                new AutoCommand(Drivetrain.getInstance(), new Degrees(90))
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(180))
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(270))
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(360))
            },
            {
                    new AutoCommand(null, 1000)
            },
    };
}
