package com.auryan898.ev3gamepad;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public abstract class KeyMapper {

  /**
   * Converts any controller key to named key, or named to controller. Returns
   * null when the input is not a key at all.
   * 
   * @param  anyKey
   * @return
   */
  public abstract String getAnyKey(String anyKey);

  /**
   * Converts named key to original controller key value.
   * Returns the controller key if given a corresponding named key, or if given a
   * controller key. Returns null when the given input is neither key.
   * 
   * @param  namedKey
   * @return
   */
  public abstract String getControllerKey(String namedKey);

  /**
   * Converts an original controller key to its corresponding named key.
   * Returns the named key, if given a corresponding controller key, or returns
   * the controller key when given a controller key. Returns null if there are no
   * matching conversions.
   * 
   * @param  controllerKey
   * @return
   */
  public abstract String getNamedKey(String controllerKey);

  /**
   * Combines multiple named keys into one long string of controller keys.
   * 
   * @param  inputKeys
   * @return
   */
  public String concatenateKeys(String... inputKeys) {
    return concatenateKeys("+", inputKeys);
  }

  /**
   * Combines multiple named keys into one long string of controller keys.
   * 
   * @param  separator the String separator between keys
   * @param  inputKeys
   * @return
   */
  public String concatenateKeys(String separator, String... inputKeys) {
    ArrayList<String> keys = new ArrayList<>();
    for (int i = 0; i < inputKeys.length; i++) {
      if (inputKeys[i] != null && inputKeys[i] != "NONE" && inputKeys[i] != "")
        keys.add(this.getControllerKey(inputKeys[i]));
    }
    Collections.sort(keys); // always has the same order then
    return String.join("+", keys);
  }

  /**
   * Converts a 2D String array into a HasMap where each subarray is a pair of
   * key-values when put into the hashmap.
   * 
   * @param  keys
   * @return
   */
  public static HashMap<String, String> generateMapFromKeys(String[][] keys) {
    HashMap<String, String> res = new HashMap<>();
    for (String[] sub : keys) {
      if (sub.length >= 2 && sub[0] != null && sub[1] != null) {
        res.put(sub[0], sub[1]);
      }
    }
    return res;
  }

  protected abstract ArrayList<String> getAxisKeys();

  protected abstract ArrayList<String> getButtonKeys();
}
