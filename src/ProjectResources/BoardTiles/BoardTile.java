package ProjectResources.BoardTiles;

import ProjectResources.PlayTiles.PlayTile;
import java.io.Serializable;
import javafx.scene.image.Image;

// BoardTile
// Programmer: Panupong Leenawarat
// Date created: 12/16/2016
// Date Modified: 9/23/16 create the abstract class BoardTile
// Date Modified: 9/25/16 Add getScore() method

/**
 * Super class for all board tiles
 * @author Panupong
 */
public abstract class BoardTile implements Serializable{
    protected int x;
    protected int y;
    protected PlayTile playTile;
    protected boolean isPlaced;
    protected String imgURL;
    protected transient Image tileImage;

    public BoardTile() {
        this.x = -1;
        this.y = -1;
        this.playTile = null;
        this.isPlaced = false;
        this.imgURL = null;
        this.tileImage = null;
    }

    public BoardTile(int x, int y, PlayTile playTile, boolean isPlaced, String tileImageUrl) {
        this.x = x;
        this.y = y;
        this.playTile = playTile;
        this.isPlaced = isPlaced;
        this.imgURL = tileImageUrl;
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgURL));
        //this.tileImage = new Image(getClass().getResourceAsStream(tileImageUrl));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public PlayTile getPlayTile() {
        return playTile;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public String getImgURL() {
        return imgURL;
    }

    public Image getTileImage() {
        return tileImage;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setPlayTile(PlayTile playTile) {
        this.playTile = playTile;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
        this.tileImage = new Image(getClass().getResourceAsStream(this.imgURL));
    }

    public void setTileImage(String tileImageUrl) {
        this.tileImage = new Image(getClass().getResourceAsStream(tileImageUrl));
    }

    public void copy(BoardTile boardTile) {
        this.x = boardTile.getX();
        this.y = boardTile.getY();
        this.isPlaced = boardTile.isPlaced();
        this.imgURL = boardTile.getImgURL();
        this.setTileImage(imgURL);
        boardTile.getPlayTile().setTileImage(boardTile.getPlayTile().getImgUrl());
        this.playTile = boardTile.getPlayTile();
    }

    public abstract int getScore();

    public void displayLocation() {
        System.out.printf("[%d, %d] ", getX(), getY());
    }
}
