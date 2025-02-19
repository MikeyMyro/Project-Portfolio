/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2024
 * Instructor: Prof. Lily Romano
 *
 * Name: Casey King
 * Section: 1:00
 * Date: 4/10/24
 * Time: 1:47 PM
 *
 * Project: csci205_final_project
 * Package: model
 * Class: ConnectionsModel
 *
 * Description:
 *
 * ****************************************
 */
package model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Class that models playing Connections
 */
public class ConnectionsModel {

    /**
     * How many categories are left
     */
    private int remainingCategories;

    /**
     * Connections game board
     */
    private Board board;



    /**
     * Kind of like a state-check
     * Determines weather or not we are in game
     * Will probably need a getter
     */
    private boolean inGame;



    /**
     * Tracks how many guesses we have.
     */
    private int guesses;


    /**
     * Shouldn't have more than 4 guesses but we can change it if we need to
     */
    private final int MAX_GUESSES = 4;

    /**
     * ConnectionsModel constructor, initializes variables
     */
    public ConnectionsModel() {
        guesses = 0;
        inGame = false;
        remainingCategories = 4;

    }

    /**
     * Choose level to play
     */
    public void chooseLevel(Level level){
        // make board with level
        board = new Board(level);
        inGame = true;
        //System.out.println(level);
    }

    /**
     * Check if selected tiles are a category
     *
     * @return 0 if we have nothing right, 1 if we are 1 guess away,
     * 2 if we got a correct guess, 3 if the game is lost, 4 if it is won, 5 if we have already guessed it
     */
    public int guess() {
        // Only guess if 4 tiles selected
        if(board.getNumSelected() == 4) {
            if (board.checkSelected() != 2) {
                guesses++;
                if (guesses == MAX_GUESSES) {
                    reset();
                    //System.out.println("Game lost");
                    return 3;
                }
                if (board.checkSelected() == 1){
                    return 1;
                }
                else{
                    return 0;
                }
            }
            // Decrease remaining categories, reset if win
            else {
                remainingCategories--;
                if (remainingCategories == 0) {
                    reset();
                    System.out.println("Game won");
                    return 4;
                }
                System.out.println("Correct Guess");
                return 2;
            }
        }
        return 0;
    }


    /**
     * this will help for the feedback part of it. We will keep track of how many guesses are left
     * Like the connections game, we should have some constant indicator of how many guesses are left
     *
     * @return 0 if we are out of guesses. Won't matter ::  otherwise, return how many guesses we have left.
     */
    public int userFeedback(){
        if (guesses<MAX_GUESSES){
            return (MAX_GUESSES-guesses);
        }
        else{
            return 0;
        }
    }

    /**
     * Resets model to not be playing game
     */
    public void reset(){
        // Reset all variables
        inGame = false;
        guesses = 0;
        remainingCategories = 4;
    }

    /**
     * getter method for the board
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * getter for boolean inGame
     * @return inGame
     */
    public boolean isInGame() {
        return inGame;
    }

    /**
     * getter for remainingCategories
     * @return remainingCategories
     */
    public int getRemainingCategories() {
        return remainingCategories;
    }
}
