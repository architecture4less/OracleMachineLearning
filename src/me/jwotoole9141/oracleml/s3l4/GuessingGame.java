/*
 * AUTH: Jared O'Toole
 * DATE: Wed Oct 16th, 2019
 * PROJ: OracleMachineLearning
 * FILE: GuessingGame.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the driver class of the game.
 *
 * - code to do a console pause from: https://stackoverflow.com/questions/6032118/make-the-console-wait-for-a-user-input-to-close
 * - code to do a console clear from: https://stackoverflow.com/questions/2979383/java-clear-the-console#33379766
 */

package me.jwotoole9141.oracleml.s3l4;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A yes/no guessing game console app.
 *
 * @author Jared O'Toole
 */
public class GuessingGame {

    public static final Scanner INPUT = new Scanner(System.in);
    public static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * Run the guessing game through the console.
     *
     * @param args unused command line args
     */
    public static void main(String[] args) {

        // print a welcome message...
        clearScreen();
        System.out.println("\nWelcome to the guessing game!\n");
        consolePause();

        // run the main menu...
        doMainMenu();

        // print a goodbye message...
        clearScreen();
        System.out.println("\nGoodbye!\n");
        consolePause();
    }

    /**
     * Run the game's main menu until the user quits.
     */
    public static void doMainMenu() {

        while (true) {

            clearScreen();

            // initialize list of user response choices...
            Map<String, File> choices = new HashMap<>();

            // if saved games are found...
            List<File> gameFiles = findGames(getDirectory());
            if (gameFiles.size() > 0) {

                // print that user should choose a game...
                System.out.println("\nChoose a saved game...");

                // for each saved game...
                int choiceIdx = 0;
                for (File gameFile : gameFiles) {

                    // add it to the list of choices...
                    choices.put(String.valueOf(choiceIdx), gameFile);

                    // print out the choice...
                    String theme = pluralized(toDisplayName(gameFile.getName()));
                    System.out.printf("  [%d] %s%n", choiceIdx, theme);
                    choiceIdx++;
                }
            }

            // else, print that no saves were found...
            if (choices.isEmpty()) {
                System.out.println("\nThere are no saved games...");
            }

            // create a choice to create a new game...
            System.out.println("  [n] new game");
            choices.put("n", null);

            // create a choice to quit...
            System.out.println("  [q] quit");
            choices.put("q", null);

            // initialize user response...
            String response = "";
            System.out.println();

            // while response is insufficient...
            while (!choices.containsKey(response)) {

                // get user response...
                System.out.print(">>> ");
                response = INPUT.nextLine();
            }

            // if response is to quit...
            if (response.equals("q")) {
                break;
            }

            // if response is to create a new game...
            else if (response.equals("n")) {
                doCreateGame();
            }

            // else, response is to play a game...
            else {
                doViewGame(choices.get(response));
            }
        }
    }

    /**
     * Run the game creation menu until success or the user quits.
     */
    public static void doCreateGame() {

        clearScreen();
        System.out.println("Creating a new game...");

        // prompt user for game's theme (non-plural)...
        System.out.println("\nWhat is the theme of this game?");
        System.out.println("(Use a non-plural word or phrase)");
        System.out.println("(Use 'q' to go back)\n");

        while (true) {

            // get user response...
            String response = "";
            while (response.isEmpty()) {

                System.out.print(">>> ");
                response = INPUT.nextLine().trim();
            }

            // if response is 'q', break the loop...
            if (response.equals("q")) {
                break;
            }

            // create a file name for the game...
            String gameFileName = toFileName(response);
            File gameFile = new File(gameFileName);

            // make sure it doesn't exist yet...
            if (gameFile.exists()) {
                System.out.println("A game with that theme already exists...\n");
                continue;
            }

            // try to initialize the file with a new json object...
            try (FileWriter writer = new FileWriter(gameFile)) {
//                writer.write(new JSONObject().toJSONString());
                writer.write("{}");
            }

            // if there was an io exception...
            catch (IOException ex) {

                // print error and break the loop...
                System.out.printf("Unable to create new game with file %s...\n\n", ex.getMessage());
                consolePause();
                break;
            }

            // if the file now exists...
            if (gameFile.exists()) {

                // print that the new game has been created...
                System.out.println("Successfully created the new game!\n");
                consolePause();
                doViewGame(gameFile);
                break;
            }
        }
    }

    /**
     * View a game until the user quits.
     *
     * @param gameFile the data for the game
     */
    public static void doViewGame(File gameFile) {

        label:
        while (true) {

            clearScreen();

            // display game options...
            System.out.println("\nWhat do you wish to do?\n");
            System.out.println("[p] play game");
            System.out.println("[d] display tree");
            System.out.println("[q] back to main menu\n");

            List<String> choices = new ArrayList<>(Arrays.asList("p", "d", "q"));

            // get user response...
            String response = "";
            while (!choices.contains(response)) {
                System.out.print(">>> ");
                response = INPUT.nextLine().trim();
            }

            switch (response) {

                // the user chose to quit...
                case "q":
                    break label;

                // the user chose to display the game's data...
                case "d":
                    DecisionTree tree = loadTree(gameFile);
                    if (tree != null) {

                        int size = tree.getSize();
                        String diagram = tree.getDiagram();
                        String theme = toDisplayName(gameFile.getName());

                        clearScreen();

                        System.out.printf("The %s Guessing Game has %d questions!\n", theme, size);
                        System.out.println(diagram);

                        consolePause();
                    }
                    break;

                // the user chose to play the game...
                case "p":
                    doPlayGame(gameFile);
                    break label;
            }
        }
    }

    /**
     * Play a game until the user quits.
     *
     * @param gameFile the data for the game
     */
    public static void doPlayGame(File gameFile) {

        // try to load the game...
        DecisionTree tree = loadTree(gameFile);

        // quit if the game couldn't be loaded...
        if (tree == null) {
            return;
        }

        // initialize question and round number...
        Question prevQuestion = null;
        Question question = tree.getRoot();
        int round = 0;

        // play rounds...
        while (true) {

            // show round number...
            System.out.printf("\nRound %d\n", round);

            // if the current question is null...
            if (question == null) {

                // the computer lost...
                doComputerLost(tree, prevQuestion);
                break;
            }
            // show the current question's prompt...
            System.out.println(question.getPrompt());

            // show the user's choices...
            System.out.println("(use 'y' or 'n' to respond or 'q' to exit)\n");
            List<String> choices = new ArrayList<>(Arrays.asList("y", "n", "q"));

            // initialize user input...
            String response = "";

            // while input is invalid...
            while (!choices.contains(response)) {

                // get user response...
                System.out.print(">>> ");
                response = INPUT.nextLine().trim().substring(0, 1);
            }
            switch (response) {

                // the user chose to quit...
                case "q":
                    break;

                // the user answered yes...
                case "y":
                    // if this is the last question, computer won...
                    if (question.isLast()) {
                        doComputerWon();
                    }
                    // else, go to the next question...
                    else {
                        prevQuestion = question;
                        question = question.getYes();
                    }
                    break;

                // the user answered no...
                case "n":
                    // if this is the last question, computer lost...
                    if (question.isLast()) {
                        doComputerLost(tree, question);
                    }
                    // else, go to the next question...
                    else {
                        prevQuestion = question;
                        question = question.getYes();
                    }
                    break;
            }
            // increment round
            round++;
        }

        // save the game...
        saveTree(tree, gameFile);
    }

    public static void doComputerWon() {

        System.out.println("I won!");
    }

    public static void doComputerLost(DecisionTree tree, Question question) {

        // TODO

        // print computer lost message...
        System.out.println("I give up... what was the answer?");

        // get the answer from user...
        System.out.println();
        consolePause();

        // computer asks what makes this answer unique...

        // get response from user...

        // create question from the response...

        // add question to the tree...
    }

    /**
     * Clear all text from the console.
     */
    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch (InterruptedException | IOException ignored) {

        }
    }

    /**
     * Tell the user to press any key to continue.
     */
    public static void consolePause() {
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

        return new File(new File("").getAbsolutePath());
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
     *     <li>Cactus -> Cacti</li>
     *     <li>Compass -> Compasses</li>
     *     <li>Fish -> Fishes</li>
     *     <li>Money -> Monies</li>
     *     <li>Pillow -> Pillows</li>
     *   </ul>
     * </p>
     *
     * @param word a word to pluralize
     * @return the pluralized word
     */
    public static String pluralized(String word) {

        if (word.endsWith("us")) {
            return word.substring(0, word.length() - 2) + "i";
        }
        else if (word.endsWith("s") || word.endsWith("sh")) {
            return word + "es";
        }
        else if (word.endsWith("y")) {
            return word.substring(0, word.length() - 1) + "ies";
        }
        else {
            return word + "s";
        }
    }

    /**
     * Capitalize the first letter of each word in the given phrase.
     *
     * @param phrase the word or phrase
     * @return the capitalized version
     */
    public static String capitalized(String phrase) {

        return Arrays.stream(phrase.split(" ")).map(
                word -> (word.substring(0, 1).toUpperCase()
                        + word.substring(1).toLowerCase() + " ")
        ).collect(Collectors.joining());
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

        // remove the potential file extension...
        int extIdx = fileName.lastIndexOf('.');
        fileName = (extIdx <= 0) ? fileName : fileName.substring(0, extIdx);

        // convert underscores to spaces and capitalize each word...
        return Arrays.stream(fileName.split("_")).map(
                word -> word.substring(0, 1).toUpperCase()
                        + word.substring(1).toLowerCase()

        ).collect(Collectors.joining(" ")).trim();
    }

    /**
     * Convert a game's name in display-name format to file-name format.
     *
     * <p>
     * The file name uses all lowercase, underscores instead of spaces,
     * and includes the json file name extension.
     * </p>
     *
     * @param displayName the game's display name
     * @return the game's file name
     */
    public static String toFileName(String displayName) {

        // convert spaces to underscores, set everything lowercase, and add an underscore...
        return String.join("_", displayName.toLowerCase().split(" ")).trim() + ".json";
    }

    /**
     * Save a game to its file.
     *
     * @param tree the game's decision tree
     * @param file the game's file
     */
    public static void saveTree(@NotNull DecisionTree tree, @NotNull File file) {

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(MAPPER.writeValueAsString(tree));
        }
        catch (IOException ex) {
            System.out.printf("Could not save game data: %s\n", ex.getMessage());
        }
    }

    /**
     * Load a game from its file.
     *
     * @param file the game file to load
     * @return the game's decision tree
     */
    public static @Nullable DecisionTree loadTree(@NotNull File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return MAPPER.readValue(reader.lines().collect(Collectors.joining()), DecisionTree.class);
        }
        catch (IOException ex) {
            System.out.printf("Could not load game data: %s\n", ex.getMessage());
            return null;
        }
    }
}
