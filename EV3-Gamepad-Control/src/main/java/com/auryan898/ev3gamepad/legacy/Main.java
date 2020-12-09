package com.auryan898.ev3gamepad.legacy;
//package ca.mcgill.ecse211.project;
//
////static import to avoid duplicating variables and make the code easier to read
//import static ca.mcgill.ecse211.project.Resources.*;
//
//import java.net.MalformedURLException;
//import java.rmi.NotBoundException;
//import java.rmi.RemoteException;
//
//import javax.swing.JFrame;
//
//import lejos.hardware.Sound;
//import lejos.remote.ev3.RemoteEV3;
//
///**
// * The main driver class for the lab.
// */
//public class Main {
//
//  /**
//   * The main entry point.
//   * 
//   * @param args not used
//   */
//  public static void main(String[] args) {
//    
//    // The manager runs in a thread that watches every physical game controller
//    GameControllerManager manager = new GameControllerManager();
//    new Thread(manager).start();
//    
//    // Contains the input from a game controller, assuming Xbox named keys.
//    // This is fine for just one controller.
//    GameController gamepad1 = manager.getRawController(0);
//    
//    // A gamepad that is assigned by pressing "start" and "a" at the same time.
//    // GameController gamepad1 = manager.getGameController("a");
//    // GameController gamepad2 = manager.getGameController("b"); // uses "b" key
//    
//    JFrame frame = new JFrame();
//    frame.setSize(300, 300);
//    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    frame.setVisible(true);
//    
//    System.out.println("Connecting to ev3...");
//    RemoteEV3 ev3 = null; 
//    try {
//      ev3 = new RemoteEV3("10.0.1.1");
//    } catch (RemoteException e) {
//      e.printStackTrace();
//      System.exit(-1);
//    } catch (MalformedURLException e) {
//      e.printStackTrace();
//      System.exit(-1);
//    } catch (NotBoundException e) {
//      e.printStackTrace();
//      System.exit(-1);
//    }
//    System.out.println("Successful connection to ev3");
//    ev3.setDefault();
//    Sound.beep();
//    
//    while(true) {
//      try {
//        if (gamepad1.get("back")>0) {
//          break;
//        }
//        
//        if (gamepad1.get("a")>0) {
//          ev3.getLED().setPattern(0);
//        } else if (gamepad1.get("b")>0) {
//          ev3.getLED().setPattern(1);
//        } else if (gamepad1.get("y")>0) {
//          ev3.getLED().setPattern(2);
//        } else if (gamepad1.get("x")>0) {
//          ev3.getLED().setPattern(3);
//        } 
//        
//        if (gamepad1.get("right_bumper")>0.5) {
//          Sound.beepSequence();
//        }
//        
//        
//      } catch (Exception e) {
//        // TODO Auto-generated catch block
//        e.printStackTrace();
//        break;
//      }
//        
//      
//    }
//    
//    
//    // Done with program
//    System.out.println("Done Program!");
//    
//  }
//
//}
