package lambda.viewcontroller.editor;

import lambda.model.levels.LevelContext;
import lambda.model.levels.LevelModel;
import lambda.viewcontroller.lambdaterm.LambdaTermViewController;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
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
     * @param skin
     *            Dialogskin
     * @param context
     *            The TargetDialog shows the target/goal given is this
     *            LevelContext
     * @param stage
     *            Stage in which the Dialog will be shown.
     */
    public TargetDialog(Skin skin, I18NBundle language, LevelContext context,
            Stage stage) {
        super("", skin);
        clear();
        EditorViewController.disableDragAndDrop();
        align(Align.top);
        setFillParent(true);
        LevelModel level = context.getLevelModel();
        Label goalLabel = new Label(
                language.get(level.isStandardMode() ? "goalDialog"
                        : "reverseGoalDialog"), skin);
        float scaleFactor = stage.getWidth()
                * 2
                / 3
                / goalLabel.getStyle().font.getBounds(goalLabel.getText()).width;
        if (scaleFactor < 1) {
            goalLabel.setFontScale(scaleFactor);
        }
        goalLabel.setFontScale(stage.getHeight() / 720 * goalLabel.getFontScaleY());
        float pad = stage.getHeight() / 15;
        Cell<Label> goalCell = add(goalLabel).width(stage.getWidth() * 2 / 3)
                .padTop(pad);
        goalLabel.setAlignment(Align.center);
        LambdaTermViewController goal = LambdaTermViewController.build(
                level.isStandardMode() ? level.getGoal() : level.getStart(),
                false, context, stage);
        goal.toBack();
        goal.setPosition((stage.getWidth() - goal.getWidth()) / 2,
                stage.getHeight() - goalCell.getPrefHeight() - 2 * pad
                        - goal.getBlockSize());
        addActor(goal);
        addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                EditorViewController.enableDragAndDrop();
                remove();
            }
        });
    }

}
