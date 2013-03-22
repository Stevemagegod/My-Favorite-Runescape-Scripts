import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;

import com.rarebot.script.util.Filter;

import javax.imageio.ImageIO;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.GroundItems;
import com.rarebot.script.methods.Prayer;
import com.rarebot.script.methods.Settings;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSGroundItem;
import com.rarebot.script.wrappers.RSInterface;
import com.rarebot.script.wrappers.RSModel;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPath;
import com.rarebot.script.wrappers.RSPlayer;
import com.rarebot.script.wrappers.RSTile;
import com.rarebot.script.wrappers.RSTilePath;

    @ScriptManifest(authors = "Magic", name = "WaterFiend Magician", version = 3.8, description = "Kills WaterFiends")
    public class WaterFiendMagician extends Script implements PaintListener, MouseListener, MessageListener {

     public WaterFiendMagicianGUI GUI;
   	 
	@SuppressWarnings("unused")
    private String status = "";
		    
    private int WaterFiends = 5361;
		     
	public int[] SKILLS_ARRAY = new int[25];
		   
    Rectangle close = new Rectangle(492, 343, 18, 20);
	Point p;
	boolean hide = false;
	        
    private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
		    
	private int GoldCharm = 12158;
	private int GreenCharm = 12159;
	private int CrimCharm = 12160;
	private int BlueCharm = 12163;
		    
	private int WaterOrb = 572;
    private int SeaWeed = 402;
    private int SnapeGrass = 232;
    private int UncutDiamond = 1618;
	private int WatermelonSeed = 5321;
	private int RanarrSeed = 5295;
	private int AvantoeSeed = 5298;
	private int ToadflaxSeed = 5296;
	private int LantadymeSeed = 5302;
	private int DwarfWeedSeed = 5303;
	private int DeathRune = 560;
	private int BloodRune = 565;
	private int MithrilArrow = 888;
	private int SapphireBolts = 9337;
	private int SteelBar = 2354;

	public int RawLob = 377;
	public int RawShark = 383;
		    
	public int WornWeapon = 0;
		    
    ArrayList<Integer> DropList = new ArrayList<Integer>();

	private int Drop[] = {0};
	private int Junk[] = {229, 555, 1383};
		    			
	public int PrayerPot[] = {143, 141, 139, 2434, 15331, 15330, 15329, 15328, 23253, 23251, 23249, 23247, 23245, 23243, 23530, 23529, 23528, 23527, 23526, 23525};
	public int AttackPot[] = {149, 147, 145, 2436, 15311, 15310, 15309, 15308,  23265,  23263,  23261,  23259,  23257,  23255, 23500,  23499,  23498,  23497,  23496,  23495};
	public int StrengthPot[] = {161, 159, 157, 2440, 15315, 15314, 15313, 15312, 23289, 23287, 23285, 23283, 23281, 23279, 23506, 23505, 23504, 23503, 23502, 23501};
	public int DefencePot[] = {167, 165, 163, 2442, 15319, 15318, 15317, 15316, 23301, 23299, 23297, 23295, 23293, 23291, 23512, 23510, 23509, 23508, 23507};
	public int OverLoadPot[] = {15335, 15334, 15333, 15332, 23536, 23535, 23534, 23533, 23532, 23531};
	public int SummonPot[] = {12146, 12144, 12142, 12140};
    public int PrayerRenewalPot[] = {21636, 21634, 21632, 21630, 23619, 23617, 23615, 23613, 23611, 23609};
			
	public int PouchID = 0;
    public int NumberOfPouchID = 0;
    public int SummonPotID = 12140;
    public int ScrollID = 0;
    public int SpecialMovePointsNeeded = 0;
    public int PointsToSummon = 0;
		     
	public int FoodID = 0;
    public int NumberOfFoodID = 0;
		
    public int PrayerPotID = 0; 
	public int NumberOfPrayerPotID = 0;
			
    public int PrayerRenewalPotID = 0;
    public int NumberOfPrayerRenewalPotID = 0;
			
	public int OverLoadPotID = 0;
	public int NumberOfOverLoadPotID = 0;
			
    public int AttackPotID = 0;
    public int StrengthPotID = 0;
	public int DefencePotID = 0;
	           
	public int NumberOfAttackPotID = 0;
	public int NumberOfStrengthPotID = 0;
	public int NumberOfDefencePotID = 0;
						
	public int CharmsCollected = 0;
	private int CharmsCollectedHour = 0;
		    
	public int CharmsCollectedBlue = 0;
	private int CharmsCollectedHourBlue = 0;
		    
    private RSNPC startNPC = null;
	private int npcCount = 0;
	private int npcCountHour = 0;

	public int LastTrip = 0;
    public int LastTripBlue = 0;
		    
	public int Count = 0;
    public int CountMinusCrims = 0;
	public int CountMinusBlues = 0;
		    		    
	public boolean TripEnded = false;
	public boolean DoneWalking = false;
	public boolean SSFlash = false;
	public boolean SSFlashPrayTab = false;
	public boolean SaraSpec = false;
	public boolean Enhanced = false;
	public boolean CachedWeapon = false;
	public boolean NoSpecLeft = false;
    public boolean HolyWrench = false;
    public boolean PrayerRenewalOff = false;
	public boolean FirstTime = true;
   	
    public long startTime = System.currentTimeMillis();

	private final Color color1 = new Color(51, 51, 51, 197);   
	private final Color color4 = new Color(51, 204, 0);
	private final Color color5 = new Color(255, 0, 0, 197);
	private final Color color6 = new Color(51, 51, 51, 197);
    private final Color color7 = new Color(255, 255, 255);
    private final Color color8 = new Color(0, 0, 0);
		    
    private final BasicStroke stroke1 = new BasicStroke(2);
		    
    private final Font font1 = new Font("Heiti SC", 1, 14);
    private final Font font2 = new Font("Heiti SC", 1, 11);
		    
    private Image getImage(String url) {
		        try {
		            return ImageIO.read(new URL(url));
		        } catch (IOException e) {
		            return null;
		        }
		    }
 	
    Image img1;
    Image img2;
    Image img3;
    Image img4;
	Image img5;

	private RSArea BankArea = new RSArea(new RSTile(2380, 4452), new RSTile(2394, 4462));
	private RSArea FairyRingArea = new RSArea(new RSTile(2395, 4427), new RSTile(2430, 4454));
	private RSArea WaterFiendArea = new RSArea(new RSTile(1734, 5330), new RSTile(1755, 5365));
	private RSArea WaterFiendAreaNoGo = new RSArea(new RSTile(1748, 5348), new RSTile(1780, 5367));
	private RSArea WaterPoolArea = new RSArea(new RSTile(2527, 3442), new RSTile(2550, 3455));
	private RSArea EdgeArea = new RSArea(new RSTile(3052, 3465), new RSTile(3138, 3520));
			
	private final RSTile[] BankToFairyRing = new RSTile[] {new RSTile(2381, 4458),
					new RSTile(2382, 4458),
					new RSTile(2383, 4458),
					new RSTile(2384, 4458),
					new RSTile(2385, 4458),
					new RSTile(2386, 4458),
					new RSTile(2387, 4458),
					new RSTile(2387, 4457),
					new RSTile(2387, 4456),
					new RSTile(2387, 4455),
					new RSTile(2387, 4454),
					new RSTile(2387, 4454),
					new RSTile(2388, 4454),
					new RSTile(2388, 4453),
					new RSTile(2388, 4452),
					new RSTile(2388, 4451),
					new RSTile(2389, 4451),
					new RSTile(2390, 4451),
					new RSTile(2391, 4451),
					new RSTile(2392, 4451),
					new RSTile(2393, 4451),
					new RSTile(2394, 4451),
					new RSTile(2395, 4451),
					new RSTile(2396, 4451),
					new RSTile(2397, 4451),
					new RSTile(2398, 4451),
					new RSTile(2398, 4450),
					new RSTile(2399, 4450),
					new RSTile(2400, 4450),
					new RSTile(2401, 4450),
					new RSTile(2402, 4450),
					new RSTile(2403, 4450),
					new RSTile(2404, 4450),
					new RSTile(2405, 4450),
					new RSTile(2406, 4450),
					new RSTile(2407, 4450),
					new RSTile(2408, 4450),
					new RSTile(2409, 4450),
					new RSTile(2410, 4450),
					new RSTile(2410, 4449),
					new RSTile(2410, 4448),
					new RSTile(2410, 4447),
					new RSTile(2410, 4446),
					new RSTile(2410, 4445),
					new RSTile(2410, 4444),
					new RSTile(2410, 4443),
					new RSTile(2410, 4442),
					new RSTile(2410, 4441),
					new RSTile(2411, 4441),
					new RSTile(2412, 4441),
					new RSTile(2412, 4440),
					new RSTile(2412, 4439),
					new RSTile(2412, 4438),
					new RSTile(2412, 4437),
					new RSTile(2412, 4436)};	
	private final RSTile[] FairyRingToBank = new RSTile[] {new RSTile(2412, 4436),
					new RSTile(2412, 4437),
					new RSTile(2412, 4438),
					new RSTile(2412, 4439),
					new RSTile(2412, 4440),
					new RSTile(2412, 4441),
					new RSTile(2411, 4441),
					new RSTile(2410, 4441),
					new RSTile(2410, 4442),
					new RSTile(2410, 4443),
					new RSTile(2410, 4444),
					new RSTile(2410, 4445),
					new RSTile(2410, 4446),
					new RSTile(2410, 4447),
					new RSTile(2410, 4448),
					new RSTile(2410, 4449),
					new RSTile(2410, 4450),
					new RSTile(2409, 4450),
					new RSTile(2408, 4450),
					new RSTile(2407, 4450),
					new RSTile(2406, 4450),
					new RSTile(2405, 4450),
					new RSTile(2404, 4450),
					new RSTile(2403, 4450),
					new RSTile(2402, 4450),
					new RSTile(2400, 4450),
					new RSTile(2399, 4450),
					new RSTile(2398, 4451),
					new RSTile(2397, 4451),
					new RSTile(2396, 4451),
					new RSTile(2395, 4451),
					new RSTile(2394, 4451),
					new RSTile(2393, 4451),
					new RSTile(2392, 4451),
					new RSTile(2391, 4451),
					new RSTile(2390, 4451),
					new RSTile(2389, 4451),
					new RSTile(2388, 4451),
					new RSTile(2388, 4452),
					new RSTile(2388, 4453),
					new RSTile(2388, 4454),
					new RSTile(2387, 4454),
					new RSTile(2387, 4455),
					new RSTile(2387, 4456),
					new RSTile(2387, 4457),
					new RSTile(2387, 4458),
					new RSTile(2386, 4458),
					new RSTile(2385, 4458),
					new RSTile(2384, 4458),
					new RSTile(2383, 4458),
					new RSTile(2382, 4458),
					new RSTile(2381, 4458)};	
	private final RSTile[] WaterFiendsToFairyRing = new RSTile[] {new RSTile(1739,  5356),
					new RSTile(1739,  5355),
					new RSTile(1739,  5354),
					new RSTile(1739,  5353),
					new RSTile(1739,  5352),
					new RSTile(1739,  5351),
					new RSTile(1739,  5350),
					new RSTile(1739,  5349),
					new RSTile(1739,  5348),
					new RSTile(1739,  5347),
					new RSTile(1739,  5346),
					new RSTile(1739,  5345),
					new RSTile(1739,  5344),
					new RSTile(1739,  5343)};	
	private final RSTile[] DragonToWaterFiends = new RSTile[] {new RSTile(1749,  5330),
					new RSTile(1748,  5330),
					new RSTile(1747,  5330),
					new RSTile(1746,  5330),
					new RSTile(1745,  5330),
					new RSTile(1744,  5330),
					new RSTile(1743,  5330),
					new RSTile(1742,  5330),
					new RSTile(1741,  5330),
					new RSTile(1740,  5330),
					new RSTile(1739,  5343), 
					new RSTile(1739,  5344),
					new RSTile(1739,  5345),
					new RSTile(1739,  5346),
					new RSTile(1739,  5347),
					new RSTile(1739,  5348),
					new RSTile(1739,  5349),
					new RSTile(1739,  5350),
					new RSTile(1739,  5351),
					new RSTile(1739,  5352),
					new RSTile(1739,  5353),
					new RSTile(1739,  5354),
					new RSTile(1739,  5355),
					new RSTile(1739,  5356)};	
	private final RSTile[] BankToSummon = new RSTile[] {new RSTile(2381, 4458),
					new RSTile(2382, 4458),
					new RSTile(2383, 4458),
					new RSTile(2384, 4458),
					new RSTile(2385, 4458),
					new RSTile(2386, 4458),
					new RSTile(2387, 4458),
					new RSTile(2387, 4457),
					new RSTile(2387, 4456),
					new RSTile(2387, 4455),
					new RSTile(2387, 4454),
					new RSTile(2387, 4454),
					new RSTile(2388, 4454),
					new RSTile(2388, 4453),
					new RSTile(2388, 4452),
					new RSTile(2388, 4451),
					new RSTile(2389, 4451),
					new RSTile(2390, 4451),
					new RSTile(2391, 4451),
					new RSTile(2392, 4451),
					new RSTile(2393, 4451),
					new RSTile(2394, 4451),
					new RSTile(2395, 4451),
					new RSTile(2396, 4451),
					new RSTile(2397, 4451),
					new RSTile(2398, 4451),
					new RSTile(2398, 4450),
					new RSTile(2399, 4450),
					new RSTile(2400, 4450),
					new RSTile(2401, 4450),
					new RSTile(2402, 4450),
					new RSTile(2403, 4450),
					new RSTile(2404, 4450),
					new RSTile(2405, 4450),
					new RSTile(2405, 4444)};						
	private final RSTile[] EdgeToFairyRing = new RSTile[] {new RSTile(3069, 3503),
	        		new RSTile(3070, 3503),
	        		new RSTile(3071, 3503),
	        		new RSTile(3072, 3503),
	        		new RSTile(3073, 3503),
	        		new RSTile(3074, 3503),
	        		new RSTile(3075, 3503),
	        		new RSTile(3076, 3503),
	        		new RSTile(3077, 3503),
	        		new RSTile(3078, 3503),
	        		new RSTile(3079, 3503),
	        		new RSTile(3080, 3503),
	        		new RSTile(3081, 3503),
	        		new RSTile(3082, 3503),
	        		new RSTile(3083, 3503),
	        		new RSTile(3084, 3503),
	        		new RSTile(3085, 3503),
	        		new RSTile(3086, 3503),
	        		new RSTile(3087, 3503),
	        		new RSTile(3088, 3503),
	        		new RSTile(3089, 3503),
	        		new RSTile(3090, 3503),
	        		new RSTile(3091, 3503),
	        		new RSTile(3092, 3503),
	        		new RSTile(3093, 3503),
	        		new RSTile(3094, 3503),
	        		new RSTile(3095, 3503),
	        		new RSTile(3096, 3503),
	        		new RSTile(3097, 3503),
	        		new RSTile(3098, 3503),
	        		new RSTile(3099, 3503),
	        		new RSTile(3100, 3503),
	        		new RSTile(3101, 3503),
	        		new RSTile(3102, 3503),
	        		new RSTile(3103, 3503),
	        		new RSTile(3104, 3503),
	        		new RSTile(3104, 3504),
	        		new RSTile(3104, 3505),
	        		new RSTile(3104, 3506),
	        		new RSTile(3104, 3507),
	        		new RSTile(3105, 3507),
	        		new RSTile(3106, 3507),
	        		new RSTile(3107, 3507),
	        		new RSTile(3108, 3507),
	        		new RSTile(3109, 3507),
	        		new RSTile(3110, 3507),
	        		new RSTile(3111, 3507),
	        		new RSTile(3112, 3507),
	        		new RSTile(3113, 3507),
	        		new RSTile(3114, 3507),
	        		new RSTile(3115, 3507),
	        		new RSTile(3115, 3508),
	        		new RSTile(3115, 3509),
	        		new RSTile(3115, 3510),
	        		new RSTile(3115, 3511),
	        		new RSTile(3115, 3512),
	        		new RSTile(3115, 3513),
	        		new RSTile(3115, 3514),
	        		new RSTile(3115, 3515),
	        		new RSTile(3115, 3516),
	        		new RSTile(3116, 3516),
	        		new RSTile(3117, 3516),
	        		new RSTile(3118, 3516),
	        		new RSTile(3119, 3516),
	        		new RSTile(3120, 3516),
	        		new RSTile(3121, 3516),
	        		new RSTile(3122, 3516),
	        		new RSTile(3123, 3516),
	        		new RSTile(3124, 3516),
	        		new RSTile(3125, 3516),
	        		new RSTile(3126, 3516),
	        		new RSTile(3127, 3516),
	        		new RSTile(3128, 3516),
	        		new RSTile(3129, 3516),
	        		new RSTile(3130, 3516),
	        		new RSTile(3131, 3516),
	        		new RSTile(3132, 3516),
	        		new RSTile(3133, 3516),
	        		new RSTile(3134, 3516),
	        		new RSTile(3134, 3515),
	        		new RSTile(3134, 3514),
	        		new RSTile(3134, 3513),
	        		new RSTile(3134, 3512),
	        		new RSTile(3134, 3511),
	        		new RSTile(3134, 3510),
	        		new RSTile(3134, 3509),
	        		new RSTile(3134, 3508),
	        		new RSTile(3134, 3507),
	        		new RSTile(3134, 3506),
	        		new RSTile(3133, 3506),
	        		new RSTile(3132, 3506),
	        		new RSTile(3131, 3506),
	        		new RSTile(3131, 3505),
	        		new RSTile(3131, 3504),
	        		new RSTile(3131, 3503),
	        		new RSTile(3131, 3502),
	        		new RSTile(3131, 3501),
	        		new RSTile(3131, 3500),
	        		new RSTile(3131, 3499),
	        		new RSTile(3131, 3498),
	        		new RSTile(3131, 3497),
	        		new RSTile(3131, 3496)};
	
	RSTilePath FairyRingWalk;
	RSTilePath BankWalk;
	RSTilePath WaterFiendWalk;
	RSTilePath DragonWalk;
	RSTilePath SummonWalk;
    RSTilePath EdgeWalk;
			
    private RSNPC InfoMonsterAttackingMeDragon() {
	        return  npcs.getNearest(new Filter<RSNPC>(){
		            public boolean accept(RSNPC npc) {  
		                   return npc.getInteracting() != null && npc != null && npc.getInteracting().equals(players.getMyPlayer()) && players.getMyPlayer().getInteracting() == null && npc.getID() == 5362 && npc.getName().equals("Brutal green dragon") && !npc.isDead() && isGroundItemValid(groundItems.getNearest(Drop)) != true && TripEnded != true; 
		            }
	        });
    }	
    private RSNPC InfoWaterFiendAttackingMe() {
		    return  npcs.getNearest(new Filter<RSNPC>(){
					public boolean accept(RSNPC npc) {  
					       return npc.getInteracting() != null && npc != null && npc.getInteracting().equals(players.getMyPlayer()) && players.getMyPlayer().getInteracting() == null && npc.getName().contains("Waterfiend") && !npc.isDead() && npc.getID() == WaterFiends && WaterFiendArea.contains(npc.getLocation()) && isGroundItemValid(groundItems.getNearest(Drop)) != true && TripEnded != true; 
					}
		    });
    }
	private RSNPC InfoWaterFiend() {
			return  npcs.getNearest(new Filter<RSNPC>(){
	                public boolean accept(RSNPC npc) {  
	                	   return npc != null && npc.getName().contains("Waterfiend") && !npc.isDead() && npc.getID() == WaterFiends && WaterFiendArea.contains(npc.getLocation()) && !WaterFiendAreaNoGo.contains(npc.getLocation()) && isGroundItemValid(groundItems.getNearest(Drop)) != true && TripEnded != true && !npc.isInCombat(); 
	                }
			});
	}
								      
    public boolean onStart() { 
 		  
 	  log("Welcome to WaterFiend Magician");

	    log("... Loading: Paint ...");
        img1 = getImage("http://i39.tinypic.com/veakgi.jpg");  
        img2 = getImage("http://i49.tinypic.com/fn5wnr.png");
        img3 = getImage("http://i46.tinypic.com/mkypu1.jpg");
        img4 = getImage("http://i49.tinypic.com/2rqm6xk.jpg"); 
        img5 = getImage("http://i41.tinypic.com/o85r4h.png");
        log("... Paint: Loaded ...");
	   
 		try {
			SwingUtilities.invokeAndWait(new Runnable() 
			{ 
				public void run() 
				{ 	
					GUI = new WaterFiendMagicianGUI();
					GUI.setVisible(true);
				} 
			});
			
		} 
 		
		catch (InterruptedException e){} 
		catch (InvocationTargetException e){}
		
		while(GUI.isVisible()) 
		{
			status = "Settings";
			sleep(50);
		}

		   while (!game.isLoggedIn()) {
				status = "Waiting...";
			    sleep(50);
		   }
		   
		   // Default Drops

			DropList.add(572);
			DropList.add(12160);
			DropList.add(12163);
			DropList.add(18778);
			DropList.add(18757);

			// RareDrop Table
			
			DropList.add(6686);
			DropList.add(1516);
			DropList.add(5304);
			DropList.add(1216);
			DropList.add(9342);
			DropList.add(20667);
			DropList.add(1392);
			DropList.add(574);
			DropList.add(570);
			DropList.add(7937);
			DropList.add(2362);
			DropList.add(452);
			DropList.add(2364);
			DropList.add(270);
			
			int[] ia = new int[DropList.size()];
			for (int i=0; i<ia.length; i++) {
			   ia[i] = DropList.get(i);
			}
			
			Drop = ia;

		    getSkillData();
		    
		    FirstTime = true;
		    
		    return true;	  
 	  }
    public void onFinish() {
    log("Thanks for using WaterFiend Magician"); 
    }
	  
 	@SuppressWarnings("static-access")
	@Override
	public int loop() {
 		
 		try {
 		
 		   if (EdgeArea.contains(players.getMyPlayer().getLocation())) {
  
			   RSObject fr = objects.getNearest(14097);
			   
			   if (!inventory.containsOneOf(PrayerPotID) || !inventory.containsOneOf(FoodID)) {
			   stopScript();
			   return 0;
			   }
			   
			   camera.setAngle(270);
			   
			   if (isQuickPrayerOn()) {
	    		   setQuickPrayer(false);
			   }
 		   
 			   if (players.getMyPlayer().getAnimation() == -1) {
 				   
 				   if (fr == null) {
 				   
 				   RSPath EdgeWalk = walking.newTilePath(EdgeToFairyRing);
 				   
 				   while (calc.distanceTo(EdgeWalk.getEnd()) > 7 && game.isLoggedIn()) {	
				 		 status = "Walking: FairyRing";
				 		 EdgeWalk.traverse();  	
					     sleep(900, 1200);    	    	        	 
 				   }
 				   return 0;
 				   }
 				   else if (fr != null) {
 						if (fr.isOnScreen()) {
 								
	    					  for (int i = 0; i < 1000000000 && players.getMyPlayer().getAnimation() != 3254; i++) {
 									status = "Using: Fairy Ring";
 									fr.getModel().interact("Use");
 									sleep(1000, 1200);
	    					  }
 									
 	    					  for (int i = 0; i < 1000000000 && !FairyRingArea.contains(players.getMyPlayer().getLocation()); i++) {
 	    					    	sleep(50);
 	    					  }
 					          return 0;
 						}
 						else if (!fr.isOnScreen()) {
 							camera.turnTo(fr);
 							walking.walkTileMM(fr.getLocation());
 							return 0;
 						}
 				  }
 			   }
 			   return 0;
 		   }
 		
 		   if (WaterPoolArea.contains(players.getMyPlayer().getLocation())) {
 			   
 			   if (isQuickPrayerOn()) {
	    		   setQuickPrayer(false);
			   }
 			   
 			   if (game.getTab() != Tab.MAGIC) {
 				   game.openTab(Tab.MAGIC);
 				   return 0;
 			   }
 			   else if (game.getTab() == Tab.MAGIC) {
 				     
 				    if (interfaces.getComponent(192, 24).isValid() && !interfaces.getComponent(1092, 45).isValid()) {
 				    	interfaces.getComponent(192, 24).doClick(true);
 				        sleep(1000, 1200);
 				    	return 0;
 				    }
 				    else if (interfaces.getComponent(1092, 45).isValid()) {
 				          	 interfaces.getComponent(1092, 45).doClick();
 				    }
 				    
 				   for (int i = 0; i < 13 && players.getMyPlayer().getAnimation() != 16385; i++) { 
 				   sleep(50);   
 				   }
 				   for (int i = 0; i < 1000000000 && !EdgeArea.contains(players.getMyPlayer().getLocation()); i++) { 
 	 			   sleep(50);   
 				   }
 				   return 0;
 			   }
 			   return 0;
 		   }
 		   
 		   if (WaterFiendArea.contains(players.getMyPlayer().getLocation())) {
 			   
    		   camera.setPitch(100);
 			    			   
 			   if (TripEnded == true) {
 				   if (DoneWalking == false) {
 					  
 	 	          status = "Walking: Fairy Ring";
 	 	          
 	 			  camera.setPitch(100);
 				  camera.setAngle(180);
 				   
 				   RSPath WaterFiendWalk = walking.newTilePath(WaterFiendsToFairyRing);
					  
				 	 while (calc.distanceTo(WaterFiendWalk.getEnd()) > 7 && game.isLoggedIn()) {	
				 		 status = "Walking: FairyRing";
				 		 WaterFiendWalk.traverse();  	
					     sleep(900, 1200);    	    	        	 
					   	 }
				 	 
				   DoneWalking = true;
				   return random(500, 800);
 				   }
				 	 
	 			  RSObject fr = objects.getNearest(16944);

 				  if (fr != null) {
 						if (fr.isOnScreen()) {
 								
 							  for (int i = 0; i < 13 && players.getMyPlayer().getAnimation() != 3254; i++) { 
 									status = "Using: Fairy Ring";
 									fr.interact("Use");
 									sleep(1000, 1200);
 							        } 
 							  
 	    					    for (int i = 0; i < 1000000000 && !FairyRingArea.contains(players.getMyPlayer().getLocation()); i++) {
 	    					    	sleep(50);
 	    					    }
 					        return 0;
 						}
 						else if (!fr.isOnScreen()) {
 							camera.turnTo(fr);
 							walking.walkTileMM(fr.getLocation());
 							return 0;
 						}
 					}
               return 0;
 			   }
 			   else {
 				   
 			   if (getInteractingNPC() != null && startNPC == null) {
 			       startNPC = getInteractingNPC();
 			   }
 			   
               final RSNPC GreenDragon = InfoMonsterAttackingMeDragon();
               
               if (GreenDragon != null) { 
            	   RSPath DragonWalk = walking.newTilePath(DragonToWaterFiends);		  
				 	 while (calc.distanceTo(DragonWalk.getEnd()) > 7 && game.isLoggedIn()) {	
		            	 status = "Walking Away From: Green drag";	
				 	   	 DragonWalk.traverse();  	
					     sleep(900, 1200);    	    	        	 
					   	 }
			   return 0;
               }
               
               if (npcs.getNearest(5362) != null) {
            	   if (calc.distanceTo(npcs.getNearest(5362).getLocation()) <= 14) {
            		 RSPath DragonWalk = walking.newTilePath(DragonToWaterFiends);		  
  				 	 while (calc.distanceTo(DragonWalk.getEnd()) > 7 && game.isLoggedIn()) {	
  		            	 status = "Walking Away From: Green drag";	
  				 	   	 DragonWalk.traverse();  	
  					     sleep(900, 1200);    	    	        	 
  					   	 }
  				   return 0;
            	   }
               }
    		   
    		   if (interfaces.getComponent(747, 0).getTextureID() == 1244 && !inventory.contains(PouchID) && NumberOfPouchID != 0) {
    		       TripEnded = true;   
    		   }
               
	    	   if (interfaces.getComponent(747, 0).getTextureID() == 1244 && inventory.contains(PouchID) && NumberOfPouchID != 0 && Integer.parseInt(interfaces.getComponent(747, 7).getText()) >= PointsToSummon)  {
		   		        	if (inventory.getItem(PouchID) != null) {
		   		        	inventory.getItem(PouchID).interact("Summon");
		   		        	sleep(random(1200, 1500));
		   		        	}
		   		        	return 0;
	    	   }
	    	   
	    	   if (inventory.contains(CrimCharm) && npcCount == 0) {
	 		       CountMinusCrims = inventory.getItem(CrimCharm).getStackSize();
	    	   }
	 			   
	 		   if (inventory.contains(BlueCharm) && npcCount == 0) {
	  		       CountMinusBlues = inventory.getItem(BlueCharm).getStackSize();
	 		   }
	    	                                  			      
 			   if (inventory.contains(CrimCharm) && npcCount > 0) {
 				  Count = inventory.getItem(CrimCharm).getStackSize();
 				  CharmsCollected = Count + LastTrip - CountMinusCrims;
 			   }
 			   
 			   if (inventory.contains(BlueCharm) && npcCount > 0) {
  				  Count = inventory.getItem(BlueCharm).getStackSize();
  				  CharmsCollectedBlue = Count + LastTripBlue - CountMinusBlues;
  			   }
 			     
 			   if (players.getMyPlayer().getInteracting() == null) {
				   status = "WaterFiend Area";
 			   }
 			   
 			   if (Enhanced == true) {
 			       if (CachedWeapon == false) {
 	 				   WornWeapon = equipment.getItem(equipment.WEAPON).getID();
 	 				   CachedWeapon = true;
 			       }
 			   }
 			   
	   			   
	          if (isGroundItemValid(groundItems.getNearest(Drop)) == true) {
	        	  if (!inventory.isFull() || inventory.containsOneOf(Junk) || inventory.containsOneOf(FoodID) || inventory.containsOneOf(RawLob) || inventory.containsOneOf(RawShark) || groundItems.getNearest(Drop).getItem().getID() == CrimCharm && inventory.contains(CrimCharm) || groundItems.getNearest(Drop).getItem().getID() == BlueCharm && inventory.contains(BlueCharm) || groundItems.getNearest(Drop).getItem().getID() == WaterOrb  && inventory.contains(WaterOrb)
	        			  || groundItems.getNearest(Drop).getItem().getID() == SeaWeed  && inventory.contains(SeaWeed)
	        			  || groundItems.getNearest(Drop).getItem().getID() == SnapeGrass  && inventory.contains(SnapeGrass)
	        			  || groundItems.getNearest(Drop).getItem().getID() == UncutDiamond  && inventory.contains(UncutDiamond)
	        			  || groundItems.getNearest(Drop).getItem().getID() ==  WatermelonSeed  && inventory.contains( WatermelonSeed)
	        			  || groundItems.getNearest(Drop).getItem().getID() == RanarrSeed  && inventory.contains(RanarrSeed)
	        			  || groundItems.getNearest(Drop).getItem().getID() == AvantoeSeed  && inventory.contains(AvantoeSeed)
	        			  || groundItems.getNearest(Drop).getItem().getID() == ToadflaxSeed  && inventory.contains(ToadflaxSeed)
	        			  || groundItems.getNearest(Drop).getItem().getID() == LantadymeSeed  && inventory.contains(LantadymeSeed)
	        			  || groundItems.getNearest(Drop).getItem().getID() ==  DwarfWeedSeed  && inventory.contains( DwarfWeedSeed)
	        			  || groundItems.getNearest(Drop).getItem().getID() == DeathRune  && inventory.contains(DeathRune)
	        			  || groundItems.getNearest(Drop).getItem().getID() == BloodRune  && inventory.contains(BloodRune)
	        			  || groundItems.getNearest(Drop).getItem().getID() == MithrilArrow  && inventory.contains(MithrilArrow)
	        			  || groundItems.getNearest(Drop).getItem().getID() == SapphireBolts  && inventory.contains(SapphireBolts)
	        			  || groundItems.getNearest(Drop).getItem().getID() ==  SteelBar  && inventory.contains(SteelBar)) {
  	              LootItemDrops();
    	          return 0;
	        	  }
	          }
 			   
 			  if (groundItems.getNearest(RawShark) != null && inventory.contains(12438) && PouchID == 12029 && skills.getRealLevel(skills.COOKING) >= 80 && !inventory.isFull() && isGroundItemValid(groundItems.getNearest(Drop)) == false && calc.distanceBetween(groundItems.getNearest(RawShark).getLocation(), players.getMyPlayer().getLocation()) <= 7) {
 				  if (groundItems.getNearest(RawShark) != null) {
			        	if (groundItems.getNearest(RawShark).isOnScreen()) {
			        		interact("Take " + groundItems.getNearest(RawShark).getItem().getName(), groundItems.getNearest(RawShark));
			        	}
			        	else if(!groundItems.getNearest(RawLob).isOnScreen()) {
			        		walking.walkTileMM(groundItems.getNearest(RawShark).getLocation());
			        		camera.turnTo(groundItems.getNearest(RawShark).getLocation());
			        	}
			        }
			   return random(800, 1200);
			   }
 			  
			   if (groundItems.getNearest(RawLob) != null && inventory.contains(12438) && PouchID == 12029 && skills.getRealLevel(skills.COOKING) >= 40 && !inventory.isFull() && isGroundItemValid(groundItems.getNearest(Drop)) == false && calc.distanceBetween(groundItems.getNearest(RawLob).getLocation(), players.getMyPlayer().getLocation()) <= 7) {
 			        if (groundItems.getNearest(RawLob) != null) {
 			        	if (groundItems.getNearest(RawLob).isOnScreen()) {
 			        		interact("Take " + groundItems.getNearest(RawLob).getItem().getName(), groundItems.getNearest(RawLob));
 			        	}
 			        	else if(!groundItems.getNearest(RawLob).isOnScreen()) {
 			        		walking.walkTileMM(groundItems.getNearest(RawLob).getLocation());
 			        		camera.turnTo(groundItems.getNearest(RawLob).getLocation());
 			        	}
 			        }
 			  return random(800, 1200);
			  }
 			  
 			  if (inventory.contains(24154)) {
 				  inventory.getItem(24154).interact("Claim");
 				  sleep(random(1000, 1200));
 			  }
 			  
 			  if (inventory.containsOneOf(Junk)) {
				    for (int i = 0; i < 13 && inventory.containsOneOf(Junk); i++) {
			         inventory.getItem(Junk).interact("Drop");
			         sleep(random(1000, 1200));
				    }
 			  }
 	  
 			  int RealHealth = skills.getRealLevel(skills.CONSTITUTION);
 		    	
 		      if (RealHealth > 99) {
 		    	  RealHealth = 99;
 		      }
 			  
 			  if (((combat.getLifePoints() * 10) / RealHealth) >= 60.6 && NoSpecLeft == true) {
 				  NoSpecLeft = false;
 			  }
 			   			   			       
 	         if (players.getMyPlayer().isInCombat()) {
 	 			 SpecialAttack();
			     Eat();
				 DrinkPotsDeg();
				 CheckPrayer(); 
 	         }
 	         
  	        if (startNPC != null && getInteractingNPC() == null) {
	             npcCount++;
	             startNPC = null;
	             for (int i = 0; i < 37 && isGroundItemValid(groundItems.getNearest(Drop)) == false; i++) {
	             sleep(50); 
	             }
	         	 if (isGroundItemValid(groundItems.getNearest(Drop)) == true) {
	         		 return 0;
	         	 }
	         }
	          			  	  			  		  
 			  final RSNPC WaterFiend = InfoWaterFiend();
 			  final RSNPC WaterFiendAttackingMe = InfoWaterFiendAttackingMe();
 
 			  if (players.getMyPlayer().getInteracting() instanceof RSNPC) {
				   status = "Attacking: " + players.getMyPlayer().getInteracting().getName();
				   return 0;
 			  } 
 			  else if (WaterFiendAttackingMe != null && calc.distanceTo(WaterFiendAttackingMe) <= 15 && isGroundItemValid(groundItems.getNearest(Drop)) != true) {
 				   if (WaterFiendAttackingMe.isOnScreen()) {
	    		       for (int i = 0; i < 13 && WaterFiendAttackingMe != null && WaterFiendAttackingMe.isOnScreen() && !(players.getMyPlayer().getInteracting() instanceof RSNPC); i++) {
 					   if (interact("Attack " + WaterFiendAttackingMe.getName(), WaterFiendAttackingMe))
 					   sleep(random(500, 800));
	    		       }
		    		   return 0;
 				   }
		      else if (!WaterFiendAttackingMe.isOnScreen()) {
   		               for (int i = 0; i < 13 && !players.getMyPlayer().isMoving(); i++) {
	    		       walking.walkTileMM(WaterFiendAttackingMe.getLocation());
	    		       sleep(100);
   		               }
	    		       for (int i = 0; i < 13 && !WaterFiendAttackingMe.isOnScreen(); i++) {
						   sleep(50);
				       }
		    		   camera.turnTo(WaterFiendAttackingMe);
		    		   return 0;
		      }
 			  }
 		      else if (WaterFiend != null && players.getMyPlayer().getInteracting() == null && calc.distanceTo(WaterFiend) <= 15 && isGroundItemValid(groundItems.getNearest(Drop)) != true) {  
 		    	   if (WaterFiend.isOnScreen()) {
	    		       for (int i = 0; i < 13 && WaterFiend != null && WaterFiend.isOnScreen() && !(players.getMyPlayer().getInteracting() instanceof RSNPC); i++) {
 		   		       if (interact("Attack " + WaterFiend.getName(), WaterFiend))
 		   		   	   sleep(random(500, 800));
	    		       }
		    		   return 0;
 		    	   }
		      else if (!WaterFiend.isOnScreen()) {
		               for (int i = 0; i < 13 && !players.getMyPlayer().isMoving(); i++) {
				       walking.walkTileMM(WaterFiend.getLocation());
				       sleep(100);
		               }
				       for (int i = 0; i < 13 && !WaterFiend.isOnScreen(); i++) {
						   sleep(50);
				       }
		    		   camera.turnTo(WaterFiend);
		    		   return 0;
		      }
 		      }
 			  }
 		   }
 		   
 		   if (FairyRingArea.contains(players.getMyPlayer().getLocation())) {
 			   
 		   if (TripEnded == true) {
 				   
	               status = "Walking: Bank";
 				   
 				   camera.setPitch(100);
 				   camera.setAngle(90);
 				   
 				   if (isQuickPrayerOn()) {
 		    		   setQuickPrayer(false);
 				   }
 				   
 				   RSPath BankWalk = walking.newTilePath(FairyRingToBank);
					  
				 	 while (calc.distanceTo(BankWalk.getEnd()) > 7 && game.isLoggedIn()) {	
				 		 status = "Walking: Bank";
				 	   	 BankWalk.traverse();  	
					     sleep(900, 1200);    	    	        	 
					   	 }
				 	 return 0;  
 		   }
 		   else {
 				   
 				  RSObject fr = objects.getNearest(12128);
 				   
 				  if (fr != null) {
				      if (fr.isOnScreen()) {
							
					        for (int i = 0; i < 13 && !interfaces.getComponent(735, 35).isValid(); i++) { 
							status = "Using: Fairy Ring";
							interact("Use", fr);
							sleep(1000, 1200);
					        } 
    						if (interfaces.getComponent(735, 35).isValid()) {
    					    	interfaces.getComponent(735, 35).doClick(true);
        			        	sleep(3000, 3500);    	
    					    }
    					    for (int i = 0; i < 13 && interfaces.getComponent(734, 0).isValid(); i++) {
    					    	interfaces.getComponent(734, 21).doClick(true);
        			        	sleep(1200, 1500);   
    					    }
    					    for (int i = 0; i < 1000000000 && !WaterFiendArea.contains(players.getMyPlayer().getLocation()); i++) {
    					    	sleep(50);
    					    }
					        return 0;
				      }
						else if (!fr.isOnScreen()) {
							camera.turnTo(fr);
							return 0;
						}
 				  }
 		   }
 			   return 0;
 		   }
 		   
 		   if (BankArea.contains(players.getMyPlayer().getLocation())) {
 			   
 		   TripEnded = false;
 		   DoneWalking = false;
 		   
		   if (PouchID == 12029) { 
		   ScrollID = 12438;
		   }
		   else if (PouchID == 12039) { 
		   ScrollID = 12434;
		   }
		   
	       int RealHealth = skills.getRealLevel(skills.CONSTITUTION);
	    	
		   if (RealHealth > 99) {
		       RealHealth = 99;
		   }

 		   status = "Bank: Area";
 			   
 		   if (bank.isOpen()) {
 			   
 		   sleep(random(500, 800));
 				   
 	       status = "Bank: Open";
 				   
 				   if (inventory.containsOneOf(Drop)) {
 					   if (inventory.getItem(CrimCharm) != null) {
 						   LastTrip += inventory.getItem(CrimCharm).getStackSize(); 
 					   }
 					   if (inventory.getItem(BlueCharm) != null) {
						   LastTripBlue += inventory.getItem(BlueCharm).getStackSize(); 
					   }
 					   status = "Bank: Depositing: All";
 					   bank.depositAll();
 					   sleep(random(500, 800));
 					   return 0;
 				   }
 				   else {
 					   		   
 				   if (inventory.isFull()) {
 						   bank.depositAll();
 						   sleep(random(500, 800));
 						   return 0;
 				   }
 					    					   
 			       if (NumberOfOverLoadPotID <= 0) {

					   if (NumberOfAttackPotID > 0) {
					   if (bank.getCount(AttackPotID) > 0) {
						   for (int i = 0; i < 13 && inventory.getCount(AttackPotID) != NumberOfAttackPotID; i++) {
						   status = "Withdrawing: Attack Potion (4)";
						   bank.withdraw(AttackPotID, NumberOfAttackPotID);
						   sleep (random (1200, 1500));
						   if (inventory.getCount(AttackPotID) > NumberOfAttackPotID) {
							   inventory.getItem(AttackPotID).interact("Deposit-All");
						   }
						   }
						   }
					   else {
					   log("Bank: Contains No Attack Potions");
					   stopScript();
					   }
					   }
					   
					   if (NumberOfStrengthPotID > 0) {
					   if (bank.getCount(StrengthPotID) > 0) {
						   for (int i = 0; i < 13 && inventory.getCount(StrengthPotID) != NumberOfStrengthPotID; i++) {
						   status = "Withdrawing: Strength Potion (4)";
						   bank.withdraw(StrengthPotID, NumberOfStrengthPotID);
						   sleep (random (1200, 1500));
						   if (inventory.getCount(StrengthPotID) > NumberOfStrengthPotID) {
							   inventory.getItem(StrengthPotID).interact("Deposit-All");
						   }
						   }
						   }
					   else {
						   log("Bank: Contains No Stength Potions");
						   stopScript();
						   }
					   }
					   
					   if (NumberOfDefencePotID > 0) {
					   if (bank.getCount(DefencePotID) > 0) {
						   for (int i = 0; i < 13 && inventory.getCount(DefencePotID) != NumberOfDefencePotID; i++) {
						   status = "Withdrawing: Defence Potion (4)";
						   bank.withdraw(DefencePotID, NumberOfDefencePotID);
						   sleep (random (1200, 1500));
						   if (inventory.getCount(DefencePotID) > NumberOfDefencePotID) {
							   inventory.getItem(DefencePotID).interact("Deposit-All");
						   }
						   }
						   }
					   else {
						   log("Bank: Contains No Defence Potions");
						   stopScript();
						   }
					   }

					   if (HolyWrench == true) {
						   if (bank.getCount(6714) > 0) {
							   for (int i = 0; i < 13 && inventory.getItem(6714) == null; i++) {
								   status = "Withdrawing: Holy Wrench";
								   bank.withdraw(6714, 1);
								   sleep (random (1200, 1500));   
							   }
						   }
						   else {
							   log("Bank: Contains No Holy Wrench");
							   stopScript();
							   }
					   }
					   
				  	   if (Enhanced == true) {
						   if (bank.getCount(14632) > 0) {
							   for (int i = 0; i < 13 && inventory.getItem(14632) == null; i++) {
								   status = "Withdrawing: Enhanced E";
								   bank.withdraw(14632, 1);
								   sleep (random (1200, 1500));   
							   }
						   }
						   else {
							   log("Bank: Contains No Enhanced E");
							   stopScript();
							   }
					   }
					   
					   if (NumberOfPouchID > 0) {
						   
					        if (bank.getCount(SummonPotID) > 0) {
								   for (int i = 0; i < 13 && inventory.getCount(SummonPotID) != 1; i++) {
								   status = "Withdrawing: Summoning Potion (4)";
								   bank.withdraw(SummonPotID, 1);
								   if (inventory.getCount(SummonPotID) > 1) {
									   inventory.getItem(SummonPotID).interact("Deposit-All");
								   }
								   sleep (random (1500, 1750));
								   }
	                               }
					        else {
								   log("Bank: Contains No Summoning Potions (4)");
								   stopScript();
								   }
						   
						   if (bank.getCount(PouchID) > 0) {
							   for (int i = 0; i < 13 && inventory.getCount(PouchID) != NumberOfPouchID; i++) {
							   status = "Withdrawing: Pouch";
							   bank.withdraw(PouchID, NumberOfPouchID);
							   if (inventory.getCount(PouchID) > NumberOfPouchID) {
								   inventory.getItem(PouchID).interact("Deposit-All");
							   }
							   sleep(1500, 1750);
							   }
						   }
						   else {
							   log("Bank: Contains No Pouches");
							   stopScript();
							   }
						   
                               if (bank.getCount(ScrollID) > 0) {						   
							   for (int i = 0; i < 13 && inventory.getItem(ScrollID) == null; i++) {
							   status = "Withdrawing: Scrolls";
							   bank.withdraw(ScrollID, 250);
							   sleep(1500, 1750);
							   }
                               }
                               else {
        						   log("Bank: Contains No Scrolls");
        						   stopScript();
        						   }
					   }
					   
					   if (NumberOfPrayerRenewalPotID > 0) {
							   if (bank.getCount(PrayerRenewalPotID) > 0) {
								   for (int i = 0; i < 13 && inventory.getCount(PrayerRenewalPotID) != NumberOfPrayerRenewalPotID; i++) {
								   status = "Withdrawing: Prayer Renewal Potion (4)";
								   bank.withdraw(PrayerRenewalPotID, NumberOfPrayerRenewalPotID);
								   sleep (random (1200, 1500));
								   if (inventory.getCount(PrayerRenewalPotID) > NumberOfPrayerRenewalPotID) {
									   inventory.getItem(PrayerRenewalPotID).interact("Deposit-All");
								   }
								   }
							   }
							   else {
								   log("Bank: Contains No Prayer Renewal Potions");
								   stopScript();
							   }
					   }
					   
				       if (NumberOfPrayerPotID > 0) {
					   if (bank.getCount(PrayerPotID) > 0) {
						   for (int i = 0; i < 13 && inventory.getCount(PrayerPotID) != NumberOfPrayerPotID; i++) {
						   status = "Withdrawing: Prayer Potion (4)";
						   bank.withdraw(PrayerPotID, NumberOfPrayerPotID);
						   sleep (random (1200, 1500));
						   if (inventory.getCount(PrayerPotID) > NumberOfPrayerPotID) {
							   inventory.getItem(PrayerPotID).interact("Deposit-All");
						   }
						   }
					   }
					   else {
						   log("Bank: Contains No Prayer Potions");
						   stopScript();
						   }
 				   }
					      
				   if (NumberOfFoodID > 0) {
				   if (bank.getCount(FoodID) > 0) {
					   for (int i = 0; i < 13 && inventory.getCount(FoodID) != NumberOfFoodID; i++) {
					   status = "Withdrawing: Food";
					   bank.withdraw(FoodID, NumberOfFoodID);
					   sleep (random (1200, 1500));

		 			   if (((combat.getLifePoints() * 10) / RealHealth) <= 75 && inventory.contains(FoodID)) {
		 					 for (int i1 = 0; i1 < 13 && combat.getHealth() <= 75 && inventory.contains(FoodID); i1++) {		
		 							if (inventory.getItem(FoodID) != null) {
		 							if (inventory.containsOneOf(FoodID)) {
		 								status = "Eating: Food";
		 								inventory.getItem(FoodID).interact("Eat");
			 					        sleep(500, 800);
		 							}
		 							}
		 					 }
		 			   }
		 			   
					   if (inventory.getCount(FoodID) > NumberOfFoodID) {
						   inventory.getItem(FoodID).interact("Deposit-All");
					   }
					   }
					   }
				   else {
					   log("Bank: Contains No Food");
					   stopScript();
				   }
				   }
 					 }
 				     else if (NumberOfOverLoadPotID > 0) {
 						 
					           if (NumberOfOverLoadPotID > 0) {
 						       if (bank.getCount(OverLoadPotID) > 0) {
 							   for (int i = 0; i < 13 && inventory.getCount(OverLoadPotID) != NumberOfOverLoadPotID; i++) {
 							   status = "Withdrawing: Extreme Potion (4)";
 							   bank.withdraw(OverLoadPotID, NumberOfOverLoadPotID);
 							   sleep (random (1200, 1500));
 							   if (inventory.getCount(OverLoadPotID) > NumberOfOverLoadPotID) {
 								   inventory.getItem(OverLoadPotID).interact("Deposit-All");
 							   }
 							   }
 						       }
 						      else {
 								   log("Bank: Contains No OverLoad Potions");
 								   stopScript();
 								   }
 						       }
					           
					    	   if (HolyWrench == true) {
								   if (bank.getCount(6714) > 0) {
									   for (int i = 0; i < 13 && inventory.getItem(6714) == null; i++) {
										   status = "Withdrawing: Holy Wrench";
										   bank.withdraw(6714, 1);
										   sleep (random (1200, 1500));   
									   }
								   }
								   else {
									   log("Bank: Contains No Holy Wrench");
									   stopScript();
									   }
							   }
					    	   
					      	   if (Enhanced == true) {
								   if (bank.getCount(14632) > 0) {
									   for (int i = 0; i < 13 && inventory.getItem(14632) == null; i++) {
										   status = "Withdrawing: Enhanced E";
										   bank.withdraw(14632, 1);
										   sleep (random (1200, 1500));   
									   }
								   }
								   else {
									   log("Bank: Contains No Enhanced E");
									   stopScript();
									   }
							   }
					           
							   if (NumberOfPouchID > 0) {
								   
							        if (bank.getCount(SummonPotID) > 0) {
										   for (int i = 0; i < 13 && inventory.getCount(SummonPotID) != 1; i++) {
										   status = "Withdrawing: Summoning Potion (4)";
										   bank.withdraw(SummonPotID, 1);
										   if (inventory.getCount(SummonPotID) > 1) {
											   inventory.getItem(SummonPotID).interact("Deposit-All");
										   }
										   sleep (random (1500, 1750));
										   }
			                               }
							        else {
										   log("Bank: Contains No Summoning Potions (4)");
										   stopScript();
										   }
								   
								   if (bank.getCount(PouchID) > 0) {
									   for (int i = 0; i < 13 && inventory.getCount(PouchID) != NumberOfPouchID; i++) {
									   status = "Withdrawing: Pouch";
									   bank.withdraw(PouchID, NumberOfPouchID);
									   if (inventory.getCount(PouchID) > NumberOfPouchID) {
										   inventory.getItem(PouchID).interact("Deposit-All");
									   }
									   sleep(1500, 1750);
									   }
								   }
								   else {
									   log("Bank: Contains No Pouches");
									   stopScript();
									   }
								   
								   if (bank.getCount(ScrollID) > 0) {						   
									   for (int i = 0; i < 13 && inventory.getItem(ScrollID) == null; i++) {
									   status = "Withdrawing: Scrolls";
									   bank.withdraw(ScrollID, 250);
									   sleep(1500, 1750);
									   }
		                               }
								   else {
									   log("Bank: Contains No Scrolls");
									   stopScript();
									   }
								   }
							   
							   if (NumberOfPrayerRenewalPotID > 0) {
								   if (bank.getCount(PrayerRenewalPotID) > 0) {
									   for (int i = 0; i < 13 && inventory.getCount(PrayerRenewalPotID) != NumberOfPrayerRenewalPotID; i++) {
									   status = "Withdrawing: Prayer Renewal Potion (4)";
									   bank.withdraw(PrayerRenewalPotID, NumberOfPrayerRenewalPotID);
									   sleep (random (1200, 1500));
									   if (inventory.getCount(PrayerRenewalPotID) > NumberOfPrayerRenewalPotID) {
										   inventory.getItem(PrayerRenewalPotID).interact("Deposit-All");
									   }
									   }
								   }
								   else {
									   log("Bank: Contains No Prayer Renewal Potions");
									   stopScript();
								   }
							   }
					           
						       if (NumberOfPrayerPotID > 0) {
								   if (bank.getCount(PrayerPotID) > 0) {
									   for (int i = 0; i < 13  && inventory.getCount(PrayerPotID) != NumberOfPrayerPotID; i++) {
									   status = "Withdrawing: Prayer Potion (4)";
									   bank.withdraw(PrayerPotID, NumberOfPrayerPotID);
									   sleep (random (1200, 1500));
									   if (inventory.getCount(PrayerPotID) > NumberOfPrayerPotID) {
										   inventory.getItem(PrayerPotID).interact("Deposit-All");
									   }
									   }
								   }
								   else {
									   log("Bank: Contains No Prayer Potions");
									   stopScript();
									   }
			 				   }
						       								   
							   if (NumberOfFoodID > 0) {
								   if (bank.getCount(FoodID) > 0) {
									   for (int i = 0; i < 13 && inventory.getCount(FoodID) != NumberOfFoodID; i++) {
									   status = "Withdrawing: Food";
									   bank.withdraw(FoodID, NumberOfFoodID);
									   sleep (random (1200, 1500));

						 			   if (((combat.getLifePoints() * 10) / RealHealth) <= 75 && inventory.contains(FoodID)) {
						 					 for (int i1 = 0; i1 < 13 && combat.getHealth() <= 75 && inventory.contains(FoodID); i1++) {		
						 							if (inventory.getItem(FoodID) != null) {
						 							if (inventory.containsOneOf(FoodID)) {
						 								status = "Eating: Food";
						 								inventory.getItem(FoodID).interact("Eat");
							 					        sleep(500, 800);
						 							}
						 							}
						 					 }
						 			   }
						 			   
									   if (inventory.getCount(FoodID) > NumberOfFoodID) {
										   inventory.getItem(FoodID).interact("Deposit-All");
									   }
									   }
									   }
								   else {
									   log("Bank: Contains No Food");
									   stopScript();
								   }
								   }
 					 }
 			       
				   bank.close();
				   sleep(random(500, 800));
		           return 0;
 				   }
 		   }
 		   
 		  if (interfaces.getComponent(109, 2).isValid()) {
		        for (int i = 0; i < 13 && interfaces.getComponent(109, 2).isValid(); i++) {
		        interfaces.getComponent(109, 14).doClick(true);
	        	sleep(1200, 1500);
		        }
 		  }
 		    
 		   if (inventory.contains(PrayerPotID) && !inventory.contains(CrimCharm)) {	 	
 			         WalkFairy();
				 	 return 0;
 		   }
 		   else {
 					RSObject Bank = objects.getNearest(52589);
					if (Bank != null) {
						if (Bank.isOnScreen()) {
							status = "Banking..";
							interact("Bank", Bank);
							sleep(1000, 1200);
							return 0;
						}
						else if (!Bank.isOnScreen()) {
							camera.turnTo(Bank);
							return 0;
						}
					}
 		   }
 		   }
 		}
	    catch (NullPointerException exception) {}
	    catch (NumberFormatException e){}
	    catch(StackOverflowError t){}	   
       return 0;
 	   }
 	
 	@SuppressWarnings("static-access")
	private void WalkFairy() {
 		
	        int SummonLeft =  Integer.parseInt(interfaces.getComponent(747, 7).getText());
 		   
 			if (SummonLeft == skills.getRealLevel(skills.SUMMONING) || NumberOfPouchID == 0) {
 
			   status = "Walking: Fairy Ring";
			   
			   camera.setPitch(100);
			   camera.setAngle(270);
			   
			   RSPath FairyRingWalk = walking.newTilePath(BankToFairyRing);
			  
		    while (calc.distanceTo(FairyRingWalk.getEnd()) > 7 && game.isLoggedIn()) {	
		 		 status = "Walking: Fairy Ring";
		 	   	 FairyRingWalk.traverse();  	
			     sleep(900, 1200);    	    	        	 
		    }
 			}
 			else {
 				
	        status = "Walking: Obelisk";
			   
			   camera.setPitch(100);
			   camera.setAngle(270);
			   
			   RSPath SummonWalk = walking.newTilePath(BankToSummon);
			  
		    while (calc.distanceTo(SummonWalk.getEnd()) > 7 && game.isLoggedIn()) {	
		 		 status = "Walking: Obelisk";
		 	   	 SummonWalk.traverse();  	
			     sleep(900, 1200);    	    	        	 
		    }
		    
		    RSObject o = objects.getNearest(29959);
		    
		    camera.turnTo(o);
		    camera.setPitch(0);
		    
		 	 if (o != null) {
		  		 if (o.isOnScreen()) {
		  	         for (int i = 0; i < 13 && players.getMyPlayer().getAnimation() != 8502; i++) {
		  	        	 o.interact("Renew-points");
		  	        	 sleep(random(1200, 1500));
		  	         }
		  		 }
		 	 }
		  	         
		  	       status = "Walking: Fairy Ring";
				   
				   camera.setPitch(100);
				   camera.setAngle(270);
				   
				   RSPath FairyRingWalk = walking.newTilePath(BankToFairyRing);
				  
			    while (calc.distanceTo(FairyRingWalk.getEnd()) > 7 && game.isLoggedIn()) {	
			 		 status = "Walking: Fairy Ring";
			 	   	 FairyRingWalk.traverse();  	
				     sleep(900, 1200);
			    }
 			}
 	}
 	
    private RSNPC getInteractingNPC() {
    return (RSNPC) getMyPlayer().getInteracting();
    }
    
    public boolean isQuickPrayerOn() {
        return interfaces.getComponent(749, 0).getTextureID() == 782;
    }
    public boolean setQuickPrayer(boolean activate) {
		int PrayerLeft =  Integer.parseInt(interfaces.getComponent(749, 6).getText());
        if (isQuickPrayerOn() != activate) {
                if (PrayerLeft > 1) {
                        interfaces.getComponent(749, 2).doClick();
                }
        }
        return isQuickPrayerOn() == activate;
    }
    
    @SuppressWarnings("static-access")
	private void CheckPrayer() {
    	
    	int RealHealth = skills.getRealLevel(skills.CONSTITUTION);
    	
    	if (RealHealth > 99) {
    		RealHealth = 99;
    	}

    	if (skills.getRealLevel(skills.PRAYER) >= 92 && SSFlash == true) {
	    	if (((combat.getLifePoints() * 10) / RealHealth) >= 95 && isGroundItemValid(groundItems.getNearest(Drop)) == false) {
	    		
	    		  if (isQuickPrayerOn()) {
	    			  setQuickPrayer(false);
	    			  sleep(900, 1200);
	    		  }
	    	}
	    	else if (((combat.getLifePoints() * 10) / RealHealth) <= 94 && isGroundItemValid(groundItems.getNearest(Drop)) == false) { 
	    		
	    	     if (!isQuickPrayerOn()) {
	    			 setQuickPrayer(true);
	    			 sleep(900, 1200);
	    	     }
	    	}
    	}
    	else if (skills.getRealLevel(skills.PRAYER) >= 92 && SSFlashPrayTab == true) {
    		
    	    if (!isQuickPrayerOn()) {
  			    setQuickPrayer(true);
  			    sleep(900, 1200);
    	    }
    		 
    		 if (((combat.getLifePoints() * 10) / RealHealth) >= 95 && isGroundItemValid(groundItems.getNearest(Drop)) == false && prayer.isPrayerOn(Prayer.Curses.SOUL_SPLIT)) {
    		    	 
    		    	 // Turn SS OFF (PRAY TAB)
    		    	 prayer.setPrayer(Prayer.Curses.SOUL_SPLIT, false);
    		 }
	    	 else if (((combat.getLifePoints() * 10) / RealHealth) <= 94 && isGroundItemValid(groundItems.getNearest(Drop)) == false && !prayer.isPrayerOn(Prayer.Curses.SOUL_SPLIT)) {
	    	    	 
	    	    	 // Turn SS ON (PRAY TAB)
    		    	 prayer.setPrayer(Prayer.Curses.SOUL_SPLIT, true);
	    	 }
    	}
	    else if (skills.getRealLevel(skills.PRAYER) < 92 || SSFlash == false || SSFlashPrayTab == false) {
	         if (!isQuickPrayerOn()) {
  			     setQuickPrayer(true);
  				 sleep(900, 1200);
	         }
	    }
    }
              
 	@SuppressWarnings("static-access")
	private void Eat() {
 		
 	    int RealHealth = skills.getRealLevel(skills.CONSTITUTION);
    	
    	if (RealHealth > 99) {
    		RealHealth = 99;
    	}
    	
 		    if (NumberOfPouchID == 0) {
 		    	
 		    	// Normal
 		    	
 				if (((combat.getLifePoints() * 10) / RealHealth) <= 66 && combat.getSpecialBarEnergy() == 100 && Enhanced == true && CachedWeapon == true) {
	
 	 		 				if (inventory.getItem(14632) != null) {
 	 		 				    for (int i = 0; i < 13 && inventory.getItem(14632) != null; i++) {
 	 		 					inventory.getItem(14632).interact("Wield");
 	 		 					sleep(1500, 2000);
 	 		 				    }
 	 		 				}
 	 		 	 		    if (combat.isSpecialEnabled() == false) {
 	 		 					combat.setSpecialAttack(true);
 	 		 					sleep(1200, 1500);
 	 		 	 		    }
 	 		 				if (inventory.getItem(WornWeapon) != null) {
 	 		 				    for (int i = 0; i < 13 && inventory.getItem(WornWeapon) != null; i++) {
 	 		 	 					inventory.getItem(WornWeapon).interact("Wield");
 	 		 	 					sleep(1500, 2000);
 	 		 				    }
 	 		 				}
 				}
 				else if (((combat.getLifePoints() * 10) / RealHealth) <= 60.6) {
 				
				if (inventory.getItem(FoodID) != null) {
				if (inventory.containsOneOf(FoodID)) {
					status = "Eating: Food";
					inventory.getItem(FoodID).interact("Eat");
					sleep(350);
				}
				}
				else if (inventory.getItem(FoodID) == null && NumberOfFoodID > 0) {
					status = "No Food";
					TripEnded = true;
				}
			}
 		    }
 		    else if (PouchID == 12029) {
 		    	
 		    	// Bunyip
 		    	
 		    int SummonLeft =  Integer.parseInt(interfaces.getComponent(747, 7).getText());
 		    
			if (((combat.getLifePoints() * 10) / RealHealth) <= 66 && combat.getSpecialBarEnergy() == 100 && Enhanced == true && CachedWeapon == true) {
					
		 				if (inventory.getItem(14632) != null) {
		 				    for (int i = 0; i < 13 && inventory.getItem(14632) != null; i++) {
		 					inventory.getItem(14632).interact("Wield");
		 					sleep(1500, 2000);
		 				    }
		 				}
		 				if (combat.isSpecialEnabled() == false) {
	 		 			    combat.setSpecialAttack(true);
	 		 			    sleep(1200, 1500);
		 				}
		 				if (inventory.getItem(WornWeapon) != null) {
		 				    for (int i = 0; i < 13 && inventory.getItem(WornWeapon) != null; i++) {
		 	 					inventory.getItem(WornWeapon).interact("Wield");
		 	 					sleep(1500, 2000);
		 				    }
		 				}
			}
	 	    else if (inventory.contains(RawLob) && skills.getRealLevel(skills.COOKING) >= 40 && inventory.contains(12438) && SummonLeft >= 3 && interfaces.getComponent(747, 0).getTextureID() != 1244 && NoSpecLeft == false) {
 		    	 if (((combat.getLifePoints() * 10) / RealHealth) <= 60.6) {
 		    		status = "Using: Swallow Whole Scroll: Lobster";
                    interfaces.getComponent(747, 0).doClick(true);
                    sleep(500, 800);
                    if (inventory.getItem(RawLob) != null) {
                    	mouse.move(inventory.getItem(RawLob).getComponent().getCenter());
                    	mouse.click(true);
                    	sleep(500, 800);
                    }
 		    	}
 		    }
 		    else if (inventory.contains(RawShark) && skills.getRealLevel(skills.COOKING) >= 80 && inventory.contains(12438) && SummonLeft >= 3 && interfaces.getComponent(747, 0).getTextureID() != 1244 && NoSpecLeft == false) {
 		             if (((combat.getLifePoints() * 10) / RealHealth) <= 60.6) {
		    		 status = "Using: Swallow Whole Scroll: Shark";
	                 interfaces.getComponent(747, 0).doClick(true);
	                 sleep(500, 800);
	                 if (inventory.getItem(RawShark) != null) {
	                    	mouse.move(inventory.getItem(RawShark).getComponent().getCenter());
	                    	mouse.click(true);
	                    	sleep(500, 800);
	                 }
 		             }
 		    }
 		    else if (((combat.getLifePoints() * 10) / RealHealth) <= 60.6 || NoSpecLeft == true) {
				status = "Eating: Food";
				if (inventory.getItem(FoodID) != null) {
					if (inventory.containsOneOf(FoodID)) {
						inventory.getItem(FoodID).interact("Eat");
						sleep(350);
					}
					}
				else if (inventory.getItem(FoodID) == null && NumberOfFoodID > 0) {
					status = "No Food";
					TripEnded = true;
				}
			} 	
 		    }
 		    else if (PouchID == 12039) {
 		    	
 		    	// Unicorn

 			int SummonLeft =  Integer.parseInt(interfaces.getComponent(747, 7).getText());
 			
			if (((combat.getLifePoints() * 10) / RealHealth) <= 66 && combat.getSpecialBarEnergy() == 100 && Enhanced == true && CachedWeapon == true) {
					
		 				if (inventory.getItem(14632) != null) {
		 				    for (int i = 0; i < 13 && inventory.getItem(14632) != null; i++) {
		 					inventory.getItem(14632).interact("Wield");
		 					sleep(1500, 2000);
		 				    }
		 				}
		 				if (combat.isSpecialEnabled() == false) {
	 		 			    combat.setSpecialAttack(true);
	 		 			    sleep(1200, 1500);
		 				}
		 				if (inventory.getItem(WornWeapon) != null) {
		 				    for (int i = 0; i < 13 && inventory.getItem(WornWeapon) != null; i++) {
		 	 					inventory.getItem(WornWeapon).interact("Wield");
		 	 					sleep(1500, 2000);
		 				    }
		 				}
			}
	 		else  if (interfaces.getComponent(747, 0).getTextureID() != 1244 && inventory.contains(12434) && SummonLeft >= 20 && NoSpecLeft == false) {
 		    	if (((combat.getLifePoints() * 10) / RealHealth) <= 60.6) {
 		    	status = "Using: Healing Aura Scroll";
                interfaces.getComponent(747, 0).doClick(true);
                sleep(500, 800);
 		    	}
 		    }
 		    else if (interfaces.getComponent(747, 0).getTextureID() == 1244 || !inventory.contains(12434) || SummonLeft <= 20 || NoSpecLeft == true || combat.getHealth() <= 60.6) {
 		    if (((combat.getLifePoints() * 10) / RealHealth) <= 60.6) {
				status = "Eating: Food";
				if (inventory.getItem(FoodID) != null) {
					if (inventory.containsOneOf(FoodID)) {
						inventory.getItem(FoodID).interact("Eat");
						sleep(350);
					}
					}
				else if (inventory.getItem(FoodID) == null && NumberOfFoodID > 0) {
					status = "No Food";
					TripEnded = true;
				}
 		    }
 		    }
 		    }
 	}
 	
 	private void SpecialAttack() {
 		
 		if (SaraSpec == true) {
 			if (combat.getSpecialBarEnergy() == 100) {
 				if (combat.isSpecialEnabled() == false) {
				    combat.setSpecialAttack(true);
				    sleep(1200, 1500);
 				}
 			}
 		}
 	}
 	
  	@SuppressWarnings("static-access")
    private void DrinkPotsDeg() {
 		   
	          if (NumberOfOverLoadPotID <= 0) {
	    	  
			  int RealAttack = skills.getRealLevel(skills.ATTACK);
	       	  int RealStrength = skills.getRealLevel(skills.STRENGTH);
	      	  int RealDefence = skills.getRealLevel(skills.DEFENSE);
 		    	
	    	  if (skills.getRealLevel(skills.ATTACK) > 99) {
	    		  RealAttack = 99;
	    	  }
	    	  
	    	  if (skills.getRealLevel(skills.STRENGTH) > 99) {
	    		  RealStrength = 99;
	    	  }
	    	  
	    	  if (skills.getRealLevel(skills.DEFENSE) > 99) {
	    		  RealDefence = 99;
	    	  }
	    	  
 			  if (inventory.containsOneOf(AttackPot)) {
				  if (skills.getCurrentLevel(skills.ATTACK) <= RealAttack * 1.1) {
					  if (inventory.getItem(AttackPot) != null) {
						status = "Drinking: Attack Potion";
						if (inventory.getItem(AttackPot).doClick(true))
						sleep(700);
						}
					}
 			    }
 			    
 			    if (inventory.containsOneOf(StrengthPot)) {
 					if (skills.getCurrentLevel(skills.STRENGTH) <= RealStrength * 1.1) {
 						if (inventory.getItem(StrengthPot) != null) {
 							status = "Drinking: Strength Potion";
 							if (inventory.getItem(StrengthPot).doClick(true))
 							sleep(700);
 						}	
 					}
 				}
 			   
			    if (inventory.containsOneOf(DefencePot)) {
			    	if (skills.getCurrentLevel(skills.DEFENSE) <= RealDefence * 1.1) {
					    if (inventory.getItem(DefencePot) != null) {
						status = "Drinking: Defence Potion";
						if (inventory.getItem(DefencePot).doClick(true))
						    sleep(700);
					    }
			    	}
			    }
	          }
 		      else if (NumberOfOverLoadPotID > 0) {
 		    	
 				   if (combat.getHealth() >= 75) {
 					   if (skills.getCurrentLevel(skills.ATTACK) <= skills.getRealLevel(skills.ATTACK) * 1.1) {
 						   if (skills.getCurrentLevel(skills.STRENGTH) <= skills.getRealLevel(skills.STRENGTH) * 1.057) {
 						   	   if (skills.getCurrentLevel(skills.DEFENSE) <= skills.getRealLevel(skills.DEFENSE) * 1.1) {
 								   status = "Drinking: Overload Potion";
 						    	   if (inventory.getItem(OverLoadPot).doClick(true))
 									   sleep(700);
 						   	   }
 						   }
 					   }
 				   }
 		      }
	       
	          if (NumberOfPouchID > 0) {

		      int SummonLeft =  Integer.parseInt(interfaces.getComponent(747, 7).getText());
		
		      if (PouchID == 12029) { 
			      PointsToSummon = 7;
			      SpecialMovePointsNeeded = 3;
		      }
			  else if (PouchID == 12039) { 
			           PointsToSummon = 9;
			           SpecialMovePointsNeeded = 20;
			  }
	       
	          if (inventory.containsOneOf(SummonPot)) {
			      if (SummonLeft <= SpecialMovePointsNeeded || SummonLeft <= PointsToSummon) {
				      if (inventory.getItem(SummonPot) != null) {
					      status = "Drinking: Summoning Potion";
					      inventory.getItem(SummonPot).doClick(true);
					      sleep(700);
				      }
			      }
	          }
	          }
 		 
		      if (inventory.containsOneOf(PrayerRenewalPot)) {
	              if (PrayerRenewalOff == true || FirstTime == true) {	    	
	    	          if (inventory.getItem(PrayerRenewalPot) != null) {
				          status = "Drinking: Prayer Renewal Potion";
				          if (inventory.getItem(PrayerRenewalPot).doClick(true))
				              sleep(700);
	    	          }
	    	          if (PrayerRenewalOff == true) {
	    		          PrayerRenewalOff = false;
	    	          }
	        	      if (FirstTime == true) {
	    	              FirstTime = false;	
	        	      }
	              }
		      }
	    
 		      int PrayerLeft =  Integer.parseInt(interfaces.getComponent(749, 6).getText());
			
			  if (inventory.containsOneOf(PrayerPot)) {
			      if (PrayerLeft <= skills.getRealLevel(skills.PRAYER) / 10 * 65) {
				      status = "Drinking: Prayer Potion";
					  if (inventory.getItem(PrayerPot).doClick(true))
						  sleep(700);
			      }
			  }
			  else if (inventory.getItem(PrayerPot) == null) {
				       TripEnded = true;
			  }
  	}
  	
    private void LootItemDrops() {
 		   
 			if (inventory.isFull()) {
 				
 				if (groundItems.getNearest(Drop) != null) {
 					 					
 				   if (groundItems.getNearest(Drop).getItem().getID() == WaterOrb && !inventory.contains(WaterOrb)) {
 					   
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				   }
 				   else if (groundItems.getNearest(Drop).getItem().getID() == SeaWeed && !inventory.contains(SeaWeed)) {
 					   
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				   }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == SnapeGrass && !inventory.contains(SnapeGrass)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == UncutDiamond && !inventory.contains(UncutDiamond)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == WatermelonSeed && !inventory.contains(WatermelonSeed)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == RanarrSeed && !inventory.contains(RanarrSeed)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == AvantoeSeed && !inventory.contains(AvantoeSeed)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == ToadflaxSeed && !inventory.contains(ToadflaxSeed)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == LantadymeSeed && !inventory.contains(LantadymeSeed)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == DwarfWeedSeed && !inventory.contains(DwarfWeedSeed)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == DeathRune && !inventory.contains(DeathRune)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == BloodRune && !inventory.contains(BloodRune)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == MithrilArrow && !inventory.contains(MithrilArrow)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == SapphireBolts && !inventory.contains(SapphireBolts)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == SteelBar && !inventory.contains(SteelBar)) {
			             if (inventory.containsOneOf(FoodID) || inventory.containsOneOf(Junk)) {
					         if (inventory.containsOneOf(Junk)) {
					             inventory.getItem(Junk).interact("Drop");
					             sleep(random(800, 1000));
					         }
					         else if (inventory.containsOneOf(FoodID)) {
						     inventory.getItem(FoodID).interact("Eat");
						     sleep(random(800, 1000));
					         }
			             }
				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == GoldCharm && !inventory.contains(GoldCharm)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == GreenCharm && !inventory.contains(GreenCharm)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
 							 inventory.getItem(Junk).interact("Drop");
 							 sleep(random(800, 1000));
 	 					     }
 							 else if (inventory.containsOneOf(FoodID)) {
 							 inventory.getItem(FoodID).interact("Eat");
 							 sleep(random(800, 1000));
 							 }
 							 else if (inventory.contains(RawLob) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 							     }
 	  						 else if (inventory.contains(RawShark) ) {
 	  						 inventory.getItem(RawLob).interact("Drop");
 		  				     sleep(random(800, 1000));	 
 	  						 }
 				    }
 				    else if (groundItems.getNearest(Drop).getItem().getID() == CrimCharm && !inventory.contains(CrimCharm)) {
 				    	
 					     if (inventory.containsOneOf(Junk)) {
						 inventory.getItem(Junk).interact("Drop");
						 sleep(random(800, 1000));
 					     }
						 else if (inventory.containsOneOf(FoodID)) {
						 inventory.getItem(FoodID).interact("Eat");
						 sleep(random(800, 1000));
						 }
						 else if (inventory.contains(RawLob) ) {
  						 inventory.getItem(RawLob).interact("Drop");
	  				     sleep(random(800, 1000));	 
						     }
  						 else if (inventory.contains(RawShark) ) {
  						 inventory.getItem(RawLob).interact("Drop");
	  				     sleep(random(800, 1000));	 
  						 }
 				    }
	 			    else if (groundItems.getNearest(Drop).getItem().getID() == BlueCharm && !inventory.contains(BlueCharm)) {
	 			    	
	 				     if (inventory.containsOneOf(Junk)) {
							 inventory.getItem(Junk).interact("Drop");
							 sleep(random(800, 1000));
	 					     }
							 else if (inventory.containsOneOf(FoodID)) {
							 inventory.getItem(FoodID).interact("Eat");
							 sleep(random(800, 1000));
							 }
							 else if (inventory.contains(RawLob) ) {
	  						 inventory.getItem(RawLob).interact("Drop");
		  				     sleep(random(800, 1000));	 
							     }
	  						 else if (inventory.contains(RawShark) ) {
	  						 inventory.getItem(RawLob).interact("Drop");
		  				     sleep(random(800, 1000));	 
	  						 }
	 			    }
	 				else if (groundItems.getNearest(Drop).getItem().getID() != DwarfWeedSeed && groundItems.getNearest(Drop).getItem().getID() != SteelBar && groundItems.getNearest(Drop).getItem().getID() != SapphireBolts && groundItems.getNearest(Drop).getItem().getID() != MithrilArrow && groundItems.getNearest(Drop).getItem().getID() != BloodRune && groundItems.getNearest(Drop).getItem().getID() != DeathRune && groundItems.getNearest(Drop).getItem().getID() != LantadymeSeed && groundItems.getNearest(Drop).getItem().getID() != ToadflaxSeed && groundItems.getNearest(Drop).getItem().getID() != AvantoeSeed && groundItems.getNearest(Drop).getItem().getID() != RanarrSeed && groundItems.getNearest(Drop).getItem().getID() != WatermelonSeed && groundItems.getNearest(Drop).getItem().getID() != UncutDiamond && groundItems.getNearest(Drop).getItem().getID() != SnapeGrass && groundItems.getNearest(Drop).getItem().getID() != SeaWeed && groundItems.getNearest(Drop).getItem().getID() != WaterOrb && groundItems.getNearest(Drop).getItem().getID() != GoldCharm && groundItems.getNearest(Drop).getItem().getID() != GreenCharm && groundItems.getNearest(Drop).getItem().getID() != CrimCharm && groundItems.getNearest(Drop).getItem().getID() != BlueCharm) {
	 					
	 				     if (inventory.containsOneOf(Junk)) {
							 inventory.getItem(Junk).interact("Drop");
							 sleep(random(800, 1000));
	 					     }
							 else if (inventory.containsOneOf(FoodID)) {
							 inventory.getItem(FoodID).interact("Eat");
							 sleep(random(800, 1000));
							 }
							 else if (inventory.contains(RawLob) ) {
	  						 inventory.getItem(RawLob).interact("Drop");
		  				     sleep(random(800, 1000));	 
							     }
	  						 else if (inventory.contains(RawShark) ) {
	  						 inventory.getItem(RawLob).interact("Drop");
		  				     sleep(random(800, 1000));	 
	  						 }
	 				}
 				}
 			}
 			if (!inventory.isFull() || inventory.contains(12158) && groundItems.getNearest(Drop).getItem().getID() == 12158 || inventory.contains(12159) && groundItems.getNearest(Drop).getItem().getID() == 12159 || inventory.contains(12160) && groundItems.getNearest(Drop).getItem().getID() == 12160 || inventory.contains(12163) && groundItems.getNearest(Drop).getItem().getID() == 12163 || inventory.contains(572) && groundItems.getNearest(Drop).getItem().getID() == 572 || inventory.contains(402) && groundItems.getNearest(Drop).getItem().getID() == 402 || inventory.contains(232) && groundItems.getNearest(Drop).getItem().getID() == 232 || inventory.contains(1618) && groundItems.getNearest(Drop).getItem().getID() == 1618 || inventory.contains(5321) && groundItems.getNearest(Drop).getItem().getID() == 5321 || inventory.contains(5295) && groundItems.getNearest(Drop).getItem().getID() == 5295 || inventory.contains(5296) && groundItems.getNearest(Drop).getItem().getID() == 5296 || inventory.contains(5302) && groundItems.getNearest(Drop).getItem().getID() == 5302 || inventory.contains(5303) && groundItems.getNearest(Drop).getItem().getID() == 5303 || inventory.contains(560) && groundItems.getNearest(Drop).getItem().getID() == 560 || inventory.contains(565) && groundItems.getNearest(Drop).getItem().getID() == 565 || inventory.contains(888) && groundItems.getNearest(Drop).getItem().getID() == 888 || inventory.contains(9337) && groundItems.getNearest(Drop).getItem().getID() == 9337 || inventory.contains(2354) && groundItems.getNearest(Drop).getItem().getID() == 2354 || inventory.contains(12158) && groundItems.getNearest(Drop).getItem().getID() == 12158 || inventory.contains(377) && groundItems.getNearest(Drop).getItem().getID() == 377 || inventory.contains(383) && groundItems.getNearest(Drop).getItem().getID() == 383) {
			 				
	        if (isGroundItemValid(groundItems.getNearest(Drop)) != false && !players.getMyPlayer().isMoving()) {
	        	
		        if (groundItems.getNearest(Drop) != null) {
		        	if (groundItems.getNearest(Drop).isOnScreen()) {
		        		status = "Picking Up: " + groundItems.getNearest(Drop).getItem().getName();
		        		for (int i = 0; i < 100 && groundItems.getNearest(Drop) != null && groundItems.getNearest(Drop).isOnScreen(); i++) {	
		        		Eat();	
		        		if (groundItems.getNearest(Drop).interact("Take " + groundItems.getNearest(Drop).getItem().getName()))				            
		        		for (int i2 = 0; i2 < 13 && groundItems.getNearest(Drop) != null; i2++) {	
		        		sleep(50);
			            }
		        	}
		        	}
		        	else if(!groundItems.getNearest(Drop).isOnScreen()) {
		        		for (int i = 0; i < 13 && !players.getMyPlayer().isMoving(); i++) {
		        		walking.walkTileMM(groundItems.getNearest(Drop).getLocation());
		        		}
		        		for (int i = 0; i < 13 && !groundItems.getNearest(Drop).isOnScreen(); i++) {
						       sleep(50);
		        		}
		        		camera.turnTo(groundItems.getNearest(Drop).getLocation());
		        	}
		        }
		        else if (Drop == null) {    
		        }
	        }
 			}
 	   }
    
 	public boolean interact(String action, RSNPC x) {
	        mouse.move(x.getModel().getPoint());
	        if (menu.contains(x.getName()) && !menu.contains(action))
	            return false;
	        else if (menu.isOpen() && !menu.contains(action))
	            mouse.moveRandomly(1000);
	        return menu.contains(action) && menu.doAction(action);
	    }
 	public boolean interact(String action, RSGroundItem x) {
	        mouse.move(x.getPoint());
	        if (menu.contains(x.getItem().getName()) && !menu.contains(action))
	            return false;
	        else if (menu.isOpen() && !menu.contains(action))
	            mouse.moveRandomly(1000);
	        return menu.contains(action) && menu.doAction(action);
	    }
 	public boolean interact(String action, RSObject x) {
 		    mouse.move(x.getModel().getPoint());
	        if (menu.contains(x.getName()) && !menu.contains(action))
	            return false;
	        else if (menu.isOpen() && !menu.contains(action))
	            mouse.moveRandomly(1000);
	        return menu.contains(action) && menu.doAction(action);
	    }
 	
	public boolean isGroundItemValid(RSGroundItem item) {
			try {
			if (item != null) {
				if (calc.distanceBetween(item.getLocation(), players.getMyPlayer().getLocation()) <= 7) {	
				int id = item.getItem().getID();
				if (id != -1) {
					for (RSGroundItem i : groundItems.getAllAt(item.getLocation())) {
						if (i.getItem().getID() == id)
							return true;
					}
				}
			}
			}
			}
			catch (NullPointerException exception) {			
 			}
			return false;
		}
	
    public void getSkillData() {
		        for (int counter = 0; counter <= 24; counter++) {
		            SKILLS_ARRAY[counter] = skills.getCurrentExp(counter);
		        }
		    }
    
	public void highlightTile(final Graphics g, final RSTile t, final Color outline, final Color fill, final String name) {
			final Point pn = calc.tileToScreen(new RSTile(t.getX(), t.getY()), 0, 0, 0);
			final Point px = calc.tileToScreen(new RSTile(t.getX() + 1, t.getY()), 0, 0, 0);
			final Point py = calc.tileToScreen(new RSTile(t.getX(), t.getY() + 1), 0, 0, 0);
			final Point pxy = calc.tileToScreen(new RSTile(t.getX() + 1, t.getY() + 1), 0, 0, 0);
			if (py.x == -1 || pxy.x == -1 || px.x == -1 || pn.x == -1) {
				return;
			}
			g.setColor(outline);
			g.drawPolygon(new int[]{py.x, pxy.x, px.x, pn.x}, new int[]{py.y, pxy.y, px.y, pn.y}, 4);
			g.setColor(fill);
			g.fillPolygon(new int[]{py.x, pxy.x, px.x, pn.x}, new int[]{py.y, pxy.y, px.y, pn.y}, 4);
			g.setColor(new Color(0, 0, 0));
			g.drawString(name, px.x - 5, py.y);
			g.setColor(new Color(255, 255, 255));
			g.drawString(name, px.x - 5 - 1, py.y - 1);
		}
	public void highlightMap(final Graphics g, final RSTile t, final Color outline, final Color fill) {
			g.setColor(fill);
			g.fillRoundRect(calc.tileToMinimap(t).x - 3, calc.tileToMinimap(t).y - 3, 5, 5, 5, 5);
			g.setColor(outline);
			g.drawRoundRect(calc.tileToMinimap(t).x - 3, calc.tileToMinimap(t).y - 3, 5, 5, 5, 5);
		}
	
	 @SuppressWarnings("serial")
    private class MousePathPoint extends Point { 
			   
	                private long finishTime;
	                @SuppressWarnings("unused")
					private double lastingTime;

	                public MousePathPoint(int x, int y, int lastingTime) {
	                        super(x, y);
	                        this.lastingTime = lastingTime;
	                        finishTime = System.currentTimeMillis() + lastingTime - 1500;
	                }

	                public boolean isUp() {
	                        return System.currentTimeMillis() > finishTime;
	                }
		   }   
 	private void drawMouse(Graphics g) {
		    ((Graphics2D) g).setRenderingHints(new RenderingHints(
		            RenderingHints.KEY_ANTIALIASING,
		            RenderingHints.VALUE_ANTIALIAS_ON));
		    Point p = mouse.getLocation();
		    Graphics2D spinG = (Graphics2D) g.create();
		    Graphics2D spinGRev = (Graphics2D) g.create();
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
		}
 	
    private static final Color MOUSE_COLOR = Color.CYAN,
    MOUSE_BORDER_COLOR = new Color(255, 255, 255);
    
			public enum FoodToUse {

			    Monk("Monk", 7946),
			    Shark("Shark", 385),
			    RockTail("Rocktail", 15272);

			    public String name;
			    public int itemId;


			    FoodToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			public enum AttackPotToUse {

			    SuperAttack("Super Attack (4)", 2436),
			    SuperAttackFlask("Super Attack Flask (6)", 23255),
			    
			    ExtremeAttack("Extreme Attack (4)", 15308),
			    ExtremeAttackFlask("Extreme Attack Flask (6)", 23495);

			    public String name;
			    public int itemId;


			    AttackPotToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			public enum StrengthPotToUse {

			    SuperStrength("Super Strength (4)", 2440),
			    SuperStrengthFlask("Super Strength Flask (6)", 23279),
			    
			    ExtremeStrength("Extreme Strength (4)", 15312),
			    ExtremeStrengthFlask("Extreme Strength Flask (6)", 23501);

			    public String name;
			    public int itemId;


			    StrengthPotToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			public enum DefencePotToUse {

			    SuperDefence("Super Defence (4)", 2442),
			    SuperDefenceFlask("Super Defence Flask (6)", 23291),
			    
			    ExtremeDefence("Extreme Defence (4)", 15316),
			    ExtremeDefenceFlask("Extreme Defence Flask (6)", 23507);

			    public String name;
			    public int itemId;


			    DefencePotToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			public enum PrayerPotToUse {

			    Prayer("Prayer (4)", 2434),
			    PrayerFlask("Prayer Flask (6)", 23243),
			    
			    SuperPrayer("Super Prayer (4)", 15328),
			    SuperPrayerFlask("Super Prayer Flask (6)", 23525);

			    public String name;
			    public int itemId;


			    PrayerPotToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			public enum FamaliarToUse {

			    Bunyip("Bunyip", 12029),
			    UnicornStallion("Unicorn Stallion", 12039);

			    public String name;
			    public int itemId;
			    
			    FamaliarToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			public enum PrayerRenewalToUse {

			    PrayerRenewal("Prayer Renewal (4)", 21630),
			    PrayerRenewalFlask("Prayer Renewal Flask (6)", 23609);

			    public String name;
			    public int itemId;
			    
			    PrayerRenewalToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			public enum OverLoadToUse {

			    OverLoad("OverLoad (4)", 15332),
			    OverLoadFlask("OverLoad Flask (6)", 23531);

			    public String name;
			    public int itemId;
			    
			    OverLoadToUse(String name, int itemId) {
			        this.name = name;
			        this.itemId = itemId;
			    }


			    public String getName() {
			        return name;
			    }

			    public int getId() {
			        return itemId;
			    }
			}
			
			@SuppressWarnings("serial")
			public class WaterFiendMagicianGUI extends JFrame {
			    Settings prop = new Settings(getCacheDirectory(), this, account.getName());
			    public WaterFiendMagicianGUI() {
			        initComponents();
			        prop.load();
			        log("Loaded: GUI Settings");
			    }
			    
			    public void OverLoadCont(){
			        if (checkBox1.isSelected()) {
			            textField6.setEnabled(true);
			            comboBox7.setEnabled(true);
			            textField4.setEnabled(false);
			            textField3.setEnabled(false);
			            textField2.setEnabled(false);   
			            textField4.setText("0");
			            textField3.setText("0");
			            textField2.setText("0");
			            comboBox3.setEnabled(false);
			            comboBox2.setEnabled(false);
			            comboBox1.setEnabled(false);
			            label1.setEnabled(false);
			            label2.setEnabled(false);
			            label3.setEnabled(false);
			        } else {
			            textField6.setEnabled(false);
			            textField6.setText("0");
			            comboBox7.setEnabled(false);
			            textField4.setEnabled(true);
			            textField3.setEnabled(true);
			            textField2.setEnabled(true);
			            comboBox3.setEnabled(true);
			            comboBox2.setEnabled(true);
			            comboBox1.setEnabled(true);
			            label1.setEnabled(true);
			            label2.setEnabled(true);
			            label3.setEnabled(true);
			        }
			    }
			    
			    private void OverLoadCheckStateChanged(ChangeEvent e) {
			        OverLoadCont();
			    }
			    
			    private void button1ActionPerformed(ActionEvent e) {
			        
			        if (checkBox2.isSelected()) {
			            SSFlash = true;
			        }   
			        if (checkBox3.isSelected()) {
			            SSFlashPrayTab = true;
			        }        
			        if (checkBox4.isSelected()) {
			            HolyWrench = true;
			        }
			        if (checkBox5.isSelected()) {
			            SaraSpec = true;
			        }
			        if (checkBox6.isSelected()) {
			            Enhanced = true;
			        }
			        
			        if (radioButton1.isSelected()) {
			            DropList.add(12158);
			        }
			        if (radioButton2.isSelected()) {
			            DropList.add(12159);
			        }
			        if (radioButton3.isSelected()) {
			            DropList.add(560);
			        }
			        if (radioButton4.isSelected()) {
			            DropList.add(565);
			        }
			        if (radioButton5.isSelected()) {
			            DropList.add(1444);
			        }
			        if (radioButton6.isSelected()) {
			            DropList.add(1123);
			        }
			        if (radioButton7.isSelected()) {
			            DropList.add(1111);
			        }
			        if (radioButton8.isSelected()) {
			            DropList.add(888);
			        }
			        if (radioButton9.isSelected()) {
			            DropList.add(9337);
			        }
			        if (radioButton10.isSelected()) {
			            DropList.add(385);
			        }
			        if (radioButton11.isSelected()) {
			            DropList.add(1395);
			        }
			        if (radioButton12.isSelected()) {
			            DropList.add(1147);
			        }
			        if (radioButton14.isSelected()) {
			            DropList.add(2354);
			        }
			        if (radioButton15.isSelected()) {
			            DropList.add(402);
			        }
			        if (radioButton16.isSelected()) {
			            DropList.add(232);
			        }
			        if (radioButton17.isSelected()) {
			            DropList.add(5321);
			        }
			        if (radioButton18.isSelected()) {
			            DropList.add(5295);
			        }
			        if (radioButton19.isSelected()) {
			            DropList.add(5302);
			        }
			        if (radioButton20.isSelected()) {
			            DropList.add(5303);
			        }
			        if (radioButton21.isSelected()) {
			            DropList.add(24154);
			        }
			        
			        if(textField2.getText()!= null) {
			            
			            try {
			                NumberOfAttackPotID = Integer.parseInt(textField2.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfAttackPotID >= 0){
			                dispose();
			            }
			        }
			        
			        if(textField3.getText()!= null) {
			            try {
			                NumberOfStrengthPotID = Integer.parseInt(textField3.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfStrengthPotID >= 0){
			                dispose();
			            }
			        }
			        
			        if(textField4.getText()!= null) {
			            try {
			                NumberOfDefencePotID = Integer.parseInt(textField4.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfDefencePotID >= 0){
			                dispose();
			            }
			        }
			        
			        if(textField5.getText()!= null) {
			            try {
			                NumberOfPrayerPotID = Integer.parseInt(textField5.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfPrayerPotID >= 0){
			                dispose();
			            }
			        }
			        
			        if(textField6.getText()!= null) {
			            try {
			                NumberOfOverLoadPotID = Integer.parseInt(textField6.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfOverLoadPotID >= 0){
			                dispose();
			            }
			        }
			        
			        if(textField8.getText()!= null) {
			            try {
			                NumberOfFoodID = Integer.parseInt(textField8.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfFoodID >= 0){
			                dispose();
			            }
			        }
			        
			        if(textField7.getText()!= null) {
			            try {
			                NumberOfPouchID = Integer.parseInt(textField7.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfPouchID >= 0){
			                dispose();
			            }
			        }
			        
			        if(textField9.getText()!= null) {
			            try {
			                NumberOfPrayerRenewalPotID = Integer.parseInt(textField9.getText());
			            }
			            catch (NumberFormatException e1) {
			                JOptionPane.showMessageDialog(null,
			                                              "Please, only use numbers!",
			                                              "WaterFiend Magician",
			                                              JOptionPane.WARNING_MESSAGE);
			            }
			            if(NumberOfPrayerRenewalPotID >= 0){
			                dispose();
			            }
			        }
			        
			        ///////////////// Information /////////////////////
			        
			        for (FoodToUse FoodObjects : FoodToUse.values()){
			            if(comboBox6.getSelectedItem().toString().equals(FoodObjects.getName())){
			                if (NumberOfFoodID > 0) {
			                    FoodID = FoodObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        for (PrayerPotToUse PrayerObjects : PrayerPotToUse.values()){
			            if(comboBox4.getSelectedItem().toString().equals(PrayerObjects.getName())){
			                if (NumberOfPrayerPotID > 0) {
			                    PrayerPotID = PrayerObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        for (FamaliarToUse PouchObjects : FamaliarToUse.values()){
			            if(comboBox5.getSelectedItem().toString().equals(PouchObjects.getName())){
			                if (NumberOfPouchID > 0) {
			                    PouchID = PouchObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        for (AttackPotToUse AttackObjects : AttackPotToUse.values()){
			            if(comboBox1.getSelectedItem().toString().equals(AttackObjects.getName())){
			                if (NumberOfAttackPotID > 0) {
			                    AttackPotID = AttackObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        for (StrengthPotToUse StrengthObjects : StrengthPotToUse.values()){
			            if(comboBox2.getSelectedItem().toString().equals(StrengthObjects.getName())){
			                if (NumberOfStrengthPotID > 0) {
			                    StrengthPotID = StrengthObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        for (DefencePotToUse DefenceObjects : DefencePotToUse.values()){
			            if(comboBox3.getSelectedItem().toString().equals(DefenceObjects.getName())){
			                if (NumberOfDefencePotID > 0) {
			                    DefencePotID = DefenceObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        for (PrayerRenewalToUse PrayerRenewalObjects : PrayerRenewalToUse.values()){
			            if(comboBox8.getSelectedItem().toString().equals(PrayerRenewalObjects.getName())){
			                if (NumberOfPrayerRenewalPotID > 0) {
			                    PrayerRenewalPotID = PrayerRenewalObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        for (OverLoadToUse OverLoadObjects : OverLoadToUse.values()){
			            if(comboBox7.getSelectedItem().toString().equals(OverLoadObjects.getName())){
			                if (NumberOfOverLoadPotID > 0) {
			                    OverLoadPotID = OverLoadObjects.getId();
			                }
			                dispose();
			            }
			        }
			        
			        prop.save();
			        log("Saved: GUI Settings");
			    }

				private void initComponents() {
					// JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
					// Generated using JFormDesigner Evaluation license - Jarno De Lacy
					panel1 = new JPanel();
					tabbedPane1 = new JTabbedPane();
					panel2 = new JPanel();
					button1 = new JButton();
					label10 = new JLabel();
					label11 = new JLabel();
					label12 = new JLabel();
					label13 = new JLabel();
					panel3 = new JPanel();
					label6 = new JLabel();
					textField8 = new JTextField();
					label4 = new JLabel();
					comboBox4 = new JComboBox();
					textField5 = new JTextField();
					label1 = new JLabel();
					label2 = new JLabel();
					label3 = new JLabel();
					textField2 = new JTextField();
					textField3 = new JTextField();
					textField4 = new JTextField();
					comboBox2 = new JComboBox();
					comboBox3 = new JComboBox();
					comboBox1 = new JComboBox();
					comboBox6 = new JComboBox();
					label8 = new JLabel();
					textField9 = new JTextField();
					comboBox8 = new JComboBox();
					checkBox1 = new JCheckBox();
					textField6 = new JTextField();
					comboBox7 = new JComboBox();
					label9 = new JLabel();
					panel4 = new JPanel();
					radioButton1 = new JRadioButton();
					radioButton2 = new JRadioButton();
					radioButton8 = new JRadioButton();
					radioButton3 = new JRadioButton();
					radioButton4 = new JRadioButton();
					radioButton6 = new JRadioButton();
					radioButton14 = new JRadioButton();
					radioButton5 = new JRadioButton();
					radioButton7 = new JRadioButton();
					radioButton10 = new JRadioButton();
					radioButton9 = new JRadioButton();
					radioButton11 = new JRadioButton();
					radioButton12 = new JRadioButton();
					radioButton15 = new JRadioButton();
					radioButton16 = new JRadioButton();
					radioButton17 = new JRadioButton();
					radioButton20 = new JRadioButton();
					radioButton19 = new JRadioButton();
					radioButton18 = new JRadioButton();
					radioButton21 = new JRadioButton();
					label7 = new JLabel();
					panel5 = new JPanel();
					label5 = new JLabel();
					textField7 = new JTextField();
					checkBox2 = new JCheckBox();
					checkBox3 = new JCheckBox();
					checkBox4 = new JCheckBox();
					comboBox5 = new JComboBox();
					checkBox5 = new JCheckBox();
					checkBox6 = new JCheckBox();

					//======== this ========
					setTitle("WaterFiend Magician");
					setFont(new Font("Lithos Pro", Font.PLAIN, 13));
					setResizable(false);
					Container contentPane = getContentPane();

					//======== panel1 ========
					{
			            
						//======== tabbedPane1 ========
						{

							//======== panel2 ========
							{

								//---- button1 ----
								button1.setText("Start");
								button1.setFont(new Font("Lithos Pro", Font.PLAIN, 14));
								button1.addActionListener(new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										button1ActionPerformed(e);
									}
								});

								//---- label10 ----
								label10.setText("WaterFiend Magician");
								label10.setFont(new Font("Lithos Pro", Font.ITALIC, 26));
								label10.setHorizontalAlignment(SwingConstants.CENTER);

								//---- label11 ----
								label11.setText("bY");
								label11.setFont(new Font("Lithos Pro", Font.ITALIC, 26));
								label11.setHorizontalAlignment(SwingConstants.CENTER);

								//---- label12 ----
								label12.setText("magic");
								label12.setFont(new Font("Lithos Pro", Font.ITALIC, 26));
								label12.setHorizontalAlignment(SwingConstants.CENTER);

								//---- label13 ----
								label13.setText("(Donations Are Always Welcome)  :D");
								label13.setFont(new Font("Lithos Pro", Font.ITALIC, 14));
								label13.setHorizontalAlignment(SwingConstants.CENTER);

								GroupLayout panel2Layout = new GroupLayout(panel2);
								panel2.setLayout(panel2Layout);
								panel2Layout.setHorizontalGroup(
									panel2Layout.createParallelGroup()
										.addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
											.addContainerGap()
											.addGroup(panel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(label13, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
												.addComponent(button1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
												.addComponent(label10, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
												.addComponent(label11, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE)
												.addComponent(label12, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 478, Short.MAX_VALUE))
											.addContainerGap())
								);
								panel2Layout.setVerticalGroup(
									panel2Layout.createParallelGroup()
										.addGroup(GroupLayout.Alignment.TRAILING, panel2Layout.createSequentialGroup()
											.addGap(76, 76, 76)
											.addComponent(label10)
											.addGap(18, 18, 18)
											.addComponent(label11)
											.addGap(18, 18, 18)
											.addComponent(label12)
											.addGap(18, 18, 18)
											.addComponent(label13)
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 138, Short.MAX_VALUE)
											.addComponent(button1)
											.addContainerGap())
								);
							}
							tabbedPane1.addTab("Welcome", panel2);


							//======== panel3 ========
							{

								//---- label6 ----
								label6.setText("Food To Take:");
								label6.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- textField8 ----
								textField8.setText("0");

								//---- label4 ----
								label4.setText("Prayer Potion:");
								label4.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- comboBox4 ----
								comboBox4.setModel(new DefaultComboBoxModel(new String[] {
									"Prayer (4)",
									"Super Prayer (4)",
									"Prayer Flask (6)",
									"Super Prayer Flask (6)"
								}));
								comboBox4.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- textField5 ----
								textField5.setText("0");

								//---- label1 ----
								label1.setText("Attack Potion:");
								label1.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- label2 ----
								label2.setText("Strength Potion:");
								label2.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- label3 ----
								label3.setText("Defence Potion:");
								label3.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- textField2 ----
								textField2.setText("0");

								//---- textField3 ----
								textField3.setText("0");

								//---- textField4 ----
								textField4.setText("0");

								//---- comboBox2 ----
								comboBox2.setModel(new DefaultComboBoxModel(new String[] {
									"Super Strength (4)",
									"Extreme Strength (4)",
									"Super Strength Flask (6)",
									"Extreme Strength Flask (6)"
								}));
								comboBox2.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- comboBox3 ----
								comboBox3.setModel(new DefaultComboBoxModel(new String[] {
									"Super Defence (4)",
									"Extreme Defence (4)",
									"Super Defence Flask (6)",
									"Extreme Defence Flask (6)"
								}));
								comboBox3.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- comboBox1 ----
								comboBox1.setModel(new DefaultComboBoxModel(new String[] {
									"Super Attack (4)",
									"Extreme Attack (4)",
									"Super Attack Flask (6)",
									"Extreme Attack Flask (6)"
								}));
								comboBox1.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- comboBox6 ----
								comboBox6.setModel(new DefaultComboBoxModel(new String[] {
									"Monk",
									"Shark",
									"Rocktail"
								}));
								comboBox6.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- label8 ----
								label8.setText("Prayer Renewal:");
								label8.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- textField9 ----
								textField9.setText("0");

								//---- comboBox8 ----
								comboBox8.setModel(new DefaultComboBoxModel(new String[] {
									"Prayer Renewal (4)",
									"Prayer Renewal Flask (6)"
								}));
								comboBox8.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- checkBox1 ----
								checkBox1.setText("OverLoads:");
								checkBox1.setFont(new Font("Lithos Pro", Font.ITALIC, 13));
			                    checkBox1.addChangeListener(new ChangeListener() {
			                        @Override
			                        public void stateChanged(ChangeEvent e) {
			                            OverLoadCheckStateChanged(e);
			                        }
			                    });

								//---- textField6 ----
								textField6.setText("0");
								textField6.setEnabled(false);

								//---- comboBox7 ----
								comboBox7.setModel(new DefaultComboBoxModel(new String[] {
									"OverLoad (4)",
									"OverLoad Flask (6)"
								}));
								comboBox7.setFont(new Font("Lithos Pro", Font.ITALIC, 13));
								comboBox7.setEnabled(false);

								//---- label9 ----
								label9.setText("Food/Potions:");
								label9.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								GroupLayout panel3Layout = new GroupLayout(panel3);
								panel3.setLayout(panel3Layout);
								panel3Layout.setHorizontalGroup(
									panel3Layout.createParallelGroup()
										.addGroup(panel3Layout.createSequentialGroup()
											.addContainerGap()
											.addGroup(panel3Layout.createParallelGroup()
												.addGroup(panel3Layout.createSequentialGroup()
													.addGroup(panel3Layout.createParallelGroup()
														.addGroup(panel3Layout.createSequentialGroup()
															.addGroup(panel3Layout.createParallelGroup()
																.addComponent(label1)
																.addComponent(label2)
																.addComponent(checkBox1)
																.addComponent(label3))
															.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
															.addGroup(panel3Layout.createParallelGroup()
																.addComponent(comboBox7, GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
																.addComponent(comboBox3, 0, 0, Short.MAX_VALUE)
																.addComponent(comboBox1, 0, 297, Short.MAX_VALUE)
																.addComponent(comboBox2, 0, 297, Short.MAX_VALUE)))
														.addGroup(panel3Layout.createSequentialGroup()
															.addGroup(panel3Layout.createParallelGroup()
																.addComponent(label4)
																.addComponent(label6)
																.addComponent(label8))
															.addGap(18, 18, 18)
															.addGroup(panel3Layout.createParallelGroup()
																.addComponent(comboBox6, GroupLayout.Alignment.TRAILING, 0, 0, Short.MAX_VALUE)
																.addComponent(comboBox4, GroupLayout.Alignment.TRAILING, 0, 293, Short.MAX_VALUE)
																.addComponent(comboBox8, 0, 293, Short.MAX_VALUE))))
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
													.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
														.addComponent(textField8, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
														.addComponent(textField5, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
														.addComponent(textField9, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
														.addComponent(textField2, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
														.addComponent(textField3, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
														.addComponent(textField4, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
														.addComponent(textField6, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)))
												.addComponent(label9, GroupLayout.PREFERRED_SIZE, 115, GroupLayout.PREFERRED_SIZE))
											.addContainerGap())
								);
								panel3Layout.setVerticalGroup(
									panel3Layout.createParallelGroup()
										.addGroup(panel3Layout.createSequentialGroup()
											.addContainerGap()
											.addComponent(label9)
											.addGap(9, 9, 9)
											.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(comboBox6, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(label6)
												.addComponent(textField8, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
											.addGap(35, 35, 35)
											.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(comboBox4, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(label4)
												.addComponent(textField5, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(comboBox8, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(textField9, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
												.addComponent(label8))
											.addGap(48, 48, 48)
											.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(comboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
												.addComponent(label1)
												.addComponent(textField2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
											.addGap(18, 18, 18)
											.addGroup(panel3Layout.createParallelGroup()
												.addGroup(panel3Layout.createSequentialGroup()
													.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(comboBox2, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(label2))
													.addGap(18, 18, 18)
													.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(comboBox3, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(label3))
													.addGap(35, 35, 35)
													.addGroup(panel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(comboBox7, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(checkBox1)
														.addComponent(textField6, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
												.addGroup(panel3Layout.createSequentialGroup()
													.addComponent(textField3, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
													.addGap(18, 18, 18)
													.addComponent(textField4, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
											.addContainerGap(32, Short.MAX_VALUE))
								);
							}
							tabbedPane1.addTab("Food/Potions", panel3);


							//======== panel4 ========
							{

								//---- radioButton1 ----
								radioButton1.setText("Gold Charms");
								radioButton1.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton2 ----
								radioButton2.setText("Green Charms");
								radioButton2.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton8 ----
								radioButton8.setText("Mithril  Arrows");
								radioButton8.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton3 ----
								radioButton3.setText("Death Runes");
								radioButton3.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton4 ----
								radioButton4.setText("Blood Runes");
								radioButton4.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton6 ----
								radioButton6.setText("Addy Plate");
								radioButton6.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton14 ----
								radioButton14.setText("Steel Bar");
								radioButton14.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton5 ----
								radioButton5.setText("Water Talisman");
								radioButton5.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton7 ----
								radioButton7.setText("Addy Chain");
								radioButton7.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton10 ----
								radioButton10.setText("Shark ");
								radioButton10.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton9 ----
								radioButton9.setText("Sapphire Bolts");
								radioButton9.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton11 ----
								radioButton11.setText("Water battlestaff");
								radioButton11.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton12 ----
								radioButton12.setText("Rune helm");
								radioButton12.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton15 ----
								radioButton15.setText("Seaweed");
								radioButton15.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton16 ----
								radioButton16.setText("Snape grass");
								radioButton16.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton17 ----
								radioButton17.setText("Watermelon seed");
								radioButton17.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton20 ----
								radioButton20.setText("Dwarf weed seed");
								radioButton20.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton19 ----
								radioButton19.setText("Lantadyme seed");
								radioButton19.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton18 ----
								radioButton18.setText("Ranarr seed");
								radioButton18.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- radioButton21 ----
								radioButton21.setText("Spin Ticket");
								radioButton21.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- label7 ----
								label7.setText("Drops:");
								label7.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								GroupLayout panel4Layout = new GroupLayout(panel4);
								panel4.setLayout(panel4Layout);
								panel4Layout.setHorizontalGroup(
									panel4Layout.createParallelGroup()
										.addGroup(panel4Layout.createSequentialGroup()
											.addContainerGap()
											.addGroup(panel4Layout.createParallelGroup()
												.addComponent(radioButton8)
												.addComponent(radioButton9)
												.addComponent(radioButton3)
												.addComponent(radioButton4)
												.addComponent(radioButton11)
												.addComponent(radioButton15, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
												.addComponent(radioButton17)
												.addComponent(radioButton2)
												.addComponent(radioButton1)
												.addComponent(label7)
												.addComponent(radioButton20, GroupLayout.PREFERRED_SIZE, 155, GroupLayout.PREFERRED_SIZE))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 124, Short.MAX_VALUE)
											.addGroup(panel4Layout.createParallelGroup()
												.addComponent(radioButton18, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
												.addComponent(radioButton19, GroupLayout.PREFERRED_SIZE, 145, GroupLayout.PREFERRED_SIZE)
												.addComponent(radioButton16, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
												.addComponent(radioButton12, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
												.addComponent(radioButton10)
												.addComponent(radioButton14)
												.addComponent(radioButton7)
												.addComponent(radioButton6)
												.addComponent(radioButton21, GroupLayout.PREFERRED_SIZE, 121, GroupLayout.PREFERRED_SIZE)
												.addComponent(radioButton5))
											.addGap(60, 60, 60))
								);
								panel4Layout.setVerticalGroup(
									panel4Layout.createParallelGroup()
										.addGroup(panel4Layout.createSequentialGroup()
											.addContainerGap()
											.addComponent(label7)
											.addGap(15, 15, 15)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton1)
												.addComponent(radioButton5))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton2)
												.addComponent(radioButton21))
											.addGap(18, 18, 18)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton8)
												.addComponent(radioButton6))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton9)
												.addComponent(radioButton7))
											.addGap(18, 18, 18)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton3)
												.addComponent(radioButton14))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton4)
												.addComponent(radioButton10))
											.addGap(18, 18, 18)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton11)
												.addComponent(radioButton12))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton15)
												.addComponent(radioButton16))
											.addGap(18, 18, 18)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton17)
												.addComponent(radioButton19))
											.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
											.addGroup(panel4Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
												.addComponent(radioButton20, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
												.addComponent(radioButton18))
											.addContainerGap(26, Short.MAX_VALUE))
								);
							}
							tabbedPane1.addTab("Drops", panel4);


							//======== panel5 ========
							{

								//---- label5 ----
								label5.setText("Familiar:");
								label5.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- textField7 ----
								textField7.setText("0");

								//---- checkBox2 ----
								checkBox2.setText("SS FLASH (Quick Pray)");
								checkBox2.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- checkBox3 ----
								checkBox3.setText("SS FLASH (Prayer Tab)");
								checkBox3.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- checkBox4 ----
								checkBox4.setText("Holy Wrench");
								checkBox4.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- comboBox5 ----
								comboBox5.setModel(new DefaultComboBoxModel(new String[] {
									"Bunyip",
									"Unicorn Stallion"
								}));
								comboBox5.setFont(new Font("Lithos Pro", Font.PLAIN, 13));

								//---- checkBox5 ----
								checkBox5.setText("Saradomin Sword (Special)");
								checkBox5.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								//---- checkBox6 ----
								checkBox6.setText("enhanced Excalibur");
								checkBox6.setFont(new Font("Lithos Pro", Font.ITALIC, 13));

								GroupLayout panel5Layout = new GroupLayout(panel5);
								panel5.setLayout(panel5Layout);
								panel5Layout.setHorizontalGroup(
									panel5Layout.createParallelGroup()
										.addGroup(panel5Layout.createSequentialGroup()
											.addContainerGap()
											.addGroup(panel5Layout.createParallelGroup()
												.addGroup(panel5Layout.createSequentialGroup()
													.addComponent(label5)
													.addGap(18, 18, 18)
													.addComponent(comboBox5, 0, 320, Short.MAX_VALUE)
													.addGap(18, 18, 18)
													.addComponent(textField7, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE))
												.addComponent(checkBox4)
												.addGroup(GroupLayout.Alignment.TRAILING, panel5Layout.createSequentialGroup()
													.addGroup(panel5Layout.createParallelGroup()
														.addComponent(checkBox2)
														.addComponent(checkBox5))
													.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
													.addGroup(panel5Layout.createParallelGroup()
														.addComponent(checkBox6)
														.addComponent(checkBox3))))
											.addContainerGap())
								);
								panel5Layout.setVerticalGroup(
									panel5Layout.createParallelGroup()
										.addGroup(panel5Layout.createSequentialGroup()
											.addGroup(panel5Layout.createParallelGroup()
												.addGroup(panel5Layout.createSequentialGroup()
													.addGap(16, 16, 16)
													.addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(comboBox5, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
														.addComponent(label5))
													.addGap(28, 28, 28)
													.addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(checkBox2)
														.addComponent(checkBox3))
													.addGap(18, 18, 18)
													.addGroup(panel5Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
														.addComponent(checkBox5)
														.addComponent(checkBox6)))
												.addGroup(panel5Layout.createSequentialGroup()
													.addGap(18, 18, 18)
													.addComponent(textField7, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)))
											.addGap(18, 18, 18)
											.addComponent(checkBox4)
											.addContainerGap(235, Short.MAX_VALUE))
								);
							}
							tabbedPane1.addTab("Extra", panel5);

						}

						GroupLayout panel1Layout = new GroupLayout(panel1);
						panel1.setLayout(panel1Layout);
						panel1Layout.setHorizontalGroup(
							panel1Layout.createParallelGroup()
								.addGroup(panel1Layout.createSequentialGroup()
									.addContainerGap()
									.addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, 495, GroupLayout.PREFERRED_SIZE)
									.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
						);
						panel1Layout.setVerticalGroup(
							panel1Layout.createParallelGroup()
								.addGroup(GroupLayout.Alignment.TRAILING, panel1Layout.createSequentialGroup()
									.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(tabbedPane1, GroupLayout.PREFERRED_SIZE, 443, GroupLayout.PREFERRED_SIZE)
									.addContainerGap())
						);
					}

					GroupLayout contentPaneLayout = new GroupLayout(contentPane);
					contentPane.setLayout(contentPaneLayout);
					contentPaneLayout.setHorizontalGroup(
						contentPaneLayout.createParallelGroup()
							.addGroup(contentPaneLayout.createSequentialGroup()
								.addComponent(panel1, GroupLayout.PREFERRED_SIZE, 492, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(16, Short.MAX_VALUE))
					);
					contentPaneLayout.setVerticalGroup(
						contentPaneLayout.createParallelGroup()
							.addGroup(contentPaneLayout.createSequentialGroup()
								.addComponent(panel1, GroupLayout.PREFERRED_SIZE, 449, GroupLayout.PREFERRED_SIZE)
								.addContainerGap(7, Short.MAX_VALUE))
					);
					pack();
					setLocationRelativeTo(getOwner());
					// JFormDesigner - End of component initialization  //GEN-END:initComponents
				}

				// JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
				// Generated using JFormDesigner Evaluation license - Jarno De Lacy
				private JPanel panel1;
				private JTabbedPane tabbedPane1;
				private JPanel panel2;
				private JButton button1;
				private JLabel label10;
				private JLabel label11;
				private JLabel label12;
				private JLabel label13;
				private JPanel panel3;
				private JLabel label6;
				private JTextField textField8;
				private JLabel label4;
				private JComboBox comboBox4;
				private JTextField textField5;
				private JLabel label1;
				private JLabel label2;
				private JLabel label3;
				private JTextField textField2;
				private JTextField textField3;
				private JTextField textField4;
				private JComboBox comboBox2;
				private JComboBox comboBox3;
				private JComboBox comboBox1;
				private JComboBox comboBox6;
				private JLabel label8;
				private JTextField textField9;
				private JComboBox comboBox8;
				private JCheckBox checkBox1;
				private JTextField textField6;
				private JComboBox comboBox7;
				private JLabel label9;
				private JPanel panel4;
				private JRadioButton radioButton1;
				private JRadioButton radioButton2;
				private JRadioButton radioButton8;
				private JRadioButton radioButton3;
				private JRadioButton radioButton4;
				private JRadioButton radioButton6;
				private JRadioButton radioButton14;
				private JRadioButton radioButton5;
				private JRadioButton radioButton7;
				private JRadioButton radioButton10;
				private JRadioButton radioButton9;
				private JRadioButton radioButton11;
				private JRadioButton radioButton12;
				private JRadioButton radioButton15;
				private JRadioButton radioButton16;
				private JRadioButton radioButton17;
				private JRadioButton radioButton20;
				private JRadioButton radioButton19;
				private JRadioButton radioButton18;
				private JRadioButton radioButton21;
				private JLabel label7;
				private JPanel panel5;
				private JLabel label5;
				private JTextField textField7;
				private JCheckBox checkBox2;
				private JCheckBox checkBox3;
				private JCheckBox checkBox4;
				private JComboBox comboBox5;
				private JCheckBox checkBox5;
				private JCheckBox checkBox6;
				// JFormDesigner - End of variables declaration  //GEN-END:variables
			}
			
			public static class Settings {
				 
	                private File PATH_FILE;
	                private String FILE_NAME = "SETTINGS.ini";
	                private WaterFiendMagicianGUI gui;
	                private final Properties prop = new Properties();
	 
	                public Settings(File f, WaterFiendMagicianGUI gui, String accountName) {
	                        if (!f.exists()) {
	                                f.mkdirs();
	                        }
	                        FILE_NAME = accountName.toUpperCase() + "_" + FILE_NAME;
	                        PATH_FILE = new File(f, FILE_NAME);
	                        this.gui = gui;
	                }
	 
	                public synchronized void save() {
	                        try {
	                                if (!PATH_FILE.exists() && !PATH_FILE.createNewFile()) {
	                                        return;
	                                }
	                                if (!PATH_FILE.canWrite()) {
	                                        PATH_FILE.setWritable(true);
	                                }
	                                prop.clear();
	                                
	                                prop.put("I1", gui.comboBox1.getSelectedItem());
	                                prop.put("T2", gui.textField2.getText());
	                                
	                                prop.put("I2", gui.comboBox2.getSelectedItem());
	                                prop.put("T3", gui.textField3.getText());
	                                
	                                prop.put("I3", gui.comboBox3.getSelectedItem());
	                                prop.put("T4", gui.textField4.getText());
	                                
	                                prop.put("I4", gui.comboBox4.getSelectedItem());
	                                prop.put("T5", gui.textField5.getText());
	                                
	                                prop.put("I7", gui.comboBox7.getSelectedItem());
	                                prop.put("T6", gui.textField6.getText());
	                                
	                                prop.put("I5", gui.comboBox5.getSelectedItem());
	                                prop.put("T7", gui.textField7.getText());
	                                
	                                prop.put("I6", gui.comboBox6.getSelectedItem());
	                                prop.put("T8", gui.textField8.getText());
	                                
	                                prop.put("I8", gui.comboBox8.getSelectedItem());
	                                prop.put("T9", gui.textField9.getText());
	                                
	                                prop.put("c1", String.valueOf(gui.checkBox1.isSelected()));
	                                prop.put("c2", String.valueOf(gui.checkBox2.isSelected()));
	                                prop.put("c3", String.valueOf(gui.checkBox3.isSelected()));
	                                prop.put("c4", String.valueOf(gui.checkBox4.isSelected()));
	                                prop.put("c5", String.valueOf(gui.checkBox5.isSelected()));
	                                prop.put("c6", String.valueOf(gui.checkBox6.isSelected()));
	                                
	                                prop.put("r1", String.valueOf(gui.radioButton1.isSelected()));
	                                prop.put("r2", String.valueOf(gui.radioButton2.isSelected()));
	                                prop.put("r3", String.valueOf(gui.radioButton3.isSelected()));
	                                prop.put("r4", String.valueOf(gui.radioButton4.isSelected()));
	                                prop.put("r5", String.valueOf(gui.radioButton5.isSelected()));
	                                prop.put("r6", String.valueOf(gui.radioButton6.isSelected()));
	                                prop.put("r7", String.valueOf(gui.radioButton7.isSelected()));
	                                prop.put("r8", String.valueOf(gui.radioButton8.isSelected()));
	                                prop.put("r9", String.valueOf(gui.radioButton9.isSelected()));
	                                prop.put("r10", String.valueOf( gui.radioButton10.isSelected()));
	                                prop.put("r11", String.valueOf(gui.radioButton11.isSelected()));
	                                prop.put("r12", String.valueOf( gui.radioButton12.isSelected()));
	                                
	                                prop.put("r14", String.valueOf(gui.radioButton14.isSelected()));
	                                prop.put("r15", String.valueOf(gui.radioButton15.isSelected()));
	                                prop.put("r16", String.valueOf(gui.radioButton16.isSelected()));
	                                prop.put("r17", String.valueOf(gui.radioButton17.isSelected()));
	                                prop.put("r18", String.valueOf(gui.radioButton18.isSelected()));
	                                prop.put("r19", String.valueOf(gui.radioButton19.isSelected()));
	                                prop.put("r20", String.valueOf(gui.radioButton20.isSelected()));
	                                prop.put("r21", String.valueOf(gui.radioButton21.isSelected()));
	      
	                                 prop.store(new FileOutputStream(PATH_FILE), "GUI Settings");
	                                 PATH_FILE.setReadOnly();
	                        } catch (Throwable e) {
	                        }
	                }
	 
	                public synchronized void load() {
	                        try {
	                                if (PATH_FILE.exists()) {
	                                        prop.load(new FileInputStream(PATH_FILE));
	                                        
	                                        gui.comboBox1.setSelectedItem(prop.getProperty("I1"));
	                                        if (prop.getProperty("T2").toString() != "") {
	                                        gui.textField2.setText(prop.getProperty("T2"));
	                                        } 
	                                        
	                                        gui.comboBox2.setSelectedItem(prop.getProperty("I2"));
	                                        if (prop.getProperty("T3").toString() != "") {
	                                        gui.textField3.setText(prop.getProperty("T3"));
	                                        } 
	                                        
	                                        gui.comboBox3.setSelectedItem(prop.getProperty("I3"));
	                                        if (prop.getProperty("T4").toString() != "") {
	                                        gui.textField4.setText(prop.getProperty("T4"));
	                                        } 
	                                        
	                                        gui.comboBox4.setSelectedItem(prop.getProperty("I4"));
	                                        if (prop.getProperty("T5").toString() != "") {
	                                        gui.textField5.setText(prop.getProperty("T5"));
	                                        } 
	                                        
	                                        gui.comboBox7.setSelectedItem(prop.getProperty("I7"));
	                                        if (prop.getProperty("T6").toString() != "") {
	                                        gui.textField6.setText(prop.getProperty("T6"));
	                                        } 
	                                        
	                                        gui.comboBox5.setSelectedItem(prop.getProperty("I5"));
	                                        if (prop.getProperty("T7").toString() != "") {
	                                        gui.textField7.setText(prop.getProperty("T7"));
	                                        }
	                                        
	                                        gui.comboBox6.setSelectedItem(prop.getProperty("I6"));
	                                        if (prop.getProperty("T8").toString() != "") {
	                                        gui.textField8.setText(prop.getProperty("T8"));
	                                        } 
	                                        
	                                        gui.comboBox8.setSelectedItem(prop.getProperty("I8"));
	                                        if (prop.getProperty("T9").toString() != "") {
	                                        gui.textField9.setText(prop.getProperty("T9"));
	                                        } 
	                                        
	                                        gui.checkBox1.setSelected(Boolean.valueOf(prop.getProperty("c1", "false")));
	                                        gui.checkBox2.setSelected(Boolean.valueOf(prop.getProperty("c2", "false")));
	                                        gui.checkBox3.setSelected(Boolean.valueOf(prop.getProperty("c3", "false")));
	                                        gui.checkBox4.setSelected(Boolean.valueOf(prop.getProperty("c4", "false")));
	                                        gui.checkBox5.setSelected(Boolean.valueOf(prop.getProperty("c5", "false")));
	                                        gui.checkBox6.setSelected(Boolean.valueOf(prop.getProperty("c6", "false")));
	                                        
	                                        gui.radioButton1.setSelected(Boolean.valueOf(prop.getProperty("r1", "false")));
	                                        gui.radioButton2.setSelected(Boolean.valueOf(prop.getProperty("r2", "false")));
	                                        gui.radioButton3.setSelected(Boolean.valueOf(prop.getProperty("r3", "false")));
	                                        gui.radioButton4.setSelected(Boolean.valueOf(prop.getProperty("r4", "false")));
	                                        gui.radioButton5.setSelected(Boolean.valueOf(prop.getProperty("r5", "false")));
	                                        gui.radioButton6.setSelected(Boolean.valueOf(prop.getProperty("r6", "false")));
	                                        gui.radioButton7.setSelected(Boolean.valueOf(prop.getProperty("r7", "false")));
	                                        gui.radioButton8.setSelected(Boolean.valueOf(prop.getProperty("r8", "false")));
	                                        gui.radioButton9.setSelected(Boolean.valueOf(prop.getProperty("r9", "false")));
	                                        gui.radioButton10.setSelected(Boolean.valueOf(prop.getProperty("r10", "false")));
	                                        gui.radioButton11.setSelected(Boolean.valueOf(prop.getProperty("r11", "false")));
	                                        gui.radioButton12.setSelected(Boolean.valueOf(prop.getProperty("r12", "false")));
	                                        
	                                        gui.radioButton14.setSelected(Boolean.valueOf(prop.getProperty("r14", "false")));
	                                        gui.radioButton15.setSelected(Boolean.valueOf(prop.getProperty("r15", "false")));
	                                        gui.radioButton16.setSelected(Boolean.valueOf(prop.getProperty("r16", "false")));
	                                        gui.radioButton17.setSelected(Boolean.valueOf(prop.getProperty("r17", "false")));
	                                        gui.radioButton18.setSelected(Boolean.valueOf(prop.getProperty("r18", "false")));
	                                        gui.radioButton19.setSelected(Boolean.valueOf(prop.getProperty("r19", "false")));
	                                        gui.radioButton20.setSelected(Boolean.valueOf(prop.getProperty("r20", "false")));
	                                        gui.radioButton21.setSelected(Boolean.valueOf(prop.getProperty("r21", "false")));    
	                                }
	                        } catch (Throwable e) {
	                        }
	                }
	                 }
			
		    @Override
            public void mouseClicked(MouseEvent e) {
            
                p = e.getPoint();
                
                if(close.contains(p) && !hide){
                    hide = true;
                }
                else if(close.contains(p) && hide){
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
            public void mousePressed(MouseEvent e) {
            }
            @Override
            public void mouseReleased(MouseEvent e) {     
            }
            @Override
            public void messageReceived(MessageEvent m) {
            	
            	String x1 = m.getMessage().toString();
             	String x2 = m.getMessage().toString();
     		    
     		    if (x1.contains("Your special move bar is too low to use this scroll")) {
     		    NoSpecLeft = true;
     		    }
     		    if (x2.contains("Your prayer renewal has ended")) {
        	    PrayerRenewalOff = true;
     		    }
            }
            
            @SuppressWarnings("static-access")
		    @Override
	 	    public void onRepaint(Graphics g1) {
			Graphics2D g = (Graphics2D)g1;
			
		    if(!hide){
			
			long millis = System.currentTimeMillis() - startTime;
			long hours = millis / (1000 * 60 * 60);
			millis -= hours * (1000 * 60 * 60);
			long minutes = millis / (1000 * 60);
			millis -= minutes * (1000 * 60);
			long seconds = millis / 1000;
					      			     		                    		
			CharmsCollectedHour = (int) ((CharmsCollected) * 3600000D / (System.currentTimeMillis() - startTime));
			CharmsCollectedHourBlue = (int) ((CharmsCollectedBlue) * 3600000D / (System.currentTimeMillis() - startTime));
			npcCountHour = (int) ((npcCount) * 3600000D / (System.currentTimeMillis() - startTime));
						
			  int counter = 0;
		      int visible = 0;
		      
		      for (int i : SKILLS_ARRAY) {
                  if (skills.getCurrentExp(counter) > i) {
                	                  	  
                    int XP_CHANGE = (skills.getCurrentExp(counter) - i);   
                    
                	int percent = skills.getPercentToNextLevel(counter);
            		int length = (percent * 505) / 100;
            		
            		int r = skills.getRealLevel(counter);
            		
            		if (skills.getRealLevel(counter) >= 99) {
            	    r = 99;
            	    length = (100 * 505) / 100; 
            		}

    		        g.setColor(color4);
    		        g.fillRect(7, (318 - visible * 18), length, 16);
    		        g.setColor(color5);
    		        g.fillRect(7, (318 - visible * 18), length, 16); 
    		        
    		        g.setColor(color6);
    		        g.setStroke(stroke1);
    		        g.drawRect(7, (318 - visible * 18), 505, 16);
    		        
          			g.setFont(font2);
        			g.setColor(color7);
                    g.drawString("[" + Skills.getSkillName(counter).replaceFirst(Skills.getSkillName(counter).substring(0, 1), Skills.getSkillName(counter).substring(0, 1).toUpperCase()) + "] [ " + r + " ] | " + "XP: "  + XP_CHANGE / 1000 + "k | " + "XP/Hour: " + (int) (XP_CHANGE * 3600000D / 1000 / (System.currentTimeMillis() - startTime)) + "k " + "|", 11, (330 - visible * 18));
                    visible++;
                  }
                    counter++;
              }
		      
			    g.drawImage(img1, 3, 269, null);	    
			    g.drawImage(img2, 7, 234, null);
			    g.drawImage(img3, 7, 228, null);
			    g.drawImage(img4, 8, 225, null);
			    
				g.setFont(font1);
				g.setColor(color8);
				
				g.drawString(hours + " : " + minutes + " : " + seconds + " (Kills: " + npcCount + " (" + npcCountHour + "/ hr))", 59, 400); 
				g.drawString(CharmsCollected + " (" + CharmsCollectedHour + "/ hr)", 59, 430);
		        g.drawString(CharmsCollectedBlue + " (" + CharmsCollectedHourBlue + "/ hr)", 59, 460);
		        
		    }
		    
		       if(hide){
		       g.drawImage(img5, 494, 344, null);
		       }
					     	      
			  	g.setColor(color1);
				drawMouse(g);
				g.setFont(font2);

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
                        	
                        	int Ran = random(1, 70);
                        	
                        	if (Ran >= 1 && Ran <= 10) {
                                g.setColor(Color.RED);
                        	}
                        	else if (Ran > 10 && Ran <= 20) {
                        		 g.setColor(Color.ORANGE);	
                        	}
                        	else if (Ran > 20 && Ran <= 30) {
                       		 g.setColor(Color.YELLOW);	
                        	}
                        	else if (Ran > 30 && Ran <= 40) {
                       		 g.setColor(Color.GREEN);	
                        	}
                        	else if (Ran > 50 && Ran <= 60) {
                       		 g.setColor(Color.BLUE);	
                        	}
                        	else if (Ran > 60 && Ran <= 70) {
                       		 g.setColor(Color.MAGENTA);	
                        	}
                                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y); 
                        }
                        lastPoint = a;
                }
						      			
			    if (isGroundItemValid(groundItems.getNearest(Drop)) != false) {	
		        RSTile RevTile = groundItems.getNearest(Drop).getLocation();
		        highlightTile(g, RevTile, Color.blue, new Color(0, 0, 0, 125), groundItems.getNearest(Drop).getItem().getName());
		        highlightMap(g, RevTile, Color.blue, new Color(0, 0, 0, 125));	
		        }
		        
			    if (npcs.getNearest(WaterFiends) != null && players.getMyPlayer().getInteracting() == null) {	
			    RSTile RevTile = npcs.getNearest(WaterFiends).getLocation();
		        highlightTile(g, RevTile, Color.orange, new Color(0, 0, 0, 125), npcs.getNearest(WaterFiends).getName());
		        highlightMap(g, RevTile, Color.orange, new Color(0, 0, 0, 125));
			    }
			    
			    if (npcs.getNearest(WaterFiends) != null && players.getMyPlayer().getInteracting() != null) {
				RSTile RevTile = players.getMyPlayer().getInteracting().getLocation();
			    highlightTile(g, RevTile, Color.green, new Color(0, 0, 0, 125), players.getMyPlayer().getInteracting().getName());
			    highlightMap(g, RevTile, Color.green, new Color(0, 0, 0, 125));
			    }
			    
			    if (groundItems.getNearest(RawLob) != null && skills.getRealLevel(skills.COOKING) >= 40 && !inventory.isFull() && PouchID == 12029) {
				RSTile RevTile = groundItems.getNearest(RawLob).getLocation();
			    highlightTile(g, RevTile, Color.MAGENTA, new Color(0, 0, 0, 125), groundItems.getNearest(RawLob).getItem().getName());
			    highlightMap(g, RevTile, Color.MAGENTA, new Color(0, 0, 0, 125));
			    }
			    
			    if (groundItems.getNearest(RawShark) != null && skills.getRealLevel(skills.COOKING) >= 80 && !inventory.isFull() && PouchID == 12029) {
			    RSTile RevTile = groundItems.getNearest(RawShark).getLocation();
				highlightTile(g, RevTile, Color.MAGENTA, new Color(0, 0, 0, 125), groundItems.getNearest(RawShark).getItem().getName());
				highlightMap(g, RevTile, Color.MAGENTA, new Color(0, 0, 0, 125));
			    }
            }   }
