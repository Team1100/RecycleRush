package org.team1100.commands.manipulator.intake;

import org.team1100.subsystems.Intake;

import edu.wpi.first.wpilibj.command.Command;

public class OpenIntakeClaw extends Command {
	
	public OpenIntakeClaw() {
		requires(Intake.getInstance());
	}

	@Override
	protected void initialize() {
	}

	@Override
	protected void execute() {
		Intake.getInstance().openIntake();
	}

	@Override
	protected boolean isFinished() {
		return true;
	}

	@Override
	protected void end() {
	}

	@Override
	protected void interrupted() {
	}

}
