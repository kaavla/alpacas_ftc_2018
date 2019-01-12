package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import static org.firstinspires.ftc.teamcode.Direction.ROBOT_DOWN;

@Autonomous(name="Callisto Depot Start", group="Callisto")
//@Disabled
public class CallistoAutoDepotStart extends CallistoAutonomousBase
{

    @Override
    public void runOpMode()
    {
        int positionGold = 2;
        RobotLog.ii("CAL", "Enter - runOpMode - CallistoAutoDepotStart");
        /*
         * Initialize the drive system variables.
         * The init() method of the hardware class does all the work here
         */
        initHW();

        // Send telemetry message to signify robot waiting;
        //telemetry.addData("Status", "Resetting Encoders");    //
        //telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        //waitForStart();
        // Do not use waitForStart() if you have Motorola E4 phones.
        //waitForStart();
        while (!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("status", "waiting for start command...");
            telemetry.update();
        }

        positionGold = myTFOD(2);
        myDetectionTest(positionGold, DRIVE_SPEED, 40.0);
        myDetectionRun(positionGold, DRIVE_SPEED, 40.0);
        myLanderLift(Direction.ROBOT_UP, 1, 5, 5.0);

        //sleep(50);     // pause for servos to move

        telemetry.addData("Path", "Run Complete");
        telemetry.update();
        RobotLog.ii("CAL", "Exit - runOpMode - CallistoAutoDepotStart");

    }

    private void myDetectionRun(int position,
                                double speed,
                                double timeoutS)
    {

        RobotLog.ii("CAL", "Enter - myDetectionRun");

        //initialized the motor encoders
        initMotorEncoders();

        // Ensure that the op mode is still active
        if (opModeIsActive() && !isStopRequested() )
        {

            // Determine new target position, and pass to motor controller
            if (position == 1)
            {
                //cube on left
                myEncoderDrive(Direction.FORWARD, TURN_SPEED, 23, 10.0);
                robot.markerServo.setPosition(0.9);
                rotate(30, TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 9, 10.0);
                rotate(72, TURN_SPEED);
                myEncoderDrive(Direction.STRAFE_RIGHT, DRIVE_SPEED, 3, 10.0);
                myEncoderDrive(Direction.FORWARD, 0.95, 71, 10.0);
            }
             else if (position == 3)
            {
                //cube on right
                myEncoderDrive(Direction.FORWARD, TURN_SPEED, 20, 10.0);
                rotate(-30, TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 12, 10.0);
                robot.markerServo.setPosition(0.9);
                myEncoderDrive(Direction.BACKWARD, 0.95, 51, 10.0);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 3, 10.0);
                myEncoderDrive(Direction.BACKWARD, 0.95, 12, 10.0);
            } else // Position = 2 also default position
            {
                //cube in middle
                myEncoderDrive(Direction.STRAFE_RIGHT, DRIVE_SPEED, 4, 5.0);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 28, 10.0);
                robot.markerServo.setPosition(0.9);
                rotate(-44, TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD, 0.95, 69, 10.0);
            }

        }
        RobotLog.ii("CAL", "Exit - myDetectionRun");
    }
}
