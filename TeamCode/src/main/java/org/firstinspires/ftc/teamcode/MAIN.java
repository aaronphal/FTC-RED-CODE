/**
 *
 * Created by Maddie, FTC Team 4962, The Rockettes
 * version 1.0 Aug 11, 2016
 *
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

/*
	Holonomic concepts from:
	http://www.vexforum.com/index.php/12370-holonomic-drives-2-0-a-video-tutorial-by-cody/0
   Robot wheel mapping:
          X FRONT X
        X           X
      X  FL       FR  X
              X
             XXX
              X
      X  BL       BR  X
        X           X
          X       X
*/
@TeleOp(name = "MAIN", group = "Concept")
//@Disabled
public class MAIN extends OpMode {

    HardwareMap robot = new HardwareMap();


    /**
     * Constructor
     */
    public MAIN() {

    }

    @Override
    public void init() {
        robot.init(hardwareMap);


        /*
         * Use the hardwareMap to get the dc motors and servos by name. Note
         * that the names of the devices must match the names used when you
         * configured your robot and created the configuration file.
         */

//        grabber = hardwareMap.get(Servo.class, "gr");
//        dragger1 = hardwareMap.get(Servo.class, "done");
//        dragger2 = hardwareMap.get(Servo.class, "dtwo");
        //These work without reversing (Tetrix motors).
        //AndyMark motors may be opposite, in which case uncomment these lines:
        //motorFrontLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorBackLeft.setDirection(DcMotor.Direction.REVERSE);
        //motorFrontRight.setDirection(DcMotor.Direction.REVERSE);
        //motorBackRight.setDirection(DcMotor.Direction.REVERSE);

    }

    @Override
    public void loop() {

        double grabberPos;
        // left stick controls direction
        // right stick X controls rotation

        float gamepad1LeftY = -gamepad1.left_stick_y;
        float gamepad1LeftX = gamepad1.left_stick_x;
        float gamepad1RightX = gamepad1.right_stick_x;

        // holonomic formulas

        float FrontLeft = -gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        float FrontRight = gamepad1LeftY - gamepad1LeftX - gamepad1RightX;
        float BackRight = gamepad1LeftY + gamepad1LeftX - gamepad1RightX;
        float BackLeft = -gamepad1LeftY + gamepad1LeftX - gamepad1RightX;

        if(gamepad2.x){
            grabberPos=0.6;
        }
        else if(gamepad2.a){
            grabberPos=0.5;
        }
        //else{
          //  grabberPos=0;
       // }

        // clip the right/left values so that the values never exceed +/- 1
        //FrontRight = Range.clip(FrontRight, -1, 1);
        //FrontLeft = Range.clip(FrontLeft, -1, 1);
        //BackLeft = Range.clip(BackLeft, -1, 1);
        //BackRight = Range.clip(BackRight, -1, 1);

        // write the values to the motors
        robot.motorFrontRight.setPower(FrontRight);
        robot.motorFrontLeft.setPower(FrontLeft);
        robot.motorBackLeft.setPower(BackLeft);
        robot.motorBackRight.setPower(BackRight);





        if(gamepad2.dpad_up){
            robot.elevator.setPower(1);
        }
        else if(gamepad2.dpad_down) {
            robot.elevator.setPower(-1);

        }
        else {
            robot.elevator.setPower(0);
        }






        if(gamepad2.a){
            robot.grabber.setPosition(0);
        }
        else if(gamepad2.b) {
            robot.grabber.setPosition(0.4);
        }
        if(gamepad2.left_bumper){
            robot.dragger1.setPosition(0);
        }
        else if(gamepad2.right_bumper) {
            robot.dragger1.setPosition(0.4);

        }if(gamepad2.left_bumper){
            robot.dragger2.setPosition(.45);
        }
        else if(gamepad2.right_bumper) {
            robot.dragger2.setPosition(0.1);
        }

        //robot.grabber.setPosition(grabberPos);
        /*
         * Telemetry for debugging
         */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("Joy XL YL XR",  String.format("%.2f", gamepad1LeftX) + " " +
                String.format("%.2f", gamepad1LeftY) + " " +  String.format("%.2f", gamepad1RightX));
        telemetry.addData("f left pwr",  "front left  pwr: " + String.format("%.2f", FrontLeft));
        telemetry.addData("f right pwr", "front right pwr: " + String.format("%.2f", FrontRight));
        telemetry.addData("b right pwr", "back right pwr: " + String.format("%.2f", BackRight));
        telemetry.addData("b left pwr", "back left pwr: " + String.format("%.2f", BackLeft));

    }

    @Override
    public void stop() {

    }

    /*
     * This method scales the joystick input so for low joystick values, the
     * scaled value is less than linear.  This is to make it easier to drive
     * the robot more precisely at slower speeds.
     */
    double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };

        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    }

}