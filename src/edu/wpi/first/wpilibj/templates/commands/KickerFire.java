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
public class KickerFire extends CommandBase {
    
    double power = -1;
    boolean shouldKick;
    boolean done;
    
    public KickerFire() {
        super("kicking the ball");
        requires(kicker);
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        if (kicker.ReturnLightSensorValue()) {
            shouldKick = true;
        }
            kicker.KickerOpenLoop(power);

    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (shouldKick) {
            kicker.KickerOpenLoop(power);
        }
        if (!kicker.ReturnLightSensorValue()) {
            shouldKick = false;
            done = true;
        }
        kicker.ReturnLightSensorValue();
//
//       Run until the light sensor changes, then done = true        
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
