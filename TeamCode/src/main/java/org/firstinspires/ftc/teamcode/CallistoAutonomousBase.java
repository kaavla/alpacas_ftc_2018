package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

//@Autonomous(name="CallistoAutonomousBase", group="Callisto")
//@Disabled
public class CallistoAutonomousBase extends LinearOpMode {

    public CallistoHW         robot   = new CallistoHW();


    @Override
    public void runOpMode() {
        //Empty Function
    }

    /**
     * Rotate left or right the number of degrees. Does not support turning more than 180 degrees.
     * @param degrees Degrees to turn, + is left - is right
     */
    private void rotate(int degrees, double power)
    {

            // On right turn we have to get off zero first.
            opModeIsActive();
    }

    private void resetAngle()
    {

    }

    private int getAngle()
    {

        return 0;
    }



    }