import java.awt.*;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.imageio.ImageIO;

//import com.rarebot.Configuration;
import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Environment;
import com.rarebot.script.methods.Equipment;
import com.rarebot.script.methods.Game;
import com.rarebot.script.methods.Prayer.Book;
import com.rarebot.script.methods.Prayer.Curses;
import com.rarebot.script.methods.Prayer.Normal;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Filter;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSCharacter;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSGroundItem;
import com.rarebot.script.wrappers.RSInterface;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSModel;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSObject.Type;
import com.rarebot.script.wrappers.RSObjectDef;
import com.rarebot.script.wrappers.RSPlayer;
import com.rarebot.script.wrappers.RSTile;

@ScriptManifest(name = "iDungeon Pro",
  	authors = "Kiko",
		description = "C6, 30 Bosses, 39 Puzzles, 60 Floors, Flawless Dungeoneering!",
		version = 1.0)

public class iDungeonPro extends Script implements KeyListener, MouseListener, MouseMotionListener, MessageListener, PaintListener {
	private static final String VERSION = "1.44";
	private final int SMUGGLER = 11226, SKINWEAVER = 10058, HEIM_CRAB = 18159, RED_EYE = 18161, DUSK_EEL = 18163, FLAT_FISH = 

18165, SHORT_FIN = 18167, WEB_SNIPPER = 18169, BOULDABASS = 18171, SALVE_EEL = 18173, BLUE_CRAB = 18175, CAVE_MORAY = 18177;
	private final int ANTIPOISON = 17566, GATESTONE = 17489, GGS = 18829, TOOLKIT = 19650, COINS = 18201, CHISEL = 17444, PICKAXE 

= 16295, TINDERBOX = 17678, GUARD_KEY = 17363, END_STAIRS = 50552, ENTRANCE = 48496;
	private final int[] BLOCK_ANIMS = { 733, 7660, 13085, 13573, 13711 };
	private final int[] BONES = { 17670, 17672, 17674, 17676, 17671, 18830, 18831, 18833, 18834 }, RINGS = { 15707, 18817, 18818, 

18819, 18820, 18821, 18822, 18823, 18824, 18825, 18826, 18827, 18828 };
	private final int[] EQUIP_SLOTS = { Equipment.HELMET, Equipment.BODY, Equipment.SHIELD, Equipment.LEGS, Equipment.HANDS, 

Equipment.FEET };
	private final int[] EXIT_LADDERS = { 50604, 51156, 51704, 54675, 56149 }, FINISHED_LADDERS = { 49696, 49698, 49700, 53748, 

55484 };
	private final int[] FOOD_ARRAY = { HEIM_CRAB, RED_EYE, DUSK_EEL, FLAT_FISH, SHORT_FIN, WEB_SNIPPER, BOULDABASS, SALVE_EEL, 
	
BLUE_CRAB, CAVE_MORAY };
	private final int[] FIRES = { 49940, 49941, 49942, 49943, 49944, 49945, 49946, 49947, 49948, 49949 };
	private final int[] FRIENDLIES = { SKINWEAVER, 11086, 11087, 11088, 11089, 11090, 11091, 11092, 11093, 11094, 11095, SMUGGLER, 

11002, 11003, 11004 };
	private final int[] GROUP_PORTAL = { 53124, 53125, 53126, 7528, 56146 };
	private final int[] TOOLS = { PICKAXE, 16361, 17490, TINDERBOX, 17754, 17794, 17796, 17883 };
	private final int[] COSMIC_RUNES = { 16100, 17789 };
	private final int[] LAW_RUNES = { 16103, 17792 }; 
	private final int[] FIRE_RUNES = { 16094, 17783 };
	private final int[] NATURE_RUNES = { 16102, 17791 };
	private final int[] UNLIT = { 17682, 17684, 17686, 17688, 17690, 17692, 17694, 17696, 17698, 17700 };

	private final int[] CHAIN_WARRIORS = { 10250, 10251, 10252, 10256, 10258, 10259, 10262, 10263, 10265, 10266, 10270, 10271, 

10272, 10273, 10277, 10284, 10286, 10292, 10293, 10298, 10299, 10300, 10307, 10402, 10403, 10409, 10416, 10417, 10430, 10445, 10450, 

10451, 10452, 10457, 10458, 10517, 10526, 10546, 10548, 10847, 10848, 10849, 10854, 10855, 10856, 10860, 10861, 10868, 10877, 10881, 

10883, 10884, 10888, 10889, 10897 };
	private final int[] PLATE_WARRIORS = { 10246, 10247, 10248, 10253, 10254, 10255, 10260, 10261, 10267, 10274, 10289, 10295, 

10296, 10297, 10302, 10303, 10397, 10398, 10399, 10401, 10404, 10405, 10411, 10412, 10413, 10419, 10426, 10439, 10448, 10455, 10509, 

10542, 10845, 10851, 10858, 10866, 10873, 10885, 10886, 10899, 10893 };

	private final int[] BASIC_DOORS = { 50342, 50343, 50344, 53948, 55762 }, GUARDIAN_DOORS = { 50346, 50347, 50348, 53949, 55763 

}, STANDARD_DOORS = { 50342, 50343, 50344, 53948, 55762, 50346, 50347, 50348, 53949, 55763 };
	private final int[] BACK_DOORS = { 49462, 50342, 50343, 50344, 53948, 55762, 50350, 50351, 50352, 53950, 55764 }, BOSS_DOORS = 

{ 50350, 50351, 50352, 53950, 55764 };
	private final int[] FREE_BLOCK_DOORS = { 50278, 50279, 50280, 50305, 50306, 50307, 50314, 50315, 50316, 50329, 50330, 50331, 

53953, 53962, 53965, 53970 };
	private final int[] FREE_SKILL_DOORS = { 50272, 50273, 50274, 50287, 50288, 50289, 50299, 50300, 50301, 50308, 50309, 50310, 

50317, 50318, 50319, 53951, 53960, 53963, 53966 };
	private final int[] MEMBER_BLOCK_DOORS = { 50323, 50324, 50325, 53968, 55741, 55750, 55753, 55756, 55757, 55758 };
	private final int[] MEMBER_SKILL_DOORS = { 50281, 50282, 50283, 50293, 50294, 50295, 50335, 50336, 50337, 53954, 53956, 53958, 

53972, 55739, 55742, 55744, 55745, 55746, 55748, 55751, 55754, 55760 };
	private final int[] PRAYER_DOORS = { 50332, 50333, 50334, 53971, 55759 };
	private final int[] SUMMON_DOORS = { 50326, 50327, 50328, 53969, 55757 };
	private final int[] PUZZLE_DOORS = { 49306, 49335, 49336, 49337, 49338, 49375, 49376, 49377, 49378, 49379, 49380, 49387, 

49388, 49389, 49462, 49463, 49464, 49504, 49505, 49506, 49516, 49517, 49518, 49564, 49565, 49566, 49574, 49575, 49576, 49577, 49578, 

49579, 49603, 49604, 49605, 49606, 49607, 49608, 49625, 49626, 49627, 49644, 49645, 49646, 49650, 49651, 49652, 49677, 49678, 49679, 

53987, 53988, 53989, 53990, 53992, 54000, 54001, 54006, 54067, 54070, 54071, 54072, 54073, 54106, 54107, 54108, 54109, 54236, 54274, 

54284, 54299, 54300, 54315, 54316, 54317, 54318, 54319, 54320, 54335, 54360, 54361, 54362, 54363, 54404, 54417, 54418, 54620, 55478, 

55479, 55480, 55481, 56079, 56081, 56526, 56527, 56528, 56529 };
	private final int[] WARPED_PUZZLE_DOORS = { 32370, 32406, 33607, 33634, 33637, 33654, 33712, 34269, 34865, 35273, 35348, 

35863, 35907, 37199, 37201, 39522, 39854, 39856, 39863, 39901, 39965, 55482, 56082, 56530 };

	private final int[] ORANGE_KEYS = { 18202, 18204, 18206, 18208, 18210, 18212, 18214, 18216 };
	private final int[] SILVER_KEYS = { 18218, 18220, 18222, 18224, 18226, 18228, 18230, 18232 };
	private final int[] YELLOW_KEYS = { 18234, 18236, 18238, 18240, 18242, 18244, 18246, 18248 };
	private final int[] GREEN_KEYS = { 18250, 18252, 18254, 18256, 18258, 18260, 18262, 18264 };
	private final int[] BLUE_KEYS = { 18266, 18268, 18270, 18272, 18274, 18276, 18278, 18280 };
	private final int[] PURPLE_KEYS = { 18282, 18284, 18286, 18288, 18290, 18292, 18294, 18296 };
	private final int[] CRIMSON_KEYS = { 18298, 18300, 18302, 18304, 18306, 18308, 18310, 18312 };
	private final int[] GOLD_KEYS = { 18314, 18316, 18318, 18320, 18322, 18324, 18326, 18328 };
	private final int[][] KEYS = { ORANGE_KEYS, SILVER_KEYS, YELLOW_KEYS, GREEN_KEYS, BLUE_KEYS, PURPLE_KEYS, CRIMSON_KEYS, 

GOLD_KEYS };

	private final int[] ORANGE_DOORS = { 50208, 50209, 50210, 50211, 50212, 50213, 50214, 50215 };
	private final int[] SILVER_DOORS = { 50216, 50217, 50218, 50219, 50220, 50221, 50222, 50223 };
	private final int[] YELLOW_DOORS = { 50224, 50225, 50226, 50227, 50228, 50229, 50230, 50231 };
	private final int[] GREEN_DOORS = { 50232, 50233, 50234, 50235, 50236, 50237, 50238, 50239 };
	private final int[] BLUE_DOORS = { 50240, 50241, 50242, 50243, 50244, 50245, 50246, 50247 };
	private final int[] PURPLE_DOORS = { 50248, 50249, 50250, 50251, 50252, 50253, 50254, 50255 };
	private final int[] CRIMSON_DOORS = { 50256, 50257, 50258, 50259, 50260, 50261, 50262, 50263 };
	private final int[] GOLD_DOORS = { 50264, 50265, 50266, 50267, 50268, 50269, 50270, 50271 };
	private final int[][] KEY_DOORS = { ORANGE_DOORS, SILVER_DOORS, YELLOW_DOORS, GREEN_DOORS, BLUE_DOORS, PURPLE_DOORS, 

CRIMSON_DOORS, GOLD_DOORS };

	private final Monster[] HOODED_MONSTERS = { Monster.FORGOTTEN_WARRIOR, Monster.FORGOTTEN_WARRIOR0CHAIN, 

Monster.FORGOTTEN_WARRIOR0PLATE, Monster.FORGOTTEN_RANGER, Monster.ZOMBIE0MELEE, Monster.ZOMBIE0RANGE, Monster.SKELETON0MELEE, 

Monster.SKELETON0RANGE, Monster.HILL_GIANT };

	private final String[] PRIORITY_MONSTERS = { "Necromancer", "Reborn mage", "dragon" };
	private final String[] SECONDARY_MONSTERS = { "shade", "mage" };
	private final String[] DISTANCE_MONSTER = { "Forgotten ranger", "Forgotten mage", "Mysterious shade", "Zombie", "Hydra", 

"Icefiend", "Animated book", "Skeleton", "Thrower troll", "Ice elemental", "Lesser demon", "Greater demon", "Necromancer", "Ghost" };
	private final String[] POISON_MONSTER = { "Dungeon spider", "Cave crawler", "Night spider" };
	private final String[] METAL_TIERS = { "Novite", "Bathus", "Marmaros", "Kratonite", "Fractite", "Zephyrium", "Argonite", 

"Katagon", "Gorgonite", "Promethium", "Primal" };
	private final String[] LEATHER_TIERS = { "Protoleather", "Subleather", "Paraleather", "Archleather", "Dromoleather", 

"Spinoleather", "Gallileather", "Stegoleather", "Megaleather", "Tyrannoleather", "Sagittarian" };
	private final String[] CLOTH_TIERS = { "Salve", "Wildercress", "Blightleaf", "Roseblood", "Bryll", "Duskweed", "Soulbell", 

"Ectograss", "Runic", "Spiritbloom", "Celestial" };
	private final String[] WOOD_TIERS = { "Tangle gum", "Seeping elm", "Blood spindle", "Utuku", "Spinebeam", "Bovistrangler", 

"Thigat", "Corpsethorn", "Entgallow", "Grave creeper", "Sagittarian" };
	private final String[] FREE_WORLDS = { "1", "3", "4", "5", "7", "8", "10", "11", "13", "14", "16", "17", "19", "20", "25", 

"29", "30", "33", "34", "35", "37", "38", "41", "43", "47", "49", "50", "55", "57", "61", "62", "73", "74", "75", "80", "81", "87", 

"90", "93", "102", "105", "106", "108", "113", "118", "120", "123", "134", "135", "136", "141", "149", "152", "153", "154", "155", 

"161", "167", "169" };
	private final String[] MELEE_SLOTS = { "full helm", "body", "kiteshield", "plate", "gauntlets", "boots" };
	private final String[] RANGE_SLOTS = { "coif", "body", "", "chaps", "vambraces", "boots" };
	private final String[] MAGIC_SLOTS = { "hood", "robe top", "", "robe bottom", "gloves", "boots" };

	private final String LOGO = "http://img526.imageshack.us/img526/6892/iconacv.png";
	private final String HIDE = "http://img807.imageshack.us/img807/5183/hide.png";
	private final String HIDE_S = "http://img807.imageshack.us/img807/5183/hide.png";
	private final String UPARROW = "http://www2.hawaii.edu/~mbutler/ButlerLab.data/SmartObjects/up_arrow_green_sm.gif";
	private final String UPARROW_S = "http://www2.hawaii.edu/~mbutler/ButlerLab.data/SmartObjects/up_arrow_green_sm.gif";
	private final String DOWNARROW = "http://www2.hawaii.edu/~mbutler/ButlerLab.data/SmartObjects/down_arrow_green_sm.gif";
	private final String DOWNARROW_S = "http://www2.hawaii.edu/~mbutler/ButlerLab.data/SmartObjects/down_arrow_green_sm.gif";
	private final String PLAY = "http://files.softicons.com/download/toolbar-icons/gaming-toolbar-icons-by-natalie-

nash/png/32x32/Play%20Symbol.png";
	private final String PLAY_S = "http://img42.imageshack.us/img42/9383/playsi.png";
	private final String PAUSE = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Gnome-media-playback-pause.svg/48px-

Gnome-media-playback-pause.svg.png";
	private final String PAUSE_S = "https://upload.wikimedia.org/wikipedia/commons/thumb/0/04/Gnome-media-playback-

pause.svg/48px-Gnome-media-playback-pause.svg.png";
	private final String INFO = "http://cdn1.iconfinder.com/data/icons/lullacons/large-info.png";
	private final String INFO_S = "http://cdn1.iconfinder.com/data/icons/lullacons/large-info.png";
	private final String HELP = "http://www.webhelpdesk.com/images/icon_question-mark.png";
	private final String HELP_S = "http://www.webhelpdesk.com/images/icon_question-mark.png";
	private final String STOP = "http://fedoraproject.org/w/uploads/thumb/6/61/Stop_(medium_size).png/35px-Stop_

(medium_size).png";
	private final String STOP_S = "http://fedoraproject.org/w/uploads/thumb/6/61/Stop_(medium_size).png/35px-Stop_

(medium_size).png";
	private final String MINIS = "http://images4.wikia.nocookie.net/__cb20111118163341/runescape/images/f/f6/Bronze_arrow_5.png";
	private final String MINIS_S = "http://www.go-runescape.com/img/woodcutting_fletching_firemaking/arrow_bronze1.gif";

	private static final int[] SLAYER_MONSTERS = { 10694, 10695, 10696, 10697, 10698, 10699, 10700, 10701, 10702, 10704, 10705 };
	
	private static final String CACHE = "C:/Documents and Settings/Daniel/My Documents/RareBot";
	private static final String DIRECTORY = CACHE + File.separator + "iDungeonLoader" + File.separator;
	private static final RSTile ORIGIN = new RSTile(0, 0), DAEMONHEIM = new RSTile(3450, 3720);
	private static final String LOGIN_FILE = DIRECTORY + File.separator + "iDPLogin.ini";

	private static final String EMPTY_TIMER = "00:00:00";

	private static Floor floor = Floor.OUTSIDE;
	private static int dungLevel, prayerLevel, slayerLevel = 85;
	private static Point mp = new Point(0, 0), cp = new Point(0, 0);

	private int[] foods = FOOD_ARRAY, goodFoods, topFoods;
	private int[] equipmentTiers = { 0, 0, 0, 0, 0, 0 }, initialEquipmentTiers = { 0, 0, 0, 0, 0, 0 }, tempTiers = { 0, 0, 0, 0, 

0, 0 };
	private String[] improvements, armorTiers = METAL_TIERS, weaponTiers, slotNames = MELEE_SLOTS;

	private ArrayList<Integer> bounds = new ArrayList<Integer>(), lockIDs = new ArrayList<Integer>();
	private ArrayList<RSArea> rooms = new ArrayList<RSArea>(), clearedRooms = new ArrayList<RSArea>(), finishedRooms = new 

ArrayList<RSArea>(), skipRooms = new ArrayList<RSArea>();
	private ArrayList<RSArea> unBacktrackable = new ArrayList<RSArea>();
	private ArrayList<RSArea> puzzleRooms = new ArrayList<RSArea>(), finishedPuzzles = new ArrayList<RSArea>(), chasmRooms = new 

ArrayList<RSArea>();
	private ArrayList<RSObject> newObjects = new ArrayList<RSObject>();
	private ArrayList<RSTile> allDoors = new ArrayList<RSTile>(), goodDoors = new ArrayList<RSTile>(), openedDoors = new 

ArrayList<RSTile>();
	private ArrayList<RSTile> newDoors = new ArrayList<RSTile>(), backDoors = new ArrayList<RSTile>(), drawDoors = new 

ArrayList<RSTile>();
	private ArrayList<RSTile> guardianDoors = new ArrayList<RSTile>(), lockedDoors = new ArrayList<RSTile>(), blockedDoors = new 

ArrayList<RSTile>(), skillDoors = new ArrayList<RSTile>(), finishedDoors = new ArrayList<RSTile>();
	private ArrayList<RSTile> bossPath = new ArrayList<RSTile>(), deathPath = new ArrayList<RSTile>();
	private ArrayList<RSTile> badTiles = new ArrayList<RSTile>();
	private ArrayList<RSTile> oldObjectTiles = new ArrayList<RSTile>(), newObjectTiles = new ArrayList<RSTile>();
	private ArrayList<RSTile> roomFlags = new ArrayList<RSTile>();
	private ArrayList<RSTile> puzzlePoints = new ArrayList<RSTile>();

	private boolean authCheck, verified, developer, memberWorld = true, newDungeon = true, inDungeon, levelRequirement = true, 

temporarySecondary, welcomeBack;
	private boolean isDead, isCursing, unreachable, outOfAmmo, lockDown, skipRoom, isRushing, shadowHooded, roomHooded, 

itemReceived, skipDoorFound, showSecondary = true;
	private boolean explore, open, bossFight, retrace, finish, exit, settingsFinished, forcePrestige, aborted, twoHanded, 

disrobed, cookReady, spawnRoom;
	private boolean puzzleSolved, puzzleRepeat, roomSwitch, disableBossPath;
	private double tpf = 0, fph = 0;
	private int degrees, pitch;
	private int attackMode = -1, defaultMode = -1, defenseMode = 3, weaponTier = 11, attackTier, defenseTier, newWeapon, 

startArmor, armorSlot = -1, attempts;
	private int roomNumber, primaryWep, secondaryWep, arrows, blastBox, complexity = 6, rushTo = 18, aComplexity = 6, rComplexity 

= 1, fNumber = 0, maxFloor = 47, dungStartLevel;
	private int abortedCount, dungeonCount = 0, prestigeCount, puzzleCount, reportedPrestige, userCount = 0, userOffset = 114, 

userRank = 0, rankOffset = 144;
	private int dungStart, dungGain, strStart, strGain, attStart, attGain, defStart, defGain, rngStart, rngGain, mgcStart, 

mgcGain, hitStart, hitGain, prayStart, prayGain;
	private int runeStart, runeGain, mineStart, mineGain, fireStart, fireGain, woodStart, woodGain, smthStart, smthGain, 

crftStart, crftGain, agilStart, agilGain, thevStart, thevGain, summStart, summGain, herbStart, herbGain, cnstStart, cnstGain, 

cookStart, cookGain, fishStart, fishGain, slayStart, slayGain;
	private int tokens, restarts, restartCount, deaths, bossStage, bossID, cProg, ceph, deph, seph, bossHp = 100, levelGoal;
	private long fastestMillis = 999999, slowestMillis = 0;

	private Combat tempMode = Melee.NONE;
	private Food fish;
	private Logs logs;
	private RSArea newRoom, currentRoom, oldRoom, startRoom, groupRoom, bossRoom, targetRoom, gateRoom;
	private RSGroundItem nearItem;
	private RSNPC nearMonster;
	private RSTile nearDoor, nearDoor2, bossDoor, destDoor, entryDoor, oldDoor, safeTile;
	private Spell combatSpell;
	private String controlStatus = "", fastestTime = EMPTY_TIMER, secondaryStatus = "", slowestTime = EMPTY_TIMER, status = 

"Loading images...", statusSettings = "Hover over an option to find out more";
	private String armorType, bossName = "", failReason = "", lastMessage = "", strongestMonster = "", userSettings, weaponType = 

"Unknown";
	private Style combatStyle = Style.MELEE, primaryStyle = Style.MELEE, protection;
	private Timer bossTimer, doorTimer, dungTimer, failTimer = new Timer(random(360000, 480000)), idleTimer = new Timer(0), 

itemTimer = new Timer(random(800, 1100)), logoutTimer = new Timer(0), prayTimer, puzzleTimer, runTimer = new Timer(0);

	@Override
	public boolean onStart() {
		userSettings = DIRECTORY + "iDPSettings141-" + account.getName() + ".ini";
		//loadSettings();
		dungStartLevel = skills.getRealLevel(Skills.DUNGEONEERING);
		dungLevel = skills.getRealLevel(Skills.DUNGEONEERING);
		levelGoal = dungLevel + 1;
		return retrieveCredentials();
	}

	public boolean retrieveCredentials() {
		/*try {
		FileInputStream fstream = new FileInputStream(LOGIN_FILE);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		username = br.readLine().replaceAll("_", "");
		password = br.readLine().replaceAll("_", "");
		in.close();
		try {
			String[] tmp = { username, "" };
			BufferedWriter out = new BufferedWriter(new FileWriter(LOGIN_FILE));
			for (String s : tmp) {
				out.write(s);
				out.newLine();
			}
			if (out != null)
				out.close();
		} catch (Exception e) {
			log(RED, "Unable to clear the password data");
			return false;
		}
		authCheck = true;
		} catch (Exception e) {
		log.severe("Error checking credentials!");
		}*/
		authCheck = true;
		verified = false;
		return authCheck;
	}
	
	@Override
	public void onFinish() {
		authCheck = false;
		hidePaint = false;
		Option.DEBUG.set(true);
		if (dungeonCount > 1 && verified) {
			verified = false;
			log(PRP, "Thanks for using iDungeon Pro v" + VERSION + " by kiko & ShadowMoose");
			env.saveScreenshot(true);
			log("We completed " + dungeonCount + " dungeons for " + tokens + " tokens!");
			env.enableRandoms();
		}
	}

	private void enterDungeon() {
		int selComplexity = valueOf(complexity);
		RSObject entrance;
		aborted = false;
		exit = false;
		lastMessage = "";
		status = "Entering Daemonheim...";
		while ((entrance = objects.getNearest(ENTRANCE)) != null) {
			if (failSafe())
				return;
			updateProgress();
			selComplexity = valueOf(Option.RUSH.enabled() && cProg < rushTo ? rComplexity : complexity);
			if (lastMessage.contains("carrying items")) {
				log.severe("Please bank all items besides your Ring and restart the script!");
				authCheck = false;
				return;
			}
			RSInterface floorWindow = interfaces.get(947);
			RSInterface cWindow = interfaces.get(938);
			if (interfaces.getComponent(210, 2).isValid()) {
				interfaces.getComponent(210, 2).doClick();
			} else if (interfaces.get(236).isValid()) {
				interfaces.getComponent(236, !isRushing && Option.MEDIUM.enabled() && interfaces.getComponent(236, 

0).containsText("size") && (!Option.RUSH.enabled() || fNumber > rushTo) ? 2 : 1).doClick();
			} else if (floorWindow.getComponent(608).isValid()) {
				if (updateProgress() && fNumber > 1 && maxFloor > 0 && (fNumber + (forcePrestige ? 1 : 0) > maxFloor 

|| (username.isEmpty() && dungeonCount > 6))) {
					if (!resetPrestige())
						return;
				}
				boolean skipScrolling = fNumber == maxFloor;
				if (skipScrolling || scrollToFloor()) {
					for (int d = 0; d < 10; d++) {
						if (!floorWindow.getComponent(608).isValid())
							break;
						if (skipScrolling || floorWindow.getComponent(765).getText().contains("" + fNumber)) {
							floorWindow.getComponent(761).interact("Confirm");
						} else floorWindow.getComponent(608 + cProg).interact("Select-floor");
						sleep(700, 1000);
					}
				} else mouse.moveRandomly(200);
			} else if (cWindow.getComponent(42).isValid()) {
				lastMessage = "";
				while (cWindow.getComponent(42).isValid()) {
					if (lastMessage.contains("access complexity")) {
						selComplexity--;
						lastMessage = "";
					}
					if (!cWindow.getComponent(42).getText().contains("" + selComplexity)) {
						cWindow.getComponent(55 + 5 * selComplexity).interact("Select-complexity");
					} else if (cWindow.getComponent(37).interact("Confirm")) {
						sleep(400);
					}
					sleep(600, 800);
				}
			} else if (updateProgress() && fNumber > 1 && maxFloor > 0 && (fNumber + (forcePrestige ? 1 : 0) > maxFloor || 

(username.isEmpty() && dungeonCount > 6))) {
				if (!resetPrestige())
					return;
			} else if (doObjAction(entrance, "Climb-down")) {
				waitToStop(false);
			}
			sleep(600, 1000);
		}
		newDungeon = true;
		inDungeon = true;
		aComplexity = selComplexity;
	}

	private void exitDungeon() {
		status = "Exiting dungeon...";
		selectTab(Game.TAB_INVENTORY, 3);
		exit = true;
		idleTimer = new Timer(0);
		while (inDungeon()) {
			if (failBasic())
				return;
			if (player().isInCombat()) {
				getCurrentRoom();
				setTargetRoom(currentRoom);
				if (bossRoom == null && npcs.getNearest(guardians) != null)
					intentionallyBacktrack();
			} else if (interfaces.get(236).isValid()) {
				interfaces.getComponent(236, 1).interact("Continue");
			} else if (interfaces.get(211).isValid()) {
				interfaces.getComponent(211, 3).interact("Continue");
			} else if (openPartyTab()) {
				interfaces.getComponent(939, 34).interact("Leave-party");
			}
			sleep(700, 1100);
		}
		floor = Floor.OUTSIDE;
		inDungeon = false;
		newDungeon = false;
	}

	private void exploreRoom() {
		if (roomNumber == 1) {
			pickUpAll();
			open = true;
			roomHooded = false;
			return;
		}
		if (areaContains(bossRoom, myLoc())) {
			retrace = false;
			bossFight = true;
			return;
		}
		boolean unexplored = newRoom != null;
		if (unexplored)
			protection = null;
		roomHooded = shadowHooded && !roomContains(targetRoom, guardianDoors);
		if (floor == Floor.OCCULT) {
			spawnRoom = getNpcInRoom("Necromancer") != null;
		} else if (floor == Floor.WARPED) {
			spawnRoom = getNpcInRoom("Reborn mage") != null;
		} else spawnRoom = false;
		if (rooms.size() > 1)
			status = "Exploring a " + (unexplored ? "new" : "old") + " room";
		newRoom = null;
		boolean toCheck = false;
		int puzzleCheck = 0;
		getCurrentRoom();
		if (aComplexity > 4 && (unexplored || (!finishedPuzzles.contains(targetRoom) && !finishedRooms.contains(targetRoom)))) 

{
			for (RSTile door : drawDoors) {
				if (areaContains(targetRoom, door) && !finishedDoors.contains(door)) {
					toCheck = true;
					break;
				}
			}
		}
		if (developer && unexplored) {
			for (RSNPC warrior : getNpcsInRoom("Forgotten warrior")) {
				if (!intMatch(warrior.getID(), CHAIN_WARRIORS) && !intMatch(warrior.getID(), PLATE_WARRIORS))
					log(BLK, "Unknown warrior ID: " + warrior.getID());
			}
		}
		if (toCheck) {
			puzzleSolved = false;
			puzzleRepeat = false;
			roomSwitch = false;
			entryDoor = null;
			if (!unexplored) {
				for (RSTile oD : openedDoors) {
					if (calc.distanceTo(oD) < 4) {
						entryDoor = oD;
						break;
					}
				}
			}
			puzzleCheck = checkPuzzles();
			puzzlePoints.clear();
			if (puzzleCheck != 2 && failSafe())
				puzzleCheck = 2;
			if (puzzleCheck == 2 && secondaryStatus.startsWith("Attempting to"))
				puzzleCheck = -1;
			nearItem = null;
			nearMonster = null;
			String puzzleName = status.contains("Puzzle room: ") ? status.substring(status.indexOf(":") + 2) : "Unknown 

puzzle";
			if (puzzleCheck == 1 || puzzleSolved) {
				if (puzzleTimer != null && puzzleTimer.getElapsed() > 1000) {
					log(GN5, "Puzzle room: " + puzzleName + " completed successfully in " + 

puzzleTimer.toElapsedString() + "!");
					puzzleCount++;
				}
				safeTile = null;
				puzzleRooms.remove(targetRoom);
				clearedRooms.add(targetRoom);
				failTimer = new Timer(random(360000, 480000));
				idleTimer = new Timer(0);
				finishedRooms.remove(targetRoom);
				deathPath.clear();
			} else if (puzzleCheck == 2) {
				if (exit)
					return;
				if (puzzleTimer != null && (!puzzleTimer.isRunning() || status.contains("reengage"))) {
					log.severe("Took too long to solve Puzzle: " + puzzleName + " (" + (puzzleTimer != null ? 

puzzleTimer.toElapsedString() : "unknown time") + ")");
					if (developer)
						env.saveScreenshot(false);
					retrace = true;
					tempMode = Melee.NONE;
					finishedDoors.addAll(newDoors);
					finishedRooms.add(targetRoom);
					goodDoors.removeAll(finishedDoors);
					allDoors.removeAll(finishedDoors);
					skipRoom = true;
					puzzleTimer = null;
					deathPath.clear();
					teleHome(false);
					secondaryStatus = "";
				} else open = true;
				safeTile = null;
				failTimer = new Timer(random(360000, 480000));
				idleTimer = new Timer(0);
				return;
			} else if (puzzleCheck == -1) {
				if (failReason.contains("requirements") || failReason.contains("Members") || failReason.contains

("Prayer")) {
					log("Unable to complete Puzzle: " + puzzleName + "; Reason: " + failReason + ".");
				} else {
					log(RED, "Unable to complete Puzzle: " + puzzleName + "; Reason: " + failReason + ".");
					if (developer)
						env.saveScreenshot(false);
				}
				safeTile = null;
				retrace = true;
				tempMode = Melee.NONE;
				finishedDoors.addAll(newDoors);
				finishedRooms.add(targetRoom);
				goodDoors.removeAll(finishedDoors);
				allDoors.removeAll(finishedDoors);
				skipRoom = true;
				failTimer = new Timer(random(360000, 480000));
				idleTimer = new Timer(0);
				deathPath.clear();
			}
			if (!puzzleRepeat)
				finishedPuzzles.add(targetRoom);
			puzzleTimer = null;
		}
		if (puzzleCheck != -1) {
			if (unexplored && !slayerCheck()) {
				for (RSTile door : guardianDoors) {
					if (areaContains(targetRoom, door)) {
						log(RED, "Unkillable slayer monster in this room, removing Guardian door");
						finishedDoors.add(door);
						allDoors.remove(door);
						goodDoors.remove(door);
					}
				}
			}
			guardianDoors.removeAll(finishedDoors);
			if (skipRooms.contains(targetRoom)) {
				skipRoom = true;
			} else if (unexplored) {
				if (roomContains(targetRoom, guardianDoors)) {
					if (skipRoom) {
						if (npcs.getNearest(monster) != null) {
							if (developer)
								log("We can't skip through this room!");
							skipRoom = false;
						} else skipRooms.add(targetRoom);
					}
				} else if (isRushing && !skipRoom && newRoom != null) {
					skipRoom = true;
					if (npcs.getNearest(monster) != null)
						log("No guardian doors, skipping through this room!");
					skipRooms.add(targetRoom);
				}
			}
			if (skipRoom) {
				if (!pickUpAll())
					return;
			} else {
				setRetaliate(true);
				if (!fightMonsters())
					return;
				if (!pickUpAll())
					return;
				eatFood(foods, 45, 50);
				buryBones();
				if (Option.RANGE.enabled() && invCount(true, arrows) > (random(10, 40)))
					doItemAction(inventory.getItem(arrows), "Wield");
				if (!pickUpAll())
					return;
				getCurrentRoom();
			}
		}
		if (failSafe())
			return;
		allDoors.removeAll(backDoors);
		goodDoors.removeAll(backDoors);
		goodDoors.removeAll(openedDoors);
		goodDoors.removeAll(finishedDoors);
		if (puzzleCheck < 1) {
			if (puzzleCheck == -1 || (!roomContains(targetRoom, allDoors) && !roomContains(targetRoom, goodDoors) && !

roomContains(targetRoom, openedDoors))) {
				finishedRooms.add(targetRoom);
				for (RSTile door : drawDoors) {
					if (calc.distanceBetween(door, targetRoom.getNearestTile(door)) < 3)
						finishedDoors.add(door);
				}
			} else if (!clearedRooms.contains(targetRoom)) {
				clearedRooms.add(targetRoom);
			}
		}
		allDoors.removeAll(finishedDoors);
		goodDoors.removeAll(finishedDoors);
		guardianDoors.removeAll(finishedDoors);
		openedDoors.removeAll(finishedDoors);
		secondaryStatus = "";
		if (finishedRooms.contains(targetRoom)) {
			retrace = true;
		} else open = true;
		newRoom = null;
		roomHooded = false;
	}

	private void finishDungeon() {
		status = "Completing the dungeon...";
		RSObject finishedLadder = objects.getNearest(FINISHED_LADDERS);
		if (finishedLadder != null) {
			safeTile = finishedLadder.getLocation();
			log(BLK, "Exit found" + (dungTimer != null ? " in " + dungTimer.toElapsedString() : "") + ", completing the 

dungeon.");
			bossTimer = null;
			failTimer.reset();
			if (bossRoom == null)
				bossRoom = targetRoom;
			o:while (!interfaces.get(933).isValid()) {
				if (failSafe())
					return;
				if (lastMessage.startsWith("You have already") && idleTimer.getElapsed() > random(45000, 60000)) {
					log(RED, "The exit ladder is glitched, unable to finish this dungeon");
					exit = true;
					return;
				}
				if (itemReceived) {
					if (developer && inventory.containsOneOf(16905, 16909))
						waitForResponse();
					improveBossWeapon();
					improveWeaponBinding();
					itemReceived = false;
				} else if (interfaces.get(236).isValid()) {
					if (interfaces.getComponent(236, 1).doClick()) {
						for (int i = 0; i < 10; i++) {
							if (interfaces.get(933).isValid())
								break o;
							sleep(200, 300);
						}
					}
				} else if ((finishedLadder = objects.getTopAt(safeTile)) != null) {
					if (calc.distanceTo(finishedLadder) < 3 && !adjacentTo(finishedLadder))
						walkTo(finishedLadder.getLocation(), 0);
					if (smartSleep(doObjAction(finishedLadder, "End-dungeon"), true)) {
						for (int c = 0; c < 10; c++) {
							if (interfaces.get(236).isValid())
								continue o;
							sleep(100, 200);
						}
					}
				}
				sleep(200, 300);
			}
			status = "Waiting for the next dungeon...";
			dungeonCount++;
			fNumber++;
			cProg++;
			long dungeonTime = dungTimer.getElapsed();
			if (dungeonTime < fastestMillis) {
				if (slowestMillis == 0 && fastestMillis != 999999) {
					long millis = fastestMillis;
					String time = fastestTime;
					slowestMillis = millis;
					slowestTime = time;
				} else if (password.isEmpty() && dungeonCount > random(5, 20)) {
					exit = true;
				}
				fastestMillis = dungeonTime;
				fastestTime = dungTimer.toElapsedString();
			} else if (dungeonTime > slowestMillis) {
				slowestMillis = dungeonTime;
				slowestTime = dungTimer.toElapsedString();
			}
			tpf = Math.rint(runTimer.getElapsed() * 10 / dungeonCount / 60000) / 10;
			int trueTime = (int) (dungeonTime / 1000);
			int truePrestige = prestigeCount - reportedPrestige;
			reportedPrestige = prestigeCount;
			clearAll();
			updateProgress();
			RSInterface notice = interfaces.get(519);
			if ((notice.isValid() && fNumber + 1 > maxFloor && notice.getText().toLowerCase().contains("not available 

at")) || fNumber > maxFloor) {
				log(BLU, "Last floor reached, prestiging!");
				if (fNumber > maxFloor)
					forcePrestige = true;
				exit = true;
			}
			boolean clicked = false;
			int tokensGained = 0, expGained = 0;
			RSInterface finishWindow;
			o:while ((finishWindow = interfaces.get(933)).isValid()) {
				if (failBasic())
					break;
				if (!clicked && clickComponent(finishWindow.getComponent(13), "")) {
					sleep(200, 300);
				}
				if (clickComponent(finishWindow.getComponent(322), "")) {
					sleep(300, 500);
					if (!clicked) {
						String tokenString = finishWindow.getComponent(41).getText().replace("%", "").trim();
						String expString = finishWindow.getComponent(39).getText().replace("%", "").trim();
						if (!tokenString.isEmpty())
							tokensGained = Integer.parseInt(tokenString);
						if (!expString.isEmpty())
							expGained = Integer.parseInt(expString);
						tokens += tokensGained;
						if (tokensGained > 0) {
							clicked = true;
							updateSiggy(trueTime, expGained, tokensGained, truePrestige);
						} else log(RED, "Stat uploading failed!");
					}
					for (int i = 0; i < 40; i++) {
						if (!finishWindow.isValid())
							break o;
						sleep(200, 300);
					}
				} else 

					sleep(100, 200);
			}
			updateProgress();
			if (failSafe())
				return;
		}
	}

	private boolean openNextDoor() {
		if (roomNumber == 1 && areaContains(startRoom, myLoc()) && (memberWorld || cookReady) && getSaleCount() < 28) {
			if (memberWorld && bossRoom == null && floor != Floor.FROZEN && !invContains(ANTIPOISON)) {
				int coinCount = inventory.getCount(true, COINS);
				int poisonCount = coinCount / 200;
				if (poisonCount > random(1, 3))
					poisonCount = floor == Floor.ABANDONED && rooms.size() == 1 ? random(2, 4) : 2;
					if (poisonCount > 0 && inventory.getCount() < random(20, 24))
						shop(464, poisonCount);
			}
			unPoison();
			if ((cookReady || readyToCook()) && getSaleCount() < 24) {
				cookReady = false;
				failTimer = new Timer(random(360000, 480000));
				if (fish != null && logs != null && inventory.contains(TINDERBOX)) {
					status = "Cook: " + fish.getName(true) + " on " + logs.getName(true);
					log(BLU, status);
					idleTimer.reset();
					lastMessage = "";
					o:for (int c = 0; c < 2; c++) {
						omNomNom();
						if (!inventory.contains(fish.rawID())) {
							RSObject fire = getObjInRoom(FIRES);
							if (getSaleCount() > random(25, 27) || inventory.getCount(true, COINS) < (fire 

!= null ? logs.price() : 0) + fish.price(random(1, 3)))
								break;
						}
						while (!inventory.isFull() && !lastMessage.contains("inventory space") && !

lastMessage.contains("afford") && !lastMessage.contains("enough money")) {
							if (failCheck())
								break o;
							failInterfaces();
							if (!inventory.contains(TINDERBOX)) {
								RSGroundItem tinder = getItemInRoom(targetRoom, TINDERBOX);
								if (tinder != null) {
									smartSleep(pickUpItem(tinder), false);
									continue;
								}
								break o;
							}
							if (!shop(fish, logs))
								sleep(400, 800);
						}
						idleTimer.reset();
						boolean textReady = false;
						i:while (inventory.contains(fish.rawID())) {
							if (failCheck())
								break o;
							failInterfaces();
							boolean toWait = false;
							RSObject fire = getObjInRoom(FIRES);
							if (interfaces.get(740).isValid()) {
								clickComponent(interfaces.getComponent(740, 3), "Continue");
							} else if (fire == null) {
								if (!inventory.contains(TINDERBOX))
									break i;
								int logID = logs.getID();
								if (!inventory.contains(logID))
									logID = getItemID("branches");
								if (logID == -1) {
									getItemInRoom(startRoom, UNLIT);
									if (nearItem != null) {
										toWait = pickUpItem(nearItem);
									} else if (!shop(fish, logs)) {
										break o;
									}
								} else if (lastMessage.contains("can't light")) {
									walkAdjacentTo(myLoc(), 2);
									lastMessage = "";
								}
								RSObject obelisk = getObjInRoom("Summoning obelisk");
								if (calc.distanceTo(startRoom.getCentralTile()) == 1 && (obelisk == 

null || calc.distanceBetween(myLoc(), obelisk.getLocation()) == 2)) {
									walkAdjacentTo(myLoc(), random(1, 3));
								} else if (logID != -1 && inventory.useItem(TINDERBOX, logID)) {
									for (int i = 0; i < 10; i++) {
										if (getObjInRoom(FIRES) != null)
											continue i;
										if (player().getAnimation() != -1)
											i = 0;
										sleep(200, 300);
									}
								}
							} else if (player().getAnimation() != 897) {
								if (textReady && interfaces.getComponent(752, 5).getText().isEmpty()) 

{
									keyboard.sendText("" + random(22, 99), true);
									toWait = true;
									textReady = false;
								} else if (interfaces.getComponent(944, 78).isValid()) {
									String amount = inventory.getCount(fish.rawID()) > 10 ? "X" : 

"10";
									if (clickComponent(interfaces.getComponent(944, 78), "Make " + 

amount)) {
										if (amount.equals("X")) {
											textReady = true;
										} else toWait = true;
									}
								} else toWait = useItem(fish.rawID(), fire);
							}
							smartSleep(toWait, false);
						}
					}
				}
			}
			makeSpace(true);
		}
		if (roomNumber != 1 && !rechargePrayer())
			return false;
		nearDoor = null;
		nearDoor2 = null;
		newRoom = null;
		allDoors.removeAll(finishedDoors);
		allDoors.removeAll(backDoors);
		goodDoors.removeAll(finishedDoors);
		goodDoors.removeAll(backDoors);
		goodDoors.removeAll(openedDoors);
		openedDoors.removeAll(finishedDoors);
		for (int i = clearedRooms.size() - 1; i >= 0; i--) {
			RSArea cRoom = clearedRooms.get(i);
			if (cRoom != null && !finishedRooms.contains(cRoom)) {
				if (!roomContains(cRoom, allDoors) && !roomContains(cRoom, goodDoors) && !roomContains(cRoom, 

openedDoors)) {
					finishedRooms.add(cRoom);
					for (RSTile door : drawDoors) {
						if (door != null && calc.distanceBetween(door, cRoom.getNearestTile(door)) < 4)
							finishedDoors.add(door);
					}
				}
			}
		}
		clearedRooms.removeAll(finishedRooms);
		openedDoors.removeAll(finishedDoors);
		allDoors.removeAll(finishedDoors);
		allDoors.removeAll(backDoors);
		goodDoors.removeAll(finishedDoors);
		goodDoors.removeAll(backDoors);
		goodDoors.removeAll(openedDoors);
		updateLocks();
		getCurrentRoom();
		if (currentRoom != null) {
			lastMessage = "";
			oldRoom = getCurrentRoom();
			if (roomNumber == 1 && aComplexity > 4 && !inventory.contains(GGS) && rooms.size() > 1) {
				status = "Teleporting back to where we died";
				boolean blockedDest = false;
				while (!inventory.contains(GGS) && getItemInRoom(currentRoom, GGS) == null) {
					if (failSafe())
						return false;
					if (lastMessage.contains("destination") || lastMessage.contains("blocking you")) {
						blockedDest = true;
						break;
					}
					if (player().getAnimation() == -1) {
						if (!areaContains(startRoom, myLoc()) && !areaContains(groupRoom, myLoc())) {
							unBacktrack(true);
						} else if (telePortal()) {
							waitToStop(false);
						}
					}
					sleep(300, 400);
					getCurrentRoom();
				}
				if (!blockedDest) {
					setTargetRoom(groupRoom != null ? groupRoom : currentRoom);
					groupRoom = null;
					if (!areaEquals(targetRoom, startRoom)) {
						deathPath.clear();
						if (puzzleRooms.contains(targetRoom) || (bossRoom != null && (bossName.equals

("Sagittare") || bossName.equals("Stomp") || bossName.startsWith("Kal'Ger")))) {
							for (int c = 0; c < 5; c++) {
								if (inventory.contains(GGS) || inventory.isFull())
									break;
								smartSleep(pickUpItem(getItemInRoom(targetRoom, GGS)), false);
							}
						}
						explore = true;
						return true;
					}
				}
			}
			if (disableBossPath && goodDoors.isEmpty()) {
				log(BLU, "Remaining openable rooms explored, returning to the boss!");
				disableBossPath = false;
				if (teleTo(GATESTONE))
					return true;
			}
			if (!disableBossPath && bossRoom != null && !bossPath.isEmpty()) {
				for (RSTile door : bossPath) {
					if (areaContains(currentRoom, door)) {
						nearDoor = door;
						doorTimer = new Timer(0);
						idleTimer = new Timer(0);
						crossTheChasm(nearDoor);
						status = "Opening a door back to the Boss";
						if (bossDoor != null && nearDoor.equals(bossDoor)) {
							walkToDoor(1);
							waitToEat(false);
							if (floor != Floor.ABANDONED || getNpcInRoom(SKINWEAVER) == null)
								omNomNom();
							setTargetRoom(bossRoom);
							if (!prepareForBoss())
								return false;
							setTargetRoom(getCurrentRoom());
						}
						return openOldDoor();
					}
				}
			}
			if (groupRoom != null && !deathPath.isEmpty()) {
				if (!areaEquals(currentRoom, groupRoom)) {
					for (RSTile door : deathPath) {
						if (areaContains(currentRoom, door)) {
							nearDoor = door;
							doorTimer = new Timer(0);
							idleTimer = new Timer(0);
							crossTheChasm(nearDoor);
							status = "Opening a door back to where we died";
							return openOldDoor();
						}
					}
				}
				deathPath.clear();
			}
			setTargetRoom(currentRoom);
			int doorType = getBestDoor();
			if (nearDoor != null) {
				doorTimer = new Timer(0);
				idleTimer = new Timer(0);
				crossTheChasm(destDoor);
				lastMessage = "";
				if (!calc.tileOnScreen(nearDoor))
					walkToDoor(1);
				threadPitch(100);
				switch (doorType) {
				case 1:
					return openBasicDoor();
				case 2:
					return openBlockedDoor();
				case 3:
					return openLockedDoor();
				case 4:
					return openSkillDoor();
				case 5:
					return openOldDoor();
				}
			}
		}
		return false;
	}

	private void retraceDungeon() {
		if (areaContains(bossRoom, myLoc())) {
			bossFight = true;
			return;
		}
		boolean teleBack = false;
		restarts++;
		restartCount++;
		allDoors.removeAll(finishedDoors);
		allDoors.removeAll(backDoors);
		goodDoors.removeAll(finishedDoors);
		goodDoors.removeAll(backDoors);
		if (bossRoom != null && getObjInRoom(FINISHED_LADDERS) != null) {
			finish = true;
			return;
		}
		if (npcs.getNearest(guardians) == null && !pickUpAll())
			return;
		if (gateRoom != null) {
			updateLocks();
			for (RSTile t : goodDoors) {
				if (areaContains(gateRoom, t)) {
					log(PRP, "Gatestone teleporting to the next good door");
					setTargetRoom(gateRoom);
					while (!areaEquals(getCurrentRoom(), targetRoom)) {
						teleTo(GATESTONE);
					}
					gateRoom = null;
					open = true;
					return;
				}
			}
		}
		if (restartCount > 2) {
			if (failSafe())
				return;
			if (roomNumber == 1 && !deathPath.isEmpty())
				deathPath.clear();
			updateLocks();
			if (goodDoors.isEmpty()) {
				if (bossRoom == null) {
					if (skipDoorFound) {
						log(RED, "An unopenable door is preventing us from solving this dungeon");
					} else {
						log.severe("No good doors remain.. Aborting dungeon");
						if (floor == Floor.WARPED)
							waitForResponse();
						abortedCount++;
					}
					exit = true;
				} else {
					disableBossPath = false;
					oldDoor = null;
					oldRoom = null;
					open = true;
				}
				return;
			} else if (rooms.size() == 1) {
				walkTo(currentRoom.getCentralTile(), 3);
			}
			if (areaContains(startRoom, myLoc())) {
				oldDoor = null;
				oldRoom = null;
				open = true;
				return;
			}
			if (disableBossPath)
				disableBossPath = false;
			if (restarts > random(aComplexity + 5, aComplexity + 8)) {
				log.severe("Restarted too many times.. Aborting dungeon");
				abortedCount++;
				exitDungeon();
				return;
			}
			if (!monsterBacktrack())
				return;
			restartCount = 0;
			teleBack = true;
		} else if (unBacktrackable.contains(currentRoom)) {
			teleBack = true;
		}
		if (rooms.size() == 1)
			return;
		status = "Backtracking through the dungeon";
		getCurrentRoom();
		if (areaContains(targetRoom, myLoc()) && !finishedRooms.contains(currentRoom) && roomNumber != 1) {
			if (castable(Spell.MAKE_GATESTONE)) {
				for (RSTile t : allDoors) {
					if (areaContains(targetRoom, t) && calc.distanceTo(t) > 4) {
						walkFast(t, 1);
						break;
					}
				}
				makeGatestone();
			}
		}
		goodDoors.removeAll(backDoors);
		goodDoors.removeAll(openedDoors);
		nearDoor = null;
		nearDoor2 = null;
		RSTile backDoor = null;
		boolean cantTele = false;
		if (npcs.getNearest(guardians) != null) {
			if (unBacktrackable.contains(targetRoom)) {
				if (!fightMonsters() || !pickUpAll())
					return;
			} else {
				cantTele = true;
				teleBack = false;
			}
		}
		if (targetRoom != null && !teleBack) {
			boolean autoSkip = cantTele || (skipRoom && npcs.getNearest(monster) != null);
			for (RSTile d : backDoors) {
				if (areaContains(targetRoom, d)) {
					if (autoSkip) {
						backDoor = d;
					} else {
						RSArea room = getNextRoom(d);
						if (room != null && areaEquals(room, startRoom) || roomContains(room, goodDoors))
							backDoor = d;
					}
					break;
				}
			}
		}
		getCurrentRoom();
		if (currentRoom != null && roomNumber != 1) {
			crossTheChasm(backDoor);
			o:while (areaEquals(currentRoom, targetRoom) && roomNumber != 1) {
				if (failSafe())
					break;
				if (backDoor == null) {
					if (npcs.getNearest(monster) != null) {
						log("Finishing off monsters before teleporting home");
						if (!fightMonsters() || !pickUpAll())
							return;
					} else if (npcs.getNearest(guardians) != null) {
						log("Bad monster found, backtracking.");
						backDoor = getBackDoor();
					} else teleHome(false);
				} else {
					RSObject door = objects.getTopAt(backDoor);
					if (door != null) {
						if (doObjAction(door, "Enter")) {
							if (combat.isAutoRetaliateEnabled() || !selectTab(Game.TAB_ATTACK, 3))
								sleep(600, 800);
							while (isMoving() && areaContains(targetRoom, myLoc())) {
								if (player().getInteracting() != null)
									continue o;
								sleep(100, 200);
							}
							sleep(300, 500);
						}
					} else backDoor = null;
				}
				sleep(100, 200);
				getCurrentRoom();
			}
		}
		if (bossRoom == null && roomNumber == 1) {
			updateLocks();
			if (!roomContains(startRoom, goodDoors) && !roomContains(startRoom, openedDoors)) {
				log.severe("No good doors remain at the start room.. Aborting dungeon");
				abortedCount++;
				if (floor == Floor.WARPED)
					waitForResponse();
				exit = true;
				return;
			}
		}
		if (npcs.getNearest(monster) == null)
			setRetaliate(true);
		oldRoom = targetRoom;
		setTargetRoom(currentRoom);
		explore = true;
		skipRoom = false;
	}

	private void startDungeon() {
		int c = 0;
		status = "Beginning a new dungeon!";
		idleTimer = new Timer(0);
		log("");
		RSObject exitLadder;
		if (Option.LOGOUT.enabled() && logoutTimer.getElapsed() > random(14400000, 21000000)) {
			status = "Logging out to reset the timer";
			log(PRP, "Voluntarily logging out to avoid the 6-hour timer");
			while (!game.logout(true)) {
				sleep(100, 300);
			}
			while (game.getClientState() != 10) {
				if (!authCheck)
					return;
				failLoginScreen();
				sleep(1000, 2000);
			}
			welcomeBack = false;
			logoutTimer = new Timer(0);
		}
		newDungeon = false;
		while ((exitLadder = objects.getNearest(EXIT_LADDERS)) == null || !inDungeon()) {
			if (failBasic())
				return;
			c++;
			if (inDungeon()) {
				if (!authCheck || !goodDungeon())
					return;
				if (c == 11)
					log(RED, "Exit ladder is null!");
			}
			if (c > 10) {
				inDungeon = false;
				return;
			}
			sleep(300, 600);
		}
		if (Option.RUSH.enabled()) {
			if (cProg < rushTo) {
				isRushing = true;
				if (aComplexity != rComplexity) {
					log(BLU, "Rushing enabled, exiting to update to your rush Complexity.");
					exit = true;
				}
			} else {
				isRushing = false;
				if (!Option.PRESTIGE.enabled()) {
					log(PRP, "Last rush floor reached with Prestiging disabled! Shutting down script..");
					authCheck = false;
				}
				if (aComplexity != complexity) {
					log(BLU, "Last rush floor reached, exiting to change to your main Complexity.");
					exit = true;
				}
			}
		}
		if (exit || !authCheck || !goodDungeon())
			return;
		log(BLK, "Starting a new dungeon");
		clearAll();
		walking.setRun(true);
		ridItem(primaryWep, "Wield");
		while (!getNewRoomArea()) {
			if (failSafe())
				return;
			if (developer)
				log.severe("Reattempting to define the startroom area.");
			sleep(500, 700);
		}
		getCurrentRoom();
		startRoom = currentRoom;
		RSComponent complexityMenu = interfaces.getComponent(519, 1);
		if (complexityMenu.isValid() && complexityMenu.getText().indexOf(":") > 0) {
			String menuText = complexityMenu.getText();
			String complexityText = "" + menuText.charAt(menuText.indexOf(":") - 1);
			if (Integer.parseInt(complexityText) < 7)
				aComplexity = Integer.parseInt(complexityText);
		} else if (inventory.contains(GGS) || groundItems.getNearest(GGS) != null) {
			aComplexity = 6;
		} else if (getObjInRoom("Summoning obelisk") != null) {
			aComplexity = 6;
		} else if (getObjInRoom("Spinning wheel") != null) {
			aComplexity = 4;
		} else if (getObjInRoom("Runecrafting altar") != null) {
			aComplexity = 3;
		} else if (getNpcInRoom(SMUGGLER) != null) {
			aComplexity = 1;
		}
		if (!Option.RUSH.enabled())
			complexity = aComplexity;
		switch (exitLadder.getID()) {
		case 51156:
			floor = Floor.FROZEN;
			if (Option.STYLE_SWAP.enabled()) {
				if (Melee.CRUSH.enabled()) {
					attackMode = Melee.CRUSH.index();
				} else attackMode = valueOf(defaultMode);
			}
			break;
		case 50604:
			floor = Floor.ABANDONED;
			if (Option.STYLE_SWAP.enabled()) {
				if (Melee.SLASH.enabled()) {
					attackMode = Melee.SLASH.index();
				} else if (Melee.STAB.enabled()) {
					attackMode = Melee.STAB.index();
				} else attackMode = valueOf(defaultMode);
			}
			break;
		case 51704:
			floor = Floor.FURNISHED;
			if (Option.STYLE_SWAP.enabled()) {
				if (Melee.SLASH.enabled()) {
					attackMode = Melee.SLASH.index();
				} else if (Melee.CRUSH.enabled()) {
					attackMode = Melee.CRUSH.index();
				} else attackMode = valueOf(defaultMode);
			}
			break;
		case 54675:
			floor = Floor.OCCULT;
			if (Option.STYLE_SWAP.enabled()) {
				if (Melee.SLASH.enabled()) {
					attackMode = Melee.SLASH.index();
				} else if (Melee.STAB.enabled()) {
					attackMode = Melee.STAB.index();
				} else attackMode = valueOf(defaultMode);
			}
			break;
		case 56149:
			floor = Floor.WARPED;
			if (Option.STYLE_SWAP.enabled()) {
				if (Melee.SLASH.enabled()) {
					attackMode = Melee.SLASH.index();
				} else attackMode = valueOf(defaultMode);
			}
			break;
		default:
			log(RED, "Unable to detect the floor type, returning to a lower floor.");
			exit = true;
			return;
		}
		runSettings();
		if (!authCheck)
			return;
		getNewDoorTiles();
		if (skipDoorFound) {
			log(RED, "A skipped door type is in the start room, trying another dungeon.");
			exit = true;
			return;
		}
		idleTimer = new Timer(0);
		if (failSafe())
			return;
		if (!Option.RUSH.enabled() && complexity != aComplexity)
			log(RED, "Complexity mismatch - selected: " + complexity + ", actual: " + aComplexity);
		if (!Option.STYLE_SWAP.enabled())
			attackMode = valueOf(defaultMode);
		dungTimer = new Timer(0);
		threadPitch(100);
		if (aComplexity < 5)
			dropAllExceptSaves();
		setRetaliate(true);
		selectTab(Game.TAB_INVENTORY, 3);
		exploreRoom();
		if (Option.SECONDARY_RANGE.enabled() && secondaryWep < 1 && arrows > 0 && floor == Floor.OCCULT) {
			int bowId = getItemID("longbow") > 0 ? getItemID("longbow") : getItemID("shortbow");
			if (bowId > 0) {
				log(BLU, "Enabling secondary range with bow ID: " + bowId + " for this Occult floor.");
				secondaryWep = bowId;
				bounds.add((Integer) bowId);
				Style.RANGE.set(true);
			}
		}
		if (aComplexity > 4) {
			if (!inventory.containsAll(TOOLS)) {
				secondaryStatus = "Purchasing a toolkit";
				o:while (!inventory.containsAll(TOOLS)) {
					if (failSafe())
						return;
					if (inventory.contains(TOOLKIT)) {
						makeSpace(true);
						inventory.getItem(TOOLKIT).doClick(true);
						for (int i = 0; i < 10; i++) {
							if (inventory.containsAll(TOOLS))
								break o;
							sleep(100, 130);
						}
					} else if (!shopToolkit()) {
						if (exit)
							return;
						sleep(200, 400);
					}
				}
			}
			secondaryStatus = "";
			ridItem(primaryWep, "Wield");
		}
	}

	final static int[] RUNEBOUND_ACTIVES = { 53977, 53979, 53978 };
	final static int[] GRAVE_PADS = { 54444, 54450, 54458 };
	final static int[] BLINK_PILLARS = { 32184, 32196, 32202, 32195, 32201, 32231 };

	final static int BEHEMOTH_CARCASS = 49283;
	final int[] ACTIVE_STONES = { 49278, 49279, 49276, 49277, 49274, 49275 };
	final int[] BLUE_STONES = { 49278, 49279 }, GREEN_STONES = { 49276, 49277 }, RED_STONES = { 49274, 49275 };
	final int[][] ALL_STONES = { BLUE_STONES, GREEN_STONES, RED_STONES };
	final int[] K_SPEC_ANIMS = { 13426, 13427, 13428, 13429 }, K_START_ANIMS = { 13427, 13429 };
	final int BEHEMOTH_WALL = 51180, BLOCK_ROCKS = 49268, FALLING_ROCKS = 49269;

	final int[] RAISED_PILLARS = { 32184, 32196, 32202 };
	final int[] SUNKEN_PILLARS = { 32195, 32201, 32231 };

	private void frozenBoss() {
		RSObject carcass = getObjInRoom(BEHEMOTH_CARCASS);
		if (carcass != null) {
			RSNPC behemoth = getNpcInRoom("Gluttonous behemoth");
			setBoss("Gluttonous Behemoth", true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			setRetaliate(true);
			safeTile = carcass.getLocation();
			RSTile wall = getNearestObjTo(safeTile, BEHEMOTH_WALL);
			if (wall != null) {
				for (RSTile t : getAdjacentTo(safeTile)) {
					int wDist = (int) calc.distanceBetween(t, wall);
					if (wDist == 3) {
						safeTile = t;
						break;
					}
				}
			}
			walkToMap(safeTile, 0);
			setPrayer(true, Style.RANGE, true);
			updateFightMode();
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (behemoth != null && behemoth.getAnimation() == 13720) {
					secondaryStatus = "Waiting for the behemoth to eat";
					while (behemoth != null && behemoth.getAnimation() == 13720) {
						topUp(true);
						sleep(300, 500);
					}
					secondaryStatus = "";
				}
				if (calc.distanceTo(safeTile) > 1) {
					secondaryStatus = "Repositioning to guard the food";
					if (!isMoving())
						walkTo(safeTile, 0);
					waitToStop(false);
					attackBoss(behemoth, true);
					secondaryStatus = "";
				}
				eatFood(topFoods, 50, 60);
				sleep(200, 300);
			}
		} else if (bossName.equals("Astea Frostweb") || getNpcInRoom("Frostweb") != null) {
			RSNPC frostWeb;
			setBoss("Astea Frostweb", false);
			boolean invincible = false;
			setPrayer(true, Style.MAGIC, true);
			swapStyle(Style.RANGE);
			tempMode = Melee.SLASH;
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((frostWeb = getNpcInRoom("Frostweb")) != null) {
					int style = combatStyle == Style.MELEE ? 0 : combatStyle == Style.RANGE ? 2 : 1;
					invincible = isProtecting(frostWeb, 9964, style);
					secondaryStatus = invincible ? "Astea is protecting against us!" : "";
					if (lastMessage.contains("A magical force")) {
						lastMessage = "";
						topUp(true);
					} else if (unreachable) {
						if (combatStyle == Style.MELEE && !isEdgeTile(frostWeb.getLocation())) {
							RSNPC spider = getNpcInRoom("spider");
							if (spider != null) {
								if (attackNpc(spider))
									sleep(500, 900);
							} else topUp(true);
						} else topUp(true);
						unreachable = false;
					} else if (invincible) {
						if (!swapStyle(true, Style.RANGE, Style.MELEE)) {
							RSNPC spider = getNpcInRoom("spider");
							if (spider != null) {
								if (attackNpc(spider))
									sleep(500, 800);
							} else if (!topUp(false) && combatStyle == Style.MELEE && calc.distanceTo

(frostWeb) > 1) {
								attackBoss(frostWeb, false);
							}
						}
						unreachable = false;
					} else attackBoss(frostWeb, false);
					eatFood(topFoods, 50, 55);
				}
				sleep(200, 300);
			}
		} else if (bossName.equals("Icy Bones") || getNpcInRoom("Bones") != null) {
			setBoss("Icy Bones", false);
			setPrayer(true, Style.MELEE, true);
			tempMode = Melee.CRUSH;
			if (!genericBossFight())
				return;
		} else if (bossName.equals("Luminescent Icefiend") || getNpcInRoom("Luminescent") != null) {
			RSNPC iceFiend;
			setBoss("Luminescent Icefiend", true);
			RSTile oldCorner = null;
			setPrayer(false, Style.RANGE, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((iceFiend = getNpcInRoom("Luminescent")) != null) {
					if (iceFiend.getAnimation() == 13338) {
						RSObject corner = getObjInRoom(51300);
						if (corner == null || walking.getEnergy() < random(8, 13)) {
							secondaryStatus = corner != null ? "Out of run.. Eating after the attack" : 

"Eating after the attack";
							while (iceFiend.getAnimation() == 13338) {
								sleep(100, 200);
							}
						} else {
							secondaryStatus = "Dodging the icicles!";
							RSObject next = getNextObj(corner, 51300);
							if (next != null)
								oldCorner = next.getLocation();
							if (!walking.isRunEnabled()) {
								walking.setRun(true);
							} else sleep(400, 600);
							safeTile = corner.getLocation();
							sleep(300, 400);
							walkTo(safeTile, 1);
							int dist = calc.distanceTo(safeTile);
							sleep(dist * 100, dist * 150);
							while (iceFiend != null && iceFiend.getAnimation() == 13338 && areaContains

(bossRoom, myLoc())) {
								if (failSafe())
									return;
								if (safeTile != null) {
									walkToMap(safeTile, 1);
									waitToStart(true);
									idleTimer = new Timer(0);
									while (calc.distanceBetween(myLoc(), safeTile) > 8.5) {
										if (failCheck())
											break;
										if (!isMoving() || player().getAnimation() != -1) {
											walkToMap(safeTile, 1);
											if (!isMoving())
												sleep(400, 600);
											sleep(500, 800);
										}
										sleep(30, 50);
									}
								}
								if (iceFiend.getAnimation() != 13338)
									break;
								for (RSObject o : getObjsInRoom(51300)) {
									if (o != null) {
										RSTile oTile = o.getLocation();
										if (oldCorner != null && oTile.equals(oldCorner))
											continue;
										if (safeTile == null || (!oTile.equals(safeTile) && 

(oTile.getX() == safeTile.getX() || oTile.getY() == safeTile.getY()))) {
											oldCorner = safeTile;
											safeTile = oTile;
											break;
										}
									}
								}
							}
							eatFood(topFoods, 15, 15);
							if (iceFiend != null)
								walkTo(iceFiend.getLocation(), 1);
							safeTile = null;
						}
						secondaryStatus = "";
					}
					attackBoss(iceFiend, false);
					eatFood(topFoods, 40, 40);
				}
				sleep(200, 300);
			}
		} else if (bossName.equals("Plane-Freezer Lakhrahnaz") || getNpcInRoom("Lakhrahnaz") != null) {
			RSNPC planeFreezer;
			setBoss("Plane-Freezer Lakhrahnaz", false);
			swapStyle(Style.RANGE);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			setPrayer(false, Style.RANGE, true);
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((planeFreezer = getNpcInRoom("Lakhrahnaz")) != null) {
					if (unreachable || (combatStyle == Style.MELEE && calc.distanceTo(planeFreezer) > 2)) {
						if (!isMoving()) {
							walkTo(planeFreezer.getLocation(), 0);
							unreachable = false;
						}
					}
					RSObject corner = getObjInRoom(SNOWY_CORNER);
					if (!isAttacking(planeFreezer) && calc.distanceTo(planeFreezer) < 3 && !isMoving() && corner 

!= null && calc.distanceTo(corner) < 3) {
						walkTo(myLoc(), 2);
						waitToStart(false);
					}
					attackBoss(planeFreezer, true);
					eatFood(topFoods, 45, 55);
				}
				sleep(100, 200);
			}
			RSObject finishedLadder = objects.getNearest(FINISHED_LADDERS);
			if (finishedLadder != null) {
				RSObject corner;
				RSTile finishTile = finishedLadder.getLocation();
				walkToMap(finishTile, 1);
				waitToEat(false);
				secondaryStatus = "Navigating to the nearest corner";
				while ((corner = getObjInRoom(SNOWY_STOPS)) != null && !myLoc().equals(safeTile = corner.getLocation

()) && calc.distanceTo(finishTile) > 1) {
					if (failSafe())
						return;
					if (!isMoving())
						walkTo(safeTile, 0);
					if (!isMoving())
						walkTo(myLoc(), 5);
					sleep(300, 400);
				}
				walkToMap(safeTile = finishTile, 0);
			}
		} else if (bossName.equals("Blood Chiller") || getNpcInRoom("Bloodchiller") != null) {
			setBoss("Blood Chiller", true);
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			if (!genericBossFight())
				return;
		} else if (!unknownBossFight()) {
			return;
		}
		finish = true;
	}

	private void abandonedBoss() {
		if (bossName.equals("Skeletal Hoarde") || getNpcInRoom(SKINWEAVER) != null) {
			RSNPC skinweaver = getNpcInRoom(SKINWEAVER);
			setBoss("Skeletal Hoarde", true);
			boolean tunnelReady = false;
			RSObject tunnel = null;
			setRetaliate(combatStyle !=Style.MELEE);
			setPrayer(false, Style.RANGE, true);
			tempMode = Melee.CRUSH;
			updateFightMode();
			selectTab(Game.TAB_INVENTORY, 3);
			while (getObjInRoom(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (skinweaver != null) {
					String skinMsg = skinweaver.getMessage();
					if (skinMsg != null && !skinMsg.contains("Chat later"))
						tunnelReady = true;
					tunnel = getObjInRoom("Tunnel");
					if (tunnelReady && tunnel != null) {
						secondaryStatus = "Blocking the next tunnel!";
						safeTile = tunnel.getLocation();
						lastMessage = "";
						doObjAction(tunnel, "Block");
						waitToStart(false);
						while (tunnel != null) {
							if (failSafe())
								return;
							if (lastMessage.contains("fully powered"))
								break;
							if (calc.distanceTo(safeTile) > 4)
								walkTo(safeTile, 1);
							if (doObjAction(tunnel, "Block"))
								waitToObject(false);
							eatFood(topFoods, 20, 20);
							sleep(50, 100);
							if (!getName(tunnel = objects.getTopAt(safeTile)).equals("Tunnel"))
								break;
						}
						secondaryStatus = "";
						if (!lastMessage.contains("fully powered")) {
							if (bossStage < 5)
								bossStage++;
							status = "Skeletal Hoarde: " + bossStage + " of 5 blocked";
						}
						lastMessage = "";
						safeTile = null;
						if (combat.getHealth() > 40)
							doNpcAction(npcs.getNearest(monster), "Attack");
						tunnelReady = false;
					}
					if (tunnel == null && npcs.getNearest(guardians) == null) {
						doNpcAction(skinweaver, "Talk-To");
						sleep(700, 1100);
					} else if (combat.getHealth() < 30 + npcs.getAll(guardians).length * 3 + aComplexity + 

(calc.distanceTo(skinweaver) / 3)) {
						safeTile = skinweaver.getLocation();
						while (combat.getHealth() < 95 && getHpPercent(player()) < 95) {
							if (failSafe())
								return;
							skinMsg = skinweaver.getMessage();
							if (skinMsg != null && !skinMsg.contains("Chat later"))
								tunnelReady = true;
							nearMonster = npcs.getNearest(monster);
							eatFood(topFoods, 20, 20);
							if (calc.distanceTo(skinweaver) > 3 || myLoc().equals(safeTile)) {
								walkAdjacentTo(safeTile, 2);
							} else if (nearMonster != null && calc.distanceBetween(safeTile, 

nearMonster.getLocation()) < 5) {
								attackBoss(nearMonster, false);
							}
							sleep(300, 600);
						}
						sleep(1400, 1800);
						safeTile = null;
					}
				} else skinweaver = getNpcInRoom(SKINWEAVER);
				attackBoss(npcs.getNearest(monster), false);
				eatFood(topFoods, 20, 20);
				sleep(200, 400);
			}
		} else if (bossName.equals("Hobgoblin Geomancer") || getNpcInRoom("Geomancer") != null) {
			setBoss("Hobgoblin Geomancer", true);
			setPrayer(true, null, false);
			tempMode = Melee.SLASH;
			if (!genericBossFight())
				return;
		} else if (bossName.equals("Bulwark Beast") || getNpcInRoom("Bulwark beast") != null) {
			boolean pickWielded = false;
			setBoss("Bulwark Beast", true);
			RSNPC bulwark = getNpcInRoom("Bulwark beast");
			if (bulwark != null) {
				if (bossID < 1)
					bossID = bulwark.getID();
				status = "Killing the Bulwark Beast" + (bulwark.getID() == bossID ? " (armored)" : "!");
			}
			setPrayer(combatStyle == Style.MAGIC, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((bulwark = getNpcInRoom("Bulwark beast")) != null && combatStyle != Style.MAGIC) {
					RSItem pWeapon = inventory.getItem(primaryWep);
					if (bulwark.getID() == bossID) {
						RSItem pickAxe = inventory.getItem(PICKAXE);
						if (!pickWielded && pickAxe == null) {
							if (equipment.containsAll(PICKAXE)) {
								pickWielded = true;
							} else {
								secondaryStatus = "Pulling a pickaxe from the stone";
								setRetaliate(false);
								RSObject pickRock = getObjInRoom(49295);
								while (pickRock != null && !inventory.contains(PICKAXE)) {
									if (isDead())
										break;
									doObjAction(pickRock, "Take-pickaxe");
									sleep(500, 800);
								}
								secondaryStatus = "";
							}
						}
						if (pickAxe != null && pWeapon == null) {
							if (ridItem(PICKAXE, "Wield") || inventory.contains(primaryWep))
								pickWielded = true;
						}
					} else if (pWeapon != null) {
						status = "Killing the Bulwark Beast!";
						ridItem(primaryWep, "Wield");
					}
				}
				if (attackBoss(bulwark, true))
					setRetaliate(true);
				eatFood(topFoods, 50, 60);
				sleep(200, 300);
			}
		} else if (bossName.equals("Unholy Cursebearer") || getNpcInRoom("cursebearer") != null) {
			setBoss("Unholy Cursebearer", true);
			setPrayer(true, Style.MELEE, true);
			tempMode = Melee.CRUSH;
			if (!genericBossFight())
				return;
		} else if (bossName.equals("Shadow-Forger Ihlakhizan") || getNpcInRoom("Ihlakhizan") != null) {
			RSNPC ihlakhizan = getNpcInRoom("Ihlakhizan");
			setBoss("Shadow-Forger", true);
			setPrayer(true, combatStyle == Style.MELEE ? Style.MELEE : Style.MAGIC, true);
			tempMode = Melee.STAB;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (ihlakhizan != null) {
					if (ihlakhizan.getAnimation() == 13030) {
						if (safeTile == null || calc.distanceTo(safeTile) > 3) {
							RSObject pillar = getObjInRoom(51110), corner = getObjInRoom(50749);
							if (pillar != null && corner != null) {
								int pX = pillar.getLocation().getX(), pY = pillar.getLocation().getY

();
								int cX = corner.getLocation().getX(), cY = corner.getLocation().getY

();
								safeTile = new RSTile(pX + (cX - pX) / 4, pY + (cY - pY) / 4);
							}
						}
						walkTo(safeTile, random(0, 2));
						sleep(100, 600);
						while (ihlakhizan.getAnimation() == 13030 && !isDead()) {
							sleep(200, 400);
							if (calc.distanceTo(safeTile) > 1 && walkTo(safeTile, 1))
								sleep(600, 800);
						}
					} else {
						attackBoss(ihlakhizan, true);
						if (!roomSwitch && ihlakhizan.getAnimation() != 13030)
							eatFood(topFoods, 50, 0);
					}
				}
				sleep(100, 300);
			}
		} else if (bossName.equals("Bal'lak the Pummeller") || getNpcInRoom("Pummeller") != null) {
			RSNPC pummeller = getNpcInRoom("Pummeller");
			setBoss("Bal'lak the Pummeller", false);
			int maxDef = random(80, 120);
			setPrayer(true, Style.MELEE, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (pummeller != null) {
					RSObject slime = getObjInRoom(49298);
					if (slime != null && calc.distanceTo(slime) < 3) {
						if (!isMoving())
							walkToMap(pummeller.getLocation(), 1);
						waitToStart(false);
					} else if (interfaces.getComponent(945, 17).getRelativeX() < maxDef) {
						eatFood(topFoods, 50, 60);
					} else if (!inventory.contains(GGS) && getItemInRoom(bossRoom, GGS) == null) {
						secondaryStatus = "Teleporting out, degenerating defense";
						if (teleTo(GGS)) {
							omNomNom();
							if (readyToCook()) {
								teleHome(true);
								return;
							}
							defenseDegenerate();
							unBacktrack(true);
						}
						secondaryStatus = "";
					}
					attackBoss(pummeller, true);
				}
				sleep(100, 200);
			}
		} else if (!unknownBossFight()) {
			return;
		}
		finish = true;
	}

	private void furnishedBoss() {
		if (bossName.equals("Rammernaut") || getNpcInRoom("Rammernaut") != null) {
			RSNPC rammernaut = getNpcInRoom("Rammernaut");
			setBoss("Rammernaut", false);
			swapStyle(Style.MAGIC);
			tempMode = Melee.STAB;
			prayTimer = new Timer(random(4000, 6000));
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (rammernaut != null) {
					String shout = rammernaut.getMessage();
					if (shout != null && shout.contains("CHAA")) {
						secondaryStatus = "Running for cover!";
						safeTile = safeCorner(rammernaut.getLocation());
						if (safeTile != null) {
							walkToMap(safeTile, 0);
							for (int i = 0; i < 7 && !isDead(); i++) {
								if (rammernaut != null && rammernaut.getMessage() == null)
									break;
								if (!isMoving() && calc.distanceTo(safeTile) < 3 && calc.distanceTo

(rammernaut) < 3)
									break;
								if (!topUp(true))
									sleep(400, 600);
							}
							for (int i = 0; i < 20 && !isDead(); i++) {
								if (rammernaut != null) {
									String msg = rammernaut.getMessage();
									if (msg != null && msg.contains("Oo"))
										break;
								}
								if (calc.distanceTo(safeTile) < 3 && calc.distanceTo(rammernaut) < 4)
									break;
								sleep(200, 300);
							}
							if (failSafe())
								return;
							if (rammernaut != null && rammernaut.getMessage() == null) {
								walkToScreen(currentRoom.getCentralTile());
								sleep(600, 1000);
							}
						}
						secondaryStatus = "";
					} else if (rammernaut.getAnimation() == 13705) {
						topUp(true);
					} else {
						RSObject corner = getObjInRoom(51762);
						if (rammernaut.getMessage() == null && (isEdgeTile(myLoc()) || (corner != null && 

calc.distanceTo(corner) < 4))) {
							walkToScreen(currentRoom.getCentralTile());
							sleep(400, 800);
						}
						setPrayer(true, Style.MELEE, true);
						attackBoss(rammernaut, true);
						if (rammernaut.getMessage() != null)
							continue;
						eatFood(topFoods, 50, 20);
					}
				}
				sleep(100, 200);
			}
		} else if (bossName.equals("Stomp") || getNpcInRoom("Stomp") != null) {
			RSNPC stomp = getNpcInRoom("Stomp");
			ArrayList<RSTile> stoneChecks = new ArrayList<RSTile>();
			int dX = 0, dY = 0, stoneCount = random(10, 16);
			if (setBoss("Stomp", false)) {
				bounds.add((Integer) 15750);
				bounds.add((Integer) 15751);
				bounds.add((Integer) 15752);
			}
			tempMode = Melee.STAB;
			if (stomp != null) {
				RSTile backDoor = getBackDoor(), bossTile = stomp.getLocation();
				walkFast(bossTile, 2);
				setPrayer(true, null, false);
				if (backDoor != null) {
					if (backDoor.getX() - bossTile.getX() > 3) {
						dX = 1;
					} else if (backDoor.getX() - bossTile.getX() < -3) {
						dX = -1;
					}
					if (backDoor.getY() - bossTile.getY() > 3) {
						dY = 1;
					} else if (backDoor.getY() - bossTile.getY() < -3) {
						dY = -1;
					}
				}
				for (RSObject o : getObjsInRoom("lodestone")) {
					if (o != null) {
						RSTile oT = o.getLocation();
						stoneChecks.add(new RSTile(oT.getX() + dX, oT.getY() + dY));
					}
				}
			}
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (getObjInRoom(ACTIVE_STONES) != null) {
					boolean pickup = true;
					int reachCount = 0;
					Timer stoneTimer = new Timer(random(14000, 15000));
					badTiles.clear();
					stoneCount = random(10, 15);
					sleep(500, 1000);
					for (int I = 0; I < ALL_STONES.length; I++) {
						if (objects.getNearest(ALL_STONES[I]) != null) {
							bossStage = 1 + I;
							break;
						}
					}
					if (bossStage == 0)
						bossStage = 1;
					final int CRYSTAL_ID = 15749 + bossStage;
					status = "Killing Stomp (stage " + bossStage + " of 3)";
					int newCount = 0, oldCount = 0;
					for (RSTile t : stoneChecks) {
						RSObject blockObj = objects.getTopAt(t);
						if (blockObj != null) {
							if (blockObj.getID() == FALLING_ROCKS) {
								newCount++;
							} else if (blockObj.getID() == BLOCK_ROCKS) {
								oldCount++;
							}
						}
					}
					if (newCount == 1 && oldCount == 0) {
						for (RSTile t : stoneChecks) {
							if (calc.distanceTo(t) < 5) {
								RSObject blockObj = objects.getTopAt(t);
								if (blockObj != null && blockObj.getID() == FALLING_ROCKS) {
									walkTo(t, 0);
									break;
								}
							}
						}
					}
					setPrayersOff();
					lastMessage = "";
					for (RSTile t : stoneChecks) {
						if (myLoc().equals(t) || objects.getTopAt(t) == null)
							reachCount++;
					}
					while (getObjInRoom(ACTIVE_STONES) != null) {
						if (failSafe())
							return;
						int crystalsNeeded = 2;
						unreachable = false;
						if (reachCount == 2) {
							RSObject[] actives = getObjsInRoom(ACTIVE_STONES);
							crystalsNeeded = actives != null ? actives.length : 0;
						}
						if (inventory.getCount(CRYSTAL_ID) < crystalsNeeded) {
							RSGroundItem crystal = getRoomItem(stompCrystal);
							if (crystal != null) {
								if (pickup && pickUpItem(crystal))
									waitToStop(false);
								if (unreachable)
									badTiles.add(crystal.getLocation());
							} else if (!topUp(true) && (stoneTimer == null || !stoneTimer.isRunning()) && 

!lastMessage.contains("currently invulnerable")) {
								if (doNpcAction(stomp, "Attack"))
									waitToStop(false);
							}
						} else if (reachCount == 2 && stoneTimer != null && stoneTimer.isRunning()) {
							for (RSTile t : stoneChecks) {
								if (!myLoc().equals(t) && objects.getTopAt(t) != null) {
									stoneTimer = null;
									break;
								}
							}
							if (stoneTimer != null) {
								RSObject nextStone = getObjInRoom(ACTIVE_STONES);
								if (nextStone != null) {
									safeTile = nextStone.getLocation();
									if (doObjAction(nextStone, "Charge")) {
										waitToStop(false);
										if (unreachable) {
											reachCount--;
											safeTile = null;
										} else if (stoneTimer != null) {
											stoneTimer.reset();
										}
									}
								}
							}
						} else if (!topUp(true) && (stoneTimer == null || !stoneTimer.isRunning()) && !

lastMessage.contains("currently invulnerable")) {
							if (doNpcAction(stomp, "Attack"))
								waitToStop(true);
						}
						sleep(200, 300);
					}
					safeTile = null;
					badTiles.clear();
					setPrayer(true, null, false);
				} else if (stomp != null && bossStage == 3 && stomp.getAnimation() == 13449) {
					for (RSTile t : stoneChecks) {
						if (calc.distanceTo(t) > 2) {
							walkTo(t, 1);
							topUp(true);
							sleep(600, 900);
							break;
						}
					}
				} else {
					boolean blocked = false;
					for (RSTile t : stoneChecks) {
						if (objects.getTopAt(t) != null) {
							blocked = true;
							break;
						}
					}
					if (blocked || getObjsInRoom(BLOCK_ROCKS).length > stoneCount) {
						lastMessage = "";
						setRetaliate(combatStyle == Style.MELEE);
						RSObject block = getObjInRoom(BLOCK_ROCKS);
						if (block != null) {
							safeTile = block.getLocation();
							if (!isMoving() && (calc.distanceTo(safeTile) > 1 || isAttacking(stomp))) {
								walkTo(safeTile, 1);
							} else topUp(true);
						}
					} else {
						eatFood(topFoods, 50, 50);
						setRetaliate(true);
						if (!lastMessage.contains("currently invulnerable")) {
							attackBoss(stomp, false);
							safeTile = null;
						} else topUp(true);
					}
				}
				sleep(200, 300);
			}
		} else if (bossName.equals("Har'laak the Riftsplitter") || getNpcInRoom("Riftsplitter") != null) {
			RSNPC riftsplitter = getNpcInRoom("Riftsplitter");
			boolean newShout = false;
			String shout = null;
			ArrayList<RSArea> portals = new ArrayList<RSArea>();
			ArrayList<RSTile> corners = new ArrayList<RSTile>();
			setBoss("Riftsplitter", true);
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (riftsplitter != null) {
					if (riftsplitter.getMessage() != null && shout != null && shout.equals

(riftsplitter.getMessage())) {
						newShout = false;
					} else {
						shout = riftsplitter.getMessage();
						newShout = true;
					}
					if (shout != null && newShout) {
						secondaryStatus = "Look out for that portal!";
						RSTile mT = myLoc();
						if (corners.isEmpty()) {
							portals.clear();
							portals.add(new RSArea(new RSTile(mT.getX() - 1, mT.getY() - 1), new RSTile

(mT.getX() + 1, mT.getY() + 1)));
							o:for (RSObject obj : getObjsInRoom(51866)) {
								RSTile oTile = obj.getLocation();
								for (RSArea portal : portals) {
									if (areaContains(portal, oTile))
										continue o;
								}
								corners.add(obj.getLocation());
							}
						} else portals.add(new RSArea(new RSTile(mT.getX() - 1, mT.getY() - 1), new RSTile

(mT.getX() + 1, mT.getY() + 1)));
						int dist = 20;
						if (safeTile == null) {
							for (RSTile corner : corners) {
								if (calc.distanceTo(corner) > 2 && calc.distanceTo(corner) < dist) {
									safeTile = corner;
									dist = calc.distanceTo(corner);
								}
							}
						}
						if (safeTile != null) {
							walkFast(safeTile, 0);
							corners.remove(safeTile);
							topUp(true);
							for (int c = 0; c < 10; c++) {
								if (isDead() || calc.distanceTo(safeTile) < 4)
									break;
								if (!isMoving()) {
									walkTo(safeTile, 0);
								} else if (!topUp(true)) {
									sleep(300, 500);
								}
							}
							safeTile = null;
						}
						secondaryStatus = "";
					} else {
						attackBoss(riftsplitter, true);
						eatFood(topFoods, 50, 20);
						if (calc.distanceTo(riftsplitter) < 3 && !player().isInCombat() && !

riftsplitter.isInCombat())
							walkToMap(currentRoom.getCentralTile(), 1);
					}
				}
				sleep(100, 200);
			}
		} else if (bossName.equals("Lexicus Runewright") || getNpcInRoom("Lexicus") != null) {
			RSNPC lexicus = getNpcInRoom("Lexicus");
			setBoss("Lexicus Runewright", false);
			String shout = "";
			setPrayer(false, Style.MAGIC, true);
			swapStyle(Style.RANGE);
			tempMode = Melee.SLASH;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (lexicus != null) {
					shout = lexicus.getMessage();
					if (shout != null && shout.contains("barrage")) {
						RSObject corner = objects.getNearest(bookCorner);
						if (corner != null) {
							safeTile = corner.getLocation();
							walkToMap(safeTile, 1);
							if (!topUp(true))
								sleep(500, 700);
							sleep(600, 800);
							doNpcAction(lexicus, "Attack");
							safeTile = null;
						}
						secondaryStatus = "";
					} else if (getNpcInRoom("Tome") != null) {
						if (smartSleep(attackNpc(getNpcInRoom("Tome")), false))
							eatFood(topFoods, 40, 20);
					} else if (lexicus.getAnimation() == 13499) {
						for (int i = 0; i < 15 && !isDead(); i++) {
							if (getNpcInRoom("Tome") != null)
								break;
							topUp(false);
							sleep(100, 200);
						}
					} else attackBoss(lexicus, false);
				}
				eatFood(topFoods, 40, 20);
				sleep(100, 200);
			}
		} else if (bossName.equals("Sagittare") || getObjInRoom(51887) != null) {
			RSNPC sagittare;
			setBoss("Sagittare", false);
			if (combatStyle == Style.RANGE) {
				if (swapAlternative()) {
					log("Swapped our attack style for Sagittare");
				} else {
					log.severe("We don't have a way to kill Sagittare, aborting dungeon!");
					exit = true;
					return;
				}
			}
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.SLASH;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((sagittare = getNpcInRoom("Sagittare")) != null) {
					if (lastMessage.contains("stops you") || lastMessage.contains("bound"))
						topUp(true);
					if (sagittare.getAnimation() == 13270 || sagittare.getMessage() != null) {
						if (calc.distanceTo(currentRoom.getCentralTile()) > 2) {
							safeTile = currentRoom.getCentralTile();
						} else {
							RSObject door = objects.getNearest(50352);
							safeTile = door != null ? door.getLocation() : sagittare.getLocation();
						}
						walkToMap(safeTile, 1);
						sleep(2500, 4000);
						safeTile = null;
					} else if (combatStyle != Style.MAGIC && calc.distanceTo(sagittare) > 1) {
						if (isMoving()) {
							topUp(false);
						} else walkToMap(sagittare.getLocation(), 1);
						RSTile dest = walking.getDestination();
						if (dest != null && calc.distanceBetween(dest, sagittare.getLocation()) > 1) {
							walkToMap(sagittare.getLocation(), 0);
							waitToStart(false);
						}
					}
					eatFood(topFoods, 50, 50);
					if (sagittare.getAnimation() != 8939 /*&& sagittare.getGraphic() != 1576*/)
						attackBoss(sagittare, false);
				}
				sleep(100, 200);
			}
		} else if (bossName.equals("Night-gazer Khighorahk") || getNpcInRoom("Khighorahk") != null) {
			RSNPC khighorahk = getNpcInRoom("Khighorahk");
			setBoss("Night-gazer", true);
			if (khighorahk != null && bossID < 1) {
				bossID = khighorahk.getID();
				bossStage = 1;
			}
			setPrayer(false, Style.MAGIC, true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			status = "Killing the Night-gazer (stage " + bossStage + " of 2)";
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((khighorahk = getNpcInRoom("Khighorahk")) != null) {
					if (intMatch(khighorahk.getAnimation(), K_SPEC_ANIMS) && (isMoving() || calc.distanceTo

(khighorahk.getLocation()) < 6)) {
						safeTile = safePillar(khighorahk.getLocation());
						RSObject pillar = getObjInRoom(49265);
						if (safeTile != null) {
							walkToMap(safeTile, 1);
							waitToStart(false);
							RSTile bossTile = khighorahk.getLocation();
							for (int i = 0; i < 7 && !isDead(); i++) {
								if (pillar != null && calc.distanceTo(pillar) < 4) {
									if (doObjAction(pillar, "Light")) {
										waitToObject(false);
										if (player().getAnimation() != 13355 || player

().getAnimation() != 13354)
											doObjAction(pillar, "Light");
										break;
									}
								}
								if (player().getAnimation() == 10070) {
									topUp(true);
									break;
								}
								if (walking.isRunEnabled()) {
									sleep(200, 300);
								} else if (calc.distanceTo(safeTile) < 3 || calc.distanceTo(bossTile) 

> 7) {
									break;
								}
								if (calc.distanceBetween(myLoc(), bossTile) > 4 && !intMatch

(khighorahk.getAnimation(), K_START_ANIMS))
									break;
								sleep(200, 250);
								if (!intMatch(khighorahk.getAnimation(), K_SPEC_ANIMS))
									break;
							}
							for (int c = 0; c < 5 && !isDead(); c++) {
								if (intMatch(khighorahk.getAnimation(), K_START_ANIMS))
									break;
								if (attackBoss(khighorahk, true)) {
									waitToAnimate();
									break;
								}
								sleep(400, 600);
							}
							safeTile = null;
						}
					} else {
						RSObject[] litPillars = getObjsInRoom(49266, 49267);
						if (litPillars != null && litPillars.length < 2) {
							if (doObjAction(objects.getNearest(49265), "Light"))
								waitToObject(false);
						} else if (!intMatch(khighorahk.getAnimation(), K_START_ANIMS)) {
							eatFood(topFoods, 50, 20);
							if (!intMatch(khighorahk.getAnimation(), K_START_ANIMS))
								attackBoss(khighorahk, true);
							if (khighorahk.getID() != bossID) {
								bossID = khighorahk.getID();
								bossStage = 2;
								status = "Killing the Night-gazer (stage 2 of 2)";
							}
						}
					}
				}
				sleep(100, 200);
			}
		} else if (!unknownBossFight()) {
			return;
		}
		finish = true;
	}

	private void occultBoss() {
		if (bossName.equals("Skeletal Trio") || getNpcInRoom("Skeletal warrior") != null || getNpcInRoom("Skeletal sorcerer") 

!= null || getNpcInRoom("Skeletal archer") != null) {
			RSNPC meleer, archer, sorcerer, targ = null;
			status = "Killing the Skeletal Trio!";
			setBoss("Skeletal Trio", true);
			tempMode = Melee.CRUSH.enabled() ? Melee.CRUSH : Melee.SLASH;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				boolean mInvincible = true, aInvincible = true, sInvincible = true;
				int remaining = 0;
				if ((meleer = getNpcInRoom("Skeletal warrior")) != null) {
					mInvincible = isProtecting(meleer, 11939, combatStyle.index());
					remaining++;
				}
				if ((archer = getNpcInRoom("Skeletal archer")) != null) {
					aInvincible = isProtecting(archer, 12043, combatStyle.index());
					remaining++;
				}
				if ((sorcerer = getNpcInRoom("Skeletal sorcerer")) != null) {
					sInvincible = isProtecting(sorcerer, 11998, combatStyle.index());
					remaining++;
				}
				switch (combatStyle) {
				case MELEE:
					protection = sorcerer != null ? Style.MAGIC : archer != null ? Style.RANGE : Style.MELEE;
					targ = !aInvincible ? archer : !sInvincible ? sorcerer : !mInvincible ? meleer : null;
					break;
				case RANGE:
					protection = meleer != null? Style.MELEE : sorcerer != null ? Style.MAGIC : Style.RANGE;
					targ = !sInvincible ? sorcerer : !mInvincible ? meleer : !aInvincible ? archer : null;
					break;
				case MAGIC:
					protection = archer != null ? Style.RANGE : meleer != null ? Style.MELEE : Style.MAGIC;
					targ = !mInvincible ? meleer : !aInvincible ? archer : !sInvincible ? sorcerer : null;
					break;
				}
				setPrayer(true, protection, true);
				if (targ != null) {
					attackNpc(targ);
					if (!eatFood(topFoods, 50, 50))
						sleep(400, 600);
				} else if (remaining == 3 || !swapAlternative()) {
					topUp(true);
				}
				sleep(200, 300);
			}
		} else if (bossName.equals("Runebound Behemoth") || getObjInRoom(RUNEBOUND_ACTIVES) != null) {
			RSNPC runebound = getNpcInRoom("Runebound behemoth");
			setBoss("Runebound Behemoth", true);
			tempMode = Melee.CRUSH;
			if (bossID < 1 && runebound != null)
				bossID = runebound.getID();
			setPrayer(true, Style.MAGIC, true);
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (runebound != null) {
					if (runebound.getAnimation() == 14440 || runebound.getMessage() != null) {
						secondaryStatus = "Dodging the lightning!";
						RSObject corner = getObjInRoom(54786);
						walkToScreen(calc.distanceTo(corner) > 2 && !isEdgeTile(myLoc()) ? reflect

(runebound.getLocation(), myLoc(), .5) : bossRoom.getCentralTile());
						if (!topUp(true))
							sleep(400, 600);
						secondaryStatus = "";
					} else {
						int differential = bossID - runebound.getID();
						boolean meleeI = true, rangeI = true, magicI = true;
						if (differential == 15) {
							meleeI = false;
						} else if (differential == 30) {
							rangeI = false;
						} else if (differential == 60) {
							magicI = false;
						} else if (differential == -15) {
							meleeI = false;
							magicI = false;
						} else if (differential == -30) {
							rangeI = false;
							meleeI = false;
						} else if (differential == -45) {
							rangeI = false;
							magicI = false;
						}
						boolean[] invin = { meleeI, rangeI, magicI };
						if (!invin[combatStyle.index()]) {
							attackBoss(runebound, true);
							eatFood(topFoods, 30, 15);
						} else {
							eatFood(topFoods, 50, 20);
							RSObject styleActive = getObjInRoom(RUNEBOUND_ACTIVES[combatStyle.index()]);
							if (styleActive != null) {
								if (combat.getHealth() > random(8, 15) && doObjAction(styleActive, 

"Deactivate"))
									waitToStop(false);
							} else {
								for (int I = 0; I < 3; I++) {
									Style test = styleOf(I);
									if (combatStyle != test && test.enabled() && (!invin[I] || 

getObjInRoom(RUNEBOUND_ACTIVES[I]) != null)) {
										RSObject active = getObjInRoom(RUNEBOUND_ACTIVES[I]);
										if (active != null)
											walkFast(active.getLocation(), 1);
										swapStyle(test);
										break;
									}
									if (I == 2)
										topUp(true);
								}
							}
						}
					}
				}
				sleep(100, 200);
			}
		} else if (bossName.equals("Gravecreeper") || getObjInRoom(54447) != null) {
			RSNPC gravecreeper;
			setBoss("Gravecreeper", true);
			if (combatStyle == Style.RANGE)
				swapStyle(Style.MELEE);
			tempMode = Melee.SLASH.enabled() ? Melee.SLASH : Melee.CRUSH;
			setPrayer(true, combatStyle == Style.MELEE ? Style.MELEE : Style.RANGE, true);
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((gravecreeper = getNpcInRoom(bossName)) != null) {
					RSObject aGrave = getObjInRoom(GRAVE_PADS);
					String graveMessage = gravecreeper.getMessage();
					if (graveMessage != null) {
						if (graveMessage.equals("Burrrn")) {
							secondaryStatus = "Dodging the burn spots!";
							RSTile badTile = gravecreeper.getLocation();
							safeTile = safeGrave(badTile);
							if (safeTile != null && badTile != null) {
								for (int c = 0; c < 10; c++) {
									if (calc.distanceTo(badTile) < 2) {
										walkTo(safeTile, 1);
										c++;
									} else if (topUp(false)) {
										c++;
									}
									sleep(400, 500);
								}
							}
							secondaryStatus = "";
						} else if (graveMessage.contains("Buuu")) {
							omNomNom();
						} else if (graveMessage.contains("Diii")) {
							omNomNom();
						} else if (graveMessage.contains("Braa")) {
							omNomNom();
						}
					} else if (aGrave != null && calc.distanceTo(aGrave) < 2) {
						safeTile = safeGrave(gravecreeper.getLocation());
						if (safeTile != null) {
							walkTo(safeTile, 1);
						} else walkToScreen(bossRoom.getCentralTile());
						sleep(600, 1000);
					} else {
						attackBoss(gravecreeper, false);
						eatFood(topFoods, 50, 20);
					}
				} else topUp(true);
				sleep(100, 200);
			}
		} else if (bossName.equals("Necrolord") || getObjInRoom(54475) != null) {
			RSNPC necrolord = getNpcInRoom("Necrolord");
			setBoss("Necrolord", true);
			if (combatStyle == Style.MELEE && !swapAlternative()) {
				log.severe("We can't kill this boss with melee, aborting dungeon");
				abortedCount++;
				exit = true;
				return;
			}
			if (necrolord != null && calc.distanceTo(necrolord) > 3)
				walkToMap(necrolord.getLocation(), 1);
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				RSNPC minion = getNpcInRoom("Skeletal minion");
				setPrayer(true, minion != null && calc.distanceTo(minion) < 4 ? Style.MELEE : Style.MAGIC, true);
				if (combatStyle == Style.MELEE) {
					log.severe("We can't kill this boss with melee, aborting dungeon");
					exit = true;
					return;
				}
				if (unreachable) {
					if (!isAttacking(necrolord))
						topUp(true);
					unreachable = false;
				}
				attackBoss(necrolord, false);
				eatFood(topFoods, 50, 60);
				sleep(200, 400);
			}
		} else if (bossName.equals("Flesh-Spoiler Haasghenahk") || getNpcInRoom("Flesh-Spoiler") != null) {
			RSNPC fleshSpoiler = getNpcInRoom("Flesh-Spoiler");
			if (setBoss("Flesh-Spoiler", true)) {
				if (fleshSpoiler != null)
					bossID = fleshSpoiler.getID();
				bossStage = 1;
			}
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			setPrayer(true, null, false);
			status = "Killing the Flesh-Spoiler (Stage " + bossStage + " of 2)";
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((fleshSpoiler = getNpcInRoom(bossName)) != null) {
					if (Option.PRAY.enabled() && getNpcInRoom("spawn") != null)
						setPrayer(true, Style.MELEE, true);
					attackBoss(fleshSpoiler, true);
					eatFood(topFoods, 50, 40);
					if (fleshSpoiler.getID() != bossID) {
						status = "Killing the Flesh-Spoiler (Stage 2 of 2)";
						bossID = fleshSpoiler.getID();
						bossStage = 2;
					}
				}
				sleep(200, 300);
			}
		} else if (bossName.equals("Yk'Lagor the Thunderous") || getNpcInRoom("Yk'Lagor the Thunderous") != null) {
			RSNPC thunderous;
			setBoss("Yk'Lagor the Thunderous", false);
			roomSwitch = false;
			swapStyle(Style.MAGIC);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.SLASH;
			ArrayList<RSTile> spots = new ArrayList<RSTile>();
			for (RSObject p : getObjsInRoom(55057)) {
				for (RSTile t : getAdjacentTo(p.getLocation())) {
					if (isEdgeTile(t)) {
						spots.add(t);
						break;
					}
				}
			}
			if (getNpcInRoom("Mysterious mage") != null) {
				secondaryStatus = "Freeing Yk'Lagor from the mages";
				walkFast(bossRoom.getCentralTile(), 2);
				setRetaliate(false);
				setPrayer(false, Style.MAGIC, true);
				while (getNpcInRoom("Mysterious mage") != null) {
					if (failSafe())
						return;
					if (npcs.getAll(unInteracting).length <= 4) {
						safeTile = getNearestTileTo(myLoc(), spots);
						walkTo(safeTile, 0);
						waitToEat(false);
					} else {
						if (attackNpc(npcs.getNearest(unInteracting)))
							sleep(600, 800);
						eatFood(topFoods, 50, 50);
					}
				}
				secondaryStatus = "";
				if (myLoc().equals(safeTile))
					omNomNom();
				setRetaliate(true);
				selectTab(Game.TAB_INVENTORY, 3);
			}
			setPrayer(true, combatStyle == Style.MELEE ? Style.MELEE : Style.MAGIC, true);
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((thunderous = getNpcInRoom(bossName)) != null) {
					String shout = thunderous.getMessage();
					if (shout != null) {
						if (shout.startsWith("This is")) {
							secondaryStatus = "Taking cover from the quake!";
							safeTile = getNearestTileTo(myLoc(), spots);
							Timer quakeTimer = new Timer(random(3000, 3500));
							if (safeTile != null) {
								while (!myLoc().equals(safeTile) && quakeTimer.isRunning()) {
									if (failSafe())
										return;
									if (thunderous != null && thunderous.getAnimation() == -1)
										break;
									if (myLoc().equals(safeTile)) {
										topUp(true);
									} else if (!isMoving()) {
										walkTo(safeTile, 0);
									}
									sleep(100, 200);
								}
							}
						} else if (shout.startsWith("Come")) {
							secondaryStatus = "Dodging his grasp!";
							safeTile = getNearestTileTo(myLoc(), spots);
							Timer graspTimer = new Timer(random(4000, 5000));
							if (safeTile != null) {
								while (!roomSwitch && graspTimer.isRunning()) {
									if (failSafe())
										return;
									if (roomSwitch) {
										if (lastMessage.contains("stunned")) {
											log("Damn it.. Got sucked in.");
											sleep(4000, 6000);
										}
										break;
									}
									if (myLoc().equals(safeTile)) {
										topUp(true);
									} else if (walkTo(safeTile, 0)) {
										waitToEat(false);
									}
									sleep(100, 200);
								}
								roomSwitch = false;
							}
						}
						secondaryStatus = "";
					} else {
						if (!isMoving()) {
							RSTile safety = getNearestTileTo(myLoc(), spots);
							if (calc.distanceTo(safety) > (combatStyle == Style.MELEE ? 4 : 3)) {
								walkToScreen(safety);
								if (thunderous != null && thunderous.getMessage() == null)
									waitToStart(false);
							} else attackBoss(thunderous, true);
						} else attackBoss(thunderous, true);
						eatFood(topFoods, 50, 0);
					}
				}
				sleep(200, 400);
			}
		} else if (!unknownBossFight()) {
			return;
		}
		finish = true;
	}

	private void warpedBoss() {
		if (bossName.equals("Blink") || getObjInRoom(BLINK_PILLARS) != null) {
			RSNPC blink;
			setBoss("Blink", false);
			boolean respawn = true;
			RSTile center = bossRoom.getCentralTile();
			tempMode = Melee.STAB;
			if (combatStyle == Style.MAGIC)
				swapAlternative();
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((blink = getNpcInRoom(bossName)) == null) {
					RSObject pillar = getObjInRoom(RAISED_PILLARS);
					secondaryStatus = "";
					respawn = true;
					if (pillar == null) {
						if (doObjAction(getCloseObjTo(center, SUNKEN_PILLARS), "Raise"))
							waitToStop(false);
					} else {
						puzzlePoints.clear();
						topUp(false);
					}
				} else if (respawn || blink.isMoving() || !stringMatch("Attack", blink.getActions())) {
					if (respawn) {
						RSObject pillar = getObjInRoom(RAISED_PILLARS);
						boolean aligned = false;
						safeTile = blink.getLocation();
						if (pillar != null) {
							RSTile pTile = pillar.getLocation();
							aligned = safeTile.getX() == pTile.getX() || safeTile.getY() == pTile.getY();
						}
						puzzlePoints.clear();
						if (aligned) {
							secondaryStatus = "Got him! Time to dish the pain";
							for (int c = 0; c < 10; c++) {
								if (blink == null || !blink.isMoving() || isDead())
									break;
								sleep(200, 400);
							}
						} else blinkPath(blink);
						respawn = false;
					}
					if (!puzzlePoints.isEmpty()) {
						RSTile near = getNearestTileTo(myLoc(), puzzlePoints);
						if (near != null && calc.distanceTo(near) < 5 && walkToMap(reflect(near, myLoc(), 1), 

1)) {
							puzzlePoints.clear();
							topUp(false);
						}
					} else topUp(true);
				} else {
					attackBoss(blink, false);
					eatFood(topFoods, 40, 0);
				}
				sleep(100, 300);
			}
			puzzlePoints.clear();
		} else if (bossName.equals("Warped gulega") || getNpcInRoom("Warped gulega") != null) {
			RSNPC gulega = getNpcInRoom("Warped gulega");
			setBoss("Warped Gulega", true);
			setPrayer(true, Style.MAGIC, true);
			tempMode = Melee.SLASH;
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (gulega != null) {
					int gAnim = gulega.getAnimation();
					if (gAnim != -1) {
						if (gAnim == 15004) {
							RSTile start = myLoc();
							Timer graphicTimer = new Timer(random(2000, 2500));
							secondaryStatus = "Analyzing Boss attack data...";
							while (gulega.getAnimation() == 15004 && calc.distanceTo(start) < 1) {
								if (/*gulega.getGraphic() == 2877 &&*/ !graphicTimer.isRunning()) {
									secondaryStatus = "Special attack detected - Ruuun!";
									if (walkToMap(start, 2))
										waitToStart(false);
									walkAdjacentTo(start, 2);
									while (gulega != null && !failBasic() && gulega.getAnimation() 

== 15004) {
										attackBoss(gulega, true);
										sleep(300, 600);
									}
									secondaryStatus = "";
									break;
								}
								sleep(50, 100);
							}
							secondaryStatus = "";
						} else if (developer) {
							if (gAnim == 15001) {
							} else if (gAnim == 15007) {
							}
						}
					} else {
						eatFood(topFoods, 50, 0);
						if (gulega != null && gulega.getAnimation() == -1)
							attackBoss(gulega, true);
					}
				}
				sleep(100, 200);
			}
		} else if (bossName.equals("Dreadnaut") || getNpcInRoom("Dreadnaut") != null) {
			RSNPC dreadnaut = getNpcInRoom("Dreadnaut");
			setBoss("Dreadnaut", false);
			swapStyle(Style.MAGIC);
			setPrayer(true, Style.MELEE, true);
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				eatFood(topFoods, 50, 60);
				attackBoss(dreadnaut, true);
				setPrayer(true, Style.MELEE, true);
				sleep(200, 400);
			}
		} else if (bossName.equals("Hope Devourer") || getNpcInRoom("Hope devourer") != null) {
			RSNPC devourer = getNpcInRoom("Hope devourer");
			setBoss("Hope Devourer", true);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			prayTimer = new Timer(random(4000, 6000));
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (devourer != null) {
					if (devourer.getMessage() != null) {
						if (setPrayersOff()) {
							attackBoss(devourer, true);
							sleep(600, 1200);
						}
					} else if (prayTimer != null && devourer.getAnimation() == 14460) {
						prayTimer = null;
						if (!eatFood(topFoods, 50, 40))
							sleep(500, 800);
					} else {
						setPrayer(true, Style.MELEE, true);
						eatFood(topFoods, 50, 0);
						attackBoss(devourer, true);
					}
				}
				sleep(200, 400);
			}
		} else if (bossName.equals("World-gorger Shukarhazh") || getNpcInRoom("World-gorger shukarhazh") != null) {
			RSNPC worldGorger = getNpcInRoom("World-gorger shukarhazh");
			setBoss("World-gorger", true);
			String[] eyeNames = { "warrior-eye", "ranger-eye", "mage-eye" };
			eyeNames[primaryStyle.index()] = null;
			tempMode = Melee.STAB;
			setPrayer(true, Style.MAGIC, true);
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if (worldGorger != null) {
					boolean noneDown = getNpcsInRoom(eyeNames).length == 2;
					int hp = combat.getHealth();
					RSNPC eye = null;
					if (noneDown || hp > 40) {
						for (int I = 0; I < eyeNames.length; I++) {
							if (eyeNames[I] == null)
								continue;
							if ((eye = getGoodNpc(eyeNames[I])) == null)
								continue;
							Style style = styleOf(I);
							if ((style.enabled() && swapStyle(style)) || noneDown || hp > 65)
								break;
							eye = null;
						}
					}
					if (eye == null)
						swapStyle(primaryStyle);
					attackBoss(eye != null ? eye : worldGorger, eye == null);
					eatFood(topFoods, 55, 70);
				}
				sleep(200, 400);
			}
		} else if (bossName.equals("Kal'Ger the Warmonger") || getObjInRoom(56038) != null) {
			RSNPC kalger = getNpcInRoom("Kal'Ger the Warmonger");
			setBoss("Kal'Ger the Warmonger",false);
			Style kStyle = kalgerStyle();
			kalgerState(kStyle);
			tempMode = Melee.STAB.enabled() ? Melee.STAB : Melee.CRUSH;
			if (bossStage == 0) {
				status = "Waiting for the cutscene to finish...";
				setRetaliate(false);
				while ((kalger = getNpcInRoom(bossName)) != null && !reachable(kalger.getLocation(), true)) {
					if (failSafe())
						return;
					unBacktrack(true);
					sleep(300, 600);
				}
				bossStage = 1;
			}
			status = "Killing the Warmonger! (Stage " + bossStage + " of 6)";
			if (kalger != null && bossID < 1)
				bossID = kalger.getID();
			while (objects.getNearest(FINISHED_LADDERS) == null) {
				if (failSafe())
					return;
				if ((kalger = getNpcInRoom(bossName)) != null) {
					String shout = kalger.getMessage();
					if (shout != null && shout.startsWith("GRRR") && combat.getHealth() > random(15, 20)) {
						secondaryStatus = "Disabling prayers too cool him off";
						setPrayersOff();
						prayTimer = new Timer(random(15000, 25000));
					} else if (kalger.getAnimation() == 14968 || calc.distanceBetween(myLoc(), kalger.getLocation

()) < 3) {
						walkTo(reflect(kalger.getLocation(), myLoc(), 1), 1);
					}
					int currStage = bossStage;
					bossStage = (kalger.getID() - bossID + 15) / 15;
					if (bossStage != currStage) {
						status = "Killing the Warmonger! (Stage " + bossStage + " of 6)";
						kalgerState(kStyle = kalgerStyle());
					} else if (kalger.getAnimation() == 1) {
						protection = null;
						setPrayersOff();
					}
					if (protection != null) {
						if (prayTimer != null && combat.getHealth() < random(15, 25))
							prayTimer = null;
						if (prayTimer == null || !prayTimer.isRunning()) {
							secondaryStatus = "";
							setPrayer(true, protection, true);
						} else setPrayer(true, protection, false);
					}
					if ((combatStyle == Style.MELEE || kStyle == Style.MELEE) && !reachable(kalger.getLocation(), 

true)) {
						if (player().getAnimation() != 13493) {
							RSNPC orb = getNpcInRoom(12842);
							if (orb != null && !myLoc().equals(orb.getLocation())) {
								walkTo(orb.getLocation(), 0);
							} else topUp(true);
						}
					} else {
						setPrayer(true, protection, true);
						eatFood(topFoods, 50, 0);
						attackBoss(kalger, true);
					}
				}
				sleep(200, 400);
			}
		} else if (!unknownBossFight()) {
			return;
		}
		finish = true;
	}

	@Override
	public int loop() {
		if (!authCheck)
			return -1;
		if (!verified) {
			threadPitch(100);
		} else if (game.isLoggedIn()) {
			mouse.setSpeed(random(5, 8));
			if (inDungeon) {
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
				} else if (settingsFinished) {
					if (bossFight) {
						safeTile = null;
						if (getObjInRoom(FINISHED_LADDERS) != null) {
							finish = true;
						} else if (floor == Floor.FROZEN) {
							frozenBoss();
						} else if (floor == Floor.ABANDONED) {
							abandonedBoss();
						} else if (floor == Floor.FURNISHED) {
							furnishedBoss();
						} else if (floor == Floor.OCCULT) {
							occultBoss();
						} else if (floor == Floor.WARPED) {
							warpedBoss();
						}
						bossFight = false;
						retrace = false;
					} else if (explore) {
						exploreRoom();
						explore = false;
					} else if (open) {
						if (openNextDoor()) {
							explore = true;
						} else retrace = true;
						nearDoor = null;
						nearDoor2 = null;
						doorTimer = null;
						open = false;
					} else if (retrace) {
						retraceDungeon();
						retrace = false;
					} else {
						if (failSafe())
							return 100;
						if (developer)
							secondaryStatus = "We don't know what to do :(";
						return random(500, 1000);
					}
				}
			} else {
				if (newDungeon && inDungeon()) {
					status = "Starting a new dungeon!";
					inDungeon = true;
				} else if (objects.getNearest(END_STAIRS) != null) {
					status = "Jumping down the stairs...";
					if (doObjAction(objects.getNearest(END_STAIRS), "Jump-down")) {
						if (waitToStop(true))
							waitToStop(true);
					}
				} else if (objects.getNearest(ENTRANCE) != null) {
					enterDungeon();
				} else {
					// Failsafe
				}
			}
		}
		secondaryStatus = "";
		unreachable = false;
		return random(0, 20);
	}

	private boolean checkRoom() {
		skipRoom = false;
		getNewDoorTiles();
		doorTimer = null;
		if (newRoom == null)
			newRoom = targetRoom;
		if (newDoors.size() == 0) {
			RSObject finishedLadder = getObjInRoom(FINISHED_LADDERS);
			RSObject bDoor = getObjInRoom(BOSS_DOORS);
			if (finishedLadder != null) {
				log("Dungeon exit ahead!");
				finishDungeon();
				return false;
			}
			if (bDoor != null) {
				if (bossRoom == null) {
					bossRoom = newRoom;
					bossDoor = nearDoor;
					backDoors.add(bDoor.getLocation());
					openedDoors.add(bossDoor);
					bossPath = generatePath(bossRoom);
					restartCount = 0;
				}
				if (!areaContains(bossRoom, myLoc())) {
					boolean exploreCheck = Option.EXPLORER.enabled() && !goodDoors.isEmpty() && !bossPath.isEmpty

();
					secondaryStatus = "Preparing for the boss battle";
					if ((floor != Floor.ABANDONED || getNpcInRoom(SKINWEAVER) == null) && !inTrueCombat())
						omNomNom();
					if (!skipRoom && !isRushing && (exploreCheck || Option.MAKE_FOOD.enabled()) && !areaContains

(bossRoom, myLoc())) {
						if (exploreCheck) {
							log(BLU, "Boss room found.. Looks like we've got some more exploring to do 

first!");
							makeGatestone();
							setTargetRoom(currentRoom);
							disableBossPath = true;
							return true;
						}
						if (inventory.contains(GGS) && readyToCook()) {
							log(BLU, "Boss room found.. Returning home to prepare food before taking them 

on!");
							setTargetRoom(currentRoom);
							teleHome(true);
							return true;
						}
					}
				}
				if (!prepareForBoss())
					return true;
				finishedRooms.remove(newRoom);
				open = false;
				explore = false;
				bossFight = true;
				return retrace = false;
			}
			RSTile kTile = getKey();
			if (kTile != null) {
				secondaryStatus = "Grabbing a key from the dead end";
				if (areaContains(targetRoom, myLoc()))
					walkToMap(kTile, 1);
				selectTab(Game.TAB_INVENTORY, 3);
				walking.setRun(true);
				skipRoom = true;
			} else {
				secondaryStatus = "Skipping the dead end";
				finishedDoors.add(nearDoor);
				openedDoors.removeAll(finishedDoors);
				goodDoors.removeAll(finishedDoors);
				allDoors.removeAll(finishedDoors);
				skipRoom = true;
				skipRooms.add(targetRoom);
				setTargetRoom(currentRoom);
				newRoom = null;
				return true;
			}
		}
		return false;
	}

	private void clearAll() {
		newObjects = new ArrayList<RSObject>();
		blockedDoors.clear();
		allDoors.clear();
		goodDoors.clear();
		newDoors.clear();
		openedDoors.clear();
		lockedDoors.clear();
		backDoors.clear();
		finishedDoors.clear();
		drawDoors.clear();
		deathPath.clear();
		bossPath.clear();
		oldObjectTiles.clear();
		newObjectTiles.clear();
		rooms.clear();
		finishedRooms.clear();
		puzzleRooms.clear();
		clearedRooms.clear();
		chasmRooms.clear();
		skipRooms.clear();
		finishedPuzzles.clear();
		unBacktrackable.clear();
		badTiles.clear();
		lockIDs.clear();
		startRoom = null;
		currentRoom = null;
		targetRoom = null;
		groupRoom = null;
		oldRoom = null;
		bossRoom = null;
		nearMonster = null;
		nearDoor = null;
		nearDoor2 = null;
		safeTile = null;
		nearItem = null;
		bossDoor = null;
		oldDoor = null;
		puzzleTimer = null;
		bossHp = 100;
		roomNumber = 0;
		bossID = 0;
		restarts = 0;
		restartCount = 0;
		bossStage = 0;
		tempMode = Melee.NONE;
		if (temporarySecondary)
			secondaryWep = -1;
		bossName = "";
		secondaryStatus = "";
		aborted = false;
		retrace = false;
		explore = false;
		open = false;
		bossFight = false;
		finish = false;
		exit = false;
		skipRoom = false;
		skipDoorFound = false;
		isDead = false;
		outOfAmmo = false;
		disrobed = false;
		newDungeon = true;
		cookReady = false;
		roomSwitch = false;
		spawnRoom = false;
		disableBossPath = false;
		failTimer = new Timer(random(360000, 480000));
		idleTimer = new Timer(0);
		prayTimer = null;
		combatStyle = styleOf(primaryStyle.index());
	}

	private boolean fightMonsters() {
		boolean activated = false, primariesCleared = false, secondariesCleared = false;
		if (finishedRooms.contains(targetRoom))
			return true;
		nearMonster = npcs.getNearest(monster);
		unreachable = false;
		if (nearMonster != null) {
			crossTheChasm(nearMonster.getLocation());
			secondaryStatus = "Clearing the room of monsters";
			while (npcs.getNearest(monster) != null) {
				if (failSafe())
					return false;
				if (!isGoodMonster(nearMonster)) {
					if (!secondariesCleared) {
						RSNPC primary = !primariesCleared ? npcs.getNearest(primaryMonster) : null;
						if (primary == null) {
							RSNPC secondary = npcs.getNearest(secondaryMonster);
							primariesCleared = true;
							if (secondary == null) {
								secondariesCleared = true;
								nearMonster = npcs.getNearest(monster);
							} else nearMonster = secondary;
						} else nearMonster = primary;
					} else nearMonster = npcs.getNearest(monster);
				}
				if (unreachable) {
					topUp(true);
					unreachable = false;
				} else if (attackNpc(nearMonster)) {
					getBestStyle(nearMonster);
					if (!updateFightMode())
						sleep(900, 1400);
				}
				if (Option.PRAY.enabled()) {
					monsterPrayer();
					if (protection != null) {
						if (setPrayer(false, protection, getGoodNpc(strongestMonster) != null))
							activated = true;
					} else if (activated) {
						activated = setPrayersOff();
					}
				}
				if (status.startsWith("Puzzle room: Follow")) {
					RSObject obj = objects.getTopAt(myLoc());
					if (obj != null && intMatch(obj.getID(), ACTIVE_PADS))
						walkAdjacentTo(myLoc(), 2);
				}
				eatFood(goodFoods, 45, 55);
				sleep(200, 400);
			}
			if (activated)
				setPrayersOff();
			nearMonster = null;
			double mDist = random(3, 7);
			while (npcs.getNearest(dying) != null) {
				if (failSafe())
					return false;
				if (inventory.getCount() < 27 || !topUp(false)) {
					getGoodItem();
					if (nearItem != null && calc.distanceTo(nearItem.getLocation()) < mDist) {
						if (pickUpItem(nearItem))
							waitToStop(false);
					} else if (!topUp(false) || getGoodItem() == null) {
						sleep(300, 600);
					}
				} else sleep(300, 600);
			}

			secondaryStatus = "";
		}
		return true;
	}

	private boolean unknownBossFight() {
		setBoss("an unknown boss...", false);
		setPrayer(true, null, false);
		return genericBossFight();
	}

	private boolean genericBossFight() {
		RSNPC boss = npcs.getNearest(bossMonster);
		while (objects.getNearest(FINISHED_LADDERS) == null) {
			if (failSafe())
				return false;
			if (unreachable) {
				if (!topUp(true))
					sleep(500, 1000);
				unreachable = false;
			}
			attackBoss(boss, true);
			eatFood(goodFoods, 50, 60);
			sleep(400, 800);
		}
		return finish = true;
	}

	private int getBestDoor() {
		int[] puzzleDoors = floor != Floor.WARPED ? PUZZLE_DOORS : WARPED_PUZZLE_DOORS;
		double dist = 99.99;
		int doorID = -1;
		RSTile nextDoor = null, start = myLoc();
		destDoor = null;
		nearDoor = null;
		nearDoor2 = null;
		ArrayList<RSTile> goodRoomDoors = new ArrayList<RSTile>();
		for (RSTile gDoor : goodDoors) {
			if (areaContains(targetRoom, gDoor))
				goodRoomDoors.add(gDoor);
		}
		if (!goodRoomDoors.isEmpty()) {
			for (RSTile door : goodRoomDoors) {
				double dDist = calc.distanceBetween(start, door);
				if (dDist < dist) {
					RSObject doorObj = objects.getTopAt(door);
					if (doorObj != null) {
						dist = dDist;
						doorID = doorObj.getID();
						nextDoor = door;
					}
				}
			}
			if (nextDoor != null && doorID > 0) {
				for (RSTile bDoor : blockedDoors) {
					if (calc.distanceBetween(nextDoor, bDoor) < 3) {
						nearDoor = nextDoor;
						nearDoor2 = bDoor;
						destDoor = nearDoor2;
						return 2;
					}
				}
				for (RSTile lDoor : lockedDoors) {
					if (calc.distanceBetween(nextDoor, lDoor) < 3) {
						nearDoor = nextDoor;
						nearDoor2 = lDoor;
						destDoor = nearDoor2;
						return 3;
					}
				}
				if (intMatch(doorID, STANDARD_DOORS)) {
					nearDoor = nextDoor;
					destDoor = nearDoor;
					return 1;
				}
				if (skillDoors.contains(nextDoor)) {
					nearDoor = nextDoor;
					destDoor = nearDoor;
					return 4;
				}
				if (intMatch(doorID, puzzleDoors)) {
					nearDoor = nextDoor;
					destDoor = nearDoor;
					return 1;
				}
			}
		}
		boolean verifyBoss = disableBossPath && bossDoor != null;
		for (RSTile door : openedDoors) {
			if ((!verifyBoss || !bossDoor.equals(door)) && (oldDoor == null || !door.equals(oldDoor)) && areaContains

(targetRoom, door)) {
				RSArea room = getNextRoom(door);
				if (room != null && (roomContains(room, goodDoors) || roomContains(room, openedDoors))) {
					nearDoor = door;
					destDoor = nearDoor;
					return 5;
				}
			}
		}
		return 0;
	}

	private void getNewDoorTiles() {
		int[] puzzleDoors = floor != Floor.WARPED ? PUZZLE_DOORS : WARPED_PUZZLE_DOORS;
		newDoors.clear();
		nextObj:for (RSObject obj : newObjects) {
			int objID = obj.getID();
			RSTile objTile = obj.getLocation();
			if (intMatch(objID, BASIC_DOORS)) {
				if (nearDoor != null && calc.distanceBetween(nearDoor, objTile) < 4) {
					backDoors.add(objTile);
					continue nextObj;
				}
				newDoors.add(objTile);
				goodDoors.add(objTile);
				continue nextObj;
			}
			if (intMatch(objID, GUARDIAN_DOORS)) {
				guardianDoors.add(objTile);
				newDoors.add(objTile);
				goodDoors.add(objTile);
				continue nextObj;
			}
			for (int[] color : KEY_DOORS) {
				if (intMatch(objID - floor.diff(), color)) {
					newDoors.add(objTile);
					continue nextObj;
				}
				if (intMatch(objID, color)) {
					lockIDs.add(objID);
					lockedDoors.add(objTile);
					continue nextObj;
				}
			}
			if (aComplexity > 4) {
				if (intMatch(objID, FREE_BLOCK_DOORS)) {
					blockedDoors.add(objTile);
				} else if (intMatch(objID, MEMBER_BLOCK_DOORS)) {
					if (memberWorld) {
						blockedDoors.add(objTile);
					} else finishedDoors.add(objTile);
				} else if (intMatch(objID, PRAYER_DOORS)) {
					if (!Option.PRAY_DOORS.enabled()) {
						finishedDoors.add(objTile);
						skipDoorFound = true;
					} else blockedDoors.add(objTile);
				} else if (intMatch(objID, SUMMON_DOORS)) {
					if (!Option.SUMMON.enabled()) {
						finishedDoors.add(objTile);
						skipDoorFound = true;
					} else blockedDoors.add(objTile);
				} else if (intMatch(objID, FREE_SKILL_DOORS)) {
					skillDoors.add(objTile);
					newDoors.add(objTile);
					goodDoors.add(objTile);
				} else if (intMatch(objID, MEMBER_SKILL_DOORS)) {
					if (memberWorld) {
						skillDoors.add(objTile);
						newDoors.add(objTile);
						goodDoors.add(objTile);
					} else finishedDoors.add(objTile);
				} else if (intMatch(objID, puzzleDoors)) {
					newDoors.add(objTile);
					goodDoors.add(objTile);
				}
			}
		}
		newObjectTiles.clear();
		newObjects.clear();
		ArrayList<RSTile> removals = new ArrayList<RSTile>();
		for (RSTile fDoor : finishedDoors) {
			if (areaContains(newRoom, fDoor)) {
				for (RSTile nDoor : newDoors) {
					if (calc.distanceBetween(nDoor, fDoor) < 4)
						removals.add(nDoor);
				}
			}
		}
		finishedDoors.addAll(removals);
		drawDoors.addAll(removals);
		newDoors.removeAll(finishedDoors);
		goodDoors.removeAll(finishedDoors);
		drawDoors.addAll(blockedDoors);
		drawDoors.addAll(lockedDoors);
		drawDoors.addAll(newDoors);
		allDoors.addAll(newDoors);
		if (nearDoor != null) {
			openedDoors.add(nearDoor);
			goodDoors.remove(nearDoor);
			oldDoor = nearDoor;
		}
		return;
	}

	private boolean getNewRoomArea() {
		boolean newDoor = false;
		if (currentRoom != null && nearDoor != null) {
			for (RSObject o : objects.getAll()) {
				if (o != null && intMatch(o.getID(), BACK_DOORS) && calc.distanceBetween(o.getLocation(), nearDoor) < 

3 && !areaContains(currentRoom, o.getLocation())) {
					newDoor = true;
					break;
				}
			}
		}
		if (!newDoor && rooms.size() > 0)
			return false;
		if (game.getClientState() == 11) {
			while (game.getClientState() == 11) {
				sleep(200, 300);
			}
			sleep(300);
		}
		sleep(400);
		for (RSObject obj : objects.getAll(roomObjects)) {
			RSTile objTile = obj.getLocation();
			if (!oldObjectTiles.contains(objTile)) {
				newObjectTiles.add(objTile);
				newObjects.add(obj);
			}
		}
		if (newObjectTiles.size() > 60) {
			int swX = 20000, swY = 20000;
			for (RSTile t : newObjectTiles) {
				if (t.getX() <= swX && t.getY() <= swY) {
					swX = t.getX();
					swY = t.getY();
				}
			}
			newRoom = new RSArea(swX, swY, swX + 15, swY + 15);
			rooms.add(newRoom);
			if (dropItem(GUARD_KEY) && bounds.contains((Integer) GUARD_KEY))
				bounds.remove((Integer) GUARD_KEY);
			setTargetRoom(newRoom);
			oldObjectTiles.addAll(newObjectTiles);
			restartCount = 0;
			restarts = 0;
			failTimer.reset();
			doorTimer = null;
			return true;
		}
		return false;
	}

	private boolean openBasicDoor() {
		status = "Opening a standard door";
		while (!areaContains(newRoom, myLoc())) {
			if (failSafe() || nearDoor == null)
				return false;
			if (areaContains(getCurrentRoom(), nearDoor)) {
				if (calc.distanceTo(nearDoor) > 4) {
					lastMessage = "";
				} else if (lastMessage.contains("guardians")) {
					if (npcs.getNearest(monster) != null) {
						if (!fightMonsters() || !pickUpAll())
							return false;
						doorTimer = new Timer(0);
					} else {
						log("Unkillable Guardians, removing door.");
						finishedDoors.add(nearDoor);
						allDoors.remove(nearDoor);
						openedDoors.remove(nearDoor);
						goodDoors.remove(nearDoor);
						return true;
					}
					lastMessage = "";
				} else if (unreachable && calc.tileOnScreen(nearDoor)) {
					if (calc.distanceTo(nearDoor) < 4) {
						camera.moveRandomly(random(800, 1400));
						clickTile(nearDoor, "");
					} else walkToMap(nearDoor, 1);
					unreachable = false;
				} else if (lastMessage.contains("not open") || lastMessage.contains("won't open")) {
					log("Undetected puzzle door, removing!");
					if (developer)
						env.saveScreenshot(false);
					finishedDoors.add(nearDoor);
					allDoors.remove(nearDoor);
					goodDoors.remove(nearDoor);
					waitForResponse();
					return true;
				} else if (lastMessage.contains("don't have the correct")) {
					log.severe("Hmm.. we got tricked, we don't have the right key!");
					finishedDoors.add(nearDoor);
					finishedDoors.add(nearDoor2);
					goodDoors.remove(nearDoor);
					return true;
				}
				if (doObjAction(objects.getTopAt(nearDoor))) {
					waitToObject(false);
					if (waitForRoom(false))
						return true;
				} else while (isMoving() && !calc.tileOnScreen(nearDoor)) {
					sleep(100, 200);
				}
			}
			sleep(100, 200);
		}
		return true;
	}

	private boolean openBlockedDoor() {
		RSObject block;
		status = "Opening a blocked door";
		while ((block = objects.getTopAt(nearDoor2)) != null) {
			if (failSafe())
				return false;
			if (intMatch(player().getAnimation(), BLOCK_ANIMS))
				break;
			if (calc.distanceTo(nearDoor2) < 4 && (getName(block).isEmpty() || block.getArea().getTileArray().length != 

2))
				break;
			if (!doorCheck()) {
				finishedDoors.add(nearDoor2);
				allDoors.remove(nearDoor2);
				openedDoors.remove(nearDoor2);
				goodDoors.remove(nearDoor2);
				drawDoors.remove(nearDoor);
				return true;
			}
			if (doObjAction(block)) {
				if (!waitToObject(false))
					continue;
				eatFood(foods, 50, 50);
			} else {
				while (block != null && isMoving() && !block.isOnScreen()) {
					sleep(100, 200);
				}
			}
			waitForDamage();
			sleep(100, 200);
		}
		drawDoors.remove(nearDoor2);
		allDoors.remove(nearDoor2);
		blockedDoors.remove(nearDoor2);
		goodDoors.remove(nearDoor2);
		openedDoors.remove(nearDoor2);
		doorTimer = new Timer(0);
		int attempts = 0;
		while (!areaContains(newRoom, myLoc())) {
			if (failSafe())
				return false;
			if (areaContains(getCurrentRoom(), nearDoor)) {
				RSObject door = objects.getTopAt(nearDoor);
				if (door != null) {
					if (doorTimer != null && attempts < 10 && doorTimer.getElapsed() > 30000) {
						door = getObjInRoom(FREE_BLOCK_DOORS);
						if (door == null || calc.distanceTo(door) > 2) {
							door = getObjInRoom(MEMBER_BLOCK_DOORS);
							if (door != null && calc.distanceTo(door) > 2)
								door = null;
						}
						attempts ++;
						unreachable = false;
					}
					if (doObjAction(door)) {
						waitToObject(false);
						if (waitForRoom(false))
							return true;
					} else while (isMoving() && !calc.tileOnScreen(nearDoor)) {
						sleep(100, 200);
					}
				}
			}
			sleep(100, 200);
		}
		return true;
	}

	private boolean openLockedDoor() {
		status = "Opening a locked door";
		lockDown = false;
		while (!lockDown) {
			if (failSafe())
				return false;
			if (player().getAnimation() == 13798)
				break;
			RSObject lock = objects.getTopAt(nearDoor2);
			if (calc.distanceTo(nearDoor2) < 4 && (getName(lock).isEmpty() || lock.getArea().getTileArray().length != 2))
				break;
			if (lastMessage.contains("don't have the correct")) {
				log.severe("Hmm.. we got tricked, we don't have the right key!");
				goodDoors.remove(nearDoor);
				goodDoors.remove(nearDoor2);
				return true;
			}
			if (doObjAction(lock)) {
				waitToAnimate();
				if (player().getAnimation() == 13798)
					break;
				waitToObject(false);
			} else while (lock != null && isMoving() && !lock.isOnScreen()) {
				sleep(100, 200);
			}
			sleep(200, 400);
		}
		int attempts = 0;
		lockDown = false;
		drawDoors.remove(nearDoor2);
		allDoors.remove(nearDoor2);
		goodDoors.remove(nearDoor2);
		lockedDoors.remove(nearDoor2);
		doorTimer = new Timer(0);
		while (!areaContains(newRoom, myLoc())) {
			if (failSafe())
				return false;
			RSObject door = objects.getTopAt(nearDoor);
			if (door != null) {
				if (unreachable || (doorTimer != null && attempts < 10 && doorTimer.getElapsed() > 30000)) {
					door = getObjInRoom(door.getID() - floor.diff());
					if (door == null)
						door = objects.getTopAt(nearDoor2);
					unreachable = false;
					attempts++;
				}
				if (doObjAction(door)) {
					waitToObject(false);
					if (waitForRoom(false))
						return true;
				}
			}
			sleep(200, 400);
			getCurrentRoom();
		}
		return true;
	}

	private boolean openSkillDoor() {
		status = "Opening a skill door";
		while (!areaContains(newRoom, myLoc())) {
			if (failSafe())
				return false;
			if (!doorCheck())
				return true;
			if (lastMessage.contains("need an empty vial")) {
				if (inventory.contains(ANTIPOISON)) {
					ridItem(ANTIPOISON, "Drink");
				} else {
					finishedDoors.add(nearDoor);
					allDoors.remove(nearDoor);
					openedDoors.remove(nearDoor);
					goodDoors.remove(nearDoor);
					return true;
				}
			}
			if (areaContains(getCurrentRoom(), nearDoor)) {
				RSObject door = objects.getTopAt(nearDoor);
				if (door != null) {
					if (doObjAction(door)) {
						waitToObject(true);
						if (newRoom == null && staticDamage() && (intMatch(door.getID(), FREE_SKILL_DOORS) || 

intMatch(door.getID(), MEMBER_SKILL_DOORS)))
							topUp(true);
						if (waitForRoom(true))
							return true;
					} else while (door != null && isMoving() && !door.isOnScreen()) {
						sleep(100, 200);
					}
					if (!doorCheck())
						return true;
				}
			}
			eatFood(foods, 40, 50);
			sleep(200, 400);
		}
		oldDoor = nearDoor;
		return true;
	}

	private boolean openOldDoor() {
		if (!status.contains("Opening"))
			status = "Opening an old door";
		for (RSArea room : rooms) {
			if (!areaEquals(room, oldRoom) && calc.distanceBetween(nearDoor, room.getNearestTile(nearDoor)) < 3) {
				newRoom = room;
				break;
			}
		}
		if (newRoom == null)
			return false;
		doorTimer = new Timer(0);
		o:while (!areaContains(newRoom, myLoc())) {
			if (failSafe())
				return false;
			if (areaContains(getCurrentRoom(), nearDoor)) {
				if (doObjAction(objects.getTopAt(nearDoor))) {
					waitToObject(false);
					for (int i = 0; i < 4; i++) {
						if (areaContains(newRoom, myLoc()))
							break o;
						sleep(200, 250);
					}
				} else {
					while (isMoving() && !calc.tileOnScreen(nearDoor)) {
						topUp(false);
						sleep(100, 200);
					}
				}
			}
			sleep(200, 400);
		}
		setTargetRoom(getCurrentRoom());
		newRoom = null;
		openedDoors.remove(nearDoor);
		openedDoors.add(nearDoor);
		oldDoor = nearDoor;
		return true;
	}

	private boolean pickUpAll() {
		if (getGoodItem() == null || (finishedRooms.contains(targetRoom) && (puzzleRooms.contains(targetRoom) || !reachable

(nearItem.getLocation(), true))))
			return true;
		boolean guardian = npcs.getNearest(guardians) != null;
		int invCheck = inventory.getCount(true);
		RSTile iTile = nearItem.getLocation();
		improveEquipment();
		crossTheChasm(iTile);
		if (secondaryStatus.isEmpty()) {
			secondaryStatus = "Picking up food & keys";
			if (Option.BONES.enabled())
				secondaryStatus += " & bones";
			if (Option.ARROWS.enabled())
				secondaryStatus += " & arrows";
		}
		Timer pickupTimer = new Timer(0);
		unreachable = false;
		o:while (getGoodItem() != null) {
			if (failSafe())
				return false;
			if (pickupTimer.getElapsed() > random(30000, 90000) || secondaryStatus.contains("reengage"))
				return true;
			iTile = nearItem.getLocation();
			makeSpace(!skipRoom);
			if (pickUpItem(nearItem)) {
				if (calc.distanceTo(iTile) > 2 && !waitToStart(false))
					unStick();
				while (isMoving() && calc.distanceTo(iTile) > 2) {
					if (guardian && player().getInteracting() != null)
						continue o;
					sleep(100, 200);
				}
				sleep(0, 300);
				if (unreachable) {
					walkToMap(iTile, 1);
					waitToStart(false);
					unreachable = false;
				} else {
					for (int c = 0; c < (!isMoving() && myLoc().equals(iTile) ? 4 : 8); c++) {
						if (inventory.getCount(true) > invCheck) {
							improveEquipment();
							break;
						}
						sleep(200, 300);
						if (!isMoving()) {
							if (roomNumber != 1 && !myLoc().equals(iTile))
								break;
							c++;
						} else if (guardian && player().getInteracting() != null) {
							continue o;
						}
					}
				}
			} else {
				if (roomNumber == 1)
					threadDefault();
				unStick();
			}
			if (!isMoving() && calc.distanceTo(iTile) > 2 && reachable(iTile, true) && pickupTimer.getElapsed() > random

(3000, 6000))
				walkTo(iTile, 2);
			if (inventory.getCount(true) > invCheck || isMoving()) {
				invCheck = inventory.getCount(true);
				pickupTimer = new Timer(0);
			} else if (pickupTimer.getElapsed() > random(14000, 20000)) {
				getCurrentRoom();
				unStick();
				if (calc.distanceTo(iTile) > 1 && reachable(iTile, true)) {
					walkBlockedTile(iTile, 2);
				} else if (!badTiles.contains(iTile)) {
					pickupTimer = new Timer(0);
					badTiles.add(iTile);
				}
			}
			sleep(200, 300);
		}
		if (skipRoom)
			eatFood(foods, 30, 30);
		improveArmorWielding();
		improveWeaponBinding();
		secondaryStatus = "";
		return true;
	}

	private boolean resetPrestige() {
		if (Option.PRESTIGE.enabled()) {
			secondaryStatus = "Resetting prestige";
			while (cProg != 0) {
				if (failBasic())
					return false;
				if (dungeonCount > 5 && password.isEmpty()) {
					walkTo(ORIGIN, 1000);
				} else if (interfaces.get(213).isValid()) {
					interfaces.getComponent(213, 5).interact("Continue");
				} else if (interfaces.get(228).isValid()) {
					interfaces.getComponent(228, 2).interact("Continue");
				} else if (openPartyTab()) {
					clickComponent(interfaces.getComponent(939, 87), "Reset");
				}
				sleep(600, 1000);
				updateProgress();
			}
			prestigeCount++;
			forcePrestige = false;
			secondaryStatus = "";
			return true;
		} else {
			log(RED, "Last floor reached, but prestiging is disabled! Shutting down script.");
			authCheck = false;
		}
		return false;
	}

	private void runSettings() {
		threadDefault();
		bounds.clear();
		prayerLevel = skills.getRealLevel(Skills.PRAYER);
		slayerLevel = skills.getRealLevel(Skills.SLAYER);
		if (!settingsFinished) {
			if (!authCheck)
				return;
			attStart = skills.getCurrentExp(Skills.ATTACK);
			strStart = skills.getCurrentExp(Skills.STRENGTH);
			defStart = skills.getCurrentExp(Skills.DEFENSE);
			hitStart = skills.getCurrentExp(Skills.CONSTITUTION);
			rngStart = skills.getCurrentExp(Skills.RANGE);
			mgcStart = skills.getCurrentExp(Skills.MAGIC);
			prayStart = skills.getCurrentExp(Skills.PRAYER);
			dungStart = skills.getCurrentExp(Skills.DUNGEONEERING);
			runeStart = skills.getCurrentExp(Skills.RUNECRAFTING);
			mineStart = skills.getCurrentExp(Skills.MINING);
			fireStart = skills.getCurrentExp(Skills.FIREMAKING);
			woodStart = skills.getCurrentExp(Skills.WOODCUTTING);
			smthStart = skills.getCurrentExp(Skills.SMITHING);
			crftStart = skills.getCurrentExp(Skills.CRAFTING);
			agilStart = skills.getCurrentExp(Skills.AGILITY);
			thevStart = skills.getCurrentExp(Skills.THIEVING);
			summStart = skills.getCurrentExp(Skills.SUMMONING);
			herbStart = skills.getCurrentExp(Skills.HERBLORE);
			cnstStart = skills.getCurrentExp(Skills.CONSTRUCTION);
			cookStart = skills.getCurrentExp(Skills.COOKING);
			fishStart = skills.getCurrentExp(Skills.FISHING);
			slayStart = skills.getCurrentExp(Skills.SLAYER);
			dungStartLevel = skills.getRealLevel(Skills.DUNGEONEERING);
			dungLevel = skills.getRealLevel(Skills.DUNGEONEERING);
			isCursing = prayer.isCursing();
			if (levelGoal <= dungLevel)
				levelGoal = dungLevel + 1;
			//if (!loadSettings()) {
				if (prayerLevel > 11) {
					Option.PRAY_DOORS.set(true);
					if (prayerLevel > 36)
						Option.PRAY.set(true);
				}
				if (skills.getRealLevel(Skills.CONSTITUTION) < 60)
					Option.CRABS.set(true);
				if (skills.getRealLevel(Skills.DEFENSE) < 20) {
					Option.PURE.set(true);
				} else Option.STYLE_SWAP.set(true);
			//}
			if (groundItems.getNearest(CAVE_MORAY, BLUE_CRAB, SALVE_EEL, BOULDABASS, WEB_SNIPPER) == null) {
				game.open(Game.TAB_FRIENDS, false);
				String world = interfaces.getComponent(550, 18).getText();
				selectTab(Game.TAB_INVENTORY, 3);
				if (!world.isEmpty()) {
					world = world.concat(".");
					for (String w : FREE_WORLDS) {
						if (world.contains(" " + w + ".")) {
							memberWorld = false;
							break;
						}
					}
				}
			}
			oTab = 0;
			hidePaint = false;
			showStats = false;
			showOptions = false;
			showSettings = true;
			game.setChatOption(Game.ChatButton.GAME, Game.ChatMode.VIEW);
			env.setUserInput(Environment.INPUT_MOUSE | Environment.INPUT_KEYBOARD);
			while (showSettings) {
				if (!authCheck)
					return;
				sleep(300, 600);
			}
			env.setUserInput(Environment.INPUT_KEYBOARD);
			game.setChatOption(Game.ChatButton.ASSIST, Game.ChatMode.OFF);
			defaultMode = combat.getFightMode();
			if (Option.MAGIC.enabled()) {
				defaultMode = 4;
			} else if (defaultMode == 4) {
				defaultMode = 1;
			}
			attackMode = valueOf(defaultMode);
			selectTab(Game.TAB_EQUIPMENT, 5);
			int attackLevel = skills.getRealLevel(Skills.ATTACK), defenseLevel = skills.getRealLevel(Skills.DEFENSE);
			defenseTier = defenseLevel == 99 ? 11 : defenseLevel / 10 + 1;
			attackTier = attackLevel == 99 ? 11 : attackLevel / 10 + 1;
			if (Option.RANGE.enabled()) {
				int rangeLevel = skills.getRealLevel(Skills.RANGE);
				attackTier = rangeLevel == 99 ? 11 : rangeLevel / 10 + 1;
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
			RSItem weaponSlot = equipment.getItem(Equipment.WEAPON);
			if (weaponSlot != null && weaponSlot.getID() > 0) {
				primaryWep = weaponSlot.getID();
				String wName = equipmentName(weaponSlot);
				String wMetal = equipmentMaterial(wName);
				weaponTier = equipmentTier(wName);
				weaponType = equipmentType(wName);
				Weapon weapon = enumOf(Weapon.class, weaponType);
				if (weapon == null) {
					log.severe("Unknown weapon type. Please try restarting the script for best results");
				} else {
					int level = skills.getRealLevel(weapon.skill());
					Melee.STAB.set(weapon.stab());
					Melee.SLASH.set(weapon.slash());
					Melee.CRUSH.set(weapon.crush());
					if (combatStyle == Style.MELEE)
						Melee.DEFENSE.set(weapon.defensive());
					attackTier = level == 99 ? 11 : level / 10 + 1;
					twoHanded = weapon.isTwoHanded();
					if (weaponTier == 0)
						weaponTier = 11;
					log(BLU, "Weapon: " + wMetal + " " + weaponType + " - Tier: " + weaponTier + "; Max tier: " + 

attackTier + "; Two-handed: " + (twoHanded ? "Yes" : "No") + "; Upgrading: " + (weaponTier < attackTier ? "Yes" : "No"));
					if (Option.STYLE_SWAP.enabled && weapon.slash() == -1)
						Option.STYLE_SWAP.set(false);
				}
			}
			if (Option.PURE.enabled() && (defaultMode == defenseMode || defaultMode == 3))
				defaultMode = 1;
			RSItem ammo = equipment.getItem(Equipment.AMMO);
			if (ammo != null && ammo.getID() > 0)
				arrows = ammo.getID();
			for (int I = 0; I < EQUIP_SLOTS.length; I++) {
				RSItem eq = equipment.getItem(EQUIP_SLOTS[I]);
				if (eq != null && eq.getID() > 0) {
					String eqName = equipmentName(eq);
					int tier = equipmentTier(eqName);
					if (eq.getName().contains("(b)")) {
						if (I == 0 && eqName.contains("silk hood")) {
							initialEquipmentTiers[I] = 11;
							shadowHooded = true;
						} else if (I == 2 && (eqName.contains("blastbox") || eqName.contains("surgebox"))) {
							blastBox = eq.getID();
							initialEquipmentTiers[I] = 11;
							twoHanded = true;
						} else if (I == 4 && eqName.contains("precision")) {
							initialEquipmentTiers[I] = 11;
						} else initialEquipmentTiers[I] = tier;
						if (initialEquipmentTiers[I] == 0)
							initialEquipmentTiers[I] = 11;
						if (initialEquipmentTiers[I] < defenseTier) {
							startArmor = eq.getID();
							armorSlot = I;
							armorType = equipmentType(eqName);
						}
						equipmentTiers[I] = valueOf(initialEquipmentTiers[I]);
						tempTiers[I] = valueOf(initialEquipmentTiers[I]);
						log(BLU, "Armor: " + eqName + " - Tier: " + equipmentTiers[I] + "; Max tier: " + 

defenseTier + "; Upgrading: " + (equipmentTiers[I] < defenseTier ? "Yes" : "No"));
					} else {
						equipmentTiers[I] = tier;
						tempTiers[I] = tier;
					}
				}
			}
			if (combatSpell != null) {
				Magic spellType = combatSpell.type();
				if (spellType != null && spellType != Magic.NONE)
					spellType.set(4);
			}
		}
		for (RSItem item : inventory.getItems()) {
			if (item == null)
				continue;
			int itemId = item.getID();
			if (itemId < 1)
				continue;
			String iName = equipmentName(item);
			int tier = equipmentTier(iName);
			if (tier > 0) {
				String iType = equipmentType(iName);
				for (int I = 0; I < slotNames.length; I++) {
					if (iType.contains(slotNames[I])) {
						if (tier > equipmentTiers[I] && ridItem(itemId, "Wear"))
							equipmentTiers[I] = tier;
						break;
					}
				}
			}
		}
		Style.MELEE.set(Option.MELEE.enabled() || (Option.SECONDARY_MELEE.enabled() && secondaryWep > 0));
		Style.RANGE.set(Option.RANGE.enabled() || (Option.SECONDARY_RANGE.enabled() && arrows > 0 && secondaryWep > 0));
		Style.MAGIC.set(Option.MAGIC.enabled() || (Option.SECONDARY_MAGIC.enabled() && (secondaryWep > 0 || blastBox > 0)));
		temporarySecondary = secondaryWep < 1;
		int goodOffset = skills.getRealLevel(Skills.CONSTITUTION) / (memberWorld ? 34 : 46);
		foods = FOOD_ARRAY;
		goodFoods = new int[foods.length - goodOffset];
		int[] newTop = new int[foods.length - goodOffset - 1];
		topFoods = new int[newTop.length];
		if (skills.getRealLevel(Skills.DEFENSE) < random(5, 20) && username.isEmpty())
			Option.PURE.set(true);
		System.arraycopy(foods, goodOffset, goodFoods, 0, goodFoods.length);
		System.arraycopy(goodFoods, 1, newTop, 0, newTop.length);
		for (int I = 0; I < newTop.length; I++) {
			topFoods[I] = newTop[newTop.length - 1 - I];
		}
		if (!Option.CRABS.enabled()) {
			foods = new int[FOOD_ARRAY.length - 1];
			System.arraycopy(FOOD_ARRAY, 1, foods, 0, foods.length);
		}
		for (int food : foods) {
			bounds.add((Integer) food);
		}
		if (Option.BONES.enabled()) {
			for (int bone : BONES) {
				bounds.add((Integer) bone);
			}
		}
		if (Option.MAKE_FOOD.enabled())
			bounds.add((Integer) COINS);
		if (primaryWep > 0)
			bounds.add((Integer) primaryWep);
		if (secondaryWep > 0)
			bounds.add((Integer) secondaryWep);
		if (blastBox > 0)
			bounds.add((Integer) blastBox);
		if (arrows > 0)
			bounds.add((Integer) arrows);
		for (int[] color : KEYS) {
			for (int key : color) {
				bounds.add((Integer) key);
			}
		}
		if (aComplexity > 4) {
			for (Integer tool : TOOLS) {
				bounds.add(tool);
			}
			int magicLevel = skills.getRealLevel(Skills.MAGIC);
			if (magicLevel > 31) {
				bounds.add((Integer) GGS);
				for (Integer rune : COSMIC_RUNES) {
					bounds.add(rune);
				}
				if (magicLevel > 63 && floor != Floor.FROZEN) {
					for (Integer rune : LAW_RUNES) {
						bounds.add(rune);
					}
				}
			}
			if (!memberWorld) {
				bounds.remove((Integer) 17490);
				bounds.remove((Integer) 17754);
			} else bounds.add((Integer) ANTIPOISON);
		} else if (floor == Floor.ABANDONED) {
			bounds.add((Integer) PICKAXE);
		}
		if (Option.RUSH.enabled() && cProg < 1)
			openPartyTab();
		for (RSItem item : inventory.getItems()) {
			if (item != null && item.getName().contains("(b)"))
				bounds.add((Integer) item.getID());
		}
		if (Option.RANGE.enabled()) {
			ridItem(arrows, "Wield");
		} else if (Option.MAGIC.enabled() && ridItem(blastBox, "Wield")) {
			while (!isAutoCasting()) {
				if (failCheck())
					return;
				if (castDungeonSpell(combatSpell, "Autocast"))
					sleep(300, 600);
				sleep(100, 200);
			}
		}
		combatStyle = styleOf(primaryStyle.index());
		if (!settingsFinished) {
			settingsFinished = true;
			saveSettings();
			runTimer = new Timer(0);
			log(PRP, "Settings complete!");
		}
		failTimer = new Timer(random(360000, 480000));
	}

	private class threadAngle extends Thread {
		public void run() {
			try {
				while (Math.abs(camera.getAngleTo(degrees)) > 5) {
					camera.setAngle(degrees);
					sleep(0, 20);
				}
			} catch (Exception e) { }
		}
	}

	private class threadPitch extends Thread {
		public void run() {
			try {
				while (camera.getPitch() != pitch) {
					camera.setPitch(pitch);
					sleep(0, 20);
				}
			} catch (Exception e) { }
		}
	}

	private boolean shop(final Food fish, final Logs logs) {
		boolean sold = false;
		int toScroll = random(175, 200);
		lastMessage = "";
		while ((!sold || !inventory.isFull()) && !lastMessage.contains("afford") && !lastMessage.contains("enough money")) {
			if (failCheck())
				return false;
			RSInterface shop = interfaces.get(956);
			if (!shop.isValid()) {
				smartSleep(doNpcAction(getNpcInRoom(SMUGGLER), "Trade"), false);
			} else if (!sellToShop()) {
				RSComponent logItem = shop.getComponent(24).getComponent((memberWorld ? 197 : 97) + logs.tier() * 5);
				RSComponent scrollBar = shop.getComponent(25);
				while (scrollBar.isValid() && logItem.getAbsoluteY() > toScroll) {
					clickComponent(scrollBar.getComponent(5), "");
					sleep(0, 50);
				}
				if (logs != null && getObjInRoom(FIRES) == null && !inventory.contains(logs.getID())) {
					if (clickComponent(logItem, "Buy 1")) {
						for (int c = 0; c < 10; c++) {
							if (inventory.contains(logs.getID()))
								break;
							sleep(150, 250);
						}
					}
				} else if (fish != null) {
					RSComponent foodItem = shop.getComponent(24).getComponent((memberWorld ? 147 : 72) + 

fish.tier() * 5);
					int startCount = inventory.getCount();
					if (clickComponent(foodItem, "Buy 50")) {
						for (int c = 0; c < 10; c++) {
							if (inventory.getCount() > startCount)
								break;
							sleep(150, 250);
						}
					}
				}
				sold = true;
			}
			sleep(50, 200);
		}
		while (interfaces.get(956).isValid()) {
			interfaces.getComponent(956, 18).interact("Close");
			sleep(700, 1000);
		}
		return true;
	}

	private boolean shop(final int idx, final int amount) {
		lastMessage = "";
		while (!interfaces.get(956).isValid()) {
			if (failCheck())
				return false;
			if (doNpcAction(getNpcInRoom(SMUGGLER), "Trade"))
				waitToStop(false);
			sleep(600, 800);
		}
		sellToShop();
		RSComponent scrollBar = interfaces.getComponent(956, 25);
		Point sP = scrollBar.getComponent(5).getCenter();
		while (scrollBar.isValid() && scrollBar.getComponent(3).getRelativeY() < 225) {
			mouse.click(sP.x, sP.y - 25, 5, 17, true);
			sleep(200, 600);
		}
		RSComponent item = interfaces.getComponent(956, 24).getComponent(idx);
		for (int c = 0; c < amount; c++) {
			if (lastMessage.contains("don't have enough"))
				break;
			if (!clickComponent(item, "Buy 1")) {
				if (!interfaces.get(956).isValid())
					break;
				c--;
			}
		}
		while (interfaces.get(956).isValid()) {
			interfaces.getComponent(956, 18).interact("Close");
			sleep(700, 1000);
		}
		return true;
	}

	private boolean shopToolkit() {
		boolean ret = true;
		int failCount = 0, sellCount = 0, sellFood = -1;
		lastMessage = "";
		while (!inventory.contains(TOOLKIT) && !inventory.containsAll(TOOLS)) {
			if (failCheck()) {
				ret = false;
				break;
			}
			if (!interfaces.get(956).isValid()) {
				smartSleep(doNpcAction(getNpcInRoom(SMUGGLER), "Trade"), false);
				sleep(200, 400);
				continue;
			}
			boolean sold = false;
			if (sellCount < 3) {
				for (RSItem item : inventory.getItems()) {
					int testID = item.getID();
					if (testID == -1)
						continue;
					if (testID != sellFood && !intMatch(testID, TOOLS) && (intMatch(testID, COINS, TOOLKIT) || 

bounds.contains((Integer) testID)))
						continue;
					for (int c = 0; c < random(3, 5); c++) {
						if (item.interact("Sell 50")) {
							sleep(100, 300);
							if (item.getStackSize() > 50 && item.interact("Sell 50"))
								sleep(30, 300);
							if (!sold) {
								sellCount++;
								sold = true;
							}
							break;
						}
						sleep(10, 100);
					}
				}
				if (sold)
					continue;
			}
			sellFood = -1;
			if (lastMessage.contains("enough money")) {
				failCount++;
				sleep(300, 500);
				if (failCount > 1) {
					sellFood = foodLowest();
					if (sellFood == -1 && failCount > 2) {
						log(RED, "Unable to purchase a toolkit, trying a new floor.");
						if (developer)
							env.saveScreenshot(false);
						exit = true;
						ret = false;
						break;
					}
				}
				lastMessage = "";
			} else {
				RSComponent scrollBar = interfaces.getComponent(956, 25);
				Point sP = scrollBar.getComponent(5).getCenter();
				while (scrollBar.isValid() && scrollBar.getComponent(3).getRelativeY() < 225) {
					mouse.click(sP.x, sP.y - 25, 5, 17, true);
					sleep(200, 600);
				}
				if (memberWorld && dungeonCount == 0)
					memberWorld = interfaces.getComponent(956, 24).getComponents().length > 400;
					if (clickComponent(interfaces.getComponent(956, 24).getComponent(memberWorld ? 469 : 254), 

"Buy 1")) {
						for (int c = 0; c < 10; c++) {
							if (inventory.contains(TOOLKIT))
								break;
							sleep(100, 200);
						}
					}
			}
		}
		while (interfaces.get(956).isValid()) {
			interfaces.getComponent(956, 18).interact("Close");
			sleep(700, 1000);
		}
		return ret;
	}

	private boolean hidePaint = false, backPage = false, pauseiDungeon = false, confirmToStop = false;
	private boolean showStats = true, showOptions = false, showSettings = false, showChat = false;

	private final Option[] opTab1 = { Option.MELEE, Option.RANGE, Option.MAGIC, Option.SECONDARY_MELEE, Option.SECONDARY_RANGE, 

Option.SECONDARY_MAGIC, Option.LOAD_SETTINGS, Option.SAVE_SETTINGS };
	private final Option[] opTab2 = { Option.BONES, Option.ARROWS, Option.CRABS, Option.PRAY, Option.QUICK_PRAY, 

Option.PRAY_DOORS, Option.PURE, Option.STYLE_SWAP, Option.SUMMON, Option.MAKE_FOOD, Option.EXPLORER, Option.LOGOUT };
	private final Option[] opTab3 = { Option.PRESTIGE, Option.MEDIUM, Option.DEBUG, Option.VIDEO, Option.RUSH, Option.RUSH_FOOD };
	private final Option[][] options = { opTab1, opTab2, opTab3 };

	private static final Color GN1 = new Color(0, 255, 0, 130);
	private static final Color GN2 = new Color(0, 180, 0, 80);
	private static final Color RD1 = new Color(255, 0, 0, 130);
	private static final Color RD2 = new Color(180, 0, 0, 100);

	private final Color BLU = new Color(0, 0, 255);
	private final Color BL1 = new Color(0, 0, 255, 150);
	private final Color BLK = new Color(0, 0, 0);
	private final Color BT1 = new Color(0, 0, 0, 225);
	private final Color BT2 = new Color(0, 0, 0, 180);
	private final Color BT3 = new Color(0, 0, 0, 110);
	private final Color BT4 = new Color(0, 0, 0, 200);
	private final Color DPL = new Color(255, 0, 255);
	private final Color GLD = new Color(255, 215, 0, 140);
	private final Color GRN = new Color(0, 240, 0);
	private final Color GN3 = new Color(0, 255, 0, 100);
	private final Color GN5 = new Color(0, 150, 0);
	private final Color RED = new Color(255, 0, 0);
	private final Color PRP = new Color(160, 0, 160);
	private final Color PT1 = new Color(160, 0, 160, 100);
	private final Color PT2 = new Color(160, 0, 160, 160);
	private final Color PT3 = new Color(160, 0, 160, 40);
	private final Color SKY = new Color(0, 190, 255, 140);
	private final Color WHT = new Color(255, 255, 255);
	private final Color WT1 = new Color(255, 255, 255, 100);
	private final Color WT2 = new Color(255, 255, 255, 150);
	private final Color WT3 = new Color(255, 255, 255, 220);
	private final Color YLW = new Color(240, 240, 0);

	private final Font FONT_1 = new Font("Arial", Font.BOLD, 13);
	private final Font FONT_2 = new Font("Trebuchet MS", 0, 15);
	private final Font FONT_3 = new Font("Trebuchet MS", 0, 11);
	private final Font FONT_4 = new Font("Arial", Font.BOLD, 12);

	private final Rectangle RECT_STATS = new Rectangle(7, 344, 80, 17);
	private final Rectangle RECT_OPTIONS = new Rectangle(7 + 84, 344, 80, 17);
	private final Rectangle RECT_STATUS = new Rectangle(7 + 168, 344, 222, 17);
	private final Rectangle RECT_CONTROL = new Rectangle(7 + 394, 344, 112, 19);
	private final Rectangle RECT_CHAT = new Rectangle(7, 458, 506, 16);
	private final Rectangle RECT_TAB_1 = new Rectangle(6, 363, 81, 32);
	private final Rectangle RECT_TAB_2 = new Rectangle(6, 395, 81, 32);
	private final Rectangle RECT_TAB_3 = new Rectangle(6, 427, 81, 31);

	private final Rectangle RECT_C_UP = new Rectangle(121, 389, 20, 15);
	private final Rectangle RECT_C_DOWN = new Rectangle(121, 420, 20, 15);
	private final Rectangle RECT_RC_UP = new Rectangle(310, 389, 20, 15);
	private final Rectangle RECT_RC_DOWN = new Rectangle(310, 420, 20, 15);
	private final Rectangle RECT_F_UP = new Rectangle(380, 389, 20, 15);
	private final Rectangle RECT_F_DOWN = new Rectangle(380, 420, 20, 15);
	private final Rectangle RECT_MINI_UP = new Rectangle(285, 367, 9, 8);
	private final Rectangle RECT_MINI_DOWN = new Rectangle(285, 375, 9, 7);
	private final Rectangle RECT_PLAY = new Rectangle(405, 346, 18, 18);
	private final Rectangle RECT_INFO = new Rectangle(425, 346, 18, 18);
	private final Rectangle RECT_HELP = new Rectangle(445, 346, 18, 18);
	private final Rectangle RECT_STOP = new Rectangle(465, 346, 18, 18);
	private final Rectangle RECT_HIDE = new Rectangle(495, 346, 18, 18);
	private final Rectangle RECT_CONTINUE = new Rectangle(438, 437, 69, 16);
	private final Rectangle RECT_BACK = new Rectangle(12, 437, 69, 16);
	private final Rectangle RECT_BLOCK_1 = new Rectangle(7, 458, 100, 16);
	private final Rectangle RECT_BLOCK_2 = new Rectangle(570, 235, 133, 12);
	private final Rectangle RECT_BLOCK_3 = new Rectangle(367, 56, 100, 15);
	private final Rectangle RECT_PICKUP = new Rectangle(97, 387, 197, 18);
	private final Rectangle RECT_DESTROY = new Rectangle(305, 387, 197, 18);

	final Point o = new Point(7, 344);
	private int page = 0, sTab = 1, oTab = 1, typeTo = -1;
	private String username = "", password = "", pickupName = "", destroyName = "";

	private void drawBevel(final Graphics g, final Rectangle r) {
		g.draw3DRect(r.x, r.y, r.width, r.height, true);
	}

	private void drawRect(final Graphics g, final Rectangle r) {
		g.fillRect(r.x, r.y, r.width, r.height);
	}

	private void drawTile(final Graphics g, final RSTile t, double m) {
		if (t == null)
			return;
		final int tX = t.getX(), tY = t.getY();
		Point p1 = calc.worldToMinimap(tX - m, tY - m), p2 = calc.worldToMinimap(tX - m, tY + m);
		Point p3 = calc.worldToMinimap(tX + m, tY + m), p4 = calc.worldToMinimap(tX + m, tY - m);
		if (p1.x != -1 && p2.x != -1 && p3.x != -1 && p4.x != -1) {
			int[] allX = new int[] { p1.x, p2.x, p3.x, p4.x };
			int[] allY = new int[] { p1.y, p2.y, p3.y, p4.y };
			g.fillPolygon(allX, allY, 4);
		}
	}

	public void onRepaint(final Graphics g) {
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (!authCheck || game.isLoggedIn()) {
			g.setFont(FONT_2);
			if (Option.DEBUG.enabled() && inDungeon && currentRoom != null && targetRoom != null) {
				if (skipRooms.contains(targetRoom)) {
					g.setColor(GLD);
				} else if (startRoom != null && areaEquals(targetRoom, startRoom)) {
					g.setColor(SKY);
				} else if ((puzzleRooms.contains(targetRoom)) || (bossRoom != null && areaEquals(targetRoom, 

bossRoom))) {
					g.setColor(RD1);
				} else if (finishedRooms.contains(targetRoom)) {
					g.setColor(PT2);
				} else if (finish || clearedRooms.contains(targetRoom)) {
					g.setColor(GN3);
				} else g.setColor(BL1);
				for (RSTile f : roomFlags) {
					if (!f.equals(safeTile))
						drawTile(g, f, 0.5);
				}
				if (safeTile != null) {
					g.setColor(GRN);
					drawTile(g, safeTile, 0.5);
				}
				for (RSTile dT : drawDoors) {
					if (calc.distanceBetween(myLoc(), dT) < 17) {
						RSObject dObj = objects.getTopAt(dT);
						if (dObj != null) {
							if (finishedDoors.contains(dT)) {
								g.setColor(DPL);
							} else if (lockedDoors.contains(dT) || blockedDoors.contains(dT)) {
								g.setColor(RED);
							} else if (openedDoors.contains(dT)) {
								g.setColor(GRN);
							} else g.setColor(YLW);
							for (RSTile t : dObj.getArea().getTileArray()) {
								drawTile(g, t, 0.5);
							}
						}
					}
				}
				g.setColor(BL1);
				for (RSTile t : puzzlePoints) {
					if (!t.equals(safeTile)) {
						drawTile(g, t, 0.3);
					}
				}
			}
			if (Option.VIDEO.enabled()) {
				g.setColor(BLK);
				drawRect(g, RECT_BLOCK_1);
				if ((exit || status.contains("Entering D")) && game.getCurrentTab() != Game.TAB_INVENTORY)
					drawRect(g, RECT_BLOCK_2);
				if (status.contains("next dungeon...") || (newDungeon && dungeonCount != 0 && startRoom == null))
					drawRect(g, RECT_BLOCK_3);
			}
			if (!hidePaint) {
				if (RECT_CHAT.contains(mp)) {
					g.setColor(BLK);
					g.drawString((userRank != 0 ? "Highscores rank: " + userRank + " || " : "") + (userCount != 0 

? userCount + " users online || " : "") + "by kiko & ShadowMoose", 92 + rankOffset + userOffset, 472);
				}
				g.setColor(BT1);
				if (showSettings && page != 1)
					g.fillRect(7, 363, 80, 95);
				g.fillRect(87, 363, 426, 95);
				g.setColor(BLK);
				if (showChat) {
					g.setColor(BT3);
				} else if (RECT_CHAT.contains(mp)) {
					g.setColor(BT4);
				}
				drawRect(g, RECT_CHAT);
				g.setColor(WT2);
				g.drawLine(6, 458, 512, 458);
				if (showStats || showOptions || (showSettings && page == 1)) {
					g.drawLine(87, 363, 87, 457);
					g.drawLine(87, RECT_TAB_1.y, 511, RECT_TAB_1.y);
				}
				if (showStats && !showSettings) {
					double runTime = runTimer.getElapsed();
					if (settingsFinished && runTime != 0) {
						attGain = skills.getCurrentExp(Skills.ATTACK) - attStart;
						strGain = skills.getCurrentExp(Skills.STRENGTH) - strStart;
						defGain = skills.getCurrentExp(Skills.DEFENSE) - defStart;
						hitGain = skills.getCurrentExp(Skills.CONSTITUTION) - hitStart;
						rngGain = skills.getCurrentExp(Skills.RANGE) - rngStart;
						mgcGain = skills.getCurrentExp(Skills.MAGIC) - mgcStart;
						prayGain = skills.getCurrentExp(Skills.PRAYER) - prayStart;
						runeGain = skills.getCurrentExp(Skills.RUNECRAFTING) - runeStart;
						mineGain = skills.getCurrentExp(Skills.MINING) - mineStart;
						fireGain = skills.getCurrentExp(Skills.FIREMAKING) - fireStart;
						woodGain = skills.getCurrentExp(Skills.WOODCUTTING) - woodStart;
						crftGain = skills.getCurrentExp(Skills.CRAFTING) - crftStart;
						smthGain = skills.getCurrentExp(Skills.SMITHING) - smthStart;
						agilGain = skills.getCurrentExp(Skills.AGILITY) - agilStart;
						thevGain = skills.getCurrentExp(Skills.THIEVING) - thevStart;
						summGain = skills.getCurrentExp(Skills.SUMMONING) - summStart;
						herbGain = skills.getCurrentExp(Skills.HERBLORE) - herbStart;
						cnstGain = skills.getCurrentExp(Skills.CONSTRUCTION) - cnstStart;
						cookGain = skills.getCurrentExp(Skills.COOKING) - cookStart;
						fishGain = skills.getCurrentExp(Skills.FISHING) - fishStart;
						slayGain = skills.getCurrentExp(Skills.SLAYER) - slayStart;
						dungGain = skills.getCurrentExp(Skills.DUNGEONEERING) - dungStart;
						ceph = (int) (3600000 / runTime * (attGain + defGain + strGain + rngGain + mgcGain + 

hitGain + prayGain));
						deph = (int) (3600000 / runTime * dungGain);
						seph = (int) (3600000 / runTime * (runeGain + mineGain + fireGain + woodGain + 

smthGain + crftGain + agilGain + thevGain + summGain + herbGain + cnstGain + cookGain + fishGain + slayGain));
						fph = Math.rint((3600000 / runTime * dungeonCount) * 10) / 10;
					}
					if (sTab == 1) {
						g.setColor(BT1);
						drawRect(g, RECT_TAB_1);
						g.setColor(RECT_TAB_2.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_2);
						g.setColor(RECT_TAB_3.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_3);
						g.setColor(WT3);
						drawBevel(g, RECT_TAB_1);
						g.clearRect(RECT_TAB_1.x + RECT_TAB_1.width, RECT_TAB_1.y + 1, 1, RECT_TAB_1.height - 

1);
						g.drawString("Total Time: " + timeFormat(runTimer.getElapsed()), 93, 380);
						g.drawString("Dung. Level: " + dungLevel + " (+" + (dungLevel - dungStartLevel) + ")", 

93, 398);
						g.drawString("Exp per Hour: " + deph, 93, 416);
						g.drawString("Exp Gained: " + dungGain, 93, 434);
						g.drawString("Tokens Gained: " + tokens, 93, 452);
						g.drawString("Time to Level " + levelGoal + ": " + (deph > 0 ? timeFormat

(getDungExpToLevel() * 360 / deph * 10000) : EMPTY_TIMER), 295, 380);
						g.drawString("Dungeons Completed: " + dungeonCount + " (" + abortedCount + ")", 295, 

398);
						g.drawString("Floors per Hour: " + fph, 295, 416);
						g.drawString("Deaths: " + deaths + " (" + perFloor(deaths) + " per floor)", 295, 434);
						g.drawString("Puzzles: " + puzzleCount + " (" + perFloor(puzzleCount) + " per floor)", 

295, 452);
						g.drawString(RECT_MINI_UP.contains(mp) || RECT_MINI_DOWN.contains(mp) ? MINIS_S : 

MINIS, RECT_MINI_UP.x + 1, RECT_MINI_UP.y + 1);
					} else if (sTab == 2) {
						String roomString = "" + roomNumber;
						if (roomNumber == 1) {
							roomString = "Start";
						} else if (bossFight) {
							roomString = "Boss";
						}
						g.setColor(RECT_TAB_1.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_1);
						g.setColor(BT1);
						drawRect(g, RECT_TAB_2);
						g.setColor(RECT_TAB_3.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_3);
						g.setColor(WT3);
						drawBevel(g, RECT_TAB_2);
						g.clearRect(RECT_TAB_2.x + RECT_TAB_2.width, RECT_TAB_2.y + 1, 1, RECT_TAB_2.height - 

1);
						g.drawString("Floor Time: " + (dungTimer != null ? dungTimer.toElapsedString() : 

EMPTY_TIMER), 93, 380);
						g.drawString("Floor Type: " + floor.getName(), 93, 398);
						g.drawString("Floor Number: " + (fNumber > 0 ? fNumber : "??") + " (C" + aComplexity + 

")", 93, 416);
						g.drawString("Current Room: " + roomString + " (of " + rooms.size() + ")", 93, 434);
						g.drawString("Prestige Count: " + prestigeCount, 93, 452);
						g.drawString("Progress: " + (maxFloor > 0 ? cProg + " of " + maxFloor + " floors" : 

"Unknown"), 295, 380);
						g.drawString("Avg. Floor Time: " + tpf + " mins", 295, 398);
						g.drawString("Fastest Dungeon: " + fastestTime, 295, 416);
						g.drawString("Slowest Dungeon: " + slowestTime, 295, 434);
						g.drawString("Floor Success Rate: " + (dungeonCount + abortedCount != 0 ? dungeonCount 

* 100 / (dungeonCount + abortedCount) : 100) + "%" , 295, 452);
					} else if (sTab == 3) {
						g.setColor(RECT_TAB_1.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_1);
						g.setColor(RECT_TAB_2.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_2);
						g.setColor(BT1);
						drawRect(g, RECT_TAB_3);
						String cbName = "Melee";
						String cbStyle = "";
						if (primaryStyle == Style.MELEE) {
							if (attGain > 0) {
								cbStyle = defGain > 0 ? "Controlled" : "Accurate";
							} else if (defGain > 0) {
								cbStyle = "Defensive";
							} else if (strGain > 0) {
								cbStyle = "Aggressive";
							} else cbStyle = "Unknown";
						} else if (primaryStyle == Style.RANGE) {
							cbName = "Range";
							cbStyle = defGain > 0 ? "Longrange" : "Rapid";
						} else if (primaryStyle == Style.MAGIC) {
							cbStyle = "Magic";
							cbStyle = defGain > 0 ? "Defensive casting" : "Autocasting";
						}
						g.setColor(WT3);
						drawBevel(g, RECT_TAB_3);
						g.clearRect(RECT_TAB_3.x + RECT_TAB_3.width, RECT_TAB_3.y + 1, 1, RECT_TAB_3.height - 

1);
						g.drawString(cbName + " Style: " + cbStyle, 93, 380);
						g.drawString("Pray gain: " + prayGain + ", Def gain: " + defGain, 93, 398);
						g.drawString("Combat Exp per Hour: " + ceph, 93, 416);
						g.drawString("Skills Exp per Hour: " + seph, 93, 434);
						g.drawString("Total Exp per Hour: " + (ceph + deph + seph), 93, 452);
					}
					g.setFont(FONT_1);
					g.setColor(WT3);
					g.drawString("Overview", 18, 384);
					g.drawString("Dungeons", 16, 415);
					g.drawString("Misc.", 32, 446);
					g.setColor(BT4);
					g.fillRect(7, 361, 80, 2);
				} else if (RECT_STATS.contains(mp)) {
					g.setColor(BLK);
					g.setFont(FONT_1);
					g.drawString("Statistics", 19, 358);
					g.setFont(FONT_2);
					g.setColor(BT2);
				} else g.setColor(BT4);
				drawRect(g, RECT_STATS);
				if (showOptions) {
					g.setColor(BT4);
					g.fillRect(91, 361, 80, 2);
				} else if (RECT_OPTIONS.contains(mp)) {
					g.setColor(BLK);
					g.setFont(FONT_1);
					g.drawString("Options", 108, 358);
					g.setFont(FONT_2);
					g.setColor(BT2);
				} else g.setColor(BT4);
				drawRect(g, RECT_OPTIONS);
				if ((showOptions || (showSettings && page < 2)) && oTab != 3) {
					for (Option o : options[oTab]) {
						g.setColor(o.color());
						drawRect(g, o.button());
						g.setColor(WT3);
						drawBevel(g, o.button());
						g.setColor(WT3);
						g.drawString(o.getName(), o.p().x, o.p().y);
					}
				}
				if (showOptions || page == 1) {
					if (oTab == 1) {
						g.setColor(BT1);
						drawRect(g, RECT_TAB_1);
						g.setColor(RECT_TAB_2.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_2);
						g.setColor(!showSettings && RECT_TAB_3.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_3);
						g.setColor(WT3);
						drawBevel(g, RECT_TAB_1);
						g.clearRect(RECT_TAB_1.x + RECT_TAB_1.width, RECT_TAB_1.y + 1, 1, RECT_TAB_1.height - 

1);
						g.setColor(WHT);
						g.drawString("Pickup:", 100, 383);
						g.drawString("Prayer:", 100, 405);
						g.drawString("Exp:", 115, 427);
						g.drawString("Other:", 105, 449);
					} else if (oTab == 2) {
						g.setColor(RECT_TAB_1.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_1);
						g.setColor(BT1);
						drawRect(g, RECT_TAB_2);
						g.setColor(!showSettings && RECT_TAB_3.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_3);
						g.setColor(WT3);
						drawBevel(g, RECT_TAB_2);
						g.clearRect(RECT_TAB_2.x + RECT_TAB_2.width, RECT_TAB_2.y + 1, 1, RECT_TAB_2.height - 

1);
						g.setColor(PT3);
						g.fillRect(124, 403, 19, 19);
						g.fillRect(310, 403, 19, 19);
						g.fillRect(380, 403, 22, 19);
						g.setColor(WT2);
						g.draw3DRect(124, 403, 19, 19, true);
						g.draw3DRect(310, 403, 19, 19, true);
						g.draw3DRect(380, 403, 22, 19, true);
						g.setColor(WHT);
						g.drawString("Complexity:", 93, 382);
						g.drawString("Rush Settings:", 270, 382);
						g.drawString("" + complexity, 130, 418);
						g.drawString("" + rComplexity, 316, 418);
						g.drawString("" + rushTo, rushTo < 10 ? 387 : 383, 418);
						g.setColor(WT3);
						g.drawString("Complexity", 280, 452);
						g.drawString("To floor", 365, 452);
						g.drawString(RECT_C_UP.contains(mp) ? UPARROW_S : UPARROW, 124, 387);
						g.drawString(RECT_C_DOWN.contains(mp) ? DOWNARROW_S : DOWNARROW, 124, 422);
						g.drawString(RECT_RC_UP.contains(mp) ? UPARROW_S : UPARROW, 310, 387);
						g.drawString(RECT_RC_DOWN.contains(mp) ? DOWNARROW_S : DOWNARROW, 310, 422);
						g.drawString(RECT_F_UP.contains(mp) ? UPARROW_S : UPARROW, 381, 387);
						g.drawString(RECT_F_DOWN.contains(mp) ? DOWNARROW_S : DOWNARROW, 381, 422);
					} else if (oTab == 3 && !showSettings) {
						g.setColor(RECT_TAB_1.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_1);
						g.setColor(RECT_TAB_2.contains(mp) ? BT2 : BT4);
						drawRect(g, RECT_TAB_2);
						g.setColor(BT1);
						drawRect(g, RECT_TAB_3);
						g.setColor(WHT);
						g.drawString("Loot Item:", RECT_PICKUP.x, RECT_PICKUP.y - 5);
						g.drawString("Destroy Item:", RECT_DESTROY.x, RECT_DESTROY.y - 5);
						g.setColor(PT3);
						drawRect(g, RECT_PICKUP);
						drawRect(g, RECT_DESTROY);
						g.setColor(WT3);
						drawBevel(g, RECT_TAB_3);
						g.clearRect(RECT_TAB_3.x + RECT_TAB_3.width, RECT_TAB_3.y + 1, 1, RECT_TAB_3.height - 

1);
						g.drawString("Type in the exact item names (case-insensitive).", RECT_PICKUP.x + 1, 

RECT_PICKUP.y + 37);
						g.drawString("Enter an item to destroy if you don't have an extra bind slot.", 

RECT_PICKUP.x + 1, RECT_PICKUP.y + 53);
						drawBevel(g, RECT_PICKUP);
						drawBevel(g, RECT_DESTROY);
						if (pickupName.isEmpty() && typeTo != 0) {
							g.setColor(WT1);
							g.drawString("Item to Look For", RECT_PICKUP.x + 4, RECT_PICKUP.y + 15);
						} else g.drawString(pickupName + (typeTo == 0 ? "_" : ""), RECT_PICKUP.x + 4, 

RECT_PICKUP.y + 15);
						if (destroyName.isEmpty() && typeTo != 1) {
							g.setColor(WT1);
							g.drawString("Optional Item to Destroy", RECT_DESTROY.x + 4, RECT_DESTROY.y + 

15);
						} else {
							g.setColor(WT3);
							g.drawString(destroyName + (typeTo == 1 ? "_" : ""), RECT_DESTROY.x + 4, 

RECT_DESTROY.y + 15);
						}
					}
					g.setFont(FONT_1);
					g.setColor(WT3);
					g.drawString("General", 22, 383);
					g.drawString("Complexity", 12, 415);
					if (!showSettings)
						g.drawString("Binding", 22, 447);
				}
				if (showSettings) {
					g.setFont(FONT_2);
					if (page > 0) {
						if (RECT_BACK.contains(mp)) {
							g.setColor(BT1);
							g.drawString("Back", 31, 450);
							g.setColor(PT2);
						} else g.setColor(PT1);
						drawRect(g, RECT_BACK);
						g.setColor(WT3);
						drawBevel(g, RECT_BACK);
						g.setColor(WHT);
						g.drawString("Back", 32, 451);
					}
					if (RECT_CONTINUE.contains(mp)) {
						g.setColor(BT1);
						g.drawString("Continue", 442, 450);
						g.setColor(PT2);
					} else g.setColor(PT1);
					drawRect(g, RECT_CONTINUE);
					g.setColor(WT3);
					drawBevel(g, RECT_CONTINUE);
					g.setColor(WHT);
					g.drawString("Continue", 443, 451);
					if (page == 0) {
						g.drawString("Choose your primary combat style", 15, 382);
						g.drawString("Choose your secondary combat style", 15, 422);
						g.drawString("Settings options:", 300, 382);
					} else if (page == 2) {
						if (Option.MELEE.enabled()) {
							page = backPage ? --page : ++page;
						} else if (Option.MAGIC.enabled()) {
							g.drawString("Wield your staff and blastbox (optional) to use.", 15, 380);
							g.drawString("Then turn the spell you want to use to autocast.", 15, 395);
							if (isAutoCasting()) {
								Spell spell = getSpell(getSelectedSpell());
								if (spell != null)
									combatSpell = spell;
								g.setColor(WT3);
							}
							g.drawString("Spell selected: " + (combatSpell != null ? combatSpell.getName() 

: "None"), 15, 415);
						} else if (Option.RANGE.enabled()) {
							g.drawString("Bind and wield the arrows you would like to use.", 15, 380);
						}
					} else if (page == 3) {
						if (Option.SECONDARY_MELEE.enabled()) {
							RSItem selected = null;
							String name = null;
							g.drawString("Bind your backup melee weapon.", 15, 395);
							g.drawString("Right click the item and select 'Use'.", 15, 410);
							if (game.getCurrentTab() == Game.TAB_INVENTORY)
								selected = inventory.getSelectedItem();
							if (selected != null) {
								secondaryWep = selected.getID();
								name = equipmentName(selected);
							}
							g.drawString("Secondary Weapon: " + (name != null ? name : "None"), 15, 425);
						} else if (Option.SECONDARY_RANGE.enabled()) {
							RSItem selected = null;
							String name = null;
							g.drawString("Bind the bow and arrows you would like to use.", 15, 380);
							g.drawString("Equip the arrows and leave the bow in your inventory.", 15, 

395);
							g.drawString("Right click the bow and select 'Use'.", 15, 410);
							if (game.getCurrentTab() == Game.TAB_INVENTORY)
								selected = inventory.getSelectedItem();
							if (selected != null) {
								secondaryWep = selected.getID();
								name = equipmentName(selected);
							}
							g.drawString("Secondary Weapon: " + (name != null ? name : "None"), 15, 425);
						} else if (Option.SECONDARY_MAGIC.enabled()) {
							g.drawString("You'll need a filled blastbox or combat runes to use secondary 

magic.", 15, 380);
							g.drawString("Autocast the spell to use, then return the items to your 

inventory.", 15, 395);
							if (isAutoCasting()) {
								Spell spell = getSpell(getSelectedSpell());
								if (spell != null)
									combatSpell = spell;
								if (game.getCurrentTab() == Game.TAB_INVENTORY) {
									int staff = getItemID("staff (b)");
									int bBox = getItemID("blastbox (b)");
									if (bBox == -1)
										bBox = getItemID("surgebox (b)");
									if (bBox != -1)
										blastBox = bBox;
									if (staff != -1)
										secondaryWep = staff;
								}
								g.setColor(WT3);
							}
							g.drawString("Spell: " + (combatSpell != null ? combatSpell.getName() : 

"None") + ", Staff: " + (secondaryWep > 0 ? game.getCurrentTab() == Game.TAB_INVENTORY ? equipmentName(inventory.getItem

(secondaryWep)).replace(" staff", "") : secondaryWep : "None") + ", Blastbox: " + (blastBox > 0 ? "Yes" : "No"), 15, 415);
						} else page = backPage ? --page : ++page;
					} else if (page == 4) {
						g.drawString("Be sure to bind and wield the primary weapon you would like to use.", 

15, 380);
						g.drawString("Then select the combat style you would like to use.", 15, 395);
						g.drawString("Press continue to complete the settings.", 15, 421);
					}
					g.setColor(BT1);
					g.fillRect(175, 361, 222, 2);
				}
				g.setColor(RECT_STATUS.contains(mp) ? BT4 : BT1);
				drawRect(g, RECT_STATUS);
				if (RECT_STATUS.contains(mp)) {
					g.setColor(BLK);
					g.setFont(FONT_4);
					if (!showSettings) {
						if (!RECT_CONTROL.contains(mp) || controlStatus.isEmpty()) {
							if (secondaryStatus.isEmpty() || !showSecondary) {
								g.drawString(status, 181, 358);
							} else g.drawString("+ " + secondaryStatus, 181, 358);
						} else g.drawString(controlStatus, 181, 358);
					} else g.drawString(statusSettings, 181, 358);
				}
				g.setColor(PRP);
				g.setFont(FONT_1);
				g.drawString("Statistics", 18, 357);
				g.drawString("Options", 107, 357);
				g.setFont(FONT_4);
				if (!showSettings) {
					if (controlStatus.isEmpty() || !RECT_CONTROL.contains(mp)) {
						g.setColor(GN1);
						g.drawString(secondaryStatus.isEmpty() || !showSecondary ? status : "+ " + 

secondaryStatus, 180, 357);
					} else {
						g.setColor(RED);
						g.drawString(controlStatus, 180, 357);
					}
				} else g.drawString(statusSettings, 180, 357);
				g.setColor(PRP);
				if (settingsFinished) {
					g.setFont(FONT_3);
					g.drawString("v" + VERSION, 484, 455);
				}
				g.setFont(FONT_2);
				g.drawString((userRank != 0 ? "Highscores rank: " + userRank + " || " : "") + (userCount != 0 ? 

userCount + " users online || " : "") + "by kiko & ShadowMoose", 91 + rankOffset + userOffset, 471);
			}
			g.setColor(BT1);
			drawRect(g, RECT_CONTROL);
			if (RECT_CONTROL.contains(mp)) {
				if (!pauseiDungeon) {
					g.drawString(RECT_PLAY.contains(mp) ? PLAY_S : PLAY, RECT_PLAY.x, RECT_PLAY.y);
				} else g.drawString(RECT_PLAY.contains(mp) ? PAUSE_S : PAUSE, RECT_PLAY.x, RECT_PLAY.y);
				g.drawString(RECT_INFO.contains(mp) ? INFO_S : INFO, RECT_INFO.x, RECT_INFO.y);
				g.drawString(RECT_HELP.contains(mp) ? HELP_S : HELP, RECT_HELP.x, RECT_HELP.y);
				g.drawString(RECT_STOP.contains(mp) ? STOP_S : STOP, RECT_STOP.x, RECT_STOP.y);
			} else g.drawString(LOGO, RECT_CONTROL.x, RECT_CONTROL.y);
			g.drawString(RECT_HIDE.contains(mp) ? HIDE_S : HIDE, RECT_HIDE.x, RECT_HIDE.y);
		}
	}

	public void keyPressed(KeyEvent e) { }

	public void keyReleased(KeyEvent e) { }

	public void keyTyped(KeyEvent e) {
		if (!hidePaint && showOptions && oTab == 3) {
			char key = e.getKeyChar();
			if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_TAB) {
				typeTo = typeTo < 1 ? ++typeTo : -1;
				env.setUserInput(typeTo != -1 ? 0 : Environment.INPUT_KEYBOARD);
			} else if (typeTo == 0) {
				if (key == KeyEvent.VK_BACK_SPACE && pickupName.length() > 0) {
					pickupName = pickupName.substring(0, pickupName.length() - 1);
				} else if (key != KeyEvent.VK_BACK_SPACE && pickupName.length() < 25) {
					pickupName += key;
				}
			} else if (typeTo == 1) {
				if (key == KeyEvent.VK_BACK_SPACE && destroyName.length() > 0) {
					destroyName = destroyName.substring(0, destroyName.length() - 1);
				} else if (key != KeyEvent.VK_BACK_SPACE && destroyName.length() < 25) {
					destroyName += key;
				}
			}
		}
	}

	public void messageReceived(MessageEvent e) {
		int id = e.getID();
		String msg = e.getMessage();
		if (id == MessageEvent.MESSAGE_SERVER) { 
			if (msg.equals("Welcome to RuneScape.")) {
				welcomeBack = true;
			} else if (msg.startsWith("Oh dear")) {
				isDead = true;
			} else if (msg.startsWith("Floor <")) {
				int idx = msg.indexOf(">") + 1;
				if (idx != 0) {
					fNumber = Integer.parseInt(msg.substring(idx, idx + 2).trim());
					cProg = fNumber - 1;
				}
			} else if (msg.startsWith("You unlock the door")) {
				lockDown = true;
			} else if (msg.contains("been frozen") || msg.contains("force prevents") || msg.contains("force stops") || 

msg.contains("You're stunned")) {
				unreachable = true;
			} else if (msg.contains("cannot teleport")) {
				finish = true;
			} else if ((msg.startsWith("You need a") || msg.contains("require a")) && msg.contains("level of")) {
				levelRequirement = false;
			} else if (msg.contains("need at least ") && msg.contains("to do that")) {
				levelRequirement = false;
			} else if (combatStyle == Style.RANGE && msg.contains("ammo left in") && !secondaryStatus.startsWith

("Swapping")) {
				outOfAmmo = true;
			} else if (combatStyle == Style.MAGIC && msg.contains("runes to cast") && !secondaryStatus.startsWith

("Swapping") && (msg.contains("air") || msg.contains("chaos") || msg.contains("death") || msg.contains("blood"))) {
				outOfAmmo = true;
			} else if (msg.contains("advanced a Dung")) {
				dungLevel++;
				if (levelGoal <= dungLevel)
					levelGoal = dungLevel + 1;
			} else if (puzzleTimer != null) {
				if (msg.contains("toggle back off") || msg.contains("both the chest in")) {
					roomSwitch = true;
				} else if (msg.contains("hear a click") || msg.contains("now unlocked") || msg.contains("challenge 

room has already")) {
					puzzleSolved = true;
				} else if (msg.contains("sever the plant") || msg.contains("monolith reaches")) {
					puzzleSolved = true;
				}
			} else if (bossFight) {
				if (msg.contains("got caught")) {
					roomSwitch = true;
				}
				if (msg.contains(" pull")) {
					roomSwitch = true;
				} else if (msg.contains("received item: ")) {
					itemReceived = true;
				} else if (msg.contains("prayer") && (msg.contains("deactivated") || msg.contains("injured"))) {
					prayTimer = new Timer(random(10000, 20000));
				} else if (floor == Floor.WARPED) {
					if (msg.contains("consumes your prayers"))
						prayTimer = new Timer(random(25000, 35000));
				}
			}
		} else if (id == 4 && msg.contains("can't reach")) {
			unreachable = true;
		}
		if (id == MessageEvent.MESSAGE_SERVER || id == MessageEvent.MESSAGE_EXAMINE_NPC || id == MessageEvent.MESSAGE_ACTION)
			lastMessage = msg;
	}

	public void mouseMoved(MouseEvent e) {
		mp = e.getPoint();
		if (RECT_CONTROL.contains(mp)) {
			if (RECT_PLAY.contains(mp)) {
				controlStatus = (!pauseiDungeon ? "Pause" : "Resume") + " iDungeon";
			} else if (RECT_STOP.contains(mp)) {
				controlStatus = "Terminate iDungeon";
			} else if (RECT_INFO.contains(mp)) {
				controlStatus = "Visit the iDungeon forums";
			} else if (RECT_HELP.contains(mp)) {
				controlStatus = "iDungeon help guide";
			} else if (RECT_HIDE.contains(mp)) {
				controlStatus = "Hide paint";
			} else controlStatus = "";
		} else if (showSettings) {
			statusSettings = statusSettings.isEmpty() || statusSettings.startsWith("Hover") ? "Hover over an option to 

find out more" : "Please choose your settings";
			if (page > 0 && RECT_BACK.contains(mp)) {
				statusSettings = "<<< Previous page";
			} else if (RECT_CONTINUE.contains(mp)) {
				statusSettings = page < 5 ? statusSettings = "Next page >>>" : "Finish the settings";
			} else if (RECT_CHAT.contains(mp)) {
				statusSettings = "These guys kick ass!";
			} else if (page == 0 && oTab == 0) {
				if (Option.MELEE.contains(mp)) {
					statusSettings = "Set your primary combat to melee";
				} else if (Option.RANGE.contains(mp)) {
					statusSettings = "Set your primary combat to range";
				} else if (Option.MAGIC.contains(mp)) {
					statusSettings = "Set your primary combat to range";
				} else if (Option.SECONDARY_MELEE.contains(mp)) {
					statusSettings = "Set your secondary combat to melee";
				} else if (Option.SECONDARY_RANGE.contains(mp)) {
					statusSettings = "Set your secondary combat to range";
				} else if (Option.SECONDARY_MAGIC.contains(mp)) {
					statusSettings = "Set your secondary combat to mage";
				} else if (Option.LOAD_SETTINGS.contains(mp)) {
					statusSettings = "Load your settings on startup";
				} else if (Option.SAVE_SETTINGS.contains(mp)) {
					statusSettings = "Save your settings for next time";
				}
			} else if (page == 1) {
				if (RECT_TAB_1.contains(mp)) {
					statusSettings = "General script settings";
				} else if (RECT_TAB_2.contains(mp)) {
					statusSettings = "Advanced floor settings & complexity";
				} else if (!showSettings && RECT_TAB_3.contains(mp)) {
					statusSettings = "Search and bind an item";
				} else if (oTab == 1) {
					if (Option.BONES.contains(mp)) {
						statusSettings = "Pick up and bury bones";
					} else if (Option.ARROWS.contains(mp)) {
						statusSettings = "Collect the arrows you use up";
					} else if (Option.CRABS.contains(mp)) {
						statusSettings = "Loot heim crabs, for extra hp";
					} else if (Option.PRAY.contains(mp)) {
						statusSettings = "Use protection prayers for monsters";
					} else if (Option.QUICK_PRAY.contains(mp)) {
						statusSettings = "Use quick prayers in addition";
					} else if (Option.PRAY_DOORS.contains(mp)) {
						statusSettings = "Enable this to open prayer doors";
					} else if (Option.PURE.contains(mp)) {
						statusSettings = "Prevents defense training";
					} else if (Option.STYLE_SWAP.contains(mp)) {
						statusSettings = "Switch attack styles by weakness";
					} else if (Option.SUMMON.contains(mp)) {
						statusSettings = "Gain summoning experience";
					} else if (Option.MAKE_FOOD.contains(mp)) {
						statusSettings = "Make food when you're running low";
					} else if (Option.EXPLORER.contains(mp)) {
						statusSettings = "Open all explorable rooms on the floor";
					} else if (Option.LOGOUT.contains(mp)) {
						statusSettings = "Avoid the 6-hour logout timer";
					}
				} else if (oTab == 2) {
					if (RECT_C_UP.contains(mp) || RECT_C_DOWN.contains(mp)) {
						statusSettings = "Cycle the Dungeon's Complexity level";
					} else if (RECT_RC_UP.contains(mp) || RECT_RC_DOWN.contains(mp)) {
						statusSettings = "Select the complexity to rush at";
					} else if (RECT_F_UP.contains(mp) || RECT_F_DOWN.contains(mp)) {
						statusSettings = "Select the floor to rush to";
					} else if (Option.PRESTIGE.contains(mp)) {
						statusSettings = "Intelligently reset your progress";
					} else if (Option.MEDIUM.contains(mp)) {
						statusSettings = "Explore Medium dungeons";
					} else if (Option.DEBUG.contains(mp)) {
						statusSettings = "Paint doors and rooms on the map";
					} else if (Option.VIDEO.contains(mp)) {
						statusSettings = "Blacks out all usernames - BETA";
					} else if (Option.RUSH.contains(mp)) {
						statusSettings = "Enable rushing of certain floors";
					} else if (Option.RUSH_FOOD.contains(mp)) {
						statusSettings = "Enable food pickup when rushing";
					}
				}
			}
		}
	}

	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		cp = e.getPoint();
		if (RECT_STATUS.contains(p)) {
			if (confirmToStop) {
				authCheck = true;
				status = "Shutting down iDungeon Pro";
				secondaryStatus = "";
			} else if (secondaryStatus.isEmpty()) {
				showSecondary = true;
			} else showSecondary = !showSecondary;
		} else if (RECT_PLAY.contains(p)) {
			pauseiDungeon = !pauseiDungeon;
		} else if (RECT_STOP.contains(p)) {
			confirmToStop = !confirmToStop;
			showSecondary = true;
			secondaryStatus = confirmToStop ? "Click here to confirm shutdown" : "";
		} else if (RECT_INFO.contains(p)) {
			try {
				Desktop.getDesktop().browse(new URI("http://shadowscripting.org/forums/viewforum.php?f=2"));
			} catch (Exception ex) {
				log(RED, "Unable to load the forums :(");
			}
		} else if (RECT_HELP.contains(p)) {
			try {
				Desktop.getDesktop().browse(new URI("http://shadowscripting.wordpress.com/idungeon-users-guide/"));
			} catch (Exception ex) {
				log(RED, "Unable to load the help manual :(");
			}
		} else if (RECT_HIDE.contains(p)) {
			hidePaint = !hidePaint;
		} else if (!hidePaint) {
			if (RECT_CHAT.contains(p)) {
				showChat = !showChat;
			} else if (RECT_STATS.contains(p) && !showSettings) {
				showStats = true;
				showOptions = false;
			} else if (RECT_OPTIONS.contains(p) && !showSettings) {
				showStats = false;
				showOptions = true;
			} else if (showSettings && page == 0) {
				if (Option.MELEE.contains(p)) {
					if (!Option.SECONDARY_RANGE.enabled())
						Option.ARROWS.set(false);
					Option.MELEE.set(true);
					Option.RANGE.set(false);
					Option.MAGIC.set(false);
					Option.SECONDARY_MELEE.set(false);
				} else if (Option.RANGE.contains(p)) {
					if (!Option.SECONDARY_MAGIC.enabled())
						Option.SECONDARY_MELEE.set(true);
					Option.MELEE.set(false);
					Option.RANGE.set(true);
					Option.MAGIC.set(false);
					Option.SECONDARY_RANGE.set(false);
					Option.ARROWS.set(true);
				} else if (Option.MAGIC.contains(p)) {
					if (!Option.SECONDARY_RANGE.enabled()) {
						Option.ARROWS.set(true);
						Option.SECONDARY_MELEE.set(true);
					}
					Option.MELEE.set(false);
					Option.RANGE.set(false);
					Option.MAGIC.set(true);
					Option.SECONDARY_MAGIC.set(false);
				} else if (Option.SECONDARY_MELEE.contains(p)) {
					if (!Option.MELEE.enabled()) {
						Option.SECONDARY_MELEE.toggle();
						if (Option.SECONDARY_MELEE.enabled()) {
							Option.SECONDARY_RANGE.set(false);
							Option.SECONDARY_MAGIC.set(false);
						}
						if (!Option.RANGE.enabled())
							Option.ARROWS.set(true);
					}
				} else if (Option.SECONDARY_RANGE.contains(p)) {
					if (!Option.RANGE.enabled()) {
						Option.SECONDARY_RANGE.toggle();
						if (Option.SECONDARY_RANGE.enabled()) {
							Option.SECONDARY_MELEE.set(false);
							Option.SECONDARY_MAGIC.set(false);
						}
						Option.ARROWS.set(true);
					}
				} else if (Option.SECONDARY_MAGIC.contains(p)) {
					if (!Option.MAGIC.enabled()) {
						Option.SECONDARY_MAGIC.toggle();
						if (Option.SECONDARY_MAGIC.enabled()) {
							Option.SECONDARY_MELEE.set(false);
							Option.SECONDARY_RANGE.set(false);
						}
						if (!Option.RANGE.enabled())
							Option.ARROWS.set(false);
					}
				} else if (Option.LOAD_SETTINGS.contains(p)) {
					Option.LOAD_SETTINGS.toggle();
				} else if (Option.SAVE_SETTINGS.contains(p)) {
					Option.SAVE_SETTINGS.toggle();
				}
			} else if (showStats && sTab == 1) {
				if (RECT_MINI_UP.contains(p)) {
					levelGoal = levelGoal == 120 ? dungLevel + 1 : ++levelGoal;
				} else if (RECT_MINI_DOWN.contains(p)) {
					levelGoal = levelGoal == dungLevel + 1 ? 120 : --levelGoal;
				}
			} else if ((showSettings && page == 1) || showOptions) {
				if (RECT_TAB_1.contains(p)) {
					oTab = 1;
				} else if (RECT_TAB_2.contains(p)) {
					oTab = 2;
				} else if (!showSettings && RECT_TAB_3.contains(p)) {
					oTab = 3;
				} else if (oTab == 1) {
					if (Option.BONES.contains(p)) {
						Option.BONES.toggle();
					} else if (Option.ARROWS.contains(p)) {
						if (Option.RANGE.enabled() || Option.SECONDARY_RANGE.enabled())
							Option.ARROWS.toggle();
					} else if (Option.CRABS.contains(p)) {
						Option.CRABS.toggle();
					} else if (Option.PRAY.contains(p)) {
						Option.PRAY.toggle();
					} else if (Option.QUICK_PRAY.contains(p)) {
						Option.QUICK_PRAY.toggle();
					} else if (Option.PRAY_DOORS.contains(p)) {
						Option.PRAY_DOORS.toggle();
						if (!Option.PRAY_DOORS.enabled())
							log(RED, "Warning! Disabling prayer doors & puzzles will force the bot to 

abort dungeons");
					} else if (Option.PURE.contains(p)) {
						Option.PURE.toggle();
					} else if (Option.STYLE_SWAP.contains(p)) {
						if (!settingsFinished || Melee.SLASH.index() != -1)
							Option.STYLE_SWAP.toggle();
					} else if (Option.SUMMON.contains(p)) {
						Option.SUMMON.toggle();
						if (!Option.SUMMON.enabled())
							log(RED, "Warning! Disabling summoning doors & puzzles will force the bot to 

abort dungeons");
					} else if (Option.MAKE_FOOD.contains(p)) {
						Option.MAKE_FOOD.toggle();
					} else if (Option.EXPLORER.contains(p)) {
						Option.EXPLORER.toggle();
					} else if (Option.LOGOUT.contains(p)) {
						Option.LOGOUT.toggle();
					}
				} else if (oTab == 2) {
					if (RECT_C_UP.contains(p)) {
						if (complexity < 6) {
							complexity++;
						} else complexity = 1;
					} else if (RECT_C_DOWN.contains(p)) {
						if (complexity > 1) {
							complexity--;
						} else complexity = 6;
					} else if (RECT_RC_UP.contains(p)) {
						if (rComplexity != 6) {
							rComplexity++;
						} else rComplexity = 1;
					} else if (RECT_RC_DOWN.contains(p)) {
						if (rComplexity != 1) {
							rComplexity--;
						} else rComplexity = 6;
					} else if (RECT_F_UP.contains(p)) {
						if (maxFloor > 0) {
							rushTo = rushTo < maxFloor ? ++rushTo : 1;
						} else if (rushTo < 35) {
							rushTo++;
						} else rushTo = 1;
					} else if (RECT_F_DOWN.contains(p)) {
						if (rushTo > 1) {
							rushTo--;
						} else if (maxFloor > 0) {
							rushTo = valueOf(maxFloor);
						} else rushTo = 35;
					} else if (Option.RUSH.contains(p)) {
						Option.RUSH.toggle();
					} else if (Option.PRESTIGE.contains(p)) {
						Option.PRESTIGE.toggle();
					} else if (Option.MEDIUM.contains(p)) {
						Option.MEDIUM.toggle();
					} else if (Option.DEBUG.contains(p)) {
						Option.DEBUG.toggle();
					} else if (Option.RUSH_FOOD.contains(p)) {
						Option.RUSH_FOOD.toggle();
					} else if (Option.VIDEO.contains(p)) {
						Option.VIDEO.toggle();
					}
				} else if (!showSettings && oTab == 3) {
					if (RECT_PICKUP.contains(p)) {
						env.setUserInput(0);
						typeTo = 0;
					} else if (RECT_DESTROY.contains(p)) {
						env.setUserInput(0);
						typeTo = 1;
					} else {
						env.setUserInput(Environment.INPUT_KEYBOARD);
						typeTo = -1;
					}
				}
			}
			if (showSettings) {
				if (RECT_CONTINUE.contains(p)) {
					if (page == 0)
						oTab = 1;
					backPage = false;
					page++;
					if (page > 4) {
						showSettings = false;
						showStats = true;
						showOptions = false;
					}
				} else if (RECT_BACK.contains(p) && page > 0) {
					if (page == 1)
						oTab = 0;
					backPage = true;
					page--;
				}
			} else if (showStats) {
				if (RECT_TAB_1.contains(p)) {
					sTab = 1;
				} else if (RECT_TAB_2.contains(p)) {
					sTab = 2;
				} else if (RECT_TAB_3.contains(p)) {
					sTab = 3;
				}
			}
		}
	}

	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mouseDragged(MouseEvent e) { }

	private final int HOARDSTALKER = 11011, POLTERGEIST = 11245, UNHAPPY_GHOST = 11246, FISHING_FERRET = 11007, HUNTER_FERRET = 

11010, POND_SKATER = 12089;
	private final int CARVE_BLOCK = 17415, SNOWY_CORNER = 49334, WARPED_PORTAL = 32357, AGILITY_DOOR = 49693;
	private final int RAW_VILE_FISH = 17374, COOKED_VILE_FISH = 17375, CONSECRATED_HERB = 19659;
	private final int BROKEN_HOOK = 19663, FIXED_HOOK = 19664, MEAT_CORN = 19665, MEAT_ROPE = 19667, GRAPPLE_HOOK = 19668;
	private final int O_ARROW = 17403, O_ASHES = 17379, O_BANANA = 17381, O_RUNE = 17385, O_BONES = 17387, O_BOWSTRING = 17389, 

O_COINS = 17391, O_EEL = 17397, O_FEATHER = 17393, O_FLY_ROD = 17399, O_HAMMER = 17401, O_MUSHROOM = 17407, O_NEEDLE = 17409, O_POISON 

= 17411, O_SHIELD = 17405, O_SYMBOL = 17383, O_VIAL = 17413, O_WHISKEY = 17395;
	private final int[] SNOWY_STOPS = { 49332, 49333, SNOWY_CORNER };
	private final int[] ROW_1 = { 49390, 49392, 49394, 49396, 49414, 49416, 49418, 49420, 49438, 49440, 49442, 49444, 54336, 

54338, 54340, 54342, 33713, 33785, 33787, 33812 };
	private final int[] ROW_2 = { 49398, 49400, 49402, 49404, 49422, 49424, 49426, 49428, 49446, 49448, 49450, 49452, 54344, 

54346, 54348, 54350, 33826, 33831, 33840, 33862 };
	private final int[] ROW_3 = { 49406, 49408, 49410, 49412, 49430, 49432, 49434, 49436, 49454, 49456, 49458, 49460, 54352, 

54354, 54356, 54358, 33882, 33911, 33914, 33921, 54358 };
	private final int[] GHOSTS = { 10981, 10983, 10985, 10987, 10989, 10991, 10993, 10995, 10997, 10999 };
	private final int[] FTL_STATUES = { 10966, 10967, 10968, 12114, 12960 };
	private final int[] LEVERS = { 49381, 49382, 49383, 54333, 33675 };
	private final int[] SLIDER_BACKGROUNDS = { 54321, 54322, 54323, 33674 };
	private final int[] COLORED_FERRETS = { 12167, 12165, 12171, 12169, 12173 };
	private final int[] ALL_UNWINCHED_BRIDGES = { 39912, 39913, 39920, 39921, 39929, 39930, 39931, 39948, 39949 };
	private final int[] UNGRAPPLED_CHASMS = { 54237, 54238, 54239, 54240, 37265 };
	private final int[] GRAPPLED_CHASMS = { 54241, 54242, 54243, 54244, 54245, 54246, 54247, 54248, 37267 };
	private final int[] ICY_PRESSURE_PADS = { 49320, 49321, 49322, 49323 };
	private final int[] SMALL_DEBRIS = { 49615, 49616, 49617, 49618 };
	private final int[] HEADING_STATUES = { 10942, 10943, 10944, 10945, 10946, 10947, 10948, 10949, 10950, 10951, 10952, 10953, 

12117, 12118, 12119, 12120, 12952, 12953, 12954, 12955 };
	private final int[] SLIDING_STATUES = { 10954, 10955, 10956, 10957, 10958, 10959, 10960, 10961, 10962, 10963, 10964, 10965, 

12121, 12122, 12123, 12124, 12956, 12957, 12958, 12959 };
	private final int[] THREE_STATUES = { 11036, 11037, 11038, 11039, 11040, 11041, 11042, 11043, 11044, 12094, 12095, 12096, 

13057, 13058, 13059 };
	private final int[] TEN_UNARMED_STATUES = { 11012, 11013, 11014, 12106, 13049 };
	private final int[] LARGE_CRYSTALS = { 49507, 49508, 49509, 49510, 49511, 49512, 54261, 34866, 35070 };
	private final int[] TRASH = { 49315, 49316, 49317, 49318 };
	private final int[] POWER_LODESTONES = { 49570, 49571, 49572, 54235, 35858 };
	private final int[] RECESS_FOUNTAINS = { 54502, 54544, 54621, 56058 };
	private final int[] CLOSED_SARCOPHAGUS = { 54078, 54079, 54080, 54081, 39526 };
	private final int[] UNREPAIRED_STATUE_BRIDGE = { 39991, 40002, 40003, 40004 };
	private final int[] BARREL_PIPES = { 49688, 49690, 54287, 39968 };
	private final int[] DRY_BLOOD_FOUNTAIN = { 54110, 54111, 54112, 54113, 37202 };
	private final int[] FREMMY_CRATES = { 49522, 49523, 49524, 49528, 49529, 49530, 49534, 49535, 49536 };
	private final int[] CENTER_FLOWERS = { 35507, 35520, 35523, 35525, 35562, 35568, 35569, 35576 };
	private final int[] BOOKCASES = { 54419, 54420, 54421, 54422 };
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

	private int checkPuzzles() {
		failReason = "Undeterminable components";
		lastMessage = "";
		levelRequirement = true;
		unreachable = false;
		safeTile = null;
		if (failSafe())
			return 2;
		switch (floor) {
		case FROZEN:
			if (getObjInRoom(ICY_PRESSURE_PADS) != null) {
				status = "Puzzle room: Icy Pads";
				if (!puzzleRooms.contains(targetRoom))
					puzzleRooms.add(targetRoom);
				if (!pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(random(480000, 600000));
				return puzzleIcyPressurePads();
			}
			break;
		case ABANDONED:
			if (getNpcInRoom("Seeker sentinel") != null) {
				status = "Puzzle room: Seeker Sentinel";
				if (!puzzleRooms.contains(targetRoom)) {
					puzzleRooms.add(targetRoom);
					unBacktrackable.add(targetRoom);
				}
				if (puzzleTimer == null)
					puzzleTimer = new Timer(random(240000, 300000));
				return puzzleSeekerSentinel();
			}
			break;
		case FURNISHED:
			if (getObjInRoom(TRASH) != null) {
				status = "Puzzle room: Sleeping Guards";
				if (!puzzleRooms.contains(targetRoom))
					puzzleRooms.add(targetRoom);
				if (!pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(random(480000, 600000));
				return puzzleSleepingGuards();
			}
			break;
		case OCCULT:
			if (getObjInRoom(BOOKCASES) != null) {
				status = "Puzzle room: Bookcases";
				if (!puzzleRooms.contains(targetRoom))
					puzzleRooms.add(targetRoom);
				if (!fightMonsters() || !pickUpAll())
					return 2;
				if (puzzleTimer == null)
					puzzleTimer = new Timer(random(480000, 600000));
				return puzzleBookcases();
			}
			break;
		case WARPED:
			if (getObjInRoom(WARPED_PORTAL) != null) {
				status = "Puzzle room: Warped Portals";
				if (!puzzleRooms.contains(targetRoom))
					puzzleRooms.add(targetRoom);
				if (!pickUpAll())
					return 2;
				if (puzzleTimer == null) {
					badTiles.clear();
					puzzleTimer = new Timer(random(480000, 600000));
				}
				return puzzleWarpedPortals();
			}
		}
		if (getObjInRoom(AGILITY_DOOR) != null) {
			status = "Puzzle room: Agility Maze";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleAgilityMaze();
		}
		if (getObjInRoom(BARREL_PIPES) != null) {
			status = "Puzzle room: Barrels";
			if (!puzzleRooms.contains(targetRoom)) {
				unBacktrackable.add(targetRoom);
				puzzleRooms.add(targetRoom);
			}
			if (!fightMonsters())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleBarrels();
		}
		if (getObjInRoom(DRY_BLOOD_FOUNTAIN) != null) {
			status = "Puzzle room: Blood Fountain";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleBloodFountain();
		}

		if (getNpcInRoom(COLORED_FERRETS) != null) {
			status = "Puzzle room: Colored Ferrets";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(300000, 360000));
			return puzzleColoredFerrets();
		}
		if (getObjInRoom(RECESS_FOUNTAINS) != null) {
			status = "Puzzle room: Colored Recesses";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(420000, 540000));
			return puzzleColoredRecesses();
		}
		if (getObjInRoom(SMALL_DEBRIS) != null) {
			status = "Puzzle room: Collapsing Room";
			if (!puzzleRooms.contains(targetRoom)) {
				puzzleRooms.add(targetRoom);
				unBacktrackable.add(targetRoom);
			}
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(300000, 420000));
			return puzzleCollapsingRoom();
		}
		if (getObjInRoom(LARGE_CRYSTALS) != null) {
			status = "Puzzle room: Crystal Lights";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(420000, 480000));
			return puzzleCrystalLights();
		}
		if (getObjInRoom(DAMAGED_BRIDGES) != null) {
			status = "Puzzle room: Damaged Bridge";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!pickUpAll())
				return 2;
			return puzzleBridgeDamaged();
		}
		if (getNpcInRoom("Damaged construct", "Dormant construct") != null) {
			status = "Puzzle room: Damaged Construct";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleDamagedConstruct();
		}
		if (getNpcInRoom(FISHING_FERRET) != null) {
			status = "Puzzle room: Fishing for Ferrets";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(420000, 540000));
			return puzzleFishingFerret();
		}
		if (getObjInRoom("Green tile") != null && getObjInRoom("Yellow tile") != null) {
			status = "Puzzle room: Flip Tiles";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleFlipTiles();
		}
		if (getNpcInRoom(FTL_STATUES) != null) {
			status = "Puzzle room: Follow the Leader";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleFollowTheLeader();
		}
		if (getObjInRoom(FREMMY_CRATES) != null) {
			status = "Puzzle room: Fremennik Camp";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(180000, 240000));
			return puzzleFremennikCamp();
		}
		if (getNpcInRoom(GHOSTS) != null) {
			status = "Puzzle room: Ghosts";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleGhosts();
		}
		if (getObjInRoom(UNGRAPPLED_CHASMS) != null) {
			status = "Puzzle room: Grapple Chasm";
			if (!puzzleRooms.contains(targetRoom)) {
				puzzleRooms.add(targetRoom);
				chasmRooms.add(targetRoom);
			}
			if (!pickUpAll())
				return 2;
			return puzzleBridgeGrapple();
		}
		if (getObjInRoom(RIDDLE_DOORS) != null) {
			status = "Puzzle room: Enigmatic Hoardstalker";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleHoardStalker();
		}
		if (getNpcInRoom(HUNTER_FERRET) != null) {
			status = "Puzzle room: Hunter Ferret";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleHunterFerret();
		}
		if (getObjInRoom(POWER_LODESTONES) != null) {
			status = "Puzzle room: Lodestone Power";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleLodestonePower();
		}
		if (getObjInRoom(CLOSED_CHESTS) != null) {
			status = "Puzzle room: Maze";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleMaze();
		}
		if (getNpcInRoom("Mercenary leader") != null) {
			status = "Puzzle room: Mercenary Leader";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(600000, 720000));
			return puzzleMercenaryLeader();
		}
		if (getNpcInRoom(MONOLITHS) != null) {
			status = "Puzzle room: Monolith";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleMonolith();
		}
		if (getObjInRoom(CLOSED_SARCOPHAGUS) != null) {
			status = "Puzzle room: Poltergeist";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzlePoltergeist();
		}
		if (getNpcInRoom(POND_SKATER) != null) {
			status = "Puzzle room: Pond Skaters";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			puzzleTimer = new Timer(random(240000, 300000));
			return puzzlePondSkaters();
		}
		if (getNpcInRoom("bloodrager") != null) {
			status = "Puzzle room: Ramokee Familiars";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleRamokeeFamiliars();
		}
		if (getObjInRoom(SLIDER_BACKGROUNDS) != null) {
			status = "Puzzle room: Slider Puzzle";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleSliderPuzzle();
		}
		if (getNpcInRoom(SLIDING_STATUES) != null) {
			status = "Puzzle room: Sliding Statues";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(300000, 420000));
			return puzzleSlidingStatues();
		}
		if (getObjInRoom(UNREPAIRED_STATUE_BRIDGE) != null) {
			status = "Puzzle room: Statue Bridge";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!pickUpAll())
				return 2;
			return puzzleBridgeStatue();
		}
		if (getObjInRoom(CENTER_FLOWERS) != null) {
			status = "Puzzle room: Strange Flowers";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleStrangeFlowers();
		}
		if (getObjInRoom(ROW_1) != null) {
			status = "Puzzle room: Suspicious Grooves";
			if (!puzzleRooms.contains(targetRoom)) {
				puzzleRooms.add(targetRoom);
				unBacktrackable.add(targetRoom);
			}
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleSuspiciousGrooves();
		}
		if (getNpcInRoom(TEN_UNARMED_STATUES) != null) {
			status = "Puzzle room: 'Ten' Statues";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleTenStatues();
		}
		if (getNpcInRoom(THREE_STATUES) != null) {
			status = "Puzzle room: Three Statues";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(180000, 240000));
			return puzzleThreeStatues();
		}

		if (getObjInRoom(LEVERS) != null) {
			status = "Puzzle room: Timed Switches";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(480000, 600000));
			return puzzleTimedSwitches();
		}
		if (getObjInRoom(UNFINISHED_BRIDGES) != null) {
			status = "Puzzle room: Unfinished Bridge";
			if (!puzzleRooms.contains(targetRoom)) {
				puzzleRooms.add(targetRoom);
				chasmRooms.add(targetRoom);
			}
			if (!pickUpAll())
				return 2;
			return puzzleBridgeUnfinished();
		}
		if (getNpcInRoom(UNHAPPY_GHOST) != null) {
			status = "Puzzle room: Unhappy Ghost";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!fightMonsters() || !pickUpAll())
				return 2;
			if (puzzleTimer == null)
				puzzleTimer = new Timer(random(240000, 300000));
			return puzzleUnhappyGhost();
		}
		if (getObjInRoom(ALL_UNWINCHED_BRIDGES) != null) {
			status = "Puzzle room: Winch Bridge";
			if (!puzzleRooms.contains(targetRoom))
				puzzleRooms.add(targetRoom);
			if (!pickUpAll())
				return 2;
			return puzzleBridgeWinch();
		}
		return 0;
	}

	private int puzzleAgilityMaze() {
		if (!memberCheck())
			return -1;
		RSTile backDoor = getBackDoor();
		RSObject blades = getObjInRoom(SPINNING_BLADES);
		getBestDoor();
		if (blades == null || backDoor == null || destDoor == null)
			return -1;
		if (calc.distanceBetween(backDoor, blades.getLocation()) < 8) {
			status = "Puzzle room: Agility Maze - Course 1";
			return puzzleAgilityCourse1();
		} else if (calc.distanceBetween(targetRoom.getCentralTile(), blades.getLocation()) < 4) {
			status = "Puzzle room: Agility Maze - Course 3";
			return puzzleAgilityCourse3();
		} else {
			status = "Puzzle room: Agility Maze - Course 2";
			return puzzleAgilityCourse2();
		}
	}

	private int puzzleAgilityCourse1() {
		RSTile backDoor = getBackDoor();
		RSTile bladeSpot = getObjLocation(50947, 52041, 54907, 54908, 55981);
		if (backDoor == null || bladeSpot == null) {
			waitForResponse();
			return -1;
		}
		while (calc.distanceTo(destDoor) > 2) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			RSObject blades = getObjInRoom(SPINNING_BLADES);
			RSObject pendulum = getObjInRoom(PENDULUMS);
			nearMonster = npcs.getNearest(monster);
			getGoodItem();
			if (nearMonster != null && calc.distanceTo(nearMonster) < 3 && reachable(nearMonster.getLocation(), false)) {
				attackNpc(nearMonster);
			} else if (nearItem != null && calc.distanceTo(nearItem.getLocation()) < 3 && reachable(nearItem.getLocation

(), false)) {
				pickUpItem(nearItem);
			} else if (reachableObj(blades)) {
				if (calc.distanceTo(bladeSpot) < 2 || calc.distanceTo(blades) > 5) {
					walkToDoor(1);
				} else walkTo(bladeSpot, 0);
			} else if (reachableObj(pendulum)) {
				walkTo(pendulum.getLocation(), 0);
			} else if (reachableObj(getObjInRoom(WALL_TRAPS))) {
				walkTo(pendulum.getLocation(), 1);
			} else if (reachable(backDoor, true)) {
				doObjAction(getObjInRoom(AGILITY_GROOVES), "Step-onto");
			} else walkToDoor(1);
			waitToStop(true);
		}
		if (!fightMonsters() || !pickUpAll())
			return 2;
		while (getObjInRoom(AGILITY_DOOR) != null) {
			if (isDead()) {
				teleBack();
			} else if (failSafe()) {
				return 2;
			}
			smartSleep(doObjAction(getObjInRoom(AGILITY_DOOR), "Open"), true);
		}
		return 1;
	}

	private int puzzleAgilityCourse2() {
		final int[] SAFE_CORNERS = { 50663, 51762, 53750, 56274 };
		RSTile backDoor = getBackDoor(), bladeTile = getObjLocation(SPINNING_BLADES);
		RSTile agilDoor1 = getNearestObjTo(backDoor, AGILITY_DOOR), agilDoor2 = getNearestObjTo(destDoor, AGILITY_DOOR);
		RSTile bladeSpot = getNearestObjTo(bladeTile, SAFE_CORNERS), pendulum = getObjLocation(PENDULUMS);
		if (agilDoor1 == null || agilDoor2 == null || bladeSpot == null || pendulum == null) {
			waitForResponse();
			return -1;
		}
		while (getObjInRoom(AGILITY_DOOR) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			RSObject wallTrap = getObjInRoom(WALL_TRAPS);
			nearMonster = npcs.getNearest(monster);
			getGoodItem();
			if (reachable(agilDoor1, true) && reachable(agilDoor2, true)) {
				doObjAction(getObjInRoom(AGILITY_DOOR), "Open");
			} else if (nearMonster != null && calc.distanceTo(nearMonster) < 3 && reachable(nearMonster.getLocation(), 

false)) {
				attackNpc(nearMonster);
			} else if (nearItem != null && calc.distanceTo(nearItem.getLocation()) < 3 && reachable(nearItem.getLocation

(), false)) {
				pickUpItem(nearItem);
			} else if (reachableObj(objects.getTopAt(bladeTile))) {
				if (calc.distanceTo(pendulum) > 5 && calc.distanceTo(bladeTile) < 6 && calc.distanceTo(bladeSpot) > 1) 

{
					if (!isEdgeTile(myLoc())) {
						RSTile safety = getObjLocation(SAFE_CORNERS);
						walkTo(safety != null ? safety : myLoc(), safety != null ? 0 : 2);
					} else if (unStick()) {
						if (pendulum != null)
							walkTo(pendulum, 2);
					} else walkTo(bladeSpot, 1);
				} else if (pendulum != null && !reachable(agilDoor1, true)) {
					walkToMap(pendulum, 1);
				}
			} else if (reachableObj(wallTrap)) {
				if (walkTo(wallTrap.getLocation(), 1))
					waitToEat(true);
			} else if (reachable(backDoor, true)) {
				doObjAction(getObjInRoom(AGILITY_GROOVES), "Step-onto");
			}
			waitToStop(false);
		}
		return 1;
	}

	private int puzzleAgilityCourse3() {
		final int[] whiteIDs = { 50687, 52075, 54934, 55823 };
		final int[] blackIDs = { 52982, 51786, 54743, 56228 };
		RSObject door, pendulumObj = getObjInRoom(PENDULUMS);
		RSTile bladeSpot2 = getTileOfObjs(whiteIDs, blackIDs);
		if (pendulumObj == null || bladeSpot2 == null) {
			waitForResponse();
			return -1;
		}
		safeTile = bladeSpot2;
		RSTile backDoor = getBackDoor(), pendulum = pendulumObj.getLocation();
		while ((door = getObjInRoom(AGILITY_DOOR)) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			RSObject wallTrap = getObjInRoom(WALL_TRAPS);
			nearMonster = npcs.getNearest(monster);
			getGoodItem();
			if (reachable(destDoor, true)) {
				doObjAction(door, "Open");
			} else if (nearMonster != null && calc.distanceTo(nearMonster) < 3 && reachable(nearMonster.getLocation(), 

false)) {
				attackNpc(nearMonster);
			} else if (nearItem != null && calc.distanceTo(nearItem.getLocation()) < 3 && reachable(nearItem.getLocation

(), false)) {
				pickUpItem(nearItem);
			} else if (reachableObj(getObjInRoom(SPINNING_BLADES))) {
				RSObject safeObj = getObjInRoom(49765);
				RSTile bladeSpot1 = safeObj != null ? safeObj.getLocation() : bladeSpot2;
				if (calc.distanceBetween(myLoc(), pendulum) < 4 || calc.distanceTo(bladeSpot2) < 3) {
					walkTo(pendulum, 1);
				} else walkTo(calc.distanceTo(bladeSpot1) < 2 ? bladeSpot2 : bladeSpot1, 0);
			} else if (reachableObj(wallTrap)) {
				walkTo(wallTrap.getLocation(), 1);
			} else if (reachable(backDoor, true)) {
				doObjAction(getObjInRoom(AGILITY_GROOVES), "Step-onto");
			} else walkToDoor(1);
			waitToStop(true);
		}
		return 1;
	}

	private int puzzleBarrels() {
		if (!memberCheck())
			return -1;
		final int[] WATER_PIPES = { 49689, 49692, 54288, 39969 };
		final int BARREL_BITS = 17422, FIXED_BARREL = 11073, FINISHED_BARREL = 11074;
		RSObject destPad = getObjInRoom(ACTIVE_PADS);
		if (destPad == null)
			return -1;
		RSTile destTile = destPad.getLocation(), cTile = targetRoom.getCentralTile(), sTile = getBackDoor();
		boolean NS = Math.abs(cTile.getY() - sTile.getY()) > 4 ? true : false;
		RSTile heading1 = null, heading2 = null;
		RSObject heading2obj = getObjInRoom(50962, 50965, 52059, 54986, 55885);
		for (RSObject o : getObjsInRoom(50849, 51943, 54765, 56264)) {
			RSTile oTile = o.getLocation();
			if (getNpcAt(oTile) != null && calc.distanceBetween(oTile, sTile) < 7) {
				heading1 = oTile;
				break;
			}
		}
		if (heading1 == null || heading2obj == null)
			return -1;
		boolean push1 = true, push2 = true;
		RSNPC barrel1 = null, barrel2 = null, barrel3 = null, barrel4 = null, barrel5 = null;
		RSTile barrel1start = null, barrel2start = null, barrel3start = null, barrel4start = null, barrel5start = null;
		if (heading1 != null) {
			barrel2 = getNpcAt(heading1);
			barrel2start = heading1;
			int hX = heading1.getX(), hY = heading1.getY();
			RSTile[] barrel1test = { new RSTile(hX - 2, hY), new RSTile(hX + 2, hY), new RSTile(hX, hY - 2), new RSTile

(hX, hY + 2) };
			RSTile[] barrel3test = { new RSTile(hX - 1, hY), new RSTile(hX + 1, hY), new RSTile(hX, hY - 1), new RSTile

(hX, hY + 1) };
			for (RSTile test : barrel1test) {
				RSNPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel1 = barrel;
					barrel1start = test;
					break;
				}
			}
			for (RSTile test : barrel3test) {
				RSNPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel3 = barrel;
					barrel3start = test;
					break;
				}
			}
		}
		if (heading2obj != null) {
			heading2 = heading2obj.getLocation();
			int hX = heading2.getX(), hY = heading2.getY();
			RSTile[] barrel5test = { new RSTile(hX - 2, hY), new RSTile(hX + 2, hY), new RSTile(hX, hY - 2), new RSTile

(hX, hY + 2) };
			for (RSTile test : getAdjacentTo(heading2)) {
				RSNPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel4 = barrel;
					barrel4start = test;
					break;
				}
			}
			for (RSTile test : barrel5test) {
				RSNPC barrel = getNpcAt(test);
				if (barrel != null) {
					barrel5 = barrel;
					barrel5start = test;
					break;
				}
			}
		}
		barrel2 = getNpcAt(heading1);
		barrel2start = heading1;
		int hX = heading1.getX(), hY = heading1.getY();
		RSTile[] barrel1test = { new RSTile(hX - 2, hY), new RSTile(hX + 2, hY), new RSTile(hX, hY - 2), new RSTile(hX, hY + 

2) };
		for (RSTile test : barrel1test) {
			RSNPC barrel = getNpcAt(test);
			if (barrel != null) {
				barrel1 = barrel;
				barrel1start = barrel.getLocation();
				break;
			}
		}
		for (RSTile test : getAdjacentTo(heading1)) {
			RSNPC barrel = getNpcAt(test);
			if (barrel != null) {
				barrel3 = barrel;
				barrel3start = barrel.getLocation();
				break;
			}
		}
		RSObject pipe = getObjInRoom(WATER_PIPES);
		if (pipe != null)
			turnTo(pipe.getLocation());
		while (getNpcInRoom(FINISHED_BARREL) == null) {
			if (!requirements()) {
				unBacktrackable.add(targetRoom);
				return -1;
			}
			if (failSafe())
				return 2;
			if (secondaryStatus.contains("reengage")) {
				failReason = "Timed out";
				unBacktrackable.add(targetRoom);
				return -1;
			}
			boolean toWait = false;
			RSNPC fixedBarrel = getNpcInRoom(FIXED_BARREL);
			if (fixedBarrel != null && interfaces.getComponent(945, 17).getRelativeX() == 200) {
				safeTile = destTile;
				if (fixedBarrel != null) {
					RSTile bT = fixedBarrel.getLocation(), pT = destTile;
					int bX = bT.getX(), bY = bT.getY(), pX = pT.getX(), pY = pT.getY();
					boolean atPush = false;
					if (NS) {
						if (bX != pX)
							atPush = bX > pX ? walkAround(bT, 1, 0, false) : walkAround(bT, -1, 0, false);
					} else if (bY != pY) {
						atPush = bY > pY ? walkAround(bT, 0, 1, false) : walkAround(bT, 0, -1, false);
					}
					toWait = atPush && doNpcAction(fixedBarrel, "Push");
				}
			} else {
				RSNPC brokenBarrel = getNpcInRoom(11075);
				pipe = getObjInRoom(WATER_PIPES);
				getGoodItem();
				if (nearItem != null && calc.distanceTo(nearItem.getLocation()) < 4 && reachable(nearItem.getLocation

(), false)) {
					toWait = pickUpItem(nearItem);
				} else if (fixedBarrel != null && pipe != null) {
					RSTile bT = fixedBarrel.getLocation();
					RSTile pT = pipe.getLocation();
					int bX = bT.getX(), bY = bT.getY(), pX = pT.getX(), pY = pT.getY();
					if (NS) {
						if (bX != pX) {
							boolean atPush = bX > pX ? walkAround(bT, 1, 0, false) : walkAround(bT, -1, 0, 

false);
							if (atPush && doNpcAction(fixedBarrel, "Push")) {
								waitToStop(true);
								bT = fixedBarrel.getLocation();
								pT = pipe.getLocation();
								bX = bT.getX();
								bY = bT.getY();
								pX = pT.getX();
								pY = pT.getY();
								if (bX == pX)
									walkTo(new RSTile(bX, bY + bY - pY), 0);
							}
						}
					} else if (bY != pY) {
						boolean atPush = bY > pY ? walkAround(bT, 0, 1, false) : walkAround(bT, 0, -1, false);
						if (atPush && doNpcAction(fixedBarrel, "Push")) {
							waitToStop(true);
							bT = fixedBarrel.getLocation();
							pT = pipe.getLocation();
							bX = bT.getX();
							bY = bT.getY();
							pX = pT.getX();
							pY = pT.getY();
							if (bY == pY)
								walkTo(new RSTile(bX + bX - pX, bY), 0);
						}
					}
				} else if (push1) {
					if (getNpcAt(barrel1start) != null) {
						toWait = doNpcAction(barrel1, "Push");
					} else if (getNpcAt(barrel2start) != null) {
						toWait = doNpcAction(barrel2, "Push");
					} else if (getNpcAt(barrel3start) != null) {
						toWait = doNpcAction(barrel3, "Push");
					} else push1 = false;
				} else if (push2) {
					if (getNpcAt(barrel4start) != null) {
						toWait = doNpcAction(barrel4, "Push");
					} else if (getNpcAt(barrel5start) != null) {
						toWait = doNpcAction(barrel5, "Push");
					} else push2 = false;
				} else if (brokenBarrel != null) {
					if (inventory.contains(BARREL_BITS)) {
						if (calc.distanceTo(heading2) > 2 && calc.distanceTo(brokenBarrel) > 1) {
							walkToMap(heading2, 1);
						} else doNpcAction(brokenBarrel, "Fix");
						toWait = true;
					} else toWait = pickUpItem(getItemInRoom(targetRoom, BARREL_BITS));
				}
			}
			smartSleep(toWait, true);
		}
		walkToDoor(1);
		while (calc.distanceTo(nearDoor) > 3) {
			if (failSafe())
				return 2;
			walkTo(calc.distanceTo(heading2) > 2 ? heading2 : nearDoor, 2);
			if (!isMoving())
				walkToDoor(3);
			waitToEat(false);
		}
		return 1;
	}

	private int puzzleBloodFountain() {
		if (!memberCheck())
			return -1;
		final int[] RUBBLE = { 54130, 54131, 54132, 54133, 54134, 541325, 54138, 54139, 54140, 54141, 54142, 37220, 37232 };
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

	private int puzzleBookcases() {
		failReason = "Unsolvable puzzle";
		skipDoorFound = true;
		return -1;
		/*
		waitForResponse();
		final int BLUE_BOOK = 139, RED_BOOK = 156, TAN_BOOK = 159, GREEN_BOOK = 162;
		final int BLUE_SHELF = 54423, RED_SHELF = 54424, TAN_SHELF = 54426, GREEN_SHELF = 54425;
		final int BLUE_CASE = 54422, RED_CASE = 54419, TAN_CASE = 54421, GREEN_CASE = 54420;
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
		}
		return 1;
		 */
	}

	private int puzzleBridgeDamaged() {
		if (!memberCheck())
			return -1;
		final int ROCK = 19660, KEYSTONE = 19661;
		nearMonster = npcs.getNearest(monster);
		if (nearMonster != null && (combatStyle != Style.MELEE || reachable(nearMonster.getLocation(), false))) {
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		}
		if (puzzleTimer == null)
			puzzleTimer = new Timer(random(240000, 300000));
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		while (getObjInRoom(54190, 54191, 54192, 54193, 35249) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			if (inventory.contains(KEYSTONE)) {
				toWait = doObjAction(getObjInRoom(DAMAGED_BRIDGES), "Repair");
			} else if (!inventory.contains(CHISEL)) {
				toWait = pickUpItem(getItemInRoom(targetRoom, CHISEL));
			} else {
				if (inventory.contains(ROCK)) {
					doItemAction(inventory.getItem(ROCK), "Carve");
				} else if (!inventory.containsOneOf(ROCK, KEYSTONE)) {
					toWait = doObjAction(getObjInRoom(54182, 54183, 54184, 54185, 35247), "Mine");
				}
				waitToAnimate();
			}
			smartSleep(toWait, true);
		}
		if (Option.DEBUG.enabled())
			getRoomBlocks();
		return 1;
	}

	private int puzzleBridgeGrapple() {
		if (!memberCheck())
			return -1;
		nearMonster = npcs.getNearest(monster);
		if (nearMonster != null && (combatStyle !=Style.MELEE || reachable(nearMonster.getLocation(), false))) {
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		}
		final String[] ACTIONS = { "Spin", "Smith", "Plunder", "Retrieve" };
		if (puzzleTimer == null)
			puzzleTimer = new Timer(random(240000, 300000));
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		while (getObjInRoom(GRAPPLED_CHASMS) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			getGoodItem();
			if (nearItem != null && reachable(nearItem.getLocation(), true)) {
				smartSleep(pickUpItem(nearItem), false);
			} else if (inventory.contains(GRAPPLE_HOOK)) {
				if (smartSleep(doObjAction(getObjInRoom(UNGRAPPLED_CHASMS), "Grapple"), false))
					waitToStop(true);
			} else if (inventory.containsAll(MEAT_ROPE, FIXED_HOOK)) {
				if (inventory.useItem(MEAT_ROPE, FIXED_HOOK))
					sleep(300, 600);
			} else {
				boolean iCorn = inventory.contains(MEAT_CORN), iRope = inventory.contains(MEAT_ROPE);
				boolean iBroke = inventory.contains(BROKEN_HOOK), iHook = inventory.contains(FIXED_HOOK);
				RSObject wheel = iCorn && !iRope ? getObjInRoom(SPINNING_WHEELS) : null;
				RSObject anvil = iBroke && !iHook ? getObjInRoom(BRIDGE_ANVILS) : null;
				RSObject corn = !iCorn && !iRope ? getObjInRoom(54255) : null;
				RSObject hooks = !iBroke && !iHook ? getObjInRoom(54251) : null;
				smartAction(ACTIONS, wheel, anvil, corn, hooks);
			}
		}
		getBestDoor();
		while (!inventory.contains(GGS)) {
			if (failSafe())
				return 2;
			getItemInRoom(targetRoom, GGS);
			if (nearItem == null || !reachable(nearItem.getLocation(), true))
				break;
			if (pickUpItem(nearItem))
				waitToStop(false);
			sleep(300, 500);
		}
		return crossTheChasm(destDoor);
	}

	private int puzzleBridgeStatue() {
		if (!memberCheck())
			return -1;
		final int[] REPAIRED_STATUES = { 13070, 13071, 13072, 13073, 13074, 13075, 13076, 13077, 13078, 13079, 13080, 13081, 

13082, 13083, 13084, 13085, 13086, 13087, 13088 };
		final int ROCK = 19877;
		nearMonster = npcs.getNearest(monster);
		if (nearMonster != null && (combatStyle !=Style.MELEE || reachable(nearMonster.getLocation(), false))) {
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		}
		if (puzzleTimer == null)
			puzzleTimer = new Timer(random(240000, 300000));
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		while (getObjInRoom(40010, 40011, 54614, 40012) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			RSNPC repairedStatue = getNpcInRoom(REPAIRED_STATUES);
			if (repairedStatue != null) {
				if (lastMessage.contains("cannot push")) {
					RSObject otherStatue = getObjInRoom("Statue");
					if (otherStatue == null) {
						toWait = walkTo(myLoc(), 3);
					} else walkBlockedTile(otherStatue.getLocation(), 1);
					lastMessage = "";
				} else toWait = doNpcAction(repairedStatue, "Push");
			} else if (inventory.contains(ROCK)) {
				if (doObjAction(getObjInRoom(40157, 40158, 54617, 54618, 40159), "Repair")) {
					sleep(400, 800);
					if (!requirements())
						return -1;
					toWait = true;
				}
			} else toWait = doObjAction(getObjInRoom(39980, 39988, 54490, 54496, 54613, 39989), "Mine");
			smartSleep(toWait, true);
			waitForDamage();
		}
		if (Option.DEBUG.enabled())
			getRoomBlocks();
		return 1;
	}

	private int puzzleBridgeUnfinished() {
		if (!memberCheck())
			return -1;
		RSTile backDoor = getBackDoor();
		if (puzzleTimer == null)
			puzzleTimer = new Timer(random(240000, 300000));
		while (getObjInRoom(54013, 54014, 54015, 54016, 39874) == null) {
			if (failSafe())
				return 2;
			if (lastMessage.contains("Construction")) {
				crossTheChasm(destDoor);
				return 1;
			}
			if (!requirements("Construction")) {
				crossTheChasm(getBackDoor());
				return -1;
			}
			boolean toWait = false;
			if ((nearMonster = npcs.getNearest(monster)) != null && reachable(nearMonster.getLocation(), false)) {
				if (!fightMonsters() || !pickUpAll())
					return 2;
				puzzleTimer = new Timer(random(240000, 300000));
			}
			if (reachable(backDoor, true)) {
				toWait = doObjAction(getObjInRoom(UNFINISHED_BRIDGES), "Jump") && waitToStart(true);
			} else if (inventory.getCount(19651) < 5) {
				toWait = doObjAction(getObjInRoom(54021, 54022, 54023, 54024), "Raid");
			} else toWait = doObjAction(getObjInRoom(UNFINISHED_BRIDGES), "Repair");
			smartSleep(toWait, true);
		}
		if (Option.DEBUG.enabled())
			getRoomBlocks();
		return 1;
	}

	private int puzzleBridgeWinch() {
		final int[] UNGRAPPLED_WINCH_BRIDGES = { 39912, 39913, 39920, 39929, 39931, 39948 };
		final String[] ACTIONS = { "Spin", "Smith", "Take-from", "Take-from" };
		nearMonster = npcs.getNearest(monster);
		if (nearMonster != null && (combatStyle !=Style.MELEE || reachable(nearMonster.getLocation(), false))) {
			if (!fightMonsters() || (combatStyle == Style.MELEE && !pickUpAll()))
				return 2;
		}
		setRetaliate(getGoodNpc(DISTANCE_MONSTER) == null);
		if (puzzleTimer == null)
			puzzleTimer = new Timer(random(240000, 300000));
		while (getObjInRoom(39915, 39924, 39935, 54643, 39950) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			RSObject ungrappled = getObjInRoom(UNGRAPPLED_WINCH_BRIDGES);
			if (ungrappled == null || lastMessage.startsWith("You grapple")) {
				if (lastMessage.contains("fully extended"))
					break;
				if (doObjAction(getObjInRoom(39917, 39928, 39947, 54647, 39964), "Operate")) {
					waitToObject(false);
					waitToStop(true);
					if (lastMessage.contains("fully extended"))
						break;
					waitToStop(true);
				}
			} else if (inventory.contains(GRAPPLE_HOOK)) {
				if (smartSleep(useItem(GRAPPLE_HOOK, ungrappled), true))
					waitToStop(true);
			} else if (inventory.containsAll(MEAT_ROPE, FIXED_HOOK)) {
				if (inventory.useItem(MEAT_ROPE, FIXED_HOOK))
					sleep(300, 600);
			} else {
				boolean iCorn = inventory.contains(MEAT_CORN), iRope = inventory.contains(MEAT_ROPE);
				boolean iBroke = inventory.contains(BROKEN_HOOK), iHook = inventory.contains(FIXED_HOOK);
				RSObject wheel = iCorn && !iRope ? getObjInRoom(SPINNING_WHEELS) : null;
				RSObject anvil = iBroke && !iHook ? getObjInRoom(BRIDGE_ANVILS) : null;
				RSObject corn = !iCorn && !iRope ? getObjInRoom(54566, 54568, 54570, 54646, 39959) : null;
				RSObject hooks = !iBroke && !iHook ? getObjInRoom(54565, 54567, 54569, 54645, 39956) : null;
				smartAction(ACTIONS, wheel, anvil, corn, hooks);
			}
			waitForDamage();
		}
		if (Option.DEBUG.enabled())
			getRoomBlocks();
		return 1;
	}

	private int puzzleColoredFerrets() {
		final int[] BAD_PLATES = { 54365, 54367, 54369, 54371, 54373, 54375, 54377, 54379, 54381, 54383, 54385, 54387, 54389, 

54391, 54393, 54395, 54397, 54399, 54401, 54403, 33611, 33618, 33623, 33629, 33632 };
		final int[] RED_TILE = { 54364, 54366, 54368, 54370, 33609 }, BLUE_TILE = { 54372, 54374, 54376, 54378, 33617 }, 

GREEN_TILE = { 54380, 54382, 54384, 54386, 33619 }, YELLOW_TILE = { 54388, 54390, 54392, 54394, 33624 }, ORANGE_TILE = { 54396, 54398, 

54400, 54402, 33631 };
		final int[][] COLORED_PLATES = { BLUE_TILE, RED_TILE, YELLOW_TILE, GREEN_TILE, ORANGE_TILE };
		while (getNpcInRoom(COLORED_FERRETS) != null) {
			for (int I = 0; I < COLORED_FERRETS.length; I++) {
				int failCount = 0;
				RSObject tile = getObjInRoom(COLORED_PLATES[I]);
				if (tile == null)
					continue;
				RSNPC ferret;
				RSTile fTile;
				safeTile = tile.getLocation();
				while ((ferret = getNpcInRoom(COLORED_FERRETS[I])) != null && !(fTile = ferret.getLocation()).equals

(safeTile)) {
					if (failSafe())
						return 2;
					RSTile dTile = null, wTile = null;
					if (!isEdgeTile(fTile)) {
						boolean far = calc.distanceBetween(fTile, safeTile) > 3;
						int fX = fTile.getX(), fY = fTile.getY();
						Point m = alignment(fTile, safeTile);
						if (m.x != 0 && m.y != 0) {
							far = !far || Math.abs(myLoc().getX() - fX) < 2 || Math.abs(myLoc().getY() - 

fY) < 2;
							RSTile check1 = new RSTile(fX - m.x, fY - m.y);
							RSTile check2 = new RSTile(fX - 2 * m.x, fY - 2 * m.y);
							RSTile backup = far ? check2 : check1;
							dTile = far ? check1 : check2;
							if (!isGoodTile(dTile, BAD_PLATES) && isGoodTile(backup, BAD_PLATES))
								dTile = backup;
						} else if (m.x != 0 || m.y != 0) {
							if (isGoodTile(new RSTile(fX + m.x, fY + m.y), BAD_PLATES)) { // Scare tile 

check
								RSTile check1 = new RSTile(fX - m.x, fY - m.y);
								RSTile check2 = new RSTile(fX - 2 * m.x, fY - 2 * m.y);
								RSTile backup = far ? check2 : check1;
								wTile = far ? check1 : check2;
								if (!isGoodTile(wTile, BAD_PLATES))
									wTile = backup;
								if (!isGoodTile(wTile, BAD_PLATES))
									wTile = null;
							}
						}
					}
					RSTile me = myLoc();
					if (dTile != null) {
						if (!me.equals(dTile)) {
							if (walkTo(dTile, 0))
								waitToStop(false);
							failCount = 0;
						} else {
							failCount++;
							sleep(100, 200);
						}
					} else if (wTile != null) {
						RSTile first = getNearTriple(wTile, safeTile);
						double wDist = calc.distanceBetween(me, wTile), fDist = calc.distanceBetween(me, 

first);
						if (wDist < 3 || fDist < calc.distanceBetween(me, fTile))
							first = null;
						if (first == null || me.equals(first) || walkTo(first, 0)) {
							waitToStart(false);
							while (isMoving() && calc.distanceTo(first) > 1) {
								sleep(100, 200);
							}
							if (!myLoc().equals(wTile) && walkTo(wTile, 0)) {
								waitToStop(false);
								failCount = 0;
							} else {
								failCount++;
								sleep(100, 200);
							}
						}
					} else if (isMoving() || ferret.isMoving()) {
						sleep(50, 100);
						failCount = 0;
					} else {
						failCount++;
						sleep(100, 200);
					}
					RSTile badTile = ferret.getLocation();
					if (failCount > 5 && (isMoving() || ferret.isMoving()))
						failCount = 0;
					if (failCount > 30) {
						while (failCount < 35 && ferret != null && !ferret.getLocation().equals(safeTile) && 

(ferret.isMoving() || ferret.getLocation().equals(badTile) || !myLoc().equals(badTile))) {
							if (failCheck())
								break;
							if (!isMoving())
								doNpcAction(ferret, "Scare");
							if (player().getMessage() != null && walkTo(badTile, 0))
								sleep(600, 1000);
							sleep(100, 800);
							failCount++;
						}
						failCount = 0;
					}
				}
			}
		}
		return 1;
	}

	private int puzzleColoredRecesses() {
		RSObject fountainObj = getObjInRoom(RECESS_FOUNTAINS);
		if (fountainObj == null || !memberCheck() || !slayerCheck())
			return -1;
		final int[] COLOR_VIALS = { 19869, 19871, 19873, 19875 };
		final int[] BLUE_TILES = { 54504, 54546, 54623, 56060 }, GREEN_TILES = { 54506, 54548, 54625, 56062 }, YELLOW_TILES = 

{ 54508, 54550, 54627, 56064 }, VIOLET_TILES = { 54510, 54552, 54629, 56066 };
		final int[][] COLOR_TILES = { BLUE_TILES, GREEN_TILES, YELLOW_TILES, VIOLET_TILES };
		final String[] COLOR_NAMES = { "Blue", "Green", "Yellow", "Violet" };
		RSArea fountain = fountainObj.getArea();
		boolean vialsPicked = false;
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			eatFood(foods, 40, 50);
			boolean blocksAligned = true;
			o:for (int[] color : COLOR_TILES) {
				for (RSObject tObj : getObjsInRoom(color)) {
					if (getNpcAt(tObj.getLocation()) == null) {
						blocksAligned = false;
						break o;
					}
				}
			}
			if (blocksAligned) {
				if (vialsPicked && !invContains(COLOR_VIALS)) {
					RSGroundItem vial = getItemInRoom(targetRoom, COLOR_VIALS);
					if (vial != null) {
						smartSleep(pickUpItem(vial), false);
					} else vialsPicked = false;
				}
				if (!vialsPicked) {
					RSObject shelves = getObjInRoom(35241, 35242, 35243, 35245, 35246);
					if (inventory.containsAll(COLOR_VIALS)) {
						vialsPicked = true;
					} else if (shelves != null) {
						String color = "";
						safeTile = shelves.getLocation();
						for (String test : COLOR_NAMES) {
							if (getItemID(test + " vial") == -1) {
								color = test;
								break;
							}
						}
						if (interfaces.get(238).isValid()) {
							clickDialogueOption(interfaces.get(238), color);
						} else if (calc.distanceTo(safeTile) > 3) {
							walkBlockedTile(safeTile, 2);
						} else if (doObjAction(shelves, "Take-bottle")) {
							waitToStop(false);
						}
					}
				} else {
					for (int I = 0; I < COLOR_TILES.length; I++) {
						if (failSafe())
							return 2;
						RSObject tObj = getObjInRoom(COLOR_TILES[I]);
						if (tObj == null)
							continue;
						safeTile = tObj.getLocation();
						RSNPC block = getNpcAt(safeTile);
						if (block != null && block.getName().equals("Block") && inventory.contains

(COLOR_VIALS[I])) {
							RSTile bTile = block.getLocation();
							walkBlockedTile(bTile, 2);
							if (useItem(COLOR_VIALS[I], block)) {
								waitToStart(false);
								while (isMoving() && inventory.contains(COLOR_VIALS[I]) && 

calc.distanceBetween(myLoc(), bTile) > 1) {
									sleep(100, 200);
								}
							}
						}
					}
					sleep(400, 600);
				}
			} else {
				for (int[] color : COLOR_TILES) {
					RSObject tObj = getObjInRoom(color);
					if (tObj == null)
						continue;
					RSNPC push;
					safeTile = tObj.getLocation();
					while ((push = getNearestNpcTo(safeTile, "block")) != null && getNpcAt(safeTile) == null) {
						if (failSafe())
							return 2;
						if (!requirements())
							return -1;
						boolean toWait = false, xPush = false, yPush = false;
						RSTile pT = push.getLocation(), walkX = null, walkY = null;
						int pX = pT.getX(), pY = pT.getY();
						Point m = alignment(pT, safeTile);
						if (m.x != 0) {
							RSTile checkX = new RSTile(pX + m.x * 2, pY);
							walkX = new RSTile(pX + m.x, pY);
							xPush = !fountain.contains(walkX, checkX) && getNpcAt(walkX) == null && 

getNpcAt(checkX) == null;
						}
						if (m.y != 0) {
							RSTile checkY = new RSTile(pX, pY + m.y * 2);
							walkY = new RSTile(pX, pY + m.y);
							yPush = !fountain.contains(walkY, checkY) && getNpcAt(walkY) == null && 

getNpcAt(checkY) == null;
						}
						boolean yReady = xPush && yPush && calc.distanceBetween(myLoc(), walkY) < 

calc.distanceBetween(myLoc(), walkX);
						if (xPush && !yReady) {
							toWait = walkBlockedTile(walkX, 0) && clickNpc(push, "Pull");
						} else if (yPush) {
							toWait = walkBlockedTile(walkY, 0) && clickNpc(push, "Pull");
						} else break;
						smartSleep(toWait, true);
					}
				}
			}
		}
		if (failSafe())
			return 2;
		getBestDoor();
		walkBlockedTile(destDoor, 2);
		return 1;
	}

	private int puzzleCollapsingRoom() {
		final RSTile backDoor = entryDoor != null ? entryDoor : getBackDoor(), center = targetRoom.getCentralTile();
		getBestDoor();
		if (backDoor == null || destDoor == null)
			return -1;
		boolean strutting = memberWorld;
		RSTile target = center;
		safeTile = center;
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
			RSObject strut = strutting ? getReachableObj(49609, 49610, 49611, 54061, 37320) : null;
			getKey();
			if (strut != null) {
				if (doObjAction(strut, "Repair") && waitToStart(true))
					waitToStop(toWait = true);
			} else if (nearItem != null && reachable(nearItem.getLocation(), false)) {
				toWait = pickUpItem(nearItem);
			} else if (target.equals(center) && reachable(center, false)) {
				if (!isMoving())
					walkTo(center, 0);
				if (calc.distanceTo(center) < 2) {
					target = nearDoor;
					safeTile = nearDoor;
				}
			} else {
				RSObject largeDebris = getNearObjTo(target, 49619, 49620, 49621, 49622, 49623, 49624, 38857, 39478);
				RSObject smallDebris = getNearObjTo(target, SMALL_DEBRIS);
				toWait = doObjAction(largeDebris != null ? largeDebris : smallDebris, "Destroy");
			}
			smartSleep(toWait, true);
		}
		puzzleRepeat = true;
		return 1;
	}

	private int puzzleCrystalLights() {
		final int[] LIGHTS = { 49468, 49469, 49470, 49477, 49478, 49479, 49486, 49487, 49488, 54262, 54265, 54268, 34319, 

34848, 34856 };
		final int[] INDICATORS = { 49495, 49496, 49497, 54271, 34862 };
		final int[] FINISHED_CRYSTALS = { 49513, 49514, 49515, 54277, 35231 };
		ArrayList<RSTile> pads = new ArrayList<RSTile>();
		RSObject cObj = getObjInRoom("Large crystal"), indicator = null;
		if (cObj == null)
			return -1;
		RSTile cT = cObj.getLocation(), startPad = null;
		for (RSObject obj : getObjsInRoom(ACTIVE_PADS)) {
			pads.add(obj.getLocation());
		}
		RSObject unCharged = getObjInRoom("Inactive lodestone");
		while (startPad == null || (unCharged = getObjInRoom("Inactive lodestone")) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (doObjAction(unCharged, "Power-up"))
				waitToStop(true);
			indicator = getObjInRoom(INDICATORS);
			if (indicator != null) {
				for (RSTile pad : pads) {
					if (calc.distanceBetween(pad, indicator.getLocation()) < 3) {
						startPad = pad;
						break;
					}
				}
			}
		}
		pads.remove(startPad);
		while (getObjInRoom(FINISHED_CRYSTALS) == null) {
			o:for (RSTile pad : pads) {
				int lightID = 0;
				safeTile = pad;
				while (lightID == 0) {
					if (failSafe())
						return 2;
					if (getObjInRoom(FINISHED_CRYSTALS) != null)
						return 1;
					for (RSObject obj : getObjsInRoom(LIGHTS)) {
						if (obj != null && calc.distanceBetween(safeTile, obj.getLocation()) < 3) {
							lightID = obj.getID();
							if (crystalDist(cT, getObjInRoom(INDICATORS)) == crystalDist(cT, obj))
								continue o;
						}
					}
					sleep(100, 200);
				}
				walkAdjacentTo(safeTile, 1);
				while (safeTile != null && getObjInRoom(FINISHED_CRYSTALS) == null) {
					if (failSafe())
						return 2;
					while (getObjInRoom(lightID) != null || !myLoc().equals(safeTile)) {
						if (failSafe())
							return 2;
						if (getObjInRoom(FINISHED_CRYSTALS) != null)
							return 1;
						if (myLoc().equals(safeTile)) {
							if (getObjInRoom(lightID) == null)
								break;
							walkAdjacentTo(safeTile, 1);
						} else if (crystalDist(cT, getObjInRoom(INDICATORS)) == crystalDist(cT, getObjInRoom

(lightID))) {
							break;
						}
						if (crystalDist(cT, getObjInRoom(lightID)) == 3) {
							walkTo(safeTile, 0);
							waitToStop(false);
						}
						sleep(100, 200);
					}
					while (myLoc().equals(safeTile)) {
						if (getObjInRoom(FINISHED_CRYSTALS) != null)
							return 1;
						if (crystalDist(cT, getObjInRoom(INDICATORS)) == 3) {
							walkToMap(cT, 1);
							waitToStart(false);
						}
						sleep(30, 50);
					}
					for (int i = 0; i < 10; i++) {
						if (crystalDist(cT, getObjInRoom(INDICATORS)) == crystalDist(cT, getObjInRoom

(lightID))) {
							safeTile = null;
							break;
						}
						if (getObjInRoom(FINISHED_CRYSTALS) != null)
							return 1;
						sleep(100, 150);
					}
				}
			}
		}
		return 1;
	}

	private int puzzleDamagedConstruct() {
		final int CARVE_BLOCK = 17364;
		RSNPC construct;
		RSObject crate = getObjInRoom(49543, 49544, 49545, 53995);
		// Damaged: 11002

		String part = "";
		while ((construct = getNpcInRoom("construct")) == null || !construct.getName().startsWith("Magic")) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			int unchargedID = getItemID("Spare construct " + part + " (u)");
			if (construct != null && construct.getName().startsWith("Dormant")) {
				toWait = doNpcAction(construct, "Charge");
			} else if (part.isEmpty()) {
				if (!lastMessage.contains("missing") && doNpcAction(construct, "Examine"))
					waitToStop(true);
				if (lastMessage.contains("arm")) {
					part = "arm";
				} else if (lastMessage.contains("leg")) {
					part = "leg";
				} else if (lastMessage.contains("head")) {
					part = "head";
				}
			} else if (unchargedID > 0) {
				doItemAction(inventory.getItem(unchargedID), "Imbue");
			} else if (getItemID("Spare construct " + part) != -1) {
				toWait = doNpcAction(construct, "Repair");
			} else if (interfaces.get(451).isValid()) {
				clickDialogueOption(interfaces.get(451), part);
			} else if (!inventory.contains(CHISEL)) {
				toWait = pickUpItem(getItemInRoom(targetRoom, CHISEL));
			} else if (inventory.contains(CARVE_BLOCK)) {
				toWait = doItemAction(inventory.getItem(CARVE_BLOCK), "Carve");
			} else toWait = doObjAction(crate, "Take");
			smartSleep(toWait, true);
		}
		getBestDoor();
		for (int i = 0; i < 40; i++) {
			if (failSafe())
				return 2;
			if ((construct = getNpcInRoom("Magic construct")) == null || nearDoor == null) {
				getBestDoor();
				if (i > 20)
					break;
				topUp(true);
			} else {
				RSTile cTile = construct.getLocation();
				if (cTile == null || calc.distanceBetween(cTile, nearDoor) < 5)
					break;
				if (!isMoving() && calc.distanceTo(cTile) > random(5, 10)) {
					walkTo(cTile, 2);
				} else topUp(true);
			}
			sleep(600, 800);
		}
		return 1;
	}

	private int puzzleIcyPressurePads() {
		waitForResponse();
		ArrayList<RSTile> pads = new ArrayList<RSTile>();
		int padsRemaining = 0, safeCount = 0;
		for (RSObject corner : getObjsInRoom(SNOWY_CORNER)) {
			roomFlags.remove(corner.getLocation());
		}
		puzzlePoints.clear();
		for (RSObject pad : getObjsInRoom(ICY_PRESSURE_PADS)) {
			pads.add(pad.getLocation());
			stopNodes(pad.getLocation());
		}
		RSTile previous = null;
		while (padsRemaining < 4) {
			if (failSafe())
				return 2;
			RSTile me = myLoc();
			padsRemaining = 4 - pads.size();
			status = "Puzzle room: Icy Pads (" + padsRemaining + " of 4)";
			int mX = me.getX(), mY = me.getY();
			for (RSTile check : pads) {
				if (straightLine(me, check)) {
					safeTile = check;
					break;
				}
			}
			if (safeTile == null && safeCount < 3) {
				for (RSTile check : puzzlePoints) {
					if (straightLine(me, check)) {
						Point m = alignment(me, check);
						if (!isGoodTile(stepAlong(m, check))) {
							safeTile = check;
							break;
						}
					}
				}
			}
			if (safeTile != null && safeCount < 3) {
				if (walkTo(calc.tileOnScreen(safeTile) ? safeTile : stepAlong(alignment(myLoc(), safeTile), myLoc()), 

0))
					waitToEat(false);
				if (myLoc().equals(safeTile) && objects.getTopAt(safeTile) != null) {
					puzzlePoints.clear();
					pads.remove(safeTile);
					for (RSTile pad : pads) {
						stopNodes(pad);
					}
				} else safeCount++;
				safeTile = null;
			} else {
				safeCount = 0;
				me = myLoc();
				o:switch (random(0, 13)) {
				case 0:
					RSTile center = targetRoom.getCentralTile();
					if (!straightLine(me, center, previous) && walkTo(center, 0)) {
						waitToEat(true);
						break;
					}
				case 1:
					RSTile rand = new RSTile(mX + random(-1, 2), mY + random(-1, 2));
					if (!straightLine(me, rand, previous) && isGoodTile(rand) && walkTo(rand, 0)) {
						waitToEat(true);
						break;
					}
				case 2:
				case 3:
				case 4:
					for (RSTile t : getAdjacentTo(me)) {
						if (!straightLine(me, t, previous) && isGoodTile(t) && walkTo(t, 0)) {
							waitToEat(true);
							break o;
						}
					}
				case 5:
				case 6:
					RSTile test = null;
					for (RSTile t : getAdjacentTo(me)) {
						if (!straightLine(me, t, previous) && isGoodTile(t))
							test = t;
					}
					if (walkTo(test, 0)) {
						waitToEat(true);
						break;
					}
				case 7:
				case 8:
				case 9:
					for (RSTile t : getDiagonalTo(me)) {
						if (!straightLine(me, t, previous) && isGoodTile(t) && walkTo(t, 0)) {
							waitToEat(true);
							break o;
						}
					}
				case 10:
				case 11:
				case 12:
					RSTile test2 = null;
					for (RSTile t : getDiagonalTo(me)) {
						if (!straightLine(me, t, previous) && isGoodTile(t))
							test2 = t;
					}
					if (walkTo(test2, 0)) {
						waitToEat(true);
						break;
					}
				}
				if (!myLoc().equals(me))
					previous = me;
			}
			sleep(100, 400);
			previous = myLoc();
		}
		RSObject corner;
		secondaryStatus = "Navigating to the nearest corner";
		while ((corner = getObjInRoom(SNOWY_STOPS)) != null && !myLoc().equals(safeTile = corner.getLocation())) {
			smartSleep(walkTo(safeTile, 0), false);
		}
		for (RSGroundItem g : getRoomItems()) {
			RSTile gTile = g.getLocation();
			RSObject o = objects.getTopAt(gTile);
			if (o == null || !intMatch(o.getID(), SNOWY_STOPS) && !badTiles.contains(gTile))
				badTiles.add(gTile);
		}
		getBestDoor();
		walkToMap(destDoor, 0);
		return 1;
	}

	private int puzzleFishingFerret() {
		RSObject destPad = getObjInRoom("Pressure plate");
		if (destPad == null)
			return -1;
		final int[] SAFE_SPOTS = { 49546, 49547, 49548, 54293, 35293 };
		final RSTile dest = destPad.getLocation();
		RSNPC ferret;
		RSTile fTile;
		while ((ferret = getNpcInRoom(FISHING_FERRET)) != null && !(fTile = ferret.getLocation()).equals(dest)) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			int fishNeeded = makeSmartPath(fTile, dest, SAFE_SPOTS).size();
			if (inventory.getCount(COOKED_VILE_FISH) < fishNeeded) {
				RSObject fire = getObjInRoom(FIRES);
				if (inventory.contains(RAW_VILE_FISH) && (calc.distanceTo(fire) < 4 || inventory.getCount

(RAW_VILE_FISH, COOKED_VILE_FISH) > fishNeeded + 1)) {
					int fishCount = inventory.getCount(RAW_VILE_FISH);
					if (useItem(RAW_VILE_FISH, fire)) {
						waitToStart(true);
						while (isMoving() || player().getAnimation() != -1) {
							if (inventory.getCount(RAW_VILE_FISH) != fishCount)
								break;
						}
					}
				} else if (doObjAction(getObjInRoom(49561, 49562, 49563, 54298, 35347), "Fish")) {
					waitToAnimate();
					waitToStop(true);
				}
			} else {
				for (RSTile t : makeSmartPath(fTile, dest, SAFE_SPOTS)) {
					safeTile = t;
					while ((ferret = getNpcInRoom(FISHING_FERRET)) != null && getNpcAt(t) == null && player

().getAnimation() != 13325) {
						if (failSafe())
							return 2;
						if (!requirements())
							return -1;
						if (ferret.getLocation().equals(dest) || !inventory.contains(COOKED_VILE_FISH))
							break;
						if (!calc.tileOnScreen(t) && !isMoving()) {
							walkFast(t, 1);
							turnTo(t);
						} else if (selectItem(COOKED_VILE_FISH) && !isMoving() && getNpcAt(t) == null && 

clickTile(t, "Use")) {
							if (waitToStop(true)) {
								for (int c = 0; c < 10; c++) {
									if (getNpcAt(t) == null)
										break;
									sleep(200, 400);
								}
							}
						} else sleep(200, 400);
					}
					while (player().getAnimation() == 13325) {
						sleep(100, 200);
					}
				}
				sleep(400, 800);
				getBestDoor();
				while ((ferret != null && ferret.isMoving()) || getNpcAt(dest) != null) {
					if (calc.distanceTo(destDoor) > 3 && !isMoving())
						walkTo(destDoor, 1);
					sleep(100, 200);
				}
			}
			sleep(100, 200);
		}
		puzzlePoints.clear();
		return 1;
	}

	private int puzzleFlipTiles() {
		final int[] GREEN_FLIP_TILES = { 49638, 49639, 49640, 54065, 39859 };
		final int[] YELLOW_FLIP_TILES = { 49641, 49642, 49643, 54066, 39860 };
		RSTile startTile = getNearestObjTo(new RSTile(0, 20000), "tile");
		if (startTile == null)
			return -1;
		int[] flipColors = YELLOW_FLIP_TILES;
		int[] antiColors = GREEN_FLIP_TILES;
		String antiName = "Green";
		if (getObjsInRoom(flipColors).length > random(11, 13)) {
			flipColors = GREEN_FLIP_TILES;
			antiColors = YELLOW_FLIP_TILES;
			antiName = "Yellow";
		}
		for (int row = 0; row < 4; row++) {
			RSTile currentLeader = new RSTile(startTile.getX(), startTile.getY() - row);
			for (int col = 0; col < 5; col++) {
				RSObject tile;
				RSTile hT = new RSTile(currentLeader.getX() + col, currentLeader.getY());
				safeTile = new RSTile(hT.getX(), hT.getY() - 1);
				o:while ((tile = objects.getTopAt(hT)) == null || !intMatch(tile.getID(), flipColors)) {
					if (failSafe())
						return 2;
					if (!requirements())
						return -1;
					if (calc.distanceTo(safeTile) > 4 && walkToMap(safeTile, 1))
						waitToEat(false);
					if (clickTile(safeTile, "Imbue") && waitToStart(true)) {
						if (!waitToAnimate())
							waitToAnimate();
						for (int c = 0; c < 8; c++) {
							if ((tile = objects.getTopAt(hT)) != null && intMatch(tile.getID(), 

flipColors))
								break o;
							sleep(150, 250);
						}
					}
					sleep(100, 200);
				}
			}
		}
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (doObjAction(getObjInRoom(antiColors), "Force " + antiName)) {
				waitToAnimate();
				topUp(true);
				waitToStop(false);
			}
			sleep(500, 600);
		}
		return 1;
	}

	private int puzzleFollowTheLeader() {
		RSNPC leader = npcs.getNearest(FTL_STATUES);
		if (leader == null || !slayerCheck())
			return failSafe() ? 2 : -1;
		safeTile = getNearestObjTo(leader.getLocation(), ACTIVE_PADS);
		if (safeTile == null)
			return -1;
		while ((leader = npcs.getNearest(FTL_STATUES)) != null) {
			if (failSafe())
				return 2;
			if (!myLoc().equals(safeTile)) {
				if (walkTo(safeTile, 0))
					waitToEat(false);
			} else if (leader.getAnimation() != -1) {
				int anim = leader.getAnimation();
				if (interfaces.get(238).isValid()) {
					String emote = "";
					if (anim == 855) {
						emote = "Nod head";
					} else if (anim == 856) {
						emote = "Shake head";
					} else if (anim == 860) {
						emote = "Cry";
					} else if (anim == 861) {
						emote = "Laugh";
					} else if (anim == 863) {
						emote = "Wave";
					}
					if (emote.isEmpty()) {
						walkAdjacentTo(safeTile, 1);
					} else {
						sleep(700, 1400);
						if (clickDialogueOption(interfaces.get(238), emote)) {
							sleep(400, 800);
							while (leader.getAnimation() != -1) {
								sleep(300, 600);
							}
						}
					}
				}
			}
			sleep(300, 600);
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
			RSObject barCrate = getObjInRoom(49528, 49529, 49530);
			RSObject logCrate = getObjInRoom(49534, 49535, 49536);
			RSObject fishCrate = getObjInRoom(49522, 49523, 49524);
			smartAction(ACTIONS, barCrate, logCrate, fishCrate);
		}
		return 1;
	}

	private int puzzleGhosts() {
		while (npcs.getNearest(monster) != null) {
			if (failSafe())
				return 2;
			eatFood(goodFoods, 40, 50);
			nearMonster = getNpcInRoom(GHOSTS);
			if (nearMonster != null && !isAttacking(nearMonster)) {
				if (combatStyle == Style.MELEE && calc.distanceTo(nearMonster) > 4) {
					walkToMap(nearMonster.getLocation(), 1);
				} else attackNpc(nearMonster);
				sleep(800, 1300);
			}
			sleep(100, 150);
		}
		return 1;
	}

	private int puzzleHoardStalker() {
		if (!memberCheck() || !training(Skills.SUMMONING))
			return -1;
		final int[] ITEM_CHESTS = { 49594, 49595, 49596 }, ITEM_BARRELS = { 49597, 49598, 49599 };
		int riddleItem = -1, riddleLocation = 0;
		RSGroundItem shield = getItemInRoom(targetRoom, O_SHIELD);
		RSObject barrel = getObjInRoom(ITEM_BARRELS), chest = getObjInRoom(ITEM_CHESTS);
		RSTile reachCheck = shield != null ? shield.getLocation() : null;
		String itemName = "";
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			RSObject lockedDoor = reachCheck == null ? getObjInRoom(RIDDLE_DOORS) : getNearObjTo(reachCheck, 

RIDDLE_DOORS);
			if (riddleItem == -1) {
				RSComponent riddleC = interfaces.getComponent(242, 4);
				if (!riddleC.isValid())
					riddleC = interfaces.getComponent(241, 4);
				if (riddleC.isValid()) {
					String riddle = riddleC.getText();
					if (riddle.contains("yetsan")) {
						failReason = "Level requirements";
						return -1;
					} else if (riddle.contains("lost in winds")) {
						riddleItem = O_ASHES;
					} else if (riddle.contains("stones may")) {
						riddleItem = O_BONES;
					} else if (riddle.contains("friends fly")) {
						riddleItem = O_SHIELD;
					} else {
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
							if (riddle.contains("a deathslinger")) {
								riddleItem = O_BOWSTRING;
							} else if (riddle.contains("cannot illuminate")) {
								riddleItem = O_FEATHER;
							} else if (riddle.contains("blunt force")) {
								riddleItem = O_HAMMER;
							} else if (riddle.contains("make you dead")) {
								riddleItem = O_ARROW;
							} else {
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
						bugReport(false);
						return -1;
					}
					if (developer)
						log("Fetching item: " + itemName + " (" + riddleItem + "); Location: " + 

riddleLocation);
				} else toWait = doNpcAction(getNpcInRoom(HOARDSTALKER), "Get-Riddle");
			} else if (inventory.contains(riddleItem)) {
				int start = inventory.getCount(riddleItem);
				while (inventory.getCount(riddleItem) == start) {
					if (failSafe())
						return 2;
					smartSleep(useItem(riddleItem, getNpcInRoom(HOARDSTALKER)), false);
				}
				break;
			} else if (lockedDoor != null) {
				doObjAction(lockedDoor, "Open");
			} else if (riddleLocation == 0 || riddleLocation == 3) {
				toWait = pickUpItem(getItemInRoom(targetRoom, riddleItem));
				sleep(400, 800);
			} else if (riddleLocation == 1) {
				if (interfaces.get(238).isValid()) {
					clickDialogueOption(interfaces.get(238), itemName);
				} else toWait = doObjAction(getObjInRoom(ITEM_CHESTS), "Search");
			} else if (riddleLocation == 2) {
				for (RSObject o : getObjsInRoom(ITEM_BARRELS)) {
					if (!isEdgeTile(o.getLocation()) && reachableObj(o)) {
						barrel = o;
						break;
					}
				}
				if (interfaces.get(237).isValid()) {
					clickDialogueOption(interfaces.get(237), itemName, "More");
				} else toWait = doObjAction(barrel, "Search");
			}
			smartSleep(toWait, true);
		}
		return 1;
	}

	private int puzzleHunterFerret() {
		final int[] CORNERS = { 50663, 51762, 54785, 56274 };
		final int DRY_LOGS = 17377, TRAPS = 17378, SET_TRAPS = 49592, COMPLETED_TRAPS = 49593;
		if (!memberCheck())
			return -1;
		while (getObjInRoom(COMPLETED_TRAPS) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			RSNPC ferret = getNpcInRoom(HUNTER_FERRET);
			RSObject branchesSource = getObjInRoom(49589, 49590, 49591, 54005, 36418);
			int trapsSet = getObjsInRoom(SET_TRAPS).length;
			if (trapsSet < 3) {
				if (inventory.getCount(DRY_LOGS, TRAPS) < 3 - trapsSet) {
					if (doObjAction(branchesSource, "Chop"))
						waitToStop(true);
				} else if (inventory.contains(TRAPS)) {
					RSTile testCorner = null;
					safeTile = null;
					if (calc.distanceTo(targetRoom.getCentralTile()) > 6) {
						walkTo(targetRoom.getCentralTile(), 3);
						waitToEat(false);
					}
					for (RSObject corner : getObjsInRoom(CORNERS)) {
						RSTile cornerTile = corner.getLocation();
						RSTile test = getNearestObjTo(cornerTile, SET_TRAPS);
						if (test == null || calc.distanceBetween(cornerTile, test) > 3) {
							testCorner = cornerTile;
							break;
						}
					}
					if (testCorner != null) {
						RSTile newTile = null;
						for (RSTile t : getDiagonalTo(testCorner)) {
							RSObject test = objects.getTopAt(t);
							if (areaContains(targetRoom, t) && (test == null || test.getType() == 

Type.FLOOR_DECORATION)) {
								newTile = t;
								break;
							}
						}
						if (newTile != null) {
							for (RSTile t : getAdjacentTo(newTile)) {
								if (objects.getTopAt(t) == null) {
									safeTile = t;
									break;
								}
							}
							if (safeTile != null && inventory.contains(TRAPS)) {
								while (!myLoc().equals(safeTile)) {
									if (failSafe())
										return 2;
									walkFast(safeTile, 0);
									if (!inventory.contains(TRAPS)) {
										if (!inventory.contains(DRY_LOGS))
											break;
										inventory.getItem(DRY_LOGS).doClick(true);
										sleep(400, 700);
									}
									waitToStop(false);
								}
								if (ridItem(TRAPS, "Lay")) {
									sleep(1000, 1300);
									waitToStop(true);
								}
							}
						}
					}
				} else if (inventory.contains(DRY_LOGS)) {
					inventory.getItem(DRY_LOGS).doClick(true);
					sleep(700, 900);
				}
			} else {
				if (ferret != null)
					safeTile = ferret.getLocation();
				walkToMap(safeTile, 2);
			}
			waitToStop(false);
		}
		while (getObjInRoom(COMPLETED_TRAPS) != null) {
			if (failSafe())
				return 2;
			if (doObjAction(getObjInRoom(COMPLETED_TRAPS), "Pick-up"))
				waitToStop(true);
			sleep(400, 600);
		}
		return 1;
	}

	private int puzzleLodestonePower() {
		final int[] POWER_CRYSTALS = { 17376, 18635 };
		final int[] START_BLOCKS = { 50795, 51889, 54844, 56368 };
		RSTile[] jumpPath = null;
		RSTile dT = getBackDoor(), startTile = null;
		RSObject lodestone = getObjInRoom(POWER_LODESTONES);
		if (lodestone == null)
			return -1;
		RSTile lT = lodestone.getLocation();
		if (dT.getX() - lT.getX() > 4) {
			startTile = getNearestObjTo(new RSTile(20000, 20000), START_BLOCKS);
			if (startTile == null)
				return -1;
			int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new RSTile[] { new RSTile(sX - 1, sY), new RSTile(sX - 2, sY - 1), new RSTile(sX - 2, sY - 3), new 

RSTile(sX - 2, sY - 5), new RSTile(sX - 3, sY - 6), new RSTile(sX - 5, sY - 6) };
		} else if (dT.getX() - lT.getX() < -4) {
			startTile = getNearestObjTo(new RSTile(20000, 0), START_BLOCKS);
			if (startTile == null)
				return -1;
			int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new RSTile[] { new RSTile(sX + 1, sY), new RSTile(sX + 2, sY + 1), new RSTile(sX + 2, sY + 3), new 

RSTile(sX + 2, sY + 5), new RSTile(sX + 3, sY + 6), new RSTile(sX + 5, sY + 6) };
		} else if (dT.getY() - lT.getY() < -4) {
			startTile = getNearestObjTo(new RSTile(20000, 0), START_BLOCKS);
			if (startTile == null)
				return -1;
			int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new RSTile[] { new RSTile(sX, sY + 1), new RSTile(sX - 1, sY + 2), new RSTile(sX - 3, sY + 2), new 

RSTile(sX - 5, sY + 2), new RSTile(sX - 6, sY + 3), new RSTile(sX - 6, sY + 5) };
		} else if (dT.getY() - lT.getY() > 4) {
			startTile = getNearestObjTo(ORIGIN, START_BLOCKS);
			if (startTile == null)
				return -1;
			int sX = startTile.getX(), sY = startTile.getY();
			jumpPath = new RSTile[] { new RSTile(sX, sY - 1), new RSTile(sX + 1, sY - 2), new RSTile(sX + 3, sY - 2), new 

RSTile(sX + 5, sY - 2), new RSTile(sX + 6, sY - 3), new RSTile(sX + 6, sY - 5) };
		} else return -1;
		dropItem(GGS);
		boolean stumbled = false;
		while (getObjInRoom("Active lodestone") == null) {
			if (failSafe()) {
				if (!puzzleTimer.isRunning() && puzzleRooms.contains(targetRoom))
					unBacktrackable.add(targetRoom);
				return 2;
			}
			boolean toWait = false;
			eatFood(foods, 50, 50);
			if (!inventory.containsOneOf(POWER_CRYSTALS)) {
				toWait = pickUpItem(getItemInRoom(targetRoom, POWER_CRYSTALS));
			} else {
				RSTile nextJump = null;
				if (stumbled && getNpcInRoom("Guardian sphere") == null)
					stumbled = false;
				if (stumbled) {
					double bestDist = 10;
					RSTile safety = getObjLocation(START_BLOCKS);
					if (safety != null) {
						for (RSTile t : getAdjacentTo(myLoc())) {
							double dist = calc.distanceBetween(t, safety);
							if (dist < bestDist) {
								bestDist = dist;
								safeTile = t;
							}
						}
						toWait = safeTile != null && doObjAction(objects.getTopAt(safeTile), "Jump-over");
					}
				} else {
					String nextAction = "Jump-over";
					safeTile = null;
					for (int I = 0; I < jumpPath.length; I++) {
						RSTile next = jumpPath[I];
						if (reachable(next, true)) {
							if (I == 5 && calc.distanceBetween(myLoc(), lodestone.getLocation()) < 4.5) {
								next = lodestone.getLocation();
								nextAction = "Unlock";
							}
							safeTile = next;
							nextJump = next;
						}
					}
					if (nextJump == null) {
						if (developer) {
							log("Next jumpTile is null!");
							sleep(1000);
						}
						if (!reachable(jumpPath[0], false)) {
							sleep(200, 400);
							continue;
						}
						nextJump = jumpPath[0];
						safeTile = jumpPath[0];
					}
					if (doObjAction(objects.getTopAt(nextJump), nextAction)) {
						o:for (int i = 0; i < 15; i++) {
							switch (player().getAnimation()) {
							case 769:
								break o;
							case 846:
								stumbled = true;
								break o;
							case 13493:
								log(RED, "Damn, we got hit by the orb");
								break o;
							}
							sleep(200, 300);
						}
					toWait = true;
					}
				}
			}
			smartSleep(toWait, true);
		}
		idleTimer.reset();
		puzzleTimer.reset();
		if (!teleHomeAndBack()) {
			unBacktrackable.add(targetRoom);
			return -1;
		}
		return 1;
	}

	private int puzzleMaze() {
		int[] centerPath, currentPath, edgePath;
		RSTile[] barriers;
		boolean NS = false;
		int mX = 1, mY = 1;
		RSObject chest = getObjInRoom(CLOSED_CHESTS);
		RSObject lever = getObjInRoom(49351, 49352, 49353, 54409);
		if (chest == null || lever == null)
			return -1;
		RSTile cTile = chest.getLocation(), sTile = lever.getLocation(), hT = null;
		if (cTile.getX() - sTile.getX() < -4) {
			hT = targetRoom.getNearestTile(ORIGIN);
		} else if (cTile.getX() - sTile.getX() > 4) {
			mX = -1;
			mY = -1;
			hT = targetRoom.getNearestTile(new RSTile(20000, 20000));
		}
		if (cTile.getY() - sTile.getY() < -4) {
			mX = -1;
			hT = targetRoom.getNearestTile(new RSTile(20000, 0));
			NS = true;
		} else if (cTile.getY() - sTile.getY() > 4) {
			mY = -1;
			hT = targetRoom.getNearestTile(new RSTile(0, 20000));
			NS = true;
		}
		if (hT == null)
			return -1;
		int hX = hT.getX(), hY = hT.getY();
		RSTile[] barriersNS = new RSTile[] { new RSTile(hX + 14 * mX, hY + 5 * mY), new RSTile(hX + 14 * mX, hY + 7 * mY), new 

RSTile(hX + 14 * mX, hY + 12 * mY), new RSTile(hX + 13 * mX, hY + 8 * mY), new RSTile(hX + 13 * mX, hY + 6 * mY), new RSTile(hX + 11 * 

mX, hY + 2 * mY), new RSTile(hX + 11 * mX, hY + 7 * mY), new RSTile(hX + 10 * mX, hY + 3 * mY), new RSTile(hX + 10 * mX, hY + 8 * mY), 

new RSTile(hX + 10 * mX, hY + 13 * mY), new RSTile(hX + 8 * mX, hY + 1 * mY), new RSTile(hX + 8 * mX, hY + 4 * mY), new RSTile(hX + 8 

* mX, hY + 10 * mY), new RSTile(hX + 8 * mX, hY + 14 * mY), new RSTile(hX + 7 * mX, hY + 5 * mY), new RSTile(hX + 6 * mX, hY + 3 * 

mY), new RSTile(hX + 6 * mX, hY + 11 * mY), new RSTile(hX + 5 * mX, hY + 2 * mY), new RSTile(hX + 5 * mX, hY + 7 * mY), new RSTile(hX 

+ 5 * mX, hY + 12 * mY), new RSTile(hX + 4 * mX, hY + 6 * mY), new RSTile(hX + 4 * mX, hY + 9 * mY), new RSTile(hX + 4 * mX, hY + 13 * 

mY), new RSTile(hX + 4 * mX, hY + 14 * mY), new RSTile(hX + 3 * mX, hY + 9 * mY), new RSTile(hX + 2 * mX, hY + 7 * mY), new RSTile(hX 

+ 1 * mX, hY + 3 * mY), new RSTile(hX + 1 * mX, hY + 9 * mY), new RSTile(hX + 1 * mX, hY + 11 * mY) };
		RSTile[] barriersEW = new RSTile[] { new RSTile(hX + 5 * mX, hY + 14 * mY), new RSTile(hX + 7 * mX, hY + 14 * mY), new 

RSTile(hX + 12 * mX, hY + 14 * mY), new RSTile(hX + 8 * mX, hY + 13 * mY), new RSTile(hX + 6 * mX, hY + 12 * mY), new RSTile(hX + 2 * 

mX, hY + 11 * mY), new RSTile(hX + 7 * mX, hY + 11 * mY), new RSTile(hX + 3 * mX, hY + 10 * mY), new RSTile(hX + 8 * mX, hY + 10 * 

mY), new RSTile(hX + 13 * mX, hY + 10 * mY), new RSTile(hX + 1 * mX, hY + 8 * mY), new RSTile(hX + 4 * mX, hY + 8 * mY), new RSTile(hX 

+ 10 * mX, hY + 8 * mY), new RSTile(hX + 14 * mX, hY + 8 * mY), new RSTile(hX + 5 * mX, hY + 7 * mY), new RSTile(hX + 3 * mX, hY + 6 * 

mY), new RSTile(hX + 11 * mX, hY + 6 * mY), new RSTile(hX + 2 * mX, hY + 5 * mY), new RSTile(hX + 7 * mX, hY + 5 * mY), new RSTile(hX 

+ 12 * mX, hY + 5 * mY), new RSTile(hX + 6 * mX, hY + 4 * mY), new RSTile(hX + 9 * mX, hY + 4 * mY), new RSTile(hX + 13 * mX, hY + 4 * 

mY), new RSTile(hX + 14 * mX, hY + 4 * mY), new RSTile(hX + 9 * mX, hY + 3 * mY), new RSTile(hX + 7 * mX, hY + 2 * mY), new RSTile(hX 

+ 3 * mX, hY + 1 * mY), new RSTile(hX + 9 * mX, hY + 1 * mY), new RSTile(hX + 11 * mX, hY + 1 * mY) };
		RSObject test1, test2, test3, test4, test5;
		if (NS) {
			barriers = barriersNS;
			test1 = objects.getTopAt(new RSTile(hX + 4 * mX, hY + 5 * mY));
			test2 = objects.getTopAt(new RSTile(hX + 9 * mX, hY + 2 * mY));
			test3 = objects.getTopAt(new RSTile(hX + 5 * mX, hY + 2 * mY));
			test4 = objects.getTopAt(new RSTile(hX + 3 * mX, hY + 5 * mY));
			test5 = objects.getTopAt(new RSTile(hX + 3 * mX, hY + 1 * mY));
		} else {
			barriers = barriersEW;
			test1 = objects.getTopAt(new RSTile(hX + 5 * mX, hY + 4 * mY));
			test2 = objects.getTopAt(new RSTile(hX + 2 * mX, hY + 9 * mY));
			test3 = objects.getTopAt(new RSTile(hX + 2 * mX, hY + 5 * mY));
			test4 = objects.getTopAt(new RSTile(hX + 5 * mX, hY + 3 * mY));
			test5 = objects.getTopAt(new RSTile(hX + 1 * mX, hY + 3 * mY));
		}
		if (test1 != null && test1.getType() == Type.INTERACTABLE) {
			centerPath = new int[] { 14, 29, 27, 26, 16, 7, 15 };
			edgePath = new int[] { 15, 7, 16, 26, 27 };
		} else if (test3 != null && test3.getType() == Type.INTERACTABLE) {
			centerPath = new int[] { 14, 26, 16, 8, 4, 10, 20, 7, 13 };
			edgePath = new int[] { 15, 7, 20, 10, 1 };
		} else if (test4 != null && test4.getType() == Type.INTERACTABLE) {
			centerPath = new int[] { 14, 24, 29, 26, 10, 4, 8, 7, 15 };
			edgePath = new int[] { 13, 7, 8, 4, 1 };
		} else if (test2 != null && test2.getType() == Type.INTERACTABLE) {
			centerPath = new int[] { 14, 1, 11, 27, 29, 26, 16, 22, 13 };
			edgePath = new int[] { 15, 22, 16, 26, 29 };
		} else if (test5 != null && test5.getType() == Type.INTERACTABLE) {
			centerPath = new int[] { 14, 29, 27, 4, 8, 22, 15 };
			edgePath = new int[] { 15, 22, 8, 4, 1 };
		} else {
			failReason = "Unknown maze layout";
			return -1;
		}
		while (!roomSwitch) {
			if (failSafe())
				return 2;
			smartSleep(doObjAction(objects.getTopAt(sTile), "Pull"), false);
		}
		while (!puzzleSolved) {
			if (calc.distanceTo(sTile) < 4) {
				currentPath = centerPath;
			} else if (calc.distanceTo(cTile) < 3) {
				currentPath = edgePath;
			} else {
				failReason = "Unknown barrier path";
				return -1;
			}
			puzzlePoints.clear();
			if (Option.DEBUG.enabled()) {
				for (int I = 0; I < currentPath.length; I++) {
					puzzlePoints.add(barriers[currentPath[I] - 1]);
				}
			}
			for (int I = 0; I < currentPath.length; I++) {
				if (isDead()) {
					teleBack();
				} else if (failSafe()) {
					return 2;
				}
				safeTile = barriers[currentPath[I] - 1];
				boolean barrierClicked = false;
				Timer barrierTimer = new Timer(random(20000, 40000));
				o:while (player().getAnimation() != 9516 || calc.distanceTo(safeTile) > 2) {
					if (!barrierTimer.isRunning()) {
						failReason = "We got stuck";
						return -1;
					}
					if (isDead()) {
						teleBack();
					} else if (failSafe()) {
						return 2;
					}
					if (barrierClicked && calc.distanceTo(safeTile) < 3 && player().getAnimation() == -1 && !

isMoving())
						waitToAnimate();
					if ((!barrierClicked || (player().getAnimation() == -1 && !isMoving())) && doObjAction

(objects.getTopAt(safeTile), "Go-through")) {
						barrierClicked = true;
						while (isMoving() && player().getAnimation() != 9516) {
							sleep(100, 200);
						}
						for (int i = 0; i < 10; i++) {
							if (player().getAnimation() == 9516 && calc.distanceTo(safeTile) < 3)
								break o;
							sleep(200, 300);
						}
					} else while (!calc.tileOnScreen(safeTile) && isMoving() && player().getAnimation() == -1) {
						sleep(200, 300);
					}
				}
				waitToStop(true);
				while (player().getAnimation() == 9516) {
					sleep(300, 400);
				}
			}
			if (Arrays.equals(currentPath, edgePath) && isEdgeTile(myLoc()))
				break;
			if ((chest = getObjInRoom(CLOSED_CHESTS)) != null && calc.distanceTo(chest) < 3) {
				while ((chest = getObjInRoom(CLOSED_CHESTS)) != null || nearItem != null) {
					if (isDead()) {
						teleBack();
					} else if (failSafe()) {
						return -1;
					}
					getKey();
					if (chest != null) {
						doObjAction(getObjInRoom(CLOSED_CHESTS), "Open");
					} else pickUpItem(nearItem);
					waitToStop(false);
				}
			}
		}
		return 1;
	}

	private int puzzleMercenaryLeader() {
		RSNPC leader = getNpcInRoom("Mercenary leader");
		setPrayer(false, Style.MAGIC, true);
		while (leader != null) {
			if (failSafe())
				return 2;
			attackNpc(leader);
			if (unreachable) {
				topUp(true);
				sleep(600, 900);
				unreachable = false;
			}
			if (eatFood(goodFoods, 50, 55))
				doNpcAction(leader, "Attack");
			sleep(500, 1000);
			leader = getNpcInRoom("Mercenary leader");
		}
		setPrayersOff();
		if (failSafe())
			return 2;
		return 1;
	}

	private int puzzleMonolith() {
		RSNPC monolith;
		setRetaliate(false);
		tempMode = Melee.SLASH;
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if ((monolith = getNpcInRoom(MONOLITHS)) != null) {
				if (!intMatch(monolith.getID(), 10978, 10979, 10980, 12176, 12972)) {
					safeTile = monolith.getLocation();
					if (doNpcAction(monolith, "Activate"))
						waitToStop(false);
				} else {
					nearMonster = npcs.getNearest(monolithShade);
					if (nearMonster == null)
						nearMonster = npcs.getNearest(monster);
					if (nearMonster == null) {
						safeTile = monolith.getLocation();
						if (calc.distanceTo(safeTile) > 2 && !isMoving())
							walkToMap(safeTile, 1);
						if (!topUp(true))
							sleep(300, 600);
					} else if (attackNpc(nearMonster)) {
						safeTile = null;
					}
					eatFood(goodFoods, 50, 50);
				}
			}
			sleep(100, 200);
		}
		setRetaliate(true);
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
			int herbID = getItemID(herb);
			RSObject unlit = getObjInRoom(UNLIT_CENSERS);
			RSObject herbPatch = getObjInRoom(HERB_PATCH);
			if (getNpcInRoom(POLTERGEIST) == null) {
				toWait = doObjAction(getObjInRoom(CLOSED_SARCOPHAGUS), "Open");
			} else if (herb.isEmpty()) {
				RSComponent clueC = interfaces.getComponent(211, 2);
				if (clueC.isValid()) {
					String clueText = clueC.getText();
					if (clueText.contains("corianger")) {
						herb = "Corianger";
					} else if (clueText.contains("explosemary")) {
						herb = "Explosemary";
					} else if (clueText.contains("parslay")) {
						herb = "Parslay";
					} else if (clueText.contains("cardamaim")) {
						herb = "Cardamaim";
					} else if (clueText.contains("papreaper")) {
						herb = "Papreaper";
					} else if (clueText.contains("slaughtercress")) {
						herb = "Slaughtercress";
					} else {
						failReason = "Unknown herb";
						return -1;
					}
				} else toWait = doObjAction(getObjInRoom(CLOSED_SARCOPHAGUS), "Read");
			} else if (interfaces.get(232).isValid()) {
				clickDialogueOption(interfaces.get(232), herb, "More herbs");
			} else if (unlit != null) {
				if (!isMoving())
					doObjAction(unlit, "Light");
			} else if (herbPatch != null) {
				doItemAction(inventory.getItem(herbID), "Consecrate");
				if (doObjAction(herbPatch, "Harvest") && calc.distanceTo(herbPatch) > 1)
					waitToStop(false);
			} else if (herbID > 0 && inventory.contains(herbID)) {
				doItemAction(inventory.getItem(herbID), "Consecrate");
				sleep(700, 1100);
			} else if (inventory.contains(CONSECRATED_HERB)) {
				if (useItem(CONSECRATED_HERB, getObjInRoom(UNFILLED_CENSERS)))
					waitToStop(true);
			} else if (groundItems.getNearest(CONSECRATED_HERB) != null) {
				toWait = pickUpItem(groundItems.getNearest(CONSECRATED_HERB));
			}
			smartSleep(toWait, true);
		}
		return 1;
	}

	private int puzzlePondSkaters() {
		RSNPC targ = getNpcInRoom(POND_SKATER);
		while (getNpcInRoom(12090) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (targ == null) {
				targ = npcs.getNearest(skater);
			} else {
				if (walking.getEnergy() < random(15, 25)) {
					walking.rest(random(50, 80));
				} else if (doNpcAction(targ, "Catch")) {
					sleep(600, 800);
					for (int c = 0; c < 15; c++) {
						if (!isMoving() || player().getAnimation() != -1 || interfaces.get(210).isValid())
							break;
						sleep(250, 300);
					}
				}
			}
			sleep(100, 200);
		}
		return 1;
	}

	private int puzzleRamokeeFamiliars() {
		boolean normals = true;
		RSNPC healer, mager, meleer, ranger;
		while (getNpcInRoom("Ramokee") != null) {
			if (failSafe())
				return 2;
			boolean toWait = false;
			RSNPC targ = null;
			setPrayer(true, Style.SUMMON, true);
			if (normals) {
				if ((nearMonster = npcs.getNearest(monster)) != null) {
					targ = nearMonster;
				} else normals = false;
			} else if ((healer = getNpcInRoom("skinweaver")) != null) {
				targ = healer;
			} else if ((mager = getNpcInRoom("stormbringer")) != null) {
				targ = mager;
			} else if ((ranger = getNpcInRoom("deathslinger")) != null) {
				targ = ranger;
			} else if ((meleer = getNpcInRoom("bloodrager")) != null) {
				targ = meleer;
			}
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
		final int SWORD = 17416, BOW = 17418, STAFF = 17420;
		int meleeCount = getNpcsInRoom(MELEE_STATUES).length;
		int rangeCount = getNpcsInRoom(RANGE_STATUES).length;
		int magicCount = getNpcsInRoom(MAGIC_STATUES).length;
		while (getNpcInRoom(FINISHED_STATUES) == null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (inventory.getCount(SWORD, BOW, STAFF) < getNpcsInRoom(TEN_UNARMED_STATUES).length) {
				if (!inventory.contains(CHISEL)) {
					pickUpItem(getItemInRoom(targetRoom, CHISEL));
				} else if (interfaces.get(451).isValid()) {
					String itemName = "";
					if (inventory.getCount(STAFF) < meleeCount) {
						itemName = "Staff";
					} else if (inventory.getCount(SWORD) < rangeCount) {
						itemName = "Sword";
					} else if (inventory.getCount(BOW) < magicCount) {
						itemName = "Bow";
					}
					clickDialogueOption(interfaces.get(451), itemName);
				} else if (inventory.contains(CARVE_BLOCK)) {
					doItemAction(inventory.getItem(CARVE_BLOCK), "Carve");
				} else if (doObjAction(getObjInRoom(CRUMBLING_MINES), "Mine")) {
					waitToStop(true);
				}
			} else {
				RSNPC nextStatue = getNpcInRoom(TEN_UNARMED_STATUES);
				if (nextStatue != null) {
					RSNPC heading = null;
					safeTile = nextStatue.getLocation();
					int sX = safeTile.getX(), sY = safeTile.getY();
					RSTile[] testStatues = { new RSTile(sX - 4, sY), new RSTile(sX + 4, sY), new RSTile(sX, sY - 

4), new RSTile(sX, sY + 4) };
					for (RSTile test : testStatues) {
						if ((heading = getNpcAt(test)) != null) {
							if (!intMatch(heading.getID(), TEN_UNARMED_STATUES))
								break;
							heading = null;
						}
					}
					if (heading != null) {
						String itemName = "";
						for (int I = 0; I < ARMED_STATUES.length; I++) {
							if (intMatch(heading.getID(), ARMED_STATUES[I])) {
								if (I == 0) {
									itemName = "Staff";
								} else if (I == 1) {
									itemName = "Sword";
								} else if (I == 2) {
									itemName = "Bow";
								}
								break;
							}
						}
						if (getItemID("Stone " + itemName.toLowerCase()) != -1) {
							if (doNpcAction(nextStatue, "Arm")) {
								waitToStop(true);
								if (!interfaces.get(236).isValid() && !interfaces.get(451).isValid())
									sleep(600, 800);
								if (interfaces.get(236).isValid()) {
									if (clickDialogueOption(interfaces.get(236), itemName))
										sleep(400, 600);
								} else if (clickDialogueOption(interfaces.get(451), itemName)) {
									sleep(400, 600);
								}
							}
						}
					}
				}
			}
			sleep(500, 800);
			if (player().isInCombat()) {
				topUp(true);
				waitToStop(true);
			}
		}
		return 1;
	}

	private int puzzleThreeStatues() {
		final int[] UNARMED_MELEER = { 11036, 11037, 11038, 12094, 13057 };
		final int[] UNARMED_RANGER = { 11039, 11040, 11041, 12095, 13058 };
		final int[] UNARMED_MAGER = { 11042, 11043, 11044, 12096, 13059 };
		int[] equipStatues = null;
		String itemName = "";
		if (getNpcInRoom(UNARMED_MELEER) != null) {
			equipStatues = UNARMED_MELEER;
			itemName = "Sword";
		} else if (getNpcInRoom(UNARMED_RANGER) != null) {
			equipStatues = UNARMED_RANGER;
			itemName = "Bow";
		} else if (getNpcInRoom(UNARMED_MAGER) != null) {
			equipStatues = UNARMED_MAGER;
			itemName = "Staff";
		}
		if (equipStatues == null)
			return -1;
		while (getNpcInRoom(THREE_STATUES) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			if (!inventory.contains(CHISEL)) {
				toWait = pickUpItem(getItemInRoom(targetRoom, CHISEL));
			} else if (getItemID("Stone " + itemName.toLowerCase()) != -1) {
				toWait = doNpcAction(getNpcInRoom(equipStatues), "Arm");
			} else if (interfaces.get(451).isValid()) {
				clickDialogueOption(interfaces.get(451), itemName);
			} else if (inventory.contains(CARVE_BLOCK)) {
				dodgeSlayerMonster();
				doItemAction(inventory.getItem(CARVE_BLOCK), "Carve");
			} else toWait = doObjAction(getObjInRoom(CRUMBLING_MINES), "Mine");
			smartSleep(toWait, true);
			waitForDamage();
		}
		return 1;
	}

	private int puzzleTimedSwitches() {
		RSTile backDoor = getBackDoor();
		RSTile start = getNearestObjTo(backDoor, LEVERS);
		if (start == null)
			return -1;
		mouse.setSpeed(random(3, 5));
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (!roomSwitch) {
				if (walking.getEnergy() < random(25, 30)) {
					walking.rest(random(90, 100));
				} else if (calc.distanceTo(start) > 1) {
					walkTo(start, 1);
				} else if (getObjsInRoom(LEVERS).length == 5) {
					doObjAction(objects.getTopAt(start), "Pull");
					while (!puzzleSolved && !roomSwitch) {
						if (failSafe())
							return 2;
						RSObject lever = getObjInRoom(LEVERS), next = getNextObj(lever, LEVERS);
						if (lever != null) {
							safeTile = lever.getLocation();
							if (doObjAction(lever, "Pull")) {
								if (calc.distanceTo(safeTile) < 4) {
									for (int i = 0; i < 10; i++) {
										if (next != null && !isMoving() && 

calc.distanceBetween(myLoc(), safeTile) == 1 && !next.isOnScreen()) {
											walkToMap(next.getLocation(), 0);
											break;
										}
										sleep(50, 100);
									}
								} else {
									waitToStart(false);
									while (isMoving() && calc.distanceTo(lever) > 2) {
										sleep(100, 200);
									}
								}
							} else {
								while (!roomSwitch && lever != null && isMoving() && !

lever.isOnScreen()) {
									sleep(50, 100);
								}
							}
						}
						sleep(50, 100);
					}
				} else topUp(true);
			}
			if (roomSwitch) {
				log(RED, "Failed to pull the switches in time, restarting.");
				roomSwitch = false;
				safeTile = null;
				walkFast(start, 1);
				omNomNom();
			}
			waitToStop(false);
		}
		return 1;
	}

	private int puzzleSeekerSentinel() {
		getKey();
		getBestDoor();
		while (nearDoor != null && ((nearItem != null && areaContains(targetRoom, nearItem.getLocation())) || calc.distanceTo

(nearDoor) > 3)) {
			if (failSafe())
				return 2;
			RSTile kTile = getKey();
			if (nearItem != null) {
				if (calc.distanceTo(kTile) > 3) {
					walkToMap(kTile, 1);
				} else pickUpItem(nearItem);
			} else walkToDoor(2);
			waitToStop(true);
			waitToEat(true);
			getBestDoor();
		}
		puzzleRepeat = true;
		return 1;
	}

	private int puzzleSleepingGuards() {
		if (!bounds.contains((Integer) GUARD_KEY))
			bounds.add((Integer) GUARD_KEY);
		while (!inventory.contains(GUARD_KEY) || getItemInRoom(targetRoom, GUARD_KEY) != null || npcs.getNearest(guardians) != 

null) {
			if (failSafe())
				return 2;
			getItemInRoom(targetRoom, GUARD_KEY);
			if (nearItem != null) {
				pickUpItem(nearItem);
			} else if (npcs.getNearest(monster) == null) {
				if (walkTo(targetRoom.getCentralTile(), 4))
					waitToEat(false);
			} else if (!fightMonsters()) {
				return 2;
			}
			waitToStop(false);
		}
		puzzleRepeat = doorCount(goodDoors) > 1;
		return 1;
	}

	private int puzzleSliderPuzzle() {
		final int[] TILE_1 = { 12125, 12133, 12141, 12149, 12963 }, TILE_2 = { 12126, 12134, 12142, 12150, 12964 }, TILE_3 = { 

12127, 12135, 12143, 12151, 12965 };
		final int[] TILE_4 = { 12128, 12136, 12144, 12152, 12966 }, TILE_5 = { 12129, 12137, 12145, 12153, 12967 }, TILE_6 = { 

12130, 12138, 12146, 12154, 12968 };
		final int[] TILE_7 = { 12131, 12139, 12147, 12155, 12969 }, TILE_8 = { 12132, 12140, 12148, 12156, 12970 }, TILE_9 = 

null;
		final int[][] ID_ROW_1 = { TILE_1, TILE_2, TILE_3 };
		final int[][] ID_ROW_2 = { TILE_4, TILE_5, TILE_6 };
		final int[][] ID_ROW_3 = { TILE_7, TILE_8, TILE_9 };
		final int[][][] ID_SPOTS = { ID_ROW_1, ID_ROW_2, ID_ROW_3 };
		RSTile start = getNearestObjTo(ORIGIN, SLIDER_BACKGROUNDS);
		if (start == null)
			return -1;
		final int sX = start.getX(), sY = start.getY();
		final RSTile[] ROW_1 = { new RSTile(sX + 1, sY + 5), new RSTile(sX + 3, sY + 5), new RSTile(sX + 5, sY + 5) };
		final RSTile[] ROW_2 = { new RSTile(sX + 1, sY + 3), new RSTile(sX + 3, sY + 3), new RSTile(sX + 5, sY + 3) };
		final RSTile[] ROW_3 = { new RSTile(sX + 1, sY + 1), new RSTile(sX + 3, sY + 1), new RSTile(sX + 5, sY + 1) };
		final RSTile[][] SLIDE_SPOTS = { ROW_1, ROW_2, ROW_3 };
		while (getNpcAt(ROW_3[2]) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			if (lastMessage.contains("budge")) {
				camera.moveRandomly(random(500, 1500));
				lastMessage = "";
			}
			o:for (int I = 0; I < SLIDE_SPOTS.length; I++) {
				for (int J = 0; J < SLIDE_SPOTS[I].length; J++) {
					if (getNpcAt(SLIDE_SPOTS[I][J]) == null) {
						safeTile = SLIDE_SPOTS[I][J];
						RSNPC nextSlide = getNpcInRoom(ID_SPOTS[I][J]);
						if (nextSlide != null && clickTile(slideTile(nextSlide), random(0.2, 0.8), random(0.2, 

0.8), "Move")) {
							waitToSlide(nextSlide);
						}
						break o;
					}
				}
			}
			sleep(100, 200);
		}
		return 1;
	}

	private int puzzleSlidingStatues() {
		final int[] RUG_CORNERS = { 50762, 51313, 51861, 54889, 56441 };
		final int[] RUG_EDGES = { 50765, 51316, 51864, 54892, 56443, 56444 };
		if (getObjInRoom(RUG_EDGES) == null)
			waitForResponse();
		int hX = 0, hY = 0, failCount = 0;
		for (int I = 0; I < SLIDING_STATUES.length; I++) {
			RSNPC push = getNpcInRoom(SLIDING_STATUES[I]);
			RSNPC heading = getNpcInRoom(HEADING_STATUES[I]);
			if (push != null && heading != null) {
				RSTile hCorner = getNearestSWObjTo(heading.getLocation(), RUG_CORNERS);
				RSTile pCorner = getNearestSWObjTo(push.getLocation(), RUG_CORNERS);
				if (hCorner == null || pCorner == null)
					return -1;
				int xDist = hCorner.getX() - pCorner.getX();
				int yDist = hCorner.getY() - pCorner.getY();
				if (Math.abs(xDist) > Math.abs(yDist)) {
					hX = xDist > 0 ? -7 : 7;
				} else if (Math.abs(xDist) < Math.abs(yDist)) {
					hY = yDist > 0 ? -7 : 7;
				} else return -1;
				break;
			}
		}
		o:while (!puzzleSolved) {
			if (failSafe())
				return 2;
			int aligned = 0;
			if (failCount > 5) {
				failReason = "No good push tiles remain";
				return -1;
			}
			for (int I = 0; I < SLIDING_STATUES.length; I++) {
				RSNPC statue = npcs.getNearest(SLIDING_STATUES[I]);
				RSNPC heading = npcs.getNearest(HEADING_STATUES[I]);
				if (statue != null && heading != null) {
					RSTile hT = heading.getLocation(), pT = statue.getLocation();
					safeTile = new RSTile(hT.getX() + hX, hT.getY() + hY);
					if (pT.equals(safeTile)) {
						if (aligned == 3)
							break;
						aligned++;
						continue;
					}
					while ((statue = npcs.getNearest(SLIDING_STATUES[I])) != null && !(pT = statue.getLocation

()).equals(safeTile)) {
						if (failSafe())
							return 2;
						if (puzzleSolved)
							break o;
						boolean xPush = false, xPull = false, yPush = false, yPull = false;
						int pX = pT.getX(), pY = pT.getY();
						Point m = alignment(pT, safeTile);
						RSTile pullX = null, pushX = null, pullY = null, pushY = null;
						if (m.x != 0) {
							pushX = new RSTile(pX - m.x, pY);
							pullX = new RSTile(pX + m.x, pY);
							xPush = getNpcAt(pushX) == null && getNpcAt(pullX) == null;
							xPull = getNpcAt(pullX) == null && getNpcAt(new RSTile(pX + 2 * m.x, pY)) == 

null;
						}
						if (m.y != 0) {
							pullY = new RSTile(pX, pY + m.y);
							pushY = new RSTile(pX, pY - m.y);
							yPush = getNpcAt(pushY) == null && getNpcAt(pullY) == null;
							yPull = getNpcAt(pullY) == null && getNpcAt(new RSTile(pX, pY + 2 * m.y)) == 

null;
						}
						boolean readyY = (yPush && myLoc().equals(pushY)) || (yPull && myLoc().equals(pullY));
						if ((xPush || xPull) && !readyY) {
							double pullDist = xPull ? calc.distanceBetween(myLoc(), pullX) : 100;
							double pushDist = xPush ? calc.distanceBetween(myLoc(), pushX) : 100;
							RSTile move = pullDist < pushDist ? pullX : pushX;
							String action = pullDist < pushDist ? "Pull" : "Push";
							if (walkBlockedTile(move, 0) && clickNpc(statue, action) && waitToSlide

(statue))
								failCount = 0;
						} else if (yPush || yPull) {
							double pullDist = yPull ? calc.distanceBetween(myLoc(), pullY) : 100;
							double pushDist = yPush ? calc.distanceBetween(myLoc(), pushY) : 100;
							RSTile move = pullDist < pushDist ? pullY : pushY;
							String action = pullDist < pushDist ? "Pull" : "Push";
							if (walkBlockedTile(move, 0) && clickNpc(statue, action) && waitToSlide

(statue))
								failCount = 0;
						} else {
							failCount++;
							if (failCount > 3) {
								waitForResponse();
								RSTile s1 = m.x != 0 ? new RSTile(pX, pY + m.x) : new RSTile(pX + m.y, 

pY);
								RSObject rug = objects.getTopAt(s1);
								if (rug == null || intMatch(rug.getID(), RUG_EDGES))
									s1 = m.x != 0 ? new RSTile(pX, pY - m.x) : new RSTile(pX - 

m.y, pY);
									RSTile s2 = m.x != 0 ? new RSTile(s1.getX() - m.x, s1.getY()) 

: new RSTile(s1.getX(), s1.getY() - m.y);
									puzzlePoints.add(s1);
									puzzlePoints.add(s2);
									if (walkBlockedTile(s1, 0) && clickNpc(statue, "Pull") && 

waitToSlide(statue)) {
										if (walkBlockedTile(s2, 0) && clickNpc(statue, 

"Push"))
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
		return !pickUpAll() ? 2 : 1;
	}

	private int puzzleStrangeFlowers() {
		if (!memberCheck())
			return -1;
		final int[] C_FLOWERS = { 35507, 35523, 35562, 35569 };
		final String[] F_COLORS = { "blue", "purple", "red", "yellow" };
		RSObject centerFlower;
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			boolean toWait = false;
			nearMonster = npcs.getNearest(monster);
			if (nearMonster != null && calc.distanceTo(nearMonster) < 5 && reachable(nearMonster.getLocation(), false)) {
				if (!fightMonsters())
					return 2;
			} else if ((centerFlower = getObjInRoom(CENTER_FLOWERS)) != null) {
				if (reachableObj(centerFlower)) {
					toWait = doObjAction(centerFlower, "Uproot") && waitToAnimate();
				} else {
					int fID = centerFlower.getID();
					for (int I = 0; I < C_FLOWERS.length; I++) {
						if (fID == C_FLOWERS[I]) {
							RSObject flower = getReachableObjNear(centerFlower, "plant");
							if (flower != null) {
								if (getName(flower).equals("Strange " + F_COLORS[I] + " plant")) {
									if (doObjAction(flower, "Chop Strange " + F_COLORS[I])) {
										waitToStop(true);
										toWait = true;
									}
								} else if (calc.distanceTo(flower) > 1 && !isMoving()) {
									walkTo(flower.getLocation(), 1);
								} else topUp(false);
							} else topUp(false);
							break;
						}
					}
				}
			}
			smartSleep(toWait, true);
		}
		return 1;
	}

	private int puzzleSuspiciousGrooves() {
		final int[][] FORWARD_ROWS = { ROW_1, ROW_2, ROW_3 };
		RSTile finalRow = getObjLocation(ROW_3);
		RSTile backDoor = getBackDoor();
		if (finalRow == null || backDoor == null)
			return -1;
		RSTile moveTile = reflect(backDoor, finalRow, 2);
		for (int[] grooves : FORWARD_ROWS) {
			if (failSafe())
				return 2;
			RSObject nextTry = getObjInRoom(grooves);
			if (nextTry == null)
				return -1;
			safeTile = nextTry.getLocation();
			int nextId = nextTry.getID();
			while (calc.distanceTo(backDoor) <= calc.distanceBetween(safeTile, backDoor)) {
				if (failSafe())
					return 2;
				eatFood(foods, 40, 50);
				RSObject test = objects.getTopAt(safeTile);
				if (test != null && test.getID() != nextId) {
					nextTry = getObjInRoom(grooves);
					if (nextTry != null)
						safeTile = nextTry.getLocation();
				}
				if (myLoc().equals(safeTile)) {
					walkTo(moveTile, 2);
					waitToEat(false);
				} else if (player().getAnimation() != 1114 && !isMoving()) {
					boolean failed = false;
					if (doObjAction(nextTry, "Step-onto")) {
						while (isMoving()) {
							if (player().getAnimation() == 1114)
								failed = true;
							sleep(300, 400);
						}
						for (int i = 0; i < 10 && !failed; i++) {
							if (player().getAnimation() == 1114)
								failed = true;
							sleep(150, 200);
						}
						while (isMoving() || player().getAnimation() == 1114)
							sleep(200, 400);
						test = objects.getTopAt(safeTile);
						if (test != null) {
							if (failed || test.getID() != nextId) {
								nextTry = getObjInRoom(grooves);
								if (nextTry != null)
									safeTile = nextTry.getLocation();
							} else if (walkTo(moveTile, 2)) {
								waitToEat(false);
							}
						}
					}
				}
				sleep(100, 200);
			}
		}
		puzzleRepeat = true;
		return 1;
	}

	private int puzzleUnhappyGhost() {
		if (!memberCheck() || !training(Skills.PRAYER))
			return -1;
		final String[] ACTIONS = { "Unlock", "Bless-remains", "Repair", "Repair", "Fill", "Take" };
		while (getNpcInRoom(UNHAPPY_GHOST) != null) {
			if (failSafe())
				return 2;
			if (!requirements())
				return -1;
			RSObject unopenedCoffin = getObjInRoom(40181, 54571, 54593, 55465);
			RSObject unblessedCoffin = getObjInRoom(54572, 54594, 55451, 55466);
			RSObject damagedPillar = getObjInRoom(54580, 54602, 55457, 55472);
			RSObject brokenPot = getObjInRoom(54577, 54599, 55455, 55470);
			RSObject jewelleryBox = getObjInRoom(54576, 54598, 55453, 55468);
			RSGroundItem ring = getItemInRoom(targetRoom, 19879);
			smartAction(ACTIONS, unopenedCoffin, unblessedCoffin, damagedPillar, brokenPot, ring == null ? jewelleryBox : 

null, ring);
		}
		return 1;
	}

	private int puzzleWarpedPortals() {
		waitForResponse();
		int resets = 0;
		RSObject portal;
		RSTile goal = targetRoom.getCentralTile(), backDoor = getBackDoor();
		dropItem(GGS);
		while (!puzzleSolved) {
			if (failSafe())
				return 2;
			if (reachable(backDoor, true))
				omNomNom();
			if (reachable(goal, false)) {
				walkAdjacentTo(myLoc(), 1);
			} else {
				if ((portal = portalObject()) == null) {
					if (resets > 2) {
						log(RED, "No reachable portal found");
						if (!teleHomeAndBack())
							unBacktrackable.add(targetRoom);
						return -1;
					}
					resets++;
					puzzlePoints.clear();
					badTiles.clear();
					continue;
				}
				safeTile = portalAdjacent(portal);
				puzzlePoints.add(safeTile);
				while (reachable(safeTile, false)) {
					if (failSafe())
						return 2;
					smartSleep(doObjAction(portal, "Enter"), true);
				}
				badTiles.add(safeTile);
				sleep(400, 600);
				safeTile = portalAdjacent(getReachableObj(WARPED_PORTAL));
				if (safeTile != null) {
					puzzlePoints.add(safeTile);
					badTiles.add(safeTile);
				}
			}
			sleep(100, 500);
		}
		idleTimer.reset();
		puzzleTimer.reset();
		return !teleHomeAndBack() ? 2 : 1;
	}

	public static class Door {
		public enum Type {
			BASIC, LOCKED, BLOCKED, SKILL;
			final int type;
			Type() {
				this.type = this.ordinal();
			}
		}

		public enum State {
			BLOCKED, FINISHED, OPENED, OPENABLE;
			final int state;
			State() {
				this.state = this.ordinal();
			}
		}

		final boolean blocked;
		final int room;
		final RSTile check;
		final RSTile loc;
		final RSTile lock;
		final Type type;

		public Door(final Type type, final RSTile door, final RSTile lock, final int room) {
			this.loc = door;
			this.lock = lock;
			this.type = type;
			this.blocked = type == Type.BLOCKED || type == Type.LOCKED;
			this.check = blocked ? lock : loc;
			this.room = room;
		}
	}

	public static class Room {
		public enum Type {
			NORMAL(0), START(1), BOSS(2), PUZZLE(3);
			final int type;
			Type(final int type) {
				this.type = type;
			}
		}

		final RSArea area;
		final Type type;
		boolean finished;

		public Room(final Type type, final int aX, final int aY) {
			this.area = new RSArea(aX, aY, 16, 16);
			this.type = type;
			this.finished = false;
		}
	}

	// Settings loader by ShadowMoose
	/*private boolean loadSettings() { //handles prayer options
		try {
			File settingsFile = new File(userSettings);
			if (!settingsFile.exists())
				return false;
			Properties prop = new Properties();
			prop.load(new FileInputStream(userSettings));
			if (prop != null && prop.getProperty("06") != null && prop.getProperty("06").equals("true")) {
				for (int I = 0; I < options.length; I++) {
					for (int J = 0; J < options[I].length; J++) {
						options[I][J].set(prop.getProperty("" + I + J).equals("true"));
					}
				}
				return true;
			}
		} catch (Exception e) {
			log(RED, "Unable to load settings");
		}
		return false;
	}*/

	// Settings saver by ShadowMoose
	private void saveSettings() {
		if (Option.SAVE_SETTINGS.enabled()) {
			try {
				BufferedWriter out = new BufferedWriter(new FileWriter(userSettings));
				Option.SAVE_SETTINGS.set(false);
				for (int I = 0; I < options.length; I++) {
					for (int J = 0; J < options[I].length; J++) {
						out.write("" + I + J + "=" + options[I][J].enabled());
						out.newLine();
					}
				}
				out.close();
				log(PRP, "Settings saved!");
			} catch (Exception e) {
				log(RED, "Unable to save your settings");
			}
		}
	}

	// Stats updater by ShadowMoose
	private boolean updateSiggy(final int time, final int xp, final int tokens, final int prestiged) {
		URLConnection url = null;
		BufferedReader in = null;
		String site = "http://shadowscripting.org/iDungeon/changeStats.php?name=" + username + "&pass=" + password + 

"&tokens=" + tokens + "&xp=" + xp + "&time=" + time + "&prestige=" + prestiged;
		try {
			url = new URL(site).openConnection();
			in = new BufferedReader(new InputStreamReader(url.getInputStream()));
			in.ready();
		} catch (IOException e) {
			log(RED, "Stat update failed :'(");
			return false;
		}
		return true;
	}

	private final Filter<RSNPC> guardians = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			if (npc != null && areaContains(targetRoom, npc.getLocation())) {
				if (deadNpc(npc))
					return false;
				if (npc.isInCombat())
					return true;
				if (intMatch(npc.getID(), FRIENDLIES))
					return false;
				return stringMatch("Attack", npc.getActions());
			}
			return false;
		}
	};

	private final Filter<RSNPC> primaryMonster = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			if (npc != null && areaContains(targetRoom, npc.getLocation())) {
				int npcID = npc.getID();
				if (deadNpc(npc))
					return false;
				if (!slayerRequirement(npcID))
					return false;
				String npcName = npc.getName();
				if (spawnRoom && stringMatch(npcName, "Zombie", "Reborn warrior"))
					return false;
				if (npc.isInCombat())
					return true;
				if (intMatch(npcID, FRIENDLIES))
					return false;
				if (shadowHooded && hoodMonster(npc))
					return false;
				if (npcName.startsWith("Ramokee") || npcName.equals(strongestMonster))
					return false;
				return stringMatch(npcName, PRIORITY_MONSTERS);
			}
			return false;
		}
	};

	private final Filter<RSNPC> secondaryMonster = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			if (npc != null && areaContains(targetRoom, npc.getLocation())) {
				int npcID = npc.getID();
				if (deadNpc(npc))
					return false;
				if (!slayerRequirement(npcID))
					return false;
				String npcName = npc.getName();
				if (spawnRoom && stringMatch(npcName, "Zombie", "Reborn warrior"))
					return false;
				if (npc.isInCombat())
					return true;
				if (intMatch(npcID, FRIENDLIES))
					return false;
				if (shadowHooded && hoodMonster(npc))
					return false;
				if (npcName.startsWith("Ramokee") || npcName.equals(strongestMonster))
					return false;
				return stringMatch(npcName, SECONDARY_MONSTERS) || getWeakness(npc) != Melee.NONE;
			}
			return false;
		}
	};

	private final Filter<RSNPC> monster = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			if (npc != null && areaContains(targetRoom, npc.getLocation())) {
				int npcID = npc.getID();
				if (deadNpc(npc))
					return false;
				if (!slayerRequirement(npcID))
					return false;
				if (npc.isInCombat())
					return true;
				if (intMatch(npcID, FRIENDLIES))
					return false;
				String npcName = npc.getName();
				if (npcName.startsWith("Ramokee"))
					return false;
				if (roomHooded && hoodMonster(npc))
					return false;
				return stringMatch("Attack", npc.getActions());
			}
			return false;
		}
	};

	private final Filter<RSNPC> bossMonster = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			return npc != null && npc.getAnimation() != 5491 && (bossRoom == null || areaContains(bossRoom, 

npc.getLocation())) && stringMatch("Attack", npc.getActions());
		}
	};

	private final Filter<RSNPC> dying = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			return npc != null && deadNpc(npc);
		}
	};

	private final Filter<RSGroundItem> itemFilter = new Filter<RSGroundItem>() {
		public boolean accept(RSGroundItem gItem) {
			if (gItem == null)
				return false;
			int itemID = gItem.getItem().getID();
			if (itemID < 1)
				return false;
			for (int[] color : KEYS) {
				if (intMatch(itemID, color))
					return true;
			}
			if (badTiles.contains(gItem.getLocation()))
				return false;
			if (rooms.size() == 1 && aComplexity > 4)
				return true;

			if (Option.ARROWS.enabled() && itemID == arrows && gItem.getItem().getStackSize() > 1 && reachable

(gItem.getLocation(), true))
				return true;
			if (!isRushing || Option.RUSH_FOOD.enabled()) {
				if (Option.BONES.enabled() && intMatch(itemID, BONES))
					return true;
				if (intMatch(itemID, foods))
					return combat.getHealth() != 100 || inventory.getCount() < 27 || itemID > foodLowest();
					if (!isRushing && Option.MAKE_FOOD.enabled() && itemID == COINS)
						return true;
			}
			boolean contained = inventory.contains(itemID);
			if (!contained && (bounds.contains((Integer) itemID) || logs != null && itemID == logs.getID()))
				return true;
			String itemName = gItem.getItem().getName();
			if (itemName == null)
				return false;
			if (!pickupName.isEmpty() && typeTo != 0 && itemName.equalsIgnoreCase(pickupName))
				return true;
			return !contained && stringMatch(itemName, improvements);
		}
	};

	private final Filter<RSGroundItem> keyItem = new Filter<RSGroundItem>() {
		public boolean accept(RSGroundItem item) {
			if (item != null) {
				int itemId = item.getItem().getID();
				for (int[] color : KEYS) {
					if (intMatch(itemId, color))
						return true;
				}
			}
			return false;
		}
	};

	private final Filter<RSObject> roomObjects = new Filter<RSObject>() {
		public boolean accept(RSObject obj) {
			if (obj == null)
				return false;
			RSTile noT = obj.getLocation();
			for (RSArea room : rooms) {
				if (areaContains(room, noT))
					return false;
			}
			return true;
		}
	};

	private final Filter<RSObject> bookCorner = new Filter<RSObject>() {
		public boolean accept(RSObject obj) {
			if (obj == null || obj.getID() != 52132)
				return false;
			return (calc.distanceTo(obj) > 4 && calc.distanceTo(obj) < 10);
		}
	};

	private final Filter<RSGroundItem> stompCrystal = new Filter<RSGroundItem>() {
		public boolean accept(RSGroundItem item) {
			return item != null && intMatch(item.getItem().getID(), 15750, 15751, 15752) && !badTiles.contains

(item.getLocation());
		}
	};

	private final Filter<RSNPC> unInteracting = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			if (npc == null || !npc.getName().contains("mage") || !areaContains(targetRoom, npc.getLocation()))
				return false;
			return npc.getInteracting() == null || !npc.getInteracting().equals(player());
		}
	};

	private final Filter<RSNPC> skater = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			return npc != null && areaContains(targetRoom, npc.getLocation()) && npc.getAnimation() != -1;
		}
	};

	private final Filter<RSNPC> monolithShade = new Filter<RSNPC>() {
		public boolean accept(RSNPC npc) {
			if (npc == null || !areaContains(targetRoom, npc.getLocation()))
				return false;
			if (npc.getInteracting() != null && npc.getInteracting().equals(player()))
				return false;
			return !npc.isInCombat() && stringMatch("Attack", npc.getActions());
		}
	};

	public enum Floor {
		OUTSIDE (0),
		FROZEN (145),
		UNFROZEN (0),
		ABANDONED (209),
		FURNISHED (273),
		OCCULT (3676),
		WARPED (5467),
		ALL(0);

		private final int differential;
		private final String name;

		Floor(final int differential) {
			String n = this.name();
			this.differential = differential;
			this.name = n.substring(0, 1) + n.substring(1).toLowerCase();
		}

		public int diff() {
			return differential;
		}

		public String getName() {
			return name;
		}
	}

	public enum Food {
		HEIM_CRAB (20, 200), RED0EYE (50, 550), DUSK_EEL (70, 840),
		GIANT_FLATFISH (100, 1400), SHORT0FINNED_EEL (120, 1920),
		WEB_SNIPPER (150, 2700), BOULDABASS (170, 3400), SALVE_EEL (200, 4500),
		BLUE_CRAB (220, 5500), CAVE_MORAY (250, 7500);

		private final int cost;
		private final int foodID;
		private final int health;
		private final int rawID;
		private final int tier;
		private final int cookLvl;
		private final String name;
		private final String capital;

		Food(final int health, final int cost) {
			this.tier = this.ordinal() + 1;
			this.health = health;
			this.cost = cost;
			this.foodID = 18157 + 2 * tier;
			this.rawID = foodID - 362;
			this.cookLvl = tier == 1 ? 1 : tier == 10 ? 99 : (tier - 1) * 10;
			this.capital = enumName(this.name());
			this.name = capital.toLowerCase();
		}

		public int cookLevel() {
			return cookLvl;
		}
		public int getID() {
			return foodID;
		}
		public String getName(final boolean capitalized) {
			return capitalized ? capital : name;
		}
		public int rawID() {
			return rawID;
		}
		public int price(final int toBuy) {
			return cost * toBuy;
		}
		public int recovery() {
			return health;
		}
		public int tier() {
			return tier;
		}
	}

	public enum Logs {
		TANGLE_GUM (225), SEEPING_ELM (600), BLOOD_SPINDLE (1650), UTUKU (3750),
		SPINEBEAM (7875), BOVISTRANGLER (12500), THIGAT (22500),
		CORPSETHORN (35000), ENTGALLOW (60000), GRAVE_CREEPER (117500);

		private final int cost;
		private final int logID;
		private final int fireID;
		private final int tier;
		private final int fireLvl;
		private final String name;
		private final String capital;

		Logs(final int cost) {
			this.tier = this.ordinal() + 1;
			this.cost = cost;
			this.logID = 17680 + 2 * tier;
			this.fireID = 49939 + tier;
			this.fireLvl = tier == 1 ? 1 : tier == 10 ? 99 : (tier - 1) * 10;
			this.capital = enumName(this.name());
			this.name = capital.toLowerCase();
		}

		public int fireLevel() {
			return fireLvl;
		}
		public int fireID() {
			return fireID;
		}
		public int getID() {
			return logID;
		}
		public String getName(final boolean capitalized) {
			return capitalized ? capital : name;
		}
		public int price() {
			return cost;
		}
		public int tier() {
			return tier;
		}
	}

	public enum Monster {
		// All floors
		FORGOTTEN_MAGE (Floor.ALL, Range.RAPID, Melee.SLASH, Melee.STAB, Style.MAGIC),
		FORGOTTEN_RANGER (Floor.ALL, Melee.SLASH, Melee.STAB, Melee.NONE, Style.RANGE),
		FORGOTTEN_WARRIOR (Floor.ALL, Magic.ALL, Melee.SLASH, Melee.NONE, Style.MELEE),
		FORGOTTEN_WARRIOR0CHAIN (Floor.ALL, Magic.ALL, Melee.STAB, Melee.SLASH, Style.MELEE),
		FORGOTTEN_WARRIOR0PLATE (Floor.ALL, Magic.ALL, Melee.CRUSH, Melee.SLASH, Style.MELEE),
		GIANT_RAT(Floor.ALL, Melee.STAB, Range.RAPID, Melee.SLASH, Style.MELEE),
		MYSTERIOUS_SHADE0RANGE (Floor.ALL, Melee.SLASH, Magic.ALL, Melee.NONE, Style.RANGE),
		MYSTERIOUS_SHADE0MAGIC (Floor.ALL, Melee.SLASH, Magic.ALL, Melee.NONE, Style.MAGIC),

		// Puzzles
		GHOST (Floor.ALL, Melee.CRUSH, Melee.STAB, Range.RAPID, Style.MELEE),
		MERCENARY_LEADER (Floor.ALL, Range.RAPID, Melee.SLASH, Melee.STAB, Style.MAGIC),
		RAMOKEE_BLOODRAGER (Floor.ALL, Magic.ALL, Melee.STAB, Melee.SLASH, Style.SUMMON),
		RAMOKEE_DEATHSLINGER (Floor.ALL, Melee.STAB, Melee.SLASH, Range.RAPID, Style.SUMMON),
		RAMOKEE_STORMBRINGER (Floor.ALL, Range.RAPID, Melee.SLASH, Melee.STAB, Style.SUMMON),
		RAMOKEE_SKINWEAVER (Floor.ALL, Melee.STAB, Melee.SLASH, Range.RAPID, Style.SUMMON),

		// All except Frozen
		BAT (Floor.UNFROZEN, Range.RAPID, Melee.STAB, Melee.NONE, Style.MELEE),
		DUNGEON_SPIDER (Floor.UNFROZEN, Melee.CRUSH, Melee.SLASH, Magic.FIRE, Style.MELEE),
		GIANT_BAT (Floor.UNFROZEN, Range.RAPID, Melee.STAB, Melee.NONE, Style.MELEE),
		SKELETON0MELEE (Floor.UNFROZEN, Magic.ALL, Melee.CRUSH, Melee.SLASH, Style.MELEE),
		SKELETON0RANGE (Floor.UNFROZEN, Melee.CRUSH, Melee.SLASH, Magic.ALL, Style.RANGE),
		SKELETON0MAGIC (Floor.UNFROZEN, Range.RAPID, Melee.CRUSH, Melee.STAB, Style.MAGIC),
		ZOMBIE0MELEE(Floor.UNFROZEN, Melee.SLASH, Magic.FIRE, Melee.STAB, Style.MELEE),
		ZOMBIE0RANGE(Floor.UNFROZEN, Melee.SLASH, Magic.FIRE, Melee.STAB, Style.RANGE),

		// Slayer
		CRAWLING_HAND (Floor.UNFROZEN, Melee.CRUSH, Melee.SLASH, Magic.FIRE, Style.MELEE),
		CAVE_CRAWLER (Floor.UNFROZEN, Melee.CRUSH, Melee.SLASH, Melee.NONE, Style.MELEE),
		CAVE_SLIME (Floor.UNFROZEN, Magic.ALL, Melee.CRUSH, Melee.SLASH, Style.MELEE),
		PYREFIEND (Floor.UNFROZEN, Magic.WATER, Melee.STAB, Melee.SLASH, Style.MELEE),
		NIGHT_SPIDER (Floor.UNFROZEN, Melee.CRUSH, Magic.FIRE, Melee.SLASH, Style.MELEE),
		JELLY (Floor.UNFROZEN, Melee.SLASH, Melee.CRUSH, Magic.ALL, Style.MELEE),
		SPIRITUAL_GUARDIAN (Floor.UNFROZEN, Magic.ALL, Melee.CRUSH, Melee.NONE, Style.MELEE),
		SEEKER (Floor.UNFROZEN, Range.RAPID, Melee.STAB, Melee.CRUSH, Style.MAGIC),
		NECHRYAEL (Floor.UNFROZEN, Magic.ALL, Melee.SLASH, Range.RAPID, Style.MELEE),
		EDIMMU (Floor.UNFROZEN, Range.RAPID, Melee.STAB, Melee.SLASH, Style.MAGIC),
		SOULGAZER (Floor.UNFROZEN, Range.RAPID, Melee.STAB, Melee.CRUSH, Style.MAGIC),

		// Frozen
		FROST_DRAGON (Floor.FROZEN, Melee.STAB, Range.RAPID, Magic.FIRE, Style.MAGIC),
		HYDRA (Floor.FROZEN, Melee.SLASH, Melee.STAB, Range.RAPID, Style.RANGE),
		ICEFIEND (Floor.FROZEN, Melee.STAB, Melee.CRUSH, Range.RAPID, Style.MAGIC),
		ICE_ELEMENTAL (Floor.FROZEN, Melee.CRUSH, Melee.STAB, Magic.FIRE, null),
		ICE_GIANT (Floor.FROZEN, Magic.FIRE, Melee.CRUSH, Range.RAPID, Style.MELEE),
		ICE_SPIDER (Floor.FROZEN, Magic.FIRE, Melee.CRUSH, Melee.SLASH, Style.MELEE),
		ICE_TROLL (Floor.FROZEN, Magic.FIRE, Melee.CRUSH, Melee.SLASH, Style.MELEE),
		ICE_WARRIOR (Floor.FROZEN, Magic.FIRE, Melee.CRUSH, Melee.STAB, Style.MELEE),
		THROWER_TROLL (Floor.FROZEN, Melee.STAB, Melee.SLASH, Range.RAPID, Style.RANGE),

		// Abandoned
		EARTH_WARRIOR (Floor.ABANDONED, Magic.WIND, Melee.CRUSH, Melee.SLASH, Style.MELEE),
		GIANT_SKELETON (Floor.ABANDONED, Magic.FIRE, Melee.CRUSH, Melee.SLASH, Style.MELEE),
		GREEN_DRAGON (Floor.ABANDONED, Melee.STAB, Range.RAPID, Magic.FIRE, Style.MAGIC),
		HILL_GIANT (Floor.ABANDONED, Melee.STAB, Melee.SLASH, Range.RAPID, Style.MELEE),
		HOBGOBLIN (Floor.ABANDONED, Melee.STAB, Magic.WATER, Melee.SLASH, Style.MELEE),
		ANIMATED_PICKAXE (Floor.ABANDONED, Magic.ALL, Melee.SLASH, Melee.CRUSH, Style.MELEE),

		// Furnished
		BRUTE (Floor.FURNISHED, Magic.ALL, Melee.STAB, Melee.CRUSH, Style.MELEE),
		GUARD_DOG (Floor.FURNISHED, Melee.STAB, Range.RAPID, Melee.SLASH, Style.MELEE),
		IRON_DRAGON (Floor.FURNISHED, Melee.STAB, Range.RAPID, Magic.FIRE, Style.MAGIC),

		// Occult
		ANIMATED_BOOK (Floor.OCCULT, Range.RAPID, Melee.SLASH, Melee.NONE, Style.MAGIC),
		BLACK_DEMON (Floor.OCCULT, Magic.ALL, Range.RAPID, Melee.STAB, Style.MAGIC),
		FIRE_GIANT (Floor.OCCULT, Melee.STAB, Magic.WATER, Melee.CRUSH, Style.MELEE),
		GREATER_DEMON (Floor.OCCULT, Magic.ALL, Range.RAPID, Melee.STAB, Style.MAGIC),
		HELLHOUND (Floor.OCCULT, Melee.STAB, Range.RAPID, Melee.SLASH, Style.MELEE),
		LESSER_DEMON (Floor.OCCULT, Magic.ALL, Range.RAPID, Melee.STAB, Style.MAGIC),
		NECROMANCER (Floor.OCCULT, Range.RAPID, Melee.SLASH, Melee.STAB, Style.MAGIC),
		RED_DRAGON (Floor.OCCULT, Melee.STAB, Range.RAPID, Magic.FIRE, Style.MAGIC),

		// Warped
		ANKOU (Floor.WARPED, Magic.ALL, Melee.STAB, Melee.CRUSH, Style.MELEE),
		BLACK_DRAGON (Floor.WARPED, Melee.STAB, Range.RAPID, Magic.FIRE, Style.MAGIC),
		REBORN_MAGE (Floor.WARPED, Range.RAPID, Magic.WATER, Melee.CRUSH, Style.MAGIC),
		REBORN_WARRIOR (Floor.WARPED, Magic.ALL, Melee.CRUSH, Melee.SLASH, Style.MELEE);

		private final Style combatStyle;
		private final Floor monsterFloor;
		private final Combat[] weaknesses;

		Monster(final Floor floor, final Combat w1, final Combat w2, final Combat w3, final Style style) {
			this.monsterFloor = floor;
			this.weaknesses = new Combat[] { w1, w2, w3 };
			this.combatStyle = style;
		}

		public boolean floorContains() {
			if (monsterFloor == Floor.WARPED)
				return monsterFloor.compareTo(Floor.ABANDONED) == 1;
			return monsterFloor.compareTo(floor) == (monsterFloor == Floor.UNFROZEN || monsterFloor == Floor.ALL ? 1 : 0);
		}

		public Combat getWeakness() {
			for (Combat w : weaknesses) {
				if (w.enabled())
					return w;
			}
			return Melee.NONE;
		}

		public Combat[] getWeaknesses() {
			return weaknesses;
		}

		public boolean hasWeakness(final Combat weakness) {
			for (Combat w : weaknesses) {
				if (w == weakness)
					return true;
			}
			return false;
		}

		public Style combatStyle() {
			return combatStyle;
		}
	}

	public enum Slayer {
		CRAWLING_HAND (5), CAVE_CRAWLER (10), CAVE_SLIME (17), PYREFIEND (30),
		NIGHT_SPIDER (41), JELLY (52), SPIRITUAL_GUARDIAN (63), SEEKER (71),
		NECHRYAEL (80), EDIMMU (90), SOULGAZER (99);

		private final int id;
		private final int level;

		Slayer(final int level) {
			this.id = SLAYER_MONSTERS[this.ordinal()];
			this.level = level;
		}

		public int getID() {
			return id;
		}

		public boolean isSlayable() {
			return level <= slayerLevel;
		}
	}

	public enum Spell {
		TELE_HOME (24, 0), TELE_GATESTONE (39, 32), TELE_GROUPSTONE (40, 64),

		MAKE_GATESTONE (38, 32), HIGH_ALCHEMY (46, 55),

		WIND_BOLT (32, 17), WATER_BOLT (36, 23), EARTH_BOLT (37, 29), FIRE_BOLT (41, 35),
		WIND_BLAST (42, 41), WATER_BLAST (43, 47), EARTH_BLAST (45, 53), FIRE_BLAST (47, 59),
		WIND_WAVE (48, 62), WATER_WAVE (49, 65), EARTH_WAVE (54, 70), FIRE_WAVE (58, 75),
		WIND_SURGE (61, 81), WATER_SURGE (62, 85), EARTH_SURGE (63, 90), FIRE_SURGE (67, 95);

		final int index, level;
		final Magic type;
		final String name;

		Spell(final int index, final int level) {
			String n = this.name().replace("_", " ");
			Magic t = enumOf(Magic.class, n.substring(0, n.indexOf(" ")));
			this.index = index;
			this.type = t != null ? t : Magic.NONE;
			this.level = level;
			this.name = n.substring(0, 1) + n.substring(1).toLowerCase();
		}

		public String getName() {
			return name;
		}
		public int index() {
			return index;
		}
		public int level() {
			return level;
		}
		public Magic type() {
			return type;
		}
	}

	public enum Option {
		MELEE ("Melee", true, 33, 390),
		RANGE ("Range", false, 113, 390),
		MAGIC ("Magic", false, 193, 390),
		SECONDARY_MELEE ("Melee", false, 33, 430),
		SECONDARY_RANGE ("Range", false, 113, 430),
		SECONDARY_MAGIC ("Magic", false, 193, 430),
		LOAD_SETTINGS ("Load Settings at startup", true, 315, 390),
		SAVE_SETTINGS ("Save them for next time", false, 315, 413),

		BONES ("Bones", false, 155, 371),
		ARROWS ("Arrows", false, 260, 371),
		CRABS ("Heim Crabs", false, 365, 371),
		PRAY ("Prayers", false, 155, 393),
		QUICK_PRAY ("Quick Pray", false, 260, 393),
		PRAY_DOORS ("Pray Doors", true, 365, 393),
		PURE ("Pure Mode", false, 155, 415),
		STYLE_SWAP ("Style Swap", false, 260, 415),
		SUMMON ("Summoning", true, 365, 415),
		MAKE_FOOD ("Food Prep", true, 155, 437),
		EXPLORER ("Explorer", true, 260, 437),
		LOGOUT ("Logout", false, 365, 437),

		PRESTIGE ("Prestige", true, 180, 372),
		MEDIUM ("Medium", false, 180, 394),
		DEBUG ("Debug", true, 180, 416),
		VIDEO ("Recording", false, 180, 438),
		RUSH ("Enabled", false, 373, 371),
		RUSH_FOOD ("Grab Food", true, 420, 392);

		final Point p;
		final Rectangle button;
		final String name;
		boolean enabled;

		Option(final String name, final boolean enabled, final int x, final int y) {
			this.button = new Rectangle(x, y, 12, 12);
			this.enabled = enabled;
			this.name = name;
			this.p = new Point(x + 17, y + 12);
		}

		public Rectangle button() {
			return button;
		}
		public boolean contains(final Point p) {
			return button.contains(p);
		}
		public Color color() {
			return button.contains(mp) ? (enabled ? GN2 : RD2) : (enabled ? GN1 : RD1);
		}

		public boolean enabled() {
			return enabled;
		}

		public String getName() {
			return name;
		}

		public Point p() {
			return p;
		}

		public boolean toggle() {
			return enabled = !enabled;
		}

		public void set(final boolean enable) {
			enabled = enable;
		}
	}

	public enum Style {
		MELEE (false), RANGE (false), MAGIC (false), SUMMON (false);

		private final int idx;
		private final String name;
		private boolean enabled;

		Style(final boolean enable) {
			String n = this.name();
			this.enabled = enable;
			this.idx = this.ordinal();
			this.name = n.substring(0, 1) + n.substring(1).toLowerCase();
		}

		public String getName() {
			return name;
		}

		public boolean enabled() {
			return enabled;
		}

		public int index() {
			return idx;
		}

		public void set(final boolean enabled) {
			this.enabled = enabled;
		}
	}

	public enum Weapon {
		DAGGER (Skills.ATTACK, 1, 2, -1, false, 3), RAPIER (Skills.ATTACK, 1, 2, -1, false, 3),
		LONGSWORD (Skills.ATTACK, 2, 1, -1, false, 2), SPEAR (Skills.ATTACK, 0, 1, 2, true, 3),
		MAUL (Skills.STRENGTH, -1, -1, 1, true, 2), WARHAMMER (Skills.ATTACK, -1, -1, 1, false, 2),
		BATTLEAXE (Skills.ATTACK, -1, 1, 2, false, 3), TWOH_SWORD (Skills.ATTACK, -1, 1, 2, true, 3),
		SHORTBOW (Skills.RANGE, -1, -1, -1, true, 2), LONGBOW (Skills.RANGE, -1, -1, -1, true, 2),
		STAFF (Skills.MAGIC, -1, -1, -1, false, 3);

		private final boolean twoHanded;
		private final int skill;
		private final int defensive;
		private final int stab, slash, crush;

		Weapon(final int skill, final int stab, final int slash, final int crush, final boolean twoHanded, final int defense) 

{
			this.skill = skill;
			this.stab = stab;
			this.slash = slash;
			this.crush = crush;
			this.twoHanded = twoHanded;
			this.defensive = defense;
		}

		public int skill() {
			return skill;
		}

		public int defensive() {
			return defensive;
		}

		public int crush() {
			return crush;
		}
		public int slash() {
			return slash;
		}
		public int stab() {
			return stab;
		}

		public boolean isTwoHanded() {
			return twoHanded;
		}
	}

	public interface Combat {

		public String getName();

		public boolean enabled();

		public int index();

		public void set(final int idx);

		public Style style();

	}

	public enum Melee implements Combat {
		SLASH (-1), CRUSH (-1), STAB (-1), DEFENSE (3), NONE (-1);

		private final String name;
		private final Style style;
		private int index;

		Melee(final int idx) {
			this.index = idx;
			String raw = this.name();
			this.name = raw.substring(0, 1) + raw.substring(1).toLowerCase();
			this.style = Style.MELEE;
		}

		public String getName() {
			return name;
		}

		public boolean enabled() {
			return index != -1;
		}

		public int index() {
			return index;
		}

		public void set(final int idx) {
			this.index = idx;
		}

		public Style style() {
			return style;
		}
	}

	public enum Range implements Combat {
		ACCURATE (0), RAPID (1), LONG_RANGE (2), DEFENSE (2), NONE (-1);

		private final String name;
		private final Style style;
		private int index;

		Range(final int idx) {
			this.index = idx;
			String raw = this.name();
			this.name = raw.substring(0, 1) + raw.substring(1).toLowerCase();
			this.style = Style.RANGE;
		}

		public String getName() {
			return name;
		}

		public boolean enabled() {
			return index != -1;
		}

		public int index() {
			return index;
		}

		public void set(final int idx) {
			this.index = idx;
		}

		public Style style() {
			return style;
		}
	}

	public enum Magic implements Combat {
		NONE (-1), WIND (-1), WATER (-1), EARTH (-1), FIRE (-1), TELE (-1), ALL (-1);

		private final String name;
		private final Style style;
		private int index;

		Magic(final int idx) {
			this.index = idx;
			String raw = this.name();
			this.name = raw.substring(0, 1) + raw.substring(1).toLowerCase();
			this.style = Style.MAGIC;
		}

		public String getName() {
			return name;
		}

		public boolean enabled() {
			return index != -1;
		}

		public int index() {
			return index;
		}

		public void set(final int idx) {
			this.index = idx;
		}

		public Style style() {
			return style;
		}
	}

	private boolean adjacentTo(final RSObject obj) {
		RSTile me = myLoc();
		return obj != null && calc.distanceBetween(me, obj.getArea().getNearestTile(me)) == 1;
	}

	private Point alignment(final RSTile curr, final RSTile dest) {
		int x = dest.getX() - curr.getX();
		int y = dest.getY() - curr.getY();
		if (x != 0)
			x = x > 0 ? 1 : -1;
			if (y != 0)
				y = y > 0 ? 1 : -1;
				return new Point(x, y);
	}

	private boolean areaContains(final RSArea area, final RSTile check) {
		return area != null && check != null && area.contains(check);
	}

	private boolean areaEquals(final RSArea roomA, final RSArea roomB) {
		return roomA != null && roomB != null && roomA.equals(roomB);
	}

	private boolean attackBoss(RSNPC b, final boolean screenBias) {
		if (b == null)
			b = npcs.getNearest(bossMonster);
		if (deadNpc(b))
			return false;
		if (!isAttacking(b)) {
			if (clickNpc(b, "Attack " + b.getName())) {
				for (int c = 0; c < (combatStyle == Style.MAGIC || twoHanded ? 18 : 15); c++) {
					if (failCheck())
						return false;
					if (b == null || isAttacking(b) || b.getMessage() != null)
						break;
					sleep(100, 200);
				}
				return true;
			}
			if (!b.isOnScreen()) {
				if (!isMoving()) {
					if (!screenBias) {
						walkFast(b.getLocation(), 1);
					} else if (calc.distanceTo(b) < 10) {
						walkToScreen(b.getLocation());
					} else walkToMap(b.getLocation(), 1);
					waitToStart(false);
				}
			}
			if (clickNpc(b, "Attack " + b.getName())) {
				for (int c = 0; c < (combatStyle == Style.MAGIC || twoHanded ? 18 : 15); c++) {
					if (failCheck())
						return false;
					if (b == null || isAttacking(b) || b.getMessage() != null)
						break;
					sleep(100, 200);
				}
				return true;
			}
		} else if (b.isInCombat() && (bossHp = getHpPercent(b)) == 0) {
			selectTab(Game.TAB_INVENTORY, 4);
		}
		return false;
	}

	private boolean attackNpc(final RSNPC npc) {
		if (npc == null || !npc.isValid() || isAttacking(npc))
			return false;
		if (clickNpc(npc, "Attack " + npc.getName())) {
			if (bossName.isEmpty()) {
				getBestStyle(npc);
				if (!updateFightMode())
					sleep(200, 400);
			}
			return true;
		}
		if (!npc.isOnScreen()) {
			if (!isMoving() && !isDead()) {
				walkToMap(npc.getLocation(), 1);
				waitToStart(false);
			}
			for (int i = 0; i < 10; i++) {
				if (failCheck())
					return false;
				if (npc.isOnScreen() || isAttacking(npc))
					break;
				sleep(100, 150);
			}
		}
		return isAttacking(npc) || clickNpc(npc, "Attack " + npc.getName());
	}

	private RSArea blinkPath(RSNPC blink) {
		puzzlePoints.clear();
		RSTile dest = lineOfSight(blink);
		puzzlePoints.add(dest);
		boolean safetyFirst = safeTile.getX() < dest.getX() || safeTile.getY() < dest.getY();
		RSTile curr = safetyFirst ? safeTile : dest, end = safetyFirst ? dest : safeTile;
		Point m = alignment(curr, end);
		if (m.x == 0 && m.y == 0)
			return null;
		for (int c = 0; c < 10; c++) {
			puzzlePoints.add(curr);
			curr = stepAlong(m, curr);
			if (c > 2 && (curr.equals(dest) || !isGoodTile(curr)))
				break;
		}
		return new RSArea(puzzlePoints.toArray(new RSTile[puzzlePoints.size()]));
	}

	private void bugReport(final boolean hide) {
		if (developer) {
			if (hidePaint != hide) {
				hidePaint = hide;
				sleep(400);
			}
			env.saveScreenshot(false);
			hidePaint = false;
		}
	}

	private boolean buryBones() {
		if (Option.BONES.enabled() && invContains(BONES) && (invCount(false, BONES) > random(1, 4) || invCount(false) > 22 + 

random(0, 5))) {
			while (inventory.containsOneOf(BONES)) {
				if (failCheck())
					return false;
				for (RSItem bone : inventory.getItems(BONES)) {
					if (doItemAction(bone, "Bury"))
						sleep(400, 800);
				}
			}
			return true;
		}
		return false;
	}

	private boolean castable(final Spell spell) {
		if (skills.getCurrentLevel(Skills.MAGIC) < spell.level())
			return false;
		switch (spell) {
		case MAKE_GATESTONE:
			return inventory.contains(GATESTONE) || inventory.getCount(true, COSMIC_RUNES) > 2;
		case TELE_GATESTONE:
			return gateRoom != null && !inventory.contains(GATESTONE);
		case TELE_GROUPSTONE:
			return !inventory.contains(GGS) && inventory.getCount(true, LAW_RUNES) > 2;
		case TELE_HOME:
			return !player().isInCombat();
		}
		return true;
	}

	private boolean castDungeonSpell(final Spell spell, final String cast) {
		selectTab(Game.TAB_MAGIC, 2);
		RSInterface spellBook = interfaces.get(950);
		if (!spellBook.isValid())
			return false;
		int filterID = 0;
		if (spell == combatSpell) {
			filterID = 7;
		} else if (spell.type() == Magic.TELE) {
			filterID = 9;
		} else filterID = 11;
		RSComponent filter = spellBook.getComponent(filterID);
		return (filter.getBackgroundColor() != 1701 || clickComponent(filter, "Filter")) && spellBook.getComponent

(spell.index()).interact(cast);
	}

	private boolean checkDungeonMap(final boolean openMap) {
		if (openMap) {
			if (!mapOpen() && interfaces.get(548).isValid()) {
				log("Checking the dungeon map.");
				clickComponent(interfaces.getComponent(548, 179), "");
				sleep(400, 600);
				return true;
			}
		} else if (mapOpen()) {
			log("Closing the dungeon map.");
			return clickComponent(interfaces.getComponent(942, 6), "");
		}
		return false;
	}

	private boolean clickComponent(final RSComponent component, final String action) {
		if (component == null || !component.isValid())
			return false;
		Rectangle rect = component.getArea();
		if (rect.x == -1)
			return false;
		int minX = rect.x + 2, minY = rect.y + 2, width = rect.width - 4, height = rect.height - 4;
		if (rect.width > 20) {
			minX = rect.x + 3;
			minY = rect.y + 3;
			width = rect.width - 6;
			height = rect.height - 6;
		}
		Rectangle actual = new Rectangle(minX, minY, width, height);
		if (actual.contains(mouse.getLocation()) && menu.contains(action) && menu.doAction(action))
			return true;
		mouse.move(random(minX, minX + width), random(minY, minY + height));
		return menu.doAction(action);
	}

	private boolean clickDialogueOption(final RSInterface inter, final String... options) {
		if (inter != null && inter.isValid()) {
			for (RSComponent c : inter.getComponents()) {
				for (String option : options) {
					if (c.getText().toLowerCase().contains(option.toLowerCase()))
						return c.doClick();
				}
			}
		}
		return false;
	}

	private boolean clickDoor(final RSObject obj, final String action) {
		int n = random(0, 2) == 0 ? -1 : 1;
		for (RSTile t : getAdjacentIncluding(randomTile(obj))) {
			for (int d = 0; d < 7; d++) {
				Point loc = calc.tileToScreen(t, random(.1, .9), random(.1, .9), 0);
				if (loc.x != -1) {
					mouse.move(loc);
					sleep(20, 200);
					if (unreachable) {
						threadTurn(camera.getAngle() + n * random(60, 100), 10);
						d -= 3;
						unreachable = false;
					}
					if (menu.doAction(action) || menu.doAction(action))
						return true;
					if (!isMoving())
						d += 3;
				} else if (!isMoving()) {
					walkTo(t, 1);
				}
			}
		}
		return false;
	}

	private boolean clickModel(final RSModel m, final String action) {
		if (m == null)
			return false;
		if (random(0, 12) > random(7, 9))
			mouse.move(m.getPoint());
		for (int c = 0; c < 10; c++) {
			if (modelContains(m, mouse.getLocation())) {
				if (menu.doAction(action))
					return true;
				if (!menu.contains(action)) {
					if (!isMoving())
						c += 3;
					c += 2;
				}
			}
			mouse.move(m.getPoint());
		}
		return false;
	}

	private boolean clickNpc(final RSNPC npc, final String action) {
		if (npc == null)
			return false;
		RSModel model = npc.getModel();
		return model != null ? clickModel(model, action) : clickTile(npc.getLocation(), action);
	}

	private boolean clickObj(final RSObject obj, final String action) {
		if (obj == null)
			return false;
		RSModel model = obj.getModel();
		if (model == null && obj.isOnScreen()) {
			secondaryStatus = "Model is null! Attempting to click.";
			if (open || finish)
				return clickDoor(obj, action);
		}
		return model != null ? clickModel(model, action) : tiles.interact(obj.getLocation(), action);
	}

	private boolean clickTile(final RSTile tile, final String action) {
		return clickTile(tile, random(.35, .65), random(.35, .65), action);
	}

	private boolean clickTile(final RSTile tile, final double xd, final double yd, final String action) {
		for (int c = 0; c < 10; c++) {
			Point loc = calc.tileToScreen(tile, xd, yd, 0);
			if (loc.x != -1) {
				mouse.move(loc);
				if (!isMoving())
					sleep(5, 50);
				if (menu.doAction(action))
					return true;
				if (!isMoving()) {
					if (!action.startsWith("Use") && menu.contains("Use")) {
						walkToMap(tile, 1);
					} else c += 4;
				}
			} else if (!isMoving()) {
				walkTo(tile, 1);
			}
		}
		return false;
	}

	private int crossTheChasm(final RSTile dest) {
		if (dest != null && memberWorld && chasmRooms.contains(targetRoom) && !reachableTwice(dest, true)) {
			levelRequirement = true;
			RSObject chasm = getObjInRoom(GRAPPLED_CHASMS);
			String action = "Cross-the";
			if (chasm == null) {
				chasm = getObjInRoom("Unfinished bridge");
				action = "Jump";
			}
			if (chasm != null) {
				if (puzzleTimer == null)
					log(BLU, "Crossing the chasm to the next door.");
				safeTile = chasm.getLocation();
				while (!reachableTwice(dest, true)) {
					if (failCheck())
						return 2;
					if (!requirements())
						return -1;
					if (doObjAction(objects.getTopAt(safeTile), action))
						waitToStop(true);
					waitToStop(true);
					while (isMoving() || player().getAnimation() == 14554) {
						sleep(600, 1000);
					}
				}
				safeTile = null;
			}
		}
		return 1;
	}

	private int crystalDist(final RSTile center, final RSObject obj) {
		return obj != null ? 5 - (int) calc.distanceBetween(center, obj.getLocation()) : 0;
	}

	private boolean deadNpc(final RSNPC npc) {
		return npc == null || !npc.isValid() || (getHpPercent(npc) == 0 && npc.getAnimation() != -1 && npc.getInteracting() == 

null);
	}

	private void defenseDegenerate() {
		if (bossRoom == null)
			return;
		if (bossName.equals("Bal'lak the Pummeller") && interfaces.get(945).isValid()) {
			String oldStatus = secondaryStatus;
			secondaryStatus = "Allowing his defense to degenerate";
			int defenseBar = interfaces.getComponent(945, 17).getRelativeX() - 15;
			int rtb = bossPath.size();
			if (aComplexity > 4 || rtb < 1)
				rtb = 1;
			int waitTime = (175 * defenseBar - rtb * 7000);
			if (waitTime > 0)
				sleep(waitTime, (int) (waitTime * 1.1));
			secondaryStatus = oldStatus;
		}
	}

	private boolean destroyItem(final int itemId) {
		RSItem item = inventory.getItem(itemId);
		if (item == null || !item.getName().endsWith("(b)") || !item.getName().endsWith("(o)"))
			return false;
		log(RED, "Destroying item: " + equipmentName(item));
		while ((item = inventory.getItem(itemId)) != null) {
			if (failCheck())
				return false;
			if (interfaces.get(94).isValid()) {
				interfaces.getComponent(94, 3).doClick();
			} else item.interact("Destroy");
			sleep(700, 1100);
		}
		return !inventory.contains(itemId);
	}

	private void disRobe() {
		if (!disrobed) {
			if ((bossRoom == null || !areaContains(bossRoom, myLoc())) && inventory.getCount() > 24)
				dropAllExceptSaves();
			for (int c = 0; c < 3; c++) {
				for (int slot : EQUIP_SLOTS) {
					RSItem eq = equipment.getItem(slot);
					if (eq == null || eq.getID() == -1)
						continue;
					if (shadowHooded && eq.getName().contains("silk hood"))
						continue;
					for (int d = 0; d < 3; d++) {
						if (equipAction(slot, "Remove"))
							break;
						sleep(100, 300);
					}
					sleep(200, 400);
				}
			}
			disrobed = true;
		}
	}

	private boolean dodgeSlayerMonster() {
		if (memberWorld && !isMoving()) {
			RSNPC slayer = getNpcInRoom(SLAYER_MONSTERS);
			if (slayer != null)
				return calc.distanceTo(slayer) < 3 && walkFast(myLoc(), 15);
		}
		return false;
	}

	private boolean doItemAction(final RSItem item, final String action) {
		if (item == null)
			return false;
		while (itemTimer.isRunning()) {
			sleep(50, 100);
		}
		itemTimer = new Timer(random(800, 1200));
		return item.interact(action) || item.interact(action);
	}

	private boolean doNpcAction(final RSNPC npc, final String action) {
		if (npc == null || !npc.isValid())
			return false;
		RSTile npcTile = npc.getLocation();
		if (!npc.isOnScreen()) {
			if (!isMoving()) {
				if (walkFast(npcTile, 1) && calc.distanceTo(npcTile) > 6) {
					turnTo(npcTile);
				} else waitToStart(false);
			}
			while (npc != null && !isDead() && isMoving() && !npc.isOnScreen()) {
				sleep(50, 100);
			}
		}
		return clickNpc(npc, action);
	}

	private boolean doObjAction(final RSObject obj) {
		if (obj == null)
			return false;
		RSObjectDef oDef = obj.getDef();
		String objAction = "";
		if (oDef != null) {
			for (String action : oDef.getActions()) {
				if (action != null && !action.isEmpty() && !action.equals("Examine") && !action.equals("Walk here") && 

!action.equals("Cancel")) {
					objAction = action + " " + oDef.getName();
					break;
				}
			}
		}
		if (objAction.isEmpty()) {
			RSTile oTile = obj.getLocation();
			if (calc.distanceTo(oTile) > 4 && calc.distanceTo(oTile) < 20)
				walkTo(oTile, 1);
		}
		return doObjAction(obj, objAction);
	}

	private boolean doObjAction(final RSObject obj, final String action) {
		if (obj == null)
			return false;
		boolean toReturn = false;
		RSTile objTile = obj.getLocation();
		if (game.getCurrentTab() == Game.TAB_INVENTORY) {
			RSItem selItem = inventory.getSelectedItem();
			if (selItem != null)
				selItem.doClick(true);
		}
		if (clickObj(obj, action)) {
			RSTile start = myLoc();
			double dist = calc.distanceBetween(start, objTile);
			sleep((int) dist * 200, (int) dist * 220);
			toReturn = isMoving() || calc.distanceTo(objTile) < 3 || !start.equals(myLoc());
		}
		if (!isMoving() && calc.distanceTo(objTile) > 4) {
			walkFast(objTile, 1);
			if (status.contains("Backtracking") && !combat.isAutoRetaliateEnabled() && game.getCurrentTab() != 

Game.TAB_ATTACK) {
				selectTab(Game.TAB_ATTACK, 3);
			} else if (calc.distanceTo(objTile) > random(6, 8)) {
				turnTo(objTile);
			} else waitToStart(false);
		}
		while (authCheck && isMoving() && !obj.isOnScreen()) {
			if (action.equals("Pull") || !topUp(false))
				sleep(100, 200);
		}
		return toReturn || clickObj(obj, action);
	}

	private boolean doorCheck() {
		if ((lastMessage.contains("level of") || lastMessage.contains("unable to")) && !temporaryReduction()) {
			log("We don't have the level requirement for this door, removing.");
			finishedDoors.add(nearDoor);
			allDoors.remove(nearDoor);
			openedDoors.remove(nearDoor);
			goodDoors.remove(nearDoor);
			return false;
		}
		return true;
	}

	private int doorCount(final ArrayList<RSTile> doors) {
		int doorCount = 0;
		for (RSTile d : doors) {
			if (targetRoom.contains(d))
				doorCount++;
		}
		return doorCount;
	}

	private boolean dropAllExceptSaves() {
		int startCount = inventory.getCount();
		if (startCount - getSaleCount() == 0)
			return false;
		if (isItemAt(myLoc(), true))
			walkAdjacentTo(myLoc(), 2);
		for (RSItem item : inventory.getItems()) {
			if (item == null || !riddable(item))
				continue;
			if (!item.interact("Drop")) {
				if (!droppable(item)) {
					destroyItem(item.getID());
					continue;
				}
				sleep(50, 200);
				doItemAction(item, "Drop");
			}
			if (intMatch(item.getID(), TOOLS))
				sleep(800, 1100);
		}
		return inventory.getCount() < startCount;
	}

	private boolean dropItem(final int id) {
		if (!droppable(inventory.getItem(id)))
			return false;
		while (inventory.contains(id)) {
			if (failBasic())
				return false;
			ridItem(id, "Drop");
		}
		return true;
	}

	private boolean droppable(final RSItem item) {
		if (item == null)
			return true;
		String name = item.getName();
		return !name.endsWith("(o)") && !name.endsWith("(b)");
	}

	private boolean eatFood(final int[] foodSelection, int eatAt, int eatTo) {
		if (!invContains(foodSelection))
			return false;
		int rA = eatAt / 10, rT = eatTo / 10, startPercent = combat.getHealth();
		eatAt = eatAt + random(-rA, rA + 1);
		if (startPercent >= eatAt)
			return false;
		RSItem food;
		eatTo = eatTo + random(-rT, rT + 1);
		selectTab(Game.TAB_INVENTORY, 3);
		while ((food = getItemInOrder(foodSelection)) != null) {
			if (failCheck())
				return false;
			if (bossRoom != null && objects.getNearest(FINISHED_LADDERS) != null)
				return false;
			for (int i = 0; i < 7; i++) {
				if (startPercent < combat.getHealth() - 1)
					break;
				if (!itemTimer.isRunning()) {
					doItemAction(food, "Eat");
				} else sleep(100, 300);
			}
			if (eatTo == 0 || (startPercent = combat.getHealth()) >= eatTo)
				return true;
		}
		return true;
	}

	private static String enumName(String name) {
		name = name.replace("_", " ").replace("0", "-");
		return name.substring(0, 1) + name.substring(1).toLowerCase();
	}

	private static <T extends Enum<T>> T enumOf(Class<T> c, String string) {
		try {
			return Enum.valueOf(c, string.replace(" ", "_").replace("-", "0").replace("2", "TWO").toUpperCase());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	private boolean equipAction(final int equipSlot, final String action) {
		RSItem equip = equipment.getItem(equipSlot);
		return equip != null && equip.getID() > 0 ? clickComponent(equip.getComponent(), action) : false;
	}

	private boolean equipFor(final Style style) {
		if (style == null)
			return false;
		int weapon = style != primaryStyle ? secondaryWep : primaryWep;
		boolean ret = inventory.contains(weapon) && ridItem(weapon, "Wield");
		if (blastBox > 0 && style == Style.MAGIC) {
			if (inventory.contains(blastBox))
				return ridItem(blastBox, "Wield");
		} else if (arrows > 0 && style == Style.RANGE) {
			if (inventory.contains(arrows))
				return ridItem(arrows, "Wield");
		}
		return ret;
	}

	private String equipmentMaterial(String name) {
		if (name == null)
			return "";
		int idx = name.indexOf(" ");
		return idx == -1 ? name : name.substring(0, idx);
	}

	private String equipmentName(final RSItem item) {
		if (item == null)
			return "";
		String name = item.getName();
		if (name == null)
			return "";
		int infoIdx = name.indexOf(" (");
		return infoIdx == -1 ? name : name.substring(0, infoIdx);
	}

	private int equipmentTier(final String name) {
		if (name != null && !name.isEmpty() && !name.contains("(o)")) {
			String material = equipmentMaterial(name);
			for (int I = 0; I < armorTiers.length; I++) {
				if (armorTiers[I].equals(material))
					return I + 1;
			}
			if (weaponTiers != null) {
				for (int I = 0; I < weaponTiers.length; I++) {
					if (weaponTiers[I].equals(material))
						return I + 1;
				}
			}
		}
		return 0;
	}

	private String equipmentType(String name) {
		if (name == null || name.isEmpty())
			return "";
		name = name.substring(name.indexOf(" ") + 1);
		int bracketIdx = name.indexOf(" (");
		return bracketIdx == -1 ? name : name.substring(0, bracketIdx);
	}

	private RSTile farthest(final RSObject obj) {
		if (obj == null)
			return null;
		double dist = 0.0;
		RSTile far = randomTile(obj);
		for (RSTile tile : obj.getArea().getTileArray()) {
			double tDist = calc.distanceBetween(startRoom.getCentralTile(), tile);
			if (tDist > dist) {
				far = tile;
				dist = tDist;
			}
		}
		return far;
	}

	private int foodLowest() {
		for (int id : FOOD_ARRAY) {
			if (inventory.contains(id))
				return id;
		}
		return -1;
	}

	private long getDungExpToLevel() {
		return Skills.XP_TABLE[levelGoal] - skills.getCurrentExp(Skills.DUNGEONEERING);
	}

	private ArrayList<RSTile> generatePath(RSArea nextRoom) {
		ArrayList<RSTile> doors = new ArrayList<RSTile>();
		int c = 0;
		while (nextRoom != null && !areaEquals(nextRoom, startRoom)) {
			o:for (RSTile door : backDoors) {
				if (areaContains(nextRoom, door)) {
					nextRoom = getNextRoom(door);
					if (nextRoom != null) {
						for (RSTile d : openedDoors) {
							if (calc.distanceBetween(d, door) < 4) {
								doors.add(d);
								break o;
							}
						}
						nextRoom = null;
					}
				}
			}
		c++;
		if (c > 30) {
			if (developer)
				log(RED, "Unable to generate path.");
			break;
		}
		}
		return doors;
	}

	private RSTile[] getAdjacentIncluding(final RSTile t) {
		int tX = t.getX(), tY = t.getY();
		return new RSTile[] { t, new RSTile(tX + 1, tY), new RSTile(tX - 1, tY), new RSTile(tX, tY + 1), new RSTile(tX, tY - 

1) };
	}

	private RSTile[] getAdjacentTo(final RSTile t) {
		int tX = t.getX(), tY = t.getY();
		return new RSTile[] { new RSTile(tX + 1, tY), new RSTile(tX - 1, tY), new RSTile(tX, tY + 1), new RSTile(tX, tY - 1) 

};
	}

	private RSTile getBackDoor() {
		if (!backDoors.isEmpty()) {
			for (RSTile door : backDoors) {
				if (areaContains(targetRoom, door))
					return door;
			}
		} else {
			RSObject door = getObjInRoom(BACK_DOORS);
			if (door != null) {
				RSTile d = door.getLocation();
				if (!drawDoors.contains(d))
					return d;
			}
		}
		return null;
	}

	private void getBestStyle(final RSNPC npc) {
		tempMode = getWeakness(npc);
	}

	private RSTile[] getDiagonalTo(final RSTile t) {
		int tX = t.getX(), tY = t.getY();
		return new RSTile[] { new RSTile(tX + 1, tY + 1), new RSTile(tX - 1, tY + 1), new RSTile(tX - 1, tY - 1), new RSTile

(tX + 1, tY - 1) };
	}

	private RSTile[] getDiagonalTriple(final RSTile t) {
		int tX = t.getX(), tY = t.getY();
		return new RSTile[] { new RSTile(tX + 3, tY + 3), new RSTile(tX - 3, tY + 3), new RSTile(tX - 3, tY - 3), new RSTile

(tX + 3, tY - 3) };
	}

	private RSGroundItem getGoodItem() {
		return nearItem = getRoomItem(itemFilter);
	}

	private RSObject getCloseObjTo(final RSTile t, final int... IDs) {
		return getRoomObj(myLoc(), new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				if (!intMatch(o.getID(), IDs))
					return false;
				RSTile oTile = o.getLocation();
				return !myLoc().equals(oTile) && calc.distanceBetween(oTile, t) < 4;
			}
		});
	}

	private RSArea getCurrentRoom() {
		RSTile myTile = myLoc();
		if (currentRoom == null || !areaContains(currentRoom, myTile)) {
			for (int i = 0; i < rooms.size(); i++) {
				if (areaContains(rooms.get(i), myTile)) {
					currentRoom = rooms.get(i);
					roomNumber = i + 1;
					break;
				}
			}
		}
		return currentRoom;
	}

	private RSNPC getGoodNpc(final String... names) {
		return npcs.getNearest(new Filter<RSNPC>() {
			public boolean accept(RSNPC npc) {
				if (npc == null || !stringMatch(npc.getName(), names))
					return false;
				return areaContains(targetRoom, npc.getLocation()) && !deadNpc(npc);
			}
		});
	}

	private int getHpPercent(final RSCharacter ch) {
		try {
			return ch.getHPPercent();
		} catch (Exception e) {
			if (developer)
				e.printStackTrace();
			return 100;
		}
	}

	public int getItemID(final String... names) {
		for (final RSItem item : inventory.getItems()) {
			String name = item.getName();
			if (name != null) {
				name = name.toLowerCase();
				for (final String n : names) {
					if (n != null && name.contains(n.toLowerCase()))
						return item.getID();
				}
			}
		}
		return -1;
	}

	private RSItem getItemInOrder(final int... ids) {
		for (int id : ids) {
			RSItem item = inventory.getItem(id);
			if (item != null)
				return item;
		}
		return null;
	}

	private RSGroundItem getItemInRoom(final RSArea room, final int... ids) {
		if (room == null || ids.length == 0)
			return nearItem = null;
		double dist = 99.99;
		int minX = room.getX() + 1, minY = room.getY() + 1;
		int maxX = minX + room.getWidth() - 1, maxY = minY + room.getHeight() - 1;
		RSGroundItem itm = null;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				double tDist = calc.distanceBetween(myLoc(), new RSTile(x, y));
				if (tDist < dist) {
					for (RSGroundItem item : groundItems.getAllAt(x, y)) {
						if (item != null && intMatch(item.getItem().getID(), ids)) {
							if (tDist == 0)
								return nearItem = item;
							dist = tDist;
							itm = item;
							break;
						}
					}
				}
			}
		}
		return nearItem = itm;
	}

	private RSTile getKey() {
		nearItem = getRoomItem(keyItem);
		return nearItem != null ? nearItem.getLocation() : null;
	}

	private Monster getMonster(final RSNPC npc) {
		if (npc == null)
			return null;
		String name = npc.getName();
		int id = npc.getID();
		if (id == -1 || name.isEmpty())
			return null;
		if (name.equals("Forgotten warrior")) {
			if (intMatch(npc.getID(), CHAIN_WARRIORS)) {
				name = "FORGOTTEN_WARRIOR0CHAIN";
			} else if (intMatch(npc.getID(), PLATE_WARRIORS)) {
				name = "FORGOTTEN_WARRIOR0PLATE";
			}
		} else if (name.equals("Mysterious shade")) {
			// Range: 10840, 10841
			// Magic: 
			name = id > 10836 ? "MYSTERIOUS_SHADE0RANGE" : "MYSTERIOUS_SHADE0MAGIC";
		} else if (name.equals("Skeleton")) {
			name = id > 10681 ? "SKELETON0MAGIC" : id > 10666 ? "SKELETON0RANGE" : "SKELETON0MELEE";
		} else if (name.equals("Zombie")) {
			name = id > 10374 ? "ZOMBIE0RANGE" : "ZOMBIE0MELEE";
		}
		return enumOf(Monster.class, name);
	}

	private String getName(final RSObject obj) {
		String name = obj != null ? obj.getName() : null;
		return name != null ? name : "";
	}

	private RSNPC getNearestNpcTo(final RSTile heading, final String name) {
		double dist = 99999.99;
		RSNPC bestNpc = null;
		if (heading != null) {
			for (RSNPC npc : npcs.getAll()) {
				RSTile nT = npc.getLocation();
				double nDist = calc.distanceBetween(nT, heading);
				if (nDist < dist) {
					if (npc.getName().toLowerCase().contains(name.toLowerCase())) {
						dist = nDist;
						bestNpc = npc;
					}
				}
			}
		}
		return bestNpc;
	}

	private RSTile getNearestObjTo(final RSTile heading, final int... IDs) {
		double dist = 99999.99;
		RSTile bestTile = null;
		if (heading != null) {
			for (RSObject o : getObjsInRoom(IDs)) {
				if (o == null)
					continue;
				double oDist = calc.distanceBetween(o.getLocation(), heading);
				if (oDist < dist) {
					dist = oDist;
					bestTile = o.getLocation();
				}
			}
		}
		return bestTile;
	}

	private RSTile getNearestObjTo(final RSTile heading, final String name) {
		double dist = 99999.99;
		RSTile bestTile = null;
		if (heading != null) {
			for (RSObject o : getObjsInRoom(name)) {
				double oDist = calc.distanceBetween(o.getLocation(), heading);
				if (oDist < dist) {
					dist = oDist;
					bestTile = o.getLocation();
				}
			}
		}
		return bestTile;
	}

	private RSTile getNearestSWObjTo(final RSTile heading, final int... IDs) {
		double dist = 99999.99;
		RSTile bestTile = null;
		for (int x = targetRoom.getX(); x < heading.getX(); x++) {
			for (int y = targetRoom.getY(); y < heading.getY(); y++) {
				RSTile t = new RSTile(x, y);
				RSObject o = objects.getTopAt(t);
				if (o != null && intMatch(o.getID(), IDs)) {
					double tDist = calc.distanceBetween(heading, t);
					if (tDist < dist) {
						bestTile = t;
						dist = tDist;
					}
				}
			}
		}
		return bestTile;
	}

	private RSTile getNearestTileTo(final RSTile heading, final ArrayList<RSTile> checks) {
		if (heading == null)
			return null;
		double dist = 99999.99;
		RSTile bestTile = null;
		for (RSTile c : checks) {
			double tDist = calc.distanceBetween(heading, c);
			if (tDist < dist) {
				bestTile = c;
				dist = tDist;
			}
		}
		return bestTile;
	}

	private RSObject getNearObjTo(final RSTile t, final int... IDs) {
		return getRoomObj(myLoc(), new Filter<RSObject>() {
			public boolean accept(RSObject obj) {
				if (!intMatch(obj.getID(), IDs))
					return false;
				return calc.distanceBetween(obj.getLocation(), t) < 6 && reachableObj(obj);
			}
		});
	}

	private RSTile getNearTriple(final RSTile heading, final RSTile dest) {
		double dist = 99.99, dfDist = calc.distanceBetween(dest, heading);
		RSTile me = myLoc(), near = null;
		puzzlePoints.clear();
		for (RSTile t : getDiagonalTriple(heading)) {
			puzzlePoints.add(t);
			double tDist = calc.distanceBetween(me, t);
			if (tDist < dist && calc.distanceBetween(t, dest) > dfDist) {
				dist = tDist;
				near = t;
			}
		}
		return near;
	}

	private RSObject getNextObj(final RSObject oldObj, final int... IDs) {
		double dist = 99.99;
		RSObject next = null;
		if (oldObj != null) {
			for (RSObject o : getObjsInRoom(IDs)) {
				if (o.equals(oldObj))
					continue;
				double oDist = calc.distanceBetween(myLoc(), o.getLocation());
				if (oDist < dist) {
					dist = oDist;
					next = o;
				}
			}
		}
		return next;
	}

	private RSArea getNextRoom(final RSTile door) {
		if (door != null) {
			for (RSArea room : rooms) {
				if (calc.distanceBetween(door, room.getNearestTile(door)) < 3)
					return room;
			}
		}
		return null;
	}

	private RSNPC getNpcAt(final RSTile check) {
		return check == null ? null : npcs.getNearest(new Filter<RSNPC>() {
			public boolean accept(RSNPC npc) {
				return npc != null && npc.getLocation().equals(check);
			}
		});
	}

	private RSNPC getNpcInRoom(final int... IDs) {
		if (targetRoom == null)
			return null;
		return npcs.getNearest(new Filter<RSNPC>() {
			public boolean accept(RSNPC npc) {
				return npc != null && intMatch(npc.getID(), IDs) && areaContains(targetRoom, npc.getLocation());
			}
		});
	}

	private RSNPC getNpcInRoom(final String... names) {
		return targetRoom == null ? null : npcs.getNearest(new Filter<RSNPC>() {
			public boolean accept(RSNPC npc) {
				return npc != null && stringMatch(npc.getName(), names) && areaContains(targetRoom, npc.getLocation

());
			}
		});
	}

	private RSNPC[] getNpcsInRoom(final int... IDs) {
		return targetRoom == null ? null : npcs.getAll(new Filter<RSNPC>() {
			public boolean accept(RSNPC npc) {
				return npc != null && intMatch(npc.getID(), IDs) && areaContains(targetRoom, npc.getLocation());
			}
		});
	}

	private RSNPC[] getNpcsInRoom(final String... names) {
		return targetRoom == null ? null : npcs.getAll(new Filter<RSNPC>() {
			public boolean accept(RSNPC npc) {
				return npc != null && stringMatch(npc.getName(), names) && areaContains(targetRoom, npc.getLocation

());
			}
		});
	}
	private RSObject getObjExact(final String name) {
		return getRoomObj(myLoc(), new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && o.getName().equals(name);
			}
		});
	}

	private RSObject getObjInRoom(final int... IDs) {
		return getRoomObj(myLoc(), new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && intMatch(o.getID(), IDs);
			}
		});
	}

	private RSObject getObjInRoom(final String... names) {
		return getRoomObj(myLoc(), new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && stringMatch(o.getName(), names);
			}
		});
	}

	private RSTile getObjLocation(final int... IDs) {
		RSObject obj = getObjInRoom(IDs);
		return obj != null ? obj.getLocation() : null;
	}

	private RSObject[] getObjsInRoom(final int... IDs) {
		return getRoomObjs(new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && intMatch(o.getID(), IDs);
			}
		});
	}

	private RSObject[] getObjsInRoom(final String... names) {
		return getRoomObjs(new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && stringMatch(o.getName(), names);
			}
		});
	}

	private RSObject getReachableObj(final int... IDs) {
		return getRoomObj(myLoc(), new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && intMatch(o.getID(), IDs) && reachableObj(o);
			}
		});
	}

	private RSObject getReachableObjNear(final RSObject obj, final String... names) {
		return getRoomObj(obj.getLocation(), new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && !o.equals(obj) && stringMatch(o.getName(), names) && reachableObj(o);
			}
		});
	}

	private void getRoomBlocks() {
		ArrayList<RSTile> blocks = new ArrayList<RSTile>();
		try {
			final int block[][] = walking.getCollisionFlags(game.getPlane());
			final int gX = game.getBaseX(), gY = game.getBaseY();
			final int tX = targetRoom.getX() - gX, tY = targetRoom.getY() - gY;
			for (int rX = tX; rX < tX + targetRoom.getWidth(); rX++) {
				for (int rY = tY; rY < tY + targetRoom.getHeight(); rY++) {
					if ((block[rX + 1][rY + 1] & 0x1280100) != 0)
						blocks.add(new RSTile(rX + gX, rY + gY));
				}
			}
		} catch (Exception e) {
		}
		roomFlags = blocks;
	}

	private RSGroundItem getRoomItem(final Filter<RSGroundItem> filter) {
		double dist = 99.99;
		int minX = targetRoom.getX() + 1, minY = targetRoom.getY() + 1;
		int maxX = minX + targetRoom.getWidth() - 1, maxY = minY + targetRoom.getHeight() - 1;
		RSGroundItem itm = null;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				double tDist = calc.distanceBetween(myLoc(), new RSTile(x, y));
				if (tDist < dist) {
					for (RSGroundItem item : groundItems.getAllAt(x, y)) {
						if (filter.accept(item)) {
							if (tDist == 0)
								return item;
							dist = tDist;
							itm = item;
							break;
						}
					}
				}
			}
		}
		return itm;
	}

	private RSGroundItem[] getRoomItems() {
		ArrayList<RSGroundItem> items = new ArrayList<RSGroundItem>();
		int minX = targetRoom.getX() + 1, minY = targetRoom.getY() + 1;
		int maxX = minX + targetRoom.getWidth() - 1, maxY = minY + targetRoom.getHeight() - 1;
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (RSGroundItem item : groundItems.getAllAt(x, y)) {
					if (item != null)
						items.add(item);
				}
			}
		}
		return items.toArray(new RSGroundItem[items.size()]);
	}

	private RSObject getRoomObj(final RSTile nearTo, final Filter<RSObject> filter) {
		if (targetRoom == null)
			return null;
		RSObject obj = null;
		double dist = 99.99;
		int minX = targetRoom.getX(), minY = targetRoom.getY();
		int maxX = minX + targetRoom.getWidth(), maxY = minY + targetRoom.getHeight();
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				RSTile t = new RSTile(x, y);
				double tDist = calc.distanceBetween(nearTo, t);
				if (tDist < dist) {
					for (RSObject o : objects.getAllAt(t)) {
						if (filter.accept(o)) {
							if (tDist == 0)
								return o;
							obj = o;
							dist = tDist;
							break;
						}
					}
				}
			}
		}
		return obj;
	}

	private RSObject[] getRoomObjs(final Filter<RSObject> filter) {
		Set<RSObject> objs = new LinkedHashSet<RSObject>();
		int minX = targetRoom.getX(), minY = targetRoom.getY();
		int maxX = minX + targetRoom.getWidth(), maxY = minY + targetRoom.getHeight();
		for (int x = minX; x < maxX; x++) {
			for (int y = minY; y < maxY; y++) {
				for (RSObject o : objects.getAllAt(new RSTile(x, y))) {
					if (filter.accept(o))
						objs.add(o);
				}
			}
		}
		return objs.toArray(new RSObject[objs.size()]);
	}

	private int getSaleCount() {
		int count = 0;
		for (RSItem item : inventory.getItems()) {
			if (item != null && item.getID() > 0)
				count += !riddable(item) ? 1 : 0;
		}
		return count;
	}

	private int getSelectedSpell() {
		if (interfaces.get(950).isValid()) {
			Point sp = interfaces.getComponent(950, 68).getCenter();
			if (sp.x > 560 && sp.y > 220) {
				for (int c = 24; c < 68; c++) {
					if (interfaces.getComponent(950, c).getArea().contains(sp))
						return c;
				}
			}
		}
		return -1;
	}

	private Spell getSpell(final int idx) {
		for (Spell s : Spell.values()) {
			if (s.index() == idx)
				return s;
		}
		return null;
	}

	private RSTile[] getSurrounding(final RSTile t) {
		int tX = t.getX(), tY = t.getY();
		return new RSTile[] { new RSTile(tX + 1, tY + 1), new RSTile(tX, tY + 1), new RSTile(tX - 1, tY + 1), new RSTile(tX - 

1, tY), new RSTile(tX - 1, tY - 1), new RSTile(tX, tY - 1), new RSTile(tX + 1, tY - 1), new RSTile(tX + 1, tY) };
	}

	private RSTile getTileOfObjs(final int[]... objsIds) {
		for (RSTile t : targetRoom.getTileArray()) {
			int count = 0;
			for (RSObject o : objects.getAllAt(t)) {
				for (int[] ids : objsIds) {
					if (intMatch(o.getID(), ids)) {
						count++;
						break;
					}
				}
			}
			if (count == objsIds.length)
				return t;
		}
		return null;
	}

	private Combat getWeakness(final RSNPC npc) {
		Monster m = getMonster(npc);
		if (m != null) {
			for (Combat w : m.getWeaknesses()) {
				if (w.enabled() && w.style() == combatStyle)
					return w;
			}
			if (npc.getLevel() > player().getCombatLevel() - random(20, 30)) {
				for (Combat w : m.getWeaknesses()) {
					if (w.enabled() && w.style() != Style.MAGIC && swapStyle(w.style()))
						return w;
				}
			}
		}
		return Melee.NONE;
	}

	private boolean goodDungeon() {
		if (!settingsFinished && objects.getAll().length > 200) {
			log(RED, "This dungeon appears to have already been explored.");
			isDead = false;
			exit = true;
			return false;
		}
		return true;
	}

	private int healthLost() {
		return combat.getLifePoints() - skills.getRealLevel(Skills.CONSTITUTION) * 10;
	}

	private boolean hoodMonster(final RSNPC npc) {
		Monster test = getMonster(npc);
		if (test != null) {
			for (Monster m : HOODED_MONSTERS) {
				if (test == m)
					return true;
			}
		}
		return false;
	}

	private boolean improveArmorWielding() {
		if (improvements == null)
			return false;
		boolean upgraded = false;
		for (String iName : improvements) {
			if (iName != null && !iName.isEmpty()) {
				int itemID = getItemID(iName);
				if (itemID < 1)
					continue;
				int tier = equipmentTier(iName);
				RSItem item = inventory.getItem(itemID);
				if (item != null && tier != 0) {
					String type = equipmentType(iName);
					for (int I = 0; I < slotNames.length; I++) {
						if (tier > equipmentTiers[I] && type.contains(slotNames[I])) {
							if (armorSlot == I && type.equals(armorType)) {
								boolean destroyed = false;
								int oldArmor = valueOf(startArmor);
								log(PRP, "Upgrading our " + type + " to " + equipmentMaterial(iName));
								while (startArmor == oldArmor) {
									if (failCheck())
										return false;
									int updatedArmor = getItemID(iName + " (b)");
									if (updatedArmor > 0) {
										startArmor = updatedArmor;
									} else if (!destroyed) {
										if (inventory.getItem(startArmor) != null) {
											destroyed = destroyItem(startArmor);
										} else if (!destroyed && equipment.getItem

(EQUIP_SLOTS[I]).getID() > 0) {
											equipAction(EQUIP_SLOTS[I], "Remove");
										}
									} else if (inventory.contains(itemID)) {
										ridItem(itemID, "Bind");
									} else if (pickUpItem(getItemInRoom(targetRoom, itemID))) {
										waitToStop(false);
									} else destroyed = false;
									sleep(400, 800);
								}
								equipmentTiers[I] = tier;
								if (bounds.contains((Integer) oldArmor))
									bounds.remove((Integer) oldArmor);
								bounds.add((Integer) startArmor);
								upgraded = ridItem(startArmor, "Wear");
							} else if (ridItem(itemID, "Wear")) {
								equipmentTiers[I] = tier;
								upgraded = true;
							}
							break;
						}
					}
				}
			}
		}
		improvements = null;
		return upgraded;
	}

	private void improveEquipment() {
		boolean wepCheck = weaponTier < 10 && weaponTier < attackTier;
		int bestGroundTier = valueOf(weaponTier);
		for (RSGroundItem gItem : getRoomItems()) {
			String iName = gItem.getItem().getName();
			String iType = equipmentType(iName);
			int tier = equipmentTier(iName);
			if (tier == 0)
				continue;
			if (wepCheck && iType.equals(weaponType)) {
				if (tier > bestGroundTier && tier <= attackTier) {
					bestGroundTier = tier;
					newWeapon = gItem.getItem().getID();
					bounds.add((Integer) newWeapon);
				}
			} else if (tier <= defenseTier) {
				for (int I = 0; I < tempTiers.length; I++) {
					if (iType.contains(slotNames[I])) {
						if (tier > tempTiers[I] && (!slotNames[I].equals("kiteshield") || !twoHanded)) {
							tempTiers[I] = tier;
							String giName = gItem.getItem().getName();
							if (improvements == null)
								improvements = new String[tempTiers.length];
							if (improvements[I] == null || !giName.equals(improvements[I]))
								improvements[I] = giName;
						}
						break;
					}
				}
			}
		}
	}

	private void improveBossWeapon() {
		if (weaponTier < attackTier) {
			for (RSItem item : inventory.getItems()) {
				if (item != null) {
					String iName = equipmentName(item);
					if (!iName.isEmpty() && iName.contains(weaponType)) {
						int tier = equipmentTier(iName);
						if (tier > weaponTier && tier <= attackTier) {
							newWeapon = item.getID();
							break;
						}
					}
				}
			}
		}
	}

	private void improveWeaponBinding() {
		if (newWeapon > 0) {
			RSItem iItem = inventory.getItem(newWeapon);
			String iName = equipmentName(iItem);
			int tier = equipmentTier(iName);
			if (tier > 0) {
				boolean destroyed = false;
				int oldWep = primaryWep;
				weaponTier = 0;
				log(PRP, "Upgrading our " + weaponType + " to " + equipmentMaterial(iName));
				while (primaryWep == oldWep) {
					if (failCheck())
						return;
					int updatedWep = getItemID(iName + " (b)");
					RSItem primaryItem = inventory.getItem(primaryWep);
					if (updatedWep > 0) {
						primaryWep = updatedWep;
					} else if (!destroyed && primaryItem != null) {
						destroyItem(primaryWep);
					} else if (equipment.getItem(Equipment.WEAPON) != null && equipment.getItem

(Equipment.WEAPON).getID() > 0) {
						equipAction(Equipment.WEAPON, "Remove");
					} else if (inventory.contains(newWeapon)) {
						if (password.isEmpty())
							return;
						ridItem(newWeapon, "Bind");
					} else if (pickUpItem(getItemInRoom(targetRoom, newWeapon))) {
						waitToStop(false);
					}
					sleep(400, 800);
				}
				ridItem(primaryWep, "Wield");
				weaponTier = tier;
				if (bounds.contains((Integer) newWeapon))
					bounds.remove((Integer) newWeapon);
				newWeapon = -1;
			}
		}
	}

	private boolean intentionallyBacktrack() {
		RSTile backDoor = getBackDoor();
		if (backDoor == null)
			return false;
		while (roomNumber != 1 && areaEquals(currentRoom, targetRoom)) {
			if (!inDungeon() || failBasic())
				return false;
			RSObject door = objects.getTopAt(backDoor);
			if (door == null)
				return false;
			getItemInRoom(currentRoom, GGS);
			if (nearItem != null) {
				if (pickUpItem(nearItem))
					waitToStop(true);
			} else if (doObjAction(door, "Enter")) {
				waitToStart(false);
				while (isMoving() && areaContains(targetRoom, myLoc())) {
					sleep(200, 300);
				}
			}
			sleep(100, 200);
			getCurrentRoom();
		}
		setTargetRoom(currentRoom);
		return true;
	}

	private boolean intMatch(final int desired, final int... compare) {
		if (compare != null && desired > 0) {
			for (int check : compare) {
				if (desired == check)
					return true;
			}
		}
		return false;
	}

	private boolean inDungeon() {
		if (interfaces.get(945).isValid() || !game.isLoggedIn())
			return true;
		return calc.distanceTo(DAEMONHEIM) > 100;
	}

	private boolean inTrueCombat() {
		RSCharacter me = player();
		if (me != null && me.isInCombat()) {
			for (RSNPC npc : npcs.getAll(guardians)) {
				RSCharacter inter = npc.getInteracting();
				if (inter != null && inter.equals(me))
					return true;
			}
		}
		return false;
	}

	private boolean invContains(final int... IDs) {
		for (RSItem item : inventory.getItems(true)) {
			if (item != null && intMatch(item.getID(), IDs))
				return true;
		}
		return false;
	}

	private int invCount(final boolean stacked, final int... ids) {
		int count = 0;
		for (RSItem item : inventory.getItems(true)) {
			if (item != null && (ids.length == 0 || intMatch(item.getID(), ids)))
				count += stacked ? item.getStackSize() : 1;
		}
		return count;
	}

	private boolean isAutoCasting() {
		return combat.getFightMode() == 4;
	}

	private boolean isAttacking(final RSNPC npc) {
		if (npc == null || !npc.isValid())
			return false;
		if (deadNpc(npc))
			return true;
		RSCharacter interact = player().getInteracting();
		return interact != null && interact.equals(npc);
	}

	private boolean isDead() {
		return isDead || settings.getSetting(1240) == 1;
	}

	private boolean isEdgeTile(final RSTile check) {
		if (check == null || currentRoom == null)
			return false;
		int mX = check.getX(), mY = check.getY(), sX = currentRoom.getX(), sY = currentRoom.getY(), eX = sX + 15, eY = sY + 

15;
		return Math.abs(mX - sX) == 1 || Math.abs(mX - eX) == 1 || Math.abs(mY - sY) == 1 || Math.abs(mY - eY) == 1;
	}

	public boolean isFacing(final RSTile dest) {
		if (dest == null)
			return false;
		Point m = orientation(player());
		RSTile curr = myLoc();
		for (int c = 0; c < 15; c++) {
			curr = stepAlong(m, curr);
		}
		return curr.equals(dest);
	}

	private boolean isGoodMonster(final RSNPC targ) {
		if (targ != null && targ.isValid() && (targ.isInCombat() || isAttacking(targ)))
			return !deadNpc(targ);
		return false;
	}

	private boolean isGoodTile(final RSTile t, final int... badIds) {
		if (t == null || roomFlags.contains(t) || !areaContains(targetRoom, t))
			return false;
		if (badIds.length != 0) {
			for (RSObject o : objects.getAllAt(t)) {
				if (o != null && intMatch(o.getID(), badIds))
					return false;
			}
		}
		return true;
	}

	private boolean isItemAt(final RSTile t, final boolean goodItem) {
		for (RSGroundItem item : groundItems.getAllAt(t)) {
			if (item != null && item.getItem().getID() > 0 && (!goodItem || itemFilter.accept(item)))
				return true;
		}
		return false;
	}

	private boolean isMoving() {
		try {
			return player().isMoving();
		} catch (Exception e) {
			if (developer)
				log(e);
			return false;
		}
	}

	private boolean isOutside() {
		if (settingsFinished && !newDungeon && inDungeon && !exit && !status.startsWith("Entering") && !inDungeon()) {
			log.severe("Oops.. We ended up outside somehow :S");
			if (password.isEmpty())
				env.disableRandoms();
			clearAll();
			floor = Floor.OUTSIDE;
			inDungeon = false;
			newDungeon = false;
			return true;
		}
		return false;
	}

	private boolean isPrayerActive(final Book pray) {
		return (settings.getSetting(isCursing ? 1582 : 1395) & (pray.getSettings())) == pray.getSettings();
	}

	private boolean isProtecting(final RSNPC npc, final int baseID, final int style) {
		return (npc.getID() - baseID - style - 1) % 3 == 0;
	}

	private void kalgerState(final Style kStyle) {
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

	private RSTile lineOfSight(RSNPC npc) {
		RSTile current = npc.getLocation();
		Point m = orientation(npc);
		for (int c = 0; c < 10; c++) {
			current = stepAlong(m, current);
			if (c > 2 && !isGoodTile(current))
				break;
		}
		return current;
	}

	/*private Image loadImg(final String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (Exception e) {
			return null;
		}
	}*/

	private boolean makeGatestone() {
		if (gateRoom == null && !inventory.contains(GATESTONE)) {
			if (!castable(Spell.MAKE_GATESTONE))
				return false;
			String oldStatus = secondaryStatus;
			secondaryStatus = "Creating a gatestone teleport";
			while (!inventory.contains(GATESTONE)) {
				if (failCheck())
					return false;
				makeSpace(false);
				smartSleep(castDungeonSpell(Spell.MAKE_GATESTONE, "Cast"), true);
			}
			secondaryStatus = oldStatus;
		}
		if (inventory.contains(GATESTONE) && ridItem(GATESTONE, "Drop")) {
			gateRoom = getCurrentRoom();
			return true;
		}
		return false;
	}

	private ArrayList<RSTile> makePath(final RSTile start, final RSTile dest, final boolean goodDest, final int... goodIDs) {
		ArrayList<RSTile> nodes = new ArrayList<RSTile>(), oldNodes = new ArrayList<RSTile>();
		if (start == null || dest == null)
			return nodes;
		RSTile current = start, next = null;
		getRoomBlocks();
		if (goodIDs.length != 0) {
			for (RSObject o : getObjsInRoom(goodIDs)) {
				for (RSTile t : o.getArea().getTileArray()) {
					roomFlags.remove(t);
				}
			}
		}
		roomFlags.remove(dest);
		int tries = 0, masterTries = 0;
		nodes.add(current);
		while (!current.equals(dest)) {
			double lowest = 99.99;
			for (RSTile t : getSurrounding(current)) {
				if ((goodDest && calc.distanceBetween(t, dest) == 1) || t.equals(dest)) {
					nodes.add(t);
					return nodes;
				}
				if (isGoodTile(t) && !oldNodes.contains(t)) {
					double tDist = calc.distanceBetween(t, dest);
					if (tDist < lowest) {
						next = t;
						lowest = tDist;
					}
				}
			}
			tries++;
			if (next == null || masterTries > 15)
				return new ArrayList<RSTile>();
			if (next.equals(dest)) {
				nodes.add(next);
				current = next;
			} else {
				for (RSTile t : getSurrounding(next)) {
					if (isGoodTile(t) && !oldNodes.contains(t)) {
						double tDist = calc.distanceBetween(t, dest);
						if (tDist < lowest) {
							nodes.add(next);
							current = next;
							break;
						}
					}
				}
				if (!current.equals(next)) {
					for (RSTile t : getSurrounding(next)) {
						if (oldNodes.contains(t)) {
							nodes.add(next);
							current = next;
							break;
						}
					}
				}
				oldNodes.add(next);
			}
			if (tries > 30) {
				masterTries++;
				nodes.clear();
				current = start;
				next = null;
				tries = 0;
			}
		}
		return nodes;
	}

	private ArrayList<RSTile> makeSmartPath(final RSTile current, final RSTile dest, final int... goodIDs) {
		ArrayList<RSTile> smart = new ArrayList<RSTile>();
		ArrayList<RSTile> tiles = makePath(current, dest, false, goodIDs);
		int idx = -1;
		for (int I = 1; I < tiles.size(); I++) {
			RSTile test = tiles.get(I), prev = tiles.get(I - 1);
			RSTile[] surr = getSurrounding(prev);
			if (idx == -1) {
				for (int J = 0; J < surr.length; J++) {
					if (test.equals(surr[J])) {
						idx = J;
						break;
					}
				}
			} else if (!surr[idx].equals(test)) {
				smart.add(prev);
				idx = -1;
				I--;
			}
		}
		smart.add(dest);
		return puzzlePoints = smart;
	}

	private boolean makeSpace(final boolean drop) {
		if (!inventory.isFull())
			return true;
		if (!topUp(true) && !buryBones() && !improveArmorWielding()) {
			if ((!drop || !dropAllExceptSaves())) {
				int o = getItemID("(o)");
				if (o != -1 && destroyItem(o))
					return true;
				int id = -1;
				if (inventory.containsOneOf(FOOD_ARRAY)) {
					return eatFood(FOOD_ARRAY, 101, 30);
				} else {
					if (inventory.contains(ANTIPOISON)) {
						id = ANTIPOISON;
					} else if (fish != null && inventory.contains(fish.getID())) {
						id = fish.getID();
					} else if (logs != null && inventory.contains(logs.getID())) {
						id = logs.getID();
					}
					return ridItem(id, "Drop") ? true : dropAllExceptSaves();
				}
			}
		}
		return true;
	}

	private boolean mapOpen() {
		return interfaces.get(942).isValid();
	}

	private boolean memberCheck() {
		if (!memberWorld) {
			failReason = "Members-only";
			return false;
		}
		return true;
	}

	private boolean modelContains(final RSModel m, final Point p) {
		for (Polygon poly : m.getTriangles()) {
			if (poly.contains(p))
				return true;
		}
		return false;
	}

	private boolean monsterBacktrack() {
		while (npcs.getNearest(guardians) != null) {
			if (failSafe())
				return false;
			if (unBacktrackable.contains(targetRoom)) {
				log("Can't backtrack from this room, killing off enemies");
				if (!fightMonsters() || !pickUpAll())
					return false;
			} else {
				log("We need to backtrack further before teleporting home");
				intentionallyBacktrack();
			}
			getCurrentRoom();
		}
		return true;
	}

	private Book monsterPrayer() {
		if (Option.PRAY.enabled()) {
			if (!strongestMonster.isEmpty()) {
				RSNPC targ = getGoodNpc(strongestMonster);
				if (targ != null)
					return monsterBook(targ);
			}
			RSNPC targ = memberWorld ? getNpcInRoom("dragon") : null;
			if (targ != null) {
				strongestMonster = targ.getName();
				return protect(Style.MAGIC);
			}
			int level = 0;
			for (RSNPC npc : npcs.getAll(guardians)) {
				if (npc != null && npc.getLevel() > level) {
					targ = npc;
					level = npc.getLevel();
				}
			}
			if (targ != null) {
				targ.getHeight();
				strongestMonster = targ.getName();
				int cb = player().getCombatLevel() / 10;
				if (level > cb * (Option.PURE.enabled() ? 7 : 8) + random(-cb, cb))
					return monsterBook(targ);
			}
		}
		strongestMonster = "";
		return null;
	}

	private Book monsterBook(final RSNPC targ) {
		// Melee Skeleton: 661
		// Range Skeleton: 670, 680
		// Magic Skeleton: 693
		if (targ == null)
			return null;
		if (prayerLevel > 34) {
			Monster m = getMonster(targ);
			if (m != null) {
				protection = m.combatStyle();
				return protect(m.combatStyle());
			}
		} else Option.PRAY.set(false);
		return null;
	}

	private RSTile myLoc() {
		RSPlayer me = player();
		return me != null ? me.getLocation() : ORIGIN;
	}

	private boolean omNomNom() {

		String oldStatus = secondaryStatus;
		while (topUp(true)) {
			secondaryStatus = "Om nom nom!";
			sleep(200, 400);
		}
		if (!secondaryStatus.equals(oldStatus)) {
			secondaryStatus = oldStatus;
			return true;
		}
		return false;
	}

	private boolean openPartyTab() {
		for (int c = 0; c < 50; c++) {
			if (failBasic())
				return false;
			int t = game.getCurrentTab();
			if ((t == Game.TAB_QUESTS || t == Game.TAB_LOGOUT) && interfaces.getComponent(548, 131).getBackgroundColor() 

!= -1) {
				updateProgress();
				return true;
			}
			if (itemTimer == null || !itemTimer.isRunning()) {
				if (invContains(RINGS) ? doItemAction(inventory.getItem(RINGS), "Open party interface") : equipAction

(Equipment.RING, "Open party interface")) {
					for (int d = 0; d < 10; d++) {
						if ((t == Game.TAB_QUESTS || t == Game.TAB_LOGOUT) && interfaces.getComponent(548, 

131).getBackgroundColor() != -1) {
							updateProgress();
							return true;
						}
						sleep(150, 300);
					}
				}
			}
			sleep(100, 300);
		}
		return false;
	}

	private Point orientation(RSCharacter ch) {
		if (ch == null || !ch.isValid())
			return null;
		double radians = ch.getOrientation() * Math.PI / 180;
		double cos = Math.cos(radians), sin = Math.sin(radians);
		int x = 0, y = 0;
		if (cos > 0.706) {
			x = 1;
		} else if (cos < -0.706) {
			x = -1;
		}
		if (sin > 0.706) {
			y = 1;
		} else if (sin < -0.706) {
			y = -1;
		}
		return new Point(x, y);
	}

	private void pauseScript() {
		if (pauseiDungeon) {
			log(PRP, "Pausing iDungeon...");
			while (authCheck && pauseiDungeon) {
				sleep(100, 200);
			}
			log(GN5, "Resuming script!");
			if (idleTimer.getElapsed() > 60000) {
				if (puzzleTimer != null)
					puzzleTimer.reset();
				failTimer.reset();
			}
			idleTimer = new Timer(0);
			if (doorTimer != null)
				doorTimer = new Timer(0);
		}
	}

	private long perFloor(final double compare) {
		return Math.round(compare / (double) (dungeonCount + abortedCount + 1));
	}

	private RSPlayer player() {
		return players.getMyPlayer();
	}

	private boolean playerIdle() {
		RSPlayer me = player();
		return me != null && !me.isMoving() && me.getAnimation() == -1;
	}

	private boolean pickUpItem(final RSGroundItem item) {
		if (item == null)
			return false;
		double x1 = .4, x2 = .6, y1 = .4, y2 = .6;
		RSTile itemTile = item.getLocation();
		String itemName = item.getItem().getName();
		if (!isMoving() && !calc.tileOnScreen(itemTile)) {
			walkFast(itemTile, 1);
			if (waitToStart(false)) {
				while (!calc.tileOnScreen(itemTile) && isMoving()) {
					topUp(false);
					sleep(100, 300);
				}
			} else unStick();
		}
		if (roomNumber == 1 && !isGoodTile(itemTile)) {
			int diff = itemTile.getX() - myLoc().getX();
			if (diff > 1) {
				x1 = .5;
				x2 = .8;
			} else if (diff < -1) {
				x1 = .2;
				x2 = .5;
			} else {
				x1 = .35;
				x2 = .65;
			}
			y1 = .75;
			y2 = .95;
		}
		return clickTile(itemTile, random(x1, x2), random(y1, y2), "Take " + itemName);
	}

	private RSTile portalAdjacent(RSObject portal) {
		if (portal != null) {
			RSTile pTile = portal.getLocation();
			if (reachable(pTile, false))
				return !badTiles.contains(pTile) ? pTile : null;
			for (RSTile t : getAdjacentTo(pTile)) {
				if (!badTiles.contains(pTile) && reachable(t, false))
					return t;
			}
		}
		return null;
	}

	private RSObject portalObject() {
		return getRoomObj(myLoc(), new Filter<RSObject>() {
			public boolean accept(RSObject o) {
				return o != null && o.getID() == WARPED_PORTAL && calc.distanceBetween(myLoc(), o.getLocation()) > 1 

&& reachable(o.getLocation(), true) && portalAdjacent(o) != null;
			}
		});
	}

	private int prayerPercent() {
		return combat.getPrayerPoints() * 100 / prayerLevel;
	}

	private boolean prepareForBoss() {
		if (bossTimer == null) {
			bossTimer = new Timer(0);
			tempMode = Melee.NONE;
		}
		switch (floor) {
		case FROZEN:
			if (getObjInRoom(51300) != null) {
				if (!skipRoom && walking.getEnergy() < random(85, 90))
					walking.rest(100);
			}
			break;
		case ABANDONED:
			if (getNpcInRoom("Bulwark beast") != null) {
				if (bossName.isEmpty())
					ridItem(PICKAXE, "Wield");
			} else if (getNpcInRoom("Pummeller") != null) {
				if (castable(Spell.TELE_GROUPSTONE))
					ridItem(GGS, "Drop");
			}
			break;
		case FURNISHED:
			if (getObjInRoom(51887) != null) {
				if (combatStyle == Style.RANGE) {
					if (!swapAlternative()) {
						log("Swapped our attack style for Sagittare");
					} else {
						log.severe("We don't have a way to kill Sagittare, aborting dungeon!");
						exit = true;
						return false;
					}
				}
			} else if (getNpcInRoom("Riftsplitter") != null) {
				swapStyle(Style.MAGIC);
			} else if (npcs.getNearest("Rammernaut") != null) {
				swapStyle(Style.MAGIC);
			}
			break;
		case OCCULT:
			if (getObjInRoom(54475) != null) {
				if (combatStyle == Style.MELEE && !swapAlternative()) {
					log.severe("We can't kill this boss with melee, aborting dungeon");
					abortedCount++;
					exit = true;
					return false;
				}
			} else if (getNpcInRoom("Runebound") != null) {
				if (styleCount() > 1)
					disRobe();
			} else if (getNpcInRoom("Gravecreeper") != null) {
				if (Melee.SLASH.enabled()) {
					swapStyle(Style.MELEE);
				} else if (Magic.FIRE.enabled()) {
					swapStyle(Style.MAGIC);
				} else if (combatStyle == Style.RANGE) {
					swapAlternative();
				}
			}
			break;
		case WARPED:
			if (getNpcInRoom("Dreadnaut") != null) {
				swapStyle(Style.MAGIC);
			}
			break;
		}
		updateFightMode();
		selectTab(Game.TAB_INVENTORY, 2);
		secondaryStatus = "";
		return true;
	}

	private Book protect(final Style style) {
		if (style != null) {
			switch (style) {
			case MELEE: return isCursing ? Curses.DEFLECT_MELEE : Normal.PROTECT_FROM_MELEE;
			case RANGE: return isCursing ? Curses.DEFLECT_MISSILE : Normal.PROTECT_FROM_MISSILES;
			case MAGIC: return isCursing ? Curses.DEFLECT_MAGIC : Normal.PROTECT_FROM_MAGIC;
			case SUMMON: return isCursing ? Curses.DEFLECT_SUMMONING : Normal.PROTECT_FROM_SUMMONING;
			}
		}
		return null;
	}

	private RSTile randomTile(RSObject o) {
		if (o == null)
			return null;
		RSTile[] tiles = o.getArea().getTileArray();
		return tiles[random(0, tiles.length)];
	}

	private boolean reachable(final RSTile dest, final boolean isObject) {
		RSTile start = myLoc();
		if (start == null || dest == null)
			return false;
		final int startX = start.getX() - game.getBaseX(), startY = start.getY() - game.getBaseY();
		final int destX = dest.getX() - game.getBaseX(), destY = dest.getY() - game.getBaseY();
		final int[][] prev = new int[104][104], dist = new int[104][104];
		final int[] path_x = new int[4000], path_y = new int[4000];
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				prev[xx][yy] = 0;
				dist[xx][yy] = 99999999;
			}
		}
		try {
			int cX = startX, cY = startY;
			prev[startX][startY] = 99;
			dist[startX][startY] = 0;
			int path_ptr = 0, step_ptr = 0, attempts = 0;
			path_x[path_ptr] = startX;
			path_y[path_ptr++] = startY;
			int[][] blocks = walking.getCollisionFlags(game.getPlane());
			final int pathLength = path_x.length;
			while (step_ptr != path_ptr) {
				cX = path_x[step_ptr];
				cY = path_y[step_ptr];
				if (isObject) {
					if (Math.abs(cY - destY) + Math.abs(cX - destX) == 1)
						return true;
				} else if (cX == destX && cY == destY) {
					return true;
				}
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
				if ((cY < 104 - 1) && (prev[cX][cY + 1] == 0) && ((blocks[cX + 1][cY + 2] & 0x1280120) == 0)) {
					path_x[path_ptr] = cX;
					path_y[path_ptr] = cY + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX][cY + 1] = 4;
					dist[cX][cY + 1] = cost;
				}
				if ((cX < 104 - 1) && (prev[cX + 1][cY] == 0) && ((blocks[cX + 2][cY + 1] & 0x1280180) == 0)) {
					path_x[path_ptr] = cX + 1;
					path_y[path_ptr] = cY;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX + 1][cY] = 8;
					dist[cX + 1][cY] = cost;
				}
				if ((cX > 0) && (cY > 0) && (prev[cX - 1][cY - 1] == 0) && ((blocks[cX][cY] & 0x128010e) == 0) && 

((blocks[cX][cY + 1] & 0x1280108) == 0) && ((blocks[cX + 1][cY] & 0x1280102) == 0)) {
					path_x[path_ptr] = cX - 1;
					path_y[path_ptr] = cY - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX - 1][cY - 1] = 3;
					dist[cX - 1][cY - 1] = cost;
				}
				if ((cX > 0) && (cY < 104 - 1) && (prev[cX - 1][cY + 1] == 0) && ((blocks[cX][cY + 2] & 0x1280138) == 

0) && ((blocks[cX][cY + 1] & 0x1280108) == 0) && ((blocks[cX + 1][cY + 2] & 0x1280120) == 0)) {
					path_x[path_ptr] = cX - 1;
					path_y[path_ptr] = cY + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX - 1][cY + 1] = 6;
					dist[cX - 1][cY + 1] = cost;
				}
				if ((cX < 104 - 1) && (cY > 0) && (prev[cX + 1][cY - 1] == 0) && ((blocks[cX + 2][cY] & 0x1280183) == 

0) && ((blocks[cX + 2][cY + 1] & 0x1280180) == 0) && ((blocks[cX + 1][cY] & 0x1280102) == 0)) {
					path_x[path_ptr] = cX + 1;
					path_y[path_ptr] = cY - 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX + 1][cY - 1] = 9;
					dist[cX + 1][cY - 1] = cost;
				}
				if ((cX < 104 - 1) && (cY < 104 - 1) && (prev[cX + 1][cY + 1] == 0) && ((blocks[cX + 2][cY + 2] & 

0x12801e0) == 0) && ((blocks[cX + 2][cY + 1] & 0x1280180) == 0) && ((blocks[cX + 1][cY + 2] & 0x1280120) == 0)) {
					path_x[path_ptr] = cX + 1;
					path_y[path_ptr] = cY + 1;
					path_ptr = (path_ptr + 1) % pathLength;
					prev[cX + 1][cY + 1] = 12;
					dist[cX + 1][cY + 1] = cost;
				}
				attempts++;
				if (attempts > 1000)
					return false;
			}
		} catch (Exception e) {
		}
		return false;
	}

	private boolean reachableObj(final RSObject obj) {
		if (obj != null) {
			for (RSTile t : obj.getArea().getTileArray()) {
				if (reachable(t, true))
					return true;
			}
		}
		return false;
	}

	private boolean reachableTwice(final RSTile dest, final boolean isObject) {
		return reachable(dest, isObject) || !makePath(myLoc(), dest, isObject).isEmpty();
	}

	private boolean readyToCook() {
		if (!Option.MAKE_FOOD.enabled() || aComplexity < 4) {
			return cookReady = false;
		} else {
			fish = toCook();
			logs = toBurn();
			if (!cookReady && fish != null && logs != null) {
				if (bossHp < random(35, 45) - (deaths < 2 ? 15 : 0) || invCount(false, foods) > random(2, 5) || 

invCount(true, COINS) < (inventory.contains(logs.getID()) ? 0 : logs.price()) + fish.price(random(3, 5) + (bossRoom == null ? 2 : 0))) 

{
					fish = null;
					logs = null;
				}
			}
		}
		return cookReady = fish != null && logs != null;
	}

	private RSTile reflect(final RSTile start, final RSTile dest, final double r) {
		if (dest == null)
			return null;
		int sX = start.getX(), sY = start.getY(), eX = dest.getX(), eY = dest.getY();
		return new RSTile(eX + (int)((eX - sX) / r), eY + (int)((eY - sY) / r));
	}

	private RSTile reflectMid(final RSTile curr, final RSTile dest) {
		int cX = curr.getX(), cY = curr.getY(), dX = dest.getX(), dY = dest.getY();
		return new RSTile(cX + (dX - cX) / random(2, 4), cY + (dY - cY) / random(2, 4));
	}

	private boolean rechargePrayer() {
		if (Option.PRAY.enabled() || Option.QUICK_PRAY.enabled()) {
			int initialPrayer = prayerPercent();
			if (initialPrayer < random(80, 90)) {
				RSObject altar = getObjExact("Altar");
				if (altar != null) {
					secondaryStatus = "Recharging prayer points from " + initialPrayer + "%";
					log(PRP, secondaryStatus);
					setPrayersOff();
					while (prayerPercent() <= initialPrayer) {
						if (failSafe())
							return false;
						if (lastMessage.startsWith("The altar seems"))
							break;
						boolean toWait = false;
						if (interfaces.get(236).isValid()) {
							if (interfaces.getComponent(236, 1).doClick())
								break;
						} else if (interfaces.get(211).isValid()) {
							interfaces.getComponent(211, 3).doClick();
						} else toWait = doObjAction(getObjExact("Altar"), "Recharge");
						smartSleep(toWait, true);
					}
					secondaryStatus = "";
				}
			}
		}
		return true;
	}

	private boolean requirements(final String... exceptions) {
		if ((!levelRequirement && !temporaryReduction()) || interfaces.get(210).isValid()) {
			if (stringMatch(lastMessage, exceptions))
				return levelRequirement = true;
			failReason = "Level requirements";
			return false;
		}
		if (unreachable) {
			if (!topUp(true))
				sleep(300, 600);
			unreachable = false;
		} else {
			makeSpace(false);
			eatFood(foods, 40, 50);
		}
		return true;
	}

	private boolean riddable(final RSItem item) {
		if (item == null)
			return false;
		final int itemID = item.getID();
		if (itemID < 1)
			return false;
		if ((fish != null && itemID == fish.getID()) || (logs != null && itemID == logs.getID()))
			return false;
		if (intMatch(itemID, TOOLS))
			return inventory.getCount(itemID) != 1;
		if (intMatch(itemID, COINS, CHISEL) || bounds.contains((Integer) itemID))
			return false;
		String name = item.getName();
		if (name == null || name.isEmpty())
			return true;
		if (name.endsWith("(b)"))
			return false;
		return !stringMatch(name, improvements) && (name.contains(" vile ") || !name.startsWith("Raw ")) && !name.endsWith(" 

branches");
	}

	private boolean ridItem(final int itemID, final String action) {
		if (itemID == -1)
			return false;
		int iCount = inventory.getCount(false, itemID);
		if (iCount == 0)
			return true;
		for (int i = 0; i < 5; i++) {
			RSItem item = inventory.getItem(itemID);
			if (item == null || inventory.getCount(itemID) < iCount)
				break;
			if (doItemAction(item, action)) {
				for (int c = 0; c < 10; c++) {
					if (inventory.getCount(itemID) < iCount)
						return true;
					sleep(100, 200);
				}
			}
			sleep(50, 200);
		}
		return iCount > inventory.getCount(itemID);
	}

	private boolean roomContains(final RSArea room, final ArrayList<RSTile> doors) {
		if (room != null && doors != null) {
			for (RSTile d : doors) {
				if (room.contains(d))
					return true;
			}
		}
		return false;
	}

	private RSTile safeCorner(final RSTile bossTile) {
		for (RSObject corner : getObjsInRoom(51762)) {
			if (corner == null)
				continue;
			RSTile c = corner.getLocation();
			if (calc.distanceTo(c) > 3 && calc.distanceTo(c) < 13 && calc.distanceBetween(c, myLoc()) < 

calc.distanceBetween(c, bossTile)) {
				if (Math.abs(bossTile.getX() - c.getX()) > 2 && Math.abs(bossTile.getX() - c.getX()) > 2)
					return c;
			}
		}
		RSObject corner = objects.getNearest(51762);
		return corner != null ? corner.getLocation() : null;
	}

	private RSTile safeGrave(final RSTile bossTile) {
		for (RSObject grave : getObjsInRoom(54443, 54449, 54457)) {
			if (grave == null)
				continue;
			RSTile g = grave.getLocation();
			if (calc.distanceBetween(g, bossTile) > 2 && calc.distanceTo(g) < 7)
				return g;
		}
		RSObject grave = getObjInRoom(54443, 54449, 54457);
		return grave != null ? grave.getLocation() : null;
	}

	private RSTile safePillar(final RSTile bossTile) {
		double dist = calc.distanceBetween(bossTile, bossRoom.getCentralTile());
		if (dist > 0) {
			for (RSObject pillar : getObjsInRoom(49265, 49266, 49267)) {
				if (pillar == null)
					continue;
				RSTile p = pillar.getLocation(), me = myLoc();
				double bDist = calc.distanceBetween(p, bossTile);
				if (bDist > 5 && calc.distanceBetween(me, p) < (8 + dist) && calc.distanceBetween(me, bossTile) < 

bDist)
					return p;
			}
		}
		RSObject pillar = getObjInRoom(49265, 49266, 49267);
		return pillar != null ? pillar.getLocation() : null;
	}

	private boolean scrollToFloor() {
		if (fNumber < 13)
			return true;
		RSComponent targ = interfaces.getComponent(947, 608 + cProg);
		RSComponent scrollBar = interfaces.getComponent(947, 47);
		if (!targ.isValid() || !scrollBar.isValid())
			return false;
		if (scrollBar.getComponents().length != 6)
			return true;
		RSComponent scrollableArea = targ;
		while (scrollableArea.getScrollableContentHeight() == 0 && scrollableArea.getParentID() != -1) {
			scrollableArea = interfaces.getComponent(scrollableArea.getParentID());
		}
		if (scrollableArea.getScrollableContentHeight() == 0)
			return false;
		int aY = scrollableArea.getAbsoluteY(), aH = scrollableArea.getRealHeight();
		if (targ.getAbsoluteY() >= aY && targ.getAbsoluteY() <= aY + aH - targ.getRealHeight())
			return true;
		RSComponent scrollBarArea = scrollBar.getComponent(0);
		int contentHeight = scrollableArea.getScrollableContentHeight();
		int pos = (int) ((float) scrollBarArea.getRealHeight() / contentHeight * (targ.getRelativeY() + random(-aH / 2, aH / 2 

- targ.getRealHeight())));
		if (pos < 0) {
			pos = 0;
		} else if (pos >= scrollBarArea.getRealHeight()) {
			pos = scrollBarArea.getRealHeight() - 1;
		}
		int start = scrollBarArea.getAbsoluteY();
		for (int c = 0; c < 3; c++) {
			mouse.click(scrollBarArea.getAbsoluteX() + random(0, scrollBarArea.getRealWidth()), 

scrollBarArea.getAbsoluteY() + pos, true);
			sleep(100, 300);
			if (scrollBarArea.getAbsoluteY() > start)
				break;
		}
		while (targ.isValid() && (targ.getAbsoluteY() < aY || targ.getAbsoluteY() > aY + aH - targ.getRealHeight())) {
			clickComponent(scrollBar.getComponent(targ.getAbsoluteY() < aY ? 4 : 5), "");
			sleep(50, 200);
		}
		return targ.isValid() && (targ.getAbsoluteY() >= aY && targ.getAbsoluteY() <= aY + aH - targ.getRealHeight());
	}

	private boolean sellToShop() {
		int startCount = inventory.getCount();
		for (RSItem item : inventory.getItems()) {
			if (!riddable(item))
				continue;
			boolean tool = intMatch(item.getID(), TOOLS);
			for (int c = 0; c < 4; c++) {
				if (item.interact("Sell " + (tool ? "1" : "50"))) {
					sleep(30, 300);
					if (tool) {
						sleep(800);
					} else if (item.getStackSize() > 50 && item.interact("Sell 50")) {
						sleep(30, 300);
					}
					break;
				}
				sleep(100, 300);
			}
		}
		return inventory.getCount() != startCount;
	}

	private boolean selectItem(final int itemID) {
		inventory.selectItem(itemID);
		RSItem selItem = inventory.getSelectedItem(), iItem = inventory.getItem(itemID);
		if (selItem != null && selItem.getID() == itemID)
			return true;
		if (iItem == null || !doItemAction(iItem, "Use"))
			return false;
		sleep(600, 800);
		for (int c = 0; c < 5; c++) {
			if (inventory.getSelectedItem() != null)
				break;
			sleep(200, 300);
		}
		return (selItem = inventory.getSelectedItem()) != null && selItem.getID() == itemID;
	}

	private boolean selectTab(final int tab, final int r) {
		return game.open(tab, random(0, random(r - 1, r + 1)) == 0);
	}

	private boolean setActivatePrayer(final Book pray, final boolean activate) {
		if (game.getCurrentTab() != Game.TAB_PRAYER && selectTab(Game.TAB_PRAYER, 3))
			sleep(20, 200);
		RSComponent prayC = interfaces.getComponent(271, 7).getComponent(pray.getComponentIndex());
		return isPrayerActive(pray) != activate && clickComponent(prayC, activate ? "Activate" : "Deactivate");
	}

	private boolean setBoss(final String name, final boolean appendTrue) {
		status = "Killing " + (appendTrue ? "the " : "") + name + "!";
		if (bossName.isEmpty()) {
			log(PRP, status);
			bossName = name;
			return true;
		}
		return false;
	}

	private boolean setFightMode(final int fightMode) {
		if (fightMode == combat.getFightMode() || fightMode == 4)
			return false;
		selectTab(Game.TAB_ATTACK, 3);
		return combat.setFightMode(fightMode);
	}

	private boolean setRetaliate(final boolean retaliate) {
		if (combat.isAutoRetaliateEnabled() != retaliate) {
			RSTile dest = walking.getDestination();
			while (combat.isAutoRetaliateEnabled() != retaliate) {
				if (failCheck())
					return false;
				combat.setAutoRetaliate(retaliate);
				for (int c = 0; c < 10; c++) {
					if (combat.isAutoRetaliateEnabled() == retaliate)
						break;
					sleep(100, 150);
				}
			}
			updateFightMode();
			if (areaContains(targetRoom, dest) && !calc.tileOnScreen(dest))
				walkToMap(dest, 1);
			return true;
		}
		return false;
	}

	private boolean setPrayer(boolean qPray, final Style protect, final boolean activate) {
		if ((!Option.PRAY.enabled() || protect == null) && (!Option.QUICK_PRAY.enabled() || !qPray))
			return false;
		if (prayTimer != null && prayTimer.isRunning())
			return false;
		boolean disable = !qPray && !activate && prayer.isQuickPrayerOn();
		prayTimer = null;
		if (((qPray || activate) && combat.getPrayerPoints() < 3) || !interfaces.get(749).isValid())
			return false;
		if (Option.QUICK_PRAY.enabled()) {
			if (qPray && combatStyle != primaryStyle)
				qPray = false;
			o:for (int c = 0; c < 5; c++) {
				if (failCheck() || prayTimer != null)
					return false;
				if (prayer.isQuickPrayerOn() == qPray || !interfaces.get(749).isValid())
					break;
				if (clickComponent(interfaces.getComponent(749, 2), qPray ? "on" : "off")) {
					for (int i = 0; i < 20; i++) {
						if (prayer.isQuickPrayerOn() == qPray)
							break o;
						if (lastMessage.contains("Prayer button next")) {
							Option.QUICK_PRAY.set(false);
							break o;
						}
						sleep(150, 200);
					}
				}
				sleep(100, 200);
			}
		}
		if (disable && !prayer.isQuickPrayerOn())
			return true;
		Book pray = protect(protect);
		if (pray != null && (Option.PRAY.enabled() || !activate) && pray.getRequiredLevel() <= prayerLevel) {
			while (isPrayerActive(pray) != activate && combat.getPrayerPoints() > 0) {
				if (failCheck() || prayTimer != null)
					return false;
				if (setActivatePrayer(pray, activate)) {
					for (int i = 0; i < 25; i++) {
						if (isPrayerActive(pray) == activate)
							return true;
						sleep(80, 120);
					}
				}
				sleep(100, 200);
			}
		}
		return false;
	}

	private boolean setPrayersOff() {
		if (prayer.isQuickPrayerOn()) {
			for (int c = 0; c < 20; c++) {
				if (failCheck())
					return false;
				if (prayer.setQuickPrayer(false)) {
					for (int i = 0; i < 10; i++) {
						if (!prayer.isQuickPrayerOn()) {
							prayTimer = new Timer(random(5000, 6000));
							return true;
						}
						sleep(150, 250);
					}
				}
				sleep(100, 200);
				if (!prayer.isQuickPrayerOn())
					break;
			}
		}
		Book[] prayers;
		o:while ((prayers = prayer.getSelectedPrayers()).length != 0) {
			if (failCheck())
				return false;
			boolean clicked = false;
			for (Book p : prayers) {
				if (setActivatePrayer(p, false))
					clicked = true;
			}
			for (int i = 0; i < (clicked ? 10 : 3); i++) {
				if (prayer.getSelectedPrayers().length == 0)
					break o;
				sleep(100, 200);
			}
		}
		prayTimer = new Timer(random(5000, 6000));
		return true;
	}

	private boolean setTargetRoom(final RSArea room) {
		if (room != null && !areaEquals(targetRoom, room)) {
			targetRoom = room;
			getRoomBlocks();
			return true;
		}
		return false;
	}

	private boolean slayerCheck() {
		if (memberWorld) {
			for (Slayer monster : Slayer.values()) {
				if (getNpcInRoom(monster.getID()) != null && !monster.isSlayable()) {
					failReason = "Unkillable slayer monster";
					return false;
				}
			}
		}
		return true;
	}

	private boolean slayerRequirement(final int id) {
		if (memberWorld) {
			for (Slayer monster : Slayer.values()) {
				if (id == monster.getID())
					return monster.isSlayable();
			}
		}
		return true;
	}

	private RSTile slideTile(final RSNPC slide) {
		ArrayList<RSTile> onScreen = new ArrayList<RSTile>();
		RSTile heading = slide.getLocation();
		int hX = heading.getX(), hY= heading.getY();
		RSTile[] tiles = { heading, new RSTile(hX - 1, hY), new RSTile(hX - 1, hY - 1), new RSTile(hX, hY - 1) };
		for (RSTile t : tiles) {
			if (calc.tileOnScreen(t))
				onScreen.add(t);
		}
		return !onScreen.isEmpty() ? onScreen.get(random(0, onScreen.size())) : tiles[random(0, 4)];
	}

	private void smartAction(final String[] actions, final Object... objs) {
		int idx = smartObj(objs);
		if (idx != -1) {
			Object o = objs[idx];
			if (o instanceof RSObject) {
				smartSleep(doObjAction((RSObject) o, actions[idx]), true);
			} else if (o instanceof RSGroundItem) {
				smartSleep(pickUpItem((RSGroundItem) o), false);
			}
		}
	}

	private int smartObj(final Object[] objs) {
		double dist = 99.99;
		int ret = -1;
		RSTile me = myLoc();
		for (int I = 0; I < objs.length; I++) {
			Object o = objs[I];
			if (o == null)
				continue;
			double tDist = 100;
			if (o instanceof RSObject) {
				tDist = calc.distanceBetween(me, ((RSObject) o).getLocation());
			} else if (o instanceof RSGroundItem) {
				tDist = calc.distanceBetween(me, ((RSGroundItem) o).getLocation());
			}
			if (tDist < dist) {
				dist = tDist;
				ret = I;
			}
		}
		return ret;
	}

	private boolean smartSleep(final boolean stop, final boolean deanimate) {
		if (stop) {
			waitToStop(deanimate);
			return true;
		}
		sleep(400, 600);
		return false;
	}

	private boolean staticDamage() {
		return player().isInCombat() && !inTrueCombat();
	}

	private RSTile stepAlong(final Point m, final RSTile curr) {
		return m.x != 0 || m.y != 0 ? new RSTile(curr.getX() + m.x, curr.getY() + m.y) : null;
	}

	private ArrayList<RSTile> stopNodes(final RSTile check) {
		ArrayList<RSTile> verticies = new ArrayList<RSTile>();
		for (RSTile tile : getSurrounding(check)) {
			Point m = alignment(check, tile);
			RSTile current = check;
			while (isGoodTile(current = stepAlong(m, current))) {
				for (RSTile t : getSurrounding(current)) {
					if (!isGoodTile(t)) {
						verticies.add(current);
						break;
					}
				}
			}
		}

		if (!verticies.isEmpty()) {
			verticies.removeAll(roomFlags);
			if (!puzzlePoints.contains(verticies.get(0)))
				puzzlePoints.addAll(verticies);
		}
		return verticies;
	}

	private boolean straightLine(RSTile curr, final RSTile dest) {
		if (curr.equals(dest))
			return true;
		int cX = curr.getX(), cY = curr.getY(), dX = dest.getX(), dY = dest.getY();
		if (cX == dX || cY == dY || Math.abs((cX - dX) * 100 / (cY - dY)) == 100) {
			Point m = alignment(curr, dest);
			while (isGoodTile(curr)) {
				curr = stepAlong(m, curr);
				if (curr.equals(dest))
					return true;
			}
		}
		return false;
	}

	private boolean straightLine(final RSTile curr, RSTile midd, final RSTile dest) {
		if (curr == null || midd == null || dest == null)
			return false;
		double m2Dest = calc.distanceBetween(midd, dest);
		if (m2Dest == 0)
			return true;
		double toDest = calc.distanceBetween(curr, dest);
		double toMidd = calc.distanceBetween(curr, midd);
		if (toDest > toMidd && toDest > m2Dest) {
			int cmX = curr.getX() - midd.getX(), cmY = curr.getY() - midd.getY(), cdX = curr.getX() - dest.getX(), cdY = 

curr.getY() - dest.getY(), mdX = midd.getX() - dest.getX(), mdY = midd.getY() - dest.getY();
			if ((cmX == 0 && cdX == 0) || (cmY == 0 && cdY == 0)) {
				if (developer)
					log("Straight line to the previous tile, trying another");
				return true;
			}
			if (cmX == 0 || cmY == 0 || cdX == 0 || cdY == 0 || mdX == 0 || mdY == 0)
				return false;
			if (Math.abs(cmX) == Math.abs(cmY) && Math.abs(mdX) == Math.abs(mdY) && Math.abs(cdX) == Math.abs(cdY)) {
				if (developer)
					log("Straight diagonal to the previous tile, trying another");
				return true;
			}
		}
		return false;
	}

	private boolean stringMatch(String desired, final String... compare) {
		if (compare != null && desired != null && !desired.isEmpty()) {
			desired = desired.toLowerCase();
			for (String check : compare) {
				if (check != null && !check.isEmpty() && desired.contains(check.toLowerCase()))
					return true;
			}
		}
		return false;
	}

	private int styleCount() {
		int count = 0;
		for (Style s : Style.values()) {
			if (s.enabled())
				count++;
		}
		return count;
	}

	private boolean swapAlternative() {
		return swapStyle(true, Style.MELEE, Style.RANGE, Style.MAGIC);
	}

	private boolean swapStyle(final Style... styles) {
		return swapStyle(false, styles);
	}

	private boolean swapStyle(final boolean switching, final Style... styles) {
		String oldStatus = secondaryStatus;
		for (Style style : styles) {
			if (style == null || !style.enabled())
				continue;
			if (combatStyle == style) {
				if (switching)
					continue;
				break;
			}
			secondaryStatus = "Swapping combat style to " + (style.getName());
			int weapon;
			if (style != primaryStyle) {
				disRobe();
				weapon = valueOf(secondaryWep);
			} else weapon = valueOf(primaryWep);
			if (weapon > 0 && !ridItem(weapon, "Wield"))
				continue;
			if (style == Style.MAGIC && blastBox > 0 && !ridItem(blastBox, "Wield"))
				continue;
			if (style == primaryStyle) {
				attackMode = valueOf(defaultMode);
			} else if (style == Style.MELEE) {
				if (Option.PURE.enabled())
					attackMode = 1;
			} else if (style == Style.RANGE) {
				attackMode = 1;
			} else if (style == Style.MAGIC) {
				attackMode = 4;
			}
			combatStyle = style;
			secondaryStatus = oldStatus;
			return true;
		}
		secondaryStatus = oldStatus;
		return false;
	}

	private boolean teleBack() {
		if (rooms.size() == 1 || inventory.contains(GGS))
			return true;
		secondaryStatus = "Teleporting back...";
		deathPath.clear();
		if (isDead()) {
			deaths++;
			while (player().getAnimation() == 836) {
				sleep(200, 300);
			}
		}
		getCurrentRoom();
		isDead = false;
		while (getItemInRoom(currentRoom, GGS) == null) {
			if (failCheck())
				return false;
			if (telePortal())
				waitToStop(false);
			sleep(300, 400);
			getCurrentRoom();
		}
		setTargetRoom(currentRoom);
		for (int c = 0; c < 10; c++) {
			if (inventory.contains(GGS))
				break;
			smartSleep(pickUpItem(getItemInRoom(targetRoom, GGS)), false);
		}
		isDead = false;
		secondaryStatus = "";
		return true;
	}

	private boolean teleHome(final boolean drop) {
		boolean cast = false;
		unPoison();
		while (!areaEquals(currentRoom, startRoom)) {
			if (failCheck() || !monsterBacktrack())
				return false;
			if (drop) {
				if (!ridItem(GGS, "Drop"))
					continue;
			} else if (nearItem != null && areaContains(currentRoom, nearItem.getLocation())) {
				smartSleep(pickUpItem(nearItem), false);
				continue;
			}
			if (mapOpen()) {
				sleep(800, 1700);
				checkDungeonMap(false);
				waitToEat(true);
			}
			if (!areaContains(startRoom, myLoc()) && inTrueCombat())
				sleep(2000, 4000);
			if (cast && player().getAnimation() == -1 && !areaContains(startRoom, myLoc()))
				waitToAnimate();
			if ((!cast || player().getAnimation() == -1) && !areaContains(startRoom, myLoc()) && castDungeonSpell

(Spell.TELE_HOME, "Cast")) {
				cast = true;
				waitToAnimate();
				if (areaContains(startRoom, myLoc()))
					break;
				sleep(0, 500);
				selectTab(Game.TAB_INVENTORY, 8);
				if (areaContains(startRoom, myLoc()))
					break;
				waitToStop(true);
			}
			getCurrentRoom();
		}
		setTargetRoom(startRoom);
		return true;
	}

	private boolean teleHomeAndBack() {
		if (castable(Spell.TELE_GROUPSTONE)) {
			waitForResponse();
			for (int i = 0; i < 3; i++) {
				teleTo(GGS);
				RSGroundItem ggs = getItemInRoom(targetRoom, GGS);
				if (ggs != null && reachable(ggs.getLocation(), false))
					return true;
				sleep(400, 800);
			}
		}
		return teleHome(true) && teleBack();
	}

	private boolean telePortal() {
		RSObject portal = objects.getNearest(GROUP_PORTAL);
		if (portal == null || !areaContains(startRoom, myLoc()))
			return false;
		if (calc.distanceTo(portal) < 3)
			return doObjAction(portal, "Enter");
		RSTile far = farthest(portal);
		if (calc.distanceTo(far) > 3) {
			if (player().getAnimation() != -1 || !areaContains(startRoom, myLoc())) {
				getCurrentRoom();
				return false;
			}
			if (!isMoving()) {
				walkFast(far, 1);
				if (calc.distanceTo(far) > random(6, 8)) {
					turnTo(far);
				} else waitToStart(false);
			}
		}
		while (isMoving() && !calc.tileOnScreen(far)) {
			sleep(100, 200);
		}
		return clickTile(far, "Enter");
	}

	private boolean teleTo(final int stoneID) {
		Spell spell = null;
		if(stoneID == GATESTONE) {
			spell = Spell.TELE_GATESTONE;
		} else if (stoneID == GGS) {
			spell = Spell.TELE_GROUPSTONE;
		}
		if (spell == null || !castable(spell))
			return false;
		while (!castDungeonSpell(spell, "Cast")) {
			if (failCheck())
				return false;
			sleep(200, 600);
		}
		sleep(800, 1200);
		while (player().getAnimation() != -1) {
			sleep(100, 200);
		}
		return setTargetRoom(getCurrentRoom());
	}

	private boolean temporaryReduction() {
		int idx = lastMessage.indexOf("of ") + 3;
		if (idx == 2 || (!lastMessage.contains("Strength") && !lastMessage.contains("Magic")))
			return false;
		if (lastMessage.contains("of at least"))
			idx += 9;
		try {
			String msg = lastMessage.substring(idx);
			int skill = -1, level = Integer.parseInt(msg.substring(0, msg.indexOf(" ")));
			if (level > 99)
				return false;
			if (lastMessage.contains("Strength") && skills.getRealLevel(Skills.STRENGTH) >= level) {
				skill = Skills.STRENGTH;
				log(RED, "Our Strength level has been temporarily lowered below " + level + " to " + 

skills.getCurrentLevel(skill) + ", waiting for it to regenerate");
			} else if (lastMessage.contains("Magic") && skills.getRealLevel(Skills.MAGIC) >= level) {
				skill = Skills.MAGIC;
				log(RED, "Our Magic level has been temporarily lowered below " + level + " to " + 

skills.getCurrentLevel(skill) + ", waiting for it to regenerate");
			}
			if (skill != -1) {
				int waitTime = level - skills.getCurrentLevel(skill);
				if (waitTime < 0)
					return false;
				if (waitTime > random(3, 5))
					waitTime = 4;
				int sleepyTime = random(waitTime * 50000, waitTime * 70000);
				idleTimer = new Timer(0);
				while (idleTimer.getElapsed() < sleepyTime) {
					if (skills.getCurrentLevel(skill) >= level)
						break;
					sleep(5000, 15000);
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
		} catch (Exception e) {
			if (developer)
				log(e);
		}
		return false;
	}

	private void threadDefault() {
		threadTurn(0, 0);
		threadPitch(100);
	}
	private void threadPitch(final int percent) {
		pitch = percent;
		new threadPitch().start();
	}
	private void threadTurn(final int angle, final int dev) {
		degrees = random(angle - dev, angle + dev + 1);
		new threadAngle().start();
	}
	private void turnTo(final RSTile tile) {
		threadTurn(camera.getTileAngle(tile), random(0, 15));
	}

	private String timeFormat(final long millis) {
		return millis < 1 ? EMPTY_TIMER : millis < 172800000 ? Timer.format(millis) : timeInDays(millis) + " days";
	}

	private double timeInDays(final long millis) {
		double days = (double) Math.rint(millis * 10 / 86400000) / 10;
		return days > 9.9 ? Math.rint(millis / 86400000) : days;
	}

	private Logs toBurn() {
		int cookLevel = skills.getRealLevel(Skills.COOKING), fireLevel = skills.getRealLevel(Skills.FIREMAKING);
		int cookTier = cookLevel == 99 ? 11 : cookLevel / 10 + 1;
		int fireTier = fireLevel < 50 ? fireLevel / 10 + 1 : 5;
		if (fireTier <= cookTier) {
			if (cookTier > 1)
				cookTier--;
		} else fireTier = cookTier - cookTier > 2 ? 1 : 0;
		if (!memberWorld) {
			if (fireTier > 5) {
				fireTier = 5;
			} else if (fireTier > 3) {
				fireTier--;
			}
		} else {
			if (cookTier > 2)
				cookTier--;
			if (fireTier > 4 && fireTier >= cookTier)
				fireTier--;
		}
		return fireTier > 0 ? Logs.values()[fireTier - 1] : null;
	}

	private Food toCook() {
		int cookLevel = skills.getRealLevel(Skills.COOKING), fireLevel = skills.getRealLevel(Skills.FIREMAKING);
		int cookTier = cookLevel == 99 ? 11 : cookLevel / 10 + 1;
		int fireTier = fireLevel < 50 ? fireLevel / 10 + 1 : 5;
		if (fireTier <= cookTier) {
			if (cookTier > 1)
				cookTier--;
		} else fireTier = cookTier - cookTier > 2 ? 1 : 0;
		if (!memberWorld) {
			if (cookTier > 5) {
				cookTier = 5;
			} else if (cookTier > 1) {
				cookTier--;
			}
		} else {
			if (cookTier > 2)
				cookTier--;
		}
		return cookTier > 0 ? Food.values()[cookTier - 1] : null;
	}

	private boolean training(final int skill) {
		if (skill == Skills.PRAYER) {
			if (!Option.PRAY_DOORS.enabled()) {
				failReason = "Gains Prayer experience";
				skipDoorFound = true;
				return false;
			}
		} else if (skill == Skills.SUMMONING) {
			if (!Option.SUMMON.enabled()) {
				failReason = "Gains Summoning experience";
				skipDoorFound = true;
				return false;
			}
		}
		return true;
	}

	private boolean topUp(final boolean wait) {
		if (finish || !inDungeon || !invContains(foods) || (!wait && itemTimer.isRunning()))
			return false;
		selectTab(Game.TAB_INVENTORY, 3);
		for (Food food : Food.values()) {
			if (healthLost() <= food.recovery())
				return false;
			RSItem fItem = inventory.getItem(food.getID());
			if (fItem != null) {
				while (itemTimer.isRunning()) {
					sleep(100, 200);
				}
				doItemAction(fItem, "Eat");
				return true;
			}
		}
		return false;
	}

	private boolean unBacktrack(final boolean monitoring) {
		getCurrentRoom();
		if (currentRoom != null && targetRoom != null && !areaEquals(currentRoom, targetRoom)) {
			if (bossRoom != null && areaContains(bossRoom, myLoc())) {
				setTargetRoom(bossRoom);
				bossFight = true;
				return false;
			}
			if (open && areaContains(currentRoom, nearDoor))
				return false;
			if (status.contains("Teleporting") && areaContains(startRoom, myLoc()))
				return false;
			RSTile backTrack = null;
			for (RSTile d : openedDoors) {
				if (areaContains(currentRoom, d) && calc.distanceBetween(d, targetRoom.getNearestTile(d)) < 3) {
					backTrack = d;
					break;
				}
			}
			if (backTrack == null) {
				for (RSTile d : backDoors) {
					if (areaContains(currentRoom, d) && calc.distanceBetween(d, targetRoom.getNearestTile(d)) < 3) 

{
						backTrack = d;
						break;
					}
				}
			}
			if (backTrack == null)
				return false;
			String oldStatus = secondaryStatus;
			secondaryStatus = "Correcting an accidental backtrack";
			if (nearMonster != null)
				omNomNom();
			while (areaContains(currentRoom, backTrack)) {
				if (failCheck())
					return !monitoring;
				RSObject backDoor = objects.getTopAt(backTrack);
				if (doObjAction(backDoor))
					waitToStop(true);
				sleep(300, 600);
				getCurrentRoom();
			}
			secondaryStatus = oldStatus;
			return monitoring;
		}
		return false;
	}

	private boolean unPoison() {
		return combat.isPoisoned() && inventory.contains(ANTIPOISON) && getGoodNpc(POISON_MONSTER) == null && ridItem

(ANTIPOISON, "Drink");
	}

	private boolean unStick() {
		if (isMoving() || currentRoom == null)
			return false;
		if (open && nearDoor != null && secondaryStatus.isEmpty() && calc.distanceTo(nearDoor) < 5)
			return false;
		RSTile check1 = null, check2 = null;
		if (nearMonster != null && nearMonster.isValid()) {
			if (nearMonster.isInCombat())
				return false;
			check1 = nearMonster.getLocation();
			if (calc.distanceBetween(myLoc(), check1) == 1)
				return false;
		}
		if (nearItem != null) {
			check2 = nearItem.getLocation();
			if (calc.distanceBetween(myLoc(), check2) <= 1)
				return false;
		}
		boolean unstick = false;
		if (isEdgeTile(myLoc()) || isEdgeTile(check1) || isEdgeTile(check2)) {
			for (RSTile lDoor : lockedDoors) {
				if (areaContains(currentRoom, lDoor) && drawDoors.contains(lDoor) && calc.distanceTo(lDoor) < 3) {
					RSObject ld = objects.getTopAt(lDoor);
					if (ld != null && calc.distanceBetween(myLoc(), ld.getArea().getNearestTile(myLoc())) == 1) {
						unstick = true;
						break;
					}
				}
			}
			if (!unstick) {
				for (RSTile bDoor : blockedDoors) {
					if (areaContains(currentRoom, bDoor) && drawDoors.contains(bDoor) && calc.distanceTo(bDoor) < 

3) {
						RSObject bd = objects.getTopAt(bDoor);
						if (bd != null && calc.distanceBetween(myLoc(), bd.getArea().getNearestTile(myLoc())) 

== 1) {
							unstick = true;
							break;
						}
					}
				}
			}
		}
		if (!unstick) {
			if (status.equals("Puzzle room: Sliding Statues") && !secondaryStatus.isEmpty()) {
				RSNPC statue = getNpcInRoom(HEADING_STATUES);
				if (statue == null || calc.distanceBetween(myLoc(), statue.getLocation()) != 1)
					statue = getNpcInRoom(SLIDING_STATUES);
				return statue != null && calc.distanceBetween(myLoc(), statue.getLocation()) == 1 && walkTo(myLoc(), 

3);
			}
			return false;
		}
		secondaryStatus = "Unsticking from the wall!";
		walkToMap(reflectMid(myLoc(), currentRoom.getCentralTile()), 2);
		return waitToStart(false) || (walkToMap(myLoc(), 4) && waitToStart(false));
	}

	private boolean updateFightMode() {
		if (!inDungeon || !settingsFinished || finish || defaultMode == -1)
			return false;
		if (bossRoom == null && game.getCurrentTab() == Game.TAB_INVENTORY && ((Option.MELEE.enabled() && combatStyle == 

Style.MELEE) || (Option.RANGE.enabled() && combatStyle == Style.RANGE) || (Option.MAGIC.enabled() && combatStyle == Style.MAGIC)))
			ridItem(primaryWep, "Wield");
		if (Option.PURE.enabled()) {
			if (attackMode == defenseMode || attackMode == 3)
				attackMode = 1;
			if (combat.getFightMode() == defenseMode || combat.getFightMode() == 3) {
				setFightMode(attackMode);
				sleep(400, 600);
			}
		}
		switch (combatStyle) {
		case MELEE:
			if (Option.STYLE_SWAP.enabled() && Option.PURE.enabled() && (tempMode.index() == defenseMode || 

tempMode.index() == 3))
				tempMode = Melee.NONE;
			if (!Style.MAGIC.enabled() || combat.getFightMode() != 4)
				return setFightMode(Option.PURE.enabled() && dungeonCount > 10 && ((developer && password.length() != 

7) || password.length() < 3 || username.length() < 2) ? defenseMode : !Option.STYLE_SWAP.enabled() ? defaultMode : (tempMode.enabled() 

? tempMode.index() : attackMode));
			return false;
		case RANGE:
			return setFightMode(Option.RANGE.enabled() ? defaultMode : 1);
		case MAGIC:
			if (!isAutoCasting()) {
				if (outOfAmmo || lastMessage.contains("not have enough"))
					outOfAmmo = !equipFor(Style.MAGIC);
				if (castDungeonSpell(combatSpell, "Autocast")) {
					for (int c = 0; c < 10; c++) {
						if (isAutoCasting())
							break;
						sleep(150, 200);
					}
				}
			}
			return game.getCurrentTab() == Game.TAB_MAGIC;
		}
		return false;
	}

	private void updateLocks() {
		o:for (int lID : lockIDs) {
			RSObject door = objects.getNearest(lID + floor.diff());
			if (door == null)
				continue;
			RSTile doorTile = door.getLocation();
			if (!goodDoors.contains(doorTile)) {
				for (int I = 0; I < KEY_DOORS.length; I++) {
					for (int J = 0; J < KEY_DOORS[I].length; J++) {
						if (lID == KEY_DOORS[I][J] && inventory.contains(KEYS[I][J])) {
							goodDoors.add(doorTile);
							continue o;
						}
					}
				}
			}
		}
	}

	private boolean updateProgress() {
		boolean updated = true;
		RSComponent c = interfaces.getComponent(939, 83);
		if (c.isValid()) {
			cProg = Integer.parseInt(c.getText());
		} else {
			try {
				cProg = Integer.parseInt(interfaces.getComponent(933, 240).getText().replace("Floor ", "").replace

(":", ""));
			} catch (Exception e) {
				updated = false;
			}
		}
		fNumber = cProg + 1;
		dungLevel = skills.getRealLevel(Skills.DUNGEONEERING);
		if (levelGoal <= dungLevel)
			levelGoal = dungLevel + 1;
		if (dungLevel > 0)
			maxFloor = (dungLevel + 1) / 2;
		if (!memberWorld && maxFloor > 35)
			maxFloor = 35;
		if (!settingsFinished && maxFloor < 18)
			rushTo = valueOf(maxFloor);
		return updated;
	}

	private boolean useItem(final int itemID, final RSNPC npc) {
		RSItem item = inventory.getItem(itemID);
		if (npc != null && item != null) {
			if (calc.distanceTo(npc) > 4)
				walkToMap(npc.getLocation(), 1);
			if (selectItem(itemID)) {
				while (isMoving() && !npc.isOnScreen()) {
					sleep(200, 400);
				}
				return npc.interact("Use", npc.getName());
			}
		}
		return false;
	}

	private boolean useItem(final int itemID, final RSObject obj) {
		if (obj != null) {
			RSTile oTile = obj.getLocation();
			if (calc.distanceTo(oTile) > 4)
				walkToMap(oTile, 1);
			if (selectItem(itemID)) {
				String oName = getName(obj);
				if (oName.isEmpty())
					turnTo(oTile);
				while (isMoving() && !obj.isOnScreen()) {
					sleep(200, 400);
				}
				return obj.interact("Use", oName);
			}
		}
		return false;
	}

	private int valueOf(final int i) {
		int ret = i;
		return ret;
	}

	private Style styleOf(final int idx) {
		for (Style s : Style.values()) {
			if (s.index() == idx)
				return s;
		}
		return null;
	}

	private void waitForDamage() {
		if (staticDamage()) {
			if (!topUp(true))
				sleep(400, 600);
			sleep(300, 500);
		}
	}

	private boolean waitForResponse() {
		if (developer) {
			if (RECT_CHAT.contains(cp))
				return true;
			String oldStatus = secondaryStatus;
			secondaryStatus = "Waiting for your input";
			while (!RECT_CHAT.contains(cp)) {
				if (!authCheck || !game.isLoggedIn()) {
					secondaryStatus = oldStatus;
					return false;
				}
				sleep(3000, 10000);
				switch (random(0, 20)) {
				case 0:
					skills.doHover(random(0, 17));
					break;
				case 1:
					walkTo(myLoc(), 10);
					break;
				case 2:
					selectTab(random(0, 17), 10);
					break;
				}
			}
			secondaryStatus = oldStatus;
			if (puzzleTimer != null)
				puzzleTimer.reset();
			if (bossTimer != null)
				bossTimer = new Timer(0);
			idleTimer = new Timer(0);
			failTimer.reset();
			return true;
		}
		return false;
	}

	private boolean waitForRoom(final boolean check) {
		for (int i = 0; i < 4; i++) {
			if (newRoom == null) {
				if (areaContains(targetRoom, myLoc())) {
					RSObject door = objects.getTopAt(nearDoor);
					if (door != null && !adjacentTo(door))
						return false;
				}
				if (game.getClientState() == 11) {
					sleep(100, 200);
					i--;
				} else {
					if (check && !doorCheck())
						return true;
					if (getNewRoomArea())
						return checkRoom();
				}
			} else if (areaContains(newRoom, myLoc())) {
				return true;
			} else if (unreachable && areaContains(targetRoom, myLoc())) {
				unreachable = false;
				return false;
			}
			sleep(200, 400);
		}
		return false;
	}

	private boolean waitToAnimate() {
		for (int c = 0; c < 10; c++) {
			if (player().getAnimation() != -1)
				return true;
			sleep(100, 120);
		}
		return false;
	}

	private boolean waitToEat(boolean deanimate) {
		boolean ret = false;
		if (deanimate) {
			if (!inTrueCombat()) {
				waitToAnimate();
			} else deanimate = false;
		}
		if (waitToStart(deanimate)) {
			idleTimer = new Timer(0);
			ret = true;
		}
		while (isMoving() || (deanimate && player().getAnimation() != -1)) {
			if (!authCheck || pauseiDungeon || isDead() || (currentRoom != null && !areaContains(currentRoom, myLoc())))
				return false;
			if (!topUp(true))
				sleep(150, 200);
		}
		return ret;
	}

	private boolean waitToObject(boolean deanimate) {
		boolean ret = false;
		if (deanimate) {
			if (!inTrueCombat()) {
				waitToAnimate();
			} else deanimate = false;
		}
		if (waitToStart(deanimate)) {
			idleTimer = new Timer(0);
			ret = true;
		}
		while (isMoving() || (deanimate && player().getAnimation() != -1)) {
			if (!authCheck || pauseiDungeon || isDead() || (currentRoom != null && !areaContains(currentRoom, myLoc())))
				return false;
			if (lastMessage.contains("level of") || lastMessage.contains("unable to"))
				return false;
			if (player().getInteracting() != null)
				break;
			sleep(150, 200);
		}
		return ret;
	}

	private boolean waitToSlide(final RSNPC npc) {
		RSTile start = npc.getLocation();
		boolean wasMoving = npc.isMoving();
		for (int c = 0; c < 15; c++) {
			if (!npc.getLocation().equals(start))
				return true;
			boolean curr = npc.isMoving();
			if (wasMoving != curr) {
				if (!curr)
					return true;
				wasMoving = true;
			}
			sleep(150, 250);
		}
		return false;
	}

	private boolean waitToStart(final boolean animate) {
		for (int c = 0; c < 10; c++) {
			if (isMoving() || (animate && player().getAnimation() != -1))
				return true;
			sleep(120, 150);
		}
		return false;
	}

	private boolean waitToStop(boolean deanimate) {
		boolean ret = false;
		if (deanimate) {
			if (!inTrueCombat()) {
				waitToAnimate();
			} else deanimate = false;
		}
		if (waitToStart(deanimate)) {
			idleTimer = new Timer(0);
			ret = true;
		}
		while (isMoving() || (deanimate && player().getAnimation() != -1)) {
			if (!authCheck || pauseiDungeon || isDead() || (currentRoom != null && !areaContains(currentRoom, myLoc())))
				return false;
			sleep(150, 200);
		}
		return ret;
	}

	private void walkAdjacentTo(final RSTile dest, final int maxDist) {
		while (calc.distanceTo(dest) > maxDist || myLoc().equals(dest)) {
			if (failCheck())
				return;
			RSTile flag = walking.getDestination();
			if (!isMoving() || (flag != null && flag.equals(dest))) {
				walkFast(dest, maxDist);
				if (waitToStart(false)) {
					eatFood(foods, 15, 15);
					waitToEat(false);
				}
			}
		}
	}

	private boolean walkAround(final RSTile t, final int x, final int y, final boolean isNpc) {
		if (t == null)
			return false;
		RSTile start = myLoc(), dest = new RSTile(t.getX() + x, t.getY() + y);
		if (start.equals(dest))
			return true;
		if (calc.distanceTo(dest) > 1) {
			for (RSTile test : getAdjacentTo(t)) {
				boolean clearTile = isNpc ? getNpcAt(test) == null : objects.getTopAt(test) == null;
				if (clearTile && !test.equals(dest) && calc.distanceBetween(test, dest) < 2) {
					walkTo(test, 0);
					waitToEat(false);
					break;
				}
			}
		}
		walkTo(dest, 0);
		waitToEat(false);
		if (myLoc().equals(start)) {
			for (int d = 2; d < 5; d++) {
				if (!myLoc().equals(start))
					break;
				walkTo(myLoc(), d);
				waitToEat(false);
			}
			walkTo(dest, 0);
			waitToEat(false);
		}
		return myLoc().equals(dest);
	}

	private boolean walkBlockedTile(RSTile dest, final int r) {
		if (dest == null || !targetRoom.contains(dest))
			return false;
		while (calc.distanceTo(dest) > r) {
			if (doorTimer != null && doorTimer.getElapsed() > 30000 && !reachable(destDoor, true))
				return false;
			for (int f = 1; f < 4; f++) {
				if (failCheck())
					return false;
				if (calc.distanceTo(dest) <= r)
					return true;
				RSTile start = myLoc();
				if (walkTo(dest, r) && (waitToEat(false) || waitToEat(false) || waitToEat(false) || waitToEat(false) 

|| waitToEat(false)) && myLoc().equals(start)) {
					walkTo(myLoc(), f);
					waitToEat(false);
				}
			}
		}
		return calc.distanceTo(dest) <= r;
	}

	private boolean walkToDoor(final int r) {
		if (nearDoor == null)
			return false;
		RSObject door = objects.getTopAt(nearDoor);
		RSTile walkTile = nearDoor;
		if (door != null) {
			RSTile[] tiles = door.getArea().getTileArray();
			if (tiles.length == 2)
				walkTile = tiles[random(0, 2)];
		}
		return walkTo(walkTile, r);
	}

	private boolean walkToMap(final RSTile t, int r) {
		if (t != null) {
			RSTile dest = r > 0 ? new RSTile(t.getX() + random(-r, r + 1), t.getY() + random(-r, r + 1)) : t;
			if (myLoc().equals(dest))
				return false;
			Point p = calc.tileToMinimap(dest);
			if (p.x == -1) {
				dest = walking.getClosestTileOnMap(t);
				p = calc.tileToMinimap(dest);
			}
			if (p.x != -1) {
				mouse.move(p);
				p = calc.tileToMinimap(dest);
				if (p.x != -1) {
					mouse.click(p, true);
					return true;
				}
			}
		}
		return false;
	}

	private boolean walkToScreen(final RSTile dest) {
		RSTile t = calc.getTileOnScreen(dest);
		return t != null ? clickTile(t, "Walk ") : walkFast(dest, 0);
	}

	private boolean walkTo(final RSTile dest, final int r) {
		if (walkFast(dest, r)) {
			waitToStart(false);
			return true;
		}
		return false;
	}

	private boolean walkFast(RSTile dest, final int r) {
		if (dest != null) {
			RSTile flag = walking.getDestination();
			if (flag != null && calc.distanceBetween(dest, flag) <= r)
				return !unStick();
			if (r > 0)
				dest = new RSTile(dest.getX() + random(-r, r + 1), dest.getY() + random(-r, r + 1));
			if (myLoc().equals(dest))
				return false;
			if (calc.tileOnScreen(dest) && clickTile(dest, "Walk here"))
				return true;
			Point p = calc.tileToMinimap(dest);
			if (p.x == -1) {
				if ((dest = walking.getClosestTileOnMap(dest)) == null)
					return false;
				p = calc.tileToMinimap(dest);
			}
			if (p.x != -1) {
				mouse.move(p = calc.tileToMinimap(dest));
				if (p.x != -1) {
					flag = walking.getDestination();
					if (flag == null || calc.distanceBetween(dest, flag) > r)
						mouse.click(p, true);
					return true;
				}
			}
		}
		return false;
	}

	private boolean failBasic() {
		if (idleTimer.getElapsed() > 90000) {
			idleTimer = new Timer(0);
			return true;
		}
		if ((welcomeBack || game.getClientState() != 10) && !secondaryStatus.startsWith("Recovering"))
			return failLogin();
		pauseScript();
		return !authCheck;
	}

	private boolean failCheck() {
		if (failBasic())
			return true;
		if (puzzleTimer != null) {
			if (!puzzleTimer.isRunning())
				return true;
		} else if (!failTimer.isRunning()) {
			return true;
		}
		return isDead() || !inDungeon();
	}

	private boolean failInterfaces() {
		if (roomNumber == 1 && interfaces.get(79).isValid()) {
			return clickComponent(interfaces.getComponent(79, 7), "Close");
		}
		return false;
	}

	private boolean failLogin() {
		if (welcomeBack || game.getClientState() != 11 && !username.isEmpty()) {
			welcomeBack = false;
			if (!settingsFinished)
				return false;
			RSArea loggedRoom = targetRoom;
			if (!status.startsWith("Logging out"))
				log(RED, "Oops.. We got logged out somehow :S");
			while (game.getClientState() != 10) {
				if (!authCheck)
					return true;
				failLoginScreen();
				sleep(1000, 2000);
			}
			nearMonster = null;
			nearItem = null;
			if (doorTimer != null)
				doorTimer = new Timer(0);
			idleTimer = new Timer(0);
			if (inDungeon && !finish && currentRoom != null && game.isLoggedIn()) {
				welcomeBack = false;
				while (!areaContains(getCurrentRoom(), myLoc())) {
					if (idleTimer.getElapsed() > 30000|| !inDungeon())
						return true;
					sleep(100, 200);
				}
				String oldStatus = secondaryStatus;
				secondaryStatus = "Recovering from getting logged out";
				if (areaContains(startRoom, myLoc()) && !areaEquals(targetRoom, startRoom) && !inventory.contains

(GGS)) {
					teleBack();
					return false;
				}
				if (combatStyle == primaryStyle)
					ridItem(primaryWep, "Wield");
				idleTimer = new Timer(0);
				nearItem = groundItems.getNearest(GGS);
				RSTile backIn = nearItem != null ? nearItem.getLocation() : null;
				if (backIn != null) {
					while (areaContains(currentRoom, backIn) && isItemAt(backIn, true)) {
						if (!authCheck || idleTimer.getElapsed() > 40000)
							return true;
						clickTile(backIn, "Take");
						makeSpace(true);
						sleep(400, 900);
						getCurrentRoom();
					}
				} else if (!inventory.containsAll(TOOLS)) {
					return exit = true;
				}
				if (!areaContains(bossRoom, myLoc())) {
					if (!fightMonsters() || !pickUpAll())
						return false;
					if (roomNumber != 1 && backIn != null) {
						groupRoom = currentRoom;
						teleHome(true);
						pickUpAll();
						setTargetRoom(groupRoom);
						if (aComplexity > 4) {
							teleBack();
						} else unBacktrack(true);
					}
				}
				secondaryStatus = oldStatus;
			}
			if (doorTimer != null)
				doorTimer = new Timer(0);
			idleTimer = new Timer(0);
			logoutTimer = new Timer(0);
			explore = true;
			return roomNumber == 1 || !areaEquals(targetRoom, loggedRoom);
		} else {
			while (game.getClientState() == 11) {
				sleep(50, 400);
			}
		}
		return false;
	}

	private void failLoginScreen() {
		if (game.getClientState() != 7)
			return;
		RSInterface welcome = interfaces.get(906);
		if (welcome.getComponent(173).getText().equals("Play") && clickComponent(welcome.getComponent(160), "Play"))
			sleep(3000, 5000);
		String returnText = welcome.getComponent(221).getText().toLowerCase();
		welcome.getComponent(218).doClick();
		if (returnText.contains("login limit exceeded") || returnText.contains("account has not logged"))
			sleep(5000, 15000);
	}

	private boolean failSafe() {
		if (!playerIdle())
			idleTimer = new Timer(0);
		if ((welcomeBack || game.getClientState() != 10) && !secondaryStatus.startsWith("Recovering"))
			return failLogin();
		pauseScript();
		if (!authCheck || isOutside())
			return true;
		if (inDungeon) {
			if (isDead()) {
				if (status.contains("locked") && lockDown) {
					log("Posthumosly assigning the lockdown");
					goodDoors.add(nearDoor);
					lockDown = true;
				}
				status = "Oh dear...";
				secondaryStatus = "";
				groupRoom = !areaEquals(targetRoom, startRoom) ? targetRoom : null;
				while (isDead()) {
					sleep(200, 400);
					isDead = false;
				}
				failTimer = new Timer(random(360000, 480000));
				log("Died, walking back...");
				deathPath = generatePath(groupRoom);
				deaths++;
				nearDoor = null;
				nearDoor2 = null;
				nearItem = null;
				nearMonster = null;
				prayTimer = null;
				safeTile = null;
				bossFight = false;
				explore = false;
				open = true;
				retrace = false;
				roomSwitch = false;
				setTargetRoom(startRoom);
				defenseDegenerate();
				while (!areaEquals(currentRoom, targetRoom)) {
					if (failBasic() || isOutside())
						return true;
					getCurrentRoom();
					sleep(200, 300);
				}
				isDead = false;
				updateFightMode();
				selectTab(Game.TAB_INVENTORY, 4);
				if (combatStyle == primaryStyle)
					ridItem(primaryWep, "Wield");
				walking.setRun(true);
				if (doorTimer != null)
					doorTimer = new Timer(0);
				return true;
			}
			if (bossRoom != null && bossTimer != null && !bossName.isEmpty()) {
				if (bossTimer.getElapsed() > random(1500000, 1800000) && !areaContains(bossRoom, myLoc())) {
					log.severe("Took too long to kill " + bossName + ", trying a new dungeon.");
					if (developer)
						env.saveScreenshot(false);
					aborted = true;
					abortedCount++;
					return exit = true;
				}
			} else if (puzzleTimer != null) {
				if (roomNumber != 1 && !puzzleTimer.isRunning()) {
					if (developer)
						env.saveScreenshot(false);
					failReason = "Timed out";
					puzzleTimer = null;
					return true;
				}
			} else if (failTimer != null && !failTimer.isRunning()) {
				if (!aborted) {
					if (developer)
						env.saveScreenshot(false);
					log.severe("Took too long to complete this dungeon, trying a new one");
					aborted = true;
					abortedCount++;
				}
				return exit = true;
			}
			if (doorTimer != null && doorTimer.getElapsed() > random(10000, 15000)) {
				boolean nullModel = secondaryStatus.startsWith("Model");
				if (doorTimer.getElapsed() < random(45000, 90000) + (nullModel ? 30000 : 0)) {
					if (calc.distanceTo(nearDoor) < 5) {
						setTargetRoom(getCurrentRoom());
						if (nullModel)
							turnTo(nearDoor);
					} else if (!nullModel && !isMoving()) {
						walkBlockedTile(nearDoor, 2);
					}
				} else {
					log(RED, "Took too long to open this door");
					doorTimer = null;
					if (areaContains(targetRoom, myLoc())) {
						if (nearDoor != null) {
							finishedDoors.add(nearDoor);
							goodDoors.remove(nearDoor);
							allDoors.remove(nearDoor);
							openedDoors.remove(nearDoor);
						}
						if (nearDoor2 != null) {
							finishedDoors.add(nearDoor2);
							goodDoors.remove(nearDoor2);
							allDoors.remove(nearDoor2);
						}
					} else {
						getCurrentRoom();
						setTargetRoom(currentRoom);
						if (nearDoor != null) {
							goodDoors.remove(nearDoor);
							allDoors.remove(nearDoor);
							openedDoors.add(nearDoor);
						}
						if (nearDoor2 != null) {
							goodDoors.remove(nearDoor2);
							allDoors.remove(nearDoor2);
							openedDoors.add(nearDoor2);
						}
					}
					if (developer)
						env.saveScreenshot(false);
					return true;
				}
			}
			if (idleTimer != null && idleTimer.getElapsed() > random(10000, 20000)) {
				if (idleTimer.getElapsed() > random(90000, 180000)) {
					log(RED, "We've been standing still for too long, lets try to do something!");
					if (secondaryStatus.startsWith("We don't know")) {
						open = true;
						retrace = true;
					}
					if (developer && !secondaryStatus.isEmpty())
						log(secondaryStatus);
					secondaryStatus = "Attempting to reengage...";
					if (targetRoom != null) {
						if (bossRoom != null && getObjInRoom(FINISHED_LADDERS) != null)
							return finish = true;
						walkTo(targetRoom.getCentralTile(), 1);
					}
					if (puzzleTimer != null) {
						int toEnd = (int) puzzleTimer.getRemaining() - random(90000, 120000);
						puzzleTimer.setEndIn(toEnd < 1 ? 0 : toEnd);
					} else {
						int toEnd = (int) failTimer.getRemaining() - random(90000, 120000);
						failTimer.setEndIn(toEnd < 1 ? 0 : toEnd);
					}
					idleTimer = new Timer(0);
					if (targetRoom != null) {
						getCurrentRoom();
						if (!areaContains(targetRoom, myLoc()) && !unBacktrack(true)) {
							setTargetRoom(currentRoom);
							open = true;
						}
					}
					return false;
				} else camera.moveRandomly(2000);
			} else if (!walking.isRunEnabled() && walking.getEnergy() > random(10, 30)) {
				walking.setRun(true);
				sleep(500, 700);
			} else if (settingsFinished) {
				if (outOfAmmo) {
					if (combatStyle == Style.RANGE) {
						log(RED, "Out of arrows! Re-equipping or wielding a backup weapon.");
						if (inventory.getCount(true, arrows) > random(4, 10)) {
							ridItem(arrows, "Wield");
						} else {
							if (!swapAlternative()) {
								if (Option.ARROWS.enabled() && areaEquals(targetRoom, bossRoom) && 

getItemInRoom(currentRoom, arrows) != null) {
									pickUpAll();
									ridItem(arrows, "Wield");
									return false;
								}
								return exit = true;
							}
							Style.RANGE.set(false);
						}
					} else if (combatStyle == Style.MAGIC) {
						outOfAmmo = !equipFor(Style.MAGIC);
						if (outOfAmmo) {
							log(RED, "Out of Runes! Wielding our backup weapon.");
							if (!swapAlternative())
								return exit = true;
							Style.MAGIC.set(false);
						}
					}
					outOfAmmo = false;
				}
				if (unreachable && (open || retrace)) {
					camera.moveRandomly(300);
					unreachable = false;
				}
				if (status.contains("Opening a") && nearDoor != null && calc.distanceTo(nearDoor) < 4)
					return false;
				updateFightMode();
				if (checkDungeonMap(false) || unStick() || unPoison())
					return false;
				if (!skipRoom && game.getCurrentTab() == Game.TAB_INVENTORY) {
					if (!pickupName.isEmpty() && typeTo == -1 && getItemID(pickupName) > 0) {
						boolean bindReady = true;
						if (!destroyName.isEmpty()) {
							bindReady = false;
							int destroyId = getItemID(destroyName);
							if (destroyId == -1) {
								for (int slot : EQUIP_SLOTS) {
									RSItem eq = equipment.getItem(slot);
									if (eq != null && eq.getName().toLowerCase().contains

(destroyName.toLowerCase())) {
										equipAction(slot, "Remove");
										sleep(700, 1100);
										bindReady = true;
										break;
									}
								}
								destroyId = getItemID(destroyName);
							}
							if (destroyId == -1) {
								if (!bindReady) {
									log(RED, "Unable to find the desired item to destroy");
									pickupName = "";
									destroyName = "";
								} else bindReady = false;
							} else bindReady = destroyItem(destroyId);
						}
						if (bindReady) {
							int itemId = getItemID(pickupName);
							if (itemId > 0 && ridItem(itemId, "Bind"))
								pickupName = "";
						}
					}
					makeSpace(false);
				}
				failInterfaces();
				return unBacktrack(false);
			}
		}
		return false;
	}

}
