/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David
 */
public class ArmFeeder extends Subsystem {
    
    Solenoid lArmSolenoid;
    Solenoid rArmSolenoid;
    
    public ArmFeeder() {
        lArmSolenoid = new Solenoid(Wiring.lArm);
        rArmSolenoid = new Solenoid(Wiring.rArm);
        rArmSolenoid.set(false);
        lArmSolenoid.set(true);
    }
    public void ArmUp() {
        //Diagnostic
        System.out.println("Attempting to move arm up");
        //these may need to be flipped
        lArmSolenoid.set(false);
        rArmSolenoid.set(true);
        System.out.println("Larm" + lArmSolenoid.get());
        System.out.println("Rarm" +rArmSolenoid.get());
    }
    
    public void ArmDown() {
        //Diagnostic
        System.out.println("Attempting to move arm down");
        //these may need to be flipped
        lArmSolenoid.set(true);
        rArmSolenoid.set(false);
        System.out.println("Larm" + lArmSolenoid.get());
        System.out.println("Rarm" +rArmSolenoid.get());
    }
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
