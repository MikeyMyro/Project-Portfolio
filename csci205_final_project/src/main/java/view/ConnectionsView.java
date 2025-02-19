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
 * Package: view
 * Class: ConnectionsView
 *
 * Description:
 *
 * ****************************************
 */
package view;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import model.ConnectionsModel;
import model.Level;
import model.Tile;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;

public class ConnectionsView {

    /**
     * Model object for the ConnectionsModel Class
     */
    private ConnectionsModel theModel;

    /**
     * Root object for the Connection game screen
     */
    private GridPane gamePlayRoot;

    /**
     * Root object for the home screen
     */
    private VBox homeScreenRoot;

    /**
     * All Buttons
     */
    public Button btnEasy, btnMedium, btnHard, btnExtreme, btnHollywood, checkSelectedButton, shuffleButton, goBackButton;
    public ArrayList<StackPane> listOfSelectableWords;
    public ArrayList<StackPane> listOfCategoriesGuessed;
    private HBox ballDisplay;
    private Label notificationLabel; // The notification label


    /**
     * Constructor for ConnectionsView class
     *
     * @author - Jonas Scott
     */
    public ConnectionsView(ConnectionsModel theModel) {
        this.theModel = theModel;
        initSceneGraph();
        gamePlayRoot = new GridPane();
        gamePlayRoot.getStyleClass().add("grid");
        listOfCategoriesGuessed = new ArrayList<>();
    }

    /**
     * Init method to initialize all objects in the scene graph
     * adding the title as well as the buttons for selecting difficulty
     *
     * @author - Jonas Scott, Mikey Myro
     */
    private void initSceneGraph() {
        this.homeScreenRoot = new VBox();
        Text title = new Text("Welcome to Connections \n Select your Difficulty");
        title.getStyleClass().add("title");

        this.homeScreenRoot.getChildren().add(title);
        this.homeScreenRoot.setMinWidth(1000);
        this.homeScreenRoot.setMinHeight(800);

        initializeEverything();

    }


    /**
     * Add the home buttons for the difficulties on the home screen and initialize rest of game
     *
     * @author Casey K, Mikey M
     */
    private void initializeEverything() {
        btnEasy = new Button("Easy");

        btnMedium = new Button("Medium");

        btnHard = new Button("Hard");

        btnExtreme = new Button("Extreme");

        btnHollywood = new Button("Hollywood");

        checkSelectedButton = new Button("Submit");

        shuffleButton = new Button("Shuffle");

        goBackButton = new Button("Return");

        listOfSelectableWords = new ArrayList<>();

        ballDisplay = new HBox(10);
        ballDisplay.setAlignment(Pos.BOTTOM_LEFT);

        notificationLabel = new Label();
        notificationLabel.getStyleClass().add("notificationText");

        this.homeScreenRoot.getChildren().addAll(btnEasy, btnMedium, btnHard, btnExtreme, btnHollywood);
    }


    /**
     * The main screen for the gameplay. It puts up a whole board of rectangles with
     * all the tiles from our level on there. we use our Enumerator.
     *
     * @throws FileNotFoundException for the game modes with photos in them, in case the URL does not exist
     * (this will never throw for now, since each URL is manually inputted in)
     *
     * @author Casey K, Owen R, Jonas S, Mikey M
     */
    public void initGamePlayRoot() throws FileNotFoundException{

        StackPane newWordTile;
        int yPos = 0;
        for(int i = 0; i < theModel.getBoard().getWords().size(); i++){
            Rectangle rect = new Rectangle(100,60);
            if (this.theModel.getBoard().getLevel() == Level.HOLLYWOOD){
                newWordTile = makeImageTile(i, rect);
            }
            else {
                Text text = new Text(this.theModel.getBoard().getWords().get(i).getWord());
                text.setTextAlignment(TextAlignment.CENTER);
                newWordTile = new StackPane(rect, text);
            }

            this.gamePlayRoot.add(newWordTile, i%4, yPos/4);
            listOfSelectableWords.add(newWordTile);
            yPos++;
        }

        updateGuessCounter(theModel.userFeedback());

        // Re add all the gameplay buttons after clearing the board
        addGameplayButtons();

    }

    /**
     * this is for the Hollywood difficulty, as well as any future difficulties with photos
     * rather than printing the word onto the tile, it pops up a url
     *
     * @param i the index of our word in the word array
     * @param rect the rectangle object that will turn into our tile
     * @return the final tile that will be presented. It is a rectangle
     * that has the photo on it, with all the tile data encapsulated in it
     *
     * @author Owen R
     */
    private StackPane makeImageTile(int i, Rectangle rect){
        StackPane newWordTile;
        String imageUrl = this.theModel.getBoard().getWords().get(i).getWord();
        Image image = new Image(imageUrl, true);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        imageView.setId(imageUrl);
        imageView.setFitHeight(rect.getHeight());
        imageView.setFitWidth(rect.getWidth());
        imageView.setPreserveRatio(true);

        newWordTile = new StackPane(rect, imageView);
        newWordTile.setAlignment(Pos.CENTER);
        return newWordTile;
    }


    /**
     * This replaces the original shuffle method that we had in the model
     * Rather than shuffling the list behind the scenes, we shuffle all the words after being added to the screen
     *
     * @Author Owen R, Casey K
     */
    public void shuffleButtons() {
        gamePlayRoot.getChildren().clear();
        addCategories();
        Collections.shuffle(listOfSelectableWords);
        addWords();

        // Re add all the gameplay buttons after clearing the board
        addGameplayButtons();

    }

    /**
     * just add our categories into our list
     * @author Jonas S
     */
    private void addCategories() {
        for(int i = 0; i < listOfCategoriesGuessed.size(); i++) {
            StackPane catLbl = listOfCategoriesGuessed.get(i);
            gamePlayRoot.add(catLbl, 0, i);
        }
    }


    /**
     * A very strong Method. This gathers in the data from our model.guess() method and
     * determines what will be outputted on the screen
     *
     *
     * @param result 1 means we are 1 away, 2 means we got it correct,
     *               3 means we lost the game
     *               4 means we won the game
     *               all of these have their own specific action
     */
    public void showFeedback(int result) {
        if (result == 1 || result == 0){
            shakeSelectedTiles();
            if (result == 1) {
                initNotificationLabel(14);
                messagePopUp("One Away!");
            }
        }
        else if (result == 2) {
            reLayoutGamePlayRoot();
            theModel.getBoard().clearSelected();
        }
        else if (result == 3){
            // game is lost
            // make it so the remaining categories are placed as should be
            initNotificationLabel(30);
            messagePopUp("You lost!");
            showLosingScreen();
        }
        else if (result == 4){
            reLayoutGamePlayRoot();
            initNotificationLabel(30);
            messagePopUp("You won!");
            this.gamePlayRoot.getChildren().remove(shuffleButton);
            this.gamePlayRoot.getChildren().remove(checkSelectedButton);
        }

        else if (result == 5){
            messagePopUp("Already Guessed!");
        }

        updateGuessCounter(theModel.userFeedback());
    }

    /**
     * Reconfigure the layout of the tiles on the game board when
     * the user gets a category right, properly moving the tiles around
     * to maintain the grid pattern. Also creates the correct category rectangle
     * to tell the user what the category was that they just got right
     * @author - Casey king, Owen Reilly, Jonas Scott, Mikey Myro
     */
    public void reLayoutGamePlayRoot() {

        // Reset root
        this.gamePlayRoot.getChildren().clear();

        addCategories(); // Add the previous categories
        addNewCategory(); // add the new category

        ArrayList<StackPane> newList = new ArrayList<>();


        // remake list without guessed
        for(StackPane wordTile : listOfSelectableWords) {
            String word;
            if (this.theModel.getBoard().getLevel() != Level.HOLLYWOOD){
            Text text = (Text) wordTile.getChildren().get(1);
            word = text.getText();
            }
            else{
                word = wordTile.getChildren().get(1).getId();
            }
            if(!checkIfSelected(word)) {
                newList.add(wordTile);
            }
        }
        listOfSelectableWords = newList;

        addWords(); // Re add words
        addGameplayButtons(); // Re add buttons
    }

    /**
     * Display word tiles
     */
    private void addWords() {
        int yPos = listOfCategoriesGuessed.size() * 4;
        for(int i = 0; i < listOfSelectableWords.size(); i++){
            gamePlayRoot.add(listOfSelectableWords.get(i), i%4 , yPos/4);
            yPos++;
        }
    }


    /**
     *
     * this is where we adjust the board, so that the correct category is added to the top
     * the category attribute of the selected tiles determines the color of the box
     * and then the category is printed out, with the words underneath
     *
     * @Author Casey K, Jonas S
     *
     */
    private void addNewCategory() {
        String words; // Get all words
        String category = theModel.getBoard().getSelected().get(0).getCategory() + " ";
        if (this.theModel.getBoard().getLevel() != Level.HOLLYWOOD) {
            words = theModel.getBoard().getSelected().toString();
        }
        else {
            words = "";
        }
        Rectangle catRect = new Rectangle(440, 60);

        // Switch case, setting the color dependent on which difficulty of category the user got
        switch (this.theModel.getBoard().getSelected().get(0).getDifficulty()) {
            case (1): {
                catRect.setFill(Color.GREEN);
                break;
            }
            case (2): {
                catRect.setFill(Color.YELLOW);
                break;
            }
            case (3): {
                catRect.setFill(Color.INDIANRED);
                break;
            }
            case (4): {
                catRect.setFill(Color.MEDIUMPURPLE);
                break;
            }
        }
        Text catAndWords = new Text(category + words);
        catAndWords.setTextAlignment(TextAlignment.CENTER);
        StackPane catLbl = new StackPane(catRect, catAndWords);
        listOfCategoriesGuessed.add(catLbl); // Make label for category
        gamePlayRoot.add(catLbl, 0, listOfCategoriesGuessed.size() - 1, 4, 1);
    }


    /**
     * Add the ball counter to the bottom of the grid
     * add the appropriate number of balls for how many guesses are remaining
     * @param count - the number of guesses remaining
     * @author - Owen Reilly
     */
    public void updateGuessCounter(int count){
        ballDisplay.getChildren().clear();
        for (int i = 0; i < count; i++) {
            Circle circle = new Circle(7, Color.RED);  // Create a new circle (ball) with radius 7
            ballDisplay.getChildren().add(circle);  // Add the circle to the display box
        }


    }

    /**
     * Checks if the word is in the list of selected words
     * @param word that we will check
     * @return true if it is, false if not
     *
     * @author casey K
     */
    public boolean checkIfSelected(String word) {
        // Check if each tile is selcted or not
        for(Tile tile : this.theModel.getBoard().getSelected()) {
            if (tile.getWord().equals(word)) {
                return true;
            }
        }
        return false;
    }



    /**
     * screen that comes up when we lose, showing the player the
     * correct categories and then offering them the chance to go back
     * to the home screen and play again
     * @author Jonas Scott
     */
    public void showLosingScreen(){
        gamePlayRoot.getChildren().remove(shuffleButton);
        gamePlayRoot.getChildren().remove(checkSelectedButton);
        gamePlayRoot.getChildren().remove(ballDisplay);
    }


    /**
     * Formulate the pop-up message if the player has won or lost, fading in
     * for a short time and then disappearing again
     * @param message - the message to fade in and out, either win, lose, or one away
     * @author - Owen Reilly
     */
    public void messagePopUp(String message) {
        // Set up message
        notificationLabel.setText(message);
        notificationLabel.setVisible(true);
        notificationLabel.setOpacity(0);

        // set up transition
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), notificationLabel);
        fadeIn.setToValue(1);  // Fade in to fully opaque

        PauseTransition stayVisible = new PauseTransition(Duration.seconds(2));  // Stay visible for 2 seconds

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), notificationLabel);
        fadeOut.setToValue(0);  // Fade out to fully transparent

        SequentialTransition sequence = new SequentialTransition(fadeIn, stayVisible, fadeOut);
        sequence.setOnFinished(event -> notificationLabel.setVisible(false));  // Hide after animation
        sequence.play();
    }

    public void shakeSelectedTiles() {
        // Move every tile selected if wrong
        for (Tile tile : theModel.getBoard().getSelected()) {
            listOfSelectableWords.stream()
                    .filter(sp -> sp.getChildren().size() > 1)
                    .filter(sp -> {
                        if (theModel.getBoard().getLevel() == Level.HOLLYWOOD) {
                            return sp.getChildren().get(1).getId().equals(tile.getWord());
                        } else {
                            Text text = (Text) sp.getChildren().get(1);
                            return text.getText().equals(tile.getWord());
                        }
                    })
                    .forEach(this::applyShakeAnimation);
        }
    }

    /**
     * Applies a shaking animation to a given StackPane.
     * @param stackPane the StackPane to animate
     */
    private void applyShakeAnimation(StackPane stackPane) {
        // Make animation
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), stackPane);
        translateTransition.setFromX(0);
        translateTransition.setToX(10);
        translateTransition.setCycleCount(6);
        translateTransition.setAutoReverse(true);
        translateTransition.play();
    }

    /**
     * Creation and sizing for the notification label that pops in and out,
     * larger sized text for winning and losing
     * @param size - the font size of the message
     * @author - Owen Reilly
     */
    private void initNotificationLabel(int size) {
        //Size corresponding to parameter
        notificationLabel.setFont(Font.font(size));
        notificationLabel.setVisible(false);  // Start hidden
    }

    /**
     * getter for go back button
     * @return goBackButton
     */
    public Button getGoBackButton() {
        return goBackButton;
    }

    /**
     * Clears the game play root node and initializes the scene graph for the game.
     * This method is typically used to reset the game to its initial state.
     * @author Mikey Myro
     */
    public void reset() {
        // Clear root and return to home
        gamePlayRoot.getChildren().clear();
        initSceneGraph();
    }

    /**
     * Add the gameplay buttons and ball display after clearing the board
     * @author - Jonas Scott
     */
    private void addGameplayButtons() {

        // Add all gameplay elements
        this.gamePlayRoot.add(ballDisplay, 0, 5);
        this.gamePlayRoot.add(checkSelectedButton, 1, 5, 2, 1);
        this.gamePlayRoot.add(shuffleButton, 2, 5);
        this.gamePlayRoot.add(goBackButton, 3, 5);
        this.gamePlayRoot.add(notificationLabel, 0,5, 2,1);

    }

    /**
     * getter for checkSelectedButton
     * @return checkSelectedButton
     */
    public Button getCheckSelectedButton() {
        return checkSelectedButton;
    }

    /**
     * getter for shuffleButton
     * @return shuffleButton
     */
    public Button getShuffleButton() {
        return shuffleButton;
    }

    /**
     * Getter for the root of the Home Screen
     */
    public VBox getHomeScreenRoot() {
        return homeScreenRoot;
    }

    /**
     * Getter for the gamePlayRoot
     */
    public Parent getGamePlayRoot() {
        return gamePlayRoot;
    }

    /**
     * Get the list of the still selectable words, used after a player gets the category right
     */
    public ArrayList<StackPane> getListOfSelectableWords() {
        return listOfSelectableWords;
    }
}
