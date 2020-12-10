package com.auryan898.ev3gamepad;

import java.util.*;

import com.auryan898.ev3gamepad.keymapping.DefaultKeyMapper;

public class AssignableGameController extends GameController {

  private PhysicalGameController physicalController;
  private KeyMapper mapper = new DefaultKeyMapper();
  private String idKey;

  AssignableGameController(List<PhysicalGameController> physicalControllers) {
    // TODO Auto-generated constructor stub
  }

  public void setKeyMapper(KeyMapper mapper) {
    this.mapper = mapper; // converts keys
  }

  @Override
  public void update() {
    // TODO implement controller deciding which controller to listen to
  }

  public String saveIdKey(String... idKeys) {
    this.idKey = createIdKey(idKeys);
    return this.idKey;
  }

  public String createIdKey(String... idKeys) {
    return this.mapper.concatenateKeys(idKeys);
  }

  public boolean matchesIdKey(String... testKeys) {
    return this.idKey != null && testKeys != null
        && this.idKey.equals(this.mapper.concatenateKeys(testKeys));
  }

  /**
   * Deselects the game controller that was added to this assigned controller.
   * Returns false if controller is not already set.
   * 
   * @return
   */
  public boolean unassign() {
    if (physicalController == null) {
      return false;
    }

    // unset physicalController's idKey to null
    this.physicalController.setIdKey(null);
    // unset physicalController, by setting it to null here
    this.physicalController = null;

    return true;
  }

  /**
   * Selects the game controller given as the main controller for this wrapper.
   * Returns false if controller is already set.
   * 
   * @param  pController
   * @return
   */
  public boolean assign(PhysicalGameController pController) {
    if (physicalController != null)
      return false;

    // set pController as physicalController
    this.physicalController = pController;
    // set pController id to this id
    pController.setIdKey(this.idKey);
    return true;
  }

  @Override
  public Float getValue(String key) {
    if (physicalController != null)
      return physicalController.getValue(key);
    return null;
  }

  @Override
  public Boolean getBoolean(String key) {
    if (physicalController != null)
      return physicalController.getBoolean(key);
    return null;
  }

  @Override
  public Boolean getBoolean(String key, float threshold) {
    if (physicalController != null)
      return physicalController.getBoolean(key, threshold);
    return null;
  }

  @Override
  public String[] getKeySignature() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String[] getNamedKeySignature() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DirectionalKey getDirectional(String key) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public DirectionalKey getDirectional(String key, float threshold) {
    // TODO Auto-generated method stub
    return null;
  }

  // TODO create assignedController delegate functions that return null when
  // assignedController is null

}
