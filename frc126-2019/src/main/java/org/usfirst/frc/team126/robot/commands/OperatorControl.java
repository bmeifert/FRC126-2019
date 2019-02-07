package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;
import org.usfirst.frc.team126.robot.RobotMap;
import org.usfirst.frc.team126.robot.subsystems.Lift.liftPos;
import edu.wpi.first.wpilibj.command.Command;
public class OperatorControl extends Command {
	double fb, lr, rot, tl, tr, x, y, v, maxSpeed;
	int smoothFactor = 5;
	boolean xboxLTrig, xboxRTrig, xboxA, xboxB, xboxX, xboxY, xboxLStick, xboxRStick;
	boolean isCurved = true;
	boolean isSmoothed = true;
	public OperatorControl() {
		// Use requires() here to declare subsystem dependencies
		requires(Robot.driveBase);
	}

	// Run before command starts 1st iteration
	@Override
	protected void initialize() {
		Robot.lift.resetLift();
		Robot.lift.setTargetPos(liftPos.free, true);
	}

	// Called every tick (20ms)
	@SuppressWarnings("static-access")
	@Override
	protected void execute() {
		fb = Robot.oi.driveController.getRawAxis(RobotMap.lStickY) * -1; // Forward and backward movement (Left stick Y)
		tl = Robot.oi.driveController.getRawAxis(RobotMap.Ltrigger); // Left trigger (for strafe L)
		tr = Robot.oi.driveController.getRawAxis(RobotMap.Rtrigger); // Right trigger (for strafe R)
		rot = Robot.oi.driveController.getRawAxis(RobotMap.rStickX); // Rotation (Right stick X)
		xboxA = Robot.oi.driveController.getRawButton(RobotMap.xboxA); // Xbox A button
		xboxB = Robot.oi.driveController.getRawButton(RobotMap.xboxB); // Xbox B button
		xboxX = Robot.oi.driveController.getRawButton(RobotMap.xboxX); // Xbox X button
		xboxY = Robot.oi.driveController.getRawButton(RobotMap.xboxY); // Xbox Y button
		xboxLTrig = Robot.oi.driveController.getRawButton(RobotMap.xboxLTrig); // Xbox L Shoulder button
		xboxRTrig = Robot.oi.driveController.getRawButton(RobotMap.xboxRTrig); // Xbox R Shoulder button
		xboxLStick = Robot.oi.driveController.getRawButton(RobotMap.xboxLStick); // Xbox L stick press down
		xboxRStick = Robot.oi.driveController.getRawButton(RobotMap.xboxRStick); // Xbox R stick press down

		if(xboxA) { // Set different drive spike smoothing factors (for testing)
			Robot.lift.setTargetPos(liftPos.free, true);
		}
		if(xboxB) {
			Robot.lift.setTargetPos(liftPos.zero, false);
		}
		if(xboxX) {
			Robot.lift.setTargetPos(liftPos.first, false);
		}
		if(xboxY) {
			Robot.lift.setTargetPos(liftPos.second, false);
		}

		if(tr > 0) {
			lr = tr;
		}
		else {
			lr = tl * -1;
		}

		if(Math.abs(rot) < 0.05) { // Prevent rotation drifting
			rot = 0;
		}
		if(Math.abs(fb) < 0.05) {
			fb = 0;
		}
		if(xboxLTrig) {
			x = Robot.vision.getPacketData(1, "x");
			y = Robot.vision.getPacketData(1, "y");
			v = Robot.vision.getPacketData(1, "v");
			System.out.println("AUTODRIVE" + x + y + v);
			if(v == 1) {
				if(x > 165) {
					Robot.driveBase.Drive(0, 0.05 + (x - 165) / 300, false, true, 5);
				}
				else if(x < 135) {
					Robot.driveBase.Drive(0, -0.05 + (x - 135) / 300, false, true, 5);
				}
				else {
					Robot.driveBase.Drive(0, 0, false, true, 5);
				}
				System.out.println("ASSIST TAKEOVER");
			}
			else {
				System.out.println("ASSIST FAIL");
				//Robot.driveBase.Drive(0, 0, false, true, 5);
			}

		}
		else {
			double lidarDistance = Robot.distance.getDistance();
			if(lidarDistance < 95 && lidarDistance != 0) {
				maxSpeed = 1 + ((lidarDistance - 95) / 40);
				if(maxSpeed < 0.5) {
					maxSpeed = 0.5;
				}
				fb *= maxSpeed;
				/*
				if(lidarDistance < 75) {
					fb = -0.3 + (lidarDistance - 75) / 20;
					if(fb < -0.5) {
						fb = -0.5;
					}
				}
				*/
				//Robot.driveBase.Drive(fb, rot, isCurved, isSmoothed, smoothFactor);
			}
			else {
				//Robot.driveBase.Drive(fb, rot, isCurved, isSmoothed, smoothFactor); // Drive with set values
			}

			Robot.intake.setIntake(lr); // Set intake to triggers
			
			Robot.lift.moveLift(fb);
			//System.out.println("Driving");
		}



	}

	// Returns true if command finished
	@Override
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	@Override
	protected void end() {

	}

	// Called when another command tries to use this command's subsystem
	@Override
	protected void interrupted() {

	}
}
