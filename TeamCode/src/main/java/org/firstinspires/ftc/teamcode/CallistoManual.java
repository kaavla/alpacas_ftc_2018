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
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //  private DcMotor leftDrive = null;
    // private DcMotor rightDrive = null;


    @Override
    public void runOpMode()
    {

        //double motor_power = 0.5;
        double motor_power = 0.7;
        double slow_motor_power = 0.2;
        double fast_motor_power = 0.7;
        int count = 0;
        telemetry.addData("Status", "Initialized");
        telemetry.update();
        robotCallisto.init(hardwareMap);

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive())
        {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "FL(%.1f),FR(%.1f),BL(%.1f),BR(%.1f), count(%d)", motor_power, motor_power, motor_power, motor_power, count);
            telemetry.update();


            if (gamepad1.left_stick_y < 0)
            {
                robotCallisto.moveForward(motor_power);
            } else if (gamepad1.left_stick_y > 0)
            {
                robotCallisto.moveBackwards(motor_power);
            } else if (gamepad1.left_stick_x < 0)
            {
                robotCallisto.turnLeft(motor_power);
            } else if (gamepad1.left_stick_x > 0)
            {
                robotCallisto.turnRight(motor_power);
            } else if (gamepad1.right_stick_x > 0)
            {
                robotCallisto.strafeRight(motor_power);
            } else if (gamepad1.right_stick_x < 0)
            {
                robotCallisto.strafeleft(motor_power);
            } else if (gamepad1.right_bumper)
            {
                robotCallisto.diagonalforwardRight(motor_power);
            } else if (gamepad1.left_bumper)
            {
                robotCallisto.diagonalforwardLeft(motor_power);
            } else if (gamepad1.left_trigger > 0.7)
            {
                robotCallisto.diagonalbackwardsLeft(motor_power);
            } else if (gamepad1.right_trigger > 0.7)
            {
                robotCallisto.diagonalbackwardsRight(motor_power);
            } else if (gamepad1.y)
            {
                robotCallisto.forwardSlow(slow_motor_power);
            } else if (gamepad1.a)
            {
                robotCallisto.backwardSlow(slow_motor_power);
            } else if (gamepad1.left_trigger> 0.7)
            {
                robotCallisto.landerliftUp(1);
            } else if (gamepad1.right_trigger > 0.7)
            {
                robotCallisto.landerliftDown(-1);
            }
             else
            {
                robotCallisto.stopAllMotors();
            }
        }
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "FL(%.1f),FR(%.1f),BL(%.1f),BR(%.1f), count(%d)", motor_power, motor_power, motor_power, motor_power, count);
        telemetry.update();
    }
}



