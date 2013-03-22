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
import com.rarebot.script.methods.Skills;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSObject;

@ScriptManifest(name = "OldsGreenBodies", version = 0.3, description = "Makes Green DragonHide Bodies", authors = "Oldrs4ever")
public class OldsGreenBodies extends Script implements PaintListener, MessageListener
{
 
  public long startTime = 0;  
	public long millis = 0;  
	public long hours = 0;  
	public long minutes = 0;  
	public long seconds = 0;  
	public long last = 0;
	public int expGained = 0;
	public int expHour = 0;
	int startExp;
	public int Bodies = 0;

	
	 int greenLeather = 1745;
	 int bankChest = 42192;
	 int greenbody = 1135;
	 int thread = 1734;
 
	 public void Bank()
	 {
		 RSObject Chest = objects.getNearest(bankChest);
		    if(!inventory.contains(greenLeather))
		    {
		    	if (Chest != null && getMyPlayer().getAnimation() ==  -1)
		    	{
				camera.turnTo(Chest);
				Chest.interact("Use Bank chest");
				sleep(random(200,300));
	                if (bank.isOpen())
	                {
	                	bank.depositAllExcept(thread);
	                    if (bank.getItem(greenLeather) != null)
	                    {
	                    	sleep(random(200,300));
	                    	bank.withdraw(greenLeather, 27);
	                    	sleep(random(500, 800));
		                    bank.close();
	                    }
	                }
		    	}
		    }
	 }
	
	
	public void doCraft()
	{
		{
			final RSItem leather = inventory.getItem(greenLeather);
			leather.interact("Craft");
			sleep(random(50,100));
			{
			    	if (interfaces.getComponent(905, 14).isValid())
			    	{
			    		interfaces.getComponent(905, 14).doClick(true);
			    		sleep(15000,16000);
			    	}
		
				}
			}
		}
	public void useNeedle()
	{
		if (interfaces.getComponent(1179, 11).isValid())
		{
	    	interfaces.getComponent(1179, 11).doClick(true);
		}
	}

	@Override
	public int loop()
	{
		try
		{
			Bank();
			if (inventory.contains(greenLeather) && getMyPlayer().getAnimation() ==  -1)
			{
				doCraft();
			}
			if(inventory.containsOneOf(24154))
			{
				inventory.getItem(24154).doClick(true);
			}
		}catch(NullPointerException np)
		{}
		return 0;
	}
	
	
	public void messageReceived(MessageEvent e)
	{    String x = e.getMessage().toLowerCase();    
		if (x.contains("you make a green dragonhide body."))
		{     
			Bodies++;
		}
	}
	

	public boolean onStart()
	{
		log(new Color(125,38,205), "Welcome to OldsGreenBodies by Oldrs4ever!");
        startTime = System.currentTimeMillis();
        mouse.setSpeed(random(4, 6));
        startExp = skills.getCurrentExp(Skills.CRAFTING);
        return true;
	}

	
	
	//START: Code generated using Enfilade's Easel
    private final Color color1 = new Color(0, 0, 0, 204);
    private final Color color2 = new Color(32, 32, 32, 229);
    private final Color color3 = new Color(24, 79, 20);
    private final Color color4 = new Color(34, 224, 21);

    private final BasicStroke stroke1 = new BasicStroke(1);

    private final Font font1 = new Font("Arial", 0, 16);
    private final Font font2 = new Font("Arial", 0, 9);

    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;
        
        millis = System.currentTimeMillis() - startTime;
        hours = millis / (1000 * 60 * 60);
        millis -= hours * (1000 * 60 * 60);
        minutes = millis / (1000 * 60);
        millis -= minutes * (1000 * 60);
        seconds = millis / 1000;
        expGained = skills.getCurrentExp(Skills.CRAFTING) - startExp;
        expHour = (int) ((expGained) * 3600000D / (System.currentTimeMillis() - startTime));
        
        g.setColor(color1);
        g.fillRoundRect(345, 227, 173, 111, 16, 16);
        g.setColor(color2);
        g.setStroke(stroke1);
        g.drawRoundRect(345, 227, 173, 111, 16, 16);
        g.setFont(font1);
        g.setColor(color3);
        g.drawString("OldsGreenBodies", 351, 246);
        g.setFont(font2);
        g.setColor(color4);
        g.drawString("Time Running:"  + hours +":"+ minutes + ":" + seconds, 352, 264);
        g.drawString("Bodies Crafted:" + Bodies , 351, 285);
        g.drawString("Xp Gained:" + expGained , 351, 303);
        g.drawString("Xp Per Hour:"  + expHour, 351, 325);
    }
    //END: Code generated using Enfilade's Easel
	
}
