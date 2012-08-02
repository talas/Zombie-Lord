=============
Zombie Lord
=============

A Java RPG featuring the Hero defending citizens from Zombie attacks brought on by unknown powers.
The game brings you chasing truth about the attacks while exploring the world, gaining allies and strength.  

Using LibGDX.  

 - Source repository: https://github.com/talas/Zombie-Lord  

Building and running
--------------------

Steps to compile depends on which platform the game is being compiled for.
ZombieLord folder contains main platform independent code.
The 3 other folders are for desktop, android and html5 specific code.


Desktop
-------

Java version 1.6 is required.  


To build, run ant in the desktop folder:  

  $ cd ZombieLord-desktop
  $ javac src/com/talas777/ZombieLord/Main.java -sourcepath src:../ZombieLord/src -d bin -classpath libs/*:../ZombieLord/libs/*
  $ ant

then run the program like:  

  $ java -jar zombie-lord.jar

