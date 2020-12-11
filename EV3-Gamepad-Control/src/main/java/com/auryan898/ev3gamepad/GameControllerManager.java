package com.auryan898.ev3gamepad;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.auryan898.ev3gamepad.keymapping.BaseGamepadKeyMapper;
import com.auryan898.ev3gamepad.keymapping.DefaultKeyMapper;
import com.auryan898.ev3gamepad.CustomEV3GamepadLoader;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class GameControllerManager implements Runnable {
  static final CustomEV3GamepadLoader loader = new CustomEV3GamepadLoader(
      ControllerEnvironment.getDefaultEnvironment().getClass().getClassLoader());

  static {
    System.setProperty("net.java.games.input.librarypath", loader.getTempDir());
  }

  public static void main(String[] args) {
    GameControllerManager manager = GameControllerManager.getInstance();
    GameControllerDisplay d = new GameControllerDisplay();

    d.create();
    d.show();

    String[] assigningKeys = {
        "a_button",
        "b_button",
        "x_button",
        "y_button",
        "left_button",
        "right_button" };
    List<PhysicalGameController> conts = manager.getPhysicalControllers();
    for (int i = 0; i < conts.size(); i++) {
      d.addController(conts.get(i));
    }
    for (int i = 0; i < assigningKeys.length; i++) {
      if (i < assigningKeys.length)
        d.addController(
            manager.getAssignableController(new BaseGamepadKeyMapper(), "start", assigningKeys[i]));
    }

    while (true) {
      d.update();
      manager.update();
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
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

  public List<PhysicalGameController> getPhysicalControllers() {
    return Collections.unmodifiableList(physicalControllers);
  }

  /**
   * Gets an assigned controller by it's determined key.
   * 
   * @param  i
   * @return
   */
  public AssignableGameController getAssignableController(KeyMapper mapper, String... idKeys) {
    String id = mapper.concatenateKeys(idKeys);
    if (assignedControllers.containsKey(id)) {
      return assignedControllers.get(id);
    }

    AssignableGameController a = new AssignableGameController(mapper, idKeys);
    assignedControllers.put(id, a);
    return a;
  }

  public AssignableGameController getAssignableController(String... idKeys) {
    return getAssignableController(this.mainMapper, idKeys);
  }

  public List<AssignableGameController> getAssignableControllers() {
    return Collections.unmodifiableList(new ArrayList<>(assignedControllers.values()));
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
      String signature = mainMapper.concatenateKeys(pController.getButtonSignature());
      if (assignedControllers.containsKey(signature)) {
        AssignableGameController a = assignedControllers.get(signature);
        if (pController.getIdKey() != null)
          assignedControllers.get(pController.getIdKey()).unassign();
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
