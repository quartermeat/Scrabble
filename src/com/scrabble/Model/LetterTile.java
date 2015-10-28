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
//Tile Class
public class LetterTile {

    private char letter;
    final private int letterPoints;
    private Space assignedSpace;

    public LetterTile() {
        letter = '#';
        letterPoints = 0;
        assignedSpace = null;
    }//end constructor

    public void setAssignedSpace(Space newSpace) {
        assignedSpace = newSpace;
    }//end setter

    public Space getAssignedSpace() {
        return assignedSpace;
    }//end getter

    public void removeAssignedSpace() {
        assignedSpace = null;
    }//end removeAssignedSpace

    public LetterTile(char newLetter, int newPoints) {
        letter = newLetter;
        letterPoints = newPoints;
    }//end constructor

    ///USED FOR TESTING///////////
    public LetterTile(char newLetter) {
        letter = newLetter;
        letterPoints = 0;
    }//end test constructor

    public char getLetter() {
        return letter;
    }//end letter getter

    public void setLetter(char newLetter) {
        letter = newLetter;
    }//end setter

    public int getPoints() {
        return letterPoints;
    }//end points getter

    @Override
    public String toString() {
        return Character.toUpperCase(letter) + "   " + Integer.toString(letterPoints);
    }//end toString
}//end Letter
