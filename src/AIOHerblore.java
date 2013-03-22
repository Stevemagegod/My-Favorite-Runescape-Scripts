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
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSObject;

/**Manifest**/
@ScriptManifest(
authors = {"Beuner"},
version = 1.4,
keywords = ("Beuner, Herblore, overload, potion, herbs"),
description = "Does everything for Herblore xp!",
name = "Beuners AIOHerblore"
)
/**Main Class**/
public class BeunersAIOHerblore extends Script implements PaintListener, MessageListener, MouseListener {
  /**Variables**/
	public boolean makeUnf;
	public boolean makeFin;
	public boolean makeOvl;
	public boolean cleanHerb;
	public boolean groundItem;
	public int ingredientToUse;
	public int unfPotToUse;
	public int potToUse;
	public int herbToClean;
	public int herbToUse;
	public int itemToGround;
	
	 private final static int[] path = { 0, 4, 8, 12, 16, 20, 24, 25, 21, 17, 13, 9, 5, 1, 2, 6, 
         10, 14, 18, 22, 26, 27, 23, 19, 15, 11, 7, 3 }; 
	
	public int bankId[] = {42192, 4483, 20607, 27663, 2693};
	public int vialOfWater = 227;
	public int pestleMortar = 233;
	
	public int grimyGuam = 199,
				grimyMarrentill = 201,
				grimyTarromin = 203,
				grimyHarralander = 205,
				grimyRanarr = 207,
				grimyIrit = 209,
				grimyAvantoe = 211,
				grimyKwuarm = 213,
				grimyCadantite = 215,
				grimyDwarfWeed = 217,
				grimyTorstol = 219,
				grimyLantadyme = 2485,
				grimyToadflax = 3049,
				grimySnapdragon = 3051,
				grimySpiritWeed = 12174,
				grimyWergali = 14836,
				grimyFellstalk = 21626;
				
	
	public int cleanGuam = 249,
				cleanMarrentill = 251,
				cleanTarromin = 253,
				cleanHarralander = 255,
				cleanRanarr = 257,
				cleanIrit = 259,
				cleanAvantoe = 261,
				cleanKwuarm = 263,
				cleanCadantite = 265,
				cleanDwarfWeed = 267,
				cleanTorstol = 269,
				cleanLantadyme = 2481,
				cleanToadflax = 2998,
				cleanSnapdragon = 3000,
				cleanSpiritWeed = 12172,
				cleanWergali = 14854,
				cleanFellstalk = 21624;
	
	public int eyeOfNewt = 221,
				groundUnicornHorn = 235,
				limpwurtRoot = 225,
				redSpiderEggs = 223,
				chocolateDust = 1975,
				whiteBerries = 239,
				toadLegs = 2152,
				groundDesertGoatHorn = 9736,
				snapeGrass = 231,
				cockatriceEgg = 12109,
				giantFrogspawn = 5004,
				mortMyreFungi = 2970,
				groundKebbitTeeth = 10111,
				wimpyBirdFeather = 11525,
				groundBlueDragonScale = 241,
				wineOfZamorak = 245,
				potatoCactus = 3138,
				jangerberries = 247,
				crushedBirdNest = 6693,
				papaya = 5972,
				desertPhoenixFeather = 4621,
				groundMudRune = 9594,
				grenwallSpikes = 12539,
				wyvernBonemeal = 6810,
				morchellaMushroom = 21622;
	
	public int unfGuam = 91,
			unfMarrentill = 93,
			unfTarromin = 95,
			unfHarralander = 97,
			unfRanarr = 99,
			unfIrit = 101,
			unfAvantoe = 103,
			unfKwuarm = 105,
			unfCadantite = 107,
			unfDwarfWeed = 109,
			unfTorstol = 111,
			unfLantadyme = 2483,
			unfToadflax = 3002,
			unfSnapdragon = 3004,
			unfSpiritWeed = 12181,
			unfWergali = 14856,
			unfFellstalk = 21628;
	
	public int xtremeStrength = 15313,
				xtremeAttack = 15309,
				xtremeDefence = 15317,
				xtremeMagic = 15321,
				xtremeRange = 15325,
				superEnergy = 3018,
				antiFire = 2454,
				superAttack = 145,
				superStrength = 157,
				superDefence = 163,
				magicPotion = 3042,
				rangePotion = 169,
				prayerRestore = 139;
	
	public int unicornHorn = 237,
				chocolateBar = 1973,
				kebbitTeeth = 10109,
				gorakClaw = 9016,
				birdNest = 5075,
				desertGoatHorn = 9735,
				blueDragonScale = 243,
				mudRune = 4698,
				ashes = 592,
				poisonKarambwan = 3146,
				fishingBait = 313,
				seaWeed = 401,					
				neemDrupe = 22445;
				
	
	public String Status = "Loading...";
    public long startTime;
    public int startExp;
    public String string;
    public int expGained = 0;
    public int gainedExp= 0;
    public int startLvl;
    public int currentLvl;
    public int XPTNL = 0;
    public int itemGround = 0;
    public int herbCleaned = 0;
    public int finHour;
    public int herbsHour;
    public int groundHour;
    public boolean showPaint = true ;
    public int unfHour;
    public int finPotsMade = 0;
    public int unfPotsMade = 0;
	private final ScriptManifest PROPS = getClass().getAnnotation(ScriptManifest.class);
	
	private static final Color MOUSE_COLOR = new Color(255, 215, 0), 
    		MOUSE_BORDER_COLOR = new Color(255, 215, 0), 
    		MOUSE_CENTER_COLOR = new Color(34, 139, 34); 
    		Point p; 
    		Point p2;
    		Point p5;
    		private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();    		
    		final Object lock = new Object(); 
    		final ArrayList<Particle> particles = new ArrayList<Particle>();
    		
    		BeunAIOHerbloreGUI g = new BeunAIOHerbloreGUI(); 
    	    public boolean guiWait = true;
	
	private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }
	
	private final Color color1 = new Color(0, 146, 17);
    private final Color color2 = new Color(255, 255, 0);
    private final Color color3 = new Color(0, 0, 0);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 12);

    private final Image img1 = getImage("http://i39.tinypic.com/2h3c3uc.png");
    private final Image img2 = getImage("http://images4.wikia.nocookie.net/__cb20111128060661/runescape/images/thumb/5/56/Saradomin_brew_detail.png/99px-Saradomin_brew_detail.png");
    private final Image img3 = getImage("http://images3.wikia.nocookie.net/__cb20111119065432/runescape/images/thumb/3/39/Clean_fellstalk_detail.png/100px-Clean_fellstalk_detail.png");
    private final Image img4 = getImage("http://images1.wikia.nocookie.net/__cb20091112192213/runescape/images/b/b1/Herblore_cape_%28t%29.png");


   
	
	/**Herblore stuff**/
    public enum Methods { 
        makeunf, makefin, makeovl, cleanherb, grounditem, sleep, stop 
} 

    public Methods getMethod() { 
        if (makeUnf == true) { 
                return Methods.makeunf; 
        }
        if (makeFin == true) {
        	return Methods.makefin;
        }
        if (makeOvl == true) {
        	return Methods.makeovl;
        }
        if (cleanHerb == true) {
        	return Methods.cleanherb;
        }
        if (groundItem == true) {
        	return Methods.grounditem;
        }
        return Methods.sleep; 
}

    public boolean onStart() {
    	g.setVisible(true);
        while(guiWait){ 
            sleep(10);
    }
    	log(new Color(34, 139, 34), "Welcome to Beuners AIO Herblore!");
    	mouse.setSpeed(random(3, 4));
    	startTime = System.currentTimeMillis();
    	startExp = skills.getCurrentExp(Skills.HERBLORE);
    	return true;
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
                		34, 139, 34, 
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
                    page.setColor(new Color(34, 139, 34, alpha)); 
                    break; 
            case 1: 
                    page.setColor(new Color(255, 215, 0, alpha)); 
                    break; 
            } 
            page.drawLine((int) posX, (int) posY, (int) posX, (int) posY); 
            posX += movX; 
            posY += movY; 
            return true; 
    } 
}

	/**Loop**/
	@Override
	public int loop() {
		switch (getMethod()) {
		case makeunf:
			if(!inventory.contains(herbToUse) || !inventory.contains(vialOfWater)) {
				bankUnfItems();
			} else {
				makeUnfPotion();
			}
			break;
		case makefin:
			if(!inventory.contains(ingredientToUse) || !inventory.contains(unfPotToUse)) {
				bankFinItems();
			} else {
				makeFinPotion();
			}					
			break;
		case makeovl:
			if(!inventory.contains(ingredientToUse) || !inventory.contains(potToUse)) {
				bankOvlItems();
			} else {
				makeOvl();
			}					
			break;
		case cleanherb:
			if(!inventory.contains(herbToClean)) {
				bankHerbs();
			} else {
				cleanHerbs();
			}					
			break;
		case grounditem:
			if(!inventory.contains(pestleMortar) || !inventory.contains(itemToGround)) {
				bankItemsToGround();
			} else {
				groundItems();
			}					
			break;
		case sleep:
			mouse.setSpeed(random(3, 4)); 
            Status = "Sleeping..."; 
            antibanlist();
			sleep(800, 1200);
			break;
		case stop:
			stopScript(true);
			break;
		}
		return 0;
	}
	
	/**Potion Methods**/
	public void makeFinPotion() {
		RSItem ingredient = inventory.getItem(ingredientToUse);
		RSItem unfPot = inventory.getItem(unfPotToUse);
		if(inventory.contains(ingredientToUse) && inventory.contains(unfPotToUse)) {
		inventory.useItem(ingredient, unfPot);
		sleep(1000);
		if (interfaces.get(905).getComponent(14).doClick()) {
			antibanlist();
			sleep(random(18000, 20000));
		} else {
			bankFinItems();
		}
		}
	}
	
	public void makeUnfPotion() {
		RSItem herb = inventory.getItem(herbToUse);
		RSItem vialofwater = inventory.getItem(vialOfWater);
		if(inventory.contains(herbToUse) && inventory.contains(vialOfWater)) {
		inventory.useItem(herb, vialofwater);
		sleep(1000);
		if (interfaces.get(905).getComponent(14).doClick()) {
			antibanlist();
			sleep(random(9000, 10000));
		} else {
			bankUnfItems();
		}
		}		
	}
	
	public void makeOvl() {
		RSItem xtremestrength = inventory.getItem(ingredientToUse);
		RSItem xtremeattack = inventory.getItem(potToUse);
		if(inventory.contains(ingredientToUse) && inventory.contains(potToUse)) {
		inventory.useItem(xtremestrength, xtremeattack);
		sleep(1000);
		if (interfaces.get(905).getComponent(14).doClick()) {
			antibanlist();
			sleep(random(10000, 11000));
		} else {
			bankOvlItems();
		}
		}
	}
	
	public void cleanHerbs() {
		RSItem herbs = inventory.getItem(herbToClean);
		if(inventory.contains(herbToClean)) {
			 for (int slot = 0; slot <= 27; slot++) { 
                 mouse.setSpeed(1);  
                 if (inventory.getItems()[path[slot]].getID() == herbToClean) { 
                     inventory.getItems()[path[slot]].doAction("Clean");                  		
		} else {
			bankHerbs();
		}
			 }
		}
		}	
	
	public void groundItems() {
		RSItem pestle = inventory.getItem(pestleMortar);
		RSItem groundable = inventory.getItem(itemToGround);
		if(inventory.contains(pestleMortar) && inventory.contains(itemToGround)) {
		inventory.useItem(pestle, groundable);
		sleep(1000);
		if (interfaces.get(905).getComponent(14).doClick()) {
			antibanlist();
			sleep(random(10000, 11000));
		} else {
			bankItemsToGround();
		}
		}
	}

	/**Bank Methods**/
	public void bankFinItems() {
		RSObject Bank = objects.getNearest(bankId);
		if (bank.isOpen()) {
			bank.depositAll();
			sleep(300, 500);
			bank.withdraw(ingredientToUse, 14);
			sleep(300, 500);
			bank.withdraw(unfPotToUse, 14);
			sleep(300, 500);
			bank.close();
		} else {
			Bank.interact("Bank"); 		
			sleep(300, 350);
		if (ingredientToUse == grenwallSpikes && bank.isOpen()) {
			bank.depositAll();
			sleep(300, 500);
			bank.withdraw(ingredientToUse, 135);
			sleep(300, 500);
			bank.withdraw(unfPotToUse, 27);
			sleep(300, 500);
			bank.close();
		}
		}
	}
	
	public void bankUnfItems() {
		RSObject Bank = objects.getNearest(bankId);
		if (bank.isOpen()) {
			bank.depositAll();
			sleep(300, 500);		
			bank.withdraw(vialOfWater, 14);
			sleep(300, 500);
			bank.withdraw(herbToUse, 14);
			sleep(300, 500);
			bank.close();
		} else {
			Bank.interact("Bank"); 		
			sleep(300, 350);
		}		
	}
	
	public void bankOvlItems() {
		RSObject Bank = objects.getNearest(bankId);
		if (bank.isOpen()) {
			bank.depositAll();
			sleep(300, 500);
			bank.withdraw(xtremeStrength, 4);
			sleep(300, 500);
			bank.withdraw(xtremeAttack, 4);
			sleep(300, 500);
			bank.withdraw(xtremeDefence, 4);
			sleep(300, 500);
			bank.withdraw(xtremeMagic, 4);
			sleep(300, 500);
			bank.withdraw(xtremeRange, 4);
			sleep(300, 500);
			bank.withdraw(cleanTorstol, 4);
			sleep(300, 500);
			bank.close();
		} else {
			Bank.interact("Bank"); 		
			sleep(300, 350);
		}	
	}
	
	public void bankHerbs() {
		RSObject Bank = objects.getNearest(bankId);
		if (bank.isOpen()) {
			bank.depositAll();
			sleep(300, 500);
			bank.withdraw(herbToClean, 28);
			sleep(300, 500);
			bank.close();
		} else {
			Bank.interact("Bank"); 		
			sleep(300, 350);
		}
	}
	
	public void bankItemsToGround() {
		RSObject Bank = objects.getNearest(bankId);
		if (bank.isOpen()) {
			bank.depositAll();
			sleep(300, 500);
			bank.withdraw(pestleMortar, 1);
			sleep(300, 500);
			bank.withdraw(itemToGround, 27);
			sleep(300, 500);
			bank.close();
		} else {
			Bank.interact("Bank"); 		
			sleep(300, 350);
		}
	}
	
	/**AntiBan**/
	public void chooserandomAFK() {
		switch (random(0, 4)) {
		case 0:
			sleep(random(500, 900));
			break;
		case 1:
			sleep(random(400, 1000));
			break;
		case 2:
			sleep(random(1000, 2000));
			break;
		case 3:
			sleep(random(1000, 3000));
			break;
		case 4:
			log("Not doing AFK");
			break;
		}
	}

	public void superAntiMoveMouse() {
		switch (random(0, 10)) {
		case 0:
			log("Doing superAnti! Wiggling mouse a lot");
			mouse.setSpeed(random(6, 9));
			mouse.moveSlightly();
			mouse.moveSlightly();
			mouse.moveSlightly();
			break;
		case 1:
			log("Doing superAnti! Wiggling mouse ");
			mouse.setSpeed(random(6, 9));
			mouse.moveSlightly();
			mouse.moveSlightly();
			break;
		case 2:
			log("Doing superAnti! Wiggling mouse a lot");
			mouse.setSpeed(random(6, 9));
			mouse.moveSlightly();
			mouse.moveSlightly();
			mouse.moveSlightly();
			mouse.moveSlightly();
			mouse.moveSlightly();
			mouse.moveSlightly();
			break;
		}
	}

	public void randomtab() {
		switch (random(0, 12)) {
		case 0:
			game.openTab(Game.TAB_STATS);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 1:
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 2:
			game.openTab(Game.TAB_CLAN);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 3:
			game.openTab(Game.TAB_FRIENDS);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 4:

		case 5:
			game.openTab(Game.TAB_EQUIPMENT);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 6:
			game.openTab(Game.TAB_MAGIC);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 7:
			game.openTab(Game.TAB_QUESTS);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 8:

		case 9:
			game.openTab(Game.TAB_NOTES);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 10:
			game.openTab(Game.TAB_PRAYER);
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 11:
			game.openTab(Game.TAB_MUSIC);
			game.openTab(Game.TAB_INVENTORY);
			break;
		}
	}

	public void randomXPcheck() {
		game.openTab(Game.TAB_STATS);
		switch (random(0, 20)) {
		case 0:
			skills.doHover(Skills.INTERFACE_FISHING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 1:
			skills.doHover(Skills.INTERFACE_WOODCUTTING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 2:
			skills.doHover(Skills.INTERFACE_ATTACK);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 3:
			skills.doHover(Skills.INTERFACE_STRENGTH);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 4:
			skills.doHover(Skills.INTERFACE_COOKING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 5:
			skills.doHover(Skills.INTERFACE_RANGE);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 6:
			skills.doHover(Skills.INTERFACE_FIREMAKING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 7:
			skills.doHover(Skills.INTERFACE_CONSTRUCTION);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 8:
			skills.doHover(Skills.INTERFACE_RUNECRAFTING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 9:
			skills.doHover(Skills.INTERFACE_SUMMONING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 10:
			skills.doHover(Skills.INTERFACE_SLAYER);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 11:
			skills.doHover(Skills.INTERFACE_CRAFTING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 12:
			skills.doHover(Skills.INTERFACE_FARMING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 13:
			skills.doHover(Skills.INTERFACE_AGILITY);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 14:
			skills.doHover(Skills.INTERFACE_THIEVING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 15:
			skills.doHover(Skills.INTERFACE_HUNTER);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 16:
			skills.doHover(Skills.INTERFACE_MINING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 17:
			skills.doHover(Skills.INTERFACE_SMITHING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 18:
			skills.doHover(Skills.INTERFACE_MAGIC);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 19:
			skills.doHover(Skills.INTERFACE_FLETCHING);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		case 20:
			skills.doHover(Skills.INTERFACE_PRAYER);
			sleep(random(200, 300));
			game.openTab(Game.TAB_INVENTORY);
			break;
		}
	}

	public int antibanlist() {
		switch (random(0, 350)) {
		case 0:
			chooserandomAFK();
			break;
		case 1:
		case 2:
		case 3:
			chooserandomAFK();
			break;
		case 4:
			mouse.moveSlightly();
			break;
		case 5:
			chooserandomAFK();
			break;
		case 6:
		case 7:

			break;
		case 8:
			superAntiMoveMouse();
			break;
		case 9:
			randomXPcheck();
			break;
		case 10:
			randomtab();
			break;
		case 11:
		case 12:
			randomtab();
			break;
		case 13:
			superAntiMoveMouse();
			break;
		case 14:
			randomXPcheck();
			break;
		case 15:
		case 16:
		case 17:
			break;
		default:
			break;
		}
		return 100;
	}


	

	public void onFinish() {
		log(new Color(34, 139, 34), "Thanks for Using Beuners AIO Herblore");
	}
	
	class BeunAIOHerbloreGUI extends JFrame {
		public BeunAIOHerbloreGUI() {
			initComponents();
		}

		private void button1ActionPerformed() {
			String herbs = herbComboBox.getSelectedItem().toString();
			String unfpots = unfComboBox.getSelectedItem().toString();
			String finpots = finComboBox.getSelectedItem().toString();
			String grounditemss = groundComboBox.getSelectedItem().toString();
			if(cleaningHerbs.isSelected()) {
				cleanHerb = true;
				makingUnfinished.disable();
				makingFinished.disable();
				groundingItems.disable();
				if (herbs.equals("Guam")) { 
                    log(new Color(34, 139, 34), "You are Cleaning: Guam!");
                    herbToClean = grimyGuam;
			} else if(herbs.equals("Marrentill")) {
                log(new Color(34, 139, 34), "You are Cleaning: Marrentill!");
                herbToClean = grimyMarrentill;
			} else if(herbs.equals("Tarromin")) {
                log(new Color(34, 139, 34), "You are Cleaning: Tarromin!");
                herbToClean = grimyTarromin;
			} else if(herbs.equals("Harralander")) {
                log(new Color(34, 139, 34), "You are Cleaning: Harralander!");
                herbToClean = grimyHarralander;
			} else if(herbs.equals("Ranarr")) {
                log(new Color(34, 139, 34), "You are Cleaning: Ranarr!");
                herbToClean = grimyRanarr;
			} else if(herbs.equals("Toadflax")) {
                log(new Color(34, 139, 34), "You are Cleaning: Toadflax!");
                herbToClean = grimyToadflax;
			} else if(herbs.equals("Spirit Weed")) {
                log(new Color(34, 139, 34), "You are Cleaning: Spirit Weed!");
                herbToClean = grimySpiritWeed;
			} else if(herbs.equals("Irit")) {
                log(new Color(34, 139, 34), "You are Cleaning: Irit!");
                herbToClean = grimyIrit;
			} else if(herbs.equals("Wergali")) {
                log(new Color(34, 139, 34), "You are Cleaning: Wergali!");
                herbToClean = grimyWergali;
			} else if(herbs.equals("Avantoe")) {
                log(new Color(34, 139, 34), "You are Cleaning: Avantoe!");
                herbToClean = grimyAvantoe;
			} else if(herbs.equals("Kwuarm")) {
                log(new Color(34, 139, 34), "You are Cleaning: Kwuarm!");
                herbToClean = grimyKwuarm;
			} else if(herbs.equals("Snapdragon")) {
                log(new Color(34, 139, 34), "You are Cleaning: Snapdragon!");
                herbToClean = grimySnapdragon;
			} else if(herbs.equals("Cadantite")) {
                log(new Color(34, 139, 34), "You are Cleaning: Cadantite!");
                herbToClean = grimyCadantite;
			} else if(herbs.equals("Lantadyme")) {
                log(new Color(34, 139, 34), "You are Cleaning: Lantadyme!");
                herbToClean = grimyLantadyme;
			} else if(herbs.equals("Dwarf Weed")) {
                log(new Color(34, 139, 34), "You are Cleaning: Dwarf Weed!");
                herbToClean = grimyDwarfWeed;
			} else if(herbs.equals("Torstol")) {
                log(new Color(34, 139, 34), "You are Cleaning: Torstol!");
                herbToClean = grimyTorstol;
			} else if(herbs.equals("Fellstalk")) {
                log(new Color(34, 139, 34), "You are Cleaning: Fellstalk!");
                herbToClean = grimyFellstalk;
			}
				guiWait = false; 
	            g.dispose(); 
		} else if(makingUnfinished.isSelected()) {
			makeUnf = true;
			cleaningHerbs.disable();
			makingFinished.disable();
			groundingItems.disable();
			if(unfpots.equals("Guam unf")) {
                log(new Color(34, 139, 34), "You are Making: Guam unfinished!");
                herbToUse = cleanGuam;
			} else if(unfpots.equals("Marrentill unf")) {
                log(new Color(34, 139, 34), "You are Making: Marrentill unfinished!");
                herbToUse = cleanMarrentill;
			} else if(unfpots.equals("Tarromin unf")) {
                log(new Color(34, 139, 34), "You are Making: Tarromin unfinished!");
                herbToUse = cleanTarromin;
			} else if(unfpots.equals("Harralander unf")) {
                log(new Color(34, 139, 34), "You are Making: Harralander unfinished!");
                herbToUse = cleanHarralander;
			} else if(unfpots.equals("Ranarr unf")) {
                log(new Color(34, 139, 34), "You are Making: Ranarr unfinished!");
                herbToUse = cleanRanarr;
			} else if(unfpots.equals("Toadflax unf")) {
                log(new Color(34, 139, 34), "You are Making: Toadflax unfinished!");
                herbToUse = cleanToadflax;
			} else if(unfpots.equals("Spirit Weed unf")) {
                log(new Color(34, 139, 34), "You are Making: Spirit Weed unfinished!");
                herbToUse = cleanSpiritWeed;
			} else if(unfpots.equals("Irit unf")) {
                log(new Color(34, 139, 34), "You are Making: Irit unfinished!");
                herbToUse = cleanIrit;
			} else if(unfpots.equals("Wergali unf")) {
                log(new Color(34, 139, 34), "You are Making: Wergali unfinished!");
                herbToUse = cleanWergali;
			} else if(unfpots.equals("Avantoe unf")) {
                log(new Color(34, 139, 34), "You are Making: Avantoe unfinished!");
                herbToUse = cleanAvantoe;
			} else if(unfpots.equals("Kwuarm unf")) {
                log(new Color(34, 139, 34), "You are Making: Kwuarm unfinished!");
                herbToUse = cleanKwuarm;
			} else if(unfpots.equals("Snapdragon unf")) {
                log(new Color(34, 139, 34), "You are Making: Snapdragon unfinished!");
                herbToUse = cleanSnapdragon;
			} else if(unfpots.equals("Cadantite unf")) {
                log(new Color(34, 139, 34), "You are Making: Cadantite unfinished!");
                herbToUse = cleanCadantite;
			} else if(unfpots.equals("Lantadyme unf")) {
                log(new Color(34, 139, 34), "You are Making: Lantadyme unfinished!");
                herbToUse = cleanLantadyme;
			} else if(unfpots.equals("Dwarf Weed unf")) {
                log(new Color(34, 139, 34), "You are Making: Dwarf Weed unfinished!");
                herbToUse = cleanDwarfWeed;
			} else if(unfpots.equals("Torstol unf")) {
                log(new Color(34, 139, 34), "You are Making: Torstol unfinished!");
                herbToUse = cleanTorstol;
			} else if(unfpots.equals("Fellstalk unf")) {
                log(new Color(34, 139, 34), "You are Making: Fellstalk unfinished!");
                herbToUse = cleanFellstalk;
		}
			guiWait = false; 
            g.dispose(); 
		} else if(makingFinished.isSelected()) {
			makeFin = true;
			makingUnfinished.disable();
			cleaningHerbs.disable();
			groundingItems.disable();
			if(finpots.equals("Attack potion")) {
				log(new Color(34, 139, 34), "You are Making: Attack Potion!");
                unfPotToUse = unfGuam;
                ingredientToUse = eyeOfNewt;
			} else if(finpots.equals("Anti-poison")) {
				log(new Color(34, 139, 34), "You are Making: Anti-poison!");
                unfPotToUse = unfMarrentill;
                ingredientToUse = groundUnicornHorn;
		} else if(finpots.equals("Strength potion")) {
			log(new Color(34, 139, 34), "You are Making: Strength Potion!");
            unfPotToUse = unfTarromin;
            ingredientToUse = limpwurtRoot;
	} else if(finpots.equals("Stat Restore potion")) {
		log(new Color(34, 139, 34), "You are Making: Stat Restore potion!");
        unfPotToUse = unfHarralander;
        ingredientToUse = redSpiderEggs;
} else if(finpots.equals("Energy potion")) {
	log(new Color(34, 139, 34), "You are Making: Energy potion!");
    unfPotToUse = unfHarralander;
    ingredientToUse = chocolateDust;
} else if(finpots.equals("Defence potion")) {
	log(new Color(34, 139, 34), "You are Making: Defence potion!");
    unfPotToUse = unfRanarr;
    ingredientToUse = whiteBerries;
} else if(finpots.equals("Agility potion")) {
	log(new Color(34, 139, 34), "You are Making: Agility Potion!");
    unfPotToUse = unfToadflax;
    ingredientToUse = toadLegs;
} else if(finpots.equals("Combat potion")) {
	log(new Color(34, 139, 34), "You are Making: Combat Potion!");
    unfPotToUse = unfHarralander;
    ingredientToUse = groundDesertGoatHorn;
} else if(finpots.equals("Prayer Restore potion")) {
	log(new Color(34, 139, 34), "You are Making: Prayer Restore Potion!");
    unfPotToUse = unfRanarr;
    ingredientToUse = snapeGrass;
} else if(finpots.equals("Summoning potion")) {
	log(new Color(34, 139, 34), "You are Making: Summoning Potion!");
    unfPotToUse = unfSpiritWeed;
    ingredientToUse = cockatriceEgg;
} else if(finpots.equals("Crafting potion")) {
	log(new Color(34, 139, 34), "You are Making: Crafting Potion!");
    unfPotToUse = unfWergali;
    ingredientToUse = giantFrogspawn;
} else if(finpots.equals("Super Attack potion")) {
	log(new Color(34, 139, 34), "You are Making: Super Attack Potion!");
    unfPotToUse = unfIrit;
    ingredientToUse = eyeOfNewt;
} else if(finpots.equals("Super Anti-poison")) {
	log(new Color(34, 139, 34), "You are Making: Super Attack Potion!");
    unfPotToUse = unfIrit;
    ingredientToUse = groundUnicornHorn;
} else if(finpots.equals("Fishing potion")) {
	log(new Color(34, 139, 34), "You are Making: Fishing Potion!");
    unfPotToUse = unfAvantoe;
    ingredientToUse = snapeGrass;
} else if(finpots.equals("Super Energy potion")) {
	log(new Color(34, 139, 34), "You are Making: Super Energy Potion!");
    unfPotToUse = unfAvantoe;
    ingredientToUse = mortMyreFungi;
} else if(finpots.equals("Hunter potion")) {
	log(new Color(34, 139, 34), "You are Making: Hunter Potion!");
    unfPotToUse = unfAvantoe;
    ingredientToUse = groundKebbitTeeth;
} else if(finpots.equals("Super Strength potion")) {
	log(new Color(34, 139, 34), "You are Making: Super Strength Potion!");
    unfPotToUse = unfKwuarm;
    ingredientToUse = limpwurtRoot;
} else if(finpots.equals("Fletching potion")) {
	log(new Color(34, 139, 34), "You are Making: Fletching Potion!");
    unfPotToUse = unfWergali;
    ingredientToUse = wimpyBirdFeather;
} else if(finpots.equals("Weapon poison")) {
	log(new Color(34, 139, 34), "You are Making: Weapon Poison!");
    unfPotToUse = unfKwuarm;
    ingredientToUse = groundBlueDragonScale;
} else if(finpots.equals("Super Restore potion")) {
	log(new Color(34, 139, 34), "You are Making: Super Restore Potion!");
    unfPotToUse = unfSnapdragon;
    ingredientToUse = redSpiderEggs;
} else if(finpots.equals("Super Defence potion")) {
	log(new Color(34, 139, 34), "You are Making: Super Defence Potion!");
    unfPotToUse = unfCadantite;
    ingredientToUse = whiteBerries;
} else if(finpots.equals("Anti-Fire potion")) {
	log(new Color(34, 139, 34), "You are Making: Anti-Fire Potion!");
    unfPotToUse = unfLantadyme;
    ingredientToUse = groundBlueDragonScale;
} else if(finpots.equals("Ranging potion")) {
	log(new Color(34, 139, 34), "You are Making: Ranging Potion!");
    unfPotToUse = unfDwarfWeed;
    ingredientToUse = wineOfZamorak;
} else if(finpots.equals("Magic potion")) {
	log(new Color(34, 139, 34), "You are Making: Magic Potion!");
    unfPotToUse = unfLantadyme;
    ingredientToUse = potatoCactus;
} else if(finpots.equals("Zamorak Brew")) {
	log(new Color(34, 139, 34), "You are Making: Zamorak Brew!");
    unfPotToUse = unfTorstol;
    ingredientToUse = jangerberries;
} else if(finpots.equals("Saradomin Brew")) {
	log(new Color(34, 139, 34), "You are Making: Saradomin Brew!");
    unfPotToUse = unfToadflax;
    ingredientToUse = crushedBirdNest;
} else if(finpots.equals("Recover Special potion")) {
	log(new Color(34, 139, 34), "You are Making: Recover Special Potion!");
    unfPotToUse = superEnergy;
    ingredientToUse = papaya;
} else if(finpots.equals("Super Antifire potion")) {
	log(new Color(34, 139, 34), "You are Making: Super Antifire Potion!");
    unfPotToUse = antiFire;
    ingredientToUse = desertPhoenixFeather;
} else if(finpots.equals("Extreme Attack potion")) {
	log(new Color(34, 139, 34), "You are Making: Extreme Attack Potion!");
    unfPotToUse = superAttack;
    ingredientToUse = cleanAvantoe;
} else if(finpots.equals("Extreme Strength potion")) {
	log(new Color(34, 139, 34), "You are Making: Extreme Strength Potion!");
    unfPotToUse = superStrength;
    ingredientToUse = cleanDwarfWeed;
} else if(finpots.equals("Extreme Defence potion")) {
	log(new Color(34, 139, 34), "You are Making: Extreme Defence Potion!");
    unfPotToUse = superDefence;
    ingredientToUse = cleanLantadyme;
} else if(finpots.equals("Extreme Magic potion")) {
	log(new Color(34, 139, 34), "You are Making: Extreme Magic Potion!");
    unfPotToUse = magicPotion;
    ingredientToUse = groundMudRune;
} else if(finpots.equals("Extreme Ranging potion")) {
	log(new Color(34, 139, 34), "You are Making: Extreme Ranging Potion!");
    unfPotToUse = rangePotion;
    ingredientToUse = grenwallSpikes;
} else if(finpots.equals("Super Prayer Restore")) {
	log(new Color(34, 139, 34), "You are Making: Super Prayer Restore Potion!");
    unfPotToUse = prayerRestore;
    ingredientToUse = wyvernBonemeal;
} else if(finpots.equals("Prayer Renewal")) {
	log(new Color(34, 139, 34), "You are Making: Prayer Renewal Potion!");
    unfPotToUse = unfFellstalk;
    ingredientToUse = morchellaMushroom;
}
			guiWait = false; 
            g.dispose(); 
		} else if (groundingItems.isSelected()) {
			groundItem = true;
			makingUnfinished.disable();
			makingFinished.disable();
			cleaningHerbs.disable();
			if(grounditemss.equals("Unicorn Horn")) {
				log(new Color(34, 139, 34), "You are Grinding: Unicorn Horn!");
			    itemToGround = unicornHorn;
			} else if(grounditemss.equals("Chocolate Bar")) {
				log(new Color(34, 139, 34), "You are Grinding: Chocolate Bar!");
			    itemToGround = chocolateBar;
			} else if(grounditemss.equals("Kebbit Teeth")) {
				log(new Color(34, 139, 34), "You are Grinding: Kebbit Teeth!");
			    itemToGround = kebbitTeeth;
		} else if(grounditemss.equals("Gorak Claw")) {
			log(new Color(34, 139, 34), "You are Grinding: Gorak Claws!");
		    itemToGround = gorakClaw;
	} else if(grounditemss.equals("Bird Nest")) {
		log(new Color(34, 139, 34), "You are Grinding: Bird's Nest!");
	    itemToGround = birdNest;
} else if(grounditemss.equals("Desert Goat Horn")) {
	log(new Color(34, 139, 34), "You are Grinding: Desert Goat Horn!");
    itemToGround = desertGoatHorn;
} else if(grounditemss.equals("Blue Dragon Scales")) {
	log(new Color(34, 139, 34), "You are Grinding: Blue Dragon Scales!");
    itemToGround = blueDragonScale;
} else if(grounditemss.equals("Mud Rune")) {
	log(new Color(34, 139, 34), "You are Grinding: Mud Rune!");
    itemToGround = mudRune;
} else if(grounditemss.equals("Ashes")) {
	log(new Color(34, 139, 34), "You are Grinding: Ashes!");
    itemToGround = ashes;
} else if(grounditemss.equals("Poison Karambwan")) {
	log(new Color(34, 139, 34), "You are Grinding: Poison Karambwan!");
    itemToGround = poisonKarambwan;
} else if(grounditemss.equals("Fishing Bait")) {
	log(new Color(34, 139, 34), "You are Grinding: Fishing Bait!");
    itemToGround = fishingBait;
} else if(grounditemss.equals("Guam")) {
	log(new Color(34, 139, 34), "You are Grinding: Guam!");
    itemToGround = cleanGuam;
} else if(grounditemss.equals("Marrentill")) {
	log(new Color(34, 139, 34), "You are Grinding: Marrentill!");
    itemToGround = cleanMarrentill;
}  else if(grounditemss.equals("Tarromin")) {
	log(new Color(34, 139, 34), "You are Grinding: Tarromin!");
    itemToGround = cleanTarromin;
} else if(grounditemss.equals("Harralander")) {
	log(new Color(34, 139, 34), "You are Grinding: Harralander!");
    itemToGround = cleanHarralander;
} else if(grounditemss.equals("Seaweed")) {
	log(new Color(34, 139, 34), "You are Grinding: Seaweed!");
    itemToGround = seaWeed;
} else if(grounditemss.equals("Neem Drupe")) {
	log(new Color(34, 139, 34), "You are Grinding: Neem Drupe!");
    itemToGround = neemDrupe;
}
			guiWait = false; 
            g.dispose(); 
		} else if(makingOverloads.isSelected()) {
			makeOvl = true;
		}
			guiWait = false; 
            g.dispose(); 
		}
		
		private void initComponents() {
			label1 = new JLabel();
			cleaningHerbs = new JCheckBox();
			makingUnfinished = new JCheckBox();
			groundingItems = new JCheckBox();
			makingFinished = new JCheckBox();
			makingOverloads = new JCheckBox();
			herbComboBox = new JComboBox();
			unfComboBox = new JComboBox();
			finComboBox = new JComboBox();
			groundComboBox = new JComboBox();
			button1 = new JButton();

			setTitle("Beuner's AIO Herblore v" + PROPS.version());
			Container contentPane = getContentPane();

			label1.setText("Beuner's AIO Herblore v" + PROPS.version());
			label1.setFont(new Font("Tahoma", Font.BOLD, 22));

			cleaningHerbs.setText("Clean Herbs");
			cleaningHerbs.setFont(new Font("Tahoma", Font.PLAIN, 14));

			makingUnfinished.setText("Make Unfinished");
			makingUnfinished.setFont(new Font("Tahoma", Font.PLAIN, 14));

			groundingItems.setText("Ground Items");
			groundingItems.setFont(new Font("Tahoma", Font.PLAIN, 14));

			makingFinished.setText("Make Finished");
			makingFinished.setFont(new Font("Tahoma", Font.PLAIN, 14));

			makingOverloads.setText("Make Overload");
			makingOverloads.setFont(new Font("Tahoma", Font.PLAIN, 14));

			herbComboBox.setModel(new DefaultComboBoxModel(new String[] {
				"Guam",
				"Marrentill",
				"Tarromin",
				"Harralander",
				"Ranarr",
				"Toadflax",
				"Spirit Weed",
				"Irit",
				"Wergali",
				"Avantoe",
				"Kwuarm",
				"Snapdragon",
				"Cadantine",
				"Lantadyme",
				"Dwarf Weed",
				"Torstol",
				"Fellstalk"
			}));

			unfComboBox.setModel(new DefaultComboBoxModel(new String[] {
				"Guam unf",
				"Marrentill unf",
				"Tarromin unf",
				"Harralander unf",
				"Ranarr unf",
				"Toadflax unf",
				"Spirit Weed unf",
				"Irit unf",
				"Wergali unf",
				"Avantoe unf",
				"Kwuarm unf",
				"Snapdragon unf",
				"Cadantine unf",
				"Lantadyme unf",
				"Dwarf Weed unf",
				"Torstol unf",
				"Fellstalk unf"
			}));

			finComboBox.setModel(new DefaultComboBoxModel(new String[] {
				"Attack potion",
				"Anti-poison",
				"Strength potion",
				"Stat Restore potion",
				"Energy potion",
				"Defence potion",
				"Agility potion",
				"Combat potion",
				"Prayer Restore potion",
				"Summoning potion",
				"Crafting potion",
				"Super Attack potion",
				"Super anti-poison",
				"Fishing potion",
				"Super Energy potion",
				"Hunter potion",
				"Super Strength potion",
				"Fletching potion",
				"Weapon poison",
				"Super Restore potion",
				"Super Defence potion",
				"Anti-Fire potion",
				"Ranging potion",
				"Magic potion",
				"Zamorak Brew",
				"Saradomin Brew",
				"Recover Special potion",
				"Super Antifire potion",
				"Extreme Attack potion",
				"Extreme Strength potion",
				"Extreme Defence potion",
				"Extreme Magic potion",
				"Extreme Ranging potion",
				"Super Prayer Restore",
				"Prayer Renewal"
			}));

			groundComboBox.setModel(new DefaultComboBoxModel(new String[] {
				"Unicorn Horn",
				"Chocolate Bar",
				"Kebbit Teeth",
				"Gorak Claw",
				"Bird Nest",
				"Desert Goat Horn",
				"Blue Dragon Scales",
				"Mud Rune",
				"Ashes",
				"Poison Karambwan",
				"Fishing Bait",
				"Guam",
				"Marrentill",
				"Tarromin",
				"Harralander",
				"Seaweed",
				"Neem Drupe"
			}));

			button1.setText("Start Herblore!");
			button1.setFont(new Font("Tahoma", Font.BOLD, 10));
			button1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					button1ActionPerformed();
				}
			});

			GroupLayout contentPaneLayout = new GroupLayout(contentPane);
			contentPane.setLayout(contentPaneLayout);
			contentPaneLayout.setHorizontalGroup(
				contentPaneLayout.createParallelGroup()
					.addGroup(contentPaneLayout.createSequentialGroup()
						.addGroup(contentPaneLayout.createParallelGroup()
							.addGroup(contentPaneLayout.createSequentialGroup()
								.addGap(55, 55, 55)
								.addComponent(label1, GroupLayout.PREFERRED_SIZE, 274, GroupLayout.PREFERRED_SIZE))
							.addGroup(contentPaneLayout.createSequentialGroup()
								.addGap(46, 46, 46)
								.addGroup(contentPaneLayout.createParallelGroup()
									.addComponent(cleaningHerbs)
									.addComponent(makingUnfinished)
									.addComponent(makingFinished)
									.addComponent(groundingItems)
									.addComponent(makingOverloads))
								.addGap(75, 75, 75)
								.addGroup(contentPaneLayout.createParallelGroup()
									.addComponent(button1, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
									.addGroup(contentPaneLayout.createParallelGroup()
										.addComponent(groundComboBox, 0, 120, Short.MAX_VALUE)
										.addComponent(finComboBox, 0, 120, Short.MAX_VALUE)
										.addComponent(unfComboBox, 0, 120, Short.MAX_VALUE)
										.addComponent(herbComboBox, 0, 120, Short.MAX_VALUE)))))
						.addGap(65, 65, 65))
			);
			contentPaneLayout.setVerticalGroup(
				contentPaneLayout.createParallelGroup()
					.addGroup(contentPaneLayout.createSequentialGroup()
						.addGap(24, 24, 24)
						.addComponent(label1, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
						.addGap(18, 18, 18)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(cleaningHerbs)
							.addComponent(herbComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(makingUnfinished)
							.addComponent(unfComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(makingFinished)
							.addComponent(finComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
							.addComponent(groundingItems)
							.addComponent(groundComboBox, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
						.addGap(18, 18, 18)
						.addGroup(contentPaneLayout.createParallelGroup()
							.addComponent(button1, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
							.addComponent(makingOverloads))
						.addContainerGap(23, Short.MAX_VALUE))
			);
			pack();
			setLocationRelativeTo(getOwner());

			ButtonGroup buttonGroup1 = new ButtonGroup();
			buttonGroup1.add(cleaningHerbs);
			buttonGroup1.add(makingUnfinished);
			buttonGroup1.add(groundingItems);
			buttonGroup1.add(makingFinished);
			buttonGroup1.add(makingOverloads);
		}

		private JLabel label1;
		private JCheckBox cleaningHerbs;
		private JCheckBox makingUnfinished;
		private JCheckBox groundingItems;
		private JCheckBox makingFinished;
		private JCheckBox makingOverloads;
		private JComboBox herbComboBox;
		private JComboBox unfComboBox;
		private JComboBox finComboBox;
		private JComboBox groundComboBox;
		private JButton button1;
	}

	@Override
	public void onRepaint(Graphics g1) {
		if(game.isLoggedIn()) {
			
			expGained = skills.getCurrentExp(Skills.HERBLORE) - startExp;
			
			long millis = System.currentTimeMillis() - startTime;
			long hours = millis / (1000 * 60 * 60);
			millis -= hours * (1000 * 60 * 60);
			long minutes = millis / (1000 * 60);
			millis -= minutes * (1000 * 60);
			long seconds = millis / 1000;
			
			float xpsec = 0;
			if((minutes > 0 || hours > 0 || seconds > 0)&& expGained > 0) {
				xpsec = ((float) expGained)/(float)(seconds + (minutes*60) + (hours*60*60));
			}
			float xpmin = xpsec * 60;
			float xphour = xpmin * 60;
			
			float finpotsec = 0;
			if((minutes > 0 || hours > 0 || seconds > 0)&& finPotsMade > 0) {
				finpotsec = ((float) finPotsMade)/(float)(seconds + (minutes*60) + (hours*60*60));
			}
			float finpotmin = finpotsec * 60;
			float finpothour = finpotmin * 60;
			
			float unfpotsec = 0;
			if((minutes > 0 || hours > 0 || seconds > 0)&& unfPotsMade > 0) {
				unfpotsec = ((float) unfPotsMade)/(float)(seconds + (minutes*60) + (hours*60*60));
			}
			float unfpotmin = unfpotsec * 60;
			float unfpothour = unfpotmin * 60;
			
			float herbsec = 0;
			if((minutes > 0 || hours > 0 || seconds > 0)&& herbCleaned > 0) {
				herbsec = ((float) herbCleaned)/(float)(seconds + (minutes*60) + (hours*60*60));
			}
			float herbmin = herbsec * 60;
			float herbhour = herbmin * 60;
			
			float groundsec = 0;
			if((minutes > 0 || hours > 0 || seconds > 0)&& itemGround > 0) {
				groundsec = ((float) itemGround)/(float)(seconds + (minutes*60) + (hours*60*60));
			}
			float groundmin = groundsec * 60;
			float groundhour = groundmin * 60;
			
			Graphics2D g = (Graphics2D) g1;
			g.setColor(color1);
	        g.fillRect(396, 459, 98, 12);
		    g.setColor(color2);
	        g.drawRect(396, 459, 98, 12);
	        g.setColor(color3);
	        g.setFont(font1);
			g.drawString("Hide/Show Paint.", 396, 470);
			if(showPaint) {
			g.drawImage(img1, -36, 207, null);
	        g.setColor(color1);
	        g.fillRoundRect(9, 345, 500, 128, 16, 16);
	        g.setColor(color2);
	        g.setStroke(stroke1);
	        g.drawRoundRect(9, 345, 500, 128, 16, 16);
	        g.setColor(color1);
	        g.fillRoundRect(16, 356, 485, 110, 16, 16);
	        g.setColor(color2);
	        g.drawRoundRect(16, 356, 485, 110, 16, 16);
	        g.setFont(font1);
	        g.setColor(color3);
	        g.drawString("Runtime: " + hours + ":" + minutes + ":" + seconds, 22, 372);
	        g.drawString("Herbs Cleaned: " + herbCleaned, 22, 386);
	        g.drawString("Herbs/hr: " + (int)herbhour, 22, 401);
	        g.drawString("Unf Pots Made: " + unfPotsMade, 22, 417);
	        g.drawString("Unf Pots/hr: " + (int)unfpothour, 22, 433);
	        g.drawString("Fin Pots Made: " + finPotsMade, 22, 446);
	        g.drawString("Fin Pots/hr: " + (int)finpothour, 23, 460);
	        g.drawString("Items Ground: " + itemGround, 180, 372);
	        g.drawString("Grounds/hr: " + (int)groundhour, 180, 387);
	        g.drawString("Xp Gained: " + expGained, 180, 402);
	        g.drawString("Xp/hr: " + (int)xphour, 180, 416);
	        g.drawString("%TNL: " + skills.getPercentToNextLevel(Skills.HERBLORE) + "%", 180, 430);
	        g.drawString("Status: ", 180, 443);
	        g.drawString("Hide/Show Paint.", 396, 470);
	        g.drawImage(img2, 590, 266, null);
	        g.drawImage(img3, 384, 370, null);
	        g.drawImage(img4, 495, 348, null);
	        g.drawImage(img4, 495, 440, null);
	        g.drawImage(img4, 5, 348, null);
	        g.drawImage(img4, 5, 440, null);	        
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
	}

	@Override
	public void messageReceived(MessageEvent messagee) {
		String message = messagee.getMessage().toLowerCase();
		if(message.contains("into the vial of water")) {
			unfPotsMade++;		
		} else if(message.contains("mix the")) {
			finPotsMade++;
		} else if(message.contains("clean the dirt")) {
			herbCleaned++;
		} else if(message.contains("grind the")) {
			itemGround++;
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
        RSComponent inter = interfaces.get(137).getComponent(176);
        if (inter.getArea().contains(e.getPoint())) {
            showPaint = !showPaint;
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
}
