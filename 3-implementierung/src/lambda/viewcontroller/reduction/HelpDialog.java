package lambda.viewcontroller.reduction;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * The helpdialog of the reductionmode.
 * 
 * @author Kai Fieger
 */
public class HelpDialog extends Dialog {
    
    /**
     * Creates a new reduction-helpdialog.
     *
     * @param skin Dialogskin
     * @param language Language in which the help is presented.
     * @param stageWidth Width of the stage on which the dialog should be shown.
     * @param stageHeight Height of the stage on which the dialog should be shown.
     */
    public HelpDialog(Skin skin, I18NBundle language, float stageWidth, float stageHeight) {
        super("", skin);
        clear();
        pad(stageHeight / 20);
        add(new Image(skin.getAtlas().createSprite("pause"))).size(stageHeight / 8); //TODO change to gamePause image
        Label pauseGameHelp = new Label(language.get("pauseGameHelp"), skin);
        pauseGameHelp.setFontScale(0.7f);
        add(pauseGameHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("info"))).size(stageHeight / 8);
        Label infoHelp = new Label(language.get("infoHelp"), skin);
        infoHelp.setFontScale(0.7f);
        add(infoHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("play"))).size(stageHeight / 8);
        Label playHelp = new Label(language.get("playHelp"), skin);
        playHelp.setFontScale(0.7f);
        add(playHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("pause"))).size(stageHeight / 8);
        Label pauseHelp = new Label(language.get("pauseHelp"), skin);
        pauseHelp.setFontScale(0.7f);
        add(pauseHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("prev"))).size(stageHeight / 8);
        Label prevHelp = new Label(language.get("prevHelp"), skin);
        prevHelp.setFontScale(0.7f);
        add(prevHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("forward"))).size(stageHeight / 8);
        Label forwardHelp = new Label(language.get("forwardHelp"), skin);
        forwardHelp.setFontScale(0.7f);
        add(forwardHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("reload"))).size(stageHeight / 8);
        Label reloadHelp = new Label(language.get("reloadHelp"), skin);
        reloadHelp.setFontScale(0.7f);
        add(reloadHelp);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }
    
}
