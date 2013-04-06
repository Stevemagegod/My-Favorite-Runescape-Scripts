import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.TreeMap;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.buddy.net.GeItem;
import org.buddy.tabs.Inventory;
import org.buddy.widgets.Bank;
import com.buddy.event.listeners.PaintListener;
import com.buddy.script.ActiveScript;
import com.buddy.script.Manifest;
import com.buddy.script.methods.Calculations;
import com.buddy.script.methods.Environment;
import com.buddy.script.methods.GroundItems;
import com.buddy.script.methods.Menu;
import com.buddy.script.methods.Mouse;
import com.buddy.script.methods.Objects;
import com.buddy.script.methods.Players;
import com.buddy.script.methods.Walking;
import com.buddy.script.methods.Widgets;
import com.buddy.script.task.Task;
import com.buddy.script.util.Filter;
import com.buddy.script.util.Random;
import com.buddy.script.util.Timer;
import com.buddy.script.wrappers.Component;
import com.buddy.script.wrappers.GameObject;
import com.buddy.script.wrappers.GroundItem;
import com.buddy.script.wrappers.HintArrow;
import com.buddy.script.wrappers.Item;
import com.buddy.script.wrappers.LocalPath;
import com.buddy.script.wrappers.Player;
import com.buddy.script.wrappers.Tile;
import com.buddy.script.wrappers.TilePath;
import com.buddy.script.wrappers.Path.TraversalOption;
/*
* _cWildyRipper
*/
@Manifest(authors={"_Clash"},
  name="_cWildyRipper",
  keywords={"wildy, loot, cash, free, fast, level, 3"},
  version=0.04,
  description="Based on auswildy idea.")
 
  /*
   * V0.05 (13 - 10 - 11 / 16 - 10 - 11)
   *   Made toggleable, get closest or most expensivest.
   *   Added bank cleaning, aka selling to store!
   *   Properly fixed high risk login.
   *   Cleaned up script.
   *   Added ingore option for certain items.
   *   Fixed eat function, now fast and instant like it should be.
   *   Fixed nullpointer error (rare)
   *   Finalized loot function, ubber effency.
   * V0.04
   *   Fixed major pile looting, in speed mode. (unique feature)
   *   When under attack, and we can't eat we go bank!
   * V0.03
   *   Script source was posted.
   * V0.02
   *   Added HighRiskWorld logging !
   *   Fixed bank tile, now always should be capable to bank
   *   Added inWilderniss check incase we would die <img src='http://powerbot-gold4rs.netdna-ssl.com/community//public/style_emoticons/<#EMO_DIR#>/wink.png' class='bbc_emoticon' alt=';)' />
   *   Doubled crossing times, should be more effiecient in most cases.
   *   Made banking way faster !
   *   Added properly saving of TreeMap, faster starting and also gives you chance to see most expensive item ever seen with script.
   *   Optimized running usage.
   * V0.01
   *   Script base is written.
   */
 
public class _cWildyRipper extends ActiveScript implements PaintListener, MouseListener {
private static String sStatus = "Start";

private final static String[] ingoreThese = new String[] { "bronze", "iron", "steel", "bones", "vial" , "trout", "tuna" };

private static TreeMap<Integer, Integer> pricesMap = new TreeMap<Integer, Integer>();
private static TreeMap<Integer, Integer> pricesMapSave = new TreeMap<Integer, Integer>();

private static int priceCheck = 125,
    iCase,
	  highRiskLogins = 0;

private static boolean bThreadStop = false,
		 bankForEscape = false;

    private static Tile nearTile = null;
   
private Timer startTime;
private static PrintStream az;

public static Filter<GroundItem> itemFilter = new Filter<GroundItem>() {
  @Override
  public boolean accept(GroundItem g) {
   if(g.getItem() != null && g.getItem().hasDefinition()) {
    for(String s : ingoreThese)
	 if(g.getItem().getName().toLowerCase().contains(s.toLowerCase()))
	  return false;	 
    int cF = g.getItem().getId();
    return g.getLocation().getY() > 3521 && (pricesMap.get(cF) != null ? pricesMap.get(cF) * g.getItem().getStackSize() > priceCheck : false);
   }
   return false;
  }
};

/**
  *
  * Comperator classes.
  */
public final static class PriceComparator implements Comparator<GroundItem> {
	 public final int compare(final GroundItem g1, final GroundItem g2) {
		 return pricesMap.get(g2.getItem().getId()) - pricesMap.get(g1.getItem().getId());
	 }
}

private final static class GroundItemComparator implements Comparator<GroundItem> {
	    public final int compare(final GroundItem n1, final GroundItem n2) {
		    return Calculations.distanceTo(n1) - Calculations.distanceTo(n2);
	    }
}

private final static GroundItem getMostExpensive() {
  if(pricesMap.size() != 0) {
	  final GroundItem[] loaded = GroundItems.getLoaded(14, itemFilter);	 
	  if(loaded.length > 0) { 
    //if(bSortDistance) {	
    // Arrays.sort(loaded, new GroundItemComparator());
    /*} else */
	   //Arrays.sort(loaded, new PriceComparator());
	   Arrays.sort(loaded, new GroundItemComparator());
    if(pricesMap.get(loaded[0].getItem().getId()) >= 2500
	 && loaded[0].getLocation().distanceTo(myPlayer().getLocation()) <= 10) {
	 //log("trying to loot an 2.5K + item!");
	 return loaded[0];
    }
    return loaded[0];
	  }
  }
	 return null;
}

protected final Item getDropableItem(final String[] sItemNamesContains) {
  final Item[] iA = Inventory.getItems();
  for(final Item i : iA)
   for(final String rI :sItemNamesContains)
    if(i.getName().toLowerCase().contains(rI.toLowerCase()))
	 return i;
  return null;
}

private static boolean ss = false;
private final static void priceMonitor() {
  new Thread(new Runnable() {
   public void run() {
    sStatus = "[PM] Thread started";
    while(!bThreadStop) { 
	 Component t = Widgets.get(906).getComponent(105);
	 if(t != null && t.isValid() && t.isVisible()) {
	  t.click();
	  highRiskLogins++;
	 } else if(Players.getLocal().getLocation().getX() < 100 && Players.getLocal().getLocation().getY() < 100) {
	  _cWildyRipper.clickMouse(new Rectangle(281, 442, 202, 37), true, "");
	 }
	 //Component at = Widgets.getComponent(906).getComponent(184);
	 // if(at != null && at.isValid() && at.isVisible()) {
	 //  _cWildyRipper.clickMouse(at.getBoundingRect(), true, "");
	 //  at.click();
	 // }
	 //lootMe = getMostExpensive();
	 if(ss = true) {
	  Inventory.getAllItems();
	 }
	 //inventoryMonitor();
	 //if(iCase == 0)
	  for(GroundItem item : GroundItems.getLoaded())
	   if(item != null)  //NullFix
	    if(pricesMap.get(item.getItem().getId()) == null) {//If null we don't have it.
		 try {
		  int a = item.getItem().getId();
		  int b = GeItem.lookup(item.getItem().getId()).getGuidePrice();
		  sStatus = "NIL : " + GeItem.lookup(item.getItem().getId()).getName() + ", $$ " + b;	   
		  pricesMap.put(a,  b);
		  pricesMapSave.put(a,  b);
		  sStatus = "New item loaded : " + GeItem.lookup(item.getItem().getId()).getName() + "ID"+  a + ", @ price :" + b;
		  az.println(a + "," + b);		  
		 } catch(NullPointerException e) {		
		 }
	    } else try {
		  Thread.sleep(100); //Save CPU.
	    } catch (InterruptedException e) {}	  
	  }
    az.close();
    sStatus = "[PM] Thread finished.";
   }
  }).start();
}

static Item[] itemsCache = null;
static Item[] currentCache;
private static TreeMap<Item, Integer> stackSizeMap = new TreeMap<Item, Integer>();

private final void inventoryMonitor() {
  itemsCache = Inventory.getItems();
  for(Item t : itemsCache) {
   int pM = pricesMap.get(t.getId());
   if(pM != -1) {
    int sS = t.getStackSize();
    if(sS == 1)
	 profit += pM;
    else {
	 sStatus = "Calc = " + pM * (stackSizeMap.get(t) != null ? sS - stackSizeMap.get(t) : sS);
	 profit += pM * (stackSizeMap.get(t) != null ? sS - stackSizeMap.get(t) : sS);   
	 stackSizeMap.put(t, t.getStackSize());
    }   
   }
  }
  currentCache = itemsCache;
}

public boolean onstart() {
  iCase = 0;
  try {
   BufferedReader ab = new BufferedReader(new FileReader("ID.txt"));
	    String line;
   line = ab.readLine();
   while (line != null) {
    String[] tmp = line.split(",");
    if(tmp.length != -1) {
	 pricesMap.put(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));	
	 pricesMapSave.put(Integer.parseInt(tmp[0]), Integer.parseInt(tmp[1]));
	 line = ab.readLine();
    }
	   }
    ab.close();
  } catch (IOException e1) {
  } catch (NumberFormatException e2) {
   log("Cache got cleared somehow.");
  }
  try {
   if(new File("ID.txt").exists()) {
    log("File already exists <img src='http://powerbot-gold4rs.netdna-ssl.com/community//public/style_emoticons/<#EMO_DIR#>/happy.png' class='bbc_emoticon' alt=':)' />");
    az = new PrintStream(new File ("ID.txt"));
   } else az = new PrintStream(new FileOutputStream("ID.txt"), true);
  } catch (FileNotFoundException e) {
  }
  while(!pricesMapSave.isEmpty()) {  
   sStatus = "Redumping txt." ;
   az.println(pricesMapSave.firstEntry().getKey() + "," + pricesMapSave.firstEntry().getValue());
   pricesMapSave.remove(pricesMapSave.firstEntry().getKey());
  }
  priceMonitor();
  Mouse.setSpeed(0);
  startTime = new Timer(0);
  Environment.disableRandom("Login");
  return true;
}

public void onfinish() {
  az.close();
  bThreadStop = true;
  sleep(5000);
}

private final static boolean clickMouse(final Rectangle x, final boolean leftMouse, final String text) {
  return clickMouse(x, leftMouse, text, false);
}

private final static boolean moveMouse(final Rectangle x, final boolean alwaysMoveMouse) {
  if(!alwaysMoveMouse && (Mouse.getLocation().x >= x.getMinX() &&  Mouse.getLocation().x <= x.getMaxX()) &&
    (Mouse.getLocation().y >= x.getMinY() &&  Mouse.getLocation().y <= x.getMaxY())) {
   return true;
  } else {  
   Mouse.move(new Point(Random.nextInt((int) x.getMinX(), (int) x.getMaxX()), Random.nextInt((int) x.getMinY(), (int) x.getMaxY())));
   if((Mouse.getLocation().x >= x.getMinX() &&  Mouse.getLocation().x <= x.getMaxX()) &&
	 (Mouse.getLocation().y >= x.getMinY() &&  Mouse.getLocation().y <= x.getMaxY()))
    return true;  
  }
  return false;
}

private final static boolean clickMouse(final Rectangle x, final boolean leftMouse, final String text, final boolean alwaysMoveMouse) {
  try {
   if(x != null) {
    moveMouse(x, alwaysMoveMouse);
    String[] t = Menu.getItems();
    if(t != null && t[0] != null && t[0].toLowerCase().contains(text.toLowerCase())) {
	 Mouse.click(leftMouse);
	 return true;
    } else return Menu.click(text);
   }
   return false;
  } catch(ArrayIndexOutOfBoundsException e) {
   return false;
  }
}

    protected final boolean moveMouseOffMenu() {
	 if(Menu.isOpen()) {
	 Point b = Menu.getLocation();
		   Mouse.move(Random.nextInt(0,2) == 0 ?
			 new Point(b.x + Random.nextInt(-20,-50), b.y + Random.nextInt(-20,-50)) : 
			  new Point(b.x + (b.x+218) + Random.nextInt(+20,+50), b.y + (b.y + Menu.getSize()*17) + Random.nextInt(+20,+50)));
	   }
    return(!Menu.isOpen());
   }

    int failSafe = 0;
   
protected final int playersOnTile(final Tile t) {
  final Player[] p = Players.getLoaded();
  int returnInt = 0;
  for(Player a : p)
   if(a.getLocation().equals(t))
    returnInt++;
  return returnInt;
}

private final boolean atMenuCurrentPosition(final String atMenu) {
  int idx;
  if(failSafe == 3 || lootMe == null || !lootMe.getItem().hasDefinition()) {
   failSafe = 0;
   lootMe = null;
   log("Falled back on failSafe Counter, item gone");
   return false;
  }
  if(Menu.isOpen()) {
   idx = Menu.getIndex(atMenu);
   if(idx == -1) {
    log("Closing menu by moving offscreen");
    moveMouseOffMenu();
    Mouse.move(lootMe.getInteractPoint());
    failSafe++;
    if(lootMe != null && lootMe.getItem().hasDefinition()) {
	 return atMenuCurrentPosition(atMenu);
    } else {
	 log("Lootme is looted!");
	 return false;	
    }
    //idx = Menu.getIndex("Cancel");
    //return false;
   }
   //Mouse.move(Random.nextInt((Mouse.getLocation().x - 10), (Mouse.getLocation().x + 40)), (Menu.getLocation().y + ((idx * 16) + Random.nextInt(21, 36))));
   //Mouse.click(true); 
   //return true;
  }
  if(!Menu.isOpen())
   Mouse.click(false);
	 if(Menu.isOpen()) {
	  if(Menu.getActions().length >= 30) { //Loot fix bitch
	   log("REAL LOOT FIX");
	   int mx = playersOnTile(lootMe.getLocation());
	   idx = (Menu.getIndex("Take " + lootMe.getItem().getName()) +  ((mx == 0 ? 0 : mx * 4) / 2)) / 2 + 1;
   } else idx = Menu.getIndex(atMenu);
	  if(idx == -1) {
	   lootMe = getMostExpensive(); //Incase item is gone <img src='http://powerbot-gold4rs.netdna-ssl.com/community//public/style_emoticons/<#EMO_DIR#>/wink.png' class='bbc_emoticon' alt=';)' />
	   return(lootMe != null ? (moveMouse(new Rectangle(lootMe.getInteractPoint().x, lootMe.getInteractPoint().y, 0, 0), false) ? atMenuCurrentPosition(atMenu) : false) : false);
	  }
	  Mouse.move(Random.nextInt((Mouse.getLocation().x - 10), (Mouse.getLocation().x + 40)), (Menu.getLocation().y + ((idx * 16) + Random.nextInt(21, 36))));
	  Mouse.click(true);
	  if(Menu.isOpen() && !Menu.contains(atMenu)) {		 
		  lootMe = getMostExpensive(); //Incase item is gone <img src='http://powerbot-gold4rs.netdna-ssl.com/community//public/style_emoticons/<#EMO_DIR#>/wink.png' class='bbc_emoticon' alt=';)' />
		  if(lootMe != null && lootMe.getItem().hasDefinition())
		   idx = Menu.getIndex("Take " + lootMe.getItem().getName());
		  if(idx == -1) {
		   log("Lood speedup failed other looters got it");
		   idx = Menu.getIndex("Cancel");
		   Mouse.move(Random.nextInt((Mouse.getLocation().x - 10), (Mouse.getLocation().x + 40)), (Menu.getLocation().y + ((idx * 16) + Random.nextInt(21, 36))));
		   Mouse.click(true);
		   return false;
		  }
	   Mouse.move(Random.nextInt((Mouse.getLocation().x - 10), (Mouse.getLocation().x + 40)), (Menu.getLocation().y + ((idx * 16) + Random.nextInt(21, 36))));
	   Mouse.click(true);
	  }
	  return true;
	 }
  return false;
}

    private final static Tile getNext(final Tile[] tiles) {
		    for (int i = tiles.length - 1; i > 0; i -= 1)
				    if (tiles[i].isOnMap())
						    return tiles[i];
		    return null;
    }
    private final static boolean walkTo(final Tile tile) {
	  try {
			 final LocalPath localPath = (LocalPath) Walking.findPath(tile);
			 if (localPath == null || localPath.getNext() == null) {
					 return false;
			 }
			 final TilePath tilePath = localPath.getCurrentTilePath();
			 if (tilePath == null || tilePath.getNext() == null) {
					 return false;
			 }
			 final Tile[] tiles = tilePath.toArray();
			 if (tiles == null) {
					 return false;
			 }
			 final Tile next = getNext(tiles);
			 if (next == null) {
					 return false;
			 }
			 nearTile = next;
			 return Walking.findPath(next).traverse(
							 EnumSet.of(TraversalOption.HANDLE_RUN,
											 TraversalOption.SPACE_ACTIONS));
	  } catch (ArrayIndexOutOfBoundsException dontKillBot) {}
	 return false;
    }
   
    private final static boolean inWilderniss() {
	 return (Widgets.get(381).getComponent(2).isVisible());
    }
   
    static int profit = 0;
    private static GroundItem lootMe = null;
   
    private final static boolean eatAsap() {
	 Item[] item = Inventory.getItems();
	 for(int i = 0; i < item.length; i++)
	  for(String t : item[i].getComponent().getActions())
	   if(t != null && t.contains("eat"))
	    return item[i].interact("Eat");
	 return false;
    }
   
    static boolean mouseHardcore = false;
    static int x, y, lX, lY;
    private final void uberMouseHandleing() {
	 mouseHardcore = false;
  new Thread(new Runnable() {
   public void run() {
    while(!mouseHardcore) {
	 lX = x;
	 lY = y;
	 x = lootMe.getInteractPoint().x;
	 y = lootMe.getInteractPoint().y;
    }   
   }
  }).start();  
  if(lX != Mouse.getLocation().getX())
   if(lX > Mouse.getLocation().getX())
    lX = lX -1;
   else lX = lX + 1;
  if(lY != Mouse.getLocation().getX())
   if(lY > Mouse.getLocation().getX())
    lY = lY -1;
   else lY = lY + 1; 
  moveMouse(new Rectangle(lX, lY,0,0), false);
  while(!lootMe.getModel().contains(Mouse.getLocation()))
   uberMouseHandleing();
  //if(moveMouse(new Rectangle(x, y, 0, 0), false))
  mouseHardcore = true;
    }
   
@Override
public int loop() {
   switch(iCase) {
   case 5 : //Test case
    if(atMenuCurrentPosition("Take Members object")) {
	 Mouse.move(Calculations.tileToScreen(Players.getLocal().getLocation()));
    }
   
   return 1;
    case 0 :
	 if(Inventory.isFull() || bankForEscape || (!inWilderniss()) && Players.getLocal().getLocation().getY() <= 3522) {
	  iCase = 1;
	  sStatus = "[SM] Time to bank.";
	  Item[] a = Inventory.getItems();
	  for(int i = 0; i < a.length; i++)
	   try {
	    profit += pricesMap.get(a[i].getId()) * a[i].getStackSize();
	   } catch(NullPointerException e) {
	    //Item has no ge value aka not in table.
	    //Meaning manual looting !<img src='http://powerbot-gold4rs.netdna-ssl.com/community//public/style_emoticons/<#EMO_DIR#>/excited.png' class='bbc_emoticon' alt=':D' />
	   }
	   log("PROFIT ++ " + profit);
	  return 1;
	 }
	 if(myPlayer().getHpPercent() < 30) {
	  if(eatAsap())
	   log("Succesufully eated");
	  else {
	   iCase++;
	   bankForEscape = true;
	   log("Failed to eat");
	   return 1;
	  }
	  return 1;
	 }
	 if(Players.getLocal().isInCombat()) {
	  log("[FS] We are in combat, walking around.");
	  Player p = Players.getLoaded()[Random.nextInt(0, Players.getLoaded().length)];
	  walkTo(p.getLocation());
	  if(Players.getLocal().getHpPercent() < 60)
	   if(!eatAsap()) {
	    iCase++;
	    bankForEscape = true;
	    return 1;
	   }
	  if(!this.waitTillMovingStop(new Timer(2000)));
	   return 1;   
	 }
	 lootMe = getMostExpensive();  
	 if(lootMe != null) {	 
	  if(!lootMe.isOnScreen()) {
	   sStatus = "MTI : " + lootMe.getItem().getName() +"-"
	    + lootMe.getItem().getStackSize() + "#-"
	    + lootMe.getItem().getStackSize() * pricesMap.get(lootMe.getItem().getId()) +"$- Distance :"
	    + lootMe.getLocation().distanceTo(myPlayer().getLocation()) + ".";
	   if(walkTo(lootMe.getLocation()))
	    waitTillMovingStop(new Timer(5000));
	    //sleep(1000);
	  }
	  try {
	   if(lootMe != null && lootMe.isOnScreen() && lootMe.getItem().hasDefinition()) { //hasDefi still valid ? 
	    sStatus = "TTL " + lootMe.getItem().getName() + " $'" + pricesMap.get(lootMe.getItem().getId());
	    moveMouse(new Rectangle(lootMe.getInteractPoint().x, lootMe.getInteractPoint().y, 0, 0), false);
	    //uberMouseHandleing();
	    while(Players.getLocal().isMoving() && lootMe.getLocation().distanceTo(myPlayer().getLocation()) >= 1)
		 sleep(20);
	    try {
		 if(atMenuCurrentPosition("Take " + lootMe.getItem().getName())) {
		  Item p = getDropableItem(ingoreThese);
		  if(p != null)
		   if(p.interact("Drop"))
		    sleep(1000);
		 
		  //TODO whatever todo here will slowdown anyways
		 } else lootMe = null;
	    } catch (NullPointerException e) {
		
	    }
		 
	   }
	  } catch(ArrayIndexOutOfBoundsException e) {
	   //ingore fag
	  } catch(NullPointerException ee) {
	   //Fking gay fix.
	   //Even when != null cast on line 504 returns null...
	  }
	 } else if(Walking.getEnergy() <= 20 || !Walking.isRunEnabled()) {
	  Walking.rest();
	  Walking.setRun(true);
	  return 1;
	 } else {
	  log("Walking around");
	  try {
	   Player p = Players.getLoaded()[Random.nextInt(0, Players.getLoaded().length)];
	   if(walkTo(p.getLocation().getY() >=3523 ? p.getLocation() : new Tile(myPlayer().getLocation().getX(), myPlayer().getLocation().getY() + Random.nextInt(10, 15))))
	    waitTillMovingStop(new Timer(5000));
	  } catch (ArrayIndexOutOfBoundsException i) {
	  
	  }
	 
	  Timer t = new Timer(Random.nextInt(3000, 7000));
	  while(t.isRunning()) {	  
	   if(getMostExpensive() != null || myPlayer().isInCombat()) {
	    log("Breaked sleep!");
	    break;
	   }
	   else if(Walking.getEnergy() < 45)
	    Walking.rest();
	   sleep(10);
	  }
	 }
    return 1;
   
    case 1 :
	 if(Players.getLocal().getLocation().getY() >= 3523 && (Inventory.isFull() || bankForEscape)) {
	  sStatus = "Still In wilderniss, time to get out."; 
	  walkTo(new Tile(Random.nextInt(3078, 3085), 3524));
	  this.waitTillMovingStop(new Timer(2000));
	  try {
	   GameObject i = Objects.getNearest(1443, 1442, 1441);
	   walkTo(i.getLocation());
	   if(i.isOnScreen()) {
	    i.interact("Cross");
	   } else return 1; //rewalk asshole.
	  } catch(NullPointerException e) {
	   return 1;
	  } 
	 
	  Timer t = new Timer(6000);
	  while(t.isRunning())
	   if(Players.getLocal().getLocation().getY() <= 3520) {
	    sStatus = "Succesfully crossed wilderniss.";
	    break;
	   }
	 } else if(Inventory.isFull()) {
	   walkTo(new Tile(3096, 3497));
	   while(myPlayer().isMoving())
	    sleep(20);
	   if(Bank.open())
	    Bank.depositAll();
	 } else {
	  if(Players.getLocal().getLocation().getY() >= 3523) {
	   sStatus = "Succesfully crossed wilderniss.";
	   iCase = 0;
	   return iCase;
	  }
	  if(Players.getLocal().getLocation().getY() <= 3520) {
	   sStatus = "Time to get in wilderniss.";
	   GameObject i = Objects.getNearest(1443, 1442, 1441);
	   if(i != null && i.isValid()) {
	    walkTo(i.getLocation());
	    this.waitTillMovingStop(new Timer(6000));
	    if(i.isOnScreen())
		 i.interact("Cross");
	    else return 1; //Rewalk asshole
	    Timer t = new Timer(6000);
	    while(t.isRunning())
		 if(Players.getLocal().getLocation().getY() >= 3523) {
		  sStatus = "Succesfully crossed wilderniss.";
		  iCase = 0;
		  bankForEscape = false;
		  return iCase;
		  //break;
		 }
	   }
	  }
	 }	
	 return 1;
   }
  return -1;
}

boolean waitTillMovingStop(Timer t){
  while(!myPlayer().isMoving() && t.isRunning())
   sleep(20);
  t.reset();
  while(t.isRunning())
   if(!myPlayer().isMoving()) {
    return true;
   } else if(nearTile != null && nearTile.distanceTo(myPlayer().getLocation()) <= 1) {
    log("Close enough, quiting loop");
    return true;
   } else sleep(50); 
  return false;
}

static Player myPlayer() {
  return Players.getLocal();
}
/*
  * Paint methods down here, needs cleaning.
  */
final private static float avarageXpAHour(final long timePassed, final int currentXp, final int startXp) {
  return 1000 / ((float)(timePassed / 60 / 60) / (currentXp - startXp));
}

//private class paint {
  protected final Rectangle chatBox_UpButton = new Rectangle(497, 345, 16, 16);
  protected boolean openPaintMenu = false;
//}
 
  //3, (441 / 14 MAX 6 ?)
 

@Override
public void onRepaint(Graphics s) {
  s.setColor(new Color(0, 0, 0, 170));
  s.fillRect(427 - 60, 395, 97 + 55, 110);
  s.fillRect(8, 458, 502, 17);
 
  s.setColor(new Color(237,89,135, 170));
  s.fillRect(chatBox_UpButton.x, chatBox_UpButton.y, chatBox_UpButton.height, chatBox_UpButton.width);
 
  s.setColor(new Color(0, 0, 0));
  s.fillRect(8, 458, 88, 17); 
  if(openPaintMenu) {
   s.setColor(new Color(237,89,135, 140));
   for(int i = 0; i < 8; i++)
    s.fillRect(7, 443 - (14 * i), 350, 14);
  }
 
  if(lootMe != null && lootMe.getItem().hasDefinition())
   lootMe.getLocation().draw(s);
 
  s.setFont(new Font("Comic Sans", Font.BOLD, 12)); 
  s.setColor(Color.WHITE);
  s.drawString("Loaded Items :  " + pricesMap.size() , 430 - 60, 410);
  s.drawString("GP/H = " + new DecimalFormat("###,###").format((int) avarageXpAHour(startTime.getElapsed(), profit, 0)) , 430 - 60, 425);
  s.drawString("GP Looted = " + new DecimalFormat("###,###").format(profit) , 430 - 60, 440);
  s.drawString("HighRisk Logins : " + highRiskLogins, 430 - 60, 455);
  s.drawString("Runtime "+  (int) (((startTime.getElapsed() / 1000) / 60) / 60) % 60 +"H:"+(int) ((startTime.getElapsed() / 1000) / 60) % 60+ "M:"+(int) (startTime.getElapsed() / 1000) % 60+"S", 430 - 60, 470);
  s.setColor(Color.GREEN);
  if(sStatus != null)
   s.drawString(sStatus, 430 - 332, 470);
 
}
@Override
public void mouseClicked(MouseEvent arg0) {
  if(chatBox_UpButton.contains(arg0.getPoint()))
   openPaintMenu = !openPaintMenu;
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

public class _cWGui extends JPanel {
  public _cWGui() {
   initComponents();
  }
  private void menuItem1MouseClicked(MouseEvent e) {
   //aa
   // TODO add your code here
  }
  private void initComponents() {
   // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
   menuBar1 = new JMenuBar();
   menu1 = new JMenu();
   menuItem1 = new JMenuItem();
   textArea1 = new JTextArea();
   checkBox1 = new JCheckBox();
   checkBox2 = new JCheckBox();
   formattedTextField1 = new JFormattedTextField();
   label1 = new JLabel();
   //======== this ========
   setLayout(null);
   //======== menuBar1 ========
   {
    //======== menu1 ========
    {
	 menu1.setText("General");
	 //---- menuItem1 ----
	 menuItem1.setText("Exit");
	 menuItem1.addMouseListener(new MouseAdapter() {
	  @Override
	  public void mouseClicked(MouseEvent e) {
	   menuItem1MouseClicked(e);
	  }
	 });
	 menu1.add(menuItem1);
    }
    menuBar1.add(menu1);
   }
   add(menuBar1);
   menuBar1.setBounds(0, 0, 400, menuBar1.getPreferredSize().height);
   //---- textArea1 ----
   textArea1.setText("Add items to ignore here!");
   add(textArea1);
   textArea1.setBounds(250, 25, 145, 270);
   //---- checkBox1 ----
   checkBox1.setText("Cache loaded items ?");
   checkBox1.setSelected(true);
   checkBox1.setEnabled(false);
   add(checkBox1);
   checkBox1.setBounds(10, 35, 160, checkBox1.getPreferredSize().height);
   //---- checkBox2 ----
   checkBox2.setText("Drop unwanted items?");
   add(checkBox2);
   checkBox2.setBounds(10, 60, 175, checkBox2.getPreferredSize().height);
   //---- formattedTextField1 ----
   formattedTextField1.setText("125");
   add(formattedTextField1);
   formattedTextField1.setBounds(115, 90, 40, formattedTextField1.getPreferredSize().height);
   //---- label1 ----
   label1.setText("Minimum price ?");
   add(label1);
   label1.setBounds(10, 90, 110, label1.getPreferredSize().height);
   { // compute preferred size
    Dimension preferredSize = new Dimension();
    for(int i = 0; i < getComponentCount(); i++) {
	 Rectangle bounds = getComponent(i).getBounds();
	 preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
	 preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
    }
    Insets insets = getInsets();
    preferredSize.width += insets.right;
    preferredSize.height += insets.bottom;
    setMinimumSize(preferredSize);
    setPreferredSize(preferredSize);
   }
   // JFormDesigner - End of component initialization  //GEN-END:initComponents
  }
  // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
  private JMenuBar menuBar1;
  private JMenu menu1;
  private JMenuItem menuItem1;
  private JTextArea textArea1;
  private JCheckBox checkBox1;
  private JCheckBox checkBox2;
  private JFormattedTextField formattedTextField1;
  private JLabel label1;
  // JFormDesigner - End of variables declaration  //GEN-END:variables
}
}
