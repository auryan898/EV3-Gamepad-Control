# EV3 Gamepad Control

This project requires Java JDK 7+ and automatically includes JInput 2.0.6 and native library dependencies as part of its execution  
(specifically in GameControllerManager initalization)

## Installation

First go to [Releases](https://github.com/auryan898/EV3-Gamepad-Control/releases) and download the latest `EV3-Control-Gamepad.jar` file. This is contains all of the code and dependencies (JInput + native). Just import it or run it using `java -jar EV3-Control-Gamepad.jar` and it will open up a window, displaying all connected controllers, and eight player slots for assigning the controllers to.

## Usage

### Getting Started: Manager Instance
The first thing you do in every situation is get the `GameControllerManager` instance:

    GameControllerManager manager = GameControllerManager.getInstance();

This is a singleton so it's always the same manager instance wherever you call this method.

---

### Getting Gamepad Controllers

Next thing is to grab some controllers, either the "physical" ones or the "assignable" ones.

    // Gets all the connected controllers, in an unknown order determined by the machine
    List<PhysicalGameController> allConnectedControllers = manager.getPhysicalControllers(); 
    
    // Gets the player controller, or creates one if not there already, based on a button combination on the gamepad
    AssignableGameController player1 = manager.getAssignableController(new BaseGamepadKeyMapper(), "start", "a_button");
    
    // BaseGamepadKeyMapper changes gamepad button names into whatever you want. Subclass KeyMapper for your own custom naming.
    // The "start" button is actually named "7", and "a_button" is "0" so this command does the same thing:
    player1 = manager.getAssignableController("7","0"); // Gets the exact same player controller as above ^

---

### Using Gamepad Controllers

Now that we have a controller, let's say `player1` using the `BaseGamepadKeyMapper`, we use the controller by doing two things, (1) Updating `manager` and (2) Getting the gamepad values.

    // Updates all gamepad values of controllers, and updates assignments of player controllers
    manager.update();
    
    // this will get you a float value, from either 0 to 1 for buttons, and -1 to 1 for axes
    // all gamepad data are floats
    player1.getValue("left_stick_x");
    
    // turns the float value into a boolean, false for 0, true for anything else.
    // any value can be put here to get a boolean out
    player1.getBoolean("y_button"); 
    
    // special method that turns the dpad float into directions (ex. UP, DOWN, UP_RIGHT, DOWN_LEFT, ...)
    // dpad values come out as 0 for no press, 0.25 for UP, 0.5 for RIGHT (and so on) and 0.375 for UP_RIGHT
    player1.getDirectional("dpad"); // NOTE: doesn't work for axes/sticks, only dpad really
   
### Updating Gamepad Controllers

Don't call `.update()` directly on on the controllers, always call `manager.update()`. The manager is also a `Runnable`, so it can be used with a thread:

    // Starts a simple while loop that calls update, and sleeps for 20ms by default
    Thread t = new Thread(manager);
    t.start(); 
    
    // manager.setUpdateIntervalMilliseconds(20L) // sets the update interval time in ms
    
You can manually update using `manager.update()` however you like, in whatever loops or threads you want to.
    
## Known Problems

### Display a Window to Use JInput
In some IDEs, such as Eclipse, running JInput is a bit problematic because gamepads might not be "captured" and whenever you do `manager.update()` the values don't actually update because the program can't access the gamepads. To bypass this, we can create a Java Swing GUI window to "capture" gamepad focus. There's a provided helper class to make this simpler:

    GameControllerDisplay display = new GameControllerDisplay();
    display.init(); // opens it up for usage
    
Also, you can add controllers to the display, to see their status:

    display.addController(player1);
    display.addController(manager.getPhysicalController(0));
    display.update(); // update the display with gamepad data
    


