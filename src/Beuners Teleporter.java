/**Imports**/
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game;
import com.rarebot.script.methods.Magic;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.util.Timer;

/**Manifest**/
@ScriptManifest(
authors = {"Beuner"},
version = 1.2,
keywords = ("Beuner, magic, teleport, varrock, camelot"),
description = "Teleports to every teleport location in the normal spellbook",
name = "Beuners Teleporter"
)

/**Main Class**/
public class BeunersTeleporterAIO extends Script implements PaintListener, MessageListener {
  
	/**Variables**/
	public int airStaff = 1381;
	public int lawRune = 563;
	public int fireRune = 554;
	public int earthRune = 557;
	public int waterRune = 555;
	public boolean varTele; 
    public boolean falTele; 
    public boolean lumTele; 
    public boolean camTele; 
    public boolean MATele; 
    public boolean POHTele; 
    public boolean trollTele; 
    public boolean ardTele; 
    public boolean watchTele;
    
    
    public long startTime;
    public int startExp;
    public String Status = "Loading Script...";
    public String string;
    public Timer timer = new Timer(0);
    public int expGained;
    public int gainedExp;
    public int startLvl;
    public int currentLvl;
    public int xpHour;
    public int teleHour;
    public int totalTeles;
	private final ScriptManifest PROPS = getClass().getAnnotation(ScriptManifest.class);
    
    private static final Color MOUSE_COLOR = new Color(0, 0, 238), 
    		MOUSE_BORDER_COLOR = new Color(0, 0, 238), 
    		MOUSE_CENTER_COLOR = new Color(253, 245, 230); 
    		Point p; 
    		Point p2; 
    		private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
    		
    		final Object lock = new Object(); 
    		final ArrayList<Particle> particles = new ArrayList<Particle>();
    		Point p5;




    
    BeunTeleporterGUI g = new BeunTeleporterGUI(); 
    public boolean guiWait = true;
    
    /**Casting stuff**/
    public enum Methods { 
        tele, sleep, stop 
} 

public Methods getMethod() { 
        if (!magic.isSpellSelected()) { 
                return Methods.tele; 
        } 
        if (!inventory.contains(lawRune) || !equipment.containsOneOf(airStaff)) { 
                return Methods.stop; 
        } 
        return Methods.sleep; 
}

/**OnStart**/
	public boolean onStart() {
        g.setVisible(true);
		log(new Color(0, 0, 238), "Pick your spell!");
        while(guiWait){ 
            sleep(10);
    }
		log(new Color(0, 0, 238), "Welcome to Beuners AIO Teleporter v" + PROPS.version() + "!");
		startExp = skills.getCurrentExp(Skills.MAGIC); 
        startTime = System.currentTimeMillis(); 
        mouse.setSpeed(random(3, 6));     
		return true;
}

	/**Methods**/
	@Override
	public int loop() {
		switch (getMethod()) { 
        case tele: 
                if (game.getCurrentTab() != Game.TAB_MAGIC) { 
                        game.openTab(Game.TAB_MAGIC); 
                } else { 
                        Status = "Teleporting..."; 
                        Teleport(); 
                } 
                break; 
        case sleep: 
                mouse.setSpeed(random(3, 6)); 
                Status = "Sleeping..."; 
                antiban(); 
                sleep(random(200, 800)); 
                break; 
        case stop: 
                log(Color.RED, "You ran out of Law Runes!"); 
                log(Color.RED, "Your logged out!");
                stopScript(true); 
                break; 
        } 
        return random(500, 750); 
}
	/**Mouse Methods**/
	private void drawMouse(Graphics g1) { 
        ((Graphics2D) g1).setRenderingHints(new RenderingHints( 
                        RenderingHints.KEY_ANTIALIASING, 
                        RenderingHints.VALUE_ANTIALIAS_ON)); 
        Point p = mouse.getLocation(); 
        Graphics2D spinG = (Graphics2D) g1.create(); 
        Graphics2D spinGRev = (Graphics2D) g1.create(); 
        Graphics2D spinG2 = (Graphics2D) g1.create(); 
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
        g1.setColor(MOUSE_CENTER_COLOR); 
        g1.fillOval(p.x, p.y, 2, 2); 
        spinG2.setColor(MOUSE_CENTER_COLOR); 
        spinG2.rotate(System.currentTimeMillis() % 2000d / 2000d * 360d 
                        * Math.PI / 180.0, p.x, p.y); 
        spinG2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND, 
                        BasicStroke.JOIN_ROUND)); 
        spinG2.drawLine(p.x - 5, p.y, p.x + 5, p.y); 
        spinG2.drawLine(p.x, p.y - 5, p.x, p.y + 5); 
} 
 
@SuppressWarnings("serial") 
private class MousePathPoint extends Point { // credits to Enfilade 
        private int toColor(double d) { 
                return Math.min(255, Math.max(0, (int) d)); 
        } 

        private long finishTime; 
        private double lastingTime; 

        public MousePathPoint(int x, int y, int lastingTime) { 
                super(x, y); 
                this.lastingTime = lastingTime; 
                finishTime = System.currentTimeMillis() + lastingTime; 
        } 

        public boolean isUp() { 
                return System.currentTimeMillis() > finishTime; 
        } 

        public Color getColor() { 
                return new Color( 
                		253, 245, 230, 
                                toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime))); 
        } 
}

private static class Particle { 
	 
    private double posX; 
    private double posY; 
    private double movX; 
    private double movY; 
    private int alpha = 255, color = -1; 
    java.util.Random generator = new java.util.Random(); 

    Particle(int pos_x, int pos_y, int color) { 
            posX = (double) pos_x; 
            posY = (double) pos_y; 
            movX = ((double) generator.nextInt(40) - 20) / 16; 
            movY = ((double) generator.nextInt(40) - 20) / 16; 
            this.color = color; 
    } 

    public boolean handle(Graphics page) { 
            alpha -= random(1, 7); 
            if (alpha <= 0) 
                    return false; 
            switch (color) { 
            case 0: 
                    page.setColor(new Color(0, 0, 238, alpha)); 
                    break; 
            case 1: 
                    page.setColor(new Color(253, 245, 230, alpha)); 
                    break; 
            } 
            page.drawLine((int) posX, (int) posY, (int) posX, (int) posY); 
            posX += movX; 
            posY += movY; 
            return true; 
    } 
}



/**Teleporting Method**/
	public void Teleport() { 
        if (varTele == true) { 
                magic.castSpell(Magic.SPELL_VARROCK_TELEPORT); 
        } else if (falTele == true) { 
                magic.castSpell(Magic.SPELL_FALADOR_TELEPORT); 
        } else if (lumTele == true) { 
                magic.castSpell(Magic.SPELL_LUMBRIDGE_TELEPORT); 
        } else if (camTele == true) { 
                magic.castSpell(Magic.SPELL_CAMELOT_TELEPORT); 
                antiban(); 
        } else if (MATele == true) { 
                magic.castSpell(Magic.SPELL_MOBILISING_ARMIES_TELEPORT); 
        } else if (POHTele == true) { 
                magic.castSpell(Magic.SPELL_HOME_TELEPORT); 
        } else if (trollTele == true) { 
                magic.castSpell(Magic.SPELL_TROLLHEIM_TELEPORT); 
        } else if (ardTele == true) { 
                magic.castSpell(Magic.SPELL_ARDOUGNE_TELEPORT); 
        } else if (watchTele == true) { 
                magic.castSpell(Magic.SPELL_WATCHTOWER_TELEPORT); 
        } 
} 

	/**OnFinish**/
	public void onFinish() {
		log(new Color(0, 0, 238), "Thank you for using Beuners AIO Teleporter!");
		env.saveScreenshot(true);
	}
	
	/**AntiBan**/
	public void antiban(){
		Status = "Waiting...";
	    int b = random(2, 12);
	    switch (b) {
	    case 1:
	        if (random(0, 13) == 4) {
	       	 mouse.moveSlightly();
	            sleep(1200, 1600);
	            mouse.moveRandomly(37, 470);
	        }
	        break;
	    case 2:
	        if (random(1, 6) == 2) {
	        	Status = "Moving Camera";
	            camera.setAngle(random(30, 70));
	            sleep(200, 600);                		            
	        }            
	        break;
	    case 3:
	        if (random(0, 18) == 5) {
	        	Status = "Moving Mouse";
	            mouse.moveOffScreen();
	            sleep(random(600, random(1200, 2000)));
	        }
	        break;
	    case 4:
	        if (random(0, 19) == 9) {
	        	Status = "Moving Camera";	        	
	            camera.setAngle(random(134, 280));
	            sleep(300, 600);                
	           
	        }            
	        break;
	    case 5:
	        if (random(0, 21) == 7) {
	        	Status = "Moving Mouse";
	       	 mouse.moveSlightly();
	            sleep(100, 200);
	            mouse.moveRandomly(25, 380);
	        }
	    case 6:
			if (random(0, 100) == 1) {
				Status = "Checking xp.";
				game.openTab(Tab.STATS);
				if (skills.doHover(Skills.INTERFACE_MAGIC)) {
					sleep(2100, 3400);
			}	
	        break;
			}
	  default:
	        break;
	    }
	}
	
	/**Message Event**/
	public void messageReceived(MessageEvent message) { 
        String string = message.getMessage().toLowerCase(); 

        if (string.contains("You do not have enough Law Runes to case this spell")) { 
        	log(Color.RED, "You ran out of Law Runes!"); 
            log(Color.RED, "Your logged out!"); 
                stopScript(true); 
        } 
}
	
	/**Paint Method & some Paint Variables**/
	private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(0, 51, 255);
    private final Color color2 = new Color(0, 0, 0);
    private final Color color3 = new Color(204, 204, 204);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 12);
    private final Font font2 = new Font("Arial", 1, 20);
    private final Font font3 = new Font("Arial", 0, 9);

    private final Image img1 = getImage("http://images4.wikia.nocookie.net/__cb20110415011727/runescape/images/thumb/7/7e/Magic_cape_detailed.png/80px-Magic_cape_detailed.png");
    private final Image img2 = getImage("http://images1.wikia.nocookie.net/__cb20111125164015/runescape/images/9/9f/Magic_cape_%28t%29.png");


    /**PAINT!**/
	@Override
	public void onRepaint(Graphics g1) {
		if(game.isLoggedIn()) {
			Graphics2D g = (Graphics2D) g1;
			
			int nextLevel = skills.getCurrentLevel(Skills.MAGIC) + 1; 		 
            string = timer.toElapsedString(); 
			long millis = System.currentTimeMillis() - startTime; 
            String timer = Timer.format(millis); 
            currentLvl = skills.getRealLevel(Skills.MAGIC) - startLvl; 
            gainedExp = skills.getCurrentExp(Skills.MAGIC) - startExp; 
            xpHour = (int) (3600000.0 / (System.currentTimeMillis() - startTime) * gainedExp); 
            teleHour = (int) (totalTeles * 3600000 / (System 
                            .currentTimeMillis() - startTime)); 
            totalTeles = (int) (gainedExp / 55.5);
            
            g.setColor(color1);
            g.fillRoundRect(6, 344, 507, 128, 16, 16);
            g.setColor(color2);
            g.setStroke(stroke1);
            g.drawRoundRect(6, 344, 507, 128, 16, 16);
            g.setFont(font1);
            g.setColor(color3);
            g.drawString("Runtime: " + timer, 17, 383);
            g.drawString("Total Teleports: " + totalTeles, 17, 395);
            g.drawString("Teleports/hr: " + teleHour, 17, 407);
            g.drawString("XP Gained: " + (gainedExp), 17, 419);
            g.drawString("XP/hr: " + xpHour, 17, 431);
            g.drawString("Current lvl: " + currentLvl, 17, 443);
            g.drawString("Status: " + Status, 17, 455);
            g.drawImage(img1, 299, 322, null);
            g.drawImage(img1, 393, 324, null);
            g.drawImage(img2, 497, 340, null);
            g.drawImage(img2, 498, 448, null);
            g.drawImage(img2, 1, 445, null);
            g.drawImage(img2, 3, 338, null);
            g.setFont(font2);
            g.drawString("Beuners AIO Teleporter v" + PROPS.version(), 23, 363);
            g.setFont(font3);
            g.drawString("By Beuner", 247, 468);
            drawMouse(g);
            
            Point p = mouse.getLocation(); 
            while (!mousePath.isEmpty() && mousePath.peek().isUp()) 
                    mousePath.remove(); 
            Point clientCursor = mouse.getLocation(); 
            MousePathPoint mpp = new MousePathPoint(clientCursor.x, 
                            clientCursor.y, 3000); 
            if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) 
                    mousePath.add(mpp); 
            MousePathPoint lastPoint = null; 
            for (MousePathPoint a : mousePath) { 
                    if (lastPoint != null) { 
                            g.setColor(a.getColor()); 
                            g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y); 
                    } 
                    lastPoint = a; 
            } 
    g.fillRect(p.x - 5, p.y, 12, 2); 
    g.fillRect(p.x, p.y - 5, 2, 12); 
    
    Point p5 = mouse.getLocation(); 
    int x = p.x; 
    int y = p.y; 
    int color = random(0, 3); 
    if (mouse.isPressed()) { 
            synchronized (lock) { 
                    for (int i = 0; i < 50; i++, particles.add(new Particle(x, 
                                    y, color))) 
                            ; 
            } 
    } 
    synchronized (lock) { 
            Iterator<Particle> piter = particles.iterator(); 
            while (piter.hasNext()) { 
                    Particle part = piter.next(); 
                    if (!part.handle(g)) { 
                            piter.remove(); 
                    } 
            } 
    }


		}
		
	} 
	/**GUI**/
	class BeunTeleporterGUI extends JFrame {
		public BeunTeleporterGUI() {
			initComponents();
		}
		
		private void StartScript(ActionEvent event) { 
            String string = comboBox1.getSelectedItem().toString(); 
            
            if (string.equals("Mobilising Armies")) { 
                    MATele = true;
                    log(new Color(0, 0, 238), "Location selected: Mobilising Armies!");
            } else if (string.equals("Varrock")) { 
                    varTele = true; 
                    log(new Color(0, 0, 238), "Location Selected: Varrock!");
            } else if (string.equals("Lumbridge")) { 
                    lumTele = true;
                    log(new Color(0, 0, 238), "Location Seleted: Lumbridge!");
            } else if (string.equals("Player-Owned-House")) { 
                    POHTele = true;
                    log(new Color(0, 0, 238), "Location Seleted: Your house!");
            } else if (string.equals("Camelot")) { 
                    camTele = true;
                    log(new Color(0, 0, 238), "Location Seleted: Camelot!");
            } else if (string.equals("Ardougne")) { 
                    ardTele = true;
                    log(new Color(0, 0, 238), "Location Seleted: Ardougne!");
            } else if (string.equals("WatchTower")) { 
                    watchTele = true;
                    log(new Color(0, 0, 238), "Location Seleted: WatchTower!");
            } else if (string.equals("Trollheim")) { 
                    trollTele = true;
                    log(new Color(0, 0, 238), "Location Seleted: Trollheim!");
            } else if(string.equals("Falador")){ 
                    falTele = true;
                    log(new Color(0, 0, 238), "Location Seleted: Falador!");
            } 
            guiWait = false; 
            g.dispose(); 
    } 


		private void initComponents() {
			label1 = new JLabel();
			label2 = new JLabel();
			comboBox1 = new JComboBox();
			button1 = new JButton();

			Container contentPane = getContentPane();

			label1.setText("Beuners Teleporter v" + PROPS.version());
			label1.setFont(new Font("Tahoma", Font.BOLD, 26));

			label2.setText("Teleport Location: ");
			label2.setFont(new Font("Tahoma", Font.PLAIN, 18));

			comboBox1.setModel(new DefaultComboBoxModel(new String[] {
				"Varrock",
				"Falador",
				"Lumbridge",
				"Camelot",
				"Ardougne",
				"Mobilising Armies",
				"Player-Owned-House",
				"Trollheim",
				"WatchTower"
			}));

			button1.setText("Start!");
			button1.addActionListener(new ActionListener() { 
                @Override 
                public void actionPerformed(ActionEvent event) { 
                        StartScript(event); 
                } 
        }); 



			GroupLayout contentPaneLayout = new GroupLayout(contentPane);
			contentPane.setLayout(contentPaneLayout);
			contentPaneLayout.setHorizontalGroup(
				contentPaneLayout.createParallelGroup()
					.addGroup(contentPaneLayout.createSequentialGroup()
						.addGroup(contentPaneLayout.createParallelGroup()
							.addGroup(contentPaneLayout.createSequentialGroup()
								.addGap(21, 21, 21)
								.addGroup(contentPaneLayout.createParallelGroup()
									.addComponent(label1, GroupLayout.PREFERRED_SIZE, 336, GroupLayout.PREFERRED_SIZE)
									.addGroup(contentPaneLayout.createSequentialGroup()
										.addComponent(label2, GroupLayout.PREFERRED_SIZE, 178, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(comboBox1, 0, 154, Short.MAX_VALUE))))
							.addGroup(contentPaneLayout.createSequentialGroup()
								.addGap(143, 143, 143)
								.addComponent(button1, GroupLayout.PREFERRED_SIZE, 95, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(12, Short.MAX_VALUE))
			);
			contentPaneLayout.setVerticalGroup(
				contentPaneLayout.createParallelGroup()
					.addGroup(contentPaneLayout.createSequentialGroup()
						.addContainerGap()
						.addComponent(label1, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(label2, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
							.addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(button1, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
			);
			pack();
			setLocationRelativeTo(getOwner());
		}

		private JLabel label1;
		private JLabel label2;
		private JComboBox comboBox1;
		private JButton button1;
	}


}
