package lambda.model.statistics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Testclass for the StatisticModel.
 * 
 * @author Kai Fieger
 */
public class StatisticModelTest implements StatisticModelObserver {
	private boolean calledLambsEnchanted;
	private boolean calledGemsEnchanted;
	private boolean calledLambsPlaced;
	private boolean calledGemsPlaced;
	private boolean calledLambsEnchantedPerLevel;
	private boolean calledGemsEnchantedPerLevel;
	private boolean calledLambsPlacedPerLevel;
	private boolean calledGemsPlacedPerLevel;
	private boolean calledHintsNotUsed;
	private boolean calledTimePlayed;
	private boolean calledLevelTries;
	private boolean calledSuccessfulLevelTries;
	private StatisticModel stats;

	@Before
	public void setUp() throws Exception {
		calledLambsEnchanted = false;
		calledGemsEnchanted = false;
		calledLambsPlaced = false;
		calledGemsPlaced = false;
		calledLambsEnchantedPerLevel = false;
		calledGemsEnchantedPerLevel = false;
		calledLambsPlacedPerLevel = false;
		calledGemsPlacedPerLevel = false;
		calledHintsNotUsed = false;
		calledTimePlayed = false;
		calledLevelTries = false;
		calledSuccessfulLevelTries = false;
		stats = new StatisticModel();
		stats.addObserver(this);
	}

	@After
	public void tearDown() throws Exception {
		if (stats != null) {
			stats.removeObserver(this);
			stats = null;
		}
	}
	
	/**
	 * Tests all getter- and setter-methods of the StatisticModel 
	 * and checks if it calls the correct methods on its observers.
	 */
	@Test
	public void testGetterSetter() {
		int i = 0;
		assertFalse(calledGemsEnchanted);
		stats.setGemsEnchanted(i);
		assertTrue(calledGemsEnchanted);
		assertEquals(i++, stats.getGemsEnchanted());
		
		assertFalse(calledGemsEnchantedPerLevel);
		stats.setGemsEnchantedPerLevel(i);
		assertTrue(calledGemsEnchantedPerLevel);
		assertEquals(i++, stats.getGemsEnchantedPerLevel());
		
		assertFalse(calledGemsPlaced);
		stats.setGemsPlaced(i);
		assertTrue(calledGemsPlaced);
		assertEquals(i++, stats.getGemsPlaced());

		assertFalse(calledGemsPlacedPerLevel);
		stats.setGemsPlacedPerLevel(i);
		assertTrue(calledGemsPlacedPerLevel);
		assertEquals(i++, stats.getGemsPlacedPerLevel());
		
		assertFalse(calledHintsNotUsed);
		stats.setHintsNotUsed(i);
		assertTrue(calledHintsNotUsed);
		assertEquals(i++, stats.getHintsNotUsed());
		
		assertFalse(calledLambsEnchanted);
		stats.setLambsEnchanted(i);
		assertTrue(calledLambsEnchanted);
		assertEquals(i++, stats.getLambsEnchanted());

		assertFalse(calledLambsEnchantedPerLevel);
		stats.setLambsEnchantedPerLevel(i);
		assertTrue(calledLambsEnchantedPerLevel);
		assertEquals(i++, stats.getLambsEnchantedPerLevel());
		
		assertFalse(calledLambsPlaced);
		stats.setLambsPlaced(i);
		assertTrue(calledLambsPlaced);
		assertEquals(i++, stats.getLambsPlaced());
		
		assertFalse(calledLambsPlacedPerLevel);
		stats.setLambsPlacedPerLevel(i);
		assertTrue(calledLambsPlacedPerLevel);
		assertEquals(i++, stats.getLambsPlacedPerLevel());

		assertFalse(calledLevelTries);
		stats.setLevelTries(i);
		assertTrue(calledLevelTries);
		assertEquals(i++, stats.getLevelTries());

		assertFalse(calledSuccessfulLevelTries);
		stats.setSuccessfulLevelTries(i);
		assertTrue(calledSuccessfulLevelTries);
		assertEquals(i++, stats.getSuccessfulLevelTries());
		
		assertFalse(calledTimePlayed);
		stats.setTimePlayed(i);
		assertTrue(calledTimePlayed);
		assertEquals(i++, stats.getTimePlayed());
	}
	
	/**
	 * Tests the time conversion of the StatisticModel
	 */
	@Test
	public void testTimeToString() {
		stats.setTimePlayed(3723);
		assertEquals("1 h  02 min  03 s", stats.convertTimeToString());
	}

	@Override
	public void changedLambsEnchanted() {
		calledLambsEnchanted = true;
	}

	@Override
	public void changedGemsEnchanted() {
		calledGemsEnchanted = true;
	}

	@Override
	public void changedLambsPlaced() {
		calledLambsPlaced = true;
	}

	@Override
	public void changedGemsPlaced() {
		calledGemsPlaced = true;
	}

	@Override
	public void changedLambsEnchantedPerLevel() {
		calledLambsEnchantedPerLevel = true;
	}

	@Override
	public void changedGemsEnchantedPerLevel() {
		calledGemsEnchantedPerLevel = true;
	}

	@Override
	public void changedLambsPlacedPerLevel() {
		calledLambsPlacedPerLevel = true;
	}

	@Override
	public void changedGemsPlacedPerLevel() {
		calledGemsPlacedPerLevel = true;
	}

	@Override
	public void changedHintsNotUsed() {
		calledHintsNotUsed = true;
	}

	@Override
	public void changedTimePlayed() {
		calledTimePlayed = true;
	}

	@Override
	public void changedLevelTries() {
		calledLevelTries = true;
	}

	@Override
	public void changedSuccessfulLevelTries() {
		calledSuccessfulLevelTries = true;
	}

}
