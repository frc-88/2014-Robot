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
    }

    protected void initialize() {
    }

    protected void execute() {
        left = oi.getDriveLeftVerticalAxis();
        right = oi.getDriveRightVerticalAxis();

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
