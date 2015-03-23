package org.team1100.commands.manipulator.miniarm;

import org.team1100.subsystems.MiniArm;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class OpenMiniArm extends Command {

	public OpenMiniArm() {
		requires(MiniArm.getInstance());
	}

	protected void initialize() {
		setTimeout(2);
	}

	protected void execute() {
		if (!MiniArm.getInstance().isOpen())
			MiniArm.getInstance().open();
	}

	protected boolean isFinished() {
		return isTimedOut() || MiniArm.getInstance().isOpen();
	}

	protected void end() {
		MiniArm.getInstance().setOut(true);
	}

	protected void interrupted() {
	}
}