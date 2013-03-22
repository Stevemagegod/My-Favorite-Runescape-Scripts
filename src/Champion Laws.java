/* 

 *LICENSE* 
 <This is a java script for the Rarebot platform.>

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.
 
 *LICENSE* 

 We encourage every script writer to make use of the skeleton code provided.
 Look at this like suggested methods or recommended ways to write scripts. Don't let this hold back your creativity 
 in any way.
 
  Special Thanks
	
  - Champion0369
  - Medo
  - Craze
  - Ubuntu4life
  - Dew
  - ExoCode
  - Alex
 */

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.util.LinkedList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Bank;
import com.rarebot.script.methods.Camera;
import com.rarebot.script.methods.Methods;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.methods.Interfaces;
import com.rarebot.script.util.Filter;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSTilePath;
import com.rarebot.script.wrappers.RSWeb;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSItem;



@ScriptManifest(authors = { "Champion0369 " }, name = "Champion Laws", keywords = { "Champion" }, version = 1.02, description = "Law running script. Special Thanks to the people of fuser scripts", website = "Reserved", requiresVersion = 257)
public class ChampionLaws extends Script implements PaintListener,
MessageListener, MouseListener {

   
    public long experienceGained, currentExperience, startExperience,
    startLevel, experiencePerHour, startTime = 0;
    
	public MousePaint mousePaint = new MousePaint();
	
    public Image cursorUnclicked, cursorClicked, userLogo,
    paintBackGround, closeButton, openButton, Avatar;
	
    public String paintType = "null", Status = "Setting up";

    private antiBanThread antibanthread;
	
    private garbageCollector garbagecollector;
	
	ChampionGUI g = new ChampionGUI();
	
	public boolean pauseThread = true;
	
	boolean npccontact = false;
	boolean spell = false;
	boolean guild = false;
	boolean norepair = false;
	
	boolean usinggiant = false;
	boolean usinglarge = false;
	boolean usingmedium = false;
	boolean usingsmall = false;
	
	boolean fullgiant = false;
	boolean fulllarge = false;
	boolean fullmedium = false;
	boolean fullsmall = false;
	
	boolean repairgiant = false;
	boolean repairlarge = false;
	boolean repairmedium = false;
	
	boolean usingpouches = false;
	boolean havering;
	
	int[] mouseX;
	int[] mouseY;
    
    String repair;
    int runescrafted = 0;
    int[] undepositeditems = {314, 307, 309, 305, 311, 303, 13431, 301, 15263, 313};
    public boolean guiIsOpen = true;
	int bankerID;	
	long time = 0;
	int runsmade = 0;
	 
    RSTile[] tile;
    RSTilePath path;
    
    public String scriptName = "Champion Laws"; 
    public String author = "Champion"; 
    private static SystemTray tray = SystemTray.getSystemTray(); 
    private static TrayIcon icon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("http://i.imgur.com/8shYp.png")); 
     
     
    /**
     * Try to break up the script in as many actions as possible.
     */
    private enum Action {
      BANK, WALKNPC, FLY, WALKRUINS, GOINSIDE, WALKTOALTER, CRAFTRUNES, TELEPORT, UNKNOWN, SLEEP;
    }


    /**
     * Constants here.
     */

  
    public interface Constants {
      
		int essenceID = 7936;
		int logID = 1511;
		int pouchID = 12810;
		int obeliskID = 29954;
		int chestID = 4483;
		int ringID = 2552;
		int ticketID = 24154;
		int ruinsID = 2459;
		int alterID = 2485;
		
		int[] cwdoorID = {36315, 36317};
		
		
		
		int cosmicRuneID = 564;
		int lawRuneID = 563;
		int astralRuneID = 9075;
		int airRuneID = 556;
		
		int smallPouchID = 5509;
		int mediumPouchID = 5510;
		int largePouchID = 5512;
		int giantPouchID = 5514;
		
		int mediumDegradedPouchID = 5511;
		int largeDegradedPouchID = 5513;
		int giantDegradedPouchID = 5515;
		
		int npcID = 5063;
		int ballonID = 19155;
		
	  
		int[] STUPID_INTERFACES = {670, 206, 207, 17};
	  
		int[] ring = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566},
					pouches = {5509, 5510, 5512, 5514},
					pouchesDegraded = {5511, 5513, 5515},
					allPouches = {5509, 5510, 5511, 5512, 5513, 5514, 5515};
	  
        RSArea clanwars = new RSArea(new RSTile(2437, 3080), new RSTile(2449, 3100));
        RSArea npcarea = new RSArea(new RSTile(2456, 3105), new RSTile(2467, 3115));
        RSArea flyarea = new RSArea(new RSTile(2805, 3352), new RSTile(2812, 3358));
        RSArea ruinsarea = new RSArea(new RSTile(2853, 3372), new RSTile(2863, 3387));
		RSArea alterarea = new RSArea(new RSTile(2443, 4810), new RSTile(2485, 4853));
		
		RSTile[] tonpc = {new RSTile(2457,3090), new RSTile(2460, 3099), new RSTile(2465, 3107)};
        RSTile[] toruins = {new RSTile(2816, 3347), new RSTile(2827, 3344), new RSTile(2838, 3348), new RSTile(2850, 3348), new RSTile(2857, 3359), new RSTile(2857, 3370), new RSTile(2857, 3378)};
		
		RSTile chest = new RSTile(2444, 3083);
		RSTile alter = new RSTile(2464, 4830);
		RSTile cwdoor = new RSTile(2445, 3089);
		RSTile ballon = new RSTile(2464,3108);
    }
  
  
    /**
     * Try to make as much logic as possible happen here instead of in the loop.
     * 
     * @return Action
     */
    public Action getAction() {
        try {
            if (game.getClientState() != 11) {
                return Action.SLEEP;
            } else {
                if(atclanwars() && needsbank()){
                  return Action.BANK;
                } else if(atclanwars() && !needsbank()){
					log("trying to walk");
                  return Action.WALKNPC;
                }else if(atnpcarea()){
                  return Action.FLY;
                } else if(atflyarea()){
                  return Action.WALKRUINS;
                } else if(atruinsarea()){
                  return Action.GOINSIDE;
                } else if(atalterarea() && !altervisable()){
                  return Action.WALKTOALTER;
                } else if(altervisable() && !needsbank()){
                  return Action.CRAFTRUNES;
                } else if(atalterarea() && needsbank()){
                  return Action.TELEPORT;
                }
             
            }
        } catch (final Exception e) {
            return Action.TELEPORT;
        }
        return Action.TELEPORT;
    }
    
	@Override
    public int loop() {
        try {
            mouse.setSpeed(random(6, 8));
            antiBan();
			checkinterfaces();
			
            switch (getAction()) {
            case BANK:
				Status = "BANKING";
				takespins();
				if(bankvisable()){
					while(needsrepair() && isrepairing()){
						repair();
					}
					bank();
				}else{
					tobank();
				}
				break;
              
            case WALKNPC:
				Status = "WALKING TO BALLON";
				tonpc();
				break;
              
            case FLY:
				Status = "FLYING";
				fly();
				break;
          
            case WALKRUINS:
				Status = "WALKING TO RUINS";
				toruins();
				break;
              
            case GOINSIDE:
				Status = "Going inside";
				if(ruinsvisable()){
					clickruins();
				}else{
					checkinterfaces();
					camera.moveRandomly(random(100,500));
				}
				
				break;
              
            case WALKTOALTER:
				Status = "Walking to Alter";
				toalter();
				break;
              
            case CRAFTRUNES:
				Status = "Crafting Runes";
				while(hasessence()){
					clickalter();
					if(!pouchsempty() && !hasessence()){
						emptypouchs();
					}
				}
				break;
              
            case TELEPORT:
				Status = "TELEPORTING";
				teleport();
				break;            
            }
        } catch (final Exception e) {
            return 25;
        }
        return Methods.random(180, 420);
    }
	
	private void takespins(){
		if(inventory.getCount(Constants.ticketID) > 0){
			log("Claimed Ticket");
			inventory.getItem(Constants.ticketID).doAction("Claim");
		}
	}
	
	private void checkinterfaces(){
		if(!game.isLoggedIn()){
			onFinish();
		}
		while(interfaces.canContinue()){
			interfaces.clickContinue();
			sleep(random(1000,1500));
		}
		for (int i = 0; i < Constants.STUPID_INTERFACES.length; i++) {
			if (interfaces.get(Constants.STUPID_INTERFACES[i]).isValid()) {
				if (i < 1) { //EQUIPMENT opened
					mouse.move(486, 22);
				} else if (i < 3) { // PRICE CHECKER opened
					mouse.move(486, 35);
				} else { //ITEMS ON DEATH opened
					mouse.move(492, 22);
				}
				sleep(random(500, 1000));
				if (menu.contains("Close")) {
					mouse.click(true);
				}
			}
		}
	}
	
    private boolean atclanwars(){
      if(Constants.clanwars.contains(getMyPlayer().getLocation())){
        log("At Clanwars");
        return true;
      }
      return false;
    }
    
    private boolean atnpcarea() {
      if(Constants.npcarea.contains(getMyPlayer().getLocation())){
        log("At Npc");
        return true;
      }
      return false;
    }
    
    private boolean atflyarea(){
      if(Constants.flyarea.contains(getMyPlayer().getLocation())){
        log("At Fly Area");
        return true;
      }
      return false;
    }
    
    private boolean atruinsarea(){
      if(Constants.ruinsarea.contains(getMyPlayer().getLocation())){
        log("At Ruins");
        return true;
      }
      return false;
    }

    private boolean atalterarea() {
		if(Constants.alterarea.contains(getMyPlayer().getLocation())){
			log("At Alter");
			return true;
		}
		return false;
    }
    
	private boolean bankvisable(){
		RSObject chest = objects.getNearest(Constants.chestID);
		if(chest != null && chest.isOnScreen()){
			log("Bank is Visable");
			return true;
		}
		return false;
	}
	
	private void tonpc(){
		log("Starting Walk to Ballon");
		checkinterfaces();
		walking.walkTileMM(Constants.cwdoor, 1, 1);
		while(calc.distanceTo(walking.getDestination()) > 1){
			checkinterfaces();
			sleep(200);
		}
		while(checkdoors()){
			checkinterfaces();
			sleep(random(400,800));
		}
		path = walking.newTilePath(Constants.tonpc);
		path.randomize(1,1);
		while(!atnpcarea()){
			checkinterfaces();
			path.traverse();
			log("Walking to next tile");
			while(calc.distanceTo(walking.getDestination()) > 3){
				checkinterfaces();
				sleep(200);
			}
		}
	}
	
	private boolean checkdoors(){
		RSObject door = objects.getNearest(Constants.cwdoorID[1]);
		if(door != null && door.isOnScreen()){
			log("Door Closed");
			Point p = door.getPoint();
			mouse.move(p);
			mouse.click(true);
			return true;
		}
		
		log("Door Open");
		return false;
	}
	
    private void toruins(){
		log("Starting Walk to Ruins");
    	path = walking.newTilePath(Constants.toruins);
		path.randomize(1,1);
		while(!atruinsarea()){
			checkinterfaces();
			path.traverse();
			log("Walking Next Tile");
			while(calc.distanceTo(walking.getDestination()) > 3){
				checkinterfaces();
				sleep(200);
			}
		}
		while(players.getMyPlayer().isMoving()){
			checkinterfaces();
			sleep(100);
		}
	}
  
	private void tobank(){
		while(!bankvisable()){
			log("Walking to Bank");
			checkinterfaces();
			walking.walkTileMM(Constants.chest);
			sleep(random(1000,2000));
		}
	}
	
	private void toalter(){
		log("Walking to Alter");
		while(!altervisable()){
			checkinterfaces();
			walking.walkTileMM(Constants.alter);
			sleep(random(3000,5000));
		}
	}
  
	private boolean ruinsvisable(){
		RSObject ruins = objects.getNearest(Constants.ruinsID);
		if(ruins != null && ruins.isOnScreen()){
			log("Ruins Visable");
			return true;
		}
		return false;
	}
	
	private boolean altervisable(){
		RSObject alter = objects.getNearest(Constants.alterID);
		if(alter != null && alter.isOnScreen()){
			log("Alter Visable");
			return true;
		}
		return false;
	}
  
	private void clickruins(){
		log("Clicking Ruins");
		RSObject ruins = objects.getNearest(Constants.ruinsID);
		Point p = ruins.getPoint();
        mouse.move(p);
		mouse.click(true);
		sleep(random(1500,2000));
	}
  
	private void clickalter(){
		log("Clicking Alter");
		RSObject alter = objects.getNearest(Constants.alterID);
		Point p = alter.getPoint();
        mouse.move(p);
		mouse.click(true);
		sleep(random(2000,2500));
	}
  
	private void clickballon(){
		log("Clicking Ballon");
		camera.setPitch(100);
		while(players.getMyPlayer().isMoving()){
			checkinterfaces();
			sleep(100);
		}
		checkinterfaces();
		RSObject ballon = objects.getNearest(Constants.ballonID);
		Point p = ballon.getPoint();
		mouse.move(p);
		if(menu.contains("Fly")){
			mouse.click(true);
			sleep(random(1500,2500));
		}
		else{
			mouse.move(p, -25, -25);
		}
		if(menu.contains("Fly")){
			mouse.click(true);
			sleep(random(1500,2500));
		}
		else{
			mouse.move(p, 25, 25);
		}
		if(menu.contains("Fly")){
			mouse.click(true);
			sleep(random(1500,2500));
		}
	}
  
	private boolean isrepairing(){
		if(npccontact){
			return true;
		}
		if(spell){
			return true;
		}
		if(guild){
			return true;
		}
		if(norepair){
			return false;
		}
		if(!usingpouches){
			return false;
		}
		return false;
	}
	
	private void checkrepair(){
		if(inventory.getCount(Constants.mediumDegradedPouchID) > 0){
			repairmedium = true;
		}
		else{
			repairmedium = false;
		}
		if(inventory.getCount(Constants.largeDegradedPouchID) > 0){
			repairlarge = true;
		}
		else{
			repairlarge = false;
		}
		if(inventory.getCount(Constants.giantDegradedPouchID) > 0){
			repairgiant = true;
		}
		else{
			repairgiant = false;
		}
	}
	
	private boolean needsrepair(){
		if(repairmedium){
			return true;
		}
		if(repairlarge){
			return true;
		}
		if(repairgiant){
			return true;
		}
		return false;
	}
	
	private void repair(){
		if(npccontact){
			npccontact();
		}
		if(spell){
			spell();
		}
		if(guild){
			guild();
		}
	}
	
	private void npccontact(){
		while(!haverunes()){
			checkinterfaces();
			getrunes();
			if(!haverunes()){
				checkinterfaces();
			}
		}
		while(needsrepair() && haverunes()){
			checkinterfaces();
			openSpells();
			mouse.move(696, 228);
			sleep(random(500, 1000));
			if (menu.contains("Cast NPC Contact")) {
				mouse.click(true);
				sleep(random(1000, 1500));
				mouse.move(487, 88);
				sleep(random(500, 1000));
				mouse.drag(487, 287);
				sleep(random(500, 1000));
				mouse.move(414, 127);
				sleep(random(500, 1000));
				if (menu.contains("Speak-to")) {
					mouse.click(true);
					log("waiting for chat");
					sleep(random(7000, 9000));
					checkinterfaces();
					log("loop 1 done");
					sleep(random(100, 200));
					for (int i = 0; i <= 3; i++) {
						log("clicking repair option");
						mouse.click(85, 415, 300, 12, true);
						sleep(random(100, 200));
					}
					log("loop 2 done");
					checkinterfaces();
					log("completely done");
				}
			}
			checkrepair();
		}
		
	}
	
	private void spell(){
		int pouchtorepair = 0;
		
		while(!haverunes()){
			getrunes();
			if(!haverunes()){
				checkinterfaces();
			}
		}
		
		while(needsrepair()){
			log("Attempting Spell");
			checkinterfaces();
			openSpells();
			if(repairmedium){
				pouchtorepair = Constants.mediumDegradedPouchID;
			}
			else if(repairlarge){
				pouchtorepair = Constants.largeDegradedPouchID;
			}
			else if(repairgiant){
				pouchtorepair = Constants.giantDegradedPouchID;
			}
			mouse.move(random(600, 608), random(311, 320));
			sleep(random(500, 1000));
			if (menu.contains("Cast Repair")) {
				mouse.click(true);
				sleep(random(500, 1000));
			}
			inventory.getItem(pouchtorepair).doAction("Cast");
			mouse.click(true);
			sleep(random(5000, 7500));
			checkrepair();
		}
	}
	
	private void guild(){
	
	}
	
	private void getrunes(){
		log("Getting Runes for Spell");
		boolean astral = false;
		boolean cosmic = false;
		boolean law = false;
		boolean air = false;
		
		if(inventory.getCount(Constants.astralRuneID) > 0){
			astral = true;
		}
		if(inventory.getCount(Constants.cosmicRuneID) > 0){
			cosmic = true;
		}
		if(inventory.getCount(Constants.lawRuneID) > 0){
			law = true;
		}
		if(inventory.getCount(Constants.airRuneID) > 0){
			air = true;
		}
		
		openbank();
		depositrunes();
		if(!astral){
			bankWithdraw(Constants.astralRuneID, 1, 1, "Withdrawing astral runes", 5000, "Out of astral runes! Stopping script...");
			if(spell){
				bankWithdraw(Constants.astralRuneID, 2, 1, "Withdrawing astral runes", 5000, "Out of astral runes! Stopping script...");
			}	
		}
		if(!cosmic){
		bankWithdraw(Constants.cosmicRuneID, 1, 1, "Withdrawing cosmic runes", 5000, "Out of cosmic runes! Stopping script...");
		}
		if(spell){
			if(!law){
				bankWithdraw(Constants.lawRuneID, 1, 1, "Withdrawing law runes", 5000, "Out of law runes! Stopping script...");
			}
		}
		if(npccontact){
			if(!air){
				bankWithdraw(Constants.airRuneID, 1, 1, "Withdrawing air runes", 5000, "Out of air runes! Stopping script...");
				bankWithdraw(Constants.airRuneID, 2, 1, "Withdrawing air runes", 5000, "Out of air runes! Stopping script...");
			}
		}
		closebank();
		
	}
	
	private boolean haverunes(){
		boolean astral = false;
		boolean cosmic = false;
		boolean law = false;
		boolean air = false;
		
		if(inventory.getCount(Constants.astralRuneID) > 0){
			astral = true;
		}
		if(inventory.getCount(Constants.cosmicRuneID) > 0){
			cosmic = true;
		}
		if(inventory.getCount(Constants.lawRuneID) > 0){
			law = true;
		}
		if(inventory.getCount(true, Constants.airRuneID) > 1){
			air = true;
		}
		
		if(npccontact){
			if(astral && cosmic && air){
				return true;
			}
		}
		if(spell){
			if(astral && cosmic && law){
				return true;
			}
		}
		return false;	
	}
	
	private void openSpells() {
		log("Open Spellbook");
		mouse.move(random(738, 755), random(174, 196));
		sleep(random(100, 200));
		if (menu.contains("Magic Spellbook")) {
			mouse.click(true);
			sleep(random(100, 200));
		}
	}
	
	private void mouseToItem(int index) {
		mouse.move(mouseX[index] + random(0, 14) - 7, mouseY[index] + random(0, 14) - 7);
	}
	
	private void openbank(){
		while(!bank.isOpen()){
			log("Opening Bank");
			checkinterfaces();
			RSObject chest = objects.getNearest(Constants.chestID);
			Point p = chest.getPoint();
			mouse.move(p);
			mouse.click(true);
			sleep(random(1000,1500));
		}
	}
	
	private void closebank() {
		while (bank.isOpen()) {
			log("Closing Bank");
			checkinterfaces();
			bank.close();
			long t = System.currentTimeMillis();
			if(bank.isOpen() && System.currentTimeMillis() - t < 2000) {
				checkinterfaces();
				sleep(random(100, 200));
			}
			sleep(random(400,700));
		}
	}
	
	private int getIndex(int itemID) {
		RSItem[] items = inventory.getItems();
		for (int i = 0; i <= 27; i++) {
			if (i + 1 <= items.length) {
				RSItem item = items[i];
				if (item != null) {
					if (item.getID() == itemID) {
						return i;
					}
				}
			}
		}
		return -1;
	}

	private int getIndex(int[] itemID) {
		RSItem[] items = inventory.getItems();
		for (int i = 0; i <= 27; i++) {
			if (i + 1 <= items.length) {
				RSItem item = items[i];
				if (item != null) {
					for (int j = 0; j <= itemID.length - 1; j++) {
						if (item.getID() == itemID[j]) {
							return i;
						}
					}
				}
			}
		}
		return -1;
	}
	
	private boolean bankWithdraw(int itemID, int itemCount, int withdrawCount, String status, int timeout, String logoutLog) {
		while (inventory.getCount(true, itemID) < itemCount && bank.isOpen() && !inventory.isFull()) {
			Status = status;
			if (bank.getCount(itemID) > 0) {
				bank.withdraw(itemID, withdrawCount);
				long t = System.currentTimeMillis();
				while (((inventory.getCount(true, itemID) < itemCount && itemCount < 12345) || (itemCount == 12345 && !inventory.isFull()))
						&& System.currentTimeMillis() - t < timeout && !inventory.isFull()) {
					sleep(random(90, 110));
				}
			} else {
				log(logoutLog);
				onFinish();
				break;
			}
		}
		if (inventory.getCount(itemID) < itemCount) {
			return false;
		}
		return true;
	}
	
	private void bank(){
		openbank();
		
		depositrunes();
		
		if(!havering){
			withdrawring();
		}
		
		getlog();
		
		fillpouchs();
		sleep(random(1500,2000));

		while(!inventory.isFull()){
			withdrawessance();
			checkinterfaces();
		}
		
		closebank();
		
	}
	
	private void depositrunes(){
		log("Making Deposit");
		if (!usingpouches) {
			bank.depositAll();
		} 
		else {
			try {
				do{
					if(inventory.getCount(Constants.lawRuneID) > 0){
						runsmade++;
					}
					runescrafted += inventory.getCount(true, Constants.lawRuneID);
					bank.depositAllExcept(Constants.allPouches);
					sleep(random(1000,1500));
				}while(inventory.getCount(Constants.lawRuneID) > 0);
			} 
			catch (final NullPointerException ignored) {
			}
		}
	}
	
	private void getlog(){
		while(!haslog()){
			log("Getting Log");
			bankWithdraw(Constants.logID, 1, 1, "Withdrawing log", 5000, "Out of logs! Stopping script...");
			sleep(random(500,1500));
			checkinterfaces();
		}
	}
	
	private boolean haslog(){
		if(inventory.getCount(Constants.logID) > 0){
			log("Have a Log");
			return true;
		}
		return false;
	}
	
	private void withdrawessance(){
		while(!inventory.isFull()){
			log("Getting Essance");
			bankWithdraw(Constants.essenceID, 12345, 0, "Withdrawing essence", 3000, "Out of pure essence! Stopping script...");
			sleep(random(500,1500));
			checkinterfaces();
		}
	}
	
	private void withdrawring(){
		while(inventory.getCount(Constants.ringID) <= 0){
			log("Getting Ring");
			bankWithdraw(Constants.ringID, 1, 1, "Withdrawing Ring", 3000, "Out of Rings! Stopping script...");
			sleep(random(500,1500));
			checkinterfaces();
		}
		while(inventory.getCount(Constants.ringID) > 0){
			log("Putting on Ring");
			inventory.getItem(Constants.ringID).interact("Wear");
			sleep(random(500,1500));
			checkinterfaces();
		}
		havering = true;
	}
	
	private void fillpouchs(){
		log("Filling Pouches");
		while(!pouchsfilled()){
			checkinterfaces();
			if(usinggiant){
				if(inventory.getCount(Constants.essenceID) > 11 && !fullgiant){
					log("Trying to Fill Giant");
					if(inventory.getCount(Constants.giantPouchID) > 0){
						if(inventory.getItem(Constants.giantPouchID).interact("Fill")){
							log("Filled Giant");
							fullgiant = true;
							sleep(random(1500,2000));
						}
					}
					if(inventory.getCount(Constants.giantDegradedPouchID) > 0){
						if(inventory.getItem(Constants.giantDegradedPouchID).interact("Fill")){
							log("Filled Degraded Giant");
							fullgiant = true;
							sleep(random(1500,2000));
						}
					}
				}
				else if(inventory.getCount(Constants.essenceID) < 12){
					withdrawessance();
				}
			}
			if(usinglarge){
				if(inventory.getCount(Constants.essenceID) > 8 && !fulllarge){
					log("Trying to Fill Large");
					if(inventory.getCount(Constants.largePouchID) > 0){
						if(inventory.getItem(Constants.largePouchID).interact("Fill")){
							log("Filled Large");
							fulllarge = true;
							sleep(random(1500,2000));
						}
					}
					if(inventory.getCount(Constants.largeDegradedPouchID) > 0){
						if(inventory.getItem(Constants.largeDegradedPouchID).interact("Fill")){
							log("Filled Degraded Large");
							fulllarge = true;
							sleep(random(1500,2000));
						}
					}
				}
				else if(inventory.getCount(Constants.essenceID) < 9){
					withdrawessance();
				}
			}
			if(usingmedium){
				if(inventory.getCount(Constants.essenceID) > 5 && !fullmedium){
					log("Trying to Fill Medium");
					if(inventory.getCount(Constants.mediumPouchID) > 0){
						if(inventory.getItem(Constants.mediumPouchID).interact("Fill")){
							log("Filled Medium");
							fullmedium = true;
							sleep(random(1500,2000));
						}
					}
					if(inventory.getCount(Constants.mediumDegradedPouchID) > 0){
						if(inventory.getItem(Constants.mediumDegradedPouchID).interact("Fill")){
							log("Filled Degraded Medium");
							fullmedium = true;
							sleep(random(1500,2000));
						}
					}
				}
				else if(inventory.getCount(Constants.essenceID) < 6){
					withdrawessance();
				}
			}
			if(usingsmall){
				if(inventory.getCount(Constants.essenceID) > 2 && !fullsmall){
					log("Trying to Fill Small");
					if(inventory.getItem(Constants.smallPouchID).interact("Fill")){
						log("Filled Small");
						fullsmall = true;
						sleep(random(1500,2000));
					}
				}
				else if(inventory.getCount(Constants.essenceID) < 3){
					withdrawessance();
				}
			}
		}
	}
	
	private void fillpouch(int pID){
		if (inventory.contains(pID)) {
			int pouchIndex = getIndex(pID);
			int generatedX = mouseX[pouchIndex] + random(0, 14) - 7;
			int generatedY = mouseY[pouchIndex] + random(0, 14) - 7;
			int generatedX2 = generatedX - 25 + random(0, 50);
			int generatedY2 = generatedY + random(40, 50);
			mouse.move(generatedX, generatedY);
			sleep(250, 500);
			if (menu.contains("pouch")) {
				mouse.click(false);
				sleep(250, 500);
				mouse.move(generatedX2, generatedY2);
				sleep(250, 500);
				mouse.click(true);
			}
		}
	}
	
	private void emptypouchs(){
		boolean havespace = true;
		while(!pouchsempty() && havespace){
			log("Empting Pouches");
			checkinterfaces();
			if(usinggiant){
				if(fullgiant){
					if(inventory.getCount(false) < 17 && fullgiant){
						log("Trying to Empty Giant");
						if(inventory.getCount(Constants.giantPouchID) > 0){
							if(inventory.getItem(Constants.giantPouchID).doAction("Empty")){
								log("Emptied Giant");
								fullgiant = false;
								sleep(random(1500,2000));;
							}
						}
						if(inventory.getCount(Constants.giantDegradedPouchID) > 0){
							if(inventory.getItem(Constants.giantDegradedPouchID).doAction("Empty")){
								log("Emptied Giant Degraded");
								fullgiant = false;
								sleep(random(1500,2000));;
							}
						}
					}
					else{
						log("No Space");
						havespace = false;
					}
				}
			}
			if(usinglarge){
				if(fulllarge){
					if(inventory.getCount(false) < 20 && fulllarge){
						log("Trying to Empty Large");
						if(inventory.getCount(Constants.largePouchID) > 0){
							if(inventory.getItem(Constants.largePouchID).doAction("Empty")){
								log("Emptied Large");
								fulllarge = false;
								sleep(random(1500,2000));;
							}
						}
						if(inventory.getCount(Constants.largeDegradedPouchID) > 0){
							if(inventory.getItem(Constants.largeDegradedPouchID).doAction("Empty")){
								log("Emptied Large Degraded");
								fulllarge = false;
								sleep(random(1500,2000));;
							}
						}
					}
					else{
						log("No Space");
						havespace = false;
					}
				}
			}			
			if(usingmedium){
				if(fullmedium){
					if(inventory.getCount(false) < 23 && fullmedium){
						log("Trying to Empty Medium");
						if(inventory.getCount(Constants.mediumPouchID) > 0){
							if(inventory.getItem(Constants.mediumPouchID).doAction("Empty")){
								log("Emptied Medium");
								fullmedium = false;
								sleep(random(1500,2000));;
							}
						}
						if(inventory.getCount(Constants.mediumDegradedPouchID) > 0){
							if(inventory.getItem(Constants.mediumDegradedPouchID).doAction("Empty")){
								log("Emptied Medium Degraded");
								fullmedium = false;
								sleep(random(1500,2000));;
							}
						}
					}
					else{
						log("No Space");
						havespace = false;
					}
				}
			}			
			if(usingsmall){
				if(fullsmall){
					if(inventory.getCount(false) < 26 && fullsmall){
						log("Trying to Empty Small");
						if(inventory.getItem(Constants.smallPouchID).doAction("Empty")){
							log("Emptied Small");
							fullsmall = false;
							sleep(random(1500,2000));
						}
					}
					else{
						log("No Space");
						havespace = false;
					}
				}
			}
		}
	}
	
	private void emptypouch(int pID){
		if(inventory.contains(pID)){
			int pouchIndex = getIndex(pID);
			int generatedX = mouseX[pouchIndex] + random(0, 14) - 7;
			int generatedY = mouseY[pouchIndex] + random(0, 14) - 7;
			int generatedX2 = generatedX - 25 + random(0, 50);
			int generatedY2 = generatedY + random(40, 50);
			mouse.move(generatedX, generatedY);
			sleep(250, 500);
			if (menu.contains("pouch")) {
				mouse.click(true);
			}
		}
	}

	private boolean pouchsfilled(){
		if(usinggiant){
			if(fullgiant == false){
				log("Giant Not Full");
				return false;
			}
		}
		if(usinglarge){
			if(fulllarge == false){
				log("Large Not Full");
				return false;
			}
		}
		if(usingmedium){
			if(fullmedium == false){
				log("Medium Not Full");
				return false;
			}
		}
		if(usingsmall){
			if(fullsmall == false){
				log("Small Not Full");
				return false;
			}
		}
		log("All Pouches Full");
		return true;
	}
	
	private boolean pouchsempty(){
		if(usinggiant){
			if(fullgiant == true){
				return false;
			}
		}
		if(usinglarge){
			if(fulllarge == true){
				return false;
			}
		}
		if(usingmedium){
			if(fullmedium == true){
				return false;
			}
		}
		if(usingsmall){
			if(fullsmall == true){
				return false;
			}
		}
		log("All Pouchs Empty");
		return true;	
	}
	
	private void checkring(){
		if(equipment.getItem(equipment.RING).getID() > 0){
			log("Have a Ring");
			havering = true;
		}
		else{
			log("No Ring");
			havering = false;
		}
	}
	
	private void checkusingpouches(){
		if(inventory.getCount(Constants.giantPouchID) > 0 || inventory.getCount(Constants.giantDegradedPouchID) > 0){
			log("Using Giant");
			usinggiant = true;
			usingpouches = true;
		}
		if(inventory.getCount(Constants.largePouchID) > 0 || inventory.getCount(Constants.largeDegradedPouchID) > 0){
			log("Using Large");
			usinglarge = true;
			usingpouches = true;
		}
		if(inventory.getCount(Constants.mediumPouchID) > 0 || inventory.getCount(Constants.mediumDegradedPouchID) > 0){
			log("Using Medium");
			usingmedium = true;
			usingpouches = true;
		}
		if(inventory.getCount(Constants.smallPouchID) > 0){
			log("Using Small");
			usingsmall = true;
			usingpouches = true;
		}
		
	}
	
	private void fly(){
		log("trying to fly");
		if(!haslog()){
			log("Trying to Fly W/O log");
			teleport();
		}
		mouse.move(random(235,255), random(135,145));
		sleep(random(200,500));
		if(menu.contains("Entrana")){
			mouse.click(true);
			sleep(random(1000,1500));
			checkinterfaces();
			time = System.currentTimeMillis();
			while (!atflyarea() && System.currentTimeMillis() - time < 7500) {
				sleep(random(250, 500));
			}
		}
		else{
			clickballon();
		}
		checkinterfaces();
	}
  
	private boolean hasessence(){
		if(inventory.getCount(Constants.essenceID) > 0){
			log("Have No Essance");
			return true;
		}
		return false;
	}
  
	private boolean needsbank(){
		if(!hasessence() && pouchsempty()){
			log("All out of Essance");
			return true;
		}
		if(!havering){
			return true;
		}
		if(atclanwars() && !haslog()){
			return true;
		}
		return false;
	}
  
	private void teleport(){
		while(!atclanwars()){
			log("Trying to Teleport");
			checkinterfaces();
			checkring();
			if(!havering){
				log("Trying to Teleport W/O a Ring");
				onFinish();
			}
			mouse.move(random(690, 708), random(376, 396));
			sleep(random(200, 500));
			if (menu.contains("Remove Ring")) {
				mouse.click(false);
				sleep(random(200, 500));
				mouse.move(random(530, 740), random(428, 433));
				sleep(random(200, 500));
				mouse.click(true);
			}
			time = System.currentTimeMillis();
			while (!atclanwars() && System.currentTimeMillis() - time < 7500) {
				sleep(random(250, 500));
			}
			
		}
		sleep(random(1500,2000));
		checkring();
		checkrepair();
	}
	
  public class ChampionGUI extends JFrame {
    public ChampionGUI() {
      initComponents();
    }

    private void button1ActionPerformed(ActionEvent e) {
		String repair = comboBox1.getSelectedItem().toString();
      
		if(repair.equals("NPC Contact")){
			npccontact = true;
		}
		else if(repair.equals("Pouch Repair Spell")){
			spell = true;
		}
		else if(repair.equals("Runecrafting Guild")){
			guild = true;
		}
		else{
			norepair = true;
		}    
      
      setVisible(false);
      guiIsOpen = false;
    }

    private void initComponents() {
      // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
      // Generated using JFormDesigner Evaluation license - Alex Martin
      label1 = new JLabel();
      label2 = new JLabel();
      comboBox1 = new JComboBox();
      button1 = new JButton();


      //======== this ========
      Container contentPane = getContentPane();

      //---- label1 ----
      label1.setText("Champion Laws");
      label1.setFont(new Font("Tahoma", Font.PLAIN, 24));

      //---- label2 ----
      label2.setText("Repair: ");
      label2.setFont(new Font("Tahoma", Font.PLAIN, 18));

      //---- comboBox1 ----
      comboBox1.setModel(new DefaultComboBoxModel(new String[] {
        "NPC Contact",
        "Pouch Repair Spell",
        "Runecrafting Guild",
		"None"
        }));

      //---- button1 ----
      button1.setText("Start");
      button1.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
          button1ActionPerformed(e);
        }
      });

      GroupLayout contentPaneLayout = new GroupLayout(contentPane);
      contentPane.setLayout(contentPaneLayout);
      contentPaneLayout.setHorizontalGroup(
        contentPaneLayout.createParallelGroup()
          .addGroup(contentPaneLayout.createSequentialGroup()
            .addGroup(contentPaneLayout.createParallelGroup()
              .addGroup(contentPaneLayout.createSequentialGroup()
                .addGap(100, 100, 100)
                .addComponent(label1))
              .addGroup(contentPaneLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                  .addComponent(label2))
                .addGap(18, 18, 18)
                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                  .addComponent(comboBox1, 0, 121, Short.MAX_VALUE)))
              .addGroup(contentPaneLayout.createSequentialGroup()
                .addGap(141, 141, 141)
                .addComponent(button1, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE)))
            .addContainerGap(93, Short.MAX_VALUE))
      );
      contentPaneLayout.setVerticalGroup(
        contentPaneLayout.createParallelGroup()
          .addGroup(contentPaneLayout.createSequentialGroup()
            .addContainerGap()
            .addComponent(label1)
            .addGap(18, 18, 18)
            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
              .addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
              .addComponent(label2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGap(18, 18, 18)
            .addComponent(button1, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
            .addGap(39, 39, 39))
      );
      pack();
      setLocationRelativeTo(getOwner());
      // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    // Generated using JFormDesigner Evaluation license - Alex Martin
    private JLabel label1;
    private JLabel label2;
    private JComboBox comboBox1;
    private JButton button1;

    // JFormDesigner - End of variables declaration  //GEN-END:variables
  }
  @Override
    /**
     * onStart version 1.00
     * @Author Ubuntu4life
     */
    public boolean onStart() {
        if (!game.isLoggedIn()) {
            log.severe("Start this script logged in please.");
            return false;
        }
        if (!game.isFixed()) {
            log.severe("It's highly recommended to bot with fixed screen mode.");
            return false;
        }
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                  new ChampionGUI().setVisible(true);
                  
                }
            });
        } catch (InterruptedException ignored) {
        } catch (InvocationTargetException ignored) {
        }
        // Load files from external server here.
        new Thread(new Runnable() {
            public void run() {
                
            }
        }).start();
        while (guiIsOpen) {
            Methods.sleep(25);
            camera.setPitch(true);
        }
        log.info("Loading version "
                + getClass().getAnnotation(ScriptManifest.class).version());
        startTime = System.currentTimeMillis();
        startExperience = skills.getCurrentExp(Skills.RUNECRAFTING);
        antibanthread = new antiBanThread();
        antibanthread.start();
        garbagecollector = new garbageCollector();
        garbagecollector.start();
        
        if (SystemTray.isSupported()) { 
            try { 
            tray.add(icon); 
            } catch (AWTException e) { 
            e.printStackTrace(); 
            } 
       } else { 
            log("System Tray is not supported."); 
       }
	   
	   checkring();
	   checkusingpouches();
	   checkrepair();
	   
        return true;
    }

    @Override
    /**
     * onFinish version 1.00
     * @Author Ubuntu4life
     */
    public void onFinish() {
        for (int Tries = 0; antibanthread.isAlive(); Tries++) {
            if (Tries > 2400) {
                antibanthread.interrupt();
                Methods.sleep(25);
                if (antibanthread.isInterrupted()) {
                    break;
                } else if (Tries > 4800) {
                    break;
                }
            } else {
                antibanthread.stopThread = true;
                Methods.sleep(25);
                if (antibanthread.isInterrupted()) {
                    break;
                }
            }
        }
        env.saveScreenshot(true);
        for (int Tries = 0; garbagecollector.isAlive(); Tries++) {
            if (Tries > 2400) {
                garbagecollector.interrupt();
                Methods.sleep(25);
                if (garbagecollector.isInterrupted()) {
                    break;
                } else if (Tries > 4800) {
                    break;
                }
            } else {
                garbagecollector.stopThread = true;
                Methods.sleep(25);
                if (garbagecollector.isInterrupted()) {
                    break;
                }
            }
        }
        showMessage("Thanks for Using " + scriptName + " - By " + author); 
        sleep(5000); 
        tray.remove(icon); 
        stopScript();
    }

    @Override
    public void messageReceived(final MessageEvent serverTraffic) {
        final String serverMessage = serverTraffic.getMessage().toLowerCase();
        if (serverMessage.contains("advanced")) {
            log("Congratulations on level up !");
        }
        if (serverMessage.contains("the next runescape")) {
            log("There will be a system update soon, logged out.");
            stopScript(true);
        }
        if(serverMessage.contains("just advanced")){
          showMessage("Congratulations, you gained a RC level! Your RC level is now: " + skills.getCurrentLevel(Skills.RUNECRAFTING));
        }
    }

    /**
     * Looped antiBan version 1.00
     * 
     * @Author Ubuntu4life
     */
    private void antiBan() {
        // CHANGE randomInteger to tune the frequency of the antiBan.
        final int randomInteger = Methods.random(1, 11);
        switch (randomInteger) {
        case 1:
            // CHANGE this random integer generator to tune the relative
            // distribution of antiBan actions.
            if (Methods.random(1, 17) != 1) {
                break;
            } else {
                mouse.move(random(10, 750), random(10, 495));
                if (Methods.random(1, 7) == 1) {
                    mouse.move(random(10, 750), random(10, 495));
                }
            }
            break;
        case 2:
            if (Methods.random(1, 12) != 1) {
                break;
            } else {
                mouse.moveSlightly();
            }
            break;
        case 3:
            if (Methods.random(1, 18) != 1) {
                break;
            } else {
                if (Methods.random(1, 11) == 1) {
                    camera.setPitch(true);
                }
                int angle = camera.getAngle() + Methods.random(-45, 45);
                if (angle < 0) {
                    angle = Methods.random(0, 10);
                }
                if (angle > 359) {
                    angle = Methods.random(0, 10);
                }
                char Direction = 37;
                if (Methods.random(0, 100) < 50) {
                    Direction = 39;
                }
                keyboard.pressKey(Direction);
                Methods.sleep(Methods.random(100, 700));
                keyboard.releaseKey(Direction);
            }
            break;
        default:
            return;
        }
    }

    /**
     * Multi-threaded antiBan version 1.00
     * 
     * @Author Ubuntu4life
     */
    class antiBanThread extends Thread {
        private volatile boolean stopThread = false;
        Random javaUtilRandom = new Random();
		private int leftrighttime = 1500;
		private char direction = 'n';
		private long lastmove = System.currentTimeMillis();
		//pauseThread = true;

        @Override
         public void run() {
        try {
		while (!stopThread && !isPaused() && game.isLoggedIn()) {
			while(pauseThread){
				Thread.sleep(random(2000,5000));
			}
		int num = random(0,20);
        Thread.sleep(javaUtilRandom.nextInt(Math.abs(5000 - 1500)));
        switch(num)
        {
            case 0:
                //camera.moveRandomly(random(100,500));
				if(random(0,1) == 1 || System.currentTimeMillis() - lastmove > random(120,480) * 1000){
					camera.setPitch(100);
					lastmove = System.currentTimeMillis();
					Thread.sleep(random(300, 500));
				}
                break;
			case 1:
				if(random(0,1) == 1 || System.currentTimeMillis() - lastmove > random(120,480) * 1000){
					camera.setPitch(random(70,100));
					lastmove = System.currentTimeMillis();
					Thread.sleep(random(300,500));
				}
				break;
            case 2:
				if(pauseThread == false){
					mouse.moveOffScreen();
					Thread.sleep(random(300, 500));
				}
                break;
			case 3:
				if(random(0,1) == 1 || System.currentTimeMillis() - lastmove > random(120,480) * 1000){
					camera.setCompass(direction);
					leftrighttime = 1500;
					lastmove = System.currentTimeMillis();
					Thread.sleep(random(300, 500));
				}
			break;
            case 4:
                if(pauseThread == false){
					mouse.moveRandomly(200, 600);
					Thread.sleep(random(300, 500));
				}
                break;
            case 5:
                if(pauseThread == false){
					mouse.moveRandomly(random(100,500));
					Thread.sleep(random(300, 500));
				}
                break;
            case 6:
				if(pauseThread == false){	
					mouse.moveSlightly();
					Thread.sleep(random(300, 500));
				}
					break;
            case 7:
                if(pauseThread == false && random(0,3) == 3){
					skills.doHover(Skills.INTERFACE_RUNECRAFTING); //Change to whatever skill you need.
					Thread.sleep(random(3500,5700));
					inventory.isFull();    
					Thread.sleep(random(2100, 3200));
				}
                break;
            case 8:
                if(pauseThread == false){
					mouse.move(random(527, 200), random(744, 464));
					Thread.sleep(random(300, 500));
				}
                break;
            case 9:    //Push left or right arrow key for a "random" amount of time.
				if(random(0,1) == 1 || System.currentTimeMillis() - lastmove > random(120,480) * 1000){
                    final char[] leftRight = new char[] { KeyEvent.VK_LEFT,KeyEvent.VK_RIGHT };
                    final char[] upDown = new char[] { KeyEvent.VK_DOWN,KeyEvent.VK_UP };
                    final int random1 = javaUtilRandom.nextInt(Math.abs(1 - 0));
                    final int random2 = javaUtilRandom.nextInt(Math.abs(1 - 0));
					final int random3 = javaUtilRandom.nextInt(Math.abs(101 -502));
                    boolean didLeftRight = false;
                    keyboard.pressKey(upDown[random2]);
					if (random(0, 5) != 1) {
						if(random1 == 0 && leftrighttime - random3 > 0){
						leftrighttime -= random3;
						keyboard.pressKey(leftRight[random1]);
						Thread.sleep(random3);
						}
						else if(random1 == 1 && leftrighttime + random3 < 3000){
							leftrighttime += random3;
							keyboard.pressKey(leftRight[random1]);
							Thread.sleep(random3);
						}
						keyboard.releaseKey(leftRight[random1]);
                    }
					else{
						Thread.sleep(javaUtilRandom.nextInt(Math.abs(301 - 601)));
					}
                    keyboard.releaseKey(upDown[random2]);
					lastmove = System.currentTimeMillis();
					Thread.sleep(random(300, 500));
				}
				break;
        }
    }
  }

    catch (final Exception e) {
    }
  }

    
    }       
    

    /**
     * Multi-threaded Java garbage collector. Removes un-used objects loaded in
     * the RAM. version 1.00
     * 
     * @Author Ubuntu4life
     */
    class garbageCollector extends Thread {
        private volatile boolean stopThread = false;

        @Override
        public void run() {
            while (!stopThread && game.isLoggedIn()) {
                try {
                    if (Methods.random(0, 450) == 1) {
                        final Runtime runtime = Runtime.getRuntime();
                        runtime.gc();
                    }
                    Thread.sleep(Methods.random(1000, 1500));
                } catch (final InterruptedException e) {
                }
            }
        }
    }

   

    private final RenderingHints renderHints = new RenderingHints(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    private final Color color1 = new Color(0, 0, 0, 205);
    private final Color color2 = new Color(0, 255, 255);
    private final Color color3 = new Color(0, 255, 255, 230);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 20);
    private final Font font2 = new Font("Arial", 0, 14);


    @Override
    public void onRepaint(Graphics g) {
      
      long millis = System.currentTimeMillis() - startTime;
      long hours = millis / (1000 * 60 * 60);
      millis -= hours * (1000 * 60 * 60);
      long minutes = millis / (1000 * 60);
      millis -= minutes * (1000 * 60);
      long seconds = millis / 1000;
      
      experienceGained = skills.getCurrentExp(Skills.RUNECRAFTING) - startExperience;
      
      float xpsec = 0;
      if (minutes > 0 || hours > 0 || seconds > 0 && experienceGained > 0){
      xpsec = ((float) experienceGained /(float)(seconds + (minutes * 60) + (hours * 60 * 60)));
      }
      float xpmin = xpsec * 60;
      float xphour = xpmin * 60;
      
      float timeTNLhour = 0;
      timeTNLhour = (float) skills.getExpToNextLevel(Skills.RUNECRAFTING)/(float)xphour;

      float timeTNLmin = timeTNLhour*60;
      float timeTNLsec = timeTNLmin*60;

      float estimatedHour = timeTNLsec/3600;
      float tempminutes = timeTNLsec%3600;
      float estimatedMinutes = tempminutes/60;
      float estimatedSeconds = tempminutes%60;
      
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHints(renderHints);
        g.setColor(color1);
        g.fillRoundRect(6, 344, 509, 129, 16, 16);
        g.setColor(color2);
        ((Graphics2D) g).setStroke(stroke1);
        g.drawRoundRect(6, 344, 509, 129, 16, 16);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("Champion Laws", 175, 371);
        g.setFont(font2);
        g.drawString("Run Time: " + hours + ":" + minutes + ":" + seconds, 18, 391);
        g.drawString("Time To Level: " + (int) estimatedHour + ":" + (int) estimatedMinutes + " :" + (int) estimatedSeconds, 18, 417);
        g.drawString("Status: " + Status, 18, 442);
        g.drawString("Experience Gained: " + (int) experienceGained, 267, 391);
        g.drawString("Experience Per/Hr: " + (int) xphour, 267, 417);
        g.drawString("Runes Crafted: " + runescrafted, 267, 442);
        mousePaint.Draw(g2);
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        
    }

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

    /**
     * Official mouse paint. Version 1.00
     * 
     * @author ExoCode
     * 
     */
    public class MousePaint {

        public int waveSize = 0;

        @SuppressWarnings({ "serial" })
        public class mousePathPoint extends Point {

            private long finishTime;
            private double lastingTime;

            public mousePathPoint(int x, int y, int lastingTime) {
                super(x, y);
                this.lastingTime = lastingTime;
                finishTime = System.currentTimeMillis() + lastingTime;
            }

            public boolean isUp() {
                return System.currentTimeMillis() > finishTime;
            }
        }

        public double getRot(int ticks) {
            return (System.currentTimeMillis() % (360 * ticks)) / ticks;
        }

        public LinkedList<mousePathPoint> mousePath = new LinkedList<mousePathPoint>();

        public void drawTrail(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(1));
            while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
                mousePath.remove();
            }
            Point clientCursor = mouse.getLocation();
            mousePathPoint mpp = new mousePathPoint(clientCursor.x,
                    clientCursor.y, 250);
            if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
                mousePath.add(mpp);
            }
            mousePathPoint lastPoint = null;
            for (mousePathPoint a : mousePath) {
                if (lastPoint != null) {
                    long mpt = System.currentTimeMillis()
                            - mouse.getPressTime();
                    if (mouse.getPressTime() == -1 || mpt >= 250) {
                        g2.setColor(Color.GREEN);
                    }
                    if (mpt < 250) {
                        g2.setColor(Color.RED);
                    }
                    g2.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
                }
                lastPoint = a;
            }
        }

        public void drawMouse(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
            g2.setStroke(new BasicStroke(3));
            g2.setColor(Color.BLACK);
            g2.drawOval(mouse.getLocation().x - 13, mouse.getLocation().y - 13,
                    25, 25);
            g2.setStroke(new BasicStroke(1));
            g2.setColor(new Color(0, 0, 0, 114));
            g2.fillOval(mouse.getLocation().x - 13, mouse.getLocation().y - 13,
                    25, 25);
            Point MouseLocation = mouse.getLocation();
            long mpt = System.currentTimeMillis() - mouse.getPressTime();
            g2.rotate(Math.toRadians(getRot(5)), mouse.getLocation().x,
                    mouse.getLocation().y);
            if (mouse.getPressTime() == -1 || mpt >= 250) {
                g2.setColor(Color.GREEN);
                g2.drawLine(MouseLocation.x - 5, MouseLocation.y,
                        MouseLocation.x + 5, MouseLocation.y);
                g2.drawLine(MouseLocation.x, MouseLocation.y - 5,
                        MouseLocation.x, MouseLocation.y + 5);
            }
            if (mpt < 250) {
                g2.setColor(Color.RED);
                g2.drawLine(MouseLocation.x - 5, MouseLocation.y,
                        MouseLocation.x + 5, MouseLocation.y);
                g2.drawLine(MouseLocation.x, MouseLocation.y - 5,
                        MouseLocation.x, MouseLocation.y + 5);
            }
        }

        public void drawWave(Graphics g, Color color) {
            Graphics2D g2 = (Graphics2D) g;
            Point MouseLoc = mouse.getPressLocation();
            long mpt = System.currentTimeMillis() - mouse.getPressTime();
            g2.setRenderingHints(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
            g2.setColor(color);
            if (mpt < 1000) {
                waveSize = (int) (mpt / 7.5);
            } else {
                waveSize = 0;
            }
            g2.setStroke(new BasicStroke(3));
            g2.drawOval(MouseLoc.x - (waveSize / 2), MouseLoc.y
                    - (waveSize / 2), waveSize, waveSize);
            g2.drawOval(MouseLoc.x - ((waveSize / 2) / 2), MouseLoc.y
                    - ((waveSize / 2) / 2), waveSize / 2, waveSize / 2);
        }

        public void Draw(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHints(new RenderingHints(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));
            drawWave(g2, Color.BLACK);
            drawTrail(g2);
            drawMouse(g2);
        }
    }

    public interface Condition {
        public boolean isTrue();
    }
    
    public static void showMessage(Object text) { 
        icon.displayMessage("Champion Laws", text.toString(), TrayIcon.MessageType.INFO); 
        }

    /**
     * Advanced sleeping method with no walking involved.
     * 
     * @Author Ubuntu4life
     * 
     * @param condition
     *            The condition under which the script has to sleep.
     * @param Threshold
     *            The maximum amount of milliseconds to wait /while/ something
     *            is not going the way it's supposed to.
     * @return True when everything went fine.
     */
    private boolean sleepWhile(final Condition condition, final int Threshold) {
        final int Irritations = Threshold / 25;
        for (int i = 0; i < Irritations && condition.isTrue(); i++) {
            if (game.getClientState() != 11) {
                i = 0;
            }
            Methods.sleep(Methods.random(20, 30));
        }
        return condition.isTrue();
    }

    /**
     * Advanced sleeping method with NPC interaction.
     * 
     * @Author Ubuntu4life
     * 
     * @param condition
     *            The condition under which the script has to sleep. Walking has
     *            already been included.
     * @param targetNPC
     *            The RSNPC to interact with.
     * @param Threshold
     *            The maximum amount of milliseconds to wait /while/ something
     *            is not going the way it's supposed to.
     * @return True when everything went fine.
     */
    private boolean sleepWhile(final Condition condition,
            final RSNPC targetNPC, final int Threshold) {
        final int Irritations = Threshold / 25;
        if (!players.getMyPlayer().isMoving()
                && calc.distanceTo(targetNPC) >= 2) {
            final long endTime = System.currentTimeMillis()
                    + random(1500, 2000);
            while (System.currentTimeMillis() < endTime) {
                if (getMyPlayer().isMoving()) {
                    break;
                }
                Methods.sleep(Methods.random(20, 30));
            }
            if (!players.getMyPlayer().isMoving()) {
                return false;
            }
        }
        for (int i = 0; i < Irritations && condition.isTrue(); i++) {
            if (players.getMyPlayer().isMoving() || game.getClientState() != 11) {
                i = 0;
            }
            Methods.sleep(Methods.random(20, 30));
        }
        return condition.isTrue();
    }

    /**
     * Advanced sleeping method with NPC interaction.
     * 
     * @Author Ubuntu4life
     * 
     * @param condition
     *            The condition under which the script has to sleep. Walking has
     *            already been included.
     * @param targetObject
     *            The RSObject to interact with.
     * @param Threshold
     *            The maximum amount of milliseconds to wait /while/ something
     *            is not going the way it's supposed to.
     * @return True when everything went fine.
     */
    private boolean sleepWhile(final Condition condition,
            final RSObject targetObject, final int Threshold) {
        final int Irritations = Threshold / 25;
        final int minimalSleepTillMove = 1500;
        final int MaximalSleepTillMove = 2000;
        if (!players.getMyPlayer().isMoving()
                && Threshold < (minimalSleepTillMove + MaximalSleepTillMove) / 2) {
            if (targetObject != null) {
                if (calc.distanceTo(targetObject) >= calc
                        .distanceTo(targetObject) + 1) {
                    final long endTime = System.currentTimeMillis()
                            + random(minimalSleepTillMove, MaximalSleepTillMove);
                    while (System.currentTimeMillis() < endTime) {
                        if (getMyPlayer().isMoving()) {
                            break;
                        }
                        Methods.sleep(Methods.random(20, 30));
                    }
                    if (!players.getMyPlayer().isMoving()) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        }
        for (int i = 0; i < Irritations && condition.isTrue(); i++) {
            if (players.getMyPlayer().isMoving() || game.getClientState() != 11) {
                i = 0;
            }
            Methods.sleep(Methods.random(20, 30));
        }
        return condition.isTrue();
    }

    /**
     * 
     * @param Tile
     *            The destination RSTile.
     */
    private void webWalk(final RSTile Tile, final int xDeviation,
            final int yDeviation) {
        final RSTile destinationTile = new RSTile(Tile.getX()
                + random(0, xDeviation), Tile.getY() + random(0, yDeviation));
		final RSWeb walkWeb = web.getWeb(destinationTile);
        if (walkWeb != null && !walkWeb.finished()) {
            try {
                walkWeb.step();
            } catch (Exception ignored) {
            }
        }
    }

       
    private void moveMouse(final double time, final Point p) {
        final double endTime = System.currentTimeMillis() + time;
        while (System.currentTimeMillis() < endTime) {
            mouse.move(p, 0, 0);
        }
    }
    
    /**
     * Extracts images from an external server.
     * 
     * @return The image.
     */
    private Image getImage(final String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch (final IOException e) {
            return null;
        }
    }
}
