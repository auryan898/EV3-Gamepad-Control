package com.auryan898.ev3gamepad;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.math3.random.ValueServer;

import com.auryan898.ev3gamepad.keymapping.BaseGamepadKeyMapper;
import com.auryan898.ev3gamepad.keymapping.DefaultKeyMapper;

import net.java.games.input.Component;
import net.java.games.input.Controller;

public class PhysicalGameController extends GameController {

  private static final float DEFAULT_DEADZONE = 0.1f;
  private Controller controller;
  private String idKey;
  private HashMap<String, Component> inputValues = new HashMap<>();
  private KeyMapper mapper = new DefaultKeyMapper();
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

  public KeyMapper getMapper() {
    return mapper;
  }

  public void setMapper(KeyMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public Float getValue(String key) {
    key = mapper.getControllerKey(key);
    Component c = inputValues.get(key);
    if (c != null)
      return c.getPollData();
    else
      return null;
  }

  public Float getDeadzone(String key) {
    key = mapper.getControllerKey(key);
    Component c = inputValues.get(key);
    if (c != null) {
      float deadzone = c.getDeadZone();
      return deadzone >= 0.99f || deadzone <= 0f ? this.defaultDeadzone : deadzone;
    } else {
      return null;
    }
  }

  @Override
  public Boolean getBoolean(String key) {
    key = mapper.getControllerKey(key);
    return getBoolean(key, getDeadzone(key));
  }

  @Override
  public Boolean getBoolean(String key, float threshold) {
    key = mapper.getControllerKey(key);
    return Math.abs(getValue(key)) > threshold;
  }

  @Override
  public DirectionalKey getDirectional(String key) {
    key = mapper.getControllerKey(key);
    return getDirectional(key, this.defaultDeadzone);
  }

  @Override
  public DirectionalKey getDirectional(String key, float threshold) {
    key = mapper.getControllerKey(key);
    Float value = Math.abs(getValue(key));

    int code = (int) (float) (value * 8 + 0.001);
    return DirectionalKey.values()[code];
  }

  public String[] getKeySignature() {
    return getNamedKeySignature(new DefaultKeyMapper());
  }

  public String[] getNamedKeySignature() {
    return getNamedKeySignature(this.mapper);
  }

  public String[] getNamedKeySignature(KeyMapper mapper) {
    return getNamedKeySignature(mapper, new ArrayList<String>());
  }

  public String[] getNamedKeySignature(KeyMapper mapper, List<String> exclusions) {
    String[] signature = new String[inputValues.size()];
    int i = 0;
    for (String key : inputValues.keySet()) {
      String displayKey = mapper.getNamedKey(key);
      signature[i] = !exclusions.contains(key) && getBoolean(key) ? displayKey : null;
      i++;
    }
    return signature;
  }

  @Override
  public String[] getButtonSignature() {
    return getNamedKeySignature(this.mapper, this.mapper.getAxisKeys());
  }

  @Override
  public String[] getButtonSignature(KeyMapper mapper) {
    return getNamedKeySignature(mapper, mapper.getAxisKeys());
  }

  public String getName() {
    return controller.toString();
  }

  public String toString() {
    return toString(this.mapper);
  }

  public String toString(KeyMapper mapper) {
    String[] keyValues = this.getKeySignature();
    for (int i = 0; i < keyValues.length; i++) {
      if (keyValues[i] != null)
        keyValues[i] = mapper.getNamedKey(keyValues[i]) + ":" + this.getValue(keyValues[i]);
    }
    return controller.toString() + " | "
        + mapper.concatenateAnyValues(", ", keyValues);
  }

}
