/*
 * AUTH: Jared O'Toole
 * DATE: Wed Oct 23rd, 2019
 * PROJ: OracleMachineLearning
 * FILE: Utils.java
 *
 * "Create a Yes/No Guessing Game" task for Section 4 Lesson 1
 * of the AI with ML in Java Oracle iLearning Course.
 *
 * Defines the Utils class.
 */

package me.jwotoole9141.oracleml.s3l4;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Defines some utility functions for the guessing game.
 *
 * @author Jared O'Toole
 */
public class Utils {

    public static final Scanner INPUT = new Scanner(System.in);

    /**
     * Clears all text from the console.
     */
    public static void clearScreen() {
        try {
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        }
        catch (InterruptedException | IOException ignored) {

        }
    }

    /**
     * Tells the user to press any key to continue.
     */
    public static void consolePause() {
        try {
            new ProcessBuilder("cmd", "/c", "pause").inheritIO().start().waitFor();
        }
        catch (InterruptedException | IOException ignored) {

        }
    }

    /**
     * Gets the directory that this program is running from.
     *
     * @return the folder if it is valid, else null
     */
    public static @NotNull File curDirectory() {

        return new File(new File("").getAbsolutePath());
    }

    /**
     * Gets a list of json files in the given directory.
     *
     * @return json files that represent serialized games
     */
    public static @NotNull List<File> findGames(@Nullable File directory) {

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
     * Applies simple grammar rules to create a pluralized version of the given word.
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
    public static @NotNull String pluralized(@NotNull String word) {

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
     * Capitalizes the first letter of each word in the given phrase.
     *
     * @param phrase the word or phrase
     * @return the capitalized version
     */
    public static @NotNull String capitalized(@NotNull String phrase) {

        return Arrays.stream(phrase.split(" ")).map(
                word -> (word.substring(0, 1).toUpperCase()
                        + word.substring(1).toLowerCase() + " ")
        ).collect(Collectors.joining());
    }

    /**
     * Converts a game's name in file-name format to display-name format.
     *
     * <p>
     * The display name uses spaces instead of underscores, capitalizes the
     * first letter of each word, and does not include the file name extension.
     * </p>
     *
     * @param fileName the game's file name
     * @return the game's display name
     */
    public static @NotNull String toDisplayName(@NotNull String fileName) {

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
     * Converts a game's name in display-name format to file-name format.
     *
     * <p>
     * The file name uses all lowercase, underscores instead of spaces,
     * and includes the json file name extension.
     * </p>
     *
     * @param displayName the game's display name
     * @return the game's file name
     */
    public static @NotNull String toFileName(@NotNull String displayName) {

        // convert spaces to underscores, set everything lowercase, and add an underscore...
        return String.join("_", displayName.toLowerCase().split(" ")).trim() + ".json";
    }

    /**
     * Saves a game to its file.
     *
     * @param tree the game's decision tree
     * @param file the game's file
     */
    public static void saveTree(@NotNull DecisionTree tree, @NotNull File file) {

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(new JSONObject(tree.toMap()).toJSONString());
        }
        catch (IOException ex) {
            System.out.printf("Could not save game data: %s\n", ex.getMessage());
        }
    }

    /**
     * Loads a game from its file.
     *
     * @param file the game file to load
     * @return the game's decision tree
     */
    public static @Nullable DecisionTree loadTree(@NotNull File file) {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return DecisionTree.fromMap(((Map<?, ?>) new JSONParser()
                    .parse(reader.lines().collect(Collectors.joining()))));
        }
        catch (IllegalArgumentException | ClassCastException | IOException | ParseException ex) {
            System.out.printf("Could not load game data: %s\n", ex.getMessage());
            return null;
        }
    }
}
