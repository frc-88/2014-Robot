/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David
 */
public class ArmFeeder extends Subsystem {
    
    Solenoid lArmSolenoid1;
    Solenoid rArmSolenoid1;
    Solenoid lArmSolenoid2;
    Solenoid rArmSolenoid2;
    
    public ArmFeeder() {
        lArmSolenoid1 = new Solenoid(Wiring.lArm1);
        rArmSolenoid1 = new Solenoid(Wiring.rArm1);
        lArmSolenoid2 = new Solenoid(Wiring.lArm2);
        rArmSolenoid2 = new Solenoid(Wiring.rArm2);
        rArmSolenoid1.set(false);
        rArmSolenoid2.set(true);
        lArmSolenoid1.set(false);
        lArmSolenoid2.set(true);
    }
    
    public void ArmUp() {
        //Diagnostic
        System.out.println("Attempting to move arm up");
        //these may need to be flipped
        rArmSolenoid1.set(false);
        lArmSolenoid1.set(false);
        rArmSolenoid2.set(true);
        lArmSolenoid2.set(true);
        
        SmartDashboard.putBoolean("l arm solenoid", lArmSolenoid1.get());
        SmartDashboard.putBoolean("r arm solenoid", rArmSolenoid1.get());
        
    }
    
    public void ArmDown() {
        //Diagnostic
        System.out.println("Attempting to move arm down");
        //these may need to be flipped
        lArmSolenoid1.set(true);
        rArmSolenoid1.set(true);
        rArmSolenoid2.set(false);
        lArmSolenoid2.set(false);        
        SmartDashboard.putBoolean("l arm solenoid", lArmSolenoid1.get());
        SmartDashboard.putBoolean("r arm solenoid", rArmSolenoid1.get());
        
    }
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
