package lambda.model.statistics;

import lambda.Consumer;
import lambda.Observable;

/**
 * This class serves as the statistic of a profile for the data gained through playing the levels of the game.
 * 
 * @author Farid el-haddad, Robert Hochweiss
 */
public class StatisticModel extends Observable<StatisticModelObserver> {

	private int gemsEnchanted;
	private int lambsEnchanted;
	private int gemsPlaced;
	private int lambsPlaced;
	private int gemsEnchantedPerLevel;
	private int lambsEnchantedPerLevel;
	private int gemsPlacedPerLevel;
	private int lambsPlacedPerLevel;
	private int levelCompleted;
	private int hintsNotUsed;
	private int levelTries;
	private int successfulLevelTries;
	private long timePlayed;

	/**
	 * Creates a new instance of this class.
	 */
	public StatisticModel() {
	}

	/**
	 * Returns the number enchanted lambs.
	 * 
	 * @return the number of enchanted lambs
	 */
	public int getLambsEnchanted() {
		return lambsEnchanted;
	}

	/**
	 * Returns the number of enchanted gems.
	 * 
	 * @return the number of enchanted gems.
	 */
	public int getGemsEnchanted() {
		return gemsEnchanted;
	}

	/**
	 * Returns the number of placed gems.
	 * 
	 * @return the number of placed gems
	 */
	public int getGemsPlaced() {
		return gemsPlaced;
	}

	/**
	 * Returns the number of placed lambs.
	 * 
	 * @return the number of placed lambs
	 */
	public int getLambsPlaced() {
		return lambsPlaced;
	}

	/**
	 * Returns the highest number of enchanted lambs in a level.
	 * 
	 * @return the highest number of enchanted lambs in a level.
	 */
	public int getLambsEnchantedPerLevel() {
		return lambsEnchantedPerLevel;
	}

	/**
	 * Returns the highest number of enchanted gems in a level.
	 * 
	 * @return the highest number of enchanted gems in a level.
	 */
	public int getGemsEnchantedPerLevel() {
		return gemsEnchantedPerLevel;
	}

	/**
	 * Returns the highest number of placed gems in a level.
	 * 
	 * @return the highest number of placed gems in a level.
	 */
	public int getGemsPlacedPerLevel() {
		return gemsPlacedPerLevel;
	}

	/**
	 * Returns the highest number of placed lambs in a level.
	 * 
	 * @return the highest number of placed gems in a level.
	 */
	public int getLambsPlacedPerLevel() {
		return lambsPlacedPerLevel;
	}

	/**
	 * Returns the number of successfully completed level.
	 * 
	 * @return the number of successfully completed level
	 */
	public int getLevelCompleted() {
		return levelCompleted;
	}

	/**
	 * Returns the number of times when the hint was not used in a solved level.
	 * 
	 * @return the number of times when the hint was not used in a solved level.
	 */
	public int getHintsNotUsed() {
		return hintsNotUsed;
	}

	/**
	 * Returns the number of level tries.
	 * 
	 * @return the number of level tries
	 */
	public int getLevelTries() {
		return levelTries;
	}

	/**
	 * Returns the number of successful level tries.
	 * 
	 * @return the number of successful level tries
	 */
	public int getSuccessfulLevelTries() {
		return successfulLevelTries;
	}

	/**
	 * Returns the played time spent playing levels in seconds.
	 * 
	 * @return the played time spent playing levels in seconds.
	 */
	public long getTimePlayed() {
		return timePlayed;
	}

	/**
	 * Sets the number of enchanted lambs.
	 * 
	 * @param lambsEnchanted
	 *            the number of enchanted lambs. to set
	 */
	public void setLambsEnchanted(int lambsEnchanted) {
		this.lambsEnchanted = lambsEnchanted;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedLambsEnchanted();
			}
		});
	}

	/**
	 * Sets the number of enchanted gems.
	 * 
	 * @param gemsEnchanted
	 *            the number of enchanted gems to set.
	 */
	public void setGemsEnchanted(int gemsEnchanted) {
		this.gemsEnchanted = gemsEnchanted;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedGemsEnchanted();
			}
		});
	}

	/**
	 * Sets the number of placed gems.
	 * 
	 * @param gemsPlaced
	 *            the number of placed gems to set
	 */
	public void setGemsPlaced(int gemsPlaced) {
		this.gemsPlaced = gemsPlaced;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedGemsPlaced();
			}
		});
	}

	/**
	 * Sets the number of placed lambs.
	 * 
	 * @param lambsPlaced
	 *            the number of placed lambs to set
	 */
	public void setLambsPlaced(int lambsPlaced) {
		this.lambsPlaced = lambsPlaced;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedLambsPlaced();
			}
		});
	}

	/**
	 * Sets the highest number of enchanted lambs in a level.
	 * 
	 * @param lambsEnchantedPerLevel
	 *            the highest number of enchanted lambs in a level to set
	 */
	public void setLambsEnchantedPerLevel(int lambsEnchantedPerLevel) {
		this.lambsEnchantedPerLevel = lambsEnchantedPerLevel;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedLambsEnchantedPerLevel();
			}
		});
	}

	/**
	 * Sets the highest number of enchanted gems in a level.
	 * 
	 * @param gemsEnchantedPerLevel
	 *            the highest number of enchanted gems in a level to set
	 */
	public void setGemsEnchantedPerLevel(int gemsEnchantedPerLevel) {
		this.gemsEnchantedPerLevel = gemsEnchantedPerLevel;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedGemsEnchantedPerLevel();
			}
		});
	}

	/**
	 * Sets the highest number of placed gems in a level.
	 * 
	 * @param gemsPlacedPerLevel
	 *            the highest number of placed gems in a level
	 */
	public void setGemsPlacedPerLevel(int gemsPlacedPerLevel) {
		this.gemsPlacedPerLevel = gemsPlacedPerLevel;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedGemsPlacedPerLevel();
			}
		});
	}

	/**
	 * Sets the highest number of placed lambs in a level.
	 * 
	 * @param lambsPlacedPerLevel
	 *            the highest number of placed lambs in a level to set
	 */
	public void setLambsPlacedPerLevel(int lambsPlacedPerLevel) {
		this.lambsPlacedPerLevel = lambsPlacedPerLevel;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedLambsPlacedPerLevel();
			}
		});
	}

	/**
	 * Sets the number of successfully completed level.
	 * 
	 * @param levelCompleted
	 *            the number of successfully completed level to set
	 */
	public void setLevelCompleted(int levelCompleted) {
		this.levelCompleted = levelCompleted;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedLevelCompleted();
			}
		});
	}

	/**
	 * Sets the number of successful level tries without using the hint.
	 * 
	 * @param hintsNotUsed
	 *            the number of successful level tries without using the hint to set
	 */
	public void setHintsNotUsed(int hintsNotUsed) {
		this.hintsNotUsed = hintsNotUsed;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedHintsNotUsed();
			}
		});
	}

	/**
	 * Sets the number of level tries.
	 * 
	 * @param levelTries
	 *            the number of level tries to set
	 */
	public void setLevelTries(int levelTries) {
		this.levelTries = levelTries;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedLevelTries();
			}
		});
	}

	/**
	 * Sets the number of successful level tries.
	 * 
	 * @param successfulLevelTries
	 *            the number of successful level tries to set
	 */
	public void setSuccessfulLevelTries(int successfulLevelTries) {
		this.successfulLevelTries = successfulLevelTries;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedSuccessfulLevelTries();
			}
		});
	}

	/**
	 * Sets the played time spent playing in levels.
	 * 
	 * @param timePlayed
	 *            the played time spent playing in levels to set
	 */
	public void setTimePlayed(long timePlayed) {
		this.timePlayed = timePlayed;
		notify(new Consumer<StatisticModelObserver>() {
			@Override
			public void accept(StatisticModelObserver observer) {
				observer.changedTimePlayed();
			}
		});
	}

	/**
	 * 
	 * Returns the string representation of the played time in the format "h min s".
	 * 
	 * @return the string representation of the played time
	 */
	public String convertTimeToString() {
		long sec = timePlayed;
		long hours = sec / 3600;
		sec %= 3600;
		long min = sec / 60;
		sec %= 60;
		String result = hours + " h  " + String.format("%02d", min) + " min  " + String.format("%02d", sec) + " s";
		return result;
	}
}
