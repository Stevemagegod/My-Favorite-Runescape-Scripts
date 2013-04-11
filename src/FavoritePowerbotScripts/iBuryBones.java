import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.SwingUtilities;
import org.powerbot.concurrent.Task;
import org.powerbot.concurrent.strategy.Strategy;
import org.powerbot.game.api.ActiveScript;
import org.powerbot.game.api.Manifest;
import org.powerbot.game.api.methods.Tabs;
import org.powerbot.game.api.methods.interactive.Players;
import org.powerbot.game.api.methods.tab.Inventory;
import org.powerbot.game.api.methods.tab.Skills;
import org.powerbot.game.api.methods.widget.Bank;
import org.powerbot.game.api.methods.widget.Bank$Amount;
import org.powerbot.game.api.util.Time;
import org.powerbot.game.api.wrappers.node.Item;
import org.powerbot.game.bot.event.MessageEvent;
import org.powerbot.game.bot.event.listener.MessageListener;
import org.powerbot.game.bot.event.listener.PaintListener;

/**
 *
 * @author iJava/iJavaCoder
 */
@Manifest(name = "iBuryBones", description = "Buries all different types of bones start next to bank", version = 1.0d, authors = {"iJava/iJavaCoder"})
public class iBuryBones extends ActiveScript implements PaintListener, MessageListener {

    private final int NORMAL_BONES_ID = 526;
    private final int MONKEY_BONES_ID = 3183;
    private final int BURNT_BONES_ID = 528;
    private final int BIG_BONES_ID = 532;
    private final int FROST_DRAGON_BONES_ID = 18832;
    private final int BABY_DRAGON_BONES_ID = 534;
    private final int DRAGON_BONES_ID = 536;
    private int SELECTED_BONES_ID = 0;
    private boolean startScript = false;
    private String status, boneType;
    private long bonesBurried, bonesBurriedH, xpGained, xpGainedH, runTime, startTime, startXP, startLevel, levelsGained;
    iBuryBonesGUI I_BURY_BONES_GUI;
    private final Color color1 = new Color(0, 0, 0);
    private final Color color2 = new Color(255, 255, 255);
    private final BasicStroke stroke1 = new BasicStroke(1);
    private final Font font1 = new Font("Arial", 1, 16);
    private final Font font2 = new Font("Arial", 1, 12);
    private final Font font3 = new Font("Arial", 1, 10);

    @Override
    protected void setup() {
        startTime = System.currentTimeMillis();
        startXP = Skills.getExperience(Skills.PRAYER);
        startLevel = Skills.getRealLevel(Skills.PRAYER);
        status = "Waiting For Input";
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                final iBuryBonesGUI I_BURY_BONES_GUI = new iBuryBonesGUI();
                I_BURY_BONES_GUI.setVisible(true);
            }
        });
        provide(new Banker());
        provide(new Sleeper());
        provide(new Burier());
        provide(new TabChanger());
    }

    private void buryAll(int itemID) {
        Item[] invItems = Inventory.getItems();
        for (Item item : invItems) {
            if (item.getId() == itemID) {
                if (Tabs.getCurrent() != Tabs.INVENTORY) {
                    Tabs.INVENTORY.open();
                    Time.sleep(300, 600);
                }
                if (!Players.getLocal().isMoving() && Players.getLocal().getAnimation() == -1 && Tabs.getCurrent() == Tabs.INVENTORY) {
                    item.getWidgetChild().interact("Bury");
                    Time.sleep(100, 300);
                } else {
                    Time.sleep(300, 600);
                }
            }
        }
    }

    private boolean inventoryContains(int itemID) {
        return Inventory.getCount(itemID) > 0;
    }

    @Override
    public void onRepaint(Graphics g1) {
        Graphics2D g = (Graphics2D) g1;
        runTime = System.currentTimeMillis() - startTime;
        bonesBurriedH = (int) Math.floor((bonesBurried * 3600000D) / runTime);
        xpGained = Skills.getExperience(Skills.PRAYER) - startXP;
        xpGainedH = (int) Math.floor((xpGained * 3600000D) / runTime);
        levelsGained = Skills.getRealLevel(Skills.PRAYER) - startLevel;
        g.setColor(color1);
        g.fillRect(4, 2, 174, 238);
        g.setStroke(stroke1);
        g.drawRect(4, 2, 174, 238);
        g.setFont(font1);
        g.setColor(color2);
        g.drawString("iBuryBones", 37, 28);
        g.setFont(font2);
        g.drawString("By iJava/iJavaCoder", 28, 46);
        g.setFont(font3);
        g.drawString("Status : " + status, 12, 107);
        g.drawString("Bones Burried : " + bonesBurried, 12, 122);
        g.drawString("Bones Burried P/H : " + bonesBurriedH, 12, 137);
        g.drawString("XP Gained: " + xpGained, 12, 152);
        g.drawString("XP Gained P/H : " + xpGainedH, 12, 167);
        g.drawString("Level : " + Skills.getRealLevel(Skills.PRAYER) + " (" + levelsGained + ")", 12, 179);
        g.drawString("Bone Type : " + boneType, 12, 194);
        g.drawString("Running Time : " + Time.format(runTime), 12, 209);
        g.drawString("Support @ http://runelite.org", 26, 235);
    }

    @Override
    public void messageReceived(MessageEvent me) {
        if (me.getMessage().contains("bury the bones")) {
            bonesBurried++;
        }
    }

    private class TabChanger extends Strategy implements Task {

        @Override
        public boolean validate() {
            return !Tabs.getCurrent().equals(Tabs.INVENTORY) && !Bank.isOpen();
        }

        @Override
        public void run() {
            if (!Tabs.getCurrent().equals(Tabs.INVENTORY)) {
                Tabs.INVENTORY.open();
                Time.sleep(400, 900);
            }
        }
    }

    private class Banker extends Strategy implements Task {

        @Override
        public boolean validate() {
            return !inventoryContains(SELECTED_BONES_ID) && startScript;
        }

        @Override
        public void run() {
            status = "Banking";
            if (!Bank.isOpen() && !Inventory.isFull()) {
                Bank.open();
                Time.sleep(800, 1500);
            }
            if (Bank.isOpen() && !Inventory.isFull()) {
                Bank.withdraw(SELECTED_BONES_ID, Bank$Amount.ALL);
                Time.sleep(500, 900);
                Bank.close();
            }
            if (Bank.isOpen() && Inventory.isFull()) {
                Bank.close();
                Time.sleep(800, 1500);
            }
        }
    }

    private class Sleeper extends Strategy implements Task {

        @Override
        public boolean validate() {
            return Players.getLocal().isMoving() && Tabs.getCurrent().equals(Tabs.INVENTORY) || Players.getLocal().getAnimation() != -1 && Tabs.getCurrent().equals(Tabs.INVENTORY) || !startScript;
        }

        @Override
        public void run() {
            status = "Sleeping";
            Time.sleep(100, 500);
        }
    }

    private class Burier extends Strategy implements Task {

        @Override
        public boolean validate() {
            return inventoryContains(SELECTED_BONES_ID) && startScript && Tabs.getCurrent().equals(Tabs.INVENTORY);
        }

        @Override
        public void run() {
            if (inventoryContains(SELECTED_BONES_ID) && !Players.getLocal().isMoving() && Players.getLocal().getAnimation() == -1) {
                status = "Burying";
                buryAll(SELECTED_BONES_ID);
            } else {
                status = "Sleeping";
                Time.sleep(100, 400);
            }
        }
    }

    /*
     * To change this template, choose Tools | Templates
     * and open the template in the editor.
     */

    /*
     * iBuryBonesGUI.java
     *
     * Created on 9/04/2012, 9:42:31 AM
     */
    /**
     *
     * @author Owner
     */
    public class iBuryBonesGUI extends javax.swing.JFrame {

        /** Creates new form iBuryBonesGUI */
        public iBuryBonesGUI() {
            initComponents();
        }

        /** This method is called from within the constructor to
         * initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">
        private void initComponents() {

            startBtn = new javax.swing.JButton();
            bonesComboBox = new javax.swing.JComboBox();
            titleLbl = new javax.swing.JLabel();
            typeBonesLbl = new javax.swing.JLabel();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

            startBtn.setText("Start");
            startBtn.addActionListener(new java.awt.event.ActionListener() {

                @Override
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startBtnActionPerformed(evt);
                }
            });

            bonesComboBox.setModel(new javax.swing.DefaultComboBoxModel(new String[]{"Normal Bones", "Monkey Bones", "Burnt Bones", "Big Bones", "Frost Dragon Bones", "Baby Dragon Bones", "Dragon Bones"}));

            titleLbl.setFont(new java.awt.Font("Tahoma", 0, 18));
            titleLbl.setText("iBuryBones");

            typeBonesLbl.setText("Type of Bones :");

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false).addComponent(startBtn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup().addComponent(typeBonesLbl).addGap(18, 18, 18).addComponent(bonesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)))).addGroup(layout.createSequentialGroup().addGap(82, 82, 82).addComponent(titleLbl))).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(4, 4, 4).addComponent(titleLbl).addGap(18, 18, 18).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(typeBonesLbl).addComponent(bonesComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)).addGap(25, 25, 25).addComponent(startBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE).addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

            pack();
        }// </editor-fold>

        private void startBtnActionPerformed(java.awt.event.ActionEvent evt) {
            // TODO add your handling code here:
            if (bonesComboBox.getSelectedItem() == "Normal Bones") {
                boneType = "Normal Bones";
                SELECTED_BONES_ID = NORMAL_BONES_ID;
            }
            if (bonesComboBox.getSelectedItem() == "Monkey Bones") {
                boneType = "Monkey Bones";
                SELECTED_BONES_ID = MONKEY_BONES_ID;
            }
            if (bonesComboBox.getSelectedItem() == "Burnt Bones") {
                boneType = "Burnt Bones";
                SELECTED_BONES_ID = BURNT_BONES_ID;
            }
            if (bonesComboBox.getSelectedItem() == "Big Bones") {
                boneType = "Big Bones";
                SELECTED_BONES_ID = BIG_BONES_ID;
            }
            if (bonesComboBox.getSelectedItem() == "Frost Dragon Bones") {
                boneType = "Frost Dragon Bones";
                SELECTED_BONES_ID = FROST_DRAGON_BONES_ID;
            }
            if (bonesComboBox.getSelectedItem() == "Baby Dragon Bones") {
                boneType = "Baby Dragon Bones";
                SELECTED_BONES_ID = BABY_DRAGON_BONES_ID;
            }
            if (bonesComboBox.getSelectedItem() == "Dragon Bones") {
                boneType = "Dragon Bones";
                SELECTED_BONES_ID = DRAGON_BONES_ID;
            }
            startScript = true;
            this.dispose();
        }
        // Variables declaration - do not modify
        private javax.swing.JComboBox bonesComboBox;
        private javax.swing.JButton startBtn;
        private javax.swing.JLabel titleLbl;
        private javax.swing.JLabel typeBonesLbl;
        // End of variables declaration
    }
}
