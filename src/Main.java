/**
 * Title:          Week 4 - JDBC
 * Author:         Elijah Cornell
 * Creation Date:  2016-02-09
 * Class:          PRG/421 - Roland Morales
 *
 * Key parts:
 * - Write a list of animal and its characteristics to a database using JDBC
 * - Display the characteristics of an animal when that animal is selected.
 *
 * Must demonstrate the use JDBC
 *
 * Program Flow:
 * -> Init DB
 * ---> Establishes connection
 * ---> Creates and populates animal table if not present
 * -> Display Menu
 * ---> Display Characteristics
 * -----> Read and display all rows on animal table
 * -----> Prompt for single animal to view
 * -----> Read and display info on a selected animal row
 *
 * Dependent libraries: lib/derby.jar
 *
 * Input: Derby database if present
 * Output: Console
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Main {

    private final UI ui = UI.getInstance();

    private Connection conn;

    /**
     * Main program loop
     */
    private void mainLoop() {

        ui.display("========================================");
        ui.display("|       Animal Collection (JDBC)       |");
        ui.display("|    PRG-421 - Wk4 - Elijah Cornell    |");
        ui.display("========================================");

        try {

            initDB();

            //

            String menuSelection;

            do {

                ui.displayTitle("Main Menu");

                ui.display("D: Display animal characteristics");
                ui.display("X: Exit");
                ui.spacer();
                ui.displayPrompt("Menu selection (D/X) : ");

                menuSelection = ui.readInputString();

                if (menuSelection.equalsIgnoreCase("D")) {

                    displayAnimalListing();

                }

            } while (!menuSelection.equalsIgnoreCase("X"));


        } catch (SQLException e) {

            ui.displayError(getSQLException(e));

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    ui.displayError("Failed to close database - " + e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage());
                }
            }

        }


    }

    /**
     * Display animals and individual characteristics
     */
    private void displayAnimalListing() {

        ui.displayTitle("Animal Listing");

        // Display numbered listing of all animals within DB

        List<Animal> animalList = loadAnimalsFromDB();

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

        Animal animal = readSingleAnimalFromDB(animalList.get(inputNumber - 1).getName());

        ui.display(animal.toString());

    }

    private void initDB() throws SQLException {

        ui.display(" . registering Derby database driver");

        DriverManager.registerDriver(new org.apache.derby.jdbc.EmbeddedDriver());

        ui.display(" . creating connection to animal database");

        conn = DriverManager.getConnection("jdbc:derby:db/animal;create=true");

        try (Statement s = conn.createStatement()) {

            String createString = "CREATE TABLE animal  "
                    + "(animal_id INT NOT NULL GENERATED ALWAYS AS IDENTITY, "
                    + " name VARCHAR(32) NOT NULL, "
                    + " color VARCHAR(32) NOT NULL, "
                    + " swim BOOLEAN NOT NULL, "
                    + " fly BOOLEAN NOT NULL, "
                    + " vertebrate BOOLEAN NOT NULL) ";

            ui.display(" . creating animal table ");

            s.execute(createString);

            ui.display(" . . initializing animal table data");

            List<Animal> animalList = new ArrayList<>();
            animalList.add(new Animal("Ant", "Black", false, false, false));
            animalList.add(new Animal("Dog", "Brown", true, false, true));
            animalList.add(new Animal("Bird", "Blue", false, true, true));

            for (Animal animal: animalList) {
                insertAnimalToDB(animal);
            }

        } catch (SQLException e) {

            if (e.getSQLState().equals("X0Y32")) {
                ui.display(" . . found an existing animal table ");
            } else {
                ui.displayError(getSQLException(e));
            }
        }

    }


    private void insertAnimalToDB(Animal animal) throws SQLException {

        String pQuery = "INSERT INTO animal (name, color, swim, fly, vertebrate) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(pQuery)) {

            pstmt.setString(1, animal.getName());
            pstmt.setString(2, animal.getColor());
            pstmt.setBoolean(3, animal.canSwim());
            pstmt.setBoolean(4, animal.canFly());
            pstmt.setBoolean(5, animal.isVertebrate());

            pstmt.execute();

        } catch (SQLException e) {
            ui.displayError(getSQLException(e));
        }

    }


    private List<Animal> loadAnimalsFromDB() {

        List<Animal> animalList = new ArrayList<>();

        String createString = "select name, color, swim, fly, vertebrate from animal order by named";

        try (Statement s = conn.createStatement()) {

            ResultSet rs = s.executeQuery(createString);

            while (rs.next()) {

                Animal a = new Animal(rs.getString("name"));

                a.setColor(rs.getString("color"));
                a.setCanFly(rs.getBoolean("fly"));
                a.setCanSwim(rs.getBoolean("swim"));
                a.setVertebrate(rs.getBoolean("vertebrate"));

                animalList.add(a);

            }

            rs.close();

        } catch (SQLException e) {
            ui.displayError(getSQLException(e));
        }

        return animalList;

    }

    private Animal readSingleAnimalFromDB(String name) {

        Animal animal = null;

        String selectAnimal = "select name, color, swim, fly, vertebrate from animal where name = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(selectAnimal)) {

            pstmt.setString(1, name);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {

                animal = new Animal(rs.getString("name"));

                animal.setColor(rs.getString("color"));
                animal.setCanFly(rs.getBoolean("fly"));
                animal.setCanSwim(rs.getBoolean("swim"));
                animal.setVertebrate(rs.getBoolean("vertebrate"));

            } else {

                ui.displayError("No animal with name " + name + " found in database");

            }

            rs.close();

        } catch (SQLException e) {
            ui.displayError(getSQLException(e));
        }

        return animal;

    }

    private String getSQLException(SQLException e) {
        return e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage();
    }


    /**
     * Main program entry point
     *
     * @param args None
     */
    public static void main(String[] args) {

        Main m = new Main();
        m.mainLoop();

    }

}
