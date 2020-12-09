package com.auryan898.ev3gamepad;

import java.util.*;

public class AssignableGameController extends GameController {

  private List<PhysicalGameController> physicalControllers;
  private GameController assignedController;
  private KeyMapping mapper = null;

  AssignableGameController(List<PhysicalGameController> physicalControllers) {
    // TODO Auto-generated constructor stub
    this.physicalControllers = physicalControllers;
  }
  
  public void setKeyMapper(KeyMapping mapper) {
    this.mapper = mapper; // converts keys
  }

  @Override
  public void update() {
    // TODO implement controller deciding which controller to listen to
  }
  
  // TODO create delegate functions that return null when assignedController is null
  
}
