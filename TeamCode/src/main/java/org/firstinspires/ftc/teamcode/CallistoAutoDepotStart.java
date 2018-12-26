package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Callisto Depot Start", group="Callisto")
//@Disabled
public class CallistoAutoDepotStart extends CallistoAutonomousBase {

    @Override
    public void runOpMode() {
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        initHW();

        // Send telemetry message to signify robot waiting;
        telemetry.addData("Status", "Resetting Encoders");    //
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        //0,2,1,3
        //unhook
        //double current_angle = getAbsoluteAngle();
        //rotate((int)(REFERENCE_ANGLE - current_angle), TURN_SPEED);

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


}
