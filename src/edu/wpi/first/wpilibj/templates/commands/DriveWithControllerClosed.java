/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Drive;
/**
 *
 * @author David
 */

/**
 * Command for driving the robot
 */
public class DriveWithControllerClosed extends CommandBase {
    
    private static final double RAMP_RATE = 0.1;
    private double last_left = 0.0;
    private double last_right = 0.0;
    
    public DriveWithControllerClosed() {
        super("DriveWithControllerClosed");
        requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        drive.enableClosedLoop();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

        
        // grab the updated motor commands from controller
        double left = oi.getDriveLeftVerticalAxis();
        double right = oi.getDriveRightVerticalAxis();
        
        // limit the change in setpoint between calls
        if(left - last_left > RAMP_RATE) {
            left = last_left + RAMP_RATE;
        } else if(left - last_left < -RAMP_RATE) {
            left = last_left - RAMP_RATE;
        }
        if(right - last_right > RAMP_RATE) {
            right = last_right + RAMP_RATE;
        } else if(right - last_right < -RAMP_RATE) {
            right = last_right - RAMP_RATE;
        }
        last_left = left;
        last_right = right;

        drive.driveTankClosedLoop(left, right);
        
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
