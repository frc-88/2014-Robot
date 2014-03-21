/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.templates.commands.CommandBase;
import edu.wpi.first.wpilibj.templates.subsystems.Camera;
import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.command.WaitCommand;
import edu.wpi.first.wpilibj.templates.commands.CalculateHotGoal;
import edu.wpi.first.wpilibj.templates.commands.DriveAutonomous;

/**
 *
 * @author David
 */
public class Autonomous extends CommandGroup {
    
    public Autonomous() {
//        addSequential(new ArmDown());
        //parameters for drive auto is speedleft, speedright, time, distance
        //addSequential(new WaitCommand(1));
        //addSequential(new ArmDown());
        addSequential(new DriveAutonomous(.5,.5,.9,8));
        addSequential(new WaitCommand(.5));
        //addSequential(new ArmUp());
        addSequential(new CalculateHotGoal(3));
        addSequential(new KickerAuto(2));
        //addSequential(new DriveAutonomous(.6,.6,.5,8));
        //addSequential(new ArmDown());
        //addSequential(new DriveAutonomous(.5,.5,1.0 ,8));
        // Add Commands here:
        // e.g. addSequential(new Command1());
        //      addSequential(new Command2());
        // these will run in order.

        // To run multiple commands at the same time,
        // use addParallel()
        // e.g. addParallel(new Command1());
        //      addSequential(new Command2());
        // Command1 and Command2 will run in parallel.
        // A command group will require all of the subsystems that each member
        // would require.
        // e.g. if Command1 requires chassis, and Command2 requires arm,
        // a CommandGroup containing them would require both the chassis and the
        // arm.
    }
}
