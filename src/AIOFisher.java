import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.LinkedList;

import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSWeb;
import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Timer;

@ScriptManifest(
authors = {"Purple"},
version = 1.02,
keywords = ("Fisher, AIO, Purple"),
description = "A Simple AIO Fisher",
name = "PurpleFisher"
)

public class PurpleFisher extends Script implements PaintListener, MessageListener, MouseListener {
//Update's ScriptManifest For The Rest Of Script
  ScriptManifest PROPS = getClass().getAnnotation(ScriptManifest.class);
	
	
	
	
//---------Variables----------------	
	
	
	
	
	
//Animations
	final int Runninganimation = -1;
	
//Status & Strings
	private  static String status = "Please Wait!";
	private  static String status1 = "Null";
	
	private String interactFish;
	private String interactBank;
	private int fishID = 0;
	private int BankID = 0;
	private int npcID;
	private int equipment = 0;
	int ID = 0;
	public boolean Mode;
	
	
//Paint Variables
	long startTime;
	private int Level = 0;
	private int  fishCaught;
	private int fishEquipment;
	private int XPTL = 0;
	Rectangle close = new Rectangle(430, 305, 80, 43);
	Point p;
	boolean hide = false;
	private int StartExp = 0;
	private int ExpGained = 0;
	public long startTimez, timeRunning;
	
//paint Cursor
	private static final Color MOUSE_COLOR = new Color(112, 200, 241), 
	MOUSE_BORDER_COLOR = new Color(112, 200, 241), 
	MOUSE_CENTER_COLOR = new Color(74, 67, 67);   
	Point p2; 
	private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();


//Tile Paths
	RSTile fishingTile;
	RSTile bankTile;
	RSWeb pathBank;
	RSWeb fishPath;


//RSAreas
	public RSArea FishArea;
	public RSArea bankArea;

	private boolean guiWait = true;
	GUI g = new GUI();

//Draynor Area
	int FishNPC = 327;
	public RSTile dFishingTile =   new RSTile(3086, 3230);
	public RSTile dBankTile = new RSTile(3092, 3244);
	

	
//Barbarian Village Area
	int flyFishNPC = 328;
	public RSTile bFishTile = new RSTile(3105, 3430);
    public RSTile bBankTile = new RSTile(3094, 3490);
   
//Catherby Fish Area
    public RSTile cBankTile = new RSTile(2809, 3441);
    public RSTile cFishTile = new RSTile(2846, 3430);
    int cNPC = 0;
    
//Fishing Guild Area
    public RSTile fgFishTile = new RSTile(2604, 3411);
    public RSTile fgBankTile = new RSTile(2586, 3422);
    int fgNPC = 0;
    
//Fishing Colony Monkfish
    public RSTile fcFishTile = new RSTile(2342, 3701);
    public RSTile fcBankTile = new RSTile(2330, 3689);
    int fcNPC = 0;
    
//Barbarian Heavy Rod Fishing
    public RSTile baFishTile = new RSTile(2499, 3507);
    int baNPC = 0;
    
    
//Shilo Village 
    public RSTile svFishTile = new RSTile(2852, 2969);
    public RSTile svBankTile = new RSTile(2852, 2955);
    int svNPC = 0;
    
    

//-------------Start up-----------------------------

//Start Up
public boolean onStart() {
	
   	
	log(Color.MAGENTA,"Enjoy PurpleFisher! Check the forums for the latest Script!");
	log(Color.MAGENTA,"http://www.rarebot.com/community/index.php/topic,2154.0.html");
	startTime = System.currentTimeMillis();
	StartExp = skills.getCurrentExp(Skills.FISHING);
	camera.setPitch(100);
	
	g.setVisible(true);
	while(guiWait) sleep(500);

    mouse.setSpeed(random(4, 7));

	return true;
}

//----------------Loop--------------------------------

//Main Loop
@Override
public int loop() {
	checkInvo();
	CheckState();
			
	return random (450, 600);
} 

//-------------------Methods-------------------------------

private void CheckState() {

	if (Mode == true) {
		  dropMethod(); 
		  }else{
			  bankMethod();
		  }
	
		
			}
private void dropMethod() {
		checkInterfaces();
	if(inventory.isFull()) {
		status = "dropping!";
		inventory.dropAllExcept(fishEquipment, equipment);
	}else{
		if(atFishArea()){
            doFish();
            antiban();
		}
	}
}

private void bankMethod() {
		checkInterfaces();
	if(inventory.isFull()) {
		if(atBank()){
		            doBank();             
			}else{
		            PathBank();
		            antiban1();
		        }
			}else{
		if(atFishArea()){
		            doFish();
		            antiban();
		           
		        }else{
		            fishPath();
		            antiban1();
		        }
		    }
}

private void checkInvo() {
	if (inventory.containsAll(fishEquipment, equipment)) {
}else{
	if (!inventory.containsAll(fishEquipment, equipment)) {
		log(Color.RED,"Required items NOT found or ran out of supplies.");

                stopScript();

        }       
	}
}


private void checkInterfaces() {
	if(interfaces.get(594).getComponent(88).isValid()) { //Report Abuse
    	interfaces.getComponent(594, 88).doClick(true);
	}
	if(interfaces.get(620).getComponent(18).isValid()) { //Piscatoris Fishing Colony
    	interfaces.getComponent(620, 18).doClick(true);
	}
	
}

private void fishPath() {
    status = "Running To Fish Area.";
    fishPath = web.getWeb(getMyPlayer().getLocation(), fishingTile);
    fishPath.step();
    try {

        // your code here

    } catch (Exception e) {}

}
    



private void doFish() {
	RSNPC Fish = npcs.getNearest(fishID);
	status = "Fishing!";
	if (idle() && inArea(FishArea) && Fish != null && Fish.isOnScreen()) {
		status = "Attempting to fish.";
            Fish.interact(interactFish);
            sleep (250, 600);
            if (Fish != null) {
                if (!Fish.isOnScreen()) {
                	camera.turnTo(npcs.getNearest(fishID));
                    walking.walkTileMM(Fish.getLocation()); }                  	
        }
	}   
                
}



private boolean idle() {
    return getMyPlayer().getInteracting() == null;
}


private void PathBank() {
    status = "Running to bank.";
    pathBank = web.getWeb(getMyPlayer().getLocation(), bankTile);
    pathBank.step();
}
   


private void doBank() {
    status = "Banking!";
    if(interfaces.get(109).getComponent(14).isValid()) {
    	interfaces.getComponent(109, 14).doClick(true);
    	sleep(1000);
}else{
    if(bank.isOpen()){ 
    	status = "Depositing Fish.";
            bank.depositAllExcept(equipment, fishEquipment);
        bank.close();
        sleep(150, 250);
    }else{
     bankchoice();
    	}
	}
}

    	 
private void bankchoice(){
	  RSObject bank = objects.getNearest(BankID);
	  RSNPC banker = npcs.getNearest(npcID);
	  	  
	  if (banker != null && banker.isOnScreen() && bank != null && bank.isOnScreen()){		  
		   bank.interact(interactBank);
		   sleep (400, 1300);
		   }
		   
		   else if (banker !=null && banker.isOnScreen()){
		   banker.interact(interactBank);
		   sleep (400, 1300);
		  }
		  else if (bank !=null && bank.isOnScreen()){
			  
		   bank.interact(interactBank);
		   sleep (400, 1300);
		  }
		  
		 }

private boolean atBank() {
	if (!inArea(bankArea)) {
        return false;
	}
	return true;
}

   
private boolean atFishArea() {
	if (!inArea(FishArea)) {
        return false;
	}
	return true;
}
        
        

private boolean inArea(final RSArea area) {
        return area.contains(getMyPlayer().getLocation());
    }
//--------------Paint-----------------------

//Paint URL
private Image getImage(String url) {
    try {
        return ImageIO.read(new URL(url));
    } catch(IOException e) {
        return null;
    }
}

private final Color color1 = new Color(112, 200, 241);

private final Font font1 = new Font("Arial", 1, 12);

private final Image img1 = getImage("http://img687.imageshack.us/img687/5382/paintjr.png");
private final Image img2 = getImage("http://img706.imageshack.us/img706/8991/hidebutton.png");
private final Image img3 = getImage("http://img20.imageshack.us/img20/6961/showbutton.png");



int Ph(int arg0, long arg1) {
	if (arg0 > 0) {
	return (int) ((arg0) * 3600000.0 / arg1);
	}
	return 0;
	}

private long timeTnl(int xpTnl, int xpPh) {
if (xpPh > 0) {
long timeTNL = (long) ((double) xpTnl / (double) xpPh * 3600000);
return timeTNL;
}
return 0;
}

private String formatTime(final long time) {
final int sec = (int) (time / 1000), d = sec / 86400, h = sec / 3600, m = sec / 60 % 60, s = sec % 60;
return (d < 10 ? "0" + d : d) + ":" + (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (s < 10 ? "0" + s : s);
}
 
public String timeTillLevel(final long expHour) {
return Timer.format((long) ((double) skills.getExpToNextLevel(Skills.FISHING) / (double) expHour));
}
 


//paint 
public void onRepaint(Graphics g1) {
    Graphics2D g = (Graphics2D)g1;
    
    if(!hide){
    
    	
    ExpGained = skills.getCurrentExp(Skills.FISHING) - StartExp;
    XPTL = skills.getExpToNextLevel(Skills.FISHING);
    timeRunning = System.currentTimeMillis() - startTime;
    	 


    g.drawImage(img1, 5, 343, null);
    g.drawImage(img2, 434, 305, null);
    g.setFont(font1);
    g.setColor(color1);
    g.drawString("Fish Caught: " + fishCaught, 20, 410);
    g.drawString("Time Til Level: " + formatTime(timeTnl(XPTL, Ph(ExpGained, timeRunning))), 20, 430);
    g.drawString("Elapsed Time: " + formatTime(timeRunning), 20, 450);
    g.drawString("XP Per /H:  " + NumberFormat.getIntegerInstance().format(ExpGained *3600000D / (System.currentTimeMillis() - startTime)), 180, 410);
    g.drawString("XP Gained: " + NumberFormat.getIntegerInstance().format(ExpGained), 180, 430);
    g.drawString("XP TNL: " + NumberFormat.getIntegerInstance().format(XPTL), 180, 450);
    g.drawString("Current Level: " + skills.getCurrentLevel(Skills.FISHING), 293, 410);
    g.drawString(": " + PROPS.version(), 70, 470);
    g.drawString("Levels Gained: " + Level, 293, 430);
    g.drawString("Status: " + (status), 180, 470);
    g.drawString("Antiban: " + (status1), 293, 450); }
    
    if(hide){
    	g.drawImage(img3, 434, 305, null);
    } 
    
    
    drawMouse(g); 
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
    { }
    }
    
    



//-------------Custom Mouse---------------------

//Custom Mouse
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
private class MousePathPoint extends Point {
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

        public Color getColor() { 
                return new Color( 
                                112, 
                                200, 
                                241, 
                                toColor(256 * ((finishTime - System.currentTimeMillis()) / lastingTime))); 
        } 
}
   

//-----------------Fish Caught-------------------------------

//Fish Caught
public void messageReceived(MessageEvent e) {

	String txt = e.getMessage();
	
	if(txt.contains("You catch")) { 
		fishCaught++;
	}
	if(txt.contains("You've just advanced a Fishing level!")) { 
		Level++;
	}
}

//----------------Anti Ban-------------------------------

//Anti Ban
public void antiban(){
	status1 = "Sleeping";
    int b = random(2, 23);
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
        	status1 = "Camera";
            camera.setAngle(random(30, 70));
            sleep(200, 600);                		            
        }            
        break;
    case 3:
        if (random(0, 18) == 5) {
        	status1 = "Moving Mouse";
            mouse.moveOffScreen();
            sleep(random(600, random(1200, 2000)));
        }
        break;
    case 4:
        if (random(0, 19) == 13) {
        	status1 = "Camera";
        	
            camera.setAngle(random(134, 280));
            sleep(300, 600);                
           
        }            
        break;
    case 5:
        if (random(0, 21) == 7) {
        	status1 = "Moving Mouse";
       	 mouse.moveSlightly();
            sleep(100, 200);
            mouse.moveRandomly(25, 380);
        }
    case 6:
		if (random(0, 100) == 1) {
			status1 = "Checking xp.";
			game.openTab(Tab.STATS);
			if (skills.doHover(Skills.INTERFACE_HERBLORE)) {
				sleep(2100, 3400);
		}	
        break;
		}
  default:
        break;
    }
}

//----------------Walking Anti Ban-------------------------

//Walking Anti ban
public void antiban1(){
	status1 = "Sleeping";
    int b = random(2, 8);
    switch (b) {
        
    case 1:
        if (random(1, 3) == 2) {
        	status1 = "Camera";
            camera.setAngle(random(30, 70));
            sleep(30, 50);                		            
        }            
        break;
    case 2:
        if (random(0, 18) == 8) {
        	status1 = "Camera";
        	
            camera.setAngle(random(10, 40));
            sleep(20, 60);                
           
        }            
        break;
    case 3:
        if (random(0, 18) == 5) {
        	status1 = "Camera";
        	
            camera.setAngle(random(15, 34));
            sleep(30, 75);                
           
        }
        break;
    case 4:
		if (random(0, 100) == 1) {
			status1 = "Checking xp.";
			game.openTab(Tab.STATS);
			if (skills.doHover(Skills.INTERFACE_HERBLORE)) {
				sleep(45, 90);
		}	
        break;
		}
  default:
        break;
    }
}

//---------------Hide/Show Paint Method------------------------------

//Hide/Show Paint Void
@Override
public void mouseClicked(MouseEvent e) {
        p = e.getPoint();
if(close.contains(p) && !hide){
    hide = true;
    } else if(close.contains(p) && hide){
    hide = false;
    }      
}


//Paint MouseListener
@Override
public void mouseEntered(MouseEvent e) {
       
}

@Override
public void mouseExited(MouseEvent e) {
        
}

@Override
public void mousePressed(MouseEvent e) {
       
}

@Override
public void mouseReleased(MouseEvent e) {
        
}



//------------When Script Stops---------------------

//When Script Ends
public void onFinish(){
	log(Color.RED,"Thanks for using PurpleFisher " + PROPS.version());
	log(Color.RED,"Stay tuned for updates!");
	env.saveScreenshot(true);
	}



//-----------------GUI-------------------------------------
//GUI Class
public class GUI extends JFrame {
	public GUI() {
		initComponents();
	}

	private void AreaActionPerformed(ActionEvent e) {
		String chosen = selectArea.getSelectedItem().toString();
		if(chosen.equals("Draynor Shrimp")) {
			fishID = FishNPC;
			BankID = 2015;
			interactBank = "Bank Counter";
			fishingTile = dFishingTile;
			bankTile = dBankTile;
			interactFish = "net fishing spot";
			
			fishEquipment = 303;
			equipment = 303;
			FishArea = new RSArea(new RSTile(3075, 3223), new RSTile(3088, 3236));
			bankArea = new RSArea(new RSTile(3088, 3240), new RSTile(3096, 3246));
			
		} else if(chosen.equals("Draynor Sardines")){
			fishID = FishNPC;
			equipment = 307;
			BankID = 2015;
			interactBank = "Bank Counter";
			interactFish = "Bait Fishing spot";
			fishingTile = dFishingTile;
			bankTile = dBankTile;
			fishEquipment = 313;
			FishArea = new RSArea(new RSTile(3075, 3223), new RSTile(3088, 3236));
			bankArea = new RSArea(new RSTile(3088, 3240), new RSTile(3096, 3246));
			
			
		}  else if(chosen.equals("Barbarian Village Salmon/Trout")){
			fishID = flyFishNPC;
			equipment = 309;
			fishEquipment = 314;
			BankID = 42217;
			interactBank = "Bank Counter";
			interactFish = "Lure Fishing spot";
			fishingTile = bFishTile;
			bankTile = bBankTile;
			
			FishArea = new RSArea(new RSTile(3099, 3420), new RSTile(3112, 3437));
			bankArea = new RSArea(new RSTile(3090, 3487), new RSTile(3099, 3497));
			
		} else if(chosen.equals("Barbarian Village Pike")){
			fishID = flyFishNPC;
			equipment = 307;
			BankID = 42373;
			interactBank = "Bank Counter";
			interactFish = "Bait Fishing spot";
			fishingTile = bFishTile;
			bankTile = bBankTile;
			fishEquipment = 313;
			
			FishArea = new RSArea(new RSTile(3099, 3420), new RSTile(3112, 3437));
			bankArea = new RSArea(new RSTile(3091, 3488), new RSTile(3098, 3496));
			
		} else if(chosen.equals("BA Grotto Heavy Feathers")){
			fishID = baNPC = 2722;
			equipment = 314;
			BankID = 0;
			interactBank = "Bank Counter";
			interactFish = "use-rod Fishing spot";
			fishingTile = baFishTile;
			bankTile = bBankTile;
			fishEquipment = 11323;
			
			FishArea = new RSArea(new RSTile(2495, 3491), new RSTile(2510, 3517));
			
		} else if(chosen.equals("BA Grotto Heavy Bait")){
			fishID = baNPC = 2722;
			equipment = 313;
			BankID = 0;
			interactBank = "Bank Counter";
			interactFish = "use-rod Fishing spot";
			fishingTile = baFishTile;
			bankTile = bBankTile;
			fishEquipment = 11323;
			
			FishArea = new RSArea(new RSTile(2495, 3491), new RSTile(2510, 3517));
			
		} else if(chosen.equals("Catherby Lobsters")){
			fishID = cNPC = 321;
			equipment = 301;
			BankID = 2213;
			interactFish = "Cage Fishing spot";
			interactBank = "Bank Bank booth";
			fishingTile = cFishTile;
			bankTile = cBankTile;
			fishEquipment = 301;
			
			FishArea = new RSArea(new RSTile(2835, 3420), new RSTile(2862, 3432));
			bankArea = new RSArea(new RSTile(2806, 3440), new RSTile(2812, 3445));
			
		} else if(chosen.equals("Catherby Tuna/Swordfish")){
			fishID = cNPC = 321;
			equipment = 311;
			BankID = 2213;
			interactBank = "Bank Bank booth";
			interactFish = "Harpoon Fishing spot";
			fishingTile = cFishTile;
			bankTile = cBankTile;
			fishEquipment = 311;
			
			FishArea = new RSArea(new RSTile(2835, 3420), new RSTile(2862, 3432));
			bankArea = new RSArea(new RSTile(2806, 3440), new RSTile(2812, 3445));
			
		} else if(chosen.equals("Catherby Sharks")){
			fishID = cNPC = 322;
			equipment = 311;
			BankID = 2213;
			interactBank = "Bank Bank booth";
			interactFish = "Harpoon Fishing spot";
			fishingTile = cFishTile;
			bankTile = cBankTile;
			fishEquipment = 311;
			npcID = 495;
			
			FishArea = new RSArea(new RSTile(2835, 3420), new RSTile(2862, 3432));
			bankArea = new RSArea(new RSTile(2806, 3440), new RSTile(2812, 3445));
			
		} else if(chosen.equals("Catherby Mackerel")){
			fishID = cNPC = 322;
			equipment = 305;
			BankID = 2213;
			interactBank = "Bank Bank booth";
			interactFish = "Net Fishing spot";
			fishingTile = cFishTile;
			bankTile = cBankTile;
			fishEquipment = 305;
			
			FishArea = new RSArea(new RSTile(2835, 3420), new RSTile(2862, 3432));
			bankArea = new RSArea(new RSTile(2806, 3440), new RSTile(2812, 3445));
			
		} else if(chosen.equals("Catherby Shrimp")){
			fishID = cNPC = 320;
			equipment = 305;
			BankID = 2213;
			interactBank = "Bank Bank booth";
			interactFish = "Net Fishing spot";
			fishingTile = cBankTile;
			bankTile = cBankTile;
			fishEquipment = 305;
			
			FishArea = new RSArea(new RSTile(2835, 3420), new RSTile(2862, 3432));
			bankArea = new RSArea(new RSTile(2806, 3440), new RSTile(2812, 3445));
			
		} else if(chosen.equals("Catherby Sardines/Herring")){
			fishID = cNPC = 320;
			equipment = 307;
			BankID = 2213;
			interactBank = "Bank Bank booth";
			interactFish = "Bait Fishing spot";
			fishingTile = cBankTile;
			bankTile = cBankTile;
			fishEquipment = 313;
			
			FishArea = new RSArea(new RSTile(2835, 3420), new RSTile(2862, 3432));
			bankArea = new RSArea(new RSTile(2806, 3440), new RSTile(2812, 3445));
			
		} else if(chosen.equals("Catherby Cod/Bass")){
			fishID = cNPC = 322;
			equipment = 305;
			BankID = 2213;
			interactBank = "Bank Bank booth";
			interactFish = "Net Fishing spot";
			fishingTile = cBankTile;
			bankTile = cBankTile;
			fishEquipment = 305;
			
			FishArea = new RSArea(new RSTile(2835, 3420), new RSTile(2862, 3432));
			bankArea = new RSArea(new RSTile(2806, 3440), new RSTile(2812, 3445));
			
		} else if(chosen.equals("Fishing Guild Sharks")){
			fishID = fgNPC = 313;
			equipment = 311;
			BankID = 49018;
			interactBank = "Bank Bank booth";
			interactFish = "Harpoon Fishing spot";
			fishingTile = fgFishTile;
			bankTile = fgBankTile;
			fishEquipment = 311;
			
			FishArea = new RSArea(new RSTile(2599, 3406), new RSTile(2611, 3419));
			bankArea = new RSArea(new RSTile(2583, 3420), new RSTile(2587, 3424));
			
		} else if(chosen.equals("Fishing Guild Lobsters")){
			fishID = fgNPC = 312;
			equipment = 301;
			BankID = 49018;
			interactBank = "Bank Bank booth";
			interactFish = "Cage Fishing spot";
			fishingTile = fgFishTile;
			bankTile = fgBankTile;
			fishEquipment = 301;
			
			FishArea = new RSArea(new RSTile(2599, 3406), new RSTile(2611, 3419));
			bankArea = new RSArea(new RSTile(2583, 3420), new RSTile(2587, 3424));
			
		} else if(chosen.equals("Fishing Guild Tuna/Swordfish")){
			fishID = fgNPC = 312;
			equipment = 311;
			BankID = 49018;
			interactBank = "Bank Bank booth";
			interactFish = "Harpoon Fishing spot";
			fishingTile = fgFishTile;
			bankTile = fgBankTile;
			fishEquipment = 311;
			
			FishArea = new RSArea(new RSTile(2599, 3406), new RSTile(2611, 3418));
			bankArea = new RSArea(new RSTile(2583, 3420), new RSTile(2587, 3424));
			
		} else if(chosen.equals("Fishing Guild Cod/Bass")){
			fishID = fgNPC = 313;
			equipment = 305;
			BankID = 49018;
			interactBank = "Bank Bank booth";
			interactFish = "Net Fishing spot";
			fishingTile = fgFishTile;
			bankTile = fgBankTile;
			fishEquipment = 305;
			
			FishArea = new RSArea(new RSTile(2599, 3406), new RSTile(2611, 3419));
			bankArea = new RSArea(new RSTile(2583, 3420), new RSTile(2587, 3424));
			
		} else if(chosen.equals("Fishing Colony Monkfish")){
			fishID = fcNPC = 3848;
			equipment = 303;
			npcID = 3824;
			interactBank = "Bank Arnold Lydspor";
			interactFish = "Net Fishing spot";
			fishingTile = fcFishTile;
			bankTile = fcBankTile;
			fishEquipment = 303;
			
			FishArea = new RSArea(new RSTile(2332, 3697), new RSTile(2353, 3710));
			bankArea = new RSArea(new RSTile(2327, 3686), new RSTile(2332, 3693));
			
		} else if(chosen.equals("Fishing Colony Tuna/Swordfish")){
			fishID = fcNPC = 3848;
			equipment = 311;
			npcID = 3824;
			interactBank = "Bank Arnold Lydspor";
			interactFish = "Harpoon Fishing spot";
			fishingTile = fcFishTile;
			bankTile = fcBankTile;
			fishEquipment = 311;
			
			FishArea = new RSArea(new RSTile(2312, 3715), new RSTile(2355, 3715));
			bankArea = new RSArea(new RSTile(2327, 3686), new RSTile(2332, 3693));
			
		} else if(chosen.equals("Shilo Village Salmon/Trout")){
			fishID = svNPC = 317;
			equipment = 309;
			npcID = 499;
			interactBank = "Bank Banker";
			interactFish = "Lure Fishing spot";
			fishingTile = svFishTile;
			bankTile = svBankTile;
			fishEquipment = 314;
			
			FishArea = new RSArea(new RSTile(2832, 2963), new RSTile(2867, 2973));
			bankArea = new RSArea(new RSTile(2848, 2952), new RSTile(2855, 2957));
			 			
		} else {
			return;
		}
	}
		

	private void powerFishActionPerformed(ActionEvent e) {
		String chosen = powerFish.getSelectedItem().toString();
		if(chosen.equals("No")) {
			Mode = false; 
			
			
		} else if(chosen.equals("Yes")){
			Mode = true;
			
			
			
		} else {
			return;
		}
		guiWait = false;
		g.dispose();
	}

	private void initComponents() {
		// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
		// Generated using JFormDesigner Evaluation license - Purple Purple
		label1 = new JLabel();
		selectArea = new JComboBox();
		label2 = new JLabel();
		label3 = new JLabel();
		powerFish = new JComboBox();
		button1 = new JButton();

		//======== this ========
		setTitle("PurpleFisher GUI - Purple");
		setFont(new Font("Arial", Font.PLAIN, 18));
		Container contentPane = getContentPane();

		//---- label1 ----
		label1.setText("Select Area/Mode!");
		label1.setFont(new Font("Arial", Font.BOLD, 24));

		//---- selectArea ----
		selectArea.setModel(new DefaultComboBoxModel(new String[] {
			"Draynor Shrimp",
			"Draynor Sardines",
			"Barbarian Village Salmon/Trout",
			"Barbarian Village Pike",
			"BA Grotto Heavy Feathers",
			"BA Grotto Heavy Bait",
			"Catherby Lobsters",
			"Catherby Tuna/Swordfish",
			"Catherby Sharks",
			"Catherby Mackerel",
			"Catherby Sardines/Herring",
			"Catherby Cod/Bass",
			"Fishing Guild Sharks",
			"Fishing Guild Lobsters",
			"Fishing Guild Tuna/Swordfish",
			"Fishing Guild Cod/Bass",
			"Fishing Colony Monkfish",
			"Fishing Colony Tuna/Swordfish",
			"Shilo Village Salmon/Trout"
		}));

		//---- label2 ----
		label2.setText("Select Area");
		label2.setFont(new Font("Arial", Font.BOLD, 14));

		//---- label3 ----
		label3.setText("Power Fish");
		label3.setFont(new Font("Arial", Font.BOLD, 14));

		//---- powerFish ----
		powerFish.setModel(new DefaultComboBoxModel(new String[] {
			"No",
			"Yes"
		}));

		//---- button1 ----
		button1.setText("Fish");
		button1.setFont(new Font("Arial", Font.PLAIN, 11));
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AreaActionPerformed(e);
				powerFishActionPerformed(e);
			}
		});

		GroupLayout contentPaneLayout = new GroupLayout(contentPane);
		contentPane.setLayout(contentPaneLayout);
		contentPaneLayout.setHorizontalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(contentPaneLayout.createParallelGroup()
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(label3)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(powerFish, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 97, Short.MAX_VALUE)
							.addComponent(button1))
						.addGroup(contentPaneLayout.createSequentialGroup()
							.addComponent(label2)
							.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
							.addComponent(selectArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(label1))
					.addContainerGap())
		);
		contentPaneLayout.setVerticalGroup(
			contentPaneLayout.createParallelGroup()
				.addGroup(contentPaneLayout.createSequentialGroup()
					.addGap(16, 16, 16)
					.addComponent(label1)
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label2)
						.addComponent(selectArea, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18, 18, 18)
					.addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
						.addComponent(label3)
						.addComponent(powerFish, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(button1))
					.addContainerGap())
		);
		pack();
		setLocationRelativeTo(getOwner());
		// JFormDesigner - End of component initialization  //GEN-END:initComponents
	}

	// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
	// Generated using JFormDesigner Evaluation license - Purple Purple
	private JLabel label1;
	private JComboBox selectArea;
	private JLabel label2;
	private JLabel label3;
	private JComboBox powerFish;
	private JButton button1;
	// JFormDesigner - End of variables declaration  //GEN-END:variables
	}
}
