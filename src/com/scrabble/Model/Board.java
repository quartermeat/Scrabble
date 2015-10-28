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
public class Board {

    //score modifiers
    public static final int TRIPLEWORDSCOREMODIFIER = 4;
    public static final int TRIPLELETTERSCOREMODIFIER = 3;
    public static final int DOUBLEWORDSCOREMODIFIER = 2;
    public static final int DOUBLELETTERSCOREMODIFIER = 1;

    //LISTS THAT PROVIDE SPECIFIC INDEXES WHERE SCORE MODIFIERS SHOULD BE PLACED BASED ON AMERICAN SCRABBLE BOARD//////////////////////////////////////////////////////////
    //indexes start at 1 for this
    private static final int[] TRIPLEWORDSCOREINDEXARRAY = {0, 7, 14, 90, 104, 210, 217, 224};
    private static final int[] DOUBLELETTERSCOREINDEXARRAY = {3, 11, 36, 38, 45, 52, 59, 92, 96, 98, 102, 108, 116, 122, 126, 128, 132, 165, 172, 179, 186, 188, 213, 221};
    private static final int[] DOUBLEWORDSCOREINDEXARRAY = {16, 28, 32, 42, 48, 56, 64, 70, 154, 160, 168, 176, 182, 192, 196, 208};
    private static final int[] TRIPLELETTERSCOREINDEXARRAY = {20, 24, 76, 80, 84, 88, 136, 140, 144, 148, 200, 204};
    private static final int NUMSPACES = 225;

    public ArrayList<Space> spaces;

    public Board() {
        spaces = new ArrayList<>();

        for (int i = 0; i < NUMSPACES; i++) {
            spaces.add(new Space(i));
        }

        populateScoreModifiers();

    }//end Board constructor

    private void populateScoreModifiers() {

        for (int i = 0; i < NUMSPACES; i++) {
            for (int j = 0; j < TRIPLEWORDSCOREINDEXARRAY.length; j++) {
                if (i == TRIPLEWORDSCOREINDEXARRAY[j]) {
                    spaces.get(i).setScoreModifier(TRIPLEWORDSCOREMODIFIER);
                    spaces.get(i).setLabel("TWS");
                }//END IF
            }//END FOR
            for (int j = 0; j < TRIPLELETTERSCOREINDEXARRAY.length; j++) {
                if (i == TRIPLELETTERSCOREINDEXARRAY[j]) {
                    spaces.get(i).setScoreModifier(TRIPLELETTERSCOREMODIFIER);
                    spaces.get(i).setLabel("TLS");
                }//END IF
            }//END FOR
            for (int j = 0; j < DOUBLEWORDSCOREINDEXARRAY.length; j++) {
                if (i == DOUBLEWORDSCOREINDEXARRAY[j]) {
                    spaces.get(i).setScoreModifier(DOUBLEWORDSCOREMODIFIER);
                    spaces.get(i).setLabel("DWS");
                }//END IF
            }//END FOR
            for (int j = 0; j < DOUBLELETTERSCOREINDEXARRAY.length; j++) {
                if (i == DOUBLELETTERSCOREINDEXARRAY[j]) {
                    spaces.get(i).setScoreModifier(DOUBLELETTERSCOREMODIFIER);
                    spaces.get(i).setLabel("DLS");
                }//END IF
            }//END FOR
        }//END FOR

    }//end populateScoreModifiers

    //get valid spaces on the board
    public ArrayList<Space> getValidSpaces(GameState gameState) {

        ArrayList<Space> validSpaces = new ArrayList<>();

        //invalidate everything to start off
        for (Space space : spaces) {
            space.setValidity(false);
        }//end for

        //List of cases that are  specific
        //valid if occupied & played this turn
        boolean boardEmpty = true;
        for (Space space : spaces) {
            if (space.isOccupied() && gameState.getPlayedSpaces().contains(space)) {
                space.setValidity(true);
            }//end if
            if (space.isOccupied()) {
                boardEmpty = false;
            }
        }//end for

        //if nothing on board, only center is valid
        //********handled in board window setup as well****************//
        if (boardEmpty) {
            spaces.get(112).setValidity(true);
        }

        //if rack tile selected, no occupied spaces are valid
        //****This case is handled in the rack button events*****//
        //if nothing is played this turn, but spaces are occupied, border is valid
        if (gameState.getPlayedSpaces().isEmpty()) {
            for (Space space : spaces) {
                //for every occupied space
                if (space.isOccupied()) {
                    //if left adjacent is not occupied
                    if (!spaces.get(space.getIndex() - 1).isOccupied()) {
                        spaces.get(space.getIndex() - 1).setValidity(true);
                    }//end if
                    //if top adjacent is not occupied
                    if (!spaces.get(space.getIndex() - 15).isOccupied()) {
                        spaces.get(space.getIndex() - 15).setValidity(true);
                    }//end if
                    //if right adjacent is not occupied
                    if (!spaces.get(space.getIndex() + 1).isOccupied()) {
                        spaces.get(space.getIndex() + 1).setValidity(true);
                    }//end if
                    //if bottom adjacent is not occupied
                    if (!spaces.get(space.getIndex() + 15).isOccupied()) {
                        spaces.get(space.getIndex() + 15).setValidity(true);
                    }//end if
                }//end if
            }//end for
        }//end if

        //if only 1 space is played, all adjacent to that one that are not occupied are valid
        if (gameState.getPlayedSpaces().size() == 1) {
            Space firstSpace = gameState.getPlayedSpaces().get(0);
            //if left adjacent is not occupied
            if (!spaces.get(firstSpace.getIndex() - 1).isOccupied()) {
                spaces.get(firstSpace.getIndex() - 1).setValidity(true);
            } else {//if left adjacent is occupied, we need to move left until we get to an unoccupied space
                while (spaces.get(firstSpace.getIndex() - 1).isOccupied()) {
                    firstSpace = spaces.get(firstSpace.getIndex() - 1);
                }//end while
                spaces.get(firstSpace.getIndex() - 1).setValidity(true);
                firstSpace = gameState.getPlayedSpaces().get(0);
            }//end if/else

            //if top adjacent is not occupied
            if (!spaces.get(firstSpace.getIndex() - 15).isOccupied()) {
                spaces.get(firstSpace.getIndex() - 15).setValidity(true);
            } else {//if top adjacent is occupied, we need to move up until we get to an unoccupied space
                while (spaces.get(firstSpace.getIndex() - 15).isOccupied()) {
                    firstSpace = spaces.get(firstSpace.getIndex() - 15);
                }//end while
                spaces.get(firstSpace.getIndex() - 15).setValidity(true);
                firstSpace = gameState.getPlayedSpaces().get(0);
            }//end if/else

            //if right adjacent is not occupied
            if (!spaces.get(firstSpace.getIndex() + 1).isOccupied()) {
                spaces.get(firstSpace.getIndex() + 1).setValidity(true);
            } else {//if right adjacent is occupied, we need to move right until we get to an unoccupied space
                while (spaces.get(firstSpace.getIndex() + 1).isOccupied()) {
                    firstSpace = spaces.get(firstSpace.getIndex() + 1);
                }//end while
                spaces.get(firstSpace.getIndex() + 1).setValidity(true);
                firstSpace = gameState.getPlayedSpaces().get(0);
            }//end if/else

            //if bottom adjacent is not occupied
            if (!spaces.get(firstSpace.getIndex() + 15).isOccupied()) {
                spaces.get(firstSpace.getIndex() + 15).setValidity(true);
            } else {//if bottom adjacent is occupied, we need to move down until we get to an unoccupied space
                while (spaces.get(firstSpace.getIndex() + 15).isOccupied()) {
                    firstSpace = spaces.get(firstSpace.getIndex() + 15);
                }//end while
                spaces.get(firstSpace.getIndex() + 15).setValidity(true);
                firstSpace = gameState.getPlayedSpaces().get(0);
            }//end if/else

            //Set up orientation specifically for these situations to make sure scoring is right
            //if left and right spaces are not occupied
            if (!spaces.get(firstSpace.getIndex() - 1).isOccupied() && !spaces.get(firstSpace.getIndex() + 1).isOccupied()) {
                gameState.setOrientation(GameState.DOWN);
                //if top and bottom spaces are not occupied
            } else if (!spaces.get(firstSpace.getIndex() + 15).isOccupied() && !spaces.get(firstSpace.getIndex() - 15).isOccupied()) {
                gameState.setOrientation(GameState.ACROSS);
                //if top and left spaces are not occupied
            } else if (!spaces.get(firstSpace.getIndex() - 15).isOccupied() && !spaces.get(firstSpace.getIndex() - 1).isOccupied()) {
                gameState.setOrientation(GameState.ACROSS);
                //if top and right spaces are not occupied
            } else if (!spaces.get(firstSpace.getIndex() - 15).isOccupied() && !spaces.get(firstSpace.getIndex() + 1).isOccupied()) {
                gameState.setOrientation(GameState.DOWN);
                //if bottom and left spaces are not occupied
            } else if (!spaces.get(firstSpace.getIndex() + 15).isOccupied() && !spaces.get(firstSpace.getIndex() - 1).isOccupied()) {
                gameState.setOrientation(GameState.ACROSS);
                //if bottom and right spaces are not occupied
            } else if (!spaces.get(firstSpace.getIndex() + 15).isOccupied() && !spaces.get(firstSpace.getIndex() + 1).isOccupied()) {
                gameState.setOrientation(GameState.ACROSS);
            }//end if/else if
        }//end if

        //if 2 or more spaces are played, figure out if word is going across or down, and only validate the ends of the word
        if (gameState.getPlayedSpaces().size() >= 2) {

            int lastIndex = gameState.getPlayedSpaces().size() - 1;
            ArrayList<Space> playedSpaces = gameState.getPlayedSpaces();
            Collections.sort(playedSpaces);
            int difference = Math.abs(playedSpaces.get(0).getIndex() - playedSpaces.get(1).getIndex());
            if (difference >= 15) {
                gameState.setOrientation(GameState.DOWN);
                spaces.get(playedSpaces.get(0).getIndex() - 15).setValidity(true);
                spaces.get(playedSpaces.get(lastIndex).getIndex() + 15).setValidity(true);
            }
            if (difference < 15) {
                gameState.setOrientation(GameState.ACROSS);
                spaces.get(playedSpaces.get(0).getIndex() - 1).setValidity(true);
                spaces.get(playedSpaces.get(lastIndex).getIndex() + 1).setValidity(true);
            }//end if
        }//end if

        //setup the return list
        for (Space space : spaces) {
            if (space.isValid()) {
                validSpaces.add(space);
            }//end if
        }//end for        

        return validSpaces;

    }//get validSpaces()
}//end BOARD

