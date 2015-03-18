package lambda.viewcontroller.editor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * The helpdialog of the editormode.
 * 
 * @author Kai Fieger
 */
public class HelpDialog extends Dialog {

    /**
     * Creates a new editor-helpdialog.
     *
     * @param skin Dialogskin
     * @param language Language in which the help is presented.
     * @param stage Stage in which the Dialog will be shown.
     */
    public HelpDialog(Skin skin, I18NBundle language, Stage stage) {
        super("", skin);
        clear();
        pad(stage.getHeight() / 20);
        float size = stage.getHeight() / 8;
        add(new Image(skin.getAtlas().createSprite("pause"))).size(size); //TODO change to gamePause image
        Label pauseGameHelp = new Label(language.get("pauseGameHelp"), skin);
        pauseGameHelp.setFontScale(0.7f);
        add(pauseGameHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("goal"))).size(size);
        Label goalHelp = new Label(language.get("goalHelp"), skin);
        goalHelp.setFontScale(0.7f);
        add(goalHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("info"))).size(size);
        Label infoHelp = new Label(language.get("infoHelp"), skin);
        infoHelp.setFontScale(0.7f);
        add(infoHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("play"))).size(size);
        Label goToRedHelp = new Label(language.get("goToRedHelp"), skin);
        goToRedHelp.setFontScale(0.7f);
        add(goToRedHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("strategy"))).size(size);
        Label strategyHelp = new Label(language.get("strategyHelp"), skin);
        strategyHelp.setFontScale(0.7f);
        add(strategyHelp);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }
    
}
