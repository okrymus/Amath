package ProjectResources.PlayTiles;



import java.io.Serializable;

// DivideTile
// Programmer: Seun Eguntola
// Date created: 10/6/16


public class DivideTile extends OperatorTile implements Serializable {

    /**
     * Constructor
     */
    public DivideTile() {
        sign = '/';
        setImgUrl("Resource/divide.png");
        setScore(2);
    }
}
