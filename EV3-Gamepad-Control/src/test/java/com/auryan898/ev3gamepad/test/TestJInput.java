package com.auryan898.ev3gamepad.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;

import com.auryan898.ev3gamepad.CustomEV3GamepadLoader;
import com.auryan898.ev3gamepad.legacy.GameControllerLegacy;
import com.google.common.io.Files;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class TestJInput {
  static final CustomEV3GamepadLoader loader = new CustomEV3GamepadLoader(ControllerEnvironment.getDefaultEnvironment().getClass().getClassLoader());

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {
    System.out.println(System.getProperty("java.io.tmpdir"));
    System.setProperty("java.library.path", loader.getTempDir());
    System.setProperty("net.java.games.input.librarypath", loader.getTempDir());
    
    // Thread.currentThread().setContextClassLoader(loader);
    // loadLibrary("jinput-dx8_64");
    // loader.loadClass(ControllerEnvironment.class.getCanonicalName());
    // ControllerEnvironment.getDefaultEnvironment();
    System.out.println(loader);
    System.out.println(loader.getParent());
    AccessController.doPrivileged(
        new PrivilegedAction() {
          public final Object run() {
            String osName = getPrivilegedProperty("os.name", "").trim();
            try {
              if (osName.equals("Linux")) {
                loader.loadClass("net.java.games.input.LinuxEnvironmentPlugin");
              } else if (osName.equals("Mac OS X")) {
                loader.loadClass("net.java.games.input.OSXEnvironmentPlugin");
              } else if (osName.equals("Windows XP") || osName.equals("Windows Vista")
                  || osName.equals("Windows 7")) {
                loader.loadClass("net.java.games.input.DirectAndRawInputEnvironmentPlugin");
              } else if (osName.equals("Windows 98") || osName.equals("Windows 2000")) {
                loader.loadClass("net.java.games.input.DirectInputEnvironmentPlugin");
              } else if (osName.startsWith("Windows")) {
                loader.loadClass("net.java.games.input.DirectAndRawInputEnvironmentPlugin");
              } else {
                // nothing?
              }
            } catch (ClassNotFoundException e) {
              // TODO Auto-generated catch block
              e.printStackTrace();
            }
            return null;
          }
        });

    // GameControllerLegacy.main(args);
    Controller[] baseControllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
    // System.out.println(baseControllers.length);

    // Boot.run(args);
    // int code = baseControllers[6].hashCode();
    // while (true) {
    //// baseControllers[6].poll();
    // }
  }

  static String getPrivilegedProperty(final String property, final String default_value) {
    return (String) AccessController.doPrivileged(new PrivilegedAction() {
      public Object run() {
        return System.getProperty(property, default_value);
      }
    });
  }

  // static void loadLibrary(final String lib_name) {
  // AccessController.doPrivileged(
  // new PrivilegedAction() {
  // public final Object run() {
  // String lib_path = System.getProperty("net.java.games.input.librarypath");
  // if (lib_path != null)
  // System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
  // else {
  // System.load(loader.findLibrary(lib_name));
  // // System.loadLibrary(lib_name);
  // }
  // return null;
  // }
  // });
  // }
}

