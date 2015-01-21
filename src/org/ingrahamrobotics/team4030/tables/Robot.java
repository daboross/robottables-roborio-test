package org.ingrahamrobotics.team4030.tables;

import org.ingrahamrobotics.robottables.RobotTables;
import org.ingrahamrobotics.robottables.api.RobotTable;
import org.ingrahamrobotics.robottables.api.RobotTablesClient;

import edu.wpi.first.wpilibj.IterativeRobot;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	private RobotTables tables;
	private RobotTable robotTable;

	private long lastDebugMessage = 0;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	public void robotInit() {
		tables = new RobotTables();
		tables.run("10.40.30.255");
		RobotTablesClient client = tables.getClientInterface();
		robotTable = client.publishTable("robot");
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {
		long now = System.currentTimeMillis();
		if (lastDebugMessage <= now - 1000) {
			lastDebugMessage = now;
			robotTable.set("message-" + now, String.valueOf(now));
		}
	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
