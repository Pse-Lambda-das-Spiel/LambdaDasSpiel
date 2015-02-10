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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import lambda.model.achievements.AchievementManager;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;

/**
 * Represents the achievment menu.
 * 
 * @author Robert Hochweiss
 *
 */
public class AchievementMenuViewController extends ViewController {

	private Stage stage;
	private static AssetManager assets;
	private final static String achievementMenuSkinJson = "data/skins/AchievementMenuSkin.json";
	private final String achievementMenuSkinAtlas = "data/skins/AchievementMenuSkin.atlas";
	private final int ACHIEVEMENTS_PER_ROW = 5;
	private AchievementManager aManager;
	private List<AchievementViewController> achievementVCList;
	private Map<String, Label> labelMap;
	private Label titleLabel;
	private ImageButton achievementLockedButton;
	
	/**
	 * Creates a new instance of this class.
	 */
	public AchievementMenuViewController() {
		stage = new Stage(new ScreenViewport());
		achievementVCList = new ArrayList<>();
		labelMap = new HashMap<>();
		aManager = AchievementManager.getManager();
		aManager.loadAchievements(achievementVCList);
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
		Skin skin = assets.get(achievementMenuSkinJson, Skin.class);
		ImageButtonStyle style = new ImageButtonStyle(skin.get(ImageButtonStyle.class));
		style.imageUp = skin.getDrawable(icon);
		return style;
	}
	
    @Override
    public void queueAssets(AssetManager assets) {
    	assets.load(achievementMenuSkinAtlas, TextureAtlas.class);
		assets.load(achievementMenuSkinJson, Skin.class, new SkinLoader.SkinParameter(achievementMenuSkinAtlas));        
    }

    @Override
    public void show() {
		Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
    	  Gdx.gl.glClearColor(1, 1, 1, 1);
          Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
          stage.act(delta);
          stage.draw();    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().setScreenSize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    	stage.dispose();
    }

    @Override
    public void create(AssetManager manager) {
    	assets = manager;
    	ProfileManager.getManager().addObserver(this);
    	Table mainTable = new Table();
		mainTable.setFillParent(true);
		stage.addActor(mainTable);
		titleLabel = new Label("Initial string", assets.get(achievementMenuSkinJson, Skin.class));
		//titleLabel.setText("Title");
		Table achievementTable = new Table();
		//achievementTable.setFillParent(true);
		achievementTable.pad(20);
		achievementTable.defaults().space(30, 30, 50, 30);
		int tmpIndex = 0;
		List<String> achievementTypeList = aManager.getAchievementTypeList();
		for (int i = 0; i < achievementTypeList.size(); i++) {
			labelMap.put(achievementTypeList.get(i), 
					new Label("Initial string", assets.get(achievementMenuSkinJson, Skin.class)));
			achievementTable.add(labelMap.get(achievementTypeList.get(i))).center().colspan(ACHIEVEMENTS_PER_ROW);
			int n =  aManager.getAchievementNumberPerType().get(achievementTypeList.get(i));
			for (int j = 0 ; j < n; j++) {
				if (j % ACHIEVEMENTS_PER_ROW == 0) {
					achievementTable.row();
				}
				AchievementViewController achievementVC = achievementVCList.get(tmpIndex + j);
				achievementVC.fillStack();
				achievementTable.add(achievementVC).size(80);
			}
			achievementTable.row();
			tmpIndex += n;
		}
		ScrollPane scrollPane = new ScrollPane(achievementTable);
		mainTable.add(titleLabel).center();
		mainTable.row();
		mainTable.add(scrollPane).expand().fill().colspan(1).center();
		mainTable.row();
		ImageButton back = new ImageButton(getImageButtonStyle("back"));
		mainTable.add(back).bottom().left().pad(10);
    }

}
