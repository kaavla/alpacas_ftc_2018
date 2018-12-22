package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="AutonomousTestMihika", group="Mihika")
//@Disabled

public class Mihika_AutonomousMode extends LinearOpMode {

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

            robot.markerServo = hardwareMap.servo.get("markerServo");

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

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            //0,2,1,3
            //unhook
            myLanderLift(0, 1, 6, 7.0);
            //rotate(-7, TURN_SPEED);
            //may need to change time to 6.5
            //may need to change inches/distance once ticks per inch is changed
            myEncoderDrive(3, 0.3, 5, 5.0);
            myEncoderDrive(0, robot.DRIVE_SPEED, 63, 5.0);
            //markerServo.setPosition(0.9);
            //myLanderLift(2, 0.6, 5, 0.5);
            //spinnerServo.setPower(0.79);
            rotate(20, robot.TURN_SPEED);
            robot.markerServo.setPosition(-0.9);
            rotate(10, robot.TURN_SPEED);
            myEncoderDrive(3, 0.3, 5, 5.0);
            myLanderLift(1, 1, 1, 1);
            //40
            myEncoderDrive(1, robot.DRIVE_SPEED, 83, 5.0);
            //might need to change length moving above
            rotate(10, robot.TURN_SPEED);
            myEncoderDrive(1, robot.DRIVE_SPEED, 13,7.0);
            //park in crater
            myLanderLift(1, 1, 6, 7.0);

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



