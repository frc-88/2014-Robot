/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


/**
 *
 * @author David
 */
public class JagPair implements PIDOutput {

    byte jagGroup = 1;
    private CANJaguar jag1, jag2;
    private Encoder encoder;
    private PIDController controller;
    private static final double p = .05;
    private static final double i = 0.0;
    private static final double d = 0.0;
    private static final double f = 0.2;
    private static final double cycleTime = .020;
    private static final int DRIVE_ENCODER_LINES = 250;
    //feet per revolution over encoder lines
    private static final double DISTANCE_PER_REVOLUTION = 1.57/DRIVE_ENCODER_LINES;
    private boolean m_closedLoop;
    private boolean m_fault;
    
    public JagPair(int jag1In, int jag2In, int encoderA, int encoderB) {
        try {
                jag1 = new CANJaguar(jag1In);
                jag2 = new CANJaguar(jag2In);
                
//                if (jag1 != null) {
//                    jag1.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                    jag2.configNeutralMode(CANJaguar.NeutralMode.kCoast);
//                }
                
            }
            catch (CANTimeoutException ex) {
                SmartDashboard.putBoolean("CAN ERROR drive ", m_fault);
                m_fault = true;
            }
               
        encoder =  new Encoder(encoderA,encoderB);
        encoder.setDistancePerPulse(DISTANCE_PER_REVOLUTION);
        encoder.setSamplesToAverage(6);
        encoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        encoder.reset();
        encoder.start();
        
        controller = new PIDController(p,i,d,f,encoder,this, cycleTime);
        controller.enable();

    }
        /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        double position;
        System.out.println("Enabling closed loop control");
        
        if(jag1 != null && jag2 != null) {
            try {
                // set the motors to closed loop
                //may be percent v bus
                controller.setPID(p, i, d, f);
                jag1.changeControlMode(CANJaguar.ControlMode.kVoltage);
                jag2.changeControlMode(CANJaguar.ControlMode.kVoltage);

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
        if(jag1 != null && jag2 != null) {
            try {
                // set the motors to open loop
                jag1.changeControlMode(CANJaguar.ControlMode.kPercentVbus);
                jag2.changeControlMode(CANJaguar.ControlMode.kPercentVbus);

                // set the enable flag to false
                m_closedLoop = false;
            } catch (CANTimeoutException ex) {
                m_fault = true;
                SmartDashboard.putBoolean("CAN ERROR drive", m_fault);
            }
        }
    }

        /**
     * Returns whether or not jagPair is in ClosedLoop. If it is it will return true and if it is not
     * it will return false.
     */
    public boolean isClosedLoop() {
        return m_closedLoop;
    }
        /**
     * Returns the value of the fault flag
     */
    public boolean getFault() {
        return m_fault;
    }

    public void setX(double x) {
        if (jag1 != null || jag2 != null) {
            try {
                jag1.setX(x,jagGroup);
                jag2.setX(x,jagGroup);
                CANJaguar.updateSyncGroup(jagGroup);
            } catch (CANTimeoutException ex) {
                m_fault = true;
                SmartDashboard.putBoolean("CAN ERROR drive", m_fault);
            }
        }

    }

    /**
     * Drives the robot in closed loop
     *
     * @param speed usually joystick value passed in
     */
    public void setSpeed(double speedIn, boolean isHighGear) {

        double maxSpeedHighGear = 12.4; // feet per second
        double maxSpeedLowGear = 5.2;   // feet per second
        double maxSpeed = 0;
       
        if (isHighGear) {
            maxSpeed = maxSpeedHighGear;
        } else {
            maxSpeed = maxSpeedLowGear;
        }

        double speed = speedIn * maxSpeed;

        //double speedError = (encoder.getRate() - speed);
        //     if (controller.onTarget()) {
        controller.setSetpoint(speed);

//  TODO Needs to be refactored for JagPair
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
    
    public void updateSmartDashboard(String name) {
        SmartDashboard.putNumber(name + " speed actual ", encoder.getRate());

        try {
            SmartDashboard.putNumber(name + " jag1 current", jag1.getOutputCurrent());
            SmartDashboard.putNumber(name + " jag2 current", jag2.getOutputCurrent());

        } catch (CANTimeoutException ex) {
            m_fault = true;
            SmartDashboard.putBoolean(name + " CAN ERROR drive", m_fault);

        }
    }
        /**
     * @returns speed of wheels 
     */
    public double getSpeed() {
        return encoder.getRate();
    }

    public double getDistance(){
        return encoder.getDistance();
    } 
    
     /**
    * Reset the encoders to 0
    */
    public void resetDistance() {
        encoder.reset();
           }
    
     public void enablePID(){
        controller.enable();
           }
     
      public void disablePID() {
        controller.disable();
            }
     
    
    
    public void pidWrite(double output) {
        jag1.pidWrite(output);
        jag2.pidWrite(output);
        
    }
    
}
