/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2024
 * Instructor: Prof. Lily Romano
 *
 * Name: Casey King
 * Section: 1:00
 * Date: 4/10/24
 * Time: 1:46
 *
 * Project: csci205_final_project
 * Package: model
 * Class: Tile
 *
 * Description:
 *
 * ****************************************
 */
package model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;

/**
 * Tile class, makes up the 16 words in Connections
 * @author Casey King
 */
public class Tile {

    /** Word stored in tile */
    private String word;

    /** Is the tile selected? */
    private SimpleBooleanProperty selected;

    /** Category */
    private String category;

    /**
     * Color when Tile is selected
     */
    private Color selectedColor;

    /**
     * Color when Tile is unselected
     */
    private Color unselectedColor;


    /**
     * This will help us later, as far as determining what our color should be.
     */
    private int difficulty;


    /**
     * Color that the tile is in game
     */
    private SimpleObjectProperty<Color> currentColor;


    /**
     * Tile constructor, initializes attributes
     * @param word word tile carries
     * @param category category word is in
     * @author Casey King
     */
    public Tile(String word, String category, int difficulty){
        this.word = word;
        this.category = category;
        this.selected = new SimpleBooleanProperty(false);
        this.difficulty = difficulty;
        this.selectedColor = Color.LAWNGREEN;
        this.unselectedColor = Color.ANTIQUEWHITE;
        this.currentColor = new SimpleObjectProperty<>();
        this.currentColor.set(unselectedColor);
    }

    /**
     * Returns whether Tile is selected
     * @return isSelected
     */
    public boolean isSelected() {
        return selected.get();
    }

    /**
     * Returns whether Tile is selected
     * @return selected
     */
    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    /**
     * Return category of word
     * @return category
     * @author Casey King
     */
    public String getCategory() {
        return category;
    }

    /**
     * Get the current color of the Tile
     * @return currentColor
     */
    public Color getCurrentColor() {
        return currentColor.get();
    }

    /**
     * Get the current color of the tile
     * @return currentColor
     */
    public SimpleObjectProperty<Color> currentColorProperty() {
        return currentColor;
    }

    /**
     * Select or unselect tile
     * @author Casey King
     */
    public void select(){
        this.selected.set(!this.isSelected());
        if(this.isSelected()){
            this.currentColor.set(selectedColor);
        }
        else {
            this.currentColor.set(unselectedColor);
        }
    }

    /**
     * Prints word of Tile
     * @return word
     */
    public String toString() {
        return(word);
    }

    /**
     * Equals method to check if Tile is in an ArrayList
     * @param obj to compare
     * @return if they are equal
     */
    public boolean equals(Object obj) {
        return obj.toString().equals( this.toString());
    }

    /**
     * Returns the difficulty of the Tile
     * @return difficulty
     */
    public int getDifficulty() {
        return this.difficulty;
    }

    /**
     * Returns stored word
     * @return word
     * @author Casey King
     */
    public String getWord() {
        return word;
    }
}