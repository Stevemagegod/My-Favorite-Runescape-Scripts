import java.awt.*;
import javax.swing.JFrame;
import javax.swing.LookAndFeel;
import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Condition;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Calculations;
import org.powerbot.game.api.methods.Game;
import org.powerbot.game.api.methods.Widgets;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.util.Filter;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.Tile;
import org.powerbot.game.api.wrappers.interactive.Player;
import org.powerbot.game.bot.event.listener.PaintListener;

@Manifest(name = "PKAid",
authors = {"Tutorial_Con"},
description = "A Player-Killing Aid.",
premium = false,
version = 1.0D,
website = "http://www.TutorialCon.com/")
public class PKAid extends ActiveScript implements Task, Condition, PaintListener {

    private Player[] players = null;
    int wildernessLevel = 0;
    int tick = 0;
    PKAidGUI GUI = null;
    PKAidGUI_Strategy GUI_Strategy = null;

    enum GUI_SETTINGS {

        BOUNDING_BOX,
        TILE,
        NAME_LEVEL;
        private boolean enabled = false;

        GUI_SETTINGS() {
        }

        public void set(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean enabled() {
            return enabled;
        }
    }

    protected void setup() {
        provide(new Strategy(this, this));
        GUI_Strategy = new PKAidGUI_Strategy();
        provide(GUI_Strategy);
    }

    public boolean validate() {
        return (GUI != null && !GUI.isVisible())
                || (GUI_Strategy != null && !GUI_Strategy.validate());
    }

    public void run() {
        if (tick == 0) {
            GUI_Strategy.start();
        }
        if (Game.isLoggedIn()) {
            final Player botter = Players.getLocal();
            players = Players.getLoaded(new Filter<Player>() {

                public boolean accept(Player p) {
                    return (p != null)
                            && (Calculations.distanceTo(p.getLocation()) < 19.0D)
                            && (botter.getLevel() - getWildernessLevel() <= p.getLevel())
                            && (botter.getLevel() + getWildernessLevel() >= p.getLevel())
                            && (!p.equals(botter));

                }
            });
        }
        tick++;
        Time.sleep(50);
    }

    public void onRepaint(Graphics grphcs) {
        if (Game.isLoggedIn()) {
            Graphics2D g = (Graphics2D) grphcs;
            g.setRenderingHints(new RenderingHints(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON));

            Player botter = Players.getLocal();
            if ((players != null)
                    && (inWilderness())) {
                for (Player p : players) {
                    if ((p != null) && (!p.equals(botter))) {
                        if (GUI_SETTINGS.TILE.enabled() && p.getLocation().isOnScreen()) {
                            drawTile(g, p.getLocation(), colorForLevel(p));
                        }
                        if (GUI_SETTINGS.BOUNDING_BOX.enabled() && p.isOnScreen()) {
                            drawPlayer(g, p, colorForLevel(p));
                        }
                        if (GUI_SETTINGS.NAME_LEVEL.enabled() && p.isOnScreen()) {
                            drawPlayerInformation(g, p, colorForLevel(p));
                        }
                    }
                }
            }
        }
    }

    public void drawTile(Graphics2D g, Tile tile, Color col) {
        for (Polygon poly : tile.getBounds()) {
            boolean drawThisOne = true;
            for (int i = 0; i < poly.npoints; i++) {
                Point p = new Point(poly.xpoints[i], poly.ypoints[i]);
                if (!Calculations.isOnScreen(p)) {
                    drawThisOne = false;
                }
            }
            if (drawThisOne) {
                Color col2 = new Color(col.getRed(), col.getGreen(), col.getBlue(), 80);
                g.setColor(col2);
                g.fillPolygon(poly);
                g.setColor(col);
                g.drawPolygon(poly);
            }
        }
    }

    public void drawPlayer(Graphics2D g, Player player, Color col) {
        if (player.isOnScreen()) {
            Rectangle rect = getBoundingBox(player.getBounds());
            if ((Calculations.isOnScreen(new Point(rect.x, rect.y))) && (Calculations.isOnScreen(new Point(rect.x + rect.width, rect.y + rect.height)))) {
                g.setColor(col);
            }
            g.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());
        }
    }

    public void drawPlayerInformation(Graphics2D g, Player player, Color col) {
        Rectangle boundingBox = getBoundingBox(player.getBounds());
        String playerName = player.getName();
        int level = player.getLevel();
        int centerBoundPointX = (int) (boundingBox.getX() + boundingBox.getWidth() / 2);
        double fullWidth = g.getFont().getStringBounds(playerName + "(" + level + ")",
                g.getFontRenderContext()).getWidth(); //Left space out to look neater in-game.
        double nameWidth = g.getFont().getStringBounds(playerName + " (",
                g.getFontRenderContext()).getWidth();
        double levelWidth = g.getFont().getStringBounds(level + "",
                g.getFontRenderContext()).getWidth();
        int stringStartX = centerBoundPointX - (int) (fullWidth / 2);
        g.setColor(Color.WHITE);
        g.drawString(playerName + " (", stringStartX,
                (int) boundingBox.getY());
        g.setColor(col);
        g.drawString("" + level, stringStartX + (int) nameWidth, (int) boundingBox.getY());
        g.setColor(Color.WHITE);
        g.drawString(")", stringStartX + (int) nameWidth + (int) levelWidth, (int) boundingBox.getY());
    }

    public Color colorForLevel(Player p) {
        Player botter = Players.getLocal();
        float delta = getScale(p.getLevel() - botter.getLevel(), getWildernessLevel());

        return Color.getHSBColor((60.0F - 60.0F * delta) / 360.0F, 1F, 1F);
    }

    public Rectangle getBoundingBox(Polygon[] polys) {
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (Polygon poly : polys) {
            Rectangle rect = poly.getBounds();
            if (minX > rect.getX()) {
                minX = rect.getX();
            }
            if (minY > rect.getY()) {
                minY = rect.getY();
            }
            if (maxX < rect.getX()) {
                maxX = rect.getX();
            }
            if (maxY < rect.getY()) {
                maxY = rect.getY();
            }
        }
        return new Rectangle((int) minX - 5, (int) minY - 5, (int) (maxX - minX) + 5, (int) (maxY - minY) + 5);
    }

    public boolean inWilderness() {
        return Widgets.get(381, 2).isVisible() ? true : false;
    }

    public int getWildernessLevel() {
        return inWilderness() ? Integer.parseInt(
                Widgets.get(381, 2).getText().replaceAll("Level: ", "")) : 0;
    }

    public float getScale(int difference, int max) {
        if (difference == 0) {
            return 0F;
        }
        float val = (float) Math.max(difference, -difference) / (float) max;
        return difference > 0 ? val : -val;
    }

    class PKAidGUI_Strategy extends Strategy implements Task {

        public void start() {
            if (GUI != null) {
                GUI.setVisible(true);
            } else {
                GUI = new PKAidGUI();
                GUI.setVisible(true);
                GUI.toFront();
            }
        }

        @Override
        public void run() {
            Time.sleep(250);
        }

        @Override
        public boolean validate() {
            return (GUI != null && GUI.isVisible());
        }
    }

    class PKAidGUI extends JFrame {

        public PKAidGUI() {
            LookAndFeel old = javax.swing.UIManager.getLookAndFeel();
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException ex) {
                java.util.logging.Logger.getLogger(PKAidGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                java.util.logging.Logger.getLogger(PKAidGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                java.util.logging.Logger.getLogger(PKAidGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(PKAidGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            initComponents();
            setTitle("PKAid Config");
            setLocationRelativeTo(getRootPane());
            setVisible(true);
            try {
                javax.swing.UIManager.setLookAndFeel(old);
            } catch (javax.swing.UnsupportedLookAndFeelException ex) {
                java.util.logging.Logger.getLogger(PKAidGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }

        private void initComponents() {
            jPanel2 = new javax.swing.JPanel();
            jCkbx_boundingBox = new javax.swing.JCheckBox();
            jCkbx_tile = new javax.swing.JCheckBox();
            jPanel3 = new javax.swing.JPanel();
            jCkbx_levelName = new javax.swing.JCheckBox();
            jBtn_start = new javax.swing.JButton();

            jBtn_start.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    setVisible(false);
                }
            });

            jCkbx_boundingBox.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    GUI_SETTINGS.BOUNDING_BOX.set(jCkbx_boundingBox.isSelected());
                }
            });

            jCkbx_tile.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    GUI_SETTINGS.TILE.set(jCkbx_tile.isSelected());
                }
            });

            jCkbx_levelName.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    GUI_SETTINGS.NAME_LEVEL.set(jCkbx_levelName.isSelected());
                }
            });

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setResizable(false);

            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Player Colouring"));

            jCkbx_boundingBox.setText("Bounding Box");

            jCkbx_tile.setText("Tile");

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCkbx_boundingBox).addComponent(jCkbx_tile)).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            jPanel2Layout.setVerticalGroup(
                    jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel2Layout.createSequentialGroup().addComponent(jCkbx_boundingBox).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jCkbx_tile)));

            jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Player Information"));

            jCkbx_levelName.setText("Level & Name");

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addComponent(jCkbx_levelName).addContainerGap(24, Short.MAX_VALUE)));
            jPanel3Layout.setVerticalGroup(
                    jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jCkbx_levelName));

            jBtn_start.setText("Start");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jBtn_start, javax.swing.GroupLayout.DEFAULT_SIZE, 75, Short.MAX_VALUE).addContainerGap()));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE).addGap(0, 0, Short.MAX_VALUE)).addGroup(layout.createSequentialGroup().addContainerGap().addComponent(jBtn_start, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addContainerGap()));

            pack();
        }
        private javax.swing.JButton jBtn_start;
        private javax.swing.JCheckBox jCkbx_boundingBox;
        private javax.swing.JCheckBox jCkbx_levelName;
        private javax.swing.JCheckBox jCkbx_tile;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JPanel jPanel3;
    }
}