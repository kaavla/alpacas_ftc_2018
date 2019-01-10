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
        //myLanderLift(0, 1, 7, 9.0);
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
            //right most side when standing near the lander lift
                {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 7, 10.0); //PERFECT
                myEncoderDrive(Direction.STRAFE_RIGHT,DRIVE_SPEED,16,10.0);
                myEncoderDrive(Direction.FORWARD,DRIVE_SPEED,16.5,10.0);
                myLanderLift(Direction.ROBOT_UP,TURN_SPEED,2, 5.0);
             } else if (position == 3)
            //left most side when standing near the lander lift
            {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 11, 20.0);//PERFECT
                myEncoderDrive(Direction.STRAFE_RIGHT,DRIVE_SPEED,53,20.0);
                rotate(160, TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,16,10.0);
                rotate(60,TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,10,10.0);
                myLanderLift(Direction.ROBOT_UP,TURN_SPEED,2, 5.0);

            } else // Position = 2 also default position
            //center
            {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 7, 20.0);//PERFECT
                rotate(-180, TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED,12, 20.0);
                myLanderLift(Direction.ROBOT_UP,TURN_SPEED,2,5.0);
            }
        }
    }
}

