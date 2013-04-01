import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.imageio.ImageIO;

import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.core.event.listeners.MessageListener;
import org.powerbot.core.event.listeners.PaintListener;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Environment;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Settings;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.Walking;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.input.Keyboard;
import org.powerbot.game.api.methods.input.Mouse;
import org.powerbot.game.api.methods.interactive.NPCs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.node.GroundItems;
import org.powerbot.game.api.methods.node.Menu;
import org.powerbot.game.api.methods.node.SceneEntities;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Prayer;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Camera;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Random;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.util.Timer;
import org.powerbot.game.api.wrappers.Area;
import org.powerbot.game.api.wrappers.Locatable;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.graphics.CapturedModel;
import org.powerbot.game.api.wrappers.interactive.Character;
import org.powerbot.game.api.wrappers.interactive.NPC;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.api.wrappers.node.GroundItem;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.api.wrappers.node.SceneObject;
import org.powerbot.game.api.wrappers.node.SceneObjectDefinition;
import org.powerbot.game.api.wrappers.widget.Widget;
import org.powerbot.game.api.wrappers.widget.WidgetChild;
import org.powerbot.game.bot.Context;

@Manifest(name = "shitDungeon - 4038", authors = "Cake", version = 02.10, description = "Retarded Dungeoneering", website = "http://parisisfrom.asia/")
public class shitDungeon extends ActiveScript implements KeyListener, MouseListener, MouseMotionListener,
  	MessageListener, PaintListener {

	private final String VERSION = "K.K.K";
	private final boolean freeVersion = false, liteVersion = false;

	private final int SMUGGLER = 11226, SKINWEAVER = 10058;
	private final int ANTIPOISON = 17566, GATESTONE = 17489, GGS = 18829, COINS = 18201, ESSENCE = 17776,
			FEATHERS = 17796, PICKAXE = 16295;
	private final int END_STAIRS = 50552, ENTRANCE = 48496, GNOMEBALL = 751, GREEN_CHARM = 18018, BLUE_CHARM = 18020;

	private final int[] BLOCK_ANIMS = { 733, 7660, 13085, 13573, 13711 };
	private final int[] BONES = { 17670, 17672, 17674, 17676, 17671, 18830, 18831, 18833, 18834 }, RINGS = { 15707,
			18817, 18818, 18819, 18820, 18821, 18822, 18823, 18824, 18825, 18826, 18827, 18828 };
	private final int[] DUNG_EQUIP_SLOTS = { 0, 4, 5, 7, 9, 10 };
	private final int[] EXIT_LADDERS = { 50604, 51156, 51704, 54675, 56149 }, FINISH_LADDERS = { 49695, 49697, 49699,
			53747, 55483 }, FINISHED_LADDERS = { 49696, 49698, 49700, 53748, 55484 };
	private final int[] FIRES = { 49940, 49941, 49942, 49943, 49944, 49945, 49946, 49947, 49948, 49949 };
	private final int[] FOOD_ARRAY = { 18159, 18161, 18163, 18165, 18167, 18169, 18171, 18173, 18175, 18177 };
	private final int[] FRIENDLIES = { SMUGGLER, 11246, SKINWEAVER, 11086, 11087, 11088, 11089, 11090, 11091, 11092,
			11093, 11094, 11095, 11002, 11003, 11004 };
	private final int[] GROUP_PORTAL = { 53124, 53125, 53126, 7528, 56146 }, RUNECRAFTING_ALTARS = { 50035, 50036,
			50037, 53844, 55994 };
	private final int[] COSMIC_RUNES = { 16100, 17789 }, LAW_RUNES = { 16103, 17792 };
	private final int[] UNLIT_LOGS = { 17682, 17684, 17686, 17688, 17690, 17692, 17694, 17696, 17698, 17700 };
	private final int[] BLASTBOXES = { 19865, 19866 };
	private final int[] SLAYER_MONSTERS = { 10694, 10695, 10696, 10697, 10698, 10699, 10700, 10701, 10702, 10704, 10705 };
	private final int[] TIER_BASE = { 16273, 16339, 16383, 16405, 16647, 16669, 16691, 16713, 16889, 16935, 17239,
			17341 };
	private final int[] SECONDARY_BOWS = { 16317, 16319, 16321, 16323, 16325, 16327, 16329, 16331, 16333, 16335, 16337,
			16867, 16869, 16871, 16873, 16875, 16877, 16879, 16881, 16883, 16885, 16887 };

	private final int[] BASIC_DOORS = { 50342, 50343, 50344, 53948, 55762 }, GUARDIAN_DOORS = { 50346, 50347, 50348,
			53949, 55763 };
	private final int[] BACK_DOORS = { 49462, 50342, 50343, 50344, 53948, 55762, 50350, 50351, 50352, 53950, 55764 },
			BOSS_DOORS = { 50350, 50351, 50352, 53950, 55764 };

	private final int[] BLOCK_RUNED = { 50278, 50279, 50280, 53953, 55741 }, BLOCK_ROCKS = { 50305, 50306, 50307,
			53962, 55750 }, BLOCK_FLAMMABLE = { 50314, 50315, 50316, 53965, 55753 }, BLOCK_MAGICAL = { 50329, 50330,
			50331, 53970, 55758 }, BLOCK_SPIRIT = { 50332, 50333, 50334, 53971, 55759 };
	private final int[] BLOCK_VINES = { 50323, 50324, 50325, 53968, 55756 }, BLOCK_RAMOKEE = { 50326, 50327, 50328,
			53969, 55757 };
	private final int[][] BLOCK_DOORS = { BLOCK_RUNED, BLOCK_ROCKS, BLOCK_FLAMMABLE, BLOCK_MAGICAL, BLOCK_SPIRIT,
			BLOCK_VINES, BLOCK_RAMOKEE };
	private final String[][] BLOCK_STRINGS = { { "Imbue", "Runed door" }, { "Mine", "Pile of rocks" },
			{ "Burn", "Flammable debris" }, { "Dispel", "Magical barrier" }, { "Exorcise", "Dark spirit" },
			{ "Prune-vines", "Vine-covered door" }, { "Dismiss", "Ramokee exile" } };

	private final int[] SKILL_BARRED = { 50272, 50273, 50274, 53951, 55739 }, SKILL_LOCKED = { 50287, 50288, 50289,
			53956, 55744 }, SKILL_PULLEY = { 50299, 50300, 50301, 53960, 55748 }, SKILL_KEYED = { 50308, 50309, 50310,
			53963, 55751 }, SKILL_BARRICADE = { 50317, 50318, 50319, 53966, 55754 };
	private final int[] SKILL_COLLAPSING = { 50281, 50282, 50283, 53954, 55742 }, SKILL_PADLOCK = { 50293, 50294,
			50295, 53958, 55746 }, SKILL_LIQUID = { 50335, 50336, 50337, 53972, 55760 };
	private final int[][] SKILL_DOORS = { SKILL_BARRED, SKILL_LOCKED, SKILL_PULLEY, SKILL_KEYED, SKILL_BARRICADE,
			SKILL_COLLAPSING, SKILL_PADLOCK, SKILL_LIQUID };
	private final String[][] SKILL_STRINGS = { { "Force-bar", "Barred door" }, { "Disarm", "Locked door" },
			{ "Fix-pulley", "Broken pulley door" }, { "Repair", "Broken key door" },
			{ "Chop-down", "Wooden barricade" }, { "Repair", "Collapsing doorframe" },
			{ "Pick-lock", "Padlocked door" }, { "Add-compound", "Liquid lock door" } };

	private final int[] PUZZLE_DOORS = { 49306, 49335, 49336, 49337, 49338, 49375, 49376, 49377, 49378, 49379, 49380,
			49387, 49388, 49389, 49462, 49463, 49464, 49504, 49505, 49506, 49516, 49517, 49518, 49564, 49565, 49566,
			49574, 49575, 49576, 49577, 49578, 49579, 49603, 49604, 49605, 49606, 49607, 49608, 49625, 49626, 49627,
			49644, 49645, 49646, 49650, 49651, 49652, 49677, 49678, 49679, 53987, 53988, 53989, 53990, 53992, 54000,
			54001, 54006, 54067, 54070, 54071, 54072, 54073, 54106, 54107, 54108, 54109, 54236, 54274, 54284, 54299,
			54300, 54315, 54316, 54317, 54318, 54319, 54320, 54335, 54360, 54361, 54362, 54363, 54404, 54417, 54418,
			54620, 55478, 55479, 55480, 55481, 56079, 56081, 56526, 56527, 56528, 56529 };
	private final int[] WARPED_PUZZLE_DOORS = { 32370, 32406, 33607, 33634, 33637, 33654, 33712, 34269, 34865, 35273,
			35348, 35863, 35907, 37199, 37201, 39522, 39854, 39856, 39863, 39901, 39965, 55482, 56082, 56530 };
	private final int[] NECROMANCERS = { 10196, 10197, 10198, 10199, 10200, 10201, 10202, 10203, 10204, 10205, 10206 };
	private final int[] SUMMONING_OBELISKS = { 50205, 50206, 50207, 53883, 54650, 55605 };

	private final int[] ROK_MELEE = { 18817, 18818, 18819 }, ROK_RANGE = { 18820, 18821, 18822 }, ROK_MAGIC = { 18823,
			18824, 18825 };
	private final int[][] KINSHIP_CUSTOM_RINGS = { ROK_MELEE, ROK_RANGE, ROK_MAGIC };

	private final int[] JOURNAL_MISC = { 15726, 15727, 15729, 15732, 15733, 15735, 15736, 15739, 15740, 19682, 19683,
			19684, 19852, 19853, 19854, 19860, 19861, 19862, 19863, 19864 };
	private final int[] JOURNAL_CHRONICLES = { 15708, 15709, 15710, 15711, 15712, 15713, 15714, 15715, 15716, 15717,
			15718, 15719, 15720, 15721, 15722, 15723, 15724, 15725, 19676, 19677, 19678, 19679, 19680, 19681, 19846,
			19847, 19848, 19849, 19850, 19851 };

	private final int[] JOURNAL_NOTES = { 15730, 15741, 15742, 19687, 19857, 15728, 15734, 15737, 19685, 19855, 15731,
			15738, 15743, 19686, 19856 };
	private final int[] SAGA_MEMORIES = { 4315, 4317, 4319, 70195 };

	private final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
	private final Monster[] HOODED_MONSTERS = { Monster.FORGOTTEN_WARRIOR0CHAIN, Monster.FORGOTTEN_WARRIOR0PLATE,
			Monster.FORGOTTEN_RANGER, Monster.ZOMBIE0MELEE, Monster.ZOMBIE0RANGE, Monster.SKELETON0MELEE,
			Monster.SKELETON0RANGE, Monster.HILL_GIANT };
	private final Slot[] EQUIP_SLOTS = { Slot.HELMET, Slot.BODY, Slot.SHIELD, Slot.LEGS, Slot.HANDS, Slot.FEET };

	private final String[] KEY_COLORS = { "Orange", "Silver", "Yellow", "Green", "Blue", "Purple", "Crimson", "Gold" };
	private final String[] KEY_SHAPES = { "triangle", "diamond", "rectangle", "pentagon", "corner", "crescent",
			"wedge", "shield" };

	private final String[] HOODED_DEACTIVATORS = { "Forgotten mage", "Necromancer", "Seeker", "Reborn mage" };
	private final String[] PRIORITY_MONSTERS = { "Necromancer", "Reborn mage", "dragon" }, SECONDARY_MONSTERS = {
			"shade", "mage" };
	private final String[] DISTANCE_MONSTER = { "Forgotten ranger", "Forgotten mage", "Mysterious shade", "Zombie",
			"Hydra", "Icefiend", "Animated book", "Skeleton", "Thrower troll", "Ice elemental", "Lesser demon",
			"Greater demon", "Necromancer", "Ghost" };
	private final String[] POISON_MONSTER = { "Dungeon spider", "Cave crawler", "Night spider" };
	private final String[] METAL_TIERS = { "Novite", "Bathus", "Marmaros", "Kratonite", "Fractite", "Zephyrium",
			"Argonite", "Katagon", "Gorgonite", "Promethium", "Primal" };
	private final String[] LEATHER_TIERS = { "Protoleather", "Subleather", "Paraleather", "Archleather",
			"Dromoleather", "Spinoleather", "Gallileather", "Stegoleather", "Megaleather", "Tyrannoleather",
			"Sagittarian" };
	private final String[] CLOTH_TIERS = { "Salve", "Wildercress", "Blightleaf", "Roseblood", "Bryll", "Duskweed",
			"Soulbell", "Ectograss", "Runic", "Spiritbloom", "Celestial" };
	private final String[] WOOD_TIERS = { "Tangle", "Seeping", "Blood", "Utuku", "Spinebeam", "Bovistrangler",
			"Thigat", "Corpsethorn", "Entgallow", "Grave", "Sagittarian" };
	private final String[] MELEE_SLOTS = { "helm", "body", "kiteshield", "plate", "gauntlets", "boots" };
	private final String[] RANGE_SLOTS = { "coif", "body", "", "chaps", "vambraces", "boots" };
	private final String[] MAGIC_SLOTS = { "hood", "top", "", "bottom", "gloves", "boots" };

	private static final String[] FOOD_NAMES = { "Heim crab", "Red-eye", "Dusk eel", "Giant flatfish",
			"Short-finned eel", "Web snipper", "Bouldabass", "Salve eel", "Blue crab", "Cave moray" };
	private static final SimpleDateFormat LOG_DATE = new SimpleDateFormat("d MMM',' HH:mm:ss");

	private final double RANDOMIZED = Random.nextDouble(0.75, 1.25);
	private final int BLOOD_NECKLACE = 17291, HEXHUNTER_BOW = 17295, SHADOW_SILK_HOOD = 17279;
	private final Point MM_CENTER = new Point(627, 88);
	private final String EMPTY_TIMER = "00:00:00";
	private final Tile ORIGIN = new Tile(0, 0, 0), DAEMONHEIM = new Tile(3450, 3720, 0);

	private static boolean developer, shadowHooded, isCursing, partyFormed;
	private static int dungLevel, prayerLevel, slayerLevel;
	private static ArrayList<Tile> roomFlags = new ArrayList<Tile>();
	private static CopyOnWriteArrayList<LogMsg> logger = new CopyOnWriteArrayList<LogMsg>();
	private static Point cp = new Point(0, 0), mp = new Point(0, 0);
	private static String username;

	private Tile[][] roomNodes;
	private Case[] cases;
	private final int[] alphaInt = new int[128];
	private int[] animBreaker;
	private int[] foods = FOOD_ARRAY, goodFoods, topFoods, improvements;
	private final int[] equipmentTiers = { 0, 0, 0, 0, 0, 0 }, initialEquipmentTiers = { 0, 0, 0, 0, 0, 0 },
			tempTiers = { 0, 0, 0, 0, 0, 0 };
	private String[] armorTiers = METAL_TIERS, weaponTiers = METAL_TIERS, slotNames = MELEE_SLOTS;

	private ArrayList<String> partyMembers = new ArrayList<String>();
	private boolean cooperativeParty, partyMode, isLeader = true, showPartySettings, afking;
	private final int designedFor = 1;
	private int partyIndex;
	private int partySize = 1;
	private String partyLeader;
	private Timer reqTimer;

	private final ArrayList<Integer> bounds = new ArrayList<Integer>(), sales = new ArrayList<Integer>(),
			saves = new ArrayList<Integer>(), keyStore = new ArrayList<Integer>();
	private ArrayList<Door> bossPath = new ArrayList<Door>(), deathPath = new ArrayList<Door>();
	private final ArrayList<Door> goodPath = new ArrayList<Door>();
	private final ArrayList<Room> rooms = new ArrayList<Room>();
	private final ArrayList<Tile> badTiles = new ArrayList<Tile>(), perm = new ArrayList<Tile>();
	private ArrayList<Tile> puzzlePoints = new ArrayList<Tile>();

	private Image HEADER, BACKGROUND, MOUSE;
	private Image STATISTICS, OPTIONS, STATISTICS_H, OPTIONS_H, TAB_SELECTED, TEXT_STATISTICS, TEXT_OPTIONS,
			TEXT_BINDS;
	private Image STOP, STOP_S, INFO, INFO_S, HELP, HELP_S, HIDE, HIDE_H, LOGO;
	private Image SIDEBAR, SIDEBAR_SELECT, CHECKBOX, CHECKBOX_A, CHECKBOX_H;
	private Image UPARROW, UPARROW_S, DOWNARROW, DOWNARROW_S, MINIS, MINIS_S;
	private Image MAP_TAB, BUTTON, BUTTON_A;

	private boolean stopAtNext, stopScript, memberWorld = true, newDungeon = true, inDungeon, levelRequirement = true,
			welcomeBack;
	private boolean isDead, unreachable, outOfAmmo, lockDown, skipRoom, isRushing, skipDoorFound, hideSecondary, relog;
	private boolean explore, open, bossFight, bossFinished, retrace, finish, exit;
	private boolean settingsFinished, forcePrestige, aborted, twoHanded, cookReady, spawnRoom;
	private boolean puzzleSolved, puzzleRepeat, roomSwitch, disableBossPath, getFeathers, scoreSubmitted;
	private double tpf;
	private int attackMode = -1, backupMode = -1, defaultMode = -1;
	private final int defenseMode = 3;
	private int weaponTier = 11;
	private int attackTier;
	private int defenseTier;
	private int combatLevel = 60, newWeapon, startArmor, armorSlot = -1, currentWep = -1, primaryWep = -1,
			secondaryWep = -1, arrows = -1, blastBox = -1;
	private int abortedCount, controlledAborts, dungeonCount, prestigeCount, puzzleCount, restartCount;
	private int roomNumber, complexity = 6, rushTo = 18, aComplexity = 6, rComplexity = 1, fNumber, maxFloor = -1,
			trueMax = -1, dungStartLevel, totalLevelsGained;
	private int dungStart, dungGain, strStart, strGain, attStart, attGain, defStart, defGain, rngStart, rngGain,
			mgcStart, mgcGain, hitStart, hitGain, prayStart, prayGain, summStart, summGain;
	private int runeStart, runeGain, mineStart, mineGain, fireStart, fireGain, woodStart, woodGain, smthStart,
			smthGain, crftStart, crftGain, agilStart, agilGain, thevStart, thevGain, herbStart, herbGain, cnstStart,
			cnstGain, cookStart, cookGain, fishStart, fishGain, slayStart, slayGain;
	private int tokens, deaths, bossStage, bossId, cProg = -1, ceph, deph, seph, bossHp = 100, levelGoal;
	private final int defaultPitch = (int) (RANDOMIZED * 60);
	private int userRank;
	private int uncaughtLoops;
	private int currLoops;
	private int startHp = -1;
	private long fastestMillis = Integer.MAX_VALUE, slowestMillis = 0;

	private Case rotation = null;
	private Combat tempMode = Melee.NONE;
	private Door nearDoor, bossDoor, oldDoor;
	private Dungeoneering combatSpell;
	private Food fish;
	private Floor floor = Floor.ALL;
	private GroundItem nearItem;
	private Logs logs;
	private NPC nearMonster;
	private static Room targetRoom;
	private Room newRoom, currentRoom, startRoom, groupRoom, bossRoom, gateRoom;
	private Size floorSize = Size.SMALL;
	private String dir, settingsFile, extrasFile, partyFile, powerName, messageBreaker;
	private String controlStatus, secondaryStatus = "", status = "Welcome to iDungeon Pro " + VERSION + "!",
			settingStatus = "Hover over an option to find out more", warnStatus;
	private String fastestTime = EMPTY_TIMER, slowestTime = EMPTY_TIMER;
	private String armorType, bossName, failReason = "", lastMessage = "", strongestMonster, weaponType = "Unknown";
	private Style combatStyle = Style.MELEE, primaryStyle = Style.MELEE, protection, ringStyle;
	private Tile safeTile;
	private Timer bossTimer, breakTimer, doorTimer, dungTimer, failTimer = new Timer(Random.nextInt(360000, 480000)),
			generateTimer, idleTimer = new Timer(0), prayTimer, puzzleTimer, runTimer = new Timer(0);
	private Timer cameraTimer = new Timer(0), foodTimer, itemTimer = new Timer(1), fightTimer = new Timer(1),
			leaveTimer;

	@Override
	public void setup() {

		dir = Environment.getStorageDirectory() + File.separator + "BitchScripting" + File.separator;
		submit(new ThreadImages());
		username();
		for (int i = 0; i < ALPHABET.length; ++i)
			alphaInt[ALPHABET[i]] = i;
		powerName = Environment.getDisplayName();
		setSettingsFiles();
		developer = false;
		showPaint = true;
		setMouseDefault();

		log(LP, true, "Welcome to shitDungeon " + VERSION + ", " + powerName + "! Enjoy cakes firm body while you bot.");
		if (partyTabSelected() && partyMembers().size() > 0) {
			log(LB, true, "It's an iDungeon Party! "
					+ (isLeader ? "Members: " + partyMembers : "Leader: " + partyLeader));
			partyMode = true;
			Option.RUSH.set(false);
			// subscribe
			showOptions = false;
			showPartySettings = true;
			showStats = false;
			page = -1;
			status = "Choose your Party settings!";
			// enable chat
			loadPartySettings();
		}
		if (Game.isLoggedIn()) {
			threadPitch(100);
			shopClose();
			failWidgets();
			updateLevels();
			memberWorld = parse(Widgets.get(747, 7).getText()) > 0;
		}
		dungStartLevel = level(Skills.DUNGEONEERING);
		dungLevel = valueOf(dungStartLevel);
		levelGoal = dungLevel == 120 ? 200 : dungLevel + 1;
		if (developer)
			logD((memberWorld ? "Members" : "Free") + " world.");

		// ----
		try {
			String url = "http://bottingfrom.asia/others/dung/version.txt";
			URLConnection c;
			c = new URL(url).openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(c.getInputStream()));
			int rank = parse(in.readLine());
			int version = 25;
			if (rank > 0)
				if (version < rank) {
					log(LG, true, "New version out, go to http://ParisIsFrom.Asia.");
				}
			in.close();
		} catch (IOException ignored) {
		}
		provide(new Strat());
	}

	@Override
	public void onStop() {
		Context.get().b(1000);
		stopScript = true;
		showPaint = true;
		if (freeVersion) {
			if (dungeonCount > 1) {
				log(null, true, null);
				log(LP, true, "Thanks for using iDungeon Free " + VERSION + " by kiko & ShadowMoose");
				log(null, true, "If you've enjoyed the free version, be sure to check out iDungeon Pro");
				log(null, true, "in the Premium section of the repository for the full experience :)");
				log(null, true, null);
				log(Color.WHITE, true, "We completed " + dungeonCount + " dungeons for " + tokens + " tokens!");
			}
		} else if (liteVersion) {
			if (dungeonCount + controlledAborts > 2) {
				log(null, false, null);
				log(LP, true, "Thanks for using iDungeon Lite " + VERSION + " by kiko & ShadowMoose");
				log(null, true, "Trial expired! If you've enjoyed iDungeon, check out the full version");
				log(null, true, "in the Premium section of the repository for the full experience :)");
				log(null, true, null);
				log(Color.WHITE, true, "We completed " + dungeonCount + " dungeons for " + tokens + " tokens!");
				logout();
			}
		} else if (settingsFinished) {
			if (dungeonCount > 1) {
				log(LP, true, "Thanks for using iDungeon " + VERSION + " by kiko & ShadowMoose");
				Environment.saveScreenCapture();
			}
			saveExtras();
		}
	}

	private void enterDungeon() {
		int selComplexity = valueOf(complexity);
		updateLevels();
		status = "Entering Daemonheim...";
		if (lastMessage.contains("carrying items that cannot")) {
			if (invContains(GNOMEBALL))
				ridItem(GNOMEBALL, "Drop");
			else {
				severe("Please bank all items besides your Ring and restart the script!");
				stopScript();
				return;
			}
			Time.sleep(300, 600);
			lastMessage = "";
		} else if (Equipment.getAppearanceId(Slot.WEAPON) == GNOMEBALL || lastMessage.contains("wearing items")) {
			final Item wep = equipItem(Slot.WEAPON);
			if (wep != null && wep.getId() == GNOMEBALL) {
				log(LR, false, "Some fool tried to get us with a gnomeball!");
				if (!clickComponent(wep.getWidgetChild(), "Remove"))
					clickComponent(wep.getWidgetChild(), "Remove");
				Time.sleep(300, 600);
			}
			for (final Item eq : Equipment.getItems())
				if (eq != null && eq.getId() != -1)
					if (clickComponent(eq.getWidgetChild(), "Remove"))
						Time.sleep(300, 600);
			lastMessage = "";
		}
		if (!openPartyTab())
			Time.sleep(200, 400);
		else if (readyToPrestige())
			resetPrestige();
		else if (!partyFormed()) {
			final WidgetChild create = Widgets.get(939, 29);
			if (create != null && create.click(true))
				forVisible(Widgets.get(939, 59), true);
		} else if (partyMode && !partyJoined()) {
			log(LP, true, "Recreating the party");
			WidgetChild close = Widgets.get(947, 23);
			if (close != null && close.validate() && close.click(true))
				Time.sleep(300, 800);
			String badMember = null;
			o: for (int i = 0; i < 10; i++) {
				for (final String member : partyMembers)
					if (partyPlayer(member) != null)
						break o;
				secondaryStatus = "Waiting for other party members";
				if (pauseScript())
					return;
				Time.sleep(3000, 4000);
			}
			secondaryStatus = "";
			for (final String member : partyMembers) {
				if (partyMembers().contains(member))
					continue;
				int attempts = 0;
				close = Widgets.get(947, 23);
				if (close != null && close.validate() && close.click(true))
					Time.sleep(300, 800);
				log(LB, false, "Attempting to rejoin " + member + " to the party");
				idleTimer = new Timer(0);
				while (!partyMembers().contains(member)) {
					if (pauseScript())
						return;
					failWidgets();
					final Player m = partyPlayer(member);
					if (m != null) {
						if (lastMessage.contains("invitation to " + member)) {
							log(null, false, "Invited " + member + ". Waiting for a response");
							for (int i = 0; i < 10; i++) {
								if (partyMembers().contains(member)) {
									log(LGR, false, "Successfully rejoined " + member + " to the party!");
									break;
								}
								if (lastMessage.contains("has declined") || lastMessage.contains("has expired")) {
									log(LR, false, "Invitation to " + member + " declined, trying another");
									break;
								}
								Time.sleep(800, 1500);
							}
							lastMessage = "";
							continue;
						}
						if (Calculations.distanceTo(m) > 3)
							break;
						if (partyInteract(m, "Invite"))
							break;
					} else {
						if (attempts == 0)
							log(LR, false, "Unable to locate " + member + ", waiting to try again");
						Time.sleep(2000, 5000);
						++attempts;
					}
					if (attempts > 10 || idleTimer.getElapsed() > Random.nextInt(60000, 90000)) {
						badMember = member;
						break;
					}
					Time.sleep(300, 500);
				}
				Time.sleep(600, 1400);
			}
			if (badMember != null) {
				log.severe("Unable to invite " + badMember + " to the party. Removing him from the group.");
				partyMembers.remove(badMember);
				if (partyMembers.isEmpty()) {
					log.severe("No more party members remain. Continuing on soloing.");
					partySet(false);
				}
			} else if (partyJoined())
				log(LGR, false, "Looks like we've got everyone back!");
		} else if (Widgets.canContinue()) {
			final WidgetChild c = Widgets.getContinue();
			if (c != null && c.validate() && c.click(true))
				forSleep(c, false);
		} else {
			final Widget cWindow = Widgets.get(938);
			final Widget fWindow = Widgets.get(947);
			final Widget size = Widgets.get(1188);
			if (size.validate()) {
				if (size.getChild(0).getText().contains(" size")) {
					floorSize = !isRushing && Option.MEDIUM.enabled() && (!Option.RUSH.enabled() || fNumber > rushTo) ? Size.MEDIUM
							: Size.SMALL;
					clickDialogueOption(size, floorSize.name);
				} else if (developer && Option.FULL_SIZE.enabled() && partySize > 1)
					clickDialogueOption(size, "Yes", "5", "4", "3", "2");
				else
					clickDialogueOption(size, "Yes", "1");
			} else if (fWindow.validate()) {
				if (cProg == -1) {
					openPartyTab();
					if (cProg == -1) {
						Time.sleep(100, 200);
						return;
					}
				}
				if (fNumber < 1)
					fNumber = cProg + 1;
				if (fNumber > 48 && !Option.JOURNALS.enabled())
					fNumber = 48;
				final boolean skipScrolling = fNumber == trueMax;
				if (skipScrolling || scrollToFloor()) {
					final WidgetChild targ = fWindow.getChild(607 + fNumber);
					final WidgetChild number = fWindow.getChild(765);
					for (int d = 0; d < 10; ++d) {
						if (targ == null || !targ.validate())
							break;
						if (skipScrolling || parse(number.getText()) == fNumber)
							clickThrough(fWindow.getChild(761), "Confirm");
						else if (targ.interact("Select-floor"))
							for (int i = 0; i < 10; ++i) {
								if (!targ.validate())
									break;
								if (parse(number.getText()) == fNumber)
									break;
								Time.sleep(100, 150);
							}
						else
							Time.sleep(20, 200);
					}
				} else
					moveMouseRandomly();
				final WidgetChild selectedFloorNumber = fWindow.getChild(765);
				if (selectedFloorNumber != null && parse(selectedFloorNumber.getText()) == fNumber)
					if (fWindow.getChild(761).interact("Confirm") && !forSleep(cWindow, true))
						forSleep(cWindow, true);
			} else if (cWindow.validate()) {
				lastMessage = "";
				if (cProg == -1) {
					openPartyTab();
					if (cProg == -1) {
						Time.sleep(100, 200);
						return;
					}
				}
				selComplexity = valueOf(Option.RUSH.enabled() && cProg < rushTo ? rComplexity : complexity);
				final WidgetChild number = cWindow.getChild(42);
				while (number != null && number.validate()) {
					final int selectedC = parse(number.getText());
					if (selectedC < 1 || selectedC > 6)
						Time.sleep(100, 300);
					else if (selectedC != selComplexity) {
						final WidgetChild cButton = cWindow.getChild(51 + 5 * selComplexity);
						if (cButton != null && cButton.validate()) {
							if (cButton.getTextureId() == 2878)
								--selComplexity;
							else if (cButton.interact("Select-complexity"))
								for (int c = 0; c < 10; ++c) {
									if (parse(number.getText()) == selComplexity)
										break;
									Time.sleep(100, 150);
								}
						} else
							Time.sleep(100, 200);
					} else {
						aComplexity = selComplexity;
						clickThrough(cWindow.getChild(37), "Confirm");
					}
				}
			} else if (doObjAction(SceneEntities.getNearest(ENTRANCE), "Climb-down"))
				waitToStop(false);
			Time.sleep(400, 800);
		}
	}

	private void enterDungeonParty() {
		updateLevels();
		lastMessage = "";
		status = "Waiting to enter Daemonheim...";
		final Widget invite = Widgets.get(949);
		if (lastMessage.contains("carrying items that cannot")) {
			if (invContains(GNOMEBALL))
				ridItem(GNOMEBALL, "Drop");
			else {
				severe("Please bank all items besides your Ring and restart the script!");
				stopScript();
				return;
			}
			Time.sleep(300, 600);
			lastMessage = "";
		} else if (Equipment.getAppearanceId(Slot.WEAPON) == GNOMEBALL || lastMessage.contains("wearing items")) {
			final Item wep = equipItem(Slot.WEAPON);
			if (wep != null && wep.getId() == GNOMEBALL) {
				log(LR, false, "Some fool tried to get us with a gnomeball!");
				if (!clickComponent(wep.getWidgetChild(), "Remove"))
					clickComponent(wep.getWidgetChild(), "Remove");
				Time.sleep(300, 600);
			}
			for (final Item eq : Equipment.getItems())
				if (eq != null && eq.getId() != -1)
					if (clickComponent(eq.getWidgetChild(), "Remove"))
						Time.sleep(300, 600);
			lastMessage = "";
		} else if (openPartyTab() && partyFormed()) {
			if (readyToPrestige() && !resetPrestige())
				return;
		} else if (invite.validate()) {
			final WidgetChild accept = invite.getChild(65);
			final WidgetChild close = invite.getChild(22);
			final WidgetChild leader = invite.getChild(72);
			if (leader != null)
				if (leader != null && leader.getText().equalsIgnoreCase(partyLeader)) {
					log(LG, false, "Accepting " + partyLeader + "'s Party invitation!");
					if (accept.click(true))
						Time.sleep(600, 900);
				} else {
					log(LR, false, "Improper invite from " + leader.getText() + " (expected: " + partyLeader
							+ "). Closing screen.");
					close.click(true);
				}
		} else {
			final SceneObject entrance = SceneEntities.getNearest(ENTRANCE);
			if (Calculations.distanceTo(entrance) > 3) {
				if (doObjAction(entrance, "Climb-down"))
					waitToStop(false);
			} else if (partyMembers().size() == 0) {
				boolean clicked = false;
				final WidgetChild chatBox = Widgets.get(137, 57);
				for (final WidgetChild ch : chatBox.getChildren())
					if (ch != null && ch.validate() && ch.getText().contains(partyLeader + " has invited you"))
						if (chatBox.contains(ch.getAbsoluteLocation())
								&& (clicked = (ch.click(true) || ch.click(true)))) {
							Time.sleep(700, 1200);
							break;
						}
				if (!clicked) {
					openPartyTab();
					final Player leader = partyPlayer(partyLeader);
					if (leader != null && Calculations.distanceTo(leader) < 4)
						smartSleep((clicked = partyInteract(leader, "Invite")), true);
				}
			} else if (openPartyTab())
				if (readyToPrestige() && !resetPrestige())
					return;
		}
		Time.sleep(600, 1000);
	}

	private void exitDungeon() {
		status = "Exiting dungeon...";
		exit = true;
		idleTimer = new Timer(0);
		if (player().isInCombat())
			setPrayersOff();
		publish(MESSAGE_EXIT_FLOOR);
		while (Game.getPlane() != 1 && distTo(DAEMONHEIM) > 20) {
			if (pauseScript())
				return;
			failWidgets();
			if (inTrueCombat()) {
				setTargetRoom(getCurrentRoom());
				if (targetRoom != null && (bossRoom == null || !currentRoom.equals(bossRoom))
						&& NPCs.getNearest(guardians) != null)
					intentionallyBacktrack();
				else
					Time.sleep(300, 600);
			} else {
				final WidgetChild step1 = Widgets.get(1186, 7);
				final WidgetChild step2 = Widgets.get(1188, 3);
				if (step2 != null && step2.validate())
					clickThrough(step2, "Continue");
				else if (step1 != null && step1.validate())
					spaceContinue();
				else {
					getCurrentRoom();
					if (openPartyTab() && clickComponent(Widgets.get(939, 34), "Leave-party"))
						forSleep(Widgets.get(1186, 3), true);
				}
			}
			Time.sleep(100, 200);
		}
		if (!isLeader && leaveTimer != null)
			abortedCount++;
		aborted = false;
		floor = Floor.ALL;
		inDungeon = false;
		newDungeon = false;
	}

	private void exploreRoom() {
		if (targetRoom == null)
			return;
		if (roomNumber == 1) {
			pickUpAll();
			open = true;
			return;
		}
		if (targetRoom.equals(bossRoom)) {
			unBacktrack();
			retrace = false;
			bossFight = true;
			return;
		}
		final boolean unexplored = newRoom != null;
		newRoom = null;
		if (unexplored)
			protection = null;
		if (floor == Floor.OCCULT)
			spawnRoom = getNpcInRoom(NECROMANCERS) != null;
		else if (floor == Floor.WARPED)
			spawnRoom = getNpcInRoom("Reborn mage") != null;
		else
			spawnRoom = false;
		if (rooms.size() > 1)
			status = "Exploring " + (unexplored ? "a new" : "an old") + " room";
		int puzzleCheck = 0;
		if (generateTimer != null && !isNpcInRoom()) {
			while (generateTimer != null && generateTimer.isRunning()) {
				if (failSafe())
					return;
				if (isNpcInRoom())
					break;
				Time.sleep(50, 100);
			}
			generateTimer = null;
		}
		getCurrentRoom();
		if (unexplored)
			updateDoors();
		targetRoom.hooded = shadowHooded && !targetRoom.hasDoor(DoorType.GUARDIAN)
				&& getNpcInRoom(HOODED_DEACTIVATORS) == null;
		if (aComplexity > 4 && !targetRoom.puzzlesFinished && targetRoom.hasDoor(State.NEW, State.CLEARED)) {
			puzzleSolved = false;
			puzzleRepeat = false;
			roomSwitch = false;
			puzzleCheck = checkPuzzles();
			puzzlePoints.clear();
			if (puzzleCheck != 2 && inRoom(startRoom) && failSafe())
				puzzleCheck = 2;
			if (puzzleCheck == 2 && secondaryStatus.startsWith("Attempting to"))
				puzzleCheck = -1;
			nearItem = null;
			nearMonster = null;
			final String puzzleName = status.contains("Puzzle room: ") ? status.substring(status.indexOf(":") + 2)
					: "Unknown puzzle";
			if (puzzleCheck == 1 || puzzleSolved) {
				if (puzzleTimer != null && puzzleTimer.getElapsed() > 1000) {
					log(LG, false, "Puzzle room: " + puzzleName + " completed in " + puzzleTimer.toElapsedString()
							+ "!");
					++puzzleCount;
				}
				safeTile = null;
				targetRoom.hasPuzzle = false;
				targetRoom.state = State.CLEARED;
				failTimer = new Timer(Random.nextInt(360000, 480000));
				idleTimer = new Timer(0);
				deathPath.clear();
			} else if (puzzleCheck == 2) {
				if (exit)
					return;
				if (puzzleTimer != null && (!puzzleTimer.isRunning() || status.contains("reengage"))) {
					severe("Took too long to solve Puzzle: " + puzzleName + " ("
							+ (puzzleTimer != null ? puzzleTimer.toElapsedString() : "unknown time") + ")");
					bugReport(true);
					retrace = true;
					tempMode = Melee.NONE;
					finishRoom(targetRoom);
					skipRoom = true;
					puzzleTimer = null;
					deathPath.clear();
					teleHome(false);
					secondaryStatus = "";
				} else
					open = true;
				safeTile = null;
				failTimer = new Timer(Random.nextInt(360000, 480000));
				idleTimer = new Timer(0);
				return;
			} else if (puzzleCheck == -1) {
				if (knownReason())
					log(null, false, "Unable to complete Puzzle: " + puzzleName + "; Reason: " + failReason + ".");
				else {
					log(LR, false, "Unable to complete Puzzle: " + puzzleName + "; Reason: " + failReason + ".");
					bugReport(true);
				}
				safeTile = null;
				retrace = true;
				tempMode = Melee.NONE;
				finishRoom(targetRoom);
				skipRoom = true;
				failTimer = new Timer(Random.nextInt(360000, 480000));
				idleTimer = new Timer(0);
				deathPath.clear();
			}
			if (!puzzleRepeat)
				targetRoom.puzzlesFinished = true;
			puzzleTimer = null;
		}
		if (puzzleCheck != -1) {
			setRetaliateDefault();
			if (unexplored && !slayerCheck()) {
				final ArrayList<Door> gDoors = targetRoom.getDoors(DoorType.GUARDIAN);
				if (gDoors.size() > 0) {
					log(null, false, "Unkillable slayer monster in this room, removing Guardian door");
					for (final Door d : gDoors)
						d.state = State.FINISHED;
					if (!targetRoom.hasDoor(State.NEW))
						targetRoom.state = State.FINISHED;
				}
				targetRoom.isSkippable = true;
			}
			if (targetRoom.isSkippable)
				skipRoom = true;
			else if (unexplored && (isRushing || skipRoom || Option.SKIP.enabled()))
				if (targetRoom.hasDoor(DoorType.GUARDIAN)) {
					if (NPCs.getNearest(attackable) != null) {
						if (developer && skipRoom)
							log(null, false, "We can't skip through this room!");
						skipRoom = false;
					} else
						targetRoom.isSkippable = true;
				} else if (!skipRoom) {
					targetRoom.isSkippable = true;
					skipRoom = true;
				}
			if (skipRoom) {
				if (!pickUpAll())
					return;
			} else {
				if (!fightMonsters() || !pickUpAll())
					return;
				if (unexplored && developer && Option.JOURNALS.enabled() && !sagaMemories())
					return;
				eatFood(foods, 42, 49);
				buryBones();
				if (Option.RANGE.enabled() && invCount(true, arrows) > (random(6, 40)))
					doItemAction(invItem(arrows), "Wield");
				if (!pickUpAll())
					return;
			}
		}
		if (puzzleCheck < 1)
			if (puzzleCheck == -1 || !targetRoom.hasDoor(State.NEW, State.CLEARED))
				finishRoom(targetRoom);
			else
				targetRoom.state = State.CLEARED;
		if (failSafe())
			return;
		secondaryStatus = "";
		if (targetRoom.state == State.FINISHED)
			retrace = true;
		else {
			if (unexplored)
				updateDoors();
			open = true;
		}
	}

	private void finishDungeon() {
		if (partyMode)
			publish(MESSAGE_BOSS_FINISHED);
		else if (!inRoom(bossRoom)) {
			open = true;
			return;
		}
		roomSwitch = false;
		bossFinished = true;
		status = "Completing the dungeon...";
		SceneObject finishedLadder = SceneEntities.getNearest(FINISHED_LADDERS);
		if (finishedLadder == null)
			return;
		safeTile = finishedLadder.getLocation();
		log(Color.WHITE, false, "Exit found" + (dungTimer != null ? " in " + dungTimer.toElapsedString() : "")
				+ ", completing the dungeon.");
		if (cooperativeParty) {
			// pub("fin", "true");
		}
		bossTimer = null;
		failTimer.reset();
		final int startExp = Skills.getExperience(Skills.DUNGEONEERING);
		while (!Widgets.get(933).validate()) {
			if (failSafe())
				return;
			if (lastMessage.startsWith("You have already") && idleTimer.getElapsed() > Random.nextInt(45000, 60000)) {
				severe("The exit ladder is glitched, unable to finish this dungeon");
				exit = true;
				return;
			}
			if (Option.JOURNALS.enabled()) {
				GroundItem notes = getItemInRoom(bossRoom, JOURNAL_MISC);
				if (notes == null) {
					notes = getItemInRoom(bossRoom, JOURNAL_CHRONICLES);
					if (notes == null)
						notes = getItemInRoom(bossRoom, JOURNAL_NOTES);
				}
				if (notes != null) {
					smartSleep(pickUpItem(notes), false);
					continue;
				}
			}
			improveBossWeapon();
			improveWeaponBinding();
			if (Widgets.get(1188, 1).validate()) {
				if (Widgets.get(1188, 1).click(true))
					if (forSleep(Widgets.get(933), true) || forSleep(Widgets.get(933), true))
						break;
			} else if (!partyMode || !roomSwitch)
				if (afking && atStartRoom())
					smartSleep(telePortal(), true);
				else if ((finishedLadder = SceneEntities.getNearest(FINISHED_LADDERS)) != null) {
					if (distTo(finishedLadder) < 3 && !adjacentTo(finishedLadder))
						walkTo(finishedLadder, 0);
					if (smartSleep(doObjAction(finishedLadder, "End-dungeon"), true))
						if (!forSleep(Widgets.get(1188), true))
							if (!moving() && distTo(finishedLadder) > 2)
								walkTo(finishedLadder, 1);
				}
			Time.sleep(100, 200);
		}
		currentRoom = null;
		targetRoom = null;
		status = "Waiting for the next dungeon...";
		++dungeonCount;
		++fNumber;
		++cProg;
		if (dungTimer != null) {
			final long dungeonTime = dungTimer.getElapsed();
			if (dungeonTime < fastestMillis) {
				if (slowestMillis == 0 && fastestMillis != Integer.MAX_VALUE) {
					final long millis = fastestMillis;
					final String time = fastestTime;
					slowestMillis = millis;
					slowestTime = time;
				}
				fastestMillis = dungeonTime;
				fastestTime = dungTimer.toElapsedString();
			} else if (dungeonTime > slowestMillis) {
				slowestMillis = dungeonTime;
				slowestTime = dungTimer.toElapsedString();
			}
		}
		tpf = Math.rint(runTimer.getElapsed() * 10 / dungeonCount / 60000) / 10;
		clearAll();
		final Widget notice = Widgets.get(519);
		if ((partyMode && isLeader && cProg > 0 && maxFloor > 0 && cProg >= maxFloor)
				|| (notice.validate() && notice.getText().contains("not available at"))) {
			String prestigeMsg = "Last floor reached (" + cProg + "/" + maxFloor + "), ";
			if (cProg > 0 && cProg >= maxFloor - 1) {
				forcePrestige = true;
				prestigeMsg += "prestiging!";
			} else
				prestigeMsg += "exiting to a new floor.";
			log(LP, false, prestigeMsg);
			exit = true;
		} else
			threadDefault();
		boolean clicked = false;
		int tokensGained = 0;
		Widget finishWindow;
		o: while ((finishWindow = Widgets.get(933)).validate()) {
			if (failBasic())
				break;
			if (!clicked && clickComponent(finishWindow.getChild(13), ""))
				Time.sleep(200, 300);
			if (clickComponent(finishWindow.getChild(322 - partyIndex), "")) {
				Time.sleep(300, 500);
				if (!scoreSubmitted) {
					final String tokenString = finishWindow.getChild(41).getText().replace("%", "");
					tokensGained = parse(tokenString);
					tokens += tokensGained;
					if (startExp > 0 && tokensGained > 0 && tokensGained < 10000) {
						scoreSubmitted = true;
					}
				}
				clicked = true;
				for (int c = 0; c < 40; ++c) {
					if (!finishWindow.validate())
						break o;
					Time.sleep(200, 300);
				}
			}
			Time.sleep(100, 200);
		}
		failSafe();
	}

	private boolean openNextDoor() {
		setPrayersOff();
		if (roomNumber == 1) {
			if (relog) {
				secondaryStatus = "Logging out to recharge our blastbox";
				logInnOut();
				secondaryStatus = "";
				if (failLogin())
					return false;
				relog = false;
			}
			buryBones();
			if (getFeathers) {
				shopQuickly("Feather", 50);
				getFeathers = false;
			}
			if (memberWorld && (bossRoom == null || disableBossPath) && floor != Floor.FROZEN
					&& !invContains(ANTIPOISON) && invSaleAfter() < random(26, 28)) {
				final int coinCount = invCount(true, COINS);
				if (coinCount > 199) {
					int poisonCount = coinCount / 200;
					if (poisonCount < 4)
						poisonCount = 5;
					else if (poisonCount > Random.nextInt(1, 3))
						poisonCount = floor == Floor.ABANDONED && rooms.size() == 1 ? Random.nextInt(2, 4) : 2;
					shopQuickly("Antipoison", poisonCount);
				}
			}
			unPoison();
			if (readyToCraft()) {
				if (invCount(true, ESSENCE) < 10)
					shop(ESSENCE, "Rune essence", 10, 50);
				shopClose();
				craftCosmics();
			}
			if (!bossFinished && rooms.size() > 2)
				if (readyToCook()) {
					cookReady = false;
					failTimer = new Timer(Random.nextInt(360000, 480000));
					prepareFood();
				}
			shopClose();
			makeSpace(true);
		}
		if (roomNumber == 1) {
			if (bossRoom != null && !disableBossPath && planToTele() && !isStart(gateRoom))
				makeGatestone(true);
		} else if (!rechargePrayer())
			return false;
		nearDoor = null;
		newRoom = null;
		finishOffRooms();
		updateLocks();
		if (getCurrentRoom() != null) {
			lastMessage = "";
			if (roomNumber == 1 && aComplexity > 4)
				if (getItemInRoom(startRoom, GGS) != null)
					while (getItemInRoom(startRoom, GGS) != null)
						smartSleep(pickUpItem(getItemInRoom(startRoom, GGS)), false);
				else if (rooms.size() > 1 && !invContains(GGS)) {
					if (exit)
						return false;
					switchRing(combatStyle);
					status = "Teleporting back to the groupstone";
					if (!isBoss("Skeletal Hoarde"))
						omNomNom();
					boolean blockedDest = false;
					while (!atTargetRoom()) {
						if (failSafe())
							return false;
						if (lastMessage.contains("destination") || lastMessage.contains("blocking you")) {
							blockedDest = true;
							break;
						}
						if (player().getAnimation() == -1 && telePortal())
							waitToStop(false);
						Time.sleep(300, 400);
					}
					if (!blockedDest) {
						setTargetRoom(getCurrentRoom());
						groupRoom = null;
						if (!isStart(targetRoom)) {
							deathPath.clear();
							if (isLeader && (targetRoom.hasPuzzle || isBoss("Sagittare", "Stomp", "Kal'Ger")))
								for (int c = 0; c < 8; ++c) {
									if (invContains(GGS) || invFull())
										break;
									smartSleep(pickUpItem(getItemInRoom(targetRoom, GGS)), false);
								}
							explore = true;
							return true;
						}
					}
				}
			if (disableBossPath && !isGoodDoor()) {
				if (developer)
					log(Color.WHITE, false, "Remaining openable rooms explored, returning to the boss!");
				disableBossPath = false;
				if (!targetRoom.contains(bossPath))
					return teleHome(false);
				if (goHome())
					return teleHome(true);
				if (teleGatestone())
					return true;
			}
			setTargetRoom(getCurrentRoom());
			if (!disableBossPath && bossRoom != null) {
				if (gateRoom != null && !inRoom(gateRoom) && gateRoom.contains(bossDoor))
					if (teleGatestone())
						return true;
				for (final Door door : bossPath)
					if (targetRoom.contains(door)) {
						if (door.state == State.FINISHED) {
							severe("The boss path is blocked by a finished door, aborting dungeon.");
							aborted = true;
							++abortedCount;
							exit = true;
							return false;
						}
						nearDoor = door;
						idleTimer = new Timer(0);
						if (crossTheChasm(reachDoor()) == 2)
							return false;
						doorTimer = new Timer(0);
						status = "Opening a door back to the Boss";
						if (bossDoor != null && nearDoor.equals(bossDoor)) {
							walkToDoor(1);
							waitToEat(false);
							if ((!skipRoom || !inTrueCombat())
									&& (floor != Floor.ABANDONED || getNpcInRoom(SKINWEAVER) == null))
								omNomNom();
							setTargetRoom(bossRoom);
							if (prepareSupplies())
								return true;
							if (!prepareForBoss())
								return false;
							setTargetRoom(getCurrentRoom());
						}
						return openOldDoor();
					}
			}
			if (groupRoom != null && !deathPath.isEmpty()) {
				if (!targetRoom.equals(groupRoom))
					for (final Door door : deathPath)
						if (targetRoom.contains(door)) {
							if (door.state == State.FINISHED)
								break;
							nearDoor = door;
							idleTimer = new Timer(0);
							if (crossTheChasm(reachDoor()) == 2)
								return false;
							doorTimer = new Timer(0);
							status = "Opening a door back to where we died";
							return openOldDoor();
						}
				deathPath.clear();
			}
			if (cooperativeParty) {
				if (isLeader) {
					getBestDoor();
					if (nearDoor != null) {
						// pub("door", nearDoor.loc);
					} else {
						// pub("door", "none");
					}
				} else
					while (nearDoor == null)
						Time.sleep(50, 500);
				final String[] members = isLeader ? partyMembers.toArray(new String[partyMembers.size()])
						: new String[] { partyLeader };
				boolean found = false;
				for (int i = 0; i < 20; i++) {
					boolean missing = false;
					for (final String p : members)
						if (!partyPlayerIn(targetRoom, p)) {
							missing = true;
							break;
						}
					if (!missing) {
						found = true;
						break;
					}
					status = "Waiting for the rest of the party";
					Time.sleep(500, 1000);
				}
				if (found) {
					if (!isLeader) {
						status = "Waiting for the next door selection";
						boolean aligned = false;
						final Player leader = partyPlayer(partyLeader);
						while (!aligned) {
							if (leader == null || !leader.validate() || failSafe())
								return false;
							Time.sleep(100, 200);
							if (!leader.isMoving()) {
								final Tile t = leader.getLocation();
								for (final Tile n : targetRoom.getBlockNodes())
									if (Calculations.distance(t, n) < 2) {
										aligned = true;
										break;
									}
							}
						}
					}
				} else
					log(LR, true, "Unable to locate the rest of the party, exploring off on our own!");
			} else
				getBestDoor();
			if (nearDoor != null) {
				doorTimer = new Timer(0);
				idleTimer = new Timer(0);
				crossTheChasm(reachDoor());
				lastMessage = "";
				if (distTo(nearDoor.loc) > 4)
					walkToDoor(1);
				if (cooperativeParty && isLeader)
					while (Calculations.distanceTo(nearDoor.block) > 1) {
						if (failSafe())
							return false;
						if (!moving())
							walkTo(nearDoor.loc, 1);
						else
							Time.sleep(200, 400);
					}
				if (randomActivate(2))
					threadCustomPitch();
				if (nearDoor.state == State.CLEARED)
					return openOldDoor();
				switch (nearDoor.type) {
				case BLOCKED:
					return openBlockedDoor();
				case KEY:
					return openKeyDoor();
				case SKILL:
					return openSkillDoor();
				default:
					return openBasicDoor();
				}
			}
		}
		return false;
	}

	private void retraceDungeon() {
		if (inRoom(bossRoom)) {
			bossFight = true;
			return;
		}
		updateLocks();
		if (disableBossPath && !isGoodDoor()) {
			log(Color.WHITE, false, "Remaining openable rooms explored, returning to the boss!");
			disableBossPath = false;
			if (teleGatestone()) {
				explore = true;
				return;
			}
		}
		boolean teleBack = false;
		++restartCount;
		finishOffRooms();
		if (bossRoom != null && getObjInRoom(FINISHED_LADDERS) != null) {
			finish = true;
			return;
		}
		setPrayersOff();
		if (targetRoom != null && NPCs.getNearest(guardians) == null && !pickUpAll())
			return;
		if (gateRoom != null) {
			updateLocks();
			if (gateRoom.hasGoodDoor()) {
				log(LB, false, "Gatestone teleporting to the next good door");
				if (teleGatestone()) {
					open = true;
					return;
				}
			}
		}
		if (currentRoom == null)
			return;
		if (restartCount > 2) {
			if (failSafe())
				return;
			if (roomNumber == 1 && !deathPath.isEmpty()) {
				setTargetRoom(startRoom);
				deathPath.clear();
			}
			if (!isGoodDoor()) {
				if (bossRoom == null) {
					if (skipDoorFound) {
						severe("A skipped skill type is preventing our progress, exiting to a new floor.");
						++controlledAborts;
					} else if (getKey() != null) {
						severe("Damn, looks like we missed a key somewhere, exiting to a new floor.");
						++abortedCount;
					} else if (SceneEntities.getNearest(4321) != null) {
						severe("Blocked tile found, unable to progress on this floor");
						++controlledAborts;
					} else {
						severe("No good doors remain.. Aborting dungeon");
						++abortedCount;
						waitForResponse();
					}
					exit = true;
				} else {
					disableBossPath = false;
					oldDoor = null;
					open = true;
				}
				return;
			}
			disableBossPath = false;
			if (!monsterBacktrack())
				return;
			restartCount = 0;
			if (roomNumber == 1) {
				oldDoor = null;
				open = true;
				return;
			}
			teleBack = true;
		} else if (currentRoom.isUnbacktrackable)
			teleBack = true;
		if (rooms.size() == 1)
			return;
		status = "Backtracking through the dungeon";
		getCurrentRoom();
		if (gateRoom == null && roomNumber != 1 && inRoom(targetRoom) && currentRoom.state != State.FINISHED)
			if (castable(Dungeoneering.CREATE_GATESTONE)) {
				for (final Door d : targetRoom.doors)
					if (!d.loc.isOnScreen()) {
						walkFast(d.loc, true, 1);
						makeSpace(true);
						break;
					}
				makeGatestone(false);
			}
		nearDoor = null;
		Tile backDoor = null;
		boolean cantTele = false;
		if (NPCs.getNearest(guardians) != null)
			if (targetRoom.isUnbacktrackable) {
				if (!fightMonsters() || !pickUpAll())
					return;
			} else {
				cantTele = true;
				teleBack = false;
			}
		if (targetRoom != null && !teleBack) {
			final Tile back = getBackDoor(targetRoom);
			if (back != null)
				if (cantTele || (skipRoom && NPCs.getNearest(guardians) != null))
					backDoor = back;
				else {
					final Room r = targetRoom.parent;
					if (r != null
							&& (isStart(r) || r.hasGoodDoor() || r.contains(generateGoodPaths()) || (!disableBossPath && r
									.contains(bossPath))))
						backDoor = back;
				}
		}
		if (roomNumber != 1) {
			crossTheChasm(backDoor);
			final Timer dTimer = new Timer(Random.nextInt(30000, 45000));
			o: while (targetRoom.equals(getCurrentRoom()) && roomNumber != 1) {
				if (failSafe())
					break;
				if (backDoor == null) {
					if (isLeader && !invContains(GGS)) {
						makeSpace(true);
						final GroundItem ggs = GroundItems.getNearest(GGS);
						if (ggs != null && currentRoom.contains(ggs) && (distTo(ggs) < 5 || reachable(ggs, true))) {
							smartSleep(pickUpItem(ggs), false);
							continue;
						}
					}
					if (NPCs.getNearest(guardians) != null) {
						if (NPCs.getNearest(fightable) == null) {
							log(null, false, "Bad monster found, backtracking.");
							backDoor = getBackDoor();
						} else {
							log(null, false, "Finishing off monsters before teleporting home");
							setRetaliate(true);
							if (!fightMonsters() || !pickUpAll())
								return;
						}
					} else
						teleHome(false);
				} else {
					final SceneObject door = SceneEntities.getAt(backDoor);
					if (door != null && dTimer.isRunning()) {
						unBacktrack();
						if (doObjAction(door, "Enter")) {
							if (!isAutoRetaliateEnabled())
								open(Tabs.ATTACK);
							waitToStart(false);
							while (moving() && inRoom(targetRoom)) {
								if (player().getInteracting() != null)
									continue o;
								Time.sleep(100, 200);
							}
							Time.sleep(300, 500);
						}
					} else
						backDoor = null;
				}
				Time.sleep(100, 200);
			}
		} else if (bossRoom == null) {
			updateLocks();
			if (!startRoom.hasDoor(State.NEW, State.CLEARED)) {
				if (skipDoorFound) {
					log(LR, false,
							"A skipped door type is preventing us from solving this dungeon, exiting to try another.");
					++controlledAborts;
				} else if (getKey() != null) {
					severe("Damn, looks like we missed a key somewhere, prevening our progress on this floor.");
					++abortedCount;
				} else if (SceneEntities.getNearest(4321) != null) {
					log(LR, false, "Blocked tile found, unable to progress on this floor");
					++controlledAborts;
				} else {
					severe("No good doors remain.. Aborting dungeon");
					++abortedCount;
					waitForResponse();
				}
				exit = true;
				return;
			}
		}
		if (NPCs.getNearest(guardians) == null)
			setRetaliateDefault();
		setTargetRoom(currentRoom);
		explore = true;
		skipRoom = false;
	}

	private void startDungeon() {
		if (stopAtNext || (liteVersion)) {
			stopScript();
			return;
		}
		int c = 0;
		SceneObject exitLadder;
		status = "Beginning a new dungeon";
		idleTimer = new Timer(0);
		newDungeon = false;
		scoreSubmitted = false;
		while ((exitLadder = SceneEntities.getNearest(EXIT_LADDERS)) == null || !inDungeon()) {
			if (failBasic() || Game.getPlane() == 1)
				return;
			++c;
			if (inDungeon()) {
				if (c > 3 && SceneEntities.getLoaded().length > 300)
					break;
				if (c == 11)
					log(null, false, "Exit ladder is null!");
			}
			if (c > 10) {
				inDungeon = false;
				return;
			}
			Time.sleep(300, 600);
		}
		if (exit)
			return;
		log(null, false, null);
		log(Color.WHITE, false, "Starting a new dungeon");
		clearAll();
		if (primaryWep > 0 && doItemAction(invItem(primaryWep), "Wield"))
			itemTimer = new Timer(3000);
		Walking.setRun(true);
		while (cProg == -1 || Game.getClientState() != Game.INDEX_MAP_LOADED || !inDungeon()
				|| (settingsFinished && NPCs.getNearest(SMUGGLER) == null) || !getStartRoom()
				|| (NPCs.getNearest(SMUGGLER) == null && SceneEntities.getLoaded().length < 300)) {
			if (failSafe() || Game.getPlane() == 1)
				return;
			if (cProg == -1)
				openPartyTab();
			Time.sleep(500, 700);
		}
		if (cProg > 0 && maxFloor > 0) {
			if (cProg >= maxFloor) {
				log(LB, true, "Last floor reached (" + cProg + "/" + maxFloor + "), prestiging!");
				exit = true;
				return;
			}
			if (cProg > 52 && fNumber > 53 && maxFloor > 54 && !Option.JOURNALS.enabled()) {
				log(null, false, "Returning to a lower Warped floor to encounter easier bosses!");
				exit = true;
				return;
			}
		}
		switchRing(primaryStyle);
		attackMode = valueOf(defaultMode);
		if (exitLadder != null)
			switch (exitLadder.getId()) {
			case 51156:
				floor = Floor.FROZEN;
				if (Option.STYLE_SWAP.enabled())
					if (Melee.CRUSH.enabled())
						attackMode = Melee.CRUSH.index();
				break;
			case 50604:
				floor = Floor.ABANDONED;
				if (isLeader && combatLevel < 15) {
					severe("Abandoned floor bosses are too strong to take on at this combat level");
					openPartyTab();
					if (cProg > 10)
						forcePrestige = true;
					exit = true;
					return;
				}
				if (Option.STYLE_SWAP.enabled())
					if (Melee.SLASH.enabled())
						attackMode = Melee.SLASH.index();
					else if (Melee.STAB.enabled())
						attackMode = Melee.STAB.index();
				break;
			case 51704:
				floor = Floor.FURNISHED;
				if (isLeader && combatLevel < 40) {
					severe("Furnished floor bosses are too strong to take on at this combat level");
					openPartyTab();
					if (cProg > 16)
						forcePrestige = true;
					exit = true;
					return;
				}
				if (Option.STYLE_SWAP.enabled())
					if (Melee.SLASH.enabled())
						attackMode = Melee.SLASH.index();
					else if (Melee.CRUSH.enabled())
						attackMode = Melee.CRUSH.index();
				break;
			case 54675:
				floor = Floor.OCCULT;
				if (Option.STYLE_SWAP.enabled())
					if (Melee.SLASH.enabled())
						attackMode = Melee.SLASH.index();
					else if (Melee.STAB.enabled())
						attackMode = Melee.STAB.index();
				break;
			case 56149:
				floor = Floor.WARPED;
				if (Option.STYLE_SWAP.enabled())
					if (Melee.SLASH.enabled())
						attackMode = Melee.SLASH.index();
				break;
			default:
				severe("Unable to detect the floor type, returning to a lower floor.");
				exit = true;
				return;
			}
		if (pauseScript())
			return;
		if (!runSettings())
			return;
		idleTimer = new Timer(0);
		dungTimer = new Timer(0);
		if (afking)
			return;
		if (Option.RUSH.enabled()) {
			if (cProg < rushTo) {
				isRushing = true;
				if (dungLevel > 5 && aComplexity != rComplexity) {
					severe("Rushing enabled, exiting to update to your rush Complexity.");
					exit = true;
				}
			} else {
				isRushing = false;
				if (Option.NO_PRESTIGE.enabled()) {
					severe("Last rush floor reached with Prestiging disabled! Shutting down script..");
					stopScript();
				} else if (aComplexity != complexity) {
					log(LP, true, "Last rush floor reached, exiting to change to your main Complexity.");
					exit = true;
				} else if (Option.MEDIUM.enabled() && floorSize != Size.MEDIUM) {
					log(LP, true, "Last rush floor reached, exiting to update to the main floor size.");
					exit = true;
				}
			}
			if (exit)
				return;
		}
		if (developer)
			logD(status + " - C" + aComplexity + " " + floor);
		getNewDoors();
		if (skipDoorFound) {
			log(LR, false, "There's a skipped door type in this start room, trying another dungeon.");
			exit = true;
			return;
		}
		open(Tabs.INVENTORY);
		updateDoors();
		exploreRoom();
		if (failSafe())
			return;
		setRetaliateDefault();
		updateDoors();
		if (Option.SECONDARY_RANGE.enabled() && (arrows < 1 || secondaryWep < 1) && floor == Floor.OCCULT) {
			if (arrows < 1) {
				final int arrowId = getItemId(" arrows");
				if (arrowId > 0)
					arrows = arrowId;
			}
			if (arrows > 0 && secondaryWep < 1) {
				final int bowId = getItemId("longbow") > 0 ? getItemId("longbow") : getItemId("shortbow");
				if (bowId > 0)
					secondaryWep = bowId;
			}
			if (arrows > 0 && secondaryWep > 0) {
				final String arrName = getName(invItem(arrows));
				log(LB, false,
						"Enabling secondary Ranged with a " + getName(invItem(secondaryWep))
								+ (!arrName.isEmpty() ? "+" + arrName : "") + " for this floor.");
				Style.RANGE.set(true);
				Style.RANGE.temporary = true;
				ridItem(arrows, "Wield");
			}
		}
		if (Option.SECONDARY_RANGE.enabled() && secondaryWep < 1 && arrows > 0 && hasCoins(562)
				&& floor == Floor.OCCULT) {
			shop(16317, "Tangle gum longbow", 1, 562);
			final int bowId = getItemId("longbow") > 0 ? getItemId("longbow") : getItemId("shortbow");
			if (bowId > 0)
				secondaryWep = bowId;
			if (secondaryWep > 0) {
				log(LB, false, "Enabling secondary Ranging with a " + getName(Inventory.getItem(secondaryWep))
						+ " and " + getName(invItem(arrows)) + " for this floor.");
				Style.RANGE.set(true);
				ridItem(arrows, "Wield");
			}
		}
		threadCustomPitch();
		ridItem(primaryWep, "Wield");
	}

	private final int[] ACTIVE_STONES = { 49278, 49279, 49276, 49277, 49274, 49275 };
	private final int[] BLUE_STONES = { 49278, 49279 }, GREEN_STONES = { 49276, 49277 }, RED_STONES = { 49274, 49275 };
	private final int[][] ALL_STONES = { BLUE_STONES, GREEN_STONES, RED_STONES };
	private final int[] STOMP_LODESTONES = { 49270, 49271, 49278, 49279, 49276, 49277, 49274, 49275 };
	private final int[] BLINK_PILLARS = { 32184, 32196, 32202, 32195, 32201, 32231 };
	private final int[] GRAVE_PADS = { 54444, 54450, 54458 };
	private final int[] K_SPEC_ANIMS = { 13426, 13427, 13428, 13429 }, K_START_ANIMS = { 13427, 13429 };
	private final int[] RAISED_PILLARS = { 32184, 32196, 32202 }, SUNKEN_PILLARS = { 32195, 32201, 32231 };
	private final int[] RUNEBOUND_ACTIVES = { 53977, 53979, 53978 };
	private final int[] TUNNELS = { 49286, 49287, 49288 };
	private final int[] STOMP_CRYSTALS = { 15750, 15751, 15752 };

	private final int BEHEMOTH_CARCASS = 49283, BEHEMOTH_WALL = 51180, SAGITTARE_FLOOR = 51887,
			STOMP_BLOCK_ROCKS = 49268, NECROLORD_FLOOR = 54475;

	private NPC bossSet(NPC boss) {
		if (boss == null || getId(boss) == -1) {
			boss = getNpcInRoom(bossName);
			if (boss == null) {
				if (isBoss("Gravecreeper"))
					topUp();
				else if (!moving() && inRoom(bossRoom)) {
					final Tile center = bossRoom.getCenter();
					if (distTo(center) > random(2, 4))
						walkTo(center, 6);
				}
				Time.sleep(100, 300);
			}
		}
		return boss;
	}

	private void frozenBoss() {
		if (isBoss("Gluttonous") || getObjInRoom(BEHEMOTH_CARCASS) != null) {
			setBoss("Gluttonous Behemoth", true, 1);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			setRetaliate(true);
			final SceneObject carcass = getObjInRoom(BEHEMOTH_CARCASS);
			NPC behemoth = getNpcInRoom(bossName);
			if (carcass != null) {
				safeTile = carcass.getLocation();
				final Tile wall = getNearestObjTo(safeTile, BEHEMOTH_WALL);
				if (wall != null)
					for (final Tile t : getAdjacentTo(safeTile)) {
						final int wDist = (int) Calculations.distance(t, wall);
						if (wDist == 3) {
							safeTile = t;
							break;
						}
					}
			}
			walkFast(safeTile, true, 0);
			setPrayer(true, Style.RANGE, true);
			updateFightMode();
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((behemoth = bossSet(behemoth)) == null)
					continue;
				if (behemoth.getAnimation() == 13720) {
					secondaryStatus = "Waiting for the behemoth to eat";
					while (behemoth.getAnimation() == 13720) {
						topUp();
						Time.sleep(300, 500);
					}
					secondaryStatus = "";
				}
				if (distTo(safeTile) > 1.5 && !isDead(behemoth)) {
					secondaryStatus = "Repositioning to guard the food";
					if (!moving())
						walkTo(safeTile, 0);
					waitToStop(false);
					attackBoss(behemoth, true);
					secondaryStatus = "";
				} else if (!moving())
					attackBoss(behemoth, true);
				eatFood(topFoods, 50, 60);
				Time.sleep(200, 300);
			}
		} else if (isBoss("Frostweb") || getNpcInRoom("Frostweb") != null) {
			setBoss("Astea Frostweb", false, 2);
			boolean invincible = false;
			setPrayer(true, Style.MAGIC, true);
			swapStyle(Style.RANGE);
			tempMode = Melee.SLASH;
			NPC frostWeb = getNpcInRoom(bossName);
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((frostWeb = bossSet(frostWeb)) == null)
					continue;
				final int style = combatStyle == Style.MELEE ? 0 : combatStyle == Style.RANGE ? 2 : 1;
				invincible = isProtecting(frostWeb, 9964, style);
				secondaryStatus = invincible ? "Astea is protecting against us!" : "";
				if (lastMessage.contains("A magical force")) {
					lastMessage = "";
					topUp();
				} else if (unreachable) {
					if (combatStyle == Style.MELEE && !isEdgeTile(frostWeb.getLocation())) {
						final NPC spider = getNpcInRoom("spider");
						if (spider != null) {
							if (attackNpc(spider))
								Time.sleep(500, 900);
						} else
							topUp();
					} else
						topUp();
					unreachable = false;
				} else if (invincible) {
					if (!swapStyle(true, Style.RANGE, Style.MELEE)) {
						final NPC spider = getNpcInRoom("spider");
						if (spider != null) {
							if (attackNpc(spider)) {
								tempMode = Melee.CRUSH;
								updateFightMode();
								Time.sleep(500, 800);
							}
						} else if (!topUp() && combatStyle == Style.MELEE && distTo(frostWeb) > 1)
							attackBoss(frostWeb, false);
					}
					unreachable = false;
				} else {
					if (Style.RANGE.enabled && combatStyle != Style.RANGE && !isProtecting(frostWeb, 9964, 2))
						swapStyle(Style.RANGE);
					else if (combatStyle == Style.MELEE) {
						tempMode = Melee.SLASH;
						updateFightMode();
					}
					attackBoss(frostWeb, false);
				}
				eatFood(topFoods, 50, 60);
				Time.sleep(200, 300);
			}
		} else if (isBoss("Icy Bones") || getNpcInRoom("Bones") != null) {
			setBoss("Icy Bones", false, 0);
			setPrayer(true, Style.MELEE, true);
			tempMode = Melee.CRUSH;
			if (!genericBossFight())
				return;
		} else if (isBoss("Icefiend") || getNpcInRoom("Luminescent") != null) {
			setBoss("Luminescent Icefiend", true, 2);
			setPrayer(false, Style.RANGE, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			NPC iceFiend = getNpcInRoom(bossName);
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((iceFiend = bossSet(iceFiend)) == null)
					continue;
				if (iceFiend.getAnimation() == 13338 && getFrame(iceFiend) < 9) {
					final SceneObject corner = getObjInRoom(51300);
					if (corner == null || Walking.getEnergy() < Random.nextInt(8, 13)) {
						secondaryStatus = corner != null ? "Out of run.. Eating after the attack"
								: "Eating after the attack";
						while (iceFiend.getAnimation() == 13338)
							Time.sleep(100, 200);
					} else {
						secondaryStatus = "Dodging the icicles!";
						Tile oldCorner = corner.getLocation();
						safeTile = corner.getLocation();
						if (!Walking.isRunEnabled())
							Walking.setRun(true);
						else
							Time.sleep(300, 600);
						Time.sleep(100, 400);
						while (iceFiend != null && iceFiend.getAnimation() == 13338 && getFrame(iceFiend) < 11
								&& inRoom(bossRoom)) {
							if (failSafe())
								return;
							if (safeTile != null) {
								walkToMap(safeTile, 1);
								idleTimer = new Timer(0);
								while (distTo(safeTile) > 8.5) {
									if (failCheck())
										break;
									if (iceFiend.getAnimation() != 13338 || getFrame(iceFiend) > 10)
										break;
									if (!moving() || player().getAnimation() != -1)
										walkToMap(safeTile, 1);
									Time.sleep(40, 80);
								}
							}
							if (iceFiend.getAnimation() != 13338 || getFrame(iceFiend) > 10)
								break;
							for (final SceneObject o : getObjsInRoom(51300))
								if (o != null) {
									final Tile oTile = o.getLocation();
									if (oldCorner != null && oTile.equals(oldCorner))
										continue;
									if (safeTile == null
											|| (!oTile.equals(safeTile) && (oTile.getX() == safeTile.getX() || oTile
													.getY() == safeTile.getY()))) {
										oldCorner = safeTile;
										safeTile = oTile;
										break;
									}
								}
						}
						eatFood(topFoods, 15, 15);
						if (iceFiend != null)
							walkTo(iceFiend, 1);
						safeTile = null;
					}
					secondaryStatus = "";
				}
				attackBoss(iceFiend, false);
				eatFood(topFoods, 50, 40);
				Time.sleep(200, 300);
			}
		} else if (isBoss("Plane-Freezer") || getNpcInRoom("Lakhrahnaz") != null) {
			setBoss("Plane-Freezer Lakhrahnaz", false, 1);
			if (!moving() && castable(Dungeoneering.GATESTONE_TELEPORT))
				ridItem(GGS, "Drop");
			NPC planeFreezer = getNpcInRoom(bossName);
			swapStyle(Style.RANGE);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			setPrayer(false, Style.RANGE, true);
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((planeFreezer = bossSet(planeFreezer)) == null)
					continue;
				if (unreachable || (combatStyle == Style.MELEE && !adjacentTo(planeFreezer)))
					if (!moving()) {
						walkTo(planeFreezer, 0);
						unreachable = false;
					}
				setPrayer(false, Style.RANGE, true);
				final SceneObject c = getObjInRoom(SNOWY_CORNER);
				if (c != null && !moving() && !isAttacking(planeFreezer) && distTo(planeFreezer) < 3
						&& !adjacentTo(planeFreezer) && distTo(c) < 3) {
					walkTo(myLoc(), 2);
					waitToStart(false);
				}
				attackBoss(planeFreezer, true);
				eatFood(topFoods, 45, 55);
				Time.sleep(100, 200);
			}
			final SceneObject finished = SceneEntities.getNearest(FINISHED_LADDERS);
			if (finished != null) {
				SceneObject corner;
				final Tile finishTile = finished.getLocation();
				walkToMap(finishTile, 1);
				waitToEat(false);
				secondaryStatus = "Navigating to the nearest corner";
				while ((corner = getObjInRoom(SNOWY_STOPS)) != null && !atLoc(safeTile = corner.getLocation())
						&& distTo(finishTile) > 1) {
					if (failSafe())
						return;
					if (!moving())
						walkTo(safeTile, 0);
					if (!moving())
						walkTo(myLoc(), 5);
					Time.sleep(300, 400);
				}
				walkToMap(safeTile = finishTile, 0);
			}
		} else if (isBoss("Blood Chiller") || getNpcInRoom("Bloodchiller") != null) {
			setBoss("Blood Chiller", true, 0);
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			if (!genericBossFight())
				return;
		} else if (!unknownBossFight())
			return;
		finish = true;
	}

	private void abandonedBoss() {
		if (isBoss("Skeletal Hoarde") || getNpcInRoom(SKINWEAVER) != null) {
			setBoss("Skeletal Hoarde", true, 0);
			NPC skinweaver = getNpcInRoom(SKINWEAVER);
			boolean tunnelReady = false;
			SceneObject tunnel = null;
			swapStyle(Style.RANGE, Style.MAGIC);
			tempMode = Melee.CRUSH;
			updateFightMode();
			setRetaliate(combatStyle != Style.MELEE);
			dropItem(GGS);
			setRetaliate(combatStyle != Style.MELEE);
			setPrayer(false, Style.RANGE, true);
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (skinweaver != null && skinweaver.validate()) {
					if (!tunnelReady)
						if (lastMessage.contains("647828"))
							tunnelReady = true;
						else {
							final String shout = skinweaver.getMessage();
							if (shout != null)
								if (!shout.contains("Chat later"))
									tunnelReady = true;
								else if (NPCs.getNearest(attackable) == null) {
									setRetaliate(true);
									Time.sleep(2000, 4000);
								}
						}
					threadPitch(combatStyle == Style.MELEE ? 100 : 0);
					if (tunnelReady && (tunnel = getObjInRoom(TUNNELS)) != null) {
						safeTile = tunnel.getLocation();
						secondaryStatus = "Blocking the next tunnel!";
						lastMessage = "";
						smartSleep(doObjAction(tunnel, "Block"), false);
						while (tunnel != null) {
							if (failSafe())
								return;
							if (lastMessage.contains("fully powered"))
								break;
							if (distTo(safeTile) > 4)
								walkTo(safeTile, 1);
							if (doObjAction(tunnel, "Block"))
								waitToObject(tunnel, false);
							eatFood(topFoods, 20, 20);
							Time.sleep(50, 100);
							if ((tunnel = SceneEntities.getAt(safeTile)) == null || !intMatch(tunnel.getId(), TUNNELS))
								break;
						}
						secondaryStatus = "";
						if (!lastMessage.contains("fully powered")) {
							if (bossStage < 5)
								++bossStage;
							status = "Skeletal Hoarde: " + bossStage + " of 5 blocked";
						}
						lastMessage = "";
						safeTile = null;
						if (combatStyle == Style.MELEE && health() > Random.nextInt(40, 50))
							attackNpc(NPCs.getNearest(attackable));
						tunnelReady = false;
					}
					if ((nearMonster = NPCs.getNearest(attackable)) == null && getObjInRoom(TUNNELS) == null) {
						if (doNpcAction(skinweaver, "Talk-To")) {
							setRetaliate(true);
							waitToStop(false);
						}
						setRetaliate(true);
					} else if (combatStyle == Style.MELEE) {
						if (health() < 30 + NPCs.getLoaded(attackable).length * 3 + aComplexity
								+ (distTo(skinweaver) / 3)) {
							final Timer healTimer = new Timer(Random.nextInt(40000, 55000));
							safeTile = skinweaver.getLocation();
							while (health() < 95) {
								if (failSafe())
									return;
								final int hpp = getHpPercent(player());
								if (!healTimer.isRunning() || (hpp > 95 && hpp != 100))
									break;
								if (!tunnelReady)
									if (lastMessage.contains("96ff7d"))
										tunnelReady = true;
									else {
										final String shout = skinweaver.getMessage();
										if (shout != null && !shout.contains("Chat later"))
											tunnelReady = true;
									}
								eatFood(topFoods, 20, 20);
								final double skinDist = distTo(skinweaver);
								if (skinDist > 3 || skinDist < 1)
									walkAdjacentTo(safeTile, 3);
								else if ((nearMonster = NPCs.getNearest(attackable)) != null
										&& Calculations.distance(safeTile, nearMonster.getLocation()) < 5)
									attackNpc(nearMonster);
								Time.sleep(300, 600);
							}
							Time.sleep(1000, 1600);
							safeTile = null;
						}
						if (attackNpc(nearMonster = NPCs.getNearest(attackable)))
							Time.sleep(200, 800);
					} else {
						safeTile = skinweaver.getLocation();
						threadDown();
						final double skinDist = distTo(safeTile);
						if (skinDist > 3 || skinDist < 1)
							walkAdjacentTo(safeTile, 3);
						else if (player().getInteracting() == null)
							if ((nearMonster = NPCs.getNearest(attackable)) != null)
								if (!nearMonster.isOnScreen()) {
									turnTo(nearMonster);
									Time.sleep(400, 800);
								} else
									attackNpc(nearMonster);
					}
					eatFood(topFoods, 20, 20);
				} else {
					log(null, false, "Finding skinweaver");
					skinweaver = getNpcInRoom(SKINWEAVER);
				}
				Time.sleep(200, 400);
			}
		} else if (isBoss("Geomancer") || getNpcInRoom("Geomancer") != null) {
			setBoss("Hobgoblin Geomancer", true, 2);
			setPrayer(true, null, false);
			swapStyle(Style.RANGE);
			tempMode = Melee.STAB;
			if (!genericBossFight())
				return;
		} else if (isBoss("Bulwark Beast") || getNpcInRoom("Bulwark beast") != null) {
			setBoss("Bulwark Beast", true, 0);
			NPC bulwark = getNpcInRoom(bossName);
			if (bulwark != null) {
				final int currId = getId(bulwark);
				if (bossId < 1) {
					bossId = currId;
					bossStage = 1;
				}
				if (primaryStyle == Style.RANGE)
					if (currId == bossId)
						swapStyle(Style.MELEE, Style.MAGIC);
					else if (Style.RANGE.enabled)
						swapStyle(Style.RANGE);
				status = "Killing the Bulwark Beast" + (currId == bossId ? " (armored)" : "!");
			}
			setPrayer(combatStyle == Style.MAGIC, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((bulwark = bossSet(bulwark)) == null)
					continue;
				if (combatStyle != Style.MAGIC)
					if (getId(bulwark) != bossId && (bossStage != 0 || invContains(currentWep))) {
						status = "Killing the Bulwark Beast!";
						if (primaryStyle == Style.RANGE && Style.RANGE.enabled)
							swapStyle(Style.RANGE);
						else
							ridItem(currentWep, "Wield");
						bossStage = 0;
					}
				if (attackBoss(bulwark, true))
					setRetaliate(true);
				eatFood(topFoods, 50, 60);
				Time.sleep(200, 300);
			}
		} else if (isBoss("Cursebearer") || getNpcInRoom("cursebearer") != null) {
			setBoss("Unholy Cursebearer", true, 2);
			setPrayer(true, Style.MELEE, true);
			tempMode = Melee.CRUSH;
			if (!genericBossFight())
				return;
		} else if (isBoss("Shadow-Forger") || getNpcInRoom("Ihlakhizan") != null) {
			setBoss("Shadow-Forger", true, 0);
			animBreaker = new int[] { 13030 };
			setPrayer(true, combatStyle == Style.MELEE ? Style.MELEE : Style.MAGIC, true);
			tempMode = Melee.STAB;
			updateFightMode();
			setRetaliate(false);
			NPC ihlakhizan = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((ihlakhizan = bossSet(ihlakhizan)) == null)
					continue;
				if (ihlakhizan.getAnimation() == 13030) {
					if (safeTile == null || distTo(safeTile) > 3) {
						final SceneObject pillar = getObjInRoom(51110), corner = getObjInRoom(50749);
						if (pillar != null && corner != null) {
							final int pX = pillar.getLocation().getX(), pY = pillar.getLocation().getY();
							final int cX = corner.getLocation().getX(), cY = corner.getLocation().getY();
							safeTile = new Tile(pX + (cX - pX) / 4, pY + (cY - pY) / 4, 0);
						}
					}
					walkTo(safeTile, 0);
					Time.sleep(100, 600);
					while (ihlakhizan.getAnimation() == 13030) {
						if (failSafe())
							return;
						Time.sleep(200, 400);
						if (distTo(safeTile) > 1.5) {
							if (walkTo(safeTile, 0))
								Time.sleep(200, 800);
						} else if (moving())
							topUp();
						else
							omNomNom();
					}
					if (!moving() && atLoc(safeTile))
						omNomNom();
				} else {
					attackBoss(ihlakhizan, true);
					if (!roomSwitch && ihlakhizan.getAnimation() != 13030)
						eatFood(topFoods, 50, 0);
				}
				Time.sleep(100, 300);
			}
		} else if (isBoss("Bal'lak") || getNpcInRoom("Pummeller") != null) {
			setBoss("Bal'lak the Pummeller", false, 1);
			final int maxDef = Random.nextInt(90, 120);
			setPrayer(true, Style.MELEE, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			NPC pummeller = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((pummeller = bossSet(pummeller)) == null)
					continue;
				final SceneObject slime = getObjInRoom(49298);
				if (slime != null && distTo(slime) < 3) {
					if (!moving())
						walkToMap(reflect(slime.getLocation(), pummeller.getLocation(), 0.7), 1);
				} else if (bossHp < random(5, 10) || Widgets.get(945, 17).getRelativeX() < maxDef)
					eatFood(topFoods, 50, 60);
				else if (castable(Dungeoneering.GATESTONE_TELEPORT)) {
					secondaryStatus = "Teleporting out to degenerate defense";
					if (escapeBoss()) {
						if (!goHome())
							defenseDegenerate(false);
						return;
					}
				}
				attackBoss(pummeller, true);
				Time.sleep(100, 200);
			}
		} else if (!unknownBossFight())
			return;
		finish = true;
	}

	private void furnishedBoss() {
		if (isBoss("Rammernaut") || getNpcInRoom("Rammernaut") != null) {
			setBoss("Rammernaut", false, 0);
			swapStyle(Style.MAGIC);
			tempMode = Melee.STAB;
			prayTimer = new Timer(Random.nextInt(4000, 6000));
			messageBreaker = "CHAA";
			NPC rammernaut = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((rammernaut = bossSet(rammernaut)) == null)
					continue;
				final String shout = rammernaut.getMessage();
				if (shout != null && shout.contains("CHAA")) {
					secondaryStatus = "Running for cover!";
					safeTile = safeCorner(rammernaut.getLocation());
					if (safeTile != null) {
						walkToMap(safeTile, 0);
						for (int c = 0; c < 7 && !isDead(); ++c) {
							if (rammernaut != null && rammernaut.getMessage() == null)
								break;
							if (!moving() && distTo(safeTile) < 3 && distTo(rammernaut) < 3)
								break;
							if (!topUp())
								Time.sleep(400, 600);
						}
						for (int c = 0; c < 20 && !isDead(); ++c) {
							if (rammernaut != null) {
								final String msg = rammernaut.getMessage();
								if (msg != null && msg.contains("Oo"))
									break;
							}
							if (distTo(safeTile) < 3 && distTo(rammernaut) < 4)
								break;
							if (!topUp())
								Time.sleep(200, 300);
						}
						if (failSafe())
							return;
						if (rammernaut != null && rammernaut.getMessage() == null)
							walkToScreen(currentRoom.getCenter());
					}
					secondaryStatus = "";
				} else if (rammernaut.getAnimation() == 13705)
					topUp();
				else {
					final SceneObject corner = getObjInRoom(51762);
					if (rammernaut.getMessage() == null
							&& (isEdgeTile(myLoc()) || (corner != null && distTo(corner) < 4))) {
						walkToScreen(currentRoom.getCenter());
						Time.sleep(200, 600);
					}
					setPrayer(true, Walking.isRunEnabled() ? Style.MELEE : null, true);
					attackBoss(rammernaut, true);
					if (rammernaut.getMessage() != null)
						continue;
					eatFood(topFoods, 50, 20);
				}
				Time.sleep(100, 200);
			}
		} else if (isBoss("Stomp") || getNpcInRoom("Stomp") != null) {
			final Tile[] stoneChecks = new Tile[2];
			int dX = 0, dY = 0, stoneCount = Random.nextInt(10, 16);
			if (setBoss("Stomp", false, 0)) {
				save(bounds, STOMP_CRYSTALS);
				bossStage = 2;
			}
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			NPC stomp = getNpcInRoom(bossName);
			if (stomp != null) {
				final Tile backDoor = getBackDoor(), bossTile = stomp.getLocation();
				if (backDoor != null && distTo(backDoor) < 4) {
					walkFast(bossTile, true, 2);
					setPrayer(true, null, false);
					if (backDoor != null) {
						if (backDoor.getX() - bossTile.getX() > 3)
							dX = 1;
						else if (backDoor.getX() - bossTile.getX() < -3)
							dX = -1;
						if (backDoor.getY() - bossTile.getY() > 3)
							dY = 1;
						else if (backDoor.getY() - bossTile.getY() < -3)
							dY = -1;
					}
					int i = 0;
					for (final SceneObject o : getObjsInRoom(STOMP_LODESTONES))
						if (o != null) {
							final Tile oT = o.getLocation();
							stoneChecks[i] = new Tile(oT.getX() + dX, oT.getY() + dY, 0);
							if (++i > 1)
								break;
						}
				}
			}
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((stomp = bossSet(stomp)) == null)
					continue;
				if (getObjInRoom(ACTIVE_STONES) != null) {
					Timer stoneTimer = new Timer(Random.nextInt(10000, 13000));
					badTiles.clear();
					stoneCount = Random.nextInt(10, 15);
					Time.sleep(500, 1000);
					for (int I = 0; I < ALL_STONES.length; ++I)
						if (SceneEntities.getNearest(ALL_STONES[I]) != null) {
							bossStage = 2 - I;
							break;
						}
					final int CRYSTAL_ID = 15752 - bossStage;
					status = "Killing Stomp (stage " + (3 - bossStage) + " of 3)";
					setPrayersOff();
					lastMessage = "";
					Tile blockTile = null;
					while (getObjInRoom(ACTIVE_STONES) != null) {
						if (failSafe())
							return;
						if (bossStage == 0 && getObjInRoom(FINISHED_LADDERS) != null)
							break;
						final int crystalCount = invCount(CRYSTAL_ID);
						unreachable = false;
						if (crystalCount < 2 && crystalCount < getObjsInRoom(ACTIVE_STONES).length) {
							final GroundItem crystal = getRoomItem(stompCrystal);
							makeSpace(true);
							if (crystal != null) {
								secondaryStatus = "Picking up "
										+ (bossStage == 2 ? "blue" : bossStage == 1 ? "green" : "red") + " crystals";
								if (pickUpItem(crystal)) {
									waitToStop(false);
									if (unreachable)
										badTiles.add(crystal.getLocation());
								} else if (!reachable(crystal, false))
									badTiles.add(crystal.getLocation());
							} else {
								final GroundItem bc = GroundItems.getNearest(STOMP_CRYSTALS);
								if (bc != null && reachable(bc, false)) {
									badTiles.remove(bc.getLocation());
									continue;
								}
								final SceneObject blockRock = bc != null ? getReachableObjNear(bc, STOMP_BLOCK_ROCKS)
										: null;
								if (blockRock != null) {
									secondaryStatus = "Mining rocks to reach the crystals";
									smartSleep(doObjAction(blockRock, "Mine"), true);
								} else if (!topUp() && (stoneTimer == null || !stoneTimer.isRunning())
										&& !lastMessage.contains("currently invulnerable")) {
									secondaryStatus = "";
									if (attackBoss(stomp, false))
										waitToStop(false);
								}
							}
						} else if (stoneTimer == null || stoneTimer.isRunning()) {
							if (blockTile == null)
								for (final Tile t : stoneChecks)
									if (t != null && SceneEntities.getAt(t) != null) {
										blockTile = t;
										break;
									}
							if (blockTile != null) {
								final SceneObject blockRock = getReachableObjNear(blockTile, STOMP_BLOCK_ROCKS);
								if (blockRock != null) {
									secondaryStatus = "Mining to reach the lodestones";
									if (smartSleep(doObjAction(blockRock, "Mine"), true))
										if (reachable(blockTile, true))
											blockTile = null;
								} else
									Time.sleep(300, 600);
							} else {
								final SceneObject stone = getObjInRoom(ACTIVE_STONES);
								if (stone != null) {
									secondaryStatus = "Activating the lodestones";
									safeTile = stone.getLocation();
									lastMessage = "";
									if (doObjAction(stone, "Charge")) {
										if (waitToStop(false) || waitToAnimate()) {
											final SceneObject next = getNextObj(stone, ACTIVE_STONES);
											for (int c = 0; c < 5; ++c) {
												if (unreachable)
													break;
												if (player().getAnimation() == 551) {
													if (next != null && doObjAction(next, "Charge"))
														Time.sleep(300, 600);
													stoneTimer = null;
													break;
												}
												Time.sleep(150, 200);
											}
										}
										if (unreachable || lastMessage.startsWith("You can't reach")) {
											blockTile = safeTile;
											safeTile = null;
										} else if (stoneTimer != null)
											stoneTimer.reset();
									}
								}
							}
						} else if (!topUp() && !lastMessage.contains("currently invulnerable")) {
							secondaryStatus = "";
							if (attackBoss(stomp, false))
								waitToStop(true);
						}
						Time.sleep(200, 300);
					}
					secondaryStatus = "";
					safeTile = null;
					badTiles.clear();
					setPrayer(true, null, false);
				} else if (bossStage == 0 && stomp.getAnimation() == 13449) {
					for (final Tile t : stoneChecks)
						if (t != null && distTo(t) > 2) {
							walkTo(t, 1);
							if (!topUp())
								Time.sleep(500, 800);
							break;
						}
				} else {
					boolean blocked = false;
					for (final Tile t : stoneChecks)
						if (t != null && SceneEntities.getAt(t) != null) {
							blocked = true;
							break;
						}
					if (blocked || getObjsInRoom(STOMP_BLOCK_ROCKS).length > stoneCount) {
						lastMessage = "";
						if (combatStyle == Style.MELEE)
							setRetaliate(false);
						final SceneObject block = getObjInRoom(STOMP_BLOCK_ROCKS);
						if (block != null) {
							safeTile = block.getLocation();
							if (!moving() && (distTo(safeTile) > 1 || isAttacking(stomp)))
								walkTo(safeTile, 1);
							else
								topUp();
						}
					} else {
						eatFood(topFoods, 50, 50);
						setRetaliate(true);
						if (!lastMessage.contains("currently invulnerable")) {
							attackBoss(stomp, false);
							safeTile = null;
						} else
							topUp();
					}
				}
				Time.sleep(200, 300);
			}
		} else if (isBoss("Riftsplitter") || getNpcInRoom("Riftsplitter") != null) {
			setBoss("Riftsplitter", true, 0);
			messageBreaker = "";
			boolean newShout = false;
			String shout = null;
			final ArrayList<Area> portals = new ArrayList<Area>();
			final ArrayList<Tile> corners = new ArrayList<Tile>();
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			NPC riftsplitter = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((riftsplitter = bossSet(riftsplitter)) == null)
					continue;
				if (riftsplitter.getMessage() != null && shout != null && shout.equals(riftsplitter.getMessage()))
					newShout = false;
				else {
					shout = riftsplitter.getMessage();
					newShout = true;
				}
				if (shout != null && newShout) {
					secondaryStatus = "Look out for that portal!";
					final Tile mT = myLoc();
					if (corners.isEmpty()) {
						portals.clear();
						portals.add(new Area(new Tile(mT.getX() - 1, mT.getY() - 1, 0), new Tile(mT.getX() + 1, mT
								.getY() + 1, 0)));
						o: for (final SceneObject obj : getObjsInRoom(51866)) {
							if (obj == null || !obj.validate())
								continue;
							final Tile oTile = obj.getLocation();
							for (final Area portal : portals)
								if (portal.contains(oTile))
									continue o;
							corners.add(obj.getLocation());
						}
					} else
						portals.add(new Area(new Tile(mT.getX() - 1, mT.getY() - 1, 0), new Tile(mT.getX() + 1, mT
								.getY() + 1, 0)));
					double dist = 20;
					if (safeTile == null)
						for (final Tile corner : corners) {
							final double cDist = distTo(corner);
							if (cDist > 2 && cDist < dist) {
								safeTile = corner;
								dist = cDist;
							}
						}
					if (safeTile != null) {
						walkFast(safeTile, true, 0);
						corners.remove(safeTile);
						topUp();
						for (int c = 0; c < 10; ++c) {
							if (isDead() || distTo(safeTile) < 4)
								break;
							if (!moving())
								walkTo(safeTile, 0);
							else if (!topUp())
								Time.sleep(300, 500);
						}
						safeTile = null;
					}
					secondaryStatus = "";
				} else {
					attackBoss(riftsplitter, true);
					eatFood(topFoods, 50, 0);
					if (distTo(riftsplitter) < 3 && !player().isInCombat() && !riftsplitter.isInCombat())
						walkToMap(currentRoom.getCenter(), 1);
				}
				Time.sleep(100, 200);
			}
		} else if (isBoss("Lexicus") || getNpcInRoom("Lexicus") != null) {
			setBoss("Lexicus Runewright", false, 1);
			messageBreaker = "barrage";
			String shout = "";
			setPrayer(false, Style.MAGIC, true);
			swapStyle(Style.RANGE);
			tempMode = Melee.SLASH;
			NPC lexicus = getNpcInRoom(bossName);
			if (developer)
				log(null, false, status);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((lexicus = bossSet(lexicus)) == null)
					continue;
				shout = lexicus.getMessage();
				if (shout != null && shout.contains("barrage")) {
					final SceneObject corner = SceneEntities.getNearest(bookCorner);
					if (corner != null) {
						safeTile = corner.getLocation();
						walkToMap(safeTile, 1);
						if (!topUp())
							Time.sleep(500, 700);
						attackBoss(lexicus, false);
						safeTile = null;
					}
					secondaryStatus = "";
				} else if (isNpcInteracting() || topUp()) {
					final NPC tome = getNpcInRoom("Tome");
					if (tome != null && (distTo(tome) < 3 || (tome.getInteracting() != null && tome.isOnScreen()))) {
						if (smartSleep(attackNpc(getNpcInRoom("Tome")), false))
							eatFood(topFoods, 50, 20);
					} else if (lexicus.getAnimation() == 13499)
						for (int c = 0; c < 15 && !isDead(); ++c) {
							if (getNpcInRoom("Tome") != null)
								break;
							topUp();
							Time.sleep(100, 200);
						}
					else
						attackBoss(lexicus, false);
				} else
					attackBoss(lexicus, false);
				eatFood(topFoods, 50, 0);
				Time.sleep(100, 200);
			}
		} else if (isBoss("Sagittare") || getObjInRoom(SAGITTARE_FLOOR) != null) {
			setBoss("Sagittare", false, 0);
			messageBreaker = "";
			if (combatStyle == Style.RANGE)
				if (swapAlternative())
					log(Color.WHITE, false, "Swapped our attack style for Sagittare");
				else {
					severe("We don't have a way to kill Sagittare, aborting dungeon!");
					exit = true;
					return;
				}
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.SLASH;
			NPC sagittare = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((sagittare = bossSet(sagittare)) == null)
					continue;
				if (lastMessage.contains("stops you") || lastMessage.contains("bound"))
					topUp();
				if (sagittare.getAnimation() == 13270 || sagittare.getMessage() != null) {
					if (distTo(currentRoom.getCenter()) > 2)
						safeTile = currentRoom.getCenter();
					else {
						final SceneObject door = SceneEntities.getNearest(50352);
						safeTile = door != null ? door.getLocation() : sagittare.getLocation();
					}
					if (walkToMap(safeTile, 1))
						topUp();
					Time.sleep(2000, 3000);
					safeTile = null;
				} else if (combatStyle != Style.MAGIC && distTo(sagittare) > 1) {
					if (moving())
						topUp();
					else
						walkToMap(sagittare, 1);
					final Tile dest = getFlag(false);
					if (dest != null && Calculations.distance(dest, sagittare.getLocation()) > 1)
						if (walkToMap(sagittare.getLocation(), 0))
							topUp();
				}
				eatFood(topFoods, 50, 60);
				if (sagittare.getAnimation() != 8939 && sagittare.getPassiveAnimation() != 1576)
					attackBoss(sagittare, false);
				Time.sleep(100, 200);
			}
		} else if (isBoss("Night-gazer") || getNpcInRoom("Khighorahk") != null) {
			setBoss("Night-gazer", true, 0);
			animBreaker = K_SPEC_ANIMS;
			NPC khighorahk = getNpcInRoom(bossName);
			if (bossId < 1) {
				if (khighorahk != null)
					bossId = getId(khighorahk);
				bossStage = 1;
			}
			setPrayer(false, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			status = "Killing the Night-gazer (stage " + (2 - bossStage) + " of 2)";
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((khighorahk = bossSet(khighorahk)) == null)
					continue;
				if (intMatch(khighorahk.getAnimation(), K_SPEC_ANIMS)
						&& (moving() || distTo(khighorahk.getLocation()) < 6)) {
					safeTile = safePillar(khighorahk.getLocation());
					final SceneObject pillar = getObjInRoom(49265);
					if (safeTile != null && (combatStyle == Style.MELEE || distTo(safeTile) > 2)) {
						walkToMap(safeTile, 1);
						final Tile bossTile = khighorahk.getLocation();
						for (int c = 0; c < 7 && !isDead(); ++c) {
							if (pillar != null && distTo(pillar) < 4)
								if (doObjAction(pillar, "Light")) {
									waitToObject(pillar, false);
									if (player().getAnimation() != 13355 || player().getAnimation() != 13354)
										doObjAction(pillar, "Light");
									break;
								}
							if (player().getAnimation() == 10070) {
								topUp();
								break;
							}
							if (Walking.isRunEnabled())
								Time.sleep(200, 300);
							else if (distTo(safeTile) < 3 || distTo(bossTile) > 7)
								break;
							if (distTo(bossTile) > 4 && !intMatch(khighorahk.getAnimation(), K_START_ANIMS))
								break;
							Time.sleep(200, 250);
							if (!intMatch(khighorahk.getAnimation(), K_SPEC_ANIMS))
								break;
						}
						for (int c = 0; c < 5 && !isDead(); ++c) {
							if (intMatch(khighorahk.getAnimation(), K_START_ANIMS))
								break;
							if (attackBoss(khighorahk, true)) {
								waitToAnimate();
								break;
							}
							Time.sleep(400, 600);
						}
						safeTile = null;
					}
				} else {
					final SceneObject[] litPillars = getObjsInRoom(49266, 49267);
					if (litPillars.length < 2) {
						final SceneObject unlit = SceneEntities.getNearest(49265);
						if (doObjAction(unlit, "Light"))
							waitToObject(unlit, false);
					} else if (!intMatch(khighorahk.getAnimation(), K_START_ANIMS)) {
						eatFood(topFoods, 50, 20);
						if (!intMatch(khighorahk.getAnimation(), K_START_ANIMS))
							attackBoss(khighorahk, true);
						if (getId(khighorahk) != bossId) {
							bossId = getId(khighorahk);
							bossStage = 0;
							status = "Killing the Night-gazer (stage 2 of 2)";
						}
					}
				}
				Time.sleep(100, 200);
			}
		} else if (!unknownBossFight())
			return;
		finish = true;
	}

	@SuppressWarnings("incomplete-switch")
	private void occultBoss() {
		if (isBoss("Skeletal Trio") || getNpcInRoom("Skeletal warrior") != null
				|| getNpcInRoom("Skeletal sorcerer") != null || getNpcInRoom("Skeletal archer") != null) {
			setBoss("Skeletal Trio", true, 0);
			NPC meleer, archer, sorcerer, targ = null;
			setRetaliate(combatStyle != Style.MELEE);
			bossStage = 2;
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				boolean mInvincible = true, aInvincible = true, sInvincible = true;
				if ((meleer = getNpcInRoom("Skeletal warrior")) != null)
					mInvincible = isProtecting(meleer, 11939, combatStyle.idx);
				if ((archer = getNpcInRoom("Skeletal archer")) != null)
					aInvincible = isProtecting(archer, 12043, combatStyle.idx);
				if ((sorcerer = getNpcInRoom("Skeletal sorcerer")) != null)
					sInvincible = isProtecting(sorcerer, 11998, combatStyle.idx);
				switch (combatStyle) {
				case MELEE:
					protection = sorcerer != null ? Style.MAGIC : archer != null ? Style.RANGE : Style.MELEE;
					targ = !aInvincible ? archer : !sInvincible ? sorcerer : !mInvincible ? meleer : null;
					tempMode = Melee.CRUSH;
					break;
				case RANGE:
					protection = meleer != null ? Style.MELEE : sorcerer != null ? Style.MAGIC : Style.RANGE;
					targ = !sInvincible ? sorcerer : !mInvincible ? meleer : !aInvincible ? archer : null;
					tempMode = Melee.SLASH;
					break;
				case MAGIC:
					protection = archer != null ? Style.RANGE : meleer != null ? Style.MELEE : Style.MAGIC;
					targ = !mInvincible ? meleer : !aInvincible ? archer : !sInvincible ? sorcerer : null;
					tempMode = Melee.SLASH;
					break;
				}
				setPrayer(true, protection, true);
				if (targ != null) {
					attackBoss(targ, false);
					updateFightMode();
					if (!eatFood(topFoods, 50, 50))
						Time.sleep(400, 600);
				} else if ((meleer != null && archer != null && sorcerer != null) || !swapAlternative())
					topUp();
				Time.sleep(200, 300);
			}
		} else if (isBoss("Runebound") || getObjInRoom(RUNEBOUND_ACTIVES) != null) {
			setBoss("Runebound Behemoth", true, 1);
			messageBreaker = "";
			tempMode = Melee.CRUSH;
			NPC runebound = getNpcInRoom(bossName);
			if (bossId < 1 && runebound != null)
				bossId = getId(runebound);
			setPrayer(true, Style.MAGIC, true);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((runebound = bossSet(runebound)) == null)
					continue;
				if (runebound.getAnimation() == 14440 || runebound.getMessage() != null) {
					secondaryStatus = "Dodging the lightning!";
					final SceneObject corner = getObjInRoom(54786);
					walkToScreen(distTo(corner) > 2 && !isEdgeTile(myLoc()) ? reflect(runebound.getLocation(), myLoc(),
							.3) : bossRoom.getCenter());
					topUp();
					secondaryStatus = "";
				} else {
					final int differential = bossId - getId(runebound);
					boolean meleeI = true, rangeI = true, magicI = true;
					if (differential == 15)
						meleeI = false;
					else if (differential == 30)
						rangeI = false;
					else if (differential == 60)
						magicI = false;
					else if (differential == -15) {
						meleeI = false;
						magicI = false;
					} else if (differential == -30) {
						rangeI = false;
						meleeI = false;
					} else if (differential == -45) {
						rangeI = false;
						magicI = false;
					}
					final boolean[] invin = { meleeI, rangeI, magicI };
					if (!invin[combatStyle.idx]) {
						attackBoss(runebound, true);
						eatFood(topFoods, 30, 15);
					} else {
						eatFood(topFoods, 50, 20);
						final SceneObject styleActive = getObjInRoom(RUNEBOUND_ACTIVES[combatStyle.idx]);
						if (styleActive != null) {
							if (health() > random(8, 15)) {
								if (doObjAction(styleActive, "Deactivate"))
									waitToStop(false);
							} else if (runebound.isOnScreen() || !isAutoRetaliateEnabled())
								attackBoss(runebound, true);
						} else
							for (int I = 0; I < 3; ++I) {
								final Style test = styleOf(I);
								if (combatStyle != test && test.enabled
										&& (!invin[I] || getObjInRoom(RUNEBOUND_ACTIVES[I]) != null)) {
									final SceneObject active = getObjInRoom(RUNEBOUND_ACTIVES[I]);
									if (active != null)
										walkFast(active, true, 1);
									swapStyle(test);
									break;
								}
								if (I == 2)
									topUp();
							}
					}
				}
				Time.sleep(100, 200);
			}
		} else if (isBoss("Gravecreeper") || getObjInRoom(54447) != null) {
			setBoss("Gravecreeper", true, 0);
			if (combatStyle == Style.RANGE)
				swapStyle(Style.MELEE);
			tempMode = Melee.SLASH.enabled() ? Melee.SLASH : Melee.CRUSH;
			NPC gravecreeper = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((gravecreeper = bossSet(gravecreeper)) == null)
					continue;
				final SceneObject aGrave = !moving() ? getObjInRoom(GRAVE_PADS) : null;
				final String graveMessage = gravecreeper.getMessage();
				if (graveMessage != null) {
					if (graveMessage.equals("Burrrn")) {
						secondaryStatus = "Dodging the burn spots!";
						final Tile badTile = gravecreeper.getLocation();
						safeTile = safeGrave(badTile);
						if (safeTile != null && badTile != null)
							for (int c = 0; c < 10; ++c) {
								if (isDead())
									break;
								if (distTo(badTile) < 2) {
									walkTo(safeTile, 1);
									++c;
								} else if (topUp())
									++c;
								Time.sleep(400, 500);
							}
						secondaryStatus = "";
					} else if (graveMessage.contains("Buuu"))
						omNomNom();
					else if (graveMessage.contains("Diii"))
						omNomNom();
					else if (graveMessage.contains("Braa"))
						omNomNom();
				} else if (aGrave != null && distTo(aGrave) < 2) {
					safeTile = safeGrave(gravecreeper.getLocation());
					if (safeTile != null)
						walkTo(safeTile, 1);
					else
						walkToScreen(bossRoom.getCenter());
				} else {
					setPrayer(true, combatStyle == Style.MELEE ? Style.MELEE : Style.RANGE, true);
					attackBoss(gravecreeper, false);
					eatFood(topFoods, 50, 20);
				}
				Time.sleep(200, 300);
			}
		} else if (isBoss("Necrolord") || getObjInRoom(NECROLORD_FLOOR) != null) {
			setBoss("Necrolord", true, 0);
			if (combatStyle == Style.MELEE && !swapAlternative()) {
				severe("We can't kill this boss with melee, aborting dungeon");
				++controlledAborts;
				exit = true;
				return;
			}
			NPC necrolord = getNpcInRoom(bossName);
			if (necrolord != null && distTo(necrolord) > 3)
				walkToMap(necrolord, 1);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((necrolord = bossSet(necrolord)) == null)
					continue;
				final NPC minion = getNpcInRoom("Skeletal minion");
				setPrayer(true, minion != null && distTo(minion) < 4 ? Style.MELEE : Style.MAGIC, true);
				if (combatStyle == Style.MELEE) {
					severe("We can't kill this boss with melee, aborting dungeon");
					++controlledAborts;
					exit = true;
					return;
				}
				if (unreachable) {
					if (!isAttacking(necrolord))
						topUp();
					unreachable = false;
				}
				attackBoss(necrolord, false);
				eatFood(topFoods, 50, 60);
				Time.sleep(200, 400);
			}
		} else if (isBoss("Flesh-Spoiler") || getNpcInRoom("Flesh-Spoiler") != null) {
			NPC fleshSpoiler = getNpcInRoom("Flesh-Spoiler Haasghenahk");
			if (setBoss("Haasghenahk", true, 0)) {
				if (fleshSpoiler != null)
					bossId = getId(fleshSpoiler);
				bossStage = 1;
			}
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			setPrayer(true, getNpcInRoom("spawn") != null ? Style.MELEE : Style.MAGIC, true);
			status = "Killing the Flesh-Spoiler (Stage " + (2 - bossStage) + " of 2)";
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((fleshSpoiler = bossSet(fleshSpoiler)) == null)
					continue;
				setPrayer(true, getNpcInRoom("spawn") != null ? Style.MELEE : Style.MAGIC, true);
				attackBoss(fleshSpoiler, true);
				eatFood(topFoods, 50, 40);
				if (bossStage == 1) {
					final int fleshId = getId(fleshSpoiler);
					if (bossId > 0 && bossId != fleshId) {
						status = "Killing the Flesh-Spoiler (Stage 2 of 2)";
						bossId = fleshId;
						bossStage = 0;
					}
				}
				Time.sleep(200, 300);
			}
		} else if (isBoss("Yk'Lagor") || getNpcInRoom("Yk'Lagor the Thunderous") != null) {
			setBoss("Yk'Lagor the Thunderous", false, 1);
			roomSwitch = false;
			if (!swapStyle(Style.MAGIC) && combatStyle == Style.MELEE)
				setRetaliate(false);
			tempMode = Melee.SLASH;
			final ArrayList<Tile> temp = new ArrayList<Tile>();
			for (final SceneObject p : getObjsInRoom(55057)) {
				if (p == null)
					continue;
				for (final Tile t : getAdjacentTo(p.getLocation()))
					if (isEdgeTile(t)) {
						temp.add(t);
						break;
					}
			}
			final Tile[] spots = temp.toArray(new Tile[temp.size()]);
			if (getNpcInRoom("Mysterious mage") != null) {
				secondaryStatus = "Freeing Yk'Lagor from the mages";
				walkFast(bossRoom.getCenter(), true, 2);
				setRetaliate(false);
				setPrayer(false, Style.MAGIC, true);
				while (getNpcInRoom("Mysterious mage") != null) {
					if (failSafe())
						return;
					if (NPCs.getLoaded(unInteracting).length <= 4) {
						safeTile = getNearestTileTo(myLoc(), spots);
						if (!atLoc(safeTile))
							walkTo(safeTile, 0);
						waitToEat(false);
					} else {
						if (attackNpc(NPCs.getNearest(unInteracting)))
							Time.sleep(600, 800);
						eatFood(topFoods, 50, 50);
					}
				}
				secondaryStatus = "";
				if (atLoc(safeTile))
					omNomNom();
				setRetaliate(true);
				open(Tabs.INVENTORY);
			}
			messageBreaker = "Come";
			setPrayer(true, combatStyle == Style.MELEE ? Style.MELEE : Style.MAGIC, true);
			NPC thunderous = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((thunderous = bossSet(thunderous)) == null)
					continue;
				final String shout = thunderous.getMessage();
				threadPitch(combatStyle == Style.MELEE ? 100 : 0);
				if (shout != null) {
					if (shout.startsWith("This is")) {
						secondaryStatus = "Taking cover from the quake!";
						safeTile = getNearestTileTo(myLoc(), spots);
						final Timer quakeTimer = new Timer(Random.nextInt(2600, 3200));
						if (safeTile != null) {
							while (!atLoc(safeTile) && quakeTimer.isRunning()) {
								if (failSafe())
									return;
								if (thunderous != null && thunderous.getAnimation() == -1)
									break;
								if (atLoc(safeTile)) {
									if (moving() || isAutoRetaliateEnabled())
										topUp();
									else
										omNomNom();
								} else if (!moving())
									walkTo(safeTile, 0);
								Time.sleep(100, 200);
							}
							if (invCacheContains(GGS) && atLoc(safeTile)) {
								walkAdjacentTo(myLoc(), 1.5);
								dropItem(GGS);
							}
						}
					} else if (shout.startsWith("Come")) {
						secondaryStatus = "Dodging his grasp!";
						safeTile = getNearestTileTo(myLoc(), spots);
						final Timer graspTimer = new Timer(Random.nextInt(3800, 4500));
						if (safeTile != null) {
							while (!roomSwitch && graspTimer.isRunning()) {
								if (failSafe())
									return;
								if (roomSwitch) {
									if (lastMessage.contains("stunned")) {
										log(null, false, "Damn it.. Got sucked in.");
										Time.sleep(4000, 6000);
									}
									break;
								}
								if (atLoc(safeTile)) {
									if (moving() || isAutoRetaliateEnabled())
										topUp();
									else
										omNomNom();
								} else if (walkTo(safeTile, 0))
									waitToEat(false);
								Time.sleep(100, 200);
							}
							if (invCacheContains(GGS) && atLoc(safeTile)) {
								walkAdjacentTo(myLoc(), 1.5);
								dropItem(GGS);
							}
							roomSwitch = false;
						}
					}
					secondaryStatus = "";
				} else if (!moving()) {
					final Tile safety = getNearestTileTo(myLoc(), spots);
					if (distTo(safety) > (combatStyle == Style.MELEE ? 4 : 3))
						walkToScreen(safety);
					else
						attackBoss(thunderous, true);
					eatFood(topFoods, 50, 0);
				} else if (attackBoss(thunderous, true))
					if (!eatFood(topFoods, 50, 0)) {
						final Tile safety = getNearestTileTo(myLoc(), spots);
						if (safety != null)
							if (!safety.isOnScreen())
								turnTo(safety);
							else
								safety.hover();
					}
				Time.sleep(200, 400);
			}
		} else if (!unknownBossFight())
			return;
		finish = true;
	}

	private void warpedBoss() {
		if (isBoss("Blink") || getObjInRoom(BLINK_PILLARS) != null) {
			setBoss("Blink", false, 0);
			boolean respawn = true;
			final Tile center = bossRoom.getCenter();
			tempMode = Melee.STAB;
			if (combatStyle == Style.MAGIC)
				swapAlternative();
			setPrayer(false, Style.MAGIC, true);
			NPC blink = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((blink = getNpcInRoom(bossName)) == null) {
					final SceneObject pillar = getObjInRoom(RAISED_PILLARS);
					secondaryStatus = "";
					respawn = true;
					if (pillar == null) {
						if (doObjAction(getCloseObjNear(center, SUNKEN_PILLARS), "Raise"))
							waitToStop(false);
					} else {
						setPrayer(false, Style.MAGIC, true);
						puzzlePoints.clear();
						topUp();
					}
				} else if (respawn || blink.isMoving() || !stringMatch("Attack", blink.getActions())) {
					if (respawn) {
						final SceneObject pillar = getObjInRoom(RAISED_PILLARS);
						boolean aligned = false;
						safeTile = blink.getLocation();
						if (pillar != null) {
							final Tile pTile = pillar.getLocation();
							aligned = safeTile.getX() == pTile.getX() || safeTile.getY() == pTile.getY();
						}
						if (aligned) {
							secondaryStatus = "Got him! Time to dish the pain";
							setPrayer(true, null, true);
							for (int c = 0; c < 10; ++c) {
								if (blink == null || !blink.isMoving() || isDead())
									break;
								Time.sleep(200, 400);
							}
						} else
							blinkPath(blink);
						respawn = false;
					}
					if (!puzzlePoints.isEmpty()) {
						final Tile near = getNearestTileTo(myLoc(), puzzlePoints.toArray(new Tile[puzzlePoints.size()]));
						if (near != null && distTo(near) < 5 && walkHop(reflect(near, myLoc(), 1), 1)) {
							puzzlePoints.clear();
							topUp();
						}
					} else
						topUp();
				} else {
					attackBoss(blink, false);
					eatFood(topFoods, 50, 0);
				}
				Time.sleep(100, 300);
			}
			puzzlePoints.clear();
		} else if (isBoss("Warped gulega") || getNpcInRoom("Warped gulega") != null) {
			setBoss("Warped Gulega", true, 0);
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.SLASH;
			NPC gulega = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((gulega = bossSet(gulega)) == null)
					continue;
				if (gulega.getAnimation() == 15004) {
					if (health() > 1) {
						final Tile start = myLoc();
						final Timer graphicTimer = new Timer(Random.nextInt(1150, 1250));
						secondaryStatus = "Analyzing Boss attack data...";
						while (gulega.getAnimation() == 15004 && atLoc(start)) {
							// if (gulega.getPassiveAnimation() == -1)
							// break;
							if (!graphicTimer.isRunning()) {
								secondaryStatus = "Special attack detected - Ruuun!";
								if (walkHop(myLoc(), 3)) {
									secondaryStatus = "";
									break;
								}
							}
							Time.sleep(20, 100);
						}
						secondaryStatus = "";
					}
				} else
					eatFood(topFoods, 50, 0);
				attackBoss(gulega, true);
				Time.sleep(100, 200);
			}
		} else if (isBoss("Dreadnaut") || getNpcInRoom("Dreadnaut") != null) {
			setBoss("Dreadnaut", false, 0);
			swapStyle(Style.MAGIC);
			setPrayer(true, Style.MELEE, true);
			NPC dreadnaut = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((dreadnaut = bossSet(dreadnaut)) == null)
					continue;
				eatFood(topFoods, 50, 60);
				attackBoss(dreadnaut, true);
				setPrayer(true, Style.MELEE, true);
				Time.sleep(200, 400);
			}
		} else if (isBoss("Hope Devourer") || getNpcInRoom("Hope devourer") != null) {
			setBoss("Hope Devourer", true, 0);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			prayTimer = new Timer(Random.nextInt(4000, 6000));
			NPC devourer = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((devourer = bossSet(devourer)) == null)
					continue;
				if (devourer.getMessage() != null) {
					if (setPrayersOff()) {
						attackBoss(devourer, true);
						Time.sleep(600, 1200);
					}
				} else if (prayTimer != null && devourer.getAnimation() == 14460) {
					prayTimer = null;
					if (!eatFood(topFoods, 50, 40))
						Time.sleep(500, 800);
				} else {
					setPrayer(true, Style.MELEE, true);
					eatFood(topFoods, 50, 0);
					attackBoss(devourer, true);
				}
				Time.sleep(200, 400);
			}
		} else if (isBoss("World-gorger") || getNpcInRoom("World-gorger shukarhazh") != null) {
			setBoss("World-gorger", true, 0);
			final String[] eyeNames = { "warrior-eye", "ranger-eye", "mage-eye" };
			eyeNames[primaryStyle.idx] = null;
			tempMode = Melee.STAB;
			setPrayer(true, Style.MAGIC, true);
			NPC worldGorger = getNpcInRoom(bossName);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((worldGorger = bossSet(worldGorger)) == null)
					continue;
				final boolean noneDown = getNpcsInRoom(eyeNames).length == 2;
				final int hp = health();
				NPC eye = null;
				if (noneDown || hp > 60 || invContains(goodFoods)) {
					for (int I = 0; I < eyeNames.length; ++I) {
						if (eyeNames[I] == null)
							continue;
						if ((eye = getGoodNpc(eyeNames[I])) == null)
							continue;
						final Style style = styleOf(I);
						if (combatStyle == style)
							break;
						eye = null;
					}
					if (eye == null)
						for (int I = 0; I < eyeNames.length; ++I) {
							if (eyeNames[I] == null)
								continue;
							if ((eye = getGoodNpc(eyeNames[I])) == null)
								continue;
							final Style style = styleOf(I);
							if ((style.enabled && swapStyle(style)) || noneDown || hp > 70)
								break;
							eye = null;
						}
				}
				if (eye == null) {
					swapStyle(primaryStyle);
					attackBoss(worldGorger, true);
				} else
					attackNpc(eye);
				eatFood(topFoods, 55, 65);
				Time.sleep(200, 400);
			}
		} else if (isBoss("Kal'Ger") || getObjInRoom(56038) != null) {
			setBoss("Kal'Ger the Warmonger", false, 1);
			Style kStyle = kalgerStyle();
			kalgerState(kStyle);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			NPC kalger = getNpcInRoom(bossName);
			if (bossStage == 0) {
				status = "Waiting for the cutscene to finish...";
				setRetaliate(false);
				final Timer cutTimer = new Timer(Random.nextInt(15000, 20000));
				while (kalger != null && !reachable(kalger, true)) {
					if (failSafe())
						return;
					if (!cutTimer.isRunning())
						break;
					Time.sleep(300, 600);
				}
				bossStage = 1;
			}
			status = "Killing the Warmonger! (Stage " + bossStage + " of 6)";
			if (kalger != null && bossId < 1)
				bossId = getId(kalger);
			while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((kalger = bossSet(kalger)) == null)
					continue;
				final String shout = kalger.getMessage();
				if (shout != null && shout.startsWith("GRRR") && health() > Random.nextInt(15, 20)) {
					secondaryStatus = "Disabling prayers too cool him off";
					setPrayersOff();
					prayTimer = new Timer(Random.nextInt(15000, 25000));
				} else if (kalger.getAnimation() == 14968 || distTo(kalger) < 3)
					walkTo(reflect(kalger.getLocation(), myLoc(), 1), 1);
				final int currStage = bossStage;
				bossStage = (getId(kalger) - bossId + 15) / 15;
				if (bossStage != currStage) {
					status = "Killing the Warmonger! (Stage " + bossStage + " of 6)";
					kalgerState(kStyle = kalgerStyle());
				} else if (kalger.getAnimation() == 1) { // Get flying anim
					protection = null;
					setPrayersOff();
				} else if (protection != null) {
					if (prayTimer != null && health() < Random.nextInt(15, 25))
						prayTimer = null;
					if (prayTimer == null || !prayTimer.isRunning()) {
						secondaryStatus = "";
						setPrayer(true, protection, true);
					} else
						setPrayer(true, protection, false);
				}
				if ((combatStyle == Style.MELEE || kStyle == Style.MELEE) && !reachable(kalger, true)) {
					if (player().getAnimation() != 13493) {
						final NPC orb = getNpcInRoom(12842);
						secondaryStatus = "Teleporting over the rift";
						if (orb != null && !atLoc(orb))
							walkTo(orb.getLocation(), 0);
						else
							topUp();
					}
				} else {
					setPrayer(true, protection, true);
					eatFood(topFoods, 50, 0);
					attackBoss(kalger, true);
				}
				Time.sleep(200, 400);
			}
		} else if (!unknownBossFight())
			return;
		finish = true;
	}

	public class Strat extends Strategy implements Runnable {
		@Override
		public boolean validate() {
			return Game.getClientState() == Game.INDEX_MAP_LOADED;
		}

		@Override
		public void run() {
			if (pauseScript()) {
				stop();
				return;
			}
			if (welcomeBack && failLogin()) {
				Time.sleep(100);
				return;
			}
			if (showPartySettings) {
				Time.sleep(200);
				return;
			}
			if (developer && ++currLoops > 100) {
				severe("Looped");
				currLoops = 0;
			}
			try {
				if (Game.getPlane() == 1) {
					status = "Jumping down the stairs...";
					inDungeon = false;
					partyFormed = false;
					if (doObjAction(SceneEntities.getNearest(END_STAIRS), "Jump-down")) {
						if (waitToStop(false))
							waitToStop(true);
						open(Tabs.INVENTORY);
					}
				} else if (inDungeon) {
					getCurrentRoom();
					if (exit) {
						exitDungeon();
						exit = false;
					} else if (finish) {
						finishDungeon();
						finish = false;
					} else if (newDungeon) {
						startDungeon();
						newDungeon = false;
					} else if (settingsFinished)
						if (afking)
							waitForLadder();
						else if (bossFight) {
							puzzleTimer = null;
							safeTile = null;
							if (getObjInRoom(FINISHED_LADDERS) != null)
								finish = true;
							else if (floor == Floor.FROZEN)
								frozenBoss();
							else if (floor == Floor.ABANDONED)
								abandonedBoss();
							else if (floor == Floor.FURNISHED)
								furnishedBoss();
							else if (floor == Floor.OCCULT)
								occultBoss();
							else if (floor == Floor.WARPED)
								warpedBoss();
							bossFight = false;
							retrace = false;
						} else if (explore) {
							exploreRoom();
							reqTimer = null;
							explore = false;
						} else if (open) {
							if (openNextDoor()) {
								if (nearDoor != null && nearDoor.state == State.NEW) {
									nearDoor.state = State.CLEARED;
									puzzleTimer = null;
								}
								explore = true;
							} else
								retrace = true;
							nearDoor = null;
							doorTimer = null;
							open = false;
						} else if (retrace) {
							retraceDungeon();
							retrace = false;
						} else {
							if (!failSafe())
								if (developer)
									secondaryStatus = "We don't know what to do :(";
							if (uncaughtLoops > 10) {
								if (currentRoom != null)
									if (!inRoom(currentRoom) || !inRoom(targetRoom)) {
										currentRoom = null;
										if (getCurrentRoom() != null) {
											setTargetRoom(currentRoom);
											open = true;
											return;
										}
									} else if (uncaughtLoops < 20) {
										if (developer)
											log(Color.WHITE, true, "Failsafe exploring");
										else
											secondaryStatus = "";
										explore = true;
									}
								if (uncaughtLoops > 100) {
									severe("Unable to determine our next move, aborting dungeon.");
									exit = true;
									uncaughtLoops = 0;
									++abortedCount;
									waitForResponse();
								}
							} else if (developer)
								log.severe("Unknown exit");
							++uncaughtLoops;
							Time.sleep(200, 600);
							return;
						}
				} else if (inDungeon()) {
					clearAll();
					status = "Starting a new dungeon!";
					inDungeon = true;
				} else if (SceneEntities.getNearest(ENTRANCE) != null)
					if (isLeader)
						enterDungeon();
					else
						enterDungeonParty();
			} catch (final Exception e) {
				if (e != null && !stopScript)
					if (developer) {
						e.printStackTrace();
						severe("Loop exception " + e.getMessage());
					} else
						log(LR, false, "Attempting to recover from an unexpected error");
			}
			secondaryStatus = "";
			unreachable = false;
			uncaughtLoops = 0;
		}
	}

	private void clearAll() {
		startRoom = null;
		currentRoom = null;
		newRoom = null;
		targetRoom = null;
		gateRoom = null;
		groupRoom = null;
		bossRoom = null;
		nearMonster = null;
		nearDoor = null;
		bossDoor = null;
		oldDoor = null;
		safeTile = null;
		nearItem = null;
		bossTimer = null;
		prayTimer = null;
		puzzleTimer = null;
		leaveTimer = null;
		roomNodes = null;
		goodPath.clear();
		deathPath.clear();
		goodPath.clear();
		bounds.clear();
		rooms.clear();
		badTiles.clear();
		puzzlePoints.clear();
		perm.clear();
		roomFlags.clear();
		keyStore.clear();
		bossHp = 100;
		Room.count = 0;
		roomNumber = 0;
		bossId = 0;
		restartCount = 0;
		bossStage = 0;
		animBreaker = null;
		tempMode = Melee.NONE;
		bossName = null;
		secondaryStatus = "";
		aborted = false;
		retrace = false;
		explore = false;
		open = false;
		bossFight = false;
		bossFinished = false;
		messageBreaker = null;
		finish = false;
		exit = false;
		skipRoom = false;
		skipDoorFound = false;
		isDead = false;
		outOfAmmo = false;
		newDungeon = true;
		cookReady = false;
		roomSwitch = false;
		spawnRoom = false;
		disableBossPath = false;
		getFeathers = false;
		failTimer = new Timer(Random.nextInt(360000, 480000));
		idleTimer = new Timer(0);
		combatStyle = styleOf(primaryStyle.idx);
	}

	private boolean craftCosmics() {
		boolean crafting = false;
		secondaryStatus = "Crafting Cosmic runes";
		while (invCount(true, COSMIC_RUNES) < random(5, 30) && invContains(ESSENCE)) {
			if (failCheck())
				return false;
			makeSpace(false);
			if (player().getAnimation() != -1)
				crafting = true;
			else if (crafting) {
				Time.sleep(400, 600);
				crafting = player().getAnimation() != -1;
			} else if (Widgets.get(956).validate())
				walkToMap(getObjInRoom(RUNECRAFTING_ALTARS), 1);
			else {
				final WidgetChild rune = Widgets.get(944, 24);
				if (rune != null && rune.getChildId() == 564) {
					final WidgetChild click = Widgets.get(944, 25);
					if (click != null && click.interact("Make 100"))
						waitToStop(false);
				} else if (doObjAction(getObjInRoom(RUNECRAFTING_ALTARS), "Imbue-Other-Runes")) {
					waitToStop(false);
					if (!Widgets.get(944).validate())
						waitToStop(false);
				}
			}
			Time.sleep(200, 400);
		}
		secondaryStatus = "";
		return true;
	}

	private boolean escapeBoss() {
		log(LB, false, "Teleporting out of the boss battle to stock back up!");
		dropItem(GGS);
		if (teleGatestone()) {
			if (SceneEntities.getNearest(FINISHED_LADDERS) == null)
				teleHome(true);
			bossFight = false;
			return open = true;
		}
		return false;
	}

	private boolean escapeReady() {
		return bossHp > 1 && !isBoss("Icefiend") && health() < random(10, 60) && !invContains(goodFoods)
				&& readyToCook() && castable(Dungeoneering.GATESTONE_TELEPORT)
				&& SceneEntities.getNearest(FINISHED_LADDERS) == null;
	}

	private boolean fightMonsters() {
		boolean activated = false;
		boolean genericFight = false;
		if ((isRushing || !Option.DEAD_END.enabled()) && targetRoom.state == State.FINISHED
				&& !status.startsWith("Backtracking") && !status.contains("old room"))
			return true;
		unreachable = false;
		strongestMonster = null;
		if ((nearMonster = NPCs.getNearest(monster)) != null) {
			crossTheChasm(nearMonster.getLocation());
			secondaryStatus = "Clearing the room of monsters";
			final boolean stayOff = status.startsWith("Puzzle room: Follow");
			while (NPCs.getNearest(monster) != null) {
				if (failSafe())
					return false;
				if (!isGoodMonster(nearMonster))
					if (!genericFight) {
						if ((nearMonster = NPCs.getNearest(primaryMonster)) == null)
							if ((nearMonster = NPCs.getNearest(secondaryMonster)) == null) {
								genericFight = true;
								nearMonster = NPCs.getNearest(monster);
							}
					} else
						nearMonster = NPCs.getNearest(monster);
				if (unreachable) {
					topUp();
					unreachable = false;
				} else if (attackNpc(nearMonster))
					getBestStyle(nearMonster);
				else
					antiban();
				if (monsterPrayer() != null) {
					if (setPrayer(false, protection, strongestMonster != null && getGoodNpc(strongestMonster) != null))
						activated = true;
				} else if (activated)
					activated = !setPrayersOff();
				if (stayOff && isObjectAt(myLoc(), ACTIVE_PADS))
					walkAdjacentTo(myLoc(), 2);
				eatFood(goodFoods, 45, 55);
				Time.sleep(200, 400);
			}
			setPrayersOff();
			nearMonster = null;
			final double mDist = Random.nextInt(3, 7);
			while (NPCs.getNearest(dying) != null) {
				if (failSafe())
					return false;
				if (invCount(false) < 27 || !topUp()) {
					if (getGoodItem() != null && distTo(nearItem.getLocation()) < mDist) {
						if (pickUpItem(nearItem))
							waitToStop(false);
					} else if (!topUp() || nearItem == null)
						Time.sleep(300, 600);
				} else
					Time.sleep(300, 600);
			}
			setPrayersOff();
			secondaryStatus = "";
		}
		return true;
	}

	private boolean goHome() {
		return readyToCraft() || readyToCook();
	}

	private boolean isBoss(String... names) {
		if (bossName != null)
			for (final String name : names)
				if (bossName.contains(name))
					return true;
		return false;
	}

	private void logInnOut() {
		logout();
		login();
		while (!dungeonLoaded() && Game.getPlane() != 1) {
			if (pauseScript())
				return;
			Time.sleep(500, 1000);
		}
	}

	private boolean login() {
		while (Game.getClientState() != Game.INDEX_MAP_LOADED) {
			if (pauseScript())
				return false;
			if (Game.getClientState() == Game.INDEX_LOGIN_SCREEN) {
				severe("Taken to the login screen, returning control to the client to log in.");
				return false;
			}
			if (Game.getClientState() == Game.INDEX_LOBBY_SCREEN)
				if (clickComponent(Widgets.get(906, 186), ""))
					Time.sleep(1400, 2400);
			Time.sleep(200, 800);
		}
		return true;
	}

	private void logout() {
		while (Game.isLoggedIn()) {
			if (pauseScript())
				return;
			if (open(Tabs.LOGOUT) && clickComponent(Widgets.get(182, 2), "Exit"))
				Time.sleep(600, 1000);
			Time.sleep(200, 400);
		}
	}

	private boolean planToCook() {
		if (!Option.MAKE_FOOD.enabled())
			return false;
		getFishAndLogs();
		return fish != null
				&& logs != null
				&& hasCoins((invContains(logs.logId) ? 0 : logs.cost)
						+ (invContains(fish.rawId) ? 0 : fish.price(Random.nextInt(2, 4))));
	}

	private boolean planToTele() {
		return isBoss("Bal'lak") || (getNpcInRoom("Luminescent") == null && planToCook());
	}

	private boolean prepareSupplies() {
		if ((!isLeader || invContains(GGS)) && goHome()) {
			log(LB, false, "Returning home to prepare supplies before taking on the boss");
			setTargetRoom(currentRoom);
			teleHome(true);
			return true;
		}
		if (!inRoom(bossRoom) && planToTele())
			makeGatestone(false);
		return false;
	}

	private boolean prepareFood() {
		status = "Cook: " + fish + " on " + logs;
		log(Color.WHITE, false, "Cooking " + fish + " on " + logs + " logs");
		idleTimer = new Timer(0);
		lastMessage = "";
		for (int c = 0; c < 2; ++c) {
			omNomNom();
			if (!invContains(fish.rawId)) {
				final SceneObject fire = getObjInRoom(FIRES);
				if (invSaleAfter() > Random.nextInt(25, 27)
						|| !hasCoins((fire != null ? logs.cost : 0) + fish.price(Random.nextInt(1, 3))))
					break;
			}
			if (hasCoins(fish.cost) || invHasSale())
				while (!lastMessage.contains(" space") && !lastMessage.contains("afford")
						&& !lastMessage.contains("enough money")) {
					if (failCheck())
						return false;
					failWidgets();
					if (!shopFood())
						break;
				}
			idleTimer = new Timer(0);
			i: while (invContains(fish.rawId)) {
				if (failCheck())
					return false;
				failWidgets();
				final SceneObject fire = getObjInRoom(FIRES);
				final Widget levelUp = Widgets.get(1186);
				if (invFull())
					break;
				if (levelUp != null && levelUp.validate())
					spaceContinue();
				else if (fire == null) {
					if (!invContains(logs.logId)) {
						getItemInRoom(startRoom, UNLIT_LOGS);
						if (nearItem != null) {
							if (pickUpItem(nearItem))
								waitToStop(false);
						} else if (!makeSpace(false) || !shopFood() || !invContains(logs.logId))
							return false;
					} else if (lastMessage.startsWith("You can't light")) {
						walkAdjacentTo(myLoc(), 2);
						lastMessage = "";
					}
					final SceneObject s = getObjInRoom(SUMMONING_OBELISKS);
					if (isEdgeTile(myLoc()) || ((s == null || distTo(s) == 2) && distTo(startRoom.getCenter()) == 1))
						walkAdjacentTo(myLoc(), Random.nextInt(1, 3));
					else if (doItemAction(invItem(logs.logId), "Light"))
						for (int d = 0; d < 10; ++d) {
							if (getObjInRoom(FIRES) != null)
								continue i;
							if (player().getAnimation() != -1)
								d = 0;
							Time.sleep(200, 300);
						}
				} else if (player().getAnimation() != 897)
					if (Widgets.get(944, 78).validate()) {
						final boolean makeX = invCount(true, fish.rawId) > 10;
						if (clickComponent(Widgets.get(944, 78), "Make " + (makeX ? "X" : "10"))) {
							if (makeX) {
								for (int d = 0; d < 10; ++d) {
									if (!Widgets.get(944).validate())
										break;
									Time.sleep(100, 150);
								}
								Time.sleep(100, 300);
								Keyboard.sendText(String.valueOf(Random.nextInt(invCount(true, fish.rawId), 100)), true);
							}
							waitToStop(false);
						}
					} else if (useItem(fish.rawId, fire)) {
						if (distTo(fire) > 1)
							waitToStop(false);
						forSleep(Widgets.get(944, 78), true);
						continue;
					}
				Time.sleep(50, 150);
			}
		}
		omNomNom();
		return true;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean prepareForBoss() {
		if (bossTimer == null) {
			roomSwitch = false;
			bossTimer = new Timer(0);
		}
		switch (floor) {
		case FROZEN:
			if (getObjInRoom(51300) != null)
				if (!inTrueCombat() && Walking.getEnergy() < Random.nextInt(85, 90))
					restTo(100);
			break;
		case ABANDONED:
			if (getNpcInRoom("Bulwark beast") != null)
				if (bossName == null) {
					bossStage = 1;
					ridItem(PICKAXE, "Wield");
				}
			break;
		case FURNISHED:
			if (getObjInRoom(SAGITTARE_FLOOR) != null) {
				if (combatStyle == Style.RANGE)
					if (swapAlternative())
						log(LB, false, "Swapped our attack style for Sagittare");
					else {
						severe("We don't have a way to kill Sagittare, aborting dungeon!");
						++controlledAborts;
						exit = true;
						return false;
					}
			} else if (getNpcInRoom("Stomp") != null)
				waitForResponse();
			else if (getNpcInRoom("Riftsplitter") != null) {
				if (!swapStyle(Style.MAGIC) && combatLevel < 50) {
					severe("Har'lak is too hard hard to take on at our combat level, trying another dungeon");
					++controlledAborts;
					exit = true;
					return false;
				}
			} else if (getNpcInRoom("Rammernaut") != null)
				swapStyle(Style.MAGIC);
			else if (getNpcInRoom("Khighorahk") != null)
				if (!inTrueCombat() && Walking.getEnergy() < Random.nextInt(85, 90))
					restTo(100);
			break;
		case OCCULT:
			if (getObjInRoom(NECROLORD_FLOOR) != null) {
				if (combatStyle == Style.MELEE && !swapAlternative()) {
					severe("We can't kill this boss with melee, aborting dungeon");
					++controlledAborts;
					exit = true;
					return false;
				}
			} else if (getNpcInRoom("Runebound") != null) {
				if (styleCount() > 1)
					disRobe();
			} else if (getNpcInRoom("Gravecreeper") != null)
				if (Melee.SLASH.enabled())
					swapStyle(Style.MELEE);
				else if (Mage.FIRE.enabled())
					swapStyle(Style.MAGIC);
				else if (combatStyle == Style.RANGE)
					swapAlternative();
			break;
		case WARPED:
			if (getNpcInRoom("Dreadnaut") != null)
				swapStyle(Style.MAGIC);
			break;
		}
		updateFightMode();
		if ((gateRoom == null || (!gateRoom.equals(startRoom) && !gateRoom.equals(currentRoom)))
				&& (planToTele() || getNpcInRoom("Pummeller") != null))
			makeGatestone(true);
		open(Tabs.INVENTORY);
		idleTimer = new Timer(0);
		secondaryStatus = "";
		return true;
	}

	private boolean previewBoss() {
		if (floor == Floor.FURNISHED) {
			if (getObjInRoom(SAGITTARE_FLOOR) != null) {
				if (combatStyle == Style.RANGE && !Style.MELEE.enabled && !Style.MAGIC.enabled) {
					severe("We don't have a way to kill Sagittare, aborting dungeon!");
					++controlledAborts;
					exit = true;
					return false;
				}
			} else if (getNpcInRoom("Riftsplitter") != null)
				if (combatLevel < 50 && !Style.MAGIC.enabled) {
					severe("Har'lak is too hard hard to take on at our combat level, trying another dungeon");
					++controlledAborts;
					exit = true;
					return false;
				}
		} else if (floor == Floor.OCCULT)
			if (getObjInRoom(NECROLORD_FLOOR) != null)
				if (combatStyle == Style.MELEE && !Style.RANGE.enabled && !Style.MAGIC.enabled) {
					severe("We can't kill this boss with melee, aborting dungeon");
					++controlledAborts;
					exit = true;
					return false;
				}
		return true;
	}

	private boolean readyToCraft() {
		return saving(bounds, ESSENCE) && invCount(true, COSMIC_RUNES) < 5
				&& (invCount(true, ESSENCE) > 5 || hasCoins(Random.nextInt(600, 1500)));
	}

	private boolean sagaMemories() {
		SceneObject memory = getObjInRoom(SAGA_MEMORIES);
		if (!developer && memory != null) {
			waitForResponse();
			final String oldStatus = secondaryStatus;
			secondaryStatus = "Recovering a Saga memory";
			while ((memory = getObjInRoom(SAGA_MEMORIES)) != null) {
				if (failSafe())
					return false;
				if (secondaryStatus.startsWith("Attempting"))
					break;
				if (memory.getModel() == null)
					break;
				// if (lastMessage.contains("memory"))
				// break;
				if (memory.getDefinition() == null)
					break;
				smartSleep(doObjAction(memory, "Recover-memory"), true);
			}
			secondaryStatus = oldStatus;
		}
		return true;
	}

	private boolean switchRing(Style style) {
		if (ringStyle != null && style != null && ringStyle != style && !inTrueCombat()) {
			final String oldStatus = secondaryStatus;
			secondaryStatus = "Quickswitching your ring to " + style;
			for (int c = 0; c < 5; ++c) {
				if (failBasic() || inTrueCombat())
					return false;
				if (clickRing("Quick-switch"))
					for (int d = 0; d < 10; ++d) {
						if (ringStyle == null || ringStyle == style) {
							secondaryStatus = oldStatus;
							return true;
						}
						Time.sleep(100, 200);
					}
				Time.sleep(100, 200);
			}
			secondaryStatus = oldStatus;
		}
		return false;
	}

	private boolean genericBossFight() {
		final NPC boss = NPCs.getNearest(bossMonster);
		while (SceneEntities.getNearest(FINISHED_LADDERS) == null) {
			if (failSafe())
				return false;
			if (unreachable) {
				if (!topUp())
					Time.sleep(500, 1000);
				unreachable = false;
			}
			attackBoss(boss, false);
			if (eatFood(goodFoods, 50, 60))
				Time.sleep(50, 200);
			else
				Time.sleep(400, 800);
		}
		return finish = true;
	}

	private boolean unknownBossFight() {
		setBoss("an unknown boss...", false, 2);
		setPrayer(true, null, false);
		return genericBossFight();
	}

	private void waitForLadder() {
		if (bossFinished) {
			finish = true;
			return;
		}
		if (leaveTimer != null && !leaveTimer.isRunning()) {
			exit = true;
			return;
		}
		status = "Waiting for the finish ladder...";
		if (invContains(GGS))
			ridItem(GGS, "Drop");
		else if (idleTimer.getElapsed() > random(90000, 120000) || randomActivate(random(3, 33))) {
			antibanEvents();
			idleTimer = new Timer(0);
			Time.sleep(random(100, 1500));
			currLoops = 0;
		} else
			Time.sleep(1000, 4000);
	}

	private enum DoorType {
		BLOCKED("Blocked", null, null), GUARDIAN("Guardian", "Enter", "Guardian door"), KEY("Key", "Unlock", "Door"), PUZZLE(
				"Puzzle", "n", "Door"), SKILL("Skill", null, null), STANDARD("Standard", "en", "Door"), UNLOCKED(
				"Unlocked", "Open", " door");

		private final String action, doorName, name;

		private DoorType(String name, String action, String doorName) {
			this.name = name;
			this.action = action;
			this.doorName = doorName;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private class Door {
		final int index;
		final Tile loc;

		boolean blocked, hasKey;
		int lockId, typeIdx;
		Tile block;
		DoorType type;
		State state;

		Door(DoorType type, Tile node, Tile block, int index, int lockId) {
			this.type = type;
			this.loc = node;
			this.block = block;
			this.state = State.NEW;
			this.index = index;
			this.lockId = lockId;
			this.blocked = type == DoorType.KEY || type == DoorType.BLOCKED;
			this.hasKey = lockId == -1;
		}

		private boolean isGood() {
			return hasKey && state == State.NEW;
		}
	}

	private Door addDoor(DoorType type, int idx, int lockId, Tile... tiles) {
		final Door door = new Door(type, tiles[0], tiles.length > 1 ? tiles[1] : null, idx, lockId);
		targetRoom.doors.add(door);
		return door;
	}

	private ArrayList<Door> generateGoodPaths() {
		goodPath.clear();
		updateLocks();
		for (final Room r : rooms)
			if (r.hasGoodDoor()) {
				goodPath.addAll(generatePath(r));
				break;
			}
		return goodPath;
	}

	private boolean getBestDoor() {
		double dist = 99.99;
		Door nextDoor = null;
		final Tile start = myLoc();
		nearDoor = null;
		for (final Door door : targetRoom.getGoodDoors()) {
			final double dDist = Calculations.distance(start, door.loc);
			if (dDist < dist) {
				final SceneObject doorObj = SceneEntities.getAt(door.loc);
				if (doorObj != null) {
					nextDoor = door;
					if (dDist < 5)
						break;
					dist = dDist;
				}
			}
		}
		if (nextDoor != null) {
			nearDoor = nextDoor;
			return true;
		}
		generateGoodPaths();
		for (final Door door : goodPath)
			if (targetRoom.contains(door)) {
				nearDoor = door;
				return true;
			}
		final boolean verifyBoss = disableBossPath && bossDoor != null;
		for (final Door door : targetRoom.getDoors(State.CLEARED))
			if ((!verifyBoss || !bossDoor.equals(door)) && (oldDoor == null || !door.equals(oldDoor))) {
				final Room room = targetRoom.parent;
				if (room != null && (room.hasDoor(State.NEW, State.CLEARED))) {
					if (developer && bossRoom == null && !isGoodDoor())
						severe("There doesn't seem to be a good door left to open.. Abort?");
					nearDoor = door;
					return true;
				}
			}
		return false;
	}

	private boolean getNewDoors() {
		final int[] puzzleDoors = floor != Floor.WARPED ? PUZZLE_DOORS : WARPED_PUZZLE_DOORS;
		final Tile[] blockNodes = targetRoom.getBlockNodes(), doorNodes = targetRoom.getDoorNodes();
		final boolean checkSkillDoors = !freeVersion && aComplexity > 4;
		for (int I = 0; I < blockNodes.length; ++I) {
			final Tile loc = doorNodes[I], node = blockNodes[I];
			final SceneObject block = SceneEntities.getAt(node);
			if (block == null)
				continue;
			if (keyDoorMatch(block.getId()))
				addDoor(DoorType.KEY, I, block.getId(), loc, node);
			else if (checkSkillDoors) {
				int di;
				if ((di = doorIndex(block.getId(), BLOCK_DOORS)) != -1) {
					final Door d = addDoor(DoorType.BLOCKED, I, -1, loc, node);
					if (di < 5) {
						if (di == 4 && !Option.PRAYER.enabled()) {
							d.state = State.FINISHED;
							skipDoorFound = true;
						} else
							d.typeIdx = di;
					} else if (memberWorld) {
						if (di == 6 && !Option.SUMMONING.enabled()) {
							d.state = State.FINISHED;
							skipDoorFound = true;
						} else
							d.typeIdx = di;
					} else
						d.state = State.FINISHED;
				}
			}
		}
		for (int I = 0; I < doorNodes.length; ++I) {
			if (targetRoom.getDoor(I) != null)
				continue;
			final Tile node = doorNodes[I];
			final SceneObject door = SceneEntities.getAt(node);
			if (door == null)
				continue;
			final int doorId = door.getId();
			if (keyDoorMatch(doorId - floor.diff))
				addDoor(DoorType.KEY, I, doorId - floor.diff, node, blockNodes[I]);
			else if (intMatch(doorId, BASIC_DOORS)) {
				if (nearDoor != null && Calculations.distance(nearDoor.loc, node) < 4)
					targetRoom.backDoor = node;
				else
					addDoor(DoorType.STANDARD, I, -1, node);
			} else if (intMatch(doorId, GUARDIAN_DOORS))
				addDoor(DoorType.GUARDIAN, I, -1, node);
			else if (checkSkillDoors) {
				final int di = doorIndex(doorId, SKILL_DOORS);
				if (di != -1) {
					final Door d = addDoor(DoorType.SKILL, I, -1, node);
					if (di == 0 && !Option.STRENGTH.enabled()) {
						d.state = State.FINISHED;
						skipDoorFound = true;
					} else if (memberWorld || di < 5)
						d.typeIdx = di;
					else
						d.state = State.FINISHED;
				} else if (intMatch(doorId, puzzleDoors))
					addDoor(DoorType.PUZZLE, I, -1, node);
			}
		}
		if (nearDoor != null) {
			nearDoor.state = State.CLEARED;
			oldDoor = nearDoor;
		}
		return targetRoom.hasDoor(State.NEW);
	}

	private boolean isGoodDoor() {
		for (final Room r : rooms)
			if (r.hasGoodDoor())
				return true;
		return false;
	}

	private Tile reachDoor() {
		return nearDoor == null ? null : nearDoor.block != null ? nearDoor.block : nearDoor.loc;
	}

	private boolean updateDoors() {
		boolean updated = false;
		for (final Door d : targetRoom.getDoors(DoorType.STANDARD)) {
			final Tile dBlock = targetRoom.getBlockNodes()[d.index];
			final SceneObject block = SceneEntities.getAt(dBlock);
			if (block != null && block.getType() == SceneEntities.TYPE_INTERACTIVE) {
				final int bId = block.getId();
				if (keyDoorMatch(bId)) {
					d.type = DoorType.KEY;
					d.block = dBlock;
					d.blocked = true;
					d.hasKey = false;
					d.lockId = bId;
					updated = true;
				} else if (aComplexity > 4) {
					final int di = doorIndex(block.getId(), BLOCK_DOORS);
					if (di == -1)
						continue;
					d.type = DoorType.BLOCKED;
					if (di < 5) {
						if (di == 4 && !Option.PRAYER.enabled()) {
							d.state = State.FINISHED;
							skipDoorFound = true;
						} else {
							d.typeIdx = di;
							d.block = dBlock;
							d.blocked = true;
						}
					} else if (memberWorld) {
						if (di == 6 && !Option.SUMMONING.enabled()) {
							d.state = State.FINISHED;
							skipDoorFound = true;
						} else {
							d.typeIdx = di;
							d.block = dBlock;
							d.blocked = true;
						}
					} else
						d.state = State.FINISHED;
					updated = true;
				}
			} else {
				final SceneObject door = SceneEntities.getAt(d.loc);
				if (door == null)
					continue;
				final int doorId = door.getId();
				if (intMatch(doorId, BASIC_DOORS))
					continue;
				if (intMatch(doorId, GUARDIAN_DOORS)) {
					updated = true;
					d.type = DoorType.GUARDIAN;
				} else if (aComplexity > 4) {
					final int di = doorIndex(doorId, SKILL_DOORS);
					if (di != -1) {
						d.type = DoorType.SKILL;
						if (di == 0 && !Option.STRENGTH.enabled()) {
							d.state = State.FINISHED;
							skipDoorFound = true;
						} else if (memberWorld || di < 5)
							d.typeIdx = di;
						else
							d.state = State.FINISHED;
						updated = true;
					}
				}
			}
		}
		return updated;
	}

	private static class Room {
		private static int count = 0;
		final int i, j, x, y, nx, ny, index;
		private boolean hasAltar, hasChasm, hasPuzzle, hooded;
		private boolean isSkippable, isUnbacktrackable, puzzlesFinished;
		private final ArrayList<Door> doors;
		private final Door entryDoor;
		private State state;
		private Tile backDoor;
		private final Room parent;

		Room(int i, int j, int x, int y, Door entry) {
			this.i = i;
			this.j = j;
			this.x = x;
			this.y = y;
			this.nx = x + 15;
			this.ny = y + 15;
			this.doors = new ArrayList<Door>();
			this.state = State.NEW;
			this.backDoor = null;
			this.entryDoor = entry;
			this.hasAltar = true;
			this.hooded = shadowHooded ? true : false;
			this.parent = targetRoom;
			this.index = ++count;
		}

		private boolean contains(ArrayList<Door> doors) {
			for (final Door d : doors)
				if (contains(d.loc))
					return true;
			return false;
		}

		private boolean contains(Locatable l) {
			return l != null && contains(l.getLocation());
		}

		private boolean contains(Door d) {
			return d != null && contains(d.loc);
		}

		private boolean contains(Tile t) {
			return t.getX() >= x && t.getY() >= y && t.getX() <= nx && t.getY() <= ny;
		}

		private boolean equals(Room room) {
			return room != null && this.index == room.index;
		}

		private ArrayList<Door> paintDoors() {
			final ArrayList<Door> all = new ArrayList<Door>();
			all.addAll(doors);
			all.add(entryDoor);
			return all;
		}

		private Tile[] getBlockNodes() {
			return new Tile[] { new Tile(x + 1, y + 8, 0), new Tile(x + 8, y + 14, 0), new Tile(x + 14, y + 8, 0),
					new Tile(x + 8, y + 1, 0) };
		}

		private Tile getCenter() {
			return new Tile(x + 8, y + 8, 0);
		}

		private Tile[] getDoorNodes() {
			return new Tile[] { new Tile(x + 0, y + 8, 0), new Tile(x + 8, y + 15, 0), new Tile(x + 15, y + 8, 0),
					new Tile(x + 8, y + 0, 0) };
		}

		private Door getDoor(int idx) {
			for (final Door d : this.doors)
				if (d != null && d.index == idx)
					return d;
			return null;
		}

		private ArrayList<Door> getDoors(State... states) {
			final ArrayList<Door> list = new ArrayList<Door>();
			for (final Door d : this.doors)
				for (final State s : states)
					if (d.state == s)
						list.add(d);
			return list;
		}

		private ArrayList<Door> getDoors(DoorType... types) {
			final ArrayList<Door> list = new ArrayList<Door>();
			for (final Door d : this.doors)
				for (final DoorType t : types)
					if (d.type == t)
						list.add(d);
			return list;
		}

		private ArrayList<Door> getGoodDoors() {
			final ArrayList<Door> list = new ArrayList<Door>();
			for (final Door d : this.doors)
				if (d.isGood())
					list.add(d);
			return list;
		}

		private Door getNearestDoor() {
			Door door = null;
			double dist = 99.99;
			final Tile me = myLoc();
			for (final Door d : this.doors) {
				final double dDist = Calculations.distance(me, d.loc);
				if (dDist < dist) {
					dist = dDist;
					door = d;
				}
			}
			return door;
		}

		private Tile nearestCornerTo(Tile t) {
			final int tx = t.getX() > x ? nx : x;
			final int ty = t.getY() > y ? ny : y;
			return new Tile(tx, ty, 0);
		}

		private Tile nearestDoorTo(Tile t) {
			double dist = Integer.MAX_VALUE;
			Tile near = null;
			for (final Door d : this.doors) {
				final double dDist = Calculations.distance(t, d.loc);
				if (dDist < dist) {
					near = d.loc;
					dist = dDist;
				}
			}
			if (near == null && developer)
				severe("UNABLE TO FIND NEAREST DOOR");
			return near;
		}

		private boolean hasDoor(DoorType... types) {
			for (final Door d : this.doors)
				for (final DoorType t : types)
					if (d.type == t)
						return true;
			return false;
		}

		private boolean hasDoor(State... states) {
			for (final Door d : doors)
				for (final State s : states)
					if (d.state == s)
						return true;
			return false;
		}

		private boolean hasGoodDoor() {
			if (this.state != State.FINISHED)
				for (final Door d : this.doors)
					if (d.isGood())
						return true;
			return false;
		}

		private Tile[] tiles() {
			final Tile[] roomTiles = new Tile[256];
			int idx = 0;
			for (int i = x; i < nx; ++i)
				for (int j = y; j < ny; ++j)
					roomTiles[++idx] = new Tile(i, j, 0);
			return roomTiles;
		}

		private void setFlags() {
			final ArrayList<Tile> blocks = new ArrayList<Tile>(100);
			try {
				final int block[][] = Walking.getCollisionFlags(Game.getPlane());
				final int sX = x - Game.getBaseX(), sY = y - Game.getBaseY();
				final int eX = nx - Game.getBaseX() + 1, eY = ny - Game.getBaseY() + 1;
				for (int rX = sX; rX < eX; ++rX)
					for (int rY = sY; rY < eY; ++rY)
						if ((block[rX + 1][rY + 1] & 0x1280100) != 0)
							blocks.add(new Tile(rX + Game.getBaseX(), rY + Game.getBaseY(), 0));
			} catch (final Exception e) {
				if (developer)
					severe("Flags exception " + e.toString());
			}
			roomFlags = blocks;
		}
	}

	private boolean checkRoom() {
		skipRoom = false;
		doorTimer = null;
		idleTimer = new Timer(0);
		if (newRoom == null)
			newRoom = targetRoom;
		if (getNewDoors()) {
			generateTimer = new Timer(1000);
			return false;
		}
		final SceneObject bDoor = getObjInRoom(BOSS_DOORS);
		if (bDoor != null) {
			if (bossRoom == null) {
				newRoom.state = State.BOSS;
				bossRoom = newRoom;
				bossDoor = nearDoor;
				bossRoom.backDoor = bDoor.getLocation();
				bossPath = generatePath(bossRoom);
				bossPath.add(nearDoor);
				restartCount = 0;
			}
			if (!previewBoss())
				return false;
			if (!inRoom(bossRoom)) {
				secondaryStatus = "Preparing for the boss battle";
				if ((!skipRoom || !inTrueCombat()) && (floor != Floor.ABANDONED || getNpcInRoom(SKINWEAVER) == null)
						&& !inTrueCombat())
					omNomNom();
				if (!inRoom(bossRoom)) {
					if (!isRushing && isGoodDoor() && !bossPath.isEmpty()) {
						makeGatestone(false);
						setTargetRoom(currentRoom);
						disableBossPath = true;
						return true;
					}
					if (prepareSupplies())
						return true;
				}
			}
			if (!prepareForBoss())
				return true;
			open = false;
			explore = false;
			bossFight = true;
			return retrace = false;
		}
		if (!isRushing && Option.DEAD_END.enabled())
			return false;
		Tile kTile = getKey();
		if (kTile == null)
			for (int c = 0; c < 4; ++c) {
				Time.sleep(200, 300);
				if ((kTile = getKey()) != null)
					break;
				if (c > 1 && isNpcInRoom())
					break;
			}
		if (kTile != null) {
			secondaryStatus = "Grabbing a key from the dead end";
			if (inRoom(targetRoom))
				walkToMap(kTile, 1);
			open(Tabs.INVENTORY);
			Walking.setRun(true);
			skipRoom = true;
			return false;
		}
		if (pickupId > 0)
			if (pickupId == BLOOD_NECKLACE) {
				if (getNpcInRoom(Slayer.EDIMMU.id) != null) {
					secondaryStatus = "Fighting the Edimmu";
					severe(secondaryStatus);
					return false;
				}
			} else if (pickupId == HEXHUNTER_BOW) {
				if (getNpcInRoom(Slayer.SOULGAZER.id) != null) {
					secondaryStatus = "Fighting the Soulgazer";
					severe(secondaryStatus);
					return false;
				}
			} else if (pickupId == SHADOW_SILK_HOOD)
				if (getNpcInRoom(Slayer.NIGHT_SPIDER.id) != null) {
					secondaryStatus = "Fighting the Night Spider";
					severe(secondaryStatus);
					return false;
				}
		secondaryStatus = "Skipping the dead end";
		finishRoom(newRoom);
		skipRoom = true;
		targetRoom.isSkippable = true;
		setTargetRoom(currentRoom);
		newRoom = null;
		return true;
	}

	private void finishOffRooms() {
		for (int i = rooms.size() - 1; i > 0; i--) {
			final Room room = rooms.get(i);
			if (room.state != State.FINISHED && !room.equals(bossRoom) && !room.hasDoor(State.NEW, State.CLEARED))
				finishRoom(room);
		}
	}

	private void finishRoom(Room room) {
		if (room != null) {
			room.state = State.FINISHED;
			if (room.entryDoor != null)
				room.entryDoor.state = State.FINISHED;
			for (final Door d : room.doors)
				d.state = State.FINISHED;
		}
	}

	private boolean getNewRoom() {
		int i = 0, j = 0;
		Room room = null;
		final Tile me = myLoc();
		for (int I = 0; I < roomNodes.length; ++I)
			for (int J = 0; J < roomNodes[I].length; ++J) {
				final Tile t = roomNodes[I][J];
				if (t != null && Calculations.distance(me, t) < 20 && SceneEntities.getAt(t) != null) {
					room = new Room(I, J, t.getX() - 1, t.getY(), nearDoor);
					i = I;
					j = J;
					break;
				}
			}
		if (room == null || getRoomObjs(room, SceneEntities.ALL_FILTER).length < 60)
			return false;
		boolean backFound = false;
		for (final Tile t : room.getDoorNodes()) {
			final SceneObject door = SceneEntities.getAt(t);
			if (door == null)
				return false;
			if (!backFound)
				backFound = intMatch(door.getId(), BACK_DOORS);
		}
		if (backFound) {
			roomNodes[i][j] = null;
			newRoom = room;
			rooms.add(newRoom);
			if (aComplexity > 4 && invCacheContains(GUARD_KEY) && dropItem(GUARD_KEY))
				remove(bounds, GUARD_KEY);
			setTargetRoom(newRoom);
			restartCount = 0;
			currLoops = 0;
			failTimer.reset();
			doorTimer = null;
			return true;
		}
		return false;
	}

	private boolean getStartRoom() {
		final SceneObject[] startObjs = SceneEntities.getLoaded();
		if (startObjs.length > 50) {
			int swX = Integer.MAX_VALUE, swY = Integer.MAX_VALUE;
			for (final SceneObject o : startObjs) {
				final Tile t = o.getLocation();
				if (t.getX() <= swX && t.getY() <= swY) {
					swX = t.getX();
					swY = t.getY();
				}
			}
			final int w = floorSize.width * 12, h = floorSize.height * 12;
			final int x = swX - w + 1, y = swY - h;
			roomNodes = new Tile[floorSize.width == 4 ? 7 : 14][floorSize.height == 4 ? 7 : 14];
			for (int I = 0; I < roomNodes.length; ++I)
				for (int J = 0; J < roomNodes[I].length; ++J)
					roomNodes[I][J] = new Tile(x + 16 * I, y + 16 * J, 0);
			final Room found = new Room(w / 16, h / 16, swX, swY, null);
			roomNodes[w / 16][h / 16] = null;
			rooms.add(newRoom = found);
			setTargetRoom(startRoom = found);
			getCurrentRoom();
			return true;
		}
		return false;
	}

	private enum State {
		BOSS, CLEARED, FINISHED, NEW
	}

	private boolean openBasicDoor() {
		status = "Opening a " + nearDoor.type + " door";
		while (!inRoom(newRoom)) {
			if (failSafe())
				return false;
			if (distTo(nearDoor.loc) > 4)
				lastMessage = "";
			else if (lastMessage.contains("guardians")) {
				if (NPCs.getNearest(monster) != null) {
					if (!fightMonsters() || !pickUpAll())
						return false;
					doorTimer = new Timer(0);
				} else {
					log(LR, false, "Unkillable Guardians, removing door.");
					nearDoor.state = State.FINISHED;
					return true;
				}
				lastMessage = "";
			} else if (unreachable && nearDoor.loc.isOnScreen()) {
				if (distTo(nearDoor.loc) < 4)
					randomCamera();
				else
					walkToMap(nearDoor.loc, 1);
				unreachable = false;
			} else if (lastMessage.startsWith("This door is locked") || lastMessage.contains("not open")
					|| lastMessage.contains("won't open")) {
				log(LR, false, "Undetected puzzle door, removing!");
				bugReport(true);
				nearDoor.state = State.FINISHED;
				waitForResponse();
				return true;
			}
			if (newRoom == null && getNewRoom() && checkRoom())
				return true;
			final SceneObject door = SceneEntities.getAt(nearDoor.loc);
			if (door != null && getCurrentRoom().contains(nearDoor) && openDoor(door, nearDoor.type, nearDoor.typeIdx)) {
				waitToObject(door, false);
				if (newRoom == null && Game.getClientState() == Game.INDEX_MAP_LOADED
						&& !door.contains(Mouse.getLocation()))
					door.hover();
				if (waitForRoom(false))
					return true;
			}
			Time.sleep(200, 400);
		}
		return true;
	}

	private boolean openBlockedDoor() {
		final Timer blockTimer = new Timer(Random.nextInt(12000, 15000));
		status = "Opening a blocked door";
		while (newRoom == null) {
			if (failSafe())
				return false;
			if (!doorCheck())
				return true;
			if (intMatch(player().getAnimation(), BLOCK_ANIMS))
				break;
			final SceneObject block = SceneEntities.getAt(nearDoor.block);
			if (distTo(nearDoor.block) < 4 && !moving() && Game.getClientState() == Game.INDEX_MAP_LOADED)
				if (block == null || block.getType() != SceneEntities.TYPE_INTERACTIVE)
					break;
			if (openDoor(block, DoorType.BLOCKED, nearDoor.typeIdx)) {
				if (!waitToObject(block, false))
					continue;
				eatFood(foods, 50, 50);
			} else if (!blockTimer.isRunning()) {
				threadSwivel(70, 30);
				blockTimer.reset();
			}
			waitForDamage();
			Time.sleep(200, 400);
		}
		doorTimer = new Timer(0);
		nearDoor.blocked = false;
		nearDoor.type = DoorType.STANDARD;
		int attempts = 0;
		while (!inRoom(newRoom)) {
			if (failSafe())
				return false;
			SceneObject door = SceneEntities.getAt(nearDoor.loc);
			if (door != null) {
				if (unreachable) {
					cancelAction();
					unreachable = false;
				}
				if (newRoom == null) {
					if (getNewRoom() && checkRoom())
						return true;
					if (attempts % 4 == 1 && nearDoor.block != null) {
						if (!doorCheck())
							return true;
						door = SceneEntities.getAt(nearDoor.block);
					}
					++attempts;
				}
				if (getCurrentRoom().contains(nearDoor) && openDoor(door, DoorType.STANDARD)) {
					waitToObject(door, false);
					if (newRoom == null && Game.getClientState() == Game.INDEX_MAP_LOADED
							&& !door.contains(Mouse.getLocation()))
						door.hover();
					if (waitForRoom(false))
						return true;
				}
			}
			Time.sleep(200, 400);
		}
		return true;
	}

	private boolean openKeyDoor() {
		status = "Opening a key door";
		lockDown = false;
		while (!lockDown) {
			if (failSafe())
				return false;
			if (player().getAnimation() == 13798)
				break;
			final SceneObject lock = SceneEntities.getAt(nearDoor.block);
			if (lock != null && distTo(nearDoor.block) < 4 && !moving()
					&& lock.getType() != SceneEntities.TYPE_INTERACTIVE)
				break;
			if (lastMessage.contains("don't have the correct")) {
				severe("Hmm.. we got tricked, we don't have the right key!");
				nearDoor.hasKey = false;
				return false;
			}
			if (openDoor(lock, DoorType.KEY)) {
				waitToAnimate();
				if (player().getAnimation() == 13798)
					break;
				waitToObject(lock, false);
			}
			Time.sleep(200, 400);
		}
		lockDown = false;
		nearDoor.blocked = false;
		nearDoor.type = DoorType.UNLOCKED;
		doorTimer = new Timer(0);
		int attempts = 0;
		while (!inRoom(newRoom)) {
			if (failSafe())
				return false;
			SceneObject door = SceneEntities.getAt(nearDoor.loc);
			if (door != null) {
				if (newRoom == null) {
					if (getNewRoom() && checkRoom())
						return true;
					if (attempts % 4 == 1 && nearDoor.block != null) {
						if (!doorCheck())
							return true;
						door = SceneEntities.getAt(nearDoor.block);
					}
					++attempts;
				}
				if (getCurrentRoom().contains(nearDoor) && openDoor(door, DoorType.UNLOCKED)) {
					waitToObject(door, false);
					if (newRoom == null && Game.getClientState() == Game.INDEX_MAP_LOADED
							&& !door.contains(Mouse.getLocation()))
						door.hover();
					if (waitForRoom(false))
						return true;
				}
			}
			Time.sleep(200, 400);
		}
		return true;
	}

	private boolean openSkillDoor() {
		status = "Opening a skill door";
		while (!inRoom(newRoom)) {
			if (failSafe())
				return false;
			if (!doorCheck())
				return true;
			if (lastMessage.contains("need an empty vial"))
				if (invContains(ANTIPOISON))
					ridItem(ANTIPOISON, "Drink");
				else {
					nearDoor.state = State.FINISHED;
					return true;
				}
			if (newRoom == null && getNewRoom() && checkRoom())
				return true;
			final SceneObject door = SceneEntities.getAt(nearDoor.loc);
			if (door != null) {
				if (nearDoor.type == DoorType.SKILL && !moving() && distTo(nearDoor.loc) < 4)
					if (Game.getClientState() == Game.INDEX_MAP_LOADED && doorIndex(door.getId(), SKILL_DOORS) == -1)
						nearDoor.type = DoorType.STANDARD;
				if (getCurrentRoom().contains(nearDoor) && openDoor(door, nearDoor.type, nearDoor.typeIdx)) {
					if (!waitToObject(door, true))
						continue;
					if (newRoom == null)
						if (staticDamage() && doorIndex(door.getId(), SKILL_DOORS) != -1)
							topUp();
						else if (Game.getClientState() == Game.INDEX_MAP_LOADED && !door.contains(Mouse.getLocation()))
							door.hover();
					if (waitForRoom(true))
						break;
				}
				if (!doorCheck())
					break;
			}
			eatFood(foods, 40, 50);
			Time.sleep(200, 400);
		}
		nearDoor.type = DoorType.STANDARD;
		oldDoor = nearDoor;
		return true;
	}

	private boolean openOldDoor() {
		if (!status.contains("Opening"))
			status = "Opening an old door";
		for (final Room r : rooms)
			if (r.entryDoor != null && r.entryDoor.equals(nearDoor)) {
				newRoom = r;
				break;
			}
		if (newRoom == null)
			return false;
		doorTimer = new Timer(0);
		o: while (!inRoom(newRoom)) {
			if (failSafe() || nearDoor == null)
				return false;
			final SceneObject door = SceneEntities.getAt(nearDoor.loc);
			if (openDoor(door, nearDoor.type, nearDoor.typeIdx)) {
				waitToObject(door, false);
				if (!randomActivate(2) && Mouse.getLocation().distance(MM_CENTER) > random(70, 80))
					Mouse.move(MM_CENTER, random(10, 50), random(10, 50));
				for (int c = 0; c < 4; ++c) {
					if (inRoom(newRoom))
						break o;
					Time.sleep(200, 250);
				}
			}
			Time.sleep(200, 400);
		}
		targetRoom.doors.remove(nearDoor);
		targetRoom.doors.add(nearDoor);
		setTargetRoom(getCurrentRoom());
		newRoom = null;
		oldDoor = nearDoor;
		return true;
	}

	private boolean pickUpAll() {
		if (getGoodItem() == null)
			return true;
		if (targetRoom.state == State.FINISHED && (targetRoom.hasPuzzle || !reachable(nearItem, true)))
			return true;
		final boolean guardian = NPCs.getNearest(guardians) != null;
		int invCheck = invCount(true);
		Tile iTile = nearItem.getLocation();
		improveEquipment();
		crossTheChasm(iTile);
		if (secondaryStatus.isEmpty())
			secondaryStatus = "Picking up loot & keys";
		Timer pickupTimer = new Timer(0);
		unreachable = false;
		o: while (getGoodItem() != null) {
			if (failSafe())
				return false;
			if (pickupTimer.getElapsed() > Random.nextInt(30000, 90000) || secondaryStatus.contains("reengage"))
				break;
			iTile = nearItem.getLocation();
			makeSpace(!skipRoom);
			if (pickUpItem(nearItem)) {
				if (distTo(iTile) > 2 && !waitToStart(false))
					unStick();
				while (moving() && headingTowards(iTile, 2.5)) {
					if (failSafe())
						return false;
					if (guardian && player().getInteracting() != null)
						continue o;
					Time.sleep(100, 200);
				}
				Time.sleep(0, 300);
				if (unreachable) {
					walkFast(iTile, false, 1);
					if (player().isInCombat())
						topUp();
					unreachable = false;
				} else
					for (int c = 0; c < (!moving() && atLoc(iTile) ? 4 : 8); ++c) {
						if (invCount(true) > invCheck) {
							improveEquipment();
							break;
						}
						Time.sleep(200, 300);
						if (!moving()) {
							if (roomNumber != 1 && !atLoc(iTile))
								break;
							++c;
						} else if (guardian && player().getInteracting() != null)
							continue o;
					}
			} else {
				if (Menu.contains("Take"))
					Mouse.move(Mouse.getLocation(), random(2, 5), random(2, 5));
				if (roomNumber == 1)
					threadDefault();
				else
					threadPitch(100);
				unStick();
			}
			if (Game.getPlane() == 1)
				return false;
			if (!moving() && distTo(iTile) > 2 && reachable(iTile, true)
					&& pickupTimer.getElapsed() > Random.nextInt(3000, 6000))
				walkTo(iTile, 2);
			if (invCount(true) > invCheck || (moving() && player().getInteracting() == null)) {
				invCheck = invCount(true);
				pickupTimer = new Timer(0);
			} else if (pickupTimer.getElapsed() > Random.nextInt(14000, 20000)) {
				unStick();
				randomCamera();
				if (distTo(iTile) > 1 && reachable(iTile, true))
					walkBlockedTile(iTile, 2);
				else if (!badTiles.contains(iTile)) {
					pickupTimer = new Timer(0);
					badTiles.add(iTile);
				}
			}
			Time.sleep(200, 300);
		}
		if (skipRoom)
			eatFood(foods, 30, 30);
		improveArmorWielding();
		improveWeaponBinding();
		secondaryStatus = "";
		return true;
	}

	private boolean resetPrestige() {
		if (!Option.NO_PRESTIGE.enabled()) {
			secondaryStatus = "Resetting prestige";
			while (cProg != 0) {
				if (failBasic())
					return false;
				if (Widgets.get(1188).validate())
					clickThrough(Widgets.get(1188, 3), "Continue");
				else if (Widgets.get(1186).validate())
					spaceContinue();
				else if (openPartyTab())
					clickThrough(Widgets.get(939, 87), "Reset");
				else
					Time.sleep(100, 200);
				updateProgress();
			}
			if (partyMode)
				maxFloor = -1;
			fNumber = 1;
			++prestigeCount;
			forcePrestige = false;
			secondaryStatus = "";
			return true;
		} else {
			severe("Last floor reached, but prestiging is disabled! Shutting down script.");
			stopScript();
		}
		return false;
	}

	private boolean runSettings() {
		threadDefault();
		bounds.clear();
		updateLevels();
		prayerLevel = level(Skills.PRAYER);
		slayerLevel = level(Skills.SLAYER);
		currentWep = valueOf(primaryWep);
		for (int I = 0; I < equipmentTiers.length; ++I) {
			equipmentTiers[I] = valueOf(initialEquipmentTiers[I]);
			tempTiers[I] = valueOf(initialEquipmentTiers[I]);
		}
		if (!settingsFinished) {
			memberWorld = parse(Widgets.get(747, 7).getText()) > 0;
			if (developer)
				severe("iDungeon Developer mode enabled. " + (memberWorld ? "Members" : "Free") + " world.");
			attStart = Skills.getExperience(Skills.ATTACK);
			strStart = Skills.getExperience(Skills.STRENGTH);
			defStart = Skills.getExperience(Skills.DEFENSE);
			hitStart = Skills.getExperience(Skills.CONSTITUTION);
			rngStart = Skills.getExperience(Skills.RANGE);
			mgcStart = Skills.getExperience(Skills.MAGIC);
			prayStart = Skills.getExperience(Skills.PRAYER);
			dungStart = Skills.getExperience(Skills.DUNGEONEERING);
			runeStart = Skills.getExperience(Skills.RUNECRAFTING);
			mineStart = Skills.getExperience(Skills.MINING);
			fireStart = Skills.getExperience(Skills.FIREMAKING);
			woodStart = Skills.getExperience(Skills.WOODCUTTING);
			smthStart = Skills.getExperience(Skills.SMITHING);
			crftStart = Skills.getExperience(Skills.CRAFTING);
			agilStart = Skills.getExperience(Skills.AGILITY);
			thevStart = Skills.getExperience(Skills.THIEVING);
			summStart = Skills.getExperience(Skills.SUMMONING);
			herbStart = Skills.getExperience(Skills.HERBLORE);
			cnstStart = Skills.getExperience(Skills.CONSTRUCTION);
			cookStart = Skills.getExperience(Skills.COOKING);
			fishStart = Skills.getExperience(Skills.FISHING);
			slayStart = Skills.getExperience(Skills.SLAYER);
			dungStartLevel = level(Skills.DUNGEONEERING);
			isCursing = Settings.get(1584) % 2 != 0;
			if (!loadSettings()) {
				if (combatLevel > 9 && level(Skills.DEFENSE) < 21)
					Option.PURE.set(true);
				else if (!freeVersion)
					Option.STYLE_SWAP.set(true);
				if (!freeVersion && level(Skills.RUNECRAFTING) > 26 && level(Skills.MAGIC) > 32)
					Option.RUNECRAFT.set(true);
			}
			if (!afking) {
				oTab = 0;
				showPaint = true;
				showStats = false;
				showOptions = false;
				showSettings = true;
				// enabledFlags = Environment.INPUT_MOUSE;
				// disabledFlags = 0;
				if (defaultMode != -1)
					if (defaultMode == 4) {
						if (combatSpell != null && !autocast(combatSpell))
							autocast(combatSpell);
					} else
						setFightMode(defaultMode);
				setUserInput(contains(PAINT, mp));
				open(Tabs.INVENTORY);
				typeTo = -1;
				failTimer = new Timer(300000);
				while (showSettings) {
					if (!failTimer.isRunning()) {
						severe("Please restart the script and remember to complete the settings!");
						stopScript();
						return false;
					}
					if (pauseScript())
						return false;
					Time.sleep(300, 600);
				}
				// enabledFlags = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;
				// disabledFlags = Environment.INPUT_KEYBOARD;
				// Environment.setUserInput(Environment.INPUT_KEYBOARD);
			}
			defaultMode = getFightMode();
			if (Option.MAGIC.enabled())
				defaultMode = 4;
			else if (defaultMode == 4)
				defaultMode = 1;
			open(Tabs.EQUIPMENT);
			if (Option.RANGE.enabled()) {
				primaryStyle = Style.RANGE;
				armorTiers = LEATHER_TIERS;
				slotNames = RANGE_SLOTS;
				weaponTiers = WOOD_TIERS;
			} else if (Option.MAGIC.enabled()) {
				primaryStyle = Style.MAGIC;
				armorTiers = CLOTH_TIERS;
				slotNames = MAGIC_SLOTS;
				weaponTiers = null;
			}
			defenseTier = maxTier(Skills.DEFENSE);
			if (Option.PURE.enabled() && (defaultMode == defenseMode || defaultMode == 3))
				defaultMode = 1;
			attackMode = valueOf(defaultMode);
			for (int c = 0; c < 10; ++c) {
				if (equipItem(Slot.WEAPON) != null)
					break;
				Time.sleep(200, 400);
			}
			final Item weaponSlot = equipItem(Slot.WEAPON);
			final Item ammo = equipItem(Slot.AMMO);
			if (weaponSlot != null) {
				primaryWep = weaponSlot.getId();
				currentWep = weaponSlot.getId();
				final String wName = equipmentName(weaponSlot);
				final String wMetal = equipmentMaterial(wName);
				weaponTier = equipmentTier(wName);
				weaponType = equipmentType(wName);
				Weapon weapon = null;
				for (final Weapon w : Weapon.values())
					if (weaponType.equals(w.name)) {
						weapon = w;
						break;
					}
				if (weapon == null || primaryWep < 1) {
					severe("Unknown weapon type. Please try restarting the script for best results");
					if (developer)
						log(null, false, primaryWep + " " + weaponType);
				} else {
					final Combat dMode = weapon.getMode(defaultMode);
					Melee.STAB.set(weapon.indexOf(Melee.STAB));
					Melee.SLASH.set(weapon.indexOf(Melee.SLASH));
					Melee.CRUSH.set(weapon.indexOf(Melee.CRUSH));
					if (dMode != null)
						dMode.set(defaultMode);
					if (combatStyle != Style.MAGIC)
						Melee.DEFENSE.set(weapon.defensive);
					attackTier = maxTier(weapon.skill);
					if (!memberWorld && attackTier > 5)
						attackTier = 5;
					twoHanded = weapon.isTwoHanded;
					if (weaponTier == 0)
						weaponTier = 11;
					log(null, false, wMetal + " " + weaponType + " - Tier: " + weaponTier + "; Max tier: " + attackTier
							+ "; Two-handed: " + (twoHanded ? "Yes" : "No") + "; Upgrade: "
							+ (weaponTier < attackTier ? "Yes" : "No"));
					if (Option.STYLE_SWAP.enabled && !Melee.SLASH.enabled())
						Option.STYLE_SWAP.set(false);
				}
			}
			for (int I = 0; I < EQUIP_SLOTS.length; ++I) {
				final Item eq = equipItem(EQUIP_SLOTS[I]);
				if (eq != null) {
					final String eqName = equipmentName(eq);
					final int tier = equipmentTier(eqName);
					if (getName(eq).endsWith("(b)")) {
						if (I == 0 && eqName.equals("Shadow silk hood")) {
							initialEquipmentTiers[I] = 11;
							shadowHooded = true;
						} else if (I == 2 && intMatch(eq.getId(), BLASTBOXES)) {
							blastBox = eq.getId();
							initialEquipmentTiers[I] = 11;
							twoHanded = true;
						} else if (I == 4 && eqName.endsWith("precision bracelet"))
							initialEquipmentTiers[I] = 11;
						else
							initialEquipmentTiers[I] = tier;
						if (initialEquipmentTiers[I] == 0)
							initialEquipmentTiers[I] = 11;
						if (initialEquipmentTiers[I] < defenseTier) {
							startArmor = eq.getId();
							armorSlot = I;
							armorType = equipmentType(eqName);
						}
						equipmentTiers[I] = valueOf(initialEquipmentTiers[I]);
						tempTiers[I] = valueOf(initialEquipmentTiers[I]);
						log(null, false, eqName + " - Tier: " + equipmentTiers[I] + "; Max tier: " + defenseTier
								+ "; Upgrade: " + (equipmentTiers[I] < defenseTier ? "Yes" : "No"));
					} else {
						equipmentTiers[I] = tier;
						tempTiers[I] = tier;
					}
				}
			}
			if (ammo != null) {
				arrows = ammo.getId();
				save(saves, arrows);
				log(null, false, "Ranged ammo: " + ammo.getName());
			}
			if (Option.RING_SWITCH.enabled()) {
				final Item ring = equipItem(Slot.RING);
				if (ring != null)
					for (int I = 0; I < 3; ++I)
						if (intMatch(ring.getId(), KINSHIP_CUSTOM_RINGS[I])) {
							ringStyle = Style.values()[I];
							break;
						}
				if (ringStyle != null)
					log(null, false, "Ring: Quickswitching enabled. Current style: " + ringStyle);
				else {
					log(LR, false, "Unable to detect your quickswitch setup, please restart to try again");
					Option.RING_SWITCH.set(false);
				}
				switchRing(primaryStyle);
			}
			if (combatSpell != null) {
				final String name = combatSpell.toString();
				if (name.contains("AIR"))
					Mage.WIND.set(4);
				else if (name.contains("WATER"))
					Mage.WATER.set(4);
				else if (name.contains("EARTH"))
					Mage.EARTH.set(4);
				else if (name.contains("FIRE"))
					Mage.FIRE.set(4);
				Mage.ALL.set(4);
			}
			if (blastBox > 0)
				save(saves, blastBox);
			save(saves, RINGS);
			save(saves, primaryWep);
			if (secondaryWep > 0)
				save(saves, secondaryWep);
		} else if (Option.SECONDARY_RANGE.enabled()) {
			if (!saving(saves, arrows)) {
				if (developer)
					log(LR, true, "Removing arrows");
				arrows = -1;
				Style.RANGE.set(false);
			}
			if (!saving(saves, secondaryWep)) {
				secondaryWep = -1;
				Style.RANGE.set(false);
			}
		}
		for (final Item item : invItems(false)) {
			if (item == null)
				continue;
			final int itemId = item.getId();
			if (itemId < 1)
				continue;
			final String iName = equipmentName(item);
			final int tier = equipmentTier(iName);
			if (tier > 0) {
				final String iType = equipmentType(iName);
				for (int I = 0; I < slotNames.length; ++I)
					if ((I != 2 || !twoHanded) && iType.contains(slotNames[I])) {
						if (tier > equipmentTiers[I] && ridItem(itemId, "Wear"))
							equipmentTiers[I] = tier;
						break;
					}
			}
		}
		Style.MELEE.set(Option.MELEE.enabled() || (Option.SECONDARY_MELEE.enabled() && secondaryWep > 0));
		Style.RANGE.set(Option.RANGE.enabled() || (Option.SECONDARY_RANGE.enabled() && arrows > 0 && secondaryWep > 0));
		Style.MAGIC
				.set(combatSpell != null
						&& (Option.MAGIC.enabled() || (Option.SECONDARY_MAGIC.enabled() && (secondaryWep > 0 || blastBox > 0))));
		final int goodOffset = level(Skills.CONSTITUTION) / (memberWorld ? 34 : 46);
		foods = FOOD_ARRAY;
		goodFoods = new int[foods.length - goodOffset];
		final int[] newTop = new int[foods.length - goodOffset - 1];
		topFoods = new int[newTop.length];
		System.arraycopy(foods, goodOffset, goodFoods, 0, goodFoods.length);
		System.arraycopy(goodFoods, 1, newTop, 0, newTop.length);
		for (int I = 0; I < newTop.length; ++I)
			topFoods[I] = newTop[newTop.length - 1 - I];
		if (level(Skills.CONSTITUTION) > Random.nextInt(40, 60) || combatLevel > 50) {
			foods = new int[FOOD_ARRAY.length - 1];
			System.arraycopy(FOOD_ARRAY, 1, foods, 0, foods.length);
		}
		save(bounds, foods);
		if (Option.BURY.enabled())
			save(bounds, BONES);
		if (Option.MAKE_FOOD.enabled() || (Option.RUNECRAFT.enabled() && !invContains(COSMIC_RUNES))) {
			save(bounds, COINS);
			save(sales, BLUE_CHARM, GREEN_CHARM);
		}

		final int magicLevel = level(Skills.MAGIC);
		if (magicLevel > 31) {
			save(bounds, COSMIC_RUNES[1]);
			if (Option.RUNECRAFT.enabled() && aComplexity > 2 && level(Skills.RUNECRAFTING) > 26)
				save(bounds, ESSENCE);
			if (aComplexity > 4 && magicLevel > 63 && floor != Floor.FROZEN)
				save(bounds, LAW_RUNES[1]);
		}
		if (!freeVersion && aComplexity > 4) {
			save(bounds, GGS);
			save(bounds, FEATHERS);
			if (memberWorld)
				save(bounds, ANTIPOISON);
		}
		if (floor == Floor.ABANDONED)
			save(bounds, PICKAXE);
		for (final Item item : invItems(false))
			if (getName(item).contains("(b)"))
				save(bounds, item.getId());
		if (Option.RANGE.enabled())
			ridItem(arrows, "Wield");
		else if (Option.MAGIC.enabled() && ridItem(blastBox, "Wield"))
			while (!isAutocasting()) {
				if (failCheck())
					return false;
				if (autocast(combatSpell))
					Time.sleep(300, 500);
				Time.sleep(100, 200);
			}
		combatStyle = styleOf(primaryStyle.idx);
		if (!settingsFinished) {
			if (!afking && Option.MEDIUM.enabled() && inDungeon()) {
				floorSize = !isRushing && (!Option.RUSH.enabled() || fNumber > rushTo) ? Size.MEDIUM : Size.SMALL;
				log(Color.WHITE, false, "Please ensure you've started the script inside a medium Dungeon");
				log(null, false, "Otherwise, please exit the floor and restart the bot.");
			}
			settingsFinished = true;
			settingStatus = null;
			saveSettings();
			runTimer = new Timer(0);
			log(Color.WHITE, false, "Settings complete!");
			if (!afking && SceneEntities.getLoaded().length > 300) {
				severe("This dungeon appears to have already been explored.");
				isDead = false;
				exit = true;
				return false;
			}
			typeTo = -1;
		}
		failTimer = new Timer(Random.nextInt(360000, 480000));
		return true;
	}

	private class ThreadAngle implements Runnable {
		private final int degrees;

		private ThreadAngle(int degrees) {
			this.degrees = degrees;
		}

		@Override
		public void run() {
			if (Math.abs(Camera.getYaw() - degrees) > 5)
				Camera.setAngle(degrees);
		}
	}

	private class ThreadPitch implements Runnable {
		private final int start, finish;

		private ThreadPitch(int start, int finish) {
			this.start = start;
			this.finish = finish;
		}

		@Override
		public void run() {
			if (Math.abs(Camera.getPitch() - start) > 5)
				Camera.setPitch(start);
			if (finish != start) {
				Time.sleep(0, random(0, 100));
				if (Math.abs(Camera.getPitch() - finish) > 5)
					Camera.setPitch(finish);
			}
		}
	}

	private boolean shopFood() {
		lastMessage = "";
		double logsNeeded = Math.ceil(invCount(true, COINS) / fish.cost / Random.nextInt(20, 24));
		if (logsNeeded < 1)
			logsNeeded = 1;
		while (!lastMessage.contains("afford") && !lastMessage.contains("enough money")
				&& !lastMessage.contains("enough space")) {
			if (failSafe())
				return false;
			if (shopOpen() && !shopSale())
				if (logs != null && hasCoins(logs.cost) && getObjInRoom(FIRES) == null
						&& invCount(true, logs.logId) < logsNeeded) {
					final WidgetChild logItem = getShopItem(logs.toString() + " branches");
					if (shopScroll(logItem) && logItem.interact("Buy 1"))
						for (int c = 0; c < 10; ++c) {
							if (invCount(true, logs.logId) >= logsNeeded)
								break;
							Time.sleep(150, 250);
						}
				} else if (fish != null && hasCoins(fish.cost)) {
					final int startCount = invCount(true);
					final WidgetChild fishItem = getShopItem(fish.toString());
					if (shopScroll(fishItem) && fishItem.interact("Buy 50"))
						for (int c = 0; c < 10; ++c) {
							if (invCount(true) > startCount)
								break;
							Time.sleep(150, 250);
						}
				} else
					return false;
			Time.sleep(50, 200);
		}
		shopClose();
		return true;
	}

	private boolean shop(int itemId, String name, int amount, int cost) {
		boolean purchased = false;
		final String prevStatus = secondaryStatus;
		lastMessage = "";
		while (invCount(true, itemId) < amount && invCount(true, itemId) < amount && hasCoins(cost)) {
			if (failSafe()) {
				purchased = false;
				break;
			}
			if (lastMessage.contains("have enough"))
				shopClose();
			secondaryStatus = "Purchasing " + amount + " " + name;
			if (shopOpen()) {
				shopSale(itemId);
				if (invCount(true, itemId) < amount) {
					final WidgetChild item = getShopItem(name);
					if (hasCoins(cost)
							&& (itemId == ANTIPOISON ? scrollToBottom(Widgets.get(956, 25)) : shopScroll(item))) {
						final int count = invCount(itemId);
						final int needed = amount - count;
						if (item.interact("Buy " + (needed < 5 ? 1 : amount))) {
							purchased = true;
							for (int c = 0; c < 10; ++c) {
								if (invCount(true, itemId) > count)
									break;
								Time.sleep(100, 200);
							}
						}
					}
				}
				Time.sleep(100, 200);
			}
		}
		secondaryStatus = prevStatus;
		return purchased;
	}

	private void shopClose() {
		while (Widgets.get(956).validate())
			clickThrough(Widgets.get(956, 18), "Close");
	}

	private boolean shopOpen() {
		if (Widgets.get(956).validate())
			return true;
		if (doNpcAction(getNpcInRoom(SMUGGLER), "Trade")) {
			waitToStop(false);
			for (int c = 0; c < 5; ++c) {
				if (Widgets.get(956).validate())
					return true;
				Time.sleep(100, 300);
			}
		}
		return false;
	}

	private boolean shopSale(int... excluded) {
		final int startCount = invCount(true);
		setMouseSpeed(1.8);
		for (final Item item : invItems(false)) {
			if (failCheck() || !Widgets.get(956).validate())
				break;
			if (!riddable(item) || intMatch(item.getId(), excluded))
				continue;
			final String action = intMatch(item.getId(), PUZZLE_ITEMS) ? "Drop" : "Sell 50";
			for (int c = 0; c < 3; ++c) {
				if (interact(item, action)) {
					if (item.getStackSize() > 50 && !action.equals("drop") && interact(item, "Sell 50"))
						Time.sleep(30, 300);
					break;
				}
				Time.sleep(100, 300);
			}
		}
		setMouseDefault();
		return invCount(true) != startCount;
	}

	private boolean shopScroll(WidgetChild targ) {
		final WidgetChild scrollBar = Widgets.get(956, 25);
		if (targ == null || scrollBar == null || !targ.validate())
			return false;
		final WidgetChild bar = scrollBar.getChild(0);
		while (Widgets.get(956).validate()) {
			final int tBar = bar.getAbsoluteY(), tTop = targ.getAbsoluteY();
			if (tTop < tBar)
				clickComponent(scrollBar.getChild(4), "");
			else if (tTop + targ.getHeight() > tBar + bar.getBoundingRectangle().getHeight())
				clickComponent(scrollBar.getChild(5), "");
			else
				break;
			Time.sleep(0, 50);
		}
		return true;
	}

	private boolean shopQuickly(String name, int amount) {
		final String lastStatus = secondaryStatus;
		secondaryStatus = "Purchasing " + amount + " " + name + "s";
		lastMessage = "";
		while (!shopOpen()) {
			if (pauseScript())
				return false;
			Time.sleep(100, 200);
		}
		shopSale();
		if (scrollToBottom(Widgets.get(956, 25))) {
			final WidgetChild item = getShopItem(name);
			if (item != null)
				for (int c = 0; c < 10 && amount > 0; ++c) {
					if (pauseScript())
						return false;
					if (!item.validate() || lastMessage.contains("don't have enough"))
						break;
					int toBuy = 1;
					if (amount > 9)
						toBuy = 10;
					else if (amount > 4)
						toBuy = 5;
					if (item.interact("Buy " + toBuy))
						amount -= toBuy;
					Time.sleep(5, 50);
				}
		}
		secondaryStatus = lastStatus;
		return true;
	}

	private enum Option {
		MELEE("Melee", true, 26, 46, "Set your primary combat to melee", false), RANGE("Range", false, 106, 46,
				"Set your primary combat to range", false), MAGIC("Magic", false, 186, 46,
				"Set your primary combat to magic", false), SECONDARY_MELEE("Melee", false, 26, 86,
				"Set your secondary combat to melee", false), SECONDARY_RANGE("Range", false, 106, 86,
				"Set your secondary combat to range", false), SECONDARY_MAGIC("Magic", false, 185, 86,
				"Set your secondary combat to magic", false), RING_SWITCH("Ring switch", false, 265, 86,
				"Quickswitch your ring when swapping styles", true),

		STYLE_SWAP("Style swap", false, 146, 27, "Intelligently switch attack modes", false), QUICK_PRAY("Quick pray",
				false, 253, 27, "Use quick prayers in addition", false), PURE("Pure mode", false, 360, 27,
				"Prevents defense training", false), MAKE_FOOD("Food prep", true, 146, 49,
				"Make food when you're running low", false), RUNECRAFT("Runecraft", false, 253, 49,
				"Craft cosmic runes for extra teles", false), BURY("Bury bones", false, 360, 49,
				"Loot and bury bones for Prayer exp", false), PRAYER("Prayer", true, 146, 71, "Gain Prayer experience",
				false), STRENGTH("Strength", true, 253, 71, "Gain Strength experience", false), SUMMONING("Summoning",
				true, 360, 71, "Gain Summoning experience", true),

		MEDIUM("Mediums", false, 165, 27, "Explore Medium dungeons", false), DEAD_END("Dead end", false, 165, 49,
				"Clear monsters from dead ends for more combat", false), SKIP("Skipping", false, 165, 71,
				"Skip over rooms w/o Guardian doors", false), JOURNALS("Journals", false, 165, 93,
				"Explore all 60 floors for journal notes", false), RUSH("Enabled", false, 390, 27,
				"Enable rushing of certain floors", false), RUSH_FOODLESS("Skip food", false, 390, 49,
				"Disable food pickup while rushing", false), NO_PRESTIGE("No prestige", false, 390, 71,
				"Disable automatic prestiging", false),

		AFK("Afk", true, 26, 46, "Party members will afk through floors", false), COOPERATIVE("Cooperative", false,
				106, 46, "Party members will work together", false), CHAT("Chat", false, 26, 86,
				"Players will use RS chat to coordinate", false), ONE_PLAYER("One", true, 26, 86,
				"Bot dungeons designed for 1 player", false), FULL_SIZE("Full size", false, 106, 86,
				"BETA - Bot full-sized dungeons", false);

		private final boolean isMembers;
		private final Point p, b;
		private final Rectangle button;
		private final String name, description;
		private boolean enabled;

		private Option(String name, boolean enabled, int x, int y, String description, boolean isMembers) {
			this.button = new Rectangle(x, y, (int) (description.length() * 2.2), 19);
			this.enabled = enabled;
			this.name = name;
			this.b = new Point(x - 2, y - 2);
			this.p = new Point(x + 21, y + 13);
			this.description = description;
			this.isMembers = isMembers;
		}

		private boolean clicked() {
			return shitDungeon.contains(button, cp);
		}

		private boolean hovered() {
			return shitDungeon.contains(button, mp);
		}

		private boolean enabled() {
			return enabled;
		}

		private boolean toggle() {
			return enabled = !enabled;
		}

		private void set(boolean enable) {
			enabled = enable;
		}
	}

	private boolean showPaint = false, backPage = false, paintMap = false;
	private boolean showStats = true, showOptions = false, showSettings = false, showChat = false;

	private final Option[] opTab0 = { Option.MELEE, Option.RANGE, Option.MAGIC, Option.SECONDARY_MELEE,
			Option.SECONDARY_RANGE, Option.SECONDARY_MAGIC, Option.RING_SWITCH };
	private final Option[] opTab1 = { Option.STYLE_SWAP, Option.PURE, Option.QUICK_PRAY, Option.MAKE_FOOD,
			Option.RUNECRAFT, Option.BURY, Option.PRAYER, Option.STRENGTH, Option.SUMMONING };
	private final Option[] opTab2 = { Option.MEDIUM, Option.DEAD_END, Option.SKIP, Option.JOURNALS, Option.RUSH,
			Option.RUSH_FOODLESS, Option.NO_PRESTIGE };
	private final Option[][] options = { opTab0, opTab1, opTab2 };
	private final Option[] opParty = { Option.AFK, Option.COOPERATIVE, Option.CHAT, Option.ONE_PLAYER, Option.FULL_SIZE };

	private static final Color GN3 = new Color(0, 255, 0, 100);
	private static final Color RD3 = new Color(255, 0, 0, 130);

	private final Color BLU = new Color(186, 243, 243);
	// private final Color BLH = new Color(200, 255, 255);
	private final Color BL1 = new Color(0, 0, 255, 150);
	private final Color BT2 = new Color(0, 0, 0, 200);
	private final Color BT3 = new Color(0, 0, 0, 180);

	private final Color GLD = new Color(255, 215, 0, 140);
	private final Color GRN = new Color(0, 240, 0);
	private final Color RED = new Color(255, 0, 0);
	private final Color PLD = new Color(255, 0, 255);
	private final Color PT2 = new Color(160, 0, 160, 160);
	private final Color PT3 = new Color(40, 10, 40);
	private final Color SKY = new Color(0, 190, 255, 140);
	private final Color WT1 = new Color(255, 255, 255, 100);
	private final Color WT2 = new Color(255, 255, 255, 135);
	private final Color WS1 = new Color(235, 235, 235);
	private final Color WS2 = new Color(210, 210, 210);
	private final Color YLW = new Color(240, 240, 0);

	private final Color LB = new Color(0, 150, 210);
	private final Color LG = new Color(0, 210, 0);
	private final Color LP = new Color(210, 0, 210);
	private final Color LR = new Color(210, 0, 0);
	private static final Color LGR = new Color(210, 210, 210);

	private final Font AVERIA_SM = new Font("Trebuchet MS", 0, 12);
	private final Font AVERIA_MED = new Font("Arial", Font.BOLD, 13);
	private final Font AVERIA_LRG = new Font("Trebuchet MS", 0, 16);
	private final Font ARIAL_BOLD = new Font("Arial", 0, 12);
	private final Font JAPAN_LRG = new Font("Arial", Font.BOLD, 15);

	private final Rectangle RECT_STATS = new Rectangle(0, 0, 75, 17);
	private final Rectangle RECT_OPTIONS = new Rectangle(79, 0, 75, 17);
	private final Rectangle RECT_STATUS = new Rectangle(158, 0, 232, 17);
	private final Rectangle RECT_CONTROL = new Rectangle(394, 0, 112, 19);
	private final Rectangle RECT_CHAT = new Rectangle(0, 113, 506, 17);
	private final Rectangle RECT_TAB_1 = new Rectangle(0, 20, 75, 31);
	private final Rectangle RECT_TAB_2 = new Rectangle(0, 51, 75, 31);
	private final Rectangle RECT_TAB_3 = new Rectangle(0, 82, 75, 31);

	// private final Rectangle TOOLBAR_FILL = new Rectangle(0, 0, 765, 50);
	private final Rectangle PAINT = new Rectangle(0, 0, 506, 113);
	private final Rectangle TAB_FILL = new Rectangle(0, 19, 75, 95);
	private final Rectangle RECT_MINI_UP = new Rectangle(276, 23, 9, 8);
	private final Rectangle RECT_MINI_DOWN = new Rectangle(276, 31, 9, 7);
	private final Rectangle RECT_STOP = new Rectangle(398, 1, 18, 18);
	private final Rectangle RECT_INFO = new Rectangle(418, 1, 18, 18);
	private final Rectangle RECT_HELP = new Rectangle(438, 1, 18, 18);
	private final Rectangle RECT_HIDE = new Rectangle(490, 3, 17, 17);
	private final Rectangle RECT_CONTINUE = new Rectangle(434, 91, 66, 16);
	private final Rectangle RECT_BACK = new Rectangle(3, 91, 66, 16);
	private final Rectangle RECT_PICKUP = new Rectangle(85, 42, 199, 18);
	private final Rectangle RECT_DESTROY = new Rectangle(295, 42, 199, 18);
	// private final Rectangle RECT_PASSCODE = new Rectangle(84, 91, 155, 18);

	private final Rectangle SEL_1_FILL = new Rectangle(111, 58, 19, 19);
	private final Rectangle RECT_C_UP = new Rectangle(SEL_1_FILL.x, SEL_1_FILL.y - 16, 20, 15);
	private final Rectangle RECT_C_DOWN = new Rectangle(SEL_1_FILL.x, SEL_1_FILL.y + 18, 20, 15);
	private final Rectangle SEL_2_FILL = new Rectangle(290, 58, 19, 19);
	private final Rectangle RECT_RC_UP = new Rectangle(SEL_2_FILL.x, SEL_2_FILL.y - 16, 20, 15);
	private final Rectangle RECT_RC_DOWN = new Rectangle(SEL_2_FILL.x, SEL_2_FILL.y + 18, 20, 15);
	private final Rectangle SEL_3_FILL = new Rectangle(350, 58, 21, 19);
	private final Rectangle RECT_F_UP = new Rectangle(SEL_3_FILL.x + 1, SEL_3_FILL.y - 16, 20, 15);
	private final Rectangle RECT_F_DOWN = new Rectangle(SEL_3_FILL.x + 1, SEL_3_FILL.y + 18, 20, 15);

	private static final Point PO = new Point(7, 394), MO = new Point(550, 253);
	private final Rectangle RECT_MAP_TAB = new Rectangle(516, 122, 30, 36);
	final int[][] DOOR_OFFSETS = { new int[] { 0, 1 }, new int[] { 1, 0 }, new int[] { 2, 1 }, new int[] { 1, 2 } };

	private int page = 0, sTab = 1, oTab = 1, typeTo = -1;
	private String pickupName = "", destroyName = "";
	private int pickupId = -1, destroyId = -1;

	private static class LogMsg {
		private final String s;
		private final Color c;
		private final Timer time;
		private float a;

		private LogMsg(String s, Color c, boolean retain) {
			this.s = s;
			this.c = c != null ? c : LGR;
			this.time = retain ? null : new Timer(45000);
			this.a = 1;
		}

		private boolean running() {
			return time == null || time.isRunning();
		}

		private void draw(Graphics render, int idx) {
			if (time != null && a > 0.006 && time.getElapsed() > 42000)
				a -= 0.006;
			((Graphics2D) render).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, a));
			render.setColor(Color.BLACK);

			if (!(render == null && s != null && idx == -1))
				render.drawString(s, PO.x, PO.y - 10 - 16 * idx);
			render.setColor(c);

			if (!(render == null && s != null && idx == -1))
				render.drawString(s, PO.x - 1, PO.y - 11 - 16 * idx);
		}
	}

	private static void severe(String err) {
		log(Color.RED, developer, err);
	}

	private static void logD(String msg) {
		if (developer)
			System.out.println("[" + (username() != null ? username.replaceAll(" ", "") + " " : "")
					+ LOG_DATE.format(new Date()) + "] " + msg);
	}

	private static void log(Color c, boolean retain, String msg) {
		if (msg != null) {
			logD(msg);
			final LogMsg prevMsg = !logger.isEmpty() ? logger.get(0) : null;
			if (prevMsg != null && msg.equals(prevMsg.s)) {
				prevMsg.a = 1;
				if (prevMsg.time != null)
					prevMsg.time.reset();
			} else {
				if (logger.size() > 4)
					logger.remove(4);
				logger.add(0, new LogMsg(msg, c, retain));
			}
		} else if (developer)
			System.out.println("");
	}

	private final String[] MESSAGE_BOSS_FINISHED = { "let's go", "lets go", "boss killed!", "next floor", "next",
			"come", "ready" };
	private final String[] MESSAGE_EXIT_FLOOR = { "I'm out", "time to go", "exiting", "exit", "outside" };

	private void publish(String... messages) {
		if (Option.CHAT.enabled())
			Keyboard.sendText(messages[Random.nextInt(0, messages.length)], true);
	}

	private String comma(int d) {
		if (d < 1000)
			return String.valueOf(d);
		final DecimalFormat df = new DecimalFormat();
		final DecimalFormatSymbols dfs = new DecimalFormatSymbols();
		dfs.setGroupingSeparator(',');
		df.setDecimalFormatSymbols(dfs);
		return df.format(d);
	}

	private double perHour(int amt) {
		if (amt < 1)
			return 0;
		final double doubleAmt = (double) 36000000 / runTimer.getElapsed() * amt;
		return doubleAmt > 99 ? (int) doubleAmt / 10 : Math.rint(doubleAmt) / 10;
	}

	private static boolean contains(Rectangle r, Point p) {
		return new Rectangle(r.x + PO.x, r.y + PO.y, r.width, r.height).contains(p);
	}

	private void paintBevel(Graphics render, Rectangle b) {
		render.draw3DRect(b.x + PO.x, b.y + PO.y, b.width, b.height, true);
	}

	private void paintImage(Graphics render, Image img, int x, int y) {
		render.drawImage(img, x + PO.x, y + PO.y, null);
	}

	private void paintLine(Graphics render, int x1, int y1, int x2, int y2) {
		if (!(render == null && x1 == -1 && y1 == -1))
			render.drawLine(x1 + PO.x, y1 + PO.y, x2 + PO.x, y2 + PO.y);
	}

	private void paintString(Graphics render, String string, int x, int y) {
		if (!(render == null && string == null && x == -1 && y == -1))
			render.drawString(string, x + new Point(7, 394).x, y + new Point(7, 394).y);
	}

	private void sBevel(Graphics render, String string, int x, int y, Color color, boolean active) {
		if (active) {
			render.setColor(Color.BLACK);
			render.drawString(string, x + PO.x + 1, y + PO.y + 1);
		}
		render.setColor(color);
		render.drawString(string, x + PO.x, y + PO.y);
	}

	private void sOutline(Graphics render, String string, int x, int y, Color color, boolean enabled) {
		if (enabled) {
			render.setColor(Color.BLACK);
			render.drawString(string, x + PO.x - 1, y + PO.y - 1);
			render.drawString(string, x + PO.x - 1, y + PO.y + 1);
			render.drawString(string, x + PO.x + 1, y + PO.y - 1);
			render.drawString(string, x + PO.x + 1, y + PO.y + 1);
		}
		render.setColor(color);
		render.drawString(string, x + PO.x, y + PO.y);
	}

	private void drawMouse(Graphics render) {
		if (!showSettings || !contains(PAINT, mp)) {
			final Point mouse = Mouse.getLocation();
			render.drawImage(MOUSE, mouse.x, mouse.y - 18, null);
		}
	}

	private void drawRect(Graphics render, Rectangle b) {
		render.fillRect(b.x + PO.x, b.y + PO.y, b.width, b.height);
	}

	private void drawTile(Graphics render, Tile t, double ds, double df) {
		if (t == null)
			return;
		final int tX = t.getX(), tY = t.getY();
		final Point p1 = Calculations.worldToMap(tX - ds, tY - ds), p2 = Calculations.worldToMap(tX - ds, tY - df);
		final Point p3 = Calculations.worldToMap(tX - df, tY - df), p4 = Calculations.worldToMap(tX - df, tY - ds);
		if (p1.x != -1 && p2.x != -1 && p3.x != -1 && p4.x != -1) {
			final int[] allX = new int[] { p1.x, p2.x, p3.x, p4.x };
			final int[] allY = new int[] { p1.y, p2.y, p3.y, p4.y };
			render.fillPolygon(allX, allY, 4);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (!showPaint)
			return;
		if (showSettings) {
			final char key = e.getKeyChar();
			if (typeTo == 2) {
				// input check
			} else if (key == KeyEvent.VK_ENTER)
				advancePage(true);
			else if (key == KeyEvent.VK_BACK_SPACE)
				if (page > 0)
					advancePage(false);
		} else if (!freeVersion && showOptions && oTab == 3) {
			final char key = e.getKeyChar();
			if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_TAB)
				typeTo = typeTo < 1 ? ++typeTo : -1;
			// Environment.setUserInput(typeTo != -1 ? 0 : disabledFlags);
			else if (typeTo == 0) {
				if (key == KeyEvent.VK_BACK_SPACE) {
					if (pickupName.length() > 0) {
						pickupName = pickupName.substring(0, pickupName.length() - 1);
						pickupId = parse(pickupName);
					}
				} else if (pickupName.length() < 25) {
					pickupName += key;
					pickupId = parse(pickupName);
				}
			} else if (typeTo == 1)
				if (key == KeyEvent.VK_BACK_SPACE) {
					if (destroyName.length() > 0) {
						destroyName = destroyName.substring(0, destroyName.length() - 1);
						destroyId = parse(destroyName);
					}
				} else if (destroyName.length() < 25) {
					destroyName += key;
					destroyId = parse(destroyName);
				}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		controlStatus = null;
		cp = e.getPoint();
		if (contains(RECT_MAP_TAB, cp))
			paintMap = !paintMap;
		else if (contains(RECT_STATUS, cp)) {
			if (warnStatus != null) {
				warnStatus = null;
				if (!settingsFinished || rooms.size() != 1) {
					log(Color.RED, true, "Shutting down iDungeon after completing this floor.");
					log(null, true, "Please use the client's stop button if you wish to shut down immediately.");
					stopAtNext = true;
				} else {
					secondaryStatus = "";
					status = "Shutting down iDungeon";
					stopScript();
				}
			} else if (secondaryStatus.isEmpty())
				hideSecondary = false;
			else
				hideSecondary = !hideSecondary;
		} else if (contains(RECT_STOP, cp)) {
			if (warnStatus == null) {
				warnStatus = warnStatus == null ? "Click here to confirm shutdown" : null;
				showPaint = true;
			} else
				warnStatus = null;
		} else if (contains(RECT_INFO, cp))
			try {
				Desktop.getDesktop().browse(new URI("http://www.powerbot.org/community/topic/732573-idungeon/"));
			} catch (final Exception ex) {
				log(LR, false, "Unable to load the ShadowScripting website :(");
			}
		else if (contains(RECT_HELP, cp))
			try {
				Desktop.getDesktop().browse(
						new URI("http://www.powerbot.org/community/topic/733955-idungeon-choose-your-settings/"));
			} catch (final Exception ex) {
				log(LR, false, "Unable to load the ShadowScripting website :(");
			}
		else if (contains(RECT_HIDE, cp)) {
			if (!(showPaint = !showPaint))
				warnStatus = null;
		} else if (showPaint) {
			if (contains(RECT_CHAT, cp))
				showChat = !showChat;
			else if (contains(RECT_STATS, cp) && !showSettings) {
				showStats = true;
				showOptions = false;
			} else if (contains(RECT_OPTIONS, cp) && !showSettings) {
				showStats = false;
				showOptions = true;
			} else if (showPartySettings) {
				if (contains(RECT_CONTINUE, cp)) {
					showPartySettings = false;
					showStats = true;
					page = 0;
					if (!isLeader && Option.AFK.enabled())
						afking = true;
					partySet(true);
					savePartySettings();
				} else if (Option.AFK.clicked()) {
					if (developer)
						Option.COOPERATIVE.set(!Option.AFK.toggle());
				} else if (Option.COOPERATIVE.clicked()) {
					if (developer)
						Option.AFK.set(!Option.COOPERATIVE.toggle());
					else
						log(LR, false, "This option is still under development, sorry!");
				} else if (Option.CHAT.clicked())
					Option.CHAT.toggle();
				else if (Option.ONE_PLAYER.clicked()) {
					if (!Option.ONE_PLAYER.enabled())
						Option.FULL_SIZE.set(!Option.ONE_PLAYER.toggle());
				} else if (Option.FULL_SIZE.clicked())
					if (!Option.FULL_SIZE.enabled())
						Option.ONE_PLAYER.set(!Option.FULL_SIZE.toggle());
			} else if (showSettings && page == 0) {
				if (Option.MELEE.clicked()) {
					Option.MELEE.set(true);
					Option.RANGE.set(false);
					Option.MAGIC.set(false);
					Option.SECONDARY_MELEE.set(false);
				} else if (Option.RANGE.clicked()) {
					if (!Option.SECONDARY_MAGIC.enabled())
						Option.SECONDARY_MELEE.set(true);
					Option.MELEE.set(false);
					Option.RANGE.set(true);
					Option.MAGIC.set(false);
					Option.SECONDARY_RANGE.set(false);
				} else if (Option.MAGIC.clicked()) {
					if (freeVersion)
						log(LR, false,
								"Magic combat is only supported in iDungeon Pro, consider upgrading to unlock more features.");
					else {
						if (!Option.SECONDARY_RANGE.enabled())
							Option.SECONDARY_MELEE.set(true);
						Option.MELEE.set(false);
						Option.RANGE.set(false);
						Option.MAGIC.set(true);
						Option.SECONDARY_MAGIC.set(false);
					}
				} else if (Option.SECONDARY_MELEE.clicked()) {
					if (!Option.MELEE.enabled())
						if (Option.SECONDARY_MELEE.toggle()) {
							Option.SECONDARY_RANGE.set(false);
							Option.SECONDARY_MAGIC.set(false);
						} else
							Option.RING_SWITCH.set(false);
				} else if (Option.SECONDARY_RANGE.clicked()) {
					if (!Option.RANGE.enabled())
						if (Option.SECONDARY_RANGE.toggle()) {
							Option.SECONDARY_MELEE.set(false);
							Option.SECONDARY_MAGIC.set(false);
						} else
							Option.RING_SWITCH.set(false);
				} else if (Option.SECONDARY_MAGIC.clicked()) {
					if (freeVersion)
						log(LR, false,
								"Magic combat is only supported in iDungeon Pro, consider upgrading to unlock more features.");
					else if (!Option.MAGIC.enabled())
						if (Option.SECONDARY_MAGIC.toggle()) {
							Option.SECONDARY_MELEE.set(false);
							Option.SECONDARY_RANGE.set(false);
						} else
							Option.RING_SWITCH.set(false);
				} else if (Option.RING_SWITCH.clicked())
					if (freeVersion)
						log(LR, false,
								"Ring quickswitching is only supported in iDungeon Pro, consider upgrading to unlock more features.");
					else if (Option.SECONDARY_MELEE.enabled() || Option.SECONDARY_RANGE.enabled()
							|| Option.SECONDARY_MAGIC.enabled())
						Option.RING_SWITCH.toggle();
			} else if (showStats && sTab == 1) {
				if (levelGoal != 200)
					if (contains(RECT_MINI_UP, cp))
						levelGoal = levelGoal == (dungLevel > 98 ? 120 : 99) ? dungLevel + 1 : ++levelGoal;
					else if (contains(RECT_MINI_DOWN, cp))
						levelGoal = levelGoal == dungLevel + 1 ? (dungLevel > 98 ? 120 : 99) : --levelGoal;
			} else if ((showSettings && page == 1) || showOptions)
				if (contains(RECT_TAB_1, cp))
					oTab = 1;
				else if (contains(RECT_TAB_2, cp))
					oTab = 2;
				else if (!showSettings && contains(RECT_TAB_3, cp))
					oTab = 3;
				else if (oTab == 1) {
					if (Option.STYLE_SWAP.clicked()) {
						if (!settingsFinished || Melee.SLASH.enabled()) {
							if (Option.STYLE_SWAP.toggle())
								Option.STRENGTH.set(false);
						} else
							Option.STYLE_SWAP.set(false);
					} else if (Option.QUICK_PRAY.clicked())
						Option.QUICK_PRAY.toggle();
					else if (Option.PURE.clicked())
						Option.PURE.toggle();
					else if (Option.MAKE_FOOD.clicked())
						Option.MAKE_FOOD.toggle();
					else if (Option.RUNECRAFT.clicked())
						Option.RUNECRAFT.toggle();
					else if (Option.BURY.clicked())
						Option.BURY.toggle();
					else if (Option.PRAYER.clicked()) {
						if (!Option.PRAYER.toggle())
							log(LR, false, "Warning! Disabling Prayer experience will force the bot to abort dungeons");
					} else if (Option.STRENGTH.clicked()) {
						if (!Option.STRENGTH.toggle()) {
							Option.STYLE_SWAP.set(false);
							log(LR, false,
									"Warning! Disabling Strength experience will force the bot to abort many dungeons");
						}
					} else if (Option.SUMMONING.clicked())
						if (!Option.SUMMONING.toggle())
							log(LR, false,
									"Warning! Disabling summoning doors & puzzles will force the bot to abort dungeons");
				} else if (oTab == 2) {
					if (Option.NO_PRESTIGE.clicked())
						Option.NO_PRESTIGE.toggle();
					else if (Option.MEDIUM.clicked()) {
						if (freeVersion)
							log(LR, false,
									"Medium floors are only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else
							Option.MEDIUM.toggle();
					} else if (Option.DEAD_END.clicked()) {
						if (freeVersion)
							log(LR, false,
									"Advanced explorer settings are only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else
							Option.DEAD_END.toggle();
					} else if (Option.SKIP.clicked()) {
						if (freeVersion)
							log(LR, false,
									"Advanced explorer settings are only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else
							Option.SKIP.toggle();
					} else if (Option.RUSH.clicked()) {
						if (freeVersion)
							log(LR, false,
									"Rushing is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else if (!partyMode) {
							Option.RUSH.toggle();
							if (Option.RUSH.enabled())
								log(LR, false,
										"Rush settings are for advanced users only. Improper usage will lower your xp/hr.");
						}
					} else if (Option.RUSH_FOODLESS.clicked()) {
						if (freeVersion)
							log(LR, false,
									"Rushing is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else
							Option.RUSH_FOODLESS.toggle();
					} else if (Option.JOURNALS.clicked()) {
						if (freeVersion)
							log(LR, false,
									"Journal pickup is supported in iDungeon Pro, consider upgrading to unlock more features.");
						else
							Option.JOURNALS.toggle();
					} else if (contains(RECT_C_UP, cp)) {
						if (complexity < (freeVersion ? 4 : 6))
							++complexity;
						else
							complexity = 1;
					} else if (contains(RECT_C_DOWN, cp)) {
						if (complexity > 1)
							--complexity;
						else
							complexity = (freeVersion ? 4 : 6);
					} else if (contains(RECT_RC_UP, cp)) {
						if (freeVersion)
							log(LR, false,
									"Rushing is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else if (rComplexity != 6)
							++rComplexity;
						else
							rComplexity = 1;
					} else if (contains(RECT_RC_DOWN, cp)) {
						if (freeVersion)
							log(LR, false,
									"Rushing is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else if (rComplexity != 1)
							--rComplexity;
						else
							rComplexity = 6;
					} else if (contains(RECT_F_UP, cp)) {
						if (freeVersion)
							log(LR, false,
									"Rushing is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else if (maxFloor > 0)
							rushTo = rushTo < 18 || rushTo < maxFloor ? ++rushTo : 1;
						else if (rushTo < 35)
							++rushTo;
						else
							rushTo = 1;
					} else if (contains(RECT_F_DOWN, cp)) {
						if (freeVersion)
							log(LR, false,
									"Rushing is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else if (rushTo > 1)
							--rushTo;
						else if (maxFloor > 0)
							rushTo = maxFloor > 18 ? valueOf(maxFloor) : 18;
						else
							rushTo = 35;
					} else
						typeTo = -1;
				} else if (!showSettings && oTab == 3)
					if (contains(RECT_PICKUP, cp)) {
						if (freeVersion)
							log(LR, false,
									"Item binding is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else
							// Environment.setUserInput(0);
							typeTo = 0;
					} else if (contains(RECT_DESTROY, cp)) {
						if (freeVersion)
							log(LR, false,
									"Item binding is only supported in iDungeon Pro, consider upgrading to unlock more features.");
						else
							// Environment.setUserInput(0);
							typeTo = 1;
					} else {
						setUserInput(false);
						typeTo = -1;
					}
			if (showSettings) {
				if (contains(RECT_CONTINUE, cp))
					advancePage(true);
				else if (page > 0 && contains(RECT_BACK, cp))
					advancePage(false);
			} else if (showStats)
				if (contains(RECT_TAB_1, cp))
					sTab = 1;
				else if (contains(RECT_TAB_2, cp))
					sTab = 2;
				else if (contains(RECT_TAB_3, cp))
					sTab = 3;
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mp = e.getPoint();
		if (showPaint && typeTo == -1)
			setUserInput(!contains(PAINT, mp));
		if (contains(RECT_CONTROL, mp)) {
			if (contains(RECT_STOP, mp))
				controlStatus = warnStatus != null ? "Cancel Script Shutdown" : "Terminate iDungeon Pro";
			else if (contains(RECT_INFO, mp))
				controlStatus = "Visit the iDungeon forums";
			else if (contains(RECT_HELP, mp))
				controlStatus = "iDungeon help guide";
			else if (contains(RECT_HIDE, mp))
				controlStatus = "Hide paint";
		} else if (showSettings) {
			controlStatus = null;
			settingStatus = settingStatus.startsWith("Hover") ? "Hover over an option to find out more"
					: "Please choose your settings";
			if (page > 0 && contains(RECT_BACK, mp))
				settingStatus = "<<< Previous page";
			else if (contains(RECT_CONTINUE, mp))
				settingStatus = page < 5 ? settingStatus = "Next page >>>" : "Finish the settings";
			else if (contains(RECT_CHAT, mp))
				settingStatus = "Toggle your username visibility";
			else if (page == 0 && oTab == 0) {
				for (final Option o : opTab0)
					if (o.hovered()) {
						settingStatus = o.description;
						return;
					}
			} else if (page == 1)
				if (contains(RECT_TAB_1, mp))
					settingStatus = "General script settings";
				else if (contains(RECT_TAB_2, mp))
					settingStatus = "Advanced floor settings & complexity";
				else if (!showSettings && contains(RECT_TAB_3, mp))
					settingStatus = "Search and bind an item";
				else if (oTab == 1)
					for (final Option o : opTab1) {
						if (o.isMembers && !memberWorld)
							continue;
						if (o.hovered()) {
							settingStatus = o.description;
							return;
						}
					}
				else if (oTab == 2)
					if (contains(RECT_C_UP, mp) || contains(RECT_C_DOWN, mp))
						settingStatus = "Cycle the Dungeon's Complexity level";
					else if (contains(RECT_RC_UP, mp) || contains(RECT_RC_DOWN, mp))
						settingStatus = "Select the complexity to rush at";
					else if (contains(RECT_F_UP, mp) || contains(RECT_F_DOWN, mp))
						settingStatus = "Select the floor to rush to";
					else
						for (final Option o : opTab2) {
							if (o.isMembers && !memberWorld)
								continue;
							if (o.hovered()) {
								settingStatus = o.description;
								return;
							}
						}
		} else
			controlStatus = null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseDragged(MouseEvent e) {
	}

	private final int HOARDSTALKER = 11011, POLTERGEIST = 11245, UNHAPPY_GHOST = 11246, FISHING_FERRET = 11007,
			HUNTER_FERRET = 11010, POND_SKATER = 12089;
	private final int SNOWY_CORNER = 49334, WARPED_PORTAL = 32357, AGILITY_DOOR = 49693;
	private final int RAW_VILE_FISH = 17374, COOKED_VILE_FISH = 17375, CONSECRATED_HERB = 19659;
	private final int BROKEN_HOOK = 19663, FIXED_HOOK = 19664, MEAT_CORN = 19665, MEAT_ROPE = 19667,
			GRAPPLE_HOOK = 19668;
	private final int O_ARROW = 17403, O_ASHES = 17379, O_BANANA = 17381, O_RUNE = 17385, O_BONES = 17387,
			O_BOWSTRING = 17389, O_COINS = 17391, O_EEL = 17397, O_FEATHER = 17393, O_FLY_ROD = 17399,
			O_HAMMER = 17401, O_MUSHROOM = 17407, O_NEEDLE = 17409, O_POISON = 17411, O_SHIELD = 17405,
			O_SYMBOL = 17383, O_VIAL = 17413, O_WHISKEY = 17395;
	private final int FERRET_LOG = 17377, FERRET_TRAP = 17378, STATUE_SWORD = 17416, STATUE_BOW = 17418,
			STATUE_STAFF = 17420, STATUE_BLOCK = 17415, CONSTRUCT_BLOCK = 17364, LODESTONE_CRYSTAL_1 = 17376,
			LODESTONE_CRYSTAL_2 = 18635, GUARD_KEY = 17363;
	private final int FIXED_BARREL = 11073, FINISHED_BARREL = 11074;

	private final int CORIANGER = 19653, PARSLAY = 19655, CARDAMAIM = 19656, PAPREAPER = 19657, SLAUGHTERCRESS = 19658;
	private final int BARREL_BITS = 17422, RECESS_BLUE_VIAL = 19869, RECESS_GREEN_VIAL = 19871,
			RECESS_YELLOW_VIAL = 19873, RECESS_VIOLET_VIAL = 19875;
	private final int BRIDGE_PLANK = 19651, DAMAGED_BRIDGE_ROCK = 19660, DAMAGED_BRIDGE_KEYSTONE = 19661,
			STATUE_BRIDGE_ROCK = 19877;
	private final int[] PUZZLE_ITEMS = { LODESTONE_CRYSTAL_1, LODESTONE_CRYSTAL_2, CONSTRUCT_BLOCK, RECESS_BLUE_VIAL,
			RECESS_GREEN_VIAL, RECESS_YELLOW_VIAL, RECESS_VIOLET_VIAL, DAMAGED_BRIDGE_ROCK, DAMAGED_BRIDGE_KEYSTONE,
			STATUE_BRIDGE_ROCK, BARREL_BITS, BRIDGE_PLANK, GRAPPLE_HOOK, GUARD_KEY, FERRET_LOG, FERRET_TRAP,
			RAW_VILE_FISH, COOKED_VILE_FISH, CONSECRATED_HERB, BROKEN_HOOK, FIXED_HOOK, MEAT_CORN, MEAT_ROPE,
			MEAT_ROPE, CORIANGER, PARSLAY, CARDAMAIM, PAPREAPER, SLAUGHTERCRESS, STATUE_SWORD, STATUE_BOW,
			STATUE_STAFF, STATUE_BLOCK, CONSTRUCT_BLOCK };

	private final int[] GREEN_FLIP_TILES = { 49638, 49639, 49640, 54065, 39859 };
	private final int[] YELLOW_FLIP_TILES = { 49641, 49642, 49643, 54066, 39860 };
	private final int[] FLIP_TILES = { 39859, 39860, 49638, 49639, 49640, 49641, 49642, 49643, 54065, 54066 };
	private final int[] WATER_PIPES = { 49689, 49692, 54288, 39969 };
	private final int[] SNOWY_STOPS = { 49332, 49333, SNOWY_CORNER };
	private final int[] ROW_1 = { 49390, 49392, 49394, 49396, 49414, 49416, 49418, 49420, 49438, 49440, 49442, 49444,
			54336, 54338, 54340, 54342, 33713, 33785, 33787, 33812 };
	private final int[] ROW_2 = { 49398, 49400, 49402, 49404, 49422, 49424, 49426, 49428, 49446, 49448, 49450, 49452,
			54344, 54346, 54348, 54350, 33826, 33831, 33840, 33862 };
	private final int[] ROW_3 = { 49406, 49408, 49410, 49412, 49430, 49432, 49434, 49436, 49454, 49456, 49458, 49460,
			54352, 54354, 54356, 54358, 33882, 33911, 33914, 33921, 54358 };
	private final int[] GHOSTS = { 10981, 10983, 10985, 10987, 10989, 10991, 10993, 10995, 10997, 10999 };
	private final int[] FTL_STATUES = { 10966, 10967, 10968, 12114, 12960 };
	private final int[] LEVERS = { 49381, 49382, 49383, 54333, 33675 };
	private final int[] SLIDER_BACKGROUNDS = { 54321, 54322, 54323, 33674 };
	private final int[] COLORED_FERRETS = { 12167, 12165, 12171, 12169, 12173 };
	private final int[] ALL_UNWINCHED_BRIDGES = { 39912, 39913, 39920, 39921, 39929, 39930, 39931, 39948, 39949 };
	private final int[] UNGRAPPLED_WINCH_BRIDGES = { 39912, 39913, 39920, 39929, 39931, 39948 };
	private final int[] UNGRAPPLED_CHASMS = { 54237, 54238, 54239, 54240, 37265 };
	private final int[] GRAPPLED_CHASMS = { 54241, 54242, 54243, 54244, 54245, 54246, 54247, 54248, 37267 };
	private final int[] ICY_PRESSURE_PADS = { 49320, 49321, 49322, 49323 };
	private final int[] SMALL_DEBRIS = { 49615, 49616, 49617, 49618 };
	private final int[] HEADING_STATUES = { 10942, 10943, 10944, 10945, 10946, 10947, 10948, 10949, 10950, 10951,
			10952, 10953, 12117, 12118, 12119, 12120, 12952, 12953, 12954, 12955 };
	private final int[] SLIDING_STATUES = { 10954, 10955, 10956, 10957, 10958, 10959, 10960, 10961, 10962, 10963,
			10964, 10965, 12121, 12122, 12123, 12124, 12956, 12957, 12958, 12959 };
	private final int[] THREE_STATUES = { 11036, 11037, 11038, 11039, 11040, 11041, 11042, 11043, 11044, 12094, 12095,
			12096, 13057, 13058, 13059 };
	private final int[] TEN_UNARMED_STATUES = { 11012, 11013, 11014, 12106, 13049 };
	private final int[] LARGE_CRYSTALS = { 49507, 49508, 49509, 49510, 49511, 49512, 54275, 54276, 54277, 34866, 35070 };
	private final int[] TRASH = { 49315, 49316, 49317, 49318 };
	private final int[] POWER_LODESTONES = { 49570, 49571, 49572, 54235, 35858 };
	private final int[] RECESS_FOUNTAINS = { 54502, 54544, 54621, 56058 };
	private final int[] CLOSED_SARCOPHAGUS = { 54078, 54079, 54080, 54081, 39526 };
	private final int[] UNREPAIRED_STATUE_BRIDGE = { 39991, 40002, 40003, 40004 };
	private final int[] BARREL_PIPES = { 49688, 49690, 54287, 39968 };
	private final int[] DRY_BLOOD_FOUNTAIN = { 54110, 54111, 54112, 54113, 37202 };
	private final int[] FREMMY_CRATES = { 49522, 49523, 49524, 49528, 49529, 49530, 49534, 49535, 49536 };
	private final int[] CENTER_FLOWERS = { 35576, 35507, 35520, 35523, 35525, 35562, 35568, 35569 };
	private final int[] STRANGE_FLOWERS = { 35577, 35588, 35602, 35604, 35606, 35609, 35611, 35613, 35616, 35625,
			35634, 35655, 35685, 35689, 35709, 35712, 35715, 35718, 35719, 35720, 35734, 35739, 35778, 35780, 35799,
			35800, 35804, 35808, 35809, 35812, 35830, 35835 };
	private final int[] ACTIVE_PADS = { 52206, 52207, 54282, 35232 };
	private final int[] AGILITY_GROOVES = { 49665, 49666, 49667, 54311, 39908 };
	private final int[] PENDULUMS = { 49659, 49660, 49661, 54309, 39905 };
	private final int[] SPINNING_BLADES = { 49662, 49663, 49664, 54310, 39906 };
	private final int[] WALL_TRAPS = { 49656, 49657, 49658, 54308, 39904 };
	private final int[] SPINNING_WHEELS = { 49934, 49935, 49936, 53749, 55566 };
	private final int[] BRIDGE_ANVILS = { 54256, 54257, 54258, 54259, 54260, 37316 };
	private final int[] RIDDLE_DOORS = { 49600, 49601, 49602, 37197 };
	private final int[] CRUMBLING_MINES = { 49647, 49648, 49649, 53991, 39900 };
	private final int[] CLOSED_CHESTS = { 49345, 49346, 49347, 54407, 32462 };
	private final int[] DAMAGED_BRIDGES = { 54186, 54187, 54188, 54189, 35248 };
	private final int[] UNFINISHED_BRIDGES = { 54009, 54010, 54011, 54012, 39872 };
	private final int[] MONOLITHS = { 10975, 10976, 10977, 10978, 10979, 10980, 12175, 12176, 12971, 12972 };
	private final int[] SHELF_IDS = { 54423, 54424, 54425, 54426 };
	private final Point[] CARDINALS = { new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(0, -1) };
	private static final int[] CASE_IDS = { 54419, 54420, 54421, 54422 };

	private int checkPuzzles() {
		failReason = "Undeterminable components";
		lastMessage = "";
		unreachable = false;
		safeTile = null;
		if (failSafe())
			return 2;
		levelRequirement = true;
		if (floor == Floor.FROZEN) {
			if (getObjInRoom(ICY_PRESSURE_PADS) != null) {
				status = "Puzzle room: Icy Pads";
				targetRoom.hasPuzzle = true;
				if (!pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(Random.nextInt(420000, 540000));
				return puzzleIcyPressurePads();
			}
		} else {
			if (floor == Floor.ABANDONED) {
				if (getNpcInRoom("Seeker sentinel") != null) {
					status = "Puzzle room: Seeker Sentinel";
					targetRoom.hasPuzzle = true;
					targetRoom.isUnbacktrackable = true;
					if (puzzleTimer == null)
						puzzleTimer = new Timer(Random.nextInt(240000, 300000));
					return puzzleSeekerSentinel();
				}
			} else if (floor == Floor.FURNISHED) {
				if (getObjInRoom(TRASH) != null) {
					status = "Puzzle room: Sleeping Guards";
					targetRoom.hasPuzzle = true;
					if (!pickUpAll())
						return 2;
					if (puzzleTimer == null)
						puzzleTimer = new Timer(Random.nextInt(420000, 540000));
					return puzzleSleepingGuards();
				}
			} else if (floor == Floor.OCCULT) {
				if (getObjInRoom(CASE_IDS) != null) {
					status = "Puzzle room: Bookcases";
					targetRoom.hasPuzzle = true;
					if (!fightMonsters() || !pickUpAll())
						return 2;
					if (puzzleTimer == null)
						puzzleTimer = new Timer(Random.nextInt(420000, 540000));
					return puzzleBookcases();
				}
			} else if (floor == Floor.WARPED)
				if (getObjInRoom(WARPED_PORTAL) != null) {
					status = "Puzzle room: Warped Portals";
					targetRoom.hasPuzzle = true;
					if (!pickUpAll())
						return 2;
					if (puzzleTimer == null) {
						badTiles.retainAll(perm);
						puzzleTimer = new Timer(Random.nextInt(240000, 300000));
					}
					return puzzleWarpedPortals();
				}
			if (getObjInRoom(AGILITY_DOOR) != null) {
				status = "Puzzle room: Agility Maze";
				targetRoom.hasPuzzle = true;
				if (!pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(Random.nextInt(300000, 360000));
				return puzzleAgilityMaze();
			}
			if (getObjInRoom(BARREL_PIPES) != null) {
				status = "Puzzle room: Barrels";
				targetRoom.isUnbacktrackable = true;
				targetRoom.hasPuzzle = true;
				if (!fightMonsters())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(Random.nextInt(480000, 600000));
				return puzzleBarrels();
			}
			if (getObjInRoom(DRY_BLOOD_FOUNTAIN) != null) {
				status = "Puzzle room: Blood Fountain";
				targetRoom.hasPuzzle = true;
				if (!fightMonsters() || !pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(Random.nextInt(180000, 240000));
				return puzzleBloodFountain();
			}
			if (getObjInRoom(RECESS_FOUNTAINS) != null) {
				status = "Puzzle room: Colored Recesses";
				targetRoom.hasPuzzle = true;
				if (!fightMonsters() || !pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(Random.nextInt(420000, 540000));
				return puzzleColoredRecesses();
			}
			if (getObjInRoom(SMALL_DEBRIS) != null) {
				status = "Puzzle room: Collapsing Room";
				targetRoom.hasPuzzle = true;
				targetRoom.isUnbacktrackable = true;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(Random.nextInt(300000, 420000));
				return puzzleCollapsingRoom();
			}
			if (getObjInRoom(FREMMY_CRATES) != null) {
				status = "Puzzle room: Fremennik Camp";
				targetRoom.hasPuzzle = true;
				if (!pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(Random.nextInt(180000, 240000));
				return puzzleFremennikCamp();
			}
		}

		if (getObjInRoom(DAMAGED_BRIDGES) != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Damaged Bridge";
			targetRoom.hasPuzzle = true;
			if (!pickUpAll())
				return 2;
			return puzzleBridgeDamaged();
		}
		if (getNpcInRoom(HUNTER_FERRET) != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Hunter Ferret";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleHunterFerret();
		}
		if (getObjInRoom(POWER_LODESTONES) != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Lodestone Power";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleLodestonePower();
		}
		if (getObjInRoom(CLOSED_SARCOPHAGUS) != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Poltergeist";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzlePoltergeist();
		}
		if (getNpcInRoom("bloodrager") != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Ramokee Familiars";
			targetRoom.hasPuzzle = true;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleRamokeeFamiliars();
		}
		if (getObjInRoom(UNREPAIRED_STATUE_BRIDGE) != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Statue Bridge";
			targetRoom.hasPuzzle = true;
			if (!pickUpAll())
				return 2;
			return puzzleBridgeStatue();
		}
		if (getObjInRoom(CENTER_FLOWERS) != null) {
			if (floor != Floor.ABANDONED)
				waitForResponse();
			status = "Puzzle room: Strange Flowers";
			targetRoom.hasPuzzle = true;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleStrangeFlowers();
		}
		if (getObjInRoom(UNFINISHED_BRIDGES) != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Unfinished Bridge";
			targetRoom.hasPuzzle = true;
			targetRoom.hasChasm = true;
			if (!pickUpAll())
				return 2;
			return puzzleBridgeUnfinished();
		}
		if (getNpcInRoom(UNHAPPY_GHOST) != null) {
			if (floor == Floor.FROZEN)
				waitForResponse();
			status = "Puzzle room: Unhappy Ghost";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleUnhappyGhost();
		}

		if (getNpcInRoom(COLORED_FERRETS) != null) {
			status = "Puzzle room: Colored Ferrets";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(300000, 360000));
			return puzzleColoredFerrets();
		}
		if (getObjInRoom(LARGE_CRYSTALS) != null) {
			status = "Puzzle room: Lodestone Lights";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(420000, 480000));
			return puzzleLodestoneLights();
		}
		if (getNpcInRoom("Damaged construct", "Dormant construct") != null) {
			status = "Puzzle room: Damaged Construct";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleDamagedConstruct();
		}
		if (getNpcInRoom(FISHING_FERRET) != null) {
			status = "Puzzle room: Fishing for Ferrets";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(420000, 540000));
			return puzzleFishingFerret();
		}
		if (getObjInRoom(FLIP_TILES) != null) {
			status = "Puzzle room: Flip Tiles";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleFlipTiles();
		}
		if (getNpcInRoom(FTL_STATUES) != null) {
			status = "Puzzle room: Follow the Leader";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleFollowTheLeader();
		}
		if (getNpcInRoom(GHOSTS) != null) {
			status = "Puzzle room: Ghosts";
			targetRoom.hasPuzzle = true;
			if (!pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleGhosts();
		}
		if (getObjInRoom(UNGRAPPLED_CHASMS) != null) {
			status = "Puzzle room: Grapple Chasm";
			targetRoom.hasPuzzle = true;
			targetRoom.hasChasm = true;
			if (!pickUpAll())
				return 2;
			return puzzleBridgeGrapple();
		}
		if (getObjInRoom(RIDDLE_DOORS) != null) {
			status = "Puzzle room: Enigmatic Hoardstalker";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleHoardStalker();
		}
		if (getObjInRoom(CLOSED_CHESTS) != null) {
			status = "Puzzle room: Maze";
			targetRoom.hasPuzzle = true;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleMaze();
		}
		if (getNpcInRoom("Mercenary leader") != null) {
			status = "Puzzle room: Mercenary Leader";
			targetRoom.hasPuzzle = true;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(600000, 720000));
			return puzzleMercenaryLeader();
		}
		if (getNpcInRoom(MONOLITHS) != null) {
			status = "Puzzle room: Monolith";
			targetRoom.hasPuzzle = true;
			if (!pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleMonolith();
		}
		if (getNpcInRoom(POND_SKATER) != null) {
			status = "Puzzle room: Pond Skaters";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzlePondSkaters();
		}
		if (getObjInRoom(SLIDER_BACKGROUNDS) != null) {
			status = "Puzzle room: Slider Puzzle";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleSliderPuzzle();
		}
		if (getNpcInRoom(SLIDING_STATUES) != null) {
			status = "Puzzle room: Sliding Statues";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(300000, 420000));
			return puzzleSlidingStatues();
		}
		if (getObjInRoom(ROW_1) != null) {
			status = "Puzzle room: Suspicious Grooves";
			targetRoom.hasPuzzle = true;
			targetRoom.isUnbacktrackable = true;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(240000, 300000));
			return puzzleSuspiciousGrooves();
		}
		if (getNpcInRoom(TEN_UNARMED_STATUES) != null) {
			status = "Puzzle room: 'Ten' Statues";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleTenStatues();
		}
		if (getNpcInRoom(THREE_STATUES) != null) {
			status = "Puzzle room: Three Statues";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(180000, 240000));
			return puzzleThreeStatues();
		}
		if (getObjInRoom(LEVERS) != null) {
			status = "Puzzle room: Timed Switches";
			targetRoom.hasPuzzle = true;
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(Random.nextInt(480000, 600000));
			return puzzleTimedSwitches();
		}
		if (getObjInRoom(ALL_UNWINCHED_BRIDGES) != null) {
			status = "Puzzle room: Winch Bridge";
			targetRoom.hasPuzzle = true;
			if (!pickUpAll())
				return 2;
			return puzzleBridgeWinch();
		}
		return 0;
	}

	private int puzzleAgilityMaze() {
		if (!memberCheck())
			return -1;
		waitForResponse();
		final Tile backDoor = getBackDoor();
		final SceneObject blades = getObjInRoom(SPINNING_BLADES);
		getBestDoor();
		if (blades == null || backDoor == null || reachDoor() == null)
			return -1;
		if (Calculations.distance(backDoor, blades.getLocation()) < 8) {
			status = "Puzzle room: Agility Maze - Course 1";
			return puzzleAgilityCourse1();
		} else if (Calculations.distance(targetRoom.getCenter(), blades.getLocation()) < 4) {
			status = "Puzzle room: Agility Maze - Course 3";
			return puzzleAgilityCourse3();
		} else {
			status = "Puzzle room: Agility Maze - Course 2";
			return puzzleAgilityCourse2();
		}
	}

	private int puzzleAgilityCourse1() {
		final Tile backDoor = getBackDoor();
		final Tile bladeSpot = getObjLocation(50947, 52041, 54907, 54908, 55981);
		getBestDoor();
		if (nearDoor == null || backDoor == null || bladeSpot == null) {
			waitForResponse();
			return -1;
		}
		while (distTo(nearDoor.loc) > 2) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			final SceneObject blades = getObjInRoom(SPINNING_BLADES);
			final SceneObject pendulum = getObjInRoom(PENDULUMS);
			nearMonster = NPCs.getNearest(monster);
			if (nearMonster != null && distTo(nearMonster) < 3 && reachable(nearMonster, false))
				attackNpc(nearMonster);
			else if (getGoodItem() != null && distTo(nearItem.getLocation()) < 3 && reachable(nearItem, false))
				pickUpItem(nearItem);
			else if (reachableObj(blades)) {
				if (distTo(bladeSpot) < 2 || distTo(blades) > 5) {
					if (!unStick())
						walkToDoor(1);
				} else
					walkTo(bladeSpot, 0);
			} else if (reachableObj(pendulum)) {
				if (walkTo(pendulum, 0))
					waitToStart(false);
			} else if (reachableObj(getObjInRoom(WALL_TRAPS)))
				walkTo(pendulum, 1);
			else if (reachable(backDoor, true))
				doObjAction(getObjInRoom(AGILITY_GROOVES), "Step-onto");
			else
				walkToDoor(1);
			waitToStop(true);
		}
		if (!fightMonsters() || !pickUpAll())
			return 2;
		while (getObjInRoom(AGILITY_DOOR) != null) {
			if (isDead())
				teleBack();
			else if (failSafe())
				return 2;
			smartSleep(doObjAction(getObjInRoom(AGILITY_DOOR), "Open"), true);
		}
		return 1;
	}

	private int puzzleAgilityCourse2() {
		final int[] SAFE_CORNERS = { 50663, 51762, 53750, 56274 };
		final Tile backDoor = getBackDoor(), bladeTile = getObjLocation(SPINNING_BLADES);
		final Tile agilDoor1 = getNearestObjTo(backDoor, AGILITY_DOOR), agilDoor2 = getNearestObjTo(reachDoor(),
				AGILITY_DOOR);
		final Tile bladeSpot = getNearestObjTo(bladeTile, SAFE_CORNERS), pendulum = getObjLocation(PENDULUMS);
		if (agilDoor1 == null || agilDoor2 == null || bladeSpot == null || pendulum == null) {
			waitForResponse();
			return -1;
		}
		while (getObjInRoom(AGILITY_DOOR) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (reachable(agilDoor1, true) && reachable(agilDoor2, true))
				doObjAction(getObjInRoom(AGILITY_DOOR), "Open");
			else if ((nearMonster = NPCs.getNearest(monster)) != null && distTo(nearMonster) < 3
					&& reachable(nearMonster, false))
				attackNpc(nearMonster);
			else if (getGoodItem() != null && distTo(nearItem.getLocation()) < 3 && reachable(nearItem, false))
				pickUpItem(nearItem);
			else if (reachableObj(SceneEntities.getAt(bladeTile))) {
				if (distTo(pendulum) > 5 && distTo(bladeTile) < 6 && distTo(bladeSpot) > 1.5) {
					if (!isEdgeTile(myLoc())) {
						final Tile safety = getObjLocation(SAFE_CORNERS);
						walkTo(safety != null ? safety : myLoc(), safety != null ? 0 : 2);
					} else if (unStick()) {
						Time.sleep(400, 1000);
						if (!walkTo(pendulum, 2))
							walkTo(pendulum, 2);
						Time.sleep(400, 1000);
					} else
						walkTo(bladeSpot, 1);
				} else if (pendulum != null && !reachable(agilDoor1, true))
					walkToMap(pendulum, 1);
			} else {
				final SceneObject wallTrap = getObjInRoom(WALL_TRAPS);
				if (reachableObj(wallTrap)) {
					if (walkTo(wallTrap, 1))
						waitToEat(true);
				} else if (reachable(backDoor, true))
					doObjAction(getObjInRoom(AGILITY_GROOVES), "Step-onto");
			}
			waitToStop(false);
		}
		return 1;
	}

	private int puzzleAgilityCourse3() {
		final int[] WHITE_IDS = { 50687, 52075, 54934, 55823 };
		final int[] BLACK_IDS = { 52982, 51786, 54743, 56228 };
		SceneObject door;
		final SceneObject pendulumObj = getObjInRoom(PENDULUMS);
		final Tile bladeSpot2 = getTileOfObjs(WHITE_IDS, BLACK_IDS), blades = getObjLocation(SPINNING_BLADES);
		safeTile = bladeSpot2;
		final Tile backDoor = getBackDoor(), pendulum = pendulumObj.getLocation();
		while ((door = getObjInRoom(AGILITY_DOOR)) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			final SceneObject wallTrap = getObjInRoom(WALL_TRAPS);
			nearMonster = NPCs.getNearest(monster);
			if (reachable(reachDoor(), true))
				doObjAction(door, "Open");
			else if (nearMonster != null && distTo(nearMonster) < 3 && reachable(nearMonster, false))
				attackNpc(nearMonster);
			else if (getGoodItem() != null && distTo(nearItem) < 3 && reachable(nearItem, false))
				pickUpItem(nearItem);
			else if (reachableObj(getObjInRoom(SPINNING_BLADES))) {
				Tile bladeSpot1 = getNearestObjTo(blades, 54742);
				if (bladeSpot1 == null)
					bladeSpot1 = getObjLocation(49765);
				if (distTo(pendulum) < 4 || distTo(bladeSpot2) < 3)
					walkTo(pendulum, 1);
				else
					walkTo(distTo(bladeSpot1) < 2 ? bladeSpot2 : bladeSpot1, 0);
			} else if (reachableObj(wallTrap))
				walkTo(wallTrap, 1);
			else if (reachable(backDoor, true))
				doObjAction(getObjInRoom(AGILITY_GROOVES), "Step-onto");
			else
				walkToDoor(1);
			waitToStop(true);
		}
		return 1;
	}

	private int puzzleBarrels() {
		if (!memberCheck() || !training(Skills.STRENGTH))
			return -1;
		final Tile dest_pad = getObjLocation(ACTIVE_PADS);
		if (dest_pad == null)
			return -1;
		final Tile cTile = targetRoom.getCenter(), sTile = getBackDoor();
		Tile heading1 = null;
		final Tile heading2 = getObjLocation(50962, 50965, 52059, 54986, 55885);
		for (final SceneObject o : getObjsInRoom(50849, 51943, 54765, 56264)) {
			final Tile oTile = o.getLocation();
			if (getNpcAt(oTile) != null && Calculations.distance(oTile, sTile) < 7) {
				heading1 = oTile;
				break;
			}
		}
		if (heading1 == null || heading2 == null)
			return -1;
		final boolean NS = Math.abs(cTile.getY() - sTile.getY()) > 4;
		boolean push1 = true, push2 = true;
		NPC barrel1 = null, barrel2 = null, barrel3 = null, barrel4 = null, barrel5 = null;
		Tile barrel1start = null, barrel2start = null, barrel3start = null, barrel4start = null, barrel5start = null;
		if (heading1 != null) {
			barrel2 = getNpcAt(heading1);
			barrel2start = heading1;
			final int hX = heading1.getX(), hY = heading1.getY();
			final Tile[] barrel1test = { new Tile(hX - 2, hY, 0), new Tile(hX + 2, hY, 0), new Tile(hX, hY - 2, 0),
					new Tile(hX, hY + 2, 0) };
			final Tile[] barrel3test = { new Tile(hX - 1, hY, 0), new Tile(hX + 1, hY, 0), new Tile(hX, hY - 1, 0),
					new Tile(hX, hY + 1, 0) };
			for (final Tile test : barrel1test) {
				final NPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel1 = barrel;
					barrel1start = test;
					break;
				}
			}
			for (final Tile test : barrel3test) {
				final NPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel3 = barrel;
					barrel3start = test;
					break;
				}
			}
		}
		if (heading2 != null) {
			final int hX = heading2.getX(), hY = heading2.getY();
			final Tile[] barrel5test = { new Tile(hX - 2, hY, 0), new Tile(hX + 2, hY, 0), new Tile(hX, hY - 2, 0),
					new Tile(hX, hY + 2, 0) };
			for (final Tile test : getAdjacentTo(heading2)) {
				final NPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel4 = barrel;
					barrel4start = test;
					break;
				}
			}
			for (final Tile test : barrel5test) {
				final NPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel5 = barrel;
					barrel5start = test;
					break;
				}
			}
		}
		barrel2 = getNpcAt(heading1);
		barrel2start = heading1;
		final int hX = heading1.getX(), hY = heading1.getY();
		final Tile[] barrel1test = { new Tile(hX - 2, hY, 0), new Tile(hX + 2, hY, 0), new Tile(hX, hY - 2, 0),
				new Tile(hX, hY + 2, 0) };
		for (final Tile test : barrel1test) {
			final NPC barrel = getNpcAt(test);
			if (barrel != null) {
				barrel1 = barrel;
				barrel1start = barrel.getLocation();
				break;
			}
		}
		for (final Tile test : getAdjacentTo(heading1)) {
			final NPC barrel = getNpcAt(test);
			if (barrel != null) {
				barrel3 = barrel;
				barrel3start = barrel.getLocation();
				break;
			}
		}
		final GroundItem bits = getItemInRoom(targetRoom, BARREL_BITS);
		Tile turnLock = null;
		if (bits != null) {
			turnLock = bits.getLocation();
			turnTo(bits);
		}
		setMouseSpeed(1.8);
		while (getNpcInRoom(FINISHED_BARREL) == null) {
			if (!requirements()) {
				targetRoom.isUnbacktrackable = true;
				return -1;
			}
			if (failSafe())
				return 2;
			if (secondaryStatus.contains("reengage")) {
				failReason = "Timed out";
				targetRoom.isUnbacktrackable = true;
				return -1;
			}
			final NPC fixedBarrel = getNpcInRoom(FIXED_BARREL);
			if (fixedBarrel != null) {
				if (Widgets.get(945, 17).getRelativeX() == 200) {
					safeTile = dest_pad;
					final Tile bT = fixedBarrel.getLocation(), pT = dest_pad;
					boolean atPush = false;
					if (NS) {
						if (bT.getX() != pT.getX())
							atPush = walkAround(bT, bT.getX() > pT.getX() ? 1 : -1, 0, false);
					} else if (bT.getY() != pT.getY())
						atPush = walkAround(bT, 0, bT.getY() > pT.getY() ? 1 : -1, false);
					if (atPush && doNpcAction(fixedBarrel, "Push"))
						waitToSlide(fixedBarrel);
				} else {
					Tile pipe = getObjLocation(WATER_PIPES);
					if (pipe != null) {
						Tile bT = fixedBarrel.getLocation();
						if (NS) {
							if (bT.getX() != pipe.getX()) {
								final boolean atPush = walkAround(bT, bT.getX() > pipe.getX() ? 1 : -1, 0, false);
								if (atPush && doNpcAction(fixedBarrel, "Push")) {
									waitToSlide(fixedBarrel);
									if ((pipe = getObjLocation(WATER_PIPES)) != null) {
										bT = fixedBarrel.getLocation();
										if (bT.getX() == pipe.getX())
											walkTo(new Tile(bT.getX(), bT.getY() + bT.getY() - pipe.getY(), 0), 0);
									}
								}
							}
						} else if (bT.getY() != pipe.getY()) {
							final boolean atPush = walkAround(bT, 0, bT.getY() > pipe.getY() ? 1 : -1, false);
							if (atPush && doNpcAction(fixedBarrel, "Push")) {
								waitToSlide(fixedBarrel);
								if ((pipe = getObjLocation(WATER_PIPES)) != null) {
									bT = fixedBarrel.getLocation();
									if (bT.getY() == pipe.getY())
										walkTo(new Tile(bT.getX() + bT.getX() - pipe.getX(), bT.getY(), 0), 0);
								}
							}
						}
					}
				}
			} else if (!unreachable && getGoodItem() != null && distTo(nearItem) < 3) {
				if (pickUpItem(nearItem))
					waitToStop(false);
			} else if (push1) {
				if (getNpcAt(barrel1start) != null) {
					if (doNpcAction(barrel1, "Push")) {
						waitToSlide(barrel1);
						if (Menu.contains("Push"))
							moveMouseRandomly();
					}
				} else if (getNpcAt(barrel2start) != null) {
					if (doNpcAction(barrel2, "Push")) {
						waitToSlide(barrel2);
						if (Menu.contains("Push"))
							moveMouseRandomly();
					}
				} else if (getNpcAt(barrel3start) != null) {
					if (doNpcAction(barrel3, "Push")) {
						waitToSlide(barrel3);
						if (Menu.contains("Push"))
							moveMouseRandomly();
					}
				} else
					push1 = false;
			} else if (push2) {
				if (getNpcAt(barrel4start) != null) {
					if (doNpcAction(barrel4, "Push")) {
						waitToSlide(barrel4);
						if (Menu.contains("Push"))
							moveMouseRandomly();
					}
				} else if (getNpcAt(barrel5start) != null) {
					if (doNpcAction(barrel5, "Push")) {
						waitToSlide(barrel5);
						if (Menu.contains("Push"))
							moveMouseRandomly();
					}
				} else
					push2 = false;
			} else {
				final NPC brokenBarrel = getNpcInRoom(11075);
				if (brokenBarrel != null)
					if (invContains(BARREL_BITS)) {
						if (distTo(heading2) > 2 && distTo(brokenBarrel) > 1) {
							if (walkToMap(heading2, 1))
								waitToStop(false);
						} else if (doNpcAction(brokenBarrel, "Fix")) {
							waitToStop(true);
							turnTo(turnLock);
						}
					} else if (pickUpItem(getItemInRoom(targetRoom, BARREL_BITS)))
						waitToStop(false);
			}
			Time.sleep(100, 200);
		}
		if (getBestDoor()) {
			if (walkToDoor(1)) {
				if (!waitToStart(false) && !waitToStart(false))
					waitToStart(false);
				waitToEat(false);
			}
			while (distTo(nearDoor.loc) > 3) {
				if (failSafe())
					return 2;
				walkTo(distTo(heading2) > 2 ? heading2 : nearDoor.loc, 2);
				if (!moving() && walkToDoor(3) && !waitToStart(false) && !waitToStart(false))
					waitToStart(false);
				waitToEat(false);
			}
		}
		return 1;
	}

	private int puzzleBloodFountain() {
		if (!memberCheck())
			return -1;
		final int[] RUBBLE = { 54130, 54131, 54132, 54133, 54134, 541325, 54138, 54139, 54140, 54141, 54142, 37220,
				37232 };
		final int[] PILLARS = { 54118, 54122, 54123, 54124, 54125, 54126, 37207 };
		final String[] ACTIONS = { "Mine", "Fix" };
		while (getObjInRoom(54114, 54115, 54116, 54117, 37203) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			smartAction(ACTIONS, getObjInRoom(RUBBLE), getObjInRoom(PILLARS));
		}
		return 1;
	}

	private enum Case {
		YELLOW(159, 54426), RED(156, 54424), GREEN(162, 54425), BLUE(139, 54423);

		private final int bookId, shelfId;
		private boolean completed;
		private int caseId, currBook, shelfBook;
		private Point m;
		private Tile loc, shelf;

		Case(int bookId, int shelfId) {
			this.bookId = bookId;
			this.shelfId = shelfId;
			this.caseId = -1;
			this.completed = false;
			this.currBook = -1;
			this.shelfBook = -1;
			this.loc = null;
			this.shelf = null;
			this.m = null;
		}

		private boolean equals(Case c) {
			return c != null && this.bookId == c.bookId;
		}

		private boolean isDepositing() {
			return this.equals(this.target());
		}

		private Case target() {
			if (this.m != null && this.loc != null) {
				Tile curr = stepAlong(this.m, this.loc);
				curr = stepAlong(this.m, curr);
				for (int c = 0; c < 12; ++c) {
					final SceneObject test = SceneEntities.getAt(curr = stepAlong(this.m, curr));
					if (test != null && test.getId() != this.caseId) {
						if (test.getId() == this.shelfId)
							return this;
						if (intMatch(test.getId(), CASE_IDS))
							return caseAt(test.getLocation());
					}
				}
			}
			return null;
		}

		@Override
		public String toString() {
			return enumName(this.name());
		}
	}

	private int puzzleBookcases() {
		if (powerName != null) {
			severe("This puzzle is currently unsolvable due to limitations with the client.");
			failReason = "Missing client data";
			skipDoorFound = true;
			return -1;
		}
		final Case[] CASES = { Case.BLUE, Case.RED, Case.GREEN, Case.YELLOW };
		cases = new Case[4];
		Point m = null;
		o: for (int c = 0; c < 10; ++c) {
			final SceneObject heading = getObjInRoom(CASE_IDS[0]);
			if (heading != null)
				for (final Point tM : CARDINALS) {
					Tile curr = stepAlong(tM, heading.getLocation());
					for (int d = 0; d < 12; ++d) {
						final SceneObject test = SceneEntities.getAt(curr = stepAlong(tM, curr));
						if (test != null && intMatch(test.getId(), SHELF_IDS)) {
							m = tM;
							break o;
						}
					}
				}
			Time.sleep(100, 200);
		}
		if (m == null)
			return -1;
		for (int I = 0; I < cases.length; ++I) {
			final SceneObject caseObj = getObjInRoom(CASE_IDS[I]);
			if (caseObj == null)
				continue;
			Tile curr = stepAlong(m, caseObj.getLocation());
			o: for (int c = 0; c < 12; ++c) {
				final SceneObject test = SceneEntities.getAt(curr = stepAlong(m, curr));
				if (test != null)
					for (int J = 0; J < CASES.length; ++J)
						if (test.getId() == SHELF_IDS[J]) {
							cases[I] = CASES[J];
							break o;
						}
			}
		}
		secondaryStatus = "Initializing the bookcases";
		for (int I = 0; I < cases.length; ++I) {
			final Case bc = cases[I];
			bc.caseId = 54419 + I;
			final Tile loc = getObjLocation(bc.caseId);
			final Tile shelf = getObjLocation(bc.caseId);
			if (loc == null || shelf == null) {
				caseClosed();
				return -1;
			}
			bc.loc = loc;
			bc.shelf = shelf;
			bc.completed = false;
		}
		submit(new ThreadCases());
		o: for (final Case bc : cases) {
			if (developer && bc.completed)
				continue;
			NPC bookcase;
			int attempts = 0;
			while ((bookcase = getNpcAt(bc.loc)) == null) {
				if (failSafe()) {
					caseClosed();
					return -1;
				}
				if (++attempts > 100) {
					log(LR, false, "Attempting to define case " + bc.toString() + " as already completed");
					continue o;
				}
				Time.sleep(150, 200);
			}
			bc.m = orientation(bookcase);
			final Case targ = bc.target();
			if (targ != null)
				for (int d = 0; d < 5; ++d) {
					final int graphic = bookcase.getPassiveAnimation();
					if (graphic > 0 && graphic < 200) {
						targ.currBook = graphic;
						break;
					}
					Time.sleep(100, 200);
				}
		}
		boolean workingHookData = false;
		for (final Case bc : cases)
			if (bc.currBook > 100 && bc.currBook < 200) {
				workingHookData = true;
				break;
			}
		if (!workingHookData) {
			severe("This puzzle is currently unsolvable due to limitations with the client.");
			failReason = "Broken hook data";
			skipDoorFound = true;
			caseClosed();
			return -1;
		}
		waitForResponse();
		secondaryStatus = "";
		while (!puzzleSolved) {
			if (failSafe()) {
				caseClosed();
				return 2;
			}
			for (final Case curr : cases) {
				if (puzzleSolved)
					break;
				if (curr.completed || curr.currBook != curr.bookId)
					continue;
				rotation = null;
				final Tile s = getObjLocation(curr.shelfId);
				if (s != null) {
					safeTile = s;
					log(LG, false, "Successfully transferred the " + curr.toString() + " book to its case!!");
					if (!caseDeposit(curr)) {
						caseClosed();
						return 2;
					}
					Time.sleep(100, 200);
				}
			}
			for (final Case curr : cases) {
				if (puzzleSolved)
					break;
				if (curr.completed)
					continue;
				rotation = null;
				final Tile s = getObjLocation(curr.shelfId);
				if (s != null) {
					safeTile = s;
					if (!caseTransfer(curr)) {
						caseClosed();
						return 2;
					}
					if (curr.currBook == curr.bookId) {
						log(LG, false, "Successfully transferred the " + curr.toString() + " book to its case!!");
						if (!caseDeposit(curr)) {
							caseClosed();
							return 2;
						}
						break;
					} else
						log(LR, false, "Failed to transfer the " + curr.toString() + " book to its case");
					Time.sleep(100, 200);
				}
			}
			Time.sleep(100, 200);
		}
		caseClosed();
		return 1;
	}

	private Tile[] caseAdjacents(Case bc) {
		final ArrayList<Tile> cases = new ArrayList<Tile>();
		if (bc != null)
			for (final Case test : Case.values()) {
				final double distTo = Calculations.distance(bc.loc, test.loc);
				if (distTo > 0 && distTo < 6)
					cases.add(test.loc);
			}
		return cases.toArray(new Tile[cases.size()]);
	}

	private static Case caseAt(Tile loc) {
		if (loc != null)
			for (final Case c : Case.values())
				if (Calculations.distance(c.loc, loc) < 2)
					return c;
		return null;
	}

	private void caseClosed() {
		cases = null;
		rotation = null;
		for (final Case c : Case.values()) {
			c.currBook = -1;
			c.loc = null;
			c.shelf = null;
			c.m = null;
			c.completed = false;
		}
	}

	private boolean caseDeposit(Case bc) {
		secondaryStatus = "Depositing the " + bc.toString() + " book to its shelf";
		idleTimer = new Timer(0);
		while (!bc.completed && bc.currBook == bc.bookId) {
			if (failSafe())
				return false;
			if (puzzleSolved)
				return true;
			if (distTo(bc.loc) > 2)
				smartSleep(walkToMap(bc.loc, 1), false);
			else if (!bc.isDepositing())
				caseTurn(bc);
			else
				Time.sleep(200, 400);
		}
		if (bc.completed)
			severe("Successfully stored the " + bc.toString() + " book to its shelf!!");
		return true;
	}

	private boolean caseOrient(Case c, Tile dest) {
		Case targ;
		idleTimer = new Timer(0);
		while ((targ = c.target()) == null || !targ.loc.equals(dest)) {
			if (failSafe())
				return false;
			if (distTo(c.loc) > 2)
				smartSleep(walkToMap(c.loc, 1), false);
			else
				caseTurn(c);
		}
		return true;
	}

	private boolean caseTransfer(Case bc) {
		secondaryStatus = "Transferring " + bc.toString() + " to its case";
		idleTimer = new Timer(0);
		while (bc.currBook != bc.bookId) {
			if (failSafe())
				return false;
			if (puzzleSolved)
				return true;
			final Case targ = caseWith(bc.bookId);
			if (targ != null)
				if (targ.shelfBook == bc.bookId) {
					if (!caseOrient(targ, targ.loc))
						return false;
				} else if (distTo(targ.loc) > 2) {
					if (!moving())
						walkTo(targ.loc, 1);
				} else {
					final Tile t = getNearestTileTo(bc.loc, caseAdjacents(targ));
					if (t != null)
						if (!caseOrient(targ, t) || !caseOrient(caseAt(t), targ.loc))
							return false;
				}
			Time.sleep(100, 200);
		}
		secondaryStatus = "";
		return true;
	}

	private void caseTurn(Case bc) {
		rotation = bc;
		final Point iP = bc.m;
		if (doObjAction(SceneEntities.getAt(bc.loc), "Rotate")) {
			waitToStart(false);
			for (int c = 0; c < 10; ++c) {
				if (moving() || getNpcAt(bc.loc) != null)
					--c;
				else if (c > 4)
					if (iP == null) {
						if (bc.m != null)
							break;
					} else if (!iP.equals(bc.m))
						break;
				Time.sleep(150, 200);
			}
		}
	}

	private Case caseWith(int bookId) {
		for (final Case bc : Case.values())
			if (bc.currBook == bookId || bc.shelfBook == bookId)
				return bc;
		return null;
	}

	private class ThreadCases implements Runnable {
		@Override
		public void run() {
			while (cases != null && !stopScript) {
				boolean ready = false;
				for (final NPC npc : getNpcsInRoom(12177))
					if (npc.getPassiveAnimation() != -1) {
						ready = true;
						break;
					}
				if (ready) {
					for (final Case bc : Case.values()) {
						if (bc == null || bc.loc == null)
							continue;
						final NPC caseNpc = getNpcAt(bc.loc);
						if (caseNpc != null) {
							final Point original = bc.m;
							bc.m = orientation(caseNpc);
							final Case targ = bc.target();
							if (targ != null)
								if (bc.equals(targ)) {
									bc.shelfBook = caseNpc.getPassiveAnimation();
									final NPC shelfNpc = getNearestNpcTo(bc.shelf, 12177);
									if (shelfNpc != null)
										bc.currBook = shelfNpc.getPassiveAnimation();
									if (bc.shelfBook == bc.bookId)
										bc.completed = true;
								} else if (bc.equals(targ.target())) {
									final NPC targNpc = getNpcAt(targ.loc);
									bc.currBook = targNpc.getPassiveAnimation();
									targ.currBook = caseNpc.getPassiveAnimation();
								}
							if (bc.equals(rotation))
								bc.m = original;
						}
					}
					while (getNpcInRoom(12177) != null) {
						if (cases == null)
							return;
						try {
							Time.sleep(200, 400);
						} catch (final Exception e) {
						}
					}
				} else if (getNpcsInRoom(12177) != null)
					for (final Case bc : Case.values()) {
						if (bc == null || bc.loc == null || bc.shelf == null)
							continue;
						final NPC caseNpc = getNpcAt(bc.loc);
						final NPC shelfNpc = getNpcAt(bc.shelf);
						if (caseNpc != null && shelfNpc != null) {
							bc.currBook = shelfNpc.getPassiveAnimation();
							bc.shelfBook = caseNpc.getPassiveAnimation();
						}
					}
				Time.sleep(50, 100);
			}
		}
	}

	private int puzzleBridgeDamaged() {
		if (!memberCheck())
			return -1;
		nearMonster = NPCs.getNearest(monster);
		if (nearMonster != null && (combatStyle != Style.MELEE || reachable(nearMonster, false)))
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		if (puzzleTimer == null)
			puzzleTimer = new Timer(Random.nextInt(240000, 300000));
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		while (getObjInRoom(54190, 54191, 54192, 54193, 35249) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			if (invContains(DAMAGED_BRIDGE_KEYSTONE))
				toWait = doObjAction(getObjInRoom(DAMAGED_BRIDGES), "Repair");
			else {
				if (invContains(DAMAGED_BRIDGE_ROCK))
					doItemAction(invItem(DAMAGED_BRIDGE_ROCK), "Carve");
				else if (!invContains(DAMAGED_BRIDGE_KEYSTONE))
					toWait = doObjAction(getObjInRoom(54182, 54183, 54184, 54185, 35247), "Mine");
				waitToAnimate();
			}
			smartSleep(toWait, true);
		}
		targetRoom.setFlags();
		return 1;
	}

	private int puzzleBridgeGrapple() {
		if (!memberCheck())
			return -1;
		nearMonster = NPCs.getNearest(monster);
		if (nearMonster != null && (combatStyle != Style.MELEE || reachable(nearMonster, false)))
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		final String[] ACTIONS = { "Spin", "Smith", "Plunder", "Retrieve" };
		if (puzzleTimer == null)
			puzzleTimer = new Timer(Random.nextInt(240000, 300000));
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		while (getObjInRoom(GRAPPLED_CHASMS) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (getGoodItem() != null && reachable(nearItem, true))
				smartSleep(pickUpItem(nearItem), false);
			else if (invContains(GRAPPLE_HOOK)) {
				if (smartSleep(clickFar(getObjInRoom(UNGRAPPLED_CHASMS), "Grapple"), false))
					waitToStop(true);
			} else if (invContainsAll(MEAT_ROPE, FIXED_HOOK)) {
				if (useItem(MEAT_ROPE, FIXED_HOOK))
					Time.sleep(300, 600);
			} else {
				final boolean iCorn = invContains(MEAT_CORN), iRope = invContains(MEAT_ROPE);
				final boolean iBroke = invContains(BROKEN_HOOK), iHook = invContains(FIXED_HOOK);
				final SceneObject wheel = iCorn && !iRope ? getObjInRoom(SPINNING_WHEELS) : null;
				final SceneObject anvil = iBroke && !iHook ? getObjInRoom(BRIDGE_ANVILS) : null;
				final SceneObject corn = !iCorn && !iRope ? getObjInRoom(54255) : null;
				final SceneObject hooks = !iBroke && !iHook ? getObjInRoom(54251) : null;
				smartAction(ACTIONS, wheel, anvil, corn, hooks);
			}
		}
		getBestDoor();
		if (isLeader)
			while (!invContains(GGS)) {
				if (failSafe())
					return 2;
				if (getItemInRoom(targetRoom, GGS) == null || !reachable(nearItem, true))
					break;
				if (pickUpItem(nearItem))
					waitToStop(false);
				Time.sleep(300, 500);
			}
		return crossTheChasm(reachDoor());
	}

	private int puzzleBridgeStatue() {
		if (!memberCheck() || !training(Skills.STRENGTH))
			return -1;
		final int[] REPAIRED_STATUES = { 13070, 13071, 13072, 13073, 13074, 13075, 13076, 13077, 13078, 13079, 13080,
				13081, 13082, 13083, 13084, 13085, 13086, 13087, 13088 };
		final int STATUE_OBJECT = 40162;
		nearMonster = NPCs.getNearest(monster);
		if (nearMonster != null && (combatStyle != Style.MELEE || reachable(nearMonster, false)))
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		if (puzzleTimer == null)
			puzzleTimer = new Timer(Random.nextInt(240000, 300000));
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		while (getObjInRoom(40010, 40011, 54614, 40012) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			final NPC repairedStatue = getNpcInRoom(REPAIRED_STATUES);
			if (repairedStatue != null) {
				if (lastMessage.contains("cannot push")) {
					final Tile otherSide = getObjLocation(STATUE_OBJECT);
					if (otherSide == null)
						toWait = walkTo(myLoc(), 3);
					else
						walkBlockedTile(otherSide, 1);
					lastMessage = "";
				} else
					toWait = doNpcAction(repairedStatue, "Push");
			} else if (invContains(STATUE_BRIDGE_ROCK)) {
				if (doObjAction(getObjInRoom(40157, 40158, 54617, 54618, 40159), "Repair")) {
					Time.sleep(400, 800);
					if (!requirements())
						return -1;
					toWait = true;
				}
			} else
				toWait = doObjAction(getObjInRoom(39980, 39988, 54490, 54496, 54613, 39989), "Mine");
			smartSleep(toWait, true);
			waitForDamage();
		}
		targetRoom.setFlags();
		return 1;
	}

	private int puzzleBridgeUnfinished() {
		if (!memberCheck())
			return -1;
		final Tile backDoor = getBackDoor();
		getBestDoor();
		if (backDoor == null || nearDoor == null)
			return -1;
		if (puzzleTimer == null)
			puzzleTimer = new Timer(Random.nextInt(60000, 120000));
		while (!reachable(reachDoor(), true)) {
			if (failSafe())
				return 2;
			if ((nearMonster = NPCs.getNearest(monster)) != null && reachable(nearMonster, false)) {
				waitForResponse();
				if (!fightMonsters() || !pickUpAll())
					return 2;
				puzzleTimer = new Timer(Random.nextInt(60000, 120000));
			}
			if (distTo(backDoor) < 6) {
				if (clickFar(getObjInRoom(UNFINISHED_BRIDGES), "Jump"))
					smartSleep(waitToAnimate() || waitToAnimate(), true);
			} else if (!reachable(backDoor, true))
				break;
			Time.sleep(100, 200);
		}
		targetRoom.setFlags();
		return 1;
	}

	private int puzzleBridgeWinch() {
		if (!training(Skills.STRENGTH))
			return -1;
		final String[] ACTIONS = { "Spin", "Smith", "Take", "Take" };
		nearMonster = NPCs.getNearest(monster);
		if (nearMonster != null && (combatStyle != Style.MELEE || reachable(nearMonster, false)))
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		if (puzzleTimer == null)
			puzzleTimer = new Timer(Random.nextInt(240000, 300000));
		while (getObjInRoom(39915, 39924, 39935, 54643, 39950) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			final SceneObject ungrappled = getObjInRoom(UNGRAPPLED_WINCH_BRIDGES);
			if (ungrappled == null || lastMessage.startsWith("You grapple")) {
				if (lastMessage.contains("fully extended"))
					break;
				final SceneObject winch = getObjInRoom(39917, 39928, 39947, 54647, 39964);
				if (doObjAction(winch, "Operate"))
					if (waitToObject(winch, false)) {
						if (lastMessage.contains("fully extended"))
							break;
						waitToStop(true);
						if (lastMessage.contains("fully extended"))
							break;
						waitToStop(true);
						if (lastMessage.contains("fully extended"))
							break;
						waitToStop(true);
						if (lastMessage.contains("fully extended"))
							break;
					}
			} else if (invContains(GRAPPLE_HOOK)) {
				if (smartSleep(clickFar(ungrappled, "Grapple"), false))
					waitToStop(true);
			} else if (invContainsAll(MEAT_ROPE, FIXED_HOOK)) {
				if (useItem(MEAT_ROPE, FIXED_HOOK))
					Time.sleep(300, 600);
			} else {
				final boolean iCorn = invContains(MEAT_CORN), iRope = invContains(MEAT_ROPE);
				final boolean iBroke = invContains(BROKEN_HOOK), iHook = invContains(FIXED_HOOK);
				final SceneObject wheel = iCorn && !iRope ? getObjInRoom(SPINNING_WHEELS) : null;
				final SceneObject anvil = iBroke && !iHook ? getObjInRoom(BRIDGE_ANVILS) : null;
				final SceneObject corn = !iCorn && !iRope ? getObjInRoom(54566, 54568, 54570, 54646, 39959) : null;
				final SceneObject hooks = !iBroke && !iHook ? getObjInRoom(54565, 54567, 54569, 54645, 39956) : null;
				smartAction(ACTIONS, wheel, anvil, corn, hooks);
			}
			waitForDamage();
		}
		targetRoom.setFlags();
		return 1;
	}

	private int puzzleColoredFerrets() {
		final int[] BAD_PLATES = { 54365, 54367, 54369, 54371, 54373, 54375, 54377, 54379, 54381, 54383, 54385, 54387,
				54389, 54391, 54393, 54395, 54397, 54399, 54401, 54403, 33611, 33618, 33623, 33629, 33632 };
		final int[] RED_TILE = { 54364, 54366, 54368, 54370, 33609 }, BLUE_TILE = { 54372, 54374, 54376, 54378, 33617 }, GREEN_TILE = {
				54380, 54382, 54384, 54386, 33619 }, YELLOW_TILE = { 54388, 54390, 54392, 54394, 33624 }, ORANGE_TILE = {
				54396, 54398, 54400, 54402, 33631 };
		final int[][] COLORED_PLATES = { BLUE_TILE, RED_TILE, YELLOW_TILE, GREEN_TILE, ORANGE_TILE };
		while (getNpcInRoom(COLORED_FERRETS) != null)
			for (int I = 0; I < COLORED_FERRETS.length; ++I) {
				int failCount = 0;
				final SceneObject tile = getObjInRoom(COLORED_PLATES[I]);
				if (tile == null)
					continue;
				NPC ferret;
				Tile fTile;
				safeTile = tile.getLocation();
				if (!isGoodTile(safeTile)) {
					severe("Ferret tile is blocked! WTF?");
					waitForResponse();
					return 2;
				}
				while ((ferret = getNpcInRoom(COLORED_FERRETS[I])) != null
						&& !(fTile = ferret.getLocation()).equals(safeTile)) {
					if (failSafe())
						return 2;
					Tile dTile = null, wTile = null;
					if (!isEdgeTile(fTile)) {
						boolean far = Calculations.distance(fTile, safeTile) > 3;
						final int fX = fTile.getX(), fY = fTile.getY();
						final Point m = alignment(fTile, safeTile);
						if (m.x != 0 && m.y != 0) {
							far = far && Math.abs(safeTile.getX() - fX) > 1 && Math.abs(safeTile.getY() - fY) > 1;
							final Tile check1 = new Tile(fX - m.x, fY - m.y, 0);
							final Tile check2 = new Tile(fX - 2 * m.x, fY - 2 * m.y, 0);
							final Tile backup = far ? check2 : check1;
							dTile = far ? check1 : check2;
							if (!isGoodTile(dTile, BAD_PLATES) && isGoodTile(backup, BAD_PLATES))
								dTile = isGoodTile(backup) ? backup : null;
						}
						if (dTile == null && (m.x != 0 || m.y != 0))
							if (isGoodTile(new Tile(fX + m.x, fY + m.y, 0), BAD_PLATES)) {
								final Tile check1 = new Tile(fX - m.x, fY - m.y, 0);
								final Tile check2 = new Tile(fX - 2 * m.x, fY - 2 * m.y, 0);
								final Tile backup = far ? check2 : check1;
								wTile = far ? check1 : check2;
								if (!isGoodTile(wTile, BAD_PLATES))
									wTile = backup;
								if (!isGoodTile(wTile, BAD_PLATES))
									wTile = null;
							}
					}
					final Tile me = myLoc();
					if (isGoodTile(dTile)) {
						if (!me.equals(dTile)) {
							if (walkTo(dTile, 0))
								waitToStop(false);
							failCount = 0;
						} else {
							++failCount;
							Time.sleep(100, 200);
						}
					} else if (wTile != null) {
						Tile first = getNearTriple(wTile, safeTile);
						final double wDist = Calculations.distance(me, wTile), fDist = Calculations.distance(me, first);
						if (wDist < 3 || fDist < Calculations.distance(me, fTile))
							first = null;
						if (first == null || me.equals(first) || walkTo(first, 0)) {
							waitToStart(false);
							while (moving() && first != null && distTo(first) > 1) {
								if (pauseScript())
									return -1;
								Time.sleep(100, 200);
							}
							if (walkTo(wTile, 0)) {
								waitToStop(false);
								failCount = 0;
							} else {
								++failCount;
								Time.sleep(100, 200);
							}
						}
					} else if (moving() || ferret.isMoving()) {
						Time.sleep(50, 100);
						failCount = 0;
					} else {
						++failCount;
						Time.sleep(50, 100);
					}
					final Tile badTile = ferret.getLocation();
					if (failCount > 5 && (moving() || ferret.isMoving()))
						failCount = 0;
					if (failCount > 15) {
						while (failCount < 20 && ferret != null && !ferret.getLocation().equals(safeTile)
								&& (ferret.isMoving() || ferret.getLocation().equals(badTile) || !atLoc(badTile))) {
							if (failCheck())
								break;
							if (!moving())
								doNpcAction(ferret, "Scare");
							if (player().getMessage() != null && walkTo(badTile, 0))
								Time.sleep(600, 1000);
							Time.sleep(100, 800);
							++failCount;
						}
						failCount = 0;
					}
				}
			}
		return 1;
	}

	private int puzzleColoredRecesses() {
		if (!memberCheck() || !training(Skills.STRENGTH) || !slayerCheck())
			return -1;
		final int[] COLOR_VIALS = { RECESS_BLUE_VIAL, RECESS_GREEN_VIAL, RECESS_YELLOW_VIAL, RECESS_VIOLET_VIAL };
		final int[] BLUE_TILES = { 54504, 54546, 54623, 56060 }, GREEN_TILES = { 54506, 54548, 54625, 56062 }, YELLOW_TILES = {
				54508, 54550, 54627, 56064 }, VIOLET_TILES = { 54510, 54552, 54629, 56066 };
		final int[][] COLOR_TILES = { BLUE_TILES, GREEN_TILES, YELLOW_TILES, VIOLET_TILES };
		final String[] COLOR_NAMES = { "Blue", "Green", "Yellow", "Violet" };
		boolean vialsPicked = false;
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			eatFood(foods, 40, 50);
			boolean blocksAligned = true;
			o: for (final int[] color : COLOR_TILES)
				for (final SceneObject tObj : getObjsInRoom(color))
					if (getNpcAt(tObj.getLocation()) == null) {
						blocksAligned = false;
						break o;
					}
			if (blocksAligned) {
				if (vialsPicked && !invContains(COLOR_VIALS)) {
					final GroundItem vial = getItemInRoom(targetRoom, COLOR_VIALS);
					if (vial != null)
						smartSleep(pickUpItem(vial), false);
					else
						vialsPicked = false;
				}
				if (!vialsPicked) {
					final SceneObject shelves = getObjInRoom(35241, 35242, 35243, 35245, 35246);
					if (invContainsAll(COLOR_VIALS))
						vialsPicked = true;
					else if (shelves != null) {
						String color = "";
						safeTile = shelves.getLocation();
						for (final String test : COLOR_NAMES)
							if (getItemId(test + " vial") == -1) {
								color = test;
								break;
							}
						if (Widgets.get(1188).validate())
							clickDialogueOption(Widgets.get(1188), color);
						else if (distTo(safeTile) > 3)
							walkBlockedTile(safeTile, 2);
						else if (doObjAction(shelves, "Take-bottle"))
							waitToStop(false);
					}
				} else {
					for (int I = 0; I < COLOR_TILES.length; ++I) {
						if (failSafe())
							return 2;
						final SceneObject tObj = getObjInRoom(COLOR_TILES[I]);
						if (tObj == null)
							continue;
						safeTile = tObj.getLocation();
						final NPC block = getNpcAt(safeTile);
						if (block != null && getName(block).equals("Block") && invContains(COLOR_VIALS[I])) {
							final Tile bTile = block.getLocation();
							walkBlockedTile(bTile, 2);
							if (useItem(COLOR_VIALS[I], block)) {
								waitToStart(false);
								while (moving() && invContains(COLOR_VIALS[I]) && distTo(bTile) > 1) {
									if (pauseScript())
										return -1;
									Time.sleep(100, 200);
								}
							}
						}
					}
					Time.sleep(400, 600);
				}
			} else
				for (final int[] color : COLOR_TILES) {
					final SceneObject tObj = getObjInRoom(color);
					if (tObj == null)
						continue;
					NPC push;
					safeTile = tObj.getLocation();
					while ((push = getNearestNpcTo(safeTile, "block")) != null && getNpcAt(safeTile) == null) {
						if (failSafe())
							return 2;
						if (!requirements())
							return -1;
						boolean toWait = false, xPush = false, yPush = false;
						final Tile pT = push.getLocation();
						Tile walkX = null, walkY = null;
						final int pX = pT.getX(), pY = pT.getY();
						final Point m = alignment(pT, safeTile);
						if (m.x != 0) {
							final Tile checkX = new Tile(pX + m.x * 2, pY, 0);
							walkX = new Tile(pX + m.x, pY, 0);
							xPush = isGoodTile(walkX) && isGoodTile(checkX) && getNpcAt(walkX) == null
									&& getNpcAt(checkX) == null;
						}
						if (m.y != 0) {
							final Tile checkY = new Tile(pX, pY + m.y * 2, 0);
							walkY = new Tile(pX, pY + m.y, 0);
							yPush = isGoodTile(walkY) && isGoodTile(checkY) && getNpcAt(walkY) == null
									&& getNpcAt(checkY) == null;
						}
						final boolean yReady = xPush && yPush && distTo(walkY) < distTo(walkX);
						if (xPush && !yReady)
							toWait = walkBlockedTile(walkX, 0) && interact(push, "Pull");
						else if (yPush)
							toWait = walkBlockedTile(walkY, 0) && interact(push, "Pull");
						else
							break;
						if (toWait)
							waitToStart(true);
						smartSleep(toWait, true);
					}
				}
		}
		if (failSafe())
			return 2;
		getBestDoor();
		walkBlockedTile(reachDoor(), 2);
		return 1;
	}

	private int puzzleCollapsingRoom() {
		getBestDoor();
		final Tile entry = getEntry();
		final Tile center = targetRoom.getCenter(), destDoor = reachDoor();
		if (nearDoor == null || destDoor == null)
			return -1;
		logD("Coll 1");
		boolean strutting = memberWorld ? true : false;
		Tile target = entry;
		safeTile = center;
		for (int c = 0; c < 3; ++c) {
			if (!reachable(destDoor, true)) {
				if (developer && c > 0)
					severe("Waited for the rocks to fall!");
				break;
			}
			Time.sleep(400, 600);
			if (c == 2 && reachable(reachDoor(), true))
				return 2;
		}
		logD("Coll 2");
		while (!reachable(destDoor, true)) {
			if (failSafe())
				return 2;
			if (!requirements("Construction"))
				return -1;
			boolean toWait = false;
			if (secondaryStatus.contains("reengage"))
				getBestDoor();
			if (strutting && lastMessage.contains("Construction"))
				strutting = false;
			final SceneObject strut = strutting ? getReachableObj(49609, 49610, 49611, 54061, 37320) : null;
			if (strut != null) {
				if (doObjAction(strut, "Repair") && waitToStart(true))
					waitToStop(toWait = true);
			} else if (getKey() != null && reachable(nearItem, false))
				toWait = pickUpItem(nearItem);
			else if (target.equals(entry) && reachable(center, false)) {
				if (distTo(center) < 2) {
					target = nearDoor.loc;
					safeTile = nearDoor.loc;
				} else if (!moving())
					walkTo(center, 0);
			} else {
				SceneObject debris = getReachableObjNear(target, 49619, 49620, 49621, 49622, 38857, 39478);
				if (debris == null)
					debris = target.equals(entry) ? getReachableObjNear(center, SMALL_DEBRIS) : getNearObjTo(target,
							SMALL_DEBRIS);
				toWait = doObjAction(debris, "Destroy");
			}
			smartSleep(toWait, true);
		}
		targetRoom.setFlags();
		puzzleRepeat = true;
		return 1;
	}

	private int puzzleDamagedConstruct() {
		NPC construct;
		final SceneObject crate = getObjInRoom(49543, 49544, 49545, 53995);
		String part = "";
		while ((construct = getNpcInRoom("construct")) == null || !getName(construct).startsWith("Magic")) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			final int unchargedId = getItemId("Spare construct " + part + " (u)");
			if (construct != null && getName(construct).startsWith("Dormant"))
				toWait = doNpcAction(construct, "Charge");
			else if (part.isEmpty()) {
				if (!lastMessage.contains("missing") && doNpcAction(construct, "Examine"))
					waitToStop(true);
				if (lastMessage.contains("arm"))
					part = "arm";
				else if (lastMessage.contains("leg"))
					part = "leg";
				else if (lastMessage.contains("head"))
					part = "head";
			} else if (unchargedId > 0)
				doItemAction(invItem(unchargedId), "Imbue");
			else if (getItemId("Spare construct " + part) != -1)
				toWait = doNpcAction(construct, "Repair");
			else if (Widgets.get(1188).validate())
				clickDialogueOption(Widgets.get(1188), part);
			else if (invContains(CONSTRUCT_BLOCK))
				toWait = doItemAction(invItem(CONSTRUCT_BLOCK), "Carve");
			else
				toWait = doObjAction(crate, "Take");
			smartSleep(toWait, true);
		}
		getBestDoor();
		for (int c = 0; c < 40; ++c) {
			if (failSafe())
				return 2;
			if ((construct = getNpcInRoom("Magic construct")) == null || nearDoor == null) {
				getBestDoor();
				if (c > 20)
					break;
				topUp();
			} else {
				final Tile cTile = construct.getLocation();
				if (cTile == null || Calculations.distance(cTile, nearDoor.loc) < 5)
					break;
				if (!moving() && distTo(cTile) > random(5, 10))
					walkTo(cTile, 2);
				else
					topUp();
			}
			Time.sleep(600, 800);
		}
		return 1;
	}

	private int puzzleIcyPressurePads() {
		final ArrayList<Tile> pads = new ArrayList<Tile>();
		int padsRemaining = 0, safeCount = 0;
		for (final SceneObject corner : getObjsInRoom(SNOWY_CORNER))
			roomFlags.remove(corner.getLocation());
		puzzlePoints.clear();
		for (final SceneObject pad : getObjsInRoom(ICY_PRESSURE_PADS)) {
			pads.add(pad.getLocation());
			stopNodes(pad.getLocation());
		}
		Tile previous = null;
		while (padsRemaining < 4) {
			if (failSafe())
				return 2;
			Tile me = myLoc();
			padsRemaining = 4 - pads.size();
			status = "Puzzle room: Icy Pads (" + padsRemaining + " of 4)";
			final int mX = me.getX(), mY = me.getY();
			for (final Tile check : pads)
				if (straightLine(me, check)) {
					safeTile = check;
					break;
				}
			if (safeTile == null && safeCount < 3)
				for (final Tile check : puzzlePoints)
					if (straightLine(me, check))
						if (!isGoodTile(stepAlong(alignment(me, check), check))) {
							safeTile = check;
							break;
						}
			if (safeTile != null && safeCount < 3) {
				if (walkTo(safeTile.isOnScreen() ? safeTile : stepAlong(alignment(myLoc(), safeTile), myLoc()), 0))
					waitToEat(false);
				if (atLoc(safeTile) && SceneEntities.getAt(safeTile) != null) {
					puzzlePoints.clear();
					pads.remove(safeTile);
					for (final Tile pad : pads)
						stopNodes(pad);
				} else
					++safeCount;
				safeTile = null;
			} else {
				safeCount = 0;
				me = myLoc();
				o: switch (Random.nextInt(0, 13)) {
				case 0:
					final Tile center = targetRoom.getCenter();
					if (!straightLine(me, center, previous) && walkTo(center, 0)) {
						waitToEat(true);
						break;
					}
				case 1:
					final Tile rand = new Tile(mX + Random.nextInt(-1, 2), mY + Random.nextInt(-1, 2), 0);
					if (!straightLine(me, rand, previous) && isGoodTile(rand) && walkTo(rand, 0)) {
						waitToEat(true);
						break;
					}
				case 2:
				case 3:
				case 4:
					for (final Tile t : getAdjacentTo(me))
						if (!straightLine(me, t, previous) && isGoodTile(t) && walkTo(t, 0)) {
							waitToEat(true);
							break o;
						}
				case 5:
				case 6:
					Tile test = null;
					for (final Tile t : getAdjacentTo(me))
						if (!straightLine(me, t, previous) && isGoodTile(t))
							test = t;
					if (walkTo(test, 0)) {
						waitToEat(true);
						break;
					}
				case 7:
				case 8:
				case 9:
					for (final Tile t : getDiagonalTo(me))
						if (!straightLine(me, t, previous) && isGoodTile(t) && walkTo(t, 0)) {
							waitToEat(true);
							break o;
						}
				case 10:
				case 11:
				case 12:
					Tile test2 = null;
					for (final Tile t : getDiagonalTo(me))
						if (!straightLine(me, t, previous) && isGoodTile(t))
							test2 = t;
					if (walkTo(test2, 0)) {
						waitToEat(true);
						break;
					}
				}
				final Tile newLoc = myLoc();
				if (!newLoc.equals(me)) {
					for (int c = 0; c < 5; ++c) {
						if (!atLoc(newLoc))
							break;
						Time.sleep(100, 200);
					}
					previous = me;
				}
			}
			Time.sleep(100, 400);
			previous = myLoc();
		}
		SceneObject corner;
		secondaryStatus = "Navigating to the nearest corner";
		while ((corner = getObjInRoom(SNOWY_STOPS)) != null && !atLoc(safeTile = corner.getLocation())) {
			if (failSafe())
				return 2;
			smartSleep(walkTo(safeTile, 0), false);
		}
		for (final GroundItem g : getRoomItems()) {
			final Tile gTile = g.getLocation();
			final SceneObject o = SceneEntities.getAt(gTile);
			if (o == null || !intMatch(o.getId(), SNOWY_STOPS) && !badTiles.contains(gTile))
				badTiles.add(gTile);
		}
		getBestDoor();
		walkToMap(reachDoor(), 0);
		return 1;
	}

	private int puzzleFishingFerret() {
		if (!invContains(FEATHERS)) {
			if (!hasCoins(Random.nextInt(42, 60)) && !invHasSale()) {
				failReason = "Unable to buy feathers";
				return -1;
			}
			log(LP, false, "Purchasing feathers to complete this puzzle");
			getFeathers = true;
			teleHome(true);
			return 2;
		}
		final int[] FISH_PLATES = { 49555, 49556, 49557, 49558, 49559, 49560, 54296, 35326 };
		final Tile dest = getObjLocation(FISH_PLATES);
		final int[] SAFE_SPOTS = { 49546, 49547, 49548, 54293, 35293 };
		NPC ferret;
		Tile fTile;
		while ((ferret = getNpcInRoom(FISHING_FERRET)) != null && !(fTile = ferret.getLocation()).equals(dest)) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			final int fishNeeded = makeSmartPath(fTile, dest, SAFE_SPOTS).size();
			if (invCount(COOKED_VILE_FISH) < fishNeeded && !ferret.isMoving() && getNpcAt(dest) == null) {
				final SceneObject fire = getObjInRoom(FIRES);
				if (invContains(RAW_VILE_FISH)
						&& (distTo(fire) < 4 || !invContains(FEATHERS) || invCount(RAW_VILE_FISH, COOKED_VILE_FISH) > fishNeeded + 1)) {
					if (fire != null) {
						safeTile = fire.getLocation();
						final int fishCount = invCount(RAW_VILE_FISH);
						if (useItem(RAW_VILE_FISH, fire)) {
							waitToStart(true);
							while (moving() || player().getAnimation() != -1) {
								if (invCount(RAW_VILE_FISH) != fishCount)
									break;
								Time.sleep(100, 200);
							}
						}
					}
				} else if (invContains(FEATHERS)) {
					final SceneObject spot = getObjInRoom(49561, 49562, 49563, 54298, 35347);
					if (spot != null) {
						safeTile = spot.getLocation();
						if (!reachable(safeTile, true) && distTo(safeTile) > 1) {
							if (!moving())
								walkTo(spot, 1);
							Time.sleep(200, 500);
						} else if (doObjAction(spot, "Fish")) {
							waitToAnimate();
							waitToStop(true);
						}
					}
				} else {
					failReason = "Out of supplies";
					return -1;
				}
			} else {
				for (final Tile t : makeSmartPath(fTile, dest, SAFE_SPOTS)) {
					safeTile = t;
					while ((ferret = getNpcInRoom(FISHING_FERRET)) != null && getNpcAt(t) == null
							&& player().getAnimation() != 13325) {
						if (failSafe())
							return 2;
						if (!requirements())
							return -1;
						if (ferret.getLocation().equals(dest) || !invContains(COOKED_VILE_FISH))
							break;
						if (!t.isOnScreen() && !moving()) {
							walkFast(t, false, 1);
							turnTo(t);
						} else if (selectItem(COOKED_VILE_FISH) && !moving() && getNpcAt(t) == null
								&& clickTile(t, "Use")) {
							if (waitToStop(true))
								for (int c = 0; c < 10; ++c) {
									if (getNpcAt(t) == null)
										break;
									Time.sleep(200, 400);
								}
						} else
							Time.sleep(200, 400);
					}
					while (player().getAnimation() == 13325)
						Time.sleep(100, 200);
				}
				Time.sleep(400, 800);
				getBestDoor();
				while (ferret != null && ferret.isMoving()) {
					if (pauseScript())
						return -1;
					if (distTo(reachDoor()) > 3 && !moving())
						walkTo(reachDoor(), 1);
					Time.sleep(100, 200);
				}
			}
			Time.sleep(100, 200);
		}
		puzzlePoints.clear();
		return 1;
	}

	private int puzzleFlipTiles() {
		final Tile startTile = getNearestObjTo(new Tile(0, 20000, 0), FLIP_TILES);
		if (startTile == null)
			return -1;
		int[] flipColors = YELLOW_FLIP_TILES;
		int[] antiColors = GREEN_FLIP_TILES;
		String antiName = "Green";
		if (getObjsInRoom(YELLOW_FLIP_TILES).length > random(11, 13)) {
			flipColors = GREEN_FLIP_TILES;
			antiColors = YELLOW_FLIP_TILES;
			antiName = "Yellow";
		}
		for (int row = 0; row < 4; ++row) {
			final Tile currentLeader = new Tile(startTile.getX(), startTile.getY() - row, 0);
			for (int col = 0; col < 5; ++col) {
				final Tile hT = new Tile(currentLeader.getX() + col, currentLeader.getY(), 0);
				safeTile = new Tile(hT.getX(), hT.getY() - 1, 0);
				o: while (!isObjectAt(hT, flipColors)) {
					if (failSafe())
						return 2;
					if (!requirements())
						return -1;
					if (distTo(safeTile) > 4 && walkToMap(safeTile, 1))
						waitToEat(false);
					if (clickTile(safeTile, "Imbue") && waitToStart(true)) {
						if (!waitToAnimate())
							waitToAnimate();
						for (int c = 0; c < 8; ++c) {
							if (isObjectAt(hT, flipColors))
								break o;
							Time.sleep(150, 250);
						}
					}
					Time.sleep(100, 200);
				}
			}
		}
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			final SceneObject anti = getObjInRoom(antiColors);
			if (anti != null && clickTile(anti, "Force", antiName)) {
				waitToAnimate();
				topUp();
				waitToStop(false);
			}
			Time.sleep(400, 600);
		}
		return 1;
	}

	private int puzzleFollowTheLeader() {
		NPC leader = getNpcInRoom(FTL_STATUES);
		if (leader == null || !slayerCheck())
			return failSafe() ? 2 : -1;
		final Tile pad = getNearestObjTo(leader.getLocation(), ACTIVE_PADS);
		if (pad == null)
			return -1;
		safeTile = pad;
		while ((leader = getNpcInRoom(FTL_STATUES)) != null) {
			if (failSafe())
				return 2;
			if (!atLoc(pad)) {
				if (walkTo(pad, 0))
					waitToEat(false);
			} else if (leader.getAnimation() != -1) {
				final int anim = leader.getAnimation();
				if (Widgets.get(1188).validate()) {
					String emote = "";
					if (anim == 855)
						emote = "Nod head";
					else if (anim == 856)
						emote = "Shake head";
					else if (anim == 860)
						emote = "Cry";
					else if (anim == 861)
						emote = "Laugh";
					else if (anim == 863)
						emote = "Wave";
					if (emote.isEmpty())
						walkAdjacentTo(pad, 1.5);
					else {
						Time.sleep(700, 1400);
						if (clickDialogueOption(Widgets.get(1188), emote)) {
							Time.sleep(400, 800);
							while (leader.getAnimation() != -1)
								Time.sleep(300, 600);
						}
					}
				}
			}
			Time.sleep(300, 600);
		}
		return 1;
	}

	private int puzzleFremennikCamp() {
		if (!memberCheck())
			return -1;
		final String[] ACTIONS = { "Smith-battleaxes", "Fletch-bows", "Cook-fish" };
		while (getObjInRoom(FREMMY_CRATES) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			final SceneObject barCrate = getObjInRoom(49528, 49529, 49530);
			final SceneObject logCrate = getObjInRoom(49534, 49535, 49536);
			final SceneObject fishCrate = getObjInRoom(49522, 49523, 49524);
			smartAction(ACTIONS, barCrate, logCrate, fishCrate);
		}
		return 1;
	}

	private int puzzleGhosts() {
		setRetaliate(false);
		if (combatStyle == Style.MELEE) {
			tempMode = Melee.CRUSH;
			updateFightMode();
		}
		while (NPCs.getNearest(monster) != null) {
			if (failSafe())
				return 2;
			eatFood(goodFoods, 40, 50);
			if (attackNpc(getNpcInRoom(GHOSTS)))
				Time.sleep(150, 300);
			Time.sleep(50, 150);
		}
		return 1;
	}

	private int puzzleHoardStalker() {
		if (!memberCheck() || !training(Skills.SUMMONING))
			return -1;
		waitForResponse();
		final int[] ITEM_CHESTS = { 49594, 49595, 49596 }, ITEM_BARRELS = { 49597, 49598, 49599 };
		int riddleItem = -1, riddleLocation = 0;
		final GroundItem shield = getItemInRoom(targetRoom, O_SHIELD);
		SceneObject barrel = getObjInRoom(ITEM_BARRELS);
		final SceneObject chest = getObjInRoom(ITEM_CHESTS);
		Tile reachCheck = shield != null ? shield.getLocation() : null;
		String itemName = "";
		logD("Hoard 1");
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			final SceneObject lockedDoor = reachCheck == null ? getObjInRoom(RIDDLE_DOORS) : getNearObjTo(reachCheck,
					RIDDLE_DOORS);
			if (riddleItem == -1) {
				final WidgetChild riddleC = Widgets.get(1184, 13);
				if (riddleC != null && riddleC.validate()) {
					final String riddle = riddleC.getText();
					if (riddle.contains("yetsan")) {
						failReason = "Level requirements";
						return -1;
					} else if (riddle.contains("lost in winds"))
						riddleItem = O_ASHES;
					else if (riddle.contains("stones may"))
						riddleItem = O_BONES;
					else if (riddle.contains("friends fly"))
						riddleItem = O_SHIELD;
					else {
						riddleLocation = 1;
						reachCheck = chest != null ? chest.getLocation() : null;
						if (riddle.contains("veins I flow")) {
							riddleItem = O_RUNE;
							itemName = "rune";
						} else if (riddle.contains("get you almost")) {
							riddleItem = O_COINS;
							itemName = "Coins";
						} else if (riddle.contains("denizens")) {
							riddleItem = O_FLY_ROD;
							itemName = "rod";
						} else if (riddle.contains("one eye")) {
							riddleItem = O_NEEDLE;
							itemName = "Needle";
						} else if (riddle.contains("demonstrate faith")) {
							riddleItem = O_SYMBOL;
							itemName = "symbol";
						} else {
							riddleLocation = 3;
							if (riddle.contains("a deathslinger"))
								riddleItem = O_BOWSTRING;
							else if (riddle.contains("cannot illuminate"))
								riddleItem = O_FEATHER;
							else if (riddle.contains("blunt force"))
								riddleItem = O_HAMMER;
							else if (riddle.contains("make you dead"))
								riddleItem = O_ARROW;
							else {
								riddleLocation = 2;
								reachCheck = barrel != null ? barrel.getLocation() : null;
								if (riddle.contains("yellow skin")) {
									riddleItem = O_BANANA;
									itemName = "Banana";
								} else if (riddle.contains("water that")) {
									riddleItem = O_WHISKEY;
									itemName = "whiskey";
								} else if (riddle.contains("many call")) {
									riddleItem = O_MUSHROOM;
									itemName = "Mushroom";
								} else if (riddle.contains("would perish")) {
									riddleItem = O_VIAL;
									itemName = "water";
								} else if (riddle.contains("slowest of")) {
									riddleItem = O_POISON;
									itemName = "Poison";
								} else if (riddle.contains("serpent am I")) {
									riddleItem = O_EEL;
									itemName = "eel";
								}
							}
						}
					}
					if (riddleItem == -1) {
						failReason = "Unknown riddle";
						waitForResponse();
						return -1;
					}
					if (developer)
						log(null, false, "Fetching item: " + itemName + " (" + riddleItem + "); Location: "
								+ riddleLocation);
				} else
					toWait = doNpcAction(getNpcInRoom(HOARDSTALKER), "Get-Riddle");
			} else if (invContains(riddleItem)) {
				final int start = invCount(riddleItem);
				while (!puzzleSolved && invCount(riddleItem) == start) {
					if (failSafe())
						return 2;
					smartSleep(useItem(riddleItem, getNpcInRoom(HOARDSTALKER)), false);
				}
				break;
			} else if (lockedDoor != null)
				doObjAction(lockedDoor, "Open");
			else if (riddleLocation == 0 || riddleLocation == 3) {
				toWait = pickUpItem(getItemInRoom(targetRoom, riddleItem));
				Time.sleep(400, 800);
			} else if (riddleLocation == 1) {
				if (Widgets.get(1188).validate())
					clickDialogueOption(Widgets.get(1188), itemName);
				else
					toWait = doObjAction(getObjInRoom(ITEM_CHESTS), "Search");
			} else if (riddleLocation == 2) {
				for (final SceneObject o : getObjsInRoom(ITEM_BARRELS))
					if (!isEdgeTile(o.getLocation()) && reachable(o, true)) {
						barrel = o;
						break;
					}
				if (Widgets.get(1188).validate())
					clickDialogueOption(Widgets.get(1188), itemName, "More");
				else
					toWait = doObjAction(barrel, "Search");
			}
			smartSleep(toWait, true);
		}
		return 1;
	}

	private int puzzleHunterFerret() {
		if (!memberCheck())
			return -1;
		final int SET_FERRET_TRAP = 49592, COMPLETED_FERRET_TRAP = 49593;
		boolean trapping = false;
		final int trapsToSet = Random.nextInt(3, 5);
		while (getObjInRoom(COMPLETED_FERRET_TRAP) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (trapping) {
				final NPC ferret = getNpcInRoom(HUNTER_FERRET);
				if (ferret != null)
					safeTile = ferret.getLocation();
				final Tile flag = getFlag(true);
				if (flag == null || Calculations.distance(flag, safeTile) > random(3, 6))
					walkTo(safeTile, 2);
			} else {
				final int trapsSet = getObjsInRoom(SET_FERRET_TRAP).length;
				if (trapsSet >= trapsToSet) {
					trapping = true;
					continue;
				}
				if (invCount(FERRET_LOG, FERRET_TRAP) < trapsToSet - trapsSet) {
					if (doObjAction(getObjInRoom(49589, 49590, 49591, 54005, 36418), "Chop"))
						waitToStop(true);
				} else {
					double dist = 99.99;
					final Tile me = myLoc();
					Tile trapCorner = null;
					for (final Tile t : trapCorners()) {
						final double tDist = Calculations.distance(me, t);
						if (tDist < dist && SceneEntities.getAt(t) == null) {
							final Tile nearest = getNearestObjTo(t, SET_FERRET_TRAP);
							if (nearest == null || Calculations.distance(t, nearest) > 2) {
								trapCorner = t;
								dist = tDist;
							}
						}
					}
					if (trapCorner != null) {
						safeTile = trapCorner;
						if (me.equals(trapCorner)) {
							final Item trap = invItem(FERRET_TRAP);
							if (trap != null) {
								if (doItemAction(trap, "Lay") && waitToStop(false))
									waitToStop(true);
							} else if (doItemAction(invItem(FERRET_LOG), "Make"))
								waitToAnimate();
						} else if (moving()) {
							if (doItemAction(invItem(FERRET_LOG), "Make"))
								waitToAnimate();
						} else
							walkTo(trapCorner, 0);
					}
				}
			}
			Time.sleep(300, 600);
		}
		while (getObjInRoom(COMPLETED_FERRET_TRAP) != null) {
			if (failSafe())
				return 2;
			smartSleep(doObjAction(getObjInRoom(COMPLETED_FERRET_TRAP), "Pick-up"), true);
		}
		return 1;
	}

	private int puzzleLodestoneLights() {
		final int[] LIGHTS = { 49468, 49469, 49470, 49477, 49478, 49479, 49486, 49487, 49488, 54262, 54265, 54268,
				34319, 34848, 34856 };
		final int[] INACTIVES = { 49471, 49472, 49473, 49480, 49481, 49482, 49489, 49490, 49491, 49498, 49499, 49500,
				54263, 54266, 54269, 54272, 34320, 34852, 34858, 34859, 34860 };
		final int[] INDICATORS = { 49495, 49496, 49497, 54271, 34862 };
		final int[] FINISHED_CRYSTALS = { 49513, 49514, 49515, 54277, 35231 };
		final Tile cT = getObjLocation(LARGE_CRYSTALS);
		if (cT == null)
			return -1;
		final ArrayList<Tile> pads = new ArrayList<Tile>();
		SceneObject indicator = null;
		Tile startPad = null;
		for (final SceneObject obj : getObjsInRoom(ACTIVE_PADS))
			pads.add(obj.getLocation());
		while (startPad == null || getObjInRoom(INACTIVES) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (doObjAction(getObjInRoom(INACTIVES), "Power-up"))
				waitToStop(true);
			if ((indicator = getObjInRoom(INDICATORS)) != null)
				for (final Tile pad : pads)
					if (Calculations.distance(pad, indicator.getLocation()) < 3.5) {
						startPad = pad;
						break;
					}
		}
		pads.remove(startPad);
		while (getObjInRoom(FINISHED_CRYSTALS) == null)
			o: for (final Tile pad : pads) {
				int lightId = 0;
				safeTile = pad;
				while (lightId == 0) {
					if (failSafe())
						return 2;
					if (getObjInRoom(FINISHED_CRYSTALS) != null)
						return 1;
					for (final SceneObject obj : getObjsInRoom(LIGHTS))
						if (Calculations.distance(safeTile, obj.getLocation()) < 4.1) {
							if (crystalDist(cT, getObjInRoom(INDICATORS)) == crystalDist(cT, obj))
								continue o;
							lightId = obj.getId();
							break;
						}
					Time.sleep(100, 200);
				}
				walkAdjacentTo(safeTile, 1.5);
				while (safeTile != null && getObjInRoom(FINISHED_CRYSTALS) == null) {
					if (failSafe())
						return 2;
					while (getObjInRoom(lightId) != null || !atLoc(safeTile)) {
						if (failSafe())
							return 2;
						if (getObjInRoom(FINISHED_CRYSTALS) != null)
							return 1;
						if (atLoc(safeTile)) {
							if (getObjInRoom(lightId) == null)
								break;
							walkAdjacentTo(safeTile, 1.5);
						} else if (crystalDist(cT, getObjInRoom(INDICATORS)) == crystalDist(cT, getObjInRoom(lightId)))
							break;
						if (crystalDist(cT, getObjInRoom(lightId)) == 3)
							if (walkTo(safeTile, 0))
								waitToStop(false);
						Time.sleep(100, 200);
					}
					if (atLoc(safeTile)) {
						if (getObjInRoom(FINISHED_CRYSTALS) != null)
							return 1;
						Mouse.move(cT.getMapPoint(), 5, 5);
						while (atLoc(safeTile)) {
							if (getObjInRoom(FINISHED_CRYSTALS) != null)
								return 1;
							if (crystalDist(cT, getObjInRoom(INDICATORS)) == 3)
								walkToMap(cT, 1);
							Time.sleep(30, 50);
						}
					}
					for (int c = 0; c < 10; ++c) {
						final SceneObject heading = getObjInRoom(INDICATORS), light = getObjInRoom(lightId);
						if (crystalDist(cT, heading) == crystalDist(cT, light)) {
							safeTile = null;
							break;
						}
						if (getObjInRoom(FINISHED_CRYSTALS) != null)
							return 1;
						Time.sleep(100, 150);
					}
				}
			}
		return 1;
	}

	private int puzzleLodestonePower() {
		final int[] POWER_CRYSTALS = { LODESTONE_CRYSTAL_1, LODESTONE_CRYSTAL_2 };
		final int[] START_BLOCKS = { 50795, 51889, 54844, 56368 };
		final int ACTIVE_CRYSTAL = 49573;
		save(bounds, POWER_CRYSTALS);
		Tile[] jumpPath = null;
		final Tile dT = getBackDoor();
		Tile startTile = null;
		final SceneObject lodestone = getObjInRoom(POWER_LODESTONES);
		if (developer && getItemInRoom(targetRoom, LODESTONE_CRYSTAL_2) != null)
			severe("Crystal id: " + LODESTONE_CRYSTAL_2);
		if (lodestone == null)
			return -1;
		final Tile lT = lodestone.getLocation();
		if (dT.getX() - lT.getX() > 4) {
			startTile = getNearestObjTo(new Tile(20000, 20000, 0), START_BLOCKS);
			if (startTile == null)
				return -1;
			final int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new Tile[] { new Tile(sX - 1, sY, 0), new Tile(sX - 2, sY - 1, 0), new Tile(sX - 2, sY - 3, 0),
					new Tile(sX - 2, sY - 5, 0), new Tile(sX - 3, sY - 6, 0), new Tile(sX - 5, sY - 6, 0) };
		} else if (dT.getX() - lT.getX() < -4) {
			startTile = getNearestObjTo(new Tile(20000, 0, 0), START_BLOCKS);
			if (startTile == null)
				return -1;
			final int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new Tile[] { new Tile(sX + 1, sY, 0), new Tile(sX + 2, sY + 1, 0), new Tile(sX + 2, sY + 3, 0),
					new Tile(sX + 2, sY + 5, 0), new Tile(sX + 3, sY + 6, 0), new Tile(sX + 5, sY + 6, 0) };
		} else if (dT.getY() - lT.getY() < -4) {
			startTile = getNearestObjTo(new Tile(20000, 0, 0), START_BLOCKS);
			if (startTile == null)
				return -1;
			final int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new Tile[] { new Tile(sX, sY + 1, 0), new Tile(sX - 1, sY + 2, 0), new Tile(sX - 3, sY + 2, 0),
					new Tile(sX - 5, sY + 2, 0), new Tile(sX - 6, sY + 3, 0), new Tile(sX - 6, sY + 5, 0) };
		} else if (dT.getY() - lT.getY() > 4) {
			startTile = getNearestObjTo(ORIGIN, START_BLOCKS);
			if (startTile == null)
				return -1;
			final int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new Tile[] { new Tile(sX, sY - 1, 0), new Tile(sX + 1, sY - 2, 0), new Tile(sX + 3, sY - 2, 0),
					new Tile(sX + 5, sY - 2, 0), new Tile(sX + 6, sY - 3, 0), new Tile(sX + 6, sY - 5, 0) };
		} else
			return -1;
		dropItem(GGS);
		if (gateRoom == null && !castable(Dungeoneering.GROUP_GATESTONE_TELEPORT))
			makeGatestone(false);
		boolean stumbled = false;
		while (getObjInRoom(ACTIVE_CRYSTAL) == null) {
			if (failSafe()) {
				if (targetRoom.hasPuzzle)
					if (getItemInRoom(targetRoom, GGS) == null || !reachable(nearItem, false))
						teleHomeAndBack();
				return 2;
			}
			boolean toWait = false;
			eatFood(foods, 50, 50);
			if (!invContains(POWER_CRYSTALS))
				toWait = pickUpItem(getItemInRoom(targetRoom, POWER_CRYSTALS));
			else {
				if (stumbled && getNpcInRoom("Guardian sphere") == null)
					stumbled = false;
				if (stumbled) {
					double bestDist = 10;
					final Tile safety = getObjLocation(START_BLOCKS);
					if (safety != null) {
						for (final Tile t : getAdjacentTo(myLoc())) {
							final double dist = Calculations.distance(t, safety);
							if (dist < bestDist) {
								bestDist = dist;
								safeTile = t;
							}
						}
						toWait = safeTile != null && clickTile(safeTile, "Jump-over");
					}
				} else {
					final Tile me = myLoc();
					Tile nextJump = jumpPath[0];
					String nextAction = "Jump";
					safeTile = null;
					for (int I = jumpPath.length - 1; I > 0; I--) {
						final Tile next = jumpPath[I];
						if (I == 5 && Calculations.distance(me, lT) < 4.5) {
							nextJump = lT;
							nextAction = "Unlock";
							break;
						}
						if (Calculations.distance(me, next) == 1) {
							safeTile = next;
							nextJump = next;
							break;
						}
					}
					if (doObjAction(SceneEntities.getAt(nextJump), nextAction)) {
						o: for (int c = 0; c < 10; ++c) {
							switch (player().getAnimation()) {
							case 769:
								break o;
							case 846:
								stumbled = true;
								break o;
							case 13493:
								log(LR, false, "Damn, we got hit by the orb");
								break o;
							}
							Time.sleep(200, 300);
						}
						toWait = true;
					}
				}
			}
			smartSleep(toWait, true);
		}
		idleTimer = new Timer(0);
		puzzleTimer.reset();
		if (!teleHomeAndBack()) {
			targetRoom.isUnbacktrackable = true;
			failReason = "Unable to teleport out";
			return -1;
		}
		return 1;
	}

	private int puzzleMaze() {
		int[] centerPath, currentPath, edgePath;
		Tile[] barriers;
		boolean NS = false;
		int mX = 1, mY = 1;
		SceneObject chest = getObjInRoom(CLOSED_CHESTS);
		final SceneObject lever = getObjInRoom(49351, 49352, 49353, 54409);
		if (chest == null || lever == null)
			return -1;
		final Tile cTile = chest.getLocation(), sTile = lever.getLocation();
		Tile hT = null;
		if (cTile.getX() - sTile.getX() < -4)
			hT = targetRoom.nearestCornerTo(ORIGIN);
		else if (cTile.getX() - sTile.getX() > 4) {
			mX = -1;
			mY = -1;
			hT = targetRoom.nearestCornerTo(new Tile(20000, 20000, 0));
		}
		if (cTile.getY() - sTile.getY() < -4) {
			mX = -1;
			hT = targetRoom.nearestCornerTo(new Tile(20000, 0, 0));
			NS = true;
		} else if (cTile.getY() - sTile.getY() > 4) {
			mY = -1;
			hT = targetRoom.nearestCornerTo(new Tile(0, 20000, 0));
			NS = true;
		}
		if (hT == null)
			return -1;
		final int hX = hT.getX(), hY = hT.getY();
		final Tile[] barriersNS = new Tile[] { new Tile(hX + 14 * mX, hY + 5 * mY, 0),
				new Tile(hX + 14 * mX, hY + 7 * mY, 0), new Tile(hX + 14 * mX, hY + 12 * mY, 0),
				new Tile(hX + 13 * mX, hY + 8 * mY, 0), new Tile(hX + 13 * mX, hY + 6 * mY, 0),
				new Tile(hX + 11 * mX, hY + 2 * mY, 0), new Tile(hX + 11 * mX, hY + 7 * mY, 0),
				new Tile(hX + 10 * mX, hY + 3 * mY, 0), new Tile(hX + 10 * mX, hY + 8 * mY, 0),
				new Tile(hX + 10 * mX, hY + 13 * mY, 0), new Tile(hX + 8 * mX, hY + 1 * mY, 0),
				new Tile(hX + 8 * mX, hY + 4 * mY, 0), new Tile(hX + 8 * mX, hY + 10 * mY, 0),
				new Tile(hX + 8 * mX, hY + 14 * mY, 0), new Tile(hX + 7 * mX, hY + 5 * mY, 0),
				new Tile(hX + 6 * mX, hY + 3 * mY, 0), new Tile(hX + 6 * mX, hY + 11 * mY, 0),
				new Tile(hX + 5 * mX, hY + 2 * mY, 0), new Tile(hX + 5 * mX, hY + 7 * mY, 0),
				new Tile(hX + 5 * mX, hY + 12 * mY, 0), new Tile(hX + 4 * mX, hY + 6 * mY, 0),
				new Tile(hX + 4 * mX, hY + 9 * mY, 0), new Tile(hX + 4 * mX, hY + 13 * mY, 0),
				new Tile(hX + 4 * mX, hY + 14 * mY, 0), new Tile(hX + 3 * mX, hY + 9 * mY, 0),
				new Tile(hX + 2 * mX, hY + 7 * mY, 0), new Tile(hX + 1 * mX, hY + 3 * mY, 0),
				new Tile(hX + 1 * mX, hY + 9 * mY, 0), new Tile(hX + 1 * mX, hY + 11 * mY, 0) };
		final Tile[] barriersEW = new Tile[] { new Tile(hX + 5 * mX, hY + 14 * mY, 0),
				new Tile(hX + 7 * mX, hY + 14 * mY, 0), new Tile(hX + 12 * mX, hY + 14 * mY, 0),
				new Tile(hX + 8 * mX, hY + 13 * mY, 0), new Tile(hX + 6 * mX, hY + 12 * mY, 0),
				new Tile(hX + 2 * mX, hY + 11 * mY, 0), new Tile(hX + 7 * mX, hY + 11 * mY, 0),
				new Tile(hX + 3 * mX, hY + 10 * mY, 0), new Tile(hX + 8 * mX, hY + 10 * mY, 0),
				new Tile(hX + 13 * mX, hY + 10 * mY, 0), new Tile(hX + 1 * mX, hY + 8 * mY, 0),
				new Tile(hX + 4 * mX, hY + 8 * mY, 0), new Tile(hX + 10 * mX, hY + 8 * mY, 0),
				new Tile(hX + 14 * mX, hY + 8 * mY, 0), new Tile(hX + 5 * mX, hY + 7 * mY, 0),
				new Tile(hX + 3 * mX, hY + 6 * mY, 0), new Tile(hX + 11 * mX, hY + 6 * mY, 0),
				new Tile(hX + 2 * mX, hY + 5 * mY, 0), new Tile(hX + 7 * mX, hY + 5 * mY, 0),
				new Tile(hX + 12 * mX, hY + 5 * mY, 0), new Tile(hX + 6 * mX, hY + 4 * mY, 0),
				new Tile(hX + 9 * mX, hY + 4 * mY, 0), new Tile(hX + 13 * mX, hY + 4 * mY, 0),
				new Tile(hX + 14 * mX, hY + 4 * mY, 0), new Tile(hX + 9 * mX, hY + 3 * mY, 0),
				new Tile(hX + 7 * mX, hY + 2 * mY, 0), new Tile(hX + 3 * mX, hY + 1 * mY, 0),
				new Tile(hX + 9 * mX, hY + 1 * mY, 0), new Tile(hX + 11 * mX, hY + 1 * mY, 0) };
		SceneObject test1, test2, test3, test4, test5;
		if (NS) {
			barriers = barriersNS;
			test1 = SceneEntities.getAt(new Tile(hX + 4 * mX, hY + 5 * mY, 0));
			test2 = SceneEntities.getAt(new Tile(hX + 9 * mX, hY + 2 * mY, 0));
			test3 = SceneEntities.getAt(new Tile(hX + 5 * mX, hY + 2 * mY, 0));
			test4 = SceneEntities.getAt(new Tile(hX + 3 * mX, hY + 5 * mY, 0));
			test5 = SceneEntities.getAt(new Tile(hX + 3 * mX, hY + 1 * mY, 0));
		} else {
			barriers = barriersEW;
			test1 = SceneEntities.getAt(new Tile(hX + 5 * mX, hY + 4 * mY, 0));
			test2 = SceneEntities.getAt(new Tile(hX + 2 * mX, hY + 9 * mY, 0));
			test3 = SceneEntities.getAt(new Tile(hX + 2 * mX, hY + 5 * mY, 0));
			test4 = SceneEntities.getAt(new Tile(hX + 5 * mX, hY + 3 * mY, 0));
			test5 = SceneEntities.getAt(new Tile(hX + 1 * mX, hY + 3 * mY, 0));
		}
		if (test1 != null && test1.getType() == SceneEntities.TYPE_INTERACTIVE) {
			centerPath = new int[] { 14, 29, 27, 26, 16, 7, 15 };
			edgePath = new int[] { 15, 7, 16, 26, 27 };
		} else if (test3 != null && test3.getType() == SceneEntities.TYPE_INTERACTIVE) {
			centerPath = new int[] { 14, 26, 16, 8, 4, 10, 20, 7, 13 };
			edgePath = new int[] { 15, 7, 20, 10, 1 };
		} else if (test4 != null && test4.getType() == SceneEntities.TYPE_INTERACTIVE) {
			centerPath = new int[] { 14, 24, 29, 26, 10, 4, 8, 7, 15 };
			edgePath = new int[] { 13, 7, 8, 4, 1 };
		} else if (test2 != null && test2.getType() == SceneEntities.TYPE_INTERACTIVE) {
			centerPath = new int[] { 14, 1, 11, 27, 29, 26, 16, 22, 13 };
			edgePath = new int[] { 15, 22, 16, 26, 29 };
		} else if (test5 != null && test5.getType() == SceneEntities.TYPE_INTERACTIVE) {
			centerPath = new int[] { 14, 29, 27, 4, 8, 22, 15 };
			edgePath = new int[] { 15, 22, 8, 4, 1 };
		} else {
			failReason = "Unknown maze layout";
			return -1;
		}
		threadPitch(100);
		while (!roomSwitch) {
			if (failSafe())
				return 2;
			if (!cooperativeParty || !isLeader || !partyPresent())
				smartSleep(doObjAction(SceneEntities.getAt(sTile), "Pull"), false);
			else
				Time.sleep(100, 200);
		}
		while (!puzzleSolved) {
			if (distTo(sTile) < 4)
				currentPath = centerPath;
			else if (distTo(cTile) < 3)
				currentPath = edgePath;
			else {
				failReason = "Unknown barrier path";
				return -1;
			}
			if (developer) {
				puzzlePoints.clear();
				for (int I = 0; I < currentPath.length; ++I)
					puzzlePoints.add(barriers[currentPath[I] - 1]);
			}
			for (int I = 0; I < currentPath.length; ++I) {
				if (isDead())
					teleBack();
				else if (failSafe())
					return 2;
				final Tile bTile = barriers[currentPath[I] - 1];
				boolean barrierClicked = false;
				final Timer barrierTimer = new Timer(Random.nextInt(20000, 30000));
				safeTile = bTile;
				unreachable = false;
				o: while (player().getAnimation() != 9516 || distTo(bTile) > 2) {
					if (!barrierTimer.isRunning()) {
						failReason = "We got stuck";
						return -1;
					}
					if (isDead())
						teleBack();
					else if (failSafe())
						return 2;
					if (barrierClicked && distTo(bTile) < 3 && player().getAnimation() == -1 && !moving())
						waitToAnimate();
					if ((!barrierClicked || (player().getAnimation() == -1 && !moving()))
							&& doObjAction(SceneEntities.getAt(bTile), "Go-through")) {
						barrierClicked = true;
						while (moving() && player().getAnimation() != 9516) {
							if (isDead())
								teleBack();
							else if (failSafe())
								return -1;
							Time.sleep(100, 200);
						}
						for (int c = 0; c < 10; ++c) {
							if (distTo(bTile) < 3) {
								if (player().getAnimation() == 9516)
									break o;
								if (!moving())
									++c;
							}
							Time.sleep(200, 300);
						}
					} else
						while (!bTile.isOnScreen() && moving() && player().getAnimation() == -1) {
							if (pauseScript())
								return -1;
							Time.sleep(200, 300);
						}
					if (unreachable) {
						I = (I == 0 ? currentPath.length : I) - 2;
						break;
					}
				}
				waitToStop(true);
				while (player().getAnimation() == 9516)
					Time.sleep(300, 400);
			}
			if (Arrays.equals(currentPath, edgePath) && isEdgeTile(myLoc()))
				break;
			if ((chest = getObjInRoom(CLOSED_CHESTS)) != null && distTo(chest) < 3)
				while ((chest = getObjInRoom(CLOSED_CHESTS)) != null || nearItem != null) {
					if (isDead())
						teleBack();
					else if (failSafe())
						return -1;
					getKey();
					if (chest != null)
						doObjAction(getObjInRoom(CLOSED_CHESTS), "Open");
					else
						pickUpItem(nearItem);
					waitToStop(false);
				}
		}
		return 1;
	}

	private int puzzleMercenaryLeader() {
		NPC leader = getNpcInRoom("Mercenary leader");
		setPrayer(false, Style.MAGIC, true);
		while (leader != null) {
			if (failSafe())
				return 2;
			attackNpc(leader);
			if (unreachable) {
				topUp();
				Time.sleep(600, 900);
				unreachable = false;
			}
			if (eatFood(goodFoods, 50, 55))
				attackNpc(leader);
			Time.sleep(500, 1000);
			leader = getNpcInRoom("Mercenary leader");
		}
		setPrayersOff();
		if (failSafe())
			return 2;
		return 1;
	}

	private int puzzleMonolith() {
		NPC monolith;
		if (distTo(targetRoom.getCenter()) > 4)
			walkFast(targetRoom.getCenter(), false, 2);
		swapStyle(Style.RANGE);
		if (combatStyle == Style.MELEE) {
			tempMode = Melee.SLASH;
			updateFightMode();
			setRetaliate(false);
		} else
			threadMid();
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if ((monolith = getNpcInRoom(MONOLITHS)) != null)
				if (!intMatch(getId(monolith), 10978, 10979, 10980, 12176, 12972)) {
					safeTile = monolith.getLocation();
					if (doNpcAction(monolith, "Activate"))
						waitToStop(false);
				} else {
					if ((nearMonster = NPCs.getNearest(monolithShade)) == null)
						nearMonster = NPCs.getNearest(monster);
					if (nearMonster == null) {
						safeTile = monolith.getLocation();
						if (distTo(safeTile) > 2 && !moving())
							walkToMap(safeTile, 1);
						if (!topUp())
							Time.sleep(300, 600);
					} else if (attackNpc(nearMonster))
						safeTile = null;
					eatFood(goodFoods, 50, 50);
				}
			Time.sleep(100, 200);
		}
		return 1;
	}

	private int puzzlePoltergeist() {
		if (!memberCheck() || !training(Skills.PRAYER) || !slayerCheck())
			return -1;
		final int[] OPENED_SARCOPHAGUS = { 54082, 54083, 54084, 54085, 39840 };
		final int[] UNFILLED_CENSERS = { 54095, 54096, 54097, 54098, 39847 };
		final int[] UNLIT_CENSERS = { 54099, 54100, 54101, 54102, 39850 };
		final int[] HERB_PATCH = { 54074, 54075, 54076 };
		String herb = "";
		while (getObjInRoom(OPENED_SARCOPHAGUS) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			final int herbId = getItemId(herb);
			final SceneObject unlit = getObjInRoom(UNLIT_CENSERS);
			final SceneObject herbPatch = getObjInRoom(HERB_PATCH);
			if (getNpcInRoom(POLTERGEIST) == null)
				toWait = doObjAction(getObjInRoom(CLOSED_SARCOPHAGUS), "Open");
			else if (herb.isEmpty()) {
				final WidgetChild clueC = Widgets.get(1186, 1);
				if (clueC != null && clueC.validate()) {
					final String clueText = clueC.getText();
					if (clueText.contains("corianger"))
						herb = "Corianger";
					else if (clueText.contains("explosemary"))
						herb = "Explosemary";
					else if (clueText.contains("parslay"))
						herb = "Parslay";
					else if (clueText.contains("cardamaim"))
						herb = "Cardamaim";
					else if (clueText.contains("papreaper"))
						herb = "Papreaper";
					else if (clueText.contains("slaughtercress"))
						herb = "Slaughtercress";
					else {
						failReason = "Unknown herb";
						return -1;
					}
				} else
					toWait = doObjAction(getObjInRoom(CLOSED_SARCOPHAGUS), "Read");
			} else if (Widgets.get(1188).validate())
				clickDialogueOption(Widgets.get(1188), herb, "More");
			else if (unlit != null) {
				if (!moving())
					doObjAction(unlit, "Light");
			} else if (herbPatch != null) {
				doItemAction(invItem(herbId), "Consecrate");
				if (doObjAction(herbPatch, "Harvest") && distTo(herbPatch) > 1)
					waitToStop(false);
			} else if (herbId > 0 && invContains(herbId)) {
				doItemAction(invItem(herbId), "Consecrate");
				Time.sleep(700, 1100);
			} else if (invContains(CONSECRATED_HERB))
				toWait = useItem(CONSECRATED_HERB, getObjInRoom(UNFILLED_CENSERS));
			else if (GroundItems.getNearest(CONSECRATED_HERB) != null)
				toWait = pickUpItem(GroundItems.getNearest(CONSECRATED_HERB));
			smartSleep(toWait, true);
		}
		return 1;
	}

	private int puzzlePondSkaters() {
		NPC targ = getNpcInRoom(POND_SKATER);
		while (getNpcInRoom(12090) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (targ == null)
				targ = NPCs.getNearest(skater);
			else if (Walking.getEnergy() < random(15, 25))
				restTo(Random.nextInt(50, 80));
			else if (doNpcAction(targ, "Catch")) {
				Time.sleep(600, 800);
				for (int c = 0; c < 15; ++c) {
					if (!interactingWith(targ))
						break;
					if (!moving() || player().getAnimation() != -1 || Widgets.get(1188).validate())
						break;
					Time.sleep(250, 300);
				}
			}
			Time.sleep(100, 200);
		}
		return 1;
	}

	private int puzzleRamokeeFamiliars() {
		final String[] RAMOKEE_TYPES = { "skinweaver", "stormbringer", "deathslinger", "bloodrager" };
		boolean normals = true;
		while (getNpcInRoom("Ramokee") != null) {
			if (failSafe())
				return 2;
			boolean toWait = false;
			NPC targ = null;
			setPrayer(true, Style.SUMMON, true);
			if (normals) {
				if ((nearMonster = NPCs.getNearest(monster)) != null)
					targ = nearMonster;
				else
					normals = false;
			} else
				for (final String ramokee : RAMOKEE_TYPES)
					if ((targ = getGoodNpc(ramokee)) != null)
						break;
			if (attackNpc(targ)) {
				getBestStyle(targ);
				toWait = true;
			}
			eatFood(goodFoods, 40, 50);
			smartSleep(toWait, false);
		}
		setPrayersOff();
		return 1;
	}

	private int puzzleTenStatues() {
		final int[] FINISHED_STATUES = { 11015, 11016, 11017, 12107, 13050 };
		final int[] MELEE_STATUES = { 11027, 11028, 11029, 12111, 13054 };
		final int[] RANGE_STATUES = { 11030, 11031, 11032, 12112, 13055 };
		final int[] MAGIC_STATUES = { 11033, 11034, 11035, 12113, 13056 };
		final int[][] ARMED_STATUES = { MELEE_STATUES, RANGE_STATUES, MAGIC_STATUES };
		final int[] STONE_WEAPONS = { STATUE_STAFF, STATUE_SWORD, STATUE_BOW };
		final String[] STONE_NAMES = { "Staff", "Sword", "Bow" };
		final int meleeCount = getNpcsInRoom(MELEE_STATUES).length;
		final int rangeCount = getNpcsInRoom(RANGE_STATUES).length;
		final int magicCount = getNpcsInRoom(MAGIC_STATUES).length;
		while (getNpcInRoom(FINISHED_STATUES) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (invCount(STONE_WEAPONS) < getNpcsInRoom(TEN_UNARMED_STATUES).length) {
				if (Widgets.get(1188).validate()) {
					String itemName = "";
					if (invCount(STATUE_STAFF) < meleeCount)
						itemName = "Staff";
					else if (invCount(STATUE_SWORD) < rangeCount)
						itemName = "Sword";
					else if (invCount(STATUE_BOW) < magicCount)
						itemName = "Bow";
					clickDialogueOption(Widgets.get(1188), itemName);
				} else if (invContains(STATUE_BLOCK)) {
					if (doItemAction(invItem(STATUE_BLOCK), "Carve"))
						forSleep(Widgets.get(1188), true);
				} else if (doObjAction(getObjInRoom(CRUMBLING_MINES), "Mine"))
					waitToStop(true);
			} else {
				final NPC nextStatue = getNpcInRoom(TEN_UNARMED_STATUES);
				if (nextStatue != null) {
					NPC heading = null;
					safeTile = nextStatue.getLocation();
					final int sX = safeTile.getX(), sY = safeTile.getY();
					final Tile[] testStatues = { new Tile(sX - 4, sY, 0), new Tile(sX + 4, sY, 0),
							new Tile(sX, sY - 4, 0), new Tile(sX, sY + 4, 0) };
					for (final Tile test : testStatues)
						if ((heading = getNpcAt(test)) != null) {
							if (!intMatch(getId(heading), TEN_UNARMED_STATUES))
								break;
							heading = null;
						}
					if (heading != null) {
						int idx = -1;
						for (int I = 0; I < ARMED_STATUES.length; ++I)
							if (intMatch(getId(heading), ARMED_STATUES[I])) {
								idx = I;
								break;
							}
						if (idx != -1 && invContains(STONE_WEAPONS[idx])) {
							if (!moving() && distTo(safeTile) > 3 && walkTo(safeTile, 1))
								waitToStart(false);
							if (doNpcAction(nextStatue, "Arm")) {
								waitToStop(true);
								if (clickDialogueOption(Widgets.get(1188), STONE_NAMES[idx]))
									Time.sleep(400, 600);
							}
						}
					}
				} else if (!moving() && (nearDoor == null || distTo(nearDoor.loc) > 2)) {
					getBestDoor();
					walkToDoor(2);
				}
			}
			Time.sleep(100, 300);
			if (player().isInCombat()) {
				topUp();
				waitToStop(true);
			}
		}
		return 1;
	}

	private int puzzleThreeStatues() {
		final int[] UNARMED_MELEER = { 11036, 11037, 11038, 12094, 13057 };
		final int[] UNARMED_RANGER = { 11039, 11040, 11041, 12095, 13058 };
		final int[] UNARMED_MAGER = { 11042, 11043, 11044, 12096, 13059 };
		final int[] STONE_WEAPONS = { 17416, 17418, 17420 };
		final int[][] UNARMED_STATUES = { UNARMED_MELEER, UNARMED_RANGER, UNARMED_MAGER };
		final String[] STONE_NAMES = { "Sword", "Bow", "Staff" };
		int[] equipStatues = null;
		int weaponId = -1;
		String itemName = "";
		for (int I = 0; I < UNARMED_STATUES.length; ++I)
			if (getNpcInRoom(UNARMED_STATUES[I]) != null) {
				equipStatues = UNARMED_STATUES[I];
				weaponId = STONE_WEAPONS[I];
				itemName = STONE_NAMES[I];
			}
		if (equipStatues == null)
			return -1;
		while (getNpcInRoom(THREE_STATUES) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			if (invContains(weaponId))
				toWait = doNpcAction(getNpcInRoom(equipStatues), "Arm");
			else if (Widgets.get(1188).validate())
				clickDialogueOption(Widgets.get(1188), itemName);
			else if (invContains(STATUE_BLOCK)) {
				dodgeSlayerMonster();
				doItemAction(invItem(STATUE_BLOCK), "Carve");
			} else
				toWait = doObjAction(getObjInRoom(CRUMBLING_MINES), "Mine");
			smartSleep(toWait, true);
			waitForDamage();
		}
		return 1;
	}

	private int puzzleTimedSwitches() {
		int distAllowed = 1;
		final Tile backDoor = getBackDoor();
		final Tile start = getNearestObjTo(backDoor, LEVERS);
		double dist = Double.MAX_VALUE;
		if (start == null)
			return -1;
		Tile begin = start;
		if (backDoor != null)
			for (final Tile t : getAdjacentTo(start)) {
				final double tDist = Calculations.distance(t, backDoor);
				if (tDist < dist && isGoodTile(t)) {
					begin = t;
					dist = tDist;
					distAllowed = 0;
				}
			}
		setMouseSpeed(2);
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (!roomSwitch)
				if (distTo(begin) > distAllowed)
					walkTo(begin, distAllowed);
				else if (Walking.getEnergy() < random(25, 30))
					restTo(Random.nextInt(90, 100));
				else if (getObjsInRoom(LEVERS).length == 5)
					while (!puzzleSolved && !roomSwitch) {
						if (failSafe())
							return 2;
						final SceneObject lever = getObjInRoom(LEVERS);
						if (lever != null) {
							safeTile = lever.getLocation();
							if (doObjAction(lever, "Pull")) {
								if (distTo(safeTile) < 4) {
									SceneObject next = getNextObj(lever, LEVERS);
									for (int c = 0; c < 10; ++c) {
										if (next == null) {
											next = getNextObj(lever, LEVERS);
											continue;
										}
										if (!moving() && distTo(safeTile) == 1) {
											Time.sleep(400, 600);
											walkHop(next, 0);
											Time.sleep(600, 900);
											break;
										}
										Time.sleep(50, 100);
									}
								} else {
									waitToStart(false);
									while (moving() && distTo(lever) > Random.nextDouble(3, 3.5))
										Time.sleep(100, 200);
								}
							} else
								while (!roomSwitch && lever != null && moving() && !lever.isOnScreen())
									Time.sleep(50, 100);
						}
						Time.sleep(50, 100);
					}
				else
					topUp();
			if (roomSwitch) {
				log(null, false, "Failed to pull the switches in time, restarting.");
				roomSwitch = false;
				safeTile = null;
				if (!pickUpAll())
					return 2;
				walkFast(start, false, 1);
				omNomNom();
			}
			waitToStop(false);
		}
		return 1;
	}

	private int puzzleSeekerSentinel() {
		getKey();
		getBestDoor();
		while (nearDoor != null && ((nearItem != null && targetRoom.contains(nearItem)) || distTo(nearDoor.loc) > 3)) {
			if (failSafe())
				return 2;
			final Tile kTile = getKey();
			if (nearItem != null) {
				if (distTo(kTile) > 3)
					walkToMap(kTile, 1);
				else
					pickUpItem(nearItem);
			} else
				walkToDoor(2);
			waitToStop(true);
			waitToEat(true);
			getBestDoor();
		}
		puzzleRepeat = true;
		return 1;
	}

	private int puzzleSleepingGuards() {
		save(bounds, GUARD_KEY);
		while (!invContains(GUARD_KEY) || getItemInRoom(targetRoom, GUARD_KEY) != null
				|| NPCs.getNearest(guardians) != null) {
			if (failSafe())
				return 2;
			getItemInRoom(targetRoom, GUARD_KEY);
			if (nearItem != null)
				pickUpItem(nearItem);
			else if (NPCs.getNearest(monster) == null) {
				if (walkTo(targetRoom.getCenter(), 4))
					waitToEat(false);
			} else if (!fightMonsters())
				return 2;
			waitToStop(false);
		}
		puzzleRepeat = doorCount(State.NEW) > 1;
		return 1;
	}

	private int puzzleSliderPuzzle() {
		final int[] SLIDE_1 = { 12125, 12133, 12141, 12149, 12963 }, SLIDE_2 = { 12126, 12134, 12142, 12150, 12964 }, SLIDE_3 = {
				12127, 12135, 12143, 12151, 12965 };
		final int[] SLIDE_4 = { 12128, 12136, 12144, 12152, 12966 }, SLIDE_5 = { 12129, 12137, 12145, 12153, 12967 }, SLIDE_6 = {
				12130, 12138, 12146, 12154, 12968 };
		final int[] SLIDE_7 = { 12131, 12139, 12147, 12155, 12969 }, SLIDE_8 = { 12132, 12140, 12148, 12156, 12970 }, SLIDE_9 = null;
		final int[][] ID_ROW_1 = { SLIDE_1, SLIDE_2, SLIDE_3 }, ID_ROW_2 = { SLIDE_4, SLIDE_5, SLIDE_6 }, ID_ROW_3 = {
				SLIDE_7, SLIDE_8, SLIDE_9 };
		final int[][][] ID_NODES = { ID_ROW_1, ID_ROW_2, ID_ROW_3 };
		final Tile start = getNearestObjTo(ORIGIN, SLIDER_BACKGROUNDS);
		if (start == null)
			return -1;
		final int sX = start.getX(), sY = start.getY();
		final Tile[] ROW_1 = { new Tile(sX + 1, sY + 5, 0), new Tile(sX + 3, sY + 5, 0), new Tile(sX + 5, sY + 5, 0) };
		final Tile[] ROW_2 = { new Tile(sX + 1, sY + 3, 0), new Tile(sX + 3, sY + 3, 0), new Tile(sX + 5, sY + 3, 0) };
		final Tile[] ROW_3 = { new Tile(sX + 1, sY + 1, 0), new Tile(sX + 3, sY + 1, 0), new Tile(sX + 5, sY + 1, 0) };
		final Tile[][] SLIDE_SPOTS = { ROW_1, ROW_2, ROW_3 };
		threadPitch(random(30, 45));
		while (getNpcAt(ROW_3[2]) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (lastMessage.contains("budge")) {
				moveCameraRandomly();
				lastMessage = "";
			}
			o: for (int I = 0; I < SLIDE_SPOTS.length; ++I)
				for (int J = 0; J < SLIDE_SPOTS[I].length; ++J)
					if (getNpcAt(SLIDE_SPOTS[I][J]) == null) {
						safeTile = SLIDE_SPOTS[I][J];
						final NPC s = getNpcInRoom(ID_NODES[I][J]);
						if (s != null && !s.isMoving() && doNpcAction(s, "Move"))
							waitToSlide(s);
						break o;
					}
			Time.sleep(100, 200);
		}
		return 1;
	}

	private int puzzleSlidingStatues() {
		final int[] RUG_CORNERS = { 50762, 51313, 51861, 54889, 56441 };
		final int[] RUG_EDGES = { 50765, 51316, 51864, 54892, 56443, 56444 };
		int hX = 0, hY = 0, failCount = 0;
		for (int I = 0; I < SLIDING_STATUES.length; ++I) {
			final NPC push = getNpcInRoom(SLIDING_STATUES[I]);
			final NPC heading = getNpcInRoom(HEADING_STATUES[I]);
			if (push != null && heading != null) {
				final Tile hCorner = getNearestSWObjTo(heading.getLocation(), RUG_CORNERS);
				final Tile pCorner = getNearestSWObjTo(push.getLocation(), RUG_CORNERS);
				if (hCorner != null && pCorner != null) {
					final int xDist = hCorner.getX() - pCorner.getX();
					final int yDist = hCorner.getY() - pCorner.getY();
					if (xDist != 0 && yDist != 0)
						continue;
					if (xDist != 0) {
						hX = xDist > 0 ? -7 : 7;
						break;
					}
					if (yDist != 0) {
						hY = yDist > 0 ? -7 : 7;
						break;
					}
				}
			}
		}
		if (hX == 0 && hY == 0) {
			waitForResponse();
			return -1;
		}
		o: while (!puzzleSolved) {
			if (failSafe())
				return 2;
			int aligned = 0;
			if (failCount > 10) {
				failReason = "No good push tiles remain";
				return -1;
			}
			for (int I = 0; I < SLIDING_STATUES.length; ++I) {
				final NPC statue = NPCs.getNearest(SLIDING_STATUES[I]);
				final NPC heading = NPCs.getNearest(HEADING_STATUES[I]);
				if (statue != null && heading != null) {
					final Tile hT = heading.getLocation();
					Tile pT = statue.getLocation();
					safeTile = new Tile(hT.getX() + hX, hT.getY() + hY, 0);
					if (pT.equals(safeTile)) {
						if (aligned == 3)
							break;
						++aligned;
						continue;
					}
					while (statue.validate() && !(pT = statue.getLocation()).equals(safeTile)) {
						if (failSafe())
							return 2;
						if (puzzleSolved)
							break o;
						boolean xPush = false, xPull = false, yPush = false, yPull = false;
						final int pX = pT.getX(), pY = pT.getY();
						final Point m = alignment(pT, safeTile);
						Tile pullX = null, pushX = null, pullY = null, pushY = null;
						if (m.x != 0) {
							pushX = new Tile(pX - m.x, pY, 0);
							pullX = new Tile(pX + m.x, pY, 0);
							xPush = getNpcAt(pushX) == null && getNpcAt(pullX) == null && isGoodTile(pushX);
							xPull = getNpcAt(pullX) == null && getNpcAt(new Tile(pX + 2 * m.x, pY, 0)) == null;
						}
						if (m.y != 0) {
							pullY = new Tile(pX, pY + m.y, 0);
							pushY = new Tile(pX, pY - m.y, 0);
							yPush = getNpcAt(pushY) == null && getNpcAt(pullY) == null;
							yPull = getNpcAt(pullY) == null && getNpcAt(new Tile(pX, pY + 2 * m.y, 0)) == null;
						}
						final boolean readyY = (yPush && atLoc(pushY)) || (yPull && atLoc(pullY));
						if ((xPush || xPull) && !readyY) {
							final double pullDist = xPull ? distTo(pullX) : 100;
							final double pushDist = xPush ? distTo(pushX) : 100;
							final Tile move = pullDist < pushDist ? pullX : pushX;
							final String action = pullDist < pushDist ? "Pull" : "Push";
							if (walkBlockedTile(move, 0) && interact(statue, action) && waitToSlide(statue))
								failCount = 0;
						} else if (yPush || yPull) {
							final double pullDist = yPull ? distTo(pullY) : 100;
							final double pushDist = yPush ? distTo(pushY) : 100;
							final Tile move = pullDist < pushDist ? pullY : pushY;
							final String action = pullDist < pushDist ? "Pull" : "Push";
							if (walkBlockedTile(move, 0) && interact(statue, action) && waitToSlide(statue))
								failCount = 0;
						} else {
							++failCount;
							if (failCount > 3) {
								Tile s1 = m.x != 0 ? new Tile(pX, pY + m.x, 0) : new Tile(pX + m.y, pY, 0);
								if (isObjectAt(s1, RUG_EDGES))
									s1 = m.x != 0 ? new Tile(pX, pY - m.x, 0) : new Tile(pX - m.y, pY, 0);
								final Tile s2 = m.x != 0 ? new Tile(s1.getX() - m.x, s1.getY(), 0) : new Tile(
										s1.getX(), s1.getY() - m.y, 0);
								if (walkBlockedTile(s1, 0) && interact(statue, "Pull") && waitToSlide(statue)) {
									if (walkBlockedTile(s2, 0) && interact(statue, "Push"))
										waitToSlide(statue);
									failCount = 0;
								}
							}
							break;
						}
					}
				}
			}
		}
		if (!pickUpAll())
			return 2;
		getBestDoor();
		walkBlockedTile(nearDoor.loc, 2);
		return 1;
	}

	private int puzzleStrangeFlowers() {
		if (!memberCheck())
			return -1;
		waitForResponse();
		final String[] F_COLORS = { "blue", "purple", "red", "yellow" };
		SceneObject centerFlower, flower;
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			nearMonster = NPCs.getNearest(monster);
			if (nearMonster != null && distTo(nearMonster) < 5 && reachable(nearMonster, false)) {
				if (!fightMonsters())
					return 2;
			} else if ((centerFlower = getObjInRoom(CENTER_FLOWERS)) != null)
				if (reachableObj(centerFlower)) {
					if (doObjAction(centerFlower, "Uproot") && waitToAnimate())
						waitToStop(true);
				} else {
					final int fId = centerFlower.getId();
					String color = "";
					for (int I = 0; I < CENTER_FLOWERS.length; ++I)
						if (fId == CENTER_FLOWERS[I]) {
							color = F_COLORS[I / 2];
							break;
						}
					if ((flower = getReachableObjNear(centerFlower, STRANGE_FLOWERS)) != null) {
						if (!color.isEmpty() && getName(flower).equals("Strange " + color + " plant")) {
							if (doObjAction(flower, "Chop", "Strange " + color + " plant")) {
								waitToStop(false);
								waitToStop(true);
							}
						} else if (distTo(flower) > 1 && !moving())
							walkTo(flower, 1);
						else
							topUp();
					} else
						topUp();
				}
			Time.sleep(50, 200);
		}
		omNomNom();
		return 1;
	}

	private int puzzleSuspiciousGrooves() {
		final int[][] FORWARD_ROWS = { ROW_1, ROW_2, ROW_3 };
		final Tile finalRow = getObjLocation(ROW_3);
		final Tile backDoor = getBackDoor();
		if (finalRow == null || backDoor == null)
			return -1;
		final Tile moveTile = reflect(backDoor, finalRow, 2);
		for (int I = 0; I < FORWARD_ROWS.length; I++) {
			final int[] grooves = FORWARD_ROWS[I];
			if (failSafe())
				return 2;
			SceneObject nextTry = getObjInRoom(grooves);
			if (nextTry == null)
				return -1;
			safeTile = nextTry.getLocation();
			final int nextId = nextTry.getId();
			while (distTo(backDoor) <= Calculations.distance(safeTile, backDoor)) {
				if (unreachable && I > 0) {
					I -= 2;
					unreachable = false;
					break;
				}
				if (failSafe())
					return 2;
				eatFood(foods, 40, 50);
				if (!isObjectAt(safeTile, nextId)) {
					nextTry = getObjInRoom(grooves);
					if (nextTry != null)
						safeTile = nextTry.getLocation();
				}
				if (atLoc(safeTile)) {
					walkTo(moveTile, 2);
					waitToEat(false);
				} else if (player().getAnimation() != 1114 && !moving()) {
					boolean failed = false;
					if (doObjAction(nextTry, "Step-onto")) {
						while (moving()) {
							if (player().getAnimation() == 1114)
								failed = true;
							Time.sleep(300, 400);
						}
						for (int c = 0; c < 10 && !failed; ++c) {
							if (player().getAnimation() == 1114)
								failed = true;
							Time.sleep(150, 200);
						}
						while (moving() || player().getAnimation() == 1114)
							Time.sleep(200, 400);
						if (failed || !isObjectAt(safeTile, nextId)) {
							nextTry = getObjInRoom(grooves);
							if (nextTry != null)
								safeTile = nextTry.getLocation();
						} else if (walkTo(moveTile, 2))
							waitToEat(false);
					}
				}
				Time.sleep(100, 200);
			}
		}

		puzzleRepeat = true;
		return 1;
	}

	private int puzzleUnhappyGhost() {
		if (!memberCheck() || !training(Skills.PRAYER))
			return -1;
		final String[] ACTIONS = { "Unlock", "Bless-remains", "Repair", "Repair", "Fill", null };
		while (getNpcInRoom(UNHAPPY_GHOST) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			final SceneObject unopenedCoffin = getObjInRoom(40181, 54571, 54593, 55465);
			final SceneObject unblessedCoffin = getObjInRoom(54572, 54594, 55451, 55466);
			final SceneObject damagedPillar = getObjInRoom(54580, 54602, 55457, 55472);
			final SceneObject brokenPot = getObjInRoom(54577, 54599, 55455, 55470);
			final SceneObject jewelryBox = getObjInRoom(54576, 54598, 55453, 55468);
			final GroundItem ring = getItemInRoom(targetRoom, 19879);
			smartAction(ACTIONS, unopenedCoffin, unblessedCoffin, damagedPillar, brokenPot, ring == null ? jewelryBox
					: null, ring);
		}
		return 1;
	}

	private int puzzleWarpedPortals() {
		int resets = 0;
		SceneObject portal;
		final Tile goal = targetRoom.getCenter(), backDoor = getBackDoor();
		Tile previous = null;
		dropItem(GGS);
		if (gateRoom == null && !castable(Dungeoneering.GROUP_GATESTONE_TELEPORT))
			makeGatestone(false);
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (reachable(backDoor, true)) {
				if (previous != null) {
					badTiles.add(previous);
					perm.add(previous);
				}
				omNomNom();
			}
			if (reachable(goal, false)) {
				if (walkToMap(myLoc(), 2) || walkToMap(myLoc(), 2))
					waitToStart(false);
			} else {
				if ((portal = portalObject()) == null) {
					if (resets > 2) {
						failReason = "No reachable portal found";
						log(LR, false, failReason);
						if (!teleHomeAndBack())
							targetRoom.isUnbacktrackable = true;
						return -1;
					}
					++resets;
					puzzlePoints.retainAll(perm);
					badTiles.retainAll(perm);
					continue;
				}
				safeTile = portalAdjacent(portal);
				if (previous != null && safeTile.equals(previous)) {
					if (developer)
						log(LR, false, "Redundant tile! Removing");
					puzzlePoints.add(safeTile);
					badTiles.add(safeTile);
					continue;
				}
				puzzlePoints.add(safeTile);
				int attempts = 0;
				while (reachable(safeTile, false)) {
					if (failSafe())
						return 2;
					if (attempts > 1)
						break;
					if (smartSleep(doObjAction(portal, "Enter"), true))
						++attempts;
				}
				previous = safeTile;
				badTiles.add(safeTile);
				puzzlePoints.add(safeTile);
				Time.sleep(400, 600);
				safeTile = portalAdjacent(getReachableObj(WARPED_PORTAL));
				if (safeTile != null) {
					puzzlePoints.add(safeTile);
					badTiles.add(safeTile);
				}
			}
			Time.sleep(100, 500);
		}
		idleTimer = new Timer(0);
		puzzleTimer.reset();
		return !teleHomeAndBack() ? 2 : 1;
	}

	private final Filter<NPC> primaryMonster = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc == null || (targetRoom != null && !targetRoom.contains(npc)) || !npc.validate() || isDead(npc)
					|| !slayerRequirement(getId(npc)))
				return false;
			final String npcName = getName(npc);
			if (spawnRoom && stringMatch(npcName, "Zombie", "Reborn warrior"))
				return false;
			if (npc.isInCombat() || intMatch(getId(npc), NECROMANCERS))
				return true;
			if (intMatch(getId(npc), FRIENDLIES))
				return false;
			if (shadowHooded && hoodMonster(npc))
				return false;
			if (npcName.startsWith("Ramokee") || npcName.equals(strongestMonster))
				return false;
			return stringMatch(npcName, PRIORITY_MONSTERS);
		}
	};

	private final Filter<NPC> secondaryMonster = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc == null || (targetRoom != null && !targetRoom.contains(npc)) || !npc.validate() || isDead(npc)
					|| !slayerRequirement(getId(npc)))
				return false;
			final String npcName = getName(npc);
			final int nid = getId(npc);
			if (spawnRoom && stringMatch(npcName, "Zombie", "Reborn warrior"))
				return false;
			if (npc.isInCombat() || intMatch(nid, NECROMANCERS))
				return true;
			if (intMatch(nid, FRIENDLIES))
				return false;
			if (shadowHooded && hoodMonster(npc))
				return false;
			if (npcName.startsWith("Ramokee") || npcName.equals(strongestMonster))
				return false;
			return stringMatch(npcName, SECONDARY_MONSTERS) || getWeakness(npc) != Melee.NONE;
		}
	};

	private final Filter<NPC> attackable = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc == null || (targetRoom != null && !targetRoom.contains(npc)) || !npc.validate() || isDead(npc))
				return false;
			if (npc.isInCombat())
				return true;
			if (intMatch(getId(npc), FRIENDLIES))
				return false;
			return stringMatch("Attack", npc.getActions());
		}
	};

	private final Filter<NPC> guardians = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc == null || (targetRoom != null && !targetRoom.contains(npc)) || !npc.validate() || isDead(npc))
				return false;
			if (npc.isInCombat())
				return true;
			if (intMatch(getId(npc), FRIENDLIES))
				return false;
			if (targetRoom != null && targetRoom.hooded && hoodMonster(npc))
				return false;
			return stringMatch("Attack", npc.getActions());
		}
	};

	private final Filter<NPC> monster = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc == null || (targetRoom != null && !targetRoom.contains(npc)) || !npc.validate() || isDead(npc)
					|| !slayerRequirement(getId(npc)))
				return false;
			if (getName(npc).startsWith("Ramokee"))
				return false;
			if (npc.isInCombat())
				return true;
			if (intMatch(getId(npc), FRIENDLIES))
				return false;
			if (targetRoom != null && targetRoom.hooded && hoodMonster(npc))
				return false;
			return stringMatch("Attack", npc.getActions());
		}
	};

	private final Filter<NPC> fightable = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc == null || (targetRoom != null && !targetRoom.contains(npc)) || !npc.validate() || isDead(npc)
					|| !slayerRequirement(getId(npc)))
				return false;
			if (npc.isInCombat())
				return true;
			if (intMatch(getId(npc), FRIENDLIES))
				return false;
			if (shadowHooded && hoodMonster(npc))
				return false;
			return stringMatch("Attack", npc.getActions());
		}
	};

	private final Filter<NPC> bossMonster = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			return npc != null && npc.validate() && !isDead(npc) && (bossRoom == null || bossRoom.contains(npc))
					&& stringMatch("Attack", npc.getActions());
		}
	};

	private final Filter<NPC> dying = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			return npc != null && npc.validate() && targetRoom.contains(npc) && isDead(npc);
		}
	};

	private final Filter<GroundItem> itemFilter = new Filter<GroundItem>() {
		@Override
		public boolean accept(GroundItem gItem) {
			if (!gItem.validate())
				return false;
			final int itemId = gItem.getId();
			if (itemId < 1)
				return false;
			if (roomNumber == 1 && Option.SECONDARY_RANGE.enabled() && floor == Floor.OCCULT && secondaryWep < 1
					&& arrows > 0)
				if (intMatch(itemId, SECONDARY_BOWS))
					return true;
			if (keyMatch(itemId))
				return true;
			if (badTiles.contains(gItem.getLocation()))
				return false;
			if (itemId == COINS)
				return saving(bounds, COINS);
			if (saving(sales, itemId) || saving(saves, itemId))
				return true;
			if (itemId == ESSENCE)
				return saving(bounds, ESSENCE) && invCount(true, ESSENCE) + invCount(true, COSMIC_RUNES) < 20;
			if (itemId == arrows && gItem.getGroundItem().getStackSize() != 1 && reachable(gItem, true))
				return true;
			if (Option.BURY.enabled() && intMatch(itemId, BONES))
				return !isRushing || !Option.RUSH_FOODLESS.enabled();
			if (intMatch(itemId, foods))
				return (!isRushing || !Option.RUSH_FOODLESS.enabled())
						&& (health() != 100 || invCount(false) < 27 || itemId > foodLowest());
			if (logs != null && itemId == logs.logId)
				return !invCacheContains(itemId);
			if (saving(bounds, itemId))
				return !invCacheContains(itemId);
			if (pickupId > 0 && typeTo != 0 && itemId == pickupId)
				return true;
			return improvements != null && intMatch(itemId, improvements) && !invCacheContains(itemId);
		}
	};

	private final Filter<GroundItem> keyItem = new Filter<GroundItem>() {
		@Override
		public boolean accept(GroundItem item) {
			return keyMatch(item.getId());
		}
	};

	private final Filter<SceneObject> bookCorner = new Filter<SceneObject>() {
		@Override
		public boolean accept(SceneObject obj) {
			return obj.getId() == 52132 && distTo(obj) > 4 && distTo(obj) < 10;
		}
	};

	private final Filter<GroundItem> stompCrystal = new Filter<GroundItem>() {
		@Override
		public boolean accept(GroundItem item) {
			return intMatch(item.getId(), STOMP_CRYSTALS) && !badTiles.contains(item.getLocation());
		}
	};

	private final Filter<NPC> unInteracting = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (npc == null || !npc.validate() || !getName(npc).contains("mage") || !targetRoom.contains(npc))
				return false;
			return npc.getInteracting() == null || !npc.getInteracting().equals(player());
		}
	};

	private final Filter<NPC> skater = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			return npc != null && npc.getAnimation() != -1 && targetRoom.contains(npc);
		}
	};

	private final Filter<NPC> monolithShade = new Filter<NPC>() {
		@Override
		public boolean accept(NPC npc) {
			if (!targetRoom.contains(npc) || !npc.validate() || isDead(npc))
				return false;
			if (npc.getInteracting() != null && npc.getInteracting().equals(player()))
				return false;
			return !npc.isInCombat() && stringMatch("Attack", npc.getActions());
		}
	};

	private boolean antiban() {
		if (randomActivate(random(10, 222))) {
			antibanEvents();
			return true;
		}
		return false;
	}

	private void antibanEvents() {
		if (randomActivate(2)) {
			randomCamera();
			if (!randomActivate(3))
				return;
			Time.sleep(random(0, 1000), random(1000, 4000));
		}
		switch (Random.nextInt(0, 4)) {
		case 0:
			if (randomActivate(3))
				open(Tabs.STATS);
			else if (randomActivate(3))
				open(Tabs.FRIENDS);
			else if (randomActivate(3))
				open(Tabs.INVENTORY);
			else if (randomActivate(3))
				open(Tabs.values()[Random.nextInt(0, Tabs.values().length)]);
			Time.sleep(random(400, 900), random(900, 1700));
			break;
		case 1:
			final ArrayList<Integer> checks = new ArrayList<Integer>();
			if (randomActivate(5)) {
				checks.add(Random.nextInt(0, 20));
				checks.add(Random.nextInt(0, 30));
				checks.add(Random.nextInt(0, 40));
			} else {
				if (Option.PURE.enabled && !randomActivate(3))
					checks.add(Skills.DEFENSE);
				if (randomActivate(3))
					checks.add(Skills.CONSTITUTION);
				if (Option.MELEE.enabled)
					checks.add(randomActivate(2) ? Skills.ATTACK : Skills.STRENGTH);
				if (Option.RANGE.enabled)
					checks.add(Skills.RANGE);
				if (Option.MAGIC.enabled)
					checks.add(Skills.MAGIC);
				checks.add(Skills.DUNGEONEERING);
			}
			randomHover(checks);
			break;
		case 2:
		case 3:
			if (randomActivate(random(2, 4)))
				Mouse.putSide(random(2, 4));
			else
				Mouse.move(random(0, 180), random(180, 570));
			break;
		}
	}

	private boolean loadExtras() {
		try {
			final File settings = new File(extrasFile);
			if (!settings.exists())
				return false;
			final Properties prop = new Properties();
			prop.load(new FileInputStream(settings));
			if (prop != null) {
				if (pickupName.isEmpty()) {
					final String pickupStore = prop.getProperty("01");
					if (pickupStore != null && !pickupStore.isEmpty()) {
						pickupName = pickupStore;
						pickupId = parse(pickupName);
						destroyName = prop.getProperty("02");
						destroyId = parse(destroyName);
					}
				}
				return true;
			}
		} catch (final Exception e) {
			if (developer)
				log(LR, false, "Unable to load your extras settings");
		}
		return false;
	}

	private boolean loadPartySettings() {
		try {
			final File settings = new File(partyFile);
			logD(partyFile);
			if (!settings.exists())
				return false;
			final Properties prop = new Properties();
			prop.load(new FileInputStream(settings));
			if (prop != null && prop.getProperty("0") != null) {
				for (int I = 0; I < opParty.length; ++I)
					opParty[I].set(prop.getProperty("" + I).equals("true"));
				return true;
			}
		} catch (final Exception e) {
			if (developer)
				log(LR, false, "Unable to load your party settings");
		}
		return false;
	}

	private boolean loadSettings() {
		try {
			if (username == null && username() != null)
				setSettingsFiles();
			final File settings = new File(settingsFile);
			if (!settings.exists())
				return false;
			final Properties prop = new Properties();
			prop.load(new FileInputStream(settings));
			if (prop != null && prop.getProperty("03") != null) {
				for (int I = 0; I < options.length; ++I)
					for (int J = 0; J < options[I].length; ++J)
						options[I][J].set(prop.getProperty("" + I + J).equals("true"));
				int test = parse(prop.getProperty("40"));
				if (test != -1)
					complexity = test;
				if ((test = parse(prop.getProperty("41"))) != -1)
					rComplexity = test;
				if ((test = parse(prop.getProperty("42"))) != -1)
					rushTo = test;
				if ((test = parse(prop.getProperty("50"))) != -1) {
					primaryWep = test;
					currentWep = test;
				}
				if ((test = parse(prop.getProperty("51"))) != -1)
					secondaryWep = test;
				if ((test = parse(prop.getProperty("52"))) != -1)
					blastBox = test;
				combatSpell = enumOf(Dungeoneering.class, prop.getProperty("53"));
				if ((test = parse(prop.getProperty("54"))) != -1)
					defaultMode = test;
				if (!freeVersion && !liteVersion)
					loadExtras();
				return true;
			}
		} catch (final Exception e) {
			log(LR, false, "Unable to load your settings.");
		}
		return false;
	}

	private static int parse(String s) {
		try {
			return Integer.parseInt(s);
		} catch (final Exception e) {
			return 0;
		}
	}

	private int random(int a, int b) {
		return (int) Math.round(Random.nextInt(a, b) * RANDOMIZED);
	}

	private static boolean randomActivate(int a) {
		return Random.nextInt(100, 100 + a) == Random.nextInt(100, 100 + a * 2);
	}

	private void randomCamera() {
		int pitch = random(Camera.getPitch() < Random.nextInt(50, 100) && randomActivate(2) ? 90 : 40, 100);
		if (pitch > 100)
			pitch = 100;
		final int spin = random(52, 195);
		submit(new ThreadAngle(Camera.getYaw() + random(-spin, spin)));
		if (!randomActivate(3))
			submit(new ThreadPitch(pitch, !randomActivate(3) ? random(72, 94) : -1));
	}

	private void randomHover(ArrayList<Integer> components) {
		final Tabs initialTab = Tabs.getCurrent();
		// Skills.hover(components.get(Random.nextInt(0, components.size())));
		Time.sleep(random(600, 1100), random(1100, 1900));
		if (!randomActivate(3))
			moveMouseRandomly();
		if (randomActivate(2))
			(initialTab == Tabs.STATS ? Tabs.INVENTORY : initialTab).open(randomActivate(2));
	}

	private void saveExtras() {
		try {
			final File folder = new File(dir);
			if (!folder.exists())
				folder.mkdir();
			final File settings = new File(extrasFile);
			final BufferedWriter out = new BufferedWriter(new FileWriter(settings));
			out.write("01=" + pickupName);
			out.newLine();
			out.write("02=" + destroyName);
			out.close();
		} catch (final Exception e) {
			if (developer)
				log(LR, false, "Unable to save your extras settings.");
		}
	}

	private void savePartySettings() {
		try {
			final File folder = new File(dir);
			if (!folder.exists())
				folder.mkdir();
			final File settings = new File(partyFile);
			final BufferedWriter out = new BufferedWriter(new FileWriter(settings));
			for (int I = 0; I < opParty.length; ++I) {
				out.write("" + I + "=" + opParty[I].enabled());
				out.newLine();
			}
			out.close();
		} catch (final Exception e) {
			if (developer)
				log(LR, false, "Unable to save your party settings.");
		}
	}

	private void saveSettings() {
		try {
			final File folder = new File(dir);
			if (!folder.exists())
				folder.mkdir();
			final File settings = new File(settingsFile);
			final BufferedWriter out = new BufferedWriter(new FileWriter(settings));
			for (int I = 0; I < options.length; ++I)
				for (int J = 0; J < options[I].length; ++J) {
					out.write("" + I + J + "=" + options[I][J].enabled());
					out.newLine();
				}
			out.write("40=" + complexity);
			out.newLine();
			out.write("41=" + rComplexity);
			out.newLine();
			out.write("42=" + rushTo);
			out.newLine();
			out.write("50=" + primaryWep);
			out.newLine();
			out.write("51=" + secondaryWep);
			out.newLine();
			out.write("52=" + blastBox);
			out.newLine();
			out.write("53=" + combatSpell);
			out.newLine();
			out.write("54=" + defaultMode);
			out.close();
		} catch (final Exception e) {
			severe("Unable to save your settings");
		}
	}

	private void setSettingsFiles() {
		extrasFile = dir + "Extras-310-" + (username() != null ? username : Environment.getDisplayName()) + ".ini";
		partyFile = dir + "Party-310-" + (username != null ? username : Environment.getDisplayName()) + ".ini";
		settingsFile = dir + (freeVersion ? "Free" : "Pro") + "Settings-310-"
				+ (username != null ? username : Environment.getDisplayName()) + ".ini";
	}

	private class ThreadImages implements Runnable {
		@Override
		public void run() {
			MOUSE = loadImg("iDmouse");
			BACKGROUND = loadImg("bg");
			STATISTICS = loadImg("statistics");
			OPTIONS = loadImg("options");
			LOGO = loadImg("logo");
			SIDEBAR = loadImg("sidebar");
			HIDE = loadImg("X");
			CHECKBOX = loadImg("check");
			STATISTICS_H = loadImg("statistics_h");
			TAB_SELECTED = loadImg("tab-select");
			TEXT_STATISTICS = loadImg("label-statistics");
			SIDEBAR_SELECT = loadImg("sidebar-select");
			TEXT_OPTIONS = loadImg("label-options");
			TEXT_BINDS = loadImg("label-binding");
			CHECKBOX_A = loadImg("check_a");
			CHECKBOX_H = loadImg("check_h");
			OPTIONS_H = loadImg("options_h");
			HIDE_H = loadImg("X_h");
			MINIS = loadImg("mini-arrows");
			MINIS_S = loadImg("mini-arrows_h");
			UPARROW = loadImg("arr-up");
			UPARROW_S = loadImg("arr-up_h");
			DOWNARROW = loadImg("arr-down");
			DOWNARROW_S = loadImg("arr-down_h");
			BUTTON = loadImg("button");
			BUTTON_A = loadImg("button_a");
			INFO = loadImg("info");
			INFO_S = loadImg("info_h");
			HELP = loadImg("question");
			HELP_S = loadImg("question_h");
			STOP = loadImg("power");
			STOP_S = loadImg("power_h");
			MAP_TAB = loadImg("mapTab");
			HEADER = loadImg("header");
		};
	}

	private boolean adjacentTo(Area a) {
		boolean adjacent = false;
		final Tile me = myLoc();
		for (final Tile t : tiles(a)) {
			final int tDist = (int) Math.ceil(Calculations.distance(me, t));
			if (tDist == 0)
				return false;
			if (tDist == 1)
				adjacent = true;
		}
		return adjacent;
	}

	private boolean adjacentTo(SceneObject obj) {
		return obj != null && adjacentTo(obj.getArea());
	}

	private boolean adjacentTo(NPC npc) {
		if (npc != null) {
			final Tile t = npc.getLocation();
			return adjacentTo(new Area(new Tile(t.getX() - 1, t.getY() - 1, t.getPlane()), t));
		}
		return false;
	}

	private void advancePage(boolean forward) {
		if (forward) {
			backPage = false;
			++page;
			if (page > 4) {
				showSettings = false;
				showStats = true;
				showOptions = false;
			}
		} else {
			backPage = true;
			--page;
		}
		if (page == 0)
			oTab = 0;
		else if (oTab == 0)
			oTab = 1;
		failTimer.reset();
	}

	private Point alignment(Tile curr, Tile dest) {
		int x = dest.getX() - curr.getX(), y = dest.getY() - curr.getY();
		if (x != 0)
			x = x > 0 ? 1 : -1;
		if (y != 0)
			y = y > 0 ? 1 : -1;
		return x == 0 && y == 0 ? null : new Point(x, y);
	}

	private boolean atLoc(Locatable l) {
		return l != null && myLoc().equals(l.getLocation());
	}

	private boolean atStartRoom() {
		if (startRoom != null)
			return inRoom(startRoom);
		final NPC smuggler = NPCs.getNearest(SMUGGLER);
		return smuggler != null && (distTo(smuggler) < 7 || reachable(smuggler.getLocation(), true));
	}

	private boolean attackBoss(NPC b, boolean screenBias) {
		if (b == null || !b.validate())
			return false;
		if (isDead(b)) {
			if (bossStage == 0)
				finishBoss(b);
			return false;
		}
		nearMonster = b;
		if (bossBreaker())
			return false;
		if (!isAttacking(b)) {
			final boolean clicked = b.isOnScreen() && interact(b, "Attack", getName(b));
			if (!clicked && !moving() && distTo(b) > 3)
				if (combatStyle != Style.MELEE) {
					turnTo(b);
					threadDown();
				} else if (!screenBias)
					walkTo(b, 1);
				else if (distTo(b) < random(9, 11))
					walkToScreen(b);
				else
					walkToMap(b, 1);
			while (moving() && !isDead(b) && !b.isOnScreen() && !isAttacking(b)) {
				if (failCheck() || !b.validate() || isDead(b) || bossBreaker())
					return false;
				if (!topUp())
					Time.sleep(50, 100);
			}
			if (player().getInteracting() != null)
				return true;
			if (clicked || (b.isOnScreen() && interact(b, "Attack", getName(b))))
				for (int c = 0, r = random(7, 11); c < r; ++c) {
					if (failCheck() || !b.validate() || isDead(b) || bossBreaker())
						return false;
					if (player().getInteracting() != null)
						return true;
					Time.sleep(100, 150);
				}
		} else {
			if (isEdgeTile(myLoc()) && distTo(b) < 4 && !player().isInCombat() && !b.isInCombat())
				walkToMap(currentRoom.getCenter(), 1);
			if (bossStage == 0) {
				final int health = getHpPercent(b);
				if (health != 100 && bossHp != health) {
					final int diff = bossHp - health;
					if (diff < 40) {
						if (health < 50 && bossHp > 49 && bossTimer != null)
							bossTimer.reset();
						bossHp = health;
						if (isDead(b))
							finishBoss(b);
					}
				}
			}
		}
		return false;
	}

	private static boolean isAutoCasting() {
		return Settings.get(43) == 4;
	}

	private static Dungeoneering getAutocastedSpell() {
		if (isAutoCasting()) {
			final Widget widget = Widgets.get(950);
			if (widget.validate()) {
				final WidgetChild auto = widget.getChild(68);
				final WidgetChild menu = widget.getChild(0);
				if (auto != null && menu != null && auto.validate() && menu.validate()) {
					final Point aP = auto.getCentralPoint();
					final Point mP = menu.getAbsoluteLocation();
					if (aP.x > mP.x && aP.y > mP.y)
						for (final Dungeoneering s : Dungeoneering.values()) {
							final WidgetChild spell = widget.getChild(s.componentId);
							if (spell.getBoundingRectangle().contains(aP))
								return s;
						}
				}
			}
		}
		return null;
	}

	private boolean autocast(Dungeoneering spell) {
		if (spell != null && spell != getAutocastedSpell() && isCastable(spell)) {
			open(Tabs.MAGIC);
			final Widget widget = Widgets.get(950);
			if (widget.validate() && open(Tabs.MAGIC)) {
				final WidgetChild c = widget.getChild(spell.componentId);
				if (c != null && c.validate() && c.getTextureId() != spell.componentId)
					return clickComponent(c, "Autocast");
			}
		}
		return false;
	}

	private boolean attackNpc(NPC npc) {
		if (npc == null || !npc.validate() || isAttacking(npc))
			return false;
		if (npc.isOnScreen() && interact(npc, "Attack", getName(npc))) {
			getBestStyle(npc);
			return true;
		}
		if (!moving() || !headingTowards(npc, 3))
			if (!interactingWith(npc) && walkFast(npc, true, 1)) {
				if (distTo(npc) > Random.nextInt(4, 7))
					turnTo(npc);
				waitToStart(false);
			}
		while (moving() && !npc.isOnScreen() && !interactingWith(npc)) {
			if (failCheck() || !npc.validate() || isDead(npc))
				return false;
			Time.sleep(50, 100);
		}
		if (isAttacking(npc) || (npc.isOnScreen() && interact(npc, "Attack", getName(npc)))) {
			getBestStyle(npc);
			return true;
		}
		return false;
	}

	private boolean atTargetRoom() {
		getCurrentRoom();
		return isLeader ? invContains(GGS) || getItemInRoom(currentRoom, GGS) != null : partyPresent();
	}

	private void blinkPath(Character blink) {
		Tile current = blink.getLocation();
		final Point m = orientation(blink);
		puzzlePoints.clear();
		for (int c = 0; c < 10; ++c) {
			current = stepAlong(m, current);
			if (c > 2 && !isGoodTile(current))
				break;
			puzzlePoints.add(current);
		}
	}

	private boolean bossBreaker() {
		if (nearMonster != null && (breakTimer == null || !breakTimer.isRunning())) {
			if (intMatch(nearMonster.getAnimation(), animBreaker)) {
				logD("Anim breaker!");
				breakTimer = new Timer(1000);
				return true;
			}
			if (messageBreaker != null) {
				final String shout = nearMonster.getMessage();
				if (shout != null && (messageBreaker.isEmpty() || shout.contains(messageBreaker))) {
					logD("Message breaker!");
					breakTimer = new Timer(1000);
					return true;
				}
			}
		}
		return false;
	}

	private void bugReport(boolean show) {
		if (developer) {
			if (showPaint != show) {
				showPaint = show;
				Time.sleep(300, 400);
			}
			Environment.saveScreenCapture();
			showPaint = true;
		}
	}

	private boolean buryBones() {
		if (Option.BURY.enabled() && invContains(BONES)
				&& (invCount(false, BONES) > random(1, 4) || invCount(false) > 22 + random(0, 5))) {
			Item bone;
			while ((bone = invItem(BONES)) != null) {
				if (failCheck())
					return false;
				final int count = invCount(false);
				if (doItemAction(bone, "Bury"))
					for (int c = 0; c < 5; c++) {
						if (invCount(false) != count)
							break;
						Time.sleep(100, 200);
					}
			}
			return true;
		}
		return false;
	}

	private void cancelAction() {
		if (randomActivate(2))
			Mouse.move(150, 600);
		else
			threadSwivel(70, 30);
	}

	private static boolean isCastable(Dungeoneering spell) {
		return Skills.getLevel(Skills.MAGIC) >= spell.level;
	}

	private boolean castSpell(Dungeoneering spell, String cast) {
		if (spell != null && isCastable(spell)) {
			open(Tabs.MAGIC);
			final WidgetChild c = Widgets.get(950, spell.componentId);
			if (c != null && c.validate() && open(Tabs.MAGIC))
				if (c.getTextureId() != spell.textureId)
					return (cast.equals("Cast") && c.getBorderThickness() == 2) || clickComponent(c, cast);
		}
		return false;
	}

	@SuppressWarnings("incomplete-switch")
	private boolean castable(Dungeoneering spell) {
		if (!isCastable(spell))
			return false;
		switch (spell) {
		case CREATE_GATESTONE:
			return invContains(GATESTONE) || invCount(true, COSMIC_RUNES) > 2;
		case GATESTONE_TELEPORT:
			if (gateRoom == null || invContains(GATESTONE))
				return false;
			open(Tabs.MAGIC);
			return Widgets.get(950, spell.componentId).getTextureId() != 3058;
		case GROUP_GATESTONE_TELEPORT:
			return !invContains(GGS) && invCount(true, LAW_RUNES) > 2;
		case HOME_TELEPORT:
			return !player().isInCombat();
		}
		return true;
	}

	private boolean checkDungeonMap(boolean openMap) {
		if (openMap) {
			if (!mapOpen() && Widgets.get(548).validate()) {
				log(null, false, "Checking the dungeon map.");
				clickComponent(Widgets.get(548, 180), "");
				Time.sleep(400, 600);
				return true;
			}
		} else if (mapOpen()) {
			log(null, false, "Closing the dungeon map.");
			if (clickComponent(Widgets.get(942, 6), "Close")) {
				Time.sleep(300, 600);
				return true;
			}
		}
		return false;
	}

	private void click(int x, int y) {
		Mouse.move(x, y);
		Mouse.click(true);
	}

	private boolean clickThrough(WidgetChild c, String action) {
		if (c != null && c.interact(action))
			return forSleep(c, false);
		Time.sleep(20, 200);
		return false;
	}

	private boolean clickComponent(WidgetChild component, String action) {
		if (component == null || !component.validate())
			return false;
		final Rectangle rect = component.getBoundingRectangle();
		if (rect.x == -1)
			return false;
		int minX = rect.x + 2, minY = rect.y + 2, width = rect.width - 4, height = rect.height - 4;
		if (rect.width > 20) {
			minX = rect.x + 3;
			minY = rect.y + 3;
			width = rect.width - 6;
			height = rect.height - 6;
		}
		final Rectangle actual = new Rectangle(minX, minY, width, height);
		if (actual.contains(Mouse.getLocation()) && Menu.contains(action) && Menu.select(action))
			return true;
		return Mouse.move(Random.nextInt(minX, minX + width), Random.nextInt(minY, minY + height))
				&& Menu.select(action);
	}

	private boolean clickDialogueOption(Widget inter, String... selects) {
		if (inter != null && inter.validate())
			for (final WidgetChild c : inter.getChildren()) {
				final String cText = c.getText().toLowerCase();
				for (final String option : selects)
					if (cText.contains(option.toLowerCase()))
						return c.click(true);
			}
		return false;
	}

	private boolean clickRing(String action) {
		return (inDungeon && settingsFinished ? invContains(RINGS) : invContains(RINGS)) ? doItemAction(invItem(RINGS),
				action) : equipAction(Slot.RING, action)
				|| (!Equipment.contains(RINGS) && doItemAction(invItem(RINGS), action));
	}

	private boolean clickFar(SceneObject o, String action) {
		return clickTile(farthest(o), Random.nextDouble(.1, .9), Random.nextDouble(.1, .9), action, null);
	}

	private boolean clickTile(Locatable loc, String action) {
		return clickTile(loc.getLocation(), Random.nextDouble(.4, .6), Random.nextDouble(.4, .6), action, null);
	}

	private boolean clickTile(Locatable loc, String action, String locName) {
		return clickTile(loc.getLocation(), Random.nextDouble(.4, .6), Random.nextDouble(.4, .6), action, locName);
	}

	private boolean clickTile(Tile loc, double xd, double yd, String action, String objName) {
		for (int c = 0, r = random(12, 19); c < r; ++c) {
			final Point p = loc.getPoint(xd, yd, 0);
			if (p.x != -1) {
				Mouse.move(p);
				if (Menu.select(action, objName)) {
					Mouse.move(Mouse.getLocation(), random(1, 4), random(1, 4));
					return true;
				}
				if (!moving())
					c += 3;
			} else if (!moving() && distTo(loc) > 3)
				if (walkFast(loc, false, 1) && waitToStart(false))
					break;
		}
		return false;
	}

	private int crossTheChasm(Tile dest) {
		if (dest != null && memberWorld && targetRoom.hasChasm && !reachableTwice(dest, true)) {
			levelRequirement = true;
			SceneObject chasm = getObjInRoom(GRAPPLED_CHASMS);
			String action = "Cross-the";
			if (chasm == null) {
				chasm = getObjInRoom(UNFINISHED_BRIDGES);
				action = "Jump";
			}
			if (chasm != null) {
				secondaryStatus = "Crossing the chasm to the next door";
				if (puzzleTimer == null)
					log(null, false, status + ".");
				safeTile = farthest(chasm);
				while (!reachableTwice(dest, true)) {
					if (failCheck())
						return 2;
					if (!requirements())
						return -1;
					if (clickTile(farthest(chasm), action))
						waitToStop(true);
					waitToStop(true);
					while (moving() || player().getAnimation() == 14554)
						Time.sleep(600, 1000);
				}
				safeTile = null;
			}
		}
		return 1;
	}

	private int crystalDist(Tile center, SceneObject obj) {
		return obj != null ? 5 - (int) Calculations.distance(center, obj.getLocation()) : 0;
	}

	private void defenseDegenerate(boolean extraTime) {
		if (!bossFinished && bossRoom != null && isBoss("Bal'lak") && Widgets.get(945).validate()) {
			final String oldStatus = secondaryStatus;
			secondaryStatus = "Allowing his defense to degenerate";
			final int defenseBar = Widgets.get(945, 17).getRelativeX() - 15;
			int rtb = bossPath.size();
			if (aComplexity > 4 || rtb < 1)
				rtb = 1;
			int waitTime = (170 * defenseBar - rtb * 7000);
			if (waitTime > 0) {
				if (!extraTime)
					waitTime /= 2.2;
				final Timer waitTimer = new Timer(Random.nextInt(waitTime, (int) (waitTime * 1.1)));
				while (waitTimer.isRunning()) {
					if (pauseScript())
						return;
					if (!topUp())
						Time.sleep(100, 200);
				}
			}
			secondaryStatus = oldStatus;
		}
	}

	private boolean destroyItem(int itemId) {
		Item item = invItem(itemId);
		if (item == null || invCount(itemId) > 1)
			return false;
		log(LR, false, "Destroying item: " + equipmentName(item));
		while ((item = invItem(itemId)) != null) {
			if (failCheck())
				return false;
			if (Widgets.get(1183).validate())
				clickThrough(Widgets.get(1183, 3), "Continue");
			else if (interact(item, "Destroy"))
				forSleep(Widgets.get(1183), true);
			else
				Time.sleep(20, 200);
		}
		return !invContains(itemId);
	}

	private void disRobe() {
		if (Style.MAGIC.enabled && wearingEquipment()) {
			if (!inRoom(bossRoom) && invCount(false) > 24)
				dropAllExceptSaves();
			for (int c = 0; c < 3; ++c)
				for (final Slot slot : EQUIP_SLOTS) {
					final Item eq = equipItem(slot);
					if (eq == null || eq.getId() == blastBox)
						continue;
					if (shadowHooded && getName(eq).contains("silk hood"))
						continue;
					for (int d = 0; d < 3; ++d) {
						if (equipAction(slot, "Remove"))
							break;
						Time.sleep(100, 300);
					}
					Time.sleep(200, 400);
				}
		}
	}

	private double distTo(Locatable l) {
		return Calculations.distanceTo(l.getLocation());
	}

	private boolean dodgeSlayerMonster() {
		if (memberWorld && !moving()) {
			final NPC slayer = getNpcInRoom(SLAYER_MONSTERS);
			if (slayer != null)
				return distTo(slayer) < 3 && walkFast(myLoc(), true, 15);
		}
		return false;
	}

	private boolean doItemAction(Item item, String action) {
		if (item == null)
			return false;
		while (itemTimer.isRunning()) {
			if (!item.getWidgetChild().validate())
				return false;
			Time.sleep(50, 100);
		}
		if (interact(item, action)) {
			itemTimer = new Timer(Random.nextInt(900, 1300));
			return true;
		}
		return false;
	}

	private void finishBoss(NPC boss) {
		open(Tabs.INVENTORY);
		final SceneObject ladder = getObjInRoom(FINISH_LADDERS);
		if (ladder != null && (!moving() || !headingTowards(ladder)))
			if (walkTo(ladder, 2)) {
				turnTo(ladder);
				for (int c = 0; c < 10; ++c) {
					if (failCheck() || getObjInRoom(FINISHED_LADDERS) == null)
						break;
					Time.sleep(100, 200);
				}
			}
		if (!bossFinished)
			bossFinished = getObjInRoom(FINISHED_LADDERS) != null;
	}

	private boolean headingTowards(Locatable o) {
		return headingTowards(o, 3);
	}

	private boolean headingTowards(Locatable o, double r) {
		if (distTo(o) <= r)
			return true;
		final Tile flag = getFlag(false);
		return flag != null && Calculations.distance(flag, o.getLocation()) < r;
	}

	private boolean doNpcAction(NPC npc, String action) {
		if (npc == null || !npc.validate())
			return false;
		if (npc.isOnScreen() && interact(npc, action))
			return true;
		if (!moving() || !headingTowards(npc)) {
			final double dist = distTo(npc);
			if (dist == 0)
				walkTo(myLoc(), 2);
			else if (dist > 3 && walkFast(npc, true, 1)) {
				if (dist > Random.nextInt(4, 7))
					turnTo(npc);
				waitToStart(false);
			}
		}
		while (npc != null && npc.validate() && moving() && !isDead(npc) && !npc.isOnScreen()) {
			if (failCheck())
				return false;
			Time.sleep(50, 100);
		}
		return npc.isOnScreen() && interact(npc, action);
	}

	private SceneObjectDefinition getDef(SceneObject obj) {
		try {
			return obj.getDefinition();
		} catch (final Exception e) {
			return null;
		}
	}

	private boolean openDoor(SceneObject door, DoorType type) {
		return door != null && doObjAction(door, type.action, type.doorName);
	}

	private boolean openDoor(SceneObject door, DoorType type, int idx) {
		if (door == null)
			return false;
		String action = type.action, name = type.doorName;
		if (type == DoorType.SKILL) {
			final String[] strings = SKILL_STRINGS[idx];
			action = strings[0];
			name = strings[1];
		} else if (type == DoorType.BLOCKED) {
			final String[] strings = BLOCK_STRINGS[idx];
			action = strings[0];
			name = strings[1];
		}
		return doObjAction(door, action, name);
	}

	private boolean doObjAction(SceneObject obj, String action) {
		return doObjAction(obj, action, null);
	}

	private boolean doObjAction(SceneObject obj, String action, String objName) {
		if (obj == null)
			return false;
		if (obj.isOnScreen() && interact(obj, action, objName))
			return true;
		final Tile objTile = obj.getLocation();
		if (!moving() || !headingTowards(objTile)) {
			final double dist = distTo(objTile);
			if (dist > 3 && (!inDungeon || dist < 20)) {
				walkFast(objTile, true, 1);
				if (distTo(objTile) > random(4, 8))
					turnTo(objTile);
				waitToStart(false);
			}
		}
		while (obj.validate() && moving() && !obj.isOnScreen()) {
			if (failCheck())
				return false;
			if (action.equals("Pull") || !topUp())
				Time.sleep(100, 200);
		}
		return obj.isOnScreen() && interact(obj, action, objName);
	}

	private boolean doorCheck() {
		if (distTo(nearDoor.loc) < 3 && (lastMessage.contains("level of") || lastMessage.contains("unable to"))
				&& !temporaryReduction())
			if (reqTimer == null || !reqTimer.isRunning()) {
				log(null, false, "We don't have the level requirement for this door, removing.");
				nearDoor.state = State.FINISHED;
				return false;
			} else {
				log(null, false, "Waiting for required level");
				Time.sleep(1000, 3000);
			}
		return true;
	}

	private int doorCount(State state) {
		int doorCount = 0;
		for (final Door d : targetRoom.doors)
			if (d.state == state)
				++doorCount;
		return doorCount;
	}

	private boolean dropAllExceptSaves() {
		final int startCount = invCount(false);
		if (startCount == invSaleAfter())
			return false;
		setMouseSpeed(1.8);
		if (secondaryStatus.startsWith("Picking") && isItemAt(myLoc()))
			walkToMap(myLoc(), 2);
		for (final Item item : invItems(false)) {
			if (pauseScript())
				return false;
			if (!riddable(item))
				continue;
			if (!interact(item, "Drop")) {
				if (!droppable(item)) {
					destroyItem(item.getId());
					continue;
				}
				Time.sleep(50, 200);
				doItemAction(item, "Drop");
			}
		}
		setMouseDefault();
		return invCount(false) < startCount;
	}

	private boolean dropItem(int id) {
		if (!invContains(id))
			return false;
		while (invContains(id)) {
			if (failBasic())
				return false;
			ridItem(id, "Drop");
		}
		return true;
	}

	private boolean droppable(Item item) {
		if (item == null)
			return true;
		final String name = getName(item);
		return !name.endsWith("(o)") && !name.endsWith("(b)");
	}

	private boolean dungeonLoaded() {
		final WidgetChild skull = Widgets.get(945, 2);
		return skull != null && skull.validate();
	}

	private boolean eatFood(int[] foodSelection, int eatAt, int eatTo) {
		if (!invContains(foodSelection))
			return false;
		final int rA = eatAt / 10, rT = eatTo / 10;
		int startPercent = health();
		eatAt = eatAt + Random.nextInt(-rA, rA + 1);
		if (startPercent >= eatAt)
			return false;
		Item food;
		eatTo = eatTo + Random.nextInt(-rT, rT + 1);
		open(Tabs.INVENTORY);
		while ((food = getItemInOrder(foodSelection)) != null) {
			if (failCheck())
				return false;
			if (bossRoom != null && SceneEntities.getNearest(FINISHED_LADDERS) != null)
				return false;
			for (int c = 0; c < 7; ++c) {
				if (bossBreaker())
					return false;
				if (!food.getWidgetChild().validate())
					break;
				if (foodTimer == null || !foodTimer.isRunning()) {
					startHp = health();
					if (interact(food, "Eat"))
						foodTimer = new Timer(Random.nextInt(700, 1200));
				} else
					Time.sleep(100, 300);
				if (isDead || startPercent < health() - 5)
					break;
			}
			if ((startPercent = health()) >= eatTo)
				break;
		}
		return true;
	}

	private static String enumName(String n) {
		n = n.replaceAll("_", " ").replaceAll("0", "-");
		return n.substring(0, 1) + n.substring(1).toLowerCase();
	}

	private static <T extends Enum<T>> T enumOf(Class<T> c, String string) {
		try {
			return Enum.valueOf(c, string.replaceAll(" ", "_").replaceAll("-", "0").toUpperCase());
		} catch (final IllegalArgumentException e) {
			return null;
		}
	}

	private boolean equipAction(Slot slot, String action) {
		final Item eq = equipItem(slot);
		return eq != null
				&& (clickComponent(eq.getWidgetChild(), action) || clickComponent(eq.getWidgetChild(), action));
	}

	private boolean equipFor(Style style) {
		if (style == null)
			return false;
		boolean ret = false;
		final int weapon = style == primaryStyle ? primaryWep : secondaryWep;
		if (style == Style.MAGIC) {
			if (blastBox > 0 && invContains(blastBox))
				ret = ridItem(blastBox, "Wield");
		} else if (style == Style.RANGE)
			if (arrows > 0 && invContains(arrows))
				ret = ridItem(arrows, "Wield");
		if (invContains(weapon))
			ret = ridItem(weapon, "Wield");
		return ret;
	}

	private Item equipItem(Slot slot) {
		final Widget widget = Equipment.getWidget();
		if (widget != null && widget.validate()) {
			final WidgetChild itemComp = widget.getChild(slot.getIndex());
			if (itemComp != null && itemComp.getChildId() > 0)
				return new Item(itemComp);
		}
		return null;
	}

	private boolean wearingEquipment() {
		final int[] eq = player().getAppearance();
		for (final int index : DUNG_EQUIP_SLOTS)
			if (eq[index] > 0)
				return true;
		return false;
	}

	private String equipmentMaterial(String name) {
		if (name == null)
			return "";
		final int idx = name.indexOf(" ");
		return idx == -1 ? name : name.substring(0, idx);
	}

	private String equipmentName(Item item) {
		if (item == null)
			return "";
		final String name = getName(item);
		final int infoIdx = name.indexOf(" (");
		return infoIdx == -1 ? name : name.substring(0, infoIdx);
	}

	private int equipmentTier(int id) {
		if (id >= TIER_BASE[0] && id <= TIER_BASE[TIER_BASE.length - 1])
			for (final int base : TIER_BASE) {
				final int diff = id - base;
				if (diff < 21)
					return diff / 2 + 1;
			}
		return 0;
	}

	private int equipmentTier(String name) {
		if (name != null && !name.isEmpty() && !name.contains("(o)")) {
			final String material = equipmentMaterial(name);
			for (int I = 0; I < armorTiers.length; ++I)
				if (armorTiers[I].equals(material))
					return I + 1;
			if (weaponTiers != null)
				for (int I = 0; I < weaponTiers.length; ++I)
					if (weaponTiers[I].equals(material))
						return I + 1;
		}
		return 0;
	}

	@SuppressWarnings("incomplete-switch")
	private String equipmentType(int id) {
		switch (primaryStyle) {
		case MELEE:
			if (id > 15752) {
				if (id < 15775)
					return "battleaxe";
				if (id > 16272) {
					if (id < 16295)
						return "gauntlets";
					if (id > 16338) {
						if (id < 16361)
							return "boots";
						if (id > 16382) {
							if (id < 16405)
								return "longsword";
							if (id < 16427)
								return "maul";
							if (id > 16646) {
								if (id < 16669)
									return "plateskirt";
								if (id < 16691)
									return "platelegs";
								if (id < 16713)
									return "helm";
								if (id < 16735)
									return "chainbody";
								if (id > 16756) {
									if (id < 16845)
										return "dagger";
									if (id > 16888) {
										if (id < 16911)
											return "sword";
										if (id > 16934) {
											if (id < 16957)
												return "rapier";
											if (id > 17018) {
												if (id < 17041)
													return "warhammer";
												if (id > 17062) {
													if (id < 17151)
														return "warhammer";
													if (id > 17239) {
														if (id < 17261)
															return "platebody";
														if (id > 17340)
															if (id < 17363)
																return "kiteshield";
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		case RANGE:
			if (id > 16316) {
				if (id < 16339)
					return "longbow";
				if (id > 16866) {
					if (id < 16889)
						return "shortbow";
					if (id > 17040) {
						if (id < 17063)
							return "coif";
						if (id > 17172) {
							if (id < 17195)
								return "body";
							if (id < 17217)
								return "vambraces";
							if (id > 17296) {
								if (id < 17319)
									return "boots";
								if (id < 17341)
									return "chaps";
							}
						}
					}
				}
			}
		case MAGIC:
			if (id > 16734) {
				if (id < 16757)
					return "hood";
				if (id > 16844) {
					if (id < 16867)
						return "bottom";
					if (id > 16910) {
						if (id < 16933)
							return "shoes";
						if (id > 17150) {
							if (id < 17173)
								return "gloves";
							if (id > 17216) {
								if (id < 17239)
									return "top";
								if (id > 17216)
									if (id < 17239)
										return "top";
							}
						}
					}
				}
			}
		}
		return "";
	}

	private String equipmentType(String name) {
		if (name == null || name.isEmpty())
			return "";
		final int bracketIdx = name.indexOf(" (");
		if (bracketIdx > 0)
			name = name.substring(0, bracketIdx);
		return name.substring(name.lastIndexOf(" ") + 1);
	}

	private Tile farthest(SceneObject obj) {
		if (obj == null)
			return null;
		Tile far = obj.getLocation();
		final Tile me = roomNumber == 1 ? startRoom.getCenter() : myLoc();
		double dist = Calculations.distance(me, far);
		for (final Tile tile : tiles(obj.getArea())) {
			final double tDist = Calculations.distance(me, tile);
			if (tDist > dist) {
				far = tile;
				dist = tDist;
			}
		}
		if (roomNumber != 1 && far.equals(obj.getLocation())) {
			if (Camera.getPitch() < random(10, 30) && !moving() && distTo(far) > 3)
				walkTo(far, 1);
			threadDown();
			turnTo(far);
		}
		return far;
	}

	private int foodLowest() {
		for (final int id : foods)
			if (invCacheContains(id))
				return id;
		return -1;
	}

	private boolean forSleep(WidgetChild cw, boolean show) {
		for (int c = 0; c < 16; ++c) {
			if (cw.validate() == show)
				return true;
			Time.sleep(80, 120);
		}
		return false;
	}

	private boolean forSleep(Widget w, boolean open) {
		for (int c = 0; c < 16; ++c) {
			if (w.validate() == open)
				return true;
			Time.sleep(80, 120);
		}
		return false;
	}

	private boolean forVisible(WidgetChild cw, boolean show) {
		for (int c = 0; c < 16; ++c) {
			if (cw.visible() == show)
				return true;
			Time.sleep(80, 120);
		}
		return false;
	}

	private long getDungExpToLevel() {
		return (levelGoal == 200 ? 200000000 : Skills.XP_TABLE[levelGoal]) - Skills.getExperience(Skills.DUNGEONEERING);
	}

	private ArrayList<Door> generatePath(Room nextRoom) {
		final ArrayList<Door> doors = new ArrayList<Door>();
		int c = 0;
		while (nextRoom != null && !isStart(nextRoom)) {
			final Room oldRoom = nextRoom;
			if ((nextRoom = oldRoom.parent) != null)
				doors.add(oldRoom.entryDoor);
			if (nextRoom == null || c > 30) {
				if (developer && !isStart(nextRoom))
					severe("Unable to generate path.");
				break;
			}
			++c;
		}
		return doors;
	}

	private Tile[] getAdjacentTo(Tile t) {
		final int x = t.getX(), y = t.getY();
		return new Tile[] { new Tile(x + 1, y, 0), new Tile(x - 1, y, 0), new Tile(x, y + 1, 0), new Tile(x, y - 1, 0) };
	}

	private Tile getBackDoor() {
		return getBackDoor(targetRoom);
	}

	private Tile getBackDoor(Room room) {
		if (room.backDoor != null)
			return room.backDoor;
		if (room.equals(targetRoom))
			o: for (final SceneObject door : getObjsInRoom(BACK_DOORS)) {
				final Tile dTile = door.getLocation();
				for (final Door d : room.doors)
					if (d.loc == dTile)
						continue o;
				return dTile;
			}
		return getObjLocation(BACK_DOORS);
	}

	private void getBestStyle(NPC npc) {
		if (!bossFight)
			if ((tempMode = getWeakness(npc)) != Melee.NONE) {
				if (swapStyle(tempMode.style()))
					updateFightMode();
			} else if (combatStyle != primaryStyle && primaryStyle.enabled)
				if (swapStyle(primaryStyle))
					updateFightMode();
	}

	private Tile[] getDiagonalTo(Tile t) {
		final int x = t.getX(), y = t.getY();
		return new Tile[] { new Tile(x + 1, y + 1, 0), new Tile(x - 1, y + 1, 0), new Tile(x - 1, y - 1, 0),
				new Tile(x + 1, y - 1, 0) };
	}

	private Tile[] getDiagonalTriple(Tile t) {
		final int x = t.getX(), y = t.getY();
		return new Tile[] { new Tile(x + 3, y + 3, 0), new Tile(x - 3, y + 3, 0), new Tile(x - 3, y - 3, 0),
				new Tile(x + 3, y - 3, 0) };
	}

	private Tile getEntry() {
		return getNearestTileTo(myLoc(), targetRoom.getDoorNodes());
	}

	private Tile getFlag(boolean ifMoving) {
		if (!ifMoving || moving()) {
			final Tile flag = Walking.getDestination();
			if (flag.getX() != -1)
				return flag;
		}
		return null;
	}

	private int getFrame(NPC npc) {
		final int frame = npc.getPassiveAnimation();
		return frame > 100 ? -1 : frame;
	}

	private GroundItem getGoodItem() {
		return nearItem = getRoomItem(itemFilter);
	}

	private SceneObject getCloseObjNear(final Tile t, final int... ids) {
		final Tile me = myLoc();
		return getRoomObj(me, new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject o) {
				if (!intMatch(o.getId(), ids))
					return false;
				return !me.equals(o.getLocation()) && Calculations.distance(o.getLocation(), t) < 4;
			}
		});
	}

	private Room getCurrentRoom() {
		final Tile myTile = myLoc();
		if (currentRoom == null || !currentRoom.contains(myTile))
			for (final Room rm : rooms)
				if (rm.contains(myTile)) {
					currentRoom = rm;
					roomNumber = rm.index;
					break;
				}
		return currentRoom;
	}

	private NPC getGoodNpc(final String... names) {
		return NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && npc.validate() && stringMatch(getName(npc), names) && targetRoom.contains(npc)
						&& !isDead(npc);
			}
		});
	}

	private int getHpPercent(Character c) {
		try {
			final int hpr = c.getHpRatio();
			return hpr == 0 ? 0 : (int) Math.ceil(hpr * 100 / 255);
		} catch (final Exception e) {
			if (developer)
				e.printStackTrace();
			return 100;
		}
	}

	private GroundItem getItemArrow() {
		final Tile me = myLoc();
		double dist = 99.99;
		GroundItem itm = null;
		for (int x = currentRoom.x + 1; x < currentRoom.nx; ++x)
			for (int y = currentRoom.y + 1; y < currentRoom.ny; ++y) {
				final double tDist = Calculations.distance(me, new Tile(x, y, 0));
				if (tDist < dist)
					for (final GroundItem item : GroundItems.getLoadedAt(x, y))
						if (item != null && item.getId() == arrows && item.getGroundItem().getStackSize() > 1) {
							if (tDist == 0)
								return nearItem = item;
							dist = tDist;
							itm = item;
							break;
						}
			}
		return nearItem = itm;
	}

	private int getItemId(String name) {
		final Item item = invItem(false, name);
		return item != null ? item.getId() : -1;
	}

	private Item getItemInOrder(int... ids) {
		for (final int id : ids) {
			final Item[] food = invItems(id);
			if (food.length > 0)
				return food[Random.nextInt(0, food.length)];
		}
		return null;
	}

	private GroundItem getItemInRoom(Room room, int... ids) {
		if (room == null || ids.length == 0)
			return nearItem = null;
		final Tile me = myLoc();
		double dist = 99.99;
		GroundItem itm = null;
		for (int x = room.x + 1; x < room.nx; ++x)
			for (int y = room.y + 1; y < room.ny; ++y) {
				final double tDist = Calculations.distance(me, new Tile(x, y, 0));
				if (tDist < dist)
					for (final GroundItem item : GroundItems.getLoadedAt(x, y))
						if (item != null && intMatch(item.getId(), ids)) {
							if (tDist == 0)
								return nearItem = item;
							dist = tDist;
							itm = item;
							break;
						}
			}
		return nearItem = itm;
	}

	private Tile getKey() {
		nearItem = getRoomItem(keyItem);
		return nearItem != null ? nearItem.getLocation() : null;
	}

	private Item getLastItem(int... ids) {
		final Item[] items = invItems(ids);
		return items.length > 0 ? items[items.length - 1] : null;
	}

	private boolean plateWarrior(int id) {
		if (id > 10842)
			id -= 10843;
		else if (id > 10506)
			id -= 10507;
		else if (id > 10396)
			id -= 10397;
		else
			id -= 10246;
		return id % 7 < 3;
	}

	private Monster getMonster(NPC npc) {
		if (npc == null || !npc.validate())
			return null;
		final String name = getName(npc);
		final int id = getId(npc);
		if (id == -1 || name.isEmpty())
			return null;
		if (name.equals("Forgotten warrior")) {
			if (plateWarrior(id))
				return Monster.FORGOTTEN_WARRIOR0PLATE;
			else
				return Monster.FORGOTTEN_WARRIOR0CHAIN;
		} else if (name.equals("Skeleton"))
			return id > 10681 ? Monster.SKELETON0MAGIC : id > 10668 ? Monster.SKELETON0RANGE : Monster.SKELETON0MELEE;
		else if (name.equals("Zombie"))
			return id > 10374 ? Monster.ZOMBIE0RANGE : Monster.ZOMBIE0MELEE;
		final int fIdx = floor.index;
		for (final Monster m : Monster.values())
			if (m.name != null) {
				final int mIdx = m.floor.index;
				if (mIdx == fIdx || mIdx == -3 || (mIdx == -2 && fIdx > 0) || (mIdx == -4 && fIdx > 2))
					if (name.equals(m.name))
						return m;
			}
		return null;
	}

	private int getId(NPC npc) {
		try {
			return npc.getId();
		} catch (final Exception e) {
			// log.severe("NPC id: " + e.toString());
			return -1;
		}
	}

	private String getName(NPC npc) {
		try {
			return npc.getName();
		} catch (final Exception e) {
			// log.severe("NPC name: " + e.toString());
			return "";
		}
	}

	private String getName(SceneObject obj) {
		if (obj != null) {
			final SceneObjectDefinition oDef = getDef(obj);
			if (oDef != null)
				try {
					final String oName = oDef.getName();
					return oName != null && !oName.equals("null") ? oName : "";
				} catch (final Exception e) {
				}
		}
		return "";
	}

	private String getLootName(int itemId) {
		if (itemId == COINS)
			return "Coins";
		if (keyMatch(itemId))
			return "key";
		for (int i = 0; i < FOOD_ARRAY.length; ++i)
			if (itemId == FOOD_ARRAY[i])
				return FOOD_NAMES[i];
		if (itemId == GGS)
			return "Group gatestone";
		if (itemId == ESSENCE)
			return "Rune essence";
		if (itemId == arrows)
			return "arrow";
		if (itemId == COSMIC_RUNES[1] || itemId == LAW_RUNES[1])
			return " rune";
		if (itemId == FEATHERS)
			return "Feather";
		if (Option.BURY.enabled() && intMatch(itemId, BONES))
			return "bones";
		return null;
	}

	/*
	 * private String getName(GroundItem gItem) { try { Item item = gItem.getGroundItem(); if (item != null) { final ItemDefinition definition = item.getDefinition(); if (definition != null) { String name = definition.getName(); if (name != null) return name.replaceAll("\\<.*?>", ""); } } }
	 * catch(Exception e) { log.severe("GROUNDITEM name " + e.toString()); } return ""; }
	 */

	private String getName(Item item) {
		if (item != null)
			try {
				final WidgetChild widg = item.getWidgetChild();
				if (widg != null) {
					final String name = widg.getChildName();
					if (name != null)
						return name.replaceAll("\\<.*?>", "");
				}
			} catch (final Exception e) {
				log.severe("ITEM name " + e.toString());
			}
		return "";
	}

	private NPC getNearestNpcTo(Tile heading, int id) {
		double dist = 99.99;
		NPC bestNpc = null;
		if (heading != null)
			for (final NPC npc : NPCs.getLoaded()) {
				final double nDist = Calculations.distance(npc.getLocation(), heading);
				if (nDist < dist && getId(npc) == id) {
					dist = nDist;
					bestNpc = npc;
				}
			}
		return bestNpc;
	}

	private NPC getNearestNpcTo(Tile heading, String name) {
		double dist = Double.MAX_VALUE;
		NPC bestNpc = null;
		if (heading != null) {
			name = name.toLowerCase();
			for (final NPC npc : NPCs.getLoaded()) {
				final double nDist = Calculations.distance(npc.getLocation(), heading);
				if (nDist < dist && getName(npc).toLowerCase().contains(name)) {
					dist = nDist;
					bestNpc = npc;
				}
			}
		}
		return bestNpc;
	}

	private Tile getNearestObjTo(Tile heading, int... ids) {
		double dist = Double.MAX_VALUE;
		Tile bestTile = null;
		if (heading != null)
			for (final SceneObject o : getObjsInRoom(ids)) {
				final double oDist = Calculations.distance(o.getLocation(), heading);
				if (oDist < dist) {
					dist = oDist;
					bestTile = o.getLocation();
				}
			}
		return bestTile;
	}

	private Tile getNearestSWObjTo(Tile heading, int... ids) {
		double dist = Double.MAX_VALUE;
		Tile bestTile = null;
		for (int x = targetRoom.x; x < heading.getX(); ++x)
			for (int y = targetRoom.y; y < heading.getY(); ++y) {
				final Tile t = new Tile(x, y, 0);
				if (isObjectAt(t, ids)) {
					final double tDist = Calculations.distance(heading, t);
					if (tDist < dist) {
						bestTile = t;
						dist = tDist;
					}
				}
			}
		return bestTile;
	}

	private Tile getNearestTileTo(Tile heading, Tile[] checks) {
		if (heading == null)
			return null;
		double dist = Double.MAX_VALUE;
		Tile bestTile = null;
		for (final Tile c : checks) {
			final double tDist = Calculations.distance(heading, c);
			if (tDist < dist) {
				bestTile = c;
				dist = tDist;
			}
		}
		return bestTile;
	}

	private SceneObject getNearObjTo(final Tile t, final int... ids) {
		return getRoomObj(myLoc(), new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject o) {
				return intMatch(o.getId(), ids) && Calculations.distance(t, o.getLocation()) < 7;
			}
		});
	}

	private Tile getNearTriple(Tile heading, Tile dest) {
		double dist = 99.99;
		final double dfDist = Calculations.distance(dest, heading);
		final Tile me = myLoc();
		Tile near = null;
		for (final Tile t : getDiagonalTriple(heading)) {
			final double tDist = Calculations.distance(me, t);
			if (tDist < dist && Calculations.distance(t, dest) > dfDist) {
				dist = tDist;
				near = t;
			}
		}
		return near;
	}

	private SceneObject getNextObj(SceneObject oldObj, int... ids) {
		if (oldObj == null)
			return getObjInRoom(ids);
		double dist = 99.99;
		SceneObject next = null;
		for (final SceneObject o : getObjsInRoom(ids)) {
			if (o.getLocation().equals(oldObj.getLocation()))
				continue;
			final double oDist = distTo(o);
			if (oDist < dist) {
				dist = oDist;
				next = o;
			}
		}
		return next;
	}

	private static NPC getNpcAt(final Tile check) {
		return check == null ? null : NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && npc.getLocation().equals(check);
			}
		});
	}

	private NPC getNpcInRoom(final int... ids) {
		return targetRoom == null ? null : NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && intMatch(getId(npc), ids) && targetRoom.contains(npc);
			}
		});
	}

	private NPC getNpcInRoom(final String... names) {
		return targetRoom == null ? null : NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && stringMatch(getName(npc), names) && targetRoom.contains(npc);
			}
		});
	}

	private NPC[] getNpcsInRoom() {
		return NPCs.getLoaded(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && (targetRoom == null || targetRoom.contains(npc));
			}
		});
	}

	private NPC[] getNpcsInRoom(final int... ids) {
		return targetRoom == null ? null : NPCs.getLoaded(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && intMatch(getId(npc), ids) && targetRoom.contains(npc);
			}
		});
	}

	private NPC[] getNpcsInRoom(final String... names) {
		return targetRoom == null ? null : NPCs.getLoaded(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && stringMatch(getName(npc), names) && targetRoom.contains(npc);
			}
		});
	}

	private SceneObject getObjInRoom(final int... ids) {
		return getRoomObj(myLoc(), new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject o) {
				return intMatch(o.getId(), ids);
			}
		});
	}

	private Tile getObjLocation(final int... ids) {
		final SceneObject obj = getObjInRoom(ids);
		return obj != null ? obj.getLocation() : null;
	}

	private SceneObject[] getObjsAt(Tile t) {
		try {
			return SceneEntities.getLoaded(t);
		} catch (final Exception e) {
			return new SceneObject[0];
		}
	}

	private SceneObject[] getObjsInRoom(final int... ids) {
		return getRoomObjs(targetRoom, new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject o) {
				return o != null && o.validate() && intMatch(o.getId(), ids);
			}
		});
	}

	private SceneObject getReachableObj(final int... ids) {
		return getRoomObj(myLoc(), new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject o) {
				return o != null && o.validate() && intMatch(o.getId(), ids) && reachableObj(o);
			}
		});
	}

	private SceneObject getReachableObjNear(final Locatable l, final int... ids) {
		return getRoomObj(l, new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject o) {
				return intMatch(o.getId(), ids) && reachableObj(o);
			}
		});
	}

	private GroundItem getRoomItem(Filter<GroundItem> filter) {
		double dist = 99.99;
		GroundItem ret = null;
		final Tile me = Players.getLocal().getLocation();
		for (int x = targetRoom.x + 1; x < targetRoom.nx; ++x)
			for (int y = targetRoom.y + 1; y < targetRoom.ny; ++y) {
				final double tDist = Calculations.distance(me, new Tile(x, y, 0));
				if (tDist < dist)
					for (final GroundItem item : GroundItems.getLoadedAt(x, y))
						if (item != null && filter.accept(item)) {
							dist = tDist;
							ret = item;
							break;
						}
			}
		return ret;
	}

	private GroundItem[] getRoomItems() {
		final ArrayList<GroundItem> items = new ArrayList<GroundItem>();
		for (int x = targetRoom.x + 1; x < targetRoom.nx; ++x)
			for (int y = targetRoom.y + 1; y < targetRoom.ny; ++y) {
				final GroundItem[] gItems = GroundItems.getLoadedAt(x, y);
				if (gItems == null)
					continue;
				for (final GroundItem item : gItems)
					if (item != null)
						items.add(item);
			}
		return items.toArray(new GroundItem[items.size()]);
	}

	private SceneObject getRoomObj(Locatable nearTo, Filter<SceneObject> filter) {
		if (targetRoom == null || nearTo == null)
			return null;
		SceneObject obj = null;
		double dist = 99.99;
		for (int x = targetRoom.x; x <= targetRoom.nx; ++x)
			for (int y = targetRoom.y; y <= targetRoom.ny; ++y) {
				final Tile t = new Tile(x, y, 0);
				final double tDist = Calculations.distance(nearTo, t);
				if (tDist < dist)
					for (final SceneObject o : getObjsAt(t))
						if (o != null && o.validate() && filter.accept(o)) {
							if (tDist == 0)
								return o;
							obj = o;
							dist = tDist;
							break;
						}
			}
		return obj;
	}

	private SceneObject[] getRoomObjs(Room room, Filter<SceneObject> filter) {
		if (room == null)
			return new SceneObject[0];
		final Set<SceneObject> objs = new LinkedHashSet<SceneObject>();
		for (int x = room.x; x <= room.nx; ++x)
			for (int y = room.y; y <= room.ny; ++y)
				for (final SceneObject o : getObjsAt(new Tile(x, y, 0)))
					if (o != null && o.validate() && filter.accept(o))
						objs.add(o);
		return objs.toArray(new SceneObject[objs.size()]);
	}

	private WidgetChild getShopItem(String name) {
		final WidgetChild shopItems = Widgets.get(956, 24);
		if (shopItems != null && shopItems.validate()) {
			name = name.toLowerCase();
			for (final WidgetChild c : shopItems.getChildren())
				if (c.getChildName().toLowerCase().endsWith(name + "</col>"))
					return c;
		}
		return null;
	}

	private Tile[] getSurrounding(Tile t) {
		final int x = t.getX(), y = t.getY();
		return new Tile[] { new Tile(x + 1, y + 1, 0), new Tile(x, y + 1, 0), new Tile(x - 1, y + 1, 0),
				new Tile(x - 1, y, 0), new Tile(x - 1, y - 1, 0), new Tile(x, y - 1, 0), new Tile(x + 1, y - 1, 0),
				new Tile(x + 1, y, 0) };
	}

	private Tile getTileOfObjs(int[]... objsIds) {
		final int requestedAmount = objsIds.length;
		for (final Tile t : targetRoom.tiles()) {
			int count = 0;
			for (final SceneObject o : getObjsAt(t)) {
				if (o == null)
					continue;
				for (final int[] ids : objsIds)
					if (intMatch(o.getId(), ids)) {
						++count;
						break;
					}
			}
			if (count == requestedAmount)
				return t;
		}
		return null;
	}

	private Combat getWeakness(NPC npc) {
		final Monster m = getMonster(npc);
		if (m != null)
			for (final Combat w : m.weaknesses)
				if (w.enabled()) {
					final Style style = w.style();
					if (style == combatStyle)
						return w;
					if (!style.temporary && style != Style.MAGIC)
						if (style == primaryStyle || npc.getLevel() > combatLevel - random(20, 30))
							return w;
				}
		return Melee.NONE;
	}

	private boolean hasCoins(int price) {
		return invCount(true, COINS) >= price;
	}

	private int healthPoints() {
		return (int) (Settings.get(1240) * 0.5);
	}

	private int health() {
		return healthPoints() * 10 / level(Skills.CONSTITUTION);
	}

	private boolean hoodMonster(NPC npc) {
		final Monster test = getMonster(npc);
		if (test != null)
			for (final Monster m : HOODED_MONSTERS)
				if (test == m)
					return true;
		return false;
	}

	private boolean improveArmorWielding() {
		if (improvements == null)
			return false;
		boolean upgraded = false;
		for (final int itemId : improvements)
			if (itemId > 0) {
				final Item iItem = invItem(itemId);
				final String iName = getName(iItem);
				if (iName.isEmpty() || iName.endsWith("(b)"))
					continue;
				final int tier = equipmentTier(iName);
				final Item item = invItem(itemId);
				if (item != null && tier != 0) {
					final String type = equipmentType(iName);
					for (int I = 0; I < slotNames.length; ++I)
						if (tier > equipmentTiers[I] && type.contains(slotNames[I])) {
							if (armorSlot == I && type.equals(armorType)) {
								boolean removed = false;
								final int oldArmor = valueOf(startArmor);
								log(LB, false, "Upgrading your tier " + equipmentTiers[I] + " " + type + " to "
										+ equipmentMaterial(iName));
								if (developer)
									log(LR, false, iName + " (b)");
								while (startArmor == oldArmor) {
									if (failCheck())
										return false;
									makeSpace(false);
									final int updatedArmor = getItemId(iName + " (b)");
									if (updatedArmor > 0)
										startArmor = updatedArmor;
									else if (invContains(oldArmor))
										destroyItem(oldArmor);
									else if (!removed && equipItem(EQUIP_SLOTS[I]) != null) {
										if (equipAction(EQUIP_SLOTS[I], "Remove"))
											Time.sleep(400, 800);
										removed = equipItem(EQUIP_SLOTS[I]) == null;
									} else if (invContains(itemId))
										ridItem(itemId, "Bind");
									else if (pickUpItem(getItemInRoom(targetRoom, itemId)))
										waitToStop(false);
									else
										removed = false;
									Time.sleep(400, 800);
								}
								equipmentTiers[I] = tier;
								remove(bounds, oldArmor);
								save(bounds, startArmor);
								upgraded = ridItem(startArmor, "Wear");
							} else if (ridItem(itemId, "Wear")) {
								equipmentTiers[I] = tier;
								upgraded = true;
							}
							break;
						}
				}
			}
		improvements = null;
		return upgraded;
	}

	private void improveEquipment() {
		final boolean wepCheck = weaponTier < 10 && weaponTier < attackTier;
		int bestGroundTier = valueOf(weaponTier);
		for (final GroundItem gItem : getRoomItems()) {
			final int itemId = gItem.getId();
			final String iType = equipmentType(itemId);
			final int tier = equipmentTier(itemId);
			if (tier == 0)
				continue;
			if (wepCheck && iType.equals(weaponType)) {
				if (tier > bestGroundTier && tier <= attackTier) {
					bestGroundTier = tier;
					save(bounds, newWeapon = gItem.getId());
				}
			} else if (tier <= defenseTier)
				for (int I = 0; I < tempTiers.length; ++I)
					if ((I != 2 || !twoHanded) && iType.contains(slotNames[I])) {
						if (tier > tempTiers[I]) {
							log(BLU, developer, "Upgrading your " + iType + " to tier " + tier);
							tempTiers[I] = tier;
							if (improvements == null)
								improvements = new int[tempTiers.length];
							if (improvements[I] < itemId)
								improvements[I] = itemId;
						}
						break;
					}
		}
	}

	private void improveBossWeapon() {
		if (weaponTier < attackTier)
			for (final Item item : invItems(false)) {
				final String iName = equipmentName(item);
				final String iType = equipmentType(iName);
				if (iType.equals(weaponType)) {
					final int tier = equipmentTier(iName);
					if (tier > weaponTier && tier <= attackTier) {
						newWeapon = item.getId();
						break;
					}
				}
			}
		if (pickupId > 0 && invContains(pickupId))
			newWeapon = valueOf(pickupId);
	}

	private void improveWeaponBinding() {
		if (newWeapon > 0) {
			final Item iItem = invItem(newWeapon);
			final String iName = equipmentName(iItem);
			final int tier = equipmentTier(iName);
			if (tier > weaponTier) {
				boolean weaponRemoved = false;
				final int oldWep = valueOf(primaryWep);
				log(LB, true, "Upgrading your " + weaponType + " to " + equipmentMaterial(iName));
				waitForResponse();
				idleTimer = new Timer(0);
				while (primaryWep == oldWep) {
					if (failCheck())
						return;
					makeSpace(false);
					final int updatedWep = getItemId(iName + " (b)");
					if (updatedWep > 0) {
						primaryWep = updatedWep;
						if (combatStyle == primaryStyle)
							currentWep = updatedWep;
						break;
					}
					if (invContains(oldWep))
						destroyItem(oldWep);
					else if (!weaponRemoved) {
						if (equipAction(Slot.WEAPON, "Remove") || equipAction(Slot.WEAPON, "Remove"))
							Time.sleep(400, 800);
						weaponRemoved = equipItem(Slot.WEAPON) == null;
					} else if (invContains(newWeapon))
						ridItem(newWeapon, "Bind");
					else if (pickUpItem(getItemInRoom(targetRoom, newWeapon)))
						waitToStop(false);
					else
						weaponRemoved = false;
					Time.sleep(400, 800);
				}
				if (combatStyle == primaryStyle)
					ridItem(primaryWep, "Wield");
				weaponTier = tier;
				remove(bounds, newWeapon);
				newWeapon = -1;
			}
		}
	}

	private boolean inRoom(Room... room) {
		for (final Room r : room)
			if (r != null && r.contains(myLoc()))
				return true;
		return false;
	}

	private boolean intentionallyBacktrack() {
		final Tile backDoor = getBackDoor();
		if (backDoor == null)
			return false;
		while (roomNumber > 1 && getCurrentRoom().equals(targetRoom)) {
			if (!inDungeon() || failBasic())
				return false;
			final SceneObject door = SceneEntities.getAt(backDoor);
			if (door == null)
				return false;
			if (doObjAction(door, "Enter")) {
				waitToStart(false);
				while (moving() && inRoom(targetRoom))
					Time.sleep(200, 300);
			}
			Time.sleep(100, 200);
		}
		setTargetRoom(currentRoom);
		return true;
	}

	private boolean interact(Item item, String action) {
		return item.getWidgetChild().interact(action);
	}

	private boolean interact(NPC npc, String action) {
		return interact(npc, action, null);
	}

	private boolean interact(NPC npc, String action, String npcName) {
		// CapturedModel m = npc.getModel();
		// if (m == null)
		// return clickTile(npc, action, npcName);
		// for (int i = 0, r = random(8, 12); i < r; ++i) {
		// if (!npc.validate() || isDead(npc))
		// break;
		// Point p = m.getNextViewportPoint();
		// if (p.x != -1) {
		// if (!randomActivate(2) || !npc.contains(p))
		// Mouse.move(p.x, p.y);
		// if (Menu.select(action, npcName))
		// return true;
		// }
		// }
		// return false;
		try {
			return npc.interact(action, npcName);
		} catch (final Exception e) {
			if (developer)
				logD("Npc interact");
			return false;
		}
	}

	private boolean interact(SceneObject obj, String action, String name) {
		final CapturedModel m = obj.getModel();
		if (m == null)
			return clickTile(obj.getLocation(), action, name);
		for (int c = 0; c < 5; ++c) {
			if (!obj.validate() || !m.validate())
				break;
			if ((c > 0 || m.contains(Mouse.getLocation())) && !randomActivate(2) && Menu.select(action, name))
				return true;
			final Point p = m.getNextViewportPoint();
			if (p.x == -1)
				break;
			Mouse.move(p);
			if (Menu.select(action, name))
				return true;
		}
		return false;
	}

	private boolean interactingWith(Character c) {
		final Character inter = player().getInteracting();
		return inter != null && inter.equals(c);
	}

	private static boolean intMatch(int desired, int... compare) {
		if (compare != null && desired > 0)
			for (final int check : compare)
				if (desired == check)
					return true;
		return false;
	}

	private int doorIndex(int desired, int[][] compares) {
		for (int I = 0; I < compares.length; ++I)
			if (desired == compares[I][floor.index])
				return I;
		return -1;
	}

	private boolean inDungeon() {
		return Game.getPlane() != 1 && (dungeonLoaded() || distTo(DAEMONHEIM) > 50);
	}

	private boolean inTrueCombat() {
		final Player m = player();
		return m.validate() && m.isInCombat() && (!isAutoRetaliateEnabled() || m.getInteracting() != null)
				&& isNpcInteracting();
	}

	private boolean invContains(int... itemIds) {
		return invItem(itemIds) != null;
	}

	private boolean invCacheContains(int... itemIds) {
		return invCacheItem(itemIds) != null;
	}

	private boolean invContainsAll(int... itemIds) {
		for (final int i : itemIds)
			if (invItem(i) == null)
				return false;
		return true;
	}

	private int invCount(boolean stacked) {
		final Item[] items = invItems(false);
		if (!stacked)
			return items.length;
		int count = 0;
		for (final Item i : items)
			count += i.getStackSize();
		return count;
	}

	private int invCount(int... itemIds) {
		return invCount(false, itemIds);
	}

	private int invCount(boolean stacked, int... itemIds) {
		int count = 0;
		for (final Item i : invItems(false))
			if (i != null && intMatch(i.getId(), itemIds))
				count += stacked ? i.getStackSize() : 1;
		return count;
	}

	private boolean invFull() {
		return invCount(false) == 28;
	}

	private boolean invHasSale() {
		for (final Item item : invItems(false))
			if (riddable(item))
				return true;
		return false;
	}

	private Item invItem(int... itemIds) {
		for (final Item it : invItems(false))
			if (it != null && intMatch(it.getId(), itemIds))
				return it;
		return null;
	}

	private Item invCacheItem(int... itemIds) {
		for (final Item it : invItems(true))
			if (it != null && intMatch(it.getId(), itemIds))
				return it;
		return null;
	}

	private Item invItem(boolean cached, String name) {
		for (final Item it : invItems(cached))
			if (getName(it).contains(name))
				return it;
		return null;
	}

	private Item[] invItems(boolean cached) {
		WidgetChild inventoryWidget = roomNumber == 1 ? Widgets.get(957, 0) : null;
		if (inventoryWidget == null || inventoryWidget.getAbsoluteX() < 50) {
			if (!cached)
				open(Tabs.INVENTORY);
			inventoryWidget = Widgets.get(679, 0);
		}
		if (inventoryWidget != null) {
			final WidgetChild[] inventoryChildren = inventoryWidget.getChildren();
			if (inventoryChildren.length > 27) {
				final List<Item> items = new LinkedList<Item>();
				for (int i = 0; i < 28; ++i)
					if (inventoryChildren[i].getChildId() != -1)
						items.add(new Item(inventoryChildren[i]));
				return items.toArray(new Item[items.size()]);
			}
		}
		return new Item[0];
	}

	private Item[] invItems(int... ids) {
		final List<Item> items = new LinkedList<Item>();
		for (final Item it : invItems(false))
			if (it.getWidgetChild().validate() && intMatch(it.getId(), ids))
				items.add(it);
		return items.toArray(new Item[items.size()]);
	}

	private int invSaleAfter() {
		int count = 0;
		for (final Item item : invItems(false))
			if (item != null && item.getId() > 0)
				count += !riddable(item) ? 1 : 0;
		return count;
	}

	private boolean isAttacking(NPC npc) {
		if (npc == null || !npc.validate())
			return false;
		if (isDead(npc))
			return true;
		return interactingWith(npc);
	}

	private boolean isAutocasting() {
		return getFightMode() == 4;
	}

	private boolean isDead() {
		return isDead || Settings.get(1240) == 1;
	}

	private boolean isDead(NPC n) {
		return n.getHpRatio() == 0 && n.getInteracting() == null;
	}

	private boolean isEdgeTile(Tile check) {
		if (check == null || currentRoom == null)
			return false;
		final int mX = check.getX(), mY = check.getY(), sX = currentRoom.x, sY = currentRoom.y, eX = sX + 15, eY = sY + 15;
		return Math.abs(mX - sX) == 1 || Math.abs(mX - eX) == 1 || Math.abs(mY - sY) == 1 || Math.abs(mY - eY) == 1;
	}

	private boolean isGoodMonster(NPC targ) {
		return targ != null && targ.validate() && (targ.isInCombat() || isAttacking(targ)) && !isDead(targ);
	}

	private boolean isGoodTile(Tile t, int... badIds) {
		if (t == null || roomFlags.contains(t) || !targetRoom.contains(t))
			return false;
		if (badIds.length != 0)
			for (final SceneObject o : getObjsAt(t))
				if (o != null && intMatch(o.getId(), badIds))
					return false;
		return true;
	}

	private boolean isItemAt(Tile t) {
		for (final GroundItem item : GroundItems.getLoadedAt(t.getX(), t.getY()))
			if (item != null && itemFilter.accept(item))
				return true;
		return false;
	}

	private boolean isNpcInRoom() {
		return targetRoom == null ? false : NPCs.getNearest(new Filter<NPC>() {
			@Override
			public boolean accept(NPC npc) {
				return npc != null && targetRoom.contains(npc);
			}
		}) != null;
	}

	private boolean isNpcInteracting() {
		for (final NPC n : getNpcsInRoom())
			if (n.getInteracting() != null)
				return true;
		return false;
	}

	private boolean isObjectAt(Tile t, int... ids) {
		for (final SceneObject o : getObjsAt(t))
			if (intMatch(o.getId(), ids))
				return true;
		return false;
	}

	private boolean isOutside() {
		if (settingsFinished && !newDungeon && inDungeon && !exit && !status.startsWith("Entering") && !inDungeon()) {
			severe("Oops.. We ended up outside somehow :S");
			clearAll();
			floor = Floor.ALL;
			inDungeon = false;
			newDungeon = false;
			return true;
		}
		return false;
	}

	private boolean isPoisoned() {
		return Settings.get(102) > 0;
	}

	private boolean isProtecting(NPC npc, int baseId, int style) {
		return (getId(npc) - baseId - style - 1) % 3 == 0;
	}

	private boolean isStart(Room r) {
		return r != null && r.index == 1;
	}

	private void kalgerState(Style kStyle) {
		if (kStyle == Style.MELEE) {
			swapStyle(Style.MAGIC, Style.RANGE);
			protection = Style.MELEE;
		} else if (kStyle == Style.RANGE) {
			swapStyle(Style.MELEE, Style.MAGIC);
			protection = Style.RANGE;
		} else if (kStyle == Style.MAGIC) {
			swapStyle(Style.RANGE, Style.MELEE);
			protection = Style.MAGIC;
		}
	}

	private Style kalgerStyle() {
		return bossStage == 3 ? Style.MAGIC : bossStage == 5 ? Style.RANGE : Style.MELEE;
	}

	private boolean keyDoorMatch(int doorId) {
		return doorId > 50207 && doorId < 50272;
	}

	private int keyId(int colorIdx, int shapeIdx) {
		return 18202 + 16 * colorIdx + 2 * shapeIdx;
	}

	private boolean keyInLock(int keyId, int doorId) {
		return (keyId - 18202) / 2 == doorId - 50208;
	}

	private final boolean keyMatch(int itemId) {
		return itemId > 18201 && itemId < 18329;
	}

	private boolean knownReason() {
		return !levelRequirement || failReason.startsWith("Members") || failReason.contains("Prayer")
				|| failReason.contains("Summoning");
	}

	private static int level(int skill) {
		int level = Skills.getRealLevel(skill);
		if (level > 99)
			if (skill == Skills.DUNGEONEERING) {
				if (level > 120)
					level = 120;
			} else
				level = 99;
		return level;
	}

	public static Image getImage(final String name, final String url) {
		File file = new File(name);
		if (file.exists()) {
			try {
				return ImageIO.read(file);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				final BufferedImage img = ImageIO.read(new URL(url));
				if (img != null) {
					ImageIO.write(img, "png", file);
					return img;
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	private Image loadImg(String name) {
		try {
			final File imageFile = new File(dir + name + ".png");
			if (imageFile.exists() && imageFile.isFile() && imageFile.canRead()){
			System.out.println(name);
				return ImageIO.read(imageFile);
			}
			System.out.println("Downloading image: " + name);
			final URL imageUrl = new URL("http://bottingfrom.asia/others/dung/" + name + ".png");
			if(true)
				return ImageIO.read(imageUrl);
			final InputStream is = imageUrl.openStream();
			try {
				if (!new File(dir).exists())
					new File(dir).mkdirs();
				final OutputStream os = new FileOutputStream(imageFile);
				final byte[] b = new byte[2048];
				int length;
				while ((length = is.read(b)) != -1)
					os.write(b, 0, length);
				os.flush();
				is.close();
				os.close();
			} catch (final Exception e) {
				e.printStackTrace();
			}
			if (imageFile.exists() && imageFile.isFile() && imageFile.canRead())
				return ImageIO.read(imageFile);
			return ImageIO.read(imageUrl.openStream());
		} catch (final Exception e) {
			System.out.println("Image load: [" +e.getClass().getSimpleName()+"] "+e.getMessage());
			return null;
		}
	}

	private boolean makeGatestone(boolean force) {
		if ((force || gateRoom == null) && !invContains(GATESTONE)) {
			if (!castable(Dungeoneering.CREATE_GATESTONE)) {
				Time.sleep(200, 400);
				if (!castable(Dungeoneering.CREATE_GATESTONE))
					return false;
			}
			final String oldStatus = secondaryStatus;
			secondaryStatus = "Creating a gatestone teleport";
			while (!invContains(GATESTONE)) {
				if (failCheck() || inRoom(bossRoom))
					return false;
				if (invFull()) {
					if (!makeSpace(false) && !makeSpace(true))
						break;
				} else if (castSpell(Dungeoneering.CREATE_GATESTONE, force ? "Quick-cast" : "Cast")) {
					waitToAnimate();
					if (player().getAnimation() == 713) {
						open(Tabs.INVENTORY);
						waitToStop(false);
					}
				} else
					Time.sleep(200, 400);
			}
			secondaryStatus = oldStatus;
		}
		return invContains(GATESTONE) && dropItem(GATESTONE);
	}

	private ArrayList<Tile> makePath(Tile start, Tile dest, boolean goodDest, int... goodIds) {
		final ArrayList<Tile> nodes = new ArrayList<Tile>(), oldNodes = new ArrayList<Tile>();
		if (start == null || dest == null)
			return nodes;
		Tile current = start, next = null;
		targetRoom.setFlags();
		if (goodIds.length != 0) {
			final ArrayList<Tile> toRemove = new ArrayList<Tile>();
			for (final SceneObject o : getObjsInRoom(goodIds))
				toRemove.add(o.getLocation());
			roomFlags.removeAll(toRemove);
		}
		roomFlags.remove(dest);
		int tries = 0, masterTries = 0;
		nodes.add(current);
		while (!current.equals(dest)) {
			double lowest = 99.99;
			for (final Tile t : getSurrounding(current)) {
				if ((goodDest && Calculations.distance(t, dest) == 1) || t.equals(dest)) {
					nodes.add(t);
					return nodes;
				}
				if (isGoodTile(t) && !oldNodes.contains(t)) {
					final double tDist = Calculations.distance(t, dest);
					if (tDist < lowest) {
						next = t;
						lowest = tDist;
					}
				}
			}
			++tries;
			if (next == null || masterTries > 15)
				return new ArrayList<Tile>();
			if (next.equals(dest)) {
				nodes.add(next);
				current = next;
			} else {
				for (final Tile t : getSurrounding(next))
					if (isGoodTile(t) && !oldNodes.contains(t)) {
						final double tDist = Calculations.distance(t, dest);
						if (tDist < lowest) {
							nodes.add(next);
							current = next;
							break;
						}
					}
				if (!current.equals(next))
					for (final Tile t : getSurrounding(next))
						if (oldNodes.contains(t)) {
							nodes.add(next);
							current = next;
							break;
						}
				oldNodes.add(next);
			}
			if (tries > 30) {
				++masterTries;
				nodes.clear();
				current = start;
				next = null;
				tries = 0;
			}
		}
		return nodes;
	}

	private ArrayList<Tile> makeSmartPath(Tile current, Tile dest, int... goodIds) {
		final ArrayList<Tile> smart = new ArrayList<Tile>();
		final ArrayList<Tile> tiles = makePath(current, dest, false, goodIds);
		int idx = -1;
		for (int I = 1; I < tiles.size(); ++I) {
			final Tile test = tiles.get(I), prev = tiles.get(I - 1);
			final Tile[] surr = getSurrounding(prev);
			if (idx == -1) {
				for (int J = 0; J < surr.length; ++J)
					if (test.equals(surr[J])) {
						idx = J;
						break;
					}
			} else if (!surr[idx].equals(test)) {
				smart.add(prev);
				idx = -1;
				--I;
			}
		}
		smart.add(dest);
		return puzzlePoints = smart;
	}

	private boolean makeSpace(boolean drop) {
		if (!invFull())
			return true;
		if (!topUp() && !buryBones() && !improveArmorWielding())
			if ((!drop || !dropAllExceptSaves())) {
				if (puzzleTimer == null) {
					final int o = getItemId("(o)");
					if (o != -1 && destroyItem(o))
						return true;
				}
				int id = -1;
				if (invContains(FOOD_ARRAY))
					return eatFood(FOOD_ARRAY, 101, 30);
				if (invContains(ANTIPOISON))
					id = ANTIPOISON;
				else if (fish != null && invContains(fish.foodId))
					id = fish.foodId;
				else if (logs != null && invContains(logs.logId))
					id = logs.logId;
				return ridItem(id, "Drop") || dropAllExceptSaves();
			}
		return true;
	}

	private boolean mapOpen() {
		final WidgetChild map = Widgets.get(942, 6);
		return map != null && map.validate();
	}

	private int currTier(int skill) {
		final int level = Skills.getLevel(skill), limit = memberWorld ? 11 : 5;
		final int tier = level == 99 ? 11 : level / 10 + 1;
		return tier > limit ? limit : tier;
	}

	private int maxTier(int skill) {
		final int level = level(skill), limit = memberWorld ? 11 : 5;
		final int tier = level == 99 ? 11 : level / 10 + 1;
		return tier > limit ? limit : tier;
	}

	private int theoreticalTier(int skill) {
		final int level = level(skill);
		return level == 99 ? 11 : level / 10 + 1;
	}

	private boolean memberCheck() {
		if (!memberWorld) {
			failReason = "Members-only";
			return false;
		}
		return true;
	}

	private boolean monsterBacktrack() {
		if (targetRoom == null)
			return false;
		while (NPCs.getNearest(guardians) != null) {
			if (failSafe())
				return false;
			if (targetRoom.isUnbacktrackable) {
				log(LR, false, "Can't backtrack from this room, killing off enemies");
				if (!fightMonsters() || !pickUpAll())
					return false;
			} else {
				if (developer)
					log(null, false, "We need to backtrack further before teleporting home");
				intentionallyBacktrack();
			}
		}
		return true;
	}

	private Effect monsterPrayer() {
		if (prayerLevel > 36) {
			if (strongestMonster != null) {
				final NPC targ = getGoodNpc(strongestMonster);
				if (targ != null)
					return monsterEffect(targ);
			}
			NPC targ = memberWorld ? getNpcInRoom("dragon") : null;
			if (targ != null) {
				strongestMonster = getName(targ);
				return protect(targ, Style.MAGIC);
			}
			int level = 0;
			for (final NPC npc : NPCs.getLoaded(guardians))
				if (npc != null && npc.getLevel() > level) {
					targ = npc;
					level = npc.getLevel();
				}
			if (targ != null) {
				targ.getHeight();
				strongestMonster = getName(targ);
				final int cb = combatLevel / 10;
				final int hcb = (int) Math.round(cb * 0.5);
				if (level > cb * (Option.PURE.enabled() ? 8 : 9) + random(-hcb, hcb))
					return monsterEffect(targ);
			}
		}
		strongestMonster = null;
		return null;
	}

	private Effect monsterEffect(NPC targ) {
		final Monster m = getMonster(targ);
		return m != null ? protect(targ, protection = m.combatStyle) : null;
	}

	private void moveMouseRandomly() {
		Mouse.move(Mouse.getLocation(), random(20, 100), random(20, 100));
	}

	private void moveCameraRandomly() {
		threadSwivel(90, 45);
	}

	private boolean moving() {
		return Players.getLocal().getSpeed() != 0;
	}

	private static Tile myLoc() {
		return Players.getLocal().getLocation();
	}

	private void omNomNom() {
		final String oldStatus = secondaryStatus;
		while (invContains(foods)) {
			if (foodTimer != null && foodTimer.isRunning()) {
				if (startHp == -1 || Math.abs(health() - startHp) < 10) {
					foodTimer = null;
					continue;
				}
			} else {
				if (!topUp())
					break;
				secondaryStatus = "Om nom nom!";
			}
			Time.sleep(200, 400);
		}
		secondaryStatus = oldStatus;
	}

	private static boolean open(Tabs tab) {
		return tab.open(false);
	}

	private boolean openPartyTab() {
		for (int c = 0; c < 30; ++c) {
			if (failBasic())
				return false;
			if (partyTabSelected() && updateProgress())
				return true;
			if (clickRing("Open party interface"))
				for (int d = 0; d < 15; ++d) {
					if (partyTabSelected() && updateProgress())
						return true;
					Time.sleep(200, 300);
				}
			Time.sleep(100, 300);
		}
		return false;
	}

	private Point orientation(Character ch) {
		if (ch == null || !ch.validate())
			return null;
		final double radians = Math.toRadians(ch.getOrientation());
		final double cos = Math.cos(radians), sin = Math.sin(radians);
		int x = 0, y = 0;
		if (cos > 0.706)
			x = 1;
		else if (cos < -0.706)
			x = -1;
		if (sin > 0.706)
			y = 1;
		else if (sin < -0.706)
			y = -1;
		return new Point(x, y);
	}

	private boolean pauseScript() {
		if (isPaused()) {
			log(LR, false, "Pausing iDungeon Pro...");
			// int prevEnabled = enabledFlags;
			// int prevDisabled = disabledFlags;
			// enabledFlags = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;
			// disabledFlags = Environment.INPUT_KEYBOARD | Environment.INPUT_MOUSE;
			// setUserInput(true);
			while (isPaused()) {
				if (stopScript)
					return true;
				Time.sleep(25, 50);
			}
			// enabledFlags = prevEnabled;
			// disabledFlags = prevDisabled;
			// setUserInput(false);
			log(null, false, "Resuming script!");
			if (idleTimer.getElapsed() > 60000) {
				if (puzzleTimer != null)
					puzzleTimer.reset();
				failTimer.reset();
			}
			idleTimer = new Timer(0);
			if (doorTimer != null)
				doorTimer = new Timer(0);
		}
		return stopScript;
	}

	private boolean partyFormed() {
		if (partyFormed)
			return true;
		final WidgetChild leader = Widgets.get(939, 59);
		return leader != null && leader.validate() && leader.visible() && !leader.getText().isEmpty();
	}

	private boolean partyInteract(Player p, String action) {
		return p != null && atLoc(p) ? player().interact(action, p.getName()) : p.interact(action, p.getName());
	}

	private boolean partyJoined() {
		return partyMembers().containsAll(partyMembers);
	}

	private ArrayList<String> partyMembers() {
		final ArrayList<String> party = new ArrayList<String>();
		if (!partyTabSelected())
			openPartyTab();
		String username = username();
		final Widget details = Widgets.get(939);
		if (details.validate()) {
			if (!partyFormed())
				return party;
			final WidgetChild leader = details.getChild(59);
			partyLeader = leader.getText();
			if (leader.equals(username))
				username = null;
			for (int c = 0; c < 4; c++) {
				final WidgetChild member = details.getChild(62 + c * 3);
				if (member.visible() && !member.getText().isEmpty()) {
					if (username != null && member.getText().equals(username)) {
						partyIndex = c + 1;
						isLeader = false;
						username = null;
					}
					party.add(member.getText());
				}
			}
			if (!settingsFinished && partyMembers.isEmpty())
				partyMembers = party;
			partySize = party.size() + 1;
		}
		return party;
	}

	private Player partyPlayer(final String name) {
		return Players.getNearest(new Filter<Player>() {
			@Override
			public boolean accept(Player p) {
				return p != null && p.validate() && p.getName().equals(name.replaceAll("\u00A0", " "));
			}
		});
	}

	private boolean partyPlayerIn(Room r, String name) {
		final Player p = partyPlayer(name);
		return p != null && (r == null || r.contains(p));
	}

	private boolean partyPresent() {
		if (!isLeader && !partyPlayerIn(targetRoom, partyLeader))
			return false;
		for (final String m : partyMembers)
			if (!partyPlayerIn(targetRoom, m))
				return false;
		return true;
	}

	private void partySet(boolean enabled) {
		if (partyMode = enabled)
			cooperativeParty = enabled && Option.COOPERATIVE.enabled();
	}

	private boolean partyTabSelected() {
		final WidgetChild t = Widgets.get(548, 116);
		return t != null && t.validate() && t.getTextureId() == 1836;
	}

	private long perFloor(double compare) {
		return Math.round(compare / (dungeonCount + controlledAborts + abortedCount + 1));
	}

	private Player player() {
		return Players.getLocal();
	}

	private boolean playerIdle() {
		return !moving() && player().getAnimation() == -1;
	}

	private boolean pickUpItem(GroundItem item) {
		if (item == null)
			return false;
		final Tile itemTile = item.getLocation();
		if (!moving() && !itemTile.isOnScreen()) {
			walkFast(itemTile, true, 1);
			if (waitToStart(false))
				while (!itemTile.isOnScreen() && moving()) {
					if (failCheck())
						return false;
					topUp();
					Time.sleep(100, 300);
				}
			else
				unStick();
		}
		double x1 = .4, x2 = .6, y1 = .4, y2 = .6;
		if (roomNumber == 1 && !isGoodTile(itemTile)) {
			if (isEdgeTile(itemTile)) {
				final int diff = itemTile.getX() - myLoc().getX();
				if (diff > 1) {
					x1 = .60;
					x2 = .95;
				} else if (diff < -1) {
					x1 = .05;
					x2 = .40;
				}
			}
			y1 = .8;
			y2 = 1;
		}
		return clickTile(itemTile, Random.nextDouble(x1, x2), Random.nextDouble(y1, y2), "Take",
				getLootName(item.getId()));
	}

	private Tile portalAdjacent(SceneObject portal) {
		if (portal != null) {
			final Tile pTile = portal.getLocation();
			if (reachable(pTile, false))
				return !badTiles.contains(pTile) ? pTile : null;
			for (final Tile t : getAdjacentTo(pTile))
				if (!badTiles.contains(pTile) && reachable(t, false))
					return t;
		}
		return null;
	}

	private SceneObject portalObject() {
		return getRoomObj(myLoc(), new Filter<SceneObject>() {
			@Override
			public boolean accept(SceneObject o) {
				return o != null && o.getId() == WARPED_PORTAL && distTo(o) > 1 && reachable(o, true)
						&& portalAdjacent(o) != null;
			}
		});
	}

	private int prayerPercent() {
		return Prayer.getPoints() * 100 / prayerLevel;
	}

	private Effect protect(NPC n, Style style) {
		if (style != null)
			switch (style) {
			case MELEE:
				return isCursing ? Ancient.DEFLECT_MELEE : Modern.PROTECT_FROM_MELEE;
			case RANGE:
				return isCursing ? Ancient.DEFLECT_MISSILE : Modern.PROTECT_FROM_MISSILES;
			case MAGIC:
				return isCursing ? Ancient.DEFLECT_MAGIC : Modern.PROTECT_FROM_MAGIC;
			case MELEE_MAGIC:
				if (n == null || primaryStyle == Style.MELEE || distTo(n) > 3)
					return isCursing ? Ancient.DEFLECT_MAGIC : Modern.PROTECT_FROM_MAGIC;
				return isCursing ? Ancient.DEFLECT_MELEE : Modern.PROTECT_FROM_MELEE;
			case RANGE_MAGIC:
				if (primaryStyle == Style.MELEE)
					return isCursing ? Ancient.DEFLECT_MAGIC : Modern.PROTECT_FROM_MAGIC;
				return isCursing ? Ancient.DEFLECT_MELEE : Modern.PROTECT_FROM_MELEE;
			case SUMMON:
				return !memberWorld ? null : isCursing ? Ancient.DEFLECT_SUMMONING : Modern.PROTECT_FROM_SUMMONING;
			}
		return null;
	}

	private boolean reachable(Locatable dest, boolean isObject) {
		if (dest == null)
			return false;
		final Tile start = myLoc(), finish = dest.getLocation();
		if (Calculations.distance(start, finish) < (isObject ? 2 : 1))
			return true;
		final int startX = start.getX() - Game.getBaseX(), startY = start.getY() - Game.getBaseY();
		final int destX = finish.getX() - Game.getBaseX(), destY = finish.getY() - Game.getBaseY();
		final int[][] prev = new int[104][104], dist = new int[104][104];
		final int[] path_x = new int[4000], path_y = new int[4000];
		for (int xx = 0; xx < 104; ++xx)
			for (int yy = 0; yy < 104; ++yy) {
				prev[xx][yy] = 0;
				dist[xx][yy] = 99999999;
			}
		try {
			int cX = startX, cY = startY;
			prev[startX][startY] = 99;
			dist[startX][startY] = 0;
			int path_ptr = 0, step_ptr = 0, attempts = 0;
			path_x[path_ptr] = startX;
			path_y[path_ptr++] = startY;
			final int[][] blocks = Walking.getCollisionFlags(Game.getPlane());
			final int pathLength = path_x.length;
			while (step_ptr != path_ptr) {
				cX = path_x[step_ptr];
				cY = path_y[step_ptr];
				if (isObject) {
					if (Math.abs(cX - destX) + Math.abs(cY - destY) == 1)
						return true;
				} else if (cX == destX && cY == destY)
					return true;
				step_ptr = (step_ptr + 1) % pathLength;
				final int cost = dist[cX][cY] + 1;
				if ((cY > 0) && (prev[cX][cY - 1] == 0) && ((blocks[cX + 1][cY] & 0x1280102) == 0)) {
					path_x[path_ptr] = cX;
					path_y[path_ptr] = cY - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX][cY - 1] = 1;
					dist[cX][cY - 1] = cost;
				}
				if ((cX > 0) && (prev[cX - 1][cY] == 0) && ((blocks[cX][cY + 1] & 0x1280108) == 0)) {
					path_x[path_ptr] = cX - 1;
					path_y[path_ptr] = cY;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX - 1][cY] = 2;
					dist[cX - 1][cY] = cost;
				}
				if ((cY < 103) && (prev[cX][cY + 1] == 0) && ((blocks[cX + 1][cY + 2] & 0x1280120) == 0)) {
					path_x[path_ptr] = cX;
					path_y[path_ptr] = cY + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX][cY + 1] = 4;
					dist[cX][cY + 1] = cost;
				}
				if ((cX < 103) && (prev[cX + 1][cY] == 0) && ((blocks[cX + 2][cY + 1] & 0x1280180) == 0)) {
					path_x[path_ptr] = cX + 1;
					path_y[path_ptr] = cY;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX + 1][cY] = 8;
					dist[cX + 1][cY] = cost;
				}
				if ((cX > 0) && (cY > 0) && (prev[cX - 1][cY - 1] == 0) && ((blocks[cX][cY] & 0x128010e) == 0)
						&& ((blocks[cX][cY + 1] & 0x1280108) == 0) && ((blocks[cX + 1][cY] & 0x1280102) == 0)) {
					path_x[path_ptr] = cX - 1;
					path_y[path_ptr] = cY - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX - 1][cY - 1] = 3;
					dist[cX - 1][cY - 1] = cost;
				}
				if ((cX > 0) && (cY < 103) && (prev[cX - 1][cY + 1] == 0) && ((blocks[cX][cY + 2] & 0x1280138) == 0)
						&& ((blocks[cX][cY + 1] & 0x1280108) == 0) && ((blocks[cX + 1][cY + 2] & 0x1280120) == 0)) {
					path_x[path_ptr] = cX - 1;
					path_y[path_ptr] = cY + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX - 1][cY + 1] = 6;
					dist[cX - 1][cY + 1] = cost;
				}
				if ((cX < 103) && (cY > 0) && (prev[cX + 1][cY - 1] == 0) && ((blocks[cX + 2][cY] & 0x1280183) == 0)
						&& ((blocks[cX + 2][cY + 1] & 0x1280180) == 0) && ((blocks[cX + 1][cY] & 0x1280102) == 0)) {
					path_x[path_ptr] = cX + 1;
					path_y[path_ptr] = cY - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX + 1][cY - 1] = 9;
					dist[cX + 1][cY - 1] = cost;
				}
				if ((cX < 103) && (cY < 103) && (prev[cX + 1][cY + 1] == 0)
						&& ((blocks[cX + 2][cY + 2] & 0x12801e0) == 0) && ((blocks[cX + 2][cY + 1] & 0x1280180) == 0)
						&& ((blocks[cX + 1][cY + 2] & 0x1280120) == 0)) {
					path_x[path_ptr] = cX + 1;
					path_y[path_ptr] = cY + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX + 1][cY + 1] = 12;
					dist[cX + 1][cY + 1] = cost;
				}
				if (++attempts > 1000)
					return false;
			}
		} catch (final Exception e) {
		}
		return false;
	}

	private boolean reachableObj(SceneObject obj) {
		if (obj != null)
			for (final Tile t : tiles(obj.getArea()))
				if (reachable(t, true))
					return true;
		return false;
	}

	private boolean reachableTwice(Tile dest, boolean isObject) {
		return reachable(dest, isObject) || !makePath(myLoc(), dest, isObject).isEmpty();
	}

	private boolean readyToCook() {
		if (!Option.MAKE_FOOD.enabled() || aComplexity < 4 || (bossRoom == null && !isGoodDoor()))
			return cookReady = false;
		if (cookReady && fish != null && logs != null)
			return true;
		getFishAndLogs();
		if (fish != null && logs != null)
			if (invSaleAfter() > Random.nextInt(23, 25)
					|| bossHp < random(30, 40) - (deaths < 2 ? 15 : 0)
					|| invCount(false, foods) > random(2, 5)
					|| !hasCoins((invContains(logs.logId) ? 0 : logs.cost)
							+ (invContains(fish.rawId) ? 0 : (fish.price(Random.nextInt(2, 5)
									+ (bossRoom == null ? 2 : 0)))))) {
				fish = null;
				logs = null;
			}
		return cookReady = fish != null && logs != null;
	}

	private boolean readyToPrestige() {
		return cProg > 0 && (forcePrestige || cProg >= maxFloor);
	}

	private Tile reflect(Tile start, Tile dest, double r) {
		if (dest == null)
			return null;
		final int sX = start.getX(), sY = start.getY(), eX = dest.getX(), eY = dest.getY();
		return new Tile(eX + (int) ((eX - sX) / r), eY + (int) ((eY - sY) / r), 0);
	}

	private Tile reflectMid(Tile curr, Tile dest) {
		final int cX = curr.getX(), cY = curr.getY(), dX = dest.getX(), dY = dest.getY();
		return new Tile(cX + (dX - cX) / Random.nextInt(2, 4), cY + (dY - cY) / Random.nextInt(2, 4), 0);
	}

	private final int[] ALTARS = { 51609, 52154, 52825, 53368, 53376, 53384, 53739, 55126, 55127, 55667, 55992, 55993 };
	private final int[] C_ALTARS = { 51609, 52154, 55992 };

	private boolean rechargePrayer() {
		if (targetRoom != null && targetRoom.hasAltar && (developer || prayerLevel > 36 || Option.QUICK_PRAY.enabled())) {
			if (developer) {
				final SceneObject altar = getObjInRoom(ALTARS);
				if (altar != null && !intMatch(altar.getId(), C_ALTARS)) {
					severe("Unknown altar " + altar.getId());
					waitForResponse();
				}
			}
			final int initialPrayer = prayerPercent();
			if (initialPrayer < Random.nextInt(85, 95)) {
				final SceneObject altar = getObjInRoom(ALTARS);
				if (altar != null) {
					secondaryStatus = "Recharging Prayer points from " + initialPrayer + "%";
					log(LP, false, secondaryStatus);
					setPrayersOff();
					while (prayerPercent() <= initialPrayer) {
						if (failSafe())
							return false;
						if (secondaryStatus.startsWith("Attempting to") || lastMessage.startsWith("The altar seems")) {
							targetRoom.hasAltar = false;
							break;
						}
						boolean toWait = false;
						if (Widgets.get(1188).validate()) {
							if (Widgets.get(1188, 1).click(true))
								break;
						} else if (Widgets.get(1186).validate())
							Widgets.get(1186, 3).click(true);
						else
							toWait = doObjAction(getObjInRoom(ALTARS), "Quick-Recharge");
						smartSleep(toWait, true);
					}
					secondaryStatus = "";
				}
			}
		}
		return true;
	}

	private void remove(ArrayList<Integer> list, int... ids) {
		for (final int id : ids)
			while (saving(list, id))
				list.remove((Integer) id);
	}

	private void restTo(int percent) {
		final WidgetChild restText = Widgets.get(750, 6);
		final String oldStatus = secondaryStatus;
		secondaryStatus = "Resting up to " + percent + "% energy";
		while (restText.validate() && parse(restText.getText()) < percent) {
			if (failBasic())
				return;
			if (inTrueCombat())
				return;
			if (isPoisoned())
				unPoison();
			else if (player().getAnimation() == -1) {
				final WidgetChild btn = Widgets.get(750, 2);
				if (btn.validate() && btn.interact("Rest"))
					Time.sleep(400, 800);
			}
			Time.sleep(100, 500);
		}
		secondaryStatus = oldStatus;
	}

	private boolean requirements(String... exceptions) {
		if ((!levelRequirement && !temporaryReduction())
				|| (Widgets.get(1186).validate() && Widgets.get(1186).getText().contains("level"))) {
			if (stringMatch(lastMessage, exceptions))
				return levelRequirement = true;
			levelRequirement = false;
			failReason = "Level requirements";
			return false;
		}
		if (unreachable) {
			if (!topUp())
				Time.sleep(300, 600);
			unreachable = false;
		} else {
			makeSpace(false);
			eatFood(foods, 40, 50);
		}
		return true;
	}

	private boolean riddable(Item item) {
		if (item == null)
			return false;
		final int itemId = item.getId();
		if (itemId < 1)
			return false;
		if ((fish != null && itemId == fish.foodId) || (logs != null && itemId == logs.logId))
			return false;
		if (itemId == arrows || itemId == secondaryWep)
			return false;
		if (itemId == ESSENCE)
			return !saving(bounds, ESSENCE) || invCount(true, COSMIC_RUNES) > 10;
		if (saving(saves, itemId))
			return false;
		if (intMatch(itemId, PUZZLE_ITEMS))
			return puzzleTimer != null;
		if (saving(bounds, itemId))
			return false;
		if (saving(sales, itemId))
			return Widgets.get(956).validate();
		if (intMatch(itemId, improvements))
			return false;
		final String name = getName(item);
		if (name.endsWith("(b)"))
			return false;
		return name.contains(" vile ") || !name.startsWith("Raw ");
	}

	private boolean ridItem(int itemId, String action) {
		if (itemId < 1)
			return false;
		final int iCount = invCount(itemId);
		if (iCount == 0)
			return true;
		for (int c = 0; c < 4; ++c) {
			final Item item = invItem(itemId);
			if (item == null || invCount(itemId) < iCount)
				break;
			if (doItemAction(item, action))
				for (int d = 0; d < 10; ++d) {
					if (invCount(itemId) < iCount)
						return true;
					Time.sleep(100, 200);
				}
			Time.sleep(50, 200);
		}
		return iCount > invCount(itemId);
	}

	private Tile safeCorner(Tile bossTile) {
		for (final SceneObject corner : getObjsInRoom(51762)) {
			if (corner == null)
				continue;
			final Tile c = corner.getLocation();
			final double cDist = Calculations.distance(c, myLoc());
			if (cDist > 3 && cDist < 15 && cDist < Calculations.distance(c, bossTile))
				if (Math.abs(bossTile.getX() - c.getX()) > 2 && Math.abs(bossTile.getX() - c.getX()) > 2)
					return c;
		}
		final SceneObject corner = SceneEntities.getNearest(51762);
		return corner != null ? corner.getLocation() : null;
	}

	private Tile safeGrave(Tile bossTile) {
		for (final SceneObject grave : getObjsInRoom(54443, 54449, 54457)) {
			if (grave == null)
				continue;
			final Tile g = grave.getLocation();
			if (Calculations.distance(g, bossTile) > 2 && distTo(g) < 7)
				return g;
		}
		final SceneObject grave = getObjInRoom(54443, 54449, 54457);
		return grave != null ? grave.getLocation() : null;
	}

	private Tile safePillar(Tile bossTile) {
		final double dist = Calculations.distance(bossTile, bossRoom.getCenter());
		if (dist > 0)
			for (final SceneObject pillar : getObjsInRoom(49265, 49266, 49267)) {
				if (pillar == null)
					continue;
				final Tile p = pillar.getLocation(), me = myLoc();
				final double bDist = Calculations.distance(p, bossTile);
				if (bDist > 5 && Calculations.distance(me, p) < (8 + dist)
						&& Calculations.distance(me, bossTile) < bDist)
					return p;
			}
		final SceneObject pillar = getObjInRoom(49265, 49266, 49267);
		return pillar != null ? pillar.getLocation() : null;
	}

	private boolean scrollToBottom(WidgetChild scroll) {
		if (scroll != null && scroll.validate()) {
			final WidgetChild down = scroll.getChild(5);
			if (down != null && down.validate()) {
				final Point sP = down.getCentralPoint();
				final WidgetChild bar = scroll.getChild(3);
				if (bar != null && bar.validate()) {
					final int h = bar.getHeight();
					while (bar.validate() && bar.getAbsoluteY() + h < sP.y - 25) {
						if (failCheck())
							break;
						Mouse.move(sP.x, sP.y - 25, 5, 17);
						Mouse.click(true);
						Time.sleep(200, 600);
					}
					return true;
				}
			}
		}
		return false;
	}

	private int targY(WidgetChild targ) {
		return targ.getAbsoluteY() + 400;
	}

	public boolean scroll(final WidgetChild targ, final WidgetChild scrollBar) {
		if (targ == null || scrollBar == null || !targ.validate() || scrollBar.getChildren().length != 6)
			return true;
		final int areaY = 26;
		final int areaHeight = 210;
		if (targY(targ) >= areaY && targY(targ) <= areaY + areaHeight - targ.getHeight())
			return true;
		final WidgetChild scrollBarArea = scrollBar.getChildren()[0];
		final int contentHeight = maxFloor * 10 + 30;
		int pos = (int) ((float) scrollBarArea.getHeight() / contentHeight * (targ.getRelativeY() + Random.nextInt(
				-areaHeight / 2, areaHeight / 2 - targ.getHeight())));
		if (pos < 0)
			pos = 0;
		else if (pos >= scrollBarArea.getHeight())
			pos = scrollBarArea.getHeight() - 1;
		click(scrollBarArea.getAbsoluteX() + Random.nextInt(0, scrollBarArea.getWidth()), scrollBarArea.getAbsoluteY()
				+ pos);
		Time.sleep(Random.nextInt(200, 400));
		while (targY(targ) < areaY || targY(targ) > areaY + areaHeight - targ.getHeight()) {
			final boolean scrollUp = targY(targ) < areaY;
			scrollBar.getChildren()[scrollUp ? 4 : 5].click(true);

			Time.sleep(Random.nextInt(100, 200));
		}
		return targY(targ) >= areaY && targY(targ) <= areaY + areaHeight - targ.getHeight();
	}

	private boolean scrollToFloor() {
		if (fNumber < 13)
			return true;
		final Widget floorWindow = Widgets.get(947);
		if (!floorWindow.validate())
			return false;
		final WidgetChild targ = floorWindow.getChild(607 + fNumber);
		final WidgetChild scroller = floorWindow.getChild(47);
		Widgets.scroll(targ, scroller);
		return targ.validate() && targ.click(true);
	}

	private boolean selectItem(int itemId) {
		Item selItem = Inventory.getSelectedItem();
		final Item iItem = getLastItem(itemId);
		if (selItem != null && selItem.getId() == itemId)
			return true;
		if (iItem == null || !doItemAction(iItem, "Use"))
			return false;
		Time.sleep(600, 800);
		for (int c = 0; c < 5; ++c) {
			if (Inventory.getSelectedItem() != null)
				break;
			Time.sleep(200, 300);
		}
		return (selItem = Inventory.getSelectedItem()) != null && selItem.getId() == itemId;
	}

	private boolean setActivatePrayer(Effect effect, boolean activate) {
		open(Tabs.PRAYER);
		final WidgetChild prayC = Widgets.get(271, 8).getChild(effect.getIndex());
		return Prayers.isActive(effect) != activate && prayC.interact(activate ? "Activate" : "Deactivate");
	}

	private boolean setBoss(String name, boolean appendTrue, int index) {
		status = "Killing " + (appendTrue ? "the " : "") + name + "!";
		if (bossName == null) {
			log(LP, false, status);
			if (index == 1)
				name = name.substring(0, name.indexOf(" "));
			else if (index == 2)
				name = name.substring(name.indexOf(" ") + 1, name.length());
			bossName = name;
			return true;
		}
		return false;
	}

	private void setMouseDefault() {
		Context.get().b(Random.nextInt(1400, 1600));
	}

	private void setMouseSpeed(double multiplier) {
		final int speed = (int) (multiplier * 1000);
		final int diff = (int) (speed / 10 * RANDOMIZED);
		Context.get().b(Random.nextInt(speed - diff, speed + diff));
	}

	private boolean setPrayer(boolean qPray, Style protect, boolean activate) {
		if ((prayerLevel < 34 || protect == null) && (!Option.QUICK_PRAY.enabled() || !qPray))
			return false;
		if (prayTimer != null && prayTimer.isRunning())
			return false;
		final boolean disable = !qPray && !activate && Prayer.isQuickOn();
		prayTimer = null;
		if (((qPray || activate) && Prayer.getPoints() < 3) || !Widgets.get(749).validate())
			return false;
		if (Option.QUICK_PRAY.enabled()) {
			if (qPray && combatStyle != primaryStyle)
				qPray = false;
			o: for (int c = 0; c < 5; ++c) {
				if (failCheck() || prayTimer != null)
					return false;
				if (Prayer.isQuickOn() == qPray || !Widgets.get(749).validate())
					break;
				if (Prayers.toggleQuickPrayers(qPray))
					for (int d = 0; d < 20; ++d) {
						if (Prayer.isQuickOn() == qPray)
							break o;
						if (lastMessage.contains("Prayer button next")) {
							Option.QUICK_PRAY.set(false);
							break o;
						}
						Time.sleep(150, 200);
					}
				Time.sleep(100, 200);
			}
		}
		if (disable && !Prayer.isQuickOn())
			return true;
		final Effect pray = protect(null, protect);
		if (pray != null && pray.getRequiredLevel() <= prayerLevel)
			while (Prayers.isActive(pray) != activate && Prayer.getPoints() > 0) {
				if (failCheck() || prayTimer != null)
					return false;
				if (setActivatePrayer(pray, activate))
					for (int c = 0; c < 16; ++c) {
						if (Prayers.isActive(pray) == activate)
							return true;
						Time.sleep(80, 120);
					}
				Time.sleep(100, 200);
			}
		return false;
	}

	private boolean setPrayersOff() {
		if (!Prayers.isEnabled() || (prayTimer != null && prayTimer.isRunning() && prayTimer.getRemaining() < 2000))
			return true;
		if (Option.QUICK_PRAY.enabled()) {
			final boolean second = !Prayer.isQuickOn();
			if (!Widgets.get(749, 2).click(true) && !Widgets.get(749, 2).click(true))
				return false;
			if (second) {
				Time.sleep(0, 100);
				if (!Widgets.get(749, 2).click(true) && !Widgets.get(749, 2).click(true))
					return false;
			}
		} else
			Prayer.deactivateAll();
		prayTimer = new Timer(Random.nextInt(1000, 2000));
		return true;
	}

	private boolean setRetaliateDefault() {
		return setRetaliate(!isRushing && !Option.SKIP.enabled);
	}

	private boolean setRetaliate(boolean retaliate) {
		if (isAutoRetaliateEnabled() != retaliate) {
			final Tile dest = getFlag(false);
			while (isAutoRetaliateEnabled() != retaliate) {
				if (failCheck())
					return false;
				setAutoRetaliate(retaliate);
				for (int c = 0; c < 10; ++c) {
					if (isAutoRetaliateEnabled() == retaliate)
						break;
					Time.sleep(100, 150);
				}
			}
			if (dest != null && targetRoom.contains(dest) && !dest.isOnScreen())
				walkToMap(dest, 1);
			updateFightMode();
			return true;
		}
		return false;
	}

	private boolean setTargetRoom(Room room) {
		if (room != null && !room.equals(targetRoom)) {
			targetRoom = room;
			room.setFlags();
			return true;
		}
		return false;
	}

	private boolean save(ArrayList<Integer> list, int... ids) {
		boolean updated = false;
		for (final int id : ids)
			if (id > 0 && !saving(list, id))
				updated = list.add(id);
		return updated;
	}

	private boolean saving(ArrayList<Integer> list, int id) {
		return list.contains(id);
	}

	private void setUserInput(boolean enabled) {
		// Environment.setUserInput(enabled ? enabledFlags : disabledFlags);
	}

	private boolean slayerCheck() {
		if (memberWorld)
			for (final Slayer monster : Slayer.values())
				if (getNpcInRoom(monster.id) != null && !monster.isSlayable()) {
					failReason = "Unkillable slayer monster";
					return false;
				}
		return true;
	}

	private boolean slayerRequirement(int id) {
		if (memberWorld)
			for (final Slayer monster : Slayer.values())
				if (id == monster.id)
					return monster.isSlayable();
		return true;
	}

	private void smartAction(String[] actions, Locatable... objs) {
		double dist = Double.MAX_VALUE;
		Locatable object = null;
		String action = null;
		final Tile me = myLoc();
		for (int I = 0; I < objs.length; ++I) {
			final Locatable o = objs[I];
			if (o == null)
				continue;
			final double tDist = Calculations.distance(me, o);
			if (tDist < dist) {
				dist = tDist;
				object = o;
				action = actions[I];
			}
		}
		if (object != null)
			if (action != null)
				smartSleep(doObjAction((SceneObject) object, action), true);
			else
				smartSleep(pickUpItem((GroundItem) object), false);
	}

	private boolean smartSleep(boolean stop, boolean deanimate) {
		if (stop) {
			waitToStop(deanimate);
			return true;
		}
		Time.sleep(400, 600);
		return false;
	}

	private void spaceContinue() {
		Keyboard.sendKey(' ');
		Time.sleep(200, 400);
	}

	private boolean staticDamage() {
		return player().isInCombat() && !inTrueCombat();
	}

	private static Tile stepAlong(Point m, Tile curr) {
		return m != null ? new Tile(curr.getX() + m.x, curr.getY() + m.y, 0) : null;
	}

	private ArrayList<Tile> stopNodes(Tile check) {
		final ArrayList<Tile> verticies = new ArrayList<Tile>();
		for (final Tile tile : getSurrounding(check)) {
			final Point m = alignment(check, tile);
			Tile current = check;
			while (isGoodTile(current = stepAlong(m, current)))
				for (final Tile t : getSurrounding(current))
					if (!isGoodTile(t)) {
						verticies.add(current);
						break;
					}
		}
		if (!verticies.isEmpty()) {
			final Tile doorTile = getBackDoor();
			if (doorTile != null) {
				final SceneObject door = SceneEntities.getAt(doorTile);
				if (door != null)
					for (final Tile t : tiles(door.getArea()))
						verticies.remove(t);
			}
			if (!puzzlePoints.contains(verticies.get(0)))
				puzzlePoints.addAll(verticies);
		}
		return verticies;
	}

	private void stopScript() {
		stopScript = true;
		stop();
	}

	private boolean straightLine(Tile curr, Tile dest) {
		if (curr.equals(dest))
			return true;
		final int cX = curr.getX(), cY = curr.getY(), dX = dest.getX(), dY = dest.getY();
		if (cX == dX || cY == dY || Math.abs((cX - dX) * 100 / (cY - dY)) == 100) {
			final Point m = alignment(curr, dest);
			while (isGoodTile(curr)) {
				curr = stepAlong(m, curr);
				if (curr.equals(dest))
					return true;
			}
		}
		return false;
	}

	private boolean straightLine(Tile curr, Tile midd, Tile dest) {
		if (curr == null || midd == null || dest == null)
			return false;
		final double m2Dest = Calculations.distance(midd, dest);
		if (m2Dest == 0)
			return true;
		final double toDest = Calculations.distance(curr, dest);
		final double toMidd = Calculations.distance(curr, midd);
		if (toDest > toMidd && toDest > m2Dest) {
			final int cmX = curr.getX() - midd.getX(), cmY = curr.getY() - midd.getY(), cdX = curr.getX() - dest.getX(), cdY = curr
					.getY() - dest.getY(), mdX = midd.getX() - dest.getX(), mdY = midd.getY() - dest.getY();
			if ((cmX == 0 && cdX == 0) || (cmY == 0 && cdY == 0)) {
				if (developer)
					log(null, false, "Straight line to the previous tile, trying another");
				return true;
			}
			if (cmX == 0 || cmY == 0 || cdX == 0 || cdY == 0 || mdX == 0 || mdY == 0)
				return false;
			if (Math.abs(cmX) == Math.abs(cmY) && Math.abs(mdX) == Math.abs(mdY) && Math.abs(cdX) == Math.abs(cdY)) {
				if (developer)
					log(null, false, "Straight diagonal to the previous tile, trying another");
				return true;
			}
		}
		return false;
	}

	private boolean stringMatch(String desired, String... compare) {
		if (compare != null && desired != null && !desired.isEmpty()) {
			desired = desired.toLowerCase();
			for (final String check : compare)
				if (check != null && !check.isEmpty() && desired.contains(check.toLowerCase()))
					return true;
		}
		return false;
	}

	private int styleCount() {
		int count = 0;
		for (final Style s : Style.values())
			if (s.enabled)
				++count;
		return count;
	}

	private boolean swapAlternative() {
		return swapStyle(true, Style.MELEE, Style.RANGE, Style.MAGIC);
	}

	private boolean swapStyle(Style... styles) {
		return swapStyle(false, styles);
	}

	private boolean swapStyle(boolean switching, Style... styles) {
		final String oldStatus = secondaryStatus;
		for (final Style style : styles) {
			if (style == null || !style.enabled)
				continue;
			if (combatStyle == style) {
				if (switching)
					continue;
				return false;
			}
			secondaryStatus = "Swapping combat style to " + style;
			int weapon;
			if (style != primaryStyle) {
				disRobe();
				weapon = secondaryWep;
			} else
				weapon = primaryWep;
			if (style == Style.MAGIC) {
				if (blastBox > 0 && !ridItem(blastBox, "Wield"))
					continue;
			} else if (style == Style.RANGE)
				if (arrows > 0 && !ridItem(arrows, "Wield"))
					continue;
			if (weapon > 0 && !ridItem(weapon, "Wield"))
				continue;
			currentWep = weapon;
			if (style == primaryStyle)
				attackMode = valueOf(defaultMode);
			else if (style == Style.MELEE) {
				if (backupMode != -1)
					attackMode = valueOf(backupMode);
				else if (Option.PURE.enabled())
					attackMode = 1;
			} else if (style == Style.RANGE)
				attackMode = 1;
			else if (style == Style.MAGIC)
				attackMode = 4;
			combatStyle = style;
			if (Option.PURE.enabled())
				updateFightMode();
			switchRing(style);
			secondaryStatus = oldStatus;
			return true;
		}
		secondaryStatus = oldStatus;
		return false;
	}

	private boolean teleBack() {
		if (rooms.size() == 1 || atTargetRoom())
			return true;
		secondaryStatus = "Teleporting back...";
		deathPath.clear();
		if (isDead()) {
			++deaths;
			while (player().getAnimation() == 836)
				Time.sleep(200, 300);
		}
		isDead = false;
		while (!atTargetRoom()) {
			if (failCheck())
				return false;
			if (telePortal())
				waitToStop(false);
			Time.sleep(300, 400);
		}
		setTargetRoom(getCurrentRoom());
		for (int c = 0; c < 10; ++c) {
			if (invContains(GGS))
				break;
			smartSleep(pickUpItem(getItemInRoom(targetRoom, GGS)), false);
		}
		isDead = false;
		secondaryStatus = "";
		return true;
	}

	private boolean teleHome(boolean drop) {
		boolean cast = false;
		unPoison();
		secondaryStatus = "Teleporting back to the start room";
		idleTimer = new Timer(0);
		while (roomNumber != 1) {
			if (failCheck())
				return false;
			if (drop) {
				if (!ridItem(GGS, "Drop"))
					continue;
			} else if (getItemInRoom(currentRoom, GGS) != null && reachable(nearItem, true)) {
				makeSpace(true);
				smartSleep(pickUpItem(nearItem), false);
				continue;
			} else if (mapOpen()) {
				Time.sleep(400, 1700);
				checkDungeonMap(false);
				waitToEat(true);
				continue;
			}
			if (!monsterBacktrack())
				return false;
			if (inTrueCombat())
				Time.sleep(500, 2000);
			if (cast && player().getAnimation() == -1 && !inRoom(startRoom))
				waitToAnimate();
			if ((!cast || player().getAnimation() == -1) && !inRoom(startRoom)
					&& castSpell(Dungeoneering.HOME_TELEPORT, "Cast")) {
				cast = true;
				waitToAnimate();
				getCurrentRoom();
				if (roomNumber == 1)
					break;
				Time.sleep(0, 500);
				open(Tabs.INVENTORY);
				getCurrentRoom();
				if (roomNumber == 1)
					break;
				waitToStop(true);
			}
			getCurrentRoom();
		}
		secondaryStatus = "";
		idleTimer = new Timer(0);
		setTargetRoom(startRoom);
		return true;
	}

	private boolean teleGatestone() {
		final Dungeoneering spell = Dungeoneering.GATESTONE_TELEPORT;
		if (gateRoom == null || (!castable(spell) && !castable(spell)))
			return false;
		final Room target = gateRoom;
		boolean casted = false;
		while (gateRoom != null) {
			if (failCheck())
				return false;
			if (!casted && castSpell(spell, "Cast"))
				for (int c = 0; c < 10; ++c) {
					if (player().getAnimation() == 13285) {
						casted = true;
						while (player().getAnimation() == 13285)
							Time.sleep(50, 500);
						break;
					}
					Time.sleep(100, 200);
				}
			if (!casted)
				Time.sleep(200, 600);
			else
				Time.sleep(10, 100);
		}
		for (int c = 0; c < 10; ++c) {
			if (inRoom(target))
				break;
			if (player().getAnimation() != -1)
				--c;
			Time.sleep(200, 300);
		}
		setTargetRoom(getCurrentRoom());
		setPrayersOff();
		return true;
	}

	private boolean teleGroupstone() {
		final Dungeoneering spell = Dungeoneering.GROUP_GATESTONE_TELEPORT;
		if (!castable(spell))
			return false;
		final Room teleTo = groupRoom;
		while (!castSpell(spell, "Cast")) {
			if (failCheck())
				return false;
			Time.sleep(200, 600);
		}
		Time.sleep(1500, 3000);
		while (player().getAnimation() != -1)
			Time.sleep(100, 200);
		for (int c = 0; c < 10; ++c) {
			if (inRoom(teleTo))
				break;
			Time.sleep(200, 300);
		}
		groupRoom = null;
		setTargetRoom(getCurrentRoom());
		return true;
	}

	private boolean teleHomeAndBack() {
		idleTimer = new Timer(0);
		failTimer.reset();
		if (castable(Dungeoneering.GROUP_GATESTONE_TELEPORT))
			for (int c = 0; c < 3; ++c) {
				teleGroupstone();
				final GroundItem ggs = getItemInRoom(targetRoom, GGS);
				if (ggs != null && reachable(ggs, false))
					return true;
				Time.sleep(400, 800);
			}
		if (gateRoom != null && getItemInRoom(targetRoom, GATESTONE) != null)
			for (int c = 0; c < 3; ++c) {
				teleGatestone();
				final GroundItem ggs = getItemInRoom(targetRoom, GGS);
				if (ggs != null && reachable(ggs, false))
					return true;
				Time.sleep(400, 800);
			}
		return teleHome(true) && teleBack();
	}

	private boolean prayerReady() {
		if (isBoss("Runebound behemoth")) {
			protection = Style.MAGIC;
			return true;
		}
		return false;
	}

	private boolean telePortal() {
		final SceneObject portal = SceneEntities.getNearest(GROUP_PORTAL);
		if (portal == null || !atStartRoom())
			return false;
		if (distTo(portal) < (moving() ? 2.5 : 4)) {
			if (doObjAction(portal, "Enter")) {
				if (prayerReady())
					setPrayer(false, protection, true);
				return true;
			}
			return false;
		}
		final Tile far = farthest(portal);
		if (distTo(far) > 3) {
			if (player().getAnimation() != -1 || !atStartRoom()) {
				getCurrentRoom();
				return false;
			}
			if (!moving()) {
				walkFast(far, true, 1);
				if (distTo(far) > random(6, 8))
					turnTo(far);
				waitToStart(false);
			}
		}
		while (moving() && !far.isOnScreen())
			Time.sleep(100, 200);
		return clickTile(far, "Enter");
	}

	private boolean temporaryReduction() {
		String text = lastMessage;
		if (Widgets.get(1188).validate())
			text = Widgets.get(1188).getText();
		if (text == null || text.isEmpty())
			return false;
		text = text.toLowerCase();
		int idx = text.indexOf("of ") + 3;
		if (idx == 2 || (!text.contains("strength") && !text.contains("magic")))
			return false;
		if (text.contains("of at least"))
			idx += 9;
		final String msg = text.substring(idx);
		int skill = -1;
		final int level = parse(msg.substring(0, msg.indexOf(" ")));
		if (level > 99)
			return false;
		if (level == -1) {
			if (developer)
				severe("Parse error: " + msg);
			return false;
		}
		if (text.contains("strength") && level(Skills.STRENGTH) >= level) {
			skill = Skills.STRENGTH;
			log(null, false, "Our Strength level has been lowered below " + level + " to " + Skills.getLevel(skill)
					+ ", allowing it to regenerate");
		} else if (text.contains("magic") && level(Skills.MAGIC) >= level) {
			skill = Skills.MAGIC;
			log(null, false, "Our Magic level has been lowered below " + level + " to " + Skills.getLevel(skill)
					+ ", allowing it to regenerate");
		}
		if (skill != -1) {
			int waitTime = level - Skills.getLevel(skill);
			if (waitTime < 0)
				return false;
			if (waitTime > Random.nextInt(3, 5))
				waitTime = 4;
			final int sleepyTime = Random.nextInt(waitTime * 50000, waitTime * 70000);
			idleTimer = new Timer(0);
			open(Tabs.STATS);
			while (idleTimer.getElapsed() < sleepyTime) {
				if (Skills.getLevel(skill) >= level)
					break;
				antiban();
				Time.sleep(500, 5000);
			}
			idleTimer = new Timer(0);
			failTimer.reset();
			if (puzzleTimer != null)
				puzzleTimer.reset();
			if (doorTimer != null)
				doorTimer = new Timer(0);
			lastMessage = "";
			levelRequirement = true;
			return true;
		}
		return false;
	}

	private void threadCustomPitch() {
		if (!developer || combatStyle == Style.MELEE)
			threadPitch(100);
		else {
			final int curr = Camera.getPitch();
			final int set = Random.nextInt(defaultPitch, 90);
			if (Math.abs(curr - set) > Random.nextInt(10, 25))
				threadPitch(defaultPitch);
		}
	}

	private void threadDefault() {
		threadTurn(0, 0);
		threadPitch(100);
	}

	private void threadDown() {
		final int downPitch = random(0, 30);
		if (Camera.getPitch() > downPitch + 9)
			submit(new ThreadPitch(downPitch, downPitch));
	}

	private void threadMid() {
		final int midPitch = random(20, 40);
		if (Camera.getPitch() > midPitch + 3)
			submit(new ThreadPitch(midPitch, midPitch));
	}

	private void threadPitch(int percent) {
		if (Math.abs(Camera.getPitch() - percent) > random(2, 4))
			submit(new ThreadPitch(percent, percent));
	}

	private void threadSwivel(int amount, int dev) {
		threadTurn(Camera.getYaw() + (Random.nextBoolean() ? 1 : -1) * amount, dev);
	}

	private void threadTurn(int angle, int dev) {
		submit(new ThreadAngle(Random.nextInt(angle - dev, angle + dev + 1)));
	}

	private void turnTo(Locatable loc) {
		if (loc != null)
			threadTurn(Camera.getMobileAngle(loc), Random.nextInt(0, 15));
	}

	private Tile[] tiles(Area a) {
		if (a == null)
			return new Tile[0];
		final int w = a.getBounds().width + 1, h = a.getBounds().height + 1;
		int c = 0;
		final Tile[] aTiles = new Tile[w * h];
		for (int i = a.getBounds().x; i < a.getBounds().x + w; ++i)
			for (int j = a.getBounds().y; j < a.getBounds().y + h; ++j)
				aTiles[c++] = new Tile(i, j, 0);
		return aTiles;
	}

	private String timeFormat(long millis) {
		return millis < 1 ? EMPTY_TIMER : millis < 86400000 ? Time.format(millis) : timeInDays(millis) + " days";
	}

	private double timeInDays(long millis) {
		final double days = Math.rint(millis * 10 / 86400000) / 10;
		return days > 9.9 ? Math.rint(millis / 86400000) : days;
	}

	private void getFishAndLogs() {
		final int theoreticalCook = theoreticalTier(Skills.COOKING);
		int cookTier = currTier(Skills.COOKING);
		int fireTier = currTier(Skills.FIREMAKING);
		final int hpTier = maxTier(Skills.CONSTITUTION);
		if (cookTier > 2 && cookTier > hpTier) {
			cookTier = hpTier;
			if (fireTier > hpTier)
				fireTier = hpTier;
		}
		if (fireTier < cookTier - 1) {
			if (cookTier > 3)
				--cookTier;
		} else
			fireTier = cookTier - (cookTier > 7 ? 3 : 1);
		if (cookTier > 3 && cookTier >= theoreticalCook - 1)
			;
		--cookTier;
		if (memberWorld)
			if (cookTier > 9)
				--cookTier;
		if (fireTier >= cookTier)
			fireTier = cookTier - 1;
		if (cookTier > 0 && fireTier > 0) {
			fish = Food.values()[cookTier - 1];
			logs = Logs.values()[fireTier - 1];
		} else {
			fish = null;
			logs = null;
		}
	}

	private Tile[] trapCorners() {
		final int tX = targetRoom.x, tY = targetRoom.y;
		final Tile t11 = new Tile(tX + 1, tY + 2, 0), t12 = new Tile(tX + 2, tY + 1, 0);
		final Tile t21 = new Tile(tX + 1, tY + 13, 0), t22 = new Tile(tX + 2, tY + 14, 0);
		final Tile t31 = new Tile(tX + 13, tY + 14, 0), t32 = new Tile(tX + 14, tY + 13, 0);
		final Tile t41 = new Tile(tX + 13, tY + 1, 0), t42 = new Tile(tX + 14, tY + 2, 0);
		return new Tile[] { t11, t12, t21, t22, t31, t32, t41, t42 };
	}

	private boolean training(int skill) {
		if (skill == Skills.PRAYER) {
			if (!Option.PRAYER.enabled()) {
				failReason = "Gains Prayer experience";
				skipDoorFound = true;
				return false;
			}
		} else if (skill == Skills.STRENGTH) {
			if (!Option.STRENGTH.enabled()) {
				failReason = "Gains Strength experience";
				skipDoorFound = true;
				return false;
			}
		} else if (skill == Skills.SUMMONING)
			if (!Option.SUMMONING.enabled()) {
				failReason = "Gains Summoning experience";
				skipDoorFound = true;
				return false;
			}
		return true;
	}

	private boolean topUp() {
		if (inDungeon && !bossFinished && invContains(foods)) {
			if (foodTimer != null && foodTimer.isRunning())
				if (startHp == -1 || Math.abs(health() - startHp) < 10)
					return false;
			open(Tabs.INVENTORY);
			final int healthLost = level(Skills.CONSTITUTION) * 10 - healthPoints();
			for (final Food food : Food.values()) {
				if (healthLost <= food.health)
					break;
				final Item[] items = invItems(food.foodId);
				if (items.length > 0) {
					startHp = health();
					if (interact(items[Random.nextInt(0, items.length)], "Eat")) {
						foodTimer = new Timer(Random.nextInt(1100, 1600));
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean unBacktrack() {
		if (currentRoom != null && targetRoom != null && !currentRoom.equals(targetRoom)) {
			if (inRoom(bossRoom)) {
				setTargetRoom(bossRoom);
				bossFight = true;
				return false;
			}
			if (open && currentRoom.contains(nearDoor))
				return false;
			if (status.contains("Teleporting") && inRoom(startRoom))
				return false;
			Tile backtrack = null;
			DoorType backtype = null;
			if (currentRoom.contains(targetRoom.entryDoor)) {
				backtrack = targetRoom.entryDoor.loc;
				backtype = targetRoom.entryDoor.type;
			}
			if (backtrack == null)
				for (final Door d : currentRoom.doors) {
					final Tile doorTile = targetRoom.nearestDoorTo(d.loc);
					if (doorTile != null && Calculations.distance(d.loc, doorTile) < 4) {
						backtrack = d.loc;
						backtype = d.type;
						break;
					}
				}
			if (backtrack == null) {
				final Tile back = getBackDoor(currentRoom);
				if (back != null) {
					final Tile nearest = targetRoom.nearestDoorTo(back);
					if (nearest != null && Calculations.distance(back, nearest) < 4) {
						backtrack = back;
						backtype = DoorType.STANDARD;
					}
				}
			}
			if (backtrack == null)
				return false;
			final String oldStatus = secondaryStatus;
			secondaryStatus = "Correcting an accidental backtrack";
			if (nearMonster != null)
				omNomNom();
			while (getCurrentRoom() != null && currentRoom.contains(backtrack)) {
				if (failCheck())
					return false;
				smartSleep(openDoor(SceneEntities.getAt(backtrack), backtype), true);
				if (unreachable)
					randomCamera();
			}
			unreachable = false;
			secondaryStatus = oldStatus;
			return true;
		}
		return false;
	}

	private boolean unPoison() {
		return isPoisoned() && invCacheContains(ANTIPOISON) && getGoodNpc(POISON_MONSTER) == null
				&& ridItem(ANTIPOISON, "Drink");
	}

	private boolean unStick() {
		if (moving() || currentRoom == null)
			return false;
		if (open && nearDoor != null && secondaryStatus.isEmpty() && distTo(nearDoor.loc) < 5)
			return false;
		Tile check1 = null, check2 = null;
		final Tile me = myLoc();
		if (nearMonster != null && nearMonster.validate()) {
			if (nearMonster.isInCombat())
				return false;
			check1 = nearMonster.getLocation();
			if (Calculations.distance(me, check1) == 1)
				return false;
		}
		if (nearItem != null) {
			check2 = nearItem.getLocation();
			if (Calculations.distance(me, check2) <= 1)
				return false;
		}
		final Door door = currentRoom.getNearestDoor();
		if (door != null && door.blocked)
			if (adjacentTo(SceneEntities.getAt(door.block))) {
				if (floor == Floor.FROZEN && adjacentTo(SceneEntities.getNearest(4321)))
					return walkTo(myLoc(), 3);
				if (isEdgeTile(myLoc()) || isEdgeTile(check1) || isEdgeTile(check2)) {
					secondaryStatus = "Unsticking from the wall!";
					if (walkToMap(reflectMid(myLoc(), currentRoom.getCenter()), 2) || walkToMap(myLoc(), 4)) {
						failTimer.reset();
						return true;
					}
				}
			}
		if (status.equals("Puzzle room: Sliding Statues")
				&& (secondaryStatus.startsWith("Clearing") || secondaryStatus.startsWith("Picking up"))) {
			NPC statue = getNpcInRoom(HEADING_STATUES);
			if (statue == null || distTo(statue) != 1)
				statue = getNpcInRoom(SLIDING_STATUES);
			if (statue != null && distTo(statue) == 1) {
				secondaryStatus = "Unsticking from the statue!";
				return walkTo(myLoc(), 3);
			}
		}
		return false;
	}

	private void updateLevels() {
		final int cb = player().getLevel();
		if (cb > 0 && cb < 137)
			combatLevel = cb;
		final int dung = level(Skills.DUNGEONEERING);
		if (dung > 0 && dung < 121) {
			dungLevel = dung;
			if (dungLevel == 120)
				levelGoal = 200;
			else if (levelGoal <= dungLevel)
				levelGoal = dungLevel + 1;
		}
	}

	@SuppressWarnings("incomplete-switch")
	private boolean updateFightMode() {
		if (!inDungeon || !settingsFinished || finish || defaultMode == -1)
			return false;
		if ((bossStage == 0 || !isBoss("Bulwark")) && !secondaryStatus.startsWith("Picking up"))
			equipFor(combatStyle);
		switch (combatStyle) {
		case MELEE:
			if (Option.PURE.enabled()) {
				if (Option.STYLE_SWAP.enabled() && (tempMode.index() == defenseMode || tempMode.index() == 3))
					tempMode = Melee.NONE;
				if (attackMode == defenseMode || attackMode == 3)
					attackMode = 1;
			}
			return setFightMode(!Option.STYLE_SWAP.enabled() || !tempMode.enabled() ? attackMode : tempMode.index());
		case RANGE:
			return setFightMode(Option.RANGE.enabled() ? defaultMode : 1);
		case MAGIC:
			if (!isAutocasting()) {
				if (autocast(combatSpell) || autocast(combatSpell))
					for (int c = 0; c < 15; ++c) {
						if (isAutocasting())
							break;
						Time.sleep(150, 200);
					}
				else if (!isCastable(combatSpell))
					outOfAmmo = true;
				return true;
			}
		}
		return false;
	}

	private void updateLocks() {
		final WidgetChild keyGroup = Widgets.get(945, 20);
		if (keyGroup.validate())
			for (final WidgetChild k : keyGroup.getChildren())
				if (save(keyStore, k.getChildId()) && developer)
					log(LR, false, "Key backup: " + k.getChildId());
		for (final Room r : rooms)
			for (final Door d : r.doors)
				if (!d.hasKey)
					for (final Integer k : keyStore)
						if (keyInLock(k.intValue(), d.lockId)) {
							d.hasKey = true;
							break;
						}
	}

	private boolean updateProgress() {
		boolean updated = false;
		final WidgetChild p = Widgets.get(939, 83);
		if (p != null && p.validate() && !p.getText().isEmpty()) {
			final int progress = parse(p.getText());
			if (progress > -1 && progress < 61) {
				cProg = progress;
				updated = true;
			}
			final WidgetChild f = Widgets.get(939, 106);
			if (f != null) {
				final int floor = parse(f.getText());
				if (floor > 0 && floor < 61)
					fNumber = floor;
			}
			final WidgetChild c = Widgets.get(939, 100);
			if (c != null) {
				final int comp = parse(c.getText());
				if (comp > 0 && comp < 7)
					aComplexity = comp;
			}
		}
		updateLevels();
		WidgetChild max = Widgets.get(947, 729);
		int newMax = -1;
		if (!partyMode) {
			if (max != null && max.validate() && max.getRelativeY() > 0)
				newMax = max.getRelativeY() / 10;
			else if (dungLevel > 0) {
				newMax = (dungLevel + 1) / 2;
				if (!memberWorld && newMax > 35)
					newMax = 35;
			}
		} else {
			if ((max == null || max.getRelativeY() < 1) && maxFloor < 1 && partyTabSelected() && partyFormed()) {
				final WidgetChild showFloors = Widgets.get(939, 108);
				if (showFloors != null && showFloors.validate())
					for (int c = 0; c < 5; c++) {
						if ((max = Widgets.get(947, 729)) != null && max.getRelativeY() > 0)
							break;
						if (showFloors.interact("Change-floor"))
							forSleep(Widgets.get(947), true);
						else
							Time.sleep(100, 300);
					}
			}
			if (max != null && max.getRelativeY() > 0) {
				newMax = max.getRelativeY() / 10;
				for (int i = 0; i < 4; i++) {
					max = Widgets.get(947, 241 + i * 122);
					if (max != null && max.getRelativeY() > 0) {
						final int m = max.getRelativeY() / 10;
						if (m < newMax)
							newMax = m;
					}
				}
			}
		}
		if (newMax > 0) {
			trueMax = newMax;
			if (combatLevel < 15 && !partyMode && trueMax > 11)
				maxFloor = 11;
			else if (combatLevel < 40 && !partyMode && trueMax > 17)
				maxFloor = 17;
			else if (freeVersion)
				maxFloor = 35;
			else
				maxFloor = newMax;
		}
		return updated;
	}

	private boolean useItem(int itemId, NPC npc) {
		final Item item = invItem(itemId);
		if (npc != null && item != null) {
			if (!npc.isOnScreen())
				walkToMap(npc, 1);
			if (selectItem(itemId)) {
				while (moving() && !npc.isOnScreen())
					Time.sleep(200, 400);
				return npc.isOnScreen() && interact(npc, "Use", getName(item) + " -> " + getName(npc));
			}
		}
		return false;
	}

	private boolean useItem(int itemId, int targetId) {
		if (selectItem(itemId)) {
			final Item target = invItem(targetId);
			return target != null && interact(target, "Use");
		}
		return false;
	}

	private boolean useItem(int itemId, SceneObject obj) {
		if (obj != null) {
			final Tile oTile = obj.getLocation();
			if (!oTile.isOnScreen())
				walkToMap(oTile, 1);
			if (selectItem(itemId)) {
				while (moving() || player().getAnimation() != -1)
					Time.sleep(200, 400);
				return interact(obj, "Use", getName(invItem(itemId)) + " -> " + getName(obj));
			}
		}
		return false;
	}

	private static String username() {
		if (username != null)
			return username;
		final WidgetChild user = Widgets.get(137, 53);
		if (user.validate()) {
			String text = user.getText();
			final int colIndex = text.indexOf("</col>");
			if (colIndex != -1)
				text = text.substring(colIndex + 6, text.length());
			return username = text.substring(0, text.indexOf("<")).replaceAll("\u00A0", " ");
		}
		return null;
	}

	private int valueOf(int i) {
		final int ret = i;
		return ret;
	}

	private Style styleOf(int idx) {
		for (final Style s : Style.values())
			if (s.idx == idx)
				return s;
		return null;
	}

	private void waitForDamage() {
		if (staticDamage()) {
			if (!topUp())
				Time.sleep(400, 600);
			Time.sleep(200, 400);
		}
	}

	private boolean waitForResponse() {
		if (!developer)
			return false;
		if (showChat)
			severe("Skipping response at: " + (!secondaryStatus.isEmpty() ? ": " + secondaryStatus : ""));
		else {
			final String oldStatus = secondaryStatus;
			secondaryStatus = "Waiting for your input";
			severe(secondaryStatus + (!oldStatus.isEmpty() ? ": " + oldStatus : ""));
			while (!showChat) {
				if (pauseScript() || !Game.isLoggedIn()) {
					secondaryStatus = oldStatus;
					return false;
				}
				antiban();
				antiban();
				antiban();
				Time.sleep(100, 1000);
			}
			secondaryStatus = oldStatus;
			if (puzzleTimer != null)
				puzzleTimer.reset();
			if (bossTimer != null)
				bossTimer = new Timer(0);
			idleTimer = new Timer(0);
			failTimer.reset();
		}
		return true;
	}

	private boolean waitForRoom(boolean check) {
		for (int c = 0; c < 6; ++c) {
			if (newRoom == null) {
				if (inRoom(targetRoom))
					if (!adjacentTo(SceneEntities.getAt(nearDoor.loc)))
						return false;
				if (waitToLoad())
					c = 0;
				else {
					if (check && !doorCheck())
						return true;
					if (getNewRoom())
						return checkRoom();
				}
			} else if (inRoom(newRoom))
				return true;
			else if (unreachable && inRoom(targetRoom)) {
				cancelAction();
				unreachable = false;
				return false;
			}
			Time.sleep(180, 300);
		}
		return false;
	}

	private boolean waitToAnimate() {
		for (int c = 0; c < 10; ++c) {
			if (player().getAnimation() != -1)
				return true;
			Time.sleep(100, 120);
		}
		return false;
	}

	private boolean waitToEat(boolean deanimate) {
		boolean ret = false;
		if (deanimate)
			if (!inTrueCombat())
				waitToAnimate();
			else
				deanimate = false;
		if (waitToStart(deanimate)) {
			idleTimer = new Timer(0);
			ret = true;
		}
		while (moving() || (deanimate && player().getAnimation() != -1)) {
			if (pauseScript() || isDead() || (currentRoom != null && !inRoom(currentRoom)))
				return false;
			if (!topUp())
				Time.sleep(150, 200);
		}
		return ret;
	}

	private boolean waitToLoad() {
		if (Game.getClientState() != Game.INDEX_MAP_LOADED) {
			while (Game.getClientState() != Game.INDEX_MAP_LOADED)
				Time.sleep(200, 300);
			return true;
		}
		return false;
	}

	private boolean waitToObject(Locatable l, boolean deanimate) {
		if (deanimate)
			if (!inTrueCombat())
				waitToAnimate();
			else
				deanimate = false;
		if (waitToStart(deanimate))
			idleTimer = new Timer(0);
		if (!headingTowards(l, 2.5))
			return false;
		for (int c = 0; c < 10; ++c) {
			if (!moving() && (!deanimate || player().getAnimation() == -1))
				break;
			if (pauseScript() || isDead() || (inDungeon && currentRoom != null && !inRoom(currentRoom)))
				return false;
			if (lastMessage.contains("level of") || lastMessage.contains("unable to"))
				return false;
			if (Game.getClientState() == Game.INDEX_MAP_LOADING || player().getInteracting() != null
					|| !headingTowards(l, 2.5))
				break;
			Time.sleep(150, 250);
		}
		return true;
	}

	private boolean waitToSlide(NPC npc) {
		final Tile start = npc.getLocation();
		boolean wasMoving = npc.isMoving();
		for (int c = 0; c < 15; ++c) {
			if (!npc.getLocation().equals(start))
				return true;
			final boolean curr = npc.isMoving();
			if (wasMoving != curr) {
				if (!curr)
					return true;
				wasMoving = true;
			}
			Time.sleep(150, 200);
		}
		return false;
	}

	private boolean waitToStart(boolean animate) {
		for (int c = 0; c < 10; ++c) {
			if (moving() || (animate && player().getAnimation() != -1))
				return true;
			Time.sleep(100, 150);
		}
		return false;
	}

	private boolean waitToStop(boolean deanimate) {
		boolean ret = false;
		if (deanimate)
			if (!inTrueCombat())
				waitToAnimate();
			else
				deanimate = false;
		if (waitToStart(deanimate)) {
			idleTimer = new Timer(0);
			ret = true;
		}
		int anim = -1;
		final boolean hasFlag = getFlag(false) != null;
		while (moving() || (deanimate && (anim = player().getAnimation()) != -1 && !intMatch(anim, 1666, 13051))) {
			if (pauseScript() || isDead() || (inDungeon && currentRoom != null && !inRoom(currentRoom)))
				return false;
			if (anim == -1) {
				final Tile flag = getFlag(false);
				if ((hasFlag && flag == null) || atLoc(flag))
					return true;
			}
			Time.sleep(100, 200);
		}
		return ret;
	}

	private void walkAdjacentTo(Tile dest, double maxDist) {
		final int walkDist = (int) (maxDist > 2 ? maxDist - 1 : maxDist);
		while (distTo(dest) > maxDist || atLoc(dest)) {
			if (failCheck())
				return;
			final Tile flag = getFlag(false);
			if (flag == null || !moving() || flag.equals(dest)) {
				walkFast(dest, true, walkDist);
				if (waitToStart(false)) {
					eatFood(foods, 15, 15);
					waitToEat(false);
				}
			}
		}
	}

	private boolean walkAround(Tile t, int x, int y, boolean isNpc) {
		if (t == null)
			return false;
		final Tile start = myLoc(), dest = new Tile(t.getX() + x, t.getY() + y, 0);
		if (start.equals(dest))
			return true;
		if (distTo(dest) > 1)
			for (final Tile test : getAdjacentTo(t)) {
				final boolean clearTile = isNpc ? getNpcAt(test) == null : SceneEntities.getAt(test) == null;
				if (clearTile && !test.equals(dest) && Calculations.distance(test, dest) < 2) {
					walkTo(test, 0);
					waitToEat(false);
					break;
				}
			}
		walkTo(dest, 0);
		waitToEat(false);
		if (atLoc(start)) {
			for (int d = 2; d < 5; ++d) {
				if (!atLoc(start))
					break;
				walkTo(myLoc(), d);
				waitToEat(false);
			}
			walkTo(dest, 0);
			waitToEat(false);
		}
		return atLoc(dest);
	}

	private boolean walkBlockedTile(Tile dest, int r) {
		if (dest == null || !targetRoom.contains(dest))
			return false;
		while (distTo(dest) > r) {
			if (doorTimer != null && doorTimer.getElapsed() > 30000 && !reachable(reachDoor(), true))
				return false;
			for (int f = 1; f < 4; ++f) {
				if (failCheck())
					return false;
				if (distTo(dest) <= r)
					return true;
				final Tile start = myLoc();
				if (walkTo(dest, r)
						&& (waitToEat(false) || waitToEat(false) || waitToEat(false) || waitToEat(false) || waitToEat(false))
						&& atLoc(start)) {
					walkTo(myLoc(), f);
					waitToEat(false);
				}
			}
		}
		return distTo(dest) <= r;
	}

	private boolean walkToDoor(int r) {
		if (nearDoor == null)
			getBestDoor();
		if (nearDoor == null || distTo(nearDoor.loc) < random(3, 5))
			return false;
		Tile walkTile = nearDoor.loc;
		final SceneObject door = SceneEntities.getAt(walkTile);
		if (door != null) {
			final Tile[] tiles = tiles(door.getArea());
			walkTile = tiles[Random.nextInt(0, tiles.length)];
		}
		return walkTo(walkTile, r);
	}

	private boolean walkToMap(Locatable loc, int r) {
		return walkFast(loc, false, r);
	}

	private boolean walkToScreen(Locatable loc) {
		if (loc == null)
			return false;
		final NPC targ = loc instanceof NPC ? (NPC) loc : null;
		for (int c = 0, r = random(13, 16); c < r; ++c) {
			if (isAttacking(targ))
				return true;
			final Tile tile = loc.getLocation();
			final Point p = tile.getNextViewportPoint();
			if (p.x != -1) {
				Mouse.move(p);
				if (isAttacking(targ) || Menu.select("Walk here")) {
					waitToStart(false);
					return true;
				}
				if (!moving())
					c += 3;
			} else if (!headingTowards(tile, 3))
				return walkTo(loc, 0);
		}
		return false;
	}

	private boolean walkTo(Locatable loc, int r) {
		if (walkFast(loc, true, r)) {
			waitToStart(false);
			return true;
		}
		return false;
	}

	private boolean walkHop(Locatable loc, int r) {
		if (loc != null) {
			Tile t = loc.getLocation();
			if (r > 0) {
				t = new Tile(t.getX() + Random.nextInt(-r, r + 1), t.getY() + Random.nextInt(-r, r + 1), 0);
				if (atLoc(t))
					return false;
			}
			Point p = t.getMapPoint();
			if (p.x == -1) {
				t = Walking.getClosestOnMap(t);
				p = t.getMapPoint();
			}
			if (p.x != -1) {
				Mouse.hop(p.x, p.y);
				Mouse.click(true);
				return true;
			}
		}
		return false;
	}

	private boolean walkFast(Locatable loc, boolean allowScreen, int r) {
		if (loc != null) {
			Tile t = loc.getLocation();
			if (!secondaryStatus.startsWith("Unsticking")) {
				final Tile flag = getFlag(false);
				if (flag != null && !moving() && Calculations.distance(t, flag) <= r)
					return !unStick();
			}
			if (r > 0)
				t = new Tile(t.getX() + Random.nextInt(-r, r + 1), t.getY() + Random.nextInt(-r, r + 1), 0);
			if (atLoc(t))
				return false;
			if (allowScreen && t.isOnScreen() && clickTile(t, "Walk here"))
				return true;
			Point p = t.getMapPoint();
			if (p.x == -1) {
				t = Walking.getClosestOnMap(t);
				p = t.getMapPoint();
			}
			if (p.x != -1)
				for (int c = 0; c < 5; ++c) {
					Mouse.move(p);
					p = t.getMapPoint();
					if (p.x != -1) {
						if (loc instanceof NPC && interactingWith((NPC) loc))
							return true;
						final Tile flag = getFlag(true);
						if (flag == null || Calculations.distance(t, flag) > r + 1) {
							click(p.x, p.y);
							return true;
						}
						return false;
					}
					p = t.getMapPoint();
					if (p.x == -1)
						break;
				}
		}
		return false;
	}

	private enum Floor {
		FROZEN(0, "Frozen", 145), THAWED(-2, null, -1), ABANDONED(1, "Abandoned", 209), FURNISHED(2, "Furnished", 273), OCCULT(
				3, "Occult", 3676), WARPED(4, "", 5467), ALL(-3, "Outside", 0), MEMBERS(-4, null, -1);

		private final int diff, index;
		private final String name;

		Floor(int index, String name, int differential) {
			this.index = index;
			this.name = name;
			this.diff = differential;
		}
	}

	private enum Food {
		HEIM_CRAB(1, 20, 200), RED0EYE(2, 50, 550), DUSK_EEL(3, 70, 840), GIANT_FLATFISH(4, 100, 1400), SHORT0FINNED_EEL(
				5, 120, 1920), WEB_SNIPPER(6, 150, 2700), BOULDABASS(7, 170, 3400), SALVE_EEL(8, 200, 4500), BLUE_CRAB(
				9, 220, 5500), CAVE_MORAY(10, 250, 7500);

		private final int cost, foodId, rawId, health;
		private final String name;

		Food(int tier, int health, int cost) {
			this.health = health;
			this.cost = cost;
			this.foodId = 18157 + 2 * tier;
			this.rawId = foodId - 362;
			this.name = FOOD_NAMES[tier - 1];
		}

		private int price(int toBuy) {
			return this.cost * toBuy;
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	private enum Logs {
		TANGLE_GUM(1, "Tangle gum", 225), SEEPING_ELM(2, "Seeping elm", 600), BLOOD_SPINDLE(3, "Blood spindle", 1650), UTUKU(
				4, "Utuku", 3750), SPINEBEAM(5, "Spinebeam", 7875), BOVISTRANGLER(6, "Bovistrangler", 12500), THIGAT(7,
				"Thigat", 22500), CORPSETHORN(8, "Corpsethorn", 35000), ENTGALLOW(9, "Entgallow", 60000), GRAVE_CREEPER(
				10, "Grave creeper", 117500);

		private final int[] treeIds;
		private final int cost, logId;
		private final String name;

		Logs(int tier, String name, int cost) {
			this.cost = cost;
			this.logId = 17680 + 2 * tier;
			this.name = name;
			this.treeIds = new int[5];
			for (int I = 0; I < 3; ++I)
				treeIds[I] = 49703 + tier * 2 + I * 20;
			treeIds[3] = 51662 + tier;
			treeIds[4] = 53750 + tier;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private enum Monster {
		// All floors
		FORGOTTEN_MAGE("Forgotten mage", Range.RAPID, Melee.SLASH, Melee.STAB, Floor.ALL, Style.MAGIC), FORGOTTEN_RANGER(
				"Forgotten ranger", Melee.SLASH, Melee.STAB, Melee.NONE, Floor.ALL, Style.RANGE), FORGOTTEN_WARRIOR0CHAIN(
				null, Mage.ALL, Melee.STAB, Melee.SLASH, Floor.ALL, Style.MELEE), FORGOTTEN_WARRIOR0PLATE(null,
				Mage.ALL, Melee.CRUSH, Melee.SLASH, Floor.ALL, Style.MELEE), GIANT_RAT("Giant rat", Melee.STAB,
				Range.RAPID, Melee.SLASH, Floor.ALL, Style.MELEE), MYSTERIOUS_SHADE(null, Melee.SLASH, Mage.ALL,
				Melee.NONE, Floor.ALL, Style.RANGE_MAGIC),

		// Puzzles
		GHOST("Ghost", Melee.CRUSH, Melee.STAB, Range.RAPID, Floor.ALL, Style.MELEE_MAGIC), MERCENARY_LEADER(
				"Mercenary leader", Range.RAPID, Melee.SLASH, Melee.STAB, Floor.ALL, Style.MAGIC), RAMOKEE_BLOODRAGER(
				"Ramokee bloodrager", Mage.ALL, Melee.STAB, Melee.SLASH, Floor.THAWED, Style.SUMMON), RAMOKEE_DEATHSLINGER(
				"Ramokee deathslinger", Melee.STAB, Melee.SLASH, Range.RAPID, Floor.THAWED, Style.SUMMON), RAMOKEE_STORMBRINGER(
				"Ramokee stormbringer", Range.RAPID, Melee.SLASH, Melee.STAB, Floor.THAWED, Style.SUMMON), RAMOKEE_SKINWEAVER(
				"Ramokee skinweaver", Melee.STAB, Melee.SLASH, Range.RAPID, Floor.THAWED, Style.SUMMON),

		// All except Frozen
		BAT("Bat", Melee.STAB, Range.RAPID, Melee.NONE, Floor.THAWED, Style.MELEE), DUNGEON_SPIDER("Dungeon spider",
				Melee.CRUSH, Melee.SLASH, Mage.FIRE, Floor.THAWED, Style.MELEE), GIANT_BAT("Giant bat", Melee.STAB,
				Range.RAPID, Melee.NONE, Floor.THAWED, Style.MELEE), SKELETON0MELEE(null, Mage.ALL, Melee.CRUSH,
				Melee.SLASH, Floor.THAWED, Style.MELEE), SKELETON0RANGE(null, Melee.CRUSH, Melee.SLASH, Mage.ALL,
				Floor.THAWED, Style.RANGE), SKELETON0MAGIC(null, Range.RAPID, Melee.CRUSH, Melee.STAB, Floor.THAWED,
				Style.MAGIC), ZOMBIE0MELEE(null, Melee.SLASH, Mage.FIRE, Melee.STAB, Floor.THAWED, Style.MELEE), ZOMBIE0RANGE(
				null, Melee.SLASH, Mage.FIRE, Melee.STAB, Floor.THAWED, Style.RANGE),

		// Slayer
		CRAWLING_HAND("Crawling hand", Melee.CRUSH, Melee.SLASH, Mage.FIRE, Floor.THAWED, Style.MELEE), CAVE_CRAWLER(
				"Cave crawler", Melee.CRUSH, Melee.SLASH, Melee.NONE, Floor.THAWED, Style.MELEE), CAVE_SLIME(
				"Cave slime", Mage.ALL, Melee.CRUSH, Melee.SLASH, Floor.THAWED, Style.MELEE), PYREFIEND("Pyrefiend",
				Mage.WATER, Melee.STAB, Melee.SLASH, Floor.THAWED, Style.MELEE), NIGHT_SPIDER("Night spider",
				Melee.CRUSH, Mage.FIRE, Melee.SLASH, Floor.THAWED, Style.MELEE), JELLY("Jelly", Melee.SLASH,
				Melee.CRUSH, Mage.ALL, Floor.THAWED, Style.MELEE), SPIRITUAL_GUARDIAN("Spiritual guardian", Mage.ALL,
				Melee.CRUSH, Melee.NONE, Floor.THAWED, Style.MELEE), SEEKER("Seeker", Range.RAPID, Melee.STAB,
				Melee.CRUSH, Floor.THAWED, Style.MAGIC), NECHRYAEL("Nechryael", Mage.ALL, Melee.SLASH, Range.RAPID,
				Floor.THAWED, Style.MELEE), EDIMMU("Edimmu", Range.RAPID, Melee.STAB, Melee.SLASH, Floor.THAWED,
				Style.MAGIC), SOULGAZER("Soulgazer", Range.RAPID, Melee.STAB, Melee.CRUSH, Floor.THAWED, Style.MAGIC),

		// Frozen
		FROST_DRAGON("Frost dragon", Melee.STAB, Range.RAPID, Mage.FIRE, Floor.FROZEN, Style.MAGIC), HYDRA("Hydra",
				Melee.SLASH, Melee.STAB, Range.RAPID, Floor.FROZEN, Style.RANGE), ICEFIEND("Icefiend", Melee.STAB,
				Melee.CRUSH, Range.RAPID, Floor.FROZEN, Style.RANGE_MAGIC), ICE_ELEMENTAL("Ice elemental", Melee.CRUSH,
				Melee.STAB, Mage.FIRE, Floor.FROZEN, null), ICE_GIANT("Ice giant", Mage.FIRE, Melee.CRUSH, Range.RAPID,
				Floor.FROZEN, Style.MELEE), ICE_SPIDER("Ice spider", Mage.FIRE, Melee.CRUSH, Melee.SLASH, Floor.FROZEN,
				Style.MELEE), ICE_TROLL("Ice troll", Mage.FIRE, Melee.CRUSH, Melee.SLASH, Floor.FROZEN, Style.MELEE), ICE_WARRIOR(
				"Ice warrior", Mage.FIRE, Melee.CRUSH, Melee.STAB, Floor.FROZEN, Style.MELEE), THROWER_TROLL(
				"Thrower troll", Melee.STAB, Melee.SLASH, Range.RAPID, Floor.FROZEN, Style.RANGE),

		// Abandoned
		EARTH_WARRIOR("Earth warrior", Mage.WIND, Melee.CRUSH, Melee.SLASH, Floor.ABANDONED, Style.MELEE), GIANT_SKELETON(
				"Giant skeleton", Mage.FIRE, Melee.CRUSH, Melee.SLASH, Floor.ABANDONED, Style.MELEE), GREEN_DRAGON(
				"Green dragon", Melee.STAB, Range.RAPID, Mage.FIRE, Floor.ABANDONED, Style.MAGIC), HILL_GIANT(
				"Hill giant", Melee.STAB, Melee.SLASH, Range.RAPID, Floor.ABANDONED, Style.MELEE), HOBGOBLIN(
				"Hobgoblin", Melee.CRUSH, Melee.STAB, Mage.WATER, Floor.ABANDONED, Style.MELEE), ANIMATED_PICKAXE(
				"Animated pickaxe", Mage.ALL, Melee.SLASH, Melee.CRUSH, Floor.ABANDONED, Style.MELEE),

		// Furnished
		BRUTE("Brute", Mage.ALL, Melee.STAB, Melee.CRUSH, Floor.FURNISHED, Style.MELEE), GUARD_DOG("Guard dog",
				Melee.STAB, Range.RAPID, Melee.SLASH, Floor.FURNISHED, Style.MELEE), IRON_DRAGON("Iron dragon",
				Melee.STAB, Range.RAPID, Mage.FIRE, Floor.FURNISHED, Style.MAGIC),

		// Occult
		FIRE_GIANT("Fire giant", Melee.STAB, Mage.WATER, Melee.CRUSH, Floor.OCCULT, Style.MELEE), HELLHOUND(
				"Hellhound", Melee.STAB, Range.RAPID, Melee.SLASH, Floor.OCCULT, Style.MELEE), NECROMANCER(
				"Necromancer", Range.RAPID, Melee.SLASH, Melee.STAB, Floor.OCCULT, Style.MAGIC), RED_DRAGON(
				"Red dragon", Melee.STAB, Range.RAPID, Mage.FIRE, Floor.OCCULT, Style.MAGIC),

		// Warped
		ANIMATED_PICKAXE_WARPED("Animated pickaxe", Mage.ALL, Melee.SLASH, Melee.CRUSH, Floor.ABANDONED, Style.MELEE), ANKOU(
				"Ankou", Mage.ALL, Melee.STAB, Melee.CRUSH, Floor.WARPED, Style.MELEE), BRUTE_WARPED("Brute", Mage.ALL,
				Melee.STAB, Melee.CRUSH, Floor.WARPED, Style.MELEE), BLACK_DRAGON("Black dragon", Melee.STAB,
				Range.RAPID, Mage.FIRE, Floor.WARPED, Style.MAGIC), GUARD_DOG_WARPED("Guard dog", Melee.STAB,
				Range.RAPID, Melee.SLASH, Floor.WARPED, Style.MELEE), REBORN_MAGE("Reborn mage", Range.RAPID,
				Mage.WATER, Melee.CRUSH, Floor.WARPED, Style.MAGIC), REBORN_WARRIOR("Reborn warrior", Mage.ALL,
				Melee.CRUSH, Melee.SLASH, Floor.WARPED, Style.MELEE),

		// Members
		ANIMATED_BOOK("Animated book", Range.RAPID, Melee.SLASH, Melee.NONE, Floor.MEMBERS, Style.MAGIC), BLACK_DEMON(
				"Black demon", Mage.ALL, Melee.STAB, Range.RAPID, Floor.MEMBERS, Style.MELEE_MAGIC), GREATER_DEMON(
				"Greater demon", Mage.ALL, Range.RAPID, Melee.STAB, Floor.MEMBERS, Style.MAGIC), LESSER_DEMON(
				"Lesser demon", Mage.ALL, Range.RAPID, Melee.STAB, Floor.MEMBERS, Style.MAGIC);

		private final Floor floor;
		private final String name;
		private final Style combatStyle;
		private final Combat[] weaknesses;

		Monster(String name, Combat w1, Combat w2, Combat w3, Floor floor, Style style) {
			this.name = name;
			this.weaknesses = new Combat[] { w1, w2, w3 };
			this.floor = floor; 
			this.combatStyle = style;
//Cake
		}
	}

	private enum Size {
		SMALL(4, "Small", 4), MEDIUM(4, "Medium", 8), LARGE(8, "Large", 8);

		private final int width, height;
		private final String name;

		Size(int width, String name, int height) {
			this.width = width;
			this.name = name;
			this.height = height;
		}
	}

	private enum Slayer {
		CRAWLING_HAND(5, 10694), CAVE_CRAWLER(10, 10695), CAVE_SLIME(17, 10696), PYREFIEND(30, 10697), NIGHT_SPIDER(41,
				10698), JELLY(52, 10699), SPIRITUAL_GUARDIAN(63, 10700), SEEKER(71, 10701), NECHRYAEL(80, 10702), EDIMMU(
				90, 10704), SOULGAZER(99, 10705);

		private final int id;
		private final int level;

		Slayer(int level, int id) {
			this.id = id;
			this.level = level;
		}

		private boolean isSlayable() {
			return level <= slayerLevel;
		}
	}

	private enum Style {
		MELEE(0, "Melee", false), RANGE(1, "Range", false), MAGIC(2, "Magic", false), SUMMON(3, "Summoning", false), MELEE_MAGIC(
				-1, null, false), RANGE_MAGIC(-1, null, false);

		private final int idx;
		private final String name;
		private boolean enabled, temporary;

		Style(int index, String name, boolean enable) {
			this.idx = index;
			this.enabled = enable;
			this.name = name;
			this.temporary = false;
		}

		private void set(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	private enum Mode {
		STAB(Melee.STAB), LUNGE(Melee.STAB), STAB_BLOCK(Melee.STAB), CHOP(Melee.SLASH), SLASH(Melee.SLASH), SLASH_BLOCK(
				Melee.SLASH), POUND(Melee.CRUSH), SMASH(Melee.CRUSH), CRUSH_BLOCK(Melee.CRUSH), LUNGE_CONTROLLED(
				Melee.STAB), SWIPE(Melee.SLASH), POUND_CONTROLLED(Melee.CRUSH);

		final Combat mode;

		Mode(Combat mode) {
			this.mode = mode;
		}
	}

	private enum Weapon {
		DAGGER("dagger", Skills.ATTACK, Mode.STAB, Mode.LUNGE, Mode.SLASH, Mode.STAB_BLOCK, false, 3), RAPIER("rapier",
				Skills.ATTACK, Mode.STAB, Mode.LUNGE, Mode.SLASH, Mode.STAB_BLOCK, false, 3), LONGSWORD("longsword",
				Skills.ATTACK, Mode.CHOP, Mode.SLASH, Mode.LUNGE_CONTROLLED, Mode.SLASH_BLOCK, false, 2), SPEAR(
				"spear", Skills.ATTACK, Mode.LUNGE_CONTROLLED, Mode.SWIPE, Mode.POUND_CONTROLLED, Mode.STAB_BLOCK,
				true, 3), MAUL("maul", Skills.STRENGTH, Mode.POUND, Mode.SMASH, Mode.CRUSH_BLOCK, null, true, 2), WARHAMMER(
				"warhammer", Skills.ATTACK, Mode.POUND, Mode.SMASH, Mode.CRUSH_BLOCK, null, false, 2), BATTLEAXE(
				"battleaxe", Skills.ATTACK, Mode.CHOP, Mode.SLASH, Mode.SMASH, Mode.SLASH_BLOCK, false, 3), SWORD(
				"sword", Skills.ATTACK, Mode.CHOP, Mode.SLASH, Mode.SMASH, Mode.SLASH_BLOCK, true, 3), SHORTBOW(
				"shortbow", Skills.RANGE, null, null, null, null, true, 2), LONGBOW("longbow", Skills.RANGE, null,
				null, null, null, true, 2), STAFF("staff", Skills.MAGIC, null, null, null, null, false, 3);

		private final boolean isTwoHanded;
		private final int skill;
		private final int defensive;
		private final String name;
		private final Mode[] modes;

		Weapon(String name, int skill, Mode m1, Mode m2, Mode m3, Mode m4, boolean twoHanded, int defense) {
			this.name = name;
			this.skill = skill;
			this.modes = new Mode[] { m1, m2, m3, m4 };
			this.isTwoHanded = twoHanded;
			this.defensive = defense;
		}

		private Combat getMode(int idx) {
			return idx != -1 && idx < 4 && modes[idx] != null ? modes[idx].mode : null;
		}

		private int indexOf(Melee style) {
			for (int I = 0; I < 4; ++I) {
				final Mode m = modes[I];
				if (m != null && m.mode == style)
					return I;
			}
			return -1;
		}
	}

	private interface Combat {
		public boolean enabled();

		public int index();

		public void set(int idx);

		public Style style();
	}

	private enum Melee implements Combat {
		SLASH(-1), CRUSH(-1), STAB(-1), DEFENSE(3), NONE(-1);

		private final Style style;
		private int index;

		Melee(int idx) {
			this.index = idx;
			this.style = Style.MELEE;
		}

		@Override
		public boolean enabled() {
			return index != -1;
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void set(int idx) {
			this.index = idx;
		}

		@Override
		public Style style() {
			return style;
		}
	}

	private enum Range implements Combat {
		ACCURATE(0), RAPID(1), LONG_RANGE(2), DEFENSE(2);

		private final Style style;
		private int index;

		Range(int idx) {
			this.index = idx;
			this.style = Style.RANGE;
		}

		@Override
		public boolean enabled() {
			return index != -1;
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void set(int idx) {
			this.index = idx;
		}

		@Override
		public Style style() {
			return style;
		}
	}

	private enum Mage implements Combat {
		WIND, WATER, EARTH, FIRE, TELE, ALL;

		private final Style style;
		private int index;

		Mage() {
			this.index = -1;
			this.style = Style.MAGIC;
		}

		@Override
		public boolean enabled() {
			return index != -1;
		}

		@Override
		public int index() {
			return index;
		}

		@Override
		public void set(int idx) {
			this.index = idx;
		}

		@Override
		public Style style() {
			return style;
		}
	}

	private static enum Dungeoneering {
		WIND_STRIKE(25, 1, 65), CONFUSE(26, 3, 66), WATER_STRIKE(27, 5, 67), EARTH_STRIKE(28, 9, 69), WEAKEN(29, 11, 70), FIRE_STRIKE(
				30, 13, 71), WIND_BOLT(32, 17, 73), CURSE(33, 19, 74), BIND(34, 20, 369), WATER_BOLT(36, 23, 76), EARTH_BOLT(
				37, 29, 79), FIRE_BOLT(41, 35, 82), WIND_BLAST(42, 41, 85), WATER_BLAST(43, 47, 88), SNARE(44, 50, 370), EARTH_BLAST(
				45, 53, 90), FIRE_BLAST(47, 59, 94), WIND_WAVE(48, 62, 96), WATER_WAVE(49, 65, 98), VULNERABILITY(50,
				66, 106), MONSTER_EXAMINE(51, 66, 627), CURE_OTHER(52, 68, 609), EARTH_WAVE(54, 70, 101), CURE_ME(55,
				71, 612), ENFEEBLE(56, 73, 107), CURE_GROUP(57, 74, 615), FIRE_WAVE(58, 75, 102), ENTANGLE(59, 79, 371), STUN(
				60, 80, 108), WIND_SURGE(61, 81, 815), WATER_SURGE(62, 85, 816), EARTH_SURGE(63, 90, 817), VENGENCE_OTHER(
				64, 93, 611), VENGENCE(65, 94, 614), VENGENCE_GROUP(66, 95, 5647), FIRE_SURGE(67, 95, 818),

		BONES_TO_BANANAS(31, 15, 72), LOW_LEVEL_ALCHEMY(35, 21, 75), HIGH_LEVEL_ALCHEMY(46, 55, 91),

		HUMIDIFY(53, 68, 628),

		HOME_TELEPORT(24, 0, -1), CREATE_GATESTONE(38, 32, 3059), GATESTONE_TELEPORT(39, 32, 3058), GROUP_GATESTONE_TELEPORT(
				40, 64, 3058);

		private final int componentId;
		private final int level;
		private final int textureId;

		private Dungeoneering(int componentId, int level, int textureId) {
			this.componentId = componentId;
			this.textureId = textureId;
			this.level = level;
		}
	}

	private boolean failBasic() {
		if (idleTimer.getElapsed() > 90000) {
			idleTimer = new Timer(0);
			return true;
		}
		if ((welcomeBack || Game.getClientState() != Game.INDEX_MAP_LOADED)
				&& !secondaryStatus.startsWith("Recovering"))
			return failLogin();
		pauseScript();
		getCurrentRoom();
		return stopScript;
	}

	private boolean failCheck() {
		if (failBasic()) {
			System.out.println("failbasic");
			return true;
		}
		if (puzzleTimer != null) {
			if (!puzzleTimer.isRunning())
				return true;
		} else if (!failTimer.isRunning())
			return true;
		if (outOfAmmo && !secondaryStatus.equals("Out of ammo!") && failAmmo())
			return true;
		return isDead() || !inDungeon();
	}

	private boolean failAmmo() {
		if (exit)
			return true;
		final String oldStatus = secondaryStatus;
		secondaryStatus = "Out of ammo!";
		if (combatStyle == Style.RANGE) {
			if (isBoss("Necrolord", "Sagittare") && !Style.MAGIC.enabled) {
				severe("Damn! Out of arrows and no viable backup combat style remains to kill this boss.");
				++controlledAborts;
				return exit = true;
			}
			if (invCount(true, arrows) > random(3, 7))
				ridItem(arrows, "Wield");
			else {
				if (secondaryWep > 0)
					log(LR, false, "Out of arrows! Wielding a backup weapon.");
				if (!swapAlternative()) {
					if (getItemArrow() != null) {
						outOfAmmo = false;
						while (getItemArrow() != null) {
							if (failCheck() || isDead())
								return false;
							smartSleep(pickUpItem(nearItem), false);
						}
						if (invCount(true, arrows) > random(3, 7)) {
							ridItem(arrows, "Wield");
							secondaryStatus = oldStatus;
							return false;
						}
					}
					secondaryStatus = "";
					severe("Unable to restock on arrows or equip a backup weapon, trying a new floor");
					++controlledAborts;
					return exit = true;
				}
				Style.RANGE.set(false);
			}
		} else if (combatStyle == Style.MAGIC) {
			outOfAmmo = !relog && !equipFor(Style.MAGIC);
			if (outOfAmmo) {
				log(LR, false, "Out of Runes! Wielding our backup weapon.");
				if (!swapAlternative()) {
					severe("Unabled to equip a backup weapon, trying a new floor");
					++controlledAborts;
					return exit = true;
				}
				Style.MAGIC.set(false);
			}
		}
		secondaryStatus = oldStatus;
		outOfAmmo = false;
		return false;
	}

	private boolean failLogin() {
		if (welcomeBack || Game.getClientState() != Game.INDEX_MAP_LOADING) {
			welcomeBack = false;
			if (!settingsFinished)
				return false;
			final Room loggedRoom = targetRoom;
			if (!welcomeBack && !status.startsWith("Logging out"))
				log(LR, false, "Oops.. We got logged out somehow :S");
			if (!login())
				return true;
			nearMonster = null;
			nearItem = null;
			if (doorTimer != null)
				doorTimer = new Timer(0);
			idleTimer = new Timer(0);
			failWidgets();
			if (inDungeon && !finish && currentRoom != null && Game.isLoggedIn()) {
				welcomeBack = false;
				while (!inRoom(getCurrentRoom())) {
					if (idleTimer.getElapsed() > 30000 || !inDungeon())
						return true;
					Time.sleep(100, 200);
				}
				if (exit || finish)
					return false;
				final String oldStatus = secondaryStatus;
				secondaryStatus = "Recovering from getting logged out";
				ridItem(currentWep, "Wield");
				idleTimer = new Timer(0);
				nearItem = GroundItems.getNearest(GGS);
				final Tile backIn = nearItem != null ? nearItem.getLocation() : null;
				if (backIn != null)
					while (getCurrentRoom().contains(backIn) && isItemAt(backIn)) {
						if (pauseScript() || idleTimer.getElapsed() > 40000)
							return true;
						clickTile(backIn, "Take");
						makeSpace(true);
						Time.sleep(400, 900);
					}
				if (!inRoom(bossRoom))
					if (puzzleTimer == null && (!fightMonsters() || !pickUpAll()))
						return false;
				secondaryStatus = oldStatus;
			}
			if (doorTimer != null)
				doorTimer = new Timer(0);
			idleTimer = new Timer(0);
			explore = true;
			return roomNumber == 1 || targetRoom == null || !targetRoom.equals(loggedRoom);
		} else
			while (Game.getClientState() == Game.INDEX_MAP_LOADING)
				Time.sleep(5
