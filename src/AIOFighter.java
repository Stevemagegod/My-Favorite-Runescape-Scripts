import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.*;
import com.rarebot.script.methods.*;
import com.rarebot.script.util.*;
import com.rarebot.script.wrappers.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import com.rarebot.event.listeners.MessageListener;
import javax.swing.table.DefaultTableModel;

@ScriptManifest(authors = {"hellomot0123"}, keywords = {"Combat"}, name = "AIOFighter", description = "Start wherever you want to fight.", version = 1.03)
public class AIOFighter extends Script implements PaintListener, MessageListener {

    public String[] NPC_ARRAY;
    public String[] LOOT_ARRAY;
    public Potion[] POTION_ARRAY;
    public Long START_TIME, ANTIBAN;
    public int[] SKILLS_ARRAY = new int[25];
    public String STATUS;
    public boolean BURY_MODE, START, SPEC_SWAP, SPEC_EQUIP, B2PB2B_TAB, B2PB2B_SPELL, NATURE, FIRE, EARTH, WATER, SWAP, MEMBER, walk, frombank, tobank, FIRST_TIME, BANKING, canUpdate, MULTI_COMBAT, NEED_TO_BANK, SHOW_PATH_TO_BANK, SHOW_PATH_TO_NPC;
    public RSItem EQUIPPED_WEAPON, EQUIPPED_SHIELD, SPECIAL_WEAPON, STAFF, TOME;
    public int PERCENT_TO_SPEC, RADIUS;
    public RSNPC TARGET;
    public RSTile START_TILE;
    Potion SARADOMIN_BREW = new Potion(new int[]{6685, 6687, 6689, 6691}, new int[][]{{Skills.DEFENSE, 0, 25}}, true, false, false, false, false, false);
    Potion ZAMORAK_BREW = new Potion(new int[]{2450, 189, 191, 193}, new int[][]{{Skills.ATTACK, 2, 20}, {Skills.STRENGTH, 2, 12}}, false, false, false, true, false, false);
    Potion ANTIPOISON_PLUS_PLUS = new Potion(new int[]{5952, 5954, 5956, 5958}, null, false, true, false, false, false, false);
    Potion ANTIPOISON_PLUS = new Potion(new int[]{5943, 5945, 5947, 5949}, null, false, true, false, false, false, false);
    Potion SUPER_ANTIPOISON = new Potion(new int[]{2448, 181, 183, 185}, null, false, true, false, false, false, false);
    Potion ANTIPOISON = new Potion(new int[]{2446, 175, 177, 179}, null, false, true, false, false, false, false);
    Potion GUTHIX_REST = new Potion(new int[]{4417, 4419, 4421, 4423}, null, true, true, false, false, false, false);
    Potion SANFEW_SERUM = new Potion(new int[]{10925, 10927, 10929, 10931}, null, false, true, false, true, true, true);
    Potion SUPER_RESTORE = new Potion(new int[]{3024, 3026, 3028, 3030}, null, false, false, false, true, true, true);
    Potion RESTORE_POTION = new Potion(new int[]{2430, 127, 129, 131}, new int[][]{{Skills.ATTACK, 0, 0}, {Skills.DEFENSE, 0, 0}, {Skills.STRENGTH, 0, 0}, {Skills.RANGE, 0, 0}, {Skills.MAGIC, 0, 0}}, false, false, false, false, false, false);
    Potion SUPER_PRAYER = new Potion(new int[]{15328, 15329, 15330, 15331}, null, false, false, false, true, false, false);
    Potion PRAYER_POTION = new Potion(new int[]{2434, 139, 141, 143}, null, false, false, false, true, false, false);
    Potion SUMMONING_POTION = new Potion(new int[]{12140, 12142, 12144, 12146}, null, false, false, false, false, true, false);
    Potion OVERLOAD = new Potion(new int[]{15332, 15333, 15334, 15335}, new int[][]{{Skills.ATTACK, 0, 0}}, false, false, true, false, false, false);
    Potion EXTREME_STRENGTH = new Potion(new int[]{15312, 15313, 15314, 15315}, new int[][]{{Skills.STRENGTH, 5, 22}}, false, false, false, false, false, false);
    Potion EXTREME_ATTACK = new Potion(new int[]{15208, 15309, 15310, 15311}, new int[][]{{Skills.ATTACK, 5, 22}}, false, false, false, false, false, false);
    Potion EXTREME_DEFENCE = new Potion(new int[]{15316, 15317, 15318, 15319}, new int[][]{{Skills.DEFENSE, 5, 22}}, false, false, false, false, false, false);
    Potion EXTREME_RANGING = new Potion(new int[]{15324, 15325, 15326, 15327}, new int[][]{{Skills.RANGE, 4, 19}}, false, false, false, false, false, false);
    Potion EXTREME_MAGIC = new Potion(new int[]{15320, 15321, 15322, 15323}, new int[][]{{Skills.MAGIC, 7, 0}}, false, false, false, false, false, false);
    Potion SUPER_STRENGTH = new Potion(new int[]{2440, 157, 159, 161}, new int[][]{{Skills.STRENGTH, 5, 15}}, false, false, false, false, false, false);
    Potion SUPER_ATTACK = new Potion(new int[]{2436, 145, 147, 149}, new int[][]{{Skills.ATTACK, 5, 15}}, false, false, false, false, false, false);
    Potion SUPER_DEFENCE = new Potion(new int[]{2442, 163, 165, 167}, new int[][]{{Skills.DEFENSE, 5, 15}}, false, false, false, false, false, false);
    Potion RANGING_POTION = new Potion(new int[]{2444, 169, 171, 173}, new int[][]{{Skills.RANGE, 4, 10}}, false, false, false, false, false, false);
    Potion MAGIC_POTION = new Potion(new int[]{3040, 3042, 3044, 3046}, new int[][]{{Skills.MAGIC, 5, 0}}, false, false, false, false, false, false);
    Potion COMBAT_POTION = new Potion(new int[]{9739, 9741, 9743, 9745}, new int[][]{{Skills.STRENGTH, 3, 10}, {Skills.ATTACK, 3, 10}}, false, false, false, false, false, false);
    Potion ATTACK_POTION = new Potion(new int[]{2428, 121, 123, 125}, new int[][]{{Skills.ATTACK, 3, 10}}, false, false, false, false, false, false);
    Potion STRENGTH_POTION = new Potion(new int[]{113, 115, 117, 119}, new int[][]{{Skills.STRENGTH, 3, 10}}, false, false, false, false, false, false);
    Potion DEFENCE_POTION = new Potion(new int[]{2432, 133, 135, 137}, new int[][]{{Skills.DEFENSE, 3, 10}}, false, false, false, false, false, false);
    Potion[] FULL_POTION_ARRAY = {SARADOMIN_BREW, ZAMORAK_BREW, ANTIPOISON_PLUS_PLUS, ANTIPOISON_PLUS, SUPER_ANTIPOISON, ANTIPOISON, GUTHIX_REST, SANFEW_SERUM, SUPER_RESTORE, RESTORE_POTION, SUPER_PRAYER, PRAYER_POTION, SUMMONING_POTION, OVERLOAD, EXTREME_STRENGTH, EXTREME_ATTACK, EXTREME_DEFENCE, EXTREME_RANGING, EXTREME_MAGIC, SUPER_STRENGTH, SUPER_ATTACK, SUPER_DEFENCE, RANGING_POTION, MAGIC_POTION, COMBAT_POTION, ATTACK_POTION, STRENGTH_POTION, DEFENCE_POTION};
    public int[] PERCENTAGE = {10, 25, 30, 35, 50, 55, 60, 65, 75, 100};
    public String[][] SPECIAL_WEAPONS = {
        {"rod of ivandis", "ivandis flail", "rune thrownaxe"},
        {"dragon dagger", "dragon longsword", "dragon mace", "dragon spear", "rune claws", "vesta's longsword"},
        {"dragon halberd"},
        {"statius's warhammer", "statius' warhammer", "magic longbow"},
        {"dragon claws", "abyssal whip", "granite maul", "granite mace", "darklight", "penance trident", "vesta's spear", "armadyl godsword", "saradomin godsword", "zanik's crossbow", "morrigan's throwing axe", "morrigan's javelin", "hand cannon"},
        {"dragon scimitar", "magic shortbow", "saradomin bow", "guthix bow", "zamorak bow"},
        {"dragon 2h sword", "abyssal vine whip", "korasi's sword", "zamorak godsword"},
        {"dark bow"},
        {"brackish blade", "bone dagger", "brine sabre", "dorgeshuun crossbow"},
        {"dragon pickaxe", "dragon battleaxe", "dragon hatchet", "excalibur", "staff of light", "ancient mace", "saradomin sword", "dwarven army axe", "bandos godsword", "seercull", "enhanced excalibur"}
    };
    public int[] DROP_ARRAY = {229, 1923, 2313, 1980, 7728};
    public ArrayList<Object> pathtobank = new ArrayList<Object>();
    public ArrayList<Object> pathfrombank = new ArrayList<Object>();
    HashMap map = new HashMap<Integer, Integer>();

    @Override
    public void onRepaint(Graphics grphcs) {
        Long RUNTIME = System.currentTimeMillis() - START_TIME;
        int HOURS = 0;
        while (RUNTIME > 3600000) {
            HOURS++;
            RUNTIME -= 3600000;
        }
        int MINUTES = 0;
        while (RUNTIME > 60000) {
            MINUTES++;
            RUNTIME -= 60000;
        }
        int SECONDS = 0;
        while (RUNTIME > 1000) {
            SECONDS++;
            RUNTIME -= 1000;
        }
        grphcs.setColor(Color.WHITE);
        grphcs.drawLine(0, (int) mouse.getLocation().getY(), game.getWidth(), (int) mouse.getLocation().getY());
        grphcs.drawLine((int) mouse.getLocation().getX(), 0, (int) mouse.getLocation().getX(), game.getHeight());
        grphcs.drawString("AIOFighter v" + AIOFighter.class.getAnnotation(ScriptManifest.class).version(), 5, 15);
        grphcs.drawString("By hellomot0123", 5, 30);
        grphcs.drawString("Runtime: " + HOURS + ":" + MINUTES + ":" + SECONDS, 5, 45);
        grphcs.drawString("Status: " + STATUS, 5, 60);
        int counter = 0;
        int visible = 0;
        for (int i : SKILLS_ARRAY) {
            if (skills.getCurrentExp(counter) > i) {
                int XP_CHANGE = (skills.getCurrentExp(counter) - i);
                grphcs.drawString(Skills.getSkillName(counter).replaceFirst(Skills.getSkillName(counter).substring(0, 1), Skills.getSkillName(counter).substring(0, 1).toUpperCase()) + ": " + XP_CHANGE + " (" + (int) (XP_CHANGE * 3600000D / (System.currentTimeMillis() - START_TIME)) + ") XP Gained (Per Hour)", 5, (75 + visible * 15));
                visible++;
            }
            counter++;
        }
        if (SHOW_PATH_TO_BANK) {
            for (Object o : pathtobank) {
                if (o instanceof ArrayList) {
                    ArrayList<RSTile> temp = (ArrayList<RSTile>) o;
                    for (int i = 0; i < temp.size(); i++) {
                        grphcs.setColor(Color.CYAN);
                        grphcs.drawString("#" + (i + 1), (int) calc.tileToMinimap(temp.get(i)).getX() - 20, (int) calc.tileToMinimap(temp.get(i)).getY());
                        grphcs.fillRect((int) calc.tileToMinimap(temp.get(i)).getX() - 2, (int) calc.tileToMinimap(temp.get(i)).getY() - 2, 4, 4);
                    }
                }
            }
        } else if (SHOW_PATH_TO_NPC) {
            for (Object o : pathfrombank) {
                if (o instanceof ArrayList) {
                    ArrayList<RSTile> temp = (ArrayList<RSTile>) o;
                    for (int i = 0; i < temp.size(); i++) {
                        grphcs.setColor(Color.MAGENTA);
                        grphcs.drawString("#" + (i + 1), (int) calc.tileToMinimap(temp.get(i)).getX() - 20, (int) calc.tileToMinimap(temp.get(i)).getY());
                        grphcs.fillRect((int) calc.tileToMinimap(temp.get(i)).getX() - 2, (int) calc.tileToMinimap(temp.get(i)).getY() - 2, 4, 4);
                    }
                }
            }
        }
    }

    @Override
    public void messageReceived(com.rarebot.event.events.MessageEvent me) {
        if (me.getID() == 0) {
            if (me.getMessage().toLowerCase().contains("you need to be on a members")) {
                MEMBER = false;
            }
        }
    }

    public enum States {

        FIGHTING, LOOTING, EATING, STOPPING, ANTIBAN, POTTING, SPECCING, RE_EQUIPPING_GEAR, TURNING_RUN_ON, CLICKING_CONTINUE, DROPPING_JUNK, CASTING_B2P_B2B, BURYING_SCATTERING, RECORDING, WALKING_TO_BANK, WALKING_TO_NPC, BANKING, QUICK_PRAY;
    };

    public States getState() {
        if (FIRST_TIME && BANKING) {
            STATUS = "Recording bank path & setting what to withdraw";
            return States.RECORDING;
        }
        if (interfaces.canContinue()) {
            STATUS = "Clicking continue";
            return States.CLICKING_CONTINUE;
        }
        if (!walking.isRunEnabled() && walking.getEnergy() > 30) {
            STATUS = "Turning Run On";
            return States.TURNING_RUN_ON;
        }
        if (!BANKING && shallWeBank()) {
            STATUS = "Stopping Script";
            return States.STOPPING;
        }
        if (BANKING && (shallWeBank() || NEED_TO_BANK) && !atBank()) {
            STATUS = "Walking to Bank";
            SHOW_PATH_TO_BANK = true;
            return States.WALKING_TO_BANK;
        }
        if (BANKING && (shallWeBank() || NEED_TO_BANK) && atBank()) {
            STATUS = "Banking";
            return States.BANKING;
        }
        if (BANKING && !shallWeBank() && atBank()) {
            STATUS = "Walking to NPC";
            SHOW_PATH_TO_NPC = true;
            return States.WALKING_TO_NPC;
        }
        if (activateAntiban()) {
            STATUS = "Antiban";
            return States.ANTIBAN;
        }
        if (areWePraying() && !prayer.isQuickPrayerOn()) {
            STATUS = "Turning on Quick Prayers";
            return States.QUICK_PRAY;
        }
        if ((B2PB2B_TAB || B2PB2B_SPELL) && needToB2PB2B()) {
            STATUS = "Casting B2P/B2B";
            return States.CASTING_B2P_B2B;
        }
        if (foodInInventory() && needToEat()) {
            STATUS = "Eating";
            return States.EATING;
        }
        if (getJunk() != null) {
            STATUS = "Dropping Junk";
            return States.DROPPING_JUNK;
        }
        if (BURY_MODE && haveBuryOrScatter() != null) {
            STATUS = "Burying bones / Scattering ash";
            return States.BURYING_SCATTERING;
        }
        if ((!canWeSpecial() && SPEC_SWAP && specWepEquipped() && notSwitchingToStaff()) || (SWAP && specWepEquipped() && notSwitchingToSpec())) {
            STATUS = "Re-Equipping Gear";
            return States.RE_EQUIPPING_GEAR;
        }
        if (LOOT_ARRAY.length != 0 && lootOnFloor() && !isPlayerInCombat() && !inventoryFull()) {
            STATUS = "Looting";
            return States.LOOTING;
        }
        if (POTION_ARRAY.length != 0 && needToPot() != null) {
            STATUS = "Potting";
            return States.POTTING;
        }
        if (NPC_ARRAY.length != 0 && availableNPC() && !attackingTarget()) {
            STATUS = "Fighting";
            return States.FIGHTING;
        }
        if ((SPEC_SWAP || SPEC_EQUIP) && canWeSpecial()) {
            STATUS = "Speccing";
            return States.SPECCING;
        }
        return null;
    }

    @Override
    public boolean onStart() {
        FIRST_TIME = true;
        MEMBER = true;
        ANTIBAN = (long) 0;
        mouse.setSpeed(0);
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new GUI().setVisible(true);
            }
        });
        while (!START) {
            sleep(100);
        }
        determineSpecial();
        determineRuneSupply();
        determineB2PB2B();
        camera.setPitch(100);
        POTION_ARRAY = potionsInUse();
        getSkillData();
        START_TIME = System.currentTimeMillis();
        START_TILE = getMyPlayer().getLocation();
        return true;
    }

    @Override
    public int loop() {
        try {
            switch (getState()) {
                case QUICK_PRAY:
                    try {
                        prayer.setQuickPrayer(true);
                        sleep(1000);
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case WALKING_TO_BANK:
                    try {
                        walkToBank();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case BANKING:
                    try {
                        bank();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case WALKING_TO_NPC:
                    try {
                        walkToNPC();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case RECORDING:
                    try {
                        java.awt.EventQueue.invokeLater(new Runnable() {

                            @Override
                            public void run() {
                                new GUI2().setVisible(true);
                            }
                        });
                        env.setUserInput(1);
                        while (FIRST_TIME) {
                            recording();
                        }
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case BURYING_SCATTERING:
                    try {
                        buryScatter();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case CASTING_B2P_B2B:
                    try {
                        castB2PB2B();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case DROPPING_JUNK:
                    try {
                        drop();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case CLICKING_CONTINUE:
                    try {
                        interfaces.clickContinue();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case TURNING_RUN_ON:
                    try {
                        walking.setRun(true);
                        sleep(1000);
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case RE_EQUIPPING_GEAR:
                    try {
                        equip();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case SPECCING:
                    try {
                        spec();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case POTTING:
                    try {
                        pot();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case LOOTING:
                    try {
                        loot();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case FIGHTING:
                    try {
                        fight();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case EATING:
                    try {
                        eat();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case STOPPING:
                    try {
                        env.saveScreenshot(true);
                        log("Screenshot saved.");
                        stopScript(true);
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                case ANTIBAN:
                    try {
                        antiban();
                    } catch (Exception e) {
                        log(Color.BLUE, "For debuggin purposes only, please post on thread:");
                        log("" + e.toString());
                        log("" + e.fillInStackTrace());
                        log(Color.BLUE, "---------END----------");
                    }
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
        }
        return 0;
    }

    @Override
    public void onFinish() {
        log("Thankyou for using AIOFighter.");
    }

    public void walkToBank() {
        int counter = -1;
        for (Object o : pathtobank) {
            counter++;
            if (o instanceof ArrayList) {
                ArrayList<RSTile> temp = (ArrayList<RSTile>) o;
                for (int i = 0; i < temp.size(); i++) {
                    Timer timer2 = new Timer(10000);
                    while (calc.distanceTo(temp.get(i)) > 3) {
                        if (!timer2.isRunning()) {
                            break;
                        }
                        walking.walkTo(temp.get(i));
                        waitToMove();
                    }
                }
                sleep(2000);
                continue;
            } else if (o instanceof String[]) {
                String[] temp = (String[]) o;
                RSObject object = objects.getNearest(Integer.parseInt(temp[0]));
                if (!object.isOnScreen()) {
                    camera.turnTo(object);
                }
                Timer timer = new Timer(10000);
                while (!object.interact(temp[1])) {
                    if (!timer.isRunning()) {
                        break;
                    }
                    object.interact(temp[1]);
                }
                sleep(5000);
                continue;
            }
        }
        SHOW_PATH_TO_BANK = false;
    }

    public void walkToNPC() {
        for (Object o : pathfrombank) {
            if (o instanceof ArrayList) {
                ArrayList<RSTile> temp = (ArrayList<RSTile>) o;
                for (int i = 0; i < temp.size(); i++) {
                    Timer timer2 = new Timer(10000);
                    while (calc.distanceTo(temp.get(i)) > 3) {
                        if (!timer2.isRunning()) {
                            break;
                        }
                        walking.walkTo(temp.get(i));
                        waitToMove();
                    }
                }
                sleep(2000);
                continue;
            } else if (o instanceof String[]) {
                String[] temp = (String[]) o;
                RSObject object = objects.getNearest(Integer.parseInt(temp[0]));
                if (!object.isOnScreen()) {
                    camera.turnTo(object);
                }
                Timer timer = new Timer(10000);
                while (!object.interact(temp[1])) {
                    if (!timer.isRunning()) {
                        break;
                    }
                    object.interact(temp[1]);
                }
                sleep(5000);
                continue;
            }
        }
        SHOW_PATH_TO_NPC = false;
    }

    public void bank() {
        while (inventory.contains(EQUIPPED_WEAPON.getID()) && EQUIPPED_WEAPON.getID() != -1) {
            inventory.getItem(EQUIPPED_WEAPON.getID()).doClick(true);
            sleep(1000);
        }
        while (inventory.contains(EQUIPPED_SHIELD.getID()) && EQUIPPED_SHIELD.getID() != -1) {
            inventory.getItem(EQUIPPED_SHIELD.getID()).doClick(true);
            sleep(1000);
        }
        Timer timer = new Timer(60000);
        while (shallWeBank()) {
            if (!timer.isRunning()) {
                break;
            }
            if (!bank.isOpen()) {
                bank.open();
            } else {
                if (inventory.getCount() == 0) {
                    if (!map.isEmpty()) {
                        for (Object o : map.keySet()) {
                            Integer i = (Integer) o;
                            while (!inventory.contains(i)) {
                                bank.withdraw(i, (Integer) map.get(i));
                                sleep(2000);
                            }
                        }
                    }
                    bank.close();
                    POTION_ARRAY = potionsInUse();
                    determineSpecial();
                    determineRuneSupply();
                    determineB2PB2B();
                } else {
                    bank.depositAll();
                }
            }
        }
        NEED_TO_BANK = false;
    }

    public boolean atBank() {
        ArrayList<RSTile> temp = (ArrayList<RSTile>) pathtobank.get((pathtobank.size() - 1));
        return (calc.distanceTo(temp.get((temp.size() - 1))) <= 10);
    }

    public boolean atNPC() {
        ArrayList<RSTile> temp = (ArrayList<RSTile>) pathfrombank.get((pathfrombank.size() - 1));
        return (calc.distanceTo(temp.get((temp.size() - 1))) <= RADIUS);
    }

    public RSTile getRSTile() {
        ArrayList<RSTile> temp = (ArrayList<RSTile>) pathfrombank.get((pathfrombank.size() - 1));
        return temp.get((temp.size() - 1));
    }

    public boolean notSwitchingToStaff() {
        if (B2PB2B_SPELL && SWAP) {
            if (inventory.contains(STAFF.getID())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public boolean notSwitchingToSpec() {
        if (SPEC_SWAP) {
            if (inventory.contains(SPECIAL_WEAPON.getID())) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }

    public void buryScatter() {
        haveBuryOrScatter().doClick(true);
        sleep(500);
    }

    public RSItem haveBuryOrScatter() {
        for (RSItem i : inventory.getItems()) {
            if (i == null || i.getID() == -1) {
                continue;
            }
            if (i.getComponent().getActions() == null || i.getComponent().getActions()[0] == null) {
                continue;
            }
            for (String s : i.getComponent().getActions()) {
                if (s != null) {
                    if (s.contains("Bury") || s.contains("Scatter")) {
                        return i;
                    }
                }
            }
        }
        return null;
    }

    public boolean needToB2PB2B() {
        if (!foodInInventory()) {
            if (inventoryHasBones()) {
                return true;
            }
        }
        return false;
    }

    public boolean inventoryHasBones() {
        for (RSItem i : inventory.getItems()) {
            if (i.getName().toLowerCase().contains("bones") && !i.getName().toLowerCase().equals("bones to peaches") && !i.getName().toLowerCase().equals("bones to bananas")) {
                return true;
            }
        }
        return false;
    }

    public void castB2PB2B() {
        if (B2PB2B_TAB) {
            if (inventory.contains(8015)) {
                inventory.getItem(8015).interact("break");
                sleep(3000);
                return;
            }
            if (inventory.contains(8014)) {
                inventory.getItem(8014).interact("break");
                sleep(3000);
                return;
            }
        }
        if (B2PB2B_SPELL) {
            if (!SWAP) {
                if (skills.getCurrentLevel(Skills.MAGIC) >= 65 && MEMBER) {
                    magic.castSpell(Magic.Spell.BONES_TO_PEACHES);
                } else {
                    magic.castSpell(Magic.Spell.BONES_TO_BANANAS);
                }
            } else {
                if (!specWepEquipped()) {
                    if (TOME != null) {
                        if (inventory.contains(TOME.getID())) {
                            inventory.getItem(TOME.getID()).doClick(true);
                            sleep(1000);
                            return;
                        }
                    }
                    if (STAFF != null) {
                        if (inventory.contains(STAFF.getID())) {
                            inventory.getItem(STAFF.getID()).doClick(true);
                            sleep(1000);
                            return;
                        }
                    }
                } else {
                    if (skills.getCurrentLevel(Skills.MAGIC) >= 65 && MEMBER) {
                        magic.castSpell(Magic.Spell.BONES_TO_PEACHES);
                    } else {
                        magic.castSpell(Magic.Spell.BONES_TO_BANANAS);
                    }
                }
            }
        }
    }

    public void drop() {
        getJunk().interact("drop");
        sleep(500);
    }

    public RSItem getJunk() {
        for (RSItem i : inventory.getItems()) {
            for (int j : DROP_ARRAY) {
                if (i.getID() == j) {
                    return i;
                }
            }
        }
        return null;
    }

    public boolean canWeSpecial() {
        if (combat.getSpecialBarEnergy() >= PERCENT_TO_SPEC) {
            return true;
        } else {
            return false;
        }
    }

    public boolean specWepEquipped() {
        return (inventory.contains(EQUIPPED_WEAPON.getID()));
    }

    public void spec() {
        if (!combat.isSpecialEnabled()) {
            if (SPEC_EQUIP) {
                game.openTab(Game.Tab.ATTACK, true);
                RSInterface inter = interfaces.get(884);
                RSComponent comp = inter.getComponent(4);
                comp.doClick(true);
                sleep(500);
            } else {
                if (!specWepEquipped()) {
                    game.openTab(Game.Tab.INVENTORY, true);
                    inventory.getItem(SPECIAL_WEAPON.getID()).doClick(true);
                    sleep(1000);
                } else {
                    game.openTab(Game.Tab.ATTACK, true);
                    RSInterface inter = interfaces.get(884);
                    RSComponent comp = inter.getComponent(4);
                    comp.doClick(true);
                    sleep(500);
                }
            }
        }
    }

    public void equip() {
        if (specWepEquipped()) {
            game.openTab(Game.Tab.INVENTORY, true);
            if (EQUIPPED_SHIELD.getID() != -1) {
                if (inventory.contains(EQUIPPED_SHIELD.getID())) {
                    inventory.getItem(EQUIPPED_SHIELD.getID()).doClick(true);
                    sleep(1000);
                    return;
                }
            }
            inventory.getItem(EQUIPPED_WEAPON.getID()).doClick(true);
            sleep(1000);
        }
    }

    public Potion needToPot() {
        for (Potion p : POTION_ARRAY) {
            if (p.havePotion()) {
                if (p.activate()) {
                    return p;
                }
            }
        }
        return null;
    }

    public boolean areWePraying() {
        for (Potion p : POTION_ARRAY) {
            if (p == ZAMORAK_BREW || p == SUPER_RESTORE || p == SUPER_PRAYER || p == PRAYER_POTION) {
                return true;
            }
        }
        return false;
    }

    public boolean prayInInventory() {
        for (Potion p : POTION_ARRAY) {
            if ((p == ZAMORAK_BREW && !p.havePotion()) || (p == SUPER_RESTORE && !p.havePotion()) || (p == SUPER_PRAYER && !p.havePotion()) || (p == PRAYER_POTION && !p.havePotion())) {
                return false;
            }
        }
        return true;
    }

    public void pot() {
        needToPot().drinkPotion();
    }

    public void determineRuneSupply() {
        if (EQUIPPED_WEAPON.getName().toLowerCase().contains("staff") || EQUIPPED_WEAPON.getName().toLowerCase().contains("battlestaff")) {
            if (EQUIPPED_WEAPON.getName().toLowerCase().contains("fire") || EQUIPPED_WEAPON.getName().toLowerCase().contains("steam") || EQUIPPED_WEAPON.getName().toLowerCase().contains("lava")) {
                FIRE = true;
            }
            if (EQUIPPED_WEAPON.getName().toLowerCase().contains("water") || EQUIPPED_WEAPON.getName().toLowerCase().contains("mud") || EQUIPPED_WEAPON.getName().toLowerCase().contains("steam")) {
                WATER = true;
            }
            if (EQUIPPED_WEAPON.getName().toLowerCase().contains("earth") || EQUIPPED_WEAPON.getName().toLowerCase().contains("mud") || EQUIPPED_WEAPON.getName().toLowerCase().contains("lava")) {
                EARTH = true;
            }
            if (EQUIPPED_WEAPON.getName().toLowerCase().contains("nature")) {
                NATURE = true;
            }
        }
        if (EQUIPPED_SHIELD.getName().toLowerCase().equals("tome of frost")) {
            WATER = true;
        }
        for (RSItem i : inventory.getItems()) {
            if (i.getName().toLowerCase().contains("staff") || i.getName().toLowerCase().contains("battlestaff")) {
                if (i.getName().toLowerCase().contains("fire") || i.getName().toLowerCase().contains("steam") || i.getName().toLowerCase().contains("lava")) {
                    FIRE = true;
                    SWAP = true;
                    STAFF = i;
                }
                if (i.getName().toLowerCase().contains("water") || i.getName().toLowerCase().contains("mud") || i.getName().toLowerCase().contains("steam")) {
                    WATER = true;
                    SWAP = true;
                    STAFF = i;
                }
                if (i.getName().toLowerCase().contains("earth") || i.getName().toLowerCase().contains("mud") || i.getName().toLowerCase().contains("lava")) {
                    EARTH = true;
                    SWAP = true;
                    STAFF = i;
                }
                if (i.getName().toLowerCase().contains("nature")) {
                    NATURE = true;
                    SWAP = true;
                    STAFF = i;
                }
            }
            if (i.getName().toLowerCase().equals("tome of frost")) {
                WATER = true;
                SWAP = true;
                TOME = i;
            }
        }
        if (inventory.contains(561)) {
            NATURE = true;
        }
        if (inventory.contains(554)) {
            FIRE = true;
        }
        if (inventory.contains(555)) {
            WATER = true;
        }
        if (inventory.contains(557)) {
            EARTH = true;
        }
    }

    public void determineB2PB2B() {
        B2PB2B_TAB = (inventory.contains(8014) || inventory.contains(8015));
        if (!B2PB2B_TAB) {
            if (NATURE && EARTH && WATER && skills.getCurrentLevel(Skills.MAGIC) >= 10) {
                B2PB2B_SPELL = true;
            }
        }
    }

    public void determineSpecial() {
        while (game.getTab() != Game.Tab.EQUIPMENT) {
            game.openTab(Game.Tab.EQUIPMENT, true);
        }
        while (game.getTab() != Game.Tab.INVENTORY) {
            EQUIPPED_WEAPON = equipment.getItem(Equipment.WEAPON);
            EQUIPPED_SHIELD = equipment.getItem(Equipment.SHIELD);
            game.openTab(Game.Tab.INVENTORY, true);

        }
        for (RSItem item : inventory.getItems()) {
            for (int i = 0; i < SPECIAL_WEAPONS.length; i++) {
                for (int j = 0; j < SPECIAL_WEAPONS[i].length; j++) {
                    if (item.getName().toLowerCase().contains(SPECIAL_WEAPONS[i][j])) {
                        SPECIAL_WEAPON = item;
                        PERCENT_TO_SPEC = PERCENTAGE[i];
                    }
                }
            }
        }
        SPEC_SWAP = SPECIAL_WEAPON != null;
        if (!SPEC_SWAP) {
            for (int i = 0; i < SPECIAL_WEAPONS.length; i++) {
                for (int j = 0; j < SPECIAL_WEAPONS[i].length; j++) {
                    if (EQUIPPED_WEAPON.getName().toLowerCase().contains(SPECIAL_WEAPONS[i][j])) {
                        SPEC_EQUIP = true;
                        PERCENT_TO_SPEC = PERCENTAGE[i];
                    }
                }
            }
        }
    }

    public Potion[] potionsInUse() {
        ArrayList<Potion> TEMP = new ArrayList<Potion>();
        for (Potion p : FULL_POTION_ARRAY) {
            if (p.havePotion()) {
                TEMP.add(p);
            }
        }
        Potion[] TEMP2 = new Potion[TEMP.size()];
        TEMP.toArray(TEMP2);
        return TEMP2;
    }

    public boolean shallWeBank() {
        if (!isPlayerInCombat()) {
            if (inventoryFull()) {
                NEED_TO_BANK = true;
                return true;
            }
            if (!BANKING && !foodInInventory() && combat.getHealth() < 50 && !areWePraying()) {
                NEED_TO_BANK = true;
                return true;
            }
            if (BANKING && !foodInInventory() && !areWePraying()) {
                NEED_TO_BANK = true;
                return true;
            }
            if (areWePraying() && !prayInInventory()) {
                NEED_TO_BANK = true;
                return true;
            }
        }
        return false;
    }

    public boolean activateAntiban() {
        if (System.currentTimeMillis() > ANTIBAN) {
            ANTIBAN = System.currentTimeMillis() + random(5000, 30000);
            return true;
        }
        return false;
    }

    private void antiban() {
        Thread mouseThread = new Thread() {

            @Override
            public void run() {
                switch (random(0, 9)) {
                    case 0:
                        mouse.moveOffScreen();
                        break;
                    case 1:
                        mouse.move(random(0, game.getWidth()), random(0, game.getHeight()));
                        break;
                    case 2:
                        mouse.moveSlightly();
                        break;
                    case 3:
                        RSNPC NPC = npcs.getNearest(NPC_FILTER);
                        if (NPC != null) {
                            NPC.interact("examine");
                        }
                        break;
                    case 4:
                        RSPlayer PLAYER = players.getNearest(new Filter<RSPlayer>() {

                            @Override
                            public boolean accept(RSPlayer p) {
                                if (p.isOnScreen() && !p.equals(getMyPlayer())) {
                                    return true;
                                }
                                return false;
                            }
                        });
                        if (PLAYER != null) {
                            mouse.click(PLAYER.getScreenLocation(), false);
                        }
                        break;
                }
            }
        };
        Thread keyThread = new Thread() {

            @Override
            public void run() {
                switch (random(0, 1)) {
                    case 0:
                        camera.setAngle(random(0, 360));
                        break;
                }
            }
        };

        if (random(0, 1) == 0) {
            keyThread.start();
            sleep(random(0, 600));
            mouseThread.start();
        } else {
            mouseThread.start();
            sleep(random(0, 600));
            keyThread.start();
        }
    }

    public void getSkillData() {
        for (int counter = 0; counter <= 24; counter++) {
            SKILLS_ARRAY[counter] = skills.getCurrentExp(counter);
        }
    }

    public boolean inventoryFull() {
        if (inventory.isFull()) {
            if (BURY_MODE && haveBuryOrScatter() != null) {
                buryScatter();
                return false;
            }
            if (foodInInventory()) {
                eat();
                if (inventory.isFull()) {
                    return inventoryFull();
                }
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean foodInInventory() {
        return getFood() != null;
    }

    private RSItem getFood() {
        for (RSItem i : inventory.getItems()) {
            if (i == null || i.getID() == -1) {
                continue;
            }
            if (i.getComponent().getActions() == null || i.getComponent().getActions()[0] == null) {
                continue;
            }
            for (String s : i.getComponent().getActions()) {
                if (s != null) {
                    if (s.contains("Eat")) {
                        return i;
                    }
                }
            }
        }
        return null;
    }

    public boolean needToEat() {
        return combat.getHealth() < 60;
    }

    public void eat() {
        RSItem i = getFood();
        if (!i.doClick(true)) {
            return;
        }
        sleep(2000);
    }

    public boolean lootOnFloor() {
        return groundItems.getNearest(LOOT_FILTER) != null;
    }

    public void loot() {
        RSGroundItem LOOT = groundItems.getNearest(LOOT_FILTER);
        if (LOOT != null) {
            if (LOOT.isOnScreen()) {
                if (!LOOT.interact("Take " + LOOT.getItem().getName())) {
                    return;
                }
                if (calc.distanceTo(LOOT.getLocation()) == 0) {
                    sleep(500);
                    return;
                }
                waitToMove();
            } else {
                walking.walkTileMM(LOOT.getLocation());
                waitLOOTOnScreen(LOOT);
            }
        }
    }

    public void fight() {
        if (TARGET != null) {
            if (TARGET.isOnScreen()) {
                if (!TARGET.interact("Attack " + TARGET.getName())) {
                    return;
                }
                if (calc.distanceTo(TARGET) < 1) {
                    sleep(500);
                    return;
                }
                waitToMove();
            } else {
                walking.walkTileMM(TARGET.getLocation());
                waitNPCOnScreen(TARGET);
            }
        }
    }

    public void waitNPCOnScreen(RSNPC TARGET) {
        Timer timer = new Timer(4000);
        while (!TARGET.isOnScreen()) {
            if (!timer.isRunning()) {
                break;
            }
        }
    }

    public void waitLOOTOnScreen(RSGroundItem LOOT) {
        Timer timer = new Timer(4000);
        while (!LOOT.isOnScreen()) {
            if (!timer.isRunning()) {
                break;
            }
        }
    }

    public void waitToMove() {
        Timer timer = new Timer(2000);
        while (!getMyPlayer().isMoving()) {
            if (!timer.isRunning()) {
                break;
            }
        }
        while (getMyPlayer().isMoving()) {
        }
    }
    private final Filter<RSNPC> NPC_FILTER = new Filter<RSNPC>() {

        @Override
        public boolean accept(RSNPC NPC) {
            for (String s : NPC_ARRAY) {
                if ((NPC.getName().toLowerCase()).equals(s)) {
                    if (!NPC.isDead() && NPC.isValid() && NPC.getLevel() > 0) {
                        if ((!BANKING && calc.distanceBetween(START_TILE, NPC.getLocation()) <= RADIUS) || (BANKING && calc.distanceBetween(getRSTile(), NPC.getLocation()) <= RADIUS)) {
                            if (underAttack()) {
                                if (NPCAttackingPlayer(NPC)) {
                                    return true;
                                }
                            } else {
                                if (MULTI_COMBAT) {
                                    if (attacking()) {
                                        if (attackingNPC(NPC)) {
                                            return true;
                                        }
                                    } else {
                                        return true;
                                    }
                                } else {
                                    if (!NPCInCombat(NPC)) {
                                        return true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return false;
        }
    };
    private final Filter<RSGroundItem> LOOT_FILTER = new Filter<RSGroundItem>() {

        @Override
        public boolean accept(RSGroundItem LOOT) {
            for (String s : LOOT_ARRAY) {
                try {
                    if ((LOOT.getItem().getName().toLowerCase()).equals(s)) {
                        if ((!BANKING && calc.distanceBetween(START_TILE, LOOT.getLocation()) <= RADIUS) || (BANKING && calc.distanceBetween(getRSTile(), LOOT.getLocation()) <= RADIUS)) {
                            if (B2PB2B_TAB || B2PB2B_SPELL) {
                                if (LOOT.getItem().getName().toLowerCase().contains("bones")) {
                                    if (SPEC_SWAP) {
                                        if (inventory.getCount() <= 25) {
                                            return true;
                                        }
                                    } else {
                                        if (inventory.getCount() <= 26) {
                                            return true;
                                        }
                                    }
                                } else {
                                    return true;
                                }
                            } else {
                                return true;
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
            return false;
        }
    };

    public boolean attacking() {
        try {
            return getMyPlayer().getInteracting() instanceof RSNPC;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean attackingNPC(RSNPC n) {
        try {
            return getMyPlayer().getInteracting().equals(n);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean availableNPC() {
        if (npcs.getNearest(NPC_FILTER) != null) {
            TARGET = npcs.getNearest(NPC_FILTER);
            return true;
        }
        return false;
    }

    public boolean NPCAttackingPlayer(RSNPC n) {
        try {
            if (n.isInteractingWithLocalPlayer()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean attackingTarget() {
        try {
            if (getMyPlayer().getInteracting().equals(TARGET)) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public boolean NPCInCombat(RSNPC NPC) {
        return NPC.getInteracting() instanceof RSCharacter || NPC.isInCombat();
    }

    public boolean isPlayerInCombat() {
        for (RSNPC NPC : npcs.getAll()) {
            try {
                if (NPC.isInteractingWithLocalPlayer()) {
                    return true;
                }
            } catch (Exception e) {
            }
            try {
                if (getMyPlayer().getInteracting().equals(NPC)) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public boolean underAttack() {
        for (RSNPC NPC : npcs.getAll()) {
            try {
                if (NPC.isInteractingWithLocalPlayer()) {
                    return true;
                }
            } catch (Exception e) {
            }
        }
        return false;
    }

    public class GUI extends javax.swing.JFrame {

        public GUI() {
            initComponents();
        }

        private void initComponents() {

            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jLabel3 = new javax.swing.JLabel();
            NPCBox = new javax.swing.JTextField();
            jLabel4 = new javax.swing.JLabel();
            LOOTBox = new javax.swing.JTextField();
            jLabel5 = new javax.swing.JLabel();
            RADIUSBox = new javax.swing.JTextField();
            BURYBox = new javax.swing.JCheckBox();
            BANKBox = new javax.swing.JCheckBox();
            STARTButton = new javax.swing.JButton();
            MULTICOMBATBox1 = new javax.swing.JCheckBox();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            jLabel1.setText("AIOFighter v" + AIOFighter.class.getAnnotation(ScriptManifest.class).version());

            jLabel2.setText("by hellomot0123");

            jLabel3.setText("NPCs to Fight:");

            NPCBox.setText("Chicken,Cow,etc");

            jLabel4.setText("Loot to take:");

            LOOTBox.setText("Coins,Bones,etc");

            jLabel5.setText("Radius (# tiles):");

            BURYBox.setText("Bury bones/ Scatter ash");

            BANKBox.setText("Use banking");
            //BANKBox.setEnabled(false);

            STARTButton.setText("START");
            STARTButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    STARTButtonActionPerformed(evt);
                }
            });

            MULTICOMBATBox1.setText("Multicombat area?");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addComponent(jLabel2).addComponent(jLabel3).addComponent(NPCBox, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel4).addComponent(LOOTBox, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(RADIUSBox, javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addComponent(BANKBox).addComponent(BURYBox).addComponent(MULTICOMBATBox1).addComponent(STARTButton)).addGap(50, 50, 50)));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(NPCBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel4).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(LOOTBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel5).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(RADIUSBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(BURYBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(BANKBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(MULTICOMBATBox1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(STARTButton).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

            pack();
        }

        private void STARTButtonActionPerformed(java.awt.event.ActionEvent evt) {
            String[] temp = NPCBox.getText().toLowerCase().split(",");
            NPC_ARRAY = temp;
            String[] temp2 = LOOTBox.getText().toLowerCase().split(",");
            LOOT_ARRAY = temp2;
            BURY_MODE = BURYBox.isSelected();
            BANKING = BANKBox.isSelected();
            MULTI_COMBAT = MULTICOMBATBox1.isSelected();
            try {
                RADIUS = Integer.parseInt(RADIUSBox.getText());
            } catch (Exception e) {
                RADIUS = 10;
            }
            dispose();
            START = true;
        }
        private javax.swing.JCheckBox BANKBox;
        private javax.swing.JCheckBox BURYBox;
        private javax.swing.JTextField LOOTBox;
        private javax.swing.JCheckBox MULTICOMBATBox1;
        private javax.swing.JTextField NPCBox;
        private javax.swing.JTextField RADIUSBox;
        private javax.swing.JButton STARTButton;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
    }

    public class Potion {

        public int[] ids;
        public int[][] skillboosted;
        public boolean getHealth;
        public boolean isPoisoned;
        public boolean getLifepoints;
        public boolean needPray;
        public boolean needSumm;
        public boolean needRestore;

        public Potion(int[] ids, int[][] skillboosted, boolean getHealth, boolean isPoisoned, boolean getLifepoints, boolean needPray, boolean needSumm, boolean needRestore) {
            this.ids = ids;
            this.skillboosted = skillboosted;
            this.getHealth = getHealth;
            this.isPoisoned = isPoisoned;
            this.getLifepoints = getLifepoints;
            this.needPray = needPray;
            this.needSumm = needSumm;
            this.needRestore = needRestore;
        }

        public int[] getIDs() {
            return ids;
        }

        public boolean activate() {
            boolean temp1, temp2, temp3, temp4, temp5, temp6;
            int counter = 0;
            if (skillboosted == null) {
                temp1 = false;
            } else {
                for (int[] i : skillboosted) {
                    if (!skillBoosted(i[0], i[1], i[2])) {
                        counter++;
                    }
                }
                if (counter > 0) {
                    temp1 = true;
                } else {
                    temp1 = false;
                }
            }
            if (!getHealth) {
                temp2 = false;
            } else {
                temp2 = combat.getHealth() < 60;
            }
            if (!needRestore) {
                temp3 = false;
            } else {
                temp3 = needRestore();
            }
            if (!needPray) {
                temp4 = false;
            } else {
                temp4 = needPray();
            }
            if (!needSumm) {
                temp5 = false;
            } else {
                temp5 = needSumm();
            }
            if (!isPoisoned) {
                temp6 = false;
            } else {
                temp6 = combat.isPoisoned();
            }
            if (!getLifepoints) {
                return (temp1 || temp2 || temp3 || temp4 || temp5 || temp6);
            } else {
                return (temp1 || temp2 || temp3 || temp4 || temp5 || temp6) && (combat.getLifePoints() > 500);
            }
        }

        public boolean havePotion() {
            return inventory.containsOneOf(ids);
        }

        public void drinkPotion() {
            for (int i = 3; i >= 0; i--) {
                if (inventory.contains(ids[i])) {
                    inventory.getItem(ids[i]).doClick(true);
                    sleep(2000);
                    return;
                }
            }
        }

        public boolean needPray() {
            return (((Integer.parseInt(interfaces.get(749).getComponent(6).getText()) * 10) / skills.getCurrentLevel(Skills.PRAYER)) < 60);
        }

        public boolean needSumm() {
            return (summoning.getSummoningPoints() == 0);
        }

        public boolean skillBoosted(int skill, int constant, int percent) {
            double temp1 = (double) skills.getRealLevel(skill);
            double temp2 = ((double) percent / 100) + 1;
            int temp3 = (int) (temp1 * temp2);
            int temp4 = (temp3 + constant) - skills.getRealLevel(skill);
            int temp5 = temp4 / 2;
            return (skills.getCurrentLevel(skill) >= (skills.getRealLevel(skill) + temp5));
        }

        public boolean needRestore() {
            for (int counter = 0; counter <= 24; counter++) {
                if (counter != Skills.CONSTITUTION && counter != Skills.PRAYER && counter != Skills.SUMMONING) {
                    if (!skillBoosted(counter, 0, 0)) {
                        return true;
                    }
                }
            }
            return false;
        }
    }

    public void recording() {
        if (walk) {
            ArrayList<RSTile> temp = new ArrayList<RSTile>();
            temp.add(getMyPlayer().getLocation());
            while (walk) {
                if (calc.distanceTo(temp.get((temp.size() - 1))) >= 10 && calc.distanceTo(temp.get((temp.size() - 1))) < 15) {
                    if (calc.tileOnMap(getMyPlayer().getLocation())) {
                        temp.add(getMyPlayer().getLocation());
                    }
                }
            }
            temp.add(getMyPlayer().getLocation());
            if (frombank) {
                pathfrombank.add(temp);
                canUpdate = true;
            } else if (tobank) {
                pathtobank.add(temp);
                canUpdate = true;
            }
        }
    }

    public class GUI2 extends javax.swing.JFrame {

        public GUI2() {
            initComponents();
        }

        private void initComponents() {

            jTabbedPane1 = new javax.swing.JTabbedPane();
            jPanel1 = new javax.swing.JPanel();
            walkingToggle = new javax.swing.JToggleButton();
            interactToggle = new javax.swing.JToggleButton();
            jScrollPane1 = new javax.swing.JScrollPane();
            tobankBox = new javax.swing.JTextArea();
            jScrollPane2 = new javax.swing.JScrollPane();
            frombankBox = new javax.swing.JTextArea();
            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            frombankradio = new javax.swing.JRadioButton();
            tobankradio = new javax.swing.JRadioButton();
            jLabel3 = new javax.swing.JLabel();
            jLabel4 = new javax.swing.JLabel();
            jTextField1 = new javax.swing.JTextField();
            jTextField2 = new javax.swing.JTextField();
            jPanel2 = new javax.swing.JPanel();
            jScrollPane3 = new javax.swing.JScrollPane();
            table = new javax.swing.JTable();
            addrow = new javax.swing.JButton();
            STARTButton = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            walkingToggle.setText("START WALKING");
            walkingToggle.setToolTipText("");
            walkingToggle.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    walkingToggleActionPerformed(evt);
                }
            });

            interactToggle.setText("INTERACT WITH OBJECT:");
            interactToggle.setToolTipText("");
            interactToggle.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    interactToggleActionPerformed(evt);
                }
            });

            tobankBox.setColumns(20);
            tobankBox.setRows(5);
            tobankBox.setEditable(false);
            jScrollPane1.setViewportView(tobankBox);

            frombankBox.setColumns(20);
            frombankBox.setRows(5);
            frombankBox.setEditable(false);
            jScrollPane2.setViewportView(frombankBox);

            jLabel1.setText("From Bank");

            jLabel2.setText("To Bank");

            frombankradio.setText("From Bank");
            frombankradio.setSelected(true);
            frombank = true;
            frombankradio.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    frombankradioActionPerformed(evt);
                }
            });

            tobankradio.setText("To Bank");
            tobankradio.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    tobankradioActionPerformed(evt);
                }
            });

            jLabel3.setText("Object ID:");

            jLabel4.setText("Action to perform:");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGap(17, 17, 17).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(frombankradio).addComponent(tobankradio).addComponent(walkingToggle)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 122, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(interactToggle).addGroup(jPanel1Layout.createSequentialGroup().addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel4).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(316, Short.MAX_VALUE).addComponent(jLabel2).addGap(74, 74, 74)).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addContainerGap(310, Short.MAX_VALUE).addComponent(jLabel1).addGap(68, 68, 68)));
            jPanel1Layout.setVerticalGroup(
                    jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup().addGap(5, 5, 5).addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jLabel2).addGap(4, 4, 4)).addGroup(jPanel1Layout.createSequentialGroup().addGap(35, 35, 35).addComponent(frombankradio).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(tobankradio).addGap(18, 18, 18).addComponent(walkingToggle).addGap(18, 18, 18))).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel1Layout.createSequentialGroup().addComponent(interactToggle).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel3).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel4).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))).addContainerGap()));

            jTabbedPane1.addTab("Bank Path", jPanel1);

            table.setModel(new javax.swing.table.DefaultTableModel(
                    new Object[][]{
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null},
                        {null, null}
                    },
                    new String[]{
                        "Item ID", "Item Quantity"
                    }) {

                Class[] types = new Class[]{
                    java.lang.Integer.class, java.lang.Integer.class
                };

                @Override
                public Class getColumnClass(int columnIndex) {
                    return types[columnIndex];
                }
            });
            jScrollPane3.setViewportView(table);
            table.getColumnModel().getColumn(0).setHeaderValue("Item ID");
            table.getColumnModel().getColumn(1).setHeaderValue("Item Quantity");

            addrow.setText("Add row");
            addrow.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addrowActionPerformed(evt);
                }
            });

            STARTButton.setText("START");
            STARTButton.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    STARTButtonActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 344, javax.swing.GroupLayout.PREFERRED_SIZE).addGroup(jPanel2Layout.createSequentialGroup().addComponent(addrow).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(STARTButton))).addContainerGap(74, Short.MAX_VALUE)));
            jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup().addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(18, 18, 18).addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(addrow).addComponent(STARTButton)).addGap(19, 19, 19)));

            jTabbedPane1.addTab("Withdraw", jPanel2);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 433, Short.MAX_VALUE));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jTabbedPane1));

            pack();
        }

        private void updateBoxes() {
            while (!canUpdate) {
                sleep(1);
            }
            frombankBox.setText("");
            for (Object o : pathfrombank) {
                if (o instanceof String[]) {
                    String[] temp = (String[]) o;
                    frombankBox.append(temp[0] + " : " + temp[1] + "\n");
                } else if (o instanceof ArrayList) {
                    ArrayList<RSTile> arrayList = (ArrayList<RSTile>) o;
                    frombankBox.append("X:" + arrayList.get(0).getX() + "Y:" + arrayList.get(0).getY() + "Z:" + arrayList.get(0).getZ() + " -> " + "X:" + arrayList.get((arrayList.size() - 1)).getX() + "Y:" + arrayList.get((arrayList.size() - 1)).getY() + "Z:" + arrayList.get((arrayList.size() - 1)).getZ() + "\n");
                }
            }
            tobankBox.setText("");
            for (Object o : pathtobank) {
                if (o instanceof String[]) {
                    String[] temp = (String[]) o;
                    tobankBox.append(temp[0] + " : " + temp[1] + "\n");
                } else if (o instanceof ArrayList) {
                    ArrayList<RSTile> arrayList = (ArrayList<RSTile>) o;
                    tobankBox.append("X:" + arrayList.get(0).getX() + "Y:" + arrayList.get(0).getY() + "Z:" + arrayList.get(0).getZ() + " -> " + "X:" + arrayList.get((arrayList.size() - 1)).getX() + "Y:" + arrayList.get((arrayList.size() - 1)).getY() + "Z:" + arrayList.get((arrayList.size() - 1)).getZ() + "\n");
                }
            }
            canUpdate = false;
        }

        private void addrowActionPerformed(java.awt.event.ActionEvent evt) {
            ((DefaultTableModel) table.getModel()).addRow(new Object[]{null, null});
        }

        private void STARTButtonActionPerformed(java.awt.event.ActionEvent evt) {
            try {
                table.getCellEditor().stopCellEditing();
            } catch (Exception e) {
            }
            Object temp = pathtobank.get((pathtobank.size() - 1));
            Object temp2 = pathfrombank.get((pathfrombank.size() - 1));
            if (temp instanceof ArrayList && temp2 instanceof ArrayList) {
                for (int i = 0; i < table.getRowCount(); i++) {
                    if (table.getValueAt(i, 0) == null) {
                        continue;
                    } else {
                        map.put((Integer) table.getValueAt(i, 0), (Integer) table.getValueAt(i, 1));
                    }
                }
                FIRST_TIME = false;
                env.setUserInput(0);
                dispose();
            } else {
                log("Please end both paths with some walking!");
            }
        }

        private void frombankradioActionPerformed(java.awt.event.ActionEvent evt) {
            if (frombankradio.isSelected()) {
                tobankradio.setSelected(false);
                frombank = true;
                tobank = false;
            } else {
                frombankradio.setSelected(true);
            }
        }

        private void tobankradioActionPerformed(java.awt.event.ActionEvent evt) {
            if (tobankradio.isSelected()) {
                frombankradio.setSelected(false);
                tobank = true;
                frombank = false;
            } else {
                tobankradio.setSelected(true);
            }
        }

        private void walkingToggleActionPerformed(java.awt.event.ActionEvent evt) {
            if (walkingToggle.isSelected()) {
                walk = true;
                walkingToggle.setText("STOP WALKING");
                interactToggle.setEnabled(false);
            } else {
                walk = false;
                walkingToggle.setText("START WALKING");
                interactToggle.setEnabled(true);
                updateBoxes();
            }
        }

        private void interactToggleActionPerformed(java.awt.event.ActionEvent evt) {
            if (interactToggle.isSelected()) {
                String[] temp2 = new String[2];
                temp2[0] = jTextField2.getText();
                temp2[1] = jTextField1.getText();
                if (frombank) {
                    pathfrombank.add(temp2);
                    canUpdate = true;
                } else if (tobank) {
                    pathtobank.add(temp2);
                    canUpdate = true;
                }
                interactToggle.setSelected(false);
                updateBoxes();
            }
        }
        private javax.swing.JButton STARTButton;
        private javax.swing.JButton addrow;
        private javax.swing.JTextArea frombankBox;
        private javax.swing.JRadioButton frombankradio;
        private javax.swing.JToggleButton interactToggle;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JScrollPane jScrollPane3;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
        private javax.swing.JTable table;
        private javax.swing.JTextArea tobankBox;
        private javax.swing.JRadioButton tobankradio;
        private javax.swing.JToggleButton walkingToggle;
    }
}
