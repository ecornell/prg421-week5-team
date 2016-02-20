/**
 * Title:          Week 5 - Program Improvement IV
 * Author:         Team B
 * Creation Date:  2016-02-19
 * Class:          PRG/421 - Roland Morales
 */

import java.io.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Loader {

    private static final DB db = DB.getInstance();
    private static final UI ui = UI.getInstance();

    public static void loadData() {

        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        File animalFile = new File("animals.txt");

        try(BufferedReader br = new BufferedReader(new FileReader(animalFile))) {

            String line = br.readLine();

            int count = 0;

            while (line != null) {

                count++;

                executor.submit(new LoadJob(line));

                line = br.readLine();
            }

            executor.shutdown();

            ui.display("New animals loaded : " + count);

        } catch (FileNotFoundException fefe) {

            ui.displayError("Could not find file " + animalFile.getAbsolutePath());

        } catch (IOException e) {

            ui.displayError("Could not process file " + animalFile.getName() + " : " + e.getMessage());

        } catch (Exception e) {

            ui.displayError("Could not load file " + animalFile.getAbsolutePath() + " : " + e.getMessage());

        }

    }


    /**
     * Runnable tasks that parses and load a new animal into the database
     */
    static class LoadJob implements Runnable {
        private String input;
        public LoadJob(String input) {
            this.input = input;
        }
        public void run() {
            try {

                String[] data = input.split(",");

                Animal animal = new Animal();
                animal.setName(data[0]);
                animal.setColor(data[1]);
                animal.setCanSwim(data[2].equals("true"));
                animal.setCanFly(data[3].equals("true"));
                animal.setVertebrate(data[4].equals("true"));

                db.insertAnimal(animal);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
