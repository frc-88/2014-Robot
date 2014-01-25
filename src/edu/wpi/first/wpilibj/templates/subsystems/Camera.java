//package edu.wpi.first.wpilibj.templates.subsystems;
//
//import edu.wpi.first.wpilibj.Timer;
//import edu.wpi.first.wpilibj.camera.AxisCamera;
//import edu.wpi.first.wpilibj.camera.AxisCameraException;
//import edu.wpi.first.wpilibj.command.Subsystem;
//import edu.wpi.first.wpilibj.image.*;
//import edu.wpi.first.wpilibj.image.NIVision.MeasurementType;
//import edu.wpi.first.wpilibj.templates.commands.CalculateHotGoal;
//
///**
// * Sample program to use NIVision to find rectangles in the scene that are
// * illuminated by a LED ring light (similar to the model from FIRSTChoice). The
// * camera sensitivity is set very low so as to only show light sources and
// * remove any distracting parts of the image.
// *
// * The CriteriaCollection is the set of criteria that is used to filter the set
// * of rectangles that are detected. In this example we're looking for rectangles
// * with a minimum width of 30 pixels and maximum of 400 pixels.
// *
// * The algorithm first does a color threshold operation that only takes objects
// * in the scene that have a bright green color component. Then a small object
// * filter removes small particles that might be caused by green reflection
// * scattered from other parts of the scene. Finally all particles are scored on
// * rectangularity, and aspect ratio, to determine if they are a target.
// *
// * Look in the VisionImages directory inside the project that is created for the
// * sample images.
// */
//public class Camera extends Subsystem {
//
//    //Camera constants used for distance calculation
//    final int Y_IMAGE_RES = 240;		//X Image resolution in pixels, should be 120, 240 or 480
//    // convert view angle constants to radians
//    //final double VIEW_ANGLE = 49  * Math.PI / (180 * 2);		//Axis M1013
//    //final double VIEW_ANGLE = 41.7 * Math.PI / (180 * 2);		//Axis 206 camera
//    final double VIEW_ANGLE = 37.4 * Math.PI / (180 * 2);  //Axis M1011 camera
//    //final double PI = 3.141592653; I don't think this is used anywhere
//
//    //Score limits used for target identification
//    final int RECTANGULARITY_LIMIT = 40;
//    final int ASPECT_RATIO_LIMIT = 50;
//
//    //Score limits used for hot target determination
//    //THESE NUMBERS SHOULD CHANGE DEPENDING ON TEST VALUES
//    final int TAPE_WIDTH_LIMIT = 45;
//    final int VERTICAL_SCORE_LIMIT = 45;
//    final int LR_SCORE_LIMIT = 45;
//
//    //Minimum area of particles to be considered
//    final int AREA_MINIMUM = 150;
//
//    //Maximum number of particles to process
//    final int MAX_PARTICLES = 8;
//    //Minimum hue setting
//    private static final int HUE_MIN = 50;
//    //Maximum hue setting
//    private static final int HUE_MAX = 100;
//    //Minimum saturation setting
//    private static final int SAT_MIN = 100;
//    //Maximum saturation setting
//    private static final int SAT_MAX = 255;
//    //Minimum value setting
//    private static final int VAL_MIN = 130;
//    //Maximum value setting
//    private static final int VAL_MAX = 255;
//    //ideal vertical aspect ratio
//    private static final double VERT_RATIO_IDEAL = 4.0 / 32.0;
//    //ideal horizontal aspect ratio
//    private static final double HORIZONTAL_RATIO_IDEAL = 23.5 / 4.0;
//
//    AxisCamera camera;          // the axis camera object (connected to the switch)
//    CriteriaCollection cc;      // the criteria for doing the particle filter operation
//    ColorImage image;
//
//    public class Scores {
//
//        double rectangularity;
//        double aspectRatioVertical;
//        double aspectRatioHorizontal;
//    }
//
//    public class TargetReport {
//
//        int verticalIndex;
//        int horizontalIndex;
//        boolean Hot;
//        double totalScore;
//        double leftScore;
//        double rightScore;
//        double tapeWidthScore;
//        double verticalScore;
//    }
//
//    public Camera() {
//        System.out.println("camera constructor running");
//        try {
//            camera = AxisCamera.getInstance();  // get an instance of the camera
//            cc = new CriteriaCollection();      // create the criteria for the particle filter
//            cc.addCriteria(MeasurementType.IMAQ_MT_AREA, AREA_MINIMUM, 65535, false);
//        } catch (NullPointerException ex) {
//            System.out.println("camera could not initialize");
//        }
//        /*try {
//         System.out.println("Image freshness: " + camera.freshImage());            
//         camera.getImage();
//         }catch(AxisCameraException ex) {
//         System.out.println("Could not capture image");
//         }catch(NIVisionException ex) {
//         System.out.println("Could not capture image");            
//         }*/
//
//    }
//
//    public boolean grabImage() {
//        boolean successful = camera.freshImage();
//        if (successful) {
//            try {
//                if (image != null) {
//                    image.free();
//                }
//                image = camera.getImage();
//            } catch (AxisCameraException ex) {
//
//            } catch (NIVisionException ex) {
//
//            }
//        } else {
//            System.out.println("Camera Capture Failed.");
//        }
//        return successful;
//    }
//
//    public boolean findHotGoal() {
//
//        boolean hot = false;
//        TargetReport target = new TargetReport();
//        int verticalTargets[] = new int[MAX_PARTICLES];
//        int horizontalTargets[] = new int[MAX_PARTICLES];
//        int particleCount, verticalTargetCount, horizontalTargetCount;
//        System.out.println("attempting hot goal");
//        try {
//            if (image != null) {
//                BinaryImage thresholdImage = image.thresholdHSV(HUE_MIN, HUE_MAX, SAT_MIN, SAT_MAX, VAL_MIN, VAL_MAX);   // keep only green objects
//                BinaryImage filteredImage = thresholdImage.particleFilter(cc);           // filter out small particles
//                //filteredImage.write("/filteredImage.bmp");
//
//                //iterate through each particle and score to see if it is a target
//                particleCount = filteredImage.getNumberParticles();
//                Scores scores[] = new Scores[particleCount];
//                horizontalTargetCount = verticalTargetCount = 0;
//                System.out.println("particle count " + particleCount);
//                if (particleCount > 0) {
//                    for (int i = 0; i < MAX_PARTICLES && i < particleCount; i++) {
//                        ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
//                        scores[i] = new Scores();
//
//                        //Display particle analysis report
//                        System.out.println("particle: " + i);
//                        System.out.println(report.toString());
//
//                        //Score each particle on rectangularity and aspect ratio
//                        scores[i].rectangularity = scoreRectangularity(report);
//                        scores[i].aspectRatioVertical = scoreAspectRatio(filteredImage, report, i, true);
//                        scores[i].aspectRatioHorizontal = scoreAspectRatio(filteredImage, report, i, false);
//
//                        //Check if the particle is a horizontal target, if not, check if it's a vertical target
//                        if (scoreCompare(scores[i], false)) {
//                            System.out.println("particle: " + i + " is a Horizontal Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y);
//                            horizontalTargets[horizontalTargetCount++] = i; //Add particle to target array and increment count
//                        } else if (scoreCompare(scores[i], true)) {
//                            System.out.println("particle: " + i + " is a Vertical Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y);
//                            verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
//                        } else {
//                            System.out.println("particle: " + i + " is not a Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y);
//                        }
//                        System.out.println("rect: " + scores[i].rectangularity + " ARHoriz: " + scores[i].aspectRatioHorizontal);
//                        System.out.println("ARVert: " + scores[i].aspectRatioVertical);
//                    }
//
//                    //Zero out scores and set verticalIndex to first target in case there are no horizontal targets
//                    target.totalScore = target.leftScore = target.rightScore = target.tapeWidthScore = target.verticalScore = 0;
//                    target.verticalIndex = verticalTargets[0];
//                    for (int i = 0; i < verticalTargetCount; i++) {
//                        ParticleAnalysisReport verticalReport = filteredImage.getParticleAnalysisReport(verticalTargets[i]);
//                        for (int j = 0; j < horizontalTargetCount; j++) {
//                            ParticleAnalysisReport horizontalReport = filteredImage.getParticleAnalysisReport(horizontalTargets[j]);
//                            double horizWidth, horizHeight, vertWidth, leftScore, rightScore, tapeWidthScore, verticalScore, total;
//
//                            //Measure equivalent rectangle sides for use in score calculation
//                            horizWidth = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
//                            vertWidth = NIVision.MeasureParticle(filteredImage.image, verticalTargets[i], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
//                            horizHeight = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
//
//                            //Determine if the horizontal target is in the expected location to the left of the vertical target
//                            leftScore = ratioToScore(1.2 * (verticalReport.boundingRectLeft - horizontalReport.center_mass_x) / horizWidth);
//                            //Determine if the horizontal target is in the expected location to the right of the  vertical target
//                            rightScore = ratioToScore(1.2 * (horizontalReport.center_mass_x - verticalReport.boundingRectLeft - verticalReport.boundingRectWidth) / horizWidth);
//                            //Determine if the width of the tape on the two targets appears to be the same
//                            tapeWidthScore = ratioToScore(vertWidth / horizHeight);
//                            //Determine if the vertical location of the horizontal target appears to be correct
//                            verticalScore = ratioToScore(1 - (verticalReport.boundingRectTop - horizontalReport.center_mass_y) / (4 * horizHeight));
//                            total = leftScore > rightScore ? leftScore : rightScore;
//                            total += tapeWidthScore + verticalScore;
//
//                            //If the target is the best detected so far store the information about it
//                            if (total > target.totalScore) {
//                                target.horizontalIndex = horizontalTargets[j];
//                                target.verticalIndex = verticalTargets[i];
//                                target.totalScore = total;
//                                target.leftScore = leftScore;
//                                target.rightScore = rightScore;
//                                target.tapeWidthScore = tapeWidthScore;
//                                target.verticalScore = verticalScore;
//                            }
//                        }
//                        //Determine if the best target is a Hot target
//                        System.out.println("tapeWidthScore = " + target.tapeWidthScore);
//                        System.out.println("verticalScore = " + target.verticalScore);
//                        System.out.println("leftScore = " + target.leftScore);
//                        System.out.println("rightScore = " + target.rightScore);
//
//                        target.Hot = hotOrNot(target);
//                    }
//
//                        //Information about the target is contained in the "target" structure
//                        //To get measurement information such as sizes or locations use the
//                        //horizontal or vertical index to get the particle report as shown below
//                        
//                        if (target.Hot) {
//                            System.out.println("Hot target located");
//                            hot = true;
//                        } else {
//                            System.out.println("No hot target present");
//                        }
//
//                    
//                }
//
//                /**
//                 * all images in Java must be freed after they are used since
//                 * they are allocated out of C data structures. Not calling
//                 * free() will cause the memory to accumulate over each pass of
//                 * this loop.
//                 */
//                filteredImage.free();
//                thresholdImage.free();
//         
//            }
//        } catch (NIVisionException ex) {
//            ex.printStackTrace();
//        }
//        return hot;
//    }
//
//    public void findHotGoalOriginal() {
//
//        TargetReport target = new TargetReport();
//        int verticalTargets[] = new int[MAX_PARTICLES];
//        int horizontalTargets[] = new int[MAX_PARTICLES];
//        int particleCount, verticalTargetCount, horizontalTargetCount;
//
//        try {
//            ColorImage image = camera.getImage();
//            if (image != null) {
//                // comment if using stored images
//                //ColorImage image;                           // next 2 lines read image from flash on cRIO
//                //image = new RGBImage("/testImage.jpg");		// get the sample image from the cRIO flash
//                BinaryImage thresholdImage = image.thresholdHSV(HUE_MIN, HUE_MAX, SAT_MIN, SAT_MAX, VAL_MIN, VAL_MAX);   // keep only green objects
//                //thresholdImage.write("/threshold.bmp");
//                BinaryImage filteredImage = thresholdImage.particleFilter(cc);           // filter out small particles
//                //filteredImage.write("/filteredImage.bmp");
//
//                //iterate through each particle and score to see if it is a target
//                particleCount = filteredImage.getNumberParticles();
//                Scores scores[] = new Scores[particleCount];
//                horizontalTargetCount = verticalTargetCount = 0;
//
//                if (particleCount > 0) {
//                    for (int i = 0; i < MAX_PARTICLES && i < particleCount; i++) {
//                        ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
//                        scores[i] = new Scores();
//
//                        //Display particle analysis report
//                        System.out.println("particle: " + i);
//                        System.out.println(report.toString());
//
//                        //Score each particle on rectangularity and aspect ratio
//                        scores[i].rectangularity = scoreRectangularity(report);
//                        scores[i].aspectRatioVertical = scoreAspectRatio(filteredImage, report, i, true);
//                        scores[i].aspectRatioHorizontal = scoreAspectRatio(filteredImage, report, i, false);
//
//                        //Check if the particle is a horizontal target, if not, check if it's a vertical target
//                        if (scoreCompare(scores[i], false)) {
//                            System.out.println("particle: " + i + " is a Horizontal Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y);
//                            horizontalTargets[horizontalTargetCount++] = i; //Add particle to target array and increment count
//                        } else if (scoreCompare(scores[i], true)) {
//                            System.out.println("particle: " + i + " is a Vertical Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y);
//                            verticalTargets[verticalTargetCount++] = i;  //Add particle to target array and increment count
//                        } else {
//                            System.out.println("particle: " + i + " is not a Target centerX: " + report.center_mass_x + " centerY: " + report.center_mass_y);
//                        }
//                        System.out.println("rect: " + scores[i].rectangularity + " ARHoriz: " + scores[i].aspectRatioHorizontal);
//                        System.out.println("ARVert: " + scores[i].aspectRatioVertical);
//                    }
//
//                    //Zero out scores and set verticalIndex to first target in case there are no horizontal targets
//                    target.totalScore = target.leftScore = target.rightScore = target.tapeWidthScore = target.verticalScore = 0;
//                    target.verticalIndex = verticalTargets[0];
//                    for (int i = 0; i < verticalTargetCount; i++) {
//                        ParticleAnalysisReport verticalReport = filteredImage.getParticleAnalysisReport(verticalTargets[i]);
//                        for (int j = 0; j < horizontalTargetCount; j++) {
//                            ParticleAnalysisReport horizontalReport = filteredImage.getParticleAnalysisReport(horizontalTargets[j]);
//                            double horizWidth, horizHeight, vertWidth, leftScore, rightScore, tapeWidthScore, verticalScore, total;
//
//                            //Measure equivalent rectangle sides for use in score calculation
//                            horizWidth = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
//                            vertWidth = NIVision.MeasureParticle(filteredImage.image, verticalTargets[i], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
//                            horizHeight = NIVision.MeasureParticle(filteredImage.image, horizontalTargets[j], false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
//
//                            //Determine if the horizontal target is in the expected location to the left of the vertical target
//                            leftScore = ratioToScore(1.2 * (verticalReport.boundingRectLeft - horizontalReport.center_mass_x) / horizWidth);
//                            //Determine if the horizontal target is in the expected location to the right of the  vertical target
//                            rightScore = ratioToScore(1.2 * (horizontalReport.center_mass_x - verticalReport.boundingRectLeft - verticalReport.boundingRectWidth) / horizWidth);
//                            //Determine if the width of the tape on the two targets appears to be the same
//                            tapeWidthScore = ratioToScore(vertWidth / horizHeight);
//                            //Determine if the vertical location of the horizontal target appears to be correct
//                            verticalScore = ratioToScore(1 - (verticalReport.boundingRectTop - horizontalReport.center_mass_y) / (4 * horizHeight));
//                            total = leftScore > rightScore ? leftScore : rightScore;
//                            total += tapeWidthScore + verticalScore;
//
//                            //If the target is the best detected so far store the information about it
//                            if (total > target.totalScore) {
//                                target.horizontalIndex = horizontalTargets[j];
//                                target.verticalIndex = verticalTargets[i];
//                                target.totalScore = total;
//                                target.leftScore = leftScore;
//                                target.rightScore = rightScore;
//                                target.tapeWidthScore = tapeWidthScore;
//                                target.verticalScore = verticalScore;
//                            }
//                        }
//                        //Determine if the best target is a Hot target
//                        System.out.println("tapeWidthScore = " + target.tapeWidthScore);
//                        System.out.println("verticalScore = " + target.verticalScore);
//                        System.out.println("leftScore = " + target.leftScore);
//                        System.out.println("rightScore = " + target.rightScore);
//
//                        target.Hot = hotOrNot(target);
//                    }
//
//                    if (verticalTargetCount > 0) {
//                        //Information about the target is contained in the "target" structure
//                        //To get measurement information such as sizes or locations use the
//                        //horizontal or vertical index to get the particle report as shown below
//                        ParticleAnalysisReport distanceReport = filteredImage.getParticleAnalysisReport(target.verticalIndex);
//                        double distance = computeDistance(filteredImage, distanceReport, target.verticalIndex);
//                        if (target.Hot) {
//                            System.out.println("Hot target located");
//                            System.out.println("Distance: " + distance);
//                        } else {
//                            System.out.println("No hot target present");
//                            System.out.println("Distance: " + distance);
//                        }
//                    }
//                }
//
//                /**
//                 * all images in Java must be freed after they are used since
//                 * they are allocated out of C data structures. Not calling
//                 * free() will cause the memory to accumulate over each pass of
//                 * this loop.
//                 */
//                filteredImage.free();
//                thresholdImage.free();
//                image.free();
//
//            }
//        } catch (AxisCameraException ex) {        // this is needed if the camera.getImage() is called
//            ex.printStackTrace();
//        } catch (NIVisionException ex) {
//            ex.printStackTrace();
//        } catch (NullPointerException ex) {
//            ex.printStackTrace();
//        }
//    }
//
//    /**
//     * Computes the estimated distance to a target using the height of the
//     * particle in the image. For more information and graphics showing the math
//     * behind this approach see the Vision Processing section of the
//     * ScreenStepsLive documentation.
//     *
//     * @param image The image to use for measuring the particle estimated
//     * rectangle
//     * @param report The Particle Analysis Report for the particle
//     * @param outer True if the particle should be treated as an outer target,
//     * false to treat it as a center target
//     * @return The estimated distance to the target in Inches.
//     */
//    public double computeDistance(BinaryImage image, ParticleAnalysisReport report, int particleNumber) throws NIVisionException {
//        double rectLong, height;
//        int targetHeight;
//
//        rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
//        //using the smaller of the estimated rectangle long side and the bounding rectangle height results in better performance
//        //on skewed rectangles
//        height = Math.min(report.boundingRectHeight, rectLong);
//        targetHeight = 32;
//
//        return Y_IMAGE_RES * targetHeight / (height * 12 * 2 * Math.tan(VIEW_ANGLE));
//    }
//
//    /**
//     * Computes a score (0-100) comparing the aspect ratio to the ideal aspect
//     * ratio for the target. This method uses the equivalent rectangle sides to
//     * determine aspect ratio as it performs better as the target gets skewed by
//     * moving to the left or right. The equivalent rectangle is the rectangle
//     * with sides x and y where particle area= x*y and particle perimeter= 2x+2y
//     *
//     * @param image The image containing the particle to score, needed to
//     * perform additional measurements
//     * @param report The Particle Analysis Report for the particle, used for the
//     * width, height, and particle number
//     * @param outer	Indicates whether the particle aspect ratio should be
//     * compared to the ratio for the inner target or the outer
//     * @return The aspect ratio score (0-100)
//     */
//    public double scoreAspectRatio(BinaryImage image, ParticleAnalysisReport report, int particleNumber, boolean vertical) throws NIVisionException {
//        double rectLong, rectShort, aspectRatio, idealAspectRatio;
//
//        //rectLong = NIVision.MeasureParticle(image.image, particleNumber, false, MeasurementType.IMAQ_MT_EQUIVALENT_RECT_LONG_SIDE);
//        //rectShort = NIVision.MeasureParticle(image.image, particleNumber, false, 
//        //                            MeasurementType.IMAQ_MT_EQUIVALENT_RECT_SHORT_SIDE);
//        idealAspectRatio = vertical ? (VERT_RATIO_IDEAL) : (HORIZONTAL_RATIO_IDEAL);	//Vertical reflector 4" wide x 32" tall, horizontal 23.5" wide x 4" tall
//        //rectLong = report.boundingRectHeight;
//        //rectShort = report.boundingRectWidth;
//        //Divide width by height to measure aspect ratio
//        //if (report.boundingRectWidth > report.boundingRectHeight) {
//        //particle is wider than it is tall, divide long by short
//        //    aspectRatio = ratioToScore((rectLong / rectShort) / idealAspectRatio);
//        //} else {
//        //particle is taller than it is wide, divide short by long
//        //    aspectRatio = ratioToScore((rectShort / rectLong) / idealAspectRatio);
//        //}
//        aspectRatio = ratioToScore(((double) report.boundingRectWidth / (double) report.boundingRectHeight) / idealAspectRatio);
//        System.out.println("ratio = " + (double) report.boundingRectWidth / (double) report.boundingRectHeight);
//        return aspectRatio;
//    }
//
//    /**
//     * Compares scores to defined limits and returns true if the particle
//     * appears to be a target
//     *
//     * @param scores The structure containing the scores to compare
//     * @param outer True if the particle should be treated as an outer target,
//     * false to treat it as a center target
//     *
//     * @return True if the particle meets all limits, false otherwise
//     */
//    boolean scoreCompare(Scores scores, boolean vertical) {
//        boolean isTarget = true;
//
//        isTarget &= scores.rectangularity > RECTANGULARITY_LIMIT;
//        if (vertical) {
//            isTarget &= scores.aspectRatioVertical > ASPECT_RATIO_LIMIT;
//        } else {
//            isTarget &= scores.aspectRatioHorizontal > ASPECT_RATIO_LIMIT;
//        }
//
//        return isTarget;
//    }
//
//    /**
//     * Computes a score (0-100) estimating how rectangular the particle is by
//     * comparing the area of the particle to the area of the bounding box
//     * surrounding it. A perfect rectangle would cover the entire bounding box.
//     *
//     * @param report The Particle Analysis Report for the particle to score
//     * @return The rectangularity score (0-100)
//     */
//    double scoreRectangularity(ParticleAnalysisReport report) {
//        if (report.boundingRectWidth * report.boundingRectHeight != 0) {
//            return 100 * report.particleArea / (report.boundingRectWidth * report.boundingRectHeight);
//        } else {
//            return 0;
//        }
//    }
//
//    /**
//     * Converts a ratio with ideal value of 1 to a score. The resulting function
//     * is piecewise linear going from (0,0) to (1,100) to (2,0) and is 0 for all
//     * inputs outside the range 0-2
//     */
//    double ratioToScore(double ratio) {
//        return (Math.max(0, Math.min(100 * (1 - Math.abs(1 - ratio)), 100)));
//    }
//
//    /**
//     * Takes in a report on a target and compares the scores to the defined
//     * score limits to evaluate if the target is a hot target or not.
//     *
//     * Returns True if the target is hot. False if it is not.
//     */
//    boolean hotOrNot(TargetReport target) {
//        boolean isHot = true;
//
//        isHot &= target.tapeWidthScore >= TAPE_WIDTH_LIMIT;
//        isHot &= target.verticalScore >= VERTICAL_SCORE_LIMIT;
//        isHot &= (target.leftScore > LR_SCORE_LIMIT) | (target.rightScore > LR_SCORE_LIMIT);
//
//        return isHot;
//    }
//
//    public void initDefaultCommand() {
//        // Set the default command for a subsystem here.
//    }
//
//    public double findDistance() {
//        TargetReport target = new TargetReport();
//        int bestVerticalTargetIndex = 0;
//        int particleCount;
//        double distance = -1;
//        double highestScore = 0;
//
//        System.out.println("find distance");
//
//        try {
//            /**
//             * Do the image capture with the camera and apply the algorithm
//             * described above. This sample will either get images from the
//             * camera or from an image file stored in the top level directory in
//             * the flash memory on the cRIO. The file name in this case is
//             * "testImage.jpg"
//             *
//             */
//            if (image != null) {
//                System.out.println("attempting to calculate distance ln 425");
//                BinaryImage thresholdImage = image.thresholdHSV(HUE_MIN, HUE_MAX, SAT_MIN, SAT_MAX, VAL_MIN, VAL_MAX);   // keep only green objects
//                BinaryImage filteredImage = thresholdImage.particleFilter(cc);           // filter out small particles
//
//                //iterate through each particle and score to see if it is a target
//                particleCount = filteredImage.getNumberParticles();
//                Scores scores[] = new Scores[particleCount];
//                System.out.println(particleCount);
//
//                if (particleCount > 0) {
//                    for (int i = 0; i < MAX_PARTICLES && i < particleCount; i++) {
//                        ParticleAnalysisReport report = filteredImage.getParticleAnalysisReport(i);
//                        scores[i] = new Scores();
//
//                        //Display particle analysis report
//                        System.out.println("particle: " + i);
//                        System.out.println(report.toString());
//
//                        //Score each particle on rectangularity and aspect ratio
//                        scores[i].rectangularity = scoreRectangularity(report);
//                        scores[i].aspectRatioVertical = scoreAspectRatio(filteredImage, report, i, true);
//
//                        //Check if the particle is a horizontal target, if not, check if it's a vertical target
//                        if (scoreCompare(scores[i], true)) {
//                            if (highestScore == 0 || (scores[i].rectangularity + scores[i].aspectRatioVertical) > highestScore) {
//                                highestScore = scores[i].rectangularity + scores[i].aspectRatioVertical;
//                                bestVerticalTargetIndex = i;
//                            }
//                        }
//                    }
//
//                    if (highestScore > 0) {
//                        ParticleAnalysisReport distanceReport = filteredImage.getParticleAnalysisReport(bestVerticalTargetIndex);
//                        distance = computeDistance(filteredImage, distanceReport, bestVerticalTargetIndex);
//                        System.out.println("Distance: " + distance);
//                    } else {
//                        System.out.println("No vertical target found");
//                    }
//                }
//
//                /**
//                 * all images in Java must be freed after they are used since
//                 * they are allocated out of C data structures. Not calling
//                 * free() will cause the memory to accumulate over each pass of
//                 * this loop.
//                 */
//                filteredImage.free();
//                thresholdImage.free();
//
//            }
//        } catch (NIVisionException ex) {
//            ex.printStackTrace();
//        }
//        return distance;
//    }
//}
