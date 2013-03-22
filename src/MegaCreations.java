import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;
import javax.swing.*;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPlayer;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSWeb;

@ScriptManifest(authors = { "MegaAlgos" }, version = .1 , keywords = ("Mini-games"), description = "Stealing Creations", name = "MegaCreations[BETA]")
public class MegaCreations extends Script implements PaintListener, MessageListener,MouseListener {

  private long startTime;
	private Image arrow;
	private boolean hidePaint;
	private boolean start;
	private Rectangle r;
	int blueTeam = 39508, redTeam = 39509, teamPicked;
	boolean pickedTeam = false, gathering = false, depositing = false;
	String status = "Starting up!!!";

	// Script Info for paint and proggies...
	public double getVersion() {
		return (.1);
	}

	public String getName() {
		return ("MegaCreations[BETA]");
	}

	public String getAuthor() {
		return ("MegaAlgos");
	}

	public boolean onStart() {
		log("MegaCreations, stealing creations");

		try {
			arrow = ImageIO.read(new URL("http://megascripts.comyr.com/Arrow.png").openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mouse.setSpeed(6);
		startTime = System.currentTimeMillis();
		return true;
	}

	// Script's graphics
	public void onRepaint(Graphics g) {
		long runTime = System.currentTimeMillis() - startTime;
		long TotalSecs = runTime / 1000;
		long TotalMins = TotalSecs / 60;
		long TotalHours = TotalMins / 60;
		int seconds = (int) TotalSecs % 60;
		int minutes = (int) TotalMins % 60;
		int hours = (int) TotalHours % 60;

		StringBuilder b = new StringBuilder();

		if (hours < 10)
			b.append('0');
		b.append(hours);
		b.append(':');
		if (minutes < 10)
			b.append('0');
		b.append(minutes);
		b.append(':');
		if (seconds < 10)
			b.append('0');
		b.append(seconds);

		if (game.isLoggedIn()) {

			if (arrow != null) {
				g.drawImage(arrow, mouse.getLocation().x - 9, mouse.getLocation().y - 2, null);
			}
			if (!hidePaint) {
				//				g.setColor(new Color(153, 0, 0, 225));
				//				g.fillRoundRect(551, 217, 184, 242, 16, 16);
				//
				//				g.setColor(new Color(102, 102, 102));
				//				r = new Rectangle(596, 415, 88, 36);
				//				g.fillRect(596, 415, 88, 36);
				//
				//				g.setFont(new Font("Verdana", 1, 16));
				//				g.setColor(new Color(167, 91, 3));
				//				g.drawString(getName() + " " + getVersion(), 555, 251);
				//
				//				g.setColor(Color.black);
				//				g.setFont(new Font("Verdana", 1, 18));
				//				g.drawString("Hide", 611, 443);
				//
				//				int x = 550;
				//				int y = 254;
				//				g.setFont(new Font("Verdana", 0, 12));
				//				g.drawString("Ran for: " + b.toString(), x, y += 23);
				//				g.drawString("Made: " + iCount + " " + pName, x, y += 15);
				//				g.drawString("Making: " + addCommas((int)roundToTenth(work), false) + " " + pName +"/hour.", x,
				//						y += 15);
				//				g.drawString("Gained: " + addCommas((cXP - sXP), false) + " xp", x, y += 15);
				//				g.drawString("Gaining: " + addCommas((int)roundToTenth(hXP), false) + " an hour", x, y += 15);
				//				g.drawString("LVLs gained: " + (cLVL - sLVL), x, y += 15);
				//				g.drawString("Current LVL: " + cLVL , x, y += 15);
				//				g.drawString("Time till n/lvl: " + roundToTenth(TTNL) + " min(s)", x, y += 15);
				//				g.setFont(new Font("Verdana", 0, 10));
				//				g.drawString("Status: " + status, x, y += 15);
			} else {
				//				g.setColor(new Color(102, 102, 102, 175));
				//				r = new Rectangle(596, 415, 88, 36);
				//				g.fillRect(596, 415, 88, 36);
				//
				//				g.setColor(Color.white);
				//				g.setFont(new Font("Verdana", 1, 18));
				//				g.drawString("Show", 611, 443);
			}
		}
	}

	public boolean waitForInvChange(int item, int timer) {
		int count;
		if (item == 0) {
			count = inventory.getCount();
		} else {
			count = inventory.getCount(item);
		}
		for (int w = 0; w < timer; w++) {
			if (item == 0) {
				if (inventory.getCount() != count) {
					return true;
				}
			} else {
				if (inventory.getCount(item) != count) {
					return true;
				}
			}
			if(random(0,100000) == 500) {
				antiBan();
			}
			sleep(1);
		}
		return false;
	}

	public boolean waitForArena(int timer, RSComponent r) {
		for (int w = 0; w < timer; w++) {
			if (!r.isValid()) {
				return true;
			}
			sleep(1);
		}
		return false;
	}

	public boolean areaContains(RSTile nw, RSTile se) {
		RSTile pLoc = getMyPlayer().getLocation();
		if (pLoc.getY() <= nw.getY() && pLoc.getY() >= se.getY()
				&& pLoc.getX() >= nw.getX() && pLoc.getX() <= se.getX()) {
			return true;
		}
		return false;
	}

	public boolean waitForMoving(int timer) {
		for (int w = 0; w < timer; w++) {
			if (!getMyPlayer().isMoving()) {
				return true;
			}
			sleep(1);
		}
		return false;
	}


	public int pickTeam() {
		if (random(0,2) == 1) {
			return blueTeam;
		} else {
			return redTeam;
		}
	}

	public int getState() {
		if (areaContains(new RSTile(2966, 9707), new RSTile(2970, 9692))) {
			status = "Picking team";
			return 1;
		}
		if (interfaces.get(804).isValid()) {
			status = "Waiting";
			return 2;
		}
		RSObject table = objects.getNearest(39533);
		if (table != null) {
			RSTile tLoc = table.getLocation();
			if (areaContains(new RSTile(tLoc.getX() - 4, tLoc.getY() + 3), 
					new RSTile(tLoc.getX() + 3, tLoc.getY() - 4)) && status != "Depositing") {
				status = "Getting out start area";
				return 3;
			}
		}
		if (interfaces.get(810).isValid()) {
			status = "Walking to pick a team";
			log("walking to pick a team");
			return 4;
		}
		return 0;
	}

	@Override
	public int loop() {
		
		if (getMyPlayer().isMoving()) {
			waitForMoving(4000);
			return 0;
		}

		if(!getMyPlayer().isIdle()) {
			return 50;
		}
		int team = getMyPlayer().getTeam();
		switch (getState()) {
		case 0:
			if (gathering) {
				if (inventory.isFull()) {
					gathering = false;
				}
				return 50;
			}

			status = "Working";

			if (!inventory.isFull()) {
				RSObject clay = objects.getNearest(39548);
				if (clay != null) {
					if (clay.isOnScreen()) {
						status = "Getting clay";
						clay.doClick();
						if (waitForInvChange(0, 3000)) {
							gathering = true;
						}
					} else {
						walking.walkTileMM(clay.getLocation());
					}
				}
			} else {
				status = "Depositing";
				RSObject table = objects.getNearest(39533);
				if (table != null) {
					RSTile tLoc = table.getLocation();
					if (areaContains(new RSTile(tLoc.getX() - 4, tLoc.getY() + 3), 
							new RSTile(tLoc.getX() + 3, tLoc.getY() - 4))) {
						table.doClick();
						status = "Done";
						sleep(1000);
						break;
					}
				}
				if (team == 2) {
					RSObject door = objects.getNearest(39767);
					if (door != null) {
						if (door.isOnScreen()) {
							door.doClick();
							sleep(1000);
						} else {
							walking.walkTileMM(door.getLocation());
						}
					}				
				} else {
					RSObject door = objects.getNearest(39766);
					if (door != null) {
						if (door.isOnScreen()) {
							door.doClick();
							sleep(1000);
						} else {
							walking.walkTileMM(door.getLocation());
						}
					}
				}
			}
			break;

		case 1:
			if (!pickedTeam) {
				teamPicked = pickTeam();
				pickedTeam = true;
			}
			RSObject c = objects.getNearest(teamPicked);
			if (c != null) {
				if (c.isOnScreen()) {
					c.doClick();
					sleep(1000);
				} else {
					walking.walkTileMM(c.getLocation());
				}
			}
			break;

		case 2:
			RSComponent wait = interfaces.getComponent(804, 2);
			if (wait.containsText("1")) {
				waitForArena(60000, wait);
			} else {
				waitForArena(120000, wait);
			}
			break;

		case 3:
			if (team == 2) {
				RSObject door = objects.getNearest(39767);
				if (door != null) {
					door.doClick();
					sleep(500);
				}
			} else {
				RSObject door = objects.getNearest(39766);
				if (door != null) {
					door.doClick();
					sleep(500);
				}
			}
			break;
			
		case 4:
			walking.walkTileMM(new RSTile(2968, 9699));
			sleep(1000);
			break;
		}

		return random(300, 500);
	}


	// Rounds a double to the nearest tenth
	public double roundToTenth(double d) {
		DecimalFormat twoDForm = new DecimalFormat("#.#");
		twoDForm = new DecimalFormat("#.#");
		return Double.valueOf(twoDForm.format(d));
	}

	public String addCommas(int d, boolean money) {
		DecimalFormat twoDForm = new DecimalFormat("###,###");
		if(money) {
			twoDForm = new DecimalFormat("$###,###");
			return twoDForm.format(d);
		} 
		return twoDForm.format(d);
	}

	// Checks for the recent version
	public double getOVersion() {
		try {
			URL url = new URL("http://megascripts.comyr.com/MegaBlacksmith/Version");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new BufferedInputStream(url.openConnection()
							.getInputStream())));
			double ver = Double.parseDouble(br.readLine().trim());
			br.close();
			return ver;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public int antiBan() {
		Point m;
		int x = random(0, 750);
		int y = random(0, 500);
		int xx = random(710, 554);
		int yy = random(444, 230);
		int screenx = random(1, 510);
		int screeny = random(1, 450);

		int gamble = random(0, 22);

		final long start = System.currentTimeMillis();
		final RSPlayer myPlayer = getMyPlayer();

		int anim = -1;
		while (System.currentTimeMillis() - start > 2000) {
			if ((anim = myPlayer.getAnimation()) != -1) {
				break;
			}
			sleep(15);
		}

		switch (gamble) {
		case 1:

			break;
		case 2:
			mouse.move(x, y);

			while (System.currentTimeMillis() - start > 2000) {

				if ((anim = myPlayer.getAnimation()) != -1) {
					break;
				}

			}


			break;
		case 3:
			game.openTab(Tab.INVENTORY);

			break;
		case 4:
			if (getMyPlayer().isMoving()) {
				break;
			}
		case 5:
			mouse.move(x, y);
			int checkTime = 0;
			long lastCheck = 0;
			if (System.currentTimeMillis() - lastCheck >= checkTime) {
				lastCheck = System.currentTimeMillis();
				checkTime = random(60000, 180000);
				break;
			}
		case 6:
			if (game.getTab() != Tab.STATS) {
				game.openTab(Tab.STATS);
				mouse.move(xx, yy);
				sleep(1500,3000);
				game.openTab(Tab.INVENTORY);
				break;
			}
		case 8:
			mouse.move(screenx, screeny);
			break;
		case 9:
			mouse.move(screenx, screeny);
			break;
		case 10:
			game.openTab(game.getRandomTab());
			break;
		case 11:
			mouse.move(x, y);
			break;
		case 13:
			mouse.move(x, y);
			break;
		case 12:
			mouse.move(x, y);
			break;
		case 14:
			m = mouse.getLocation();
			mouse.move(m, 20, 20);	
			break;
		case 15:
			m = mouse.getLocation();
			mouse.move(m, 20, 20);			
			break;
		case 16:
			m = mouse.getLocation();
			mouse.move(m, 20, 20);
			break;
		case 17:
			m = mouse.getLocation();
			mouse.move(m, 20, 20);
			break;
		case 19:
			mouse.move(x, y);
			break;
		case 20:
			mouse.move(x, y);
			break;
		case 21:
			mouse.move(x, y);
			break;

		}

		return 100;
	}

	// JButton startButton;
	// JComboBox whereandwhat;
	// JLabel text;
	// JLabel text2;
	// String[] list = { "Smelt", "Smith"};
	// String[] list2 = { "Bronze", "Iron", "Steel" };

	public class MegaGUI extends JFrame implements ItemListener, ActionListener {

		/**
		 * Initialize the contents of the
		 */
		JLabel text;
		JLabel lblWhatItem;
		JLabel lblWhatBar;
		JComboBox comboBox;
		JComboBox comboBox_1;
		JComboBox comboBox_2;
		JButton btnStart;

		public MegaGUI() {
			setTitle("MegaBlacksmith");
			setSize(350, 239);
			setLocationRelativeTo(null);
			getContentPane().setLayout(null);

			text = new JLabel("Smelt or Smith?");
			text.setBounds(25, 11, 91, 20);
			getContentPane().add(text);

			comboBox = new JComboBox();
			comboBox.addItemListener((ItemListener) this);
			comboBox.setModel(new DefaultComboBoxModel(new String[] { "Smelt",
			"Smith" }));
			comboBox.setMaximumRowCount(2);
			comboBox.setBounds(122, 11, 73, 20);
			getContentPane().add(comboBox);

			lblWhatBar = new JLabel("What bar?");
			lblWhatBar.setBounds(25, 56, 73, 14);
			getContentPane().add(lblWhatBar);

			comboBox_1 = new JComboBox();
			comboBox_1.setModel(new DefaultComboBoxModel(new String[] {
					"Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune" }));
			comboBox_1.setBounds(122, 53, 73, 20);
			getContentPane().add(comboBox_1);

			btnStart = new JButton("Start");
			btnStart.setBounds(122, 145, 89, 23);
			btnStart.addActionListener(this);
			getContentPane().add(btnStart);

			lblWhatItem = new JLabel("What item?");
			lblWhatItem.setBounds(25, 100, 73, 14);
			getContentPane().add(lblWhatItem);
			lblWhatItem.setVisible(false);

			comboBox_2 = new JComboBox();
			comboBox_2.setModel(new DefaultComboBoxModel(new String[] {
					"Dagger", "Hatchet", "Mace", "Bolts", "Med helm",
					"Dart tips", "Sword", "Nails", "Arrow tips",
					"Scimitar", "Limbs", "Longsword", "Throwing knives",
					"Full helm", "Square shield", "Warhammer", "Battleaxe",
					"Chainbody", "Kiteshield", "Claws", "2h sword",
					"Platelegs", "Plateskirt", "Platebody" }));
			comboBox_2.setBounds(122, 97, 95, 20);
			getContentPane().add(comboBox_2);
			comboBox_2.setVisible(false);
		}

		public void itemStateChanged(ItemEvent e) {
			if (e.getItem().equals("Smith")) {
				comboBox_1.setModel(new DefaultComboBoxModel(new String[] {
						"Bronze", "Iron", "Steel", "Mithril", "Adamant", "Rune" }));
				lblWhatItem.setVisible(true);
				comboBox_2.setVisible(true);
			} else {
				comboBox_1.setModel(new DefaultComboBoxModel(new String[] {
						"Bronze", "Iron", "Silver", "Steel", "Gold", "Mithril", "Adamant", "Rune" }));
				lblWhatItem.setVisible(false);
				comboBox_2.setVisible(false);
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().contains("Start")) {
				String doingString = comboBox.getSelectedItem().toString();
				String barString = comboBox_1.getSelectedItem().toString();
				String itemString = comboBox_2.getSelectedItem().toString();

				dispose();
				setVisible(false);
				start = true;
			}
		}

	}

	JButton oB;
	JButton gN;
	JLabel oL;

	public class VersionGUI extends JFrame implements ActionListener {

		public VersionGUI() {
			setTitle("OUTDATED");

			oB = new JButton("Okay");
			oL = new JLabel("OUTDATED");
			gN = new JButton("Get new version");

			setLayout(null);
			setResizable(false);
			setSize(150, 185);
			setLocationRelativeTo(null);

			oB.setBounds(20, 35, 65, 25);
			gN.setBounds(20, 85, 115, 25);
			oB.addActionListener(this);
			gN.addActionListener(this);
			oL.setBounds(20, 5, 182, 21);

			add(oL);
			add(gN);
			add(oB);

			setVisible(true);
		}

		public void getNewVersion() {
			File directory = getCacheDirectory();
			File newDirectory = new File(new File(new File(
					directory.getParent()).getParent()).getParent()
					+ File.separator
					+ "Scripts"
					+ File.separator
					+ "Sources"
					+ File.separator + "MegaBlacksmith.java");

			BufferedInputStream in;
			try {
				in = new BufferedInputStream(
						new URL(
								"http://megascripts.comyr.com/MegaBlacksmith/MegaBlacksmith.java")
						.openStream());
				FileOutputStream fos = new FileOutputStream(newDirectory);
				BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
				byte[] data = new byte[1024];
				int x = 0;
				while ((x = in.read(data, 0, 1024)) >= 0) {
					bout.write(data, 0, x);
				}
				bout.close();
				in.close();
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().contains("Okay")) {
				dispose();
				setVisible(false);
			}
			if (e.getActionCommand().contains("Get new version")) {
				getNewVersion();
				sleep(1000);
				oL.setText("Recompile the script");
				repaint();
				stopScript();
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		if (r.contains(arg0.getPoint())) {
			if (hidePaint) {
				hidePaint = false;
			} else {
				hidePaint = true;
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void messageReceived(MessageEvent m) {
		if(m.getMessage().contains("died")) {
			RSObject door = objects.getNearest(39805);
			if(door != null)
			door.doClick();
			sleep(1500);
		}
		if(m.getMessage().contains("You take the last")) {
			gathering = false;
		}
	}
}
