package Project_Login;

import java.io.Serializable;

// History
// Programmer: Prakrit Saetang
// Date created: 12/10/16


/**
 * History class - to hold history instances
 * @author Prakrit
 */
public class History implements Serializable {

    // Class instances
    private String user1;
    private String user2;
    private String winner;
    private String game_date;

    /**
     * Constructor
     * @param user1
     * @param user2
     * @param winner
     * @param game_date
     */
    public History(String user1, String user2, String winner, String game_date) {
        this.user1 = user1;
        this.user2 = user2;
        this.winner = winner;
        this.game_date = game_date;
    }

    //Getters

    /**
     * Get user1
     * @return String - user1 username
     */
    public String getUser1() {
        return user1;
    }

    /**
     * Get user2
     * @return String - user2 username
     */
    public String getUser2() {
        return user2;
    }

    /**
     * Get winner
     * @return String - winner username
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Get the date of the game
     * @return String - the date of the game (MM/DD/YY HH:MM:SS)
     */
    public String getGame_date() {
        return game_date;
    }



    // Setters

    /**
     * Set user1
     * @param user1
     */
    public void setUser1(String user1) {
        this.user1 = user1;
    }

    /**
     * Set user2
     * @param user2
     */
    public void setUser2(String user2) {
        this.user2 = user2;
    }

    /**
     * Set winner
     * @param winner
     */
    public void setWinner(String winner) {
        this.winner = winner;
    }

    /**
     * Set the date of the game
     * @param game_date
     */
    public void setGame_date(String game_date) {
        this.game_date = game_date;
    }
}
