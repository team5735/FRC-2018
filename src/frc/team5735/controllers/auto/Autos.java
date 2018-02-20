package frc.team5735.controllers.auto;

import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;
import frc.team5735.subsystems.ElevatorIntake;
import frc.team5735.subsystems.Wrist;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class Autos {
    public static AutoCommand[][] centerToLeftSwitch = {
            {
                    new AutoCommand(ElevatorIntake.getInstance(), 0.4)
            },
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("centerToLeftSwitch")),
                    new AutoCommand(Wrist.getInstance(), new Degrees(-20)),
                    new AutoCommand(Elevator.getInstance(), new Inches(26))
            },
            {
                    new AutoCommand(Wrist.getInstance(), new Degrees(-40)),
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },
            {
                    new AutoCommand(null, 1000)
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
                    new AutoCommand(Wrist.getInstance(), new Degrees(-20)),
                    new AutoCommand(Elevator.getInstance(), new Inches(40))
            }
            ,{
                    new AutoCommand(Wrist.getInstance(), new Degrees(-30)),
                    new AutoCommand(Elevator.getInstance(), new Inches(58))
            },{
                    new AutoCommand(ElevatorIntake.getInstance(), -1.)
            },{
                    new AutoCommand(null, 500)
            },{
                    new AutoCommand(ElevatorIntake.getInstance(), 0)
            },{
                    new AutoCommand(Wrist.getInstance(), new Degrees(-85)),
                    new AutoCommand(Elevator.getInstance(), new Inches(6))
            }
    };

    public static AutoCommand[][] gyroTest = {
            {
                new AutoCommand(Drivetrain.getInstance(), new Degrees(180))
            }
    };
}
