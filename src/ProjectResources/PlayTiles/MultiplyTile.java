package ProjectResources.PlayTiles;


import java.io.Serializable;

// MultiplyTile
// Programmer: Seun Eguntola
// Date created: 10/6/16

public class MultiplyTile extends OperatorTile implements Serializable{

    /**
     * Constructor
     */
    public MultiplyTile() {
        sign = '*';
        setImgUrl("Resource/x.png");
        setScore(2);
    }

}
