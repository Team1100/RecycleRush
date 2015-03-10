package org.team1100.commands.autonomous;

import org.team1100.commands.drive.Drive;
import org.team1100.commands.drive.TurnLeft;
import org.team1100.commands.manipulator.RollOutTote;
import org.team1100.commands.manipulator.arm.PickUpContainerAndMove;
import org.team1100.commands.manipulator.elevator.SetElevatorHeight;
import org.team1100.subsystems.Elevator;

import edu.wpi.first.wpilibj.command.CommandGroup;

public class ThreeToteAuto extends CommandGroup {
	public ThreeToteAuto() {
		addParallel(new PickUpContainerAndMove());
		addParallel(new Drive(.52, .6, 8));
		addSequential(new PickUpThreeTotes());
		addSequential(new TurnLeft());
		addParallel(new Drive(-.75, -.8, 1));
		addSequential(new SetElevatorHeight(Elevator.BOTTOM));
		addParallel(new RollOutTote());
		addSequential(new Drive(-.75, -.8, 1));
	}
}