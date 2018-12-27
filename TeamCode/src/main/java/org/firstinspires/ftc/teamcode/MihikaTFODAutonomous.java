package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;


@Autonomous(name="TFODAutonomousMihika", group="Mihika")

public class MihikaTFODAutonomous extends LinearOpMode {


    /* Declare OpMode members. */
    HardwareAutonomous         robot   = new HardwareAutonomous();   // Use a Pushbot's hardware
    /***private ElapsedTime runtime = new ElapsedTime();

     static final double     COUNTS_PER_MOTOR_REV    = 1440 ;    // eg: TETRIX Motor Encoder
     static final double     DRIVE_GEAR_REDUCTION    = 1.0 ;     // This is < 1.0 if geared UP
     static final double     WHEEL_DIAMETER_INCHES   = 4.0 ;     // For figuring circumference
     static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
     (WHEEL_DIAMETER_INCHES * 3.1415);
     static final double     DRIVE_SPEED             = 0.7;
     static final double     TURN_SPEED              = 0.8;
     private Servo markerServo = null;
     private CRServo spinnerServo = null;
     ***/

    //number of encoder ticks for going up and down.
    // 1.75 inch requires 20 rotations.
    //1 rotation : 1120 Ticks.
    //1 inch = 1120*20/1.75 ticks
    static final double ROTATIONS_PER_INCH = 11.42;
    static final double TICKS_PER_INCH = (ROTATIONS_PER_INCH * 1120); //may need to update Counts per motor rev because the motor may have a different amount of ticks
    final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    final String LABEL_GOLD_MINERAL = "Gold Mineral";
    final String LABEL_SILVER_MINERAL = "Silver Mineral";
    final String VUFORIA_KEY = "AUWa2hP/////AAABmYAEN4JY30WlndAsvgcYjZAcwt/KX4c9VUt+Br3zZxPhIbJ+ovlQrV3YlETOwJ4Q5NajUuwkdpnX2292snWM8iiXzQ2Nm37xl78r82PlDZPAKP8XV+9sBg1KMHO+0zDzTtWNa/fmNPeEhmdff/YWUzcqTmGLnccOhj57waivZa4Y5xDfH4YKssYJUNQKumOd8v5m90IEKYgWghs7BhxpWfQbjzC+3QUKPc7q34V+9W4xQ+2S+hI0inYLrK4rSdiCGU76d8hwlBWuDC8PWrkqwIi6EptTL/nP1rLoWy/Usv6ZUqRRHwkgLNYsrWusN0G5d71F6tdvRDbGdgQKQ2evHWZPPtlVW6u0S5N5S2sXu7R+";
    TFObjectDetector tfod = null;
    VuforiaLocalizer vuforia = null;
    int positionGold;




    //static final double     TICKS_PER_INCH          = 16500;
    //14000 - may be the right one - might need to try to check ---for above

    Orientation lastAngles = new Orientation();
    double globalAngle, power = .30, correction;
    @Override
    public void runOpMode() {


        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        robot.init(hardwareMap);

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        //robot.markerServo = hardwareMap.servo.get("markerServo");

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.landerLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.landerLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset;
        telemetry.addData("MotorTelemetry",  "Starting at %7d :%7d :%7d :%7d",
                robot.leftMotor.getCurrentPosition(),
                robot.rightMotor.getCurrentPosition(),
                robot.backleftMotor.getCurrentPosition(),
                robot.backrightMotor.getCurrentPosition());
        telemetry.update();




        /*
         * IMPORTANT: You need to obtain your own license key to use Vuforia. The string below with which
         * 'parameters.vuforiaLicenseKey' is initialized is for illustration only, and will not function.
         * A Vuforia 'Development' license key, can be obtained free of charge from the Vuforia developer
         * web site at https://developer.vuforia.com/license-manager.
         *
         * Vuforia license keys are always 380 characters long, and look as if they contain mostly
         * random data. As an example, here is a example of a fragment of a valid key:
         *      ... yIgIzTqZ4mWjk9wd3cZO9T1axEqzuhxoGlfOOI2dRzKS4T0hQ8kT ...
         * Once you've obtained a license key, copy the string from the Vuforia web site
         * and paste it in to your code on the next line, between the double quotes.
         */

        /**
         * {@link #vuforia} is the variable we will use to store our instance of the Vuforia
         * localization engine.
         */
        VuforiaLocalizer vuforia;

        /**
         * {@link #tfod} is the variable we will use to store our instance of the Tensor Flow Object
         * Detection engine.
         */

        //@Override
        //public void runOpModex(){
            // The TFObjectDetector uses the camera frames from the VuforiaLocalizer, so we create that
            // first.
            initVuforia();

            if (ClassFactory.getInstance().canCreateTFObjectDetector()) {
                 initTfod();
            } else {
                telemetry.addData("Sorry!", "This device is not compatible with TFOD");
            }

            /** Wait for the game to begin */
            telemetry.addData(">", "Press Play to start tracking");
            telemetry.update();
            waitForStart();

            if (opModeIsActive()) {
                /** Activate Tensor Flow Object Detection. */
                if (tfod != null) {
                    tfod.activate();
                }

                while (opModeIsActive()) {
                    if (tfod != null) {
                        // getUpdatedRecognitions() will return null if no new information is available since
                        // the last time that call was made.
                        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                        if (updatedRecognitions != null) {
                            telemetry.addData("# Object Detected", updatedRecognitions.size());
                            if (updatedRecognitions.size() == 3) {
                                int goldMineralX = -1;
                                int silverMineral1X = -1;
                                int silverMineral2X = -1;
                                for (Recognition recognition : updatedRecognitions) {
                                    if (recognition.getLabel().equals(LABEL_GOLD_MINERAL)) {
                                        goldMineralX = (int) recognition.getLeft();
                                    } else if (silverMineral1X == -1) {
                                        silverMineral1X = (int) recognition.getLeft();
                                    } else {
                                        silverMineral2X = (int) recognition.getLeft();
                                    }
                                }
                                if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1) {
                                    if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X) {
                                        telemetry.addData("Gold Mineral Position", "Left");
                                        positionGold = 3;
                                        break;
                                    } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X) {
                                        telemetry.addData("Gold Mineral Position", "Right");
                                        positionGold = 1;
                                        break;
                                    } else {
                                        telemetry.addData("Gold Mineral Position", "Center");
                                        positionGold = 2;
                                        break;
                                    }
                                }
                            }
                            telemetry.update();
                        }
                    }
                }
            }

            if (tfod != null) {
                tfod.shutdown();
            }
        //}

        /**
         * Initialize the Vuforia localization engine.
         */

        

        // Wait for the game to start (driver presses PLAY)
        //waitForStart();



        //0,2,1,3
        //add sensing in here - done
        //get position # - done
        //int detectionPosition = ADDPOSITIONHERE
        myLanderLift(0, 1, 7, 9.0);
        ///For above, you will need to check how many seconds
        //it takes from where we are starting to all the way out
        //myEncoderDrive(3, 0.3, 5, 5.0);
        myDetectionTest(positionGold, robot.DRIVE_SPEED, 40.0);
        myDetectionRun(positionGold, robot.DRIVE_SPEED, 40.0);
        //use position number to determine routes above
        myLanderLift(1, 1, 6, 7.0);




        //rotate(-7, TURN_SPEED);
        //may need to change time to 6.5
        //may need to change inches/distance once ticks per inch is changed
        //myEncoderDrive(3, 0.3, 5, 5.0);
        //myEncoderDrive(0, robot.DRIVE_SPEED, 63, 5.0);
        //markerServo.setPosition(0.9);
        //myLanderLift(2, 0.6, 5, 0.5);
        //spinnerServo.setPower(0.79);
        //rotate(20, robot.TURN_SPEED);
        //robot.markerServo.setPosition(-0.9);
        //rotate(10, robot.TURN_SPEED);
        //myEncoderDrive(3, 0.3, 5, 5.0);
        //myLanderLift(1, 1, 1, 1);
        //40
        //myEncoderDrive(1, robot.DRIVE_SPEED, 83, 5.0);
        //might need to change length moving above
        //rotate(10, robot.TURN_SPEED);
        //myEncoderDrive(1, robot.DRIVE_SPEED, 13,7.0);
        //park in crater

/*
        myEncoderDrive(0, DRIVE_SPEED  D, 24, 24,5.0);
        myEncoderDrive(2, DRIVE_SPEED, 24, -24,5.0);
        myEncoderDrive(0, DRIVE_SPEED, 24, 24,5.0);
        myEncoderDrive(2, DRIVE_SPEED, 24, -24,5.0);
        myEncoderDrive(0, DRIVE_SPEED, 24, 24,5.0);
        myEncoderDrive(2, DRIVE_SPEED, 24, -24,5.0);
        myEncoderDrive(0, DRIVE_SPEED, 24, 24,5.0);
        */
        //robot.leftClaw.setPosition(1.0);            // S4: Stop and close the claw.
        //robot.rightClaw.setPosition(0.0);
        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();
    }





    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    private void myDetectionTest (int position,
                                  double speed,
                                  double timeoutS) {


        //Reset the encoder
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (position == 1)
            {
                myEncoderDrive(3, robot.DRIVE_SPEED, 16, 10.0);
                myEncoderDrive(0, robot.DRIVE_SPEED, 14, 10.0);
                rotate(80, robot.TURN_SPEED);
                myEncoderDrive(0, robot.TURN_SPEED, 13, 10.0);
            }
            else if (position == 3)
            {
                myEncoderDrive(3, robot.DRIVE_SPEED, 16, 10.0);
                myEncoderDrive(1, robot.DRIVE_SPEED, 14, 10.0);
                rotate(70, robot.TURN_SPEED);
                myEncoderDrive(0, robot.TURN_SPEED, 13, 10.0);
            } else // Position = 2 and default position
            {
                myEncoderDrive(3, robot.DRIVE_SPEED, 16, 10.0);
                rotate(82 , robot.TURN_SPEED);
                myEncoderDrive(0, robot.DRIVE_SPEED, 15, 10.0);
            }




            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backleftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backrightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            robot.runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));
            robot.backleftMotor.setPower(Math.abs(speed));
            robot.backrightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (robot.runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() )) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d");
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
            robot.backleftMotor.setPower(0);
            robot.backrightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backleftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backrightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(200);   // optional pause after each move
        }
    }

    private void myDetectionRun (int position,
                                  double speed,
                                  double timeoutS) {


        //Reset the encoder
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (position == 1)
            {
                myEncoderDrive(0, robot.TURN_SPEED, 20, 10.0);
                rotate(30, robot.TURN_SPEED);
                myEncoderDrive(0, robot.DRIVE_SPEED, 20, 10.0);
                rotate(79, robot.TURN_SPEED);
                //robot.markerServo.setPosition(-0.9);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 2, 10.0);
                myEncoderDrive(0, robot.DRIVE_SPEED, 67, 10.0);
                //rotate(-14, robot.TURN_SPEED);
                myEncoderDrive(0, robot.DRIVE_SPEED, 8, 10.0);
            }
            else if (position == 3)
            {
                myEncoderDrive(0, robot.TURN_SPEED, 20, 10.0);
                rotate(-30, robot.TURN_SPEED);
                myEncoderDrive(0, robot.DRIVE_SPEED, 10, 10.0);
                //robot.markerServo.setPosition(-0.9);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 2, 10.0);
                myEncoderDrive(1, robot.DRIVE_SPEED, 60, 10.0);
                //rotate(-14, robot.TURN_SPEED);
                myEncoderDrive(1, robot.DRIVE_SPEED, 8, 10.0);
            } else // Position = 2 also default position
            {
                myEncoderDrive(0, robot.DRIVE_SPEED, 26, 10.0);
                rotate(-49, robot.TURN_SPEED);
                //robot.markerServo.setPosition(-0.9);
                myEncoderDrive(1, robot.DRIVE_SPEED, 35, 10.0);
                myEncoderDrive(2, robot.DRIVE_SPEED, 3, 10.0);
                myEncoderDrive(1, robot.DRIVE_SPEED, 33, 10.0);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 20, 10.0);
                //rotate(-14, robot.TURN_SPEED);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 13, 10.0);
            }




            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backleftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backrightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            robot.runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));
            robot.backleftMotor.setPower(Math.abs(speed));
            robot.backrightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (robot.runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() )) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d");
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
            robot.backleftMotor.setPower(0);
            robot.backrightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backleftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backrightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(200);   // optional pause after each move
        }
    }



    private void myLanderLift (double direction,
                               double speed,
                               double Inches,
                               double timeoutS) {
        int newLiftTarget;


        //Reset the encoder
        robot.landerLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (direction == 0)
            {
                //Go down
                newLiftTarget = robot.landerLift.getCurrentPosition() + (int)(Inches * TICKS_PER_INCH);
            } else if (direction == 1) {
                //Go down
                newLiftTarget = robot.landerLift.getCurrentPosition() + (int) (-1 * Inches * TICKS_PER_INCH);
            } else if (direction == 2) {
                newLiftTarget = robot.collectionLift.getCurrentPosition() + (int) (Inches * TICKS_PER_INCH);
            }
            else
            {
                Inches = 0;
                newLiftTarget = robot.landerLift.getCurrentPosition() + (int)(Inches * TICKS_PER_INCH);
            }

            robot.landerLift.setTargetPosition(newLiftTarget);

            // Turn On RUN_TO_POSITION
            robot.landerLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            robot.runtime.reset();
            robot.landerLift.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            //while (opModeIsActive() &&
            //        (runtime.seconds() < timeoutS) &&
            //       (robot.landerLift.isBusy() )) {
            while (opModeIsActive() &&
                    (robot.runtime.seconds() < timeoutS)) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Run time to %7f", robot.runtime.seconds());
                //telemetry.addData("Path2",  "Running at %7d :%7d",
                //        robot.landerLift.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.landerLift.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.landerLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(200);   // optional pause after each move
        }
    }

    public void myEncoderDrive(int direction,
                               double speed,
                               double Inches,
                               double timeoutS) {
        int newLeftTarget;
        int newRightTarget;
        int newLeftBackTarget;
        int newRightBackTarget;


        //Reset the encoder
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (direction == 0)
            {
                //Go forward
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
            } else if (direction == 1)
            {
                //Go backward
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);
            }
            else if (direction == 2)
            {
                //Strafe Right
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);

            }
            else if (direction == 3)
            {
                //Strafe Left
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int)(-1*Inches * robot.COUNTS_PER_INCH);

            }
            else
            {
                Inches = 0;
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int)(Inches * robot.COUNTS_PER_INCH);

            }

            robot.leftMotor.setTargetPosition(newLeftTarget);
            robot.rightMotor.setTargetPosition(newRightTarget);
            robot.backleftMotor.setTargetPosition(newLeftBackTarget);
            robot.backrightMotor.setTargetPosition(newRightBackTarget);

            // Turn On RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backleftMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            robot.backrightMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            robot.runtime.reset();
            robot.leftMotor.setPower(Math.abs(speed));
            robot.rightMotor.setPower(Math.abs(speed));
            robot.backleftMotor.setPower(Math.abs(speed));
            robot.backrightMotor.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (robot.runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy() )) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Running to %7d :%7d", newLeftTarget,  newRightTarget);
                telemetry.addData("Path2",  "Running at %7d :%7d",
                        robot.leftMotor.getCurrentPosition(),
                        robot.rightMotor.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.leftMotor.setPower(0);
            robot.rightMotor.setPower(0);
            robot.backleftMotor.setPower(0);
            robot.backrightMotor.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backleftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backrightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(200);   // optional pause after each move
        }
    }
    /**
     private void resetAngle()
     {
     lastAngles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

     globalAngle = 0;
     }*/

    /*
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
        /*
        private double getAngle()
        {
            // We experimentally determined the Z axis is the axis we want to use for heading angle.
            // We have to process the angle because the imu works in euler angles so the Z axis is
            // returned as 0 to +180 or 0 to -180 rolling back to -179 or +179 when rotation passes
            // 180 degrees. We detect this transition and track the total cumulative angle of rotation.

            Orientation angles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            double deltaAngle = angles.firstAngle - lastAngles.firstAngle;

            if (deltaAngle < -180)
                deltaAngle += 360;
            else if (deltaAngle > 180)
                deltaAngle -= 360;

            globalAngle += deltaAngle;

            lastAngles = angles;

            return globalAngle;
        }
        */
    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power)
    {

        // restart imu movement tracking.
        robot.resetAngle();


        if (degrees < 0)
        {   // turn right.

            robot.leftMotor.setPower(power);
            robot.rightMotor.setPower(-1*power);
            robot.backleftMotor.setPower(power);
            robot.backrightMotor.setPower(-1*power);

        }
        else if (degrees > 0)
        {   // turn left.

            robot.leftMotor.setPower(-1*power);
            robot.rightMotor.setPower(power);
            robot.backleftMotor.setPower(-1*power);
            robot.backrightMotor.setPower(power);

        }
        else return;


        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && robot.getAngle() == 0) {}

            while (opModeIsActive() && robot.getAngle() > degrees) {}
        }
        else    // left turn.
            while (opModeIsActive() && robot.getAngle() < degrees) {}

        // turn the motors off.
        power=0;
        robot.leftMotor.setPower(power);
        robot.rightMotor.setPower(power);
        robot.backleftMotor.setPower(power);
        robot.backrightMotor.setPower(power);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        robot.resetAngle();
    }
}
