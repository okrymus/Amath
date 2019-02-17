package Project_Login;

// Login_Constants
// Programmer: Prakrit Saetang
// Date created: 12/2/16
// Date Modified: 12/9/16 (Add REQUEST_HISTORY, REQUEST RANK, TABLE_GAME Constants)

/**
 * Interface containing all constants needed to handle user database
 * @author Prakrit
 */
public interface Login_Constants {

    // ======================= [ My SQL SETUP ] ==============================
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost/";

    // ======================= [ TABLES INFO ] ==============================
    public static final String TABLE_NAME = "account";
    public static final String TABLE_GAME = "games";
    public static final String DATABASE_NAME = "easygroup285";
    class Cols {
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String EMAIL = "email";
        public static final String DATE_CREATED = "date_created";
        public static final String AVG_SCORE = "avg_score";
        public static final String TOTAL_MATCH = "total_match";
        public static final String TOTAL_WIN = "total_win";
    }
    class ColsGame {
        public static final String USER1 = "user1";
        public static final String USER2 = "user2";
        public static final String WINNER = "winner";
        public static final String GAME_DATE = "game_date";
    }

    // ======================= [ INDICATOR ] ==============================
    public final int LOGIN = 0, SIGNUP = 1, CHANGE_PASSWORD = 2,
            FORGET_PASSWORD = 3, REQUEST_HISTORY = 4, REQUEST_RANK = 5,
            REQUEST_USER_INFO = 6;
    
    // ======================= [ NETWORKING ] ==============================
    public final static int PORT_LOGIN = 8000;
    public final static int PORT_GAME = 2705;
    
}
