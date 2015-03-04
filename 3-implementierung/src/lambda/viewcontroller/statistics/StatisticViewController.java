package lambda.viewcontroller.statistics;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.I18NBundle;

import lambda.model.profiles.ProfileManager;
import lambda.model.profiles.ProfileModel;
import lambda.model.statistics.StatisticModel;
import lambda.viewcontroller.AudioManager;
import lambda.viewcontroller.StageViewController;
import lambda.viewcontroller.mainmenu.MainMenuViewController;
import lambda.viewcontroller.settings.SettingsViewController;



public class StatisticViewController extends StageViewController {
	
	private String skin = "data/skins/MasterSkin.json";
	private StatisticModel statistics;
	private TextButton lambsEnchanted;
	private TextButton gemsEnchanted;
	private TextButton gemsPlaced;
	private TextButton lambsPlaced;
	private TextButton levelTries;
	private TextButton successfulLevelTries;
	private TextButton timePlayed;
	
	
	
	private AssetManager manager;
	 private final float space;

	public StatisticViewController() {
		ProfileManager.getManager().addObserver(this);
		statistics = new StatisticModel();
		space = getStage().getWidth() / 64;
	}

    @Override
    public void queueAssets(AssetManager assets) {
    	assets.load(skin, Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
        assets.load("data/backgrounds/default.png", Texture.class, new TextureParameter());
    }
 

    @Override
    public void create(final AssetManager manager) {
    	 setLastViewController(SettingsViewController.class);
    	 Image background = new Image(manager.get("data/backgrounds/default.png", Texture.class));
         background.setWidth(getStage().getWidth());
         background.setHeight(getStage().getHeight());
         getStage().addActor(background);
         
         lambsEnchanted = new TextButton("", manager.get(skin, Skin.class));
         gemsEnchanted = new TextButton("", manager.get(skin, Skin.class));
         gemsPlaced = new TextButton("", manager.get(skin, Skin.class));
         lambsPlaced = new TextButton("", manager.get(skin, Skin.class));
         levelTries = new TextButton("", manager.get(skin, Skin.class));
         successfulLevelTries = new TextButton("", manager.get(skin, Skin.class));
         timePlayed = new TextButton("", manager.get(skin, Skin.class));
         
         
         Table statisticsView = new Table();
         statisticsView.align(Align.top);
         getStage().addActor(statisticsView);
         statisticsView.setFillParent(true);
         float height = getStage().getHeight() / 15;
         float width = getStage().getWidth() * 0.5f;
         statisticsView.row().height(height);
         statisticsView.add();
         statisticsView.row().height(height);
        statisticsView.add(lambsEnchanted).width(width);
        statisticsView.row().height(height);
        statisticsView.add();  
        statisticsView.row().height(height);
        statisticsView.add(gemsEnchanted).width(width);
        statisticsView.row().height(height);
        statisticsView.add();
        statisticsView.row().height(height);
        statisticsView.add(gemsPlaced).width(width);
        statisticsView.row().height(height);
        statisticsView.add();
        statisticsView.row().height(height);
        statisticsView.add(lambsPlaced).width(width);
        statisticsView.row().height(height);
        statisticsView.add();
        statisticsView.row().height(height);
        statisticsView.add(levelTries).width(width);
        statisticsView.row().height(height);
        statisticsView.add();
        statisticsView.row().height(height);
        statisticsView.add(successfulLevelTries).width(width);
        statisticsView.row().height(height);
        statisticsView.add();
        statisticsView.row().height(height);
        statisticsView.add(timePlayed).width(width);
        
         //backButton
         ImageButton backButton = new ImageButton(manager.get(skin, Skin.class), "backButton");
         Container<ImageButton> buttonContainer = new Container<ImageButton>();
         buttonContainer.pad(space * 5 / 2).maxSize(getStage().getHeight() / 5);
         buttonContainer.align(Align.bottomLeft);
         buttonContainer.setActor(backButton);
         backButton.addListener(new backClickListener());
         getStage().addActor(buttonContainer);
         buttonContainer.setFillParent(true);
         changedProfileList();
        
    }
    public void show() {
        super.show();
        lambsEnchanted.setText("Lambs enchanted = "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getLambsEnchanted()));
        gemsEnchanted.setText("Gems enchanted = "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getGemsEnchanted()));
        gemsPlaced.setText("Gems placed = "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getGemsPlaced()));
        lambsPlaced.setText("Lambs placed = "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getLambsPlaced()));
        levelTries.setText("Level tries = "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getLevelTries()));
        successfulLevelTries.setText("Successful level tries = "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getSuccessfulLevelTries()));
        timePlayed.setText("Time played = "+ Long.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getTimePlayed()));
        
        
     }
    @Override
    public void changedProfile() {
        
       
    }
    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            getGame().setScreen(MainMenuViewController.class);
        }
    }
}
