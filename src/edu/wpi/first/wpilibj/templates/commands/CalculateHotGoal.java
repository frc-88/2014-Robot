/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.subsystems.Camera;

/**
 *
 * @author David
 */
public class CalculateHotGoal extends CommandBase {
    
    double delay;
    boolean done = false;
    int count = 0;
    public CalculateHotGoal(double timeout) {
        super("CameraHotGoal");
        delay=timeout;
        requires(camera);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
        done = false;
        setTimeout(delay);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
        if (count % 50 == 0) {
            camera.grabImage();
            done = camera.findHotGoal();
            count++;
        }
        
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        done = isTimedOut()||done;
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
