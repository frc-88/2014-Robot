/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.PIDOutput;


/**
 *
 * @author David
 */
public class JagPair implements PIDOutput {

    CANJaguar jag1, jag2;
    public JagPair(CANJaguar jag1In, CANJaguar jag2In) {
        jag1 = jag1In;
        jag2 = jag2In;
        
    }
    public void pidWrite(double output) {
        jag1.pidWrite(output);
        jag2.pidWrite(output);
        
    }
    
}
