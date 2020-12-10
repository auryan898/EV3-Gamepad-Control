package com.auryan898.ev3gamepad;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.auryan898.ev3gamepad.keymapping.DefaultKeyMapper;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class GameControllerManager implements Runnable {
  
  public static void main(String[] args) {
    GameControllerManager manager = GameControllerManager.getInstance();
    PhysicalGameController p = manager.getPhysicalController(0);
    GameControllerDisplay d = new GameControllerDisplay();
    d.create();
    d.addController(manager.getPhysicalController(0));
    d.show();
    while (true) {
      d.update();
      p.update();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    /*
    manager.createAssignableControlller("Button 9");
    
    
    while (true) {
      // TODO Include periodic update
      manager.update();
      d.update();
      try {
        Thread.sleep(UPDATE_INTERVAL);
      } catch (InterruptedException e) {
        e.printStackTrace();
        break;
      }
    }*/
  }
  
  private static GameControllerManager instance;
  private ReentrantLock lock = new ReentrantLock();
  private KeyMapper mainMapper = new DefaultKeyMapper();

  public static GameControllerManager getInstance() {
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

  /**
   * Sets interval for update thread to be at least 1 millisecond no matter what.
   * 
   * @param  interval
   * @return
   */
  public Long setUpdateIntervalMilliseconds(long interval) {
    UPDATE_INTERVAL = Math.max(1L, interval);
    return UPDATE_INTERVAL;
  }

  // Method to be run in thread
  @Override
  public void run() {
    while (true) {
      // TODO Include periodic update
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
  public AssignableGameController getAssignableController(KeyMapper mapper, String... idKeys) {
    return assignedControllers.get(mapper.concatenateKeys(idKeys));
  }
  
  public AssignableGameController getAssignableController(String... idKeys) {
    return assignedControllers.get(this.mainMapper.concatenateKeys(idKeys));
  }

  /**
   * 
   * 
   * @param  key
   * @return
   */
  public AssignableGameController createAssignableControlller(String key) {
    // TODO: Possible improvements, gamepadType, isValid key
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
    lock.lock();
    for (PhysicalGameController p : physicalControllers) {
      p.update();
    }
    lock.unlock();
  }

  protected void updateAssignedControllers() {
    lock.lock();
    ArrayList<String[]> signatures = new ArrayList<>();

    for (PhysicalGameController pController : physicalControllers) {
      String signature = mainMapper.concatenateKeys(pController.getKeySignature());
      if (assignedControllers.containsKey(signature)) {
        AssignableGameController a = assignedControllers.get(signature);
        a.unassign();
        a.assign(pController);
      }
    }
    lock.unlock();
  }

  public boolean unassign(PhysicalGameController pController) {
    String key = pController.getIdKey();
    if (assignedControllers.containsKey(key)) {
      return assignedControllers.get(key).unassign();
    }
    return false;
  }

  public boolean assign(PhysicalGameController pController, AssignableGameController aController) {
    return aController.assign(pController);
  }
}
