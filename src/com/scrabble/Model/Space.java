/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scrabble.Model;

/**
 *
 * @author jeremy.williamson
 */
public class Space implements Comparable<Space> {

    private LetterTile assignedTile;
    private int index;
    private int scoreModifier;
    private String label;
    private boolean valid;
    private boolean occupied;

    public Space(int newIndex) {
        assignedTile = null;
        index = newIndex;
        scoreModifier = 0;
        occupied = false;
        label = "";
        valid = false;
    }//end Space constructor

    @Override
    public int compareTo(Space otherSpace) {
        final int BEFORE = -1;
        final int AFTER = 1;

        if (index > otherSpace.index) {
            return AFTER;
        } else {
            return BEFORE;
        }//end if/else
    }//end compareTo

    public int getIndex() {
        return index;
    }

    public void setOccupied(boolean isOccupied) {
        occupied = isOccupied;
    }//end setter

    public boolean isOccupied() {
        return occupied;
    }//end getter

    public void setScoreModifier(int newModifier) {
        scoreModifier = newModifier;
    }//set Score Modifier

    public int getScoreModifier() {
        return scoreModifier;
    }//end getter

    public void removeAssignedTile() {
        occupied = false;
        assignedTile = null;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValidity(boolean newValid) {
        valid = newValid;
    }

    public void setAssignedTile(LetterTile newTile) {
        assignedTile = newTile;
        occupied = true;
    }//end setter

    public LetterTile getAssignedTile() {
        return assignedTile;
    }//end getter

    public void setLabel(String newLabel) {
        label = newLabel;
    }//end setter

    public String getLabel() {
        return label;
    }//end getter

    @Override
    public String toString() {
        String returnString;
        returnString = "This space has this tile assigned to it: ";

        if (assignedTile != null) {
            returnString = returnString + assignedTile.toString() + ". ";
        } else {
            returnString = returnString + "none. ";
        }

        return returnString;
    }//end toString()

}//end SPACE
