/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.command.CommandGroup;
import edu.wpi.first.wpilibj.command.WaitCommand;

/**
 *
 * @author David
 */
public class AutonomousTwoBall extends CommandGroup {
    
    public AutonomousTwoBall() {
        //parameters for drive auto is speedleft, speedright, time, distance
        addSequential(new DriveAutonomous(.2,.2,.5,10));//last is inches or feet?
 //       addSequential(new DriveAutonomous(0, 0, 0 ,0));
        addSequential(new KickerAuto(2));
        addParallel(new DriveAutonomous(.3,.3, 2,10));
        //additional .5 seconds built in to armdown
        //note sequential runs after first parallel component. Add wait before entering parallel.
        addSequential(new ArmDown());
        addParallel(new DriveAutonomous(.3,.3,3,10));
        addParallel(new KickerArm());
        addParallel(new RollersAuto(3));
        addSequential(new WaitCommand(3));
        //additional .5 seconds built in to armup
        addSequential(new ArmUp());
        addSequential(new KickerAuto(2));
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
