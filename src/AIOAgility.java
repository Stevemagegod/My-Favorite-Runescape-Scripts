import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Filter;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSModel;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSTile;

@SuppressWarnings("unused")
@ScriptManifest(authors = { "Robert G" }, name = "AIOAgility", version = 1.7, description = "Barbarian basic, Barbarian advanced, Gnome basic, Gnome advanced, Ape atol.")
public class AIOAgility extends Script implements PaintListener, MouseListener, MouseMotionListener {
  
	private List<Obstacle> Obstacles = new LinkedList<Obstacle>();
	
	private AgilityGUI gui = null;
	
	private static int[] foodID = null;
	private static final int[] chatBox = {137, 0};
	private static int paintX, paintY;
	private static int startXP, curXP, startLVL, curLVL;
	private static int mouseSpeed = 2;
	
	private static long startTime, runTime;
	
	private static String courseName = "Loading...";
	private static String status = "Loading...";
	
	private static boolean hide = false, eat = false, start = false;
	
	private static Rectangle box;
	
	private static final NumberFormat nf = NumberFormat.getInstance();

    private static final Color color2 = new Color(0, 0, 0);
    private static final Color color3 = new Color(139, 119, 101);
    private static final Color color4 = new Color(153, 0, 0);

    private static final Font font1 = new Font("Calibri", 0, 10);
    private static final Font font2 = new Font("Calibri", 1, 15);

    private static Image img1 = null;
    private static Image img2 = null;
    
    private RSModel model = null;
   
    private static final Timer logOutTimer = new Timer(5 * 60000);
    
    private static final RenderingHints antialiasing = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	
	public interface Obstacle {
		
		public boolean isValid();
		public int loop();
		public String state();
		
	}
	
	private RSComponent chatArea() {
		return interfaces.getComponent(chatBox[0], chatBox[1]);
	}
	
	private RSTile myTile() {
		return getMyPlayer().getLocation();
	}
	
	private boolean in(final RSArea area) {
		return area.contains(myTile());
	}
	
	private boolean idle() {
		final RSObject barrier = objects.getNearest("Barrier");
		return getMyPlayer().getAnimation() == -1 || getMyPlayer().getAnimation() == 10289 || getMyPlayer().getAnimation() == 11789 && barrier != null && calc.distanceTo(barrier.getLocation()) <= 3;
	}
	
	private void moveRandomly() {
		final int x = (int)mouse.getLocation().getX();
		final int y = (int)mouse.getLocation().getY();
		final int randomX = random(x - 15, x + 15);
		final int randomY = random(y - 15, y + 15);
		mouse.move(randomX, randomY);
	}
	 
	private boolean walk(RSTile dest) {
		final Point p = calc.tileToMinimap(dest);
		if (p.x != -1 && p.y != -1) {
			if (!mouse.getLocation().equals(p)) {
				mouse.hop(p);
				mouse.click(true);
				moveRandomly();
				return true;
			}
		}
		return false;
	}
	
	private RSObject getObject(final RSTile location, final int... id) {
		return objects.getNearest(new Filter<RSObject>() {
			
			@Override
			public boolean accept(RSObject obj) {
				if (location != null) {
					for (int i : id) {
						if (obj.getID() != i || !obj.getLocation().equals(location)) {
							continue;
						}
						return obj.getID() == i && obj.getLocation().equals(location);
					}
				} else {
					for (int i : id) {
						if (obj.getID() != i) {
							continue;
						}
						return obj.getID() == i;
					}
				}
				return false;
			}
			
		});
	}
	
	private static int trys = 0;

	private boolean doObstacle(String action, int... id) {
		final RSObject obj = getObject(null, id);
		if (obj != null) {
			model = obj.getModel();
			if (obj.isOnScreen()) {
				if (model != null) {
					if (trys > 20) {
						moveRandomly();
						final int angle = camera.getAngle();
						camera.setAngle(random(angle - 30, angle + 30));
						trys = 0;
						return false;
					}
					if (trys < 1) {
						mouse.hop(model.getCentralPoint());
						trys++;
					}
					if (model.interact(action)) {
						trys = 0;
						return true;
					} else {
						trys ++;
						return false;
					}
				} else {
					log("Model for " + obj.getName() + " is null.");
					log("Switching interact method.");
					return obj.interact(action);
				}
			} else {
				if (!getMyPlayer().isMoving()) {
					new turn(obj);
				}
				if (!obj.isOnScreen()) {
					if (walking.getDestination() == null || walking.getDestination() != null && !getMyPlayer().isMoving()) {
						if (walk(obj.getLocation())) {
							return false;
						}
					} else {
						return false;
					}
				}
			}
		}
		return false;
	}
	
	private boolean vexilliumInterfaceOpen() {
		return interfaces.getComponent(1107, 156).isValid();
	}
	
	private boolean clientBusy() {
		return !game.isLoggedIn() || game.isWelcomeScreen() || game.getClientState() != 11;
	}
	
	public class ClientHandler implements Obstacle {

		@Override
		public boolean isValid() {
			return clientBusy() || logOutTimer.getRemaining() == 0;
		}

		@Override
		public int loop() {
			if (logOutTimer.getRemaining() == 0) {
				log("Idle for 5 minutes, stopping script and loging out.");
				env.saveScreenshot(false);
				stopScript(true);
			}
			return 1000;
		}

		@Override
		public String state() {
			return "Waiting for client";
		}
		
	}
	
	public class MotionHandler implements Obstacle {
		
		@Override
		public boolean isValid() {
			return !idle() || getMyPlayer().isMoving() && getMyPlayer().getAnimation() != 11789;
		}

		@Override
		public int loop() {
			logOutTimer.reset();
			return(random(300, 400));
		}

		@Override
		public String state() {
			return status;
		}
		
	}
	
	public class EnergyHandler implements Obstacle {

		@Override
		public boolean isValid() {
			return walking.getEnergy() >= 20 && !walking.isRunEnabled();
		}

		@Override
		public int loop() {
			walking.setRun(true);
			return random(1000, 1200);
		}

		@Override
		public String state() {
			return "Turning on run energy.";
		}
		
	}
	
	public class VexilliumHandler implements Obstacle {

		@Override
		public boolean isValid() {
			return vexilliumInterfaceOpen();
		}

		@Override
		public int loop() {
			if (interfaces.getComponent(1107, 156).isValid()) {
				if (interfaces.getComponent(1107, 156).doClick()) {
					return random(1000, 1200);
				}
			}
			return 0;
		}

		@Override
		public String state() {
			return "Closing clan vexillium.";
		}
		
	}
	
	public class FoodHandler implements Obstacle {
		
		private int[] food;
		
		private FoodHandler(int[] food) {
			this.food = food;
		}

		@Override
		public boolean isValid() {
			return eat && combat.getHealth() <= 50;
		}

		@Override
		public int loop() {
			final RSItem munch = inventory.getItem(food);
			if (munch != null) {
				if (munch.interact("Eat")) {
					return random(1000, 1200);
				}
			} else {
				log.severe("Ran out of food!, stopping script.");
				stopScript(true);
				return 0;
			}
			return 0;
		}

		@Override
		public String state() {
			return "Eating food.";
		}
		
	}
	
	public class BarbarianEntrancePipe implements Obstacle {
		
		private final int entrancePipe = 20210;
		private final RSArea entrancePipeArea = new RSArea(new RSTile(2546, 3561), new RSTile(2555, 3564), 0);

		@Override
		public boolean isValid() {
			return in(entrancePipeArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Squeeze", entrancePipe)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Entrance pipe.";
		}
		
	}
	
	public class BarbarianRopeSwing implements Obstacle {
		
		private final int ropeSwing = 43526;
		private final RSArea ropeSwingArea = new RSArea(new RSTile(2543, 3551), new RSTile(2555, 3559), 0);

		@Override
		public boolean isValid() {
			return in(ropeSwingArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Swing", ropeSwing)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Rope swing.";
		}
		
	}
	
	public class BarbarianPit implements Obstacle {
		
		private final int failedLadder = 32015;
		
		private boolean inBarbarianPit() {
			final RSObject ladder = getObject(null, failedLadder);
			return ladder != null;
		}

		@Override
		public boolean isValid() {
			return inBarbarianPit() && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", failedLadder)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Climbing out of pit.";
		}
		
	}
	
	public class BarbarianLogBalance implements Obstacle {
		
		private final int balance = 43595;
		private final RSArea balanceArea = new RSArea(new RSTile(2543, 3542), new RSTile(2555, 3549), 0);
		
		@Override
		public boolean isValid() {
			return in(balanceArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Walk", balance)) {
				new camera('w');
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Log balance.";
		}
		
	}
	
	public class BarbarianObstacleNet implements Obstacle {
		
		private final int obstacleNet = 20211;
		private final RSArea obstacleNetArea = new RSArea(new RSTile(2539, 3542), new RSTile(2542, 3548), 0);

		@Override
		public boolean isValid() {
			return in(obstacleNetArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", obstacleNet)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Obstacle net.";
		}
		
	}
	
	public class BarbarianBalancingLedge implements Obstacle {
		
		private final int balancingLedge = 2302;
		private final RSArea balancingLedgeArea = new RSArea(new RSTile(2536, 3545), new RSTile(2537, 3547), 1);

		@Override
		public boolean isValid() {
			return in(balancingLedgeArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Walk", balancingLedge)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Balancing ledge.";
		}
		
	}
	
	public class BarbarianLadderDown implements Obstacle {
		
		private final int ladderDown = 3205;
		private final RSArea ladderDownArea = new RSArea(new RSTile(2532, 3546), new RSTile(2532, 3547), 1);

		@Override
		public boolean isValid() {
			return in(ladderDownArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", ladderDown)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Ladder.";
		}
		
	}
	
	public class BarbarianCrumblingWall implements Obstacle {
		
		private final int crumblingWall = 1948;
		private final RSArea crumblingWallArea = new RSArea(new RSTile(2531, 3545), new RSTile(2537, 3556), 0);
		private final RSTile wallTile = new RSTile(2537, 3553);

		@Override
		public boolean isValid() {
			return in(crumblingWallArea) && idle();
		}

		@Override
		public int loop() {
			final RSObject wall = getObject(wallTile, crumblingWall);
			if (wall != null) {
				model = wall.getModel();
				if (wall.isOnScreen()) {
					if (wall.interact("Climb")) {
						new camera('e');
						return random(1200, 1400);
					}
				} else {
					new turn(wall);
					if (!wall.isOnScreen()) {
						if (walk(new RSTile(2537, 3553))) {
							return random(1000, 1200);
						}
					}
				}
			}
			return 0;
		}

		@Override
		public String state() {
			return "Crumbling wall.";
		}
		
	}
	
	public class BarbarianCrumblingWall2 implements Obstacle {
		
		private int crumblingWall = 1948;
		private final RSArea crumblingWallArea2 = new RSArea(new RSTile(2538, 3552), new RSTile(2542, 3554), 0);
		private RSTile wallTile = new RSTile(2542, 3553);

		@Override
		public boolean isValid() {
			return in(crumblingWallArea2) && idle();
		}

		@Override
		public int loop() {
			final RSObject wall = getObject(wallTile, crumblingWall);
			if (wall != null) {
				model = wall.getModel();
				if (wall.isOnScreen()) {
					if (wall.interact("Climb")) {
						return random(1200, 1400);
					}
				} else {
					new turn(wall);
					if (!wall.isOnScreen()) {
						if (walk(wall.getLocation())) {
							return random(1000, 1200);
						}
					}
				}
			}
			return 0;
		}

		@Override
		public String state() {
			return "Crumbling wall.";
		}
		
	}
	
	public class BarbarianRunUpWall implements Obstacle {
		
		private final int runUpWall = 43533;
		private final RSArea runUpWallArea = new RSArea(new RSTile(2536, 3542), new RSTile(2542, 3551), 0);

		@Override
		public boolean isValid() {
			return in(runUpWallArea) && idle() && !getMyPlayer().isMoving();
		}

		@Override
		public int loop() {
			if (doObstacle("Run", runUpWall)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Wall.";
		}
		
	}
	
	public class BarbarianClimbUpWall implements Obstacle {
		
		private final int wall = 43597;
		private final RSArea runUpWallArea2 = new RSArea(new RSTile(2537, 3545), new RSTile(2538, 3547), 2);

		@Override
		public boolean isValid() {
			return in(runUpWallArea2) && idle();
		}

		@Override
		public int loop() {
			new cameraPitch(true);
			if (doObstacle("Climb", wall)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Wall.";
		}
		
	}
	
	public class BarbarianSpringDevice implements Obstacle {
		
		private final int springDevice = 43587;
		private final RSArea springDeviceArea = new RSArea(new RSTile(2532, 3545), new RSTile(2536, 3547), 3);

		@Override
		public boolean isValid() {
			return in(springDeviceArea) && idle() && !getMyPlayer().isMoving();
		}

		@Override
		public int loop() {
			if (doObstacle("Fire", springDevice)) {
				new camera('n');
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Spring device.";
		}
		
	}
	
	public class BarbarianBalancingBeam implements Obstacle {
		
		private final int balancingBeam = 43527;
		private final RSArea balancingBeamArea = new RSArea(new RSTile(2530, 3553), new RSTile(2533, 3554), 3);

		@Override
		public boolean isValid() {
			return in(balancingBeamArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Cross", balancingBeam)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Balancing beam.";
		}
		
	}
	
	public class BarbarianGap implements Obstacle {
		
		private final int gap = 43531;
		
		private boolean atBarbarianGap() {
			final RSTile tile = new RSTile(2536, 3553, 3);
			return getMyPlayer().getLocation().equals(tile);
		}

		@Override
		public boolean isValid() {
			return atBarbarianGap();
		}

		@Override
		public int loop() {
			if (doObstacle("Jump", gap)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Gap.";
		}
		
	}
	
	public class BarbarianRoof implements Obstacle {
		
		private final int roof = 43532;
		private final RSArea slideDownRoofArea = new RSArea(new RSTile(2538, 3552), new RSTile(2542, 3554), 2);

		@Override
		public boolean isValid() {
			return in(slideDownRoofArea) && idle() && !getMyPlayer().isMoving();
		}

		@Override
		public int loop() {
			if (doObstacle("Slide", roof)) {
				new camera('e');
				new cameraPitch(false);
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Roof.";
		}
		
	}
	
	public class GnomeLogBalance implements Obstacle {
		
		private final int logBalance = 69526;
		private final RSTile logTile = new RSTile(2475, 3435);
		private final RSArea logBalanceArea = new RSArea(new RSTile(2469, 3430), new RSTile(2479, 3440), 0);

		@Override
		public boolean isValid() {
			return in(logBalanceArea) && idle();
		}

		@Override
		public int loop() {
			if (calc.distanceTo(logTile) > 5) {
				if (walk(logTile)) {
					new camera('s');
					new cameraPitch(true);
					return random(1000, 1200);
				}
			} else {
				if (doObstacle("walk", logBalance)) {
					return random(1000, 1200);
				}
			}
			return 0;
		}

		@Override
		public String state() {
			return "Log balance.";
		}
		
	}
	
	public class GnomeObstacleNet implements Obstacle {
		
		private final int obstacleNet = 69383;
		private final RSArea obstacleNetArea = new RSArea(new RSTile(2471, 3426), new RSTile(2478, 3430), 0);

		@Override
		public boolean isValid() {
			return in(obstacleNetArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", obstacleNet)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Obstacle net.";
		}
		
	}
	
	public class GnomeTreeBranchUp implements Obstacle {
		
		private boolean camera;
		private boolean rope;
		
		private final int treeBranch = 69508;
		private final RSArea treeBranchArea = new RSArea(new RSTile(2471, 3422), new RSTile(2476, 3424), 1);
		
		private GnomeTreeBranchUp(boolean camera, boolean rope) {
			this.camera = camera;
			this.rope = rope;
		}

		@Override
		public boolean isValid() {
			return in(treeBranchArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", treeBranch)) {
				if (rope) {
					new camera('e');
					new cameraPitch(camera);
				}
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Tree branch";
		}
		
	}
	
	public class GnomeTree implements Obstacle {
		
		private final int tree = 69506;
		private final RSArea treeArea = new RSArea(new RSTile(2471, 3418), new RSTile(2477, 3421), 2);

		@Override
		public boolean isValid() {
			return in(treeArea) || objects.getNearest(tree) != null;
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", tree)) {
				new cameraPitch(false);
				new camera('e');
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Tree";
		}
		
	}
	
	public class GnomeBalancingRope implements Obstacle {
		
		private final int balancingRope = 2312;
		private final RSArea balancingRopeArea = new RSArea(new RSTile(2472, 3418), new RSTile(2477, 3421), 2);

		@Override
		public boolean isValid() {
			return in(balancingRopeArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Walk-on", balancingRope)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Balancing rope.";
		}
		
	}
	
	public class GnomeTreeDown implements Obstacle {
		
		private final int treeDown = 69507;
		private final RSArea treeDownArea = new RSArea(new RSTile(2483, 3418), new RSTile(2488, 3421), 2);

		@Override
		public boolean isValid() {
			return in(treeDownArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", treeDown)) {
				new camera('n');
				new cameraPitch(false);
				return random(1000, 1200);
			}	
			return 0;
		}

		@Override
		public String state() {
			return "Tree.";
		}
		
	}
	
	public class GnomeObstacleNet2 implements Obstacle {
		
		private final int obstacleNet2 = 69384;
		private final RSArea obstacleNet2Area = new RSArea(new RSTile(2481, 3415), new RSTile(2490, 3426), 0);

		@Override
		public boolean isValid() {
			return in(obstacleNet2Area) && idle();
		}

		@Override
		public int loop() {
			if (!getMyPlayer().isMoving()) {
				new cameraPitch(false);
			}
			if (doObstacle("Climb", obstacleNet2)) {
				if (!getMyPlayer().isMoving()) {
					new cameraPitch(true);
				}
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Obstacle net.";
		}
		
	}
	
	public class GnomeExitPipe implements Obstacle {
		
		private final int exitPipe[] = { 69378, 69377 };
		private final RSArea exitPipeArea = new RSArea(new RSTile(2480, 3426), new RSTile(2489, 3435), 0);

		@Override
		public boolean isValid() {
			return in(exitPipeArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Squeeze", exitPipe)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Exit pipe.";
		}
		
	}
	
	public class GnomeSignPost implements Obstacle {
		
		private final int signpost = 69514;
		private final RSArea signpostArea = new RSArea(new RSTile(2472, 3418), new RSTile(2477, 3420), 3);

		@Override
		public boolean isValid() {
			return in(signpostArea) && idle() && !getMyPlayer().isMoving();
		}

		@Override
		public int loop() {
			if (doObstacle("Run", signpost)) {
				new cameraPitch(false);
				new camera('n');
				return random(1800, 2000);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Signpost.";
		}
		
	}
	
	public class GnomePole implements Obstacle {
		
		private final int pole = 43529;
		private final RSArea poleArea = new RSArea(new RSTile(2482, 3418), new RSTile(2487, 3421), 3);

		@Override
		public boolean isValid() {
			return in(poleArea) && idle() && !getMyPlayer().isMoving();
		}

		@Override
		public int loop() {
			if (doObstacle("Swing", pole)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Pole.";
		}
		
	}
	
	public class GnomeBarrier implements Obstacle {
		
		private final int barrier = 69389;
		private final RSArea barrierArea = new RSArea(new RSTile(2482, 3432), new RSTile(2488, 3435), 3);

		@Override
		public boolean isValid() {
			return in(barrierArea);
		}

		@Override
		public int loop() {
			if (doObstacle("Jump", barrier)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Barrier";
		}
		
	}
	
	public class GnomeExitArea implements Obstacle {
		
		private final RSArea exitArea = new RSArea(new RSTile(2480, 3436), new RSTile(2490, 3440), 0);
		private final RSTile logTile = new RSTile(2475, 3435);

		@Override
		public boolean isValid() {
			return in(exitArea) && idle();
		}

		@Override
		public int loop() {
			if (calc.distanceTo(walking.getDestination()) <= 5) {
				if (walk(logTile)) {
					new camera('s');
					new cameraPitch(true);
					return random(500, 800);
				}
			} else {
				return 500;
			}
			return 0;
		}

		@Override
		public String state() {
			return "Walking to log.";
		}
		
	}
	
	public class ApeSteppingStone implements Obstacle {
		
		private final int stoneID = 12568;
		private final RSArea steppingStoneArea = new RSArea(new RSTile(2755, 2742), new RSTile(2779, 2754), 0);
		private final RSTile tile = new RSTile(2754, 2742);
		private final RSTile stoneTile = new RSTile(2755, 2742);
		
		private boolean steppingStone() {
			
			final RSObject stone = getObject(tile, stoneID);
			model = stone.getModel();
			if (stone != null) {
				if (stone.isOnScreen()) {
					return stone.interact("Jump");
				} else {
					if (walk(stoneTile)) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public boolean isValid() {
			return in(steppingStoneArea) && idle();
		}

		@Override
		public int loop() {
			new camera('w');
			new cameraPitch(true);
			if (steppingStone()) {
				return random(1000, 1200);
			}
			return 0;
		}
		
		@Override
		public String state() {
			return "Stepping stone.";
		}
		
	}
	
	public class ApeTropicalTree implements Obstacle {
		
		private final int tropicalTree = 12570;
		private final RSArea tropicalTreeArea = new RSArea(new RSTile(2752, 2742), new RSTile(2753, 2742, 0));

		@Override
		public boolean isValid() {
			return in(tropicalTreeArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", tropicalTree)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Tropical tree.";
		}
		
	}
	
	public class ApeMonkeyBars implements Obstacle {
		
		private final int monkeyBars = 12573;
		private final RSArea monkeyBarsArea = new RSArea(new RSTile(2752, 2741), new RSTile(2754, 2742), 2);

		@Override
		public boolean isValid() {
			return in(monkeyBarsArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Swing", monkeyBars)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Monkey bars.";
		}
		
	}
	
	public class ApeSkullSlope implements Obstacle {
		
		private final int skullSlope = 12576;
		private final RSTile skullSlopeTile = new RSTile(2747, 2741, 0);
		
		private boolean atSlope() {
			return myTile().equals(skullSlopeTile);
		}

		@Override
		public boolean isValid() {
			return atSlope() && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", skullSlope)) {
				new camera('e');
				new cameraPitch(false);
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Skull slope.";
		}
		
	}
	
	public class ApeRopeSwing implements Obstacle {
		
		private final int ropeSwing = 12578;
		private final RSTile[] ropeSwingArea = { new RSTile(2741, 2740),
				new RSTile(2741, 2739), new RSTile(2741, 2738),
				new RSTile(2741, 2733), new RSTile(2741, 2732),
				new RSTile(2742, 2741), new RSTile(2742, 2740),
				new RSTile(2742, 2739), new RSTile(2742, 2738),
				new RSTile(2742, 2737), new RSTile(2742, 2736),
				new RSTile(2742, 2734), new RSTile(2742, 2732),
				new RSTile(2742, 2731), new RSTile(2743, 2741),
				new RSTile(2743, 2740), new RSTile(2743, 2739),
				new RSTile(2743, 2738), new RSTile(2743, 2737),
				new RSTile(2743, 2736), new RSTile(2743, 2735),
				new RSTile(2743, 2734), new RSTile(2743, 2732),
				new RSTile(2743, 2731), new RSTile(2743, 2730),
				new RSTile(2744, 2741), new RSTile(2744, 2740),
				new RSTile(2744, 2739), new RSTile(2744, 2738),
				new RSTile(2744, 2737), new RSTile(2744, 2736),
				new RSTile(2744, 2735), new RSTile(2744, 2734),
				new RSTile(2744, 2732), new RSTile(2744, 2731),
				new RSTile(2744, 2730), new RSTile(2745, 2741),
				new RSTile(2745, 2739), new RSTile(2745, 2737),
				new RSTile(2745, 2736), new RSTile(2745, 2735),
				new RSTile(2745, 2734), new RSTile(2745, 2733),
				new RSTile(2745, 2732), new RSTile(2745, 2731),
				new RSTile(2745, 2730), new RSTile(2745, 2729),
				new RSTile(2746, 2738), new RSTile(2746, 2737),
				new RSTile(2746, 2736), new RSTile(2746, 2735),
				new RSTile(2746, 2734), new RSTile(2746, 2733),
				new RSTile(2746, 2732), new RSTile(2746, 2731),
				new RSTile(2746, 2730), new RSTile(2746, 2729),
				new RSTile(2747, 2736), new RSTile(2747, 2735),
				new RSTile(2747, 2734), new RSTile(2747, 2733),
				new RSTile(2747, 2732), new RSTile(2747, 2729),
				new RSTile(2748, 2735), new RSTile(2748, 2734),
				new RSTile(2748, 2733), new RSTile(2748, 2732),
				new RSTile(2748, 2729), new RSTile(2748, 2728),
				new RSTile(2749, 2734), new RSTile(2749, 2733),
				new RSTile(2749, 2730), new RSTile(2749, 2729),
				new RSTile(2749, 2727), new RSTile(2750, 2734),
				new RSTile(2750, 2733), new RSTile(2750, 2730),
				new RSTile(2750, 2729), new RSTile(2750, 2728),
				new RSTile(2750, 2727), new RSTile(2750, 2726),
				new RSTile(2751, 2733), new RSTile(2751, 2732),
				new RSTile(2751, 2731), new RSTile(2751, 2730),
				new RSTile(2751, 2729), new RSTile(2751, 2728),
				new RSTile(2751, 2727), new RSTile(2751, 2726),
				new RSTile(2751, 2725), new RSTile(2752, 2730),
				new RSTile(2752, 2729) };
		
		private boolean atSwing() {
			for (RSTile t : ropeSwingArea) {
				if (myTile().equals(t)) {
					return true;
				}
			}
			return false;
		}
		
		private boolean ropeSwing() {
			final RSObject swing = getObject(null, ropeSwing);
			model = swing.getModel();
			if (swing.isOnScreen()) {
				mouse.hop(swing.getPoint());
				return swing.interact("Swing");
			} else {
				return walk(new RSTile(2751, 2731));
			}
		}
		
		@Override
		public boolean isValid() {
			return atSwing() && idle();
		}

		@Override
		public int loop() {
			if (ropeSwing()) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Ropeswing.";
		}
		
	}
	
	public class ApeTreeDown implements Obstacle {
		
		private final int upperTropicalTree = 12618;
		private final RSArea upperTropicalTreeArea = new RSArea(2756, 2731, 2760, 2736);
		
		@Override
		public boolean isValid() {
			return in(upperTropicalTreeArea) && idle();
		}

		@Override
		public int loop() {
			if (doObstacle("Climb", upperTropicalTree)) {
				return random(1000, 1200);
			}
			return 0;
		}

		@Override
		public String state() {
			return "Tropical tree.";
		}
		
	}
	
	public class loader extends Thread {

		private loader() {
			Thread t = new Thread(this);
			t.start();
		}

		@Override
		public void run() {
			img1 = loadImage("http://images1.wikia.nocookie.net/__cb20120116170308/runescape/images/1/14/Agility.png", "AgilitySprite.png");
			img2 = loadImage("http://desmond.imageshack.us/Himg546/scaled.php?server=546&filename=cooltext656478950.png&res=medium", "PaintBase.png");
		}

	}
	
	public class turn extends Thread {
		
		private RSObject object;
		
		private turn(RSObject object) {
			this.object = object;
			Thread t= new Thread(this);
			t.start();
		}
		
		@Override
		public void run() {
			if (object != null) {
				if (!object.isOnScreen()) {
					camera.turnTo(object);
				}
			}
		}
		
	}
	
	public class camera extends Thread {

		private char direction;

		private camera(char direction) {
			this.direction = direction;
			Thread t = new Thread(this);
			t.start();
		}

		public void setCompass(final char direction) {
			switch (direction) {
			case 'n':
				camera.setAngle(random(10, 20));
				break;
			case 'w':
				camera.setAngle(random(79, 99));
				break;
			case 's':
				camera.setAngle(random(169, 189));
				break;
			case 'e':
				camera.setAngle(random(259, 279));
				break;
			default:
				break;
			}
		}
		 
		private boolean cameraDirection(final char direction) {
			final int angle = camera.getAngle();
			switch (direction) {
			case 'n':
				if (angle >= 0 && angle <= 20 || angle >= 335 && angle <= 359) {
					return true;
				}
				return false;
			case 'w':
				if (angle >= 70 && angle <= 110) {
					return true;
				}
				return false;
			case 's':
				if (angle >= 160 && angle <= 199) {
					return true;
				}
				return false;
			case 'e':
				if (angle >= 249 && angle <= 289) {
					return true;
				}
				return false;
			}
			return false;
		}	
		
		@Override
		public void run() {
			if (!cameraDirection(direction)) {
				setCompass(direction);
			}
		}
		
	}
	
	public class cameraPitch extends Thread {
		
		private boolean direction;
		
		private cameraPitch(boolean direction) {
			this.direction = direction;
			Thread t = new Thread(this);
			t.start();
		}
		
		@Override
		public void run() {
			if (direction) {
				keyboard.pressKey((char)KeyEvent.VK_UP);
				try {
					Thread.sleep(random(2800, 3000));
				} catch (InterruptedException e) {
				}
				keyboard.releaseKey((char)KeyEvent.VK_UP);
			} else {
				keyboard.pressKey((char)KeyEvent.VK_DOWN);
				try {
					Thread.sleep(random(2800, 3000));
				} catch (InterruptedException e) {
				}
				keyboard.releaseKey((char)KeyEvent.VK_DOWN);
			}
		}
		
	}
	
	public class cameraAngle extends Thread {
		
		private boolean direction;
		
		private cameraAngle(boolean direction) {
			this.direction = direction;
			Thread t = new Thread(this);
			t.start();
		}
		
		@Override
		public void run() {
			if (direction) {
				keyboard.pressKey((char)KeyEvent.VK_LEFT);
				try {
					Thread.sleep(random(2800, 3000));
				} catch (InterruptedException e) {
				}
				keyboard.releaseKey((char)KeyEvent.VK_LEFT);
			} else {
				keyboard.pressKey((char)KeyEvent.VK_RIGHT);
				try {
					Thread.sleep(random(2800, 3000));
				} catch (InterruptedException e) {
				}
				keyboard.releaseKey((char)KeyEvent.VK_RIGHT);
			}
		}
		
	}
	
	private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }
	
	private void saveImage(Image img, String fileName, String imageType, File file) {
		 try {
			ImageIO.write((RenderedImage) img, imageType, file);
			log("Saved image.");
		} catch (IOException e) {
		}
	}
	
	private Image loadImage(String url, String imageDetails) {
		File file = new File(getCacheDirectory() + File.separator, imageDetails);
		Image i;
		if (!file.exists()) {
			i = getImage(url);
			saveImage(i, imageDetails, "png", file);
			return i;
		} else {
			try {
				log("loaded image from hard drive.");
				return ImageIO.read(file);
			} catch (IOException e) {
				return null;
			}
		}
	}
	
	private int PH(int arg0) {
		int PH = (int) (3600000.0 / runTime * arg0);
		return PH;
	}

	private String nf(int number) {
		return nf.format(number);
	}

	private String ft(long duration) {
		String res = "";
		long days = TimeUnit.MILLISECONDS.toDays(duration);
		long hours = TimeUnit.MILLISECONDS.toHours(duration)
				- TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(duration));
		long minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
				- TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS
						.toHours(duration));
		long seconds = TimeUnit.MILLISECONDS.toSeconds(duration)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
						.toMinutes(duration));
		if (days == 0) {
			res = (hours + ":" + minutes + ":" + seconds);
		} else {
			res = (days + ":" + hours + ":" + minutes + ":" + seconds);
		}
		return res;
	}
	
	@Override
	public boolean onStart() {
		log("Welcome to AIOAgility!");
		if (!game.isLoggedIn()) {
			log.severe("Please start logged in.");
			return false;
		}
		if (!game.isFixed()) {
			log.severe("For best results run in fixed screen size.");
		}
		new loader();
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					gui = new AgilityGUI();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		while (gui.isVisible()) {
			sleep(50);
		}
		if (chatArea().isValid()) {
			paintX = chatArea().getAbsoluteX();
			paintY = chatArea().getAbsoluteY();
		}
		env.disableRandom("InterfaceCloser");
		mouse.setSpeed(mouseSpeed);
		return start;
	}

	@Override
	public int loop() {
		if (clientBusy()) {
			return 1000;
		}
		if (curLVL == 99) {
			env.saveScreenshot(false);
			sleep(2000, 3000);
			log.severe("Reached 99 Agility!, logging out!");
			stopScript(true);
		}
		for (Obstacle obstacle : Obstacles) {
			if (obstacle.isValid()) {
				status = obstacle.state();
				return obstacle.loop();
			}
		}
		return 0;
	}
	
	@Override
	public void onFinish() {
		env.saveScreenshot(false);
	}
	
	private void paintObjects(Graphics g) {
		if (model != null && calc.pointOnScreen(model.getPoint())) {
			for (Polygon p : model.getTriangles()) {
				if (p != null) {
					g.setColor(new Color(250, 0, 0, 100));
					g.fillPolygon(p);
				}
			}
		}
	}
		
	@Override
	public void onRepaint(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHints(antialiasing);
		if (!hide) {
			runTime = System.currentTimeMillis() - startTime;
			curXP = skills.getCurrentExp(Skills.AGILITY);
			curLVL = skills.getRealLevel(Skills.AGILITY);
			int gainedXP = curXP - startXP;
			int gainedLVL = curLVL - startLVL;
			int tnl = skills.getExpToNextLevel(Skills.AGILITY);
			int till99 = skills.getExpToMaxLevel(Skills.AGILITY);
			long timeTNL = (long) ((double) tnl / (double) PH(gainedXP) * 3600000);
			long timeTo99 = (long) ((double) till99 / (double) PH(gainedXP) * 3600000);
			g.drawImage(img2, paintX - 1, paintY - 2, null);
			g.drawImage(img1, paintX + 443, paintY + 35, null);
			g.setFont(font1);
			g.setColor(color3.darker().darker().darker());
			g.drawString("v1.6", paintX + 466, paintY + 35);
			g.setFont(font2);
			g.drawString("Run Time: " + ft(runTime), paintX + 10, paintY + 25);
			g.drawString("Current Level: " + curLVL, paintX + 10, paintY + 40);
			g.drawString("Levels Gained: " + gainedLVL, paintX + 10, paintY + 55);
			if (gainedXP > 0) {
				g.drawString("Time TNL: " + ft(timeTNL), paintX + 10, paintY + 70);
				g.drawString("Time T99: " + ft(timeTo99), paintX + 10, paintY + 85);
			} else {
				g.drawString("Time TNL: Calculating", paintX + 10, paintY + 70);
				g.drawString("Time T99: Calculating", paintX + 10, paintY + 85);
			}
			g.drawString("Course: " + courseName, paintX + 10, paintY + 100);
			
			g.drawString("XP Gained: " + nf(gainedXP), paintX + 170, paintY + 40);
			g.drawString("XP Ph: " + nf(PH(gainedXP)), paintX + 170, paintY + 55);
			g.drawString("XP TNL: " + nf(tnl), paintX + 170, paintY + 70);
			g.drawString("Eating = " + eat, paintX + 170, paintY + 85);
			g.drawString("Status: " + status, paintX + 170, paintY + 100);
			
			
			int percent = skills.getPercentToNextLevel(Skills.AGILITY);
			int length = percent * 420 / 100;
			g.setColor(color4);
			g.fillRect(paintX + 10, paintY + 105, 420, 18);
			g.setColor(color2);
			g.drawRect(paintX + 10, paintY + 105, 420, 18);
			g.setColor(Color.green.darker().darker());
			g.fillRect(paintX + 10, paintY + 105, length, 18);
			g.setColor(color2);
			g.drawRect(paintX + 10, paintY + 105, length, 18);
			g.setColor(color3.darker().darker().darker());
			g.drawString(percent + "%", paintX + 220, paintY + 119);
			
			g.drawString("Hide", paintX + 460, paintY + 120);
			g.drawRect(paintX + 454, paintY + 105, 41, 18);
			paintObjects(g);
		} else {
			g.setColor(Color.gray.brighter().brighter());
			g.fillRect(paintX + 454, paintY + 105, 41, 18);
			g.setColor(color3.darker().darker().darker());
			g.drawRect(paintX + 454, paintY + 105, 41, 18);
			g1.setFont(font2);
			g.drawString("Show", paintX + 458, paintY + 120);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		box = new Rectangle(paintX + 454, paintY + 105, 41, 18);
		Point p = e.getPoint();
		if (box.contains(p) && !hide) {
			hide = true;
		} else if (box.contains(p) && hide) {
			hide = false;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {

	}

	@Override
	public void mouseExited(MouseEvent arg0) {

	}

	@Override
	public void mousePressed(MouseEvent e) {
	
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
	
	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (!hide) {
			paintX = arg0.getX();
			paintY = arg0.getY();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		
	}
	
	/**
	 * @author Robert-G
	 */
	public class AgilityGUI extends JFrame {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		//private File file;
		
		public AgilityGUI() {
			//file = new File(getCacheDirectory() + File.separator + "AIOAgility.ini");
			initComponents();
		}
		
		private int[] split(String string) {
			int[] integers;
			try {
				String[] x = string.replaceAll(" ", "").split(",");
				integers = new int[x.length];
				for (int i = 0; i < integers.length; i++) {
					integers[i] = Integer.parseInt(x[i].replaceAll("\\D", ""));
				}
			} catch (NumberFormatException e) {
				integers = new int[1];
				integers[0] = 0;
			}
			return integers;
		}
		
		private void save(Properties prop, Object[] e, String key) {
			StringBuilder builder = new StringBuilder();
			@SuppressWarnings({ "rawtypes", "unchecked" })
			ArrayList<Object> arrayList = new ArrayList();
			arrayList.addAll(Arrays.asList(e));
			for (int i = 0; i < arrayList.size(); i++) {
				builder.append(arrayList.get(i).toString());
				if (i != arrayList.size() - 1) {
					builder.append(",");
				}
			}
			prop.put(key, builder.toString());
		}

		private Object[] load(Properties prop, String key) {
			String value = prop.getProperty(key);
			String[] values;
			Object[] ee = new Object[0];
			if (value != null && (values = value.split(",")).length > 0) {
				ee = new Object[values.length];
				System.arraycopy(values, 0, ee, 0, values.length);
			}
			return ee;
		}
		
		ArrayList<Integer> foodArray = new ArrayList<Integer>();
		
		private Properties loadProperties() {
			try {
				//if (!file.exists()) {
					//file.createNewFile();
				//}
				Properties p = new Properties();
				//p.load(new FileInputStream(file));
				if (p.getProperty("foodID") != null) {
					for (Object o : load(p, "foodID")) {
						if (!foodArray.contains(Integer.parseInt(((String) o).trim()))) {
							foodArray.add(Integer.parseInt(((String) o).trim()));
						}
					}
				}
				return p;
			} catch (Exception e) {
			}
			return null;
		}

		private void saveProperties() {
			Properties p = new Properties();
			p.put("slider1", String.valueOf(slider1.getValue()));
			p.put("courseBox", courseBox.getSelectedItem());
			p.put("foodMode", Boolean.toString(foodMode.isSelected()));
			save(p, foodArray.toArray(new Object[foodArray.size()]), "foodID");
			try {
				//p.store(new FileOutputStream(file), "");
				log("Successfully saved GUI settings.");
			} catch (Exception e) {
				log("Failed to saved GUI settings.");
				e.printStackTrace();
			}
		}
		
		private void setValues() {
			Obstacles.add(new ClientHandler());
			setFood();
			Obstacles.add(new EnergyHandler());
			Obstacles.add(new VexilliumHandler());
			Obstacles.add(new MotionHandler());
			final String x = courseBox.getSelectedItem().toString().toLowerCase();
			if (x.equals("barbarian basic")) {
				createBarbBasicCourse();
			} else if (x.equals("barbarian advanced")) {
				createBarbAdvancedCourse();
			} else if (x.equals("gnome basic")) {
				createGnomeBasicCourse();
			} else if (x.equals("gnome advanced")) {
				createGnomeAdvancedCourse();
			} else if (x.equals("ape atol")) {
				createApeAtolCourse();
			}
			startXP = skills.getCurrentExp(Skills.AGILITY);
			startLVL = skills.getRealLevel(Skills.AGILITY);
			mouseSpeed = slider1.getValue();
			log("Mouse speed: " + mouseSpeed);
			startTime = System.currentTimeMillis();
			start = true;
		}
		
		private void createBarbBasicCourse() {
			courseName = "Barb basic.";
			Obstacles.add(new BarbarianEntrancePipe());
			Obstacles.add(new BarbarianRopeSwing());
			Obstacles.add(new BarbarianPit());
			Obstacles.add(new BarbarianLogBalance());
			Obstacles.add(new BarbarianObstacleNet());
			Obstacles.add(new BarbarianBalancingLedge());
			Obstacles.add(new BarbarianLadderDown());
			Obstacles.add(new BarbarianCrumblingWall());
			Obstacles.add(new BarbarianCrumblingWall2());
		}
		
		private void createBarbAdvancedCourse() {
			courseName = "Barb advanced.";
			Obstacles.add(new BarbarianEntrancePipe());
			Obstacles.add(new BarbarianRopeSwing());
			Obstacles.add(new BarbarianPit());
			Obstacles.add(new BarbarianLogBalance());
			Obstacles.add(new BarbarianRunUpWall());
			Obstacles.add(new BarbarianClimbUpWall());
			Obstacles.add(new BarbarianSpringDevice());
			Obstacles.add(new BarbarianBalancingBeam());
			Obstacles.add(new BarbarianGap());
			Obstacles.add(new BarbarianRoof());
		}
		
		private void createGnomeBasicCourse() {
			courseName = "Gnome basic.";
			Obstacles.add(new GnomeLogBalance());
			Obstacles.add(new GnomeObstacleNet());
			Obstacles.add(new GnomeTreeBranchUp(true, true));
			Obstacles.add(new GnomeBalancingRope());
			Obstacles.add(new GnomeTreeDown());
			Obstacles.add(new GnomeObstacleNet2());
			Obstacles.add(new GnomeExitPipe());
			Obstacles.add(new GnomeExitArea());
		}
		
		private void createGnomeAdvancedCourse() {
			courseName = "Gnome adv.";
			Obstacles.add(new GnomeLogBalance());
			Obstacles.add(new GnomeObstacleNet());
			Obstacles.add(new GnomeTreeBranchUp(false, false));
			Obstacles.add(new GnomeTree());
			Obstacles.add(new GnomeSignPost());
			Obstacles.add(new GnomePole());
			Obstacles.add(new GnomeBarrier());
			Obstacles.add(new GnomeExitArea());
		}
		
		private void createApeAtolCourse() {
			courseName = "Ape atol.";
			Obstacles.add(new ApeSteppingStone());
			Obstacles.add(new ApeTropicalTree());
			Obstacles.add(new ApeMonkeyBars());
			Obstacles.add(new ApeSkullSlope());
			Obstacles.add(new ApeRopeSwing());
			Obstacles.add(new ApeTreeDown());
		}
		
		private void setFood() {
			if (foodMode.isSelected()) {
				final String food = foodField.getText();
				foodID = split(food);
				Obstacles.add(new FoodHandler(foodID));
				eat = true;
			} else {
				foodID = new int[1];
				foodID[0] = 0;
			}
		}

		private void okButtonActionPerformed(ActionEvent e) {
			setValues();
			//saveProperties();
			dispose();
		}

		private void cancelButtonActionPerformed(ActionEvent e) {
			start = false;
			dispose();
		}

		private void initComponents() {
			//Properties props = loadProperties();
			dialogPane = new JPanel();
			contentPanel = new JPanel();
			label1 = new JLabel();
			tabbedPane1 = new JTabbedPane();
			panel1 = new JPanel();
			label2 = new JLabel();
			courseBox = new JComboBox();
			panel2 = new JPanel();
			foodMode = new JCheckBox();
			label3 = new JLabel();
			foodField = new JTextField();
			panel3 = new JPanel();
			slider1 = new JSlider();
			buttonBar = new JPanel();
			okButton = new JButton();
			cancelButton = new JButton();

			//======== this ========
			setTitle("AIOAgility GUI");
			Container contentPane = getContentPane();
			contentPane.setLayout(new BorderLayout());

			//======== dialogPane ========
			{
				dialogPane.setBorder(new EmptyBorder(12, 12, 12, 12));
				dialogPane.setLayout(new BorderLayout());

				//======== contentPanel ========
				{
					contentPanel.setLayout(null);

					//---- label1 ----
					label1.setIcon(new ImageIcon(img1));
					label1.setText("Welcome to AIOAgility!");
					label1.setFont(new Font("Tahoma", Font.BOLD, 16));
					contentPanel.add(label1);
					label1.setBounds(0, 0, 360, 110);

					//======== tabbedPane1 ========
					{
						tabbedPane1.setBackground(new Color(204, 204, 204));

						//======== panel1 ========
						{
							panel1.setLayout(null);

							//---- label2 ----
							label2.setText("Please select a course below.");
							panel1.add(label2);
							label2.setBounds(new Rectangle(new Point(5, 5), label2.getPreferredSize()));

							//---- courseBox ----
							courseBox.setModel(new DefaultComboBoxModel(new String[] {
								"Barbarian basic",
								"Barbarian advanced",
								"Gnome basic",
								"Gnome advanced",
								"Ape atol"
							}));
							//if (props.getProperty("courseBox") != null) {
								//courseBox.setSelectedItem(props.get("courseBox"));
							//}
							panel1.add(courseBox);
							courseBox.setBounds(5, 25, 125, courseBox.getPreferredSize().height);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < panel1.getComponentCount(); i++) {
									Rectangle bounds = panel1.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = panel1.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								panel1.setMinimumSize(preferredSize);
								panel1.setPreferredSize(preferredSize);
							}
						}
						tabbedPane1.addTab("Course Options", panel1);


						//======== panel2 ========
						{
							panel2.setLayout(null);

							//---- foodMode ----
							foodMode.setText("Tick this box to enable eating.");
							//if (props.getProperty("foodMode") != null) {
								//foodMode.setSelected(Boolean.valueOf(props.getProperty("foodMode")));
							//}
							panel2.add(foodMode);
							foodMode.setBounds(new Rectangle(new Point(5, 5), foodMode.getPreferredSize()));

							//---- label3 ----
							label3.setText("Enter food IDs here seperated by a comma");
							panel2.add(label3);
							label3.setBounds(new Rectangle(new Point(10, 35), label3.getPreferredSize()));
							
							//---- foodField ---
							//if (props.getProperty("foodID") != null) {
								//foodField.setText(props.getProperty("foodID"));
							//} else {
								//foodField.setText("7220,7218");
							//}
							panel2.add(foodField);
							foodField.setBounds(235, 30, 110, foodField.getPreferredSize().height);

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < panel2.getComponentCount(); i++) {
									Rectangle bounds = panel2.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = panel2.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								panel2.setMinimumSize(preferredSize);
								panel2.setPreferredSize(preferredSize);
							}
						}
						tabbedPane1.addTab("Food Options", panel2);


						//======== panel3 ========
						{
							panel3.setLayout(null);

							//---- slider1 ----
							slider1.setMaximum(12);
							slider1.setMinorTickSpacing(1);
							slider1.setMajorTickSpacing(1);
							slider1.setMinimum(1);
							slider1.setPaintTicks(true);
							slider1.setPaintLabels(true);
							slider1.setSnapToTicks(true);
							//if (props.getProperty("slider1") != null) {
								//slider1.setValue(Integer.parseInt(props.getProperty("slider1")));
							//}
							panel3.add(slider1);
							slider1.setBounds(new Rectangle(new Point(70, 10), slider1.getPreferredSize()));

							{ // compute preferred size
								Dimension preferredSize = new Dimension();
								for(int i = 0; i < panel3.getComponentCount(); i++) {
									Rectangle bounds = panel3.getComponent(i).getBounds();
									preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
									preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
								}
								Insets insets = panel3.getInsets();
								preferredSize.width += insets.right;
								preferredSize.height += insets.bottom;
								panel3.setMinimumSize(preferredSize);
								panel3.setPreferredSize(preferredSize);
							}
						}
						tabbedPane1.addTab("Mouse Speed", panel3);

					}
					contentPanel.add(tabbedPane1);
					tabbedPane1.setBounds(0, 110, 360, 90);

					{ // compute preferred size
						Dimension preferredSize = new Dimension();
						for(int i = 0; i < contentPanel.getComponentCount(); i++) {
							Rectangle bounds = contentPanel.getComponent(i).getBounds();
							preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
							preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
						}
						Insets insets = contentPanel.getInsets();
						preferredSize.width += insets.right;
						preferredSize.height += insets.bottom;
						contentPanel.setMinimumSize(preferredSize);
						contentPanel.setPreferredSize(preferredSize);
					}
				}
				dialogPane.add(contentPanel, BorderLayout.WEST);

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
			setSize(400, 300);
			setLocationRelativeTo(getOwner());
			setVisible(true);
		}

		private JPanel dialogPane;
		private JPanel contentPanel;
		private JLabel label1;
		private JTabbedPane tabbedPane1;
		private JPanel panel1;
		private JLabel label2;
		private JComboBox courseBox;
		private JPanel panel2;
		private JCheckBox foodMode;
		private JLabel label3;
		private JTextField foodField;
		private JPanel panel3;
		private JSlider slider1;
		private JPanel buttonBar;
		private JButton okButton;
		private JButton cancelButton;
	}
	
}
