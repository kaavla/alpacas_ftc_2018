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

        myDetectionTest(3, DRIVE_SPEED, 40.0);
        myDetectionRun(3,  DRIVE_SPEED, 40.0);

        sleep(1000);     // pause for servos to move

        telemetry.addData("Path", "Complete");
        telemetry.update();



    }

    private void myDetectionRun (int position,
                                 double speed,
                                 double timeoutS) {


        //initialized the motor encoders
        initMotorEncoders();

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (position == 1)
            {
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 10, 10.0);
                rotate(70, TURN_SPEED);
                myEncoderDrive(Direction.STRAFE_LEFT, 10, DRIVE_SPEED, 10.0);
                rotate(45, TURN_SPEED);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 15, 10.0);
                //markerServo.setPosition(-0.9);
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 2, 10.0);
                myEncoderDrive(Direction.STRAFE_RIGHT, DRIVE_SPEED, 60, 10.0);
                rotate(10, TURN_SPEED);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 13, 10.0);
            }
            else if (position == 3)
            {
                myEncoderDrive(Direction.STRAFE_LEFT, TURN_SPEED, 22, 10.0);
                rotate(40, TURN_SPEED);
                //markerServo.setPosition(-0.9);
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 2, 10.0);
                rotate(-90, TURN_SPEED);
                myEncoderDrive(Direction.STRAFE_RIGHT, DRIVE_SPEED, 60, 10.0);
                rotate(10, TURN_SPEED);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 13, 10.0);
            } else // Position = 2 also default position
            {
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 40, 10.0);
                rotate(45, TURN_SPEED);
                //markerServo.setPosition(-0.9);
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 2, 10.0);
                myEncoderDrive(Direction.STRAFE_RIGHT, DRIVE_SPEED, 60, 10.0);
                rotate(10, TURN_SPEED);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 13, 10.0);
            }
        }
    }
}
