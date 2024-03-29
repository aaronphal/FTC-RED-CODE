/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

/**
 * This file contains an example of an iterative (Non-Linear) "OpMode".
 * An OpMode is a 'program' that runs in either the autonomous or the teleop period of an FTC match.
 * The names of OpModes appear on the menu of the FTC Driver Station.
 * When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all iterative OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="flywheel", group="Iterative Opmode")
//@Disabled
public class RobotTeleop2 extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private DcMotor centerDrive = null;
    private DcMotor elevator = null;
    private DcMotor frontLeftDrive = null;
    private DcMotor frontRightDrive = null;
    private DcMotor leftFlywheel = null;
    private DcMotor rightFlywheel = null;
    private Servo grabber = null;

    /*
     * Code to run ONkCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "bl");
        rightDrive = hardwareMap.get(DcMotor.class, "br");
        centerDrive = hardwareMap.get(DcMotor.class, "c");
        elevator = hardwareMap.get(DcMotor.class, "elevator_drive");
        frontLeftDrive  = hardwareMap.get(DcMotor.class, "fl");
        frontRightDrive = hardwareMap.get(DcMotor.class, "fr");
        leftFlywheel = hardwareMap.get(DcMotor.class, "lfw");
        rightFlywheel = hardwareMap.get(DcMotor.class, "rfw");
        grabber = hardwareMap.get(Servo.class, "gr");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runsn backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        centerDrive.setDirection(DcMotor.Direction.FORWARD);
        elevator.setDirection(DcMotor.Direction.FORWARD);
        frontLeftDrive.setDirection(DcMotor.Direction.FORWARD);
        frontRightDrive.setDirection(DcMotor.Direction.REVERSE);
        leftFlywheel.setDirection(DcMotor.Direction.REVERSE);
        rightFlywheel.setDirection(DcMotor.Direction.FORWARD);


        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        // Setup a variable for each drive wheel to save power level for telemetry
        double leftPower;
        double rightPower;
        double centerPower;
        double elevatorPower;
        double frontLeftPower;
        double frontRightPower;
        double flyWheelPower = 0;

        // Choose to drive using either Tank Mode, or POV Mode
        // Comment out the method that's not used.  The default below is POV.

        // POV Mode uses left stick to go forward, and right stick to turn.
        // - This uses basic math to combine motions and is easier to drive straight.
        double drive = -gamepad1.left_stick_y;
        double turn  =  gamepad1.left_stick_x;
        double enable_h= gamepad1.right_stick_x;
        double enableElevator= gamepad1.right_stick_y;

        leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;
        centerPower   = Range.clip(enable_h + drive, -1.0, 1.0) ;
        elevatorPower   = Range.clip(enableElevator + drive, -1.0, 1.0) ;
        frontLeftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
        frontRightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

        // Tank Mode uses one stick to control each wheel.
        // - This requires no math, but it is hard to drive forward slowly and keep straight.
        leftPower  = -gamepad1.left_stick_y ;
        rightPower = -gamepad1.left_stick_y ;
        centerPower =  -gamepad1.left_stick_x ;
        elevatorPower  = -gamepad1.right_stick_y ;
        frontLeftPower  = -gamepad1.left_stick_y ;
        frontRightPower = -gamepad1.left_stick_y ;

        if (Math.abs(gamepad1.right_stick_x) > 0.1) {
            leftPower  = -gamepad1.right_stick_x ;
            rightPower = gamepad1.right_stick_x ;
            frontLeftPower  = -gamepad1.right_stick_x ;
            frontRightPower = gamepad1.right_stick_x ;
        }
        else {
            leftPower  = gamepad1.left_stick_y ;
            rightPower = gamepad1.left_stick_y ;
            centerPower =  -gamepad1.left_stick_x ;
            elevatorPower  = gamepad1.right_stick_y ;
            frontLeftPower  = gamepad1.left_stick_y ;
            frontRightPower = gamepad1.left_stick_y ;
        }

        while(gamepad1.x){
            flyWheelPower=0.1;
        }
        while(gamepad1.y){
            flyWheelPower=-0.1;
        }
        // Send calculated power to wheels, omnidirectional
        leftDrive.setPower(leftPower);
        rightDrive.setPower(rightPower);
        centerDrive.setPower(centerPower);
        elevator.setPower(elevatorPower);
        frontLeftDrive.setPower(frontLeftPower);
        frontRightDrive.setPower(-frontRightPower);
        leftFlywheel.setPower(flyWheelPower);
        rightFlywheel.setPower(flyWheelPower);
        if(gamepad1.a){
            grabber.setPosition(0.1);
        }
        else if(gamepad1.b){
            grabber.setPosition(0.15);
        } else {
            grabber.setPosition(0.13);
        }
        /*if(gamepad1.a){
            grabber.setPosition(grabber.getPosition()+0.1);
        }
        else if(gamepad1.b){
            grabber.setPosition(grabber.getPosition()-0.1);
        }*/

        // Show the elapsed game time and wheel power.
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f), center  (%.2f)", leftPower, rightPower, centerPower, elevatorPower, frontLeftPower, frontRightPower);
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
