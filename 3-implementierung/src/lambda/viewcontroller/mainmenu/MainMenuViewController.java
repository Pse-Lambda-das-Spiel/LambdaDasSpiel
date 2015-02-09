package lambda.viewcontroller.mainmenu;

import lambda.LambdaGame;
import lambda.model.profiles.ProfileManager;
import lambda.viewcontroller.ViewController;
import lambda.viewcontroller.settings.SettingsViewController;
import lambda.viewcontroller.shop.ShopViewController;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class MainMenuViewController  implements Screen {	
	 private Stage stage = new Stage();
	    private Table table = new Table();
	    private LambdaGame game;
	   
	    
	    private Skin skin = new Skin(Gdx.files.internal("data/skins/mainMenuSkin.json"),
	                new TextureAtlas(Gdx.files.internal("data/skins/MasterSkin.atlas")));
	    private ImageButton backButton = new ImageButton(skin,"backButton"),
	    settingsButton = new ImageButton(skin,"settings"),
	     
	    		sound_unmuted = new ImageButton(skin , "sound_unmuted"),
	            sound_muted = new ImageButton(skin , "sound_muted");
	    private Label coins;
	    private Image coinBar;
	    MainMenuViewController(LambdaGame game){
	    	this.game = game;
	    }
	    @Override
	    public void render(float delta) {
	        Gdx.gl.glClearColor(0, 0, 0, 1);
	        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	        stage.act();
	        stage.draw();
	    }

	    @Override
	    public void resize(int width, int height) {
	    }

	    @Override
	    public void show() {
	    	Container<ImageButton> backButtonContainer = new Container<ImageButton>();
	    	backButtonContainer.pad(25);
	    	backButtonContainer.align(Align.topLeft);
	    	backButtonContainer.setActor(backButton);
	        
	        stage.addActor(backButtonContainer);
	        backButtonContainer.setFillParent(true);
	        
	        
	        
	      Container<ImageButton> settingsButtonContainer = new Container<ImageButton>();
	        settingsButtonContainer.pad(15);
	        settingsButtonContainer.align(Align.bottomLeft);
	        settingsButtonContainer.setActor(settingsButton);
	        
	        stage.addActor(settingsButtonContainer);
	        settingsButtonContainer.setFillParent(true);
	        
	        
	        
	        Container<ImageButton> soundButtonContainer = new Container<ImageButton>();
	        soundButtonContainer.pad(15);
	        soundButtonContainer.align(Align.bottomRight);
	        soundButtonContainer.setActor(sound_unmuted);
	        
	        stage.addActor(soundButtonContainer);
	        soundButtonContainer.setFillParent(true);
	        
	      /*  coins = new Label(String.valueOf(ProfileManager.getManager().getCurrentProfile().getCoins()),
	                skin);
	        Container<Label> labelContainer = new Container();
	        labelContainer.align(Align.topRight);
	        labelContainer.setActor(coins);
*/
	        coinBar = new Image(skin, "coin_bar");
	        Container<Image> imageContainer = new Container();
	        imageContainer.align(Align.topRight);
	        imageContainer.setActor(coinBar);
	        stage.addActor(imageContainer);
	        imageContainer.setFillParent(true);
	    	backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	 Gdx.app.exit();

            }
        });
	    	settingsButton.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	            	game.setScreen(new SettingsViewController());

	            }
	        });
	        sound_unmuted.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	            	soundButtonContainer.setActor(sound_muted);

	            }
	        });
	    	sound_muted.addListener(new ClickListener() {
	            @Override
	            public void clicked(InputEvent event, float x, float y) {
	            	soundButtonContainer.setActor(sound_unmuted);

	            }
	        });
	        
	        

	       

	        

	        Gdx.input.setInputProcessor(stage);
	    }

	    @Override
	    public void hide() {
	        dispose();
	    }

	    @Override
	    public void pause() {
	    }

	    @Override
	    public void resume() {
	    }

	    @Override
	    public void dispose() {
	        stage.dispose();
	        skin.dispose();
	    }

		

}