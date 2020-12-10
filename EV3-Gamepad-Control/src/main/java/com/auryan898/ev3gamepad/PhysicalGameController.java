package com.auryan898.ev3gamepad;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.random.ValueServer;

import net.java.games.input.Component;
import net.java.games.input.Controller;

public class PhysicalGameController extends GameController {

  private static final float DEFAULT_DEADZONE = 0.05f;
  private Controller controller;
  private String idKey;
  private HashMap<String, Component> inputValues = new HashMap<>();
  private float defaultDeadzone;

  PhysicalGameController(Controller controller) {
    this(controller, DEFAULT_DEADZONE);
  }

  PhysicalGameController(Controller controller, float defaultDeadzone) {
    this.controller = controller;
    Component[] components = controller.getComponents();
    for (Component comp : components) {
      inputValues.put(comp.getIdentifier().getName(), comp);
    }
    this.defaultDeadzone = defaultDeadzone;
  }

  @Override
  public void update() {
    controller.poll();
  }

  public String getIdKey() {
    return idKey;
  }

  void setIdKey(String idKey) {
    this.idKey = idKey;
  }

  @Override
  public Float getValue(String key) {
    return inputValues.get(key).getPollData();
  }

  public Float getDeadzone(String key) {
    float deadzone = inputValues.get(key).getDeadZone();
    return deadzone >= 0.99f || deadzone <= 0f ? this.defaultDeadzone : deadzone;
  }

  @Override
  public Boolean getBoolean(String key) {
    return getBoolean(key, getDeadzone(key));
  }

  @Override
  public Boolean getBoolean(String key, float threshold) {
    return Math.abs(getValue(key)) > threshold;
  }

  @Override
  public DirectionalKey getDirectional(String key) {
    return getDirectional(key, this.defaultDeadzone);
  }

  @Override
  public DirectionalKey getDirectional(String key, float threshold) {
    Float value = Math.abs(getValue(key));

    int code = (int) (float) (value * 8 + 0.001);
    return DirectionalKey.values()[code];
  }

  public String[] getKeySignature() {
    String[] signature = new String[inputValues.size()];
    int i = 0;
    for (String key : inputValues.keySet()) {
      signature[i] = getBoolean(key) ? (key) + "" : null;
      i++;
    }
    return signature;
  }

  public String[] getNamedKeySignature() {
    return getKeySignature();
  }

  public String toString() {
    return controller.toString() + ":" + String.join("+", getKeySignature());
  }

}
