import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSTilePath;
import com.rarebot.script.wrappers.RSWeb;

@ScriptManifest(authors = { "Itz_Craze" }, name = "Crazy Summer Garden", version = 0.01, description = "Picks Herbs in Summer Garden for Profit and XP")
public class CrazySummerGarden extends Script implements PaintListener, MessageListener{
  
	//Object ID's
	private final static int GATE = 21687;
	private final static int HERBS = 21669;
	private static final int FOUNTAIN = 21764;
	private final static int BANK_CHEST = 2693;
	
	//NPC ID's
	private final static int ELEMENTAL_ONE = 5547;
	private final static int ELEMENTAL_TWO = 5548;
	private final static int ELEMENTAL_THREE = 5549;
	private final static int ELEMENTAL_FOUR = 5550;
	private final static int ELEMENTAL_FIVE = 5551;
	private final static int APPRENTICE = 5532;
	
	//Walking Tiles/Area's
	RSArea SAFE_WALK_ONE = new RSArea(new RSTile(2907, 5484), new RSTile(2907, 5485));
	RSArea SAFE_WALK_TWO = new RSArea(new RSTile(2907, 5493), new RSTile(2907, 5494));
	RSArea SAFE_WALK_THREE = new RSArea(new RSTile(2910, 5489), new RSTile(2910, 5490));
	RSArea SAFE_WALK_FOUR = new RSArea(new RSTile(2915, 5485), new RSTile(2915, 5483));
	RSArea SAFE_WALK_FIVE = new RSArea(new RSTile(2923, 5486), new RSTile(2923, 5489));
	RSTile IN_FRONT_OF_GATE = new RSTile(2910, 5480);
	RSTile TILE_INSIDE_GATE = new RSTile(2910, 5481);
	RSTile FIRST_TILE = new RSTile(2908, 5482);
	RSTile SAFE_SPOT_ONE = new RSTile(2906, 5486);
	RSTile SAFE_SPOT_TWO = new RSTile(2906, 5492);
	RSTile SAFE_SPOT_THREE = new RSTile(2909, 5490);
	RSTile SAFE_SPOT_FOUR = new RSTile(2912, 5484);
	RSTile SAFE_SPOT_FIVE = new RSTile(2921, 5485);
	RSTile HERB_lOCATION = new RSTile(2923, 5484);
	RSTile BANK_TILE = new RSTile(3307, 3120);
	RSTile HOUSE_END_TILE = new RSTile(3320, 3139);
    RSTile[] HOUSE_TILE = {new RSTile(3305, 3126), new RSTile(3307, 3136), new RSTile(3315, 3143), new RSTile(3320, 3139)};
    RSTilePath TO_HOUSE;
	
	//Boolean's
	private boolean NpcIsntNull;
	private boolean IN_GARDEN;
	
	//Paint Variables
	private int XPGained;
	private int startXP;
	private int failedTrips = 0;
	private int trips = 0;
	private long startTime;
	private int herbsPicked;
	private double percentSuccess;
	
	public boolean onStart() {
		mouse.setSpeed(random(3, 7));
		TO_HOUSE = walking.newTilePath(HOUSE_TILE);
		startTime = System.currentTimeMillis();
		startXP = skills.getCurrentExp(skills.FARMING);
		
		return true;
	}
	
	@Override
	public int loop() {
		if(walking.getEnergy() <= 10){
			rest();
		}
		if(!inventory.isFull()){
			if(atBank()){
				walkToHouse();
			}
			if(atHouse()){
				teleport();
			}
			if(atGarden()){
				walkToGate();
			}
			if(atGate()){
				enterGate();
			}
			if(IN_GARDEN == true){
				walkToherbs();
			}
			if(atHerbs()){
				pickHerbs();
			}
		} else if(inventory.isFull()){
			if(atGarden()){
				exitGarden();
			}
			if(atHouse()){
				walkBank();
			}
			if(atBank()){
				useBank();
			}
		}
		return random(180, 400);
	}

	private void rest() {
		while(getMyPlayer().isMoving()){
			sleep(180, 400);
		}
		while(walking.getEnergy() < 99){
			walking.rest();
		}
		if(walking.getEnergy() >= 99){
			walking.walkTileOnScreen(getMyPlayer().getLocation());
		}
	}

	private void teleport() {
		RSNPC apprentice = npcs.getNearest(APPRENTICE);
		if(apprentice != null){
			if(apprentice.isOnScreen()){
				apprentice.interact("Teleport");
				sleep(1200, 2000);
			} else {
				walking.walkTileMM(apprentice.getLocation());
			}
		}
	}

	private void walkToHouse() {
		while(calc.distanceTo(HOUSE_END_TILE) > 3){
			TO_HOUSE.traverse();
		}
		
	}

	private void useBank() {
		
		RSObject chest = objects.getNearest(BANK_CHEST);
		if(chest != null && chest.isOnScreen()){
			chest.interact("Open");
			sleep(1200, 1500);
			if(bank.isOpen()){
				bank.depositAll();
				sleep(400, 600);
				bank.close();
			} else {
				chest.interact("Open");
				sleep(1500, 1800);
			}
		}
	}

	private boolean atBank() {
		if(objects.getNearest(BANK_CHEST) != null && objects.getNearest(BANK_CHEST).isOnScreen()){
		
			return true;
		}
		return false;
	}

	private void walkBank() {
		while(calc.distanceTo(BANK_TILE) > 3){
			walking.walkTileMM(BANK_TILE);
		
			sleep(1500, 2000);
		}
	}

	private boolean atHouse() {
		RSNPC apprentice = npcs.getNearest(APPRENTICE);
		if(apprentice != null && apprentice.isOnScreen()){

			return true;
		}
		return false;
	}
	

	private void exitGarden() {
		RSObject fountain = objects.getNearest(FOUNTAIN);
		if(fountain != null){
			if(fountain.isOnScreen()){
				fountain.interact("Drink-from");
				sleep(2200, 2600);
			} else {
				walking.walkTileMM(fountain.getLocation());
			}
		}
		
	}

	private void pickHerbs() {
		RSObject herbs = objects.getNearest(HERBS);
		if(herbs != null && herbs.isOnScreen()){
			herbs.doClick();
			
			sleep(2200, 2500);
			IN_GARDEN = false;
			
		}
	}

	private boolean atHerbs() {
		RSObject herbs = objects.getNearest(HERBS);
		if(herbs != null && herbs.isOnScreen() && calc.distanceTo(herbs) < 2){
			
			return true;
		}
		return false;
	}

	private void walkToherbs() {
		if(insideGate()){
			walkSpot1();
		}
		if(atSpot1()){
			walkSpot2();
		}
		else if(atSpot2()){
			walkSpot3();
		}
		else if(atSpot3()){
		walkSpot4();
		}
		else if(atSpot4()){
		walkSpot5();
		}
		else if(atSpot5()){
		walkSpot6();
		}
		else if(atSpot6()){
		walkSpot7();
		}
	}

	private boolean atSpot6() {
		if(calc.distanceBetween(getMyPlayer().getLocation(), SAFE_SPOT_FIVE) < 2){
			
			return true;
		}
		return false;
	}

	private boolean atSpot5() {
		if(calc.distanceBetween(getMyPlayer().getLocation(), SAFE_SPOT_FOUR) < 4){
			
			return true;
		}
		return false;
	}

	private boolean atSpot4() {
		if(calc.distanceBetween(getMyPlayer().getLocation(), SAFE_SPOT_THREE) < 2){
			
			return true;
		}
		return false;
	}

	private boolean atSpot3() {
		if(calc.distanceBetween(getMyPlayer().getLocation(), SAFE_SPOT_TWO) < 2){
			
			return true;
		}
		return false;
	}

	private boolean atSpot2() {
		if(calc.distanceBetween(SAFE_SPOT_ONE, getMyPlayer().getLocation()) < 2){
			
			return true;
		} else {
			sleep(500);
		}
		return false;
	}

	private boolean atSpot1() {
		if(calc.distanceBetween(getMyPlayer().getLocation(), FIRST_TILE) < 2){
			
			return true;
		} else {
			sleep(500);
		}
		return false;
	}

	private void walkSpot7() {
		RSNPC elemental = npcs.getNearest(ELEMENTAL_FIVE);
		if(SAFE_WALK_FIVE.contains(elemental.getLocation()) && Direction(elemental, 1)){
			walking.walkTileOnScreen(HERB_lOCATION);
			
		}
		
	}

	private void walkSpot6() {
		RSNPC elemental = npcs.getNearest(ELEMENTAL_FOUR);
		if(elemental != null){
			if(SAFE_WALK_FOUR.contains(elemental.getLocation())){
				walking.walkTileMM(SAFE_SPOT_FIVE, 1, 1);
				sleep(2800);
				walking.walkTileOnScreen(SAFE_SPOT_FIVE);
				
			}
		}
		
	}

	private void walkSpot5() {
		RSNPC elemental = npcs.getNearest(ELEMENTAL_THREE);
		if(elemental != null){
			if(SAFE_WALK_THREE.contains(elemental.getLocation()) && Direction(elemental, 1)){
				walking.walkTileMM(SAFE_SPOT_FOUR, 0);
			}
		}
	}

	private void walkSpot4() {
		RSNPC elemental = npcs.getNearest(ELEMENTAL_TWO);
		if(elemental != null){
			if(SAFE_WALK_TWO.contains(elemental.getLocation())&& Direction(elemental, 3)){
				walking.walkTileOnScreen(SAFE_SPOT_THREE);
				
			}
		}
		
	}

	private void walkSpot3() {
		RSNPC elemental = npcs.getNearest(ELEMENTAL_ONE);
		if(elemental != null){
			if(SAFE_WALK_ONE.contains(elemental.getLocation()) && Direction(elemental, 1)){
				walking.walkTileMM(SAFE_SPOT_TWO, 0);
				
			}
		}
	}

	private void walkSpot2() {
		RSNPC elemental = npcs.getNearest(ELEMENTAL_ONE);
		if(elemental != null){
			if(SAFE_WALK_ONE.contains(elemental.getLocation()) && Direction(elemental, 1)){
				walking.walkTileOnScreen(SAFE_SPOT_ONE);
				
			}
		}
	}

	private void walkSpot1() {
		walking.walkTileOnScreen(FIRST_TILE);
		sleep(2000);
		
	}

	private void walkToGate() {
		walking.walkTileMM(IN_FRONT_OF_GATE, 0);
		
		
	}

	

	private boolean atGarden() {
		RSObject fountain = objects.getNearest(FOUNTAIN);
		if(fountain != null && fountain.isOnScreen()){
				
				return true;
			}
		return false;
	}

	private boolean insideGate() {
		if(calc.distanceBetween(getMyPlayer().getLocation(), TILE_INSIDE_GATE) < 1){
			
			IN_GARDEN = true;
			return true;
		}
		return false;
	}

	private void enterGate() {
		RSObject gate = objects.getNearest(GATE);
		if(gate != null){
			if(gate.isOnScreen()){
				gate.interact("Open");
				sleep(1500, 1700);
				trips++;
			}
		}
		
	}

	private boolean atGate() {
		RSObject gate = objects.getNearest(GATE);
		if(calc.distanceTo(IN_FRONT_OF_GATE) < 3 && insideGate() == false && atSpot1() == false){
			
			return true;
		}
		return false;
	}

	public boolean Direction(RSNPC npc, int direction) {
		switch (direction) {
		case 1:
			if (getDirection(npc) == 1024)
				return true;
			break;
		case 2:
			if (getDirection(npc) == 512)
				return true;
			break;
		case 3:
			if (getDirection(npc) == 0)
				return true;
			break;
		case 4:
			if (getDirection(npc) == 1536)
				return true;
			break;
		}
		return false;
	}

	private int getDirection(RSNPC npc) {

		if (npc != null) {
			NpcIsntNull = true;
		} else {
			NpcIsntNull = false;
		}

		if (NpcIsntNull == true) {
			final int x1 = npc.getLocation().getX();
			final int y1 = npc.getLocation().getY();
			while (npc.getLocation().getX() == x1 && npc.getLocation().getY() == y1)
				sleep(100);
			final int x2 = npc.getLocation().getX();
			final int y2 = npc.getLocation().getY();
			if (x2 == x1 && y2 > y1) {// N
				return 1024;
			} else if (x2 > x1 && y2 == y1) {// E
				return 1536;
			} else if (x2 == x1 && y2 < y1) {// S
				return 0;
			} else if (x2 < x1 && y2 == y1) {// W
				return 512;
			} else {
				return -1;
			}
		} else {
			return -1;
		}
	}
	
    //START: Code generated using Enfilade's Easel
    private final Color color1 = new Color(0, 0, 0, 214);
    private final Color color2 = new Color(0, 255, 0, 214);
    private final Color color3 = new Color(0, 255, 51, 214);

    private final BasicStroke stroke1 = new BasicStroke(2);

    private final Font font1 = new Font("Arial", 0, 16);

    public void onRepaint(Graphics g1) {
    	
    	long milis = System.currentTimeMillis() - startTime;
    	long hours = milis / (1000 * 60 * 60);
    	milis -= hours * (1000 * 60 * 60);
    	long minutes = milis / (1000 * 60);
    	milis -= minutes * (1000 * 60);
    	long seconds = milis / (1000);
    	milis = seconds * 1000;
    	
    	XPGained = skills.getCurrentExp(skills.FARMING) - startXP;
    	
    	float xpsec = 0;
    	if (minutes > 0 || hours > 0 || seconds > 0 && XPGained > 0){
    		xpsec = ((float) XPGained /(float)(seconds + (minutes * 60) + (hours * 60 * 60)));
    	}
    	float xpmin = xpsec * 60;
    	float xphour = xpmin * 60;
    	
    	if(failedTrips > 0){
    		percentSuccess = (100 - (failedTrips / trips)); 
    	} else {
    		percentSuccess = 100.0;
    	}
    	herbsPicked = XPGained / 25;
    	
        Graphics2D g = (Graphics2D)g1;
        g.setColor(color1);
        g.fillRoundRect(3, 337, 518, 139, 16, 16);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRoundRect(3, 337, 518, 139, 16, 16);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("Crazy Summer Garden", 179, 357);
        g.drawString("Run Time: " + hours + ":" + minutes + ":" + seconds, 12, 400);
        g.drawString("Herbs Picked: " + herbsPicked, 291, 400);
        g.drawString("Farming XP Gained: " + XPGained, 12, 420);
        g.drawString("Farming Xp P/Hr: " + xphour, 291, 420);
        g.drawString("Failed Trips: " + failedTrips, 12, 440);
        g.drawString("% of Successful Trips: " + percentSuccess, 291, 440);
    }
    //END: Code generated using Enfilade's Easel

	@Override
	public void messageReceived(MessageEvent e) {
		e.getMessage().toString().toLowerCase();
		if(e.getMessage().contains("spotted")){ 
			failedTrips++;
		}
	}

}
