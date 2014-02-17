package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David
 */
public class Arms extends Subsystem {
    
    private final Solenoid rArmSolenoid1;
    private final Solenoid rArmSolenoid2;
    private final Solenoid lArmSolenoid1;
    private final Solenoid lArmSolenoid2;
    
    public Arms() {
        rArmSolenoid1 = new Solenoid(Wiring.rArm1);
        rArmSolenoid2 = new Solenoid(Wiring.rArm2);
        lArmSolenoid1 = new Solenoid(Wiring.lArm1);
        lArmSolenoid2 = new Solenoid(Wiring.lArm2);
        ArmUp();
    }
    /**
     * Method that activates the arm solenoids so the arm moves up
     */
    public void ArmUp() {
        System.out.println("Attempting to move arm up");

        rArmSolenoid1.set(true);
        rArmSolenoid2.set(false);
        lArmSolenoid1.set(true);
        lArmSolenoid2.set(false);
           
        SmartDashboard.putBoolean("r arm solenoid1", rArmSolenoid1.get());
        SmartDashboard.putBoolean("l arm solenoid1", lArmSolenoid1.get());
    }
    
    /**
     * Method that activates the arm solenoids so the arm moves down 
     */
    public void ArmDown() {
        System.out.println("Attempting to move arm down");

        rArmSolenoid1.set(false);
        rArmSolenoid2.set(true);
        lArmSolenoid1.set(false);
        lArmSolenoid2.set(true);
        
        SmartDashboard.putBoolean("r arm solenoid1", rArmSolenoid1.get());
        SmartDashboard.putBoolean("l arm solenoid1", lArmSolenoid1.get());
    }
    
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
