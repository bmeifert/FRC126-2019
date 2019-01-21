		
package org.usfirst.frc.team126.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import org.usfirst.frc.team126.robot.commands.AutoCenterToLeft;
import org.usfirst.frc.team126.robot.subsystems.Intake;
import org.usfirst.frc.team126.robot.subsystems.InternalData;
import org.usfirst.frc.team126.robot.subsystems.MecanumDrivebase;
import org.usfirst.frc.team126.robot.subsystems.Vision;
import org.usfirst.frc.team126.robot.RobotMap;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class Robot extends TimedRobot {

	public static Command autonomous; // Create commands
	public static MecanumDrivebase driveBase;
	public static InternalData internalData;
	public static OI oi;
	public static Intake intake;
	public static Vision vision;

	public static TalonSRX frontLeft = new TalonSRX(RobotMap.frontLeft); // Create devices
	public static TalonSRX frontRight = new TalonSRX(RobotMap.frontRight);
	public static TalonSRX backLeft = new TalonSRX(RobotMap.backLeft);
	public static TalonSRX backRight = new TalonSRX(RobotMap.backRight);
	public static Spark intakeMotor = new Spark(9);


	@SuppressWarnings("unchecked")
	@Override
	public void robotInit() {
		oi = new OI();
		driveBase = new MecanumDrivebase();
		internalData = new InternalData();
		intake = new Intake();
		vision = new Vision();
		CameraServer.getInstance().startAutomaticCapture();
	}
	
	@Override
	public void robotPeriodic() {

	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}
	
	@Override
	public void autonomousInit() {
		autonomous = (Command) new AutoCenterToLeft();
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
		
	}

	@Override
	public void teleopInit() {
		if(autonomous != null){
			autonomous.cancel();
		}
    }

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void testPeriodic() {
	}
}
