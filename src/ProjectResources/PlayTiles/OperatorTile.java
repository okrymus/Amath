package ProjectResources.PlayTiles;

import java.io.Serializable;

// OperatorTile
// Programmer: Seun Eguntola
// Date created: 10/7/16

/**
 * Super class for all operator tiles
 */
public class OperatorTile extends PlayTile implements Serializable{
    protected Character sign;

    /**
     * get a sign
     * @return Char - sign
     */
    public Character getSign() {
        return sign;
    }
}
