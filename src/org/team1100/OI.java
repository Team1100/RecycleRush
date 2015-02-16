package org.team1100;

import org.team1100.commands.manipulator.PickUpToteCommand;
import org.team1100.commands.manipulator.elevator.PushToteCommand;
import org.team1100.commands.manipulator.intake.RollInCommand;
import org.team1100.commands.manipulator.intake.RollOutCommand;
import org.team1100.commands.manipulator.intake.ToggleIntakeCommand;
import org.team1100.input.AttackThree;
import org.team1100.input.LaunchpadController;
import org.team1100.input.XboxController;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	
	public static OI oi;
	
	public static OI getInstance(){
		if (oi == null)
			oi = new OI();
		return oi;
	}
	
	private AttackThree rightStick;
	private AttackThree leftStick;
	private XboxController xbox;
	private LaunchpadController launchPad;

	private OI() {
		rightStick = new AttackThree(RobotMap.C_RIGHT_JOYSTICK, 0.1);
		leftStick = new AttackThree(RobotMap.C_LEFT_JOYSTICK, 0.1);
		xbox = new XboxController(RobotMap.C_XBOX_CONTROLLER, 0.1);
		launchPad = new LaunchpadController(RobotMap.C_LAUNCHPAD_CONTROLLER);
		
		xbox.getButtonLeftBumper().whileHeld(new RollInCommand());
		xbox.getButtonRightBumper().whileHeld(new RollOutCommand());
		xbox.getButtonA().whenPressed(new ToggleIntakeCommand());
		xbox.getButtonY().toggleWhenPressed(new PickUpToteCommand());
		xbox.getButtonX().whenPressed(new PushToteCommand());
	}

	/**
	 * Returns the instance of the Right JoyStick to be able to get the value of
	 * the axis' and to test whether a button is pressed
	 * 
	 * @return the instance of the Right JoyStick
	 */
	public AttackThree getRightJoystick() {
		return rightStick;
	}

	/**
	 * Returns the instance of the Left JoyStick to be able to get the value of
	 * the axis' and to test whether a button is pressed
	 * 
	 * @return the instance of the Left JoyStick
	 */
	public AttackThree getLeftJoystick() {
		return leftStick;
	}

	/**
	 * Returns the instance of the XBOX Controller to be able to get the value
	 * of the axis' and to test whether a button is pressed
	 * 
	 * @return the instance of the XBOX Controller
	 */
	public XboxController getXboxController() {
		return xbox;
	}

	public LaunchpadController getLaunchpad() {
		return launchPad;

	}
}
