package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.RollersMove;

/**
 *
 * @author David
 */
public class Rollers extends Subsystem {

    Jaguar rollerJag;

    public Rollers() {
        rollerJag = new Jaguar(Wiring.rollerJag);
    }

    public void RollerPower(double power) {
        rollerJag.set(power);
        SmartDashboard.putDouble("Roller power ", power);
    }

    public void initDefaultCommand() {
        setDefaultCommand(new RollersMove());
    }
}
