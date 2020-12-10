package com.auryan898.ev3gamepad.keymapping;

import com.auryan898.ev3gamepad.KeyMapper;

/**
 * Default mapper that always returns the input of its conversion functions.
 * 
 * @author Ryan Au
 *
 */
public class DefaultKeyMapper extends KeyMapper {

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

}
