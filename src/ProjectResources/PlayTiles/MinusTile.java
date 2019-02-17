package ProjectResources.PlayTiles;



import java.io.Serializable;

// MinusTile
// Programmer: Seun Eguntola
// Date created: 10/6/16

public class MinusTile extends OperatorTile implements Serializable {

    /**
     * Constructor
     */
    public MinusTile() {
        sign = '-';
        setImgUrl("Resource/-.png");
        setScore(2);
    }

}
