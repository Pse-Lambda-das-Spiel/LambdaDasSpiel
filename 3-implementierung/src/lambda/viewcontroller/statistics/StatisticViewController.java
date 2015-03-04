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
	private TextButton levelCompleted;
	private TextButton hintsNotUsed;
	private TextButton timePlayed;
	I18NBundle language ;
	
	//Label label = new Label("Initial string", skin, "robotoLight");
	
	private AssetManager manager;
	 private final float space;

	public StatisticViewController() {
		ProfileManager.getManager().addObserver(this);
		space = getStage().getWidth() / 64;
		statistics = new StatisticModel();
	}

    @Override
    public void queueAssets(AssetManager assets) {
    	assets.load(skin, Skin.class,
                new SkinLoader.SkinParameter("data/skins/MasterSkin.atlas"));
        assets.load("data/backgrounds/default.png", Texture.class, new TextureParameter());
    }
 

    @Override
    public void create(final AssetManager manager) {
    	this.manager = manager;
    	 Image background = new Image(manager.get("data/backgrounds/default.png", Texture.class));
         background.setWidth(getStage().getWidth());
         background.setHeight(getStage().getHeight());
         getStage().addActor(background);
         
         lambsEnchanted = new TextButton("", manager.get(skin, Skin.class));
         gemsEnchanted = new TextButton("", manager.get(skin, Skin.class));
         gemsPlaced = new TextButton("", manager.get(skin, Skin.class));
         lambsPlaced = new TextButton("", manager.get(skin, Skin.class));
         levelCompleted = new TextButton("", manager.get(skin, Skin.class));
         hintsNotUsed = new TextButton("", manager.get(skin, Skin.class));
         timePlayed = new TextButton("", manager.get(skin, Skin.class));
         
         
         Table statisticsView = new Table();
         statisticsView.align(Align.top);
         getStage().addActor(statisticsView);
         statisticsView.setFillParent(true);
         float height = getStage().getHeight() / 15;
         float width = getStage().getWidth() * 0.7f;
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
        statisticsView.add(levelCompleted).width(width);
        statisticsView.row().height(height);
        statisticsView.add();
        statisticsView.row().height(height);
        statisticsView.add(hintsNotUsed).width(width);
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
        lambsEnchanted.setText(language.get("lambsEnchantedAchievementLabel") + " : " + Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getLambsEnchanted())) ;
        gemsEnchanted.setText(language.get("lambsEnchantedAchievementLabel") + " : "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getGemsEnchanted()));
        gemsPlaced.setText(language.get("gemsEnchantedAchievementLabel") + " : " + Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getGemsPlaced()));
        lambsPlaced.setText(language.get("lambsPlacedAchievementLabel") + " : " + Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getLambsPlaced()));
        levelCompleted.setText(language.get("levelAchievementLabel") + " : "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getLevelCompleted()));
        hintsNotUsed.setText(language.get("hintsAchievementLabel") + " : "+ Integer.toString(  ProfileManager.getManager().getCurrentProfile().getStatistics().getHintsNotUsed()));
        timePlayed.setText(language.get("timeAchievementLabel") + " : " + statistics.convert(  ProfileManager.getManager().getCurrentProfile().getStatistics().getTimePlayed()));
        
        
     }
    @Override
    public void changedProfile() {
    	statistics.update();
    	statistics = ProfileManager.getManager().getCurrentProfile().getStatistics() ; 
    	language = manager.get(ProfileManager.getManager().getCurrentProfile().getLanguage(), 
				I18NBundle.class);
    	
    }
    
    private class backClickListener extends ClickListener {
        @Override
        public void clicked(InputEvent event, float x, float y) {
            AudioManager.playSound("buttonClick");
            getGame().setScreen(SettingsViewController.class);
        }
    }
}
