/**
 * Title:          Week 5 - Program Improvement IV
 * Author:         Team B
 * Creation Date:  2016-02-19
 * Class:          PRG/421 - Roland Morales
 */

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DB {

    private final UI ui = UI.getInstance();

    private static final DB SINGLETON = new DB();

    public static DB getInstance() {
        return SINGLETON;
    }

    private Connection conn;

    private DB() {
    }


    protected void initDB() throws SQLException {

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

        } catch (SQLException e) {

            if (e.getSQLState().equals("X0Y32")) {
                ui.display(" . . found an existing animal table ");
            } else {
                ui.displayError(getSQLException(e));
            }
        }

    }

    protected void clearAllAnimals() {

        try (Statement s = conn.createStatement()) {

            String deleteAnimals  = "DELETE FROM animal";

            ui.display(" . deleting all animals");

            s.execute(deleteAnimals);


        } catch (SQLException e) {
            ui.displayError(getSQLException(e));
        }

    }


    protected void insertAnimal(Animal animal) throws SQLException {

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

    public int totalAnimals() {

        int totalAnimals = 0;

        String sql  = "select count(*) from animal";

        try (Statement s = conn.createStatement()) {

            ResultSet rs = s.executeQuery(sql);

            while (rs.next()) {

                totalAnimals = rs.getInt(1);

            }

            rs.close();

        } catch (SQLException e) {
            ui.displayError(getSQLException(e));
        }

        return totalAnimals;

    }

    protected List<Animal> loadAnimals() {

        List<Animal> animalList = new ArrayList<>();

        String createString = "select name, color, swim, fly, vertebrate from animal order by name";

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

    protected Animal readAnimal(String name) {

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


    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("ERROR: Failed to close database - " + e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage());
            }
        }
    }

    private String getSQLException(SQLException e) {
        return e.getErrorCode() + " " + e.getSQLState() + " " + e.getMessage();
    }



}
