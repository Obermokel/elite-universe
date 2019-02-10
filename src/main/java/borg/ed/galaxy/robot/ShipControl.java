package borg.ed.galaxy.robot;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ShipControl {

	static final Logger logger = LoggerFactory.getLogger(ShipControl.class);

	public static final String SHIP_ANACONDA = "Anaconda";
	public static final String SHIP_ASP_SCOUT = "Asp_Scout";
	public static final String SHIP_KRAIT_MK2 = "Krait_MkII";
	public static final String SHIP_ORCA = "Orca";
	public static final String SHIP_TYPE9 = "Type9";

	// Time for a 180° pitch in SUPERCRUISE!
	public static final Map<String, Long> PITCH_180_MILLIS = new HashMap<>();

	public static final Map<String, Float> PITCH_FACTOR = new HashMap<>();
	public static final Map<String, Float> ROLL_FACTOR = new HashMap<>();
	public static final Map<String, Float> YAW_FACTOR = new HashMap<>();

	static {
		PITCH_180_MILLIS.put(SHIP_ANACONDA, 24000L);
		PITCH_180_MILLIS.put(SHIP_ASP_SCOUT, 10000L);
		PITCH_180_MILLIS.put(SHIP_KRAIT_MK2, 16000L);
		PITCH_180_MILLIS.put(SHIP_ORCA, 18000L);
		PITCH_180_MILLIS.put(SHIP_TYPE9, 30000L);

		// Anaconda
		PITCH_FACTOR.put(SHIP_ANACONDA, 2.5f);
		ROLL_FACTOR.put(SHIP_ANACONDA, 1.5f);
		YAW_FACTOR.put(SHIP_ANACONDA, 4.5f);

		// Asp Scout
		PITCH_FACTOR.put(SHIP_ASP_SCOUT, 0.4f);
		ROLL_FACTOR.put(SHIP_ASP_SCOUT, 0.5f);
		YAW_FACTOR.put(SHIP_ASP_SCOUT, 0.5f);

		// Krait MkII
		PITCH_FACTOR.put(SHIP_KRAIT_MK2, 2.0f);
		ROLL_FACTOR.put(SHIP_KRAIT_MK2, 0.8f);
		YAW_FACTOR.put(SHIP_KRAIT_MK2, 2.5f);

		// Orca
		PITCH_FACTOR.put(SHIP_ORCA, 1.0f);
		ROLL_FACTOR.put(SHIP_ORCA, 1.0f);
		YAW_FACTOR.put(SHIP_ORCA, 1.0f);

		// Type-9
		PITCH_FACTOR.put(SHIP_TYPE9, 3.0f);
		ROLL_FACTOR.put(SHIP_TYPE9, 4.0f);
		YAW_FACTOR.put(SHIP_TYPE9, 4.5f);
	}

	public static final int UI_NEXT_TAB = KeyEvent.VK_PAGE_UP;
	public static final int UI_PREV_TAB = KeyEvent.VK_PAGE_DOWN;
	public static final int DEPLOY_HEATSINK = KeyEvent.VK_H;

	@Autowired
	private Robot robot = null;
	private Rectangle screenRect = null;
	private MouseUtil mouseUtil = null;
	private String myShip = null;

	private int pitchUp = 0;
	private int pitchDown = 0;
	private int rollRight = 0;
	private int rollLeft = 0;
	private int yawRight = 0;
	private int yawLeft = 0;
	private int throttle = 0;
	private long lastThrottleChange = 0;

	public ShipControl() {
		GraphicsDevice primaryScreen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		this.screenRect = new Rectangle(primaryScreen.getDisplayMode().getWidth(), primaryScreen.getDisplayMode().getHeight());
		logger.debug("Primary screen resolution is " + this.screenRect.width + "x" + this.screenRect.height);

		this.mouseUtil = new MouseUtil(screenRect.width, screenRect.height, 1920, 1080);
	}

	public String getMyShip() {
		return myShip;
	}

	public void setMyShip(String myShip) {
		this.myShip = myShip;
	}

	public long getPitch180TimeMillis() {
		return PITCH_180_MILLIS.getOrDefault(this.myShip, 20000L);
	}

	/**
	 * Does not sleep artificially
	 */
	public synchronized void mouseMoveOnScreen(int xOnScreen, int yOnScreen) {
		this.robot.mouseMove(xOnScreen, yOnScreen);
	}

	/**
	 * Does not sleep artificially
	 */
	public synchronized void mouseMove(int xInImage, int yInImage) {
		Point pScreen = this.mouseUtil.imageToScreen(new Point(xInImage, yInImage));
		this.robot.mouseMove(pScreen.x, pScreen.y);
	}

	/**
	 * Sleeps between mouse move and click, but not before and after
	 */
	public synchronized void leftClick(int xInImage, int yInImage) throws InterruptedException {
		this.mouseMove(xInImage, yInImage);
		Thread.sleep(200 + (long) (Math.random() * 100));
		this.leftClick();
	}

	/**
	 * Sleeps between button press and release, but not before and after
	 */
	public synchronized void leftClick() throws InterruptedException {
		this.robot.mousePress(InputEvent.getMaskForButton(1));
		Thread.sleep(100 + (long) (Math.random() * 50));
		this.robot.mouseRelease(InputEvent.getMaskForButton(1));
	}

	public synchronized void type(String text) throws InterruptedException {
		for (int i = 0; i < text.length(); i++) {
			char c = text.toUpperCase().charAt(i);
			this.robot.keyPress(c);
			Thread.sleep(100 + (long) (Math.random() * 50));
			this.robot.keyRelease(c);
			Thread.sleep(100 + (long) (Math.random() * 50));
		}
	}

	public synchronized void releaseAllKeys() {
		KeyDownThread.interruptAll();
	}

	public synchronized void fullStop() throws InterruptedException {
		logger.warn("Full stop requested. Stop turning!");
		this.stopTurning();
		logger.warn("Full stop requested. Throttle to 0!");
		this.setThrottle(0);
		logger.warn("Full stop requested. Release all keys!");
		this.releaseAllKeys();
	}

	public synchronized void stopTurning() {
		this.setPitchUp(0);
		this.setPitchDown(0);
		this.setRollRight(0);
		this.setRollLeft(0);
		this.setYawRight(0);
		this.setYawLeft(0);
	}

	public int getPitchUp() {
		return pitchUp;
	}

	public synchronized void setPitchUp(int percent) {
		if (percent < 0) {
			this.setPitchDown(-1 * percent);
		} else {
			this.pitchDown = 0;
			this.pressKeyPercentOfTime(0, KeyEvent.VK_X);

			this.pitchUp = Math.min(100, Math.max(0, Math.round(PITCH_FACTOR.getOrDefault(this.myShip, 1.0f) * percent)));
			this.pressKeyPercentOfTime(this.pitchUp, KeyEvent.VK_W);
		}
	}

	public int getPitchDown() {
		return pitchDown;
	}

	public synchronized void setPitchDown(int percent) {
		if (percent < 0) {
			this.setPitchUp(-1 * percent);
		} else {
			this.pitchUp = 0;
			this.pressKeyPercentOfTime(0, KeyEvent.VK_W);

			this.pitchDown = Math.min(100, Math.max(0, Math.round(PITCH_FACTOR.getOrDefault(this.myShip, 1.0f) * percent)));
			this.pressKeyPercentOfTime(this.pitchDown, KeyEvent.VK_X);
		}
	}

	public int getRollRight() {
		return rollRight;
	}

	public synchronized void setRollRight(int percent) {
		if (percent < 0) {
			this.setRollLeft(-1 * percent);
		} else {
			this.rollLeft = 0;
			this.pressKeyPercentOfTime(0, KeyEvent.VK_Q);

			this.rollRight = Math.min(100, Math.max(0, Math.round(ROLL_FACTOR.getOrDefault(this.myShip, 1.0f) * percent)));
			this.pressKeyPercentOfTime(this.rollRight, KeyEvent.VK_E);
		}
	}

	public int getRollLeft() {
		return rollLeft;
	}

	public synchronized void setRollLeft(int percent) {
		if (percent < 0) {
			this.setRollRight(-1 * percent);
		} else {
			this.rollRight = 0;
			this.pressKeyPercentOfTime(0, KeyEvent.VK_E);

			this.rollLeft = Math.min(100, Math.max(0, Math.round(ROLL_FACTOR.getOrDefault(this.myShip, 1.0f) * percent)));
			this.pressKeyPercentOfTime(this.rollLeft, KeyEvent.VK_Q);
		}
	}

	public int getYawRight() {
		return yawRight;
	}

	public synchronized void setYawRight(int percent) {
		if (percent < 0) {
			this.setYawLeft(-1 * percent);
		} else {
			this.yawLeft = 0;
			this.pressKeyPercentOfTime(0, KeyEvent.VK_A);

			this.yawRight = Math.min(100, Math.max(0, Math.round(YAW_FACTOR.getOrDefault(this.myShip, 1.0f) * percent)));
			this.pressKeyPercentOfTime(this.yawRight, KeyEvent.VK_D);
		}
	}

	public int getYawLeft() {
		return yawLeft;
	}

	public synchronized void setYawLeft(int percent) {
		if (percent < 0) {
			this.setYawRight(-1 * percent);
		} else {
			this.yawRight = 0;
			this.pressKeyPercentOfTime(0, KeyEvent.VK_D);

			this.yawLeft = Math.min(100, Math.max(0, Math.round(YAW_FACTOR.getOrDefault(this.myShip, 1.0f) * percent)));
			this.pressKeyPercentOfTime(this.yawLeft, KeyEvent.VK_A);
		}
	}

	public int getThrottle() {
		return throttle;
	}

	public synchronized void setThrottle(int throttle) throws InterruptedException {
		long millisSinceLastChange = System.currentTimeMillis() - this.lastThrottleChange;
		if (millisSinceLastChange < 50) {
			Thread.sleep(50 - millisSinceLastChange);
		}
		this.lastThrottleChange = System.currentTimeMillis();

		if (throttle < 25) {
			this.throttle = 0;
			this.pressKey(KeyEvent.VK_6);
		} else if (throttle < 50) {
			this.throttle = 25;
			this.pressKey(KeyEvent.VK_7);
		} else if (throttle < 75) {
			this.throttle = 50;
			this.pressKey(KeyEvent.VK_8);
		} else if (throttle < 100) {
			this.throttle = 75;
			this.pressKey(KeyEvent.VK_9);
		} else {
			this.throttle = 100;
			this.pressKey(KeyEvent.VK_0);
		}
	}

	public synchronized void deployHeatsink() {
		this.pressKey(DEPLOY_HEATSINK);
	}

	public synchronized void toggleFsd() {
		this.pressKey(KeyEvent.VK_J);
	}

	public synchronized void toggleSystemMap() {
		this.robot.mouseMove(1, 1);
		this.pressKey(KeyEvent.VK_S);
	}

	public synchronized void toggleGalaxyMap() {
		this.robot.mouseMove(1, 1);
		this.pressKey(KeyEvent.VK_G);
	}

	public synchronized void honk() {
		this.pressKey(KeyEvent.VK_SPACE, 8000);
	}

	public synchronized void honkDelayed(final long millis) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(millis);
					ShipControl.this.honk();
				} catch (InterruptedException e) {
					// Quit
				}
			}
		}).start();
	}

	/**
	 * Navigation
	 */
	public synchronized long uiLeftPanel() {
		return this.pressKey(KeyEvent.VK_1);
	}

	/**
	 * Systems
	 */
	public synchronized long uiRightPanel() {
		return this.pressKey(KeyEvent.VK_4);
	}

	/**
	 * Comms
	 */
	public synchronized long uiTopPanel() {
		return this.pressKey(KeyEvent.VK_2);
	}

	/**
	 * Roles
	 */
	public synchronized long uiBottomPanel() {
		return this.pressKey(KeyEvent.VK_3);
	}

	public synchronized long uiNextTab() {
		return this.pressKey(KeyEvent.VK_NUMPAD9);
	}

	public synchronized long uiPrevTab() {
		return this.pressKey(KeyEvent.VK_NUMPAD3);
	}

	public synchronized long uiUp() {
		return this.pressKey(KeyEvent.VK_NUMPAD8);
	}

	public synchronized long uiDown() {
		return this.pressKey(KeyEvent.VK_NUMPAD2);
	}

	public synchronized long uiRight() {
		return this.pressKey(KeyEvent.VK_NUMPAD6);
	}

	public synchronized long uiLeft() {
		return this.pressKey(KeyEvent.VK_NUMPAD4);
	}

	public synchronized long uiUp(long millis) {
		return this.pressKey(KeyEvent.VK_NUMPAD8, millis);
	}

	public synchronized long uiDown(long millis) {
		return this.pressKey(KeyEvent.VK_NUMPAD2, millis);
	}

	public synchronized long uiRight(long millis) {
		return this.pressKey(KeyEvent.VK_NUMPAD6, millis);
	}

	public synchronized long uiLeft(long millis) {
		return this.pressKey(KeyEvent.VK_NUMPAD4, millis);
	}

	public synchronized long uiSelect() {
		return this.pressKey(KeyEvent.VK_ENTER);
	}

	public synchronized long uiBack() {
		return this.pressKey(KeyEvent.VK_BACK_SPACE);
	}

	public synchronized long selectNextSystemInRoute() {
		return this.pressKey(KeyEvent.VK_R);
	}

	public synchronized long selectHighestThreat() {
		return this.pressKey(KeyEvent.VK_NUMPAD5);
	}

	public synchronized long selectNextTarget() {
		return this.pressKey(KeyEvent.VK_COMMA);
	}

	public synchronized long selectPrevTarget() {
		// TODO selectPrevTarget
		return 0;
	}

	public synchronized long selectNextHostileTarget() {
		return this.pressKey(KeyEvent.VK_DECIMAL);
	}

	public synchronized long selectPrevHostileTarget() {
		// TODO selectPrevHostileTarget
		return 0;
	}

	public synchronized long targetWingman1() {
		return this.pressKey(KeyEvent.VK_I);
	}

	public synchronized long targetWingman2() {
		return this.pressKey(KeyEvent.VK_O);
	}

	public synchronized long targetWingman3() {
		return this.pressKey(KeyEvent.VK_P);
	}

	public synchronized long selectWingmanTarget() {
		// TODO selectPrevHostileTarget ß
		return 0;
	}

	public synchronized long wingmanNavlock() {
		// TODO selectPrevHostileTarget ´
		return 0;
	}

	public synchronized long fighterOrderRecall() {
		return this.pressKey(KeyEvent.VK_F5);
	}

	public synchronized long fighterOrderDefend() {
		return this.pressKey(KeyEvent.VK_F6);
	}

	public synchronized long fighterOrderAttack() {
		return this.pressKey(KeyEvent.VK_F7);
	}

	public synchronized long saveShadowplay() {
		logger.info("Saving shadowplay");
		return this.pressKey(KeyEvent.VK_F8);
	}

	public synchronized long exitToMainMenu() {
		return this.exitToMainMenu(16000); // Wait more than 15s because most likely we are in danger
	}

	public synchronized long exitToMainMenu(long dangerWaitMillis) {
		try {
			// Compute mouse positions for menu
			Point pExitToMainMenu = mouseUtil.imageToScreen(new Point(200, 390));
			Point pYes = mouseUtil.imageToScreen(new Point(900, 660));

			// Start exiting
			this.robot.mouseMove(1, 1);
			logger.warn("ESC");
			this.pressKey(KeyEvent.VK_ESCAPE, 250);
			Thread.sleep(500);
			this.robot.mouseMove(pExitToMainMenu.x, pExitToMainMenu.y);
			Thread.sleep(500);
			this.leftClick();
			Thread.sleep(dangerWaitMillis);
			this.robot.mouseMove(pYes.x, pYes.y);
			Thread.sleep(500);
			this.leftClick();
			logger.warn("Exited to main menu");
		} catch (InterruptedException e) {
			logger.error("Interrupted while exiting the game", e);
		}

		return 250 + 500 + 500 + 150 + dangerWaitMillis + 500 + 150;
	}

	private long pressKey(int keyCode) {
		long millis = 85 + (long) (Math.random() * 10);
		return this.pressKey(keyCode, millis);
	}

	private long pressKey(int keyCode, long millis) {
		new KeyPressThread(this.robot, keyCode, millis).start();
		return millis;
	}

	private void pressKeyPercentOfTime(int percent, int keyCode) {
		KeyDownThread kdt = KeyDownThread.lookup(keyCode);
		if (kdt == null) {
			new KeyDownThread(this.robot, keyCode, percent).start();
		} else {
			kdt.setPercent(percent);
		}
	}

	public static class KeyPressThread extends Thread {

		private final Robot robot;
		private final int keyCode;
		private final long millis;

		public KeyPressThread(Robot robot, int keyCode, long millis) {
			this.setName(generateThreadName(keyCode));
			this.setDaemon(true);

			this.robot = robot;
			this.keyCode = keyCode;
			this.millis = millis;
		}

		private static String generateThreadName(int keyCode) {
			return "KeyPressThread-" + keyCode;
		}

		public static void interruptAll() {
			Thread[] tarray = new Thread[Thread.activeCount() + 100];
			Thread.enumerate(tarray);
			for (Thread t : tarray) {
				if (t != null && t instanceof KeyPressThread) {
					t.interrupt();
				}
			}
		}

		public static KeyPressThread lookup(int keyCode) {
			String name = generateThreadName(keyCode);
			Thread[] tarray = new Thread[Thread.activeCount() + 100];
			Thread.enumerate(tarray);
			for (Thread t : tarray) {
				if (t != null && name.equals(t.getName())) {
					return (KeyPressThread) t;
				}
			}
			return null;
		}

		@Override
		public void run() {
			try {
				this.robot.keyPress(this.keyCode);
				Thread.sleep(this.millis);
			} catch (InterruptedException e) {
				// Quit
			}
			this.robot.keyRelease(this.keyCode);
		}

	}

	public static class KeyDownThread extends Thread {

		private final Robot robot;
		private final int keyCode;
		private int percent;

		public KeyDownThread(Robot robot, int keyCode, int percent) {
			this.setName(generateThreadName(keyCode));
			this.setDaemon(true);

			this.robot = robot;
			this.keyCode = keyCode;
			this.percent = percent;
		}

		private static String generateThreadName(int keyCode) {
			return "KeyDownThread-" + keyCode;
		}

		public static void interruptAll() {
			Thread[] tarray = new Thread[Thread.activeCount() + 100];
			Thread.enumerate(tarray);
			for (Thread t : tarray) {
				if (t != null && t instanceof KeyDownThread) {
					t.interrupt();
				}
			}
		}

		public static KeyDownThread lookup(int keyCode) {
			String name = generateThreadName(keyCode);
			Thread[] tarray = new Thread[Thread.activeCount() + 100];
			Thread.enumerate(tarray);
			for (Thread t : tarray) {
				if (t != null && name.equals(t.getName())) {
					return (KeyDownThread) t;
				}
			}
			return null;
		}

		@Override
		public void run() {
			int lastPercent = 0;
			while (this.percent > 0 && this.percent <= 100) {
				try {
					// if (lastPercent == 100 && this.percent == 100) {
					lastPercent = this.percent;
					this.robot.keyPress(this.keyCode);
					Thread.sleep(this.percent * 2);
					// }

					if (this.percent < 100) {
						this.robot.keyRelease(this.keyCode);
						Thread.sleep((100 - this.percent) * 2);
					}
				} catch (InterruptedException e) {
					break;
				}
			}
			this.robot.keyRelease(this.keyCode);
		}

		public int getPercent() {
			return percent;
		}

		public void setPercent(int percent) {
			this.percent = percent;
		}

	}

}
