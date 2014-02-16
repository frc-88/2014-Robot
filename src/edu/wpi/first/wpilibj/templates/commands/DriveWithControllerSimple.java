package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author TJ2
 */
public class DriveWithControllerSimple extends CommandBase {

    public DriveWithControllerSimple() {
        super("DriveWithControllerSimple");
        requires(drive);
    }

    protected void initialize() {
        drive.disableClosedLoop();
    }

    protected void execute() {
        double left = oi.getDriveLeftVerticalAxis();
        double right = oi.getDriveRightVerticalAxis();

        drive.driveTankOpenLoop(left, right);
    }

    protected boolean isFinished() {
        return false;
    }

    protected void end() {
    }

    protected void interrupted() {
    }
}
