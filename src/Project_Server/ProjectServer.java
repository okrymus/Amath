package Project_Server;

import ProjectResources.AMathConstants;
import ProjectResources.BoardTiles.BoardTile;
import ProjectResources.PlayTiles.PlayTile;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

// ProjectServer
// Programmer: Prakrit Saetang
// Date created: 9/24/16
// Date Modified: 12/1/16 (Add Playing Part)
// Date Modified: 12/2/16 (Add CHANGE TILES, PASS, DISCONNECTED Part)

/**
 * ProjectServer class - taking care of the data sending in and out between 2
 * players while the game is being played
 * @author Prakrit
 */
public class ProjectServer extends Application implements AMathConstants {

    // Variables for keeping track
    private static final int MAX_MATCHES = 500;
    private static int amountMatches = 0;

    // Networking
    private ServerSocket serverSocket;
    private static final int SOCKET = 2705;

    private Socket[] player1 = new Socket[MAX_MATCHES];
    private Socket[] player2 = new Socket[MAX_MATCHES];
    private ObjectOutputStream[] outP1 = new ObjectOutputStream[MAX_MATCHES];
    private ObjectOutputStream[] outP2 = new ObjectOutputStream[MAX_MATCHES];
    private ObjectInputStream[] inP1 = new ObjectInputStream[MAX_MATCHES];
    private ObjectInputStream[] inP2 = new ObjectInputStream[MAX_MATCHES];
    private DataInputStream[] inP1Data = new DataInputStream[MAX_MATCHES];
    private DataInputStream[] inP2Data = new DataInputStream[MAX_MATCHES];
    private DataOutputStream[] outP1Data = new DataOutputStream[MAX_MATCHES];
    private DataOutputStream[] outP2Data = new DataOutputStream[MAX_MATCHES];

    // UI
    TextArea taLog = new TextArea();
    private boolean[] isConnected = new boolean[MAX_MATCHES];

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    @Override
    public void start(Stage primaryStage) {

        // Create a scene and place it in the stage
        Scene scene = new Scene(new ScrollPane(taLog), 500, 250);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project Server");
        primaryStage.getIcons().addAll(new Image(getClass().getResourceAsStream("Resource/logoGameServer.png")));
        primaryStage.show();


        // Set onCloseRequest Event
        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        // Run the thread
        new Thread(() -> {
           try {

               // Create a new server socket
               serverSocket = new ServerSocket(SOCKET);
               // Log the activity
               Platform.runLater(() -> {
                   try {
                       taLog.appendText(new Date() + ": Server started from: " +
                               serverSocket.getInetAddress().getLocalHost().getHostAddress() + "\n");
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
                   taLog.appendText(new Date() + ": Server started at socket " + SOCKET + "\n");
               });

               Platform.runLater(() -> {
                   taLog.appendText(new Date() + ": Waiting for players to join\n");
               });

               // Loop until the amount of games reach the limit
               while (true/*amountMatches <= MAX_MATCHES*/) {

                   int numPlayers = 0;
                   Socket[] players = new Socket[2];
                   PlayTile[] playTiles = null;

                   players[0] = new Socket();
                   players[1] = new Socket();

                   // Waiting for 2 players to join the game
                   while (numPlayers < 2) {
                       System.out.print("amount of match: " + amountMatches);

                       // if there is one player waiting
                       if (numPlayers == 1) {
                           players[1] = serverSocket.accept();

                           try {
                               // Test the connection of the first player
                               // If the test is passed connect the new player as player 2
                               outP1Data[amountMatches].writeInt(1);
                               inP1Data[amountMatches].readInt();
                               System.out.println("Write/Read integer");

                               Platform.runLater(() -> {
                                   taLog.appendText(new Date() + ": player2 " + players[1].getInetAddress().getHostAddress() + " joined\n");
                               });
                               outP2[amountMatches] = new ObjectOutputStream(players[1].getOutputStream());
                               outP2Data[amountMatches] = new DataOutputStream(players[1].getOutputStream());
                               outP2Data[amountMatches].writeInt(AMathConstants.PLAYER2);
                               outP2[amountMatches].flush();

                               inP2[amountMatches] = new ObjectInputStream(players[1].getInputStream());
                               inP2Data[amountMatches] = new DataInputStream(players[1].getInputStream());
                               Platform.runLater(() -> {
                                   taLog.appendText("Stream for player2 is set up..\n");
                               });

                               outP2[amountMatches].writeObject("you are player 2");
                               outP2[amountMatches].flush();

                               outP2[amountMatches].writeObject(playTiles);
                               outP2[amountMatches].flush();
                               Platform.runLater(() -> {
                                   taLog.appendText(new Date() + ": send the bag to Player 2\n");
                               });
                               numPlayers = 2;
                           }
                           catch (Exception ex) {

                               // If the test failed, connect the new player as player 1
                               players[0] = players[1];
                               players[1] = new Socket();

                               Platform.runLater(() -> {
                                   taLog.appendText(new Date() + ": player1 " + players[0].getInetAddress().getHostAddress() + " joined\n");
                               });

                               outP1[amountMatches] = new ObjectOutputStream(players[0].getOutputStream());
                               outP1Data[amountMatches] = new DataOutputStream(players[0].getOutputStream());
                               outP1Data[amountMatches].writeInt(AMathConstants.PLAYER1);
                               outP1[amountMatches].flush();

                               inP1[amountMatches] = new ObjectInputStream(players[0].getInputStream());
                               inP1Data[amountMatches] = new DataInputStream(players[0].getInputStream());
                               Platform.runLater(() -> {
                                   taLog.appendText("Stream for player1 is set up..\n");
                               });

                               outP1[amountMatches].writeObject("You are player 1");
                               outP1[amountMatches].flush();

                               outP1[amountMatches].writeObject("Wait for player 2 to join...");
                               outP1[amountMatches].flush();

                               playTiles = (PlayTile[]) inP1[amountMatches].readObject();
                               Platform.runLater(() -> {
                                   taLog.appendText(new Date() + ": take the bag from Player 1\n");
                               });
                               numPlayers = 1;
                           }
                       }
                       else {

                           // If no player is waiting Connect the new player as player 1
                           players[0] = serverSocket.accept();
                           Platform.runLater(() -> {
                               taLog.appendText(new Date() + ": player1 " + players[0].getInetAddress().getHostAddress() + " joined\n");
                           });

                           outP1[amountMatches] = new ObjectOutputStream(players[0].getOutputStream());
                           outP1Data[amountMatches] = new DataOutputStream(players[0].getOutputStream());
                           outP1Data[amountMatches].writeInt(AMathConstants.PLAYER1);
                           outP1[amountMatches].flush();

                           inP1[amountMatches] = new ObjectInputStream(players[0].getInputStream());
                           inP1Data[amountMatches] = new DataInputStream(players[0].getInputStream());
                           Platform.runLater(() -> {
                               taLog.appendText("Stream for player1 is set up..\n");
                           });

                           outP1[amountMatches].writeObject("You are player 1");
                           outP1[amountMatches].flush();

                           outP1[amountMatches].writeObject("Wait for player 2 to join...");
                           outP1[amountMatches].flush();

                           playTiles = (PlayTile[]) inP1[amountMatches].readObject();
                           Platform.runLater(() -> {
                               taLog.appendText(new Date() + ": take the bag from Player 1\n");
                           });
                           numPlayers = 1;
                       }
                   }

                   // Set up game components
                   playTiles = (PlayTile[]) inP2[amountMatches].readObject();
                   outP1[amountMatches].writeObject(playTiles);
                   outP1[amountMatches].flush();

                   String user2Username = (String) inP2[amountMatches].readObject();
                   outP1[amountMatches].writeObject(user2Username);
                   outP1[amountMatches].flush();

                   // Run the game
                   runGame(players[0], players[1]);

                   // Increase amount of games
                   amountMatches++;
               }

           }
           catch (Exception ex) {

           }

        }).start();


    }

    /**
     * To Run the game
     * @param player1
     * @param player2
     */
    private void runGame(Socket player1, Socket player2) {
        try {

            // Change the boolean flag
            isConnected[amountMatches] = true;

            // Notify game started to both players
            outP1[amountMatches].writeObject("Game started...");
            outP2[amountMatches].writeObject("Game started...");

            // Running the thread for each player
            new Thread(new ProcessingGame(inP1[amountMatches], outP2[amountMatches],
                    inP1Data[amountMatches], outP2Data[amountMatches],
                    player1.getInetAddress().getHostAddress(), amountMatches)).start();
            new Thread(new ProcessingGame(inP2[amountMatches], outP1[amountMatches],
                    inP2Data[amountMatches], outP1Data[amountMatches],
                    player2.getInetAddress().getHostAddress(), amountMatches)).start();

        }
        catch (Exception ex) {
        }
    }

    /**
     * Class to process the game
     */
    class ProcessingGame implements Runnable {

        // Streams
        private ObjectInputStream inputStream;
        private ObjectOutputStream outputStream;
        private DataInputStream inputStreamData;
        private DataOutputStream outputStreamData;

        private String ip;
        private int index;

        /**
         * Constructor
         * @param in
         * @param out
         * @param inData
         * @param outData
         * @param ip
         * @param index
         */
        ProcessingGame(ObjectInputStream in, ObjectOutputStream out,
                       DataInputStream inData, DataOutputStream outData, String ip, int index) {
            this.inputStream = in;
            this.outputStream = out;
            this.inputStreamData = inData;
            this.outputStreamData = outData;
            this.ip = ip;
            this.index = index;
        }

        @Override
        public void run() {

            // Loop as long as both players are connected
            while (isConnected[index]) {
                try {

                    // Read the code to indicate the process
                    int code = inputStreamData.readInt();

                    if (code == CHAT) {
                        String message = inputStream.readObject().toString();
                        System.out.println("Receive: " +  message);
                        taLog.appendText("Receive: " +  message + " from " + ip + "\n");

                        outputStreamData.writeInt(CHAT);
                        outputStream.writeObject(message);
                        System.out.println("Write: " +  message);
                        taLog.appendText("Write: " +  message + "\n");
                        outputStream.flush();
                    }
                    else if (code == CHANGE_TILES) {
                        outputStreamData.writeInt(CHANGE_TILES);
                        PlayTile[] playTiles = (PlayTile[]) inputStream.readObject();
                        System.out.println("Receive: Bag");

                        outputStream.writeObject(playTiles);
                        System.out.println("Write: Bag");

                        taLog.appendText(ip + " changed tile(s) \n");
                        outputStream.flush();
                    }
                    else if (code == PLAY || code == GAME_OVER) {
                        BoardTile[] boardTiles = (BoardTile[]) inputStream.readObject();
                        System.out.println("Receive: Board");

                        if (code == PLAY)
                            outputStreamData.writeInt(PLAY);
                        else if (code == GAME_OVER)
                            outputStreamData.writeInt(GAME_OVER);
                        outputStream.writeObject(boardTiles);
                        System.out.println("Write: Board");
                        outputStream.flush();

                        PlayTile[] playTiles = (PlayTile[]) inputStream.readObject();
                        System.out.println("Receive: Bag");

                        outputStream.writeObject(playTiles);
                        System.out.println("Write: Bag");
                        outputStream.flush();

                        int score = inputStreamData.readInt();
                        System.out.println("Receive: Score");
                        outputStreamData.writeInt(score);
                        System.out.println("Receive: Score");
                        outputStreamData.flush();

                        taLog.appendText(ip + " submitted \n");
                    }
                    else if (code == PASS) {
                        outputStreamData.writeInt(PASS);
                        taLog.appendText(ip + " passed \n");
                    }
                    else if (code == DISCONNECTED) {
                        outputStreamData.writeInt(DISCONNECTED);

                        taLog.appendText(ip + " disconnected \n");
                    }
                }
                catch (Exception ex) {
                    isConnected[index] = false;
                    break;
                }
            }

            // Close all connections when the loop ends
            try {
                inputStream.close();
                outputStream.close();
                inputStreamData.close();
                outputStreamData.close();
            }
            catch (Exception e) {}

            taLog.appendText("Sockets are disconnected\n");

            return;
        }
    }
}