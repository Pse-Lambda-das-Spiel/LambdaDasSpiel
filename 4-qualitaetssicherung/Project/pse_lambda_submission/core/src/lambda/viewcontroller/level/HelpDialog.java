package lambda.viewcontroller.level;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
     * @param skin
     *            Dialogskin
     * @param language
     *            Language in which the help is presented.
     * @param stageWidth
     *            Width of the stage on which the dialog should be shown.
     * @param stageHeight
     *            Height of the stage on which the dialog should be shown.
     */
    public HelpDialog(Skin skin, I18NBundle language, float stageWidth,
            float stageHeight) {
        super("", skin);
        clear();
        pad(stageHeight / 20);
        defaults().space(stageHeight / 72);
        float labelWidth = stageWidth / 2;
        float size = stageHeight / 8;
        add(new Image(skin.getAtlas().createSprite("back"))).size(size);
        add(new Image(skin.getAtlas().createSprite("next"))).size(size);
        Label selectLevelHelp = new Label(language.get("selectLevelHelp"), skin);
        add(selectLevelHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("level_completed1"))).size(size).colspan(2);
        Label completedLevelHelp = new Label(
                language.get("completedLevelHelp"), skin);
        add(completedLevelHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("level_unlocked1"))).size(size).colspan(2);
        Label unlockedLevelHelp = new Label(language.get("unlockedLevelHelp"),
                skin);
        add(unlockedLevelHelp).width(labelWidth).height(size);
        row();
        Label levelColorHelp = new Label(language.get("levelColorHelp"), skin);
        add(levelColorHelp).colspan(3).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("level_locked"))).size(size).colspan(2);
        Label lockedLevelHelp = new Label(language.get("lockedLevelHelp"), skin);
        add(lockedLevelHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("sandbox"))).size(size).colspan(2);
        Label sandboxHelp = new Label(language.get("sandboxHelp"), skin);
        add(sandboxHelp).width(labelWidth).height(size);
        
        Label[] labels = {
                selectLevelHelp, completedLevelHelp, unlockedLevelHelp, levelColorHelp, lockedLevelHelp, sandboxHelp};
        float smallestScale = Float.POSITIVE_INFINITY;
        for (Label label : labels) {
            float current = labelWidth
                    / label.getStyle().font.getBounds(label.getText()).width;
            if (current < smallestScale) {
                smallestScale = current;
            }
        }
        for (Label label : labels) {
            label.setFontScale(smallestScale);
            label.setAlignment(Align.center);
        }
        
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }

}