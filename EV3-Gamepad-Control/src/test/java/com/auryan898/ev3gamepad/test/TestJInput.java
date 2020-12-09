package com.auryan898.ev3gamepad.test;

import java.io.File;
import java.nio.file.Files;

import com.simontuffs.onejar.Boot;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class TestJInput {
  public static void main(String[] args) throws Exception {
//    Boot.run(args);
    Controller[] baseControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
//    int code = baseControllers[6].hashCode();
//    while (true) {
////      baseControllers[6].poll();
//      System.out.println(baseControllers.length);
//    }
  }
}

class CustomLoader extends ClassLoader {
  File tempDir;
  
  public CustomLoader() {
    this.tempDir = Files.createTempDirectory("ev3gamepad");
    
  }
  
  @Override
  protected String findLibrary(String libname) {
      return tempDir.get;
      // or return null if unknown, then the path will be searched
  }
}