/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

//import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Jaguar;
//import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David
 */
public class Kicker extends Subsystem {
    
    DigitalInput lightSensor1;
    Jaguar kickerJag;
    
    public Kicker() {
      lightSensor1 = new DigitalInput(Wiring.lightSensor);
      kickerJag = new Jaguar(Wiring.kickerJag);
    }
    
    /**
     * Method for returning the value of the light sensor
     * @return lightsensor value
     */
    public boolean ReturnLightSensorValue() {
        SmartDashboard.putBoolean("light sensor choo choo ", lightSensor1.get()); 
        return lightSensor1.get();
    }
    
    /**
     * Method that gives the kicker a value for power
     * @param value within 1 and -1
     */
    public void KickerOpenLoop(double value){
            kickerJag.set(value);
            SmartDashboard.putDouble("kicker power", value);
    }

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
