package ProjectResources.PlayTiles;

import ProjectResources.PlayTiles.PlayTile;
import java.io.Serializable;

// OperatorTile
// Programmer: Seun Eguntola
// Date created: 10/7/16

public class PlusTile extends OperatorTile implements Serializable {

    /**
     * Constructor
     */
    public PlusTile() {
        sign = '+';
        setImgUrl("Resource/+.png");
        setScore(2);
    }

    /**
     * get the sign in charactor form
     * @return Char of the sign
     */
    public char toChar() {
        return sign;
    }
}
