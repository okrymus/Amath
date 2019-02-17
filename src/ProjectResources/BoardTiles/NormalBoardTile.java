package ProjectResources.BoardTiles;

// NormalBoardTile
// Programmer: Emerson Moniz
// Date created: 9/23/16

import java.io.Serializable;

public class NormalBoardTile extends BoardTile implements Serializable{

    /**
     * get score
     * @return int - score
     */
    @Override
    public int getScore() {
        return playTile.getScore();
    }
}
