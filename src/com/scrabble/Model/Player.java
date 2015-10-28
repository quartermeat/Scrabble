/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scrabble.Model;

import java.util.ArrayList;

/**
 *
 * @author jeremy.williamson
 */
public class Player {

    //players rack of tiles
    private ArrayList<LetterTile> rack;
    private SpaceButton selectedRackButton;
    private int score;
    private String name;

    //constructor
    public Player(Bag bag, String newName) {
        name = newName;

        rack = new ArrayList<>();
        selectedRackButton = null;

        //upon creation, player gets 7 tiles from bag
        for (int i = 0; i < 7; i++) {
            addTile(bag);
        }//end for

    }//end constructor

    public final boolean addTile(Bag bag) {
        boolean add = rack.add(bag.pullTile());

        return add;
    }//end addTile

    public boolean addSpecificTile(LetterTile tile) {
        return rack.add(tile);
    }//end addSpecificTile

    public boolean removeTile(LetterTile tile) {
        return rack.remove(tile);
    }//end removeTile

    public ArrayList<LetterTile> getRack() {
        return rack;
    }//getRack();

    public void setRack(ArrayList<LetterTile> newList) {
        rack = newList;
    }//end setRack();

    public SpaceButton getSelectedRackButton() {
        return selectedRackButton;
    }//end getSelectedRackButton

    public void setSelectedRackButton(SpaceButton newSelectedRackButton) {
        selectedRackButton = newSelectedRackButton;
    }//end setSelectedRackButton

    public void addScore(int wordScore) {
        this.score += wordScore;
    }//end addScore

    public int getScore() {
        return this.score;
    }//end getScore

    public String getName() {
        return this.name;
    }//end getName

}//end Player
