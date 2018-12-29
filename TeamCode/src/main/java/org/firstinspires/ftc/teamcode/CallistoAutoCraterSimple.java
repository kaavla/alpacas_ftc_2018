package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name = "Callisto Crater Simple", group = "Callisto")
//@Disabled
public class CallistoAutoCraterSimple extends CallistoAutonomousBase {

    @Override
    public void runOpMode() {
        int positionGold = 2;
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

        positionGold = myTFOD(2);
        myLanderLift(0, 1, 7, 9.0);
        myDetectionTest(positionGold, DRIVE_SPEED, 40.0);
        myDetectionRun(positionGold, DRIVE_SPEED, 40.0);

        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();


    }

    private void myDetectionRun(int position,
                                double speed,
                                double timeoutS) {


        //initialized the motor encoders
        initMotorEncoders();

        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (position == 1)
            {
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 7, 10.0);
                //  rotate(30, robot.TURN_SPEED);
                //myEncoderDrive(0, robot.DRIVE_SPEED, 20, 10.0);
                //rotate(79, robot.TURN_SPEED);
                //robot.markerServo.setPosition(-0.9);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 2, 10.0);
                //myEncoderDrive(0, robot.DRIVE_SPEED, 67, 10.0);
                //rotate(-14, robot.TURN_SPEED);
                //myEncoderDrive(0, robot.DRIVE_SPEED, 8, 10.0);
            }
            else if (position == 3)
            {
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 7, 10.0);
                //rotate(-30, robot.TURN_SPEED);
                //myEncoderDrive(0, robot.DRIVE_SPEED, 10, 10.0);
                //robot.markerServo.setPosition(-0.9);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 2, 10.0);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 60, 10.0);
                //rotate(-14, robot.TURN_SPEED);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 8, 10.0);
            } else // Position = 2 also default position
            {
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 7, 40.0);
                //rotate(-45, robot.TURN_SPEED);
                //robot.markerServo.setPosition(-0.9);
                //myEncoderDrive(3, robot.DRIVE_SPEED, 2, 10.0);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 60, 10.0);
                //rotate(-14, robot.TURN_SPEED);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 13, 10.0);
            }
        }
}

