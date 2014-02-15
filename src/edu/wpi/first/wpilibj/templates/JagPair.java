package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author TJ^2 Programming Team
 */
public class JagPair implements PIDOutput {

    private static final double pDefault = .05;
    private static final double iDefault = 0.0;
    private static final double dDefault = 0.0;
    private static final double f = 0.2;
    private static final double cycleTime = .020;
    private static byte jagGroup = 1;
    private static final int DRIVE_ENCODER_LINES = 250;
    //feet per revolution over encoder lines
    private static final double DISTANCE_PER_PULSE = 1.57 / 250;
    //private static final double RAMP_RATE = 30;
    
    private Jaguar jag1, jag2;
    private Encoder encoder;
    private PIDController controller;
    private String name;
    private boolean m_closedLoop = false;
    private boolean m_fault = false;
    // private double last_speed = 0.0;

    public JagPair(String nameIn, int jag1In, int jag2In, int encoderA, int encoderB) {
        name = nameIn;
        
        jag1 = new Jaguar(jag1In);
        jag2 = new Jaguar(jag2In);
        
        encoder = new Encoder(encoderA, encoderB);
        encoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        encoder.setSamplesToAverage(6);
        encoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        encoder.reset();
        encoder.start();
    }

    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        enableClosedLoop(pDefault, iDefault, dDefault);
    }
    
    public void enableClosedLoop(double p, double i, double d) {
        if (m_closedLoop) {
            System.out.println(name + " closed loop already enabled!");
        } else {
            // set the motors to closed loop
            //may be percent v bus
            controller = new PIDController(p, i, d, f, encoder, this, cycleTime);
            // set the enable flag
            m_closedLoop = true;
            controller.enable();
            System.out.println(name + " closed loop enabled.");
        }
    }

    /**
     * Disables the Drive closed loop and puts it into open loop.
     */
    public void disableClosedLoop() {
        if (!m_closedLoop) {
            System.out.println(name + " closed loop already disabled!");
        } else {
            // set the enable flag to false
            m_closedLoop = false;
            controller.disable();
            System.out.println(name + " closed loop disabled.");
        }
    }

    /**
     * Returns the value of the fault flag
     */
    public boolean getFault() {
        return m_fault;
    }

    public void setX(double x) {
        if (jag1 != null || jag2 != null) {
            jag1.set(x);
            jag2.set(x);
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

//        apply ramp rate
//        if(speed - last_speed > RAMP_RATE) {
//            speed = last_speed + RAMP_RATE;
//        } else if(speed - last_speed < -RAMP_RATE) {
//            speed = last_speed - RAMP_RATE;
//        }
//        last_speed = speed;

        //double speedError = (encoder.getRate() - speed);
        //if (controller.onTarget()) {
        //} 

        SmartDashboard.putNumber(name + " speed requested ", speedIn);
        SmartDashboard.putNumber(name + " speed actual ", getSpeed());
        SmartDashboard.putNumber(name + " new setpoint ", speed);

        controller.setSetpoint(speed);
    }

    public void updateSmartDashboard() {
        SmartDashboard.putNumber(name + " speed actual ", encoder.getRate());
    }

    /**
     * @returns speed of wheels
     */
    public double getSpeed() {
        return encoder.getRate();
    }

    public double getDistance() {
        return encoder.getDistance();
    }

    /**
     * Reset the encoders to 0
     */
    public void resetDistance() {
        encoder.reset();
    }

    public void pidWrite(double output) {
        jag1.pidWrite(output);
        jag2.pidWrite(output);
    }
}
