/*
 * AUTH: Jared O'Toole
 * DATE: Wed Oct 16th, 2019
 * PROJ: OracleMachineLearning
 * FILE: GuessingGame.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Define the driver class of the game.
 */

package me.jwotoole9141.oracleml.s4l1;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * A yes/no guessing game console app.
 *
 * <p>
 *   Uses a binary decision tree and json serialization.
 * </p>
 *
 * @author Jared O'Toole
 */
public class GuessingGame {

    public static boolean runningApp = true;
    public static boolean playingGame = false;

    public static Scanner input = new Scanner(System.in);

    /**
     * Run the guessing game through the console.
     *
     * @param args unused command line args
     */
    public static void main(String[] args) {

        // clear the screen
        clearScreen();

        // print a welcome message
        System.out.println();
        System.out.println("Welcome to the guessing game!");
        System.out.println();

        consolePause();
        doMainMenu();
    }

    public static void doMainMenu() {

        while (runningApp) {

            // clear the screen
            clearScreen();

            // ----------------------------------------------

            // initialize list of user response choices
            Map<String, File> choices = new HashMap<>();

            // if saved games are found...
            File gamesDir = getDirectory();
            if (gamesDir != null) {

                List<File> gameFiles = findGames(gamesDir.getParentFile());
                if (gameFiles.size() > 0) {

                    // print that user should choose a game
                    System.out.println();
                    System.out.println("Choose a saved game to play:");

                    // for each saved game...
                    int choiceIdx = 0;
                    for (File gameFile : gameFiles) {

                        // add the option to the list of choices
                        choices.put(String.valueOf(choiceIdx), gameFile);

                        // print an option for that save
                        String theme = pluralized(toDisplayName(gameFile.getName()));
                        System.out.printf("  [%d] %s%n", choiceIdx, theme);
                        choiceIdx++;
                    }
                }
            }
            // else... print that no saves were found
            if (choices.isEmpty()) {
                System.out.println("There are no saved games to play...");
            }

            // create a choice to create a new game
            System.out.println("  [n] new game");
            choices.put("n", null);

            // create a choice to quit
            System.out.println("  [q] quit");
            choices.put("q", null);

            // ----------------------------------------------

            // initialize user response
            String response = "";
            System.out.println();

            // while response is insufficient...
            while (!choices.containsKey(response)) {

                // get user response
                System.out.print(">>> ");
                response = input.nextLine();
            }

            // ----------------------------------------------

            // if response is to quit...
            if (response.equals("q")) {

                doQuitApp();
            }

            // ----------------------------------------------

            // if response is to create a new game...
            else if (response.equals("n")) {

                doCreateGame();
            }

            // ----------------------------------------------

            // else, response is to play a game...
            else {

                doPlayGame();
            }
        }
    }

    public static void doCreateGame() {

        System.out.println("TODO created a game...");
        consolePause();

        // while true...
        {
            // prompt user for game's theme (non-plural)

            // get user response

            // prompt for confirmation

            // if user confirms... break response loop
            {
                // print that the new game has been created

                // change response to play a game
            }
        }
    }

    public static void doPlayGame() {

        System.out.println("TODO Played a game...");
        consolePause();

        // try...
        {
            // create the binary tree using the chosen game's serialized data file
        }
        // except...
        {
            // print error message

            // wait for user input

            // continue main loop
        }
        // initialize current node and round number

        // while true...
        {
            // show round number and brief help / instr

            // if the current node is null...
            {
                // TODO do the computer-lost routine...

                // ask the user to play again or go to main menu

                // get user response

                // if user wants to play again...
                {
                    // initialize current node and round number
                }
            }
            // show the current node's question

            // also show that the user can quit at any time

            // initialize user input

            // while input is invalid...
            {
                // get user response

                // if response is a valid choice (y/n)... break response loop
            }
            // if response is to quit...
            {
                // serialize the binary tree and save it to the game's file

                // break main loop
            }
            // if the current node is marked as win/loose case...
            {
                // if the response is affirmative...
                {
                    // print that the computer has won!

                    // ask the user to play again or go to main menu

                    // get user response

                    // if user wants to play again...
                    {
                        // TODO this flow isn't good for immediately playing a game again
                        // initialize current node and round number
                    }
                    // else... break the game loop
                }
                // if the response is negative...
                {
                    // TODO do the computer-lost routine...

                    // ask the user to play again or go to main menu

                    // get user response

                    // if user wants to play again...
                    {
                        // initialize current node and round number
                    }
                }
            }

            // increment round number
        }
    }

    public static void doQuitApp() {

        // print goodbye message
        System.out.println("Goodbye!");
        consolePause();

        // break main loop
        runningApp = false;
    }

    /**
     * Clear all text from the console.
     */
    public static void clearScreen() {
        // https://stackoverflow.com/questions/2979383/java-clear-the-console#33379766
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch (InterruptedException | IOException ignored) {

        }
    }

    /**
     * Tell the user to press any key to continue...
     */
    public static void consolePause() {
        // https://stackoverflow.com/questions/6032118/make-the-console-wait-for-a-user-input-to-close
        try {
            new ProcessBuilder("cmd", "/c", "pause").inheritIO().start().waitFor();
        }
        catch (InterruptedException | IOException ignored) {

        }
    }

    /**
     * Get the directory that this program is running from.
     *
     * @return the folder if it is valid, else null
     */
    public static File getDirectory() {
        try {
            return new File(GuessingGame.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI());
        }
        catch (URISyntaxException ex) {
            return null;
        }
    }

    /**
     * Get a list of json files in the given directory.
     *
     * @return json files that represent serialized games
     */
    public static List<File> findGames(File directory) {

        List<File> results = new ArrayList<>();
        if (directory == null) {
            return results;
        }
        File[] dirFiles = directory.listFiles();
        if (dirFiles != null) {
            for (File dirFile : dirFiles) {
                if (dirFile.isFile()) {
                    if (dirFile.getName().endsWith("json")) {
                        results.add(dirFile);
                    }
                }
            }
        }
        return results;
    }

    /**
     * Apply simple grammar rules to create a pluralized version of the given word.
     * <br /><br />
     * <p>
     * Here is an example of each rule taken into consideration:
     *   <ul>
     *     <li>Compass -> Compasses</li>
     *     <li>Fish -> Fishes</li>
     *     <li>Money -> Monies</li>
     *     <li>Cactus -> Cacti</li>
     *     <li>Pillow -> Pillows</li>
     *   </ul>
     * </p>
     *
     * @param word a word to pluralize
     * @return the pluralized word
     */
    public static String pluralized(String word) {

        if (word.endsWith("s") || word.endsWith("sh")) {
            return word + "es";
        }
        else if (word.endsWith("y")) {
            return word.substring(0, word.length() - 1) + "ies";
        }
        else if (word.endsWith("us")) {
            return word.substring(0, word.length() - 1) + "i";
        }
        else {
            return word + "s";
        }
    }

    /**
     * Convert a game's name in file-name format to display-name format.
     *
     * <p>
     * The display name uses spaces instead of underscores, capitalizes the
     * first letter of each word, and does not include the file name extension.
     * </p>
     *
     * @param fileName the game's file name
     * @return the game's display name
     */
    public static String toDisplayName(String fileName) {

        // remove the potential file extension
        int extIdx = fileName.lastIndexOf('.');
        fileName = (extIdx <= 0) ? fileName : fileName.substring(0, extIdx);

        // convert underscores to spaces and capitalize each word
        StringBuilder result = new StringBuilder();
        for (String word : fileName.split("_")) {

            result.append(word.substring(0, 1).toUpperCase());
            result.append(word.substring(1));
            result.append(" ");
        }
        return result.toString().trim();
    }
}
