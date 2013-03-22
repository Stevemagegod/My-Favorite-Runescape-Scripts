import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.net.*;
import java.util.List;
import java.util.ArrayList;
import java.text.*;
import javax.swing.*;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.*;
import com.rarebot.script.methods.*;
import com.rarebot.script.util.*;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.*;

@ScriptManifest(name = "Castle Wars Pro", version = 1.02, authors = { "XSuicideBotDung" }, keywords = { "Castle", "Wars" }, description = "The Best, Flawless Castle Wars Bot -BETA-")
public class CWPro extends Script implements PaintListener, MouseListener {
    //GUI VARS
    private Image guiBG, guiLabels, guiStats, guiLevels, guiOptions, guiLogo, noGui, miniMap, miniMapOpening, miniMapClosed, miniMapWaiting, miniMapNoIcon, playerPosRed, playerPosBlue;
    private int miniMapExt;
    private boolean showPaint, showMiniMap, guiBuffered, statsOpen, optionsOpen;
    private String guiAction, guiLocation;
    private static final String[] TEAM_OPTION = new String[] {"Zamorak", "Saradomin", "Guthix", "Losers", "Winners"};
    private int joinTeamType;
    private GUI gui;
    private DecimalFormat df = new DecimalFormat("#.###");
    
    //STATS VARS
    private long startTimer;
    private int gamesWon, gamesTied, gamesLost, deaths, kills, flagScores, ticketsWon;
    
    //GAME VARS
    private boolean onRed, initiated, zammyWonLast, useBrace, defendAction, attackAction, getFlagAction, catapultAction, deadTrigger, switchBrace, usedBank;
    private int speed, cooldown, blockCoolDown, attackTimer;
    private NextAction currentAction;
    private RSTile lastPosition;
    private int[] castlewarBraceId = {11083, 11081, 11079};
    private RSItem holdBrace;
    
    //MAP VARS
    private RSTile[] outsideArea = {new RSTile(2438, 3097), new RSTile(2448, 3082)};
    private RSTile[] battlefieldArea = {new RSTile(2368, 3135), new RSTile(2431, 3072)};
    private RSTile[] undergroundArea = {new RSTile(2369, 9528), new RSTile(2430, 9481)};
    private RSTile[] waitAreaBlue = {new RSTile(2371, 9496), new RSTile(2391, 9483)};
    private RSTile[] waitAreaRed = {new RSTile(2411, 9533), new RSTile(2429, 9515)};
    
    private RSTile[] startAreaRed = {new RSTile(2368, 3135), new RSTile(2376, 3127)};
    private RSTile[] castleAreaRed = {new RSTile(2368, 3135), new RSTile(2383, 3120)};
    private RSTile[] fl2AreaRed = {new RSTile(2368, 3135), new RSTile(2379, 3124)};
    private RSTile[] fl3AreaRed = {new RSTile(2368, 3135), new RSTile(2376, 3127)};
    private RSTile[] fl4AreaRed = {new RSTile(2369, 3134), new RSTile(2373, 3130)};
    private RSTile[] wall1Red = {new RSTile(2383, 3132), new RSTile(2385, 3116)};
    private RSTile[] wall2Red = {new RSTile(2374, 3120), new RSTile(2387, 3118)};
    private RSTile[] wall3Red = {new RSTile(2382, 3117), new RSTile(2386, 3121)};

    private RSTile[] startAreaBlue = {new RSTile(2423, 3080), new RSTile(2431, 3072)};
    private RSTile[] castleAreaBlue = {new RSTile(2414, 3088), new RSTile(2431, 3072)};
    private RSTile[] fl2AreaBlue = {new RSTile(2420, 3083), new RSTile(2431, 3072)};
    private RSTile[] fl3AreaBlue = {new RSTile(2425, 3080), new RSTile(2431, 3072)};
    private RSTile[] fl4AreaBlue = {new RSTile(2426, 3077), new RSTile(2430, 3073)};
    private RSTile[] wall1Blue = {new RSTile(2414, 3091), new RSTile(2416, 3075)};
    private RSTile[] wall2Blue = {new RSTile(2412, 3089), new RSTile(2424, 3087)};
    private RSTile[] wall3Blue = {new RSTile(2413, 3086), new RSTile(2417, 3090)};

    private enum Location {
        OUTSIDE, WAITINGROOMRED, WAITINGROOMBLUE, STARTROOMRED, STARTROOMBLUE, FL1RED, FL1BLUE, FL2RED, FL2BLUE, FL3RED, FL3BLUE, FL4RED, FL4BLUE, UNDERGROUND, WALLRED, WALLBLUE, BATTLEFIELD, UNKNOWN
    }

    private enum State {
        STARTINGGAME, WAITING, SPAWNING, ACTIONABLE, UNKNOWN
    }
    
    private enum NextAction {
        DEFEND, ATTACK, RETRIEVE, CATAPULT
    }

    public boolean onStart() {
        initiated = false;
        log("loading images...");
        guiAction = "Loading Images...";
        guiLocation = "Please Wait";
        try {
            Toolkit tool = Toolkit.getDefaultToolkit();
            guiBG = tool.createImage(new URL("http://i56.tinypic.com/29nw6cm.png"));
            guiLabels = tool.createImage(new URL("http://i51.tinypic.com/2h334nq.png"));
            guiStats = tool.createImage(new URL("http://i52.tinypic.com/a0yqt5.png"));
            guiLevels = tool.createImage(new URL("http://i53.tinypic.com/2882vww.png"));
            guiOptions = tool.createImage(new URL("http://i54.tinypic.com/2gy0zkm.png"));
            guiLogo = tool.createImage(new URL("http://img542.imageshack.us/img542/2659/sw3t.png"));         
            noGui = tool.createImage(new URL("http://i55.tinypic.com/2j5c4rs.png"));
            miniMap = tool.createImage(new URL("http://i53.tinypic.com/rb060l.png"));
            miniMapOpening = tool.createImage(new URL("http://i51.tinypic.com/23v02tz.png"));
            miniMapClosed = tool.createImage(new URL("http://i54.tinypic.com/2wqr442.png"));
            miniMapWaiting = tool.createImage(new URL("http://i51.tinypic.com/e9djd5.png"));
            miniMapNoIcon = tool.createImage(new URL("http://i55.tinypic.com/24cfp1u.png"));
            playerPosRed = tool.createImage(new URL("http://i55.tinypic.com/b4hvgy.png"));
            playerPosBlue = tool.createImage(new URL("http://i52.tinypic.com/oggz2b.png"));

            showPaint = true;
            showMiniMap = false;
            guiBuffered = false;
            statsOpen = true;
            optionsOpen = true;
            
            miniMapExt = 0;
        } catch (Exception e) {
            log("Could not load images, Shuting Down");
            return false;
        }
        
        gamesWon = 0;
        gamesTied = 0;
        gamesLost = 0;
        deaths = 0;
        kills = 0;
        flagScores = 0;
        ticketsWon = 0;
        zammyWonLast = false;
        deadTrigger = false;
        blockCoolDown = 0;
        attackTimer = 0;
        switchBrace = false;
        usedBank = false;
        
        onRed =true;
        startTimer = System.currentTimeMillis();
        lastPosition = getMyPlayer().getLocation();
        
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    gui = new GUI();
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        while (!initiated) {
            sleep(500);
        } 
        
        return true;
    }
    
    public int loop() {
        mouse.setSpeed(Random.random(speed-1, speed+1));
        checkForGameEnd();
        try {
            switch (getState()) {
                case STARTINGGAME:
                    guiAction = "Starting New Game";
                    sleep(2500, 3000);
                    
                    startGame();
                    break;
                case WAITING:
                    guiAction = "Waiting for game to start...";
                    if (getLoc(getMyPlayer()) == Location.WAITINGROOMRED) {
                        onRed = true;
                    } else {
                        onRed = false;
                    }     

                    if(interfaces.getComponent(228, 2).isValid())
                        interfaces.getComponent(228, 2).doClick();
                        
                    if(interfaces.canContinue())
                        interfaces.clickContinue();
                        
                    waitingAntiBan();
                    
                    deadTrigger = false;
                    break;
                case SPAWNING:     
                    guiAction = "Spawning in Base";
                    currentAction = null;
                    cooldown = 0;
                    
                    if (deadTrigger == true) {
                        deadTrigger = false;
                        deaths++;
                    } 
                    
                    if (holdBrace != null && switchBrace && inventory.contains(holdBrace.getID())) {
                        if(!inventory.containsOneOf(holdBrace.getID())) {
                            switchBrace = false;
                            log ("brace has been switched");
                        }
                        
                        inventory.getItem(holdBrace.getID()).interact("Wear");
                        log ("wearing hold brace");
                        log("hold brace id = " + holdBrace.getID());         

                    } else {
                        if (holdBrace == null) {
                            log("hold brace is null");
                        } else { 
                            log("hold brace id = " + holdBrace.getID());
                        }
                        
                        log(switchBrace);
                    }
                                    
                    if(!inventory.isFull() || !inventory.contains(590)) {
                        if(!inventory.contains(590)) {
                            if(!inventory.isFull() && groundItems.getNearest(590) != null) {
                                walking.getPath(groundItems.getNearest(590).getLocation()).traverse();
                                try {
                                    groundItems.getNearest(590).interact("Take");
                                } catch (Exception e) {
                                    log(e);
                                }
                            }
                        }
                    
                        if (objects.getNearest(36586) != null && inventory.contains(590)) {
                            proInteract(objects.getNearest(36586), "Take-5 ");
                        } else if (objects.getNearest(36579) != null && inventory.contains(590)) {
                            proInteract(objects.getNearest(36579), "Take-5 ");
                        }
                    } else {
                        if (getLoc(getMyPlayer()) == Location.STARTROOMRED && objects.getNearest(4470) != null) {
                            proInteract(objects.getNearest(4470), "Pass ");
                            onRed = true;
                            sleep(2500, 3000);                     
                        } 
                        if (getLoc(getMyPlayer()) == Location.STARTROOMBLUE && objects.getNearest(4469) != null) {
                            proInteract(objects.getNearest(4469), "Pass ");
                            onRed = false;
                            sleep(2500, 3000);
                        }                     
                    }
                    
                    break; 
                case ACTIONABLE:
                    deadTrigger = true;
                    if (currentAction == null)
                        currentAction = getNextAction();
                        
                    switch (currentAction) {
                        case DEFEND:
                            guiAction = "Defending Base";
                            if (onRed) {
                                if (getLoc(getMyPlayer()) == Location.FL2RED) {
                                    attackPlayers();
                                    waitingAntiBan();
                                } else {
                                    backTrack();
                                }
                            } else {
                                if (getLoc(getMyPlayer()) == Location.FL2BLUE) {
                                    attackPlayers();
                                    waitingAntiBan();
                                } else {
                                    backTrack();
                                }
                            }
                            break;
                        case ATTACK:
                            guiAction = "Attacking Opponents Base";
                            if(getLoc(getMyPlayer()) != Location.BATTLEFIELD && getMyPlayer().getInteracting() == null && !getMyPlayer().isInCombat()) {
                                moveToBattleField();
                            } else { 
                                if (!attackPlayers()) {
                                    if (onRed) {
                                        if(calc.distanceTo(new RSTile(2423, 3101)) > 10 && calc.distanceTo(new RSTile(2400, 3104)) > 15) {
                                            proMover(new RSTile(2400, 3104));
                                        } else if (calc.distanceTo(new RSTile(2423, 3101)) > 15)
                                        {
                                            proMover(new RSTile(2423, 3101));
                                        }
                                    } else {
                                        if(calc.distanceTo(new RSTile(2375, 3102)) > 10 && calc.distanceTo(new RSTile(2400, 3104)) > 15) {
                                            proMover(new RSTile(2400, 3104));
                                        } else if (calc.distanceTo(new RSTile(2375, 3102)) > 15) {
                                            proMover(new RSTile(2375, 3102));
                                        }
                                    } 
                                }

                                waitingAntiBan();
                            }
                            
                            break;
                        case RETRIEVE:
                            guiAction = "Retrieving Flag";
                            break;
                            
                        case CATAPULT:
                            guiAction = "Using Catapult";
                            useCatapult();
                            break;
                    }
                    
                    break;             
            }
                    
            changeLocationGui();

            if (optionsOpen && !gui.isVisible()) {
                gui.setVisible(true);         
            } else if (!optionsOpen && gui.isVisible()) {
                gui.setVisible(false);
            }
            
            return Random.random(600, 1200);
        } catch (Exception e) {
            changeLocationGui();
            e.printStackTrace();
            log(e);
        }
        return Random.random(200, 400);
    }
    
    public boolean checkForGameEnd() {
        try {
            if (interfaces.get(985) != null) {
                if (interfaces.get(985).isValid()) {
                    kills += Integer.parseInt(interfaces.getComponent(985, 21).getText());
                    flagScores += Integer.parseInt(interfaces.getComponent(985, 22).getText());
                    if (interfaces.getComponent(985, 78).getText().contains("won")) {
                        gamesWon++;
                        ticketsWon+=2;
                    } else {
                        if (interfaces.getComponent(985, 78).getText().contains("lost")) {
                            gamesLost++;
                        } else {
                            gamesTied++;
                            ticketsWon++;
                        }
                    }
                    interfaces.getComponent(985, 77).doClick();
                }
                    
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        
        return false;
    }
    
    public boolean moveToBattleField() {
        switch (getLoc(getMyPlayer())) {
            case FL1RED:
                if (calc.distanceTo(new RSTile(2385, 3134)) < calc.distanceTo(new RSTile(2373, 3119))) {
                    proInteract(objects.getAllAt(new RSTile(2385, 3134)), "Unlock");
                    proMover(new RSTile(2385, 3134)); 
                } else {
                    proInteract(objects.getAllAt(new RSTile(2373, 3119)), "Open");
                    proMover(new RSTile(2373, 3118)); 
                } 
                break;
            case FL1BLUE:
                if (calc.distanceTo(new RSTile(2415, 3073)) < calc.distanceTo(new RSTile(2427, 3088))) {
                    proInteract(objects.getAllAt(new RSTile(2415, 3073)), "Unlock");
                    proMover(new RSTile(2413, 3073));     
                } else {                 
                    proInteract(objects.getAllAt(new RSTile(2427, 3088)), "Open");                 
                    proMover(new RSTile(2427, 3089)); 
                }
                break;
            case FL2RED:
                if (calc.distanceTo(new RSTile(2378, 3134)) < calc.distanceTo(new RSTile(2380, 3127))) {
                    proInteract(objects.getAllAt(new RSTile(2378, 3134)), "Climb-down");
                } else {
                    proInteract(objects.getAllAt(new RSTile(2380, 3127)), "Climb-down");
                }
                break;
            case FL2BLUE:
                if (calc.distanceTo(new RSTile(2421, 3073)) < calc.distanceTo(new RSTile(2419, 3080))) {
                    proInteract(objects.getAllAt(new RSTile(2421, 3073)), "Climb-down");
                } else {
                    proInteract(objects.getAllAt(new RSTile(2419, 3080)), "Climb-down");
                }
                break;
            case FL3RED:
                proInteract(objects.getAllAt(new RSTile(2369, 3126)), "Climb-down");
                break;
            case FL3BLUE:
                proInteract(objects.getAllAt(new RSTile(2430, 3081)), "Climb-down");
                break;
            case FL4RED:
                proInteract(objects.getAllAt(new RSTile(2374, 3133)), "Climb-down");
                break;
            case FL4BLUE:
                proInteract(objects.getAllAt(new RSTile(2425, 3074)), "Climb-down");
                break;
            case WALLRED:
                proInteract(objects.getAllAt(new RSTile(2382, 3132)), "Climb-down");
                break;
            case WALLBLUE:
                proInteract(objects.getAllAt(new RSTile(2417, 3075)), "Climb-down");
                break;
            case UNDERGROUND:
                if (calc.distanceTo(new RSTile(2369, 9525)) < calc.distanceTo(new RSTile(2430, 9482))) {
                    proInteract(objects.getAllAt(new RSTile(2369, 9525)), "Climb-up ");
                } else {
                    proInteract(objects.getAllAt(new RSTile(2430, 9482)), "Climb-up ");
                }
                break;
        }
        
        sleep (2000, 2500);
        
        return true;
    }
    
    public boolean useCatapult() { 
        if((onRed && getLoc(getMyPlayer()) == Location.WALLRED) || (!onRed && getLoc(getMyPlayer()) == Location.WALLBLUE)) {
            if (onRed) {
                proInteract(objects.getAllAt(new RSTile(2414, 3089)), "Operate");
            } else {
                proInteract(objects.getAllAt(new RSTile(2414, 3089)), "Operate");
            }
            
            interfaces.getComponent(54, 89).doClick();
            interfaces.getComponent(54, 12).doClick();
        } else {
            moveToWall();
        }
        
        return true;
    }
    
    public boolean moveToWall() {
        if(onRed) {
            switch (getLoc(getMyPlayer())) {
                case FL1RED:
                    proInteract(objects.getAllAt(new RSTile(2382, 3130)), "Climb-up");
                    break;
                case FL1BLUE:
                    if (calc.distanceTo(new RSTile(2415, 3073)) < calc.distanceTo(new RSTile(2427, 3088))) {
                        proInteract(objects.getAllAt(new RSTile(2415, 3073)), "Unlock");
                        proMover(new RSTile(2413, 3073));     
                    } else {                 
                        proInteract(objects.getAllAt(new RSTile(2427, 3088)), "Open");                 
                        proMover(new RSTile(2427, 3089)); 
                    }
                    break;
                case FL2RED:
                    if (calc.distanceTo(new RSTile(2378, 3134)) < calc.distanceTo(new RSTile(2380, 3127))) {
                        proInteract(objects.getAllAt(new RSTile(2378, 3134)), "Climb-down");
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2380, 3127)), "Climb-down");
                    }
                    break;
                case FL2BLUE:
                    if (calc.distanceTo(new RSTile(2421, 3073)) < calc.distanceTo(new RSTile(2419, 3080))) {
                        proInteract(objects.getAllAt(new RSTile(2421, 3073)), "Climb-down");
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2419, 3080)), "Climb-down");
                    }
                    break;
                case FL3RED:
                    proInteract(objects.getAllAt(new RSTile(2369, 3126)), "Climb-down");
                    break;
                case FL3BLUE:
                    proInteract(objects.getAllAt(new RSTile(2430, 3081)), "Climb-down");
                    break;
                case FL4RED:
                    proInteract(objects.getAllAt(new RSTile(2374, 3133)), "Climb-down");
                    break;
                case FL4BLUE:
                    proInteract(objects.getAllAt(new RSTile(2425, 3074)), "Climb-down");
                    break;
                case WALLBLUE:
                    proInteract(objects.getAllAt(new RSTile(2417, 3075)), "Climb-down");
                    break;
                case UNDERGROUND:
                    if (calc.distanceTo(new RSTile(2369, 9525)) < calc.distanceTo(new RSTile(2430, 9482))) {
                        proInteract(objects.getAllAt(new RSTile(2369, 9525)), "Climb-up ");
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2430, 9482)), "Climb-up ");
                    }
                    break;
                case BATTLEFIELD:
                    if (calc.distanceTo(new RSTile(2385, 3134)) < calc.distanceTo(new RSTile(2373, 3119))) {
                        proInteract(objects.getAllAt(new RSTile(2385, 3134)), "Unlock");
                        proMover(new RSTile(2384, 3134)); 
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2373, 3119)), "Open");
                        proMover(new RSTile(2373, 3119)); 
                    } 
                    break;
            }
        } else {
            switch (getLoc(getMyPlayer())) {
                case FL1RED:
                    if (calc.distanceTo(new RSTile(2385, 3134)) < calc.distanceTo(new RSTile(2373, 3119))) {
                        proInteract(objects.getAllAt(new RSTile(2385, 3134)), "Unlock");
                        proMover(new RSTile(2385, 3134)); 
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2373, 3119)), "Open");
                        proMover(new RSTile(2373, 3118)); 
                    } 
                    break;
                case FL1BLUE:                 
                    proInteract(objects.getAllAt(new RSTile(2417, 3077)), "Climb-up");
                    break;
                case FL2RED:
                    if (calc.distanceTo(new RSTile(2378, 3134)) < calc.distanceTo(new RSTile(2380, 3127))) {
                        proInteract(objects.getAllAt(new RSTile(2378, 3134)), "Climb-down");
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2380, 3127)), "Climb-down");
                    }
                    break;
                case FL2BLUE:
                    if (calc.distanceTo(new RSTile(2421, 3073)) < calc.distanceTo(new RSTile(2419, 3080))) {
                        proInteract(objects.getAllAt(new RSTile(2421, 3073)), "Climb-down");
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2419, 3080)), "Climb-down");
                    }
                    break;
                case FL3RED:
                    proInteract(objects.getAllAt(new RSTile(2369, 3126)), "Climb-down");
                    break;
                case FL3BLUE:
                    proInteract(objects.getAllAt(new RSTile(2430, 3081)), "Climb-down");
                    break;
                case FL4RED:
                    proInteract(objects.getAllAt(new RSTile(2374, 3133)), "Climb-down");
                    break;
                case FL4BLUE:
                    proInteract(objects.getAllAt(new RSTile(2425, 3074)), "Climb-down");
                    break;
                case WALLRED:
                    proInteract(objects.getAllAt(new RSTile(2382, 3132)), "Climb-down");
                    break;
                case UNDERGROUND:
                    if (calc.distanceTo(new RSTile(2369, 9525)) < calc.distanceTo(new RSTile(2430, 9482))) {
                        proInteract(objects.getAllAt(new RSTile(2369, 9525)), "Climb-up ");
                    } else {
                        proInteract(objects.getAllAt(new RSTile(2430, 9482)), "Climb-up ");
                    }
                    break;
                case BATTLEFIELD:
                    if (calc.distanceTo(new RSTile(2415, 3073)) < calc.distanceTo(new RSTile(2427, 3088))) {
                        proInteract(objects.getAllAt(new RSTile(2415, 3073)), "Unlock");
                        proMover(new RSTile(2413, 3073));     
                    } else {                 
                        proInteract(objects.getAllAt(new RSTile(2427, 3088)), "Open");                 
                        proMover(new RSTile(2427, 3088)); 
                    }
                    break;
            }
        }
        
        return true;
    }
    
    public boolean attackPlayers() {
        if (getMyPlayer().getLocation().equals(lastPosition) && blockCoolDown > 2 && !getMyPlayer().isInCombat()) {
            log("checking for blocks");
            if (checkForBlocks()) {
                log("breakign blocks");
                breakBlocks();
            }
        } else {
            if (getMyPlayer().getLocation().equals(lastPosition)) {
                blockCoolDown++;
            } else {
                blockCoolDown = 0;
            }
            
            lastPosition = getMyPlayer().getLocation();
        }
        
        if (getMyPlayer().getHPPercent() <= 80 && inventory.contains(4049))
            inventory.getItem(4049).interact("Heal");
    
        if (getMyPlayer().getInteracting() != null && attackTimer < 6) {
            attackTimer++;
            cooldown = 0;     
                
            while (prayer.getPrayerLeft() > 0 && !prayer.isQuickPrayerOn()) {
                mouse.moveSlightly();
                interfaces.getComponent(749, 1).interact("Turn quick prayers on");
                sleep(Random.random(300,500));
            }             
        
            return true;
        } else {
            attackTimer = 0;
            
            if (cooldown < 50) {
                cooldown++;
            } else {
                while (prayer.isQuickPrayerOn()) {
                    mouse.moveSlightly();
                    interfaces.getComponent(749, 1).interact("Turn prayers off");
                    sleep(Random.random(300,500));
                }
            }
        
        
            try {
                if ((!players.getNearest(OPPONENT_VISIBLE).isOnScreen() || calc.distanceTo(players.getNearest(OPPONENT_VISIBLE).getLocation()) > 5) && getLoc(getMyPlayer()) == getLoc(players.getNearest(OPPONENT_VISIBLE)))             
                    camera.turnTo(players.getNearest(OPPONENT_VISIBLE));

                
                if (calc.distanceTo(players.getNearest(OPPONENT_VISIBLE).getLocation()) > 8 && getLoc(getMyPlayer()) == getLoc(players.getNearest(OPPONENT_VISIBLE)))
                    walking.getPath(players.getNearest(OPPONENT_VISIBLE).getLocation()).traverse();
                    
                if (getLoc(getMyPlayer()) == getLoc(players.getNearest(OPPONENT_VISIBLE))) {
                    mouse.move(players.getNearest(OPPONENT_VISIBLE).getModel().getPoint());
                    menu.doAction("Attack");
                    return true;
                }
            } catch (Exception e) {
                return false;
            } 
        }
        return false;
    }
    
    public boolean proInteract(RSObject object, String action) {
        if (object == null) {
            log("null object");
            return false;
        }
        
        attackTimer = 0;
        while (prayer.isQuickPrayerOn()) {
            mouse.moveSlightly();
            interfaces.getComponent(749, 1).interact("Turn prayers off");
            sleep(Random.random(300,500));
        }
        
        if (getMyPlayer().getLocation().equals(lastPosition) && blockCoolDown > 2 && !getMyPlayer().isInCombat()) {
            log("checking for blocks");
            if (checkForBlocks()) {
                log("breakign blocks");
                breakBlocks();
            } else {
                blockCoolDown = 0;
            }
        } else {
            if (getMyPlayer().getLocation().equals(lastPosition)) {
                blockCoolDown++;
            } else {
                blockCoolDown = 0;
            }
            
            lastPosition = getMyPlayer().getLocation();     
        
            try { 
                log(object.getDef().getName());
                    
                if ((!object.isOnScreen() || calc.distanceTo(object.getLocation()) > 5 ) /* && getMyPlayer().isIdle()*/) {
                    walking.getPath(object.getLocation()).traverse();
                    camera.turnTo(object);
                }
            } catch (Exception e) {
                log("null object");
            }
        }
        
        return object.interact(action);
    }
    
    public boolean checkForBlocks() {
        if (calc.distanceTo(npcs.getNearest(NPC_ADJACENT)) == 1)
            return true;
            
        return false;
    }
    
    public boolean breakBlocks() {
        inventory.getItem(590).doClick(true);
        sleep(300,600);
        try {
            npcs.getNearest(1534).interact("Use");
            return true;
        } catch (Exception e) {
            log (e);
            return false;
        }
    }
    
    public boolean proInteract(RSObject[] object, String action) {

        if (object == null) {
            log("null object");
            return false;
        }
        
        attackTimer = 0;
        while (prayer.isQuickPrayerOn()) {
            mouse.moveSlightly();
            interfaces.getComponent(749, 1).interact("Turn prayers off");
            sleep(Random.random(300,500));
        }
        
        if (getMyPlayer().getLocation().equals(lastPosition) && blockCoolDown > 2 && !getMyPlayer().isInCombat()) {
            log("checking for blocks");
            if (checkForBlocks()) {
                log("breakign blocks");
                breakBlocks();
            } else {
                blockCoolDown = 0;
            }
        } else {
            if (getMyPlayer().getLocation().equals(lastPosition)) {
                blockCoolDown++;
            } else {
                blockCoolDown = 0;
            }
            
            lastPosition = getMyPlayer().getLocation();
        
            
            try {
                if ((!object[0].isOnScreen() || calc.distanceTo(object[0].getLocation()) > 5)/* && getMyPlayer().isIdle()*/) {
                    walking.getPath(object[0].getLocation()).traverse();
                    camera.turnTo(object[0]);
                } 
            
                for(RSObject temp : object) {
                    if (temp.getDef().getActions()[0].contains(action))
                        return temp.interact(action);
                }     
            }catch (Exception e) {
                return false;
            } 
        }
        
        return false;
    }
    
    public boolean proMover(RSTile dest) {
        walking.getPath(dest).traverse();
        
        attackTimer = 0;
        while (prayer.isQuickPrayerOn()) {
            mouse.moveSlightly();
            interfaces.getComponent(749, 1).interact("Turn prayers off");
            sleep(Random.random(300,500));
        }
        
        if (getMyPlayer().getLocation().equals(lastPosition) && blockCoolDown > 5 && !getMyPlayer().isInCombat()) {
            log("checking for blocks");
            if (checkForBlocks()) {
                log("breakign blocks");
                breakBlocks();
            } else {
                blockCoolDown = 0;
            }
        } else {
            if (getMyPlayer().getLocation().equals(lastPosition)) {
                blockCoolDown++;
            } else {
                blockCoolDown = 0;
            }
            
            lastPosition = getMyPlayer().getLocation();
        }
        
        return true;
    }
    
    public boolean getBracelet() {
        if (!bank.isOpen()) {
            if (equipment.containsOneOf(castlewarBraceId)) {
                usedBank = true;
                log ("used bank");
                return true;
            }
        
            if (calc.distanceTo(objects.getNearest(4483).getLocation()) > 5) {
                proMover(objects.getNearest(4483).getLocation());
                return false;
            } 
        } 
        if (/*!Equipment.containsOneOf(castlewarBraceId) &&*/ !inventory.containsOneOf(castlewarBraceId) && !usedBank) {         
            if(!bank.isOpen()) {
                proInteract(objects.getNearest(4483), "Use");
                return false;
            }
                
            boolean gotBrace = false;
            for(int i=0; i<=2; i++) {
                if (bank.getCount(castlewarBraceId[i]) != 0) {
                    bank.withdraw(castlewarBraceId[i], 1);
                    gotBrace = true;
                    log ("got brace");
                    return false;
                } 
            }
            
            if(!gotBrace) {
                useBrace = false;
                log ("not using brace anymore");
                gui.disableBrace();
            }
            
            if (inventory.containsOneOf(holdBrace.getID()) || inventory.containsOneOf(castlewarBraceId)) {
                log ("used bank");
                usedBank = false;
            }             
        }     
        
        if (inventory.containsOneOf(castlewarBraceId)) {
            holdBrace = equipment.getItem(Equipment.HANDS);
            switchBrace = true;
            log ("need to switch brace");
            inventory.getItem(castlewarBraceId).interact("Wear"); 
        }             
        
        bank.close();
        return true;
    }
    
    public boolean startGame() {
        if (useBrace && !usedBank) {
            getBracelet();
            return true;
        }
    
        switch(joinTeamType) {
            case 0:                         //Zammy portal
                if (objects.getNearest(4388) != null)
                    proInteract(objects.getNearest(4388), "Enter ");
                sleep(400, 1000);
                guiAction = "Entering Zammy Portal";
                break;
            case 1:                         //Sara portal
                if (objects.getNearest(4387) != null)
                    proInteract(objects.getNearest(4387), "Enter ");
                sleep(400, 1000);
                guiAction = "Entering Sara Portal";
                break;
            case 2:                         //Guthix portal
                if (objects.getNearest(4408) != null)
                    proInteract(objects.getNearest(4408), "Enter ");
                sleep(400, 1000);
                guiAction = "Entering Guthix Portal";
                break;
            case 3:                         //Lossers
                if (gamesWon + gamesTied + gamesLost == 0) {
                    if (objects.getNearest(4408) != null)
                        proInteract(objects.getNearest(4408), "Enter ");
                    sleep(400, 1000);
                    guiAction = "Entering Guthix Portal";
                } else {
                    if (zammyWonLast) {
                        if (objects.getNearest(4387) != null)
                            proInteract(objects.getNearest(4387), "Enter ");
                        sleep(400, 1000);
                        guiAction = "Entering Sara Portal";
                    } else {
                        if (objects.getNearest(4388) != null)
                            proInteract(objects.getNearest(4388), "Enter ");
                        sleep(400, 1000);
                        guiAction = "Entering Zammy Portal";
                    }
                }
                break;
            case 4:                         //Winners
                if (gamesWon + gamesTied + gamesLost == 0) {
                    if (objects.getNearest(4408) != null)
                        proInteract(objects.getNearest(4408), "Enter ");
                    sleep(400, 1000);
                    guiAction = "Entering Guthix Portal";
                } else {
                    if (zammyWonLast) {
                        if (objects.getNearest(4388) != null)
                            proInteract(objects.getNearest(4388), "Enter ");
                        sleep(400, 1000);
                        guiAction = "Entering Zammy Portal";
                    } else {
                        if (objects.getNearest(4387) != null)
                            proInteract(objects.getNearest(4387), "Enter ");
                        sleep(400, 1000);
                        guiAction = "Entering Sara Portal";
                    }
                }
                break;
        }
        
        return true;
    }
    
    public void changeLocationGui() {
        switch (getLoc(getMyPlayer())) {
            case OUTSIDE:
                guiLocation = "Location: Outside";
                break;
            case WAITINGROOMRED:
                guiLocation = "Location: Red Waiting Room";
                break;
            case WAITINGROOMBLUE:
                guiLocation = "Location: Blue Waiting Room";
                break;
            case STARTROOMRED:
                guiLocation = "Location: Red Startroom";
                break;
            case STARTROOMBLUE:
                guiLocation = "Location: Blue Startroom";
                break;
            case FL1RED:
                guiLocation = "Location: Red Base (F1)";
                break;
            case FL1BLUE:
                guiLocation = "Location: Blue Base (F1)";
                break;
            case FL2RED:
                guiLocation = "Location: Red Base (F2)";
                break;
            case FL2BLUE:
                guiLocation = "Location: Blue Base (F2)";
                break;
            case FL3RED:
                guiLocation = "Location: Red Base (F3)";
                break;
            case FL3BLUE:
                guiLocation = "Location: Blue Base (F3)";
                break;
            case FL4RED:
                guiLocation = "Location: Red Base (F4)";
                break;
            case FL4BLUE:
                guiLocation = "Location: Blue Base (F4)";
                break;
            case WALLRED:
                guiLocation = "Location: Red Base Outer Wall";
                break;
            case WALLBLUE:
                guiLocation = "Location: Blue Base Outer Wall";
                break;
            case BATTLEFIELD:
                guiLocation = "Location: Battle Field";
                break;
            case UNDERGROUND:
                guiLocation = "Location: Underground";
                break;
            case UNKNOWN:
                guiLocation = "Location: Unknown";
                break;
        } 
    }
    
    public void backTrack() {
        if (onRed) {
            switch (getLoc(getMyPlayer())) {
                case FL1RED:
                    if (calc.distanceTo(new RSTile(2380, 3129)) < calc.distanceTo(new RSTile(2378, 3134))) {
                        proInteract(objects.getTopAt(new RSTile(2380, 3129), Objects.TYPE_INTERACTABLE), "Climb-up ");
                    } else {
                        proInteract(objects.getTopAt(new RSTile(2378, 3134), Objects.TYPE_INTERACTABLE), "Climb-up ");
                    }                 
                    break;
                case FL1BLUE:
                    if (calc.distanceTo(new RSTile(2415, 3073)) < calc.distanceTo(new RSTile(2427, 3088))) {
                        proInteract(objects.getTopAt(new RSTile(2415, 3073), Objects.TYPE_INTERACTABLE), "Unlock ");
                        proMover(new RSTile(2413, 3073));     
                    } else {                 
                        proInteract(objects.getTopAt(new RSTile(2427, 3088), Objects.TYPE_INTERACTABLE), "Open ");                 
                        proMover(new RSTile(2427, 3089)); 
                    }
                    break;
                case FL2BLUE:
                    if (calc.distanceTo(new RSTile(2421, 3073)) < calc.distanceTo(new RSTile(2419, 3080))) {
                        proInteract(objects.getTopAt(new RSTile(2421, 3073), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    } else {
                        proInteract(objects.getTopAt(new RSTile(2419, 3080), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    }
                    break;
                case FL3RED:
                    proInteract(objects.getTopAt(new RSTile(2369, 3126), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case FL3BLUE:
                    proInteract(objects.getTopAt(new RSTile(2430, 3081), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case FL4RED:
                    proInteract(objects.getTopAt(new RSTile(2374, 3133), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case FL4BLUE:
                    proInteract(objects.getTopAt(new RSTile(2425, 3074), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case WALLRED:
                    proInteract(objects.getTopAt(new RSTile(2382, 3132), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case WALLBLUE:
                    proInteract(objects.getTopAt(new RSTile(2417, 3075), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case BATTLEFIELD:
                    if (calc.distanceTo(new RSTile(2385, 3134)) < calc.distanceTo(new RSTile(2373, 3119))) {
                        proInteract(objects.getTopAt(new RSTile(2385, 3134), Objects.TYPE_INTERACTABLE), "Unlock ");                 
                        proMover(new RSTile(2384, 3134)); 
                    } else {
                        proInteract(objects.getTopAt(new RSTile(2373, 3119), Objects.TYPE_INTERACTABLE), "Open ");                 
                        proMover(new RSTile(2373, 3119)); 
                    }
                    break;
                case UNDERGROUND:
                    proInteract(objects.getTopAt(new RSTile(2369, 9525), Objects.TYPE_INTERACTABLE), "Climb-up ");
                    break;
            }
        } else {
            switch (getLoc(getMyPlayer())) {
                case FL1RED:
                    if (calc.distanceTo(new RSTile(2385, 3134)) < calc.distanceTo(new RSTile(2373, 3119))) {
                        proInteract(objects.getTopAt(new RSTile(2385, 3134), Objects.TYPE_INTERACTABLE), "Unlock ");
                        proMover(new RSTile(2385, 3134)); 
                    } else {
                        proInteract(objects.getTopAt(new RSTile(2373, 3119), Objects.TYPE_INTERACTABLE), "Open ");
                        proMover(new RSTile(2373, 3118)); 
                    } 
                    break;
                case FL1BLUE:
                    if (calc.distanceTo(new RSTile(2419, 3078)) < calc.distanceTo(new RSTile(2421, 3072))) {
                        proInteract(objects.getTopAt(new RSTile(2419, 3078), Objects.TYPE_INTERACTABLE), "Climb-up ");
                    } else {
                        proInteract(objects.getTopAt(new RSTile(2421, 3072), Objects.TYPE_INTERACTABLE), "Climb-up ");
                    }
                    break;
                case FL2RED:
                    if (calc.distanceTo(new RSTile(2379, 3133)) < calc.distanceTo(new RSTile(2378, 3127))) {
                        proInteract(objects.getTopAt(new RSTile(2379, 3133), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    } else {
                        proInteract(objects.getTopAt(new RSTile(2378, 3127), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    }
                    break;
                case FL3RED:
                    proInteract(objects.getTopAt(new RSTile(2369, 3126), Objects.TYPE_INTERACTABLE), "Climb-down");
                    break;
                case FL3BLUE:
                    proInteract(objects.getTopAt(new RSTile(2430, 3081), Objects.TYPE_INTERACTABLE), "Climb-down");
                    break;
                case FL4RED:
                    proInteract(objects.getTopAt(new RSTile(2374, 3133), Objects.TYPE_INTERACTABLE), "Climb-down");
                    break;
                case FL4BLUE:
                    proInteract(objects.getTopAt(new RSTile(2425, 3074), Objects.TYPE_INTERACTABLE), "Climb-down");
                    break;
                case WALLRED:
                    proInteract(objects.getTopAt(new RSTile(2382, 3132), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case WALLBLUE:
                    proInteract(objects.getTopAt(new RSTile(2417, 3075), Objects.TYPE_INTERACTABLE), "Climb-down ");
                    break;
                case BATTLEFIELD:
                    if (calc.distanceTo(new RSTile(2415, 3073)) < calc.distanceTo(new RSTile(2427, 3088))) {
                        proInteract(objects.getTopAt(new RSTile(2415, 3073), Objects.TYPE_INTERACTABLE), "Unlock ");
                    } else {
                        proInteract(objects.getTopAt(new RSTile(2427, 3088), Objects.TYPE_INTERACTABLE), "Open ");
                        proMover(new RSTile(2427, 3088)); 
                    }
                    break;
                case UNDERGROUND:
                    proInteract(objects.getTopAt(new RSTile(2430, 9482), Objects.TYPE_INTERACTABLE), "Climb-up ");
                    break;
            }
        }         
    }
    
    public NextAction getNextAction() {
        List<NextAction> tempBowl = new ArrayList<NextAction>();
        
        if (defendAction)
            tempBowl.add(NextAction.DEFEND);
        if (attackAction)
            tempBowl.add(NextAction.ATTACK);
        if (getFlagAction)
            tempBowl.add(NextAction.RETRIEVE);
        if (catapultAction)
            tempBowl.add(NextAction.CATAPULT);
            
        return tempBowl.get(Random.random(0, tempBowl.size()-1)); 
    }
    
    public State getState() {
        if (getLoc(getMyPlayer()) == Location.OUTSIDE)
            return State.STARTINGGAME;
            
        if (getLoc(getMyPlayer()) == Location.WAITINGROOMBLUE || getLoc(getMyPlayer()) == Location.WAITINGROOMRED)
            return State.WAITING;
            
        if (getLoc(getMyPlayer()) == Location.STARTROOMBLUE || getLoc(getMyPlayer()) == Location.STARTROOMRED)
            return State.SPAWNING;
            
        if (getLoc(getMyPlayer()) != Location.UNKNOWN)
            return State.ACTIONABLE;
            
        return State.UNKNOWN;
    }
    
    public Location getLoc(RSPlayer player) {
        RSTile location = player.getLocation();
    
        if (isInArea(outsideArea, location))
            return Location.OUTSIDE;
            
        if (isInArea(waitAreaBlue, location))
            return Location.WAITINGROOMBLUE;
            
        if (isInArea(waitAreaRed, location))
            return Location.WAITINGROOMRED;

        switch (game.getPlane()) {
            case 0:
                if (isInArea(undergroundArea, location))
                    return Location.UNDERGROUND;
                if (isInArea(wall1Red, location) || isInArea(wall2Red, location) || isInArea(wall3Red, location))
                    return Location.WALLRED;
                if (isInArea(wall1Blue, location) || isInArea(wall2Blue, location) || isInArea(wall3Blue, location))
                    return Location.WALLBLUE;
                if (isInArea(castleAreaRed, location))
                    return Location.FL1RED;
                if (isInArea(castleAreaBlue, location))
                    return Location.FL1BLUE;
                
                break;             
            case 1:
                if (isInArea(startAreaRed, location))
                    return Location.STARTROOMRED;
                if (isInArea(startAreaBlue, location))
                    return Location.STARTROOMBLUE;
                    
                if (isInArea(fl2AreaRed, location))
                    return Location.FL2RED;
                if (isInArea(fl2AreaBlue, location))
                    return Location.FL2BLUE;
                

                break;
            case 2:
                if (isInArea(fl3AreaRed, location))
                    return Location.FL3RED;
                if (isInArea(fl3AreaBlue, location))
                    return Location.FL3BLUE;
                break;
                
            case 3:
                if (isInArea(fl4AreaRed, location))
                    return Location.FL4RED;
                if (isInArea(fl4AreaBlue, location))
                    return Location.FL4BLUE;
                break;
        }
        
        if (isInArea(battlefieldArea, location))
            return Location.BATTLEFIELD;
        
        
        return Location.UNKNOWN;
    }
    
    public boolean isInArea(RSTile[] area, RSTile position) {
        if (((position.getX() >= area[0].getX() && position.getX() <= area[1].getX()) || (position.getX() <= area[0].getX() && position.getX() >= area[1].getX())) && ((position.getY() >= area[0].getY() && position.getY() <= area[1].getY()) || (position.getY() <= area[0].getY() && position.getY() >= area[1].getY())))
            return true;
        return false;
    }
    
    public void waitingAntiBan() {
        if (Random.random(0, 5) == 0) {
            RSObject[] obj = objects.getAll(FILTER_VISIBLE);
            switch (Random.random(1, 5)) {
                case 1:
                    camera.moveRandomly(Random.random(2000, 4200));
                    break;
                case 2:
                    if (Random.random(0, 5) == 0)
                        camera.setPitch(true);
                    break;
                case 3:
                    if (Random.random(0, 5) == 0)
                        walking.getPath(getMyPlayer().getLocation().randomize(2, 2)).traverse();                     
                    break;
                case 4:
                    if (obj.length > 0) {
                        camera.turnTo(obj[Random.random(0, obj.length - 1)].getLocation());
                    }
                    break;
                default:
                    walking.walkTileMM(getMyPlayer().getLocation().randomize(2, 2));
                    break;
            }
        }
    }
    
    public void onRepaint(final Graphics g) {
        Point chatTopLeft = interfaces.get(752).getComponent(0).getLocation();
        
        if (!guiBuffered) {
            g.drawImage(guiBG,0,0,null);
            g.drawImage(guiLabels,0,0,null);
            g.drawImage(guiStats,0,0,null);
            g.drawImage(guiLevels,0,0,null);
            g.drawImage(guiOptions,0,0,null);
            g.drawImage(guiLogo,0,0,null);
            g.drawImage(noGui,0,0,null);
            g.drawImage(miniMap,0,0,null);
            g.drawImage(miniMapOpening,0,0,null);
            g.drawImage(miniMapClosed,0,0,null);
            g.drawImage(miniMapWaiting,0,0,null);
            g.drawImage(miniMapNoIcon,0,0,null);
            g.drawImage(playerPosRed,0,0,null);
            g.drawImage(playerPosBlue,0,0,null);
            guiBuffered = true;
        }
    
        if (showPaint) {         
            if (showMiniMap) {
                if (miniMapExt < 286) {
                    miniMapExt+=10;         
                } 
            } else {
                if (miniMapExt > 0) {
                    miniMapExt-=10;         
                } 
            }
            
            if (miniMapExt < 130) {             
                g.drawImage(miniMapClosed, (int)chatTopLeft.getX()+236, (int)chatTopLeft.getY()-miniMapExt, null);
                if (isInArea(battlefieldArea, getMyPlayer().getLocation())) {
                    if (onRed) {
                        if(showMiniMap)
                            g.drawImage(playerPosRed, (int)((chatTopLeft.getX()+245)+(((2431-getMyPlayer().getLocation().getX())/63.0)*253)), (int)((chatTopLeft.getY()-miniMapExt+9)+(((getMyPlayer().getLocation().getY()-3072)/63.0)*257)), null);
                    } else {
                        if(showMiniMap)
                            g.drawImage(playerPosBlue, (int)((chatTopLeft.getX()+245)+(((2431-getMyPlayer().getLocation().getX())/63.0)*253)), (int)((chatTopLeft.getY()-miniMapExt+9)+(((getMyPlayer().getLocation().getY()-3072)/63.0)*257)), null);
                    }
                }
            } else {
                if (miniMapExt < 250) {
                    g.drawImage(miniMapOpening, (int)chatTopLeft.getX()+236, (int)chatTopLeft.getY()-miniMapExt, null);
                    if (isInArea(battlefieldArea, getMyPlayer().getLocation())) {
                        if (onRed) {
                            if(showMiniMap)
                                g.drawImage(playerPosRed, (int)((chatTopLeft.getX()+245)+(((2431-getMyPlayer().getLocation().getX())/63.0)*253)), (int)((chatTopLeft.getY()-miniMapExt+9)+(((getMyPlayer().getLocation().getY()-3072)/63.0)*257)), null);
                        } else {
                            if(showMiniMap)
                                g.drawImage(playerPosBlue, (int)((chatTopLeft.getX()+245)+(((2431-getMyPlayer().getLocation().getX())/63.0)*253)), (int)((chatTopLeft.getY()-miniMapExt+9)+(((getMyPlayer().getLocation().getY()-3072)/63.0)*257)), null);
                        }
                    }
                } else {
                    g.drawImage(miniMap, (int)chatTopLeft.getX()+236, (int)chatTopLeft.getY()-miniMapExt, null);
                    if (isInArea(battlefieldArea, getMyPlayer().getLocation())) {
                        if (onRed) {
                            if(showMiniMap)
                                g.drawImage(playerPosRed, (int)((chatTopLeft.getX()+245)+(((2431-getMyPlayer().getLocation().getX())/63.0)*253)), (int)((chatTopLeft.getY()-miniMapExt+9)+(((getMyPlayer().getLocation().getY()-3072)/63.0)*257)), null);
                        } else {
                            if(showMiniMap)
                                g.drawImage(playerPosBlue, (int)((chatTopLeft.getX()+245)+(((2431-getMyPlayer().getLocation().getX())/63.0)*253)), (int)((chatTopLeft.getY()-miniMapExt+9)+(((getMyPlayer().getLocation().getY()-3072)/63.0)*257)), null);
                        }
                    }
                }
            }
            
            if (miniMapExt > 0) {
                if (miniMapExt < 42) {
                    g.drawImage(miniMapNoIcon, (int)chatTopLeft.getX()+473, (int)chatTopLeft.getY()+42-miniMapExt, null);
                } else {
                    g.drawImage(miniMapNoIcon, (int)chatTopLeft.getX()+473, (int)chatTopLeft.getY(), null);
                }
            }
            
            g.drawImage(guiBG, (int)chatTopLeft.getX(), (int)chatTopLeft.getY(), null);         
            g.drawImage(guiLogo, (int)chatTopLeft.getX()+2, (int)chatTopLeft.getY()+2, null);
            
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 12) );
            if (guiAction!=null && guiLocation!=null)
                g.drawString(guiAction + " | " + guiLocation, (int)chatTopLeft.getX()+93, (int)chatTopLeft.getY()+15);
            
            
            if (statsOpen) {
                
                g.drawImage(guiStats, (int)chatTopLeft.getX(), (int)chatTopLeft.getY(), null);                 
                long currTimer = System.currentTimeMillis()- startTimer;
                g.drawString(Timer.format(currTimer), (int)chatTopLeft.getX()+378, (int)chatTopLeft.getY()+124);
                g.drawString(gamesWon + " ", (int)chatTopLeft.getX()+193, (int)chatTopLeft.getY()+39);
                g.drawString(gamesTied + " ", (int)chatTopLeft.getX()+193, (int)chatTopLeft.getY()+62);
                g.drawString(gamesLost + " ", (int)chatTopLeft.getX()+193, (int)chatTopLeft.getY()+85);
                g.drawString(deaths + " ", (int)chatTopLeft.getX()+365, (int)chatTopLeft.getY()+40);
                g.drawString(kills + " ", (int)chatTopLeft.getX()+365, (int)chatTopLeft.getY()+62);
                g.drawString(flagScores + " ", (int)chatTopLeft.getX()+365, (int)chatTopLeft.getY()+85);
                g.drawString(ticketsWon + " ", (int)chatTopLeft.getX()+218, (int)chatTopLeft.getY()+113);
                g.drawString(df.format((ticketsWon * 3600000.0)/currTimer) + " ", (int)chatTopLeft.getX()+218, (int)chatTopLeft.getY()+133);
                

            } else {
                g.drawImage(guiLevels, (int)chatTopLeft.getX(), (int)chatTopLeft.getY(), null);
            }
            
            if (optionsOpen) 
                g.drawImage(guiOptions, (int)chatTopLeft.getX(), (int)chatTopLeft.getY(), null);
                
            g.drawImage(guiLabels, (int)chatTopLeft.getX(), (int)chatTopLeft.getY(), null); 
        } else {
            g.drawImage(noGui, (int)chatTopLeft.getX()+503, (int)chatTopLeft.getY()+126, null); 
        }
        
        drawMouse(g);
    }
    
    private void drawMouse(final Graphics g) {
        Point mouseLoc = mouse.getLocation();
    
        // Paint the mouse cursor
        int cursorSize = 0;
        if (mouse.isPressed()) {
            cursorSize = 4; // Set the square in pixels here
        } else {
            cursorSize = 6;
        }
        g.setColor(Color.CYAN); // Your color here
        // Bottom of screen up to cursor
        g.drawLine(mouseLoc.x, 0, mouseLoc.x, mouseLoc.y - cursorSize);
        // Top of screen down to cursor
        g.drawLine(mouseLoc.x, (int)(game.getHeight()), mouseLoc.x, mouseLoc.y + cursorSize);
        // Left of screen to cursor
        g.drawLine(0, mouseLoc.y, mouseLoc.x - cursorSize, mouseLoc.y);
        // Right of screen to cursor
        g.drawLine((int)(game.getWidth()), mouseLoc.y, mouseLoc.x + cursorSize, mouseLoc.y);
        // Cursor
        //g.drawString(lastPosition.toString(), mouseLoc.x, mouseLoc.y+15);
        if (mouse.isPressed()) {
            g.setColor(Color.WHITE);
        } else {
            g.setColor(Color.CYAN);
        }
        int outerCursorMod = cursorSize / 2;
        g.drawRect(mouseLoc.x - cursorSize, mouseLoc.y - cursorSize, cursorSize * 2, cursorSize * 2);
        g.drawRect(mouseLoc.x - cursorSize - outerCursorMod, mouseLoc.y - cursorSize - outerCursorMod, (cursorSize + outerCursorMod) * 2, (cursorSize + outerCursorMod) * 2);
        g.drawRect(mouseLoc.x, mouseLoc.y, 1, 1);
    }
    
    public void mousePressed(MouseEvent e) {
        Point p = e.getPoint();
        if (showPaint && !showMiniMap && p.x >= 473 && p.x <= 519 && p.y >= 339 && p.y <= 383) {
            showMiniMap = true;
        } else if (showPaint && showMiniMap && p.x >= 473 && p.x <= 519 && p.y >= 53 && p.y <= 99) {
            showMiniMap = false;
        }
        
        if (!showPaint && p.x >= 504 && p.x <= 519 && p.y >= 464 && p.y <= 478) {
            showPaint = true;
        } else if (showPaint && p.x >= 504 && p.x <= 519 && p.y >= 464 && p.y <= 478) {
            showPaint = false;
        }
        
        if (statsOpen && p.x >= 1 && p.x <= 84 && p.y >= 426 && p.y <= 451) {
            statsOpen = false;
        } else if (!statsOpen && p.x >= 1 && p.x <= 84 && p.y >= 399 && p.y <= 424) {
            statsOpen = true;
        }
        
        if (p.x >= 1 && p.x <= 84 && p.y >= 453 && p.y <= 479)
            optionsOpen = !optionsOpen;
    }
    
    public void mouseEntered(MouseEvent arg0) {
    }

    public void mouseExited(MouseEvent arg0) {
    }

    public void mouseClicked(MouseEvent arg0) {
    }

    public void mouseReleased(MouseEvent arg0) {
    }
    
    private final Filter<RSObject> FILTER_VISIBLE = new Filter<RSObject>() {
        public boolean accept(RSObject obj) {
            return obj.isOnScreen();
        }
    };
    
    private final Filter<RSNPC> NPC_ADJACENT = new Filter<RSNPC>() {
        public boolean accept(RSNPC target) {
            int playerX = getMyPlayer().getLocation().getX();
            int playerY = getMyPlayer().getLocation().getY();
            int targetX = target.getLocation().getX();
            int targetY = target.getLocation().getY();
            
            if (((targetX == playerX && Math.abs(targetY - playerY) == 1) || (targetY == playerY && Math.abs(targetX - playerX) == 1)) && target.getID() == 1534)
                return true;
                
            return false;
        }
    };


    private final Filter<RSPlayer> OPPONENT_VISIBLE = new Filter<RSPlayer>() {
        public boolean accept(RSPlayer player) {
            if (player.getTeam() != getMyPlayer().getTeam())
                return true;
            return false;
        }
    };
    //-------------------------------------------------------------------------------------------------------------------------------
    
    class GUI extends JFrame implements WindowListener {

        public GUI() {
            initComponents();
            setDefaultCloseOperation(HIDE_ON_CLOSE);
        }
        
        public void disableBrace() {
            useCWBrace.setSelected(false);
        }
        
        private JCheckBox attCastle, defendFlag, enableMagic, enableMelee, enableRange, getFlag, useCWBrace, useCata;
        private JPanel controlTab, magicTab, meleeTab, mainPanel, rangeTab;
        private JButton curEquiptMagic, curEquiptMelee, curEquiptRange, removeMagic, removeMelee, removeRange, startButton;
        private JScrollPane equipmentMagic, equipmentMelee, equipmentRange;
        private JLabel jLabel1, jLabel2;
        private JList jList1, jList2, jList3;
        private JSeparator jSeparator1;
        private JTabbedPane jTabbedPane1;
        private JSlider mouseSpeed;
        private JComboBox joinTeam;
        
        private void initComponents() {

            mainPanel = new JPanel();
            jTabbedPane1 = new JTabbedPane();
            controlTab = new JPanel();
            jLabel1 = new JLabel("Join Team:");
            joinTeam = new JComboBox(TEAM_OPTION);
            useCWBrace = new JCheckBox("Use Castle Wars Bracelet?");
            mouseSpeed = new JSlider();
            jLabel2 = new JLabel("Mouse Speed: (Faster <--> Slower)");
            jSeparator1 = new JSeparator();
            defendFlag = new JCheckBox("Defend Flag");
            useCata = new JCheckBox("Use Catapult");
            attCastle = new JCheckBox("Attack Castle");
            getFlag = new JCheckBox("Get Flag");
            startButton = new JButton("START");
            meleeTab = new JPanel();
            enableMelee = new JCheckBox("Enabled");
            equipmentMelee = new JScrollPane();
            jList1 = new JList();
            removeMelee = new JButton("Remove");
            curEquiptMelee = new JButton("Current Equipt");
            magicTab = new JPanel();
            enableMagic = new JCheckBox("Enabled");
            equipmentMagic = new JScrollPane();
            jList2 = new JList();
            removeMagic = new JButton("Remove");
            curEquiptMagic = new JButton("Current Equipt");
            rangeTab = new JPanel();
            enableRange = new JCheckBox("Enabled");
            equipmentRange = new JScrollPane();
            jList3 = new JList();
            removeRange = new JButton("Remove");
            curEquiptRange = new JButton("Current Equipt");

            mouseSpeed.setMajorTickSpacing(1);
            mouseSpeed.setMinimum(1);
            mouseSpeed.setMaximum(10);
            mouseSpeed.setPaintTicks(true);
            mouseSpeed.setValue(4);
            mouseSpeed.setPaintLabels(true);
            mouseSpeed.setSnapToTicks(true);
            
            startButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    initiated = true;
                    speed = gui.mouseSpeed.getValue();
                    optionsOpen = false;
                    useBrace = useCWBrace.isSelected();
                    defendAction = defendFlag.isSelected(); 
                    attackAction = attCastle.isSelected(); 
                    getFlagAction = getFlag.isSelected();
                    catapultAction = useCata.isSelected();
                    startButton.setText("UPDATE"); 
                    joinTeamType = joinTeam.getSelectedIndex();
                    setVisible(false);
                }
            });         
            
            addWindowListener(this);

            javax.swing.GroupLayout controlTabLayout = new javax.swing.GroupLayout(controlTab);
            controlTab.setLayout(controlTabLayout);
            controlTabLayout.setHorizontalGroup(
                controlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 395, Short.MAX_VALUE)
                .addGroup(controlTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(controlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(getFlag)
                        .addComponent(attCastle)
                        .addComponent(useCata)
                        .addComponent(defendFlag)
                        .addComponent(useCWBrace)
                        .addGroup(controlTabLayout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(joinTeam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                            .addComponent(startButton))
                        .addComponent(mouseSpeed, javax.swing.GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE)
                        .addComponent(jLabel2))
                    .addContainerGap())
            );
            controlTabLayout.setVerticalGroup(
                controlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(controlTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(controlTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(joinTeam, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(startButton))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(useCWBrace)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(defendFlag)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(useCata)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(attCastle)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(getFlag)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(mouseSpeed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Control", controlTab);
/****************************************************************

            jList1.setModel(new javax.swing.AbstractListModel() {
                String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
            equipmentMelee.setViewportView(jList1);

            javax.swing.GroupLayout meleeTabLayout = new javax.swing.GroupLayout(meleeTab);
            meleeTab.setLayout(meleeTabLayout);
            meleeTabLayout.setHorizontalGroup(
                meleeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(meleeTabLayout.createSequentialGroup()
                    .addGroup(meleeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(meleeTabLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(equipmentMelee, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, meleeTabLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(enableMelee)
                            .addGap(138, 138, 138)
                            .addComponent(curEquiptMelee)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(removeMelee)))
                    .addContainerGap())
            );
            meleeTabLayout.setVerticalGroup(
                meleeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, meleeTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(equipmentMelee, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                    .addGroup(meleeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(removeMelee)
                        .addComponent(curEquiptMelee)
                        .addComponent(enableMelee))
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Melee Set-Up", meleeTab);


            jList2.setModel(new javax.swing.AbstractListModel() {
                String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
            equipmentMagic.setViewportView(jList2);

            javax.swing.GroupLayout magicTabLayout = new javax.swing.GroupLayout(magicTab);
            magicTab.setLayout(magicTabLayout);
            magicTabLayout.setHorizontalGroup(
                magicTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 395, Short.MAX_VALUE)
                .addGroup(magicTabLayout.createSequentialGroup()
                    .addGroup(magicTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(magicTabLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(equipmentMagic, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, magicTabLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(enableMagic)
                            .addGap(138, 138, 138)
                            .addComponent(curEquiptMagic)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(removeMagic)))
                    .addContainerGap())
            );
            magicTabLayout.setVerticalGroup(
                magicTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 272, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, magicTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(equipmentMagic, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                    .addGroup(magicTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(removeMagic)
                        .addComponent(curEquiptMagic)
                        .addComponent(enableMagic))
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Magic Set-Up", magicTab);


            jList3.setModel(new javax.swing.AbstractListModel() {
                String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
                public int getSize() { return strings.length; }
                public Object getElementAt(int i) { return strings[i]; }
            });
            equipmentRange.setViewportView(jList3);
            
            javax.swing.GroupLayout rangeTabLayout = new javax.swing.GroupLayout(rangeTab);
            rangeTab.setLayout(rangeTabLayout);
            rangeTabLayout.setHorizontalGroup(
                rangeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 395, Short.MAX_VALUE)
                .addGap(0, 395, Short.MAX_VALUE)
                .addGroup(rangeTabLayout.createSequentialGroup()
                    .addGroup(rangeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(rangeTabLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(equipmentRange, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, rangeTabLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(enableRange)
                            .addGap(138, 138, 138)
                            .addComponent(curEquiptRange)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(removeRange)))
                    .addContainerGap())
            );
            rangeTabLayout.setVerticalGroup(
                rangeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 272, Short.MAX_VALUE)
                .addGap(0, 272, Short.MAX_VALUE)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, rangeTabLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(equipmentRange, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                    .addGroup(rangeTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(removeRange)
                        .addComponent(curEquiptRange)
                        .addComponent(enableRange))
                    .addContainerGap())
            );

            jTabbedPane1.addTab("Range Set-Up", rangeTab);
/*******************************************************************/

            javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
            mainPanel.setLayout(mainPanelLayout);
            mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
            );
            mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
            );
            
            setSize(417, 338);
            add(mainPanel);
            setVisible(true);
        }
        
        public void windowClosing(WindowEvent e) {
            optionsOpen = false;
        }
        
        public void windowDeactivated(WindowEvent e) {
        }
        
        public void windowActivated(WindowEvent e) {
        }
        
        public void windowDeiconified(WindowEvent e) {
        }
        
        public void windowIconified(WindowEvent e) {
        }
        
        public void windowClosed(WindowEvent e) {
        }
        
        public void windowOpened(WindowEvent e) {
        }
    }
}
