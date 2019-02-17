package ProjectResources.BoardTiles;

import java.io.Serializable;

// DoubleAllBoardTile
// Programmer: Emerson Moniz
// Date created: 9/23/16

public class DoubleAllBoardTile extends BoardTile implements Serializable{

    int totalScore = -1;

    /**
     * get total score
     * @return int - total score
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * get score
     * @return int - score
     */
    public int getScore() {
        return (2 * totalScore);
    }

    /**
     * set total score
     * @param totalScore
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }
}
