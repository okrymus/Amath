package Project_Login;

import ProjectResources.Main_GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;

import static Project_Login.Login_Constants.CHANGE_PASSWORD;
import static Project_Login.Login_Constants.REQUEST_HISTORY;
import static Project_Login.Login_Constants.REQUEST_RANK;

// DefaultPane
// Programmer: Prakrit Saetang
// Date created: 12/9/16

/**
 * DefaultPane Class - to handle the default pane (After a user log in)
 * @author Prakrit
 */
public class DefaultPane implements Login_Constants {

    // Class instances

    // User info
    private UserID user;
    private ObservableList<UserID> rankingList;

    // Networking
    private DataOutputStream toDatabase;
    private ObjectOutputStream toDatabaseObj;
    private DataInputStream fromDatabase;
    private ObjectInputStream fromDatabaseObj;
    private String ip;
    private int port;

    // UI
    private Stage stage;
    private Scene scene1;

    // Keeping track integers
    int counter;


    /**
     * Constructor
     * @param user
     * @param rankingList
     * @param toDatabase
     * @param toDatabaseObj
     * @param fromDatabase
     * @param fromDatabaseObj
     * @param ip
     * @param port
     * @param stage
     * @param scene1
     */
    public DefaultPane(UserID user, ObservableList<UserID> rankingList,
                       DataOutputStream toDatabase, ObjectOutputStream toDatabaseObj,
                       DataInputStream fromDatabase, ObjectInputStream fromDatabaseObj,
                       String ip, int port, Stage stage, Scene scene1) {
        this.user = user;
        this.rankingList = rankingList;
        this.toDatabase = toDatabase;
        this.toDatabaseObj = toDatabaseObj;
        this.fromDatabase = fromDatabase;
        this.fromDatabaseObj = fromDatabaseObj;
        this.ip = ip;
        this.port = port;
        this.stage = stage;
        this.scene1 = scene1;
    }

    // ============================== Default pane ===============================//
    /**
     * To open up the default pane (After logged in)
     * @return BoarderPane
     */
    public BorderPane getPane() {

        // Set up the pane
        BorderPane mainPane = new BorderPane();
        String image = getClass().getResource("Resource/DefaultPane1.jpg").toExternalForm();
        mainPane.setStyle("-fx-background-image: url('" + image + "'); " +
                "-fx-background-position: center center; " +
                "-fx-background-repeat: stretch;");

        HBox buttonsPane = new HBox();
        buttonsPane.setPadding(new Insets(0,0,5,0));
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.setSpacing(10);

        ImageView imgStart = new ImageView(getClass().getResource("Resource/startButton.png").toExternalForm());
        ImageView imgUserInfo = new ImageView(getClass().getResource("Resource/userInfoButton.png").toExternalForm());
        ImageView imgRank = new ImageView(getClass().getResource("Resource/rankButton.png").toExternalForm());
        ImageView imgHistory = new ImageView(getClass().getResource("Resource/historyButton.png").toExternalForm());
        ImageView imgTutorial = new ImageView(getClass().getResource("Resource/tutorialButton.png").toExternalForm());

        Button imgLogout = new Button();
        imgLogout.getStyleClass().add("logout");
        String url = getClass().getResource("Resource/logoutButton.png").toExternalForm();
        imgLogout.setStyle("-fx-graphic: url(" + url + ");");

        buttonsPane.getChildren().addAll(imgUserInfo, imgRank, imgStart, imgHistory, imgTutorial);

        imgStart.getStyleClass().add("my-button");
        imgUserInfo.getStyleClass().add("my-button");
        imgRank.getStyleClass().add("my-button");
        imgHistory.getStyleClass().add("my-button");
        imgTutorial.getStyleClass().add("my-button");
        imgLogout.getStyleClass().add("my-button");


        // Start the game
        imgStart.setOnMouseClicked(event -> {
            Main_GUI main_gui = new Main_GUI();
            main_gui.display(user, toDatabase, ip, port);
        });

        mainPane.setBottom(buttonsPane);

        VBox mainDisplayPane = new VBox();
        mainDisplayPane.setAlignment(Pos.CENTER);
        mainDisplayPane.setPadding(new Insets(70, 10, 30, 10));

        VBox centerPane = new VBox();
        centerPane.setStyle("-fx-background-color: transparent;");

        // User info
        imgUserInfo.setOnMouseClicked(event -> {
            mainDisplayPane.setPadding(new Insets(70, 10, 30, 10));
            centerPane.getChildren().clear();
            showUserInfo(centerPane);
        });

        // Ranking
        imgRank.setOnMouseClicked(event -> {
            mainDisplayPane.setPadding(new Insets(70, 105, 30, 105));
            centerPane.getChildren().clear();
            showRank(centerPane);
        });

        // History
        imgHistory.setOnMouseClicked(event -> {
            mainDisplayPane.setPadding(new Insets(70, 105, 30, 105));
            centerPane.getChildren().clear();
            showHistory(centerPane);
        });

        // Tutorial
        imgTutorial.setOnMouseClicked(event -> {
            mainDisplayPane.setPadding(new Insets(70, 10, 30, 10));
            centerPane.getChildren().clear();
            showTutorial(centerPane);
        });

        HBox topPane = new HBox();
        topPane.setPadding(new Insets(10,10,0,0));
        topPane.setAlignment(Pos.CENTER_RIGHT);

        imgLogout.setPrefWidth(30);
        imgLogout.setPrefHeight(30);

        // Logout
        imgLogout.setOnMouseClicked(event -> {
            centerPane.getChildren().clear();
            stage.setScene(scene1);
        });

        topPane.getChildren().add(imgLogout);
        mainPane.setTop(topPane);


        mainDisplayPane.getChildren().addAll(centerPane);
        mainPane.setCenter(mainDisplayPane);

        showUserInfo(centerPane);

        return mainPane;
    }

    /**
     * To show the user info page
     * @param pane
     */
    private void showUserInfo(Pane pane) {
        //UserID user = null;
        try {
            toDatabase.writeInt(REQUEST_USER_INFO);
            toDatabase.writeUTF(user.getUsername().trim());
            user = (UserID) fromDatabaseObj.readObject();
        }
        catch (Exception ex) {}
        
        
        // choices pane
        HBox paneChoices = new HBox();
        paneChoices.setSpacing(3);
        paneChoices.setAlignment(Pos.CENTER_RIGHT);
        paneChoices.setPadding(new Insets(5,5,5,5));

        // Change password
        Hyperlink lbChangePassword = new Hyperlink("Change Password");

        lbChangePassword.setOnAction(event -> {
            changePasswordPopUp(user);
        });


        paneChoices.getChildren().addAll(lbChangePassword);

        // info pane
        VBox paneInfo = new VBox();
        paneInfo.setAlignment(Pos.CENTER);
        paneInfo.setPrefWidth(350);
        paneInfo.setSpacing(10);
        paneInfo.setPadding(new Insets(20,20,20,20));

        Label lbUsername = new Label("Username: " + user.getUsername());
        Label lbEmail = new Label("Email: " + user.getEmail());
        Label lbAvgScore = new Label("Average Score: " + user.getAvgScore());
        Label lbTotalMatch = new Label("Total Matches: " + user.getTotalMatch());
        Label lbTotalWin = new Label("Total Wins: " + user.getTotalWin());
        paneInfo.getChildren().addAll(lbUsername, lbEmail, lbAvgScore, lbTotalMatch, lbTotalWin);

        pane.getChildren().addAll(paneChoices, paneInfo);
    }


    /**
     * To show ranking page
     * @param pane
     */
    private void showRank(Pane pane) {

        // List to be added to the table
        ArrayList<UserID> rank = new ArrayList<>();
        try {
            toDatabase.writeInt(REQUEST_RANK);
            rank = (ArrayList<UserID>) fromDatabaseObj.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the table view
        VBox leftPane = new VBox();
        TableView<UserID> table = new TableView<>();


        // Set up the table columns
        TableColumn<UserID, String> colUsername = new TableColumn<>("Username");
        colUsername.setMinWidth(200);
        colUsername.setCellValueFactory(
                new PropertyValueFactory<UserID, String>("username")
        );

        TableColumn<UserID, Integer> colAvgScore = new TableColumn<>("Average score");
        colAvgScore.setMinWidth(130);
        colAvgScore.setCellValueFactory(
                new PropertyValueFactory<UserID, Integer>("avgScore")
        );

        TableColumn<UserID, Integer> colTotalMatch = new TableColumn<>("Total matches");
        colTotalMatch.setMinWidth(130);
        colTotalMatch.setCellValueFactory(
                new PropertyValueFactory<UserID, Integer>("totalMatch")
        );

        TableColumn<UserID, Integer> colTotalWin = new TableColumn<>("Total wins");
        colTotalWin.setMinWidth(130);
        colTotalWin.setCellValueFactory(
                new PropertyValueFactory<UserID, Integer>("totalWin")
        );

        // Set the list to the table
        for (UserID userID: rankingList) {
            System.out.println(userID.getUsername());
        }
        table.setItems(FXCollections.observableArrayList(rank));

        table.getColumns().addAll(colUsername, colAvgScore, colTotalMatch, colTotalWin);
        leftPane.getChildren().add(table);
        pane.getChildren().addAll(leftPane);
    }


    /**
     * To show history page
     * @param pane
     */
    private void showHistory(Pane pane) {

        // List to be added to the table
        ArrayList<History> histories = new ArrayList<>();
        try {
            toDatabase.writeInt(REQUEST_HISTORY);
            toDatabase.writeUTF(user.getUsername());
            histories = (ArrayList<History>) fromDatabaseObj.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Set up the table view
        VBox leftPane = new VBox();
        TableView<History> table = new TableView<>();

        // Set up the table columns
        TableColumn<History, String> colUser1 = new TableColumn<>("User 1");
        colUser1.setMinWidth(130);
        colUser1.setCellValueFactory(
                new PropertyValueFactory<History, String>("user1")
        );

        TableColumn<History, String> colUser2 = new TableColumn<>("User 2");
        colUser2.setMinWidth(130);
        colUser2.setCellValueFactory(
                new PropertyValueFactory<History, String>("user2")
        );

        TableColumn<History, String> colWinner = new TableColumn<>("Winner");
        colWinner.setMinWidth(130);
        colWinner.setCellValueFactory(
                new PropertyValueFactory<History, String>("winner")
        );

        TableColumn<History, String> colGameDate = new TableColumn<>("Date");
        colGameDate.setMinWidth(200);
        colGameDate.setCellValueFactory(
                new PropertyValueFactory<History, String>("game_date")
        );

        // Set the list to the table
        table.setItems(FXCollections.observableArrayList(histories));

        table.getColumns().addAll(colUser1, colUser2, colWinner, colGameDate);
        leftPane.getChildren().add(table);
        pane.getChildren().addAll(leftPane);
    }


    /**
     * To show tutorial page
     * @param pane
     */
    private void showTutorial(Pane pane) {

        // Set up the page
        HBox tutorialPane = new HBox();
        tutorialPane.setAlignment(Pos.CENTER);
        tutorialPane.setSpacing(30);
        counter = 0;
        int size = 14;

        ArrayList<ImageView> imgTutorials = new ArrayList<>();
        for (int i = 0; i < size - 1; i++) {
            ImageView imgTutorial = new ImageView(getClass().
                    getResource("Resource/tutorial" + i + ".jpg").toExternalForm());
            imgTutorial.getStyleClass().addAll("myimage");
            imgTutorial.setFitHeight(280);
            imgTutorial.setFitWidth(500);
            imgTutorials.add(imgTutorial);
            System.out.println("Resource/tutorial" + i + ".jpg");
        }

        ImageView imgTutorial = new ImageView(getClass().
                getResource("Resource/tutorial" + (size-1) + ".png").toExternalForm());
        imgTutorial.getStyleClass().addAll("myimage");
        imgTutorial.setFitHeight(280);
        imgTutorial.setFitWidth(500);
        imgTutorials.add(imgTutorial);


        ImageView btNext = new ImageView(getClass().getResource("Resource/next.png").toExternalForm());
        btNext.getStyleClass().addAll("my-button");
        ImageView btPrevious = new ImageView(getClass().getResource("Resource/previous.png").toExternalForm());
        btPrevious.getStyleClass().addAll("my-button");

        // Next button
        btNext.setOnMouseClicked(event -> {
            counter++;
            if (counter == size - 1) {
                btNext.setDisable(true);
                btNext.setVisible(false);
            }
            if (counter == 1) {
                btPrevious.setDisable(false);
                btPrevious.setVisible(true);
            }
            tutorialPane.getChildren().clear();
            tutorialPane.getChildren().addAll(btPrevious, imgTutorials.get(counter), btNext);
        });

        // Previous button
        btPrevious.setOnMouseClicked(event -> {
            counter--;
            if (counter == size - 2) {
                btNext.setVisible(true);
                btNext.setDisable(false);
            }
            if (counter == 0) {
                btPrevious.setVisible(false);
                btPrevious.setDisable(true);
            }
            tutorialPane.getChildren().clear();
            tutorialPane.getChildren().addAll(btPrevious, imgTutorials.get(counter), btNext);
        });

        btPrevious.setVisible(false);
        btPrevious.setDisable(true);
        tutorialPane.getChildren().addAll(btPrevious, imgTutorials.get(0), btNext);


        pane.getChildren().addAll(tutorialPane);

    }

    // ============================== Change Password dialog ===============================//

    /**
     * To pop up 'change password' dialog
     * @param user
     */
    public void changePasswordPopUp(UserID user) {

        // Set up the dialog views
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox vbox_changePassword = new VBox();
        vbox_changePassword.setPadding(new Insets(20, 20, 20, 20));
        vbox_changePassword.setSpacing(10);

        // Password Info
        Label lbCurrentPassword = new Label("Current Password:");
        Label lbNewPassword = new Label("New Password:");
        Label lbReEnterPassword = new Label("Re-enter Password:");

        PasswordField pfCurrentPassword = new PasswordField();
        PasswordField pfNewPassword = new PasswordField();
        PasswordField pfReEnterPassword = new PasswordField();

        // Change Password button
        Button btn_change_password = new Button("Change Password");

        // Message
        Label lbMsg = new Label();

        vbox_changePassword.getChildren().addAll(
                lbCurrentPassword, pfCurrentPassword,
                lbNewPassword, pfNewPassword,
                lbReEnterPassword, pfReEnterPassword,
                btn_change_password, lbMsg
        );

        // ActionListener
        btn_change_password.setOnAction(event -> {
            boolean isValidInput = true;

            lbMsg.setStyle("-fx-text-fill: red;");

            if (!user.getPassword().equals(pfCurrentPassword.getText())) {
                lbMsg.setText("Incorrect Password");
                isValidInput = false;
            }

            if (!user.isValidPassword(pfNewPassword.getText())) {
                lbMsg.setText("Invalid New Password");
                isValidInput = false;
            }

            if (!pfNewPassword.getText().equals(pfReEnterPassword.getText())) {
                lbMsg.setText("New Password does not match Re-enter Password");
                isValidInput = false;
            }

            if (isValidInput) {
                try {
                    user.setPassword(pfNewPassword.getText());
                    toDatabase.writeInt(CHANGE_PASSWORD);
                    toDatabaseObj.writeObject(user);

                    if (fromDatabase.readBoolean() == true) {
                        lbMsg.setStyle("-fx-text-fill: green;");
                        lbMsg.setText("Password changed successfully");
                    }
                    else {
                        lbMsg.setStyle("-fx-text-fill: red;");
                        lbMsg.setText("Failed to change password");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        });

        Scene dialogScene = new Scene(vbox_changePassword, 380, 300);

        dialog.setTitle("Change Password");
        dialog.setScene(dialogScene);
        dialog.show();

    }
}
