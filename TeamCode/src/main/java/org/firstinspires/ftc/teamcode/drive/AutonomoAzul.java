package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.drive.subsystems.Elevator;
import org.firstinspires.ftc.teamcode.drive.subsystems.Claw;
import org.firstinspires.ftc.teamcode.drive.subsystems.Intake;
import org.firstinspires.ftc.teamcode.drive.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.drive.subsystems.SampleMecanumDrive;
import org.firstinspires.ftc.teamcode.drive.subsystems.TwoWheelTrackingLocalizer;



@Autonomous(group="drive")
public class AutonomoAzul extends LinearOpMode {

    TwoWheelTrackingLocalizer localizer;

    @Override
    public void runOpMode() throws InterruptedException {
        double speed = 0.2;
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Claw claw = new Claw(hardwareMap);
        waitForStart();
        localizer = new TwoWheelTrackingLocalizer(hardwareMap,drive);

        drive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //launcher.stop();
        //elevator.resetElevatorEncoders();
        drive.resetHeading();
        drive.resetEncoder();
        Waypoints[] waypoints = {
                new Waypoints(60, 0, 0,  50),
                new Waypoints(0, 0, 0,  50),
                new Waypoints(0,0,0,50)
        };

        while (!isStopRequested()) {

            for (int idx = 0; idx < waypoints.length; idx++) {
                if (isStopRequested()) {
                    break;
                }
                Waypoints w = waypoints[idx];
                drive.resetEncoder();

                drive.setLimiterAuto(speed);
                if (w.x < 0)
                    drive.setWeightedDrivePowerAuto(new Pose2d(-speed, 0, 0));
                else
                    drive.setWeightedDrivePowerAuto(new Pose2d(speed, 0, 0));

                while (Math.abs(localizer.getPerpendicularPosition()) < Math.abs(w.x)) {
                    drive.update();
                    updateTelemetry();
                }

                if (w.y < 0)
                    drive.setWeightedDrivePowerAuto(new Pose2d(0, -speed, 0));
                else
                    drive.setWeightedDrivePowerAuto(new Pose2d(0, speed, 0));

                while (Math.abs(localizer.getParallelPosition()) < Math.abs(w.y)) {
                    drive.update();
                    updateTelemetry();
                }

                while (localizer.getHeading() > w.heading) {
                    drive.setWeightedDrivePower(new Pose2d(0, 0, speed));
                    updateTelemetry();
                }
                updateTelemetry();
                drive.setWeightedDrivePowerAuto(new Pose2d(0, 0, 0));
                /*
                if(w.actIntake){
                    intake.activate();
                }
                else if(w.actReverse){
                    intake.close();
                }
                else{
                    intake.stop();
                }

                 */

                /*
                if(w.extendElevator){
                    elevator.finalPosition();
                }
                else{
                    elevator.initialPosition();
                }

                 */

                /*if(w.openClaw){
                    claw.open();
                }
                else{
                    claw.close();
                }*/
                for (int i =0; i<(w.timeout/10);i++){
                    sleep(1);
                    //elevator.task();
                    updateTelemetry();
                }


            }

            break;
        }

    }
    void updateTelemetry(){
        telemetry.addData("Heading",localizer.getHeading());
        telemetry.addData("Parallel",localizer.getParallelPosition());
        telemetry.addData("Perpendicular",localizer.getPerpendicularPosition());
        telemetry.update();
    }
}
