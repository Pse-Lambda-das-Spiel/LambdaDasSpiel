package lambda.viewcontroller.editor;

import lambda.model.levels.LevelContext;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The target-dialog of the EditorViewController. It shows the goal of the level.
 * 
 * @author Kai Fieger
 */
public class TargetDialog extends Dialog {

    /**
     * Creates a new target-dialog.
     * 
     * @param skin Dialogskin
     * @param context The TargetDialog shows the target/goal given is this LevelContext
     * @param stage Stage in which the Dialog will be shown.
     */
    public TargetDialog(Skin skin, LevelContext context, Stage stage) {
        super("", skin);
        clear();
        setFillParent(true);
        LambdaTermViewController goal = LambdaTermViewController.build(context.getLevelModel().getGoal(), false, context, stage, false);
        goal.toBack();
        goal.setPosition((stage.getWidth() - goal.getWidth()) / 2, stage.getHeight() * 0.8f);
        addActor(goal);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }
    
}
