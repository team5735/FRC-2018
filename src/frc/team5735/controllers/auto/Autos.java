package frc.team5735.controllers.auto;

import com.sun.org.apache.bcel.internal.generic.SWITCH;
import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.controllers.motionprofiling.TrajectoryParser;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;
import frc.team5735.subsystems.ElevatorIntake;
import frc.team5735.subsystems.Wrist;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class Autos {
    private static final double INTAKE_HOLDING_SPEED = 0.42;
    private static final double SWITCH_WRIST_ANGLE = -40;
    private static final double SWITCH_ELEVATOR_HEIGHT = 24;
    private static final double SCALE_WRIST_ANGLE = -80;
    private static final double SCALE_ELEVATOR_HEIGHT = 78;

    public static AutoCommand[][] doNothing = {
            {
                    new AutoCommand(null,null)
            }
    };

    public static AutoCommand[][] startLeftToHomeLeft = { //done
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startLeftToHomeLeft", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SWITCH_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SWITCH_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] startLeftToScaleLeft = { //done
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startLeftToScaleLeft", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SCALE_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SCALE_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] startLeftToForward = { //done
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startLeftToForward", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SWITCH_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SWITCH_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            }
    };

    public static AutoCommand[][] startCenterToHomeLeft = { //done
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startCenterToHomeLeft", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SWITCH_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SWITCH_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] startCenterToHomeRight = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startCenterToHomeRight", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SWITCH_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SWITCH_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] startCenterToScaleLeft = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startCenterToScaleLeft", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SCALE_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SCALE_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            }
//            ,
//            {
//                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
//            },
//            {
//                    new AutoCommand(null, 1000)
//            },
//            {
//                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
//            }
    };

    public static AutoCommand[][] startCenterToScaleRight = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startCenterToScaleRight", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SCALE_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SCALE_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            }
//            ,
//            {
//                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
//            },
//            {
//                    new AutoCommand(null, 1000)
//            },
//            {
//                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
//            }
    };

    public static AutoCommand[][] startRightToHomeRight = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startRightToHomeRight", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SWITCH_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SWITCH_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] startRightToScaleRight = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startRightToScaleRight", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SWITCH_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SWITCH_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 1000)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] startRightToForward = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("startRightToForward", TrajectoryParser.CSV_FORMAT.NORMAL)),
                    new AutoCommand(Wrist.getInstance(), new Degrees(SWITCH_WRIST_ANGLE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(SWITCH_ELEVATOR_HEIGHT)),
                    new AutoCommand(ElevatorIntake.getInstance(), INTAKE_HOLDING_SPEED)
            }
    };
}
