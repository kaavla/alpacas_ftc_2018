package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

//@Autonomous(name = "Callisto Crater Advanced - 1", group = "Callisto")
//@Disabled
public class CallistoAutoCraterAdvanced1 extends CallistoAutonomousBase {

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

        // Ensure that the op mode is still active
        if (opModeIsActive()) {

            // Determine new target position, and pass to motor controller
            if (position == 1) {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 7, 10.0, SensorsToUse.NONE);
            } else if (position == 3) {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 7, 10.0, SensorsToUse.NONE);
                rotate(75, TURN_SPEED);
                myEncoderDrive(Direction.FORWARD, DRIVE_SPEED, 70, 10.0, SensorsToUse.NONE);
                rotate(15,TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED,90, 20.0, SensorsToUse.NONE);

            } else // Position = 2 also default position
            {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 12, 10.0, SensorsToUse.NONE);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 45, 10.0, SensorsToUse.NONE);
                rotate(125,TURN_SPEED);
                myEncoderDrive(Direction.FORWARD,DRIVE_SPEED,84,10.0, SensorsToUse.NONE);
            }

        }


        }
}
