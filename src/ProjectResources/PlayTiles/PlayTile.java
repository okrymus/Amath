package ProjectResources.PlayTiles;

import java.io.Serializable;
import javafx.scene.image.Image;

// PlayTile
// Programmer: Prakrit Saetang
// Date created: 9/20/16

/**
 * Super class for all play tiles
 */
public class PlayTile implements Serializable {

    // Instances
    protected int score;        // to hold a score on a play tile
    protected boolean isUsed;   // to tell if a tile is used
    protected String imgUrl;
    protected transient Image tileImage;  // to hold an image object of a tile

    /**
     * Constructor
     */
    public PlayTile() {
        this.score = -1;
        this.isUsed = false;
        this.imgUrl = null;
        this.tileImage = null;
    }

    /**
     * Constructor w/ arguments
     * @param score
     * @param isUsed
     * @param tileImageUrl
     */
    public PlayTile(int score, boolean isUsed, String tileImageUrl) {
        this.score = score;
        this.isUsed = isUsed;
        this.imgUrl = tileImageUrl;
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgUrl));
        //this.tileImage = new Image(getClass().getResourceAsStream(tileImageUrl));
    }

    // Getters

    /**
     * Get score
     * @return int - score
     */
    public int getScore() {
        return score;
    }

    /**
     * Get isUsed
     * @return boolean - isUsed
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Get image URL
     * @return String - image URL
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * get tile image
     * @return Image - tile image
     */
    public Image getTileImage() {
        return tileImage;
    }

    // Setters

    /**
     * set score
     * @param score
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * set isUsed
     * @param used
     */
    public void setUsed(boolean used) {
        isUsed = used;
    }

    /**
     * set image URL
     * @param imgUrl
     */
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgUrl));
    }

    /**
     * set tile image
     * @param tileImageUrl
     */
    public void setTileImage(String tileImageUrl) {
        this.tileImage = new Image(getClass().getResourceAsStream(tileImageUrl));
    }

    // Methods

    /**
     * copy all info from another playtile to this playtile
     * @param playTile
     */
    public void copy(PlayTile playTile) {
        this.score = playTile.getScore();
        this.isUsed = playTile.isUsed();
        this.imgUrl = playTile.getImgUrl();
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgUrl));
    }
}
