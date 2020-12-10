package com.auryan898.ev3gamepad;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class GameControllerDisplay {
  ArrayList<GameController> controllers = new ArrayList<>();
  private JFrame frame;
  private ArrayList<JLabel> labels = new ArrayList<>();

  public static void main(String[] args) {
    GameControllerDisplay d = new GameControllerDisplay();
    d.create();
    d.show();
  }

  public GameControllerDisplay(Collection<GameController> controllersToAdd) {
    this();
    controllers.addAll(controllersToAdd);
  }

  public GameControllerDisplay() {
    create();
  }

  public void addController(GameController controller) {
    controllers.add(controller);
    JLabel l = new JLabel("__");
    labels.add(l);
    frame.add(l);
  }

  protected void create() {
    // TODO: fill in with display init
    frame = new JFrame();
    BoxLayout box = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS);
    frame.setLayout(box);
  }

  public void update() {
    // TODO: fill in with update code
    for (int i = 0; i < controllers.size() && i < labels.size(); i++) {
      labels.get(i).setText("<html>" + controllers.get(i).toString() + "</html>");
    }
    frame.pack();
    frame.update(frame.getGraphics());
  }

  public void show() {
    // TODO: fill in with display show
    frame.pack();
    frame.setSize(600, 300);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }

}
