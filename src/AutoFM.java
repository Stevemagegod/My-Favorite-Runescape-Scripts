import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.methods.Trade;
import com.rarebot.script.util.Filter;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSModel;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPath;
import com.rarebot.script.wrappers.RSPlayer;
import com.rarebot.script.wrappers.RSTile;

@ScriptManifest(
  	authors = { "Robert G" }, 
		name = "AutoFM", 
		version = 1.09, 
		description = "Number 1 FM script! || Bonfires || Curly roots @ Jadinko's lair || See thread for more info.")
public class AutoFM extends Script implements PaintListener, MessageListener, MouseListener {
	
	ScriptManifest props = getClass().getAnnotation(ScriptManifest.class);
	
	private static ArrayList<ValidTask> tasks = new ArrayList<ValidTask>();
	
	private static final int TICKET = 24154;
	private static final int DRY_PATCH = 12284;
	private static final int FIRE_PIT = 12286;
	private static final int CURLY_ROOT = 21350;
	private static final int JADE_ROOT = 12274 ;
	private static final int JADE_ROOT_CUT = 12279;
	private static final int HOLE = 12328;
	private static final int LARGE_ROOT = 12321;
	private static final int VINE = 12322;
	private static final int OFFERING_STONE = 12320;
	private static final int DEPOSIT_BOX = 32931;
	private static final int[] ANIMATIONS = { 896, 16699, 16703 };
	private static final int[] INTERFACES = { 1186, 8, 1188, 29 };
	
	private static int logID = -1;
	private static int startLvl = -1;
	private static int startXp = -1;
	private static int logsBurnt = 0;
	private static int spiritsReleased = 0;
	private static int rootsChopped = 0;
	
	private static final Timer CT = new Timer(5000);
	private static Timer BT;
	
	private static final RSArea HOLE_AREA = new RSArea(2944, 2953, 2950, 2958);
	private static RSArea fireArea = null;
	
	private static final RSTile SAFE_TILE = new RSTile(3012, 9273);
	private static final RSTile OFFERING_STONE_TILE = new RSTile(3043, 9225);
	
	private static RSTile vineTile = null;
	private static RSTile destination = null;
	
	private static String state = "Loading....";
	
	private static final String VERSION_URL = "http://pastebin.com/raw.php?i=JsTAqPMY";
	private static final String SCRIPT_URL = "http://www.rarebot.com/community/index.php/topic,4496.0.html";
	
	private static long startTime = -1;
	
	private static final NumberFormat FORMAT = NumberFormat.getIntegerInstance();
	
	private static final Color C1 = new Color(0, 0, 0);
	private static final Color C2 = new Color(255, 255, 255);
	private static final Color C3 = new Color(0, 250, 0, 90);

	private static final BasicStroke S1 = new BasicStroke(2);

	private static final Font F1 = new Font("Tahoma", 1, 14);

	private static Image img1;
	
	private static boolean hide = false, jadinko = false, running = false, debug = false;

    private static final RenderingHints ANIALIASING = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
	private static AutoFMGui g = null;
	private static FMUpdateGui f = null;
	
	 {
		AutoFM.tasks.add(new LoginManager());
		AutoFM.tasks.add(new InterfaceHandler());
		AutoFM.tasks.add(new TicketTask());
		AutoFM.tasks.add(new TimerHandler());
		AutoFM.tasks.add(new CameraPitchTask());
	}
	
	@Override
	public boolean onStart() {
		log("Welcome to AutoFM by Robert G!");
		if (!game.isLoggedIn()) {
			log.severe("Start logged in outside a bank on an empty tile!");
			return false;
		}
		log("Checking for updates.");
		if (Util.checkVersion(props.version(), Util.getCurrentVersion(VERSION_URL, props.name()))) {
			log("You have the latest version, starting script.");
		} else {
			try {
				SwingUtilities.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						f = new FMUpdateGui();
					}
					
				});
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
			while (f.isVisible()) {
				sleep(50);
			}
			return false;
		}
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					g = new AutoFMGui();
				}
				
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		while (g.isVisible()) {
			sleep(50);
		}
		if (logID != -1) {
			new ImageLoader();
			env.disableRandom("Improved Login");
			env.disableRandom("InterfaceCloser");
			mouse.setSpeed(9);
		}
		return logID != -1;
	}

	@Override
	public int loop() {
		if (walking.getDestination() == null) {
			destination = null;
		}
		if (debug()) {
			debug = true;
		} else {
			debug = false;
		}
		for (ValidTask task : tasks) {
			if (task.validate()) {
				try {
					return task.loop();
				} catch (Exception e) {
					return 0;
				}
			}
		}
		return 100;
	}
	
	@Override
	public void onFinish() {
		if (runTime() >= 60 * 60000) {
			env.saveScreenshot(false);
		}
		log("Thanks for using AutoFM by Robert G!");
		env.enableRandom("Improved Login");
		env.enableRandom("InterfaceCloser");
	}
	
	public void log(RSObject object) {
		if (debug)
			log (object.getName()+" Location: " +object.getLocation() + ", Destination tile: " + destination);
	}
	
	public RSNPC fireSpirit() {
		return npcs.getNearest(new Filter<RSNPC>() {

			@Override
			public boolean accept(RSNPC arg0) {
				return arg0.getName().equals("Fire spirit");
			}
			
		});
	}
	
	public RSArea generateArea() {
		final RSTile location = players.getMyPlayer().getLocation();
		final int x = location.getX();
		final int y = location.getY();
		final int z = game.getPlane();
		return new RSArea(new RSTile(x - 1, y - 1, z), new RSTile(x + 1, y + 1, z));
	}
	
	public void sleep(final long time, final boolean condition) {
		final long start = System.currentTimeMillis();
		while(System.currentTimeMillis() - start < time) {
			if (condition)break;
			sleep(50);
		}
	}
	
	private RSTile destination() {
		return destination;
	}
	
	public int distance() {
		if (destination() == null) {
			return 0;
		}
		return calc.distanceTo(destination());
	}
	
	public boolean inLair() {
		return interfaces.get(715).isValid();
	}
	
	public boolean canWalk() {
		return distance() <= 6 || distance() > 0 && !getMyPlayer().isMoving();
	}
	
	private RSComponent getComp() {
		return interfaces.getComponent(137, 55);
	}
	
	private String getText() {
		return getComp().getText().trim();
	}
	
	private boolean debug() {
		return getText().toLowerCase().equals("debug");
	}
	
	private void paintArea(final Graphics g, final RSArea area) {
		final RSTile[] tiles = area.getTileArray();
		if (tiles != null) {
			for (RSTile tile : tiles) {
				if (calc.tileOnScreen(tile)) {
					if (calc.canReach(tile, false)) {
						drawTile(g, tile, new Color(0, 100, 0, 100));
					} else {
						drawTile(g, tile, new Color(100, 0, 0, 100));
					}
				}
			}
		}
	}
	
	private void drawTile(final Graphics g, final RSTile tile, final Color color) {
		final Point sw = calc.tileToScreen(tile, 0, 0, 0);
		final Point se = calc.tileToScreen(new RSTile(tile.getX() + 1, tile.getY()), 0, 0, 0);
		final Point nw = calc.tileToScreen(new RSTile(tile.getX(), tile.getY() + 1), 0, 0, 0);
		final Point ne = calc.tileToScreen(new RSTile(tile.getX() + 1, tile.getY() + 1), 0, 0, 0);
		final int[] xPoints = { (int) nw.getX(), (int) ne.getX(), (int) se.getX(), (int) sw.getX() };
		final int[] yPoints = { (int) nw.getY(), (int) ne.getY(), (int) se.getY(), (int) sw.getY() };
		if (calc.pointOnScreen(sw) && calc.pointOnScreen(se) && calc.pointOnScreen(nw) && calc.pointOnScreen(ne)) {
			g.setColor(Color.BLACK);
			g.drawPolygon(xPoints, yPoints, 4);
			g.setColor(color);
			g.fillPolygon(xPoints, yPoints, 4);
		}
	}
	
	private void drawRoots(Graphics g) {
		final RSObject[] straightJadeRoots = objects.getAll(new Filter<RSObject>() {

			@Override
			public boolean accept(RSObject arg0) {
				return arg0.getID() == AutoFM.JADE_ROOT - 2;
			}
			
		});
		final RSObject[] curlyJadeRoots = objects.getAll(new Filter<RSObject>() {

			@Override
			public boolean accept(RSObject arg0) {
				return arg0.getID() == AutoFM.JADE_ROOT;
			}
			
		});
		final RSObject[] cutJadeRoots = objects.getAll(new Filter<RSObject>() {

			@Override
			public boolean accept(RSObject arg0) {
				return arg0.getID() == AutoFM.JADE_ROOT_CUT;
			}
			
		});
		for (RSObject obj : straightJadeRoots) {
			if (obj != null && obj.getModel() != null) {
				if (obj.isOnScreen()) {
					final RSModel model = obj.getModel();
					for (Polygon p : model.getTriangles()) {
						if (p != null) {
							g.setColor(new Color(250, 0, 0, 100));
							g.fillPolygon(p);
						}
					}
				}
			}
		}
		for (RSObject obj : cutJadeRoots) {
			if (obj != null && obj.getModel() != null) {
				if (obj.isOnScreen()) {
					final RSModel model = obj.getModel();
					for (Polygon p : model.getTriangles()) {
						if (p != null) {
							g.setColor(new Color(0, 250, 0, 100));
							g.fillPolygon(p);
						}
					}
				}
			}
		}
		for (RSObject obj : curlyJadeRoots) {
			if (obj != null && obj.getModel() != null) {
				if (obj.isOnScreen()) {
					final RSModel model = obj.getModel();
					for (Polygon p : model.getTriangles()) {
						if (p != null) {
							g.setColor(new Color(0, 250, 0, 100));
							g.fillPolygon(p);
						}
					}
				}
			}
		}
	}
	
	public boolean walkTileMM(final RSTile t) {
		return walkTileMM(t, 0, 0);
	}

	public boolean walkTileMM(final RSTile t, final int x, final int y) {
		return walkTileMM(t, x, y, 0, 0, 0);
	}
	
	public boolean walkTileMM(final RSTile t, final int x, final int y, final int rx, final int ry, final int rm) {
		int xx = t.getX(), yy = t.getY();
		if (x > 0) {
			if (random(1, 3) == random(1, 3)) {
				xx += random(0, x);
			} else {
				xx -= random(0, x);
			}
		}
		if (y > 0) {
			if (random(1, 3) == random(1, 3)) {
				yy += random(0, y);
			} else {
				yy -= random(0, y);
			}
		}
		RSTile dest = new RSTile(xx, yy);
		if (!calc.tileOnMap(dest)) {
			dest = walking.getClosestTileOnMap(dest);
		}
		AutoFM.destination = dest;
		final Point p = calc.tileToMinimap(dest);
		if (p.x != -1 && p.y != -1) {
			mouse.move(p, rx, ry, rm);
			final Point p2 = calc.tileToMinimap(dest);
			if (p2.x != -1 && p2.y != -1) {
				if (!mouse.getLocation().equals(p2)) {
					mouse.hop(p2);
				}
				if (!mouse.getLocation().equals(p2)) {
					mouse.hop(p2);
				}
				if (!mouse.getLocation().equals(p2)) {
					mouse.hop(p2);
				}
				mouse.click(true, rm);
				return true;
			}
		}
		return false;
	}
	
	public int sleepTime(final RSTile tile) {
		final int dist = calc.distanceTo(tile);
		if (dist <= 1 || tile == null) {
			return random(1000, 1200);
		}
		return (dist * random(600, 700));
	}
	
	private long runTime() {
		if (AutoFM.startTime == -1) {
			AutoFM.startTime = System.currentTimeMillis();
		}
		return System.currentTimeMillis() - AutoFM.startTime;
	}
	
	private int length(final int arg0) {
		return percent() * arg0 / 100;
	}
	
	private int percent() {
		return skills.getPercentToNextLevel(Skills.FIREMAKING);
	}
	
	private int currentLevel() {
		return skills.getRealLevel(Skills.FIREMAKING);
	}
	
	private int xpTnl() {
		return skills.getExpToLevel(Skills.FIREMAKING, currentLevel() + 1);
	}
	
	private long timeTnl() {
		return gainedXp(false) > 0 ? (long) ((double) xpTnl() / (double) gainedXp(true) * 3600000) : 0;
	}
	
	private int gainedXp(final boolean ph) {
		if (AutoFM.startXp == -1) {
			AutoFM.startXp = skills.getCurrentExp(Skills.FIREMAKING);
		}
		return ph ? (ph(skills.getCurrentExp(Skills.FIREMAKING) - AutoFM.startXp)) :
			(skills.getCurrentExp(Skills.FIREMAKING) - AutoFM.startXp);
	}
	
	private int gainedLevels() {
		if (AutoFM.startLvl == -1) {
			AutoFM.startLvl = skills.getRealLevel(Skills.FIREMAKING);
		}
		return currentLevel() - AutoFM.startLvl;
	}
	
	private int ph(final int arg0) {
		int ph = (int) (3600000.0 / runTime() * arg0);
		return ph;
	}
	
	private String logsBurnt(final boolean ph) {
		return ph ? format(ph(AutoFM.logsBurnt)) : format(AutoFM.logsBurnt);
	}
	
	private String spiritsFreed(final boolean ph) {
		return ph ? format(ph(AutoFM.spiritsReleased)) : format(AutoFM.spiritsReleased);
	}
	
	private String rootsChopped(final boolean ph) {
		return ph ? format(ph(AutoFM.rootsChopped)) : format(AutoFM.rootsChopped);
	}
	
	public int currentPoints() {
		if (!interfaces.get(715).isValid()) {
			return 0;
		}
		final String text = interfaces.getComponent(715, 1).getText();
		final String removed = text.replaceAll("<col=ff0000>", "").replaceAll("</col>", "").replaceAll("<col=ff981f>", "");
		return Integer.parseInt(removed.split("/")[0]);
	}
	
	private String format(final int number) {
		return FORMAT.format(number);
	}
	
	@Override
	public void onRepaint(Graphics g1) {
		if (!AutoFM.hide) {
			Graphics2D g = (Graphics2D)g1;
			g.setRenderingHints(AutoFM.ANIALIASING);
			g.drawImage(AutoFM.img1, 6, 344, null);
			g.setColor(AutoFM.C1);
			g.setStroke(AutoFM.S1);
			g.drawRect(5, 343, 509, 131);
			g.setFont(AutoFM.F1);
			g.setColor(AutoFM.C2);
			g.drawString("Run Time: " + Timer.format(runTime()), 10, 360);
			g.drawString("Time Tnl: " + Timer.format(timeTnl()), 380, 360);
			g.drawString("Current Level: " + currentLevel() + " (Gained: " + gainedLevels() + ")", 10, 385);
			g.drawString("Xp Gained: " + format(gainedXp(false)) + " (Ph: " + format(gainedXp(true)) + ")", 255, 385);
			if (AutoFM.jadinko) {
				g.drawString("Roots Burnt: " + logsBurnt(false) + " (Ph: " + logsBurnt(true) + ")", 10, 410);
				g.drawString("Roots cut: " + rootsChopped(false) + " (Ph: " + rootsChopped(true) + ")", 255, 410);
			} else {
				g.drawString("Logs Burnt: " + logsBurnt(false) + " (Ph: " + logsBurnt(true) + ")", 10, 410);
				g.drawString("Spirits Freed: " + spiritsFreed(false) + " (Ph: " + spiritsFreed(true) + ")", 255, 410);
			}
			g.drawString("Status: " + AutoFM.state, 10, 435);
			g.setColor(AutoFM.C3);
			g.fillRect(10, 445, length(500), 25);
			g.setColor(AutoFM.C1);
			g.drawRect(10, 445, 500, 25);
			g.setColor(AutoFM.C2);
			g.drawString(percent() + "% (Xp Tnl: " + format(xpTnl()) + ")", 191, 463);
			if (debug && jadinko) {
				drawRoots(g);
			}
			if (!jadinko) {
				if (AutoFM.fireArea != null) {
					paintArea(g, AutoFM.fireArea);
				}
			}
		}
	}

	@Override
	public void messageReceived(MessageEvent arg0) {
		processMessage(arg0);
	}
	
	private void processMessage(final MessageEvent arg0) {
		final String addToPit = "you successfully refuel the fire";
		final String lightPit = "the fire catches and the roots begin to burn.";
		final String chopped = "you get four curly jade roots";
		final String addTo = "you add a log to the fire.";
		final String light = "the fire catches and the logs begin to burn.";
		final String spirit = "a fire spirit emerges from the bonfire.";
		final String message = arg0.getMessage().toLowerCase();
		if (message.contains(addTo)) {
			AutoFM.logsBurnt++;
		} else if (message.contains(light)) {
			AutoFM.logsBurnt++;
		} else if (message.contains(spirit)) {
			AutoFM.spiritsReleased++;
		} else if (message.contains(addToPit)) {
			AutoFM.logsBurnt++;
		} else if (message.contains(lightPit)) {
			AutoFM.logsBurnt++;
		} else if (message.contains(chopped)) {
			AutoFM.rootsChopped+=4;
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		final Rectangle close = new Rectangle(6, 344, 506, 131);
		final Point p = mouse.getLocation();
		if (close.contains(p) && !AutoFM.hide) {
			AutoFM.hide = true;
		} else if (close.contains(p) && AutoFM.hide) {
			AutoFM.hide = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	public enum Log {
		Normal(1511),
 		Oak(1521),
 		Willow(1519),
 		Teak(6333),
 		Artic_pine(10810),
 		Maple(1517),
 		Mahogany(6332),
 		Eucalyptus(12581),
 		Yew(1515),
 		Magic(1513);
		
		private int lodId;
		
		private Log(int id) {
			this.lodId = id;
		}
		
		public int getId() {
			return this.lodId;
		}
		
		public static String getName(int id) {
			for (Log log : Log.values()) {
				if (log.getId() == id) {
					return log.getName();
				}
			}
			return null;
		}
		
		public String getName() {
			final String name = name();
			return name.replaceAll("_", " ");
		}
 	}
	
	public interface ValidTask {
		boolean validate();
		int loop();
	}
	
	public class LoginManager implements ValidTask {
		
		private boolean skipped = false;

		@Override
		public boolean validate() {
			return game.isWelcomeScreen() || !game.isLoggedIn();
		}
		
		private long start = -1;

		@Override
		public int loop() {
			if (this.start == -1) {
				this.start = System.currentTimeMillis();
			}
			if (!this.skipped) {
				if (interfaces.getComponent(906, 432).isValid()) {
					AutoFM.state = "Closing gay email confirmation window.";
					if (interfaces.getComponent(906, 432).doClick()) {
						this.skipped = true;
						this.start = -1;
						return random(1200, 1400);
					}
				}
			}
			if (interfaces.getComponent(906, 185).isValid()) {
				AutoFM.state = "Clicking play.";
				if (interfaces.getComponent(906, 185).doClick()) {
					this.skipped = false;
					this.start = -1;
					return(5000);
				}
			}
			if (System.currentTimeMillis() - this.start > 30000) {
				log.severe("Time limit reached for login, stopping script.");
				env.saveScreenshot(false);
				return -1;
			}
			return 0;
		}
		
	}
	
	public class CameraPitchTask implements ValidTask {

		@Override
		public boolean validate() {
			if (game.isLoggedIn() && AutoFM.logID > -1) {
				return camera.getPitch() < 80;
			}
			return false;
		}

		@Override
		public int loop() {
			camera.setPitch(true);
			return 0;
		}
		
	}
	
	public class TurnTo extends Thread {
		
		private RSObject object;
		
		private RSNPC npc;
		
		private RSTile tile;
		
		private TurnTo(RSObject object) {
			this.object = object;
			final Thread t = new Thread(this);
			t.start();
		}
		
		private TurnTo(RSNPC npc) {
			this.npc = npc;
			final Thread t = new Thread(this);
			t.start();
		}
		
		private TurnTo(RSTile tile) {
			this.tile = tile;
			final Thread t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			if (object != null) {
				camera.turnTo(object);
			} else if (npc != null) {
				camera.turnTo(npc);
			} else if (tile != null) {
				camera.turnTo(tile);
			} else {
				return;
			}
		}
	}
	
	public class TicketTask implements ValidTask {

		@Override
		public boolean validate() {
			return game.isLoggedIn() && inventory.contains(AutoFM.TICKET);
		}

		@Override
		public int loop() {
			AutoFM.state = "Claiming ticket.";
			if (bank.isOpen()) {
				if (bank.close()) {
					return random(500, 600);
				}
			}
			final RSItem ticket = inventory.getItem(AutoFM.TICKET);
			if (ticket != null) {
				if (ticket.interact("claim")) {
					return random(1000, 1200);
				}
			}
			return 0;
		}
		
	}
	
	public class InterfaceHandler implements ValidTask {
		
		private final Map<Integer, Integer> components = new HashMap<Integer, Integer>();
		
		{
			components.put(109, 14);
			components.put(Trade.INTERFACE_TRADE_MAIN, Trade.INTERFACE_TRADE_MAIN_DECLINE);
			components.put(1218, 76);
			components.put(1252, 5);
			components.put(1272, 25);
		}

		@Override
		public boolean validate() {
			if (game.isLoggedIn()) {
				for (Map.Entry<Integer, Integer> c : components.entrySet()) {
					if (interfaces.getComponent(c.getKey(), c.getValue()).isValid()) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public int loop() {
			if (!validate()) {
				return 0;
			}
			for (final Map.Entry<Integer, Integer> c : components.entrySet()) {
				if (interfaces.getComponent(c.getKey(), c.getValue()).isValid()) {
					if (interfaces.getComponent(c.getKey(), c.getValue()).doClick()) {
						return random(800, 900);
					}
				}
			}
			return 0;
		}
		
	}
	
	public class TimerHandler implements ValidTask {

		@Override
		public boolean validate() {
			if (game.isLoggedIn()) {
				for (int ani : AutoFM.ANIMATIONS) {
					if (getMyPlayer().getAnimation() == ani) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public int loop() {
			if (!AutoFM.running) {
				new Antiban();
				AutoFM.running = true;
			}
			AutoFM.state = "Burning stoof";
			AutoFM.BT.reset();
			return 1000;
		}
		
	}
	
	public class FireSpiritTask implements ValidTask {

		@Override
		public boolean validate() {
			if (game.isLoggedIn() && AutoFM.logID > -1) {
				return fireSpirit() != null;
			}
			return false;
		}

		@Override
		public int loop() {
			if (fireSpirit() == null) {
				return 0;
			}
			if (bank.isOpen() && inventory.getCount() <= 23) {
				if (bank.close()) {
					return 0;
				}
			}
			if (fireSpirit() != null) {
				if (inventory.getCount() <= 23) {
					if (fireSpirit().isOnScreen()) {
						AutoFM.state = "Collecting reward from Fire spirit.";
						if (!getMyPlayer().isMoving()) {
							if (fireSpirit().interact("Collect")) {
								sleep(3000, fireSpirit() == null);
								return 0;
							}
						} else {
							return 250;
						}
					} else {
						if (!getMyPlayer().isMoving()) {
							new TurnTo(fireSpirit());
						}
						if (!fireSpirit().isOnScreen()) {
							AutoFM.state = "Walking to Fire spirit.";
							if (canWalk()) {
								if (walkTileMM(fireSpirit().getLocation())) {
									return random(1000, 1200);
								}
							} else {
								return 250;
							}
						}
					}
				} else {
					if (!bank.isOpen()) {
						AutoFM.state = "No room for reward. Depositing items.";
						if (bank.open()) {
							sleep(1200, bank.isOpen());
							return 0;
						}
					} else {
						if (bank.depositAll()) {
							return random(300, 400);
						}
					}
				}
			}
			return 0;
		}
		
	}
	
	public class FireTask implements ValidTask {

		@Override
		public boolean validate() {
			if (game.isLoggedIn() && AutoFM.logID > -1) {
				if (fireSpirit() == null && !bank.isOpen()) {
					if (inventory.getCount(AutoFM.logID) > 0) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public int loop() {
			if (BT.isRunning()) {
				return 1000;
			} else {
				if (fire() != null) {
					if (fire().isOnScreen()) {
						AutoFM.state = "Adding logs to fire.";
						if (fire().interact("Add-logs")) {
							AutoFM.BT.reset();
							return ((int)sleepTime(fire().getLocation()));
						}
					} else {
						if (!getMyPlayer().isMoving()) {
							new TurnTo(fire());
						}
						if (!fire().isOnScreen()) {
							if (canWalk()) {
								if (walkTileMM(fire().getLocation())) {
									return ((int)sleepTime(fire().getLocation()));
								}
							} else {
								return 300;
							}
						}
					}
				} else {
					AutoFM.state = "Lighting new fire.";
					if (AutoFM.fireArea != null) {
						if (calc.distanceTo(AutoFM.fireArea.getCentralTile()) == 0) {
							if (inventory.getItem(AutoFM.logID) != null) {
								if (inventory.getItem(AutoFM.logID).interact("Light")) {
									sleep(3500, fire() != null);
									return 0;
								}	
							}
						} else {
							if (canWalk()) {
								if (!calc.tileOnScreen(AutoFM.fireArea.getCentralTile())) {
									if (walkTileMM(AutoFM.fireArea.getCentralTile())) {
										return random(1000, 1200);
									}
								} else {
									if (walking.walkTileOnScreen(AutoFM.fireArea.getCentralTile())) {
										return random(1000, 1200);
									}
								}
							} else {
								return 300;
							}
						}
					}
				}
			}
			return 300;
		}
		
		private RSObject fire() {
			return objects.getNearest(new Filter<RSObject>() {

				@Override
				public boolean accept(RSObject arg0) {
					return arg0.getName().equals("Fire") 
						&& calc.distanceBetween(AutoFM.fireArea.getCentralTile(), arg0.getLocation()) <= 1;
				}
				
			});
		}
		
	}
	
	public class BankingTask implements ValidTask {

		@Override
		public boolean validate() {
			if (game.isLoggedIn() && AutoFM.logID > -1) {
				if (fireSpirit() == null) {
					if (inventory.getCount(AutoFM.logID) == 0 || bank.isOpen() && fireSpirit() == null) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public int loop() {
			if (!bank.isOpen()) {
				AutoFM.state = "Opening bank.";
				if (bank.open()) {
					sleep(1000, bank.isOpen());
					return 0;
				}
			} else {
				if (inventory.getCountExcept(logID) > 0) {
					AutoFM.state = "Depositing items.";
					if (bank.depositAll()) {
						return random(500, 600);
					}
				}
				if (inventory.getCount(AutoFM.logID) > 0) {
					AutoFM.state = "Closing bank.";
					if (bank.close()) {
						return 0;
					}
				} else {
					if (bank.getCount(AutoFM.logID) > 0) {
						AutoFM.state = "Withdrawing logs.";
						if (bank.withdraw(AutoFM.logID, 0)) {
							sleep(500, inventory.contains(AutoFM.logID));
							loop();
						}
					} else {
						log.severe("Ran out of logs, stopping.");
						env.saveScreenshot(false);
						return -1;
					}
				}
			}
			return 0;
		}
		
	}
	
	public class JadinkoCombatHandler implements ValidTask {

		@Override
		public boolean validate() {
			return game.isLoggedIn() && getMyPlayer().isInCombat() && inLair();
		}

		@Override
		public int loop() {
			if (calc.distanceTo(AutoFM.SAFE_TILE) > 6) {
				AutoFM.CT.reset();
				AutoFM.state = "Avoiding combat!, walking to safe area.";
				final RSPath path = walking.getPath(AutoFM.SAFE_TILE);
				if (path != null) {
					if (distance() <= 6) {
						if (path.traverse()) {
							return random(800, 900);
						}
					} else {
						return 250;
					}
				}
			} else {
				if (AutoFM.CT.isRunning()) {
					return 300;
				} else {
					AutoFM.state = "Still in combat!, exiting lair.";
					final RSObject exit = objects.getNearest(new Filter<RSObject>() {

						@Override
						public boolean accept(RSObject arg0) {
							return arg0.getName().contains("exit");
						}
						
					});
					if (exit != null) {
						if (exit.isOnScreen()) {
							if (getMyPlayer().getAnimation() == -1) {
								if (exit.interact("exit")) {
									return sleepTime(exit.getLocation());
								}
							} else {
								return 250;
							}
						} else {
							if (!getMyPlayer().isMoving()) {
								new TurnTo(exit);
							}
							if (!exit.isOnScreen()) {
								if (canWalk()) {
									if (walkTileMM(exit.getLocation())) {
										return random(800, 900);
									}
								} else {
									return 250;
								}
							}
						}
					}
				}
				return 250;
			}
			return 0;
		}
		
	}
	
	public class JadinkoHoleTask implements ValidTask {

		@Override
		public boolean validate() {
			return game.isLoggedIn() && AutoFM.HOLE_AREA.contains(getMyPlayer().getLocation());
		}

		@Override
		public int loop() {
			if (getMyPlayer().isInCombat()) {
				AutoFM.state = "Waiting till out of combat.";
				return 500;
			} else {
				if (getMyPlayer().getAnimation() == -1) {
					final RSObject hole = objects.getNearest(new Filter<RSObject>() {

						@Override
						public boolean accept(RSObject arg0) {
							return arg0.getID() == AutoFM.HOLE;
						}
						
					});
					if (hole != null) {
						if (hole.isOnScreen()) {
							if (hole.interact("squeeze")) {
								return sleepTime(hole.getLocation());
							}
						} else {
							if (!getMyPlayer().isMoving()) {
								new TurnTo(hole);
							}
						}
					}
				} else {
					AutoFM.state = "Entering Jadinko lair.";
					return 400;
				}
			}
			return 0;
		}
		
	}
	
	public class JadinkoRewardsTask implements ValidTask {

		@Override
		public boolean validate() {
			return game.isLoggedIn() && currentPoints() == 2000 && inventory.getCount() == 0 && inLair();
		}

		@Override
		public int loop() {
			if (calc.distanceTo(AutoFM.OFFERING_STONE_TILE) > 10) {
				AutoFM.state = "Walking to Offering stone.";
				if (canWalk()) {
					if (walkTileMM(AutoFM.OFFERING_STONE_TILE)) {
						return random(1000, 1200);
					}
				} else {
					return 300;
				}
			} else {
				AutoFM.state = "Claiming rewards.";
				if (interfaces.get(AutoFM.INTERFACES[0]).isValid()) {
					if (interfaces.getComponent(AutoFM.INTERFACES[0], AutoFM.INTERFACES[1]).doClick()) {
						return random(900, 1000);
					}
				}
				if (interfaces.get(AutoFM.INTERFACES[2]).isValid()) {
					if (interfaces.getComponent(AutoFM.INTERFACES[2], AutoFM.INTERFACES[3]).doClick()) {
						return random(900, 1000);
					}
				}
				final RSObject stone = objects.getNearest(new Filter<RSObject>() {

					@Override
					public boolean accept(RSObject arg0) {
						return arg0.getID() == AutoFM.OFFERING_STONE;
					}
					
				});
				if (stone != null) {
					if (stone.isOnScreen()) {
						if (stone.interact("Withdraw-all rewards")) {
							return random(1000, 1200);
						}
					} else {
						new TurnTo(stone);
						if (!stone.isOnScreen()) {
							if (canWalk()) {
								if (walkTileMM(stone.getLocation())) {
									return random(1000, 1200);
								}
							} else {
								return 300;
							}
						}
					}
				}
			}
			return 0;
		}
		
	}
	
	public class JadinkoBankingTask implements ValidTask {

		@Override
		public boolean validate() {
			return game.isLoggedIn() && inventory.getCountExcept(AutoFM.CURLY_ROOT) > 4 || isOpen();
		}
		
		private boolean isOpen() {
			return interfaces.get(11).isValid();
		}
		
		private boolean close() {
			if (!isOpen()) {
				return true;
			}
			return interfaces.getComponent(11, 15).doClick();
		}
		
		private int getBoxCountExcluding(final int... ids) {
			if (!isOpen()) {
				return -1;
			}
			int count = 0;
			for (int i = 0; i < 28; ++i) {
				for (final int id : ids) {
					final RSComponent comp = interfaces.get(11).getComponent(17);
					if (comp != null && comp.isValid() && getID(i) != id && getID(i) != -1) {
						count++;
					}
				}
			}
			return count;
		}
		
		private int getID(final int index) {
			final RSComponent comp = interfaces.get(11).getComponent(17);
			final RSComponent comps[] = comp.getComponents();
			return comps[index].getComponentID();
		}

		@Override
		public int loop() {
			if (inLair()) {
				final RSObject root = objects.getNearest(new Filter<RSObject>() {

					@Override
					public boolean accept(RSObject arg0) {
						return arg0.getID() == AutoFM.LARGE_ROOT;
					}
					
				});
				if (root != null) {
					if (root.isOnScreen()) {
						AutoFM.state = "Entering Large root.";
						if (getMyPlayer().getAnimation() == -1) {
							if (root.interact("enter")) {
								sleep(1200, getMyPlayer().getAnimation() != -1);
								return 0;
							}
						} else {
							return 500;
						}
					} else {
						new TurnTo(root);
						if (!root.isOnScreen()) {
							if (canWalk()) {
								AutoFM.state = "Walking to large root.";
								if (walkTileMM(root.getLocation())) {
									return random(900, 1000);
								}
							} else {
								return 250;
							}
						}
					}
				}
			} else {
				if (isOpen()) {
					AutoFM.state = "Depositing rewards.";
					if (getBoxCountExcluding(AutoFM.CURLY_ROOT) > 0) {
						if (bank.depositAll()) {
							return random(1000, 1200);
						}
					} else {
						if (close()) {
							return 0;
						}
					}
				} else {
					final RSObject box = objects.getNearest(new Filter<RSObject>() {
	
						@Override
						public boolean accept(RSObject arg0) {
							return arg0.getID() == AutoFM.DEPOSIT_BOX;
						}
						
					});
					if (box != null) {
						if (box.isOnScreen()) {
							AutoFM.state = "Opening deposit box.";
							if (box.interact("deposit")) {
								sleep(1500, bank.isOpen());
								return 0;
							}
						} else {
							new TurnTo(box);
							if (!box.isOnScreen()) {
								if (canWalk()) {
									if (walkTileMM(box.getLocation())) {
										return sleepTime(box.getLocation());
									}
								} else {
									return 300;
								}
							}
						}
					}
				}
			}
			return 0;
		}
		
	}
	
	public class JadinkoVineTask implements ValidTask {
		
		private final RSArea DEPOSIT_AREA = new RSArea(2945, 2882, 2953, 2889);

		@Override
		public boolean validate() {
			return game.isLoggedIn() && this.DEPOSIT_AREA.contains(getMyPlayer().getLocation())
				&& inventory.getCountExcept(AutoFM.CURLY_ROOT) == 0;
		}

		@Override
		public int loop() {
			if (getMyPlayer().getAnimation() != -1) {
				AutoFM.state = "Entering lair.";
				return 500;
			}
			final RSObject vine = objects.getNearest(new Filter<RSObject>() {

				@Override
				public boolean accept(RSObject arg0) {
					return arg0.getID() == AutoFM.VINE;
				}
				
			});
			if (vine != null) {
				if (vine.isOnScreen()) {
					AutoFM.state = "Entering vine.";
					if (vine.interact("enter")) {
						return random(1000, 1200);
					}
				} else {
					new TurnTo(vine);
					if (!vine.isOnScreen()) {
						AutoFM.state = "Walking to vine.";
						if (canWalk()) {
							if (walkTileMM(vine.getLocation())) {
								log(vine);
								if (!AutoFM.running) {
									new Antiban();
									AutoFM.running = true;
								}
								return random(800, 900);
							}
						} else {
							return 300;
						}
					}
				}
			}
			return 0;
		}
		
	}
	
	public class JadinkoChoppingTask implements ValidTask {
		
		private final Filter<RSObject> VINE_FILTER = new Filter<RSObject>() {
			public boolean accept(RSObject arg0) {
				final RSPlayer[] playerlist = players.getAll();
				if (arg0.getID() == AutoFM.JADE_ROOT) {
					if (playerlist == null) {
						return true;
					} else if (playerlist != null) {
						for (RSPlayer player : playerlist) {
							if (player != null) {
								if (calc.distanceBetween(player.getLocation(), arg0.getLocation()) > 1) {
									return true;
								} else if (calc.distanceBetween(player.getLocation(), arg0.getLocation()) <= 1) {
									if (player.getAnimation() == -1 || player.isInCombat()) {
										return true;
									}
								}
							}
						}
					}
				}
				return false;
			}
		};
		
		private boolean rootValid() {
			final RSObject object = objects.getTopAt(vineTile);
			if (vineTile == null || object == null) {
				return false;
			} else {
				if (object.getID() != AutoFM.JADE_ROOT && object.getID() != AutoFM.JADE_ROOT_CUT) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean validate() {
			return game.isLoggedIn() && !inventory.isFull() && AutoFM.BT.getRemaining() == 0 && inLair();
		}

		@Override
		public int loop() {
			if (getMyPlayer().getAnimation() == -1 || !rootValid()) {
				final RSObject cutRoot = objects.getNearest(new Filter<RSObject>() {

					@Override
					public boolean accept(RSObject arg0) {
						return arg0.getID() == AutoFM.JADE_ROOT_CUT && AutoFM.vineTile != null 
							&& arg0.getLocation().equals(AutoFM.vineTile);
					}
					
				});
				if (cutRoot != null) {
					if (cutRoot.isOnScreen()) {
						AutoFM.state = "Collecting curly roots.";
						if (cutRoot.interact("collect")) {
							return (sleepTime(cutRoot.getLocation()));
						}
					} else {
						if (!getMyPlayer().isMoving()) {
							new TurnTo(cutRoot);
						}
						if (!cutRoot.isOnScreen()) {
							if (canWalk()) {
								if (walkTileMM(cutRoot.getLocation())) {
									return random(800, 900);
								}
							} else {
								return 300;
							}
						}
					}
				} else {
					final RSObject root = objects.getNearest(VINE_FILTER);
					if (root != null) {
						if (root.isOnScreen()) {
							AutoFM.state = "Chopping jade root";
							if (getMyPlayer().isMoving())
								return 500;
							for (int i = 0; i < 10; i++) {
								if (root.interact("chop")) {
									AutoFM.vineTile = root.getLocation();
									sleep(sleepTime(root.getLocation()), getMyPlayer().getAnimation() != -1);
									return 0;
								}
							}
						} else {
							if (!getMyPlayer().isMoving()) {
								new TurnTo(root);
							}
							if (!root.isOnScreen()) {
								AutoFM.state = "Walking to jade root.";
								if (distance() <= 5) {
									if (walkTileMM(root.getLocation())) {
										log(root);
										if (!AutoFM.running) {
											new Antiban();
											AutoFM.running = true;
										}
										return random(800, 900);
									}
								} else {
									return 300;
								}
							}
						}
					}
				}
			} else {
				if (!AutoFM.running) {
					new Antiban();
					AutoFM.running = true;
				}
				return 1000;
			}
			return 0;
		}
		
	}
	
	public class JadinkoBurningTask implements ValidTask {

		@Override
		public boolean validate() {
			return game.isLoggedIn() && inventory.isFull() && !AutoFM.BT.isRunning()
				&& inLair();
		}

		@Override
		public int loop() {
			final RSComponent comp = interfaces.getComponent(905, 14);
			if (comp != null && comp.isValid()) {
				AutoFM.state = "Clicking interface.";
				if (comp.doClick()) {
					if (!AutoFM.running) {
						new Antiban();
						running = true;
					}
					AutoFM.BT.reset();
					return 2000;
				}
			} else {
				final RSObject firePit = objects.getNearest(new Filter<RSObject>() {

					@Override
					public boolean accept(RSObject arg0) {
						return arg0.getID() == AutoFM.FIRE_PIT;
					}
					
				});
				if (firePit != null) {
					if (firePit.isOnScreen()) {
						AutoFM.state = "Adding roots to fire pit.";
						mouse.move(firePit.getModel().getCentralPoint());
						if (firePit.interact("add")) {
							if (!AutoFM.running) {
								new Antiban();
								AutoFM.running = true;
							}
							return sleepTime(firePit.getLocation());
						}
					} else {
						if (!getMyPlayer().isMoving()) {
							new TurnTo(firePit);
						}
						if (!firePit.isOnScreen()) {
							AutoFM.state = "Walking to fire pit";
							if (distance() <= 5) {
								if (walkTileMM(firePit.getLocation())) {
									log(firePit);
									if (!AutoFM.running) {
										new Antiban();
										AutoFM.running = true;
									}
									return random(800, 900);
								}
							} else {
								return 300;
							}
						}
					}
				} else {
					final RSObject unlitFire = objects.getNearest(new Filter<RSObject>() {

						@Override
						public boolean accept(RSObject arg0) {
							return arg0.getID() == AutoFM.DRY_PATCH + 1;
						}
						
					});
					if (unlitFire != null) {
						if (unlitFire.isOnScreen()) {
							AutoFM.state = "Lighting fire pit.";
							if (unlitFire.interact("light")) {
								if (!AutoFM.running) {
									new Antiban();
									AutoFM.running = true;
								}
								return sleepTime(unlitFire.getLocation());
							}
						}
					} else {
						final RSObject dryPatch = objects.getNearest(new Filter<RSObject>() {
	
							@Override
							public boolean accept(RSObject arg0) {
								return arg0.getID() == AutoFM.DRY_PATCH;
							}
							
						});
						if (dryPatch != null) {
							if (dryPatch.isOnScreen()) {
								AutoFM.state = "Adding roots to dry patch.";
								if (dryPatch.interact("add")) {
									if (!AutoFM.running) {
										new Antiban();
										AutoFM.running = true;
									}
									return sleepTime(dryPatch.getLocation());
								}
							} else {
								if (!getMyPlayer().isMoving()) {
									new TurnTo(dryPatch);
								}
								if (!dryPatch.isOnScreen()) {
									AutoFM.state = "Walking to dry patch.";
									if (distance() <= 5) {
										if (walkTileMM(dryPatch.getLocation())) {
											log(dryPatch);
											if (!AutoFM.running) {
												new Antiban();
												AutoFM.running = true;
											}
											return random(800, 900);
										}
									} else {
										return 300;
									}
								}
							}
						}
					}
				}
			}
			return 0;
		}
		
	}
	
	public class Antiban extends Thread {
		
		private Antiban() {
			final Thread t = new Thread(this);
			t.start();
		}
		
		private final Timer XP_TIMER = new Timer(60 * 60000);
		
		private long checkTime = 30 * 60000;
		
		private boolean antiban() {
			try {
				if (XP_TIMER.getRemaining() <= checkTime) {
					log("[Antiban] Checked gained exp at " + Timer.format(runTime()) + ".");
					if (game.getTab() != Tab.STATS) {
						if (game.openTab(Tab.STATS)) {
							sleep(200, 300);
						}
					}
					if (skills.doHover(Skills.INTERFACE_FIREMAKING)) {
						checkTime = random(30 * 60000, 60 * 60000);
						XP_TIMER.reset();
						sleep(3000, 3500);
						return true;
					}
				} else {
					final int x = random(0, 5);
					switch (x) {
					case 1:
						log("[Antiban] Moved camera at " + Timer.format(runTime()) + ".");
						camera.moveRandomly(random(400, 800));
						sleep(10000, 20000);
						return true;
					case 2:
						return true;
					default:
						log("[Antiban] Moved mouse at " + Timer.format(runTime()) + ".");
						mouse.moveSlightly();
						sleep(8000, 9500);
						return true;
					}
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		public void run() {
			try {
				if (antiban()) {
					sleep(random(20000, 90000));
				}
				AutoFM.running = false;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public class ImageLoader extends Thread {
		
		private ImageLoader() {
			Thread t = new Thread(this);
			t.start();
		}
		
		private Image getImage(String url) {
			try {
				return ImageIO.read(new URL(url));
			} catch (IOException e) {
				return null;
			}
		}
		
		@Override
		public void run() {
			img1 = getImage("http://desmond.imageshack.us/Himg838/scaled.php?server=838&filename=baset.jpg&res=landing");
		}
		
	}
	
	public static class Util {
		
		public static boolean checkVersion(double currentVersion, double latestVersion) {
			return  (currentVersion >= latestVersion);
		}
		
		public static double getCurrentVersion(String webUrl, String scriptName) {
			try {
				URL url = new URL(webUrl);
				BufferedReader x = new BufferedReader(new InputStreamReader(
						url.openStream()));
				String inputLine;
				while ((inputLine = x.readLine()) != null) {
					if (inputLine.contains(scriptName)) {
						return Double.parseDouble(x.readLine());
					}
				}
				x.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return -1D;
		}
		
	}
	
	public static class Browser {
		
		private static Desktop desk = null;
		
		public static void launch() {
			if (Desktop.isDesktopSupported()) {
				desk = Desktop.getDesktop();
			}
			if (desk != null) {
				URI uri = null;
				try {
					uri = new URI(SCRIPT_URL);
					desk.browse(uri);
				} catch (URISyntaxException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}

	}
	
	public class AutoFMGui extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public AutoFMGui() {
			initComponents();
		}

		private void bonfireBoxItemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				logCombo.setEnabled(true);
				jadBox.setSelected(false);
			} else {
				logCombo.setEnabled(false);
				jadBox.setSelected(true);
			}
		}

		private void jadBoxItemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				logCombo.setEnabled(false);
				bonfireBox.setSelected(false);
			} else {
				logCombo.setEnabled(true);
				bonfireBox.setSelected(true);
			}
		}
		
		private void addBonfireTasks() {
			AutoFM.logID = ((Log)logCombo.getSelectedItem()).getId();
			AutoFM.fireArea = generateArea();
			AutoFM.BT = new Timer(3000);
			AutoFM.tasks.add(new FireSpiritTask());
			AutoFM.tasks.add(new FireTask());
			AutoFM.tasks.add(new BankingTask());
		}
		
		private void addJadinkoTasks() {
			AutoFM.jadinko = true;
			AutoFM.logID = 1;
			AutoFM.BT = new Timer(2500);
			AutoFM.tasks.add(new JadinkoHoleTask());
			AutoFM.tasks.add(new JadinkoCombatHandler());
			AutoFM.tasks.add(new JadinkoRewardsTask());
			AutoFM.tasks.add(new JadinkoBankingTask());
			AutoFM.tasks.add(new JadinkoVineTask());
			AutoFM.tasks.add(new JadinkoChoppingTask());
			AutoFM.tasks.add(new JadinkoBurningTask());
		}

		private void okButtonActionPerformed(ActionEvent e) {
			if (bonfireBox.isSelected()) {
				addBonfireTasks();
			} else if (jadBox.isSelected()) {
				addJadinkoTasks();
			}
			dispose();
		}

		private void initComponents() {
			dialogPane = new JPanel();
			contentPanel = new JPanel();
			label1 = new JLabel();
			label2 = new JLabel();
			bonfireBox = new JCheckBox();
			jadBox = new JCheckBox();
			logCombo = new JComboBox();
			buttonBar = new JPanel();
			okButton = new JButton();

			//======== this ========
			setTitle("AutoFM Gui");
			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());

			//======== dialogPane ========
			{
				dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
				dialogPane.setLayout(new BorderLayout());

				//======== contentPanel ========
				{

					//---- label1 ----
					label1.setText("Welcome to AutoFM!");
					label1.setFont(new Font("Tahoma", Font.BOLD, 20));

					//---- label2 ----
					label2.setText("Please select a mode.");

					//---- bonfireBox ----
					bonfireBox.setText("Bonfires");
					bonfireBox.setSelected(true);
					bonfireBox.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							bonfireBoxItemStateChanged(e);
						}
					});

					//---- jadBox ----
					jadBox.setText("Curly roots (Jadinkos lair)");
					jadBox.addItemListener(new ItemListener() {
						@Override
						public void itemStateChanged(ItemEvent e) {
							jadBoxItemStateChanged(e);
						}
					});

					//---- logCombo ----
					logCombo.setModel(new DefaultComboBoxModel(Log.values()));
					logCombo.setEnabled(false);

					GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
					contentPanel.setLayout(contentPanelLayout);
					contentPanelLayout.setHorizontalGroup(
						contentPanelLayout.createParallelGroup()
							.addGroup(contentPanelLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(contentPanelLayout.createParallelGroup()
									.addGroup(contentPanelLayout.createSequentialGroup()
										.addComponent(bonfireBox)
										.addGap(29, 29, 29)
										.addComponent(jadBox))
									.addComponent(label1)
									.addComponent(label2)
									.addComponent(logCombo, GroupLayout.PREFERRED_SIZE, 102, GroupLayout.PREFERRED_SIZE))
								.addContainerGap(39, Short.MAX_VALUE))
					);
					contentPanelLayout.setVerticalGroup(
						contentPanelLayout.createParallelGroup()
							.addGroup(contentPanelLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(label1)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label2)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contentPanelLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
									.addComponent(bonfireBox)
									.addComponent(jadBox))
								.addGap(18, 18, 18)
								.addComponent(logCombo, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(36, Short.MAX_VALUE))
					);
				}
				dialogPane.add(contentPanel, BorderLayout.CENTER);

				//======== buttonBar ========
				{
					buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
					buttonBar.setLayout(new GridBagLayout());
					((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 80};
					((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0};

					//---- okButton ----
					okButton.setText("OK");
					okButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							okButtonActionPerformed(e);
						}
					});
					buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				}
				dialogPane.add(buttonBar, BorderLayout.SOUTH);
			}
			contentPane.add(dialogPane, BorderLayout.CENTER);
			setSize(330, 250);
			setLocationRelativeTo(getOwner());
			setVisible(true);
		}

		private JPanel dialogPane;
		private JPanel contentPanel;
		private JLabel label1;
		private JLabel label2;
		private JCheckBox bonfireBox;
		private JCheckBox jadBox;
		private JComboBox logCombo;
		private JPanel buttonBar;
		private JButton okButton;
	}
	
	public class FMUpdateGui extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public FMUpdateGui() {
			initComponents();
		}

		private void okButtonActionPerformed(ActionEvent e) {
			Browser.launch();
			dispose();
		}

		private void cancelButtonActionPerformed(ActionEvent e) {
			dispose();
		}

		private void initComponents() {
			dialogPane = new JPanel();
			contentPanel = new JPanel();
			label1 = new JLabel();
			label2 = new JLabel();
			buttonBar = new JPanel();
			okButton = new JButton();
			cancelButton = new JButton();

			//======== this ========
			setTitle("AutoFM update alert");
			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());

			//======== dialogPane ========
			{
				dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
				dialogPane.setLayout(new BorderLayout());

				//======== contentPanel ========
				{

					//---- label1 ----
					label1.setText("This version of AutoFM is outdated!");
					label1.setForeground(Color.RED);
					label1.setFont(label1.getFont().deriveFont(label1.getFont().getStyle() | Font.BOLD));

					//---- label2 ----
					label2.setText("Click ok to open the Rarebot thread.");
					label2.setFont(label2.getFont().deriveFont(label2.getFont().getStyle() | Font.BOLD));

					GroupLayout contentPanelLayout = new GroupLayout(contentPanel);
					contentPanel.setLayout(contentPanelLayout);
					contentPanelLayout.setHorizontalGroup(
						contentPanelLayout.createParallelGroup()
							.addGroup(contentPanelLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(contentPanelLayout.createParallelGroup()
									.addComponent(label1)
									.addComponent(label2))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);
					contentPanelLayout.setVerticalGroup(
						contentPanelLayout.createParallelGroup()
							.addGroup(contentPanelLayout.createSequentialGroup()
								.addContainerGap()
								.addComponent(label1)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(label2)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					);
				}
				dialogPane.add(contentPanel, BorderLayout.CENTER);

				//======== buttonBar ========
				{
					buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
					buttonBar.setLayout(new GridBagLayout());
					((GridBagLayout)buttonBar.getLayout()).columnWidths = new int[] {0, 85, 80};
					((GridBagLayout)buttonBar.getLayout()).columnWeights = new double[] {1.0, 0.0, 0.0};

					//---- okButton ----
					okButton.setText("OK");
					okButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							okButtonActionPerformed(e);
						}
					});
					buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 5), 0, 0));

					//---- cancelButton ----
					cancelButton.setText("Cancel");
					cancelButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							cancelButtonActionPerformed(e);
						}
					});
					buttonBar.add(cancelButton, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER, GridBagConstraints.BOTH,
						new Insets(0, 0, 0, 0), 0, 0));
				}
				dialogPane.add(buttonBar, BorderLayout.SOUTH);
			}
			contentPane.add(dialogPane, BorderLayout.CENTER);
			setSize(380, 150);
			setLocationRelativeTo(getOwner());
			setVisible(true);
		}

		private JPanel dialogPane;
		private JPanel contentPanel;
		private JLabel label1;
		private JLabel label2;
		private JPanel buttonBar;
		private JButton okButton;
		private JButton cancelButton;
	}
	
}
