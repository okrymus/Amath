package ProjectResources.BoardTiles;

import java.io.Serializable;

// DoubleBoardTile
// Programmer: Emerson Moniz
// Date created: 9/23/16

public class DoubleBoardTile extends BoardTile implements Serializable {

    /**
     * Multiplies the current score by two
     * @return int - score
     */
    @Override
    public int getScore() {
        return playTile.getScore()*2;
    }
}

