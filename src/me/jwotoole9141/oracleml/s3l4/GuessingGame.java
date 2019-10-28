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
 */

package me.jwotoole9141.oracleml.s3l4;

import org.javatuples.Pair;
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

    /**
     * Runs the guessing game through the console.
     *
     * @param args unused command line args
     */
    public static void main(String[] args) {

        // print a welcome message...
        Utils.clearScreen();
        System.out.println("\nWelcome to the guessing game!\n");
        Utils.consolePause();

        // run the main menu...
        doMainMenu();

        // print a goodbye message...
        Utils.clearScreen();
        System.out.println("\nGoodbye!\n");
    }

    /**
     * Runs the game's main menu until the user quits.
     */
    public static void doMainMenu() {

        loopMainMenu:
        while (true) {

            Utils.clearScreen();

            // initialize list of user response choices...
            List<String> choices = new ArrayList<>();
            Map<String, File> gameChoices = new HashMap<>();

            // if saved games are found...
            List<File> gameFiles = Utils.findGames(Utils.curDirectory());
            if (gameFiles.size() > 0) {

                // print that the user should choose a game...
                System.out.println("\nChoose a saved game...");

                // for each saved game...
                int choiceIdx = 0;
                for (File gameFile : gameFiles) {

                    // add it to the list of choices...
                    String key = String.valueOf(choiceIdx);
                    gameChoices.put(key, gameFile);
                    choices.add(key);

                    // print out the choice...
                    String theme = Utils.pluralized(Utils.toDisplayName(gameFile.getName()));
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
            choices.add("n");

            // create a choice to quit...
            System.out.println("  [q] quit");
            choices.add("q");

            // initialize user response...
            String response = "";
            System.out.println();

            // while response is insufficient...
            while (!choices.contains(response)) {

                // get user response...
                System.out.print(">>> ");
                response = Utils.INPUT.nextLine().trim().substring(0, 1);
            }

            switch (response) {

                // the user chose to quit...
                case "q":
                    break loopMainMenu;

                // the user chose to create a new game...
                case "n":
                    doCreateGame();
                    break;

                // else, the user chose to view a game...
                default:
                    new GuessingGame(gameChoices.get(response)).view();
                    break;
            }
        }
    }

    /**
     * Runs the game creation menu until success or the user quits.
     */
    public static void doCreateGame() {

        // print that a game is being created...
        Utils.clearScreen();
        System.out.println("\nCreating a new game...");

        // prompt user for game's theme (non-plural)...
        System.out.println("\nWhat is the theme of this game?");
        System.out.println("(Use a non-plural word or phrase)");
        System.out.println("(Use 'q' to go back)\n");

        GuessingGame game = null;

        while (true) {

            // initialize user response...
            String response = "";

            // while response is insufficient...
            while (response.isEmpty()) {

                // get user response...
                System.out.print(">>> ");
                response = Utils.INPUT.nextLine().trim();
            }

            // if response is 'q', break the loop...
            if (response.equals("q")) {
                break;
            }

            // create a file name for the chosen theme...
            String gameFileName = Utils.toFileName(response);
            File gameFile = new File(gameFileName);

            // make sure it doesn't exist yet...
            if (gameFile.exists()) {
                System.out.println("A game with that theme already exists...\n");
                continue;
            }

            // try to initialize the file...
            try (FileWriter writer = new FileWriter(gameFile)) {
                writer.write("{\"tree\": null}");
            }

            // if there was an io exception...
            catch (IOException ex) {

                // print error and break the loop...
                System.out.printf("Unable to create new game with file %s...\n\n", ex.getMessage());
                Utils.consolePause();
                break;
            }

            // if the file now exists...
            if (gameFile.exists()) {

                // print that the new game has been created...
                System.out.println("Successfully created the new game!\n");
                Utils.consolePause();

                // view the new game...
                new GuessingGame(gameFile).view();
                break;
            }
        }
    }

    private @NotNull File file;
    private @NotNull String name;
    private @NotNull String theme;
    private @Nullable DecisionTree tree;

    private @NotNull List<Pair<Question, Boolean>> answers;
    private @Nullable Question question;
    private int round;

    /**
     * Creates a new guessing game with the specified save file.
     *
     * <p>
     * The game's decision tree is loaded from file during construction.
     * If it couldn't be loaded, the game's tree will be null.
     * </p>
     *
     * @param saveFile the file to contain the game's decision tree
     */
    public GuessingGame(@NotNull File saveFile) {

        this.file = saveFile;
        this.name = Utils.toDisplayName(saveFile.getName());
        this.theme = Utils.pluralized(name);
        this.tree = Utils.loadTree(saveFile);

        this.answers = new ArrayList<>();
        this.question = null;
        this.round = 0;
    }

    /**
     * Gets the game's save file.
     *
     * @return the save file
     */
    public @NotNull File getFile() {
        return file;
    }

    /**
     * Gets the singular form of the game's theme.
     *
     * @return the singular word or phrase
     */
    public @NotNull String getName() {
        return name;
    }

    /**
     * Gets the game's theme.
     *
     * @return a plural word or phrase
     */
    public @NotNull String getTheme() {
        return theme;
    }

    /**
     * Gets the game's decision tree.
     *
     * @return the game's decision tree, if it exists, else null
     */
    public @Nullable DecisionTree getTree() {
        return tree;
    }

    /**
     * Views the game until the user quits.
     *
     * <p>
     * The user has the options to view the game's tree structure and to play the game.
     * Playing the game in this way immediately saves any changes back to file afterwards.
     * </p>
     */
    public void view() {

        loopViewGame:
        while (true) {

            Utils.clearScreen();

            // display game options...
            System.out.println("\nWhat do you wish to do?\n");
            System.out.println("[p] play game");
            System.out.println("[d] display tree");
            System.out.println("[q] back to main menu\n");

            List<String> choices = new ArrayList<>(Arrays.asList("p", "d", "q"));

            // initialize user response...
            String response = "";

            // while response is insufficient...
            while (!choices.contains(response)) {

                // get user response...
                System.out.print(">>> ");
                response = Utils.INPUT.nextLine().trim().substring(0, 1);
            }

            switch (response) {

                // the user chose to quit...
                case "q":
                    break loopViewGame;

                // the user chose to display the game's data...
                case "d":

                    // if the game could be loaded...
                    if (tree != null) {

                        // get game data...
                        int size = tree.getSize();
                        String diagram = tree.getDiagram();

                        Utils.clearScreen();

                        // print game data...
                        System.out.printf("\nThe %s Guessing Game has %d question(s):\n\n", theme, size);
                        System.out.println((!diagram.isEmpty()) ? diagram : "(nothing to display...)");

                        Utils.consolePause();
                    }
                    break;

                // the user chose to play the game...
                case "p":
                    play();
                    save();
                    break;
            }
        }
    }

    /**
     * Plays the game until the user quits.
     *
     * <p>
     * Will immediately return if the game's tree is null.
     * </p>
     */
    public void play() {

        // if the game couldn't be loaded, return...
        if (tree == null) {
            return;
        }

        // initialize the question, path, and round number...
        answers.clear();
        question = tree.getRoot();
        round = 0;

        // play rounds...
        loopPlayGame:
        while (true) {

            // show round number...
            Utils.clearScreen();
            System.out.printf("\nThe '%s' Guessing Game\n\n", theme);
            System.out.printf("Round %d\n\n", round);

            // show the computer's summary of knowledge about the answer...
            System.out.printf("%s...\n", Utils.sentenceCase(answers.stream()
                    .map(prevQA -> {
                        Question question = prevQA.getValue0();
                        Boolean answer = prevQA.getValue1();
                        return question.getAnswer(answer, "it");
                    })
                    .collect(Collectors.joining(", "))));

            // if the current question is null...
            if (question == null) {

                // the computer must forfeit...
                cpuForfeit();
                break;
            }

            // show the current question's prompt...
            System.out.println();
            System.out.println(question.getPrompt());

            // show the user's choices...
            System.out.println("(use 'y' or 'n' to respond or 'q' to exit)\n");
            List<String> choices = new ArrayList<>(Arrays.asList("y", "n", "q"));

            // initialize user input...
            String response = "";

            // while input is insufficient...
            while (!choices.contains(response)) {

                // get user response...
                System.out.print(">>> ");
                response = Utils.INPUT.nextLine().trim().substring(0, 1);
            }
            switch (response) {

                // the user chose to quit...
                case "q":
                    break loopPlayGame;

                // the user answered yes...
                case "y":
                    // if this is the last question, computer won...
                    if (question.isLast()) {
                        cpuWon();
                        break loopPlayGame;
                    }
                    // else, go to the next question...
                    answers.add(new Pair<>(question, true));
                    question = question.getYes();
                    break;

                // the user answered no...
                case "n":
                    // if this is the last question, computer lost...
                    if (question.isLast()) {
                        cpuLost();
                        break loopPlayGame;
                    }
                    // else, go to the next question...
                    answers.add(new Pair<>(question, false));
                    question = question.getNo();
                    break;
            }
            // increment round number...
            round++;
        }
    }

    /**
     * The CPU wins the game by guessing correctly.
     */
    public void cpuWon() {

        System.out.println("\nThe computer wins!\n");
        Utils.consolePause();
    }

    /**
     * The CPU forfeits the game by running out of questions.
     */
    public void cpuForfeit() {

        System.out.println("\nThe computer forfeits...\n");
        teachCpu();
    }

    /**
     * The CPU loses the game by guessing wrongly.
     */
    public void cpuLost() {

        System.out.println("\nThe computer loses...\n");
        teachCpu();
    }

    /**
     * Has the user tell the CPU their answer and possibly
     * something to differentiate that answer from the others.
     *
     * <p>
     * Will immediately return if the game's tree is null.
     * The answer list should not contain any pairs with final questions.
     * </p>
     */
    public void teachCpu() {

        if (tree == null) {
            return;
        }
        for (Pair<Question, Boolean> prevQA : answers) {
            if (prevQA.getValue0().isLast()) {

                System.out.print("Huh. The game should have ended already... ");
                System.out.printf("The answer was '%s'.\n\n", prevQA.getValue0().getSubject());
                System.out.println("There are no questions to ask.\n");
                return;
            }
        }

        // prompt user for the answer...
        System.out.println("What was the answer?");
        System.out.println("(use a non-plural word or phrase)\n");

        // initialize user response...
        String response = "";

        // while response is insufficient...
        while (response.isEmpty()) {

            // get user response...
            System.out.print(">>> ");
            response = Utils.INPUT.nextLine().trim();
        }
        String subject = response;

        // create a new final question...
        Question newFinalQuestion = new Question(Relation.ANSWER, subject);

        // if there was no final question...
        if (question == null) {

            // if there was a previous, non-final question...
            if (!answers.isEmpty()) {

                Pair<Question, Boolean> prevQA = answers.get(answers.size() - 1);
                Question prevQuestion = prevQA.getValue0();
                Boolean prevAnswer = prevQA.getValue1();

                // add the new final question as an answer...
                prevQuestion.setYesOrNo(prevAnswer, newFinalQuestion);
            }
            // else, set the root question...
            else {
                tree.setRoot(newFinalQuestion);
            }
            System.out.println("\nOkay. Got it.");
        }
        // else, if this was the final question...
        else if (question.isLast()) {

            // computer asks what makes this subject unique...
            Utils.clearScreen();
            System.out.printf("\nThe '%s' Guessing Game\n\n", theme);
            System.out.printf("Round %d\n", round);

            String computerGuess = question.getSubject();
            if (answers.isEmpty()) {
                System.out.printf("\nSo, what makes '%s' different from '%s'?\n",
                        subject, computerGuess);
            }
            else {
                Question firstQuestion = answers.get(0).getValue0();
                Boolean firstAnswer = answers.get(0).getValue1();
                System.out.printf("\nSo, %s.\n\nBut, what makes it different from '%s'?\n",
                        firstQuestion.getAnswer(firstAnswer, "'" + subject + "'")
                                + ((answers.size() > 2) ? ", " : "")
                                + answers.subList(1, answers.size()).stream()
                                .map(prevQA -> {
                                    Question question = prevQA.getValue0();
                                    Boolean answer = prevQA.getValue1();
                                    return question.getAnswer(answer, "it");
                                })
                                .collect(Collectors.joining(", ")),
                        computerGuess);
            }

            // tell user how response should be structured...
            System.out.println("(describe using one of the following relationships)");
            System.out.println();
            System.out.println("  it is/isnt a type of X   e.g. it is a type of vehicle");
            System.out.println("  it is/isnt X             e.g. it is metallic / isnt on earth");
            System.out.println("  it has/doesnt have X     e.g. it has four wheels / doesnt have weight");
            System.out.println("  it can/cant X            e.g. it can go real fast / cant make noise");
            System.out.println();

            Pair<Relation, String> parsedResponse;
            Relation parsedRelation;
            String parsedSubject;

            // while response is insufficient...
            while (true) {

                // get user response...
                System.out.print(">>> ");
                response = Utils.INPUT.nextLine().trim();

                // if there is a response...
                if (!response.isEmpty()) {

                    // parse the relation type and subject...
                    parsedResponse = Relation.parse(response);

                    // check if the response was sufficient...
                    if (parsedResponse == null) {

                        System.out.println("I didn't understand that...");
                        System.out.println("Please form your response as instructed above.\n");
                    }
                    else if (parsedResponse.getValue0() == Relation.ANSWER) {

                        System.out.println("I did not ask for an answer...");
                    }
                    else {
                        parsedRelation = parsedResponse.getValue0();
                        parsedSubject = parsedResponse.getValue1();
                        break;
                    }
                }
            }

            // create question from the response...
            Relation trueRelation = parsedRelation.toForm(true);
            Question newQuestion = new Question(trueRelation, parsedSubject);
            boolean newAnswer = parsedRelation.isTrueForm();

            // set the new question's answer to the new final question...
            newQuestion.setYesOrNo(newAnswer, newFinalQuestion);

            // if there was a previous, non-final question...
            if (!answers.isEmpty()) {

                Pair<Question, Boolean> prevQA = answers.get(answers.size() - 1);
                Question prevQuestion = prevQA.getValue0();
                Boolean prevAnswer = prevQA.getValue1();

                // insert between these two questions...
                prevQuestion.setYesOrNo(prevAnswer, newQuestion);
            }
            // else, insert above the root question...
            else {
                newQuestion.setYesOrNo(!newAnswer, tree.getRoot());
                tree.setRoot(newQuestion);
            }

            // print confirmation of new question...
            System.out.printf("\nAh, so %s.\n", parsedRelation.getStatement(subject, newQuestion.getSubject()));
        }
        // else, something strange happened...
        else {
            System.out.println("I could have kept going. I'm not sure why I gave up...");
        }

        System.out.println();
        Utils.consolePause();
    }

    /**
     * Saves the game's decision tree to file.
     *
     * <p>
     * Will immediately return if the game's tree is null.
     * </p>
     */
    public void save() {

        if (tree != null) {
            Utils.saveTree(tree, file);
        }
    }
}
