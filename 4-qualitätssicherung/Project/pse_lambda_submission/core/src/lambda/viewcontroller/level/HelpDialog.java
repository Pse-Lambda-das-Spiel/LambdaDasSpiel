package lambda.viewcontroller.level;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * The helpdialog of the levelselection.
 * 
 * @author Kai Fieger
 */
public class HelpDialog extends Dialog {

    /**
     * Creates a new levelselection-helpdialog.
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
        add(new Image(skin.getAtlas().createSprite("back"))).size(stageHeight / 8);
        add(new Image(skin.getAtlas().createSprite("next"))).size(stageHeight / 8);
        Label selectLevelHelp = new Label(language.get("selectLevelHelp"), skin);
        selectLevelHelp.setFontScale(0.7f);
        add(selectLevelHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("level_completed1"))).size(stageHeight / 8).colspan(2);
        Label completedLevelHelp = new Label(language.get("completedLevelHelp"), skin);
        completedLevelHelp.setFontScale(0.7f);
        add(completedLevelHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("level_unlocked1"))).size(stageHeight / 8).colspan(2);
        Label unlockedLevelHelp = new Label(language.get("unlockedLevelHelp"), skin);
        unlockedLevelHelp.setFontScale(0.7f);
        add(unlockedLevelHelp);
        row();
        Label levelColorHelp = new Label(language.get("levelColorHelp"), skin);
        levelColorHelp.setFontScale(0.7f);
        add(levelColorHelp).colspan(3);
        row();
        add(new Image(skin.getAtlas().createSprite("level_locked"))).size(stageHeight / 8).colspan(2);
        Label lockedLevelHelp = new Label(language.get("lockedLevelHelp"), skin);
        lockedLevelHelp.setFontScale(0.7f);
        add(lockedLevelHelp);
        row();
        add(new Image(skin.getAtlas().createSprite("sandbox"))).size(stageHeight / 8).colspan(2);
        Label sandboxHelp = new Label(language.get("sandboxHelp"), skin);
        sandboxHelp.setFontScale(0.7f);
        add(sandboxHelp);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }
    
}