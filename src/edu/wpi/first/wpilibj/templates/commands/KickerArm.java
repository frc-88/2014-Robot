/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

/**
 *
 * @author David
 */
public class KickerArm extends CommandBase {
    
    boolean done;
    double power = -1;
    public KickerArm() {
        super("arming the kicker");
        requires(kicker);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        kicker.KickerOpenLoop(power);
        
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        /* something is light value and then the method cant be set
         * up yet because we don't know what to declare the sensor as
         * or even what sensor we are using
        */
        if (kicker.ReturnLightSensorValue()) {
            done = true;
        }
        else {
            done = false;
        }
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
        kicker.KickerOpenLoop(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
