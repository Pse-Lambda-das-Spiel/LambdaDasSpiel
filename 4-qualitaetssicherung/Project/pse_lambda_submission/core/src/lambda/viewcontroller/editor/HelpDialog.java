package lambda.viewcontroller.editor;

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
 * The helpdialog of the editormode.
 * 
 * @author Kai Fieger
 */
public class HelpDialog extends Dialog {

    /**
     * Creates a new editor-helpdialog.
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
        EditorViewController.disableDragAndDrop();
        pad(stage.getHeight() / 20);
        defaults().space(stage.getHeight() / 72);
        float size = stage.getHeight() / 8;
        float labelWidth = stage.getWidth() / 2;
        add(new Image(skin.getAtlas().createSprite("pause"))).size(size);
        Label pauseGameHelp = new Label(language.get("pauseGameHelp"), skin);
        add(pauseGameHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("goal"))).size(size);
        Label goalHelp = new Label(language.get("goalHelp"), skin);
        add(goalHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("info"))).size(size);
        Label infoHelp = new Label(language.get("infoHelp"), skin);
        add(infoHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("play"))).size(size);
        Label goToRedHelp = new Label(language.get("goToRedHelp"), skin);
        add(goToRedHelp).width(labelWidth).height(size);
        row();
        add(new Image(skin.getAtlas().createSprite("strategy"))).size(size);
        Label strategyHelp = new Label(language.get("strategyHelp"), skin);
        add(strategyHelp).width(labelWidth).height(size);

        Label[] labels = {
pauseGameHelp, goalHelp, infoHelp, goToRedHelp,
                strategyHelp };
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
                EditorViewController.enableDragAndDrop();
                remove();
            }
        });
    }

}
