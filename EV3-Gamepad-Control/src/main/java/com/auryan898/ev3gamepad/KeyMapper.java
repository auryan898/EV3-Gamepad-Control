package com.auryan898.ev3gamepad;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collection;
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
      if (inputKeys[i] != null && inputKeys[i] != "NONE" && inputKeys[i] != "") {
        String e = this.getControllerKey(inputKeys[i]);
        if (e != null)
          keys.add(e);
      }
    }
    Collections.sort(keys); // always has the same order then
    return KeyMapper.join("+", keys);
  }

  public String concatenateNamedKeys(String... inputKeys) {
    return concatenateNamedKeys("+", inputKeys);
  }

  public String concatenateNamedKeys(String separator, String... inputKeys) {
    ArrayList<String> keys = new ArrayList<>();
    for (int i = 0; i < inputKeys.length; i++) {
      if (inputKeys[i] != null && inputKeys[i] != "NONE" && inputKeys[i] != "") {
        String e = this.getNamedKey(inputKeys[i]);
        if (e != null)
          keys.add(e);
      }
    }
    Collections.sort(keys); // always has the same order then
    return KeyMapper.join("+", keys);
  }

  public String concatenateAnyValues(String separator, String... inputs) {
    ArrayList<String> keys = new ArrayList<>();
    for (int i = 0; i < inputs.length; i++) {
      if (inputs[i] != null && inputs[i] != "") {
        String e = inputs[i];
        keys.add(e);
      }
    }
    Collections.sort(keys); // always has the same order then
    return KeyMapper.join("+", keys);
  }

  /**
   * Converts a 2D String array into a HasMap where each subarray is a pair of
   * key-values when put into the hashmap.
   * 
   * @param  keys
   * @return
   */
  public static HashMap<String, String> generateMapFromKeys(String[][] keys, boolean isReversed) {
    HashMap<String, String> res = new HashMap<>();
    for (String[] sub : keys) {
      if (sub.length >= 2 && sub[0] != null && sub[1] != null) {
        if (isReversed) {
          res.put(sub[1], sub[0]);
        } else {
          res.put(sub[0], sub[1]);
        }
      }
    }
    return res;
  }

  public static HashMap<String, String> generateMapFromKeys(String[][] keys) {
    return generateMapFromKeys(keys, false);
  }

  public abstract List<String> getAxisKeys();

  public abstract List<String> getButtonKeys();

  public abstract List<String> getNamedAxisKeys();

  public abstract List<String> getNamedButtonKeys();

  public ArrayList<String> convertListToNamedKeys(Collection<String> inputKeys) {
    ArrayList<String> result = new ArrayList<>();
    for (String key : inputKeys) {
      result.add(this.getNamedKey(key));
    }
    return result;
  }

  public ArrayList<String> convertListToControllerKeys(Collection<String> inputKeys) {
    ArrayList<String> result = new ArrayList<>();
    for (String key : inputKeys) {
      result.add(this.getControllerKey(key));
    }
    return result;
  }

  public ArrayList<String> convertListToOther(List<String> inputKeys) {
    ArrayList<String> result = new ArrayList<>();
    for (String key : inputKeys) {
      result.add(this.getAnyKey(key));
    }
    return result;
  }
  
  public static String join(String separator, Collection<String> strings) {
    return join(separator, strings.toArray(new String[strings.size()]));
  }

  public static String join(String separator, String... strings) {
    String res = "";
    for (int i = 0; i < strings.length - 1; i++) {
      res += strings[i] + separator;
    }
    return res + (strings.length > 0 ? strings[strings.length - 1] : "");
  }
  
  public abstract List<String> getAllControllerKeys();
  
  public abstract List<String> getAllNamedKeys();
}
