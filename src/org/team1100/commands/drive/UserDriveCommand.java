package org.team1100.commands.drive;

import org.team1100.OI;
import org.team1100.Robot;
import org.team1100.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.command.Command;

public class UserDriveCommand extends Command {

	public UserDriveCommand() {
		requires(DriveTrain.getInstance());
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		double leftValue = OI.getInstance().getLeftJoystick().getAxis(Joystick.AxisType.kY);
		double rightValue = OI.getInstance().getRightJoystick().getAxis(Joystick.AxisType.kY);

		DriveTrain.getInstance().driveTank(leftValue, rightValue);
	}

	@Override
	protected boolean isFinished() {
		return false;
	}

	@Override
	protected void end() {
		DriveTrain.getInstance().stop();
	}

	@Override
	protected void interrupted() {
		end();
	}

}
