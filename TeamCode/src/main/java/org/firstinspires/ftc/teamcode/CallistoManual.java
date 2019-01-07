package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Callisto Manual", group = "Linear Opmode")
//@Disabled

public class CallistoManual extends LinearOpMode
{
    CallistoHW robotCallisto = new CallistoHW();
    private ElapsedTime runtime = new ElapsedTime();


    @Override
    public void runOpMode()
    {
        //This is where we set our motor powers
        double motor_power = 0.7;

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        robotCallisto.init(hardwareMap);

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();

            if (gamepad1.left_stick_y < 0)
            {
                robotCallisto.moveForward(motor_power);
            }
            else if (gamepad1.left_stick_y > 0)
            {
                robotCallisto.moveBackwards(motor_power);
            }
            else if (gamepad1.left_stick_x < 0)
            {
                robotCallisto.turnLeft(motor_power);
            }
            else if (gamepad1.left_stick_x > 0)
            {
                robotCallisto.turnRight(motor_power);
            }
            else if (gamepad1.right_stick_x > 0)
            {
                robotCallisto.strafeRight(motor_power);
            }
            else if (gamepad1.right_stick_x < 0)
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
            else if (gamepad1.x)
            {
                robotCallisto.landerliftUp(1);
            }
            else if (gamepad1.b)
            {
                robotCallisto.landerliftDown(1);
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
                robotCallisto.collectionDropLiftUp(0.8);
            }
            else if (gamepad2.a)
            {
                robotCallisto.collectionDropLiftDown(0.8);
            }
            else if (gamepad2.left_bumper)
            {
                robotCallisto.turnspinnerservoforward(motor_power);
            }
            else if (gamepad2.right_bumper)
            {
                robotCallisto.turnspinnerservobacwards(motor_power);
            }
            else if (gamepad2.x)
            {
                robotCallisto.turnTraytodrop();
            }
            else if (gamepad2.b)
            {
                robotCallisto.turnTraytocollect();
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



