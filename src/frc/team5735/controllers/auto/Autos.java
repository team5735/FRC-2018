package frc.team5735.controllers.auto;

import frc.team5735.controllers.motionprofiling.Trajectory;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;
import frc.team5735.subsystems.ElevatorIntake;
import frc.team5735.subsystems.Wrist;
import frc.team5735.utils.units.Degrees;
import frc.team5735.utils.units.Inches;

public class Autos {
    public static AutoCommand[][] straightSwitch = {
            {
                    new AutoCommand(Drivetrain.getInstance(), new Trajectory("temp")),
                    new AutoCommand(Wrist.getInstance(), new Degrees(-40)),
                    new AutoCommand(Elevator.getInstance(), new Inches(24))
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
}
