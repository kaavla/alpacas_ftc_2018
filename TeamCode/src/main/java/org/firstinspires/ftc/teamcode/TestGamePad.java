
package org.firstinspires.ftc.teamcode;

import android.widget.Spinner;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;


@TeleOp(name="Test GamePad OpMode", group="Linear Opmode")
@Disabled
public class TestGamePad extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //  private DcMotor leftDrive = null;
    // private DcMotor rightDrive = null;

    private DcMotor temp1 = null;
    private DcMotor temp2 = null;
    private CRServo TempS1 = null;




    @Override
    public void runOpMode() {

        //double motor_power = 0.5;
        double motor_power = 0.7;
        double slow_motor_power = 0.2;
        double fast_motor_power = 0.7;
        int count = 0;
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        //Initialize motor variables
        temp1 = hardwareMap.get(DcMotor.class, "Temp1");
        temp2 = hardwareMap.get(DcMotor.class, "Temp2");
        TempS1 = hardwareMap.crservo.get("TempS1");

        temp2.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "FL(%.1f),FR(%.1f),BL(%.1f),BR(%.1f), count(%d)", motor_power, motor_power, motor_power, motor_power, count);
            telemetry.update();

            if (gamepad1.dpad_up)
            {
                //Go Forward
                temp1.setPower(1);
                temp2.setPower(1);

            }
            else if (gamepad1.dpad_down)
            {
                //Go Backward
                temp1.setPower(-1);
                temp2.setPower(-1);
            }
            else if (gamepad1.dpad_right)
            {
                temp2.setPower(0.3);
            }
                else if (gamepad1.dpad_left)
            {
                temp2.setPower(-0.3);
            }
            else if (gamepad2.left_bumper) {
                TempS1.setPower(0.79);
            }
            else if (gamepad2.right_bumper) {
                //spinnerServo.setPower(1);
                TempS1.setPower(-0.79);
            }
            else {
                temp1.setPower(0);
                temp2.setPower(0);
                TempS1.setPower(0);

            }
        }

        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "FL(%.1f),FR(%.1f),BL(%.1f),BR(%.1f), count(%d)", motor_power, motor_power, motor_power, motor_power, count);
        telemetry.update();
    }
}



