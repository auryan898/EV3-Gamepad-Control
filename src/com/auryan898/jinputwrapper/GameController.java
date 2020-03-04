package com.auryan898.jinputwrapper;

import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.auryan898.jinputwrapper.GameControllerManager.FixedController;

public class GameController {
  private FixedController controller;
  private GameControllerManager manager;
  private String primaryKey;
  private HashMap<String, Float> defaultMap;

  
  /**
   * A testing method to show the hardware-assigned controllers, and
   * their user-assigned 'id' values.
   * @param args arguments
   */
  public static void main(String[] args) {
    GameControllerManager manager = new GameControllerManager();
    Thread t1 = new Thread(manager);
    t1.start();

    JFrame frame = new JFrame();
    frame.setLayout(new GridLayout(8, 1));
    JLabel[] labels = new JLabel[8];
    for (int i = 0; i < labels.length; i++) {
      labels[i] = new JLabel("_");
      frame.add(labels[i]);
    }

    // You need a JFrame to capture the input events for you
    // Just create one anywhere, and click on the window to get it working
    frame.pack();
    frame.setSize(600, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    GameController gamepad1; // The user-assigned controller wrapper
    GameController gamepad2; 
    try {
      gamepad1 = manager.getGameController("a");
      gamepad2 = manager.getGameController("b");
    } catch (Exception e) {
      System.out.println("Oh no the GameController! " + e);
      return;
    }

    while (true) {
      try {
        String[] status = manager.allControllerStatus();
        int cutoff = 146;
        for (int i = 0; i < labels.length && i < status.length; i += 2) {
          String str = "Controller #" + i + " \n->" + status[i].substring(0, cutoff);

          labels[i].setText(str);
          labels[i + 1].setText(status[i].substring(cutoff));
        }
        frame.pack();
        frame.update(frame.getGraphics());
        Thread.sleep(100);
      } catch (Exception e) {
        System.out.println("Exiting Program: " + e);
        break;
      }
    }
  }

  /**
   * Do not instantiate this class directly, instantiate GameControllerManager and
   * use `getGameController(String primaryKey)` to get instances of this class.
   * May also use `getRawController(int index)` with no player assigning
   * capability.
   * 
   * @param controller an originally hardware assigned controller
   */
  public GameController(GameControllerManager manager, FixedController controller) {
    this.controller = controller;
    this.manager = null;
    this.primaryKey = null;
    this.defaultMap = manager.getDefaultMap();
  }

  /**
   * Do not instantiate this class directly, instantiate GameControllerManager and
   * use `getGameController(String primaryKey)` to get instances of this class.
   * May also use `getRawController(int index)` with no player assigning
   * capability.
   * 
   * @param manager - the controller manager that manages the original thread
   * @param primaryKey - the gamepad button name that is used to assign the controller
   */
  public GameController(GameControllerManager manager, String primaryKey) {
    this.manager = manager;
    this.primaryKey = primaryKey;
    this.defaultMap = manager.getDefaultMap();
  }

  /**
   * isAssigned 
   * if this instance has been assigned a physical controller, then
   * returns true.
   * 
   * @return true if this instance is usable
   */
  public boolean isAssigned() {
    return this.controller != null;
  }

  /**
   * get Retrieves the value of a button on the game controller assigned to this
   * instance. The values will be up to date as long as the GameControllerManager
   * instance's separate thread is still running without exceptions
   * 
   * @param key - the name of the button/stick that you want the value of
   * @return a float value between -1.0 and 1.0 with a deadzone of +/- 0.1
   * @throws Exception - when the key is not right
   */
  public float get(String key) throws Exception {
    if (!(primaryKey == null && controller != null)) {
      controller = manager.getRoleController(primaryKey);
    }
    if (controller == null) {
      return 0;      
    }
    return controller.get(key);
  }

  /**
   * getMap Retrieves a copy of the map of values of the game controller assigned
   * to this instance. The values will be up to date as long as the
   * GameControllerManager instance's separate thread is still running without
   * exceptions
   * 
   * @return a hashmap with float values between -1.0 and 1.0 with a deadzone of +/- 0.1
   * @throws Exception - when the map is empty, or no controller is assigned
   */
  public HashMap<String, Float> getMap() throws Exception {
    if (!(primaryKey == null && controller != null)) {
      controller = manager.getRoleController(primaryKey);
    }

    if (controller == null) {
      return new HashMap<String, Float>();
    }
    HashMap<String, Float> map = controller.getMap();
    return map.size() > 0 ? map : defaultMap;
  }

  public String toString() {
    return this.controller.toString();
  }
}
