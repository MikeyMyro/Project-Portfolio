/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2024
 * Instructor: Prof. Lily Romano
 *
 * Name: Casey King
 * Section: 1:00
 * Date: 4/10/24
 * Time: 1:45 PM
 *
 * Project: csci205_final_project
 * Package: model
 * Class: Board
 *
 * Description:
 *
 * ****************************************
 */
package model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Board of words for Connections
 */
public class Board {

    /**
     * 4x4 array of tiles containing words to be guessed
     */
    ArrayList<Tile> words;

    /**
     * Array of selected words
     */
    ArrayList<Tile> selected;

    /**
     * Level of Connections
     */
    private Level level;



    /**
     * Initialize board according to level
     *
     * @param level difficulty of game
     * @author Casey King, Mikey Myro
     */
    public Board(Level level) {
        level = level; // make specific board depending on the level of difficulty
        switch (level) {
            case EASY: {
                this.words = GridMaker.makeEasyModeBoard();
                this.level = Level.EASY;
                break;
            }
            case MEDIUM: {
                this.words = GridMaker.makeMediumModeBoard();
                this.level = Level.MEDIUM;
                break;
            }
            case HARD: {
                this.words = GridMaker.makeHardModeBoard();
                this.level = Level.HARD;
                break;
            }
            case EXTREME: {
                this.words = GridMaker.makeExtremeModeBoard();
                this.level = Level.EXTREME;
                break;
            }
            case HOLLYWOOD:{
                this.level = Level.HOLLYWOOD;
                this.words = GridMaker.makeHollywoodBoard();
                break;
            }
        }
        // Shuffle words
        Collections.shuffle(this.words);
        selected = new ArrayList<>();
    }

    /**
     * Selects a tile on the board using indexing
     *
     * @param i index of tile
     *
     * @author Casey K
     */
    public void select(int i) {
        Tile selectTile = words.get(i);

        // Selected on deselect a tile
        if (selected.contains(selectTile)) {
            selected.remove(selectTile);
            selectTile.select();
        } else if (selected.size() < 4) {
            selected.add(selectTile);
            selectTile.select();
        }
    }

    /**
     * Check if selected tiles are in correct category
     * Acts as a type of extended boolean. A lot of different options can happen depending
     * on what the user selects
     *
     * @return 0 if it's incorrect / invalid, 1 if there are 3 of the same category, 2 if there are all 4
     * and 3 if we have already guessed it
     * @author Owen R, Casey K
     */
    public int checkSelected() {
         if(this.selected.size() < 4) {
            return 0;
        }


        // see if categories all match
        Map<Integer, Integer> guessesPer = new HashMap<>(); // Mutable map
        guessesPer.put(1, 0);
        guessesPer.put(2, 0);
        guessesPer.put(3, 0);
        guessesPer.put(4, 0);

        //String choosenCategory = this.selected.get(0).getCategory();
        for (int i = 0; i < 4; i++) {
            guessesPer.put(this.selected.get(i).getDifficulty(), guessesPer.get(this.selected.get(i).getDifficulty()) + 1);
        }

        int keyWithMaxValue = 0;
        Integer maxValue = 0;

        for (int i = 1; i <= 4; i++) {
            int currentCount = guessesPer.get(i);
            if (currentCount > maxValue) {
                maxValue = currentCount;  // Update maxValue to the current highest count
                keyWithMaxValue = i;      // Track the key with the highest count
            }
        }

        // Return based on the max count found
        if (maxValue < 3) {
            return 0;  // Less than three of any category
        } else if (maxValue == 3) {
            return 1;  // Exactly three of one category
        } else {
            return 2;       // Four of one category
        }
    }

    /**
     * Give number of tiles selected
     * @return size of selected list
     */
    public int getNumSelected(){
        return selected.size();
    }

    /**
     * Clears the list of selected tiles
     */
    public void clearSelected() {
        this.selected.clear();
    }

    /**
     * Getter for selected tiles
     * @return tiles
     */
    public ArrayList<Tile> getSelected() {
        return selected;
    }

    /**
     * Gets the level of the game
     * @return level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Gets the list of words
     * @return words
     */
    public ArrayList<Tile> getWords() {
        return words;
    }
}