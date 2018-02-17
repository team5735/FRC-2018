package frc.team5735.controllers.auto.AutoCommands;

import frc.team5735.controllers.auto.AutoCommand;
import frc.team5735.subsystems.Drivetrain;
import frc.team5735.subsystems.Elevator;
import frc.team5735.utils.units.Inches;

public class ElevatorCommand implements AutoCommand{

    Elevator elevator;
    public Inches height;

    public ElevatorCommand(double height) {
        this.elevator = Elevator.getInstance();
        this.height = new Inches(height);
    }

    @Override
    public void runInit() {
        elevator.runInit();
    }

    @Override
    public boolean runPeriodic() {
        if (elevator.getState() == Elevator.ElevatorState.POSITION_HOLDING) {
            return true;
        }
        elevator.setTargetHeight(height);
        elevator.runPeriodic();
        return false;
    }
}
