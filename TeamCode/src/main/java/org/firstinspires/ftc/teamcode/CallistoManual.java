package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

@TeleOp(name = "Callisto Manual", group = "Linear Opmode")
//@Disabled

public class CallistoManual extends LinearOpMode
{
    CallistoHW robotCallisto = new CallistoHW();
    private ElapsedTime runtime = new ElapsedTime();


    public void myLanderLiftTest (int direction,
                                  double speed,
                                  double Inches,
                                  double timeoutS) {
        int newLiftTarget = 0;

        robotCallisto.MLanderLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        robotCallisto.MLanderLift.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (direction == 0)
            {                //Go Up
                newLiftTarget = robotCallisto.MLanderLift.getCurrentPosition() + (int)(Inches * 11.42 * 1120);
            } else if (direction == 1) {
                //Go down
                newLiftTarget = robotCallisto.MLanderLift.getCurrentPosition() + (int) (-1 * Inches * 11.42 * 1120);
            }
            telemetry.addData("Path1",  "newLiftTarget %7d", newLiftTarget);
            telemetry.addData("Path2",  "Curr Pos at %7d",
                    robotCallisto.MLanderLift.getCurrentPosition());
            telemetry.update();

            robotCallisto.MLanderLift.setTargetPosition(newLiftTarget);

            // Turn On RUN_TO_POSITION
            robotCallisto.MLanderLift.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            // reset the timeout time and start motion.
            runtime.reset();
            robotCallisto.MLanderLift.setPower(Math.abs(speed));

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
                telemetry.addData("Path2",  "Running at %7d ",
                        robotCallisto.MLanderLift.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion;
            robotCallisto.MLanderLift.setPower(0);

            // Turn off RUN_TO_POSITION
            robotCallisto.MLanderLift.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            //sleep(50);   // optional pause after each move
        }
    }



    @Override
    public void runOpMode()
    {
        //This is where we set our motor powers
        double motor_power = 0.7;

        robotCallisto.init(hardwareMap);

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            if (gamepad1.dpad_up)
            {
                robotCallisto.moveForward(motor_power);
            }
            else if (gamepad1.dpad_down)
            {
                robotCallisto.moveBackwards(motor_power);
            }
            else if (gamepad1.dpad_left)
            {
                robotCallisto.turnLeft(motor_power);
            }
            else if (gamepad1.dpad_right)
            {
                robotCallisto.turnRight(motor_power);
            }
            else if (gamepad1.b)
            {
                robotCallisto.strafeRight(motor_power);
            }
            else if (gamepad1.x)
            {
                robotCallisto.strafeleft(motor_power);
            }
            else if (gamepad1.right_bumper)
            {
                robotCallisto.diagonalforwardRight(motor_power);
            }
            else if (gamepad1.left_bumper)
            {
                robotCallisto.diagonalforwardLeft(motor_power);
            }
            else if (gamepad1.left_trigger > 0.7)
            {
                robotCallisto.diagonalbackwardsLeft(motor_power);
            }
            else if (gamepad1.right_trigger > 0.7)
            {
                robotCallisto.diagonalbackwardsRight(motor_power);
            }
            else if (gamepad1.y)
            {
                robotCallisto.forwardSlow();
            }
            else if (gamepad1.a)
            {
                robotCallisto.backwardSlow();
            }
            else if (gamepad1.left_stick_button)
            {
                robotCallisto.landerliftUp(1);
                //myLanderLiftTest(0, 0.5, 5, 2);
            }
            else if (gamepad1.right_stick_button)
            {
                robotCallisto.landerliftDown(1);
                //myLanderLiftTest(1, 0.5, 5, 2);

            }
            else if (gamepad2.dpad_up)
            {
                robotCallisto.collectionSlideIn(0.7);
            }
            else if (gamepad2.dpad_down)
            {
                robotCallisto.collectionSlideOut(0.7);
            }
            else if (gamepad2.dpad_left)
            {
                robotCallisto.collectionLiftDown(0.7);
            }
            else if (gamepad2.dpad_right)
            {
                robotCallisto.collectionLiftUp(0.7);
            }
            else if (gamepad2.y)
            {
                robotCallisto.collectionDropLiftUp(0.9);
            }
            else if (gamepad2.a)
            {
                robotCallisto.collectionDropLiftDown(0.9);
            }
            else if (gamepad2.left_bumper)
            {
                robotCallisto.turnspinnerservobacwards(motor_power);
            }
            else if (gamepad2.right_bumper)
            {
                robotCallisto.turnspinnerservoforward(motor_power);
            }
            else if (gamepad2.b)
            {
                robotCallisto.turnTraytodrop();
            }
            else if (gamepad2.x)
            {
                robotCallisto.turnTraytocollect();
            }
            else if (gamepad2.left_stick_button)
            {
                robotCallisto.turnMarkerServotoInitPos();
            }
            else if (gamepad2.right_stick_button)
            {
                robotCallisto.turnMarkerServotoDrop();
            }
            else
            {
                robotCallisto.stopAllMotors();
            }

        }
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.update();
    }
}



