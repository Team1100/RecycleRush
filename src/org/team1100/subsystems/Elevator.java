package org.team1100.subsystems;

import org.team1100.RobotMap;
import org.team1100.commands.manipulator.elevator.UserElevatorCommand;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.PIDSubsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.livewindow.LiveWindowSendable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.ITableListener;

public class Elevator extends PIDSubsystem {

	public static int TOP_SETPOINT = 8000; //9000
	public static int DRIVING_HEIGHT = 2000; // TODO Find setpoint
	public static int BOTTOM_SETPOINT = 0;

	private static Elevator elevator;

	private static final String topKey = "Elevator_Top";
	private static final String driveKey = "Elevator_Drive";
	private static final String botKey = "Elevator_Bot";
	private static final String pKey = "Elevator_P";
	private static final String iKey = "Elevator_I";
	private static final String dKey = "Elevator_D";

	private static double P = .001;
	private static double I = 0;
	private static double D = .001;

	public static Elevator getInstance() {
		if (elevator == null)
			elevator = new Elevator();
		updatePreferences();
		return elevator;
	}

	public static void updatePreferences() {
		P = Preferences.getInstance().getDouble(pKey, P);
		I = Preferences.getInstance().getDouble(iKey, I);
		D = Preferences.getInstance().getDouble(dKey, D);
		elevator.getPIDController().setPID(P, I, D);
		TOP_SETPOINT = Preferences.getInstance().getInt(topKey, TOP_SETPOINT);
		DRIVING_HEIGHT = Preferences.getInstance().getInt(driveKey, DRIVING_HEIGHT);
		BOTTOM_SETPOINT = Preferences.getInstance().getInt(botKey, BOTTOM_SETPOINT);
	}

	private boolean encoderReset = false;
	private boolean isBeamBroken = false;
	private ElevatorDrive elevatorDrive;
	private Encoder encoder;
	private DigitalInput beamBreak;
	private DigitalInput infraredSensorBack;
	private DigitalInput infraredSensorFront;
	private Thread beamBreakThread;

	private Elevator() {
		super(P, I, D);
		elevatorDrive = new ElevatorDrive(RobotMap.E_ELEVATOR_CIM_1, RobotMap.E_ELEVATOR_CIM_2);

		encoder = new Encoder(RobotMap.E_ENCODER_A, RobotMap.E_ENCODER_B);
		encoder.setReverseDirection(true);
		beamBreak = new DigitalInput(RobotMap.E_BEAM_BREAK);
		infraredSensorBack = new DigitalInput(RobotMap.E_INFRARED_SENSOR_BACK);
		infraredSensorFront = new DigitalInput(RobotMap.E_INFRARED_SENSOR_FRONT);
		beamBreakThread = new Thread() {

			@Override
			public void run() {
				while (true) {
					if (!beamBreak.get() && !isBeamBroken)
						isBeamBroken = true;
					// SmartDashboard.putBoolean("Beam Break", isBeamBroken);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// DriverStation.reportError(e.getMessage(), true);
					}
				}
			}
		};
		beamBreakThread.start();
		setAbsoluteTolerance(250);

		LiveWindow.addActuator("Elevator", "Encoder", encoder);
		LiveWindow.addSensor("Elevator", "Beam Break", beamBreak);
		LiveWindow.addSensor("Elevator", "Infrared Sensor", infraredSensorBack);
		LiveWindow.addSensor("Elevator", "Infrared Sensor", infraredSensorFront);
		LiveWindow.addSensor("Elevator", "PID Controller", getPIDController());
		LiveWindow.addActuator("Elevator", "Elevator Drive", elevatorDrive);

		Preferences.getInstance().putDouble(pKey, P);
		Preferences.getInstance().putDouble(iKey, I);
		Preferences.getInstance().putDouble(dKey, D);
		Preferences.getInstance().putInt(topKey, TOP_SETPOINT);
		Preferences.getInstance().putInt(driveKey, DRIVING_HEIGHT);
		Preferences.getInstance().putInt(botKey, BOTTOM_SETPOINT);
	}

	public void lift(double speed) {
		if (isBeamBroken && speed < 0)
			speed = 0;
		if (speed > 0 && isBeamBroken)
			isBeamBroken = false;
		SmartDashboard.putNumber("Encoder", getPosition());
		SmartDashboard.putBoolean("Tote In", isToteInElevator());
		elevatorDrive.lift(speed);
	}

	public double getPosition() {
		return encoder.get();
	}

	public double getSpeed() {
		return encoder.getRate();
	}

	public void resetEncoder() {
		encoder.reset();
		encoderReset = true;
	}

	public boolean isEncoderReset() {
		return encoderReset;
	}

	/**
	 * Default: false
	 */
	public boolean isBeamBroken() {
		return isBeamBroken;
	}

	public boolean isToteInElevator() {
		return !infraredSensorBack.get();
	}

	public boolean isFrontBeamBroken() {
		return !infraredSensorFront.get();
	}

	@Override
	protected double returnPIDInput() {
		return getPosition();
	}

	@Override
	protected void usePIDOutput(double output) {
		lift(output);
	}

	@Override
	protected void initDefaultCommand() {
		setDefaultCommand(new UserElevatorCommand());
	}

	private class ElevatorDrive implements LiveWindowSendable {

		private CANTalon talon1;
		private CANTalon talon2;
		private ITable m_table;
		private ITableListener m_table_listener;

		public ElevatorDrive(int channel1, int channel2) {
			talon1 = new CANTalon(channel1);
			talon2 = new CANTalon(channel2);
		}

		@Override
		public void initTable(ITable subtable) {
			m_table = subtable;
			updateTable();

		}

		@Override
		public ITable getTable() {
			return m_table;
		}

		@Override
		public String getSmartDashboardType() {
			return "Speed Controller";
		}

		@Override
		public void updateTable() {
			if (m_table != null) {
				m_table.putNumber("Value1", talon1.getSpeed());
				m_table.putNumber("Value2", talon2.getSpeed());
			}
		}

		@Override
		public void startLiveWindowMode() {
			talon1.set(0);
			talon2.set(0);
			m_table_listener = new ITableListener() {
				public void valueChanged(ITable itable, String key, Object value, boolean bln) {
					lift(((Double) value).doubleValue());
				}
			};
			m_table.addTableListener("Value", m_table_listener, true);

		}

		@Override
		public void stopLiveWindowMode() {
			talon1.set(0);
			talon2.set(0);

		}

		public void lift(double speed) {
			talon1.set(-speed);
			talon2.set(-speed);

		}

	}

}
