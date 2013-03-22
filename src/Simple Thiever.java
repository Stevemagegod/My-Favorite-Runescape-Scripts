//This script is the sole property of Robert G
//Anyone found re-distributing it may face legal consequences!

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.text.NumberFormat;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.wrappers.RSNPC;

@ScriptManifest(name = "Simple Thiever", authors = {"Robert G"}, description = "Trains thieving at the Thieves' Guild on the Pickpocketing trainer.")
public class SimpleThiever extends Script implements MessageListener, PaintListener {
  
	private static final int trainerID = 11281;
	private static int curXP, startXP, curLVL, startLVL, picked = 0;
	private static final long startTime = System.currentTimeMillis();
	private static long runTime;
	private static String status = "Loading...";
	private static boolean stunned;
	private static final NumberFormat nf = NumberFormat.getInstance();
	private final RenderingHints antialiasing = new RenderingHints(
			RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
	@Override
	public boolean onStart() {
		startXP = skills.getCurrentExp(Skills.THIEVING);
		startLVL = skills.getRealLevel(Skills.THIEVING);
		return true;
	}

	@Override
	public int loop() {
		mouse.setSpeed(random(3, 5));
		if (stunned) {
			status = "Stunned, waiting 3 seconds.";
			sleep(3000, 3500);
			stunned = false;
		} else {
			RSNPC x = npcs.getNearest(trainerID);
			if (x != null) {
				if (x.isOnScreen()) {
					status = "Picking trainers pocket.";
					if (x.interact("Pickpocket " + x.getName())) {
						return random(1000, 1200);
					}
				} else {
					status = "Walking to trainer.";
					if (walking.walkTileMM(x.getLocation())) {
						return random(1000, 1200);
					}
				}
			}
		}
		return 0;
	}


	@Override
	public void messageReceived(MessageEvent e) {
		String x = e.getMessage().toLowerCase();
		if (x.contains("you retrieve a")) {
			picked++;
		}
		if (x.contains("you've been stunned")) {
			stunned = true;
		}
	}
	
	private static String formatTime(final long time) {
		final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}
	
	private int PH(int arg0) {
		int PH = (int) (3600000.0 / runTime * arg0);
		return PH;
	}
	
	private String nf(int number) {
		return nf.format(number);
	}

	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHints(antialiasing);
		runTime = (System.currentTimeMillis() - startTime);
		curXP = skills.getCurrentExp(Skills.THIEVING) - startXP;
		curLVL = skills.getRealLevel(Skills.THIEVING);
		int aTrans = 200;
		int x = 0, y = 0, width = 190, height = 1;
		for (int i = 0; i < 210; i++) {
			Color a = new Color(0, 0, 0, aTrans);
			g.setColor(a);
			g.fillRect(x, y, width, height);
			y++;
			if (aTrans > 0) 
				aTrans--;
		}
		int t = 20;
		g.setColor(Color.white);
		g.setFont(new Font("Calibri", 1, 20));
		g.drawString("SimpleThiever", 10, t);
		t += 20;
		g.setFont(new Font("Calibri", 0, 14));
		g.drawString("Run Time: " + formatTime(runTime), 10, t);
		t += 20;
		g.drawString("Picked: " + nf(picked), 10, t);
		t += 20;
		g.drawString("Picked Ph: " + nf(PH(picked)), 10, t);
		t += 20;
		g.drawString("Exp Gained: " + nf(curXP), 10, t);
		t += 20;
		g.drawString("Exp Ph: " + nf(PH(curXP)), 10, t);
		t += 20;
		g.drawString("Levels Gained: " + (curLVL - startLVL), 10, t);
		t += 20;
		g.drawString("Current Level: " + curLVL, 10, t);
		t += 10;
		// stat bar
		int percent = skills.getPercentToNextLevel(Skills.THIEVING);
		int length = (percent * 170) / 100;
		g.setColor(Color.red);
		g.fillRect(10, t, 170, 20);
		g.setColor(Color.black);
		g.drawRect(10, t, 170, 20);
		g.setColor(Color.green);
		g.fillRect(10, t, length, 20);
		g.setColor(Color.black);
		g.drawRect(10, t, length, 20);
		g.setColor(Color.black);
		g.drawString(percent + "% (" + nf(skills.getExpToNextLevel(Skills.THIEVING))+  " Xp Tnl)", 40, t + 15);
		g.setColor(Color.black);
		g.drawString("Status: " + status, 5, 335);
		g.drawLine(0, (int) (mouse.getLocation().getY()) + 1, 800,
				(int) (mouse.getLocation().getY()) + 1);
		g.drawLine((int) (mouse.getLocation().getX()) + 1, 0,
				(int) (mouse.getLocation().getX()) + 1, 800);
	}
	
}
