/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * This is NOT an opmode.
 *
 * This class can be used to define all the specific hardware for a single robot.
 * In this case that robot is a Pushbot.
 * See PushbotTeleopTank_Iterative and others classes starting with "Pushbot" for usage examples.
 *
 * This hardware class assumes the following device names have been configured on the robot:
 * Note:  All names are lower case and some have single spaces between words.
 *
 * Motor channel:  Left  drive motor:        "left_drive"
 * Motor channel:  Right drive motor:        "right_drive"
 * Motor channel:  Manipulator drive motor:  "left_arm"
 * Servo channel:  Servo to open left claw:  "left_hand"
 * Servo channel:  Servo to open right claw: "right_hand"
 */
public class HardwareAutonomous
{
    /* Public OpMode members. */
    public DcMotor  leftMotor          = null;
    public DcMotor  rightMotor         = null;
    public DcMotor  backrightMotor     = null;
    public DcMotor  backleftMotor      = null;
    public BNO055IMU imu;
    public DcMotor  landerLift           = null;
    public DcMotor  collectionLift       = null;
    public Servo markerServo = null;
    public CRServo spinnerServo = null;


    // public static final double MID_SERVO       =  0.5 ;
   // public static final double ARM_UP_POWER    =  0.45 ;
   // public static final double ARM_DOWN_POWER  = -0.45 ;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    public HardwareAutonomous(){

    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        // Define and Initialize Motors
        leftMotor         = hwMap.get(DcMotor.class, "MFrontLeft");
        rightMotor        = hwMap.get(DcMotor.class, "MFrontRight");
        backrightMotor    = hwMap.get(DcMotor.class, "MBackRight");
        backleftMotor     = hwMap.get(DcMotor.class, "MBackLeft");
        landerLift        = hwMap.get(DcMotor.class, "MLanderLift");
        collectionLift    = hwMap.get(DcMotor.class, "MCollectionLift");
        markerServo       =hwMap.get(Servo.class, "markerServo");
        spinnerServo      = hwMap.get(CRServo.class, "spinnerServo");
        imu               = hwMap.get(BNO055IMU .class, "imu");

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit            = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit            = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile  = "BNO055IMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled       = true;
        parameters.loggingTag           = "IMU";

        imu.initialize(parameters);

/*
        while (!isStopRequested() && !imu.isGyroCalibrated())
        {
            sleep(50);
            idle();
        }
*/

        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        backleftMotor.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        leftMotor.setPower(0);
        rightMotor.setPower(0);
        backrightMotor.setPower(0);
        backleftMotor.setPower(0);
        landerLift.setPower(0);
        markerServo.setPosition(1.2);

        collectionLift.setTargetPosition(0);
        spinnerServo.setPower(0);

        leftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backrightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backleftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        landerLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        collectionLift.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Set all motors to run without encoders.
        // May want to use RUN_USING_ENCODERS if encoders are installed.
        /*leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);*/

        /* Define and initialize ALL installed servos.
        leftClaw  = hwMap.get(Servo.class, "left_hand");
        rightClaw = hwMap.get(Servo.class, "right_hand");
        leftClaw.setPosition(MID_SERVO);
        rightClaw.setPosition(MID_SERVO);*/
    }
 }

