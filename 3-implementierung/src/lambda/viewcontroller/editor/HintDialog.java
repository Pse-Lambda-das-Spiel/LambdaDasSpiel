package lambda.viewcontroller.editor;

import lambda.model.levels.LevelContext;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The hint-dialog of the EditorViewController
 * 
 * @author Kai Fieger
 */
public class HintDialog extends Dialog {

    /**
     * Creates a new hint-dialog.
     * 
     * @param skin Dialogskin
     * @param context The HintDialog shows the hint given is this LevelContext
     * @param stage Stage in which the Dialog will be shown.
     */
    public HintDialog(Skin skin, LevelContext context, Stage stage) {
        super("", skin);
        clear();
        pad(stage.getHeight() / 48);
        LambdaTermViewController hint = LambdaTermViewController.build(context.getLevelModel().getHint(), false, context, stage, false);
        hint.addOffset(0.0f, 300.0f); // TODO number
        add(hint);
        hint.toBack();
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }
}
