package ProjectResources.PlayTiles;

// NumberTile
// Programmer: Seun Eguntola
// Date created:  9/20/16

import ProjectResources.PlayTiles.PlayTile;
import java.io.Serializable;

import static ProjectResources.AMathConstants.allNumberNumber;

/**
 * Super class for all number tiles
 */
public class NumberTile extends PlayTile implements Serializable{

    // Instances
    private int number;  // to hold a number on a number tile

    // Constructors

    /**
     * Constructor
     */
    public NumberTile() {
        this.number = -1;
    }

    /**
     * Constructor w/ arguments
     * @param score
     * @param isUsed
     * @param tileImageUrl
     * @param number
     */
    public NumberTile(int score, boolean isUsed, String tileImageUrl, int number) {
        super(score, isUsed, tileImageUrl);
        this.number = number;
    }

    // Getters
    /**
     * Get number
     * @return int - number
     */
    public int getNumber() {
        return number;
    }

    // Setters

    /**
     * Set a number to a tile
     * @param number
     */
    public void setNumber(int number) {
        this.number = number;
    }

    // Methods
    @Override
    public String toString() {
        return "NumberTile{" +
                "number=" + number + ", " +
                "score=" + score + ", " +
                "isUsed=" + isUsed +
                '}' + "\n";
    }

    /**
     * Copy all info from another tile to this tile
     * @param numberTile
     */
    public void copy(NumberTile numberTile) {
        this.score = numberTile.getScore();
        this.number = numberTile.getNumber();
        this.isUsed = numberTile.isUsed();
        this.imgUrl = numberTile.getImgUrl();
        setTileImage(imgUrl);
    }
}
