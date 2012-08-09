#! /bin/bash

rm -f ZombieLord.jar

echo "-- Making bin directories --"
mkdir bin && mkdir ../ZombieLord/bin

echo "-- Compiling with javac --"
javac src/com/talas777/ZombieLord/Main.java -sourcepath src:../ZombieLord/src -d bin -classpath libs/*:../ZombieLord/libs/*

echo "-- Building jar --"
ant

echo "-- Done, run with 'java -jar ZombieLord.jar' or double click the jar file if your system supports it --"