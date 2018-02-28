package frc.team5735.controllers.auto;

import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;
import frc.team5735.subsystems.ElevatorIntake;
import frc.team5735.subsystems.Wrist;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;
import static frc.team5735.constants.PositionConstants.*;

public class Autos {

    //Center = 9ft from right
    //Right = 5ft from right
    //Left = 5ft from left

    public static AutoCommand[][] doNothing = {};

    // NEEDS CONFIRMATION
    public static AutoCommand[][] centerToLeftSwitch = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(),0.4)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("centerToLeftSwitch")),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 250)     //1000
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] centerToRightSwitch = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.4)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("centerToRightSwitch")),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 250)  //1000
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };

    public static AutoCommand[][] rightToRightScale = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.4)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("rightToRightScale")),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE))
            },
            {
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
//                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE))
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 500)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            },
            {
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE))
            }
    };

    // ======= TESTING CODE =======
    public static AutoCommand[][] gyroTest = {
            {
                new AutoCommand(Drivetrain.getInstance(), new Degrees(180))
            }
    };

    public static AutoCommand[][] twoCube = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.4)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("rightToRightScale")),
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SCALE))
            },
//            {
//                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_SCALE)),
//            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 100)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            },
            {
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_INTAKE)),
                    new AutoCommand(Drivetrain.getInstance(), new Degrees(125-37))
            },
            {
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_INTAKE)),
                    new AutoCommand(ElevatorIntake.getInstance(), 1.),
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("rightScaleToRightSwitch")),
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.4)
            },
            {
                    new AutoCommand(Wrist.getInstance(), new Degrees(WRIST_FLAT)),
                    new AutoCommand(Elevator.getInstance(), new Inches(ELEVATOR_SWITCH))
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 500)
            },
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.)
            }
    };
}
