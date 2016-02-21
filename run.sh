#!/bin/bash
#java -cp ./lib/derby.jar:./out/production/Week5 Main
javac -d ./out/production/Week5 -cp ./lib/derby.jar -sourcepath ./src ./src/Main.java
java -cp ./lib/derby.jar\;./out/production/Week5 Main
