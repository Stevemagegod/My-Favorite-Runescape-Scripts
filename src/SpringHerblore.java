import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.*;

import com.rarebot.event.listeners.*;
import com.rarebot.event.events.*;
import com.rarebot.script.*;
import com.rarebot.script.methods.*;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.wrappers.*;

@ScriptManifest(authors = { "Spring" }, name = "SpringHerblore", version = 1.0, description = "AIO Herblore")
public class SpringHerblore extends Script implements PaintListener,
  	MessageListener {

	private String action = "Unknown";
	private long lastAnim, startTime, hours, mins, secs;
	private Task currentTask = Task.CLEAN;
	private Extremes extreme = Extremes.ATTACK;
	private Potions potion = Potions.ATTACK;
	private Grind grindItem = Grind.BIRD_NEST;
	private int[] mousePath = MOUSE_PATHS[0];
	private final Antiban anti = new Antiban();
	private final HerbloreGUI gui = new HerbloreGUI();
	private int efficiency = 60;
	private boolean guiActive = true;
	private boolean nextTask = false;
	private int outCount, outCount2;
	private int cleanCount;

	private static final int SPIN_TICKET = 24154;
	private static final int VIAL_OF_WATER = 227;

	private enum Grind {
		UNICORN_HORN(237, "Grind"),
		CHOCOLATE_BAR(1973, "Powder"),
		KEBBIT_TEETH(10109, "Grind"),
		GORAK_CLAW(9016, "Grind"),
		BIRD_NEST(5075, "Grind"),
		GOAT_HORN(9735, "Grind"),
		DRAGON_SCALE(243, "Grind"),
		MUD_RUNE(4698, "Grind");

		private int item;
		private String action;

		private Grind(int i, String a) {
			item = i;
			action = a;
		}

		private int getItem() {
			return item;
		}

		private String getAction() {
			return action;
		}

	}

	private enum Potions {
		ATTACK(199, 249, 91, 221),
		ANTIPOISON(201, 251, 93, 235),
		STRENGTH(203, 253, 95, 225),
		SERUM207(203, 253, 95, 592),
		STAT_RESTORE(205, 255, 97, 223),
		ENERGY(205, 255, 97, 1975),
		DEFENCE(207, 257, 99, 239),
		AGILITY(3049, 2998, 3002, 2152),
		COMBAT(205, 255, 97, 9736),
		PRAYER(207, 257, 99, 231),
		SUMMONING(12174, 12172, 12181, 12109),
		SUPER_ATTACK(209, 259, 101, 221),
		SUPER_ANTIPOSION(209, 259, 101, 235),
		FISHING(211, 261, 103, 231),
		SUPER_ENERGY(211, 261, 103, 2970),
		HUNTER(211, 261, 103, 10111),
		SUPER_STRENGTH(213, 263, 105, 225),
		FLETCHING(14836, 14854, 14856, 11525),
		WEAPON_POISON(213, 263, 105, 241),
		SUPER_RESTORE(3051, 3000, 3004, 223),
		SUPER_DEFENCE(215, 265, 107, 239),
		ANTIFIRE(2485, 2481, 2483, 241),
		RANGING(217, 267, 109, 245),
		MAGIC(2485, 2481, 2483, 3138),
		ZAMORAK(219, 269, 111, 247),
		BREW(3049, 2998, 3002, 6693),
		PRAY_RENEWAL(21626, 21624, 21628, 21622);

		private int grimy;
		private int clean;
		private int unfinished;
		private int secondary;

		private Potions(int grim, int g, int u, int s) {
			grimy = grim;
			clean = g;
			unfinished = u;
			secondary = s;
		}

		private int getGrimy() {
			return grimy;
		}

		private int getClean() {
			return clean;
		}

		private int getUnfinished() {
			return unfinished;
		}

		private int getSecondary() {
			return secondary;
		}

	}

	private enum Extremes {
		ATTACK(145, 261),
		STRENGTH(157, 267),
		DEFENCE(163, 2481),
		RANGING(169, 12539),
		MAGIC(3042, 9594),
		SPECIAL(3018, 5972),
		ANTIFIRE(2454, 4621);

		private int potion;
		private int secondary;

		private Extremes(int potion, int secondary) {
			this.potion = potion;
			this.secondary = secondary;
		}

		private int getPotion() {
			return potion;
		}

		private int getSecondary() {
			return secondary;
		}

	}

	private static final int[][] MOUSE_PATHS = {
			{ 0, 1, 4, 5, 8, 9, 12, 13, 16, 17, 20, 21, 24, 25, 26, 27, 23, 22,
					19, 18, 15, 14, 11, 10, 7, 6, 3, 2 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18,
					19, 20, 21, 22, 23, 24, 25, 26, 27 },
			{ 0, 4, 8, 12, 16, 20, 24, 1, 5, 9, 13, 17, 21, 25, 2, 6, 10, 14,
					18, 22, 26, 3, 7, 11, 15, 19, 23, 27 },
			{ 27, 26, 25, 24, 23, 22, 21, 20, 19, 18, 17, 16, 15, 14, 13, 12,
					11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 },
			{ 0, 1, 2, 3, 7, 6, 5, 4, 8, 9, 10, 11, 15, 14, 13, 12, 11, 16, 17,
					18, 19, 23, 22, 21, 20, 24, 25, 26, 27 },
			{ 3, 2, 1, 0, 4, 5, 6, 7, 11, 10, 9, 8, 12, 13, 14, 15, 19, 18, 17,
					16, 20, 21, 22, 23, 27, 26, 25, 24 } };

	private interface Condition {
		public boolean isValid();
	}

	private void sleepWhile(Condition c, int max) {
		int timer = 0;
		while (c.isValid() && timer < max) {
			action = "Waiting " + (max - timer);
			sleep(1);
			timer++;
		}
	}

	private boolean containsAction(RSItem item, String action) {
		final String[] actions = item.getComponent().getActions();
		if (actions == null || actions.length < 1)
			return false;
		for (String a : actions) {
			if (a != null && a.contains(action)) {
				return true;
			}
		}
		return false;
	}

	private void doPath(int[] path) {
		final int temp = mouse.getSpeed();
		mouse.setSpeed(random(2, 4));
		for (Integer i : path) {
			RSItem item = inventory.getItemAt(i);
			if (item != null && containsAction(item, "Clean")) {
				mouse.move(item.getComponent().getCenter());
				sleep(50, 60);
				item.interact("Clean");
				sleep(50, 60);
			}
		}
		mouse.setSpeed(temp);
	}

	private static int[] BANKERS = { 44, 45, 494, 495, 496, 497, 498, 499, 553,
			909, 958, 1036, 2271, 2354, 2355, 2718, 2759, 3198, 3293, 3416,
			3418, 3824, 4456, 4457, 4458, 4459, 5488, 5901, 5912, 6362, 6532,
			6533, 6534, 6535, 7605, 8948, 9710, 14367 };
	private static int[] BANK_BOOTHS = { 782, 2213, 2995, 5276, 6084, 10517,
			11402, 11758, 12759, 14367, 19230, 20325, 24914, 11338, 25808,
			26972, 29085, 52589, 34752, 35647, 36786, 2012, 2015, 2019, 42217,
			42377, 42378 };
	private static int[] BANK_CHESTS = { 2693, 4483, 8981, 12308, 21301, 20607,
			21301, 27663, 42192 };

	private boolean openBank() {
		try {
			if (!bank.isOpen()) {
				if (menu.isOpen()) {
					mouse.moveSlightly();
					sleep(random(20, 30));
				}
				RSObject bankBooth = objects.getNearest(BANK_BOOTHS);
				RSNPC banker = npcs.getNearest(BANKERS);
				final RSObject bankChest = objects.getNearest(BANK_CHESTS);
				int lowestDist = calc.distanceTo(bankBooth);
				if ((banker != null) && (calc.distanceTo(banker) < lowestDist)) {
					lowestDist = calc.distanceTo(banker);
					bankBooth = null;
				}
				if ((bankChest != null)
						&& (calc.distanceTo(bankChest) < lowestDist)) {
					bankBooth = null;
					banker = null;
				}
				if (((bankBooth != null) && (calc.distanceTo(bankBooth) < 5)
						&& calc.tileOnMap(bankBooth.getLocation()) && calc
						.canReach(bankBooth.getLocation(), true))
						|| ((banker != null) && (calc.distanceTo(banker) < 8)
								&& calc.tileOnMap(banker.getLocation()) && calc
								.canReach(banker.getLocation(), true))
						|| ((bankChest != null)
								&& (calc.distanceTo(bankChest) < 8)
								&& calc.tileOnMap(bankChest.getLocation())
								&& calc.canReach(bankChest.getLocation(), true) && !bank
								.isOpen())) {
					if (bankBooth != null) {
						if (bankBooth.interact("Bank")) {
							int count = 0;
							while (!bank.isOpen() && ++count < 10) {
								sleep(random(200, 400));
								if (getMyPlayer().isMoving()) {
									count = 0;
								}
							}
						}
					} else if (banker != null) {
						if (banker.interact("Bank ")) {
							int count = 0;
							while (!bank.isOpen() && ++count < 10) {
								sleep(random(200, 400));
								if (getMyPlayer().isMoving()) {
									count = 0;
								}
							}
						}
					} else if (bankChest != null) {
						if (bankChest.interact("Bank") || menu.doAction("Use")) {
							int count = 0;
							while (!bank.isOpen() && ++count < 10) {
								sleep(random(200, 400));
								if (getMyPlayer().isMoving()) {
									count = 0;
								}
							}
						}
					}
				} else {
				}
			}
			return bank.isOpen();
		} catch (final Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	private enum Task {
		CLEAN,
		MAKE_POTION,
		MAKE_UNFINISHED,
		GRIND,
		MAKE_EXTREME,
		MAKE_OVERLOAD,
		SLEEP
	}

	private static final String BASE_URL = "http://version.springbots.hostoi.com/";

	private boolean hasIngredients() {
		switch (currentTask) {
		case CLEAN:
			return inventory.containsOneOf(potion.getGrimy());
		case MAKE_POTION:
			return inventory.containsOneOf(potion.getUnfinished())
					&& inventory.containsOneOf(potion.getSecondary());
		case MAKE_UNFINISHED:
			return inventory.containsOneOf(VIAL_OF_WATER)
					&& inventory.containsOneOf(potion.getClean());
		case GRIND:
			if (grindItem == Grind.MUD_RUNE)
				return inventory.getCount(true, Grind.MUD_RUNE.getItem()) > 1
						&& !inventory.isFull();
			else
				return inventory.containsOneOf(grindItem.getItem());
		case MAKE_EXTREME:
			return inventory.containsOneOf(extreme.getPotion())
					&& inventory.containsOneOf(extreme.getSecondary());
		case MAKE_OVERLOAD:
			return inventory.containsOneOf(15309)
					&& inventory.containsOneOf(15313)
					&& inventory.containsOneOf(15317)
					&& inventory.containsOneOf(15321)
					&& inventory.containsOneOf(15325)
					&& inventory.containsOneOf(269);
		}
		return false;
	}

	private double getWebVersion() {
		try {
			final BufferedReader r = new BufferedReader(new InputStreamReader(
					new URL(BASE_URL + getClass().getName() + ".html")
							.openStream()));
			final double d = Double.parseDouble(r.readLine());
			r.close();
			return d;
		} catch (Exception e) {
			e.printStackTrace();
			log("Failed to load web version");
		}
		return getLocalVersion();
	}

	private double getLocalVersion() {
		return (double) getClass().getAnnotation(ScriptManifest.class)
				.version();
	}

	private boolean checkUpdate() {
		return getWebVersion() > getLocalVersion();
	}

	private int getTTLSecs(int skill, int gainedExp) {
		long runTime = System.currentTimeMillis() - startTime;
		int seconds = (int) ((double) skills.getExpToNextLevel(skill) / ((1000.0 / (double) runTime) * (double) gainedExp));
		if (seconds >= 60) {
			int minutes = seconds / 60;
			seconds -= (minutes * 60);
		}
		return seconds;
	}

	private int getTTLMins(int skill, int gainedExp) {
		long runTime = System.currentTimeMillis() - startTime;
		int minutes = 0;
		int seconds = (int) ((double) skills.getExpToNextLevel(skill) / ((1000.0 / (double) runTime) * (double) gainedExp));
		if (seconds >= 60) {
			minutes = seconds / 60;
			seconds -= (minutes * 60);
		}
		if (minutes >= 60) {
			int hours = minutes / 60;
			minutes -= (hours * 60);
		}
		return minutes;
	}

	private int getTTLHours(int skill, int gainedExp) {
		long runTime = System.currentTimeMillis() - startTime;
		int hours = 0;
		int minutes = 0;
		int seconds = (int) ((double) skills.getExpToNextLevel(skill) / ((1000.0 / (double) runTime) * (double) gainedExp));
		if (seconds >= 60) {
			minutes = seconds / 60;
			seconds -= (minutes * 60);
		}
		if (minutes >= 60) {
			hours = minutes / 60;
			minutes -= (hours * 60);
		}
		return hours;
	}

	private String getTTL(int skill, int gainedExp) {
		return "" + getTTLHours(skill, gainedExp) + ":"
				+ getTTLMins(skill, gainedExp) + ":"
				+ getTTLSecs(skill, gainedExp);
	}

	private final Color background = new Color(0, 0, 0, 150);// black
	private final Color background2 = new Color(180, 0, 0, 100);// Dark red
	private final Color background3 = new Color(0, 0, 0, 100);// black
	private final String scriptName = "SpringHerblore V"
			+ getClass().getAnnotation(ScriptManifest.class).version();
	private final Font sans = new Font("sans serif", Font.PLAIN, 12);
	private final int[] skillToPaint = { Skills.HERBLORE };
	private int[] startExp = new int[skillToPaint.length];
	private int[] expGain = new int[startExp.length];
	private boolean isAfk;
	private static final int PAINT_X = 4;
	private static final int PAINT_Y = 35;
	private static final int PAINT_WIDTH = 200;// x
	private static final int PAINT_INFO_X = 8;
	private static final int PAINT_SPACING = 15;
	private static final int FIRST_LINE = PAINT_Y + 20;
	private static final int PAINT_BOX_TOP = 111;
	private static final int PAINT_BOX_HEIGHT = 25;
	private static final int PAINT_TEXT_TOP = 122;
	private static final int PAINT_TEXT_TOP_2 = 131;

	private String formatNumber(int paramInt) {
		if (paramInt >= 100000)
			return paramInt / 1000 + "K";
		double d;
		if (paramInt >= 10000) {
			d = round(paramInt / 1000.0D, 1);
			return d + "K";
		}
		if (paramInt >= 1000) {
			d = round(paramInt / 1000.0D, 2);
			return d + "K";
		}
		return paramInt + "";
	}

	private double round(double paramDouble, int paramInt) {
		BigDecimal localBigDecimal = new BigDecimal(
				Double.toString(paramDouble));
		localBigDecimal = localBigDecimal.setScale(paramInt, 4);
		return localBigDecimal.doubleValue();
	}

	public void onRepaint(Graphics g) {
		long millis = System.currentTimeMillis() - startTime;
		hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		mins = millis / (1000 * 60);
		millis -= mins * (1000 * 60);
		secs = millis / 1000;
		int herbH = (int) ((cleanCount) * 3600000D / (System
				.currentTimeMillis() - startTime));

		if (isAfk) {
			action = "Antiban - AFK";
			g.setColor(new Color(0, 0, 0, 75));
			g.fillRect(0, 0, game.getWidth(), game.getHeight());
		}
		for (int i = 0; i < startExp.length; i++) {
			if (startExp[i] < 1) {
				startExp[i] = skills.getCurrentExp(skillToPaint[i]);
			}
		}
		for (int i = 0; i < startExp.length; i++) {
			if (startExp[i] > 0) {
				expGain[i] = skills.getCurrentExp(skillToPaint[i])
						- startExp[i];
			}
		}
		Point clientCursor = mouse.getLocation();
		g.setColor(background2);
		g.fillRect(clientCursor.x - 5, clientCursor.y, 12, 2);
		g.fillRect(clientCursor.x, clientCursor.y - 5, 2, 12);
		int currentPos = FIRST_LINE;
		int boxCount = 0;
		int barCount = 0;
		g.setColor(background);
		g.fillRect(PAINT_X, PAINT_Y, PAINT_WIDTH, 20);
		g.setColor(Color.black);
		g.drawRect(PAINT_X, PAINT_Y, PAINT_WIDTH, 20);
		g.setColor(Color.black);
		g.drawString(scriptName, 46, PAINT_Y + 16);// Shadow
		g.setColor(Color.red);
		g.drawString(scriptName, 45, PAINT_Y + 15);// ScriptName
		// Information
		g.setColor(background3);
		g.fillRect(PAINT_X, PAINT_Y + 20, 200, 80);
		g.setColor(Color.black);
		g.drawRect(PAINT_X, PAINT_Y + 20, 200, 80);
		// Strings
		g.setFont(sans);
		g.setColor(Color.white);
		g.drawString("Time Running: " + hours + ":" + mins + ":" + secs,
				PAINT_INFO_X, currentPos += PAINT_SPACING);
		g.setColor(Color.green);
		g.drawString(
				"Time to level: "
						+ (expGain[0] > 0 ? getTTL(skillToPaint[0], expGain[0])
								: "Unknown"), PAINT_INFO_X,
				currentPos += PAINT_SPACING);
		g.setColor(Color.yellow);
		g.drawString(currentTask == Task.CLEAN ? "Herbs cleaned: "
				+ formatNumber(cleanCount) : "Potions made: "
				+ formatNumber(cleanCount), PAINT_INFO_X,
				currentPos += PAINT_SPACING);
		g.drawString(currentTask == Task.CLEAN ? "Herbs/H: "
				+ formatNumber(herbH) : "Potions/H: " + herbH, PAINT_INFO_X,
				currentPos += PAINT_SPACING);
		g.setColor(Color.cyan);
		g.drawString("Activity: " + action, PAINT_INFO_X,
				currentPos += PAINT_SPACING);
		for (int i = 0; i < startExp.length; i++) {
			if (expGain[i] > 0) {
				boxCount++;
			}
		}
		g.setColor(background3);
		g.fillRect(PAINT_X, PAINT_Y + 100, 200, (25 * boxCount));
		g.setColor(Color.black);
		g.drawRect(PAINT_X, PAINT_Y + 100, 200, (25 * boxCount));
		for (int i = 0; i < startExp.length; i++) {
			if (expGain[i] > 0) {
				final int expH = (int) ((expGain[i]) * 3600000D / (System
						.currentTimeMillis() - startTime));
				barCount++;
				g.setColor(background2);
				g.fillRect(PAINT_X + 1, PAINT_BOX_TOP
						+ (PAINT_BOX_HEIGHT * barCount),
						(skills.getPercentToNextLevel(skillToPaint[i]) * 2), 24);
				g.setColor(Color.white);
				g.setFont(new Font("Verdana", Font.PLAIN, 9));
				g.drawString(
						Skills.getSkillName(skillToPaint[i])
								+ " level: "
								+ skills.getCurrentLevel(skillToPaint[i])
								+ " | "
								+ formatNumber(skills
										.getExpToNextLevel(skillToPaint[i]))
								+ " to lvl", PAINT_X + 5, PAINT_TEXT_TOP
								+ (barCount * 25));
				g.drawString("XP Gained " + formatNumber(expGain[i]) + " | "
						+ formatNumber(expH) + "/H", PAINT_X + 5,
						PAINT_TEXT_TOP_2 + (barCount * 25));
				g.setColor(Color.black);
				g.drawLine(PAINT_X + 1, PAINT_BOX_TOP
						+ (PAINT_BOX_HEIGHT * (barCount + 1) - 1), 203,
						PAINT_BOX_TOP + (PAINT_BOX_HEIGHT * (barCount + 1)) - 1);
			}
		}
	}

	public boolean onStart() {
		mouse.setSpeed(8);
		startTime = System.currentTimeMillis();
		gui.setVisible(true);
		return true;
	}

	public void onFinish() {

	}

	public int loop() {
		if (guiActive || isAfk)
			return 500;
		if (getMyPlayer().getAnimation() != -1)
			lastAnim = System.currentTimeMillis();
		try {
			RSComponent collect = interfaces.getComponent(109, 14);
			RSComponent report = interfaces.getComponent(594, 17);
			RSItem ticket = inventory.getItem(SPIN_TICKET);
			if (ticket != null) {
				log("Squeal of fortune ticket was found, we are claiming it.");
				if (bank.isOpen())
					bank.close();
				if (ticket.interact("Claim"))
					sleepWhile(new Condition() {
						public boolean isValid() {
							return inventory.containsOneOf(SPIN_TICKET);
						}
					}, 1000);
			}
			if (report.isValid())
				if (report.doClick())
					sleep(200, 600);
			if (collect.isValid())
				if (collect.doClick())
					sleep(200, 600);
			switch (currentTask) {
			case GRIND:
				if (hasIngredients()) {
					if (bank.isOpen())
						if (bank.close())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return bank.isOpen();
								}
							}, 1000);
					if (System.currentTimeMillis() - lastAnim > 3000) {
						final RSInterface menu = interfaces.get(905);
						if (menu.isValid())
							menu.getComponent(14).doClick();
						else {
							final RSItem grind = inventory.getItem(grindItem
									.getItem());
							if (grind.interact(grindItem.getAction()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !menu.isValid();
									}
								}, 1800);
						}
					} else {
						action = "Grinding item";
						anti.run();
					}
				} else {
					if (bank.isOpen()) {
						action = "Depositing";
						if (inventory.getCount() > 0) {
							if (grindItem == Grind.MUD_RUNE) {
								if (bank.depositAllExcept(grindItem.getItem()))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return inventory.getCount() > 1;
										}

									}, 1000);
							} else {
								if (bank.depositAll())
									sleepWhile(new Condition() {
										public boolean isValid() {
											return inventory.getCount() > 0;
										}

									}, 1000);
							}
						} else {
							if (bank.getCount(grindItem.getItem()) > 0) {
								if (outCount > 0) {
									log("Grind item was found, reseting count.");
									outCount = 0;
								}
								if (bank.withdraw(grindItem.getItem(), 0))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(grindItem
													.getItem());
										}
									}, 1000);

							} else {
								outCount++;
								log("Ran out of grind items? Check : "
										+ outCount);
								if (outCount > 5) {
									stopScript(true);
									log("We ran out of grind items.");

								}
								return 300;
							}
						}
					} else {
						action = "Opening bank";
						if (openBank())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return !bank.isOpen();
								}
							}, 1000);
					}
				}
				break;
			case MAKE_UNFINISHED:
				if (hasIngredients()) {
					if (bank.isOpen())
						if (bank.close())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return bank.isOpen();
								}
							}, 1000);
					if (System.currentTimeMillis() - lastAnim > 3000) {
						final RSInterface menu = interfaces.get(905);
						if (menu.isValid()) {
							menu.getComponent(14).doClick();
						} else {
							final RSItem herb = inventory.getItem(potion
									.getClean());
							final RSItem vial = inventory
									.getItem(VIAL_OF_WATER);
							if (!inventory.isItemSelected()
									&& herb.interact("Use " + herb.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !inventory.isItemSelected();
									}
								}, 1800);
							if (vial.interact(herb.getName() + " -> "
									+ vial.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !menu.isValid();
									}
								}, 1800);
						}
					} else {
						action = "Making unfinished";
						anti.run();
					}
				} else {
					if (bank.isOpen()) {
						action = "Depositing";
						if (inventory.getCount() > 0) {
							if (bank.depositAll())
								sleepWhile(new Condition() {
									public boolean isValid() {
										return inventory.getCount() > 0;
									}
								}, 1000);
						} else {
							if (bank.getCount(potion.getClean()) > 0) {
								if (outCount > 0) {
									log("Herbs were found, reseting count.");
									outCount = 0;
								}
								if (bank.withdraw(potion.getClean(), 14))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(potion
													.getClean());
										}
									}, 1000);

							} else {
								outCount++;
								log("Ran out of herbs? Check : " + outCount);
								if (outCount > 5) {
									if (nextTask) {
										cleanCount = 0;
										log("We are now finishing potions.");
										currentTask = Task.MAKE_POTION;
									} else {
										stopScript(true);
										log("We ran out of herbs.");
									}
								}
								return 300;
							}
							if (bank.getCount(VIAL_OF_WATER) > 0) {
								if (outCount2 > 0) {
									log("Vial of water were found, reseting count.");
									outCount2 = 0;
								}
								if (bank.withdraw(VIAL_OF_WATER, 14))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(VIAL_OF_WATER);
										}
									}, 1000);

							} else {
								outCount2++;
								log("Ran out of vial of water? Check : "
										+ outCount2);
								if (outCount2 > 5) {
									if (nextTask) {
										cleanCount = 0;
										log("We are now finishing potions.");
										currentTask = Task.MAKE_POTION;
									} else {
										stopScript(true);
										log("We ran out of vial of water.");
									}
								}
							}
						}
					} else {
						action = "Opening bank";
						if (openBank())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return !bank.isOpen();
								}
							}, 1000);
					}
				}
				break;

			case MAKE_POTION:
				if (hasIngredients()) {
					if (bank.isOpen())
						if (bank.close())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return bank.isOpen();
								}
							}, 1000);
					if (System.currentTimeMillis() - lastAnim > 3000) {
						final RSInterface menu = interfaces.get(905);
						if (menu.isValid()) {
							menu.getComponent(14).doClick();
						} else {
							final RSItem unfinished = inventory.getItem(potion
									.getUnfinished());
							final RSItem secondary = inventory.getItem(potion
									.getSecondary());
							if (!inventory.isItemSelected()
									&& unfinished.interact("Use "
											+ unfinished.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !inventory.isItemSelected();
									}
								}, 1800);
							if (secondary.interact(unfinished.getName()
									+ " -> " + secondary.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !menu.isValid();
									}
								}, 1800);
						}
					} else {
						action = "Making potion";
						anti.run();
					}
				} else {
					if (bank.isOpen()) {
						action = "Depositing";
						if (inventory.getCount() > 0) {
							if (bank.depositAll())
								sleepWhile(new Condition() {
									public boolean isValid() {
										return inventory.getCount() > 0;
									}
								}, 1000);
						} else {
							if (bank.getCount(potion.getUnfinished()) > 0) {
								if (outCount > 0) {
									log("Potion(unf) were found, reseting count.");
									outCount = 0;
								}
								if (bank.withdraw(potion.getUnfinished(), 14))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(potion
													.getUnfinished());
										}
									}, 1000);

							} else {
								outCount++;
								log("Ran out of potion(unf)? Check : "
										+ outCount);
								if (outCount > 5) {
									stopScript(true);
									log("We ran out of potion(unf).");
								}
								return 300;
							}
							if (bank.getCount(potion.getSecondary()) > 0) {
								if (outCount2 > 0) {
									log("Secondary was found, reseting count.");
									outCount2 = 0;
								}
								if (bank.withdraw(potion.getSecondary(), 14))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(potion
													.getSecondary());
										}
									}, 1000);

							} else {
								outCount2++;
								log("Ran out of vial of secondary item? Check : "
										+ outCount2);
								if (outCount2 > 5) {
									stopScript(true);
									log("We ran out of secondary item.");
								}
							}
						}
					} else {
						action = "Opening bank";
						if (openBank())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return !bank.isOpen();
								}
							}, 1000);
					}
				}
				break;

			case CLEAN:
				if (hasIngredients()) {
					if (bank.isOpen())
						bank.close();
					doPath(mousePath);
				} else {
					if (bank.isOpen()) {
						mousePath = MOUSE_PATHS[random(0, MOUSE_PATHS.length)];
						action = "Depositing";
						if (inventory.getCount() > 0) {
							if (bank.depositAll())
								sleepWhile(new Condition() {
									public boolean isValid() {
										return inventory.getCount() > 0;
									}
								}, 1000);
						} else {
							if (bank.getCount(potion.getGrimy()) > 0) {
								if (outCount > 0) {
									log("Grimy herbs were found, reseting count.");
									outCount = 0;
								}
								if (bank.withdraw(potion.getGrimy(), 0))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(potion
													.getGrimy());
										}
									}, 1000);

							} else {
								outCount++;
								log("Ran out of grimy herbs? Check : "
										+ outCount);
								if (outCount > 5) {
									if (nextTask) {
										cleanCount = 0;
										log("We are now making unfinished potions.");
										currentTask = Task.MAKE_UNFINISHED;
									} else {
										stopScript(true);
										log("We ran out of grimy herbs.");
									}
								}
							}
						}
					} else {
						action = "Opening bank";
						if (bank.open())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return !bank.isOpen();
								}
							}, 1000);
					}
				}
				break;

			case MAKE_EXTREME:
				if (hasIngredients()) {
					if (bank.isOpen())
						if (bank.close())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return bank.isOpen();
								}
							}, 1000);
					if (System.currentTimeMillis() - lastAnim > 3000) {
						final RSInterface menu = interfaces.get(905);
						if (menu.isValid()) {
							menu.getComponent(14).doClick();
						} else {
							final RSItem unfinished = inventory.getItem(extreme
									.getPotion());
							final RSItem secondary = inventory.getItem(extreme
									.getSecondary());
							if (!inventory.isItemSelected()
									&& unfinished.interact("Use "
											+ unfinished.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !inventory.isItemSelected();
									}
								}, 1800);
							if (secondary.interact(unfinished.getName()
									+ " -> " + secondary.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !menu.isValid();
									}
								}, 1800);
						}
					} else {
						action = "Making extreme";
						anti.run();
					}
				} else {
					if (bank.isOpen()) {
						action = "Depositing";
						if (inventory.getCount() > 0) {
							if (extreme == Extremes.RANGING) {
								if (bank.depositAllExcept(extreme
										.getSecondary()))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return inventory.getCount() > 1;
										}
									}, 1000);
							} else {
								if (extreme == Extremes.RANGING)
									if (bank.depositAllExcept(extreme
											.getSecondary()))
										sleepWhile(new Condition() {
											public boolean isValid() {
												return inventory.getCount() > 0;
											}
										}, 1000);
									else if (bank.depositAll())
										sleepWhile(new Condition() {
											public boolean isValid() {
												return inventory.getCount() > 0;
											}
										}, 1000);
							}
						} else {
							if (bank.getCount(extreme.getSecondary()) > 0) {
								if (outCount2 > 0) {
									log("Secondary was found, reseting count.");
									outCount2 = 0;
								}
								if (bank.withdraw(extreme.getSecondary(), 14))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(extreme
													.getSecondary());
										}
									}, 1000);

							} else {
								outCount2++;
								log("Ran out of vial of secondary item? Check : "
										+ outCount2);
								if (outCount2 > 5) {
									stopScript(true);
									log("We ran out of secondary item.");
								}
							}
							if (bank.getCount(extreme.getPotion()) > 0) {
								if (outCount > 0) {
									log("Potions were found, reseting count.");
									outCount = 0;
								}
								if (extreme == Extremes.RANGING) {
									if (bank.withdraw(extreme.getPotion(), 0))
										sleepWhile(new Condition() {
											public boolean isValid() {
												return !inventory.containsOneOf(extreme
														.getPotion());
											}
										}, 1000);
								} else {
									if (bank.withdraw(extreme.getPotion(), 14))
										sleepWhile(new Condition() {
											public boolean isValid() {
												return !inventory.containsOneOf(extreme
														.getPotion());
											}
										}, 1000);
								}
							} else {
								outCount++;
								log("Ran out of potions? Check : " + outCount);
								if (outCount > 5) {
									stopScript(true);
									log("We ran out of potions.");
								}
								return 300;
							}
						}
					} else {
						action = "Opening bank";
						if (openBank())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return !bank.isOpen();
								}
							}, 1000);
					}
				}
				break;

			case MAKE_OVERLOAD:
				if (hasIngredients()) {
					if (bank.isOpen())
						if (bank.close())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return bank.isOpen();
								}
							}, 1000);
					if (System.currentTimeMillis() - lastAnim > 3000) {
						final RSInterface menu = interfaces.get(905);
						if (menu.isValid()) {
							menu.getComponent(14).doClick();
						} else {
							final RSItem herb = inventory.getItem(269);
							final RSItem pot = inventory.getItem(new int[] {
									15309, 15313, 15317, 15321, 15325 });
							if (!inventory.isItemSelected()
									&& herb.interact("Use " + herb.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !inventory.isItemSelected();
									}
								}, 1800);
							if (pot.interact(herb.getName() + " -> "
									+ pot.getName()))
								sleepWhile(new Condition() {
									public boolean isValid() {
										return !menu.isValid();
									}
								}, 1800);
						}
					} else {
						action = "Making overload";
						anti.run();
					}
				} else {
					if (bank.isOpen()) {
						action = "Depositing";
						if (inventory.getCount() > 0) {
							if (bank.depositAll())
								sleepWhile(new Condition() {
									public boolean isValid() {
										return inventory.getCount() > 0;
									}
								}, 1000);
						} else {
							if (bank.getCount(new int[] { 15309, 15313, 15317,
									15321, 15325, 269 }) > 0) {
								if (outCount > 0) {
									log("Ingredients were found, reseting count.");
									outCount = 0;
								}
								if (bank.withdraw(15313, 4))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(15313);
										}
									}, 1000);
								if (bank.withdraw(15317, 4))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(15317);
										}
									}, 1000);
								if (bank.withdraw(15321, 4))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(15321);
										}
									}, 1000);
								if (bank.withdraw(15325, 4))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(15325);
										}
									}, 1000);
								if (bank.withdraw(269, 4))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(269);
										}
									}, 1000);
								if (bank.withdraw(15309, 4))
									sleepWhile(new Condition() {
										public boolean isValid() {
											return !inventory.containsOneOf(15309);
										}
									}, 1000);

							} else {
								outCount++;
								log("Ran out of ingredients? Check : "
										+ outCount);
								if (outCount > 5) {
									stopScript(true);
									log("We ran out of ingredients.");
								}
								return 300;
							}
						}
					} else {
						action = "Opening bank";
						if (openBank())
							sleepWhile(new Condition() {
								public boolean isValid() {
									return !bank.isOpen();
								}
							}, 1000);
					}
				}
				break;
			}
		} catch (Exception e) {
			log("Exception found, if this keeps occurring please report to thread.");
		}
		return random(200, 300);

	}

	@Override
	public void messageReceived(MessageEvent e) {
		String message = e.getMessage();
		if (message.contains("You mix the")
				|| message.contains("You clean the dirt")
				|| message.contains("You put the")
				|| message.contains("You grind"))
			cleanCount++;
	}

	private class Antiban {
		private boolean hoverPlayer() {
			action = "Hover - Player";
			RSPlayer player = null;
			RSPlayer[] validPlayers = players.getAll();

			player = validPlayers[random(0, validPlayers.length - 1)];
			if (player != null) {
				try {
					String playerName = player.getName();
					String myPlayerName = getMyPlayer().getName();
					if (playerName.equals(myPlayerName)) {
						return false;
					}
				} catch (NullPointerException e) {
				}
				try {
					RSTile targetLoc = player.getLocation();
					Point checkPlayer = calc.tileToScreen(targetLoc);
					if (calc.pointOnScreen(checkPlayer) && checkPlayer != null) {
						mouse.click(checkPlayer, 5, 5, false);
					} else {
						return false;
					}
					return true;
				} catch (Exception ignored) {
				}
			}
			return false;
		}

		private void hoverObject() {
			action = "Hover - Object";
			examineRandomObject(5);
			sleep(randGenerator(50, 1000));
			int mousemoveAfter2 = randGenerator(0, 4);
			sleep(randGenerator(100, 800));
			if (mousemoveAfter2 == 1 && mousemoveAfter2 == 2) {
				mouse.move(1, 1, 760, 500);
			}
		}

		private int randGenerator(int min, int max) {
			return min + (int) (java.lang.Math.random() * (max - min));
		}

		private RSTile examineRandomObject(int scans) {
			RSTile start = getMyPlayer().getLocation();
			ArrayList<RSTile> possibleTiles = new ArrayList<RSTile>();
			for (int h = 1; h < scans * scans; h += 2) {
				for (int i = 0; i < h; i++) {
					for (int j = 0; j < h; j++) {
						int offset = (h + 1) / 2 - 1;
						if (i > 0 && i < h - 1) {
							j = h - 1;
						}
						RSTile tile = new RSTile(start.getX() - offset + i,
								start.getY() - offset + j);
						RSObject objectToList = objects.getTopAt(tile);
						if (objectToList != null
								&& calc.tileOnScreen(objectToList.getLocation())
								&& objectToList.getLocation() != null) {
							possibleTiles.add(objectToList.getLocation());
						}
					}
				}
			}
			if (possibleTiles.size() == 0) {
				return null;
			}
			if (possibleTiles.size() > 0 && possibleTiles != null) {
				final RSTile objectLoc = possibleTiles.get(randGenerator(0,
						possibleTiles.size()));
				Point objectPoint = calc.tileToScreen(objectLoc);
				if (objectPoint != null) {
					try {
						mouse.move(objectPoint);
						if (menu.doAction("xamine")) {
						} else {
						}
						sleep(random(100, 500));
					} catch (NullPointerException ignored) {
					}
				}
			}
			return null;
		}

		private void run() {
			final int chckObj = random(1, (16000 / efficiency));
			final int hover = random(1, (12000 / efficiency));
			final int checkxp = random(1, (12000 / efficiency));
			final int afk = random(1, (12000 / efficiency));
			final int camerahh = random(1, (1800 / efficiency));
			final int hoverObject = random(1, (8000 / efficiency));
			if (efficiency != 0) {
				if (chckObj == 5) {
					action = "AB - Random prayer";
					game.openTab(Tab.PRAYER);
					sleep(random(300, 500));
					mouse.move(644, 394, 51, 6);
					sleep(random(900, 1600));
					mouse.move(644, 394, 51, 6);
					sleep(random(500, 1000));
					mouse.moveRandomly(500);
					sleep(random(400, 900));
				} else if (hover == 5) {
					hoverPlayer();
					sleep(random(1150, 2800));
					mouse.moveRandomly(750);
					sleep(random(400, 1000));
				}
			} else if (hoverObject == 5) {
				action = "AB - Hovering object";
				hoverObject();
				sleep(random(1150, 2800));
				mouse.moveRandomly(750);
				sleep(random(400, 1000));
			} else if (checkxp == 5) {
				action = "AB - checking exp";
				final int GambleInt5 = random(0, 100);
				if (GambleInt5 > 50) {
					game.openTab(Tab.STATS);
					sleep(random(400, 800));
					mouse.move(613, 351, 30, 15);
					sleep(random(200, 1200));
					mouse.move(613, 351, 30, 15);
					sleep(random(900, 1750));
					mouse.moveRandomly(700);
					sleep(random(300, 800));
					game.openTab(Tab.MAGIC);
				}
			} else if (afk == 5) {
				action = "AB - Afking";
				isAfk = true;
				sleep(random(5000, 18000));
				isAfk = false;
			} else if (camerahh == 5) {
				action = "AB - Rotating camera";
				int randomTurn = random(1, 4);
				switch (randomTurn) {
				case 1:
					new CameraRotateThread().start();
					break;
				case 2:
					new CameraHeightThread().start();
					break;
				case 3:
					int randomFormation = random(0, 2);
					if (randomFormation == 0) {
						new CameraRotateThread().start();
						new CameraHeightThread().start();
					} else {
						new CameraHeightThread().start();
						mouse.moveRandomly(200);
						new CameraRotateThread().start();
					}
				}
			}
			return;
		}

		private class CameraRotateThread extends Thread {
			public void run() {
				log("Rotating camera");
				char LR = KeyEvent.VK_RIGHT;
				if (random(0, 2) == 0) {
					LR = KeyEvent.VK_LEFT;
				}
				keyboard.pressKey(LR);
				try {
					Thread.sleep(random(450, 2600));
				} catch (final Exception ignored) {
				}
				keyboard.releaseKey(LR);
			}
		}

		private class CameraHeightThread extends Thread {
			public void run() {
				log("Rotating camera");
				char UD = KeyEvent.VK_UP;
				if (random(0, 2) == 0) {
					UD = KeyEvent.VK_DOWN;
				}
				keyboard.pressKey(UD);
				try {
					Thread.sleep(random(450, 1700));
				} catch (final Exception ignored) {
				}
				keyboard.releaseKey(UD);
			}
		}
	}

	public class HerbloreGUI extends JFrame {
		private static final long serialVersionUID = 1L;

		public HerbloreGUI() {
			initComponents();
		}

		private void sliderStateChanged(ChangeEvent e) {
			label5.setText("Antiban efficiency (current: " + slider.getValue()
					+ ")");
			label5.setBounds(new Rectangle(new Point(45, 160), label5
					.getPreferredSize()));
		}

		private void startButtonAction(ActionEvent e) {
			dispose();
			if (updateBox.isSelected()) {
				log("Checking for updates...");
				if (checkUpdate())
					log(Color.RED,
							"An update is avaiable, please visit the thread.");
				else
					log("No update needed.");
			}
			currentTask = Task.values()[taskBox.getSelectedIndex()];
			log("Current task: " + currentTask.toString());
			switch (taskBox.getSelectedIndex()) {
			case 0:
				potion = Potions.values()[potionBox.getSelectedIndex()];
				log("Current potion: " + potion.toString());
				break;
			case 1:
				potion = Potions.values()[potionBox.getSelectedIndex()];
				log("Current potion: " + potion.toString());
				break;
			case 2:
				potion = Potions.values()[potionBox.getSelectedIndex()];
				log("Current potion: " + potion.toString());
				break;
			case 3:
				grindItem = Grind.values()[potionBox.getSelectedIndex()];
				log("We are grinding: " + grindItem.toString());
				break;
			case 4:
				extreme = Extremes.values()[potionBox.getSelectedIndex()];
				log("Current extreme: " + extreme.toString());
				break;
			}
			nextTask = checkBox.isSelected();
			if (nextTask)
				log("We are going to next task.");
			guiActive = false;
		}

		private void taskBoxChanged(ItemEvent e) {
			switch (taskBox.getSelectedIndex()) {
			case 0:
				label2.setText("Current potion:");
				potionBox.setModel(new DefaultComboBoxModel(Potions.values()));
				break;
			case 1:
				label2.setText("Current potion:");
				potionBox.setModel(new DefaultComboBoxModel(Potions.values()));
				break;
			case 2:
				label2.setText("Current potion:");
				potionBox.setModel(new DefaultComboBoxModel(Potions.values()));
				break;
			case 3:
				label2.setText("Current item:");
				potionBox.setModel(new DefaultComboBoxModel(Grind.values()));
				break;
			case 4:
				label2.setText("Current potion:");
				potionBox.setModel(new DefaultComboBoxModel(Extremes.values()));
				break;
			case 5:
				label2.setEnabled(false);
				potionBox.setEnabled(false);
				break;
			}
		}

		private void initComponents() {
			label1 = new JLabel();
			taskBox = new JComboBox();
			label2 = new JLabel();
			potionBox = new JComboBox();
			checkBox = new JCheckBox();
			updateBox = new JCheckBox();
			label3 = new JLabel();
			label4 = new JLabel();
			slider = new JSlider();
			label5 = new JLabel();
			startButton = new JButton();

			// ======== this ========
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("SpringHerblore");
			setResizable(false);
			Container contentPane = getContentPane();
			contentPane.setLayout(null);

			// ---- label1 ----
			label1.setText("Current task:");
			label1.setFont(new Font("Dialog", Font.PLAIN, 10));
			contentPane.add(label1);
			label1.setBounds(5, 5, label1.getPreferredSize().width, 20);

			// ---- taskBox ----
			taskBox.setModel(new DefaultComboBoxModel(Task.values()));
			taskBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					taskBoxChanged(e);
				}
			});
			contentPane.add(taskBox);
			taskBox.setBounds(85, 5, 130, taskBox.getPreferredSize().height);

			// ---- label2 ----
			label2.setText("Current potion:");
			label2.setFont(new Font("Dialog", Font.PLAIN, 10));
			contentPane.add(label2);
			label2.setBounds(5, 35, 75, 20);

			// ---- potionBox ----
			potionBox.setModel(new DefaultComboBoxModel(Potions.values()));
			contentPane.add(potionBox);
			potionBox.setBounds(85, 35, 130, 20);

			// ---- checkBox ----
			checkBox.setText("Next task when finished?");
			checkBox.setFont(new Font("Dialog", Font.PLAIN, 10));
			contentPane.add(checkBox);
			checkBox.setBounds(new Rectangle(new Point(40, 65), checkBox
					.getPreferredSize()));

			// ---- updateBox ----
			updateBox.setSelected(true);
			updateBox.setText("Check for updates?");
			updateBox.setFont(new Font("Dialog", Font.PLAIN, 10));
			contentPane.add(updateBox);
			updateBox.setBounds(55, 95, 141, 23);

			// ---- label3 ----
			label3.setText("Please refer to thread for instructions");
			label3.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 10));
			contentPane.add(label3);
			label3.setBounds(new Rectangle(new Point(20, 125), label3
					.getPreferredSize()));

			// ---- label4 ----
			label4.setText("and information about Task system.");
			label4.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 10));
			contentPane.add(label4);
			label4.setBounds(new Rectangle(new Point(25, 140), label4
					.getPreferredSize()));

			// ---- slider ----
			slider.setMinimum(1);
			slider.setValue(70);
			slider.setMinorTickSpacing(10);
			slider.setPaintTicks(true);
			slider.setMaximum(101);
			slider.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					sliderStateChanged(e);
					sliderStateChanged(e);
				}
			});
			contentPane.add(slider);
			slider.setBounds(15, 185, 195, slider.getPreferredSize().height);

			// ---- label5 ----
			label5.setText("Antiban efficiency (current: " + slider.getValue()
					+ ")");
			label5.setFont(new Font("Dialog", Font.PLAIN, 10));
			contentPane.add(label5);
			label5.setBounds(new Rectangle(new Point(45, 160), label5
					.getPreferredSize()));

			// ---- startButton ----
			startButton.setText("Start");
			startButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					startButtonAction(e);
				}
			});
			contentPane.add(startButton);
			startButton.setBounds(5, 220, 215, 50);

			{
				Dimension preferredSize = new Dimension();
				for (int i = 0; i < contentPane.getComponentCount(); i++) {
					Rectangle bounds = contentPane.getComponent(i).getBounds();
					preferredSize.width = Math.max(bounds.x + bounds.width,
							preferredSize.width);
					preferredSize.height = Math.max(bounds.y + bounds.height,
							preferredSize.height);
				}
				Insets insets = contentPane.getInsets();
				preferredSize.width += insets.right;
				preferredSize.height += insets.bottom;
				contentPane.setMinimumSize(preferredSize);
				contentPane.setPreferredSize(preferredSize);
			}
			setSize(230, 300);
			setLocationRelativeTo(getOwner());
		}

		private JLabel label1;
		private JComboBox taskBox;
		private JLabel label2;
		private JComboBox potionBox;
		private JCheckBox checkBox;
		private JCheckBox updateBox;
		private JLabel label3;
		private JLabel label4;
		private JSlider slider;
		private JLabel label5;
		private JButton startButton;
	}
}
