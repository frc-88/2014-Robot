///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.wpi.first.wpilibj.templates.commands;
//
//import edu.wpi.first.wpilibj.templates.subsystems.Camera;
//
///**
// *
// * @author David
// */
//public class CalculateHotGoal extends CommandBase {
//    
//    boolean done = false;
//            
//    public CalculateHotGoal() {
//        super("CameraHotGoal");
//        requires(camera);
//    }
//
//    // Called just before this Command runs the first time
//    protected void initialize() {
//        done = false;
//    }
//
//    // Called repeatedly when this Command is scheduled to run
//    protected void execute() {
//        if (!done) {
//            camera.findHotGoal();
//            done = true;
//        }
//    }
//
//    // Make this return true when this Command no longer needs to run execute()
//    protected boolean isFinished() {
//        return done;
//    }
//
//    // Called once after isFinished returns true
//    protected void end() {
//    }
//
//    // Called when another command which requires one or more of the same
//    // subsystems is scheduled to run
//    protected void interrupted() {
//    }
//}
