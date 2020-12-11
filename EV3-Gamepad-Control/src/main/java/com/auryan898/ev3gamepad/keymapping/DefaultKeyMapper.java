package com.auryan898.ev3gamepad.keymapping;

import java.util.ArrayList;
import java.util.Arrays;

import com.auryan898.ev3gamepad.KeyMapper;

/**
 * Default mapper that always returns the input of its conversion functions.
 * 
 * @author Ryan Au
 *
 */
public class DefaultKeyMapper extends KeyMapper {
  private ArrayList<String> axisKeys = new ArrayList<String>();
  private ArrayList<String> buttonKeys = new ArrayList<String>();

  public DefaultKeyMapper() {
    axisKeys.addAll(Arrays.asList("x", "y", "rx", "ry", "z", "rz"));
    for (int i = 0; i < 20; i++) {
      buttonKeys.add(i + "");
    }
  }

  @Override
  public String getAnyKey(String anyKey) {
    return anyKey;
  }

  @Override
  public String getControllerKey(String namedKey) {
    return namedKey;
  }

  @Override
  public String getNamedKey(String controllerKey) {
    return controllerKey;
  }

  @Override
  protected ArrayList<String> getAxisKeys() {
    return axisKeys;
  }

  @Override
  protected ArrayList<String> getButtonKeys() {
    return buttonKeys;
  }

}
