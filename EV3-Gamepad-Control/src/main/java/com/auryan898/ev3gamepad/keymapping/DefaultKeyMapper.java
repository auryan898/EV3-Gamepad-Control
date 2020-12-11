package com.auryan898.ev3gamepad.keymapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

import com.auryan898.ev3gamepad.KeyMapper;

/**
 * Default mapper that always returns the input of its conversion functions.
 * 
 * @author Ryan Au
 *
 */
public class DefaultKeyMapper extends KeyMapper {
  protected List<String> axisKeys = new ArrayList<String>();
  protected List<String> buttonKeys = new ArrayList<String>();

  public DefaultKeyMapper() {
    axisKeys.addAll(Arrays.asList("x", "y", "rx", "ry", "z", "rz"));
    for (int i = 0; i < 20; i++) {
      buttonKeys.add(i + "");
    }

    axisKeys = Collections.unmodifiableList(axisKeys);
    buttonKeys = Collections.unmodifiableList(buttonKeys);
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
  public List<String> getAxisKeys() {
    return axisKeys;
  }

  @Override
  public List<String> getButtonKeys() {
    return buttonKeys;
  }

  @Override
  public List<String> getNamedAxisKeys() {
    return getAxisKeys();
  }

  @Override
  public List<String> getNamedButtonKeys() {
    return getButtonKeys();
  }

}
