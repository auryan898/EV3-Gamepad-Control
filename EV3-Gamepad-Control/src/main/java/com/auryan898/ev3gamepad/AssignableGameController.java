package com.auryan898.ev3gamepad;

import java.util.*;

import com.auryan898.ev3gamepad.keymapping.DefaultKeyMapper;

public class AssignableGameController extends GameController {

  private PhysicalGameController physicalController;
  private KeyMapper mapper = new DefaultKeyMapper();
  private String idKey;
  private String displayIdKey;

  AssignableGameController(KeyMapper mapper, String... keys) {
    if (mapper != null) {
      this.mapper = mapper;
    }
    setIdKey(keys);
  }

  AssignableGameController(KeyMapper mapper, String id) {
    if (mapper != null) {
      this.mapper = mapper;
    }
    this.idKey = id;
  }

  public void setKeyMapper(KeyMapper mapper) {
    this.mapper = mapper; // converts keys
  }

  @Override
  public void update() {
    // TODO implement controller deciding which controller to listen to
  }

  public String setIdKey(String... idKeys) {
    this.idKey = createIdKey(idKeys);
    this.setDisplayIdKey(this.mapper.concatenateNamedKeys("+", idKeys));
    return this.idKey;
  }

  public String createIdKey(String... idKeys) {
    return this.mapper.concatenateKeys(idKeys);
  }

  public boolean matchesIdKey(String... testKeys) {
    return this.idKey != null && testKeys != null
        && this.idKey.equals(this.mapper.concatenateKeys(testKeys));
  }

  public String getDisplayIdKey() {
    return displayIdKey;
  }

  public void setDisplayIdKey(String displayIdKey) {
    this.displayIdKey = displayIdKey;
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
      return physicalController.getValue(mapper.getControllerKey(key));
    return null;
  }

  @Override
  public Boolean getBoolean(String key) {
    if (physicalController != null)
      return physicalController.getBoolean(mapper.getControllerKey(key));
    return null;
  }

  @Override
  public Boolean getBoolean(String key, float threshold) {
    if (physicalController != null)
      return physicalController.getBoolean(mapper.getControllerKey(key), threshold);
    return null;
  }

  @Override
  public String[] getKeySignature() {
    if (physicalController != null)
      return physicalController.getKeySignature();
    return null;
  }

  @Override
  public String[] getNamedKeySignature() {
    return getNamedKeySignature(this.mapper);
  }

  @Override
  public String[] getNamedKeySignature(KeyMapper mapper) {
    if (physicalController != null)
      return physicalController.getNamedKeySignature(mapper);
    return null;
  }

  @Override
  public DirectionalKey getDirectional(String key) {
    if (physicalController != null)
      return physicalController.getDirectional(mapper.getControllerKey(key));
    return null;
  }

  @Override
  public DirectionalKey getDirectional(String key, float threshold) {
    if (physicalController != null)
      return physicalController.getDirectional(mapper.getControllerKey(key), threshold);
    return null;
  }

  public KeyMapper getMapper() {
    return mapper;
  }

  public void setMapper(KeyMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public String[] getButtonSignature() {
    if (physicalController != null)
      return physicalController.getButtonSignature();
    return null;
  }

  @Override
  public String[] getButtonSignature(KeyMapper mapper) {
    if (physicalController != null)
      return physicalController.getButtonSignature(mapper);
    return null;
  }

  public String toString() {
    return "Player: " + this.displayIdKey + " | " + (physicalController != null
        ? physicalController.toString(this.mapper)
        : "");
  }
}
