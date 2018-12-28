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
public class CallistoAutoDepotStart extends CallistoAutonomousBase
{

    @Override
    public void runOpMode()
    {
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
                                double timeoutS)
    {


        //initialized the motor encoders
        initMotorEncoders();

        // Ensure that the op mode is still active
        if (opModeIsActive())
        {

            // Determine new target position, and pass to motor controller
            if (position == 1)
            {
                myEncoderDrive(Direction.FORWARD, TURN_SPEED, 20, 10.0);
                rotate(30, TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 20, 10.0);
                rotate(79, TURN_SPEED);
                //robot.markerServo.setPosition(-0.9);
                //myEncoderDrive(1, robot.DRIVE_SPEED, 2, 10.0);
                myEncoderDrive(Direction.FORWARD, 0.95, 67, 10.0);
                //rotate(-14, TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 8, 10.0);
            } else if (position == 3)
            {
                myEncoderDrive(Direction.FORWARD, TURN_SPEED, 20, 10.0);
                rotate(-30, TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 10, 10.0);
                //roboderDt.markerServo.setPosition(-0.9);
                //myEncoderDrive(1, DRIVE_SPEED, 2, 10.0);
                myEncoderDrive(Direction.BACKWARD, 0.95, 60, 10.0);
                //rotate(-14, TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 8, 10.0);
            } else // Position = 2 also default position
            {
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 26, 10.0);
                rotate(-43, TURN_SPEED);
                //robot.markerServo.setPosition(-0.9);
                myEncoderDrive(Direction.BACKWARD, 0.95, 35, 10.0);
                //myEncoderDrive(2, 0.9, 3, 10.0);
                myEncoderDrive(Direction.BACKWARD, 0.95, 33, 10.0);
                //myEncoderDrive(1, DRIVE_SPEED, 20, 10.0);
                //rotate(-14, TURN_SPEED);
                //myEncoderDrive(1, DRIVE_SPEED, 13, 10.0);
            }

        }
    }
}
