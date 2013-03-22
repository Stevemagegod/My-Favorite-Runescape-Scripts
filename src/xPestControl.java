import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@ScriptManifest(authors = "Chad [Xeroday]", name = "xPestControl", version = 1.78, website = "http://node13.info/xeroday/pages/xpestcontrol.php", description = "Flawless pest control. Start near the gangplank.")
public class xPestControl extends Script implements PaintListener, MouseListener, MessageListener {

    String state = "Loading up...";
    public static final String version = Double.toString(xPestControl.class.getAnnotation(ScriptManifest.class).version());
    public static final String URL = "http://node13.info/xeroday/pages/xpestcontrol.php";
    private final RenderingHints antialiasing = new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    RSTile joinTile = null;
    RSTile boatTile = null;
    public int gangplankID;
    private static final RSTile joinNovice = new RSTile(2657, 2639);
    private static final RSTile boatNovice = new RSTile(2661, 2639);
    public int gangplankNovice = 14315;
    private final static RSTile joinIntermediate = new RSTile(2644, 2644);
    private final static RSTile boatIntermediate = new RSTile(2640, 2644);
    private final static int gangplankIntermediate = 25631;
    private final static RSTile joinVeteran = new RSTile(2638, 2653);
    private final static RSTile boatVeteran = new RSTile(2634, 2653);
    private final static int gangplankVeteran = 25632;
    private RSTile knightTile;
    private RSTile W;
    private RSTile E;
    private RSTile SE;
    private RSTile SW;
    private int oW = 0;
    private int oE = 0;
    private int oSE = 0;
    private int oSW = 0;
    public int inGame = 0;
    public int speed = -1337;
    private final static RSArea islandArea = new RSArea(new RSTile(2500, 2500), new RSTile(2800, 2700));
    private static int squireID = 3781;
    RSNPC squire;
    RSNPC knight;
    RSNPC check;
    RSNPC target;
    RSObject gangplank;
    private static final Color White = new Color(255, 255, 255);
    private static final Font font1 = new Font("Arial", 1, 12);
    private static final Font font2 = new Font("Arial", 1, 18);
    private final Image paintIMG = getImage("http://i.imgur.com/BHJLl.png");
    public long startTime = System.currentTimeMillis();
    long runTime;
    long seconds;
    long minutes;
    long hours;
    int currentXP;
    int startXP;
    int gainedXP;
    int xpPerHour;
    int pointsStart = 1337;
    int pointsGained;
    int pointsPerHour;
    public int useAntiban = 0;
    public int useSpecial = 0;
    public int usePrayer = 0;
    public int attackPortals = 0;
    public boolean showPaint = true;
    public int spec = 0;
    public int pray = 0;
    private String text;
    private static final Color MOUSE_COLOR = new Color(0, 255, 255),
            MOUSE_BORDER_COLOR = new Color(220, 220, 220),
            MOUSE_CENTER_COLOR = new Color(89, 255, 89);
    private boolean pressed = false;
    private int absoluteY = 0;

    private static enum State {

        SLEEP, Gangplank, WalkCenter, Fight, WalkSouth, Lost, WalkPortal
    }

    @Override
    public boolean onStart() {
        log("Welcome to xPestControl " + version + ".");
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {

                    Project gui = new Project();
                    gui.setVisible(true);

                }
            });
        } catch (Throwable ignore) {
            log.severe("Error 1");
        }
        while (speed < 0 && joinTile == null && boatTile == null) {
            state = "Waiting for GUI";
            sleep(200);
        }
        if (useAntiban > 0) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        AB.setVisible(true);
                    }
                });
            } catch (Throwable ignore) {
                log.severe("Error 2");
            }
        }
        while (AB.isVisible()) {
            sleep(200);
        }
        mouse.setSpeed(speed);
        startTime = System.currentTimeMillis();
        startXP = skills.getCurrentExp(Skills.ATTACK) + skills.getCurrentExp(Skills.CONSTITUTION) + skills.getCurrentExp(Skills.DEFENSE) + skills.getCurrentExp(Skills.STRENGTH);
        log("Info: Make sure to raise your camera to the highest view.");
        log("Info: Click GUI to show/hide.");
        log("Info: It is suggested to disable randoms fix clan vexilla.");
        return true;
    }

    @Override
    public void onFinish() {
        log("Thanks for using xPestControl!");
    }

    private State getState() {
        if (players.getMyPlayer() != null && game.isLoggedIn()) {
          
            /*
             * while (camera.getPitch() < 90) { //camera.setPitch(100);
             * log("Please raise your camera to the highest view."); state =
             * "Adjust camera"; sleep(1000); }
             */
            if (calc.distanceTo(joinTile) > 200 || !islandArea.contains(getMyPlayer().getLocation())) {
                knight = npcs.getNearest("Void Knight");
                if (knightTile != null) {
                    if (calc.distanceBetween(knightTile, joinTile) < 100) {
                        knightTile = null;
                    }
                }
                if (knight == null && knightTile == null) {
                    return State.WalkSouth;
                } else if (knight != null && (oW + oSW + oSE + oE < 1)) {
                    knightTile = knight.getLocation();
                    W = new RSTile(knightTile.getX() - 25, knightTile.getY());
                    SW = new RSTile(knightTile.getX() - 10, knightTile.getY() - 20);
                    E = new RSTile(knightTile.getX() + 23, knightTile.getY() - 3);
                    SE = new RSTile(knightTile.getX() + 14, knightTile.getY() - 19);
                    if (!knight.isOnScreen() && (oW + oSW + oSE + oE < 1)) {
                        return State.WalkCenter;
                    } else if ((calc.distanceTo(knight.getLocation()) <= 5 || knight.isOnScreen())) {
                        return State.Fight;
                    }
                } else if (oW + oSW + oSE + oE >= 1) {
                    if (attackPortals > 0) {
                        check = npcs.getNearest("Shifter", "Brawler", "Defiler", "Ravager", "Torcher", "Spinner", "Portal");
                    } else {
                        check = npcs.getNearest("Shifter", "Brawler", "Defiler", "Ravager", "Torcher", "Spinner");
                    }
                    if (calc.distanceTo(W) > 9 && calc.distanceTo(SW) > 9 && calc.distanceTo(E) > 9 && calc.distanceTo(SE) > 9 && check == null) {
                        if (W != null) {
                            return State.WalkPortal;
                        } else {
                            return State.WalkSouth;
                        }
                    } else {
                        if (check != null) {
                            if (calc.distanceTo(check.getLocation()) < 10) {
                                return State.Fight;
                            } else {
                                walking.walkTo(check.getLocation());
                                sleep(1000, 1500);
                            }
                        } else {
                            return State.WalkPortal;
                        }
                    }
                }
            } else if (calc.distanceTo(joinTile) < 2) {
                reset();
                return State.Gangplank;
            } else if (calc.distanceTo(boatTile) < 2) {
                if (pointsStart == 1337) {
                    text = interfaces.getComponent(407, 16).getText();
                    text = text.replace("Commendations: ", "");
                    pointsStart = Integer.parseInt(text);
                } else {
                    text = interfaces.getComponent(407, 16).getText();
                    text = text.replace("Commendations: ", "");
                    if (Integer.parseInt(text) == 500) {
                        log("Max points. Stopping script & taking screenshot...");
                        env.saveScreenshot(true);
                        stopScript(false);
                    }
                    pointsGained = Integer.parseInt(text) - pointsStart;
                }
                return State.SLEEP;
            } else {
                return State.Lost;
            }
        } else {
            state = "Player not detected";
            return State.SLEEP;
        }
        return State.SLEEP;
    }

    @Override
    public int loop() {
        if (useAntiban == 1) {
            Antiban();
            Antiban2();
            if (AB.AFKTimer.isRunning()) {
                return 200;
            }
        }
        RSPlayer me = players.getMyPlayer();
        if (interfaces.getComponent(244, 4).containsText("must defend the ")) {
            inGame = 1;
        } else if (islandArea.contains(me.getLocation())) {
            knightTile = null;
            inGame = 0;
        }
        if (knightTile != null) {
            if (islandArea.contains(knightTile)) {
                knightTile = null;
            }
        }
        State currentState = getState();
        switch (currentState) {
            case SLEEP:
                state = "Sleeping";
                sleep(333);
                break;
            case Gangplank:
                state = "Joining";
                gangplank = objects.getNearest(gangplankID);
                if (gangplank != null) {
                    if (gangplank.isOnScreen()) {
                        mouse.click(gangplank.getPoint(), 1, 1, true);
                        sleep(666, 1337);
                    }
                }
                if (interfaces.getComponent(1107).isValid()) {
                    interfaces.getComponent(1107, 157).doClick();
                    sleep(2000);
                }
                break;
            case WalkSouth:
                activate();
                state = "Walking south";
                walking.walkTileMM(new RSTile(me.getLocation().getX(), me.getLocation().getY() - random(8, 10)));
                sleep(2333, 3000);
                break;
            case WalkPortal:
                state = "Walking to portal";
                if (oW == 1) {
                    for (int i = 0; i < 5; i++) {
                        if (calc.distanceTo(W) > 3 && !islandArea.contains(me.getLocation())) {
                            walking.walkTo(W);
                            sleep(1000, 1700);
                        }
                    }
                } else if (oE == 1) {
                    for (int i = 0; i < 5; i++) {
                        if (calc.distanceTo(E) > 3 && !islandArea.contains(me.getLocation())) {
                            walking.walkTo(E);
                            sleep(1700, 2600);
                        }
                    }
                } else if (oSW == 1) {
                    for (int i = 0; i < 3; i++) {
                        if (calc.distanceTo(SW) > 3 && !islandArea.contains(me.getLocation())) {
                            walking.walkTo(SW);
                            sleep(1000, 1600);
                        }
                    }
                } else if (oSE == 1) {
                    for (int i = 0; i < 3; i++) {
                        if (calc.distanceTo(SE) > 3 && !islandArea.contains(me.getLocation())) {
                            walking.walkTo(SE);
                            sleep(1000, 1600);
                        }
                    }
                }
                break;
            case WalkCenter:
                state = "Walking to center";
                knight = npcs.getNearest("Void Knight");
                squire = npcs.getNearest(squireID);
                if (knight != null || squire != null) {
                    if (calc.distanceTo(knightTile) < 15) {
                        walking.walkTileMM(knightTile, 1, 1);
                        sleep(3333, 4444);
                    } else {
                        walking.walkTileMM(new RSTile(me.getLocation().getX(), me.getLocation().getY() - random(6, 8)));
                        sleep(2333, 3000);
                    }
                }
                break;
            case Fight:
                state = "Fighting";
                if (me.getInteracting() == null || !me.isInCombat()) {
                    if (attackPortals > 0) {
                        target = npcs.getNearest("Shifter", "Brawler", "Defiler", "Ravager", "Torcher", "Spinner", "Portal");
                    } else {
                        target = npcs.getNearest("Shifter", "Brawler", "Defiler", "Ravager", "Torcher", "Spinner");
                    }
                    if (target != null && me.getAnimation() < 0 && !me.isMoving()) {
                        if (target.isOnScreen()) {
                            if (target.getName().contains("Portal")) {
                                if (target.isInCombat()) {
                                    mouse.click(target.getPoint(), 1, 1, true);
                                    sleep(200, 333);
                                } else {
                                    sleep(912, 1337);
                                    break;
                                }
                            } else {
                                AttackonPoint(target.getPoint());
                                sleep(200, 333);
                            }
                        } else {
                            if (oW + oSW + oSE + oE >= 1) {
                                if (calc.distanceTo(target.getLocation()) < 13) {
                                    walking.walkTileMM(target.getLocation());
                                    sleep(1500, 2000);
                                }
                            }
                        }
                    }
                }
                break;
            case Lost:
                sleep(1400, 1800);
                if (islandArea.contains(me.getLocation())) {
                    state = "Attempting to fix";
                    walking.walkTo(joinTile);
                    sleep(1000, 1500);
                } else if (knightTile != null) {
                    state = "Attempting to fix";
                    walking.walkTo(knightTile);
                    sleep(1000, 1500);
                } else {
                    break;
                }

        }
        return 300;
    }

    private void Antiban2() {
		// TODO Auto-generated method stub
		
	}

	private void Antiban() {
		int r = random(0, 20);
    	switch (r) {
    	case 1:
    		break;
    	case 3:
    		mouse.moveSlightly();
    		sleep(300, 600);
    		break;
    	case 5:
    		camera.setAngle(random(0, 360));
    		camera.setPitch(random(8000, 12000));
    		break;
    	case 6:
    		break;
    	case 7:
    		mouse.moveRandomly(10, 50);
    		break;
    	case 8:
    		break;
    	case 9:
    		break;
    	case 10:
    		mouse.moveOffScreen();
    		sleep (random(2000, 3000));
    		break;
    	case 12:
    		break;
    	case 15:
    		break;
		case 16:
    		mouse.moveSlightly();
    		sleep(300, 600);
    		break;
    	case 17:
    		break;
    	}
    }

	@SuppressWarnings("static-access")
	public void activate() {
        if (useSpecial > 0) {
            if (spec == 0) {
                state = "Special attack";
                game.openTab(game.getTab().ATTACK);
                sleep(500, 1000);
                interfaces.getComponent(884, 30).doClick();
                spec++;
                sleep(333, 666);
            }
        }
        if (usePrayer > 0) {
            if (pray == 0) {
                state = "Quick prayer";
                interfaces.getComponent(749, 2).doClick();
                pray++;
                sleep(333, 666);
            }
        }
    }

    private boolean AttackonPoint(Point point) { //credits to Whitebear
        if (!this.calc.pointOnScreen(point)) {
            return false;
        }
        try {
            boolean stop = false;
            for (int i = 0; i <= 50; i++) {
                this.mouse.move(point);
                Object[] menuItems = this.menu.getItems();
                for (Object menuItem : menuItems) {
                    if (menuItem.toString().contains("Attack")) {
                        stop = true;
                        break;
                    }
                }
                if (stop) {
                    break;
                }
            }
            return this.menu.doAction("Attack");
        } catch (Exception localException) {
        }
        return false;
    }

    @Override
    public void messageReceived(MessageEvent e) {
        String x = e.getMessage();
        if (!islandArea.contains(players.getMyPlayer().getLocation())) {
            if (x.contains("The purple, western portal shield has dropped!")) {
                oW = 1;
                oE = 0;
                oSW = 0;
                oSE = 0;
            } else if (x.contains("The blue, eastern portal shield has dropped!")) {
                oW = 0;
                oE = 1;
                oSW = 0;
                oSE = 0;
            } else if (x.contains("The yellow, south-eastern portal shield has dropped!")) {
                oW = 0;
                oE = 0;
                oSW = 0;
                oSE = 1;
            } else if (x.contains("The red, south-western portal shield has dropped!")) {
                oW = 0;
                oE = 0;
                oSW = 1;
                oSE = 0;
            } else if (x.contains("Oh dear, you are dead!")) {
                spec = 0;
                pray = 0;
                sleep(1000, 1400);
                activate();
                walking.walkTo(knightTile);
                sleep(2000, 2500);
            }
        }
    }

    public void paintTile(final Graphics g, final RSTile tile, final Color tileColor, final String caption, final Color captionColor) {
        if (calc.tileOnScreen(tile)) {
            final RSTile tx = new RSTile(tile.getX() + 1, tile.getY());
            final RSTile ty = new RSTile(tile.getX(), tile.getY() + 1);
            final RSTile txy = new RSTile(tile.getX() + 1, tile.getY() + 1);
            final Point pn = calc.tileToScreen(tile, 0, 0, 0);
            final Point px = calc.tileToScreen(tx, 0, 0, 0);
            final Point py = calc.tileToScreen(ty, 0, 0, 0);
            final Point pxy = calc.tileToScreen(txy, 0, 0, 0);
            if (pn.x != -1 && pn.y != -1 && px.x != -1 && px.y != -1 && py.x != -1 && py.y != -1 && pxy.x != -1 && pxy.y != -1) {
                g.setColor(new Color(0, 0, 0, 255));
                g.drawPolygon(new int[]{py.x, pxy.x, px.x, pn.x}, new int[]{py.y, pxy.y, px.y, pn.y}, 4);
                g.setColor(tileColor);
                g.fillPolygon(new int[]{py.x, pxy.x, px.x, pn.x}, new int[]{py.y, pxy.y, px.y, pn.y}, 4);
                g.setColor(captionColor);
                g.setFont(new Font("Arial", Font.BOLD, 11));
                g.drawString(caption, Math.max(Math.max(pn.x, px.x), Math.max(py.x, pxy.x)) + 5, Math.min(Math.min(pn.y, px.y), Math.min(py.y, pxy.y)) + 15);
            }
        }
    }

    public void reset() {
        oW = 0;
        oE = 0;
        oSE = 0;
        oSW = 0;
        W = null;
        SW = null;
        E = null;
        SE = null;
        knightTile = null;
        pray = 0;
        spec = 0;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        RSComponent inter = interfaces.get(137).getComponent(0);
        if (inter.getArea().contains(e.getPoint())) {
            showPaint = !showPaint;
        }
    }

    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (IOException e) {
            return null;
        }
    }

    private void drawMouse(Graphics g) {
        ((Graphics2D) g).setRenderingHints(new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON));
        Point p = mouse.getLocation();
        Graphics2D spinG = (Graphics2D) g.create();
        Graphics2D spinGRev = (Graphics2D) g.create();
        Graphics2D spinG2 = (Graphics2D) g.create();
        spinG.setColor(MOUSE_BORDER_COLOR);
        spinGRev.setColor(MOUSE_COLOR);
        spinG.rotate(System.currentTimeMillis() % 2000d / 2000d * (360d) * 2
                * Math.PI / 180.0, p.x, p.y);
        spinGRev.rotate(System.currentTimeMillis() % 2000d / 2000d * (-360d)
                * 2 * Math.PI / 180.0, p.x, p.y);
        final int outerSize = 20;
        final int innerSize = 12;
        spinG.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        spinGRev.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        spinG.drawArc(p.x - (outerSize / 2), p.y - (outerSize / 2), outerSize,
                outerSize, 100, 75);
        spinG.drawArc(p.x - (outerSize / 2), p.y - (outerSize / 2), outerSize,
                outerSize, -100, 75);
        spinGRev.drawArc(p.x - (innerSize / 2), p.y - (innerSize / 2),
                innerSize, innerSize, 100, 75);
        spinGRev.drawArc(p.x - (innerSize / 2), p.y - (innerSize / 2),
                innerSize, innerSize, -100, 75);
        g.setColor(MOUSE_CENTER_COLOR);
        g.fillOval(p.x, p.y, 2, 2);
        spinG2.setColor(MOUSE_CENTER_COLOR);
        spinG2.rotate(System.currentTimeMillis() % 2000d / 2000d * 360d
                * Math.PI / 180.0, p.x, p.y);
        spinG2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));
        spinG2.drawLine(p.x - 5, p.y, p.x + 5, p.y);
        spinG2.drawLine(p.x, p.y - 5, p.x, p.y + 5);
    }

    @Override
    public void onRepaint(Graphics g1) {
        runTime = System.currentTimeMillis() - startTime;
        seconds = runTime / 1000;
        if (seconds >= 60) {
            minutes = seconds / 60;
            seconds -= (minutes * 60);
        }
        if (minutes >= 60) {
            hours = minutes / 60;
            minutes -= (hours * 60);
        }
        currentXP = skills.getCurrentExp(Skills.ATTACK) + skills.getCurrentExp(Skills.CONSTITUTION) + skills.getCurrentExp(Skills.DEFENSE) + skills.getCurrentExp(Skills.STRENGTH);
        gainedXP = currentXP - startXP;
        xpPerHour = (int) ((3600000.0 / (double) runTime) * gainedXP);
        pointsPerHour = (int) ((3600000.0 / (double) runTime) * pointsGained);
        Graphics2D g = (Graphics2D) g1;
        g.setRenderingHints(antialiasing);
        if (showPaint) {
            g.drawImage(paintIMG, 0, 0, null);
            g.setFont(font2);
            g.setColor(White);
            g.drawString("Time running: " + hours + ":" + minutes + ":" + seconds, 200, 370);
            g.drawString("State: " + state, 200, 400);
            g.setFont(font1);
            g.setColor(White);
            g.drawString("Points gained: " + pointsGained, 200, 430);
            g.drawString("Points per hour: " + pointsPerHour, 200, 445);
            g.drawString("XP gained: " + gainedXP, 350, 430);
            g.drawString("XP per hour: " + xpPerHour, 350, 445);
        }

        drawMouse(g);
        try {
            paintTile(g, knightTile, new Color(0, 255, 0, 80), "Void Knight", new Color(0, 255, 0, 255));
        } catch (NullPointerException x) {
        }
        try {
            paintTile(g, target.getLocation(), new Color(255, 0, 0, 80), "Target", new Color(255, 0, 0, 255));
        } catch (NullPointerException x) {
        }
        try {
            paintTile(g, walking.getDestination(), new Color(0, 255, 0, 80), "Destination", new Color(0, 255, 0, 255));
        } catch (NullPointerException x) {
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
        Point mp = arg0.getPoint();
        final Rectangle toggleRectangle = new Rectangle(493, absoluteY + 3, 16,
                15);
        if (toggleRectangle.contains(mp)) {
            pressed = !pressed;
        }
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    class Project extends JFrame {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public Project() {
            initComponents();
        }

        private void button1ActionPerformed(ActionEvent e) throws URISyntaxException, IOException {
            java.awt.Desktop myNewBrowserDesktop = java.awt.Desktop.getDesktop();
            java.net.URI myNewLocation = new java.net.URI(URL);
            myNewBrowserDesktop.browse(myNewLocation);
        }

        private void Start(ActionEvent e) {
            if (comboBox1.getSelectedIndex() == 0) {
                joinTile = joinNovice;
                boatTile = boatNovice;
                gangplankID = gangplankNovice;
                log("Settings: Novice boat.");
            } else if (comboBox1.getSelectedIndex() == 1) {
                joinTile = joinIntermediate;
                boatTile = boatIntermediate;
                gangplankID = gangplankIntermediate;
                log("Settings: Intermediate boat.");
            } else if (comboBox1.getSelectedIndex() == 2) {
                joinTile = joinVeteran;
                boatTile = boatVeteran;
                gangplankID = gangplankVeteran;
                log("Settings: Veteran boat.");
            }
            if (settingsAntiban.isSelected()) {
                useAntiban = 1;
                log("Settings: Using antiban.");
            }
            if (settingsSpecial.isSelected()) {
                useSpecial = 1;
                log("Settings: Using special attacks.");
            }
            if (settingsPrayer.isSelected()) {
                usePrayer = 1;
                log("Settings: Using quick prayer.");
            }
            if (settingsPortals.isSelected()) {
                attackPortals = 1;
                log("Settings: Attacking portals.");
            }

            speed = Integer.parseInt(textField1.getText());

            this.setVisible(false);
        }

        private void initComponents() {
            textField1 = new JTextField();
            panel1 = new JPanel();
            button1 = new JButton();
            button2 = new JButton();
            label1 = new JLabel();
            settingsAntiban = new JCheckBox();
            settingsSpecial = new JCheckBox();
            settingsPrayer = new JCheckBox();
            settingsPortals = new JCheckBox();
            comboBox1 = new JComboBox();

            //default toggles
            settingsPrayer.setSelected(true);
            settingsPortals.setSelected(true);

            //======== this ========
            setTitle("xPestControl GUI");
            Container contentPane = getContentPane();
            contentPane.setLayout(null);

            //---- textField1 ----
            textField1.setText("7");
            contentPane.add(textField1);
            textField1.setBounds(160, 40, 30, 30);

            //======== panel1 ========
            {

                // JFormDesigner evaluation mark
                panel1.setBorder(new javax.swing.border.CompoundBorder(
                        new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(0, 0, 0, 0),
                        "JFormDesigner Evaluation", javax.swing.border.TitledBorder.CENTER,
                        javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Dialog", java.awt.Font.BOLD, 12),
                        java.awt.Color.red), panel1.getBorder()));
                panel1.addPropertyChangeListener(new java.beans.PropertyChangeListener() {

                    public void propertyChange(java.beans.PropertyChangeEvent e) {
                        if ("border".equals(e.getPropertyName())) {
                            throw new RuntimeException();
                        }
                    }
                });

                panel1.setLayout(null);

                { // compute preferred size
                    Dimension preferredSize = new Dimension();
                    for (int i = 0; i < panel1.getComponentCount(); i++) {
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
            contentPane.add(panel1);
            panel1.setBounds(110, 100, panel1.getPreferredSize().width, 0);

            //---- button1 ----
            button1.setText("View thread & changelog");
            button1.setFont(new Font("Tahoma", Font.PLAIN, 10));
            button1.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        button1ActionPerformed(e);
                    } catch (URISyntaxException ex) {
                        log.severe(ex.toString());
                    } catch (IOException ex) {
                        log.severe(ex.toString());
                    }
                }
            });
            contentPane.add(button1);
            button1.setBounds(-5, 260, 210, button1.getPreferredSize().height);

            //---- button2 ----
            //button2.setText("Start & Check for Update");
            button2.setText("Start");
            button2.setFont(new Font("Tahoma", Font.PLAIN, 14));
            button2.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    Start(e);
                }
            });
            contentPane.add(button2);
            button2.setBounds(-5, 200, 210, 55);

            //---- label1 ----
            label1.setText("Mouse Speed (Lower is faster)");
            contentPane.add(label1);
            label1.setBounds(5, 50, 150, 15);

            //---- settingsAntiban ----
            settingsAntiban.setText("Use Antiban");
            contentPane.add(settingsAntiban);
            settingsAntiban.setBounds(new Rectangle(new Point(5, 75), settingsAntiban.getPreferredSize()));

            //---- settingsSpecial ----
            settingsSpecial.setText("Use Special Attack");
            contentPane.add(settingsSpecial);
            settingsSpecial.setBounds(new Rectangle(new Point(5, 100), settingsSpecial.getPreferredSize()));

            //---- settingsPrayer ----
            settingsPrayer.setText("Use Quick Prayer");
            contentPane.add(settingsPrayer);
            settingsPrayer.setBounds(new Rectangle(new Point(5, 125), settingsPrayer.getPreferredSize()));

            //---- settingsPortals ----
            settingsPortals.setText("Attack Portals");
            contentPane.add(settingsPortals);
            settingsPortals.setBounds(new Rectangle(new Point(5, 150), settingsPortals.getPreferredSize()));

            //---- comboBox1 ----
            comboBox1.setModel(new DefaultComboBoxModel(new String[]{
                        "Novice",
                        "Intermediate",
                        "Expert"
                    }));
            contentPane.add(comboBox1);
            comboBox1.setBounds(5, 10, 185, comboBox1.getPreferredSize().height);

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
        // Generated using JFormDesigner Evaluation license - Ji adf
        private JTextField textField1;
        private JPanel panel1;
        private JButton button1;
        private JButton button2;
        private JLabel label1;
        private JCheckBox settingsAntiban;
        private JCheckBox settingsSpecial;
        private JCheckBox settingsPrayer;
        private JCheckBox settingsPortals;
        private JComboBox comboBox1;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
    public AntiBan AB = new AntiBan();

    @SuppressWarnings({ })
	class AntiBan extends JFrame {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		/*
         * @Author Joe Titus, mod by Chad [Xeroday] @Date 5/8/2011
         */
        public Timer antiBanTimer = new Timer(0);
        public boolean customAB, AFK, Mouse, Camera, Tabs, LogOut, ABOn;
        public int ABFreq;
        public double ABFreqTime;
        Timer AFKTimer = new Timer(0);

        public void antiBanMethod() {
            if (ABOn) {
                if (!antiBanTimer.isRunning() && !AFKTimer.isRunning()) {
                    antiBan();
                    antiBanTimer.setEndIn((int) random(ABFreqTime * .33, ABFreqTime * 1.66));
                }
            }
        }

        @SuppressWarnings({ })
		public void antiBan() {
            int t = 0;
            if (Camera) {
                t++;
            };
            if (Tabs) {
                t++;
            };
            if (AFK) {
                t++;
            };
            if (Mouse) {
                t++;
            };
            t += 0;
            switch (random(1, t)) {
                case 1:
                    if (Camera) {
                        state = "Antiban";
                        camera.turnTo(getMyPlayer().getLocation().randomize(5, 5), 5);
                        sleep(random(500, 750));
                        return;
                    }
                case 2:
                    if (Tabs) {
                        switch (random(1, 4)) {
                            case 1:
                                state = "Antiban";
                                mouse.move((585 + random(-35, 35)), (330 + random(-90, 90)));
                                sleep(750, 3000);
                                sleep(random(500, 750));
                                return;

                            case 2:
                                state = "Antiban";
                                mouse.move((585 + random(-35, 35)), (330 + random(-90, 90)));
                                sleep(750, 1500);
                                sleep(random(500, 750));
                                return;

                            default:
                                state = "Antiban";
                                mouse.moveRandomly(random(1, 300));
                                sleep(750, 1500);
                                sleep(random(500, 750));
                                return;
                        }
                    }
                case 3:
                    if (AFK) {
                        state = "Antiban";
                        int r = random(1, 100);
                        if (r < 75) {
                            AFKTimer.setEndIn(random(3000, 5000));
                        } else if (r >= 70 && r < 90) {
                            AFKTimer.setEndIn(random(5000, 15000));
                        } else {
                            AFKTimer.setEndIn(random(10000, 30000));
                        }
                        return;
                    }
                case 4:
                    if (Mouse) {
                        int s = random(1, 4);
                        switch (s) {
                            case 1:
                                state = "Antiban";
                                mouse.moveOffScreen();
                                sleep(750, 1500);
                                return;
                            case 2:
                                state = "Antiban";
                                mouse.moveSlightly();
                                sleep(500, 1000);
                                return;
                            case 3:
                                state = "Antiban";
                                mouse.moveRandomly(random(1, 300));
                                sleep(750, 1500);
                                return;
                            default:
                                return;
                        }

                    }
                default:
                    return;
            }
        }

        public void setEnabled(boolean a, boolean b, boolean c, boolean d,
                boolean e, boolean f, boolean g, boolean h,
                boolean i, boolean j, boolean k, boolean l) {

            ABFreqSlider.setEnabled(a);
            ABOnOff.setEnabled(b);
            customAntiBanButton.setEnabled(c);
            ABLevelOptions.setEnabled(d);
            separator1.setEnabled(e);
            ABFreqLabel.setEnabled(f);
            AFKCheck.setEnabled(g);
            mouseMoveCheck.setEnabled(h);
            camMoveCheck.setEnabled(i);
            logOutCheck.setEnabled(j);
            openTabsCheck.setEnabled(k);
            ABLevelLabel.setEnabled(l);

        }

        public void getSettings() {
            if (!ABOnOff.isSelected()) {
                ABOn = true;
                if (customAntiBanButton.isSelected()) {
                    customAB = true;
                    ABFreq = ABFreqSlider.getValue();
                    AFK = AFKCheck.isSelected();
                    Mouse = mouseMoveCheck.isSelected();
                    Camera = camMoveCheck.isSelected();
                    Tabs = openTabsCheck.isSelected();
                    LogOut = logOutCheck.isSelected();
                } else {
                    customAB = false;
                    if (ABLevelOptions.getSelectedItem().toString().contains("Low")) {
                        ABFreq = 20;
                        LogOut = true;
                        Camera = true;
                        Mouse = true;

                    } else if (ABLevelOptions.getSelectedItem().toString().contains("Medium")) {
                        ABFreq = 40;
                        Mouse = true;
                        LogOut = true;
                        Camera = true;
                        AFK = true;

                    } else if (ABLevelOptions.getSelectedItem().toString().contains("High")) {
                        ABFreq = 60;
                        LogOut = true;
                        Camera = true;
                        Tabs = true;
                        AFK = true;
                        Mouse = true;
                    }
                }
                log("AntiBan on");
                ABFreqTime = ((double) (240 / ABFreq) * 15000);
                antiBanTimer = new Timer((int) random(ABFreqTime * .5, ABFreqTime * 1.5));
            } else {
                log("AntiBan off");
                ABOn = false;
            }

        }

        public void ABToggle() {
            if ((ABOnOff.isSelected())) {
                setEnabled(false, true, false, false, false, false, false, false, false, false, false, false);
            } else {
                if (customAntiBanButton.isSelected()) {
                    setEnabled(true, true, true, false, true, true, true, true, true, false, true, false);
                } else {
                    setEnabled(false, true, true, true, true, false, false, false, false, false, false, true);
                }
            }
        }

        public void cantCont() {
            if ((customAntiBanButton.isSelected() && !AFKCheck.isSelected() && !mouseMoveCheck.isSelected()
                    && !camMoveCheck.isSelected() && !openTabsCheck.isSelected()) || ABFreqSlider.getValue() == 0) {
                contButton.setEnabled(false);
            } else {
                contButton.setEnabled(true);
            }
        }

        public AntiBan() {
            initComponents();
        }

        private void ABFreqSliderStateChanged(ChangeEvent e) {
            cantCont();
        }

        private void ABOnOffStateChanged(ChangeEvent e) {
            ABOnOff.setToolTipText("The AntiBan is currently: " + content());
            ABOnOff.setText("Turn AntiBan " + OPcontent());
            ABToggle();
            cantCont();
        }

        private void customAntiBanButtonStateChanged(ChangeEvent e) {
            ABToggle();
            cantCont();
        }

        private void AFKCheckStateChanged(ChangeEvent e) {
            cantCont();
        }

        private void mouseMoveCheckStateChanged(ChangeEvent e) {
            cantCont();
        }

        private void camMoveCheckStateChanged(ChangeEvent e) {
            cantCont();
        }

        private void logOutCheckStateChanged(ChangeEvent e) {
            cantCont();
        }

        private void openTabsCheckStateChanged(ChangeEvent e) {
            cantCont();
        }

        private void contButtonActionPerformed(ActionEvent e) {
            getSettings();
            setVisible(false);
        }

        String content() {
            if (ABOnOff.isSelected()) {
                return "OFF";
            } else {
                return "ON";
            }
        }

        String OPcontent() {
            if (!ABOnOff.isSelected()) {
                return "OFF";
            } else {
                return "ON";
            }
        }

        private void initComponents() {
            ABFreqSlider = new JSlider();
            ABOnOff = new JToggleButton();
            customAntiBanButton = new JRadioButton();
            ABLevelOptions = new JComboBox();
            separator1 = new JSeparator();
            ABFreqLabel = new JLabel();
            AFKCheck = new JRadioButton();
            mouseMoveCheck = new JRadioButton();
            camMoveCheck = new JRadioButton();
            logOutCheck = new JRadioButton();
            openTabsCheck = new JRadioButton();
            ABLevelLabel = new JLabel();
            contButton = new JButton();

            addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    stopScript();
                }
            });
            //======== this ========
            setTitle("AntiBan");
            Container contentPane = getContentPane();
            //---- ABFreqSlider ----
            ABFreqSlider.setMajorTickSpacing(40);
            ABFreqSlider.setSnapToTicks(true);
            ABFreqSlider.setPaintTicks(true);
            ABFreqSlider.setBackground(new Color(238, 238, 238));
            ABFreqSlider.setEnabled(false);
            ABFreqSlider.setPaintLabels(true);
            ABFreqSlider.setMinorTickSpacing(8);
            ABFreqSlider.setMaximum(240);
            ABFreqSlider.setValue(60);
            ABFreqSlider.setToolTipText("Allows you to select the average amount of AntiBans per hour");
            ABFreqSlider.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ABFreqSliderStateChanged(e);
                }
            });

            //---- ABOnOff ----
            ABOnOff.setText("Turn AntiBan Off");

            ABOnOff.setToolTipText("The AntiBan is currently: ON");
            ABOnOff.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    ABOnOffStateChanged(e);
                }
            });

            //---- customAntiBanButton ----
            customAntiBanButton.setText("Customized AntiBan");
            customAntiBanButton.setToolTipText("Enables you to customize your AntiBan");
            customAntiBanButton.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    customAntiBanButtonStateChanged(e);
                }
            });

            //---- ABLevelOptions ----
            ABLevelOptions.setModel(new DefaultComboBoxModel(new String[]{
                        "Low (20 P/H)",
                        "Medium (40 P/H)",
                        "High (60 P/H)"
                    }));
            ABLevelOptions.setSelectedIndex(2);
            ABLevelOptions.setToolTipText("Selects the level of AntiBan");
            //---- separator1 ----
            separator1.setForeground(new Color(100, 100, 100));
            //---- ABFreqLabel ----
            ABFreqLabel.setText("Avg. AntiBans P/H");
            ABFreqLabel.setToolTipText("Average. AntiBans per hour");
            ABFreqLabel.setEnabled(false);

            //---- AFKCheck ----
            AFKCheck.setText("AFK's");
            AFKCheck.setEnabled(false);
            AFKCheck.setToolTipText("If checked, the AntiBan will perform AFK's");
            AFKCheck.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    AFKCheckStateChanged(e);
                }
            });

            //---- mouseMoveCheck ----
            mouseMoveCheck.setText("Mouse MoveMents");
            mouseMoveCheck.setEnabled(false);
            mouseMoveCheck.setToolTipText("If checked, the AntiBan will move the mouse");
            mouseMoveCheck.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    mouseMoveCheckStateChanged(e);
                }
            });

            //---- camMoveCheck ----
            camMoveCheck.setText("Camera Movements");
            camMoveCheck.setEnabled(false);
            camMoveCheck.setToolTipText("If checked, the AntiBan will move the camera");
            camMoveCheck.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    camMoveCheckStateChanged(e);
                }
            });

            //---- logOutCheck ----
            logOutCheck.setText("Log Out if Innactive");
            logOutCheck.setEnabled(false);
            logOutCheck.setVisible(false);
            logOutCheck.setToolTipText("Currently under construction");
            logOutCheck.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    logOutCheckStateChanged(e);
                }
            });

            //---- openTabsCheck ----
            openTabsCheck.setText("Open Tabs");
            openTabsCheck.setEnabled(false);
            openTabsCheck.setToolTipText("If checked, the AntiBan will open your friends list tab");
            openTabsCheck.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    openTabsCheckStateChanged(e);
                }
            });

            //---- ABLevelLabel ----
            ABLevelLabel.setText("AntiBan Level");
            ABLevelLabel.setToolTipText("AntiBan Level");

            //---- contButton ----
            contButton.setText("Continue");
            contButton.setToolTipText("Starts the Script");
            contButton.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    contButtonActionPerformed(e);
                }
            });

            GroupLayout contentPaneLayout = new GroupLayout(contentPane);
            contentPane.setLayout(contentPaneLayout);
            contentPaneLayout.setHorizontalGroup(
                    contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGap(102, 102, 102).addComponent(ABOnOff, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)).addGroup(contentPaneLayout.createSequentialGroup().addContainerGap().addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addComponent(ABFreqLabel).addGap(12, 12, 12).addComponent(ABFreqSlider, GroupLayout.PREFERRED_SIZE, 260, GroupLayout.PREFERRED_SIZE)).addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup().addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(ABLevelLabel).addComponent(customAntiBanButton, GroupLayout.PREFERRED_SIZE, 159, GroupLayout.PREFERRED_SIZE)).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGap(18, 18, 18).addComponent(separator1, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)).addGroup(contentPaneLayout.createSequentialGroup().addGap(27, 27, 27).addComponent(ABLevelOptions, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))).addGap(96, 96, 96)))).addGroup(contentPaneLayout.createSequentialGroup().addGap(20, 20, 20).addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false).addGroup(contentPaneLayout.createSequentialGroup().addComponent(mouseMoveCheck).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(logOutCheck)).addGroup(contentPaneLayout.createSequentialGroup().addComponent(openTabsCheck).addGap(18, 18, 18).addComponent(AFKCheck).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addComponent(camMoveCheck)))).addGroup(contentPaneLayout.createSequentialGroup().addGap(109, 109, 109).addComponent(contButton, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE))).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            contentPaneLayout.setVerticalGroup(
                    contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGap(10, 10, 10).addComponent(ABOnOff).addGap(18, 18, 18).addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(ABLevelLabel).addComponent(ABLevelOptions, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addGap(18, 18, 18).addComponent(customAntiBanButton, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)).addGroup(contentPaneLayout.createSequentialGroup().addGap(35, 35, 35).addComponent(separator1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))).addGroup(contentPaneLayout.createParallelGroup().addGroup(contentPaneLayout.createSequentialGroup().addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(ABFreqSlider, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)).addGroup(contentPaneLayout.createSequentialGroup().addGap(26, 26, 26).addComponent(ABFreqLabel))).addGap(15, 15, 15).addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(openTabsCheck).addComponent(AFKCheck).addComponent(camMoveCheck)).addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(logOutCheck).addComponent(mouseMoveCheck)).addGap(18, 18, 18).addComponent(contButton).addContainerGap(12, Short.MAX_VALUE)));
            pack();
            setLocationRelativeTo(getOwner());
            // JFormDesigner - End of component initialization  //GEN-END:initComponents
        }
        // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
        // Generated using JFormDesigner Evaluation license - Joe Titus
        private JSlider ABFreqSlider;
        private JToggleButton ABOnOff;
        private JRadioButton customAntiBanButton;
        private JComboBox ABLevelOptions;
        private JSeparator separator1;
        private JLabel ABFreqLabel;
        private JRadioButton AFKCheck;
        private JRadioButton mouseMoveCheck;
        private JRadioButton camMoveCheck;
        private JRadioButton logOutCheck;
        private JRadioButton openTabsCheck;
        private JLabel ABLevelLabel;
        private JButton contButton;
        // JFormDesigner - End of variables declaration  //GEN-END:variables
    }
}
