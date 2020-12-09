package com.auryan898.ev3gamepad;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class GameControllerManager implements Runnable {
  private static GameControllerManager instance;

  public GameControllerManager getInstance() {
    if (instance == null)
      instance = new GameControllerManager();

    return instance;
  }

  private GameControllerManager() {
    loadPhysicalControllers();
    update();
  }

  private static Long UPDATE_INTERVAL = 20L;
  private List<PhysicalGameController> physicalControllers = Collections
      .synchronizedList(new ArrayList<PhysicalGameController>());
  private ArrayList<String> players = new ArrayList<>();
  private ConcurrentHashMap<String, AssignableGameController> assignedControllers = new ConcurrentHashMap<>();
  private ArrayList<Controller> baseGamepads = new ArrayList<>();

  public Long setUpdateIntervalMilliseconds(long interval) {
    UPDATE_INTERVAL = Math.max(1L, interval);
    return UPDATE_INTERVAL;
  }

  // Method to be run in thread
  @Override
  public void run() {
    while (true) {
      // TODO Include periodic update methods here
      update();
      try {
        Thread.sleep(UPDATE_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }
  }

  /**
   * Gets a physical controller from the internal list, which has an unkown order.
   * 
   * @param  i index of the physical controller
   * @return
   */
  public PhysicalGameController getPhysicalController(int i) {
    return physicalControllers.get(i);
  }

  /**
   * Gets an assigned controller by it's determined key.
   * 
   * @param  i
   * @return
   */
  public AssignableGameController getAssignableController(String key) {
    return assignedControllers.get(key);
  }
  
  /**
   * 
   * 
   * @param key
   * @return
   */
  public AssignableGameController createAssignableControlller(String key) { // TODO: Possible improvements, gamepadType, isValid key
    AssignableGameController a = new AssignableGameController(physicalControllers);
    assignedControllers.put(key, a);
    return a;
  }

  protected void loadPhysicalControllers() {
    Controller[] baseControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
    this.baseGamepads = new ArrayList<>();
    for (Controller cont : baseControllers) {
      if (cont.getType() == Controller.Type.GAMEPAD) {
        baseGamepads.add(cont);
        this.physicalControllers.add(new PhysicalGameController(cont));
      }
    }
  }

  protected void update() {
    updatePhysicalControllers();
    updateAssignedControllers();
  }

  protected void updatePhysicalControllers() {
    synchronized (physicalControllers) {
      for (PhysicalGameController p : physicalControllers) {
        p.update();
      }
    }
  }

  protected void updateAssignedControllers() {
    synchronized (assignedControllers) {
      for (Entry<String, AssignableGameController> a : assignedControllers.entrySet()) {
        a.getValue().update();
      }
    }
  }
}
