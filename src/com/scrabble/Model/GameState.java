/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scrabble.Model;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author jeremy.williamson
 */
public class GameState {

    public final static int GAME_STARTED = 0;
    public final static int NO_TILE_SELECTED = 1;
    public final static int RACK_TILE_SELECTED = 2;
    public final static int ACROSS = 0;
    public final static int DOWN = 1;

    private int currentGameState;
    private final boolean isRunning;
    private ScoredWord spacesPlayedThisTurn;
    private ArrayList<ScoredWord> scoredWords;
    private ArrayList<String> wordsScoredLastTurn;

    public GameState(int newGameState) {
        currentGameState = newGameState;
        isRunning = true;
        spacesPlayedThisTurn = new ScoredWord(ACROSS);
        scoredWords = new ArrayList<>();
        wordsScoredLastTurn = new ArrayList<>();
    }//end constructor

    public ArrayList<String> getWordsScoredLastTurn() {
        return wordsScoredLastTurn;
    }//end getter

    public void setGameState(int newGameState) {
        currentGameState = newGameState;
    }//end setter

    public int getGameState() {
        return currentGameState;
    }//end getter

    public boolean isRunning() {
        return isRunning;
    }//end isRunning

    public boolean addPlayedSpace(Space space) {

        return spacesPlayedThisTurn.add(space);
    }//end addPLayedTile

    public boolean removePlayedSpace(Space space) {
        return spacesPlayedThisTurn.remove(space);
    }//end removePlayedTile

    public ArrayList<Space> getPlayedSpaces() {
        return spacesPlayedThisTurn;
    }//end getter

    public void endTurn() {
        this.spacesPlayedThisTurn.clear();
        this.scoredWords.clear();
    }

    public void setOrientation(int newOrientation) {
        spacesPlayedThisTurn.setOrientation(newOrientation);
    }

    public int getOrientation() {
        return spacesPlayedThisTurn.getOrientation();
    }

    public ArrayList<ScoredWord> getScoredWords() {
        return scoredWords;
    }

    public void addScoredWords(ScoredWord scoredWord, Board gameBoard) {

        System.out.println("Adding " + scoredWord + " now.");
        //sort them into a word based on indexes
        Collections.sort(scoredWord);

        //start at first space in the word
        Space currentSpace = scoredWord.get(0);

        if (scoredWord.getOrientation() == GameState.ACROSS) {
            //get occupied spaces to the left
            while ((currentSpace.getIndex() % 15) - 1 >= 0) {
                //get the spaces to the left of the first space
                currentSpace = gameBoard.spaces.get(currentSpace.getIndex() - 1);

                if (currentSpace.isOccupied()) {
                    scoredWord.add(currentSpace);
                } else {
                    break;
                }//end if/else
            }//end while

            currentSpace = scoredWord.get(0);
            //get occupied spaces to the right
            while (((currentSpace.getIndex() % 15) + 1) <= 14) {

                if (currentSpace.isOccupied() && !scoredWord.contains(currentSpace)) {
                    scoredWord.add(currentSpace);
                } else if (currentSpace.isOccupied() && spacesPlayedThisTurn.contains(currentSpace)) {
                    //go to the next space to the right(continue loop)
                    Space aboveSpace = gameBoard.spaces.get(currentSpace.getIndex() - 15);
                    Space belowSpace = gameBoard.spaces.get(currentSpace.getIndex() + 15);

                    if (aboveSpace.isOccupied()) {
                        ScoredWord newWord = new ScoredWord(GameState.DOWN);
                        newWord.add(aboveSpace);
                        addScoredWords(newWord, gameBoard);
                    } else if (belowSpace.isOccupied()) {
                        ScoredWord newWord = new ScoredWord(GameState.DOWN);
                        newWord.add(belowSpace);
                        addScoredWords(newWord, gameBoard);
                    }//end if/else if

                } else if (!currentSpace.isOccupied()) {
                    Collections.sort(scoredWord);
                    scoredWords.add(scoredWord);
                    break;
                }

                currentSpace = gameBoard.spaces.get(currentSpace.getIndex() + 1);
            }//end while

        } else if (scoredWord.getOrientation() == GameState.DOWN) {
            //get occupied spaces above
            while (currentSpace.getIndex() - 15 >= 0) {
                //get the spaces to the left of the first space
                currentSpace = gameBoard.spaces.get(currentSpace.getIndex() - 15);

                if (currentSpace.isOccupied()) {
                    scoredWord.add(currentSpace);
                } else {
                    break;
                }//end if/else
            }//end while

            currentSpace = scoredWord.get(0);
            //get occupied spaces to the right
            while ((currentSpace.getIndex() + 15) <= 224) {

                if (currentSpace.isOccupied() && !scoredWord.contains(currentSpace)) {
                    scoredWord.add(currentSpace);
                } else if (currentSpace.isOccupied() && spacesPlayedThisTurn.contains(currentSpace)) {
                    //go to the next space to the right(continue loop)
                    Space leftSpace = gameBoard.spaces.get(currentSpace.getIndex() - 1);
                    Space rightSpace = gameBoard.spaces.get(currentSpace.getIndex() + 1);

                    if (leftSpace.isOccupied()) {
                        ScoredWord newWord = new ScoredWord(GameState.ACROSS);
                        newWord.add(leftSpace);
                        addScoredWords(newWord, gameBoard);
                    } else if (rightSpace.isOccupied()) {
                        ScoredWord newWord = new ScoredWord(GameState.ACROSS);
                        newWord.add(rightSpace);
                        addScoredWords(newWord, gameBoard);
                    }//end if/else if

                } else if (!currentSpace.isOccupied()) {
                    Collections.sort(scoredWord);
                    scoredWords.add(scoredWord);
                    break;
                }

                currentSpace = gameBoard.spaces.get(currentSpace.getIndex() + 15);
            }//end while
        }//end if/else

        wordsScoredLastTurn.clear();

        for (ScoredWord wordList : scoredWords) {
            String word = new String();
            for (Space space : wordList) {
                word = word.concat(Character.toString(space.getAssignedTile().getLetter()));
            }//end for
            wordsScoredLastTurn.add(word);
        }//end for

    }//end getScoredWords

}//end GameState class
