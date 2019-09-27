package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

    @Autonomous(name = "Concept: NullOp", group = "Concept")
    @Disabled
    public class Vision2 extends OpMode {

        private ElapsedTime runtime = new ElapsedTime();

        //vuforia initialization
        VuforiaLocalizer vuforia;

        VuforiaTrackables relicTrackables;
        VuforiaTrackable relicTemplate;

        @Override
        public void init() {

            telemetry.addData("Status", "Initialized");

            int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
            VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

            parameters.vuforiaLicenseKey = "ASh+s4v/////AAABmZ3TByjbWkBivrG+6BbFDF91VG+lsvvHoEKEM9J92i/QUaK0ZADmUU7CtgddPbF+P/QPKy9T0M4uMPzM+IdrYsTsvJ//P6BLnYiqd4IaXGJv8Xr7Mbh1/BtbjNX0QunaQNRF/GJq0N6DdpgVNI3GkDR9IYt63sIvZOAJ0GNEP4BeT/GGxzf8o7V1nOLcqfwK2uVbViLkn7rQ8KYUKEO4N1RBH72nln5Y7qqCx9HIoc6MQ5ss2a3Xw7QN2L46Sp6dZeVjgkAwRmKmvIBqkS20mHwx9myDPo+PSutNH7F1ZM61IEODqqjKn1PTIYlZKB8+3LHRzilVXGq34wOaA4dNNtYcK0Et62fYZiA7N54kT0hi";

            parameters.cameraDirection = VuforiaLocalizer.CameraDirection.FRONT;
            this.vuforia = ClassFactory.createVuforiaLocalizer(parameters); //deprecated, please fix

            relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
            relicTemplate = relicTrackables.get(0);
            relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        }

        @Override
        public void start() {

            runtime.reset();

            relicTrackables.activate();

        }

        @Override
        public void loop() {

            RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
            if (vuMark != RelicRecoveryVuMark.UNKNOWN) {

                OpenGLMatrix pose = ((VuforiaTrackableDefaultListener) relicTemplate.getListener()).getPose();
                if (vuMark == RelicRecoveryVuMark.RIGHT)
                    telemetry.addData("VuMark", "Right");
                else if (vuMark == RelicRecoveryVuMark.CENTER)
                    telemetry.addData("VuMark", "Center");
                else if (vuMark == RelicRecoveryVuMark.LEFT)
                    telemetry.addData("VuMark", "Left");
                else if (vuMark == RelicRecoveryVuMark.UNKNOWN)
                    telemetry.addData("VuMark", "Unknown");

                if (pose != null) {
                    VectorF trans = pose.getTranslation();
                    Orientation rot = Orientation.getOrientation(pose, AxesReference.EXTRINSIC, AxesOrder.XYZ, AngleUnit.DEGREES);

                    // Extract the X, Y, and Z components of the offset of the target relative to the robot
                    double tX = trans.get(0);
                    double tY = trans.get(1);
                    double tZ = trans.get(2);

                    // Extract the rotational components of the target relative to the robot
                    double rX = rot.firstAngle;
                    double rY = rot.secondAngle;
                    double rZ = rot.thirdAngle;
                }
            } else {
                telemetry.addData("VuMark", "not visible");
            }

            telemetry.update();
        }
    }
