///*
// * To change this template, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.wpi.first.wpilibj.templates.commands;
//
//import edu.wpi.first.wpilibj.DriverStation;
//
///**
// *
// * @author Ag (orig) David (revised)
// */
//public class LightsDefault extends CommandBase {
//        
//    public LightsDefault() {
//        // Use requires() here to declare subsystem dependencies
//        // eg. requires(chassis);
//        requires(lights);
//    }
//
//    // Called just before this Command runs the first time
//    protected void initialize() {
//        if (DriverStation.getInstance().getAlliance() == DriverStation.Alliance.kBlue) {
//                // We're in auto and we're on the blue alliance.
//                lights.setRedAlliance(false);
//                lights.setSecret(false);
//            }
//            else {
//                // Otherwise, we must be on the red alliance.
//                lights.setRedAlliance(true);
//                lights.setSecret(true);
//            }
//    }
//
//    // Called repeatedly when this Command is scheduled to run
//    protected void execute() {
//        if (DriverStation.getInstance().isAutonomous()) {
//            lights.setMode(lights.MODE_AUTONOMOUS);
//        } else if (DriverStation.getInstance().isOperatorControl()) {
//            // We're in teleop
//            lights.setMode(lights.MODE_DRIVE_FILL);
//            lights.setAnalog(lights.ANALOG_CHANNEL_LEFT, drive.getLeftSpeed()/80.0);
//            lights.setAnalog(lights.ANALOG_CHANNEL_RIGHT, drive.getRightSpeed()/80.0);
////            if (kicker.firing) {
////                lights.setMode(lights.MODE_SECRET);
////            }
////            else if (!kicker.firing) {
////            lights.setMode(lights.MODE_DRIVE_FILL);
////            }
//        } else if (DriverStation.getInstance().isDisabled()) {
//            // We're disabled.  This should trigger at the end of the match.
//            lights.setMode(lights.MODE_BLINKY);
//        }
//        if (kicker.ReturnLightSensorValue() == true) {
//            lights.setKickerArmed(true);
//        } else{
//            lights.setKickerArmed(false);  
//        }
//    }
//
//    // Make this return true when this Command no longer needs to run execute()
//    protected boolean isFinished() {
//        return false;
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
