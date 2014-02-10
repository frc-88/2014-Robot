/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Drive;

/**
 *
 * @author Chris
 */
public class ShiftGearBox extends CommandBase {

    /**
     *
     */
    
    boolean done = false;
    
    public ShiftGearBox() {
        super("ShiftGearBox");
        requires(drive);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        //diagnostic, + runs shifter.shift
        //System.out.println("Attempting to shift: Command running.");
        //shifter.Shift();
        done=false;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        //makes sure you are moving in order to shift
        //is checking for done needed? - David
        if ((!done) && (Math.abs(drive.getLeftSpeed()) > .3) && (Math.abs(drive.getRightSpeed()) > .3)) {
            drive.Shift();
            done = true;
        }
        //doesnt shift because you are moving too slow or not moving at all
        else if ((!done) && (Math.abs(drive.getLeftSpeed()) < .3) && (Math.abs(drive.getRightSpeed()) < .3)){
            done = true;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
