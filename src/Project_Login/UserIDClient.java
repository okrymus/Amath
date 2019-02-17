package Project_Login;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;


// UserIDClient
// Programmer: Panupong Leenawarat
// Date created: 12/16/2016
// Date modified: 11/10/2016 Create template for login client
// Date modified: 10/11/2016 Add connection with sever
// Date modified: 10/12/2016 Add Switch case to send spacific function to server
// Date modified: 10/15/2016 Add pop up display for sign up
// Date modified: 10/17/2016 Add Validation for the user input

/**
 * UserIDClient class - main class for client after logging in
 * @author PANUPONG
 */
public class UserIDClient extends Application implements Login_Constants {


    private Socket socket;
    private String host = "localhost";

    // create IO streams
    DataOutputStream toServer = null;
    DataInputStream fromServer = null;
    ObjectOutputStream toServerObj = null;
    ObjectInputStream fromServerObj = null;

    // JavaFX
    Stage stage;
    private Scene scene1, scene2;

    // nodes
    private Label lbl_newEmail = new Label("Your email (e.g. admin@test.com):");
    private TextField tf_newEmail = new TextField();
    
    private Label lbl_newUsername = new Label("New Username\n(8 - 20 characters, Only Letters/Numbers):");
    private TextField tf_newUsername = new TextField();
    
    private Label lbl_newPassword = new Label("New Password\n(8 - 20 characters, Only Letters/Numbers):");
    private PasswordField pf_newPassword = new PasswordField();
    
    private Label lbl_reEnterPassword = new Label("Re-Enter Password:");
    private PasswordField pf_reEnterPassword = new PasswordField();
    
    private Label lbl_username = new Label("Username");
    private TextField tf_username = new TextField();
    
    private Label lbl_password = new Label("Password");
    private PasswordField pf_password = new PasswordField();

    private Label lbStatus = new Label("");
    
    private Label lbwarnningMassage = new Label("");
    
    private Button btn_signup = new Button("Sign up");

    /**
     * To open up the window
     * @param primaryStage
     * @throws IOException
     */
    public void start(Stage primaryStage) throws IOException {

        // ============================== Change ip address pane ===============================//
        HBox paneChangeIp = new HBox();
        paneChangeIp.setAlignment(Pos.CENTER_RIGHT);
        Hyperlink hlChangeIp = new Hyperlink("Change Server IP Address");
        hlChangeIp.setStyle("-fx-font-size: 12; -fx-text-fill: white;");
        paneChangeIp.getChildren().addAll(hlChangeIp);

        hlChangeIp.setOnAction(event -> {
            changeServerIpPopUp();
        });


        // ============================== Logo pane ===============================//
        VBox paneTop = new VBox();
        paneTop.setAlignment(Pos.CENTER);
        paneTop.setPadding(new Insets(50,70,15,70));
        paneTop.getChildren().add(new ImageView(new Image(getClass().getResourceAsStream("Resource/logo.png"))));

        // ============================== Login Info pane ===============================//
        VBox vbox_login = new VBox();
        vbox_login.setSpacing(5);
        vbox_login.setPadding(new Insets(0,50,15,50));
        vbox_login.getChildren().addAll(lbl_username, tf_username, lbl_password, pf_password);

        tf_username.setOnAction(event -> {
            processLogin();
        });

        pf_password.setOnAction(event -> {
            processLogin();
        });

        // ============================== Login button pane ===============================//
        HBox bthPane = new HBox();
        // LOG IN BUTTON
        Button btn_log = new Button("Log in");
        btn_log.setPrefWidth(300);
        btn_log.setOnAction(e -> {
            processLogin();
        });
        
        bthPane.getChildren().addAll(btn_log);
        bthPane.setSpacing(10);
        bthPane.setPadding(new Insets(0,50,0,50));
        bthPane.setAlignment(Pos.CENTER);

        // ============================== Forget Password pane ===============================//
        HBox forgetPasswordPane = new HBox();
        forgetPasswordPane.setAlignment(Pos.CENTER_RIGHT);
        forgetPasswordPane.setPadding(new Insets(0,50,15,30));
        Hyperlink hlForgetPassword = new Hyperlink("Forget Password?");
        hlForgetPassword.setStyle("-fx-font-size: 12;");

        hlForgetPassword.setOnAction(event -> {
            forgetPasswordPopUp();
        });

        forgetPasswordPane.getChildren().addAll(hlForgetPassword);

        // ============================== Status pane ===============================//
        VBox paneStatus = new VBox();
        paneStatus.setAlignment(Pos.CENTER);
        paneStatus.setPadding(new Insets(0,50,30,50));
        lbStatus.setId("status");
        paneStatus.getChildren().add(lbStatus);

        // ============================== Sign up pane ===============================//
        VBox paneBottom = new VBox();
        paneBottom.setAlignment(Pos.CENTER);
        paneBottom.setSpacing(10);
        Label lbSignUpAsking = new Label("Don't have an account? Join us free!!");

        // Sign Up BUTTON
        btn_signup.setPrefWidth(300);
        btn_signup.setOnAction(e -> {
            signUpPopUp();
        });

        Label lbCredit = new Label("Developed by EASY GROUP");
        lbCredit.setId("credit");
        paneBottom.getChildren().addAll(lbSignUpAsking, btn_signup, lbCredit);

        VBox paneMain = new VBox();
        paneMain.setPadding(new Insets(10, 10, 20, 10));
        paneMain.setSpacing(5);
        paneMain.getChildren().addAll(paneChangeIp, paneTop, vbox_login, bthPane, forgetPasswordPane, paneStatus, paneBottom);
        
        scene1 = new Scene(paneMain);
        scene1.getStylesheets().add(getClass().getResource("Resource/StyleSheetLogin.css").toString());
        stage = primaryStage;
        stage.setResizable(false);
        stage.setTitle("AMath EASY Group");
        stage.setScene(scene1);
        stage.getIcons().addAll(new Image(getClass().getResourceAsStream("Resource/icon.png")));
        stage.show();


        // ============================== Connection ===============================//
        try {
            // Create a socket to connect to the server
            socket = new Socket(host, PORT_LOGIN);

            // Create an input stream to receive data from the server
            fromServer = new DataInputStream(socket.getInputStream());

            // Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());

            toServerObj = new ObjectOutputStream(socket.getOutputStream());
            fromServerObj = new ObjectInputStream(socket.getInputStream());

        }
        catch (IOException ex) {
            System.err.println(ex);
        }

    }


    // ============================== Sign up dialog ===============================//
    /**
     * To pop up the sign up dialog
     */
    public void signUpPopUp() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        VBox vbox_signup = new VBox();
        vbox_signup.setPadding(new Insets(20, 20, 20, 20));
        vbox_signup.setSpacing(10);

        // Sign up button
        Button btn_signUp = new Button("SIGN UP");
        
        vbox_signup.getChildren().addAll(
                lbl_newEmail, tf_newEmail,
                lbl_newUsername, tf_newUsername,
                lbl_newPassword, pf_newPassword,
                lbl_reEnterPassword, pf_reEnterPassword,
                btn_signUp, lbwarnningMassage
        );

        lbwarnningMassage.setStyle("-fx-text-fill: red");
        btn_signUp.setOnAction(new SignupListener());

        Scene dialogScene = new Scene(vbox_signup, 350, 445);
        
        dialog.setTitle("SIGN UP");
        dialog.setScene(dialogScene);
        dialog.show();
        
    }

    /**
     * Handle sign up button action
     */
    private class SignupListener implements EventHandler<ActionEvent> {
        
        @Override
        public void handle(ActionEvent e) {
            try {
                lbwarnningMassage.setText("");
                
                if (!tf_newEmail.getText().trim().isEmpty() && !tf_newUsername.getText().trim().isEmpty()
                        && !pf_newPassword.getText().trim().isEmpty() && !pf_reEnterPassword.getText().trim().isEmpty() &&
                        pf_newPassword.getText().trim().equals(pf_reEnterPassword.getText().trim())) {

                    // Get user info
                    String email = tf_newEmail.getText().trim();
                    String username = tf_newUsername.getText().trim();
                    String password = pf_newPassword.getText().trim();

                    // Create a Student object and send to the server
                    UserID s = new UserID(email, username, password);

                    // validate information
                    // if valid, send to the database server
                    if (s.isValidEmail() && s.isValidPassword() && s.isValidUserName()) {
                        toServer.writeInt(SIGNUP);
                        toServerObj.writeObject(s);
                        lbwarnningMassage.setText("");
                        if (fromServer.readBoolean() == true) {
                            lbwarnningMassage.setTextFill(Color.GREEN);
                            lbwarnningMassage.setText("Signed up successfully");
                        }
                        else {
                            lbwarnningMassage.setText("Failed to Sign up");
                        }
                    } else {
                        lbwarnningMassage.setText(s.errorMessage());
                    }
                }
                else {
                    String warningMsg = "";
                    // Display empty email warning
                    if (tf_newEmail.getText().trim().isEmpty()) {
                        warningMsg += "Please fill email\n";
                    }
                    // Display empty username warning
                    if (tf_newUsername.getText().trim().isEmpty()) {
                        warningMsg += "Please fill username\n";
                    }
                    if (pf_newPassword.getText().trim().isEmpty()) {
                        warningMsg += "Please fill password\n";
                    }
                    if (pf_reEnterPassword.getText().trim().isEmpty()) {
                        warningMsg += "Please fill re-password\n";
                    }
                    if (!pf_newPassword.getText().trim().equals(pf_reEnterPassword.getText().trim())) {
                        warningMsg += "Password is not match\n";
                    }

                    lbwarnningMassage.setText(warningMsg);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    // ============================== Forget Password dialog ===============================//
    public void forgetPasswordPopUp() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox vbox_forgetPassword = new VBox();
        vbox_forgetPassword.setPadding(new Insets(20, 20, 20, 20));
        vbox_forgetPassword.setSpacing(10);

        // Password Info
        Label lbEmail = new Label("Email:");

        TextField tfEmail = new TextField();

        // Change Password button
        Button btSubmit = new Button("Submit");

        // Message
        Label lbMsg = new Label();

        vbox_forgetPassword.getChildren().addAll(
                lbEmail, tfEmail, btSubmit, lbMsg
        );


        btSubmit.setOnAction(event -> {
            boolean isValidInput = true;
            String email = tfEmail.getText();

            lbMsg.setStyle("-fx-text-fill: red;");

            if (!(new UserID(null, null, null).isValidEmail(email))) {
                lbMsg.setText("Invalid Email");
                isValidInput = false;
            }

            if (isValidInput) {
                try {
                    toServer.writeInt(FORGET_PASSWORD);
                    toServer.writeUTF(email);

                    if (fromServer.readBoolean() == true) {
                        lbMsg.setStyle("-fx-text-fill: green;");
                        lbMsg.setText("Your password has been sent to your email");
                    }
                    else {
                        lbMsg.setStyle("-fx-text-fill: red;");
                        lbMsg.setText("Email not found");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        Scene dialogScene = new Scene(vbox_forgetPassword, 380, 160);

        dialog.setTitle("Forget Password");
        dialog.setScene(dialogScene);
        dialog.show();

    }

    // ============================== Change Server IP dialog ===============================//
    public void changeServerIpPopUp() {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);

        VBox vbox_forgetPassword = new VBox();
        vbox_forgetPassword.setPadding(new Insets(20, 20, 20, 20));
        vbox_forgetPassword.setSpacing(10);

        // Server ip
        Label lbServerIp = new Label("Server IP Address:");

        TextField tfServerIp = new TextField();
        tfServerIp.setText(host);

        // Change Password button
        Button btSubmit = new Button("Submit");

        // Message
        Label lbMsg = new Label();

        vbox_forgetPassword.getChildren().addAll(
                lbServerIp, tfServerIp, btSubmit, lbMsg
        );


        btSubmit.setOnAction(event -> {
            boolean isValidInput = true;
            String ip = tfServerIp.getText();

            lbMsg.setStyle("-fx-text-fill: red;");

            if (!isValidIp(ip) && !ip.equals(host)) {
                lbMsg.setText("Invalid IP Address");
                isValidInput = false;
            }

            if (isValidInput) {
                host = ip;
                try {
                    // Create a socket to connect to the server
                    socket = new Socket(host, PORT_LOGIN);

                    // Create an input stream to receive data from the server
                    fromServer = new DataInputStream(socket.getInputStream());

                    // Create an output stream to send data to the server
                    toServer = new DataOutputStream(socket.getOutputStream());

                    toServerObj = new ObjectOutputStream(socket.getOutputStream());
                    fromServerObj = new ObjectInputStream(socket.getInputStream());

                    lbMsg.setStyle("-fx-text-fill: green;");
                    lbMsg.setText("Server IP Address Changed");
                }
                catch (IOException ex) {
                    lbMsg.setStyle("-fx-text-fill: red;");
                    lbMsg.setText("Invalid IP Address");
                    System.err.println(ex);
                }

            }
            else {
                lbMsg.setStyle("-fx-text-fill: red;");
                lbMsg.setText("Invalid IP Address");
            }

        });

        Scene dialogScene = new Scene(vbox_forgetPassword, 380, 160);

        dialog.setTitle("Change Server IP Address");
        dialog.setScene(dialogScene);
        dialog.show();

    }

    private boolean isValidIp(String ip) {
        Pattern pattern = Pattern.compile(
                "([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})\\.([0-9]{1,3})");

        return pattern.matcher(ip).matches();
    }

    private void processLogin() {
        // get username & password
        String uS = tf_username.getText().trim();
        String pW = pf_password.getText().trim();

        // check with the database
        try {
            toServer.writeInt(LOGIN);
            toServer.writeUTF(uS);
            toServer.writeUTF(pW);

            // Valid username & password
            if (fromServer.readBoolean() == true) {
                System.out.println("Logged in");
                UserID user = (UserID) fromServerObj.readObject();
                ArrayList<UserID> rankingList = (ArrayList<UserID>) fromServerObj.readObject();
                System.out.println(rankingList);
                DefaultPane defaultPane = new DefaultPane(user, FXCollections.observableList(rankingList),
                        toServer, toServerObj, fromServer, fromServerObj, host, PORT_GAME, stage, scene1);
                scene2 = new Scene(defaultPane.getPane(), 800, 550);

                scene2.getStylesheets().add(getClass().getResource("Resource/StyleSheetDefault.css").toString());
                stage.setScene(scene2);
            }
            // Invalid input
            else {
                System.out.println("Failed");
                lbStatus.setStyle("-fx-text-fill: red");
                lbStatus.setText("Incorrect Username or Password");
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Failed");
            lbStatus.setStyle("-fx-text-fill: red");
            lbStatus.setText("Unable to connect to the database server");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
