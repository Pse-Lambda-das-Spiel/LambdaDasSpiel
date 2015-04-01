package lambda.viewcontroller.level;

import lambda.model.levels.TutorialMessageModel;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * This class represents a full tutorial including the area and vector
 *
 * @author Kay Schmitteckert, Robert Hochweiss
 */
public class TutorialMessage extends Dialog {

    /**
     * Creates a new TutorialMessage.
     *
     * @param model
     *            The TutorialModel of this message
     * @param skin
     *            Dialogskin
     * @param language
     *            Language in which the tutorial is presented.
     * @param stageHeight
     *            Height of the stage on which the dialog should be shown.
     * @param stageWidth
     *            Width of the stage on which the dialog should be shown.
     */
    public TutorialMessage(TutorialMessageModel model, Skin skin,
            I18NBundle language, float stageHeight, float stageWidth) {
        super("", skin);
        clear();
        pad(stageHeight / 20);
        Label message = new Label(language.get(model.getId()), skin);
        message.setWrap(true);
        if (model.getImageName().equals("")) {
            message.setFontScale(0.7f);
            add(message).width(stageWidth / 2);
            message.setFontScale(stageHeight / 720 * message.getFontScaleY());
        } else {
            float scaleFactorX = stageWidth / 1280;
            float scaleFactorY = stageHeight / 720;
            Image tutorialImage = new Image(skin.getAtlas().createSprite(model.getImageName()));
            add(tutorialImage).size(tutorialImage.getWidth() * scaleFactorX, tutorialImage.getHeight() * scaleFactorY)
                    .row();
            message.setFontScale(0.7f);
            add(message).width(stageWidth / 2);
            message.setFontScale(stageHeight / 720 * message.getFontScaleY());
        }
    }

}
