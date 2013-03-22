
import com.rarebot.script.methods.Skills;
import com.rarebot.script.methods.FriendChat.User;
import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSGroundItem;
import com.rarebot.script.wrappers.RSInterface;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSWeb;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.methods.Equipment;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Magic.Spell;
import com.rarebot.script.methods.Prayer.Book;
import com.rarebot.script.util.Filter;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSPlayer;

@ScriptManifest(authors = {"Xiffs"},
version = 1.3,
description = "Gains zeals for you! Pro edition!",
keywords = {"Zeals"},
name = "iSoulWarsPro")
public class SoulWarsPro extends Script implements MessageListener, PaintListener, MouseListener {

    // GUI Stuff
    public AutoZeals gui;
    public dynSigGUI gui2;
    public boolean canStart = false;
    String x;
    String z;
    String y;
    int west;
    int east;
    int mouseSpeed;
    int current;
    int other;
    boolean show2 = true;
    int lastZeal;
    int lastWonz;
    int lastLostz;
    int lastKicked;
    int lastGames;
    int[] blueAvatar = {8597}, redAvatar = {8596};
    int b, r, bm, rm;
    boolean nonCombat = false;
    int norm, spec;
    boolean specEquiped = false;
    boolean useTwoSpec = false;
    String dynSigUser;
    RSTile eastTile = new RSTile(1891, 3234);
    RSTile westTile;
    RSArea obby = new RSArea(new RSTile(1911, 3216), new RSTile(1876, 3245));
    RSTile mid = new RSTile(1900, 3230);
    RSTile blueBan = new RSTile(1812, 3261);
    RSTile redBan = new RSTile(1964, 3209);
    int[] fragment = {14639, 14646, 15792};
    RSArea jelliesNorth = new RSArea(new RSTile(1906, 3263), new RSTile(1869, 3248));
    RSArea jelliesSouth = new RSArea(new RSTile(1899, 3214), new RSTile(1873, 3199));
    RSArea pyreNorth = new RSArea(new RSTile(1856, 3258), new RSTile(1834, 3238));
    RSArea pyreSouth = new RSArea(new RSTile(1938, 3222), new RSTile(1917, 3204));
    RSTile ps = new RSTile(1924, 3212);
    RSTile pn = new RSTile(1844, 3247);
    RSTile js = new RSTile(1882, 3208);
    RSTile jn = new RSTile(1889, 3252);
    RSArea avaRed = new RSArea(new RSTile(1976, 3261), new RSTile(1959, 3244));
    RSArea avaBlue = new RSArea(new RSTile(1976, 3261), new RSTile(1959, 3244));
    RSTile avaRedTile = new RSTile(1966, 3246);
    RSTile avaBlueTile = new RSTile(1807, 3214);

    // Entering room stuff
    public enum State {

        Enter, Wait, Leave, Bury, Fight, Heal, jellies, pyres, avatar, random, skiller
    };

    public enum Team {

        Red, blue, lastwon, lastlost, average, most
    };
    public Team toEnter;
    int jellies = 8599;
    int red = 42030;
    int blue = 42029;
    int green = 42031;
    RSArea redArea = new RSArea(new RSTile(1909, 3167), new RSTile(1899, 3156));
    RSArea blueArea = new RSArea(new RSTile(1880, 3167), new RSTile(1869, 3157));
    RSArea lobbyArea = new RSArea(new RSTile(1918, 3187), new RSTile(1862, 3149));
    RSTile redTile = new RSTile(1899, 3162);
    RSTile blueTile = new RSTile(1880, 3162);
    RSTile tile;
    Long startTime;
    private final static int[] BarrierID = {42013, 42014, 42015, 42016, 42017, 42018};
    public boolean useSpec = false;
    int minAmount;
    int bandage = 14640;
    // Paint stuff
    String status;
    int games, zeal, lost, won, tied, lastLost, lastWon;
    int[] bandageTable = {42023, 42024};
    int[] barricade = {8600};
    boolean getSupplies = false;
    boolean usePrayer = false;
    // Inside game
    RSArea redYard = new RSArea(new RSTile(1951, 3234), new RSTile(1958, 3244));
    RSArea blueYard = new RSArea(new RSTile(1816, 3220), new RSTile(1823, 3230));
    RSArea westGrave = new RSArea(new RSTile(1841, 3217), new RSTile(1843, 3219));
    RSArea eastGrave = new RSArea(new RSTile(1932, 3244), new RSTile(1934, 3246));

    public boolean onStart() {
        startTime = System.currentTimeMillis();
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                gui = new AutoZeals();
                gui.setVisible(true);


            }
        });


        return true;
    }

    public State doing() {
        if (!interfaces.get(836).isValid()) {
            if (!atArea(blueArea) || !atArea(redArea)) {
                return State.Enter;
            }
            if (atArea(blueArea) || !atArea(redArea)) {
                return State.Wait;
            }
        } else if (interfaces.get(836).isValid()) {
            if (atArea(redYard) || atArea(blueYard) || atArea(eastGrave) || atArea(westGrave)) {
                return State.Leave;
            } else {
                if (z.toLowerCase().contains("@")) {
                    return State.Fight;
                } else if (z.toLowerCase().contains("pick")) {

                    return State.Bury;
                } else if (z.toLowerCase().contains("heal")) {
                    return State.Heal;
                } else if (z.toLowerCase().contains("pyre")) {
                    return State.pyres;
                } else if (z.toLowerCase().contains("jel")) {
                    return State.jellies;
                } else if (z.toLowerCase().contains("ava")) {
                    return State.avatar;
                } else if (z.toLowerCase().contains("random")) {
                    return random();
                } else if (z.toLowerCase().contains("pure")) {
                    return pure();
                }
            }
        }
        return State.Wait;

    }

    private State pure() {
        switch (random(1, 10)) {
            case 1:
                return State.Bury;
            case 2:
                return State.Heal;
            case 3:
                return State.Bury;
            case 4:
                return State.Heal;
            case 5:
                return State.Bury;
            case 6:
                return State.Heal;
            case 7:
                return State.Bury;
            case 8:
                return State.Heal;
            case 9:
                return State.Bury;
            case 10:
                return State.Heal;

        }
        return State.Bury;
    }

    private State random() {
        switch (random(0, 6)) {
            case 1:
                return State.Bury;
            case 2:
                return State.Heal;
            case 3:
                return State.Fight;
            case 4:
                if (canAttackPyres()) {
                    return State.pyres;
                }
            case 5:
                if (canAttackJellies()) {
                    return State.jellies;
                }
            case 6:
                if (canAttackAva()) {
                    return State.avatar;
                }
        }
        return State.Bury;
    }

    @Override
    public int loop() {
        if (canStart) {
            mouse.setSpeed(mouseSpeed);
            try {
                antiBan();
                mouse.moveRandomly(7);
                if (!game.isLoggedIn()) {
                    game.login();
                }
                while (getMyPlayer().isMoving()) {
                    sleep(1000);
                }
                switch (doing()) {
                    case Wait:
                        status = "Waiting";
                        sleep(2000);
                        mouse.moveRandomly(7);
                        mouse.moveSlightly();
                        antiBan();
                        int r = random(1, 100);
                        switch (r) {
                            case 1:
                                mouse.moveRandomly(7);
                                camera.setAngle(170);
                                break;

                            case 10:
                                game.openTab(Tab.MAGIC);
                                break;

                            case 30:
                                game.openTab(Tab.PRAYER);

                            case 40:
                                camera.moveRandomly(3);
                                break;

                            case 60:
                                game.openTab(Tab.QUESTS);
                        }
                        mouse.moveRandomly(5);
                        break;

                    case Enter:
                        status = "Entering";
                        enter();
                        break;

                    case Leave:
                        status = "Leaving";
                        leaveGraveYard();
                        pray();
                        current = getMyPlayer().getTeam();

                        break;

                    case Bury:
                        status = "Picking + Burying";
                        BuryPick();
                        pray();
                        break;

                    case Fight:
                        status = "Fighting";
                        if (!getMyPlayer().isDead()) {


                            if (!atArea(obby)) {
                                walkTo(mid);

                            } else {
                                if (ActivityBarPercent() < 300) {
                                    pick();
                                    if (inventory.isFull()) {
                                        if (inventory.contains(14638)) {


                                            inventory.getItem(14638).interact("Destroy");

                                            if (interfaces.canContinue()) {
                                                interfaces.clickContinue();
                                            }
                                            sleep(1000);
                                            clickDialogOption("Yes");
                                        }
                                    }
                                } else {
                                    special();
                                    fight();
                                    if (barricade()) {
                                        sleep(200);
                                    }
                                }
                            }
                        }
                        pray();
                        break;

                    case Heal:
                        if (!getMyPlayer().isDead()) {
                            if (inventory.contains(bandage)) {
                                status = "Healing others";
                                if (!atArea(obby)) {
                                    walkTo(mid);
                                } else {
                                    healOthers();
                                }
                            } else {
                                status = "Getting supplies";
                                getSupplies();
                            }
                            pray();
                        }
                        break;

                    case pyres:
                        if (!getMyPlayer().isDead()) {
                            if (ActivityBarPercent() < 300) {
                                if (!inventory.isFull()) {
                                    pick();
                                } else {
                                    destroy();
                                }
                            } else {
                                special();
                                attackPyres();
                            }
                            pray();
                        }
                        break;

                    case jellies:
                        if (!getMyPlayer().isDead()) {
                            if (ActivityBarPercent() < 300) {
                                if (!inventory.isFull()) {
                                    pick();
                                } else {
                                    destroy();
                                }
                            } else {

                                special();
                                attackJellies();
                            }
                            pray();
                        }
                        break;

                    case avatar:
                        if (!getMyPlayer().isDead()) {
                            if (canAttackAva()) {
                                if (ActivityBarPercent() < 300) {
                                    if (!inventory.isFull()) {
                                        pick();
                                    } else {
                                        destroy();
                                    }
                                } else {
                                    special();
                                    attAva();
                                }
                            } else {
                                BuryPick();
                            }
                            pray();
                        }
                        break;

                }
            } catch (Exception e) {
            }
        }
        return random(500, 900);
    }

    public void joinHighestCombat() {



        if (r == 0) {
            walkTo(redTile);
            sleep(7000);
            r = getAvarage(redArea);
            log("The average of the red team is " + r);
        }

        sleep(2000);

        if (b == 0) {
            walkTo(blueTile);
            sleep(7000);
            b = getAvarage(blueArea);
            log("The average of the blue team is " + b);
        }
        sleep(2000);

        if (b > r) {
            if (!atArea(blueArea)) {
                enterBlue();

                int s = settings.getSetting(settings.SETTING_MOUSE_BUTTONS);

            }
        } else {
            if (!atArea(redArea)) {
                enterRed();
            }
        }


    }

    public void joinMost() {

        if (rm == 0) {
            walkTo(redTile);
            sleep(7000);
            rm = getMost(redArea);
            log("The amount of players in red team is " + rm);
        }

        sleep(2000);

        if (bm == 0) {

            walkTo(blueTile);
            sleep(7000);
            bm = getMost(blueArea);
            log("The amount of players in blue team is " + bm);
        }
        sleep(2000);

        if (bm > rm) {
            enterBlue();
        } else {
            enterRed();
        }
    }

    public void BuryPick() {
        if (!inventory.isFull()) {
            pick();
        } else {
            bury();
        }
    }

    public void pick() {




        if (!inventory.isFull()) {

            RSGroundItem bones = groundItems.getNearest(14638);
            if (bones != null) {
                if (bones.isOnScreen()) {
                    camera.setAngle(180);
                    bones.interact("Take " + bones.getItem().getName());
                } else {
                    camera.turnTo(bones.getLocation());
                    if (calc.distanceTo(bones.getLocation()) > 6) {
                        walking.walkTileMM(bones.getLocation());
                        while (getMyPlayer().isMoving()) {
                            sleep(1000);
                        }

                    }
                }

            } else {

                walkTo(mid);


            }
        }
    }

    public void fight() {
        try {
            RSPlayer p = opponent();
            if (p != null) {
                if (getMyPlayer().getInteracting() != null) {
                    walking.walkTileMM(p.getLocation());
                }
                if (p.isOnScreen()) {
                    if (getMyPlayer().getInteracting() == null) {
                        if (p.getTeam() != getMyPlayer().getTeam()) {


                            p.interact("Attack");
                        }
                    }
                } else {
                    camera.turnTo(p);
                    if (calc.distanceTo(p) >= 10) {
                        walking.walkTileMM(p.getLocation());
                    }

                }
            }
        } catch (Exception e) {
        }
    }

    public void bury() {
        if (getMyPlayer().getTeam() == 1) {
            if (gotWest()) {
                if (calc.distanceTo(westTile) > 4) {
                    walkTo(westTile);
                } else {
                    if (inventory.contains(14638)) {
                        if (ActivityBarPercent() < 300) {
                            inventory.getItem(14638).doClick(true);
                        } else {
                            status = "Waiting....";
                        }
                    }
                }
            } else if (gotEast()) {
                if (calc.distanceTo(eastTile) > 4) {
                    walkTo(eastTile);
                } else {
                    if (inventory.contains(14638)) {
                        if (ActivityBarPercent() < 300) {
                            inventory.getItem(14638).doClick(true);
                        } else {
                            status = "Waiting....";
                        }
                    }
                }
            } else {
                destroy();
            }


        } else if (getMyPlayer().getTeam() == 2) {
            if (gotWest()) {
                if (calc.distanceTo(westTile) < 4) {
                    walkTo(westTile);
                } else {
                    if (inventory.contains(14638)) {
                        if (ActivityBarPercent() < 300) {
                            inventory.getItem(14638).doClick(true);
                        } else {
                            status = "Waiting....";
                        }
                    }
                }
            } else if (gotEast()) {
                if (calc.distanceTo(eastTile) < 4) {
                    walkTo(eastTile);
                } else {
                    if (inventory.contains(14638)) {
                        if (ActivityBarPercent() < 300) {
                            inventory.getItem(14638).doClick(true);
                        } else {
                            status = "Waiting....";
                        }
                    }
                }
            } else {
                destroy();
            }

        }
    }

    public void destroy() {
        if (inventory.contains(14638)) {


            inventory.getItem(14638).interact("Destroy");

            if (interfaces.canContinue()) {
                interfaces.clickContinue();
            }
            sleep(1000);
            clickDialogOption("Yes");
        }
    }

    public boolean outsideGame() {
        RSArea lob = lobbyArea;
        return lob.contains(getMyPlayer().getLocation());
    }

    public void leaveGraveYard() {
        RSObject b = objects.getNearest(BarrierID);
        if (b != null) {
            walking.walkTileMM(b.getLocation());
            camera.turnTo(b);

            if (b.isOnScreen()) {
                if (getMyPlayer().getNPCID() == 8623) {
                    walking.walkTileMM(b.getLocation());
                    camera.turnTo(b);
                } else {
                    camera.turnTo(b);
                    mouse.move(b.getPoint());
                    mouse.click(true);
                    sleep(2000);
                }
            }
        }
    }

    public boolean atArea(RSArea area) {
        RSArea Ar = area;
        return area.contains(getMyPlayer().getLocation());
    }

    public Team whatToEnter() {

        if (x.toLowerCase().equals("always red")) {
            return Team.Red;
        } else if (x.toLowerCase().equals("always blue")) {
            return Team.blue;
        } else if (x.toLowerCase().contains("won")) {
            return Team.lastwon;
        } else if (x.toLowerCase().contains("lost")) {
            return Team.lastlost;
        } else if (x.toLowerCase().contains("average")) {

            return Team.average;
        } else if (x.toLowerCase().contains("most")) {
            return Team.most;
        } else if (x.toLowerCase().contains("Randomly switch between blue & red")) {
            return redBlue();
        } else if (x.toLowerCase().contains("between last lost")) {
            return wonLost();
        } else if (x.toLowerCase().contains("players and highest average")) {
            return HighMost();
        } else if (x.toLowerCase().contains("everything")) {
            return totalRandom();
        } else {
            return Team.Red;

        }
    }

    public Team totalRandom() {
        switch (random(1, 6)) {
            case 1:
                return Team.most;
            case 2:
                return Team.average;
            case 3:
                return Team.Red;
            case 4:
                return Team.blue;
            case 5:
                return Team.lastlost;
            case 6:
                return Team.lastwon;
        }
        return Team.Red;
    }

    public Team HighMost() {
        switch (random(1, 2)) {
            case 1:
                return Team.most;
            case 2:
                return Team.average;
        }
        return Team.most;
    }

    public Team wonLost() {
        switch (random(1, 2)) {
            case 1:
                return Team.lastwon;
            case 2:
                return Team.lastlost;
        }
        return Team.lastlost;
    }

    public Team redBlue() {
        switch (random(1, 2)) {
            case 1:
                return Team.blue;
            case 2:
                return Team.Red;
        }
        return Team.Red;
    }

    public void enterRed() {
        if (!atArea(redArea)) {
            RSObject bar = objects.getNearest(red);
            if (bar != null) {
                if (bar.isOnScreen()) {

                    walking.walkTileMM(bar.getLocation());
                    sleep(1000);
                    camera.turnTo(bar);
                    sleep(500);
                    bar.doClick(true);
                    sleep(2000);
                    if (interfaces.canContinue()) {
                        interfaces.clickContinue();
                        sleep(2000);
                    }
                    clickDialogOption("Enter");
                    sleep(3000);


                } else {
                    camera.turnTo(bar);
                    if (calc.distanceTo(redTile) > 5) {
                        walkTo(redTile);
                    }
                }
            }

        }
    }

    public void enterBlue() {
        if (!atArea(blueArea)) {
            RSObject bar = objects.getNearest(blue);
            if (bar != null) {
                if (bar.isOnScreen()) {

                    walking.walkTileMM(bar.getLocation());
                    sleep(1000);
                    camera.turnTo(bar);
                    sleep(500);
                    bar.doClick(true);
                    sleep(2000);
                    if (interfaces.canContinue()) {
                        interfaces.clickContinue();
                        sleep(2000);
                    }
                    clickDialogOption("Enter");
                    sleep(3000);



                } else {
                    camera.turnTo(bar);
                    if (calc.distanceTo(blueTile) > 5) {
                        walkTo(blueTile);
                    }
                }
            }
        }
    }

    public void drop() {
        if (nonCombat = true) {
            while (inventory.contains(229)) {
                game.openTab(Tab.INVENTORY);
                inventory.getItem(229).interact("Drop");
            }
            nonCombat = false;
        }
    }

    public void enter() {

        switch (whatToEnter()) {
            case most:
                joinMost();
                drop();
                break;
            case average:

                joinHighestCombat();
                drop();
                break;
            case Red:
                enterRed();
                drop();
                break;
            case blue:
                enterBlue();
                drop();
                break;
            case lastwon:
                if (lastWon == 1) {
                    if (!atArea(blueArea)) {
                        joinLastWon();
                        drop();
                    }
                } else {
                    if (!atArea(redArea)) {
                        joinLastWon();
                        drop();
                    }

                }
                break;
            case lastlost:
                if (lastLost == 1) {
                    if (!atArea(blueArea)) {
                        joinLastLost();
                        drop();
                    }
                } else {
                    if (!atArea(redArea)) {
                        joinLastLost();
                        drop();
                    }

                }
                break;

        }
    }

    public void joinLastWon() {
        RSObject bar = objects.getNearest(red);
        if (lastWon == 1) {
            bar = objects.getNearest(blue);
            tile = blueTile;
        } else {
            bar = objects.getNearest(red);
            tile = redTile;
        }
        if (bar != null) {
            if (bar.isOnScreen()) {

                walking.walkTileMM(bar.getLocation());
                sleep(1000);
                camera.turnTo(bar);
                sleep(500);
                bar.doClick(true);
                sleep(2000);
                if (interfaces.canContinue()) {
                    interfaces.clickContinue();
                    sleep(2000);
                }
                clickDialogOption("Enter");
                sleep(3000);


            } else {
                camera.turnTo(bar);
                if (calc.distanceTo(tile) > 5) {
                    walkTo(tile);
                }
            }
        }
    }

    public void joinLastLost() {
        RSObject bar = objects.getNearest(red);
        if (lastLost == 1) {
            bar = objects.getNearest(blue);
            tile = blueTile;
        } else {
            bar = objects.getNearest(red);
            tile = redTile;
        }
        if (bar != null) {
            if (bar.isOnScreen()) {

                walking.walkTileMM(bar.getLocation());
                sleep(1000);
                camera.turnTo(bar);
                sleep(500);
                bar.doClick(true);
                sleep(2000);
                if (interfaces.canContinue()) {
                    interfaces.clickContinue();
                    sleep(2000);
                }
                clickDialogOption("Enter");
                sleep(3000);


            } else {
                camera.turnTo(bar);
                if (calc.distanceTo(tile) > 5) {
                    walkTo(tile);
                }
            }
        }
    }

    public void special() {
        if (useSpec) {
            if (combat.getSpecialBarEnergy() >= minAmount) {
                if (!combat.isSpecialEnabled()) {
                    if (useTwoSpec) {
                        newWeapon();
                        if (specEquiped) {
                            combat.setSpecialAttack(true);
                        }
                    } else {
                        combat.setSpecialAttack(true);
                    }
                }
            } else {
                if (equipment.getItem(Equipment.WEAPON).getID() == spec) {
                    specEquiped = true;
                }
                if (specEquiped) {
                    oldWeapon();
                }
            }
        }
    }

    private void healOthers() {
        camera.setPitch(true);
        try {
            if (ActivityBarPercent() < 300) {
                pick();
                destroy();
            } else {
                if (inventory.getSelectedItem() == null
                        && inventory.getSelectedItem().getID() == bandage) {
                    RSItem ban = inventory.getItem(bandage);
                    if (ban != null) {
                        ban.interact("Use");
                    }
                } else {
                    if (teamMate() != null) {
                        teamMate().interact("Use");
                    }
                    sleep(3000, 5000);
                }
            }






        } catch (Exception e) {
            log("Error:" + e.getLocalizedMessage());
        }
    }

    public void getSupplies() {
        if (getMyPlayer().getTeam() == 1) {
            if (calc.distanceTo(blueBan) > 5) {
                walkTo(blueBan);
            } else {
                RSObject banTable = objects.getNearest(bandageTable);
                if (banTable != null) {
                    if (banTable.isOnScreen()) {
                        banTable.interact("x");
                        sleep(2000);
                        if (interfaces.getComponent(752, 5) != null) {
                            keyboard.sendText("28", false);
                            sleep(3000);
                        }
                        if (interfaces.getComponent(752, 5) != null
                                && interfaces.getComponent(752, 5).getText().contains("28")
                                && !getMyPlayer().isInCombat()) {
                            keyboard.sendText("", true);
                            sleep(1800, 2000);
                        }
                    }
                }
            }

        } else {
            if (calc.distanceTo(redBan) > 5) {
                walkTo(redBan);
            } else {
                RSObject banTable = objects.getNearest(bandageTable);
                if (banTable != null) {
                    if (banTable.isOnScreen()) {
                        banTable.interact("x");
                        if (interfaces.getComponent(752, 4).isValid()) {
                            if (getMyPlayer().getInteracting() == null) {
                                keyboard.sendText("28", true);
                            }
                            sleep(3000);
                        }
                    }
                }
            }
        }
    }

    public class AutoZeals extends javax.swing.JFrame {

        public AutoZeals() {
            initComponents();
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            jFrame1 = new javax.swing.JFrame();
            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jSpinner1 = new javax.swing.JSpinner();
            jComboBox1 = new javax.swing.JComboBox();
            jLabel4 = new javax.swing.JLabel();
            jButton2 = new javax.swing.JButton();
            jLabel3 = new javax.swing.JLabel();
            jComboBox2 = new javax.swing.JComboBox();
            jLabel5 = new javax.swing.JLabel();
            jCheckBox1 = new javax.swing.JCheckBox();
            jLabel6 = new javax.swing.JLabel();
            jTextField1 = new javax.swing.JTextField();
            jLabel7 = new javax.swing.JLabel();
            jCheckBox2 = new javax.swing.JCheckBox();
            jLabel8 = new javax.swing.JLabel();
            jComboBox3 = new javax.swing.JComboBox();
            jLabel9 = new javax.swing.JLabel();
            jCheckBox3 = new javax.swing.JCheckBox();
            jLabel10 = new javax.swing.JLabel();
            jLabel11 = new javax.swing.JLabel();
            jTextField2 = new javax.swing.JTextField();
            jTextField3 = new javax.swing.JTextField();

            javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
            jFrame1.getContentPane().setLayout(jFrame1Layout);
            jFrame1Layout.setHorizontalGroup(
                    jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 400, Short.MAX_VALUE));
            jFrame1Layout.setVerticalGroup(
                    jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGap(0, 300, Short.MAX_VALUE));

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            jLabel1.setText("Method of entering:");

            jLabel2.setText("Mouse speed:");

            jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Always red", "Always blue", "Last lost", "Last won", "Highest combat level average", "Most players", "Randomly most players and highest average", "Switch between everything", "Switch randomly between last lost and last won", "Switch between most players and highest average", "Randomly switch between blue & red"}));

            jLabel4.setText("Note: The lower the faster");

            jButton2.setText("Start");
            jButton2.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton2ActionPerformed(evt);
                }
            });

            jLabel3.setText("How to keep up the activity bar?");

            jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Pure/Skiller mode (No exp)", "Randomize everything", "Pick bones and bury them (No prayer XP)", "Fight at obby", "Heal others", "Kill pyrefiends", "Kill jellies", "Attack opponents avatar"}));

            jLabel5.setText("Use special attack?");

            jCheckBox1.setText("Yes");

            jLabel6.setText("Minimum spec amount");

            jTextField1.setText("100");

            jLabel7.setText("Use quick prayer?");

            jCheckBox2.setText("Yes");

            jLabel8.setText("At:");

            jComboBox3.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Obelisk", "Anytime"}));

            jLabel9.setText("Use weapon out of inventory?");

            jCheckBox3.setText("Yes");

            jLabel10.setText("Spec wep ID");

            jLabel11.setText("Norm wep ID");

            jTextField2.setText("111");

            jTextField3.setText("12341");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel9).addComponent(jLabel3).addComponent(jLabel5).addComponent(jLabel7).addComponent(jLabel2).addComponent(jLabel4).addComponent(jLabel1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jComboBox1, 0, 247, Short.MAX_VALUE).addGap(89, 89, 89)).addGroup(layout.createSequentialGroup().addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap()).addGroup(layout.createSequentialGroup().addComponent(jComboBox2, 0, 326, Short.MAX_VALUE).addContainerGap()).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(jCheckBox3).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE).addComponent(jLabel10)).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCheckBox1).addComponent(jCheckBox2, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)).addGap(70, 70, 70).addComponent(jLabel8))).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addGroup(layout.createSequentialGroup().addComponent(jLabel6).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)).addComponent(jComboBox3, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))).addGroup(layout.createSequentialGroup().addGap(4, 4, 4).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel11).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))).addContainerGap(32, Short.MAX_VALUE)))).addGroup(layout.createSequentialGroup().addGap(197, 197, 197).addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE).addGap(235, 235, 235)));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jLabel2).addGap(1, 1, 1).addComponent(jLabel4)).addGroup(layout.createSequentialGroup().addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel3).addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel5).addComponent(jLabel6).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jCheckBox1)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel9).addComponent(jCheckBox3).addComponent(jLabel10).addComponent(jLabel11).addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(6, 6, 6).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel7).addComponent(jComboBox3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addComponent(jLabel8).addComponent(jCheckBox2)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE).addComponent(jButton2)));

            pack();
        }// </editor-fold>

        private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
            java.awt.EventQueue.invokeLater(new Runnable() {

                public void run() {
                    gui2 = new dynSigGUI();
                    gui2.setVisible(true);


                }
            });
            dispose();
            x = jComboBox1.getSelectedItem().toString();
            z = jComboBox2.getSelectedItem().toString();
            y = jComboBox3.getSelectedItem().toString();
            mouseSpeed = (Integer) jSpinner1.getValue();
            if (jCheckBox1.isSelected()) {
                useSpec = true;
            }
            if (jCheckBox2.isSelected()) {
                usePrayer = true;
            }
            minAmount = Integer.parseInt(jTextField1.getText().replaceAll("\\D", ""));
            spec = Integer.parseInt(jTextField3.getText().replaceAll("\\D", ""));
            norm = Integer.parseInt(jTextField2.getText().replaceAll("\\D", ""));
            if (jCheckBox3.isSelected()) {
                useTwoSpec = true;
            }
        }
        // Variables declaration - do not modify
        private javax.swing.JButton jButton2;
        private javax.swing.JCheckBox jCheckBox1;
        private javax.swing.JCheckBox jCheckBox2;
        private javax.swing.JCheckBox jCheckBox3;
        private javax.swing.JComboBox jComboBox1;
        private javax.swing.JComboBox jComboBox2;
        private javax.swing.JComboBox jComboBox3;
        private javax.swing.JFrame jFrame1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel10;
        private javax.swing.JLabel jLabel11;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JLabel jLabel7;
        private javax.swing.JLabel jLabel8;
        private javax.swing.JLabel jLabel9;
        private javax.swing.JSpinner jSpinner1;
        private javax.swing.JTextField jTextField1;
        private javax.swing.JTextField jTextField2;
        private javax.swing.JTextField jTextField3;
        // End of variables declaration
    }

    public void clickDialogOption(String o) {
        mouse.moveRandomly(7);

        RSComponent comp = getComponentContaining(o);
        if (comp != null) {
            comp.doClick(true);
        }

    }

    public RSComponent getComponentContaining(String contain) {
        final RSInterface[] valid = interfaces.getAll();
        for (final RSInterface iface : valid) {
            if (iface.getIndex() != 137) {
                final int len = iface.getChildCount();
                for (int i = 0; i < len; i++) {
                    final RSComponent child = iface.getComponent(i);
                    if (child.containsText(contain) && child.isValid() && child.getAbsoluteX() > 10 && child.getAbsoluteY() > 300) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    private void walkTo(RSTile tile) {
        if (atArea(redYard) || atArea(blueYard) || atArea(eastGrave) || atArea(westGrave)) {
            leaveGraveYard();
        }
        RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), tile);
        if (calc.distanceTo(tile) > 4) {
            walkToTile.step();
        }
    }

    private int ActivityBarPercent() {
        return settings.getSetting(1380);
    }

    public void messageReceived(MessageEvent me) {
        String m = me.getMessage().toLowerCase();
        if (me.getID() == MessageEvent.MESSAGE_SERVER
                || me.getID() == MessageEvent.MESSAGE_CLIENT
                || me.getID() == MessageEvent.MESSAGE_ACTION) {
            if (m.contains("you receive 0 zeal")) {
                lastLost = current;
                lastWon = other;
            }
            if (m.contains("you receive 1 zeal")) {
                lost++;
                zeal++;
                lastLost = current;
                lastWon = other;
            } else if (m.contains("you receive 2 zeal")) {
                tied++;

                zeal += 2;
            } else if (m.contains("you receive 3 zeal")) {
                won++;
                lastWon = current;
                lastLost = other;
                zeal += 3;
            }
            if (m.contains("non-combat")) {
                nonCombat = true;
            }
            if (m.contains("eastern")) {
                if (m.contains("taken")) {
                    if (m.contains("red")) {
                        east = 2;
                    } else if (m.contains("blue")) {
                        east = 1;
                    }
                } else if (m.contains("lost")) {
                    east = 0;
                }

                if (m.contains("western")) {
                    if (m.contains("taken")) {
                        if (m.contains("red")) {
                            west = 2;
                        } else if (m.contains("blue")) {
                            west = 1;
                        }
                    } else if (m.contains("lost")) {
                        west = 0;
                    }
                }
            }
        }

    }

    public int east() {
        if (interfaces.getComponent(836, 31).getTextureID() == -1) {
            return 2;
        } else {
            return 1;
        }

    }

    public boolean gotEast() {
        return east == getMyPlayer().getTeam();
    }

    public boolean gotWest() {
        return west == getMyPlayer().getTeam();
    }

    public int distToWest() {
        return calc.distanceTo(westTile);
    }

    public int distToEast() {
        return calc.distanceTo(eastTile);
    }

    //START: Code generated using Enfilade's Easel
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }
    private final Color color1 = new Color(0, 153, 255);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(255, 0, 0);
    private final Font font1 = new Font("Arial", 0, 11);
    private final Font font2 = new Font("Arial", 1, 11);
    private final Font font3 = new Font("Batang", 1, 11);
    private final Font font4 = new Font("Cambria Math", 1, 11);
    private final Image img1 = getImage("http://i52.tinypic.com/4to9av.png");
    private final Image img2 = getImage("http://www.bottombracket.co.uk/images/bike-shop-down-button.gif");

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        double version = getClass().getAnnotation(ScriptManifest.class).version();

        int played = tied + lost + won;
        Long runTime = System.currentTimeMillis() - startTime;
        if (runTime > 3600000 && runTime < 3600003) { // 1hour
            env.saveScreenshot(true);
            sleep(1000);
            log("We took a proggie because we had another hour of botting!");
        }
        if (show2) {
            g.drawImage(img1, 7, 344, null);
            g.setFont(font1);
            g.setColor(color1);
            g.drawString("" + played, 120, 384);
            g.drawString("" + won, 120, 403);
            g.drawString("" + lost, 120, 422);
            g.drawString("" + tied, 120, 442);
            g.drawString("" + zeal, 239, 443);
            g.setFont(font2);
            g.setColor(color2);
            g.drawString("" + version, 72, 466);
            g.setFont(font3);
            g.drawString("" + status, 158, 467);
            g.setFont(font4);
            g.setColor(color3);
            g.drawString("" + Timer.format(System.currentTimeMillis() - startTime), 127, 360);
        } else {
            g.drawImage(img2, 492, 338, null);
        }
    }

    public RSPlayer opponent() {
        return players.getNearest(new Filter<RSPlayer>() {

            public boolean accept(RSPlayer player) {
                return player != null && getMyPlayer().getInteracting() == null && obby.contains(player.getLocation())
                        && player.getTeam() != getMyPlayer().getTeam()
                        && player.getHPPercent() > 1;
            }
        });

    }

    public void antiBan() {
        int r = random(1, 100);
        switch (r) {
            case 1:
                mouse.moveRandomly(7);
                camera.setAngle(170);
                break;

            case 10:
                game.openTab(Tab.MAGIC);
                break;

            case 30:
                game.openTab(Tab.PRAYER);

            case 40:
                camera.moveRandomly(3);
                break;

            case 60:
                game.openTab(Tab.QUESTS);
                break;
            case 70:
                game.openTab(Tab.STATS);

                break;
            case 80:
                game.openTab(Tab.FRIENDS_CHAT);
                break;
            case 90:
                game.openTab(Tab.NOTES);
                break;
            case 100:
                mouse.moveOffScreen();
                break;

        }

    }
    private final Rectangle Hide = new Rectangle(481, 336, 37, 33);

    public void mouseClicked(MouseEvent e) {

        if (Hide.contains(e.getPoint())) {
            if (show2) {
                show2 = !show2;
            } else {
                show2 = true;
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    public void mouseHovered(MouseEvent e) {
    }

    public RSPlayer teamMate() {
        return players.getNearest(new Filter<RSPlayer>() {

            public boolean accept(RSPlayer player) {
                return player != null
                        && player.getTeam() == getMyPlayer().getTeam();
            }
        });
    }

    private boolean barricade() {
        try {
            RSNPC barricadez = npcs.getNearest(barricade);
            if (barricadez != null) {
                RSTile barricadeLoc = barricadez.getLocation();
                if (barricadeLoc != null) {
                    if (calc.distanceTo(barricadeLoc) <= 2) {
                        if (getMyPlayer().getInteracting() != null) {
                            return true;
                        } else {
                            barricadez.interact("Attack " + barricadez.getName());
                            sleep(3000, 5000);
                            return true;
                        }
                    }
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public int getMost(RSArea area) {
        int result = 0;
        ArrayList<Integer> Players = new ArrayList<Integer>();
        for (RSPlayer p : players.getAll()) {
            if (p != null && area.contains(p.getLocation())) {
                Players.add(p.getCombatLevel());
            } else {
                camera.moveRandomly(2);
                antiBan();
            }
        }
        return Players.size();
    }

    public int getAvarage(RSArea area) {
        int result = 0;
        ArrayList<Integer> Players = new ArrayList<Integer>();
        for (RSPlayer p : players.getAll()) {
            if (p != null && area.contains(p.getLocation())) {
                Players.add(p.getCombatLevel());
            } else {
                camera.moveRandomly(2);
                antiBan();
            }
        }
        for (int x = 0; x < Players.size(); x++) {
            result += Players.get(x);
        }
        return result / Players.size();
    }

    private void attackJellies() {
        if (getMyPlayer().getTeam() == 1) {
            if (!atArea(jelliesSouth)) {
                walkTo(js);
            } else {

                RSNPC jel = npcs.getNearest(jellies);
                RSGroundItem frag = groundItems.getNearest(fragment);

                if (!pickFrags()) {


                    if (jel != null) {
                        camera.setPitch(true);

                        if (jel.isOnScreen()) {
                            if (getMyPlayer().getInteracting() == null) {
                                jel.getModel().doClick(true);
                                sleep(3000, 5000);
                            }
                        } else {
                            if (calc.distanceTo(jel.getLocation()) > 7) {
                                walking.walkTileMM(jel.getLocation());
                            } else {
                                camera.turnTo(jel);
                            }
                        }
                    }
                }
            }

        } else {
            if (!atArea(jelliesNorth)) {
                walkTo(jn);
            } else {

                RSNPC jel = npcs.getNearest(jellies);
                RSGroundItem frag = groundItems.getNearest(fragment);

                if (!pickFrags()) {


                    if (jel != null) {
                        camera.setPitch(true);

                        if (jel.isOnScreen()) {
                            if (getMyPlayer().getInteracting() == null) {
                                jel.getModel().doClick(true);
                                sleep(3000, 5000);
                            }
                        } else {
                            if (calc.distanceTo(jel.getLocation()) > 7) {
                                walking.walkTileMM(jel.getLocation());
                            } else {
                                camera.turnTo(jel);
                            }
                        }

                    }
                }
            }
        }
    }

    private void attackPyres() {
        if (getMyPlayer().getTeam() == 2) {
            if (!atArea(pyreSouth)) {
                walkTo(ps);
            } else {
                int pyres = 8598;

                RSNPC pyr = npcs.getNearest(pyres);
                if (!pickFrags()) {

                    if (pyr != null) {
                        camera.setPitch(true);

                        if (pyr.isOnScreen()) {
                            if (getMyPlayer().getInteracting() == null) {
                                pyr.getModel().doClick(true);
                                sleep(3000, 5000);
                            }
                        } else {
                            if (calc.distanceTo(pyr.getLocation()) > 7) {
                                walking.walkTileMM(pyr.getLocation());
                            } else {
                                camera.turnTo(pyr);
                            }
                        }
                    }
                }
            }
        } else {


            if (!atArea(pyreNorth)) {
                walkTo(pn);
            } else {
                int pyres = 8598;

                RSNPC pyr = npcs.getNearest(pyres);
                if (!pickFrags()) {



                    if (pyr != null) {
                        camera.setPitch(true);

                        if (pyr.isOnScreen()) {
                            if (getMyPlayer().getInteracting() == null) {
                                pyr.getModel().doClick(true);
                                sleep(3000, 5000);
                            }
                        } else {
                            if (calc.distanceTo(pyr.getLocation()) > 7) {
                                walking.walkTileMM(pyr.getLocation());
                            } else {
                                camera.turnTo(pyr);
                            }
                        }
                    }



                }
            }
        }
    }

    public boolean pickFrags() {
        RSGroundItem frag = groundItems.getNearest(fragment);

        if (frag != null) {
            if (getMyPlayer().isInCombat()) {
            }
            if (frag.isOnScreen() && !inventory.isFull()
                    && calc.distanceTo(frag.getLocation()) < 4) {
                frag.interact("Take " + frag.getItem().getName());
                return true;
            }
        }
        return false;
    }

    private int oppAvaLvl() {
        String level;
        try {
            if (getMyPlayer().getTeam() == 1) {
                level = interfaces.get(836).getComponent(14).getText();
                if (!level.equals("---")) {
                    return Integer.parseInt(level);
                }
            } else {
                level = interfaces.get(836).getComponent(10).getText();
                if (!level.equals("---")) {
                    return Integer.parseInt(level);
                }
            }
        } catch (Exception e) {
            return 100;
        }
        return 100;
    }

    private void attAva() {

        if (getMyPlayer().getTeam() == 1) {
            if (!atArea(avaRed)) {
                walkTo(avaRedTile);
            } else {
                RSNPC avatar = npcs.getNearest(8596);
                if (avatar != null) {
                    if (avatar.isOnScreen()) {
                        if (getMyPlayer().getInteracting() == null) {
                            avatar.interact("Attack " + avatar.getName());
                            sleep(3000, 5000);
                        }
                    } else {


                        if (calc.distanceTo(walking.getDestination()) < 4) {
                            walking.walkTileMM(avatar.getLocation());
                        } else {
                            camera.turnTo(avatar);
                        }

                    }
                }
            }
        } else {
            if (!atArea(avaBlue)) {
                walkTo(avaBlueTile);
            } else {
                RSNPC avatar = npcs.getNearest(8597);
                if (avatar != null) {
                    if (avatar.isOnScreen()) {
                        if (getMyPlayer().getInteracting() == null) {
                            avatar.interact("Attack " + avatar.getName());
                            sleep(3000, 5000);
                        }
                    } else {


                        if (calc.distanceTo(walking.getDestination()) < 4) {
                            walking.walkTileMM(avatar.getLocation());
                        } else {
                            camera.turnTo(avatar);
                        }

                    }
                }
            }
        }
    }

    public boolean canAttackAva() {
        return skills.getCurrentLevel(Skills.SLAYER) >= oppAvaLvl();
    }

    public boolean canAttackJellies() {
        return skills.getCurrentLevel(Skills.SLAYER) >= 52;

    }

    public boolean canAttackPyres() {
        return skills.getCurrentLevel(Skills.SLAYER) >= 30;
    }

    public void pray() {
        if (usePrayer) {
            if (!prayer.isQuickPrayerOn()) {
                if (prayer.getPrayerPercentLeft() > 1) {
                    if (y.toLowerCase().contains("obe")) {
                        if (atArea(obby)) {
                            prayer.setQuickPrayer(true);
                        }
                    } else {
                        prayer.setQuickPrayer(true);
                    }
                }

            }
        }
    }

    public void newWeapon() {
        game.openTab(Tab.INVENTORY);
        if (inventory.contains(spec)) {
            inventory.getItem(spec).doClick(true);
        } else if (equipment.getItem(Equipment.WEAPON).getID() == spec) {
            specEquiped = true;
        } else {
            log("We don't have the special ID you entered in our inventory or in our equipment tab!");
            log("Setting spec to off");
            useTwoSpec = false;
            useSpec = false;

        }
    }

    public void oldWeapon() {
        inventory.getItem(norm).doClick(true);
        specEquiped = false;
    }

    public class dynSigGUI extends javax.swing.JFrame {

        /** Creates new form dynSigGUI */
        public dynSigGUI() {
            initComponents();
        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            jLabel1 = new javax.swing.JLabel();
            jTextField1 = new javax.swing.JTextField();
            jButton1 = new javax.swing.JButton();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            jLabel1.setText("Username you want to use for dyn sig:");

            jTextField1.setText("Peter pan");

            jButton1.setText("Start script");
            jButton1.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButton1ActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE).addComponent(jTextField1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE).addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 210, Short.MAX_VALUE));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jLabel1).addGap(11, 11, 11).addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jButton1)));

            pack();
        }// </editor-fold>

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
            if (jTextField1.getText() != null || !jTextField1.getText().equals("")) {
                dynSigUser = jTextField1.getText();
                createAccount();
                dispose();
                canStart = true;
            } else {
                log(Color.RED, "Please fill in something for user :)");
            }
        }
        private javax.swing.JButton jButton1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JTextField jTextField1;
    }

    public void onFinish() {
        log("Thanks for using SoulWarsPro.");
        try {
            Long runTime = System.currentTimeMillis() - startTime;
            URL url = new URL("http://isoulwars.comeze.com/dynsigs/iSoulWarsSig.php?user=" + dynSigUser + "timer=" + runTime + "&zeals=" + zeal);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.close();
        } catch (Exception e) {
            log("Encountered a problem submitting signature data :'(");
        }
    }

    public void createAccount() {
        try {
            URL url = new URL("http://isoulwars.comeze.com/dynsigs/submitSwInfo.php?user=" + dynSigUser);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            in.close();
        } catch (Exception e) {
            log("Encountered a problem submitting signature data :'(");

        }
    }
}
// This script has been made by Xiffs!
