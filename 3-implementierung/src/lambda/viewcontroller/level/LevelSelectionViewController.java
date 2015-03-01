package lambda.viewcontroller.level;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import lambda.model.levels.LevelContext;
import lambda.model.levels.LevelManager;
import lambda.model.levels.LevelModel;
import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.profiles.ProfileModelObserver;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.editor.EditorViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * This class represents the menu for the level selection.
 * 
 * @author Robert Hochweiss
 */
public class LevelSelectionViewController extends StageViewController implements ProfileModelObserver {

   
	private final int LEVEL_BUTTONS_PER_ROW = 3;
	private int currentPage;
    private LevelManager levelManager;
    private Skin skin;
    private List<TextButton> levelButtons;
    private LevelStack levelStack;
    private Table mainTable;
    private float buttonSize;
    
    /**
     * Creates a new LevelSelectionViewController.
     */
	public LevelSelectionViewController() {
        levelManager = LevelManager.getLevelManager();
        levelButtons = new ArrayList<>();
        currentPage = 1;
	}

	/**
	 * {@inheritDoc}
	 */
    @Override
    public void queueAssets(AssetManager assets) {
    	levelManager.queueAssets(assets);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
    	super.show();
    	int levelIndex = ProfileManager.getManager().getCurrentProfile().getLevelIndex();
    	int pageNumber = (levelIndex - 1) / LevelManager.LEVEL_PER_DIFFICULTY;
    	if (pageNumber >= levelStack.size()) {
    		currentPage = levelStack.size() - 1;
    	} else {
    		currentPage = pageNumber;
    	}
    	levelStack.setPageVisible(currentPage);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void changedProfile() {
    	int levelIndex = ProfileManager.getManager().getCurrentProfile().getLevelIndex();
    	for (int i = 0; i < levelButtons.size(); i++) {
    		String str = "";
    		if ((i < levelIndex - 1) && (levelIndex >= 2)) {
    			str = "completed";
    		} else if (i == levelIndex - 1) {
    			str = "unlocked";
    		} else {
    			str = "locked";
    		}
    		updateLevelButton(levelButtons.get(i), str, levelManager.getLevel(i + 1));
    	}
    	ProfileManager.getManager().getCurrentProfile().addObserver(this);
    }
    
    /**
     * {@inheritDoc}
     */
	@Override
	public void changedLevelIndex() {
		int levelIndex = ProfileManager.getManager().getCurrentProfile().getLevelIndex();
		if (levelIndex >= 2) {
			updateLevelButton(levelButtons.get(levelIndex - 2), "completed", levelManager.getLevel(levelIndex - 1));
		}
		if (levelIndex < levelManager.getNumberOfLevels() + 1) {
			updateLevelButton(levelButtons.get(levelIndex - 1), "unlocked", levelManager.getLevel(levelIndex));
		}
	}
    
	/**
	 * Update the background and the listeners of the given level button according to the given new state.
	 * 
	 * @param levelButton the level button which background is to be changed
	 * @param newState the new state of the level button
	 * @param level the {@link LevelModel} that is represented by the button
	 * @throws IllegalArgumentException if levelButton or difficultySetting is null or if newState is invalid or null
	 */
    public void updateLevelButton(TextButton levelButton, String newState, final LevelModel level) {
    	if (levelButton == null) {
    		throw new IllegalArgumentException("The levelButton cannot be null!");
    	}
    	if (level == null) {
    		throw new IllegalArgumentException("The level cannot be null!");
    	}
    	if (newState == null) {
    		throw new IllegalArgumentException("The newState cannot be null!");
    	}
    	/*
    	 * normal order for states: locked => unlocked => completed (order can be different because of profile change)
    	 * locked => unlocked: level can be startet
    	 * unlocked, completed => locked (at a profile change): level cannot be started
    	 */
    	switch(newState) {
    	case "locked":
    		levelButton.clearListeners();
    		break;
    	case "completed":
    		levelButton.clearListeners();
    		levelButton.addListener(new ClickListener() {
    			@Override
    			public void clicked(InputEvent event, float x, float y) {
    				getGame().getController(EditorViewController.class).reset(new LevelContext(level));
    				getGame().setScreen(EditorViewController.class);
    			}
    		});
    		break;
    	case "unlocked":
    		levelButton.clearListeners();
    		levelButton.addListener(new ClickListener() {
    			@Override
    			public void clicked(InputEvent event, float x, float y) {
    				getGame().getController(EditorViewController.class).reset(new LevelContext(level));
    				getGame().setScreen(EditorViewController.class);
    			}
    		});
    		break;
		default:
			throw new IllegalArgumentException("Invalide newState for the levelButton!");
    	}
    	// change background of button according to newState
    	TextButtonStyle style = new TextButtonStyle(skin.get("level", TextButtonStyle.class));
    	if (newState.equalsIgnoreCase("locked")) {
    		style.up = skin.getDrawable("level_" + newState);
    	} else {
    		style.up = skin.getDrawable("level_" + newState 
    				+ levelManager.getDifficultySetting(level.getDifficulty()).getDifficulty());
    	}
    	levelButton.setStyle(style);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void create(final AssetManager manager) {
    	skin = manager.get("data/skins/MasterSkin.json", Skin.class);
    	ProfileManager.getManager().addObserver(this);
    	
    	buttonSize =  (getStage().getWidth() / 8);
    	ImageButton leftButton = new ImageButton(skin, "backButton"); 
    	ImageButton rightButton = new ImageButton(skin, "nextButton"); 
    	ImageButton backButton = new ImageButton(skin, "backButton"); 
    	ImageButton helpButton = new ImageButton(skin, "helpButton"); 
    	ImageButtonStyle sandboxStyle = new ImageButtonStyle(skin.get(ImageButtonStyle.class));
    	sandboxStyle.imageUp = skin.getDrawable("sandbox");
    	ImageButton sandboxButton = new ImageButton(sandboxStyle); 
    	
    	for (int i = 0; i < levelManager.getNumberOfLevels(); i++) {
    		levelButtons.add(new TextButton(Integer.toString(i + 1), skin, "level"));
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
    	centralTable.add(leftButton).pad(buttonSize / 15).size(buttonSize * 0.8f);
    	centralTable.add(levelStack).size(getStage().getWidth() / 2, getStage().getHeight() / 2);
    	centralTable.add(rightButton).pad(buttonSize / 15).size(buttonSize * 0.8f);
    	mainTable.add(centralTable).align(Align.center).row();
    	mainTable.add(backButton).pad(buttonSize / 10).align(Align.bottomLeft).size(buttonSize * 0.8f);
    	mainTable.add();
    	mainTable.add(helpButton).pad(buttonSize / 10).size(buttonSize * 0.8f).align(Align.bottomRight);
    	
    	backButton.addListener(new ClickListener() {
    			@Override
    			public void clicked(InputEvent event, float x, float y) {
    				getGame().setScreen(MainMenuViewController.class);
    			}
		});
    	
    	sandboxButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				getGame().getController(EditorViewController.class).reset(new LevelContext(levelManager.getLevel(0)));
				getGame().setScreen(EditorViewController.class);
			}
    	});
    	
    	helpButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				/* for the test pupose
				ProfileModel profile = ProfileManager.getManager().getCurrentProfile();
				if (profile.getLevelIndex() <= levelManager.getNumberOfLevels() + 1) {
					profile.setLevelIndex(profile.getLevelIndex() + 1);
				}
				*/
				
				// TODO help dialog
			}
    	});
    	
    	leftButton.addListener(new SelectLeftClickListener());
    	rightButton.addListener(new SelectRightClickListener());
    }

    private void displayLevelButtons() {
    	int pageNumber = levelManager.getNumberOfLevels() / levelManager.LEVEL_PER_DIFFICULTY;
    	if ((levelManager.getNumberOfLevels() % levelManager.LEVEL_PER_DIFFICULTY) > 0) {
    		pageNumber++;
    	}
    	int rowNumber = levelManager.LEVEL_PER_DIFFICULTY / LEVEL_BUTTONS_PER_ROW;
    	if ((levelManager.LEVEL_PER_DIFFICULTY % LEVEL_BUTTONS_PER_ROW) > 0) {
    		rowNumber++;
    	}
    	int n = 0;
    	for (int i = 0; i < pageNumber; i++) {
    		Table page = new Table();
    		page.defaults().size(buttonSize).space(buttonSize / 4, buttonSize / 6, buttonSize / 4, buttonSize / 6);
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
