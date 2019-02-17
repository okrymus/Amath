package ProjectResources;

import ProjectResources.PlayTiles.*;

import java.util.ArrayList;
import java.util.Collections;


// Bag
// Programmer: Prakrit Saetang
// Date created: 9/27/16
// Date Modified: 12/20/16 (Add 'newPlayTiles' method)

/**
 * Bag class - to hold All unused Play Tiles
 * @author Prakrit
 */
public class Bag implements AMathConstants {

    // Lists
    private ArrayList<PlayTile> playTilesArray;


    /**
     * Constructor
     */
    Bag() {
        playTilesArray = new ArrayList<>();
        setupBag();
    }

    // Getters

    /**
     * To get PlayTiles in Bag
     * @return ArrayList of PlayTiles
     */
    public ArrayList<PlayTile> getPlayTilesArray() {
        return playTilesArray;
    }

    /**
     * To get PlayTile in Bag (Array form)
     * @return PlayTiles[]
     */
    public PlayTile[] getBagArray() {
        PlayTile[] newBag = new PlayTile[getTilesLeft()];
        for (int i = 0; i < newBag.length; i++) {
            newBag[i] = playTilesArray.get(i);
        }
        return newBag;
    }

    /**
     * To set up bag when Bag is first initialized
     */
    private void setupBag() {

        // initializing all Number tiles (0 - 20)
        for (int i = 0; i < allNumberAmount.length; i++) {
            for (int j = 0; j < allNumberAmount[i]; j++) {
                String tempImageUrl = "Resource/" + String.valueOf(allNumberNumber[i]) + ".png";
                int tempNumber = allNumberNumber[i];
                int tempScore = allNumberScore[i];
                NumberTile tempNumberTile = new NumberTile(tempScore, false, tempImageUrl, tempNumber);
                playTilesArray.add(tempNumberTile);
            }
        }

        // initializing Blank tiles
        for (int i = 0; i < infoBlank[AMOUNT]; i++) {
            BlankTile blankTile = new BlankTile();
            playTilesArray.add(blankTile);
        }

        // initializing Plus tiles
        for (int i = 0; i < infoPlus[AMOUNT]; i++) {
            PlusTile plusTile = new PlusTile();
            playTilesArray.add(plusTile);
        }

        // initializing Minus tiles
        for (int i = 0; i < infoMinus[AMOUNT]; i++) {
            MinusTile minusTile = new MinusTile();
            playTilesArray.add(minusTile);
        }

        // initializing Plus/Minus tiles
        for (int i = 0; i < infoPlusOrMinus[AMOUNT]; i++) {
            PlusOrMinusTile plusOrMinusTile = new PlusOrMinusTile();
            playTilesArray.add(plusOrMinusTile);
        }

        // initializing Multiply tiles
        for (int i = 0; i < infoMultiply[AMOUNT]; i++) {
            MultiplyTile multiplyTile = new MultiplyTile();
            playTilesArray.add(multiplyTile);
        }

        // initializing Divide tiles
        for (int i = 0; i < infoDivide[AMOUNT]; i++) {
            DivideTile divideTile = new DivideTile();
            playTilesArray.add(divideTile);
        }

        // initializing Multiply/Divide tiles
        for (int i = 0; i < infoMultipleOrDivide[AMOUNT]; i++) {
            MultipleOrDivideTile multipleOrDivideTile = new MultipleOrDivideTile();
            playTilesArray.add(multipleOrDivideTile);
        }

        // initializing Equal sign tiles
        for (int i = 0; i < infoEqualSign[AMOUNT]; i++) {
            EqualTile equalTile = new EqualTile();
            playTilesArray.add(equalTile);
        }

        // Shuffle the ArrayList<PlayTile>
        Collections.shuffle(playTilesArray);
    }

    /**
     * Draw one PlayTile from the Bag
     * @return PlayTile
     */
    public PlayTile draw() {
        PlayTile temp = playTilesArray.get(0);
        playTilesArray.remove(0);

        return temp;
    }

    /**
     * Get amount of PlayTiles left in the Bag
     * @return int size of ArrayList of PlayTile
     */
    public int getTilesLeft() {
        return playTilesArray.size();
    }

    /**
     * Return a PlayTile back to the Bag
     * @param tile
     */
    public void returnTiles(PlayTile tile) {
        playTilesArray.add(tile);
        Collections.shuffle(playTilesArray);
    }

    /**
     * Set up new List of PlayTiles to the Bag
     * @param playTiles
     */
    public void newPlayTiles(PlayTile[] playTiles) {
        playTilesArray.clear();

        for (PlayTile playTile : playTiles) {
            playTile.setTileImage(playTile.getImgUrl());
            playTilesArray.add(playTile);
        }
    }
}
