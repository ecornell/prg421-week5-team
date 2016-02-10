Title:          Week 4 - JDBC (Animal Collection)
Author:         Elijah Cornell
Creation Date:  2016-02-09
Class:          PRG/421 - Roland Morales

Program Requirements:

Key parts:
 - Write a list of animal and its characteristics to a database using JDBC
 - Display the characteristics of an animal when that animal is selected.

Must demonstrate the use JDBC

Program Flow:
-> Init DB
---> Establishes connection
---> Creates and populates animal table if not present
-> Display Menu
---> Display Characteristics
-----> Read and display all rows on animal table
-----> Prompt for single animal to view
-----> Read and display info on a selected animal row

Input: animals.txt file
Output: Console

Dependent libraries: lib/derby.jar

To Run: java -cp ./lib/derby.jar:./out/production/Week4 Main
