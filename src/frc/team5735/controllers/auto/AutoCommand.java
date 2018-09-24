package frc.team5735.controllers.auto;

import frc.team5735.subsystems.Subsystem;

public class AutoCommand {
    private Subsystem subsystem;
    private Object value;

    public AutoCommand(Subsystem subsystem, Object value) {
        this.subsystem = subsystem;
        this.value = value;
    }

    public Subsystem getSubsystem() {
        return subsystem;
    }

    public Object getValue() {
        return value;
    }
}
