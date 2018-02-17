package frc.team5735.controllers.auto;

import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;

public interface AutoCommand {
    void runInit();
    boolean runPeriodic();
}
