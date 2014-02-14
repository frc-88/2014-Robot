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

    private static final double p = .05;
    private static final double i = 0.0;
    private static final double d = 0.0;
    private static final double f = 0.2;
    private static final double cycleTime = .020;
    private static byte jagGroup = 1;
    private static final int DRIVE_ENCODER_LINES = 250;
    //feet per revolution over encoder lines
    private static final double DISTANCE_PER_PULSE = 1.57 / 250;
    private Jaguar jag1, jag2;
    private Encoder encoder;
    private PIDController controller;
    // private double last_speedL = 0.0;
    // private double last_speedR = 0.0;
    private boolean m_closedLoop;
    private boolean m_fault;

    public JagPair(int jag1In, int jag2In, int encoderA, int encoderB) {
        jag1 = new Jaguar(jag1In);
        jag2 = new Jaguar(jag2In);

        encoder = new Encoder(encoderA, encoderB);
        encoder.setDistancePerPulse(DISTANCE_PER_PULSE);
        encoder.setSamplesToAverage(6);
        encoder.setPIDSourceParameter(PIDSource.PIDSourceParameter.kRate);
        encoder.reset();
        encoder.start();

        controller = new PIDController(p, i, d, f, encoder, this, cycleTime);
    }

    /**
     * Enables ClosedLoop control Driving. It sets it to speed.
     */
    public void enableClosedLoop() {
        System.out.println("Enabling closed loop control");

        if (jag1 != null && jag2 != null) {
            System.out.println("jag1 is " + jag1);
            System.out.println("jag2 is " + jag2);

            // set the motors to closed loop
            //may be percent v bus
            controller.setPID(p, i, d, f);

            // set the enable flag
            m_closedLoop = true;
        }
        controller.enable();
    }

    /**
     * Disables the Drive closed loop and puts it into open loop.
     */
    public void disableClosedLoop() {
        if (jag1 != null && jag2 != null) {
            // set the enable flag to false
            m_closedLoop = false;
        }
        controller.disable();
    }

    /**
     * Returns whether or not jagPair is in ClosedLoop. If it is it will return
     * true and if it is not it will return false.
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
