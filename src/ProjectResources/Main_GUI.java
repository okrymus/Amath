package ProjectResources;

import ProjectResources.BoardTiles.BoardTile;
import ProjectResources.BoardTiles.DoubleAllBoardTile;
import ProjectResources.BoardTiles.TripleAllBoardTile;
import ProjectResources.PlayTiles.EqualTile;
import ProjectResources.PlayTiles.PlayTile;
import Project_Login.UserID;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

// Main_GUI
// Programmer: Prakrit Saetang
// Date created: 9/23/16
// Date Modified: 12/1/16 (Add networking part)
// Date Modified: 12/4/16 (Add Passing turn event)
// Date Modified: 12/9/16 (Make the chat room with different color text)

/**
 * Main_GUI Class - to handle the game
 * @author Prakrit
 */
public class Main_GUI implements AMathConstants {

    // Networking instances
    private Socket socket;

    private ObjectInputStream fromServerObject;
    private ObjectOutputStream toServerObject;
    private DataInputStream fromServerData;
    private DataOutputStream toServerData;
    
    private DataOutputStream toDatabase;

    // Game components
    private Bag bag;
    private OnHandTiles onHandTiles;
    private PlayTools playTools;
    private Board board;

    // Boolean Flags
    private boolean isGameOver = false;
    private boolean isGameStarted = false;
    private boolean isDisconnected = false;
    private boolean isMyTurn;

    // Integers keeping track
    private int totalScore = 0, opponentTotalScore = 0;
    private int numPlayer, numPlayerDisconnected;
    private static int turnCounter = 1;

    // GUI
    private TextFlow chatFlow;
    private ScrollPane scrollPane;

    // Information
    private String opponentUsername = "";

    /**
     * Display the game window
     * @param user
     * @param toDatabase
     * @param ip
     * @param port
     */
    public void display(UserID user, DataOutputStream toDatabase, String ip, int port) {
        this.toDatabase = toDatabase;
        Stage primaryStage = new Stage();
        primaryStage.setResizable(false);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        // Tiles Container
        bag = new Bag();
        playTools = new PlayTools();
        
        // Tiles on hand
        onHandTiles = new OnHandTiles();
        
        // Draw tiles from bag to on hand
        for (int i = 0; i < 8; i++) {
            PlayTile temp = bag.draw();
            onHandTiles.add(temp);
        }

        // Board
        board = new Board(onHandTiles);

        // Chat room
        chatFlow = new TextFlow();
        chatFlow.setStyle("-fx-background-color: white;");
        chatFlow.setPrefWidth(390);
        chatFlow.setPrefHeight(200);
        chatFlow.setPadding(new Insets(10,10,10,10));

        scrollPane = new ScrollPane();
        scrollPane.setPrefWidth(390);
        scrollPane.setPrefHeight(200);
        scrollPane.setFitToWidth(true);
        scrollPane.setVvalue(1.0);
        scrollPane.setContent(chatFlow);

        TextArea chatRoom = new TextArea();
        chatRoom.setWrapText(true);
        chatRoom.setEditable(false);

        HBox typingArea = new HBox();
        TextField tfTyping = new TextField();
        tfTyping.setPromptText("Type here..");
        tfTyping.setPrefWidth(300);
        Button btTyping = new Button("Send");
        btTyping.setPrefWidth(90);
        typingArea.getChildren().addAll(tfTyping, btTyping);

        // Game Info
        Label lbTotalScore = new Label("Total Score: ");
        Label lbTotalOpponentScore = new Label("Opponent Total Score: ");
        Label lbTilesLeft = new Label();

        HBox processButtonArea = new HBox();
        processButtonArea.setAlignment(Pos.CENTER);
        processButtonArea.setSpacing(10);
        Button btSubmit = new Button("Submit");
        btSubmit.setPrefWidth(110);
        Button btChangeTiles = new Button("Change Tiles");
        btChangeTiles.setPrefWidth(110);
        Button btPass = new Button("Pass");
        btPass.setPrefWidth(110);
        processButtonArea.getChildren().addAll(btSubmit, btPass, btChangeTiles);

        // Right Pane (Chat room, Game info, Tiles on hand)
        VBox rightPane = new VBox();
        rightPane.setSpacing(10);
        rightPane.setPadding(new Insets(20, 10, 10, 10));
        rightPane.setPrefHeight(613);
        rightPane.setPrefWidth(400);
        rightPane.setId("rightPane");
        rightPane.getChildren().addAll(scrollPane, typingArea, /*lbCurrentScore,*/
                lbTotalScore, lbTotalOpponentScore, lbTilesLeft, processButtonArea, onHandTiles.getPane());

        // Left Pane (Board)
        VBox leftPane = new VBox();
        leftPane.getChildren().add(board.getBoardPane());

        // Main Pane (Left Pane, Right Pane)
        HBox mainPane = new HBox();
        mainPane.getChildren().addAll(leftPane, rightPane);

        double leftPaneWidth = new Board(new OnHandTiles()).getBoardPane().getPrefWidth();

        Scene scene = new Scene(mainPane, leftPaneWidth + rightPane.getPrefWidth(), rightPane.getPrefHeight());
        scene.getStylesheets().add(getClass().getResource("Resource/StyleSheet.css").toString());
        primaryStage.setTitle("A - math");
        primaryStage.setScene(scene);
        primaryStage.show();

        //==================================[ Action Listener ]=======================================

        // onCloseRequest (Signal the server and close connections)
        primaryStage.setOnCloseRequest(event -> {

            if (isGameStarted == true) {
                try {
                    System.out.println("\n\nin setting disconnected\n\n");
                    toServerData.writeInt(DISCONNECTED);
                    isDisconnected = true;
                    numPlayerDisconnected = numPlayer;
                    /*
                    toDatabase.writeInt(LOSE);
                    toDatabase.writeUTF(user.getUsername());
                    
                    toDatabase.writeInt(totalScore);
                    */
                }
                catch (Exception ex) {}
            }
            
            if (numPlayer == PLAYER1 && !opponentUsername.equals("")) {
                    try {
                        
                        System.out.println("\n\n" + user.getUsername().trim());
                        System.out.println(opponentUsername);
                        System.out.println(numPlayerDisconnected);
                        //System.out.println(user.getUsername().trim());
                        System.out.println(isDisconnected);
                        if (isDisconnected) {
                            System.out.println("is disconnected\n\n");
                            this.toDatabase.writeInt(MATCH_INFO_DISCONNECTED);
                            this.toDatabase.flush();
                            this.toDatabase.writeUTF(user.getUsername().trim());
                            this.toDatabase.flush();
                            this.toDatabase.writeUTF(opponentUsername);
                            this.toDatabase.flush();
                            if (numPlayerDisconnected == numPlayer) {
                                this.toDatabase.writeUTF(user.getUsername().trim());
                                this.toDatabase.flush();
                            }
                            else {
                                this.toDatabase.writeUTF(opponentUsername);
                                this.toDatabase.flush();
                            }
                            //this.toDatabase.flush();
                        }
                        else {
                            System.out.println("In match info\n\n");
                            this.toDatabase.writeInt(MATCH_INFO);
                            this.toDatabase.flush();
                            this.toDatabase.writeUTF(user.getUsername().trim());
                            this.toDatabase.flush();
                            this.toDatabase.writeInt(totalScore);
                            this.toDatabase.flush();
                            this.toDatabase.writeUTF(opponentUsername);
                            this.toDatabase.flush();
                            this.toDatabase.writeInt(opponentTotalScore);
                            this.toDatabase.flush();
                        }
                        
                        //toDatabase.writeUTF(user.getUsername().trim());

                        /*
                        writeGameInfo(user.getUsername(), opponentUsername,
                        user.getUsername(), toDatabase);
                        */
                                           
                    }
                    catch (Exception ex) {}
            }   

            isGameOver = true;
            try {
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            }
            catch (Exception ex) {}
        });

        // TextField Typing
        tfTyping.setOnAction(event -> {
            sendMsg(user, tfTyping, chatRoom);
        });

        // Button to send message
        btTyping.setOnAction(event -> {
            sendMsg(user, tfTyping, chatRoom);
        });

        // Submit button
        btSubmit.setOnAction(event -> {

            // get used board tiles
            ArrayList<BoardTile> onUsedBoardTile = board.getUsedBoardTile();

            // check the location
            int x = onUsedBoardTile.get(0).getX(), y = onUsedBoardTile.get(0).getY();

            // validate the move
            if (!validateMove(onUsedBoardTile, x, y)) {
                return;
            }

            // check if the equations are true
            boolean isEquationTrue = false;
            if (playTools.checkWholeBoard(onUsedBoardTile)) {
                showAlert(playTools.getAlertText(), Alert.AlertType.INFORMATION);
                isEquationTrue = true;
            }
            else {
                showAlert(playTools.getAlertText(), Alert.AlertType.ERROR);
                isEquationTrue = false;
            }

            // if the equations are true
            if (isEquationTrue == true) {

                try {
                    // draw tiles to hand
                    for (int i = onHandTiles.getOnHandPlayTileArray().size(); i < MAX_AMOUNT_ON_HAND; i++) {
                        try {
                            PlayTile temp = bag.draw();
                            onHandTiles.add(temp);
                        }
                        catch (Exception ex) {
                            break;
                        }
                    }

                    // update total score
                    totalScore += getTotalScore();

                    // send status
                    if (totalScore >= GAME_OVER_SCORE || bag.getTilesLeft() == 0) {
                        
                        isGameStarted = false;
                        isGameOver = true;
                        toServerData.writeInt(GAME_OVER);

                        appendTextFlow("Game Over!!\n", Color.RED);
                        chatRoom.appendText("Game Over!!\n");

                        if (totalScore > opponentTotalScore) {
                            appendTextFlow("You Win!!\n", Color.RED);
                            chatRoom.appendText("You Win!!\n");

                            /*
                            try {
                                toDatabase.writeInt(WIN);
                                toDatabase.writeUTF(user.getUsername());

                                toDatabase.writeInt(totalScore);

                                isGameStarted = false;
                            }
                            catch (Exception ex) {}
                            */
                        }
                        else {
                            appendTextFlow("You lose!!\n", Color.RED);
                            chatRoom.appendText("You lose!!\n");

                            /*
                            try {
                                toDatabase.writeInt(LOSE);
                                toDatabase.writeUTF(user.getUsername());

                                toDatabase.writeInt(totalScore);

                                isGameStarted = false;
                            }
                            catch (Exception ex) {}
                            */
                        }

                        //setResultToDatabase(user, toDatabase);
                    }
                    else {
                        toServerData.writeInt(PLAY);
                    }

                    // send board to sever
                    BoardTile[] newBoard = board.getBoardUsedArray();
                    toServerObject.writeObject(newBoard);

                    // send bag to server
                    PlayTile[] newBag = bag.getBagArray();
                    toServerObject.writeObject(newBag);

                    // send total score to server
                    toServerData.writeInt(totalScore);

                    // update part
                    updateTotalScore(lbTotalScore, totalScore);

                    isMyTurn = false;
                    updateTileLeft(lbTilesLeft);
                    onHandTiles.updatePane();

                    btSubmit.setDisable(true);
                    btPass.setDisable(true);
                    btChangeTiles.setDisable(true);

                    playTools.addUsedBoardTiles(board.getUsedBoardTile());

                    board.takeOutUsedTiles();
                    board.disableBoard();
                }
                catch (Exception ex) {}
            }
            else {
                return;
            }
        });

        // Pass button
        btPass.setOnAction(event -> {

            // Confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Dialog");
            alert.setContentText("Would you like to skip your turn?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                // user chose OK
                try {
                    toServerData.writeInt(PASS);

                    isMyTurn = false;
                    btSubmit.setDisable(true);
                    btPass.setDisable(true);
                    btChangeTiles.setDisable(true);

                    board.clearBoard();
                    board.disableBoard();
                }
                catch (Exception ex) {
                }
            } else {
                // user chose CANCEL or closed the dialog
            }
        });

        // Change Tiles Button
        btChangeTiles.setOnAction(event -> {

            onHandTiles.clearUsed();
            ChangeTile changeTile = new ChangeTile();
            changeTile.display(onHandTiles, bag);

            // if tiles are changed
            if (changeTile.isTileChanged()) {
                try {
                    toServerData.writeInt(CHANGE_TILES);

                    // send bag to server
                    PlayTile[] newBag = bag.getBagArray();
                    toServerObject.writeObject(newBag);
                    isMyTurn = false;
                    btSubmit.setDisable(true);
                    btPass.setDisable(true);
                    btChangeTiles.setDisable(true);

                    board.clearBoard();
                    board.disableBoard();
                }
                catch (Exception ex) {
                }
            }
        });


        //==================================[ CONNECTION ]=======================================

        try {
            socket = new Socket(ip, port);
            fromServerObject = new ObjectInputStream(socket.getInputStream());
            toServerObject = new ObjectOutputStream(socket.getOutputStream());
            fromServerData = new DataInputStream(socket.getInputStream());
            toServerData = new DataOutputStream(socket.getOutputStream());

            appendTextFlow("You are connected\n", Color.BROWN);
            chatRoom.appendText("You are connected\n");

            // get Number of player from the server
            numPlayer = fromServerData.readInt();
            System.out.println(numPlayer);

        }
        catch (Exception ex) {}

        new Thread(() -> {
            try {

                // Display Number of Player
                String notifyPlayerNum = fromServerObject.readObject().toString();
                Platform.runLater(() -> {
                    appendTextFlow(notifyPlayerNum + "\n", Color.BROWN);
                    chatRoom.appendText(notifyPlayerNum + "\n");
                });

                // if the user is player #1
                if (numPlayer == PLAYER1) {

                    // Display wait for player #2
                    String notifyWaitForPlayer2 = fromServerObject.readObject().toString();
                    Platform.runLater(() -> {
                        {
                            Text txConnected = new Text(notifyWaitForPlayer2 + "\n");
                            txConnected.setFill(Color.BROWN);
                            chatFlow.getChildren().add(txConnected);
                        }
                        chatRoom.appendText(notifyWaitForPlayer2 + "\n");
                        btSubmit.setDisable(true);
                        btPass.setDisable(true);
                        btChangeTiles.setDisable(true);
                        lbTilesLeft.setText("Tiles Left: " + bag.getTilesLeft());
                    });

                    // send the bag to server
                    PlayTile[] newBag = bag.getBagArray();
                    toServerObject.writeObject(newBag);

                    // test connection
                    fromServerData.readInt();
                    toServerData.writeInt(1);

                    // get the bag from server
                    newBag = (PlayTile[]) fromServerObject.readObject();
                    bag.newPlayTiles(newBag);
                    Platform.runLater(() -> {
                        updateTileLeft(lbTilesLeft);
                    });

                    // Set up opponent name
                    opponentUsername = (String) fromServerObject.readObject();
                    System.out.println(opponentUsername);

                    isMyTurn = true;
                }
                // if the user is player #2
                else if (numPlayer == PLAYER2) {

                    // get the bag from server
                    PlayTile[] newBag = (PlayTile[]) fromServerObject.readObject();

                    bag.newPlayTiles(newBag);

                    // draw tiles from the bag
                    for (int i = 0; i < 8; i++) {
                        PlayTile temp = bag.draw();
                        onHandTiles.replace(i, temp);
                    }

                    // update pane
                    Platform.runLater(() -> {
                        onHandTiles.updatePane();
                        updateTileLeft(lbTilesLeft);
                        btSubmit.setDisable(true);
                        btPass.setDisable(true);
                        btChangeTiles.setDisable(true);

                        board.disableBoard();
                    });

                    System.out.println(bag.getTilesLeft());

                    // send the bag to server
                    newBag = bag.getBagArray();
                    toServerObject.writeObject(newBag);
                    toServerObject.flush();

                    isMyTurn = false;

                    // write username to server
                    toServerObject.writeObject(user.getUsername());
                    toServerObject.flush();
                }

                // Notify when game started ( 2 players joined)
                String notifyGameStarted = fromServerObject.readObject().toString();
                Platform.runLater(() -> {
                    {
                        Text txConnected = new Text(notifyGameStarted + "\n");
                        txConnected.setFill(Color.BROWN);
                        chatFlow.getChildren().add(txConnected);
                    }
                    chatRoom.appendText(notifyGameStarted + "\n");
                    isGameStarted = true;

                    if (numPlayer == PLAYER1) {
                        btSubmit.setDisable(false);
                        btPass.setDisable(false);
                        btChangeTiles.setDisable(false);
                    }
                });

                //============================[ Playing Part ]====================================
                new Thread(() -> {
                    // loop until the game is over
                    while (!isGameOver) {

                        try {
                            // Read the code to indicate an event to proceed
                            int code = fromServerData.readInt();

                            if (code == CHAT) {
                                String message = fromServerObject.readObject().toString();
                                System.out.println("Receive: " +  message);
                                Platform.runLater(() -> {
                                    appendTextFlow(message + "\n", Color.BLACK);
                                    scrollPane.localToScene(0, 0);
                                    scrollPane.setVvalue(1.0);
                                    chatRoom.appendText(message + "\n");
                                });
                            }
                            else if (code == CHANGE_TILES) {
                                // get bag
                                PlayTile[] playTiles = (PlayTile[]) fromServerObject.readObject();
                                System.out.println("Receive: Bag");

                                Platform.runLater(() -> {
                                    bag.newPlayTiles(playTiles);
                                    updateTileLeft(lbTilesLeft);

                                    btSubmit.setDisable(false);
                                    btPass.setDisable(false);
                                    btChangeTiles.setDisable(false);

                                    board.enableBoard();

                                    appendTextFlow("Your Turn!!\n", Color.GREEN);
                                    chatRoom.appendText("Your Turn!!\n");
                                });
                            }
                            else if (code == PASS) {
                                Platform.runLater(() -> {
                                    btSubmit.setDisable(false);
                                    btPass.setDisable(false);
                                    btChangeTiles.setDisable(false);

                                    board.enableBoard();

                                    appendTextFlow("Your Turn!!\n", Color.GREEN);
                                    chatRoom.appendText("Your Turn!!\n");
                                });
                            }
                            else if (code == DISCONNECTED) {
                                Platform.runLater(() -> {
                                    btSubmit.setDisable(true);
                                    btPass.setDisable(true);
                                    btChangeTiles.setDisable(true);

                                    board.disableBoard();
                                    
                                    //if (totalScore != 0) {
                                        
                                    appendTextFlow("Your opponent is disconnected..\nYou win!!", Color.RED);
                                    chatRoom.appendText("Your opponent is disconnected..\nYou win!!");

                                    //}
                                    
                                    /*
                                    try {
                                        toDatabase.writeInt(WIN);
                                        toDatabase.writeUTF(user.getUsername());
                                        
                                        toDatabase.writeInt(totalScore);

                                        isGameStarted = false;
                                    }
                                    catch (Exception ex) {}
                                    */
                                    numPlayerDisconnected = (numPlayer == PLAYER1 ? PLAYER2 : PLAYER1);
                                    isDisconnected = true;
                                    isGameStarted = false;
                                });
                            }
                            else if (code == PLAY || code == GAME_OVER) {

                                // get board
                                BoardTile[] boardTiles = (BoardTile[]) fromServerObject.readObject();
                                System.out.println("Receive: Board");
                                // get bag
                                PlayTile[] playTiles = (PlayTile[]) fromServerObject.readObject();
                                System.out.println("Receive: Bag");
                                // get score
                                opponentTotalScore = fromServerData.readInt();
                                System.out.println("Receive: Score");

                                // update part
                                isMyTurn = true;
                                turnCounter++;

                                Platform.runLater(() -> {

                                    board.updateBoardTile(boardTiles);
                                    bag.newPlayTiles(playTiles);
                                    updateTileLeft(lbTilesLeft);
                                    updateTotalScore(lbTotalOpponentScore, opponentTotalScore);

                                    playTools.addUsedBoardTiles(board.getUsedBoardTile());
                                    board.takeOutUsedTiles();

                                    if (code == GAME_OVER) {
                                        appendTextFlow("Game Over!!\n", Color.RED);
                                        chatRoom.appendText("Game Over!!\n");
                                        
                                        if (totalScore > opponentTotalScore) {
                                            appendTextFlow("You win!!\n", Color.RED);
                                            chatRoom.appendText("You win!!\n");

                                            /*
                                            try {
                                                toDatabase.writeInt(WIN);
                                                toDatabase.writeUTF(user.getUsername());

                                                toDatabase.writeInt(totalScore);

                                                isGameStarted = false;
                                            }
                                            catch (Exception ex) {}
                                            */
                                        }
                                        else {
                                            appendTextFlow("You lose!!\n", Color.RED);
                                            chatRoom.appendText("You lose!!\n");

                                            /*
                                            try {
                                                toDatabase.writeInt(LOSE);
                                                toDatabase.writeUTF(user.getUsername());

                                                toDatabase.writeInt(totalScore);

                                                isGameStarted = false;
                                            }
                                            catch (Exception ex) {}
                                            */
                                        }

                                        //setResultToDatabase(user, toDatabase);
                                        isGameStarted = false;
                                        isGameOver = true;
                                    }
                                    else if (code == PLAY) {
                                        btSubmit.setDisable(false);
                                        btPass.setDisable(false);
                                        btChangeTiles.setDisable(false);

                                        board.enableBoard();

                                        appendTextFlow("Your Turn!!\n", Color.GREEN);
                                        chatRoom.appendText("Your Turn!!\n");
                                    }

                                });

                                if (code == GAME_OVER || isGameOver) {
                                    break;
                                }
                            }
                        }
                        catch (Exception ex) {}
                    }
                }).start();

            }
            catch (Exception ex) {

            }
        }).start();
    }

    
    //==================================[ PRIVATE METHODS ]=======================================

    /**
     * Validate a move
     * @param onUsedBoardTile
     * @param x
     * @param y
     * @return boolean - true if the move is valid, false otherwise
     */
    private boolean validateMove(ArrayList<BoardTile> onUsedBoardTile, int x, int y) {

        // If it is the first play, check if one tile is on the star
        if (!playTools.isOnStar(onUsedBoardTile) && (playTools.getUsedBoardTiles().size() == 0)) {
            showAlert("Invalid Input: One Tile must be placed on the star", Alert.AlertType.ERROR);
            return false;
        }

        // If the tiles are lined up Horizontally
        if (playTools.isHorizontal(onUsedBoardTile, y)) {

            // sort the tiles
            playTools.sortByX(onUsedBoardTile);

            // if it is the first play
            if ((playTools.getUsedBoardTiles().size() == 0)) {

                // check if there is missing spot in the line
                if (playTools.hasMissingSpotHorizontal(onUsedBoardTile)) {
                    showAlert("Invalid Input", Alert.AlertType.ERROR);
                    return false;
                }

                // check for an equal sign
                int numEqualSign = 0;
                for (BoardTile bt: onUsedBoardTile) {
                    if (bt.getPlayTile() instanceof EqualTile)
                        numEqualSign++;
                }

                if (numEqualSign == 0) {
                    showAlert("Invalid Input: An equation must contain at least one equal sign", Alert.AlertType.ERROR);
                    return false;
                }
            }
            else {

                // check if at least one tile is connected to the old tiles
                if (!playTools.isConnectedWithOldTilesVertical(onUsedBoardTile) &&
                        !playTools.isConnectedWithOldTilesHorizontal(onUsedBoardTile)) {
                    showAlert("Invalid Input: no connection", Alert.AlertType.ERROR);
                    return false;
                }
            }
        }
        // If the tiles are lined up Vertically
        else if (playTools.isVertical(onUsedBoardTile, x)) {

            // sort the tiles
            playTools.sortByY(onUsedBoardTile);

            // if it is the first play
            if ((playTools.getUsedBoardTiles().size() == 0)) {

                // check if there is missing spot in the line
                if (playTools.hasMissingSpotVertical(onUsedBoardTile)) {
                    showAlert("Invalid Input", Alert.AlertType.ERROR);
                    return false;
                }

                // check for an equal sign
                int numEqualSign = 0;
                for (BoardTile bt: onUsedBoardTile) {
                    if (bt.getPlayTile() instanceof EqualTile)
                        numEqualSign++;
                }

                if (numEqualSign == 0) {
                    showAlert("Invalid Input: An equation must contain at least one equal sign", Alert.AlertType.ERROR);
                    return false;
                }
            }
            else {

                // check if at least one tile is connected to the old tiles
                if (!playTools.isConnectedWithOldTilesVertical(onUsedBoardTile) &&
                        !playTools.isConnectedWithOldTilesHorizontal(onUsedBoardTile)) {
                    showAlert("Invalid Input: no connection", Alert.AlertType.ERROR);
                    return false;
                }
            }
        }
        else {
            showAlert("Invalid Input", Alert.AlertType.ERROR);
            return false;
        }

        return true;
    }


    /**
     * Show Alert Dialog
     * @param string
     * @param AlertType
     */
    private void showAlert(String string, Alert.AlertType AlertType) {
        Alert alert = new Alert(AlertType, string);
        alert.setResizable(false);
        alert.show();
    }

    /**
     * Update Tiles left in the bag on the pane
     * @param label
     */
    private void updateTileLeft(Label label) {
        label.setText("Tiles Left: " + bag.getTilesLeft());
    }

    /**
     * Update total scores on the pane
     * @param label
     * @param score
     */
    private void updateTotalScore(Label label, int score) {
        String text = label.getText();
        text = text.substring(0, text.indexOf(':') + 1);
        label.setText(text + " " + score);
    }

    /**
     * Calculate the score
     * @return int - total score
     */
    private int getTotalScore() {
        int totalScore = 0;
        int tempTotal = 0;

        // get score from each play tile
        for (BoardTile boardTile: board.getUsedBoardTile()) {
            tempTotal += boardTile.getPlayTile().getScore();
        }

        // get bonus scores
        for (BoardTile boardTile: board.getUsedBoardTile()) {
            if (boardTile instanceof DoubleAllBoardTile) {
                ((DoubleAllBoardTile) boardTile).setTotalScore(tempTotal);
                totalScore += boardTile.getScore();
            }
            else if (boardTile instanceof TripleAllBoardTile) {
                ((TripleAllBoardTile) boardTile).setTotalScore(tempTotal);
                totalScore += boardTile.getScore();
            }
            else {
                totalScore += boardTile.getScore();
            }
        }

        return totalScore;
    }

    /**
     * Send result to the database server
     * @param user UserID
     * @param toDatabase DataOutputStream
     */
    /*
    private void setResultToDatabase(UserID user, DataOutputStream toDatabase) {

        if (totalScore > opponentTotalScore) {

            try {
                toDatabase.writeInt(WIN);
                toDatabase.writeUTF(user.getUsername());
                
                toDatabase.writeInt(totalScore);

                if (numPlayer == PLAYER1) {
                    writeGameInfo(user.getUsername(), opponentUsername,
                            user.getUsername(), toDatabase);
                }
            }
            catch (Exception ex) {}
        }
        else {
            try {
                toDatabase.writeInt(LOSE);
                toDatabase.writeUTF(user.getUsername());
                
                toDatabase.writeInt(totalScore);

                if (numPlayer == PLAYER1) {
                    writeGameInfo(user.getUsername(), opponentUsername,
                            opponentUsername, toDatabase);
                }
            }
            catch (Exception ex) {}
        }

        isGameStarted = false;
    }
    */

    /**
     * Send message to the chat room
     * @param user UserID
     * @param tfTyping TextField
     * @param chatRoom TextArea
     */
    private void sendMsg(UserID user, TextField tfTyping, TextArea chatRoom) {
        if (!tfTyping.getText().equals("")) {
            try {
                toServerData.writeInt(CHAT);
                String message = user.getUsername() + ": " + tfTyping.getText();
                toServerObject.writeObject(message);
                toServerObject.flush();
                Platform.runLater(() -> {
                    appendTextFlow(message + "\n", Color.ORCHID);
                    tfTyping.setText("");
                    chatRoom.appendText(message + "\n");
                });
            }
            catch (Exception ex) {}
        }
    }

    /**
     * Append message to the text flow
     * @param msg
     * @param color
     */
    private void appendTextFlow(String msg, Color color) {
        Text txTemp = new Text(msg);
        txTemp.setFill(color);
        chatFlow.getChildren().add(txTemp);
        scrollPane.localToScene(0, 0);
        scrollPane.setVvalue(1.0);
    }

    /**
     * Send the game info to the database server
     * @param user1
     * @param user2
     * @param winner
     * @param toDatabase
     */
    /*
    private void writeGameInfo(String user1, String user2, String winner,
                               DataOutputStream toDatabase) {
        try {
            toDatabase.writeInt(MATCH_INFO);
            toDatabase.writeUTF(user1);
            toDatabase.writeUTF(user2);
            toDatabase.writeUTF(winner);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    */
}

