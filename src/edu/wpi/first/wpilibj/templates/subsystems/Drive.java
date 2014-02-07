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
import edu.wpi.first.wpilibj.templates.JagPair;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.commands.DrivewithControllerOpen;
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
    
    private CANJaguar leftJag1;
    private CANJaguar leftJag2;
    private CANJaguar rightJag1;
    private CANJaguar rightJag2;
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
    //feet per revolution over encoder lines
    private static final double DISTANCE_PER_REVOLUTION = 1.57/DRIVE_ENCODER_LINES;

    private static final int lEncoderAChannel = 4;
    private static final int lEncoderBChannel = 5;
    private static final int rEncoderAChannel = 6;
    private static final int rEncoderBChannel = 7;
    
    public Drive()  {
        m_LShifter = new Solenoid(Wiring.rShifter);
        m_RShifter = new Solenoid(Wiring.lShifter);
        m_LShifter.set(true);
        m_RShifter.set(false);
        try {
                leftJag1 = new CANJaguar(Wiring.leftCANDrive);
                leftJag2 = new CANJaguar(Wiring.leftCANDrive2);
                System.out.println("setting up left side jags");
//                if (leftJag1 != null) {
//                    leftJag1.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                    leftJag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                }
                
            }
            catch (CANTimeoutException ex) {
                m_fault = true;
                SmartDashboard.putBoolean("CAN ERROR drive ", m_fault);
            }
        
        try {
                rightJag1 = new CANJaguar(Wiring.rightCANDrive);
                rightJag2 = new CANJaguar(Wiring.rightCANDrive2);
                System.out.println("setting up right side jags");
                //rightJag2.disableControl();
//                if (rightJag2 != null && rightJag1 != null) {
//                    rightJag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                    rightJag1.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                }
        }catch  (CANTimeoutException ex) {
                m_fault = true;
                SmartDashboard.putBoolean("CAN ERROR drive ", m_fault);
        }
        JagPair lPair = new JagPair(leftJag1, leftJag2);
        JagPair rPair = new JagPair(rightJag1, rightJag2);
        leftPIDSource =  new Encoder(lEncoderAChannel,lEncoderBChannel);
        rightPIDSource = new Encoder(rEncoderAChannel,rEncoderBChannel);
        leftPIDSource.setDistancePerPulse(DISTANCE_PER_REVOLUTION);
        rightPIDSource.setDistancePerPulse(DISTANCE_PER_REVOLUTION);
        rightPIDSource.setSamplesToAverage(6);
        leftPIDSource.setSamplesToAverage(6);
        leftPIDSource.setPIDSourceParameter(PIDSourceParameter.kRate);
        rightPIDSource.setPIDSourceParameter(PIDSourceParameter.kRate);
        leftPIDControl = new PIDController(p,i,d,leftPIDSource,lPair, cycleTime);
        rightPIDControl = new PIDController(p,i,d,rightPIDSource,rPair, cycleTime);
        leftPIDSource.reset();
        rightPIDSource.reset();
        rightPIDSource.start();
        leftPIDSource.start();
        leftPIDControl.enable();
        rightPIDControl.enable();
    }
    
    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        double position;
        System.out.println("Enabling closed loop control");
        
        if(rightJag2 != null && leftJag1 != null && rightJag1 != null) {
            try {
                // set the right motor to closed loop
                //may be percent v bus
                leftPIDControl.setPID(p, i, d);
                rightPIDControl.setPID(p, i, d);
                EnablePID();
                rightJag2.changeControlMode(CANJaguar.ControlMode.kVoltage);
                rightJag1.changeControlMode(CANJaguar.ControlMode.kVoltage);

                // set the left motor to closed loop
                leftJag1.changeControlMode(CANJaguar.ControlMode.kVoltage);
                leftJag2.changeControlMode(CANJaguar.ControlMode.kVoltage);
                // set the enable flag
                m_closedLoop = true;
            } catch (CANTimeoutException ex) {
                SmartDashboard.putBoolean("CAN ERROR drive ", m_fault);
                m_fault = true;
            }
        }
    }
    
     /**
     * Disables the Drive closed loop and puts it into open loop.
     */
    public void disableClosedLoop() {
        if(rightJag2 != null && leftJag1 != null) {
            try {
                // set the right motor to open loop
                DisablePID();
                rightJag2.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                rightJag1.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

                // set the left motor to open loop
                leftJag1.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                leftJag2.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

                // set the enable flag to false
                m_closedLoop = false;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                SmartDashboard.putBoolean("CAN ERROR drive", m_fault);
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
    /**
     * Drives the robot in closed loop
     * @param speedLeft joystick value passed in
     * @param speedRight joystick value passed in
     */
    public void driveTankClosedLoop(double speedLeft, double speedRight) {
        
        double maxSpeedHighGear = 10.4; // feet per second
        double maxSpeedLowGear = 4.2;   // feet per second

        double maxSpeed = 0;
        if (getGearing()) {
            maxSpeed = maxSpeedHighGear;
        }
        else {
            maxSpeed = maxSpeedLowGear;
        }
        
        double lSpeed = speedLeft * maxSpeed;
        double rSpeed = -speedRight * maxSpeed;

        //double lSpeedError = (leftPIDSource.getRate() - lSpeed);
        //double rSpeedError = (rightPIDSource.getRate() - rSpeed);

   //     if (leftPIDControl.onTarget() && rightPIDControl.onTarget()) {
        leftPIDControl.setSetpoint(lSpeed);
        rightPIDControl.setSetpoint(rSpeed);
        SmartDashboard.putDouble("encoder value left ", leftPIDSource.getRate());
        SmartDashboard.putDouble("encoder value right ", rightPIDSource.getRate());
//        } else {
            
  //      }

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
   
    }

    public void driveTankOpenLoop(double left, double right) {

        if(leftJag1 != null || leftJag2 !=null) {
            try {
                System.out.println("trying to run open loop");
                leftJag1.setX(left);
                leftJag2.setX(left);
                System.out.println("Commanded motor speed left: " + left);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                SmartDashboard.putBoolean("CAN ERROR drive", m_fault);
                }
        }
        if(rightJag1 != null || rightJag2 != null) {
            try {
                rightJag1.setX(-right);
                rightJag2.setX(-right);
                System.out.println("Commanded motor speed right: " + -right);
            } catch(CANTimeoutException ex) {
                m_fault = true;
                SmartDashboard.putBoolean("CAN ERROR drive", m_fault);
                }
        }
    }
    /**
     * @returns speed of left wheel 
     */
    public double getLeftSpeed() {
        return leftPIDSource.getRate();
    }
    
    /**
     * @returns speed of right wheel 
     */
    public double getRightSpeed() {
        return rightPIDSource.getRate();
    }
    /**
     * The distance traveled by the left wheel since reset
     * 
     * @return Total distance traveled by left wheel in inches 
     */
    public double getLeftDistance() {
        return leftPIDSource.getDistance();
    }
    
   /**
    * The distance traveled by the right wheel since reset
    * 
    * @return Total distance traveled by right wheel in inches 
    */
    public double getRightDistance() {
        return rightPIDSource.getDistance();
    }
    
   /**
    * The average distance traveled by the two wheels since reset
    * 
    * @return Total distance traveled by wheels in inches 
    */
    public double getAverageDistance() {
        return (getLeftDistance() + getRightDistance())/2.0;
    }

   /**
    * Reset the encoders to 0
    */
    public void resetDistance() {
        leftPIDSource.reset();
        rightPIDSource.reset();
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
        //setDefaultCommand(new DrivewithControllerOpen());
        setDefaultCommand(new DriveWithControllerClosed());
    }
}
