package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.subsystems.Drive;
/**
 *
 * @author David
 */

    /**
     * Command for driving the robot
     */
public class DrivewithControllerOpen extends CommandBase {
    
    private static int TANK = 1;
    private static int TANK_AVG = 2;
    private static int ARCADE_SINGLE = 3;
    private static int ARCADE_SPLIT = 4;
    private static double AVG_RANGE = 0.1;
    private int controllerMode;
    
    public DrivewithControllerOpen() {
        super("DriveWithController");
        requires(drive);
    }

    protected void initialize() {
        drive.disableClosedLoop();
        controllerMode = SmartDashboard.getInt("controller mode", TANK);
    }

    /**
     * Part that drives it
     */
    protected void execute() {
        double left;
        double right;
        double speed;
        double direction;
        double average;
        
        if (controllerMode==TANK) {
            // drive the robot based on driver sticks
            left = oi.getDriveLeftVerticalAxis();
            right = oi.getDriveRightVerticalAxis();
    
            drive.driveTankOpenLoop(left, right);

       } else if (controllerMode == TANK_AVG) {
            // drive the robot based on driver sticks
            left = oi.getDriveLeftVerticalAxis();
            right = oi.getDriveRightVerticalAxis();
    
            if ( Math.abs(left - right) < AVG_RANGE) {
                average = left + right / 2.0;
                drive.driveTankOpenLoop(average, average);
            }
            else {
                  drive.driveTankOpenLoop(left, right);
            }

       } else if (controllerMode == ARCADE_SINGLE) {
           speed = oi.getDriveLeftVerticalAxis();
           direction = oi.getDriveLeftHorizontalAxis();
           
           arcade(speed, direction);

       } else if (controllerMode == ARCADE_SPLIT) {
           speed = oi.getDriveLeftVerticalAxis();
           direction = oi.getDriveRightHorizontalAxis();
           
           arcade(speed, direction);
       }
    }

    private void arcade (double speed, double direction) {
        double left;
        double right;
        
       // set left and right to speed adjusted for direction
       left = (2.0 * speed + direction) / 3.0;
       right = (2.0 * speed - direction) / 3.0;
       
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
