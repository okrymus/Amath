package Project_Login;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.sql.Date;
import java.util.*;
import ProjectResources.AMathConstants;
import javafx.application.Platform;

import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;


// Login_Server
// Programmer: Panupong Leenawarat/ Prakrit Saetang
// Date creted: 12/2/16
// Date Modified: 10/11/2016 Add multi thread server to handle with login
// Date Modified: 10/12/2016 Add the database and create table for store user's information
// Date Modified: 10/13/2016 Template for server display
// Date modifeid: 10/15/2016 Add switch case for function the client requests
// Date modified: 10/16/2016 Add executeSQLDataBaseCertainData method to find that database certains user's information
// Date modified: 12/18/2016 Take out the case WIN and LOSE, add case MACTH_INFO and MATCH_INFO_DISCONNECTED


/**
 * Login_Server class - to handle log in, sign up, forget password, change server IP events
 * @author PRAKRIT/PANUPONG
 */
public class Login_Server extends Application implements Login_Constants, AMathConstants {

    private int clientCount = 0;
    boolean isSignUpSucceeded = false;

    // Admin email address
    String adminEmail;
    String adminEmailPassword;

    // Connection to the database
    private Connection connection;
    private ServerSocket serverSocket;
    private final static int PORT = 8000;

    // Statement to execute SQL commands
    private Statement statement;

    // Text area to enter SQL commands
    private TextArea jtasqlCommand = new TextArea();

    // Text area to display results from SQL commands
    private TextArea jtaSQLResult = new TextArea();


    // JDBC info for a database connection
    private TextField jtfUsername = new TextField();
    private PasswordField jpfPassword = new PasswordField();

    // Buttons
    private Button jbtExecuteSQL = new Button("Execute SQL Command");
    private Button jbtClearSQLCommand = new Button("Clear");
    private Button jbtConnectDB1 = new Button("Connect to Database");
    private Button jbtClearSQLResult = new Button("Clear Result");
    private Button jbtChangeAdminEmail = new Button("Change Admin Email");

    // Create titled
    private Label titledBorder1 = new Label("Enter an SQL Command (Table name: account, games)");
    private Label titledBorder2 = new Label("SQL Execution Result");
    private Label titledBorder3 = new Label("Enter Database Information");

    // Labels
    private Label lbUsername = new Label("Username: ");
    private Label lbPassword = new Label("Password: ");
    private Label lbConnectionStatus = new Label("Database Status: ");
    private Label jlblConnectionStatus = new Label("No connection now");

    private Text txDbStatus = new Text("disconnected");


    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setOnCloseRequest(event -> {
            System.exit(0);
        });

        jtaSQLResult.setStyle("-fx-font-family: monospace");

        // Database info
        VBox paneDBInfo = new VBox();
        paneDBInfo.setPrefWidth(230);
        paneDBInfo.setSpacing(5);
        lbConnectionStatus.setPadding(new Insets(10,0,0,0));
        txDbStatus.setFill(Color.RED);
        paneDBInfo.getChildren().addAll(titledBorder3, lbUsername, jtfUsername, lbPassword, jpfPassword, jbtConnectDB1, lbConnectionStatus, txDbStatus);


        // SQL Command
        VBox paneSQLCommand = new VBox();
        paneSQLCommand.setSpacing(10);
        paneSQLCommand.getChildren().addAll(titledBorder1, jtasqlCommand);

        // Top pane
        HBox paneTop = new HBox();
        paneTop.setSpacing(30);
        paneTop.setPadding(new Insets(5,5,5,5));
        paneTop.getChildren().addAll(paneDBInfo, paneSQLCommand);

        // Buttons pane
        HBox paneButtons = new HBox();
        paneButtons.setAlignment(Pos.CENTER);
        paneButtons.setSpacing(10);
        paneButtons.setPadding(new Insets(5,5,5,5));
        paneButtons.getChildren().addAll(jbtExecuteSQL, jbtClearSQLCommand, jbtClearSQLResult, jbtChangeAdminEmail);

        VBox paneSQLResult = new VBox();
        paneSQLResult.getChildren().addAll(titledBorder2, jtaSQLResult);

        VBox paneMain = new VBox();
        paneMain.setPadding(new Insets(10, 10, 10, 10));
        paneMain.getChildren().addAll(paneTop, paneButtons, paneSQLResult);

        // Create a scene and place it in the stage
        Scene scene = new Scene(paneMain);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Project Login Server");
        primaryStage.getIcons().addAll(new Image(getClass().getResourceAsStream("Resource/logoLoginServer.png")));
        primaryStage.show();

        jbtExecuteSQL.setOnAction(event ->  {
            executeSQL();
        });

        jbtConnectDB1.setOnAction(event ->  {
                connectToDB();
        });

        jbtClearSQLCommand.setOnAction(event ->  {
                jtasqlCommand.setText(null);
        });

        jbtClearSQLResult.setOnAction(event ->  {
                jtaSQLResult.setText(null);
        });

        jbtChangeAdminEmail.setOnAction(event -> {
                adminEmailDialogPopUp();
        });

        new Thread(() -> {
            try {
                // Create a server socket
                serverSocket = new ServerSocket(PORT);
                jtaSQLResult.setText("MultiThreadServer started at "
                        + new java.util.Date() + '\n');

                while (true) {
                    // Listen for a new connection request
                    Socket socket = serverSocket.accept();

                    // Increment clientNo
                    clientCount++;

                    //Platform.runLater(() -> {
                    // Display the client number
                    jtaSQLResult.appendText("Starting thread for client " + clientCount
                            + " at " + new java.util.Date() + '\n');

                    // Find the client's host name, and IP address
                    InetAddress inetAddress = socket.getInetAddress();
                    jtaSQLResult.appendText("Client " + clientCount + "'s host name is "
                            + inetAddress.getHostName() + "\n");
                    jtaSQLResult.appendText("Client " + clientCount + "'s IP Address is "
                            + inetAddress.getHostAddress() + "\n");
                    //});

                    // Create and start a new thread for the connection
                    new Thread(new HandleAClient(socket)).start();
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }).start();
    }


    /**
     * Define the thread class for handling new connection
     */
    class HandleAClient implements Runnable {

        private Socket socket; // A connected socket

        /**
         * Construct a thread
         */
        public HandleAClient(Socket socket) {
            this.socket = socket;
        }

        /**
         * Run a thread
         */
        public void run() {
            try {
                // Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                ObjectInputStream inputFromClientObj = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputToClientObj = new ObjectOutputStream(socket.getOutputStream());

                // Continuously serve the client
                while (true) {
                    // Receive radius from the client
                    switch (inputFromClient.readInt()) {
                        case LOGIN: {
                            String uN = inputFromClient.readUTF();    // Get username from client
                            String pW = inputFromClient.readUTF();    // Get password from client

                            String sql = "SELECT " + Cols.USERNAME + ", " + Cols.PASSWORD + " from " + TABLE_NAME +
                                    " where " + Cols.USERNAME + " = \'" + uN + "\' and " +
                                    Cols.PASSWORD + " =\'" + pW + "\';";

                            // If the databate certains data which matching with username and password
                            executeSQLDataBaseCertainData(sql);
                            if (executeSQLDataBaseCertainData(sql)) {

                                if (pW.equals(getUser(uN).getPassword()) && uN.equals(getUser(uN).getUsername())) {
                                    outputToClient.writeBoolean(true);    // Return true to client
                                    outputToClient.flush();
                                   

                                    outputToClientObj.writeObject(getUser(uN));
                                    outputToClientObj.flush();
                                    System.out.println(getUser(uN).getEmail());
                                    Platform.runLater(() -> {
                                        jtaSQLResult.appendText("\n" + uN + " Logged In...\n");
                                    });

                                    outputToClientObj.writeObject(getRankingList());
                                    outputToClientObj.flush();
                                }
                                else {
                                    outputToClient.writeBoolean(false);
                                    outputToClient.flush();
                                }
                            }
                            else {
                                outputToClient.writeBoolean(false);     // Return false to client when the username or password is not existed
                                outputToClient.flush();
                            }

                        }
                        break;
                        case SIGNUP: {
                            
                            isSignUpSucceeded = false;

                            try {
                                // Get Username
                                UserID newID = (UserID) inputFromClientObj.readObject();

                                // SQL command to insert a new account
                                String sql = "INSERT INTO " + TABLE_NAME + " VALUES (\'" + newID.getUsername() + "\',\'"
                                        + newID.getPassword() + "\',\'" + newID.getEmail() + "\', NOW(), 0, 0, 0);";
                                executeSQL(sql);

                                // Send back the result
                                if (isSignUpSucceeded == true) {
                                    outputToClient.writeBoolean(true);
                                    outputToClient.flush();
                                    
                                    Platform.runLater(() -> {
                                        jtaSQLResult.appendText("\n" + newID.getUsername() + " Signed Up...\n");
                                    });
                                }
                                else {
                                    outputToClient.writeBoolean(false);
                                    outputToClient.flush();
                                }

                            }
                            catch (Exception ex) {
                                outputToClient.writeBoolean(isSignUpSucceeded);
                                outputToClient.flush();
                            }

                        }
                        break;
                        case CHANGE_PASSWORD: {
                            try {
                                
                                // Get UserID object
                                UserID user = (UserID) inputFromClientObj.readObject();

                                // SQL command
                                String sql = "UPDATE " + TABLE_NAME +
                                        " SET " + Cols.PASSWORD + " = '" + user.getPassword() + "\' " +
                                        "WHERE " + Cols.USERNAME + " = \'" + user.getUsername() + "\';";

                                executeSQL(sql);

                                // Send back the result
                                if (isSignUpSucceeded == true) {
                                    outputToClient.writeBoolean(true);
                                    
                                    Platform.runLater(() -> {
                                        jtaSQLResult.appendText("\n" + user.getUsername() + " Changed Password...\n");
                                    });
                                }
                                else {
                                    outputToClient.writeBoolean(false);
                                    outputToClient.flush();
                                }

                            }
                            catch (Exception ex) {
                                outputToClient.writeBoolean(isSignUpSucceeded);
                                outputToClient.flush();
                            }

                        }
                        break;
                        case FORGET_PASSWORD: {
                            try {
                                
                                // Get email
                                String email = inputFromClient.readUTF();

                                // Serach for UserID associated with the email
                                ArrayList<UserID> temp = getUserByEmail(email);

                                for (UserID id : temp) {
                                    System.out.println(id.getUsername());
                                }

                                // Send email
                                Mail mail = new Mail(temp, adminEmail, adminEmailPassword);

                                mail.sendEmail();

                                outputToClient.writeBoolean(true);

                                Platform.runLater(() -> {
                                    jtaSQLResult.appendText("Sent email to " + email + "\n");
                                });
                            }
                            catch (Exception ex) {
                                outputToClient.writeBoolean(false);
                                ex.printStackTrace();
                            }
                        }
                        break;
                        case MATCH_INFO: {
                            
                            System.out.println("In match info");
                            
                            // READ MACTH INFO
                            String user1 = inputFromClient.readUTF();
                            int score1 = inputFromClient.readInt();
                          
                            String user2 = inputFromClient.readUTF();
                            int score2 = inputFromClient.readInt();
                          
                            
                            System.out.println("\n" + user1 + " " + score1 + " " + user2 + " " + score2 + "\n");

                            Platform.runLater(() -> {
                                jtaSQLResult.appendText("\n" + user1 + " " + score1 + " " + user2 + " " + score2 + "\n");
                            });
                          
                            // Determine WINNER & LOSER
                            String winner, loser;
                            int winnerScore, loserScore;
                            if (score1 > score2) {
                                winner = user1;
                                winnerScore = score1;
                                loser = user2;
                                loserScore = score2;
                            }
                            else {
                                winner = user2;
                                winnerScore = score2;
                                loser = user1;
                                loserScore = score1;
                            }
                         

                            // SQL command
                            String sql = "INSERT INTO " + TABLE_GAME + " VALUES(0, \'" + user1 + "\', \'" +
                                    user2 + "\', \'" + winner + "\', NOW());";

                            executeSQL(sql);
                            
                            Platform.runLater(() -> {
                                jtaSQLResult.appendText(user1 + " vs " + user2 + ", " + winner + " won \n");
                            });
                            
                            // Loser
                            UserID user = getUser(loser);
                            int newAvgScore = (user.getAvgScore() == 0 ? loserScore : (user.getAvgScore() + loserScore) / 2);
                            user.setAvgScore(newAvgScore);
                            user.setTotalMatch(user.getTotalMatch() + 1);

                            System.out.println("RECEIVE LOSE");

                            sql = "UPDATE " + TABLE_NAME +
                                    " SET " + Cols.AVG_SCORE + " = " + user.getAvgScore() + ", " +
                                    Cols.TOTAL_MATCH + " = " + user.getTotalMatch() + " " +
                                    "WHERE " + Cols.USERNAME + " = \'" + user.getUsername() + "\';";

                            executeSQL(sql);
                            
                            // Winner
                            user = getUser(winner);
                            newAvgScore = (user.getAvgScore() == 0 ? winnerScore : (user.getAvgScore() + winnerScore) / 2);
                            user.setAvgScore(newAvgScore);
                            user.setTotalMatch(user.getTotalMatch() + 1);
                            user.setTotalWin(user.getTotalWin() + 1);

                            System.out.println("RECEIVE WIN");

                            sql = "UPDATE " + TABLE_NAME +
                                    " SET " + Cols.AVG_SCORE + " = " + user.getAvgScore() + ", " +
                                    Cols.TOTAL_MATCH + " = " + user.getTotalMatch() + ", " +
                                    Cols.TOTAL_WIN + " = " + user.getTotalWin() + " " +
                                    "WHERE " + Cols.USERNAME + " = \'" + user.getUsername() + "\';";

                            executeSQL(sql);
                            
                            Platform.runLater(() -> {
                                jtaSQLResult.appendText("Macth info updated\n");
                            });
                        }
                        break;
                        case MATCH_INFO_DISCONNECTED: {
                            
                            // Get user1, user2, user disconnected
                            String user1 = inputFromClient.readUTF();
                            String user2 = inputFromClient.readUTF();
                            String userDisconnected = inputFromClient.readUTF();
                            
                            // Determine the winner and loser
                            String winner, loser;
                            if (userDisconnected.equals(user1)) {
                                winner = user2;
                                loser = user1;
                            }
                            else {
                                winner = user1;
                                loser = user2;
                            }
                            
                            // SQL Command
                            String sql = "INSERT INTO " + TABLE_GAME + " VALUES(0, \'" + user1 + "\', \'" +
                                    user2 + "\', \'" + winner + "\', NOW());";

                            executeSQL(sql);
                            Platform.runLater(() -> {
                                jtaSQLResult.appendText(user1 + " vs " + user2 + ", " + winner + " won \n");
                            });
                            
                            // Loser
                            UserID user = getUser(loser);
                            user.setTotalMatch(user.getTotalMatch() + 1);

                            System.out.println("RECEIVE LOSE");

                            sql = "UPDATE " + TABLE_NAME +
                                    " SET " + Cols.TOTAL_MATCH + " = " + user.getTotalMatch() + " " +
                                    "WHERE " + Cols.USERNAME + " = \'" + user.getUsername() + "\';";

                            executeSQL(sql);
                            
                            // Winner
                            user = getUser(winner);
                            user.setTotalMatch(user.getTotalMatch() + 1);
                            user.setTotalWin(user.getTotalWin() + 1);

                            System.out.println("RECEIVE WIN");

                            sql = "UPDATE " + TABLE_NAME +
                                    " SET " + Cols.TOTAL_MATCH + " = " + user.getTotalMatch() + ", " +
                                    Cols.TOTAL_WIN + " = " + user.getTotalWin() + " " +
                                    "WHERE " + Cols.USERNAME + " = \'" + user.getUsername() + "\';";

                            executeSQL(sql);
                            Platform.runLater(() -> {
                                jtaSQLResult.appendText("Macth info updated\n");
                            });
                        }
                        break;
                        case REQUEST_HISTORY: {
                            
                            // Get username
                            String username = inputFromClient.readUTF();
                            // Write history list to the client
                            outputToClientObj.writeObject(getHistoryList(username));
                            outputToClientObj.flush();
                        }
                        break;
                        case REQUEST_RANK: {
                            
                            // Write ranking list to a client
                            outputToClientObj.writeObject(getRankingList());
                            outputToClientObj.flush();
                        }
                        break;
                        case REQUEST_USER_INFO: {
                            
                            // Get username
                            String username = inputFromClient.readUTF();
                            
                            // Write user info to the client
                            outputToClientObj.writeObject(getUser(username));
                            outputToClientObj.flush();
                          
                        }
                        break;
                    }
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }



    /**
     * Connect to DB
     */
    private void connectToDB() {
        // Get database information from the user input
        String driver = DRIVER;
        String url = URL;
        String username = jtfUsername.getText().trim();
        String password = jpfPassword.getText().trim();

        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            Statement s1 = connection.createStatement();

            // Create database if the database is not exit with input
            String queryString = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME + ";";
            s1.executeUpdate(queryString);
            s1.executeUpdate("use " + DATABASE_NAME);

            queryString = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(\n" +
                    "   username varchar(20) not null,\n" +
                    "   password varchar(20) not null,\n" +
                    "   email varchar(60),\n" +
                    "   date_created timestamp,\n" +
                    "   avg_score int,\n" +
                    "   total_match int,\n" +
                    "   total_win int,\n" +
                    "   PRIMARY KEY(username)\n" +
                    ")";

            s1.executeUpdate(queryString);

            queryString = "CREATE TABLE IF NOT EXISTS " + TABLE_GAME + "(\n" +
                    "     game_id INT NOT NULL AUTO_INCREMENT,\n" +
                    "     user1 varchar(20) NOT NULL,\n" +
                    "     user2 varchar(20) NOT NULL,\n" +
                    "     winner varchar(20),\n" +
                    "     game_date timestamp,\n" +
                    "     PRIMARY KEY (game_id)\n" +
                    ");";

            s1.executeUpdate(queryString);

            queryString = "USE " + DATABASE_NAME + ";";
            s1.executeUpdate(queryString);

            jlblConnectionStatus.setText("Connected to " + url);
            jtaSQLResult.setText("Connected to " + url + '\n');
            txDbStatus.setFill(Color.GREEN);
            txDbStatus.setText("connected at "
                    + serverSocket.getInetAddress().getLocalHost().getHostAddress()
                    + " at port " + PORT);
            adminEmailDialogPopUp();
            //lbConnectionStatus.setText("Database Status: Connected");
        } catch (SQLException err) {
            System.out.println(err.getMessage());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Execute SQL commands
     */
    private void executeSQL() {
        if (connection == null) {
            jtaSQLResult.setText("Please connect to a database first");
            return;
        } else {
            String sqlCommands = jtasqlCommand.getText().trim();
            String[] commands = sqlCommands.replace('\n', ' ').split(";");

            for (String aCommand : commands) {
                if (aCommand.trim().toUpperCase().startsWith("SELECT")) {

                    if (aCommand.contains(TABLE_NAME))
                        processSQLSelect(aCommand, TABLE_NAME);
                    else if (aCommand.contains(TABLE_GAME))
                        processSQLSelect(aCommand, TABLE_GAME);
                } else {
                    processSQLNonSelect(aCommand);
                }
            }
        }
    }

    private void executeSQL(String sql) {
        if (connection == null) {
            jtaSQLResult.setText("Please connect to a database first");
            return;
        } else {
            String sqlCommands = sql.trim();
            String[] commands = sqlCommands.replace('\n', ' ').split(";");

            for (String aCommand : commands) {
                if (aCommand.trim().toUpperCase().startsWith("SELECT")) {

                    if (aCommand.contains(TABLE_NAME))
                        processSQLSelect(aCommand, TABLE_NAME);
                    else if (aCommand.contains(TABLE_GAME))
                        processSQLSelect(aCommand, TABLE_GAME);
                } else {
                    processSQLNonSelect(aCommand);
                }
            }
        }
    }


    /**
     * Execute SQL commands
     */
    private boolean executeSQLDataBaseCertainData(String stmt) {
        if (connection == null) {
            jtaSQLResult.setText("Please connect to a database first");
            return false;
        } else {
            String sqlCommands = stmt.trim();
            String[] commands = sqlCommands.replace('\n', ' ').split(";");

            for (String aCommand : commands) {
                if (aCommand.trim().toUpperCase().startsWith("SELECT")) {
                    try {
                        // Get a new statement for the current connection
                        statement = connection.createStatement();

                        // Execute a SELECT SQL command
                        ResultSet resultSet = statement.executeQuery(aCommand);

                        return resultSet.next();
                    } catch (SQLException ex) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    /**
     * Execute SQL SELECT commands
     * @param sqlCommand
     * @param table
     */
    private void processSQLSelect(String sqlCommand, String table) {
        jtaSQLResult.appendText("\n");
        try {
            // Get a new statement for the current connection
            statement = connection.createStatement();

            // Execute a SELECT SQL command
            ResultSet resultSet = statement.executeQuery(sqlCommand);

            // Find the number of columns in the result set
            int columnCount = resultSet.getMetaData().getColumnCount();
            String row = "";

            // Display column names
            if (table == TABLE_NAME) {

                row = String.format("%-20s %-20s %-30s %-25s %-15s %-15s %-15s", Cols.USERNAME, Cols.PASSWORD, Cols.EMAIL,
                        Cols.DATE_CREATED, Cols.AVG_SCORE, Cols.TOTAL_MATCH, Cols.TOTAL_WIN);

            }
            else if (table == TABLE_GAME) {

                row = String.format("%-20s %-20s %-20s %-30s", ColsGame.USER1, ColsGame.USER2, ColsGame.WINNER,
                        ColsGame.GAME_DATE);

            }

            System.out.println(row);
            jtaSQLResult.appendText(row + '\n');

            while (resultSet.next()) {
                // Reset row to empty
                row = "";

                //String.
                if (table == TABLE_NAME) {

                    row = String.format("%-20s %-20s %-30s %-25s %-15s %-15s %-15s", resultSet.getString(Cols.USERNAME).trim(),
                            resultSet.getString(Cols.PASSWORD).trim(), resultSet.getString(Cols.EMAIL).trim(),
                            resultSet.getString(Cols.DATE_CREATED).trim(), resultSet.getString(Cols.AVG_SCORE).trim(),
                            resultSet.getString(Cols.TOTAL_MATCH).trim(), resultSet.getString(Cols.TOTAL_WIN).trim());

                }
                else if (table == TABLE_GAME) {

                    row = String.format("%-20s %-20s %-20s %-30s", resultSet.getString(ColsGame.USER1).trim(),
                            resultSet.getString(ColsGame.USER2).trim(), resultSet.getString(ColsGame.WINNER).trim(),
                            resultSet.getString(ColsGame.GAME_DATE).trim());

                }

                System.out.println(row);

                jtaSQLResult.appendText(row + '\n');
            }
        } catch (SQLException ex) {
            jtaSQLResult.appendText('\n' + ex.toString());
        }
    }

    /**
     * Execute SQL DDL, and modification commands
     */
    private void processSQLNonSelect(String sqlCommand) {
        try {
            // Get a new statement for the current connection
            statement = connection.createStatement();

            // Execute a non-SELECT SQL command
            statement.executeUpdate(sqlCommand);

            Platform.runLater(() -> {
                jtaSQLResult.appendText("\nSQL command executed\n");
            });
            isSignUpSucceeded = true;
        } catch (SQLException ex) {
            
            Platform.runLater(() -> {
                jtaSQLResult.appendText('\n' + ex.toString());
            });
        }
    }



    /**
     * get UserID from database which is matched with the username
     * @param uN username
     * @return UserID
     */
    private UserID getUser(String uN) {

        UserID user = null;
        String username, password, email;
        int avgScore, totalMatch, totalWin;

        String sql = "SELECT * FROM " + TABLE_NAME +
                " where " + Cols.USERNAME + " = \'" + uN + "\';";

        try {
            // Get a new statement for the current connection
            statement = connection.createStatement();

            // Execute a SELECT SQL command
            ResultSet resultSet = statement.executeQuery(sql);
            resultSet.next();

            username = resultSet.getString(Cols.USERNAME).trim();
            password = resultSet.getString(Cols.PASSWORD).trim();
            email = resultSet.getString(Cols.EMAIL).trim();
            avgScore = resultSet.getInt(Cols.AVG_SCORE);
            totalMatch = resultSet.getInt(Cols.TOTAL_MATCH);
            totalWin = resultSet.getInt(Cols.TOTAL_WIN);

            user = new UserID(username, password, avgScore, totalWin, totalMatch, email);

        } catch (Exception ex) {
            jtaSQLResult.appendText('\n' + ex.toString());
        }

        return user;
    }


    /**
     * Get user's information from database which certain the email
     * @param em  email
     * @return arraylist of userID
     */
    private ArrayList<UserID> getUserByEmail (String em) {

        String sql = "SELECT * FROM " + TABLE_NAME +
                " where " + Cols.EMAIL + " = \'" + em + "\';";

        ArrayList<UserID> list = new ArrayList<>();

        try {
            // Get a new statement for the current connection
            statement = connection.createStatement();

            // Execute a SELECT SQL command
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                UserID temp = new UserID(resultSet.getString(Cols.USERNAME), resultSet.getString(Cols.PASSWORD),
                        resultSet.getInt(Cols.AVG_SCORE), resultSet.getInt(Cols.TOTAL_WIN),
                        resultSet.getInt(Cols.TOTAL_MATCH), resultSet.getString(Cols.EMAIL));
                //System.out.println(temp.getUsername());
                list.add(temp);
            }

        } catch (Exception ex) {
            jtaSQLResult.appendText('\n' + ex.toString());
        }

        //return user;
        return list;
    }

    /**
     * Get ranking list from database
     * @return arrayList of UserID which holds the users' information
     */
    private ArrayList<UserID> getRankingList() {
        String sqlCommand = "SELECT * FROM " + TABLE_NAME +
                " ORDER BY " + Cols.AVG_SCORE + " DESC;";

        ArrayList<UserID> list = new ArrayList<>();
        System.out.println("Create list");

        try {
            // Get a new statement for the current connection
            statement = connection.createStatement();

            // Execute a SELECT SQL command
            ResultSet resultSet = statement.executeQuery(sqlCommand);


            while (resultSet.next()) {
                UserID temp = new UserID(resultSet.getString(Cols.USERNAME), "", resultSet.getInt(Cols.AVG_SCORE),
                        resultSet.getInt(Cols.TOTAL_WIN), resultSet.getInt(Cols.TOTAL_MATCH), "");
                //System.out.println(temp.getUsername());
                list.add(temp);
            }
        } catch (SQLException ex) {
            jtaSQLResult.appendText('\n' + ex.toString());
        }

        return list;
    }

    /**
     * Get history list from database for the user
     * @param username
     * @return Arraylist of history which holds the user's history
     */
    private ArrayList<History> getHistoryList(String username) {
        String sqlCommand = "SELECT * FROM " + TABLE_GAME +
                " WHERE " + ColsGame.USER1 + " = \'" + username + "\'" +
                " OR " + ColsGame.USER2 + " = \'" +username + "\'" +
                " ORDER BY " + ColsGame.GAME_DATE + " DESC;";

        ArrayList<History> list = new ArrayList<>();
        System.out.println("Create list");

        try {
            // Get a new statement for the current connection
            statement = connection.createStatement();

            // Execute a SELECT SQL command
            ResultSet resultSet = statement.executeQuery(sqlCommand);


            while (resultSet.next()) {
                String date = resultSet.getString(ColsGame.GAME_DATE);
                date = date.substring(0, date.length() - 2);


                History temp = new History(resultSet.getString(ColsGame.USER1),
                        resultSet.getString(ColsGame.USER2), resultSet.getString(ColsGame.WINNER),
                        date);

                //System.out.println(temp.getUser1());
                list.add(temp);
            }
        } catch (SQLException ex) {
            jtaSQLResult.appendText('\n' + ex.toString());
        }

        return list;
    }


    // ============================== Admin email address dialog ===============================//
    public void adminEmailDialogPopUp() {
        // Create pop up dialog
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox vbox_forgetPassword = new VBox();
        vbox_forgetPassword.setPadding(new Insets(20, 20, 20, 20));
        vbox_forgetPassword.setSpacing(10);

        // Email Info
        Label lbEmail = new Label("Email (Only Gmail):");
        TextField tfEmail = new TextField();

        Label lbEmailPassword = new Label("Email Password");
        PasswordField pfEmailPassword = new PasswordField();

        // Submit button
        Button btSubmit = new Button("Submit");

        // Message
        Label lbMsg = new Label();

        vbox_forgetPassword.getChildren().addAll(
                lbEmail, tfEmail, lbEmailPassword, pfEmailPassword, btSubmit, lbMsg
        );


        btSubmit.setOnAction(event -> {
            boolean isValidInput = true;
            String email = tfEmail.getText();
            String emailPassword = pfEmailPassword.getText();

            lbMsg.setStyle("-fx-text-fill: red;");

            if (!(new UserID(null, null, null).isValidEmail(email))) {
                lbMsg.setText("Invalid Email");
                isValidInput = false;
            }

            Mail mail = new Mail(null, email, emailPassword);
            if (!mail.testEmail()) {
                lbMsg.setText("Invalid Email");
                isValidInput = false;
            }

            if (isValidInput) {
                adminEmail = email;
                adminEmailPassword = emailPassword;
                lbMsg.setStyle("-fx-text-fill: green;");
                lbMsg.setText("Successfully Login");
            }

        });

        Scene dialogScene = new Scene(vbox_forgetPassword, 380, 230);

        dialog.setTitle("Admin email address");
        dialog.setScene(dialogScene);
        dialog.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
