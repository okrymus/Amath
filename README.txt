
===========[ DIRECTION ]=============
1. Run Login_Server.java in Project_Login package
  1.1 Connect to the database using username & password registered with mySQL server
  1.2 [Optional] register the admin email address using only Gmail that has been set up for smtp (the admin email address is used in the event of a user forgetting password).
2. Run ProjectServer.java in Project_Server package
3. Run UserIDClient.java in Project_Login package for one user
  3.1 If a client is running on a different computer, click ‘Change Server IP address’ to change the server ip address

===========[ HOW TO PLAY ]=============
1. Form a valid equation on the board and try to get the highest score
	(total score is based on the used tiles’ scores + extra score from the board)
	- If it is not the first turn, at least one of your tiles must be placed next to your opponent’s
	- In one turn, you are allowed to place your tiles in one column/row
2. Click Submit button to submit your equation and pass the turn to an opponent
3. Click Change Tiles to change the tiles on your hand (up to 8 tiles)
	(after you change your tiles the turn will be passed to an opponent)
4. Click pass to skip your turn
5. The game will be over when
	- No more tiles left in the bag
	- One player gets to 150 scores
6. You may talk to your opponent through the chat room

NOTE: CLICK ‘TUTORIAL’ Button inside the game window for more information


===========[ DIRECTION FOR GRADING ]=============
1. JavaDoc - Go to ‘dist’ -> ‘javadoc’ -> ‘index.html’
2. JUnit test - Go to ‘test’ -> ‘ProjectResources’ -> ‘ResultFinderTest.java’
3. Pseudocode - Go to ‘Pseudocode’ folder
(Go to http://prezi.com/7y53uqhk82v5/?utm_campaign=share&utm_medium=copy&rc=ex0share for the project diagram)
4. Database with 2 tables - 
	Database name: easygroup285 
	Table names: account, games
	More detail in ‘Login_Constants.java’ in Project_Login package


