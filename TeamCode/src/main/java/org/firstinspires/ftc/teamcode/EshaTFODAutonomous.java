
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import org.firstinspires.ftc.robotcontroller.external.samples.HardwarePushbot;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

@Autonomous(name="EshaTFODAutonomous", group="Esha")
//@Disabled
public class EshaTFODAutonomous extends LinearOpMode {

    /* Declare OpMode members. */
    HardwareAutonomous robot = new HardwareAutonomous();   // Use a Pushbot's hardware
    private ElapsedTime runtime = new ElapsedTime();

    /*
        static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
        static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
        static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
        static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);
        static final double DRIVE_SPEED = 1.0;
        static final double TURN_SPEED = 0.6;
        static final double ROTATIONS_PER_INCH = 11.42;
        static final double TICKS_PER_INCH = (ROTATIONS_PER_INCH * COUNTS_PER_MOTOR_REV); //may need to update Counts per motor rev because the motor may have a different amount of ticks
        aprivate Servo markerServo = null;
        Orientation lastAngles = new Orientation();
        double globalAngle, power = .30, correction;
    */
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

        robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //robot.landerLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backleftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robot.backrightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
       // robot.landerLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful Encoder reset;
        telemetry.addData("MotorTelemetry", "Starting at %7d :%7d :%7d :%7d",
                robot.leftMotor.getCurrentPosition(),
                robot.rightMotor.getCurrentPosition(),
                robot.backleftMotor.getCurrentPosition(),
                robot.backrightMotor.getCurrentPosition());
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //0,1,2,3
        //0 - forward
        //1 - backward
        //2 - strafe right
        //3 - strafe left

        //myLanderLift(4, 1, 6, 7.0);
        myEncoderDrive(3, robot.DRIVE_SPEED, 18, 7.0);
        myEncoderDrive(1, robot.DRIVE_SPEED, 13, 7.0);
        myDetectionTF(1);
        myEncoderDrive(1, robot.DRIVE_SPEED, 18, 7.0);
        rotate(90, robot.TURN_SPEED);
        myEncoderDrive(1, robot.DRIVE_SPEED, 66, 10.0);
        myEncoderDrive(0, robot.DRIVE_SPEED, 97, 10.0);
       /* myEncoderDrive(0, robot.DRIVE_SPEED, 30, 10.0);
        rotate(107, robot.TURN_SPEED);
        myEncoderDrive(0, robot.DRIVE_SPEED, 84, 10.0);
        rotate(5, robot.TURN_SPEED);
        robot.markerServo.setPosition(-0.9);
        myEncoderDrive(3, robot.DRIVE_SPEED, 9, 5.0);
        rotate(7, robot.TURN_SPEED);
        myEncoderDrive(1, robot.DRIVE_SPEED, 70, 10.0);
        rotate(15, robot.TURN_SPEED);
        myEncoderDrive(1, robot.DRIVE_SPEED, 13, 10.0);
        myLanderLift(5, robot.TURN_SPEED, 6, 7.25); */
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

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (direction == 0) {
                //Go forward
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
            } else if (direction == 1) {
                //Go backward
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
            } else if (direction == 2) {
                //Strafe Right
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
            } else if (direction == 3) {
                //Strafe Left
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (-1 * Inches * robot.COUNTS_PER_INCH);
            } else {
                Inches = 0;
                newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
                newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * robot.COUNTS_PER_INCH);
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
                    (robot.leftMotor.isBusy())) {

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

            sleep(200);   // optional pause after each move
        }
    }
/*
    public void resetAngle() {
        lastAngles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        globalAngle = 0;
    }
    /**
     * Get current cumulative angle rotation from last reset.
     * @return Angle in degrees. + = left, - = right.
     */
    /**?
     public double getAngle() {
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
     }*/
    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     *
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power) {

        // restart imu movement tracking.
        class resetAngle {
        }
        if (degrees < 0) {   // turn right.

            robot.leftMotor.setPower(power);
            robot.rightMotor.setPower(-1 * power);
            robot.backleftMotor.setPower(power);
            robot.backrightMotor.setPower(-1 * power);

        } else if (degrees > 0) {   // turn left.

            robot.leftMotor.setPower(-1 * power);
            robot.rightMotor.setPower(power);
            robot.backleftMotor.setPower(-1 * power);
            robot.backrightMotor.setPower(power);

        } else return;


        // rotate until turn is completed.
        if (degrees < 0) {
            // On right turn we have to get off zero first.
            while (opModeIsActive() && robot.getAngle() == 0) {
            }

            while (opModeIsActive() && robot.getAngle() > degrees) {
            }
        } else    // left turn.
            while (opModeIsActive() && robot.getAngle() < degrees) {
            }

        // turn the motors off.
        power = 0;
        robot.leftMotor.setPower(power);
        robot.rightMotor.setPower(power);
        robot.backleftMotor.setPower(power);
        robot.backrightMotor.setPower(power);

        // wait for rotation to stop.
        sleep(1000);

        // reset angle tracking on new heading.
        robot.resetAngle();
    }

    /*private void myLanderLift(int direction,
                              double speed,
                              double Inches,
                              double timeoutS) {
        int newLanderLiftTarget;


        //Reset the encoder
        robot.landerLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the opmode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (direction == 4) {
                //Go up
                newLanderLiftTarget = robot.landerLift.getCurrentPosition() + (int) (Inches * robot.TICKS_PER_INCH);
            } else if (direction == 5) {
                //Go down
                newLanderLiftTarget = robot.landerLift.getCurrentPosition() + (int) (Inches - 1 * robot.TICKS_PER_INCH);

            } else {
                Inches = 0;
                newLanderLiftTarget = robot.landerLift.getCurrentPosition() + (int) (Inches * robot.TICKS_PER_INCH);
            }

            robot.landerLift.setTargetPosition(newLanderLiftTarget);

            // Turn On RUN_TO_POSITION
            robot.landerLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robot.landerLift.setPower(Math.abs(speed));

            // keep looping while we are still active, and there is time left, and both motors are running.
            // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
            // its target position, the motion will stop.  This is "safer" in the event that the robot will
            // always end the motion as soon as possible.
            // However, if you require that BOTH motors have finished their moves before the robot continues
            // onto the next step, use (isBusy() || isBusy()) in the loop test.
            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (robot.landerLift.isBusy())) {

                // Display it for the driver.
                //telemetry.addData("Path1", "Running to %7d :%7d", newLanderLiftTarget);
                //telemetry.addData("Path2", "Running at %7d :%7d",
                //        robot.landerLift.getCurrentPosition(),
                //telemetry.update());
            }

            // Stop all motion;
            robot.landerLift.setPower(0);

            // Turn off RUN_TO_POSITION
            robot.landerLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(200);   // optional pause after each move*/

    private void myDetectionTF(int position) {
        int newDetectionTFTarget;


        //Reset the encoder
        // robot.landerLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the opmode is still active
        //if (opModeIsActive()) {

        // Determine new target position, and pass to motor controller
        if (position == 1) {
            //If GOLD MINERAL is on the left
            myEncoderDrive(3, 0.5, 11, 5.0);
            myEncoderDrive(2, 0.5, 13, 5.0);
        } else if (position == 2) {
            //if GOLD MINERAL is in the center
            myEncoderDrive(1, 0.5, 20, 10.0);
            myEncoderDrive(4, 0.5, 12, 10.0);
            myEncoderDrive(1, 0.5, 4, 5.0);
        } else if (position == 3) {
            //if GOLD MINERAL is on the right
            myEncoderDrive(2, 2, 2, 2);
        }


        //robot.setTargetPosition(newDetectionTFTarget);
        // Turn On RUN_TO_POSITION
        //robot.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        // reset the timeout time and start motion.
        runtime.reset();
        //robot.setPower(Math.abs(speed));

        // keep looping while we are still active, and there is time left, and both motors are running.
        // Note: We use (isBusy() && isBusy()) in the loop test, which means that when EITHER motor hits
        // its target position, the motion will stop.  This is "safer" in the event that the robot will
        // always end the motion as soon as possible.
        // However, if you require that BOTH motors have finished their moves before the robot continues
        // onto the next step, use (isBusy() || isBusy()) in the loop test.
        // while (opModeIsActive() &&
        //        (runtime.seconds() < timeoutS) &&
        //      (robot.landerLift.isBusy())) {

        // Display it for the driver.
        //telemetry.addData("Path1", "Running to %7d :%7d", newLanderLiftTarget);
        //telemetry.addData("Path2", "Running at %7d :%7d",
        //        robot.landerLift.getCurrentPosition(),
        //telemetry.update());


        // Stop all motion;
        // robot.landerLift.setPower(0);

        // Turn off RUN_TO_POSITION
        //robot.landerLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // sleep(200);   // optional pause after each move
    }
}