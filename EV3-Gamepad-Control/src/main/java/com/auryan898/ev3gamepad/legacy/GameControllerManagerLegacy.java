package com.auryan898.ev3gamepad.legacy;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

/**
 * GameControllerManager This class generates instances of GameController
 * objects and updates those same instances in a separate thread. It allows the
 * creation of assignable GameController objects, as well as GameController
 * objects that just start working, with no assigning necessary.
 * 
 * <p>First steps for every usage: - instantiate this class: `GameControllerManager
 * m = new GameControllerManager();` - then start the manager in a thread:
 * `Thread t = new Thread(m); t.start()'
 * 
 * <p>For assignable GameController objects: - then retrieve GameController objects
 * by: `GameController c = m.getGameController("a");` - where the String input
 * is a button identifier on the controller, that is not "start" - press "start"
 * button and your key (ie. "a") simultaneously to ASSIGN the physical
 * controller to the virtual instance (ie. `c` in this case) - see
 * getGameController javadoc for more info - any number of these instances can
 * be created, regardless of the number of physical controllers, effectively
 * allowing the creation of multiple players - the more players created in this
 * manner, the more overhead it requires
 * 
 * <p>For non-assignable GameController objects: - generate the object by:
 * `GameController c = m.getRawController(0);` where 0 is an index - it will
 * connect directly to a physical game controller, so the relationship is one to
 * one - assigning is not necessary, but an Exception is thrown if the
 * controller cannot be found
 * 
 * @author Ryan Au auryan898@gmail.com
 *
 */
public class GameControllerManagerLegacy implements Runnable {
  /**
   * UPDATE_INTERVAL
   * the milliseconds interval at which the main thread updates gamepads.
   */
  private static final long UPDATE_INTERVAL = 20;
  private Controller[] tempControllers;
  private int numTargetControllers;

  private Controller[] controllers;
  private FixedController[] players;
  private HashMap<String, String> keymap;
  private LinkedHashMap<String, Integer> roleKeys;

  /**
   * GameControllerManager - instantiate this class: `GameControllerManager m =
   * new GameControllerManager();` - then start the manager in a thread: `Thread t
   * = new Thread(m);t.start()' - then retrieve GameController objects by:
   * `GameController c = m.getGameController("a");` - where the String input is a
   * button identifier on the controller, that is not "start" - press "start"
   * button and your key (ie. "a") simultaneously to ASSIGN the physical
   * controller to the virtual instance (ie. `c` in this case)
   * 
   */
  public GameControllerManagerLegacy() {
    initKeymap();
    initControllers();
    initPlayers();

    roleKeys = new LinkedHashMap<String, Integer>();
  }

  /**
   * run implementation of this function for the Runnable interface, so that it
   * can be used in a thread: `new Thread(Runnable m);`.
   * 
   */
  public void run() {
    while (true) {      
      updatePlayers();
      updateRoles();
      
      try {
        Thread.sleep(UPDATE_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }

  }

  /**
   * getRawController Retrieves the controller by an index assigned by the
   * computer upon device connection. Most useful for single player/controller
   * usage.
   * 
   * @param index - the index of the controller in the computer's device list
   * @return either returns a useful GameController instance, or null
   */
  public GameControllerLegacy getRawController(int index) {
    try {
      return new GameControllerLegacy(this, this.players[index]);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  /**
   * getButtonKeys Retrieves the list of acceptable keys to be used by Controllers.
   * 
   * @return array of strings that are used to identify the controller buttons
   */
  public String[] getButtonKeys() {
    return (String[]) keymap.values().toArray();
  }

  /**
   * getDefaultMap
   * Gives a new hashmap instance of the gamepad keys associated with each gamepad.  
   * @return
   */
  public HashMap<String, Float> getDefaultMap() {
    HashMap<String, Float> map = new HashMap<String, Float>();
    for (Map.Entry<String, String> entry : keymap.entrySet()) {
      map.put(entry.getValue(), 0.0f);
    }
    return map;
  }

  private void initKeymap() {

    String[] keys = { "Button 9", "X Axis", "Button 8", "X Rotation", "Button 7", "Button 6", 
        "Button 5","Y Axis","Button 4", "Button 3", "Button 2", "Y Rotation", "Button 1", 
        "Button 0", "Z Axis", "Hat Switch" };
    String[] vals = { "right_stick_button", "left_stick_x", "left_stick_button", 
        "right_stick_x", "start", "back","left_bumper", "left_stick_y", "right_bumper",
        "y", "x", "right_stick_y", "b", "a", "bumpers", "dpad" };
    keymap = new HashMap<String, String>();
    for (int i = 0; i < keys.length && keys.length == vals.length; i++) {
      keymap.put(keys[i], vals[i]);
    }
  }

  private void initControllers() {
    tempControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
    numTargetControllers = 0;
    for (int i = 0; i < tempControllers.length; i++) {
      if (tempControllers[i].getType() == Controller.Type.GAMEPAD) {
        numTargetControllers++;
      }
    }
    controllers = new Controller[numTargetControllers];
    int j = 0;
    for (int i = 0; i < tempControllers.length && j < numTargetControllers; i++) {
      if (tempControllers[i].getType() == Controller.Type.GAMEPAD) {
        controllers[j] = tempControllers[i];
        j++;
      }
    }
  }

  private void initPlayers() {
    players = new FixedController[controllers.length];
    for (int i = 0; i < players.length; i++) {
      players[i] = new FixedController(controllers[i], this.keymap);
    }
  }

  private void updatePlayers() {
    for (int i = 0; i < players.length; i++) {
      players[i].update();
    }
  }
  
  /**
   * getGameController the recommended method of creating GameController
   * instances. Each instance represents a player in a video game that has not yet
   * joined the game. The primaryKey that is given, is the key you use to join
   * (assign the controller).
   * 
   * <p>For example: with two GameController instances with primaryKeys "a" and "b",
   * on the physical controller, press "start" and your primaryKey to be assigned
   * to the instance "a", and to reassign instance (ie. switch players), press
   * "start" and primaryKey "b" to switch to the "b" instance and unassign from
   * the "a" instance.
   * 
   * <p>GameController player1 = manager.getGameController("a"); GameController
   * player2 = manager.getGameController("b"); // player1 presses "start" and "a"
   * to activate the controller // player2 presses "start" and "b" to activate the
   * controller // player1 can switch to player2 by pressing "start" and "b" //
   * neither player will give input until someone activates the // controller
   * first
   * 
   * @param primaryKey - the key to press in combination with "start" to assign
   *                   the controller, and it cannot be "start"
   * @return a useful GameController instance
   * @throws Exception when the primaryKey is not a valid identifier for the
   *                   controller buttons
   */
  public GameControllerLegacy getGameController(String primaryKey) throws Exception {
    GameControllerLegacy res = new GameControllerLegacy(this, primaryKey);
    this.addRoleKey(primaryKey);
    return res;
  }

  private void addRoleKey(String key) throws Exception {
    if (!keymap.containsValue(key) || key.equals("start")) {
      throw new Exception();
    }
    roleKeys.put(key, -1);

  }

  private void updateRoles() {
    // works with players FixedController[], roles FixedController[], and roleKeys
    // HashMap<String>
    for (int i = 0; i < players.length; i++) {
      HashMap<String, Float> m = players[i].getMap();
      int j = 0;
      for (Map.Entry<String, Integer> entry : roleKeys.entrySet()) {
        if (j >= roleKeys.size()) {
          break;
        }
        if (m.get("start") == 1.0 && m.get(entry.getKey()) == 1.0) {
          // Place controller into role, and remove duplicates
          players[i].id = j;
          players[i].setPrimaryKey(entry.getKey());
          roleKeys.put(entry.getKey(), i);
        }
        j++;
      }
      // removes the duplicates, so basically if a controller tries to simultaneously
      // take all places, they will be placed into the first one
      j = 0;
      for (Map.Entry<String, Integer> entry : roleKeys.entrySet()) {
        int val = entry.getValue();
        if (val >= 0 && val < players.length) {
          if (players[val].id != j) {
            roleKeys.put(entry.getKey(), -1);
          }
        }
        j++;
      }
    }

  }

  /**
   * getRoleController An internal function to be used by the GameController class.
   * 
   * @param primaryKey button key that will be used to assign the controller
   * @return
   */
  public FixedController getRoleController(String primaryKey) {
    int index = roleKeys.get(primaryKey);
    if (index >= 0 && index < players.length) {
      return players[index];
    } else {
      return null;
    }
  }

  /**
   * allControllerStatus Gives Strings representing the state of each controller
   * in an order determined by the computer. The state will identify each
   * controller and which primaryKey the physical controller is assigned to.
   * 
   * @return the states of the physical controllers
   */
  public String[] allControllerStatus() {
    String[] result = new String[players.length];
    for (int i = 0; i < result.length; i++) {
      result[i] = players[i].toString();
    }
    return result;
  }

  /**
   * The simplest way to get all of the information of 
   * every hardware-assigned controller.
   */
  public String toString() {
    String result = "";
    for (FixedController f : players) {
      result += f.toString() + "\n";
    }
    return result;
  }

  /**
   * FixedController An internal class that represents 
   * the game controllers originally listed by the computer.
   *
   * <p>Don't go messing with this class.
   * 
   * @author Ryan Au
   *
   */
  class FixedController {
    private String primaryKey = null;
    private int id = -1;
    private HashMap<String, Float> values;
    private HashMap<String, String> keymap;
    private Controller controller;
    private Component[] components;

    private FixedController(Controller controller, HashMap<String, String> keymap) {
      this.controller = controller;
      this.keymap = keymap;
      values = new HashMap<String, Float>();
      for (Map.Entry<String, String> entry : keymap.entrySet()) {
        values.put(entry.getValue(), 0.0f);
      }
    }

    private void setPrimaryKey(String key) {
      primaryKey = key;
    }

    private void update() {
      if (controller != null) {
        controller.poll();
        components = controller.getComponents();
        for (int i = 0; i < components.length; i++) {
          String name = (components[i].getName());
          float val = components[i].getPollData();
          values.put(keymap.containsKey(name) ? keymap.get(name) : name,
              val < 0.1 && val > -0.1 ? 0 : val);
        }

      }
    }

    private float getRaw(String key) throws Exception {
      if (!values.containsKey(key)) {
        throw new Exception();
      }
      return values.get(key);
    }

    public float get(String key) throws Exception {
      return this.getRaw(key);
    }

    public float getPrimaryValue() throws Exception {
      return this.get(primaryKey);
    }

    public HashMap<String, Float> getMap() {
      return (HashMap<String, Float>) this.values.clone();
    }

    public String toString() {
      return "[ id:" + this.id + " primaryKey:" + this.primaryKey + " " 
          + this.getMap().toString() + " ]";
    }
  }
}
