package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

public class CallistoHW {

    private HardwareMap hwMap =  null;

    public DcMotor leftMotor      = null;
    public DcMotor rightMotor     = null;
    public DcMotor backrightMotor = null;
    public DcMotor backleftMotor  = null;

    public BNO055IMU imu          = null;

    public DcMotor MLanderLift = null;

    private ElapsedTime period = new ElapsedTime();

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        leftMotor        = ahwMap.get(DcMotor.class, "MFrontLeft");
        rightMotor       = ahwMap.get(DcMotor.class, "MFrontRight");
        backleftMotor    = ahwMap.get(DcMotor.class, "MBackLeft");
        backrightMotor   = ahwMap.get(DcMotor.class, "MBackRight");
        MLanderLift      = ahwMap.get(DcMotor.class, "MLanderLift");
        imu              = ahwMap.get(BNO055IMU.class, "imu");

        //initialize the IMU
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile  = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled       = true;
        parameters.loggingTag           = "IMU";
        imu.initialize(parameters);

        //Invert direction for left motors
        leftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backleftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        // Set all motors to zero power
        stopAllMotors();

        //Set zero power behavior to braking
        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backrightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backleftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void stopAllMotors()
    {
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        backleftMotor.setPower(0);
        backrightMotor.setPower(0);
        MLanderLift.setPower(0);
    }

    public void moveForward(double power)
    {
        leftMotor.setPower(power);
        rightMotor.setPower(power);
        backleftMotor.setPower(power);
        backrightMotor.setPower(power);
    }
    public void moveBackwards(double power)
    {
        leftMotor.setPower(-1 * power);
        rightMotor.setPower(-1 * power);
        backleftMotor.setPower(-1 * power);
        backrightMotor.setPower(-1 * power);
    }
    public void turnRight(double power)
    {
        leftMotor.setPower(power);
        rightMotor.setPower(-1 * power);
        backleftMotor.setPower(power);
        backrightMotor.setPower(-1 * power);
    }
    public void turnLeft(double power)
    {
        leftMotor.setPower(-1 * power);
        rightMotor.setPower(power);
        backleftMotor.setPower(-1 * power);
        backrightMotor.setPower(power);
    }
    public void strafeRight(double power)
    {
        leftMotor.setPower(power);
        rightMotor.setPower(-1 * power);
        backleftMotor.setPower(-1 * power);
        backrightMotor.setPower(power);
    }
    public void strafeleft(double power)
    {
        leftMotor.setPower(-1 * power);
        rightMotor.setPower(power);
        backleftMotor.setPower(power);
        backrightMotor.setPower(-1 * power);
    }
    public void diagonalforwardRight(double power)
    {
        leftMotor.setPower(power);
        backrightMotor.setPower(power);
    }
    public void diagonalforwardLeft(double power)
    {
        rightMotor.setPower(power);
        backleftMotor.setPower(power);
    }
    public void diagonalbackwardsRight(double power)
    {
        rightMotor.setPower(-1 * power);
        backleftMotor.setPower(-1 * power);
    }
    public void diagonalbackwardsLeft(double power)
    {
        leftMotor.setPower(-1 * power);
        backrightMotor.setPower(-1 * power);
    }
    public void forwardSlow(double power)
    {
        leftMotor.setPower(Range.clip(leftMotor.getPower() + 0.01, 0.3, 1.0) );
        rightMotor.setPower(Range.clip(rightMotor.getPower() + 0.01, 0.3, 1.0));
        backleftMotor.setPower(Range.clip(backleftMotor.getPower() + 0.01, 0.3, 1.0));
        backrightMotor.setPower(Range.clip(backrightMotor.getPower() + 0.01, 0.3, 1.0));

    }
    public void backwardSlow(double power)
    {
        leftMotor.setPower(Range.clip(leftMotor.getPower() - 0.01, -0.3, -1.0) );
        rightMotor.setPower(Range.clip(rightMotor.getPower() - 0.01, -0.3, -1.0));
        backleftMotor.setPower(Range.clip(backleftMotor.getPower() - 0.01, -0.3, -1.0));
        backrightMotor.setPower(Range.clip(backrightMotor.getPower() - 0.01, -0.3, -1.0) );
    }
    public void landerliftUp(double power)
    {
        MLanderLift.setPower(1);
    }
    public void landerliftDown(double power)
    {
        MLanderLift.setPower(-1);
    }

}