package ProjectResources.PlayTiles;

import java.io.Serializable;

// BlankTile
// Programmer: Seun Eguntola
// Date created: 10/6/16

public class BlankTile extends PlayTile implements Serializable {

    /**
     * Constructor
     */
    public BlankTile() {
        setImgUrl("Resource/blankPlay.png");
        setScore(0);
    }
}
