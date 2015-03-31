package lambda.viewcontroller.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.levels.LevelContext;
import lambda.model.levels.LevelManager;
import lambda.model.levels.LevelModel;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModelObserver;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.editor.EditorViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * This class represents the menu for the level selection.
 * 
 * @author Robert Hochweiss
 */
public class LevelSelectionViewController extends StageViewController implements
        ProfileModelObserver {

    private static final int LEVEL_BUTTONS_PER_ROW = 3;
    private int currentPage;
    private LevelManager levelManager;
    private Skin skin;
    private List<TextButton> levelButtons;
    private LevelStack levelStack;
    private Table mainTable;
    private final float buttonSize;

    /**
     * Creates a new LevelSelectionViewController.
     */
    public LevelSelectionViewController() {
        levelManager = LevelManager.getLevelManager();
        levelButtons = new ArrayList<>();
        currentPage = 1;
        buttonSize = (getStage().getWidth() / 8);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void queueAssets(AssetManager assets) {
        levelManager.queueAssets(assets);
        TextureParameter textureParameter = new TextureParameter();
        textureParameter.minFilter = Texture.TextureFilter.Linear;
        textureParameter.magFilter = Texture.TextureFilter.Linear;
        assets.load("data/backgrounds/levelmenu.png", Texture.class,
                textureParameter);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
        super.show();
        AudioManager.playDefaultMusic();
        int levelIndex = ProfileManager.getManager().getCurrentProfile()
                .getLevelIndex();
        int pageNumber = (levelIndex - 1) / LevelManager.LEVEL_PER_DIFFICULTY;
        if (pageNumber >= levelStack.size()) {
            pageNumber--;
        }
        currentPage = pageNumber;
        levelStack.setPageVisible(currentPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedProfile() {
        int levelIndex = ProfileManager.getManager().getCurrentProfile()
                .getLevelIndex();
        for (int i = 0; i < levelButtons.size(); i++) {
            String str = "";
            if ((i < levelIndex - 1) && (levelIndex >= 2)) {
                str = "completed";
            } else if (i == levelIndex - 1) {
                str = "unlocked";
            } else {
                str = "locked";
            }
            updateLevelButton(levelButtons.get(i), str,
                    levelManager.getLevel(i + 1));
        }
        ProfileManager.getManager().getCurrentProfile().addObserver(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedLevelIndex() {
        int levelIndex = ProfileManager.getManager().getCurrentProfile()
                .getLevelIndex();
        if (levelIndex >= 2) {
            updateLevelButton(levelButtons.get(levelIndex - 2), "completed",
                    levelManager.getLevel(levelIndex - 1));
        }
        if (levelIndex < levelManager.getNumberOfLevels() + 1) {
            updateLevelButton(levelButtons.get(levelIndex - 1), "unlocked",
                    levelManager.getLevel(levelIndex));
        }
    }

    /**
     * Creates a new {@link LevelContext} with the given level and starts the
     * level.
     * 
     * @param level
     *            the to be started level
     * @throws IllegalArgumentException
     *             level is null
     */
    public void startLevel(LevelModel level) {
        if (level == null) {
            throw new IllegalArgumentException(
                    "The to be started level cannot be null!");
        }
        getGame().getController(EditorViewController.class).reset(
                new LevelContext(level));
        getGame().setScreen(EditorViewController.class);
    }

    /**
     * Update the background and the listeners of the given level button
     * according to the given new state.
     * 
     * @param levelButton
     *            the level button which background is to be changed
     * @param newState
     *            the new state of the level button
     * @param level
     *            the {@link LevelModel} that is represented by the button
     * @throws IllegalArgumentException
     *             if levelButton or level is null or if newState is invalid or
     *             null
     */
    public void updateLevelButton(final TextButton levelButton,
            String newState, final LevelModel level) {
        if (levelButton == null) {
            throw new IllegalArgumentException(
                    "The levelButton cannot be null!");
        }
        if (level == null) {
            throw new IllegalArgumentException("The level cannot be null!");
        }
        if (newState == null) {
            throw new IllegalArgumentException("The newState cannot be null!");
        }
        /*
         * normal order for states: locked => unlocked => completed (order can
         * be different because of profile change) locked => unlocked: level can
         * be started unlocked, completed => locked (at a profile change): level
         * cannot be started
         */
        switch (newState) {
        case "locked":
            levelButton.clearListeners();
            break;
        case "completed":
            levelButton.clearListeners();
            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    startLevel(level);
                }
            });
            break;
        case "unlocked":
            levelButton.clearListeners();
            levelButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    AudioManager.playSound("buttonClick");
                    startLevel(level);
                }
            });
            break;
        default:
            throw new IllegalArgumentException(
                    "Invalide newState for the levelButton!");
        }
        // change background of button according to newState
        TextButtonStyle style = new TextButtonStyle(skin.get("level",
                TextButtonStyle.class));
        if (newState.equals("locked")) {
            style.up = skin.getDrawable("level_" + newState);
        } else {
            style.up = skin.getDrawable("level_"
                    + newState
                    + levelManager.getDifficultySetting(level.getDifficulty())
                            .getDifficulty());
        }
        levelButton.setStyle(style);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final AssetManager manager) {
        setLastViewController(MainMenuViewController.class);
        Image background = new Image(manager.get(
                "data/backgrounds/levelmenu.png", Texture.class));
        background.setWidth(getStage().getWidth());
        background.setHeight(getStage().getHeight());
        getStage().addActor(background);

        skin = manager.get("data/skins/MasterSkin.json", Skin.class);
        ProfileManager.getManager().addObserver(this);
        ImageButton leftButton = new ImageButton(skin, "backButton");
        ImageButton rightButton = new ImageButton(skin, "nextButton");
        ImageButton backButton = new ImageButton(skin, "backButton");
        ImageButton helpButton = new ImageButton(skin, "helpButton");
        ImageButtonStyle sandboxStyle = new ImageButtonStyle(
                skin.get(ImageButtonStyle.class));
        sandboxStyle.imageUp = skin.getDrawable("sandbox");
        ImageButton sandboxButton = new ImageButton(sandboxStyle);

        for (int i = 0; i < levelManager.getNumberOfLevels(); i++) {
            levelButtons.add(new TextButton(Integer.toString(i + 1), skin,
                    "level"));
        }
        levelStack = new LevelStack();
        displayLevelButtons();
        mainTable = new Table();
        mainTable.setFillParent(true);
        getStage().addActor(mainTable);
        mainTable.add(sandboxButton).align(Align.center).size(buttonSize).row();
        mainTable.add();

        Table centralTable = new Table();
        centralTable.align(Align.center);
        centralTable.add(leftButton).pad(buttonSize / 15)
                .size(buttonSize * 0.8f);
        centralTable.add(levelStack).size(getStage().getWidth() / 2,
                getStage().getHeight() / 2);
        centralTable.add(rightButton).pad(buttonSize / 15)
                .size(buttonSize * 0.8f);
        mainTable.add(centralTable).align(Align.center).row();
        mainTable.add(backButton).pad(buttonSize / 10).align(Align.bottomLeft)
                .size(buttonSize * 0.8f);
        mainTable.add();
        mainTable.add(helpButton).pad(buttonSize / 10).size(buttonSize * 0.8f)
                .align(Align.bottomRight);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                getGame().setScreen(MainMenuViewController.class);
            }
        });

        sandboxButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                startLevel(levelManager.getLevel(0));
            }
        });

        helpButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                AudioManager.playSound("buttonClick");
                showDialog(new HelpDialog(manager.get("data/skins/DialogTemp.json",
                        Skin.class), manager.get(ProfileManager.getManager()
                        .getCurrentProfile().getLanguage(), I18NBundle.class),
                        getStage().getWidth(), getStage().getHeight()));
            }
        });

        leftButton.addListener(new SelectLeftClickListener());
        rightButton.addListener(new SelectRightClickListener());
    }

    private void displayLevelButtons() {
        int pageNumber = levelManager.getNumberOfLevels()
                / LevelManager.LEVEL_PER_DIFFICULTY;
        if ((levelManager.getNumberOfLevels() % LevelManager.LEVEL_PER_DIFFICULTY) > 0) {
            pageNumber++;
        }
        int rowNumber = LevelManager.LEVEL_PER_DIFFICULTY
                / LEVEL_BUTTONS_PER_ROW;
        if ((LevelManager.LEVEL_PER_DIFFICULTY % LEVEL_BUTTONS_PER_ROW) > 0) {
            rowNumber++;
        }
        int n = 0;
        for (int i = 0; i < pageNumber; i++) {
            Table page = new Table();
            page.defaults()
                    .size(buttonSize)
                    .space(buttonSize / 4, buttonSize / 6, buttonSize / 4,
                            buttonSize / 6);
            for (int j = 0; j < rowNumber; j++) {
                for (int k = 0; k < LEVEL_BUTTONS_PER_ROW; k++) {
                    page.add(levelButtons.get(k + n));
                }
                n += LEVEL_BUTTONS_PER_ROW;
                page.row();
            }
            levelStack.addPage(page);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void changedCoins() {
    }

    private class SelectLeftClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            // for cyclic display
            if ((currentPage - 1) < 0) {
                currentPage = levelStack.size() - 1;
            } else {
                currentPage--;
            }
            levelStack.setPageVisible(currentPage);
        }
    }

    private class SelectRightClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            // for cyclic display
            if ((currentPage + 1) == levelStack.size()) {
                currentPage = 0;
            } else {
                currentPage++;
            }
            levelStack.setPageVisible(currentPage);
        }
    }

}
