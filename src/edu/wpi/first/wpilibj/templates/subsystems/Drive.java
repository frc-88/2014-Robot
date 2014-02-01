/*
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSource.PIDSourceParameter;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DrivewithController;
import edu.wpi.first.wpilibj.templates.commands.DriveWithControllerClosed;
import edu.wpi.first.wpilibj.templates.commands.CommandBase;

/**
 * @author David
 */
public class Drive extends Subsystem {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    private static final double p = .8;
    private static final double i = .05;
    private static final double d = 0;
    private static final double cycleTime = .020;
    PIDController leftPIDControl;
    PIDController rightPIDControl;
    Encoder leftPIDSource;
    Encoder rightPIDSource;
    PIDOutput leftPIDOutput;
    PIDOutput rightPIDOutput;
    
    private CANJaguar leftJag = null;
    private CANJaguar rightJag = null;
    private CANJaguar leftJag2 = null;
    private CANJaguar rightJag2 = null;
    private boolean m_fault = false;
    private static final int DRIVE_ENCODER_LINES = 250;
    private boolean m_closedLoop;
    
    byte groupLeft = 1;
    byte groupRight = 2;
    
    Solenoid m_LShifter;
    Solenoid m_RShifter;
    
    double last_speedL = 0.0;
    double last_speedR = 0.0;
    private double m_leftResetPosn = 0.0;
    private double m_rightResetPosn = 0.0;
    private static final double DISTANCE_PER_REVOLUTION = 19.25;

    private static final int lEncoderAChannel = 1;
    private static final int lEncoderBChannel = 2;
    private static final int rEncoderAChannel = 3;
    private static final int rEncoderBChannel = 4;
    
    public Drive()  {
        m_LShifter = new Solenoid(Wiring.rShifter);
        m_RShifter = new Solenoid(Wiring.lShifter);
        m_LShifter.set(true);
        m_RShifter.set(false);
        try {
                leftJag = new CANJaguar(Wiring.leftCANDrive);
                leftJag2 = new CANJaguar(Wiring.leftCANDrive2);
                leftPIDOutput = leftJag;
                System.out.println("setting up left side jags");
//                if (leftJag != null) {
//                    leftJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
//                    leftJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                    leftJag2.changeControlMode(CANJaguar.ControlMode.kSpeed);
//                    leftJag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                }
                
            }
            catch (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
        
        try {
                rightJag = new CANJaguar(Wiring.rightCANDrive);
                rightJag2 = new CANJaguar(Wiring.rightCANDrive2);
                rightPIDOutput = rightJag;
                System.out.println("setting up right side jags");
                //rightJag2.disableControl();
//                if (rightJag2 != null && rightJag != null) {
//                    rightJag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                    rightJag2.changeControlMode(CANJaguar.ControlMode.kSpeed);
//                    rightJag.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                    rightJag.changeControlMode(CANJaguar.ControlMode.kSpeed);
//                }
        }
            catch  (CANTimeoutException ex) {
                System.out.println("***CAN ERROR***");
                m_fault = true;
            }
        leftPIDSource =  new Encoder(lEncoderAChannel,lEncoderBChannel);
        rightPIDSource = new Encoder(rEncoderAChannel,rEncoderBChannel);
        leftPIDSource.setPIDSourceParameter(PIDSourceParameter.kRate);
        rightPIDSource.setPIDSourceParameter(PIDSourceParameter.kRate);
        leftPIDControl = new PIDController(p,i,d,leftPIDSource,leftJag, cycleTime);
        rightPIDControl = new PIDController(p,i,d,rightPIDSource,rightJag, cycleTime);
        
    }
    
    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        double position;
        System.out.println("Enabling closed loop control");
        
        if(rightJag2 != null && leftJag != null && rightJag != null) {
            try {
                // set the right motor to closed loop
                //may be percent v bus
                leftPIDControl.setPID(p, i, d);
                rightPIDControl.setPID(p, i, d);
                EnablePID();
                rightJag2.changeControlMode(CANJaguar.ControlMode.kVoltage);
                rightJag.changeControlMode(CANJaguar.ControlMode.kVoltage);

                // set the left motor to closed loop
                leftJag.changeControlMode(CANJaguar.ControlMode.kVoltage);
                leftJag2.changeControlMode(CANJaguar.ControlMode.kVoltage);
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
                DisablePID();
                rightJag2.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                rightJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

                // set the left motor to open loop
                leftJag.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
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
        double lSpeed = speedLeft * maxRPM;
        double rSpeed = -speedRight * maxRPM;
        leftPIDControl.setSetpoint(lSpeed);
        rightPIDControl.setSetpoint(rSpeed);
        double lJagTarget = leftPIDControl.get();
        double rJagTarget = rightPIDControl.get();
//        double RAMP_RATE = 30;
//
//        if(lSpeed - last_speedL > RAMP_RATE) {
//            lSpeed = last_speedL + RAMP_RATE;
//        } else if(lSpeed - last_speedL < -RAMP_RATE) {
//            lSpeed = last_speedL - RAMP_RATE;
//        }
//        if(rSpeed - last_speedR > RAMP_RATE) {
//            rSpeed = last_speedR + RAMP_RATE;
//        } else if(rSpeed - last_speedR < -RAMP_RATE) {
//            rSpeed = last_speedR - RAMP_RATE;
//        }
//        last_speedL = lSpeed;
//        last_speedR = rSpeed;
        
        if(leftJag != null && leftJag2 != null) {
            try {
                leftJag.setX(lJagTarget, groupLeft);
                leftJag2.setX(lJagTarget, groupLeft);
                leftJag.updateSyncGroup(groupLeft);
//                System.out.println("Commanded motor speed left: " + lSpeed);
//                System.out.println("Actual motor speed left 1: " + leftJag.getSpeed());
//                System.out.println("Actual motor speed left 2: " + leftJag2.getSpeed());
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null && rightJag2 != null) {
            try {
                rightJag.setX(rJagTarget, groupRight);
                rightJag2.setX(rJagTarget, groupRight);
                rightJag.updateSyncGroup(groupRight);
                System.out.println("Commanded motor speed right: " + rSpeed);
                System.out.println("Actual motor output right 1: " + rightJag.getOutputVoltage());
                System.out.println("Actual motor speed right 1: " + rightJag.getSpeed());
                System.out.println("Actual motor speed right 2: " + rightJag2.getSpeed());
             } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }

    public void driveTankOpenLoop(double left, double right) {

        if(leftJag != null || leftJag2 !=null) {
            try {
                System.out.println("trying to run open loop");
                leftJag.setX(left);
                leftJag2.setX(left);
                System.out.println("Commanded motor speed left: " + left);
                System.out.println("Actual motor speed left 1: " + leftJag.getSpeed());
                System.out.println("Actual motor speed left 2: " + leftJag2.getSpeed());
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
        if(rightJag != null || rightJag2 != null) {
            try {
                rightJag.setX(-right);
                rightJag2.setX(-right);
                System.out.println("Commanded motor speed right: " + -right);
                System.out.println("Actual motor speed right 1: " + rightJag.getSpeed());
                System.out.println("Actual motor speed right 2: " + rightJag2.getSpeed());
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
            }
        }
    }
    
    /**
     * Converts from gearbox shaft rotation to wheel distance traveled
     * 
     * @param revolutions output shaft rotations
     * @return distance wheel travels in inches
     */
    private double encoderToDistance(double revolutions) {
        /* convert from revolutions at geabox output shaft to distance in inches
         * ouput sproket has 18 teeth, and wheel sproket has 22 teeth
         * wheel diameter is nominal 6 inches.
         */
        return DISTANCE_PER_REVOLUTION * revolutions;
    }
    
    /**
     * The distance traveled by the left wheel since Jag powered on
     * 
     * @return Total distance traveled by right wheel in inches 
     */
    private double getLDist() {

        double position = 0.0;
        if (leftJag != null) {
            try {
                position = leftJag.getPosition();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
                SmartDashboard.putString("Drive Fault", ex.toString());
            }
        }
        return position;
    }
    
    /**
     * The distance traveled by the right wheel since Jag powered on
     * 
     * @return Total distance traveled by right wheel in inches 
     */
    public double getLeftDistance() {

        return encoderToDistance(getLDist()-m_leftResetPosn);
    }
    
    /**
     * The distance traveled by the right wheel since Jag powered on
     * 
     * @return Total distance traveled by right wheel in inches 
     */
    private double getRDist() {

        double position = 0.0;
        if (rightJag != null){
            try {
                position = -rightJag.getPosition();
            } catch(CANTimeoutException ex) {
                m_fault = true;
                System.err.println("****************CAN timeout***********");
                SmartDashboard.putString("Drive Fault", ex.toString());
            }
        }
        return position;
    }
    
    /**
     * The distance traveled by the right wheel since Jag powered on
     * 
     * @return Total distance traveled by right wheel in inches 
     */
    public double getRightDistance() {

        return encoderToDistance(getRDist()-m_rightResetPosn);
    }
    
    public double getAverageDistance() {
        System.out.println("right distane = " + getRightDistance());
        System.out.println("left distane = " + getLeftDistance());
        return (getLeftDistance() + getRightDistance()) / 2.0;
    }

    public void resetDistance() {
        m_leftResetPosn = getLDist();
        m_rightResetPosn = getRDist();
    }
    /**
     * Returns the value of the fault flag
     */
    public boolean getFault() {
        return m_fault;
    }


    public void Shift() {
        //'gearing' is set to whatever state the shifting solenoids are currently in
        boolean gearing = m_LShifter.get();

        //Diagnostic
        System.out.println("Attempting to shift: Subsystem Method.");
        System.out.println("Attempting to shift: Gearing =" + gearing);

        //Switch on 'gearing', sent to "shifter".set()
        if (gearing) {
            //high unsure
            m_LShifter.set(false);
            m_RShifter.set(true);
        } else {
            //low unsure
            m_RShifter.set(false);
            m_LShifter.set(true);
            //System.err.println("LShifter" + m_LShifter.get());
            //System.err.println("RShifter" + m_RShifter.get());
        }
        System.out.println("LShifter" + m_LShifter.get());
        System.out.println("RShifter" + m_RShifter.get());
        
    }
    public boolean getGearing() {
        return m_RShifter.get();
    }
     
    public void EnablePID(){
        leftPIDControl.enable();
        rightPIDControl.enable();
    }
    
    public void DisablePID() {
        leftPIDControl.disable();
        rightPIDControl.disable();
    }


    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new DrivewithController());
        setDefaultCommand(new DriveWithControllerClosed());
    }
}
