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
