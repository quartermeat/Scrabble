/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scrabble.Controller;

import com.scrabble.Model.Bag;
import com.scrabble.Model.Board;
import com.scrabble.Model.GameState;
import com.scrabble.Model.LetterTile;
import com.scrabble.Model.Player;
import com.scrabble.Model.ScoredWord;
import com.scrabble.Model.Space;
import com.scrabble.Model.SpaceButton;
import com.scrabble.Model.WordList;
import com.scrabble.View.BoardWindow;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Nachay
 */
public class ScrabbleMain {

    /////Model objects///////////////////
    private WordList wordList;
    private final Bag bag;
    private final Board gameBoard;
    private Player currentPlayer;
    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;
    private int numPlayers = 0;
    private int playerTurn = 0;
    private final GameState gameState;

    ///END MODEL OBJECTS//////////////////
    ////View objects////////////////////
    private final BoardWindow boardWindow;

    /**
     *
     */
    public ScrabbleMain() {

        ////initialize MODEL and VIEW////////////////
        ////MODEL//////////////////////////////////////
        //try to initialize wordlist from text file
        try {
            this.wordList = new WordList();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Scrabble.class.getName()).log(Level.SEVERE, null, ex);
        }//end try/catch

        //initialize bag
        bag = new Bag();

        //initialize game board
        gameBoard = new Board();

        //setup gamestate
        gameState = new GameState(GameState.GAME_STARTED);

        ////END MODEL//////////////////////////////////
        ////VIEW//////////////////////////////////////
        boardWindow = new BoardWindow();
        boardWindow.initBasicComponents(gameBoard);

        boardWindow.setChallengeButtonEnabled(false);

        boardWindow.setVisible(true);

    }//end instructions

    //ACTIONS PERFORMED////////////////////////////////////////

    /**
     *
     */
        public void startNewGameButtonPressed() {
        boardWindow.addStartNewGameButtonActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here//////////

            //MODEL changes////////////////
            //change gamestate
            gameState.setGameState(GameState.NO_TILE_SELECTED);

            //Prompt User for number of players
            JFrame frame = new JFrame("UserInput");
            frame.setAlwaysOnTop(true);
            String numPlayersString = "empty";
            //Prompt for number of players a number 2 - 4 is entered
            while (!numPlayersString.equals("2") && !numPlayersString.equals("3") && !numPlayersString.equals("4")) {
                numPlayersString = JOptionPane.showInputDialog(frame, "How many players? (2-4)");
            }//end while

            numPlayers = Integer.parseInt(numPlayersString);

            //String used for player name in next loop
            String playerName = "";

            if (numPlayers == 2) {
                frame.toFront();
                frame.repaint();
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 1");
                player1 = new Player(bag, playerName);
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 2");
                player2 = new Player(bag, playerName);
            } else if (numPlayers == 3) {
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 1");
                player1 = new Player(bag, playerName);
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 2");
                player2 = new Player(bag, playerName);
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 3");
                player3 = new Player(bag, playerName);
            } else {
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 1");
                player1 = new Player(bag, playerName);
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 2");
                player2 = new Player(bag, playerName);
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 3");
                player3 = new Player(bag, playerName);
                playerName = JOptionPane.showInputDialog(frame, "Enter name of player 4");
                player4 = new Player(bag, playerName);
            }

            //set playerTurn to 1 for first player and set currentPlayer to 1
            playerTurn = 1;
            currentPlayer = player1;

            ////End Model Changes//////////
            //View changes/////////////////
            //set players rack up
            boardWindow.updateRack(currentPlayer);
            //disable new game button
            boardWindow.setStartNewGameButton1Enabled(false);

            ////End View Changes///////////
        } //end actionListener
        );//end startNewGameButtonPressed
    }//end startNewGameButtonPressed()

    /**
     *
     */
    public void challengeButtonPressed() {
        boardWindow.addChallengeButtonActionListener((java.awt.event.ActionEvent evt) -> {

            String[] words = new String[gameState.getWordsScoredLastTurn().size()];
            words = gameState.getWordsScoredLastTurn().toArray(words);

            int response = JOptionPane.showOptionDialog(boardWindow, "Select The word you want to challenge.", "Challenge Word",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                    null, words, words[0]);

            if (wordList.challenge(words[response])) {
                JOptionPane.showMessageDialog(boardWindow,
                        words[response] + " is in the wordlist! Challenge Invalid!");
                boardWindow.setChallengeButtonEnabled(false);
            } else if (!wordList.challenge(words[response])) {
                JOptionPane.showMessageDialog(boardWindow,
                        words[response] + " is not in the wordlist! Challenge Valid!");

                //basically have to undo last turn here.... ugh!!!!!
                undoLastTurn();
            }//end if/else if

        } //end ActionListener
        );//end method parameters
    }//end challenge button pressed

    /**
     *
     */
    public void endTurnButtonPressed() {
        boardWindow.addEndTurnButtonActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here//////////

            //MODEL changes////////////////
            //Calculate score of played tiles
            int turnScore = 0;

            ScoredWord playedSpaces = new ScoredWord(gameState.getPlayedSpaces(), gameState.getOrientation());
            gameState.addScoredWords(playedSpaces, gameBoard);

            boolean wordModifierDetected = false;
            ArrayList<Integer> scoreModifiers = new ArrayList<>();

            for (ScoredWord currentWord : gameState.getScoredWords()) {

                /* TODO: get the array list of spaces and add up score of the tiles*/
                for (Space currentSpace : currentWord) {
                    //if currentSpace has a score modifier
                    if (currentSpace.getScoreModifier() != 0) {
                        switch (currentSpace.getScoreModifier()) {
                            case Board.DOUBLELETTERSCOREMODIFIER: {
                                System.out.println("DLS detected for " + currentSpace.getAssignedTile().getLetter());
                                turnScore += currentSpace.getAssignedTile().getPoints() * 2;
                                break;
                            }//end case
                            case Board.TRIPLELETTERSCOREMODIFIER: {
                                System.out.println("TLS detected for " + currentSpace.getAssignedTile().getLetter());
                                turnScore += currentSpace.getAssignedTile().getPoints() * 3;
                                break;
                            }//end case
                            case Board.DOUBLEWORDSCOREMODIFIER: {
                                System.out.println("DWS detected for " + currentSpace.getAssignedTile().getLetter());
                                turnScore += currentSpace.getAssignedTile().getPoints();
                                wordModifierDetected = true;
                                scoreModifiers.add(2);
                                break;
                            }//end case
                            case Board.TRIPLEWORDSCOREMODIFIER: {
                                System.out.println("TWS detected for " + currentSpace.getAssignedTile().getLetter());
                                turnScore += currentSpace.getAssignedTile().getPoints();
                                wordModifierDetected = true;
                                scoreModifiers.add(3);
                                break;
                            }//end case
                        }//end switch
                        //turn off modifier, cannot be used again
                        gameBoard.spaces.get(currentSpace.getIndex()).setScoreModifier(0);
                    } else if (currentSpace.getScoreModifier() == 0) {
                        System.out.println("No modifier space scored.");
                        turnScore += currentSpace.getAssignedTile().getPoints();
                    }//end if/else                
                }//end for

                //if a word modifier was detected
                if (wordModifierDetected) {
                    //for each modifier used
                    for (Integer modifier : scoreModifiers) {
                        //multiply score by modifier
                        turnScore = turnScore * modifier;
                    }//end for
                }//end if

                //Add that turnscore to current player's score
                currentPlayer.addScore(turnScore);

            }//end for

            System.out.println("Spaces Played this turn:");
            for (Space space : gameState.getPlayedSpaces()) {
                System.out.println("    " + space);
            }//end for

            System.out.println("Words scored this turn:");

            for (ScoredWord wordList : gameState.getScoredWords()) {
                for (Space space : wordList) {
                    System.out.print(space.getAssignedTile().getLetter());
                }//end for
                System.out.println("");
            }//end for

            System.out.println("*************END of TURN**************");

            //clear playedSpaces and scoredWords
            gameState.endTurn();

            //Refill current players rack to 7
            while (currentPlayer.getRack().size() < 7 && bag.size() > 0) {
                currentPlayer.addTile(bag);
            }

            //Determine who's turn it should be using current value of playerTurn
            //If it was player 1's turn
            if (playerTurn == 1) {
                //change rack to player2
                boardWindow.updateRack(player2);
                //Change playerTurn to 2 and set current player to 2
                playerTurn = 2;
                currentPlayer = player2;
            } //If it was player 2's turn
            else if (playerTurn == 2) {
                //change rack to player1
                boardWindow.updateRack(player1);
                //Change playerTurn to 1 and set current player to 2
                playerTurn = 1;
                currentPlayer = player1;
            }

            //reset space validity
            //for each space on the game board
            for (Space space : gameBoard.spaces) {
                if (gameBoard.getValidSpaces(gameState).contains(space)) {
                    space.setValidity(true);
                } else {
                    space.setValidity(false);
                }//end if/else
            }//end for
            ////End Model Changes//////////

            //View changes/////////////////
            //Update Player scores
            boardWindow.setPlayer1Score(player1.getScore());
            boardWindow.setPlayer2Score(player2.getScore());
            //set players rack up
            boardWindow.updateRack(currentPlayer);
            //disable new game button
            boardWindow.setStartNewGameButton1Enabled(false);

            //enable challenge button
            boardWindow.setChallengeButtonEnabled(true);

            //update board view
            boardWindow.updateBoard(gameBoard);

            ////End View Changes///////////
        } //end actionListener
        );//end startNewGameButtonPressed
    }//end startNewGameButtonPressed()

    /**
     *
     */
    public void rackButton1Pressed() {
        boardWindow.addRackButton1ActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here
            //if rack tile is selected, unselect it and enable other buttons
            if (gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                //model changes
                gameState.setGameState(GameState.NO_TILE_SELECTED);
                currentPlayer.setSelectedRackButton(null);

                //view changes
                //Change background color back to orange
                boardWindow.setRackButton1bg(java.awt.Color.orange);
                boardWindow.setRackButton2Enabled(true);
                boardWindow.setRackButton3Enabled(true);
                boardWindow.setRackButton4Enabled(true);
                boardWindow.setRackButton5Enabled(true);
                boardWindow.setRackButton6Enabled(true);
                boardWindow.setRackButton7Enabled(true);

                boardWindow.showInstructions(false);

                boardWindow.setCursor(Cursor.getDefaultCursor());

            } else {//if rack tile is not selected

                //Model changes//////////////
                gameState.setGameState(GameState.RACK_TILE_SELECTED);
                currentPlayer.setSelectedRackButton((SpaceButton) evt.getSource());

                for (Space currentSpace : gameBoard.spaces) {
                    if (currentSpace.isOccupied()) {
                        currentSpace.setValidity(false);
                    }//end if
                }//end for

                ///End Model Changes/////////
                //View Changes///////////////
                //disable all rack buttons - change background color of selected
                boardWindow.setRackButton1bg(java.awt.Color.yellow);
                boardWindow.setRackButton2Enabled(false);
                boardWindow.setRackButton3Enabled(false);
                boardWindow.setRackButton4Enabled(false);
                boardWindow.setRackButton5Enabled(false);
                boardWindow.setRackButton6Enabled(false);
                boardWindow.setRackButton7Enabled(false);

                boardWindow.showInstructions(true);

                boardWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon("src/com/scrabble/view/Scrabble_icon.png").getImage(),
                        new Point(0, 0), "custom cursor"));

                boardWindow.updateBoard(gameBoard);
                ////End View Changes/////////
            }//end if/else
        } //end actionPerformed
        );//end addRackButton1ActionListener 
    }//end rackButton1pressed

    /**
     *
     */
    public void rackButton2Pressed() {
        boardWindow.addRackButton2ActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here
            //if rack tile is selected, unselect it and enable other buttons
            if (gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                //model changes
                gameState.setGameState(GameState.NO_TILE_SELECTED);
                currentPlayer.setSelectedRackButton(null);
                //view changes
                boardWindow.setRackButton1Enabled(true);
                boardWindow.setRackButton2bg(java.awt.Color.orange);
                boardWindow.setRackButton3Enabled(true);
                boardWindow.setRackButton4Enabled(true);
                boardWindow.setRackButton5Enabled(true);
                boardWindow.setRackButton6Enabled(true);
                boardWindow.setRackButton7Enabled(true);

                boardWindow.showInstructions(false);

                boardWindow.setCursor(Cursor.getDefaultCursor());

            } else {//if rack tile is not selected

                //Model changes//////////////
                gameState.setGameState(GameState.RACK_TILE_SELECTED);
                currentPlayer.setSelectedRackButton((SpaceButton) evt.getSource());

                for (Space currentSpace : gameBoard.spaces) {
                    if (currentSpace.isOccupied()) {
                        currentSpace.setValidity(false);
                    }//end if
                }//end for

                ///End Model Changes/////////
                //View Changes///////////////
                //disable all rack buttons - change background color of selected
                boardWindow.setRackButton1Enabled(false);
                boardWindow.setRackButton2bg(java.awt.Color.yellow);
                boardWindow.setRackButton3Enabled(false);
                boardWindow.setRackButton4Enabled(false);
                boardWindow.setRackButton5Enabled(false);
                boardWindow.setRackButton6Enabled(false);
                boardWindow.setRackButton7Enabled(false);

                boardWindow.showInstructions(true);

                boardWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon("src/com/scrabble/view/Scrabble_icon.png").getImage(),
                        new Point(0, 0), "custom cursor"));

                boardWindow.updateBoard(gameBoard);

                ////End View Changes/////////
            }//end if/else
        } //end actionPerformed
        );//end addRackButton1ActionListener 
    }//end rackButton1pressed

    /**
     *
     */
    public void rackButton3Pressed() {
        boardWindow.addRackButton3ActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here
            //if rack tile is selected, unselect it and enable other buttons
            if (gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                //model changes
                gameState.setGameState(GameState.NO_TILE_SELECTED);
                currentPlayer.setSelectedRackButton(null);

                //view Changes
                boardWindow.setRackButton1Enabled(true);
                boardWindow.setRackButton2Enabled(true);
                boardWindow.setRackButton3bg(java.awt.Color.orange);
                boardWindow.setRackButton4Enabled(true);
                boardWindow.setRackButton5Enabled(true);
                boardWindow.setRackButton6Enabled(true);
                boardWindow.setRackButton7Enabled(true);

                boardWindow.showInstructions(false);

                boardWindow.setCursor(Cursor.getDefaultCursor());

            } else {//if rack tile is not selected

                //Model changes//////////////
                gameState.setGameState(GameState.RACK_TILE_SELECTED);
                currentPlayer.setSelectedRackButton((SpaceButton) evt.getSource());

                for (Space currentSpace : gameBoard.spaces) {
                    if (currentSpace.isOccupied()) {
                        currentSpace.setValidity(false);
                    }//end if
                }//end for

                ///End Model Changes/////////
                //View Changes///////////////
                //disable all rack buttons - change background color of selected
                boardWindow.setRackButton1Enabled(false);
                boardWindow.setRackButton2Enabled(false);
                boardWindow.setRackButton3bg(java.awt.Color.yellow);
                boardWindow.setRackButton4Enabled(false);
                boardWindow.setRackButton5Enabled(false);
                boardWindow.setRackButton6Enabled(false);
                boardWindow.setRackButton7Enabled(false);

                boardWindow.showInstructions(true);

                boardWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon("src/com/scrabble/view/Scrabble_icon.png").getImage(),
                        new Point(0, 0), "custom cursor"));

                boardWindow.updateBoard(gameBoard);
                ////End View Changes/////////
            }//end if/else
        } //end actionPerformed
        );//end addRackButton1ActionListener 
    }//end rackButton1pressed

    /**
     *
     */
    public void rackButton4Pressed() {
        boardWindow.addRackButton4ActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here
            //if rack tile is selected, unselect it and enable other buttons
            if (gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                //model changes
                gameState.setGameState(GameState.NO_TILE_SELECTED);
                currentPlayer.setSelectedRackButton(null);

                //view changes
                boardWindow.setRackButton1Enabled(true);
                boardWindow.setRackButton2Enabled(true);
                boardWindow.setRackButton3Enabled(true);
                boardWindow.setRackButton4bg(java.awt.Color.orange);
                boardWindow.setRackButton5Enabled(true);
                boardWindow.setRackButton6Enabled(true);
                boardWindow.setRackButton7Enabled(true);

                boardWindow.showInstructions(false);

                boardWindow.setCursor(Cursor.getDefaultCursor());

            } else {//if rack tile is not selected

                //Model changes//////////////
                gameState.setGameState(GameState.RACK_TILE_SELECTED);
                currentPlayer.setSelectedRackButton((SpaceButton) evt.getSource());

                for (Space currentSpace : gameBoard.spaces) {
                    if (currentSpace.isOccupied()) {
                        currentSpace.setValidity(false);
                    }//end if
                }//end for

                ///End Model Changes/////////
                //View Changes///////////////
                //disable all rack buttons - change background color of selected
                boardWindow.setRackButton1Enabled(false);
                boardWindow.setRackButton2Enabled(false);
                boardWindow.setRackButton3Enabled(false);
                boardWindow.setRackButton4bg(java.awt.Color.yellow);
                boardWindow.setRackButton5Enabled(false);
                boardWindow.setRackButton6Enabled(false);
                boardWindow.setRackButton7Enabled(false);

                boardWindow.showInstructions(true);

                boardWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon("src/com/scrabble/view/Scrabble_icon.png").getImage(),
                        new Point(0, 0), "custom cursor"));

                boardWindow.updateBoard(gameBoard);
                ////End View Changes/////////
            }//end if/else
        } //end actionPerformed
        );//end addRackButton1ActionListener 
    }//end rackButton1pressed

    /**
     *
     */
    public void rackButton5Pressed() {
        boardWindow.addRackButton5ActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here
            //if rack tile is selected, unselect it and enable other buttons
            if (gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                /////model changes////////////////////
                //change gamestate
                gameState.setGameState(GameState.NO_TILE_SELECTED);
                //reset player's tile selected
                currentPlayer.setSelectedRackButton(null);

                ////view changes///////////////////////
                boardWindow.setRackButton1Enabled(true);
                boardWindow.setRackButton2Enabled(true);
                boardWindow.setRackButton3Enabled(true);
                boardWindow.setRackButton4Enabled(true);
                boardWindow.setRackButton5bg(java.awt.Color.orange);
                boardWindow.setRackButton6Enabled(true);
                boardWindow.setRackButton7Enabled(true);

                boardWindow.showInstructions(false);

                boardWindow.setCursor(Cursor.getDefaultCursor());
            } else {//if rack tile is not selected

                //Model changes//////////////
                //change gamestate
                gameState.setGameState(GameState.RACK_TILE_SELECTED);
                //change button selected
                currentPlayer.setSelectedRackButton((SpaceButton) evt.getSource());

                for (Space currentSpace : gameBoard.spaces) {
                    if (currentSpace.isOccupied()) {
                        currentSpace.setValidity(false);
                    }
                }//end for

                ///End Model Changes/////////
                //View Changes///////////////
                //disable all rack buttons - change background color of selected
                boardWindow.setRackButton1Enabled(false);
                boardWindow.setRackButton2Enabled(false);
                boardWindow.setRackButton3Enabled(false);
                boardWindow.setRackButton4Enabled(false);
                boardWindow.setRackButton5bg(java.awt.Color.yellow);
                boardWindow.setRackButton6Enabled(false);
                boardWindow.setRackButton7Enabled(false);

                boardWindow.showInstructions(true);

                boardWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon("src/com/scrabble/view/Scrabble_icon.png").getImage(),
                        new Point(0, 0), "custom cursor"));

                boardWindow.updateBoard(gameBoard);
                ////End View Changes/////////
            }//end if/else
        } //end actionPerformed
        );//end addRackButton1ActionListener 
    }//end rackButton1pressed

    /**
     *
     */
    public void rackButton6Pressed() {
        boardWindow.addRackButton6ActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here
            //if rack tile is selected, unselect it and enable other buttons
            if (gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                //model changes
                gameState.setGameState(GameState.NO_TILE_SELECTED);
                currentPlayer.setSelectedRackButton(null);

                //view changes
                boardWindow.setRackButton1Enabled(true);
                boardWindow.setRackButton2Enabled(true);
                boardWindow.setRackButton3Enabled(true);
                boardWindow.setRackButton4Enabled(true);
                boardWindow.setRackButton5Enabled(true);
                boardWindow.setRackButton6bg(java.awt.Color.orange);
                boardWindow.setRackButton7Enabled(true);

                boardWindow.showInstructions(false);

                boardWindow.setCursor(Cursor.getDefaultCursor());

            } else {//if rack tile is not selected

                //Model changes//////////////
                gameState.setGameState(GameState.RACK_TILE_SELECTED);
                currentPlayer.setSelectedRackButton((SpaceButton) evt.getSource());

                for (Space currentSpace : gameBoard.spaces) {
                    if (currentSpace.isOccupied()) {
                        currentSpace.setValidity(false);
                    }//end if
                }//end for

                ///End Model Changes/////////
                //View Changes///////////////
                //disable all rack buttons - change background color of selected
                boardWindow.setRackButton1Enabled(false);
                boardWindow.setRackButton2Enabled(false);
                boardWindow.setRackButton3Enabled(false);
                boardWindow.setRackButton4Enabled(false);
                boardWindow.setRackButton5Enabled(false);
                boardWindow.setRackButton6bg(java.awt.Color.yellow);
                boardWindow.setRackButton7Enabled(false);

                boardWindow.showInstructions(true);

                boardWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon("src/com/scrabble/view/Scrabble_icon.png").getImage(),
                        new Point(0, 0), "custom cursor"));

                boardWindow.updateBoard(gameBoard);
                ////End View Changes/////////
            }//end if/else
        } //end actionPerformed
        );//end addRackButton1ActionListener 
    }//end rackButton1pressed

    /**
     *
     */
    public void rackButton7Pressed() {
        boardWindow.addRackButton7ActionListener((java.awt.event.ActionEvent evt) -> {
            //handle event here
            //if rack tile is selected, unselect it and enable other buttons
            if (gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                //model changes
                gameState.setGameState(GameState.NO_TILE_SELECTED);
                currentPlayer.setSelectedRackButton(null);

                //view changes
                boardWindow.setRackButton1Enabled(true);
                boardWindow.setRackButton2Enabled(true);
                boardWindow.setRackButton3Enabled(true);
                boardWindow.setRackButton4Enabled(true);
                boardWindow.setRackButton5Enabled(true);
                boardWindow.setRackButton6Enabled(true);
                boardWindow.setRackButton7bg(java.awt.Color.orange);
                boardWindow.showInstructions(false);

                boardWindow.setCursor(Cursor.getDefaultCursor());

            } else {//if rack tile is not selected

                //Model changes//////////////
                gameState.setGameState(GameState.RACK_TILE_SELECTED);
                //set selected button in order to get it later
                currentPlayer.setSelectedRackButton((SpaceButton) evt.getSource());

                for (Space currentSpace : gameBoard.spaces) {
                    if (currentSpace.isOccupied()) {
                        currentSpace.setValidity(false);
                    }//end if
                }//end for

                ///End Model Changes/////////
                //View Changes///////////////
                //disable all rack buttons - change background color of selected
                boardWindow.setRackButton1Enabled(false);
                boardWindow.setRackButton2Enabled(false);
                boardWindow.setRackButton3Enabled(false);
                boardWindow.setRackButton4Enabled(false);
                boardWindow.setRackButton5Enabled(false);
                boardWindow.setRackButton6Enabled(false);
                boardWindow.setRackButton7bg(java.awt.Color.yellow);

                boardWindow.showInstructions(true);

                boardWindow.setCursor(Toolkit.getDefaultToolkit().createCustomCursor(
                        new ImageIcon("src/com/scrabble/view/Scrabble_icon.png").getImage(),
                        new Point(0, 0), "custom cursor"));

                boardWindow.updateBoard(gameBoard);
                ////End View Changes////////
            }//end if/else
        } //end actionPerformed
        );//end addRackButton1ActionListener 
    }//end rackButton1pressed

    /**
     *
     */
    public void spaceButtonPressed() {
        boardWindow.addSpaceButtonListActionListener((java.awt.event.ActionEvent evt) -> {

            //get the button that was pressed
            SpaceButton currentButton = (SpaceButton) evt.getSource();

            //get the space that was pressed
            Space currentSpace = currentButton.getAssignedSpace();

            //if a rack tile is selected and space does not have a tile assigned
            if (!currentSpace.isOccupied() && gameState.getGameState() == GameState.RACK_TILE_SELECTED) {
                //model stuff///////////////////////////////////////////////

                //get the tile that is changing position
                LetterTile currentTile = currentPlayer.getSelectedRackButton().getAssignedTile();

                //If the letter tile is a ?
                if (currentTile.getPoints() == 0) {

                    String newLetter = "empty";
                    //Until the user enters a letter
                    newLetter = JOptionPane.showInputDialog(boardWindow, "Enter the letter for the tile");

                    char letterChar = newLetter.charAt(0);
                    //assign new letter to the tile
                    currentTile.setLetter(letterChar);
                }

                //assign tile to space that is assigned to button
                currentSpace.setAssignedTile(currentTile);

                //set assigned space for tile layed down on board
                currentTile.setAssignedSpace(currentSpace);

                //change game state
                gameState.setGameState(GameState.NO_TILE_SELECTED);

                //keep track of spaces played this turn, add selected button's assigned space to played list
                gameState.addPlayedSpace(currentSpace);

                //remove the tile from players rack
                currentPlayer.removeTile(currentTile);

                //reset space validity
                //for each space on the game board
                for (Space space : gameBoard.spaces) {
                    if (gameBoard.getValidSpaces(gameState).contains(space)) {
                        space.setValidity(true);
                    } else {
                        space.setValidity(false);
                    }//end if/else
                }//end for

                //view stuff////////////////////////////////////////////////////
                //assign selected rack button's assigned tile to button
                currentButton.setAssignedTile(currentTile);

                //un select rack button
                //currentPlayer.setSelectedRackButton(null);
                //hide the instruction label
                boardWindow.showInstructions(false);

                //change cursor back to normal
                boardWindow.setCursor(Cursor.getDefaultCursor());

                //update board
                boardWindow.updateBoard(gameBoard);

                //update rack
                boardWindow.updateRack(currentPlayer);

                //if rack tile is not selected and space has an assigned tile
            } else if (currentSpace.isOccupied() && gameState.getGameState() == GameState.NO_TILE_SELECTED) {

                //MODEL CHANGES//////////////////////////////////////////
                LetterTile currentTile = currentSpace.getAssignedTile();

                //remove button's assigned tile from played tiles list
                gameState.removePlayedSpace(currentSpace);

                //add the tile back to the players rack
                currentPlayer.addSpecificTile(currentTile);

                //un select the button from player
                currentPlayer.setSelectedRackButton(null);

                //view changes
                //if this is a blank tile, change back to question mark
                if (currentTile.getPoints() == 0) {
                    currentTile.setLetter('?');
                }//end if

                //remove assigned space from tile assigned to this button
                currentTile.removeAssignedSpace();

                //remove assigned tile from button's assigned space
                currentSpace.removeAssignedTile();

                //reset space validity
                //for each space on the game board
                for (Space space : gameBoard.spaces) {
                    if (gameBoard.getValidSpaces(gameState).contains(space)) {
                        space.setValidity(true);
                    } else {
                        space.setValidity(false);
                    }//end if/else
                }//end for

                //remove assigned tile from button
                currentButton.removeAssignedTile();

                //re-enable all rack buttons
                boardWindow.setRackButton1Enabled(true);
                boardWindow.setRackButton2Enabled(true);
                boardWindow.setRackButton3Enabled(true);
                boardWindow.setRackButton4Enabled(true);
                boardWindow.setRackButton5Enabled(true);
                boardWindow.setRackButton6Enabled(true);
                boardWindow.setRackButton7Enabled(true);

                //hide the instructions
                boardWindow.showInstructions(false);

                //update rack view
                boardWindow.updateRack(currentPlayer);

                //update board view
                boardWindow.updateBoard(gameBoard);

            }//end if/else

        });//end actionListener
    }//end spaceButtonPressed
    ////END ACTIONS PERFORMED/////////////////////////////////////

    //working on undoing last turn if a challenge is valid
    private void undoLastTurn() {
        //for every space played last turn, remove it from the board
        //subtract any points scored last turn from players score
    }//end undoLastTurn

}//end ScrabbleMain
