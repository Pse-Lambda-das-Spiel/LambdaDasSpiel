package lambda.viewcontroller.reduction;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
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
     * @param skin
     *            Dialogskin
     * @param language
     *            Language in which the help is presented.
     * @param stage
     *            Stage in which the Dialog will be shown.
     */
    public HelpDialog(Skin skin, I18NBundle language, Stage stage) {
        super("", skin);
        clear();
        pad(stage.getHeight() / 20);
        defaults().space(stage.getHeight() / 72);
        float size = stage.getHeight() / 8;
        float labelWidth = stage.getWidth() / 2;
        add(new Image(skin.getAtlas().createSprite("pause"))).size(size); // TODO
                                                                          // change
                                                                          // to
                                                                          // gamePause
                                                                          // image
        Label pauseGameHelp = new Label(language.get("pauseGameHelp"), skin);
        add(pauseGameHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("play"))).size(size);
        Label playHelp = new Label(language.get("playHelp"), skin);
        add(playHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("pause"))).size(size);
        Label pauseHelp = new Label(language.get("pauseHelp"), skin);
        add(pauseHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("prev"))).size(size);
        Label prevHelp = new Label(language.get("prevHelp"), skin);
        add(prevHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("forward"))).size(size);
        Label forwardHelp = new Label(language.get("forwardHelp"), skin);
        add(forwardHelp).width(labelWidth).height(size);
        row();

        Label[] labels = {
                pauseGameHelp, playHelp, pauseHelp, prevHelp, forwardHelp };
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
