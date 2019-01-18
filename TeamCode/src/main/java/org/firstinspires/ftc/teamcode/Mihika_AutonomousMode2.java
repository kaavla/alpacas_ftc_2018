package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

    @Autonomous(name = "AutonomousTestMihika2", group = "Mihika")
//@Disabled

    public class Mihika_AutonomousMode2 extends LinearOpMode {

        /* Declare OpMode members. */
        HardwareAutonomous robot = new HardwareAutonomous();   // Use a Pushbot's hardware
        private ElapsedTime runtime = new ElapsedTime();

        static final double COUNTS_PER_MOTOR_REV = 1440;    // eg: TETRIX Motor Encoder
        static final double DRIVE_GEAR_REDUCTION = 1.0;     // This is < 1.0 if geared UP
        static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
        static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                (WHEEL_DIAMETER_INCHES * 3.1415);
        static final double DRIVE_SPEED = 0.5;
        static final double TURN_SPEED = 0.2;
        private Servo markerServo = null;
        static final double     REFERENCE_ANGLE           = 165;
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

            markerServo = hardwareMap.servo.get("markerServo");

            robot.leftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.backrightMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            robot.backleftMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            robot.leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backleftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            robot.backrightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            // Send telemetry message to indicate successful Encoder reset;
            telemetry.addData("MotorTelemetry", "Starting at %7d :%7d :%7d :%7d",
                    robot.leftMotor.getCurrentPosition(),
                    robot.rightMotor.getCurrentPosition(),
                    robot.backleftMotor.getCurrentPosition(),
                    robot.backrightMotor.getCurrentPosition());
            telemetry.update();

            // Wait for the game to start (driver presses PLAY)
            waitForStart();

            //0,2,1,3
            //unhook
            double current_angle = getAbsoluteAngle();
            rotate((int)(REFERENCE_ANGLE - current_angle), TURN_SPEED);

            /*
            myEncoderDrive(2, DRIVE_SPEED, 4, 5.0);
            // 24
            myEncoderDrive(0, DRIVE_SPEED, 30, 5.0);
            rotate(80, TURN_SPEED);
            myEncoderDrive(0, DRIVE_SPEED, 100, 5.0);
            // 91
            markerServo.setPosition(0.9);
            rotate(40, TURN_SPEED);
            myEncoderDrive(1, DRIVE_SPEED, 146, 5.0);
            rotate(25, TURN_SPEED);
            myEncoderDrive(1, DRIVE_SPEED, 10, 7.0);
            //park in crater
            */

/*
        myEncoderDrive(0, DRIVE_SPEED, 24, 24,5.0);
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
                if (direction == 0) {
                    //Go forward
                    newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                    newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                    newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                    newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                } else if (direction == 1) {
                    //Go backward
                    newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                    newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                    newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                    newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                } else if (direction == 2) {
                    //Strafe Right
                    newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                    newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                    newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                    newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);

                } else if (direction == 3) {
                    //Strafe Left
                    newLeftTarget = robot.rightMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);
                    newRightTarget = robot.leftMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                    newLeftBackTarget = robot.backrightMotor.getCurrentPosition() + (int) (Inches * COUNTS_PER_INCH);
                    newRightBackTarget = robot.backleftMotor.getCurrentPosition() + (int) (-1 * Inches * COUNTS_PER_INCH);

                } else {
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

        private void resetAngle() {
            lastAngles = robot.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);

            globalAngle = 0;
        }

        /**
         * Get current cumulative angle rotation from last reset.
         *
         * @return Angle in degrees. + = left, - = right.
         */
        private double getAngle() {
            return 0;
        }
        private double getAbsoluteAngle()
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

        /**
         * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
         *
         * @param degrees Degrees to turn, + is left - is right
         */
        private void rotate(int degrees, double power) {

            // restart imu movement tracking.
            resetAngle();


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
                while (opModeIsActive() && getAngle() == 0) {
                }

                while (opModeIsActive() && getAngle() > degrees) {
                }
            } else    // left turn.
                while (opModeIsActive() && getAngle() < degrees) {
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
            resetAngle();
        }
    }