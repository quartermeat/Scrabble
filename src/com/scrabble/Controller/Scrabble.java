package com.scrabble.Controller;

/**
 *
 * @author jeremy.williamson
 */
public class Scrabble {

    public static void main(String[] args) {
        Scrabble game = new Scrabble();
    }//END MAIN

    //CONSTRUCTOR
    public Scrabble() {
        //create a new scrabbleMain object
        ScrabbleMain inst = new ScrabbleMain();
        inst.startNewGameButtonPressed();
        inst.challengeButtonPressed();
        inst.endTurnButtonPressed();
        inst.rackButton1Pressed();
        inst.rackButton2Pressed();
        inst.rackButton3Pressed();
        inst.rackButton4Pressed();
        inst.rackButton5Pressed();
        inst.rackButton6Pressed();
        inst.rackButton7Pressed();
        inst.spaceButtonPressed();

    }//end constructor

}//END SCRABBLE
