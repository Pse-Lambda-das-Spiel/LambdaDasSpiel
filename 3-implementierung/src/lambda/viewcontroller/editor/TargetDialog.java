package lambda.viewcontroller.editor;

import lambda.model.levels.LevelContext;
import lambda.model.levels.LevelModel;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;
import lambda.viewcontroller.lambdaterm.LambdaValueViewController;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.I18NBundle;

/**
 * The target-dialog of the EditorViewController. It shows the goal of the
 * level.
 *
 * @author Kai Fieger
 */
public class TargetDialog extends Dialog {

    /**
     * Creates a new target-dialog.
     *
     * @param skin Dialogskin
     * @param context The TargetDialog shows the target/goal given is this
     * LevelContext
     * @param stage Stage in which the Dialog will be shown.
     */
    public TargetDialog(Skin skin, I18NBundle language, LevelContext context, Stage stage) {
        super("", skin);
        clear();
        align(Align.top);
        setFillParent(true);
        LevelModel level = context.getLevelModel();
        String key = level.isStandardMode() ? "goalDialog" : "reverseGoalDialog";
        Label goalLabel = new Label(language.get(key), skin);
        float pad = stage.getHeight() / 15;
        add(goalLabel).padTop(pad);
        LambdaTermViewController goal = LambdaTermViewController.build(
                level.isStandardMode() ? level.getGoal() : level.getStart(), false, context, stage);
        goal.toBack();
        goal.setPosition((stage.getWidth() - goal.getWidth()) / 2, stage.getHeight()
                - goalLabel.getHeight() - 2 * pad - LambdaValueViewController.BLOCK_HEIGHT);
        addActor(goal);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                remove();
            }
        });
    }

}
