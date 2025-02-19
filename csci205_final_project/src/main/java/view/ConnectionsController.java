/* *****************************************
 * CSCI 205 - Software Engineering and Design
 * Spring 2024
 * Instructor: Prof. Lily Romano / Prof. Joshua Stough
 *
 * Name: Owen Reilly
 * Section: 2pm
 * Date: 4/17/24
 * Time: 1:35 PM
 *
 * Project: csci205_final_project
 * Package: view
 * Class: ConnectionsController
 *
 * Description:
 *
 * ****************************************
 */
package view;


import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.ConnectionsModel;
import model.Level;
import model.Tile;

import java.io.FileNotFoundException;

public class ConnectionsController {
    private final Scene gameBoardScene;
    private ConnectionsModel theModel;
    private ConnectionsView theView;
    private Stage primaryStage;


    /**
     * Our controller
     *
     * @param theModel
     * @param theView
     *
     * @author Owen R
     */
    public ConnectionsController(ConnectionsModel theModel, ConnectionsView theView, Stage primaryStage){
        this.theModel = theModel;
        this.primaryStage = primaryStage;
        this.theView = theView;

        gameBoardScene = new Scene(theView.getGamePlayRoot());
        gameBoardScene.getStylesheets().add(
                getClass().getResource("/GameScreen.css")
                        .toExternalForm());

        initEventHandlers();
    }

    /**
     * Method for going back to the home screen from the gameplay screen, used
     * if the player loses or if they just want to switch difficulties. The user
     * hits a return button to invoke this method
     * @author - Casey King, Mikey Myro
     */
    private void switchToHomeScreen() {
        theModel.reset();
        theView.reset();

        Scene homeScene = new Scene(theView.getHomeScreenRoot());
        primaryStage.setScene(homeScene);
        homeScene.getStylesheets().add(
                getClass().getResource("/ConnectionsHomeScreen.css")
                        .toExternalForm());

        // Reset the categories guessed to 0 when the player exits the level
        theView.listOfCategoriesGuessed.clear();
        initEventHandlers();
    }

    /**
     * Binds all the buttons in the view to a specific action
     * each action chooses a level that we have in the model.
     *
     * @author Owen R, Mikey Myro
     */
    private void initEventHandlers() {
        /// initialize level selector buttons
        this.theView.btnEasy.setOnAction(e -> switchToGameBoard(Level.EASY));
        this.theView.btnMedium.setOnAction(e -> switchToGameBoard(Level.MEDIUM));
        this.theView.btnHard.setOnAction(e -> switchToGameBoard(Level.HARD));
        this.theView.btnExtreme.setOnAction(e -> switchToGameBoard(Level.EXTREME));
        this.theView.btnHollywood.setOnAction(e -> switchToGameBoard(Level.HOLLYWOOD));

        // set go back button to return to home screen
        theView.getGoBackButton().setOnAction(e -> switchToHomeScreen());

        // Check selected tiles
        theView.getCheckSelectedButton().setOnAction(e -> {
            int result = theModel.guess();

            theView.showFeedback(result);
        });

        // set shuffle button to shuffle buttons
        theView.getShuffleButton().setOnAction(e -> {
            theView.shuffleButtons();
        });
    }

    /**
     * Switch the window to the appropriate level game board
     * showing the new window
     * @param level - difficulty the user chooses to play
     *
     * @author Mikey M
     */
    private void switchToGameBoard(Level level) {
        // Choose level based on what was clicked
        theModel.chooseLevel(level);

        // Create the game board scene
        try {
            theView.initGamePlayRoot();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        // initialize bindings for squares
        initGameBoardBindings();

        // Set the scene in the primary stage
        primaryStage.setScene(gameBoardScene);
        primaryStage.setTitle("Connections");
        primaryStage.show();
    }

    /**
     * Create all bindings for the game board, adding to the tiles color properties
     * and creating the tiles and rectangles
     *
     * @author Casey K
     */
    private void initGameBoardBindings() {

        // Bind tiles to selecting Tile
        for(int i = 0; i < theView.getListOfSelectableWords().size(); i++){
                final int row = i;
                Tile tile = theModel.getBoard().getWords().get(i);
                StackPane wordTile = theView.getListOfSelectableWords().get(i);
                Rectangle rect = (Rectangle) wordTile.getChildren().get(0);
                rect.fillProperty().bind(tile.currentColorProperty());
                wordTile.onMouseClickedProperty().setValue(event -> theModel.getBoard().select(row));
        }
    }

}