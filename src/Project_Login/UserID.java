package Project_Login;

import java.io.Serializable;
import java.util.regex.*;

// Programmer: Panupong Leenawarat/Seun Eguntola/Emerson Moniz
// Date created: 12/16/2016
// Date modified: 10/16/2016 Add validation for email, username, password
// Date modified: 10/20/2016 Add Serializable to handle with I/O as an object for server

/**
 * UserID Class - to hold all instances and methods for a user
 * @author PANUPONG/ SEUN/ EMERSON
 */
public class UserID implements Serializable {

    String username;
    private String password;
    private int avgScore;
    private int totalWin;
    private int totalMatch;
    private String email;
    //private int function = 0; // is 0 for log in

    /**
     * Constructor
     * @param username
     * @param password
     * @param avgScore
     * @param totalWin
     * @param totalMatch
     * @param email
     */
    public UserID(String username, String password, int avgScore, int totalWin, int totalMatch, String email) {
        this.username = username;
        this.password = password;
        this.avgScore = avgScore;
        this.totalWin = totalWin;
        this.totalMatch = totalMatch;
        this.email = email;
    }

    /**
     * Constructor
     * @param mail
     * @param uN
     * @param pW
     */
    UserID(String mail, String uN, String pW) {
        email = mail;
        username = uN;
        password = pW;
    }

    /**
     * To get username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * To get password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * To get Average Score
     * @return
     */
    public int getAvgScore() {
        return avgScore;
    }

    /**
     * To get Total amount of win games
     * @return
     */
    public int getTotalWin() {
        return totalWin;
    }

    /**
     * To get Total amount of games
     * @return
     */
    public int getTotalMatch() {
        return totalMatch;
    }

    /**
     * To get email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * To set username
     * @param uN username
     */
    public void setUsername(String uN) {
        username = uN;
    }

    /**
     * To set Password
     * @param pW password
     */
    public void setPassword(String pW) {
        password = pW;
    }

    /**
     * To set average score
     * @param avgScore average score
     */
    public void setAvgScore(int avgScore) {
        this.avgScore = avgScore;
    }

    /**
     * To set total amount of win games
     * @param totalWin total amount of win games
     */
    public void setTotalWin(int totalWin) {
        this.totalWin = totalWin;
    }

    /**
     * To set total amount of games
     * @param totalMatch total amount of games
     */
    public void setTotalMatch(int totalMatch) {
        this.totalMatch = totalMatch;
    }

    /**
     * To set email of user
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * To validate the username given by user
     * @param uN
     * @return
     */
    boolean isValidUserName(String uN) {
        String patternString = "[a-zA-Z_0-9]{8,20}";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(uN);
        boolean b = m.matches();

        return b;
    }

    /**
     * To validate the username for first time
     * @return
     */
    boolean isValidUserName() {
        //String patternString = "^[^\\W+\\w]{6,10}$";
        String patternString = "[a-zA-Z_0-9]{8,20}";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(this.username);
        boolean b = m.matches();

        System.out.println(b);
        return b;
    }

    /**
     * To display error message for wrong username/password/email
     * @return
     */
    String errorMessage() {
        String str = "";
        if (isValidEmail() == false) {
            str += "Invalid Email\n";
        }
        if (isValidUserName() == false) {
            str += "Username is Invalid\n";
        } if (isValidPassword() == false) {
            str += "Password is Invalid\n";
        }
        return str;
    }

    /**
     * To validate if the email given by the user is valid
     * @param mail
     * @return boolean - true if the email given by the user is valid, false otherwise
     */
    boolean isValidEmail(String mail) {
        String patternString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(mail);
        boolean b = m.matches();

        return b;
    }

    /**
     * To validate the email for the first time
     * @return
     */
    boolean isValidEmail() {
        String patternString = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(this.email);
        boolean b = m.matches();

        return b;
    }

    /**
     * To validate if the password given by the user is valid
     * @param pW
     * @return
     */
    boolean isValidPassword(String pW) {
        String patternString = "[a-zA-Z_0-9]{8,20}";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(pW);
        boolean b = m.matches();

        return b;
    }

    /**
     * To validate the password for the first time
     * @return
     */
      boolean isValidPassword() {
        String patternString = "[a-zA-Z_0-9]{8,20}";
        Pattern p = Pattern.compile(patternString);
        Matcher m = p.matcher(this.password);
        boolean b = m.matches();
          System.out.println(b);
        return b;
    }
}
