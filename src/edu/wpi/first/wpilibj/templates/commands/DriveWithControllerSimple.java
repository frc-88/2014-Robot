/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author TJ2
 */
public class DriveWithControllerSimple extends CommandBase {
    
    double left;
    double right; 
    
    public DriveWithControllerSimple() {
        
        
        super("DriveWithControllerSimple");
        requires(drive);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
         left = oi.getDriveLeftVerticalAxis();
            right = oi.getDriveRightVerticalAxis();
    
            drive.driveTankOpenLoop(left, right);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
