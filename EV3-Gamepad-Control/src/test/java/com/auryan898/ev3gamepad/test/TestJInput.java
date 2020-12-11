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

import com.auryan898.ev3gamepad.legacy.GameControllerLegacy;
import com.google.common.io.Files;
import com.simontuffs.onejar.Boot;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;

public class TestJInput {
  static final CustomEV3GamepadLoader loader = new CustomEV3GamepadLoader(ControllerEnvironment.getDefaultEnvironment().getClass().getClassLoader());

  @SuppressWarnings("unchecked")
  public static void main(String[] args) throws Exception {

    // Thread.currentThread().setContextClassLoader(loader);
    // loadLibrary("jinput-dx8_64");
    // loader.loadClass(ControllerEnvironment.class.getCanonicalName());
    // ControllerEnvironment.getDefaultEnvironment();
    System.out.println(ControllerEnvironment.getDefaultEnvironment().getClass().getClassLoader());
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

  static void loadLibrary(final String lib_name) {
    AccessController.doPrivileged(
        new PrivilegedAction() {
          public final Object run() {
            String lib_path = System.getProperty("net.java.games.input.librarypath");
            if (lib_path != null)
              System.load(lib_path + File.separator + System.mapLibraryName(lib_name));
            else {
              System.load(loader.findLibrary(lib_name));
              // System.loadLibrary(lib_name);
            }
            return null;
          }
        });
  }
}

class CustomEV3GamepadLoader extends ClassLoader {
  private static final int TEMP_DIR_ATTEMPTS = 10000;
  File tempDir;

  public CustomEV3GamepadLoader(ClassLoader parent) {
    super(parent);
    this.tempDir = createTempDir(); // create temp directory
    copyResToTemp(); // fill temp directory with library files from resources
  }

  @Override
  protected String findLibrary(String libname) {
    // or return null if unknown, then the path will be searched
    String filename = System.mapLibraryName(libname);
    String[] list = this.tempDir.list(new FilenameFilter() {

      @Override
      public boolean accept(File file, String word) {
        if (file.getName().equals(filename) || word.equals(filename))
          return true;
        return false;
      }
    });
    if (list.length > 0) {
      return this.tempDir.getAbsolutePath() + File.separator + list[0];
    }
    return null;
  }

  private void copyResToTemp() {
    try {
      List<String> files = getResourceFiles("ev3gamepad_lib");
      // TODO: copy files into tempDir
      for (String filename : files) {
        File resFile = toURI("ev3gamepad_lib", filename);
        File tempFile = new File(
            this.tempDir.getAbsoluteFile() + File.separator + resFile.getName());
        Files.copy(resFile, tempFile);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private File toURI(String directory, String filename) {
    try {
      URI uri = getResource(directory + "/" + filename).toURI();
      return new File(uri);
    } catch (URISyntaxException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return null;
    }

  }

  public static File createTempDir() {
    // https://stackoverflow.com/questions/617414/how-to-create-a-temporary-directory-folder-in-java
    // https://github.com/google/guava
    File baseDir = new File(System.getProperty("java.io.tmpdir"));
    String baseName = System.currentTimeMillis() + "-";

    for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
      File tempDir = new File(baseDir, baseName + counter);
      if (tempDir.mkdir()) {
        return tempDir;
      }
    }
    throw new IllegalStateException("Failed to create directory within "
        + TEMP_DIR_ATTEMPTS + " attempts (tried "
        + baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
  }

  // https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
  @SuppressWarnings("finally")
  private List<String> getResourceFiles(String path) throws IOException {
    List<String> filenames = new ArrayList<>();

    try {
      InputStream in = getResourceAsStream(path);
      BufferedReader br = new BufferedReader(new java.io.InputStreamReader(in));
      String resource;

      while ((resource = br.readLine()) != null) {
        filenames.add(resource);
      }
    } finally {
      return filenames;
    }
  }

}