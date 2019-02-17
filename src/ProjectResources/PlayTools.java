package ProjectResources;

import ProjectResources.BoardTiles.*;
import ProjectResources.PlayTiles.NumberTile;
import ProjectResources.PlayTiles.OperatorTile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

// PlayTools
// Programmer: Prakrit Saetang
// Date created: 10/26/16
// Date Modified: 11/3/16 (Add 'isOnStar', 'hasMissingSpotVertical', 'hasMissingSpotHorizontal' Methods)
// Date Modified: 11/15/16 (Add 'isConnectedWithOldTilesVertical', 'isConnectedWithOldTilesHorizontal' Methods)
// Date Modified: 11/20/16 (Add 'checkEquation', 'checkWholeBoard', 'copyBoardTiles', 'sortByX', 'sortByY'
//                          'sortXY', 'sortYX', 'toString' Methods)


/**
 * To handle all move validations
 * @author Prakrit
 */
public class PlayTools {

    // Lists
    private ArrayList<BoardTile> usedBoardTiles = new ArrayList<>(); // used board tiles
    // list to hold the tiles connect to the old tiles
    private ArrayList<BoardTile> connectedVerticalTiles = new ArrayList<>();
    private ArrayList<BoardTile> connectedHorizontalTiles = new ArrayList<>();

    // Alert text
    private String alertText = "";

    /**
     * Get Alert text
     * @return String - alert text
     */
    public String getAlertText() {
        return alertText;
    }

    /**
     * Get used board tiles
     * @return ArrayList of used board tiles
     */
    public ArrayList<BoardTile> getUsedBoardTiles() {
        return usedBoardTiles;
    }

    /**
     * Check equations
     * @param boardTiles
     * @return boolean - true if equations are true, false otherwise
     */
    public boolean checkEquation(ArrayList<BoardTile> boardTiles) {

        alertText = "";
        ResultFinder resultFinder;

        // convert the board tiles to equations in String
        String equation = toString(boardTiles);
        System.out.println(equation);

        // return false if there is no '=' in equations
        if (!equation.contains("=")) {
            alertText = "Invalid Input: An equation must contain at least one equal sign";
            return false;
        }

        // split equations
        String[] equations = equation.split("=");

        // check if there is more than 2 expression in the equation
        if (equations.length < 2) {
            alertText = "Invalid Input: An equation must contain at least 2 statements";
            return false;
        }

        // find results
        int[] results = new int[equations.length];

        for (int j = 0; j < equations.length; j++) {
            String eq = equations[j];

            try {
                resultFinder = new ResultFinder(eq);

                // return false if uneven division is found
                if (resultFinder.isNotEvenDivision()) {
                    alertText = "Invalid Division";
                    return false;
                }

                results[j] = resultFinder.getResult();
            }
            catch (Exception ex) {
                alertText = "Invalid input";
                return false;
            }

            alertText += "Statement " + "#" + (j+1) + ": " + eq + "\n" +
                    "Result: " + String.valueOf(results[j]) + "\n";
        }

        // Check all results if they are the same
        int firstResult = results[0];
        for (int k = 1; k < results.length; k++) {
            if (results[k] != firstResult) {
                alertText += "Equation is false";
                return false;
            }
        }

        alertText = "Equation is true";
        return true;
    }

    /**
     * Check all equations on the board
     * @param boardTiles
     * @return boolean - true if all equations on the board are true, false otherwise
     */
    public boolean checkWholeBoard(ArrayList<BoardTile> boardTiles) {

        // add new used tiles to old used tiles
        ArrayList<BoardTile> temp = copyBoardTiles(boardTiles);
        ArrayList<BoardTile> temp1 = copyBoardTiles(usedBoardTiles);
        ArrayList<BoardTile> calculatedTiles = new ArrayList<>();
        temp.addAll(temp1);

        //////////////// CHECK VERTICALLY /////////////////
        sortXY(temp);

        calculatedTiles.add(temp.get(0));

        for (int i = 1; i < temp.size(); i++) {

            // if the tile is still in the same line
            if (temp.get(i-1).getX() == temp.get(i).getX()) {

                // if the tile is connected to the previous one
                if (temp.get(i - 1).getY() + 1 == temp.get(i).getY()) {

                    calculatedTiles.add(temp.get(i));
                }
                // if the tile is not connected to the previous one and there are tiles in calculatedTiles array
                else if (temp.get(i-1).getY() + 1 != temp.get(i).getY() && calculatedTiles.size() > 1) {

                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }

                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
                else {
                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
            }
            else {

                if (calculatedTiles.size() > 1) {
                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }
                }

                calculatedTiles.clear();
                calculatedTiles.add(temp.get(i));
            }
        }

        if (calculatedTiles.size() > 1) {
            System.out.println(calculatedTiles.size());

            for (BoardTile b: calculatedTiles) {
                System.out.println(b.getPlayTile().getScore());
            }

            if (!checkEquation(calculatedTiles)) {
                return false;
            }
        }


        //////////////// CHECK HORIZONTALLY /////////////////
        calculatedTiles.clear();
        sortYX(temp);
        calculatedTiles.add(temp.get(0));

        for (int i = 1; i < temp.size(); i++) {
            // if the tile is still in the same line
            if (temp.get(i-1).getY() == temp.get(i).getY()) {

                // if the tile is connected to the previous one
                if (temp.get(i - 1).getX() + 1 == temp.get(i).getX()) {

                    calculatedTiles.add(temp.get(i));
                }
                // if the tile is not connected to the previous one and there are tiles in calculatedTiles array
                else if (temp.get(i-1).getX() + 1 != temp.get(i).getX() && calculatedTiles.size() > 1) {

                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }

                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
                else {
                    calculatedTiles.clear();
                    calculatedTiles.add(temp.get(i));
                }
            }
            else {
                if (calculatedTiles.size() > 1) {
                    if (!checkEquation(calculatedTiles)) {
                        return false;
                    }
                }

                calculatedTiles.clear();
                calculatedTiles.add(temp.get(i));
            }
        }

        if (calculatedTiles.size() > 1) {
            System.out.println(calculatedTiles.size());

            for (BoardTile b: calculatedTiles) {
                System.out.println(b.getPlayTile().getScore());
            }

            if (!checkEquation(calculatedTiles)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Copy board tiles
     * @param boardTiles
     * @return ArrayList of BoardTile
     */
    public ArrayList<BoardTile> copyBoardTiles(ArrayList<BoardTile> boardTiles) {
        ArrayList<BoardTile> tempBoardTiles = new ArrayList<>();

        for (BoardTile boardTile: boardTiles) {
            BoardTile temp;

            if (boardTile instanceof DoubleAllBoardTile) {
                temp = new DoubleAllBoardTile();
            }
            else if (boardTile instanceof DoubleBoardTile) {
                temp = new DoubleBoardTile();
            }
            else if (boardTile instanceof NormalBoardTile) {
                temp = new NormalBoardTile();
            }
            else if (boardTile instanceof StarBoardTile) {
                temp = new StarBoardTile();
            }
            else if (boardTile instanceof TripleAllBoardTile) {
                temp = new TripleAllBoardTile();
            }
            else {
                temp = new TripleBoardTile();
            }

            temp.copy(boardTile);
            tempBoardTiles.add(temp);
        }

        return tempBoardTiles;
    }

    /**
     * Add board tiles to ArrayList of Used board tiles
     * @param boardTiles
     */
    public void addUsedBoardTiles(ArrayList<BoardTile> boardTiles) {

        for (BoardTile boardTile: boardTiles) {
            BoardTile temp;

            if (boardTile instanceof DoubleAllBoardTile) {
                temp = new DoubleAllBoardTile();
            }
            else if (boardTile instanceof DoubleBoardTile) {
                temp = new DoubleBoardTile();
            }
            else if (boardTile instanceof NormalBoardTile) {
                temp = new NormalBoardTile();
            }
            else if (boardTile instanceof StarBoardTile) {
                temp = new StarBoardTile();
            }
            else if (boardTile instanceof TripleAllBoardTile) {
                temp = new TripleAllBoardTile();
            }
            else {
                temp = new TripleBoardTile();
            }

            temp.copy(boardTile);
            usedBoardTiles.add(temp);
        }
    }

    /**
     * Check if the ArrayList of used board tiles contain location 'x'
     * @param x
     * @return boolean - true if the ArrayList contains 'x', false otherwise
     */
    public boolean isUsedTilesContainX(int x) {
        for (BoardTile boardTile : usedBoardTiles) {
            if (boardTile.getX() == x) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the ArrayList of used board tiles contain location 'y'
     * @param y
     * @return boolean - true if the ArrayList contains 'y', false otherwise
     */
    public boolean isUsedTilesContainY(int y) {
        for (BoardTile boardTile : usedBoardTiles) {
            if (boardTile.getY() == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if there is missing spot in the ArrayList of Board tiles vertically
     * @param boardTiles
     * @return boolean - true if there is missing spot in the ArrayList of Board tiles, false otherwise
     */
    public boolean hasMissingSpotVertical(ArrayList<BoardTile> boardTiles) {
        int previousY = boardTiles.get(0).getY();
        System.out.println(boardTiles.size());
        for (int j = 1; j < boardTiles.size(); j++) {
            System.out.println(previousY + ", " + boardTiles.get(j).getY());
            if (previousY + 1 != boardTiles.get(j).getY()) {
                System.out.println("return true");
                return true;
            }
            previousY = boardTiles.get(j).getY();
        }
        System.out.println("return false");
        return false;
    }

    /**
     * Check if there is missing spot in the ArrayList of Board tiles horizontally
     * @param boardTiles
     * @return boolean - true if there is missing spot in the ArrayList of Board tiles, false otherwise
     */
    public boolean hasMissingSpotHorizontal(ArrayList<BoardTile> boardTiles) {
        int previousX = boardTiles.get(0).getX();
        System.out.println(boardTiles.size());
        for (int j = 1; j < boardTiles.size(); j++) {
            System.out.println(previousX + ", " + boardTiles.get(j).getX());
            if (previousX + 1 != boardTiles.get(j).getX()) {
                System.out.println("return true");
                return true;
            }
            previousX = boardTiles.get(j).getX();
        }
        System.out.println("return false");
        return false;
    }

    public boolean isConnectedWithOldTilesVertical(ArrayList<BoardTile> boardTiles) {
        boolean isConnected = false;

        for (BoardTile boardTile: boardTiles) {
            int x = boardTile.getX();
            int y = boardTile.getY();

            System.out.println("==" + x + ", " + y);

            for (BoardTile boardTile1: getUsedBoardTiles()) {
                if (boardTile1.getX() == x && (boardTile1.getY() + 1 == y || boardTile1.getY() - 1 == y)) {
                    connectedVerticalTiles.add(boardTile1);
                    System.out.println("--" + boardTile1.getX() + ", " + boardTile1.getY());
                    isConnected = true;
                }
            }
        }

        System.out.println("**" + isConnected);
        System.out.println(connectedVerticalTiles.size());
        return isConnected;
    }

    /**
     * Check if the tiles in 'boardTiles' are connected to the old used tiles
     * @param boardTiles
     * @return boolean - true if the tiles in 'boardTiles' are connected to the old used tiles, false otherwise
     */
    public boolean isConnectedWithOldTilesHorizontal(ArrayList<BoardTile> boardTiles) {
        boolean isConnected = false;

        for (BoardTile boardTile: boardTiles) {
            int x = boardTile.getX();
            int y = boardTile.getY();

            System.out.println("==" + x + ", " + y);

            for (BoardTile boardTile1: getUsedBoardTiles()) {
                if (boardTile1.getY() == y && (boardTile1.getX() + 1 == x || boardTile1.getX() - 1 == x)) {
                    connectedHorizontalTiles.add(boardTile1);
                    System.out.println("--" + boardTile1.getX() + ", " + boardTile1.getY());
                    isConnected = true;
                }
            }
        }

        System.out.println("**" + isConnected);
        System.out.println(connectedHorizontalTiles.size());
        return isConnected;
    }

    /**
     * Check if the tiles in boardTiles have the same 'y'
     * @param boardTiles
     * @param y
     * @return boolean - true if the tiles in boardTiles have the same 'y', false otherwise
     */
    public boolean isHorizontal(ArrayList<BoardTile> boardTiles, int y) {
        boolean isHorizontal = false;

        for (BoardTile boardTile : boardTiles) {
            if (boardTile.getY() != y) {
                isHorizontal = false;
                break;
            }
            else {
                isHorizontal = true;
            }
        }

        return isHorizontal;
    }

    /**
     * Check if the tiles in boardTiles have the same 'x'
     * @param boardTiles
     * @param x
     * @return boolean - true if the tiles in boardTiles have the same 'x', false otherwise
     */
    public boolean isVertical(ArrayList<BoardTile> boardTiles, int x) {
        boolean isVertical = false;

        for (BoardTile boardTile : boardTiles) {
            if (boardTile.getX() != x) {
                isVertical = false;
                break;
            }
            else {
                isVertical = true;
            }
        }

        return isVertical;
    }

    /**
     * Sort the boardTiles vertically
     * @param boardTiles
     */
    public void sortByY(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                return ((Integer)o1.getY()).compareTo(o2.getY());
            }
        });
    }

    /**
     * Sort the boardTiles horizontally
     * @param boardTiles
     */
    public void sortByX(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                return ((Integer)o1.getX()).compareTo(o2.getX());
            }
        });
    }

    /**
     * Sort the boardTiles by x first then y
     * @param boardTiles
     */
    public void sortXY(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                int x1 = o1.getX();
                int x2 = o2.getX();

                int y1 = o1.getY();
                int y2 = o2.getY();

                if (x1 < x2) return -1;
                if (x1 > x2) return 1;
                if (y1 < y2) return -1;
                if (y1 > y2) return 1;
                return 0;
            }
        });
    }

    /**
     * Sort the boardTiles by y first then x
     * @param boardTiles
     */
    public void sortYX(ArrayList<BoardTile> boardTiles) {
        Collections.sort(boardTiles, new Comparator<BoardTile>() {
            @Override
            public int compare(BoardTile o1, BoardTile o2) {
                int x1 = o1.getX();
                int x2 = o2.getX();

                int y1 = o1.getY();
                int y2 = o2.getY();

                if (y1 < y2) return -1;
                if (y1 > y2) return 1;
                if (x1 < x2) return -1;
                if (x1 > x2) return 1;
                return 0;
            }
        });
    }


    /**
     * Convert board tiles to String form
     * @param boardTiles
     * @return String - equations from board tiles
     */
    public String toString(ArrayList<BoardTile> boardTiles) {
        String equation = "";
        for (BoardTile boardTile : boardTiles) {
            if (boardTile.getPlayTile() instanceof NumberTile) {
                String temp;
                temp = String.valueOf(((NumberTile) boardTile.getPlayTile()).getNumber());
                equation += temp;
            }
            else if (boardTile.getPlayTile() instanceof OperatorTile) {
                Character temp;
                temp = ((OperatorTile) boardTile.getPlayTile()).getSign();
                equation += temp;
            }
        }
        return equation;
    }

    /**
     * Check if there is any tile in boardTiles on the star
     * @param boardTiles
     * @return boolean - true if there is any tile in boardTiles on the star, false otherwise
     */
    public boolean isOnStar(ArrayList<BoardTile> boardTiles) {
        for (BoardTile boardTile : boardTiles) {
            if (boardTile instanceof StarBoardTile)
                return true;
        }
        return false;
    }
}
