package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.JagPair;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DriveWithControllerClosed;
import edu.wpi.first.wpilibj.templates.commands.DriveWithControllerSimple;
import edu.wpi.first.wpilibj.templates.commands.DrivewithControllerOpen;

/**
 * @author TJ^2 Programming Team
 */
public class Drive extends Subsystem {
    Solenoid m_LowShifter;
    Solenoid m_HighShifter;
    private JagPair lPair;
    private JagPair rPair;    
    
    public Drive() {
        lPair = new JagPair("left", Wiring.leftDrive, Wiring.leftDrive2,
                Wiring.lEncoderAChannel, Wiring.lEncoderBChannel);
        rPair = new JagPair("right", Wiring.rightDrive, Wiring.rightDrive2,
                Wiring.rEncoderAChannel, Wiring.rEncoderBChannel);

        m_LowShifter = new Solenoid(Wiring.highShifter);
        m_HighShifter = new Solenoid(Wiring.lowShifter);

        m_LowShifter.set(true);
        m_HighShifter.set(false);        
    }

    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        System.out.println("Enabling closed loop control");

        lPair.enableClosedLoop();
        rPair.enableClosedLoop();
    }

    /**
     * Disables the Drive closed loop and puts it into open loop.
     */
    public void disableClosedLoop() {
        System.out.println("Disabling closed loop control");
       
        lPair.disableClosedLoop();
        rPair.disableClosedLoop();
    }

    /**
     * Drives the robot in closed loop
     *
     * @param speedLeft joystick value passed in
     * @param speedRight joystick value passed in
     */
    public void driveTankClosedLoop(double speedLeft, double speedRight) {
        lPair.setSpeed(-speedLeft, getGearing());
        rPair.setSpeed(speedRight, getGearing());
    }
    
    public void driveTankOpenLoop(double left, double right) {
        SmartDashboard.putNumber("left speed requested ", -left);
        SmartDashboard.putNumber("right speed requested ", right);
        
        lPair.setX(-left);
        rPair.setX(right);
    }

    /**
     * @returns speed of left wheel
     */
    public double getLeftSpeed() {
        return lPair.getSpeed();
    }

    /**
     * @returns speed of right wheel
     */
    public double getRightSpeed() {
        return rPair.getSpeed();
    }

    /**
     * The distance traveled by the left wheel since reset
     *
     * @return Total distance traveled by left wheel in inches
     */
    public double getLeftDistance() {
        return lPair.getDistance();
    }

    /**
     * The distance traveled by the right wheel since reset
     *
     * @return Total distance traveled by right wheel in inches
     */
    public double getRightDistance() {
        return rPair.getDistance();
    }

    /**
     * The average distance traveled by the two wheels since reset
     *
     * @return Total distance traveled by wheels in inches
     */
    public double getAverageDistance() {
        return (getLeftDistance() + getRightDistance()) / 2.0;
    }

    /**
     * Reset the encoders to 0
     */
    public void resetDistance() {
        lPair.resetDistance();
        rPair.resetDistance();
    }

    /**
     * Returns the value of the fault flag
     */
    public boolean getFault() {
        return (lPair.getFault() || rPair.getFault());
    }
    
    public void Shift() {
        //'gearing' is set to whatever state the shifting solenoids are currently in
        boolean gearing = m_LowShifter.get();

        //Diagnostic
        System.out.println("Attempting to shift: Subsystem Method.");
        System.out.println("Attempting to shift: Gearing =" + gearing);

        //Switch on 'gearing', sent to "shifter".set()
        if (gearing) {
            //high
            m_LowShifter.set(false);
            m_HighShifter.set(true);
        } else {
            //low
            m_HighShifter.set(false);
            m_LowShifter.set(true);
        }
        System.out.println("LShifter" + m_LowShifter.get());
        System.out.println("RShifter" + m_HighShifter.get());
        
    }

    public boolean getGearing() {
        return m_HighShifter.get();
    }
    
    public void initDefaultCommand() {
        setDefaultCommand(new DriveWithControllerSimple());
        //setDefaultCommand(new DrivewithControllerOpen());
        //setDefaultCommand(new DriveWithControllerClosed());
    }
}
