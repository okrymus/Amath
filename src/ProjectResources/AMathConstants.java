package ProjectResources;

// AMathConstants
// Programmer: Prakrit Saetang
// Date created: 10/27/16
// Date modified: 12/1/16 (Add Networking Constants)

/**
 * Interface containing all constants needed to play AMath
 * @author Prakrit
 */
public interface AMathConstants {
    
    public static final int GAME_OVER_SCORE = 150;

    // ======================= [ Networking constants ] ==============================
    public static final int PLAYER1 = 11;
    public static final int PLAYER2 = 12;

    public static final int CHAT = 21;
    public static final int PLAY = 22;
    public static final int CHANGE_TILES = 23;
    public static final int PASS = 24;
    public static final int GAME_OVER = 25;
    public static final int DISCONNECTED = 26;
    public static final int MATCH_INFO = 27;
    public static final int MATCH_INFO_DISCONNECTED = 28;

    /*
    public static final int WIN = 31;
    public static final int LOSE = 32;
    */

    // ======================= [ On Hand ] ==============================
    public static final int MAX_AMOUNT_ON_HAND = 8;

    // ======================= [ Play Tile ] ==============================
    public static final int AMOUNT = 0;
    public static final int NUMBER = 1;
    public static final int SCORE = 2;

    // each PlayTile [amount, number, score]
    public static final int[] info0 = new int[]{5, 0, 1};
    public static final int[] info1 = new int[]{6, 1, 1};
    public static final int[] info2 = new int[]{6, 2, 1};
    public static final int[] info3 = new int[]{5, 3, 1};
    public static final int[] info4 = new int[]{5, 4, 2};
    public static final int[] info5 = new int[]{4, 5, 2};
    public static final int[] info6 = new int[]{4, 6, 2};
    public static final int[] info7 = new int[]{4, 7, 2};
    public static final int[] info8 = new int[]{4, 8, 2};
    public static final int[] info9 = new int[]{4, 9, 2};
    public static final int[] info10 = new int[]{2, 10, 3};
    public static final int[] info11 = new int[]{1, 11, 4};
    public static final int[] info12 = new int[]{2, 12, 3};
    public static final int[] info13 = new int[]{1, 13, 6};
    public static final int[] info14 = new int[]{1, 14, 4};
    public static final int[] info15 = new int[]{1, 15, 4};
    public static final int[] info16 = new int[]{1, 16, 4};
    public static final int[] info17 = new int[]{1, 17, 6};
    public static final int[] info18 = new int[]{1, 18, 4};
    public static final int[] info19 = new int[]{1, 19, 7};
    public static final int[] info20 = new int[]{1, 20, 5};
    public static final int[] infoPlus = new int[]{4, 0, 2};
    public static final int[] infoMinus = new int[]{4, 0, 2};
    public static final int[] infoPlusOrMinus = new int[]{5, 0, 1};
    public static final int[] infoMultiply = new int[]{4, 0, 2};
    public static final int[] infoDivide = new int[]{4, 0, 2};
    public static final int[] infoMultipleOrDivide = new int[]{4, 0, 1};
    public static final int[] infoEqualSign = new int[]{11, 0, 1};
    public static final int[] infoBlank = new int[]{4};

    public static final int[] allNumberAmount = new int[]{
            info0[AMOUNT], info1[AMOUNT], info2[AMOUNT], info3[AMOUNT], info4[AMOUNT], info5[AMOUNT],
            info6[AMOUNT], info7[AMOUNT], info8[AMOUNT], info9[AMOUNT], info10[AMOUNT], info11[AMOUNT],
            info12[AMOUNT], info13[AMOUNT], info14[AMOUNT], info15[AMOUNT], info16[AMOUNT], info17[AMOUNT],
            info18[AMOUNT], info19[AMOUNT], info20[AMOUNT]
    };

    public static final int[] allNumberNumber = new int[]{
            info0[NUMBER], info1[NUMBER], info2[NUMBER], info3[NUMBER], info4[NUMBER], info5[NUMBER],
            info6[NUMBER], info7[NUMBER], info8[NUMBER], info9[NUMBER], info10[NUMBER], info11[NUMBER],
            info12[NUMBER], info13[NUMBER], info14[NUMBER], info15[NUMBER], info16[NUMBER], info17[NUMBER],
            info18[NUMBER], info19[NUMBER], info20[NUMBER]
    };

    public static final int[] allNumberScore = new int[]{
            info0[SCORE], info1[SCORE], info2[SCORE], info3[SCORE], info4[SCORE], info5[SCORE],
            info6[SCORE], info7[SCORE], info8[SCORE], info9[SCORE], info10[SCORE], info11[SCORE],
            info12[SCORE], info13[SCORE], info14[SCORE], info15[SCORE], info16[SCORE], info17[SCORE],
            info18[SCORE], info19[SCORE], info20[SCORE]
    };

    public static final int[] allOperatorAmount = new int[]{
            infoPlus[AMOUNT], infoMinus[AMOUNT],
            infoPlusOrMinus[AMOUNT], infoMultiply[AMOUNT], infoDivide[AMOUNT], infoMultipleOrDivide[AMOUNT],
            infoEqualSign[AMOUNT], infoBlank[AMOUNT]
    };


    // ======================= [ Board Tile ] ==============================

    //to hold locations of board tiles [x, y]

    // Double Tiles
    public static final int locationsDoubleTile[][] = new int[][]{
            {0, 3}, {0, 11}, {2, 6}, {2, 8}, {3, 0}, {3, 7}, {3, 14}, {6, 2}, {6, 6}, {6, 8}
            , {6, 12}, {7, 3}, {7, 11}, {8, 2}, {8, 6}, {8, 8}, {8, 12}, {11, 0}, {11, 7}, {11, 14}
            , {12, 6}, {12, 8}, {14, 3}, {14, 11}
    };

    // Double All Tiles
    public static final int locationsDoubleAllTile[][] = new int[][]{
            {1, 1}, {1, 13}, {2, 2}, {2, 12}, {3, 3}, {3, 11}, {11, 3}, {11, 11}, {12, 2}, {12, 12}
            , {13, 1}, {13, 13}
    };

    // Triple Tiles
    public static final int locationsTripleTile[][] = new int[][]{
            {1, 5}, {1, 9}, {4, 4}, {4, 10}, {5, 1}, {5, 5}, {5, 9}, {5, 13}, {9, 1}, {9, 5}
            , {9, 9}, {9, 13}, {10, 4}, {10, 10}, {13, 5}, {13, 9}
    };

    // Triple All Tiles
    public static final int locationsTripleAllTile[][] = new int[][]{
            {0, 0}, {0, 7}, {0, 14}, {7, 0}, {7, 14}, {14, 0}, {14, 7}, {14, 14}
    };


    public static final int PLUS_SIGN = 0;
    public static final int MINUS_SIGN = 1;
    public static final int MULTIPLY_SIGN = 2;
    public static final int DIVIDE_SIGN = 3;
}
