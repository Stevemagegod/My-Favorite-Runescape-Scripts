import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Equipment;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.wrappers.RSObject;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

@ScriptManifest(authors = {"b0xb0x"}, keywords = {"Agility", "Low wall"}, name = "Low Wall Climber", version = 1.01, description = "Start at either low wall in Brimhaven Agility Arena.")
public class LowWallClimber extends Script implements PaintListener {

    public int lowWallID = 3565;
    
    private long startTime;

    int wallAttempted = -1;
    int wallSucceeded = 0;
    int wallFailed = 0;
    int startExp;
    int currentLevel;
    int currentExp;
    int startLevel;
    int expGained;
    int expPerHr;
    int levelsGained;
    private long runTime;
    int wallToLvl;
    int expToLvl;
    int wallPerHr;
    int seconds;
    int minutes;
    int hours;

    public boolean hopWall;
    public boolean antiBan;

    @Override
    public boolean onStart() {
        log("Starting script!");
        mouse.setSpeed(random(8, 10));
        startTime = System.currentTimeMillis();
        startExp = skills.getCurrentExp(Skills.AGILITY);
        startLevel = skills.getCurrentLevel(Skills.AGILITY);
        return true;
    }

    @Override
    public int loop() {
        hopWall();
        antiBan();
        return random(4500, 4700);
    }

    @Override
    public void onFinish() {
    int seconds = (int) (runTime / 1000);
    int minutes = 0;
    int hours = 0;
    if (seconds >= 60) {
        minutes = seconds / 60;
        seconds -= minutes * 60;
    }
    if (minutes >= 60) {
        hours = minutes / 60;
        minutes -= hours * 60;
    }
    log("Thanks for using Low Wall Climber!");
    log("-----------------------------------------------");
    log("Ran for " + hours + ":" + minutes + ":" + seconds + ".");
    log("Gained " + expGained + " agility experience.");
    log("Gained " + levelsGained + " levels.");
    log("Climbed " + wallSucceeded + " walls.");
    }

    public boolean hopWall() {
        RSObject lowWall = objects.getNearest(3565);
        if (lowWall != null) {
            lowWall.doAction("Climb-over");
            wallAttempted++;
        }
        return true;
    }
    
    public boolean antiBan() {
        final int random = random(1,20);
        if (random == 1) {
            camera.setAngle(56);
            sleep(134);
        }
        if (random == 2) {
            camera.setAngle(189);
            sleep(124);
        }
        if (random == 5) {
            camera.setAngle(93);
            sleep(123);
        }
        if (random == 6) {
            camera.setAngle(13);
            sleep(125);
        }
        if (random == 7) {
            camera.setAngle(253);
            sleep(126);
        }
        if (random == 8) {
            camera.setAngle(132);
            sleep(137);
        }
            return true;
    }

    @Override
    public void onRepaint(Graphics g1) {
        if (game.isLoggedIn()) {
            runTime = System.currentTimeMillis() - startTime;
            expToLvl = skills.getExpToNextLevel(Skills.AGILITY);
            currentLevel = skills.getCurrentLevel(Skills.AGILITY);
            currentExp = skills.getCurrentExp(Skills.AGILITY);
            expGained = currentExp - startExp;
            expPerHr = (int) (expGained * 3600000.0D / runTime);
            levelsGained = currentLevel - startLevel;
            wallSucceeded = expGained / 8;
            wallFailed = wallAttempted - wallSucceeded;
            wallToLvl = expToLvl / 8;
            wallPerHr = (int) (wallSucceeded * 3600000.0D / runTime);
            int seconds = (int) (runTime / 1000);
            int minutes = 0;
            int hours = 0;
            if (seconds >= 60) {
                minutes = seconds / 60;
                seconds -= minutes * 60;
            }
            if (minutes >= 60) {
                hours = minutes / 60;
                minutes -= hours * 60;
            }
            Graphics2D g = (Graphics2D) g1;
            g.setColor(new Color(0, 100, 100, 150));
            g.fillRoundRect(549, 207, 186, 256, 16, 16);
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("GEORGE", Font.BOLD, 22));
            g.drawString("Low Wall Climber", 555, 237);
            g.setFont(new Font("Palatino Linotype", Font.PLAIN, 13));
            g.drawString("Script by b0xb0x" , 565, 253);
            g.drawString("Time elapsed: " + hours + ":" + minutes + ":" + seconds, 565, 272);
            g.drawString("Current level: " + currentLevel, 565, 286);
            g.drawString("Levels gained: " + levelsGained, 565, 300);
            g.drawString("Exp gained: " + expGained, 565, 322);
            g.drawString("Exp/hr: " + expPerHr, 565, 336);
            g.drawString("Exp to level: " + expToLvl, 565, 350);
            g.drawString("Walls climbed: " + wallSucceeded, 565, 372);
            g.drawString("Walls attemped: " + wallAttempted, 565, 386);
            g.drawString("Walls failed: " + wallFailed, 565, 400);
            g.drawString("Walls to lvl: " + wallToLvl, 565, 414);
            g.drawString("Walls/hr: " + wallPerHr, 565, 428);
            g.setColor(new Color(125, 0, 0)); 
            g.fill3DRect(585, 438, 100, 11, true);
            g.setColor(new Color(0, 125, 0));
            g.fill3DRect(585, 438, skills.getPercentToNextLevel(Skills.AGILITY), 11, true); 
            g.setColor(new Color(255, 255, 255));
            g.drawString(skills.getPercentToNextLevel(Skills.AGILITY) + "%  to " + (skills.getCurrentLevel(Skills.AGILITY) + 1), 605, 448);
            g.setColor(new Color(255, 255, 255));
            g.setFont(new Font("Palatino Linotype", Font.PLAIN, 10));
            g.drawString("V 1.01", 700, 460);
        }
    }
