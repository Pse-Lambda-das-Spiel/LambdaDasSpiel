package lambda.model.statistics;

import lambda.Consumer;
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
	
	

	/**
	 * @return the lambsEnchanted
	 */
	public int getLambsEnchanted() {
		return lambsEnchanted;
	}



	/**
	 * @return the gemsEnchanted
	 */
	public int getGemsEnchanted() {
		return gemsEnchanted;
	}



	/**
	 * @return the gemsPlaced
	 */
	public int getGemsPlaced() {
		return gemsPlaced;
	}



	/**
	 * @return the lambsPlaced
	 */
	public int getLambsPlaced() {
		return lambsPlaced;
	}



	/**
	 * @return the lambsEnchantedPerLevel
	 */
	public int getLambsEnchantedPerLevel() {
		return lambsEnchantedPerLevel;
	}



	/**
	 * @return the gemsEnchantedPerLevel
	 */
	public int getGemsEnchantedPerLevel() {
		return gemsEnchantedPerLevel;
	}



	/**
	 * @return the gemsPlacedPerLevel
	 */
	public int getGemsPlacedPerLevel() {
		return gemsPlacedPerLevel;
	}



	/**
	 * @return the lambsPlacedPerLevel
	 */
	public int getLambsPlacedPerLevel() {
		return lambsPlacedPerLevel;
	}



	/**
	 * @return the levelCompleted
	 */
	public int getLevelCompleted() {
		return levelCompleted;
	}



	/**
	 * @return the hintsNotUsed
	 */
	public int getHintsNotUsed() {
		return hintsNotUsed;
	}



	/**
	 * @return the beginTime
	 */
	public long getBeginTime() {
		return beginTime;
	}



	/**
	 * @return the timePlayed
	 */
	public long getTimePlayed() {
		return timePlayed;
	}



	/**
	 * @param lambsEnchanted the lambsEnchanted to set
	 */
	public void setLambsEnchanted(int lambsEnchanted) {
		this.lambsEnchanted = lambsEnchanted;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedLambsEnchanted();
             }
         });
	}

	/**
	 * @param gemsEnchanted the gemsEnchanted to set
	 */
	public void setGemsEnchanted(int gemsEnchanted) {
		this.gemsEnchanted = gemsEnchanted;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedGemsEnchanted();
             }
         });
	}

	/**
	 * @param gemsPlaced the gemsPlaced to set
	 */
	public void setGemsPlaced(int gemsPlaced) {
		this.gemsPlaced = gemsPlaced;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedGemsPlaced();
             }
         });
	}

	/**
	 * @param lambsPlaced the lambsPlaced to set
	 */
	public void setLambsPlaced(int lambsPlaced) {
		this.lambsPlaced = lambsPlaced;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedLambsPlaced();
             }
         });
	}

	/**
	 * @param lambsEnchantedPerLevel the lambsEnchantedPerLevel to set
	 */
	public void setLambsEnchantedPerLevel(int lambsEnchantedPerLevel) {
		this.lambsEnchantedPerLevel = lambsEnchantedPerLevel;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedLambsEnchantedPerLevel();
             }
         });
	}

	/**
	 * @param gemsEnchantedPerLevel the gemsEnchantedPerLevel to set
	 */
	public void setGemsEnchantedPerLevel(int gemsEnchantedPerLevel) {
		this.gemsEnchantedPerLevel = gemsEnchantedPerLevel;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedGemsEnchantedPerLevel();
             }
         });
	}

	/**
	 * @param gemsPlacedPerLevel the gemsPlacedPerLevel to set
	 */
	public void setGemsPlacedPerLevel(int gemsPlacedPerLevel) {
		this.gemsPlacedPerLevel = gemsPlacedPerLevel;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedGemsPlacedPerLevel();
             }
         });
	}

	/**
	 * @param lambsPlacedPerLevel the lambsPlacedPerLevel to set
	 */
	public void setLambsPlacedPerLevel(int lambsPlacedPerLevel) {
		this.lambsPlacedPerLevel = lambsPlacedPerLevel;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedLambsPlacedPerLevel();
             }
         });
	}

	/**
	 * @param levelCompleted the levelCompleted to set
	 */
	public void setLevelCompleted(int levelCompleted) {
		this.levelCompleted = levelCompleted;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedLevelCompleted();
             }
         });
	}

	/**
	 * @param hintsNotUsed the hintsNotUsed to set
	 */
	public void setHintsNotUsed(int hintsNotUsed) {
		this.hintsNotUsed = hintsNotUsed;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedHintsNotUsed();
             }
         });
	}

	/**
	 * @param timePlayed the timePlayed to set
	 */
	public void setTimePlayed(long timePlayed) {
		this.timePlayed = timePlayed;
		 notify(new Consumer<StatisticModelObserver>(){
             @Override
             public void accept(StatisticModelObserver observer) {
                 observer.changedTimePlayed();
             }
         });
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
