package ProjectResources;

import ProjectResources.BoardTiles.*;
import ProjectResources.PlayTiles.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.*;

// Board
// Programmer: Prakrit Saetang
// Date created: 9/23/16
// Date Modified: 10/15/16 (Add 'getKey' Method)
// Date Modified: 11/25/16 (Add 'ChoosePlayTile' Class)
// Date Modified: 11/27/16 (Add 'updateBoard', 'clearBoard', 'takeOutTiles',
//                          'disableBoard', 'enableBoard' Methods)

/**
 * Board Class - to hold all Board Tiles
 * @author Prakrit
 */
public class Board implements AMathConstants {

    // Lists to hold all board tiles
    private Map<String, BoardTile> boardTileMap;  // Board Tiles
    private Map<String, ImageView> imageViewMap;  // ImageViews of Board Tiles

    // onHandTiles to set up 'OnActionListener'
    private OnHandTiles onHandTiles;

    /**
     * Constructor
     * @param onHandTiles
     */
    // constructors
    public Board(OnHandTiles onHandTiles) {
        this.onHandTiles = onHandTiles;
        boardTileMap = new HashMap<>();
        imageViewMap = new HashMap<>();
        setupBoardTileArray();
    }

    // Getters

    /**
     * Get Map of BoardTile
     * @return Map of BoardTile
     */
    public Map<String, BoardTile> getBoardTileMap() {
        return boardTileMap;
    }

    /**
     * Get used Board Tiles in Array form
     * @return BoardTile[]
     */
    public BoardTile[] getBoardUsedArray() {
        BoardTile[] newBoard = new BoardTile[getUsedBoardTile().size()];
        for (int i = 0; i < newBoard.length; i++) {
            newBoard[i] = getUsedBoardTile().get(i);
        }
        return newBoard;
    }

    /**
     * Get key of specific BoardTile
     * @param boardTile
     * @return String - key of 'boardTile'
     */
    public String getKey(BoardTile boardTile) {
        return String.valueOf(boardTile.getX()) + " " + String.valueOf(boardTile.getY());
    }

    // private methods

    /**
     * To set up Board when it is initialized
     */
    private void setupBoardTileArray() {

        // To temporarily hold a key
        String mapID;

        // Set up Star Board Tile
        {
            StarBoardTile temp = new StarBoardTile();
            temp.setImgURL("Resource/Star.png");
            temp.setX(7);
            temp.setY(7);
            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);
        }

        // Set up DoubleBoardTile
        for (int count = 0; count < locationsDoubleTile.length; count++) {
            DoubleBoardTile temp = new DoubleBoardTile();
            temp.setImgURL("Resource/x2piece.png");

            temp.setX(locationsDoubleTile[count][0]);
            temp.setY(locationsDoubleTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);

        }

        // Set up DoubleAllBoardTile
        for (int count = 0; count < locationsDoubleAllTile.length; count++) {
            DoubleAllBoardTile temp = new DoubleAllBoardTile();
            temp.setImgURL("Resource/x2equation.png");

            temp.setX(locationsDoubleAllTile[count][0]);
            temp.setY(locationsDoubleAllTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);
        }

        // Set up TripleBoardTile
        for (int count = 0; count < locationsTripleTile.length; count++) {
            TripleBoardTile temp = new TripleBoardTile();
            temp.setImgURL("Resource/x3piece.png");

            temp.setX(locationsTripleTile[count][0]);
            temp.setY(locationsTripleTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);
        }

        // Set up TripleAllBoardTile
        for (int count = 0; count < locationsTripleAllTile.length; count++) {
            TripleAllBoardTile temp = new TripleAllBoardTile();
            temp.setImgURL("Resource/x3equation.png");

            temp.setX(locationsTripleAllTile[count][0]);
            temp.setY(locationsTripleAllTile[count][1]);

            mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
            boardTileMap.put(mapID, temp);

        }

        // Set up NormalBoardTile
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                NormalBoardTile temp = new NormalBoardTile();
                temp.setImgURL("Resource/blank.png");

                temp.setX(i);
                temp.setY(j);

                mapID = String.valueOf(temp.getX()) + " " + String.valueOf(temp.getY());
                if (!boardTileMap.containsKey(mapID)) {
                    boardTileMap.put(mapID, temp);
                }
            }
        }
    }

    // public methods

    /**
     * Get the pane of the Board
     * @return GridPane
     */
    public GridPane getBoardPane() {

        GridPane boardPane = new GridPane();
        boardPane.setPrefHeight(600);
        boardPane.setPrefWidth(600);
        boardPane.setVgap(1);
        boardPane.setHgap(1);

        // Set up each board tile to the pane
        for ( Map.Entry<String, BoardTile> entry : boardTileMap.entrySet()) {
            String key = entry.getKey();
            BoardTile temp = entry.getValue();
            ImageView imageView = new ImageView(temp.getTileImage());
            imageView.setFitHeight(40);
            imageView.setFitWidth(40);
            imageViewMap.put(key, imageView);
        }

        // Set up 'OnMouseClickedListener' to each image view
        for ( Map.Entry<String, ImageView> entry : imageViewMap.entrySet()) {

            entry.getValue().setOnMouseClicked(event -> {
                if(event.getButton().equals(MouseButton.PRIMARY)){
                    // Double Click
                    if(event.getClickCount() == 2){
                        System.out.println("Double clicked");

                        if (boardTileMap.get(entry.getKey()).isPlaced() == true) {
                            entry.getValue().setImage(boardTileMap.get(entry.getKey()).getTileImage());

                            boardTileMap.get(entry.getKey()).getPlayTile().setUsed(false);
                            onHandTiles.add(boardTileMap.get(entry.getKey()).getPlayTile());

                            boardTileMap.get(entry.getKey()).setPlayTile(null);
                            boardTileMap.get(entry.getKey()).setPlaced(false);
                        }
                    }
                    // One click
                    else {
                        if (onHandTiles.getTileSelected() != -1 && !boardTileMap.get(entry.getKey()).isPlaced()) {

                            int indexTileSelected = onHandTiles.getTileSelected();

                            // Pop up dialog to choose a tile for Plus/Minus Tile
                            if (onHandTiles.getTile(indexTileSelected) instanceof PlusOrMinusTile) {
                                System.out.println("Plus or minus");
                                onHandTiles.clearUsed();
                                ChoosePlayTiles choosePlayTiles =
                                        new ChoosePlayTiles(
                                                entry.getValue(),
                                                entry.getKey(),
                                                onHandTiles.getTile(indexTileSelected),
                                                indexTileSelected,
                                                PLUS_SIGN, MINUS_SIGN);
                                choosePlayTiles.display();
                            }
                            // Pop up dialog to choose a tile for Multiply/Divide Tile
                            else if (onHandTiles.getTile(indexTileSelected) instanceof MultipleOrDivideTile) {
                                System.out.println("Multiply or divide");
                                onHandTiles.clearUsed();
                                ChoosePlayTiles choosePlayTiles =
                                        new ChoosePlayTiles(
                                                entry.getValue(),
                                                entry.getKey(),
                                                onHandTiles.getTile(indexTileSelected),
                                                indexTileSelected,
                                                MULTIPLY_SIGN, DIVIDE_SIGN);
                                choosePlayTiles.display();
                            }
                            // Pop up dialog to choose a tile for Blank Tile
                            else if (onHandTiles.getTile(indexTileSelected) instanceof BlankTile) {
                                System.out.println("Blank");
                                onHandTiles.clearUsed();
                                ChoosePlayTiles choosePlayTiles =
                                        new ChoosePlayTiles(
                                                entry.getValue(),
                                                entry.getKey(),
                                                onHandTiles.getTile(indexTileSelected),
                                                indexTileSelected,
                                                0);
                                choosePlayTiles.displayBlankTile();
                            }
                            // Other Play Tiles
                            else {
                                entry.getValue().setImage(onHandTiles.getTile(indexTileSelected).getTileImage());

                                boardTileMap.get(entry.getKey()).setPlaced(true);
                                boardTileMap.get(entry.getKey()).setPlayTile(onHandTiles.getTile(indexTileSelected));

                                onHandTiles.remove(indexTileSelected);

                                onHandTiles.updatePane();
                            }
                        }
                    }
                }
            });

            ImageView tempImageView = entry.getValue();
            BoardTile tempTile = boardTileMap.get(entry.getKey());
            boardPane.add(tempImageView, tempTile.getX(), tempTile.getY());
        }

        return boardPane;
    }

    /**
     * Get used Board Tiles
     * @return ArrayList of used Board Tiles
     */
    public ArrayList<BoardTile> getUsedBoardTile() {
        ArrayList<BoardTile> onUsedBoardTile = new ArrayList<BoardTile>();

        for ( Map.Entry<String, BoardTile> entry : boardTileMap.entrySet()) {
            if (entry.getValue().isPlaced() == true) {
                onUsedBoardTile.add(entry.getValue());
            }
        }

        return onUsedBoardTile;
    }

    /**
     * Update the Board
     * @param boardTiles
     */
    public void updateBoardTile(BoardTile[] boardTiles) {
        for (BoardTile boardTile : boardTiles) {
            boardTile.setTileImage(boardTile.getImgURL());
            boardTile.getPlayTile().setTileImage(boardTile.getPlayTile().getImgUrl());
            String key = String.valueOf(boardTile.getX()) + " " + String.valueOf(boardTile.getY());
            System.out.println(key);
            boardTileMap.get(key).copy(boardTile);
            imageViewMap.get(key).setImage(boardTile.getPlayTile().getTileImage());
        }
    }

    /**
     * Take out used Board Tiles when the turn changed
     */
    public void takeOutUsedTiles() {

        for (BoardTile boardTile: getUsedBoardTile()) {
            boardTileMap.remove(getKey(boardTile));
            imageViewMap.get(getKey(boardTile)).setDisable(true);
        }

    }

    /**
     * Clear all unused Play Tiles out of the board
     */
    public void clearBoard() {
        for (BoardTile boardTile: getUsedBoardTile()) {
            imageViewMap.get(getKey(boardTile)).setImage(boardTile.getTileImage());
            boardTileMap.get(getKey(boardTile)).getPlayTile().setUsed(false);

            onHandTiles.add(boardTileMap.get(getKey(boardTile)).getPlayTile());
            boardTileMap.get(getKey(boardTile)).setPlayTile(null);
            boardTileMap.get(getKey(boardTile)).setPlaced(false);
        }
    }

    /**
     * Disable Action Listener of the board
     */
    public void disableBoard() {
        for ( Map.Entry<String, BoardTile> entry : boardTileMap.entrySet()) {
            imageViewMap.get(entry.getKey()).setDisable(true);
        }
    }

    /**
     * Enable Action Listener of the board
     */
    public void enableBoard() {
        for ( Map.Entry<String, BoardTile> entry : boardTileMap.entrySet()) {
            imageViewMap.get(entry.getKey()).setDisable(false);
        }
    }

    //========================== Choose Play Tiles class ==============================
    /**
     * Class to display the dialog to choose Tiles
     */
    class ChoosePlayTiles {

        private int tileSelected = 0;
        private Stage stage = new Stage();
        private ArrayList<Integer> choices = new ArrayList<>();
        private PlayTile playTile;
        private ImageView imageView;
        private String key;

        /**
         * Constructor
         * @param boardTile
         * @param key
         * @param playTile
         * @param index
         * @param playTiles
         */
        ChoosePlayTiles(ImageView boardTile, String key, PlayTile playTile, int index, int... playTiles) {
            stage.initModality(Modality.APPLICATION_MODAL);
            this.imageView = boardTile;
            this.playTile = playTile;
            this.key = key;
            this.tileSelected = index;

            for (int i : playTiles) {
                choices.add(i);
            }
        }

        /**
         * Get the index of the selected tile
         * @return int - index of the selected tile
         */
        public int getTileSelected() {
            return tileSelected;
        }

        /**
         * Display the dialog for Plus/Minus or Multiply/Divide Tile
         */
        public void display() {

            HBox tilesPane = new HBox();
            tilesPane.setSpacing(10);

            // Set up Radio Buttons
            ToggleGroup toggleGroupChoices = new ToggleGroup();

            for (int i : choices) {
                RadioButton radioButton = new RadioButton();
                switch (i) {
                    case PLUS_SIGN: radioButton = new RadioButton("Plus"); break;
                    case MINUS_SIGN: radioButton = new RadioButton("Minus"); break;
                    case MULTIPLY_SIGN: radioButton = new RadioButton("Multiply"); break;
                    case DIVIDE_SIGN: radioButton = new RadioButton("Divide"); break;
                }

                radioButton.setToggleGroup(toggleGroupChoices);
                tilesPane.getChildren().add(radioButton);
            }

            VBox mainPane = new VBox();
            mainPane.setAlignment(Pos.CENTER);
            mainPane.setSpacing(10);
            mainPane.setPadding(new Insets(10,10,10,10));

            // Submit Button
            Button btSubmit = new Button("Submit");
            btSubmit.setPrefWidth(200);
            btSubmit.setOnAction(event -> {
                String choice = ((RadioButton)toggleGroupChoices.getSelectedToggle()).getText();

                switch (choice) {
                    case "Plus":
                        playTile = new PlusTile();
                        System.out.println("Plus"); break;
                    case "Minus":
                        playTile = new MinusTile();
                        System.out.println("Minus"); break;
                    case "Multiply":
                        playTile = new MultiplyTile();
                        System.out.println("Multiply"); break;
                    case "Divide":
                        playTile = new DivideTile();
                        System.out.println("Divide"); break;
                }

                System.out.println(tileSelected);
                stage.close();
                imageView.setImage(playTile.getTileImage());

                boardTileMap.get(key).setPlaced(true);
                boardTileMap.get(key).setPlayTile(playTile);

                onHandTiles.remove(tileSelected);

                onHandTiles.updatePane();
            });

            // Set up the pane
            mainPane.getChildren().addAll(tilesPane, btSubmit);
            stage.setOnCloseRequest(event -> {
                onHandTiles.clearUsed();
                onHandTiles.updatePane();
            });


            Scene scene = new Scene(mainPane);
            scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
            stage.setTitle("Choose Tiles");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }


        /**
         * Display the dialog for Blank Tile
         */
        public void displayBlankTile() {
            HBox tilesPane = new HBox();
            tilesPane.setSpacing(10);

            Label lbChooseNumberTile = new Label("Choose the tile: ");

            // Set up the ComboBox
            ComboBox<String> cbNumberTile = new ComboBox<>();
            cbNumberTile.setPrefWidth(100);


            for (int i = 0; i <= 20; i++) {
                String temp = String.valueOf(i);
                cbNumberTile.getItems().add(temp);
            }

            cbNumberTile.getItems().add("+");
            cbNumberTile.getItems().add("-");
            cbNumberTile.getItems().add("x");
            cbNumberTile.getItems().add("/");
            cbNumberTile.getItems().add("=");

            cbNumberTile.getSelectionModel().selectFirst();

            tilesPane.getChildren().addAll(lbChooseNumberTile, cbNumberTile);

            VBox mainPane = new VBox();
            mainPane.setAlignment(Pos.CENTER);
            mainPane.setSpacing(10);
            mainPane.setPadding(new Insets(10,10,10,10));

            // Submit Button
            Button btSubmit = new Button("Submit");
            btSubmit.setPrefWidth(200);
            btSubmit.setOnAction(event -> {

                String tile = cbNumberTile.getSelectionModel().getSelectedItem();

                try {
                    int number = Integer.parseInt(tile);
                    String tempImageUrl = "Resource/" + String.valueOf(allNumberNumber[number]) + ".png";
                    int tempNumber = allNumberNumber[number];
                    int tempScore = allNumberScore[number];
                    playTile = new NumberTile(tempScore, false, tempImageUrl, tempNumber);
                    System.out.println("Selected number: " + number);
                }
                catch (Exception ex) {
                    switch (tile) {
                        case "+":
                            playTile = new PlusTile();
                            System.out.println("Plus"); break;
                        case "-":
                            playTile = new MinusTile();
                            System.out.println("Minus"); break;
                        case "x":
                            playTile = new MultiplyTile();
                            System.out.println("Multiply"); break;
                        case "/":
                            playTile = new DivideTile();
                            System.out.println("Divide"); break;
                        case "=":
                            playTile = new EqualTile();
                            System.out.println("Equal"); break;
                    }
                }

                System.out.println(tileSelected);
                stage.close();

                imageView.setImage(playTile.getTileImage());
                boardTileMap.get(key).setPlaced(true);
                boardTileMap.get(key).setPlayTile(playTile);

                onHandTiles.remove(tileSelected);

                onHandTiles.updatePane();
            });

            // Set up the pane
            mainPane.getChildren().addAll(tilesPane, btSubmit);
            stage.setOnCloseRequest(event -> {
                onHandTiles.clearUsed();
                onHandTiles.updatePane();
            });

            Scene scene = new Scene(mainPane, 400, 100);
            scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
            stage.setTitle("Choose Tiles");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        }
    }
}
