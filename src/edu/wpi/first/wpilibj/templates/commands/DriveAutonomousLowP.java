/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David and minions
 */
public class DriveAutonomousLowP extends CommandBase {
    
    private double m_speedLeft;
    private double m_speedRight;
    private double m_timeout;
    private double m_distance;
    private boolean m_backwards = false;
    
    public DriveAutonomousLowP(double leftSpeed, double rightSpeed, double time, double distance) {
        
        super("DriveClosed(" + leftSpeed + ", " + rightSpeed + ", " + time + ", " + distance +")");

//        setInterruptible(false); // this may work, but doesn't solve our problem

        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
        requires(drive);
        if(leftSpeed + rightSpeed < 0.0) {
            m_backwards = true;
        } 
        m_speedLeft = leftSpeed;
        m_speedRight = rightSpeed;
        m_distance = Math.abs(distance);
        m_timeout = time;
    }

//    public boolean isInterruptible() {        
//        return false;
//    }
    
    // Called just before this Command runs the first time
    protected void initialize() {
        double currentDistance;
        
        drive.enableClosedLoop(-.02,0,0);
        drive.resetDistance();
        currentDistance = drive.getAverageDistance();
        if(m_backwards) {
            m_distance = currentDistance - m_distance;
        } else {
            m_distance = currentDistance + m_distance;
        }
        setTimeout(m_timeout);
        drive.driveTankClosedLoop(m_speedLeft, m_speedRight);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        System.out.println("Lspeed is " + drive.getLeftSpeed());
        System.out.println("Rspeed is " + drive.getRightSpeed());
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        boolean done = isTimedOut();

        if(!done) {
            double currentDistance = drive.getAverageDistance();
            System.out.println("L dist " + drive.getLeftDistance());
            System.out.println("R dist " + drive.getRightDistance());
            
            System.out.print("current distance =" +currentDistance);
            if(m_backwards) {
                if(currentDistance <= m_distance) {
                    done = true;
                }
                System.out.println(" m_distance = " + m_distance);
            } else {
                if(currentDistance >= m_distance) {
                    done = true;
                }
                System.out.println(" m_distance = " + m_distance);
            }
        }
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
        System.out.println("driving autonomous has ended");
        //-.8
        drive.enableClosedLoop(-.2,0,0);
        drive.driveTankClosedLoop(0.0, 0.0);
        drive.disableClosedLoop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
