package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David
 */
public class Kicker extends Subsystem {

    //Have to wait as we may be changing what we use
    DigitalInput lightSensor1;
    Jaguar kickerJag;

    public Kicker() {
        lightSensor1 = new DigitalInput(Wiring.lightSensor);
        kickerJag = new Jaguar(Wiring.kickerJag);
    }

    public boolean ReturnLightSensorValue() {
        SmartDashboard.putBoolean("light sensor choo choo ", lightSensor1.get());
        return lightSensor1.get();
    }

    public void KickerOpenLoop(double value) {
        kickerJag.set(value);
        SmartDashboard.putDouble("kicker power", value);
    }

    public void initDefaultCommand() {
    }
}
