import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Filter;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPath;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSTilePath;

@ScriptManifest(authors = { "wyn10" }, name = "Pwn Miner", version = 2.0, description = "The best.")
public class PwnMiner extends Script implements PaintListener, MessageListener,
  	MouseListener {

	double Version = 2.0;

	// GUI STUFF
	private TestGUI gui = new TestGUI();
	public String Location;
	public String[] mineOres;
	public boolean startProgram = false;
	public boolean Banking = false;
	public int checkDistance = 0;
	public boolean M1D1;

	// TILES
	public RSTile[] Bank;
	public RSTilePath toBank;
	public RSTile atBank;
	public RSTile[] Mine;
	public RSTilePath toMine;
	public RSTile[] MineFally;
	public RSTilePath toMineFally;
	public RSTile[] MineLumby;
	public RSTilePath toMineLumby;
	public RSTile[] MineLRC;
	public RSTilePath toMineLRC;
	public RSTile[] SouthtoBank;
	public RSTilePath toBankSouthLRC;
	public RSTilePath toBankWestLRC;
	public RSTilePath toSouthLRC;
	public RSTilePath toWestLRC;
	public RSTile[] WesttoBank;
	public RSTile[] WesttoSouth;
	public RSTilePath toWestfromSouth;
	public RSTilePath toSouthfromWest;
	public RSTile[] Ladder;
	public RSTilePath toLadderTop;
	public RSTilePath toLadderBottom;

	public RSTile nearLadderDown = new RSTile(3019, 3337);
	public RSTile nearLadderUp = new RSTile(3019, 9737);
	public int LadderDown = 2113;
	public int LadderUp = 6226;

	public RSArea atMine;
	public RSArea atFally;
	public RSArea atLumby;

	public RSTile startPos;

	public RSObject LRCDoor;
	public final int LRC_DOOR = 11714;
	public final RSTile LRCDoorTile = new RSTile(3061, 3374);
	public RSObject LRCStairs;
	public final int LRC_STAIRS = 35921;
	public final int LRC_STAIRSUP = 30943;
	public RSObject LRCRope;
	public final int LRC_ROPE = 45077;
	public final RSTile LRCRopeTile = new RSTile(3014, 9831);
	public final int LRC_INTERFACE = 892;
	public final int LRC_INTERFACE_COMP = 17;
	public final int LRC_BANK = 2738;
	public final RSTile SOUTH_GOLD = new RSTile(3668, 5076);
	public final RSTile WEST_GOLD = new RSTile(3638, 5095);
	public final RSTile SOUTH_COAL = new RSTile(3665, 5091);
	public final RSTile WEST_COAL = new RSTile(3675, 5099);
	private int goldID = 444;
	private int coalID = 453;
	public boolean shouldWait = false;
	public int lastRock = -1;

	public final int TZHAAR_OUT = 9359;
	public final RSTile TZHAAR_OUT_LOC = new RSTile(2480, 5175);
	public final int TZHAAR_IN = 31284;

	public final int TAVERLY_SHORTCUT = 11844;
	public final RSTile TAVERLY_SHORTCUT_BANK = new RSTile(2934, 3355);
	public final RSTile TAVERLY_SHORTCUT_MINE = new RSTile(2938, 3355);

	public final RSTile roomTile = new RSTile(3061, 3376);
	public final RSTile midMine = new RSTile(3050, 9775);

	public static final int[] PICK_IDS = { 1265, 1267, 1269, 1271, 1273, 1275,
			14099, 14107, 15259, 15261 };
	public static final int[] ANIMATIONS = { 624, 625, 626, 627, 628, 629,
			12188, 12189, 6746 };
	public static final int[] BANK_BOOTHS = { 11402, 11758, 11402, 2213, 35647,
			26972, 34752, 782 };
	public final int TZHAAR_BANK = 2619;
	public static final int[] ORE_IDS = { 434, 436, 438, 440, 442, 453, 444,
			447, 449, 451, 6979, 6981, 6983 };

	RSObject currRock = null;
	RSObject nextRock = null;
	int currID;

	public int hitpoints = 200;
	public int trys = 0;
	public int mined = 0;
	public int gemFound = 0;
	public int startlevel;
	public int startexp;
	public int currLevel;
	public int lvlGain;
	public int xpGained;
	public long millis;
	public int xpPerHour;
	public int orePerHour;
	public long minutes;
	public long hours;
	public long seconds;
	public int xpToLevel;
	public double percent;
	public long startTime = System.currentTimeMillis();
	public boolean run;

	public boolean showPaint = true;

	private final int CLAY = 0;
	private final int COPPER = 1;
	private final int TIN = 2;
	private final int IRON = 3;
	private final int SILVER = 4;
	private final int COAL = 5;
	private final int GOLD = 6;
	private final int GRANITE = 7;
	private final int MITHRIL = 8;
	private final int ADAMANTITE = 9;
	private final int RUNITE = 10;
	private final int CONC_GOLD = 11;
	private final int CONC_COAL = 12;
	private final String[] oreID = {
			"11504, 11503, 11505, 9711, 9713, 15503, 15505",
			"14906, 14907, 5779, 5780, 5781, 11961, 11962, 11960, 9710, 9708, 9709, 11937, 11938",
			"11933, 11959, 11957, 11958, 11935, 11934, 9714, 9716, 5777, 5776, 5778",
			"14914, 14913, 2092, 2093, 11955, 11956, 11954, 9718, 9719, 9717, 37307, 37308, 37309, 21282, 21281, 5773, 5774, 5775",
			"37306, 37304, 37305, 11950, 11949, 11948, 15579, 15580, 15581",
			"3032, 3233, 11932, 11931, 11930, 2097, 2096, 5770, 5772, 5771, 11963",
			"5770, 5771, 5772, 9720, 9722, 2099, 2098, 34977, 34976, 5989, 5768, 5769, 45067, 45068, 2109, 2108, 15578, 15576, 15577, 32432, 32433, 32434",
			"6979, 6980, 6981, 6982, 6983, 6984",
			"3280, 3041, 11942, 11943, 11944, 5784, 5786",
			"11941, 11939, 5782, 5783, 3273, 3040, 29233, 29235",
			"45069, 45070", "45076", "5999" };

	public int x;
	public int y;
	public double num;
	public int trys2;

	RSObject banker;

	public antiBan anti = new antiBan();
	public Thread Ban = new Thread(anti);
	public boolean banRun = true;

	Rectangle close = new Rectangle(421, 347, 493, 362);
	Point p;
	private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();

	public void messageReceived(MessageEvent e) {
		String msg = e.getMessage().toLowerCase();
		;
		if (msg.contains("manage to mine two")) {
			mined += 2;
		} else if (msg.contains("manage to")) {
			mined++;
		} else if (msg.contains("just found")) {
			gemFound++;
		} else if (msg.contains("need to subscribe")) {
			log.severe("Out of bank space! Stopping!");
			stopScript();
		} else if (msg.contains("You've just advanced")) {
			env.saveScreenshot(true);
		}
	}

	public void walkPath(RSPath path, int dist) {
		path.traverse();
		while (walking.getDestination() != null
				&& calc.distanceTo(walking.getDestination()) > dist) {
			sleep(10);
		}
	}

	public void walk(RSTile Target, int offset) {
		offset = (int) (offset * Math.random());
		if (random(1, 10) > 5) {
			offset *= -1;
		}
		walking.walkTo(new RSTile(Target.getX() + offset, Target.getY()
				+ offset));
		while (walking.getDestination() != null
				&& calc.distanceTo(walking.getDestination()) >= random(8, 11)
				&& getMyPlayer().isMoving()) {
			sleep(random(500, 900));
		}
	}

	public int getHP() {
		int HP = 0;
		if (interfaces.get(748).getComponent(8).isValid()) {
			HP = Integer
					.parseInt(interfaces.get(748).getComponent(8).getText());
		}
		return HP;
	}

	public void restHP(int hitpoints) {
		if (getHP() >= hitpoints) {
			return;
		}
		log("Resting to " + hitpoints + " HP...");
		for (int i = 0; i <= 10 && !menu.doAction("Rest"); i++) {
			mouse.click(random(713, 731), random(99, 114), false);
			sleep(2000, 4000);
		}
		sleep(3000, 4000);
		if (getMyPlayer().getAnimation() == -1) {
			return;
		}
		while (getHP() < hitpoints && !getMyPlayer().isInCombat()) {
			sleep(400, 600);
		}
		walking.walkTileOnScreen(getMyPlayer().getLocation());
	}

	@Override
	public int loop() {

		if (!walking.isRunEnabled() && (walking.getEnergy() > random(50, 70))) {
			walking.setRun(true);
		}
		if (interfaces.get(18).isValid()) {
			interfaces.get(18).getComponent(36).doClick();
		}
		if (Location.equals("Power-Mining")) {
			if (calc.distanceTo(startPos) > checkDistance) {
				while (getMyPlayer().isMoving() && !currRock.isOnScreen()) {
					sleep(100);
				}
				walking.walkTileMM(startPos);
			}
			if (!shouldBank()) {
				currRock = getRock();
				if (currRock != null) {
					while (calc.distanceBetween(currRock.getLocation(),
							getMyPlayer().getLocation()) > checkDistance) {
						getRock();
					}
					powerMine();
				} else {
					if (menu.isOpen() && menu.contains("Drop")) {
						menu.doAction("Drop");
					}
				}
			}
			if (shouldBank() && !M1D1) {
				inventory.dropAllExcept(PICK_IDS);
			}
		} else if (Location.equals("Living Rock Cavern")
				|| Location.equals("LRC Power-Mining")) {
			if (atFally.contains(getMyPlayer().getLocation())) {
				sleep(random(2000, 3000));
				reEquipItems();
				while (calc.distanceTo(LRCDoorTile) >= 10) {
					if (!game.isLoggedIn()) {
						break;
					}
					walkPath(toMineFally, 5);
				}
			} else if (atLumby.contains(getMyPlayer().getLocation())) {
				reEquipItems();
				while (calc.distanceTo(LRCDoorTile) >= 10) {
					if (!game.isLoggedIn()) {
						break;
					}
					walkPath(toMineLumby, 5);
				}
			}
			if (calc.distanceTo(LRCDoorTile) < 10) {
				doLRCDoorAndStairs();
			}
			if (getMyPlayer().getLocation().getY() > 9000
					&& getMyPlayer().getLocation().getY() < 10000
					&& calc.distanceTo(LRCRopeTile) > 7) {
				walkPath(toMineLRC, 5);
			}
			if (interfaces.get(LRC_INTERFACE).isValid()) {
				interfaces.get(LRC_INTERFACE).getComponent(LRC_INTERFACE_COMP)
						.doClick();
				while (getMyPlayer().getLocation().getY() > 6000
						&& !getMyPlayer().isIdle()) {
					sleep(300);
				}
				walking.walkTileMM(atBank);
				reEquipItems();
			}
			if (calc.distanceTo(LRCRopeTile) <= 10) {
				doLRCRope();
			}

			if (interfaces.get(266).isValid()) {
				interfaces.get(266).getComponent(1).doClick();
			}
			if (interfaces.get(18).isValid()) {
				interfaces.get(18).getComponent(36).doClick();
			}
			if (getMyPlayer().isInCombat() || getHP() < hitpoints
					&& mineOres[0].toString().equals("Concentrated Gold")) {
				shouldWait = false;
				log.severe("Being attacked at gold.");
				if (calc.distanceTo(new RSTile(3654, 5113)) > 3) {
					walk(new RSTile(3654, 5113), 2);
					return random(100, 400);
				} else {
					restHP(hitpoints);
				}
			}

			if (getMyPlayer().isInCombat() || getHP() < hitpoints
					&& mineOres[0].toString().equals("Concentrated Coal")) {
				shouldWait = false;
				log.severe("Being attacked at coal.");
				if (calc.distanceTo(new RSTile(3654, 5113)) > 3) {
					walk(new RSTile(3654, 5113), 2);
					return random(100, 400);
				} else {
					restHP(hitpoints);
				}
			}

			if (Location.equals("Living Rock Cavern")) {
				if (shouldBank() && !nearBank()
						&& mineOres[0].toString().equals("Concentrated Gold")) {
					if (calc.distanceTo(SOUTH_GOLD) <= calc
							.distanceTo(WEST_GOLD)) {
						walkPath(toBankSouthLRC, 5);
					} else {
						walkPath(toBankWestLRC, 5);
					}
				}

				if (shouldBank() && !nearBank()
						&& mineOres[0].toString().equals("Concentrated Coal")) {
					if (calc.distanceTo(SOUTH_COAL) <= calc
							.distanceTo(WEST_COAL)) {
						walkPath(toBankSouthLRC, 5);
					} else {
						walkPath(toBankWestLRC, 5);
					}
				}

				if (shouldBank() && nearBank()) {
					openLRCBank();
					doLRCBank();
					return 200;
				}
			} else {
				if (shouldBank()) {
					inventory.dropAllExcept(PICK_IDS);
				}
			}

			if (!shouldBank() && nearBank()
					&& mineOres[0].toString().equals("Concentrated Gold")) {
				if (!shouldBank()) {
					if (lastRock == -1) {
						while (calc.distanceTo(SOUTH_GOLD) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toSouthLRC, 5);
						}
					} else if (lastRock == 1) {
						while (calc.distanceTo(WEST_GOLD) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toWestLRC, 5);
						}
					} else if (lastRock == 2) {
						while (calc.distanceTo(SOUTH_GOLD) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toSouthLRC, 5);
						}
					}
				}
			}

			if (!shouldBank() && nearBank()
					&& mineOres[0].toString().equals("Concentrated Coal")) {
				if (!shouldBank()) {
					if (lastRock == -1) {
						while (calc.distanceTo(SOUTH_COAL) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toSouthLRC, 5);
						}
					} else if (lastRock == 1) {
						while (calc.distanceTo(WEST_COAL) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toWestLRC, 5);
						}
					} else if (lastRock == 2) {
						while (calc.distanceTo(SOUTH_COAL) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toSouthLRC, 5);
						}
					}
				}
			}

			if (!shouldBank() && calc.distanceTo(SOUTH_GOLD) < 7
					&& mineOres[0].toString().equals("Concentrated Gold")) {
				getRock();
				if (currRock != null
						&& calc.distanceTo(currRock.getLocation()) <= 7) {
					shouldWait = false;
					mineLRC();
				} else {
					lastRock = 1;
					if (!shouldWait) {
						while (calc.distanceTo(WEST_GOLD) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toWestfromSouth, 5);
							shouldWait = true;
						}
					} else {
						if (Location.equals("LRC Power-Mining")
								&& inventory.getCount(goldID) > 0) {
							inventory.dropAllExcept(PICK_IDS);
						}
						return 300;
					}
				}
			}

			if (!shouldBank() && calc.distanceTo(WEST_GOLD) < 7
					&& mineOres[0].toString().equals("Concentrated Gold")) {
				getRock();
				if (currRock != null
						&& calc.distanceTo(currRock.getLocation()) <= 7) {
					shouldWait = false;
					mineLRC();
				} else {
					lastRock = 2;
					if (!shouldWait) {
						while (calc.distanceTo(SOUTH_GOLD) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toSouthfromWest, 5);
							shouldWait = true;
						}
					} else {
						if (Location.equals("LRC Power-Mining")
								&& inventory.getCount(goldID) > 0) {
							inventory.dropAllExcept(PICK_IDS);
						}
						return 300;
					}
				}
			}

			if (!shouldBank() && calc.distanceTo(SOUTH_COAL) < 7
					&& mineOres[0].toString().equals("Concentrated Coal")) {
				getRock();
				if (currRock != null
						&& calc.distanceTo(currRock.getLocation()) <= 7) {
					shouldWait = false;
					mineLRC();
				} else {
					lastRock = 1;
					if (!shouldWait) {
						while (calc.distanceTo(WEST_COAL) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toWestfromSouth, 5);
							shouldWait = true;
						}
					} else {
						if (Location.equals("LRC Power-Mining")
								&& inventory.getCount(coalID) > 0) {
							inventory.dropAllExcept(PICK_IDS);
						}
						return 300;
					}
				}
			}

			if (!shouldBank() && calc.distanceTo(WEST_COAL) < 7
					&& mineOres[0].toString().equals("Concentrated Coal")) {
				getRock();
				if (currRock != null
						&& calc.distanceTo(currRock.getLocation()) <= 7) {
					shouldWait = false;
					mineLRC();
				} else {
					lastRock = 2;
					if (!shouldWait) {
						while (calc.distanceTo(SOUTH_COAL) > 5) {
							if (getMyPlayer().isInCombat()
									|| !game.isLoggedIn()
									|| getMyPlayer().getLocation().getY() > 6000
									|| getMyPlayer().getLocation().getY() < 5000) {
								break;
							}
							walkPath(toSouthfromWest, 5);
							shouldWait = true;
						}
					} else {
						if (Location.equals("LRC Power-Mining")
								&& inventory.getCount(coalID) > 0) {
							inventory.dropAllExcept(PICK_IDS);
						}
						return 300;
					}
				}
			}
		} else if (Location.equals("Mining Guild")) {
			if (!shouldBank()
					&& atMine.contains(players.getMyPlayer().getLocation())) {
				currRock = getRock();
				if (currRock != null) {
					mine();
				}
			}
			if (shouldBank() && calc.distanceTo(nearLadderUp) < 5) {
				doLadder(LadderUp);
				while (!getMyPlayer().isIdle()) {
					sleep(300);
				}
			}
			if (!shouldBank() && calc.distanceTo(nearLadderDown) < 7) {
				doLadder(LadderDown);
				while (!getMyPlayer().isIdle()) {
					sleep(300);
				}
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getY() < 7000) {
				walkPath(toBank, 5);
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getY() > 7000) {
				walkPath(toLadderBottom, 5);
			}
			if (shouldBank() && nearBank()) {
				if (openBank()) {
					if (doBank()) {
						// Done
					}
				}
			}
			if (!shouldBank()
					&& !atMine.contains(players.getMyPlayer().getLocation())
					&& getMyPlayer().getLocation().getY() < 7000) {
				walkPath(toLadderTop, 5);
			}
			if (!shouldBank()
					&& !atMine.contains(players.getMyPlayer().getLocation())
					&& getMyPlayer().getLocation().getY() > 7000) {
				walkPath(toMine, 5);
			}
		} else if (Location.equals("Taverly")) {
			if (!shouldBank()
					&& atMine.contains(players.getMyPlayer().getLocation())) {
				currRock = getRock();
				if (currRock != null) {
					mine();
				}
			}
			if (shouldBank() && calc.distanceTo(TAVERLY_SHORTCUT_BANK) < 5
					&& getMyPlayer().getLocation().getX() < 2936) {
				doShortcut();
				while (!getMyPlayer().isIdle()) {
					sleep(300);
				}
			}
			if (!shouldBank() && calc.distanceTo(TAVERLY_SHORTCUT_MINE) < 7
					&& getMyPlayer().getLocation().getX() >= 2936) {
				doShortcut();
				while (!getMyPlayer().isIdle()) {
					sleep(300);
				}
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getX() >= 2936) {
				walkPath(toBank, 5);
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getX() < 2936) {
				walkPath(toLadderBottom, 5);
			}
			if (shouldBank() && nearBank()) {
				if (openBank()) {
					if (doBank()) {
						// Done
					}
				}
			}
			if (!shouldBank()
					&& !atMine.contains(players.getMyPlayer().getLocation())
					&& getMyPlayer().getLocation().getX() >= 2936) {
				walkPath(toLadderTop, 5);
			}
			if (!shouldBank()
					&& !atMine.contains(players.getMyPlayer().getLocation())
					&& getMyPlayer().getLocation().getX() < 2936) {
				walkPath(toMine, 5);
			}
		} else if (Location.equals("Karamja Volcano")) {
			if (getMyPlayer().isInCombat()) {
				if (getMyPlayer().getLocation().getY() > 9000) {
					clickCave(true);
					sleep(10000);
				} else {
					walking.setRun(true);
					walkPath(toBank, 5);
				}
			}
			if (!shouldBank()
					&& atMine.contains(players.getMyPlayer().getLocation())) {
				currRock = getRock();
				if (currRock != null) {
					mine();
				}
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getY() > 9000) {
				clickCave(true);
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getY() < 9000) {
				walkPath(toBank, 5);
			}
			if (shouldBank() && nearBank()) {
				if (openBankTzhaar()) {
					if (doBank()) {
					}
				}
			}
			if (!shouldBank()
					&& !atMine.contains(players.getMyPlayer().getLocation())
					&& calc.distanceTo(TZHAAR_OUT_LOC) < 6) {
				clickCave(false);
			}
			if (!shouldBank()
					&& !atMine.contains(players.getMyPlayer().getLocation())) {
				walkPath(toMine, 5);
			}
		} else if (Location.equals("Dwarven Mine")) {
			if (!getMyPlayer().isInCombat() && !shouldBank()
					&& atMine.contains(players.getMyPlayer().getLocation())) {
				currRock = getRock();
				if (currRock != null) {
					mine();
				}
			}
			if (atFally.contains(getMyPlayer().getLocation())) {
				reEquipItems();
				while (calc.distanceTo(LRCDoorTile) >= 10) {
					if (!game.isLoggedIn()) {
						break;
					}
					walkPath(toMineFally, 5);
				}
			} else if (atLumby.contains(getMyPlayer().getLocation())) {
				reEquipItems();
				while (calc.distanceTo(LRCDoorTile) >= 10) {
					if (!game.isLoggedIn()) {
						break;
					}
					walkPath(toMineLumby, 5);
				}
			}
			if (interfaces.get(18).isValid()) {
				interfaces.get(18).getComponent(36).doClick();
			}
			if ((shouldBank() && !nearBank() && getMyPlayer().getLocation()
					.getY() > 9000) || getMyPlayer().isInCombat()) {
				doMineStairsUp();
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getY() > 9000
					&& calc.distanceTo(roomTile) <= 2) {
				doDoorBank();
			}
			if (shouldBank() && !nearBank()
					&& getMyPlayer().getLocation().getY() < 9000) {
				walkPath(toBank, 5);
			}
			if (shouldBank() && nearBank()) {
				if (openBank()) {
					if (doBank()) {
					}
				}
			}
			if (!shouldBank() && calc.distanceTo(LRCDoorTile) < 10) {
				doLRCDoorAndStairs();
				sleep(1000);
				walking.walkTileMM(midMine);
			}
			if (!shouldBank() && calc.distanceTo(LRCDoorTile) >= 10
					&& !atMine.contains(players.getMyPlayer().getLocation())
					&& getMyPlayer().getLocation().getY() < 9000) {
				walkPath(toMine, 5);
			}

		} else {
			try {
				if (!shouldBank()
						&& atMine.contains(players.getMyPlayer().getLocation())) {
					currRock = getRock();
					if (currRock != null) {
						mine();
					}
				}
				if (shouldBank() && !nearBank()) {
					walkPath(toBank, 5);
				}
				if (!Location.equals("TzHaar")) {
					if (shouldBank() && nearBank()) {
						if (openBank()) {
							if (doBank()) {
							}
						}
					}
				} else {
					if (shouldBank() && nearBank()) {
						if (openBankTzhaar()) {
							if (doBank()) {
							}
						}
					}
				}
				if (!shouldBank()
						&& !atMine
								.contains(players.getMyPlayer().getLocation())) {
					walkPath(toMine, 5);
				}
			} catch (Exception e) {
				log(e);
			}
		}
		return 250;
	}

	public RSTile[] reversePath(final RSTile[] other) {
		final RSTile[] t = new RSTile[other.length];
		for (int i = 0; i < t.length; i++) {
			t[i] = other[other.length - i - 1];
		}
		return t;
	}

	public void reEquipItems() {
		RSItem[] inv = inventory.getItems();
		if (inventory.getCount() > 0) {
			for (int i = 0; i < 3; i++) {
				if (inv[i].getID() != coalID && inv[i].getID() != goldID)
					if (!inv[i].interact("Wield"))
						if (!inv[i].interact("Wear"))
							inv[i].interact("Equip");
			}
		}
	}

	public void clickCave(Boolean in) {
		RSObject cave = null;
		if (in) {
			cave = objects.getNearest(TZHAAR_IN);
		} else {
			cave = objects.getNearest(TZHAAR_OUT);
		}
		if (cave != null) {
			if (cave.isOnScreen()) {
				cave.getModel().doClick(true);
			} else {
				walking.walkTileMM(cave.getLocation());
			}
		}
		sleep(500);
		while (getMyPlayer().isMoving()) {
			sleep(100);
		}
	}

	public boolean openLRCBank() {
		RSObject bankBox = objects.getNearest(LRC_BANK);
		if (bankBox != null) {
			try {
				if (bankBox != null && bankBox.isOnScreen()) {
					clickObject(bankBox, "Deposit");
					sleep(500);
					while (players.getMyPlayer().isMoving()) {
						sleep(100);
					}
					sleep(1000);
				} else {
					walking.walkTileMM(bankBox.getLocation());
					return false;
				}
			} catch (Exception e) {

			}
			return bank.isDepositOpen();
		} else {
			return false;
		}
	}

	public boolean doLRCBank() {
		try {
			if (bank.isDepositOpen()) {
				bank.depositAllExcept(PICK_IDS);
				bank.close();
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	public void doLRCDoorAndStairs() {
		LRCDoor = null;
		LRCDoor = objects.getNearest(LRC_DOOR);
		if (LRCDoor != null) {
			LRCDoor.getModel().interact("Open");
			sleep(500);
			while (getMyPlayer().isMoving()) {
				sleep(100);
			}
		}
		LRCDoor = objects.getNearest(LRC_DOOR);
		if (LRCDoor == null || calc.distanceTo(LRCDoor.getLocation()) > 10) {
			LRCStairs = objects.getNearest(LRC_STAIRS);
			if (LRCStairs != null && LRCStairs.isOnScreen()) {
				LRCStairs.getModel().interact("Climb-down");
				sleep(500);
			} else if (LRCStairs != null) {
				walking.walkTileMM(LRCStairs.getLocation());
			}
		}
		sleep(500);
	}

	public void doDoorBank() {
		LRCDoor = null;
		LRCDoor = objects.getNearest(LRC_DOOR);
		if (LRCDoor != null) {
			camera.turnTo(LRCDoor);
			LRCDoor.getModel().interact("Open");
			sleep(500);
			while (getMyPlayer().isMoving()) {
				sleep(100);
			}
		}
	}

	public void doMineStairsUp() {
		LRCStairs = objects.getNearest(LRC_STAIRSUP);
		if (LRCStairs != null && LRCStairs.isOnScreen()) {
			LRCStairs.getModel().interact("Climb-up");
			sleep(1000);
		} else if (LRCStairs != null && calc.tileOnMap(LRCStairs.getLocation())) {
			walking.walkTileMM(LRCStairs.getLocation());
		} else if (LRCStairs != null) {
			walking.walkTileMM(midMine);
		}
		while (getMyPlayer().isMoving()) {
			sleep(100);
		}
		sleep(500);
	}

	public void doLRCRope() {
		LRCRope = null;
		LRCRope = objects.getNearest(LRC_ROPE);
		if (LRCRope != null) {
			if (LRCRope.isOnScreen()) {
				try {
					LRCRope.getModel().interact("Climb");
					sleep(500);
					while (getMyPlayer().isMoving()) {
						sleep(100);
					}
				} catch (Exception e) {

				}
			} else {
				walking.walkTileMM(LRCRope.getLocation());
				sleep(500);
				while (getMyPlayer().isMoving()) {
					sleep(100);
				}
			}
		}
	}

	public void doLadder(int ladderID) {
		RSObject Ladder1 = objects.getNearest(ladderID);
		if (Ladder1 != null) {
			Ladder1.getModel().interact("Climb");
			sleep(500);
			while (getMyPlayer().isMoving()) {
				sleep(100);
			}
		}
	}

	public void doShortcut() {
		RSObject Shortcut = objects.getNearest(TAVERLY_SHORTCUT);
		if (Shortcut != null) {
			Shortcut.getModel().doClick(true);
			sleep(500);
			while (getMyPlayer().isMoving()) {
				sleep(100);
			}
		}
	}

	@Override
	public boolean onStart() {
		while (!game.isLoggedIn()) {
			sleep(1000, 2000);
		}
		if (!game.isFixed()) {
			log.warning("Your screen size is not Fixed!");
			stop();
		}
		startlevel = skills.getCurrentLevel(Skills.MINING);
		startexp = skills.getCurrentExp(Skills.MINING);
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					gui = new TestGUI();
					gui.setVisible(true);

				}
			});
		} catch (InterruptedException e) {
		} catch (InvocationTargetException e) {
		}
		while (gui.isVisible()) {
			sleep(50);
		}
		mouse.setSpeed(4);
		camera.setAngle(random(1, 360));
		Ban.start();
		return true;
	}

	@Override
	public void onFinish() {
		banRun = false;
		showPaint = true;
		env.saveScreenshot(true);
		log("Thanks for using Pwn Miner.");
	}

	private final RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}

	private class MousePathPoint extends Point {
		private static final long serialVersionUID = 1L;
		private long finishTime;
		private double lastingTime;

		private int toColor(double d) {
			return Math.min(255, Math.max(0, (int) d));
		}

		public MousePathPoint(int x, int y, int lastingTime) {
			super(x, y);
			this.lastingTime = lastingTime;
			finishTime = System.currentTimeMillis() + lastingTime;
		}

		public boolean isUp() {
			return System.currentTimeMillis() > finishTime;
		}

		public Color getColor() {
			return new Color(
					0,
					191,
					255,
					toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime)));

		}
	}

	public double getPercentToNextLevel(final int index) {
		final int lvl = skills.getRealLevel(index);
		if (lvl == 99) {
			return 0;
		}
		final double xpTotal = Skills.XP_TABLE[lvl + 1] - Skills.XP_TABLE[lvl];
		if (xpTotal == 0) {
			return 0;
		}
		final double xpDone = skills.getCurrentExp(index)
				- Skills.XP_TABLE[lvl];
		return 100 * xpDone / xpTotal;
	}

	private final Color color1 = new Color(255, 255, 255);

	private final Color color10 = new Color(204, 204, 204);
	private final Color color20 = new Color(0, 0, 0);
	private final Color color30 = new Color(51, 153, 255, 80);

	private final Color mouse1 = new Color(65, 105, 225);

	private final Font font1 = new Font("Arial", 0, 10);
	private final Font font2 = new Font("Arial", 0, 9);
	private final Font font3 = new Font("Arial", 1, 15);
	private final Font font5 = new Font("Arial", 0, 12);

	BasicStroke stroke1 = new BasicStroke(1);

	private final Image img1 = getImage("http://oi42.tinypic.com/2cqfrsj.jpg");
	private final Image img2 = getImage("http://i42.tinypic.com/2njk3yx.jpg");

	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHints(antialiasing);

		java.text.DecimalFormat df = new java.text.DecimalFormat("##.##");
		currLevel = skills.getCurrentLevel(Skills.MINING);
		lvlGain = currLevel - startlevel;
		xpGained = skills.getCurrentExp(Skills.MINING) - startexp;
		millis = System.currentTimeMillis() - startTime;
		num = ((3600000.0 / (double) millis));
		xpPerHour = (int) (num * xpGained);
		orePerHour = (int) (num * mined);
		hours = millis / (1000 * 60 * 60);
		millis -= hours * (1000 * 60 * 60);
		minutes = millis / (1000 * 60);
		millis -= minutes * (1000 * 60);
		seconds = millis / 1000;
		xpToLevel = skills.getExpToNextLevel(Skills.MINING);
		percent = getPercentToNextLevel(Skills.MINING);

		g.drawImage(img2, 421, 347, null);


		if (showPaint) {

			g.setColor(color10);
			g.fillRoundRect(3, 320, 513, 16, 7, 5);
			g.setColor(color20);
			g.drawRoundRect(3, 320, 513, 16, 7, 5);

			g.setFont(font5);
			g.drawString(df.format(percent) + "%", 247, 334);
			g.setColor(color30);
			g.fillRoundRect(3, 320, (int) (511 * (percent / 100)), 15, 7, 5);
			g.setColor(color20);
			g.drawRoundRect(3, 320, (int) (511 * (percent / 100)), 15, 7, 5);

			g.drawImage(img1, 4, 343, null);
			g.setFont(font1);
			g.setColor(color1);
			g.drawString("" + currLevel + " (+" + lvlGain + ")", 133, 353);
			g.drawString("" + xpGained, 133, 369);
			g.drawString("" + xpPerHour, 133, 387);
			g.drawString("" + xpToLevel, 133, 404);
			g.drawString("" + hours + ":" + minutes + ":" + seconds, 100, 437);
			g.setFont(font2);
			g.drawString("" + Location, 100, 455);
			g.setFont(font3);
			g.drawString("" + mined, 270, 386);
			g.drawString("" + orePerHour, 272, 423);
			g.drawString("" + gemFound, 275, 461);
	        g.setFont(font2);
			try {
				if (xpPerHour > 0) {
					int sTNL = (xpToLevel) / (xpPerHour / 3600);
					int hTNL = sTNL / (60 * 60);
					sTNL -= hTNL * (60 * 60);
					int mTNL = sTNL / 60;
					sTNL -= mTNL * 60;
					int msTNL = sTNL / 60;
					sTNL -= msTNL * 60;
					g.drawString("" + (int) hTNL + ":" + (int) mTNL + ":"
							+ (int) sTNL, 381, 419);
				} else {
					g.drawString("--:--:--", 381, 419);
				}
			} catch (Exception e) {
				g.drawString("", 381, 419);
			}
		}

		while (!mousePath.isEmpty() && mousePath.peek().isUp())
			mousePath.remove();
		Point clientCursor = mouse.getLocation();
		MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y,
				1200);
		if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp))
			mousePath.add(mpp);
		MousePathPoint lastPoint = null;
		for (MousePathPoint a : mousePath) {
			if (lastPoint != null) {
				g.setColor(a.getColor());
				g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);

			}
			lastPoint = a;
		}

		g.setColor(mouse1);
		g.fillOval(mouse.getLocation().x - 2, mouse.getLocation().y - 2, 5, 5);

	}

	public void mouseClicked(MouseEvent e) {
		p = e.getPoint();
		if (close.contains(p)) {
			showPaint = !showPaint;
		}
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
	}

	public boolean shouldBank() {
		if (inventory.isFull()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean nearBank() {
		if (calc.distanceTo(atBank) <= 10) {
			return true;
		}
		return false;
	}

	public boolean openBank() {
		try {
			banker = objects.getNearest(BANK_BOOTHS);
			if (banker == null) {
				walkPath(toBank, 5);
				return false;
			}
			if (!banker.isOnScreen()) {
				walking.walkTileMM(banker.getLocation(), 2, 2);
			}
			trys2 = 0;
			while (!banker.isOnScreen()) {
				trys2++;
				if (trys2 > 10) {
					walking.walkTileMM(atBank);
					banker = objects.getNearest(BANK_BOOTHS);
					trys2 = 0;
				}
				if (banker != null) {
					if (banker.isOnScreen()) {
						break;
					}
				}
				sleep(100);
			}
			trys++;
			if (trys > 10) {
				walking.walkTileMM(atBank);
				trys = 0;
			}
			banker.interact("Bank Bank");
			sleep(500);
			while (players.getMyPlayer().isMoving()) {
				sleep(100);
			}
			sleep(300);
			return bank.isOpen();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean openBankTzhaar() {
		try {
			RSNPC banker1 = npcs.getNearest(TZHAAR_BANK);
			if (banker1 == null) {
				walkPath(toBank, 5);
				return false;
			}
			if (!banker1.isOnScreen()) {
				walking.walkTileMM(banker1.getLocation(), 2, 2);
			}
			trys2 = 0;
			while (!banker1.isOnScreen()) {
				trys2++;
				if (trys2 > 10) {
					walking.walkTileMM(atBank);
					banker1 = npcs.getNearest(TZHAAR_BANK);
					trys2 = 0;
				}
				if (banker1 != null) {
					if (banker1.isOnScreen()) {
						break;
					}
				}
				sleep(100);
			}
			trys++;
			if (trys > 10) {
				walking.walkTileMM(atBank);
				trys = 0;
			}
			banker1.interact("Bank");
			sleep(500);
			while (players.getMyPlayer().isMoving()) {
				sleep(100);
			}
			sleep(300);
			return bank.isOpen();
		} catch (Exception e) {
			return false;
		}
	}

	public boolean doBank() {
		if (bank.isOpen()) {
			if (bank.depositAllExcept(PICK_IDS)) {
				return true;
			}
		}
		return false;
	}

	public boolean clickObject(final RSObject c, final String action) {
		try {
			Point screenLoc;
			int X = (int) calc.tileToScreen(c.getLocation()).getX();
			int Y = (int) calc.tileToScreen(c.getLocation()).getY();
			screenLoc = new Point(X, Y);
			if ((c == null) || !calc.pointOnScreen(screenLoc)) {
				log("Not on screen " + action);
				return false;
			}
			mouse.move(screenLoc);
			X = (int) calc.tileToScreen(c.getLocation()).getX();
			Y = (int) calc.tileToScreen(c.getLocation()).getY();
			screenLoc = new Point(X, Y);
			if (!mouse.getLocation().equals(screenLoc)) {
				return false;
			}
			final String[] items = menu.getItems();
			if (items.length <= 1) {
				return false;
			}
			if (items[0].toLowerCase().contains(action.toLowerCase())) {
				mouse.click(screenLoc, true);
				return true;
			} else {
				mouse.click(screenLoc, false);
				return menu.doAction(action);
			}
		} catch (final NullPointerException ignored) {
		}
		return true;
	}

	public boolean mineLRC() {
		while (isMining()) {
			sleep(100);
			if (!checkRock(currRock)) {
				return false;
			}
			if (inventory.isFull()) {
				return false;
			}
		}
		while (players.getMyPlayer().isMoving()) {
			sleep(100);
		}
		sleep(750);
		try {
			if (currRock != null) {
			} else {
				return false;
			}
			if (currRock.isOnScreen() && !isMining() && checkRock(currRock)) {
				mouse.move(currRock.getModel().getPoint());
				mouse.click(false);
				sleep(400, 500);
				if (menu.isOpen())
					if (menu.contains("Mine"))
						menu.doAction("Mine");
					else if (menu.contains("Mineral")) {
						menu.doAction("Mineral");
					}
			}
		} catch (Exception e) {
		}
		if (currRock == null) {
			return false;
		}
		sleep(700);
		while (players.getMyPlayer().isMoving()) {
			sleep(100);
		}
		sleep(600);
		while (isMining()) {
			sleep(100);
			if (!checkRock(currRock)) {
				return false;
			}
			if (inventory.isFull()) {
				return false;
			}
			sleep(300);
		}
		return true;
	}

	public boolean powerMine() {
		while (isMining()) {
			sleep(100);
			if (!checkRock(currRock)) {
				return false;
			}
		}
		if (menu.isOpen() && menu.contains("Drop")) {
			menu.doAction("Drop");
		}
		while (players.getMyPlayer().isMoving()) {
			sleep(100);
		}
		try {
			if (currRock != null) {
				while (calc.distanceBetween(currRock.getLocation(),
						getMyPlayer().getLocation()) > checkDistance) {
					getRock();
				}
			} else {
				return false;
			}
		} catch (Exception e) {
		}
		if (currRock.isOnScreen() && !isMining() && checkRock(currRock)) {
			currRock.getModel().interact("Mine");
		}
		if (currRock == null) {
			return false;
		}
		sleep(700);
		if (M1D1) {
			if (inventory.getCount(ORE_IDS) > 0) {
				inventory.getItem(ORE_IDS).doClick(false);
				if (menu.contains("Drop") && menu.isOpen()) {
					mouse.move(new Point(menu.getLocation().x + random(15, 50),
							menu.getLocation().y + random(38, 52)));
				}
			}
		}
		while (players.getMyPlayer().isMoving()) {
			sleep(100);
		}
		sleep(600);
		while (isMining()) {
			sleep(100);
			if (!checkRock(currRock)) {
				getRock();
				if (currRock != null) {
					clickObject(currRock, "Mine");
				}
			}
			sleep(100);
		}
		if (menu.isOpen() && menu.contains("Drop")) {
			menu.doAction("Drop");
		}
		return true;
	}

	public boolean mine() {
		while (isMining()) {
			sleep(100);
			if (nextRock != null) {
				hoverRock(nextRock);
			}
			if (!checkRock(currRock)) {
				if (nextRock != null) {
					currRock = nextRock;
					nextRock = null;
					if (calc.distanceTo(currRock.getLocation()) < checkDistance) {
						if (calc.distanceBetween(mouse.getLocation(),
								calc.tileToScreen(currRock.getLocation())) > 10) {
							mouse.move(
									calc.tileToScreen(currRock.getLocation()),
									2, 2);
						}
						if (menu.getActions()[0].toString().contains("Mine")
								&& !shouldBank()) {
							mouse.click(true);
							sleep(700);
						} else if (currRock.isOnScreen()) {
							clickObject(currRock, "Mine");
							sleep(3000);
						} else if (calc.tileOnMap(currRock.getLocation())) {
							walking.walkTileMM(currRock.getLocation());
						}
					}
				} else {
					currRock = getRock();
					if (currRock != null
							&& calc.distanceTo(currRock.getLocation()) < checkDistance) {
						if (calc.distanceBetween(mouse.getLocation(),
								calc.tileToScreen(currRock.getLocation())) > 10) {
							mouse.move(
									calc.tileToScreen(currRock.getLocation()),
									2, 2);
						}
						if (menu.getActions()[0].toString().contains("Mine")
								&& !shouldBank()) {
							mouse.click(true);
							sleep(700);
						} else if (currRock.isOnScreen()) {
							clickObject(currRock, "Mine");
							sleep(1200);
						} else if (calc.tileOnMap(currRock.getLocation())) {
							walking.walkTileMM(currRock.getLocation());
						}
					}
				}
			}
		}
		sleep(500);
		if (currRock != null) {
			while (players.getMyPlayer().isMoving() && !currRock.isOnScreen()) {
				sleep(100);
			}
		}
		try {
			if (currRock.isOnScreen() && !isMining() && checkRock(currRock)
					&& calc.distanceTo(currRock.getLocation()) < checkDistance) {
				if (calc.distanceBetween(mouse.getLocation(),
						calc.tileToScreen(currRock.getLocation())) > 10) {
					mouse.move(calc.tileToScreen(currRock.getLocation()), 2, 2);
				}
				if (menu.getActions()[0].toString().contains("Mine")
						&& !shouldBank()) {
					mouse.click(true);
					sleep(700);
				} else if (currRock.isOnScreen()) {
					clickObject(currRock, "Mine");
					sleep(700);
				} else if (calc.tileOnMap(currRock.getLocation())) {
					walking.walkTileMM(currRock.getLocation());
				}
				nextRock = null;
			} else if (!currRock.isOnScreen() && !isMining()
					&& calc.distanceTo(currRock.getLocation()) < checkDistance) {
				if (calc.tileOnMap(currRock.getLocation())) {
					walking.walkTileMM(currRock.getLocation(), 2, 2);
				} else {
					x = getMyPlayer().getLocation().getX()
							+ ((currRock.getLocation().getX() - getMyPlayer()
									.getLocation().getX()) / 2);
					y = getMyPlayer().getLocation().getY()
							+ ((currRock.getLocation().getY() - getMyPlayer()
									.getLocation().getY()) / 2);
					walking.walkTileMM(new RSTile(x, y));
				}
			}
		} catch (Exception e) {
		}
		if (currRock == null) {
			return false;
		}
		sleep(800);
		while (players.getMyPlayer().isMoving() && !currRock.isOnScreen()) {
			sleep(100);
		}
		sleep(800);
		if (nextRock == null) {
			getSecondNearest(currRock);
		}

		while (isMining()) {
			sleep(100);
			if (nextRock != null) {
				hoverRock(nextRock);
			}
			if (!checkRock(currRock)) {
				if (nextRock != null) {
					currRock = nextRock;
					nextRock = null;
					if (calc.distanceTo(currRock.getLocation()) < checkDistance) {
						if (calc.distanceBetween(mouse.getLocation(),
								calc.tileToScreen(currRock.getLocation())) > 10) {
							mouse.move(
									calc.tileToScreen(currRock.getLocation()),
									2, 2);
						}
						if (menu.getActions()[0].toString().contains("Mine")
								&& !shouldBank()) {
							mouse.click(true);
							sleep(700);
						} else if (currRock.isOnScreen()) {
							clickObject(currRock, "Mine");
							sleep(700);
						} else if (calc.tileOnMap(currRock.getLocation())) {
							walking.walkTileMM(currRock.getLocation());
						}
					}
				} else {
					getRock();
					try {
						if (currRock != null
								&& calc.distanceTo(currRock.getLocation()) < checkDistance) {
							if (calc.distanceBetween(mouse.getLocation(),
									calc.tileToScreen(currRock.getLocation())) > 10) {
								mouse.move(calc.tileToScreen(currRock
										.getLocation()), 2, 2);
							}
							if (menu.getActions()[0].toString()
									.contains("Mine") && !shouldBank()) {
								mouse.click(true);
								sleep(700);
							} else if (currRock.isOnScreen()) {
								clickObject(currRock, "Mine");
								sleep(700);
							} else if (calc.tileOnMap(currRock.getLocation())) {
								walking.walkTileMM(currRock.getLocation());
							}
						}
					} catch (Exception e) {
					}
				}
			}
		}

		return true;
	}

	public boolean isMining() {
		for (int i = 0; i < ANIMATIONS.length; i++) {
			if (players.getMyPlayer().getAnimation() == ANIMATIONS[i]) {
				return true;
			}
		}
		return false;
	}

	public void hoverRock(RSObject Rock) {
		int i = 0;
		while (!menu.getItems()[0].toString().contains("Mine")
				&& calc.distanceBetween(calc.tileToScreen(Rock.getLocation()),
						mouse.getLocation()) > 10) {
			mouse.move(calc.tileToScreen(Rock.getLocation()));
			i++;
			if (i > 20) {
				return;
			}
		}
	}

	public boolean checkRock(RSObject rock) {
		if (rock != null) {
			try {
				RSObject current = objects.getTopAt(currRock.getLocation());
				if (current.getID() == currID) {
					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
			}
		}
		return false;
	}

	public RSObject getRock() {
		int[] ids = {};
		String[] sIDS;
		for (int i = mineOres.length - 1; i >= 0; i--) {
			if (mineOres[i].equals("Concentrated Gold")) {
				sIDS = oreID[CONC_GOLD].split(", ");
				try {
					ids = toIntArray(sIDS);
				} catch (Exception ex) {
					log(ex);
					return null;
				}
			} else if (mineOres[i].equals("Concentrated Coal")) {
				sIDS = oreID[CONC_COAL].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Runite")) {
				sIDS = oreID[RUNITE].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Adamantite")) {
				sIDS = oreID[ADAMANTITE].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Mithril")) {
				sIDS = oreID[MITHRIL].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Granite")) {
				sIDS = oreID[GRANITE].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Gold")) {
				sIDS = oreID[GOLD].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Coal")) {
				sIDS = oreID[COAL].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					log(ex);
					log(ex.getMessage());
					return null;
				}
			} else if (mineOres[i].equals("Silver")) {
				sIDS = oreID[SILVER].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Iron")) {
				sIDS = oreID[IRON].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Tin")) {
				sIDS = oreID[TIN].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Copper")) {
				sIDS = oreID[COPPER].split(", ");
				try {
					ids = toIntArray(sIDS);
				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Clay")) {
				sIDS = oreID[CLAY].split(", ");
				try {
					ids = toIntArray(sIDS);
				} catch (Exception ex) {
					return null;
				}
			}

			currRock = objects.getNearest(ids);
			if (currRock != null) {
				if (calc.distanceBetween(currRock.getLocation(), getMyPlayer()
						.getLocation()) > checkDistance) {
					currRock = null;
				}
				if (currRock != null && Location.equals("Dwarven Mine")) {
					if (currRock.getLocation().getY() < 9758) {
						currRock = null;
					}
				}
				if (currRock != null && Location.equals("Mining Guild")) {
					if (currRock.getLocation().getY() > 9751) {
						currRock = null;
					}
				}
				if ((currRock != null && !Location.equals("Power-Mining")
						&& !Location.equals("Living Rock Cavern") && !Location
							.equals("LRC Power-Mining"))) {
					try {
						if (!atMine.contains(currRock.getLocation())) {
							currRock = null;
						}
					} catch (Exception e) {
						log(Arrays.toString(e.getStackTrace()));
						currRock = null;
					}
				}
			}
			if (currRock != null) {
				currID = currRock.getID();
				return currRock;
			}
		}
		return null;
	}

	public RSObject getSecondNearest(final RSObject curRock) {
		int[] ids = {};
		String[] sIDS;
		for (int i = mineOres.length - 1; i >= 0; i--) {
			if (mineOres[i].equals("Runite")) {
				sIDS = oreID[RUNITE].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Adamantite")) {
				sIDS = oreID[ADAMANTITE].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Mithril")) {
				sIDS = oreID[MITHRIL].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Granite")) {
				sIDS = oreID[GRANITE].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Gold")) {
				sIDS = oreID[GOLD].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Coal")) {
				sIDS = oreID[COAL].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Silver")) {
				sIDS = oreID[SILVER].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Iron")) {
				sIDS = oreID[IRON].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Tin")) {
				sIDS = oreID[TIN].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Copper")) {
				sIDS = oreID[COPPER].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			} else if (mineOres[i].equals("Clay")) {
				sIDS = oreID[CLAY].split(", ");
				try {
					ids = toIntArray(sIDS);

				} catch (Exception ex) {
					return null;
				}
			}

			nextRock = checkSecondNearest(curRock, ids);

			if (nextRock != null) {
				return nextRock;
			}
		}
		return null;
	}

	public RSObject checkSecondNearest(final RSObject curRock, final int[] ids) {
		return objects.getNearest(new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				if (curRock.equals(o)) {
					return false;
				}
				if (calc.distanceBetween(o.getLocation(), getMyPlayer()
						.getLocation()) > checkDistance) {
					return false;
				}
				for (int id : ids) {
					if (o.getID() == id) {
						return true;
					}
				}
				return false;
			}
		});
	}

	public int[] toIntArray(String[] sarray) throws Exception {
		if (sarray != null) {
			int intarray[] = new int[sarray.length];
			for (int i = 0; i < sarray.length; i++) {
				intarray[i] = Integer.parseInt(sarray[i]);
			}
			return intarray;
		}
		return null;
	}

	public class antiBan implements Runnable {
		private int randomProd;
		private int randomMore;
		private int randomNum;

		public void run() {
			while (banRun) {
				antiBan3();
				sleep(1500, 2000);
			}
		}

		public void antiBan3() {
			randomProd = random(1, 600);
			if (randomProd == 1) {
				randomMore = random(1, 5);
				if (randomMore == 1) {
					game.openTab(Tab.INVENTORY);
					sleep(150, 300);
					mouse.move(random(678, 728), random(213, 232));
					sleep(1000, 1100);
				} else {
					mouse.move(random(678, 728), random(213, 232));
					sleep(1000, 1100);
				}
			}
			randomNum = random(1, 40);
			if (randomNum == 15) {
				if (currRock != null) {
					camera.turnTo(currRock);
					return;
				}
				camera.setAngle(random(1, 360));
			}

			if (randomNum == 6) {
				camera.setPitch(random(50, 68));
			}

			if (randomNum == 8) {
				camera.setPitch(random(68, 90));
			} else {
			}
		}
	}

	public class TestGUI extends javax.swing.JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		/** Creates new form TestGUI */
		public TestGUI() {
			try {
				UIManager.setLookAndFeel(UIManager
						.getSystemLookAndFeelClassName());
			} catch (Exception e) {

			}
			initComponents();
			lstOres.setModel(new DefaultListModel());
		}

		/**
		 * This method is called from within the constructor to initialize the
		 * form. WARNING: Do NOT modify this code. The content of this method is
		 * always regenerated by the Form Editor.
		 */
		// <editor-fold defaultstate="collapsed" desc="Generated Code">
		private void initComponents() {

			jPanel1 = new javax.swing.JPanel();
			jLabel1 = new javax.swing.JLabel();
			jSeparator1 = new javax.swing.JSeparator();
			cmdStart = new javax.swing.JButton();
			jLabel2 = new javax.swing.JLabel();
			jLabel3 = new javax.swing.JLabel();
			jLabel4 = new javax.swing.JLabel();
			jLabel5 = new javax.swing.JLabel();
			jLabel6 = new javax.swing.JLabel();
			jPanel2 = new javax.swing.JPanel();
			jScrollPane1 = new javax.swing.JScrollPane();
			lstPlaces = new javax.swing.JList();
			jLabel7 = new javax.swing.JLabel();
			jLabel8 = new javax.swing.JLabel();
			jScrollPane2 = new javax.swing.JScrollPane();
			lstOres = new javax.swing.JList();
			txtDistance = new javax.swing.JTextField();
			lblDistance = new javax.swing.JLabel();
			chkM1D1 = new javax.swing.JCheckBox();
			setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
			setTitle("Pwn Miner");
			setResizable(false);

			cmdStart.setText("Start");
			cmdStart.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent evt) {
					cmdStartActionPerformed(evt);
					gui.dispose();
				}
			});

			jLabel2.setText("Instructions:");

			jLabel3.setText("Fill out the wanted settings. Be sure to");
			jLabel3.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

			jLabel4.setText(" check the tabs in case you miss something.");
			jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.TOP);

			jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11));
			jLabel5.setText("Created By:");

			jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11));
			jLabel6.setText("wyn10");

			txtDistance.setText("15");

			jPanel2.setBackground(new java.awt.Color(255, 255, 255));
			jPanel2.setBorder(javax.swing.BorderFactory
					.createLineBorder(new java.awt.Color(0, 0, 0)));

			lstPlaces.setModel(new javax.swing.AbstractListModel() {

				private static final long serialVersionUID = 1L;
				String[] strings = { "Power-Mining", "Al Kharid",
						"Ardougne East", "Ardougne South", "Barbarian Village",
						"Dwarven Mine", "Karamja Volcano",
						"Khazard Battlefield", "Living Rock Cavern",
						"LRC Power-Mining", "Lumbridge Swamp", "Mining Guild",
						"North Draynor Village", "Rimmington", "Taverly",
						"TzHaar", "Varrock East", "Varrock West", "Yanille" };

				public int getSize() {
					return strings.length;
				}

				public Object getElementAt(int i) {
					return strings[i];
				}
			});
			lstPlaces
					.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
			lstPlaces
					.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
						public void valueChanged(
								javax.swing.event.ListSelectionEvent evt) {
							lstPlacesValueChanged(evt);
						}
					});
			jScrollPane1.setViewportView(lstPlaces);

			jLabel7.setText("Locations:");

			jLabel8.setText("Ores:");

			lblDistance.setFont(new java.awt.Font("Tahoma", 0, 11));
			lblDistance.setText("Check Distance:");

			chkM1D1.setText("Use M1D1 while powermining");
			chkM1D1.setBackground(Color.WHITE);

			lstOres.setModel(new javax.swing.AbstractListModel() {
				private static final long serialVersionUID = 1L;
				String[] strings = { "Clay", "Copper", "Tin", "Iron", "Silver",
						"Coal", "Sandstone", "Gold", "Granite", "Mithril",
						"Adamant", "Runite", " " };

				public int getSize() {
					return strings.length;
				}

				public Object getElementAt(int i) {
					return strings[i];
				}
			});
			jScrollPane2.setViewportView(lstOres);

			javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
					jPanel2);
			jPanel2.setLayout(jPanel2Layout);
			jPanel2Layout
					.setHorizontalGroup(jPanel2Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									jPanel2Layout
											.createSequentialGroup()
											.addContainerGap()
											.addGroup(
													jPanel2Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(
																	jScrollPane1,
																	javax.swing.GroupLayout.PREFERRED_SIZE,
																	217,
																	javax.swing.GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	jLabel7)
															.addComponent(
																	lblDistance)
															.addComponent(
																	txtDistance))
											.addGap(22, 22, 22)
											.addGroup(
													jPanel2Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(
																	jScrollPane2,
																	javax.swing.GroupLayout.PREFERRED_SIZE,
																	217,
																	javax.swing.GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	jLabel8)
															.addComponent(
																	chkM1D1))
											.addContainerGap()));
			jPanel2Layout
					.setVerticalGroup(jPanel2Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									jPanel2Layout
											.createSequentialGroup()
											.addContainerGap()
											.addGroup(
													jPanel2Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.BASELINE)
															.addComponent(
																	jLabel7)
															.addComponent(
																	jLabel8))
											.addPreferredGap(
													javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(
													jPanel2Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(
																	jScrollPane1,
																	javax.swing.GroupLayout.PREFERRED_SIZE,
																	174,
																	javax.swing.GroupLayout.PREFERRED_SIZE)
															.addComponent(
																	jScrollPane2,
																	javax.swing.GroupLayout.PREFERRED_SIZE,
																	174,
																	javax.swing.GroupLayout.PREFERRED_SIZE))
											.addContainerGap(
													javax.swing.GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)
											.addGap(0, 10, Short.MAX_VALUE)
											.addGroup(
													jPanel2Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(
																	lblDistance)
															.addComponent(
																	chkM1D1))
											.addComponent(txtDistance)
											.addContainerGap()));

			javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
					jPanel1);
			jPanel1.setLayout(jPanel1Layout);
			jPanel1Layout
					.setHorizontalGroup(jPanel1Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									jPanel1Layout
											.createSequentialGroup()
											.addComponent(jLabel1)
											.addPreferredGap(
													javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(
													jPanel1Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.LEADING)
															.addComponent(
																	cmdStart,
																	javax.swing.GroupLayout.Alignment.TRAILING,
																	javax.swing.GroupLayout.PREFERRED_SIZE,
																	88,
																	javax.swing.GroupLayout.PREFERRED_SIZE)
															.addGroup(
																	jPanel1Layout
																			.createSequentialGroup()
																			.addGroup(
																					jPanel1Layout
																							.createParallelGroup(
																									javax.swing.GroupLayout.Alignment.TRAILING)
																							.addComponent(
																									jLabel5)
																							.addComponent(
																									jLabel2))
																			.addPreferredGap(
																					javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																			.addGroup(
																					jPanel1Layout
																							.createParallelGroup(
																									javax.swing.GroupLayout.Alignment.LEADING)
																							.addComponent(
																									jLabel6)
																							.addComponent(
																									jLabel4)
																							.addComponent(
																									jLabel3))))
											.addContainerGap())
							.addComponent(jSeparator1,
									javax.swing.GroupLayout.Alignment.TRAILING,
									javax.swing.GroupLayout.DEFAULT_SIZE, 498,
									Short.MAX_VALUE)
							.addGroup(
									jPanel1Layout
											.createSequentialGroup()
											.addContainerGap()
											.addComponent(
													jPanel2,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													Short.MAX_VALUE)
											.addContainerGap()));
			jPanel1Layout
					.setVerticalGroup(jPanel1Layout
							.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
							.addGroup(
									jPanel1Layout
											.createSequentialGroup()
											.addGroup(
													jPanel1Layout
															.createParallelGroup(
																	javax.swing.GroupLayout.Alignment.TRAILING)
															.addComponent(
																	jLabel1)
															.addGroup(
																	jPanel1Layout
																			.createSequentialGroup()
																			.addContainerGap()
																			.addGroup(
																					jPanel1Layout
																							.createParallelGroup(
																									javax.swing.GroupLayout.Alignment.LEADING)
																							.addGroup(
																									jPanel1Layout
																											.createSequentialGroup()
																											.addComponent(
																													jLabel3)
																											.addGap(1,
																													1,
																													1)
																											.addComponent(
																													jLabel4))
																							.addComponent(
																									jLabel2))
																			.addPreferredGap(
																					javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																			.addGroup(
																					jPanel1Layout
																							.createParallelGroup(
																									javax.swing.GroupLayout.Alignment.BASELINE)
																							.addComponent(
																									jLabel5)
																							.addComponent(
																									jLabel6))
																			.addPreferredGap(
																					javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																					35,
																					Short.MAX_VALUE)
																			.addComponent(
																					cmdStart,
																					javax.swing.GroupLayout.PREFERRED_SIZE,
																					35,
																					javax.swing.GroupLayout.PREFERRED_SIZE)))
											.addPreferredGap(
													javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
											.addComponent(
													jSeparator1,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													5,
													javax.swing.GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(
													javax.swing.LayoutStyle.ComponentPlacement.RELATED)
											.addComponent(
													jPanel2,
													javax.swing.GroupLayout.PREFERRED_SIZE,
													javax.swing.GroupLayout.DEFAULT_SIZE,
													javax.swing.GroupLayout.PREFERRED_SIZE)
											.addContainerGap()));

			javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
					getContentPane());
			getContentPane().setLayout(layout);
			layout.setHorizontalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGap(0, 498, Short.MAX_VALUE)
					.addGroup(
							layout.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(
											layout.createSequentialGroup()
													.addGap(0, 0,
															Short.MAX_VALUE)
													.addComponent(
															jPanel1,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE)
													.addGap(0, 0,
															Short.MAX_VALUE))));
			layout.setVerticalGroup(layout
					.createParallelGroup(
							javax.swing.GroupLayout.Alignment.LEADING)
					.addGap(0, 381, Short.MAX_VALUE)
					.addGroup(
							layout.createParallelGroup(
									javax.swing.GroupLayout.Alignment.LEADING)
									.addGroup(
											layout.createSequentialGroup()
													.addGap(0, 0,
															Short.MAX_VALUE)
													.addComponent(
															jPanel1,
															javax.swing.GroupLayout.PREFERRED_SIZE,
															javax.swing.GroupLayout.DEFAULT_SIZE,
															javax.swing.GroupLayout.PREFERRED_SIZE)
													.addGap(0, 0,
															Short.MAX_VALUE))));

			pack();
		}// </editor-fold>

		private void cmdStartActionPerformed(java.awt.event.ActionEvent evt) {
			if (txtDistance.getText().isEmpty()) {
				return;
			}
			try {
				checkDistance = Integer.parseInt(txtDistance.getText());
			} catch (Exception e) {
				return;
			}
			Location = lstPlaces.getSelectedValue().toString();
			mineOres = new String[lstOres.getSelectedValues().length];
			for (int i = 0; i < lstOres.getSelectedValues().length; i++) {
				mineOres[i] = lstOres.getSelectedValues()[i].toString();
			}
			if (Location.equals("Power-Mining")) {
				Banking = false;
				if (chkM1D1.isSelected()) {
					M1D1 = true;
				} else {
					M1D1 = false;
				}
			} else if (Location.equals("Al Kharid")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(3269, 3167),
						new RSTile(3274, 3167), new RSTile(3277, 3171),
						new RSTile(3278, 3176), new RSTile(3279, 3181),
						new RSTile(3282, 3186), new RSTile(3283, 3191),
						new RSTile(3283, 3196), new RSTile(3283, 3201),
						new RSTile(3283, 3206), new RSTile(3284, 3211),
						new RSTile(3287, 3215), new RSTile(3291, 3219),
						new RSTile(3294, 3223), new RSTile(3295, 3228),
						new RSTile(3296, 3233), new RSTile(3297, 3238),
						new RSTile(3298, 3243), new RSTile(3298, 3248),
						new RSTile(3299, 3253), new RSTile(3299, 3258),
						new RSTile(3299, 3263), new RSTile(3299, 3268),
						new RSTile(3299, 3273), new RSTile(3299, 3278),
						new RSTile(3299, 3283), new RSTile(3299, 3288),
						new RSTile(3299, 3293), new RSTile(3297, 3298),
						new RSTile(3299, 3301) };
				atBank = new RSTile(3269, 3167);
				atMine = new RSArea(new RSTile[] { new RSTile(3298, 3280),
						new RSTile(3293, 3282), new RSTile(3291, 3287),
						new RSTile(3291, 3292), new RSTile(3291, 3297),
						new RSTile(3290, 3302), new RSTile(3293, 3306),
						new RSTile(3294, 3311), new RSTile(3295, 3316),
						new RSTile(3299, 3319), new RSTile(3304, 3318),
						new RSTile(3306, 3313), new RSTile(3302, 3310),
						new RSTile(3306, 3307), new RSTile(3304, 3302),
						new RSTile(3304, 3297), new RSTile(3304, 3292),
						new RSTile(3303, 3287), new RSTile(3301, 3282) });
			} else if (Location.equals("Ardougne East")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(2652, 3283),
						new RSTile(2647, 3283), new RSTile(2642, 3283),
						new RSTile(2642, 3288), new RSTile(2645, 3292),
						new RSTile(2649, 3295), new RSTile(2654, 3296),
						new RSTile(2659, 3299), new RSTile(2663, 3302),
						new RSTile(2668, 3305), new RSTile(2673, 3306),
						new RSTile(2678, 3306), new RSTile(2683, 3306),
						new RSTile(2688, 3305), new RSTile(2692, 3308),
						new RSTile(2694, 3313), new RSTile(2696, 3318),
						new RSTile(2697, 3323), new RSTile(2699, 3328) };
				atBank = new RSTile(2653, 3284);
				atMine = new RSArea(new RSTile[] { new RSTile(2690, 3341),
						new RSTile(2688, 3336), new RSTile(2688, 3331),
						new RSTile(2691, 3327), new RSTile(2696, 3326),
						new RSTile(2701, 3325), new RSTile(2706, 3325),
						new RSTile(2711, 3326), new RSTile(2715, 3329),
						new RSTile(2719, 3333) });
			} else if (Location.equals("Ardougne South")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(2611, 3092),
						new RSTile(2606, 3092), new RSTile(2606, 3097),
						new RSTile(2609, 3101), new RSTile(2614, 3102),
						new RSTile(2616, 3107), new RSTile(2618, 3112),
						new RSTile(2621, 3116), new RSTile(2623, 3121),
						new RSTile(2625, 3126), new RSTile(2627, 3131),
						new RSTile(2628, 3136), new RSTile(2630, 3141) };
				atBank = new RSTile(2653, 3284);
				atMine = new RSArea(new RSTile[] { new RSTile(2606, 3222),
						new RSTile(2601, 3221), new RSTile(2600, 3226),
						new RSTile(2600, 3231), new RSTile(2599, 3236),
						new RSTile(2601, 3241), new RSTile(2606, 3241),
						new RSTile(2609, 3237), new RSTile(2608, 3232),
						new RSTile(2608, 3227), new RSTile(2608, 3222) });
			} else if (Location.equals("Barbarian Village")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(3094, 3493),
						new RSTile(3089, 3493), new RSTile(3089, 3488),
						new RSTile(3092, 3484), new RSTile(3097, 3483),
						new RSTile(3100, 3479), new RSTile(3100, 3474),
						new RSTile(3100, 3469), new RSTile(3099, 3464),
						new RSTile(3096, 3460), new RSTile(3096, 3455),
						new RSTile(3093, 3451), new RSTile(3092, 3446),
						new RSTile(3091, 3441), new RSTile(3090, 3436),
						new RSTile(3089, 3431), new RSTile(3087, 3426),
						new RSTile(3085, 3421) };
				atBank = new RSTile(3093, 3491);
				atMine = new RSArea(new RSTile[] { new RSTile(3082, 3425),
						new RSTile(3077, 3424), new RSTile(3075, 3419),
						new RSTile(3078, 3415), new RSTile(3083, 3414),
						new RSTile(3087, 3417), new RSTile(3087, 3422) });
			} else if (Location.equals("North Draynor Village")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(3093, 3243),
						new RSTile(3093, 3248), new RSTile(3098, 3248),
						new RSTile(3103, 3250), new RSTile(3104, 3255),
						new RSTile(3104, 3260), new RSTile(3104, 3265),
						new RSTile(3104, 3270), new RSTile(3104, 3275),
						new RSTile(3107, 3279), new RSTile(3109, 3284),
						new RSTile(3110, 3289), new RSTile(3111, 3294),
						new RSTile(3115, 3298), new RSTile(3120, 3300),
						new RSTile(3124, 3303), new RSTile(3128, 3306),
						new RSTile(3132, 3309), new RSTile(3135, 3313),
						new RSTile(3140, 3316) };
				atBank = new RSTile(3092, 3243);
				atMine = new RSArea(new RSTile[] { new RSTile(3146, 3324),
						new RSTile(3141, 3324), new RSTile(3136, 3323),
						new RSTile(3134, 3318), new RSTile(3137, 3314),
						new RSTile(3142, 3313), new RSTile(3147, 3316),
						new RSTile(3149, 3321) });
			} else if (Location.equals("Lumbridge Swamp")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(3092, 3244),
						new RSTile(3092, 3249), new RSTile(3097, 3248),
						new RSTile(3101, 3245), new RSTile(3101, 3240),
						new RSTile(3101, 3235), new RSTile(3104, 3231),
						new RSTile(3108, 3228), new RSTile(3112, 3225),
						new RSTile(3114, 3220), new RSTile(3115, 3215),
						new RSTile(3119, 3212), new RSTile(3123, 3209),
						new RSTile(3128, 3208), new RSTile(3133, 3206),
						new RSTile(3136, 3202), new RSTile(3139, 3197),
						new RSTile(3140, 3192), new RSTile(3141, 3187),
						new RSTile(3142, 3182), new RSTile(3143, 3177),
						new RSTile(3146, 3173), new RSTile(3146, 3168),
						new RSTile(3147, 3163), new RSTile(3148, 3158),
						new RSTile(3149, 3153), new RSTile(3146, 3149) };
				atBank = new RSTile(3092, 3243);
				atMine = new RSArea(new RSTile[] { new RSTile(3146, 3156),
						new RSTile(3151, 3156), new RSTile(3150, 3151),
						new RSTile(3150, 3146), new RSTile(3147, 3142),
						new RSTile(3142, 3142), new RSTile(3141, 3147),
						new RSTile(3141, 3152), new RSTile(3145, 3155) });
			} else if (Location.equals("Rimmington")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(3013, 3354),
						new RSTile(3013, 3359), new RSTile(3009, 3362),
						new RSTile(3004, 3359), new RSTile(3004, 3354),
						new RSTile(3008, 3350), new RSTile(3008, 3345),
						new RSTile(3007, 3340), new RSTile(3007, 3335),
						new RSTile(3007, 3330), new RSTile(3006, 3325),
						new RSTile(3005, 3320), new RSTile(3005, 3315),
						new RSTile(3005, 3310), new RSTile(3004, 3305),
						new RSTile(3003, 3300), new RSTile(3002, 3295),
						new RSTile(3001, 3290), new RSTile(2999, 3285),
						new RSTile(2997, 3280), new RSTile(2995, 3275),
						new RSTile(2992, 3271), new RSTile(2988, 3268),
						new RSTile(2985, 3264), new RSTile(2982, 3259),
						new RSTile(2978, 3256), new RSTile(2978, 3251),
						new RSTile(2978, 3246), new RSTile(2977, 3242) };
				atBank = new RSTile(3012, 3356);
				atMine = new RSArea(new RSTile[] { new RSTile(2975, 3253),
						new RSTile(2970, 3251), new RSTile(2966, 3247),
						new RSTile(2965, 3242), new RSTile(2963, 3237),
						new RSTile(2966, 3233), new RSTile(2971, 3231),
						new RSTile(2974, 3227), new RSTile(2979, 3228),
						new RSTile(2984, 3227), new RSTile(2987, 3231),
						new RSTile(2989, 3236), new RSTile(2992, 3240),
						new RSTile(2993, 3245), new RSTile(2989, 3248),
						new RSTile(2984, 3249), new RSTile(2981, 3253) });
			} else if (Location.equals("Varrock East")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(3257, 3429),
						new RSTile(3262, 3429), new RSTile(3267, 3429),
						new RSTile(3272, 3429), new RSTile(3277, 3429),
						new RSTile(3282, 3427), new RSTile(3284, 3422),
						new RSTile(3285, 3417), new RSTile(3287, 3412),
						new RSTile(3290, 3408), new RSTile(3290, 3403),
						new RSTile(3291, 3398), new RSTile(3291, 3393),
						new RSTile(3291, 3388), new RSTile(3293, 3383),
						new RSTile(3293, 3378), new RSTile(3290, 3374),
						new RSTile(3287, 3369) };
				atBank = new RSTile(3253, 3421);
				atMine = new RSArea(new RSTile[] { new RSTile(3286, 3372),
						new RSTile(3281, 3371), new RSTile(3279, 3366),
						new RSTile(3276, 3362), new RSTile(3279, 3358),
						new RSTile(3284, 3357), new RSTile(3289, 3356),
						new RSTile(3294, 3355), new RSTile(3294, 3360),
						new RSTile(3291, 3364), new RSTile(3291, 3369),
						new RSTile(3288, 3373) });
			} else if (Location.equals("Varrock West")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(3182, 3436),
						new RSTile(3185, 3432), new RSTile(3182, 3428),
						new RSTile(3177, 3427), new RSTile(3172, 3427),
						new RSTile(3171, 3422), new RSTile(3171, 3417),
						new RSTile(3171, 3412), new RSTile(3170, 3407),
						new RSTile(3170, 3402), new RSTile(3172, 3397),
						new RSTile(3176, 3394), new RSTile(3176, 3389),
						new RSTile(3178, 3384), new RSTile(3181, 3380),
						new RSTile(3184, 3376), new RSTile(3181, 3372) };
				atBank = new RSTile(3185, 3436);
				atMine = new RSArea(new RSTile[] { new RSTile(3180, 3380),
						new RSTile(3177, 3376), new RSTile(3175, 3371),
						new RSTile(3171, 3368), new RSTile(3171, 3363),
						new RSTile(3176, 3365), new RSTile(3181, 3367),
						new RSTile(3183, 3372), new RSTile(3184, 3377),
						new RSTile(3186, 3382) });
			} else if (Location.equals("Yanille")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(2611, 3092),
						new RSTile(2606, 3092), new RSTile(2606, 3097),
						new RSTile(2609, 3101), new RSTile(2614, 3102),
						new RSTile(2616, 3107), new RSTile(2618, 3112),
						new RSTile(2621, 3116), new RSTile(2623, 3121),
						new RSTile(2625, 3126), new RSTile(2627, 3131),
						new RSTile(2628, 3136), new RSTile(2630, 3141) };
				atBank = new RSTile(2611, 3092);
				atMine = new RSArea(new RSTile[] { new RSTile(2628, 3152),
						new RSTile(2628, 3147), new RSTile(2626, 3142),
						new RSTile(2625, 3137), new RSTile(2625, 3132),
						new RSTile(2630, 3130), new RSTile(2634, 3133),
						new RSTile(2638, 3136), new RSTile(2640, 3141) });
			} else if (Location.equals("Khazard Battlefield")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(2652, 3284),
						new RSTile(2647, 3284), new RSTile(2643, 3281),
						new RSTile(2641, 3276), new RSTile(2641, 3271),
						new RSTile(2641, 3266), new RSTile(2638, 3262),
						new RSTile(2634, 3259), new RSTile(2631, 3255),
						new RSTile(2626, 3253), new RSTile(2622, 3250),
						new RSTile(2618, 3247), new RSTile(2613, 3245),
						new RSTile(2608, 3243), new RSTile(2603, 3241),
						new RSTile(2599, 3238), new RSTile(2595, 3235),
						new RSTile(2590, 3232), new RSTile(2585, 3230),
						new RSTile(2580, 3227), new RSTile(2575, 3225),
						new RSTile(2570, 3224), new RSTile(2565, 3226),
						new RSTile(2562, 3230), new RSTile(2560, 3235),
						new RSTile(2560, 3240), new RSTile(2560, 3245),
						new RSTile(2560, 3250), new RSTile(2560, 3255),
						new RSTile(2560, 3260), new RSTile(2555, 3261),
						new RSTile(2550, 3261), new RSTile(2545, 3262),
						new RSTile(2540, 3262), new RSTile(2535, 3262),
						new RSTile(2530, 3262), new RSTile(2525, 3262),
						new RSTile(2520, 3262), new RSTile(2515, 3262),
						new RSTile(2510, 3262), new RSTile(2505, 3263),
						new RSTile(2500, 3263), new RSTile(2495, 3263),
						new RSTile(2490, 3262), new RSTile(2485, 3260),
						new RSTile(2480, 3259), new RSTile(2475, 3257) };
				atBank = new RSTile(2653, 3284);
				atMine = new RSArea(new RSTile[] { new RSTile(2472, 3260),
						new RSTile(2467, 3257), new RSTile(2465, 3252),
						new RSTile(2470, 3251), new RSTile(2475, 3250),
						new RSTile(2476, 3255), new RSTile(2478, 3260) });
			} else if (Location.equals("Living Rock Cavern")
					|| Location.equals("LRC Power-Mining")) {
				Banking = true;
				MineFally = new RSTile[] { new RSTile(2969, 3341),
						new RSTile(2967, 3346), new RSTile(2964, 3350),
						new RSTile(2963, 3355), new RSTile(2964, 3360),
						new RSTile(2965, 3365), new RSTile(2966, 3370),
						new RSTile(2967, 3375), new RSTile(2970, 3379),
						new RSTile(2975, 3379), new RSTile(2980, 3378),
						new RSTile(2984, 3374), new RSTile(2988, 3371),
						new RSTile(2992, 3368), new RSTile(2997, 3368),
						new RSTile(3002, 3366), new RSTile(3007, 3365),
						new RSTile(3012, 3365), new RSTile(3017, 3365),
						new RSTile(3022, 3365), new RSTile(3027, 3365),
						new RSTile(3032, 3368), new RSTile(3037, 3368),
						new RSTile(3042, 3369), new RSTile(3047, 3369),
						new RSTile(3052, 3369), new RSTile(3057, 3369),
						new RSTile(3060, 3373), new RSTile(3060, 3378) };
				MineLumby = new RSTile[] { new RSTile(3221, 3218),
						new RSTile(3226, 3219), new RSTile(3231, 3219),
						new RSTile(3232, 3224), new RSTile(3232, 3229),
						new RSTile(3229, 3233), new RSTile(3225, 3236),
						new RSTile(3221, 3240), new RSTile(3219, 3245),
						new RSTile(3216, 3249), new RSTile(3211, 3249),
						new RSTile(3206, 3247), new RSTile(3201, 3247),
						new RSTile(3196, 3246), new RSTile(3191, 3246),
						new RSTile(3186, 3245), new RSTile(3181, 3245),
						new RSTile(3176, 3245), new RSTile(3171, 3245),
						new RSTile(3166, 3244), new RSTile(3160, 3244),
						new RSTile(3155, 3244), new RSTile(3150, 3244),
						new RSTile(3145, 3245), new RSTile(3143, 3250),
						new RSTile(3141, 3255), new RSTile(3140, 3260),
						new RSTile(3135, 3262), new RSTile(3130, 3262),
						new RSTile(3125, 3262), new RSTile(3120, 3262),
						new RSTile(3115, 3263), new RSTile(3110, 3264),
						new RSTile(3105, 3267), new RSTile(3103, 3272),
						new RSTile(3103, 3277), new RSTile(3103, 3282),
						new RSTile(3103, 3287), new RSTile(3098, 3289),
						new RSTile(3093, 3289), new RSTile(3088, 3288),
						new RSTile(3083, 3286), new RSTile(3078, 3284),
						new RSTile(3073, 3282), new RSTile(3069, 3279),
						new RSTile(3064, 3277), new RSTile(3059, 3277),
						new RSTile(3054, 3277), new RSTile(3049, 3277),
						new RSTile(3044, 3277), new RSTile(3039, 3277),
						new RSTile(3034, 3277), new RSTile(3029, 3277),
						new RSTile(3024, 3277), new RSTile(3018, 3277),
						new RSTile(3013, 3277), new RSTile(3009, 3280),
						new RSTile(3008, 3285), new RSTile(3007, 3290),
						new RSTile(3005, 3295), new RSTile(3005, 3300),
						new RSTile(3004, 3305), new RSTile(3004, 3310),
						new RSTile(3004, 3315), new RSTile(3005, 3320),
						new RSTile(3005, 3325), new RSTile(3006, 3330),
						new RSTile(3007, 3335), new RSTile(3007, 3340),
						new RSTile(3007, 3345), new RSTile(3008, 3350),
						new RSTile(3006, 3355), new RSTile(3003, 3359),
						new RSTile(3005, 3363), new RSTile(3010, 3363),
						new RSTile(3015, 3363), new RSTile(3020, 3363),
						new RSTile(3025, 3365), new RSTile(3030, 3367),
						new RSTile(3035, 3368), new RSTile(3040, 3368),
						new RSTile(3045, 3368), new RSTile(3050, 3369),
						new RSTile(3055, 3370), new RSTile(3059, 3373),
						new RSTile(3060, 3378) };
				MineLRC = new RSTile[] { new RSTile(3058, 9777),
						new RSTile(3051, 9780), new RSTile(3046, 9784),
						new RSTile(3044, 9791), new RSTile(3043, 9799),
						new RSTile(3041, 9806), new RSTile(3041, 9815),
						new RSTile(3041, 9824), new RSTile(3038, 9832),
						new RSTile(3028, 9834), new RSTile(3019, 9833),
						new RSTile(3013, 9832) };
				/* Feb 27th */atFally = new RSArea(new RSTile[] {
						new RSTile(2974, 3398), new RSTile(2974, 3393),
						new RSTile(2974, 3388), new RSTile(2974, 3383),
						new RSTile(2974, 3378), new RSTile(2974, 3373),
						new RSTile(2974, 3368), new RSTile(2974, 3363),
						new RSTile(2971, 3359), new RSTile(2966, 3359),
						new RSTile(2961, 3359), new RSTile(2956, 3360),
						new RSTile(2954, 3365), new RSTile(2954, 3370),
						new RSTile(2954, 3375), new RSTile(2954, 3380),
						new RSTile(2954, 3385), new RSTile(2954, 3390),
						new RSTile(2954, 3395), new RSTile(2954, 3400),
						new RSTile(2959, 3400), new RSTile(2964, 3400),
						new RSTile(2969, 3400), new RSTile(2974, 3400) });
				atLumby = new RSArea(new RSTile[] { new RSTile(3217, 3230),
						new RSTile(3217, 3225), new RSTile(3217, 3220),
						new RSTile(3217, 3215), new RSTile(3217, 3209),
						new RSTile(3222, 3207), new RSTile(3225, 3211),
						new RSTile(3226, 3216), new RSTile(3226, 3221),
						new RSTile(3224, 3226), new RSTile(3222, 3231) });
				atBank = new RSTile(3654, 5114);
				if (mineOres[0].toString().equals("Concentrated Gold")) {
					SouthtoBank = new RSTile[] { new RSTile(3666, 5080),
							new RSTile(3660, 5090), new RSTile(3658, 5098),
							new RSTile(3659, 5107), new RSTile(3655, 5114) };
					WesttoBank = new RSTile[] { new RSTile(3641, 5095),
							new RSTile(3651, 5095), new RSTile(3658, 5104),
							new RSTile(3655, 5113) };
					WesttoSouth = new RSTile[] { new RSTile(3640, 5095),
							new RSTile(3650, 5093), new RSTile(3660, 5089),
							new RSTile(3666, 5081), new RSTile(3669, 5076) };
				} else {
					SouthtoBank = new RSTile[] { new RSTile(3665, 5092),
							new RSTile(3661, 5095), new RSTile(3660, 5104),
							new RSTile(3654, 5114) };
					WesttoBank = new RSTile[] { new RSTile(3674, 5101),
							new RSTile(3673, 5104), new RSTile(3667, 5111),
							new RSTile(3660, 5114), new RSTile(3654, 5114)

					};
					WesttoSouth = new RSTile[] { new RSTile(3673, 5098),
							new RSTile(3670, 5095), new RSTile(3665, 5092) };
				}
				toMineLumby = walking.newTilePath(MineLumby);
				toMineFally = walking.newTilePath(MineFally);
				toMineLRC = walking.newTilePath(MineLRC);
				toBankSouthLRC = walking.newTilePath(SouthtoBank);
				toBankWestLRC = walking.newTilePath(WesttoBank);
				toSouthLRC = walking.newTilePath(reversePath(SouthtoBank));
				toWestLRC = walking.newTilePath(reversePath(WesttoBank));
				toWestfromSouth = walking.newTilePath(reversePath(WesttoSouth));
				toSouthfromWest = walking.newTilePath(WesttoSouth);
			} else if (Location.equals("Mining Guild")) {
				Banking = true;
				Ladder = new RSTile[] { new RSTile(3012, 3356),
						new RSTile(3021, 3360), new RSTile(3023, 3352),
						new RSTile(3031, 3344), new RSTile(3023, 3337) };
				Mine = new RSTile[] { new RSTile(3019, 9737),
						new RSTile(3028, 9736), new RSTile(3039, 9736),
						new RSTile(3050, 9736) };
				atBank = new RSTile(3012, 3356);
				atMine = new RSArea(new RSTile[] { new RSTile(3026, 9741),
						new RSTile(3027, 9730), new RSTile(3037, 9730),
						new RSTile(3043, 9730), new RSTile(3060, 9730),
						new RSTile(3060, 9746), new RSTile(3055, 9750),
						new RSTile(3030, 9749), new RSTile(3026, 9741) });
				Bank = reversePath(Ladder);
				toLadderTop = walking.newTilePath(Ladder);
				toMine = walking.newTilePath(Mine);
				toBank = walking.newTilePath(Bank);
				toLadderBottom = walking.newTilePath(reversePath(Mine));
			} else if (Location.equals("TzHaar")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(2445, 5178),
						new RSTile(2447, 5171), new RSTile(2452, 5168),
						new RSTile(2459, 5169), };
				atBank = new RSTile(2445, 5178);
				atMine = new RSArea(new RSTile[] { new RSTile(2453, 5170),
						new RSTile(2459, 5178), new RSTile(2464, 5178),
						new RSTile(2464, 5167), new RSTile(2454, 5164) });
			} else if (Location.equals("Karamja Volcano")) {
				Banking = true;
				Mine = new RSTile[] { new RSTile(2445, 5178),
						new RSTile(2447, 5171), new RSTile(2452, 5168),
						new RSTile(2459, 5169), new RSTile(2463, 5171),
						new RSTile(2470, 5168), new RSTile(2478, 5168),
						new RSTile(2480, 5175) };
				atBank = new RSTile(2445, 5178);
				atMine = new RSArea(new RSTile[] { new RSTile(2857, 9569),
						new RSTile(2857, 9583), new RSTile(2866, 9583),
						new RSTile(2866, 9569) });
			} else if (Location.equals("Taverly")) {
				Banking = true;
				Ladder = new RSTile[] { new RSTile(2944, 3371),
						new RSTile(2945, 3376), new RSTile(2940, 3377),
						new RSTile(2940, 3372), new RSTile(2940, 3367),
						new RSTile(2940, 3362), new RSTile(2938, 3357) };
				Mine = new RSTile[] { new RSTile(2934, 3356),
						new RSTile(2929, 3356), new RSTile(2924, 3355),
						new RSTile(2919, 3355), new RSTile(2914, 3356),
						new RSTile(2909, 3358) };
				atBank = new RSTile(2946, 3369);
				atMine = new RSArea(new RSTile[] { new RSTile(2907, 3371),
						new RSTile(2904, 3367), new RSTile(2903, 3362),
						new RSTile(2902, 3357), new RSTile(2902, 3352),
						new RSTile(2907, 3351), new RSTile(2909, 3356),
						new RSTile(2910, 3361), new RSTile(2913, 3365),
						new RSTile(2914, 3370) });
				Bank = reversePath(Ladder);
				toLadderTop = walking.newTilePath(Ladder);
				toMine = walking.newTilePath(Mine);
				toBank = walking.newTilePath(Bank);
				toLadderBottom = walking.newTilePath(reversePath(Mine));
			} else if (Location.equals("Dwarven Mine")) {
				Banking = true;
				atFally = new RSArea(new RSTile[] { new RSTile(2979, 3374),
						new RSTile(2978, 3379), new RSTile(2973, 3382),
						new RSTile(2970, 3386), new RSTile(2968, 3391),
						new RSTile(2963, 3391), new RSTile(2961, 3386),
						new RSTile(2956, 3384), new RSTile(2955, 3379),
						new RSTile(2960, 3379), new RSTile(2961, 3374),
						new RSTile(2961, 3369), new RSTile(2961, 3364),
						new RSTile(2962, 3359), new RSTile(2962, 3354),
						new RSTile(2962, 3349), new RSTile(2961, 3344),
						new RSTile(2963, 3339), new RSTile(2968, 3338),
						new RSTile(2972, 3335), new RSTile(2977, 3335),
						new RSTile(2981, 3338), new RSTile(2980, 3344),
						new RSTile(2975, 3346), new RSTile(2971, 3349),
						new RSTile(2968, 3353), new RSTile(2968, 3358),
						new RSTile(2968, 3363), new RSTile(2970, 3368),
						new RSTile(2971, 3373) });
				atLumby = new RSArea(new RSTile[] { new RSTile(3217, 3230),
						new RSTile(3217, 3225), new RSTile(3217, 3220),
						new RSTile(3217, 3215), new RSTile(3217, 3209),
						new RSTile(3222, 3207), new RSTile(3225, 3211),
						new RSTile(3226, 3216), new RSTile(3226, 3221),
						new RSTile(3224, 3226), new RSTile(3222, 3231) });
				MineFally = new RSTile[] { new RSTile(2969, 3341),
						new RSTile(2967, 3346), new RSTile(2964, 3350),
						new RSTile(2963, 3355), new RSTile(2964, 3360),
						new RSTile(2965, 3365), new RSTile(2966, 3370),
						new RSTile(2967, 3375), new RSTile(2970, 3379),
						new RSTile(2975, 3379), new RSTile(2980, 3378),
						new RSTile(2984, 3374), new RSTile(2988, 3371),
						new RSTile(2992, 3368), new RSTile(2997, 3368),
						new RSTile(3002, 3366), new RSTile(3007, 3365),
						new RSTile(3012, 3365), new RSTile(3017, 3365),
						new RSTile(3022, 3365), new RSTile(3027, 3365),
						new RSTile(3032, 3368), new RSTile(3037, 3368),
						new RSTile(3042, 3369), new RSTile(3047, 3369),
						new RSTile(3052, 3369), new RSTile(3057, 3369),
						new RSTile(3060, 3373), new RSTile(3060, 3378) };
				MineLumby = new RSTile[] { new RSTile(3221, 3218),
						new RSTile(3226, 3219), new RSTile(3231, 3219),
						new RSTile(3232, 3224), new RSTile(3232, 3229),
						new RSTile(3229, 3233), new RSTile(3225, 3236),
						new RSTile(3221, 3240), new RSTile(3219, 3245),
						new RSTile(3216, 3249), new RSTile(3211, 3249),
						new RSTile(3206, 3247), new RSTile(3201, 3247),
						new RSTile(3196, 3246), new RSTile(3191, 3246),
						new RSTile(3186, 3245), new RSTile(3181, 3245),
						new RSTile(3176, 3245), new RSTile(3171, 3245),
						new RSTile(3166, 3244), new RSTile(3160, 3244),
						new RSTile(3155, 3244), new RSTile(3150, 3244),
						new RSTile(3145, 3245), new RSTile(3143, 3250),
						new RSTile(3141, 3255), new RSTile(3140, 3260),
						new RSTile(3135, 3262), new RSTile(3130, 3262),
						new RSTile(3125, 3262), new RSTile(3120, 3262),
						new RSTile(3115, 3263), new RSTile(3110, 3264),
						new RSTile(3105, 3267), new RSTile(3103, 3272),
						new RSTile(3103, 3277), new RSTile(3103, 3282),
						new RSTile(3103, 3287), new RSTile(3098, 3289),
						new RSTile(3093, 3289), new RSTile(3088, 3288),
						new RSTile(3083, 3286), new RSTile(3078, 3284),
						new RSTile(3073, 3282), new RSTile(3069, 3279),
						new RSTile(3064, 3277), new RSTile(3059, 3277),
						new RSTile(3054, 3277), new RSTile(3049, 3277),
						new RSTile(3044, 3277), new RSTile(3039, 3277),
						new RSTile(3034, 3277), new RSTile(3029, 3277),
						new RSTile(3024, 3277), new RSTile(3018, 3277),
						new RSTile(3013, 3277), new RSTile(3009, 3280),
						new RSTile(3008, 3285), new RSTile(3007, 3290),
						new RSTile(3005, 3295), new RSTile(3005, 3300),
						new RSTile(3004, 3305), new RSTile(3004, 3310),
						new RSTile(3004, 3315), new RSTile(3005, 3320),
						new RSTile(3005, 3325), new RSTile(3006, 3330),
						new RSTile(3007, 3335), new RSTile(3007, 3340),
						new RSTile(3007, 3345), new RSTile(3008, 3350),
						new RSTile(3006, 3355), new RSTile(3003, 3359),
						new RSTile(3005, 3363), new RSTile(3010, 3363),
						new RSTile(3015, 3363), new RSTile(3020, 3363),
						new RSTile(3025, 3365), new RSTile(3030, 3367),
						new RSTile(3035, 3368), new RSTile(3040, 3368),
						new RSTile(3045, 3368), new RSTile(3050, 3369),
						new RSTile(3055, 3370), new RSTile(3059, 3373),
						new RSTile(3060, 3378) };
				Mine = new RSTile[] { new RSTile(3013, 3355),
						new RSTile(3013, 3360), new RSTile(3018, 3362),
						new RSTile(3023, 3364), new RSTile(3028, 3366),
						new RSTile(3033, 3367), new RSTile(3038, 3369),
						new RSTile(3043, 3369), new RSTile(3048, 3370),
						new RSTile(3053, 3370), new RSTile(3058, 3370),
						new RSTile(3060, 3375) };
				atBank = new RSTile(3012, 3356);
				atMine = new RSArea(new RSTile[] { new RSTile(3063, 9757),
						new RSTile(3063, 9787), new RSTile(3024, 9787),
						new RSTile(3024, 9757) });
				toMineLumby = walking.newTilePath(MineLumby);
				toMineFally = walking.newTilePath(MineFally);
			}
			if (!Location.equals("Power-Mining")
					&& !Location.equals("Living Rock Cavern")
					&& !Location.equals("Mining Guild")
					&& !Location.equals("LRC Power-Mining")
					&& !Location.equals("Taverly")) {
				Bank = reversePath(Mine);
				toMine = walking.newTilePath(Mine);
				toBank = walking.newTilePath(Bank);
			}
			startPos = getMyPlayer().getLocation();
			startProgram = true;
			this.setVisible(false);
		}

		private void lstPlacesValueChanged(
				javax.swing.event.ListSelectionEvent evt) {
			DefaultListModel Ores = (DefaultListModel) lstOres.getModel();
			String Loc = lstPlaces.getSelectedValue().toString();
			if (Loc.equals("Power-Mining")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Clay");
				Ores.addElement("Copper");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
				Ores.addElement("Silver");
				Ores.addElement("Coal");
				Ores.addElement("Gold");
				Ores.addElement("Granite");
				Ores.addElement("Mithril");
				Ores.addElement("Adamantite");
				Ores.addElement("Runite");
			} else if (Loc.equals("LRC Power-Mining")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				Ores.addElement("Concentrated Coal");
				Ores.addElement("Concentrated Gold");
			} else if (Loc.equals("Al Kharid")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Copper");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
				Ores.addElement("Silver");
				Ores.addElement("Coal");
				Ores.addElement("Gold");
				Ores.addElement("Mithril");
				Ores.addElement("Adamantite");
			} else if (Loc.equals("Ardougne East")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Iron");
				Ores.addElement("Coal");
			} else if (Loc.equals("Ardougne South")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Iron");
				Ores.addElement("Coal");
			} else if (Loc.equals("Barbarian Village")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Tin");
				Ores.addElement("Coal");
			} else if (Loc.equals("North Draynor Village")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Clay");
			} else if (Loc.equals("Lumbridge Swamp")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Coal");
				Ores.addElement("Mithril");
				Ores.addElement("Adamantite");
			} else if (Loc.equals("Rimmington")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Clay");
				Ores.addElement("Copper");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
				Ores.addElement("Gold");
			} else if (Loc.equals("Varrock East")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Copper");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
			} else if (Loc.equals("Varrock West")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Clay");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
				Ores.addElement("Silver");
			} else if (Loc.equals("Yanille")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Clay");
				Ores.addElement("Copper");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
				Ores.addElement("Coal");
				Ores.addElement("Mithril");
			} else if (Loc.equals("Khazard Battlefield")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Copper");
			} else if (Loc.equals("Living Rock Cavern")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				Ores.addElement("Concentrated Coal");
				Ores.addElement("Concentrated Gold");
			} else if (Loc.equals("Mining Guild")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Coal");
				Ores.addElement("Mithril");
			} else if (Loc.equals("TzHaar")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Silver");
				Ores.addElement("Gold");
			} else if (Loc.equals("Karamja Volcano")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Gold");
			} else if (Loc.equals("Taverly")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Copper");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
				Ores.addElement("Coal");
			} else if (Loc.equals("Dwarven Mine")) {
				Ores.clear();
				lstOres.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
				Ores.addElement("Copper");
				Ores.addElement("Tin");
				Ores.addElement("Iron");
				Ores.addElement("Coal");
				Ores.addElement("Gold");
				Ores.addElement("Mithril");
				Ores.addElement("Adamantite");
			}
		}

		private javax.swing.JTextField txtDistance;
		private javax.swing.JButton cmdStart;
		private javax.swing.JLabel jLabel1;
		private javax.swing.JLabel lblDistance;
		private javax.swing.JLabel jLabel2;
		private javax.swing.JLabel jLabel3;
		private javax.swing.JLabel jLabel4;
		private javax.swing.JLabel jLabel5;
		private javax.swing.JLabel jLabel6;
		private javax.swing.JLabel jLabel7;
		private javax.swing.JLabel jLabel8;
		private javax.swing.JPanel jPanel1;
		private javax.swing.JPanel jPanel2;
		private javax.swing.JScrollPane jScrollPane1;
		private javax.swing.JScrollPane jScrollPane2;
		private javax.swing.JSeparator jSeparator1;
		private javax.swing.JList lstOres;
		private javax.swing.JList lstPlaces;
		private javax.swing.JCheckBox chkM1D1;
	}
}
