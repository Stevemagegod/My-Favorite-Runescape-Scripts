import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSCharacter;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSGroundItem;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPlayer;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSWeb;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

@ScriptManifest(authors = "NEXBot (Muhatashim AKA Voltron AKA Harry Potter AKA ExKALLEbur)", name = "SoulWars ExKALLEbur", version = 0.80, description = "ExKALLEbur's ultimate soul wars script")
public class NEXSoulWars extends Script implements PaintListener, MouseListener, MessageListener {

    String version = "0.80 beta";
    SoulWars gui = new SoulWars();
    SWAuth authGui = new SWAuth();
    public boolean Activated = false;
    public boolean enableSpecialAttack = false;
    public boolean Jelly = false;
    public boolean Pyre = false;
    public boolean Players = false;
    public boolean joinRed = false;
    public boolean joinBlue = false;
    public boolean joinClan = false;
    public boolean joinHigh = false;
    public boolean joinLow = false;
    public boolean joinLastWon = false;
    public boolean joinLastLost = false;
    public boolean enableChatBot = false;
    public boolean inBlueTeam = false;
    public boolean inRedTeam = false;
    public boolean lookForDeath = false;
    public boolean inClan = false;
    public final int blueTeamEnterGate = 42029;
    public final int redTeamEnterGate = 42030;
    public int redPlayers = 0;
    public double lastSpecial = -1;
    public double currentSpecial = -1;
    public double specialDamage = -1;
    public double averageRed = 0;
    public int bluePlayers = 0;
    public double averageBlue = 0;
    public String clanNameString = "";
    public String lastWon = "";
    public String lastLost = "";
    public String ActionString = "";
    public String whattheysaid = "";
    public String input = "";
    public final String[] deathResponses = {"nice,", "lol", "lmao", "rofl", "rolf", "gf", "noooooooo =0", "=)", "thanks lol", "=( gg", "omg i keep dieing...", "thanks for the high activity XD", "wow dude", "noooooob!!!", "i will not die!!", "nu u!"};
    //Paint stoof
    public final LinkedList<mousePathPoint> mousePath = new LinkedList<mousePathPoint>();
    public int startExpStr = 0;
    public int startExpDef = 0;
    public int startExpAtt = 0;
    public int startExpHit = 0;
    public int startExpRan = 0;
    public int currExpStr = 0;
    public int currExpDef = 0;
    public int currExpAtt = 0;
    public int currExpHit = 0;
    public int currExpRan = 0;
    public long ExpStrHr = 0;
    public long ExpDefHr = 0;
    public long ExpAttHr = 0;
    public long ExpHitHr = 0;
    public long ExpRanHr = 0;
    public long startTime = 0;
    public long currentTime = 0;
    public boolean isPressed = false;
    public long timeout2Start = 0;
    public long timeout2Curr = 0;
    public boolean showPaint = true;
    public boolean showExtraPaint = false;
    //SoulWars stoof
    boolean doingSomething = false;
    public final int pyreId = 8598;
    public final int jellyId = 8599;
    public final int soulfragId = 14639;
    public final int bandageIdInvent = 14640;
    public final int bonesID = 14638;
    public final int deathAnimation = 1120;
    public final int oblesikID = 8593;
    public int deaths = 0;
    public int zeals = 0;
    public final int[] barriersID = {42018, 42015, 42014, 42017, 42016};
    public boolean wait = false;
    public boolean getBandage = false;
    public final String redWon = "The <col=e12323>red</col> team was victorious! = red won";
    public final String blueWon = "The <col=3366ff>blue</col> team was victorious! = blue won";
    public String whichTeam = "";
    public final String spamBotsAt[] = {"all at ", "everyone come at ", "noobs at ", "!!!!!!!!!!!!!!!! ", "@@@@@@@@ ", "pl0x c0me at ", "all you bots, come over at ", "gomogmgomgomgo "};
    public final String spamBotsAtEastGrave[] = {"east grave@@#!!", "east graveyard!!", "eastern grave!", "eastern grave yard!!", "eastt grave!!!!!!!!!!!", "ast grave@!", "grave <<<<<<<<"};
    public final String spamBotsAtWestGrave[] = {"west grave@!@", "westr graveyard#!", "west grave", "west grave yard!!*!(", "west grav e >>>>", "grave <<<<<"};
    public final String spamBotsAtCenter[] = {"center!!!!", "oblesik!", "obleisik!!", "obleik!@@!", "centah!!*&", "middle <<<<<<<<<<<<<<<<<<<<<<<<<<"};
    public final RSTile redGraveyardTile = new RSTile(1931, 3245);
    public final RSTile blueGraveyardTile = new RSTile(1844, 3220);
    public final RSTile RedPyreTile = new RSTile(1926, 3213);
    public final RSTile BluePyreTile = new RSTile(1847, 3247);
    RSTile SouthJellyTile = new RSTile(1877, 3212);
    public final RSTile oblesikLocation = new RSTile(1885, 3231);

    public boolean inBriefingRoom() {
        RSArea area = new RSArea(new RSTile(1880, 3186), new RSTile(1899, 3157));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inRedRoom(RSPlayer x) {
        RSArea area = new RSArea(new RSTile(1899, 3167), new RSTile(1908, 3156));
        return area.contains(x.getLocation());
    }

    public boolean inBlueRoom(RSPlayer x) {
        RSArea area = new RSArea(new RSTile(1880, 3167), new RSTile(1869, 3157));
        return area.contains(x.getLocation());
    }

    public boolean inBlueSpawnBig() {
        RSArea area = new RSArea(new RSTile(1816, 3230), new RSTile(1870, 3158));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inBlueSpawnSmall() {
        RSArea area = new RSArea(new RSTile(1841, 3118), new RSTile(1843, 3217));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inRedSpawnBig() {
        RSArea area = new RSArea(new RSTile(1951, 3244), new RSTile(1958, 3235));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inRedSpawnSmall() {
        RSArea area = new RSArea(new RSTile(1841, 3118), new RSTile(1843, 3217));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inBlueBandage() {
        RSArea area = new RSArea(new RSTile(1803, 3249), new RSTile(1811, 3256));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inRedBandage() {
        RSArea area = new RSArea(new RSTile(1961, 3211), new RSTile(1971, 3205));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inBlueGraveyard() {
        RSArea area = new RSArea(new RSTile(1838, 3223), new RSTile(1846, 3213));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean inOblesik() {
        RSArea area = new RSArea(new RSTile(1884, 3223), new RSTile(1889, 3230));
        return area.contains(players.getMyPlayer().getLocation());
    }

    public boolean doingNothing() {
        return (inBlueSpawnBig() || inBlueSpawnSmall() || inRedSpawnBig() || inRedSpawnSmall()) && !inventory.isFull()
                && ((players.getMyPlayer().getInteracting() != null && players.getMyPlayer().getInteracting().isDead())
                || players.getMyPlayer().getInteracting() == null) && !getBandage && !players.getMyPlayer().isMoving();
    }

    public void openBarier(RSObject x) {
        if (x != null) {
            if (x.isOnScreen()) {
                ActionString = "Open(" + x.getName() + ")";
                //x.interact("Pass");
                x.doClick(true);
                sleep(random(2000, 4000));
                if (interfaces.getComponent(1186, 1) != null && interfaces.getComponent(1186, 1).getText().contains("minutes")) {
                    String waitTimer = interfaces.getComponent(1186, 1).getText().split("wait")[1].split("minutes")[0].replaceAll(" ", "");
                    int waiting = Integer.parseInt(waitTimer) * 60000;
                    ActionString = "Waiting for " + waitTimer + " minutes";
                    interfaces.getComponent(1186, 8).doClick();
                    mouse.moveOffScreen();
                    sleep(Math.abs(waiting - random(60000, 120000)));
                }
                if (interfaces.getComponent(1186) != null) {
                    interfaces.getComponent(1186, 8).doClick(true);
                    sleep(random(2000, 4000));
                }
                if (interfaces.getComponent(1188) != null) {
                    interfaces.getComponent(1188, 11).doClick(true);
                }
                if (interfaces.getComponent(228) != null) {
                    interfaces.getComponent(228, 2).doClick(true);
                }
                redPlayers = 0;
                averageBlue = 0;
                bluePlayers = 0;
                averageRed = 0;
            } else if (!x.isOnScreen()) {
                Walk(x.getLocation());
            }
            sleep(random(2000, 4000));
        }
    }

    public void webWalk(RSTile x) {
        checkZealGained();
        if (random(0, 30) == 5) {
            if (camera.getPitch() <= 2300) {
                ActionString = "Adjusting camera";
                camera.setPitch(random(2700, 2900));
                if (random(1, 2) == 2) {
                    camera.setAngle(random(15700, 16300));
                } else {
                    camera.setAngle(random(0, 20));
                }
            }
        }
        if ((walking.getDestination() != null && calc.distanceTo(walking.getDestination()) <= random(5, 7)) || !players.getMyPlayer().isMoving() || walking.getDestination() == null && players.getMyPlayer().getInteracting() == null) {
            ActionString = "Generating web";
            RSWeb path = web.getWeb(x);
            if (path != null) {
                ActionString = "walking" + x + "";
                path.step();
                sleep(random(1000, 2000));
            } else if (path == null) {
                ActionString = "Error Fixing";
                RSObject barrier = objects.getNearest(barriersID);
                if (barrier != null && !players.getMyPlayer().isMoving()) {
                    ActionString = "Error Fixing: Barrier";
                    if (barrier.isOnScreen()) {
                        camera.moveRandomly(300);
                        barrier.doClick(true);
                        //sleep(random(2500, 3000));
                        //doingSomething = false;
                        getBandage = true;
                    } else if (!barrier.isOnScreen()) {
                        Walk(barrier.getLocation());
                    }
                    sleep(random(3000, 4000));
                }
            }
        }
    }

    public void WalkTile(int x, int y) {
        checkZealGained();
        if ((walking.getDestination() != null && (calc.distanceTo(walking.getDestination()) <= random(5, 7) || calc.distanceTo(walking.getDestination()) >= random(9, 12))) || walking.getDestination() == null || !players.getMyPlayer().isMoving() && players.getMyPlayer().getInteracting() == null) {
            ActionString = "walking(" + x + ", " + y + ")";
            walking.walkTileMM(new RSTile(x, y));
        }
    }

    public void Walk(RSTile x) {

        if ((walking.getDestination() != null && (calc.distanceTo(walking.getDestination()) <= random(5, 7) || calc.distanceTo(walking.getDestination()) >= random(9, 12))) || walking.getDestination() == null || !players.getMyPlayer().isMoving() && players.getMyPlayer().getInteracting() == null) {
            ActionString = "walking(" + x + ")";
            walking.walkTileMM(x);
        }
    }

    public void fightNpc(int npc) {
        int x = 0;
        int npcLegnth = npcs.getAll(npc).length;
        RSNPC closest = null;
        RSNPC monster2 = null;
        ActionString = "Fight(" + npc + ")";
        try {
            while (x <= npcLegnth && (players.getMyPlayer().getInteracting() == null) || (players.getMyPlayer().getInteracting() != null && players.getMyPlayer().getInteracting().isDead())) {
                if (players.getMyPlayer().getAnimation() != -1) {
                    sleep(2000);
                    break;
                }
                try {
                    monster2 = npcs.getAll(npc)[x];
                    if ((calc.distanceTo(monster2.getLocation()) < calc.distanceTo(closest) || closest == null) && players.getMyPlayer().getAnimation() == -1 && (!monster2.isInCombat() || monster2.isInteractingWithLocalPlayer()) && !monster2.isDead()) {
                        if (monster2.isInteractingWithLocalPlayer()) {
                            break;
                        } else {
                            closest = monster2;
                        }
                    }
                    x++;
                } catch (Exception ignored) {
                    break;
                }
            }
            if (closest != null && (closest.getInteracting() == null || (players.getMyPlayer().getInteracting() != null && players.getMyPlayer().getInteracting().isDead()))) {
                if ((players.getMyPlayer().getInteracting() != null && players.getMyPlayer().getInteracting().isDead()) || players.getMyPlayer().getInteracting() == null) {
                    if (closest.isOnScreen()) {
                        closest.interact("Attack");
                        //sleep(5000);
                    } else if (!closest.isOnScreen()) {
                        RSWeb path = web.getWeb(closest.getLocation());
                        if (path != null) {
                            path.step();
                            sleep(random(1000, 2000));
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }

    public void checkZealGained() {
        if (interfaces.getComponent(1184) != null) {
            String team = interfaces.getComponent(1184, 13).getText();
            if (team.contains("3 Zeal")) {
                zeals += 3;
                log(Color.GREEN, "You won! Deaths: " + deaths);
                deaths = 0;
            }
            if (team.contains("2 Zeal")) {
                zeals += 2;
                log(Color.GREEN, "You won! Deaths: " + deaths);
                deaths = 0;
            }
            if (team.contains("1 Zeal")) {
                zeals += 1;
                log(Color.orange, "You lost.... Deaths: " + deaths);
                deaths = 0;
            }
            if (team.contains("blue") && team.contains("team was victorious!")) {
                lastWon = "blue";
                lastLost = "red";
                ActionString = "Won: blue";
                sleep(random(4000, 7000));
            }
            if (team.contains("red") && team.contains("team was victorious!")) {
                lastWon = "red";
                lastLost = "blue";
                ActionString = "Won: red";
                sleep(random(4000, 7000));
            }
            if (team.contains("drawn!")) {
                lastWon = "";
                lastLost = "";
                ActionString = "Draw!";
                deaths = 0;
                log(Color.YELLOW, "Draw! Deaths: " + deaths);
            }
            mouse.click(interfaces.getComponent(1184, 8).getPoint(), true);
        }
    }

    public void joinTeam() {
        ActionString = "Joining team.";
        checkZealGained();
        if (joinRed) {
            openBarier(objects.getNearest(redTeamEnterGate));
        }
        if (joinBlue) {
            openBarier(objects.getNearest(blueTeamEnterGate));
        }
        if (lastWon.equals("red") && joinLastWon) {
            openBarier(objects.getNearest(redTeamEnterGate));
        }
        if (lastLost.equals("red") && joinLastLost) {
            openBarier(objects.getNearest(redTeamEnterGate));
        }
        if (lastWon.equals("blue") && joinLastWon) {
            openBarier(objects.getNearest(blueTeamEnterGate));
        }
        if (lastLost.equals("blue") && joinLastLost) {
            openBarier(objects.getNearest(blueTeamEnterGate));
        }
        if (lastLost.equals("") && lastWon.equals("") && (joinLastWon || joinLastLost)) {
            openBarier(objects.getNearest(42031));
        }
        if (!clanNameString.equals("") && !inClan) {
            if (interfaces.getComponent(1109) != null) {
                if (interfaces.getComponent(1109, 1).getText().contains("Not in chat")) {
                    interfaces.getComponent(1109, 23).doClick(true);
                    keyboard.sendText(clanNameString, true);
                }
                if (!interfaces.getComponent(1109, 1).getText().contains("Not in chat")) {
                    inClan = true;
                }
            }
            if (interfaces.getComponent(1109) == null) {
                game.openTab(Tab.FRIENDS_CHAT);
            }
        }
        if (joinClan) {
            //Check red room
            if (averageRed == 0 && averageBlue == 0) {
                WalkTile(1900, 3162);
                if (calc.distanceTo(objects.getNearest(redTeamEnterGate)) <= 1) {
                    camera.moveRandomly(random(1000, 3000));
                    mouse.moveRandomly(random(5500, 7000));
                    RSPlayer AllRed[] = players.getAll();
                    int x = 0;
                    while (x <= AllRed.length) {
                        if (inRedRoom(AllRed[x]) && AllRed[x].getTeam() != 0) {
                            redPlayers++;
                            averageRed += AllRed[x].getLevel();
                            ActionString = "Curr lvl: " + AllRed[x].getLevel();
                        }
                        x++;
                    }
                    averageRed = averageRed / redPlayers;
                    log("The clan average level for the red team is: " + averageRed);
                }
            }
            //Check blue room
            if (averageRed != 0 && averageBlue == 0) {
                WalkTile(1879, 3162);
                if (calc.distanceTo(objects.getNearest(blueTeamEnterGate)) <= 1) {
                    camera.moveRandomly(random(1000, 3000));
                    mouse.moveRandomly(random(5500, 7000));
                    RSPlayer AllBlue[] = players.getAll();
                    int x = 0;
                    while (x <= AllBlue.length) {
                        if (inBlueRoom(AllBlue[x]) && AllBlue[x].getTeam() != 0) {
                            bluePlayers++;
                            averageBlue += AllBlue[x].getLevel();
                            ActionString = "Curr lvl: " + AllBlue[x].getLevel();
                        }
                        x++;
                    }
                    averageBlue = averageBlue / bluePlayers;
                    log("The clan average level for the blue team is: " + averageBlue);
                }
            }
            if (averageBlue != 0) {
                if (averageRed >= averageBlue) {
                    openBarier(objects.getNearest(redTeamEnterGate));
                } else if (averageBlue >= averageRed) {
                    openBarier(objects.getNearest(blueTeamEnterGate));
                }
            }
            if (Double.isNaN(averageBlue) || Double.isNaN(averageRed)) {
                openBarier(objects.getNearest(42031));
            }
        }

        if (joinHigh || joinLow) {
            //Check red room
            if (averageRed == 0 && averageBlue == 0) {
                WalkTile(1900, 3162);
                ActionString = "Scanning red room";
                if (calc.distanceTo(objects.getNearest(redTeamEnterGate)) <= 1) {
                    camera.moveRandomly(random(1000, 3000));
                    sleep(random(5500, 7000));
                    RSPlayer AllRed[] = players.getAll();
                    for (RSPlayer CurrRed : AllRed) {
                        if (inRedRoom(CurrRed)) {
                            redPlayers++;
                            averageRed += CurrRed.getLevel();
                        }
                    }
                    averageRed = averageRed / redPlayers;
                    log("The average level for the red team is: " + averageRed);
                }
            }
            //Check blue room
            if (averageRed != 0 && averageBlue == 0) {
                WalkTile(1879, 3162);
                ActionString = "Scanning blue room";
                if (calc.distanceTo(objects.getNearest(blueTeamEnterGate)) <= 1) {
                    camera.moveRandomly(random(1000, 3000));
                    sleep(random(5500, 7000));
                    RSPlayer AllBlue[] = players.getAll();
                    for (RSPlayer CurrBlue : AllBlue) {
                        if (inBlueRoom(CurrBlue)) {
                            bluePlayers++;
                            averageBlue += CurrBlue.getLevel();
                        }
                    }
                    averageBlue = averageBlue / bluePlayers;
                    log("The average level for the blue team is: " + averageBlue);
                    log.warning("Red: " + averageRed + ", Blue: " + averageBlue);
                }
            }
            if (averageBlue != 0) {
                if (Double.isNaN(averageBlue) || Double.isNaN(averageRed)) {
                    openBarier(objects.getNearest(42031));
                }
                if (joinHigh) {
                    if (averageRed >= averageBlue) {
                        openBarier(objects.getNearest(redTeamEnterGate));
                    } else if (averageBlue >= averageRed) {
                        openBarier(objects.getNearest(blueTeamEnterGate));
                    }
                } else if (joinLow) {
                    if (averageRed <= averageBlue) {
                        openBarier(objects.getNearest(redTeamEnterGate));
                    } else if (averageBlue <= averageRed) {
                        openBarier(objects.getNearest(blueTeamEnterGate));
                    }
                }
            }
        }
    }

    public void AntiBan() {
        int randomizer = random(1, 30);
        if (randomizer == 2) {
            int randomOne = random(1, 13);
            if (randomOne == 1) {
                ActionString = "Antiban: Moving camera.";
                camera.moveRandomly(random(500, 1000));
            }
            if (randomOne == 2) {
                ActionString = "Antiban: Moving mouse.";
                mouse.move(random(0, 900), random(0, 900));
            }
            if (randomOne == 3) {
                ActionString = "Antiban: Looking attack modes.";
                game.openTab(Tab.ATTACK);
                sleep(random(2000, 3000));
            }
            if (randomOne == 4) {
                if (randomOne == 2) {
                    ActionString = "Antiban: Moving mouse.";
                    //mouse.moveRandomly(random(500, 1000));
                    mouse.move(random(0, 500), random(0, 700));
                }
            }
            if (randomOne == 5) {
                int randomer = random(2000, 6000);
                ActionString = "Antiban: Pausing for " + randomer / 1000 + "seconds";
                sleep(randomer);
            }
            if (randomOne == 6) {
                ActionString = "Antiban: hovering over map.";
                mouse.move(539, 138);
            }
            if (randomOne == 7) {
                ActionString = "Antiban: Simulating look away.";
                mouse.move(1185, 321);
            }
            if (randomOne == 8) {
                ActionString = "Antiban: Looking at skills.";
                game.openTab(Tab.STATS);
                sleep(2000, 5000);
            }
            if (randomOne == 9) {
                ActionString = "Looking at friends.";
                game.openTab(Tab.FRIENDS);
                sleep(2000, 5000);
            }
            if (randomOne == 10) {
                ActionString = "Antiban: Checking equipment.";
                game.openTab(Tab.EQUIPMENT);
                sleep(2000, 5000);
            }
            if (randomOne == 11) {
                ActionString = "Antiban: Turning on run.";
                walking.setRun(true);
            }
            if (randomOne == 12) {
                ActionString = "Antiban: Turning camera to top.";
                camera.setPitch(true);
                camera.setPitch(random(1000, 3000));
                camera.setPitch(true);
            }
            if (randomOne == 13) {
                ActionString = "Antiban: Wiggling mouse.";
                mouse.moveRandomly(25);
            }
        }
    }

    public void AIReply() {
        if (!game.getLastMessage().toLowerCase().contains(whattheysaid)) {
            input = game.getLastMessage().toLowerCase();
            whattheysaid = input;
            input = input.replace(" <col=0000ff>", "");
            input = input.replace("<col=c86400>sir", "");
            input = input.replace("<col=c86400>lady", "");
            input = input.replace("<col=c86400>lord", "");
            input = input.replace("<col=c86400>dame", "");
            String displayName = interfaces.getComponent(137, 55).getText().toLowerCase().split("<")[0];
            String lastName = game.getLastMessage().toLowerCase().split("<")[0];
            if (input.equals("")) {
            }
            if (input.contains(displayName)) {
            }
            if (!input.contains(displayName)) {
                input = input.replace(":", " ");
                lastName = lastName.replace(":", "");
                input = input.replace(lastName, "");
                log("Alice AI: Replieing to: " + lastName + "::> " + input);
                getAnswer();
            }
        }
    }

    public String getAnswer() {
        String answer = null;
        try {
            URL u = new URL("http://noneevr2.r00t.la/ALICE.php?in=" + input.replace(" ", "%20"));
            URLConnection connect = u.openConnection();
            connect.connect();
            BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
            String line = reader.readLine();
            keyboard.sendText(line, true);
            log("Replied: " + line);
            //while((line = reader.readLine()) != "99999" ) {
            //startsWith
            //line = reader.readLine();
            //log(line);
            //if(line.contains("<B>splotchy ==> ")) {
            // answer = line.substring("<B>splotchy ==> ".length());
            // answer = answer.substring(0, answer.length() - 4);
            // keyboard.sendText(answer , true );
            // break;
            //}
            //log(line);
            //}
        } catch (MalformedURLException e) {
        } catch (IOException e) {
        }
        return answer;
    }

    public void openUrl(URI uri) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(uri);
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public boolean onStart() {
        authGui.setVisible(true);
        log(Color.CYAN, "If you have forgotten your auth, email me @ nexbot@pbot.org");
        log(Color.CYAN, "Or email me on powerbot, Harry_Potter");
        while (authGui.isVisible()) {
            sleep(10);
        }
        //if (!Activated) {
        try {
            URI n = URI.create("http://adf.ly/33VLH");
            log(Color.RED, "[Adf.ly] Please click on skip ad on your browser.");
            sleep(3000);
            openUrl(n);
            sleep(10000);
        } catch (Exception ex) {
            log("Error " + ex);
        }
        //}
        log(Color.GREEN, "You are using version " + version);
        sleep(500);
        log(Color.PINK, "Don't forget to visit the official thread for bug reporting/proggy/improovements =)");
        sleep(500);
        log(Color.CYAN, "http://www.powerbot.org/community/topic/603511-nex-soul-wars/page__fromsearch__1");
        sleep(500);
        log(Color.magenta, "Our official Soul Wars friends chat is \"YouTube 2012\"");

        //For revenue from script and counter
        gui.setVisible(true);
        //gui.clanChatTeam.setVisible(false);
        while (gui.isVisible()) {
            sleep(10);
        }
        startExpStr = skills.getCurrentExp(skills.STRENGTH);
        startExpAtt = skills.getCurrentExp(skills.ATTACK);
        startExpDef = skills.getCurrentExp(skills.DEFENSE);
        startExpHit = skills.getCurrentExp(skills.CONSTITUTION);
        startExpRan = skills.getCurrentExp(skills.RANGE);
        startTime = System.currentTimeMillis();
        return true;
    }

    @Override
    public int loop() {
        try {
            ActionString = "-";
            checkZealGained();
            if (!Activated && zeals >= 1) {
                log(Color.red, "Script trial has ended!");
                game.logout(true);
                stopScript();
            }
            if (game.isLoading()) {
                ActionString = "RS Lag, waiting.";
                sleep(2000);
            }
            if (!game.isLoading()) {
                mouse.setSpeed(random(10, 13));
                if (players.getMyPlayer().getTeam() == 2) {
                    whichTeam = "red";
                }
                if (players.getMyPlayer().getTeam() == 1) {
                    whichTeam = "blue";
                }
                if (inBriefingRoom()) {
                    whichTeam = "Briefing room";
                    if (inBlueTeam) {
                        inBlueTeam = false;
                    }
                    if (inRedTeam) {
                        inRedTeam = false;
                    }
                    joinTeam();
                }
                if (inBlueRoom(players.getMyPlayer())) {
                    whichTeam = "blue";
                    inBlueTeam = true;
                    lookForDeath = false;
                    if (random(10, 70) == 20) {
                        RSArea area = new RSArea(new RSTile(1879, 3165), new RSTile(1870, 3158));
                        Walk(area.getCentralTile().randomize(2, 3));
                    }
                    AntiBan();
                }
                if (inRedRoom(players.getMyPlayer())) {
                    whichTeam = "red";
                    inRedTeam = true;
                    lookForDeath = false;
                    if (random(10, 70) == 20) {
                        RSArea area = new RSArea(new RSTile(1900, 3165), new RSTile(1907, 3158));
                        Walk(area.getCentralTile().randomize(2, 3));
                    }
                    AntiBan();
                }
                if (players.getMyPlayer().getHPPercent() <= 65) {
                    if (inventory.contains(bandageIdInvent)) {
                        int x = 0;
                        int y = 0;
                        while (x <= 27 && players.getMyPlayer().getHPPercent() <= 65 && y != 4) {
                            ActionString = "Heal (Optimized)";
                            mouse.setSpeed(random(6, 10));
                            if (inventory.getItemAt(x) != null && inventory.getItemAt(x).getID() == bandageIdInvent) {
                                inventory.getItemAt(x).doClick(true);
                                y++;
                            }
                            x++;
                        }
                    }
                }

                if (interfaces.getComponent(836) != null && interfaces.getComponent(836, 56).getHeight() <= 58 && !(inBlueSpawnBig() || inBlueSpawnSmall() || inRedSpawnBig() || inRedSpawnSmall() || inBriefingRoom() || inRedRoom(players.getMyPlayer()) || inBlueRoom(players.getMyPlayer()))) {
                    ActionString = "Low activity: Fixing.";
                    if (inventory.isFull()) {
                        if (inventory.contains(bandageIdInvent)) {
                            inventory.getItem(bandageIdInvent).doClick(true);
                            if (inventory.contains(bandageIdInvent)) {
                                ActionString = "Fast Heal";
                                for (int x = 0; x <= 27; x++) {
                                    if (inventory.getItemAt(x).getID() == bandageIdInvent) {
                                        int y = 0;
                                        int f = players.getAll().length;
                                        while (y < f) {
                                            try {
                                                if (players.getAll()[y].getTeam() == 1 && whichTeam.equals("blue") && players.getAll()[y].getHPPercent() <= 99) {
                                                    inventory.getItemAt(x).interact("Use");
                                                    if (players.getAll()[y].isOnScreen()) {
                                                        players.getAll()[y].getModel().doClick(true);
                                                    }
                                                }
                                                if (players.getAll()[y].getTeam() == 2 && whichTeam.equals("red") && players.getAll()[y].getHPPercent() <= 99) {
                                                    inventory.getItemAt(x).interact("Use");
                                                    if (players.getAll()[y].isOnScreen()) {
                                                        players.getAll()[y].getModel().doClick(true);
                                                    }
                                                }
                                            } catch (Exception ignored) {
                                            }
                                            y++;
                                        }
                                        if (y == f) {
                                            inventory.getItemAt(x).doClick(true);
                                        }
                                    }
                                }
                            }
                        }
                        if (!inventory.contains(bandageIdInvent) && inventory.contains(bonesID)) {
                            if (whichTeam.equals("blue") && interfaces.getComponent(836, 30).getTextureID() == 989) {
                                if (inBlueGraveyard()) {
                                    ActionString = "Fast Bury";
                                    for (RSItem item : inventory.getItems()) {
                                        if (item.getID() == bonesID) {
                                            item.doClick(true);
                                        }
                                    }
                                }
                            }
                            if (whichTeam.equals("blue") && interfaces.getComponent(836, 30).getTextureID() == 990) {
                                ActionString = "Fast Destroy";
                                for (RSItem item : inventory.getItems()) {
                                    if (item.getID() == bonesID) {
                                        item.interact("Destroy");
                                        sleep(random(1000, 3000));
                                        if (interfaces.getComponent(94) != null) {
                                            interfaces.getComponent(94, 3).doClick(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    try {
                        RSGroundItem bones = groundItems.getNearest(bonesID);
                        if (bones != null) {
                            if (!inventory.isFull()) {
                                if (bones.isOnScreen()) {
                                    ActionString = "Take Bones";
                                    bones.interact("Take");
                                } else if (!bones.isOnScreen()) {
                                    ActionString = "Going to bones";
                                    webWalk(bones.getLocation());
                                }
                            }
                        } else if (bones == null) {
                            webWalk(oblesikLocation);
                        }
                    } catch (Exception i) {
                        log(Color.red, "Something's wrong. Your client cannot detect grounditems.");
                    }
                    AntiBan();
                } else {
                    if (inventory.isFull()) {
                        if (inventory.contains(bandageIdInvent)) {
                            inventory.getItem(bandageIdInvent).doClick(true);
                            if (inventory.contains(bandageIdInvent)) {
                                ActionString = "Fast Heal";
                                for (int t = 0; t <= 27; t++) {
                                    if (inventory.getItemAt(t).getID() == bandageIdInvent) {
                                        int y = 0;
                                        int f = players.getAll().length;
                                        while (y < f) {
                                            try {
                                                if (players.getAll()[y].getTeam() == 1 && whichTeam.equals("blue") && players.getAll()[y].getHPPercent() <= 99) {
                                                    inventory.getItemAt(t).interact("Use");
                                                    players.getAll()[y].getModel().doClick(true);
                                                }
                                                if (players.getAll()[y].getTeam() == 2 && whichTeam.equals("red") && players.getAll()[y].getHPPercent() <= 99) {
                                                    inventory.getItemAt(t).interact("Use");
                                                    players.getAll()[y].getModel().doClick(true);
                                                }
                                            } catch (Exception ignored) {
                                            }
                                            y++;
                                        }
                                        if (y == f) {
                                            inventory.getItemAt(t).doClick(true);
                                        }
                                    }
                                }
                            }
                        }
                        if (!inventory.contains(bandageIdInvent) && inventory.contains(bonesID)) {
                            if (whichTeam.equals("blue") && interfaces.getComponent(836, 30).getTextureID() == 989) {
                                if (inBlueGraveyard()) {
                                    ActionString = "Fast Bury";
                                    for (int t = 0; t <= 27; t++) {
                                        if (inventory.getItemAt(t).getID() == bonesID) {
                                            inventory.getItemAt(t).doClick(true);
                                        }
                                    }
                                } else if (!inBlueGraveyard()) {
                                    webWalk(blueGraveyardTile);
                                }
                            }
                            if (whichTeam.equals("blue") && interfaces.getComponent(836, 30).getTextureID() == 990) {
                                ActionString = "Fast Destroy";;
                                for (int t = 0; t <= 27; t++) {
                                    if (inventory.getItemAt(t).getID() == bonesID) {
                                        inventory.getItemAt(t).interact("Destroy");
                                        sleep(random(1000, 3000));
                                        if (interfaces.getComponent(94) != null) {
                                            interfaces.getComponent(94, 3).doClick(true);
                                        }
                                    }
                                }
                            }
                        }
                    } else if (interfaces.getComponent(836) != null && interfaces.getComponent(836, 56).getHeight() >= 58 && !inRedRoom(players.getMyPlayer()) && !inBlueRoom(players.getMyPlayer())) {
                        //Playing the game atm
                        //&& !players.getMyPlayer().isInCombat()
                        if (!inBriefingRoom() && !inRedRoom(players.getMyPlayer()) && !inBlueRoom(players.getMyPlayer())) {
                            ActionString = "Playing game";
                            mouse.setSpeed(random(8, 13));
                            if (interfaces.getComponent(836) != null) {
                                //else {
                                if ((inBlueSpawnBig() || inBlueSpawnSmall() || inRedSpawnBig() || inRedSpawnSmall()) || wait) {
                                    AntiBan();
                                    ActionString = "Trying go through barrier.";
                                    //ctx.bot.disableRendering = true;
                                    //doingSomething = true;
                                    RSObject barr = objects.getNearest(barriersID);
                                    if (barr != null && !players.getMyPlayer().isMoving()) {
                                        if (lookForDeath && (players.getMyPlayer().getAnimation() == deathAnimation) || wait) {
                                            if (barr.isOnScreen()) {
                                                camera.moveRandomly(300);
                                                barr.getModel().doClick(true);
                                                //ctx.bot.disableRendering = false;
                                                getBandage = true;
                                                wait = false;
                                                //sleep(random(2000, 4000));
                                                //doingSomething = false;
                                            } else if (!barr.isOnScreen()) {
                                                Walk(barr.getLocation());
                                                wait = true;
                                                sleep(random(2000, 3000));
                                            }
                                        } else if (!lookForDeath) {
                                            if (barr != null && barr.isOnScreen()) {
                                                camera.moveRandomly(300);
                                                barr.getModel().doClick(true);
                                                getBandage = true;
                                                //sleep(random(2000, 4000));
                                                //doingSomething = false;
                                                lookForDeath = true;
                                            } else if (barr != null && !barr.isOnScreen()) {
                                                Walk(barr.getLocation());
                                                sleep(random(2000, 3000));
                                            }
                                        }
                                        barr.doClick(true);
                                        getBandage = true;
                                        sleep(random(2500, 3000));
                                    }
                                }

                                if (whichTeam.equals("red") && inventory.getCount(bandageIdInvent) <= 12 && getBandage) {
                                    RSTile center = new RSTile(1964, 3208);
                                    if (calc.distanceTo(center) >= 2) {
                                        webWalk(center);
                                    }
                                }
                                if (whichTeam.equals("blue") && inventory.getCount(bandageIdInvent) <= 12 && getBandage) {
                                    RSTile center = new RSTile(1809, 3255);
                                    if (calc.distanceTo(center) >= 2) {
                                        webWalk(center);
                                        //log("Here");
                                    }
                                }

                                //getBandage && 
                                if (!inventory.isFull() && inventory.getCount(bandageIdInvent) <= 12 && (inBlueBandage() || inRedBandage()) && !players.getMyPlayer().isInCombat() && getBandage) {
                                    //doingSomething = true;
                                    ActionString = "Searching for bandages";
                                    RSObject bandage = objects.getNearest(42023, 42024);
                                    if (bandage != null) {
                                        ActionString = "Take bandage";
                                        if (bandage.isOnScreen()) {
                                            //sleep(random(4000, 5000));
                                            Point p = bandage.getPoint();
                                            int x = 0;
                                            int f = random(10, 15);
                                            while (x <= f && objects.getNearest(42023, 42024) != null && calc.distanceBetween(p, objects.getNearest(42023, 42024).getPoint()) < 30) {
                                                ActionString = "Clicking on bandages.";
                                                mouse.setSpeed(random(3, 5));
                                                mouse.click(p, true);
                                                int y = random(1, 10);
                                                if (y == 3) {
                                                    mouse.move(bandage.getModel().getCentralPoint(), 4);
                                                    p = mouse.getLocation();
                                                }
                                                x++;
                                            }
                                            if (inventory.getCount(bandageIdInvent) >= 12) {
                                                //doingSomething = false;
                                            }
                                        } else if (!bandage.isOnScreen()) {
                                            Walk(bandage.getLocation());
                                        }
                                    }
                                }
                                if (inventory.getCount(bandageIdInvent) > 12 || inventory.isFull()) {
                                    getBandage = false;
                                }
                                if (doingNothing()) {
                                    try {
                                        RSGroundItem frag = groundItems.getNearest(soulfragId);
                                        if (frag != null) {
                                            ActionString = "Found soulfrag(s).";
                                            if (calc.distanceTo(frag.getLocation()) <= 4) {
                                                ActionString = "Taking soulfrag(s).";
                                                if (!frag.isOnScreen()) {
                                                    ActionString = "Going to frags";
                                                    webWalk(frag.getLocation());
                                                } else if (frag.isOnScreen()) {
                                                    frag.interact("Take");
                                                    sleep(random(500, 1000));
                                                }
                                            }
                                        }
                                    } catch (Exception i) {
                                        log(Color.RED, "Something's wrong with the bot. It doesn't find grounditems");
                                    }
                                }
                                if (Pyre && whichTeam.equals("red") && !getBandage && calc.distanceTo(RedPyreTile) >= 13) {
                                    webWalk(RedPyreTile);
                                }
                                if (Pyre && whichTeam.equals("blue") && !getBandage && calc.distanceTo(BluePyreTile) >= 13) {
                                    webWalk(BluePyreTile);
                                }
                                if (Jelly && !getBandage && calc.distanceTo(SouthJellyTile) >= 20) {
                                    webWalk(SouthJellyTile);
                                }
                                if (Players && !getBandage && calc.distanceTo(oblesikLocation) >= 20) {
                                    webWalk(oblesikLocation);
                                }
                                if (Pyre && ((calc.distanceTo(BluePyreTile) <= 20 || calc.distanceTo(RedPyreTile) <= 20) && !getBandage)) {
                                    fightNpc(pyreId);
                                }
                                if (Jelly && calc.distanceTo(SouthJellyTile) <= 20 && !getBandage) {
                                    fightNpc(jellyId);
                                }
                                if (npcs.getNearest(8601) != null && (calc.distanceTo(npcs.getNearest(8601).getLocation()) <= 1) && ((players.getMyPlayer().getInteracting() != null && players.getMyPlayer().getInteracting().isDead()) || players.getMyPlayer().getInteracting() == null)) {
                                    RSNPC Npc = npcs.getNearest(8601);
                                    if (Npc != null && Npc.getModel() != null) {
                                        Npc.getModel().doClick(true);
                                    }
                                }
                                if (Players && calc.distanceTo(oblesikLocation) <= 20 && ((players.getMyPlayer().getInteracting() != null && players.getMyPlayer().getInteracting().isDead()) || players.getMyPlayer().getInteracting() == null)) {
                                    ActionString = "Attack players.";
                                    int x = 0;
                                    RSPlayer closest = null;
                                    RSPlayer current = null;
                                    while (x < players.getAll().length && x <= 200) {
                                        if (whichTeam.equals("blue")) {
                                            if (players.getAll()[x].getTeam() == 2) {
                                                current = players.getAll()[x];
                                                ActionString = "Scan: " + current.getName();
                                                if (closest != null) {
                                                    if (calc.distanceTo(current) < calc.distanceTo(closest)) {
                                                        closest = current;
                                                    }
                                                }
                                                if (closest == null) {
                                                    closest = players.getAll()[x];
                                                }
                                            }
                                        }
                                        if (whichTeam.equals("red")) {
                                            if (players.getAll()[x].getTeam() == 1) {
                                                current = players.getAll()[x];
                                                ActionString = "Scan: " + current.getName();
                                                if (closest != null) {
                                                    if (calc.distanceTo(current) < calc.distanceTo(closest) && calc.distanceBetween(current.getLocation(), oblesikLocation) < 7) {
                                                        closest = current;
                                                    }
                                                }
                                                if (closest == null) {
                                                    closest = players.getAll()[x];
                                                }
                                            }
                                        }
                                        x++;
                                    }
                                    if (closest != null) {
                                        try {
                                            if (closest.isOnScreen()) {
                                                closest.interact("Attack");
                                                sleep(random(2000, 3000));
                                            } else {
                                                Walk(closest.getLocation());
                                            }
                                        } catch (Exception ignored) {
                                        }
                                    }
                                }
                                //}
                            } else if (interfaces.getComponent(839) == null) {
                                ActionString = "Something went wrong.";
                            }
                        }
                    }
                    if (players.getMyPlayer().getInteracting() != null) {
                        try {
                            ActionString = "Under combat. " + players.getMyPlayer().getInteracting().getName();
                            if (interfaces.getComponent(836) != null) {
                                if (interfaces.getComponent(836, 56).getHeight() <= 58) {
                                    try {
                                        RSGroundItem bones = groundItems.getNearest(bonesID);
                                        if (bones != null) {
                                            if (!inventory.isFull()) {
                                                if (bones.isOnScreen()) {
                                                    ActionString = "Take Bones";
                                                    bones.interact("Take");
                                                } else if (!bones.isOnScreen()) {
                                                    Walk(bones.getLocation());
                                                }
                                            }
                                        } else if (bones == null) {
                                            ActionString = "Going to oblesik";
                                            webWalk(oblesikLocation);
                                        }
                                    } catch (Exception i) {
                                    }
                                }
                            }
                            if (players.getMyPlayer().getHPPercent() <= 65) {
                                if (inventory.contains(bandageIdInvent)) {
                                    ActionString = "Fast heal";
                                    int x = 0;
                                    while (x <= 27 && players.getMyPlayer().getHPPercent() <= 65) {
                                        if (inventory.getItemAt(x).getID() == bandageIdInvent) {
                                            inventory.getItemAt(x).doClick(true);
                                        }
                                        x++;
                                    }
                                }
                            }
                        } catch (NullPointerException i) {
                            ActionString = "Error catched";
                        }
                        //Special
                        if (enableSpecialAttack) {
                            currentSpecial = Integer.parseInt(interfaces.getComponent(884, 8).getText().replace("(", "").replace(")", "").replace("%", "").replace("Special Attack", "").replace(" ", ""));
                            if (lastSpecial == -1 && interfaces.getComponent(884, 8).getText().contains("100")) {
                                game.openTab(Tab.ATTACK);
                                if (interfaces.getComponent(884, 8).getTextColor() != 16776960) {
                                    interfaces.getComponent(884, 8).doClick(true);
                                    sleep(random(4000, 5000));
                                }
                                if (interfaces.getComponent(884, 8).getTextColor() == 16776960) {
                                    interfaces.getComponent(884, 8).doClick(true);
                                    lastSpecial = Integer.parseInt(interfaces.getComponent(884, 8).getText().replace("(", "").replace(")", "").replace("%", "").replace("Special Attack", "").replace(" ", ""));
                                }
                            } else if (lastSpecial != -1) {
                                if (currentSpecial != lastSpecial && specialDamage == -1) {
                                    game.openTab(Tab.ATTACK);
                                    log("Last " + lastSpecial + " Current " + currentSpecial);
                                    specialDamage = lastSpecial - currentSpecial;
                                    log("IAD Detected Special Damage " + specialDamage * 100 + "%");
                                }
                                if (currentSpecial > 0) {
                                    if (currentSpecial / specialDamage >= 1.00 && interfaces.getComponent(884, 8).getTextColor() != 16776960) {
                                        ActionString = "Specialing";
                                        game.openTab(Tab.ATTACK);
                                        lastSpecial = Integer.parseInt(interfaces.getComponent(884, 8).getText().replace("(", "").replace(")", "").replace("%", "").replace("Special Attack", "").replace(" ", ""));
                                        interfaces.getComponent(884, 8).doClick(true);
                                    }
                                }
                            }
                        }
                        AntiBan();
                    }
                    //log(interfaces.getComponent(837, 9).getText().split(":")[1].split("m")[0].replaceAll(" ", ""));
                    if (!ActionString.equals("-") && interfaces.getComponent(837, 9) != null && interfaces.getComponent(837, 9).getText().contains("min")) {
                        try {
                            if (Integer.parseInt(interfaces.getComponent(837, 9).getText().split(":")[1].split("m")[0].replaceAll(" ", "")) >= 5) {
                                if (random(1, 20) == 15) {
                                    ActionString = "Moving mouse off screen";
                                    mouse.moveOffScreen();
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                    if (!ActionString.equals("-") && interfaces.getComponent(837, 9) != null && interfaces.getComponent(837, 9).getText().contains("min")) {
                        try {
                            if (Integer.parseInt(interfaces.getComponent(837, 9).getText().split(":")[1].split("m")[0].replaceAll(" ", "")) <= 5) {
                                if (random(1, 20) == 15) {
                                    ActionString = "Moving mouse slightly";
                                    mouse.moveSlightly();
                                }
                            }
                        } catch (Exception ignored) {
                        }
                    }
                }
            }
            if (lookForDeath && players.getMyPlayer().isDead()) {
                ActionString = "You died.";
                deaths++;
                AntiBan();
                if (random(1, 30) == 20) {
                    keyboard.sendText(deathResponses[random(0, deathResponses.length)], true);
                }
                sleep(random(5000, 7000));
            }
        } catch (Exception ClassCastException) {
            log("ClassCastException handled -> Grounditems are not working! Report this problem to the bot devs, not me!");
        }
        return (random(200, 300));
    }
    //Start of paint by Popm4n
    //START: Code generated using Enfilade's Easel
    public final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    public Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }
    public final Color color1 = Color.cyan.darker(); //Blue Bar
    public final Color color2 = new Color(255, 153, 153); //Constitution Bar
    public final Color color3 = new Color(153, 0, 0); //Attack Bar
    public final Color color4 = new Color(51, 153, 0); //Str Bar
    public final Color color5 = new Color(0, 153, 204); //Defense Bar
    public final Color color6 = new Color(102, 102, 0); //Range Bar
    public final Color color7 = new Color(255, 255, 255, 150); //Fade bar
    public final Color color8 = new Color(30, 30, 30); //Text Color
    public final Color color9 = new Color(20, 20, 20); //Black
    public final Color color10 = new Color(231, 212, 255);
    public final Color color11 = new Color(255, 234, 255);
    public final Color color12 = new Color(255, 80, 255);
    public final Color color13 = new Color(187, 215, 255);
    public final Color color14 = new Color(145, 219, 255);
    public final Color color16 = new Color(255, 255, 255);
    public final BasicStroke stroke1 = new BasicStroke(1);
    public final Font font1 = new Font("Arial", 1, 9);
    public final Font font2 = new Font("Segoe Script", 3, 18);
    public final Font font3 = new Font("Arial", 1, 15);
    public final Font font4 = new Font("Arial", 1, 10);
    public final Font font5 = new Font("Arial", 1, 13);
    public final Font font6 = new Font("Calibri", 0, 18);
    public final Font font7 = new Font("Arial", 0, 13);
    public final Image img1 = getImage("http://i176.photobucket.com/albums/w194/littlecoolomlor2/Untitled-27.jpg");

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        if (showPaint) {
            g.setRenderingHints(antialiasing);
            currExpStr = skills.getCurrentExp(skills.STRENGTH);
            currExpAtt = skills.getCurrentExp(skills.ATTACK);
            currExpDef = skills.getCurrentExp(skills.DEFENSE);
            currExpHit = skills.getCurrentExp(skills.CONSTITUTION);
            currExpRan = skills.getCurrentExp(skills.RANGE);
            currentTime = System.currentTimeMillis();
            double divide = 0;
            divide = currentTime - startTime;
            divide = divide / 1000 / 60 / 60;
            ExpStrHr = Math.round((currExpStr - startExpStr) / divide);
            ExpHitHr = Math.round((currExpHit - startExpHit) / divide);
            ExpDefHr = Math.round((currExpDef - startExpDef) / divide);
            ExpAttHr = Math.round((currExpAtt - startExpAtt) / divide);
            ExpRanHr = Math.round((currExpRan - startExpRan) / divide);

            int ExpStrGained = skills.getCurrentExp(skills.STRENGTH) - startExpStr;
            int strPercentTNL = skills.getPercentToNextLevel(skills.STRENGTH);
            int ExpAttGained = skills.getCurrentExp(skills.ATTACK) - startExpAtt;
            int attPercentTNL = skills.getPercentToNextLevel(skills.ATTACK);
            int ExpDefGained = skills.getCurrentExp(skills.DEFENSE) - startExpDef;
            int defPercentTNL = skills.getPercentToNextLevel(skills.DEFENSE);
            int ExpHitGained = skills.getCurrentExp(skills.CONSTITUTION) - startExpHit;
            int hitPercentTNL = skills.getPercentToNextLevel(skills.CONSTITUTION);
            int ExpRanGained = skills.getCurrentExp(skills.RANGE) - startExpRan;
            int ranPercentTNL = skills.getPercentToNextLevel(skills.RANGE);
            final int hitPercentBar = (int) (skills.getPercentToNextLevel(skills.CONSTITUTION) * 2.00);
            final int attPercentBar = (int) (skills.getPercentToNextLevel(skills.ATTACK) * 2.00);
            final int strPercentBar = (int) (skills.getPercentToNextLevel(skills.STRENGTH) * 2.00);
            final int defPercentBar = (int) (skills.getPercentToNextLevel(skills.DEFENSE) * 2.00);
            final int ranPercentBar = (int) (skills.getPercentToNextLevel(skills.RANGE) * 2.00);
            long Mill = System.currentTimeMillis() - startTime;
            long Hrs = Mill / (1000 * 60 * 60);
            Mill -= Hrs * (1000 * 60 * 60);
            long Min = Mill / (1000 * 60);
            Mill -= Min * (1000 * 60);
            long Sec = Mill / 1000;

            g.drawImage(img1, 1, 339, null);
            g.setColor(color1);
            g.fillRect(20, 447, 200, 17);
            g.fillRect(20, 348, 200, 17);
            g.fillRect(20, 422, 200, 17);
            g.fillRect(20, 398, 200, 17);
            g.fillRect(20, 373, 200, 17);
            g.setFont(font1);
            g.setColor(color9);
            g.drawString("Gained:" + ExpHitGained + "xp" + (" (" + ExpHitHr + "xp/hr)"), 32, 472);
            g.drawString("Gained:" + ExpAttGained + "xp" + (" (" + ExpAttHr + "xp/hr)"), 32, 447);
            g.drawString("Gained:" + ExpStrGained + "xp" + (" (" + ExpStrHr + "xp/hr)"), 32, 422);
            g.drawString("Gained:" + ExpDefGained + "xp" + (" (" + ExpDefHr + "xp/hr)"), 32, 397);
            g.drawString("Gained:" + ExpRanGained + "xp" + (" (" + ExpRanHr + "xp/hr)"), 32, 372);
            g.setColor(color2);
            g.fillRect(20, 447, hitPercentBar, 17);
            g.setColor(color3);
            g.fillRect(20, 422, attPercentBar, 17);
            g.setColor(color4);
            g.fillRect(20, 398, strPercentBar, 17);
            g.setColor(color5);
            g.fillRect(20, 373, defPercentBar, 17);
            g.setColor(color6);
            g.fillRect(20, 348, ranPercentBar, 17);
            g.setColor(color1);
            g.setFont(font2);
            g.setColor(Color.red.darker());
            g.drawString("Voltrain's SoulWars", 273, 365);
            g.setColor(color7);
            g.fillRect(20, 447, 200, 8);
            g.fillRect(20, 422, 200, 8);
            g.fillRect(20, 398, 200, 8);
            g.fillRect(20, 373, 200, 8);
            g.fillRect(20, 348, 200, 8);
            g.setFont(font3);
            g.setColor(color1);
            String Minutes = "" + Min;
            String Seconds = "" + Sec;
            if (Minutes.length() == 1) {
                Minutes = "0" + Minutes;
            }
            if (Seconds.length() == 1) {
                Seconds = "0" + Seconds;
            }
            g.setColor(Color.DARK_GRAY);
            g.drawString("Time: " + Hrs + "h:" + Minutes + "m:" + Seconds + "s", 230, 385);
            g.drawString("Zeal: " + zeals, 236, 405);
            g.drawString("Team: " + whichTeam, 227, 425);
            g.drawString("Status: " + ActionString, 221, 445);
            g.setFont(font4);
            g.setColor(color8);
            g.drawString("" + hitPercentTNL + "%" + " to lvl " + (skills.getCurrentLevel(skills.CONSTITUTION) + 1) + (" Constitution"), 60, 458);
            g.drawString("" + attPercentTNL + "%" + " to lvl " + (skills.getCurrentLevel(skills.ATTACK) + 1) + (" Attack"), 60, 433);
            g.drawString("" + strPercentTNL + "%" + " to lvl " + (skills.getCurrentLevel(skills.STRENGTH) + 1) + (" Stregnth"), 60, 409);
            g.drawString("" + defPercentTNL + "%" + " to lvl " + (skills.getCurrentLevel(skills.DEFENSE) + 1) + (" Defense"), 60, 384);
            g.drawString("" + ranPercentTNL + "%" + " to lvl " + (skills.getCurrentLevel(skills.RANGE) + 1) + (" Range"), 60, 359);
        }
        //Draw interaction
        RSCharacter draw = players.getMyPlayer().getInteracting();
        if (draw != null) {
            if (draw.isOnScreen() && players.getMyPlayer().isOnScreen()) {
                g.setFont(font4);
                g.setColor(Color.RED);
                g.drawRect(draw.getModel().getCentralPoint().x - 15, draw.getModel().getCentralPoint().y - 15, 30, 30);
                g.setColor(Color.BLUE);
                g.drawRect(players.getMyPlayer().getModel().getCentralPoint().x - 15, players.getMyPlayer().getModel().getCentralPoint().y - 15, 30, 30);
                g.setFont(font5);
                g.setColor(Color.WHITE);
                g.drawString("LVL: " + draw.getLevel() + " || Name: " + draw.getName() + " || HP: " + draw.getHPPercent() + "%", calc.tileToScreen(draw.getLocation()).x - 200, calc.tileToScreen(draw.getLocation()).y);
                g.setColor(Color.ORANGE);
                g.drawLine(players.getMyPlayer().getModel().getCentralPoint().x, players.getMyPlayer().getModel().getCentralPoint().y, draw.getModel().getCentralPoint().x, draw.getModel().getCentralPoint().y);
            }
        }
        g.drawOval(mouse.getLocation().x - 11, mouse.getLocation().y - 11, 22, 22);
        g.setColor(Color.BLUE);
        g.drawOval(mouse.getLocation().x - 3, mouse.getLocation().y - 3, 6, 6);
        //Mouse click
        if (mouse.isPressed() || isPressed) {
            isPressed = true;
            if (timeout2Start != 0) {
                timeout2Curr = System.currentTimeMillis();
                if (timeout2Curr - timeout2Start > 5000) {
                    timeout2Start = 0;
                    timeout2Curr = 0;
                    isPressed = false;
                }
            }
            if (timeout2Start == 0) {
                timeout2Start = System.currentTimeMillis();
            }
            g.setColor(Color.PINK);
            g.drawOval(mouse.getPressLocation().x - 11, mouse.getPressLocation().y - 11, 22, 22);
        }
        //Mouse trail credit to Enfilide
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        Point clientCursor = mouse.getLocation();
        mousePathPoint mpp = new mousePathPoint(clientCursor.x, clientCursor.y,
                1000); //1000 = lasting time/MS
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
            mousePath.add(mpp);
        }
        mousePathPoint lastPoint = null;
        for (mousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(a.getColor());
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }
        //Extra
        g.setColor(color10);
        g.fillRoundRect(379, 308, 133, 27, 16, 16);
        g.setColor(color11);
        g.setStroke(stroke1);
        g.drawRoundRect(379, 308, 133, 27, 16, 16);
        g.setFont(font6);
        g.setColor(color12);
        g.drawString("Extended Views", 388, 330);
        if (showExtraPaint) {
            g.setColor(color13);
            g.fillRoundRect(35, 23, 448, 260, 16, 16);
            g.setColor(color14);
            g.drawRoundRect(35, 23, 448, 260, 16, 16);
            g.setFont(font7);
            g.setColor(Color.BLACK);
            g.drawString("Player HP Percent: " + players.getMyPlayer().getHPPercent(), 47, 47);
            g.drawString("Player HP: Unsupported." /*players.getMyPlayer().getAccessor().getHPRatio()*/, 47, 66);
            g.drawString("Player Location: " + players.getMyPlayer().getLocation(), 47, 85);
            if (players.getMyPlayer().getInteracting() != null) {
                g.drawString("Player Interacting Name: " + players.getMyPlayer().getInteracting().getName(), 47, 104);
                g.drawString("Player Interacting HP Percent: " + players.getMyPlayer().getInteracting().getHPPercent(), 47, 123);
                g.drawString("Player Interacting HP: Unsupported" /*players.getMyPlayer().getAccessor().getHPRatio()*/, 47, 142);
            }
            if (players.getMyPlayer().getInteracting() == null) {
                g.drawString("Player Interacting Name: Not available", 47, 104);
                g.drawString("Player Interacting HP: Not available", 47, 123);
                g.drawString("Player Interacting HP Percent: Not available", 47, 142);
            }
            g.drawString("Deaths: " + deaths, 47, 161);
            if (Pyre) {
                g.drawString("Attack Mode: Pyre", 47, 180);
            }
            if (Jelly) {
                g.drawString("Attack Mode: Jellies", 47, 180);
            }
            if (Players) {
                g.drawString("Attack Mode: Players", 47, 180);
            }
            if (joinLastLost) {
                g.drawString("Join Mode: Last Lost", 47, 199);
            }
            if (joinLastWon) {
                g.drawString("Join Mode: Last Won", 47, 199);
            }
            if (joinRed) {
                g.drawString("Join Mode: Red", 47, 199);
            }
            if (joinBlue) {
                g.drawString("Join Mode: Blue", 47, 199);
            }
            if (joinHigh) {
                g.drawString("Join Mode: Highest player's level average", 47, 199);
            }
            if (joinLow) {
                g.drawString("Join Mode: Lowest player's level average", 47, 199);
            }
            if (joinClan) {
                g.drawString("Join Mode: Clan", 47, 199);
            }
            g.drawString("Version: " + version, 47, 218);
            g.drawString("Paint credits: Popm4n, NEXBot (Me)", 47, 237);
            g.setColor(Color.RED);
            g.drawString("VOLTRAIN SOULWARS", 47, 256);
            g.setColor(color16);
            g.drawLine(34, 204, 486, 205);
            g.drawLine(36, 149, 486, 148);
            g.drawLine(35, 91, 486, 90);


        }
    }
    //END: Code generated using Enfilade's Easel

    @SuppressWarnings("serial")
    public class mousePathPoint extends Point { // All credits to Enfilade

        public long finishTime;
        public double lastingTime;

        public int toColor(double d) {
            return Math.min(255, Math.max(0, (int) d));
        }

        public mousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }

        public Color getColor() {
            return new Color(150, 150, 150, toColor(256 * ((finishTime - System //first 3 numbers are the colour
                    .currentTimeMillis()) / lastingTime)));
        }
    }
//End of paint

    public void mouseClicked(MouseEvent e) {
        RSComponent inter = interfaces.get(137).getComponent(0);
        if (inter.getArea().contains(e.getPoint())) {
            showPaint = !showPaint;
        }
        if (new Rectangle(379, 308, 133, 27).contains(e.getPoint())) {
            showExtraPaint = !showExtraPaint;
        }
    }

    public void mousePressed(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseReleased(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseEntered(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void mouseExited(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void messageReceived(MessageEvent me) {
        if (me.getMessage().contains("You can't reach that.")) {
            Walk(players.getMyPlayer().getLocation().randomize(2, 4));


        }
    }

    public class SoulWars extends JFrame {

        public SoulWars() {
            initComponents();
        }

        public void checkBox2ActionPerformed(ActionEvent e) {
            /*
            if (!clanChatTeam.isVisible()) {
            clanChatTeam.setVisible(true);
            } else if (clanChatTeam.isVisible()) {
            clanChatTeam.setVisible(false);
            }
             */
        }

        public void button1ActionPerformed(ActionEvent e) {
            if (attackJelly.isSelected()) {
                Jelly = true;
            }
            if (attackPlayers.isSelected()) {
                Players = true;
            }
            if (attackPyre.isSelected()) {
                Pyre = true;
            }
            if (specialAttackCheck.isSelected()) {
                enableSpecialAttack = true;
            }
            if (chatBotCheck.isSelected()) {
                enableChatBot = true;
            }
            if (redTeam.isSelected()) {
                joinRed = true;
            }
            if (blueTeam.isSelected()) {
                joinBlue = true;
            }
            if (clanChatTeam.isSelected()) {
                joinClan = true;
            }
            if (joinClan) {
                clanNameString = clanName.getText();
            }
            if (highLvlTeam.isSelected()) {
                joinHigh = true;
            }
            if (lowLvlTeam.isSelected()) {
                joinLow = true;
            }
            if (redTeam.isSelected()) {
                joinRed = true;
            }
            if (lastWonTeam.isSelected()) {
                joinLastWon = true;
            }
            if (lastLostTeam.isSelected()) {
                joinLastLost = true;
            }
            gui.setVisible(false);
        }

        public void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner Evaluation license - em ezzy
            attackJelly = new JRadioButton();
            attackPyre = new JRadioButton();
            label1 = new JLabel();
            attackPlayers = new JRadioButton();
            specialAttackCheck = new JCheckBox();
            chatBotCheck = new JCheckBox();
            redTeam = new JCheckBox();
            blueTeam = new JCheckBox();
            lastWonTeam = new JCheckBox();
            lastLostTeam = new JCheckBox();
            clanChatTeam = new JCheckBox();
            highLvlTeam = new JCheckBox();
            lowLvlTeam = new JCheckBox();
            startButton = new JButton();
            clanName = new JTextField();
            label2 = new JLabel();
            label3 = new JLabel();
            label4 = new JLabel();

            //======== this ========
            Container contentPane = getContentPane();
            contentPane.setLayout(null);

            //---- attackJelly ----
            attackJelly.setText("Jelly");
            contentPane.add(attackJelly);
            attackJelly.setBounds(95, 5, 60, attackJelly.getPreferredSize().height);

            //---- attackPyre ----
            attackPyre.setText("Pyrefiends");
            contentPane.add(attackPyre);
            attackPyre.setBounds(95, 25, 95, 18);

            //---- label1 ----
            label1.setText("What to attack?");
            contentPane.add(label1);
            label1.setBounds(5, 25, 95, label1.getPreferredSize().height);

            //---- attackPlayers. ----
            attackPlayers.setText("players.");
            contentPane.add(attackPlayers);
            attackPlayers.setBounds(95, 45, 75, 18);

            //---- specialAttackCheck ----
            specialAttackCheck.setText("Special Attack?");
            contentPane.add(specialAttackCheck);
            specialAttackCheck.setBounds(new Rectangle(new Point(200, 15), specialAttackCheck.getPreferredSize()));

            //---- chatBotCheck ----
            chatBotCheck.setText("Clan Chat Bot?");
            chatBotCheck.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    checkBox2ActionPerformed(e);
                }
            });
            contentPane.add(chatBotCheck);
            chatBotCheck.setBounds(new Rectangle(new Point(200, 35), chatBotCheck.getPreferredSize()));

            //---- redTeam ----
            redTeam.setText("Red");
            contentPane.add(redTeam);
            redTeam.setBounds(new Rectangle(new Point(120, 100), redTeam.getPreferredSize()));

            //---- blueTeam ----
            blueTeam.setText("Blue");
            contentPane.add(blueTeam);
            blueTeam.setBounds(120, 120, 65, 18);

            //---- lastWonTeam ----
            lastWonTeam.setText("Last Won");
            contentPane.add(lastWonTeam);
            lastWonTeam.setBounds(120, 140, 85, 18);

            //---- lastLostTeam ----
            lastLostTeam.setText("Last Lost");
            contentPane.add(lastLostTeam);
            lastLostTeam.setBounds(120, 160, 85, 18);

            //---- clanChatTeam ----
            clanChatTeam.setText("Clan Chat");
            contentPane.add(clanChatTeam);
            clanChatTeam.setBounds(120, 180, 85, 18);

            //---- highLvlTeam ----
            highLvlTeam.setText("Most High Level players.");
            highLvlTeam.setForeground(Color.red);
            contentPane.add(highLvlTeam);
            highLvlTeam.setBounds(215, 125, 160, 18);

            //---- lowLvlTeam ----
            lowLvlTeam.setText("Most Low Level players.");
            lowLvlTeam.setForeground(Color.white);
            contentPane.add(lowLvlTeam);
            lowLvlTeam.setBounds(215, 145, 160, 18);

            //---- startButton ----
            startButton.setText("Start Script");
            startButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    button1ActionPerformed(e);
                }
            });
            contentPane.add(startButton);
            startButton.setBounds(new Rectangle(new Point(155, 235), startButton.getPreferredSize()));

            //---- clanName ----
            clanName.setText("Clan Name");
            contentPane.add(clanName);
            clanName.setBounds(200, 55, 115, clanName.getPreferredSize().height);

            //---- label2 ----
            label2.setText("Version: " + version);
            contentPane.add(label2);
            label2.setBounds(255, 240, 125, label2.getPreferredSize().height);

            //---- label3 ----
            label3.setText("By Muhatashim (NEXBot)");
            contentPane.add(label3);
            label3.setBounds(5, 240, 145, 16);

            //---- label4 ----
            label4.setText("What team?");
            contentPane.add(label4);
            label4.setBounds(5, 125, 80, label4.getPreferredSize().height);

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < contentPane.getComponentCount(); i++) {
                    Rectangle bounds = contentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = contentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                contentPane.setMinimumSize(preferredSize);
                contentPane.setPreferredSize(preferredSize);
            }
            pack();
            setLocationRelativeTo(getOwner());
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - em ezzy
        public JRadioButton attackJelly;
        public JRadioButton attackPyre;
        public JLabel label1;
        public JRadioButton attackPlayers;
        public JCheckBox specialAttackCheck;
        public JCheckBox chatBotCheck;
        public JCheckBox redTeam;
        public JCheckBox blueTeam;
        public JCheckBox lastWonTeam;
        public JCheckBox lastLostTeam;
        public JCheckBox clanChatTeam;
        public JCheckBox highLvlTeam;
        public JCheckBox lowLvlTeam;
        public JButton startButton;
        public JTextField clanName;
        public JLabel label2;
        public JLabel label3;
        public JLabel label4;
    }

    public class SWAuth extends JFrame {

        public SWAuth() {
            initComponents();
        }

        public void button1ActionPerformed(ActionEvent e) {
            log("Activated free trial");
            Activated = false;
            authGui.setVisible(false);
        }

        public void button2ActionPerformed(ActionEvent e) {
            String authCode = textField1.getText();
            Activated = false;
            try {
                URL u = new URL("http://pastebin.com/raw.php?i=04FDWEcA");
                URLConnection connect = u.openConnection();
                connect.connect();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
                String line = reader.readLine();
                while (reader.readLine() != null) {
                    try {
                        line = reader.readLine();
                        if (line.equals(authCode)) {
                            Activated = true;
                            log(Color.green, "Activated paid version, remember to keep your auth private");
                            log(Color.green, "or your auth will be black listed!");
                            authGui.setVisible(false);
                            break;
                        }
                    } catch (NullPointerException y) {
                        log(Color.RED, "Wrong auth");
                        break;
                    }
                }
            } catch (MalformedURLException j) {
                log(Color.RED, "Auth system is currently down, try running free trial");
            } catch (IOException j) {
                log(Color.RED, "Auth system is currently down, try running free trial");
            }
        }

        public void initComponents() {
            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
            // Generated using JFormDesigner Evaluation license - jb jbot
            textField1 = new JTextField();
            button1 = new JButton();
            button2 = new JButton();

            //======== this ========
            Container contentPane = getContentPane();
            contentPane.setLayout(null);

            //---- textField1 ----
            textField1.setText("noauth (Remove this, and yes thats the auth (noauth). I'm still working on the auth system, so enjoy the freeness)");
            contentPane.add(textField1);
            textField1.setBounds(45, 40, 260, textField1.getPreferredSize().height);

            //---- button1 ----
            button1.setText("Run Free Trial (Lasts for 1 game and has ads)");
            button1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    button1ActionPerformed(e);
                }
            });
            contentPane.add(button1);
            button1.setBounds(50, 120, 260, button1.getPreferredSize().height);

            //---- button2 ----
            button2.setText("Use Full Version");
            button2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    button2ActionPerformed(e);
                }
            });
            contentPane.add(button2);
            button2.setBounds(new Rectangle(new Point(115, 85), button2.getPreferredSize()));

            { // compute preferred size
                Dimension preferredSize = new Dimension();
                for (int i = 0; i < contentPane.getComponentCount(); i++) {
                    Rectangle bounds = contentPane.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = contentPane.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                contentPane.setMinimumSize(preferredSize);
                contentPane.setPreferredSize(preferredSize);
            }
            pack();
            setLocationRelativeTo(getOwner());
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - jb jbot
        public JTextField textField1;
        public JButton button1;
        public JButton button2;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}
