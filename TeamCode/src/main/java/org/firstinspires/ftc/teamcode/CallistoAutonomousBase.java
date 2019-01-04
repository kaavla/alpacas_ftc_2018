package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

enum Direction
{
    FORWARD, BACKWARD, STRAFE_RIGHT, STRAFE_LEFT;
}

//@Autonomous(name="CallistoAutonomousBase", group="Callisto")
//@Disabled
public class CallistoAutonomousBase extends LinearOpMode
{

    public CallistoHW robot = new CallistoHW();
    private ElapsedTime runtime = new ElapsedTime();
    private Orientation lastAngles = new Orientation();
    private double globalAngle = 0;
    public Direction direction;

    static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) / (WHEEL_DIAMETER_INCHES * 3.1415);

    //Need to Change
    static final double ROTATIONS_PER_INCH = 11.42;
    static final double TICKS_PER_INCH = (ROTATIONS_PER_INCH * 1120);
    //Need to Change

    static final double DRIVE_SPEED = 0.7;
    static final double TURN_SPEED = 0.8;

    final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    final String LABEL_GOLD_MINERAL = "Gold Mineral";
    final String LABEL_SILVER_MINERAL = "Silver Mineral";
    final String VUFORIA_KEY = "AUWa2hP/////AAABmYAEN4JY30WlndAsvgcYjZAcwt/KX4c9VUt+Br3zZxPhIbJ+ovlQrV3YlETOwJ4Q5NajUuwkdpnX2292snWM8iiXzQ2Nm37xl78r82PlDZPAKP8XV+9sBg1KMHO+0zDzTtWNa/fmNPeEhmdff/YWUzcqTmGLnccOhj57waivZa4Y5xDfH4YKssYJUNQKumOd8v5m90IEKYgWghs7BhxpWfQbjzC+3QUKPc7q34V+9W4xQ+2S+hI0inYLrK4rSdiCGU76d8hwlBWuDC8PWrkqwIi6EptTL/nP1rLoWy/Usv6ZUqRRHwkgLNYsrWusN0G5d71F6tdvRDbGdgQKQ2evHWZPPtlVW6u0S5N5S2sXu7R+";
    public TFObjectDetector tfod = null;
    public VuforiaLocalizer vuforia = null;

    @Override
    public void runOpMode()
    {
        //Empty Function
    }

    public void initHW()
    {
        robot.init(hardwareMap);
        initMotorEncoders();
        initVuforia();

        if (ClassFactory.getInstance().canCreateTFObjectDetector())
        {
            initTfod();
        } else
        {
            telemetry.addData("Sorry!", "This device is not compatible with TFOD");
        }
        if (tfod != null)
        {
            tfod.activate();
        }
    }

    private void initVuforia()
    {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

    }

    private void initTfod()
    {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }

    public void initMotorEncoders()
    {
        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    private void resetAngle()
    {
        lastAngles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }

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

    public void rotate(int degrees, double power)
    {

        // restart imu movement tracking.
        resetAngle();

        if (degrees < 0)
        {   // turn right.
            robot.turnRight(power);
        } else if (degrees > 0)
        {   // turn left.
            robot.turnLeft(power);
        } else return;


        // rotate until turn is completed.
        if (degrees < 0)
        {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && getAngle() == 0)
            {
            }

            while (opModeIsActive() && getAngle() > degrees)
            {
            }
        } else    // left turn.
            while (opModeIsActive() && getAngle() < degrees)
            {
            }

        // turn the motors off.
        power = 0;
        robot.leftMotor.setPower(power);
        robot.rightMotor.setPower(power);
        robot.backleftMotor.setPower(power);
        robot.backrightMotor.setPower(power);

        // wait for rotation to stop.
        sleep(50);

        // reset angle tracking on new heading.
        resetAngle();
    }


    public int myTFOD(double timeoutS)
    {
        int positionGold = 2;
        runtime.reset();

        while (opModeIsActive() &&
                (runtime.seconds() < timeoutS))
        {
            if (tfod != null)
            {
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null)
                {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 3)
                    {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions)
                        {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
                            {
                                goldMineralX = (int) recognition.getLeft();
                            } else if (silverMineral1X == -1)
                            {
                                silverMineral1X = (int) recognition.getLeft();
                            } else
                            {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }
                        if (goldMineralX != -1 && silverMineral1X != -1 && silverMineral2X != -1)
                        {
                            if (goldMineralX < silverMineral1X && goldMineralX < silverMineral2X)
                            {
                                telemetry.addData("Gold Mineral Position", "Left");
                                positionGold = 3;
                                break;
                            } else if (goldMineralX > silverMineral1X && goldMineralX > silverMineral2X)
                            {
                                telemetry.addData("Gold Mineral Position", "Right");
                                positionGold = 1;
                                break;
                            } else
                            {
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

        if (tfod != null)
        {
            tfod.shutdown();
        }
        return positionGold;
    }


    public void myEncoderDrive(Direction direction,
                               double speed,
                               double Inches,
                               double timeoutS)
    {
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
        if (opModeIsActive())
        {

            // Determine new target position, and pass to motor controller
            if (direction == Direction.FORWARD)
            {
                //Go forward
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
            } else if (direction == Direction.BACKWARD)
            {
                //Go backward
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
            } else if (direction == Direction.STRAFE_RIGHT)
            {
                //Strafe Right
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);

            } else if (direction == Direction.STRAFE_LEFT)
            {
                //Strafe Left
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);

            } else
            {
                Inches = 0;
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
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
            runtime.reset();

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
                    (runtime.seconds() < timeoutS) &&
                    (robot.leftMotor.isBusy()))
            {

                // Display it for the driver.
                telemetry.addData("Path1", "Running to %7d :%7d", newLeftTarget, newRightTarget);
                telemetry.addData("Path2", "Running at %7d :%7d",
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

            sleep(50);   // optional pause after each move
        }
    }

    public void myLanderLift (double direction,
                               double speed,
                               double Inches,
                               double timeoutS) {
        int newLiftTarget;


        //Reset the encoder
        robot.MLanderLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (direction == 0)
            {
                //Go down
                newLiftTarget = robot.MLanderLift.getCurrentPosition() + (int)(Inches * TICKS_PER_INCH);
            } else if (direction == 1) {
                //Go down
                newLiftTarget = robot.MLanderLift.getCurrentPosition() + (int) (-1 * Inches * TICKS_PER_INCH);
            }
            else
            {
                Inches = 0;
                newLiftTarget = robot.MLanderLift.getCurrentPosition() + (int)(Inches * TICKS_PER_INCH);
            }

            robot.MLanderLift.setTargetPosition(newLiftTarget);

            // Turn On RUN_TO_POSITION
            robot.MLanderLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.MLanderLift.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            //while (opModeIsActive() &&
            //        (runtime.seconds() < timeoutS) &&
            //       (robot.MLanderLift.isBusy() )) {
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS)) {

                // Display it for the driver.
                telemetry.addData("Path1",  "Run time to %7f", runtime.seconds());
                //telemetry.addData("Path2",  "Running at %7d :%7d",
                //        robot.MLanderLift.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robot.MLanderLift.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.MLanderLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //sleep(50);   // optional pause after each move
        }
    }


    public void myDetectionTest(int position,
                                double speed,
                                double timeoutS)
    {

        // Ensure that the op mode is still active
        if (opModeIsActive())
        {
            myLanderLift(0, 1, 7.25, 10.0);
            // Determine new target position, and pass to motor controller
            if (position == 1)
            {
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 16, 10.0);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 15, 10.0);
                rotate(80, TURN_SPEED);
                //myEncoderDrive(Direction.FORWARD, TURN_SPEED, 13, 10.0);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 11, 10.0);
            }
            else if (position == 3)
            {
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 16, 10.0);
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 15, 10.0);
                rotate(70, TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, TURN_SPEED, 13, 10.0);
            } else // Position = 2 and default position
            {
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 16, 10.0);
                rotate(82 , TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 15, 10.0);
            }




        }
    }
}