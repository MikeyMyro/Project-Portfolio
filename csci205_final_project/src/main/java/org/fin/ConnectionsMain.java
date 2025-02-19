package org.fin;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ConnectionsModel;
import view.ConnectionsController;
import view.ConnectionsView;

/**
 * Main Class for the Connections game, the JavaFX class to show the home screen and the gameplay screen
 */
public class ConnectionsMain extends Application {

    /** Model object for the home screen */
    private ConnectionsModel theModel;

    /** View object for the home screen */
    private ConnectionsView theView;

    /**
     * Main model to start the game
     */
    private ConnectionsController theController;
    public static void main(String[] args) {launch(args);}


    /**
     * Start the view, initializing all JavaFX objects and launching the window
     * @param primaryStage - Primary Stage object
     * @author - Jonas Scott, Mikey Myro
     */
    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(theView.getHomeScreenRoot());

        // Attach a CSS file for styling
        scene.getStylesheets().add(
                getClass().getResource("/ConnectionsHomeScreen.css")
                        .toExternalForm());
        this.theController = new ConnectionsController(this.theModel, this.theView, primaryStage);

        primaryStage.setTitle("Connections");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    /**
     * Init method to initialize the model and view objects for the Connections game
     * @throws Exception
     * @author - Jonas Scott
     */
    @Override
    public void init() throws Exception {
        super.init();
        this.theModel = new ConnectionsModel();
        this.theView = new ConnectionsView(this.theModel);
    }
}
