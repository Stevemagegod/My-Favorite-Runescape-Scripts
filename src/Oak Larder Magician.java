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
import java.io.IOException;
import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import javax.imageio.ImageIO;

import com.rarebot.event.events.MessageEvent;
import com.rarebot.event.listeners.MessageListener;
import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game.Tab;
import com.rarebot.script.methods.Skills;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Magic" }, keywords = "Oak Larder Magician", name = "Oak Larder Magician", version = 1.0, description = "Makes Oak Larders")
public class OakLarderMagician extends Script implements PaintListener, MouseListener, MessageListener {

  private String status = "";	
	
	public int Made = 0;
	public int MadeHour = 0;
	
	public int OakPlank = 8778;
	public int Demon = 4243;
	
	public int LarderBuilt = 13566;
	public int EmptyLarder = 15403;
	
	public int bLarderID = 15403;
	public int rLarderID = 13566;
	public int demonID = 4243;
	public int oakPlankID = 8778;
	
	   public long startTime = System.currentTimeMillis();

	    private final Color color1 = new Color(51, 51, 51, 197);
	    private final Color color2 = new Color(0, 0, 0);
	    private final Color color3 = new Color(51, 51, 51);
	    private final Color color4 = new Color(255, 255, 255);

	    private final BasicStroke stroke1 = new BasicStroke(2);
	    private final BasicStroke stroke2 = new BasicStroke(1);

	    private final Font font1 = new Font("Lithos Pro", 0, 15);
	    
	    private Image getImage(String url) {
	        try {
	            return ImageIO.read(new URL(url));
	        } catch(IOException e) {
	            return null;
	        }
	    }
	    
	    private final Image img1 = getImage("http://i42.tinypic.com/2582eec.png");
	    private final Image img2 = getImage("http://www.zybez.net/img/skillimg/construction/oak_larder.gif");
	    private final Image img3 = getImage("http://z4.ifrm.com/30300/188/0/f5249103/Clock.gif");
	    
		public boolean onStart() {			
	    log("Welcome to Oak Larder Magician");
	    if (!game.isLoggedIn()) {
	    	return false;
	    }
	    return true;
		}
		
        public void onFinish() {
        	log("Thanks for using Oak Larder Magician");
        }
        
		@Override
		public int loop() {
			
		if (inventory.getItem(OakPlank) == null || inventory.getCount(OakPlank) <= 7) {
			
	        RSNPC DemonBut = npcs.getNearest(Demon);
			
			if (DemonBut != null) {
				if (DemonBut.isOnScreen()) {
					
					status = "Talking: Demon";
					 
					DemonBut.interact("Fetch-from-bank");	
					sleep(1200, 1500);
			        
			        //Pay Demon
			        
			        if (interfaces.getComponent(1184, 21).isValid()) {
			        	if (!interfaces.getComponent(1184, 13).getText().contains("I have returned")) {
			      
			        	status = "Paying: Demon";
				        for (int i = 0; i < 13 && !interfaces.getComponent(1188, 3).isValid(); i++) { 
				        	interfaces.getComponent(1184, 21).doClick(true);
				        	sleep(1000, 1200);
				        }
			        
			        if (interfaces.getComponent(1188, 3).isValid()) {
				        for (int i = 0; i < 13 && !interfaces.getComponent(1184, 21).isValid(); i++) { 
				          	interfaces.getComponent(1188, 3).doClick(true);
				        	sleep(1000, 1200);
				        }
			        }
			        
			        if (interfaces.getComponent(1184, 21).isValid()) {
				        for (int i = 0; i < 13 && interfaces.getComponent(1184, 21).isValid(); i++) { 
				          	interfaces.getComponent(1184, 21).doClick(true);
				        	sleep(1000, 1200);
				        }
				        return 0;
			        }
			        }
			        }

			        // Fetch Another
			        
			        else if (interfaces.getComponent(1188, 3).isValid()) {
			        	
			        if (interfaces.getComponent(1188, 3).getText().contains("Fetch another 26 oak planks")) {
			        	
			        	status = "Fetching: Another Oak Planks";
			        	     	
				        for (int i = 0; i < 13 && interfaces.getComponent(1188, 3).isValid(); i++) { 
				        	interfaces.getComponent(1188, 3).doClick(true);
				        	sleep(1000, 1200);
				        }
				        for (int i = 0; i < 13 && npcs.getNearest(Demon) != null; i++) { 
				        	sleep(50);
				        }
				        for (int i = 0; i < 1000000000 && npcs.getNearest(Demon) == null; i++) { 
				        	sleep(50);
				        }
				        for (int i = 0; i < 13 && inventory.getCount(OakPlank) < 8; i++) { 
				        	sleep(50);
				        }
				        return 0;
			        }
			        
			        //Oak Planks 1
			        
			        else if (interfaces.getComponent(1188, 24).getText().contains("Oak planks")) {
			        	 
				        	interfaces.getComponent(1188, 24).doClick(true);
				        	sleep(1000, 1200);
				        
				        	keyboard.sendText("26", true);
				        
				        for (int i = 0; i < 13 && npcs.getNearest(Demon) != null; i++) { 
				        	sleep(50);
				        }
				        for (int i = 0; i < 1000000000 && npcs.getNearest(Demon) == null; i++) { 
				        	sleep(50);
				        }
				        return 0;
			        }
			        
                    //Oak Planks 2
			        
			        else if (interfaces.getComponent(1188, 28).getText().contains("Oak planks")) {
			        	 
				        	interfaces.getComponent(1188, 28).doClick(true);
				        	sleep(1000, 1200);
				        
				        	keyboard.sendText("26", true);
				        
				        for (int i = 0; i < 13 && npcs.getNearest(Demon) != null; i++) { 
				        	sleep(50);
				        }
				        for (int i = 0; i < 1000000000 && npcs.getNearest(Demon) == null; i++) { 
				        	sleep(50);
				        }
						return 0;
			        }
			        else {
			        	return 0;
			        }
			        }
					return 0;
				}
				else if (!DemonBut.isOnScreen()) {
					camera.turnTo(DemonBut);
					camera.setPitch(DemonBut.getHeight());
					return 0;
				}
			}	
		}
		else  if (inventory.getItem(OakPlank) != null) {
			      if (inventory.getCount(OakPlank) >= 8) {
			    	  
			      status = "Building: Oak Larder";	  
			    	  
			      RSObject le = objects.getNearest(EmptyLarder);
			      RSObject lb = objects.getNearest(LarderBuilt);
			      
				  	 if (le != null) {
				  		 if (le.isOnScreen()) {
				  			 
				  	         for (int i = 0; i < 13 && !interfaces.get(394).getComponent(11).isValid(); i++) {
				  	        	 le.interact("Build");
				  	        	 sleep(1200, 1500);
				  	         }
				  	         
				  	         if (interfaces.get(394).getComponent(11).isValid()) {
				  	  	         for (int i = 0; i < 13 && interfaces.get(394).getComponent(11).isValid() && players.getMyPlayer().getAnimation() != 3676; i++) {
				  	  	        	interfaces.get(394).getComponent(11).getComponent(1).doClick(true);
				  	  	        	sleep(500, 800);
				  	  	         }
				  	  	         Made += 1;
				  	  	         for (int i = 0; i < 13 && players.getMyPlayer().getAnimation() != -1; i++) {
				  	  	        	sleep(50);
				  	  	         }
				  	         }
				  	         
				  	         return 0;
				  		 }
				  		 else if (!le.isOnScreen()) {
				  			 camera.turnTo(le);
				  			 return 0; 
				  		 }
				  	 } 
				  	 else if (lb != null) {
				  		 if (lb.isOnScreen()) {
				  	         for (int i = 0; i < 13 && !interfaces.getComponent(1188, 3).isValid(); i++) {
				  	        	 lb.interact("Remove");
				  	        	 sleep(1200, 1500);
				  	         }
				  	         
				  	         if (interfaces.getComponent(1188, 3).isValid()) {
				  	  	         for (int i = 0; i < 13 && interfaces.getComponent(1188, 3).isValid() && players.getMyPlayer().getAnimation() != 3685; i++) {
				  	  	        	interfaces.getComponent(1188, 3).doClick(true);
				  	  	         	sleep(500, 800);
				  	  	         }
				  	  	         for (int i = 0; i < 13 && players.getMyPlayer().getAnimation() != -1; i++) {
				  	  	        	sleep(50);
				  	  	         }
				  	         }
				  	         return 0;
				  		 }
				  		 else if (!lb.isOnScreen()) {
				  			 camera.turnTo(lb);
				  			 return 0; 
				  		 }
				  	 }  
			      }
		}					
		return 0;	
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
				 
			private static final Color MOUSE_COLOR = new Color(132, 198, 99),
		            MOUSE_BORDER_COLOR = new Color(225, 200, 25),
		            MOUSE_CENTER_COLOR = new Color(168, 9, 9);
		    private boolean pressed = false;
		    private int absoluteY = 0;
		    
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
		    
			@Override
			public void onRepaint(Graphics g1) {
				Graphics2D g = (Graphics2D)g1;
				
				long millis = System.currentTimeMillis() - startTime;
				long hours = millis / (1000 * 60 * 60);
				millis -= hours * (1000 * 60 * 60);
				long minutes = millis / (1000 * 60);
				millis -= minutes * (1000 * 60);
				long seconds = millis / 1000;
				
				MadeHour = (int) ((Made) * 3600000D / (System.currentTimeMillis() - startTime));
				
				g.setColor(color1);
				g.fillRect(7, 345, 505, 128);
				g.setColor(color2);
				g.setStroke(stroke1);
				g.drawRect(7, 345, 505, 128);
				g.setColor(color3);
				g.fillRect(9, 461, 500, 8);
				g.setColor(color2);
				g.setStroke(stroke2);
				g.drawRect(9, 461, 500, 8);
				g.setFont(font1);
				g.setColor(color4);
				g.drawString("Status: " + status, 16, 370);
				g.drawString("Time Running: " + hours + " : " + minutes + " : " + seconds, 52, 409); 
				g.drawString("Built: " + Made + " (" + MadeHour + "/ hr)", 52, 453);
			    g.drawString("Level: " + skills.getCurrentLevel(skills.CONSTRUCTION) , 255, 453);
				
		        g.drawImage(img1, 395, 182, null);
		        g.drawImage(img2, 12, 427, null);
		        g.drawImage(img3, 11, 387, null);

				g.setColor(color1);
				drawMouse(g);	
				
				  if (npcs.getNearest(Demon) != null) {
			    	    RSTile RevTile = npcs.getNearest(Demon).getLocation();
			            highlightTile(g, RevTile, Color.blue, new Color(0, 0, 0, 125), npcs.getNearest(Demon).getName());
			            highlightMap(g, RevTile, Color.blue, new Color(0, 0, 0, 125));	
			        }
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
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
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void messageReceived(MessageEvent m) {
				// TODO Auto-generated method stub
			    }
			}
