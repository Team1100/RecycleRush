package org.team1100.commands.drive;

import org.team1100.subsystems.DriveTrain;

import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.command.Command;

public class TurnRight extends Command {

	private int dAngle;
	private double EPSILON = 5;
	private double startAngle;
	private double SPEED_ONE = 1;
	private double SPEED_TWO = 0;

	private String speedOneKey = "SpeedOne";
	private String speedTwoKey = "SpeedTwo";

	public TurnRight(int dAngle) {
		requires(DriveTrain.getInstance());
		this.dAngle = dAngle;
		Preferences.getInstance().putDouble(speedOneKey, SPEED_ONE);
		Preferences.getInstance().putDouble(speedTwoKey, SPEED_TWO);
	}

	@Override
	protected void initialize() {
		SPEED_ONE = Preferences.getInstance().getDouble(speedOneKey, SPEED_ONE);
		SPEED_TWO = Preferences.getInstance().getDouble(speedTwoKey, SPEED_TWO);
		startAngle = DriveTrain.getInstance().getHeading();
	}

	@Override
	protected void execute() {
		DriveTrain.getInstance().driveTank(SPEED_ONE, SPEED_TWO);
	}

	@Override
	protected boolean isFinished() {
		return DriveTrain.getInstance().getHeading() - startAngle > (dAngle - EPSILON);
		// return Math.abs(startAngle - (dAngle -
		// DriveTrain.getInstance().getHeading())) < EPSILON;
	}

	@Override
	protected void end() {

	}

	@Override
	protected void interrupted() {
	}

}
