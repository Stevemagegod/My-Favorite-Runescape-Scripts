import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.text.StyledEditorKit.BoldAction;

import com.rarebot.concurrent.Task;
import com.rarebot.concurrent.strategy.Strategy;
import com.rarebot.game.api.ActiveScript;
import com.rarebot.game.api.Manifest;
import com.rarebot.game.api.methods.Calculations;
import com.rarebot.game.api.methods.Game;
import com.rarebot.game.api.methods.Widgets;
import com.rarebot.game.api.methods.input.Mouse;
import com.rarebot.game.api.methods.interactive.NPCs;
import com.rarebot.game.api.methods.interactive.Players;
import com.rarebot.game.api.methods.widget.Camera;
import com.rarebot.game.api.util.Random;
import com.rarebot.game.api.util.Time;
import com.rarebot.game.api.wrappers.RSTile;
import com.rarebot.game.api.wrappers.widget.Widget;
import com.rarebot.game.api.wrappers.widget.WidgetChild;
import com.rarebot.game.bot.event.MessageEvent;
import com.rarebot.game.bot.event.listener.MessageListener;
import com.rarebot.game.bot.event.listener.PaintListener;

@Manifest(name = "Sumo's Sudoku", description = "Solve Ali Morrisane's RuneSudoku. Stand close to him and start the script.", version = 1.0, authors = {"Sumo"})

public class Sudoku extends ActiveScript implements PaintListener, MessageListener{
  
	final Antiban antiban = new Antiban();
	final SudokuStart sudoku = new SudokuStart();
	final StartSudoku startSudoku = new StartSudoku();
	final RunTo run = new RunTo();

	//Antiban times so it doesn't check extremely often.
	private long lastAnitban = (long) System.currentTimeMillis() / 1000;
	private long nextAntibanCheck =(long) (System.currentTimeMillis() / 1000) + 15;
	
	private long startTime = System.currentTimeMillis();
	private long elapsedTimeMillis;
	private long elapsedTimeHours;
	private long elapsedTimeMinutes;
	
	private int sudokusSolved = 0;
	
	//The model id of runes. Used to grab the number below.
	private int earthRuneId = 8979;
	private int waterRuneId = 8987;
	private int airRuneId = 8975;
	private int fireRuneId = 8980;
	private int mindRuneId = 8982;
	private int lawRuneId = 8981;
	private int chaosRuneId = 8977;
	private int deathRuneId = 8978;
	private int bodyRuneId = 8976;
	
	//The number I assign each rune to solve the sudoku.
	private int earthRuneNum = 1;
	private int waterRuneNum = 2;
	private int airRuneNum = 3;
	private int fireRuneNum = 4;
	private int mindRuneNum = 5;
	private int lawRuneNum = 6;
	private int chaosRuneNum = 7;
	private int deathRuneNum = 8;
	private int bodyRuneNum = 9;
	
	//The id of runes that you can click
	private int clickableEarth = 202;
	private int clickableWater = 203;
	private int clickableAir = 204;
	private int clickableFire = 206;
	private int clickableMind = 205;
	private int clickableBody = 207;
	private int clickableDeath = 208;
	private int clickableChaos = 209;
	private int clickableLaw = 210;
	private int clickableClear = 211;	
	
	//Just same areas/positions in case the bot missclicks and runs away.
	final RSTile bottomLeft = new RSTile(3302, 3210, 0);
	final RSTile topRight = new RSTile(3305, 3212, 0);
	final Area aliArea = new Area(bottomLeft, topRight);
	final RSTile startPos = new RSTile(3305, 3211, 0);
	
	//Number of runes on the board.
	private int runeSlots = 81;
	
	//Ali Morrisane's Id.
	private int aliMorrison = 1862;

	ArrayList<Integer> filledSlots = new ArrayList<Integer>();
	
	//Has the bot started solving the sudoku and finished talking to ali moris?
	private boolean sudokuStarted = false;
	private boolean sudokuFinishedTalkingTo = false;
	private boolean sudokuStartedTalkingTo = false;

	//Status for paint.
	private String status;
	
	
	
	@Override
	public void messageReceived(MessageEvent arg0) {
		String messageText = arg0.getMessage();
		
		if (messageText.contains("still locked")){
			walkRSTileMM(startPos);
		}else if(messageText.contains("locking mechanism")){
			sudokusSolved++;
		}
		
	}

	@Override
	public void onRepaint(Graphics arg0) {
		Graphics2D g = (Graphics2D) arg0;
		//g.draw3DRect(5, 20, 250, 75, true);
		//g.drawRect(5, 20, 250, 70);
		g.setColor(Color.red);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		g.drawString("Sumo's Sudoku Solver " + getVersion(), 10, 35);
		g.setFont(new Font("Arial", Font.PLAIN, 12));
		
		elapsedTimeMillis = System.currentTimeMillis() - startTime;
        elapsedTimeHours = elapsedTimeMillis / (1000 * 60 * 60);
        elapsedTimeMillis -= elapsedTimeHours * (1000 * 60 * 60);
        elapsedTimeMinutes = elapsedTimeMillis / (1000 * 60);
        elapsedTimeMillis -= elapsedTimeMinutes * (1000 * 60);
        g.drawString("Status: " + status, 8, 50);
        g.drawString("Sudoku's Solved: " + sudokusSolved, 8, 65);
        g.drawString("Running for " + elapsedTimeHours + " hours and " + elapsedTimeMinutes + " minutes", 8, 80);
	
        //g.setColor(Color.yellow);
 		//g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));
 		//g.fillRect(5, 20, 225, 70);
	}
	
	public double getVersion(){
		return 1.0;
	}

	@Override
	protected void setup() {
		provide(new Strategy(antiban, antiban));
		provide(new Strategy(startSudoku, startSudoku));
		provide(new Strategy(run, run));
		provide(new Strategy(sudoku, sudoku));
	}
	
	private int getNumFromModelId(int modelid){
		if (modelid == earthRuneId){
			return earthRuneNum;
		}else if(modelid == waterRuneId){
			return waterRuneNum;
		}else if (modelid == airRuneId){
			return airRuneNum;
		}else if(modelid == fireRuneId){
			return fireRuneNum;
		}else if (modelid == mindRuneId){
			return mindRuneNum;
		}else if (modelid == lawRuneId){
			return lawRuneNum;
		}else if (modelid == chaosRuneId){
			return chaosRuneNum;
		}else if (modelid == deathRuneId){
			return deathRuneNum;
		}else if (modelid == bodyRuneId){
			return bodyRuneNum;
		}
		
		return 0;
	}
	
	private int getClicakbelIdFromNum(int num){
		if (num == earthRuneNum){
			return clickableEarth;
		}else if (num == waterRuneNum){
			return clickableWater;
		}else if (num == airRuneNum){
			return clickableAir;
		}else if (num == fireRuneNum){
			return clickableFire;
		}else if (num == mindRuneNum){
			return clickableMind;
		}else if (num == lawRuneNum){
			return clickableLaw;
		}else if (num == chaosRuneNum){
			return clickableChaos;
		}else if (num == deathRuneNum){
			return clickableDeath;
		}else if (num == bodyRuneNum){
			return clickableBody;
		}
		
		return 0;
	}
	
	//Incase of a missclick and character runs off.
	
	private class RunTo extends Strategy implements Task{

		@Override
		public void run() {
			status = "Walking to Ali";
			
			sudokuFinishedTalkingTo = false;
			sudokuStarted = false;
			sudokuStartedTalkingTo = false;
			
			while (!aliArea.contains(Players.getLocal().getLocation())){
				walkRSTileMM(startPos);
			}
		}
		
		@Override
		public boolean validate(){
			return !aliArea.contains(Players.getLocal().getLocation());
		}
		
	}
	
	//Stuff for talking to ali
	
	private class StartSudoku extends Strategy implements Task{

		@Override
		public void run() {
			sudokuStartedTalkingTo = true;
			Time.sleep(Random.nextInt(1000, 1500));
			status = "Talking to Ali";
			NPCs.getNearest(aliMorrison).click(true);
			Time.sleep(Random.nextInt(1500, 1750));
			
			//This is a little buggy as different chat options for different people.
			//Originally I had it going of widget id's.
			//It now grabs the text it should click, finds the widget and clicks it.
			//Bug? .clickContinue works if it is not the characters dialog.
			
			while (aliArea.contains(Players.getLocal().getLocation()) && !sudokuFinishedTalkingTo && isRunning()){
				
			if (progressThroughChat()){
				Time.sleep(Random.nextInt(750, 1000));
			}else{
			}
			
			Time.sleep(Random.nextInt(250, 500));
					
			if (Widgets.get(288, clickableAir).isOnScreen()){
				sudokuFinishedTalkingTo = true;
			}
			
			Time.sleep(500);
			
			}
			
			sudokuStartedTalkingTo = false;
			
		}
		
		private boolean progressThroughChat(){
			WidgetChild[] TalkWidgets = Widgets.get(1188).getChildren();
			String chatOptions[] = {"not bad", "selection of runes", "large casket", "examine"};

			for (String str : chatOptions){
				if (clickWidgetByText(TalkWidgets, str)){
					return true;
				}
			}
			
			if (continueIfPossible()){
				return true;
			}
			
			return false;
		}
		
		//Get a widget's text and click it.
		
		private boolean clickWidgetByText(WidgetChild[] widgets, String text){
			for (WidgetChild widge : widgets){
				if (widge.getText() != null){
					if (widge.getText().toLowerCase().contains(text.toLowerCase())){
						widge.click(true);
						Time.sleep(Random.nextInt(750, 1000));
						return true;
					}
				}
			}
			
			return false;
		}
		
		private boolean continueIfPossible(){
			if (Widgets.canContinue()){
				Widgets.clickContinue();
				return true;
			}else if (Widgets.get(1191).getChild(18).isOnScreen()){;
				Widgets.get(1191).getChild(18).click(true);
				return true;
			}
			
			return false;
		}
		
		@Override
		public boolean validate(){
			if (!sudokuFinishedTalkingTo && !sudokuStarted && aliArea.contains(Players.getLocal().getLocation()) && !sudokuStartedTalkingTo){
				return true;
			}
			
			return false;
		}
		
	}
	
	//Solve le sudoku

	private class SudokuStart extends Strategy implements Task{
		
		@Override
		public void run() {
			status = "Starting Sudoku";
			sudokuStarted = true;
			filledSlots.clear();
			Widget sud = Widgets.get(288);
			WidgetChild[] sudChilds = sud.getChildren();
			
			int count = 0;
			
				int[] widgetIndex = new int[81];
			
				for (WidgetChild wchild : sudChilds){
					
					if (wchild.getType() == 6 && count < 81){
						widgetIndex[count] = wchild.getIndex();
						count++;
						filledSlots.add(wchild.getModelId());
					}
				}
				
				int x = 0;
				int y = 0;
				int z = 1;

				int[] row1 = new int[9];
				int[] row2 = new int[9];
				int[] row3 = new int[9];
				int[] row4 = new int[9];
				int[] row5 = new int[9];
				int[] row6 = new int[9];
				int[] row7 = new int[9];
				int[] row8 = new int[9];
				int[] row9 = new int[9];
				
				int[] zrow1 = new int[9];
				int[] zrow2 = new int[9];
				int[] zrow3 = new int[9];
				int[] zrow4 = new int[9];
				int[] zrow5 = new int[9];
				int[] zrow6 = new int[9];
				int[] zrow7 = new int[9];
				int[] zrow8 = new int[9];
				int[] zrow9 = new int[9];
				
				for (int i : filledSlots){
					x++;
						if (z == 1){
							row1[y] = getNumFromModelId(i);
							zrow1[y] = getNumFromModelId(i);
						}else if (z == 2){
							row2[y] = getNumFromModelId(i);
							zrow2[y] = getNumFromModelId(i);
						}else if (z == 3){
							row3[y]= getNumFromModelId(i); 
							zrow3[y]= getNumFromModelId(i); 
						}else if (z == 4){
							row4[y] = getNumFromModelId(i);
							zrow4[y] = getNumFromModelId(i);
						}else if (z == 5){
							row5[y] = getNumFromModelId(i);
							zrow5[y] = getNumFromModelId(i);
						}else if (z == 6){
							row6[y] = getNumFromModelId(i);
							zrow6[y] = getNumFromModelId(i);
						}else if (z == 7){
							row7[y] = getNumFromModelId(i);
							zrow7[y] = getNumFromModelId(i);
						}else if (z == 8){
							row8[y] = getNumFromModelId(i);
							zrow8[y] = getNumFromModelId(i);
						}else if (z == 9){
							row9[y] = getNumFromModelId(i);
							zrow9[y] = getNumFromModelId(i);
						}
					y++;
					if (y > 8){
						y = 0;
					}
					
					if (x % 9 == 0){
						z++;
					}
				}
				
				
				int[][] preboard = {row1, row2, row3, row4, row5, row6, row7, row8, row9};
				int[][] board = {zrow1, zrow2, zrow3, zrow4, zrow5, zrow6, zrow7, zrow8, zrow9};
				
				SudokuSolve solve = new SudokuSolve();
				if (!solve.start(board)){
					System.out.println("Failed to solve!");
				}
				
				/*System.out.println("\n");
				
				for (int[] p : preboard){
					System.out.print("{");
					for (int q : p){
						System.out.print(q + ",");
					}
					System.out.print("}\n");
				}
				
				System.out.println("\n\n");
				
				for (int[] p : board){
					System.out.print("{");
					for (int q : p){
						System.out.print(q + ",");
					}
					System.out.print("}\n");
				}*/
				
				
				//int c = 0;
				//int col = 0;
				//int totalCount = 0;
				
				status = "Completing Sudoku";
				
				//Start at first square increment by one to end
				
				/*for (int[] a : preboard){
					for (int b : a){
						if (b == 0 && isRunning()){
							int numToSet = board[c][col];
							int widgetId = getClicakbelIdFromNum(numToSet);
							sud.getChild(widgetId).click(true);
							Time.sleep(Random.nextInt(500, 1000));
							sudChilds[widgetIndex[totalCount]].click(true);
							Time.sleep(Random.nextInt(500, 1000));
						}
						totalCount++;
						col++;
					}
					col = 0;
					c++;
				}*/
				
				//Randomly select a square to fill.
				
				ArrayList<Integer> slotsToFill = new ArrayList<Integer>();
				
				int tCount = 0;
				
				for (int[] a : preboard){
					for (int b : a){
						if (b == 0 && isRunning()){
							slotsToFill.add(tCount);
							//System.out.println("Tcount = " + tCount);
						}
						tCount++;
					}
				}
				
				while (slotsToFill.size() > 0 && isRunning() && aliArea.contains(Players.getLocal().getLocation())){
					int RandNum = Random.nextInt(0, slotsToFill.size()-1);
					int Rand = slotsToFill.get(RandNum);
					int rowToSet = getRow(Rand);
					int colToSet = getCol(Rand);
					int numToSet = board[rowToSet][colToSet];
					int widgetId = getClicakbelIdFromNum(numToSet);
					sud.getChild(widgetId).click(true);
					Time.sleep(Random.nextInt(500, 1000));
					sudChilds[widgetIndex[Rand]].click(true);
					slotsToFill.remove(RandNum);
					Time.sleep(Random.nextInt(500, 1000));
				}
									
				Time.sleep(Random.nextInt(1000, 1500));
				
				Widgets.get(288).getChild(9).click(true);
				
				Time.sleep(Random.nextInt(2500, 3000));
				
				Widgets.get(1188, 3).click(true);
				
				Time.sleep(Random.nextInt(750, 1000));
				
				//Sudoku Solved Open Casket
				sudokuFinishedTalkingTo = false;
				sudokuStarted = false;
				
				filledSlots.clear();
				
		}
		
		private int getRow(int num){
			
			int floor = (int)num / 9;
			return floor;		
		}
		
		private int getCol(int num){
			int floor = (int)(num + 1) / 9;
			int returnNum = num;
			for (int i = 0; i < floor; i++){
				returnNum-=9;
			}
			
			if (returnNum < 0){
				return 8;
			}
			
			return returnNum;
		}
		
		@Override
		public boolean validate(){
			return sudokuFinishedTalkingTo && Widgets.get(288, clickableAir).isOnScreen() && aliArea.contains(Players.getLocal().getLocation()) && !sudokuStarted;
		}
		
	}
	
	//Antiban, is this even needed for sudoku?
	
	private class Antiban extends Strategy implements Task{

		@Override
		public void run() {
			String previousStatus = status;
			status = "Antiban";
			int ranNum = Random.nextInt(1,5);
			if (ranNum == 1){
				Camera.setAngle(Random.nextInt(1, 180));
			}else if(ranNum == 2){
				Camera.setAngle(Random.nextInt(180, 360));
			}else if (ranNum == 3){
				Camera.setPitch(Random.nextInt(25,60));
			}else if (ranNum == 4){
				Camera.setPitch(Random.nextInt(26, 90));
			}else{
				Time.sleep(Random.nextInt(250, 1000));
			}
			
			Time.sleep(1000);
			status = previousStatus;
			
			lastAnitban = (long)System.currentTimeMillis() / 1000;
			nextAntibanCheck = lastAnitban + (17 + Random.nextInt(1, 14));
		}
		
		@Override
		public boolean validate() {
			long now = (long)System.currentTimeMillis() / 1000;
			if (now >= nextAntibanCheck && Game.isLoggedIn()){
				return aliArea.contains(Players.getLocal().getLocation());
			}
			
			return false;
		}
		
	}
	
	//Sudoku Solving thanks to http://www.colloquial.com/games/sudoku/java_sudoku.html

	public class SudokuSolve {

	    /**
	     * Print the specified Sudoku problem and its solution.  The
	     * problem is encoded as specified in the class documentation
	     * above.
	     *
	     * @param args The command-line arguments encoding the problem.
	     */
	    public boolean start(int[][]board) {
	        int[][] matrix = board;
	        //writeMatrix(matrix);
	        if (solve(0,0,matrix)){    // solves in place
	           return true;
	        }

	        
	        return false;
	    }

	    boolean solve(int i, int j, int[][] cells) {
	        if (i == 9) {
	            i = 0;
	            if (++j == 9)
	                return true;
	        }
	        if (cells[i][j] != 0)  // skip filled cells
	            return solve(i+1,j,cells);

	        for (int val = 1; val <= 9; ++val) {
	            if (legal(i,j,val,cells)) {
	                cells[i][j] = val;
	                if (solve(i+1,j,cells))
	                    return true;
	            }
	        }
	        cells[i][j] = 0; // reset on backtrack
	        return false;
	    }

	    boolean legal(int i, int j, int val, int[][] cells) {
	        for (int k = 0; k < 9; ++k)  // row
	            if (val == cells[k][j])
	                return false;

	        for (int k = 0; k < 9; ++k) // col
	            if (val == cells[i][k])
	                return false;

	        int boxRowOffset = (i / 3)*3;
	        int boxColOffset = (j / 3)*3;
	        for (int k = 0; k < 3; ++k) // box
	            for (int m = 0; m < 3; ++m)
	                if (val == cells[boxRowOffset+k][boxColOffset+m])
	                    return false;

	        return true; // no violations, so it's legal
	    }

	    int[][] parseProblem(String[] args) {
	        int[][] problem = new int[9][9]; // default 0 vals
	        for (int n = 0; n < args.length; ++n) {
	            int i = Integer.parseInt(args[n].substring(0,1));
	            int j = Integer.parseInt(args[n].substring(1,2));
	            int val = Integer.parseInt(args[n].substring(2,3));
	            problem[i][j] = val;
	        }
	        return problem;
	    }
	}
	
	//Credit to custom method thread
	
	public class Area {

        private RSTile bl, tr;
        private RSTile[] array;

        public Area(final RSTile bottomLeft, final RSTile topRight) {
            this.bl = bottomLeft;
            this.tr = topRight;
            final List<RSTile> array = new ArrayList<RSTile>();
            for (int x = bl.getX(); x <= tr.getX(); x++) {
                for (int y = bl.getY(); y <= tr.getY(); y++) {
                    array.add(new RSTile(x, y, bl.getPlane()));
                }
            } this.array = array.toArray(new RSTile[array.size()]);
        }

        public boolean contains(final RSTile t) {
            return t.getX() >= bl.getX() && t.getX() <= tr.getX()
                    && t.getY() >= bl.getY() && t.getY() <= tr.getY();
        }

        public RSTile[] getRSTileArray() {
            return array;
        }

        public boolean containsAll(final RSTile... RSTiles) {
            for (final RSTile t : RSTiles) {
                if (!contains(t)) return false;
            } return true;
        }
        public float distanceTo(RSTile RSTile) {
            return (float) Calculations.distance(Players.getLocal()
                    .getLocation(), RSTile);
        }
        public RSTile getNearest(final RSTile t) {
            int dist = 10000;
            RSTile ret = null;
            for (final RSTile t2 : getRSTileArray()) {
                final int temp = (int) distanceTo(t2);
                if (temp < dist) {
                    ret = t2;
                    dist = temp;
                }
            } return ret;
        }

        public RSTile getCentre() {
            return new RSTile((tr.getX() + bl.getX()) / 2, (tr.getY() - bl.getY()) / 2, bl.getPlane());
        }

    }
	
	public boolean walkRSTileMM(RSTile dest) {
		if (!RSTileOnMap(dest)) {
			dest = getClosestRSTileOnMap(dest);
		}
		final Point p = Calculations.worldToMap(dest.getX(), dest.getY());
		if (p.x != -1 && p.y != -1) {
			Mouse.move(p.x, p.y);
			final Point p2 = Calculations.worldToMap(dest.getX(), dest.getY());
			if (p2.x != -1 && p2.y != -1) {
				if (Mouse.getLocation().equals(p2)) {
					Mouse.move(p2.x, p2.y);
				}
				if (Mouse.getLocation().equals(p2)) {
					Mouse.move(p2.x, p2.y);
				}
				if (!Mouse.getLocation().equals(p2)) {
					Mouse.move(p2.x, p2.y);
				}
				Mouse.click(true);
				return true;
			}
		}
		return false;
	}
	
	public RSTile getClosestRSTileOnMap(final RSTile RSTile) {
		if (!RSTileOnMap(RSTile) && Game.isLoggedIn()) {
			final RSTile loc = Players.getLocal().getLocation();
			final RSTile walk = new RSTile((loc.getX() + RSTile.getX()) / 2, (loc.getY() + RSTile.getY()) / 2, Game.getPlane());
			return RSTileOnMap(walk) ? walk : getClosestRSTileOnMap(walk);
		}
		return RSTile;
	}
	
	public double distanceBetween(final RSTile curr, final RSTile dest) {
		return Math.sqrt((curr.getX() - dest.getX()) * (curr.getX() - dest.getX()) + (curr.getY() - dest.getY()) * (curr.getY() - dest.getY()));
	}
	
	public int distanceTo(final RSTile t) {
		return t == null ? -1 : (int) distanceBetween(Players.getLocal().getLocation(), t);
	}

	public boolean RSTileOnMap(final RSTile t) {
		return distanceTo(t) < 15;
	}
}
