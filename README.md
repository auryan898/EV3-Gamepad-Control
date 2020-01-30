# EV3 Gamepad Control

This project requires Java JDK 8, JInput and the native dll files to be installed for JInput:

see instructions on installing JInput [here](https://github.com/jinput/jinput/wiki/Getting-started-with-JInput).

JInput is compiled using Java JDK 8, but I'm working to port it to JDK 7 for more ease of use, considering the fact the Lejos on the EV3 is only compatible with JDK 7.  JInput is currently using some lambda expressions that were introduced in JDK 8, but they can seemingly be converted to work with JDK 7, which seems to be the only issue regarding compatibility.  Remote communications between pc and ev3 are independent of java version, so a computer can run Java 8 programs that communicate with Java 7 programs on the EV3.

### Running the program

Run the `Main` class as a "Java Application" not as a "LeJos Application".  It should run locally on your pc using JDK 8, which you'd have to install.
