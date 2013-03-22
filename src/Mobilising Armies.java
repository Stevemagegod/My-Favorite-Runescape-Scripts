import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import com.rarebot.event.listeners.PaintListener;
import com.rarebot.script.Script;
import com.rarebot.script.ScriptManifest;
import com.rarebot.script.methods.Game;
import com.rarebot.script.util.Filter;
import com.rarebot.script.util.Timer;
import com.rarebot.script.wrappers.RSArea;
import com.rarebot.script.wrappers.RSComponent;
import com.rarebot.script.wrappers.RSInterface;
import com.rarebot.script.wrappers.RSItem;
import com.rarebot.script.wrappers.RSNPC;
import com.rarebot.script.wrappers.RSObject;
import com.rarebot.script.wrappers.RSPlayer;
import com.rarebot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Swell" }, keywords = { "ma"}, name = "MA Bot", version = 0.41, description = "Does the MA minigame.")
public class SwellMA extends Script implements PaintListener, MouseListener, MouseMotionListener {

  private final ScriptManifest script = getClass().getAnnotation(ScriptManifest.class);
	private final Methods methods = new Methods();
	private boolean exchangeCredits, needResupply, counted, breaking, showPaint = true, fadePaint, doubleCheck = true, shouldBreak = false;
	private int battleCount, ranksGained, ranksCheck, fade = 255, whichRecruiter = random(0, 3);
	private Timer runTime = new Timer(0), breakTimer, failSafe, breakLength;
	private Rectangle paintArea = new Rectangle(6, 344, 506, 128), togglePaint = new Rectangle(493, 347, 16, 15);
	private final RenderingHints rh = new RenderingHints(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
	private BufferedImage pic = null;
	private Image faded;
	private AlphaFilter f;
	private FilteredImageSource fis;

	/*TODO
	 * Attacking different parts of wall (may be quite difficult because of the inaccurate object model)
	 * Special unit support (uses more credits in exchange for faster games)
	 * Support for exchanging different items (even though rune chain is best...)
	 * */

	public boolean onStart() {
		if (!game.isLoggedIn() || !methods.areas.inMA()) {
			log.severe("Please start logged in and at Mobilising Armies.");
			return false;
		}
		if (!methods.invent.hasContracts()) {
			log.severe("Please start with 10 contracts in your inventory.");
			return false;
		}
		try {
			pic = ImageIO.read(new URL("http://i.imgur.com/c7CUR.png"));
		} catch (Exception e) {
			e.printStackTrace();
			log.warning("Failed to load the image.");
		}
		f = new AlphaFilter();
		f.setLevel(255);
		fis = new FilteredImageSource(pic.getSource(), f) ;
		fadePicture();
		log("Breaking removed until RSBot fix the login random.");
		//breakTimer = new Timer(random(30, 150)*random(55, 65)*random(900, 1100));
		failSafe = new Timer(random(24, 35)*random(55, 65)*random(900, 1100));
		mouse.setSpeed(random(5, 8));
		ranksCheck = 0;
		sleep(500);
		log(Constants.START_MESSAGE);
		return true;
	}

	public int loop() {
		if (!game.isLoggedIn()) {
			return random(500, 1000);
		}

		if (!failSafe.isRunning()) {
			if (ranksCheck == ranksGained) {
				log.severe("Haven't gained any ranks in " + format(failSafe.getElapsed()));
				if (methods.areas.inMA() || methods.areas.inWaitingRoom() || methods.areas.inFinishedRoom()) {
					log("Location: " + getMyPlayer().getLocation().toString());
					stopScript(true);
				} else {
					log("Mouse loc: " + mouse.getLocation().toString() + " --- Tab: " + methods.inGame.getCurrentTab());
					return -1;
				}
			}
			ranksCheck = ranksGained;
			failSafe = new Timer(random(24, 35)*random(55, 65)*random(900, 1100));
		}

		if (shouldBreak && !breakTimer.isRunning() && methods.areas.inMA()) {
			methods.waitWhileMoving();
			RSInterface credit = interfaces.get(Constants.Interfaces.CREDIT_EXCHANGE),
			squad = interfaces.get(Constants.Interfaces.SQUAD_MANAGEMENT),
			selection = interfaces.get(Constants.Interfaces.SCENARIO_SELECTION),
			options = interfaces.get(Constants.Interfaces.SCENARIO_OPTIONS),
			investment = interfaces.get(Constants.Interfaces.SCENARIO_INVESTMENT);
			if (credit.isValid()) {
				methods.iface.get(credit, Constants.Interfaces.CREDIT_EXCHANGE_CLOSE).doClick();
				for (int i = 0; credit.isValid(); i++) {
					sleep(100, 200);
					if (i > 15)
						return 0;
				}
			} else if (squad.isValid()) {
				methods.iface.get(squad, Constants.Interfaces.SQUAD_MANAGEMENT_CLOSE).doClick();
				for (int i = 0; squad.isValid(); i++) {
					sleep(100, 200);
					if (i > 15)
						return 0;
				}
			} else if (selection.isValid()) {
				methods.iface.get(selection, Constants.Interfaces.SCENARIO_SELECTION_CLOSE).doClick();
				for (int i = 0; selection.isValid(); i++) {
					sleep(100, 200);
					if (i > 15)
						return 0;
				}
			} else if (options.isValid()) {
				methods.iface.get(options, Constants.Interfaces.SCENARIO_OPTIONS_CLOSE).doClick();
				for (int i = 0; options.isValid(); i++) {
					sleep(100, 200);
					if (i > 15)
						return 0;
				}
			} else if (investment.isValid()) {
				methods.iface.get(investment, Constants.Interfaces.SCENARIO_INVESTMENT_CLOSE).doClick();
				for (int i = 0; credit.isValid(); i++) {
					sleep(100, 200);
					if (i > 15)
						return 0;
				}
			} else if (bank.isOpen()) {
				bank.close();
				for (int i = 0; bank.isOpen(); i++) {
					sleep(100, 200);
					if (i > 15)
						return 0;
				}
			}
			game.logout(true);
			breakLength = new Timer(random(200, 1897)*random(900, 1100));
			breaking = true;
			log("Taking break for " + format(breakLength.getRemaining()));
			while (breakLength.isRunning())
				sleep(50, 100);
			breakTimer = new Timer(random(30, 150)*random(55, 65)*random(900, 1100));
			failSafe.reset();
			breaking = false;
			return random(500, 1000);
		}

		if (methods.areas.inMA() && !needResupply)
			needResupply = methods.invent.needResupply();

		if (exchangeCredits) {
			if (inventory.contains(Constants.Items.RUNE_CHAINBODY)) {
				RSInterface exchange = interfaces.get(Constants.Interfaces.CREDIT_EXCHANGE);
				if (exchange.isValid()) {
					RSComponent value = methods.iface.get(exchange, Constants.Interfaces.CREDIT_EXCHANGE_VALUE);
					if (value.getText().equals("0")) {
						if (inventory.getItem(Constants.Items.RUNE_CHAINBODY).doAction("Offer All")) {
							for (int i = 0; value.getText().equals("0"); i++) {
								sleep(100, 200);
								if (i > 20)
									return 0;
							}
						}
					} else {
						RSComponent btn = methods.iface.get(exchange, Constants.Interfaces.CREDIT_EXCHANGE_EXCHANGE);
						if (btn.doClick()) {
							sleep(1000, 2000);
							RSComponent confirm = methods.iface.get(exchange, Constants.Interfaces.CREDIT_EXCHANGE_CONFIRM);
							if (confirm.doClick()) {
								for (int i = 0; !value.getText().equals("0"); i++) {
									sleep(100, 200);
									if (i > 20)
										return 0;
								}
								RSComponent close = methods.iface.get(exchange, Constants.Interfaces.CREDIT_EXCHANGE_CLOSE);
								if (random(0, 3) == 1) {
									close.doClick();
									sleep(100, 1000);
								}
								exchangeCredits = false;
							}
						}
					}
				} else {
					RSNPC mal = npcs.getNearest(Constants.NPCs.MAL);
					if (mal.isOnScreen()) {
						mal.doAction("Invest");
						methods.waitWhileMoving();
						for (int i = 0; !exchange.isValid(); i++) {
							sleep(100, 200);
							if (i > 10)
								return 0;
						}
					} else {
						walking.walkTileMM(mal.getLocation(), 2, 2);
						methods.waitWhileMoving();
						if (random(0, 4) == 2)
							camera.turnTo(mal, 10);
					}
				}
			} else {
				if (calc.tileOnScreen(Constants.Tiles.BANK)) {
					if (!bank.isOpen()) {
						bank.open();
						for (int i = 0; !bank.isOpen(); i++) {
							sleep(100, 200);
							if (i > 25)
								return 0;
						}
						return random(50, 100);
					}
					RSItem chain = bank.getItem(Constants.Items.RUNE_CHAINBODY);
					if (chain != null) {
						bank.withdraw(Constants.Items.RUNE_CHAINBODY, 10);
						for (int i = 0; !inventory.contains(Constants.Items.RUNE_CHAINBODY); i++) {
							sleep(100, 200);
							if (i > 10)
								return 0;
						}
					} else {
						log.severe("Could not find rune chainbody in your bank.");
						log.severe("You're out of credits and items to exchange for them.");
						stopScript(true);
					}
				} else {
					if (calc.tileOnMap(Constants.Tiles.BANK)) {
						walking.walkTileMM(Constants.Tiles.BANK, 2, 2);
					} else {
						walking.walkTileMM(Constants.Tiles.MIDDLE, 2, 2);
					}
					methods.waitWhileMoving();
					return random(100, 200);
				}
			}
		} else if (methods.areas.inGame()) {
			if (battleCount != 10) {
				if (!methods.inGame.wallOnScreen()) {
					String t = methods.inGame.getTeam();
					if (t.equals("Red")) {
						methods.inGame.cam.move(Constants.Keys.UP);
					} else if (t.equals("Blue")) {
						methods.inGame.cam.move(Constants.Keys.DOWN);
					} else if (t.equals("Green")) {
						methods.inGame.cam.move(Constants.Keys.RIGHT);
					} else if (t.equals("Yellow")) {
						methods.inGame.cam.move(Constants.Keys.LEFT);
					}
				} else {
					if (methods.inGame.selectSquad(battleCount) == 1)
						battleCount++;
					if (methods.inGame.attackCastle()) {
						battleCount++;
					}
				}
			} else {
				if (doubleCheck) {
					battleCount = 0;
					doubleCheck = false;
					return random(1000, 3000);
				}
				if (random(0, 13) == 3) {
					log("Antiban InGame: Move mouse.");
					mouse.moveSlightly();
				}
				if (random(0, 38) == 14) {
					log("Antiban InGame: Open random tab.");
					int r = 130;
					while (r == 130 || r == methods.inGame.getCurrentTab()) {
						r = random(129, 134);
					}
					methods.inGame.openTab(r);
				}
				if (random(0, 63) == 18) {
					log("Antiban InGame: Move game camera.");
					methods.antiban.gameCamera();
					mouse.moveSlightly();
				}
				if (random(0, 108) == 41) {
					int rnd = random(0, 10), c = 0;
					try {
						while (methods.inGame.getAliveSquads()[rnd] == 0) {
							rnd = random(0, 10);
							if (c++ > 50)
								return random(500, 1000);
						}
					} catch (Exception e) {//more efficient to just catch exception
						log("exception");
						return random(500, 1000);
					}
					log("Antiban InGame: Select random squad.");
					methods.inGame.selectSquad(rnd);
				}
				return random(500, 1000);
			}
		} else if (methods.areas.inWaitingRoom()) {
			if (random(0, 8) == 1) {
				log("Antiban WaitingRoom: Move mouse.");
				mouse.moveSlightly();
			}
			if (random(0, 43) == 26) {
				log("Antiban WaitingRoom: Rotate camera.");
				camera.setAngle(random(0, 361));
			}
			if (random(0, 33) == 17)
				methods.antiban.clickPlayer();
			if (random(0, 22) == 14)
				methods.antiban.moveTiles();
			return random(200, 1500);
		} else if (methods.areas.inFinishedRoom()) {
			RSInterface info = interfaces.get(Constants.Interfaces.GAME_END_INFO);
			if (info.isValid()) {
				RSComponent close = methods.iface.get(info, Constants.Interfaces.GAME_END_INFO_CLOSE), 
				msg = methods.iface.get(info, Constants.Interfaces.GAME_END_INFO_MESSAGE);
				if (!counted) {
					if (msg.containsText("gained a"))
						ranksGained++;
					else if (msg.containsText("gained 2"))
						ranksGained += 2;
					counted = true;
				}
				if (close.doClick()) {
					for (int i = 0; info.isValid(); i++) {
						sleep(100, 200);
						if (i > 15)
							return 0;
					}
					return random(200, 500);
				}
			}
			RSObject ladder = objects.getNearest(Constants.Objects.LADDER_FINISH_ROOM);
			if (!ladder.isOnScreen()) {
				camera.turnTo(ladder, 10);
				sleep(200, 400);
			}
			if (!ladder.isOnScreen()) {
				walking.walkTileOnScreen(new RSTile(ladder.getLocation().getX()+1, ladder.getLocation().getY()-3));
				methods.waitWhileMoving();
			}
			if (ladder.doAction("Climb-up")) {
				methods.waitWhileMoving();
				for (int i = 0; methods.areas.inFinishedRoom(); i++) {
					sleep(100, 200);
					if (i > 40)
						return 0;
				}
				return random(1000, 2000);
			}
		} else if (needResupply) {
			if (methods.areas.inRecruit()) {
				RSInterface management = interfaces.get(Constants.Interfaces.SQUAD_MANAGEMENT);
				if (management.isValid()) {
					RSComponent resupply = methods.iface.get(management, Constants.Interfaces.SQUAD_MANAGEMENT_RESUPPLY_ALL);
					if (methods.canAffordResupply()) {
						if (resupply.doClick()) {
							sleep(1000, 1600);
							RSComponent confirm = methods.iface.get(management, Constants.Interfaces.SQUAD_MANAGEMENT_RESUPPLY_ALL_CONFIRM);
							if (confirm.doClick()) {
								sleep(1000, 1600);
								for (int i = 0; methods.iface.get(management, 
										Constants.Interfaces.SQUAD_MANAGEMENT_RESUPPLY_ALL_COST).containsText("<br>"); i++) {
									sleep(100, 200);
									if (i > 30)
										return 0;
								}
								RSComponent close = methods.iface.get(management, Constants.Interfaces.SQUAD_MANAGEMENT_CLOSE);
								while (management.isValid()) {
									if (close.doClick()) {
										sleep(1000, 1600);
									}
								}
								sleep(600, 1200);
								needResupply = false;
							}
						}
					} else {
						if (random(0, 3) == 1) {
							RSComponent close = methods.iface.get(management, Constants.Interfaces.SQUAD_MANAGEMENT_CLOSE);
							if (close.doClick()) {
								for (int i = 0; management.isValid(); i++) {
									sleep(100, 200);
									if (i > 15)
										return 0;
								}

							}
						}
						exchangeCredits = true;
					}
				} else {
					RSNPC recruiter = npcs.getNearest(Constants.NPCs.RECRUITERS[whichRecruiter]);
					if (random(0, 3) == 1)
						camera.turnTo(recruiter, 10);
					if (recruiter.isOnScreen()) {
						if (menu.isOpen()) {
							menu.doAction("Close");
							return random(200, 500);
						}
						if (recruiter.doAction("Manage")) {
							methods.waitWhileMoving();
							return random(1000, 1600);
						}
					} else {
						walking.walkTileMM(recruiter.getLocation());
						methods.waitWhileMoving();
					}
				}
			} else {
				if (calc.tileOnMap(Constants.Tiles.RECRUITERS)) {
					walking.walkTileMM(Constants.Tiles.RECRUITERS, 2, 2);
					methods.waitWhileMoving();
				} else {
					walking.walkTileMM(Constants.Tiles.MIDDLE, 4, 4);
					methods.waitWhileMoving();
				}
			}
		} else {
			RSInterface scenarioSelection = interfaces.get(Constants.Interfaces.SCENARIO_SELECTION), 
			scenarioOptions = interfaces.get(Constants.Interfaces.SCENARIO_OPTIONS), 
			scenarioInvestment = interfaces.get(Constants.Interfaces.SCENARIO_INVESTMENT);
			if (scenarioSelection.isValid()) {
				RSComponent siege = methods.iface.get(scenarioSelection, Constants.Interfaces.SCENARIO_SELECTION_SIEGE);
				if (siege.doClick()) {
					return random(1500, 2000);
				}
			} else if (scenarioOptions.isValid()) {
				RSComponent next = methods.iface.get(scenarioOptions, Constants.Interfaces.SCENARIO_OPTIONS_NEXT);
				if (next.doClick()) {
					return random(1500, 2000);
				}
			} else if (scenarioInvestment.isValid()) {
				RSComponent all = methods.iface.get(scenarioInvestment, Constants.Interfaces.SCENARIO_INVESTMENT_ALL), 
				play = methods.iface.get(scenarioInvestment, Constants.Interfaces.SCENARIO_INVESTMENT_PLAY), 
				investment = methods.iface.get(scenarioInvestment, Constants.Interfaces.SCENARIO_INVESTMENT_INVESTMENT);
				if (methods.canAffordInvest() || !methods.canAffordInvest() && !investment.getText().equals("0")) {
					if (investment.getText().equals("0")) {
						if (all.doClick()) {
							for (int i = 0; investment.getText().equals("0"); i++) {
								sleep(100, 200);
								if (i > 25)
									return 0;
							}
							return random(200, 400);
						}
					}
					methods.antiban.previouslyClicked = new String[0];
					battleCount = 0;
					counted = false;
					doubleCheck = true;
					whichRecruiter = random(0, 3);
					if (play.doClick()) {
						for (int i = 0; !methods.areas.inWaitingRoom(); i++) {
							sleep(100, 200);
							if (i > 25)
								return 0;
						}
					}
				} else {
					RSComponent close = methods.iface.get(scenarioInvestment, Constants.Interfaces.SCENARIO_INVESTMENT_CLOSE);
					if (random(0, 4) == 2) {
						close.doClick();
						sleep(400, 820);
					}
					exchangeCredits = true;
				}
			} else if (methods.areas.inMA() && !methods.invent.hasContracts()) {
				log.severe("May have lost contracts, could be caused by lag lets wait.");
				sleep(4000, 6000);
				if (!methods.invent.hasContracts()) {
					log.severe("Lost contracts, shutting down.");
					log.severe(getMyPlayer().getLocation().toString());
					stopScript(true);
				} else {
					log("Few we didn't lose them :P");
				}
			} else {
				RSObject ladder = objects.getNearest(Constants.Objects.LADDER_BRIEFING_ROOM);
				if (ladder == null) {
					return random(500, 1000);
				}
				if (ladder.isOnScreen()) {
					if (ladder.doAction("Climb-down")) {
						methods.waitWhileMoving();
						for (int i = 0; !interfaces.get(Constants.Interfaces.SCENARIO_SELECTION).isValid(); i++) {
							sleep(100, 200);
							if (i > 30)
								return 0;
						}
						return random(300, 600);
					}
				} else {
					walking.walkTileMM(Constants.Tiles.START, 2, 2);
					methods.waitWhileMoving();
					if (random(0, 3) == 2)
						camera.turnTo(ladder, 10);
				}
			}
		}
		return random(50, 100);
	}

	public void onRepaint(Graphics g2) {
		Graphics2D g = (Graphics2D) g2;
		g.setRenderingHints(rh);

		if (fadePaint) {
			if (fade > 50) {
				fade -= 3;
				fadePicture();
			}
		} else {
			if (fade < 255) {
				fade += 3;
				fadePicture();
			}
		}

		if (showPaint) {
			g.setColor(new Color(194, 178, 146, fade));
			g.fillRect(6, 344, 506, 128);
			g.setColor(new Color(49, 42, 27, 255));
			g.drawRect(6, 344, 506, 128);
			g.setColor(new Color(108,107,107, fade));
			g.drawRect(9, 347, 390+17, 22);
			g.setColor(new Color(184, 170, 142, fade));
			g.drawRect(10, 348, 388+17, 20);	
			g.setColor(new Color(214, 196, 160, fade));
			g.fillRect(12, 350, 385+17, 17);

			g.setColor(new Color(168,9,9, fade));
			g.setFont(new Font("Arial", Font.BOLD, 12));
			g.drawString("Time Running:", 12, 385);
			g.drawString("Ranks Gained:", 12, 401);
			if (shouldBreak) {
				if (!breaking) {
					g.drawString("Next break:", 12, 417);
				} else {
					g.drawString("Breaking for:", 12, 417);
				}
			}

			g.setColor(new Color(0,0,0, fade));
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			g.drawString(runTime.toElapsedString(), 100, 385);
			g.drawString(ranksGained + " (" + ranksGained * 3600 / (runTime.getElapsed()/1000) + "hr)", 100, 401);
			if (shouldBreak) {
				if (!breaking) {
					g.drawString(Timer.format(breakTimer.getRemaining()), 100, 417);
				} else {
					g.drawString(Timer.format(breakLength.getRemaining()), 100, 417);
				}
			}

			g.setFont(new Font("Arial", Font.PLAIN, 9));
			g.drawString("Swell MA Bot v" + script.version(), 435, 468);
			g2.drawImage(faded, 300, 370, null);
		}
		g.setColor(showPaint ? new Color(168,9,9, fade) : new Color(40,130,0, fade));
		g.fillRect(493, 347, 16,15);
		g.setColor(showPaint ? new Color(102,0,0, fade) : new Color(32,104,0, fade));
		g.drawRect(493, 347, 16,15);
		g.setColor(new Color(255, 255, 255, fade));
		g.setFont(new Font("Arial", Font.BOLD, 14));
		g.drawString(showPaint ? "-": "¤", 497, 360);
		drawMouse(g2);
	}

	public void drawMouse(final Graphics g) {
		Point m = mouse.getLocation();
		if (mouse.isPressed()) {
			g.setColor(new Color(255, 252, 0, 150));
			g.fillOval(m.x - 5, m.y - 5, 10, 10);
			g.setColor(new Color(0, 0, 0, 225));
			g.drawOval(m.x - 5, m.y - 5, 10, 10);
			g.setColor(new Color(255, 252, 0, 100));
		} else
			g.setColor(new Color(255, 252, 0, 50));

		g.drawLine(0, m.y, 766, m.y);
		g.drawLine(m.x, 0, m.x, 505);

		g.setColor(new Color(0, 0, 0, 50));
		g.drawLine(0, m.y+1, 766, m.y+1);
		g.drawLine(0, m.y-1, 766, m.y-1);
		g.drawLine(m.x+1, 0, m.x+1, 505);
		g.drawLine(m.x-1, 0, m.x-1, 505);
	}

	public void mouseClicked(MouseEvent m) {
		if (togglePaint.contains(m.getPoint())) {
			showPaint = !showPaint;
		}
	}

	public void mouseMoved(MouseEvent m) {
		if (paintArea.contains(m.getPoint()) && !togglePaint.contains(m.getPoint())) {
			if (showPaint) {
				fadePaint = true;
			} else {
				fadePaint = false;
			}
		} else {
			fadePaint = false;
		}
	}

	public void mouseEntered(MouseEvent m) {}
	public void mouseExited(MouseEvent m) {}
	public void mouseDragged(MouseEvent m) {}
	public void mousePressed(MouseEvent m) {}
	public void mouseReleased(MouseEvent m) {}

	private String format(long time) {
		String formatted = "";
		int left = (int) time;
		int h = left/1000/60/60;
		left -= h*1000*60*60;
		int m = (int) left/1000/60;
		left -= m*1000*60;
		int s = (int) left/1000;
		if (h > 0) {
			formatted += h +" hours ";
		} else if (m > 0) {
			formatted += m + " minutes ";
		} else if (s > 0) {
			formatted += s + " seconds";
		}
		return formatted;
	}

	public void fadePicture() {
		f.setLevel(fade);
		if (faded != null)
			faded.flush();
		faded = Toolkit.getDefaultToolkit().createImage(fis);
	}

	public void onFinish() {
		env.saveScreenshot(true);
		log(Constants.FINISH_MESSAGE);
	}

	private class AlphaFilter extends RGBImageFilter {
		private int level;

		public AlphaFilter() {
			canFilterIndexColorModel = true;
		}

		public void setLevel(int lev) {
			level = lev;
		}

		public int filterRGB(int x, int y, int rgb) {
			int a = level * 0x01000000;
			return (rgb &   0x00ffffff) | a;
		}
	}

	private static final class Constants {

		private static final String START_MESSAGE = "Welcome to MA bot by Swell.";
		private static final String FINISH_MESSAGE = "Thanks for using MA bot by Swell.";
		/*private static final int PRICE_CHINCHOMPA = 500;
		private static final int PRICE_CANNON = 300;*/

		private static final class Interfaces {
			private static final int SCENARIO_SELECTION = 655;
			private static final int SCENARIO_SELECTION_CLOSE = 17;
			private static final int SCENARIO_SELECTION_SIEGE = 29;

			private static final int SCENARIO_OPTIONS = 291;
			private static final int SCENARIO_OPTIONS_CLOSE = 25;
			private static final int SCENARIO_OPTIONS_NEXT = 31;

			private static final int SCENARIO_INVESTMENT = 852;
			private static final int SCENARIO_INVESTMENT_CLOSE = 37;
			private static final int SCENARIO_INVESTMENT_PLAY = 74;
			private static final int SCENARIO_INVESTMENT_CREDITS = 62;
			private static final int SCENARIO_INVESTMENT_INVESTMENT = 63;
			private static final int SCENARIO_INVESTMENT_ALL = 45;
			private static final int SCENARIO_INVESTMENT_LIMIT = 8;
			//private static final int SCENARIO_INVESTMENT_CUSTOM = 55;

			private static final int STATUS_WAITING_ROOM = 46;

			private static final int CREDIT_EXCHANGE = 862;
			//private static final int CREDIT_EXCHANGE_CREDITS = 6;
			private static final int CREDIT_EXCHANGE_VALUE = 8;
			private static final int CREDIT_EXCHANGE_EXCHANGE = 4;
			private static final int CREDIT_EXCHANGE_CONFIRM = 46;
			private static final int CREDIT_EXCHANGE_CLOSE = 27;

			/*private static final int SPECIAL_UNITS = 654;
			private static final int SPECIAL_UNITS_CLOSE = 36;
			private static final int SPECIAL_UNITS_CHINCHOMPA_ADD = 55;
			private static final int SPECIAL_UNITS_CANNON_ADD = 15;
			private static final int SPECIAL_UNITS_BUY = 39;
			private static final int SPECIAL_UNITS_CREDITS = 77;*/

			private static final int SQUAD_MANAGEMENT = 292;
			private static final int SQUAD_MANAGEMENT_RESUPPLY_ALL = 119;
			private static final int SQUAD_MANAGEMENT_RESUPPLY_ALL_COST = 124;
			private static final int SQUAD_MANAGEMENT_RESUPPLY_ALL_CONFIRM = 137;
			private static final int SQUAD_MANAGEMENT_CREDITS = 128;
			private static final int SQUAD_MANAGEMENT_CLOSE = 13;

			private static final int GAME_STATUS = 37;
			private static final int GAME_STATUS_TEAM = 33;
			//private static final int GAME_STATUS_TIME_LEFT = 32;

			/*private static final int GAME_CAMERA = 853;
			private static final int GAME_CAMERA_FWD = 10;
			private static final int GAME_CAMERA_BACK = 13;
			private static final int GAME_CAMERA_LEFT = 12;
			private static final int GAME_CAMERA_RIGHT = 11;*/

			private static final int GAME_INTERFACE = 548;
			//private static final int GAME_TAB_CAMERA = 129;
			private static final int GAME_TAB_SQUAD_COMMANDS = 130;
			//private static final int GAME_TAB_SPECIAL_UNITS = 131;
			private static final int GAME_TAB_MY_SQUADS = 132;
			//private static final int GAME_TAB_FORFEIT = 133;

			private static final int GAME_SQUADS = 39;
			private static final int[] GAME_SQUADS_STATUS = {71, 65, 51, 38, 45, 33, 27, 15, 21, 9};
			private static final int[] GAME_SQUADS_ACTIVATE = {72, 66, 52, 40, 46, 34, 28, 16, 22, 10};

			private static final int GAME_SQUAD_COMMANDS = 859;
			private static final int GAME_SQUAD_COMMANDS_ACTIVITY = 11;

			private static final int GAME_END_INFO = 857;
			private static final int GAME_END_INFO_CLOSE = 5;
			private static final int GAME_END_INFO_MESSAGE = 8;
		}

		private static final class Keys {
			private static final int F1 = KeyEvent.VK_F1;
			private static final int UP = KeyEvent.VK_UP;
			private static final int DOWN = KeyEvent.VK_DOWN;
			private static final int LEFT = KeyEvent.VK_LEFT;
			private static final int RIGHT = KeyEvent.VK_RIGHT;
		}

		private static final class Objects {
			private static final int LADDER_BRIEFING_ROOM = 43901;
			//private static final int LADDER_WAITING_ROOM = 43790;
			private static final int LADDER_FINISH_ROOM = 43790;
			//private static final int[] TABLE_SPECIAL_UNITS = {43804, 43805};
			private static final int GAME_CASTLE = 43726;
			private static final int GAME_CASTLE_RED = 43727;
			private static final int TABLE_FINISHED_ROOM = 43634;
		}

		private static final class NPCs {
			private static final int[] RECRUITERS = {8946, 8944, 8942};
			private static final int MAL = 8916;
		}

		private static final class Tiles {
			private static final RSTile START = new RSTile(2412, 2827);
			private static final RSTile RECRUITERS = new RSTile(2403, 2827);
			private static final RSTile BANK = new RSTile(2403, 2840);
			private static final RSTile MIDDLE = new RSTile(2412, 2836);
		}

		private static final class Items {
			private static final int RUNE_CHAINBODY = 1113;
		}

		private static final class Points {
			//Location will always be the same, easiest and most accurate way of doing this is with static points
			private static final Point[] RED = {new Point(217, 125), new Point(250, 128), new Point(350, 133)};
			private static final Point[] BLUE = {new Point(220, 220), new Point(274, 226), new Point(347, 222)};
			private static final Point[] GREEN = {new Point(367, 180), new Point(368, 148), new Point(365, 109)};
			private static final Point[] YELLOW = {new Point(190, 150), new Point(185, 190), new Point(190, 110)};

		}
	}

	private class Methods {
		private final Inventory invent = new Inventory();
		private final Areas areas = new Areas();
		private final InGame inGame = new InGame();
		private final Interface iface = new Interface();
		private final Antiban antiban = new Antiban();
		private final Filters filters = new Filters();

		private void waitWhileMoving() {
			sleep(600, 1000);
			while (getMyPlayer().isMoving())
				sleep(100, 200);
		}

		private boolean openTab(int tab) {
			for (int i = 0; game.getCurrentTab() != tab; i++) {
				if (i > 3)
					return false;
				game.openTab(tab);
				sleep(600, 1000);
			}
			return true;
		}

		private boolean canAffordResupply() {
			String cost = interfaces.getComponent(Constants.Interfaces.SQUAD_MANAGEMENT, 
					Constants.Interfaces.SQUAD_MANAGEMENT_RESUPPLY_ALL_COST).getText().substring(12).replace("<br>(", "").replace(")", ""),
					credits = interfaces.getComponent(Constants.Interfaces.SQUAD_MANAGEMENT, 
							Constants.Interfaces.SQUAD_MANAGEMENT_CREDITS).getText().substring(30);
			return !cost.equals("") ? Integer.parseInt(cost) < Integer.parseInt(credits) : false;
		}

		private boolean canAffordInvest() {
			int limit = Integer.parseInt(interfaces.getComponent(Constants.Interfaces.SCENARIO_INVESTMENT, 
					Constants.Interfaces.SCENARIO_INVESTMENT_LIMIT).getText().substring(7).replace(",", "")),
					credits = Integer.parseInt(interfaces.getComponent(Constants.Interfaces.SCENARIO_INVESTMENT, 
							Constants.Interfaces.SCENARIO_INVESTMENT_CREDITS).getText().replace(",", ""));
			return credits > limit;
		}

		private class Interface {
			private RSComponent get(RSInterface iface, int i) {
				return interfaces.getComponent(iface.getIndex(), i);
			}
		}

		private class InGame {
			private final Camera cam = new Camera();

			private int selectSquad(int i) {
				if (openTab(Constants.Interfaces.GAME_TAB_MY_SQUADS)) {
					if (!squadDeceased(i)) {
						RSComponent btn = interfaces.getComponent(Constants.Interfaces.GAME_SQUADS,
								Constants.Interfaces.GAME_SQUADS_ACTIVATE[i]);
						Point loc = btn.getCenter();
						if (btn.isValid()) {
							mouse.move(loc, 5, 5);
							mouse.click(true);
							for (int c = 0; getCurrentTab() != Constants.Interfaces.GAME_TAB_SQUAD_COMMANDS; c++) {
								sleep(100, 200);
								if (c > 30)
									return 2;//wait timeout
							}
							sleep(200, 400);
							return 5;//true
						}
					} else {
						return 1;//squad dead
					}
				}
				return 0;
			}

			private boolean openTab(int tab) {
				for (int i = 0; getCurrentTab() != tab; i++) {
					if (i > 3)
						return false;
					if (tab == 132) {
						keyboard.pressKey((char) Constants.Keys.F1);
						sleep(100, 350);
						keyboard.releaseKey((char) Constants.Keys.F1);
					} else {
						interfaces.getComponent(Constants.Interfaces.GAME_INTERFACE, tab).doClick();
					}
					for (int c = 0; c < 9 && getCurrentTab() != tab; c++) {
						sleep(60, 100);
					}
				}
				return true;
			}

			private int getCurrentTab() {
				int i = 0;
				for (int tab = 129; tab < 133; tab++) {
					if (interfaces.getComponent(Constants.Interfaces.GAME_INTERFACE, tab).getBackgroundColor() != -1)
						i = tab;
				}
				return i;
			}

			private boolean squadDeceased(int i) {
				if (interfaces.getComponent(Constants.Interfaces.GAME_SQUADS, 
						Constants.Interfaces.GAME_SQUADS_STATUS[i]).getBackgroundColor() != -1) {
					return true;
				}
				return false;
			}

			private int[] getAliveSquads() {
				if (openTab(Constants.Interfaces.GAME_TAB_MY_SQUADS)) {
					int[] array = new int[10];
					for (int i = 0; i < 10; i++) {
						if (interfaces.getComponent(Constants.Interfaces.GAME_SQUADS, 
								Constants.Interfaces.GAME_SQUADS_STATUS[i]).getBackgroundColor() == -1) {
							array[i] = 1;
						} else {
							array[i] = 0;
						}
					}
					return array;
				}
				return null;
			}

			private boolean squadSelected() {
				return getCurrentTab() == Constants.Interfaces.GAME_TAB_SQUAD_COMMANDS;
			}

			private String getTeam() {
				return interfaces.getComponent(Constants.Interfaces.GAME_STATUS, Constants.Interfaces.GAME_STATUS_TEAM).getText();
			}

			private boolean attackCastle() {
				if (squadSelected()) {
					if(!interfaces.getComponent(Constants.Interfaces.GAME_SQUAD_COMMANDS, 
							Constants.Interfaces.GAME_SQUAD_COMMANDS_ACTIVITY).containsText("Idle"))
						return true;
					Point p = new Point(-1, -1);
					if (getTeam().equals("Green")) {
						p = Constants.Points.GREEN[random(0, Constants.Points.GREEN.length)];
					} else if (getTeam().equals("Yellow")) {
						p = Constants.Points.YELLOW[random(0, Constants.Points.YELLOW.length)];
					} else if (getTeam().equals("Red")) {
						p = Constants.Points.RED[random(0, Constants.Points.RED.length)];
					} else if (getTeam().equals("Blue")) {
						p = Constants.Points.BLUE[random(0, Constants.Points.BLUE.length)];
					}
					mouse.move(p, 5, 5);
					sleep(100, 200);
					if (menu.getItems()[0].equals("Attack Castle wall")) {
						mouse.click(true);
					} else if (getIndex("Attack Castle wall") != -1) {
						mouse.click(false);
						menu.clickIndex(getIndex("Attack Castle wall"));
					} else {
						return false;
					}
					return true;
				}
				return false;
			}

			public int getIndex(String action) {
				action = action.toLowerCase();
				String[] items = menu.getItems();
				for (int i = 0; i < items.length; i++) {
					if (items[i].toLowerCase().equals(action)) {
						return i;
					}
				}
				return -1;
			}

			/*private Point getWallPoint(RSTile t) {
				Point location = new Point(-1, -1);
				if (getTeam().equals("Green")) {
					location = calc.tileToScreen(t, 0.7, 0.5, 0);
				} else if (getTeam().equals("Yellow")) {
					location = calc.tileToScreen(t, 0.3, 0.5, 0);
				} else {
					location = calc.tileToScreen(t);
				}
				int i;
				topLoop: for (i = 0; i < 20; i += 5) {
					location = new Point((int) location.getX() + i, (int) location.getY());
					mouse.move(location, 5, 5);
					sleep(100, 200);
					for (String s : menu.getActions()) {
						if (s.equals("Attack")) {
							if (i > 0) {
								mouse.move((int)location.getX() + (i+5), (int) location.getY(), 5, 5); 
								sleep(100, 200);
								if (menu.getActions()[0].equals("Attack")) {
									location = new Point((int)location.getX() + (i+5), (int) location.getY());
								}
							}
							break topLoop;
						}
					}
					if (i == 15)
						return new Point(-1, -1);
				}
				return location;
			}*/

			private RSTile myWall() {
				String t = getTeam();
				RSTile o = objects.getNearest(Constants.Objects.GAME_CASTLE).getLocation();
				if (!t.equals("Red")) {
					o = new RSTile(o.getX()-1, o.getY());
				} else {
					o = objects.getNearest(Constants.Objects.GAME_CASTLE_RED).getLocation();
					o = new RSTile(o.getX()-2, o.getY());
				}
				return o;
			}

			private boolean wallOnScreen() {
				return calc.distanceTo(myWall()) < 5;
			}

			private class Camera {
				private boolean move(int dir) {
					if (!methods.areas.inGame())
						return false;
					RSTile t = getMyPlayer().getLocation();
					keyboard.pressKey((char) dir);
					sleep(100, 350);
					keyboard.releaseKey((char) dir);
					for (int i = 0; t.equals(getMyPlayer().getLocation()); i++) {
						sleep(100, 200);
						if (i > 25)
							return false;
					}
					return false;
				}
			}
		}

		private class Inventory {
			private boolean contains(String name) {
				if (methods.openTab(Game.TAB_INVENTORY)) {
					for (RSItem item : inventory.getItems()) {
						if (item.getName().contains(name))
							return true;
					}
				}
				return false;
			}

			private boolean hasContracts() {
				int i = 0;
				if (methods.openTab(Game.TAB_INVENTORY)) {
					for (RSItem item : inventory.getItems()) {
						if (item.getName().contains("squad"))
							i++;
					}
				}
				return i == 10;
			}

			private boolean needResupply() {
				return invent.contains("defeated");
			}
		}

		private class Antiban {
			private String[] previouslyClicked = new String[0];

			private boolean clickPlayer() {
				if (methods.areas.inGame())
					return false;
				RSPlayer p = players.getNearest(filters.PLAYER);
				Set<String> names = new HashSet<String>();
				for (String name : previouslyClicked)
					names.add(name);
				if (p != null && p.isOnScreen()) {
					try {
						log("Antiban WaitingRoom: Examine player " + p.getName() + ".");
						mouse.move(p.getModel().getPoint());
						mouse.click(false);
						sleep(600, 2500);
						mouse.moveSlightly();
						sleep(300, 600);
						if (menu.isOpen())
							menu.doAction("Cancel");
						names.add(p.getName());
						previouslyClicked = names.toArray(new String[names.size()]);
						return true;
					} catch (Exception e) {
						return false;//happens when goes to examine player but game starts
					}
				}
				return false;
			}

			private boolean moveTiles() {
				if (methods.areas.inGame())
					return false;
				RSTile t = new RSTile(getMyPlayer().getLocation().getX() + random(-2, 3), 
						getMyPlayer().getLocation().getY() + random(-2, 3));
				if (objects.getTopAt(t) == null) {
					if (t.equals(getMyPlayer().getLocation()))
						moveTiles();
					log("Antiban WaitingRoom: Move tiles.");
					return(tiles.doAction(t, "Walk"));
				} else {
					moveTiles();
				}
				return false;
			}

			private void gameCamera() {
				int rnd = random(0, 4);
				log("Antiban InGame: Move camera.");
				if (rnd == 1) {
					methods.inGame.cam.move(Constants.Keys.UP);
					sleep(1000, 2000);
					if (random(0, 3) == 1)
						methods.inGame.cam.move(Constants.Keys.DOWN);
				} else if (rnd == 2) {
					methods.inGame.cam.move(Constants.Keys.DOWN);
					sleep(1000, 2000);
					if (random(0, 3) == 1)
						methods.inGame.cam.move(Constants.Keys.UP);
				} else if (rnd == 3) {
					methods.inGame.cam.move(Constants.Keys.RIGHT);
					sleep(1000, 2000);
					if (random(0, 3) == 1)
						methods.inGame.cam.move(Constants.Keys.LEFT);
				} else if (rnd == 4) {
					methods.inGame.cam.move(Constants.Keys.LEFT);
					sleep(1000, 2000);
					if (random(0, 3) == 1)
						methods.inGame.cam.move(Constants.Keys.RIGHT);
				}
			}
		}

		private class Filters {
			private Filter<RSPlayer> PLAYER = new Filter<RSPlayer>() {
				public boolean accept(RSPlayer p) {
					if (p.getLocation().equals(getMyPlayer().getLocation()))
						return false;
					if (random(0, 4) == 2)
						return true;
					for (String name : antiban.previouslyClicked)
						if (p.getName().equals(name))
							return false;
					return true;
				}
			};
		}

		private class Areas {
			private boolean inWaitingRoom() {
				return interfaces.get(Constants.Interfaces.STATUS_WAITING_ROOM).isValid();
			}

			private boolean inGame() {
				return interfaces.get(Constants.Interfaces.GAME_STATUS).isValid();
			}

			private boolean inMA() {
				return new RSArea(new RSTile(2397, 2821), new RSTile(2430, 2845)).contains(getMyPlayer().getLocation());
			}

			private boolean inRecruit() {
				return new RSArea(new RSTile(2399, 2824), new RSTile(2406, 2831)).contains(getMyPlayer().getLocation());
			}

			private boolean inFinishedRoom() {
				RSObject o = objects.getNearest(Constants.Objects.TABLE_FINISHED_ROOM);
				return o != null && calc.distanceTo(o) < 10;
			}
		}
	}
}
