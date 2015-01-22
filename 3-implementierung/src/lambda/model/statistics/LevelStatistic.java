package lambda.model.statistics;

/**
 * Represents - in the database -to every unlocked level everything there is to know about the things a
 * user has done within the game .
 * 
 * @author Farid
 */
public class LevelStatistic extends Statistic{
/**
 * the ID of the statistic level	
 */
private String levelID;
/**
 * Creates a new default level statistic.
 */
public LevelStatistic (){
	super();
}
/**
 * 
 * @param ownerID
 *  the ID of the statistic owner
 * @param playedtime
 * The amount of time a user has played the level.
 * @param played
 * The number of times the user has played the level.
 * @param usedHints
 * The number of times the user has used the hint .
 * @param solved
 * The number of times the user has solved the level.
 * @param gemsEnchanted
 * The number of gems enchanted during beta reductions.
 * @param gemsPlaced
 * The number of lambs placed in the editor mode.
 *  @param lambsEnchanted
 * The number of Lambs enchanted during beta reductions.
 * @param  LambsPlaced 
 * The number of lambs placed in the editor mode.
 * @param levelID
 */
public LevelStatistic (String ownerID, long playedTime, int played, int solved, int usedHints, int gemsEnchanted,
		int gemsPlaced, int lambsEnchanted, int lambsPlaced,String levelID){
	super(ownerID,playedTime, played, solved,usedHints, gemsEnchanted,
		gemsPlaced, lambsEnchanted, lambsPlaced);
	this.setLevelID(levelID);
}

/**
 * gets the Level ID
 * @return the Level ID
 */
public String getLevelID() {
	return levelID;
}
/**
 * sets the Level ID
 * @param levelID : the Level ID
 */
public void setLevelID(String levelID) {
	this.levelID = levelID;
}

}
