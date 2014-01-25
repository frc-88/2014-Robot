///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.wpi.first.wpilibj.templates.commands;
//
///**
// *
// * @author David
// */
//public class GetImage extends CommandBase {
//    
//    boolean done;
//    
//    public GetImage() {
//        super("Get Image");
//        requires(camera);
//        // Use requires() here to declare subsystem dependencies
//        // eg. requires(chassis);
//    }
//
//    // Called just before this Command runs the first time
//    protected void initialize() {
//        System.out.println("attempting to grab an image");
//        done = false;
//    }
//
//    // Called repeatedly when this Command is scheduled to run
//    protected void execute() {
//        System.out.println("Executing grabbing an image");
//        done = camera.grabImage();
//    }
//
//    // Make this return true when this Command no longer needs to run execute()
//    protected boolean isFinished() {
//        System.out.println("finished grabbing an image");
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
