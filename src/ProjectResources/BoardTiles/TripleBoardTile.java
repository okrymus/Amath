package ProjectResources.BoardTiles;

import java.io.Serializable;

// TripleBoardTile
// Programmer: Emerson Moniz
// Date created: 9/23/16

public class TripleBoardTile extends BoardTile implements Serializable{

    /**
     * get score
     * @return int - score
     */
    @Override
    public int getScore() {
        return playTile.getScore()*3;
    }
}
