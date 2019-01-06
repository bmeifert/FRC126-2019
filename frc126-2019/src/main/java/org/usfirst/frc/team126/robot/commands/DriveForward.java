package org.usfirst.frc.team126.robot.commands;

import org.usfirst.frc.team126.robot.Robot;
import edu.wpi.first.wpilibj.command.Command;

public class DriveForward extends Command {
    public DriveForward() {
        // Use requires() here to declare subsystem dependencies
    	requires(Robot.driveBase);
    }

    // Run before command starts 1st iteration
    protected void initialize() {
        Robot.driveBase.Drive(0, 0, 1);
    }

    // Called every tick (20ms)
    protected void execute() {
        Robot.driveBase.Drive(0.5, 0,1);
    }

    // Returns true if command finished
    protected boolean isFinished() {
            return false;

    }

    // Called once after isFinished returns true
    protected void end() {
		System.out.println("Drive Forward END");
        Robot.driveBase.Drive(0, 0, 1);
    }

    // Called when another command tries to use this command's subsystem
    protected void interrupted() {
		System.out.println("Drive Forward Interrupted");
        Robot.driveBase.Drive(0, 0, 1);
    }
}
