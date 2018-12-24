package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class CallistoHW {

    private HardwareMap hwMap =  null;

    public DcMotor leftMotor = null;

    HardwareMap getHwMap = null;
    private ElapsedTime period = new ElapsedTime();

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap ahwMap) {
        hwMap = ahwMap;

        leftMotor         = hwMap.get(DcMotor.class, "MFrontLeft");

    }

    public void stopAllWheels()
    {
        leftMotor.setPower(0);
    }

    public void moveForward(double power)
    {
        leftMotor.setPower(power);
    }
}