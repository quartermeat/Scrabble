package com.scrabble.Model;

import java.text.AttributedString;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is used to keep track of the tiles used in the game. There also
 * exists some functionality to return the score of a given letter.
 *
 * @author jeremy.williamson
 */
public class Bag {

    //List of tiles used for scoring words
    public ArrayList<LetterTile> tileList;

    //List used to hold the 100 available letter tiles
    final private ArrayList<LetterTile> letterPool;

    //Constructor, initializes list and addes tiles to it
    public Bag() {

        /*
         English Scrabble Letter points and distribution
         0 points: blank/wild (2)
         1 point: E (12), A (9), I (9), O (8), N (6), R (6), T (6), L (4), S (4), U (4)
         2 points: D (4), G (3)
         3 points: B (2), C (2), M (2), P (2)
         4 points: F (2), H (2), V (2), W (2), Y (2)
         5 points: K (1)
         8 points: J (1), X (1)
         10 points: Q (1), Z (1)
         */
        //create all required tiles, assigning a letter and points
        LetterTile aTile = new LetterTile('A', 1);
        LetterTile bTile = new LetterTile('B', 3);
        LetterTile cTile = new LetterTile('C', 3);
        LetterTile dTile = new LetterTile('D', 2);
        LetterTile eTile = new LetterTile('E', 1);
        LetterTile fTile = new LetterTile('F', 4);
        LetterTile gTile = new LetterTile('G', 2);
        LetterTile hTile = new LetterTile('H', 4);
        LetterTile iTile = new LetterTile('I', 1);
        LetterTile jTile = new LetterTile('J', 8);
        LetterTile kTile = new LetterTile('K', 5);
        LetterTile lTile = new LetterTile('L', 1);
        LetterTile mTile = new LetterTile('M', 3);
        LetterTile nTile = new LetterTile('N', 1);
        LetterTile oTile = new LetterTile('O', 1);
        LetterTile pTile = new LetterTile('P', 3);
        LetterTile qTile = new LetterTile('Q', 10);
        LetterTile rTile = new LetterTile('R', 1);
        LetterTile sTile = new LetterTile('S', 1);
        LetterTile tTile = new LetterTile('T', 1);
        LetterTile uTile = new LetterTile('U', 1);
        LetterTile vTile = new LetterTile('V', 4);
        LetterTile wTile = new LetterTile('W', 4);
        LetterTile xTile = new LetterTile('X', 8);
        LetterTile yTile = new LetterTile('Y', 4);
        LetterTile zTile = new LetterTile('Z', 10);
        LetterTile blankTile = new LetterTile('?', 0);

        ////vvvvv NOT NEEDED FOR POOL, ONLY USED FOR SCORING vvvvv//////////////
        //initialize tileList
        tileList = new ArrayList<>();
        //add tiles to list
        tileList.add(aTile);
        tileList.add(bTile);
        tileList.add(cTile);
        tileList.add(dTile);
        tileList.add(eTile);
        tileList.add(fTile);
        tileList.add(gTile);
        tileList.add(hTile);
        tileList.add(iTile);
        tileList.add(jTile);
        tileList.add(kTile);
        tileList.add(lTile);
        tileList.add(mTile);
        tileList.add(nTile);
        tileList.add(oTile);
        tileList.add(pTile);
        tileList.add(qTile);
        tileList.add(rTile);
        tileList.add(sTile);
        tileList.add(tTile);
        tileList.add(uTile);
        tileList.add(vTile);
        tileList.add(wTile);
        tileList.add(xTile);
        tileList.add(yTile);
        tileList.add(zTile);
        tileList.add(blankTile);
        ////^^^^^USED FOR SCORING ONLY^^^^^^^//////////

        //initialize tile list
        letterPool = new ArrayList<>();

        //////ADD TILES TO POOL/////////////////////
        //add 12 e tiles to pool
        for (int i = 0; i < 12; i++) {
            letterPool.add(eTile);
        }//end for

        //add 9 a and i tiles
        for (int i = 0; i < 9; i++) {
            letterPool.add(aTile);
            letterPool.add(iTile);
        }//end for

        //add 8 o tiles
        for (int i = 0; i < 8; i++) {
            letterPool.add(iTile);
        }//end for

        //add 6 n,r and t tiles
        for (int i = 0; i < 6; i++) {
            letterPool.add(nTile);
            letterPool.add(rTile);
            letterPool.add(tTile);
        }//end for

        //add 4 l,s,u and d tiles
        for (int i = 0; i < 4; i++) {
            letterPool.add(lTile);
            letterPool.add(sTile);
            letterPool.add(uTile);
            letterPool.add(dTile);
        }//end for

        //add 3 g tiles
        for (int i = 0; i < 3; i++) {
            letterPool.add(gTile);
        }//end for

        //add 2 b,c,m,p,f,h,v,w,y and blank tiles
        for (int i = 0; i < 2; i++) {
            letterPool.add(bTile);
            letterPool.add(cTile);
            letterPool.add(mTile);
            letterPool.add(pTile);
            letterPool.add(fTile);
            letterPool.add(hTile);
            letterPool.add(vTile);
            letterPool.add(wTile);
            letterPool.add(yTile);
            letterPool.add(blankTile);
        }//end for

        //add 1 k,j,x,q and z tiles
        letterPool.add(kTile);
        letterPool.add(jTile);
        letterPool.add(xTile);
        letterPool.add(qTile);
        letterPool.add(zTile);
    }//end constructor

    //return how many tiles are in the bag
    public int size() {
        return letterPool.size();
    }//end size()

    //arraylist indexes start at 0, so a full tileList is 0 - 99, size() being 100
    public LetterTile pullTile() {
        int max = letterPool.size() - 1;
        Random rand = new Random();
        int randomTileIndex = rand.nextInt(max + 1);

        return letterPool.remove(randomTileIndex);
    }//end pullTile()

    //get letter score returns the score of the corresponding letter
    public int getLetterScore(char letter) {
        int score = 0;
        for (LetterTile tileList1 : tileList) {
            if (letter == tileList1.getLetter()) {
                score = tileList1.getPoints();
                break;
            } //end if
        } //end for

        return score;
    }//end getLetterScore

}//end Bag
