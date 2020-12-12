package com.auryan898.ev3gamepad.keymapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.auryan898.ev3gamepad.KeyMapper;

public class BaseGamepadKeyMapper extends DefaultKeyMapper {
  protected String[][] namedKeys = {
      { "0", "a_button" },
      { "1", "b_button" },
      { "2", "x_button" },
      { "3", "y_button" },
      { "4", "left_button" },
      { "5", "right_button" },
      { "6", "back" },
      { "7", "start" },
      { "pov", "dpad" },
      { "8", "left_stick_button" },
      { "9", "right_stick_button" },
      { "x", "left_stick_x" },
      { "y", "left_stick_y" },
      { "rx", "right_stick_x" },
      { "ry", "right_stick_y" },
      { "z", "triggers" }, // Triggers are combined into one value unfortunately
      { "10", "10" },
      { "11", "11" },
      { "12", "12" },
      { "13", "13" },
      { "14", "14" },
      { "15", "15" },
  };
  protected HashMap<String, String> controllerToNamedKeys = this.generateMapFromKeys(namedKeys,
      false);
  protected HashMap<String, String> namedToControllerKeys = this.generateMapFromKeys(namedKeys,
      true);
  private List<String> namedAxisKeys;
  private List<String> namedButtonKeys;

  public BaseGamepadKeyMapper() {
    super();
    namedAxisKeys = Collections.unmodifiableList(this.convertListToNamedKeys(this.axisKeys));
    namedButtonKeys = Collections.unmodifiableList(this.convertListToNamedKeys(this.buttonKeys));
  }

  @Override
  public String getAnyKey(String anyKey) {
    if (controllerToNamedKeys.containsKey(anyKey))
      return controllerToNamedKeys.get(anyKey);
    else if (namedToControllerKeys.containsKey(anyKey))
      return namedToControllerKeys.get(anyKey);
    else
      return null;
  }

  @Override
  public String getControllerKey(String namedKey) {
    if (controllerToNamedKeys.containsKey(namedKey))
      return namedKey; // Actually given a controller key, so returning that instead
    else if (namedToControllerKeys.containsKey(namedKey))
      return namedToControllerKeys.get(namedKey);
    else
      return null;
  }

  @Override
  public String getNamedKey(String controllerKey) {
    if (namedToControllerKeys.containsKey(controllerKey))
      return controllerKey; // Actually given a named key, so returning that instead
    else if (controllerToNamedKeys.containsKey(controllerKey))
      return controllerToNamedKeys.get(controllerKey);
    else
      return null;
  }

  @Override
  public List<String> getAxisKeys() {
    return super.getAxisKeys();
  }

  @Override
  public List<String> getButtonKeys() {
    return super.getButtonKeys();
  }

  @Override
  public List<String> getNamedAxisKeys() {
    return namedAxisKeys;
  }

  @Override
  public List<String> getNamedButtonKeys() {
    return namedButtonKeys;
  }
  
  @Override
  public List<String> getAllControllerKeys() {
    return new ArrayList<String>(controllerToNamedKeys.keySet());
  }

  @Override
  public List<String> getAllNamedKeys() {
    return new ArrayList<String>(namedToControllerKeys.keySet());
  }
}
