package com.auryan898.ev3gamepad;

import net.java.games.input.Controller;

public abstract class GameController {

  public abstract void update();

  public abstract Float getValue(String key);

  public abstract Boolean getBoolean(String key);

  public abstract Boolean getBoolean(String key, float threshold);

  public abstract DirectionalKey getDirectional(String key);

  public abstract DirectionalKey getDirectional(String key, float threshold);

  public abstract String[] getKeySignature();

  public abstract String[] getNamedKeySignature();
  
  public abstract String[] getNamedKeySignature(KeyMapper mapper);
  
  public abstract String[] getButtonSignature();
  
  public abstract String[] getButtonSignature(KeyMapper mapper);
  
  public abstract KeyMapper getMapper();
}
