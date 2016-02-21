/**
 * Title:          Week 5 - Program Improvement IV
 * Author:         Team B : ( Elijah Cornell / Eric Landeis / Gordon Doskas / James Rippon /
 *                            Joseph Hart / Keith Green / Lance Branford )
 * Creation Date:  2016-02-19
 * Class:          PRG/421 - Roland Morales
 * <p/>
 * Dependent libraries: lib/derby.jar
 * <p/>
 * Input: Derby database if present
 * Output: Console
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Main {

    private static final UI ui = UI.getInstance();

    private static final DB db = DB.getInstance();

    /**
     * Main program loop
     */
    private void mainLoop() {

        String menuSelection;

        do {

            ui.displayTitle("Main Menu");

            ui.display("1: Display animal summary");
            ui.display("2: Display animal details");
            ui.display("3: Load new animals from file");
            ui.display("4: Reset animal catalog");
            ui.display("5: Fight animals");
            ui.display("X: Exit");
            ui.spacer();
            ui.displayPrompt("Menu selection : ");

            menuSelection = ui.readInputString();

            if (menuSelection.equalsIgnoreCase("1")) {

                ui.displayTitle("Animal Summary");

                displayAnimalSummary();

            } else if (menuSelection.equalsIgnoreCase("2")) {

                ui.displayTitle("Animal Summary");

                displayAnimalListing();

            } else if (menuSelection.equalsIgnoreCase("3")) {

                ui.displayTitle("Loading Animal");

                Loader.loadData();

            } else if (menuSelection.equalsIgnoreCase("4")) {

                ui.displayTitle("Resetting Catalog");

                db.clearAllAnimals();

            } else if (menuSelection.equalsIgnoreCase("5")) {

                ui.displayTitle("Fight Random Animals");

                List<Animal> al1;
                al1 = db.loadAnimals();
                int totalAnimals = db.totalAnimals();
                Random randGen = new Random();
                int a1 = randGen.nextInt(totalAnimals);
                int a2 = randGen.nextInt(totalAnimals);
                fightAnimals(al1.get(a1), al1.get(a2));

            }

        } while (!menuSelection.equalsIgnoreCase("X"));

    }

    /**
     * Display a summary of the animal catalog data
     */
    private void displayAnimalSummary() {

        int totalAnimal = db.totalAnimals();

        ui.display("Total Animals in Catalog: " + totalAnimal);

    }

    /**
     * Display animals and individual characteristics
     */
    private void displayAnimalListing() {

        // Display numbered listing of all animals within DB

        List<Animal> animalList = db.loadAnimals();

        if (animalList.size() == 0) {
            ui.display("Catalog is empty");
            return;
        }

        int s = 1;
        for (Animal animal : animalList) {
            ui.display(s++ + " : " + animal.getName());
        }

        //

        int inputNumber;
        do {

            ui.spacer();
            inputNumber = ui.promptNumber("Enter # to view: ");

            if (inputNumber < 1 || inputNumber > animalList.size()) {
                ui.displayError("Invalid animal number entered");
                inputNumber = 0;
            }

        } while (inputNumber == 0);

        // Query animal table by name and  display the returned information

        ui.displayTitle("Animal Characteristics");

        Animal animal = db.readAnimal(animalList.get(inputNumber - 1).getName());

        ui.display(animal.toString());

    }




    public int stringCalc(String s1){
        int calcTotal = 0;
        for(int i = 0; i < s1.length(); i++){
            calcTotal = calcTotal + Character.getNumericValue(s1.charAt(i));
        }
        return calcTotal;
    }

    public int convertBoolToInt(boolean b1){
        int bInt = 0;
        if(b1 == true){
            bInt = 15;
        }
        else{
            bInt = 0;
        }
        return bInt;
    }

    public void fightAnimals(Animal a1, Animal a2){
        int a1Name = stringCalc(a1.getName());
        int a1Color = stringCalc(a1.getColor());
        int a1Swim = convertBoolToInt(a1.canSwim());
        int a1Fly = convertBoolToInt(a1.canSwim());
        int a1Skel = convertBoolToInt(a1.isVertebrate());

        int a2Name = stringCalc(a2.getName());
        int a2Color = stringCalc(a2.getColor());
        int a2Swim = convertBoolToInt(a2.canSwim());
        int a2Fly = convertBoolToInt(a2.canSwim());
        int a2Skel = convertBoolToInt(a2.isVertebrate());

        int a1Score = a1Name + a1Color + a1Swim + a1Fly + a1Skel;
        int a2Score = a2Name + a2Color + a2Swim + a2Fly + a2Skel;

        if(a1Score > a2Score){
            System.out.println(a1.getName() + " is Victorous over " + a2.getName());
            System.out.println("End score " + a1Score + "(" + a1.getName() + ") || "
                    + a2Score + "(" + a2.getName() + ")");
        }

        if(a1Score < a2Score){
            System.out.println(a2.getName() + " is Victorous over " + a1.getName());
            System.out.println("End score " + a2Score + "(" + a2.getName() + ") || "
                    + a1Score + "(" + a1.getName() + ")");
        }

        if(a1Score == a2Score){
            System.out.println(a1.getName() + " tied with " + a2.getName());
            System.out.println("End score " + a1Score + "(" + a1.getName() + ") || "
                    + a2Score + "(" + a2.getName() + ")");
        }
    }


    /**
     * Main program entry point
     *
     * @param args None
     */
    public static void main(String[] args) {

        ui.display("========================================");
        ui.display("|       Animal Collection (JDBC)       |");
        ui.display("|        PRG-421 - Wk5 - Team B        |");
        ui.display("========================================");

        //
        ScheduledFuture future = null;
        ScheduledExecutorService exec = null;

        try {

            db.initDB();

            // Loader.updateAge();
            // http://stackoverflow.com/a/18590615
            //
            exec = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors());

            future = exec.scheduleAtFixedRate(new Runnable() {
              public void run() {
                List<Animal> animalList = db.loadAnimals();
                if (animalList.size() > 0) {
                  for (Animal animal : animalList) {
                    // System.out.print(animal.getName() + " ");
                    db.updateAge(animal.getName(), animal.getAge() + 1);
                  }
                }
              }
            }, 1, 1, TimeUnit.SECONDS);

            Main m = new Main();
            m.mainLoop();

        } catch (SQLException e) {
            ui.displayError(e.getMessage());
        } finally {
            if (future != null) {
                future.cancel(true);
            }
            if (exec != null) {
                exec.shutdown();
            }
            db.close();
        }
    }
}
