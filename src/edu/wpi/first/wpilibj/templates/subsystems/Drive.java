/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DrivewithController;
import edu.wpi.first.wpilibj.templates.commands.DriveWithControllerClosed;
import edu.wpi.first.wpilibj.templates.subsystems.*;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 * @author David
 */
public class Drive extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private CANJaguar leftJag = null;
    private CANJaguar rightJag = null;
    private CANJaguar leftJag2 = null;
    private CANJaguar rightJag2 = null;
    private boolean m_fault = false;
    private static final int DRIVE_ENCODER_LINES = 250;
    private boolean m_closedLoop;
    
    Solenoid m_LShifter;
    Solenoid m_RShifter;
    
    public Drive()  {
        
        m_LShifter = new Solenoid(Wiring.rShifter);
        m_RShifter = new Solenoid(Wiring.lShifter);
        m_LShifter.set(false);
        m_RShifter.set(false);
        try {

                leftJag = new CANJaguar(Wiring.leftCANDrive2);
                leftJag2 = new CANJaguar(Wiring.leftCANDrive);
                //leftJag.disableControl();
                if (leftJag != null) {
                    leftJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
                    leftJag.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                    leftJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    leftJag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    leftJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                    leftJag2.changeControlMode(CANJaguar.ControlMode.kSpeed);
                    leftJag2.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                    leftJag2.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    leftJag2.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    leftJag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                    
                }
                
            }
            catch (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
        
        try {
                rightJag = new CANJaguar(Wiring.rightCANDrive);
                rightJag2 = new CANJaguar(Wiring.rightCANDrive2);
                //rightJag2.disableControl();
                if (rightJag2 != null && rightJag != null) {
                    rightJag2.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                    rightJag2.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    rightJag2.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    rightJag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                    rightJag2.changeControlMode(CANJaguar.ControlMode.kSpeed);
                    rightJag.configEncoderCodesPerRev(DRIVE_ENCODER_LINES);
                    rightJag.setPositionReference(CANJaguar.PositionReference.kQuadEncoder);
                    rightJag.setSpeedReference(CANJaguar.SpeedReference.kQuadEncoder);
                    rightJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
                    rightJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
                }
        }
            catch  (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
    }
    
    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        //double position;
        System.out.println("Enabling closed loop control");
        
        if(rightJag2 != null && leftJag != null && rightJag != null) {
            try {
                // set the right motor to closed loop
                rightJag2.changeControlMode(CANJaguar.ControlMode.kSpeed);
                rightJag2.setPID(-.5, -0.01, 0.0);
                rightJag2.enableControl();
                rightJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
                rightJag.setPID(-.5, -0.01, 0.0);
                rightJag.enableControl();

                // set the left motor to closed loop
                leftJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
                leftJag.setPID(-.5, -0.01, 0.0);
                leftJag.enableControl();
                leftJag2.changeControlMode(CANJaguar.ControlMode.kSpeed);
                leftJag2.setPID(-.5, -0.01, 0.0);
                leftJag2.enableControl();

                // set the enable flag
                m_closedLoop = true;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    
     /**
     * Disables the Drive closed loop and puts it into open loop.
     */
    public void disableClosedLoop() {
        if(rightJag2 != null && leftJag != null) {
            try {
                // set the right motor to open loop
                rightJag2.disableControl();
                rightJag2.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                rightJag.disableControl();
                rightJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

                // set the left motor to open loop
                leftJag.disableControl();
                leftJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                leftJag2.disableControl();
                leftJag2.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

                // set the enable flag to false
                m_closedLoop = false;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                System.err.println("CAN timeout");
            }
        }
    }
    
    /**
     * Returns whether or not Drive is in ClosedLoop. If it is it will return true and if it is not
     * it will return false.
     */
    public boolean isClosedLoop() {
        return m_closedLoop;
    }
    
    public void driveTankClosedLoop(double speedLeft, double speedRight) {


        double maxRPMHighGear = 400;
        double maxRPMLowGear = 160;
        double maxRPM = 0;
        if (getGearing()) {
            maxRPM = maxRPMHighGear;
        }
        else {
            maxRPM = maxRPMLowGear;
        }
        
        if(leftJag != null) {
            try {
                leftJag.setX(speedLeft * maxRPM);
                leftJag2.setX(speedLeft * maxRPM);
                System.out.println("Commanded motor speed left: " + speedLeft * maxRPM);
                System.out.println("Actual motor speed left: " + leftJag.getSpeed());
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null && rightJag2 != null) {
            try {
                rightJag.setX(-speedRight * maxRPM);
                rightJag2.setX(-speedRight * maxRPM);
                System.out.println("Commanded motor speed right: " + -speedRight * maxRPM);
                System.out.println("Actual motor speed right: " + rightJag.getSpeed());
             } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }

    public void driveTankOpenLoop(double left, double right) {

        if(leftJag != null || leftJag2 !=null) {
            try {
                leftJag.setX(left);
                leftJag2.setX(left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null || rightJag2 != null) {
            try {
                rightJag.setX(-right);
                rightJag2.setX(-right);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
    
    /**
     * Returns the value of the fault flag
     */
    public boolean getFault() {
        return m_fault;
    }


    public void Shift() {
        //'gearing' is set to whatever state the shifting solenoids are currently in
        boolean gearing = false;
        gearing = m_LShifter.get();

        //Diagnostic
        System.out.println("Attempting to shift: Subsystem Method.");

        System.out.println("Attempting to shift: Gearing =" + gearing);

        //Switch on 'gearing', sent to "shifter".set()
        if (gearing == true) {
            m_LShifter.set(false);
            m_RShifter.set(true);
        }
        if (gearing == false) {
            m_RShifter.set(false);
            m_LShifter.set(true);
            System.err.println("LShifter" + m_LShifter.get());
            System.err.println("RShifter" + m_RShifter.get());
        }
    }
    public boolean getGearing() {
        return m_RShifter.get();
    }


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveWithControllerClosed());
    }
}
