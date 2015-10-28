/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.scrabble.Model;

import com.scrabble.View.BoardWindow;
import java.awt.Color;

/**
 *
 * @author jeremy.williamson
 */
//Button that has an associated Tile
public class SpaceButton extends javax.swing.JButton {

    private LetterTile assignedTile;
    private Space assignedSpace;

    public SpaceButton() {
        assignedTile = null;
        assignedSpace = null;
    }//end constructor

    public SpaceButton(LetterTile newTile) {
        setAssignedTile(newTile);
        assignedSpace = null;
    }//end constructor

    public final void setAssignedTile(LetterTile newTile) {
        assignedTile = newTile;
        setText(assignedTile.toString());
        setBackground(Color.ORANGE);
    }//end setter

    public void removeAssignedTile() {
        assignedTile = null;
        setText("");
        setBackground(BoardWindow.DEFAULT_BUTTON_COLOR);
    }

    public LetterTile getAssignedTile() {
        return assignedTile;
    }//end getter

    public void setAssignedSpace(Space newSpace) {
        assignedSpace = newSpace;
    }//end setter

    public Space getAssignedSpace() {
        return assignedSpace;
    }//end getter

}//end SpaceButton class
