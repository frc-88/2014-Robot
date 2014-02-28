package edu.wpi.first.wpilibj.templates.commands;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.OI;
import edu.wpi.first.wpilibj.templates.Wiring;
import edu.wpi.first.wpilibj.templates.subsystems.*;

public abstract class CommandBase extends Command {
    
    public static OI oi;
    public static Camera camera = new Camera();
    public static Drive drive = new Drive();
    public static Rollers roller = new Rollers();
    public static Arms arm = new Arms();
    public static Kicker kicker = new Kicker();
    private static Compressor compressor;
    private static int iterator = 0;
    public static Lights lights;
    
    public static void init() {
        if (Wiring.lights) {
            lights = new Lights();
        }
        // This MUST be here. If the OI creates Commands (which it very likely
        // will), constructing it during the construction of CommandBase (from
        // which commands extend), subsystems are not guaranteed to be
        // yet. Thus, their requires() statements may grab null pointers. Bad
        // news. Don't move it.
        System.out.println("command base init");
        oi = new OI();
        compressor = new Compressor(Wiring.compressorSwitchChannel,Wiring.pressorSwitchChannel);
        compressor.start();
        System.out.println("command base init finished");
        SmartDashboard.putData(drive);
//        SmartDashboard.putData(roller);
//        SmartDashboard.putData(arm);
        //SmartDashboard.putData(camera);
        // Show what command your subsystem is running on the SmartDashboard
        //SmartDashboard.putData(exampleSubsystem);
    }

    public CommandBase(String name) {
        super(name);
    }

    public static void updateDashboard() {

        // put data on dashboard every 10th call
        if(iterator % 10 == 0) {
            // Subsystem faults lights
//            SmartDashboard.putBoolean("Rollers ", !roller.getFault());
//            SmartDashboard.putBoolean("Arm ", !arm.getFault());
//            SmartDashboard.putBoolean("Kicker ", !kicker.getFault());
            SmartDashboard.putBoolean("Drive ", !drive.getFault());
        }

       // put data on dashboard every 10th call
        if(iterator % 10 == 5) {
            // Various indicators
             SmartDashboard.putNumber("Drive (left) ", drive.getLeftSpeed());
             SmartDashboard.putNumber("Drive (right) ", drive.getRightSpeed());
             SmartDashboard.putNumber("DriveDist (left) ", drive.getLeftDistance());
             SmartDashboard.putNumber("DriveDist (right) ", drive.getRightDistance());
        }
        iterator++;
    }

    public CommandBase() {
        super();
    }
}
