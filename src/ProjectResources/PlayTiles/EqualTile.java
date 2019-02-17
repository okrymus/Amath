package ProjectResources.PlayTiles;



import java.io.Serializable;

// EqualTile
// Programmer: Seun Eguntola
// Date created: 10/6/16

public class EqualTile extends OperatorTile implements Serializable{

    /**
     * Constructor
     */
    public EqualTile() {
        sign = '=';
        setImgUrl("Resource/=.png");
        setScore(1);
    }
}
