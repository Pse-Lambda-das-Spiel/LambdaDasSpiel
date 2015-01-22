package lambda.model.statistics;


/**
 * Class Represents  everything there is to know about the things a
 * user has done within the game .
 * 
 * @author Farid
 *
 */
public class Statistic {
/**
*the ID of the statistic owner 
*/
private String ownerID ;
/**
* The amount of time a user has played the game.
*/
private long playedTime = 0;

/**
* The number of levels the user has played.
*/
private int played = 0;
/**
* The number of levels the user has solved.
*/
private int solved = 0;
/**
 * The success rate of the player
 */
private float successRate = 0;
/**
* The number of used hints.
*/
private int usedHints = 0;

/**
* The number of gems enchanted  during beta reductions.
*/
private int gemsEnchanted = 0;
/**
* The number of gems placed in the editor mode.
*/
private int gemsPlaced = 0;
/**
* The number of Lambs enchanted during beta reductions.
*/
private int lambsEnchanted = 0;
/**
* The number of lambs placed in the editor mode.
*/
private int lambsPlaced = 0;
/**
* Creates a new default statistic.
*/
public Statistic() {
}
/**
 * @param ownerID
 *  the ID of the statistic owner
 * @param playedtime
 * The amount of time a user has played the game.
 * @param played
 * The number of levels the user has played.
 * @param solved
 * The number of levels the user has solved.
 * @param usedHints
 * The number of used hints.
 * @param gemsEnchanted
 * The number of gems enchanted during beta reductions.
 * @param gemsPlaced
 * The number of lambs placed in the editor mode.
 *  @param lambsEnchanted
 * The number of Lambs enchanted during beta reductions.
 * @param  LambsPlaced 
 * The number of lambs placed in the editor mode.
 */
public Statistic(String ownerID, long playedTime, int played, int solved, int usedHints, int gemsEnchanted,
		int gemsPlaced, int lambsEnchanted, int lambsPlaced) {
this.setOwnerID(ownerID);
this.playedTime = playedTime;
this.usedHints = usedHints;
this.setPlayed(played);
this.setSolved(solved);
this.setGemsEnchanted(gemsEnchanted);
this.setGemsPlaced(gemsPlaced);
this.setLambsEnchanted(lambsEnchanted);
this.setLambsPlaced(lambsPlaced);
}
/**
* Gets the played time.
*
* @return the  played time 
*/
public long getPlayedTime() {
return this.playedTime;
}
/**
 * sets played time
 * @param the played Time
 */
public void setPlayedTime(long playedTime) {
 this.playedTime = playedTime;
}
/**
* Gets the number of hints used.
*
* @return the number of hints used
*/
public int getUsedHints() {
return usedHints;
}
/**
 * sets the number of hints used 
 * @param usedHints
 */
public void setUsedHints(int usedHints){
	this.usedHints = usedHints;
}
/**
 * gets the number of levels played 
 * @return the number of levels played 
 */
public int getPlayed() {
	return played;
}

/**
 * sets the The number of levels played
 * @param played : The number of levels played
 */
public void setPlayed(int played) {
	this.played = played;
}

/**
 * gets the ownerID
 * @return the ID of statistic owner
 */
public String getOwnerID() {
	return ownerID;
}

/**
 * sets the ID of the statistic owner 
 * @param ownerID
 */
public void setOwnerID(String ownerID) {
	this.ownerID = ownerID;
}
/**
 * gets the number of solved levels 
 * @return : the number of solved levels 
 */
public int getSolved() {
	return solved;
}
/**
 * sets the number of solved levels
 * @param solved : the number of solved levels
 */
public void setSolved(int solved) {
	this.solved = solved;
}

/**
 * gets the success rate of the player 
 * @return the success rate of the player 
 */
public float getSuccessRate() {
	return successRate;
}

/**
 * sets the success rate of the player 
 * @param successRate : the success rate of the player 
 */
public void setSuccessRate() {
	try{
		this.successRate = this.solved /this.getPlayed();
	}catch(ArithmeticException e){
		this.successRate = 0;
	}
	
}

/**
 * gets the number of gems placed in the editor mode.
 * @return the number of gems placed in the editor mode.
 */ 
public int getGemsPlaced() {
	return gemsPlaced;
}
/**
 * sets the number of gems placed in the editor mode.
 * @param gemsPlaced : the number of gems placed in the editor mode.
 */
public void setGemsPlaced(int gemsPlaced) {
	this.gemsPlaced = gemsPlaced;
}

/**
 * gets the number of lambs placed in the editor mode.
 * @return : the number of lambs placed in the editor mode.
 */
public int getLambsEnchanted() {
	return lambsEnchanted;
}
/**
 * sets the number of lambs placed in the editor mode.
 * @param lambsEnchanted : the number of lambs placed in the editor mode.
 */
public void setLambsEnchanted(int lambsEnchanted) {
	this.lambsEnchanted = lambsEnchanted;
}
/**
 * gets the number of gems enchanted during beta reductions.
 * @return : the number of gems enchanted during beta reductions.
 */
public int getGemsEnchanted() {
	return gemsEnchanted;
}
/**
 * sets the number of gems enchanted during beta reductions.
 * @param gemsEnchanted : the number of gems enchanted during beta reductions.
 */
public void setGemsEnchanted(int gemsEnchanted) {
	this.gemsEnchanted = gemsEnchanted;
}
/**
 * gets the number of lambs placed in editor mode.
 * @return : the number of lambs placed in editor mode.
 */
public int getLambsPlaced() {
	return lambsPlaced;
}
/**
 * sets the number of lambs placed in editor mode.
 * @param lambsPlaced : the number of lambs placed in editor mode.
 */
public void setLambsPlaced(int lambsPlaced) {
	this.lambsPlaced = lambsPlaced;
}


}


