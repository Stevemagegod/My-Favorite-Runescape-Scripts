    import java.awt.*;
    import java.awt.event.*;
    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStreamReader;
    import java.net.URL;
    import java.net.URLConnection;
     
    import org.powerbot.game.api.methods.Settings;
    import javax.swing.*;
    import org.powerbot.game.client.*;
     
    import org.powerbot.core.event.listeners.PaintListener;
    import org.powerbot.core.script.ActiveScript;
    import org.powerbot.core.script.job.Task;
    import org.powerbot.core.script.job.state.Node;
    import org.powerbot.game.api.Manifest;
    import org.powerbot.game.api.methods.Calculations;
    import org.powerbot.game.api.methods.Environment;
    import org.powerbot.game.api.methods.Game;
    import org.powerbot.game.api.methods.Settings;
    import org.powerbot.game.api.methods.Walking;
    import org.powerbot.game.api.methods.Widgets;
    import org.powerbot.game.api.methods.input.Keyboard;
    import org.powerbot.game.api.methods.input.Mouse;
    import org.powerbot.game.api.methods.interactive.NPCs;
    import org.powerbot.game.api.methods.interactive.Players;
    import org.powerbot.game.api.methods.node.GroundItems;
    import org.powerbot.game.api.methods.node.SceneEntities;
    import org.powerbot.game.api.methods.tab.Inventory;
    import org.powerbot.game.api.methods.tab.Skills;
    import org.powerbot.game.api.methods.widget.Bank;
    import org.powerbot.game.api.methods.widget.Camera;
    import org.powerbot.game.api.util.net.GeItem;
    import org.powerbot.game.api.wrappers.Area;
    import org.powerbot.game.api.wrappers.Tile;
    import org.powerbot.game.api.wrappers.interactive.NPC;
    import org.powerbot.game.api.wrappers.node.GroundItem;
    import org.powerbot.game.api.wrappers.node.Item;
    import org.powerbot.game.api.wrappers.node.SceneObject;
    import org.powerbot.game.bot.Context;
    import org.powerbot.game.client.CombatStatus;
    import org.powerbot.game.client.CombatStatusData;
    import org.powerbot.game.client.LinkedList;
    import org.powerbot.game.client.LinkedListNode;
    import org.powerbot.game.client.RSCharacter;
     
     
     
     
     
    //Blue Dragon Killer, property of Dad from Powerbot
     
    @Manifest(authors = "Dad", name = "Taverly Blue Dragon Killer", description = "Kills Blue Dragons, Loots Bones and hides", version = 1.0)
    public class BlueDragonKiller extends ActiveScript implements PaintListener{
     
            public Node[] array = new Node[] {};
     
            @Override
            public int loop() {
                    for (final Node node : array) {
                            if (node.activate()) {
                                    node.execute();
                            }
                    }
                    return 50;
            }
           
           
           
           
           
           
           
     
            public void onStart() {
                    startTime = System.currentTimeMillis();
                    try {
                            hPrice = getPrice(dragonHide);
                    } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
                    try {
                            dPrice = getPrice(dragonBones);
                    } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                    }
     
                    g.setVisible(true);
            }
     
            public static CombatStatusData getAdrenalineBar(final RSCharacter accessor) {
                    LinkedListNode sentinel = (LinkedListNode) ((LinkedList) accessor
                                    .getCombatStatusList()).getTail();
                    LinkedListNode current = (LinkedListNode) sentinel.getNext();
                    if (!sentinel.equals(current) && !sentinel.equals(current.getNext())) {
                            sentinel = ((LinkedListNode) ((LinkedList) ((CombatStatus) current)
                                            .getData()).getTail());
                            if (!sentinel.equals(sentinel.getNext())) {
                                    final CombatStatusData adrenalineBar = (CombatStatusData) sentinel
                                                    .getNext();
                                    if (adrenalineBar != null) {
                                            return adrenalineBar;
                                    }
                            }
                    }
                    return null;
            }
     
            public static CombatStatusData getHealthBar(final RSCharacter accessor) {
                    LinkedListNode sentinel = (LinkedListNode) ((LinkedList) accessor
                                    .getCombatStatusList()).getTail();
                    LinkedListNode current = (LinkedListNode) sentinel.getNext();
                    if (!sentinel.equals(current)) {
                            if (!sentinel.equals(current.getNext())) {
                                    current = (LinkedListNode) current.getNext();
                            }
                            sentinel = ((LinkedListNode) ((LinkedList) ((CombatStatus) current)
                                            .getData()).getTail());
                            if (!sentinel.equals(sentinel.getNext())) {
                                    final CombatStatusData healthBar = (CombatStatusData) sentinel
                                                    .getNext();
                                    if (healthBar != null) {
                                            return healthBar;
                                    }
                            }
                    }
                    return null;
            }
     
            public static int getAdrenalinePercent(final RSCharacter accessor) {
                    final CombatStatusData adrenalineBar = getAdrenalineBar(accessor);
                    return adrenalineBar != null ? toPercent(adrenalineBar.getHPRatio()
                                    * Context.multipliers().CHARACTER_HPRATIO) : 0;
            }
     
            public static int getHealthPercent(final RSCharacter accessor) {
                    final CombatStatusData healthBar = getHealthBar(accessor);
                    return healthBar != null ? toPercent(healthBar.getHPRatio()
                                    * Context.multipliers().CHARACTER_HPRATIO) : 100;
            }
     
            public static int toPercent(final int ratio) {
                    return (int) Math.ceil((ratio * 100) / 0xFF);
            }
     
            public static int getPrice(int id) throws IOException {
                    URL url = new URL("http://open.tip.it/json/ge_single_item?item=" + id);
                    URLConnection con = url.openConnection();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                    con.getInputStream()));
                    String line = "";
                    String inputLine;
     
                    while ((inputLine = in.readLine()) != null) {
                            line += inputLine;
                    }
     
                    in.close();
     
                    if (!line.contains("mark_price")) {
                            return -1;
                    }
     
                    line = line.substring(line.indexOf("mark_price\":\"")
                                    + "mark_price\":\"".length());
                    line = line.substring(0, line.indexOf("\""));
                    line = line.replaceAll(",", "");
                    return Integer.parseInt(line);
            }
     
            gui g = new gui();
            public final String name = Environment.getDisplayName();
            public String status = "Starting up...";
            public static int numOfFood;
            public static int hPrice;
            public static int dPrice;
            public long startTime;
            public int Profit = 0;
            public int lootItems[] = { 1751, 536 };
            public int ladder = 66991;
            public int ShortCut = 9293;
            public int faladorTab = 8009;
            public int dragonID[] = { 4683, 55, 4682 };
            public int dragonBones = 536;
            public int dragonHide = 1751;
            public static int foodID;
            public int bankerID = 6200;
            public int wallID = 11844;
            public int antiFire4 = 2452;
            public int antiFires[] = { 2452, 2454, 2456, 2458 };
            public Area Fally_Area = new Area(new Tile[] { new Tile(2943, 3399, 0),
                            new Tile(3007, 3398, 0), new Tile(3007, 3322, 0),
                            new Tile(2931, 3324, 0) });
     
            public Tile[] pathToBank = new Tile[] { new Tile(2965, 3381, 0),
                            new Tile(2960, 3380, 0), new Tile(2954, 3381, 0),
                            new Tile(2949, 3378, 0), new Tile(2947, 3374, 0),
                            new Tile(2944, 3369, 0) };
     
            public Tile[] pathToLadder = new Tile[] { new Tile(2945, 3370, 0),
                            new Tile(2944, 3375, 0), new Tile(2940, 3374, 0),
                            new Tile(2938, 3369, 0), new Tile(2940, 3361, 0),
                            new Tile(2937, 3356, 0), new Tile(2932, 3355, 0),
                            new Tile(2930, 3358, 0), new Tile(2929, 3364, 0),
                            new Tile(2929, 3367, 0), new Tile(2925, 3371, 0),
                            new Tile(2916, 3373, 0), new Tile(2909, 3373, 0),
                            new Tile(2900, 3380, 0), new Tile(2892, 3386, 0),
                            new Tile(2887, 3392, 0), new Tile(2886, 3393, 0) };
     
            public boolean inFally() {
                    return Fally_Area.contains(Players.getLocal().getLocation());
            }
     
            public Area Bank_Area = new Area(new Tile[] { new Tile(2941, 3368, 0),
                            new Tile(2941, 3374, 0), new Tile(2947, 3374, 0),
                            new Tile(2947, 3367, 0), new Tile(2948, 3367, 0),
                            new Tile(2948, 3358, 0), new Tile(2943, 3358, 0) });
     
            public boolean useAntiFire;
     
            public boolean inCave() {
                    return Players.getLocal().getLocation().getY() >= 7000;
            }
     
            public boolean attackingDragon() {
                    return Players.getLocal().getInteracting() != null;
            }
     
            public boolean isAntifired() {
                    return Settings.get(1299) > 3;
            }
     
            public boolean atDragons() {
                    return pastPipe() && inCave();
            }
     
            public boolean pastPipe() {
                    return Players.getLocal().getLocation().getX() >= 2889;
            }
     
            public boolean pastWall() {
                    return Players.getLocal().getLocation().getX() <= 2935;
            }
     
            public boolean atBank() {
                    return Bank_Area.contains(Players.getLocal().getLocation());
            }
     
            public void DrawBounds(Graphics2D g, Tile tile, Color col) {
                    top: for (Polygon poly : tile.getBounds()) {
                            for (int i = 0; i < poly.npoints; i++)
                                    if (!Calculations.isOnScreen(new Point(poly.xpoints[i],
                                                    poly.ypoints[i])))
                                            continue top;
                            g.setColor(new Color(col.getRed(), col.getGreen(), col.getBlue(),
                                            80));
                            g.fillPolygon(poly);
                            g.setColor(col);
                            g.drawPolygon(poly);
                    }
            }
           
           
            public class DrinkAnti extends Node {
     
                    @Override
                    public boolean activate() {
                            return !isAntifired() && atDragons() && useAntiFire;
                    }
     
                    @Override
                    public void execute() {
                            Item pot = Inventory.getItem(antiFires);
                            if (pot != null) {
                                    status = "Drinking Antifire...";
                                    pot.getWidgetChild().interact("Drink");
                                    Task.sleep(1500);
                            }
     
                    }
     
            }
     
            public class BankC extends Node {
     
                    @Override
                    public boolean activate() {
                            return (Inventory.isFull() && atBank())
                                            || (Inventory.getCount(foodID) == 0 && atBank());
                    }
     
                    @Override
                    public void execute() {
                            if (Bank.isOpen()) {
                                    status = "Depositing...";
                                    Bank.depositInventory();
                                    Task.sleep(1500);
                                    if (useAntiFire) {
                                            status = "Withdrawing Antifire...";
                                            Bank.withdraw(antiFire4, 1);
                                            Task.sleep(1500);
                                    }
                                    status = "Withdrawing Teleport...";
                                    Bank.withdraw(faladorTab, 2);
                                    Task.sleep(1000);
                                    status = "Withdrawing food...";
                                    Bank.withdraw(foodID, numOfFood);
                                    Task.sleep(1500);
     
                            } else {
                                    NPC bank = NPCs.getNearest(bankerID);
                                    if (bank != null) {
                                            if (bank.isOnScreen()) {
                                                    status = "Opening Bank...";
                                                    Camera.turnTo(bank);
                                                    bank.interact("Bank");
                                                    Task.sleep(1500);
                                            } else {
                                                    status = "Finding Bank...";
                                                    Camera.turnTo(bank);
                                                    Walking.walk(bank);
                                            }
                                    }
                            }
     
                    }
     
            }
     
            public class WalkToBank extends Node {
     
                    @Override
                    public boolean activate() {
                            return inFally() && !atBank() && Inventory.getCount(foodID) == 0;
                    }
     
                    @Override
                    public void execute() {
                            status = "Walking To Bank...";
                            Walking.newTilePath(pathToBank).traverse();
     
                    }
     
            }
     
            public class HopWall extends Node {
     
                    @Override
                    public boolean activate() {
                            SceneObject wall = SceneEntities.getNearest(wallID);
                            return Inventory.getCount(foodID) != 0 && wall != null
                                            && Calculations.distanceTo(wall) < 8 && !pastWall();
                    }
     
                    @Override
                    public void execute() {
                            SceneObject wall = SceneEntities.getNearest(wallID);
                            if (wall != null) {
                                    status = "Climbing Wall...";
                                    Camera.turnTo(wall);
                                    wall.interact("Climb-over");
                                    Task.sleep(3500);
                            }
     
                    }
     
            }
     
            public class ShortCut extends Node {
     
                    @Override
                    public boolean activate() {
                            SceneObject pipe = SceneEntities.getNearest(ShortCut);
                            return pipe != null && !pastPipe();
                    }
     
                    @Override
                    public void execute() {
                            SceneObject pipe = SceneEntities.getNearest(ShortCut);
                            if (pipe != null) {
                                    status = "Shortcut...";
                                    Camera.turnTo(pipe);
                                    pipe.interact("Squeeze-through");
                                    Task.sleep(3500);
                            }
     
                    }
     
            }
     
            public class AttackDragon extends Node {
     
                    @Override
                    public boolean activate() {
                            GroundItem gi = GroundItems.getNearest(lootItems);
                            NPC dragon = NPCs.getNearest(dragonID);
                            return gi == null && dragon != null && !attackingDragon()
                                            && atDragons() && Inventory.getCount(foodID) != 0;
                    }
     
                    @Override
                    public void execute() {
                            NPC dragon = NPCs.getNearest(dragonID);
                            if (dragon != null) {
                                    if (!dragon.isInCombat()) {
                                            if (dragon.isOnScreen()) {
                                                    status = "Attacking Dragon...";
                                                    Camera.turnTo(dragon);
                                                    dragon.interact("Attack");
                                            } else {
                                                    status = "Finding Dragon...";
                                                    Walking.walk(dragon);
                                                    Camera.turnTo(dragon);
                                            }
                                    }
                            }
     
                    }
     
            }
     
            public class ActivateAbility extends Node {
     
                    @Override
                    public boolean activate() {
                            RSCharacter player = Players.getLocal().get();
                            return attackingDragon() && getHealthPercent(player) > 65
                                            && Inventory.getCount(foodID) != 0;
                    }
     
                    @Override
                    public void execute() {
                            status = "In Combat...";
                            RSCharacter player = Players.getLocal().get();
                            if (getAdrenalinePercent(player) >= 100) {
                                    Keyboard.sendKey((char) KeyEvent.VK_4);
                                    Task.sleep(1500);
                            } else {
                                    Keyboard.sendKey((char) KeyEvent.VK_1);
                                    Task.sleep(1500);
                                    Keyboard.sendKey((char) KeyEvent.VK_2);
                                    Task.sleep(1500);
                                    Keyboard.sendKey((char) KeyEvent.VK_3);
                                    Task.sleep(3500);
     
                            }
                    }
     
            }
     
            public class EatFood extends Node {
     
                    @Override
                    public boolean activate() {
                            RSCharacter player = Players.getLocal().get();
                            return getHealthPercent(player) <= 65
                                            && Inventory.getCount(foodID) != 0;
                    }
     
                    @Override
                    public void execute() {
                            status = "Eating Food...";
                            Item food = Inventory.getItem(foodID);
                            if (food != null) {
                                    food.getWidgetChild().interact("Eat");
                                    Task.sleep(1250);
                            }
     
                    }
     
            }
     
            public class TeleportOut extends Node {
     
                    @Override
                    public boolean activate() {
                            return !inFally() && Inventory.getCount(foodID) == 0;
                    }
     
                    @Override
                    public void execute() {
                            Item tab = Inventory.getItem(faladorTab);
                            if (tab != null) {
                                    status = "Teleporting Out...";
                                    tab.getWidgetChild().interact("Break");
                                    Task.sleep(1250);
                            }
     
                    }
     
            }
     
            public class WalkToLadder extends Node {
     
                    @Override
                    public boolean activate() {
                            return Inventory.getCount(foodID) != 0 && !atDragons();
                    }
     
                    @Override
                    public void execute() {
                            status = "Walking TO Dragons";
                            Walking.newTilePath(pathToLadder).traverse();
     
                    }
     
            }
     
            public class EnterCave extends Node {
     
                    @Override
                    public boolean activate() {
                            SceneObject lad = SceneEntities.getNearest(ladder);
                            return lad != null && Calculations.distanceTo(lad) <= 8;
                    }
     
                    @Override
                    public void execute() {
                            SceneObject lad = SceneEntities.getNearest(ladder);
                            if (lad != null) {
                                    status = "Entering Cave...";
                                    Camera.turnTo(lad);
                                    lad.interact("Climb-down");
                                    Task.sleep(2000);
                            }
     
                    }
     
            }
     
            public class LootBones extends Node {
     
                    @Override
                    public boolean activate() {
                            GroundItem gi = GroundItems.getNearest(dragonBones);
                            return gi != null && Calculations.distanceTo(gi) <= 15;
                    }
     
                    @Override
                    public void execute() {
                            GroundItem gi = GroundItems.getNearest(dragonBones);
                            if (gi != null) {
                                    if (Inventory.isFull() && Inventory.getCount(foodID) != 0) {
                                            status = "Eating For Room...";
                                            Item food = Inventory.getItem(foodID);
                                            food.getWidgetChild().interact("Eat");
                                            Task.sleep(1000);
                                    } else if (!Inventory.isFull()) {
                                            if (gi.isOnScreen()) {
                                                    status = "Taking Bones...";
                                                    Camera.turnTo(gi);
                                                    gi.interact("Take", "Dragon bones");
                                                    Task.sleep(2000);
                                                    Profit += dPrice;
                                            } else {
                                                    status = "Finding Bones...";
                                                    Walking.walk(gi);
                                                    Camera.turnTo(gi);
                                            }
     
                                    }
     
                            }
     
                    }
            }
     
            public class LootHides extends Node {
     
                    @Override
                    public boolean activate() {
                            GroundItem gi = GroundItems.getNearest(dragonHide);
                            return gi != null && Calculations.distanceTo(gi) <= 15;
                    }
     
                    @Override
                    public void execute() {
                            GroundItem gi = GroundItems.getNearest(dragonHide);
                            if (gi != null) {
                                    if (Inventory.isFull() && Inventory.getCount(foodID) != 0) {
                                            status = "Eating For Room...";
                                            Item food = Inventory.getItem(foodID);
                                            food.getWidgetChild().interact("Eat");
                                            Task.sleep(1000);
                                    } else if (!Inventory.isFull()) {
                                            if (gi.isOnScreen()) {
                                                    status = "Taking Hide...";
                                                    Camera.turnTo(gi);
                                                    gi.interact("Take", "Blue dragonhide");
                                                    Task.sleep(2000);
                                                    Profit += hPrice;
                                            } else {
                                                    status = "Finding Hide...";
                                                    Walking.walk(gi);
                                                    Camera.turnTo(gi);
                                            }
     
                                    }
     
                            }
     
                    }
            }
     
           
            public class gui extends JFrame {
                    public gui() {
                            initComponents();
                    }
     
                    private void goButtonActionPerformed(ActionEvent e) {
                           
                            String num = numBox.getSelectedItem().toString();
                            if(num.equals("1")){
                                    numOfFood = 1;
                            }else if(num.equals("2")){
                                    numOfFood = 2;
                            }else if(num.equals("3")){
                                    numOfFood = 3;
                            }else if(num.equals("4")){
                                    numOfFood = 4;
                            }else if(num.equals("5")){
                                    numOfFood = 5;
                            }else if(num.equals("6")){
                                    numOfFood = 6;
                            }else if(num.equals("7")){
                                    numOfFood = 7;
                            }else if(num.equals("8")){
                                    numOfFood = 8;
                            }else if(num.equals("9")){
                                    numOfFood = 9;
                            }else if(num.equals("10")){
                                    numOfFood = 10;
                            }else if(num.equals("11")){
                                    numOfFood = 11;
                            }else if(num.equals("12")){
                                    numOfFood = 12;
                            }else if(num.equals("13")){
                                    numOfFood = 13;
                            }else if(num.equals("14")){
                                    numOfFood = 14;
                            }else if(num.equals("15")){
                                    numOfFood = 15;
                            }else if(num.equals("16")){
                                    numOfFood = 16;
                            }else if(num.equals("17")){
                                    numOfFood = 17;
                            }else if(num.equals("18")){
                                    numOfFood = 18;
                            }else if(num.equals("19")){
                                    numOfFood = 19;
                            }else if(num.equals("20")){
                                    numOfFood = 20;
                            }else if(num.equals("21")){
                                    numOfFood = 21;
                            }else if(num.equals("22")){
                                    numOfFood = 22;
                            }else if(num.equals("23")){
                                    numOfFood = 23;
                            }else if(num.equals("24")){
                                    numOfFood = 24;
                            }else if(num.equals("25")){
                                    numOfFood = 25;
                            }else if(num.equals("26")){
                                    numOfFood = 26;
                            }else if(num.equals("27")){
                                    numOfFood = 27;
                            }
                           
                           
                            String food = foodBox.getSelectedItem().toString();
                            if (food.equals("Lobster")) {
                                    foodID = 379;
                            } else if (food.equals("Swordfish")) {
                                    foodID = 373;
                            } else if (food.equals("Monkfish")) {
                                    foodID = 7946;
                            } else if (food.equals("Shark")) {
                                    foodID = 385;
                            } else {
                                    foodID = 15272;
                            }
     
                            if (checkBox1.isSelected()) {
                                    useAntiFire = true;
                            } else {
                                    useAntiFire = false;
                            }
     
                            array = new Node[] { new DrinkAnti(), new BankC(),
                                            new WalkToBank(), new WalkToLadder(), new HopWall(),
                                            new EnterCave(), new ShortCut(), new AttackDragon(),
                                            new ActivateAbility(), new EatFood(), new TeleportOut(),
                                            new LootBones(), new LootHides() };
     
                           
                            g.dispose();
     
                    }
     
                    private void initComponents() {
                            // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
                            // Generated using JFormDesigner Evaluation license - mark colby
                            label1 = new JLabel();
                            label2 = new JLabel();
                            label3 = new JLabel();
                            foodBox = new JComboBox<>();
                            checkBox1 = new JCheckBox();
                            label4 = new JLabel();
                            goButton = new JButton();
                            numBox = new JComboBox<>();
     
                            //======== this ========
                            Container contentPane = getContentPane();
     
                            //---- label1 ----
                            label1.setText("wBlue Dragons");
                            label1.setFont(label1.getFont().deriveFont(label1.getFont().getSize() + 17f));
                            label1.setForeground(Color.blue);
     
                            //---- label2 ----
                            label2.setText("by Dad");
                            label2.setFont(label2.getFont().deriveFont(label2.getFont().getStyle() | Font.BOLD));
     
                            //---- label3 ----
                            label3.setText("Food: ");
                            label3.setFont(label3.getFont().deriveFont(label3.getFont().getSize() + 4f));
     
                            //---- foodBox ----
                            foodBox.setModel(new DefaultComboBoxModel<>(new String[] {
                                    "Lobster",
                                    "Swordfish",
                                    "Monkfish",
                                    "Shark",
                                    "Rocktail"
                            }));
     
                            //---- checkBox1 ----
                            checkBox1.setText("Use Antifire");
     
                            //---- label4 ----
                            label4.setText("Number of Food: ");
     
                            //---- goButton ----
                            goButton.setText("text");
                            goButton.addActionListener(new ActionListener() {
                                    @Override
                                    public void actionPerformed(ActionEvent e) {
                                            goButtonActionPerformed(e);
                                    }
                            });
     
                            //---- numBox ----
                            numBox.setModel(new DefaultComboBoxModel<>(new String[] {
                                    "1",
                                    "2",
                                    "3",
                                    "4",
                                    "5",
                                    "6",
                                    "7",
                                    "8",
                                    "9",
                                    "10",
                                    "11",
                                    "12",
                                    "13",
                                    "14",
                                    "15",
                                    "16",
                                    "17",
                                    "18",
                                    "19",
                                    "20",
                                    "21",
                                    "22",
                                    "23",
                                    "24",
                                    "25",
                                    "26",
                                    "27"
                            }));
     
                            GroupLayout contentPaneLayout = new GroupLayout(contentPane);
                            contentPane.setLayout(contentPaneLayout);
                            contentPaneLayout.setHorizontalGroup(
                                    contentPaneLayout.createParallelGroup()
                                            .addComponent(checkBox1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                                    .addContainerGap(99, Short.MAX_VALUE)
                                                    .addComponent(goButton)
                                                    .addGap(88, 88, 88))
                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                    .addGroup(contentPaneLayout.createParallelGroup()
                                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                                    .addContainerGap()
                                                                    .addGroup(contentPaneLayout.createParallelGroup()
                                                                            .addComponent(label1, GroupLayout.PREFERRED_SIZE, 230, GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(label2, GroupLayout.PREFERRED_SIZE, 117, GroupLayout.PREFERRED_SIZE)))
                                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                                                    .addComponent(label3, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
                                                                                    .addGap(41, 41, 41))
                                                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                                                    .addComponent(label4, GroupLayout.DEFAULT_SIZE, 94, Short.MAX_VALUE)
                                                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)))
                                                                    .addGroup(contentPaneLayout.createParallelGroup()
                                                                            .addComponent(foodBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                                            .addComponent(numBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                                                    .addGap(0, 0, Short.MAX_VALUE))
                            );
                            contentPaneLayout.setVerticalGroup(
                                    contentPaneLayout.createParallelGroup()
                                            .addGroup(contentPaneLayout.createSequentialGroup()
                                                    .addContainerGap()
                                                    .addComponent(label1, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(label2, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(34, 34, 34)
                                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label3)
                                                            .addComponent(foodBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addGap(36, 36, 36)
                                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                            .addComponent(label4)
                                                            .addComponent(numBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                                    .addGap(18, 18, 18)
                                                    .addComponent(checkBox1)
                                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(goButton)
                                                    .addContainerGap(8, Short.MAX_VALUE))
                            );
                            pack();
                            setLocationRelativeTo(getOwner());
                            // JFormDesigner - End of component initialization  //GEN-END:initComponents
                    }
     
                    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
                    // Generated using JFormDesigner Evaluation license - mark colby
                    private JLabel label1;
                    private JLabel label2;
                    private JLabel label3;
                    private JComboBox<String> foodBox;
                    private JCheckBox checkBox1;
                    private JLabel label4;
                    private JButton goButton;
                    private JComboBox<String> numBox;
                    // JFormDesigner - End of variables declaration  //GEN-END:variables
            }
     
           
           
       
        private final Color color1 = new Color(0, 0, 255);
        private final Color color2 = new Color(0, 0, 0);
        private final Color color3 = new Color(255, 255, 255);
     
        private final BasicStroke stroke1 = new BasicStroke(1);
     
        private final Font font1 = new Font("Arial Black", 3, 32);
        private final Font font2 = new Font("Arial Black", 1, 18);
     
        @Override
        public void onRepaint(Graphics g1) {
           
            long millis = System.currentTimeMillis() - startTime;
                    long hours = millis / (1000 * 60 * 60);
                    millis -= hours * (1000 * 60 * 60);
                    long minutes = millis / (1000 * 60);
                    millis -= minutes * (1000 * 60);
                    long seconds = millis / 1000;
           
                    int profitPerHour = (int) ((Profit) * 3600000D / (System
                                    .currentTimeMillis() - startTime));
           
            Graphics2D g = (Graphics2D)g1;
            g.setColor(Color.WHITE);
                    g.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND,
                                    BasicStroke.JOIN_ROUND));
                    g.drawLine(0, Mouse.getY(), Game.getDimensions().width, Mouse.getY());
                    g.drawLine(Mouse.getX(), 0, Mouse.getX(), Game.getDimensions().height);
            g.setColor(color1);
            g.fillRect(4, 394, 512, 135);
            g.setColor(color2);
            g.setStroke(stroke1);
            g.drawRect(4, 394, 512, 135);
            g.setFont(font1);
            g.setColor(color1);
            g.drawString("Blue Dragon Killer", 84, 388);
            g.setFont(font2);
            g.setColor(color3);
            g.drawString("Status: " + status, 10, 516);
            g.drawString("Profit (per hour): " + Profit + "(" + profitPerHour + ")", 10, 473);
            g.drawString("RunTime: " + hours + ":" + minutes +":" + seconds, 10, 429);
       
            if (NPCs.getNearest(dragonID) != null ) {
                            DrawBounds(g, NPCs.getNearest(dragonID).getLocation(),
                                            Color.blue);
                            Point p = NPCs.getNearest(dragonID).getLocation()
                                            .getMapPoint();
                            g.setRenderingHints(new RenderingHints(
                                            RenderingHints.KEY_ANTIALIASING,
                                            RenderingHints.VALUE_ANTIALIAS_ON));
                            g.setColor(Color.blue);
                            g.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_ROUND,
                                            BasicStroke.JOIN_ROUND));
                            g.fillOval(p.x - 2, p.y - 2, 4, 4);
                    }
       
       
       
       
       
        }
       
           
           
           
           
           
           
    }

