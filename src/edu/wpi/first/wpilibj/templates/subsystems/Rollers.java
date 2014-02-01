///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.wpi.first.wpilibj.templates.subsystems;
//
//import edu.wpi.first.wpilibj.CANJaguar;
//import edu.wpi.first.wpilibj.can.CANTimeoutException;
//import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.templates.Wiring;
//
///**
// *
// * @author David
// */
//public class Rollers extends Subsystem {
//    
//    CANJaguar rollerJag;
//    public static double ROLLER_IN_POWER = -1.0;
//    public static double ROLLER_OUT_POWER = 1.0;
//    public static double ROLLER_STOP_POWER = 0.0;
//    
//    public Rollers() {
//        try {
//            rollerJag = new CANJaguar(Wiring.armJag);
//        }catch (CANTimeoutException ex) {
//            System.out.println("arm jag has timedout");
//        }
//    }
//    public void RollerPower(double power) {
//        try {
//            rollerJag.setX(power);
//        }catch(CANTimeoutException ex) {
//            System.out.println("roller jag has timed out");
//        }
//    }
//   
//    public void initDefaultCommand() {
//        // Set the default command for a subsystem here.
//        //setDefaultCommand(new MySpecialCommand());
//    }
//}
