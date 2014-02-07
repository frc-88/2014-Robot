/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates.subsystems;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.templates.Wiring;

/**
 *
 * @author David
 */
public class Compressorr extends Subsystem {
    
    Compressor compressor;
    
    public Compressorr() {
        compressor = new Compressor(Wiring.pressorSwitchChannel, Wiring.compressorSwitchChannel);
        
    }
    
    public void RunCompressor() {
        compressor.start();
        SmartDashboard.putBoolean("Compressor running ", true);
    }
    
    public void StopCompressor() {
        compressor.stop();
        SmartDashboard.putBoolean("Compressor running ", false);
    }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}
