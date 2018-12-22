
package org.firstinspires.ftc.robotcontroller.external.samples;

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

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;


@TeleOp(name="M_A_Gamepad Linear OpMode", group="Linear Opmode")
//@Disabled
public class AlphaGamepad extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    //  private DcMotor leftDrive = null;
    // private DcMotor rightDrive = null;

    private DcMotor leftMotor = null;
    private DcMotor rightMotor = null;
    private DcMotor backrightMotor = null;
    private DcMotor backleftMotor = null;
    private DcMotor MCollectionSlide = null;
    private DcMotor MCollectionLift = null;
    private DcMotor MDropLift = null;
    private DcMotor MLanderLift = null;
    private CRServo spinnerServo = null;
    private Servo trayServo = null;
    private Servo markerServo = null;





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
        leftMotor = hardwareMap.get(DcMotor.class, "MFrontLeft");
        rightMotor = hardwareMap.get(DcMotor.class, "MFrontRight");
        backleftMotor = hardwareMap.get(DcMotor.class, "MBackLeft");
        backrightMotor = hardwareMap.get(DcMotor.class, "MBackRight");
        MCollectionSlide = hardwareMap.get(DcMotor.class, "MCollectionSlide");
        MCollectionLift = hardwareMap.get(DcMotor.class, "MCollectionLift");
        MDropLift = hardwareMap.get(DcMotor.class, "MDropLift");
        MLanderLift = hardwareMap.get(DcMotor.class, "MLanderLift");
        spinnerServo = hardwareMap.crservo.get("spinnerServo");
        trayServo = hardwareMap.servo.get("trayServo");
        markerServo = hardwareMap.servo.get("markerServo");



        //Set default reverse for right motors to account for inverse motors
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backleftMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "FL(%.1f),FR(%.1f),BL(%.1f),BR(%.1f), count(%d)", motor_power, motor_power, motor_power, motor_power, count);
            telemetry.update();

            if (gamepad1.dpad_up) {
                //Go Forward
                leftMotor.setPower(motor_power);
                rightMotor.setPower(motor_power);
                backleftMotor.setPower(motor_power);
                backrightMotor.setPower(motor_power);
            } else if (gamepad1.dpad_down) {
                //Go Backward
                leftMotor.setPower(-1 * motor_power);
                rightMotor.setPower(-1 * motor_power);
                backleftMotor.setPower(-1 * motor_power);
                backrightMotor.setPower(-1 * motor_power);
            } else if (gamepad1.dpad_right) {
                //Rotate Right on Axis
                leftMotor.setPower(motor_power);
                rightMotor.setPower(-1 * motor_power);
                backleftMotor.setPower(motor_power);
                backrightMotor.setPower(-1 * motor_power);
            } else if (gamepad1.dpad_left) {
                //Rotate Left on axis
                leftMotor.setPower(-1 * motor_power);
                rightMotor.setPower(motor_power);
                backleftMotor.setPower(-1 * motor_power);
                backrightMotor.setPower(motor_power);
            } else if (gamepad1.b) {
                //Strafe Right
                leftMotor.setPower(fast_motor_power);
                rightMotor.setPower(-1 * fast_motor_power);
                backleftMotor.setPower(-1 * fast_motor_power);
                backrightMotor.setPower(fast_motor_power);
            } else if (gamepad1.x) {
                //Strafe Left
                backleftMotor.setPower(fast_motor_power);
                leftMotor.setPower(-1 * fast_motor_power);
                rightMotor.setPower(fast_motor_power);
                backrightMotor.setPower(-1 * fast_motor_power);
            } else if (gamepad1.right_bumper) {
                //right diagonal forward
                leftMotor.setPower(motor_power);
                backrightMotor.setPower(motor_power);
            } else if (gamepad1.left_bumper) {
                //forward diagonal left
                rightMotor.setPower(motor_power);
                backleftMotor.setPower(motor_power);
            } else if (gamepad1.right_stick_button) {
                //right diagonal backward
                rightMotor.setPower(-1 * motor_power);
                backleftMotor.setPower(-1 * motor_power);
            } else if (gamepad1.left_stick_button) {
                //left diagonal backward
                leftMotor.setPower(-1 * motor_power);
                backrightMotor.setPower(-1 * motor_power);
            }else if (gamepad1.y) {
                //Go Forward Slowly
                leftMotor.setPower(Range.clip(leftMotor.getPower() + 0.01, -1.0, 1.0) );
                rightMotor.setPower(Range.clip(rightMotor.getPower() + 0.01, -1.0, 1.0));
                backleftMotor.setPower(Range.clip(backleftMotor.getPower() + 0.01, -1.0, 1.0));
                backrightMotor.setPower(Range.clip(backrightMotor.getPower() + 0.01, -1.0, 1.0));

                //go up
                //markerServo.setPosition(1.2);
            }else if (gamepad1.a) {
                //Go backward Slowly
                //leftMotor.setPower(-1*Range.clip(leftMotor.getPower() + 0.01, -1.0, 1.0) );
                //rightMotor.setPower(-1*Range.clip(rightMotor.getPower() + 0.01, -1.0, 1.0));
                //backleftMotor.setPower(-1*Range.clip(backleftMotor.getPower() + 0.01, -1.0, 1.0));
                //backrightMotor.setPower(-1*Range.clip(backrightMotor.getPower() + 0.01, -1.0, 1.0) );

                leftMotor.setPower(Range.clip(leftMotor.getPower() - 0.01, -1.0, 1.0) );
                rightMotor.setPower(Range.clip(rightMotor.getPower() - 0.01, -1.0, 1.0));
                backleftMotor.setPower(Range.clip(backleftMotor.getPower() - 0.01, -1.0, 1.0));
                backrightMotor.setPower(Range.clip(backrightMotor.getPower() - 0.01, -1.0, 1.0) );

                //go down
                //markerServo.setPosition(-0.9);

            }
            else if (gamepad2.left_stick_button)
            {
                MLanderLift.setPower(1);
            }
            else if (gamepad2.right_stick_button)
            {
                MLanderLift.setPower(-1);
            }
            else if (gamepad2.dpad_up) {
                MCollectionSlide.setPower(-0.6);
            }
            else if (gamepad2.dpad_down) {
                MCollectionSlide.setPower(0.6);
            }
            else if (gamepad2.dpad_left) {
                //going down
                //MCollectionLift.setPower(0.4);
                MCollectionLift.setPower(0.5);
            }
            else if (gamepad2.dpad_right) {
                //going up
                //MCollectionLift.setPower(-0.4);
                MCollectionLift.setPower(-0.6);
            }
            else if (gamepad2.y) {
                MDropLift.setPower(0.6);
            }
            else if (gamepad2.a) {
                MDropLift.setPower(-0.6);
            }
            else if (gamepad2.left_bumper) {
                spinnerServo.setPower(0.79);
            }
            else if (gamepad2.right_bumper) {
                //spinnerServo.setPower(1);
                spinnerServo.setPower(-0.79);
            }
            else if (gamepad2.x) {
                trayServo.setPosition(0.7);
            }
            else if (gamepad2.b) {
                trayServo.setPosition(0);
            }
            else {
                leftMotor.setPower(0);
                rightMotor.setPower(0);
                backleftMotor.setPower(0);
                backrightMotor.setPower(0);

                MCollectionLift.setPower(0);
                MCollectionSlide.setPower(0);
                MDropLift.setPower(0);
                spinnerServo.setPower(0);
                //trayServo.setPosition(0);
                MLanderLift.setPower(0);

            }
        }

                telemetry.addData("Status", "Run Time: " + runtime.toString());
                telemetry.addData("Motors", "FL(%.1f),FR(%.1f),BL(%.1f),BR(%.1f), count(%d)", motor_power, motor_power, motor_power, motor_power, count);
                telemetry.update();
    }
}



