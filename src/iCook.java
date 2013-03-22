import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Players;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPath;
import com.rarebot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Robert G" }, name = "iCook", version = 1.01, description = "Cooks any food in Rogues den or Al kharid.", website = "http://www.powerbot.org/vb/showthread.php?t=803023")
public class iCook extends Script implements PaintListener, MessageListener,
  	MouseListener {

	private int foodID;
	private static final int em = 14707;
	private static final int[] otherNpcs = { 2266, 2270 };
	private static final int[] rangeID = { 2732, 25730 };
	private static final RSTile fireTile = new RSTile(3028, 4958);
	private static final RSTile rangeTile = new RSTile(3273, 3180);
	private static final RSTile bankTile = new RSTile(3270, 3167);
	private static final RSArea bank_area = new RSArea(new RSTile(3264, 3161, 0),
			new RSTile(3272, 3173, 0));
	private RSTile fire = new RSTile(3029, 4959);
	private RSTile range = new RSTile(3271, 3181);
	private Timer timer;
	private int startLVL, currentLVL, gainedLVL, nextLVL, startXP, currentXP,
			gainedXP, xpPH, foodTNL, TNL;
	private double foodXP;
	private long timeTNL;
	private int cooked = 0, burnt = 0;
	private int cookedPH;
	private long startTime;
	private long runTime;
	private static final Rectangle close = new Rectangle(6, 344, 506, 131);
	private Point p;
	private boolean roguesDen = false;
	private boolean hide = false;
	private boolean afk = false;
	private boolean start = false;
	private boolean run = false, timerReset;
	private iCookGui g = new iCookGui();

	private boolean atBank() {
		return bank_area.contains(getMyPlayer().getLocation());
	}

	private boolean atRange() {
		final RSObject range = objects.getNearest(rangeID);
		if (range != null) {
			if (range.isOnScreen()) {
				return true;
			}
		}
		return false;
	}

	private boolean atRoguesDen() {
		final RSNPC a = npcs.getNearest(otherNpcs);
		final RSNPC b = npcs.getNearest(em);
		if (a != null || b != null) {
			roguesDen = true;
			return true;
		}
		return false;
	}

	private void rogueBank() {
		final RSNPC m = npcs.getNearest(em);
		if (m != null) {
			if (m.isOnScreen()) {
			mouse.hop(m.getScreenLocation());
			if (m.interact("Bank " + m.getName())) {
				sleep(1200, 1400);
			}
		} else {
			if (walking.walkTileMM(m.getLocation())) {
				sleep(900, 1000);	
				}
			}
		}
	}

	private void bank() {
		if (!bank.isOpen()) {
			if (!roguesDen) {
				if (bank.open()) {
					sleep(1000, 1200);
				}
			} else {
				rogueBank();
			}
		} else {
			if (bank.isOpen()) {
				if (inventory.getCount() > 0) {
						if (bank.depositAll()) {
							sleep(100, 200);
						}
					}
				if (bank.getCount(foodID) > 0) {
					if (!inventory.contains(foodID)) {
						if (bank.withdraw(foodID, 0)) {
							sleep(600, 700);
						}
						bank.close();
					}
				} else {
					log.severe("Ran out of raw food!, stopping script.");
					stopScript();
				}
			}
		}
	}

	private void cookFire() {
		final RSItem food = inventory.getItem(foodID);
		if (!atRange()) {
			if (walk(fireTile)) {
				sleep(1000, 1200);
			}
		} else {
			if (!inventory.isItemSelected()) {
				mouse.move(food.getComponent().getCenter());
				if (food.interact("Use")) {
					sleep(100, 200);
				}
			} else {
				if (tiles.interact(fire,"Use " + food.getName() + " -> Fire")) {
					waitFor();
				}
			}
		}
	}

	private void cook() {
		final RSItem food = inventory.getItem(foodID);
		final RSObject r = objects.getNearest(rangeID);
		if (r != null) {
			if (!r.isOnScreen()) {
				if (walk(rangeTile)) {
					camera.turnTo(range);
				}
			} else {
				if (!inventory.isItemSelected()) {
					mouse.move(food.getComponent().getCenter());
					if (food.interact("Use " + food.getName())) {
						sleep(random(100, 200));
					}
				} else {
					if (tiles.interact(range, "Use " + food.getName() + " -> " + r.getName()))
						waitFor();
				}
			}
		}
	}

	public void antiban() {

		int b = random(0, 90);
		switch (b) {
		case 1:
			if (random(0, 90) == 1) {
				mouse.moveSlightly();
				sleep(2000, 6000);
				mouse.moveRandomly(1500, 3500);
			}
			break;
		case 2:
			if (random(0, 90) == 2) {
				camera.setAngle(random(30, 130));
				sleep(400, 800);

			}
			break;
		case 3:
			if (random(0, 90) == 3) {
				camera.setAngle(random(150, 270));
				sleep(1200, 1500);
			}
			break;
		case 4:
			if (random(0, 90) == 4) {
				game.openTab(Tab.STATS);
				skills.doHover(Skills.INTERFACE_COOKING);
				sleep(2100, 3400);
			}
			break;
		case 5:
			if (random(0, 90) == 5) {
				game.openTab(game.getRandomTab());
				sleep(1500, 2200);
			}
			break;
		case 6:
			try {
				if (random(0, 90) == 6) {
					mouse.setSpeed(random(12, 18));
					players.getNearest(Players.ALL_FILTER).hover();
					mouse.click(false);
					sleep(1400, 1800);
					mouse.moveRandomly(150, 350);
					sleep(2200, 2400);
				}
			} catch (Exception e) {
			}
			break;
		case 7:
			if (random(0, 30) == 7) {
				mouse.moveOffScreen();
				afk = true;
				sleep(6000, 14000);
				afk = false;
			}
			break;
		default:
			break;
		}
	}

	private boolean walk(RSTile tile) {
		final int dist = calc.distanceTo(walking.getDestination());
		final RSPath path = walking.getPath(tile);
		if (dist == -1 || dist <= 5) {
			return path.traverse();
		} else {
			sleep(500);
		}
		return false;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		p = e.getPoint();
		if (close.contains(p) && !hide) {
			hide = true;
		} else if (close.contains(p) && hide) {
			hide = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}
	
	private void waitFor() {
		long start = System.currentTimeMillis();
		while (System.currentTimeMillis() - start < 3000) {
			sleep(50);
			if (interfaces.getComponent(905, 14).isValid())break;
		}
	}

	@Override
	public int loop() {
		if (!game.isLoggedIn() || game.isWelcomeScreen() || game.isLoading()
				|| game.inRandom()) {
			sleep(1000);
		}
		mouse.setSpeed(random(6, 7));
		if (roguesDen) {
			if (!afk) {
				if (timerReset) {
					timer.reset();
					timerReset = false;
					if (random(0, 5) == 3) {
						antiban();
					}
				}
				if (inventory.contains(foodID)) {
					if (getMyPlayer().getAnimation() == -1
							&& timer.getRemaining() <= 500) {
						if (!interfaces.getComponent(905, 14).isValid()) {
							cookFire();
						} else {
							if (interfaces.getComponent(905, 14).doClick()) {
								sleep(1200, 1400);

							}
						}
					}
				} else {
					bank();
				}
			} else {
				sleep(1000);
			}
		} else {
			if (!afk) {
				if (timerReset) {
					timer.reset();
					timerReset = false;
					if (random(0, 5) == 3) {
						antiban();
					}
				}
				if (inventory.contains(foodID)) {
					if (!atRange()) {
						if (walk(rangeTile)) {
							camera.setPitch(true);
							camera.setCompass('w');
						}
					} else {
						if (getMyPlayer().getAnimation() == -1 && timer.getRemaining() <= 500) {
							if (!interfaces.getComponent(905, 14).isValid()) {
								cook();
							} else {
								if (interfaces.getComponent(905, 14).doClick()) {
									sleep(1200, 1400);
								}
							}
						} else {
							sleep(1000);
						}
					}
				} else {
					if (atBank()) {
						bank();
					} else {
						if (walk(bankTile)) {
						camera.setPitch(true);
						camera.setCompass('s');
						sleep(1000, 1200);
						}
					}
				}
			}
		}
		return 500;
	}

	@Override
	public boolean onStart() {
		log("Welcome to Robert G's iCook!");
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					g.setVisible(true);
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		while (g.isVisible()) {
			sleep(100);
		}
		startLVL = skills.getCurrentLevel(Skills.COOKING);
		startXP = skills.getCurrentExp(Skills.COOKING);
		if (atRoguesDen()) {
			log("Cooking in Rogues Den");
			roguesDen = true;
		} else {
			log("Cooking in Al kharid");
		}
		startTime = System.currentTimeMillis();
		timer = new Timer(4000);
		new loader();
		return start;

	}

	@Override
	public void onFinish() {
		run = false;
		env.saveScreenshot(true);
		log("Thank you for using Robert G's iCook!");
		log("You gained " + gainedXP + " cooking xp || " + gainedLVL
				+ " cooking levels.");
	}

	private String formatTime(final long time) {
		final int sec = (int) (time / 1000), h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
		return (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":"
				+ (s < 10 ? "0" + s : s);
	}
	
	@Override
	public void messageReceived(MessageEvent e) {
		String x = e.getMessage().toLowerCase();
			if (x.contains("burn")) {
				burnt++;
				timerReset = true;
			}
			if (x.contains("cook")) {
				cooked++;
				timerReset = true;
			}
			if (x.contains("roast")) {
				cooked++;
				timerReset = true;
			}
			if (x.contains("bake")) {
				cooked++;
				timerReset = true;
			}

	}

	/**
	 * @param foodTNL the foodTNL to set
	 */
	public void setFoodTNL(int foodTNL) {
		this.foodTNL = foodTNL;
	}

	/**
	 * @return the foodTNL
	 */
	public int getFoodTNL() {
		return foodTNL;
	}

	/**
	 * @param currentXP the currentXP to set
	 */
	public void setCurrentXP(int currentXP) {
		this.currentXP = currentXP;
	}

	/**
	 * @return the currentXP
	 */
	public int getCurrentXP() {
		return currentXP;
	}

	private Image getImage(String url) {
		try {
			return ImageIO.read(new URL(url));
		} catch (IOException e) {
			return null;
		}
	}
	
	public class loader extends Thread {
		
		private loader() {
			Thread t = new Thread(this);
			t.start();
		}
		
		@Override
		public void run() {
			img1 = getImage("http://img64.imageshack.us/img64/8079/cooltext525966506.png");
			img2 = getImage("http://i298.photobucket.com/albums/mm280/xxxkrodukyxxx/cooking_cape.png");

		}
		
	}

	private final Color color1 = new Color(255, 255, 255, 0);
	private final Color color2 = new Color(0, 0, 0);
	private final Color color4 = new Color(204, 204, 204, 100);

	private final BasicStroke stroke1 = new BasicStroke(1);

	private final Font font1 = new Font("Calibri", 1, 17);
	private static NumberFormat format = NumberFormat.getInstance();
	private Image img1;
	private Image img2;

	@Override
	@SuppressWarnings("static-access")
	public void onRepaint(Graphics g1) {
		((Graphics2D) g1).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		((Graphics2D) g1).setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		((Graphics2D) g1).setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		Graphics2D g = (Graphics2D) g1;
		if (game.isLoggedIn()) {
			if (!hide) {
				g.drawImage(img1, -2, 333, null);
				g.drawImage(img2, 400, 356, null);
				g.setFont(font1);
				g.setColor(color4);
				g.drawString("Run Time: " + formatTime(runTime), 20, 370);
				g.drawString("Start Level: " + startLVL + " || Current Level: "
						+ currentLVL + " || Levels Gained: " + gainedLVL, 20,
						390);
				g.drawString("Food Cooked: " + format.format(cooked) + " || Food Burnt: "
						+ format.format(burnt) + " || Food Ph: " + format.format(cookedPH), 20, 430);
				g.drawString("Xp Gained: " + format.format(gainedXP) + " || Xp Ph: " + format.format(xpPH)
						+ " || Time Tnl: " + formatTime(timeTNL), 20, 410);

				// percent bar
				int percent = skills.getPercentToNextLevel(skills.COOKING);
				int specialColor = percent * 255 / 100;
				int specialColor2 = 255 - specialColor;
				int length = (percent * 477) / 100;
				g.setColor(color1);
				g.fillRect(20, 440, 477, 20);
				g.setColor(color2);
				g.setStroke(stroke1);
				g.drawRect(20, 440, 477, 20);
				g.setColor(new Color(+specialColor2, +specialColor, 0, 100));
				g.fillRect(20, 440, +length, 20);
				g.setColor(color2);
				g.drawRect(20, 440, +length, 20);
				g.setColor(new Color(0, 0, 0));
				g.drawString(percent + "% To " + nextLVL, 215, 456);

				if (!afk) {
					g.setColor(Color.black);
					g.drawLine(0, (int) (mouse.getLocation().getY()) + 1, 800,
							(int) (mouse.getLocation().getY()) + 1);
					g.drawLine((int) (mouse.getLocation().getX()) + 1, 0,
							(int) (mouse.getLocation().getX()) + 1, 800);
				}

				if (afk) {
					g.setColor(new Color(+specialColor2, +specialColor, 0, 100));
					g.fillRect(0, 0, 516, 337);
					g.setColor(Color.black);
					g.setStroke(stroke1);
					g.drawRect(0, 0, 516, 337);
					g.setFont(font1);
					g.setColor(color2);
					g.drawString("Antiban: Away from keyboard.", 135, 175);

				}

				nextLVL = currentLVL + 1;
				setFoodTNL((int) (skills.getExpToNextLevel(Skills.COOKING) / foodXP));
				xpPH = (int) ((gainedXP) * 3600000.0 / runTime);
				cookedPH = (int) ((cooked) * 3600000.0 / runTime);
				currentLVL = skills.getCurrentLevel(Skills.COOKING);
				gainedXP = skills.getCurrentExp(Skills.COOKING) - startXP;
				gainedLVL = skills.getCurrentLevel(Skills.COOKING) - startLVL;
				TNL = skills.getExpToNextLevel(Skills.COOKING);
				timeTNL = (long) ((double) TNL / (double) xpPH * 3600000);
				runTime = System.currentTimeMillis() - startTime;

			}
		}
	}

	class antibanThread extends Thread {
		@Override
		public void run() {
			while (run) {
				if (getMyPlayer().getAnimation() != -1)
					antiban();
				try {
					sleep(random(30000, 60000));
				} catch (InterruptedException e) {
				}
			}
		}
	}

	// END: Code generated using Enfilade's Easel
	/**
	 * @author Robert G
	 */
	public class iCookGui extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public iCookGui() {
			initComponents();
		}

		private void okButtonActionPerformed(ActionEvent e) {
			foodID = Integer.parseInt(foodBox.getText());
			run = true;
			Thread thread = new antibanThread();
			thread.start();
			g.dispose();
			start = true;
		}

		private void initComponents() {
			// JFormDesigner - Component initialization - DO NOT MODIFY
			// //GEN-BEGIN:initComponents
			// Generated using JFormDesigner Evaluation license - Robert G
			dialogPane = new JPanel();
			contentPanel = new JPanel();
			label1 = new JLabel();
			label2 = new JLabel();
			label3 = new JLabel();
			foodBox = new JTextField();
			buttonBar = new JPanel();
			okButton = new JButton();

			// ======== this ========
			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());

			// ======== dialogPane ========
			{
				dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));

				// JFormDesigner evaluation mark
				dialogPane.setBorder(new javax.swing.border.CompoundBorder(
						new javax.swing.border.TitledBorder(
								new javax.swing.border.EmptyBorder(0, 0, 0, 0),
								"Made by Robert G",
								javax.swing.border.TitledBorder.CENTER,
								javax.swing.border.TitledBorder.BOTTOM,
								new java.awt.Font("Dialog", java.awt.Font.BOLD,
										12), java.awt.Color.black), dialogPane
								.getBorder()));
				dialogPane
						.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
							@Override
							public void propertyChange(
									java.beans.PropertyChangeEvent e) {
								if ("border".equals(e.getPropertyName()))
									throw new RuntimeException();
							}
						});

				dialogPane.setLayout(new BorderLayout());

				// ======== contentPanel ========
				{

					// ---- label1 ----
					label1.setText("Welcome to iCook!");
					label1.setFont(new Font("Georgia", Font.BOLD, 20));

					// ---- label2 ----
					label2.setText("To use this script simply put the ID of the food you want to cook,");

					// ---- label3 ----
					label3.setText("into the box below and then hit start.");

					// ---- foodBox ----
					foodBox.setText("335");

					GroupLayout contentPanelLayout = new GroupLayout(
							contentPanel);
					contentPanel.setLayout(contentPanelLayout);
					contentPanelLayout
							.setHorizontalGroup(contentPanelLayout
									.createParallelGroup()
									.addGroup(
											contentPanelLayout
													.createSequentialGroup()
													.addGroup(
															contentPanelLayout
																	.createParallelGroup()
																	.addGroup(
																			contentPanelLayout
																					.createSequentialGroup()
																					.addGap(30,
																							30,
																							30)
																					.addComponent(
																							label1))
																	.addComponent(
																			label2)
																	.addComponent(
																			label3)
																	.addComponent(
																			foodBox,
																			GroupLayout.PREFERRED_SIZE,
																			48,
																			GroupLayout.PREFERRED_SIZE))
													.addContainerGap(38,
															Short.MAX_VALUE)));
					contentPanelLayout
							.setVerticalGroup(contentPanelLayout
									.createParallelGroup()
									.addGroup(
											contentPanelLayout
													.createSequentialGroup()
													.addComponent(label1)
													.addGap(29, 29, 29)
													.addComponent(label2)
													.addPreferredGap(
															LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(label3)
													.addPreferredGap(
															LayoutStyle.ComponentPlacement.RELATED)
													.addComponent(
															foodBox,
															GroupLayout.PREFERRED_SIZE,
															GroupLayout.DEFAULT_SIZE,
															GroupLayout.PREFERRED_SIZE)
													.addContainerGap(101,
															Short.MAX_VALUE)));
				}
				dialogPane.add(contentPanel, BorderLayout.CENTER);

				// ======== buttonBar ========
				{
					buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
					buttonBar.setLayout(new GridBagLayout());
					((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[] {
							0, 80 };
					((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[] {
							1.0, 0.0 };

					// ---- okButton ----
					okButton.setText("Start");
					okButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							okButtonActionPerformed(e);
						}
					});
					buttonBar.add(okButton, new GridBagConstraints(1, 0, 1, 1,
							0.0, 0.0, GridBagConstraints.CENTER,
							GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
							0));
				}
				dialogPane.add(buttonBar, BorderLayout.SOUTH);
			}
			contentPane.add(dialogPane, BorderLayout.CENTER);
			pack();
			setLocationRelativeTo(getOwner());
			// JFormDesigner - End of component initialization
			// //GEN-END:initComponents
		}

		// JFormDesigner - Variables declaration - DO NOT MODIFY
		// //GEN-BEGIN:variables
		// Generated using JFormDesigner Evaluation license - Robert G
		private JPanel dialogPane;
		private JPanel contentPanel;
		private JLabel label1;
		private JLabel label2;
		private JLabel label3;
		private JTextField foodBox;
		private JPanel buttonBar;
		private JButton okButton;
		// JFormDesigner - End of variables declaration //GEN-END:variables
	}

}
