package lambda.viewcontroller.editor;

import lambda.model.levels.LevelContext;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * The hint-dialog of the EditorViewController
 *
 * @author Kai Fieger
 */
public class HintDialog extends Dialog {

    /**
     * Creates a new hint-dialog.
     *
     * @param skin
     *            Dialogskin
     * @param context
     *            The HintDialog shows the hint given is this LevelContext
     * @param stage
     *            Stage in which the Dialog will be shown.
     */
    public HintDialog(Skin skin, I18NBundle language, LevelContext context,
            Stage stage) {
        super("", skin);
        clear();
        setFillParent(true);
        if (context.getLevelModel().getHint().getChild() == null) {
            Label noHint = new Label(language.get("noHint"), skin);
            noHint.setWrap(true);
            add(noHint).width(stage.getWidth() * 0.8f);
        } else {
            LambdaTermViewController hint = LambdaTermViewController.build(
                    context.getLevelModel().getHint(), false, context, stage);
            hint.toBack();
            hint.setPosition((stage.getWidth() - hint.getWidth()) / 2,
                    stage.getHeight() * 0.7f);
            addActor(hint);
        }
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }
}
