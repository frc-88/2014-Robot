/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

//import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Jaguar;
//import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David
 */
public class Rollers extends Subsystem {
    
    Jaguar rollerJag;
    public static double ROLLER_IN_POWER = 0.6;
    public static double ROLLER_OUT_POWER = -1.0;
    public static double ROLLER_STOP_POWER = 0.0;
    
    public Rollers() {
//        try {
            rollerJag = new Jaguar(Wiring.rollerJag);
  //      }catch (CANTimeoutException ex) {
        //        SmartDashboard.putBoolean("CAN ERROR roller", true);
        //}
    }
    public void RollerPower(double power) {
        //try {
            rollerJag.set(power);
        //}catch(CANTimeoutException ex) {
                SmartDashboard.putDouble("Roller power ", power);
        //}
    }
   
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
