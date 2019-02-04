package org.usfirst.frc.team126.robot.subsystems;

import org.usfirst.frc.team126.robot.Robot;

import edu.wpi.first.wpilibj.command.Subsystem;

public class Lift extends Subsystem {
	public static enum liftPos { 
		first, second, third, zero, free
	}
	public static enum liftStates {
		notzeroed, ready, moving, estop, zeroing
	}
	public static enum limitStates {
		bottomLimit, topLimit, ok
	}
	public static liftPos targetPos = null;
	public static liftPos currentPos = null;
	public static liftStates lState = null;
	public static limitStates limitState = null;
	public static double encoderVal = 0;
	public static double liftSpeed = 0;

	public void initDefaultCommand() {

	}

	public static void resetLift() { // Set everything to default. Will require us to re-zero the lift.
		lState = liftStates.notzeroed;
		targetPos = liftPos.zero;
		currentPos = liftPos.free;
		limitState = limitStates.ok;
		liftSpeed = 0;
		encoderVal = 0;
	}

	public static void setTargetPos(liftPos lPos) {
		if(lState == liftStates.ready) {
			targetPos = lPos;
			lState = liftStates.moving;
		}
		else if(lState == liftStates.estop || lState == liftStates.notzeroed){ // if the lift is having a major issue (not zeroed or emergency stopped)
			System.out.println("LIFT MOVE FAILED -- LIFT CRITICAL ERROR");
		}
		else { // if the lift is in any other state, e.g. already moving to another position
			System.out.println("LIFT TARGET FAILED -- LIFT BUSY"); 
		}

	}
	public static void moveLift(double newPos) {
		if(Robot.limitSwitch.get() == false) { // The first thing we should always do is check if we're at a limit
			limitState = limitStates.bottomLimit;
		}
		else if(Robot.limitSwitch2.get() == false) {
			limitState = limitStates.topLimit;
		}
		else {
			limitState = limitStates.ok;
		}
	}
	public static void setLiftSpeed(double targetSpeed) {
		if(limitState == limitStates.bottomLimit && targetSpeed < 0) { // Prevent movement below bottom limit
			targetSpeed = 0;
		}
		else if(limitState == limitStates.topLimit && targetSpeed > 0) { // Prevent movement above top limit
			targetSpeed = 0;
		}
		else if(lState != liftStates.moving) {
			if(lState == liftStates.zeroing) { // If we're zeroing the lift, limit it to 20% speed so it doesn't break anything.
				if(targetSpeed > 0.2) {
					targetSpeed = 0.2;
				}
				else if(targetSpeed < -0.2) {
					targetSpeed = -0.2;
				}
			}
			else { // If it's saying we should move but we're not in that state something is going wrong.
				targetSpeed = 0;
			}
			
		}
		// TODO: Set arm motor speed to targetSpeed
	}
}
