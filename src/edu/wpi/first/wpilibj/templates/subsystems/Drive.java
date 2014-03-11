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
//    private static final double leftP = 0.05;
//    private static final double leftI = 0.0;
//    private static final double leftD = 0.0;
//    private static final double rightP = 0.05;
//    private static final double rightI = 0.0;
//    private static final double rightD = 0.0;

    private Solenoid m_LowShifter;
    private Solenoid m_HighShifter;
    private static final double lowGearScaleFactor = 1;
    private static final double highGearScaleFactor = .75;
    private JagPair lPair;
    private JagPair rPair;    
    
    private double scaleFactor = lowGearScaleFactor;
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
     * Enables Closed Loop control Driving.
     */
    public void enableClosedLoop() {
        lPair.enableClosedLoop();
        rPair.enableClosedLoop();
//        lPair.enableClosedLoop(leftP, leftI, leftD);
//        rPair.enableClosedLoop(rightP, rightI, rightD);
    }

    /**
     * Disables the Drive closed loop and puts it into open loop.
     */
    public void disableClosedLoop() {
        lPair.disableClosedLoop();
        rPair.disableClosedLoop();
    }

    /**
     * Drives in closed loop
     *
     * @param speedLeft joystick value passed in
     * @param speedRight joystick value passed in
     */
    public void driveTankClosedLoop(double speedLeft, double speedRight) {
        lPair.setSpeed(speedLeft, getGearing());
        rPair.setSpeed(-speedRight, getGearing());
    }
    
    /**
     * Drive in open loop
     * 
     * @param left joystick value passed in
     * @param right joystick value passed in
     */
    public void driveTankOpenLoop(double left, double right) {        
        lPair.setX(left*scaleFactor);
        rPair.setX(-right*scaleFactor);
        System.out.println("LPair speed " + lPair.getSpeed());
        System.out.println("RPair speed " + rPair.getSpeed());        
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
     * Returns true if either JagPair has a fault
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
            setScaleFactor(highGearScaleFactor);
        } else {
            //low
            m_HighShifter.set(false);
            m_LowShifter.set(true);
            setScaleFactor(lowGearScaleFactor);
        }
        System.out.println("LShifter" + m_LowShifter.get());
        System.out.println("RShifter" + m_HighShifter.get());
        
    }

    public boolean getGearing() {
        return m_HighShifter.get();
    }
    
    public void setScaleFactor(double value) {
        scaleFactor = value;
    }
    
    public void initDefaultCommand() {
        //setDefaultCommand(new DriveWithControllerSimple());
        setDefaultCommand(new DrivewithControllerOpen());
        //setDefaultCommand(new DriveWithControllerClosed());
    }
}
