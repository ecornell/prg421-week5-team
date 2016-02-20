/**
 * Title:          Week 5 - Program Improvement IV
 * Author:         Team B
 * Creation Date:  2016-02-19
 * Class:          PRG/421 - Roland Morales
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Helper class to handle user interface / console interactions
 * <p/>
 * Usage: UI ui = UI.getInstance();
 */
public class UI {

    private static final UI SINGLETON = new UI();

    /**
     * @return Singleton UI instance
     */
    public static UI getInstance() {
        return SINGLETON;
    }

    /**
     * Private default constructor for singleton pattern
     */
    private UI() {
    }

    /**
     * Display a message to the console
     *
     * @param text Text string to display
     */
    public void display(String text) {
        System.out.println(text);
    }

    /**
     * Display a custom error message to the console
     *
     * @param text Error text to display
     */
    public void displayError(String text) {
        spacer();
        display("!!!  ERROR: " + text);
        spacer();
    }

    /**
     * Display a blank line to the console
     */
    public void spacer() {
        System.out.print(System.lineSeparator());
    }


    /**
     * Display a message to the console before a user input prompt action.
     * Doesn't include an ending line break
     *
     * @param text Text string to display
     */
    public void displayPrompt(String text) {
        System.out.print(text);
    }

    /**
     * Prompt for and capture a number value
     * @param text Prompt text
     * @return Entered number
     */
    public int promptNumber(String text) {
        int inputNumber = 0;
        do {
            displayPrompt(text);
            try {
                inputNumber = readInputInt();
            } catch (NumberFormatException nfe) {
                displayError("Invalid number entered");
            }

        } while (inputNumber == 0);
        return inputNumber;
    }

    /**
     * Prompt the user for a text value
     *
     * @return Enter text string value
     */
    public String readInputString() {
        String in = "";
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {

            in = br.readLine();

        } catch (IOException e) {
            displayError("Input failure - " + e.getMessage());
        }

        return in;
    }

    /**
     * Prompt the user for an integer value
     *
     * @return Entered number value
     */
    public int readInputInt() {
        return Integer.parseInt(readInputString());
    }

    /**
     * Display a boxed title message to the console
     *
     * @param title Title text
     */
    public void displayTitle(String title) {
        StringBuilder titleBar = new StringBuilder();
        for (int i = 0; i < 40; i++) {
            titleBar.append("-");
        }
        StringBuilder titleBarText = new StringBuilder();
        for (int i = 0; i < (18 - title.length() / 2); i++) {
            titleBarText.append(" ");
        }
        titleBarText.append(title);
        for (int i = titleBarText.length(); i < 38; i++) {
            titleBarText.append(" ");
        }

        spacer();
        display(titleBar.toString());
        display("|" + titleBarText.toString() + "|");
        display(titleBar.toString());
    }

}
