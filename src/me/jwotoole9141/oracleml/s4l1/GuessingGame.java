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

public class GuessingGame {

    public static void main(String[] args) {

        // MAIN LOOP:
        // while true...

            // clear screen

            // ----------------------------------------------

            // print a welcome message

            // initialize list of user response choices

            // if saved games are found...

                // print that user should choose a game

                // for each saved game...

                    // print an option for that save

                    // add the option to the list of choices

            // else... print that no saves were found

            // print a choice to create a new game

            // ----------------------------------------------

            // initialize user response

            // RESPONSE LOOP:
            // while response is insufficient...

                // get user response

                // if response is valid... break response loop

            // ----------------------------------------------

            // if response is to create a new game...

            // RESPONSE-LOOP:
            // while true...

                // prompt user for game's theme (non-plural)

                // get user response

                // prompt for confirmation

                // if user confirms... break response loop

            // print that the new game has been created

            // change response to play a game

            // ----------------------------------------------

            // if response is to play a game...

                // try...

                    // create the binary tree using the chosen game's serialized data file

                // except...

                    // print error message

                    // wait for user input

                    // continue main loop

                // initialize current node and round number

                // GAME LOOP:
                // while true...

                    // show round number and brief help / instr

                    // if the current node is null...

                        // TODO do the computer-lost routine...

                        // ask the user to play again or go to main menu

                        // get user response

                        // if user wants to play again...

                            // initialize current node and round number

                    // show the current node's question

                    // also show that the user can quit at any time

                    // initialize user input

                    // RESPONSE LOOP:
                    // while input is invalid...

                        // get user response

                        // if response is a valid choice (y/n)... break response loop

                    // if response is to quit...

                        // serialize the binary tree and save it to the game's file

                        // break main loop

                    // if the current node is marked as win/loose case...

                        // if the response is affirmative...

                            // print that the computer has won!

                            // ask the user to play again or go to main menu

                            // get user response

                            // if user wants to play again...

                                // TODO this flow isn't good for immediately playing a game again
                                // initialize current node and round number

                            // else... break the game loop

                        // if the response is negative...

                            // TODO do the computer-lost routine...

                            // ask the user to play again or go to main menu

                            // get user response

                            // if user wants to play again...

                                // initialize current node and round number

                        // if the current node is marked as a win/loose case...

                    // if the next node corresponding with the user's response is null...

                        // give up

                    // if the current node is marked

                    // if the current node is "an answer"...

                        //

                    // else, the current node is "a question"...

                        //

                    // seek the next child node based on response

                    // if its null...

                    // increment round number

            // ----------------------------------------------

            // if response is to quit...

                // print goodbye message

                // break main loop
    }
}
