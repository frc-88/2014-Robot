///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package edu.wpi.first.wpilibj.templates.subsystems;
//
//import edu.wpi.first.wpilibj.CANJaguar;
//import edu.wpi.first.wpilibj.DigitalInput;
//import edu.wpi.first.wpilibj.can.CANTimeoutException;
//import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.templates.Wiring;
//
///**
// *
// * @author David
// */
//public class Kicker extends Subsystem {
//    
//    //Have to wait as we may be changing what we use
//    DigitalInput lightSensor;
//    CANJaguar kickerJag;
//    
//    public Kicker() {
//        lightSensor = new DigitalInput(Wiring.lightsensor);
//        try {
//            kickerJag = new CANJaguar(Wiring.kickerJag);
//        }catch(CANTimeoutException ex) {
//            System.out.println("kicker Jag timedout");
//        }
//    }
//    // Put methods for controlling this subsystem
//    // here. Call these from Commands.
//    
////    public boolean ReturnLightSensorValue() {
////        boolean tripped = lightSensor.get();
////        return tripped;
////    }
//    
//    public void KickerOpenLoop(double value){
//        try{
//            kickerJag.setX(value);
//        }catch(CANTimeoutException ex){
//            System.out.println("kicker Jag timedout");
//        }
//    }
//
//    public void initDefaultCommand() {
//        // Set the default command for a subsystem here.
//        //setDefaultCommand(new MySpecialCommand());
//    }
//}