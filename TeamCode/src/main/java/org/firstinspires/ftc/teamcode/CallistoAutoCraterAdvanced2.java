package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="Callisto Auto Crater Advanced - 2", group="Callisto")
//@Disabled
public class CallistoAutoCraterAdvanced2 extends CallistoAutonomousBase
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
        myDetectionTest(positionGold, DRIVE_SPEED, 40.0);
        myDetectionRun(positionGold, DRIVE_SPEED, 40.0);
        myLanderLift(1, 1, 7.5, 12.0);

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
            //gold mineral is right most side when standing near the lander lift
            {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 12, 10.0);
                myEncoderDrive(Direction.STRAFE_LEFT,DRIVE_SPEED + 0.2,88.5,10.0); //PERFECT
                rotate(125,TURN_SPEED);
                myEncoderDrive(Direction.FORWARD,DRIVE_SPEED,31,10.0);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,60,10.0);
                rotate(10,TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,12,10.0);
            } else if (position == 3)
            //gold mineral on left most side when standing near the lander lift
            {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 12, 10.0);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 35.5, 10.0);
                rotate(127,TURN_SPEED);
                myEncoderDrive(Direction.FORWARD,DRIVE_SPEED,39,10.0);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,60,10.0);
                rotate(10,TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,10.5,10.0); // PERFECT

            } else // Position = 2 also default position
            //gold mineral in center
            {
                myEncoderDrive(Direction.BACKWARD, DRIVE_SPEED, 10, 10.0);
                myEncoderDrive(Direction.STRAFE_LEFT, DRIVE_SPEED, 62, 10.0);
                rotate(125,TURN_SPEED);
                myEncoderDrive(Direction.FORWARD,DRIVE_SPEED,31,10.0);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,59,10.0);
                rotate(10,TURN_SPEED);
                myEncoderDrive(Direction.BACKWARD,DRIVE_SPEED,11,10.0);//PERFECT

            }

        }
    }
}
