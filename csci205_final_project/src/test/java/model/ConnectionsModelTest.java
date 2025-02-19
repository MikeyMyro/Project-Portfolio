/* ****************************************
CSCI 205 - Software Engineering and Design
Spring 2024
Instructor: Prof. Lily Romano / Prof. Joshua Stough
Name: Mikey Myro: 1 PM Lecture 3PM Lab Date: 4/15/24
Time: 1:19 PM
Project: csci205_final_project Package: model Class: ConnectionsModelTest
Description:
*
*
*
* 
* 
*
*
*
*
*
*
*
*
*
*
* **************************************** 
*/
package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionsModelTest {

    private ConnectionsModel connectionsModel;

    @BeforeEach
    /**
     * @author Mikey M
     */
    void setUp() {
        connectionsModel = new ConnectionsModel();
        connectionsModel.chooseLevel(Level.EASY);
    }

    @Test
    /**
     * Ensure that when the user chooses a level to play, the correct game board is created
     * with the tiles from that difficulty level
     * @author Mikey M
     */
    void chooseLevel() {
        assertTrue(connectionsModel.isInGame());
        assertNotNull(connectionsModel.getBoard());
    }

    @Test
    /**
     * Ensure that the guess method is working correctly, meaning that
     * if a user makes a guess it does not crash the game
     * @author Mikey M
     */
    void guess() {
        connectionsModel.getBoard().select(0);
        connectionsModel.getBoard().select(1);
        connectionsModel.getBoard().select(2);
        connectionsModel.getBoard().select(3);

        // Make a guess
        connectionsModel.guess();



        // Check if the game is still in progress
        assertTrue(connectionsModel.isInGame());
    }

    }
