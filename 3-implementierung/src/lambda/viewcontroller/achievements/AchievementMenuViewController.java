package lambda.viewcontroller.achievements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.achievements.AchievementManager;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;

/**
 * Represents the achievement menu.
 * 
 * @author Robert Hochweiss
 *
 */
public class AchievementMenuViewController extends ViewController {

	private final Stage stage;
	private static AssetManager manager;
	private final static String achievementMenuSkinJson = "data/skins/AchievementMenuSkin.json";
	private final String achievementMenuSkinAtlas = "data/skins/AchievementMenuSkin.atlas";
	private final int ACHIEVEMENTS_PER_ROW = 5;
	private AchievementManager achievementManager;
	private List<AchievementViewController> achievementVCList;
	private Map<String, Label> labelMap;
	private Label titleLabel;
	
	/**
	 * Creates a new instance of this class.
	 */
	public AchievementMenuViewController() {
		stage = new Stage(new ScreenViewport());
		achievementVCList = new ArrayList<>();
		labelMap = new HashMap<>();
		achievementManager = AchievementManager.getManager();
		achievementManager.loadAchievements(achievementVCList);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changedProfile() {
		// It also adds all achievements as observer for the new StatisticModel
		achievementManager.resetAchievements(manager);
		/* Since the achievements are not to be saved 
		 * and the statistic values are already set after a profile change
		 * this has to be called after every profile change
		 */
		achievementManager.checkAllAchievements();
		// Set the label texts according to the current language
		I18NBundle stringBundle = manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(), 
												I18NBundle.class);
		titleLabel.setText(stringBundle.get("achievementMenuTitle"));
		for (String str : achievementManager.getAchievementTypeList()) {
			labelMap.get(str).setText(stringBundle.get(str + "Label") + ":");
		}
		
	}
	
	/*
	 * We need a central class as singleton for the styles.
	 * This method is only tmp
	 */
	/**
	 * Returns the style for an image button with the given icon as image.
	 * 
	 * @param icon the identifier of the icon in the texture atlas
	 * @return the image button style
	 */
	public static ImageButtonStyle getImageButtonStyle(String icon) {
		Skin skin = manager.get(achievementMenuSkinJson, Skin.class);
		ImageButtonStyle style = new ImageButtonStyle(skin.get(ImageButtonStyle.class));
		style.up = skin.getDrawable(icon);
		return style;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void show() {
		Gdx.input.setInputProcessor(stage);
    }

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(delta);
		stage.draw();
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pause() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resume() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void hide() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void dispose() {
    	stage.dispose();
    }

    /**
	 * {@inheritDoc}
	 */
    @Override
    public void queueAssets(AssetManager assets) {
    	assets.load(achievementMenuSkinAtlas, TextureAtlas.class);
		assets.load(achievementMenuSkinJson, Skin.class, new SkinLoader.SkinParameter(achievementMenuSkinAtlas));        
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void create(AssetManager manager) {
    	AchievementMenuViewController.manager = manager;
    	ProfileManager.getManager().addObserver(this);
    	Table mainTable = new Table();
		mainTable.setFillParent(true);
		stage.addActor(mainTable);
		titleLabel = new Label("Initial string", manager.get(achievementMenuSkinJson, Skin.class), "title");
		Table achievementTable = new Table();
		achievementTable.pad(20);
		achievementTable.defaults().space(30, 30, 50, 30);
		int tmpIndex = 0;
		List<String> achievementTypeList = achievementManager.getAchievementTypeList();
		for (int i = 0; i < achievementTypeList.size(); i++) {
			// the labels have to be stored to change their text at a later time
			Label label = new Label("Initial string", manager.get(achievementMenuSkinJson, Skin.class), "normal");
			// only tmp, does not look so nice with scaling
			labelMap.put(achievementTypeList.get(i), label);
			achievementTable.add(label).center().colspan(ACHIEVEMENTS_PER_ROW);
			int n =  achievementManager.getAchievementNumberPerType().get(achievementTypeList.get(i));
			for (int j = 0 ; j < n; j++) {
				if (j % ACHIEVEMENTS_PER_ROW == 0) {
					achievementTable.row();
				}
				AchievementViewController achievementVC = achievementVCList.get(tmpIndex + j);
				achievementVC.fillStack();
				achievementVC.addListener(new AchievementClickListener());
				achievementTable.add(achievementVC).size(80);
			}
			achievementTable.row();
			tmpIndex += n;
		}
		ScrollPane scrollPane = new ScrollPane(achievementTable);
		ImageButton back = new ImageButton(getImageButtonStyle("back"));
		back.addListener(new ClickListener() {
		        @Override
		        public void clicked(InputEvent event, float x, float y) {
		            getGame().setScreen(MainMenuViewController.class);
		        }
		});
		mainTable.add(titleLabel).colspan(2).center().row();
		mainTable.add(back).align(Align.bottomLeft).pad(15);
		mainTable.add(scrollPane).expand().fill().colspan(1).center();
    }
    
    
    private class AchievementClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AchievementViewController clickedActor = (AchievementViewController) event.getListenerActor();
            new Dialog("", manager.get(achievementMenuSkinJson, Skin.class)) {
            	{
            		setWidth(stage.getWidth() - 300);
            		setHeight(stage.getHeight() - 300);
            		addListener(new ClickListener() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            setVisible(false);
                            hide();
                        }
                    });
            		Image image= (new ImageButton(clickedActor.getShownImageButtonStyle()).getImage());
                	add(image).pad(30,10,30,30).size(100);
                	Label label = new Label(clickedActor.getShownText(), 
                			manager.get(achievementMenuSkinJson, Skin.class), "normal");
                	label.setWrap(true);
                	add(label).width(getWidth());
            	}
            }.show(stage);
        }
    }
    
}
