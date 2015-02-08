package lambda.model.profiles;

import lambda.Observable;
import lambda.model.settings.SettingsModel;
import lambda.model.shop.ShopModel;
import lambda.model.statistics.StatisticModel;

/**
 * Represents a player profile. All playerspecific data is in this class.
 * 
 * @author Kai Fieger
 */
public class ProfileModel extends Observable<ProfileModelObserver> {

    private String name;
    private String avatar;
    private String language;
    private int levelIndex;
    private int coins;
    private SettingsModel settings;
    private ShopModel shop;
    private StatisticModel statistics;

    /**
     * Creates a new profile with the given name and standard values.
     * 
     * @param name
     *            The name of the player. It's unique to all profiles of the
     *            game.
     */
    public ProfileModel(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        this.name = name;
        avatar = "a0";
        language = "data/i18n/StringBundle_de";
        levelIndex = 1;
        coins = 0;
        settings = new SettingsModel();
        shop = ShopModel.getShop();
        statistics = new StatisticModel();
    }

    /**
     * Creates a new profile with the given name and the values of the other.
     * Called to rename a profile. The old profile should not be used
     * afterwards.
     * 
     * @param newName
     *            The new name of the player.
     * @param oldProfile
     *            Profile which gets "renamed"
     */
    public ProfileModel(String newName, ProfileModel oldProfile) {
        if (newName == null) {
            throw new IllegalArgumentException("newName cannot be null");
        }
        if (oldProfile == null) {
            throw new IllegalArgumentException("oldProfile cannot be null");
        }
        if (newName.equals("")) {
            throw new IllegalArgumentException("newName cannot be empty");
        }
        name = newName;
        avatar = oldProfile.avatar;
        language = oldProfile.language;
        levelIndex = oldProfile.levelIndex;
        coins = oldProfile.coins;
        settings = oldProfile.settings;
        shop = oldProfile.shop;
        statistics = oldProfile.statistics;
    }

    /**
     * Returns the name of the player. It identifies the profile.
     * 
     * @return The name of the player.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the ID of the avatar-picture.
     * 
     * @return The ID of the avatar-picture.
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Sets the ID of the avatar-picture.
     * 
     * @param avatar
     *            The new ID of the avatar-picture.
     */
    public void setAvatar(String avatar) {
        if (avatar == null) {
            throw new IllegalArgumentException("avatar cannot be null");
        }
        this.avatar = avatar;
    }

    /**
     * Returns the ID of the language of the profile.
     * 
     * @return The language-ID of the profile.
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the ID of the language of the profile.
     * 
     * @param language
     *            The new language-ID.
     */
    public void setLanguage(String language) {
        if (language == null) {
            throw new IllegalArgumentException("language cannot be null");
        }
        this.language = language;
    }

    /**
     * Returns the levelIndex, which represents the first, unbeaten level of the
     * player (for example: LevelIndex = 2 => Level1 is beaten).
     * 
     * @return The levelIndex. It is always bigger than 0.
     */
    public int getLevelIndex() {
        return levelIndex;
    }

    /**
     * Sets the levelIndex, which represents the first, unbeaten level of the
     * player (for example: LevelIndex = 2 => Level1 is beaten). Afterwards it
     * notifies its observers by calling their
     * {@link ProfileModelObserver#changedLevelIndex() changedLevelIndex()}
     * method.
     * 
     * @param levelIndex
     *            has to be bigger than 0
     */
    public void setLevelIndex(int levelIndex) {
        if (levelIndex < 1) {
            throw new IllegalArgumentException(
                    "levelIndex cannot be smaller than 1");
        }
        this.levelIndex = levelIndex;
        notify(o -> o.changedLevelIndex());
    }

    /**
     * Returns the amount of coins the player owns.
     * 
     * @return coins (always 0 or higher)
     */
    public int getCoins() {
        return coins;
    }

    /**
     * Sets the amount of coins the player owns. Afterwards it notifies its
     * observers by calling their {@link ProfileModelObserver#changedCoins()
     * changedCoins()} method.
     * 
     * @param coins
     *            has to be 0 or higher
     */
    public void setCoins(int coins) {
        if (coins < 0) {
            throw new IllegalArgumentException("coins cannot be smaller than 0");
        }
        this.coins = coins;
        notify(o -> o.changedCoins());
    }

    /**
     * Returns the settings of the profile.
     * 
     * @return The settings of the profile.
     */
    public SettingsModel getSettings() {
        return settings;
    }

    /**
     * Returns the shop of the profile.
     * 
     * @return The shop of the profile.
     */
    public ShopModel getShop() {
        return shop;
    }

    /**
     * Returns the statistics of the profile.
     * 
     * @return The statistics of the profile.
     */
    public StatisticModel getStatistics() {
        return statistics;
    }

}
