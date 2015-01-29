package lambda.model.statistics;

import lambda.Observable;
/**
 * 
 * @author Farid el-haddad
 *
 */
public class StatisticModel extends Observable<StatisticModelObserver> {

	private int lambsEnchanted;
	private int gemsEnchanted;
	private int gemsPlaced;
	private int lambsPlaced;
	private int lambsEnchantedPerLevel;
	private int gemsEnchantedPerLevel;
	private int gemsPlacedPerLevel;
	private int lambsPlacedPerLevel;
	private int levelCompleted;
	private int hintsNotUsed;
	private int levelTries;
	private int successfulLevelTries;
	private long beginTime ;
	private long timePlayed;
	
	public StatisticModel() {
	}
	
	public int getLambsEnchanted() {
		return lambsEnchanted;
	}

	public int getGemsEnchanted() {
		return gemsEnchanted;
	}

	public int getGemsPlaced() {
		return gemsPlaced;
	}

	public int getLambsPlaced() {
		return lambsPlaced;
	}

	public long getTimePlayed() {
		return timePlayed;
	}

	public int getHintsNotUsed() {
		return hintsNotUsed;
	}

	public int getLevelCompleted() {
		return levelCompleted;
	}

	public int getGemsEnchantedPerLevel() {
		return gemsEnchantedPerLevel;
	}

	public int getLambsEnchantedPerLevel() {
		return lambsEnchantedPerLevel;
	}

	public int getGemsPlacedPerLevel() {
		return gemsPlacedPerLevel;
	}

	public int getLambsPlacedPerLevel() {
		return lambsPlacedPerLevel;
	}

	/**
	 * @param lambsEnchanted the lambsEnchanted to set
	 */
	public void setLambsEnchanted(int lambsEnchanted) {
		this.lambsEnchanted = lambsEnchanted;
		notify(observer -> observer.changedLambsEnchanted());
	}

	/**
	 * @param gemsEnchanted the gemsEnchanted to set
	 */
	public void setGemsEnchanted(int gemsEnchanted) {
		this.gemsEnchanted = gemsEnchanted;
		notify(observer -> observer.changedGemsEnchanted());
	}

	/**
	 * @param gemsPlaced the gemsPlaced to set
	 */
	public void setGemsPlaced(int gemsPlaced) {
		this.gemsPlaced = gemsPlaced;
		notify(observer -> observer.changedGemsPlaced());
	}

	/**
	 * @param lambsPlaced the lambsPlaced to set
	 */
	public void setLambsPlaced(int lambsPlaced) {
		this.lambsPlaced = lambsPlaced;
		notify(observer -> observer.changedLambsPlaced());
	}

	/**
	 * @param lambsEnchantedPerLevel the lambsEnchantedPerLevel to set
	 */
	public void setLambsEnchantedPerLevel(int lambsEnchantedPerLevel) {
		this.lambsEnchantedPerLevel = lambsEnchantedPerLevel;
		notify(observer -> observer.changedLambsEnchantedPerLevel());
	}

	/**
	 * @param gemsEnchantedPerLevel the gemsEnchantedPerLevel to set
	 */
	public void setGemsEnchantedPerLevel(int gemsEnchantedPerLevel) {
		this.gemsEnchantedPerLevel = gemsEnchantedPerLevel;
		notify(observer -> observer.changedGemsEnchantedPerLevel());
	}

	/**
	 * @param gemsPlacedPerLevel the gemsPlacedPerLevel to set
	 */
	public void setGemsPlacedPerLevel(int gemsPlacedPerLevel) {
		this.gemsPlacedPerLevel = gemsPlacedPerLevel;
		notify(observer -> observer.changedGemsPlacedPerLevel());
	}

	/**
	 * @param lambsPlacedPerLevel the lambsPlacedPerLevel to set
	 */
	public void setLambsPlacedPerLevel(int lambsPlacedPerLevel) {
		this.lambsPlacedPerLevel = lambsPlacedPerLevel;
		notify(observer -> observer.changedLambsPlacedPerLevel());
	}

	/**
	 * @param levelCompleted the levelCompleted to set
	 */
	public void setLevelCompleted(int levelCompleted) {
		this.levelCompleted = levelCompleted;
		notify(observer -> observer.changedLevelCompleted());
	}

	/**
	 * @param hintsNotUsed the hintsNotUsed to set
	 */
	public void setHintsNotUsed(int hintsNotUsed) {
		this.hintsNotUsed = hintsNotUsed;
		notify(observer -> observer.changedHintsNotUsed());
	}

	/**
	 * @param timePlayed the timePlayed to set
	 */
	public void setTimePlayed(long timePlayed) {
		this.timePlayed = timePlayed;
		notify(observer -> observer.changedTimePlayed());
	}

	/**
	 * @return the levelTries
	 */
	public int getLevelTries() {
		return levelTries;
	}

	/**
	 * @return the successfulLevelTries
	 */
	public int getSuccessfulLevelTries() {
		return successfulLevelTries;
	}

	/**
	 * @param levelTries the levelTries to set
	 */
	public void setLevelTries(int levelTries) {
		this.levelTries = levelTries;     
	}

	/**
	 * @param successfulLevelTries the successfulLevelTries to set
	 */
	public void setSuccessfulLevelTries(int successfulLevelTries) {
		this.successfulLevelTries = successfulLevelTries;
	}

}
