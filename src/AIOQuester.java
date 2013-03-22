import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rsbot.script.util.Timer;
import java.net.URI;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import org.rsbot.Configuration;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.Script;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Game.Tab;
import org.rsbot.script.methods.Magic.Spell;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;
import org.rsbot.script.wrappers.RSWeb;

@ScriptManifest(authors = {"Ericthecmh", "Xiffs"},
version = 0.6,
description = "Does F2P Quests Version 0.6",
keywords = {"quest"},
name = "AIOQuester")
public class AIOQuester extends Script implements PaintListener, MouseListener {

    final TrayIcon trayIcon = new TrayIcon(getImage("http://i201.photobucket.com/albums/aa162/Zantareous/Icons/icon_quest.gif"));
    final SystemTray tray = SystemTray.getSystemTray();
    public boolean show2 = true;
    String Thread = "http://www.powerbot.org/community/topic/583881-aio-quester/";
    private int[] questitems = new int[]{995, 291, 15410, 15597, 15361, 15596, 15597};
    private boolean canContinue = false;
    private boolean canStart = false;
    private final String[] questnames = {"Cook's Assistant", "Prince Ali Rescue", "Restless Ghost", "Rune Mysteries", "Blood Pact", "Doric's Quest", "Vampire Slayer", "Gunnar's Grounds"};
    private String questName = "None";
    final Object lock = new Object();
    final ArrayList<Particle> particles = new ArrayList<Particle>();
    private String questAction = "None";
    long start = 0;
    public boolean already = false;
    private Action currentAction;
    private int currentQuestIndex = 0;
    private String dialogOption = "None";
    private final LinkedList<MousePathPoint> mousePath = new LinkedList<MousePathPoint>();
    BufferedImage normal = null;
    boolean canContinue2 = false;
    HashMap<String, ArrayList<Boolean>> doActions;
    ArrayList<Action> actions = new ArrayList<Action>();
    int nextActionIndex = 0;
    java.awt.Desktop myNewBrowserDesktop = java.awt.Desktop.getDesktop();
    long runTime;
    long  startTime;


    private void promptProgress(String quest) {
        this.actions.add(Action.QUESTSTART);
        if (quest.equals("Cook's Assistant")) {
            doActions.put(quest, new ArrayList<Boolean>());
            String[] actions = new String[]{"Started Quest", "Got fine flour (in inventory)", "Got super large egg (in inventory)", "Got prized milk (in inventory)"};

              java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run(){
                       String[] actions = new String[]{"Started Quest", "Got fine flour (in inventory)", "Got super large egg (in inventory)", "Got prized milk (in inventory)"};

                      PROMPTGUI p = new PROMPTGUI(actions);

            p.setVisible(false);
                  }
              });
              actions = new String[]{"Started Quest", "Got fine flour (in inventory)", "Got super large egg (in inventory)", "Got prized milk (in inventory)"};

                      PROMPTGUI p = new PROMPTGUI(actions);
            while (p.isVisible()) {
                sleep(300, 400);
            }
            for (int x = 0; x < actions.length; x++) {
                if (x == 1) {
                    this.actions.add(Action.COOKASSWALKTOSTORE);
                }
                if (!p.todoListModel.contains(actions[x])) {
                    switch (x) {
                        case 0:
                            this.actions.add(Action.COOKASSSTARTWALK);
                            break;
                        case 1:
                            this.actions.add(Action.COOKASSWALKTOGRAINS);
                            break;
                        case 2:
                            this.actions.add(Action.COOKASSWALKTOEGG);
                            break;
                        case 3:
                            this.actions.add(Action.COOKASSWALKTOCOWS);
                    }
                }
            }
            this.actions.add(Action.COOKASSTELEPORTLUMBRIDGECOWS);
        } else if (quest.equals("Prince Ali Rescue")) {
            this.actions.add(Action.PRINCEALISTARTWALK);
        } else if (quest.equals("Restless Ghost")) {
            this.actions.add(Action.RESTLESSGHOSTSTART);
        } else if (quest.equals("Gunnar's Grounds")) {
            this.actions.add(Action.GunFirstWalk);
        } else if (quest.equals("Vampire Slayer")) {
            this.actions.add(Action.VampStartQuest);
        } else if (quest.equals("Rune Mysteries")) {
            doActions.put(quest, new ArrayList<Boolean>());
            String[] actions = new String[]{"Started Quest", "Obtained the Research Package (in inventory or given to Aubury)", "Obtained the Research notes (in inventory or given to Sedridor(", "Ended Quest"};
            PROMPTGUI p = new PROMPTGUI(actions);
              java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run(){
                       String[] actions = new String[]{"Started Quest", "Obtained the Research Package (in inventory or given to Aubury)", "Obtained the Research notes (in inventory or given to Sedridor(", "Ended Quest"};

                      PROMPTGUI p = new PROMPTGUI(actions);

            p.setVisible(false);
                  }
              });
            while (p.isVisible()) {
                sleep(300, 400);
            }
            for (int x = 0; x < actions.length; x++) {
                if (!p.todoListModel.contains(actions[x])) {
                    switch (x) {
                        case 0:
                            this.actions.add(Action.RUNEMYSTSTARTWALK);
                            break;
                        case 1:
                            this.actions.add(Action.RUNEMYSTTELEPORTLUMBRIDGE);
                            break;
                        case 2:
                            this.actions.add(Action.RUNEMYSTTELEPORTLUMBRIDGE2);
                            break;
                        case 3:
                            this.actions.add(Action.RUNEMYSTWALKTOWIZARDTOWER_3);
                    }
                }
            }
        } else if (quest.equals("Blood Pact")) {
            doActions.put(quest, new ArrayList<Boolean>());
            String[] actions = new String[]{"Started Quest", "Watched first cutscene", "Killed first cultist (ranger)", "Killed second cultist (mage)", "Turned the winch and lowered the gate", "Killed third cultist (warrior)", "Ended Quest"};
            PROMPTGUI p = new PROMPTGUI(actions);
              java.awt.EventQueue.invokeLater(new Runnable() {
                  public void run(){
                     String[] actions = new String[]{"Started Quest", "Watched first cutscene", "Killed first cultist (ranger)", "Killed second cultist (mage)", "Turned the winch and lowered the gate", "Killed third cultist (warrior)", "Ended Quest"};

                      PROMPTGUI p = new PROMPTGUI(actions);

            p.setVisible(false);
                  }
              });
            while (p.isVisible()) {
                sleep(300, 400);
            }
            this.actions.add(Action.BLOODPACTSTARTWALK);
            for (int x = 0; x < actions.length; x++) {
                if (!p.todoListModel.contains(actions[x])) {
                    switch (x) {
                        case 0:
                            this.actions.add(Action.BLOODPACTSTARTTALK);
                            break;
                        case 1:
                            this.actions.add(Action.BLOODPACTWATCHCUTSCENE);
                            break;
                        case 2:
                            this.actions.add(Action.BLOODPACTKILLFIRSTCULTIST);
                            break;
                        case 3:
                            this.actions.add(Action.BLOODPACTKILLSECONDCULTIST);
                            break;
                        case 4:
                            this.actions.add(Action.BLOODPACTPULLBRIDGE);
                            break;
                        case 5:
                            this.actions.add(Action.BLOODPACTKILLLASTCULTIST);
                            break;
                    }
                }
                switch (x) {
                    case 0:
                        this.actions.add(Action.BLOODPACTDESCENDTRAP);
                        break;
                    case 2:
                        this.actions.add(Action.BLOODPACTWALKTOSECOND);
                        break;
                    case 4:
                        this.actions.add(Action.BLOODPACTWALKTOLAST);
                }
            }
        } else if (quest.equals("Doric's Quest")) {
            this.actions.add(Action.DORICWALKTOBOB);
        }
    }

    /*private void updateNextAction() {
    ArrayList<Boolean> barr = doActions.get(currentQuest);
    for(int x = 0; x < barr.size(); x++){
    if(!barr.get(x)){
    if(currentQuest.equa)
    }
    }
    }*/
    class PROMPTGUI extends javax.swing.JFrame {
   
        public boolean isDone = false;
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                canContinue2 = true;
            }
        };

        public PROMPTGUI(String[] actions) {
            initComponents();
            for (String a : actions) {
                availableListModel.addElement(a);
            }
        }

        private void startActionPerformed(java.awt.event.ActionEvent evt) {
            canContinue2 = true;
            canStart = true;
            setVisible(false);
        }

        private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
            canContinue2 = true;
            setVisible(false);
            stopScript();
        }

        private void initComponents() {

            jScrollPane1 = new javax.swing.JScrollPane();
            availableList = new javax.swing.JList();
            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jScrollPane2 = new javax.swing.JScrollPane();
            todoList = new javax.swing.JList();
            jLabel3 = new javax.swing.JLabel();
            addToList = new javax.swing.JButton();
            removeFromList = new javax.swing.JButton();
            start = new javax.swing.JButton();
            cancel = new javax.swing.JButton();

            availableListModel = new DefaultListModel();
            availableList.setModel(availableListModel);
            jScrollPane1.setViewportView(availableList);

            jLabel1.setText("iQuestSolver by Xiffs!");

            jLabel2.setText("Actions not yet completed:");

            todoListModel = new DefaultListModel();
            todoList.setModel(todoListModel);
            jScrollPane2.setViewportView(todoList);

            jLabel3.setText("Actions already done:");

            addWindowListener(exitListener);

            addToList.setText("->");
            addToList.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addToListActionPerformed(evt);
                }
            });

            removeFromList.setText("<-");
            removeFromList.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    removeFromListActionPerformed(evt);
                }
            });

            start.setText("Go");

            start.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startActionPerformed(evt);
                }
            });

            cancel.setText("Cancel");
            cancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancelActionPerformed(evt);
                }
            });
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE).addComponent(start, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(removeFromList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(addToList, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cancel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)).addGap(24, 24, 24)))));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(145, 145, 145).addComponent(addToList).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(removeFromList)).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(start).addComponent(cancel)).addContainerGap()));
            pack();
        }

        private void addToListActionPerformed(java.awt.event.ActionEvent evt) {
            String selected = (String) availableListModel.get(availableList.getSelectedIndex());
            availableListModel.remove(availableList.getSelectedIndex());
            todoListModel.addElement(selected);
        }

        private void removeFromListActionPerformed(java.awt.event.ActionEvent evt) {
            String selected = (String) todoListModel.get(todoList.getSelectedIndex());
            todoListModel.remove(todoList.getSelectedIndex());
            availableListModel.addElement(selected);
        }

        public void main(String args[]) {
            setVisible(true);
        }
        // Variables declaration - do not modify
        public javax.swing.JButton addToList;
        private javax.swing.JList availableList;
        private javax.swing.JButton cancel;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JButton removeFromList;
        private javax.swing.JButton start;
        private javax.swing.JList todoList;
        // End of variables declaration
        private DefaultListModel availableListModel;
        private DefaultListModel todoListModel;
    }

    class GUI extends javax.swing.JFrame {

        public boolean isDone = false;
        WindowListener exitListener = new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                canContinue = true;
            }
        };

        public GUI() {
            initComponents();
            for (int x = 0; x < questnames.length; x++) {
                availableListModel.addElement(questnames[x]);
            }
        }

        private void startActionPerformed(java.awt.event.ActionEvent evt) {
            canContinue = true;
            canStart = true;
            setVisible(false);
        }

        private void cancelActionPerformed(java.awt.event.ActionEvent evt) {
            canContinue = true;
            setVisible(false);
        }

        private void initComponents() {

            jScrollPane1 = new javax.swing.JScrollPane();
            availableList = new javax.swing.JList();
            jLabel1 = new javax.swing.JLabel();
            jLabel2 = new javax.swing.JLabel();
            jScrollPane2 = new javax.swing.JScrollPane();
            todoList = new javax.swing.JList();
            jLabel3 = new javax.swing.JLabel();
            addToList = new javax.swing.JButton();
            removeFromList = new javax.swing.JButton();
            start = new javax.swing.JButton();
            cancel = new javax.swing.JButton();

            availableListModel = new DefaultListModel();
            availableList.setModel(availableListModel);
            jScrollPane1.setViewportView(availableList);

            jLabel1.setText("iQuestSolver by Xiffs!");

            jLabel2.setText("Available Quests:");

            todoListModel = new DefaultListModel();
            todoList.setModel(todoListModel);
            jScrollPane2.setViewportView(todoList);

            jLabel3.setText("Quests to do:");

            addWindowListener(exitListener);

            addToList.setText("->");
            addToList.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    addToListActionPerformed(evt);
                }
            });

            removeFromList.setText("<-");
            removeFromList.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    removeFromListActionPerformed(evt);
                }
            });

            start.setText("Start!");

            start.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    startActionPerformed(evt);
                }
            });

            cancel.setText("Cancel");
            cancel.addActionListener(new java.awt.event.ActionListener() {

                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    cancelActionPerformed(evt);
                }
            });
            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel1).addGroup(layout.createSequentialGroup().addContainerGap().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jLabel2).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE).addComponent(start, javax.swing.GroupLayout.DEFAULT_SIZE, 223, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false).addComponent(removeFromList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE).addComponent(addToList, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addComponent(jLabel3).addContainerGap()).addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING).addComponent(cancel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)).addGap(24, 24, 24)))));
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addGroup(layout.createSequentialGroup().addGap(145, 145, 145).addComponent(addToList).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED).addComponent(removeFromList)).addGroup(layout.createSequentialGroup().addComponent(jLabel1).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE).addComponent(jLabel2).addComponent(jLabel3)).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE).addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 297, Short.MAX_VALUE)))).addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED).addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING).addComponent(start).addComponent(cancel)).addContainerGap()));
            pack();
        }

        private void addToListActionPerformed(java.awt.event.ActionEvent evt) {
            String selected = (String) availableListModel.get(availableList.getSelectedIndex());
            availableListModel.remove(availableList.getSelectedIndex());
            todoListModel.addElement(selected);
        }

        private void removeFromListActionPerformed(java.awt.event.ActionEvent evt) {
            String selected = (String) todoListModel.get(todoList.getSelectedIndex());
            todoListModel.remove(todoList.getSelectedIndex());
            availableListModel.addElement(selected);
        }

        public void main(String args[]) {
            setVisible(true);
        }
        // Variables declaration - do not modify
        public javax.swing.JButton addToList;
        private javax.swing.JList availableList;
        private javax.swing.JButton cancel;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JButton removeFromList;
        private javax.swing.JButton start;
        private javax.swing.JList todoList;
        // End of variables declaration
        private DefaultListModel availableListModel;
        private DefaultListModel todoListModel;
    }
    GUI gui;
    ArrayList<String> doquests;
    private static final Pattern UPDATER_VERSION_PATTERN = Pattern.compile("version\\s*=\\s*([0-9.]+)");

    public boolean checkUpdates() {
        log("Checking for updates...");
        String[] urls = new String[]{"http://pastebin.com/raw.php?i=Azf6Fwfi"};
        String newest = "";
        double newestversion = AIOQuester.class.getAnnotation(ScriptManifest.class).version();
        for (String url : urls) {
            try {
                URL file = new URL(url);
                BufferedReader in = new BufferedReader(new InputStreamReader(file.openStream()));
                String lines = "";
                String s;
                double tempversion = -1.0;
                Matcher m;
                while ((s = in.readLine()) != null) {
                    lines += s + "\n";
                    if ((m = UPDATER_VERSION_PATTERN.matcher(s)).find()) {
                        tempversion = Double.parseDouble(m.group(1));
                        break;
                    }
                }
                if (tempversion > newestversion) {
                    newestversion = tempversion;
                    newest = url;
                }
            } catch (Exception e) {
                log("Failed to check for updates from: " + url);
            }
        }
        if (newestversion > AIOQuester.class.getAnnotation(ScriptManifest.class).version()) {
            try {
                URL file = new URL(newest);
                BufferedReader in = new BufferedReader(new InputStreamReader(file.openStream()));
                log("Downloading updates from: " + newest);
                PrintStream ps = new PrintStream(Configuration.Paths.getScriptsSourcesDirectory() + "\\AIOQuester.java");
                String line;
                while ((line = in.readLine()) != null) {
                    ps.println(line);
                }
            } catch (Exception e) {
                log("Failed to retrieve newest update");
                return false;
            }
            log("Could not compile the script. Please manually compile to finish the update.");

            return false;
        }
        return true;
    }

    public boolean onStart() {

          SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                        tray();
                        }
                });
        if (!checkUpdates()) {
            return false;
        }
        startTime = System.currentTimeMillis();
        doActions = new HashMap<String, ArrayList<Boolean>>();
        gui = new GUI();
       
           java.awt.EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            gui = new GUI();
                                 gui.setVisible(true);
                        }
                });
        return true;
    }

    public static RSTile[] reverse(RSTile[] initialPath) {
        RSTile[] path2 = new RSTile[initialPath.length];
        for (int x = initialPath.length - 1; x >= 0; x--) {
            path2[initialPath.length - 1 - x] = initialPath[x];
        }
        return path2;
    }

    boolean processed = false;

    public enum Action {

        QUESTSTART, ENDALL, //General
        COOKASSSTARTWALK, COOKASSSTARTTALK, COOKASSOBTAINPOT, COOKASSGETBOBI, COOKASSWALKTOSTORE, COOKASSGETSTOREITEMS, COOKASSWALKTOGRAINS, COOKASSPICKGRAINS, COOKASSWALKTOMILL, COOKASSSETFINEFLOUR, COOKASSMOVETOHOPPER, COOKASSOPERATEHOPPER, COOKASSMOVETOBIN, COOKASSPICKBIN, COOKASSTELEPORTLUMBRIDGEMILL, COOKASSWALKTOEGG, COOKASSOPENEGGGATE, COOKASSPICKEGG, COOKASSWALKTOCOWS_OPENDOOR1, COOKASSWALKTOCOWS, COOKASSWALKTOCOWS_OPENDOOR2, COOKASSGETMILK, COOKASSTELEPORTLUMBRIDGECOWS, COOKASSENDWALK, COOKASSENDTALK, //COOK'S ASSISTANT
        PRINCEALISTARTWALK, PRINCEALISTARTTALK, PRINCEALISTARTWALKOSMAN, PRINCEALISTARTTALKOSMAN, PRINCEALIWALKTOBOB, PRINCEALIBOBGETPICK, PRINCEALIWALKTOMINE, PRINCEALIMINEORES, PRINCEALIWALKTOFURNACE, PRINCEALIMAKEBRONZEBAR, PRINCEALIWALKTOBOB_Furnace, PRINCEALIBOBGETHATCHET, PRINCEALIWALKTOSTORE, PRINCEALIGETSTOREITEMS, PRINCEALIWALKTOSHEEP, PRINCEALIOPENSHEEPGATE, PRINCEALISHEARSHEEP, PRINCEALIOPENSHEEPGATE2, PRINCEALIWALKTOSPINNINGWHEEL, PRINCEALICLIMBSTAIRS, PRINCEALIUSESPINNING, PRINCEALIWALKTONED, PRINCEALITELEPORTLUMBRIDGE, PRINCEALIOPENNEDDOOR, PRINCEALIGETROPENED, PRINCEALIWALKTOTREE, PRINCEALICUTLOGS, PRINCEALIBURNLOGS, PRINCEALIOPENNEDDOOR2, PRINCEALIWALKTOONIONS, PRINCEALIPICKONIONS, PRINCEALIWALKTOAGGIE, PRINCEALIAGGIEGETMATERIALS, PRINCEALIOPENAGGIEDOOR, PRINCEALIMINECLAY, PRINCEALIOPENAGGIEDOOR2, PRINCEALIWALKTOCLAYMINE, PRINCEALIWALKTOPUMP, PRINCEALIFILLBUCKET, PRINCEALIAGGIEGETMATERIALS2, PRINCEALIWALKTOVARROCK, PRINCEALIFILLBUCKETFOUNTAIN, PRINCEALIWALKTOCLOTHESSHOP, PRINCEALIWALKTOBLUEMOON, PRINCEALIBUYBEER, PRINCEALIPICKREDBERRIES, PRINCEALIWALKTOREDBERRIES, PRINCEALITELEPORTLUMBRIDGE2, PRINCEALIWALKTOJAIL, PRINCEALIGETKEYMOULD, PRINCEALIOPENJAILDOOR2, PRINCEALIOPENJAILDOOR, PRINCEALIWALKTOLEELA, PRINCEALIGIVELEELAMOULD, PRINCEALIOPENAGGIEDOOR3, PRINCEALIWALKTOAGGIE2, PRINCEALIWALKTOOSMAN, PRINCEALITELEPORTLUMBRIDGE3, PRINCEALIWALKTOOSMAN2, PRINCEALIKEYMOULDOSMAN, PRINCEALITELEPORTLUMBRIDGE4, PRINCEALIGETBRONZEKEY, PRINCEALIOPENJAILDOOR3, PRINCEALIWALKTOJAIL2, PRINCEALIGETADVICELEELA, PRINCEALIGETGUARDDRUNK, PRINCEALIBINDKELI, PRINCEALIRESCUEPRINCE, PRINCEALIENDTALK, PRINCEALITELEPORTLUMBRIDGE5, PRINCEALIENDWALK, COOKASSOPENBOBDOOR, COOKASSOPENBOBDOOR2, PRINCEALIOPENBOBDOOR, PRINCEALIOPENBOBDOOR2, PRINCEALIOPENBOBDOOR3, PRINCEALIOPENBOBDOOR4,//PRINCE ALI RESCUE
        RESTLESSGHOSTSTART, RESTLESSGHOSTSOLVE, //RESTLESS GHOST
        RUNEMYSTSTARTWALK, RUNEMYSTOPENDUKEDOOR, RUNEMYSTCLIMBSTAIRS_1, RUNEMYSTSTARTTALK, RUNEMYSTTELEPORTLUMBRIDGE, RUNEMYSTWALKTOWIZARDTOWER_1, RUNEMYSTWALKTOWIZARDTOWER_2, RUNEMYSTOPENTOWERDOOR, RUNEMYSTOPENTOWERDOOR2, RUNEMYSTDESCENDLADDER, RUNEMYSTTALKTOSEDRIDOR, RUNEMYSTTELEPORTLUMBRIDGE2, RUNEMYSTWALKTOAUBURY, RUNEMYSTTALKTOAUBURY, RUNEMYSTWALKTOWIZARDTOWER_3, RUNEMYSTOPENTOWERDOOR4, RUNEMYSTOPENTOWERDOOR3, RUNEMYSTDESCENDLADDER2, RUNEMYSTENDTALK, //RUNE MYSTERIES
        DORICSTARTWALK, DORICOPENDOOR, DORICSTARTTALK, DORICWALKTOMINE, DORICMINEROCKS, DORICWALKTOSTAIRS, DORICCLIMBSTAIRS, DORICOPENDOOR2, DORICWALKBACK, DORICOPENDOORLAST, DORICENDTALK,// //Doric's Quest
        BLOODPACTSTARTWALK, BLOODPACTSTARTTALK, BLOODPACTWATCHCUTSCENE, BLOODPACTKILLFIRSTCULTIST, BLOODPACTKILLSECONDCULTIST, BLOODPACTPULLBRIDGE, BLOODPACTKILLLASTCULTIST, ENDQUEST, BLOODPACTWALKTOFIRST, BLOODPACTWALKTOSECOND, BLOODPACTWALKTOLAST, BLOODPACTDESCENDTRAP, OPENDORICDOOR, DORICBOBGETPICK, DORICOPENBOBDOOR, DORICWALKTOBOB, //BLOODPACT
        VampStartQuest, VampFirstTalk, VampClimbStairs, VampGetOnion, VampDownStairs, VampOpenDoor, VampWalkVarrock, VampOpenDoorBar, VampTalkDoctor, VampGetBeer, VampTalkDoctor2, VampOpenDoorBar2, VampWalkCastle, VampOpenCastle, VampToStairs, VampDownCastle, VampOpenChest, VampKillVamp, VampUpStairs, VampToExit, VampOpenExit, VampWalkLast, VampOpenLast, VampTalkLast, //Vampire slayer
        GunFirstWalk, GunFirstTalk, GunWalkEdge, GunGetRing, GunWalkDwarf, GunTalkDwarf, GunWalkGud, GunTalkGud, GunWalkFather, GunTalkFather, GunWalkGud2, GunTalkGud2, GunWalkDwarf2, GunTalkDwarf2, GunWalkGud3, GunTalkGud3, FinishQuest, GunOpenCowGate, GunFirstWalk_2        // Gunnar's ground
    }
    int coinsid = 995;
    int bronzepickid = 1265;
    int woolid = 1737;
    int ballofwoolid = 1759;
    int nedid = 918;
    int tinderboxid = 590;
    int bronzehatchetid = 1351;
    int treeid = 38760;
    int aggieid = 922;
    int bucketid = 1925;
    int[] clayrocksid = new int[]{10578, 10579, 10577};
    int clayid = 434;
    int shearsid = 1735;
    int potid = 1931;
    /*VARIABLES FOR COOKS ASSISTANT*/
    int ladderID = 36687;
    int ladderID2 = 29355;
    RSTile lumbridge1 = new RSTile(3225, 3222, 0);
    RSTile lumbridge2 = new RSTile(3218, 3213, 0);
    boolean cookassstarted = false;
    boolean telied = false;
    int nextTileIndex = -1;
    RSTile[] cookassstartpath = new RSTile[]{new RSTile(3214, 3219), new RSTile(3208, 3215)};
    RSTile[] cookasspathtobob = new RSTile[]{new RSTile(3212, 3210, 0), new RSTile(3218, 3218, 0), new RSTile(3226, 3218, 0), new RSTile(3233, 3215, 0), new RSTile(3235, 3207, 0), new RSTile(3234, 3203, 0)};
    RSTile[] cookasspathtostore = new RSTile[]{new RSTile(3222, 3218), new RSTile(3228, 3219), new RSTile(3233, 3223), new RSTile(3230, 3230), new RSTile(3225, 3236), new RSTile(3220, 3240), new RSTile(3214, 3243)};
    RSTile[] cookasspathtograins = new RSTile[]{new RSTile(3216, 3251), new RSTile(3216, 3256), new RSTile(3216, 3261), new RSTile(3216, 3268), new RSTile(3214, 3276), new RSTile(3208, 3279), new RSTile(3200, 3280), new RSTile(3192, 3282), new RSTile(3183, 3286), new RSTile(3174, 3286), new RSTile(3168, 3289), new RSTile(3165, 3295)};
    RSTile[] cookasspathtoegg = new RSTile[]{new RSTile(3219, 3243), new RSTile(3224, 3248), new RSTile(3230, 3251), new RSTile(3231, 3257), new RSTile(3235, 3261), new RSTile(3241, 3262), new RSTile(3240, 3267), new RSTile(3239, 3275), new RSTile(3238, 3282), new RSTile(3238, 3289), new RSTile(3237, 3295)};
    RSTile[] cookasspathtocows = new RSTile[]{new RSTile(3239, 3289), new RSTile(3241, 3281), new RSTile(3245, 3275), new RSTile(3248, 3269), new RSTile(3252, 3266)};
    int cookasscookid = 278;
    int cookassshopkeeper = 520;
    int cookasspotid = 1931;
    int cookassbowlid = 1923;
    int cookassbobid = 519;
    int cookasswheatobjid = 15506;
    int cookasswheatid = 1947;
    int cookassmillerid = 3806;
    int cookasssupereggid = 15412;
    int cookassgillieid = 3807;
    int cookassprizedcowid = 44432;
    private final int pot = 1931;
    private final int bucket = 1925;
    //Tiles
    private RSTile LadderTile = new RSTile(3208, 9616);
    private RSTile BucketTile = new RSTile(3216, 9624);
    private RSTile GateTile = new RSTile(3252, 3267);
    private RSTile BeginTile = new RSTile(3216, 3220);

    /*VARIABLES FOR PRINCE ALI RESCUE*/
    RSTile[] princealipathtohassan = new RSTile[]{new RSTile(3228, 3219), new RSTile(3235, 3213), new RSTile(3235, 3206), new RSTile(3238, 3200), new RSTile(3241, 3195), new RSTile(3243, 3188), new RSTile(3241, 3181), new RSTile(3239, 3174), new RSTile(3242, 3169), new RSTile(3249, 3170), new RSTile(3256, 3173), new RSTile(3259, 3173), new RSTile(3267, 3176), new RSTile(3275, 3177), new RSTile(3280, 3181), new RSTile(3285, 3181), new RSTile(3291, 3180), new RSTile(3290, 3173), new RSTile(3292, 3167), new RSTile(3296, 3164), new RSTile(3299, 3162)};
    RSTile[] princealipathtoosman_from_hassan = new RSTile[]{new RSTile(3293, 3165), new RSTile(3290, 3172), new RSTile(3292, 3179), new RSTile(3287, 3180)};
    RSTile[] princealiwalktobob = new RSTile[]{new RSTile(3280, 3181), new RSTile(3274, 3177), new RSTile(3266, 3177), new RSTile(3260, 3174), new RSTile(3252, 3174), new RSTile(3247, 3169), new RSTile(3240, 3172), new RSTile(3241, 3179), new RSTile(3241, 3186), new RSTile(3243, 3192), new RSTile(3240, 3197), new RSTile(3237, 3202), new RSTile(3233, 3203)};
    RSTile[] princealipathtomine = new RSTile[]{new RSTile(3237, 3201), new RSTile(3240, 3196), new RSTile(3244, 3191), new RSTile(3241, 3184), new RSTile(3240, 3177), new RSTile(3239, 3170), new RSTile(3238, 3163), new RSTile(3235, 3157), new RSTile(3232, 3152), new RSTile(3230, 3149)};
    RSTile[] princealipathtofurnace = new RSTile[]{new RSTile(3229, 3149), new RSTile(3233, 3153), new RSTile(3237, 3158), new RSTile(3240, 3164), new RSTile(3246, 3168), new RSTile(3252, 3173), new RSTile(3259, 3173), new RSTile(3266, 3176), new RSTile(3273, 3177), new RSTile(3278, 3182), new RSTile(3274, 3186)};
    RSTile[] princealipathtotree = new RSTile[]{new RSTile(3235, 3206), new RSTile(3235, 3212), new RSTile(3235, 3218), new RSTile(3235, 3223), new RSTile(3231, 3228)};
    RSTile[] princealipathtostore = new RSTile[]{new RSTile(3230, 3232), new RSTile(3225, 3236), new RSTile(3221, 3240), new RSTile(3215, 3242)};
    RSTile[] princealipathtosheep = new RSTile[]{new RSTile(3215, 3249), new RSTile(3215, 3254), new RSTile(3215, 3259), new RSTile(3213, 3262)};
    RSTile[] princealipathtospinningwheel = new RSTile[]{new RSTile(3213, 3262), new RSTile(3217, 3258), new RSTile(3218, 3254), new RSTile(3219, 3250), new RSTile(3220, 3246), new RSTile(3222, 3242), new RSTile(3225, 3237), new RSTile(3229, 3232), new RSTile(3231, 3226), new RSTile(3231, 3219), new RSTile(3224, 3219), new RSTile(3218, 3218), new RSTile(3214, 3214), new RSTile(3210, 3210), new RSTile(3207, 3210)};
    RSTile[] princealipathtoned = new RSTile[]{new RSTile(3219, 3224), new RSTile(3214, 3230), new RSTile(3207, 3231), new RSTile(3203, 3227), new RSTile(3203, 3221), new RSTile(3198, 3218), new RSTile(3190, 3220), new RSTile(3183, 3221), new RSTile(3176, 3225), new RSTile(3169, 3229), new RSTile(3161, 3232), new RSTile(3154, 3235), new RSTile(3148, 3239), new RSTile(3142, 3242), new RSTile(3136, 3243), new RSTile(3130, 3249), new RSTile(3123, 3250), new RSTile(3117, 3250), new RSTile(3113, 3255), new RSTile(3108, 3254), new RSTile(3103, 3257)};
    RSTile[] princealipathtopump = new RSTile[]{new RSTile(3101, 3261), new RSTile(3095, 3265), new RSTile(3093, 3270)};
    RSTile[] princealipathtoonions = new RSTile[]{new RSTile(3088, 3273), new RSTile(3082, 3276), new RSTile(3076, 3276), new RSTile(3071, 3276), new RSTile(3064, 3276), new RSTile(3058, 3276), new RSTile(3051, 3275), new RSTile(3045, 3274), new RSTile(3039, 3273), new RSTile(3036, 3269), new RSTile(3032, 3265), new RSTile(3027, 3263), new RSTile(3021, 3264), new RSTile(3015, 3263), new RSTile(3009, 3263), new RSTile(3004, 3262), new RSTile(2998, 3262), new RSTile(2992, 3260), new RSTile(2986, 3260), new RSTile(2981, 3258), new RSTile(2975, 3258), new RSTile(2968, 3258), new RSTile(2962, 3255), new RSTile(2957, 3255), new RSTile(2952, 3253)};
    RSTile[] princealipathtoaggie = new RSTile[]{new RSTile(2960, 3254), new RSTile(2966, 3256), new RSTile(2972, 3259), new RSTile(2978, 3262), new RSTile(2984, 3263), new RSTile(2991, 3265), new RSTile(2998, 3268), new RSTile(3005, 3270), new RSTile(3011, 3271), new RSTile(3018, 3273), new RSTile(3025, 3276), new RSTile(3031, 3277), new RSTile(3038, 3277), new RSTile(3044, 3276), new RSTile(3051, 3276), new RSTile(3058, 3276), new RSTile(3065, 3276), new RSTile(3071, 3276), new RSTile(3075, 3272), new RSTile(3078, 3268), new RSTile(3080, 3264), new RSTile(3080, 3258), new RSTile(3085, 3257), new RSTile(3089, 3259)};
    RSTile[] princealipathtoclay = new RSTile[]{new RSTile(3089, 3259), new RSTile(3094, 3264), new RSTile(3094, 3270), new RSTile(3095, 3276), new RSTile(3095, 3282), new RSTile(3094, 3287), new RSTile(3089, 3292), new RSTile(3088, 3297), new RSTile(3086, 3302), new RSTile(3084, 3307), new RSTile(3083, 3313), new RSTile(3079, 3320), new RSTile(3078, 3326), new RSTile(3078, 3332), new RSTile(3076, 3338), new RSTile(3076, 3343), new RSTile(3077, 3349), new RSTile(3077, 3355), new RSTile(3078, 3361), new RSTile(3076, 3367), new RSTile(3076, 3373), new RSTile(3073, 3378), new RSTile(3075, 3383), new RSTile(3078, 3389), new RSTile(3079, 3395), new RSTile(3081, 3399)};
    RSTile[] princealipathtovarrock = new RSTile[]{new RSTile(3086, 3403), new RSTile(3091, 3406), new RSTile(3095, 3411), new RSTile(3098, 3416), new RSTile(3103, 3420), new RSTile(3110, 3421), new RSTile(3117, 3420), new RSTile(3122, 3416), new RSTile(3129, 3416), new RSTile(3136, 3417), new RSTile(3143, 3417), new RSTile(3150, 3417), new RSTile(3157, 3419), new RSTile(3162, 3421), new RSTile(3166, 3425), new RSTile(3172, 3428), new RSTile(3178, 3428), new RSTile(3185, 3429), new RSTile(3191, 3429), new RSTile(3198, 3429), new RSTile(3205, 3429), new RSTile(3211, 3428)};
    RSTile[] princealipathtoinn = new RSTile[]{new RSTile(3209, 3415), new RSTile(3211, 3409), new RSTile(3212, 3403), new RSTile(3212, 3397), new RSTile(3217, 3395), new RSTile(3222, 3397), new RSTile(3225, 3396)};
    RSTile[] princealipathtoredberries = new RSTile[]{new RSTile(3232, 3404), new RSTile(3239, 3402), new RSTile(3244, 3406), new RSTile(3246, 3412), new RSTile(3252, 3412), new RSTile(3259, 3413), new RSTile(3264, 3417), new RSTile(3266, 3422), new RSTile(3268, 3427), new RSTile(3272, 3428), new RSTile(3277, 3424), new RSTile(3281, 3420), new RSTile(3284, 3414), new RSTile(3288, 3409), new RSTile(3291, 3403), new RSTile(3291, 3397), new RSTile(3291, 3391), new RSTile(3292, 3384), new RSTile(3293, 3380), new RSTile(3291, 3377), new RSTile(3286, 3373), new RSTile(3280, 3373), new RSTile(3276, 3373)};
    RSTile[] princealipathtojail = new RSTile[]{new RSTile(3219, 3224), new RSTile(3214, 3230), new RSTile(3207, 3231), new RSTile(3203, 3227), new RSTile(3203, 3221), new RSTile(3198, 3218), new RSTile(3190, 3220), new RSTile(3183, 3221), new RSTile(3176, 3225), new RSTile(3169, 3229), new RSTile(3161, 3232), new RSTile(3154, 3235), new RSTile(3148, 3239), new RSTile(3142, 3242), new RSTile(3136, 3243), new RSTile(3132, 3245), new RSTile(3128, 3247)};
    RSTile[] princealipathtoaggie2 = new RSTile[]{new RSTile(3123, 3248), new RSTile(3117, 3250), new RSTile(3113, 3255), new RSTile(3107, 3254), new RSTile(3101, 3254), new RSTile(3095, 3256), new RSTile(3089, 3259)};
    RSTile[] princealipathtoosman = new RSTile[]{new RSTile(3228, 3219), new RSTile(3235, 3213), new RSTile(3235, 3206), new RSTile(3238, 3200), new RSTile(3241, 3195), new RSTile(3243, 3188), new RSTile(3241, 3181), new RSTile(3239, 3174), new RSTile(3242, 3169), new RSTile(3249, 3170), new RSTile(3256, 3173), new RSTile(3259, 3173), new RSTile(3267, 3176), new RSTile(3275, 3177), new RSTile(3280, 3181), new RSTile(3285, 3180)};
    RSTile[] princealipathtoleela = new RSTile[]{new RSTile(3219, 3224), new RSTile(3214, 3230), new RSTile(3207, 3231), new RSTile(3203, 3227), new RSTile(3203, 3221), new RSTile(3198, 3218), new RSTile(3190, 3220), new RSTile(3183, 3221), new RSTile(3176, 3225), new RSTile(3169, 3229), new RSTile(3161, 3232), new RSTile(3154, 3235), new RSTile(3148, 3239), new RSTile(3142, 3242), new RSTile(3136, 3243), new RSTile(3132, 3245), new RSTile(3128, 3250), new RSTile(3124, 3254), new RSTile(3118, 3257), new RSTile(3117, 3261), new RSTile(3113, 3262)};
    RSTile[] princealipathtojail2 = new RSTile[]{new RSTile(3118, 3259), new RSTile(3121, 3254), new RSTile(3124, 3250), new RSTile(3128, 3248)};
    int princealiosmanid = 5282;
    int princealihassanid = 923;
    int princealileelaid = 915;
    int[] princealicopperrocks = new int[]{3027, 3229};
    int[] princealitinrocks = new int[]{3038, 3245};
    int princealicopperoreid = 436;
    int princealitinoreid = 438;
    int princealifurnaceid = 11666;
    int princealibronzebarid = 2349;
    int[] princealishearablesheep = new int[]{43, 5160, 1765, 5157, 5161};
    int princealispinningwheelid = 36970;
    int princealioniongroundid = 3366;
    int princealionionid = 1957;
    //VARIABLES FOR RESTLESS GHOST
    public boolean TalkedToFather1 = false;
    public boolean TalkedToFather2 = false;
    public boolean WalkedToFather2 = false;
    public boolean TalkedToGhost = false;
    public boolean WalkedToGhost = false;
    public boolean WalkedToMine = false;
    public boolean HeadGained = false;
    public boolean WalkedToGhost2 = false;
    
    public boolean TalkedToGhost2 = false;
    public boolean openedChest = true;
    public boolean headGained = false;
    public int amulet = 552;
    private int chest = 2145;
    private int openChest = 15061;
    private RSTile tile = new RSTile(3212, 3157);
    private RSTile frontOfHouse = new RSTile(3207, 3152);
    private RSTile inHouse = new RSTile(3207, 3150);
    private RSTile ghostTile = new RSTile(3249, 3193);
    private RSTile mineTile = new RSTile(3235, 3144);
    private RSArea mineArea = new RSArea(new RSTile(3233, 3143), new RSTile(3236, 3147));
    private RSArea frontOfHouseArea = new RSArea(new RSTile(3209, 3155), new RSTile(3204, 3152));
    private RSArea inHouseArea = new RSArea(new RSTile(3209, 3148), new RSTile(3204, 3149));
    private RSArea atGhost = new RSArea(new RSTile[]{new RSTile(3247, 3195), new RSTile(3251, 3195), new RSTile(3251, 3191), new RSTile(3247, 3191)});
    private int door = 45539;

    /*VARIABLES FOR RUNE MYSTERIES*/
    RSTile[] runemyststartpath = new RSTile[]{new RSTile(3215, 3219), new RSTile(3214, 3225), new RSTile(3206, 3228)};
    RSTile[] runemystpathtotower_1 = new RSTile[]{new RSTile(3219, 3224), new RSTile(3214, 3230), new RSTile(3207, 3231), new RSTile(3203, 3227), new RSTile(3203, 3221)};
    RSTile[] runemystpathtoaubury = new RSTile[]{new RSTile(3227, 3219), new RSTile(3234, 3222), new RSTile(3240, 3226), new RSTile(3246, 3226), new RSTile(3252, 3227), new RSTile(3257, 3230), new RSTile(3257, 3237), new RSTile(3254, 3242), new RSTile(3252, 3249), new RSTile(3250, 3256), new RSTile(3250, 3263), new RSTile(3249, 3269), new RSTile(3245, 3275), new RSTile(3241, 3281), new RSTile(3238, 3288), new RSTile(3238, 3295), new RSTile(3238, 3302), new RSTile(3238, 3308), new RSTile(3239, 3314), new RSTile(3239, 3320), new RSTile(3240, 3326), new RSTile(3240, 3332), new RSTile(3235, 3337), new RSTile(3229, 3338), new RSTile(3226, 3344), new RSTile(3225, 3350), new RSTile(3224, 3356), new RSTile(3220, 3360), new RSTile(3217, 3365), new RSTile(3214, 3371), new RSTile(3212, 3377), new RSTile(3212, 3384), new RSTile(3213, 3389), new RSTile(3221, 3391), new RSTile(3224, 3391, 0), new RSTile(3231, 3392, 0), new RSTile(3231, 3392, 0), new RSTile(3235, 3397, 0), new RSTile(3241, 3400, 0), new RSTile(3246, 3401, 0), new RSTile(3253, 3398), new RSTile(3253, 3402)};
    RSTile wizardTowerTile = new RSTile(3109, 3168, 0);
    RSTile auburyTile = new RSTile(3220, 3391, 0);
    int runemystladderid = 2147;
    int runemystdukeid = 741;
    int runemystsedridorid = 300;
    int runemystauburyid = 5913;

    /*VARIABLES FOR BLOOD PACT*/
    int bloodpactxenia = 9633;
    int bloodpactkyle = 9628;
    int bloodpacttrap = 48797;
    boolean bloodpacttalk;
    private int bloodpactinitialx;
    private int bloodpactinitialy;
    /*VARIABLES FOR DORIC'S QUEST*/
    int Dwarf = 284;
    int doorClosed = 1530;
    int doorClosed2 = 11714;
    int stairsid = 30944;
    int clayOreID = 434;
    public int[] clayOre = {5766, 5767};
    public int[] ironOre = {5775, 5774};
    private static final RSTile[] toMine = {new RSTile(2948, 3445), new RSTile(2949, 3438), new RSTile(2951, 3432), new RSTile(2955, 3426), new RSTile(2958, 3420), new RSTile(2962, 3415), new RSTile(2966, 3410), new RSTile(2966, 3404), new RSTile(2966, 3398), new RSTile(2967, 3391), new RSTile(2969, 3385), new RSTile(2973, 3380), new RSTile(2979, 3379), new RSTile(2985, 3375), new RSTile(2990, 3371), new RSTile(2996, 3369), new RSTile(3002, 3365), new RSTile(3009, 3365), new RSTile(3015, 3364), new RSTile(3022, 3364), new RSTile(3029, 3364), new RSTile(3034, 3367), new RSTile(3041, 3369), new RSTile(3048, 3370), new RSTile(3055, 3370), new RSTile(3060, 3373)};
    private static final RSTile[] toDoric = reverse(toMine);
    private static final RSTile[] pathtodoric = {new RSTile(3229, 3219), new RSTile(3232, 3224), new RSTile(3230, 3230), new RSTile(3225, 3234), new RSTile(3223, 3241), new RSTile(3220, 3247), new RSTile(3218, 3253), new RSTile(3215, 3267), new RSTile(3215, 3274), new RSTile(3211, 3279), new RSTile(3203, 3279), new RSTile(3195, 3280), new RSTile(3191, 3285), new RSTile(3189, 3291), new RSTile(3188, 3296), new RSTile(3187, 3302), new RSTile(3183, 3307), new RSTile(3178, 3312), new RSTile(3177, 3319), new RSTile(3175, 3326), new RSTile(3175, 3333), new RSTile(3175, 3339), new RSTile(3176, 3346), new RSTile(3176, 3352), new RSTile(3176, 3358), new RSTile(3174, 3363), new RSTile(3168, 3366), new RSTile(3163, 3371), new RSTile(3159, 3377), new RSTile(3156, 3382), new RSTile(3151, 3386), new RSTile(3146, 3390), new RSTile(3140, 3393), new RSTile(3134, 3395), new RSTile(3128, 3397), new RSTile(3124, 3401), new RSTile(3121, 3406), new RSTile(3118, 3412), new RSTile(3114, 3416), new RSTile(3110, 3420), new RSTile(3103, 3421), new RSTile(3096, 3421), new RSTile(3090, 3421), new RSTile(3084, 3422), new RSTile(3077, 3421), new RSTile(3072, 3417), new RSTile(3065, 3417), new RSTile(3058, 3416), new RSTile(3052, 3416), new RSTile(3046, 3418), new RSTile(3041, 3423), new RSTile(3036, 3427), new RSTile(3029, 3428), new RSTile(3022, 3429), new RSTile(3015, 3430), new RSTile(3009, 3431), new RSTile(3002, 3432), new RSTile(2995, 3432), new RSTile(2990, 3428), new RSTile(2988, 3422), new RSTile(2981, 3423), new RSTile(2976, 3426), new RSTile(2971, 3430), new RSTile(2966, 3434), new RSTile(2960, 3437), new RSTile(2954, 3440), new RSTile(2948, 3444), new RSTile(2949, 3450)};
    RSTile[] doricpathtobob = new RSTile[]{new RSTile(3226, 3218, 0), new RSTile(3233, 3215, 0), new RSTile(3235, 3207, 0), new RSTile(3234, 3203, 0)};
    RSTile LastTile = new RSTile(2949, 3450);
    RSTile ladder = new RSTile(3061, 3374);
    RSTile clayTile = new RSTile(3054, 9818);
    RSTile doorTile = new RSTile(3061, 3374);
    RSTile lastTile = new RSTile(3061, 3374);
    RSTile walkIn = new RSTile(2951, 3451);
    RSArea StairArea = new RSArea(new RSTile(3062, 3374), new RSTile(3059, 3373));
    RSArea clayArea = new RSArea(new RSTile(3052, 9816), new RSTile(3054, 9821));
    RSArea IronArea = new RSArea(new RSTile(3029, 9823), new RSTile(3033, 9827));
    RSArea StairAreaDown = new RSArea(new RSTile(3058, 9744), new RSTile(3056, 9779));
    RSArea FrontOfShed = new RSArea(new RSTile(2955, 3447), new RSTile(2944, 3458));

    /*VARIABLES FOR GUNNAR'S GROUND*/
    RSTile[] gunpathtogunnar = new RSTile[]{ new RSTile(3228, 3219), new RSTile(3232, 3225), new RSTile(3225, 3235), new RSTile(3219, 3249), new RSTile(3216, 3265), new RSTile(3210, 3279), new RSTile(3196, 3279), new RSTile(3183, 3286), new RSTile(3174, 3286), new RSTile(3163, 3288), new RSTile(3158, 3291), new RSTile(3146, 3295), new RSTile(3134, 3295), new RSTile(3120, 3298), new RSTile(3111, 3307), new RSTile(3100, 3318), new RSTile(3085, 3323), new RSTile(3078, 3333) };    RSTile[] gunpathtogunnar_2 = new RSTile[]{new RSTile(3177, 3320), new RSTile(3176, 3327), new RSTile(3176, 3334), new RSTile(3178, 3341), new RSTile(3177, 3347), new RSTile(3177, 3353), new RSTile(3176, 3360), new RSTile(3171, 3364), new RSTile(3166, 3368), new RSTile(3165, 3375), new RSTile(3163, 3381), new RSTile(3160, 3388), new RSTile(3157, 3394), new RSTile(3152, 3398), new RSTile(3147, 3402), new RSTile(3142, 3407), new RSTile(3136, 3408), new RSTile(3129, 3410), new RSTile(3122, 3410), new RSTile(3117, 3414), new RSTile(3112, 3418), new RSTile(3105, 3421), new RSTile(3099, 3422)};
    /*VARIABLES FOR VAMPIRE SLAYER*/
    RSTile[] vamppathtovarrock = new RSTile[]{new RSTile(3229, 3219), new RSTile(3234, 3222), new RSTile(3240, 3225), new RSTile(3246, 3225), new RSTile(3253, 3226), new RSTile(3258, 3229), new RSTile(3259, 3235), new RSTile(3259, 3241), new RSTile(3256, 3247), new RSTile(3252, 3252), new RSTile(3251, 3258), new RSTile(3250, 3264), new RSTile(3249, 3269), new RSTile(3246, 3274), new RSTile(3241, 3278), new RSTile(3238, 3284), new RSTile(3238, 3290), new RSTile(3238, 3296), new RSTile(3238, 3302), new RSTile(3238, 3308), new RSTile(3239, 3314), new RSTile(3238, 3319), new RSTile(3239, 3324), new RSTile(3240, 3330), new RSTile(3238, 3337), new RSTile(3231, 3338), new RSTile(3227, 3342), new RSTile(3226, 3348), new RSTile(3225, 3354), new RSTile(3222, 3360), new RSTile(3218, 3365), new RSTile(3214, 3370), new RSTile(3212, 3376), new RSTile(3212, 3382), new RSTile(3212, 3388), new RSTile(3212, 3393), new RSTile(3215, 3395)};

    public int min(int a, int b) {
        return a < b ? a : b;
    }

    public int max(int a, int b) {
        return a > b ? a : b;
    }

    public boolean inArea(RSTile c1, RSTile c2) {
        int minx = min(c1.getX(), c2.getX());
        int maxx = max(c1.getX(), c2.getX());
        int miny = min(c1.getY(), c2.getY());
        int maxy = max(c1.getY(), c2.getY());
        int curx = getMyPlayer().getLocation().getX();
        int cury = getMyPlayer().getLocation().getY();
        int curz = getMyPlayer().getLocation().getZ();
        return (curx >= minx && curx <= maxx && cury <= maxy && cury >= miny && curz == c1.getZ());
    }

    public boolean walkPath(RSTile[] path) {
        if (nextTileIndex == -1 || calc.distanceTo(path[nextTileIndex]) < 7) {
            if (nextTileIndex == path.length - 1) {
                sleep(400, 500);
                while (getMyPlayer().isMoving()) {
                    sleep(300, 400);
                }
                sleep(1000, 1100);
                nextTileIndex = -1;
                return true;
            } else {
                nextTileIndex++;
                if (walking.getEnergy() >= random(30, 40)) {
                    walking.setRun(true);
                }
                if (nextTileIndex == path.length - 1) {
                    walking.walkTileMM(path[nextTileIndex], 0, 0);
                } else {
                    walking.walkTileMM(path[nextTileIndex]);
                }

                try {
                    sleep(1000, 1500);
                    while (calc.distanceTo(path[nextTileIndex]) > 6) {
                        if (interfaces.canContinue()) {
                            sleep(1000, 1100);
                            interfaces.clickContinue();
                            sleep(1000, 1100);
                            clickDialogOption("Leave the starting area.");
                            sleep(1000, 1100);
                        } else {

                            if (!getMyPlayer().isMoving()) {
                                walking.walkTileMM(new RSTile((getMyPlayer().getLocation().getX() + path[nextTileIndex].getX()) / 2, (getMyPlayer().getLocation().getY() + path[nextTileIndex].getY()) / 2));
                            }
                            if (calc.distanceTo(path[nextTileIndex]) <= 6) {
                                break;
                            }
                            sleep(1000, 1100);
                        }
                        if (calc.distanceTo(path[nextTileIndex]) <= 6) {
                            break;
                        }
                        sleep(300, 400);
                    }
                } catch (Exception e) {
                }
                return false;
            }
        }
        return false;
    }

    public RSComponent getComponentContaining(String contain) {
        final RSInterface[] valid = interfaces.getAll();
        for (final RSInterface iface : valid) {
            if (iface.getIndex() != 137) {
                final int len = iface.getChildCount();
                for (int i = 0; i < len; i++) {
                    final RSComponent child = iface.getComponent(i);
                    if (child.containsText(contain) && child.isValid() && child.getAbsoluteX() > 10 && child.getAbsoluteY() > 300) {
                        return child;
                    }
                }
            }
        }
        return null;
    }

    public int loop() {
        if (currentAction != null) {
            log(currentAction.toString());
        }
        try {
            camera.setPitch(true);
            if (!processed) {
                if (canContinue) {
                    doquests = new ArrayList<String>();
                    if (gui.todoListModel.getSize() == 0) {
                        log("No Quest Selected");
                        stopScript();
                    }
                    if (!canStart) {
                        stopScript();
                    }
                    for (int x = 0; x < gui.todoListModel.getSize(); x++) {
                        String doquest = (String) gui.todoListModel.getElementAt(x);
                        doquests.add(doquest);
                    }
                    processed = true;
                    for (String quest : doquests) {
                        canContinue2 = false;
                        promptProgress(quest);
                    }
                    actions.add(Action.ENDALL);
                    //currentQuest = doquests.get(0);
                    //updateNextAction();
                    currentAction = actions.get(nextActionIndex++);
                    start = System.currentTimeMillis();
                }
                return random(200, 400);
            } else {
                switch (currentAction) {
                    case QUESTSTART:
                        log("Dropping");
                        inventory.dropAllExcept(questitems);
                        teleportLumbridge();
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case ENDALL:
                        log("Finished all quests :D");
                        String time = "";
                        long cur = System.currentTimeMillis();
                        long timepassed = cur - start;
                        time += (timepassed / 3600000) + ":";
                        time += (timepassed / 60000) % 60 + ":";
                        time += (timepassed / 1000) % 60 + "";
                        log("Total runtime: " + time);
                        stopScript();
                    case COOKASSSTARTWALK:
                        questName = "Cook's assistant";
                        questAction = "Walking to the Cook";
                        sleep(1000, 1100);
                        if (inArea(lumbridge1, lumbridge2) || telied) {
                            if (getMyPlayer().getAnimation() != -1) {
                                return random(500, 1000);
                            }
                            telied = true;
                            if (!walkPath(cookassstartpath)) {
                                return random(400, 500);
                            } else {
                                currentAction = Action.COOKASSSTARTTALK;
                                questAction = "Talking to Cook";
                                telied = false;
                            }
                        } else {
                            if (getMyPlayer().getAnimation() != -1) {
                                return random(500, 1000);
                            }
                            game.openTab(Tab.MAGIC);
                            try {
                                questAction = "Teleing to Lumbridge";
                                teleportLumbridge();
                                return random(300, 300);

                            } catch (Exception e) {
                            }
                        }
                        break;

                    case COOKASSSTARTTALK:
                        questAction = "Talking to the Cook";
                        startTalk(cookasscookid, true);
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        //log("Click");
                        boolean done = false;
                        for (int x = 0; x < 3; x++) {
                            //log("wait");
                            sleep(1000, 1100);
                            //log("wait over");
                            RSComponent comp = getComponentContaining("What's wrong?");
                            if (comp != null) {
                                comp.doClick(true);
                                done = true;
                                break;
                            } else {
                                //log("Click2");
                                comp = getComponentContaining("Help you with what?");
                                if (comp != null) {
                                    comp.doClick(true);
                                    done = true;
                                    break;
                                } else {
                                }
                            }
                        }
                        if (!done) {
                            currentAction = Action.COOKASSOBTAINPOT;
                            break;
                        }
                        sleep(1000, 1100);
                        while (!interfaces.canContinue()) {
                            sleep(300, 400);
                        }
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(1000, 1100);
                        while (!interfaces.canContinue()) {
                            sleep(300, 400);
                        }
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(1000, 1100);
                        while (!interfaces.canContinue()) {
                            sleep(300, 400);
                        }
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(2000, 2100);
                        while (!interfaces.get(178).isValid()) {
                            sleep(400, 500);
                            if (interfaces.canContinue()) {
                                interfaces.clickContinue();
                            }
                            sleep(1000, 1100);
                        }
                        if (interfaces.get(178).isValid()) {

                            interfaces.getComponent(178, 78).doClick(); // Start quest interface - button

                        }
                      
                        currentAction = Action.COOKASSOBTAINPOT;
                        break;
                    case COOKASSOBTAINPOT:
                          sleep(1000);
                    GetPot();
                    sleep(3000);

                     if(!inventory.containsOneOf(bucket)){
                GetBucket();
                        sleep(1000, 1100);
                        }
                    if(inventory.containsOneOf(bucket)){
                    currentAction = Action.COOKASSWALKTOSTORE;
                        }
                        break;
                    case COOKASSWALKTOSTORE:
                       if (!walkPath(cookasspathtostore)) {
                            return random(300, 400);
                        }
                    currentAction = Action.COOKASSGETSTOREITEMS;
                        break;
                    case COOKASSGETSTOREITEMS:

                        questAction = "Buying bucket and pot";
                        RSNPC shopkeeper = npcs.getNearest(cookassshopkeeper);
                        if (shopkeeper == null || !shopkeeper.isOnScreen()) {
                            return random(400, 500);
                        }
                        shopkeeper.interact("Trade");
                        sleep(300, 400);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        while (!interfaces.get(620).isValid()) {
                            sleep(1300, 1400);
                            shopkeeper = npcs.getNearest(cookassshopkeeper);
                            if (shopkeeper == null || !shopkeeper.isOnScreen()) {
                                return random(400, 500);
                            }
                            shopkeeper.interact("Trade");
                        }
                        sleep(300, 500);
                        RSItem pickaxe = inventory.getItem("Bronze hatchet");
                        while (inventory.containsOneOf(1351)) {
                            pickaxe.interact("Sell 1");
                        }
                        sleep(1500, 1600);
                        while (!inventory.containsOneOf(1931) && !inventory.containsOneOf(15414)) {
                            interactInterface(620, 25, 0, "Buy 1");
                            sleep(1000, 1100);
                        }
                        sleep(500, 600);
                        while (!inventory.containsOneOf(1925) && !inventory.containsOneOf(15413)) {
                            interactInterface(620, 25, 18, "Buy 1");
                            sleep(1000, 1100);
                        }
                        if ((inventory.containsOneOf(15414) || inventory.containsOneOf(1931)) && (inventory.containsOneOf(15413) || inventory.containsOneOf(1925))) {
                            clickInterface(620, 18);
                            while (interfaces.get(620).isValid()) {
                                sleep(300, 400);
                            }
                            currentAction = actions.get(nextActionIndex++);
                        }
                        break;
                    case COOKASSWALKTOGRAINS:
                        questAction = "Walking to grains";
                        if (!walkPath(cookasspathtograins)) {
                            return random(300, 400);
                        } else {
                            sleep(1000, 1100);
                            camera.turnTo(new RSTile(3164, 3295, 0));
                            camera.setPitch(false);
                            while (objects.getNearest(45212) != null && objects.getNearest(45212).isOnScreen()) {
                                objects.getNearest(45212).interact("Open");
                                sleep(1000, 1100);
                            }
                            /*RSObject left = objects.getNearest(45212);
                            RSObject right = objects.getNearest(45210);
                            int averagex = (left.getPoint().x + right.getPoint().x) / 2;
                            int averagey = (left.getPoint().y + right.getPoint().y) / 2;
                            mouse.click(new Point(averagex, averagey), 0, 0, true);*/

                            sleep(1000, 1100);
                            walking.walkTileMM(new RSTile(3160, 3298));
                            sleep(400, 500);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                            sleep(1500, 1600);
                            boolean toggle = false;
                            while (!inArea(new RSTile(3162, 3300, 0), new RSTile(3154, 3296, 0))) {
                                if (toggle) {
                                    camera.turnTo(new RSTile(3164, 3295, 0));
                                } else {
                                    camera.turnTo(new RSTile(3164, 3294, 0));
                                }
                                toggle = !toggle;
                                camera.setPitch(false);
                                /*left = objects.getNearest(45212);
                                right = objects.getNearest(45210);
                                averagex = (left.getPoint().x + right.getPoint().x) / 2;
                                averagey = (left.getPoint().y + right.getPoint().y) / 2;
                                mouse.click(new Point(averagex, averagey), 0, 0, true);*/
                                objects.getNearest(45212).interact("Open");
                                sleep(1000, 1100);
                                walking.walkTileMM(new RSTile(3160, 3298));
                                sleep(400, 500);
                                while (getMyPlayer().isMoving()) {
                                    sleep(300, 400);
                                }
                                sleep(1000, 1100);
                            }
                            currentAction = Action.COOKASSPICKGRAINS;
                            questAction = "Picking some wheat";
                        }
                        break;
                    case COOKASSPICKGRAINS:
                        questAction = "Picking some wheat";
                        RSObject obj = objects.getNearest(cookasswheatobjid);
                        while (obj == null) {
                            obj = objects.getNearest(cookasswheatobjid);
                        }
                        while (!inventory.contains(cookasswheatid)) {
                            obj.interact("Pick");
                            sleep(1000, 1100);
                        }
                        while (getMyPlayer().getAnimation() != -1) {
                            sleep(300, 400);
                        }
                        currentAction = Action.COOKASSWALKTOMILL;
                        questAction = "Walking to mill";
                        break;
                    case COOKASSWALKTOMILL:
                        questAction = "Walking to mill";
                        RSTile mill = new RSTile(3166, 3302, 0);
                        while (calc.distanceTo(mill) > 3) {
                            walking.walkTileMM(mill, 0, 0);
                            sleep(1300, 1400);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                            sleep(1500, 1600);
                        }
                        camera.setPitch(false);
                        camera.turnTo(new RSTile(getMyPlayer().getLocation().getX(), getMyPlayer().getLocation().getY() + 1, 0));
                        RSObject temp = objects.getNearest(45964);
                        while (temp != null) {
                            temp.interact("Open");
                            sleep(2000, 2200);
                            temp = objects.getNearest(45964);
                        }

                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        currentAction = Action.COOKASSSETFINEFLOUR;
                        questAction = "Setting fine flour";
                        break;
                    case COOKASSSETFINEFLOUR:
                        questAction = "Setting fine flour";
                        walking.walkTileOnScreen(new RSTile(3168, 3306, 0));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        RSNPC miller = npcs.getNearest(cookassmillerid);
                        if (miller == null) {
                            sleep(500, 600);
                            miller = npcs.getNearest(cookassmillerid);
                        }
                        miller.interact("Talk");
                        sleep(500, 600);
                        while (!interfaces.canContinue()) {
                            sleep(500, 600);
                            miller.interact("Talk");
                        }
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(1000, 1100);
                        while (!clickDialogOption("I'm looking for extra fine flour.")) {
                        }
                        sleep(1000, 1100);
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(1000, 1100);
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(1000, 1100);
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(1000, 1100);
                        if (interfaces.canContinue()) {
                            interfaces.clickContinue();
                        }
                        sleep(1000, 1100);
                        currentAction = Action.COOKASSMOVETOHOPPER;
                        questAction = "Moving to hopper";
                        break;
                    case COOKASSMOVETOHOPPER:
                        questAction = "Moving to hopper";
                        walking.walkTileOnScreen(new RSTile(3165, 3307, 0));
                        sleep(1500, 1600);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(500, 600);
                        if (getMyPlayer().getLocation().getX() != 3165 || getMyPlayer().getLocation().getY() != 3307) {
                            walking.walkTileOnScreen(new RSTile(3165, 3307, 0));
                            sleep(1000, 1100);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                        }
                        camera.turnTo(new RSTile(3164, 3307, 0));
                        objects.getNearest(36795).interact("Climb");
                        sleep(1000, 1100);
                        while (getMyPlayer().getLocation().getZ() != 1) {
                            sleep(1000, 1100);
                            if (objects.getNearest(36795) != null) {
                                objects.getNearest(36795).interact("Climb");
                            }
                        }
                        objects.getNearest(36796).interact("Climb-up");
                        sleep(1000, 1100);
                        while (getMyPlayer().getLocation().getZ() != 2) {
                            sleep(1000, 1100);
                            if (objects.getNearest(36796) != null) {
                                objects.getNearest(36796).interact("Climb-up");
                            }
                        }
                        sleep(1000, 1100);
                        currentAction = Action.COOKASSOPERATEHOPPER;
                        questAction = "Operating hopper";
                        break;
                    case COOKASSOPERATEHOPPER:
                        questAction = "Operating hopper";
                        RSItem grain = inventory.getItem(cookasswheatid);
                        grain.doClick(true);
                        RSObject hopper = objects.getNearest(36881);

                        hopper.interact("Use Grain -> Hopper");
                        sleep(1000, 1100);
                        while (getMyPlayer().getAnimation() == -1) {
                            grain = inventory.getItem(cookasswheatid);
                            grain.doClick(true);
                            sleep(1000, 1100);
                            hopper = objects.getNearest(36881);
                            hopper.interact("Use Grain -> Hopper");
                            sleep(1000, 1100);
                        }
                        while (getMyPlayer().getAnimation() != -1) {
                            sleep(300, 400);
                        }
                        walking.walkTileMM(new RSTile(3165, 3305));
                        sleep(1500, 1600);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        //camera.turnTo(new RSTile(3166, 3305));
                        //camera.setPitch(true);
                        sleep(2000, 2100);
                        RSObject lever = objects.getNearest(2718);
                        lever.doClick();
                        sleep(1100, 1200);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        while (getMyPlayer().getAnimation() == -1) {
                            lever.doClick();
                            sleep(800, 1000);
                        }
                        currentAction = Action.COOKASSMOVETOBIN;
                        questAction = "Moving to flour bin";
                        break;
                    case COOKASSMOVETOBIN:
                        questAction = "Moving to flour bin";
                        RSObject ladder = objects.getNearest(36797);
                        while (ladder == null) {
                            ladder = objects.getNearest(36797);
                        }
                        ladder.interact("Climb-down");
                        sleep(2000, 2100);
                        while (getMyPlayer().getLocation().getZ() != 1) {
                            sleep(300, 400);
                            ladder = objects.getNearest(36797);
                            if (ladder != null) {
                                ladder.interact("Climb-down");
                            }
                        }
                        ladder = objects.getNearest(36796);
                        while (ladder == null) {
                            ladder = objects.getNearest(36796);
                        }
                        ladder.interact("Climb-down");
                        sleep(2000, 2100);
                        while (getMyPlayer().getLocation().getZ() != 0) {
                            sleep(300, 400);
                            ladder = objects.getNearest(36796);
                            if (ladder != null) {
                                ladder.interact("Climb-down");
                            }
                        }
                        currentAction = Action.COOKASSPICKBIN;
                        questAction = "Taking flour from bin";
                        break;
                    case COOKASSPICKBIN:
                        questAction = "Taking flour from bin";
                        RSObject flourbin = objects.getNearest(36880);
                        flourbin.interact("Take-flour");
                        sleep(1500, 1600);
                        currentAction = Action.COOKASSTELEPORTLUMBRIDGEMILL;
                        break;
                    case COOKASSTELEPORTLUMBRIDGEMILL:
                        questAction = "Teleporting to Lumbridge";
                        teleportLumbridge();
                        telied = true;
                        currentAction = actions.get(nextActionIndex++);
                        questAction = "Walking to egg";
                        return random(300, 400);
                    case COOKASSWALKTOEGG:
                        questAction = "Walking to egg";
                        if (!walkPath(cookasspathtoegg)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.COOKASSOPENEGGGATE;
                        }
                        break;
                    case COOKASSOPENEGGGATE:
                        questAction = "Opening gate";
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        if (objects.getNearest(45208) != null && objects.getNearest(45208).isOnScreen()) {
                            camera.turnTo(new RSTile(getMyPlayer().getLocation().getX() + 1, getMyPlayer().getLocation().getY()));
                            camera.setPitch(false);
                            objects.getNearest(45208).interact("Open");
                        }
                        sleep(1000, 1100);
                        walking.walkTileMM(new RSTile(3229, 3300, 0), 0, 0);
                        sleep(400, 500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        currentAction = Action.COOKASSPICKEGG;
                        questAction = "Picking up egg";
                        break;
                    case COOKASSPICKEGG:
                        questAction = "Picking up egg";
                        sleep(1000, 1100);
                        RSGroundItem superegg = groundItems.getNearest(cookasssupereggid);
                        while (superegg == null || !superegg.isOnScreen()) {
                            sleep(400, 500);
                            log("Waiting for egg...");
                            questAction = "Waiting for egg";
                            superegg = groundItems.getNearest(cookasssupereggid);
                        }
                        sleep(1000, 1100);
                        superegg.interact("Take Super");
                        sleep(1000, 1100);
                        while (inventory.getCount(cookasssupereggid) == 0) {
                            superegg.interact("Take Super");
                            sleep(400, 500);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                            sleep(1500, 1600);
                        }
                        currentAction = Action.COOKASSWALKTOCOWS_OPENDOOR1;
                        questAction = "Walking to cows";
                        break;
                    case COOKASSWALKTOCOWS_OPENDOOR1:
                        questAction = "Walking to cows";
                        walking.walkTileMM(new RSTile(3236, 3295, 0), 0, 0);
                        sleep(400, 500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        sleep(400, 500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        while (objects.getNearest(45208) != null && objects.getNearest(45208).isOnScreen()) {
                            camera.turnTo(new RSTile(3237, 3295, 0));
                            camera.setPitch(false);
                            Point left = objects.getNearest(45208).getPoint();
                            Point right = objects.getNearest(45206).getPoint();
                            int avgx = (left.x + right.x) / 2;
                            int avgy = (left.y + right.y) / 2;
                            mouse.click(avgx, avgy, true);
                            sleep(1000, 1100);
                        }
                        sleep(1000, 1100);
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case COOKASSWALKTOCOWS:
                        questAction = "Walking to cows";
                        if (calc.distanceTo(cookasspathtocows[cookasspathtocows.length - 1]) > 4) {
                            rswebStep(cookasspathtocows[cookasspathtocows.length - 1]);
                            return random(300, 400);
                        } else {
                            currentAction = Action.COOKASSWALKTOCOWS_OPENDOOR2;
                        }
                        sleep(400, 500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        break;
                    case COOKASSWALKTOCOWS_OPENDOOR2:
                        questAction = "Walking to cows";
                        Point p = calc.tileToScreen(new RSTile(3252, 3267, 0));
                        mouse.click(p, true);
                        sleep(400, 500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        if (objects.getNearest(45210) != null && objects.getNearest(45210).isOnScreen()) {
                            camera.turnTo(new RSTile(3251, 3267, 0));
                            camera.setPitch(false);
                            objects.getNearest(45212).interact("Open");
                        }
                        sleep(1000, 1100);
                        currentAction = Action.COOKASSGETMILK;
                        questAction = "Getting milk";
                        break;
                    case COOKASSGETMILK:
                        questAction = "Getting milk";
                        walking.walkTileMM(new RSTile(3259, 3270, 0), 0, 0);
                        sleep(400, 500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        while (!inArea(new RSTile(3262, 3280), new RSTile(3265, 3275))) {
                            walking.walkTileMM(new RSTile(3264, 3276, 0), 0, 0);
                            sleep(400, 500);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                            sleep(1500, 1600);
                        }
                        RSObject prizedcow = objects.getNearest(cookassprizedcowid);
                        while (prizedcow == null || !prizedcow.isOnScreen()) {
                            prizedcow = objects.getNearest(cookassprizedcowid);
                            sleep(300, 400);
                        }
                        prizedcow.interact("Milk");
                        camera.turnTo(new RSTile(3264, 3300));
                        sleep(300, 400);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        camera.turnTo(new RSTile(3300, 3300));
                        prizedcow.interact("Milk");
                        sleep(3000, 4000);
                        while (getMyPlayer().getAnimation() == -1) {
                            camera.turnTo(new RSTile(3264, 3300));
                            prizedcow.interact("Milk");
                            if (inventory.getCount(15413) != 0) {
                                currentAction = Action.COOKASSTELEPORTLUMBRIDGECOWS;
                                questAction = "Teleing to Lumbridge";
                                break;
                            }
                            sleep(3000, 4000);
                        }
                        while (getMyPlayer().getAnimation() != -1) {
                            sleep(300, 400);
                        }
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case COOKASSTELEPORTLUMBRIDGECOWS:
                        questAction = "Teleporting to Lumbridge";
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.COOKASSENDWALK;
                        questAction = "Walking to Cook";
                        return random(300, 400);
                    case COOKASSENDWALK:
                        questAction = "Walking to Cook";
                        if (!walkPath(cookassstartpath)) {
                            return random(400, 500);
                        } else {
                            currentAction = Action.COOKASSENDTALK;
                            questAction = "Ending quest w00t";
                            telied = false;
                        }
                        break;
                    case COOKASSENDTALK:
                        questAction = "Ending quest w00t";
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        RSNPC cook = npcs.getNearest(278);
                        if (cook == null || !cook.isOnScreen()) {
                            log("Cook Not Found!");
                        } else {
                            cook.interact("Talk");
                            sleep(1000, 1100);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                            //mouse.click(cook.getScreenLocation().x, cook.getScreenLocation().y, 1, 1, true);
                            sleep(1000, 1100);
                            while (!interfaces.get(277).isValid()) {
                                while (!clickDialogOption("Click here to continue"));
                                sleep(1000, 1100);
                            }
                            sleep(1000, 1100);
                            currentQuestIndex++;
                            currentAction = actions.get(nextActionIndex++);
                            break;
                        }
                    case PRINCEALISTARTWALK:
                        questName = "Prince Ali Rescue";
                        sleep(1000, 1100);
                        if (inArea(lumbridge1, lumbridge2) || telied) {
                            questAction = "Walking to Hassan";
                            if (getMyPlayer().getAnimation() != -1) {
                                return random(500, 1000);
                            }
                            telied = true;
                            if (!walkPath(princealipathtohassan)) {
                                return random(400, 500);
                            } else {
                                currentAction = Action.PRINCEALISTARTTALK;
                                questAction = "Talking to Hassan";
                                telied = false;
                            }
                        } else {
                            if (getMyPlayer().getAnimation() != -1) {
                                return random(500, 1000);
                            }
                            game.openTab(Tab.MAGIC);
                            try {
                                questAction = "Teleing to Lumbridge";
                                teleportLumbridge();
                                telied = true;
                                return random(300, 400);

                            } catch (Exception e) {
                            }
                        }
                        break;
                    case PRINCEALISTARTTALK:
                        startTalk(princealihassanid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        done = false;
                        for (int x = 0; x < 3; x++) {
                            //log("wait");
                            sleep(1000, 1100);
                            //log("wait over");
                            RSComponent comp = getComponentContaining(("Can I help you? You must need some help here in the desert."));
                            if (comp != null) {
                                comp.doClick(true);
                                done = true;
                                break;
                            } else {
                                done = false;
                            }
                        }
                        if (!done) {
                            currentAction = Action.PRINCEALISTARTWALKOSMAN;
                            questAction = "Walking to Osman";
                            break;
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        currentAction = Action.PRINCEALISTARTWALKOSMAN;
                        questAction = "Walking to Osman";
                        break;
                    case PRINCEALISTARTWALKOSMAN:
                        if (!walkPath(princealipathtoosman_from_hassan)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALISTARTTALKOSMAN;
                            questAction = "Talking to Osman";
                            break;
                        }
                    case PRINCEALISTARTTALKOSMAN:
                        startTalk(princealiosmanid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        done = false;
                        for (int x = 0; x < 3; x++) {
                            //log("wait");
                            sleep(1000, 1100);
                            //log("wait over");
                            RSComponent comp = getComponentContaining("What is the first thing I must do?");
                            if (comp != null) {
                                comp.doClick(true);
                                done = true;
                                break;
                            } else {
                                done = false;
                            }
                        }
                        if (!done) {
                            questAction = "Walking to Bob";
                            currentAction = Action.PRINCEALIWALKTOBOB;
                            break;
                        }

                        sleep(1000, 1100);
                        clickContinue();

                        sleep(1000, 1100);
                        clickContinue();
                        startTalk(princealiosmanid, true);

                        sleep(1000, 1100);
                        clickContinue();

                        sleep(1000, 1100);
                        clickContinue();

                        sleep(1000, 1100);
                        clickContinue();

                        sleep(1000, 1100);
                        clickContinue();

                        sleep(1000, 1100);
                        clickContinue();

                        sleep(1000, 1100);
                        clickContinue();

                        sleep(1000, 1100);
                        clickContinue();
                        currentAction = Action.PRINCEALIWALKTOBOB;
                        questAction = "Walking to Bob";
                        break;
                    case PRINCEALIWALKTOBOB:
                        if (!walkPath(princealiwalktobob)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENBOBDOOR;
                            questAction = "Opening Bob's door";
                        }
                        break;
                    case PRINCEALIOPENBOBDOOR:
                        RSObject door = objects.getNearest(45476);
                        if (door != null && calc.distanceTo(door) < 3) {
                            camera.turnTo(new RSTile(3235, 3203, 0));
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(45476);
                        }
                        walking.walkTileMM(new RSTile(3230, 3203, 0));
                        sleep(3000, 3300);
                        currentAction = Action.PRINCEALIBOBGETPICK;
                        questAction = "Getting bronze pickaxe";
                        break;
                    case PRINCEALIBOBGETPICK:
                        RSNPC bob = npcs.getNearest(cookassbobid);
                        if (bob != null && bob.isOnScreen()) {
                            bob.interact("Trade");
                            sleep(1000, 1100);
                            while (!interfaces.get(620).isValid()) {
                                bob.interact("Trade");
                                sleep(1000, 1100);
                            }
                            if (interfaces.get(620).isValid()) {
                                interfaces.getComponent(620, 26).getComponent(0).interact("Take 1");
                                sleep(500, 600);
                            }
                            while (inventory.getCount(bronzepickid) == 0) {
                                interfaces.getComponent(620, 26).getComponent(0).interact("Take 1");
                                sleep(1500, 1600);
                            }
                            currentAction = Action.PRINCEALIOPENBOBDOOR2;
                            questAction = "Opening Bob's door";
                            nextTileIndex = -1;
                        }
                        break;
                    case PRINCEALIOPENBOBDOOR2:
                        door = objects.getNearest(45476);
                        if (door != null && calc.distanceTo(door) < 3) {
                            camera.turnTo(new RSTile(3235, 3203, 0));
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(45476);
                        }
                        walking.walkTileMM(new RSTile(3232, 3203, 0));
                        sleep(3000, 3300);
                        currentAction = Action.PRINCEALIWALKTOMINE;
                        questAction = "Walking to mine";
                        break;
                    case PRINCEALIWALKTOMINE:
                        if (!walkPath(princealipathtomine)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIMINEORES;
                            questAction = "Mining copper and tin";
                        }
                        break;
                    case PRINCEALIMINEORES:
                        while (inventory.getCount(princealicopperoreid) == 0) {
                            if (getMyPlayer().getAnimation() == -1) {
                                mineRocks(princealicopperrocks);
                            }
                        }
                        while (inventory.getCount(princealitinoreid) == 0) {
                            if (getMyPlayer().getAnimation() == -1) {
                                mineRocks(princealitinrocks);
                            }
                        }
                        currentAction = Action.PRINCEALIWALKTOFURNACE;
                        questAction = "Walking to furnace";
                        break;
                    case PRINCEALIWALKTOFURNACE:
                        if (!walkPath(princealipathtofurnace)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIMAKEBRONZEBAR;
                            questAction = "Smelting bronze bar";
                        }
                        break;
                    case PRINCEALIMAKEBRONZEBAR:
                        RSItem copperore = inventory.getItem(princealicopperoreid);
                        if (copperore == null) {
                            currentAction = Action.PRINCEALIWALKTOBOB_Furnace;
                            questAction = "Walking to Bob";
                            break;
                        }
                        copperore.doClick(true);
                        sleep(1000, 1100);
                        RSObject furnace = objects.getNearest(princealifurnaceid);
                        furnace.interact("Use copper ore -> Furnace");
                        while (inventory.getCount(princealibronzebarid) == 0) {
                            sleep(1000, 1100);
                            if (getMyPlayer().getAnimation() == -1) {
                                copperore = inventory.getItem(princealicopperoreid);
                                if (copperore == null) {
                                    currentAction = Action.PRINCEALIWALKTOBOB_Furnace;
                                    questAction = "Walking to Bob";
                                    break;
                                }
                                copperore.doClick(true);
                                sleep(1000, 1100);
                                furnace = objects.getNearest(princealifurnaceid);
                                furnace.interact("Use copper ore -> Furnace");
                            }
                        }
                        currentAction = Action.PRINCEALIWALKTOBOB_Furnace;
                        questAction = "Walking to Bob";
                        break;
                    case PRINCEALIWALKTOBOB_Furnace:
                        if (!walkPath(princealiwalktobob)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENBOBDOOR3;
                            questAction = "Opening Bob's door";
                        }
                        break;
                    case PRINCEALIOPENBOBDOOR3:
                        door = objects.getNearest(45476);
                        if (door != null && calc.distanceTo(door) < 3) {
                            camera.turnTo(new RSTile(3235, 3203, 0));
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(45476);
                        }
                        walking.walkTileMM(new RSTile(3230, 3203, 0));
                        sleep(3000, 3300);
                        currentAction = Action.PRINCEALIBOBGETHATCHET;
                        questAction = "Getting bronze hatchet";
                        break;
                    case PRINCEALIBOBGETHATCHET:
                        bob = npcs.getNearest(cookassbobid);
                        if (bob != null && bob.isOnScreen() && inventory.getCount(coinsid) < 6) {
                            bob.interact("Trade");
                            sleep(1000, 1100);
                            while (!interfaces.get(620).isValid()) {
                                bob.interact("Trade");
                                sleep(1000, 1100);
                            }
                            sleep(1000, 1100);
                            if (interfaces.get(620).isValid()) {
                                interfaces.getComponent(620, 26).getComponent(4).interact("Take 1");
                                sleep(1000, 1100);
                            }

                            while (inventory.getCount(bronzehatchetid) == 0) {
                                interfaces.getComponent(620, 25).getComponent(6).interact("Buy 1");
                                sleep(1000, 1100);
                            }
                            currentAction = Action.PRINCEALIOPENBOBDOOR4;
                            questAction = "Opening Bob's door";
                            nextTileIndex = -1;
                        }
                        break;
                    case PRINCEALIOPENBOBDOOR4:
                        door = objects.getNearest(45476);
                        if (door != null && calc.distanceTo(door) < 3) {
                            camera.turnTo(new RSTile(3235, 3203, 0));
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(45476);
                        }
                        walking.walkTileMM(new RSTile(3232, 3203, 0));
                        sleep(3000, 3300);
                        currentAction = Action.PRINCEALIWALKTOTREE;
                        questAction = "Walking to tree";
                        break;
                    case PRINCEALIWALKTOTREE:
                        if (!walkPath(princealipathtotree)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALICUTLOGS;
                            questAction = "Cutting logs";
                        }
                        break;
                    case PRINCEALICUTLOGS:
                        while (inventory.getCount(1511) == 0) {
                            if (getMyPlayer().getAnimation() == -1) {
                                cutTree(new int[]{treeid});
                            }
                        }
                        currentAction = Action.PRINCEALIWALKTOSTORE;
                        questAction = "Walking to store";
                    case PRINCEALIWALKTOSTORE:
                        if (!walkPath(princealipathtostore)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIGETSTOREITEMS;
                            questAction = "Buying things";
                        }
                        break;
                    case PRINCEALIGETSTOREITEMS:
                        shopkeeper = npcs.getNearest(cookassshopkeeper);
                        if (shopkeeper == null || !shopkeeper.isOnScreen()) {
                            return random(400, 500);
                        }
                        shopkeeper.interact("Trade");
                        sleep(300, 400);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        while (!interfaces.get(620).isValid()) {
                            sleep(1300, 1400);
                            shopkeeper = npcs.getNearest(cookassshopkeeper);
                            if (shopkeeper == null || !shopkeeper.isOnScreen()) {
                                return random(400, 500);
                            }
                            shopkeeper.interact("Trade");
                        }
                        sleep(300, 500);
                        pickaxe = inventory.getItem("Bronze hatchet");
                        if (pickaxe != null) {
                            pickaxe.interact("Sell 1");
                        }
                        sleep(1500, 1600);
                        interactInterface(620, 25, 12, "Buy 1");
                        sleep(1500, 1600);
                        while (inventory.getCount(shearsid) == 0) {
                            interactInterface(620, 25, 12, "Buy 1");
                            sleep(1500, 1600);
                        }
                        interactInterface(620, 25, 0, "Buy 1");
                        sleep(1500, 1600);
                        while (inventory.getCount(potid) == 0) {
                            interactInterface(620, 25, 0, "Buy 1");
                            sleep(1500, 1600);
                        }
                        interactInterface(620, 25, 36, "Buy 1");
                        sleep(1500, 1600);
                        while (inventory.getCount(tinderboxid) == 0) {
                            interactInterface(620, 25, 36, "Buy 1");
                            sleep(1500, 1600);
                        }
                        interactInterface(620, 25, 18, "Buy 1");
                        sleep(1500, 1600);
                        while (inventory.getCount(bucketid) == 0) {
                            interactInterface(620, 25, 18, "Buy 1");
                            sleep(1500, 1600);
                        }
                        sleep(500, 600);
                        questAction = "Burning logs";
                        currentAction = Action.PRINCEALIBURNLOGS;
                        break;
                    case PRINCEALIBURNLOGS:
                        clickInterface(620, 18);
                        sleep(1000, 1100);
                        RSItem tinderbox = inventory.getItem(tinderboxid);
                        RSItem logs = inventory.getItem(1511);
                        tinderbox.doClick(true);
                        sleep(1000, 1100);
                        logs.doClick(true);
                        sleep(1000, 1100);
                        while (getMyPlayer().getAnimation() != -1) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        while (objects.getNearest(2732) == null || !objects.getNearest(2732).isOnScreen()) {
                            tinderbox = inventory.getItem(tinderboxid);
                            logs = inventory.getItem(1511);
                            tinderbox.doClick(true);
                            sleep(1000, 1100);
                            logs.doClick(true);
                            sleep(1000, 1100);
                            while (getMyPlayer().getAnimation() != -1) {
                                sleep(300, 400);
                            }
                            sleep(1500, 1600);
                        }
                        questAction = "Waiting for ashes";
                        RSGroundItem ashes = groundItems.getNearest(592);
                        while (ashes == null || !ashes.isOnScreen()) {
                            ashes = groundItems.getNearest(592);
                            sleep(300, 400);
                        }
                        mouse.click(ashes.getPoint(), true);
                        while (inventory.getCount(592) == 0) {
                            sleep(300, 400);
                            ashes = groundItems.getNearest(592);
                            if (ashes != null) {
                                mouse.click(ashes.getPoint(), true);
                            }
                            sleep(300, 400);
                        }
                        currentAction = Action.PRINCEALIWALKTOSHEEP;
                        questAction = "Walking to sheep";
                    case PRINCEALIWALKTOSHEEP:
                        if (!walkPath(princealipathtosheep)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENSHEEPGATE;
                            questAction = "Opening Sheep Gate";
                        }
                        break;
                    case PRINCEALIOPENSHEEPGATE:
                        camera.turnTo(new RSTile(3212, 3261, 0));
                        camera.setPitch(false);
                        while (objects.getNearest(45206) != null && objects.getNearest(45206).isOnScreen()) {
                            objects.getNearest(45208).interact("Open");
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALISHEARSHEEP;
                        questAction = "Shearing sheep";
                        break;
                    case PRINCEALISHEARSHEEP:
                        walking.walkTileMM(new RSTile(3201, 3267, 0), 0, 0);
                        sleep(1400, 1500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        while (inventory.getCount(woolid) < 7) {
                            RSNPC sheep = npcs.getNearest(princealishearablesheep);
                            if (sheep != null && getMyPlayer().getAnimation() == -1) {
                                if (sheep.isOnScreen()) {
                                    sheep.interact("Shear");

                                } else {
                                    walking.walkTileMM(sheep.getLocation());
                                }
                                sleep(1400, 1500);
                                while (getMyPlayer().isMoving()) {
                                    sleep(300, 400);
                                }
                            }
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALIOPENSHEEPGATE2;
                        questAction = "Opening Sheep Gate";
                        break;
                    case PRINCEALIOPENSHEEPGATE2:
                        walking.walkTileMM(new RSTile(3201, 3267, 0), 0, 0);
                        sleep(1400, 1500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        walking.walkTileMM(new RSTile(3212, 3261, 0));
                        sleep(1400, 1500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        camera.turnTo(new RSTile(3211, 3261, 0));
                        camera.setPitch(false);
                        while (objects.getNearest(45206) != null && objects.getNearest(45206).isOnScreen()) {
                            objects.getNearest(45208).interact("Open");
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALIWALKTOSPINNINGWHEEL;
                        questAction = "Walking to spinning wheel";
                        break;
                    case PRINCEALIWALKTOSPINNINGWHEEL:
                        if (!walkPath(princealipathtospinningwheel)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALICLIMBSTAIRS;
                            questAction = "Climbing staircase";
                        }
                        break;
                    case PRINCEALICLIMBSTAIRS:
                        RSObject staircase = objects.getTopAt(new RSTile(3204, 3207, 0));
                        staircase.interact("Climb-up");
                        while (getMyPlayer().getLocation().getZ() != 1) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        questAction = "Walking to Spinning Wheel";
                        walking.walkTileMM(new RSTile(3210, 3215, 1));
                        sleep(1400, 1500);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        currentAction = Action.PRINCEALIUSESPINNING;
                        questAction = "Spinning Balls of Wool";
                        break;
                    case PRINCEALIUSESPINNING:
                        RSItem wool = inventory.getItem(woolid);
                        wool.doClick(true);
                        sleep(1500, 1600);
                        RSObject wheel = objects.getNearest(princealispinningwheelid);
                        wheel.interact("Use Wool -> Spinning wheel");
                        while (!interfaces.get(905).isValid()) {
                            sleep(300, 400);
                        }
                        clickInterface(905, 14);
                        while (inventory.getCount(ballofwoolid) != 7) {
                            sleep(300, 400);
                        }
                        currentAction = Action.PRINCEALITELEPORTLUMBRIDGE;
                        questAction = "Teleporting to Lumbridge";
                        break;
                    case PRINCEALITELEPORTLUMBRIDGE:
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.PRINCEALIWALKTONED;
                        questAction = "Walking to Ned";
                        return random(300, 400);
                    case PRINCEALIWALKTONED:
                        if (!walkPath(princealipathtoned)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENNEDDOOR;
                            questAction = "Opening Ned's Door";
                        }
                        break;
                    case PRINCEALIOPENNEDDOOR:
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        while (objects.getNearest(1239) != null && objects.getNearest(1239).isOnScreen()) {

                            camera.turnTo(new RSTile(3102, 3257, 0));
                            objects.getNearest(1239).interact("Open");
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALIGETROPENED;
                        questAction = "Getting rope from Ned";
                        break;
                    case PRINCEALIGETROPENED:
                        startTalk(nedid, false);
                        sleep(1000, 1100);
                        while (!clickDialogOption("Talk about something else.")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("Yes, I would like some rope.")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        if (interfaces.canContinue()) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        while (!clickDialogOption("I have some balls of wool. Could you make me some rope?")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        questAction = "Getting wig from Ned";
                        startTalk(nedid, false);
                        sleep(1000, 1100);
                        while (!clickDialogOption("Talk about something else.")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("Ned, could you make other things from wool?")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("How about some sort of a wig?")) {
                            sleep(300, 400);
                        }
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("I have that now. Please, make me a wig.")) {
                            sleep(300, 400);
                        }
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        currentAction = Action.PRINCEALIOPENNEDDOOR2;
                        questAction = "Opening Ned's door";
                        break;
                    case PRINCEALIOPENNEDDOOR2:
                        sleep(1500, 1600);
                        while (objects.getNearest(1239) != null && objects.getNearest(1239).isOnScreen()) {

                            camera.turnTo(new RSTile(3102, 3257, 0));
                            objects.getNearest(1239).interact("Open");
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALIWALKTOPUMP;
                        questAction = "Walking to Water Pump";
                        break;
                    case PRINCEALIWALKTOPUMP:
                        if (!walkPath(princealipathtopump)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIFILLBUCKET;
                            questAction = "Filling bucket at pump";
                        }
                        break;
                    case PRINCEALIFILLBUCKET:
                        RSItem bucket = inventory.getItem(bucketid);
                        bucket.doClick(true);
                        RSObject pump = objects.getNearest(3460);
                        while (pump == null) {
                            pump = objects.getNearest(3460);
                            sleep(300, 400);
                        }
                        pump.interact("Use Bucket -> Waterpump");
                        sleep(1000, 1100);
                        while (inventory.getCount(1929) == 0) {
                            sleep(1000, 1100);
                            bucket = inventory.getItem(bucketid);
                            if (bucket == null) {
                                break;
                            }
                            bucket.doClick(true);
                            pump = objects.getNearest(3460);
                            while (pump == null) {
                                pump = objects.getNearest(3460);
                                sleep(300, 400);
                            }
                            pump.interact("Use Bucket -> Waterpump");
                        }
                        currentAction = Action.PRINCEALIWALKTOONIONS;
                        questAction = "Walking to onions";
                        break;
                    case PRINCEALIWALKTOONIONS:
                        if (!walkPath(princealipathtoonions)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIPICKONIONS;
                            questAction = "Picking onions";
                        }
                        break;
                    case PRINCEALIPICKONIONS:
                        obj = objects.getNearest(princealioniongroundid);
                        while (obj == null) {
                            questAction = "Searching for onions";
                            obj = objects.getNearest(princealioniongroundid);
                        }
                        obj.interact("Pick");
                        sleep(1000, 1100);
                        while (inventory.getCount(princealionionid) < 2) {
                            sleep(2300, 2400);
                            obj = objects.getNearest(princealioniongroundid);
                            while (obj == null) {
                                questAction = "Searching for onions";
                                obj = objects.getNearest(princealioniongroundid);
                            }
                            obj.interact("Pick");
                        }
                        currentAction = Action.PRINCEALIWALKTOAGGIE;
                        questAction = "Walking to Aggie";
                        break;
                    case PRINCEALIWALKTOAGGIE:
                        if (!walkPath(princealipathtoaggie)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENAGGIEDOOR;
                            questAction = "Opening Aggie's door";
                        }
                        break;
                    case PRINCEALIOPENAGGIEDOOR:
                        sleep(1500, 1600);
                        while (objects.getNearest(1239) != null && objects.getNearest(1239).isOnScreen()) {

                            camera.turnTo(new RSTile(3102, 3257, 0));
                            objects.getNearest(1239).interact("Open");
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALIAGGIEGETMATERIALS;
                        questAction = "Getting things from Aggie";
                        break;
                    case PRINCEALIAGGIEGETMATERIALS:
                        walking.walkTileMM(new RSTile(3083, 3260, 0));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1500, 1600);
                        startTalk(aggieid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("dyes")) {
                            while (!clickDialogOption("More")) {
                                sleep(300, 400);
                            }
                            sleep(1000, 1100);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("yellow")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("make me some yellow dye")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (inventory.getCount(1765) == 0) {

                            clickContinue();
                            sleep(1000, 1100);
                        }
                        sleep(1000, 1100);
                        RSItem dye = inventory.getItem(1765);
                        RSItem wig = inventory.getItem(2421);
                        dye.doClick(true);
                        sleep(1000, 1100);
                        wig.doClick(true);
                        while (inventory.getCount(2419) == 0) {
                            sleep(300, 400);
                        }

                        currentAction = Action.PRINCEALIOPENAGGIEDOOR2;
                        questAction = "Opening Aggie's door";
                        break;
                    case PRINCEALIOPENAGGIEDOOR2:
                        sleep(1500, 1600);
                        while (objects.getNearest(1239) != null && objects.getNearest(1239).isOnScreen()) {

                            camera.turnTo(new RSTile(3102, 3257, 0));
                            objects.getNearest(1239).interact("Open");
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALIWALKTOCLAYMINE;
                        questAction = "Walking to clay mine";
                        break;
                    case PRINCEALIWALKTOCLAYMINE:
                        if (!walkPath(princealipathtoclay)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIMINECLAY;
                            questAction = "Mining clay";
                        }
                        break;
                    case PRINCEALIMINECLAY:
                        while (inventory.getCount(clayid) == 0) {
                            if (getMyPlayer().getAnimation() == -1) {
                                mineRocks(clayrocksid);
                            }
                        }
                        bucket = inventory.getItem(1929);
                        RSItem clay = inventory.getItem(clayid);
                        bucket.doClick(true);
                        sleep(1000, 1100);
                        clay.doClick(true);
                        while (inventory.getCount(1761) == 0) {
                            sleep(300, 400);
                        }
                        currentAction = Action.PRINCEALIWALKTOVARROCK;
                        questAction = "Walking to Varrock";
                        break;
                    case PRINCEALIWALKTOVARROCK:
                        if (!walkPath(princealipathtovarrock)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIFILLBUCKETFOUNTAIN;
                            questAction = "Filling bucket";
                        }
                        break;
                    case PRINCEALIFILLBUCKETFOUNTAIN:
                        bucket = inventory.getItem(bucketid);
                        bucket.doClick(true);
                        sleep(1000, 1100);
                        RSObject fountain = objects.getNearest(24265);
                        fountain.interact("Use Bucket -> Fountain");
                        sleep(2000, 2300);
                        while (inventory.getCount(1929) == 0) {
                            bucket = inventory.getItem(bucketid);
                            bucket.doClick(true);
                            sleep(1000, 1100);
                            fountain = objects.getNearest(24265);
                            fountain.interact("Use Bucket -> Fountain");
                            sleep(2000, 2300);
                        }
                        currentAction = Action.PRINCEALIWALKTOCLOTHESSHOP;
                        questAction = "Walking to clothes shop";
                        break;
                    case PRINCEALIWALKTOCLOTHESSHOP:
                        walking.walkTileMM(new RSTile(3206, 3417, 0));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        questAction = "Getting pink skirt";
                        RSNPC thessalia = npcs.getNearest(548);
                        while (thessalia == null) {
                            thessalia = npcs.getNearest(548);
                            sleep(300, 400);
                        }
                        thessalia.interact("Trade");
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        while (!interfaces.get(620).isValid()) {
                            sleep(300, 400);
                            thessalia = npcs.getNearest(548);
                            while (thessalia == null) {
                                thessalia = npcs.getNearest(548);
                                sleep(300, 400);
                            }
                            thessalia.interact("Trade");
                            sleep(1000, 1100);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                            sleep(1000, 1100);
                        }
                        interactInterface(620, 25, 33, "Buy 1");
                        sleep(1000, 1100);
                        while (inventory.getCount(1013) == 0) {
                            interactInterface(620, 25, 33, "Buy 1");
                            sleep(1000, 1100);
                        }
                        currentAction = Action.PRINCEALIWALKTOBLUEMOON;
                        questAction = "Walking to blue moon inn";
                        break;
                    case PRINCEALIWALKTOBLUEMOON:
                        if (!walkPath(princealipathtoinn)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIBUYBEER;
                            questAction = "Buying 3 beers";
                        }
                    case PRINCEALIBUYBEER:
                        for (int x = 0; x < 3 || inventory.getCount(1917) < 3; x++) {
                            startTalk(733, true);
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                            while (!clickDialogOption("A glass of your finest ale please.")) {
                                sleep(300, 400);
                            }
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        currentAction = Action.PRINCEALIWALKTOREDBERRIES;
                        questAction = "Walking to redberries";
                        break;
                    case PRINCEALIWALKTOREDBERRIES:
                        if (!walkPath(princealipathtoredberries)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIPICKREDBERRIES;
                            questAction = "Picking redberries";
                        }
                        break;
                    case PRINCEALIPICKREDBERRIES:
                        RSObject redberries = null;
                        while (inventory.getCount(1951) == 0) {
                            redberries = objects.getNearest(23628, 23629, 23630);
                            while (redberries == null) {
                                redberries = objects.getNearest(23628, 23629, 23630);
                                sleep(300, 400);
                            }
                            redberries.interact("Pick-from");
                            sleep(2000, 2300);
                        }
                        questAction = "Teleing to Lumbridge";
                        currentAction = Action.PRINCEALITELEPORTLUMBRIDGE2;
                        break;
                    case PRINCEALITELEPORTLUMBRIDGE2:
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.PRINCEALIWALKTOJAIL;
                        questAction = "Walking to Jail";
                        return random(300, 400);
                    case PRINCEALIWALKTOJAIL:
                        if (!walkPath(princealipathtojail)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENJAILDOOR;
                            questAction = "Opening jail door";
                        }
                        break;
                    case PRINCEALIOPENJAILDOOR:
                        while (objects.getNearest(3434) != null && objects.getNearest(3434).isOnScreen()) {
                            camera.turnTo(new RSTile(3126, 3264, 0));
                            objects.getNearest(3434).interact("Open");
                            sleep(2000, 2300);
                        }
                        questAction = "Getting key mould from Keli";
                        currentAction = Action.PRINCEALIGETKEYMOULD;
                        break;
                    case PRINCEALIGETKEYMOULD:
                        walking.walkTileMM(new RSTile(3125, 3245, 0));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        startTalk(919, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("Heard of you? You are famous in RuneScape!")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        log("plan");
                        while (!clickDialogOption("plan")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("Can")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("Could")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("touch")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        questAction = "Opening jail door";
                        currentAction = Action.PRINCEALIOPENJAILDOOR2;
                        break;
                    case PRINCEALIOPENJAILDOOR2:
                        while (objects.getNearest(3434) != null && objects.getNearest(3434).isOnScreen()) {
                            camera.turnTo(new RSTile(3126, 3264, 0));
                            objects.getNearest(3434).interact("Open");
                            sleep(2000, 2300);
                        }
                        questAction = "Walking to Aggie";
                        currentAction = Action.PRINCEALIWALKTOAGGIE2;
                    case PRINCEALIWALKTOAGGIE2:
                        if (!walkPath(princealipathtoaggie2)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENAGGIEDOOR3;
                            questAction = "Opening Aggie's door";
                        }
                        break;
                    case PRINCEALIOPENAGGIEDOOR3:
                        sleep(1500, 1600);
                        while (objects.getNearest(1239) != null && objects.getNearest(1239).isOnScreen()) {

                            camera.turnTo(new RSTile(3102, 3257, 0));
                            objects.getNearest(1239).interact("Open");
                            sleep(1500, 1600);
                        }
                        currentAction = Action.PRINCEALIAGGIEGETMATERIALS2;
                        questAction = "Getting skin paste";
                        break;
                    case PRINCEALIAGGIEGETMATERIALS2:
                        startTalk(aggieid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("Talk about Prince Ali Rescue.")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("Yes")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        currentAction = Action.PRINCEALITELEPORTLUMBRIDGE3;
                        questAction = "Teleing lumbridge";
                        break;
                    case PRINCEALITELEPORTLUMBRIDGE3:
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.PRINCEALIWALKTOOSMAN2;
                        questAction = "Walking to Osman";
                        return random(300, 400);
                    case PRINCEALIWALKTOOSMAN2:
                        if (!walkPath(princealipathtoosman)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIKEYMOULDOSMAN;
                            questAction = "Giving key mould to Osman";
                        }
                        break;
                    case PRINCEALIKEYMOULDOSMAN:
                        startTalk(princealiosmanid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        currentAction = Action.PRINCEALITELEPORTLUMBRIDGE4;
                        questAction = "Teleing to Lumbridge";
                        break;
                    case PRINCEALITELEPORTLUMBRIDGE4:
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.PRINCEALIWALKTOLEELA;
                        questAction = "Walking to Leela";
                        return random(300, 400);
                    case PRINCEALIWALKTOLEELA:
                        if (!walkPath(princealipathtoleela)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIGETBRONZEKEY;
                            questAction = "Retrieving key";
                        }
                        break;
                    case PRINCEALIGETBRONZEKEY:
                        startTalk(princealileelaid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        currentAction = Action.PRINCEALIGETADVICELEELA;
                        questAction = "Getting \"advice\" from Leela";
                        break;
                    case PRINCEALIGETADVICELEELA:
                        while (!clickDialogOption("drunk")) {
                            startTalk(princealileelaid, true);
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        currentAction = Action.PRINCEALIWALKTOJAIL2;
                        questAction = "Walking to Jail";
                        break;
                    case PRINCEALIWALKTOJAIL2:
                        if (!walkPath(princealipathtojail2)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIOPENJAILDOOR3;
                            questAction = "Opening Jail door";
                        }
                        break;
                    case PRINCEALIOPENJAILDOOR3:
                        while (objects.getNearest(3434) != null && objects.getNearest(3434).isOnScreen()) {
                            camera.turnTo(new RSTile(3126, 3264, 0));
                            objects.getNearest(3434).interact("Open");
                            sleep(2000, 2300);
                        }
                        walking.walkTileMM(new RSTile(3126, 3245, 0));
                        sleep(2000, 2300);
                        currentAction = Action.PRINCEALIGETGUARDDRUNK;
                        questAction = "Getting guard drunk";
                        break;
                    case PRINCEALIGETGUARDDRUNK:
                        int oldcount = inventory.getCount(1917);
                        RSItem beer = inventory.getItem(1917);
                        beer.interact("Use");
                        sleep(1000, 1100);
                        RSNPC guard = npcs.getNearest(916);
                        while (guard == null || !guard.isOnScreen()) {
                            guard = npcs.getNearest(916);
                            sleep(300, 400);
                        }
                        guard.interact("Use");
                        sleep(400, 500);
                        while (!interfaces.canContinue()) {
                            guard.interact("Use");
                            sleep(300, 400);
                        }
                        while (interfaces.canContinue()) {
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        currentAction = Action.PRINCEALIBINDKELI;
                        questAction = "Binding Keli";
                        break;
                    case PRINCEALIBINDKELI:
                        walking.walkTileMM(new RSTile(3126, 3245, 0));
                        sleep(2000, 2300);
                        RSItem rope = inventory.getItem(954);
                        rope.doClick(true);
                        RSNPC keli = npcs.getNearest(919);
                        while (keli == null || !keli.isOnScreen()) {
                            keli = npcs.getNearest(919);
                            sleep(300, 400);
                        }
                        keli.interact("Use");
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (interfaces.canContinue()) {
                            clickContinue();
                            sleep(1000, 1100);
                        }

                        currentAction = Action.PRINCEALIRESCUEPRINCE;
                        questAction = "Rescuing Prince";
                        break;
                    case PRINCEALIRESCUEPRINCE:
                        walking.walkTileMM(new RSTile(3123, 3244, 0));
                        sleep(3000, 3300);
                        camera.turnTo(new RSTile(3123, 3243, 0));
                        RSObject jaildoor = objects.getNearest(3436);
                        while (jaildoor == null) {
                            jaildoor = objects.getNearest(3436);
                            sleep(300, 400);
                        }
                        while (!inArea(new RSTile(3125, 3243, 0), new RSTile(3121, 3240, 0))) {
                            jaildoor.interact("Open");
                            sleep(6000, 7000);
                        }
                        startTalk(920, true);
                        while (interfaces.canContinue()) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        currentAction = Action.PRINCEALITELEPORTLUMBRIDGE5;
                        questAction = "Teleing to Lumbridge";
                        break;
                    case PRINCEALITELEPORTLUMBRIDGE5:
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.PRINCEALIENDWALK;
                        questAction = "Walking to Hassan";
                        return random(300, 400);
                    case PRINCEALIENDWALK:
                        if (!walkPath(princealipathtohassan)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.PRINCEALIENDTALK;
                            questAction = "Ending quest w00t";
                        }
                        break;
                    case PRINCEALIENDTALK:
                        startTalk(princealihassanid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        currentQuestIndex++;
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case RESTLESSGHOSTSTART:
                        questName = "The Restless Ghost";
                        inventory.dropAllExcept(questitems);
                        sleep(1000, 1100);
                        if (inArea(lumbridge1, lumbridge2) || telied) {
                            RSTile temptile = new RSTile(3242, 3205);
                            while (calc.distanceTo(temptile) > 3) {
                                RSWeb walkToFather1 = web.getWeb(getMyPlayer().getLocation(), temptile);
                                walkToFather1.step();
                            }
                        } else {
                            if (getMyPlayer().getAnimation() != -1) {
                                return random(500, 1000);
                            }
                            game.openTab(Tab.MAGIC);
                            try {
                                questAction = "Teleing to Lumbridge";
                                teleportLumbridge();
                                telied = true;
                                return random(300, 400);

                            } catch (Exception e) {
                            }
                        }
                        if (inventory.containsOneOf(553)) {
                            TalkedToFather1 = true;
                            WalkedToFather2 = true;
                            TalkedToFather2 = true;
                            WalkedToGhost = true;
                            TalkedToGhost = true;
                            WalkedToMine = true;
                            HeadGained = true;
                            WalkedToGhost2 = false;
                            log("We've already got a head of a skeleton! Let's go the the ghost's place and finish the quest!");
                        }
                        if (inventory.containsOneOf(amulet) || equipment.containsOneOf(amulet)) {
                            TalkedToFather1 = true;
                            WalkedToFather2 = true;
                            TalkedToFather2 = true;
                            if (!atGhost()) {
                                WalkedToGhost = false;
                            } else {
                                WalkedToGhost = true;
                            }
                            log("We already have an amulet, awesome!");
                        }
                        currentAction = Action.RESTLESSGHOSTSOLVE;
                        break;
                    case RESTLESSGHOSTSOLVE:
                        if (TalkedToFather1 == true) {
                            if (WalkedToFather2 == true) {
                                if (TalkedToFather2 == true) {
                                    if (WalkedToGhost == true) {
                                        if (openedChest == true) {
                                            if (TalkedToGhost == true) {

                                                if (WalkedToMine == true) {
                                                    if (headGained == true) {
                                                        if (WalkedToGhost2 == true) {
                                                            questAction = "Finishing quest w00t";
                                                            sleep(5000, 5500);
                                                            RSObject chestClosed = objects.getNearest(chest);
                                                            RSObject chestOpen = objects.getNearest(openChest);

                                                            if (chestClosed != null) {
                                                                if (chestClosed.isOnScreen()) {
                                                                    chestClosed.interact("Open");
                                                                } else {
                                                                    camera.turnTo(chestClosed);
                                                                }
                                                            }

                                                            chestOpen = objects.getNearest(openChest);
                                                            while ((chestOpen = objects.getNearest(openChest)) == null) {
                                                            }
                                                            if (chestOpen != null) {
                                                                while (inventory.getCount(553) > 0) {
                                                                    inventory.selectItem(553);
                                                                    chestOpen.interact("Use");
                                                                    sleep(2000, 2200);
                                                                }
                                                            }

                                                            while (getMyPlayer().isMoving()) {
                                                                sleep(300, 400);
                                                            }
                                                            sleep(5000, 5500);

                                                            log("We've finished the quest");
                                                            while (!atGhost.contains(getMyPlayer().getLocation())) {
                                                                sleep(300, 400);
                                                            }
                                                            sleep(3000, 4000);
                                                            while (inventory.getCount(15410) > 0) {
                                                                inventory.getItem(15410).interact("Bury");
                                                                sleep(3500, 4000);
                                                            }

                                                            while (inventory.getCount(15410) > 0) {
                                                                inventory.getItem(15410).interact("Bury");
                                                                sleep(3500, 4000);
                                                            }
                                                            currentQuestIndex++;
                                                            currentAction = actions.get(nextActionIndex++);
                                                            break;
                                                        } else {

                                                            if (!atGhost()) {
                                                                walkToGhost2();
                                                            }
                                                            if (atGhost()) {
                                                                WalkedToGhost2 = true;
                                                            }


                                                        }
                                                    } else {
                                                        getHead();

                                                    }

                                                } else {
                                                    walkToMine();
                                                }

                                            } else {
                                                talkToGhost();
                                            }
                                        } else {
                                            RSObject Chest = objects.getNearest(chest);
                                            while (Chest != null) {
                                                Chest.interact("Open");
                                                openedChest = true;
                                            }
                                        }
                                    } else {
                                        walkToGhost();
                                    }
                                } else {
                                    talkToFather2();
                                }
                            } else {
                                walkToFather2();
                            }
                        } else {
                            talkToFather1();

                        }
                        if (!walking.isRunEnabled() && walking.getEnergy() > 30) {
                            walking.setRun(true);
                        }
                        return random(300, 400);
                    case RUNEMYSTSTARTWALK:
                        questName = "Rune mysteries";
                        questAction = "Walking to Duke";
                        inventory.dropAllExcept(questitems);
                        sleep(1000, 1100);
                        if (inArea(lumbridge1, lumbridge2) || telied) {
                            questAction = "Walking to the Duke";
                            if (getMyPlayer().getAnimation() != -1) {
                                return random(500, 1000);
                            }
                            telied = true;
                            if (!walkPath(runemyststartpath)) {
                                return random(400, 500);
                            } else {
                                currentAction = Action.RUNEMYSTCLIMBSTAIRS_1;
                                questAction = "Talking to Cook";
                                telied = false;
                            }
                        } else {
                            if (getMyPlayer().getAnimation() != -1) {
                                return random(500, 1000);
                            }
                            game.openTab(Tab.MAGIC);
                            try {
                                questAction = "Teleing to Lumbridge";
                                log("Teleporting to Lumbridge");
                                teleportLumbridge();
                                telied = true;
                                return random(300, 400);

                            } catch (Exception e) {
                            }
                        }
                        break;
                    case RUNEMYSTCLIMBSTAIRS_1:
                        questAction = "Climbing stairs";
                        sleep(1000, 1100);
                        RSObject stairs;
                        while ((stairs = objects.getNearest(36776)) == null);
                        while (getMyPlayer().getLocation().getZ() != 1) {
                            stairs.interact("Climb-up");
                            stairs = objects.getNearest(36776);
                            sleep(1000, 1100);
                        }
                        sleep(1000, 1100);
                        walking.walkTileMM(new RSTile(3207, 3222, 1));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        currentAction = Action.RUNEMYSTOPENDUKEDOOR;
                        questAction = "Opening Duke's door";
                        break;
                    case RUNEMYSTOPENDUKEDOOR:
                        questAction = "Opening Duke's door";
                        while ((door = objects.getNearest(36844)) != null) {
                            door.interact("Open");
                            sleep(1000, 1100);
                        }
                        sleep(1000, 1100);
                        walking.walkTileMM(new RSTile(3210, 3222, 1));
                        sleep(1000, 1100);
                        currentAction = Action.RUNEMYSTSTARTTALK;
                        questAction = "Talking to Duke";
                        break;
                    case RUNEMYSTSTARTTALK:
                        questAction = "Talking to Duke";
                        startTalk(runemystdukeid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("quests")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        if (getComponentContaining("delivery") != null) {
                            currentAction = actions.get(nextActionIndex++); //FASTER
                            questAction = "Teleing to Lumbridge (faster than walk)";
                            break;
                        }
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(2000, 2100);
                        while (!interfaces.get(178).isValid()) {
                            sleep(400, 500);
                            if (interfaces.canContinue()) {
                                interfaces.clickContinue();
                            }
                            sleep(1000, 1100);
                        }
                        if (interfaces.get(178).isValid()) {

                            interfaces.getComponent(178, 78).doClick(); // Start quest interface - button

                        }
                        sleep(1000, 1100);
                        currentAction = actions.get(nextActionIndex++); //FASTER
                        questAction = "Teleing to Lumbridge (faster than walk)";
                        break;
                    case RUNEMYSTTELEPORTLUMBRIDGE:
                        questAction = "Teleporting to Lumbridge";
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.RUNEMYSTWALKTOWIZARDTOWER_1;
                        return random(300, 400);
                    case RUNEMYSTWALKTOWIZARDTOWER_1:
                        questAction = "Walking to Wizard's Tower";
                        if (!walkPath(runemystpathtotower_1)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.RUNEMYSTWALKTOWIZARDTOWER_2;
                        }
                        break;
                    case RUNEMYSTWALKTOWIZARDTOWER_2:
                        questAction = "Walking to Wizard's Tower";
                        if (calc.distanceTo(wizardTowerTile) > 3) {
                            RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), wizardTowerTile);
                            walkToTile.step();
                        } else {
                            currentAction = Action.RUNEMYSTOPENTOWERDOOR;
                            questAction = "Opening Tower door";
                        }
                        break;
                    case RUNEMYSTOPENTOWERDOOR:
                        questAction = "Opening door";
                        camera.turnTo(new RSTile(3109, 3173, 0));
                        door = objects.getNearest(11993);
                        while (door != null && door.isOnScreen()) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(11993);
                        }
                        walking.walkTileMM(new RSTile(3107, 3163, 0));
                        sleep(2000, 2200);
                        currentAction = Action.RUNEMYSTOPENTOWERDOOR2;
                        questAction = "Opening Second Tower Door";
                        break;
                    case RUNEMYSTOPENTOWERDOOR2:
                        questAction = "Opening door";
                        camera.turnTo(new RSTile(3104, 3161, 0));
                        door = objects.getNearest(11993);
                        while (door != null && door.isOnScreen()) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(11993);
                        }
                        walking.walkTileMM(new RSTile(3104, 3163, 0));
                        sleep(2000, 2200);
                        currentAction = Action.RUNEMYSTDESCENDLADDER;
                        questAction = "Descending ladder";
                        break;
                    case RUNEMYSTDESCENDLADDER:
                        questAction = "Descending ladder";
                        ladder = objects.getNearest(runemystladderid);
                        while (ladder == null) {
                            ladder = objects.getNearest(runemystladderid);
                            sleep(300, 400);
                        }
                        ladder.interact("Climb-down");
                        sleep(1300, 1400);
                        while (calc.distanceTo(new RSTile(3104, 9576, 0)) > 10) {

                            ladder = objects.getNearest(runemystladderid);
                            if (ladder != null) {
                                ladder.interact("Climb-down");
                            }
                            sleep(1300, 1400);
                        }
                        sleep(1000, 1100);
                        walking.walkTileMM(new RSTile(3108, 9570, 0));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }

                        camera.turnTo(new RSTile(3096, 9570, 0));
                        door = objects.getNearest(33060);
                        while (door != null && calc.distanceTo(door) < 5) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(33060);
                        }
                        walking.walkTileMM(new RSTile(3103, 9571, 0));
                        sleep(1000, 1100);
                        currentAction = Action.RUNEMYSTTALKTOSEDRIDOR;
                        questAction = "Talking to Sedridor";
                        break;
                    case RUNEMYSTTALKTOSEDRIDOR:
                        questAction = "Talking to Sedridor";
                        startTalk(runemystsedridorid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        if (getComponentContaining("goes") == null) {
                            while (!clickDialogOption("head")) {
                                sleep(300, 400);
                            }
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                            for (int x = 0; x < 13; x++) {
                                clickContinue();
                                sleep(1000, 1100);
                            }
                        } else {
                            if (inventory.contains(290)) {
                                currentAction = actions.get(nextActionIndex++);
                                questAction = "Teleing to Lumbridge";
                                break;
                            } else {
                                for (int x = 0; x < 7; x++) {
                                    clickContinue();
                                    sleep(1000, 1100);
                                    currentAction = actions.get(nextActionIndex++);
                                    questAction = "Teleing to Lumbridge";
                                }
                            }
                        }
                        while (!clickDialogOption("certainly")) {
                            if (interfaces.canContinue()) {
                                clickContinue();
                            }
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        for (int x = 0; x < 4; x++) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        currentAction = actions.get(nextActionIndex++);
                        questAction = "Teleing to Lumbridge";
                        break;
                    case RUNEMYSTTELEPORTLUMBRIDGE2:
                        questAction = "Teleporting to Lumbridge";
                        teleportLumbridge();
                        telied = true;
                        currentAction = Action.RUNEMYSTWALKTOAUBURY;
                        questAction = "Walking to Aubury";
                        break;
                    case RUNEMYSTWALKTOAUBURY:
                        questAction = "Walking to Aubury";
                        if (!walkPath(runemystpathtoaubury)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.RUNEMYSTTALKTOAUBURY;
                            questAction = "Talking to Aubury";
                        }
                        break;
                    case RUNEMYSTTALKTOAUBURY:
                        questAction = "Talking to Aubury";
                        startTalk(runemystauburyid, true);
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        if (getComponentContaining("lost") != null) {
                            for (int x = 0; x < 3; x++) {
                                clickContinue();
                                sleep(1000, 1100);
                            }
                            clickContinue();
                            teleportLumbridge();
                            currentAction = actions.get(nextActionIndex++);
                            questAction = "Walking to Wizard Tower";
                            break;
                        } else {
                            while (!clickDialogOption("package")) {
                                sleep(300, 400);
                            }
                            sleep(1000, 1100);
                            for (int x = 0; x < 8; x++) {
                                clickContinue();
                                sleep(1000, 1100);
                            }
                            if (interfaces.canContinue()) {
                                clickContinue();
                                sleep(1000, 100);
                            }
                            while (!clickDialogOption("Yes")) {
                                sleep(300, 400);
                            }
                            sleep(1000, 1100);
                            clickContinue();
                            while (calc.distanceTo(new RSTile(3114, 3186, 0)) > 20) {
                                sleep(300, 400);
                            }
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        currentAction = actions.get(nextActionIndex++);
                        questAction = "Walking to Wizard's Tower";
                        break;
                    case RUNEMYSTWALKTOWIZARDTOWER_3:
                        questAction = "Walking to Wizard's Tower";
                        if (calc.distanceTo(wizardTowerTile) > 3) {
                            RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), wizardTowerTile);
                            walkToTile.step();
                        } else {
                            currentAction = Action.RUNEMYSTOPENTOWERDOOR3;
                            questAction = "Opening Tower door";
                        }
                        break;
                    case RUNEMYSTOPENTOWERDOOR3:
                        questAction = "Opening door";
                        camera.turnTo(new RSTile(3109, 3173, 0));
                        door = objects.getNearest(11993);
                        while (door != null && door.isOnScreen()) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(11993);
                        }
                        walking.walkTileMM(new RSTile(3107, 3163, 0));
                        sleep(2000, 2200);
                        currentAction = Action.RUNEMYSTOPENTOWERDOOR4;
                        questAction = "Opening Second Tower Door";
                        break;
                    case RUNEMYSTOPENTOWERDOOR4:
                        questAction = "Opening door";
                        camera.turnTo(new RSTile(3104, 3161, 0));
                        door = objects.getNearest(11993);
                        while (door != null && door.isOnScreen()) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(11993);
                        }
                        walking.walkTileMM(new RSTile(3104, 3163, 0));
                        sleep(2000, 2200);
                        currentAction = Action.RUNEMYSTDESCENDLADDER2;
                        questAction = "Descending ladder";
                        break;
                    case RUNEMYSTDESCENDLADDER2:
                        questAction = "Descending ladder";
                        ladder = objects.getNearest(runemystladderid);
                        while (ladder == null) {
                            ladder = objects.getNearest(runemystladderid);
                            sleep(300, 400);
                        }
                        ladder.interact("Climb-down");
                        while (calc.distanceTo(new RSTile(3104, 9576, 0)) > 10) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        walking.walkTileMM(new RSTile(3108, 9570, 0));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }

                        camera.turnTo(new RSTile(3096, 9570, 0));
                        door = objects.getNearest(33060);
                        while (door != null && calc.distanceTo(door) < 5) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(33060);
                        }
                        walking.walkTileMM(new RSTile(3103, 9571, 0));
                        sleep(1000, 1100);
                        currentAction = Action.RUNEMYSTENDTALK;
                        questAction = "Talking to Sedridor";
                        break;
                    case RUNEMYSTENDTALK:
                        questAction = "Talking to Sedridor";
                        startTalk(runemystsedridorid, true);
                        sleep(1000, 1100);
                        for (int x = 0; x < 28; x++) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        currentQuestIndex++;
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTSTARTWALK:
                        questName = "The Blood Pact";
                        questAction = "Walking to Xenia";
                        RSTile xenialoc = new RSTile(3244, 3197);
                        while (calc.distanceTo(xenialoc) > 3) {
                            rswebStep(xenialoc);
                        }
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTSTARTTALK:
                        questAction = "Talking to Xenia";
                        RSNPC xenia = npcs.getNearest(9633);
                        for (int x = 0; x < 3; x++) {
                            if (xenia != null && xenia.isOnScreen()) {
                                xenia.interact("Talk");
                                sleep(2000, 3000);
                                if (interfaces.canContinue()) {
                                    clickContinue();
                                    sleep(1000, 1100);
                                    while (!clickDialogOption("with")) {
                                        sleep(300, 400);
                                    }
                                    sleep(1000, 1100);
                                    for (int c = 0; c < 3; c++) {
                                        clickContinue();
                                        sleep(1000, 1100);
                                    }
                                    while (!clickDialogOption("I'll")) {
                                        sleep(300, 400);
                                    }
                                    sleep(1000, 1100);
                                    while (!interfaces.get(178).isValid()) {
                                        sleep(300, 400);
                                        log("Waiting");
                                    }
                                    log("click!");
                                    clickInterface(178, 73);
                                    sleep(1000, 1100);
                                    clickContinue();
                                    sleep(1000, 1100);
                                    clickContinue();
                                    sleep(1000, 1100);
                                    if (interfaces.canContinue()) {
                                        clickContinue();
                                        sleep(1000, 1100);
                                    }
                                    break;
                                } else {
                                    continue;
                                }
                            }

                        }
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTDESCENDTRAP:
                        questAction = "Descending the Catacombs";
                        RSObject trap = objects.getNearest(bloodpacttrap);
                        while (trap == null || !trap.isOnScreen()) {
                            trap = objects.getNearest(bloodpacttrap);
                            sleep(300, 400);
                        }
                        trap.interact("Climb");
                        sleep(7000, 7700);
                        bloodpactinitialx = getMyPlayer().getLocation().getX();
                        bloodpactinitialy = getMyPlayer().getLocation().getY();
                        log("initial set:" + bloodpactinitialx + "," + bloodpactinitialy);
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTWATCHCUTSCENE:
                        questAction = "Watching the cutscene";
                        while (!interfaces.canContinue()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        for (int x = 0; x < 10; x++) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        sleep(5000);
                        bloodpactinitialx -= 8;
                        bloodpactinitialy -= 12;
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTKILLFIRSTCULTIST:
                        questAction = "Killing Kyle";
                        xenia = npcs.getNearest(9634);
                        if (xenia != null && xenia.isOnScreen()) {
                            walking.walkTileMM(new RSTile(getMyPlayer().getLocation().getX(), getMyPlayer().getLocation().getY() + 10));
                            sleep(2000);
                            while (!interfaces.canContinue()) {
                                sleep(300, 400);
                            }
                            log("Continuing...");
                            xenia = npcs.getNearest(9636);
                            while (xenia == null) {
                                xenia = npcs.getNearest(9636);
                                wait(300, 400);
                            }
                        }
                        walking.walkTileMM(new RSTile(getMyPlayer().getLocation().getX(), getMyPlayer().getLocation().getY() + 10));
                        sleep(4000);
                        RSNPC kyle = npcs.getNearest(9628);
                        if (kyle != null) {
                            combat.setAutoRetaliate(true);
                            if (calc.distanceTo(kyle.getLocation()) > 3) {
                                rswebStep(kyle.getLocation());
                            }

                            while (kyle != null) {

                                sleep(3000);

                                kyle.interact("Attack");
                                if (kyle.isDead()) {
                                    kyle = npcs.getNearest(9629);
                                }
                                if (kyle != null) {
                                    if (kyle.isOnScreen()) {
                                        while (!interfaces.canContinue()) {
                                            kyle.interact("Talk");
                                            sleep(1000);
                                        }
                                        mouse.click(296, 456, true);
                                        sleep(1200, 1700);
                                        interfaces.getComponent(230, 3).doClick();
                                        sleep(1000, 2000);
                                        while (interfaces.canContinue()) {
                                            interfaces.clickContinue();
                                        }
                                        kyle = npcs.getNearest(1);
                                    } else {
                                        camera.turnTo(kyle);
                                    }

                                }
                            }
                            sleep(5000);
                            RSGroundItem sling = groundItems.getNearest(15597);
                            sling.interact("Take");
                            sleep(1000, 1100);
                            while (inventory.getCount(15597) == 0) {
                                sling.interact("Take");
                                sleep(1000, 1100);
                            }
                            RSItem inventorysling = inventory.getItem(15597);
                            while (inventory.getCount(15597) > 0) {
                                inventorysling.interact("Wield");
                                sleep(1000, 1100);
                            }


                        }
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTWALKTOSECOND:
                        questAction = "Walking to second cultist";
                        if (npcs.getNearest(9636) != null && npcs.getNearest(9636).isOnScreen()) {
                            startTalk(9636, true);
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        log("Walking to tile: " + (bloodpactinitialx - 8) + "," + (bloodpactinitialy + 8));
                        walking.walkTileMM(new RSTile(bloodpactinitialx - 8, bloodpactinitialy + 8));
                        sleep(1000, 1100);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTKILLSECONDCULTIST:
                        questAction = "Killing Caitlin";
                        RSNPC Second = npcs.getNearest(9626);
                        walking.walkTileMM(Second.getLocation());
                        while (Second != null) {
                            while (Second.getHPPercent() > 10) {
                                Second.interact("Attack");

                            }
                            if (Second.getHPPercent() <= 10) {
                                Second = npcs.getNearest(1);
                            }
                        }
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTPULLBRIDGE:
                        questAction = "Lowering the gate";
                        RSObject wheels = objects.getNearest(48791);
                        walking.walkTileMM(wheels.getLocation());
                        camera.turnTo(wheels);
                        if (wheels != null) {
                            if (wheels.isOnScreen()) {

                                wheels.interact("Operate");
                                sleep(14000);

                            }



                        }



                        questAction = "Getting Caitlin's staff";

                        Second = npcs.getNearest(9627);
                        walking.walkTileMM(Second.getLocation());
                        sleep(7000);
                        if (Second != null) {
                            if (Second.isOnScreen()) {
                                while (!interfaces.canContinue()) {
                                    Second.interact("Talk");
                                    sleep(2000);
                                }
                                mouse.click(296, 456, true);
                                sleep(1200, 1700);
                                interfaces.getComponent(230, 3).doClick(true);
                                sleep(2000);
                                mouse.click(296, 456, true);
                                sleep(5000, 6000);
                                mouse.click(296, 456, true);
                                sleep(1200, 1700);
                                mouse.click(296, 456, true);
                                sleep(1200, 1700);
                                mouse.click(296, 456, true);
                                sleep(1200, 1700);
                            }
                            camera.turnTo(Second);
                        }
                        Second = npcs.getNearest(1);

                        while (!inventory.containsOneOf(15598)) {
                            RSGroundItem staff = groundItems.getNearest(15598);
                            if (staff != null) {
                                staff.interact("Take");
                            }
                        }
                        while (inventory.containsOneOf(15598) && !equipment.containsOneOf(15598)) {
                            inventory.getItem(15598).doClick(true);
                        }
                        if (equipment.containsOneOf(15598)) {
                            game.openTab(Tab.MAGIC);
                            magic.autoCastSpell(Spell.WIND_RUSH);
                        }
                        sleep(1000, 1100);
                        magic.autoCastSpell(Spell.WIND_RUSH);
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTWALKTOLAST:
                        questAction = "Walking to last cultist";
                        if (npcs.getNearest(9636) != null && npcs.getNearest(9636).isOnScreen()) {
                            startTalk(9636, true);
                            sleep(1000, 1100);
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        stairs = objects.getNearest(48678);
                        if (!stairs.isOnScreen()) {
                            walking.walkTileMM(stairs.getLocation());
                            sleep(3000, 3300);
                        }
                        stairs.interact("Climb");
                        while (getMyPlayer().getLocation().getZ() != 0) {
                            stairs = objects.getNearest(48678);
                            if (stairs != null) {
                                stairs.interact("Climb");
                            }
                            sleep(2000, 2200);
                        }
                        RSObject tombdoor = objects.getNearest(48793);
                        while (calc.distanceTo(tombdoor.getLocation()) > 3) {
                            walking.walkTileMM(tombdoor.getLocation());
                            sleep(300, 400);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }

                        }
                        sleep(300, 400);
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case BLOODPACTKILLLASTCULTIST:
                        questAction = "Killing last cultist";
                        tombdoor = objects.getNearest(48793);
                        while (!interfaces.canContinue()) {
                            sleep(2000, 2200);
                            tombdoor.interact("Open");
                        }
                        sleep(1000, 1100);
                        while (interfaces.canContinue()) {
                            clickContinue();
                            sleep(1500, 2000);
                        }
                        while (!clickDialogOption("scum")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        while (interfaces.canContinue()) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        RSNPC lastone = npcs.getNearest(9624);
                        walking.walkTileMM(lastone.getLocation());
                        if (lastone != null) {
                            lastone.interact("Attack");
                            sleep(30000);
                        }
                        sleep(1000, 2200);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("to die!")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!interfaces.canContinue()) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        RSNPC hostage = npcs.getNearest(9630);
                        if (!hostage.isOnScreen()) {
                            camera.turnTo(hostage);
                        }
                        hostage.interact("Untie");
                        while (calc.distanceTo(new RSTile(3246, 3198)) > 8) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (!clickDialogOption("nothing")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        sleep(1000, 1100);
                        while (interfaces.canContinue()) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        while (interfaces.canContinue()) {
                            clickContinue();
                            sleep(1000, 1100);
                        }
                        while (!clickDialogOption("go")) {
                            sleep(300, 400);
                        }
                        sleep(1000, 1100);
                        clickContinue();
                        currentQuestIndex++;
                        currentAction = actions.get(nextActionIndex++);
                        
                        break;
                    case DORICWALKTOBOB:
                        questName = "Doric's quest";
                        if (!walkPath(doricpathtobob)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.DORICOPENBOBDOOR;
                            questAction = "Opening Bob's door";
                        }
                        break;
                    case DORICOPENBOBDOOR:
                        door = objects.getNearest(45476);
                        if (door != null && calc.distanceTo(door) < 3) {
                            camera.turnTo(new RSTile(3235, 3203, 0));
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(45476);
                        }
                        walking.walkTileMM(new RSTile(3230, 3203, 0));
                        sleep(3000, 3300);
                        currentAction = Action.DORICBOBGETPICK;
                        questAction = "Getting bronze pickaxe";
                        break;
                    case DORICBOBGETPICK:
                        bob = npcs.getNearest(cookassbobid);
                        if (bob != null && bob.isOnScreen()) {
                            bob.interact("Trade");
                            sleep(1000, 1100);
                            while (!interfaces.get(620).isValid()) {
                                bob.interact("Trade");
                                sleep(1000, 1100);
                            }
                            if (interfaces.get(620).isValid()) {
                                interfaces.getComponent(620, 26).getComponent(0).interact("Take 1");
                                sleep(500, 600);
                            }
                            while (inventory.getCount(bronzepickid) == 0) {
                                interfaces.getComponent(620, 26).getComponent(0).interact("Take 1");
                                sleep(1500, 1600);
                            }
                            currentAction = Action.DORICSTARTWALK;
                            walking.walkTileMM(new RSTile(3230, 3202));
                            teleportLumbridge();
                            questAction = "Opening Bob's door";
                            nextTileIndex = -1;
                        }
                        break;
                    case DORICSTARTWALK:
                        questAction = "Walking to Doric";
                        if (!walkPath(pathtodoric)) {
                            return random(300, 400);
                        } else {
                            currentAction = Action.OPENDORICDOOR;
                            break;
                        }
                    case OPENDORICDOOR:
                        questAction = "Opening Doric's door";
                        door = objects.getNearest(1530);
                        while (door != null && door.isOnScreen()) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(1530);
                        }
                        currentAction = Action.DORICSTARTTALK;
                        break;
                    case DORICSTARTTALK:
                        questAction = "Talking to Doric";
                        RSNPC dwarf = npcs.getNearest(Dwarf);

                        if (dwarf != null) {
                            if (dwarf.isOnScreen()) {
                                while (!interfaces.canContinue()) {
                                    dwarf.interact("Talk");
                                    sleep(2000);
                                }
                                while (interfaces.canContinue()) {
                                    interfaces.clickContinue();
                                    sleep(500);
                                }
                                interfaces.getComponent(232, 2).doClick();
                                sleep(1000);

                                while (interfaces.canContinue()) {
                                    interfaces.clickContinue();
                                    sleep(500);
                                }
                                interfaces.getComponent(228, 2).doClick();
                                sleep(1000);

                                while (interfaces.canContinue()) {
                                    interfaces.clickContinue();
                                    sleep(500);
                                }

                            } else {
                                walking.walkTileMM(dwarf.getLocation());
                                camera.turnTo(dwarf);

                            }

                        }

                        door = objects.getNearest(doorClosed);
                        if (door != null) {
                            if (door.isOnScreen()) {
                                door.interact("Open");
                            } else {
                                camera.turnTo(door);
                            }
                        }
                        door = objects.getNearest(doorClosed);
                        if (door != null) {
                            if (door.isOnScreen()) {
                                door.interact("Open");
                            } else {
                                camera.turnTo(door);
                            }
                        }

                        if (inventory.getCount(clayOreID) >= 6 && inventory.getCount(440) >= 2 && inventory.getCount(436) >= 4) {
                            currentAction = Action.DORICENDTALK;
                        }
                        currentAction = Action.DORICWALKTOMINE;
                        break;
                    case DORICWALKTOMINE:
                        questAction = "Walking to mine";
                        /*RSPath pathToMine1 = walking.newTilePath(toMine);
                        while (calc.distanceTo(lastTile) > 5) {
                        pathToMine1.traverse();
                        while (getMyPlayer().isMoving()) {

                        sleep(500);
                        }
                        }*/
                        while (calc.distanceTo(toMine[toMine.length - 1]) > 5) {
                            if (!walkPath(toMine)) {
                                sleep(300, 400);
                            } else {
                                currentAction = Action.DORICOPENDOOR;
                                break;
                            }
                        }
                        currentAction = Action.DORICOPENDOOR;
                        break;
                    case DORICOPENDOOR:
                        questAction = "Opening door";
                        door = objects.getNearest(doorClosed2);
                        if (door != null) {
                            if (door.isOnScreen()) {
                                door.interact("Open");

                            } else {
                                camera.turnTo(door);
                            }
                        }

                        questAction = "Walking down stairs";

                        RSObject stairz = objects.getNearest(stairsid);
                        while (stairz == null || !stairz.isOnScreen()) {
                            stairz = objects.getNearest(stairsid);
                        }
                        while (calc.distanceTo(stairz) < 10) {
                            stairz.interact("Climb");
                            sleep(5000, 5500);
                        }
                        currentAction = Action.DORICMINEROCKS;
                        break;
                    case DORICMINEROCKS:
                        questAction = "Walking to clay";
                        RSWeb clayWeb = web.getWeb(clayTile);
                        while (!atClay()) {
                            clayWeb.step();
                            sleep(300, 400);
                            while (getMyPlayer().isMoving()) {
                                sleep(300, 400);
                            }
                        }
                        questAction = "Mining clay";
                        while (atClay() && inventory.getCount(clayOreID) < 6) {
                            RSObject clay1 = objects.getNearest(clayOre);
                            if (clay1 != null) {
                                if (clay1.isOnScreen()) {
                                    clay1.interact("Mine");
                                    while (getMyPlayer().getAnimation() != -1) {
                                        sleep(500);
                                    }
                                } else {
                                    camera.turnTo(clay1);
                                }
                            } else {
                                sleep(500);
                            }

                        }
                        if (inventory.getCount(clayOreID) >= 6 && inventory.getCount(440) < 2) {
                            RSWeb walkToIron = web.getWeb(new RSTile(3032, 9825));
                            questAction = "Walking to Iron";
                            while (!atIron()) {
                                walkToIron.step();
                            }
                            questAction = "Mining iron";




                            RSObject oreMine = objects.getNearest(ironOre);
                            while (inventory.getCount(440) < 2) {
                                if (oreMine != null) {
                                    if (oreMine.isOnScreen()) {
                                        oreMine.interact("Mine");
                                        while (getMyPlayer().getAnimation() != -1) {
                                            sleep(500);
                                        }
                                    }
                                } else {
                                    sleep(500);
                                }
                            }

                        }
                        if (inventory.getCount(clayOreID) >= 6 && inventory.getCount(440) >= 2 && inventory.getCount(436) < 4) {
                            walking.walkTileMM(new RSTile(3031, 9825));
                            RSObject copper = objects.getNearest(5781);
                            questAction = "Mining copper";
                            while (inventory.getCount(436) < 4) {
                                if (copper != null) {
                                    if (copper.isOnScreen()) {
                                        copper.interact("Mine");
                                        while (getMyPlayer().getAnimation() != -1) {
                                            sleep(500);
                                        }
                                    } else {
                                        camera.turnTo(copper);
                                    }
                                } else {
                                    sleep(500);
                                }
                            }
                        }

                        currentAction = Action.DORICWALKTOSTAIRS;
                        break;
                    case DORICWALKTOSTAIRS:
                        questAction = "Walking to stairs";
                        RSWeb webToStairs = web.getWeb(new RSTile(3058, 9776));
                        while (!atDownStairs()) {
                            webToStairs.step();
                        }

                        currentAction = Action.DORICCLIMBSTAIRS;



                        break;
                    case DORICCLIMBSTAIRS:
                        questAction = "Climbing stairs";
                        RSObject stair = objects.getNearest(30943);
                        if (stair != null) {
                            if (stair.isOnScreen()) {
                                stair.interact("Climb");

                            } else {
                                camera.turnTo(stair);
                            }
                        }
                        currentAction = Action.DORICOPENDOOR2;

                        break;
                    case DORICOPENDOOR2:
                        questAction = "Opening door";
                        door = objects.getNearest(doorClosed2);
                        if (door != null) {
                            if (door.isOnScreen()) {
                                door.interact("Open");

                            } else {
                                camera.turnTo(door);
                            }
                        }
                        currentAction = Action.DORICWALKBACK;
                        break;
                    case DORICWALKBACK:
                        questAction = "Walking back to Doric";
                        if (!walkPath(toDoric)) {
                            return random(300, 400);
                        } else {

                            currentAction = Action.DORICOPENDOORLAST;
                            break;
                        }
                    case DORICOPENDOORLAST:
                        questAction = "Opening Doric's door";
                        door = objects.getNearest(doorClosed);
                        if (door != null) {
                            if (door.isOnScreen()) {
                                door.interact("Open");
                            } else {
                                camera.turnTo(door);
                            }
                        }
                        sleep(3000);
                        door = objects.getNearest(doorClosed);
                        if (door != null) {
                            if (door.isOnScreen()) {
                                door.interact("Open");
                                sleep(5000);
                            } else {
                                camera.turnTo(door);
                            }
                        }
                        currentAction = Action.DORICENDTALK;
                        break;
                    case DORICENDTALK:
                        questAction = "Ending quest w00t";
                        RSNPC Doric = npcs.getNearest(Dwarf);
                        if (Doric != null) {
                            if (Doric.isOnScreen()) {
                                while (!interfaces.canContinue()) {
                                    Doric.interact("Talk");
                                    sleep(2000);
                                }
                                while (interfaces.canContinue()) {
                                    interfaces.clickContinue();
                                    sleep(1000);
                                }
                            }
                        }
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case VampStartQuest:
                questName = "Vampire slayer";
                        teleportLumbridge();

                        while (calc.distanceTo(new RSTile(3098, 3271)) > 4) {
                            RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), new RSTile(3098, 3271));
                            walkToTile.step();
                        }
                        log("End walk");
                        sleep(300, 400);
                        while (getMyPlayer().isMoving()) {
                            sleep(300, 400);
                        }
                        sleep(300, 400);
                        while (calc.distanceTo(new RSTile(3098, 3271)) > 4) {
                            walking.walkTileMM(new RSTile(3098, 3271));
                        }
                        log("Checking for door");
                        door = objects.getNearest(1239);
                        while (door != null && calc.distanceTo(door) < 4) {
                            door.interact("Open");
                            sleep(1000, 1100);
                            door = objects.getNearest(1239);
                        }
                        walking.walkTileMM(new RSTile(3098, 3271));
                        currentAction = Action.VampFirstTalk;
                        break;

                    case VampFirstTalk:
                        if (calc.distanceTo(objects.getNearest(1239)) < 4) {
                            inter(1239, "Open");
                            sleep(1000, 1100);
                        }
                        walking.walkTileMM(new RSTile(3098, 3271));
                        talkTo(755, "Talk");

                        clickContinue();
                        click(230, 3);
                        clickContinue();
                        click(228, 2);
                        clickContinue();
                        currentAction = Action.VampClimbStairs;
                        break;
                    case VampClimbStairs:
                        inter(2347, "climb");
                        sleep(3000);
                        currentAction = Action.VampGetOnion;
                        break;
                    case VampGetOnion:
                        walking.walkTileMM(new RSTile(3097, 3270));
                        sleep(2000);
                        while (!inventory.containsOneOf(1550)) {
                            inter(46250, "Open");
                            sleep(2000);
                            inter(12961, "Search");
                        }
                        teleportLumbridge();
                        currentAction = Action.VampWalkVarrock;
                        break;
                    case VampWalkVarrock:
                        while (calc.distanceTo(new RSTile(3215, 3395)) > 3) {
                            if (!walkPath(vamppathtovarrock)) {
                                return random(300, 400);
                            } else {
                                currentAction = Action.VampOpenDoorBar;
                                break;
                            }
                        }
                    case VampOpenDoorBar:
                        inter(24376, "Open");
                        sleep(3000);
                        walking.walkTileMM(new RSTile(3222, 3398));
                        sleep(3000);
                        currentAction = Action.VampTalkDoctor;
                        break;
                    case VampTalkDoctor:
                        talkTo(756, "Talk");
                        clickContinue();
                        click(230, 3);
                        clickContinue();
                        currentAction = Action.VampGetBeer;
                        break;
                    case VampGetBeer:
                        walking.walkTileMM(new RSTile(3224, 3397));
                        sleep(3000);
                        talkTo(733, "Talk");
                        clickContinue();
                        click(230, 2);
                        clickContinue();
                        currentAction = Action.VampTalkDoctor2;
                        break;
                    case VampTalkDoctor2:
                        walking.walkTileMM(new RSTile(3222, 3398));
                        sleep(3000);
                        talkTo(756, "Talk");
                        clickContinue();
                        magic.castSpell(Spell.LUMBRIDGE_HOME_TELEPORT);
                        sleep(5000);
                        while (getMyPlayer().getAnimation() == -1) {
                            magic.castSpell(Spell.LUMBRIDGE_HOME_TELEPORT);
                            sleep(5000);
                        }
                        while (getMyPlayer().getAnimation() != -1) {
                            sleep(50);
                        }
                        currentAction = Action.VampWalkCastle;
                        break;
                    case VampWalkCastle:
                        while (calc.distanceTo(new RSTile(3109, 3353)) > 3) {
                            if (walking.getEnergy() >= 40) {
                                if (!walking.isRunEnabled()) {
                                    walking.setRun(true);
                                }
                            }
                            RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), new RSTile(3109, 3353));
                            walkToTile.step();
                            sleep(3000);
                        }
                        currentAction = Action.VampOpenCastle;
                        break;
                    case VampOpenCastle:
                        inter(47421, "Open");
                        while (interfaces.canContinue()) {
                            interfaces.clickContinue();
                            sleep(1500);
                        }
                        click(236, 2);
                        currentAction = Action.VampToStairs;
                        break;
                    case VampToStairs:
                        walking.walkTileMM(new RSTile(3112, 3360));
                        sleep(2000);
                        inter(47512, "Open");
                        sleep(2000);
                        while(calc.distanceTo(new RSTile(3116, 3355)) >4){
                        walking.walkTileMM(new RSTile(3116, 3355));

                        sleep(3000);
                        }
                        currentAction = Action.VampDownCastle;

                        break;
                    case VampDownCastle:
                        inter(47643, "Walk");
                        sleep(2000);
                        walking.walkTileMM(new RSTile(3080, 9784));
                        sleep(4000);
                        if (getMyPlayer().getLocation().getZ() == -1) {
                            currentAction = Action.VampToStairs;
                        } else {
                            currentAction = Action.VampOpenChest;
                        }
                        break;
                    case VampOpenChest:
                        inter(158, "Open");
                        sleep(30000);
                        currentAction = Action.VampKillVamp;
                        break;
                    case VampKillVamp:
                        kill(9357, "Attack");
                        currentAction = Action.VampUpStairs;
                        break;
                    case VampUpStairs:
                        inter(164, "Climb");
                        sleep(5000);
                        currentAction = Action.VampToExit;
                        break;
                    case VampToExit:
                        walking.walkTileMM(new RSTile(3119, 3357));
                        sleep(4000);
                        inter(47512, "Open");
                        sleep(1000);
                        walking.walkTileMM(new RSTile(3123, 3363));
                        sleep(1000);
                        inter(47443, "Open");
                        currentAction = Action.VampWalkLast;
                        break;
                    case VampWalkLast:
                        while (calc.distanceTo(new RSTile(3098, 3271)) > 3) {
                            if (walking.getEnergy() >= 40) {
                                if (!walking.isRunEnabled()) {
                                    walking.setRun(true);
                                }
                            }
                            RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), new RSTile(3098, 3271));
                            walkToTile.step();
                            sleep(3000);
                        }
                        currentAction = Action.VampOpenLast;
                        break;
                    case VampOpenLast:
                        inter(1239, "Open");
                        sleep(2000);
                        currentAction = Action.VampTalkLast;
                        break;
                    case VampTalkLast:
                        talkTo(755, "Talk");
                        clickContinue();
                        currentQuestIndex ++;
                        currentAction = actions.get(nextActionIndex++);
                        break;
                    case GunFirstWalk:
                        questAction = "Walking to gunnar";
                        questName = "Gunnar's ground";
                         while(calc.distanceTo(new RSTile(3078, 3333)) >4){
                      walkPath(gunpathtogunnar);
                      while(getMyPlayer().isMoving()){
                          sleep(50);
                      }
                        }
                        if(calc.distanceTo(new RSTile(3078, 3333)) <4){
                            walking.walkTileMM(new RSTile(3075,3338));
                            sleep(3000);
                        }
                      while(interfaces.canContinue()){
                          interfaces.clickContinue();
                          sleep(2000);
                      
                        }
                        click(236,1);
                        sleep(200);
                           walking.walkTileMM(new RSTile(3075,3338));
                           sleep(3000);
                           while(calc.distanceTo(new RSTile(3099, 3422)) >4){
                      walkTo(new RSTile(3099, 3422));
                        }
                            currentAction = Action.GunFirstTalk;
                            break;
                        
                    
                        
                    case GunFirstTalk:
                        questAction = "Talking to gunnar";
                        talkTo(1168, "Talk");
                        clickContinue();
                        click(451, 1);
                        clickContinue();
                        click(451, 1);
                        clickContinue();
                        click(451, 1);
                        clickContinue();
                        click(236, 2);
                        clickContinue();
                        click(236, 1);
                        clickContinue();
                        click(178, 78);
                        clickContinue();

                        currentAction = Action.GunWalkEdge;
                        break;
                    case GunWalkEdge:
                        questAction = "Walking to edgeville";
                        walkTo(new RSTile(3108, 3501));
                        currentAction = Action.GunGetRing;
                        break;
                    case GunGetRing:
                        questAction = "Talking to smith";
                        talkTo(637, "Talk");
                        clickContinue();
                        while (!clickDialogOption("here")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("hoping")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("splendid")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Yes")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("gold")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        currentAction = Action.GunWalkDwarf;
                        break;

                    case GunWalkDwarf:
                        questAction = "Walking to dwarf";
                        walkTo(new RSTile(3099, 3422));
                        currentAction = Action.GunTalkDwarf;
                        break;

                    case GunTalkDwarf:
                        questAction = "Talking to dwarf";
                        talkTo(1168, "talk");
                        clickContinue();
                        RSComponent comp = getComponentContaining("What");

                        while (!clickDialogOption("What")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();

                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("simple")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Do")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();


                        while (!inventory.isItemSelected()) {
                            inventory.getItem(1755).doClick(true);
                            sleep(2000);
                        }
                        if (inventory.isItemSelected()) {
                            inventory.getItem(19770).doClick(true);
                            sleep(4000);
                        }
                        clickContinue();
                        talkTo(1168, "Talk");
                        clickContinue();
                        while (!clickDialogOption("look")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Of")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Very")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("I")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();

                        currentAction = Action.GunWalkGud;



                        break;
                    case GunWalkGud:
                        questAction = "Walking to Gudrun";
                        walkTo(new RSTile(3082, 3415));
                        RSNPC Gud = npcs.getNearest(2864);
                        if (Gud != null) {
                            walking.walkTileMM(Gud.getLocation());
                        }
                        talkTo(2864, "Talk");
                        currentAction = Action.GunTalkGud;
                        break;

                    case GunTalkGud:
                        questAction = "Talking to gudrun";
                        talkTo(2864, "Talk");
                        clickContinue();
                        sleep(1000);
                        while (!clickDialogOption("Yes")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("me")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("poet")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("talk")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("see")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        currentAction = Action.GunWalkFather;
                        break;

                    case GunWalkFather:
                        questAction = "Walking to father";
                        walkTo(new RSTile(3079, 3435));
                        inter(11621, "Open");
                        sleep(1000);
                        walkTo(new RSTile(3079, 3444));
                        currentAction = Action.GunTalkFather;
                        break;
                    case GunTalkFather:
                        questAction = "Talking to father";
                        talkTo(2876, "Talk");
                        clickContinue();
                        sleep(1000);

                        while (!clickDialogOption("speak")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("daughter")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("tribe")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("finished")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("try")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        currentAction = Action.GunWalkGud2;
                        break;
                    case GunWalkGud2:
                        walkTo(new RSTile(3082, 3415));
                        Gud = npcs.getNearest(2864);
                        if (Gud != null) {
                            walking.walkTileMM(Gud.getLocation());
                        }
                        talkTo(2864, "Talk");
                        currentAction = Action.GunTalkGud2;
                        break;
                    case GunTalkGud2:
                        talkTo(2864, "Talk");
                        clickContinue();
                        sleep(2000);
                        while (!clickDialogOption("What")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        currentAction = Action.GunWalkDwarf2;
                        break;

                    case GunWalkDwarf2:
                        walkTo(new RSTile(3099, 3422));
                        currentAction = Action.GunTalkDwarf2;
                        break;

                    case GunTalkDwarf2:
                        talkTo(1168, "Talk");
                        clickContinue();
                        while (!clickDialogOption("liked")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("going")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        sleep(6000);
                        clickContinue();
                        while (!clickDialogOption("problem")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Saradomin")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Lay")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("More")) {
                            sleep(200);
                        }
                        sleep(1000);
                        while (!clickDialogOption("Stray")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Longsword")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Peril")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("More")) {
                            sleep(200);
                        }
                        sleep(1000);
                        while (!clickDialogOption("Threat")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Schemed")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("Picked")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("More")) {
                            sleep(200);
                        }
                        sleep(1000);
                        while (!clickDialogOption("Swept")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();

                        currentAction = Action.GunWalkGud3;
                        break;
                    case GunWalkGud3:
                        walkTo(new RSTile(3082, 3415));
                        Gud = npcs.getNearest(2864);
                        if (Gud != null) {
                            walking.walkTileMM(Gud.getLocation());
                        }
                        talkTo(2864, "Talk");
                        currentAction = Action.GunTalkGud3;
                        break;
                    case GunTalkGud3:
                        talkTo(2864, "Talk");
                        clickContinue();
                        sleep(6000);
                        clickContinue();
                        sleep(5000);
                        clickContinue();


                        while (!clickDialogOption("just")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("so")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("silly")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("just")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        while (!clickDialogOption("chance")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        sleep(8000);
                        clickContinue();
                        sleep(8000);
                        clickContinue();
                        sleep(8000);
                        clickContinue();
                        while (!clickDialogOption("soon")) {
                            sleep(200);
                        }
                        sleep(1000);
                        clickContinue();
                        sleep(8000);
                        currentQuestIndex ++;
                        currentAction = actions.get(nextActionIndex++);
                        break;
                }
                return random(300, 400);
            }
        } catch (Exception e) {
            return random(300, 400);
        }
    }

    public void interactInterface(int a, int b, String command) {
        while (!interfaces.get(a).isValid()) {
            sleep(100, 200);
        }
        if (interfaces.get(a).isValid()) {
            interfaces.getComponent(a, b).interact(command);
            sleep(500, 600);
        }
        mouse.moveRandomly(7);
    }

    public void clickInterface(int a, int b) {
        while (!interfaces.get(a).isValid()) {
            sleep(100, 200);
        }
        if (interfaces.get(a).isValid()) {
            interfaces.getComponent(a, b).doClick();
            sleep(500, 600);
        }
        mouse.moveRandomly(7);
    }

    public void clickInterface(int a, int b, int c) {
        while (!interfaces.get(a).isValid()) {
            sleep(100, 200);
        }
        if (interfaces.get(a).isValid()) {
            interfaces.getComponent(a, b).getComponent(c).doClick();
            sleep(500, 600);
        }
        mouse.moveRandomly(7);
    }

    public void interactInterface(int a, int b, int c, String command) {
        while (!interfaces.get(a).isValid()) {
            sleep(100, 200);
        }
        if (interfaces.get(a).isValid() && interfaces.getComponent(a, b).isValid() && interfaces.getComponent(a, b).getComponent(c).isValid()) {
            interfaces.getComponent(a, b).getComponent(c).interact(command);
            sleep(500, 600);
        }
        mouse.moveRandomly(7);
    }

    public void startTalk(int id, boolean mustContinue) {
        while (getMyPlayer().isMoving()) {
            sleep(300, 400);
        }
        RSNPC npc = npcs.getNearest(id);
        if (npc == null || !npc.isOnScreen()) {
            log("NPC Not Found");
        } else {
            npc.interact("Talk");
            sleep(1000, 1100);
            while (getMyPlayer().isMoving()) {
                sleep(300, 400);
            }
            //mouse.click(cook.getScreenLocation().x, cook.getScreenLocation().y, 1, 1, true);
            sleep(1000, 1100);
        }
        if (id == nedid) {
            while (getComponentContaining("something") == null) {
                sleep(1500, 1600);
                npc.interact("Talk");
                sleep(1000, 1100);
                while (getMyPlayer().isMoving()) {
                    sleep(300, 400);
                }
            }
        }
        if (mustContinue) {
            while (!interfaces.canContinue()) {
                sleep(1500, 1600);
                npc.interact("Talk");
                sleep(1000, 1100);
                while (getMyPlayer().isMoving()) {
                    sleep(300, 400);
                }
            }
        }
    }

    public boolean clickDialogOption(String o) {
        mouse.moveRandomly(7);
        dialogOption = o;
        RSComponent comp = getComponentContaining(o);
        if (comp != null) {
            comp.doClick(true);
            return true;
        }
        return false;
    }

    public void mineRocks(int[] ids) {
        sleep(1000, 1100);
        while (getMyPlayer().isMoving()) {
            sleep(300, 400);
        }
        RSObject rock = objects.getNearest(ids);
        if (rock != null) {
            if (!rock.isOnScreen()) {
                walking.walkTileMM(rock.getLocation());
                sleep(1300, 1400);
                while (getMyPlayer().isMoving()) {
                    sleep(300, 400);
                }
                sleep(1000, 1100);
            }
            rock.interact("Mine");
            sleep(1000, 1100);
        }
    }

    public void cutTree(int[] ids) {
        RSObject tree = objects.getNearest(ids);
        if (tree != null && tree.isOnScreen()) {
            tree.interact("Chop");
            sleep(1000, 1100);
        }
    }

    public void teleportLumbridge() {
        if (inArea(lumbridge1, lumbridge2)) {
            return;
        }
        magic.castSpell(Spell.LUMBRIDGE_HOME_TELEPORT);
        sleep(4000, 5000);
        boolean toggle = false;
        if (getMyPlayer().getAnimation() == -1) {
            toggle = true;
            magic.castSpell(Spell.LUMBRIDGE_HOME_TELEPORT);
            sleep(4000, 5000);
        }
        while (!inArea(lumbridge1, lumbridge2)) {
            if (toggle) {
                if (getMyPlayer().getAnimation() == -1) {
                    magic.castSpell(Spell.LUMBRIDGE_HOME_TELEPORT);
                    sleep(4000, 5000);
                } else {
                    toggle = false;
                }
            }
            sleep(1000, 1100);
        }
    }

    public void rswebStep(RSTile tile) {
        RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), tile);
        walkToTile.step();
    }

    //START: Code generated using Enfilade's Easel
    private Image getImage(String url) {
        try {
            return ImageIO.read(new URL(url));
        } catch(IOException e) {
            return null;
        }
    }

    private final Color color1 = new Color(0, 0, 0);

    private final Font font1 = new Font("Batang", 0, 13);

    private final Image img1 = getImage("http://i1085.photobucket.com/albums/j439/Xiffs/Paint/paint.png");
    private final Image img2 = getImage("http://www.dobrogeagrup.ro/images/CloseButton.gif");



    private boolean show = true;

    public void onRepaint(Graphics g1) {

        Graphics2D g = (Graphics2D) g1;


      

 if (show2) {
     Point Mouse = mouse.getLocation();
        int x1 = (int) Mouse.getX();
        int y1 = (int) Mouse.getY();
         g.setColor(new Color(255, 0, 0, 75));
        g.drawLine(x1, 0, x1, game.getHeight());
        g.drawLine(0, y1, game.getWidth(), y1);
        g.fillRect(x1 - 1, y1 - 1, 3, 3);
        while (!mousePath.isEmpty() && mousePath.peek().isUp()) {
            mousePath.remove();
        }
        Point clientCursor = mouse.getLocation();
        MousePathPoint mpp = new MousePathPoint(clientCursor.x, clientCursor.y,
                1000); //1000 = lasting time/MS
        if (mousePath.isEmpty() || !mousePath.getLast().equals(mpp)) {
            mousePath.add(mpp);
        }
        MousePathPoint lastPoint = null;
        for (MousePathPoint a : mousePath) {
            if (lastPoint != null) {
                g.setColor(a.getColor());
                g.drawLine(a.x, a.y, lastPoint.x, lastPoint.y);
            }
            lastPoint = a;
        }

            String time = "";
            if (processed) {
                long cur = System.currentTimeMillis();
                long timepassed = cur - start;
                time += (timepassed / 3600000) + ":";
                time += (timepassed / 60000) % 60 + ":";
                time += (timepassed / 1000) % 60 + "";
            }
        g.drawImage(img1, 2, 339, null);
        g.setFont(font1);
        g.setColor(color1);
        g.drawString(Timer.format(System.currentTimeMillis() - startTime), 101, 365);
        g.drawString(currentQuestIndex +  " quests", 101, 379);
        g.drawString(questName, 318, 363);
        g.drawString(questAction, 317, 379);
g.drawImage(img2, 498, 347, null);




            
          

            Point p = mouse.getLocation();
            int x = p.x;
            int y = p.y;
            int color = random(0, 3);
            if (mouse.isPressed()) {
                synchronized (lock) {
                    for (int i = 0; i < 50; i++, particles.add(new Particle(x, y, color)))                       ;
                }
            }
            synchronized (lock) {
                Iterator<Particle> piter = particles.iterator();
                while (piter.hasNext()) {
                    Particle part = piter.next();
                    if (!part.handle(g)) {
                        piter.remove();
                    }
                }
            }

        }
 else{
             g.drawImage(img2, 498, 347, null);

 }
        
    }

    //THE FOLLOWING METHODS ARE FOR THE RESTLESS GHOST QUEST
    public boolean talkToFather1() {
        questAction = "Talking to Father";
        RSNPC father = npcs.getNearest(456);
        if (father != null) {
            if (!father.isOnScreen()) {
                camera.turnTo(father);
            } else {
                father.interact("Talk");
                while (!interfaces.canContinue()) {
                    father.interact("Talk");
                    sleep(300, 400);
                }
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(208, 441, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                interfaces.getComponent(178, 78).doClick();
                TalkedToFather1 = true;
            }

        }
        return true;
    }

    public boolean openChest() {
        questAction = "Opening Chest";
        RSObject Chest = objects.getNearest(chest);
        if (Chest != null) {
            Chest.interact("Open");
            openedChest = true;
        } else {
            RSObject chest = objects.getNearest(openChest);
            if (chest != null) {
                chest.interact("Search");
                openedChest = true;
            }
        }


        return true;
    }

    public boolean openChest2() {
        questAction = "Opening Chest";
        RSObject Chest = objects.getNearest(chest);
        if (Chest != null) {
            Chest.interact("Open");

        } else {
            RSObject chest = objects.getNearest(openChest);
            if (chest != null) {
                chest.interact("Search");

            }
        }


        return true;
    }

    public boolean openDoor() {
        questAction = "Opening door";
        RSObject Door = objects.getNearest(door);
        if (Door != null) {
            Door.interact("Open");
            return true;
        } else {
            return true;
        }
    }

    public boolean walkToFather2() {
        questAction = "Walking to Father";
        if (!inFrontOfFathersHouse()) {
            RSWeb walkToFather = web.getWeb(getMyPlayer().getLocation(), frontOfHouse);
            walkToFather.step();
        } else {

            if (inFrontOfFathersHouse()) {
                openDoor();
                sleep(4000);
            }
            walking.walkTileMM(inHouse);
            WalkedToFather2 = true;

        }
        return true;
    }

    public boolean talkToFather2() {
        questAction = "Talking to Father";
        RSNPC father = npcs.getNearest(458);
        if (father != null) {
            if (!father.isOnScreen()) {
                camera.turnTo(father);
            } else {
                father.interact("Talk");
                while (!interfaces.canContinue()) {
                    father.interact("Talk");
                    sleep(300, 400);
                }
                mouse.click(296, 456, true);
                sleep(1200, 1700);

                interfaces.getComponent(230, 3).doClick();
                sleep(1000);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                interfaces.getComponent(228, 2).doClick();
                sleep(500);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                mouse.click(296, 456, true);
                sleep(1200, 1700);
                if (inventory.containsOneOf(amulet)) {
                    inventory.getItem(amulet).interact("Wear");
                }
                sleep(2000);
                openDoor();
                sleep(2000);
                walking.walkTileMM(frontOfHouse);
                walking.walkTileMM(tile);
                sleep(2000);
                TalkedToFather2 = true;

            }

        }

        return true;
    }

    public boolean talkToGhost() {
        questAction = "Talking to Ghost";
        while (inventory.contains(amulet)) {
            inventory.getItem(amulet).doClick(true);
            sleep(1000, 1100);
        }
        openChest();
        openedChest = true;
        sleep(7000);
        RSNPC Ghost = npcs.getNearest(457);
        if (Ghost != null) {
            Ghost.interact("Talk");

        }
        sleep(3000);
        while (!interfaces.canContinue()) {
            Ghost.interact("Talk");
            sleep(300, 400);
        }
        while (interfaces.canContinue()) {
            interfaces.clickContinue();
            sleep(1000);
        }

        mouse.click(249, 393, true);
        sleep(1200, 1700);
        while (interfaces.canContinue()) {
            interfaces.clickContinue();
            sleep(1200, 1700);
        }

        while (interfaces.canContinue()) {
            interfaces.clickContinue();
            sleep(1000, 1100);
        }
        combat.setAutoRetaliate(false);
        game.openTab(Tab.INVENTORY);
        TalkedToGhost = true;
        return true;
    }

    public boolean walkToMine() {
        questAction = "Walking to Mine";
        if (!atMine()) {
            RSWeb walkToGhost = web.getWeb(getMyPlayer().getLocation(), mineTile);
            walkToGhost.step();
        }
        if (atMine()) {
            WalkedToMine = true;
        }
        return true;
    }

    public boolean atMine() {
        RSArea Mine = mineArea;
        if (Mine.contains(getMyPlayer().getLocation())) {
            return true;
        }
        return false;
    }

    public boolean inFrontOfFathersHouse() {
        RSArea FHouse = frontOfHouseArea;
        if (FHouse.contains(getMyPlayer().getLocation())) {
            return true;
        }
        return false;
    }

    public boolean inFathersHouse() {
        RSArea inFHouse = inHouseArea;
        if (inFHouse.contains(getMyPlayer().getLocation())) {
            return true;
        }

        return false;
    }

    public boolean walkToGhost() {
        questAction = "Walking to Ghost";
        if (!atGhost()) {
            RSWeb walkToGhost = web.getWeb(getMyPlayer().getLocation(), ghostTile);
            walkToGhost.step();
        }
        if (atGhost()) {
        }

        return true;
    }

    public boolean walkToGhost2() {
        questAction = "Walking to Ghost";
        if (!atGhost()) {
            RSWeb walkToGhost = web.getWeb(getMyPlayer().getLocation(), ghostTile);
            walkToGhost.step();

        }
        if (getMyPlayer().getLocation().equals(ghostTile)) {
            WalkedToGhost2 = true;
        }
        return true;

    }

    public boolean getHead() {
        questAction = "Getting Head";
        RSObject stone = objects.getNearest(47713);
        if (walking.getEnergy() < 50) {
            walking.rest(50);
        }
        if (stone != null) {
            stone.interact("Search");
            if (inventory.containsOneOf(553)) {
                headGained = true;
            }
        }
        return true;
    }

    public boolean finishQuest() {
        questAction = "Finishing quest";
        RSItem head = inventory.getItem(553);
        RSObject chestOpen = objects.getNearest(openChest);
        RSObject chestClosed = objects.getNearest(chest);

        while (chestClosed != null) {
            chestClosed.interact("Open");
            sleep(1000, 1100);
            chestClosed = objects.getNearest(chest);
        }
        head.doClick(true);
        objects.getNearest(openChest).doClick(true);
        return true;
    }

    public boolean atGhost() {
        RSArea Ghost = atGhost;
        if (Ghost.contains(getMyPlayer().getLocation())) {
            WalkedToGhost = true;
            return true;
        }
        return false;
    }

    public boolean atStairs() {
        RSArea area = StairArea;
        return area.contains(getMyPlayer().getLocation());
    }

    public boolean atClay() {
        RSArea area = clayArea;
        return area.contains(getMyPlayer().getLocation());
    }

    public boolean atIron() {
        RSArea area = IronArea;
        return area.contains(getMyPlayer().getLocation());
    }

    public boolean atDownStairs() {
        RSArea area = StairAreaDown;
        return area.contains(getMyPlayer().getLocation());
    }

    public boolean atShed() {
        RSArea area = FrontOfShed;
        return area.contains(getMyPlayer().getLocation());
    }

    private static class Particle {

        private double posX;
        private double posY;
        private double movX;
        private double movY;
        private int alpha = 255, color = -1;
        java.util.Random generator = new java.util.Random();

        Particle(int pos_x, int pos_y, int color) {
            posX = (double) pos_x;
            posY = (double) pos_y;
            movX = ((double) generator.nextInt(40) - 20) / 16;
            movY = ((double) generator.nextInt(40) - 20) / 16;
            this.color = color;
        }

        public boolean handle(Graphics page) {
            alpha -= random(1, 7);
            if (alpha <= 0) {
                return false;
            }
            switch (color) {
                case 0:
                    page.setColor(new Color(0, 0, 0, alpha));
                    break;
                case 1:
                    page.setColor(new Color(255, 0, 0, alpha));
                    break;
                case 2:
                    page.setColor(new Color(255, 255, 255, alpha));
                    break;
            }
            page.drawLine((int) posX, (int) posY, (int) posX, (int) posY);
            posX += movX;
            posY += movY;
            return true;
        }
    }

    private void drawTile(Graphics render, RSTile tile, Color color) {
        Point southwest = calc.tileToScreen(tile, 0, 0, 0);
        Point southeast = calc.tileToScreen(new RSTile(tile.getX() + 1, tile.getY()), 0, 0, 0);
        Point northwest = calc.tileToScreen(new RSTile(tile.getX(),
                tile.getY() + 1), 0, 0, 0);
        Point northeast = calc.tileToScreen(new RSTile(tile.getX() + 1, tile.getY() + 1), 0, 0, 0);

        if (calc.pointOnScreen(southwest) && calc.pointOnScreen(southeast)
                && calc.pointOnScreen(northwest)
                && calc.pointOnScreen(northeast)) {
            render.setColor(Color.BLACK);
            render.drawPolygon(new int[]{(int) northwest.getX(),
                        (int) northeast.getX(), (int) southeast.getX(),
                        (int) southwest.getX()}, new int[]{
                        (int) northwest.getY(), (int) northeast.getY(),
                        (int) southeast.getY(), (int) southwest.getY()}, 4);
            render.setColor(color);
            render.fillPolygon(new int[]{(int) northwest.getX(),
                        (int) northeast.getX(), (int) southeast.getX(),
                        (int) southwest.getX()}, new int[]{
                        (int) northwest.getY(), (int) northeast.getY(),
                        (int) southeast.getY(), (int) southwest.getY()}, 4);




        }
    }

    @SuppressWarnings("serial")
    private class MousePathPoint extends Point { // All credits to Enfilade

        private long finishTime;
        private double lastingTime;

        private int toColor(double d) {
            return Math.min(255, Math.max(0, (int) d));
        }

        public MousePathPoint(int x, int y, int lastingTime) {
            super(x, y);
            this.lastingTime = lastingTime;
            finishTime = System.currentTimeMillis() + lastingTime;
        }

        public boolean isUp() {
            return System.currentTimeMillis() > finishTime;
        }

        public Color getColor() {

            return new Color(200, 0, 0, toColor(256 * ((finishTime - System //first 3 numbers are the colour
                    .currentTimeMillis()) / lastingTime)));
        }
    }
    private final Rectangle OpenLink = new Rectangle(47, 323, 319, 7);
    private final Rectangle Hide = new Rectangle(498, 347, 16, 16);
    private final Rectangle Screenshot = new Rectangle(43, 447, 125, 13);
    private final Rectangle Yes = new Rectangle(188, 26, 139, 16);
    private final Rectangle No = new Rectangle(332, 26, 139, 16);

    public void mouseClicked(MouseEvent e) {

        if (Hide.contains(e.getPoint())) {
            if (show2) {
                show2 = !show2;
            } else {
                show2 = true;
            }
        }
       
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub
    }

    @Override
    public void mouseReleased(MouseEvent e) {
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

    public void mouseHovered(MouseEvent e) {
    }

    public boolean openSite() throws URISyntaxException, IOException {
        java.net.URI myNewLocation = new java.net.URI(Thread);
        myNewBrowserDesktop.browse(myNewLocation);
        return true;
    }

    public void kill(int g, String doz) {
        RSNPC m = npcs.getNearest(g);
        camera.turnTo(m);
        if (m != null) {
            if (m.isOnScreen()) {
                m.interact("Attack");
                while (!m.isDead()) {
                    sleep(200, 300);
                }
            } else {
                camera.turnTo(m);
            }
        }
    }

    public void click(int a, int b) {
        interfaces.getComponent(a, b).doClick();
        sleep(1000);
    }

    public void clickContinue() {
        while (interfaces.canContinue()) {
            interfaces.clickContinue();
            sleep(1000);
        }
    }

    private void talkTo(int d, String action2) {
        RSNPC a = npcs.getNearest(d);
        while (!a.isOnScreen()) {
            camera.turnTo(a);
        }
        if (a != null) {
            if (a.isOnScreen()) {
                while (!interfaces.canContinue()) {
                    a.interact(action2);
                    sleep(2000);
                }
            } else {
                camera.turnTo(a);
            }
        }
    }

    private void inter(int c, String action) {
        RSObject a = objects.getNearest(c);

        sleep(2000);
        if (a != null) {
            if (a.isOnScreen()) {

                a.interact(action);
            } else {
                camera.turnTo(a);
            }
        }

    }

    private void walkTo(RSTile tile) {
        RSWeb walkToTile = web.getWeb(getMyPlayer().getLocation(), tile);
        while (calc.distanceTo(tile) > 4) {
            walkToTile.step();
        }
    }
            public void tray(){
                   if (!SystemTray.isSupported()) {
                        System.out.println("SystemTray is not supported");
                        return;
                }
                     final PopupMenu popup = new PopupMenu();
                       MenuItem aboutItem = new MenuItem("About");
                MenuItem state1 = new  MenuItem("Stop Script");
                MenuItem exitItem = new MenuItem("Remove tray");
                MenuItem site = new MenuItem("Go to script thread");
                popup.add(aboutItem);
                popup.add(site);
                popup.addSeparator();
                popup.add(state1);
                popup.addSeparator();
                popup.add(exitItem);
                  trayIcon.setPopupMenu(popup);
                trayIcon.setToolTip("Running for" + Timer.format(System.currentTimeMillis() - startTime));
                   state1.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                trayIcon.displayMessage("Stop Script","Script stopped!",TrayIcon.MessageType.WARNING);
          tray.remove(trayIcon);
                stopScript();
                }
                });
                 aboutItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                trayIcon.displayMessage("About","This script is made by xiffs and ericthecmh, this script is for completing quests", TrayIcon.MessageType.INFO);

                }
                });
                exitItem.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                tray.remove(trayIcon);

                }
                });
                 site.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
              java.awt.Desktop myNewBrowserDesktop = java.awt.Desktop.getDesktop();
              java.net.URI myNewLocation = null;
                try {
                    myNewLocation = new java.net.URI(Thread);
                } catch (URISyntaxException ex) {
                    Logger.getLogger(AIOQuester.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    myNewBrowserDesktop.browse(myNewLocation);
                } catch (IOException ex) {
                    Logger.getLogger(AIOQuester.class.getName()).log(Level.SEVERE, null, ex);
                }

                }
                });
           try {
                        tray.add(trayIcon);
                } catch (AWTException e) {
                        log("TrayIcon could not be added.");
                        return;
                }
                trayIcon.displayMessage("AIO Quester","Is now running", TrayIcon.MessageType.INFO);




}
            public boolean GetBucket(){
    RSObject ladder = objects.getNearest(ladderID);
if (ladder != null && ladder.isOnScreen()) {
   ladder.interact("Climb");


}
    sleep(5000);
walking.walkTileMM(BucketTile);
sleep(2000);
while(!inventory.containsOneOf(bucket)){
 RSGroundItem gitem = groundItems.getNearest(bucket);
                                if(gitem !=null && (!inventory.isFull())){
                                        if(gitem.isOnScreen()){
                                                if(calc.distanceTo(gitem.getLocation()) < 2){
                                                        grabItem(gitem);
                                                        sleep(random(1000,1200));
                                                }
                                        } else{
                                                camera.turnTo(gitem.getLocation());
                                        }
                                }
if(inventory.containsOneOf(bucket)){
walking.walkTileMM(LadderTile);
        camera.setPitch(80);
        sleep(5000);
        upLadder();

           }


    }
return true;

    }
            private void upLadder() {
     RSObject lowerLadder = objects.getNearest(ladderID2);
          if(lowerLadder != null) {
               lowerLadder.interact("Climb-Up");


                   sleep(1000);

          }
}
             public boolean grabItem(RSGroundItem mItem) {
                camera.setPitch(100);
                camera.setCompass('n');
                return tiles.interact(mItem.getLocation(), random(.45, .55), random(.75, .95), 0, "Take " + mItem.getItem().getName());
        }
             public boolean GetPot(){
while(!inventory.containsOneOf(pot)){
 RSGroundItem gitem = groundItems.getNearest(pot);
                                if(gitem !=null && (!inventory.isFull())){
                                        if(gitem.isOnScreen()){
                                                if(calc.distanceTo(gitem.getLocation()) < 2){
                                                        grabItem(gitem);
                                                        sleep(random(1000,1200));
                                                }else{
                                                        walking.walkTileMM(gitem.getLocation());
                                                        sleep(random(1500,1800));
                                                }
                                        } else{
                                                camera.turnTo(gitem.getLocation());
                                        }
    }
                                }
    return true;
}
            public void onFinish(){
                 tray.remove(trayIcon);
            }
              public boolean isOnTradeWindow(){
                return interfaces.get(334).isValid() || interfaces.get(335).isValid();

        }

}
