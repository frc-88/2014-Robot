package edu.wpi.first.wpilibj.templates;

/**
 *
 * @author David
 */
public class Wiring {
    public static boolean lights = false;
    /*
     * ### Drive ###
     */
    // motors
    public static final int leftDrive = 3;
    public static final int leftDrive2 = 4;
    public static final int rightDrive = 8;
    public static final int rightDrive2 = 9;
    // encoders
    public static final int lEncoderAChannel = 6;
    public static final int lEncoderBChannel = 7;
    public static final int rEncoderAChannel = 8;
    public static final int rEncoderBChannel = 9;
    // shifters
    public static final int highShifter=3;
    public static final int lowShifter=2;

    /*
     * ### Rollers ###
     */
    public static final int rollerJag = 7;
    
    /*
     * ### Kicker ###
     */
    // motor
    public static final int kickerJag = 5;
    // encoder???
    // light sensor
    public static final int lightSensor = 14;
    public static final double kickerPower = 1.0;
    
    /*
     * ### Arms ###
     */
    //wrong values
    public static final int lArm1 = 4;  
    public static final int lArm2 = 5;
    public static final int rArm1 = 8;
    public static final int rArm2 = 7;
    
    /*
     * ### Compressor ###
     */
    public static final int pressorSwitchChannel = 5;
    public static final int compressorSwitchChannel = 5;
    
    /*
     * Lights 
     */
    public static final int lightDigitalOutPin1 = 4;
    public static final int lightDigitalOutPin2 = 5;
    public static final int lightDigitalOutPin3 = 6;
    public static final int lightDigitalOutPin4 = 7;
    public static final int lightDigitalOutPinSecret = 8;
    public static final int lightPwmOutPin1 = 3;
    public static final int lightPwmOutPin2 = 4;
    public static final int lightPwmOutPin3 = 5;
    
}
