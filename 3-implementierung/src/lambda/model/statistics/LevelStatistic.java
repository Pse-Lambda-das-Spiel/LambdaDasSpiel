package lambda.model.statistics;

import java.util.Observable;
import java.util.Observer;
 

/**
 * Represents - in the database -to every unlocked level everything there is to know about the things a
 * user has done within the game .
 * 
 * @author Farid
 */
public class LevelStatistic implements Observer  {
	private StatisticModel statistic;
	private int lambsEnchanted;
	private int gemsEnchanted;
	private int gemsPlaced;
	private int lambsPlaced;
	private int lambsEnchantedPerLevel;
	private int gemsEnchantedPerLevel;
	private int gemsPlacedPerLevel;
	private int lambsPlacedPerLevel;
	private int levelCompleted;
	private int hintsUsed;
	private int levelTries;
	private int successfulLevelTries;
	private long beginTime ;
	private long timePlayed;
	/**
	 * initialise a level Statistic 
	 * @param statistic
	 */
	LevelStatistic(StatisticModel statistic){
		this.statistic = statistic ;
		this.lambsEnchanted = 0;
		this.gemsEnchanted = 0;
		this.gemsPlaced = 0;
		this.lambsPlaced= 0;
		this.lambsEnchantedPerLevel= 0;
		this.gemsEnchantedPerLevel= 0;
		this.gemsPlacedPerLevel = 0;
		this.lambsPlacedPerLevel = 0;
		this.levelCompleted = 0;
		this.setHintsUsed(0);
		this.levelTries = 0;
		this.successfulLevelTries = 0;
		this.beginTime = (new java.util.Date()).getTime();
		
	}
	
	/**
	 * 
	 * @return : the time the player begins to play the game 
	 */
		public long getBeginTime() {
			return beginTime;
		}
	/**
	 * 
	 * @param beginTime : the time the player begins to play the game 
	 */
		public void setBeginTime(long beginTime) {
			this.beginTime = beginTime;
		}
	/**
	 * update the played time	
	 */
	public void updateTime(){
		long actuelTime = (new java.util.Date()).getTime();
		this.setTimePlayed(actuelTime - this.getBeginTime());
	}
	/**
	 * update the statistics 
	 */
	public void update(){
		this.updateTime();
		}
	public long getTimePlayed() {
		return timePlayed;
	}
	public void setTimePlayed(long timePlayed) {
		this.timePlayed = timePlayed;
	}
	public int getSuccessfulLevelTries() {
		return successfulLevelTries;
	}
	public void setSuccessfulLevelTries(int successfulLevelTries) {
		this.successfulLevelTries = successfulLevelTries;
	}
	public int getLevelTries() {
		return levelTries;
	}
	public void setLevelTries(int levelTries) {
		this.levelTries = levelTries;
	}
	
	public int getLevelCompleted() {
		return levelCompleted;
	}
	public void setLevelCompleted(int levelCompleted) {
		this.levelCompleted = levelCompleted;
	}
	public int getLambsPlacedPerLevel() {
		return lambsPlacedPerLevel;
	}
	public void setLambsPlacedPerLevel(int lambsPlacedPerLevel) {
		this.lambsPlacedPerLevel = lambsPlacedPerLevel;
	}
	public int getGemsPlacedPerLevel() {
		return gemsPlacedPerLevel;
	}
	public void setGemsPlacedPerLevel(int gemsPlacedPerLevel) {
		this.gemsPlacedPerLevel = gemsPlacedPerLevel;
	}
	public int getGemsEnchantedPerLevel() {
		return gemsEnchantedPerLevel;
	}
	public void setGemsEnchantedPerLevel(int gemsEnchantedPerLevel) {
		this.gemsEnchantedPerLevel = gemsEnchantedPerLevel;
	}
	public int getLambsEnchantedPerLevel() {
		return lambsEnchantedPerLevel;
	}
	public void setLambsEnchantedPerLevel(int lambsEnchantedPerLevel) {
		this.lambsEnchantedPerLevel = lambsEnchantedPerLevel;
	}
	public int getLambsPlaced() {
		return lambsPlaced;
	}
	public void setLambsPlaced(int lambsPlaced) {
		this.lambsPlaced = lambsPlaced;
	}
	public int getGemsPlaced() {
		return gemsPlaced;
	}
	public void setGemsPlaced(int gemsPlaced) {
		this.gemsPlaced = gemsPlaced;
	}
	public int getGemsEnchanted() {
		return gemsEnchanted;
	}
	public void setGemsEnchanted(int gemsEnchanted) {
		this.gemsEnchanted = gemsEnchanted;
	}
	public int getLambsEnchanted() {
		return lambsEnchanted;
	}
	public void setLambsEnchanted(int lambsEnchanted) {
		this.lambsEnchanted = lambsEnchanted;
	}

	public int getHintsUsed() {
		return hintsUsed;
	}

	public void setHintsUsed(int hintsUsed) {
		this.hintsUsed = hintsUsed;
	}

	@Override
	public void update(Observable ReductionModel , Object arg) {
		
		
	}
	
}
